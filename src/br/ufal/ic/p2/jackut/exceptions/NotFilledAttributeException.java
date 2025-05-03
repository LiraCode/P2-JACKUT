package br.ufal.ic.p2.jackut.exceptions;

public class NotFilledAttributeException extends Exception {

    public NotFilledAttributeException() {
        super("Atributo não preenchido.");
    }

    /** public NotFilledAttributeException(String attribute) {
        super("Atributo " + attribute + " não preenchido.");
    }
    */

    public NotFilledAttributeException(Throwable cause) {
        super("Atributo não preenchido.", cause);
    }
}