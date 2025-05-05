package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to establish a relationship with someone who has them as an enemy.
 */
public class EnemyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EnemyException(String message) {
        super(message);
    }
}