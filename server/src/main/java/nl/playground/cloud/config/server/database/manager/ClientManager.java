package nl.playground.cloud.config.server.database.manager;

import nl.playground.cloud.config.server.database.entity.ClientEntity;
import nl.playground.cloud.config.server.database.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ClientManager {
    private final ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Stream<ClientEntity> getClientList () {
        return StreamSupport.stream(clientRepository.findAll().spliterator(), true);
    }

    public Optional<ClientEntity> findClientById(Long id) {
        return clientRepository.findById(id);
    }

    public ClientEntity save(ClientEntity client) {
        return clientRepository.save(client);
    }
}
