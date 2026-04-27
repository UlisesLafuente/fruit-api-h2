package cat.itacademy.s04.t02.n02.provider.controllers;

import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.provider.dto.ProviderDto;
import cat.itacademy.s04.t02.n02.provider.model.Provider;
import cat.itacademy.s04.t02.n02.provider.repository.ProviderRepository;
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
class ProviderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private FruitRepository fruitRepository;

    @BeforeEach
    void setUp() {
        fruitRepository.deleteAll();
        providerRepository.deleteAll();
    }

    @Test
    void addProvider_shouldReturn201() throws Exception {
        ProviderDto providerDto = new ProviderDto(null, "Provider1", "Spain");

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(providerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Provider1"))
                .andExpect(jsonPath("$.country").value("Spain"));
    }

    @Test
    void addProvider_withDuplicateName_shouldReturn409() throws Exception {
        ProviderDto providerDto = new ProviderDto(null, "Provider1", "Spain");
        providerRepository.save(providerDto.toEntity());

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(providerDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void addProvider_withInvalidData_shouldReturn400() throws Exception {
        ProviderDto providerDto = new ProviderDto(null, "", "");

        mockMvc.perform(post("/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(providerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getAllProviders_shouldReturn200() throws Exception {
        Provider provider = new Provider();
        provider.setName("Provider1");
        provider.setCountry("Spain");
        providerRepository.save(provider);

        mockMvc.perform(get("/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Provider1"));
    }

    @Test
    void getProviderById_whenExists_shouldReturn200() throws Exception {
        Provider provider = new Provider();
        provider.setName("Provider1");
        provider.setCountry("Spain");
        Provider saved = providerRepository.save(provider);

        mockMvc.perform(get("/providers/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Provider1"));
    }

    @Test
    void getProviderById_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(get("/providers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateProvider_whenExists_shouldReturn200() throws Exception {
        Provider provider = new Provider();
        provider.setName("Provider1");
        provider.setCountry("Spain");
        Provider saved = providerRepository.save(provider);

        ProviderDto updatedDto = new ProviderDto(saved.getId(), "Provider2", "France");

        mockMvc.perform(put("/providers/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Provider2"))
                .andExpect(jsonPath("$.country").value("France"));
    }

    @Test
    void updateProvider_whenNotExists_shouldReturn404() throws Exception {
        ProviderDto updatedDto = new ProviderDto(999L, "Provider2", "France");

        mockMvc.perform(put("/providers/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteProvider_whenExists_shouldReturn204() throws Exception {
        Provider provider = new Provider();
        provider.setName("Provider1");
        provider.setCountry("Spain");
        Provider saved = providerRepository.save(provider);

        mockMvc.perform(delete("/providers/" + saved.getId()))
                .andExpect(status().isNoContent());

        assertFalse(providerRepository.existsById(saved.getId()));
    }

    @Test
    void deleteProvider_whenNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/providers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteProvider_whenHasFruits_shouldReturn400() throws Exception {
        Provider provider = new Provider();
        provider.setName("Provider1");
        provider.setCountry("Spain");
        Provider saved = providerRepository.save(provider);

        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruit.setProvider(saved);
        fruitRepository.save(fruit);

        mockMvc.perform(delete("/providers/" + saved.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getFruitsByProviderId_shouldReturnFruits() throws Exception {
        Provider provider = new Provider();
        provider.setName("Provider1");
        provider.setCountry("Spain");
        Provider saved = providerRepository.save(provider);

        Fruit fruit = new Fruit();
        fruit.setName("Apple");
        fruit.setWeightInKilos(2);
        fruit.setProvider(saved);
        fruitRepository.save(fruit);

        mockMvc.perform(get("/providers/" + saved.getId() + "/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }

    @Test
    void getFruitsByProviderId_whenProviderNotExists_shouldReturn404() throws Exception {
        mockMvc.perform(get("/providers/999/fruits"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}