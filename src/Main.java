import easyaccept.EasyAccept;

/**
 * Classe principal para execução dos testes do sistema Jackut.
 * Utiliza o framework EasyAccept para validar as funcionalidades implementadas
 * por meio de arquivos de teste contendo cenários de uso.
 * O metodo main carrega a classe Facade e executa os testes definidos nos arquivos
 * de texto fornecidos no array de argumentos.
 *
 * @author Felipe Lira
 */
public class Main {

    /**
     * Ponto de entrada do programa para execução dos testes.
     * Configura o EasyAccept para rodar os testes de aceitação listados nos arquivos de teste.
     *
     * @param args Argumentos da linha de comando (não utilizados neste programa).
     */
    public static void main(String[] args) {
        // Define os arquivos de teste e a classe principal Facade
        String[] arguments = {
                "br.ufal.ic.p2.jackut.facade.Facade", // Classe que implementa as funcionalidades
                "tests/us1_1.txt",  "tests/us1_2.txt",  // Arquivo de teste para User Story 1
                "tests/us2_1.txt", "tests/us2_2.txt",  // Arquivo de teste para User Story 2
                "tests/us3_1.txt",  "tests/us3_2.txt", // Arquivo de teste para User Story 3
                "tests/us4_1.txt", "tests/us4_2.txt", // Arquivo de teste para User Story 4
                "tests/us5_1.txt", "tests/us5_2.txt", // Arquivo de teste para User Story 5
                "tests/us6_1.txt", "tests/us6_2.txt", // Arquivo de teste para User Story 6
                "tests/us7_1.txt", "tests/us7_2.txt", // Arquivo de teste para User Story 7
                "tests/us8_1.txt", "tests/us8_2.txt", // Arquivo de teste para User Story 8
                "tests/us9_1.txt", "tests/us9_2.txt", // Arquivo de teste para User Story 9
        };

        // Executa os testes utilizando o EasyAccept
        EasyAccept.main(arguments);
    }
}