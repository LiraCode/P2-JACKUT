package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Serviço responsável pelas operações relacionadas a mensagens.
 * Implementa o padrão Service para isolar a lógica de negócio.
 */
public class MessageService {
    private final UserRepository userRepository;

    /**
     * Construtor que recebe o repositório de usuários.
     *
     * @param userRepository o repositório de usuários
     */
    public MessageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Envia uma mensagem de um usuário para outro.
     *
     * @param sessionId o ID da sessão do remetente
     * @param destinatario o login do destinatário
     * @param mensagem o conteúdo da mensagem
     * @throws SelfMessageException se o usuário tentar enviar mensagem para si mesmo
     * @throws NotFoundUserException se o remetente ou destinatário não forem encontrados
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
            throw new InvalidFriendOpException("Função inválida: " + recipient.getName() + " é seu inimigo.");
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
            throw new NotFoundMessageException("Não há recados.");
        }

        return recado.getMensagem();
    }
}
