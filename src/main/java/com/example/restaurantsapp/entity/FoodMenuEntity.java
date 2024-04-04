package com.example.restaurantsapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food_menu")
@Getter
@Setter
public class FoodMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String foodName;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ingrident_id")
    private List<IngridentEntity> ingridentEntities;

    @Column
    private String foodPrice;

}
