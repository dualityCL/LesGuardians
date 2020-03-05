package objects;

import common.Constantes;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.Mundo;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Casa;
import objects.Cuenta;
import objects.Objeto;
import objects.Personaje;

public class Cofre {
	private int _id;
	private int _casaID;
	private short _mapaID;
	private int _celdaID;
	private Map<Integer, Objeto> _objetos = new TreeMap<Integer, Objeto>();
	private long _kamas;
	private String _clave;
	private int _due\u00f1oID;

	public Cofre(int id, int casaID, short mapaID, int celdaID, String objetos, long kamas, String clave, int due\u00f1oID) {
		_id = id;
		_casaID = casaID;
		_mapaID = mapaID;
		_celdaID = celdaID;
		for (String objeto : objetos.split("\\|")) {
			if (objeto.equals("")) {
				continue;
			}
			String[] infos = objeto.split(":");
			int idObjeto = Integer.parseInt(infos[0]);
			Objeto obj = Mundo.getObjeto(idObjeto);
			if (obj == null) {
				continue;
			}
			_objetos.put(obj.getID(), obj);
		}
		_kamas = kamas;
		_clave = clave;
		_due\u00f1oID = due\u00f1oID;
	}

	public int getID() {
		return _id;
	}

	public int getCasaPorID() {
		return _casaID;
	}

	public int getMapaID() {
		return _mapaID;
	}

	public int getCeldaID() {
		return _celdaID;
	}

	public Map<Integer, Objeto> getObjetos() {
		return _objetos;
	}

	public long getKamas() {
		return _kamas;
	}

	public void setKamas(long kamas) {
		_kamas = kamas;
	}

	public String getClave() {
		return _clave;
	}

	public void setClave(String clave) {
		_clave = clave;
	}

	public int getDue\u00f1oID() {
		return _due\u00f1oID;
	}

	public void setDue\u00f1oID(int due\u00f1oID) {
		_due\u00f1oID = due\u00f1oID;
	}

	public void bloquear(Personaje perso) {
		SocketManager.ENVIAR_K_CLAVE(perso, "CK1|8");
	}

	public static Cofre getCofrePorUbicacion(int mapaID, int celdaID) {
		for (Map.Entry<Integer, Cofre> cofres : Mundo.getCofres().entrySet()) {
			Cofre cofre = cofres.getValue();
			if (cofre.getMapaID() != mapaID || cofre.getCeldaID() != celdaID)
				continue;
			return cofre;
		}
		return null;
	}

	public static void codificarCofre(Personaje perso, String packet) {
		Cofre cofre = perso.getCofre();
		if (cofre == null) {
			return;
		}
		if (cofre.esSuCofre(perso, cofre)) {
			SQLManager.CODIFICAR_COFRE(perso, cofre, packet);
			cofre.setClave(packet);
			Cofre.cerrarVentanaCofre(perso);
		} else {
			Cofre.cerrarVentanaCofre(perso);
		}
		perso.setCofre(null);
	}

	public void chekeadoPor(Personaje perso) {
		if (perso.getPelea() != null || perso.getConversandoCon() != 0 || perso.getIntercambiandoCon() != 0
				|| perso.getHaciendoTrabajo() != null || perso.getIntercambio() != null) {
			return;
		}
		Cofre cofre = perso.getCofre();
		Casa casa = Mundo.getCasa(_casaID);
		if (cofre == null) {
			return;
		}
		if (cofre.getDue\u00f1oID() == perso.getCuentaID() || perso.getGremio() != null
				&& perso.getGremio().getID() == casa.getGremioID() && casa.tieneDerecho(Constantes.C_SINCODIGOGREMIO)) {
			Cofre.abrirCofre(perso, "-", true);
		} else {
			if (perso.getGremio() == null && casa.tieneDerecho(Constantes.C_ABRIRGREMIO)) {
				SocketManager.ENVIAR_cs_CHAT_MENSAJE(perso,
						"Este cofre esta abierto s\u00f3lo para los miembros del gremio.", LesGuardians.COR_MSG);
				return;
			}
			if (cofre.getDue\u00f1oID() > 0) {
				SocketManager.ENVIAR_K_CLAVE(perso, "CK0|8");
			} else {
				return;
			}
		}
	}

	public static void abrirCofre(Personaje perso, String packet, boolean esSuCofre) {
		Cofre cofre = perso.getCofre();
		if (cofre == null) {
			return;
		}
		if (packet.compareTo(cofre.getClave()) == 0 || esSuCofre) {
			SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(perso.getCuenta().getEntradaPersonaje().getOut(), 5, "");
			SocketManager.ENVIAR_EL_LISTA_OBJETOS_COFRE(perso, cofre);
			Cofre.cerrarVentanaCofre(perso);
		} else if (packet.compareTo(cofre.getClave()) != 0) {
			SocketManager.ENVIAR_K_CLAVE(perso, "KE");
			Cofre.cerrarVentanaCofre(perso);
			perso.setCofre(null);
		}
	}

	public static void cerrarVentanaCofre(Personaje perso) {
		SocketManager.ENVIAR_K_CLAVE(perso, "V");
	}

	public boolean esSuCofre(Personaje perso, Cofre cofre) {
		return cofre.getDue\u00f1oID() == perso.getCuentaID();
	}

	public static ArrayList<Cofre> getCofresPorCasa(Casa casa) {
		ArrayList<Cofre> cofres = new ArrayList<Cofre>();
		for (Map.Entry<Integer, Cofre> cofre : Mundo.getCofres().entrySet()) {
			if (cofre.getValue().getCasaPorID() != casa.getID())
				continue;
			cofres.add(cofre.getValue());
		}
		return cofres;
	}

	public String analizarCofre() {
		String packet = "";
		for (Objeto obj : _objetos.values()) {
			packet = String.valueOf(packet) + "O" + obj.stringObjetoConGui\u00f1o() + ";";
		}
		if (getKamas() != 0L) {
			packet = String.valueOf(packet) + "G" + getKamas();
		}
		return packet;
	}

	public void agregarAlCofre(int idObj, int cantidad, Personaje perso) {
		if (_objetos.size() >= 80) {
			SocketManager.ENVIAR_cs_CHAT_MENSAJE(perso,
					"Llegaste al m\u00e1ximo de objetos que puede soportar este cofre", LesGuardians.COR_MSG);
			return;
		}
		Objeto persoObj = Mundo.getObjeto(idObj);
		if (persoObj == null) {
			return;
		}
		if (!perso.tieneObjetoID(idObj)) {
			return;
		}
		String str = "";
		if (persoObj.getPosicion() != -1) {
			return;
		}
		Objeto cofreObj = objetoSimilarEnElCofre(persoObj);
		int nuevaCant = persoObj.getCantidad() - cantidad;
		if (cofreObj == null) {
			if (nuevaCant <= 0) {
				perso.borrarObjetoSinEliminar(idObj);
				_objetos.put(idObj, persoObj);
				str = "O+" + idObj + "|" + persoObj.getCantidad() + "|" + persoObj.getModelo().getID() + "|"
						+ persoObj.convertirStatsAString();
				SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(perso, idObj);
			} else {
				persoObj.setCantidad(nuevaCant);
				cofreObj = Objeto.clonarObjeto(persoObj, cantidad);
				Mundo.addObjeto(cofreObj, true);
				_objetos.put(cofreObj.getID(), cofreObj);
				str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|"
						+ cofreObj.convertirStatsAString();
				SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, persoObj);
			}
		} else if (nuevaCant <= 0) {
			perso.borrarObjetoSinEliminar(idObj);
			Mundo.eliminarObjeto(idObj);
			cofreObj.setCantidad(cofreObj.getCantidad() + persoObj.getCantidad());
			str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|"
					+ cofreObj.convertirStatsAString();
			SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(perso, idObj);
		} else {
			persoObj.setCantidad(nuevaCant);
			cofreObj.setCantidad(cofreObj.getCantidad() + cantidad);
			str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|"
					+ cofreObj.convertirStatsAString();
			SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, persoObj);
		}
		for (Personaje pj : perso.getMapa().getPersos()) {
			if (pj.getCofre() == null || _id != pj.getCofre().getID())
				continue;
			SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(pj, str);
		}
		SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
	}

	public void retirarDelCofre(int idObj, int cant, Personaje perso) {
		if (perso.getCofre().getID() != _id) {
			return;
		}
		Objeto cofreObj = Mundo.getObjeto(idObj);
		if (cofreObj == null) {
			return;
		}
		if (_objetos.get(idObj) == null) {
			SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, "O-" + idObj);
			return;
		}
		Objeto persoObj = perso.getObjSimilarInventario(cofreObj);
		String str = "";
		int nuevaCant = cofreObj.getCantidad() - cant;
		if (persoObj == null) {
			if (nuevaCant <= 0) {
				_objetos.remove(idObj);
				perso.getObjetos().put(idObj, cofreObj);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, cofreObj);
				str = "O-" + idObj;
			} else {
				persoObj = Objeto.clonarObjeto(cofreObj, cant);
				Mundo.addObjeto(persoObj, true);
				cofreObj.setCantidad(nuevaCant);
				perso.getObjetos().put(persoObj.getID(), persoObj);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, persoObj);
				str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|"
						+ cofreObj.convertirStatsAString();
			}
		} else if (nuevaCant <= 0) {
			_objetos.remove(cofreObj.getID());
			Mundo.eliminarObjeto(cofreObj.getID());
			persoObj.setCantidad(persoObj.getCantidad() + cofreObj.getCantidad());
			SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, persoObj);
			str = "O-" + idObj;
		} else {
			cofreObj.setCantidad(nuevaCant);
			persoObj.setCantidad(persoObj.getCantidad() + cant);
			SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, persoObj);
			str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|"
					+ cofreObj.convertirStatsAString();
		}
		for (Personaje pj : perso.getMapa().getPersos()) {
			if (pj.getCofre() == null || _id != pj.getCofre().getID())
				continue;
			SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(pj, str);
		}
		SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
	}

	private Objeto objetoSimilarEnElCofre(Objeto obj) {
		for (Objeto objeto : _objetos.values()) {
			if (objeto.getModelo().getTipo() == 85 || objeto.getModelo().getID() != obj.getModelo().getID()
					|| !objeto.getStats().sonStatsIguales(obj.getStats()))
				continue;
			return objeto;
		}
		return null;
	}

	public String analizarObjetoCofreABD() {
		String str = "";
		for (Objeto objeto : _objetos.values()) {
			str = String.valueOf(str) + objeto.getID() + "|";
		}
		return str;
	}

	public void limpiarCofre() {
		for (Map.Entry<Integer, Objeto> obj : getObjetos().entrySet()) {
			Mundo.eliminarObjeto(obj.getKey());
		}
		getObjetos().clear();
	}

	public void moverCofreABanco(Cuenta cuenta) {
		for (Map.Entry<Integer, Objeto> obj : getObjetos().entrySet()) {
			cuenta.getObjetosBanco().put(obj.getKey(), obj.getValue());
		}
		getObjetos().clear();
	}
}