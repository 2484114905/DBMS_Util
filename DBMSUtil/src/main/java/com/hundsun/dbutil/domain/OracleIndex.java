package com.hundsun.dbutil.domain;

public class OracleIndex extends Index {
    private String uniqueness;

    public OracleIndex() {
    }

    public String getUniqueness() {
        return uniqueness;
    }

    public void setUniqueness(String uniqueness) {
        this.uniqueness = uniqueness;
    }
}
