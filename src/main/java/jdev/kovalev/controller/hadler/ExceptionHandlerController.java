package jdev.kovalev.controller.hadler;

import jdev.kovalev.dto.response.ErrorResponse;
import jdev.kovalev.exception.NotEnoughSocksException;
import jdev.kovalev.exception.SocksNotFoundException;
import jdev.kovalev.exception.UnlogicalFilterConditionException;
import jdev.kovalev.exception.WrongUUIDFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler({
            IllegalArgumentException.class,
            NotEnoughSocksException.class,
            UnlogicalFilterConditionException.class,
            WrongUUIDFormatException.class,
            SocksNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestStatusException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
