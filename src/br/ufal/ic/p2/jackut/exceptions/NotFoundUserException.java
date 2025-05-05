package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando um usu�rio n�o � encontrado no sistema.
 */
public class NotFoundUserException extends RuntimeException {

    /**
     * Construtor padr�o que define a mensagem de erro como "Usu�rio n�o cadastrado."
     */
    public NotFoundUserException() {
        super("Usu�rio n�o cadastrado.");
    }

    /**
     * Construtor que permite definir uma mensagem de erro personalizada.
     *
     * @param message a mensagem de erro
     */
    public NotFoundUserException(String message) {
        super(message);
    }
}