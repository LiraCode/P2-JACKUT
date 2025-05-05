package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidCommunityException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.models.Community;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.CommunityRepository;
import br.ufal.ic.p2.jackut.repositories.UserRepository;


import java.util.List;


/**
 * Service class responsible for managing community-related operations.
 * This class handles the creation, modification, and deletion of communities,
 * as well as user membership and message management within communities.
 */
public class CommunityService {
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    /**
     * Constructor that initializes the CommunityService with required repositories.
     * Loads community data upon initialization.
     *
     * @param userRepository The repository for user data
     * @param communityRepository The repository for community data
     */
    public CommunityService(UserRepository userRepository, CommunityRepository communityRepository) {
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
        this.communityRepository.loadData(); // Carrega os dados ao inicializar
    }

    /**
     * Creates a new community with the specified name and description.
     * The user identified by the session becomes the community manager.
     *
     * @param sessao The session ID of the user creating the community
     * @param nome The name of the community to be created
     * @param descricao The description of the community
     * @throws InvalidCommunityException If a community with the same name already exists
     * @throws NotFoundUserException If the user session is invalid
     */
    public void createCommunity(String sessao,String nome, String descricao)
            throws InvalidCommunityException, NotFoundUserException {
        User user = userRepository.getUserBySession(sessao);
        if (user == null) {
            throw new NotFoundUserException();
        }


        if (communityRepository.communityExists(nome)) {
            throw new InvalidCommunityException("alreadyExists");
        }

        Community community = new Community(nome, descricao, user.getLogin());
        communityRepository.addCommunity(community);
        userRepository.JoinCommunity(sessao,nome);
        communityRepository.saveData(); // Persiste após criação
        userRepository.saveData();
    }

    /**
     * Retrieves a list of all community names in the system.
     *
     * @return A list of community names
     */
    public List<String> getCommunities() {
        return communityRepository.getCommunities().keySet().stream().toList();
    }

    /**
     * Retrieves a formatted string of communities associated with a specific user.
     *
     * @param nome The login of the user
     * @return A formatted string containing the communities the user belongs to
     * @throws NotFoundUserException If the user is not found
     */
    public String getCommunityByUser(String nome) throws NotFoundUserException {
        return"{" + String.join(",", communityRepository.listCommunitiesByUser(nome)) + "}";

    }

    /**
     * Retrieves a community by its name.
     *
     * @param nome The name of the community to retrieve
     * @return The Community object
     * @throws InvalidCommunityException If the community does not exist
     */
    public Community getCommunity(String nome) throws InvalidCommunityException {
        Community community = communityRepository.getCommunityByName(nome);
        if (community == null) {
            throw new InvalidCommunityException("notFound");
        }
        return community;
    }

    /**
     * Retrieves the description of a community.
     *
     * @param nome The name of the community
     * @return The description of the community
     * @throws InvalidCommunityException If the community does not exist
     */
    public String getCommunityDescription(String nome) throws InvalidCommunityException {
        Community community = getCommunity(nome);
        if (community == null) {
            throw new InvalidCommunityException("notFound");
        }
        return community.getDescription();
    }
    /**
     * Retrieves the manager (owner) of a community.
     *
     * @param owner The name of the community
     * @return The login of the community manager
     * @throws InvalidCommunityException If the community does not exist
     */
    public String getCommunityOwner(String owner) throws InvalidCommunityException {
        Community community = getCommunity(owner);
        if (community == null) {
            throw new InvalidCommunityException("notFound");

        }
        return community.getManager();
    }

    /**
     * Retrieves a formatted string of members in a community.
     *
     * @param nome The name of the community
     * @return A formatted string containing the members of the community
     * @throws InvalidCommunityException If the community does not exist
     */
    public String getCommunityMembers(String nome) throws InvalidCommunityException {
        Community community = getCommunity(nome);
        if (community == null) {
            throw new InvalidCommunityException("notFound");
        }
        return "{" + String.join(",", community.getMembers()) + "}";
    }

    /**
     * Lists the communities a user has joined.
     *
     * @param login The login of the user
     * @return A formatted string containing the communities the user has joined
     * @throws NotFoundUserException If the user is not found
     */
    public String listCommunities(String login) throws NotFoundUserException {

        return "{" + String.join(",", userRepository.getCommunities(login)) + "}";
    }


    /**
     * Adds a user to a community.
     *
     * @param id The session ID of the user
     * @param nome The name of the community to join
     * @throws NotFoundUserException If the user is not found
     * @throws InvalidCommunityException If the community does not exist or the user is already a member
     */
    public void joinCommunity(String id, String nome)
            throws NotFoundUserException, InvalidCommunityException {
        User user = userRepository.getUserBySession(id);
        if (user == null) {
            throw new NotFoundUserException();
        }

        Community community = getCommunity(nome);
        if (community.isMember(user.getLogin())) {
            throw new InvalidCommunityException("alreadyMember");
        }

        community.addMember(user.getLogin());
        userRepository.JoinCommunity(id, nome);// adiciona no User para manter a ordem em que foi adicionado pelo o usuário

        communityRepository.saveData(); // Persiste após adição de membro
        userRepository.saveData();
    }

    /**
     * Removes a user from a community.
     *
     * @param id The session ID of the user
     * @param nome The name of the community to leave
     * @throws NotFoundUserException If the user is not found
     * @throws InvalidCommunityException If the community does not exist, the user is not a member, or the user is the manager
     */
    public void leaveCommunity(String id, String nome)
            throws NotFoundUserException, InvalidCommunityException {
        User user = userRepository.getUserBySession(id);
        if (user == null) {
            throw new NotFoundUserException();
        }

        Community community = getCommunity(nome);
        if (community.isManager(user.getLogin())) {
            throw new InvalidCommunityException("managerCantLeave");
        }
        if (!community.isMember(user.getLogin())) {
            throw new InvalidCommunityException("notMember");
        }

        community.removeMember(user.getLogin());
        userRepository.LeaveCommunity(id, nome);
        communityRepository.saveData(); // Persiste após remoção de membro
        userRepository.saveData();
    }

    /**
     * Updates the description of a community.
     * Only the community manager can perform this operation.
     *
     * @param id The session ID of the user
     * @param nome The name of the community
     * @param descricao The new description for the community
     * @throws NotFoundUserException If the user is not found
     * @throws InvalidCommunityException If the community does not exist or the user is not the manager
     */
    public void editCommunityDescription(String id, String nome, String descricao)
            throws NotFoundUserException, InvalidCommunityException {
        User user = userRepository.getUserBySession(id);
        if (user == null) {
            throw new NotFoundUserException();
        }

        Community community = getCommunity(nome);
        if (!community.isManager(user.getLogin())) {
            throw new InvalidCommunityException("notManager");
        }

        community.setDescription(descricao);
        communityRepository.saveData(); // Persiste após edição
    }

    /**
     * Transfers the management of a community to another user.
     * Only the current manager can perform this operation.
     *
     * @param id The session ID of the current manager
     * @param nome The name of the community
     * @param novoGerente The login of the user who will become the new manager
     * @throws NotFoundUserException If either user is not found
     * @throws InvalidCommunityException If the community does not exist, the current user is not the manager, or the new manager is not a member
     */
    public void transferManagement(String id, String nome, String novoGerente)
            throws NotFoundUserException, InvalidCommunityException {
        User currentManager = userRepository.getUserBySession(id);
        if (currentManager == null) {
            throw new NotFoundUserException();
        }

        User newManager = userRepository.getUserByLogin(novoGerente);
        if (newManager == null) {
            throw new NotFoundUserException();
        }

        Community community = getCommunity(nome);
        if (!community.isManager(currentManager.getLogin())) {
            throw new InvalidCommunityException("notManager");
        }
        if (!community.isMember(novoGerente)) {
            throw new InvalidCommunityException("notMember");
        }

        community.setManager(novoGerente);
        communityRepository.saveData(); // Persiste após transferência
    }

    /**
     * Deletes a community.
     * Only the community manager can perform this operation.
     *
     * @param id The session ID of the user
     * @param nome The name of the community to delete
     * @throws NotFoundUserException If the user is not found
     * @throws InvalidCommunityException If the community does not exist or the user is not the manager
     */
    public void deleteCommunity(String id, String nome)
            throws NotFoundUserException, InvalidCommunityException {
        User user = userRepository.getUserBySession(id);
        if (user == null) {
            throw new NotFoundUserException();
        }

        Community community = getCommunity(nome);
        if (!community.isManager(user.getLogin())) {
            throw new InvalidCommunityException("notManager");
        }

        communityRepository.removeCommunity(nome);
        communityRepository.saveData(); // Persiste após remoção
    }
    // Add these methods to the CommunityService class

    /**
     * Sends a message to a community.
     * The message will be visible to all community members.
     *
     * @param sessionId The session ID of the user sending the message
     * @param communityName The name of the community
     * @param content The content of the message
     * @throws NotFoundUserException If the user is not found
     * @throws InvalidCommunityException If the community does not exist
     */
    public void sendMessage(String sessionId, String communityName, String content) {
        User user = userRepository.getUserBySession(sessionId);
        if (user == null) {
            throw new NotFoundUserException();
        }

        Community community = communityRepository.getCommunityByName(communityName);
        if (community == null) {
            throw new InvalidCommunityException("notFound");
        }

        community.addMessage(content);
        for (String member : community.getMembers()) {
            User memberUser = userRepository.getUserByLogin(member);
            if (memberUser != null) {
                memberUser.addCommunityMessage(content);
            }
        }
    }

    /**
     * Reads messages from communities the user is a member of.
     *
     * @param sessionId The session ID of the user
     * @return The content of the next unread message
     * @throws NotFoundUserException If the user is not found
     */
    public String readMessage(String sessionId) {
        User user = userRepository.getUserBySession(sessionId);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return user.readCommunitiesMessages();
    }

    /**
     * Loads community data from persistent storage.
     */
    public void loadData() {
        communityRepository.loadData();
    }

    /**
     * Saves community data to persistent storage.
     */
    public void saveData() {
        communityRepository.saveData();
    }

    /**
     * Clears all community data from memory.
     */
    public void clearAll() {
        communityRepository.clearAll();
    }

    /**
     * Deletes the community data file.
     *
     * @return true if the file was successfully deleted or did not exist, false otherwise
     */
    public boolean deleteDataFile() {
        return communityRepository.deleteDataFile();
    }

}
