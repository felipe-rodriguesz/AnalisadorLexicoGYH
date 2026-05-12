import java.util.HashMap;

// Implementa o automato do analisador lexico
public class AnaliseLexica {
    // Leitura de caracteres com controle de linha
    private LeitorArquivo ldat;
    // Tabela de palavras reservadas da linguagem
    private HashMap<String, TipoToken> palavrasChave;

    public AnaliseLexica(String nome) {
        ldat = new LeitorArquivo(nome);
        palavrasChave = new HashMap<>();
        // Mapeamento de palavras-chave para tipos de token
        palavrasChave.put("DEC", TipoToken.PCDec);
        palavrasChave.put("PROG", TipoToken.PCProg);
        palavrasChave.put("INT", TipoToken.PCInt);
        palavrasChave.put("REAL", TipoToken.PCReal);
        palavrasChave.put("LER", TipoToken.PCLer);
        palavrasChave.put("IMPRIMIR", TipoToken.PCImprimir);
        palavrasChave.put("SE", TipoToken.PCSe);
        palavrasChave.put("ENTAO", TipoToken.PCEntao);
        palavrasChave.put("SENAO", TipoToken.PCSenao);
        palavrasChave.put("ENQTO", TipoToken.PCEnqto);
        palavrasChave.put("INI", TipoToken.PCIni);
        palavrasChave.put("FIM", TipoToken.PCFim);
        palavrasChave.put("E", TipoToken.OpBoolE);
        palavrasChave.put("OU", TipoToken.OpBoolOu);
    }

    // Exibe erro lexico e encerra a execucao
    private void erroLexico(String msg) {
        System.err.println("Erro lexico na linha " + ldat.getLinhaAtual() + ": " + msg);
        System.exit(1);
    }

    // Processa o automato e retorna o proximo token
    public Token proxToken() {
        Estado estadoAtual = Estado.Q0;
        StringBuilder lexema = new StringBuilder();

        while (true) {
            int caractere = ldat.lerProximoCaractere();

            // Cada estado trata um grupo de lexemas
            switch (estadoAtual) {
                case Q0:
                    // Estado inicial: ignora espacos e identifica inicio de token
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
                    // Reconhece ':' ou ':='
                    if (caractere != -1 && (char) caractere == '=') {
                        return new Token(":=", TipoToken.Atrib);
                    } else {
                        if (caractere != -1) {
                            ldat.devolverCaractere(caractere);
                        }
                        return new Token(":", TipoToken.Delim);
                    }

                case Q2:
                    // Reconhece '<' ou '<='
                    if (caractere != -1 && (char) caractere == '=') {
                        return new Token("<=", TipoToken.OpRelMenorIgual);
                    } else {
                        if (caractere != -1) {
                            ldat.devolverCaractere(caractere);
                        }
                        return new Token("<", TipoToken.OpRelMenor);
                    }

                case Q3:
                    // Reconhece '>' ou '>='
                    if (caractere != -1 && (char) caractere == '=') {
                        return new Token(">=", TipoToken.OpRelMaiorIgual);
                    } else {
                        if (caractere != -1) {
                            ldat.devolverCaractere(caractere);
                        }
                        return new Token(">", TipoToken.OpRelMaior);
                    }

                case Q4:
                    // Reconhece o operador '=='
                    if (caractere != -1 && (char) caractere == '=') {
                        return new Token("==", TipoToken.OpRelIgual);
                    } else {
                        erroLexico("esperado '=' apos '=' para formar '=='");
                    }
                    break;

                case Q5:
                    // Reconhece o operador '!='
                    if (caractere != -1 && (char) caractere == '=') {
                        return new Token("!=", TipoToken.OpRelDif);
                    } else {
                        erroLexico("esperado '=' apos '!' para formar '!='");
                    }
                    break;

                case Q6:
                    // Consome comentario de linha iniciado por '#'
                    if (caractere == -1 || (char) caractere == '\n') {
                        estadoAtual = Estado.Q0;
                    }
                    break;

                case Q7:
                    // Le cadeia entre aspas
                    if (caractere == -1) {
                        erroLexico("cadeia de caracteres nao fechada");
                    } else if ((char) caractere == '"') {
                        return new Token(lexema.toString(), TipoToken.Cadeia);
                    } else {
                        lexema.append((char) caractere);
                    }
                    break;

                case Q8:
                    // Le identificadores em minusculo
                    if (caractere != -1) {
                        char ch = (char) caractere;
                        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
                            lexema.append(ch);
                        } else {
                            ldat.devolverCaractere(caractere);
                            return new Token(lexema.toString(), TipoToken.Var);
                        }
                    } else {
                        return new Token(lexema.toString(), TipoToken.Var);
                    }
                    break;

                case Q9:
                    // Le palavras-chave em maiusculo
                    if (caractere != -1) {
                        char ch = (char) caractere;
                        if (ch >= 'A' && ch <= 'Z') {
                            lexema.append(ch);
                        } else {
                            ldat.devolverCaractere(caractere);
                            String palavra = lexema.toString();
                            TipoToken tipo = palavrasChave.get(palavra);
                            if (tipo != null) {
                                return new Token(palavra, tipo);
                            } else {
                                erroLexico("palavra nao reconhecida '" + palavra + "'");
                            }
                        }
                    } else {
                        String palavra = lexema.toString();
                        TipoToken tipo = palavrasChave.get(palavra);
                        if (tipo != null) {
                            return new Token(palavra, tipo);
                        } else {
                            erroLexico("palavra nao reconhecida '" + palavra + "'");
                        }
                    }
                    break;

                case Q10:
                    // Le numero inteiro ou inicio de real
                    if (caractere != -1) {
                        char ch = (char) caractere;
                        if (ch >= '0' && ch <= '9') {
                            lexema.append(ch);
                        } else if (ch == '.') {
                            lexema.append(ch);
                            estadoAtual = Estado.Q11;
                        } else {
                            ldat.devolverCaractere(caractere);
                            return new Token(lexema.toString(), TipoToken.NumInt);
                        }
                    } else {
                        return new Token(lexema.toString(), TipoToken.NumInt);
                    }
                    break;

                case Q11:
                    // Garante digito apos ponto decimal
                    if (caractere != -1) {
                        char ch = (char) caractere;
                        if (ch >= '0' && ch <= '9') {
                            lexema.append(ch);
                            estadoAtual = Estado.Q12;
                        } else {
                            erroLexico("esperado digito apos '.' no numero real");
                        }
                    } else {
                        erroLexico("numero real incompleto");
                    }
                    break;

                case Q12:
                    // Finaliza leitura de numero real
                    if (caractere != -1) {
                        char ch = (char) caractere;
                        if (ch >= '0' && ch <= '9') {
                            lexema.append(ch);
                        } else {
                            ldat.devolverCaractere(caractere);
                            return new Token(lexema.toString(), TipoToken.NumReal);
                        }
                    } else {
                        return new Token(lexema.toString(), TipoToken.NumReal);
                    }
                    break;
            }
        }
    }
}