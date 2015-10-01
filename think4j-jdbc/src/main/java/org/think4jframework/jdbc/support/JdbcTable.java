package org.think4jframework.jdbc.support;


import org.springframework.jdbc.core.JdbcTemplate;
import org.think4jframework.jdbc.support.table.Table;

/**
 * Created by zhoubin on 15/9/6.
 * jdbc数据表接口
 */
public interface JdbcTable {

    /***
     * 设置表的定义信息
     *
     * @param table 表定义
     */

    /***
     * 设置表的定义信息
     *
     * @param table        @param table 表定义
     * @param jdbcTemplate spring JdbcTemplate对象
     */
    void setTable(Table table, JdbcTemplate jdbcTemplate);

    /***
     * 初始化数据库表，如果表不存在则创建表、添加索引、添加初始化数据，如果表存在则修改表字段
     */
    void initTable();
}
