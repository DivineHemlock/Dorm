package com.airbyte.dorm.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;
@Embeddable
public class Money implements Serializable {
    private @Column(columnDefinition = "VARCHAR(30)") String unit;
    private @Column(columnDefinition = "NUMERIC(20, 2)") BigDecimal value;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Money(){}

    public Money(String unit, BigDecimal value) {
        this.unit = unit;
        this.value = value;
    }
}
