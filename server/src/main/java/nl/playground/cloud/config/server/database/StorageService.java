package nl.playground.cloud.config.server.database;

import nl.playground.cloud.config.server.database.conversion.ClientConverter;
import nl.playground.cloud.config.server.database.conversion.ConfigConverter;
import nl.playground.cloud.config.server.database.entity.ClientEntity;
import nl.playground.cloud.config.server.database.entity.PropertyEntity;
import nl.playground.cloud.config.server.database.entity.PropertyId;
import nl.playground.cloud.config.server.database.manager.ClientManager;
import nl.playground.cloud.config.server.database.manager.PropertyManager;
import nl.playground.cloud.config.server.rest.model.Client;
import nl.playground.cloud.config.server.rest.model.Profile;
import nl.playground.cloud.config.server.rest.model.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService {
    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    private final ClientManager clientManager;
    private final PropertyManager propertyManager;

    public StorageService(ClientManager clientManager, PropertyManager propertyManager) {
        this.clientManager = clientManager;
        this.propertyManager = propertyManager;
    }

    public List<Property> getPropertyList() {
        List<Property> resultList = new ArrayList<>(128);
        propertyManager.getPropertyList().collect(Collectors.toList()).stream().forEach(pe -> {
            resultList.add(ConfigConverter.convertEntityToProperty(pe));
        });
        return resultList;
    }

    public Property getProperty(String application, String profile, String label, String key) {
        try {
            PropertyId id = new PropertyId(application, profile, label, key);
            PropertyEntity propertyEntity = propertyManager.findPropertyById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Record not found for id: " + id));
            return ConfigConverter.convertEntityToProperty(propertyEntity);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new StorageException(e.getMessage(), e);
        }
    }

    public Property save(Property property) {
        try {
            PropertyEntity entity = propertyManager.save(ConfigConverter.convertPropertyToEntity(property));
            return ConfigConverter.convertEntityToProperty(entity);
        } catch (EntityExistsException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new StorageException(e.getMessage(), e);
        }
    }

    public List<Property> saveProperties(List<Property> propertyList) {
        List<Property> returnList = new ArrayList<>(128);
        propertyList.stream().forEach(p -> {
            returnList.add(save(p));
        });
        return returnList;
    }

    public void deleteProperties(List<Property> propertyList) {
        propertyList.stream().forEach(p -> deleteProperty(p));
    }

    private void deleteProperty(Property property) {
        PropertyId id = new PropertyId(property.getApplication(), property.getProfile(), property.getLabel(), property.getKey());
        propertyManager.delete(id);
    }

    public List<Client> getClientList() {
        try {
            List<Client> resultList = new ArrayList<>(16);
            clientManager.getClientList().collect(Collectors.toList()).stream().forEach(e -> {
                resultList.add(ClientConverter.convertEntityToClient(e));
            });
            return resultList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new StorageException(e.getMessage(), e);
        }
    }

    public Client getClient(Long id) {
        try {
            ClientEntity clientEntity = clientManager.findClientById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Record not found for id: " + String.valueOf(id)));
            return ClientConverter.convertEntityToClient(clientEntity);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new StorageException(e.getMessage(), e);
        }
    }

    public Client getClient(String name) {
        try {
            ClientEntity clientEntity = clientManager.findClientByName(name)
                    .orElseThrow(() -> new EntityNotFoundException("Record not found for name: " + name));
            return ClientConverter.convertEntityToClient(clientEntity);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new StorageException(e.getMessage(), e);
        }
    }

    public Client getSpecificProfile(String clientName, String profileName) {
        try {
            Client client = getClient(clientName);
            Profile profile = new Profile();
            for (Profile p : client.getProfileList()) {
                if (profileName.equals(p.getName())) {
                    profile.setName(p.getName());
                    profile.setUrl(p.getUrl());
                }
            }
            client.getProfileList().clear();
            client.getProfileList().add(profile);
            return client;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new StorageException(e.getMessage(), e);
        }
    }

    public Client save(Client client) {
        try {
            ClientEntity entity = clientManager.save(ClientConverter.convertClientToEntity(client));
            return ClientConverter.convertEntityToClient(entity);
        } catch (EntityExistsException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new StorageException(e.getMessage(), e);
        }
    }
}
