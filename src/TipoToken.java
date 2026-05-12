// Lista de tipos de token reconhecidos
public enum TipoToken {
    // Palavras-chave
    PCDec, PCProg, PCInt, PCReal, PCLer, PCImprimir, PCSe, PCEntao, PCSenao, PCEnqto, PCIni, PCFim,
    // Operadores aritmeticos
    OpAritMult, OpAritDiv, OpAritSoma, OpAritSub,
    // Operadores relacionais
    OpRelMenor, OpRelMenorIgual, OpRelMaior, OpRelMaiorIgual, OpRelIgual, OpRelDif,
    // Operadores booleanos
    OpBoolE, OpBoolOu,
    // Delimitadores e atribuicao
    Delim, Atrib,
    // Parenteses
    AbrePar, FechaPar,
    // Literais e identificadores
    Var, NumInt, NumReal, Cadeia
}