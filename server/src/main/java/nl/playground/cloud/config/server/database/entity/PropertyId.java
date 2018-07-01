package nl.playground.cloud.config.server.database.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PropertyId implements Serializable {
    private static final long serialVersionUID = 5141852711862641784L;
    private String application;
    private String profile;
    private String label;
    private String key;

    public PropertyId() {
    }

    public PropertyId(String application, String profile, String label, String key) {
        this.application = application;
        this.profile = profile;
        this.label = label;
        this.key = key;
    }

    public PropertyId(String application, String profile, String key) {
        this.application = application;
        this.profile = profile;
        this.label = "master";
        this.key = key;
    }

    @Override
    public String toString() {
        return "PropertyId{" +
                "application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", label='" + label + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    // getters and setters
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
