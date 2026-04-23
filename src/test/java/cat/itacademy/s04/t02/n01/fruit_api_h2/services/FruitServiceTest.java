package cat.itacademy.s04.t02.n01.fruit_api_h2.services;

import cat.itacademy.s04.t02.n01.fruit_api_h2.dto.FruitDto;
import cat.itacademy.s04.t02.n01.fruit_api_h2.exception.ResourceNotFoundException;
import cat.itacademy.s04.t02.n01.fruit_api_h2.model.Fruit;
import cat.itacademy.s04.t02.n01.fruit_api_h2.repository.FruitRepository;
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

    @InjectMocks
    private FruitService fruitService;

    private Fruit fruit;
    private FruitDto fruitDto;

    @BeforeEach
    void setUp() {
        fruit = new Fruit();
        fruit.setId(1L);
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);

        fruitDto = new FruitDto(1L, "Apple", 2);
    }

    @Test
    void addFruit_shouldSaveAndReturnDto() {
        when(fruitRepository.save(any(Fruit.class))).thenReturn(fruit);

        FruitDto result = fruitService.addFruit(fruitDto);

        assertNotNull(result);
        assertEquals("Apple", result.getName());
        assertEquals(2, result.getWeightInKilos());
        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    void getAllFruits_shouldReturnListOfDtos() {
        List<Fruit> fruits = Arrays.asList(fruit);
        when(fruitRepository.findAll()).thenReturn(fruits);

        List<FruitDto> result = fruitService.getAllFruits();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).getName());
        verify(fruitRepository, times(1)).findAll();
    }

    @Test
    void getFruitById_whenFound_shouldReturnDto() {
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        FruitDto result = fruitService.getFruitById(1L);

        assertNotNull(result);
        assertEquals("Apple", result.getName());
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
        FruitDto updatedDto = new FruitDto(1L, "Banana", 3);
        Fruit updatedFruit = new Fruit();
        updatedFruit.setId(1L);
        updatedFruit.setName("Banana");
        updatedFruit.setWeightInKilos(3);

        when(fruitRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updatedFruit);

        FruitDto result = fruitService.updateFruit(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Banana", result.getName());
        assertEquals(3, result.getWeightInKilos());
        verify(fruitRepository, times(1)).existsById(1L);
        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    void updateFruit_whenNotFound_shouldThrowException() {
        FruitDto updatedDto = new FruitDto(99L, "Banana", 3);

        when(fruitRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, 
            () -> fruitService.updateFruit(99L, updatedDto));
        
        verify(fruitRepository, times(1)).existsById(99L);
        verify(fruitRepository, never()).save(any(Fruit.class));
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