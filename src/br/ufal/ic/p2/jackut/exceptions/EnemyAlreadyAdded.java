package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to add an enemy that is already in their enemy list.
 */
public class EnemyAlreadyAdded extends RuntimeException {

    public EnemyAlreadyAdded() {
        super("Usu�rio j� est� adicionado como inimigo.");
    }

    public EnemyAlreadyAdded(String message) {
        super(message);
    }
}