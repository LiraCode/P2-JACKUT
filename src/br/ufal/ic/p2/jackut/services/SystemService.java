package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.repositories.CommunityRepository;
import br.ufal.ic.p2.jackut.repositories.UserRepository;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.exceptions.InvalidCommunityException;

import java.util.List;
import java.util.Set;

/**
 * Serviço responsável pelas operações do sistema.
 * Gerencia a inicialização e encerramento do sistema.
 */
public class SystemService {
    private final UserRepository userRepository;
    private final CommunityService communityService;

    /**
     * Construtor que recebe os repositórios e serviços necessários.
     *
     * @param userRepository o repositório de usuários
     * @param communityService o serviço de comunidades
     */
    public SystemService(UserRepository userRepository, CommunityService communityService) {
        this.userRepository = userRepository;
        this.communityService = communityService;
    }

    /**
     * Inicializa o sistema, carregando os dados dos arquivos serializados.
     */
    public void loadSystem() {
        userRepository.loadData();
        communityService.loadData();
    }

    /**
     * Encerra o sistema, salvando os dados nos arquivos serializados.
     */
    public void saveSystem() {
        userRepository.saveData();
        communityService.saveData();
    }

    /**
     * Reinicia o sistema, limpando todos os dados.
     */
    public void resetSystem() {
        userRepository.clearAll();
        communityService.clearAll();
        userRepository.deleteDataFile();
        communityService.deleteDataFile();
    }


    /**
     * Lista todas as comunidades de um usuário baseado no seu ID de sessão.
     *
     * @param sessionId o ID da sessão do usuário
     * @return lista com os nomes das comunidades em que o usuário está
     * @throws NotFoundUserException se o usuário não for encontrado
     */
    public String listUserCommunities(String sessionId) throws NotFoundUserException {
        return communityService.listCommunities(sessionId);
    }
}
