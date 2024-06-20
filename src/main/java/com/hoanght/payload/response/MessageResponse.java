package com.hoanght.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    private String message;
    private int status;

    public MessageResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
