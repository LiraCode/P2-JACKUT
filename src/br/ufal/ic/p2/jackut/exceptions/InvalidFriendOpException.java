package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when an invalid friend operation is attempted.
 */
public class InvalidFriendOpException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidFriendOpException(String message) {
        super(message);
    }
}