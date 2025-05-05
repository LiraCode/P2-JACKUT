package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.NotFoundMessageException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.exceptions.SelfMessageException;
import br.ufal.ic.p2.jackut.models.Message;
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
     * @param id o ID da sessão do remetente
     * @param destinatario o login do destinatário
     * @param mensagem o conteúdo da mensagem
     * @throws SelfMessageException se o usuário tentar enviar mensagem para si mesmo
     * @throws NotFoundUserException se o remetente ou destinatário não forem encontrados
     */
    public void sendMessage(String id, String destinatario, String mensagem) throws SelfMessageException, NotFoundUserException {
        User sender = userRepository.getUserBySession(id);
        if (sender == null) {
            throw new NotFoundUserException();
        }

        User recipient = userRepository.getUserByLogin(destinatario);
        if (recipient == null) {
            throw new NotFoundUserException();
        }

        // Verifica se o usuário está tentando enviar mensagem para si mesmo
        if (sender.getLogin().equals(destinatario)) {
            throw new SelfMessageException();
        }

        Message recado = new Message(sender.getLogin(), mensagem, destinatario);
        recipient.incomingMessage(recado);
    }

    /**
     * Lê a próxima mensagem da fila de mensagens do usuário.
     *
     * @param id o ID da sessão do usuário
     * @return o conteúdo da mensagem lida
     * @throws NotFoundMessageException se não houver mensagens para ler
     * @throws NotFoundUserException se o usuário não for encontrado
     */
    public String readMessage(String id) throws NotFoundMessageException, NotFoundUserException {
        User user = userRepository.getUserBySession(id);
        if (user == null) {
            throw new NotFoundUserException();
        }

        Message recado = user.getMessages().poll();
        if (recado == null) {
            throw new NotFoundMessageException();
        }

        return recado.getMensagem();
    }
}
