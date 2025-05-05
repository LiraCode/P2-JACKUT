package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to add themselves as their own enemy.
 */
public class EnemyOfItself extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EnemyOfItself() {
        super("Usuário não pode ser inimigo de si mesmo.");
    }

    public EnemyOfItself(String message) {
        super(message);
    }
}