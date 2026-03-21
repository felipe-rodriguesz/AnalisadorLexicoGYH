import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LeitorArquivo {
    private InputStream is;
    private int pushback = -1;
    private int linhaAtual = 1;

    public LeitorArquivo(String nome) {
        try {
            is = new FileInputStream(nome);
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo nao encontrado: " + nome);
            System.exit(1);
        }
    }

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

    public void devolverCaractere(int c) {
        if (c == '\n') {
            linhaAtual--;
        }
        pushback = c;
    }

    public int getLinhaAtual() {
        return linhaAtual;
    }
}