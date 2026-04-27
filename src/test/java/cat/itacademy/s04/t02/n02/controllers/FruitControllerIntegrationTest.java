package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.fruit.dto.FruitDto;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FruitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FruitRepository fruitRepository;

    @BeforeEach
    void setUp() {
        fruitRepository.deleteAll();
    }

    @Test
    void addFruit_shouldReturn201() throws Exception {
        FruitDto fruitDto = new FruitDto(null, "Apple", 2);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Apple"))
                .andExpect(jsonPath("$.weightInKilos").value(2));
    }

    @Test
    void addFruit_withInvalidData_shouldReturn400() throws Exception {
        FruitDto fruitDto = new FruitDto(null, "", -1);

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void getAllFruits_shouldReturn200() throws Exception {
        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruitRepository.save(fruit);

        mockMvc.perform(get("/fruits"))
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
        Fruit saved = fruitRepository.save(fruit);

        mockMvc.perform(get("/fruits/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Apple"));
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
        Fruit saved = fruitRepository.save(fruit);

        FruitDto updatedDto = new FruitDto(saved.getId(), "Banana", 5);

        mockMvc.perform(put("/fruits/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Banana"))
                .andExpect(jsonPath("$.weightInKilos").value(5));
    }

    @Test
    void updateFruit_whenNotExists_shouldReturn404() throws Exception {
        FruitDto updatedDto = new FruitDto(999L, "Banana", 5);

        mockMvc.perform(put("/fruits/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteFruit_whenExists_shouldReturn204() throws Exception {
        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
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