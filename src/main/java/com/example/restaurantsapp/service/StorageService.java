package com.example.restaurantsapp.service;

import com.example.restaurantsapp.dto.FoodMenuDTO;
import com.example.restaurantsapp.dto.ResponseDTO;
import com.example.restaurantsapp.dto.StorageDTO;
import com.example.restaurantsapp.entity.FoodMenuEntity;
import com.example.restaurantsapp.entity.StorageEntity;
import com.example.restaurantsapp.repository.StorageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StorageService {
    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public ResponseDTO<StorageDTO>  create(StorageDTO storageDTO) {

        // check unique product name
        if (!checkUniqueProduct(storageDTO.getProductName()).getSuccess()){
            return new ResponseDTO<>(false, checkUniqueProduct(storageDTO.getProductName()).getMessage());
        }

        // Validate input
        if (Objects.isNull(storageDTO.getProductName())) {
            return new ResponseDTO<>(false, "Product name must be provided");
        }

        if (Objects.isNull(storageDTO.getProductGram())) {
            return new ResponseDTO<>(false, "Product gram must be provided");
        }

        // Create and save entity
        StorageEntity entity = new StorageEntity();
        entity.setProductName(storageDTO.getProductName());
        entity.setProductQuantity(storageDTO.getProductGram());
        StorageEntity savedEntity = storageRepository.save(entity);

        // Return DTO
        return new ResponseDTO<>(mapToDTO(savedEntity), true, "succsess");
    }


    public  List<ResponseDTO<StorageDTO>> getList() {

        List<StorageEntity> allEntities = (List<StorageEntity>) storageRepository.findAll();

        List<ResponseDTO<StorageDTO>> responseDTOList = allEntities.stream()
                .map(entity -> new ResponseDTO<>(mapToDTO(entity), true, "success"))
                .collect(Collectors.toList());

        return responseDTOList;
    }


    public  ResponseDTO<StorageDTO> getProduct(Integer id) {

        Optional<StorageEntity> storageEntityOptional = storageRepository.findById(id);
        if (storageEntityOptional.isEmpty()) {
            // If the entity doesn't exist, return null or throw an exception
            new ResponseDTO<>(false, "StorageEntity with ID " + id + " does not exist.");
        }


        StorageEntity existingEntity = storageEntityOptional.get();

        return new ResponseDTO<>(mapToDTO(existingEntity), true, "succsess");
    }

    public  ResponseDTO<StorageDTO>  update(Integer id, StorageDTO dto) {
        // Check if the storage entity with the given ID exists
        Optional<StorageEntity> storageEntityOptional = storageRepository.findById(id);
        if (storageEntityOptional.isEmpty()) {
            // If the entity doesn't exist, return null or throw an exception
            new ResponseDTO<>(false, "StorageEntity with ID " + id + " does not exist.");
        }

        StorageEntity existingEntity = storageEntityOptional.get();

        // Update the properties of the existing entity with the values from the DTO
        if (dto.getProductName() != null) {
            existingEntity.setProductName(dto.getProductName());
        }
        if (dto.getProductGram() != null) {
            existingEntity.setProductQuantity(dto.getProductGram());
        }

        // Save the updated entity
        StorageEntity updatedEntity = storageRepository.save(existingEntity);

        // Map the updated entity to DTO and return
        return new ResponseDTO<>(mapToDTO(updatedEntity), true, "succsess");
    }

    public ResponseDTO delete(Integer id) {
        // Check if the storage entity with the given ID exists
        if (storageRepository.existsById(id)) {
            // Delete the storage entity by ID
            storageRepository.deleteById(id);
            return new ResponseDTO<>(true, "\"StorageEntity with ID \" + id + \" deleted successfully.\"");
        } else {
            // If the entity doesn't exist, return a message indicating so
            return new ResponseDTO<>(false, "StorageEntity with ID " + id + " does not exist.");
        }
    }



    public ResponseDTO addQuantity(Integer id, Integer productQuantity) {
        // Check if the storage entity with the given ID exists
        Optional<StorageEntity> existingProduct = storageRepository.findById(id);

        // If the entity does not exist, return a response indicating failure
        if (existingProduct.isEmpty()) {
            return new ResponseDTO(false, "Product not found");
        }

        // Retrieve the entity from the optional
        StorageEntity entity = existingProduct.get();

        // Calculate the new quantity
        int newQuantity = entity.getProductQuantity() + productQuantity;

        // Update the quantity in the database
        storageRepository.updateQuantityById(newQuantity, id);

        // Return a response indicating success
        return new ResponseDTO(true, "Success");
    }


    private ResponseDTO checkUniqueProduct(String product){

        Optional<StorageEntity> exisitProductName = storageRepository.findByProductName(product);

        if (exisitProductName.isPresent()){
            return new ResponseDTO<>(false, "Already exsist this product");
        }

        return new ResponseDTO<>(true, "succsess");

    }


    // Utility method to map entity to DTO
    private StorageDTO mapToDTO(StorageEntity entity) {
        return new StorageDTO(entity.getId(), entity.getProductName(), entity.getProductQuantity());
    }


}
