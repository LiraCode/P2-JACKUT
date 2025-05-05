package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidCommunityException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundMessageException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.models.Community;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.CommunityRepository;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommunityService {
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;

    public CommunityService(UserRepository userRepository, CommunityRepository communityRepository) {
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
        this.communityRepository.loadData(); // Carrega os dados ao inicializar
    }

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

    public List<String> getCommunities() {
        return communityRepository.getCommunities().keySet().stream().toList();
    }

    public String getCommunityByUser(String nome) throws NotFoundUserException {
        return"{" + String.join(",", communityRepository.listCommunitiesByUser(nome)) + "}";

    }

    public Community getCommunity(String nome) throws InvalidCommunityException {
        Community community = communityRepository.getCommunityByName(nome);
        if (community == null) {
            throw new InvalidCommunityException("notFound");
        }
        return community;
    }

    public String getCommunityDescription(String nome) throws InvalidCommunityException {
        Community community = getCommunity(nome);
        if (community == null) {
            throw new InvalidCommunityException("notFound");
        }
        return community.getDescription();
    }
    public String getCommunityOwner(String owner) throws InvalidCommunityException {
        Community community = getCommunity(owner);
        if (community == null) {
            throw new InvalidCommunityException("notFound");

        }
        return community.getManager();
    }

    public String getCommunityMembers(String nome) throws InvalidCommunityException {
        Community community = getCommunity(nome);
        if (community == null) {
            throw new InvalidCommunityException("notFound");
        }
        return "{" + String.join(",", community.getMembers()) + "}";
    }

    public String listCommunities(String login) throws NotFoundUserException {

        return "{" + String.join(",", userRepository.getCommunities(login)) + "}";
    }


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

    public void sendMessage(String sessionId, String communityName, String content)
            throws NotFoundUserException, InvalidCommunityException {
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

    public String readMessage(String sessionId)
            throws NotFoundUserException, NotFoundMessageException {
        User user = userRepository.getUserBySession(sessionId);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return user.readCommunitiesMessages();
    }


    public List<String> searchCommunities(String termo) {
        String termLower = termo.toLowerCase();
        return communityRepository.getCommunities().values().stream()
                .filter(c -> c.getName().toLowerCase().contains(termLower) ||
                        c.getDescription().toLowerCase().contains(termLower))
                .map(Community::getName)
                .collect(Collectors.toList());
    }
    public void loadData() {
        communityRepository.loadData();
    }

    public void saveData() {
        communityRepository.saveData();
    }

    public void clearAll() {
        communityRepository.clearAll();
    }

    public boolean deleteDataFile() {
        return communityRepository.deleteDataFile();
    }

}
