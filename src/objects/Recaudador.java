package objects;

import common.CryptManager;
import common.LesGuardians;
import common.Pathfinding;
import common.SocketManager;
import common.Mundo;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import objects.Combate;
import objects.Gremio;
import objects.Mapa;
import objects.Objeto;
import objects.Personaje;

public class Recaudador {
	private int _id;
	private short _mapaID;
	private short _celdaID;
	private byte _orientacion;
	private int _gremioID = 0;
	private String _nombre_1 = "";
	private String _nombre_2 = "";
	private byte _estadoPelea = 0;
	private short _peleaID = (short) -1;
	private Map<Integer, Objeto> _objetos = new TreeMap<Integer, Objeto>();
	private long _kamas = 0L;
	private long _xp = 0L;
	private boolean _enRecolecta = false;
	private int _tiempoTurno = 45000;
	private Combate _pelea;

	public Recaudador(int ID, short mapa, short celdaID, byte orientacion, int gremioID, String N1, String N2,
			String items, long kamas, long xp) {
		_id = ID;
		_mapaID = mapa;
		_celdaID = celdaID;
		_orientacion = orientacion;
		_gremioID = gremioID;
		_nombre_1 = N1;
		_nombre_2 = N2;
		for (String item : items.split("\\|")) {
			String[] infos = item.split(":");
			int id = Integer.parseInt(infos[0]);
			Objeto obj = Mundo.getObjeto(id);
			if (item.equals("") || (obj = Mundo.getObjeto(id = Integer.parseInt((infos = item.split(":"))[0]))) == null)
				continue;
			_objetos.put(obj.getID(), obj);
		}
		_xp = xp;
		_kamas = kamas;
		_pelea = null;
	}

	public long getKamas() {
		return _kamas;
	}

	public int getPodsActuales() {
		int pods = 0;
		for (Map.Entry<Integer, Objeto> entry : _objetos.entrySet()) {
			Objeto obj = entry.getValue();
			if (obj == null)
				continue;
			pods += obj.getModelo().getPeso() * obj.getCantidad();
		}
		return pods;
	}

	public void setKamas(long kamas) {
		_kamas = kamas;
	}

	public long getXp() {
		return _xp;
	}

	public void setXp(long xp) {
		_xp = xp;
	}

	public void addXp(long xp) {
		_xp += xp;
	}

	public Map<Integer, Objeto> getObjetos() {
		return _objetos;
	}

	public void borrarObjeto(int id) {
		_objetos.remove(id);
	}

	public boolean tieneObjeto(int id) {
		return _objetos.get(id) != null;
	}

	public void descontarTiempoTurno(int tiempo) {
		_tiempoTurno -= tiempo;
	}

	public void setTiempoTurno(int tiempo) {
		_tiempoTurno = tiempo;
	}

	public int getTiempoTurno() {
		return _tiempoTurno;
	}

	public static String enviarGMDeRecaudador(Mapa mapa) {
		String packet = "GM|";
		boolean primero = true;
		Map<Integer, Recaudador> todosRecaudadores = Mundo.getTodosRecaudadores();
		for (Map.Entry<Integer, Recaudador> recau : todosRecaudadores.entrySet()) {
			Gremio G;
			Recaudador recaudador = recau.getValue();
			if (recau.getValue()._estadoPelea > 0 || recaudador._mapaID != mapa.getID())
				continue;
			if (!primero) {
				packet = String.valueOf(packet) + "|";
			}
			if ((G = Mundo.getGremio(recaudador._gremioID)) == null) {
				recaudador.borrarRecaudador(recaudador.getID());
				continue;
			}
			packet = String.valueOf(packet) + "+";
			packet = String.valueOf(packet) + recaudador._celdaID + ";";
			packet = String.valueOf(packet) + recaudador._orientacion + ";";
			packet = String.valueOf(packet) + "0;";
			packet = String.valueOf(packet) + recaudador._id + ";";
			packet = String.valueOf(packet) + recaudador._nombre_1 + "," + recaudador._nombre_2 + ";";
			packet = String.valueOf(packet) + "-6;";
			packet = String.valueOf(packet) + "6000^100;";
			packet = String.valueOf(packet) + G.getNivel() + ";";
			packet = String.valueOf(packet) + G.getNombre() + ";" + G.getEmblema();
			primero = false;
		}
		return packet;
	}

	public int getGremioID() {
		return _gremioID;
	}

	public void borrarRecaudador(int idRecaudador) {
		for (Objeto obj : _objetos.values()) {
			Mundo.eliminarObjeto(obj._id);
		}
		Mundo.borrarRecaudador(idRecaudador);
	}

	public void borrarRecauPorRecolecta(int idRecaudador, Personaje perso) {
		perso.addKamas(_kamas);
		try {
			for (Objeto obj : _objetos.values()) {
				if (obj == null)
					continue;
				int id = 0;
				int cant = 0;
				id = obj.getID();
				cant = obj.getCantidad();
				if (id <= 0 || cant <= 0)
					continue;
				if (perso.addObjetoSimilar(obj, true, -1)) {
					Mundo.eliminarObjeto(id);
					continue;
				}
				perso.addObjetoPut(obj);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, obj);
			}
		} catch (NumberFormatException numberFormatException) {
			// empty catch block
		}
		Mundo.borrarRecaudador(idRecaudador);
	}

	public byte getEstadoPelea() {
		return _estadoPelea;
	}

	public void setEstadoPelea(byte estado) {
		_estadoPelea = estado;
	}

	public int getID() {
		return _id;
	}

	public short getCeldalID() {
		return _celdaID;
	}

	public void setPeleaID(short ID) {
		_peleaID = ID;
	}

	public void setPelea(Combate pelea) {
		_pelea = pelea;
	}

	public Combate getPelea() {
		return _pelea;
	}

	public short getPeleaID() {
		return _peleaID;
	}

	public short getMapaID() {
		return _mapaID;
	}

	public String getN1() {
		return _nombre_1;
	}

	public String getN2() {
		return _nombre_2;
	}

	public static String analizarRecaudadores(int gremioID) {
		String packet = "+";
		boolean primero = false;
		Map<Integer, Recaudador> todosRecaudadores = Mundo.getTodosRecaudadores();
		for (Map.Entry<Integer, Recaudador> recau : todosRecaudadores.entrySet()) {
			Recaudador recaudador = recau.getValue();
			if (recaudador.getGremioID() != gremioID)
				continue;
			Mapa mapa = Mundo.getMapa(recaudador.getMapaID());
			if (primero) {
				packet = String.valueOf(packet) + "|";
			}
			packet = String.valueOf(packet) + Integer.toString(recaudador.getID(), 36) + ";" + recaudador.getN1() + ","
					+ recaudador.getN2() + ";";
			packet = String.valueOf(packet) + Integer.toString(mapa.getID(), 36) + "," + mapa.getX() + "," + mapa.getY()
					+ ";";
			packet = String.valueOf(packet) + recaudador.getEstadoPelea() + ";";
			if (recaudador.getEstadoPelea() == 1) {
				packet = mapa.getPelea(recaudador.getPeleaID()) == null ? String.valueOf(packet) + "45000;"
						: String.valueOf(packet) + recaudador.getTiempoTurno() + ";";
				packet = String.valueOf(packet) + "45000;";
				packet = String.valueOf(packet) + "7;";
				packet = String.valueOf(packet) + "?,?,";
			} else {
				packet = String.valueOf(packet) + "0;";
				packet = String.valueOf(packet) + "45000;";
				packet = String.valueOf(packet) + "7;";
				packet = String.valueOf(packet) + "?,?,";
			}
			packet = String.valueOf(packet) + "1,2,3,4,5";
			primero = true;
		}
		if (packet.length() == 1) {
			packet = null;
		}
		return packet;
	}

	public static int getIDGremioPorMapaID(int id) {
		for (Map.Entry<Integer, Recaudador> recau : Mundo.getTodosRecaudadores().entrySet()) {
			if (recau.getValue().getMapaID() != id)
				continue;
			return recau.getValue().getGremioID();
		}
		return 0;
	}

	public static void analizarAtaque(Personaje perso, int gremioID) {
		for (Map.Entry<Integer, Recaudador> recau : Mundo.getTodosRecaudadores().entrySet()) {
			Recaudador recaudador = recau.getValue();
			if (recaudador._estadoPelea <= 0 || recaudador._gremioID != gremioID)
				continue;
			SocketManager.ENVIAR_gITp_INFO_ATACANTES_RECAUDADOR(perso,
					Recaudador.atacantesAlGremio(recaudador._id, recaudador._mapaID, recaudador._peleaID));
		}
	}

	public static void analizarDefensa(Personaje perso, int gremioID) {
		for (Map.Entry<Integer, Recaudador> recau : Mundo.getTodosRecaudadores().entrySet()) {
			Recaudador recaudador = recau.getValue();
			if (recaudador._estadoPelea <= 0 || recaudador._gremioID != gremioID)
				continue;
			SocketManager.ENVIAR_gITP_INFO_DEFENSORES_RECAUDADOR(perso,
					Recaudador.defensoresDelGremio(recaudador._id, recaudador._mapaID, recaudador._peleaID));
		}
	}

	public static String atacantesAlGremio(int id, short mapaID, short peleaID) {
		String str = "+";
		str = String.valueOf(str) + Integer.toString(id, 36);
		for (Map.Entry<Short, Combate> pelea : Mundo.getMapa(mapaID).getPeleas().entrySet()) {
			if (pelea.getValue().getID() != peleaID)
				continue;
			for (Combate.Luchador luchador : pelea.getValue().luchadoresDeEquipo(1)) {
				if (luchador.getPersonaje() == null)
					continue;
				str = String.valueOf(str) + "|";
				str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getID(), 36) + ";";
				str = String.valueOf(str) + luchador.getPersonaje().getNombre() + ";";
				str = String.valueOf(str) + luchador.getPersonaje().getNivel() + ";";
				str = String.valueOf(str) + "0;";
			}
		}
		return str;
	}

	public static String defensoresDelGremio(int id, short mapaID, int peleaID) {
		String str = "+";
		String stra = "-";
		str = String.valueOf(str) + Integer.toString(id, 36);
		for (Map.Entry<Short, Combate> pelea : Mundo.getMapa(mapaID).getPeleas().entrySet()) {
			if (pelea.getValue().getID() != peleaID)
				continue;
			for (Combate.Luchador luchador : pelea.getValue().luchadoresDeEquipo(2)) {
				if (luchador.getPersonaje() == null)
					continue;
				str = String.valueOf(str) + "|";
				str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getID(), 36) + ";";
				str = String.valueOf(str) + luchador.getPersonaje().getNombre() + ";";
				str = String.valueOf(str) + luchador.getPersonaje().getGfxID() + ";";
				str = String.valueOf(str) + luchador.getPersonaje().getNivel() + ";";
				str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getColor1(), 36) + ";";
				str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getColor2(), 36) + ";";
				str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getColor3(), 36) + ";";
				str = String.valueOf(str) + "0;";
			}
			stra = str.substring(1);
			stra = "-" + stra;
			pelea.getValue().setListaDefensores(stra);
		}
		return str;
	}

	public String getListaObjRecaudador() {
		String objetos = "";
		for (Objeto obj : _objetos.values()) {
			objetos = String.valueOf(objetos) + "O" + obj.stringObjetoConGui\u00f1o();
		}
		if (_kamas != 0L) {
			objetos = String.valueOf(objetos) + "G" + _kamas;
		}
		return objetos;
	}

	public String stringListaObjetosBD() {
		String objetos = "";
		for (Objeto obj : _objetos.values()) {
			objetos = String.valueOf(objetos) + obj._id + "|";
		}
		return objetos;
	}

	public void borrarDesdeRecaudador(Personaje perso, int idObjeto, int cantidad) {
		Objeto RecauObj = Mundo.getObjeto(idObjeto);
		Objeto PersoObj = perso.getObjSimilarInventario(RecauObj);
		int nuevaCant = RecauObj.getCantidad() - cantidad;
		if (PersoObj == null) {
			if (nuevaCant <= 0) {
				borrarObjeto(idObjeto);
				perso.addObjetoPut(RecauObj);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, RecauObj);
				String str = "O-" + idObjeto;
				SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
			} else {
				PersoObj = Objeto.clonarObjeto(RecauObj, cantidad);
				Mundo.addObjeto(PersoObj, true);
				RecauObj.setCantidad(nuevaCant);
				perso.addObjetoPut(PersoObj);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, PersoObj);
				String str = "O+" + RecauObj.getID() + "|" + RecauObj.getCantidad() + "|" + RecauObj.getModelo().getID()
						+ "|" + RecauObj.convertirStatsAString();
				SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
			}
		} else if (nuevaCant <= 0) {
			borrarObjeto(idObjeto);
			Mundo.eliminarObjeto(RecauObj.getID());
			PersoObj.setCantidad(PersoObj.getCantidad() + RecauObj.getCantidad());
			SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, PersoObj);
			String str = "O-" + idObjeto;
			SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
		} else {
			RecauObj.setCantidad(nuevaCant);
			PersoObj.setCantidad(PersoObj.getCantidad() + cantidad);
			SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, PersoObj);
			String str = "O+" + RecauObj.getID() + "|" + RecauObj.getCantidad() + "|" + RecauObj.getModelo().getID()
					+ "|" + RecauObj.convertirStatsAString();
			SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
		}
		SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
	}

	public String stringObjetos() {
		String str = "";
		boolean esPrimero = true;
		for (Objeto obj : _objetos.values()) {
			if (!esPrimero) {
				str = String.valueOf(str) + ";";
			}
			str = String.valueOf(str) + obj.getModelo().getID() + "," + obj.getCantidad();
			esPrimero = false;
		}
		return str;
	}

	public void addObjeto(Objeto nuevoObj) {
		_objetos.put(nuevoObj.getID(), nuevoObj);
	}

	public void setEnRecolecta(boolean Exchange) {
		_enRecolecta = Exchange;
	}

	public boolean getEnRecolecta() {
		return _enRecolecta;
	}

	public int getOrientacion() {
		return _orientacion;
	}

	public Mapa getMapa() {
		return Mundo.getMapa(_mapaID);
	}

	public void moverPerco() {
		Mapa mapa = Mundo.getMapa(_mapaID);
		short celdadestino = Pathfinding.celdaMovPerco(mapa, _celdaID);
		ArrayList<Mapa.Celda> celdas = Pathfinding.pathMasCortoEntreDosCeldas(mapa, _celdaID, celdadestino, 0);
		String pathstr = "";
		short tempCeldaID = _celdaID;
		byte tempDireccion = 0;
		for (Mapa.Celda celda : celdas) {
			char dir = Pathfinding.getDirEntreDosCeldas(tempCeldaID, celda.getID(), mapa, true);
			if (dir == '\000')
				return;
			if (dir != '\000') {
				if (celdas.indexOf(celda) != 0)
					pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
				pathstr = String.valueOf(pathstr) + dir;
				switch (dir) {
				case 'a':
					tempDireccion = 0;
					break;
				case 'b':
					tempDireccion = 1;
					break;
				case 'c':
					tempDireccion = 2;
					break;
				case 'd':
					tempDireccion = 3;
					break;
				case 'e':
					tempDireccion = 4;
					break;
				case 'f':
					tempDireccion = 5;
					break;
				case 'g':
					tempDireccion = 6;
					break;
				case 'h':
					tempDireccion = 7;
					break;
				}
			}
			tempCeldaID = celda.getID();
		}
		if (tempCeldaID != _celdaID)
			pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
		try {
			Thread.sleep(100L);
		} catch (InterruptedException interruptedException) {
		}
		String path = pathstr;
		if (path.equals("")) {
			if (LesGuardians.MOSTRAR_RECIBIDOS)
				System.out.println("Fallo de desplazamiento: camino vacio");
			return;
		}
		AtomicReference<String> pathRef = new AtomicReference<String>(path);
		int resultado = Pathfinding.numeroMovimientos(getMapa(), _celdaID, pathRef, null);
		if (resultado == 0)
			return;
		if (resultado != -1000 && resultado < 0)
			resultado = -resultado;
		path = pathRef.get();
		if (resultado == -1000)
			path = String.valueOf(CryptManager.getValorHashPorNumero(getOrientacion()))
					+ CryptManager.celdaIDACodigo(getCeldalID());
		SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(getMapa(), "0", 1,
				(new StringBuilder(String.valueOf(_id))).toString(),
				"a" + CryptManager.celdaIDACodigo(_celdaID) + path);
		_celdaID = celdadestino;
		_orientacion = tempDireccion;
	}
}