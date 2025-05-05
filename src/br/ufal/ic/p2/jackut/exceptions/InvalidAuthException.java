package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando se tenta autenticar com credenciais inv�lidas.
 */

public class InvalidAuthException extends RuntimeException {

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