package nl.playground.cloud.config.server.database.conversion;

import nl.playground.cloud.config.server.database.entity.PropertyEntity;
import nl.playground.cloud.config.server.database.entity.PropertyId;
import nl.playground.cloud.config.server.rest.model.Property;
import nl.playground.cloud.config.server.rest.http.PropertyRequest;

import java.util.ArrayList;
import java.util.List;

public class ConfigConverter {
    public static Property convertEntityToProperty(PropertyEntity entity) {
        Property property = new Property();
        property.setApplication(entity.getId().getApplication());
        property.setProfile(entity.getId().getProfile());
        property.setLabel(entity.getId().getLabel());
        property.setKey(entity.getId().getKey());
        property.setValue(entity.getValue());
        return property;
    }

    public static PropertyEntity convertPropertyToEntity(Property property) {
        PropertyEntity entity = new PropertyEntity();
        PropertyId pk = new PropertyId();
        pk.setApplication(property.getApplication());
        pk.setProfile(property.getProfile());
        pk.setLabel(property.getLabel());
        pk.setKey(property.getKey());
        entity.setId(pk);
        entity.setValue(property.getValue());
        return entity;
    }

    public static List<Property> convertPropertyRequestToProperties(PropertyRequest request) {
        List<Property> resultList = new ArrayList<>(128);
        request.getKeyValueList().stream().forEach(keyValuePair -> {
            Property property = new Property();
            property.setApplication(request.getApplication());
            property.setProfile(request.getProfile());
            property.setLabel(request.getLabel());
            property.setKey(keyValuePair.getKey());
            property.setValue(keyValuePair.getValue());
            resultList.add(property);
        });
        return resultList;
    }
}
