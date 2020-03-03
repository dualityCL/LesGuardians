package objects;

import common.LesGuardians;
import java.util.ArrayList;
import objects.Acao;

public class Tutorial {
    private int _id;
    private ArrayList<Acao> _recompensa = new ArrayList(4);
    private Acao _inicio;
    private Acao _final;

    public Tutorial(int id, String recompensa, String inicio, String fin) {
        this._id = id;
        try {
            String[] c;
            String[] b;
            for (String str : recompensa.split("\\$")) {
                if (str.isEmpty()) {
                    this._recompensa.add(null);
                    continue;
                }
                String[] a = str.split("@");
                if (a.length >= 2) {
                    this._recompensa.add(new Acao(Integer.parseInt(a[0]), a[1], ""));
                    continue;
                }
                this._recompensa.add(new Acao(Integer.parseInt(a[0]), "", ""));
            }
            this._inicio = inicio.isEmpty() ? null : ((b = inicio.split("@")).length >= 2 ? new Acao(Integer.parseInt(b[0]), b[1], "") : new Acao(Integer.parseInt(b[0]), "", ""));
            this._final = fin.isEmpty() ? null : ((c = fin.split("@")).length >= 2 ? new Acao(Integer.parseInt(c[0]), c[1], "") : new Acao(Integer.parseInt(c[0]), "", ""));
        }
        catch (Exception e) {
            System.out.println("Ocorreu um erro ao carregar o tutorial. " + id);
            LesGuardians.cerrarServer();
        }
    }

    public ArrayList<Acao> getRecompensa() {
        return this._recompensa;
    }

    public Acao getInicio() {
        return this._inicio;
    }

    public Acao getFin() {
        return this._final;
    }

    public int getID() {
        return this._id;
    }
}

