package cat.itacademy.s04.t02.n01.fruit_api_h2.services;

import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.fruit_api_h2.model.Fruit;
import cat.itacademy.s04.t02.n01.fruit_api_h2.repository.FruitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitService {

    private final FruitRepository fruitRepository;

    public FruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    public FruitDto addFruit(FruitDto fruitDto) {
        Fruit fruit = fruitDto.toEntity();
        Fruit savedFruit = fruitRepository.save(fruit);
        return FruitDto.fromEntity(savedFruit);
    }

    public List<FruitDto> getAllFruits() {
        return fruitRepository.findAll().stream()
                .map(FruitDto::fromEntity)
                .toList();
    }

    public FruitDto getFruitById(Long id) {
        return fruitRepository.findById(id)
                .map(FruitDto::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found with id: " + id));
    }

    public FruitDto updateFruit(Long id, FruitDto fruitDto) {
        if (!fruitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fruit not found with id: " + id);
        }
        fruitDto.setId(id);
        Fruit fruit = fruitDto.toEntity();
        Fruit updatedFruit = fruitRepository.save(fruit);
        return FruitDto.fromEntity(updatedFruit);
    }

    public void deleteFruit(Long id) {
        if (!fruitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fruit not found with id: " + id);
        }
        fruitRepository.deleteById(id);
    }

}