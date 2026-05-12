// Estrutura para representar um token
public class Token {
    // Categoria do token
    public TipoToken padrao;
    // Texto original reconhecido
    public String lexema;

    @Override
    // Formato padrao de saida dos tokens
    public String toString(){
        return "<"+lexema+","+padrao+">";
    }

    // Construtor com lexema e tipo
    public Token(String lexema, TipoToken padrao){
        this.lexema = lexema;
        this.padrao = padrao;
    }
}