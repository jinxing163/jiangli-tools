package com.jinxing.utils;

import com.alibaba.druid.util.StringUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author JinXing
 * @date 2019/12/31 13:46
 */
public class BeanUtils {


    /**
     * 对象类型转换(字段类型、名称应相同，字段数可以不等)
     *
     * @param origin
     *         原对象
     * @param targetType
     *         目标类型
     *
     * @return T
     */
    public static <T> T transTo(Object origin, Class<T> targetType) {
        if (origin == null) {
            return null;
        }
        Assert.notNull(targetType, "targetType cannot be empty");
        T target = org.springframework.beans.BeanUtils.instantiate(targetType);
        org.springframework.beans.BeanUtils.copyProperties(origin, target);
        return target;
    }

    /**
     * 集合类型转换，转换返回泛型List集合
     *
     * @param collection
     *         集合
     * @param clazz
     *         class
     *
     * @return List<T>
     */
    public static <T> List<T> transTo(Collection<?> collection, Class<T> clazz) {
        Assert.notNull(clazz, "转换目标对象类型参数不能为空!");
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        List<T> target = new ArrayList<>();
        for (Object aCollection : collection) {
            T t = transTo(aCollection, clazz);
            if (t != null) {
                target.add(t);
            }
        }
        return target;
    }

    /**
     * 自定义类型转换器
     *
     * @param <S>
     *         origin
     * @param <T>
     *         target
     *
     * @author zhanglikun
     * @date 2016年4月19日 下午3:24:58
     */
    public static interface Convertor<S, T> {
        T convert(S origin);
    }

    /**
     * 使用自定义转换器转换列表，返回泛型List集合，转换过程中，过滤空值
     *
     * @param collection
     *         集合
     * @param convertor
     *         自定义转换器
     *
     * @return List<T>
     */
    public static <S, T> List<T> transTo(Collection<S> collection, Convertor<S, T> convertor) {
        Assert.notNull(convertor, "转换器不能为空!");
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        List<T> target = new ArrayList<>();
        for (S coll : collection) {
            T t = convertor.convert(coll);
            if (t != null) {
                target.add(t);
            }
        }
        return target;
    }

    /**
     * MAP类型转换为Bean
     *
     * @param map
     *         集合
     * @param type
     *         类型
     *
     * @return T
     */
    public static <T> T transTo(Map<String, ?> map, Class<T> type) {
        T t = org.springframework.beans.BeanUtils.instantiate(type);
        populate(t, map);
        return t;
    }


    /**
     * 使用Map填充Bean实体
     *
     * @param bean
     *         bean
     * @param props
     *         map
     */
    public static void populate(Object bean, Map<String, ?> props) {
        BeanUtilsBean bub = BeanUtilsBean.getInstance();
        try {
            init(bub);
            bub.populate(bean, props);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Bean转换为Map<String ,String>，转换后会过滤空值(即Map结构中不包含空值)
     *
     * @param origin
     *         目标对象
     *
     * @return Map
     */
    public static Map<String, String> describe(Object origin) {
        BeanUtilsBean bub = BeanUtilsBean.getInstance();
        try {
            init(bub);
            Map<String, String> data = bub.describe(origin);
            if (!CollectionUtils.isEmpty(data)) {
                Iterator<Map.Entry<String, String>> iter = data.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    if (entry.getValue() == null) {
                        iter.remove();    // 将空值过滤掉
                    } else if (StringUtils.equals(entry.getKey(), "class")) {
                        iter.remove();    // 过滤class属性
                    }
                }
            }
            return data;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    /** 日期模式 */
    private static String[] patterns = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};

    /**
     * <b style="color:#F00;">设定日期格式转换器，理论上下面注册机制是在单例上发生的，只需要执行一次，但实际情况在JBoss服务器下经常出现未注册自定义转换器的问题，怀疑实际执行过程中
     * BeanUtilsBean.getInstance()并非是真正单例的，将注册代码块做成初始化方法，在用到的工具类中执行，以避免上述问题</b>
     *
     * @param bub
     *         单例问题仍然存在，所以不再使用单例，来规避此问题，注册的对象由外部传入，工具方法也使用相同类来实现逻辑
     */
    private static final void init(final BeanUtilsBean bub) {
        // 注册自定义日期转换器
        ConvertUtilsBean cub = bub.getConvertUtils();
        DateConverter dateConverter = new DateConverter();
        dateConverter.setPatterns(patterns);
        cub.register(dateConverter, Date.class);
        cub.register(dateConverter, String.class);
    }


}
