package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to establish a relationship with someone who has them as an enemy.
 */
public class EnemyException extends RuntimeException {

    public EnemyException(String message) {
        super(message);
    }
}