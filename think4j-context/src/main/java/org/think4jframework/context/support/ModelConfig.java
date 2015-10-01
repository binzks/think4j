package org.think4jframework.context.support;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.think4jframework.jdbc.support.DataBaseType;
import org.think4jframework.jdbc.support.model.Filter;
import org.think4jframework.jdbc.support.model.Join;
import org.think4jframework.jdbc.support.model.Model;
import org.think4jframework.jdbc.support.model.Order;
import org.think4jframework.jdbc.support.model.Column;
import org.think4jframework.jdbc.support.model.FilterType;
import org.think4jframework.jdbc.support.model.OrderType;

import java.util.*;

/**
 * Created by zhoubin on 15/9/9.
 * 获取model配置
 */
public class ModelConfig {

    public List<Model> init(Element models) {
        List<Model> list = new ArrayList<>();
        for (Iterator i = models.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            List<Join> joins = getJoinList(element.element("joins"));
            List<Filter> filters = getFilterList(element.element("filters"));
            List<Order> orders = getOrderList(element.element("orders"));
            Map<String, Column> columns = getColumns(element.element("columns"));
            Model model = new Model();
            model.setName(element.attributeValue("name"));
            model.setDataBaseType(DataBaseType.valueOfString(element.attributeValue("type")));
            model.setDsName(element.attributeValue("ds"));
            model.setTableName(element.attributeValue("table"));
            model.setPkName(element.attributeValue("pk"));
            model.setJoins(joins);
            model.setFilters(filters);
            model.setOrders(orders);
            model.setColumns(columns);
            list.add(model);
        }
        return list;
    }


    /***
     * 获取列的定义
     *
     * @param columnsElement 列
     * @return 列定义
     */
    private Map<String, Column> getColumns(Element columnsElement) {
        if (null == columnsElement) {
            return null;
        } else {
            Map<String, Column> map = new HashMap<>();
            for (Iterator i = columnsElement.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Column column = new Column();
                String name = element.attributeValue("name");
                column.setName(name);
                column.setJoinName(element.attributeValue("joinName"));
                String alias = element.attributeValue("alias");
                column.setAlias(alias);
                if (StringUtils.isNotBlank(alias)) {
                    map.put(alias, column);
                } else {
                    map.put(name, column);
                }
            }
            return map;
        }
    }

    /***
     * 获取关联定义
     *
     * @param joinsElement 关联
     * @return 关联定义
     */
    private List<Join> getJoinList(Element joinsElement) {
        if (null == joinsElement) {
            return null;
        } else {
            List<Join> joinList = new ArrayList<>();
            for (Iterator i = joinsElement.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Join join = new Join();
                join.setName(element.attributeValue("name"));
                join.setTable(element.attributeValue("table"));
                join.setType(element.attributeValue("type"));
                join.setKey(element.attributeValue("key"));
                join.setJoinName(element.attributeValue("joinName"));
                join.setJoinKey(element.attributeValue("joinkey"));
                joinList.add(join);
            }
            return joinList;
        }
    }

    /***
     * 获取过滤定义
     *
     * @param filtersElement 过滤
     * @return 过滤定义
     */
    private List<Filter> getFilterList(Element filtersElement) {
        if (null == filtersElement) {
            return null;
        } else {
            List<Filter> filterList = new ArrayList<>();
            for (Iterator i = filtersElement.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Filter filter = new Filter();
                filter.setJoinName(element.attributeValue("joinName"));
                filter.setKey(element.attributeValue("key"));
                filter.setType(FilterType.valueOfString(element.attributeValue("type")));
                filter.setValue(element.attributeValue("value"));
                filterList.add(filter);
            }
            return filterList;
        }
    }

    /**
     * 获取排序定义
     *
     * @param ordersElement 排序
     * @return 排序定义
     */
    private List<Order> getOrderList(Element ordersElement) {
        if (null == ordersElement) {
            return null;
        } else {
            List<Order> orderList = new ArrayList<>();
            for (Iterator i = ordersElement.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Order order = new Order();
                order.setJoinName(element.attributeValue("joinName"));
                order.setKey(element.attributeValue("key"));
                order.setType(OrderType.valueOfString(element.attributeValue("type")));
                orderList.add(order);
            }
            return orderList;
        }
    }
}
