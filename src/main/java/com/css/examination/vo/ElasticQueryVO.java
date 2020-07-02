package com.css.examination.vo;

import java.io.Serializable;

public class ElasticQueryVO implements Serializable {
    private String indexName;
    private String queryColunm;
    private String queryValue;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getQueryColunm() {
        return queryColunm;
    }

    public void setQueryColunm(String queryColunm) {
        this.queryColunm = queryColunm;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(String queryValue) {
        this.queryValue = queryValue;
    }
}