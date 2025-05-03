package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma operação de amizade é inválida.
 */
public class InvalidFriendOpException extends Exception {

    /**
     * Construtor que define a mensagem de erro com base no tipo de erro.
     *
     * @param type o tipo de erro de operação de amizade
     */
    public InvalidFriendOpException(String type) {
        super(getMessageForType(type));
    }

    public InvalidFriendOpException(Throwable cause) {
        super("Operação de amizade inválida.", cause);
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
                return "Usuário não pode adicionar a si mesmo como amigo.";
            case "already":
                return "Usuário já está adicionado como amigo.";
            case "pending":
                return "Usuário já está adicionado como amigo, esperando aceitação do convite.";
            default:
                return "Operação de amizade inválida.";
        }
    }
}