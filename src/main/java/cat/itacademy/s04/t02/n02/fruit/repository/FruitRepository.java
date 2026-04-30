package cat.itacademy.s04.t02.n02.fruit.repository;

import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Fruit entity operations.
 * Provides database access methods for fruit data.
 */
@Repository
public interface FruitRepository extends JpaRepository<Fruit, Long> {
    /**
     * Finds all fruits associated with a provider.
     * 
     * @param providerId The provider ID
     * @return List of fruits from the provider
     */
    List<Fruit> findByProviderId(Long providerId);
}