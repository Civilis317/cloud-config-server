package nl.playground.cloud.config.server.rest.http;

import nl.playground.cloud.config.server.rest.model.Client;

public class ClientRequest {
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
