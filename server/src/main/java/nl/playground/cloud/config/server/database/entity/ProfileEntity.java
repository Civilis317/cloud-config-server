package nl.playground.cloud.config.server.database.entity;

import javax.persistence.*;

@Entity
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @SequenceGenerator(name = "id_seq", sequenceName = "pfe_seq", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "clt_id")
    private ClientEntity clientEntity;

    @Override
    public String toString() {
        return "ProfileEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ClientEntity getClientEntity() {
        return clientEntity;
    }

    public void setClientEntity(ClientEntity clientEntity) {
        this.clientEntity = clientEntity;
    }
}
