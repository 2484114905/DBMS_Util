package com.hundsun.dbutil.service;

import com.hundsun.dbutil.dao.ColumnDAO;
import com.hundsun.dbutil.dao.IndexDAO;
import com.hundsun.dbutil.domain.Column;
import com.hundsun.dbutil.util.ConfigUtil;
import java.util.Set;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工具类，用于将mysql数据类型转换为oracle数据类型并拼接出完整的create table语句
 * @author wangyang31647
 * @date 2020/08/10
 */
@Service
public class MysqlToOracleService {
    @Autowired
    private ColumnDAO columnDAO;
    @Autowired
    private IndexDAO indexDAO;

    /**
     * 获取mysql表字段名称、字段类型、字段长度、字段实际长度、小数位、是否为空、注释、主键，并封装到集合中
     * @param tableName 表名
     * @return List<Column>
     */
    public  List<Column> getMysqlColumns(String tableName) {
        List<Column> columnList = columnDAO.getAllColumnsFromMysql(ConfigUtil.schemaName, tableName);
        return columnList;
    }

    /**
     * 根据表名和列名获取某个字段的信息
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    public Column getColumn(String tableName, String columnName){
        Column column = columnDAO.getColumnFromMysql(ConfigUtil.schemaName, tableName, columnName);
        return column;
    }

    /**
     * 根据表名和每个字段的信息，进行类型转换后拼接出完整的 create table语句
     * @param tableName 表名
     * @param columnList 每个字段的信息
     * @return StringBuffer类型的建表语句
     */
    public static StringBuffer mysqlDDLToOracleDDL(String tableName, List<Column> columnList){
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("CREATE TABLE ").append(tableName.toUpperCase()+"( ");
        String primaryKey = "";
        for (Column column : columnList) {
            String columnName = column.getColumnName().toUpperCase();
            String nullAble = column.getNullAble();
            // 设置字段名称
            strBuffer.append(columnName+" ");
            // 设置字段类型和长度
            strBuffer.append(mysqlTypeToOracleType(column)+" ");
            // 设置字段是否为空
            if ("NO".equalsIgnoreCase(nullAble)){
                strBuffer.append("NOT NULL ENABLE, \n");
            }else{
                strBuffer.append(", \n");
            }
            if (null != column.getPrimaryKey() && !"".equals(column.getPrimaryKey())) {
                if ("".equals(primaryKey)) {
                    primaryKey = column.getColumnName();
                }
            }
        }
        // 设置主键
        if (primaryKey.length() > 0){
            strBuffer.append("PRIMARY KEY ("+primaryKey+")");
        }else {
            strBuffer.replace(strBuffer.length()-3, strBuffer.length(), "");
        }
        strBuffer.append(")");
        return strBuffer;
    }

    /**
     * 将mysql数据type转成oracle数据type
     * @param column 需要进行转换的字段
     * @return 转换后的数据类型
     */
    public static String mysqlTypeToOracleType(Column column){
        Set<String> keys = ConfigUtil.mysqlToOracleMap.stringPropertyNames();
        if (keys.contains(column.getDataType().toUpperCase())){
            return ConfigUtil.mysqlToOracleMap.getProperty(column.getDataType().toUpperCase());
        }else if (ConfigUtil.DECIMAL.equalsIgnoreCase(column.getDataType())){
            return column.getColumnType().toUpperCase().replace(ConfigUtil.DECIMAL, ConfigUtil.NUMBER);
        }else if (ConfigUtil.VARCHAR.equalsIgnoreCase(column.getDataType())){
            return column.getColumnType().toUpperCase().replace(ConfigUtil.VARCHAR, ConfigUtil.VARCHAR2);
        }else {
            return column.getColumnType().toUpperCase();
        }
    }

    /**
     * 根据表名先调用 getMysqlColumns 获取所有字段信息，再 调用 mysqlDDLToOracleDDL 拼接出完整的create table 语句
     * @param tableName 表名
     * @return
     */
    public  String getConvertedDDL(String tableName){
        List<Column> columnList = getMysqlColumns(tableName);
        return mysqlDDLToOracleDDL(tableName, columnList).substring(0);
    }

    /**
     * 获取不需要转换类型的 alter table 语句
     * @param tableName 表名
     * @param columnName 字段名
     * @return
     */
    public  String getMysqlAlterColumnStatement(String tableName, String columnName){
        Column column = getColumn(tableName, columnName);
        StringBuffer result = getAlterString(tableName, columnName);
        result.append(column.getColumnType());
        return result.substring(0);
    }

    /**
     * 获取需要转换类型的 alter table 语句
     * @param tableName 表名
     * @param columnName 字段名
     * @return
     */
    public  String getConvertedMysqlAlterColumnStatement(String tableName, String columnName){
        Column column = getColumn(tableName, columnName);
        StringBuffer result = getAlterString(tableName, columnName);
        result.append(mysqlTypeToOracleType(column));
        return result.substring(0);
    }

    /**
     * 拼接出还未加入数据类型的 alter table 语句
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    public static StringBuffer getAlterString(String tableName, String columnName){
        StringBuffer result = new StringBuffer("ALTER TABLE ");
        result.append(tableName.toUpperCase() + " ").append("ADD ").append(columnName.toUpperCase()).append(" ");
        return result;
    }


}
