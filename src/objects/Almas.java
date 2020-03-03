package objects;

import common.World;
import java.util.ArrayList;
import objects.Objeto;

public class Almas
extends Objeto {
    private ArrayList<World.Duo<Integer, Integer>> _mobs;

    public Almas(int id, int cantidad, int modelo, int pos, String strStats) {
        this._id = id;
        this._modelo = World.getObjModelo(modelo);
        this._idObjModelo = this._modelo.getID();
        this._cantidad = 1;
        this._posicion = -1;
        this._mobs = new ArrayList();
        this.convertirStringAStats(strStats);
    }

    @Override
    public void convertirStringAStats(String mounstros) {
        String[] split;
        String[] arrstring = split = mounstros.split(",");
        int n = split.length;
        for (int i = 0; i < n; ++i) {
            String s = arrstring[i];
            try {
                int mob = Integer.parseInt(s.split("#")[3], 16);
                int nivel = Integer.parseInt(s.split("#")[1], 16);
                this._mobs.add(new World.Duo<Integer, Integer>(mob, nivel));
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    @Override
    public String convertirStatsAString() {
        String stats = "";
        boolean primero = true;
        for (World.Duo<Integer, Integer> coupl : this._mobs) {
            if (!primero) {
                stats = String.valueOf(stats) + ",";
            }
            try {
                stats = String.valueOf(stats) + "26f#" + Integer.toHexString((Integer)coupl._segundo) + "#0#" + Integer.toHexString((Integer)coupl._primero);
            }
            catch (Exception e) {
                continue;
            }
            primero = false;
        }
        return stats;
    }

    public String analizarGrupo() {
        String string = "";
        boolean primero = true;
        for (World.Duo<Integer, Integer> mob : this._mobs) {
            if (!primero) {
                string = String.valueOf(string) + ";";
            }
            string = String.valueOf(string) + mob._primero + "," + mob._segundo + "," + mob._segundo;
            primero = false;
        }
        return string;
    }
}

