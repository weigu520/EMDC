package com.briup.environment.bean;

public enum DataType {
    CO2("二氧化碳"),TMP("温度"),HUM("湿度"),ILL("光照强度");

    private String name;
    DataType(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
