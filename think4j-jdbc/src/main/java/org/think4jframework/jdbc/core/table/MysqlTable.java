package org.think4jframework.jdbc.core.table;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.think4jframework.jdbc.support.JdbcTable;
import org.think4jframework.jdbc.support.table.Field;
import org.think4jframework.jdbc.support.table.Index;
import org.think4jframework.jdbc.support.table.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by zhoubin on 15/9/6.
 * Mysql数据库表处理对象
 */
public class MysqlTable implements JdbcTable {

    private static Logger logger = Logger.getLogger(JdbcTable.class);  //日志对象
    private Table table; //表结构定义
    private JdbcTemplate jdbcTemplate; //处理sql的spring jdbc对象

    @Override
    public void setTable(Table table, JdbcTemplate jdbcTemplate) {
        this.table = table;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Table getTable() {
        return this.table;
    }

    @Override
    public void initTable() {
        List<Map<String, Object>> list = null;
        try {
            list = jdbcTemplate.queryForList(String.format("describe %s", this.table.getName()));
        } catch (Exception e) {
            logger.debug("describe " + this.table.getName() + "错误[" + e.getMessage() + "]");
        }
        if (null == list) {
            String sql = getCreateTableSql();
            logger.debug(sql);
            jdbcTemplate.execute(sql);
            List<String> indexes = getCreateIndexSql();
            if (null != indexes) {
                for (String index : indexes) {
                    logger.debug(index);
                    jdbcTemplate.execute(index);
                }
            }
            List<String> data = getCreateDataSql();
            if (null != data) {
                for (String d : data) {
                    logger.debug(d);
                    jdbcTemplate.execute(d);
                }
            }
        }
    }

    @Override
    public String getCreateSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SET FOREIGN_KEY_CHECKS = 0;");
        sql.append("DROP TABLE IF EXISTS `" + this.table.getName() + "`;");
        sql.append(getCreateSql()).append(";");
        List<String> indexes = getCreateIndexSql();
        if (null != indexes) {
            for (String index : indexes) {
                sql.append(index).append(";");
            }
        }
        List<String> data = getCreateDataSql();
        if (null != data) {
            sql.append("BEGIN;");
            for (String d : data) {
                sql.append(d).append(";");
            }
            sql.append("COMMIT;");
        }
        sql.append("SET FOREIGN_KEY_CHECKS = 1;");
        return sql.toString();
    }

    /***
     * 获取创建表的sql语句
     *
     * @return 返回创建表的sql语句
     */
    private String getCreateTableSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE `").append(this.table.getName()).append("` (");
        for (Field field : this.table.getFields()) {
            sql.append(getFieldSql(field));
            sql.append(",");
        }
        sql.append("PRIMARY KEY (`").append(this.table.getPkName()).append("`))");
        String comment = this.table.getComment();
        if (StringUtils.isNotBlank(comment)) {
            sql.append(" COMMENT='").append(comment).append("'");
        }
        return sql.toString();
    }

    /**
     * 创建表的索引
     *
     * @return sql语句列表
     */
    private List<String> getCreateIndexSql() {
        List<Index> indexes = this.table.getIndexes();
        if (null == indexes) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (Index index : indexes) {
            StringBuilder sql = new StringBuilder();
            sql.append("ALTER TABLE `").append(this.table.getName()).append("` ADD");
            if (index.getType().toUpperCase().equals("UNIQUE")) {
                sql.append(" UNIQUE ");
            }
            sql.append(" INDEX `").append(index.getName()).append("` (").append(index.getFields()).append(")  COMMENT '").append(index.getComment()).append("'");
            list.add(sql.toString());
        }
        return list;
    }

    /**
     * 获取新增默认数据的sql语句
     *
     * @return sql语句列表
     */
    private List<String> getCreateDataSql() {
        List<Map<String, String>> data = this.table.getData();
        if (null == data) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (Map<String, String> map : data) {
            StringBuilder colSql = new StringBuilder();
            StringBuilder valSql = new StringBuilder();
            for (Entry<String, String> entry : map.entrySet()) {
                if (colSql.length() > 0) {
                    colSql.append(",");
                }
                colSql.append("`").append(entry.getKey()).append("`");
                if (valSql.length() > 0) {
                    valSql.append(",");
                }
                valSql.append("'").append(entry.getValue()).append("'");
            }
            String sql = String.format("INSERT INTO `%s`(%s) VALUES (%s)", this.table.getName(), colSql, valSql);
            list.add(sql);
        }
        return list;
    }

    /***
     * 根据字段定义获取创建字段的sql语句，其中主键固定为int(11)不可空自增长
     *
     * @param field 表定义的字段
     * @return 返回字段列表sql
     */
    private String getFieldSql(Field field) {
        StringBuilder sql = new StringBuilder();
        String name = field.getName();
        if (name.equals(this.table.getPkName())) {
            sql.append("`").append(name).append("` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键'");
        } else {
            sql.append("`").append(name).append("` ").append(field.getType());
            String size = field.getSize();
            if (null != size) {
                sql.append("(").append(size).append(")");
            }
            if (!field.getIsNull()) {
                sql.append(" NOT NULL");
            }
            String defaultValue = field.getDefaultValue();
            if (StringUtils.isNotBlank(defaultValue)) {
                if (defaultValue.trim().toUpperCase().equals("NULL")) {
                    sql.append(" DEFAULT NULL");
                } else {
                    sql.append(" DEFAULT '").append(defaultValue).append("'");
                }
            }
            if (StringUtils.isNotBlank(field.getComment())) {
                sql.append(" COMMENT '").append(field.getComment()).append("'");
            }
        }
        return sql.toString();
    }


}
