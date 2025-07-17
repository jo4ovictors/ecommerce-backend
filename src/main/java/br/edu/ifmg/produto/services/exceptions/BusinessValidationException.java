package br.edu.ifmg.produto.services.exceptions;

public class BusinessValidationException extends RuntimeException {

  public BusinessValidationException() {
        super();
  }

  public BusinessValidationException(String message) {
        super(message);
  }

}
