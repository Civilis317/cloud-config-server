package nl.playground.cloud.config.server.database.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class ClientEntity {

    @Id
    @SequenceGenerator(name = "id_seq", sequenceName = "clt_seq", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "clientEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProfileEntity> profileList = new ArrayList<>(10);

    public void addProfile(ProfileEntity profileEntity) {
        if (profileEntity != null) {
            this.profileList.add(profileEntity);
            profileEntity.setClientEntity(this);
        }
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
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

    public List<ProfileEntity> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<ProfileEntity> profileList) {
        this.profileList = profileList;
    }
}
