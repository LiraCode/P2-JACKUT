package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando se tenta criar um usuário com um login que já existe.
 */
public class UserExistsException extends Exception {

    /**
     * Construtor padrão que define a mensagem de erro como "Conta com esse nome já existe."
     */
    public UserExistsException() {
        super("Conta com esse nome já existe.");
    }
}