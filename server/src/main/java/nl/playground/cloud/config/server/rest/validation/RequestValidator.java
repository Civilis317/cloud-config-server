package nl.playground.cloud.config.server.rest.validation;

import nl.playground.cloud.config.server.rest.http.ClientRequest;

public class RequestValidator {

    public static void validate(ClientRequest request) {
        if (request == null || request.getClient() == null) throw new IllegalArgumentException("Invalid request");
        if (request.getClient().getName() == null || request.getClient().getName().length() == 0) {
            throw new IllegalArgumentException("client name is mandatory");
        }

        // todo:
        // if client id == null && no profile, throw error

        // vaidate profile:
        // name is mandatory field
    }
}
