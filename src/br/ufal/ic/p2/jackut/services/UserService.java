package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.repositories.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Serviço responsável pelas operações relacionadas a usuários.
 * Implementa o padrão Service para isolar a lógica de negócio.
 */
public class UserService {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final SystemService systemService;

    /**
     * Construtor que recebe os repositórios e serviços necessários.
     *
     * @param userRepository o repositório de usuários
     * @param communityRepository o repositório de comunidades
     * @param systemService o serviço de sistema
     */
    public UserService(UserRepository userRepository, CommunityRepository communityRepository, SystemService systemService) {
        this.userRepository = userRepository;
        this.communityRepository = communityRepository;
        this.systemService = systemService;
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
     * Obtém um atributo específico de um usuário.
     *
     * @param login o login do usuário
     * @param atributo o atributo a ser obtido
     * @return o valor do atributo
     * @exception  NotFoundUserException Se o usuário inimigo não for encontrado.
     * @throws InvalidAuthException se o atributo for inválido
     * @throws NotFilledAttributeException se o atributo não estiver preenchido
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
     * Edita o perfil de um usuário.
     *
     * @param sessionId o ID da sessão do usuário
     * @param atributo o atributo a ser editado
     * @param valor o novo valor do atributo
     * @exception  NotFoundUserException Se o usuário inimigo não for encontrado.
     * @throws InvalidAuthException se o login for inválido
     * @throws UserAlreadyExistsException se o novo login já existir
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
     * Métod0 alternativo para editar o perfil de um usuário.
     * Delega a operação para editUserProfile.
     *
     * @param id o ID da sessão do usuário
     * @param atributo o atributo a ser editado
     * @param valor o novo valor do atributo
     * @exception  NotFoundUserException Se o usuário inimigo não for encontrado.
     * @throws InvalidAuthException se o login for inválido
     * @throws UserAlreadyExistsException se o novo login já existir
     */
    public void editProfile(String id, String atributo, String valor){
        editUserProfile(id, atributo, valor);
    }

    /**
     * Adiciona um ídolo para o usuário.
     *
     * @param userLogin O login do usuário que está adicionando o ídolo.
     * @param idolLogin O login do usuário a ser adicionado como ídolo.
     * @exception  NotFoundUserException Se o usuário inimigo não for encontrado.
     * @throws IdolAlreadyAdded Se o usuário já for ídolo.
     * @throws FanOfItself Se o usuário tentar se adicionar como seu próprio ídolo.
     * @throws EnemyException Se o ídolo tiver o usuário como inimigo.
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
     * Verifica se um usuário é fã de outro.
     *
     * @param userLogin O login do usuário a verificar.
     * @param idolLogin O login do possível ídolo.
     * @return true se o usuário for fã do ídolo, false caso contrário.
     */
    public boolean isFan(String userLogin, String idolLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        if (user == null) {
            return false;
        }
        return user.getIdols().contains(idolLogin);
    }

    /**
     * Adiciona uma paquera para o usuário.
     *
     * @param userLogin O login do usuário que está adicionando a paquera.
     * @param crushLogin O login do usuário a ser adicionado como paquera.
     * @exception  NotFoundUserException Se o usuário inimigo não for encontrado.
     * @throws EnemyException Se a paquera tiver o usuário como inimigo.
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

        // Verificar se é paquera mútua
        if (crush.getCrushes().contains(userLogin)) {
            String recadoJackutDefault = "%s é seu paquera - Recado do Jackut.";
            String recadoJackutUser = String.format(recadoJackutDefault, crush.getName());
            String recadoJackutCrush = String.format(recadoJackutDefault, user.getName());

                Recado systemMessage = new Recado("jackut", recadoJackutUser, userLogin);
            Recado systemMessageCrush = new Recado("jackut", recadoJackutCrush, crushLogin);

            user.addMessage(systemMessage);
            crush.addMessage(systemMessageCrush);
        }
    }

    /**
     * Adiciona um inimigo para o usuário.
     *
     * @param userLogin O login do usuário que está adicionando o inimigo.
     * @param enemyLogin O login do usuário a ser adicionado como inimigo.
     * @exception  NotFoundUserException Se o usuário inimigo não for encontrado.
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
     * Remove um ídolo da lista de ídolos do usuário.
     *
     * @param userLogin O login do usuário que está removendo o ídolo.
     * @param idolLogin O login do ídolo a ser removido.
     * @throws RuntimeException Se o usuário não for ídolo.
     */
    public void removeIdol(String userLogin, String idolLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        User idol = userRepository.getUserByLogin(idolLogin);

        if (!user.getIdols().contains(idolLogin)) {
            throw new RuntimeException("Usuário não é seu ídolo");
        }

        user.getIdols().remove(idolLogin);
        idol.getFans().remove(userLogin);
    }

    /**
     * Remove uma paquera da lista de paqueras do usuário.
     *
     * @param userLogin O login do usuário que está removendo a paquera.
     * @param crushLogin O login da paquera a ser removida.
     * @throws RuntimeException Se o usuário não for paquera.
     */
    public void removeCrush(String userLogin, String crushLogin) {
        User user = userRepository.getUserByLogin(userLogin);

        if (!user.getCrushes().contains(crushLogin)) {
            throw new RuntimeException("Usuário não é seu paquera");
        }

        user.getCrushes().remove(crushLogin);
    }

    /**
     * Remove um inimigo da lista de inimigos do usuário.
     *
     * @param userLogin O login do usuário que está removendo o inimigo.
     * @param enemyLogin O login do inimigo a ser removido.
     * @throws RuntimeException Se o usuário não for inimigo.
     */
    public void removeEnemy(String userLogin, String enemyLogin) {
        User user = userRepository.getUserByLogin(userLogin);

        if (!user.getEnemies().contains(enemyLogin)) {
            throw new RuntimeException("Usuário não é seu inimigo");
        }

        user.getEnemies().remove(enemyLogin);
    }

    /**
     * Verifica se um usuário é paquera de outro.
     *
     * @param userLogin O login do usuário a verificar.
     * @param crushLogin O login da possível paquera.
     * @return true se o usuário for paquera, false caso contrário.
     */
    public boolean isCrush(String userLogin, String crushLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        if (user == null) {
            return false;
        }
        return user.getCrushes().contains(crushLogin);
    }

    /**
     * Verifica se um usuário é inimigo de outro.
     *
     * @param userLogin O login do usuário a verificar.
     * @param enemyLogin O login do possível inimigo.
     * @return true se o usuário for inimigo, false caso contrário.
     */
    public boolean isEnemy(String userLogin, String enemyLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        if (user == null) {
            return false;
        }
        return user.getEnemies().contains(enemyLogin);
    }

    /**
     * Obtém a lista de ídolos do usuário formatada.
     *
     * @param userLogin O login do usuário.
     * @return Uma string formatada com a lista de ídolos.
     */
    public String getIdolsFormatted(String userLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        return "{" + String.join(",", user.getIdols()) + "}";
    }

    /**
     * Obtém a lista de fãs do usuário formatada.
     *
     * @param userLogin O login do usuário.
     * @return Uma string formatada com a lista de fãs.
     */
    public String getFansFormatted(String userLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        return "{" + String.join(",", user.getFans()) + "}";
    }

    /**
     * Obtém a lista de paqueras do usuário formatada.
     *
     * @param userLogin O login do usuário.
     * @return Uma string formatada com a lista de paqueras.
     */
    public String getCrushesFormatted(String userLogin) {
        User user = userRepository.getUserByLogin(userLogin);
        return "{" + String.join(",", user.getCrushes()) + "}";
    }

    /**
     * Obtém a lista de inimigos do usuário formatada.
     *
     * @param userLogin O login do usuário.
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
     * @exception  NotFoundUserException Se o usuário inimigo não for encontrado.
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
