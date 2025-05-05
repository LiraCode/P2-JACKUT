package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando ocorre um erro relacionado a operações com comunidades.
 */
public class InvalidCommunityException extends RuntimeException {

    /**
     * Construtor que define a mensagem de erro com base no tipo de erro.
     *
     * @param type o tipo de erro de operação com comunidade
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
                return "Comunidade não existe.";
            case "alreadyExists":
                return "Comunidade com esse nome já existe.";
            case "notMember":
                return "Usuário não é membro da comunidade.";
            case "alreadyMember":
                return "Usuario já faz parte dessa comunidade.";
            case "notManager":
                return "Apenas o gerente pode editar a comunidade.";
            case "managerCantLeave":
                return "O gerente não pode sair da comunidade.";
            default:
                return type; // Se for uma mensagem personalizada
        }
    }
}
