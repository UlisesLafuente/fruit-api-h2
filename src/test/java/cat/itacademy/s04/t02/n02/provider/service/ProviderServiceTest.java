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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private ProviderService providerService;

    private Provider provider;
    private ProviderRequestDto providerRequestDto;

    @BeforeEach
    void setUp() {
        provider = new Provider();
        provider.setId(1L);
        provider.setName("Provider1");
        provider.setCountry("Spain");

        providerRequestDto = new ProviderRequestDto("Provider1", "Spain");
    }

    @Test
    void addProvider_shouldSaveAndReturnDto() {
        when(providerRepository.existsByName("Provider1")).thenReturn(false);
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        ProviderResponseDto result = providerService.addProvider(providerRequestDto);

        assertNotNull(result);
        assertEquals("Provider1", result.name());
        assertEquals("Spain", result.country());
        verify(providerRepository, times(1)).save(any(Provider.class));
    }

    @Test
    void addProvider_whenNameExists_shouldThrowException() {
        when(providerRepository.existsByName("Provider1")).thenReturn(true);

        assertThrows(ProviderAlreadyExistsException.class, 
            () -> providerService.addProvider(providerRequestDto));
        
        verify(providerRepository, never()).save(any());
    }

    @Test
    void getAllProviders_shouldReturnListOfDtos() {
        List<Provider> providers = Arrays.asList(provider);
        when(providerRepository.findAll()).thenReturn(providers);

        List<ProviderResponseDto> result = providerService.getAllProviders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Provider1", result.get(0).name());
    }

    @Test
    void getProviderById_whenFound_shouldReturnDto() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));

        ProviderResponseDto result = providerService.getProviderById(1L);

        assertNotNull(result);
        assertEquals("Provider1", result.name());
    }

    @Test
    void getProviderById_whenNotFound_shouldThrowException() {
        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, 
            () -> providerService.getProviderById(99L));
    }

    @Test
    void updateProvider_shouldUpdateAndReturnDto() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        ProviderResponseDto result = providerService.updateProvider(1L, providerRequestDto);

        assertNotNull(result);
        verify(providerRepository, times(1)).save(any(Provider.class));
    }

    @Test
    void updateProvider_whenNotFound_shouldThrowException() {
        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, 
            () -> providerService.updateProvider(99L, providerRequestDto));
    }

    @Test
    void updateProvider_withDuplicateName_shouldThrowException() {
        Provider otherProvider = new Provider();
        otherProvider.setId(2L);
        otherProvider.setName("Provider2");
        otherProvider.setCountry("France");

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.existsByName("Provider1")).thenReturn(true);
        when(providerRepository.findByName("Provider1")).thenReturn(otherProvider);

        assertThrows(ProviderAlreadyExistsException.class, 
            () -> providerService.updateProvider(1L, providerRequestDto));
    }

    @Test
    void deleteProvider_whenNoFruits_shouldDelete() {
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.findByProviderId(1L)).thenReturn(List.of());
        doNothing().when(providerRepository).deleteById(1L);

        providerService.deleteProvider(1L);

        verify(providerRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProvider_whenHasFruits_shouldThrowException() {
        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.findByProviderId(1L)).thenReturn(Arrays.asList(fruit));

        assertThrows(ProviderHasFruitsException.class, 
            () -> providerService.deleteProvider(1L));
        
        verify(providerRepository, never()).deleteById(any());
    }

    @Test
    void deleteProvider_whenNotFound_shouldThrowException() {
        when(providerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProviderNotFoundException.class, 
            () -> providerService.deleteProvider(99L));
    }

    @Test
    void getFruitsByProviderId_whenProviderExists_shouldReturnFruits() {
        Fruit fruit = new Fruit();
        fruit.setId(1L);
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruit.setProvider(provider);

        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.findByProviderId(1L)).thenReturn(Arrays.asList(fruit));

        List<FruitResponseDto> result = providerService.getFruitsByProviderId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).name());
    }

    @Test
    void getFruitsByProviderId_whenProviderNotFound_shouldThrowException() {
        when(providerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProviderNotFoundException.class, 
            () -> providerService.getFruitsByProviderId(99L));
    }
}