package org.think4jframework.jdbc.support;

import org.springframework.jdbc.core.JdbcTemplate;
import org.think4jframework.jdbc.support.model.Column;
import org.think4jframework.jdbc.support.model.Filter;
import org.think4jframework.jdbc.support.model.Model;
import org.think4jframework.jdbc.support.model.Order;

import java.util.List;
import java.util.Map;

/**
 * Created by zhoubin on 15/9/7.
 * model处理数据接口
 */
public interface JdbcModel {

    /***
     * 设置model定义信息
     *
     * @param model        model定义
     * @param jdbcTemplate spring JdbcTemplate对象
     */
    void setModel(Model model, JdbcTemplate jdbcTemplate);

    /***
     * 获取model的名称标识
     *
     * @return 返回model名称
     */
    String getName();

    /***
     * 获取model对应主表的表名
     *
     * @return 返回model主表表名
     */
    String getTableName();

    /***
     * 获取model对应主表的主键名称
     *
     * @return 主键名称
     */
    String getPkName();

    /***
     * 获取model的列
     *
     * @return model的列
     */
    Map<String, Column> getColumns();

    /**
     * 根据主键值删除数据
     *
     * @param id 要删除数据的id
     */
    void deleteById(Object id);

    /***
     * 根据过滤条件删除数据
     *
     * @param filters 过滤条件
     */
    void delete(List<Filter> filters);

    /***
     * 新增数据，返回新增数据的数据id，数据对象必须对应model列，并且有get字段名方法
     *
     * @param instance 待新增的数据
     * @return 返回新增数据的id
     */
    int insert(Object instance);

    /***
     * 新增数据，返回新增数据的数据id
     *
     * @param dataMap 待新增的数据
     * @return 返回新增数据的id
     */
    int insert(Map<String, Object> dataMap);


    /***
     * 批量新增数据，数据list必须具体有相同的格式，取第一条数据的数据格式生成新增sql语句
     *
     * @param list 待新增的数据列表
     */
    void batchInsert(List<Map<String, Object>> list);

    /***
     * 修改数据，数据主键必须有值，不修改数据值为null的列
     *
     * @param instance 待修改的数据
     */
    void update(Object instance);

    /***
     * 修改数据，数据主键必须有值，不修改数据值为null的列
     *
     * @param dataMap 待修改的数据
     */
    void update(Map<String, Object> dataMap);

    /***
     * 修改数据，keyName字段必须有值，不修改数据值为null的列
     *
     * @param dataMap 待修改的数据
     * @param keyName 修改条件字段名称，以该字段作为修改的过滤字段
     */
    void update(Map<String, Object> dataMap, String keyName);

    /***
     * 修改数据，数据主键必须有值
     *
     * @param dataMap      待修改的数据
     * @param isHandleNull 是否要修改值为null的字段true表示是
     */
    void update(Map<String, Object> dataMap, boolean isHandleNull);

    /***
     * 修改数据，keyName字段必须有值
     *
     * @param dataMap      待修改的数据
     * @param isHandleNull 是否要修改值为null的字段true表示是
     * @param keyName      修改条件字段名称，以该字段作为修改的过滤字段
     */
    void update(Map<String, Object> dataMap, boolean isHandleNull, String keyName);

    /***
     * 批量修改数据，list必须具有相同的数据结构，以第一条数据生成sql语句，不修改值为null的数据
     *
     * @param list 待修改的数据列表
     */
    void batchUpdate(List<Map<String, Object>> list);

    /***
     * 批量修改数据，list必须具有相同的数据结构，以第一条数据生成sql语句，不修改值为null的数据
     *
     * @param list    待修改的数据列表
     * @param keyName 修改条件字段名称，以该字段作为修改的过滤字段
     */
    void batchUpdate(List<Map<String, Object>> list, String keyName);

    /**
     * 批量修改数据，list必须具有相同的数据结构，以第一条数据生成sql语句
     *
     * @param list         待修改的数据列表
     * @param isHandleNull 是否要修改值为null的字段true表示是
     */
    void batchUpdate(List<Map<String, Object>> list, boolean isHandleNull);

    /***
     * 批量修改数据，list必须具有相同的数据结构，以第一条数据生成sql语句
     *
     * @param list         待修改的数据列表
     * @param isHandleNull 是否要修改值为null的字段true表示是
     * @param keyName      修改条件字段名称，以该字段作为修改的过滤字段，如果keyName为null，则取主键
     */
    void batchUpdate(List<Map<String, Object>> list, boolean isHandleNull, String keyName);

    /**
     * 获取数据总数
     *
     * @return 返回总数
     */
    Integer getTotalCount();

    /**
     * 根据过滤条件获取数据总数
     *
     * @param filters 过滤条件
     * @return 返回总数
     */
    Integer getTotalCount(List<Filter> filters);

    /***
     * 查询单条数据，如果没有数据则返回null
     *
     * @return 返回数据
     */
    Map<String, Object> selectMap();

    /***
     * 根据条件查询单条数据，如果没有数据则返回null
     *
     * @param filters 过滤条件
     * @return 返回数据
     */
    Map<String, Object> selectMap(List<Filter> filters);

    /***
     * 根据条件获取指定字段的单条数据，如果没有数据则返回null
     *
     * @param filters 过滤条件
     * @param columns 要查询的字段，多个,隔开 *表示全部
     * @return 返回数据
     */
    Map<String, Object> selectMap(List<Filter> filters, String columns);

    /**
     * 获取单条数据，如果没有数据则返回null
     *
     * @param filters 过滤条件
     * @param orders  排序
     * @param columns 要查询的字段，多个,隔开 *表示全部
     * @return 返回数据
     */
    Map<String, Object> selectMap(List<Filter> filters, List<Order> orders, String columns);

    /***
     * 查询数据，如果没有数据则返回null
     *
     * @return 返回数据列表
     */
    List<Map<String, Object>> selectMaps();

    /***
     * 根据条件查询数据，如果没有数据则返回null
     *
     * @param filters 过滤条件
     * @return 返回数据列表
     */
    List<Map<String, Object>> selectMaps(List<Filter> filters);

    /***
     * 根据条件获取指定字段的数据，如果没有数据则返回null
     *
     * @param filters 过滤条件
     * @param columns 要查询的字段，多个,隔开 *表示全部
     * @return 返回数据列表
     */
    List<Map<String, Object>> selectMaps(List<Filter> filters, String columns);

    /***
     * 根据条件获取分页数据，如果没有数据则返回null
     *
     * @param filters 过滤条件
     * @param begin   开始数据行
     * @param size    查询数量，0表示不限制
     * @return 返回数据列表
     */
    List<Map<String, Object>> selectMaps(List<Filter> filters, int begin,
                                         int size);

    /***
     * 根据条件获取指定字段的分页数据，如果没有数据则返回null
     *
     * @param filters 过滤条件
     * @param columns 要查询的字段，多个,隔开 *表示全部
     * @param begin   开始数据行
     * @param size    查询数量，0表示不限制
     * @return 返回数据列表
     */
    List<Map<String, Object>> selectMaps(List<Filter> filters, String columns, int begin,
                                         int size);

    /**
     * 获取数据，如果没有数据则返回null
     *
     * @param filters 过滤条件
     * @param orders  排序
     * @return 返回数据列表
     */
    List<Map<String, Object>> selectMaps(List<Filter> filters, List<Order> orders);

    /**
     * 获取数据，如果没有数据则返回null
     *
     * @param filters 过滤条件
     * @param orders  排序
     * @param columns 要查询的字段，多个,隔开 *表示全部
     * @param begin   开始数据行
     * @param size    查询数量，0表示不限制
     * @return 返回数据列表
     */
    List<Map<String, Object>> selectMaps(List<Filter> filters, List<Order> orders, String columns, int begin,
                                         int size);
}
