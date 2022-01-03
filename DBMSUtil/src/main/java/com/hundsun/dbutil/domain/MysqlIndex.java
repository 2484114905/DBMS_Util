package com.hundsun.dbutil.domain;

public class MysqlIndex extends Index {
    private Integer nonUnique;

    public MysqlIndex() {
    }

    public Integer getNonUnique() {
        return nonUnique;
    }

    public void setNonUnique(Integer nonUnique) {
        this.nonUnique = nonUnique;
    }
}
