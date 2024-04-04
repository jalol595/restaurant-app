package com.example.restaurantsapp.dto;

import com.example.restaurantsapp.entity.FoodMenuEntity;
import com.example.restaurantsapp.util.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {


    private Integer id;

    private Integer tableNumber;

    private Map<String, Integer> foods;

    private String foodName;

    private Integer contingent;

    private String date;


    private String status;



    public OrderDTO() {
    }

    public OrderDTO(Integer tableNumber, String foodName, Integer contingent, String date) {
        this.tableNumber = tableNumber;
        this.foodName = foodName;
        this.contingent = contingent;
        this.date = date;
    }

    public OrderDTO(Integer tableNumber, String foodName, Integer contingent, String date, String status) {
        this.tableNumber = tableNumber;
        this.foodName = foodName;
        this.contingent = contingent;
        this.date = date;
        this.status = status;
    }


    //    public OrderDTO(Integer id, Integer tableNumber, List<FoodMenuEntity> menuEntities) {
//        this.id = id;
//        this.tableNumber = tableNumber;
//        this.foods = menuEntities;
//    }
//
//    public OrderDTO(Integer tableNumber, List<FoodMenuEntity> menuEntities, Integer contingent) {
//        this.tableNumber = tableNumber;
//        this.foods = menuEntities;
//        this.contingent = contingent;
//    }
}
