package br.edu.ifmg.produto.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
