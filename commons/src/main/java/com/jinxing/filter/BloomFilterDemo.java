package com.jinxing.filter;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.*;

/**
 * @author JinXing
 * @date 2021/2/9 15:06
 */
public class BloomFilterDemo {


    /** 100w */
    private static final int insertions = 1000000;

    public static void main(String[] args) {

        //判错率
        bF(0.001);
        bF(0.002);
        bF(0.003);
        bF(0.008);
        bF(0.050);

    }

    private static void bF(double fpp) {

        long startTime = System.currentTimeMillis();

        //初始化一个存储string数据的布隆过滤器，初始化大小100w,不能设置为0
        BloomFilter<String> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), insertions, fpp);

        //初始化一个存储string数据的set，初始化大小100w
        Set<String> sets = new HashSet<>(insertions);

        //初始化一个存储string数据的set，初始化大小100w
        List<String> lists = new ArrayList<String>(insertions);

        //向三个容器初始化100万个随机并且唯一的字符串---初始化操作
        for (int i = 0; i < insertions; i++) {
            String uuid = UUID.randomUUID().toString();
            bf.put(uuid);
            sets.add(uuid);
            lists.add(uuid);
        }


        //布隆过滤器错误判断的次数
        int wrong = 0;
        //布隆过滤器正确判断的次数
        int right = 0;
        for (int i = 0; i < 100; i++) {
            //按照一定比例选择bf中肯定存在的字符串
//            String test = i % 100 == 0 ? lists.get(i / 100) : UUID.randomUUID().toString();

            String test;
            if (i % 100 == 0) {
                int index = i / 100;
                test = lists.get(index);
            } else {
                test = UUID.randomUUID().toString();
            }


            if(bf.mightContain(test)){
                if(sets.contains(test)){
                    right ++;
                }else{
                    wrong ++;
                }
            }
        }

        //100
        System.out.println("\n\n fpp: "+fpp);
        System.out.println("=================right====================="+right);
        System.out.println("=================wrong====================="+wrong);


        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime +"毫秒");
        System.out.println((endTime-startTime)/1000.00 +"秒" );
    }

}
