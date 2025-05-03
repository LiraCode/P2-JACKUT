package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando não há mensagens para ler.
 */
public class NotFoundMessageException extends Exception {

    /**
     * Construtor padrão que define a mensagem de erro como "Não há recados."
     */
    public NotFoundMessageException() {
        super("Não há recados.");
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