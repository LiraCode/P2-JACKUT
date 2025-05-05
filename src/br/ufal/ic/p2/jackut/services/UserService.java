package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Servi�o respons�vel pelas opera��es relacionadas a usu�rios.
 * Implementa o padr�o Service para isolar a l�gica de neg�cio.
 */
public class UserService {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final SystemService systemService;

    /**
     * Construtor que recebe os reposit�rios e servi�os necess�rios.
     *
     * @param userRepository o reposit�rio de usu�rios
     * @param communityRepository o reposit�rio de comunidades
     * @param systemService o servi�o de sistema
     */
    public UserService(UserRepository userRepository, CommunityRepository communityRepository, SystemService systemService) {
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
        this.systemService = systemService;
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
    public void createUser(String login, String senha, String nome) {
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
     * @exception  NotFoundUserException Se o usu�rio inimigo n�o for encontrado.
     * @throws InvalidAuthException se o atributo for inv�lido
     * @throws NotFilledAttributeException se o atributo n�o estiver preenchido
     */
    public String getUserAttribute(String login, String atributo){
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
     * @exception  NotFoundUserException Se o usu�rio inimigo n�o for encontrado.
     * @throws InvalidAuthException se o login for inv�lido
     * @throws UserAlreadyExistsException se o novo login j� existir
     */
    public void editUserProfile(String sessionId, String atributo, String valor) {
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

                userRepository.removeUserCompletely(user.getLogin());
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
     * @exception  NotFoundUserException Se o usu�rio inimigo n�o for encontrado.
     * @throws InvalidAuthException se o login for inv�lido
     * @throws UserAlreadyExistsException se o novo login j� existir
     */
    public void editProfile(String id, String atributo, String valor){
        editUserProfile(id, atributo, valor);
    }

    /**
     * Adiciona um �dolo para o usu�rio.
     *
     * @param userLogin O login do usu�rio que est� adicionando o �dolo.
     * @param idolLogin O login do usu�rio a ser adicionado como �dolo.
     * @exception  NotFoundUserException Se o usu�rio inimigo n�o for encontrado.
     * @throws IdolAlreadyAdded Se o usu�rio j� for �dolo.
     * @throws FanOfItself Se o usu�rio tentar se adicionar como seu pr�prio �dolo.
     * @throws EnemyException Se o �dolo tiver o usu�rio como inimigo.
     */
    public void addIdol(String userLogin, String idolLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        User idol = userRepository.getUserByLogin(idolLogin);

        if (idol == null) {
            throw new NotFoundUserException();
        }

        if (user.getIdols().contains(idolLogin)) {
            throw new IdolAlreadyAdded();
        }

        if (userLogin.equals(idolLogin)) {
            throw new FanOfItself();
        }

        if (idol.getEnemies().contains(userLogin)) {
            throw new EnemyException(idol.getName());
        }

        user.addIdol(idolLogin);
        idol.addFan(userLogin);
    }

    /**
     * Verifica se um usu�rio � f� de outro.
     *
     * @param userLogin O login do usu�rio a verificar.
     * @param idolLogin O login do poss�vel �dolo.
     * @return true se o usu�rio for f� do �dolo, false caso contr�rio.
     */
    public boolean isFan(String userLogin, String idolLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        if (user == null) {
            return false;
        }
        return user.getIdols().contains(idolLogin);
    }

    /**
     * Adiciona uma paquera para o usu�rio.
     *
     * @param userLogin O login do usu�rio que est� adicionando a paquera.
     * @param crushLogin O login do usu�rio a ser adicionado como paquera.
     * @exception  NotFoundUserException Se o usu�rio inimigo n�o for encontrado.
     * @throws EnemyException Se a paquera tiver o usu�rio como inimigo.
     */
    public void addCrush(String userLogin, String crushLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        User crush = userRepository.getUserByLogin(crushLogin);

        if (crush == null) {
            throw new NotFoundUserException();
        }

        user.addCrush(crushLogin);

        if (crush.getEnemies().contains(userLogin)) {
            throw new EnemyException(crush.getName());
        }

        // Verificar se � paquera m�tua
        if (crush.getCrushes().contains(userLogin)) {
            String recadoJackutDefault = "%s � seu paquera - Recado do Jackut.";
            String recadoJackutUser = String.format(recadoJackutDefault, crush.getName());
            String recadoJackutCrush = String.format(recadoJackutDefault, user.getName());

                Recado systemMessage = new Recado("jackut", recadoJackutUser, userLogin);
            Recado systemMessageCrush = new Recado("jackut", recadoJackutCrush, crushLogin);

            user.addMessage(systemMessage);
            crush.addMessage(systemMessageCrush);
        }
    }

    /**
     * Adiciona um inimigo para o usu�rio.
     *
     * @param userLogin O login do usu�rio que est� adicionando o inimigo.
     * @param enemyLogin O login do usu�rio a ser adicionado como inimigo.
     * @exception  NotFoundUserException Se o usu�rio inimigo n�o for encontrado.
     */
    public void addEnemy(String userLogin, String enemyLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        User enemy = userRepository.getUserByLogin(enemyLogin);

        if (enemy == null) {
            throw new NotFoundUserException();
        }

        user.addEnemy(enemyLogin);
    }

    /**
     * Remove um �dolo da lista de �dolos do usu�rio.
     *
     * @param userLogin O login do usu�rio que est� removendo o �dolo.
     * @param idolLogin O login do �dolo a ser removido.
     * @throws RuntimeException Se o usu�rio n�o for �dolo.
     */
    public void removeIdol(String userLogin, String idolLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        User idol = userRepository.getUserByLogin(idolLogin);

        if (!user.getIdols().contains(idolLogin)) {
            throw new RuntimeException("Usu�rio n�o � seu �dolo");
        }

        user.getIdols().remove(idolLogin);
        idol.getFans().remove(userLogin);
    }

    /**
     * Remove uma paquera da lista de paqueras do usu�rio.
     *
     * @param userLogin O login do usu�rio que est� removendo a paquera.
     * @param crushLogin O login da paquera a ser removida.
     * @throws RuntimeException Se o usu�rio n�o for paquera.
     */
    public void removeCrush(String userLogin, String crushLogin) {
        User user = userRepository.getUserByLogin(userLogin);

        if (!user.getCrushes().contains(crushLogin)) {
            throw new RuntimeException("Usu�rio n�o � seu paquera");
        }

        user.getCrushes().remove(crushLogin);
    }

    /**
     * Remove um inimigo da lista de inimigos do usu�rio.
     *
     * @param userLogin O login do usu�rio que est� removendo o inimigo.
     * @param enemyLogin O login do inimigo a ser removido.
     * @throws RuntimeException Se o usu�rio n�o for inimigo.
     */
    public void removeEnemy(String userLogin, String enemyLogin) {
        User user = userRepository.getUserByLogin(userLogin);

        if (!user.getEnemies().contains(enemyLogin)) {
            throw new RuntimeException("Usu�rio n�o � seu inimigo");
        }

        user.getEnemies().remove(enemyLogin);
    }

    /**
     * Verifica se um usu�rio � paquera de outro.
     *
     * @param userLogin O login do usu�rio a verificar.
     * @param crushLogin O login da poss�vel paquera.
     * @return true se o usu�rio for paquera, false caso contr�rio.
     */
    public boolean isCrush(String userLogin, String crushLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        if (user == null) {
            return false;
        }
        return user.getCrushes().contains(crushLogin);
    }

    /**
     * Verifica se um usu�rio � inimigo de outro.
     *
     * @param userLogin O login do usu�rio a verificar.
     * @param enemyLogin O login do poss�vel inimigo.
     * @return true se o usu�rio for inimigo, false caso contr�rio.
     */
    public boolean isEnemy(String userLogin, String enemyLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        if (user == null) {
            return false;
        }
        return user.getEnemies().contains(enemyLogin);
    }

    /**
     * Obt�m a lista de �dolos do usu�rio formatada.
     *
     * @param userLogin O login do usu�rio.
     * @return Uma string formatada com a lista de �dolos.
     */
    public String getIdolsFormatted(String userLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        return "{" + String.join(",", user.getIdols()) + "}";
    }

    /**
     * Obt�m a lista de f�s do usu�rio formatada.
     *
     * @param userLogin O login do usu�rio.
     * @return Uma string formatada com a lista de f�s.
     */
    public String getFansFormatted(String userLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        return "{" + String.join(",", user.getFans()) + "}";
    }

    /**
     * Obt�m a lista de paqueras do usu�rio formatada.
     *
     * @param userLogin O login do usu�rio.
     * @return Uma string formatada com a lista de paqueras.
     */
    public String getCrushesFormatted(String userLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        return "{" + String.join(",", user.getCrushes()) + "}";
    }

    /**
     * Obt�m a lista de inimigos do usu�rio formatada.
     *
     * @param userLogin O login do usu�rio.
     * @return Uma string formatada com a lista de inimigos.
     */
    public String getEnemiesFormatted(String userLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        return "{" + String.join(",", user.getEnemies()) + "}";
    }

    /**
     * Removes a user from the system.
     *
     * @param login The login of the user to be removed
     * @return true if the user was successfully removed, false otherwise
     * @exception  NotFoundUserException Se o usu�rio inimigo n�o for encontrado.
     */
    public boolean removeUser(String login)  {
        User user = userRepository.getUserByLogin(login);
        if (user == null) {
            throw new NotFoundUserException();
        }

        // Get communities owned by the user
        List<String> ownedCommunities = new ArrayList<>();
        if (user.getCommunitiesJoined() != null) {
            ownedCommunities.addAll(user.getCommunitiesJoined());
        }

        // Remove all communities owned by the user
        for (String communityName : ownedCommunities) {
            try {
                communityRepository.removeCommunity(communityName);
            } catch (Exception e) {
                System.err.println("Erro ao remover comunidade: " + e.getMessage());
                // Continue with user removal even if community removal fails
            }
        }

        boolean result = userRepository.removeUserCompletely(login);

        // Persist changes to disk
        userRepository.saveData();
        communityRepository.saveData();

        return result;
    }
}
