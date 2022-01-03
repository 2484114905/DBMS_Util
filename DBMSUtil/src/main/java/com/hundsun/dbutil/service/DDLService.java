package com.hundsun.dbutil.service;

import com.hundsun.dbutil.util.ConfigUtil;
import com.hundsun.dbutil.util.CreateStatementUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 调用工具类获取 create table 语句 或 alter table 语句
 * @author wangyang31647
 * @date 2020/08/10
 */

@Service
public class DDLService {

    @Autowired
    private MysqlToOracleService mysqlToOracleService;
    @Autowired
    private OracleToMysqlService oracleToMysqlService;
    /**
     * 返回alter table 语句，如果目标数据库类型与源数据库类型相同，就不需要类型转换
     * @param tableName 表名
     * @param columnName 列名
     * @param kind 目标数据库类型 MySQL / Oracle
     * @return String类型的 Alter语句
     */
    public  String getColumnDDL(String tableName, String columnName, String kind){
        if (ConfigUtil.sourceKind.equalsIgnoreCase(kind)){
            if (ConfigUtil.MYSQL.equalsIgnoreCase(kind)){
                return mysqlToOracleService.getMysqlAlterColumnStatement(tableName, columnName);
            }else {
                return oracleToMysqlService.getOracleAlterColumnStatement(tableName, columnName);
            }
        }else {
            if (ConfigUtil.MYSQL.equalsIgnoreCase(kind)){
                return oracleToMysqlService.getConvertedOracleAlterColumnStatement(tableName, columnName);
            }else {
                return mysqlToOracleService.getConvertedMysqlAlterColumnStatement(tableName, columnName);
            }
        }

    }

    /**
     * 返回建表语句，如过目标数据库和源数据库类型相同，直接使用 CreateStatementUtil获取建表语句，
     * 否则需要先获取每个字段信息，进行类型转换后再拼接处完整的建表语句
     * @param tableName 表名
     * @param kind 目标数据库类型 MySQL / Oracle
     * @return String 类型的 Create Table 语句
     */
    public  String getTableDDL(String tableName, String kind){
        if (ConfigUtil.sourceKind.equalsIgnoreCase(kind)){
            return CreateStatementUtil.getCreateString(tableName);

        }else {
            if (ConfigUtil.MYSQL.equalsIgnoreCase(kind)){
                return oracleToMysqlService.getConvertedDDL(tableName);
            }else {
                return mysqlToOracleService.getConvertedDDL(tableName);
            }
        }
    }

}
