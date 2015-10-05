package org.think4jframework.jdbc.support.table;


import org.think4jframework.jdbc.support.DataBaseType;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/6.
 * 数据库表的定义，用于生成创建和修改表结构
 */
public class Table {

    private DataBaseType dataBaseType;  //数据库类型

    private String name;    //数据库表名

    private String comment; //表的说明

    private String dsName;  //表所属spring jdbc数据源bean id

    private String pkName;  //主键字段名称

    private List<Field> fields; //表所有的列

    private List<Index> indexes; //表所有的索引，在创建表的时候会将索引添加到数据库

    private List<Map<String, String>> data; //表的初始化数据，在创建表的时候会将数据插入表中

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
}
