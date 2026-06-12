package com.ynov.tickets.dto;

import com.ynov.tickets.model.Priority;

public class CreateTicketRequest {

    private String title;
    private Priority priority;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
