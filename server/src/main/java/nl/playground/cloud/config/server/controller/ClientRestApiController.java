package nl.playground.cloud.config.server.controller;

import nl.playground.cloud.config.server.database.StorageService;
import nl.playground.cloud.config.server.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientRestApiController extends AbstractController{
    private static final Logger logger = LoggerFactory.getLogger(ClientRestApiController.class);

    private final StorageService storageService;

    public ClientRestApiController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/example", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ClientRequest getExample() {
        Client client = new Client();
        client.setId(Long.valueOf("42"));
        client.setName("config-server-client");

        Profile local = new Profile();
        local.setName("local");
        local.setPublicKey("AgBfFvD7Yyv2pE7H...MORYsRNKQ8ixnFg=");
        local.setUrl("http://localhost:9080/config-client/actuator/refresh");

        client.getProfileList().add(local);

        Profile test = new Profile();
        test.setName("test");
        test.setPublicKey("AgCU3gtR...GHXdQnH3PtHPzo=");
        test.setUrl("http://some.host.com:8080/config-client/actuator/refresh");

        client.getProfileList().add(test);
        ClientRequest request = new ClientRequest();
        request.setClient(client);
        return request;
    }

    @GetMapping(value = "/list")
    public @ResponseBody ClientResponse getList() {
        ClientResponse response = new ClientResponse();
        response.setClientList(storageService.getClientList());
        return response;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ClientResponse getClient(@PathVariable(name = "id") Long id) {
        ClientResponse response = new ClientResponse();
        response.getClientList().add(storageService.getClient(id));
        return response;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ClientResponse newClient(@RequestBody ClientRequest request) {
        RequestValidator.validate(request);
        ClientResponse response = new ClientResponse();
        response.getClientList().add(storageService.save(request.getClient()));
        return response;
    }
}
