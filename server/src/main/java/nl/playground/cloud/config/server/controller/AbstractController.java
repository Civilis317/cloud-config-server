package nl.playground.cloud.config.server.controller;

import nl.playground.cloud.config.server.database.StorageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AbstractController {

    @ExceptionHandler
    protected void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), "The request was not valid");
    }

    @ExceptionHandler
    protected void handleEntityNotFoundException(EntityNotFoundException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), "The requested entity was not found");
    }

    @ExceptionHandler
    protected void handleEntityExistsException(EntityExistsException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "The indicated entity already exists");
    }

    @ExceptionHandler
    protected void handleStorageException(StorageException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error in the epaymentservice caused the request to not be processed");
    }
}
