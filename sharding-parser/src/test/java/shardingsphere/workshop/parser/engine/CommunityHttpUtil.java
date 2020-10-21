package shardingsphere.workshop.parser.engine;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author : lijinbao3
 * @desc :
 * @date : 2020/4/13 17:04
 */
@Slf4j
public class CommunityHttpUtil {
    /**
     * get 请求
     * @param url
     * @return
     */
    public static String httpGet(String url){
        String resString = null;
        try {
            long start = System.currentTimeMillis();
            resString = cn.hutool.http.HttpUtil.createGet(url).execute().body();
            log.info("url:{},get request result:{}",url,resString);
            log.info("处理时间：{}", System.currentTimeMillis()-start);
        } catch (Exception e) {
            log.error("url:{},get request ex:",url,e);
        }
        return resString;
    }

    /**
     * get 请求 设置header
     * @param url
     * @return
     */
    public static String httpGet(String url, Map<String, String> headerMap){
        String resString = null;
        try {
            HttpRequest httpRequest = HttpUtil.createGet(url);
            assembleHeader(httpRequest,headerMap);
            resString = httpRequest.execute().body();
            log.info("url:{},get request result:{}",url,resString);
        } catch (Exception e) {
            log.error("url:{},get request ex:",url,e);
        }
        return resString;
    }

    /**
     * get 请求 设置header
     * @param url
     * @return
     */
    public static String httpGet(String url, Map<String, String> headerMap, Map<String, Object> bodyMap){
        String resString = null;
        try {
            HttpRequest httpRequest = HttpUtil.createGet(url);
            assembleHeader(httpRequest,headerMap);
            resString = httpRequest.body(JSONObject.toJSONString(bodyMap),"application/json;utf-8").execute().body();
            log.info("url:{},get request result:{}",url,resString);
        } catch (Exception e) {
            log.error("url:{},get request ex:",url,e);
        }
        return resString;
    }

    /**
     * post 请求
     * @param url
     * @param paramJson
     * @return
     */
    public static String httpPost(String url, Map<String, String> headerMap, String paramJson){
        String result = null;
        try {
            HttpRequest httpRequest = HttpUtil.createPost(url);
            assembleHeader(httpRequest,headerMap);
            result = httpRequest.body(paramJson,"application/json;utf-8").execute().body();
            log.info("url:{} post request result :{}",url,result);
        } catch (Exception e) {
            log.error("url:{},post request ex:",url,e);
        }
        return result;
    }

    /**
     * post 请求
     * @param url
     * @param paramJson
     * @return
     */
    public static String httpPost(String url, String paramJson){
        String result = null;
        try {
            result = HttpUtil.createPost(url).body(paramJson,"application/json;utf-8").execute().body();
            log.info("url:{} post request result :{}",url,result);
        } catch (Exception e) {
            log.error("url:{},post request ex:",url,e);
        }
        return result;
    }


    private static void assembleHeader(HttpRequest httpRequest, Map<String, String> headerMap){
        if(null!=headerMap && headerMap.size()>0 ){
            for(String key :headerMap.keySet()){
                httpRequest.header(key, headerMap.get(key));
            }
        }
    }
}
