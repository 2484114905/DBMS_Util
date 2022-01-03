package com.hundsun.dbutil.domain;

/**
* 索引信息的领域对象
* @author wangyang31647
* @date 2020/08/10
*/
public class Index {

    /**
     * 索引名
     */
    private String indexName;

    /**
     * 与索引相关的字段名
     */
    private String columnName;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
