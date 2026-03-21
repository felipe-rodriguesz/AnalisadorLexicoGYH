public class Token {
    public TipoToken padrao;
    public String lexema;

    @Override
    public String toString(){
        return "<"+lexema+","+padrao+">";
    }

    public Token(String lexema, TipoToken padrao){
        this.lexema = lexema;
        this.padrao = padrao;
    }
}