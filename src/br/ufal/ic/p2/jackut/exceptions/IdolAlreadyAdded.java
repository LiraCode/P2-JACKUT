package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to add an idol that is already in their idol list.
 */
public class IdolAlreadyAdded extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public IdolAlreadyAdded() {
        super("Usuário já está adicionado como ídolo.");
    }
}