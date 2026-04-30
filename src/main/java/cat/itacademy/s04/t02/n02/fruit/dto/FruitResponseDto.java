package cat.itacademy.s04.t02.n02.fruit.dto;

/**
 * Data Transfer Object for fruit API responses.
 * Contains the fruit data returned to clients after read operations.
 * 
 * @param id           The unique identifier of the fruit
 * @param name        The name of the fruit
 * @param weightInKilos The weight of the fruit in kilograms
 * @param providerName The name of the provider supplying the fruit
 */
public record FruitResponseDto(
        Long id,
        String name,
        Integer weightInKilos,
        String providerName
) {}