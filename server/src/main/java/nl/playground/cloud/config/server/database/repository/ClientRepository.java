package nl.playground.cloud.config.server.database.repository;

import nl.playground.cloud.config.server.database.entity.ClientEntity;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {
}
