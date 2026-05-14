import java.util.List;

public class AnalisadorSintatico {
    private List<Token> tokens;
    private int pos;
    private Token tokenAtual;
    private GeradorCodigoIntermediario gerador;

    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
        this.tokenAtual = tokens.get(0);
        this.gerador = new GeradorCodigoIntermediario();
    }

    // Método auxiliar para avançar na lista de tokens
    private void avancar() {
    pos++;
    if (pos < tokens.size()) {
        tokenAtual = tokens.get(pos);
    } else {
        // Isso evita o erro de "IndexOutOfBounds" e o loop infinito no final
        tokenAtual = new Token("$", null); 
    }
}
    // Verifica se o token atual é do tipo esperado
    private void match(TipoToken tipo) {
        if (tokenAtual.padrao == tipo) {
            avancar();
        } else {
            throw new RuntimeException("\n\nErro Sintático: Esperado " + tipo + " mas encontrado " + tokenAtual.padrao + "\n\n");
        }
    }

    // Inicia a análise
    public void analisar() {
        programa();
        System.out.println("Análise Sintática e Geração de Código concluídas!");
        gerador.imprimir();
    }

    // Programa -> ':' 'DEC' ListaDeclaracoes 'PROG' ListaComandos
    private void programa() {
        match(TipoToken.Delim); // ':'
        match(TipoToken.PCDec);
        listaDeclaracoes();
        match(TipoToken.Delim);
        match(TipoToken.PCProg);
        listaComandos();
    }

    // ListaDeclaracoes -> Declaracao ListaDeclaracoes | ε
    private void listaDeclaracoes() {
        if (tokenAtual.padrao == TipoToken.Var) {
            declaracao();
            listaDeclaracoes();
        }
        // ε: não faz nada e retorna (Follow é PCProg)
    }

    // Declaracao -> VARIAVEL ':' TipoVar
    private void declaracao() {
        String nomeVar = tokenAtual.lexema; // Captura o nome (ex: "x")
        match(TipoToken.Var);
        
        match(TipoToken.Delim); // ':'
        
        TipoToken tipo = tokenAtual.padrao; // Captura o tipo (PCInt ou PCReal)
        tipoVar();
        
        // Salva na Tabela de Símbolos (Singleton)
        TabelaDeSimbolos.getTable().tabela.put(nomeVar, tipo);
        System.out.println("Tabela de Símbolos: Variável '" + nomeVar + "' do tipo " + tipo + " registrada.");
    }

    // TipoVar -> 'INT' | 'REAL'
    private void tipoVar() {
        if (tokenAtual.padrao == TipoToken.PCInt) {
            match(TipoToken.PCInt);
        } else {
            match(TipoToken.PCReal);
        }
    }

    // ExpressaoAritmetica -> TermoAritmetico Termo1
    private String expressaoAritmetica() {
        String esq = termoAritmetico();
        return termo1(esq);
    }

    // Termo1 -> Complemento1 TermoAritmetico Termo1 | ε
    private String termo1(String esq) {
        if (tokenAtual.padrao == TipoToken.OpAritSoma || tokenAtual.padrao == TipoToken.OpAritSub) {
            String op = tokenAtual.lexema;
            avancar();
            String dir = termoAritmetico();
            String temp = gerador.novaTemporaria();
            gerador.emitir(op, esq, dir, temp);
            return termo1(temp);
        }
        return esq;
    }

    // TermoAritmetico -> FatorAritmetico Termo2
    private String termoAritmetico() {
        String esq = fatorAritmetico();
        return termo2(esq);
    }

    // Termo2 -> Complemento2 FatorAritmetico Termo2 | ε
    private String termo2(String esq) {
        if (tokenAtual.padrao == TipoToken.OpAritMult || tokenAtual.padrao == TipoToken.OpAritDiv) {
            String op = tokenAtual.lexema;
            avancar();
            String dir = fatorAritmetico();
            String temp = gerador.novaTemporaria();
            gerador.emitir(op, esq, dir, temp);
            return termo2(temp);
        }
        return esq;
    }

    // FatorAritmetico -> NUMINT | NUMREAL | VARIAVEL | '(' ExpressaoAritmetica ')'
    private String fatorAritmetico() {
        if (tokenAtual.padrao == TipoToken.NumInt || tokenAtual.padrao == TipoToken.NumReal || tokenAtual.padrao == TipoToken.Var) {
            String lexema = tokenAtual.lexema;
            avancar();
            return lexema;
        } else if (tokenAtual.padrao == TipoToken.AbrePar) {
            // Este bloco só deve ser executado se o parêntese for interno à conta,
            // ex: (a + b) * c. 
            // No caso do SE (3 > 2), o '(' já foi consumido pelo comandoCondicao.
            match(TipoToken.AbrePar);
            String res = expressaoAritmetica();
            match(TipoToken.FechaPar);
            return res;
        }
        throw new RuntimeException("Erro: Fator inválido " + tokenAtual.lexema);
    }

    // ListaComandos -> Comando ListaComandos | ε
    private void listaComandos() {
        // First de Comando: VARIAVEL, LER, IMPRIMIR, SE, ENQTO, INI
        if (tokenAtual.padrao == TipoToken.Var || tokenAtual.padrao == TipoToken.PCLer || 
            tokenAtual.padrao == TipoToken.PCImprimir || tokenAtual.padrao == TipoToken.PCSe || 
            tokenAtual.padrao == TipoToken.PCEnqto || tokenAtual.padrao == TipoToken.PCIni) {
            comando();
            listaComandos();
        }
    }

    private void comando() {
        switch (tokenAtual.padrao) {
            case Var: comandoAtribuicao(); break;
            case PCLer: comandoEntrada(); break;
            case PCImprimir: comandoSaida(); break;
            case PCSe: comandoCondicao(); break;
            case PCEnqto: comandoRepeticao(); break;
            case PCIni: subAlgoritmo(); break;
            default: throw new RuntimeException("Comando inválido: " + tokenAtual.lexema);
        }
    }

    private void comandoAtribuicao() {
        String destino = tokenAtual.lexema;
        match(TipoToken.Var);
        match(TipoToken.Atrib);
        String origem = expressaoAritmetica();
        gerador.emitir(":=", origem, null, destino);
    }

    private void comandoEntrada() {
        match(TipoToken.PCLer);
        String var = tokenAtual.lexema;
        match(TipoToken.Var);
        gerador.emitir("LER", null, null, var);
    }

    private void comandoSaida() {
    match(TipoToken.PCImprimir);
    if (tokenAtual.padrao == TipoToken.Var || tokenAtual.padrao == TipoToken.Cadeia) {
        String valor = tokenAtual.lexema;
        gerador.emitir("IMPRIMIR", null, null, valor);
        
        avancar(); // <--- ESSENCIAL: Consome a Var/Cadeia
    } else {
        throw new RuntimeException("Erro: IMPRIMIR espera variável ou cadeia.");
    }
}

    private void comandoCondicao() {
        match(TipoToken.PCSe);
        
        boolean usouParenteses = false;
        if (tokenAtual.padrao == TipoToken.AbrePar) {
            match(TipoToken.AbrePar);
            usouParenteses = true;
        }
        
        expressaoRelacional();
        
        if (usouParenteses) {
            match(TipoToken.FechaPar);
        }
        
        match(TipoToken.PCEntao);
        comando();
        
        if (tokenAtual.padrao == TipoToken.PCSenao) {
            match(TipoToken.PCSenao);
            comando();
        }
    }

    private void comandoRepeticao() {
        match(TipoToken.PCEnqto);
        
        boolean usouParenteses = false;
        if (tokenAtual.padrao == TipoToken.AbrePar) {
            match(TipoToken.AbrePar);
            usouParenteses = true;
        }
        
        expressaoRelacional();
        
        if (usouParenteses) {
            match(TipoToken.FechaPar);
        }
        
        comando();
    }

    private void subAlgoritmo() {
        match(TipoToken.PCIni);
        listaComandos();
        match(TipoToken.PCFim);
    }

    // ExpressaoRelacional -> TermoRelacional Termo3
private String expressaoRelacional() {
    String esq = termoRelacional();
    return termo3(esq);
}

// Termo3 -> OperadorBooleano TermoRelacional | ε
private String termo3(String esq) {
    if (tokenAtual.padrao == TipoToken.OpBoolE || tokenAtual.padrao == TipoToken.OpBoolOu) {
        String op = tokenAtual.lexema;
        avancar();
        String dir = termoRelacional();
        String temp = gerador.novaTemporaria();
        gerador.emitir(op, esq, dir, temp);
        return termo3(temp);
    }
    return esq;
}

// TermoRelacional -> ExpressaoAritmetica ComplementoRelacional
private String termoRelacional() {
    String esq = expressaoAritmetica();
    return complementoRelacional(esq);
}

// ComplementoRelacional -> OpRel... ExpressaoAritmetica | ε
private String complementoRelacional(String esq) {
    if (isOpRel()) {
        String op = tokenAtual.lexema;
        avancar(); // Consome o operador relacional (<, >, <=, etc)
        String dir = expressaoAritmetica();
        String temp = gerador.novaTemporaria();
        gerador.emitir(op, esq, dir, temp);
        return temp;
    }
    return esq; // Caso ε
}

// Ajudante para o ComplementoRelacional identificar os terminais
private boolean isOpRel() {
    TipoToken t = tokenAtual.padrao;
    return t == TipoToken.OpRelMenor || t == TipoToken.OpRelMaior || 
           t == TipoToken.OpRelMenorIgual || t == TipoToken.OpRelMaiorIgual || 
           t == TipoToken.OpRelIgual || t == TipoToken.OpRelDif;
}
}