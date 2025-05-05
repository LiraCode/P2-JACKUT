package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.repositories.CommunityRepository;
import br.ufal.ic.p2.jackut.repositories.UserRepository;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.exceptions.InvalidCommunityException;

import java.util.List;
import java.util.Set;

/**
 * Servi�o respons�vel pelas opera��es do sistema.
 * Gerencia a inicializa��o e encerramento do sistema.
 */
public class SystemService {
    private final UserRepository userRepository;
    private final CommunityService communityService;

    /**
     * Construtor que recebe os reposit�rios e servi�os necess�rios.
     *
     * @param userRepository o reposit�rio de usu�rios
     * @param communityService o servi�o de comunidades
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
     * Lista todas as comunidades de um usu�rio baseado no seu ID de sess�o.
     *
     * @param sessionId o ID da sess�o do usu�rio
     * @return lista com os nomes das comunidades em que o usu�rio est�
     * @throws NotFoundUserException se o usu�rio n�o for encontrado
     */
    public String listUserCommunities(String sessionId) throws NotFoundUserException {
        return communityService.listCommunities(sessionId);
    }
}
