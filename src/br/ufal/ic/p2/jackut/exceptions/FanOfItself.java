package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exception thrown when a user tries to add themselves as their own idol.
 */
public class FanOfItself extends RuntimeException {

    public FanOfItself() {
        super("Usuário não pode ser fã de si mesmo.");
    }
}