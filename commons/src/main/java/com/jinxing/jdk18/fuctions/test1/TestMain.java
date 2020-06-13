package com.jinxing.jdk18.fuctions.test1;

import java.util.ArrayList;
import java.util.List;

/**
 * 需求1：从user集合中 找出age=15的用户  传统方法 就不说了 举例明一下 java1.8 使用Predicate接口解决该需求：

 * @author JinXing
 * @date 2020/4/9 14:29
 */
public class TestMain {


    private static List<User> testPredicate(List<User> users, Predicate<User> predicate) {
        List<User> tempUsers = new ArrayList<>();
        for (User user : users) {

            //校验规则
            boolean test = predicate.test(user);
            System.out.println(test);
            if (test) {
                tempUsers.add(user);
            }
        }
        return tempUsers;
    }


    public static void main(String[] args) {

        List<User> users=new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            users.add(new User(i,"张三"+i,i));
        }

        // predicate.test 定义了一个方法规范
        // (user) -> user.getAge() == 15 为方法增加了一些条件，必须满足该条件，test方法才会返回true
        List<User> testPredicate = TestMain.testPredicate(users, (user) ->
                user.getAge() == 15);



        System.out.println("testPredicate:"+testPredicate);
    }




}
