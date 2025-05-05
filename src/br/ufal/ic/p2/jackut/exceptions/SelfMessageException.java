package br.ufal.ic.p2.jackut.exceptions;

/** Exception thrown when a user tries to send a message to themselves. */

public class SelfMessageException extends RuntimeException {
    public SelfMessageException() {
        super("Usu�rio n�o pode enviar recado para si mesmo.");
    }
}
