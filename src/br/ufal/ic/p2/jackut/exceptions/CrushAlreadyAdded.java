package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to add a crush that is already in their crush list.
 */
public class CrushAlreadyAdded extends RuntimeException {

    public CrushAlreadyAdded() {
        super("Usu�rio j� est� adicionado como paquera.");
    }
}