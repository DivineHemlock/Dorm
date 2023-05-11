package com.airbyte.dorm.model;

import com.airbyte.dorm.common.TimeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class TimePeriod implements Serializable {
    private @Column(columnDefinition = "TIMESTAMP") Date startDate;
    private @Column(columnDefinition = "TIMESTAMP") Date endDate;

    public String getStartDate() {
        return TimeConverter.convert(this.startDate, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return TimeConverter.convert(this.endDate, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    @JsonIgnore
    public Date endDate() {
        return this.endDate;
    }

    @JsonIgnore
    public Date startDate() {
        return this.startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public TimePeriod() {}

    public TimePeriod(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }


}
