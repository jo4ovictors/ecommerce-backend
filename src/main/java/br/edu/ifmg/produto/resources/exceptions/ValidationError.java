package br.edu.ifmg.produto.resources.exceptions;


import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    private List<FieldMessage> errors = new ArrayList<FieldMessage>();

    public ValidationError() {}

    public List<FieldMessage> getFieldMessages() {
        return errors;
    }

    public void setFieldMessages(List<FieldMessage> fieldMessages) {
        this.errors = fieldMessages;
    }

    public void addFieldMessage(String field, String message) {
        this.errors.add(new FieldMessage(field, message));
    }

}
