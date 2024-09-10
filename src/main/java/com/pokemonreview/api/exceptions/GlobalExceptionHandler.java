package com.pokemonreview.api.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice       //Hace que esta clase actúe como un controlador global de excepciones.
public class GlobalExceptionHandler {

    //@ExceptionHandler: Este método maneja la excepción PokemonNotFoundException.
    @ExceptionHandler(PokemonNotFoundException.class)
    //ResponseEntity<ErrorObject>: Crea una respuesta HTTP personalizada con el objeto de error.
    //ErrorObject: Contiene detalles del error, como el código de estado, mensaje, y timestamp.
    public ResponseEntity<ErrorObject> handlePokemonNotFoundException(PokemonNotFoundException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
    }

    //@ExceptionHandler: Este método maneja la excepción ReviewNotFoundException.
    @ExceptionHandler(ReviewNotFoundException.class)
    //ResponseEntity<ErrorObject>: Crea una respuesta HTTP personalizada con el objeto de error.
    //ErrorObject: Contiene detalles del error, como el código de estado, mensaje, y timestamp.
    public ResponseEntity<ErrorObject> handlePokemonNotFoundException(ReviewNotFoundException ex, WebRequest request) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
    }
}
