package cat.itacademy.s04.t02.n02.fruit.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitDto;
import cat.itacademy.s04.t02.n02.fruit.services.FruitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {

    private final FruitService fruitService;

    public FruitController(FruitService fruitService) {
        this.fruitService = fruitService;
    }

    @PostMapping
    public ResponseEntity<FruitDto> addFruit(@Valid @RequestBody FruitDto fruitDto) {
        FruitDto created = fruitService.addFruit(fruitDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<FruitDto>> getAllFruits(@RequestParam(required = false) Long providerId) {
        List<FruitDto> fruits;
        if (providerId != null) {
            fruits = fruitService.getFruitsByProviderId(providerId);
        } else {
            fruits = fruitService.getAllFruits();
        }
        return ResponseEntity.ok(fruits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitDto> getFruitById(@PathVariable Long id) {
        FruitDto fruit = fruitService.getFruitById(id);
        return ResponseEntity.ok(fruit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitDto> updateFruit(@PathVariable Long id,
                                                  @Valid @RequestBody FruitDto fruitDto) {
        FruitDto updated = fruitService.updateFruit(id, fruitDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFruit(@PathVariable Long id) {
        fruitService.deleteFruit(id);
        return ResponseEntity.noContent().build();
    }
}