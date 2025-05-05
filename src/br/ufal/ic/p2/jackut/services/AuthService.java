package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Servi�o respons�vel pela autentica��o de usu�rios.
 * Implementa o padr�o Service para isolar a l�gica de neg�cio.
 */
public class AuthService {

    private final UserRepository userRepository;

    /**
     * Construtor que recebe o reposit�rio de usu�rios.
     *
     * @param userRepository o reposit�rio de usu�rios
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
            throw new InvalidAuthException("Login ou senha inv�lidos.");
        }

        String sessionId = generateSessionId(login);
        userRepository.addSession(sessionId, user);
        return sessionId;
    }

    /**
     * Gera um ID �nico para a sess�o do usu�rio.
     *
     * @param login o login do usu�rio
     * @return o ID da sess�o gerado
     */
    private String generateSessionId(String login) {
        return login + "_" + System.currentTimeMillis();
    }

    /**
     * Verifica se uma sess�o � v�lida.
     *
     * @param sessionId o ID da sess�o a ser verificada
     * @return true se a sess�o for v�lida, false caso contr�rio
     */
    public boolean isValidSession(String sessionId) throws NotFoundUserException {
        return sessionId != null && userRepository.getUserBySession(sessionId) != null;
    }

    /**
     * Obt�m o usu�rio associado a uma sess�o.
     *
     * @param sessionId o ID da sess�o
     * @return o usu�rio associado � sess�o ou null se a sess�o for inv�lida
     */
    public User getUserFromSession(String sessionId) throws NotFoundUserException {
        return userRepository.getUserBySession(sessionId);
    }

    /**
     * Encerra uma sess�o de usu�rio.
     *
     * @param sessionId o ID da sess�o a ser encerrada
     * @return true se a sess�o foi encerrada com sucesso, false caso contr�rio
     */
    public boolean logout(String sessionId) throws NotFoundUserException {
        if (isValidSession(sessionId)) {
            userRepository.removeSession(sessionId);
            return true;
        }
        return false;
    }
}