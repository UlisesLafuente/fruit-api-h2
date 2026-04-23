package cat.itacademy.s04.t02.n01.fruit_api_h2.dto;

import cat.itacademy.s04.t02.n01.fruit_api_h2.model.Fruit;
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

    public static FruitDto fromEntity(Fruit fruit) {
        return new FruitDto(
                fruit.getId(),
                fruit.getName(),
                fruit.getWeightInKilos()
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
}