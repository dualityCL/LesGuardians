package objects;

import common.LesGuardians;
import java.util.ArrayList;
import objects.Acción;

public class Tutorial {
	private int _id;
	private ArrayList<Acción> _recompensa = new ArrayList<Acción>(4);
	private Acción _inicio;
	private Acción _final;

	public Tutorial(int id, String recompensa, String inicio, String fin) {
		_id = id;
		try {
			String[] c;
			String[] b;
			for (String str : recompensa.split("\\$")) {
				if (str.isEmpty()) {
					_recompensa.add(null);
					continue;
				}
				String[] a = str.split("@");
				if (a.length >= 2) {
					_recompensa.add(new Acción(Integer.parseInt(a[0]), a[1], ""));
					continue;
				}
				_recompensa.add(new Acción(Integer.parseInt(a[0]), "", ""));
			}
			_inicio = inicio.isEmpty() ? null
					: ((b = inicio.split("@")).length >= 2 ? new Acción(Integer.parseInt(b[0]), b[1], "")
							: new Acción(Integer.parseInt(b[0]), "", ""));
			_final = fin.isEmpty() ? null
					: ((c = fin.split("@")).length >= 2 ? new Acción(Integer.parseInt(c[0]), c[1], "")
							: new Acción(Integer.parseInt(c[0]), "", ""));
		} catch (Exception e) {
			System.out.println("Ocorreu um erro ao carregar o tutorial. " + id);
			LesGuardians.cerrarServer();
		}
	}

	public ArrayList<Acción> getRecompensa() {
		return _recompensa;
	}

	public Acción getInicio() {
		return _inicio;
	}

	public Acción getFin() {
		return _final;
	}

	public int getID() {
		return _id;
	}
}