package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to add themselves as their own crush.
 */
public class CrushOfItself extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CrushOfItself() {
        super("Usuário não pode ser paquera de si mesmo.");
    }

    public CrushOfItself(String message) {
        super(message);
    }
}