package br.ufal.ic.p2.jackut.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Classe que representa as relações de amizade de um usuário.
 * Gerencia a lista de amigos e solicitações de amizade pendentes.
 */
public class Friends implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> friendsList;
    private final List<String> friendSolicitations;

    /**
     * Construtor padrão que inicializa as listas vazias.
     */
    public Friends() {
        friendsList = new ArrayList<>();
        friendSolicitations = new ArrayList<>();
    }

    /**
     * Obtém a lista de amigos.
     * @return Lista de logins dos amigos
     */
    public List<String> getFriendsList() {
        return friendsList;
    }

    /**
     * Obtém a lista de solicitações de amizade pendentes.
     * @return Lista de logins dos solicitantes
     */
    public List<String> getFriendSolicitations() {
        return friendSolicitations;
    }

    /**
     * Adiciona um amigo à lista de amigos e remove da lista de solicitações.
     * @param friendLogin Login do amigo a ser adicionado
     */
    public void addFriend(String friendLogin) {
        this.friendSolicitations.remove(friendLogin);
        if (!this.friendsList.contains(friendLogin)) {
            this.friendsList.add(friendLogin);
        }
    }

    /**
     * Adiciona uma solicitação de amizade pendente.
     * @param friendLogin Login do usuário que enviou a solicitação
     */
    public void addFriendSolicitation(String friendLogin) {
        if (!this.friendSolicitations.contains(friendLogin)) {
            this.friendSolicitations.add(friendLogin);
        }
    }

    /**
     * Verifica se um usuário está na lista de amigos.
     * @param friendLogin Login do usuário a verificar
     * @return true se for amigo, false caso contrário
     */
    public boolean contains(String friendLogin) {
        return this.friendsList.contains(friendLogin);
    }

    /**
     * Retorna a lista de amigos formatada para exibição.
     * @return String formatada com a lista de amigos
     */
    public String getFormattedFriendsList() {
        return "{" + String.join(",", this.friendsList) + "}";
    }

    /**
     * Removes a friend from the friends list.
     * This method removes the specified user from this user's friends list.
     *
     * @param friendLogin the login of the friend to remove
     * @return true if the friend was removed, false if they weren't in the list
     */
    public boolean removeFriend(String friendLogin) {
        return this.friendsList.remove(friendLogin);
    }

    /**
     * Removes a friend solicitation.
     * This method removes a pending friend request from this user's solicitations list.
     *
     * @param friendLogin the login of the friend solicitation to remove
     * @return true if the solicitation was removed, false if it wasn't in the list
     */
    public boolean removeFriendSolicitation(String friendLogin) {
        return this.friendSolicitations.remove(friendLogin);
    }
}