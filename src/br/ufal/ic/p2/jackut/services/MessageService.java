package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Servi�o respons�vel pelas opera��es relacionadas a mensagens.
 * Implementa o padr�o Service para isolar a l�gica de neg�cio.
 */
public class MessageService {
    private final UserRepository userRepository;

    /**
     * Construtor que recebe o reposit�rio de usu�rios.
     *
     * @param userRepository o reposit�rio de usu�rios
     */
    public MessageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Envia uma mensagem de um usu�rio para outro.
     *
     * @param sessionId o ID da sess�o do remetente
     * @param destinatario o login do destinat�rio
     * @param mensagem o conte�do da mensagem
     * @throws SelfMessageException se o usu�rio tentar enviar mensagem para si mesmo
     * @throws NotFoundUserException se o remetente ou destinat�rio n�o forem encontrados
     */
    public void sendMessage(String sessionId, String destinatario, String mensagem) throws NotFoundUserException, SelfMessageException {
        User sender = userRepository.getUserBySession(sessionId);
        if (sender == null) {
            throw new NotFoundUserException();
        }

        User recipient = userRepository.getUserByLogin(destinatario);
        if (recipient == null) {
            throw new NotFoundUserException();
        }

        if (sender.getLogin().equals(destinatario)) {
            throw new SelfMessageException();
        }

        if (recipient.getEnemies().contains(sender.getLogin())) {
            throw new InvalidFriendOpException("Fun��o inv�lida: " + recipient.getName() + " � seu inimigo.");
        }

        Recado recado = new Recado(sender.getLogin(), mensagem, destinatario);
        recipient.incomingMessage(recado);
    }

    /**
     * Reads a message from the user's queue.
     *
     * @param id The session ID of the user
     * @return The content of the message
     * @throws NotFoundUserException if the user doesn't exist
     * @throws NotFoundMessageException if there are no messages
     */
    public String readMessage(String id) throws NotFoundUserException, NotFoundMessageException {
        User user = userRepository.getUserBySession(id);
        if (user == null) {
            throw new NotFoundUserException();
        }

        Recado recado = user.getMessages().poll();
        if (recado == null) {
            throw new NotFoundMessageException("N�o h� recados.");
        }

        return recado.getMensagem();
    }
}
