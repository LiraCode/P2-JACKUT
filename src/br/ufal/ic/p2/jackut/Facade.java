package br.ufal.ic.p2.jackut;

import java.io.*;
import java.util.*;

/**
 * Classe que gerencia as funcionalidades principais do sistema Jackut.
 * Oferece métodos para criar usuários, gerenciar sessões, enviar recados,
 * adicionar amigos e manipular dados do sistema.
 * Implementa a persistência dos dados usando serialização de objetos.
 *
 * @author Felipe Lira
 */
public class Facade {

    /**
     * Mensagem de erro para indicar que o usuário não foi encontrado.
     */
    public static final String USER_NOT_FOUND = "Usuário não cadastrado.";

    /**
     * Mapa para armazenar todos os usuários do sistema, identificado pelo login.
     */
    private Map<String, User> users;

    /**
     * Mapa para armazenar as sessões dos usuários, identificado pelo ID da sessão.
     */
    private Map<String, User> sessions;

    /**
     * Construtor padrão que inicializa os mapas de usuários e sessões.
     */
    public Facade() {
        this.users = new HashMap<>();
        this.sessions = new HashMap<>();
    }

    /**
     * Reseta o sistema, limpando todos os usuários e sessões.
     * Também exclui o arquivo de persistência, se existente.
     */
    public void zerarSistema() {
        try {
            users.clear();
            sessions.clear();
            File usersData = new File("usuarios.ser");
            if (!usersData.delete()) {
                System.err.println("Erro ao deletar o arquivo de usuários.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao zerar o sistema: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Carrega os dados do sistema a partir de um arquivo serializado.
     * Lê os dados dos usuários e recria o mapa de usuários.
     */
    public void carregarSistema() {
        try {
            File usersData = new File("usuarios.ser");

            if (usersData.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usersData))) {
                    users = (Map<String, User>) ois.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados do arquivo serializado: " + e.getMessage());
            throw new RuntimeException("Falha ao carregar o sistema.", e);
        }
    }

    /**
     * Salva os dados do sistema em um arquivo serializado.
     * Escreve o mapa de usuários no arquivo "usuarios.ser".
     */
    public void encerrarSistema() {
        try {
            File usersData = new File("usuarios.ser");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersData))) {
                oos.writeObject(users);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados no arquivo serializado: " + e.getMessage());
            throw new RuntimeException("Erro ao encerrar o sistema.", e);
        }
    }

    // Outras funcionalidades

    /**
     * Cria um novo usuário no sistema com login, senha e nome.
     *
     * @param login O login único do usuário.
     * @param senha A senha do usuário.
     * @param nome  O nome do usuário.
     * @throws RuntimeException Se o login já existe ou os dados são inválidos.
     */
    public void criarUsuario(String login, String senha, String nome) {
        try {
            validateLogin(login);
            validateSenha(senha);

            if (users.containsKey(login)) {
                throw new RuntimeException("Conta com esse nome já existe.");
            }

            if (nome == null) {
                nome = "";
            }
            User user = new User(nome, login, senha);
            users.put(login, user);
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());
            throw e;
        }
    }

    // Métodos de validação internos
    private void validateLogin(String login) {
        if (login == null || login.isBlank() || login.length() < 3) {
            throw new RuntimeException("Login inválido.");
        }
    }

    private void validateSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new RuntimeException("Senha inválida.");
        }
    }

    /**
     * Obtém um atributo de um usuário com base no login.
     *
     * @param login    O login do usuário.
     * @param atributo O atributo solicitado (nome, senha, login ou outro).
     * @return O valor do atributo solicitado.
     * @throws RuntimeException Se o usuário não for encontrado ou o atributo não existir.
     */
    public String getAtributoUsuario(String login, String atributo) {
        try {
            User user = users.get(login);
            if (user == null) {
                throw new RuntimeException(USER_NOT_FOUND);
            }

            return switch (atributo) {
                case "nome" -> user.getName();
                case "senha" -> user.getPassword();
                case "login" -> user.getLogin();
                default -> user.getAttributeExtra(atributo);
            };
        } catch (Exception e) {
            System.err.println("Erro ao obter atributo do usuário: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Inicia uma nova sessão para um usuário, verificando o login e a senha.
     *
     * @param login O login do usuário.
     * @param senha A senha do usuário.
     * @return O ID único da sessão.
     * @throws RuntimeException Se o login ou senha forem inválidos.
     */
    public String abrirSessao(String login, String senha) {
        try {
            User user = users.get(login);
            if (user != null && user.checkPassword(senha)) {
                String sessionId = generateSessionId(login);
                sessions.put(sessionId, user);
                return sessionId;
            }
            throw new RuntimeException("Login ou senha inválidos.");
        } catch (Exception e) {
            System.err.println("Erro ao abrir sessão: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Gera um ID único para uma sessão de usuário.
     * O ID é composto pelo login do usuário concatenado com um timestamp.
     *
     * @param login O login do usuário para o qual a sessão será criada.
     * @return Uma string representando o ID único da sessão.
     */
    private String generateSessionId(String login) {
        return login + "_" + System.currentTimeMillis();
    }

    /**
     * Edita o perfil de um usuário logado. Permite alterar atributos específicos,
     * como nome, senha ou outros atributos extras.
     *
     * @param id       O ID da sessão do usuário que está logado.
     * @param atributo O atributo a ser editado (ex.: "nome", "senha", "login", ou outro atributo extra).
     * @param valor    O novo valor do atributo.
     * @throws RuntimeException Se o usuário não estiver logado ou se o novo login já estiver em uso.
     */
    public void editarPerfil(String id, String atributo, String valor) {
        try {
            User user = sessions.get(id);
            if (user == null) {
                throw new RuntimeException(USER_NOT_FOUND);
            }

            switch (atributo) {
                case "nome" -> user.setName(valor);
                case "senha" -> user.setPassword(valor);
                case "login" -> {
                    if (users.containsKey(valor)) {
                        throw new RuntimeException("Login inválido.");
                    }
                    users.remove(user.getLogin());
                    user.setLogin(valor);
                    users.put(valor, user);
                }
                default -> user.setAttributeExtra(atributo, valor);
            }
        } catch (Exception e) {
            System.err.println("Erro ao editar perfil: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifica se dois usuários são amigos.
     *
     * @param login O login do usuário principal.
     * @param amigo O login do possível amigo.
     * @return {@code true} se os usuários são amigos; {@code false} caso contrário.
     * @throws RuntimeException Se o usuário não for encontrado.
     */
    public boolean ehAmigo(String login, String amigo) {
        try {
            User user = users.get(login);
            if (user == null) {
                throw new RuntimeException(USER_NOT_FOUND);
            }
            return user.getFriends().contains(amigo);
        } catch (Exception e) {
            System.err.println("Erro ao verificar amizade: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Adiciona um amigo ao usuário logado. Envia uma solicitação de amizade caso ainda não sejam amigos.
     *
     * @param id    O ID da sessão do usuário que está logado.
     * @param amigo O login do usuário que será adicionado como amigo.
     * @throws RuntimeException Se o usuário não estiver logado, o amigo não estiver cadastrado,
     *                          ou a solicitação não for válida.
     */
    public void adicionarAmigo(String id, String amigo) {
        try {
            User user = sessions.get(id);
            if (user == null) {
                throw new RuntimeException(USER_NOT_FOUND);
            }

            User friendUser = users.get(amigo);
            if (friendUser == null) {
                throw new RuntimeException(USER_NOT_FOUND);
            }

            if (user.getLogin().equals(amigo)) {
                throw new RuntimeException("Usuário não pode adicionar a si mesmo como amigo.");
            }

            if (user.getFriends().contains(amigo)) {
                throw new RuntimeException("Usuário já está adicionado como amigo.");
            }

            if (friendUser.getFriendSolicitation().contains(user.getLogin())) {
                throw new RuntimeException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
            }

            if (user.getFriendSolicitation().contains(friendUser.getLogin())) {
                user.getFriends().add(friendUser.getLogin());
                friendUser.getFriends().add(user.getLogin());
                user.getFriendSolicitation().remove(friendUser.getLogin());
            } else {
                friendUser.addFriendSolicitation(user.getLogin());
            }
        } catch (Exception e) {
            System.err.println("Erro ao adicionar amigo: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtém a lista de amigos de um usuário no formato de texto.
     *
     * @param login O login do usuário cuja lista de amigos será obtida.
     * @return Uma string representando a lista de amigos no formato: "{amigo1,amigo2,...}".
     * @throws RuntimeException Se o usuário não for encontrado.
     */
    public String getAmigos(String login) {
        try {
            User user = users.get(login);
            if (user == null) {
                throw new RuntimeException(USER_NOT_FOUND);
            }
            return "{" + String.join(",", user.getFriends()) + "}";
        } catch (Exception e) {
            System.err.println("Erro ao obter amigos: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Envia um recado de um usuário logado para outro usuário cadastrado.
     *
     * @param id           O ID da sessão do usuário que está enviando o recado.
     * @param destinatario O login do usuário que receberá o recado.
     * @param mensagem     O conteúdo da mensagem.
     * @throws RuntimeException Se o usuário ou destinatário não estiverem cadastrados
     *                          ou se tentar enviar recado a si mesmo.
     */
    public void enviarRecado(String id, String destinatario, String mensagem) {
        try {
            User sender = sessions.get(id);
            if (sender == null) {
                throw new RuntimeException("Usuário não cadastrado.");
            }

            User recipient = users.get(destinatario);
            if (recipient == null) {
                throw new RuntimeException("Usuário não cadastrado.");
            }

            if (sender.getLogin().equals(destinatario)) {
                throw new RuntimeException("Usuário não pode enviar recado para si mesmo.");
            }

            Message recado = new Message(sender.getLogin(), mensagem, destinatario);
            recipient.incomingMessage(recado);
        } catch (Exception e) {
            System.err.println("Erro ao enviar recado: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Lê o próximo recado da fila de mensagens de um usuário logado.
     *
     * @param id O ID da sessão do usuário que está lendo o recado.
     * @return O conteúdo do recado mais recente na fila.
     * @throws RuntimeException Se o usuário não estiver logado ou não houver recados disponíveis.
     */
    public String lerRecado(String id) {
        try {
            User user = sessions.get(id);
            if (user == null) {
                throw new RuntimeException("Usuário não cadastrado.");
            }

            Message recado = user.getMessages().poll();
            if (recado == null) {
                throw new RuntimeException("Não há recados.");
            }
            return recado.getMensagem();
        } catch (Exception e) {
            System.err.println("Erro ao ler recado: " + e.getMessage());
            throw e;
        }
    }
}