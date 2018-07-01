package nl.playground.cloud.config.server.database;

import nl.playground.cloud.config.server.database.entity.ClientEntity;
import nl.playground.cloud.config.server.database.entity.ProfileEntity;
import nl.playground.cloud.config.server.rest.Client;
import nl.playground.cloud.config.server.rest.Profile;

public class ClientConverter {

    public static ClientEntity convertClientToEntity(Client client) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(client.getId());
        clientEntity.setName(client.getName());
        client.getProfileList().stream().forEach(p -> {
            ProfileEntity profileEntity = new ProfileEntity();
            profileEntity.setId(p.getId());
            profileEntity.setName(p.getName());
            profileEntity.setUrl(p.getUrl());
            profileEntity.setPublicKey(p.getPublicKey());
            clientEntity.addProfile(profileEntity);
        });
        return clientEntity;
    }

    public static Client convertEntityToClient(ClientEntity entity) {
        Client client = new Client();
        client.setId(entity.getId());
        client.setName(entity.getName());
        entity.getProfileList().stream().forEach(pe -> {
            Profile profile = new Profile();
            profile.setId(pe.getId());
            profile.setName(pe.getName());
            profile.setUrl(pe.getUrl());
            profile.setPublicKey(pe.getPublicKey());
            client.getProfileList().add(profile);
        });
        return client;
    }

}
