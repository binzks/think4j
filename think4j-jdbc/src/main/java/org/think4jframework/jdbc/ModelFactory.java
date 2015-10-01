package org.think4jframework.jdbc;


import org.springframework.jdbc.core.JdbcTemplate;
import org.think4jframework.jdbc.core.model.MysqlModel;
import org.think4jframework.jdbc.support.DataBaseType;
import org.think4jframework.jdbc.support.JdbcModel;
import org.think4jframework.jdbc.support.model.Model;

/**
 * Created by zhoubin on 15/9/7.
 * model工厂，用于获取model对象
 */
public class ModelFactory {

    /***
     * 获取数据model对象，根据数据库类型获取对应对象，用于数据增、删、改、查
     *
     * @param model        数据model定义
     * @param jdbcTemplate spring JdbcTemplate对象
     * @return 数据model对象
     */
    public static JdbcModel getModel(Model model, JdbcTemplate jdbcTemplate) {
        JdbcModel jdbcModel;
        if (model.getDataBaseType() == DataBaseType.Mysql) {
            jdbcModel = new MysqlModel();
        } else {
            throw new RuntimeException("不支持的数据库类型[" + model.getDataBaseType().toString() + "]");
        }
        jdbcModel.setModel(model, jdbcTemplate);
        return jdbcModel;
    }
}
