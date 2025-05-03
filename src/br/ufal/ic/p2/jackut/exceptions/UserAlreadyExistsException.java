package br.ufal.ic.p2.jackut.exceptions;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException() {
        super("Conta com esse nome já existe.");
    }

    public UserAlreadyExistsException(String login) {
        super("Já existe um usuário com o login: " + login);
    }

    public UserAlreadyExistsException(Throwable cause) {
        super("Já existe um usuário com este login.", cause);
    }
}