package org.think4jframework.jdbc.support.model;

/**
 * Created by zhoubin on 15/9/7.
 * model的排序定义
 */
public class Order {

    private String joinName; //排序字段关联名称，如果null表示主表

    private String key;  //排序字段名称

    private OrderType type; //排序规则 asc desc

    public Order() {

    }

    public Order(String key, OrderType orderType) {
        this.joinName = null;
        this.key = key;
        this.type = orderType;
    }

    public String getJoinName() {
        return joinName;
    }

    public void setJoinName(String joinName) {
        this.joinName = joinName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }
}
