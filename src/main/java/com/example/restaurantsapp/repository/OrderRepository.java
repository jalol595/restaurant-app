package com.example.restaurantsapp.repository;

import com.example.restaurantsapp.entity.OrderEntity;
import com.example.restaurantsapp.entity.StorageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {


    @Query(value = "select * from order_food b where  b.status='NEW'",
            nativeQuery = true)
    List<OrderEntity> getAllOrderListByStatusNew();


    @Query(value = "select * from order_food b where  b.status='PREPERATED'",
            nativeQuery = true)
    List<OrderEntity> getAllOrderListByStatusPrepered();


    @Query(value = "select * from order_food b where  b.status='FINESHED'",
            nativeQuery = true)
    List<OrderEntity> getAllOrderListByStatusFinshed();

    @Query(value = "select * from order_food b where b.table_number =:tableNumber and  b.status='PREPERATED'",
            nativeQuery = true)
    List<OrderEntity> findByIdAndTableNumber(Integer tableNumber);


    @Query(value = "select * from order_food b where b.table_number =:tableNumber and  b.status='NEW'",
            nativeQuery = true)
    List<OrderEntity> findByIdAndTableNumberNEW(Integer tableNumber);


}
