package nl.playground.cloud.config.server.rest.http;

import nl.playground.cloud.config.server.rest.model.KeyValuePair;

import java.util.ArrayList;
import java.util.List;

public class PropertyRequest {
    private String application;
    private String profile;
    private String label;
    private List<KeyValuePair> keyValueList = new ArrayList<>(128);

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<KeyValuePair> getKeyValueList() {
        return keyValueList;
    }

    public void setKeyValueList(List<KeyValuePair> keyValueList) {
        this.keyValueList = keyValueList;
    }
}
