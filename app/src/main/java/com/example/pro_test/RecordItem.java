package com.example.pro_test;

public class RecordItem {
    public String date, type, result;
    public int rank;
    public float time;

    public RecordItem(String date, String type, String result, int rank, float time) {
        this.date = date;
        this.type = type;
        this.result = result;
        this.rank = rank;
        this.time = time;
    }
}
