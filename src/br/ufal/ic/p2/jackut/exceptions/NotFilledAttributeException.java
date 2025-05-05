package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando um atributo não está preenchido.
 */

public class NotFilledAttributeException extends RuntimeException {

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