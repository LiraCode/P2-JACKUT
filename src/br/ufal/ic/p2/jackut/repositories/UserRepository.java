package br.ufal.ic.p2.jackut.repositories;

import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Repositório em memória + persistência via serialização de usuários e sessões.
 */
public class UserRepository {
    // Armazenamento em memória
    private Map<String, User> users = new HashMap<>();
    private Map<String, User> sessions = new HashMap<>();

    // Nome do arquivo onde serializamos o mapa de usuários
    private static final String DATA_FILE = "usuarios.ser";

    /** Adiciona um novo usuário ao repositório. */
    public void addUser(User user) {
        users.put(user.getLogin(), user);
    }

    /** Remove usuário pelo login. */
    public void removeUser(String login) {
        users.remove(login);
    }

    /** Verifica se usuário existe. */
    public boolean userExists(String login) {
        return users.containsKey(login);
    }

    /** Busca um usuário pelo login. */
    public User getUserByLogin(String login) {
        return users.get(login);
    }

    /**
     * Busca um usuário pelo login ou lança exceção se não encontrado.
     *
     * @throws RuntimeException se o usuário não for encontrado
     */
    public User getUserByLoginOrThrow(String login) throws NotFoundUserException {
        User user = users.get(login);
        if (user == null) {
            throw new NotFoundUserException();
        }
        return user;
    }

    /** Retorna mapa imutável de usuários (só leitura). */
    public Map<String, User> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    /** Associa uma sessão a um usuário. */
    public void addSession(String sessionId, User user) {
        sessions.put(sessionId, user);
    }

    /** Remove uma sessão. */
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    /** Recupera usuário a partir de um sessionId. */
    public User getUserBySession(String sessionId) {
        return sessions.get(sessionId);
    }

    /** Limpa tod0 o repositório (usuários + sessões). */
    public void clearAll() {
        users.clear();
        sessions.clear();
    }

    /** Deleta o arquivo de dados do disco. */
    public boolean deleteDataFile() {
        File f = new File(DATA_FILE);
        return !f.exists() || f.delete();
    }

    /** Persiste o mapa de usuários em disco. */
    public void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados no arquivo serializado: " + e.getMessage());
            throw new RuntimeException("Erro ao encerrar o sistema.", e);
        }
    }

    /** Carrega o mapa de usuários do disco (se existir). */
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
