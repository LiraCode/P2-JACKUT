package br.ufal.ic.p2.jackut.exceptions;

public class NotFilledAttributeException extends Exception {

    public NotFilledAttributeException() {
        super("Atributo n�o preenchido.");
    }

    /** public NotFilledAttributeException(String attribute) {
        super("Atributo " + attribute + " n�o preenchido.");
    }
    */

    public NotFilledAttributeException(Throwable cause) {
        super("Atributo n�o preenchido.", cause);
    }
}