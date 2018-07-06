package nl.playground.cloud.config.server.database.manager;

import nl.playground.cloud.config.server.database.entity.PropertyEntity;
import nl.playground.cloud.config.server.database.entity.PropertyId;
import nl.playground.cloud.config.server.database.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class PropertyManager {
    private final PropertyRepository repository;

    public PropertyManager(PropertyRepository repository) {
        this.repository = repository;
    }

    public Stream<PropertyEntity> getPropertyList() {
        return StreamSupport.stream(repository.findAll().spliterator(), true);
    }

    public Optional<PropertyEntity> findPropertyById(PropertyId id) {
        return repository.findById(id);
    }

    public PropertyEntity save(PropertyEntity propertyEntity) {
        return repository.save(propertyEntity);
    }

    public void delete(PropertyId id) {
        repository.findById(id).ifPresent(pe -> repository.delete(pe));
    }
}
