import java.util.HashMap;

public class TabelaDeSimbolos {
    private static TabelaDeSimbolos instancia;
    public HashMap<String, TipoToken> tabela;

    private TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }
    public static synchronized TabelaDeSimbolos getTable() {
        if (instancia == null) {
            instancia = new TabelaDeSimbolos();
        }
        return instancia;
    }
    
}
