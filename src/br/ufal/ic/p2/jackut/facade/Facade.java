package br.ufal.ic.p2.jackut.facade;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.CommunityRepository;
import br.ufal.ic.p2.jackut.repositories.UserRepository;
import br.ufal.ic.p2.jackut.services.*;

import java.util.List;

/**
 * Facade que exp�e as funcionalidades do sistema para os clientes.
 * Implementa o padr�o Facade para simplificar a interface do sistema.
 */
public class Facade {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final UserService userService;
    private final AuthService authService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    private final SystemService systemService;
    private final CommunityService communityService;
    private final RelationshipService relationshipService;

    public Facade() {
        this.userRepository = new UserRepository();
        this.communityRepository = new CommunityRepository();
        this.communityService = new CommunityService(userRepository, communityRepository);
        this.systemService = new SystemService(userRepository, communityService);
        this.userService = new UserService(userRepository, communityRepository, systemService);
        this.authService = new AuthService(userRepository);
        this.friendshipService = new FriendshipService(userRepository);
        this.messageService = new MessageService(userRepository);
        this.relationshipService = new RelationshipService(userRepository, authService);
    }

    public void zerarSistema() {
        systemService.resetSystem();

    }

    public void carregarSistema() {
        systemService.loadSystem();

    }

    public void encerrarSistema() {
        systemService.saveSystem();
    }

    public void criarUsuario(String login, String senha, String nome)
            throws InvalidAuthException, UserAlreadyExistsException {

        userService.createUser(login, senha, nome);

    }

    public String getAtributoUsuario(String login, String atributo)
            throws NotFoundUserException, NotFilledAttributeException, InvalidAuthException {

        return userService.getUserAttribute(login, atributo);

    }

    public String abrirSessao(String login, String senha)
            throws InvalidAuthException {
        return authService.login(login, senha);

    }

    public void editarPerfil(String id, String atributo, String valor)
            throws NotFoundUserException, InvalidAuthException, UserAlreadyExistsException {

        userService.editProfile(id, atributo, valor);

    }

    public boolean ehAmigo(String login, String amigo)
            throws NotFoundUserException {
        return friendshipService.areFriends(login, amigo);
    }

    public void adicionarAmigo(String id, String amigo)
            throws NotFoundUserException, InvalidFriendOpException {

        friendshipService.addFriend(id, amigo);

    }

    public String getAmigos(String login)
            throws NotFoundUserException {
        return friendshipService.getFriendsList(login);

    }

    public void enviarRecado(String id, String destinatario, String mensagem)
            throws NotFoundUserException, SelfMessageException {

        messageService.sendMessage(id, destinatario, mensagem);


    }

    public String lerRecado(String id)
            throws NotFoundUserException, NotFoundMessageException {
        return messageService.readMessage(id);

    }

    public void criarComunidade(String session, String nome, String descricao) throws InvalidCommunityException, NotFoundUserException {

        communityService.createCommunity(session, nome, descricao);
//        }
    }

    public void editarComunidade(String session, String nome, String descricao) throws InvalidCommunityException, NotFoundUserException {

        communityService.editCommunityDescription(session, nome, descricao);

    }

    public void deletarComunidade(String session, String nome) throws InvalidCommunityException, NotFoundUserException {

        communityService.deleteCommunity(session, nome);

    }

    public void adicionarComunidade(String session, String nome) throws InvalidCommunityException, NotFoundUserException {
        communityService.joinCommunity(session, nome);

    }

    public void sairComunidade(String session, String nome) throws InvalidCommunityException, NotFoundUserException {

        communityService.leaveCommunity(session, nome);

    }

    public void listarComunidades(String session) throws  NotFoundUserException {

        communityService.listCommunities(session);

    }

    public String getDescricaoComunidade(String nome) throws InvalidCommunityException {
        return communityService.getCommunityDescription(nome);
    }

    public String getDonoComunidade(String nome) throws InvalidCommunityException {
        return communityService.getCommunityOwner(nome);
    }

    public String getMembrosComunidade(String nome) throws InvalidCommunityException {
        return communityService.getCommunityMembers(nome);
    }

    public String getComunidades(String nome) throws  NotFoundUserException {


        return communityService.listCommunities(nome);
    }

    public void enviarMensagem(String id, String comunidade, String mensagem)
            throws NotFoundUserException, InvalidCommunityException {
        communityService.sendMessage(id, comunidade, mensagem);
    }

    public String lerMensagem(String id)
            throws NotFoundUserException, NotFoundMessageException {
        return communityService.readMessage(id);
    }

    /**
     * Adiciona um usu�rio como �dolo de outro.
     *
     * @param id    O ID da sess�o do usu�rio que est� adicionando o �dolo.
     * @param idolo O login do usu�rio a ser adicionado como �dolo.
     */
    public void adicionarIdolo(String id, String idolo) throws NotFoundUserException {

        relationshipService.adicionarIdolo(id, idolo);
    }

    /**
     * Verifica se um usu�rio � f� de outro.
     *
     * @param login O login do usu�rio a verificar.
     * @param idolo O login do poss�vel �dolo.
     * @return true se o usu�rio for f� do �dolo, false caso contr�rio.
     */
    public boolean ehFa(String login, String idolo) {

        return relationshipService.ehFa(login, idolo);

    }

    /**
     * Obt�m a lista de f�s de um usu�rio.
     *
     * @param login O login do usu�rio.
     * @return Uma string formatada com a lista de f�s.
     */
    public String getFas(String login) {

        return relationshipService.getFas(login);

    }

    /**
     * Adiciona um usu�rio como paquera de outro.
     *
     * @param id      O ID da sess�o do usu�rio que est� adicionando a paquera.
     * @param paquera O login do usu�rio a ser adicionado como paquera.
     */
    public void adicionarPaquera(String id, String paquera) throws NotFoundUserException {

        relationshipService.adicionarPaquera(id, paquera);

    }

    /**
     * Verifica se um usu�rio � paquera de outro.
     *
     * @param id      O ID da sess�o do usu�rio.
     * @param paquera O login da poss�vel paquera.
     * @return true se o usu�rio for paquera, false caso contr�rio.
     */
    public boolean ehPaquera(String id, String paquera) throws NotFoundUserException {

        return relationshipService.ehPaquera(id, paquera);

    }

    /**
     * Obt�m a lista de paqueras de um usu�rio.
     *
     * @param id O ID da sess�o do usu�rio.
     * @return Uma string formatada com a lista de paqueras.
     */
    public String getPaqueras(String id) throws NotFoundUserException {

        return relationshipService.getPaqueras(id);

    }

    /**
     * Adiciona um usu�rio como inimigo de outro.
     *
     * @param id      O ID da sess�o do usu�rio que est� adicionando o inimigo.
     * @param inimigo O login do usu�rio a ser adicionado como inimigo.
     */
    public void adicionarInimigo(String id, String inimigo) throws NotFoundUserException {
        relationshipService.adicionarInimigo(id, inimigo);

    }

    /**
     * Verifica se um usu�rio � inimigo de outro.
     *
     * @param id      O ID da sess�o do usu�rio.
     * @param inimigo O login do poss�vel inimigo.
     * @return true se o usu�rio for inimigo, false caso contr�rio.
     */
    public boolean ehInimigo(String id, String inimigo) throws NotFoundUserException {
        return relationshipService.ehInimigo(id, inimigo);

    }

    /**
     * Obt�m a lista de inimigos de um usu�rio.
     *
     * @param id O ID da sess�o do usu�rio.
     * @return Uma string formatada com a lista de inimigos.
     */
    public String getInimigos(String id) throws NotFoundUserException {
        return relationshipService.getInimigos(id);

    }

    /**
     * Removes the currently logged-in user from the system.
     *
     * @param sessionId The session ID of the user to be removed
     * @throws NotFoundUserException if the user doesn't exist
     */
    public void removerUsuario(String sessionId) throws NotFoundUserException {

        User user = userRepository.getUserBySession(sessionId);
        String login = user.getLogin();
        userService.removeUser(login);

    }
}

