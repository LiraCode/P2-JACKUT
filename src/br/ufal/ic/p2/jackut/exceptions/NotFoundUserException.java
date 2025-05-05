package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando um usuário não é encontrado no sistema.
 */
public class NotFoundUserException extends RuntimeException {

    /**
     * Construtor padrão que define a mensagem de erro como "Usuário não cadastrado."
     */
    public NotFoundUserException() {
        super("Usuário não cadastrado.");
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