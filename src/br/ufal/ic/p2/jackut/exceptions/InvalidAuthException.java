package br.ufal.ic.p2.jackut.exceptions;

public class InvalidAuthException extends Exception {

    public InvalidAuthException(String type) {
        super(getMessageForType(type));
    }

    public InvalidAuthException(Throwable cause) {
        super("Login ou senha inv�lidos.", cause);
    }

    private static String getMessageForType(String type) {
        return switch (type) {
            case "login" -> "Login inv�lido.";
            case "senha" -> "Senha inv�lida.";
            case "loginSenha" -> "Login ou senha inv�lidos.";
            default -> (type);
        };
    }
}