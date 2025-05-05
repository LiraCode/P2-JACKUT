package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Serviço responsável pela autenticação e gerenciamento de sessões de usuários.
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
     * Realiza o login de um usuário e cria uma sessão.
     *
     * @param login O login do usuário
     * @param senha A senha do usuário
     * @return O ID da sessão
     * @throws InvalidAuthException se o login ou senha for inválido
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
     * @throws NotFoundUserException se o usuário não for encontrado
     */
    public boolean isValidSession(String sessionId) throws NotFoundUserException {
        return sessionId != null && userRepository.getUserBySession(sessionId) != null;
    }

    /**
     * Obtém o usuário associado a uma sessão.
     *
     * @param sessionId o ID da sessão
     * @return o usuário associado à sessão
     * @throws NotFoundUserException se o usuário não for encontrado
     */
    public User getUserFromSession(String sessionId) throws NotFoundUserException {
        User user = userRepository.getUserBySession(sessionId);
        if (user == null) {
            throw new NotFoundUserException("Sessão inválida ou expirada.");
        }
        return user;
    }

}
