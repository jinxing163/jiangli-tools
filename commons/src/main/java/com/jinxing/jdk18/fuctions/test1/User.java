package com.jinxing.jdk18.fuctions.test1;

import lombok.Data;

/**
 * @author JinXing
 * @date 2020/4/9 14:29
 */

@Data
public class User {

    private int id;
    private String name;
    private int age;

    public User() {
    }

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(String s) {

    }
}
