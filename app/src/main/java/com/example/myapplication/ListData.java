package com.example.myapplication;

public class ListData {
    private int text_data_id;
    private String text_data_str;
    private int text_data_len;

    public void setId(int id) {
        this.text_data_id = id;
    }

    public void setTextData_str(String str){
        this.text_data_str = str;
    }

    public void setTextData_len(int len){
        this.text_data_len = len;
    }

    public int getId() {
        return text_data_id;
    }

    public String getTextData_str(){
        return text_data_str;
    }
    public int getTextData_len(){
        return text_data_len;
    }
}

