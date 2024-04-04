package com.example.restaurantsapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ingeredent")
@Getter
@Setter
public class IngridentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String name;

    private Integer quantity;



    public IngridentEntity() {
    }

    public IngridentEntity(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public IngridentEntity(Integer id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}
