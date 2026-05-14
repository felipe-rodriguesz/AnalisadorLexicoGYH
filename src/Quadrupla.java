public class Quadrupla {
    public String operador;
    public String arg1;
    public String arg2;
    public String resultado;

    public Quadrupla(String operador, String arg1, String arg2, String resultado) {
        this.operador = operador;
        this.arg1 = (arg1 == null) ? "_" : arg1;
        this.arg2 = (arg2 == null) ? "_" : arg2;
        this.resultado = (resultado == null) ? "_" : resultado;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s, %s)", operador, arg1, arg2, resultado);
    }
}