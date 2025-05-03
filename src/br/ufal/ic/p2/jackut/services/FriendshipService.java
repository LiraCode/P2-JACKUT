package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidFriendOpException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

import java.util.List;

/**
 * Servi�o respons�vel pelas opera��es relacionadas a amizades.
 * Implementa o padr�o Service para isolar a l�gica de neg�cio.
 */
public class FriendshipService {

    private final UserRepository userRepository;

    /**
     * Construtor que recebe o reposit�rio de usu�rios.
     *
     * @param userRepository o reposit�rio de usu�rios
     */
    public FriendshipService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Verifica se dois usu�rios s�o amigos.
     *
     * @param login o login do primeiro usu�rio
     * @param amigo o login do segundo usu�rio
     * @return true se os usu�rios s�o amigos, false caso contr�rio
     * @throws NotFoundUserException se algum dos usu�rios n�o for encontrado
     */
    public boolean areFriends(String login, String amigo) throws NotFoundUserException {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return user.getFriends().contains(amigo);
    }

    /**
     * Adiciona um amigo ao usu�rio ou envia uma solicita��o de amizade.
     *
     * @param id o ID da sess�o do usu�rio
     * @param amigo o login do usu�rio a ser adicionado como amigo
     * @throws NotFoundUserException se algum dos usu�rios n�o for encontrado
     * @throws InvalidFriendOpException se a opera��o de amizade for inv�lida
     */
    public void addFriend(String id, String amigo) throws NotFoundUserException, InvalidFriendOpException {
        User user = userRepository.getUserBySession(id);
        if (user == null) {
            throw new NotFoundUserException();
        }

        User friendUser = userRepository.getUserByLogin(amigo);
        if (friendUser == null) {
            throw new NotFoundUserException();
        }

        // N�o pode adicionar a si mesmo como amigo
        if (user.getLogin().equals(amigo)) {
            throw new InvalidFriendOpException("self");
        }

        // J� s�o amigos
        if (user.getFriends().contains(amigo)) {
            throw new InvalidFriendOpException("already");
        }

        // Verifica se j� existe uma solicita��o pendente
        List<String> friendSolicitations = friendUser.getFriends().getFriendSolicitations();
        if (friendSolicitations.contains(user.getLogin())) {
            throw new InvalidFriendOpException("pending");
        }

        // Verifica se o amigo j� enviou uma solicita��o
        List<String> userSolicitations = user.getFriends().getFriendSolicitations();
        if (userSolicitations.contains(friendUser.getLogin())) {
            // Se o amigo j� enviou uma solicita��o, aceita automaticamente
            user.getFriends().addFriend(friendUser.getLogin());
            friendUser.getFriends().addFriend(user.getLogin());
        } else {
            // Caso contr�rio, envia uma solicita��o
            friendUser.getFriends().addFriendSolicitation(user.getLogin());
        }
    }

    /**
     * Obt�m a lista de amigos de um usu�rio no formato de texto.
     *
     * @param login o login do usu�rio
     * @return uma string representando a lista de amigos no formato: "{amigo1,amigo2,...}"
     * @throws NotFoundUserException se o usu�rio n�o for encontrado
     */
    public String getFriendsList(String login) throws NotFoundUserException {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return user.getFriends().getFormattedFriendsList();
    }

    /**
     * Obt�m a lista de solicita��es de amizade pendentes de um usu�rio.
     *
     * @param login o login do usu�rio
     * @return uma lista com os logins dos usu�rios que enviaram solicita��es
     * @throws NotFoundUserException se o usu�rio n�o for encontrado
     */
    public List<String> getPendingFriendRequests(String login) throws NotFoundUserException {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return user.getFriends().getFriendSolicitations();
    }

    /**
     * Rejeita uma solicita��o de amizade.
     *
     * @param id o ID da sess�o do usu�rio
     * @param solicitante o login do usu�rio que enviou a solicita��o
     * @throws NotFoundUserException se algum dos usu�rios n�o for encontrado
     * @throws InvalidFriendOpException se n�o houver solicita��o pendente
     */
    public void rejectFriendRequest(String id, String solicitante)
            throws NotFoundUserException, InvalidFriendOpException {
        User user = userRepository.getUserBySession(id);
        if (user == null) {
            throw new NotFoundUserException();
        }

        List<String> solicitations = user.getFriends().getFriendSolicitations();
        if (!solicitations.contains(solicitante)) {
            throw new InvalidFriendOpException("default");
        }

        solicitations.remove(solicitante);
    }
}
