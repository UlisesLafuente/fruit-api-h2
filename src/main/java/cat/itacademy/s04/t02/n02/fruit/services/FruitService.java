package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n02.provider.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.common.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.provider.model.Provider;
import cat.itacademy.s04.t02.n02.provider.repository.ProviderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for fruit business logic.
 * Handles all fruit-related operations including creation, retrieval, update, and deletion.
 */
@Service
public class FruitService {

    private final FruitRepository fruitRepository;
    private final ProviderRepository providerRepository;

    public FruitService(FruitRepository fruitRepository, ProviderRepository providerRepository) {
        this.fruitRepository = fruitRepository;
        this.providerRepository = providerRepository;
    }

    /**
     * Creates a new fruit with the provided data.
     * 
     * @param fruitRequestDto The fruit data from the request
     * @return The created fruit as response DTO
     * @throws ResourceNotFoundException if the provider doesn't exist
     */
    @Transactional
    public FruitResponseDto addFruit(FruitRequestDto fruitRequestDto) {
        Provider provider = providerRepository.findById(fruitRequestDto.providerId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + fruitRequestDto.providerId()));
        
        Fruit fruit = new Fruit();
        fruit.setName(fruitRequestDto.name());
        fruit.setWeightInKilos(fruitRequestDto.weightInKilos());
        fruit.setProvider(provider);
        
        Fruit savedFruit = fruitRepository.save(fruit);
        return toResponseDto(savedFruit);
    }

    /**
     * Retrieves all fruits from the database.
     * 
     * @return List of all fruits as response DTOs
     */
    public List<FruitResponseDto> getAllFruits() {
        return fruitRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    /**
     * Retrieves fruits filtered by provider ID.
     * 
     * @param providerId The provider ID to filter by
     * @return List of fruits from the specified provider
     * @throws ProviderNotFoundException if the provider doesn't exist
     */
    @Transactional(readOnly = true)
    public List<FruitResponseDto> getFruitsByProviderId(Long providerId) {
        if (!providerRepository.existsById(providerId)) {
            throw new ProviderNotFoundException("Provider not found with id: " + providerId);
        }
        return fruitRepository.findByProviderId(providerId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public FruitResponseDto getFruitById(Long id) {
        return fruitRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found with id: " + id));
    }

    @Transactional
    public FruitResponseDto updateFruit(Long id, FruitRequestDto fruitRequestDto) {
        Fruit existingFruit = fruitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found with id: " + id));
        
        Provider provider = providerRepository.findById(fruitRequestDto.providerId())
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + fruitRequestDto.providerId()));
        
        existingFruit.setName(fruitRequestDto.name());
        existingFruit.setWeightInKilos(fruitRequestDto.weightInKilos());
        existingFruit.setProvider(provider);
        
        Fruit updatedFruit = fruitRepository.save(existingFruit);
        return toResponseDto(updatedFruit);
    }

    /**
     * Deletes a fruit by its ID.
     * 
     * @param id The fruit ID to delete
     * @throws ResourceNotFoundException if the fruit doesn't exist
     */
    @Transactional
    public void deleteFruit(Long id) {
        if (!fruitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fruit not found with id: " + id);
        }
        fruitRepository.deleteById(id);
    }

    @Transactional
    private FruitResponseDto toResponseDto(Fruit fruit) {
        String providerName = fruit.getProvider() != null ? fruit.getProvider().getName() : null;
        return new FruitResponseDto(
                fruit.getId(),
                fruit.getName(),
                fruit.getWeightInKilos(),
                providerName
        );
    }

}