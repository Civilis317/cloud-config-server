package nl.playground.cloud.config.server.rest;

import java.util.ArrayList;
import java.util.List;

public class PropertyResponse {
    private List<Property> propertyList = new ArrayList<>(128);

    // getters and setters
    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }
}
