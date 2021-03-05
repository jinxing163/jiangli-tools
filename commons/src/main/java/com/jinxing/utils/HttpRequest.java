package com.jinxing.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import org.apache.commons.collections.MapUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * get、post请求
 *
 * @author JinXing
 * @date 2018/7/17 15:06
 */
public class HttpRequest {

    //默认编码格式
    private static final String DEFAULT_CHARSET="utf-8";

    //超时时间
    private static final Integer TIME_OUT=3000;

    public static void main(String[] args) {
        //https://developer.herewhite.com/server-zh/home/server-room
        String postUrl = "https://api.netless.link/v5/rooms";
        Map<String, String> propertyMap = new HashMap<>(10);
        propertyMap.put("content-type", "application/json");
        propertyMap.put("token", "22");
        String sendPost = sendPost(postUrl, "utf-8", new HashMap<>(10), propertyMap);
        System.out.println("创建房间的结果： " + sendPost);
    }

    /**
     * 向指定URL发送GET方法的请求
     * @param url 发送请求的URL
     * @param map 请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @param charset 发送和接收的格式
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, Map<String,Object> map,String charset){
        StringBuffer sb=new StringBuffer();
        //构建请求参数
        if(MapUtils.isNotEmpty(map)){
            //定义迭代器
            Iterator it=map.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry  er= (Map.Entry) it.next();
                Object value = er.getValue();
                if(value ==null){
                    continue;
                }
                sb.append(er.getKey());
                sb.append("=");
                sb.append(value);
                sb.append("&");
            }
        }
        return  sendGet(url,sb.toString(), charset);
    }


    /**
     * 向指定URL发送POST方法的请求
     * @param url 发送请求的URL
     * @param map 请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @param charset 发送和接收的格式
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, Object> map, String charset) {
        return sendPost(url,charset,map,new HashMap<>(10));
    }


    /**
     * 向指定URL发送POST方法的请求
     * @param url 发送请求的URL
     * @param map 请求Map参数，请求参数应该是 {"name1":"value1","name2":"value2"}的形式。
     * @param propertyMap 请求头属性配置
     * @param charset 发送和接收的格式
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendPost(String url,String charset, Map<String, Object> map, Map<String, String> propertyMap) {
        StringBuilder buffer = new StringBuilder();
        //构建请求参数
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> e : map.entrySet()) {
                Object value = e.getValue();
                if (value == null) {
                    continue;
                }

                buffer.append(e.getKey());
                buffer.append("=");
                buffer.append(value);
                buffer.append("&");
            }
        }
        return sendPost(url, buffer.toString(), charset, propertyMap);
    }


    /**
     * 向指定URL发送GET方法的请求
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param charset 发送和接收的格式
     * @return URL 所代表远程资源的响应结果
     */
    private static String sendGet(String url, String param, String charset) {

        if(StringUtils.isEmpty(charset)) {
            charset=DEFAULT_CHARSET;
        }

        String result = "";
        String line;
        StringBuilder sb=new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性 设置请求格式
            conn.setRequestProperty("contentType", charset);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            // 建立实际的连接
            conn.connect();
            // 定义 BufferedReader输入流来读取URL的响应,设置接收格式
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),charset));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result=sb.toString();
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param charset 发送和接收的格式
     * @return 所代表远程资源的响应结果
     */
    private static String sendPost(String url, String param, String charset) {
        return sendPost(url, param, charset, new HashMap<>(10));
    }



    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param charset 发送和接收的格式
     * @return 所代表远程资源的响应结果
     */
    private static String sendPost(String url, String param, String charset, Map<String,String> propertyMap) {

        param = StringUtils.isEmpty(param) ? "" : param;
        if(StringUtils.isEmpty(charset)) {
            charset=DEFAULT_CHARSET;
        }

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        String line;
        StringBuilder sb=new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            //设置请求的属性
            conn.setRequestProperty("contentType", charset);
            if (MapUtils.isNotEmpty(propertyMap)) {
                propertyMap.forEach(conn::setRequestProperty);
            } else {
                // 设置通用的请求属性 设置请求格式
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            }

            //设置超时时间
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),charset));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result=sb.toString();
        } catch (Exception e) {
            System.out.println("发送 POST请求出现异常!"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }



}
