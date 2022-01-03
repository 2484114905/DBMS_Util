package com.hundsun.dbutil.service;

import com.hundsun.dbutil.dao.ColumnDAO;
import com.hundsun.dbutil.dao.IndexDAO;
import com.hundsun.dbutil.domain.Column;
import com.hundsun.dbutil.domain.OracleIndex;
import com.hundsun.dbutil.util.ConfigUtil;
import java.util.Set;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工具类，用于将oracle数据类型转换为mysql数据类型并拼接出完整的create table语句
 * @author wangyang31647
 * @date 2020/08/10
 */
@Service
public class OracleToMysqlService {

    @Autowired
    private ColumnDAO columnDAO;
    @Autowired
    private IndexDAO indexDAO;
    /**
     * 获取oracle表：字段名称、字段类型、字段长度、数字类型的实际长度、小数位、是否可以为空-Y或N、列注释、主键列
     * @param tableName 表名
     * @return 字段信息的集合
     */
    public List<Column> getOracleColumns(String tableName) {
        List<Column> columnList = columnDAO.getAllColumnsFromOracle(tableName.toUpperCase(), ConfigUtil.userName);

        return columnList;
    }

    /**
     * 根据表名获取索引信息
     * @param tableName 表名
     * @return
     */
    public List<OracleIndex> getOracleIndices(String tableName){
        return indexDAO.getIndexFromOracle(tableName.toUpperCase());
    }

    /**
     * 根据表名和列名获取某个字段的信息
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    public  Column getColumn(String tableName, String columnName){
        Column column = columnDAO.getColumnFromOracle(tableName.toUpperCase(), columnName.toUpperCase(), ConfigUtil.userName);
        return column;
    }

    /**
     * 根据表名和每个字段的信息，进行类型转换后拼接出完整的 create table语句
     * @param columnList 每个字段的信息
     * @param tableName 表名
     * @param indices
     * @return StringBuffer类型的建表语句
     */
    public static StringBuffer oracleDDLToMysql(List<Column> columnList, String tableName,
        List<OracleIndex> indices) {
        StringBuffer strBuffer = new StringBuffer();
        // 拼接建表语句
        strBuffer.append("CREATE TABLE ").append(tableName.toLowerCase()).append("(");
        String primaryKey = "";
        for (Column column : columnList) {
            // 字段名称
            String columnName = column.getColumnName();
            // 字段是否允许为空
            String nullAble = column.getNullAble();
            // 设置字段名称
            strBuffer.append(columnName.toUpperCase() + " ");
            // 设置字段类型和长度
            strBuffer.append(oracleTypeToMysqlType(column) + " ");
            // 设置字段是否为空
            if ("N".equalsIgnoreCase(nullAble)) {
                strBuffer.append(" NOT NULL, \n");
            } else {
                strBuffer.append(", \n");
            }
            // 获取主键
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

        if (indices.isEmpty()){
            System.out.println("indices is empty");
        }
        if (!indices.isEmpty()){
            for (OracleIndex index : indices){
                strBuffer.append(",\n");
                if (index.getUniqueness().equalsIgnoreCase(ConfigUtil.UNIQUENESS)){
                    strBuffer.append("UNIQUE KEY ");
                }else {
                    strBuffer.append("KEY ");
                }
                strBuffer.append(index.getIndexName()).append(" (").append(index.getColumnName().toUpperCase()).append(")");
            }
        }
        strBuffer.append(")");

        return strBuffer;
    }

    /**
     * 将oracle类型转换成mysql类型
     * @param column 字段信息
     * @return
     */
    public static String oracleTypeToMysqlType(Column column) {
        Set<String> keys = ConfigUtil.oracleToMysqlMap.stringPropertyNames();
        if (keys.contains(column.getDataType().toUpperCase())){
            return ConfigUtil.oracleToMysqlMap.getProperty(column.getDataType().toUpperCase());
        }else if (ConfigUtil.NUMBER.equalsIgnoreCase(column.getDataType()) ){
            StringBuffer result = new StringBuffer(ConfigUtil.DECIMAL);
            result.append("(").append(column.getDataPrecision());
            if (column.getDataScale() != null){
                result.append(",").append(column.getDataScale());
            }
            result.append(")");
            return result.substring(0);
        }else if (ConfigUtil.VARCHAR2.equalsIgnoreCase(column.getDataType()) ||
        ConfigUtil.NVARCHAR2.equalsIgnoreCase(column.getDataType())){
            StringBuffer result = new StringBuffer(ConfigUtil.VARCHAR);
            result.append("(").append(column.getDataLength()).append(")");
            return result.substring(0);
        }else {
            return column.getDataType().toUpperCase();
        }
    }

    /**
     * 根据表名先调用 getOracleColumn 获取所有字段信息，再 调用 oracleDDLToMysql 拼接出完整的create table 语句
     * @param tableName 表名
     * @return
     */
    public  String getConvertedDDL(String tableName){
        List<Column> columnList = getOracleColumns(tableName);
        List<OracleIndex> indices = getOracleIndices(tableName);
        return oracleDDLToMysql(columnList, tableName, indices).substring(0);
    }

    /**
     * 获取不需要转换类型的 alter table 语句
     * @param tableName 表名
     * @param columnName 字段名
     * @return
     */
    public  String getOracleAlterColumnStatement(String tableName, String columnName){
        Column column = getColumn(tableName, columnName);
        StringBuffer result = getAlterString(tableName, columnName);
        result.append(getColumnTypeByDataType(column).toUpperCase());
        return result.substring(0);
    }

    /**
     * 获取需要转换类型的 alter table 语句
     * @param tableName 表名
     * @param columnName 字段名
     * @return
     */
    public  String getConvertedOracleAlterColumnStatement(String tableName, String columnName){
        Column column = getColumn(tableName, columnName);
        StringBuffer result = getAlterString(tableName, columnName);
        result.append(oracleTypeToMysqlType(column));
        return result.substring(0);
    }

    /**
     * dataType 指的是varchar2, number等，columnType指的是该字段完整的数据类型，如：varchar(20)
     * @param column 字段信息
     * @return
     */
    public  String getColumnTypeByDataType(Column column){
        if (ConfigUtil.NUMBER.equalsIgnoreCase(column.getDataType())){
            StringBuffer result = new StringBuffer(ConfigUtil.NUMBER);
            result.append("(").append(column.getDataPrecision());
            if (column.getDataScale() != null){
                result.append(",").append(column.getDataScale());
            }
            result.append("(");
            return result.substring(0);
        }else if (ConfigUtil.VARCHAR2.equalsIgnoreCase(column.getDataType()) ||
            ConfigUtil.NVARCHAR2.equalsIgnoreCase(column.getDataType()) ){
            StringBuffer result = new StringBuffer(column.getDataType().toUpperCase());
            result.append("(").append(column.getDataLength()).append(")");
            return result.substring(0);
        }else {
            return column.getDataType();
        }
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
