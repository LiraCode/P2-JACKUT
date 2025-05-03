package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando se tenta criar um usu�rio com um login que j� existe.
 */
public class UserExistsException extends Exception {

    /**
     * Construtor padr�o que define a mensagem de erro como "Conta com esse nome j� existe."
     */
    public UserExistsException() {
        super("Conta com esse nome j� existe.");
    }
}