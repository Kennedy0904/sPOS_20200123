package com.example.dell.smartpos.Settings.BinRange;

public class RangeProduct {

    public int id;
    public String rangeStart, rangeEnd;

    public RangeProduct(int id, String rangeStart, String rangeEnd) {
        this.id = id;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(String rangeStart){
        this.rangeStart = rangeStart;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(String rangeEnd){
        this.rangeEnd = rangeEnd;
    }
}
