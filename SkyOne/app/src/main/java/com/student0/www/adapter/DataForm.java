package com.student0.www.adapter;

/**
 * Created by willj on 2017/2/24.
 */

public class DataForm {

    private int Type;
    private int Position;
    private String data;

    //
    public DataForm(String data, int position, int type) {
        this.data = data;
        Position = position;
        Type = type;
    }

    public String getData() {
        return data;
    }

    public int getPosition() {
        return Position;
    }

    public int getType() {
        return Type;
    }
    //
}
