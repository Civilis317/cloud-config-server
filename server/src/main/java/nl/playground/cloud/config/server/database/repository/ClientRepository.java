package nl.playground.cloud.config.server.database.repository;

import nl.playground.cloud.config.server.database.entity.ClientEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {

    public Optional<ClientEntity> findByName(String name);

}
