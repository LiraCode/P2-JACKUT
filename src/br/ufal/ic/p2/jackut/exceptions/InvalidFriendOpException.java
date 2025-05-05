package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when an invalid friend operation is attempted.
 */
public class InvalidFriendOpException extends RuntimeException {

    public InvalidFriendOpException(String message) {
        super(message);
    }
}