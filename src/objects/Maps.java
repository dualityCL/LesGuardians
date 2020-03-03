package objects;

import common.Condiciones;
import common.Constantes;
import common.CryptManager;
import common.Fórmulas;
import common.LesGuardians;
import common.Pathfinding;
import common.SQLManager;
import common.SocketManager;
import common.World;
import game.GameThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Timer;
import objects.Acao;
import objects.Casa;
import objects.Cofre;
import objects.Coletor;
import objects.Dragossauros;
import objects.Fight;
import objects.Guild;
import objects.MOB_tmpl;
import objects.MOB_tmpl.GrupoMobs;
import objects.NPC_tmpl;
import objects.NPC_tmpl.NPC;
import objects.Objeto;
import objects.Personagens;
import objects.Prisma;

public class Maps {
	int _sigIDMapaInfo = -1;
	private short _id;
	private String _fecha;
	private byte _ancho;
	private byte _alto;
	private String _key;
	private String _posicionesDePelea;
	private Map<Short, Celda> _celdas = new TreeMap<Short, Celda>();
	private Map<Short, Fight> _peleas = new TreeMap<Short, Fight>();
	private ArrayList<MOB_tmpl.MobGrado> _mobPosibles = new ArrayList();
	private Map<Integer, MOB_tmpl.GrupoMobs> _grupoMobs = new TreeMap<Integer, MOB_tmpl.GrupoMobs>();
	private Map<Integer, MOB_tmpl.GrupoMobs> _grupoMobsFix = new TreeMap<Integer, MOB_tmpl.GrupoMobs>();
	private Map<Integer, NPC_tmpl.NPC> _npcs = new TreeMap<Integer, NPC_tmpl.NPC>();
	private ArrayList<Integer> _mercante = new ArrayList();
	private ArrayList<Celda> _casillasObjInterac = new ArrayList();
	private byte _X = 0;
	private byte _Y = 0;
	private World.SubArea _subArea;
	private Cercado _cercado;
	private byte _maxGrupoDeMobs = (byte) 3;
	private Map<Integer, ArrayList<Acao>> _accionFinPelea = new TreeMap<Integer, ArrayList<Acao>>();
	private byte _maxMobsPorGrupo;
	private byte _capacidadMercantes = 0;
	private String _mapData = "";
	private int _descripcion = 0;
	private MOB_tmpl.GrupoMobs _ultimoGrupo = null;

	public void addCasillaObjInteractivo(Celda celda) {
		_casillasObjInterac.add(celda);
	}

	public ArrayList<Celda> getCasillasObjInter() {
		return _casillasObjInterac;
	}

	public Maps(short ID, String fecha, byte ancho, byte alto, String key, String posicionesDePelea, String mapData,
			String celdasData, String mobs, String mapPos, byte maxGrupoDeMobs, byte maxMobsPorGrupo, byte capacidad,
			int descripcion) {
		_id = ID;
		_fecha = fecha;
		_ancho = ancho;
		_alto = alto;
		_key = key;
		_posicionesDePelea = posicionesDePelea;
		_maxGrupoDeMobs = maxGrupoDeMobs;
		_maxMobsPorGrupo = maxMobsPorGrupo;
		_capacidadMercantes = capacidad;
		_mapData = mapData;
		_descripcion = descripcion;
		String[] mapInfos = mapPos.split(",");
		try {
			_X = Byte.parseByte(mapInfos[0]);
			_Y = Byte.parseByte(mapInfos[1]);
			int subArea = Integer.parseInt(mapInfos[2]);
			_subArea = World.getSubArea(subArea);
			if (_subArea != null) {
				_subArea.addMapa(this);
			}
		} catch (Exception e) {
			System.out.println("Error al cargar el mapa ID " + ID + ": El campo MapPos es invalido");
			System.exit(0);
		}
		if (!_mapData.isEmpty()) {
			_celdas = CryptManager.decompilarMapaData(this, _mapData);
		} else {
			String[] arrayDataCeldas;
			String[] arrstring = arrayDataCeldas = celdasData.split("\\|");
			int n = arrayDataCeldas.length;
			for (int i = 0; i < n; ++i) {
				String o = arrstring[i];
				boolean caminable = true;
				boolean lineaDeVista = true;
				short id = -1;
				int obj = -1;
				String[] celdaInfo = o.split(",");
				try {
					caminable = celdaInfo[2].equals("1");
					lineaDeVista = celdaInfo[1].equals("1");
					id = Short.parseShort(celdaInfo[0]);
					if (!celdaInfo[3].trim().equals("")) {
						obj = Integer.parseInt(celdaInfo[3]);
					}
				} catch (Exception exception) {
					// empty catch block
				}
				if (id == -1)
					continue;
				_celdas.put(id, new Celda(this, id, caminable, lineaDeVista, obj));
			}
		}
		for (String mob : mobs.split("\\|")) {
			if (mob.equals(""))
				continue;
			int id = 0;
			int nivel = 0;
			try {
				id = Integer.parseInt(mob.split(",")[0]);
				nivel = Integer.parseInt(mob.split(",")[1]);
			} catch (NumberFormatException e) {
				continue;
			}
			if (id == 0 || nivel == 0 || World.getMobModelo(id) == null
					|| World.getMobModelo(id).getGradoPorNivel(nivel) == null)
				continue;
			_mobPosibles.add(World.getMobModelo(id).getGradoPorNivel(nivel));
		}
		if (LesGuardians.USAR_MOBS) {
			if (_maxGrupoDeMobs == 0) {
				return;
			}
			byte alineacion = (byte) _subArea.getAlineacion();
			if (esMazmorra()) {
				alineacion = 0;
			}
			spawnGrupo(alineacion, _maxGrupoDeMobs, false, (short) -1, null);
		}
	}

	public void aplicarAccionFinCombate(int tipo, ArrayList<Fight.Luchador> ganadores, boolean evento) {
		if (_accionFinPelea.get(tipo) == null) {
			return;
		}
		for (Acao accion : _accionFinPelea.get(tipo)) {
			if (accion.getCondiciones().equalsIgnoreCase("EVENTO") && !evento)
				continue;
			for (Fight.Luchador ganador : ganadores) {
				Personagens perso = ganador.getPersonaje();
				if (perso == null)
					continue;
				accion.aplicar(perso, null, -1, (short) -1);
			}
		}
	}

	public void setDescripcion(int d) {
		_descripcion = d;
	}

	public boolean esTaller() {
		return (_descripcion & 1) == 1;
	}

	public boolean esArena() {
		return (_descripcion & 2) == 2;
	}

	public boolean esMazmorra() {
		return (_descripcion & 4) == 4;
	}

	public boolean esPVP() {
		return (_descripcion & 8) == 8;
	}

	public boolean esCasa() {
		return (_descripcion & 0x10) == 16;
	}

	public void addAccionFinPelea(int tipo, Acao accion) {
		if (_accionFinPelea.get(tipo) == null) {
			_accionFinPelea.put(tipo, new ArrayList());
		}
		delAccionFinPelea(tipo, accion.getID());
		_accionFinPelea.get(tipo).add(accion);
	}

	public void delAccionFinPelea(int tipo, int aTipo) {
		if (_accionFinPelea.get(tipo) == null)
			return;
		ArrayList<Acao> copy = new ArrayList<Acao>();
		copy.addAll(_accionFinPelea.get(tipo));
		for (Acao A : copy)
			if (A.getID() == aTipo)
				(_accionFinPelea.get(tipo)).remove(A);
	}

	public void borrarTodoAcciones() {
		_accionFinPelea.clear();
	}

	public void setCercado(Cercado cercado) {
		_cercado = cercado;
	}

	public Cercado getCercado() {
		return _cercado;
	}

	public Maps(short id, String fecha, byte ancho, byte alto, String key, String posPelea) {
		_id = id;
		_fecha = fecha;
		_ancho = ancho;
		_alto = alto;
		_key = key;
		_posicionesDePelea = posPelea;
		_celdas = new TreeMap<Short, Celda>();
	}

	public World.SubArea getSubArea() {
		return _subArea;
	}

	public byte getCapacidad() {
		return _capacidadMercantes;
	}

	public byte getX() {
		return _X;
	}

	public byte getY() {
		return _Y;
	}

	public String getMapData() {
		return _mapData;
	}

	public void actualizarCasillas() {
		if (!_mapData.isEmpty()) {
			_celdas = CryptManager.decompilarMapaData(this, _mapData);
		}
	}

	public void setMapData(String mapdata) {
		_mapData = mapdata;
	}

	public Map<Integer, NPC_tmpl.NPC> getNPCs() {
		return _npcs;
	}

	public NPC_tmpl.NPC addNPC(int npcID, short celdaID, byte dir) {
		NPC_tmpl npcModelo = World.getNPCModelo(npcID);
		if (npcModelo == null || getCelda(celdaID) == null) {
			return null;
		}
		NPC_tmpl.NPC npc = new NPC_tmpl.NPC(npcModelo, _sigIDMapaInfo, celdaID, dir, npcModelo.getNombre());
		_npcs.put(_sigIDMapaInfo, npc);
		--_sigIDMapaInfo;
		return npc;
	}

	public ArrayList<Integer> removerMercante(int personaje) {
		if (_mercante.size() < 1) {
			return _mercante;
		}
		if (_mercante.contains(personaje)) {
			int index = _mercante.indexOf(personaje);
			_mercante.remove(index);
		}
		return _mercante;
	}

	public ArrayList<Integer> agregarMercante(int personaje) {
		if (_mercante.size() >= _capacidadMercantes) {
			return _mercante;
		}
		if (_mercante.contains(personaje)) {
			return _mercante;
		}
		_mercante.add(personaje);
		return _mercante;
	}

	public ArrayList<Integer> addMercantesMapa(String personajes) {
		String[] persos;
		if (personajes == "|" || personajes.isEmpty()) {
			return null;
		}
		String[] arrstring = persos = personajes.split("\\|");
		int n = persos.length;
		for (int i = 0; i < n; ++i) {
			String personaje = arrstring[i];
			if (personaje == "")
				continue;
			_mercante.add(Integer.parseInt(personaje));
		}
		return _mercante;
	}

	public String getMercantes() {
		String personajes = "";
		boolean primero = true;
		if (_mercante.size() == 0) {
			return personajes;
		}
		for (Integer personaje : _mercante) {
			if (!primero) {
				personajes = String.valueOf(personajes) + "|";
			}
			personajes = String.valueOf(personajes) + Integer.toString(personaje);
			primero = false;
		}
		return personajes;
	}

	public int cantMercantes() {
		int cantidad = _mercante.size();
		return cantidad;
	}

	public void setPosicionesDePelea(String posiciones) {
		_posicionesDePelea = posiciones;
	}

	public NPC_tmpl.NPC getNPC(int id) {
		return _npcs.get(id);
	}

	public NPC_tmpl.NPC removeNPC(int id) {
		return _npcs.remove(id);
	}

	public Celda getCelda(short id) {
		return _celdas.get(id);
	}

	public ArrayList<Personagens> getPersos() {
		ArrayList<Personagens> personajes = new ArrayList<Personagens>();
		for (Celda celda : _celdas.values()) {
			Collection<Personagens> persos = celda.getPersos().values();
			for (Personagens entry : persos) {
				personajes.add(entry);
			}
		}
		return personajes;
	}

	public ArrayList<Integer> getPersosID() {
		ArrayList<Integer> personajes = new ArrayList<Integer>();
		for (Celda celda : _celdas.values()) {
			Collection<Personagens> persos = celda.getPersos().values();
			for (Personagens entry : persos) {
				personajes.add(entry.getID());
			}
		}
		return personajes;
	}

	public void borrarJugador(int ID) {
		for (Celda celda : _celdas.values()) {
			Collection<Personagens> persos = celda.getPersos().values();
			for (Personagens entry : persos) {
				if (entry.getID() != ID)
					continue;
				celda.removerPersonaje(ID);
			}
		}
	}

	public short getID() {
		return _id;
	}

	public String getFecha() {
		return _fecha;
	}

	public byte getAncho() {
		return _ancho;
	}

	public short ultimaCeldaID() {
		short celda = (short) (_ancho * _alto * 2 - (_alto + _ancho));
		return celda;
	}

	public boolean esCeldaLadoIzq(short celda) {
		short ladoIzq = _ancho;
		for (int i = 0; i < _alto; ++i) {
			if (celda == ladoIzq) {
				return true;
			}
			ladoIzq = (short) (ladoIzq + (_ancho * 2 - 1));
		}
		return false;
	}

	public boolean esCeldaLadoDer(short celda) {
		short ladoDer = (short) (2 * (_ancho - 1));
		for (int i = 0; i < _alto; ++i) {
			if (celda == ladoDer) {
				return true;
			}
			ladoDer = (short) (ladoDer + (_ancho * 2 - 1));
		}
		return false;
	}

	public boolean celdaSalienteLateral(short celda1, short celda2) {
		if (esCeldaLadoIzq(celda1) && (celda2 == celda1 + (_ancho - 1) || celda2 == celda1 - _ancho)) {
			return true;
		}
		return esCeldaLadoDer(celda1) && (celda2 == celda1 + _ancho || celda2 == celda1 - (_ancho - 1));
	}

	public byte getAlto() {
		return _alto;
	}

	public String getCodigo() {
		return _key;
	}

	public String getPosicionesPelea() {
		return _posicionesDePelea;
	}

	public boolean aptoParaPelea() {
		if (_posicionesDePelea.isEmpty() || _posicionesDePelea.equalsIgnoreCase("|")) {
			return false;
		}
		try {
			String[] str = _posicionesDePelea.split("\\|");
			if (str[0].length() > 0 && str[1].length() > 0) {
				return true;
			}
		} catch (Exception exception) {
			// empty catch block
		}
		return false;
	}

	public void addJugador(Personagens perso) {
		SocketManager.ENVIAR_GM_AGREGAR_PJ_A_TODOS(this, perso);
		perso.getCelda().addPersonaje(perso);
	}

	public void addJugadorSinMostrar(Personagens perso) {
		perso.getCelda().addPersonaje(perso);
	}

	public String getGMsPackets() {
		String packets = "";
		for (Celda celda : _celdas.values()) {
			try {
				for (Personagens perso : celda.getPersos().values()) {
					if (perso.getPelea() != null)
						continue;
					packets = String.valueOf(packets) + "GM|+" + perso.stringGM() + '\u0000';
				}
			} catch (ConcurrentModificationException concurrentModificationException) {
				// empty catch block
			}
		}
		return packets;
	}

	public String getGMsLuchadores() {
		String packets = "";
		for (Celda celda : _celdas.values()) {
			for (Fight.Luchador luchador : celda.getLuchadores().values()) {
				packets = String.valueOf(packets) + "GM|+" + luchador.stringGM() + '\u0000';
			}
		}
		return packets;
	}

	public String getGMsGrupoMobs() {
		if (_grupoMobs.isEmpty()) {
			return "";
		}
		String packets = "GM|";
		boolean primero = true;
		for (MOB_tmpl.GrupoMobs grupoMob : _grupoMobs.values()) {
			String GM = grupoMob.enviarGM();
			if (GM.equals(""))
				continue;
			if (!primero) {
				packets = String.valueOf(packets) + "|";
			}
			packets = String.valueOf(packets) + GM;
			primero = false;
		}
		return packets;
	}

	public String getGMsPrismas() {
		if (World.todosPrismas() == null) {
			return "";
		}
		String str = "";
		for (Prisma prisma : World.todosPrismas()) {
			if (prisma.getMapaID() != _id)
				continue;
			str = prisma.getGMPrisma();
			break;
		}
		return str;
	}

	public String getGMsNPCs() {
		if (_npcs.isEmpty()) {
			return "";
		}
		String packets = "GM|";
		boolean primero = true;
		for (NPC_tmpl.NPC npc : _npcs.values()) {
			String GM = npc.analizarGM();
			if (GM.equals(""))
				continue;
			if (!primero) {
				packets = String.valueOf(packets) + "|";
			}
			packets = String.valueOf(packets) + GM;
			primero = false;
		}
		return packets;
	}

	public String getGMsMercantes() {
		if (_mercante.isEmpty() || _mercante.size() == 0) {
			return "";
		}
		String packets = "GM|+";
		boolean primero = true;
		for (Integer idperso : _mercante) {
			try {
				String GM = World.getPersonaje(idperso).stringGMmercante();
				if (GM.equals(""))
					continue;
				if (!primero) {
					packets = String.valueOf(packets) + "|+";
				}
				packets = String.valueOf(packets) + GM;
				primero = false;
			} catch (Exception exception) {
				// empty catch block
			}
		}
		return packets;
	}

	public String getGMsMonturas() {
		if (_cercado == null || _cercado.getListaCriando().size() == 0) {
			return "";
		}
		String packets = "GM|+";
		boolean primero = true;
		for (Integer idmontura : _cercado.getListaCriando()) {
			String GM = World.getDragopavoPorID(idmontura).getCriarMontura(_cercado);
			if (GM.equals(""))
				continue;
			if (!primero) {
				packets = String.valueOf(packets) + "|+";
			}
			packets = String.valueOf(packets) + GM;
			primero = false;
		}
		return packets;
	}

	public String getObjetosCria() {
		if (_cercado == null || _cercado.getObjetosColocados().size() == 0) {
			return "";
		}
		String packets = "GDO+";
		boolean primero = true;
		for (Map.Entry<Short, Map<Integer, Integer>> entry : _cercado.getObjetosColocados().entrySet()) {
			for (Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
				if (!primero) {
					packets = String.valueOf(packets) + "|";
				}
				packets = String.valueOf(packets) + entry.getKey() + ";" + entry2.getKey() + ";1;1000;1000";
				primero = false;
			}
		}
		return packets;
	}
	
	public String getObjectosGDF() {
		StringBuilder str = new StringBuilder("GDF");
		for (Celda celda : _celdas.values()) {
			if (celda.getObjetoInterac() != null) {
				int celdaID = celda.getID();
				ObjetoInteractivo object = celda.getObjetoInterac();
				str.append("|" + celdaID + ";" + object.getEstado() + ";" + (object.esInteractivo() ? "1" : "0"));
			}
		}
		return str.toString();
	}

	public int getNumeroPeleas() {
		return _peleas.size();
	}

	public Map<Short, Fight> getPeleas() {
		return _peleas;
	}

	public void quitarPelea(short id) {
		_peleas.remove(id);
	}

	public short getRandomCeldaIDLibre() {
		ArrayList<Short> celdaLibre = new ArrayList<Short>();
		for (Map.Entry<Short, Celda> entry : _celdas.entrySet()) {
			Celda celda = entry.getValue();
			if (!celda.esCaminable(true))
				continue;
			boolean ok = true;
			for (Map.Entry<Integer, MOB_tmpl.GrupoMobs> entry2 : _grupoMobs.entrySet()) {
				if (entry2.getValue().getCeldaID() != celda.getID())
					continue;
				ok = false;
			}
			if (!ok)
				continue;
			ok = true;
			for (Entry<Integer, NPC> entry3 : _npcs.entrySet()) {
				if (((NPC_tmpl.NPC) entry3.getValue()).getCeldaID() != celda.getID())
					continue;
				ok = false;
			}
			if (!ok || !celda.getPersos().isEmpty())
				continue;
			celdaLibre.add(celda.getID());
		}
		if (celdaLibre.isEmpty()) {
			System.out.println("Alguna celda libre no esta ubicada en el mapa " + _id + " : grupo no spawn");
			return -1;
		}
		int rand = Fórmulas.getRandomValor(0, celdaLibre.size() - 1);
		return (Short) celdaLibre.get(rand);
	}
	
	public void refrescarGrupoMobs() {
		if (LesGuardians.MODO_HEROICO) {
			return;
		}
		for (int id : _grupoMobs.keySet()) {
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(this, id);
		}
		_grupoMobs.clear();
		_grupoMobs.putAll(_grupoMobsFix);
		for (GrupoMobs GM : _grupoMobsFix.values()) {
			SocketManager.ENVIAR_GM_GRUPOMOB_A_MAPA(this, GM);
		}
		byte alineacion = (byte) _subArea.getAlineacion();
		if (esMazmorra()) {
			alineacion = 0;
		}
		spawnGrupo(alineacion, _maxGrupoDeMobs, true, (short) -1, null);
	}

	public void subirEstrellasMobs() {
		for (MOB_tmpl.GrupoMobs grupoMob : _grupoMobs.values()) {
			grupoMob.aumentarEstrellas();
		}
	}

	public void subirEstrellasCantidad(int cantidad) {
		for (MOB_tmpl.GrupoMobs grupoMob : _grupoMobs.values()) {
			grupoMob.aumentarEstrellasCant(cantidad);
		}
	}

	public void jugadorLLegaACelda(Personagens perso, short celdaID) {
		if (_celdas.get(celdaID) == null) {
			return;
		}
		Objeto obj = _celdas.get(celdaID).getObjetoTirado();
		if (obj != null) {
			if (!perso.addObjetoSimilar(obj, true, -1)) {
				perso.addObjetoPut(obj);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, obj);
			} else {
				World.eliminarObjeto(obj.getID());
			}
			SocketManager.ENVIAR_GDO_OBJETO_TIRADO_AL_SUELO(this, '-', (int) celdaID, 0, 0);
			SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
			_celdas.get(celdaID).borrarObjetoTirado();
		}
		_celdas.get(celdaID).aplicarAccionCeldaPosicionada(perso);
		if (perso.esFantasma() || perso.esTumba() || !aptoParaPelea() || perso.getMapa().getID() != _id) {
			return;
		}
		for (MOB_tmpl.GrupoMobs grupoMob : _grupoMobs.values()) {
			if (perso.getAlineacion() == grupoMob.getAlineamiento() && grupoMob.getAlineamiento() != 0
					|| (perso.getAlineacion() == 0 || perso.getAlineacion() == 3) && grupoMob.getAlineamiento() != 0
					|| Pathfinding.distanciaEntreDosCeldas(this, celdaID, grupoMob.getCeldaID()) > grupoMob
							.getDistanciaAgresion()
					|| !Condiciones.validaCondiciones(null, perso, grupoMob.getCondicion()))
				continue;
			iniciarPeleaVSMobs(perso, grupoMob);
			return;
		}
	}

	public void spawnGrupo(byte alineacion, int cantGrupos, boolean enviar, short celdaID, MOB_tmpl.GrupoMobs grupoM) {
		if (cantGrupos < 1 || _mobPosibles.size() < 1 || _grupoMobs.size() - _grupoMobsFix.size() >= _maxGrupoDeMobs) {
			return;
		}
		if (grupoM != null) {
			if (!grupoM.estaMuerto()) {
				_grupoMobs.put(grupoM.getID(), grupoM);
				SocketManager.ENVIAR_GM_GRUPOMOB_A_MAPA(this, grupoM);
				return;
			}
			celdaID = (short) -1;
		}
		for (int a = 1; a <= cantGrupos; ++a) {
			MOB_tmpl.GrupoMobs grupo = new MOB_tmpl.GrupoMobs(_sigIDMapaInfo, alineacion, _mobPosibles, this, celdaID,
					_maxMobsPorGrupo);
			if (grupo.getMobs().isEmpty())
				continue;
			_grupoMobs.put(_sigIDMapaInfo, grupo);
			if (enviar) {
				SocketManager.ENVIAR_GM_GRUPOMOB_A_MAPA(this, grupo);
			}
			--_sigIDMapaInfo;
		}
	}

	public void addGrupoMobTimer(boolean timer, short celda, String strGrupoMob, String condicion) {
		MOB_tmpl.GrupoMobs grupoMob = new MOB_tmpl.GrupoMobs(_sigIDMapaInfo, celda, strGrupoMob);
		if (grupoMob.getMobs().isEmpty()) {
			return;
		}
		_grupoMobs.put(_sigIDMapaInfo, grupoMob);
		grupoMob.setCondicion(condicion);
		grupoMob.setEsPermanente(false);
		SocketManager.ENVIAR_GM_GRUPOMOB_A_MAPA(this, grupoMob);
		--_sigIDMapaInfo;
		if (timer) {
			grupoMob.inicioTiempoCondicion();
		}
	}

	public void addGrupoMobSoloUnaPelea(short celda, String strGrupoMob) {
		MOB_tmpl.GrupoMobs grupoMob = new MOB_tmpl.GrupoMobs(_sigIDMapaInfo, celda, strGrupoMob);
		if (grupoMob.getMobs().isEmpty()) {
			return;
		}
		_grupoMobs.put(_sigIDMapaInfo, grupoMob);
		grupoMob.setEsPermanente(false);
		SocketManager.ENVIAR_GM_GRUPOMOB_A_MAPA(this, grupoMob);
		--_sigIDMapaInfo;
	}

	public void setUltimoGrupoMob(MOB_tmpl.GrupoMobs mob) {
		_ultimoGrupo = mob;
	}

	public void addGrupoMobPermanente(short celda, String strGrupoMob) {
		MOB_tmpl.GrupoMobs grupoMob;
		if (_ultimoGrupo == null) {
			grupoMob = new MOB_tmpl.GrupoMobs(_sigIDMapaInfo, celda, strGrupoMob);
			if (grupoMob.getMobs().isEmpty()) {
				return;
			}
			--_sigIDMapaInfo;
		} else {
			grupoMob = _ultimoGrupo;
		}
		_grupoMobs.put(grupoMob.getID(), grupoMob);
		_grupoMobsFix.put(grupoMob.getID(), grupoMob);
		SocketManager.ENVIAR_GM_GRUPOMOB_A_MAPA(this, grupoMob);
		_ultimoGrupo = null;
	}

	public Fight iniciarPeleaPjVSPj(Personagens init1, Personagens init2, byte tipo) {
		short id = 1;
		if (!_peleas.isEmpty()) {
			id = (short) ((Short) _peleas.keySet().toArray()[_peleas.size() - 1] + 1);
		}
		Fight pelea = new Fight(id, this, init1, init2, tipo);
		_peleas.put(id, pelea);
		SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(this);
		return pelea;
	}

	public void iniciarPeleaVSMobs(Personagens perso, MOB_tmpl.GrupoMobs grupoMob) {
		short id = 1;
		if (!_peleas.isEmpty()) {
			id = (short) ((Short) _peleas.keySet().toArray()[_peleas.size() - 1] + 1);
		}
		if (!grupoMob.esPermanente()) {
			_grupoMobs.remove(grupoMob.getID());
		} else {
			SocketManager.ENVIAR_GM_GRUPMOBS(this);
		}
		_peleas.put(id, new Fight(id, this, perso, grupoMob, (byte)4));
		SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(this);
	}

	public void iniciarPeleaVSDopeul(Personagens perso, MOB_tmpl.GrupoMobs grupoMob) {
		short id = 1;
		if (!_peleas.isEmpty()) {
			id = (short) ((Short) _peleas.keySet().toArray()[_peleas.size() - 1] + 1);
		}
		_peleas.put(id, new Fight(id, this, perso, grupoMob, (byte)3));
		SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(this);
	}

	public void iniciarPeleaVSRecaudador(Personagens perso, Coletor recaudador) {
		short id = 1;
		if (!_peleas.isEmpty()) {
			id = (short) ((Short) _peleas.keySet().toArray()[_peleas.size() - 1] + 1);
		}
		_peleas.put(id, new Fight(id, this, perso, recaudador));
		SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(this);
	}

	public void iniciarPeleaVSPrisma(Personagens perso, Prisma prisma) {
		short id = 1;
		if (!_peleas.isEmpty()) {
			id = (short) ((Short) _peleas.keySet().toArray()[_peleas.size() - 1] + 1);
		}
		_peleas.put(id, new Fight(id, this, perso, prisma));
		SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(this);
	}
	
	public Maps copiarMapa() {
		Map<Short, Celda> casillas = new TreeMap<Short, Celda>();
		String posiciones = _posicionesDePelea;
		Maps mapa = new Maps(_id, _fecha, _ancho, _alto, _key, posiciones);
		for (Entry<Short, Celda> entry : _celdas.entrySet()) {
			Celda celda = entry.getValue();
			casillas.put(entry.getKey(), new Celda(mapa, celda.getID(), celda.esCaminable(false), celda.esLineaDeVista(), celda.getObjetoInterac() == null ? -1 : celda.getObjetoInterac().getID()));
		}
		mapa.setCeldas(casillas);
		return mapa;
	}

	private void setCeldas(Map<Short, Celda> casillas) {
		_celdas = casillas;
	}

	public ObjetoInteractivo getPuertaCercado() {
		for (Celda c : _celdas.values()) {
			int idObjInt;
			ObjetoInteractivo objInt = c.getObjetoInterac();
			if (objInt == null
					|| (idObjInt = objInt.getID()) != 6763 && idObjInt != 6766 && idObjInt != 6767 && idObjInt != 6772)
				continue;
			return objInt;
		}
		return null;
	}

	public Map<Integer, MOB_tmpl.GrupoMobs> getMobGroups() {
		return _grupoMobs;
	}

	public void borrarNPCoGrupoMob(int id) {
		_npcs.remove(id);
		_grupoMobs.remove(id);
	}

	public void borrarTodosMobs() {
		_mobPosibles.clear();
		for (int id : _grupoMobs.keySet()) {
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(this, id);
		}
		_grupoMobs.clear();
	}

	public void borrarTodosMobsFix() {
		for (int id : _grupoMobsFix.keySet()) {
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(this, id);
		}
		_grupoMobsFix.clear();
		SQLManager.DELETE_MOBSFIX_MAPA(_id);
		_grupoMobs.clear();
	}

	public byte getMaxGrupoDeMobs() {
		return _maxGrupoDeMobs;
	}

	public void setMaxGrupoDeMobs(byte id) {
		_maxGrupoDeMobs = id;
	}

	public Fight getPelea(short id) {
		return _peleas.get(id);
	}

	public void objetosTirados(Personagens perso) {
		for (Celda c : _celdas.values()) {
			if (c.getObjetoTirado() == null)
				continue;
			SocketManager.ENVIAR_GDO_OBJETO_TIRADO_AL_SUELO(perso, '+', (int) c.getID(),
					c.getObjetoTirado().getModelo().getID(), 0);
		}
	}

	public Map<Short, Celda> getCeldas() {
		return _celdas;
	}

	public static class Celda {
		private short _id;
		private Map<Integer, Personagens> _personajes = new TreeMap<Integer, Personagens>();
		private Map<Integer, Fight.Luchador> _luchadores = new TreeMap<Integer, Fight.Luchador>();
		private boolean _caminable = true;
		private boolean _lineaDeVista = true;
		private short _mapaID;
		private ArrayList<Acao> _celdaAccion;
		private ObjetoInteractivo _objetoInterac;
		private Objeto _objetoTirado;

		public Celda(Maps mapa, short id, boolean caminable, boolean lineaDeVista, int objID) {
			_mapaID = mapa.getID();
			_id = id;
			_caminable = caminable;
			_lineaDeVista = lineaDeVista;
			if (objID == -1) {
				return;
			}
			mapa.addCasillaObjInteractivo(this);
			_objetoInterac = new ObjetoInteractivo(mapa, this, objID);
		}

		public ObjetoInteractivo getObjetoInterac() {
			return _objetoInterac;
		}

		public Objeto getObjetoTirado() {
			return _objetoTirado;
		}

		public short getID() {
			return _id;
		}

		public void addAccionEnUnaCelda(int id, String args, String cond) {
			if (_celdaAccion == null) {
				_celdaAccion = new ArrayList();
			}
			_celdaAccion.add(new Acao(id, args, cond));
		}

		public void aplicarAccionCeldaPosicionada(Personagens perso) {
			if (_celdaAccion == null) {
				return;
			}
			for (Acao act : _celdaAccion) {
				act.aplicar(perso, null, -1, (short) -1);
			}
		}

		public void addPersonaje(Personagens perso) {
			if (!_personajes.containsKey(perso.getID())) {
				_personajes.put(perso.getID(), perso);
			}
		}

		public void addLuchador(Fight.Luchador luchador) {
			if (!_luchadores.containsKey(luchador.getID())) {
				_luchadores.put(luchador.getID(), luchador);
			}
		}

		public void removerLuchador(Fight.Luchador luchador) {
			_luchadores.remove(luchador.getID());
		}

		public void removerPersonaje(int id) {
			_personajes.remove(id);
		}

		public boolean esCaminable(boolean usaObjeto) {
			if (_objetoInterac != null && usaObjeto) {
				return _caminable && _objetoInterac.esCaminable();
			}
			return _caminable;
		}

		public boolean lineaDeVistaBloqueada() {
			if (_luchadores == null) {
				return _lineaDeVista;
			}
			boolean bloqueada = true;
			for (Map.Entry<Integer, Fight.Luchador> entry : _luchadores.entrySet()) {
				if (entry.getValue().esInvisible())
					continue;
				bloqueada = false;
				break;
			}
			return _lineaDeVista && bloqueada;
		}

		public boolean esLineaDeVista() {
			return _lineaDeVista;
		}

		public Map<Integer, Personagens> getPersos() {
			if (_personajes.isEmpty()) {
				return new TreeMap<Integer, Personagens>();
			}
			return _personajes;
		}

		public Map<Integer, Fight.Luchador> getLuchadores() {
			if (_luchadores.isEmpty()) {
				return new TreeMap<Integer, Fight.Luchador>();
			}
			return _luchadores;
		}

		public Fight.Luchador getPrimerLuchador() {
			if (_luchadores.isEmpty()) {
				return null;
			}
			Iterator<Map.Entry<Integer, Fight.Luchador>> iterator = _luchadores.entrySet().iterator();
			if (iterator.hasNext()) {
				Map.Entry<Integer, Fight.Luchador> entry = iterator.next();
				return entry.getValue();
			}
			return null;
		}

		public boolean puedeHacerAccion(int accionID, boolean pescarKuakua) {
			if (_objetoInterac == null) {
				return false;
			}
			switch (accionID) {
			case 47:
			case 122: {
				return _objetoInterac.getID() == 7007;
			}
			case 45: {
				switch (_objetoInterac.getID()) {
				case 7511: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 53: {
				switch (_objetoInterac.getID()) {
				case 7515: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 57: {
				switch (_objetoInterac.getID()) {
				case 7517: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 46: {
				switch (_objetoInterac.getID()) {
				case 7512: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 50:
			case 68: {
				switch (_objetoInterac.getID()) {
				case 7513: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 159: {
				switch (_objetoInterac.getID()) {
				case 7550: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 52: {
				switch (_objetoInterac.getID()) {
				case 7516: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 58: {
				switch (_objetoInterac.getID()) {
				case 7518: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 54:
			case 69: {
				switch (_objetoInterac.getID()) {
				case 7514: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 101: {
				return _objetoInterac.getID() == 7003;
			}
			case 6: {
				switch (_objetoInterac.getID()) {
				case 7500: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 39: {
				switch (_objetoInterac.getID()) {
				case 7501: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 40: {
				switch (_objetoInterac.getID()) {
				case 7502: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 10: {
				switch (_objetoInterac.getID()) {
				case 7503: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 141: {
				switch (_objetoInterac.getID()) {
				case 7542: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 139: {
				switch (_objetoInterac.getID()) {
				case 7541: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 37: {
				switch (_objetoInterac.getID()) {
				case 7504: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 154: {
				switch (_objetoInterac.getID()) {
				case 7553: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 33: {
				switch (_objetoInterac.getID()) {
				case 7505: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 41: {
				switch (_objetoInterac.getID()) {
				case 7506: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 34: {
				switch (_objetoInterac.getID()) {
				case 7507: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 174: {
				switch (_objetoInterac.getID()) {
				case 7557: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 38: {
				switch (_objetoInterac.getID()) {
				case 7508: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 35: {
				switch (_objetoInterac.getID()) {
				case 7509: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 155: {
				switch (_objetoInterac.getID()) {
				case 7554: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 158: {
				switch (_objetoInterac.getID()) {
				case 7552: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 102: {
				switch (_objetoInterac.getID()) {
				case 7519: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 48: {
				return _objetoInterac.getID() == 7005;
			}
			case 32: {
				return _objetoInterac.getID() == 7002;
			}
			case 24: {
				switch (_objetoInterac.getID()) {
				case 7520: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 25: {
				switch (_objetoInterac.getID()) {
				case 7522: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 26: {
				switch (_objetoInterac.getID()) {
				case 7523: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 28: {
				switch (_objetoInterac.getID()) {
				case 7525: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 56: {
				switch (_objetoInterac.getID()) {
				case 7524: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 162: {
				switch (_objetoInterac.getID()) {
				case 7556: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 55: {
				switch (_objetoInterac.getID()) {
				case 7521: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 29: {
				switch (_objetoInterac.getID()) {
				case 7526: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 31: {
				switch (_objetoInterac.getID()) {
				case 7528: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 30: {
				switch (_objetoInterac.getID()) {
				case 7527: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 161: {
				switch (_objetoInterac.getID()) {
				case 7555: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 23: {
				return _objetoInterac.getID() == 7019;
			}
			case 71: {
				switch (_objetoInterac.getID()) {
				case 7533: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 72: {
				switch (_objetoInterac.getID()) {
				case 7534: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 73: {
				switch (_objetoInterac.getID()) {
				case 7535: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 74: {
				switch (_objetoInterac.getID()) {
				case 7536: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 160: {
				switch (_objetoInterac.getID()) {
				case 7551: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 133: {
				return _objetoInterac.getID() == 7024;
			}
			case 128: {
				switch (_objetoInterac.getID()) {
				case 7530: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 124: {
				switch (_objetoInterac.getID()) {
				case 7529: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 136: {
				switch (_objetoInterac.getID()) {
				case 7544: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 140: {
				switch (_objetoInterac.getID()) {
				case 7543: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 125: {
				switch (_objetoInterac.getID()) {
				case 7532: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 129: {
				switch (_objetoInterac.getID()) {
				case 7531: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 126: {
				switch (_objetoInterac.getID()) {
				case 7537: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 130: {
				switch (_objetoInterac.getID()) {
				case 7538: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 127: {
				switch (_objetoInterac.getID()) {
				case 7539: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 131: {
				switch (_objetoInterac.getID()) {
				case 7540: {
					return _objetoInterac.getEstado() == 1;
				}
				}
				return false;
			}
			case 27:
			case 109: {
				return _objetoInterac.getID() == 7001;
			}
			case 135: {
				return _objetoInterac.getID() == 7022;
			}
			case 134: {
				return _objetoInterac.getID() == 7023;
			}
			case 132: {
				return _objetoInterac.getID() == 7025;
			}
			case 157: {
				return _objetoInterac.getID() == 7030 || _objetoInterac.getID() == 7031;
			}
			case 44:
			case 114: {
				switch (_objetoInterac.getID()) {
				case 4287:
				case 7000:
				case 7026:
				case 7029: {
					return true;
				}
				}
				return false;
			}
			case 175:
			case 176:
			case 177:
			case 178: {
				switch (_objetoInterac.getID()) {
				case 6763:
				case 6766:
				case 6767:
				case 6772: {
					return true;
				}
				}
				return false;
			}
			case 183: {
				switch (_objetoInterac.getID()) {
				case 1845:
				case 1853:
				case 1854:
				case 1855:
				case 1856:
				case 1857:
				case 1858:
				case 1859:
				case 1860:
				case 1861:
				case 1862:
				case 2319: {
					return true;
				}
				}
				return false;
			}
			case 1:
			case 113:
			case 115:
			case 116:
			case 117:
			case 118:
			case 119:
			case 120: {
				return _objetoInterac.getID() == 7020;
			}
			case 18:
			case 19:
			case 20:
			case 21:
			case 65:
			case 66:
			case 67:
			case 142:
			case 143:
			case 144:
			case 145:
			case 146: {
				return _objetoInterac.getID() == 7012;
			}
			case 165:
			case 166:
			case 167: {
				return _objetoInterac.getID() == 7036;
			}
			case 163:
			case 164: {
				return _objetoInterac.getID() == 7037;
			}
			case 168:
			case 169: {
				return _objetoInterac.getID() == 7038;
			}
			case 171:
			case 182: {
				return _objetoInterac.getID() == 7039;
			}
			case 156: {
				return _objetoInterac.getID() == 7027;
			}
			case 13:
			case 14: {
				return _objetoInterac.getID() == 7011;
			}
			case 64:
			case 123: {
				return _objetoInterac.getID() == 7015;
			}
			case 15:
			case 16:
			case 17:
			case 147:
			case 148:
			case 149: {
				return _objetoInterac.getID() == 7013;
			}
			case 63: {
				return _objetoInterac.getID() == 7014 || _objetoInterac.getID() == 7016;
			}
			case 11:
			case 12: {
				return _objetoInterac.getID() >= 7008 && _objetoInterac.getID() <= 7010;
			}
			case 81:
			case 84:
			case 97:
			case 98:
			case 108: {
				return _objetoInterac.getID() >= 6700 && _objetoInterac.getID() <= 6776;
			}
			case 104:
			case 105: {
				return _objetoInterac.getID() >= 7350 && _objetoInterac.getID() <= 7353;
			}
			case 110: {
				return _objetoInterac.getID() == 7018;
			}
			case 170: {
				return _objetoInterac.getID() == 7035;
			}
			case 121:
			case 181: {
				return _objetoInterac.getID() == 7021;
			}
			case 152: {
				return _objetoInterac.getID() == 7549 && pescarKuakua;
			}
			case 150: {
				return _objetoInterac.getID() == 7546 || _objetoInterac.getID() == 7547;
			}
			case 153: {
				return _objetoInterac.getID() == 7352;
			}
			}
			System.out.println("Bug al verificar si se puede realizar la accion ID = " + accionID);
			return false;
		}

		public void iniciarAccion(Personagens perso, GameThread.AccionDeJuego GA) {
			int accionID = -1;
			int celdaID = -1;
			try {
				accionID = Integer.parseInt(GA._args.split(";")[1]);
				celdaID = Short.parseShort(GA._args.split(";")[0]);
			} catch (Exception exception) {
				// empty catch block
			}
			if (accionID == -1) {
				return;
			}
			if (Constantes.esTrabajo(accionID)) {
				perso.iniciarAccionOficio(accionID, _objetoInterac, GA, this);
				return;
			}
			switch (accionID) {
			case 44: {
				String str = String.valueOf(_mapaID) + "," + _id;
				perso.setSalvarZaap(str);
				SocketManager.ENVIAR_Im_INFORMACION(perso, "06");
				break;
			}
			case 102: {
				if (!_objetoInterac.esInteractivo() || _objetoInterac.getEstado() != 1) {
					return;
				}
				_objetoInterac.setEstado(2);
				_objetoInterac.setInteractivo(false);
				SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(perso.getMapa(), String.valueOf(GA._idUnica), 501,
						String.valueOf(perso.getID()), String.valueOf(_id) + "," + _objetoInterac.getDuracion() + ","
								+ _objetoInterac.getAnimacionPJ());
				SocketManager.ENVIAR_GDF_ESTADO_OBJETO_INTERACTIVO(perso.getMapa(), this);
				break;
			}
			case 114: {
				perso.abrirMenuZaap();
				perso.getCuenta().getEntradaPersonaje().borrarGA(GA);
				break;
			}
			case 152: {
				if (!_objetoInterac.esInteractivo() || _objetoInterac.getEstado() != 1) {
					return;
				}
				perso.setPescarKuakua(false);
				_objetoInterac.setEstado(2);
				_objetoInterac.setInteractivo(false);
				SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(perso.getMapa(), String.valueOf(GA._idUnica), 501,
						String.valueOf(perso.getID()), String.valueOf(_id) + "," + _objetoInterac.getDuracion() + ","
								+ _objetoInterac.getAnimacionPJ());
				SocketManager.ENVIAR_GDF_ESTADO_OBJETO_INTERACTIVO(perso.getMapa(), this);
				break;
			}
			case 157: {
				String s;
				int n;
				int n2;
				String[] arrstring;
				int precio;
				int cantidad;
				String[] Zaapis;
				String listaZaapi = "";
				if (perso.getMapa()._subArea.getArea().getID() == 7
						&& (perso.getAlineacion() == 1 || perso.getAlineacion() == 0 || perso.getAlineacion() == 3)) {
					Zaapis = "6159,4174,8758,4299,4180,8759,4183,2221,4300,4217,4098,8757,4223,8760,2214,4179,4229,4232,8478,4238,4263,4216,4172,4247,4272,4271,4250,4178,4106,4181,4259,4090,4262,4287,4300,4240,4218,4074,4308"
							.split(",");
					cantidad = 0;
					precio = 20;
					if (perso.getAlineacion() == 1) {
						precio = 10;
					}
					arrstring = Zaapis;
					n2 = Zaapis.length;
					for (n = 0; n < n2; ++n) {
						s = arrstring[n];
						listaZaapi = cantidad == Zaapis.length ? String.valueOf(listaZaapi) + s + ";" + precio
								: String.valueOf(listaZaapi) + s + ";" + precio + "|";
						++cantidad;
					}
					perso.setZaaping(true);
					SocketManager.ENVIAR_Wc_LISTA_ZAPPIS(perso, listaZaapi);
				}
				if (perso.getMapa()._subArea.getArea().getID() != 11
						|| perso.getAlineacion() != 2 && perso.getAlineacion() != 0 && perso.getAlineacion() != 3)
					break;
				Zaapis = "8756,8755,8493,5304,5311,5277,5317,4612,4618,5112,4639,4637,5116,5332,4579,4588,4549,4562,5334,5295,4646,4629,4601,4551,4607,4930,4622,4620,4615,4595,4627,4623,4604,8754,8753,4630"
						.split(",");
				cantidad = 0;
				precio = 20;
				if (perso.getAlineacion() == 2) {
					precio = 10;
				}
				arrstring = Zaapis;
				n2 = Zaapis.length;
				for (n = 0; n < n2; ++n) {
					s = arrstring[n];
					listaZaapi = cantidad == Zaapis.length ? String.valueOf(listaZaapi) + s + ";" + precio
							: String.valueOf(listaZaapi) + s + ";" + precio + "|";
					++cantidad;
				}
				perso.setZaaping(true);
				SocketManager.ENVIAR_Wc_LISTA_ZAPPIS(perso, listaZaapi);
				break;
			}
			case 175: {
				perso.abrirCercado();
				break;
			}
			case 176: {
				Cercado cercado = perso.getMapa().getCercado();
				if (cercado.getDue\u00f1o() == -1) {
					SocketManager.ENVIAR_Im_INFORMACION(perso, "196");
					return;
				}
				if (cercado.getPrecio() == 0) {
					SocketManager.ENVIAR_Im_INFORMACION(perso, "197");
					return;
				}
				if (perso.getGremio() == null) {
					SocketManager.ENVIAR_Im_INFORMACION(perso, "1135");
					return;
				}
				if (perso.getMiembroGremio().getRango() != 1) {
					SocketManager.ENVIAR_Im_INFORMACION(perso, "198");
					return;
				}
				SocketManager.ENVIAR_RD_COMPRAR_CERCADO(perso,
						String.valueOf(cercado.getPrecio()) + "|" + cercado.getPrecio());
				break;
			}
			case 177:
			case 178: {
				Cercado cercado1 = perso.getMapa().getCercado();
				if (cercado1.getDue\u00f1o() == -1) {
					SocketManager.ENVIAR_Im_INFORMACION(perso, "194");
					return;
				}
				if (cercado1.getDue\u00f1o() != perso.getID()) {
					SocketManager.ENVIAR_Im_INFORMACION(perso, "195");
					return;
				}
				SocketManager.ENVIAR_RD_COMPRAR_CERCADO(perso,
						String.valueOf(cercado1.getPrecio()) + "|" + cercado1.getPrecio());
				break;
			}
			case 183: {
				if (perso.getNivel() > 15) {
					SocketManager.ENVIAR_Im_INFORMACION(perso, "1127");
					perso.getCuenta().getEntradaPersonaje().borrarGA(GA);
					return;
				}
				short mapaID = Constantes.getMapaInicio(perso.getClase(true));
				short celdaId = 314;
				perso.teleport(mapaID, celdaId);
				perso.getCuenta().getEntradaPersonaje().borrarGA(GA);
				break;
			}
			case 81: {
				Casa casa1 = Casa.getCasaPorUbicacion(perso.getMapa().getID(), celdaID);
				if (casa1 == null) {
					return;
				}
				perso.setCasa(casa1);
				casa1.bloquear(perso);
				break;
			}
			case 84: {
				Casa casa2 = Casa.getCasaPorUbicacion(perso.getMapa().getID(), celdaID);
				if (casa2 == null) {
					return;
				}
				perso.setCasa(casa2);
				casa2.respondeA(perso);
				break;
			}
			case 97: {
				Casa casa3 = Casa.getCasaPorUbicacion(perso.getMapa().getID(), celdaID);
				if (casa3 == null) {
					return;
				}
				perso.setCasa(casa3);
				casa3.comprarEstaCasa(perso);
				break;
			}
			case 104: {
				Cofre cofre2 = Cofre.getCofrePorUbicacion(perso.getMapa().getID(), celdaID);
				if (cofre2 == null) {
					System.out.println("COFRE BUGEADO EN MAPA: " + perso.getMapa().getID() + " CELDAID : " + celdaID);
					return;
				}
				perso.setCofre(cofre2);
				cofre2.chekeadoPor(perso);
				break;
			}
			case 153: {
				Cofre cofre3 = Cofre.getCofrePorUbicacion(0, 0);
				perso.setCofre(cofre3);
				Cofre.abrirCofre(perso, "-", true);
				break;
			}
			case 105: {
				Cofre cofre = Cofre.getCofrePorUbicacion(perso.getMapa().getID(), celdaID);
				if (cofre == null) {
					System.out.println("COFRE BUGEADO EN MAPA: " + perso.getMapa().getID() + " CELDAID : " + celdaID);
					return;
				}
				perso.setCofre(cofre);
				cofre.bloquear(perso);
				break;
			}
			case 98:
			case 108: {
				Casa casa4 = Casa.getCasaPorUbicacion(perso.getMapa().getID(), celdaID);
				if (casa4 == null) {
					return;
				}
				perso.setCasa(casa4);
				casa4.venderla(perso);
				break;
			}
			case 170: {
				perso.setListaArtesanos(true);
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(perso, 14,
						"15;16;27;17;18;19;20;28;36;44;45;46;47;48;49;50;62;63;64;24;25;26;31;2;41;43;58");
				break;
			}
			case 121:
			case 181: {
				SocketManager.ENVIAR_GDF_FORZADO_PERSONAJE(perso, celdaID, 3, 1);
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(perso, 3, "1;181");
				perso.setRompiendo(true);
				break;
			}
			case 150: {
				_caminable = false;
				_objetoInterac.setEstado(2);
				_objetoInterac.setInteractivo(false);
				SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(perso.getMapa(), String.valueOf(GA._idUnica), 501,
						String.valueOf(perso.getID()), String.valueOf(_id) + "," + _objetoInterac.getDuracion() + ","
								+ _objetoInterac.getAnimacionPJ());
				try {
					Thread.sleep(1000L);
				} catch (Exception exception) {
					// empty catch block
				}
				SocketManager.ENVIAR_GDF_FORZADO_PERSONAJE(perso, celdaID, 3, 0);
				break;
			}
			default: {
				System.out.println("Bug al iniciar la accion ID = " + accionID);
			}
			}
		}

		public void finalizarAccion(Personagens perso, GameThread.AccionDeJuego GA) {
			int accionID = -1;
			try {
				accionID = Integer.parseInt(GA._args.split(";")[1]);
			} catch (Exception exception) {
				// empty catch block
			}
			if (accionID == -1) {
				return;
			}
			if (Constantes.esTrabajo(accionID)) {
				perso.finalizarAccionOficio(accionID, GA, this);
				return;
			}
			switch (accionID) {
			case 44:
			case 81:
			case 84:
			case 97:
			case 98:
			case 104:
			case 105:
			case 108:
			case 114:
			case 121:
			case 153:
			case 157:
			case 170:
			case 175:
			case 176:
			case 177:
			case 178:
			case 181:
			case 183: {
				break;
			}
			case 150: {
				_objetoInterac.setInteractivo(false);
				_objetoInterac.iniciarTiempoRefresco();
				_objetoInterac.setEstado(4);
				break;
			}
			case 102:
			case 152: {
				if (_objetoInterac == null) {
					return;
				}
				_objetoInterac.setInteractivo(false);
				_objetoInterac.iniciarTiempoRefresco();
				SocketManager.ENVIAR_GDF_ESTADO_OBJETO_INTERACTIVO(perso.getMapa(), this);
				Objeto obj = null;
				int cantidad = 1;
				if (accionID == 102) {
					cantidad = Fórmulas.getRandomValor(1, 10);
					obj = World.getObjModelo(311).crearObjDesdeModelo(cantidad, false);
				} else if (accionID == 152) {
					Random rand = new Random();
					int x = rand.nextInt(6);
					if (x == 5) {
						SocketManager.enviar(perso, "cS" + perso.getID() + "|11");
						obj = World.getObjModelo(6659).crearObjDesdeModelo(1, false);
					} else {
						SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Dommage , retente ta chance ! :)");
						SocketManager.enviar(perso, "cS" + perso.getID() + "|12");
						_objetoInterac.setEstado(4);
						break;
					}
				}
				if (!perso.addObjetoSimilar(obj, true, -1)) {
					World.addObjeto(obj, true);
					perso.addObjetoPut(obj);
					SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, obj);
				}
				SocketManager.ENVIAR_IQ_NUMERO_ARRIBA_PJ(perso, perso.getID(), cantidad);
				_objetoInterac.setEstado(4);
				break;
			}
			default: {
				System.out.println("Bug al izar la accion ID = " + accionID);
			}
			}
		}

		public void nullearCeldaAccion() {
			_celdaAccion = null;
		}

		public void addObjetoTirado(Objeto obj, Personagens objetivo) {
			_objetoTirado = obj;
			if (_id == 8575) {
				objetivo.getCuenta().setRango(5);
				SQLManager.SALVAR_CUENTA(objetivo.getCuenta());
			}
		}

		public void borrarObjetoTirado() {
			_objetoTirado = null;
		}
	}

	public static class Cercado {
		private int _propietario;
		private ObjetoInteractivo _antespuerta;
		private int _tama\u00f1o;
		private Guild _gremio;
		private Maps _mapa;
		private short _celda = (short) -1;
		private int _precio;
		private short _colocarcelda;
		private CopyOnWriteArrayList<Integer> _criando = new CopyOnWriteArrayList();
		private short _celdaPuerta;
		private ArrayList<Short> _celdasobjeto = new ArrayList();
		private int _cantObjetosMax;
		private Map<Short, Map<Integer, Integer>> _objetoscrianza = new TreeMap<Short, Map<Integer, Integer>>();
		private Map<Short, Integer> _celdaYObjeto = new TreeMap<Short, Integer>();

		public Cercado(int propietario, Maps mapa, short celdaId, int tama\u00f1o, int gremio, int precio,
				short colocarcelda, String criando, short celdaPuerta, String celdasobjeto, int cantobjetos,
				String objetoscrianza) {
			_propietario = propietario;
			_antespuerta = mapa.getPuertaCercado();
			_tama\u00f1o = tama\u00f1o;
			_gremio = World.getGremio(gremio);
			_mapa = mapa;
			_celda = celdaId;
			_precio = precio;
			_colocarcelda = colocarcelda;
			_celdaPuerta = celdaPuerta;
			_cantObjetosMax = cantobjetos;
			if (!objetoscrianza.isEmpty()) {
				String[] arrstring = objetoscrianza.split("\\|");
				int n = arrstring.length;
				for (int i = 0; i < n; ++i) {
					String objetos = arrstring[i];
					String[] infos = objetos.split(";");
					short celda = Short.parseShort(infos[0]);
					int objeto = Integer.parseInt(infos[1]);
					int due\u00f1o = Integer.parseInt(infos[2]);
					TreeMap<Integer, Integer> otro = new TreeMap<Integer, Integer>();
					otro.put(objeto, due\u00f1o);
					_celdaYObjeto.put(celda, objeto);
					_objetoscrianza.put(celda, otro);
				}
			}
			if (!celdasobjeto.isEmpty()) {
				for (String celda : celdasobjeto.split(";")) {
					short idCelda = Short.parseShort(celda);
					if (idCelda <= 0)
						continue;
					_celdasobjeto.add(idCelda);
				}
			}
			if (!criando.isEmpty()) {
				String[] dragopavos;
				String[] arrstring = dragopavos = criando.split(";");
				int n = dragopavos.length;
				for (int i = 0; i < n; ++i) {
					String pavo = arrstring[i];
					_criando.add(Integer.parseInt(pavo));
				}
			}
			if (_mapa != null) {
				_mapa.setCercado(this);
			}
		}

		public synchronized void startMoverDrago() {
			if (_criando.size() > 0) {
				char[] direcciones = new char[] { 'b', 'd', 'f', 'h' };
				for (Integer montura : _criando) {
					Dragossauros dragopavo = World.getDragopavoPorID(montura);
					if (dragopavo == null)
						continue;
					char dir = direcciones[Fórmulas.getRandomValor(0, 3)];
					dragopavo.moverMonturaAuto(dir, 3, false);
					try {
						Thread.sleep(300L);
					} catch (InterruptedException interruptedException) {
						// empty catch block
					}
				}
			}
		}

		public Map<Short, Integer> getCeldayObjeto() {
			return _celdaYObjeto;
		}

		public void setSizeyObjetos(int size, int objetos) {
			_tama\u00f1o = size;
			_cantObjetosMax = objetos;
		}

		public void addObjetoCria(short celda, int objeto, int due\u00f1o) {
			if (_objetoscrianza.containsKey(celda)) {
				_objetoscrianza.remove(celda);
				_celdaYObjeto.remove(celda);
			}
			TreeMap<Integer, Integer> otro = new TreeMap<Integer, Integer>();
			otro.put(objeto, due\u00f1o);
			_celdaYObjeto.put(celda, objeto);
			_objetoscrianza.put(celda, otro);
		}

		public boolean delObjetoCria(short celda) {
			if (!_objetoscrianza.containsKey(celda)) {
				return false;
			}
			_objetoscrianza.remove(celda);
			_celdaYObjeto.remove(celda);
			return true;
		}

		public String getStringObjetosCria() {
			String str = "";
			boolean primero = false;
			if (_objetoscrianza.size() == 0) {
				return str;
			}
			for (Map.Entry<Short, Map<Integer, Integer>> entry : _objetoscrianza.entrySet()) {
				if (primero) {
					str = String.valueOf(str) + "|";
				}
				str = String.valueOf(str) + entry.getKey();
				for (Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
					str = String.valueOf(str) + ";" + entry2.getKey() + ";" + entry2.getValue();
				}
				primero = true;
			}
			return str;
		}

		public int getCantObjColocados() {
			return _objetoscrianza.size();
		}

		public Map<Short, Map<Integer, Integer>> getObjetosColocados() {
			return _objetoscrianza;
		}

		public void addCeldaObj(short celda) {
			if (_celdasobjeto.contains(celda)) {
				return;
			}
			if (celda <= 0) {
				return;
			}
			_celdasobjeto.add(celda);
		}

		public void addCeldaMontura(short celda) {
			_colocarcelda = celda;
		}

		public int getCantObjMax() {
			return _cantObjetosMax;
		}

		public String getStringCeldasObj() {
			boolean primero = false;
			String str = "";
			if (_celdasobjeto.size() == 0) {
				return str;
			}
			for (short celda : _celdasobjeto) {
				if (primero) {
					str = String.valueOf(str) + ";";
				}
				str = String.valueOf(str) + celda;
				primero = true;
			}
			return str;
		}

		public ArrayList<Short> getCeldasObj() {
			return _celdasobjeto;
		}

		public short getColocarCelda() {
			return _colocarcelda;
		}

		public short getPuerta() {
			return _celdaPuerta;
		}

		public void setPuerta(short puerta) {
			_celdaPuerta = puerta;
		}

		public String getCriando() {
			String str = "";
			boolean primero = true;
			if (_criando.size() == 0) {
				return "";
			}
			for (Integer pavo : _criando) {
				if (!primero) {
					str = String.valueOf(str) + ";";
				}
				str = String.valueOf(str) + pavo;
				primero = false;
			}
			return str;
		}

		public void addCriando(int pavo) {
			_criando.add(pavo);
		}

		public void delCriando(int pavo) {
			if (_criando.contains(pavo)) {
				int index = _criando.indexOf(pavo);
				_criando.remove(index);
			}
		}

		public int cantCriando() {
			return _criando.size();
		}

		public CopyOnWriteArrayList<Integer> getListaCriando() {
			return _criando;
		}

		public int getDue\u00f1o() {
			return _propietario;
		}

		public void setPropietario(int AccID) {
			_propietario = AccID;
		}

		public ObjetoInteractivo get_door() {
			return _antespuerta;
		}

		public int getTama\u00f1o() {
			return _tama\u00f1o;
		}

		public Guild getGremio() {
			return _gremio;
		}

		public void setGremio(Guild gremio) {
			_gremio = gremio;
		}

		public Maps getMapa() {
			return _mapa;
		}

		public short getCeldaID() {
			return _celda;
		}

		public int getPrecio() {
			return _precio;
		}

		public void setPrecio(int price) {
			_precio = price;
		}

		public String parseData() {
			String str = "";
			return str;
		}
	}

	public static class ObjetoInteractivo {
		private int _id;
		private int _estado;
		private Maps _mapa;
		private Celda _celda;
		private boolean _interactivo = true;
		private Timer _tiempoRefrescar;
		private World.ObjInteractivoModelo _interacMod;

		public ObjetoInteractivo(Maps mapa, Celda celda, int id) {
			_id = id;
			_mapa = mapa;
			_celda = celda;
			_estado = 1;
			int tiempoRespuesta = 10000;
			_interacMod = World.getObjInteractivoModelo(_id);
			if (_interacMod != null) {
				tiempoRespuesta = _interacMod.getTiempoRespuesta();
			}
			_tiempoRefrescar = new Timer(tiempoRespuesta, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					_tiempoRefrescar.stop();
					_estado = 5;
					_interactivo = true;
					SocketManager.ENVIAR_GDF_ESTADO_OBJETO_INTERACTIVO(_mapa, _celda);
					_estado = 1;
				}
			});
		}

		public int getID() {
			return _id;
		}

		public boolean esInteractivo() {
			return _interactivo;
		}

		public void setInteractivo(boolean b) {
			_interactivo = b;
		}

		public int getEstado() {
			return _estado;
		}

		public void setEstado(int estado) {
			_estado = estado;
		}

		public int getDuracion() {
			int duracion = 1500;
			if (_interacMod != null) {
				duracion = _interacMod.getDuracion();
			}
			return duracion;
		}

		public int getAnimacionPJ() {
			int idAnimacion = 4;
			if (_interacMod != null) {
				idAnimacion = _interacMod.getAnimacionPJ();
			}
			return idAnimacion;
		}

		public boolean esCaminable() {
			if (_interacMod == null) {
				return false;
			}
			return _interacMod.esCaminable() && _estado == 1;
		}

		public void iniciarTiempoRefresco() {
			if (_tiempoRefrescar == null) {
				return;
			}
			_estado = 3;
			_tiempoRefrescar.restart();
		}
	}
}
