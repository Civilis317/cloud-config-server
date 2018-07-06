package nl.playground.cloud.config.server.database.conversion;

import nl.playground.cloud.config.server.database.entity.ClientEntity;
import nl.playground.cloud.config.server.database.entity.ProfileEntity;
import nl.playground.cloud.config.server.rest.model.Client;
import nl.playground.cloud.config.server.rest.model.Profile;

public class ClientConverter {

    public static ClientEntity convertClientToEntity(Client client) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(client.getId());
        clientEntity.setName(client.getName());
        clientEntity.setPublicKeyAlias(client.getAlias());
        clientEntity.setPublicKeyPwd(client.getSecret());
        client.getProfileList().stream().forEach(p -> {
            ProfileEntity profileEntity = new ProfileEntity();
            profileEntity.setId(p.getId());
            profileEntity.setName(p.getName());
            profileEntity.setUrl(p.getUrl());
            clientEntity.addProfile(profileEntity);
        });
        return clientEntity;
    }

    public static Client convertEntityToClient(ClientEntity entity) {
        Client client = new Client();
        client.setId(entity.getId());
        client.setName(entity.getName());
        client.setAlias(entity.getPublicKeyAlias());
        client.setSecret(entity.getPublicKeyPwd());
        entity.getProfileList().stream().forEach(pe -> {
            Profile profile = new Profile();
            profile.setId(pe.getId());
            profile.setName(pe.getName());
            profile.setUrl(pe.getUrl());
            client.getProfileList().add(profile);
        });
        return client;
    }

}
