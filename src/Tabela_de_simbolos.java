import java.util.HashMap;

public class Tabela_de_simbolos {
    private static Tabela_de_simbolos instancia;
    public HashMap<String, Object> tabela;
            
    private Tabela_de_simbolos() {
        this.tabela = new HashMap<>();
    }
    public static synchronized Tabela_de_simbolos getTable() {
        if (instancia == null) {
            instancia = new Tabela_de_simbolos();
        }
        return instancia;
    }
    
}
