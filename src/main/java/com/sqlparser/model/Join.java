package com.sqlparser.model;

public class Join {
    private String joinType;
    private String tableName;
    private String alias;
    private String joinCondition;

    public Join(String joinType, String tableName, String alias, String joinCondition) {
        this.joinType = joinType;
        this.tableName = tableName;
        this.alias = alias;
        this.joinCondition = joinCondition;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getJoinCondition() {
        return joinCondition;
    }

    public void setJoinCondition(String joinCondition) {
        this.joinCondition = joinCondition;
    }
}
