package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando n�o h� mensagens para ler.
 */
public class NotFoundMessageException extends Exception {

    /**
     * Construtor padr�o que define a mensagem de erro como "N�o h� recados."
     */
    public NotFoundMessageException() {
        super("N�o h� recados.");
    }

    /**
     * Construtor que permite definir uma mensagem de erro personalizada.
     *
     * @param message a mensagem de erro
     */
    public NotFoundMessageException(String message) {
        super(message);
    }
}