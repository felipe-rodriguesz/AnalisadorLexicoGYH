import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

// Encapsula a leitura de caracteres do arquivo fonte
public class LeitorArquivo {
    // Fluxo do arquivo de entrada
    private InputStream is;
    // Buffer para devolver um caractere lido
    private int pushback = -1;
    // Linha atual para mensagens de erro
    private int linhaAtual = 1;

    public LeitorArquivo(String nome) {
        try {
            is = new FileInputStream(nome);
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo nao encontrado: " + nome);
            System.exit(1);
        }
    }

    // Le o proximo caractere considerando pushback
    public int lerProximoCaractere() {
        int c;

        if (pushback != -1) {
            c = pushback;
            pushback = -1;
        } else {
            try {
                c = is.read();
            } catch (IOException ex) {
                c = -1;
            }
        }

        if (c == '\n') {
            linhaAtual++;
        }

        return c;
    }

    // Devolve um caractere para ser relido
    public void devolverCaractere(int c) {
        if (c == '\n') {
            linhaAtual--;
        }
        pushback = c;
    }

    // Retorna a linha atual da leitura
    public int getLinhaAtual() {
        return linhaAtual;
    }
}