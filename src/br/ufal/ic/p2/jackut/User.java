package br.ufal.ic.p2.jackut;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Classe que representa um usuário no sistema Jackut.
 * A classe contém informações como nome, login, senha, lista de amigos,
 * solicitações de amizade, atributos extras e recados recebidos.
 * Implementa a interface Serializable para permitir persistência de objetos.
 *
 * @author Felipe Lira
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // Identificador único para a versão da classe

    /**
     * Nome do usuário.
     */
    private String name;

    /**
     * Login do usuário (identificação única).
     */
    private String login;

    /**
     * Senha do usuário para autenticação.
     */
    private String password;

    /**
     * Lista de amigos do usuário.
     */
    private ArrayList<String> friends;

    /**
     * Lista de solicitações de amizade pendentes.
     */
    private ArrayList<String> friendSolicitation;

    /**
     * Map contendo atributos extras personalizados do usuário.
     */
    private Map<String, String> attributes;

    /**
     * Fila de mensagens (recados) recebidas pelo usuário.
     */
    private Queue<Message> messages;

    /**
     * Construtor padrão necessário para serialização. Inicializa listas e mapas vazios.
     */
    public User() {
        this.friends = new ArrayList<>();
        this.friendSolicitation = new ArrayList<>();
        this.attributes = new HashMap<>();
        this.messages = new LinkedList<>();
    }

    /**
     * Construtor principal para criar um usuário com nome, login e senha.
     * Inicializa as listas e mapas automaticamente.
     *
     * @param name O nome do usuário.
     * @param login O login único do usuário.
     * @param password A senha do usuário.
     */
    public User(String name, String login, String password) {
        this(); // Chama o construtor padrão para inicializar listas e mapas
        this.name = name;
        this.login = login;
        this.password = password;
    }

    // Getters e setters com documentação

    /**
     * Obtém o nome do usuário.
     *
     * @return O nome do usuário.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do usuário.
     *
     * @param name O novo nome do usuário.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém o login do usuário.
     *
     * @return O login do usuário.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Define o login do usuário.
     *
     * @param login O novo login do usuário.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Obtém a senha do usuário.
     *
     * @return A senha do usuário.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define a senha do usuário.
     *
     * @param password A nova senha do usuário.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtém a lista de amigos do usuário.
     *
     * @return Lista de amigos do usuário.
     */
    public ArrayList<String> getFriends() {
        return friends;
    }

    /**
     * Define a lista de amigos do usuário.
     *
     * @param friends A lista de amigos a ser definida.
     */
    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    /**
     * Obtém a lista de solicitações de amizade pendentes.
     *
     * @return Lista de solicitações de amizade.
     */
    public ArrayList<String> getFriendSolicitation() {
        return friendSolicitation;
    }

    /**
     * Define a lista de solicitações de amizade pendentes.
     *
     * @param friendSolicitation A nova lista de solicitações.
     */
    public void setFriendSolicitation(ArrayList<String> friendSolicitation) {
        this.friendSolicitation = friendSolicitation;
    }

    /**
     * Obtém os atributos extras personalizados do usuário.
     *
     * @return Mapa contendo os atributos do usuário.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Define os atributos extras do usuário.
     *
     * @param attributes O mapa de atributos a ser definido.
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * Obtém a fila de mensagens recebidas pelo usuário.
     *
     * @return A fila de mensagens (recados) do usuário.
     */
    public Queue<Message> getMessages() {
        return messages;
    }

    /**
     * Define a fila de mensagens recebidas pelo usuário.
     *
     * @param messages A nova fila de recados.
     */
    public void setMessages(Queue<Message> messages) {
        this.messages = messages;
    }

    // Métodos funcionais

    /**
     * Adiciona um atributo extra ao usuário ou atualiza caso já exista.
     *
     * @param atributo O nome do atributo.
     * @param conteudo O valor do atributo.
     */
    public void setAttributeExtra(String atributo, String conteudo) {
        attributes.put(atributo, conteudo);
    }

    /**
     * Obtém um atributo extra do usuário.
     *
     * @param atributo O nome do atributo a ser obtido.
     * @return O valor do atributo, se existir.
     * @throws RuntimeException Caso o atributo não esteja preenchido.
     */
    public String getAttributeExtra(String atributo) {
        if (attributes.containsKey(atributo)) {
            return attributes.get(atributo);
        } else {
            throw new RuntimeException("Atributo não preenchido.");
        }
    }

    /**
     * Adiciona um amigo à lista de amigos do usuário e remove da lista de solicitações pendentes.
     *
     * @param friend O login do amigo a ser adicionado.
     */
    public void addFriend(String friend) {
        this.friendSolicitation.remove(friend); // Remove da lista de solicitações
        if (!this.friends.contains(friend)) {
            this.friends.add(friend); // Adiciona à lista de amigos
        }
    }

    /**
     * Adiciona uma solicitação de amizade à lista de solicitações pendentes do usuário.
     *
     * @param friend O login do usuário que enviou a solicitação.
     */
    public void addFriendSolicitation(String friend) {
        if (!this.friendSolicitation.contains(friend)) {
            this.friendSolicitation.add(friend); // Evita duplicatas
        }
    }

    /**
     * Adiciona um recado à fila de mensagens recebidas do usuário.
     *
     * @param recado O objeto Message a ser adicionado.
     */
    public void incomingMessage(Message recado) {
        messages.add(recado); // Adiciona à fila de recados
    }

    /**
     * Verifica se a senha fornecida corresponde à senha do usuário.
     *
     * @param password A senha a ser verificada.
     * @return true se a senha corresponder; false caso contrário.
     */
    public boolean checkPassword(String password) {
        return Objects.equals(this.password, password);
    }
}

