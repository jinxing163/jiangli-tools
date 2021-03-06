package com.jinxing.utils;


import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 常用批量查询 producer:->List<T> 生产者列表 srcIdAggregator:T->ID 生产者id提取器 requestHandler:List<ID>->Iterable<RS>  请求执行者
 * resultIdExtractor:RS->ID 结果id提取器 resultHandler:RS,T 结果消费者
 * <p>
 * 请求中返回null和根本没返回结果是区别对待的 比如传入参数 1，2,3 只返回了 2的结果 那么1,3属于 notAccessedKeys范畴； 2可能为null，但也是个结果，所以在resultHandler中需要进行非空判断，并记录日志
 *
 * @author Jiangli
 * @date 2018/2/1 13:36
 */
public class PagedRequester {
    public static final int COMMON_PAGE_SIZE = 20;

    public static Supplier<Integer> intSupplier(Supplier<String> stringSupplier) {
        String s = stringSupplier.get();
        return () -> getInt(s);
    }

    public static Integer getInt(String s) {
        Integer ret1 = null;
        if (s != null) {
            try {
                ret1 = Integer.parseInt(s);
            } catch (Exception e) {
            }
        }
        return ret1;
    }

    public static Integer getInt(Long s) {
        Integer ret1 = null;
        if (s != null) {
            try {
                ret1 = s.intValue();
            } catch (Exception e) {
            }
        }
        return ret1;
    }

    public static <T> Function<T, Integer> intFunction(Function<T, String> stringSupplier) {
        return stringSupplier.andThen(PagedRequester::getInt);
    }

    public static <T> Function<T, Integer> intFunctionFromLong(Function<T, Long> stringSupplier) {
        return stringSupplier.andThen(PagedRequester::getInt);
    }

    public static <T> Function<T, Long> longFunction(Function<T, String> stringSupplier) {
        return stringSupplier.andThen(PagedRequester::getLong);
    }

    public static Supplier<Long> longSupplier(Supplier<String> stringSupplier) {
        String s = stringSupplier.get();
        return () -> getLong(s);
    }

    public static Long getLong(String s) {
        Long ret1 = null;
        if (s != null) {
            try {
                ret1 = Long.parseLong(s);
            } catch (Exception e) {
            }
        }
        return ret1;
    }

    public static <T, ID, RS> RequestResult<ID> processListOne(
            T producer,
            Function<T, ID> srcIdAggregator,
            Function<ID, RS> requestHandler,
            Function<RS, ID> resultIdExtractor,
            BiConsumer<RS, T> resultHandler
    ) {

        return processBatchList(
                1
                , () -> Collections.singletonList(producer)
                , srcIdAggregator
                , idList -> {
                    List<RS> ret = new ArrayList<>();
                    idList.forEach(o -> ret.add(requestHandler.apply(o)));
                    return ret;
                }
                , resultIdExtractor
                , resultHandler
        );
    }

    public static <T, ID, RS> RequestResult<ID> processListSeqOne(
            T producer,
            Function<T, ID> srcIdAggregator,
            Function<ID, RS> requestHandler,
            BiConsumer<RS, T> resultHandler
    ) {

        return processBatchListSequential(
                1
                , () -> Collections.singletonList(producer)
                , srcIdAggregator
                , idList -> {
                    List<RS> ret = new ArrayList<>();
                    idList.forEach(o -> ret.add(requestHandler.apply(o)));
                    return ret;
                }
                , resultHandler
        );
    }

    public static <T, ID, MID, MRS> RequestResult<ID> processMapOne(
            T producer,
            Function<T, ID> srcIdAggregator,
            Function<ID, ? extends Map<MID, MRS>> requestHandler,
            Function<MID, ID> idMapper,
            BiConsumer<MRS, T> resultHandler
    ) {

        return processBatchMap(
                1
                , () -> Collections.singletonList(producer)
                , srcIdAggregator
                , idList -> {
                    Map<MID, MRS> ret = new HashMap<>();
                    idList.forEach(o -> {
                        Map<? extends MID, ? extends MRS> apply = requestHandler.apply(o);
                        if (apply != null) {
                            ret.putAll(apply);
                        }
                    });
                    return ret;
                }
                , idMapper
                , resultHandler
        );
    }

    public static <T, ID, MID, MRS> RequestResult<ID> processBatchMap(
            int pageSize,
            Supplier<List<T>> producer,
            Function<T, ID> srcIdAggregator,
            Function<List<ID>, ? extends Map<MID, MRS>> requestHandler,
            Function<MID, ID> idMapper,
            BiConsumer<MRS, T> resultHandler
    ) {
        return processBatch(
                pageSize,
                producer,
                srcIdAggregator,
                (idBulk, idListMap) -> {
                    Map<MID, MRS> resultBulk = requestHandler.apply(idBulk);

                    if (resultBulk != null) {
                        resultBulk.forEach((mid, mrs) -> {
                            if (mid != null) {
                                //自行判断 mrs null
                                ID rId = idMapper.apply(mid);
                                List<T> tList = idListMap.get(rId);
                                if (tList != null) {
                                    //mrs @Nullable
                                    tList.forEach(t -> resultHandler.accept(mrs, t));
                                }
                            }
                        });
                    }
                }
        );
    }

    //RS Nullable
    public static <ID, RS> List<RS> processPage(
            int pageSize,
            Supplier<List<ID>> producer,
            Function<List<ID>, RS> requestHandler
    ) {
        List<RS> ret = new ArrayList<>();

        PagedRequester.processBatch(
                pageSize,
                producer,
                id -> id,
                (ids, longListMap) -> {
                    RS apply = requestHandler.apply(ids);
                    ret.add(apply);
                });

        return ret;
    }

    //RS NonNullable
    public static <ID, RS extends Map<K, V>, K, V> Map<K, V> processPageMap(
            int pageSize,
            Supplier<List<ID>> producer,
            Function<List<ID>, RS> requestHandler) {
        Map<K, V> ret = new HashMap<>();
        //Map<K,V> ret =collector.get();

        PagedRequester.processBatch(
                pageSize,
                producer,
                id -> id,
                (ids, longListMap) -> {
                    RS apply = requestHandler.apply(ids);
                    if (apply != null) {
                        ret.putAll(apply);
                    }
                });

        return ret;
    }

    //RS NonNullable
    public static <ID, RS extends Iterable<K>, K> List<K> processPageList(
            int pageSize,
            Supplier<List<ID>> producer,
            Function<List<ID>, RS> requestHandler) {
        List<K> ret = new ArrayList<>();
        //Map<K,V> ret =collector.get();

        PagedRequester.processBatch(
                pageSize,
                producer,
                id -> id,
                (ids, longListMap) -> {
                    RS apply = requestHandler.apply(ids);
                    if (apply != null) {
                        for (K k : apply) {
                            if (k != null) {
                                ret.add(k);
                            }
                        }
                    }
                });

        return ret;
    }

    public static <T, ID> RequestResult<ID> processBatch(
            int pageSize,
            Supplier<List<T>> producer,
            Function<T, ID> srcIdAggregator,
            BiConsumer<List<ID>, Map<ID, List<T>>> handler
    ) {
        RequestResult<ID> ret = new RequestResult<ID>();
        if (producer == null) {
            return ret;
        }

        List<T> list = producer.get();
        if (list == null || list.size() == 0) {
            return ret;
        }

        //extract
        List<ID> idList = new ArrayList<>(list.size());
        RecordableHashMap<ID, List<T>> idTMap = new RecordableHashMap<>();
        for (T t : list) {
            if (t == null) {
                continue;
            }
            ID id = srcIdAggregator.apply(t);
            if (id != null && !idList.contains(id)) {
                idList.add(id);
            }

            if (id != null) {
                List<T> tList = idTMap.get(id);
                if (tList == null) {
                    tList = new ArrayList<>();
                    idTMap.put(id, tList);
                }
                tList.add(t);
            }
        }

        //request
        //int pageIndex=0;
        if (pageSize < 1) {
            pageSize = COMMON_PAGE_SIZE;
        }

        int totalPage = idList.size() / pageSize;
        if (idList.size() % pageSize != 0) {
            totalPage += 1;
        }


        idTMap.recording();

        //分页
        for (int pageIndex = 0; pageIndex < totalPage; pageIndex++) {
            int toIndex = pageIndex * pageSize + pageSize;
            if (toIndex > idList.size()) {
                toIndex = idList.size();
            }

            List<ID> idBulk = new ArrayList<>(pageSize);
            for (int i = pageIndex * pageSize; i < toIndex; i++) {
                idBulk.add(idList.get(i));
            }

            //没有序列化错误
            //List<ID> idBulk = idList.subList(pageIndex * pageSize, toIndex);

            handler.accept(idBulk, idTMap);
        }

        //只返回了部分请求的id
        ret.notAccessedKeys = idTMap.notAccessedKeys();
        ret.shouldHaveKeys = idTMap.shouldHaveKeys();
        return ret;
    }


    /**
     * 批量查询List
     */
    public static <T, ID, RS> RequestResult<ID> processBatchList(
            int pageSize,
            Supplier<List<T>> producer,
            Function<T, ID> srcIdAggregator,
            Function<List<ID>, ? extends Iterable<RS>> requestHandler,
            Function<RS, ID> resultIdExtractor,
            BiConsumer<RS, T> resultHandler) {

        return processBatch(
                pageSize,
                producer,
                srcIdAggregator,
                (idBulk, idListMap) -> {
                    Iterable<RS> resultBulk = requestHandler.apply(idBulk);

                    if (resultBulk != null) {
                        resultBulk.forEach(rs -> {
                            //只能extract到Id才能对应上
                            if (rs != null) {
                                ID rId = resultIdExtractor.apply(rs);
                                List<T> tList = idListMap.get(rId);
                                if (tList != null) {
                                    //rs @NotNullable
                                    tList.forEach(t -> resultHandler.accept(rs, t));
                                }
                            }
                        });
                    }
                }
        );
    }

    public static <T, ID, RS> RequestResult<ID> processBatchListSequential(
            int pageSize,
            Supplier<List<T>> producer,
            Function<T, ID> srcIdAggregator,
            Function<List<ID>, ? extends Iterable<RS>> requestHandler,
            BiConsumer<RS, T> resultHandler
    ) {

        return processBatch(
                pageSize,
                producer,
                srcIdAggregator,
                (idBulk, idListMap) -> {
                    int n = 0;
                    Iterable<RS> resultBulk = requestHandler.apply(idBulk);

                    if (resultBulk != null) {
                        for (RS rs : resultBulk) {
                            //if (rs != null) {
                            ID rId = idBulk.get(n);
                            List<T> tList = idListMap.get(rId);
                            if (tList != null) {
                                //rs @Nullable
                                tList.forEach(t -> resultHandler.accept(rs, t));
                            }
                            //}
                            n++;
                        }
                    }
                }
        );
    }


    static class RecordableHashMap<K, V> extends HashMap<K, V> {
        private boolean record = false;
        private Set<K> accessedKey = new HashSet<K>();
        private Set<K> accessedNullObjectKey = new HashSet<K>();

        public void recording() {
            record = true;
        }

        @Override
        public V get(Object key) {
            V v = super.get(key);
            if (record && key != null) {
                try {
                    accessedKey.add((K) key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (record && v == null) {
                try {
                    accessedNullObjectKey.add((K) key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return v;
        }

        public Set<K> notAccessedKeys() {
            HashSet<K> ret = new HashSet<>();
            ret.addAll(keySet());
            ret.removeAll(accessedKey);
            return ret;
        }

        public Set<K> shouldHaveKeys() {
            return accessedNullObjectKey;
        }
    }

    public static class RequestResult<K> {
        //left
        private Set<K> notAccessedKeys;
        //right
        private Set<K> shouldHaveKeys;

        //左连接
        //代表服务器端根本没有返回这些id，连个null都没有
        public void listenLeftIds(BiConsumer<Set<K>, RequestResult<K>> consumer) {
            if (notAccessedKeys != null && consumer != null && notAccessedKeys.size() > 0) {
                consumer.accept(notAccessedKeys, this);
            }
        }

        //右连接
        //代表服务器端返回的这些id，我们本地没有，是不是出现了逻辑问题
        public void listenRightIds(BiConsumer<Set<K>, RequestResult<K>> consumer) {
            if (shouldHaveKeys != null && consumer != null && shouldHaveKeys.size() > 0) {
                consumer.accept(shouldHaveKeys, this);
            }
        }
    }


    public static void main(String[] args) {
        List<Long> idList = new ArrayList<>();
        int i = 101;
        for (int j = 0; j < i; j++) {
            idList.add((long) j);
        }

        //测试去重
        for (int j = 0; j < i; j++) {
            idList.add((long) j);
        }
        System.out.println("idList:" + idList);

        //
        //PagedRequester.processBatch(
        //    20,
        //    () -> idList,
        //    longId ->longId,
        //    (ids, longListMap) -> {
        //        System.out.println(ids);
        //    });

        Map<Long, Object> totalMap = PagedRequester.processPageMap(
                20,
                () -> idList,
                (ids) -> {
                    System.out.println("本页请求的ids:" + ids);
                    Map<Long, Object> ret = new HashMap<>();
                    ids.forEach(id -> ret.put(id, "id rs:" + id));
                    return ret;
                });

        System.out.println("合成的map:");
        System.out.println(totalMap);

        System.out.println("-------------------------------");

        List<Object> total = PagedRequester.processPageList(
                20,
                () -> idList,
                (ids) -> {
                    System.out.println("本页请求的ids:" + ids);
                    List<Object> ret = new ArrayList<>();
                    ids.forEach(id -> ret.add("id rs:" + id));
                    return ret;
                });

        System.out.println("合成的list:");
        System.out.println(total);


//        PagedRequester.<ClassLeaderDto, Long, Long, UserQueryDto>processBatchMap(
//                50,
//                () -> list,
//                ClassLeaderDto::getUserLongId,
//                longs -> userRemoteService.listByUserId(longs),
//                id -> id,
//                (userQueryDto, classLeaderDto) -> {
//                    classLeaderDto.setUserName(userQueryDto.getName());
//                    classLeaderDto.setUserAvatar(userQueryDto.getAvatar());
//                    classLeaderDto.setUserId(UserIdHashHelper.longToHash(userQueryDto.getId()));
//                }
//        );


    }


}
