package com.hundsun.dbutil.dao;

import com.hundsun.dbutil.domain.Column;
import java.util.List;

/**
 * 从数据库中获取一张表中所有字段或某个字段的所有信息
 * @author wangyang31647
 * @date 2020/08/10
 */
public interface ColumnDAO {

    /**
     * 返回mysql数据库中的某一张表的某一字段的信息
     * @param schemaName 数据库名
     * @param tableName 表名
     * @param columnName 字段名
     * @return 字段信息， 封装在Column对象中
     */
    Column getColumnFromMysql(String schemaName, String tableName, String columnName);

    /**
     * 返回mysql数据库中的某一张表的所有字段的信息
     * @param schemaName 数据库名
     * @param tableName 表名
     * @return 字段信息的集合
     */
    List<Column> getAllColumnsFromMysql(String schemaName, String tableName);

    /**
     * 返回oracle数据库中的某一张表的某一字段的信息
     * @param tableName 表名
     * @param columnName 列名
     * @param userName 用户名
     * @return 字段信息， 封装在Column对象中
     */
    Column getColumnFromOracle(String tableName, String columnName, String userName);

    /**
     * 返回oracle数据库中的某一张表的某一字段的信息
     * @param tableName 表名
     * @param userName 用户名
     * @return 字段信息的集合
     */
    List<Column> getAllColumnsFromOracle(String tableName, String userName);

}
