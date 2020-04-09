package com.jinxing.jdk18.fuctions.test2;

import lombok.Data;

/**
 * @author JinXing
 * @date 2020/4/9 14:29
 */

@Data
public class Product {

    private int id;
    private String name;
    /** 误差率 */
    private int error;

    public Product(int id, String name, int error) {
        this.id = id;
        this.name = name;
        this.error = error;
    }
}
