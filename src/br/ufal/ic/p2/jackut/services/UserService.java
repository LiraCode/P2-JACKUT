package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.exceptions.InvalidAuthException;
import br.ufal.ic.p2.jackut.exceptions.NotFilledAttributeException;
import br.ufal.ic.p2.jackut.exceptions.NotFoundUserException;
import br.ufal.ic.p2.jackut.exceptions.UserAlreadyExistsException;
import br.ufal.ic.p2.jackut.repositories.UserRepository;

/**
 * Serviço responsável pelas operações relacionadas a usuários.
 * Implementa o padrão Service para isolar a lógica de negócio.
 */
public class UserService {

    private final UserRepository userRepository;

    /**
     * Construtor que recebe o repositório de usuários.
     *
     * @param userRepository o repositório de usuários
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param login o login do usuário
     * @param senha a senha do usuário
     * @param nome o nome do usuário
     * @throws InvalidAuthException se o login ou senha forem inválidos
     * @throws UserAlreadyExistsException se já existir um usuário com o mesmo login
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
     * Obtém um atributo específico de um usuário.
     *
     * @param login o login do usuário
     * @param atributo o atributo a ser obtido
     * @return o valor do atributo
     * @throws NotFoundUserException se o usuário não for encontrado
     * @throws InvalidAuthException se o atributo for inválido
     * @throws NotFilledAttributeException se o atributo não estiver preenchido
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
     * Edita o perfil de um usuário.
     *
     * @param sessionId o ID da sessão do usuário
     * @param atributo o atributo a ser editado
     * @param valor o novo valor do atributo
     * @throws NotFoundUserException se o usuário não for encontrado
     * @throws InvalidAuthException se o login for inválido
     * @throws UserAlreadyExistsException se o novo login já existir
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
     * Métod0 alternativo para editar o perfil de um usuário.
     * Delega a operação para editUserProfile.
     *
     * @param id o ID da sessão do usuário
     * @param atributo o atributo a ser editado
     * @param valor o novo valor do atributo
     * @throws NotFoundUserException se o usuário não for encontrado
     * @throws InvalidAuthException se o login for inválido
     * @throws UserAlreadyExistsException se o novo login já existir
     */
    public void editProfile(String id, String atributo, String valor)
            throws NotFoundUserException, InvalidAuthException, UserAlreadyExistsException {
        editUserProfile(id, atributo, valor);
    }
}
