import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Inicia análise léxica
        AnaliseLexica lex = new AnaliseLexica("programa.gyh");
        List<Token> listaTokens = new ArrayList<>();
        
        // Lê todos os tokens e guarda na lista
        Token t = lex.proxToken();
        while(t != null){
            listaTokens.add(t);
            t = lex.proxToken();
        }
        
        System.out.println("Análise léxica concluída. Foram lidos " + listaTokens.size() + " tokens.");
        
        // Chama o Sintático passando a lista
        // AnalisadorSintatico sintatico = new AnalisadorSintatico(listaTokens);
        // sintatico.analisar();
    }
}