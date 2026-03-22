public class AnalisadorLexico {

    public static void main(String[] args) {
        AnaliseLexica lex=new AnaliseLexica("programa.gyh");
        
        Token t=lex.proxToken();
        
        while(t!=null){
            System.out.println(t.toString());
            t=lex.proxToken();
        }
        
        System.out.println("\nAnalise lexica concluida com sucesso.");
        
    }
}