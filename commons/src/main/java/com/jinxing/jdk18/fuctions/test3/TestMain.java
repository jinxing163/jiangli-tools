package com.jinxing.jdk18.fuctions.test3;

import com.jinxing.jdk18.fuctions.test1.User;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 总结
 * 1.Consumer 有个accept的接口方法，无返回值，可以用作更改对象
 *
 * 2.Function<T, R> 该接口泛型 T 你需要修改 或者 操作的对象  R 是你返回的对象  接口方法
 * R apply(T t); 传入参数T 对象  返回  R 对象 例子：传入的是user对象 返回的是String 对象
 *
 * 3.supplier也是是用来创建对象的，但是不同于传统的创建对象语法：new
 *
 * @author JinXing
 * @date 2020/4/9 14:57
 */
public class TestMain {


    private static List<User> getUsers() {
        List<User> users = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            users.add(new User(i, "张三" + i, i));
        }
        return users;
    }

    private static void consumerUser(List<User> users, Consumer<User> consumer) {

        if (CollectionUtils.isEmpty(users) || consumer == null) {
            return;
        }

        users.stream().filter(Objects::nonNull).forEach(consumer::accept);
    }


    /**
     * 将所有的user对象的age都初始化为0 传统的可以循环每个user  将该user的age设置为0
     * java1.8 则可以使用 Consumer 接口实现
     * Consumer 有个accept的接口方法，无返回值，可以用作更改对象
     */
    private static void test1() {
        List<User> users = getUsers();
        System.out.println("执行前 userInfo:");
        users.forEach(System.out::println);
        TestMain.consumerUser(users, user -> user.setAge(0));
        System.out.println("执行后 userInfo:");
        users.forEach(System.out::println);

    }


    private static List<String> functionUser(List<User> users, Function<User, String> function) {

        if (CollectionUtils.isEmpty(users) || function == null) {
            return null;
        }

        List<String> resultList = new ArrayList<>(users.size());
        users.stream().filter(Objects::nonNull).forEach(user -> {
            String apply = function.apply(user);
            resultList.add(apply);
        });
        return resultList;
    }


    /**
     * 返回一个user的姓名 一般都是直接user.getName java1.8 则 用function接口 可以实现，
     * 相对这个需求来说 不用funcation 更快,这里就只是为了使用 而使用
     *
     * Function<T, R> 该接口泛型 T 你需要修改 或者 操作的对象  R 是你返回的对象  接口方法
     * R apply(T t); 传入参数T 对象  返回  R 对象 例子：传入的是user对象 返回的是String 对象
     */
    public static void test2() {

        List<User> users = getUsers();
        System.out.println("users:");
        users.forEach(System.out::println);
        List<String> strings = functionUser(users, User::getName);
        System.out.println(strings);

    }

    private static List<User> supplierUser(List<User> users, Supplier<User> supplier) {

        if (CollectionUtils.isEmpty(users) || supplier == null) {
            return null;
        }

        List<User> resultList = new ArrayList<>(users.size());
        users.stream().filter(Objects::nonNull).forEach(user -> {
            User apply = supplier.get();
            resultList.add(apply);
        });
        return resultList;
    }


    /**
     * 尴尬的需求，就是返回一个User对象 java1.8 Supplier
     */
    public static void test3() {

        //supplier也是是用来创建对象的，但是不同于传统的创建对象语法：new
        Supplier<User> sup= User::new;

        List<User> resultList=new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            //调用get()方法，此时会调用对象的构造方法，即获得到真正对象
            User user = sup.get();
            user.setName("张阿曼"+i);
            user.setId(i);
            resultList.add(user);
        }


        resultList.forEach(System.out::println);

    }


    public static void test4() {

        //supplier也是是用来创建对象的，但是不同于传统的创建对象语法：new
        Supplier<User> sup = User::new;

        List<User> resultList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            //调用get()方法，此时会调用对象的构造方法，即获得到真正对象
            User user = sup.get();
            if(i%2 ==0) {
                user.setName("张阿曼" + i);
            }
            user.setId(i);
            resultList.add(user);
        }


        System.out.println("BF:"+resultList);

        resultList.forEach(System.out::println);

    }

    public static void main(String[] args) {
//        test1();

//        test2();


//        test3();

        test4();


    }

}
