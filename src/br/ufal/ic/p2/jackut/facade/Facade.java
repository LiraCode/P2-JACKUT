package br.ufal.ic.p2.jackut.facade;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.Message;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.UserRepository;
import br.ufal.ic.p2.jackut.services.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Facade que expõe as funcionalidades do sistema para os clientes.
 * Implementa o padrão Facade para simplificar a interface do sistema.
 */
public class Facade {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    private final SystemService systemService;

    public Facade() {
        this.userRepository = new UserRepository();
        this.systemService = new SystemService(userRepository);
        this.userService = new UserService(userRepository);
        this.authService = new AuthService(userRepository);
        this.friendshipService = new FriendshipService(userRepository);
        this.messageService = new MessageService(userRepository);
    }

    public void zerarSistema() {
        try {
            systemService.resetSystem();
        } catch (Exception e) {
            System.err.println("Erro ao zerar o sistema: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public void carregarSistema() {
        try {
            systemService.loadSystem();
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do arquivo serializado: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public void encerrarSistema() {
        try {
            systemService.saveSystem();
        } catch (Exception e) {
            System.err.println("Erro ao salvar os dados no arquivo serializado: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public void criarUsuario(String login, String senha, String nome)
            throws InvalidAuthException, UserAlreadyExistsException {
        try {
            userService.createUser(login, senha, nome);
        } catch (InvalidAuthException | UserAlreadyExistsException e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public String getAtributoUsuario(String login, String atributo)
            throws NotFoundUserException, NotFilledAttributeException {
        try {
            return userService.getUserAttribute(login, atributo);
        } catch (NotFoundUserException | NotFilledAttributeException e) {
            System.err.println("Erro ao obter atributo: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao obter atributo: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public String abrirSessao(String login, String senha)
            throws InvalidAuthException {
        try {
            return authService.login(login, senha);
        } catch (InvalidAuthException e) {
            System.err.println("Erro ao abrir sessão: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao abrir sessão: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public void editarPerfil(String id, String atributo, String valor)
            throws NotFoundUserException, InvalidAuthException, UserAlreadyExistsException {
        try {
            userService.editProfile(id, atributo, valor);
        } catch (NotFoundUserException | InvalidAuthException | UserAlreadyExistsException e) {
            System.err.println("Erro ao editar perfil: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao editar perfil: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public boolean ehAmigo(String login, String amigo)
            throws NotFoundUserException {
        try {
            return friendshipService.areFriends(login, amigo);
        } catch (NotFoundUserException e) {
            System.err.println("Erro ao verificar amizade: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao verificar amizade: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public void adicionarAmigo(String id, String amigo)
            throws NotFoundUserException, InvalidFriendOpException {
        try {
            friendshipService.addFriend(id, amigo);
        } catch (NotFoundUserException | InvalidFriendOpException e) {
            System.err.println("Erro ao adicionar amigo: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao adicionar amigo: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public String getAmigos(String login)
            throws NotFoundUserException {
        try {
            return friendshipService.getFriendsList(login);
        } catch (NotFoundUserException e) {
            System.err.println("Erro ao obter amigos: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao obter amigos: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public void enviarRecado(String id, String destinatario, String mensagem)
            throws NotFoundUserException, SelfMessageException {
        try {
            messageService.sendMessage(id, destinatario, mensagem);
        } catch (NotFoundUserException | SelfMessageException e) {
            System.err.println("Erro ao enviar recado: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao enviar recado: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }

    public String lerRecado(String id)
            throws NotFoundUserException, NotFoundMessageException {
        try {
            return messageService.readMessage(id);
        } catch (NotFoundUserException | NotFoundMessageException e) {
            System.err.println("Erro ao ler recado: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro ao ler recado: " + e.getMessage());
            throw new SystemOperationException(e);
        }
    }
}
