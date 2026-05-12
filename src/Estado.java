// Estados do automato lexico
public enum Estado {
    // Estado inicial.
    Q0,
    // ':' ou ':='
    Q1,
    // '<' ou '<='
    Q2,
    // '>' ou '>='
    Q3,
    // '=='
    Q4,
    // '!='
    Q5,
    // Comentario de linha
    Q6,
    // Cadeia de caracteres
    Q7,
    // Identificador
    Q8,
    // Palavra-chave
    Q9,
    // Numero inteiro
    Q10,
    // Ponto de numero real
    Q11,
    // Numero real
    Q12
}