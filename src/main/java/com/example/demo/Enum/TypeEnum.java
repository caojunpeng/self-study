package com.example.demo.Enum;

import com.example.demo.utils.StringDateUtils;

/**
 * 定义枚举
 */
public enum TypeEnum {
    TYPE1("涉黄","1"),
    TYPE2("涉毒","2"),
    TYPE3("涉赌","3");
    private String name;
    private String type;

    TypeEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public static String getNameByType(String type){
        if(StringDateUtils.isNotBlank(type)){
            for(TypeEnum dt : TypeEnum.values()){
                if(dt.type.equals(type)){
                    return dt.getName();
                }
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
