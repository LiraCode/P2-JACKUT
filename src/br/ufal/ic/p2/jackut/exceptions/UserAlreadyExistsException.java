package br.ufal.ic.p2.jackut.exceptions;

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException() {
        super("Conta com esse nome j� existe.");
    }

    public UserAlreadyExistsException(String login) {
        super("J� existe um usu�rio com o login: " + login);
    }

    public UserAlreadyExistsException(Throwable cause) {
        super("J� existe um usu�rio com este login.", cause);
    }
}