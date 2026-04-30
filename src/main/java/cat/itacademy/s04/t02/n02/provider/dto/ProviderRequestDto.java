package cat.itacademy.s04.t02.n02.provider.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for provider creation and update requests.
 * Contains the data required to create or update a provider entity.
 * 
 * @param name    The name of the provider (required, must be unique)
 * @param country The country where the provider is located (required)
 */
public record ProviderRequestDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Country is required")
        String country
) {}