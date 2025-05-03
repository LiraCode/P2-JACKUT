package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
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
     * Autentica um usu�rio e cria uma nova sess�o.
     *
     * @param login o login do usu�rio
     * @param senha a senha do usu�rio
     * @return o ID da sess�o criada
     * @throws InvalidAuthException se o login ou senha forem inv�lidos
     */
    public String login(String login, String senha) throws InvalidAuthException {
        try {
            User user = userRepository.getUserByLogin(login);

            if (user == null || !user.checkPassword(senha)) {
                throw new InvalidAuthException("Login ou senha inv�lidos.");
            }

            String sessionId = generateSessionId(login);
            userRepository.addSession(sessionId, user);
            return sessionId;
        } catch (InvalidAuthException e) {
            // Repassamos a exce��o original
            throw e;
        } catch (Exception e) {
            // Capturamos outras exce��es e as convertemos para InvalidAuthException
            throw new InvalidAuthException("Erro ao realizar login: " + e.getMessage());
        }
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
    public boolean isValidSession(String sessionId) {
        return sessionId != null && userRepository.getUserBySession(sessionId) != null;
    }

    /**
     * Obt�m o usu�rio associado a uma sess�o.
     *
     * @param sessionId o ID da sess�o
     * @return o usu�rio associado � sess�o ou null se a sess�o for inv�lida
     */
    public User getUserFromSession(String sessionId) {
        return userRepository.getUserBySession(sessionId);
    }

    /**
     * Encerra uma sess�o de usu�rio.
     *
     * @param sessionId o ID da sess�o a ser encerrada
     * @return true se a sess�o foi encerrada com sucesso, false caso contr�rio
     */
    public boolean logout(String sessionId) {
        if (isValidSession(sessionId)) {
            userRepository.removeSession(sessionId);
            return true;
        }
        return false;
    }
}