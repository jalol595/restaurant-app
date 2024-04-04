package com.example.restaurantsapp.repository;

import com.example.restaurantsapp.entity.FoodMenuEntity;
import com.example.restaurantsapp.entity.IngridentEntity;
import org.springframework.data.repository.CrudRepository;

public interface IngredentRepository extends CrudRepository<IngridentEntity, Integer> {
}
