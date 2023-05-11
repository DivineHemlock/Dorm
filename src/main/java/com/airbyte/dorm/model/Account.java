package com.airbyte.dorm.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "account")
public class Account {
    @Id
    private @Column(columnDefinition = "VARCHAR(50)" , nullable = false) String id;
    private @Column(columnDefinition = "NUMERIC(20,2)") BigDecimal totalPayment = new BigDecimal("0");
    private @Column(columnDefinition = "NUMERIC(20,2)") BigDecimal totalReceived = new BigDecimal("0");
    private @Column(columnDefinition = "VARCHAR(50)") String name;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-","");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(BigDecimal totalReceived) {
        this.totalReceived = totalReceived;
    }
}
