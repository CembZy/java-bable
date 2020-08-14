package com.cemb.mybatis.config;

import java.util.ArrayList;
import java.util.List;

//存储具体的mapper的信息
public class MappedStatement<T> {

    //空间
    private String namespace;

    //空间+id
    private String sourceId;

    //sql
    private String sql;

    //返回结果
    private String resultType;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
