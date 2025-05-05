package br.ufal.ic.p2.jackut.repositories;

import br.ufal.ic.p2.jackut.models.Community;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repositório em memória + persistência via serialização de comunidades.
 */
public class CommunityRepository {
    // Armazenamento em memória
    private Map<String, Community> communities = new LinkedHashMap<>();

    // Nome do arquivo onde serializamos o mapa de comunidades
    private static final String DATA_FILE = "comunidades.ser";

    /** Adiciona uma nova comunidade ao repositório. */
    public void addCommunity(Community community) {
        communities.put(community.getName(), community);
    }

    /**
     * Removes a community from the repository.
     * This method completely removes a community from the system, including:
     * 1. Removing it from the communities map
     * 2. Any references to the community in user profiles will need to be handled separately
     *
     * @param name The name of the community to remove
     * @return true if the community was removed, false if it didn't exist
     */
    public boolean removeCommunity(String name) {
        return communities.remove(name) != null;
    }

    /** Verifica se comunidade existe. */
    public boolean communityExists(String name) {
        return communities.containsKey(name);
    }

    /** Busca uma comunidade pelo nome. */
    public Community getCommunityByName(String name) {
        return communities.get(name);
    }

    /** Retorna mapa imutável de comunidades (só leitura). */
    public Map<String, Community> getCommunities() {
        return Collections.unmodifiableMap(communities);

        }

    public List<String> listCommunitiesByUser(String login) {
        return communities.values().stream()
                .filter(community -> community.getMembers().contains(login))
                .map(Community::getName)
                .collect(Collectors.toList());
    }


    public List<String> listCommunities() {
            return new ArrayList<>(getCommunities().keySet());
        }
        /** Persiste o mapa de comunidades em disco. */
    public void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(communities);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados no arquivo serializado: " + e.getMessage());
            throw new RuntimeException("Erro ao encerrar o sistema.", e);
        }
    }

    /** Carrega o mapa de comunidades do disco (se existir). */
    @SuppressWarnings("unchecked")
    public  void loadData() {
        File f = new File(DATA_FILE);
        if (!f.exists()) {
            communities = new HashMap<>();
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            communities = (Map<String, Community>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados do arquivo serializado: " + e.getMessage());
            throw new RuntimeException("Falha ao carregar o sistema.", e);
        }
    }

    /** Deleta o arquivo de dados do disco. */
    public boolean deleteDataFile() {
        File f = new File(DATA_FILE);
        return !f.exists() || f.delete();
    }

    /** Limpa todo o repositório de comunidades. */
    public void clearAll() {
        communities.clear();
    }
}
