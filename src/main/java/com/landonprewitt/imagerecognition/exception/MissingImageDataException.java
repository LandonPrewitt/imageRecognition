package com.landonprewitt.imagerecognition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingImageDataException extends RuntimeException{

    public MissingImageDataException(String message) { super(message); }

}
