package com.hundsun.dbutil.domain;

import java.io.Serializable;

/**
 * 字段信息的领域对象
 * @author wangyang31647
 * @date 2020/08/10
 */
public class Column implements Serializable {
    /**
     * 列名
     */
    private String columnName;

    /**
     * 字段数据类型
     */
    private String dataType;

    /**
     * 对应于mysql information_schema 数据库中 columns 表中的column_type 字段
     */
    private String columnType;

    /**
     * 字段的数据长度
     */
    private Integer dataLength;

    /**
     * 数字类型的实际长度
     */
    private Integer dataPrecision;

    /**
     * 小数位数
     */
    private Integer dataScale;

    /**
     * 是否可以为空，oracle取值为Y或N， MySQL 取值为 NO 或 YES
     */
    private String nullAble;

    /**
     * 列注释
     */
    private String comments;

    /**
     * 是否是主键列 mysql取值为 PRI, oracle 取值为P
     */
    private String primaryKey;


    public Column() {
    }

    public Column(String columnName, String dataType, String columnType, Integer dataLength,
        Integer dataPrecision, Integer dataScale, String nullAble, String comments, String primaryKey) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.columnType = columnType;
        this.dataLength = dataLength;
        this.dataPrecision = dataPrecision;
        this.dataScale = dataScale;
        this.nullAble = nullAble;
        this.comments = comments;
        this.primaryKey = primaryKey;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Integer getDataLength() {
        return dataLength;
    }

    public void setDataLength(Integer dataLength) {
        this.dataLength = dataLength;
    }

    public Integer getDataPrecision() {
        return dataPrecision;
    }

    public void setDataPrecision(Integer dataPrecision) {
        this.dataPrecision = dataPrecision;
    }

    public Integer getDataScale() {
        return dataScale;
    }

    public void setDataScale(Integer dataScale) {
        this.dataScale = dataScale;
    }

    public String getNullAble() {
        return nullAble;
    }

    public void setNullAble(String nullAble) {
        this.nullAble = nullAble;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
