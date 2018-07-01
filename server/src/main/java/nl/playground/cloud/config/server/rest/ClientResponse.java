package nl.playground.cloud.config.server.rest;

import java.util.ArrayList;
import java.util.List;

public class ClientResponse {
    private List<Client> clientList = new ArrayList<>(10);

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }
}
