package org.think4jframework.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by zhoubin on 15/10/12.
 * xml处理公用方法
 */
public class XmlUtils {

    private static final String DEFAULT_ENCODING = "UTF-8";

    public static Map<String, Object> get(String url)throws  Exception{
        return get(url, DEFAULT_ENCODING);
    }

    public static Map<String, Object> get(String url, String encoding)throws  Exception{
        SAXReader reader = new SAXReader();
        Document document = reader.read(new URL(url));
        Map<String, Object> map = new HashMap<>();
        elementToMap(map, document.getRootElement());
        return map;
    }

    /**
     * xml的节点转换成map对象，递归
     *
     * @param map    最终map对象
     * @param source xml节点
     */
    public static void elementToMap(Map map, Element source) {
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
