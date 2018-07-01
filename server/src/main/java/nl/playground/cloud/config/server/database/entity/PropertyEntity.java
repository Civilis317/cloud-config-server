package nl.playground.cloud.config.server.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "properties")
public class PropertyEntity {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "application", column = @Column(name = "application")),
            @AttributeOverride(name = "profile", column = @Column(name = "profile")),
            @AttributeOverride(name = "label", column = @Column(name = "label")),
            @AttributeOverride(name = "key", column = @Column(name = "key"))
    })
    PropertyId id;

    private String value;

    @Override
    public String toString() {
        return "PropertyEntity{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }

    //getters and setters
    public PropertyId getId() {
        return id;
    }

    public void setId(PropertyId id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
