package org.think4jframework.jdbc.support;


import org.springframework.jdbc.core.JdbcTemplate;
import org.think4jframework.jdbc.support.table.Table;

import java.util.List;

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

    /**
     * 获取table定义
     *
     * @return table
     */
    Table getTable();

    /***
     * 初始化数据库表，如果表不存在则创建表、添加索引、添加初始化数据
     */
    void initTable();

    /**
     * 获取表创建sql语句，包括表创建，索引创建和初始化的数据新增语句
     *
     * @return 创建sql语句列表
     */
    String getCreateSql();
}
