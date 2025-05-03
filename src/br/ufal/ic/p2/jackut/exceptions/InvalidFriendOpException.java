package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o de amizade � inv�lida.
 */
public class InvalidFriendOpException extends Exception {

    /**
     * Construtor que define a mensagem de erro com base no tipo de erro.
     *
     * @param type o tipo de erro de opera��o de amizade
     */
    public InvalidFriendOpException(String type) {
        super(getMessageForType(type));
    }

    public InvalidFriendOpException(Throwable cause) {
        super("Opera��o de amizade inv�lida.", cause);
    }

    /**
     * Retorna a mensagem de erro apropriada com base no tipo de erro.
     *
     * @param type o tipo de erro
     * @return a mensagem de erro correspondente
     */
    private static String getMessageForType(String type) {
        switch (type) {
            case "self":
                return "Usu�rio n�o pode adicionar a si mesmo como amigo.";
            case "already":
                return "Usu�rio j� est� adicionado como amigo.";
            case "pending":
                return "Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.";
            default:
                return "Opera��o de amizade inv�lida.";
        }
    }
}