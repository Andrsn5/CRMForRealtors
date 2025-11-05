package com.company.crm.model;

import java.math.BigDecimal;
import java.time.LocalDate;
public class Deal {
    private Integer id;
    private String dealNumber;
    private Integer taskId;
    private BigDecimal dealAmount;
    private LocalDate dealDate;
    private BigDecimal commission;
    private String status;

    public String getDealNumber() {
        return dealNumber;
    }

    public void setDealNumber(String dealNumber) {
        this.dealNumber = dealNumber;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }

    public LocalDate getDealDate() {
        return dealDate;
    }

    public void setDealDate(LocalDate dealDate) {
        this.dealDate = dealDate;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return String.format("Deal{id=%d, number='%s', taskId=%d, amount=%s, date=%s, commission=%s, status='%s'}",
                id, dealNumber, taskId, dealAmount, dealDate, commission, status);
    }
}