package br.ufal.ic.p2.jackut.exceptions;

/** Exception thrown when a user tries to send a message to themselves. */

public class SelfMessageException extends RuntimeException {
    public SelfMessageException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }
}
