package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.repositories.UserRepository;
import br.ufal.ic.p2.jackut.exceptions.*;

/**
 * Service class responsible for managing different types of relationships between users in the Jackut social network.
 * This class handles idol-fan relationships, crushes, and enemy relationships.
 */
public class RelationshipService {

    /**
     * The authentication service used to validate sessions and retrieve user information.
     */
    private final AuthService sessionManager;

    /**
     * The user repository used to access and modify user data.
     */
    private final UserRepository userManager;

    /**
     * Constructs a new RelationshipService with the specified repositories.
     *
     * @param userRepository The repository for accessing user data
     * @param authService The service for authentication and session management
     */
    public RelationshipService(UserRepository userRepository, AuthService authService) {
        this.userManager = userRepository;
        this.sessionManager = authService;
    }

    /**
     * Adds another user as an idol for the current user.
     * The current user becomes a fan of the idol.
     *
     * @param session The session ID of the current user
     * @param loginIdol The login of the user to be added as an idol
     * @throws NotFoundUserException If either user is not found
     * @throws IdolAlreadyAdded If the user is already an idol
     * @throws FanOfItself If the user tries to become a fan of themselves
     * @throws EnemyException If the idol has the current user as an enemy
     */
    public void adicionarIdolo(String session, String loginIdol) {
        User fan = sessionManager.getUserFromSession(session);
        if (fan == null) {
            throw new NotFoundUserException();
        }

        User idol = userManager.getUserByLogin(loginIdol);
        if (idol == null) {
            throw new NotFoundUserException("Usuário não cadastrado.");
        }

        if (fan.idols.contains(loginIdol)) {
            throw new IdolAlreadyAdded();
        }

        if (loginIdol.equals(fan.getLogin())) {
            throw new FanOfItself();
        }

        if (idol.getEnemies().contains(fan.getLogin())) {
            throw new EnemyException("Função inválida: " + idol.getName() + " é seu inimigo.");
        }

        fan.idols.add(loginIdol);
        idol.fans.add(fan.getLogin());
    }

    /**
     * Checks if a user is a fan of another user.
     *
     * @param loginUser The login of the potential fan
     * @param loginIdol The login of the potential idol
     * @return true if the user is a fan of the idol, false otherwise
     */
    public boolean ehFa(String loginUser, String loginIdol) {
        User user = userManager.getUserByLogin(loginUser);
        if (user == null || user.idols == null) return false;
        return user.idols.contains(loginIdol);
    }

    /**
     * Gets a formatted string of fans for a specific user.
     *
     * @param login The login of the user
     * @return A formatted string containing the logins of all fans
     */
    public String getFas(String login) {
        User user = userManager.getUserByLogin(login);
        return "{" + String.join(",", user.fans) + "}";
    }

    /**
     * Checks if the current user has a crush on another user.
     *
     * @param session The session ID of the current user
     * @param paquera The login of the potential crush
     * @return true if the user has a crush on the specified user, false otherwise
     */
    public boolean ehPaquera(String session, String paquera) {
        User user = sessionManager.getUserFromSession(session);
        return user.getCrushes().contains(paquera);
    }

    /**
     * Adds another user as a crush for the current user.
     * If both users have a crush on each other, a system message is sent to both.
     *
     * @param session The session ID of the current user
     * @param paqueraLogin The login of the user to be added as a crush
     * @throws NotFoundUserException If either user is not found
     * @throws CrushAlreadyAdded If the user is already a crush
     * @throws CrushOfItself If the user tries to have a crush on themselves
     * @throws EnemyException If the crush has the current user as an enemy
     */
    public void adicionarPaquera(String session, String paqueraLogin) {
        User user = sessionManager.getUserFromSession(session);
        if (user == null) {
            throw new NotFoundUserException();
        }

        User paquera = userManager.getUserByLogin(paqueraLogin);
        if (paquera == null) {
            throw new NotFoundUserException("Usuário não cadastrado.");
        }

        if (user.getCrushes().contains(paqueraLogin)) {
            throw new CrushAlreadyAdded();
        }

        if (paqueraLogin.equals(user.getLogin())) {
            throw new CrushOfItself("Usuário não pode ser paquera de si mesmo.");
        }

        if (paquera.getEnemies().contains(user.getLogin())) {
            throw new EnemyException("Função inválida: " + paquera.getName() + " é seu inimigo.");
        }

        user.getCrushes().add(paqueraLogin);

        // Verificar se é paquera mútua
        if (paquera.getCrushes().contains(user.getLogin())) {
            String recadoJackutDefault = "%s é seu paquera - Recado do Jackut.";
            String recadoJackutUser = String.format(recadoJackutDefault, paquera.getName());
            String recadoJackutPaquera = String.format(recadoJackutDefault, user.getName());

            Recado systemMessage = new Recado("jackut", recadoJackutUser, user.getLogin());
            Recado systemMessagePaquera = new Recado("jackut", recadoJackutPaquera, paquera.getLogin());

            user.addMessage(systemMessage);
            paquera.addMessage(systemMessagePaquera);
        }
    }

    /**
     * Gets a formatted string of crushes for the current user.
     *
     * @param session The session ID of the current user
     * @return A formatted string containing the logins of all crushes
     */
    public String getPaqueras(String session) {
        User user = sessionManager.getUserFromSession(session);
        return "{" + String.join(",", user.getCrushes()) + "}";
    }

    /**
     * Adds another user as an enemy for the current user.
     * An enemy cannot add the current user as a friend, idol, or crush.
     *
     * @param session The session ID of the current user
     * @param loginInimigo The login of the user to be added as an enemy
     * @throws NotFoundUserException If either user is not found
     * @throws EnemyAlreadyAdded If the user is already an enemy
     * @throws EnemyOfItself If the user tries to become an enemy of themselves
     */
    public void adicionarInimigo(String session, String loginInimigo) {
        User user = sessionManager.getUserFromSession(session);
        if (user == null) {
            throw new NotFoundUserException();
        }

        User enemy = userManager.getUserByLogin(loginInimigo);
        if (enemy == null) {
            throw new NotFoundUserException("Usuário não cadastrado.");
        }

        if (user.getEnemies().contains(loginInimigo)) {
            throw new EnemyAlreadyAdded("Usuário já está adicionado como inimigo.");
        }

        if (loginInimigo.equals(user.getLogin())) {
            throw new EnemyOfItself("Usuário não pode ser inimigo de si mesmo.");
        }

        user.getEnemies().add(loginInimigo);
    }

    /**
     * Gets a formatted string of enemies for the current user.
     *
     * @param session The session ID of the current user
     * @return A formatted string containing the logins of all enemies
     */
    public String getInimigos(String session)  {
        User user = sessionManager.getUserFromSession(session);
        return "{" + String.join(",", user.getEnemies()) + "}";
    }

    /**
     * Checks if the current user has another user as an enemy.
     *
     * @param session The session ID of the current user
     * @param loginInimigo The login of the potential enemy
     * @return true if the user has the specified user as an enemy, false otherwise
     */
    public boolean ehInimigo(String session, String loginInimigo) {
        User user = sessionManager.getUserFromSession(session);
        return user.getEnemies().contains(loginInimigo);
    }

    /**
     * Removes an idol from the current user's list of idols.
     * The current user is also removed from the idol's list of fans.
     *
     * @param session The session ID of the current user
     * @param loginIdol The login of the idol to be removed
     * @throws RuntimeException If the user is not an idol of the current user
     */
    public void removerIdolo(String session, String loginIdol)  {
        User fan = sessionManager.getUserFromSession(session);
        User idol = userManager.getUserByLogin(loginIdol);

        if (!fan.idols.contains(loginIdol)) {
            throw new RuntimeException("Usuário não é seu ídolo");
        }

        fan.idols.remove(loginIdol);
        idol.fans.remove(fan.getLogin());
    }

    /**
     * Removes a crush from the current user's list of crushes.
     *
     * @param session The session ID of the current user
     * @param loginPaquera The login of the crush to be removed
     * @throws RuntimeException If the user is not a crush of the current user
     */
    public void removerPaquera(String session, String loginPaquera)  {
        User user = sessionManager.getUserFromSession(session);

        if (!user.getCrushes().contains(loginPaquera))
            throw new RuntimeException("Usuário não é seu paquera");

        user.getCrushes().remove(loginPaquera);
    }

    /**
     * Removes an enemy from the current user's list of enemies.
     *
     * @param session The session ID of the current user
     * @param loginInimigo The login of the enemy to be removed
     * @throws RuntimeException If the user is not an enemy of the current user
     */
    public void removerInimigo(String session, String loginInimigo) {
        User user = sessionManager.getUserFromSession(session);

        if (!user.getEnemies().contains(loginInimigo))
            throw new RuntimeException("Usuário não é seu inimigo");

        user.getEnemies().remove(loginInimigo);
    }

    /**
     * Gets a formatted string of idols for the current user.
     *
     * @param session The session ID of the current user
     * @return A formatted string containing the logins of all idols
     */
    public String getIdolos(String session) {
        User user = sessionManager.getUserFromSession(session);
        return "{" + String.join(",", user.idols) + "}";
    }
}