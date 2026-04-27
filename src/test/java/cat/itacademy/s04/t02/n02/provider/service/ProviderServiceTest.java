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
    private ProviderDto providerDto;

    @BeforeEach
    void setUp() {
        provider = new Provider();
        provider.setId(1L);
        provider.setName("Provider1");
        provider.setCountry("Spain");

        providerDto = new ProviderDto(1L, "Provider1", "Spain");
    }

    @Test
    void addProvider_shouldSaveAndReturnDto() {
        when(providerRepository.existsByName("Provider1")).thenReturn(false);
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        ProviderDto result = providerService.addProvider(providerDto);

        assertNotNull(result);
        assertEquals("Provider1", result.getName());
        assertEquals("Spain", result.getCountry());
        verify(providerRepository, times(1)).save(any(Provider.class));
    }

    @Test
    void addProvider_whenNameExists_shouldThrowException() {
        when(providerRepository.existsByName("Provider1")).thenReturn(true);

        assertThrows(ProviderAlreadyExistsException.class, 
            () -> providerService.addProvider(providerDto));
        
        verify(providerRepository, never()).save(any());
    }

    @Test
    void getAllProviders_shouldReturnListOfDtos() {
        List<Provider> providers = Arrays.asList(provider);
        when(providerRepository.findAll()).thenReturn(providers);

        List<ProviderDto> result = providerService.getAllProviders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Provider1", result.get(0).getName());
    }

    @Test
    void getProviderById_whenFound_shouldReturnDto() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));

        ProviderDto result = providerService.getProviderById(1L);

        assertNotNull(result);
        assertEquals("Provider1", result.getName());
    }

    @Test
    void getProviderById_whenNotFound_shouldThrowException() {
        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, 
            () -> providerService.getProviderById(99L));
    }

    @Test
    void updateProvider_shouldUpdateAndReturnDto() {
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        ProviderDto result = providerService.updateProvider(1L, providerDto);

        assertNotNull(result);
        verify(providerRepository, times(1)).save(any(Provider.class));
    }

    @Test
    void updateProvider_whenNotFound_shouldThrowException() {
        when(providerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProviderNotFoundException.class, 
            () -> providerService.updateProvider(99L, providerDto));
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

        List<FruitDto> result = providerService.getFruitsByProviderId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).getName());
    }

    @Test
    void getFruitsByProviderId_whenProviderNotFound_shouldThrowException() {
        when(providerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProviderNotFoundException.class, 
            () -> providerService.getFruitsByProviderId(99L));
    }
}