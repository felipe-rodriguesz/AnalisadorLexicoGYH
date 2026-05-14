import java.util.ArrayList;
import java.util.List;

public class GeradorCodigoIntermediario {
    private List<Quadrupla> quadruplas;
    private int contadorTemporarias;

    public GeradorCodigoIntermediario() {
        this.quadruplas = new ArrayList<>();
        this.contadorTemporarias = 1;
    }

    public String novaTemporaria() {
        return "T" + (contadorTemporarias++);
    }

    public void emitir(String op, String a1, String a2, String res) {
        quadruplas.add(new Quadrupla(op, a1, a2, res));
    }

    public List<Quadrupla> getQuadruplas() {
        return quadruplas;
    }

    public void imprimir() {
        System.out.println("\n--- CÓDIGO INTERMEDIÁRIO (QUÁDRUPLAS) ---");
        for (Quadrupla q : quadruplas) {
            System.out.println(q);
        }
    }
}