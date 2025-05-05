package br.ufal.ic.p2.jackut.repositories;

import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.*;

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

    /**
     * Removes a user completely from the system, including all references.
     *
     * @param login The login of the user to be removed
     * @return true if the user was successfully removed, false otherwise
     */
    public boolean removeUserCompletely(String login) {
        User user = getUserByLogin(login);
        if (user == null) {
            return false;
        }

        // Get the communities owned by this user before removing them
        List<String> ownedCommunities = new ArrayList<>();
        if (user.getCommunitiesJoined() != null) {
            ownedCommunities.addAll(user.getCommunitiesJoined());
        }

        // Remove user from all friend lists, fan lists, idol lists, etc.
        for (User otherUser : users.values()) {
            // Remove from friends
            if (otherUser.getFriends() != null) {
                otherUser.getFriends().getFriendsList().remove(login);
                otherUser.getFriends().getFriendSolicitations().remove(login);
            }

            // Remove from fans
            if (otherUser.getFans() != null) {
                otherUser.getFans().remove(login);
            }

            // Remove from idols
            if (otherUser.getIdols() != null) {
                otherUser.getIdols().remove(login);
            }

            // Remove from crushes
            if (otherUser.getCrushes() != null) {
                otherUser.getCrushes().remove(login);
            }

            // Remove from enemies
            if (otherUser.getEnemies() != null) {
                otherUser.getEnemies().remove(login);
            }

            // Remove any messages from this user
            if (otherUser.getMessages() != null) {
                Queue<Recado> updatedMessages = new LinkedList<>();
                for (Recado recado : otherUser.getMessages()) {
                    if (!recado.getRemetente().equals(login)) {
                        updatedMessages.add(recado);
                    }
                }
                otherUser.setMessages(updatedMessages);
            }

            // Remove user from communities joined by other users
            if (otherUser.getCommunitiesJoined() != null) {
                // Make a copy to avoid ConcurrentModificationException
                List<String> communitiesToCheck = new ArrayList<>(otherUser.getCommunitiesJoined());
                for (String communityName : communitiesToCheck) {
                    // If this community was owned by the deleted user, remove it from the joined list
                    if (ownedCommunities.contains(communityName)) {
                        otherUser.getCommunitiesJoined().remove(communityName);
                    }
                }
            }
        }

        // Remove user's sessions
        List<String> sessionsToRemove = new ArrayList<>();
        for (Map.Entry<String, User> entry : sessions.entrySet()) {
            if (entry.getValue().getLogin().equals(login)) {
                sessionsToRemove.add(entry.getKey());
            }
        }

        for (String sessionId : sessionsToRemove) {
            sessions.remove(sessionId);
        }

        // Remove communities owned by this user
        for (String communityName : ownedCommunities) {
            // This would need to be implemented in the CommunityRepository
            // communityRepository.removeCommunity(communityName);
        }

        // Finally remove the user
        users.remove(login);
        saveData();
        return true;
    }

    /** Verifica se usu�rio existe. */
    public boolean userExists(String login) {
        return users.containsKey(login);
    }

    /** Busca um usu�rio pelo login. */
    public User getUserByLogin(String login) {
        return users.get(login);
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
    public User getUserBySession(String sessionId) throws NotFoundUserException {
        if (sessions.containsKey(sessionId)) {
            return sessions.get(sessionId);
        }
        throw new NotFoundUserException();
    }

    public void JoinCommunity(String session, String name) throws NotFoundUserException {
        User user = getUserBySession(session);
        if (user == null) {
            throw new NotFoundUserException();
        }
        user.addCommunity(name);

    }

    public void LeaveCommunity(String session, String name) throws NotFoundUserException {
        User user = getUserBySession(session);
        if (user == null) {
            throw new NotFoundUserException();
        }
        user.removeCommunity(name);
    }

    public ArrayList<String> getCommunities(String login) throws NotFoundUserException {
        User user = getUserByLogin(login);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return user.getCommunitiesJoined();
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
