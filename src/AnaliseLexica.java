public class AnaliseLexica {
    public LeitorArquivo ldat;

    public AnaliseLexica(String nome){
        ldat = new LeitorArquivo(nome);
    }

    private void erroLexico(String msg) {
        System.err.println("Erro lexico na linha " + ldat.getLinhaAtual() + ": " + msg);
        System.exit(1);
    }

    public Token proxToken(){
        Estado estadoAtual = Estado.Q0;
        StringBuilder lexema = new StringBuilder();

        while (true) {
            int caractere = ldat.lerProximoCaractere();

            switch (estadoAtual) {
                case Q0:
                    if (caractere == -1) {
                        return null;
                    }

                    char c = (char) caractere;

                    if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                        break;
                    }

                    switch (c) {
                        case '+': return new Token("+", TipoToken.OpAritSoma);
                        case '-': return new Token("-", TipoToken.OpAritSub);
                        case '*': return new Token("*", TipoToken.OpAritMult);
                        case '/': return new Token("/", TipoToken.OpAritDiv);
                        case '(': return new Token("(", TipoToken.AbrePar);
                        case ')': return new Token(")", TipoToken.FechaPar);

                        case ':':
                            lexema.append(c);
                            estadoAtual = Estado.Q1;
                            break;
                        case '<':
                            lexema.append(c);
                            estadoAtual = Estado.Q2;
                            break;
                        case '>':
                            lexema.append(c);
                            estadoAtual = Estado.Q3;
                            break;
                        case '=':
                            lexema.append(c);
                            estadoAtual = Estado.Q4;
                            break;
                        case '!':
                            lexema.append(c);
                            estadoAtual = Estado.Q5;
                            break;
                        case '#':
                            estadoAtual = Estado.Q6;
                            break;
                        case '"':
                            estadoAtual = Estado.Q7;
                            break;
                    
                        default:
                                if (c >= 'a' && c <= 'z') {
                                    lexema.append(c);
                                    estadoAtual = Estado.Q8;
                                } else if (c >= 'A' && c <= 'Z') {
                                    lexema.append(c);
                                    estadoAtual = Estado.Q9;
                                } else if (c >= '0' && c <= '9') {
                                    lexema.append(c);
                                    estadoAtual = Estado.Q10;
                                } else {
                                    erroLexico("caractere inesperado '" + c + "'");
                                }
                                break;
                        }
                        break;

                    case Q1:

                        break;

                    case Q2:

                        break;

                    case Q3:

                        break;

                    case Q4:

                        break;
                    case Q5:

                        break;
                    case Q6:

                        break;
                    case Q7:

                        break;
                    case Q8:

                        break;

                    case Q9:

                        break;

                    case Q10:

                        break;

                    case Q11:

                        break;

                    case Q12:

                        break;
                    }

        }
    }
}