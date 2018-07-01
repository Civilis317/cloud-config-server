package nl.playground.cloud.config.server.database.repository;

import nl.playground.cloud.config.server.database.entity.PropertyEntity;
import nl.playground.cloud.config.server.database.entity.PropertyId;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<PropertyEntity, PropertyId> {
}
