package br.ufal.ic.p2.jackut;

import java.io.Serializable;

/**
 * Classe que representa um Mensagem enviado de um usuário para outro no sistema Jackut.
 * Implementa a interface Serializable, permitindo a persistência de objetos em arquivos.
 * Cada Mensagem contém informações sobre o remetente, destinatário e o conteúdo da mensagem.
 *
 * @author Felipe Lira
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L; // Identificador único para a classe

    /**
     * Login do destinatário do Mensagem.
     */
    private String destinatario;

    /**
     * Login do remetente do Mensagem.
     */
    private String remetente;

    /**
     * Conteúdo da mensagem do Mensagem.
     */
    private String mensagem;

    /**
     * Construtor que inicializa um Mensagem com remetente, mensagem e destinatário.
     *
     * @param remetente O login do usuário que enviou o Mensagem.
     * @param mensagem O conteúdo do Mensagem.
     * @param destinatario O login do usuário que recebeu o Mensagem.
     */
    public Message(String remetente, String mensagem, String destinatario) {
        this.remetente = remetente;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
    }

    /**
     * Obtém o login do remetente do Mensagem.
     *
     * @return O login do remetente.
     */
    public String getRemetente() {
        return remetente;
    }

    /**
     * Obtém o login do destinatário do Mensagem.
     *
     * @return O login do destinatário.
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Obtém o conteúdo da mensagem do Mensagem.
     *
     * @return A mensagem do Mensagem.
     */
    public String getMensagem() {
        return mensagem;
    }
}