package cat.itacademy.s04.t02.n02.provider.service;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitDto;
import cat.itacademy.s04.t02.n02.fruit.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.provider.dto.ProviderDto;
import cat.itacademy.s04.t02.n02.provider.model.Provider;
import cat.itacademy.s04.t02.n02.provider.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository){
        this.providerRepository=providerRepository;
    }

    public ProviderDto addProvider(ProviderDto providerDto){
        Provider provider= providerDto.toEntity();
        Provider savedProvider = providerRepository.save(provider);
        return ProviderDto.fromEntity(savedProvider);
    }

    public List<ProviderDto> getAllProviders(){
        return providerRepository.findAll().stream()
                .map(ProviderDto::fromEntity)
                .toList();
    }

    public ProviderDto getProviderByID(Long id){
        return providerRepository.findById(id)
                .map(ProviderDto::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + id));
    }

    public ProviderDto updateProvider(Long id, ProviderDto providerDto) {
        if (!providerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fruit not found with id: " + id);
        }
        providerDto.setId(id);
        Provider provider = providerDto.toEntity();
        Provider updatedProvider = providerRepository.save(provider);
        return ProviderDto.fromEntity(updatedProvider);
    }
    public void deleteProvider(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fruit not found with id: " + id);
        }
        providerRepository.deleteById(id);
    }
}


