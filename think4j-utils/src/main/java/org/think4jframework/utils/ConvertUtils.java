package org.think4jframework.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/10/12.
 * 对象转换方法
 */
public class ConvertUtils {

    /**
     * json字符串转map对象
     *
     * @param value json字符串
     * @return map对象
     */
    public static Map<String, Object> jsonToMap(String value) {
        Gson gson = new Gson();
        return gson.fromJson(value, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * json字符串转单体对象
     *
     * @param value json字符串
     * @param clazz 类
     * @param <T>   类对象
     * @return 对象
     */
    public static <T> T jsonToObject(String value, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(value, clazz);
    }

    /**
     * json字符串转不定对象，单体或者数组
     *
     * @param value json字符串
     * @param type  对象类型
     * @param <T>   对象类型T
     * @return 返回对象
     */
    public static <T> T jsonToObject(String value, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(value, type);
    }

    /***
     * xml字符串转map对象
     *
     * @param value xml字符串
     * @return map对象
     * @throws Exception 异常
     */
    public static Map<String, Object> xmlToMap(String value) throws Exception {
        Map<String, Object> map = new HashMap<>();
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new StringReader(value));
        elementToMap(map, document.getRootElement());
        return map;
    }

    /**
     * xml节点转map对象
     *
     * @param value xml节点
     * @return map对象
     */
    public static Map<String, Object> xmlToMap(Element value) {
        Map<String, Object> map = new HashMap<>();
        elementToMap(map, value);
        return map;
    }

    /**
     * xml的节点转换成map对象，递归
     *
     * @param map    最终map对象
     * @param source xml节点
     */
    private static void elementToMap(Map map, Element source) {
        List<Element> elements = source.elements();
        if (elements.size() == 0) {
            // 如果没有子节点则添加自己
            map.put(source.getName(), source.getText());
        } else if (elements.size() == 1) {
            // 如果有一个子节点，则添加一个对象
            Map<String, Object> nodeMap = new HashMap<>();
            elementToMap(nodeMap, elements.get(0));
            map.put(source.getName(), nodeMap);
        } else {
            // 如果有多个子节点，则添加一个数组
            List<Map<String, Object>> list = new ArrayList<>();
            for (Element element : elements) {
                Map<String, Object> nodeMap = new HashMap<>();
                elementToMap(nodeMap, elements.get(0));
                list.add(nodeMap);
            }
            map.put(source.getName(), list);
        }
    }
}
