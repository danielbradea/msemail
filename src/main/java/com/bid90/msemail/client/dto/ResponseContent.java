package com.bid90.msemail.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseContent<T> extends Response {
    T content;
    public ResponseContent(T content) {
        this.content = content;
    }

    public ResponseContent(T content, boolean success, String message){
        this.content = content;
        this.message = message;
        this.success = success;
    }
}
