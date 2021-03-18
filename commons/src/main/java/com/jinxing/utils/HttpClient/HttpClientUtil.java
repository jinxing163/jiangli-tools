package com.jinxing.utils.HttpClient;


import com.jinxing.utils.HttpClient.HttpClientFactory;
import com.jinxing.utils.HttpClient.HttpConstant;
import org.apache.commons.collections.MapUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 *
 * @author : chenlinyan
 * @version : 2.0
 */
public class HttpClientUtil {

    protected static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);


    /**
     * 通过get方式调用http接口
     *
     * @param url    url路径
     * @param reSend 重发次数
     * @return String json格式，请自行解析
     */
    public static String sendGetByJson(String url, int reSend, Map<String, String> propertyMap) {
        //声明返回结果
        String result = "";
        //开始请求API接口时间
        long startTime = System.currentTimeMillis();
        //请求API接口的响应时间
        long endTime;
        HttpEntity httpEntity = null;
        try {
            // 创建连接
            HttpClient httpClient = HttpClientFactory.getInstance().getHttpClient();
            // 设置请求头和报文
            HttpGet httpGet = HttpClientFactory.getInstance().httpGet(url);
            Header header = new BasicHeader("Accept-Encoding", null);
            httpGet.setHeader(header);

            //设置请求的属性
            if (MapUtils.isNotEmpty(propertyMap)) {
                propertyMap.forEach(httpGet::setHeader);
            } else {
                // 设置通用的请求属性 设置请求格式
                httpGet.setHeader("Content-type", "application/x-www-form-urlencoded");
            }

            //执行发送，获取相应结果
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            logger.error("请求{}接口出现异常", url, e);
            if (reSend > 0) {
                logger.info("请求{}出现异常:{}，进行重发。进行第{}次重发", url, e.getMessage(), (HttpConstant.REQ_TIMES - reSend + 1));
                result = sendGetByJson(url, reSend - 1, propertyMap);
                if (result != null && !"".equals(result)) {
                    return result;
                }
            }
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                logger.error("http请求释放资源异常", e);
            }
        }
        //请求接口的响应时间
        endTime = System.currentTimeMillis();
        logger.info("请求{}接口的响应报文内容为{},本次请求API接口的响应时间为:{}毫秒", url, result, (endTime - startTime));
        return result;

    }


    /**
     * 通过post方式调用http接口
     *
     * @param url       url路径
     * @param jsonParam json格式的参数
     * @param reSend    重发次数
     * @return String json格式，请自行解析
     */
    public static String sendPostByJson(String url, String jsonParam, int reSend, Map<String, String> propertyMap) {
        //声明返回结果
        String result = "";
        //开始请求API接口时间
        long startTime = System.currentTimeMillis();
        //请求API接口的响应时间
        long endTime;
        HttpEntity httpEntity = null;
        HttpResponse httpResponse;
        HttpClient httpClient;
        try {
            // 创建连接
            httpClient = HttpClientFactory.getInstance().getHttpClient();
            // 设置请求头和报文
            HttpPost httpPost = HttpClientFactory.getInstance().httpPost(url);
            Header header = new BasicHeader("Accept-Encoding", null);
            httpPost.setHeader(header);

            //设置请求的属性
            if (MapUtils.isNotEmpty(propertyMap)) {
                propertyMap.forEach(httpPost::setHeader);
            } else {
                // 设置通用的请求属性 设置请求格式
                httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            }

            // 设置报文和通讯格式
            StringEntity stringEntity = new StringEntity(jsonParam, HttpConstant.UTF8_ENCODE);
            stringEntity.setContentEncoding(HttpConstant.UTF8_ENCODE);
            stringEntity.setContentType(HttpConstant.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);
            logger.warn("请求{}接口的参数为{}", url, jsonParam);
            //执行发送，获取相应结果
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
            logger.warn("状态码{}", httpResponse.getStatusLine().getStatusCode());

        } catch (Exception e) {
            logger.error(" 请求{}接口出现异常", url, e);
            if (reSend > 0) {
                logger.info(" 请求{}出现异常:{}，进行重发。进行第{}次重发", url, e.getMessage(), (HttpConstant.REQ_TIMES - reSend + 1));
                result = sendPostByJson(url, jsonParam, reSend - 1, propertyMap);
                if (result != null && !"".equals(result)) {
                    return result;
                }
            }
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                logger.error("http请求释放资源异常", e);
            }
        }
        //请求接口的响应时间
        endTime = System.currentTimeMillis();
        logger.info("请求{}接口的响应报文内容为{},本次请求API接口的响应时间为:{}毫秒", url, result, (endTime - startTime));
        return result;

    }


    /**
     * 通过post方式调用http接口
     *
     * @param url    url路径
     * @param map    json格式的参数
     * @param reSend 重发次数
     * @return String json格式，请自行解析
     */
    public static String sendPostByForm(String url, Map<String, String> map, int reSend) {
        //声明返回结果
        String result = "";
        //开始请求API接口时间
        long startTime = System.currentTimeMillis();
        //请求API接口的响应时间
        long endTime;
        HttpEntity httpEntity = null;
        try {
            // 创建连接
            HttpClient httpClient = HttpClientFactory.getInstance().getHttpClient();
            // 设置请求头和报文
            HttpPost httpPost = HttpClientFactory.getInstance().httpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, HttpConstant.UTF8_ENCODE);
            httpPost.setEntity(entity);
            logger.info("请求{}接口的参数为{}", url, map);
            //执行发送，获取相应结果
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            logger.error("请求{}接口出现异常", url, e);
            if (reSend > 0) {
                logger.info("请求{}出现异常:{}，进行重发。进行第{}次重发", url, e.getMessage(), (HttpConstant.REQ_TIMES - reSend + 1));
                result = sendPostByForm(url, map, reSend - 1);
                if (result != null && !"".equals(result)) {
                    return result;
                }
            }
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                logger.error("http请求释放资源异常", e);
            }
        }
        //请求接口的响应时间
        endTime = System.currentTimeMillis();
        logger.info("请求{}接口的响应报文内容为{},本次请求API接口的响应时间为:{}毫秒", url, result, (endTime - startTime));
        return result;

    }

    /**
     * 通过post方式调用http接口
     *
     * @param url      url路径
     * @param xmlParam json格式的参数
     * @param reSend   重发次数
     * @return String json格式，请自行解析
     */
    public static String sendPostByXml(String url, String xmlParam, int reSend, Map<String, String> propertyMap) {
        //声明返回结果
        String result = "";
        //开始请求API接口时间
        long startTime = System.currentTimeMillis();
        //请求API接口的响应时间
        long endTime;
        HttpEntity httpEntity = null;
        HttpResponse httpResponse;
        HttpClient httpClient;
        try {
            // 创建连接
            httpClient = HttpClientFactory.getInstance().getHttpClient();
            // 设置请求头和报文
            HttpPost httpPost = HttpClientFactory.getInstance().httpPost(url);
            StringEntity stringEntity = new StringEntity(xmlParam, HttpConstant.UTF8_ENCODE);
            stringEntity.setContentEncoding(HttpConstant.UTF8_ENCODE);
            stringEntity.setContentType(HttpConstant.TEXT_XML);
            httpPost.setEntity(stringEntity);
            logger.info("请求{}接口的参数为{}", url, xmlParam);
            //执行发送，获取相应结果
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, HttpConstant.UTF8_ENCODE);
        } catch (Exception e) {
            logger.error("请求{}接口出现异常", url, e);
            if (reSend > 0) {
                logger.info("请求{}出现异常:{}，进行重发。进行第{}次重发", url, e.getMessage(), (HttpConstant.REQ_TIMES - reSend + 1));
                result = sendPostByJson(url, xmlParam, reSend - 1, propertyMap);
                if (result != null && !"".equals(result)) {
                    return result;
                }
            }
        } finally {
            try {
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                logger.error("http请求释放资源异常", e);
            }
            //请求接口的响应时间
            endTime = System.currentTimeMillis();
            logger.info("请求{}接口的响应报文内容为{},本次请求API接口的响应时间为:{}毫秒", url, result, (endTime - startTime));
            return result;
        }

    }
}