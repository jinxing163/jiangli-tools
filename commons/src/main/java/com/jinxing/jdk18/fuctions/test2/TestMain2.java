package com.jinxing.jdk18.fuctions.test2;

import java.util.ArrayList;
import java.util.List;

/**
 * 需求1：从Product集合中 找出误差率 error<=10的产品  传统方法 就不说了 举例明一下 java1.8 使用Predicate接口解决该需求：

 * @author JinXing
 * @date 2020/4/9 14:40
 */
public class TestMain2 {


    private static List<Product> qcProduct(List<Product> productList, ProductQC<Product> productQC){

        List<Product> resultList=new ArrayList<>(productList.size());
        for (Product product : productList) {

            //进行产品质量检查，满足条件，返回true
            boolean qc = productQC.QC(product);
            if(qc){
                resultList.add(product);
            }
        }

        return resultList;
    }

    public static void main(String[] args) {

        List<Product> products = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            products.add(new Product(i, "电脑" + i, i));
        }

        List<Product> products1 = TestMain2.qcProduct(products, product -> product.getError() <= 10);

        for (Product product : products1) {
            System.out.println(product);
        }
    }

}
