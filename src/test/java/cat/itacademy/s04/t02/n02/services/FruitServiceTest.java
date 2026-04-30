package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.dto.FruitResponseDto;
import cat.itacademy.s04.t02.n02.common.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.fruit.services.FruitService;
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
class FruitServiceTest {

    @Mock
    private FruitRepository fruitRepository;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private FruitService fruitService;

    private Fruit fruit;
    private FruitRequestDto fruitRequestDto;
    private Provider provider;

    @BeforeEach
    void setUp() {
        provider = new Provider();
        provider.setId(1L);
        provider.setName("Provider1");
        provider.setCountry("Spain");

        fruit = new Fruit();
        fruit.setId(1L);
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruit.setProvider(provider);

        fruitRequestDto = new FruitRequestDto("Apple", 2, 1L);
    }

    @Test
    void addFruit_shouldSaveAndReturnDto() {
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(fruit);

        FruitResponseDto result = fruitService.addFruit(fruitRequestDto);

        assertNotNull(result);
        assertEquals("Apple", result.name());
        assertEquals(2, result.weightInKilos());
        assertEquals("Provider1", result.providerName());
        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    void addFruit_withInvalidProvider_shouldThrowException() {
        FruitRequestDto fruitRequestNoProvider = new FruitRequestDto("Apple", 2, 99L);
        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> fruitService.addFruit(fruitRequestNoProvider));
    }

    @Test
    void getAllFruits_shouldReturnListOfDtos() {
        List<Fruit> fruits = Arrays.asList(fruit);
        when(fruitRepository.findAll()).thenReturn(fruits);

        List<FruitResponseDto> result = fruitService.getAllFruits();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).name());
        verify(fruitRepository, times(1)).findAll();
    }

    @Test
    void getFruitsByProviderId_shouldReturnFilteredList() {
        List<Fruit> fruits = Arrays.asList(fruit);
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.findByProviderId(1L)).thenReturn(fruits);

        List<FruitResponseDto> result = fruitService.getFruitsByProviderId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).name());
    }

    @Test
    void getFruitsByProviderId_whenProviderNotFound_shouldThrowException() {
        when(providerRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProviderNotFoundException.class, 
            () -> fruitService.getFruitsByProviderId(99L));
    }

    @Test
    void getFruitById_whenFound_shouldReturnDto() {
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        FruitResponseDto result = fruitService.getFruitById(1L);

        assertNotNull(result);
        assertEquals("Apple", result.name());
        assertEquals("Provider1", result.providerName());
        verify(fruitRepository, times(1)).findById(1L);
    }

    @Test
    void getFruitById_whenNotFound_shouldThrowException() {
        when(fruitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> fruitService.getFruitById(99L));
        
        verify(fruitRepository, times(1)).findById(99L);
    }

    @Test
    void updateFruit_shouldUpdateAndReturnDto() {
        FruitRequestDto updatedRequest = new FruitRequestDto("Banana", 3, 1L);
        Fruit updatedFruit = new Fruit();
        updatedFruit.setId(1L);
        updatedFruit.setName("Banana");
        updatedFruit.setWeightInKilos(3);
        updatedFruit.setProvider(provider);

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));
        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruit);

        FruitResponseDto result = fruitService.updateFruit(1L, updatedRequest);

        assertNotNull(result);
        assertEquals("Banana", result.name());
        assertEquals(3, result.weightInKilos());
        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    void updateFruit_whenNotFound_shouldThrowException() {
        FruitRequestDto updatedRequest = new FruitRequestDto("Banana", 3, 1L);

        when(fruitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> fruitService.updateFruit(99L, updatedRequest));
    }

    @Test
    void deleteFruit_shouldCallRepository() {
        when(fruitRepository.existsById(1L)).thenReturn(true);
        doNothing().when(fruitRepository).deleteById(1L);

        fruitService.deleteFruit(1L);

        verify(fruitRepository, times(1)).existsById(1L);
        verify(fruitRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteFruit_whenNotFound_shouldThrowException() {
        when(fruitRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, 
            () -> fruitService.deleteFruit(99L));
        
        verify(fruitRepository, times(1)).existsById(99L);
        verify(fruitRepository, never()).deleteById(any());
    }
}