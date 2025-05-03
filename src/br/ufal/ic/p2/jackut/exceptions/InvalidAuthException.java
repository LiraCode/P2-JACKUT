package br.ufal.ic.p2.jackut.exceptions;

public class InvalidAuthException extends Exception {

    public InvalidAuthException(String type) {
        super(getMessageForType(type));
    }

    public InvalidAuthException(Throwable cause) {
        super("Login ou senha inválidos.", cause);
    }

    private static String getMessageForType(String type) {
        return switch (type) {
            case "login" -> "Login inválido.";
            case "senha" -> "Senha inválida.";
            case "loginSenha" -> "Login ou senha inválidos.";
            default -> (type);
        };
    }
}