package br.ufal.ic.p2.jackut;

import java.io.Serializable;

/**
 * Classe que representa uma Mensagem enviado de um usuário para outro no sistema Jackut.
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
     * Login do remetente da Mensagem.
     */
    private String remetente;

    /**
     * Conteúdo da mensagem da Mensagem.
     */
    private String mensagem;

    /**
     * Construtor que inicializa uma Mensagem com remetente, conteúdo da mensagem e destinatário.
     *
     * @param remetente O login do usuário que enviou a Mensagem.
     * @param mensagem O conteúdo da Mensagem.
     * @param destinatario O login do usuário que recebeu a Mensagem.
     */
    public Message(String remetente, String mensagem, String destinatario) {
        this.remetente = remetente;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
    }

    /**
     * Obtém o login do remetente da Mensagem.
     *
     * @return O login do remetente.
     */
    public String getRemetente() {
        return remetente;
    }

    /**
     * Obtém o login do destinatário da Mensagem.
     *
     * @return O login do destinatário.
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Obtém o conteúdo da mensagem do Recado.
     *
     * @return A texto da Mensagem.
     */
    public String getMensagem() {
        return mensagem;
    }
}