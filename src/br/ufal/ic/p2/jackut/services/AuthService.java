package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Serviço responsável pela autenticação de usuários.
 * Implementa o padrão Service para isolar a lógica de negócio.
 */
public class AuthService {

    private final UserRepository userRepository;

    /**
     * Construtor que recebe o repositório de usuários.
     *
     * @param userRepository o repositório de usuários
     */
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Logs in a user.
     * This method authenticates a user by verifying their login and password,
     * and creates a new session if successful.
     *
     * @param login The login of the user
     * @param senha The password of the user
     * @return The session ID
     * @throws InvalidAuthException if the login or password is invalid
     */
    public String login(String login, String senha) throws InvalidAuthException {
        User user = userRepository.getUserByLogin(login);

        if (user == null || !user.getPassword().equals(senha)) {
            throw new InvalidAuthException("Login ou senha inválidos.");
        }

        String sessionId = generateSessionId(login);
        userRepository.addSession(sessionId, user);
        return sessionId;
    }

    /**
     * Gera um ID único para a sessão do usuário.
     *
     * @param login o login do usuário
     * @return o ID da sessão gerado
     */
    private String generateSessionId(String login) {
        return login + "_" + System.currentTimeMillis();
    }

    /**
     * Verifica se uma sessão é válida.
     *
     * @param sessionId o ID da sessão a ser verificada
     * @return true se a sessão for válida, false caso contrário
     */
    public boolean isValidSession(String sessionId) throws NotFoundUserException {
        return sessionId != null && userRepository.getUserBySession(sessionId) != null;
    }

    /**
     * Obtém o usuário associado a uma sessão.
     *
     * @param sessionId o ID da sessão
     * @return o usuário associado à sessão ou null se a sessão for inválida
     */
    public User getUserFromSession(String sessionId) throws NotFoundUserException {
        return userRepository.getUserBySession(sessionId);
    }

    /**
     * Encerra uma sessão de usuário.
     *
     * @param sessionId o ID da sessão a ser encerrada
     * @return true se a sessão foi encerrada com sucesso, false caso contrário
     */
    public boolean logout(String sessionId) throws NotFoundUserException {
        if (isValidSession(sessionId)) {
            userRepository.removeSession(sessionId);
            return true;
        }
        return false;
    }
}