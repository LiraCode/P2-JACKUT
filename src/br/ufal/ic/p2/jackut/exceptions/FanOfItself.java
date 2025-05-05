package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to add themselves as their own idol.
 */
public class FanOfItself extends RuntimeException {

    public FanOfItself() {
        super("Usu�rio n�o pode ser f� de si mesmo.");
    }
}