package com.example.restaurantsapp.service;

import com.example.restaurantsapp.dto.OrderDTO;
import com.example.restaurantsapp.dto.ResponseDTO;
import com.example.restaurantsapp.entity.FoodMenuEntity;
import com.example.restaurantsapp.entity.OrderEntity;
import com.example.restaurantsapp.entity.StorageEntity;
import com.example.restaurantsapp.repository.FoodMenuRepository;
import com.example.restaurantsapp.repository.OrderRepository;
import com.example.restaurantsapp.util.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrderService {


    private OrderRepository orderRepository;

    private final FoodMenuRepository foodMenuRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, FoodMenuRepository foodMenuRepository) {
        this.orderRepository = orderRepository;
        this.foodMenuRepository = foodMenuRepository;
    }


    public List<ResponseDTO<OrderDTO>> create(OrderDTO orderDTO) {


        // check table status
        if (!checkTable(orderDTO.getTableNumber()).getSuccess()) {
            return List.of(new ResponseDTO<>(false, checkTable(orderDTO.getTableNumber()).getMessage()));
        }


        // Validate input using DTO annotations
        if (Optional.ofNullable(orderDTO.getTableNumber()).isEmpty()) {
            return List.of(new ResponseDTO<>(null, false, "Table number must be provided"));
        }

        if (Optional.ofNullable(orderDTO.getFoods()).isEmpty()) {
            return List.of(new ResponseDTO<>(null, false, "Food must be provided"));
        }

        // Create and save entities
        List<ResponseDTO<OrderDTO>> responseDTOs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : orderDTO.getFoods().entrySet()) {
            OrderEntity entity = new OrderEntity();
            entity.setTableNumber(orderDTO.getTableNumber());

            if (!validateIngeredents(entry.getKey())) {
                return List.of(new ResponseDTO<>(false, "Not found food"));
            }

            entity.setFoodName(entry.getKey());
            entity.setContingent(entry.getValue());
            entity.setStatus(OrderStatus.NEW);
            OrderEntity savedEntity = orderRepository.save(entity);


            responseDTOs.add(new ResponseDTO<>(mapToDTO(savedEntity), true, "success"));
        }

        return responseDTOs;
    }


    public List<ResponseDTO<OrderDTO>> getList() {

        Iterable<OrderEntity> allEntities = orderRepository.findAll();

        // Map entities to DTOs
        List<OrderDTO> dtoList = StreamSupport.stream(allEntities.spliterator(), false).map(this::mapToDTO).collect(Collectors.toList());

        // Create ResponseDTO objects for each DTO
        List<ResponseDTO<OrderDTO>> responseList = dtoList.stream().map(dto -> new ResponseDTO<>(dto, true, "Success")).collect(Collectors.toList());

        return responseList;
    }

    public List<ResponseDTO<OrderDTO>> getAllOrderListByStatusNew() {

        Iterable<OrderEntity> allEntities = orderRepository.getAllOrderListByStatusNew();

        // Map entities to DTOs
        List<OrderDTO> dtoList = StreamSupport.stream(allEntities.spliterator(), false).map(this::mapToDTO).collect(Collectors.toList());

        // Create ResponseDTO objects for each DTO
        List<ResponseDTO<OrderDTO>> responseList = dtoList.stream().map(dto -> new ResponseDTO<>(dto, true, "Success")).collect(Collectors.toList());

        return responseList;
    }

    public List<ResponseDTO<OrderDTO>> getAllOrderListByStatusPrepered() {

        Iterable<OrderEntity> allEntities = orderRepository.getAllOrderListByStatusPrepered();

        // Map entities to DTOs
        List<OrderDTO> dtoList = StreamSupport.stream(allEntities.spliterator(), false).map(this::mapToDTO).collect(Collectors.toList());

        // Create ResponseDTO objects for each DTO
        List<ResponseDTO<OrderDTO>> responseList = dtoList.stream().map(dto -> new ResponseDTO<>(dto, true, "Success")).collect(Collectors.toList());

        return responseList;
    }


    public List<ResponseDTO<OrderDTO>> getAllOrderListByStatusFinshed() {

        Iterable<OrderEntity> allEntities = orderRepository.getAllOrderListByStatusFinshed();

        // Map entities to DTOs
        List<OrderDTO> dtoList = StreamSupport.stream(allEntities.spliterator(), false).map(this::mapToDTO).collect(Collectors.toList());

        // Create ResponseDTO objects for each DTO
        List<ResponseDTO<OrderDTO>> responseList = dtoList.stream().map(dto -> new ResponseDTO<>(dto, true, "Success")).collect(Collectors.toList());

        return responseList;
    }

    public ResponseDTO<OrderDTO> getOrder(Integer id) {

        Optional<OrderEntity> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            // If the entity doesn't exist, return null or throw an exception
            return new ResponseDTO<>(false, "OrderEntity with ID " + id + " does not exist.");
        }

        OrderEntity existingEntity = orderOptional.get();

        return new ResponseDTO<>(mapToDTO(existingEntity), true, "succses");

    }


    public ResponseDTO<OrderDTO> changeStatusPREPERATED(Integer id) {

        Optional<OrderEntity> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            // If the entity doesn't exist, return null or throw an exception
            return new ResponseDTO<>(false, "OrderEntity with ID " + id + " does not exist.");
        }

        OrderEntity existingEntity = orderOptional.get();

        existingEntity.setStatus(OrderStatus.valueOf(String.valueOf(OrderStatus.PREPERATED)));

        OrderEntity save = orderRepository.save(existingEntity);

        return new ResponseDTO<>(mapToDTO(save), true, "succses");

    }

    public ResponseDTO<OrderDTO> changeStatusFINESHED(Integer id) {

        Optional<OrderEntity> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            // If the entity doesn't exist, return null or throw an exception
            return new ResponseDTO<>(false, "OrderEntity with ID " + id + " does not exist.");
        }

        OrderEntity existingEntity = orderOptional.get();

        existingEntity.setStatus(OrderStatus.valueOf(String.valueOf(OrderStatus.FINESHED)));

        OrderEntity save = orderRepository.save(existingEntity);

        return new ResponseDTO<>(mapToDTO(save), true, "succses");

    }


    public ResponseDTO delete(Integer id) {

        if (orderRepository.existsById(id)) {
            // Delete the storage entity by ID
            orderRepository.deleteById(id);
            return new ResponseDTO<>(true, "OrderEntity with ID " + id + " deleted successfully.");
        } else {
            // If the entity doesn't exist, return a message indicating so
            return new ResponseDTO<>(false, "OrderEntity with ID " + id + " does not exist.");
        }
    }


    private ResponseDTO checkTable(Integer tabNum) {

        List<OrderEntity> byIdAndTableNumber = orderRepository.findByIdAndTableNumber(tabNum);

        if (!byIdAndTableNumber.isEmpty()) {
            return new ResponseDTO<>(false, "this table busy");

        }

        List<OrderEntity> byIdAndTableNumberAndNew = orderRepository.findByIdAndTableNumberNEW(tabNum);

        if (!byIdAndTableNumberAndNew.isEmpty()) {
            return new ResponseDTO<>(false, "this table busy");

        }

        return new ResponseDTO<>(true, "succsess");

    }

    // Utility method to map entity to DTO
    private OrderDTO mapToDTO(OrderEntity entity) {
        return new OrderDTO(entity.getTableNumber(), entity.getFoodName(), entity.getContingent(), String.valueOf(entity.getCreatedDate()), String.valueOf(entity.getStatus()));

    }


    private Boolean validateIngeredents(String foodName) {

        Optional<FoodMenuEntity> foodMenu = foodMenuRepository.findByFoodName(foodName);
        if (foodMenu.isEmpty()) {
            return false;
        }
        return true;
    }

}
