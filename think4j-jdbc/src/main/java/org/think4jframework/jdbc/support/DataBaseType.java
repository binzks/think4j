package org.think4jframework.jdbc.support;


/**
 * Created by zhoubin on 15/9/6.
 * 数据库类型定义
 */
public enum DataBaseType {
    Mysql("MYSQL"), Sqlite("SQLITE"), SqlServer("SQLSERVER"), Oracle("ORACLE");

    private String name;

    DataBaseType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /***
     * 根据字符串获取枚举值
     *
     * @param name 枚举值字符串
     * @return 数据库类型
     */
    public static DataBaseType valueOfString(String name) {
        switch (name.toUpperCase()) {
            case "MYSQL":
                return DataBaseType.Mysql;
            case "SQLITE":
                return DataBaseType.Sqlite;
            case "SQLSERVER":
                return DataBaseType.SqlServer;
            case "ORACLE":
                return DataBaseType.Oracle;
            default:
                throw new RuntimeException("不支持的数据库类型[" + name + "]");
        }
    }
}