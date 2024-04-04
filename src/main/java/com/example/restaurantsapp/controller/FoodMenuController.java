package com.example.restaurantsapp.controller;


import com.example.restaurantsapp.dto.FoodMenuDTO;
import com.example.restaurantsapp.dto.ResponseDTO;
import com.example.restaurantsapp.service.FoodMenuService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-menu")
public class FoodMenuController {

    private final FoodMenuService foodMenuService;

    public FoodMenuController(FoodMenuService foodMenuService) {
        this.foodMenuService = foodMenuService;
    }



    @PostMapping("/cretate")
    public ResponseEntity<ResponseDTO<FoodMenuDTO>> cretate(@RequestBody FoodMenuDTO foodMenuDTO) {
        ResponseDTO<FoodMenuDTO> foodMenuDTOResponseDTO = foodMenuService.create(foodMenuDTO);
        return ResponseEntity.ok(foodMenuDTOResponseDTO);
    }


    @GetMapping("/getProductList")
    public ResponseEntity<List<ResponseDTO<FoodMenuDTO>>> getProductList() {
        List<ResponseDTO<FoodMenuDTO>> list = foodMenuService.getList();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<ResponseDTO<FoodMenuDTO>> getProduct(@PathVariable("id") Integer id) {
        ResponseDTO<FoodMenuDTO> foodMenu = foodMenuService.getFoodMenu(id);
        return ResponseEntity.ok().body(foodMenu);
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<ResponseDTO<FoodMenuDTO>> update(@PathVariable("id") Integer id, @RequestBody FoodMenuDTO dto) {
        ResponseDTO<FoodMenuDTO> update = foodMenuService.update(id, dto);
        return ResponseEntity.ok().body(update);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<ResponseDTO<FoodMenuDTO>> delete(@PathVariable("id") Integer id) {
        ResponseDTO delete = foodMenuService.delete(id);
        return ResponseEntity.ok().body(delete);
    }

}
