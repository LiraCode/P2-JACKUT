package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando um atributo de perfil não está preenchido.
 */
public class AttributeNotFoundException extends Exception {

    /**
     * Construtor padrão que define a mensagem de erro como "Atributo não preenchido."
     */
    public AttributeNotFoundException() {
        super("Atributo não preenchido.");
    }

    /**
     * Construtor que permite especificar o nome do atributo não preenchido.
     *
     * @param attributeName o nome do atributo
     */
    public AttributeNotFoundException(String attributeName) {
        super("Atributo não preenchido.");
    }
}