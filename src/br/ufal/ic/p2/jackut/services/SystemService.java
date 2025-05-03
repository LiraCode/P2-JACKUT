package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Serviço responsável pelas operações do sistema.
 * Implementa o padrão Service para isolar a lógica de negócio.
 */
public class SystemService {
    private final UserRepository userRepository;

    public SystemService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void resetSystem() {
        userRepository.clearAll();
        if (!userRepository.deleteDataFile()) {
            System.err.println("Erro ao deletar o arquivo de usuários.");
        }
    }

    public void loadSystem() throws Exception {
        userRepository.loadData();
    }

    public void saveSystem() throws Exception {
        userRepository.saveData();
    }
}