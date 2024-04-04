package com.example.restaurantsapp.service;

import com.example.restaurantsapp.dto.FoodMenuDTO;
import com.example.restaurantsapp.dto.IngridentDTO;
import com.example.restaurantsapp.dto.ResponseDTO;
import com.example.restaurantsapp.entity.FoodMenuEntity;
import com.example.restaurantsapp.entity.IngridentEntity;
import com.example.restaurantsapp.entity.StorageEntity;
import com.example.restaurantsapp.repository.FoodMenuRepository;
import com.example.restaurantsapp.repository.IngredentRepository;
import com.example.restaurantsapp.repository.StorageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodMenuService {
    private final FoodMenuRepository foodMenuRepository;
    private final IngredentRepository ingridentRepository;
    private final StorageRepository storageRepository;

    public FoodMenuService(FoodMenuRepository foodMenuRepository, IngredentRepository ingridentRepository, StorageRepository storageRepository) {
        this.foodMenuRepository = foodMenuRepository;
        this.ingridentRepository = ingridentRepository;
        this.storageRepository = storageRepository;
    }

    public ResponseDTO<FoodMenuDTO> create(FoodMenuDTO foodmenuDTO) {

        // check unique food name
        if (!checkUniqueFood(foodmenuDTO.getFoodName()).getSuccess()){
            return new ResponseDTO<>(false, checkUniqueFood(foodmenuDTO.getFoodName()).getMessage());
        }

        // Validate input
        ResponseDTO<?> responseDTO = validateInput(foodmenuDTO);

        if (!responseDTO.getSuccess()) {
            return new ResponseDTO<>(null, responseDTO.getSuccess(), responseDTO.getMessage());
        }
        // Validate ingeredents
        ResponseDTO<?> response = validateIngeredents(foodmenuDTO.getIngridentDTO());

        if (!response.getSuccess()) {
            return new ResponseDTO<>(null, response.getSuccess(), response.getMessage());
        }

        // Create entity
        FoodMenuEntity entity = new FoodMenuEntity();
        entity.setFoodName(foodmenuDTO.getFoodName());
        entity.setFoodPrice(foodmenuDTO.getFoodPrice());

        List<IngridentEntity> ingredientEntities = foodmenuDTO.getIngridentDTO().stream()
                .map(this::mapToIngredientEntity)
                .map(ingridentRepository::save)
                .collect(Collectors.toList());
        entity.setIngridentEntities(ingredientEntities);


        // Save entity
        FoodMenuEntity savedEntity = foodMenuRepository.save(entity);
        FoodMenuDTO foodMenuDTO = mapToDTO(savedEntity);

        // Return DTO
        return new ResponseDTO<>(foodMenuDTO, true, "succsess");
    }


    public List<ResponseDTO<FoodMenuDTO>> getList() {

        List<FoodMenuEntity> allEntities = (List<FoodMenuEntity>) foodMenuRepository.findAll();

        List<ResponseDTO<FoodMenuDTO>> responseDTOList = allEntities.stream()
                .map(entity -> new ResponseDTO<>(mapToDTO(entity), true, "success"))
                .collect(Collectors.toList());

        return responseDTOList;
    }


    public ResponseDTO<FoodMenuDTO> getFoodMenu(Integer id) {

        Optional<FoodMenuEntity> foodMenuOptional = foodMenuRepository.findById(id);
        if (foodMenuOptional.isEmpty()) {
            // If the entity doesn't exist, return null or throw an exception
            throw new EntityNotFoundException("FoodMenuEntity with ID " + id + " does not exist.");
        }

        FoodMenuEntity existingEntity = foodMenuOptional.get();

        return new ResponseDTO<>(mapToDTO(existingEntity), true, "succsess");

    }

    public ResponseDTO<FoodMenuDTO> update(Integer id, FoodMenuDTO foodmenuDTO) {

        // Check if the storage entity with the given ID exists
        Optional<FoodMenuEntity> foodMenuOptional = foodMenuRepository.findById(id);
        if (foodMenuOptional.isEmpty()) {
            // If the entity doesn't exist, return null or throw an exception
            return new ResponseDTO<>(null, false, "FoodMenuEntity with ID " + id + " does not exist.");
        }

        // Validate input
        ResponseDTO<?> responseDTO = validateInput(foodmenuDTO);
        if (!responseDTO.getSuccess()) {
            return new ResponseDTO<>(null, responseDTO.getSuccess(), responseDTO.getMessage());
        }
        // Validate ingeredents
        ResponseDTO<?> response = validateIngeredents(foodmenuDTO.getIngridentDTO());

        if (!response.getSuccess()) {
            return new ResponseDTO<>(null, response.getSuccess(), response.getMessage());
        }

        // Get the existing storage entity
        FoodMenuEntity existingEntity = foodMenuOptional.get();

        // Update the properties of the existing entity with the values from the DTO
        if (foodmenuDTO.getFoodName() != null) {
            existingEntity.setFoodName(foodmenuDTO.getFoodName());
        }

        if (foodmenuDTO.getIngridentDTO() != null) {
            List<IngridentEntity> ingredientEntities = foodmenuDTO.getIngridentDTO().stream()
                    .map(this::mapToIngredientEntity)
                    .map(ingridentRepository::save)
                    .collect(Collectors.toList());
            existingEntity.setIngridentEntities(ingredientEntities);
        }

        if (foodmenuDTO.getFoodPrice() != null) {
            existingEntity.setFoodPrice(foodmenuDTO.getFoodPrice());
        }

        // Save the updated entity
        FoodMenuEntity updatedEntity = foodMenuRepository.save(existingEntity);

        // Map the updated entity to DTO and return
        return new ResponseDTO<>(mapToDTO(updatedEntity), true, "succsess");
    }


    public ResponseDTO delete(Integer id) {
        // Check if the storage entity with the given ID exists
        if (foodMenuRepository.existsById(id)) {
            // Delete the storage entity by ID
            foodMenuRepository.deleteById(id);
            return new ResponseDTO<>(true, "\"FoodMenuEntity with ID \" + id + \" deleted successfully.\"");
        } else {
            // If the entity doesn't exist, return a message indicating so
            return new ResponseDTO<>(false, "FoodMenuEntity with ID " + id + " does not exist.");
        }
    }

    private ResponseDTO<?> validateInput(FoodMenuDTO foodmenuDTO) {

        if (Objects.isNull(foodmenuDTO.getFoodName())) {
            return new ResponseDTO<>(false, "Food name must be provided");
        }

        if (Objects.isNull(foodmenuDTO.getFoodPrice())) {
            return new ResponseDTO<>(false, "Food price must be provided");
        }

        if (Objects.isNull(foodmenuDTO.getIngridentDTO()) || foodmenuDTO.getIngridentDTO().isEmpty()) {
            return new ResponseDTO<>(false, "Food ingredients must be provided");
        }

        return new ResponseDTO<>(true, "success");
    }


    private ResponseDTO<?> validateIngeredents(List<IngridentDTO> ingridentDTO) {
        for (IngridentDTO dto : ingridentDTO) {
            ResponseDTO<?> responseDTO = checkProduct(dto);
            if (!responseDTO.getSuccess()) {
                return new ResponseDTO<>(false, responseDTO.getMessage());
            }
        }
        return new ResponseDTO<>(true, "success");
    }


    public ResponseDTO<?> checkProduct(IngridentDTO ingredientDTO) {
        // Validate parameter
        if (ingredientDTO == null) {
            return new ResponseDTO<>(false, "IngredientDTO cannot be null");
        }

        // Check if the product exists in the storage
        Optional<StorageEntity> storageOptional = storageRepository.findByProductName(ingredientDTO.getName());
        if (storageOptional.isEmpty()) {
            return new ResponseDTO<>(false, "Product '" + ingredientDTO.getName() + "' not found");
        }

        // Retrieve the storage entity
        StorageEntity storageEntity = storageOptional.get();

        // Check if the quantity is sufficient
        if (storageEntity.getProductQuantity() < ingredientDTO.getQuantity()) {
            return new ResponseDTO<>(false, "Product '" + ingredientDTO.getName() + "' quantity is insufficient");
        }

        int residualQuantity = storageEntity.getProductQuantity() - ingredientDTO.getQuantity();

        storageRepository.updateQuantityByName(residualQuantity, storageEntity.getProductName());

        // Return the input ingredientDTO if checks pass
        return new ResponseDTO<>(true, "success");
    }

    private ResponseDTO checkUniqueFood(String food){

        Optional<FoodMenuEntity> exisitProductName = foodMenuRepository.findByFoodName(food);

        if (exisitProductName.isPresent()){
            return new ResponseDTO<>(false, "Already exsist this food in menu");
        }

        return new ResponseDTO<>(true, "succsess");

    }

    private IngridentEntity mapToIngredientEntity(IngridentDTO ingredientDTO) {

        IngridentEntity ingredientEntity = new IngridentEntity();
        ingredientEntity.setName(ingredientDTO.getName());
        ingredientEntity.setQuantity(ingredientDTO.getQuantity());
        return ingredientEntity;
    }

    private IngridentDTO mapToIngredientDTO(IngridentEntity ingridentEntity) {
        IngridentDTO ingrident = new IngridentDTO();
        ingrident.setName(ingridentEntity.getName());
        ingrident.setQuantity(ingridentEntity.getQuantity());
        return ingrident;
    }

    private FoodMenuDTO mapToDTO(FoodMenuEntity entity) {
        List<IngridentDTO> responseDTO = new ArrayList<>();
        FoodMenuDTO dto = new FoodMenuDTO();
        dto.setId(entity.getId());
        dto.setFoodName(entity.getFoodName());
        dto.setFoodPrice(entity.getFoodPrice());

        for (IngridentEntity ingridentEntity : entity.getIngridentEntities()) {
            IngridentDTO ingridentDTO = mapToIngredientDTO(ingridentEntity);
            responseDTO.add(ingridentDTO);
        }

        dto.setIngridentDTO(responseDTO);
        return dto;
    }


}
