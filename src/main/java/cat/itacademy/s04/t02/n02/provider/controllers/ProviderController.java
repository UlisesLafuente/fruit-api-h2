package cat.itacademy.s04.t02.n02.provider.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n02.provider.dto.ProviderRequestDto;
import cat.itacademy.s04.t02.n02.provider.dto.ProviderResponseDto;
import cat.itacademy.s04.t02.n02.provider.service.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for provider CRUD operations.
 * Provides endpoints for creating, reading, updating, and deleting providers.
 */
@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService){
        this.providerService=providerService;
    }

    /**
     * Creates a new provider.
     * 
     * @param providerRequestDto The provider data from the request body
     * @return The created provider with generated ID
     */
    @PostMapping
    public ResponseEntity<ProviderResponseDto> addProvider(@Valid @RequestBody ProviderRequestDto providerRequestDto){
        ProviderResponseDto created = providerService.addProvider(providerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves all providers.
     * 
     * @return List of all providers
     */
    @GetMapping
    public ResponseEntity<List<ProviderResponseDto>> getAllProviders(){
        List<ProviderResponseDto> providers = providerService.getAllProviders();
        return ResponseEntity.ok(providers);
    }

    /**
     * Retrieves a provider by its ID.
     * 
     * @param id The provider ID
     * @return The provider data
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponseDto> getProviderById(@PathVariable Long id) {
        ProviderResponseDto provider = providerService.getProviderById(id);
        return ResponseEntity.ok(provider);
    }

    /**
     * Retrieves fruits for a specific provider.
     * 
     * @param id The provider ID
     * @return List of fruits from the provider
     */
    @GetMapping("/{id}/fruits")
    public ResponseEntity<List<FruitResponseDto>> getFruitsByProviderId(@PathVariable Long id) {
        List<FruitResponseDto> fruits = providerService.getFruitsByProviderId(id);
        return ResponseEntity.ok(fruits);
    }

    /**
     * Updates an existing provider.
     * 
     * @param id                   The provider ID to update
     * @param providerRequestDto The updated provider data
     * @return The updated provider
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponseDto> updateProvider(@PathVariable Long id,
                                                @Valid @RequestBody ProviderRequestDto providerRequestDto) {
        ProviderResponseDto updated = providerService.updateProvider(id, providerRequestDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a provider by its ID.
     * 
     * @param id The provider ID to delete
     * @return No content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }

}