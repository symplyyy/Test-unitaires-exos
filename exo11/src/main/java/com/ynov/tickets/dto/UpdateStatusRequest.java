package com.ynov.tickets.dto;

import com.ynov.tickets.model.Status;

public class UpdateStatusRequest {

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
