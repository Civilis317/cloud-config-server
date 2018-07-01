package nl.playground.cloud.config.server.controller;

import nl.playground.cloud.config.server.database.ConfigConverter;
import nl.playground.cloud.config.server.database.StorageService;
import nl.playground.cloud.config.server.rest.PropertyRequest;
import nl.playground.cloud.config.server.rest.PropertyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class PropertyController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);

    private final StorageService storageService;

    public PropertyController(StorageService storageService) {
        this.storageService = storageService;
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
        response.setPropertyList(storageService.saveProperties(ConfigConverter.convertPropertyRequestToProperties(request)));
        return response;
    }

}
