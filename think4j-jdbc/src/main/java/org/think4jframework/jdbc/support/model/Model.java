package org.think4jframework.jdbc.support.model;


import org.think4jframework.jdbc.support.DataBaseType;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/7.
 * model定义，用于数据处理，与数据库交互
 */
public class Model {

    private DataBaseType dataBaseType;  //数据库类型

    private String name;  //model名称

    private String tableName; //model对应主表名称

    private String pkName; //主表主键名称

    private String dsName; //model对应的spring数据源bean id

    private Map<String, Column> columns; //model的列定义

    private List<Join> joins;  //model的关联定义

    private List<Order> orders; //model的排序定义，查询数据都会带上的排序

    private List<Filter> filters; //model的过滤定义，查询数据都会带上的过滤条件

    public DataBaseType getDataBaseType() {
        return dataBaseType;
    }

    public void setDataBaseType(DataBaseType dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public Map<String, Column> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, Column> columns) {
        this.columns = columns;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public void setJoins(List<Join> joins) {
        this.joins = joins;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
