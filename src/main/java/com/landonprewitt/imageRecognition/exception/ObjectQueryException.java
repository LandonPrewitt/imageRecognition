package com.landonprewitt.imageRecognition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ObjectQueryException extends RuntimeException {

    public ObjectQueryException(String message) { super(message); }

}
