package com.airbyte.dorm.model;

import com.airbyte.dorm.common.FileConverter;
import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.model.enums.PaymentType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Table(name = "payment_history")
@Entity
public class PaymentHistory implements Serializable, Comparable<PaymentHistory> {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "TIMESTAMP") Date date;
    private @Embedded Money amount;
    private @Column(columnDefinition = "VARCHAR(50)") String type;
    private @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)") PaymentType paymentType;
    private @Column(columnDefinition = "VARCHAR(4000)") String description;
    private @Convert(converter = FileConverter.class) @Column(columnDefinition = "VARCHAR(255)") Map<String, String> file;
    private @Column(columnDefinition = "VARCHAR(50)") String parentId;
    private @Column(columnDefinition = "VARCHAR(50)") String parentType;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getDate() {
        return TimeConverter.convert(this.date, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getFile() {
        return file;
    }

    public void setFile(Map<String, String> file) {
        this.file = file;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    @Override
    public int compareTo(PaymentHistory another) {
        return this.date.compareTo(another.date);
    }
}
