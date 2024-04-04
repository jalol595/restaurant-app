package com.example.restaurantsapp.controller;

import com.example.restaurantsapp.dto.FoodMenuDTO;
import com.example.restaurantsapp.dto.ResponseDTO;
import com.example.restaurantsapp.dto.StorageDTO;
import com.example.restaurantsapp.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storage")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }



//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cretate")
    public ResponseEntity<ResponseDTO<StorageDTO>> cretate(@RequestBody StorageDTO storageDTO) {
        ResponseDTO<StorageDTO> storageDTOResponseDTO = storageService.create(storageDTO);
        return ResponseEntity.ok(storageDTOResponseDTO);
    }


    @GetMapping("/getProductList")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResponseDTO<StorageDTO>>> getProductList() {
        List<ResponseDTO<StorageDTO>> list = storageService.getList();
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/update/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    private ResponseEntity<ResponseDTO<StorageDTO>> update(@PathVariable("id") Integer id, @RequestBody StorageDTO dto) {
        ResponseDTO<StorageDTO> update = storageService.update(id, dto);
        return ResponseEntity.ok().body(update);
    }



    @PutMapping("/addQuantity/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<StorageDTO>> addQuantity(@PathVariable("id") Integer id, @RequestBody StorageDTO dto) {
        ResponseDTO<StorageDTO> product = storageService.addQuantity(id, dto.getProductGram());
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    private ResponseEntity<ResponseDTO<StorageDTO>> delete(@PathVariable("id") Integer id) {
        ResponseDTO delete = storageService.delete(id);
        return ResponseEntity.ok().body(delete);
    }

}
