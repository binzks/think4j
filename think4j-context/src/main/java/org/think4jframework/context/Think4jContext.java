package org.think4jframework.context;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.think4jframework.context.support.ModelConfig;
import org.think4jframework.context.support.TableConfig;
import org.think4jframework.jdbc.ModelFactory;
import org.think4jframework.jdbc.TableFactory;
import org.think4jframework.jdbc.support.JdbcModel;
import org.think4jframework.jdbc.support.JdbcTable;
import org.think4jframework.jdbc.support.model.Column;
import org.think4jframework.jdbc.support.model.Join;
import org.think4jframework.jdbc.support.model.Model;
import org.think4jframework.jdbc.support.model.Order;
import org.think4jframework.jdbc.support.table.Field;
import org.think4jframework.jdbc.support.table.Table;

import java.io.File;
import java.util.*;

/**
 * Created by zhoubin on 15/9/30.
 * think框架基本配置生成和获取，包括参数、表和model，以及spring获取bean
 */
public class Think4jContext implements ApplicationContextAware {

    private static Logger logger = Logger.getLogger(Think4jContext.class);  //log4j日志对象

    private static ApplicationContext springContext;  //spring配置
    private static Map<String, String> paramMap; //参数缓存map
    private static Map<String, JdbcModel> jdbcModelMap; //JdbcModel缓存map
    private static Map<String, JdbcTable> jdbcTableMap; //JdbcTable缓存map

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;
        // 加载spring配置后自动加载resources目录下的配置文件
        String resourcesPath = this.getClass().getClassLoader().getResource("").getPath();
        init(null, resourcesPath);
    }

    /**
     * 初始化context内容，包括参数、表和数据model
     *
     * @param path xml文件的路径，加载路径下所有的xml文件
     */
    public static void init(String path) {
        init(null, path);
    }

    /**
     * 初始化context内容，主要有spring的配置文件初始化，参数、表和数据model的初始化
     * 如果spring配置文件为null或者空则不初始化，认为是注解加载类实现初始化
     * 初始化参数，参数可以通过${key}获取System.getProperty(key)的值
     * 初始化table和model，生成jdbc对象
     *
     * @param springFile spring配置文件的路径+名称
     * @param path       xml文件的路径，加载路径下所有的xml文件
     */
    public static void init(String springFile, String path) {
        if (StringUtils.isNotBlank(springFile)) {
            springContext = new FileSystemXmlApplicationContext(springFile);
        }
        if (StringUtils.isBlank(path)) {
            return;
        }
        try {
            paramMap = new HashMap<>();
            jdbcModelMap = new HashMap<>();
            jdbcTableMap = new HashMap<>();
            File dir = new File(path);
            File[] files = dir.listFiles();
            SAXReader reader = new SAXReader();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (fileName.toLowerCase().endsWith(".xml")) {
                        Element root = reader.read(file).getRootElement();
                        if (root.attributeValue("type").equals("think4j-context")) {
                            String initTable = root.attributeValue("initTable");
                            setParamConfig(root.element("params"));
                            setTableConfig(root.element("tables"), initTable);
                            setModelConfig(root.element("models"));
                            logger.debug("加载think4j-context配置文件[" + fileName + "]");
                        }
                    }
                }
                logger.debug("加载param：" + new Gson().toJson(paramMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 根据param的xml配置，将数据写入paramMap缓存
     *
     * @param root param的xml配置
     * @throws Exception 参数重名异常
     */

    private static void setParamConfig(Element root) throws Exception {
        if (null == root) {
            return;
        }
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String key = element.attributeValue("key");
            if (null != paramMap.get(key)) {
                throw new Exception("Param定义[" + key + "]已存在！");
            }
            String value = element.attributeValue("value");
            //如果配置参数值有${key}，则用System.getProperty(key)替换
            if (null != value && value.contains("${")) {
                String k = StringUtils.substringBetween(value, "${", "}");
                String v = System.getProperty(k);
                if (null == v) {
                    logger.warn("参数配置[" + key + "]值[${" + k + "}]不存在");
                    v = "";
                }
                value = value.replace("${" + k + "}", v);
            }
            paramMap.put(key, value);
        }
    }

    /**
     * 根据表的xml配置，生成jdbcTable对象，并缓存
     *
     * @param root 表的xml配置
     * @throws Exception 表重名异常
     */
    private static void setTableConfig(Element root, String initTable) throws Exception {
        if (null == root) {
            return;
        }
        TableConfig tableConfig = new TableConfig();
        List<Table> list = tableConfig.init(root);
        for (Table table : list) {
            String key = table.getName();
            if (null != jdbcTableMap.get(key)) {
                throw new Exception("Table定义[" + key + "]已存在！");
            }
            JdbcTemplate jdbcTemplate = (JdbcTemplate) springContext.getBean(table.getDsName());
            JdbcTable jdbcTable = TableFactory.getTable(table, jdbcTemplate);
            if (null != initTable && initTable.toLowerCase().equals(Boolean.TRUE.toString())) {
                jdbcTable.initTable();
            }
            jdbcTableMap.put(key, jdbcTable);
        }
        logger.debug("加载table：" + new Gson().toJson(list));
    }


    /**
     * 根据model的xml配置，生成jdbcModel对象，并缓存
     *
     * @param root model的xml配置
     * @throws Exception model重名异常
     */
    private static void setModelConfig(Element root) throws Exception {
        if (null == root) {
            return;
        }
        ModelConfig modelConfig = new ModelConfig();
        List<Model> list = modelConfig.init(root);
        for (Model model : list) {
            String key = model.getName();
            if (null != jdbcModelMap.get(key)) {
                throw new Exception("Model定义[" + key + "]已存在！");
            }
            JdbcTemplate jdbcTemplate = (JdbcTemplate) springContext.getBean(model.getDsName());
            JdbcModel jdbcModel = ModelFactory.getModel(model, jdbcTemplate);
            jdbcModelMap.put(key, jdbcModel);
        }
        logger.debug("加载model：" + new Gson().toJson(list));
    }

    /***
     * 获取spring注解的bean对象
     *
     * @param name bean的id
     * @return bean对象
     */
    public static Object getSpringBean(String name) {
        return springContext.getBean(name);
    }

    /**
     * 根据参数名称获取参数值
     *
     * @param key 参数名称
     * @return 参数值
     */
    public static String getParam(String key) {
        return paramMap.get(key);
    }

    /**
     * 根据表名获取表的数据处理对象
     *
     * @param key 表名
     * @return 表的数据处理对象
     */
    public static JdbcTable getJdbcTable(String key) {
        return jdbcTableMap.get(key);
    }

    /**
     * 根据表名获取表的创建sql语句，包括创建sql，索引，默认添加数据，如果表定义不存在则返回空字符串
     *
     * @param key 表名
     * @return sql语句
     */
    public static String getCreateSql(String key) {
        JdbcTable jdbcTable = jdbcTableMap.get(key);
        if (null != jdbcTable) {
            return jdbcTable.getCreateSql();
        } else {
            return "";
        }
    }

    /**
     * 获取所有表的创建sql
     *
     * @return 创建sql列表
     */
    public static List<String> getAllCreateSql() {
        List<String> list = new ArrayList<>();
        for (JdbcTable jdbcTable : jdbcTableMap.values()) {
            list.add(jdbcTable.getCreateSql());
        }
        return list;
    }

    /***
     * 根据model的名称获取数据处理model的对象
     *
     * @param key model名称
     * @return 数据处理model的对象
     */
    public static JdbcModel getJdbcModel(String key) {
        return jdbcModelMap.get(key);
    }
}
