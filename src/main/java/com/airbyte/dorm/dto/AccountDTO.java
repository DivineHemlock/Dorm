package com.airbyte.dorm.dto;

import java.math.BigDecimal;

public class AccountDTO extends ParentDTO{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
