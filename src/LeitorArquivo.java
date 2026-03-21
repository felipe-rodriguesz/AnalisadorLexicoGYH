import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class LeitorArquivo {
    public InputStream is;

    public LeitorArquivo(String nome){
        try {
            is=new FileInputStream(nome);
        } catch (FileNotFoundException ex) {
            System.getLogger(LeitorArquivo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public int lerProximoCaractere(){
        int c = -1;

        try {
            c = is.read();
        } catch (IOException ex) {
            System.getLogger(LeitorArquivo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return c;
    }

}