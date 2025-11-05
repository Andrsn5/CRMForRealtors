package com.company.crm.model;

import java.time.LocalDate;
public class AdditionalCondition {
    private Integer id;
    private String conditionType;
    private String description;
    private LocalDate deadline;
    private boolean required = false;
    private String status = "активно";
    private String priority = "средний";
    private String notes;

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return String.format("AdditionalCondition{id=%d, type='%s', description='%s', deadline=%s, required=%s, status='%s', priority='%s'}",
                id, conditionType, description, deadline, required, status, priority);
    }
}