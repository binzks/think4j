package org.think4jframework.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClientUtils {

    private static final String DEFAULT_ENCODING = "UTF-8";

    public static String get(String url) throws Exception {
        return get(url, DEFAULT_ENCODING);
    }

    public static Map<Object, Object> getMap(String url) throws Exception {
        Gson gson = new Gson();
        String value = get(url);
        return gson.fromJson(value, new TypeToken<Map<Object, Object>>() {
        }.getType());
    }

    public static <T> T getObject(String url, Class<T> clazz) throws Exception {
        Gson gson = new Gson();
        String value = get(url);
        return gson.fromJson(value, clazz);
    }

    public static <T> T getObject(String url, Type type) throws Exception {
        Gson gson = new Gson();
        String value = get(url);
        return gson.fromJson(value, type);
    }

    public static String get(String url, String encoding) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String result = null;
        try {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("http post response status line " + response.getStatusLine());
            }
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, encoding);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return result;
    }

    public static byte[] getByteArray(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        byte[] result = null;
        try {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("http post response status line " + response.getStatusLine());
            }
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toByteArray(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return result;
    }

    public static String get(String url, Map<String, String> params) throws Exception {
        return get(url, params, DEFAULT_ENCODING);
    }

    public static String get(String url, Map<String, String> params, String encoding) throws Exception {
        String p = null;
        if (null != params && params.size() > 0) {
            List<NameValuePair> nameValuePairs = params.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
            p = new UrlEncodedFormEntity(nameValuePairs).toString();
        }
        if (null != p) {
            if (url.contains("?")) {
                return get(url + "&" + p, encoding);
            } else {
                return get(url + "?" + p, encoding);
            }
        } else {
            return get(url, encoding);
        }
    }

    public static String post(String url) throws Exception {
        return post(url, null, DEFAULT_ENCODING);
    }

    public static String post(String url, Map<String, String> params) throws Exception {
        return post(url, params, DEFAULT_ENCODING);
    }

    public static String post(String url, Map<String, String> params, String encoding) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (null != params && params.size() > 0) {
            List<NameValuePair> nameValuePairs = params.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = null;
        try {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("http post response status line " + response.getStatusLine());
            }
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, encoding);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return result;
    }
}
