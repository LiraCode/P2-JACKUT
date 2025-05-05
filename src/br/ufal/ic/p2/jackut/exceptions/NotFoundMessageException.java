package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando não há mensagens para ler.
 */
public class NotFoundMessageException extends RuntimeException {

    /**
     * Construtor padrão que define a mensagem de erro como "Não há recados."
     */
    public NotFoundMessageException() {
        super("Não há recados.");
    }

    /**
     * Construtor que permite definir uma mensagem de erro personalizada.
     *
     * @param type a mensagem de erro
     */
    public NotFoundMessageException(String type) {
        super(type.equals("community") ? "Não há mensagens." : "Não há recados.");
    }
}