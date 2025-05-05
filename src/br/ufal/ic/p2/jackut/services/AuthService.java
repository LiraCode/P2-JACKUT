package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Servi�o respons�vel pela autentica��o e gerenciamento de sess�es de usu�rios.
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
     * Realiza o login de um usu�rio e cria uma sess�o.
     *
     * @param login O login do usu�rio
     * @param senha A senha do usu�rio
     * @return O ID da sess�o
     * @throws InvalidAuthException se o login ou senha for inv�lido
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
     * @throws NotFoundUserException se o usu�rio n�o for encontrado
     */
    public boolean isValidSession(String sessionId) throws NotFoundUserException {
        return sessionId != null && userRepository.getUserBySession(sessionId) != null;
    }

    /**
     * Obt�m o usu�rio associado a uma sess�o.
     *
     * @param sessionId o ID da sess�o
     * @return o usu�rio associado � sess�o
     * @throws NotFoundUserException se o usu�rio n�o for encontrado
     */
    public User getUserFromSession(String sessionId) throws NotFoundUserException {
        User user = userRepository.getUserBySession(sessionId);
        if (user == null) {
            throw new NotFoundUserException("Sess�o inv�lida ou expirada.");
        }
        return user;
    }

}
