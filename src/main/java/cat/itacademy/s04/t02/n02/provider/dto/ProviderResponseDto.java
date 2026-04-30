package cat.itacademy.s04.t02.n02.provider.dto;

/**
 * Data Transfer Object for provider API responses.
 * Contains the provider data returned to clients after read operations.
 * 
 * @param id      The unique identifier of the provider
 * @param name   The name of the provider
 * @param country The country where the provider is located
 */
public record ProviderResponseDto(
        Long id,
        String name,
        String country
) {}