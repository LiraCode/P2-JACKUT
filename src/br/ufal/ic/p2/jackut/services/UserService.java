package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
import br.ufal.ic.p2.jackut.exceptions.NotFilledAttributeException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.exceptions.UserAlreadyExistsException;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Servi�o respons�vel pelas opera��es relacionadas a usu�rios.
 * Implementa o padr�o Service para isolar a l�gica de neg�cio.
 */
public class UserService {

    private final UserRepository userRepository;

    /**
     * Construtor que recebe o reposit�rio de usu�rios.
     *
     * @param userRepository o reposit�rio de usu�rios
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo usu�rio no sistema.
     *
     * @param login o login do usu�rio
     * @param senha a senha do usu�rio
     * @param nome o nome do usu�rio
     * @throws InvalidAuthException se o login ou senha forem inv�lidos
     * @throws UserAlreadyExistsException se j� existir um usu�rio com o mesmo login
     */
    public void createUser(String login, String senha, String nome)
            throws InvalidAuthException, UserAlreadyExistsException {
        if (login == null || login.isBlank() || login.length() < 3) {
            throw new InvalidAuthException("login");
        }

        if (senha == null || senha.isBlank()) {
            throw new InvalidAuthException("senha");
        }

        if (userRepository.userExists(login)) {
            throw new UserAlreadyExistsException();
        }

        if (nome == null) {
            nome = "";
        }

        User user = new User(nome, login, senha);
        userRepository.addUser(user);
    }

    /**
     * Obt�m um atributo espec�fico de um usu�rio.
     *
     * @param login o login do usu�rio
     * @param atributo o atributo a ser obtido
     * @return o valor do atributo
     * @throws NotFoundUserException se o usu�rio n�o for encontrado
     * @throws InvalidAuthException se o atributo for inv�lido
     * @throws NotFilledAttributeException se o atributo n�o estiver preenchido
     */
    public String getUserAttribute(String login, String atributo)
            throws NotFoundUserException, InvalidAuthException, NotFilledAttributeException {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundUserException();
        }

        return switch (atributo) {
            case "nome"  -> user.getName();
            case "senha" -> user.getPassword();
            case "login" -> user.getLogin();
            default -> {
                try {
                    String value = user.getAttributeExtra(atributo);
                    if (value == null) {
                        throw new NotFilledAttributeException();
                    }
                    yield value;
                } catch (RuntimeException e) {
                    throw new NotFilledAttributeException();
                }
            }
        };
    }

    /**
     * Edita o perfil de um usu�rio.
     *
     * @param sessionId o ID da sess�o do usu�rio
     * @param atributo o atributo a ser editado
     * @param valor o novo valor do atributo
     * @throws NotFoundUserException se o usu�rio n�o for encontrado
     * @throws InvalidAuthException se o login for inv�lido
     * @throws UserAlreadyExistsException se o novo login j� existir
     */
    public void editUserProfile(String sessionId, String atributo, String valor)
            throws NotFoundUserException, InvalidAuthException, UserAlreadyExistsException {
        User user = userRepository.getUserBySession(sessionId);
        if (user == null) {
            throw new NotFoundUserException();
        }

        switch (atributo) {
            case "nome" -> user.setName(valor);
            case "senha" -> user.setPassword(valor);
            case "login" -> {
                if (valor == null || valor.isBlank() || valor.length() < 3) {
                    throw new InvalidAuthException("login");
                }

                if (userRepository.userExists(valor) && !valor.equals(user.getLogin())) {
                    throw new UserAlreadyExistsException();
                }

                userRepository.removeUser(user.getLogin());
                user.setLogin(valor);
                userRepository.addUser(user);
            }
            default -> user.setAttributeExtra(atributo, valor);
        }
    }

    /**
     * M�tod0 alternativo para editar o perfil de um usu�rio.
     * Delega a opera��o para editUserProfile.
     *
     * @param id o ID da sess�o do usu�rio
     * @param atributo o atributo a ser editado
     * @param valor o novo valor do atributo
     * @throws NotFoundUserException se o usu�rio n�o for encontrado
     * @throws InvalidAuthException se o login for inv�lido
     * @throws UserAlreadyExistsException se o novo login j� existir
     */
    public void editProfile(String id, String atributo, String valor)
            throws NotFoundUserException, InvalidAuthException, UserAlreadyExistsException {
        editUserProfile(id, atributo, valor);
    }
}
