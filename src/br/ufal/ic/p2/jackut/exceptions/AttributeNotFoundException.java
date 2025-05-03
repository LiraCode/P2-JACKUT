package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando um atributo de perfil n�o est� preenchido.
 */
public class AttributeNotFoundException extends Exception {

    /**
     * Construtor padr�o que define a mensagem de erro como "Atributo n�o preenchido."
     */
    public AttributeNotFoundException() {
        super("Atributo n�o preenchido.");
    }

    /**
     * Construtor que permite especificar o nome do atributo n�o preenchido.
     *
     * @param attributeName o nome do atributo
     */
    public AttributeNotFoundException(String attributeName) {
        super("Atributo n�o preenchido.");
    }
}