package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.NotFilledAttributeException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundMessageException;
import br.ufal.ic.p2.jackut.models.Community;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Classe que representa um usu�rio no sistema Jackut.
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String login;
    private String password;
    private Friends friends;
    private Map<String, String> attributes;
    private Queue<Message> messages;
    private ArrayList<String> communitiesJoined;
    private Queue<String> communitiesMessages;
    public ArrayList<String> fans = new ArrayList<>();
    public ArrayList<String> idols = new ArrayList<>();
    private ArrayList<String> crushes = new ArrayList<>();
    private ArrayList<String> enemies = new ArrayList<>();
    /**

    /**
     * Construtor padr�o necess�rio para serializa��o.
     */
    public User() {
        this.friends = new Friends();
        this.attributes = new HashMap<>();
        this.messages = new LinkedList<>();
        this.communitiesJoined = new ArrayList<>();
        this.communitiesMessages = new LinkedList<>();
    }

    /**
     * Construtor principal para criar um usu�rio com nome, login e senha.
     */
    public User(String name, String login, String password) {
        this();
        this.name = name;
        this.login = login;
        this.password = password;
    }

    // Getters e setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Friends getFriends() {
        return friends;
    }

    public List<String> getFriendsList() {
        return friends.getFriendsList();
    }

    public List<String> getFriendSolicitations() {
        return friends.getFriendSolicitations();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Queue<Message> getMessages() {
        return messages;
    }

    public void setCommunitiesJoined(ArrayList<String> communitiesJoined) {
        this.communitiesJoined = communitiesJoined;
    }

    // M�todos funcionais

    /**
     * Adiciona um atributo extra ao usu�rio ou atualiza caso j� exista.
     */
    public void setAttributeExtra(String attribute, String content) {
        attributes.put(attribute, content);
    }

    /**
     * Obt�m um atributo extra do usu�rio.
     * @throws NotFilledAttributeException se o atributo n�o estiver preenchido
     */
    public String getAttributeExtra(String attribute) throws NotFilledAttributeException {
        if (attributes.containsKey(attribute) && !attributes.get(attribute).isEmpty()) {
            return attributes.get(attribute);
        } else {
            throw new NotFilledAttributeException();
        }
    }

    /**
     * Adiciona um recado � fila de mensagens recebidas do usu�rio.
     */
    public void incomingMessage(Message message) {
        messages.add(message);
    }

    /**
     * Verifica se a senha fornecida corresponde � senha do usu�rio.
     */
    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }

    /**
     * Verifica se um usu�rio � amigo.
     */
    public boolean isFriend(String friendLogin) {
        return friends.contains(friendLogin);
    }

    /**
     * Adiciona um amigo � lista de amigos.
     */
    public void addFriend(String friendLogin) {
        friends.addFriend(friendLogin);
    }

    /**
     * Obt�m a lista de amigos do usu�rio.
     * @return Lista de logins dos amigos
     */
    public List<String> getUserFriends() {
        return friends.getFriendsList();
    }

    public List<String> getUserFriendSolicitations() {
        return friends.getFriendSolicitations();
    }



    /**
     * Adiciona uma solicita��o de amizade.
     * @param login Login do usu�rio que solicitou amizade
     */
    public void addFriendSolicitation(String login) {
        friends.addFriendSolicitation(login);
    }

    public void removeFriendSolicitation(String login) {}

    public void removeFriend(String friendLogin) {}
    public void  addCommunity(String name) {
        communitiesJoined.add(name);
    }
    public void removeCommunity(String name) {
        communitiesJoined.remove(name);
    }

    public ArrayList<String> getCommunitiesJoined() {
        return communitiesJoined;
    }

    public String readCommunitiesMessages() throws NotFoundMessageException {

        if (communitiesMessages.isEmpty() || communitiesMessages.peek() == null) {
             throw new NotFoundMessageException("community");

        }
        return communitiesMessages.poll();
    }

    public void addCommunityMessage(String message) {
        communitiesMessages.add(message);
    }



}