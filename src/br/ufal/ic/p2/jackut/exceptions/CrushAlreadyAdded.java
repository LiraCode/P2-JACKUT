package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to add a crush that is already in their crush list.
 */
public class CrushAlreadyAdded extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CrushAlreadyAdded() {
        super("Usuário já está adicionado como paquera.");
    }
}