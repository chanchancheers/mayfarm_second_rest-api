package com.mayfarm.rest_api.entity;
import org.springframework.core.annotation.Order;

public enum OrderKey {
    ID("ORDER BY id"),
    ID_DESC("ORDER BY id DESC"),
    CREATED_DATE("ORDER BY created_date"),
    CREATED_DATE_DESC("ORDER BY created_date DESC"),
    MODIFIED_DATE("ORDER BY modified_date"),
    MODIFIED_DATE_DESC("ORDER BY modified_date DESC")
  ;
    private final String value;

    OrderKey(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
//    private final String id = "id";
//
//    private final String createdDate = "created_date";
//    private final String modifiedDate = "modified_date";
//



}
