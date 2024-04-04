package com.example.restaurantsapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "storage")
@Getter
@Setter
public class StorageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String productName;

    private Integer productQuantity;


    public StorageEntity() {
    }

    public StorageEntity(String productName, Integer productQuantity) {
        this.productName = productName;
        this.productQuantity = productQuantity;
    }

    public StorageEntity(Integer id, String productName, Integer productQuantity) {
        this.id = id;
        this.productName = productName;
        this.productQuantity = productQuantity;
    }
}
