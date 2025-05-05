package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando se tenta criar um usuário com um login que já existe.
 */

public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Construtor padrão que define a mensagem de erro como "Conta com esse nome já existe."
     */
    public UserAlreadyExistsException() {
        super("Conta com esse nome já existe.");
    }

}