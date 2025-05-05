package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.EnemyException;
import br.ufal.ic.p2.jackut.exceptions.InvalidFriendOpException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

import java.util.List;

/**
 * Serviço responsável pelas operações relacionadas a amizades.
 * Implementa o padrão Service para isolar a lógica de negócio.
 */
public class FriendshipService {

    private final UserRepository userRepository;

    /**
     * Construtor que recebe o repositório de usuários.
     *
     * @param userRepository o repositório de usuários
     */
    public FriendshipService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Verifica se dois usuários são amigos.
     *
     * @param login o login do primeiro usuário
     * @param amigo o login do segundo usuário
     * @return true se os usuários são amigos, false caso contrário
     * @throws NotFoundUserException se algum dos usuários não for encontrado
     */
    public boolean areFriends(String login, String amigo) throws NotFoundUserException {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return user.getFriends().contains(amigo);
    }

    /**
     * Adiciona um amigo ao usuário ou envia uma solicitação de amizade.
     *
     * @param sessionId o ID da sessão do usuário
     * @param friendLogin o login do usuário a ser adicionado como amigo
     * @throws NotFoundUserException se algum dos usuários não for encontrado
     * @throws InvalidFriendOpException se a operação de amizade for inválida
     */
    public void addFriend(String sessionId, String friendLogin) throws NotFoundUserException, InvalidFriendOpException {
        User user = userRepository.getUserBySession(sessionId);
        if (user == null) {
            throw new NotFoundUserException();
        }

        User friend = userRepository.getUserByLogin(friendLogin);
        if (friend == null) {
            throw new NotFoundUserException();
        }

        if (user.getLogin().equals(friendLogin)) {
            throw new InvalidFriendOpException("Usuário não pode adicionar a si mesmo como amigo.");
        }

        if (friend.getEnemies().contains(user.getLogin())) {
            throw new InvalidFriendOpException("Função inválida: " + friend.getName() + " é seu inimigo.");
        }

        // Já são amigos
        if (user.getFriends().contains(friendLogin)) {
            throw new InvalidFriendOpException("Usuário já está adicionado como amigo.");
        }

        // Verifica se já existe uma solicitação pendente
        List<String> friendSolicitations = friend.getFriends().getFriendSolicitations();
        if (friendSolicitations.contains(user.getLogin())) {
            throw new InvalidFriendOpException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        }

        // Verifica se o amigo já enviou uma solicitação
        List<String> userSolicitations = user.getFriends().getFriendSolicitations();
        if (userSolicitations.contains(friend.getLogin())) {
            // Se o amigo já enviou uma solicitação, aceita automaticamente
            user.getFriends().addFriend(friend.getLogin());
            friend.getFriends().addFriend(user.getLogin());
        } else {
            // Caso contrário, envia uma solicitação
            friend.getFriends().addFriendSolicitation(user.getLogin());
        }
    }

    /**
     * Obtém a lista de amigos de um usuário no formato de texto.
     *
     * @param login o login do usuário
     * @return uma string representando a lista de amigos no formato: "{amigo1,amigo2,...}"
     * @throws NotFoundUserException se o usuário não for encontrado
     */
    public String getFriendsList(String login) throws NotFoundUserException {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return user.getFriends().getFormattedFriendsList();
    }

    /**
     * Obtém a lista de solicitações de amizade pendentes de um usuário.
     *
     * @param login o login do usuário
     * @return uma lista com os logins dos usuários que enviaram solicitações
     * @throws NotFoundUserException se o usuário não for encontrado
     */
    public List<String> getPendingFriendRequests(String login) throws NotFoundUserException {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return user.getFriends().getFriendSolicitations();
    }

    /**
     * Rejeita uma solicitação de amizade.
     *
     * @param id o ID da sessão do usuário
     * @param solicitante o login do usuário que enviou a solicitação
     * @throws NotFoundUserException se algum dos usuários não for encontrado
     * @throws InvalidFriendOpException se não houver solicitação pendente
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
