package cat.itacademy.s04.t02.n02.fruit.dto;

import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.provider.model.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FruitDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private int weightInKilos;

    @NotNull(message = "Provider is required")
    private Long providerId;

    public static FruitDto fromEntity(Fruit fruit) {
        Long providerId = null;
        if (fruit.getProvider() != null) {
            providerId = fruit.getProvider().getId();
        }
        return new FruitDto(
                fruit.getId(),
                fruit.getName(),
                fruit.getWeightInKilos(),
                providerId
        );
    }

    public Fruit toEntity() {
        Fruit fruit = new Fruit();
        fruit.setName(this.name);
        fruit.setWeightInKilos(this.weightInKilos);
        if (this.id != null) {
            fruit.setId(this.id);
        }
        return fruit;
    }

    public Fruit toEntityWithProvider(Provider provider) {
        Fruit fruit = this.toEntity();
        fruit.setProvider(provider);
        return fruit;
    }
}