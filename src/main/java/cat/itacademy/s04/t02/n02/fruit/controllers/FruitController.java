package cat.itacademy.s04.t02.n02.fruit.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.services.FruitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for fruit CRUD operations.
 * Provides endpoints for creating, reading, updating, and deleting fruits.
 */
@RestController
@RequestMapping("/fruits")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    /**
     * Creates a new fruit.
     * 
     * @param fruitRequestDto The fruit data from the request body
     * @return The created fruit with generated ID
     */
    @PostMapping
    public ResponseEntity<FruitResponseDto> addFruit(@Valid @RequestBody FruitRequestDto fruitRequestDto) {
        FruitResponseDto created = fruitService.addFruit(fruitRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves all fruits, optionally filtered by provider.
     * 
     * @param providerId Optional provider ID to filter fruits
     * @return List of fruits
     */
    @GetMapping
    public ResponseEntity<List<FruitResponseDto>> getAllFruits(@RequestParam(required = false) Long providerId) {
        List<FruitResponseDto> fruits;
        if (providerId != null) {
            fruits = fruitService.getFruitsByProviderId(providerId);
        } else {
            fruits = fruitService.getAllFruits();
        }
        return ResponseEntity.ok(fruits);
    }

    /**
     * Retrieves a fruit by its ID.
     * 
     * @param id The fruit ID
     * @return The fruit data
     */
    @GetMapping("/{id}")
    public ResponseEntity<FruitResponseDto> getFruitById(@PathVariable Long id) {
        FruitResponseDto fruit = fruitService.getFruitById(id);
        return ResponseEntity.ok(fruit);
    }

    /**
     * Updates an existing fruit.
     * 
     * @param id              The fruit ID to update
     * @param fruitRequestDto The updated fruit data
     * @return The updated fruit
     */
    @PutMapping("/{id}")
    public ResponseEntity<FruitResponseDto> updateFruit(@PathVariable Long id,
                                                  @Valid @RequestBody FruitRequestDto fruitRequestDto) {
        FruitResponseDto updated = fruitService.updateFruit(id, fruitRequestDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a fruit by its ID.
     * 
     * @param id The fruit ID to delete
     * @return No content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFruit(@PathVariable Long id) {
        fruitService.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }
}