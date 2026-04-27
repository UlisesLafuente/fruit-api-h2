package cat.itacademy.s04.t02.n02.provider.repository;

import cat.itacademy.s04.t02.n02.provider.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    boolean existsByName(String name);
}
