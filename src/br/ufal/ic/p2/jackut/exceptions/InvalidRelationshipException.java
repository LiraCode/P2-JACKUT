package br.ufal.ic.p2.jackut.exceptions;

/**
 *  Exce��o lan�ada quando se tenta criar uma rela��o inv�lida.
 */

public class InvalidRelationshipException extends RuntimeException {
    public InvalidRelationshipException(String message) {
        super(message);
    }
}