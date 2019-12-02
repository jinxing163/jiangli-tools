package com.jinxing.helper;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页查询工具类 默认20条 查询一次
 * pageSize 每次查询条数
 * function 查询接口
 * s 需要查询的数据源（会去重）
 * consumer 消费者
 *
 * 示例：
 *         PageHelper.getResultBatch(
 *               2,
 *               longs ->userCourseOpenServiceForTeachManage.queryUserIdByClassIdsBatch(longs),
 *               classIds,
 *               map->{
 *                   if(MapUtils.isNotEmpty(map)){
 *                        resultBatch1.putAll(map);
 *                    }
 *                }
 *        );
*/
public class PageHelper {

    private static final int COMMON_PAGE_SIZE = 20;
    public static <K extends Number,V>void getResultBatch(int pageSize, Function<List<K>,V> function, List<K> s, Consumer<V> consumer){
        //验证源数据
        if(pageSize<1){
            pageSize = COMMON_PAGE_SIZE;
        }
        if(CollectionUtils.isEmpty(s)){
            return ;
        }
        //数据源去重
        s = distinctSource(s);
        //分页查询
        int totalPage = s.size()/pageSize;
        if(s.size()%pageSize != 0){
            totalPage += 1;
        }
        for(int pageIndex=0;pageIndex<totalPage ;pageIndex++){
            int toIndex = pageIndex*pageSize + pageSize;
            if(toIndex>s.size()){
                toIndex =  s.size();
            }
            List<K> needList = new ArrayList<>();
            for(int i = pageIndex * pageSize ;i<toIndex;i++){
                needList.add(s.get(i));
            }
            V apply = function.apply(needList);
            consumer.accept(apply);
        }
    }

    private static  <K> List<K> distinctSource(List<K> s){
        Set<K> set = new HashSet<>(s);
        if(s.size() == set.size()){
            return s;
        }
        return s.stream().distinct().collect(Collectors.toList());
    }

    public static void main(String[] args) {
/*                Map<Long, List<Long>> resultBatch1 = new HashMap<>();
        PageHelper.getResultBatch(
                2,
                longs ->userCourseOpenServiceForTeachManage.queryUserIdByClassIdsBatch(longs),
                classIds,
                map->{
                    if(MapUtils.isNotEmpty(map)){
                        resultBatch1.putAll(map);
                    }
                }
        );
        return resultBatch1;*/
    }

}
