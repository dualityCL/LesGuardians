package common;

import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;

import java.util.ArrayList;

import objects.Conta;
import objects.Personagens;

public class Condiciones {
    public static boolean validaCondiciones(Conta compte, Personagens perso, String condiciones) {
        if (condiciones == null || condiciones.equals("") || condiciones.equalsIgnoreCase("EVENTO") || condiciones.contains("Pg") || condiciones.contains("Pj") || condiciones.contains("PJ")) {
            return true;
        }
        if (condiciones.contains("BI")) {
            return false;
        }
        Jep jep = new Jep();
        if (condiciones.contains("PO")) {
            condiciones = Condiciones.tieneObjetoModelo(condiciones, perso);
        }
        condiciones = condiciones.replace("&", "&&").replace("=", "==").replace("|", "||").replace("!", "!=");
        try {
            Personagens.Stats totalStas = perso.getTotalStats();
            jep.addVariable("CI", totalStas.getEfecto(126));
            jep.addVariable("CV", totalStas.getEfecto(125));
            jep.addVariable("CA", totalStas.getEfecto(119));
            jep.addVariable("CW", totalStas.getEfecto(124));
            jep.addVariable("CC", totalStas.getEfecto(123));
            jep.addVariable("CS", totalStas.getEfecto(118));
            jep.addVariable("Ci", perso.getBaseStats().getEfecto(126));
            jep.addVariable("Cs", perso.getBaseStats().getEfecto(118));
            jep.addVariable("Cv", perso.getBaseStats().getEfecto(125));
            jep.addVariable("Ca", perso.getBaseStats().getEfecto(119));
            jep.addVariable("Cw", perso.getBaseStats().getEfecto(124));
            jep.addVariable("Cc", perso.getBaseStats().getEfecto(123));
            jep.addVariable("Ps", perso.getAlineacion());
            jep.addVariable("Pa", perso.getNivelAlineacion());
            jep.addVariable("PP", perso.getNivelAlineacion());
            jep.addVariable("PL", perso.getNivel());
            jep.addVariable("PK", perso.getKamas());
            jep.addVariable("PG", perso.getClase(true));
            jep.addVariable("PS", perso.getSexo());
            jep.addVariable("PZ", true);
            jep.addVariable("esCasado", perso.getEsposo());
            jep.addVariable("siKamas", perso.getKamas());
            jep.addVariable("esKoliseo", perso.getEnKoliseo());
            jep.addVariable("MiS", perso.getID());
            jep.addVariable("VIP", compte.getVIP());
            jep.parse(condiciones);
            Object resultado = jep.evaluate();
            boolean ok = false;
            if (resultado != null) {
                ok = Boolean.valueOf(resultado.toString());
            }
            return ok;
        }
        catch (JepException e) {
            System.out.println("Un error ocurrio en Condicion Jugador: " + e.getMessage());
            System.out.println("La String Condiciones fue: " + condiciones);
            return true;
        }
    }

    public static String tieneObjetoModelo(String condiciones, Personagens perso) {
		String[] str = condiciones.replaceAll("[ ()]", "").split("[|&]");
		ArrayList<Integer> valores = new ArrayList<Integer>(str.length);
		for (String condicion : str) {
			if (!condicion.contains("PO"))
				continue;
			if (perso.tieneObjModeloNoEquip(Integer.parseInt(condicion.split("[=]")[1]), 1))
				valores.add(Integer.parseInt(condicion.split("[=]")[1]));
			else
				valores.add(-1);
		}
		for (int valor : valores) {
			condiciones = condiciones.replaceFirst("PO", valor + "");
		}
		return condiciones;
	}
}