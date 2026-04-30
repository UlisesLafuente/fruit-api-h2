package cat.itacademy.s04.t02.n02.provider.service;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.provider.dto.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.provider.dto.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.provider.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.t02.n02.provider.exception.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.provider.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.provider.model.Provider;
import cat.itacademy.s04.t02.n02.provider.repository.ProviderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for provider business logic.
 * Handles all provider-related operations including creation, retrieval, update, deletion, and fruit management.
 */
@Service
public class ProviderService {
    private final ProviderRepository providerRepository;
    private final FruitRepository fruitRepository;

    public ProviderService(ProviderRepository providerRepository, FruitRepository fruitRepository){
        this.providerRepository=providerRepository;
        this.fruitRepository=fruitRepository;
    }

    /**
     * Creates a new provider with the provided data.
     * 
     * @param providerRequestDto The provider data from the request
     * @return The created provider as response DTO
     * @throws ProviderAlreadyExistsException if a provider with the same name exists
     */
    @Transactional
    public ProviderResponseDto addProvider(ProviderRequestDto providerRequestDto){
        if (providerRepository.existsByName(providerRequestDto.name())) {
            throw new ProviderAlreadyExistsException("Provider with name '" + providerRequestDto.name() + "' already exists");
        }
        Provider provider = new Provider();
        provider.setName(providerRequestDto.name());
        provider.setCountry(providerRequestDto.country());
        
        Provider savedProvider = providerRepository.save(provider);
        return toResponseDto(savedProvider);
    }

    /**
     * Retrieves all providers from the database.
     * 
     * @return List of all providers as response DTOs
     */
    public List<ProviderResponseDto> getAllProviders(){
        return providerRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    /**
     * Retrieves a provider by its ID.
     * 
     * @param id The provider ID
     * @return The provider as response DTO
     * @throws ProviderNotFoundException if the provider doesn't exist
     */
    @Transactional(readOnly = true)
    public ProviderResponseDto getProviderById(Long id){
        return providerRepository.findById(id)
                .map(this::toResponseDto)
                .orElseThrow(() -> new ProviderNotFoundException("Provider not found with id: " + id));
    }

    /**
     * Updates an existing provider.
     * 
     * @param id                   The provider ID to update
     * @param providerRequestDto The updated provider data
     * @return The updated provider as response DTO
     * @throws ProviderNotFoundException if the provider doesn't exist
     * @throws ProviderAlreadyExistsException if the new name is taken by another provider
     */
    @Transactional
    public ProviderResponseDto updateProvider(Long id, ProviderRequestDto providerRequestDto) {
        Provider existingProvider = providerRepository.findById(id)
                .orElseThrow(() -> new ProviderNotFoundException("Provider not found with id: " + id));
        
        if (providerRepository.existsByName(providerRequestDto.name())) {
            Provider providerWithName = providerRepository.findByName(providerRequestDto.name());
            if (providerWithName != null && !providerWithName.getId().equals(id)) {
                throw new ProviderAlreadyExistsException("Provider with name '" + providerRequestDto.name() + "' already exists");
            }
        }
        
        existingProvider.setName(providerRequestDto.name());
        existingProvider.setCountry(providerRequestDto.country());
        
        Provider updatedProvider = providerRepository.save(existingProvider);
        return toResponseDto(updatedProvider);
    }

    /**
     * Deletes a provider by its ID.
     * Only providers without associated fruits can be deleted.
     * 
     * @param id The provider ID to delete
     * @throws ProviderNotFoundException if the provider doesn't exist
     * @throws ProviderHasFruitsException if the provider has associated fruits
     */
    @Transactional
    public void deleteProvider(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ProviderNotFoundException("Provider not found with id: " + id);
        }
        List<Fruit> fruits = fruitRepository.findByProviderId(id);
        if (!fruits.isEmpty()) {
            throw new ProviderHasFruitsException("Cannot delete provider with id " + id + " - it has " + fruits.size() + " associated fruits");
        }
        providerRepository.deleteById(id);
    }

    /**
     * Retrieves fruits associated with a provider.
     * 
     * @param providerId The provider ID
     * @return List of fruits as response DTOs
     * @throws ProviderNotFoundException if the provider doesn't exist
     */
    public List<FruitResponseDto> getFruitsByProviderId(Long providerId) {
        if (!providerRepository.existsById(providerId)) {
            throw new ProviderNotFoundException("Provider not found with id: " + providerId);
        }
        return fruitRepository.findByProviderId(providerId).stream()
                .map(this::toFruitResponseDto)
                .toList();
    }

    private ProviderResponseDto toResponseDto(Provider provider) {
        return new ProviderResponseDto(
                provider.getId(),
                provider.getName(),
                provider.getCountry()
        );
    }

    private FruitResponseDto toFruitResponseDto(Fruit fruit) {
        String providerName = fruit.getProvider() != null ? fruit.getProvider().getName() : null;
        return new FruitResponseDto(
                fruit.getId(),
                fruit.getName(),
                fruit.getWeightInKilos(),
                providerName
        );
    }
}