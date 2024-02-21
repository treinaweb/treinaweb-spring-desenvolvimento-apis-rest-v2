package br.com.treinaweb.twjobs.api.common.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.treinaweb.twjobs.api.common.dtos.ErrorResponse;
import br.com.treinaweb.twjobs.core.exceptions.ModelNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleModelNotFoundException(ModelNotFoundException exception) {
        return ErrorResponse.builder()
            .message(exception.getLocalizedMessage())
            .build();
    }
    
}
