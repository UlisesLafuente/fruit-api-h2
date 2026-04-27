package cat.itacademy.s04.t02.n02.provider.service;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitDto;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.provider.dto.ProviderDto;
import cat.itacademy.s04.t02.n02.provider.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.t02.n02.provider.exception.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.provider.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.provider.model.Provider;
import cat.itacademy.s04.t02.n02.provider.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;
    private final FruitRepository fruitRepository;

    public ProviderService(ProviderRepository providerRepository, FruitRepository fruitRepository){
        this.providerRepository=providerRepository;
        this.fruitRepository=fruitRepository;
    }

    public ProviderDto addProvider(ProviderDto providerDto){
        if (providerRepository.existsByName(providerDto.getName())) {
            throw new ProviderAlreadyExistsException("Provider with name '" + providerDto.getName() + "' already exists");
        }
        Provider provider = providerDto.toEntity();
        Provider savedProvider = providerRepository.save(provider);
        return ProviderDto.fromEntity(savedProvider);
    }

    public List<ProviderDto> getAllProviders(){
        return providerRepository.findAll().stream()
                .map(ProviderDto::fromEntity)
                .toList();
    }

    public ProviderDto getProviderById(Long id){
        return providerRepository.findById(id)
                .map(ProviderDto::fromEntity)
                .orElseThrow(() -> new ProviderNotFoundException("Provider not found with id: " + id));
    }

    public ProviderDto updateProvider(Long id, ProviderDto providerDto) {
        if (!providerRepository.existsById(id)) {
            throw new ProviderNotFoundException("Provider not found with id: " + id);
        }
        providerDto.setId(id);
        Provider provider = providerDto.toEntity();
        Provider updatedProvider = providerRepository.save(provider);
        return ProviderDto.fromEntity(updatedProvider);
    }

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

    public List<FruitDto> getFruitsByProviderId(Long providerId) {
        if (!providerRepository.existsById(providerId)) {
            throw new ProviderNotFoundException("Provider not found with id: " + providerId);
        }
        return fruitRepository.findByProviderId(providerId).stream()
                .map(FruitDto::fromEntity)
                .toList();
    }
}


