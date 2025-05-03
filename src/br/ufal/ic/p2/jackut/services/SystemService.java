package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Servi�o respons�vel pelas opera��es do sistema.
 * Implementa o padr�o Service para isolar a l�gica de neg�cio.
 */
public class SystemService {
    private final UserRepository userRepository;

    public SystemService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void resetSystem() {
        userRepository.clearAll();
        if (!userRepository.deleteDataFile()) {
            System.err.println("Erro ao deletar o arquivo de usu�rios.");
        }
    }

    public void loadSystem() throws Exception {
        userRepository.loadData();
    }

    public void saveSystem() throws Exception {
        userRepository.saveData();
    }
}