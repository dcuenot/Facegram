package gr.personal.story.controller;

import org.bouncycastle.eac.EACException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Nick Kanakis on 1/5/2017.
 */

@ControllerAdvice
public class ErrorHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    // TODO: 1/5/2017  : Replace general handler with specific error handlers

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void processError(Exception e){
        logger.error("Returning HTTP 400 Bad Request", e);
    }

}