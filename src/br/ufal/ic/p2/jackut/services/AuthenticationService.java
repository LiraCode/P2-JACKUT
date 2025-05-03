package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Servi�o respons�vel pela autentica��o de usu�rios.
 * Implementa o padr�o Service para isolar a l�gica de neg�cio.
 */
public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Realiza o login de um usu�rio e cria uma sess�o.
     * @throws InvalidAuthException se o login ou senha forem inv�lidos
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
     * Gera um ID de sess�o �nico para o usu�rio.
     */
    private String generateSessionId(String login) {
        return login + "_" + System.currentTimeMillis();
    }
}