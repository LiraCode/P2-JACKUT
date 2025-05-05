package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

/**
 * Classe que representa uma Mensagem enviado de um usu�rio para outro no sistema Jackut.
 * Implementa a interface Serializable, permitindo a persist�ncia de objetos em arquivos.
 * Cada Mensagem cont�m informa��es sobre o remetente, destinat�rio e o conte�do da mensagem.
 *
 * @author Felipe Lira
 */
public class Recado implements Serializable {
    private static final long serialVersionUID = 1L; // Identificador �nico para a classe

    /**
     * Login do destinat�rio do Mensagem.
     */
    private String destinatario;

    /**
     * Login do remetente da Mensagem.
     */
    private String remetente;

    /**
     * Conte�do da mensagem da Mensagem.
     */
    private String mensagem;

    /**
     * Construtor que inicializa uma Mensagem com remetente, conte�do da mensagem e destinat�rio.
     *
     * @param remetente O login do usu�rio que enviou a Mensagem.
     * @param mensagem O conte�do da Mensagem.
     * @param destinatario O login do usu�rio que recebeu a Mensagem.
     */
    public Recado(String remetente, String mensagem, String destinatario) {
        this.remetente = remetente;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
    }

    /**
     * Obt�m o login do remetente da Mensagem.
     *
     * @return O login do remetente.
     */
    public String getRemetente() {
        return remetente;
    }

    /**
     * Obt�m o login do destinat�rio da Mensagem.
     *
     * @return O login do destinat�rio.
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Obt�m o conte�do da mensagem do Recado.
     *
     * @return A texto da Mensagem.
     */
    public String getMensagem() {
        return mensagem;
    }
}