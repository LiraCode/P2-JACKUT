package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.NotFoundMessageException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.exceptions.SelfMessageException;
import br.ufal.ic.p2.jackut.models.Message;
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
     * @param id o ID da sess�o do remetente
     * @param destinatario o login do destinat�rio
     * @param mensagem o conte�do da mensagem
     * @throws SelfMessageException se o usu�rio tentar enviar mensagem para si mesmo
     * @throws NotFoundUserException se o remetente ou destinat�rio n�o forem encontrados
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

        // Verifica se o usu�rio est� tentando enviar mensagem para si mesmo
        if (sender.getLogin().equals(destinatario)) {
            throw new SelfMessageException();
        }

        Message recado = new Message(sender.getLogin(), mensagem, destinatario);
        recipient.incomingMessage(recado);
    }

    /**
     * L� a pr�xima mensagem da fila de mensagens do usu�rio.
     *
     * @param id o ID da sess�o do usu�rio
     * @return o conte�do da mensagem lida
     * @throws NotFoundMessageException se n�o houver mensagens para ler
     * @throws NotFoundUserException se o usu�rio n�o for encontrado
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
