package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando se tenta criar um usu�rio com um login que j� existe.
 */

public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Construtor padr�o que define a mensagem de erro como "Conta com esse nome j� existe."
     */
    public UserAlreadyExistsException() {
        super("Conta com esse nome j� existe.");
    }

}