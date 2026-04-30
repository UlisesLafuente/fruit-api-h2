package cat.itacademy.s04.t02.n02.fruit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Data Transfer Object for fruit creation and update requests.
 * Contains the data required to create or update a fruit entity.
 * 
 * @param name        The name of the fruit (required)
 * @param weightInKilos The weight of the fruit in kilograms (required, must be positive)
 * @param providerId  The ID of the provider supplying the fruit (required)
 */
public record FruitRequestDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Weight is required")
        @Positive(message = "Weight must be positive")
        Integer weightInKilos,

        @NotNull(message = "Provider is required")
        Long providerId
) {}