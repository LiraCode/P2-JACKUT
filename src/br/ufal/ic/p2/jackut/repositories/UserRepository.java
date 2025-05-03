package br.ufal.ic.p2.jackut.repositories;

import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Reposit�rio em mem�ria + persist�ncia via serializa��o de usu�rios e sess�es.
 */
public class UserRepository {
    // Armazenamento em mem�ria
    private Map<String, User> users = new HashMap<>();
    private Map<String, User> sessions = new HashMap<>();

    // Nome do arquivo onde serializamos o mapa de usu�rios
    private static final String DATA_FILE = "usuarios.ser";

    /** Adiciona um novo usu�rio ao reposit�rio. */
    public void addUser(User user) {
        users.put(user.getLogin(), user);
    }

    /** Remove usu�rio pelo login. */
    public void removeUser(String login) {
        users.remove(login);
    }

    /** Verifica se usu�rio existe. */
    public boolean userExists(String login) {
        return users.containsKey(login);
    }

    /** Busca um usu�rio pelo login. */
    public User getUserByLogin(String login) {
        return users.get(login);
    }

    /**
     * Busca um usu�rio pelo login ou lan�a exce��o se n�o encontrado.
     *
     * @throws RuntimeException se o usu�rio n�o for encontrado
     */
    public User getUserByLoginOrThrow(String login) throws NotFoundUserException {
        User user = users.get(login);
        if (user == null) {
            throw new NotFoundUserException();
        }
        return user;
    }

    /** Retorna mapa imut�vel de usu�rios (s� leitura). */
    public Map<String, User> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    /** Associa uma sess�o a um usu�rio. */
    public void addSession(String sessionId, User user) {
        sessions.put(sessionId, user);
    }

    /** Remove uma sess�o. */
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    /** Recupera usu�rio a partir de um sessionId. */
    public User getUserBySession(String sessionId) {
        return sessions.get(sessionId);
    }

    /** Limpa tod0 o reposit�rio (usu�rios + sess�es). */
    public void clearAll() {
        users.clear();
        sessions.clear();
    }

    /** Deleta o arquivo de dados do disco. */
    public boolean deleteDataFile() {
        File f = new File(DATA_FILE);
        return !f.exists() || f.delete();
    }

    /** Persiste o mapa de usu�rios em disco. */
    public void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados no arquivo serializado: " + e.getMessage());
            throw new RuntimeException("Erro ao encerrar o sistema.", e);
        }
    }

    /** Carrega o mapa de usu�rios do disco (se existir). */
    @SuppressWarnings("unchecked")
    public void loadData() {
        File f = new File(DATA_FILE);
        if (!f.exists()) {
            users = new HashMap<>();
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            users = (Map<String, User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados do arquivo serializado: " + e.getMessage());
            throw new RuntimeException("Falha ao carregar o sistema.", e);
        }
    }
}
