package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Serviço responsável pela autenticação de usuários.
 * Implementa o padrão Service para isolar a lógica de negócio.
 */
public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Realiza o login de um usuário e cria uma sessão.
     * @throws InvalidAuthException se o login ou senha forem inválidos
     */
    public String login(String login, String senha) throws InvalidAuthException {
        User user = userRepository.getUserByLogin(login);

        if (user == null || !user.checkPassword(senha)) {
            throw new InvalidAuthException("loginSenha");
        }

        String sessionId = generateSessionId(login);
        userRepository.addSession(sessionId, user);
        return sessionId;
    }

    /**
     * Gera um ID de sessão único para o usuário.
     */
    private String generateSessionId(String login) {
        return login + "_" + System.currentTimeMillis();
    }
}