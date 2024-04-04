package com.example.restaurantsapp.repository;

import com.example.restaurantsapp.entity.StorageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StorageRepository extends CrudRepository<StorageEntity, Integer> {


    @Modifying
    @Transactional
    @Query("update  StorageEntity s set s.productQuantity=?1 where s.productName=?2")
    void updateQuantityByName(Integer quantity, String name);

    Optional<StorageEntity> findByProductName(String productName);
    @Modifying
    @Transactional
    @Query("update  StorageEntity s set s.productQuantity=?1 where s.id=?2")
    void updateQuantityById(Integer quantity, Integer id);



}
