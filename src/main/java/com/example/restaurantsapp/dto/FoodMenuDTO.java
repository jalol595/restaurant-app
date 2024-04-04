package com.example.restaurantsapp.dto;


import com.example.restaurantsapp.entity.IngridentEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodMenuDTO {

    private Integer id;

    private String foodName;

    private List<IngridentDTO> ingridentDTO;

    private String foodPrice;


    public FoodMenuDTO() {
    }

    public FoodMenuDTO(Integer id, String foodName, List<IngridentDTO> ingridentEntities, String foodPrice) {
        this.id = id;
        this.foodName = foodName;
        this.ingridentDTO = ingridentEntities;
        this.foodPrice = foodPrice;
    }
}
