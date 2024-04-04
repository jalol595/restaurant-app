package com.example.restaurantsapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {

    T data;

    private Boolean success;

    private String message;

    public ResponseDTO() {
    }

    public ResponseDTO(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }


    public ResponseDTO(T data, Boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }
}

