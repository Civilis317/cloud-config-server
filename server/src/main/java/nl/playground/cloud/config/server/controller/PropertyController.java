package nl.playground.cloud.config.server.controller;

import nl.playground.cloud.config.server.database.conversion.ConfigConverter;
import nl.playground.cloud.config.server.database.StorageService;
import nl.playground.cloud.config.server.encryption.Encryptor;
import nl.playground.cloud.config.server.rest.http.HttpClient;
import nl.playground.cloud.config.server.rest.model.Client;
import nl.playground.cloud.config.server.rest.http.PropertyRequest;
import nl.playground.cloud.config.server.rest.http.PropertyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class PropertyController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);

    private final StorageService storageService;
    private final Encryptor encryptor;

    public PropertyController(StorageService storageService, Encryptor encryptor) {
        this.storageService = storageService;
        this.encryptor = encryptor;
    }

    @GetMapping(value = "/list")
    public @ResponseBody
    PropertyResponse getList() {
        PropertyResponse response = new PropertyResponse();
        response.setPropertyList(storageService.getPropertyList());
        return response;
    }

    @GetMapping(value = "/get/{application}/{profile}/{key:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    PropertyResponse getProperty(@PathVariable(name = "application") String application,
                                 @PathVariable(name = "profile") String profile,
                                 @PathVariable(name = "key") String key) {
        return getProperty(application, profile, "master", key);
    }

    @GetMapping(value = "/get/{application}/{profile}/{label}/{key:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    PropertyResponse getProperty(@PathVariable(name = "application") String application,
                                 @PathVariable(name = "profile") String profile,
                                 @PathVariable(name = "label") String label,
                                 @PathVariable(name = "key") String key) {
        PropertyResponse response = new PropertyResponse();
        response.getPropertyList().add(storageService.getProperty(application, profile, label, key));
        return response;
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    PropertyResponse saveProperties(@RequestBody PropertyRequest request) {
        PropertyResponse response = new PropertyResponse();
        encrypt(request);
        response.setPropertyList(storageService.saveProperties(ConfigConverter.convertPropertyRequestToProperties(request)));
        HttpClient.refreshClient(getClientUrl(request));
        return response;
    }

    @PostMapping(value = "/delete")
    public void deleteProperties(@RequestBody PropertyRequest request) {
        logger.debug("Deleteing");
        storageService.deleteProperties(ConfigConverter.convertPropertyRequestToProperties(request));
    }

    private void encrypt(PropertyRequest request) {
        String clientName = request.getApplication();
        Client client = storageService.getClient(clientName);
        if (client == null)
            throw new IllegalArgumentException("Unknown client name: " + clientName);
        request.getKeyValueList().stream().forEach(kv -> {
            if (kv.getEncrypt()) {
                kv.setValue(encryptor.encrypt(client.getAlias(), client.getSecret(), kv.getValue()));
            }
        });
    }

    private String getClientUrl (PropertyRequest request) {
        String result = null;
        try {
            Client client = storageService.getSpecificProfile(request.getApplication(), request.getProfile());
            result = client.getProfileList().get(0).getUrl();
        } catch (Throwable e) {
            logger.info("Unable to refresh client/profile: {}/{}", request.getApplication(), request.getProfile());
            logger.error(e.getMessage(), e);
        }
        return result;
    }

}
