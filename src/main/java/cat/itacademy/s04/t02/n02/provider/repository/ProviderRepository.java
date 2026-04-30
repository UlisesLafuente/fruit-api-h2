package cat.itacademy.s04.t02.n02.provider.repository;

import cat.itacademy.s04.t02.n02.provider.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Provider entity operations.
 * Provides database access methods for provider data.
 */
@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    /**
     * Checks if a provider with the given name exists.
     * 
     * @param name The provider name
     * @return true if a provider with the name exists
     */
    boolean existsByName(String name);

    /**
     * Finds a provider by its name.
     * 
     * @param name The provider name
     * @return The provider if found, null otherwise
     */
    Provider findByName(String name);
}
