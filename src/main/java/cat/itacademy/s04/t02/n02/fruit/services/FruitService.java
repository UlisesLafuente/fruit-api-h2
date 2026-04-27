package cat.itacademy.s04.t02.n02.fruit.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitDto;
import cat.itacademy.s04.t02.n02.fruit.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.provider.model.Provider;
import cat.itacademy.s04.t02.n02.provider.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitService {

    private final FruitRepository fruitRepository;
    private final ProviderRepository providerRepository;

    public FruitService(FruitRepository fruitRepository, ProviderRepository providerRepository) {
        this.fruitRepository = fruitRepository;
        this.providerRepository = providerRepository;
    }

    public FruitDto addFruit(FruitDto fruitDto) {
        Provider provider = null;
        if (fruitDto.getProviderId() != null) {
            provider = providerRepository.findById(fruitDto.getProviderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + fruitDto.getProviderId()));
        }
        Fruit fruit = fruitDto.toEntityWithProvider(provider);
        Fruit savedFruit = fruitRepository.save(fruit);
        return FruitDto.fromEntity(savedFruit);
    }

    public List<FruitDto> getAllFruits() {
        return fruitRepository.findAll().stream()
                .map(FruitDto::fromEntity)
                .toList();
    }

    public List<FruitDto> getFruitsByProviderId(Long providerId) {
        return fruitRepository.findByProviderId(providerId).stream()
                .map(FruitDto::fromEntity)
                .toList();
    }

    public FruitDto getFruitById(Long id) {
        return fruitRepository.findById(id)
                .map(FruitDto::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found with id: " + id));
    }

    public FruitDto updateFruit(Long id, FruitDto fruitDto) {
        Fruit existingFruit = fruitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fruit not found with id: " + id));
        
        Provider provider = null;
        if (fruitDto.getProviderId() != null) {
            provider = providerRepository.findById(fruitDto.getProviderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + fruitDto.getProviderId()));
        }
        
        existingFruit.setName(fruitDto.getName());
        existingFruit.setWeightInKilos(fruitDto.getWeightInKilos());
        existingFruit.setProvider(provider);
        
        Fruit updatedFruit = fruitRepository.save(existingFruit);
        return FruitDto.fromEntity(updatedFruit);
    }

    public void deleteFruit(Long id) {
        if (!fruitRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fruit not found with id: " + id);
        }
        fruitRepository.deleteById(id);
    }

}