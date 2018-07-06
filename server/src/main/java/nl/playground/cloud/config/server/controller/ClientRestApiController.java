package nl.playground.cloud.config.server.controller;

import nl.playground.cloud.config.server.database.StorageService;
import nl.playground.cloud.config.server.rest.http.ClientRequest;
import nl.playground.cloud.config.server.rest.http.ClientResponse;
import nl.playground.cloud.config.server.rest.validation.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientRestApiController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(ClientRestApiController.class);

    private final StorageService storageService;

    public ClientRestApiController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/list")
    public @ResponseBody
    ClientResponse getList() {
        ClientResponse response = new ClientResponse();
        response.setClientList(storageService.getClientList());
        return response;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ClientResponse getClient(@PathVariable(name = "id") Long id) {
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
