package org.think4jframework.jdbc.support.model;

/**
 * Created by zhoubin on 15/9/10.
 * 查询过滤类型
 */
public enum FilterType {

    // ==
    Eq("="),
    // 模糊
    Like("LIKE"),
    // >=
    Ge(">="),
    // >
    Gt(">"),
    // <=
    Le("<="),
    // <
    Lt("<"),
    // !=
    Ne("!="),
    // In
    In("IN"),
    // Not In
    NotIn("NOT IN"),
    // Null
    Null("IS NULL"),
    //Not Null
    NotNull("IS NOT NULL"),
    //Between
    Between("BETWEEN ? AND ?");

    private String name;

    FilterType(String name) {
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
     * @return 过滤类型
     */
    public static FilterType valueOfString(String name) {
        switch (name.toUpperCase()) {
            case "=":
                return FilterType.Eq;
            case "!=":
                return FilterType.Ne;
            case "LIKE":
                return FilterType.Like;
            case "IN":
                return FilterType.In;
            case "NOT IN":
                return FilterType.NotIn;
            case "NULL":
                return FilterType.Null;
            case "NOT NULL":
                return FilterType.NotNull;
            case "BETWEEN":
                return FilterType.Between;
            case ">":
                return FilterType.Gt;
            case "<":
                return FilterType.Lt;
            case ">=":
                return FilterType.Ge;
            case "<=":
                return FilterType.Le;
            default:
                throw new RuntimeException("不支持的查询过滤类型[" + name + "]");
        }
    }
}
