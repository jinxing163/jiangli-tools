package com.jiangli.commons.client.proto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jiangli
 * @date 2018/2/28 16:51
 */
public class PageRecords<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long totalRecords=0L;
    private Long nextId=-1L;
    private List<T> records;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }

    @Override
    public String toString() {
        return "PageRecords{" +
                "totalRecords=" + totalRecords +
                ", nextId=" + nextId +
                ", records=" + records +
                '}';
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public PageRecords setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
        return this;
    }

    public List<T> getRecords() {
        return records;
    }

    public PageRecords setRecords(List<T> records) {
        this.records = records;
        return this;
    }
}
