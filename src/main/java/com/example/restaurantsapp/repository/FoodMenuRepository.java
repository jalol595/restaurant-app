package com.example.restaurantsapp.repository;

import com.example.restaurantsapp.entity.FoodMenuEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FoodMenuRepository extends CrudRepository<FoodMenuEntity, Integer> {


    Optional<FoodMenuEntity> findByFoodName(String foodName);
}
