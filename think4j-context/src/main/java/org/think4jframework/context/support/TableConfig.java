package org.think4jframework.context.support;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.think4jframework.jdbc.support.DataBaseType;
import org.think4jframework.jdbc.support.table.Field;
import org.think4jframework.jdbc.support.table.Index;
import org.think4jframework.jdbc.support.table.Table;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhoubin on 15/9/9.
 * table配置
 */
public class TableConfig {

    public List<Table> init(Element tables) {
        List<Table> list = new ArrayList<>();
        for (Iterator i = tables.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String name = element.attributeValue("name");
            List<Field> fields = getFields(element.element("fields"));
            List<Index> indexes = getIndexes(element.element("indexes"));
            List<Map<String, String>> data = getData(element.element("defaults"), fields);
            Table table = new Table();
            table.setDataBaseType(DataBaseType.valueOfString(element.attributeValue("type")));
            table.setName(name);
            table.setComment(element.attributeValue("comment"));
            table.setDsName(element.attributeValue("ds"));
            table.setPkName(element.attributeValue("pk"));
            table.setFields(fields);
            table.setIndexes(indexes);
            table.setData(data);
            list.add(table);
        }
        return list;
    }

    /***
     * 获取table的字段定义
     *
     * @param fieldsElement 字段xml配置
     * @return 字段定义列表
     */
    private List<Field> getFields(Element fieldsElement) {
        if (null == fieldsElement) {
            return null;
        }
        List<Field> list = new ArrayList<>();
        for (Iterator i = fieldsElement.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            Field field = new Field();
            String name = element.attributeValue("name");
            field.setName(name);
            field.setComment(element.attributeValue("comment"));
            String type = element.attributeValue("type");
            if (StringUtils.isBlank(type)) {
                type = "varchar";
            }
            field.setType(type);
            field.setSize(element.attributeValue("size"));
            String isNull = element.attributeValue("isnull");
            if (StringUtils.isNotBlank(isNull) && isNull.toLowerCase().equals(Boolean.FALSE.toString())) {
                field.setIsNull(false);
            } else {
                field.setIsNull(true);
            }
            field.setDefaultValue(element.attributeValue("default"));
            list.add(field);
        }
        return list;
    }

    /***
     * 获取table的索引定义
     *
     * @param indexesElement 索引xml配置
     * @return 索引列表
     */
    private List<Index> getIndexes(Element indexesElement) {
        if (null == indexesElement) {
            return null;
        }
        List<Index> list = new ArrayList<>();
        for (Iterator i = indexesElement.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            Index index = new Index();
            String name = element.attributeValue("name");
            index.setName(name);
            index.setComment(element.attributeValue("comment"));
            String type = element.attributeValue("type");
            if (StringUtils.isBlank(type)) {
                type = "UNIQUE";
            }
            index.setType(type);
            index.setFields(element.attributeValue("fields"));
            list.add(index);
        }
        return list;
    }

    /***
     * 获取table的默认初始化表数据
     *
     * @param dataElement 初始化数据xml配置
     * @param fields      表的字段定义
     * @return 数据列表
     */
    private List<Map<String, String>> getData(Element dataElement, List<Field> fields) {
        if (null == dataElement || null == fields) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();
        for (Iterator i = dataElement.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            Map<String, String> map = new HashMap<>();
            for (Field field : fields) {
                String value = element.attributeValue(field.getName());
                if (StringUtils.isNotBlank(value)) {
                    map.put(field.getName(), value);
                }
            }
            list.add(map);
        }
        return list;
    }
}
