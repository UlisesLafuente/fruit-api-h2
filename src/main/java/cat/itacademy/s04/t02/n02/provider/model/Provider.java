package cat.itacademy.s04.t02.n02.provider.model;

import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String country;

    @OneToMany(mappedBy = "provider")
    private List<Fruit> fruits;
}
