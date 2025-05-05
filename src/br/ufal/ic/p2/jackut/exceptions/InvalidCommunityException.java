package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando ocorre um erro relacionado a opera��es com comunidades.
 */
public class InvalidCommunityException extends RuntimeException {

    /**
     * Construtor que define a mensagem de erro com base no tipo de erro.
     *
     * @param type o tipo de erro de opera��o com comunidade
     */
    public InvalidCommunityException(String type) {
        super(getMessageForType(type));
    }

    /**
     * Retorna a mensagem de erro apropriada com base no tipo de erro.
     *
     * @param type o tipo de erro
     * @return a mensagem de erro correspondente
     */
    private static String getMessageForType(String type) {
        switch (type) {
            case "notFound":
                return "Comunidade n�o existe.";
            case "alreadyExists":
                return "Comunidade com esse nome j� existe.";
            case "notMember":
                return "Usu�rio n�o � membro da comunidade.";
            case "alreadyMember":
                return "Usuario j� faz parte dessa comunidade.";
            case "notManager":
                return "Apenas o gerente pode editar a comunidade.";
            case "managerCantLeave":
                return "O gerente n�o pode sair da comunidade.";
            default:
                return type; // Se for uma mensagem personalizada
        }
    }
}
