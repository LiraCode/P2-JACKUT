# Jackut - Sistema de Rede Social

Jackut é uma aplicação de rede social simples, projetada para gerenciar usuários, amigos e recados, com funcionalidades robustas e persistência de dados usando serialização em Java.

---

## Funcionalidades Principais

1. **Gerenciamento de Usuários**
    - Criação de contas com login, senha e nome.
    - Autenticação de usuários com controle de sessão.
    - Edição de perfis, permitindo modificar atributos como nome e senha.

2. **Gerenciamento de Amizades**
    - Adição de amigos com envio e aceitação de solicitações de amizade.
    - Lista de amigos e verificação de amizades existentes.

3. **Mensagens e Recados**
    - Envio de recados de um usuário para outro.
    - Leitura de recados em ordem de chegada (FIFO).

4. **Persistência de Dados**
    - Salvamento e carregamento dos dados do sistema usando arquivos serializados (`usuarios.ser`).

5. **Testes com EasyAccept**
    - Integração com o framework EasyAccept para execução de testes de aceitação.

---
## Etapas




***02-04-2025 -*** **Milestone 1 |  (Users Stories 1 a 4).**

---

## Estrutura do Projeto

```plaintext
br/ufal/ic/p2/jackut/
├── Facade.java           # Classe principal para operações do sistema
├── User.java             # Classe que representa os usuários
├── Recado.java           # Classe que encapsula recados enviados entre usuários
├── Main.java             # Classe principal para execução dos testes
├── tests/                # Pasta com arquivos de teste para o EasyAccept
└── usuarios.ser          # Arquivo serializado contendo os dados persistidos (gerado em runtime)
