package br.ufal.ic.p2.jackut.exceptions;

/**
 *  Exceção lançada quando se tenta criar uma relação inválida.
 */

public class InvalidRelationshipException extends RuntimeException {
    public InvalidRelationshipException(String message) {
        super(message);
    }
}