package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando um atributo n�o est� preenchido.
 */

public class NotFilledAttributeException extends RuntimeException {

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