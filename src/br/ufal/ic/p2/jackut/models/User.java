package br.ufal.ic.p2.jackut.models;

import br.ufal.ic.p2.jackut.exceptions.NotFilledAttributeException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundMessageException;

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
    private Queue<Recado> recados;
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
        this.recados = new LinkedList<>();
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

    public Queue<Recado> getRecados() {
        return recados;
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
    public void incomingMessage(Recado message) {
        recados.add(message);
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

    /**
     * Adiciona uma mensagem � fila de mensagens do usu�rio.
     *
     * @param message A mensagem a ser adicionada.
     */
    public void addMessage(Recado message) {
        this.recados.add(message);
    }

    /**
     * Obt�m a fila de mensagens do usu�rio.
     *
     * @return A fila de mensagens.
     */
    public Queue<Recado> getMessages() {
        return recados;
    }

    /**
     * Sets the messages queue for this user.
     * This method replaces the current messages queue with a new one.
     * It's particularly useful when filtering messages, such as during user removal.
     *
     * @param messages the new messages queue
     */
    public void setMessages(Queue<Recado> messages) {
        this.recados = messages;
    }

    /**
     * Adiciona um usu�rio � lista de �dolos.
     *
     * @param idolLogin O login do �dolo a ser adicionado.
     */
    public void addIdol(String idolLogin) {
        if (!idols.contains(idolLogin)) {
            idols.add(idolLogin);
        }
    }

    /**
     * Adiciona um usu�rio � lista de f�s.
     *
     * @param fanLogin O login do f� a ser adicionado.
     */
    public void addFan(String fanLogin) {
        if (!fans.contains(fanLogin)) {
            fans.add(fanLogin);
        }
    }

    /**
     * Adiciona um usu�rio � lista de paqueras.
     *
     * @param crushLogin O login da paquera a ser adicionada.
     */
    public void addCrush(String crushLogin) {
        if (!crushes.contains(crushLogin)) {
            crushes.add(crushLogin);
        }
    }

    /**
     * Adiciona um usu�rio � lista de inimigos.
     *
     * @param enemyLogin O login do inimigo a ser adicionado.
     */
    public void addEnemy(String enemyLogin) {
        if (!enemies.contains(enemyLogin)) {
            enemies.add(enemyLogin);
        }
    }

    /**
     * Obt�m a lista de paqueras do usu�rio.
     *
     * @return A lista de paqueras.
     */
    public ArrayList<String> getCrushes() {
        return crushes;
    }

    /**
     * Obt�m a lista de inimigos do usu�rio.
     *
     * @return A lista de inimigos.
     */
    public ArrayList<String> getEnemies() {
        return enemies;
    }

    /**
     * Obt�m a lista de �dolos do usu�rio.
     *
     * @return A lista de �dolos.
     */
    public ArrayList<String> getIdols() {
        return idols;
    }

    /**
     * Obt�m a lista de f�s do usu�rio.
     *
     * @return A lista de f�s.
     */
    public ArrayList<String> getFans() {
        return fans;
    }
}