package com.example.restaurantsapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StorageDTO {

    private Integer id;

    private String productName;

    private Integer productGram;

    public StorageDTO() {

    }

    public StorageDTO(Integer id, String productName, Integer productGram) {
        this.id = id;
        this.productName = productName;
        this.productGram = productGram;
    }
}
