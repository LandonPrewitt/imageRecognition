package com.landonprewitt.imagerecognition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Imagga Couldn't process URL")
public class ImaggaImageException extends RuntimeException {

    public ImaggaImageException(String message) { super(message); }

}
