import easyaccept.EasyAccept;

/**
 * Classe principal para execu��o dos testes do sistema Jackut.
 * Utiliza o framework EasyAccept para validar as funcionalidades implementadas
 * por meio de arquivos de teste contendo cen�rios de uso.
 * O m�todo main carrega a classe Facade e executa os testes definidos nos arquivos
 * de texto fornecidos no array de argumentos.
 *
 * @author Felipe Lira
 */
public class Main {

    /**
     * Ponto de entrada do programa para execu��o dos testes.
     * Configura o EasyAccept para rodar os testes de aceita��o listados nos arquivos de teste.
     *
     * @param args Argumentos da linha de comando (n�o utilizados neste programa).
     */
    public static void main(String[] args) {
        // Define os arquivos de teste e a classe principal Facade
        String[] arguments = {
                "br.ufal.ic.p2.jackut.Facade", // Classe que implementa as funcionalidades
                "tests/us1_1.txt",            // Arquivo de teste para User Story 1
                "tests/us2_1.txt",            // Arquivo de teste para User Story 2
                "tests/us3_1.txt",            // Arquivo de teste para User Story 3
                "tests/us4_1.txt",            // Arquivo de teste para User Story 4
        };

        // Executa os testes utilizando o EasyAccept
        EasyAccept.main(arguments);
    }
}