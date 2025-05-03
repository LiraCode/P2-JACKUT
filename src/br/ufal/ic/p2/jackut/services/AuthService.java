package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
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
     * Autentica um usuário e cria uma nova sessão.
     *
     * @param login o login do usuário
     * @param senha a senha do usuário
     * @return o ID da sessão criada
     * @throws InvalidAuthException se o login ou senha forem inválidos
     */
    public String login(String login, String senha) throws InvalidAuthException {
        try {
            User user = userRepository.getUserByLogin(login);

            if (user == null || !user.checkPassword(senha)) {
                throw new InvalidAuthException("Login ou senha inválidos.");
            }

            String sessionId = generateSessionId(login);
            userRepository.addSession(sessionId, user);
            return sessionId;
        } catch (InvalidAuthException e) {
            // Repassamos a exceção original
            throw e;
        } catch (Exception e) {
            // Capturamos outras exceções e as convertemos para InvalidAuthException
            throw new InvalidAuthException("Erro ao realizar login: " + e.getMessage());
        }
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
    public boolean isValidSession(String sessionId) {
        return sessionId != null && userRepository.getUserBySession(sessionId) != null;
    }

    /**
     * Obtém o usuário associado a uma sessão.
     *
     * @param sessionId o ID da sessão
     * @return o usuário associado à sessão ou null se a sessão for inválida
     */
    public User getUserFromSession(String sessionId) {
        return userRepository.getUserBySession(sessionId);
    }

    /**
     * Encerra uma sessão de usuário.
     *
     * @param sessionId o ID da sessão a ser encerrada
     * @return true se a sessão foi encerrada com sucesso, false caso contrário
     */
    public boolean logout(String sessionId) {
        if (isValidSession(sessionId)) {
            userRepository.removeSession(sessionId);
            return true;
        }
        return false;
    }
}