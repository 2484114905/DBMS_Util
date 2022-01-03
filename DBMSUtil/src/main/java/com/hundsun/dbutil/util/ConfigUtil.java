package com.hundsun.dbutil.util;


import com.hundsun.fund.ecmp.common.log.LoggersUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置工具类，位于工具类的逻辑底层，
 * @author wangyang31647
 * @date 2020/08/10
 */
public class ConfigUtil {

    /**
     * 源数据库类型
     */
    public static String sourceKind;

    /**
     * 数据库名,用于mysql查询
     */
    public static String schemaName;

    /**
     * 用户名，用于oracle查询
     */
    public static String userName;

    /**
     * mysql数据类型到oracle 数据类型的映射
     */
    public static Properties mysqlToOracleMap = new Properties();

    /**
     * oracle数据类型到mysql 数据类型的映射
     */
    public static Properties oracleToMysqlMap = new Properties();

    /**
     * 数据类型映射文件的位置
     */
    public static String MYSQL_TO_ORACLE_MAP_LOCATION = "dataTypeMap/MysqlToOracle.properties";

    /**
     * 数据类型映射文件的位置
     */
    public static String ORACLE_TO_MYSQL_MAP_LOCATION = "dataTypeMap/OracleToMysql.properties";
    public static final String MYSQL = "mysql";
    public static final String ORACLE = "oracle";
    public static final String NUMERIC = "NUMERIC";
    public static final String NUMBER = "NUMBER";
    public static final String VARCHAR = "VARCHAR";
    public static final String VARCHAR2 = "VARCHAR2";
    public static final String NVARCHAR2 = "NVARCHAR2";
    public static final String UNIQUENESS = "UNIQUE";
    public static final String SEGMENT = "SEGMENT";
    public static final String TABLE_SPACE = "TABLESPACE";
    public static final String DECIMAL = "DECIMAL";

    /**
     * 用于保存配置属性
     */
//    private static Properties configProperties;

    /**
     * 配置文件的路径
     */
//    private static String configPath;

    /**
     * 获取默认的配置，如果没有默认的配置文件，则返回一个空配置,默认配置文件为项目根目录下 ：EnumUtil-config.properties
     * 为了能及时的读取到配置文件的变动，每次调用该方法时都重新读取配置文件
     *
     * @return Properties, 封装了配置文件中的信息
     */
//    public static Properties getConfigProperties() {
//        if (configProperties == null) {
//            configProperties = new Properties();
//        }
//        FileInputStream in = null;
//        try {
//            // 加载默认配置文件
//            if (configPath == null) {
//                in = new FileInputStream("config.properties");
//            } else {
//                in = new FileInputStream(configPath);
//            }
//            configProperties.load(in);
//            in.close();
//        } catch (FileNotFoundException e) {// 没有默认的配置文件，返回一个空配置
//            configProperties = new Properties();
//            return configProperties;
//        } catch (IOException e) {
//            LoggersUtil.error(e.getMessage(), e);
//        }
//
//        return configProperties;
//    }

    /**
     * 设置配置文件的路径
     */
//    public static void setConfigProperties(String configFile) {
//        configPath = configFile;
//    }
}
