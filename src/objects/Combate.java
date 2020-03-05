package objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Timer;

import common.Constantes;
import common.CryptManager;
import common.Fórmulas;
import common.IA;
import common.LesGuardians;
import common.Pathfinding;
import common.SQLManager;
import common.SocketManager;
import common.Mundo;
import common.Mundo.Drop;

import game.GameThread;

import objects.PiedraAlma;
import objects.Recaudador;
import objects.Reto;
import objects.Gremio;
import objects.MobModelo;
import objects.MobModelo.MobGrado;
import objects.Mapa;
import objects.Mapa.Celda;
import objects.Objeto;
import objects.Objeto.ObjetoModelo;
import objects.Personaje;
import objects.Personaje.Stats;
import objects.Mascota;
import objects.Prisma;
import objects.RankingPVP;
import objects.Hechizo;
import objects.Hechizo.StatsHechizos;
import objects.EfectoHechizo;

public class Combate {
	private short _id;
	private Map<Integer, Luchador> _equipo1 = new TreeMap<Integer, Luchador>();
	private Map<Integer, Luchador> _equipo2 = new TreeMap<Integer, Luchador>();
	private Map<Integer, Luchador> _listaMuertos = new TreeMap<Integer, Luchador>();
	private Map<Integer, Personaje> _espectadores = new TreeMap<Integer, Personaje>();
	private Mapa _mapaCopia;
	private Mapa _mapaReal;
	private Luchador _luchInit1;
	private Luchador _luchInit2;
	private int _idLuchInit1;
	private int _idLuchInit2;
	private ArrayList<Celda> _celdasPos1 = new ArrayList<Celda>();
	private ArrayList<Celda> _celdasPos2 = new ArrayList<Celda>();
	private byte _estadoPelea = 0;
	private int _gremioID = -1;
	private byte _tipo = (byte) -1;
	private boolean _cerrado1 = false;
	private boolean _soloGrupo1 = false;
	private boolean _cerrado2 = false;
	private boolean _soloGrupo2 = false;
	private boolean _espectadorOk = true;
	private boolean _ayuda1 = false;
	private boolean _ayuda2 = false;
	private byte _celdaColor2;
	private byte _celdaColor1;
	private byte _nroOrdenLuc;
	private short _tempLuchadorPA;
	private short _tempLuchadorPM;
	private short _tempLuchadorPAusados;
	private short _tempLuchadorPMusados;
	private String _tempAccion = "";
	private List<Luchador> _ordenJugadores = new ArrayList<Luchador>();
	private Timer _tiempoTurno;
	private List<Glifo> _glifos = new ArrayList<Glifo>();
	private List<Trampa> _trampas = new ArrayList<Trampa>();
	private MobModelo.GrupoMobs _mobGrupo;
	private ArrayList<Luchador> _capturadores = new ArrayList<Luchador>(8);
	private boolean _esCapturable = false;
	private int _capturadorGanador = -1;
	private PiedraAlma _piedraAlma;
	private Recaudador _Recaudador;
	private Prisma _Prisma;
	private ArrayList<Integer> _mobsMuertosReto = new ArrayList<Integer>();
	private ArrayList<Integer> _muertesLuchInic1 = new ArrayList<Integer>();
	private ArrayList<Integer> _muertesLuchInic2 = new ArrayList<Integer>();
	private byte _cantLucEquipo1 = 0;
	private byte _cantLucEquipo2 = 1;
	public List<Luchador> _inicioLucEquipo1 = new ArrayList<Luchador>();
	public List<Luchador> _inicioLucEquipo2 = new ArrayList<Luchador>();
	private byte _numeroInvos = 0;
	private Map<Integer, Mapa.Celda> _posinicial = new TreeMap<Integer, Mapa.Celda>();
	private String _listadefensores = "";
	private Map<Integer, String> _stringReto = new TreeMap<Integer, String>();
	private int _idMobReto = 0;
	private int _luchMenorNivelReto = 0;
	private int _elementoReto = 0;
	private byte _cantMobsMuerto = 0;
	private Map<Integer, Integer> _ordenNivelMobs = new TreeMap<Integer, Integer>();
	private Map<Integer, Luchador> _ordenLuchMobs = new TreeMap<Integer, Luchador>();
	private Map<Integer, Integer> _retos = new TreeMap<Integer, Integer>();
	private short _estrellas = 0;
	private long _tiempoInicio = 0L;
	private long _tiempoInicioTurno = 0L;
	private int _misionPVP = 0;
	private boolean _evento = false;
	private byte _cantUltAfec = 0;
	private int _tiempoResetAccion = 0;
	private Timer _esperarTempAccion;
	private int _cantTimer = 0;
	private int _cantFinTurno = 0;
	private ArrayList<Objeto> _objetosRobados = new ArrayList<Objeto>();
	private long _kamasRobadas = 0L;

	public void setUltAfec(byte afec) {
		_cantUltAfec = afec;
	}

	public void setEvento(boolean even) {
		_evento = even;
	}

	public Map<Integer, Integer> getRetos() {
		return _retos;
	}

	public synchronized void tiempoTurno() {
		if (_tiempoInicio == 0L) {
			long tiempoRestante = 44000L - (System.currentTimeMillis() - _tiempoInicioTurno);
			if (tiempoRestante <= 0L) {
				try {
					iniciarPelea();
				} catch (Exception e) {
					SocketManager.ENVIAR_cMK_CHAT_MENSAJE_ADMINS("@", 0, "BUG-PELEA",
							"El mapa " + _mapaReal.getID() + " no inicia la pelea, ir a debugearla.");
				}
				if (_Recaudador != null) {
					_Recaudador.setTiempoTurno(45000);
				} else if (_Prisma != null) {
					_Prisma.setTiempoTurno(45000);
				}
			} else if (_Recaudador != null) {
				_Recaudador.setTiempoTurno((int) tiempoRestante);
			} else if (_Prisma != null) {
				_Prisma.setTiempoTurno((int) tiempoRestante);
			}
		} else {
			long system = System.currentTimeMillis();
			if (system - _tiempoInicioTurno >= (long) (LesGuardians.TIEMPO_PELEA - 1000)) {
				_tiempoInicioTurno = system;
				finTurno(null);
			}
		}
	}

	public void putStringReto(int id, String reto) {
		_stringReto.put(id, reto);
	}

	public void setListaDefensores(String str) {
		_listadefensores = str;
	}

	public String getListaDefensores() {
		return _listadefensores;
	}

	public Map<Integer, Mapa.Celda> getPosInicial() {
		return _posinicial;
	}

	public int getNumeroInvos() {
		return _numeroInvos;
	}

	public void setIDMobReto(int mob) {
		_idMobReto = mob;
	}

	public int getIDMobReto() {
		return _idMobReto;
	}

	public Combate(short id, Mapa mapa, Personaje init1, Personaje init2, byte tipo) {
		try {
			_tipo = tipo;
			_id = id;
			_mapaCopia = mapa.copiarMapa();
			_mapaReal = mapa;
			int id1 = init1.getID();
			int id2 = init2.getID();
			_luchInit1 = new Luchador(this, init1);
			_luchInit2 = new Luchador(this, init2);
			_idLuchInit1 = _luchInit1.getID();
			_idLuchInit2 = _luchInit2.getID();
			_equipo1.put(id1, _luchInit1);
			_equipo2.put(id2, _luchInit2);
			_tiempoInicioTurno = System.currentTimeMillis();
			SocketManager.ENVIAR_GJK_UNIRSE_PELEA(this, 3, 2, _tipo == 0, true, false, _tipo == 0 ? 0 : 45000, _tipo);
			SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(init1, 0);
			SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(init2, 0);
			if (_tipo != 0) {
				_tiempoTurno = new Timer(1000, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (_tiempoInicio == 0L) {
							tiempoTurno();
						} else {
							_tiempoTurno.stop();
						}
					}
				});
				_tiempoTurno.start();
			}
			Random equipos = new Random();
			if (equipos.nextBoolean()) {
				_celdasPos1 = analizarPosiciones(0);
				_celdasPos2 = analizarPosiciones(1);
				SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 0);
				SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 2, _mapaCopia.getPosicionesPelea(), 1);
				_celdaColor1 = 0;
				_celdaColor2 = 1;
			} else {
				_celdasPos1 = analizarPosiciones(1);
				_celdasPos2 = analizarPosiciones(0);
				_celdaColor1 = 1;
				_celdaColor2 = 0;
				SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 1);
				SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 2, _mapaCopia.getPosicionesPelea(), 0);
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id1),
					String.valueOf(id1) + "," + 8 + ",0");
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id1),
					String.valueOf(id1) + "," + 3 + ",0");
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id2),
					String.valueOf(id2) + "," + 8 + ",0");
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id2),
					String.valueOf(id2) + "," + 3 + ",0");
			_luchInit1.setCeldaPelea(getCeldaRandom(_celdasPos1));
			_luchInit2.setCeldaPelea(getCeldaRandom(_celdasPos2));
			init1.getCelda().removerPersonaje(id1);
			init2.getCelda().removerPersonaje(id2);
			_luchInit1.getCeldaPelea().addLuchador(_luchInit1);
			_luchInit2.getCeldaPelea().addLuchador(_luchInit2);
			init1.setPelea(this);
			_luchInit1.setEquipoBin(0);
			init2.setPelea(this);
			_luchInit2.setEquipoBin(1);
			Mapa mapaTemp = init1.getMapa();
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapaTemp, id1);
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapaTemp, id2);
			if (_tipo == 1) {
				SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_EN_MAPA(mapaTemp, 0, id1, id2, init1.getCelda().getID(),
						"0;" + init1.getAlineacion(), init2.getCelda().getID(), "0;" + init2.getAlineacion());
			} else {
				SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_EN_MAPA(mapaTemp, 0, id1, id2, init1.getCelda().getID(), "0;-1",
						init2.getCelda().getID(), "0;-1");
			}
			SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(mapaTemp, id1, _luchInit1);
			SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(mapaTemp, id2, _luchInit2);
			SocketManager.ENVIAR_GM_LUCHADOR_A_TODA_PELEA(this, 7, _mapaCopia);
			setEstado((byte) 2);
		} catch (Exception e) {
			return;
		}
	}

	public Combate(short id, Mapa mapa, Personaje perso, MobModelo.GrupoMobs grupoMob, byte tipo) {
		_tipo = tipo;
		_id = id;
		_mapaCopia = mapa.copiarMapa();
		_mapaReal = mapa;
		_tiempoInicioTurno = System.currentTimeMillis();
		int id1 = perso.getID();
		_luchInit1 = new Luchador(this, perso);
		_idLuchInit1 = _luchInit1.getID();
		_mobGrupo = grupoMob;
		_idLuchInit2 = _mobGrupo.getID();
		_equipo1.put(id1, _luchInit1);
		for (Map.Entry<Integer, MobModelo.MobGrado> entry : grupoMob.getMobs().entrySet()) {
			entry.getValue().setIdEnPelea(entry.getKey());
			Luchador mob = new Luchador(this, entry.getValue());
			_equipo2.put(entry.getKey(), mob);
		}
		if (tipo == 4) {
			_estrellas = grupoMob.getEstrellas();
		}
		_tiempoTurno = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_tiempoInicio == 0L) {
					tiempoTurno();
				} else {
					_tiempoTurno.stop();
				}
			}
		});
		_tiempoTurno.start();
		SocketManager.ENVIAR_GJK_UNIRSE_PELEA(this, 1, 2, false, true, false, 45000, _tipo);
		SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(perso, 0);
		if (_equipo2.size() <= 2) {
			_celdasPos1 = analizarPosiciones(0);
			_celdasPos2 = analizarPosiciones(1);
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 0);
			_celdaColor1 = 0;
			_celdaColor2 = 1;
		} else {
			Random equipos = new Random();
			if (equipos.nextBoolean()) {
				_celdasPos1 = analizarPosiciones(0);
				_celdasPos2 = analizarPosiciones(1);
				SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 0);
				_celdaColor1 = 0;
				_celdaColor2 = 1;
			} else {
				_celdasPos1 = analizarPosiciones(1);
				_celdasPos2 = analizarPosiciones(0);
				_celdaColor1 = 1;
				_celdaColor2 = 0;
				SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 1);
			}
		}
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id1), String.valueOf(id1) + "," + 8 + ",0");
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id1), String.valueOf(id1) + "," + 3 + ",0");
		ArrayList<Map.Entry<Integer, Luchador>> equipo2 = new ArrayList<Map.Entry<Integer, Luchador>>();
		equipo2.addAll(_equipo2.entrySet());
		for (Entry<Integer, Luchador> entry : equipo2) {
			Luchador mob = (Luchador) entry.getValue();
			Mapa.Celda celdaRandom = getCeldaRandom(_celdasPos2);
			if (celdaRandom == null) {
				_equipo2.remove(mob.getID());
				continue;
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(mob.getID()),
					String.valueOf(mob.getID()) + "," + 8 + ",0");
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(mob.getID()),
					String.valueOf(mob.getID()) + "," + 3 + ",0");
			mob.setCeldaPelea(celdaRandom);
			mob.getCeldaPelea().addLuchador(mob);
			mob.setEquipoBin(1);
			mob.fullPDV();
		}
		_luchInit1.setCeldaPelea(getCeldaRandom(_celdasPos1));
		perso.getCelda().removerPersonaje(id1);
		_luchInit1.getCeldaPelea().addLuchador(_luchInit1);
		perso.setPelea(this);
		_luchInit1.setEquipoBin(0);
		Mapa maps = perso.getMapa();
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(maps, id1);
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(maps, grupoMob.getID());
		if (tipo == 4) {
			SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_EN_MAPA(maps, 4, id1, grupoMob.getID(), perso.getCelda().getID(),
					"0;-1", grupoMob.getCeldaID() - 1, "1;-1");
			SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(maps, id1, _luchInit1);
			for (Luchador luchador : _equipo2.values()) {
				SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(maps, grupoMob.getID(), luchador);
			}
		}
		SocketManager.ENVIAR_GM_LUCHADOR_A_TODA_PELEA(this, 7, _mapaCopia);
		setEstado((byte) 2);
	}

	public Combate(short id, Mapa mapa, Personaje perso, Recaudador recaudador) {
		setGremioID(recaudador.getGremioID());
		recaudador.setEstadoPelea((byte) 1);
		recaudador.setPeleaID(id);
		recaudador.setPelea(this);
		_tipo = (byte) 5;
		_id = id;
		_mapaCopia = mapa.copiarMapa();
		_mapaReal = mapa;
		_tiempoInicioTurno = System.currentTimeMillis();
		_luchInit1 = new Luchador(this, perso);
		_idLuchInit1 = _luchInit1.getID();
		_Recaudador = recaudador;
		_idLuchInit2 = _Recaudador.getID();
		int id1 = perso.getID();
		_equipo1.put(id1, _luchInit1);
		Luchador lRecaudador = new Luchador(this, recaudador);
		_equipo2.put(-1, lRecaudador);
		SocketManager.ENVIAR_GJK_UNIRSE_PELEA(this, 1, 2, false, true, false, 45000, _tipo);
		SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(perso, 0);
		_tiempoTurno = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_tiempoInicio == 0L) {
					tiempoTurno();
				} else {
					_tiempoTurno.stop();
				}
			}
		});
		_tiempoTurno.start();
		Random equipos = new Random();
		if (equipos.nextBoolean()) {
			_celdasPos1 = analizarPosiciones(0);
			_celdasPos2 = analizarPosiciones(1);
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 0);
			_celdaColor1 = 0;
			_celdaColor2 = 1;
		} else {
			_celdasPos1 = analizarPosiciones(1);
			_celdasPos2 = analizarPosiciones(0);
			_celdaColor1 = 1;
			_celdaColor2 = 0;
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 1);
		}
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id1), String.valueOf(id1) + "," + 8 + ",0");
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id1), String.valueOf(id1) + "," + 3 + ",0");
		ArrayList<Map.Entry<Integer, Luchador>> equipo2 = new ArrayList<Map.Entry<Integer, Luchador>>();
		equipo2.addAll(_equipo2.entrySet());
		for (Entry<Integer, Luchador> entry : equipo2) {
			Luchador recau = (Luchador) entry.getValue();
			Mapa.Celda celdaRandom = getCeldaRandom(_celdasPos2);
			if (celdaRandom == null) {
				_equipo2.remove(recau.getID());
				continue;
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(recau.getID()),
					String.valueOf(recau.getID()) + "," + 8 + ",0");
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(recau.getID()),
					String.valueOf(recau.getID()) + "," + 3 + ",0");
			recau.setCeldaPelea(celdaRandom);
			recau.getCeldaPelea().addLuchador(recau);
			recau.setEquipoBin(1);
			recau.fullPDV();
		}
		_luchInit1.setCeldaPelea(getCeldaRandom(_celdasPos1));
		perso.getCelda().removerPersonaje(id1);
		_luchInit1.getCeldaPelea().addLuchador(_luchInit1);
		perso.setPelea(this);
		_luchInit1.setEquipoBin(0);
		Mapa maps = perso.getMapa();
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(maps, _idLuchInit1);
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(maps, recaudador.getID());
		SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_EN_MAPA(maps, 5, id1, recaudador.getID(), perso.getCelda().getID(),
				"0;-1", recaudador.getCeldalID(), "3;-1");
		SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(maps, id1, _luchInit1);
		for (Luchador luchador : _equipo2.values()) {
			SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(maps, recaudador.getID(), luchador);
		}
		SocketManager.ENVIAR_GM_LUCHADOR_A_TODA_PELEA(this, 7, _mapaCopia);
		setEstado((byte) 2);
		String str = "";
		if (_Recaudador != null) {
			str = "A" + _Recaudador.getN1() + "," + _Recaudador.getN2() + "|.|" + _Recaudador.getMapaID() + "|"
					+ _Recaudador.getCeldalID();
		}
		for (Personaje z : Mundo.getGremio(_gremioID).getPjMiembros()) {
			if (z == null || !z.enLinea())
				continue;
			SocketManager.ENVIAR_gITM_INFO_RECAUDADOR(z, Recaudador.analizarRecaudadores(z.getGremio().getID()));
			Recaudador.analizarAtaque(z, _gremioID);
			Recaudador.analizarDefensa(z, _gremioID);
			SocketManager.ENVIAR_gA_MENSAJE_SOBRE_RECAUDADOR(z, str);
		}
	}

	public Combate(short id, Mapa mapa, Personaje perso, Prisma prisma) {
		prisma.setEstadoPelea((byte) 0);
		prisma.setPeleaID(id);
		prisma.setPelea(this);
		_tipo = (byte) 2;
		_id = id;
		_mapaCopia = mapa.copiarMapa();
		_mapaReal = mapa;
		_tiempoInicioTurno = System.currentTimeMillis();
		_luchInit1 = new Luchador(this, perso);
		_idLuchInit1 = _luchInit1.getID();
		_Prisma = prisma;
		_idLuchInit2 = _Prisma.getID();
		int id1 = perso.getID();
		_equipo1.put(id1, _luchInit1);
		Luchador lPrisma = new Luchador(this, prisma);
		_equipo2.put(-1, lPrisma);
		SocketManager.ENVIAR_GJK_UNIRSE_PELEA(this, 1, 2, false, true, false, 45000, _tipo);
		SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(perso, 0);
		_tiempoTurno = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_tiempoInicio == 0L) {
					tiempoTurno();
				} else {
					_tiempoTurno.stop();
				}
			}
		});
		_tiempoTurno.start();
		Random equipos = new Random();
		if (equipos.nextBoolean()) {
			_celdasPos1 = analizarPosiciones(0);
			_celdasPos2 = analizarPosiciones(1);
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 0);
			_celdaColor1 = 0;
			_celdaColor2 = 1;
		} else {
			_celdasPos1 = analizarPosiciones(1);
			_celdasPos2 = analizarPosiciones(0);
			_celdaColor1 = 1;
			_celdaColor2 = 0;
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 1);
		}
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id1), String.valueOf(id1) + "," + 8 + ",0");
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(id1), String.valueOf(id1) + "," + 3 + ",0");
		ArrayList<Map.Entry<Integer, Luchador>> equipo2 = new ArrayList<Map.Entry<Integer, Luchador>>();
		equipo2.addAll(_equipo2.entrySet());
		for (Entry<Integer, Luchador> entry : equipo2) {
			Luchador lprisma = (Luchador) entry.getValue();
			Mapa.Celda celdaRandom = getCeldaRandom(_celdasPos2);
			if (celdaRandom == null) {
				_equipo2.remove(lprisma.getID());
				continue;
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(lprisma.getID()),
					String.valueOf(lprisma.getID()) + "," + 8 + ",0");
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(lprisma.getID()),
					String.valueOf(lprisma.getID()) + "," + 3 + ",0");
			lprisma.setCeldaPelea(celdaRandom);
			lprisma.getCeldaPelea().addLuchador(lprisma);
			lprisma.setEquipoBin(1);
			lprisma.fullPDV();
		}
		_luchInit1.setCeldaPelea(getCeldaRandom(_celdasPos1));
		perso.getCelda().removerPersonaje(id1);
		_luchInit1.getCeldaPelea().addLuchador(_luchInit1);
		perso.setPelea(this);
		_luchInit1.setEquipoBin(0);
		Mapa maps = perso.getMapa();
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(maps, id1);
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(maps, prisma.getID());
		SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_EN_MAPA(maps, 0, id1, prisma.getID(), perso.getCelda().getID(),
				"0;" + perso.getAlineacion(), prisma.getCeldaID(), "0;" + prisma.getAlineacion());
		SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(maps, id1, _luchInit1);
		for (Luchador f : _equipo2.values()) {
			SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(maps, prisma.getID(), f);
		}
		SocketManager.ENVIAR_GM_LUCHADOR_A_TODA_PELEA(this, 7, _mapaCopia);
		setEstado((byte) 2);
		String str = "";
		if (_Prisma != null) {
			str = String.valueOf(prisma.getMapaID()) + "|" + prisma.getX() + "|" + prisma.getY();
		}
		for (Personaje z : Mundo.getPJsEnLinea()) {
			if (z == null || z.getAlineacion() != prisma.getAlineacion())
				continue;
			SocketManager.ENVIAR_CA_MENSAJE_ATAQUE_PRISMA(z, str);
		}
	}

	public Combate(short id, Mapa mapa, ArrayList<Personaje> grupo1, ArrayList<Personaje> grupo2) {
		Mapa mapaTemp;
		_tipo = (byte) 6;
		_id = id;
		_mapaCopia = mapa.copiarMapa();
		_mapaReal = mapa;
		_tiempoInicioTurno = System.currentTimeMillis();
		for (Personaje persos : grupo1) {
			persos.setPelea(this);
			Luchador pj1 = new Luchador(this, persos);
			_equipo1.put(persos.getID(), pj1);
			SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(persos, 0);
		}
		for (Personaje persos : grupo2) {
			persos.setPelea(this);
			Luchador pj2 = new Luchador(this, persos);
			_equipo2.put(persos.getID(), pj2);
			SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(persos, 0);
		}
		SocketManager.ENVIAR_GJK_UNIRSE_PELEA(this, 3, 2, false, true, false, 45000, _tipo);
		_tiempoTurno = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_tiempoInicio == 0L) {
					tiempoTurno();
				} else {
					_tiempoTurno.stop();
				}
			}
		});
		_tiempoTurno.start();
		ArrayList<Map.Entry<Integer, Luchador>> equipo1 = new ArrayList<Map.Entry<Integer, Luchador>>();
		equipo1.addAll(_equipo1.entrySet());
		ArrayList<Map.Entry<Integer, Luchador>> equipo2 = new ArrayList<Map.Entry<Integer, Luchador>>();
		equipo2.addAll(_equipo2.entrySet());
		Random equipos = new Random();
		if (equipos.nextBoolean()) {
			_celdasPos1 = analizarPosiciones(0);
			_celdasPos2 = analizarPosiciones(1);
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 0);
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 2, _mapaCopia.getPosicionesPelea(), 1);
			_celdaColor1 = 0;
			_celdaColor2 = 1;
		} else {
			_celdasPos1 = analizarPosiciones(1);
			_celdasPos2 = analizarPosiciones(0);
			_celdaColor1 = 1;
			_celdaColor2 = 0;
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 1, _mapaCopia.getPosicionesPelea(), 1);
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(this, 2, _mapaCopia.getPosicionesPelea(), 0);
		}
		for (Entry<Integer, Luchador> entry : equipo2) {
			Luchador lucha = (Luchador) entry.getValue();
			Mapa.Celda celdaRandom = getCeldaRandom(_celdasPos2);
			if (celdaRandom == null) {
				_equipo2.remove(lucha.getID());
				continue;
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(lucha.getID()),
					String.valueOf(lucha.getID()) + "," + 8 + ",0");
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(lucha.getID()),
					String.valueOf(lucha.getID()) + "," + 3 + ",0");
			lucha.setCeldaPelea(celdaRandom);
			lucha.getCeldaPelea().addLuchador(lucha);
			lucha.setEquipoBin(1);
		}
		for (Personaje personagens : grupo1) {
			mapaTemp = personagens.getMapa();
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapaTemp, personagens.getID());
		}
		for (Personaje personagens : grupo2) {
			mapaTemp = personagens.getMapa();
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapaTemp, personagens.getID());
		}
		int n = _equipo1.get(0).getID();
		int grupoID2 = _equipo2.get(0).getID();
		for (Luchador luchador : _equipo1.values()) {
			SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(mapa, n, luchador);
		}
		for (Luchador luchador : _equipo2.values()) {
			SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(mapa, grupoID2, luchador);
		}
		SocketManager.ENVIAR_GM_LUCHADOR_A_TODA_PELEA(this, 7, _mapaCopia);
		setEstado((byte) 2);
	}

	public Mapa getMapaCopia() {
		return _mapaCopia;
	}

	public List<Trampa> getTrampas() {
		return _trampas;
	}

	public List<Glifo> getGlifos() {
		return _glifos;
	}

	private Mapa.Celda getCeldaRandom(List<Mapa.Celda> celdas) {
		Mapa.Celda celda;
		Random rand = new Random();
		CopyOnWriteArrayList<Mapa.Celda> celdas2 = new CopyOnWriteArrayList<Mapa.Celda>();
		celdas2.addAll(celdas);
		if (celdas2.isEmpty()) {
			return null;
		}
		int limit = 0;
		if (celdas2.size() == 1) {
			Mapa.Celda celda2 = (Mapa.Celda) celdas2.get(0);
			if (celda2 == null || !celda2.getLuchadores().isEmpty()) {
				return null;
			}
			return celda2;
		}
		do {
			int size;
			if ((size = celdas2.size()) == 1) {
				celda = (Mapa.Celda) celdas2.get(0);
				continue;
			}
			int id = rand.nextInt(size - 1);
			celda = (Mapa.Celda) celdas2.get(id);
			if (celda.getLuchadores().isEmpty())
				continue;
			celdas2.remove(id);
		} while ((celda == null || !celda.getLuchadores().isEmpty()) && ++limit < 80);
		if (limit == 80) {
			return null;
		}
		return celda;
	}

	private ArrayList<Mapa.Celda> analizarPosiciones(int num) {
		return CryptManager.analizarInicioCelda(_mapaCopia, num);
	}

	public short getID() {
		return _id;
	}

	public ArrayList<Luchador> luchadoresDeEquipo(int equipos) {
		ArrayList<Luchador> luchadores = new ArrayList<Luchador>();
		if (equipos - 4 >= 0) {
			for (Map.Entry<Integer, Personaje> entry : _espectadores.entrySet())
				luchadores.add(new Luchador(this, entry.getValue()));
			equipos -= 4;
		}
		if (equipos - 2 >= 0) {
			for (Map.Entry<Integer, Luchador> entry : _equipo2.entrySet())
				luchadores.add(entry.getValue());
			equipos -= 2;
		}
		if (equipos - 1 >= 0)
			for (Map.Entry<Integer, Luchador> entry : _equipo1.entrySet())
				luchadores.add(entry.getValue());
		return luchadores;
	}

	public synchronized void cambiarLugar(Personaje perso, short celda) {
		Luchador luchador = getLuchadorPorPJ(perso);
		int equipo = getParamEquipo(perso.getID()) - 1;
		if (luchador == null) {
			return;
		}
		if (getEstado() != 2 || celdaOcupada(celda) || perso.estaListo()
				|| equipo == 0 && !grupoCeldasContiene(_celdasPos1, celda)
				|| equipo == 1 && !grupoCeldasContiene(_celdasPos2, celda)) {
			return;
		}
		if (_mapaCopia.getCelda(celda) == null) {
			return;
		}
		luchador.getCeldaPelea().getLuchadores().clear();
		luchador.setCeldaPelea(_mapaCopia.getCelda(celda));
		_mapaCopia.getCelda(celda).addLuchador(luchador);
		SocketManager.ENVIAR_GIC_CAMBIAR_POS_PELEA(this, 3, _mapaCopia, perso.getID(), celda);
	}

	public boolean celdaOcupada(short celda) {
		Mapa.Celda celd = _mapaCopia.getCelda(celda);
		if (celd == null) {
			return true;
		}
		return celd.getLuchadores().size() > 0;
	}

	private boolean grupoCeldasContiene(ArrayList<Mapa.Celda> celdas, int celda) {
		for (int a = 0; a < celdas.size(); ++a) {
			if (celdas.get(a).getID() != celda)
				continue;
			return true;
		}
		return false;
	}

	public void verificaTodosListos() {
		int a;
		boolean val = true;
		for (a = 0; a < _equipo1.size(); ++a) {
			if (_equipo1.get(_equipo1.keySet().toArray()[a]).getPersonaje().estaListo())
				continue;
			val = false;
		}
		if (_tipo != 4 && _tipo != 5 && _tipo != 2 && _tipo != 3) {
			for (a = 0; a < _equipo2.size(); ++a) {
				if (_equipo2.get(_equipo2.keySet().toArray()[a]).getPersonaje().estaListo())
					continue;
				val = false;
			}
		}
		if (_tipo == 5 || _tipo == 2) {
			val = false;
		}
		if (val) {
			iniciarPelea();
		}
	}

	private void iniciarPelea() {
		if (_estadoPelea >= 3)
			return;
		_estadoPelea = 3;
		_tiempoInicio = System.currentTimeMillis();
		_tiempoInicioTurno = 0L;
		if (_tipo == 5) {
			_Recaudador.setEstadoPelea((byte) 2);
			for (Personaje z : Mundo.getGremio(_gremioID).getPjMiembros()) {
				if (z == null)
					continue;
				if (z.enLinea()) {
					SocketManager.ENVIAR_gITM_INFO_RECAUDADOR(z,
							Recaudador.analizarRecaudadores(z.getGremio().getID()));
					Recaudador.analizarAtaque(z, _gremioID);
					Recaudador.analizarDefensa(z, _gremioID);
				}
			}
		} else if (_tipo == 2) {
			_Prisma.setEstadoPelea((byte) -2);
			for (Personaje z : Mundo.getPJsEnLinea()) {
				if (z == null)
					continue;
				if (z.getAlineacion() == _Prisma.getAlineacion()) {
					Prisma.analizarAtaque(z);
					Prisma.analizarDefensa(z);
				}
			}
		} else if (_tipo == 1) {
			if (_equipo1.size() == 1) {
				Personaje init1 = _luchInit1.getPersonaje();
				if (init1 != null && init1.getMisionPVP() != null) {
					String victima = "";
					try {
						victima = init1.getMisionPVP().getPjMision().getNombre();
					} catch (NullPointerException e) {
						victima = "";
					}
					for (Luchador luchador : _equipo2.values()) {
						if (luchador.getPersonaje().getNombre().equalsIgnoreCase(victima))
							_misionPVP = 1;
					}
				}
			} else if (_tipo == 4 && _mobGrupo.esPermanente()) {
				_mapaReal.addGrupoMobPermanente(_mobGrupo.getCeldaID(), _mobGrupo.getStrGrupoMob());
			}
			if (_equipo2.size() == 1) {
				Personaje init2 = _luchInit2.getPersonaje();
				if (init2 != null && init2.getMisionPVP() != null) {
					String victima = "";
					try {
						victima = init2.getMisionPVP().getPjMision().getNombre();
					} catch (NullPointerException e) {
						victima = "";
					}
					for (Luchador luchador : _equipo1.values()) {
						if (luchador.getPersonaje().getNombre().equalsIgnoreCase(victima))
							_misionPVP = 2;
					}
				}
			}
		}
		SocketManager.ENVIAR_Gc_BORRAR_ESPADA_EN_MAPA(_mapaReal, _idLuchInit1);
		SocketManager.ENVIAR_GIC_UBICACION_LUCHADORES_INICIAR(this, 7);
		SocketManager.ENVIAR_GS_EMPEZAR_COMBATE_EQUIPOS(this, 7);
		iniciarOrdenLuchadores();
		_nroOrdenLuc = -1;
		SocketManager.ENVIAR_GTL_ORDEN_JUGADORES(this, 7);
		SocketManager.ENVIAR_GTM_INFO_STATS_TODO_LUCHADORES(this, 7);
		if (_tipo == 4 || _tipo == 3) {
			ArrayList<Integer> retosPosibles = new ArrayList<Integer>();
			for (int i = 1; i < 51; i++) {
				if (i != 13 && i != 16 && i != 19 && i != 26 && i != 27 && i != 38 && i != 48 && i != 49)
					if (Constantes.esRetoPosible1(i, this))
						retosPosibles.add(Integer.valueOf(i));
			}
			int idReto = ((Integer) retosPosibles.get(Fórmulas.getRandomValor(0, retosPosibles.size() - 1))).intValue();
			_retos.put(Integer.valueOf(idReto), Integer.valueOf(0));
			SocketManager.ENVIAR_Gd_RETO_A_LOS_LUCHADORES(this, Mundo.getReto(idReto).getDetalleReto(this));
			if (_mapaReal.esArena() || _mapaReal.esMazmorra()) {
				idReto = ((Integer) retosPosibles.get(Fórmulas.getRandomValor(0, retosPosibles.size() - 1))).intValue();
				boolean repetir = true;
				while (repetir) {
					repetir = false;
					idReto = ((Integer) retosPosibles.get(Fórmulas.getRandomValor(0, retosPosibles.size() - 1)))
							.intValue();
					for (Integer nro : _retos.keySet()) {
						if (Constantes.esRetoPosible2(nro.intValue(), idReto) && !repetir) {
							repetir = false;
							continue;
						}
						repetir = true;
					}
				}
				_retos.put(Integer.valueOf(idReto), Integer.valueOf(0));
				SocketManager.ENVIAR_Gd_RETO_A_LOS_LUCHADORES(this, Mundo.getReto(idReto).getDetalleReto(this));
			}
		}
		for (Luchador luchador : luchadoresDeEquipo(3)) {
			_posinicial.put(Integer.valueOf(luchador.getID()), luchador.getCeldaPelea());
			Personaje perso = luchador.getPersonaje();
			if (perso == null)
				continue;
			if (perso.estaMontando())
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950,
						(new StringBuilder(String.valueOf(perso.getID()))).toString(),
						String.valueOf(perso.getID()) + "," + '\013' + ",1");
			if (perso.getPDV() != luchador.getPDVConBuff()) {
				luchador.setPDV(perso.getPDV());
				luchador.setPDVMAX(perso.getPDVMAX());
			}
			luchador._totalStats = perso.getTotalStats();
		}
		_cantLucEquipo1 = (byte) luchadoresDeEquipo(1).size();
		_cantLucEquipo2 = (byte) luchadoresDeEquipo(2).size();
		_inicioLucEquipo1.addAll(luchadoresDeEquipo(1));
		_inicioLucEquipo2.addAll(luchadoresDeEquipo(2));
		try {
			Thread.sleep(200L);
		} catch (Exception exception) {
		}
		if (_tipo == 4 || _tipo == 3) {
			Map<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
			copiaRetos.putAll(_retos);
			for (Map.Entry<Integer, Integer> entry : copiaRetos.entrySet()) {
				ArrayList<Luchador> temporal;
				int reto = ((Integer) entry.getKey()).intValue();
				int exitoReto = ((Integer) entry.getValue()).intValue();
				if (exitoReto != 0)
					continue;
				int idLuch = 0;
				int size2 = _equipo2.size();
				int nivel = 10000;
				Map<Integer, Integer> ordenNivelMobs = new TreeMap<Integer, Integer>();
				Map<Integer, Luchador> ordenLuchMobs = new TreeMap<Integer, Luchador>();
				switch (reto) {
				case 30:
					for (Luchador luch : _equipo1.values()) {
						if (luch.getNivel() < nivel) {
							_luchMenorNivelReto = luch.getID();
							nivel = luch.getNivel();
						}
					}
				case 10:
					while (ordenNivelMobs.size() < size2) {
						nivel = 10000;
						for (Luchador luch : _equipo2.values()) {
							if (luch.getNivel() < nivel && !ordenNivelMobs.containsKey(Integer.valueOf(luch.getID()))) {
								idLuch = luch.getID();
								nivel = luch.getNivel();
							}
						}
						ordenNivelMobs.put(Integer.valueOf(idLuch), Integer.valueOf(nivel));
					}
					_ordenNivelMobs.putAll(ordenNivelMobs);
				case 17:
					for (Luchador luch : _equipo1.values())
						luch._intocable = true;
				case 25:
					while (ordenNivelMobs.size() < size2) {
						nivel = 0;
						for (Luchador luch : _equipo2.values()) {
							if (luch.getNivel() > nivel && !ordenNivelMobs.containsKey(Integer.valueOf(luch.getID()))) {
								idLuch = luch.getID();
								nivel = luch.getNivel();
							}
						}
						ordenNivelMobs.put(Integer.valueOf(idLuch), Integer.valueOf(nivel));
					}
					_ordenNivelMobs.putAll(ordenNivelMobs);
				case 35:
					temporal = new ArrayList<Luchador>();
					temporal.addAll(_equipo2.values());
					while (ordenLuchMobs.size() < size2) {
						Luchador l = temporal.get(Fórmulas.getRandomValor(0, temporal.size() - 1));
						temporal.remove(l);
						ordenLuchMobs.put(Integer.valueOf(l.getID()), l);
					}
					_ordenLuchMobs.putAll(ordenLuchMobs);
					for (Entry<Integer, Luchador> e : _ordenLuchMobs.entrySet()) {
						SocketManager.ENVIAR_Gf_MOSTRAR_CASILLA_EN_PELEA(this, 5, e.getKey(),
								e.getValue().getCeldaPelea().getID());
					}
				case 47:
					for (Luchador luch : _equipo1.values())
						luch._contaminacion = true;
				}
			}
		}
		inicioTurno();
	}

	public Map<Integer, Personaje> getEspectadores() {
		return _espectadores;
	}

	public boolean hechizoDisponible(Luchador luchador, int idHechizo) {
		boolean ver = false;
		if (luchador.getPersonaje().tieneHechizoID(idHechizo)) {
			ver = true;
			for (HechizoLanzado HL : luchador.getHechizosLanzados()) {
				if (HL._hechizoId != idHechizo || HL.getSigLanzamiento() <= 0)
					continue;
				ver = false;
			}
		}
		return ver;
	}

	private void iniciarOrdenLuchadores() {
		int j = 0;
		int k = 0;
		int empieza0 = 0;
		int empieza1 = 0;
		int curMaxIni0 = 0;
		int curMaxIni1 = 0;
		Luchador curMax0 = null;
		Luchador curMax1 = null;
		boolean team1_ready = false;
		boolean team2_ready = false;
		do {
			if (!team1_ready) {
				team1_ready = true;
				Map<Integer, Luchador> _Team0 = _equipo1;
				for (Map.Entry<Integer, Luchador> entry : _Team0.entrySet()) {
					if (_ordenJugadores.contains(entry.getValue()))
						continue;
					team1_ready = false;
					if (entry.getValue().getIniciativa() >= curMaxIni0) {
						curMaxIni0 = entry.getValue().getIniciativa();
						curMax0 = entry.getValue();
					}
					if (curMaxIni0 <= empieza0)
						continue;
					empieza0 = curMaxIni0;
				}
			}
			if (!team2_ready) {
				team2_ready = true;
				for (Map.Entry<Integer, Luchador> entry : _equipo2.entrySet()) {
					if (_ordenJugadores.contains(entry.getValue()))
						continue;
					team2_ready = false;
					if (entry.getValue().getIniciativa() >= curMaxIni1) {
						curMaxIni1 = entry.getValue().getIniciativa();
						curMax1 = entry.getValue();
					}
					if (curMaxIni1 <= empieza1)
						continue;
					empieza1 = curMaxIni1;
				}
			}
			if (curMax1 == null && curMax0 == null) {
				return;
			}
			if (empieza0 > empieza1) {
				if (luchadoresDeEquipo(1).size() > j) {
					_ordenJugadores.add(curMax0);
					++j;
				}
				if (luchadoresDeEquipo(2).size() > k) {
					_ordenJugadores.add(curMax1);
					++k;
				}
			} else {
				if (luchadoresDeEquipo(2).size() > j) {
					_ordenJugadores.add(curMax1);
					++j;
				}
				if (luchadoresDeEquipo(1).size() > k) {
					_ordenJugadores.add(curMax0);
					++k;
				}
			}
			curMaxIni0 = 0;
			curMaxIni1 = 0;
			curMax0 = null;
			curMax1 = null;
		} while (_ordenJugadores.size() != luchadoresDeEquipo(3).size());
	}

	public void unirsePelea(Personaje perso, int idOtroPerso) {
		Luchador luchador;
		Personaje.Grupo g;
		Mapa.Celda celda;
		PrintWriter out = perso.getCuenta().getEntradaPersonaje().getOut();
		if (_estadoPelea > 2) {
			SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'l');
			return;
		}
		Luchador jugadorUnirse = null;
		int tiempoRestante = (int) (45000L - (System.currentTimeMillis() - _tiempoInicioTurno));
		if (_equipo1.containsKey(idOtroPerso)) {
			celda = getCeldaRandom(_celdasPos1);
			if (celda == null) {
				return;
			}
			if (_equipo1.size() >= 8) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 't');
				return;
			}
			if (_soloGrupo1 && (g = _luchInit1.getPersonaje().getGrupo()) != null && !g.getPersos().contains(perso)) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'f');
				return;
			}
			if (_tipo == 1) {
				if (perso.getAlineacion() == -1) {
					SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'a');
					return;
				}
				if (_luchInit1.getPersonaje().getAlineacion() != perso.getAlineacion()) {
					SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'a');
					return;
				}
				try {
					for (Luchador xL : _equipo1.values()) {
						try {
							if (xL.getPersonaje().getCuenta().getActualIP()
									.compareTo(perso.getCuenta().getActualIP()) != 0)
								continue;
							SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'f');
							return;
						} catch (NullPointerException nullPointerException) {
							// empty catch block
						}
					}
				} catch (Exception xL) {
					// empty catch block
				}
				perso.botonActDesacAlas('+');
			} else if (_tipo == 2) {
				if (perso.getAlineacion() == -1) {
					SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'a');
					return;
				}
				if (_luchInit1.getPersonaje().getAlineacion() != perso.getAlineacion()) {
					SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'a');
					return;
				}
				perso.botonActDesacAlas('+');
			}
			if (_gremioID > -1 && perso.getGremio() != null && getGremioID() == perso.getGremio().getID()) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'g');
				return;
			}
			if (_cerrado1) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'f');
				return;
			}
			if (_tipo == 0) {
				SocketManager.ENVIAR_GJK_UNIRSE_PELEA(perso, 2, true, true, false, 0, _tipo);
			} else {
				SocketManager.ENVIAR_GJK_UNIRSE_PELEA(perso, 2, false, true, false, tiempoRestante, _tipo);
			}
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(out, _mapaCopia.getPosicionesPelea(), _celdaColor1);
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(perso.getID()),
					String.valueOf(perso.getID()) + "," + 8 + ",0");
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(perso.getID()),
					String.valueOf(perso.getID()) + "," + 3 + ",0");
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(perso.getMapa(), perso.getID());
			luchador = new Luchador(this, perso);
			luchador.setEquipoBin(0);
			_equipo1.put(perso.getID(), luchador);
			perso.setPelea(this);
			luchador.setCeldaPelea(celda);
			luchador.getCeldaPelea().addLuchador(luchador);
			jugadorUnirse = luchador;
		} else if (_equipo2.containsKey(idOtroPerso)) {
			celda = getCeldaRandom(_celdasPos2);
			if (celda == null) {
				return;
			}
			if (_equipo2.size() >= 8) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 't');
				return;
			}
			if (_soloGrupo2 && (g = _luchInit2.getPersonaje().getGrupo()) != null && !g.getPersos().contains(perso)) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'f');
				return;
			}
			if (_tipo == 1) {
				if (perso.getAlineacion() == -1) {
					SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'a');
					return;
				}
				if (_luchInit2.getPersonaje().getAlineacion() != perso.getAlineacion()) {
					SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'a');
					return;
				}
				perso.botonActDesacAlas('+');
				try {
					for (Luchador xL : _equipo2.values()) {
						try {
							if (xL.getPersonaje().getCuenta().getActualIP()
									.compareTo(perso.getCuenta().getActualIP()) != 0)
								continue;
							SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'f');
							return;
						} catch (NullPointerException nullPointerException) {
							// empty catch block
						}
					}
				} catch (Exception xL) {
				}
			} else if (_tipo == 2) {
				if (perso.getAlineacion() == -1) {
					SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'a');
					return;
				}
				if (_luchInit2.getPrisma().getAlineacion() != perso.getAlineacion()) {
					SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'a');
					return;
				}
				perso.botonActDesacAlas('+');
			}
			if (_gremioID > -1 && perso.getGremio() != null && getGremioID() == perso.getGremio().getID()) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'g');
				return;
			}
			if (_cerrado2) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'f');
				return;
			}
			if (_tipo == 0) {
				SocketManager.ENVIAR_GJK_UNIRSE_PELEA(perso, 2, true, true, false, 0, _tipo);
			} else {
				SocketManager.ENVIAR_GJK_UNIRSE_PELEA(perso, 2, false, true, false, tiempoRestante, _tipo);
			}
			SocketManager.ENVIAR_GP_POSICIONES_PELEA(out, _mapaCopia.getPosicionesPelea(), _celdaColor2);
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(perso.getID()),
					String.valueOf(perso.getID()) + "," + 8 + ",0");
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, String.valueOf(perso.getID()),
					String.valueOf(perso.getID()) + "," + 3 + ",0");
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(perso.getMapa(), perso.getID());
			luchador = new Luchador(this, perso);
			luchador.setEquipoBin(1);
			_equipo2.put(perso.getID(), luchador);
			perso.setPelea(this);
			luchador.setCeldaPelea(celda);
			luchador.getCeldaPelea().addLuchador(luchador);
			jugadorUnirse = luchador;
		}
		SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(perso, 0);
		perso.getCelda().removerPersonaje(perso.getID());
		SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(perso.getMapa(),
				(jugadorUnirse.getEquipoBin() == 0 ? _luchInit1 : _luchInit2).getID(), jugadorUnirse);
		SocketManager.ENVIAR_GM_JUGADO_UNIRSE_PELEA(this, 7, jugadorUnirse);
		SocketManager.ENVIAR_GM_LUCHADORES(this, _mapaCopia, perso);
		if (_Recaudador != null) {
			for (Personaje z : Mundo.getGremio(_gremioID).getPjMiembros()) {
				if (!z.enLinea())
					continue;
				Recaudador.analizarAtaque(z, _gremioID);
				Recaudador.analizarDefensa(z, _gremioID);
			}
		}
		if (_Prisma != null) {
			for (Personaje z : Mundo.getPJsEnLinea()) {
				if (z == null || z.getAlineacion() != _Prisma.getAlineacion())
					continue;
				Prisma.analizarAtaque(perso);
			}
		}
	}

	public boolean unirsePeleaRecaudador(Personaje perso, int recauID, short mapaID, short celdaID) {
		PrintWriter out = perso.getCuenta().getEntradaPersonaje().getOut();
		if (_tiempoInicio != 0L) {
			SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'l');
			return false;
		}
		Luchador jugadorAUnirse = null;
		Mapa.Celda celda = getCeldaRandom(_celdasPos2);
		if (celda == null)
			return false;
		if (perso.getMapa().getID() != mapaID) {
			perso.setMapaDefPerco(perso.getMapa());
			perso.setCeldaDefPerco(perso.getCelda());
			try {
				if (!perso.teleportSinTodos(mapaID, celdaID))
					return false;
				Thread.sleep(500L);
			} catch (Exception e) {
				return false;
			}
		}
		int tiempoRecaudador = 0;
		if (_Recaudador != null)
			tiempoRecaudador = _Recaudador.getTiempoTurno();
		int idPerso = perso.getID();
		try {
			Thread.sleep(500L);
		} catch (Exception exception) {
		}
		SocketManager.ENVIAR_GJK_UNIRSE_PELEA(perso, 2, false, true, false, tiempoRecaudador, _tipo);
		SocketManager.ENVIAR_GP_POSICIONES_PELEA(out, _mapaCopia.getPosicionesPelea(), _celdaColor2);
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, (new StringBuilder(String.valueOf(idPerso))).toString(),
				String.valueOf(idPerso) + "," + '\b' + ",0");
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, (new StringBuilder(String.valueOf(idPerso))).toString(),
				String.valueOf(idPerso) + "," + '\003' + ",0");
		Luchador luchador = new Luchador(this, perso);
		jugadorAUnirse = luchador;
		luchador.setEquipoBin(1);
		_equipo2.put(Integer.valueOf(idPerso), luchador);
		perso.setPelea(this);
		luchador.setCeldaPelea(celda);
		celda.addLuchador(luchador);
		SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(perso, 0);
		SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(perso.getMapa(), recauID, jugadorAUnirse);
		SocketManager.ENVIAR_GM_JUGADO_UNIRSE_PELEA(this, 7, jugadorAUnirse);
		try {
			Thread.sleep(300L);
		} catch (Exception exception) {
		}
		SocketManager.ENVIAR_GM_LUCHADORES(this, _mapaCopia, perso);
		try {
			Thread.sleep(300L);
		} catch (Exception exception) {
		}
		return true;
	}

	public boolean unirsePeleaPrisma(Personaje perso, int prismaID, short mapaID, short celdaID) {
		PrintWriter out = perso.getCuenta().getEntradaPersonaje().getOut();
		if (_tiempoInicio != 0L) {
			SocketManager.ENVIAR_GA903_ERROR_PELEA(out, 'l');
			return false;
		}
		Luchador jugadorAUnirse = null;
		Mapa.Celda celda = getCeldaRandom(_celdasPos2);
		if (celda == null)
			return false;
		if (perso.getMapa().getID() != mapaID) {
			perso.setMapaDefPerco(perso.getMapa());
			perso.setCeldaDefPerco(perso.getCelda());
			try {
				if (!perso.teleportSinTodos(mapaID, celdaID))
					return false;
				Thread.sleep(500L);
			} catch (Exception e) {
				return false;
			}
		}
		int tiempoPrisma = 0;
		if (_Prisma != null)
			tiempoPrisma = _Prisma.getTiempoTurno();
		int idPerso = perso.getID();
		try {
			Thread.sleep(500L);
		} catch (Exception exception) {
		}
		SocketManager.ENVIAR_GJK_UNIRSE_PELEA(perso, 2, false, true, false, tiempoPrisma, _tipo);
		SocketManager.ENVIAR_GP_POSICIONES_PELEA(out, _mapaCopia.getPosicionesPelea(), _celdaColor2);
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, (new StringBuilder(String.valueOf(idPerso))).toString(),
				String.valueOf(idPerso) + "," + '\b' + ",0");
		SocketManager.ENVIAR_GA_ACCION_PELEA(this, 3, 950, (new StringBuilder(String.valueOf(idPerso))).toString(),
				String.valueOf(idPerso) + "," + '\003' + ",0");
		Luchador luchador = new Luchador(this, perso);
		jugadorAUnirse = luchador;
		luchador.setEquipoBin(1);
		_equipo2.put(Integer.valueOf(idPerso), luchador);
		perso.setPelea(this);
		luchador.setCeldaPelea(celda);
		celda.addLuchador(luchador);
		SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(perso, 0);
		SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(perso.getMapa(), prismaID, jugadorAUnirse);
		SocketManager.ENVIAR_GM_JUGADO_UNIRSE_PELEA(this, 7, jugadorAUnirse);
		try {
			Thread.sleep(300L);
		} catch (Exception exception) {
		}
		SocketManager.ENVIAR_GM_LUCHADORES(this, _mapaCopia, perso);
		try {
			Thread.sleep(300L);
		} catch (Exception exception) {
		}
		return true;
	}

	public void unirseEspectador(Personaje perso) {
		if (_tiempoInicio == 0L || perso == null) {
			return;
		}
		if (perso.esFantasma()) {
			SocketManager.ENVIAR_Im_INFORMACION(perso, "1116");
			return;
		}
		if (!_espectadorOk || _estadoPelea != 3) {
			SocketManager.ENVIAR_Im_INFORMACION(perso, "157");
			return;
		}
		int tiempoRestante = (int) (29000L - (System.currentTimeMillis() - _tiempoInicioTurno));
		perso.setPelea(this);
		perso.getCelda().removerPersonaje(perso.getID());
		SocketManager.ENVIAR_GJK_UNIRSE_PELEA(perso, _estadoPelea, false, false, true, 0, _tipo);
		SocketManager.ENVIAR_GS_EMPEZAR_COMBATE(perso);
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(perso.getMapa(), perso.getID());
		SocketManager.ENVIAR_GM_LUCHADORES(this, _mapaCopia, perso);
		SocketManager.ENVIAR_GTS_INICIO_TURNO_PELEA(perso, _ordenJugadores.get(_nroOrdenLuc).getID(), tiempoRestante);
		SocketManager.ENVIAR_GTL_ORDEN_JUGADORES(perso, this);
		try {
			Thread.sleep(200L);
		} catch (Exception exception) {
			// empty catch block
		}
		if (_tipo == 4 || _tipo == 3) {
			for (Map.Entry<Integer, Integer> entry : _retos.entrySet()) {
				String str = _stringReto.get(entry.getKey());
				if (str.isEmpty())
					continue;
				SocketManager.ENVIAR_Gd_RETO_A_PERSONAJE(perso, str);
				if (entry.getValue() == 1) {
					SocketManager.ENVIAR_GdaK_RETO_REALIZADO(perso, (int) entry.getKey());
					continue;
				}
				if (entry.getValue() != 2)
					continue;
				SocketManager.ENVIAR_GdaO_RETO_PERDIDO(perso, (int) entry.getKey());
			}
		}
		_espectadores.put(perso.getID(), perso);
		SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 7, "036;" + perso.getNombre());
	}

	public void desconectarLuchador(Personaje perso) {
		Luchador luchador = getLuchadorPorPJ(perso);
		if (luchador == null) {
			return;
		}
		luchador._desconectado = true;
		if (luchador.puedeJugar()) {
			SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 7,
					"1182;" + luchador.getNombreLuchador() + "~" + luchador._turnosRestantes);
			Luchador luchador2 = luchador;
			luchador2._turnosRestantes = luchador2._turnosRestantes - 1;
			finTurno(luchador);
		}
	}

	public void reconectarLuchador(Personaje perso) {
		if (_estadoPelea != 3 || _ordenJugadores.size() == 0) {
			return;
		}
		int tiempoRestante = (int) ((long) LesGuardians.TIEMPO_PELEA
				- (System.currentTimeMillis() - _tiempoInicioTurno));
		perso.getCelda().removerPersonaje(perso.getID());
		SocketManager.ENVIAR_GJK_UNIRSE_PELEA(perso, _estadoPelea, false, true, false, 0, _tipo);
		SocketManager.ENVIAR_GM_LUCHADORES(this, _mapaCopia, perso);
		SocketManager.ENVIAR_GS_EMPEZAR_COMBATE(perso);
		SocketManager.ENVIAR_GTS_INICIO_TURNO_PELEA(perso, _ordenJugadores.get(_nroOrdenLuc).getID(), tiempoRestante);
		SocketManager.ENVIAR_GTL_ORDEN_JUGADORES(perso, this);
		SocketManager.ENVIAR_GTM_INFO_STATS_TODO_LUCHADORES(this, perso);
		try {
			Thread.sleep(200L);
		} catch (Exception exception) {
			// empty catch block
		}
		if (_tipo == 4 || _tipo == 3) {
			for (Map.Entry<Integer, Integer> entry : _retos.entrySet()) {
				String str = _stringReto.get(entry.getKey());
				if (str.isEmpty())
					continue;
				SocketManager.ENVIAR_Gd_RETO_A_PERSONAJE(perso, str);
				if (entry.getValue() == 1) {
					SocketManager.ENVIAR_GdaK_RETO_REALIZADO(perso, (int) entry.getKey());
					continue;
				}
				if (entry.getValue() != 2)
					continue;
				SocketManager.ENVIAR_GdaO_RETO_PERDIDO(perso, (int) entry.getKey());
			}
		}
		try {
			getLuchadorPorPJ(perso)._desconectado = false;
			SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 7, "1184;" + perso.getNombre());
		} catch (Exception exception) {}
	}

	public void botonBloquearMasJug(int id) {
		if (_luchInit1 != null && _idLuchInit1 == id) {
			_cerrado1 = !_cerrado1;
			SocketManager.ENVIAR_Go_BOTON_ESPEC_AYUDA(_mapaReal, _cerrado1 ? (char) '+' : '-', 'A', id);
			SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 1, _cerrado1 ? "095" : "096");
		} else if (_luchInit2 != null && _idLuchInit2 == id) {
			_cerrado2 = !_cerrado2;
			SocketManager.ENVIAR_Go_BOTON_ESPEC_AYUDA(_mapaReal, _cerrado2 ? (char) '+' : '-', 'A', id);
			SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 2, _cerrado2 ? "095" : "096");
		}
	}

	public synchronized void botonSoloGrupo(int id) {
		if (_luchInit1 != null && _idLuchInit1 == id) {
			_soloGrupo1 = !_soloGrupo1;
			if (_soloGrupo1) {
				ArrayList<Integer> lista = new ArrayList<Integer>();
				ArrayList<Integer> expulsar = new ArrayList<Integer>();
				lista.addAll(_luchInit1.getPersonaje().getGrupo().getIDsPersos());
				for (Map.Entry<Integer, Luchador> entry : _equipo1.entrySet()) {
					int expulsadoID = entry.getKey();
					Luchador luch = entry.getValue();
					if (lista.contains(expulsadoID))
						continue;
					expulsar.add(expulsadoID);
					SocketManager.ENVIAR_GM_BORRAR_LUCHADOR(this, expulsadoID, 3);
					SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(luch.getPersonaje());
					luch.getPersonaje().retornoMapa();
					luch._celda.removerLuchador(luch);
					SocketManager.ENVIAR_Gt_BORRAR_NOMBRE_ESPADA(_mapaReal, _idLuchInit1, luch);
				}
				for (Integer ID : expulsar) {
					_equipo1.remove(ID);
				}
			}
			SocketManager.ENVIAR_Go_BOTON_ESPEC_AYUDA(_mapaReal, _soloGrupo1 ? (char) '+' : '-', 'P', id);
			SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 1, _soloGrupo1 ? "093" : "094");
		} else if (_luchInit2 != null && _idLuchInit2 == id) {
			_soloGrupo2 = !_soloGrupo2;
			if (_soloGrupo2) {
				ArrayList<Integer> lista = new ArrayList<Integer>();
				ArrayList<Integer> expulsar = new ArrayList<Integer>();
				lista.addAll(_luchInit2.getPersonaje().getGrupo().getIDsPersos());
				for (Map.Entry<Integer, Luchador> entry : _equipo2.entrySet()) {
					int expulsadoID = entry.getKey();
					Luchador luch = entry.getValue();
					if (lista.contains(expulsadoID))
						continue;
					expulsar.add(expulsadoID);
					SocketManager.ENVIAR_GM_BORRAR_LUCHADOR(this, expulsadoID, 3);
					SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(luch.getPersonaje());
					luch.getPersonaje().retornoMapa();
					luch._celda.removerLuchador(luch);
					SocketManager.ENVIAR_Gt_BORRAR_NOMBRE_ESPADA(_mapaReal, _idLuchInit2, luch);
				}
				for (Integer ID : expulsar) {
					_equipo2.remove(ID);
				}
			}
			SocketManager.ENVIAR_Go_BOTON_ESPEC_AYUDA(_mapaReal, _soloGrupo2 ? (char) '+' : '-', 'P', id);
			SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 2, _soloGrupo2 ? "095" : "096");
		}
	}

	public synchronized void botonBloquearEspect(int id) {
		if (_luchInit1 != null && _idLuchInit1 == id || _luchInit2 != null && _idLuchInit2 == id) {
			_espectadorOk = !_espectadorOk;
			if (_idLuchInit1 == id) {
				SocketManager.ENVIAR_Go_BOTON_ESPEC_AYUDA(_mapaReal, _espectadorOk ? (char) '+' : '-', 'S',
						_idLuchInit1);
			} else {
				SocketManager.ENVIAR_Go_BOTON_ESPEC_AYUDA(_mapaReal, _espectadorOk ? (char) '+' : '-', 'S',
						_idLuchInit2);
			}
			SocketManager.ENVIAR_Im_INFORMACION_A_MAPA(_mapaCopia, _espectadorOk ? "039" : "040");
		}
		if (_espectadores.size() > 0) {
			for (Personaje espec : _espectadores.values()) {
				SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(espec);
				espec.retornoMapa();
			}
			_espectadores.clear();
		}
	}

	public void botonAyuda(int id) {
		if (_luchInit1 != null && _idLuchInit1 == id) {
			_ayuda1 = !_ayuda1;
			SocketManager.ENVIAR_Go_BOTON_ESPEC_AYUDA(_mapaReal, _ayuda1 ? (char) '+' : '-', 'H', id);
			SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 1, _ayuda1 ? "0103" : "0104");
		} else if (_luchInit2 != null && _idLuchInit2 == id) {
			_ayuda2 = !_ayuda2;
			SocketManager.ENVIAR_Go_BOTON_ESPEC_AYUDA(_mapaReal, _ayuda2 ? (char) '+' : '-', 'H', id);
			SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 2, _ayuda2 ? "0103" : "0104");
		}
	}

	private void setEstado(byte estado) {
		_estadoPelea = estado;
	}

	private void setGremioID(int gremioID) {
		_gremioID = gremioID;
	}

	public int getEstado() {
		return _estadoPelea;
	}

	public int getGremioID() {
		return _gremioID;
	}

	public int getTipoPelea() {
		return _tipo;
	}

	public List<Luchador> getOrdenJug() {
		return _ordenJugadores;
	}

	public boolean puedeMoverseLuchador(Luchador movedor, GameThread.AccionDeJuego GA) {
		PrintWriter out;
		short sigCeldaID;
		Luchador luchador;
		String path = GA._args;
		if (path.equals("") || _ordenJugadores.size() <= 0 || _nroOrdenLuc < 0) {
			return false;
		}
		if (_nroOrdenLuc >= _ordenJugadores.size()) {
			_nroOrdenLuc = 0;
		}
		if ((luchador = _ordenJugadores.get(_nroOrdenLuc)) == null) {
			return false;
		}
		if (!_tempAccion.isEmpty() || luchador.getID() != movedor.getID() || _estadoPelea != 3) {
			return false;
		}
		Personaje perso = movedor.getPersonaje();
		Luchador tacleador = Pathfinding.getEnemigoAlrededor(movedor.getCeldaPelea().getID(), _mapaCopia, this);
		if (tacleador != null && !movedor.tieneEstado(6) && !movedor.tieneEstado(8)) {
			int porcEsquiva = Fórmulas.getPorcTacleo(movedor, tacleador);
			int rand = Fórmulas.getRandomValor(0, 99);
			if (rand > porcEsquiva) {
				SocketManager.ENVIAR_GA_ACCION_PELEA_CON_RESPUESTA(this, 7, GA._idUnica, "104",
						String.valueOf(movedor.getID()) + ";", "");
				int pierdePA = _tempLuchadorPA * porcEsquiva / 100;
				if (pierdePA < 0) {
					pierdePA = -pierdePA;
				}
				if (_tempLuchadorPM < 0) {
					_tempLuchadorPM = 0;
				}
				SocketManager.ENVIAR_GA_ACCION_PELEA_CON_RESPUESTA(this, 7, GA._idUnica, "129",
						String.valueOf(movedor.getID()), String.valueOf(movedor.getID()) + ",-" + _tempLuchadorPM);
				SocketManager.ENVIAR_GA_ACCION_PELEA_CON_RESPUESTA(this, 7, GA._idUnica, "102",
						String.valueOf(movedor.getID()), String.valueOf(movedor.getID()) + ",-" + pierdePA);
				_tempLuchadorPM = 0;
				_tempLuchadorPA = (short) (_tempLuchadorPA - pierdePA);
				return false;
			}
		}
		AtomicReference<String> pathRef = new AtomicReference<String>(path);
		short nroCeldasMov = Pathfinding.numeroMovimientos(_mapaCopia, movedor.getCeldaPelea().getID(), pathRef, this);
		if (nroCeldasMov >= 10000) {
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 151, String.valueOf(movedor.getID()), "-1");
			nroCeldasMov = (short) (nroCeldasMov - 10000);
		}
		String nuevoPath = pathRef.get();
		if (nroCeldasMov > _tempLuchadorPM || nroCeldasMov == -1000) {
			return false;
		}
		_tempLuchadorPM = (short) (_tempLuchadorPM - nroCeldasMov);
		_tempLuchadorPMusados = (short) (_tempLuchadorPMusados + nroCeldasMov);
		if ((_tipo == 4 || _tipo == 3) && movedor.getPersonaje() != null) {
			TreeMap<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
			copiaRetos.putAll(_retos);
			for (Entry<Integer, Integer> entry : copiaRetos.entrySet()) { //FIXME Is this really working?
				int reto = (Integer) entry.getKey();
				int exitoReto = (Integer) entry.getValue();
				if (exitoReto != 0)
					continue;
				switch (reto) {
				case 1: {
					if (_tempLuchadorPMusados == 1)
						break;
					SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
					exitoReto = 2;
				}
				}
				if (exitoReto == 0)
					continue;
				_retos.remove(reto);
				_retos.put(reto, exitoReto);
			}
		}
		if (_mapaCopia.getCelda(
				sigCeldaID = CryptManager.celdaCodigoAID(nuevoPath.substring(nuevoPath.length() - 2))) == null) {
			return false;
		}
		if (perso != null) {
			SocketManager.ENVIAR_GAS_INICIO_DE_ACCION(this, 7, movedor.getID());
		}
		if (!movedor.esInvisible()) {
			SocketManager.ENVIAR_GA_ACCION_PELEA_CON_RESPUESTA(this, 7, GA._idUnica, "1",
					String.valueOf(movedor.getID()),
					"a" + CryptManager.celdaIDACodigo(movedor.getCeldaPelea().getID()) + nuevoPath);
		} else if (perso != null && (out = perso.getCuenta().getEntradaPersonaje().getOut()) != null) {
			SocketManager.ENVIAR_GA_ACCION_DE_JUEGO(out, String.valueOf(GA._idUnica), "1",
					String.valueOf(movedor.getID()),
					"a" + CryptManager.celdaIDACodigo(movedor.getCeldaPelea().getID()) + nuevoPath);
		}
		Luchador portador = movedor.getTransportadoPor();
		if (portador != null && movedor.tieneEstado(8) && portador.tieneEstado(3)) {
			if (sigCeldaID != portador.getCeldaPelea().getID()) {
				portador.getCeldaPelea().removerLuchador(movedor);
				portador.setEstado(3, 0);
				movedor.setEstado(8, 0);
				portador.setTransportando(null);
				movedor.setTransportadoPor(null);
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 950, String.valueOf(portador.getID()),
						String.valueOf(portador.getID()) + "," + 3 + ",0");
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 950, String.valueOf(movedor.getID()),
						String.valueOf(movedor.getID()) + "," + 8 + ",0");
			} else {
				movedor.getCeldaPelea().getLuchadores().clear();
			}
		} else {
			movedor.getCeldaPelea().getLuchadores().clear();
		}
		movedor.setCeldaPelea(_mapaCopia.getCelda(sigCeldaID));
		movedor.getCeldaPelea().addLuchador(movedor);
		if (nroCeldasMov < 0) {
			nroCeldasMov = (short) (nroCeldasMov * -1);
		}
		_tempAccion = "GA;129;" + movedor.getID() + ";" + movedor.getID() + ",-" + nroCeldasMov;
		Luchador tranportado = movedor.getTransportando();
		if (tranportado != null && movedor.tieneEstado(3) && tranportado.tieneEstado(8)) {
			tranportado.setCeldaPelea(movedor.getCeldaPelea());
			movedor.getCeldaPelea().addLuchador(tranportado);
		}
		if (perso == null) {
			SocketManager.ENVIAR_GAMEACTION_A_PELEA(this, 7, _tempAccion);
			ArrayList<Trampa> trampas = new ArrayList<Trampa>();
			trampas.addAll(_trampas);
			try {
				if (nroCeldasMov > 2) {
					Thread.sleep(600 * nroCeldasMov);
				} else {
					Thread.sleep(900 * nroCeldasMov);
				}
			} catch (Exception exitoReto) {
				// empty catch block
			}
			for (Trampa t : trampas) {
				short dist = Pathfinding.distanciaEntreDosCeldas(_mapaCopia, t.getCelda().getID(),
						movedor.getCeldaPelea().getID());
				if (dist > t.getTama\u00f1o())
					continue;
				t.activaTrampa(movedor);
			}
			_tempAccion = "";
		} else {
			perso.getCuenta().getEntradaPersonaje().addGA(GA);
		}
		return true;
	}

	public void finalizarMovimiento(Personaje perso) {
		if (_nroOrdenLuc < 0 || _ordenJugadores.size() == 0) {
			return;
		}
		if (_nroOrdenLuc >= _ordenJugadores.size()) {
			_nroOrdenLuc = 0;
		}
		int idLuchador = _ordenJugadores.get(_nroOrdenLuc).getID();
		if (_tempAccion.equals("") || idLuchador != perso.getID() || _estadoPelea != 3) {
			return;
		}
		SocketManager.ENVIAR_GAMEACTION_A_PELEA(this, 7, _tempAccion);
		SocketManager.ENVIAR_GAF_FINALIZAR_ACCION(this, 7, 2, idLuchador);
		ArrayList<Trampa> trampas = new ArrayList<Trampa>();
		trampas.addAll(_trampas);
		for (Trampa trampa : trampas) {
			if (_estadoPelea != 3)
				break;
			Luchador luchador = getLuchadorPorPJ(perso);
			short dist = Pathfinding.distanciaEntreDosCeldas(_mapaCopia, trampa.getCelda().getID(),
					luchador.getCeldaPelea().getID());
			if (dist > trampa.getTama\u00f1o())
				continue;
			trampa.activaTrampa(luchador);
		}
		_tempAccion = "";
	}

	public void pasarTurno(Personaje perso) {
		if (!_tempAccion.isEmpty()) {
			return;
		}
		Luchador luchador = getLuchadorPorPJ(perso);
		if (luchador == null || !luchador.puedeJugar()) {
			return;
		}
		finTurno(luchador);
	}

	private void inicioTurno() {
		if (!verificaSiQuedaUno()) {
			acaboPelea(false, false);
			return;
		}
		if (_estadoPelea >= 4) {
			return;
		}
		_cantTimer = 0;
		_nroOrdenLuc = (byte) (_nroOrdenLuc + 1);
		_tempAccion = "";
		_cantMobsMuerto = 0;
		if (_nroOrdenLuc >= _ordenJugadores.size()) {
			_nroOrdenLuc = 0;
		}
		if (_ordenJugadores.get(_nroOrdenLuc) == null) {
			return;
		}
		Luchador luchador = _ordenJugadores.get(_nroOrdenLuc);
		luchador.setPuedeJugar(true);
		_cantFinTurno = 0;
		if (luchador._estaMuerto) {
			finTurno(luchador);
			return;
		}
		SocketManager.ENVIAR_GTS_INICIO_TURNO_PELEA(this, 7, luchador.getID(), LesGuardians.TIEMPO_PELEA);
		luchador.aplicarBuffInicioTurno(this);
		if (luchador._estaMuerto) {
			return;
		}
		ArrayList<Glifo> glifos = new ArrayList<Glifo>();
		glifos.addAll(_glifos);
		for (Glifo glifo : glifos) {
			if (_estadoPelea >= 4) {
				return;
			}
			if (glifo.getLanzador().getID() == luchador.getID() && glifo.disminuirDuracion() == 0) {
				_glifos.remove(glifo);
				glifo.desaparecer();
				continue;
			}
			short dist = Pathfinding.distanciaEntreDosCeldas(_mapaCopia, luchador.getCeldaPelea().getID(),
					glifo.getCelda().getID());
			if (dist > glifo.getTama\u00f1o() || glifo._hechizos == 476)
				continue;
			glifo.activarGlifo(luchador);
		}
		if (luchador._estaMuerto) {
			return;
		}
		luchador._bonusCastigo.clear();
		_tempLuchadorPA = (short) luchador.getPAConBuff();
		_tempLuchadorPM = (short) luchador.getPMConBuff();
		_tempLuchadorPAusados = 0;
		_tempLuchadorPMusados = 0;
		luchador.actualizaHechizoLanzado();
		try {
			Thread.sleep(300L);
		} catch (Exception glifo) {
			// empty catch block
		}
		if (luchador.getPersonaje() != null) {
			SocketManager.ENVIAR_As_STATS_DEL_PJ(luchador.getPersonaje());
		}
		if (luchador.tieneBuff(140)) {
			finTurno(luchador);
			return;
		}
		if (luchador.getPDVConBuff() <= 0) {
			agregarAMuertos(luchador);
			return;
		}
		if (luchador._desconectado) {
			SocketManager.ENVIAR_Im_INFORMACION_A_PELEA(this, 7,
					"1182;" + luchador.getNombreLuchador() + "~" + luchador._turnosRestantes);
			Luchador luchador2 = luchador;
			luchador2._turnosRestantes = luchador2._turnosRestantes - 1;
			if (luchador._turnosRestantes == 0) {
				agregarAMuertos(luchador);
			} else {
				finTurno(luchador);
			}
			return;
		}
		_tiempoInicioTurno = System.currentTimeMillis();
		if ((_tipo == 4 || _tipo == 3) && luchador.getPersonaje() != null) {
			TreeMap<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
			copiaRetos.putAll(_retos);
			for (Entry<Integer, Integer> entry : copiaRetos.entrySet()) { //FIXME Is this really working?
				int reto = (Integer) entry.getKey();
				int exitoReto = (Integer) entry.getValue();
				if (exitoReto != 0)
					continue;
				switch (reto) {
				case 2: {
					luchador._idCeldaIniTurnoReto = luchador._celda.getID();
					break;
				}
				case 6: {
					luchador._hechiLanzadosReto.clear();
					break;
				}
				case 34: {
					ArrayList<Luchador> mobsVivos = new ArrayList<Luchador>();
					for (Luchador luch : _inicioLucEquipo2) {
						if (luch.estaMuerto())
							continue;
						mobsVivos.add(luch);
					}
					if (mobsVivos.size() <= 0)
						break;
					Luchador x = (Luchador) mobsVivos.get(Fórmulas.getRandomValor(0, mobsVivos.size() - 1));
					_idMobReto = x.getID();
					SocketManager.ENVIAR_Gf_MOSTRAR_CASILLA_EN_PELEA(this, 5, _idMobReto, x.getCeldaPelea().getID());
					break;
				}
				case 47: {
					if (!luchador._contaminado)
						break;
					Luchador luchador3 = luchador;
					luchador3._turnosParaMorir = luchador3._turnosParaMorir + 1;
					if (luchador._turnosParaMorir <= 3)
						break;
					SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
					exitoReto = 2;
				}
				}
				if (exitoReto == 0)
					continue;
				_retos.remove(reto);
				_retos.put(reto, exitoReto);
			}
		}
		if (luchador.getPersonaje() == null || luchador._doble != null || luchador._recaudador != null
				|| luchador._prisma != null) {
			try {
				Thread.sleep(500L);
			} catch (Exception exception) {
				// empty catch block
			}
			new IA.MotorIA(luchador, this);
		}
	}

	public void agregarAMuertos(Luchador victima) {
		if (victima._estaMuerto)
			return;
		addTiempoReset(300);
		victima._estaMuerto = true;
		int idVictima = victima.getID();
		if (!victima._estaRetirado)
			_listaMuertos.put(Integer.valueOf(idVictima), victima);
		if ((_tipo == 4 || _tipo == 3) && !victima.esInvocacion() && _equipo2.values().contains(victima))
			_mobsMuertosReto.add(Integer.valueOf(idVictima));
		if (_nroOrdenLuc < 0)
			return;
		if (_nroOrdenLuc >= _ordenJugadores.size())
			_nroOrdenLuc = 0;
		Luchador asesino = null;
		try {
			asesino = _ordenJugadores.get(_nroOrdenLuc);
		} catch (Exception exception) {
		}
		if ((_tipo == 4 || _tipo == 3) && victima.getMob() != null && !victima.esInvocacion() && asesino != null) {
			Map<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
			copiaRetos.putAll(_retos);
			for (Map.Entry<Integer, Integer> entry : copiaRetos.entrySet()) {
				boolean siguiente;
				Iterator<Map.Entry<Integer, Luchador>> iterator;
				int reto = ((Integer) entry.getKey()).intValue();
				int exitoReto = ((Integer) entry.getValue()).intValue();
				if (exitoReto != 0)
					continue;
				int cant2 = 0;
				int nivelVict = victima.getNivel();
				switch (reto) {
				case 3:
					if (_mobsMuertosReto.size() > 0) {
						if (((Integer) _mobsMuertosReto.get(0)).intValue() == _idMobReto) {
							SocketManager.ENVIAR_GdaK_RETO_REALIZADO(this, reto);
							exitoReto = 1;
							break;
						}
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
						exitoReto = 2;
					}
					break;
				case 4:
					cant2 = _inicioLucEquipo2.size();
					if (_mobsMuertosReto.size() == cant2) {
						if (((Integer) _mobsMuertosReto.get(cant2 - 1)).intValue() == _idMobReto) {
							SocketManager.ENVIAR_GdaK_RETO_REALIZADO(this, reto);
							exitoReto = 1;
							break;
						}
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
						exitoReto = 2;
					}
					break;
				case 10:
				case 25:
					for (Map.Entry<Integer, Integer> e : _ordenNivelMobs.entrySet()) {
						if (((Integer) e.getValue()).intValue() == nivelVict) {
							if (((Integer) e.getKey()).intValue() == idVictima) {
								_ordenNivelMobs.remove(Integer.valueOf(idVictima));
								break;
							}
							continue;
						}
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
						exitoReto = 2;
						break;
					}
					break;
				case 28:
					if (asesino.getPersonaje() == null)
						continue;
					if (asesino.getPersonaje().getSexo() == 0) {
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
						exitoReto = 2;
					}
					break;
				case 29:
					if (asesino.getPersonaje() == null)
						continue;
					if (asesino.getPersonaje().getSexo() == 1) {
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
						exitoReto = 2;
					}
					break;
				case 30:
					if (asesino.getPersonaje() == null)
						continue;
					if (asesino.getID() != _luchMenorNivelReto) {
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
						exitoReto = 2;
					}
					break;
				case 31:
					if (_idMobReto == 0) {
						_idMobReto = idVictima;
						break;
					}
					if (_mobsMuertosReto.contains(Integer.valueOf(_idMobReto)))
						_idMobReto = 0;
					break;
				case 32:
					if (_idMobReto == idVictima) {
						SocketManager.ENVIAR_GdaK_RETO_REALIZADO(this, reto);
						exitoReto = 1;
						break;
					}
					SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
					exitoReto = 2;
					break;
				case 35:
					siguiente = false;
					iterator = _ordenLuchMobs.entrySet().iterator();
					if (iterator.hasNext()) {
						Map.Entry<Integer, Luchador> e = iterator.next();
						if (((Integer) e.getKey()).intValue() == idVictima) {
							_ordenLuchMobs.remove(Integer.valueOf(idVictima));
							siguiente = true;
						} else {
							SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
							exitoReto = 2;
						}
					}
					if (siguiente) {
						iterator = _ordenLuchMobs.entrySet().iterator();
						if (iterator.hasNext()) {
							Map.Entry<Integer, Luchador> e = iterator.next();
							SocketManager.ENVIAR_Gf_MOSTRAR_CASILLA_EN_PELEA(this, 5, ((Integer) e.getKey()).intValue(),
									((Luchador) e.getValue()).getCeldaPelea().getID());
						}
					}
					break;
				case 42:
					_cantMobsMuerto = (byte) (_cantMobsMuerto + 1);
					if (_cantMobsMuerto > 2) {
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
						exitoReto = 2;
					}
					break;
				case 44:
				case 46:
					if (asesino.getPersonaje() == null)
						continue;
					asesino._mobMatadosReto.add(Integer.valueOf(idVictima));
					break;
				}
				if (exitoReto != 0) {
					_retos.remove(Integer.valueOf(reto));
					_retos.put(Integer.valueOf(reto), Integer.valueOf(exitoReto));
				}
			}
		}
		if (_inicioLucEquipo1.contains(victima) && !victima.esInvocacion()
				&& !_muertesLuchInic1.contains(Integer.valueOf(idVictima)))
			_muertesLuchInic1.add(Integer.valueOf(idVictima));
		if (_inicioLucEquipo2.contains(victima) && !victima.esInvocacion()
				&& !_muertesLuchInic2.contains(Integer.valueOf(idVictima)))
			_muertesLuchInic2.add(Integer.valueOf(idVictima));
		SocketManager.ENVIAR_GA103_JUGADOR_MUERTO(this, 7, idVictima);
		if (victima.tieneEstado(3)) {
			Luchador transportado = victima.getTransportando();
			transportado.setEstado(8, 0);
			victima.setEstado(3, 0);
			transportado.setTransportadoPor(null);
			victima.setTransportando(null);
		} else if (victima.tieneEstado(8)) {
			Luchador portador = victima.getTransportadoPor();
			victima.setEstado(8, 0);
			portador.setEstado(3, 0);
			victima.setTransportadoPor(null);
			portador.setTransportando(null);
		}
		victima.getCeldaPelea().removerLuchador(victima);
		if (victima.getEquipoBin() == 0) {
			TreeMap<Integer, Luchador> team = new TreeMap<Integer, Luchador>();
			team.putAll(_equipo1);
			for (Map.Entry<Integer, Luchador> entry : team.entrySet()) {
				Luchador invocacion = entry.getValue();
				if (invocacion._estaMuerto || invocacion._estaRetirado || !invocacion.esInvocacion()
						|| invocacion.getInvocador().getID() != idVictima)
					continue;
				try {
					Thread.sleep(150L);
				} catch (Exception exception) {
				}
				agregarAMuertos(invocacion);
			}
		} else if (victima.getEquipoBin() == 1) {
			TreeMap<Integer, Luchador> team = new TreeMap<Integer, Luchador>();
			team.putAll(_equipo2);
			for (Map.Entry<Integer, Luchador> entry : team.entrySet()) {
				Luchador invocacion = entry.getValue();
				if (invocacion._estaMuerto || invocacion._estaRetirado)
					continue;
				if (!invocacion.esInvocacion() || invocacion.getInvocador().getID() != idVictima)
					continue;
				try {
					Thread.sleep(150L);
				} catch (Exception exception) {
				}
				agregarAMuertos(invocacion);
			}
		}
		if (victima.getMob() != null)
			try {
				boolean esEstatico = false;
				byte b;
				int i, arrayOfInt[];
				for (i = (arrayOfInt = Constantes.INVOCACIONES_ESTATICAS).length, b = 0; b < i;) {
					int id = arrayOfInt[b];
					if (id == victima.getMob().getModelo().getID())
						esEstatico = true;
					b++;
				}
				if (victima.esInvocacion() && !esEstatico) {
					(victima.getInvocador())._nroInvocaciones = (victima.getInvocador())._nroInvocaciones - 1;
					if (!_ordenJugadores.isEmpty()) {
						int index = _ordenJugadores.indexOf(victima);
						if (index != -1) {
							if (_nroOrdenLuc >= index && _nroOrdenLuc > 0)
								_nroOrdenLuc = (byte) (_nroOrdenLuc - 1);
							_ordenJugadores.remove(index);
						}
						if (_nroOrdenLuc < 0) {
							_tempAccion = "";
							return;
						}
						if (_equipo1.containsKey(Integer.valueOf(idVictima))) {
							_equipo1.remove(Integer.valueOf(idVictima));
						} else if (_equipo2.containsKey(Integer.valueOf(idVictima))) {
							_equipo2.remove(Integer.valueOf(idVictima));
						}
						SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 999,
								(new StringBuilder(String.valueOf(idVictima))).toString(), stringOrdenJugadores());
						if (victima.puedeJugar() && asesino.getID() == idVictima) {
							_tempAccion = "";
							finTurno(victima);
						}
						Thread.sleep(500L);
					}
				}
			} catch (Exception exception) {
			}
		ArrayList<Glifo> glifos = new ArrayList<Glifo>();
		glifos.addAll(_glifos);
		for (Glifo glifo : glifos) {
			if (glifo.getLanzador().getID() == idVictima) {
				int celdaID = glifo.getCelda().getID();
				SocketManager.ENVIAR_GDZ_ACTUALIZA_ZONA_EN_PELEA(this, 7, "-", celdaID, glifo.getTamaño(), 4);
				SocketManager.ENVIAR_GDC_ACTUALIZAR_CELDA_EN_PELEA(this, 7, celdaID);
				_glifos.remove(glifo);
			}
		}
		ArrayList<Trampa> trampas = new ArrayList<Trampa>();
		trampas.addAll(_trampas);
		for (Trampa trampa : trampas) {
			if (trampa.getLanzador().getID() == idVictima) {
				trampa.desaparecer();
				_trampas.remove(trampa);
			}
		}
		_tempAccion = "";
		if (_tipo == 5 && victima.esRecaudador()) {
			acaboPelea(true, false);
		} else if (_tipo == 2 && victima.esPrisma()) {
			acaboPelea(true, false);
		} else if (_muertesLuchInic1.size() == _cantLucEquipo1 || _muertesLuchInic2.size() == _cantLucEquipo2) {
			acaboPelea(false, false);
		} else if (victima.puedeJugar()
				&& ((victima.getMob() != null && !victima.esInvocacion()) || victima.getMob() == null)) {
			finTurno(victima);
		}
	}

	private Timer resetFinTurno(final Luchador luch) {
		ActionListener accion = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				_esperarTempAccion.stop();
				Combate fight = Combate.this;
				fight._cantTimer = fight._cantTimer + 1;
				finTurno(luch);
			}
		};
		return new Timer(500, accion);
	}

	public synchronized void finTurno(Luchador luch) {
		Luchador luchador;
		if (_estadoPelea >= 4 || _nroOrdenLuc < 0) {
			return;
		}
		if (!_tempAccion.isEmpty() && _cantTimer < 10) {
			_esperarTempAccion = resetFinTurno(luch);
			_esperarTempAccion.start();
			return;
		}
		++_cantFinTurno;
		_tiempoInicioTurno = 0L;
		if (luch == null) {
			if (_nroOrdenLuc >= _ordenJugadores.size()) {
				_nroOrdenLuc = 0;
			}
			luchador = _ordenJugadores.get(_nroOrdenLuc);
		} else {
			luchador = luch;
		}
		if (_cantFinTurno > 1 || !luchador.puedeJugar()) {
			return;
		}
		luchador.setPuedeJugar(false);
		try {
			SocketManager.ENVIAR_GTF_FIN_DE_TURNO(this, 7, luchador.getID());
			try {
				Thread.sleep(250L);
			} catch (Exception exception) {}
			if (!luchador._estaRetirado) {
				_tempAccion = "";
				if (!luchador._estaMuerto) {
					for (EfectoHechizo EH : luchador.getBuffsPorEfectoID(131)) {
						int pDa\u00f1os;
						int cadaCuantosPA = EH.getValor();
						int val = -1;
						try {
							val = Integer.parseInt(EH.getArgs().split(";")[1]);
						} catch (Exception exception) {
							// empty catch block
						}
						if (val == -1)
							continue;
						int nroPAusados = (int) Math.floor(_tempLuchadorPAusados / cadaCuantosPA);
						int da\u00f1oFinTurno = val * nroPAusados;
						Personaje.Stats totalLanz = EH.getLanzador().getTotalStatsConBuff();
						int inte = 0;
						if (EH.getHechizoID() == 200 && (inte = totalLanz.getEfecto(126)) < 0) {
							inte = 0;
						}
						if ((pDa\u00f1os = totalLanz.getEfecto(138)) < 0) {
							pDa\u00f1os = 0;
						}
						int da\u00f1os = totalLanz.getEfecto(112);
						da\u00f1oFinTurno = (100 + inte + pDa\u00f1os) / 100 * da\u00f1oFinTurno + da\u00f1os;
						if (luchador.tieneBuff(105)) {
							SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 105, String.valueOf(luchador.getID()),
									String.valueOf(luchador.getID()) + "," + luchador.getBuff(105).getValor());
							da\u00f1oFinTurno -= luchador.getBuff(105).getValor();
						}
						if (da\u00f1oFinTurno <= 0)
							continue;
						if (da\u00f1oFinTurno > luchador.getPDVConBuff()) {
							da\u00f1oFinTurno = luchador.getPDVConBuff();
						}
						luchador.restarPDV(da\u00f1oFinTurno);
						da\u00f1oFinTurno = -da\u00f1oFinTurno;
						SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 100, String.valueOf(EH.getLanzador().getID()),
								String.valueOf(luchador.getID()) + "," + da\u00f1oFinTurno);
					}
					ArrayList<Glifo> glifos = new ArrayList<Glifo>();
					glifos.addAll(_glifos);
					for (Glifo glifo : glifos) {
						if (_estadoPelea >= 4) {
							return;
						}
						short dist = Pathfinding.distanciaEntreDosCeldas(_mapaCopia, luchador.getCeldaPelea().getID(),
								glifo.getCelda().getID());
						if (dist > glifo.getTama\u00f1o() || glifo._hechizos != 476)
							continue;
						glifo.activarGlifo(luchador);
					}
					if (luchador.getPDVConBuff() <= 0) {
						agregarAMuertos(luchador);
					}
					Personaje perso = luchador.getPersonaje();
					if ((_tipo == 4 || _tipo == 3) && perso != null) {
						TreeMap<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
						copiaRetos.putAll(_retos);
						for (Entry<Integer, Integer> entry : copiaRetos.entrySet()) { //FIXME Is this really working?
							int reto = (Integer) entry.getKey();
							int exitoReto = (Integer) entry.getValue();
							if (exitoReto != 0)
								continue;
							switch (reto) {
							case 1: {
								if (_tempLuchadorPMusados != 0)
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 2: {
								if (luchador._idCeldaIniTurnoReto == luchador._celda.getID())
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 7: {
								if (!hechizoDisponible(luchador, 367))
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 8: {
								if (_tempLuchadorPM <= 0)
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 12: {
								if (!hechizoDisponible(luchador, 373))
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 14: {
								if (!hechizoDisponible(luchador, 101))
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 15: {
								if (!hechizoDisponible(luchador, 370))
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 36: {
								if (Pathfinding.hayAlrededor(_mapaCopia, this, luchador, false))
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 37: {
								if (Pathfinding.hayAlrededor(_mapaCopia, this, luchador, true))
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 39: {
								if (!Pathfinding.hayAlrededor(_mapaCopia, this, luchador, true))
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 40: {
								if (!Pathfinding.hayAlrededor(_mapaCopia, this, luchador, false))
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							case 41: {
								if (_tempLuchadorPA <= 0)
									break;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
							}
							}
							if (exitoReto == 0)
								continue;
							_retos.remove(reto);
							_retos.put(reto, exitoReto);
						}
					}
					_tempLuchadorPAusados = 0;
					_tempLuchadorPMusados = 0;
					Personaje.Stats statsConBuff = luchador.getTotalStatsConBuff();
					_tempLuchadorPA = (short) statsConBuff.getEfecto(111);
					_tempLuchadorPM = (short) statsConBuff.getEfecto(128);
					luchador.actualizarBuffsPelea();
					if (perso != null && perso.enLinea()) {
						SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
					}
					SocketManager.ENVIAR_GTM_INFO_STATS_TODO_LUCHADORES(this, 7);
					SocketManager.ENVIAR_GTR_TURNO_LISTO(this, 7,
							_nroOrdenLuc == _ordenJugadores.size() ? _ordenJugadores.get(0).getID() : luchador.getID());
				}
			}
			inicioTurno();
		} catch (NullPointerException e) {
			finTurno(luchador);
		}
	}

	public void addTiempoReset(int tiempo) {
		_tiempoResetAccion += tiempo;
	}

	public int intentarLanzarHechizo(Luchador lanzador, Hechizo.StatsHechizos sHechizo, short celdaID) {
		Personaje perso = lanzador.getPersonaje();
		if (!_tempAccion.isEmpty() && perso != null) {
			return 10;
		}
		if (sHechizo == null) {
			return 10;
		}
		Mapa.Celda celda = _mapaCopia.getCelda(celdaID);
		_tempAccion = "lanzando hechizo";
		_tiempoResetAccion = 200;
		if (puedeLanzarHechizo(lanzador, sHechizo, celda, (short) -1)) {
			boolean esFC;
			if (perso != null) {
				SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
			}
			if (lanzador.getTipo() == 1 && perso.getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
				int modi = perso.getModifSetClase(sHechizo.getHechizoID(), 285);
				_tempLuchadorPA = (short) (_tempLuchadorPA - (sHechizo.getCostePA() - modi));
				_tempLuchadorPAusados = (short) (_tempLuchadorPAusados + (sHechizo.getCostePA() - modi));
			} else {
				_tempLuchadorPA = (short) (_tempLuchadorPA - sHechizo.getCostePA());
				_tempLuchadorPAusados = (short) (_tempLuchadorPAusados + sHechizo.getCostePA());
			}
			SocketManager.ENVIAR_GAS_INICIO_DE_ACCION(this, 7, lanzador.getID());
			esFC = sHechizo.getPorcFC() != 0
					&& Fórmulas.getRandomValor(1, sHechizo.getPorcFC()) == sHechizo.getPorcFC();
			if (esFC) {
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 302, String.valueOf(lanzador.getID()),
						String.valueOf(sHechizo.getHechizoID()));
			} else {
				if ((_tipo == 4 || _tipo == 3) && lanzador.getPersonaje() != null) {
					TreeMap<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
					copiaRetos.putAll(_retos);
					for (Entry<Integer, Integer> entry : copiaRetos.entrySet()) { //FIXME Is this really working?
						int reto = (Integer) entry.getKey();
						int exitoReto = (Integer) entry.getValue();
						if (exitoReto != 0)
							continue;
						switch (reto) {
						case 5: {
							if (lanzador._hechiLanzadosReto.contains(sHechizo.getHechizoID())) {
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							lanzador._hechiLanzadosReto.add(sHechizo.getHechizoID());
							break;
						}
						case 6: {
							if (lanzador._hechiLanzadosReto.contains(sHechizo.getHechizoID())) {
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
								break;
							}
							lanzador._hechiLanzadosReto.add(sHechizo.getHechizoID());
							break;
						}
						case 9: {
							SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
							exitoReto = 2;
							break;
						}
						case 18: {
							for (EfectoHechizo efecto : sHechizo.getEfectos()) {
								if (efecto.getEfectoID() != 108)
									continue;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
							}
							break;
						}
						case 20: {
							for (EfectoHechizo efecto : sHechizo.getEfectos()) {
								int efectoID = efecto.getEfectoID();
								if (efectoID < 85 || efectoID > 100 || efectoID == 90)
									continue;
								if (_elementoReto == 0) {
									_elementoReto = Constantes.efectoElemento(efectoID);
									continue;
								}
								if (Constantes.efectoElemento(efectoID) == _elementoReto)
									continue;
								SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
								exitoReto = 2;
							}
							break;
						}
						case 24: {
							int hechizoID = sHechizo.getHechizoID();
							if (lanzador._idHechiLanzReto == -1) {
								lanzador._idHechiLanzReto = hechizoID;
								break;
							}
							if (lanzador._idHechiLanzReto == hechizoID)
								break;
							SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
							exitoReto = 2;
						}
						}
						if (exitoReto == 0)
							continue;
						_retos.remove(reto);
						_retos.put(reto, exitoReto);
					}
				}
				boolean esGC = lanzador.testSiEsGC(sHechizo.getPorcGC(), sHechizo, lanzador);
				String hechizoStr = String.valueOf(sHechizo.getHechizoID()) + "," + celdaID + ","
						+ sHechizo.getSpriteID() + "," + sHechizo.getNivel() + "," + sHechizo.getSpriteInfos();
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 300, String.valueOf(lanzador.getID()), hechizoStr);
				if (esGC) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 301, String.valueOf(lanzador.getID()), hechizoStr);
				}
				try {
					sHechizo.aplicaHechizoAPelea(this, lanzador, celda, esGC);
				} catch (Exception exception) {}
				if (lanzador.getPersonaje() == null) {
					try {
						Thread.sleep(1000L);
					} catch (Exception exception) {}
				}
			}
			if (lanzador.getTipo() == 1 && perso.getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
				int modi = perso.getModifSetClase(sHechizo.getHechizoID(), 285);
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 102, String.valueOf(lanzador.getID()),
						String.valueOf(lanzador.getID()) + ",-" + (sHechizo.getCostePA() - modi));
			} else {
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 102, String.valueOf(lanzador.getID()),
						String.valueOf(lanzador.getID()) + ",-" + sHechizo.getCostePA());
			}
			SocketManager.ENVIAR_GAF_FINALIZAR_ACCION(this, 7, 0, lanzador.getID());
			if (!esFC) {
				lanzador.addHechizoLanzado(celda.getPrimerLuchador(), sHechizo, lanzador);
			}
			if (esFC && sHechizo.esFinTurnoSiFC() || sHechizo.getHechizoID() == 1735) {
				if (lanzador.getMob() != null || lanzador.esInvocacion()) {
					_tempAccion = "";
					return 5;
				}
				_tempAccion = "";
				finTurno(lanzador);
				return 5;
			}
			if (!acaboPelea(false, false)) {
				_tempAccion = "";
				return 10;
			}
		} else if (lanzador.getMob() != null || lanzador.esInvocacion() && lanzador.getPersonaje() == null) {
			_tempAccion = "";
			return 10;
		}
		try {
			Thread.sleep(_tiempoResetAccion);
		} catch (Exception exception) {}
		_tempAccion = "";
		return 0;
	}

	public void intentarCaC(Personaje perso, short celdaID) {
		Luchador lanzador = getLuchadorPorPJ(perso);
		if (lanzador == null || _ordenJugadores.get(_nroOrdenLuc).getID() != lanzador.getID()) {
			return;
		}
		if (_tipo == 4 || _tipo == 3) {
			TreeMap<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
			copiaRetos.putAll(_retos);
			for (Entry<Integer, Integer> entry : copiaRetos.entrySet()) { //FIXME Is this really working?
				int reto = (Integer) entry.getKey();
				int exitoReto = (Integer) entry.getValue();
				if (exitoReto != 0) {
					continue;
				}
				switch (reto) {
				case 5: {
					if (lanzador._hechiLanzadosReto.contains(0)) {
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
						exitoReto = 2;
						break;
					}
					lanzador._hechiLanzadosReto.add(0);
					break;
				}
				case 6: {
					if (lanzador._hechiLanzadosReto.contains(0)) {
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
						exitoReto = 2;
						break;
					}
					lanzador._hechiLanzadosReto.add(0);
					break;
				}
				case 11: {
					SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
					exitoReto = 2;
					break;
				}
				case 24: {
					int hechizoID = 0;
					if (lanzador._idHechiLanzReto == -1) {
						lanzador._idHechiLanzReto = hechizoID;
						break;
					}
					if (lanzador._idHechiLanzReto == hechizoID)
						break;
					SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
					exitoReto = 2;
				}
				}
				if (exitoReto == 0)
					continue;
				_retos.remove(reto);
				_retos.put(reto, exitoReto);
			}
		}
		if (perso.getObjPosicion(1) == null) {
			if (_tempLuchadorPA < 4) {
				return;
			}
			SocketManager.ENVIAR_GAS_INICIO_DE_ACCION(this, 7, perso.getID());
			if (lanzador.esInvisible()) {
				lanzador.hacerseVisible(-1);
			}
			Luchador objetivo = _mapaCopia.getCelda(celdaID).getPrimerLuchador();
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 303, String.valueOf(perso.getID()), String.valueOf(celdaID));
			if (objetivo != null) {
				int da\u00f1o = Fórmulas.getRandomValor("1d5+0");
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(this, lanzador, objetivo, 0, da\u00f1o, true, -1,
						false);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, lanzador, this, -1,
						false)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 100, String.valueOf(lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
			}
			_tempLuchadorPA = (short) (_tempLuchadorPA - 4);
			SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 102, String.valueOf(perso.getID()),
					String.valueOf(perso.getID()) + ",-4");
			SocketManager.ENVIAR_GAF_FINALIZAR_ACCION(this, 7, 0, perso.getID());
			if (objetivo.getPDVConBuff() <= 0) {
				agregarAMuertos(objetivo);
			}
			acaboPelea(false, false);
		} else {
			boolean esFC;
			int costePA;
			Objeto arma = perso.getObjPosicion(1);
			if (arma.getModelo().getTipo() == 83) {
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 305, String.valueOf(perso.getID()), "");
				SocketManager.ENVIAR_GAF_FINALIZAR_ACCION(this, 7, 0, perso.getID());
				finTurno(lanzador);
			}
			if (_tempLuchadorPA < (costePA = arma.getModelo().getCostePA())) {
				return;
			}
			SocketManager.ENVIAR_GAS_INICIO_DE_ACCION(this, 7, perso.getID());
			esFC = arma.getModelo().getPorcFC() != 0
					&& Fórmulas.getRandomValor(1, arma.getModelo().getPorcFC()) == arma.getModelo().getPorcFC();
			if (esFC) {
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 305, String.valueOf(perso.getID()), "");
				SocketManager.ENVIAR_GAF_FINALIZAR_ACCION(this, 7, 0, perso.getID());
				finTurno(lanzador);
			} else {
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 303, String.valueOf(perso.getID()),
						String.valueOf(celdaID));
				boolean esGC = lanzador.calculaSiGC(arma.getModelo().getPorcGC());
				if (esGC) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 301, String.valueOf(perso.getID()), "0");
				}
				if (lanzador.esInvisible()) {
					lanzador.hacerseVisible(-1);
				}
				ArrayList<EfectoHechizo> efectos = arma.getEfectos();
				if (esGC) {
					efectos = arma.getEfectosCriticos();
				}
				for (EfectoHechizo EH : efectos) {
					if (_estadoPelea != 3)
						break;
					ArrayList<Luchador> objetivos = Pathfinding.getObjetivosZonaArma(this, arma.getModelo().getTipo(),
							_mapaCopia.getCelda(celdaID), lanzador.getCeldaPelea().getID());
					EH.setTurnos(0);
					EH.aplicarAPelea(this, lanzador, objetivos, true, null);
				}
				_tempLuchadorPA = (short) (_tempLuchadorPA - costePA);
				SocketManager.ENVIAR_GA_ACCION_PELEA(this, 7, 102, String.valueOf(perso.getID()),
						String.valueOf(perso.getID()) + ",-" + costePA);
				SocketManager.ENVIAR_GAF_FINALIZAR_ACCION(this, 7, 0, perso.getID());
				acaboPelea(false, false);
			}
		}
	}

	public boolean puedeLanzarHechizo(Luchador lanzador, Hechizo.StatsHechizos sHechizo, Mapa.Celda celda,
			short celdaObjetivo) {
		boolean modif;
		int modi;
		short celdaDondeLanza = celdaObjetivo <= -1 ? lanzador.getCeldaPelea().getID() : celdaObjetivo;
		if (_ordenJugadores == null || _nroOrdenLuc < 0) {
			return false;
		}
		if (_nroOrdenLuc >= _ordenJugadores.size()) {
			_nroOrdenLuc = 0;
		}
		Luchador tempLuchador = _ordenJugadores.get(_nroOrdenLuc);
		Personaje perso = lanzador.getPersonaje();
		if (sHechizo == null) {
			if (perso != null) {
				SocketManager.ENVIAR_Im_INFORMACION(perso, "1169");
			}
			return false;
		}
		for (int estado : sHechizo.getEstadosProhi()) {
			if (!lanzador.tieneEstado(estado))
				continue;
			return false;
		}
		for (int estado : sHechizo.getEstadosNeces()) {
			if (estado == -1)
				break;
			if (lanzador.tieneEstado(estado))
				continue;
			return false;
		}
		if (tempLuchador == null || tempLuchador.getID() != lanzador.getID()) {
			if (perso != null) {
				SocketManager.ENVIAR_Im_INFORMACION(perso, "1175");
			}
			return false;
		}
		int gastarPA = 0;
		if (lanzador.getTipo() == 1 && perso.getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
			int modi2 = perso.getModifSetClase(sHechizo.getHechizoID(), 285);
			gastarPA = sHechizo.getCostePA() - modi2;
		} else {
			gastarPA = sHechizo.getCostePA();
		}
		if (_tempLuchadorPA < gastarPA) {
			if (perso != null) {
				SocketManager.ENVIAR_Im_INFORMACION(perso, "1170;" + _tempLuchadorPA + "~" + sHechizo.getCostePA());
			}
			return false;
		}
		if (celda == null) {
			if (perso != null) {
				SocketManager.ENVIAR_Im_INFORMACION(perso, "1172");
			}
			return false;
		}
		if (lanzador.getTipo() == 1 && perso.getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
			modi = perso.getModifSetClase(sHechizo.getHechizoID(), 288);
			modif = modi == 1;
			if (sHechizo.esLanzLinea() && !modif
					&& !Pathfinding.siCeldasEstanEnMismaLinea(_mapaCopia, celdaDondeLanza, celda.getID(), 'z')) {
				if (perso != null) {
					SocketManager.ENVIAR_Im_INFORMACION(perso, "1173");
				}
				return false;
			}
		} else if (sHechizo.esLanzLinea()
				&& !Pathfinding.siCeldasEstanEnMismaLinea(_mapaCopia, celdaDondeLanza, celda.getID(), 'z')) {
			if (perso != null) {
				SocketManager.ENVIAR_Im_INFORMACION(perso, "1173");
			}
			return false;
		}
		if (lanzador.getTipo() == 1 && perso.getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
			modi = perso.getModifSetClase(sHechizo.getHechizoID(), 289);
			modif = modi == 1;
			if (sHechizo.tieneLineaVuelo()
					&& !Pathfinding.checkearLineaDeVista(_mapaCopia, celdaDondeLanza, celda.getID(), lanzador)
					&& !modif) {
				if (perso != null) {
					SocketManager.ENVIAR_Im_INFORMACION(perso, "1174");
				}
				return false;
			}
		} else if (sHechizo.tieneLineaVuelo()
				&& !Pathfinding.checkearLineaDeVista(_mapaCopia, celdaDondeLanza, celda.getID(), lanzador)) {
			if (perso != null) {
				SocketManager.ENVIAR_Im_INFORMACION(perso, "1174");
			}
			return false;
		}
		short dist = Pathfinding.distanciaEntreDosCeldas(_mapaCopia, celdaDondeLanza, celda.getID());
		int maxAlc = sHechizo.getMaxAlc();
		int minAlc = sHechizo.getMinAlc();
		if (lanzador.getTipo() == 1 && perso.getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
			int modi3 = perso.getModifSetClase(sHechizo.getHechizoID(), 281);
			maxAlc += modi3;
		}
		Personaje.Stats statsConBuff = lanzador.getTotalStatsConBuff();
		if (lanzador.getTipo() == 1 && perso.getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
			boolean modif2;
			int modi4 = perso.getModifSetClase(sHechizo.getHechizoID(), 282);
			modif2 = modi4 == 1;
			if ((sHechizo.esModifAlc() || modif2) && (maxAlc += statsConBuff.getEfecto(117)) < minAlc) {
				maxAlc = minAlc;
			}
		} else if (sHechizo.esModifAlc() && (maxAlc += statsConBuff.getEfecto(117)) < minAlc) {
			maxAlc = minAlc;
		}
		if (dist < minAlc || dist > maxAlc) {
			if (perso != null) {
				SocketManager.ENVIAR_Im_INFORMACION(perso, "1171;" + minAlc + "~" + maxAlc + "~" + dist);
			}
			return false;
		}
		if (!HechizoLanzado.poderSigLanzamiento(lanzador, sHechizo.getHechizoID())) {
			return false;
		}
		int nroLanzTurno = sHechizo.getMaxLanzPorTurno();
		if (lanzador.getTipo() == 1 && perso.getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
			int modi5 = perso.getModifSetClase(sHechizo.getHechizoID(), 290);
			nroLanzTurno = sHechizo.getMaxLanzPorTurno() + modi5;
		}
		if (nroLanzTurno - HechizoLanzado.getNroLanzamientos(lanzador, sHechizo.getHechizoID()) <= 0
				&& nroLanzTurno > 0) {
			return false;
		}
		Luchador objetivo = celda.getPrimerLuchador();
		int nroLanzObjetivo = sHechizo.getMaxLanzPorJugador();
		if (lanzador.getTipo() == 1 && perso.getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
			int modi6 = perso.getModifSetClase(sHechizo.getHechizoID(), 291);
			nroLanzObjetivo = sHechizo.getMaxLanzPorJugador() + modi6;
		}
		return nroLanzObjetivo - HechizoLanzado.getNroLanzPorObjetivo(lanzador, objetivo, sHechizo.getHechizoID()) > 0
				|| nroLanzObjetivo <= 0;
	}

	public String getPanelResultados(int equipoGanador) {
		int n;
		long tiempo = System.currentTimeMillis() - _tiempoInicio;
		int initID = _idLuchInit1;
		int tipoX = 0;
		if (_tipo == 1 || _tipo == 2 || _tipo == 6) {
			tipoX = 1;
		}
		boolean exito = false;
		if (_tipo == 4 || _tipo == 3) {
			int exitoReto;
			int reto;
			TreeMap<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
			copiaRetos.putAll(_retos);
			if (equipoGanador == 1) {
				for (Entry<Integer, Integer> entry : copiaRetos.entrySet()) { //FIXME Is this really working?
					reto = (Integer) entry.getKey();
					exitoReto = (Integer) entry.getValue();
					switch (reto) {
					case 33: {
						for (Luchador luchador : _inicioLucEquipo1) {
							if (!luchador._estaMuerto)
								continue;
							exitoReto = 2;
							SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
							break;
						}
						break;
					}
					case 44:
					case 46: {
						for (Luchador luchador : _inicioLucEquipo1) {
							if (luchador._mobMatadosReto.size() <= 0)
								continue;
							exitoReto = 2;
							SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
							break;
						}
						break;
					}
					}
					if (exitoReto == 2)
						continue;
					if (exitoReto == 0) {
						exitoReto = 1;
						SocketManager.ENVIAR_GdaK_RETO_REALIZADO(this, reto);
					}
					exito = true;
					_retos.remove(reto);
					_retos.put(reto, exitoReto);
				}
			} else {
				for (Entry<Integer, Integer> entry : copiaRetos.entrySet()) { //FIXME Is this really working?
					reto = (Integer) entry.getKey();
					exitoReto = (Integer) entry.getValue();
					if (exitoReto != 0)
						continue;
					exitoReto = 2;
					SocketManager.ENVIAR_GdaO_RETO_PERDIDO(this, reto);
					_retos.remove(reto);
					_retos.put(reto, exitoReto);
				}
			}
		}
		StringBuffer packet = new StringBuffer("GE");
		ArrayList<Luchador> ganadores = new ArrayList<Luchador>();
		ArrayList<Luchador> perdedores = new ArrayList<Luchador>();
		if (equipoGanador == 1) {
			ganadores.addAll(_equipo1.values());
			perdedores.addAll(_equipo2.values());
		} else {
			ganadores.addAll(_equipo2.values());
			perdedores.addAll(_equipo1.values());
		}
		Personaje pj1 = null;
		long exp = 0L;
		for (Luchador luchador : ganadores) {
			Objeto nuevo;
			pj1 = luchador.getPersonaje();
			if (luchador.esInvocacion() || luchador.getMob() != null)
				continue;
			if (_tipo == 1 && pj1 != null) {
				if (!Mundo.estaRankingPVP(pj1.getID())) {
					RankingPVP rank = new RankingPVP(pj1.getID(), pj1.getNombre(), 0, 0, pj1.getAlineacion());
					Mundo.addRankingPVP(rank);
					SQLManager.INSERT_RANKINGPVP(rank);
					rank.aumentarVictoria();
				} else {
					Mundo.getRanking(pj1.getID()).aumentarVictoria();
				}
			}
			if (_tipo == 1 && pj1 != null && _misionPVP > 0) {
				if (_equipo1.containsValue(luchador) && _misionPVP == 1) {
					int kamas;
					if (pj1.tieneObjModeloNoEquip(10085, 1)) {
						pj1.eliminarObjetoPorModelo(10085);
					}
					if ((kamas = pj1.getMisionPVP().getKamasRecompensa()) > 0) {
						pj1.setKamas(pj1.getKamas() + (long) kamas);
						SocketManager.ENVIAR_Im_INFORMACION(pj1, "045;" + kamas);
						_misionPVP = 3;
					} else {
						Objeto nuevo2 = Mundo.getObjModelo(10275).crearObjDesdeModelo(LesGuardians.CHAPAS_MISION,
								false);
						if (!pj1.addObjetoSimilar(nuevo2, true, -1)) {
							Mundo.addObjeto(nuevo2, true);
							pj1.addObjetoPut(nuevo2);
							SocketManager.ENVIAR_OAKO_APARECER_OBJETO(pj1, nuevo2);
						}
						exp = (long) ((float) Fórmulas.getXPMision(pj1.getNivel()) * LesGuardians.RATE_XP_PVP);
						pj1.addExp(exp);
						SocketManager.ENVIAR_Im_INFORMACION(pj1, "08;" + exp);
					}
					pj1.setMisionPVP(null);
					continue;
				}
				exp = (long) ((float) Fórmulas.getXPMision(pj1.getNivel()) * LesGuardians.RATE_XP_PVP);
				pj1.addExp(exp);
				SocketManager.ENVIAR_Im_INFORMACION(pj1, "08;" + exp);
				nuevo = Mundo.getObjModelo(10275).crearObjDesdeModelo(LesGuardians.CHAPAS_MISION, false);
				if (pj1.addObjetoSimilar(nuevo, true, -1))
					continue;
				Mundo.addObjeto(nuevo, true);
				pj1.addObjetoPut(nuevo);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(pj1, nuevo);
				continue;
			}
			if (_tipo == 4 && pj1 != null) {
				float porc = Mundo.getBalanceMundo(pj1.getAlineacion()) / 100.0f;
				float porcN = (float) Math.rint((double) pj1.getNivelAlineacion() / 2.5);
				luchador._bonusAlineacion = porc * porcN;
				continue;
			}
			if (_tipo != 6 || pj1 == null)
				continue;
			if (pj1.tieneObjModeloNoEquip(11158, 1)) {
				pj1.eliminarObjetoPorModelo(11158);
			}
			if (!pj1.addObjetoSimilar(nuevo = Mundo.getObjModelo(11158).crearObjDesdeModelo(1, false), true, -1)) {
				Mundo.addObjeto(nuevo, true);
				pj1.addObjetoPut(nuevo);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(pj1, nuevo);
			}
			pj1.setEnKoliseo(false);
		}
		for (Luchador luchador : perdedores) {
			if (luchador.esInvocacion())
				continue;
			Personaje pj2 = luchador.getPersonaje();
			if (_tipo == 6 && pj2 != null) {
				pj1.setEnKoliseo(false);
			} else if (_tipo == 1 && pj2 != null && _misionPVP > 0 && _equipo2.containsValue(luchador)
					&& _misionPVP == 2) {
				if (pj2.tieneObjModeloNoEquip(10085, 1)) {
					pj2.eliminarObjetoPorModelo(10085);
				}
				pj2.setMisionPVP(null);
			}
			if (_tipo != 1 || pj2 == null)
				continue;
			if (!Mundo.estaRankingPVP(pj2.getID())) {
				RankingPVP rank = new RankingPVP(pj2.getID(), pj2.getNombre(), 0, 0, pj2.getAlineacion());
				Mundo.addRankingPVP(rank);
				SQLManager.INSERT_RANKINGPVP(rank);
				rank.aumentarDerrota();
				continue;
			}
			Mundo.getRanking(pj2.getID()).aumentarDerrota();
		}
		int grupoPP = 0;
		long minkamas = 0L;
		long maxkamas = 0L;
		int grupoPPIntacta = 0;
		int PPreto = 0;
		int XPreto = 0;
		float coefEstrella = 0.0f;
		if (_estrellas != 0) {
			coefEstrella = (float) _estrellas / 100.0f;
		}
		int estrellaPP = 0;
		int estrellaXP = 0;
		long totalXP = 0L;
		ArrayList<Luchador> ordenLuchMasAMenosPP = new ArrayList<Luchador>();
		Luchador lucConMaxPP = null;
		ArrayList<Mundo.Drop> posibleDrops = new ArrayList<Mundo.Drop>();
		if (LesGuardians.MODO_HEROICO && _tipo != 0 && !LesGuardians._cerrando) {
			for (Luchador perdedor : perdedores) {
				MobGrado mobGrado = perdedor.getMob();
				Personaje pjPerd = perdedor.getPersonaje();
				if (mobGrado != null) {
					_objetosRobados.addAll(((MobModelo.MobGrado) ((Object) mobGrado)).getObjetoHeroico());
					((MobModelo.MobGrado) ((Object) mobGrado)).borrarObjetosHeroico();
					_kamasRobadas += ((MobModelo.MobGrado) ((Object) mobGrado)).getKamasHeroico() / 2L;
					((MobModelo.MobGrado) ((Object) mobGrado)).borrarKamasHeroico();
					continue;
				}
				if (pjPerd == null)
					continue;
				ArrayList<Objeto> objPerder = pjPerd.getObjetosEquipados();
				_objetosRobados.addAll(objPerder);
				_kamasRobadas += pjPerd.getKamas() / 2L;
				pjPerd.setKamas(pjPerd.getKamas() / 2L);
				for (Objeto object : objPerder) {
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(pjPerd, object.getID());
					pjPerd.borrarObjetoSinEliminar(object.getID());
				}
				int n2 = pjPerd.getObjetos().size() / 2;
				TreeMap<Integer, Objeto> objetos = new TreeMap<Integer, Objeto>();
				objetos.putAll(pjPerd.getObjetos());
				int c2 = 0;
				for (Entry<Integer, Objeto> oxx : objetos.entrySet()) {
					if (c2 > n2) {
						continue;
					}
					if (((Objeto) oxx.getValue()).getPosicion() != -1) {
						continue;
					}
					_objetosRobados.add((Objeto) oxx.getValue());
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(pjPerd, (int) ((Integer) oxx.getKey()));
					pjPerd.borrarObjetoSinEliminar((Integer) oxx.getKey());
					++c2;
				}
			}
		}
		if ((_tipo == 4 || _tipo == 5) && equipoGanador == 1) {
			for (Luchador ganador : ganadores) {
				if (ganador.esInvocacion() && (ganador.getMob() == null || ganador.getMob().getModelo().getID() != 285))
					continue;
				int prosp = ganador.getTotalStatsConBuff().getEfecto(176);
				ganador._prospeccionTemporal = prosp;
				Personaje pjGanador = ganador.getPersonaje();
				if (pjGanador != null) {
					Mascota pets;
					if (pjGanador.getAlineacion() == 1 || pjGanador.getAlineacion() == 2) {
						if (_mapaReal.getSubArea().getAlineacion() == pjGanador.getAlineacion()) {
							ganador._prospeccionTemporal = (int) ((float) prosp
									+ (float) prosp * ganador._bonusAlineacion);
						} else {
							ganador._prospeccionTemporal = (int) ((float) prosp
									- (float) prosp * ganador._bonusAlineacion);
						}
					}
					if (_tipo == 4 && (pets = pjGanador.getMascota()) != null && pets.esDevoraAlmas()) {
						boolean comio = false;
						for (Map.Entry<Integer, Integer> entry : _mobGrupo.getAlmasMobs().entrySet()) {
							if (!pets.esComestible(entry.getKey()))
								continue;
							comio = true;
							pets.comerAlma(entry.getKey(), entry.getValue());
						}
						if (comio) {
							SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(pjGanador, Mundo.getObjeto(pets.getID()));
						}
					}
				}
				grupoPP += ganador._prospeccionTemporal;
			}
			grupoPPIntacta = grupoPP;
			if (grupoPP < 0) {
				grupoPP = 0;
			}
			if (exito) {
				for (Map.Entry<Integer, Integer> entry : _retos.entrySet()) {
					int reto = entry.getKey();
					int exitoReto = entry.getValue();
					if (exitoReto == 2)
						continue;
					Reto desafios = Mundo.getReto(reto);
					PPreto += desafios.bonusPP();
					XPreto += desafios.bonusXP();
				}
			}
			if (coefEstrella > 0.0f) {
				estrellaPP = (int) ((float) grupoPP * coefEstrella);
			}
			grupoPP += grupoPP * PPreto / 100 + estrellaPP;
			for (Luchador perdedor : perdedores) {
				MobGrado mob = perdedor.getMob();
				if (perdedor.esInvocacion() || mob == null) {
					continue;
				}
				MobModelo mobModelo = mob.getModelo();
				minkamas += mobModelo.getMinKamas();
				maxkamas += mobModelo.getMaxKamas();
				for (Drop drop : mobModelo.getDrops()) { // FIXME confirmar buen funcionamiento de este ciclo for
					int prospReq = (int) (drop.getProspReq() * Fórmulas.PROSP_REQ);
					if (prospReq <= grupoPP) {
						float div = grupoPP / prospReq;
						int nuevaP = (int) (drop.getProbabilidad() * div) + LesGuardians.RATE_DROP;
						int random = Fórmulas.getRandomValor(1, 100);
						if (random > nuevaP) {
							continue;
						}
						int cant = grupoPP * drop.getProbabilidad() * LesGuardians.RATE_DROP;
						posibleDrops.add(new Drop(drop.getObjetoID(), 0, cant, drop.getDropMax()));
					}
				}
			}
			Map<Integer, Luchador> todosConPP = new TreeMap<Integer, Luchador>();
			for (Luchador ganador : ganadores) {
				int prosp = ganador.getTotalStatsConBuff().getEfecto(176);
				while (todosConPP.containsKey(prosp)) {
					++prosp;
				}
				todosConPP.put(prosp, ganador);
			}
			while (ordenLuchMasAMenosPP.size() < ganadores.size()) {
				int tempPP = -1;
				for (Entry<Integer, Luchador> entry : todosConPP.entrySet()) {
					if (entry.getKey() > tempPP && !ordenLuchMasAMenosPP.contains(entry.getValue())) {
						lucConMaxPP = entry.getValue();
						tempPP = entry.getKey();
					}
				}
				ordenLuchMasAMenosPP.add(lucConMaxPP);
			}
			ganadores.clear();
			ganadores.addAll(ordenLuchMasAMenosPP);
			for (Luchador perdedor : perdedores) {
				if (perdedor.esInvocacion() || perdedor.getMob() == null) {
					continue;
				}
				totalXP += perdedor.getMob().getBaseXp();
			}
			if (coefEstrella > 0.0f) {
				estrellaXP = (int) ((float) totalXP * coefEstrella);
			}
			totalXP = totalXP + totalXP * (long) XPreto / 100L + (long) estrellaXP;
			boolean mobCapturable = true;
			for (Luchador perdedor : perdedores) {
				try {
					mobCapturable &= perdedor.getMob().getModelo().esCapturable();
				} catch (Exception e) {
					mobCapturable = false;
					break;
				}
			}
			this._esCapturable |= mobCapturable;
			if (this._esCapturable) {
				boolean primero = false;
				int maxNivel = 0;
				String piedraStats = "";
				for (Luchador perdedor : perdedores) {
					if (primero)
						piedraStats = String.valueOf(piedraStats) + ",";
					piedraStats = String.valueOf(piedraStats) + "26f#" + Integer.toHexString(perdedor.getNivel())
							+ "#0#" + Integer.toHexString(perdedor.getMob().getModelo().getID());
					primero = true;
					if (perdedor.getNivel() > maxNivel)
						maxNivel = perdedor.getNivel();
				}
				_piedraAlma = new PiedraAlma(Mundo.getSigIDObjeto(), 1, 7010, -1, piedraStats);
				for (Luchador ganador : ganadores) {
					if (ganador.esInvocacion() || !ganador.tieneEstado(2))
						continue;
					_capturadores.add(ganador);
				}
				if (_capturadores.size() > 0 && !_mapaReal.esArena()) {
					int i = 0;
					while (i < _capturadores.size()) {
						try {
							Luchador capt = _capturadores.get(Fórmulas.getRandomValor(0, _capturadores.size() - 1));
							Personaje capturador = capt.getPersonaje();
							Objeto objPos1 = capturador.getObjPosicion(1);
							if (objPos1.getModelo().getTipo() != 83) {
								_capturadores.remove(capt);
								continue;
							}
							Mundo.Duo<Integer, Integer> piedraJug = Fórmulas.decompilarPiedraAlma(objPos1);
							if ((Integer) piedraJug._segundo < maxNivel) {
								_capturadores.remove(capt);
								continue;
							}
							int suerteCaptura = Fórmulas.totalPorcCaptura((Integer) piedraJug._primero, capturador);
							if (Fórmulas.getRandomValor(1, 100) > suerteCaptura)
								continue;
							n = objPos1.getID();
							capturador.borrarObjetoSinEliminar(n);
							SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(capturador, n);
							_capturadorGanador = capt._id;
						} catch (NullPointerException localNullPointerException) {
							++i;
						}
					}
				}
			}
		}
		int k = 0;
		for (Luchador luch : ganadores) {
			if (luch._estaRetirado || luch.esInvocacion())
				continue;
			++k;
		}
		if (k < 1) {
			k = 1;
		}
		int repart = _objetosRobados.size() / k;
		if (_tipo == 4) {
			packet.append(String.valueOf(tiempo) + ";" + _estrellas + "|" + initID + "|" + tipoX + "|");
		} else {
			packet.append(String.valueOf(tiempo) + "|" + initID + "|" + tipoX + "|");
		}
		for (Luchador ganador : ganadores) {
			if (ganador._estaRetirado || ganador._doble != null)
				continue;
			String drops = "";
			long l = 0L;
			long xpGanada = 0L;
			Personaje pjGanador = ganador.getPersonaje();
			if (LesGuardians.MODO_HEROICO && _tipo != 0 && !LesGuardians._cerrando) {
				for (int i = 0; i < repart; ++i) {
					int index = Fórmulas.getRandomValor(0, _objetosRobados.size() - 1);
					Objeto o = _objetosRobados.get(index);
					o.setPosicion(-1);
					if (pjGanador != null) {
						if (!pjGanador.addObjetoSimilar(o, true, o.getID())) {
							pjGanador.addObjetoPut(o);
							SocketManager.ENVIAR_OAKO_APARECER_OBJETO(pjGanador, o);
							l += _kamasRobadas / (long) (k * 4);
							pjGanador.addKamas(_kamasRobadas / (long) (k * 4));
						}
					} else if (ganador.getMob() != null) {
						ganador.getMob().addObjetoHeroico(o);
						ganador.getMob().addKamasHeroico(_kamasRobadas / (long) (k * 4));
						l += _kamasRobadas / (long) (k * 4);
					}
					if (drops.length() > 0) {
						drops = String.valueOf(drops) + ",";
					}
					drops = String.valueOf(drops) + o.getModelo().getID() + "~" + o.getCantidad();
					_objetosRobados.remove(index);
				}
			}
			if (tipoX == 0) {
				if (ganador.esInvocacion() && pjGanador == null && ganador.getMob().getModelo().getID() != 285)
					continue;
				xpGanada = Fórmulas.getXpGanadaPVM(ganador, ganadores, perdedores, totalXP);
				long xpParaGremio = Fórmulas.getXPDonadaGremio(ganador, xpGanada);
				xpGanada -= xpParaGremio * 10L;
				long xpParaDragopavo = 0L;
				if (pjGanador != null && pjGanador.estaMontando()) {
					xpParaDragopavo = Fórmulas.getXPDonadaDragopavo(ganador, xpGanada);
					pjGanador.getMontura().addXp(xpParaDragopavo);
					SocketManager.ENVIAR_Re_DETALLES_MONTURA(pjGanador, "+", pjGanador.getMontura());
				}
				xpGanada -= xpParaDragopavo * 100L;
				l += Fórmulas.getKamasGanadas(ganador, minkamas, maxkamas);
				if (pjGanador != null && (pjGanador.getAlineacion() == 1 || pjGanador.getAlineacion() == 2)) {
					if (_mapaReal.getSubArea().getAlineacion() == pjGanador.getAlineacion()) {
						xpGanada += (long) ((float) xpGanada * ganador._bonusAlineacion);
						l += (long) ((float) l * ganador._bonusAlineacion);
					} else {
						xpGanada -= (long) ((float) xpGanada * ganador._bonusAlineacion);
						l -= (long) ((float) l * ganador._bonusAlineacion);
					}
					if (xpGanada < 0L) {
						xpGanada = 0L;
					}
					if (l < 0L) {
						l = 0L;
					}
				}
				ArrayList<Mundo.Drop> tempDrops = new ArrayList<Mundo.Drop>();
				tempDrops.addAll(posibleDrops);
				TreeMap<Integer, Integer> objetosGanados = new TreeMap<Integer, Integer>();
				int veces = 0;
				int parteCorresponde = 100;
				int maximo = 0;
				int canDrops = 0;
				if (_tipo == 4 || _tipo == 5) {
					if (pjGanador != null) {
						parteCorresponde = grupoPPIntacta
								/ (ganador._prospeccionTemporal <= 0 ? 1 : ganador._prospeccionTemporal);
					} else {
						maximo = 3;
					}
					if (_tipo == 4) {
						for (Mundo.Drop drop : tempDrops) {
							canDrops += drop.getDropMax();
						}
						maximo = canDrops / (parteCorresponde <= 0 ? 1000 : parteCorresponde);
					}
				}
				if (_tipo == 5 && pjGanador != null) {
					if (pjGanador.getGremio() == null || _Recaudador.getGremioID() != pjGanador.getGremio().getID()) {
						Collection<Objeto> objRecaudador = null;
						ArrayList<Integer> idObjetos = new ArrayList<Integer>();
						objRecaudador = _Recaudador.getObjetos().values();
						if (objRecaudador.size() > 0) {
							maximo = objRecaudador.size() / parteCorresponde;
							for (Objeto objeto : objRecaudador) {
								if (objeto == null) {
									continue;
								}
								if (drops.length() > 0) {
									drops = String.valueOf(drops) + ",";
								}
								drops = String.valueOf(drops) + objeto.getModelo().getID() + "~" + objeto.getCantidad();
								if (pjGanador.addObjetoSimilar(objeto, true, -1)) {
									Mundo.eliminarObjeto(objeto.getID());
								} else {
									pjGanador.addObjetoPut(objeto);
									SocketManager.ENVIAR_OAKO_APARECER_OBJETO(pjGanador, objeto);
								}
								idObjetos.add(objeto.getID());
								if (++veces >= maximo)
									break;
							}
							for (int integer : idObjetos) {
								_Recaudador.borrarObjeto(integer);
							}
						}
					}
				} else {
					for (Drop drop : tempDrops) {
						ObjetoModelo OM = Mundo.getObjModelo(drop.getObjetoID());
						if (OM == null) {
							continue;
						}
						Random random = new Random();
						int cant = random.nextInt(drop.getDropMax());
						if (cant == 0) {
							continue;
						}
						veces++;
						int id = OM.getID();
						objetosGanados.put(id,
								(objetosGanados.get(id = OM.getID()) == null ? 0 : (Integer) objetosGanados.get(id))
										+ cant);
						drop.setDropMax(drop.getDropMax() - cant);
						if (drop.getDropMax() <= 0) {
							posibleDrops.remove(drop);
						}
						if (veces >= maximo)
							break;
					}
					if (_tipo == 3) {
						for (Luchador luchador : perdedores) {
							int idMob;
							MobModelo.MobGrado mob = luchador.getMob();
							if (luchador.esInvocacion() || mob == null
									|| Constantes.getDoplonDopeul(idMob = mob.getModelo().getID()) == -1)
								continue;
							objetosGanados.put(Constantes.getDoplonDopeul(idMob), 1);
							objetosGanados.put(Constantes.getCertificadoDopeul(idMob), 1);
						}
					} else if (ganador._id == _capturadorGanador && _piedraAlma != null) {
						if (drops.length() > 0) {
							drops = String.valueOf(drops) + ",";
						}
						drops = String.valueOf(drops) + "7010~1";
						if (!pjGanador.addObjetoSimilar(_piedraAlma, false, -1)) {
							Mundo.addObjeto(_piedraAlma, true);
							pjGanador.addObjetoPut(_piedraAlma);
							SocketManager.ENVIAR_OAKO_APARECER_OBJETO(pjGanador, (Objeto) _piedraAlma);
						}
					}
					for (Entry<Integer, Integer> entry : objetosGanados.entrySet()) {
						ObjetoModelo OM = Mundo.getObjModelo(((Integer) entry.getKey()).intValue());
						if (OM == null) {
							continue;
						}
						if (drops.length() > 0) {
							drops = String.valueOf(drops) + ",";
						}
						drops = String.valueOf(drops) + entry.getKey() + "~" + entry.getValue();
						Objeto obj = OM.crearObjDesdeModelo((Integer) entry.getValue(), false);
						if (pjGanador == null) {
							Personaje invocador = ganador.getInvocador().getPersonaje();
							if (invocador.addObjetoSimilar(obj, true, -1))
								continue;
							Mundo.addObjeto(obj, true);
							invocador.addObjetoPut(obj);
							SocketManager.ENVIAR_OAKO_APARECER_OBJETO(invocador, obj);
							continue;
						}
						if (pjGanador.addObjetoSimilar(obj, true, -1))
							continue;
						Mundo.addObjeto(obj, true);
						pjGanador.addObjetoPut(obj);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(pjGanador, obj);
					}
				}
				if (xpGanada != 0L && pjGanador != null) {
					pjGanador.addExp(xpGanada);
				}
				if (l != 0L && pjGanador != null) {
					pjGanador.addKamas(l);
				}
				if (xpParaGremio > 0L && pjGanador.getMiembroGremio() != null) {
					pjGanador.getMiembroGremio().darXpAGremio(xpParaGremio);
				}
				packet.append("2;" + ganador.getID() + ";" + ganador.getNombreLuchador() + ";" + ganador.getNivel()
						+ ";" + (ganador._estaMuerto ? "1" : "0") + ";");
				packet.append(String.valueOf(ganador.xpStringLuch(";")) + ";");
				packet.append((xpGanada == 0L ? "" : Long.valueOf(xpGanada)) + ";");
				packet.append((xpParaGremio == 0L ? "" : Long.valueOf(xpParaGremio)) + ";");
				packet.append((xpParaDragopavo == 0L ? "" : Long.valueOf(xpParaDragopavo)) + ";");
				packet.append(String.valueOf(drops) + ";");
				packet.append((l == 0L ? "" : Long.valueOf(l)) + "|");
				if (pjGanador == null || !pjGanador.enLinea())
					continue;
				SocketManager.ENVIAR_Ow_PODS_DEL_PJ(pjGanador);
				continue;
			}
			if (ganador.esInvocacion() && pjGanador == null)
				continue;
			int honor = 0;
			int deshonor = 0;
			if (pjGanador != null) {
				int maxHonor;
				packet.append("2;" + ganador.getID() + ";" + ganador.getNombreLuchador() + ";" + ganador.getNivel()
						+ ";" + (ganador._estaMuerto ? "1" : "0") + ";"
						+ (pjGanador.getAlineacion() != -1
								? Mundo.getExpNivel((int) pjGanador.getNivelAlineacion())._pvp
								: 0)
						+ ";" + pjGanador.getHonor() + ";");
				if (_tipo == 1) {
					if (_luchInit2.getPersonaje().getAlineacion() != 0
							&& _luchInit1.getPersonaje().getAlineacion() != 0) {
						if (_luchInit2.getPersonaje().getCuenta().getActualIP()
								.compareTo(_luchInit1.getPersonaje().getCuenta().getActualIP()) != 0) {
							honor = Fórmulas.calcularHonorGanado(ganadores, perdedores, ganador);
						}
						if (_luchInit2.getPersonaje().getAlineacion() != 0
								&& _luchInit1.getPersonaje().getAlineacion() != 0 && pjGanador.getDeshonor() > 0) {
							deshonor = pjGanador.getDeshonor() - 1;
						}
					}
				} else if (_tipo == 2) {
					honor = Fórmulas.calcularHonorGanado(ganadores, perdedores, ganador);
				}
				if (honor < 0) {
					honor = 0;
				}
				if ((maxHonor = Mundo.getExpNivel((int) (pjGanador.getNivelAlineacion() + 1))._pvp) == -1) {
					maxHonor = Mundo.getExpNivel((int) pjGanador.getNivelAlineacion())._pvp;
				}
				packet.append(String.valueOf(pjGanador.getAlineacion() != -1 ? maxHonor : 0) + ";");
				packet.append(String.valueOf(honor) + ";");
				packet.append(String.valueOf(pjGanador.getNivelAlineacion()) + ";");
				packet.append(String.valueOf(pjGanador.getDeshonor()) + ";");
				packet.append(String.valueOf(deshonor) + ";");
				pjGanador.addHonor(honor);
				pjGanador.setDeshonor(pjGanador.getDeshonor() + deshonor);
				if (_tipo == 6) {
					long l2 = pjGanador.kamasKoli();
					long expKoli = pjGanador.expKoli();
					if (drops.length() > 0) {
						packet.append(String.valueOf(drops) + ",");
					}
					packet.append("11158~1;");
					packet.append(String.valueOf(l2) + ";");
					packet.append(String.valueOf(ganador.xpStringLuch(";")) + ";");
					packet.append(String.valueOf(expKoli) + "|");
					continue;
				}
				if (_tipo == 1) {
					if (drops.length() > 0) {
						packet.append(drops);
					}
					if (_misionPVP > 0 && _misionPVP != 3) {
						if (drops.length() > 0) {
							packet.append(",");
						}
						packet.append("10275~" + LesGuardians.CHAPAS_MISION);
					}
					packet.append(";" + l + ";");
					packet.append(String.valueOf(pjGanador.xpString(";")) + ";");
					packet.append(String.valueOf(exp) + "|");
					continue;
				}
				if (_tipo != 2)
					continue;
				packet.append(";");
				if (drops.length() > 0) {
					packet.append(drops);
				}
				packet.append(";" + l + ";");
				packet.append("0;0;0;0|");
				continue;
			}
			if (ganador.getPrisma() == null)
				continue;
			honor = Fórmulas.calcularHonorGanado(ganadores, perdedores, ganador);
			Prisma prisma = ganador.getPrisma();
			if (prisma.getHonor() + (honor *= 3) < 0) {
				honor = -prisma.getHonor();
			}
			prisma.addHonor(honor);
			packet.append("2;" + ganador.getID() + ";" + ganador.getNombreLuchador() + ";" + ganador.getNivel() + ";"
					+ (ganador._estaMuerto ? "1" : "0") + ";" + Mundo.getExpNivel((int) prisma.getNivel())._pvp + ";"
					+ prisma.getHonor() + ";");
			n = Mundo.getExpNivel((int) (prisma.getNivel() + 1))._pvp;
			if (n == -1) {
				n = Mundo.getExpNivel((int) prisma.getNivel())._pvp;
			}
			packet.append(String.valueOf(n) + ";");
			packet.append(String.valueOf(honor) + ";");
			packet.append(String.valueOf(prisma.getNivel()) + ";");
			packet.append("0;0;;0;0;0;0;0|");
		}
		for (Luchador perdedor : perdedores) {
			if (perdedor._doble != null)
				continue;
			Personaje pjPerdedor = perdedor.getPersonaje();
			if (perdedor.esInvocacion() && pjPerdedor == null)
				continue;
			if (tipoX == 0) {
				packet.append("0;" + perdedor.getID() + ";" + perdedor.getNombreLuchador() + ";" + perdedor.getNivel());
				if (perdedor.getPDVConBuff() == 0 || perdedor._estaRetirado) {
					packet.append(";1");
				} else {
					packet.append(";0");
				}
				packet.append(";" + perdedor.xpStringLuch(";") + ";;;;|");
				continue;
			}
			int honor = 0;
			int deshonor = 0;
			if (pjPerdedor != null) {
				int maxHonor;
				packet.append("0;" + perdedor.getID() + ";" + perdedor.getNombreLuchador() + ";" + perdedor.getNivel()
						+ ";" + (perdedor._estaMuerto ? "1" : "0") + ";"
						+ (pjPerdedor.getAlineacion() != -1
								? Mundo.getExpNivel((int) pjPerdedor.getNivelAlineacion())._pvp
								: 0)
						+ ";" + pjPerdedor.getHonor() + ";");
				if (_tipo == 1) {
					if (_luchInit2.getPersonaje().getAlineacion() != 0
							&& _luchInit1.getPersonaje().getAlineacion() != 0) {
						if (_luchInit2.getPersonaje().getCuenta().getActualIP()
								.compareTo(_luchInit1.getPersonaje().getCuenta().getActualIP()) != 0) {
							honor = Fórmulas.calcularHonorGanado(ganadores, perdedores, perdedor);
						}
						if (_luchInit2.getPersonaje().getAlineacion() != 0
								&& _luchInit1.getPersonaje().getAlineacion() != 0 && pjPerdedor.getDeshonor() > 0) {
							deshonor = pjPerdedor.getDeshonor() - 1;
						}
					}
				} else if (_tipo == 2) { // Prisma
					honor = Fórmulas.calcularHonorGanado(ganadores, perdedores, perdedor); // FIXME object = honor
				}
				if (honor < 0)
					honor = 0;
				if ((maxHonor = Mundo.getExpNivel((int) (pjPerdedor.getNivelAlineacion() + 1))._pvp) == -1) {
					maxHonor = Mundo.getExpNivel((int) pjPerdedor.getNivelAlineacion())._pvp;
				}
				packet.append(String.valueOf(pjPerdedor.getAlineacion() != -1 ? maxHonor : 0) + ";");
				packet.append(String.valueOf((int) honor) + ";");
				packet.append(String.valueOf(pjPerdedor.getNivelAlineacion()) + ";");
				packet.append(String.valueOf(pjPerdedor.getDeshonor()) + ";");
				packet.append(String.valueOf(deshonor) + ";");
				if (_tipo == 6) {
					long kamasKoli = pjPerdedor.kamasKoli();
					long expKoli = pjPerdedor.expKoli();
					packet.append("11158~1;");
					packet.append(String.valueOf(kamasKoli) + ";");
					packet.append(String.valueOf(perdedor.xpStringLuch(";")) + ";");
					packet.append(String.valueOf(expKoli) + "|");
					continue;
				}
				if (_tipo == 1) {
					packet.append(";0;");
					packet.append(String.valueOf(pjPerdedor.xpString(";")) + ";");
					packet.append("0|");
					continue;
				}
				if (_tipo != 2)
					continue;
				packet.append(";;0;0;0;0;0|");
				continue;
			}
			if (perdedor.getPrisma() == null)
				continue;
			honor = Fórmulas.calcularHonorGanado(ganadores, perdedores, perdedor);
			Prisma prisma = perdedor.getPrisma();
			if (prisma.getHonor() + honor < 0) {
				honor = -prisma.getHonor();
			}
			prisma.addHonor((int) honor);
			packet.append("0;" + perdedor.getID() + ";" + perdedor.getNombreLuchador() + ";" + perdedor.getNivel() + ";"
					+ (perdedor._estaMuerto ? "1" : "0") + ";" + Mundo.getExpNivel((int) prisma.getNivel())._pvp + ";"
					+ prisma.getHonor() + ";");
			int maxHonor = Mundo.getExpNivel((int) (prisma.getNivel() + 1))._pvp;
			if (maxHonor == -1) {
				maxHonor = Mundo.getExpNivel((int) prisma.getNivel())._pvp;
			}
			packet.append(String.valueOf(maxHonor) + ";");
			packet.append(String.valueOf((int) honor) + ";");
			packet.append(String.valueOf(prisma.getNivel()) + ";");
			packet.append("0;0;;0;0;0;0;0|");
		}
		if (Mundo.getRecauPorMapaID(_mapaCopia.getID()) != null && _tipo == 4) {
			Recaudador recau = Mundo.getRecauPorMapaID(_mapaCopia.getID());
			long xpGanada = Fórmulas.getXpGanadaRecau(recau, totalXP) / 1000L;
			long l = Fórmulas.getKamasGanadaRecau(minkamas, maxkamas) / 100L;
			recau.addXp(xpGanada);
			recau.setKamas(recau.getKamas() + l);
			packet.append("5;" + recau.getID() + ";" + recau.getN1() + "," + recau.getN2() + ";"
					+ Mundo.getGremio(recau.getGremioID()).getNivel() + ";0;");
			Gremio gremio = Mundo.getGremio(recau.getGremioID());
			packet.append(String.valueOf(gremio.getNivel()) + ";");
			packet.append(String.valueOf(gremio.getXP()) + ";");
			packet.append(String.valueOf(Mundo.getXPMaxGremio(gremio.getNivel())) + ";");
			packet.append(";" + xpGanada + ";;");
			String drops = "";
			if (gremio.getStats().get(158) >= recau.getPodsActuales()) {
				ArrayList<Mundo.Drop> tempDrops = new ArrayList<Mundo.Drop>();
				tempDrops.addAll(posibleDrops);
				TreeMap<Integer, Integer> objGanados = new TreeMap<Integer, Integer>();
				int veces = 0;
				int maximo = gremio.getStats(176) / 25;
				for (Drop drop : tempDrops) {
					ObjetoModelo OM = Mundo.getObjModelo(drop.getObjetoID());
					if (OM == null) {
						continue;
					}
					veces++;
					int id = OM.getID();
					objGanados.put(id, objGanados.get(id = OM.getID()) == null ? 1 : (Integer) objGanados.get(id) + 1);
					drop.setDropMax(drop.getDropMax() - 1);
					if (drop.getDropMax() == 0) {
						posibleDrops.remove(drop);
					}
					if (veces >= maximo)
						break;
				}
				for (Map.Entry<Integer, Integer> entry : objGanados.entrySet()) {
			          Objeto.ObjetoModelo OM = Mundo.getObjModelo(((Integer)entry.getKey()).intValue());
			          if (OM == null)
			            continue; 
					if (drops.length() > 0) {
						drops = String.valueOf(drops) + ",";
					}
					drops = String.valueOf(drops) + entry.getKey() + "~" + entry.getValue();
					Objeto obj = OM.crearObjDesdeModelo((Integer) entry.getValue(), false);
					Mundo.addObjeto(obj, true);
					recau.addObjeto(obj);
				}
			}
			packet.append(String.valueOf(drops) + ";");
			packet.append(String.valueOf(l) + "|");
		}
		return packet.toString();
	}

	public boolean acaboPelea(boolean usarPara5y2, boolean e1Muerto) {
		if (_estadoPelea == 4) {
			return false;
		}
		boolean equipo1Muerto = true;
		boolean equipo2Muerto = true;
		if (!usarPara5y2) {
			for (Map.Entry<Integer, Luchador> entry : _equipo1.entrySet()) {
				if (entry.getValue().esInvocacion() || entry.getValue()._estaMuerto)
					continue;
				equipo1Muerto = false;
				break;
			}
			for (Map.Entry<Integer, Luchador> entry : _equipo2.entrySet()) {
				if (entry.getValue().esInvocacion() || entry.getValue()._estaMuerto)
					continue;
				equipo2Muerto = false;
				break;
			}
		} else {
			equipo1Muerto = e1Muerto;
			equipo2Muerto = !e1Muerto;
		}
		if (_tipo == 4) {
			if (equipo2Muerto) {
				_mobGrupo.setEstrellas((short) 0);
				_mobGrupo.setMuerto(true);
			} else if (_mobGrupo.esPermanente()) {
				_mapaReal.setUltimoGrupoMob(_mobGrupo);
			}
		}
		if (equipo1Muerto || equipo2Muerto || !verificaSiQuedaUno()) {
			Personaje pjPerdedor;
			_estadoPelea = (byte) 4;
			int equipoGanador = equipo1Muerto ? 2 : 1;
			for (Luchador luchador : luchadoresDeEquipo(7)) {
				Personaje perso = luchador.getPersonaje();
				if (perso == null)
					continue;
				perso.setPelea(null);
				perso.setOcupado(false);
				perso.setDueloID(-1);
				perso.setListo(false);
				if (!luchador._desconectado)
					continue;
				if (!LesGuardians.MODO_HEROICO) {
					luchador._estaRetirado = true;
				}
				luchador._estaMuerto = true;
				perso.setReconectado(false);
				perso.resetVariables();
				SQLManager.SALVAR_PERSONAJE(perso, true);
				Mundo.desconectarPerso(perso);
			}
			_mapaReal.quitarPelea(_id);
			String packet = getPanelResultados(equipoGanador);
			try {
				Thread.sleep(1100 * (_cantUltAfec + 1));
			} catch (Exception exception) {
				// empty catch block
			}
			SocketManager.ENVIAR_GE_PANEL_RESULTADOS_PELEA(this, 7, packet);
			for (Personaje p : _espectadores.values()) {
				p.retornoMapa();
			}
			SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(_mapaReal);
			ArrayList<Luchador> ganadores = new ArrayList<Luchador>();
			ArrayList<Luchador> perdedores = new ArrayList<Luchador>();
			if (equipo1Muerto) {
				perdedores.addAll(_equipo1.values());
				ganadores.addAll(_equipo2.values());
			} else {
				ganadores.addAll(_equipo1.values());
				perdedores.addAll(_equipo2.values());
			}
			try {
				Thread.sleep(1500L);
			} catch (Exception exception) {
				// empty catch block
			}
			String str = "";
			if (_Recaudador != null) {
				str = "S" + _Recaudador.getN1() + "," + _Recaudador.getN2() + "|.|" + _Recaudador.getMapaID() + "|"
						+ _Recaudador.getCeldalID();
			}
			if (_Prisma != null) {
				str = String.valueOf(_Prisma.getMapaID()) + "|" + _Prisma.getX() + "|" + _Prisma.getY();
			}
			for (Luchador ganador : ganadores) {
				PrintWriter out;
				if (ganador._recaudador != null) {
					for (Personaje pj : Mundo.getGremio(_gremioID).getPjMiembros()) {
						if (pj == null || !pj.enLinea())
							continue;
						SocketManager.ENVIAR_gITM_INFO_RECAUDADOR(pj,
								Recaudador.analizarRecaudadores(pj.getGremio().getID()));
						SocketManager.ENVIAR_gA_MENSAJE_SOBRE_RECAUDADOR(pj, str);
					}
					ganador._recaudador.setEstadoPelea((byte) 0);
					ganador._recaudador.setPeleaID((short) -1);
					ganador._recaudador.setPelea(null);
					for (Personaje z : Mundo.getMapa(ganador._recaudador.getMapaID()).getPersos()) {
						if (z.getCuenta().getEntradaPersonaje() == null)
							continue;
						out = z.getCuenta().getEntradaPersonaje().getOut();
						SocketManager.ENVIAR_GM_RECAUDADORES(out, z.getMapa());
					}
				} else if (ganador._prisma != null) {
					for (Personaje z : Mundo.getPJsEnLinea()) {
						if (z == null || z.getAlineacion() != _Prisma.getAlineacion())
							continue;
						SocketManager.ENVIAR_CS_MENSAJE_SOBREVIVIO_PRISMA(z, str);
					}
					ganador._prisma.setEstadoPelea((byte) -1);
					ganador._prisma.setPeleaID((short) -1);
					ganador._prisma.setPelea(null);
					for (Personaje z : Mundo.getMapa(ganador._prisma.getMapaID()).getPersos()) {
						if (z == null || z.getCuenta().getEntradaPersonaje() == null
								|| (out = z.getCuenta().getEntradaPersonaje().getOut()) == null)
							continue;
						SocketManager.ENVIAR_GM_PRISMAS(out, z.getMapa());
					}
				}
				Personaje pjGanador = ganador.getPersonaje();
				if (ganador._estaRetirado || pjGanador == null || ganador.esInvocacion() || !pjGanador.enLinea())
					continue;
				if (_tipo == 5 && pjGanador != null) {
					if (_Recaudador != null) {
						SocketManager.ENVIAR_gITP_INFO_DEFENSORES_RECAUDADOR(pjGanador, getListaDefensores());
					}
				} else if (_tipo == 2 && pjGanador != null && _Prisma != null) {
					SocketManager.ENVIAR_CP_INFO_DEFENSORES_PRISMA(pjGanador, getListaDefensores());
				}
				if (_tipo != 0) {
					if (ganador.getPDV() < 1) {
						pjGanador.setPDV(1);
					} else {
						pjGanador.setPDV(ganador.getPDV());
					}
				}
				if ((_tipo == 5 || _tipo == 2) && pjGanador.getMapaDefPerco() != null) {
					try {
						Thread.sleep(400L);
					} catch (Exception exception) {
						// empty catch block
					}
					pjGanador.retornoMapaDesPeleaRecau();
					continue;
				}
				if (_tipo == 6) {
					pjGanador.setEnKoliseo(false);
					pjGanador.retornoPtoSalvada(true);
					continue;
				}
				pjGanador.retornoMapa();
			}
			if (_Recaudador != null) {
				str = "D" + _Recaudador.getN1() + "," + _Recaudador.getN2() + "|.|" + _Recaudador.getMapaID() + "|"
						+ _Recaudador.getCeldalID();
			}
			for (Luchador perdedor : perdedores) {
				if (perdedor._recaudador != null) {
					for (Personaje z : Mundo.getGremio(_gremioID).getPjMiembros()) {
						if (z == null || !z.enLinea())
							continue;
						SocketManager.ENVIAR_gITM_INFO_RECAUDADOR(z,
								Recaudador.analizarRecaudadores(z.getGremio().getID()));
						SocketManager.ENVIAR_gA_MENSAJE_SOBRE_RECAUDADOR(z, str);
					}
					_mapaReal.removeNPC(perdedor._recaudador.getID());
					SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_mapaReal, perdedor._recaudador.getID());
					_Recaudador.borrarRecaudador(perdedor._recaudador.getID());
				}
				if (perdedor._prisma != null) {
					Mundo.SubArea subarea = _mapaReal.getSubArea();
					for (Personaje z : Mundo.getPJsEnLinea()) {
						if (z == null)
							continue;
						if (z.getAlineacion() == 0) {
							SocketManager.ENVIAR_am_MENSAJE_ALINEACION_SUBAREA(z,
									String.valueOf(subarea.getID()) + "|0|1");
							continue;
						}
						if (z.getAlineacion() == _Prisma.getAlineacion()) {
							SocketManager.ENVIAR_CD_MENSAJE_MURIO_PRISMA(z, str);
						}
						SocketManager.ENVIAR_am_MENSAJE_ALINEACION_SUBAREA(z,
								String.valueOf(subarea.getID()) + "|-1|0");
						SocketManager.ENVIAR_am_MENSAJE_ALINEACION_SUBAREA(z, String.valueOf(subarea.getID()) + "|0|1");
						if (_Prisma.getAreaConquistada() == -1)
							continue;
						SocketManager.ENVIAR_aM_MENSAJE_ALINEACION_AREA(z,
								String.valueOf(subarea.getArea().getID()) + "|-1");
						subarea.getArea().setPrismaID(0);
						subarea.getArea().setAlineacion(0);
					}
					int prismaID = perdedor._prisma.getID();
					subarea.setPrismaID(0);
					subarea.setAlineacion(0);
					_mapaReal.removeNPC(prismaID);
					SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_mapaReal, prismaID);
					Mundo.borrarPrisma(prismaID);
					SQLManager.DELETE_PRISMA(prismaID);
				}
				pjPerdedor = perdedor.getPersonaje();
				if (perdedor._estaRetirado || pjPerdedor == null || perdedor.esInvocacion() || !pjPerdedor.enLinea())
					continue;
				if (_tipo == 5 && pjPerdedor != null) {
					if (_Recaudador != null) {
						SocketManager.ENVIAR_gITP_INFO_DEFENSORES_RECAUDADOR(pjPerdedor, getListaDefensores());
					}
				} else if (_tipo == 2 && pjPerdedor != null && _Prisma != null) {
					SocketManager.ENVIAR_CP_INFO_DEFENSORES_PRISMA(pjPerdedor, getListaDefensores());
				}
				if (_tipo != 0) {
					try {
						Thread.sleep(700L);
					} catch (Exception prismaID) {
						// empty catch block
					}
					if (_tipo != 6) {
						int energiaAPerder = 5 * pjPerdedor.getNivel();
						if (_tipo == 5) {
							energiaAPerder += 500;
						}
						pjPerdedor.restarEnergia(energiaAPerder);
						SocketManager.ENVIAR_Im_INFORMACION(pjPerdedor, "034;" + energiaAPerder);
					} else {
						pjPerdedor.setEnKoliseo(false);
					}
					if ((_tipo == 5 || _tipo == 2) && pjPerdedor.getMapaDefPerco() != null) {
						try {
							Thread.sleep(400L);
						} catch (Exception exception) {
							// empty catch block
						}
						pjPerdedor.retornoPtoSalvadaRecau();
					} else {
						pjPerdedor.retornoPtoSalvada(
								!LesGuardians.MODO_HEROICO || LesGuardians.MODO_HEROICO && _tipo != 1);
					}
					pjPerdedor.setPDV(1);
					pjPerdedor.restarVidaMascota(null);
					continue;
				}
				pjPerdedor.retornoMapa();
			}
			if (_tipo == 4 && !_mobGrupo.esPermanente()) {
				byte alineacion = (byte) _mapaReal.getSubArea().getAlineacion();
				if (_mapaReal.esMazmorra()) {
					alineacion = 0;
				}
				_mapaReal.spawnGrupo(alineacion, 1, true, _mobGrupo.getCeldaID(), _mobGrupo);
			}
			if (LesGuardians.MODO_HEROICO && _tipo != 0 && !LesGuardians._cerrando) {
				for (Luchador perdedor : perdedores) {
					pjPerdedor = perdedor.getPersonaje();
					if (pjPerdedor == null)
						continue;
					pjPerdedor.convertirTumba();
				}
			}
			if (_evento || equipoGanador == 1 && _tipo != 0) {
				_mapaReal.aplicarAccionFinCombate(_tipo, ganadores, _evento);
			}
			return true;
		}
		return false;
	}

	public int getParamEquipo(int id) {
		if (_equipo1.containsKey(id)) {
			return 1;
		}
		if (_equipo2.containsKey(id)) {
			return 2;
		}
		if (_espectadores.containsKey(id)) {
			return 4;
		}
		return -1;
	}

	public int getIDEquipoEnemigo(int id) {
		if (_equipo1.containsKey(id)) {
			return 2;
		}
		if (_equipo2.containsKey(id)) {
			return 1;
		}
		return -1;
	}

	public Luchador getLuchadorPorPJ(Personaje perso) {
		Luchador luchador = null;
		if (_equipo1.get(perso.getID()) != null) {
			luchador = _equipo1.get(perso.getID());
		} else if (_equipo2.get(perso.getID()) != null) {
			luchador = _equipo2.get(perso.getID());
		}
		return luchador;
	}

	public Luchador getLuchadorTurno() {
		return _ordenJugadores.get(_nroOrdenLuc);
	}

	public void actualizarInfoJugador() {
		Personaje.Stats statConBuff = _ordenJugadores.get(_nroOrdenLuc).getTotalStatsConBuff();
		_tempLuchadorPA = (short) (statConBuff.getEfecto(111) - _tempLuchadorPAusados);
		if (_tempLuchadorPA < 0) {
			_tempLuchadorPA = 0;
		}
		_tempLuchadorPM = (short) (statConBuff.getEfecto(128) - _tempLuchadorPMusados);
		if (_tempLuchadorPM < 0) {
			_tempLuchadorPM = 0;
		}
	}

	/*
	 * Enabled force condition propagation Lifted jumps to return sites
	 */
	public void retirarsePelea(Personaje retirador, Personaje expulsado) {
		if (retirador == null || _ordenJugadores == null || _nroOrdenLuc < 0) {
			return;
		}
		if (_nroOrdenLuc >= _ordenJugadores.size()) {
			_nroOrdenLuc = 0;
		}
		_cantUltAfec = 1;
		Luchador lucRetirador = getLuchadorPorPJ(retirador);
		Luchador lucExpulsado = null;
		if (expulsado != null) {
			lucExpulsado = getLuchadorPorPJ(expulsado);
		}
		if (lucRetirador != null) {
			switch (_tipo) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6: {
				if (_estadoPelea == 3) {
					if (lucRetirador.puedeJugar() && cuantosQuedanDelEquipo(lucRetirador.getID()) > 1) {
						_tempAccion = "";
						finTurno(lucRetirador);
					}
					agregarAMuertos(lucRetirador);
					if (_estadoPelea == 4) {
						return;
					}
					SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_mapaCopia, lucRetirador.getID());
					if (_tipo != 0) {
						retirador.restarEnergia(1500);
						if (retirador.enLinea()) {
							SocketManager.ENVIAR_Im_INFORMACION(retirador, "034;1500");
						}
						if (_tipo == 5 || _tipo == 2) {
							retirador.addHonor(-500);
							if (retirador.enLinea()) {
								SocketManager.ENVIAR_Im_INFORMACION(retirador, "076;-500");
							}
							retirador.retornoPtoSalvadaRecau();
						} else {
							retirador.retornoPtoSalvada(
									!LesGuardians.MODO_HEROICO || LesGuardians.MODO_HEROICO && _tipo != 1);
						}
						if (_tipo == 6) {
							retirador.setEnKoliseo(false);
						}
						retirador.setPDV(1);
						retirador.restarVidaMascota(null);
					} else if (retirador.enLinea()) {
						retirador.retornoMapa();
					}
					if (LesGuardians.MODO_HEROICO && _tipo != 0 && !LesGuardians._cerrando) {
						ArrayList<Objeto> objPerder = retirador.getObjetosEquipados();
						_objetosRobados.addAll(objPerder);
						_kamasRobadas += retirador.getKamas() / 2L;
						retirador.setKamas(retirador.getKamas() / 2L);
						for (Objeto obj : objPerder) {
							if (retirador.enLinea()) {
								SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(retirador, obj.getID());
							}
							retirador.borrarObjetoSinEliminar(obj.getID());
						}
						retirador.convertirTumba();
					}
					lucRetirador._estaRetirado = true;
					return;
				}
				if (_estadoPelea == 2) {
					Luchador salir;
					boolean puedeExpulsar = false;
					if ((_tipo == 4 || _tipo == 3) && _luchInit1 != null && _luchInit1.getPersonaje() != null
							&& retirador.getID() != _luchInit1.getPersonaje().getID()) {
						return;
					}
					if (_luchInit1 != null && _luchInit1.getPersonaje() != null
							&& retirador.getID() == _luchInit1.getPersonaje().getID()) {
						puedeExpulsar = true;
					} else if (_luchInit2 != null && _luchInit2.getPersonaje() != null
							&& retirador.getID() == _luchInit2.getPersonaje().getID()) {
						puedeExpulsar = true;
					}
					SocketManager.ENVIAR_GM_BORRAR_LUCHADOR(this, retirador.getID(), 3);
					if (lucExpulsado != null && puedeExpulsar) {
						int idLucExpuls = lucExpulsado.getID();
						if (lucExpulsado.getEquipoBin() != lucRetirador.getEquipoBin())
							return;
						if (idLucExpuls == lucRetirador.getID())
							return;
						if (expulsado.enLinea()) {
							expulsado.retornoMapa();
						}
					} else if (puedeExpulsar && _tipo != 6) {
						_tiempoInicioTurno = 0L;
						if (_tipo == 0) {
							for (Luchador luch : luchadoresDeEquipo(3)) {
								Personaje perso = luch.getPersonaje();
								if (!perso.enLinea())
									continue;
								SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(perso);
								perso.retornoMapa();
							}
							_estadoPelea = (byte) 4;
							_mapaReal.quitarPelea(_id);
							SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(_mapaReal);
							SocketManager.ENVIAR_Gc_BORRAR_ESPADA_EN_MAPA(_mapaReal, _idLuchInit1);
						} else {
							retirador.restarEnergia(1500);
							if (retirador.enLinea()) {
								SocketManager.ENVIAR_Im_INFORMACION(retirador, "034;1500");
							}
							if (_tipo == 1 || _tipo == 2) {
								retirador.addHonor(-500);
								if (retirador.enLinea()) {
									SocketManager.ENVIAR_Im_INFORMACION(retirador, "076;-500");
								}
							}
							if (_tipo == 5 || _tipo == 2) {
								retirador.retornoPtoSalvadaRecau();
							} else {
								retirador.retornoPtoSalvada(
										!LesGuardians.MODO_HEROICO || LesGuardians.MODO_HEROICO && _tipo != 1);
							}
							retirador.restarVidaMascota(null);
							retirador.setPDV(1);
						}
					} else if (_tipo != 0) {
						if (_tipo != 6) {
							retirador.restarEnergia(1500);
							if (retirador.enLinea()) {
								SocketManager.ENVIAR_Im_INFORMACION(retirador, "034;1500");
							}
						} else {
							retirador.setEnKoliseo(false);
						}
						if (_tipo == 1 || _tipo == 2) {
							retirador.addHonor(-500);
							if (retirador.enLinea()) {
								SocketManager.ENVIAR_Im_INFORMACION(retirador, "076;-500");
							}
						}
						if (_tipo == 5 || _tipo == 2) {
							retirador.retornoPtoSalvadaRecau();
						} else {
							retirador.retornoPtoSalvada(
									!LesGuardians.MODO_HEROICO || LesGuardians.MODO_HEROICO && _tipo != 1);
						}
						retirador.setPDV(1);
						retirador.restarVidaMascota(null);
						if (LesGuardians.MODO_HEROICO && !LesGuardians._cerrando) {
							ArrayList<Objeto> objPerder = retirador.getObjetosEquipados();
							_objetosRobados.addAll(objPerder);
							_kamasRobadas += retirador.getKamas() / 2L;
							retirador.setKamas(retirador.getKamas() / 2L);
							for (Objeto obj : objPerder) {
								if (retirador.enLinea()) {
									SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(retirador, obj.getID());
								}
								retirador.borrarObjetoSinEliminar(obj.getID());
							}
							retirador.convertirTumba();
						}
					} else if (retirador.enLinea()) {
						SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(retirador);
						retirador.retornoMapa();
					}
					if (_equipo1.containsKey((salir = lucExpulsado != null ? lucExpulsado : lucRetirador).getID())) {
						lucRetirador._celda.removerLuchador(salir);
						_equipo1.remove(salir.getID());
					} else if (_equipo2.containsKey(salir.getID())) {
						lucRetirador._celda.removerLuchador(salir);
						_equipo2.remove(salir.getID());
					}
					SocketManager.ENVIAR_Gt_BORRAR_NOMBRE_ESPADA(_mapaReal,
							_equipo1.containsKey(salir.getID()) ? _idLuchInit1 : _idLuchInit2, salir);
					return;
				}
				System.out.println("ERROR RETIRARSE, estado de combate: " + _estadoPelea + " tipo de combate:" + _tipo
						+ " LuchadorExp:" + lucExpulsado + " LuchadorRet:" + lucRetirador);
				return;
			}
			default: {
				System.out.println("Tipo de combate no generado, tipo de combate:" + _tipo + " LuchadorExp:"
						+ lucExpulsado + " LuchadorRet:" + lucRetirador);
				return;
			}
			}
		} else {
			_espectadores.remove(retirador.getID());
			SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(retirador);
			retirador.retornoMapa();
		}
	}

	public String stringOrdenJugadores() {
		String packet = "GTL";
		for (Luchador luchador : _ordenJugadores) {
			packet = String.valueOf(packet) + "|" + luchador.getID();
		}
		return packet;
	}

	public int getSigIDLuchador() {
		int id = -1;
		for (Luchador luchador : luchadoresDeEquipo(3)) {
			if (luchador.getID() >= id)
				continue;
			id = luchador.getID();
		}
		_numeroInvos = (byte) (_numeroInvos + 1);
		return --id;
	}

	public void addLuchadorEnEquipo(Luchador luchador, int equipo) {
		if (equipo == 0) {
			_equipo1.put(luchador.getID(), luchador);
		} else if (equipo == 1) {
			_equipo2.put(luchador.getID(), luchador);
		}
	}

	public String infoPeleasEnMapa() {
		if (_estadoPelea >= 4) {
			_mapaReal.quitarPelea(_id);
			return "";
		}
		String infos = String.valueOf(_id) + ";";
		Date actDate = new Date();
		long tiempo = actDate.getTime() + 3600000L - (System.currentTimeMillis() - _tiempoInicio);
		infos = String.valueOf(infos) + (_tiempoInicio == 0L ? "-1" : Long.valueOf(tiempo)) + ";";
		int jugEquipo0 = 0;
		int jugEquipo1 = 0;
		for (Luchador l : _equipo1.values()) {
			if (l == null || l.esInvocacion())
				continue;
			++jugEquipo0;
		}
		for (Luchador l : _equipo2.values()) {
			if (l == null || l.esInvocacion())
				continue;
			++jugEquipo1;
		}
		infos = String.valueOf(infos) + "0,";
		switch (_tipo) {
		case 0: {
			infos = String.valueOf(infos) + "0,";
			infos = String.valueOf(infos) + jugEquipo0 + ";";
			infos = String.valueOf(infos) + "0,";
			infos = String.valueOf(infos) + "0,";
			infos = String.valueOf(infos) + jugEquipo1 + ";";
			break;
		}
		case 1: {
			infos = String.valueOf(infos) + _luchInit1.getPersonaje().getAlineacion() + ",";
			infos = String.valueOf(infos) + jugEquipo0 + ";";
			infos = String.valueOf(infos) + "0,";
			infos = String.valueOf(infos) + _luchInit2.getPersonaje().getAlineacion() + ",";
			infos = String.valueOf(infos) + jugEquipo1 + ";";
			break;
		}
		case 2: {
			infos = String.valueOf(infos) + _luchInit1.getPersonaje().getAlineacion() + ",";
			infos = String.valueOf(infos) + jugEquipo0 + ";";
			infos = String.valueOf(infos) + "0,";
			infos = String.valueOf(infos) + _Prisma.getAlineacion() + ",";
			infos = String.valueOf(infos) + jugEquipo1 + ";";
			break;
		}
		case 3: {
			infos = String.valueOf(infos) + "0,";
			infos = String.valueOf(infos) + jugEquipo0 + ";";
			infos = String.valueOf(infos) + "1,";
			infos = String.valueOf(infos)
					+ _equipo2.get(_equipo2.keySet().toArray()[0]).getMob().getModelo().getAlineacion() + ",";
			infos = String.valueOf(infos) + jugEquipo1 + ";";
			break;
		}
		case 4: {
			infos = String.valueOf(infos) + "0,";
			infos = String.valueOf(infos) + jugEquipo0 + ";";
			infos = String.valueOf(infos) + "1,";
			infos = String.valueOf(infos)
					+ _equipo2.get(_equipo2.keySet().toArray()[0]).getMob().getModelo().getAlineacion() + ",";
			infos = String.valueOf(infos) + jugEquipo1 + ";";
			break;
		}
		case 5: {
			infos = String.valueOf(infos) + "0,";
			infos = String.valueOf(infos) + jugEquipo0 + ";";
			infos = String.valueOf(infos) + "3,";
			infos = String.valueOf(infos) + "0,";
			infos = String.valueOf(infos) + jugEquipo1 + ";";
		}
		}
		return infos;
	}

	public boolean verificaSiQuedaUno() {
		for (Luchador luchador : _equipo1.values()) {
			if (luchador._estaMuerto || luchador.esInvocacion())
				continue;
			return true;
		}
		for (Luchador luchador : _equipo2.values()) {
			if (luchador._estaMuerto || luchador.esInvocacion())
				continue;
			return true;
		}
		return false;
	}

	public int cuantosQuedanDelEquipo(int id) {
		int num;
		block3: {
			block2: {
				num = 0;
				if (!_equipo1.containsKey(id))
					break block2;
				for (Luchador luchador : _equipo1.values()) {
					if (luchador._estaMuerto || luchador.esInvocacion())
						continue;
					++num;
				}
				break block3;
			}
			if (!_equipo2.containsKey(id))
				break block3;
			for (Luchador luchador : _equipo2.values()) {
				if (luchador._estaMuerto || luchador.esInvocacion())
					continue;
				++num;
			}
		}
		return num;
	}

	/*
	 * Enabled aggressive block sorting
	 */
	public static void agregarEspadaDePelea(Mapa mapa, Personaje perso) {
		block7: for (Map.Entry<Short, Combate> peleas : mapa.getPeleas().entrySet()) {
			Luchador luchador;
			Combate pelea = peleas.getValue();
			if (pelea._estadoPelea != 2)
				continue;
			Personaje persoInit1 = pelea._luchInit1.getPersonaje();
			int id1 = pelea._idLuchInit1;
			int id2 = pelea._idLuchInit2;
			String enviar1 = "";
			String enviar2 = "";
			boolean primero1 = true;
			boolean primero2 = true;
			Personaje persoInit2 = null;
			switch (pelea._tipo) {
			case 0: {
				persoInit2 = pelea._luchInit2.getPersonaje();
				if (persoInit2 == null)
					continue block7;
				SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_A_JUGADOR(perso, 0, id1, id2, persoInit1.getCelda().getID(),
						"0;-1", persoInit2.getCelda().getID(), "0;-1");
				break;
			}
			case 1: {
				persoInit2 = pelea._luchInit2.getPersonaje();
				if (persoInit2 == null)
					continue block7;
				SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_A_JUGADOR(perso, 0, id1, id2, persoInit1.getCelda().getID(),
						"0;" + persoInit1.getAlineacion(), persoInit2.getCelda().getID(),
						"0;" + persoInit2.getAlineacion());
				break;
			}
			case 2: {
				SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_A_JUGADOR(perso, 0, id1, pelea._Prisma.getID(),
						persoInit1.getCelda().getID(), "0;" + persoInit1.getAlineacion(), pelea._Prisma.getCeldaID(),
						"0;" + pelea._Prisma.getAlineacion());
				break;
			}
			case 4: {
				SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_A_JUGADOR(perso, 4, id1, pelea._mobGrupo.getID(),
						persoInit1.getCelda().getID(), "0;-1", pelea._mobGrupo.getCeldaID() - 1, "1;-1");
				break;
			}
			case 5: {
				SocketManager.ENVIAR_Gc_MOSTRAR_ESPADA_A_JUGADOR(perso, 5, id1, pelea._Recaudador.getID(),
						persoInit1.getCelda().getID(), "0;-1", pelea._Recaudador.getCeldalID(), "3;-1");
			}
			}
			for (Map.Entry<Integer, Luchador> entry : pelea._equipo1.entrySet()) {
				luchador = entry.getValue();
				if (!primero1) {
					enviar1 = String.valueOf(enviar1) + "|+";
				}
				enviar1 = String.valueOf(enviar1) + luchador.getID() + ";" + luchador.getNombreLuchador() + ";"
						+ luchador.getNivel();
				primero1 = false;
			}
			SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(perso, id1, enviar1);
			for (Map.Entry<Integer, Luchador> entry : pelea._equipo2.entrySet()) {
				luchador = entry.getValue();
				if (!primero2) {
					enviar2 = String.valueOf(enviar2) + "|+";
				}
				enviar2 = String.valueOf(enviar2) + luchador.getID() + ";" + luchador.getNombreLuchador() + ";"
						+ luchador.getNivel();
				primero2 = false;
			}
			SocketManager.ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(perso, id2, enviar2);
		}
	}

	public static short getPeleaIDPorLuchador(Mapa mapa, int id) {
		for (Map.Entry<Short, Combate> pelea : mapa.getPeleas().entrySet()) {
			for (Map.Entry<Integer, Luchador> luch : pelea.getValue()._equipo1.entrySet()) {
				if (luch.getValue().getPersonaje() == null || luch.getValue().getID() != id)
					continue;
				return pelea.getValue().getID();
			}
		}
		return 0;
	}

	public Map<Integer, Luchador> getListaMuertos() {
		return _listaMuertos;
	}

	public void borrarUnMuerto(Luchador objetivo) {
		_listaMuertos.remove(objetivo.getID());
	}

	public static class Glifo {
		private Luchador _lanzador;
		private Mapa.Celda _celda;
		private byte _tama\u00f1o;
		private int _hechizos;
		private Hechizo.StatsHechizos _glifoHechizo;
		private byte _duracion;
		private Combate _pelea;
		private int _color;

		public Glifo(Combate pelea, Luchador lanzador, Mapa.Celda celda, byte tama\u00f1o,
				Hechizo.StatsHechizos glifoHechizo, byte duracion, int hechizo) {
			_pelea = pelea;
			_lanzador = lanzador;
			_celda = celda;
			_hechizos = hechizo;
			_tama\u00f1o = tama\u00f1o;
			_glifoHechizo = glifoHechizo;
			_duracion = duracion;
			_color = Constantes.getColorGlifo(hechizo);
		}

		public Mapa.Celda getCelda() {
			return _celda;
		}

		public byte getTama\u00f1o() {
			return _tama\u00f1o;
		}

		public Luchador getLanzador() {
			return _lanzador;
		}

		public byte getDuracion() {
			return _duracion;
		}

		public int disminuirDuracion() {
			_duracion = (byte) (_duracion - 1);
			return _duracion;
		}

		public void activarGlifo(Luchador glifeado) {
			String str = String.valueOf(_hechizos) + "," + _celda.getID() + ",0,1,1," + _lanzador.getID();
			SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, 7, 307, String.valueOf(glifeado.getID()), str);
			_glifoHechizo.aplicaHechizoAPelea(_pelea, _lanzador, glifeado.getCeldaPelea(), false);
			_pelea.acaboPelea(false, false);
		}

		public void desaparecer() {
			SocketManager.ENVIAR_GDZ_ACTUALIZA_ZONA_EN_PELEA(_pelea, 7, "-", _celda.getID(), _tama\u00f1o, _color);
			SocketManager.ENVIAR_GDC_ACTUALIZAR_CELDA_EN_PELEA(_pelea, 7, _celda.getID());
		}

		public int getColor() {
			return _color;
		}
	}

	public static class HechizoLanzado {
		private int _hechizoId = 0;
		private int _sigLanzamiento = 0;
		private Luchador _objetivo = null;

		public HechizoLanzado(Luchador objetivo, Hechizo.StatsHechizos sHechizo, Luchador lanzador) {
			_objetivo = objetivo;
			_hechizoId = sHechizo.getHechizoID();
			if (lanzador.getTipo() == 1
					&& lanzador.getPersonaje().getHechizosSetClase().containsKey(sHechizo.getHechizoID())) {
				int modi = lanzador.getPersonaje().getModifSetClase(sHechizo.getHechizoID(), 286);
				_sigLanzamiento = sHechizo.getSigLanzamiento() - modi;
			} else {
				_sigLanzamiento = sHechizo.getSigLanzamiento();
			}
		}

		public void actuSigLanzamiento() {
			--_sigLanzamiento;
		}

		public int getSigLanzamiento() {
			return _sigLanzamiento;
		}

		public int getID() {
			return _hechizoId;
		}

		public Luchador getObjetivo() {
			return _objetivo;
		}

		public static boolean poderSigLanzamiento(Luchador lanzador, int id) {
			for (HechizoLanzado HL : lanzador.getHechizosLanzados()) {
				if (HL._hechizoId != id || HL.getSigLanzamiento() <= 0)
					continue;
				return false;
			}
			return true;
		}

		public static int getNroLanzamientos(Luchador lanzador, int id) {
			int nro = 0;
			for (HechizoLanzado HL : lanzador.getHechizosLanzados()) {
				if (HL._hechizoId != id)
					continue;
				++nro;
			}
			return nro;
		}

		public static int getNroLanzPorObjetivo(Luchador lanzador, Luchador objetivo, int id) {
			if (objetivo == null) {
				return 0;
			}
			int nro = 0;
			for (HechizoLanzado HL : lanzador.getHechizosLanzados()) {
				if (HL._objetivo == null || HL._hechizoId != id || HL._objetivo.getID() != objetivo.getID())
					continue;
				++nro;
			}
			return nro;
		}
	}

	public static class Luchador {
		private int _id = 0;
		private boolean _puedeJugar = false;
		private Combate _pelea;
		private int _tipo = 0;
		private MobModelo.MobGrado _mob = null;
		private Personaje _perso = null;
		private int _equipoBin = -2;
		private Mapa.Celda _celda;
		private ArrayList<EfectoHechizo> _buffsPelea = new ArrayList<EfectoHechizo>();
		private Luchador _invocador;
		private int _PDVMAX;
		private int _PDV;
		private boolean _estaMuerto;
		private boolean _estaRetirado;
		private int _gfxID;
		private Map<Integer, Integer> _estados = new TreeMap<Integer, Integer>();
		private Luchador _transportado;
		private Luchador _transportadoPor;
		private Recaudador _recaudador = null;
		private Prisma _prisma = null;
		private Personaje _doble = null;
		private ArrayList<HechizoLanzado> _hechizosLanzados = new ArrayList<HechizoLanzado>();
		private Luchador _objetivoDestZurca = null;
		private float _bonusAlineacion = 0.0f;
		private Personaje.Stats _totalStats;
		private Map<Integer, Integer> _bonusCastigo = new TreeMap<Integer, Integer>();
		private int _nroInvocaciones = 0;
		private int _idHechiLanzReto = -1;
		private int _idCeldaIniTurnoReto;
		private ArrayList<Integer> _hechiLanzadosReto = new ArrayList<Integer>();
		private ArrayList<Integer> _mobMatadosReto = new ArrayList<Integer>();
		private boolean _intocable = false;
		private boolean _contaminacion = false;
		private boolean _contaminado = false;
		private int _turnosParaMorir = 0;
		private int _pjAtacante = 0;
		private int _prospeccionTemporal = 0;
		private boolean _desconectado = false;
		private int _turnosRestantes = 20;

		public void setPjAtacante(int id) {
			_pjAtacante = id;
		}

		public int getPjAtacante() {
			return _pjAtacante;
		}

		public void setBonusCastigo(int bonus, int stat) {
			_bonusCastigo.put(stat, bonus);
		}

		public int getBonusCastigo(int stat) {
			int bonus = 0;
			if (_bonusCastigo.containsKey(stat)) {
				bonus = _bonusCastigo.get(stat);
			}
			return bonus;
		}

		public Luchador getObjetivoDestZurca() {
			return _objetivoDestZurca;
		}

		public void setObjetivoDestZurca(Luchador objetivo) {
			_objetivoDestZurca = objetivo;
		}

		public int getTipo() {
			return _tipo;
		}

		public Luchador(Combate pelea, MobModelo.MobGrado mob) {
			_pelea = pelea;
			_tipo = 2;
			_mob = mob;
			_id = mob.getIdEnPelea();
			_PDVMAX = mob.getPDVMAX();
			_PDV = mob.getPDV();
			_gfxID = getGfxDefecto();
			_totalStats = mob.getStats();
		}

		public Luchador(Combate pelea, Personaje perso) {
			_pelea = pelea;
			if (perso.esDoble()) {
				_tipo = 10;
				_doble = perso;
			} else {
				_tipo = 1;
				_perso = perso;
			}
			_id = perso.getID();
			_PDVMAX = perso.getPDVMAX();
			_PDV = perso.getPDV();
			_gfxID = getGfxDefecto();
			_totalStats = perso.getTotalStats();
		}

		public Luchador(Combate pelea, Recaudador recaudador) {
			_pelea = pelea;
			_tipo = 5;
			_recaudador = recaudador;
			_id = -1;
			Gremio gremio = Mundo.getGremio(recaudador.getGremioID());
			_PDVMAX = gremio.getNivel() * 100;
			_PDV = gremio.getNivel() * 100;
			_gfxID = 6000;
			_totalStats = gremio.getStatsPelea();
		}

		public Luchador(Combate pelea, Prisma prisma) {
			_pelea = pelea;
			_tipo = 7;
			_prisma = prisma;
			_id = -1;
			_PDVMAX = prisma.getNivel() * 10000;
			_PDV = prisma.getNivel() * 10000;
			_gfxID = prisma.getAlineacion() == 1 ? 8101 : 8100;
			prisma.actualizarStats();
			_totalStats = prisma.getStats();
		}

		public ArrayList<HechizoLanzado> getHechizosLanzados() {
			return _hechizosLanzados;
		}

		public void actualizaHechizoLanzado() {
			ArrayList<HechizoLanzado> copia = new ArrayList<HechizoLanzado>();
			copia.addAll(_hechizosLanzados);
			int i = 0;
			for (HechizoLanzado HL : copia) {
				HL.actuSigLanzamiento();
				if (HL.getSigLanzamiento() <= 0) {
					_hechizosLanzados.remove(i);
					--i;
				}
				++i;
			}
		}

		public void addHechizoLanzado(Luchador objetivo, Hechizo.StatsHechizos sort, Luchador lanzador) {
			HechizoLanzado lanzado = new HechizoLanzado(objetivo, sort, lanzador);
			_hechizosLanzados.add(lanzado);
		}

		public int getID() {
			return _id;
		}

		public Luchador getTransportando() {
			return _transportado;
		}

		public void setTransportando(Luchador transportado) {
			_transportado = transportado;
		}

		public Luchador getTransportadoPor() {
			return _transportadoPor;
		}

		public void setTransportadoPor(Luchador transportadoPor) {
			_transportadoPor = transportadoPor;
		}

		public int getGfxID() {
			return _gfxID;
		}

		public void setGfxID(int gfxID) {
			_gfxID = gfxID;
		}

		public ArrayList<EfectoHechizo> getBuffPelea() {
			return _buffsPelea;
		}

		public boolean esInvisible() {
			return tieneBuff(150);
		}

		public Mapa.Celda getCeldaPelea() {
			return _celda;
		}

		public void setCeldaPelea(Mapa.Celda celda) {
			_celda = celda;
		}

		public void setEquipoBin(int i) {
			_equipoBin = i;
		}

		public boolean estaMuerto() {
			return _estaMuerto;
		}

		public void setMuerto(boolean estaMuerto) {
			_estaMuerto = estaMuerto;
		}

		public boolean estaRetirado() {
			return _estaRetirado;
		}

		public Personaje getPersonaje() {
			if (_tipo == 1) {
				return _perso;
			}
			return null;
		}

		public Recaudador getRecau() {
			if (_tipo == 5) {
				return _recaudador;
			}
			return null;
		}

		public Prisma getPrisma() {
			if (_tipo == 7) {
				return _prisma;
			}
			return null;
		}

		public boolean calculaSiGC(int porcGC) {
			if (porcGC < 2)
				return false;
			Stats statsConBuff = getTotalStatsConBuff();
			int agi = statsConBuff.getEfecto(119);
			if (agi < 0)
				agi = 0;
			porcGC -= statsConBuff.getEfecto(115);
			porcGC = (int) (porcGC * 2.9901D / Math.log((agi + 12)));
			if (porcGC < 2)
				porcGC = 2;
			int jet = Fórmulas.getRandomValor(1, porcGC);
			return (jet == porcGC);
		}

		public boolean testSiEsGC(int porcGC, StatsHechizos sHechizo, Luchador luchador) {
			Personaje perso = luchador.getPersonaje();
			if (porcGC < 2)
				return false;
			Stats statsConBuff = getTotalStatsConBuff();
			int agi = statsConBuff.getEfecto(119);
			if (agi < 0)
				agi = 0;
			porcGC -= statsConBuff.getEfecto(115);
			if (luchador.getTipo() == 1
					&& perso.getHechizosSetClase().containsKey(Integer.valueOf(sHechizo.getHechizoID()))) {
				int modi = perso.getModifSetClase(sHechizo.getHechizoID(), 287);
				porcGC -= modi;
			}
			porcGC = (int) (porcGC * 2.9901D / Math.log((agi + 12)));
			if (porcGC < 2)
				porcGC = 2;
			int jet = Fórmulas.getRandomValor(1, porcGC);
			return (jet == porcGC);
		}

		public ArrayList<EfectoHechizo> getBuffsPorEfectoID(int efectotID) {
			ArrayList<EfectoHechizo> buffs = new ArrayList<EfectoHechizo>();
			for (EfectoHechizo buff : _buffsPelea) {
				if (buff.getEfectoID() != efectotID)
					continue;
				buffs.add(buff);
			}
			return buffs;
		}

		public Personaje.Stats getTotalStatsSinBuff() {
			Personaje.Stats stats = new Personaje.Stats(new TreeMap<Integer, Integer>());
			stats = _totalStats;
			return stats;
		}

		public Personaje.Stats getTotalStatsConBuff() {
			Personaje.Stats stats = new Personaje.Stats(new TreeMap<Integer, Integer>());
			stats = _totalStats;
			stats = Personaje.Stats.acumularStats(stats, getBuffsStatsPelea());
			return stats;
		}

		private Personaje.Stats getBuffsStatsPelea() {
			Personaje.Stats stats = new Personaje.Stats();
			for (EfectoHechizo entry : _buffsPelea) {
				stats.addStat(entry.getEfectoID(), entry.getValor());
			}
			return stats;
		}

		public String stringGM() {
			String str = "";
			str = String.valueOf(str) + _celda.getID() + ";";
			str = String.valueOf(str) + "1;";
			str = String.valueOf(str) + "0;";
			str = String.valueOf(str) + _id + ";";
			str = String.valueOf(str) + getNombreLuchador() + ";";
			switch (_tipo) {
			case 1: {
				Personaje.Stats totalStats = getTotalStatsConBuff();
				str = String.valueOf(str) + _perso.getClase(false) + ";";
				str = String.valueOf(str) + _perso.getGfxID() + "^" + _perso.getTalla() + ";";
				str = String.valueOf(str) + _perso.getSexo() + ";";
				str = String.valueOf(str) + _perso.getNivel() + ";";
				str = String.valueOf(str) + _perso.getAlineacion() + ",";
				str = String.valueOf(str) + "0,";
				str = String.valueOf(str)
						+ (_perso.estaMostrandoAlas() ? Integer.valueOf(_perso.getNivelAlineacion()) : "0") + ",";
				str = String.valueOf(str) + _perso.getID() + ";";
				str = String.valueOf(str) + (_perso.getColor1() == -1 ? "-1" : Integer.toHexString(_perso.getColor1()))
						+ ";";
				str = String.valueOf(str) + (_perso.getColor2() == -1 ? "-1" : Integer.toHexString(_perso.getColor2()))
						+ ";";
				str = String.valueOf(str) + (_perso.getColor3() == -1 ? "-1" : Integer.toHexString(_perso.getColor3()))
						+ ";";
				str = String.valueOf(str) + _perso.getStringAccesorios() + ";";
				str = String.valueOf(str) + getPDVConBuff() + ";";
				str = String.valueOf(str) + totalStats.getEfecto(111) + ";";
				str = String.valueOf(str) + totalStats.getEfecto(128) + ";";
				str = String.valueOf(str) + totalStats.getEfecto(214) + ";";
				str = String.valueOf(str) + totalStats.getEfecto(210) + ";";
				str = String.valueOf(str) + totalStats.getEfecto(213) + ";";
				str = String.valueOf(str) + totalStats.getEfecto(211) + ";";
				str = String.valueOf(str) + totalStats.getEfecto(212) + ";";
				str = String.valueOf(str) + totalStats.getEfecto(160) + ";";
				str = String.valueOf(str) + totalStats.getEfecto(161) + ";";
				str = String.valueOf(str) + _equipoBin + ";";
				if (_perso.estaMontando() && _perso.getMontura() != null) {
					str = String.valueOf(str) + _perso.getMontura().getStringColor(_perso.stringColorDue\u00f1oPavo());
				}
				str = String.valueOf(str) + ";";
				break;
			}
			case 2: {
				str = String.valueOf(str) + "-2;";
				str = String.valueOf(str) + _mob.getModelo().getGfxID() + "^" + _mob.getModelo().getTalla() + ";";
				str = String.valueOf(str) + _mob.getGrado() + ";";
				str = String.valueOf(str) + _mob.getModelo().getColores().replace(",", ";") + ";";
				str = String.valueOf(str) + "0,0,0,0;";
				str = String.valueOf(str) + getPDVMaxConBuff() + ";";
				str = String.valueOf(str) + _mob.getPA() + ";";
				str = String.valueOf(str) + _mob.getPM() + ";";
				str = String.valueOf(str) + _equipoBin;
				break;
			}
			case 5: {
				str = String.valueOf(str) + "-6;";
				str = String.valueOf(str) + "6000^100;";
				Gremio G = Mundo.getGremio(Recaudador.getIDGremioPorMapaID(_pelea._mapaReal.getID()));
				str = String.valueOf(str) + G.getNivel() + ";";
				str = String.valueOf(str) + "1;";
				str = String.valueOf(str) + "2;4;";
				str = String.valueOf(str) + (int) Math.floor(G.getNivel() / 2) + ";"
						+ (int) Math.floor(G.getNivel() / 2) + ";" + (int) Math.floor(G.getNivel() / 2) + ";"
						+ (int) Math.floor(G.getNivel() / 2) + ";" + (int) Math.floor(G.getNivel() / 2) + ";"
						+ (int) Math.floor(G.getNivel() / 2) + ";" + (int) Math.floor(G.getNivel() / 2) + ";";
				str = String.valueOf(str) + _equipoBin;
				break;
			}
			case 7: {
				str = String.valueOf(str) + "-2;";
				str = String.valueOf(str) + (_prisma.getAlineacion() == 1 ? 8101 : 8100) + "^100;";
				str = String.valueOf(str) + _prisma.getNivel() + ";";
				str = String.valueOf(str) + "-1;-1;-1;";
				str = String.valueOf(str) + "0,0,0,0;";
				str = String.valueOf(str) + getPDVMaxConBuff() + ";";
				str = String.valueOf(str) + "0;";
				str = String.valueOf(str) + "0;";
				str = String.valueOf(str) + _equipoBin;
				break;
			}
			case 10: {
				Personaje.Stats totalStats2 = getTotalStatsConBuff();
				str = String.valueOf(str) + _doble.getClase(false) + ";";
				str = String.valueOf(str) + _doble.getGfxID() + "^" + _doble.getTalla() + ";";
				str = String.valueOf(str) + _doble.getSexo() + ";";
				str = String.valueOf(str) + _doble.getNivel() + ";";
				str = String.valueOf(str) + _doble.getAlineacion() + ",";
				str = String.valueOf(str) + "0,";
				str = String.valueOf(str)
						+ (_doble.estaMostrandoAlas() ? Integer.valueOf(_doble.getNivelAlineacion()) : "0") + ",";
				str = String.valueOf(str) + _doble.getID() + ";";
				str = String.valueOf(str) + (_doble.getColor1() == -1 ? "-1" : Integer.toHexString(_doble.getColor1()))
						+ ";";
				str = String.valueOf(str) + (_doble.getColor2() == -1 ? "-1" : Integer.toHexString(_doble.getColor2()))
						+ ";";
				str = String.valueOf(str) + (_doble.getColor3() == -1 ? "-1" : Integer.toHexString(_doble.getColor3()))
						+ ";";
				str = String.valueOf(str) + _doble.getStringAccesorios() + ";";
				str = String.valueOf(str) + getPDVConBuff() + ";";
				str = String.valueOf(str) + totalStats2.getEfecto(111) + ";";
				str = String.valueOf(str) + totalStats2.getEfecto(128) + ";";
				str = String.valueOf(str) + totalStats2.getEfecto(214) + ";";
				str = String.valueOf(str) + totalStats2.getEfecto(210) + ";";
				str = String.valueOf(str) + totalStats2.getEfecto(213) + ";";
				str = String.valueOf(str) + totalStats2.getEfecto(211) + ";";
				str = String.valueOf(str) + totalStats2.getEfecto(212) + ";";
				str = String.valueOf(str) + totalStats2.getEfecto(160) + ";";
				str = String.valueOf(str) + totalStats2.getEfecto(161) + ";";
				str = String.valueOf(str) + _equipoBin + ";";
				if (_doble.estaMontando() && _doble.getMontura() != null) {
					str = String.valueOf(str) + _doble.getMontura().getStringColor(_doble.stringColorDue\u00f1oPavo());
				}
				str = String.valueOf(str) + ";";
			}
			}
			return str;
		}

		public void setEstado(int id, int estado) {
			_estados.remove(id);
			if (estado != 0) {
				_estados.put(id, estado);
			}
		}

		public boolean tieneEstado(int id) {
			if (_estados.get(id) == null) {
				return false;
			}
			return _estados.get(id) != 0;
		}

		public void disminuirEstados() {
			TreeMap<Integer, Integer> copia = new TreeMap<Integer, Integer>();
			for (Map.Entry<Integer, Integer> est : _estados.entrySet()) {
				if (est.getKey() <= 0)
					continue;
				int nVal = est.getValue() - 1;
				if (nVal == 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, 7, 950, String.valueOf(_id),
							String.valueOf(_id) + "," + est.getKey() + ",0");
					continue;
				}
				copia.put(est.getKey(), nVal);
			}
			_estados.clear();
			_estados.putAll(copia);
		}

		public int getPDVMaxConBuff() {
			return _PDVMAX + getValorBuffPelea(125);
		}

		public int getPDVConBuff() {
			return _PDV + getValorBuffPelea(125);
		}

		public int getPDVMax() {
			return _PDVMAX;
		}

		public int getPDV() {
			return _PDV;
		}

		public void restarPDV(int pdv) {
			_PDV -= pdv;
			if (_intocable && pdv > 0) {
				_pelea._retos.remove(17);
				_pelea._retos.put(17, 2);
				SocketManager.ENVIAR_GdaO_RETO_PERDIDO(_pelea, 17);
				for (Luchador luch : _pelea._inicioLucEquipo1) {
					luch._intocable = false;
				}
			}
			if (_contaminacion && pdv > 0) {
				_contaminado = true;
			}
		}

		public void setPDV(int pdv) {
			_PDV = pdv;
		}

		public void setPDVMAX(int pdv) {
			_PDVMAX = pdv;
		}

		public int getValorBuffPelea(int id) {
			int valor = 0;
			for (EfectoHechizo entry : _buffsPelea) {
				if (entry.getEfectoID() != id)
					continue;
				valor += entry.getValor();
			}
			return valor;
		}

		/*
		 * WARNING - Removed try catching itself - possible behaviour change.
		 */
		public void aplicarBuffInicioTurno(Combate pelea) {
			ArrayList<EfectoHechizo> arrayList = _buffsPelea;
			synchronized (arrayList) {
				int[] arrn = Constantes.BUFFS_INICIO_TURNO;
				int n = Constantes.BUFFS_INICIO_TURNO.length;
				for (int i = 0; i < n; ++i) {
					int efectoID = arrn[i];
					ArrayList<EfectoHechizo> buffs = new ArrayList<EfectoHechizo>();
					buffs.addAll(_buffsPelea);
					for (EfectoHechizo EH : buffs) {
						if (EH.getEfectoID() != efectoID)
							continue;
						EH.aplicarBuffDeInicioTurno(pelea, this);
					}
				}
			}
		}

		public EfectoHechizo getBuff(int id) {
			for (EfectoHechizo EH : _buffsPelea) {
				if (EH.getEfectoID() != id || EH.getDuracion() <= 0)
					continue;
				return EH;
			}
			return null;
		}

		public boolean tieneBuff(int id) {
			for (EfectoHechizo entry : _buffsPelea) {
				if (entry.getEfectoID() != id || entry.getDuracion() <= 0)
					continue;
				return true;
			}
			return false;
		}

		public int getDa\u00f1oDominio(int id) {
			int value = 0;
			for (EfectoHechizo entry : _buffsPelea) {
				if (entry.getHechizoID() != id)
					continue;
				value += entry.getValor();
			}
			return value;
		}

		public boolean tieneBuffHechizoID(int id) {
			for (EfectoHechizo entry : _buffsPelea) {
				if (entry.getHechizoID() != id)
					continue;
				return true;
			}
			return false;
		}

		public void actualizarBuffsPelea() {
			ArrayList<EfectoHechizo> efectos = new ArrayList<EfectoHechizo>();
			disminuirEstados();
			for (EfectoHechizo buff : _buffsPelea) {
				int valor;
				int pdv;
				if (buff.disminuirDuracion() > 0) {
					efectos.add(buff);
					continue;
				}
				switch (buff.getEfectoID()) {
				case 125:
					valor = buff.getValor();
					if (buff.getHechizoID() != 441)
						continue;
					_PDVMAX -= valor;
					pdv = 0;
					if (_PDV - valor <= 0) {
						pdv = 0;
						_pelea.agregarAMuertos(this);
						continue;
					}
					pdv = _PDV - valor;
					_PDV = pdv;
				case 150:
					SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, 7, 150,
							(new StringBuilder(String.valueOf(buff.getLanzador().getID()))).toString(),
							String.valueOf(_id) + ",0");
				}
			}
			_buffsPelea.clear();
			_buffsPelea.addAll(efectos);
		}

		public void addBuff(int efectoID, int valor, int turnos, int duracion, boolean desbufeable, int hechizoID,
				String args, Luchador lanzador, boolean veneno) {
			if (hechizoID == 99 || hechizoID == 5 || hechizoID == 20 || hechizoID == 127 || hechizoID == 89
					|| hechizoID == 126 || hechizoID == 115 || hechizoID == 192 || hechizoID == 4 || hechizoID == 1
					|| hechizoID == 6 || hechizoID == 14 || hechizoID == 18 || hechizoID == 7 || hechizoID == 284
					|| hechizoID == 197) {
				desbufeable = true;
			}
			if (hechizoID == 431 || hechizoID == 433 || hechizoID == 437 || hechizoID == 443) {
				desbufeable = false;
			}
			_buffsPelea.add(new EfectoHechizo(efectoID, valor, _puedeJugar ? turnos + 1 : turnos, duracion, desbufeable,
					lanzador, args, hechizoID, veneno));
			switch (efectoID) {
			case 106: {
				SocketManager.ENVIAR_GIE_EFECTO_HECHIZO(_pelea, 7, efectoID, _id, -1, String.valueOf(valor), "10", "",
						turnos, hechizoID);
				break;
			}
			case 950: {
				SocketManager.ENVIAR_GIE_EFECTO_HECHIZO(_pelea, 7, efectoID, _id, -1, "", String.valueOf(valor), "",
						turnos, hechizoID);
				break;
			}
			case 79: {
				valor = Short.parseShort(args.split(";")[0]);
				String valMax = args.split(";")[1];
				String suerte = args.split(";")[2];
				SocketManager.ENVIAR_GIE_EFECTO_HECHIZO(_pelea, 7, efectoID, _id, valor, valMax, suerte, "", turnos,
						hechizoID);
				break;
			}
			case 788: {
				valor = Short.parseShort(args.split(";")[1]);
				String valMax2 = args.split(";")[2];
				if (Integer.parseInt(args.split(";")[0]) == 108) {
					return;
				}
				SocketManager.ENVIAR_GIE_EFECTO_HECHIZO(_pelea, 7, efectoID, _id, valor, String.valueOf(valor),
						String.valueOf(valMax2), "", turnos, hechizoID);
				break;
			}
			case 96:
			case 97:
			case 98:
			case 99:
			case 100:
			case 107:
			case 108:
			case 165:
			case 781:
			case 782: {
				valor = Short.parseShort(args.split(";")[0]);
				String valMax1 = args.split(";")[1];
				if (valMax1.compareTo("-1") == 0 || hechizoID == 82 || hechizoID == 94 || hechizoID == 132) {
					SocketManager.ENVIAR_GIE_EFECTO_HECHIZO(_pelea, 7, efectoID, _id, valor, "", "", "", turnos,
							hechizoID);
					break;
				}
				if (valMax1.compareTo("-1") == 0)
					break;
				SocketManager.ENVIAR_GIE_EFECTO_HECHIZO(_pelea, 7, efectoID, _id, valor, valMax1, "", "", turnos,
						hechizoID);
				break;
			}
			default: {
				SocketManager.ENVIAR_GIE_EFECTO_HECHIZO(_pelea, 7, efectoID, _id, valor, "", "", "", turnos, hechizoID);
			}
			}
		}

		public int getIniciativa() {
			if (_tipo == 1) {
				return _perso.getIniciativa();
			}
			if (_tipo == 2) {
				return _mob.getIniciativa();
			}
			if (_tipo == 5) {
				return Mundo.getGremio(_recaudador.getGremioID()).getNivel();
			}
			if (_tipo == 7) {
				return 0;
			}
			if (_tipo == 10) {
				return _doble.getIniciativa();
			}
			return 0;
		}

		public int getNivel() {
			if (_tipo == 1) {
				return _perso.getNivel();
			}
			if (_tipo == 2) {
				return _mob.getNivel();
			}
			if (_tipo == 5) {
				return Mundo.getGremio(_recaudador.getGremioID()).getNivel();
			}
			if (_tipo == 7) {
				return _prisma.getNivel();
			}
			if (_tipo == 10) {
				return _doble.getNivel();
			}
			return 0;
		}

		public String xpStringLuch(String str) {
			if (_perso != null) {
				int max = _perso.getNivel() + 1;
				if (max > LesGuardians.MAX_NIVEL) {
					max = LesGuardians.MAX_NIVEL;
				}
				return String.valueOf(Mundo.getExpNivel((int) _perso.getNivel())._personaje) + str
						+ _perso.getExperiencia() + str + Mundo.getExpNivel((int) max)._personaje;
			}
			return "0" + str + "0" + str + "0";
		}

		public String getNombreLuchador() {
			if (_tipo == 1) {
				return _perso.getNombre();
			}
			if (_tipo == 2) {
				return String.valueOf(_mob.getModelo().getID());
			}
			if (_tipo == 5) {
				return String.valueOf(_recaudador.getN1()) + "," + _recaudador.getN2();
			}
			if (_tipo == 7) {
				return String.valueOf(_prisma.getAlineacion() == 1 ? 1111 : 1112);
			}
			if (_tipo == 10) {
				return _doble.getNombre();
			}
			return "";
		}

		public MobModelo.MobGrado getMob() {
			if (_tipo == 2) {
				return _mob;
			}
			return null;
		}

		public int getEquipoBin() {
			return _equipoBin;
		}

		public int getParamEquipoAliado() {
			return _pelea.getParamEquipo(_id);
		}

		public int getParamEquipoEnemigo() {
			return _pelea.getIDEquipoEnemigo(_id);
		}

		public boolean puedeJugar() {
			return _puedeJugar;
		}

		public void setPuedeJugar(boolean b) {
			_puedeJugar = b;
		}

		public int getPAConBuff() {
			return getTotalStatsConBuff().getEfecto(111);
		}

		public int getPMConBuff() {
			return getTotalStatsConBuff().getEfecto(128);
		}

		public int getTempPA(Combate pelea) {
			return pelea._tempLuchadorPA;
		}

		public int getTempPM(Combate pelea) {
			return pelea._tempLuchadorPM;
		}

		public void addTempPM(Combate pelea, int pm) {
			Combate fight = pelea;
			fight._tempLuchadorPM = (short) (fight._tempLuchadorPM + pm);
		}

		public void addTempPA(Combate pelea, int pa) {
			Combate fight = pelea;
			fight._tempLuchadorPA = (short) (fight._tempLuchadorPA + pa);
		}

		public void setInvocador(Luchador invocador) {
			_invocador = invocador;
		}

		public Luchador getInvocador() {
			return _invocador;
		}

		public void aumentarInvocaciones() {
			++_nroInvocaciones;
		}

		public int getNroInvocaciones() {
			return _nroInvocaciones;
		}

		public boolean esInvocacion() {
			return _invocador != null;
		}

		public boolean esRecaudador() {
			return _recaudador != null;
		}

		public boolean esPrisma() {
			return _prisma != null;
		}

		public boolean esDoble() {
			return _doble != null;
		}

		public synchronized void desbuffear() {
			ArrayList<EfectoHechizo> nuevosBuffs = new ArrayList<EfectoHechizo>();
			for (EfectoHechizo EH : _buffsPelea) {
				if (!EH.esDesbufeable()) {
					nuevosBuffs.add(EH);
				}
				switch (EH.getEfectoID()) {
				case 111:
				case 120: {
					SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, 7, 101, String.valueOf(_id),
							String.valueOf(_id) + ",-" + EH.getValor());
					break;
				}
				case 78:
				case 128: {
					SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, 7, 127, String.valueOf(_id),
							String.valueOf(_id) + ",-" + EH.getValor());
				}
				}
			}
			_buffsPelea.clear();
			_buffsPelea.addAll(nuevosBuffs);
			if (_perso != null && !_estaRetirado) {
				SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
			}
		}

		public void fullPDV() {
			_PDV = _PDVMAX;
		}

		public void setEstaMuerto(boolean b) {
			_estaMuerto = b;
		}

		public void hacerseVisible(int turnos) {
			if (turnos >= 1) {
				return;
			}
			ArrayList<EfectoHechizo> buffs = new ArrayList<EfectoHechizo>();
			buffs.addAll(getBuffPelea());
			for (EfectoHechizo EH : buffs) {
				if (EH.getEfectoID() != 150)
					continue;
				getBuffPelea().remove(EH);
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, 7, 150, String.valueOf(_id), String.valueOf(_id) + ",0");
			SocketManager.ENVIAR_GIC_APARECER_LUCHADORES_INVISIBLES(_pelea, 7, this);
		}

		public void aparecer(Luchador mostrar) {
			int equipo = mostrar.getParamEquipoAliado();
			SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, equipo, 150, String.valueOf(_id), String.valueOf(_id) + ",0");
			SocketManager.ENVIAR_GIC_APARECER_LUCHADORES_INVISIBLES(_pelea, equipo, this);
		}

		public int getPDVMAXFueraPelea() {
			if (_perso != null) {
				return _perso.getPDVMAX();
			}
			if (_mob != null) {
				return _mob.getPDVMAX();
			}
			return 0;
		}

		public int getGfxDefecto() {
			if (_perso != null) {
				return _perso.getGfxID();
			}
			if (_mob != null) {
				return _mob.getModelo().getGfxID();
			}
			return 0;
		}

		public long getXpGive() {
			if (_mob != null) {
				return _mob.getBaseXp();
			}
			return 0L;
		}

		static /* synthetic */ int access$21(Luchador luchador) {
			return luchador._nroInvocaciones;
		}

		static /* synthetic */ void access$22(Luchador luchador, int n) {
			luchador._nroInvocaciones = n;
		}
	}

	public static class Trampa {
		private Luchador _lanzador;
		private Mapa.Celda _celda;
		private byte _tama\u00f1o;
		private int _hechizo;
		private Hechizo.StatsHechizos _trampaHechizo;
		private Combate _pelea;
		private int _color;
		private boolean _visible = true;
		private int _paramEquipoDue\u00f1o = -1;

		public Trampa(Combate pelea, Luchador lanzador, Mapa.Celda celda, byte tama\u00f1o,
				Hechizo.StatsHechizos trampaHechizo, int hechizo) {
			_pelea = pelea;
			_lanzador = lanzador;
			_celda = celda;
			_hechizo = hechizo;
			_tama\u00f1o = tama\u00f1o;
			_trampaHechizo = trampaHechizo;
			_color = Constantes.getColorTrampa(hechizo);
			_paramEquipoDue\u00f1o = lanzador.getParamEquipoAliado();
		}

		public Mapa.Celda getCelda() {
			return _celda;
		}

		public int getParamEquipoDue\u00f1o() {
			return _paramEquipoDue\u00f1o;
		}

		public byte getTama\u00f1o() {
			return _tama\u00f1o;
		}

		public Luchador getLanzador() {
			return _lanzador;
		}

		public void esVisibleParaEnemigo() {
			_visible = true;
		}

		public boolean esVisible() {
			return _visible;
		}

		public int getColor() {
			return _color;
		}

		public void desaparecer() {
			String str = "GDZ-" + _celda.getID() + ";" + _tama\u00f1o + ";" + _color;
			SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, _paramEquipoDue\u00f1o, 999, String.valueOf(_lanzador.getID()),
					str);
			str = "GDC" + _celda.getID();
			SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, _paramEquipoDue\u00f1o, 999, String.valueOf(_lanzador.getID()),
					str);
			if (_visible) {
				int equipo2 = _lanzador.getParamEquipoEnemigo();
				String str2 = "GDZ-" + _celda.getID() + ";" + _tama\u00f1o + ";" + _color;
				SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, equipo2, 999, String.valueOf(_lanzador.getID()), str2);
				str2 = "GDC" + _celda.getID();
				SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, equipo2, 999, String.valueOf(_lanzador.getID()), str2);
			}
		}

		public void aparecer(int equipo) {
			String str = "GDZ+" + _celda.getID() + ";" + _tama\u00f1o + ";" + _color;
			SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, equipo, 999, String.valueOf(_lanzador.getID()), str);
			str = "GDC" + _celda.getID() + ";Haaaaaaaaz3005;";
			SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, equipo, 999, String.valueOf(_lanzador.getID()), str);
		}

		public void activaTrampa(Luchador trampeado) {
			if (trampeado._estaMuerto) {
				return;
			}
			_pelea.getTrampas().remove(this);
			desaparecer();
			String str = String.valueOf(_hechizo) + "," + _celda.getID() + ",0,1,1," + _lanzador.getID();
			SocketManager.ENVIAR_GA_ACCION_PELEA(_pelea, 7, 307, String.valueOf(trampeado.getID()), str);
			ArrayList<Mapa.Celda> celdas = new ArrayList<Mapa.Celda>();
			celdas.add(_celda);
			for (int a = 0; a < _tama\u00f1o; ++a) {
				char[] dirs = new char[] { 'b', 'd', 'f', 'h' };
				ArrayList<Mapa.Celda> celdas2 = new ArrayList<Mapa.Celda>();
				celdas2.addAll(celdas);
				for (Mapa.Celda aCelda : celdas2) {
					char[] arrc = dirs;
					int n = dirs.length;
					for (int i = 0; i < n; ++i) {
						char d = arrc[i];
						Mapa.Celda celda = _pelea.getMapaCopia().getCelda(
								Pathfinding.getSigIDCeldaMismaDir(aCelda.getID(), d, _pelea.getMapaCopia(), true));
						if (celda == null || celdas.contains(celda))
							continue;
						celdas.add(celda);
					}
				}
			}
			Luchador trampaLanzador = _lanzador.getPersonaje() == null ? new Luchador(_pelea, _lanzador.getMob())
					: new Luchador(_pelea, _lanzador.getPersonaje());
			trampaLanzador.setCeldaPelea(_celda);
			if (_trampaHechizo.getHechizoID() == 1688) {
				_trampaHechizo.aplicaTrampaAPelea(_pelea, trampaLanzador, _celda, celdas, false);
			} else {
				_trampaHechizo.aplicaTrampaAPelea(_pelea, trampaLanzador, trampeado.getCeldaPelea(), celdas, false);
			}
			_pelea.acaboPelea(false, false);
		}
	}
}