package cat.itacademy.s04.t02.n02.fruit.model;

import cat.itacademy.s04.t02.n02.provider.model.Provider;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Fruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int weightInKilos;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;
}