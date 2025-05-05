package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.models.User;
import br.ufal.ic.p2.jackut.models.Recado;
import br.ufal.ic.p2.jackut.repositories.UserRepository;
import br.ufal.ic.p2.jackut.exceptions.*;

public class RelationshipService {

    private final AuthService sessionManager;
    private final UserRepository userManager;

    public RelationshipService(UserRepository userRepository, AuthService authService) {
        this.userManager = userRepository;
        this.sessionManager = authService;
    }

    public void adicionarIdolo(String session, String loginIdol) throws NotFoundUserException {
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

    public boolean ehFa(String loginUser, String loginIdol) {
        User user = userManager.getUserByLogin(loginUser);
        if (user == null || user.idols == null) return false;
        return user.idols.contains(loginIdol);
    }

    public String getFas(String login) {
        User user = userManager.getUserByLogin(login);
        return "{" + String.join(",", user.fans) + "}";
    }

    public boolean ehPaquera(String session, String paquera) throws NotFoundUserException {
        User user = sessionManager.getUserFromSession(session);
        return user.getCrushes().contains(paquera);
    }

    public void adicionarPaquera(String session, String paqueraLogin) throws NotFoundUserException {
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

    public String getPaqueras(String session) throws NotFoundUserException {
        User user = sessionManager.getUserFromSession(session);
        return "{" + String.join(",", user.getCrushes()) + "}";
    }

    public void adicionarInimigo(String session, String loginInimigo) throws NotFoundUserException {
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

    public String getInimigos(String session) throws NotFoundUserException {
        User user = sessionManager.getUserFromSession(session);
        return "{" + String.join(",", user.getEnemies()) + "}";
    }

    public boolean ehInimigo(String session, String loginInimigo) throws NotFoundUserException {
        User user = sessionManager.getUserFromSession(session);
        return user.getEnemies().contains(loginInimigo);
    }

    public void removerIdolo(String session, String loginIdol) throws NotFoundUserException {
        User fan = sessionManager.getUserFromSession(session);
        User idol = userManager.getUserByLogin(loginIdol);

        if (!fan.idols.contains(loginIdol)) {
            throw new RuntimeException("Usuário não é seu ídolo");
        }

        fan.idols.remove(loginIdol);
        idol.fans.remove(fan.getLogin());
    }

    public void removerPaquera(String session, String loginPaquera) throws NotFoundUserException {
        User user = sessionManager.getUserFromSession(session);

        if (!user.getCrushes().contains(loginPaquera))
            throw new RuntimeException("Usuário não é seu paquera");

        user.getCrushes().remove(loginPaquera);
    }

    public void removerInimigo(String session, String loginInimigo) throws NotFoundUserException {
        User user = sessionManager.getUserFromSession(session);

        if (!user.getEnemies().contains(loginInimigo))
            throw new RuntimeException("Usuário não é seu inimigo");

        user.getEnemies().remove(loginInimigo);
    }

    public String getIdolos(String session) throws NotFoundUserException {
        User user = sessionManager.getUserFromSession(session);
        return "{" + String.join(",", user.idols) + "}";
    }
}