package io.hexlet.spring.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException{
    public ResourceAlreadyExistsException (String msg) {
        super(msg);
    }
}
