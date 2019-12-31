package com.jinxing.utils;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author JinXing
 * @date 2019/12/31 14:41
 */
public class BeanUtilsTest {

    @Data
    public static class Student {
        private int id;
        private String name;
    }

    @Data
    public static class Teacher {
        private int id;
        private String name;
    }

    public static void main(String[] args) {

        bToB();
        bToList();

        mapToB();
    }

    private static void bToB() {
        System.out.println("\n\n BToB............");
        Student student = new Student();
        student.setId(1);
        student.setName("张三");
        Teacher teacher = BeanUtils.transTo(student, Teacher.class);
        System.out.println("student->" + student);
        System.out.println("teacher->" + teacher);
    }


    private static void bToList() {

        System.out.println("\n\n bToList............");
        List<Student> list = new ArrayList<>(10);
        int endIndex = 3;
        for (int i = 0; i < endIndex; i++) {
            Student student = new Student();
            student.setId(i);
            student.setName("张三" + i);
            list.add(student);
        }

        List<Teacher> teachers = BeanUtils.transTo(list, Teacher.class);
        System.out.println("student->" + list);
        System.out.println("teacher->" + teachers);
    }


    private static void mapToB() {

        Map<String, String> describe = describe();

        System.out.println("\n\n mapToB............");
        Student student1 = BeanUtils.transTo(describe, Student.class);
        System.out.println("student1:" + student1);
    }

    private static Map<String, String> describe() {

        System.out.println("\n\n describe............");
        Student student = new Student();
        student.setId(1);
        student.setName("张三");
        Map<String, String> describe = BeanUtils.describe(student);
        System.out.println("map:" + describe);

        return describe;
    }

}
