package com.tsystems.javaschool.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 23.09.16.
 */
public class TableResponse {
    Long recordsTotal;
    Integer draw;
    List<Object> data = new ArrayList<>();

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
