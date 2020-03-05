package objects;

import common.CryptManager;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.Mundo;
import game.GameThread;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Dragopavo;
import objects.Mercadillo;
import objects.Objeto;
import objects.Personaje;
import realm.RealmThread;

public class Cuenta {
	private int _guid;
	private String _account;
	private String _pass;
	private String _apodo;
	private String _key;
	private String _ultimoIP = "";
	private String _question;
	private String _reponse;
	private boolean _baneado = false;
	private int _rango = 0;
	private int _vip = 0;
	private String _tempIP = "";
	private String _ultimaFechaConeccion = "";
	private GameThread _entradaPersonaje;
	private RealmThread _entradaGeneral;
	private Personaje _tempPerso;
	private long _kamasBanco = 0L;
	private Map<Integer, Objeto> _objetosEnBanco = new TreeMap<Integer, Objeto>();
	private ArrayList<Integer> _idsAmigos = new ArrayList<Integer>();
	private ArrayList<Integer> _idsEnemigos = new ArrayList<Integer>();
	private ArrayList<Dragopavo> _establo = new ArrayList<Dragopavo>();
	private boolean _muteado = false;
	public long _tiempoMuteado;
	public int _posicion = -1;
	private int _primeraVez;
	private Map<Integer, ArrayList<Mercadillo.ObjetoMercadillo>> _objMercadillos;
	private Map<Integer, Personaje> _personajes = new TreeMap<Integer, Personaje>();
	private int _regalo;
	public long _horaMuteada = 0L;

	public Cuenta(int ID, String nombre, String password, String apodo, String pregunta, String respuesta, int nivelGM,
			int vip, boolean baneado, String ultimaIP, String ultimaConeccion, String banco, long kamasBanco,
			String amigos, String enemigos, String establo, int primeravez, int regalo) {
		_guid = ID;
		_account = nombre;
		_pass = password;
		_apodo = apodo;
		_question = pregunta;
		_reponse = respuesta;
		_rango = nivelGM;
		_vip = vip;
		_baneado = baneado;
		_ultimoIP = ultimaIP;
		_ultimaFechaConeccion = ultimaConeccion;
		_kamasBanco = kamasBanco;
		_objMercadillos = Mundo.getMisObjetos(_guid);
		for (String item : banco.split("\\|")) {
			if (item.equals("")) {
				continue;
			}
			String[] infos = item.split(":");
			int id = Integer.parseInt(infos[0]);
			Objeto obj = Mundo.getObjeto(id);
			if (obj == null) {
				continue;
			}
			_objetosEnBanco.put(obj.getID(), obj);
		}
		for (String f : amigos.split(";")) {
			try {
				_idsAmigos.add(Integer.parseInt(f));
			} catch (Exception infos) {
			}
		}
		for (String f : enemigos.split(";")) {
			try {
				_idsEnemigos.add(Integer.parseInt(f));
			} catch (Exception infos) {
				// empty catch block
			}
		}
		for (String d : establo.split(";")) {
			try {
				Dragopavo DP = Mundo.getDragopavoPorID(Integer.parseInt(d));
				if (DP == null)
					continue;
				_establo.add(DP);
			} catch (Exception exception) {
				// empty catch block
			}
		}
		_primeraVez = primeravez;
		_regalo = regalo;
	}

	public ArrayList<Dragopavo> getEstablo() {
		return _establo;
	}

	public int getPrimeraVez() {
		return _primeraVez;
	}

	public void setKamasBanco(long i) {
		_kamasBanco = i;
	}

	public boolean estaMuteado() {
		return _muteado;
	}

	public int getRegalo() {
		return _regalo;
	}

	public void setRegalo() {
		_regalo = 0;
	}

	public void setRegalo(int regalo) {
		_regalo = regalo;
	}

	public void mutear(boolean b, int tiempo) {
		_muteado = b;
		String msg = "";
		msg = _muteado ? "Ha sido muteado" : "Ha sido desmuteado";
		SocketManager.ENVIAR_cs_CHAT_MENSAJE(_tempPerso, msg, LesGuardians.COR_MSG);
		if (tiempo == 0) {
			return;
		}
		_tiempoMuteado = tiempo * 1000;
		_horaMuteada = System.currentTimeMillis();
	}

	public String stringBancoObjetosBD() {
		String str = "";
		for (Map.Entry<Integer, Objeto> entry : _objetosEnBanco.entrySet()) {
			Objeto obj = entry.getValue();
			str = String.valueOf(str) + obj.getID() + "|";
		}
		return str;
	}

	public Map<Integer, Objeto> getObjetosBanco() {
		return _objetosEnBanco;
	}

	public long getKamasBanco() {
		return _kamasBanco;
	}

	public void setEntradaPersonaje(GameThread t) {
		_entradaPersonaje = t;
	}

	public void setTempIP(String ip) {
		_tempIP = ip;
	}

	public String getUltimaConeccion() {
		return _ultimaFechaConeccion;
	}

	public void setUltimoIP(String ultimoIP) {
		_ultimoIP = ultimoIP;
	}

	public void setUltimaConeccion(String ultimaConeccion) {
		_ultimaFechaConeccion = ultimaConeccion;
	}

	public GameThread getEntradaPersonaje() {
		return _entradaPersonaje;
	}

	public RealmThread getEntradaGeneral() {
		return _entradaGeneral;
	}

	public int getID() {
		return _guid;
	}

	public String getNombre() {
		return _account;
	}

	public String getContrase\u00f1a() {
		return _pass;
	}

	public String getApodo() {
		if (_apodo.isEmpty() || _apodo == "") {
			_apodo = _account;
		}
		return _apodo;
	}

	public String getClaveCliente() {
		return _key;
	}

	public void setClaveCliente(String aKey) {
		_key = aKey;
	}

	public String getUltimoIP() {
		return _ultimoIP;
	}

	public String getPregunta() {
		return _question;
	}

	public Personaje getTempPersonaje() {
		return _tempPerso;
	}

	public String getRespuesta() {
		return _reponse;
	}

	public boolean estaBaneado() {
		return _baneado;
	}

	public void setBaneado(boolean baneado) {
		_baneado = baneado;
	}

	public boolean enLinea() {
		return _entradaGeneral != null || _entradaPersonaje != null;
	}

	public int getRango() {
		return _rango;
	}

	public String getActualIP() {
		return _tempIP;
	}

	public void cambiarContrase\u00f1a(String nueva) {
		_pass = nueva;
	}

	public static boolean cuentaLogin(String nombre, String contrase\u00f1a, String codigoLlave) {
		Cuenta cuenta = Mundo.getCuentaPorNombre(nombre);
		return cuenta != null && cuenta.esContrase\u00f1aValida(contrase\u00f1a, codigoLlave);
	}

	public boolean esContrase\u00f1aValida(String contrase\u00f1a, String codigoLlave) {
		return contrase\u00f1a.equals(CryptManager.encriptarPassword(codigoLlave, _pass));
	}

	public Map<Integer, Personaje> getPersonajes() {
		return _personajes;
	}

	public void addPerso(Personaje perso) {
		if (_personajes.get(perso.getID()) != null) {
			System.out.println("Se esta intentado volver agregar a la cuenta, al personaje " + perso.getNombre());
			return;
		}
		_personajes.put(perso.getID(), perso);
	}

	public boolean crearPj(String nombre, int sexo, int clase, int color1, int color2, int color3) {
		Personaje perso = Personaje.crearPersonaje(nombre, sexo, clase, color1, color2, color3, this);
		if (perso == null) {
			return false;
		}
		_personajes.put(perso.getID(), perso);
		SocketManager.ENVIAR_TB_CINEMA_INICIO_JUEGO(perso);
		return true;
	}

	public void borrarPerso(int id) {
		if (!_personajes.containsKey(id)) {
			return;
		}
		Mundo.eliminarPj(_personajes.get(id), true);
		_personajes.remove(id);
	}

	public void setEntradaGeneral(RealmThread thread) {
		_entradaGeneral = thread;
	}

	public void setTempPerso(Personaje perso) {
		_tempPerso = perso;
	}

	public void actualizarInformacion(int id, String nombre, String contrase\u00f1a, String apodo, String pregunta,
			String respuesta, int rango, boolean baneado) {
		_guid = id;
		_account = nombre;
		_pass = contrase\u00f1a;
		_apodo = apodo;
		_question = pregunta;
		_reponse = respuesta;
		_rango = rango;
		_baneado = baneado;
	}

	public synchronized void desconexion() {
		_tempPerso = null;
		_entradaPersonaje = null;
		_entradaGeneral = null;
		_tempIP = "";
		SQLManager.SALVAR_CUENTA(this);
		resetTodosPjs();
		SQLManager.UPDATE_CUENTA_LOG_CERO(getID());
	}

	public synchronized void resetTodosPjs() {
		try {
			for (Personaje perso : _personajes.values()) {
				if (!perso.enLinea())
					continue;
				if (perso.getIntercambio() != null)
					perso.getIntercambio().cancel();
				Combate pelea = perso.getPelea();
				perso.setEnLinea(false);
				if (perso.getHaciendoTrabajo() != null)
					perso.getHaciendoTrabajo().interrumpirMagueada();
				if (pelea != null) {
					if (pelea.getEstado() != 3 || pelea.getEspectadores().containsKey(Integer.valueOf(perso.getID()))
							|| pelea.getTipoPelea() == 0) {
						pelea.retirarsePelea(perso, null);
					} else {
						perso.getCelda().removerPersonaje(perso.getID());
						pelea.desconectarLuchador(perso);
						continue;
					}
				} else if (perso.getMapa() != null) {
					SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(perso.getMapa(), perso.getID());
				}
				if (perso.getGrupo() != null)
					perso.getGrupo().dejarGrupo(perso);
				perso.getCelda().removerPersonaje(perso.getID());
				perso.resetVariables();
				SQLManager.SALVAR_PERSONAJE(perso, true);
				Mundo.desconectarPerso(perso);
			}
		} catch (Exception e) {
			resetTodosPjs();
		}
	}

	public void mensajeAAmigos() {
		for (int i : _idsAmigos) {
			Personaje perso = Mundo.getPersonaje(i);
			if (perso == null || !perso.mostrarConeccionAmigo() || !perso.enLinea())
				continue;
			SocketManager.ENVIAR_Im0143_AMIGO_CONECTADO(_tempPerso, perso);
		}
	}

	public String analizarListaAmigosABD() {
		StringBuffer str = new StringBuffer();
		for (int i : _idsAmigos) {
			if (!str.toString().isEmpty()) {
				str = str.append(";");
			}
			str = str.append(i);
		}
		return str.toString();
	}

	public String stringListaEnemigosABD() {
		StringBuffer str = new StringBuffer();
		for (int i : _idsEnemigos) {
			if (!str.toString().isEmpty()) {
				str = str.append(";");
			}
			str = str.append(i);
		}
		return str.toString();
	}

	public String stringListaAmigos() {
		StringBuffer str = new StringBuffer();
		for (int i : _idsAmigos) {
			Personaje perso;
			Cuenta cuenta = Mundo.getCuenta(i);
			if (cuenta == null)
				continue;
			str = str.append("|" + cuenta.getApodo());
			if (!cuenta.enLinea() || (perso = cuenta.getTempPersonaje()) == null)
				continue;
			str = str.append(perso.analizarListaAmigos(_guid));
		}
		return str.toString();
	}

	public String stringListaEnemigos() {
		StringBuffer str = new StringBuffer();
		for (int i : _idsEnemigos) {
			Personaje perso;
			Cuenta cuenta = Mundo.getCuenta(i);
			if (cuenta == null)
				continue;
			str = str.append("|" + cuenta.getApodo());
			if (!cuenta.enLinea() || (perso = cuenta.getTempPersonaje()) == null)
				continue;
			str = str.append(perso.analizarListaAmigos(_guid));
		}
		return str.toString();
	}

	public void addAmigo(int id) {
		if (_guid == id) {
			SocketManager.ENVIAR_FA_AGREGAR_AMIGO(_tempPerso, "Ey");
			return;
		}
		if (_idsEnemigos.contains(id)) {
			SocketManager.ENVIAR_iA_AGREGAR_ENEMIGO(_tempPerso, "Ea");
			return;
		}
		if (!_idsAmigos.contains(id)) {
			_idsAmigos.add(id);
			Cuenta amigo = Mundo.getCuenta(id);
			SocketManager.ENVIAR_FA_AGREGAR_AMIGO(_tempPerso,
					"K" + amigo.getApodo() + amigo.getTempPersonaje().analizarListaAmigos(_guid));
		} else {
			SocketManager.ENVIAR_FA_AGREGAR_AMIGO(_tempPerso, "Ea");
		}
	}

	public void addEnemigo(String packet, int id) {
		if (_guid == id) {
			SocketManager.ENVIAR_iA_AGREGAR_ENEMIGO(_tempPerso, "Ey");
			return;
		}
		if (_idsAmigos.contains(id)) {
			SocketManager.ENVIAR_FA_AGREGAR_AMIGO(_tempPerso, "Ea");
			return;
		}
		if (!_idsEnemigos.contains(id)) {
			_idsEnemigos.add(id);
			Cuenta amigo = Mundo.getCuenta(id);
			SocketManager.ENVIAR_iA_AGREGAR_ENEMIGO(_tempPerso,
					"K" + amigo.getApodo() + amigo.getTempPersonaje().analizarListaEnemigos(_guid));
		} else {
			SocketManager.ENVIAR_iA_AGREGAR_ENEMIGO(_tempPerso, "Ea");
		}
	}

	public void borrarAmigo(int id) {
		_idsAmigos.remove(_idsAmigos.indexOf(id));
		SocketManager.ENVIAR_FD_BORRAR_AMIGO(_tempPerso, "K");
	}

	public void borrarEnemigo(int id) {
		_idsEnemigos.remove(_idsEnemigos.indexOf(id));
		SocketManager.ENVIAR_iD_BORRAR_ENEMIGO(_tempPerso, "K");
	}

	public boolean esAmigo(int id) {
		return _idsAmigos.contains(id);
	}

	public boolean esEnemigo(int id) {
		return _idsEnemigos.contains(id);
	}

	public void setPrimeraVez(int valor) {
		_primeraVez = valor;
	}

	public synchronized String stringIDsEstablo() {
		StringBuffer str = new StringBuffer();
		boolean primero = false;
		for (Dragopavo DP : _establo) {
			if (primero) {
				str = str.append(";");
			}
			str = str.append(DP.getID());
			primero = true;
		}
		return str.toString();
	}

	public void setRango(int rango) {
		_rango = rango;
	}

	public int getVIP() {
		return _vip;
	}

	public boolean recuperarObjeto(int lineaID, int cantidad) {
		if (_tempPerso == null) {
			return false;
		}
		if (_tempPerso.getIntercambiandoCon() >= 0) {
			return false;
		}
		int idPuestoMerca = Math.abs(_tempPerso.getIntercambiandoCon());
		Mercadillo.ObjetoMercadillo objMerca = null;
		try {
			for (Mercadillo.ObjetoMercadillo tempEntry : _objMercadillos.get(idPuestoMerca)) {
				if (tempEntry.getLineaID() != lineaID)
					continue;
				objMerca = tempEntry;
				break;
			}
		} catch (NullPointerException e) {
			return false;
		}
		if (objMerca == null) {
			return false;
		}
		_objMercadillos.get(idPuestoMerca).remove(objMerca);
		Objeto obj = objMerca.getObjeto();
		if (_tempPerso.addObjetoSimilar(obj, true, -1)) {
			Mundo.eliminarObjeto(obj.getID());
		} else {
			_tempPerso.addObjetoPut(obj);
			SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_entradaPersonaje.getOut(), obj);
		}
		Mundo.getPuestoMerca(idPuestoMerca).borrarObjMercaDelPuesto(objMerca);
		return true;
	}

	public Mercadillo.ObjetoMercadillo[] getObjMercaDePuesto(int idPuestoMerca) {
		if (_objMercadillos.get(idPuestoMerca) == null) {
			return new Mercadillo.ObjetoMercadillo[1];
		}
		Mercadillo.ObjetoMercadillo[] listaObjMercadillos = new Mercadillo.ObjetoMercadillo[20];
		for (int i = 0; i < _objMercadillos.get(idPuestoMerca).size(); ++i) {
			listaObjMercadillos[i] = _objMercadillos.get(idPuestoMerca).get(i);
		}
		return listaObjMercadillos;
	}

	public int cantidadObjMercadillo(int idPuestoMerca) {
		if (_objMercadillos.get(idPuestoMerca) == null) {
			return 0;
		}
		return _objMercadillos.get(idPuestoMerca).size();
	}
}