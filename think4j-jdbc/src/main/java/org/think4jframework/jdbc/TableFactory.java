package org.think4jframework.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.think4jframework.jdbc.core.table.MysqlTable;
import org.think4jframework.jdbc.support.DataBaseType;
import org.think4jframework.jdbc.support.JdbcTable;
import org.think4jframework.jdbc.support.table.Table;

/**
 * Created by zhoubin on 15/9/6.
 * 数据库表工厂，用于生成数据库表处理对象
 */
public class TableFactory {

    /**
     * 获取数据库表处理对象，根据数据库类型获取相应语法的对象，并将表的定义设置到处理JdbcTable对象进行初始化
     *
     * @param table        数据库表定义
     * @param jdbcTemplate spring JdbcTemplate对象
     * @return 数据库表处理对象
     */
    public static JdbcTable getTable(Table table, JdbcTemplate jdbcTemplate) {
        JdbcTable jdbcTable;
        if (table.getDataBaseType() == DataBaseType.Mysql) {
            jdbcTable = new MysqlTable();
        } else {
            throw new RuntimeException("不支持的数据库类型[" + table.getDataBaseType().toString() + "]");
        }
        jdbcTable.setTable(table, jdbcTemplate);
        return jdbcTable;
    }
}
