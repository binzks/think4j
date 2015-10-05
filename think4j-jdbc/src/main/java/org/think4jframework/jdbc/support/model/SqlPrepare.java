package org.think4jframework.jdbc.support.model;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/7.
 * 主要用于生成关联sql语句，将数据对象转换为sql语句和值
 */
public class SqlPrepare {

    private final String TABLE_ALIAS_PREFIX = "t"; // 主表别名
    private String joinSql; // model设置的统一关联查询sql
    private Map<String, String> tableAliasMap; // 表的别名

    /***
     * 初始化sqlPrepare，主要生产关联表信息和关联表别名
     *
     * @param joins 关联表定义
     */
    public SqlPrepare(List<Join> joins) {
        tableAliasMap = new HashMap<>();
        if (null == joins) {
            joinSql = "";
        } else {
            //先生成所有关联表的别名
            for (int i = 0; i < joins.size(); i++) {
                tableAliasMap.put(joins.get(i).getName(), TABLE_ALIAS_PREFIX + (i + 1));
            }
            //生成关联sql语句
            StringBuilder joinBuilder = new StringBuilder();
            for (Join join : joins) {
                String alias = tableAliasMap.get(join.getName());
                String joinAlias = getTableAlias(join.getJoinName());
                joinBuilder.append(" ").append(join.getType()).append(" ");
                String joinTable = join.getTable();
                // 如果关联表名不包含“.”则表名增加``，防止表名是关键字，如果包含“.”则认为是跨库查询，不增加``
                if (!StringUtils.contains(joinTable, ".")) {
                    joinBuilder.append("`").append(joinTable).append("`");
                } else {
                    joinBuilder.append(joinTable);
                }
                joinBuilder.append(" AS ").append(alias).append(" ON ").append(alias).append(".").append(join.getKey()).
                        append("=").append(joinAlias).append(".").append(join.getJoinKey());
            }
            joinSql = joinBuilder.toString();
        }
    }

    /**
     * 返回关联表sql语句
     *
     * @return 关联sql语句
     */
    public String getJoinSql() {
        return this.joinSql;
    }

    /***
     * 根据列的定义获取列的查询语句块
     *
     * @param columns 列定义
     * @return 列的语句块
     */
    public String getColumnSql(Map<String, Column> columns) {
        if (null == columns) {
            return "";
        }
        StringBuilder columnBuffer = new StringBuilder();
        for (Column column : columns.values()) {
            if (columnBuffer.length() > 0) {
                columnBuffer.append(",");
            }
            columnBuffer.append(getTableAlias(column.getJoinName())).append(".").append(column.getName());
            String alias = column.getAlias();
            if (StringUtils.isNotBlank(alias)) {
                columnBuffer.append(" AS ").append(alias);
            }
        }
        return columnBuffer.toString();
    }

    /***
     * 根据过滤条件生成，过滤的sql语句和过滤值
     *
     * @param filterList 过滤条件
     * @return 过滤sql和值
     */
    public SqlValues getFilter(List<Filter> filterList) {
        SqlValues sqlValues = new SqlValues();
        if (null == filterList) {
            sqlValues.setSql("");
            sqlValues.setValues(null);
            return sqlValues;
        }
        StringBuilder filterBuffer = new StringBuilder();
        List<Object> filterValues = new ArrayList<>();
        for (Filter filter : filterList) {
            filterBuffer.append(" AND ").append(getTableAlias(filter.getJoinName())).append(".").append(filter.getKey())
                    .append(" ");
            FilterType filterType = filter.getType();
            filterBuffer.append(filterType.toString()).append(" ");
            switch (filterType) {
                case Eq:
                case Ne:
                    filterBuffer.append("?");
                    filterValues.add(filter.getValue());
                    break;
                case Null:
                case NotNull:
                    break;
                case Like:
                    filterBuffer.append("?");
                    filterValues.add("%" + filter.getValue() + "%");
                    break;
                case Between:
                    String[] v = filter.getValue().toString().split(",");
                    filterValues.add(v[0]);
                    filterValues.add(v[1]);
                    break;
                case In:
                case NotIn:
                    if (null == filter.getValue()) {
                        filterBuffer.append("(?)");
                        filterValues.add(null);
                    } else {
                        String[] values = filter.getValue().toString().split(",");
                        StringBuilder in = new StringBuilder();
                        for (String value : values) {
                            if (in.length() > 0) {
                                in.append(",");
                            }
                            in.append("?");
                            filterValues.add(value);
                        }
                        filterBuffer.append("(").append(in).append(")");
                    }
                    break;
                default:
                    filterBuffer.append("?");
                    filterValues.add(filter.getValue());
            }
        }
        sqlValues.setSql(filterBuffer.toString());
        sqlValues.setValues(filterValues);
        return sqlValues;
    }

    /***
     * 根据数据对象生成新增数据的sql语句和值，如果instance为null表示dataMap数据，否则以instance数据生成
     *
     * @param instance 对象类型数据
     * @param dataMap  map类型数据
     * @param columns  model对应的列
     * @param pkName   model主键名称
     * @return 新增字段sql和值
     */
    public SqlValues getInsert(Object instance, Map<String, Object> dataMap, Map<String, Column> columns, String pkName) {
        StringBuilder colSql = new StringBuilder();
        StringBuilder valSql = new StringBuilder();
        List<Object> values = new ArrayList<>();
        for (Column column : columns.values()) {
            String tableAlias = getTableAlias(column.getJoinName());
            if (!tableAlias.equals(TABLE_ALIAS_PREFIX)) {
                continue;
            }
            String colName = column.getName();
            if (colName.equals(pkName)) {
                continue;
            }
            Object value;
            if (null != instance) {
                value = getFieldValueByName(colName, instance);
            } else {
                value = dataMap.get(colName);
            }
            if (colSql.length() > 0) {
                colSql.append(",");
                valSql.append(",");
            }
            colSql.append("`").append(colName).append("`");
            valSql.append("?");
            values.add(value);
        }
        SqlValues sqlValues = new SqlValues();
        sqlValues.setSql(colSql.toString());
        sqlValues.setValSql(valSql.toString());
        sqlValues.setValues(values);
        return sqlValues;
    }

    /**
     * 根据数据对象生成修改数据sql和值，如果instance为null表示dataMap数据，否则以instance数据生成，数据一定要包含主键值
     *
     * @param instance     对象类型数据
     * @param dataMap      map类型数据
     * @param isHandleNull null字段是否更新，true是 false不更新
     * @param columns      model对应的列
     * @param pkName       model的主键名称
     * @return 修改字段sql和值
     */
    public SqlValues getUpdate(Object instance, Map<String, Object> dataMap, boolean isHandleNull, Map<String, Column> columns, String pkName, String keyName) {
        StringBuilder colSql = new StringBuilder();
        List<Object> values = new ArrayList<>();
        for (Column column : columns.values()) {
            String tableAlias = getTableAlias(column.getJoinName());
            if (!tableAlias.equals(TABLE_ALIAS_PREFIX)) {
                continue;
            }
            String colName = column.getName();
            // 主键字段和过滤关键字段不处理
            if (colName.equals(pkName) || colName.equals(keyName)) {
                continue;
            }
            Object value;
            if (null != instance) {
                value = getFieldValueByName(colName, instance);
            } else {
                value = dataMap.get(colName);
            }
            if (null != value || isHandleNull) {
                if (colSql.length() > 0) {
                    colSql.append(",");
                }
                colSql.append("`").append(colName).append("`=?");
                values.add(value);
            }
        }
        Object keyValue;
        if (null != instance) {
            keyValue = getFieldValueByName(keyName, instance);
        } else {
            keyValue = dataMap.get(keyName);
        }
        values.add(keyValue);
        SqlValues sqlValues = new SqlValues();
        sqlValues.setSql(colSql.toString());
        sqlValues.setValues(values);
        return sqlValues;
    }

    /***
     * 获取关联的数据库表在生成sql的别名，如果没有获取则返回主表的别名
     *
     * @param joinName 关联名称
     * @return 关联表别名
     */
    public String getTableAlias(String joinName) {
        String tableAlias = tableAliasMap.get(joinName);
        if (null == tableAlias) {
            tableAlias = TABLE_ALIAS_PREFIX;
        }
        return tableAlias;
    }


    /***
     * 生成排序的sql语句
     *
     * @param orders 排序条件
     * @return 排序sql
     */
    public String getOrderSql(List<Order> orders) {
        if (null == orders) {
            return "";
        } else {
            StringBuilder orderBuffer = new StringBuilder();
            for (Order order : orders) {
                if (orderBuffer.length() > 0) {
                    orderBuffer.append(",");
                }
                orderBuffer.append(getTableAlias(order.getJoinName())).append(".").append(order.getKey())
                        .append(" ").append(order.getType().toString());
            }
            return orderBuffer.toString();
        }
    }

    /***
     * 回去对象指定属性的值
     *
     * @param fieldName 属性名称
     * @param object    取值对象
     * @return 对象值
     */
    private Object getFieldValueByName(String fieldName, Object object) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter);
            return method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
