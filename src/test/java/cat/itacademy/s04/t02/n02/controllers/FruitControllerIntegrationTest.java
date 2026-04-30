package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitRequestDto;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.provider.model.Provider;
import cat.itacademy.s04.t02.n02.provider.repository.ProviderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FruitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FruitRepository fruitRepository;

    @Autowired
    private ProviderRepository providerRepository;

    private Provider provider;

    @BeforeEach
    void setUp() {
        fruitRepository.deleteAll();
        providerRepository.deleteAll();
        
        provider = new Provider();
        provider.setName("Provider1");
        provider.setCountry("Spain");
        provider = providerRepository.save(provider);
    }

    @Test
    void addFruit_shouldReturn201() throws Exception {
        FruitRequestDto fruitRequestDto = new FruitRequestDto("Apple", 2, provider.getId());

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.weightInKilos").value(2))
                .andExpect(jsonPath("$.providerName").value("Provider1"));
    }

    @Test
    void addFruit_withoutProvider_shouldReturn400() throws Exception {
        FruitRequestDto fruitRequestDto = new FruitRequestDto("Apple", 2, null);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addFruit_withInvalidData_shouldReturn400() throws Exception {
        FruitRequestDto fruitRequestDto = new FruitRequestDto("", -1, provider.getId());

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void addFruit_withInvalidProvider_shouldReturn404() throws Exception {
        FruitRequestDto fruitRequestDto = new FruitRequestDto("Apple", 2, 999L);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getAllFruits_shouldReturn200() throws Exception {
        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruit.setProvider(provider);
        fruitRepository.save(fruit);

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }

    @Test
    void getFruitsByProviderId_shouldReturnFilteredFruits() throws Exception {
        Provider provider2 = new Provider();
        provider2.setName("Provider2");
        provider2.setCountry("France");
        provider2 = providerRepository.save(provider2);

        Fruit fruit1 = new Fruit();
        fruit1.setName("Apple");
        fruit1.setWeightInKilos(2);
        fruit1.setProvider(provider);
        fruitRepository.save(fruit1);

        Fruit fruit2 = new Fruit();
        fruit2.setName("Banana");
        fruit2.setWeightInKilos(3);
        fruit2.setProvider(provider2);
        fruitRepository.save(fruit2);

        mockMvc.perform(get("/fruits?providerId=" + provider.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }

    @Test
    void getFruitById_whenExists_shouldReturn200() throws Exception {
        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruit.setProvider(provider);
        Fruit saved = fruitRepository.save(fruit);

        mockMvc.perform(get("/fruits/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.providerName").value("Provider1"));
    }

    @Test
    void getFruitById_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(get("/fruits/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateFruit_whenExists_shouldReturn200() throws Exception {
        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruit.setProvider(provider);
        Fruit saved = fruitRepository.save(fruit);

        FruitRequestDto updatedRequest = new FruitRequestDto("Banana", 5, provider.getId());

        mockMvc.perform(put("/fruits/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Banana"))
                .andExpect(jsonPath("$.weightInKilos").value(5));
    }

    @Test
    void updateFruit_whenNotExists_shouldReturn404() throws Exception {
        FruitRequestDto updatedRequest = new FruitRequestDto("Banana", 5, provider.getId());

        mockMvc.perform(put("/fruits/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateFruit_withInvalidProvider_shouldReturn404() throws Exception {
        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruit.setProvider(provider);
        Fruit saved = fruitRepository.save(fruit);

        FruitRequestDto updatedRequest = new FruitRequestDto("Banana", 5, 999L);

        mockMvc.perform(put("/fruits/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteFruit_whenExists_shouldReturn204() throws Exception {
        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruit.setProvider(provider);
        Fruit saved = fruitRepository.save(fruit);

        mockMvc.perform(delete("/fruits/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertFalse(fruitRepository.existsById(saved.getId()));
    }

    @Test
    void deleteFruit_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/fruits/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}