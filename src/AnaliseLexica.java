public class AnaliseLexica {
    public LeitorArquivo ldat;

    public AnaliseLexica(String nome){
        ldat = new LeitorArquivo(nome);
    }

    public Token proxToken(){
        int caractere = ldat.lerProximoCaractere();

        while (caractere >= 0) {
            char c = (char)caractere;
            switch (c) {
                case '+': return new Token("+", TipoToken.OpAritSoma);
                case '-': return new Token("-", TipoToken.OpAritSub);
                // Melhorar essa lógica
            }
            caractere = ldat.lerProximoCaractere();
        }
        return null;
    }
}