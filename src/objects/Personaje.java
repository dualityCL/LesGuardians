package objects;

import common.Constantes;
import common.Fórmulas;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.Mundo;
import game.GameThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Timer;
import objects.Casa;
import objects.Cofre;
import objects.Cuenta;
import objects.Dragopavo;
import objects.Combate;
import objects.Gremio;
import objects.Encarnación;
import objects.Mapa;
import objects.Objeto;
import objects.Mascota;
import objects.Prisma;
import objects.Oficio;
import objects.Objevivo;
import objects.Hechizo;
import objects.Tienda;
import objects.Tutorial;
import org.fusesource.jansi.AnsiConsole;

public class Personaje {
	private int _ID;
	private String _nombre;
	private int _sexo;
	private int _clase;
	private int _color1;
	private int _color2;
	private int _color3;
	private long _kamas;
	private int _puntosHechizo;
	private int _capital;
	private int _energia;
	private int _nivel;
	private long _experiencia;
	private int _talla;
	private int _gfxID;
	private int _esMercante = 0;
	private byte _orientacion = 1;
	private Cuenta _cuenta;
	private String _emotes = "7667711";
	private byte _alineacion = 0;
	private int _deshonor = 0;
	private int _honor = 0;
	private boolean _mostrarAlas = false;
	private int _nivelAlineacion = 0;
	private Gremio.MiembroGremio _miembroGremio;
	private boolean _mostrarConeccionAmigos;
	private String _canales;
	Stats _baseStats;
	private Combate _pelea;
	private boolean _ocupado;
	private Mapa _mapa;
	private Mapa.Celda _celda;
	private int _PDV;
	private boolean _estarBanco;
	private int _PDVMAX;
	private boolean _sentado;
	private boolean _listo = false;
	private boolean _enLinea = false;
	private Grupo _grupo;
	private int _intercambioCon = 0;
	private Mundo.Intercambio _intercambio;
	private Mundo.InvitarTaller _tallerInvitado;
	private int _conversandoCon = 0;
	private int _invitando = 0;
	private int _dueloID = -1;
	private Map<Integer, Hechizo.StatsHechizos> _hechizos = new TreeMap<Integer, Hechizo.StatsHechizos>();
	private Map<Integer, Character> _lugaresHechizos = new TreeMap<Integer, Character>();
	private Map<Integer, Objeto> _objetos = new TreeMap<Integer, Objeto>();
	private ArrayList<Objeto> _tienda = new ArrayList<Objeto>();
	private Map<Integer, Oficio.StatsOficio> _statsOficios = new TreeMap<Integer, Oficio.StatsOficio>();
	private Timer _recuperarVida;
	private String _puntoSalvado;
	private int _exPdv;
	private Mapa.Cercado _enCercado;
	private int _emoteActivado = 0;
	private Oficio.AccionTrabajo _accionTrabajo;
	private Dragopavo _montura;
	private int _xpDonadaMontura = 0;
	private boolean _montando = false;
	private boolean _enZaaping = false;
	private ArrayList<Short> _zaaps = new ArrayList<Short>();
	private boolean _ausente = false;
	private boolean _invisible = false;
	private Personaje _siguiendo = null;
	private boolean _olvidandoHechizo = false;
	private boolean _esDoble = false;
	private boolean _recaudando = false;
	private boolean _dragopaveando = false;
	private int _recaudandoRecaudadorID = 0;
	private MisionPVP _misionPvp = null;
	private int _titulo = 0;
	private int _esposo = 0;
	private int _esOK = 0;
	private GameThread.AccionDeJuego _taller = null;
	private Mundo.Trueque _trueque = null;
	private ArrayList<Integer> _setClase = new ArrayList<Integer>();
	private Map<Integer, Mundo.Duo<Integer, Integer>> _hechizosSetClase = new TreeMap<Integer, Mundo.Duo<Integer, Integer>>();
	private boolean _esFantasma = false;
	private Mapa _tempMapaDefPerco = null;
	private Mapa.Celda _tempCeldaDefPerco = null;
	private Map<Integer, Personaje> _seguidores = new TreeMap<Integer, Personaje>();
	private Cofre _cofre;
	private Casa _casa;
	private Mascota _mascota;
	private int _scrollFuerza = 0;
	private int _scrollInteligencia = 0;
	private int _scrollAgilidad = 0;
	private int _scrollSuerte = 0;
	private int _scrollVitalidad = 0;
	private int _scrollSabiduria = 0;
	private boolean _oficioPublico = false;
	private String _stringOficiosPublicos = "";
	private boolean _listaArtesanos = false;
	private int _bendHechizo = 0;
	private int _bendEfecto = 0;
	private int _bendModif = 0;
	private boolean _cambiarNombre = false;
	private boolean _enKoliseo = false;
	private GrupoKoliseo _koliseo = null;
	private int _categoria = 0;
	private boolean _aceptaKoli = false;
	private int _restriccionesA;
	private int _restriccionesB;
	private boolean _puedeAgredir;
	private boolean _puedeDesafiar;
	private boolean _puedeIntercambiar;
	private boolean _puedeAtacarAMutante;
	private boolean _puedeChatATodos;
	private boolean _puedeMercante;
	private boolean _puedeUsarObjetos;
	private boolean _puedeInteractuarRecaudador;
	private boolean _puedeInteractuarObjetos;
	private boolean _puedeHablarNPC;
	private boolean _puedeAtacarMobsDungCuandoMutante;
	private boolean _puedeMoverTodasDirecciones;
	private boolean _puedeAtacarMobsCualquieraCuandoMutante;
	private boolean _puedeInteractuarPrisma;
	private boolean _puedeSerAgredido;
	private boolean _puedeSerDesafiado;
	private boolean _puedeHacerIntercambio;
	private boolean _puedeSerAtacado;
	private boolean _forzadoCaminar;
	private boolean _esLento;
	private boolean _puedeSwitchModoCriatura;
	private boolean _esTumba;
	private String _forjaEcK;
	private String _ultimaMisionPVP = "";
	private boolean _pescarKuakua = false;
	private Encarnación _encarnacion;
	private int _idEncarnacion = -1;
	private boolean _reconectado = false;
	private Tutorial _tutorial;
	private boolean _defendiendo = false;
	private boolean _agresion = false;
	private long _tiempoAgre = 0L;
	private boolean _huir = true;
	private int _objetoIDRomper = 0;
	private boolean _rompiendo = false;
	private int savestat;

	public void setTutorial(Tutorial tuto) {
		_tutorial = tuto;
	}

	public Tutorial getTutorial() {
		return _tutorial;
	}

	public void setReconectado(boolean recon) {
		_reconectado = recon;
	}

	public boolean getReconectado() {
		Personaje.setTitle("Les Guardians, Conectados : " + LesGuardians._servidorPersonaje.nroJugadoresLinea());
		return _reconectado;
	}

	public void setEncarnacion(Encarnación encarnacion) {
		_encarnacion = encarnacion;
		_idEncarnacion = encarnacion != null ? encarnacion.getID() : -1;
	}

	public int getIDEncarnacion() {
		return _idEncarnacion;
	}

	public Encarnación getEncarnacion() {
		return _encarnacion;
	}

	public void setPescarKuakua(boolean pescar) {
		_pescarKuakua = pescar;
	}

	public boolean getPescarKuakua() {
		return _pescarKuakua;
	}

	public void setUltimaMision(String nombre) {
		_ultimaMisionPVP = nombre;
	}

	public String getUltimaMision() {
		return _ultimaMisionPVP;
	}

	public void setForjaEcK(String forja) {
		_forjaEcK = forja;
	}

	public String getForjaEcK() {
		return _forjaEcK;
	}

	public void efectuarRestriccionesA() {
		_puedeAgredir = (_restriccionesA & 1) != 1;
		_puedeDesafiar = (_restriccionesA & 2) != 2;
		_puedeIntercambiar = (_restriccionesA & 4) != 4;
		_puedeAtacarAMutante = (_restriccionesA & 8) == 8;
		_puedeChatATodos = (_restriccionesA & 0x10) != 16;
		_puedeMercante = (_restriccionesA & 0x20) != 32;
		_puedeUsarObjetos = (_restriccionesA & 0x40) != 64;
		_puedeInteractuarRecaudador = (_restriccionesA & 0x80) != 128;
		_puedeInteractuarObjetos = (_restriccionesA & 0x100) != 256;
		_puedeHablarNPC = (_restriccionesA & 0x200) != 512;
		_puedeAtacarMobsDungCuandoMutante = (_restriccionesA & 0x1000) == 4096;
		_puedeMoverTodasDirecciones = (_restriccionesA & 0x2000) == 8192;
		_puedeAtacarMobsCualquieraCuandoMutante = (_restriccionesA & 0x4000) == 16384;
		_puedeInteractuarPrisma = (_restriccionesA & 0x8000) != 32768;
	}

	public String mostrarmeA() {
		_puedeAgredir = (_restriccionesA & 1) != 1;
		_puedeDesafiar = (_restriccionesA & 2) != 2;
		_puedeIntercambiar = (_restriccionesA & 4) != 4;
		_puedeAtacarAMutante = (_restriccionesA & 8) == 8;
		_puedeChatATodos = (_restriccionesA & 0x10) != 16;
		_puedeMercante = (_restriccionesA & 0x20) != 32;
		_puedeUsarObjetos = (_restriccionesA & 0x40) != 64;
		_puedeInteractuarRecaudador = (_restriccionesA & 0x80) != 128;
		_puedeInteractuarObjetos = (_restriccionesA & 0x100) != 256;
		_puedeHablarNPC = (_restriccionesA & 0x200) != 512;
		_puedeAtacarMobsDungCuandoMutante = (_restriccionesA & 0x1000) == 4096;
		_puedeMoverTodasDirecciones = (_restriccionesA & 0x2000) == 8192;
		_puedeAtacarMobsCualquieraCuandoMutante = (_restriccionesA & 0x4000) == 16384;
		_puedeInteractuarPrisma = (_restriccionesA & 0x8000) != 32768;
		String retorno = "RESTRICCIONES DE A ---------------------------" + _nombre + "\n_puedeAgredir : "
				+ _puedeAgredir + "\n_puedeDesafiar : " + _puedeDesafiar + "\n_puedeIntercambiar : "
				+ _puedeIntercambiar + "\n_puedeAtacarAMutante : " + _puedeAtacarAMutante + "\n_puedeChatATodos : "
				+ _puedeChatATodos + "\n_puedeMercante : " + _puedeMercante + "\n_puedeUsarObjetos : "
				+ _puedeUsarObjetos + "\n_puedeInteractuarRecaudador : " + _puedeInteractuarRecaudador
				+ "\n_puedeInteractuarObjetos : " + _puedeInteractuarObjetos + "\n_puedeHablarNPC : " + _puedeHablarNPC
				+ "\n_puedeAtacarMobsDungCuandoMutante : " + _puedeAtacarMobsDungCuandoMutante
				+ "\n_puedeMoverTodasDirecciones : " + _puedeMoverTodasDirecciones
				+ "\n_puedeAtacarMobsCualquieraCuandoMutante : " + _puedeAtacarMobsCualquieraCuandoMutante
				+ "\n_puedeInteractuarPrisma : " + _puedeInteractuarPrisma;
		return retorno;
	}

	public int getRestriccionesA() {
		int restr = 0;
		if (!_puedeAgredir) {
			++restr;
		}
		if (!_puedeDesafiar) {
			restr += 2;
		}
		if (!_puedeIntercambiar) {
			restr += 4;
		}
		if (_puedeAtacarAMutante) {
			restr += 8;
		}
		if (!_puedeChatATodos) {
			restr += 16;
		}
		if (!_puedeMercante) {
			restr += 32;
		}
		if (!_puedeUsarObjetos) {
			restr += 64;
		}
		if (!_puedeInteractuarRecaudador) {
			restr += 128;
		}
		if (!_puedeInteractuarObjetos) {
			restr += 256;
		}
		if (!_puedeHablarNPC) {
			restr += 512;
		}
		if (_puedeAtacarMobsDungCuandoMutante) {
			restr += 4096;
		}
		if (_puedeMoverTodasDirecciones) {
			restr += 8192;
		}
		if (_puedeAtacarMobsCualquieraCuandoMutante) {
			restr += 16384;
		}
		if (!_puedeInteractuarPrisma) {
			restr += 32768;
		}
		_restriccionesA = restr;
		return restr;
	}

	public void efectuarRestriccionesB() {
		_puedeSerAgredido = (_restriccionesB & 1) != 1;
		_puedeSerDesafiado = (_restriccionesB & 2) != 2;
		_puedeHacerIntercambio = (_restriccionesB & 4) != 4;
		_puedeSerAtacado = (_restriccionesB & 8) != 8;
		_forzadoCaminar = (_restriccionesB & 0x10) == 16;
		_esLento = (_restriccionesB & 0x20) == 32;
		_puedeSwitchModoCriatura = (_restriccionesB & 0x40) != 64;
		_esTumba = (_restriccionesB & 0x80) == 128;
	}

	public String mostrarmeB() {
		_puedeSerAgredido = (_restriccionesB & 1) != 1;
		_puedeSerDesafiado = (_restriccionesB & 2) != 2;
		_puedeHacerIntercambio = (_restriccionesB & 4) != 4;
		_puedeSerAtacado = (_restriccionesB & 8) != 8;
		_forzadoCaminar = (_restriccionesB & 0x10) == 16;
		_esLento = (_restriccionesB & 0x20) == 32;
		_puedeSwitchModoCriatura = (_restriccionesB & 0x40) != 64;
		_esTumba = (_restriccionesB & 0x80) == 128;
		String retorno = "RESTRICCIONES DE B ---------------------------" + _nombre + "\n_puedeSerAgredido : "
				+ _puedeSerAgredido + "\n_puedeSerDesafiado : " + _puedeSerDesafiado + "\n_puedeHacerIntercambio : "
				+ _puedeHacerIntercambio + "\n_puedeSerAtacado : " + _puedeSerAtacado + "\n_forzadoCaminar : "
				+ _forzadoCaminar + "\n_esLento : " + _esLento + "\n_puedeSwitchModoCriatura : "
				+ _puedeSwitchModoCriatura + "\n_esTumba : " + _esTumba;
		return retorno;
	}

	public int getRestriccionesB() {
		int restr = 0;
		if (!_puedeSerAgredido) {
			++restr;
		}
		if (!_puedeSerDesafiado) {
			restr += 2;
		}
		if (!_puedeHacerIntercambio) {
			restr += 4;
		}
		if (!_puedeSerAtacado) {
			restr += 8;
		}
		if (_forzadoCaminar) {
			restr += 16;
		}
		if (_esLento) {
			restr += 32;
		}
		if (!_puedeSwitchModoCriatura) {
			restr += 64;
		}
		if (_esTumba) {
			restr += 128;
		}
		_restriccionesB = restr;
		return restr;
	}

	public Map<Integer, Personaje> getSeguidores() {
		return _seguidores;
	}

	public void setMascota(Mascota mascota) {
		_mascota = mascota;
	}

	public Mascota getMascota() {
		return _mascota;
	}

	public void setGrupoKoliseo(GrupoKoliseo koli) {
		_koliseo = koli;
	}

	public GrupoKoliseo getGrupoKoliseo() {
		return _koliseo;
	}

	public void setEnKoliseo(boolean koliseo) {
		_enKoliseo = koliseo;
	}

	public boolean getEnKoliseo() {
		return _enKoliseo;
	}

	public void setCategoria(int categoria) {
		_categoria = categoria;
	}

	public int getCategoria() {
		return _categoria;
	}

	public Map<Integer, Mundo.Duo<Integer, Integer>> getHechizosSetClase() {
		return _hechizosSetClase;
	}

	public void delHechizosSetClase(int hechizo) {
		if (_hechizosSetClase.containsKey(hechizo)) {
			_hechizosSetClase.remove(hechizo);
		}
	}

	public void addHechizosSetClase(int hechizo, int efecto, int modificacion) {
		if (!_hechizosSetClase.containsKey(hechizo)) {
			_hechizosSetClase.put(hechizo, new Mundo.Duo<Integer, Integer>(efecto, modificacion));
		}
	}

	public void setListaArtesanos(boolean viendo) {
		_listaArtesanos = viendo;
	}

	public boolean getListaArtesanos() {
		return _listaArtesanos;
	}

	public void addScrollFuerza(int scroll) {
		_scrollFuerza += scroll;
	}

	public void addScrollAgilidad(int scroll) {
		_scrollAgilidad += scroll;
	}

	public void addScrollSuerte(int scroll) {
		_scrollSuerte += scroll;
	}

	public void addScrollVitalidad(int scroll) {
		_scrollVitalidad += scroll;
	}

	public void addScrollSabiduria(int scroll) {
		_scrollSabiduria += scroll;
	}

	public void addScrollInteligencia(int scroll) {
		_scrollInteligencia += scroll;
	}

	public int getScrollFuerza() {
		return _scrollFuerza;
	}

	public int getScrollAgilidad() {
		return _scrollAgilidad;
	}

	public int getScrollSuerte() {
		return _scrollSuerte;
	}

	public int getScrollVitalidad() {
		return _scrollVitalidad;
	}

	public int getScrollSabiduria() {
		return _scrollSabiduria;
	}

	public int getScrollInteligencia() {
		return _scrollInteligencia;
	}

	public void setMapaDefPerco(Mapa mapa) {
		_tempMapaDefPerco = mapa;
	}

	public void setCeldaDefPerco(Mapa.Celda celda) {
		_tempCeldaDefPerco = celda;
	}

	public Mapa.Celda getCeldaDefPerco() {
		return _tempCeldaDefPerco;
	}

	public Mapa getMapaDefPerco() {
		return _tempMapaDefPerco;
	}

	public ArrayList<Integer> getSetClase() {
		return _setClase;
	}

	public void setSetClase(ArrayList<Integer> SetClase) {
		_setClase = SetClase;
	}

	public void agregarSetClase(int item) {
		if (!_setClase.contains(item)) {
			_setClase.add(item);
		}
	}

	public void borrarSetClase(int item) {
		if (_setClase.contains(item)) {
			int index = _setClase.indexOf(item);
			_setClase.remove(index);
		}
	}

	public int getMercante() {
		return _esMercante;
	}

	public void setMercante(int mercante) {
		_esMercante = mercante;
	}

	public Mundo.Trueque getTrueque() {
		return _trueque;
	}

	public void setTrueque(Mundo.Trueque trueque) {
		_trueque = trueque;
	}

	public GameThread.AccionDeJuego getTaller() {
		return _taller;
	}

	public void setTaller(GameThread.AccionDeJuego Taller) {
		_taller = Taller;
	}

	public Personaje(int id, String nombre, int sexo, int clase, int color1, int color2, int color3, long kamas,
			int pts, int capital, int energia, int nivel, long exp, int talla, int gfxID, byte alineacion, int cuenta,
			Map<Integer, Integer> stats, int mostrarAmigos, byte mostarAlineacion, String canal, short mapa, int celda,
			String inventario, int pdvPorc, String hechizos, String ptoSalvada, String oficios, int xpMontura,
			int montura, int honor, int deshonor, int nivelAlineacion, String zaaps, byte titulo, int esposoId,
			String tienda, int mercante, int ScrollFuerza, int ScrollInteligencia, int ScrollAgilidad, int ScrollSuerte,
			int ScrollVitalidad, int ScrollSabiduria, int restriccionesA, int restriccionesB, int encarnacion) {
		String[] infos;
		Objeto obj;
		_encarnacion = Mundo.getEncarnacion(encarnacion);
		if (_encarnacion != null) {
			_idEncarnacion = encarnacion;
		}
		_oficioPublico = false;
		_scrollAgilidad = ScrollAgilidad;
		_scrollFuerza = ScrollFuerza;
		_scrollInteligencia = ScrollInteligencia;
		_scrollSabiduria = ScrollSabiduria;
		_scrollSuerte = ScrollSuerte;
		_scrollVitalidad = ScrollVitalidad;
		_ID = id;
		_nombre = nombre;
		_sexo = sexo;
		_clase = clase;
		_color1 = color1;
		_color2 = color2;
		_color3 = color3;
		savestat = 0;
		_kamas = kamas;
		_puntosHechizo = pts;
		_capital = capital;
		_alineacion = alineacion;
		_honor = honor;
		_deshonor = deshonor;
		_nivelAlineacion = nivelAlineacion;
		_energia = energia;
		_nivel = nivel;
		_experiencia = exp;
		if (montura != -1) {
			_montura = Mundo.getDragopavoPorID(montura);
		}
		_talla = talla;
		_gfxID = gfxID;
		_xpDonadaMontura = xpMontura;
		_baseStats = new Stats(stats, true, this);
		_cuenta = Mundo.getCuenta(cuenta);
		try {
			_cuenta.addPerso(this);
		} catch (NullPointerException e) {
			System.out.println("O personagem " + nombre + " n\u00e3o pode ser adicionado \u00e0 conta.");
		}
		_mostrarConeccionAmigos = mostrarAmigos == 1;
		_esposo = esposoId;
		_mostrarAlas = _alineacion != 0 ? mostarAlineacion == 1 : false;
		_canales = canal;
		_mapa = Mundo.getMapa(mapa);
		_puntoSalvado = ptoSalvada;
		if (_mapa == null) {
			_mapa = Mundo.getMapa((short) mapaClase());
			_celda = _mapa.getCelda((short) 311);
		} else if (_mapa != null) {
			_celda = _mapa.getCelda((short) celda);
			if (_celda == null) {
				_mapa = Mundo.getMapa((short) mapaClase());
				_celda = _mapa.getCelda((short) 311);
			}
		}
		_cambiarNombre = false;
		for (String str : zaaps.split(",")) {
			try {
				_zaaps.add(Short.parseShort(str));
			} catch (Exception exception) {
				// empty catch block
			}
		}
		if (_mapa == null || _celda == null) {
			System.out.println("Mapa ou c\u00e9lula invalida no personagem: " + _nombre + ".");
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException str) {
				// empty catch block
			}
			LesGuardians.cerrarServer();
			return;
		}
		if (!inventario.equals("")) {
			if (inventario.charAt(inventario.length() - 1) == '|') {
				inventario = inventario.substring(0, inventario.length() - 1);
			}
			SQLManager.CARGAR_OBJETOS(inventario.replace("|", ","));
		}
		for (String item : inventario.split("\\|")) {
			int idObj;
			if (item.equals("")
					|| (obj = Mundo.getObjeto(idObj = Integer.parseInt((infos = item.split(":"))[0]))) == null)
				continue;
			_objetos.put(obj.getID(), obj);
		}
		if (!tienda.equals("")) {
			if (tienda.charAt(tienda.length() - 1) == '|') {
				tienda = tienda.substring(0, tienda.length() - 1);
			}
			SQLManager.CARGAR_OBJETOS(tienda.replace("|", ","));
		}
		for (String item : tienda.split("\\|")) {
			int idObjeto;
			if (item.equals("")
					|| (obj = Mundo.getObjeto(idObjeto = Integer.parseInt((infos = item.split(":"))[0]))) == null)
				continue;
			_tienda.add(obj);
		}
		_esMercante = mercante;
		_PDVMAX = _encarnacion != null ? _encarnacion.getPDVMAX()
				: (nivel - 1) * 5 + (_nivel > 200 ? (_nivel - 200) * (_clase == 11 ? 2 : 1) * 5 : 0)
						+ Constantes.getBasePDV(clase) + getTotalStats().getEfecto(125);
		_PDV = pdvPorc > 100 ? _PDVMAX * 100 / 100 : _PDVMAX * pdvPorc / 100;
		analizarPosHechizos(hechizos);
		_recuperarVida = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				regenerarPuntoAPunto();
			}
		});
		_exPdv = _PDV;
		if (!oficios.equals("")) {
			for (String aJobData : oficios.split(";")) {
				infos = aJobData.split(",");
				try {
					int oficioID = Integer.parseInt(infos[0]);
					long xp = Long.parseLong(infos[1]);
					Oficio oficio = Mundo.getOficio(oficioID);
					int pos = aprenderOficio(oficio);
					if (pos == -1)
						continue;
					Oficio.StatsOficio statsOficio = _statsOficios.get(pos);
					statsOficio.addXP(this, xp);
				} catch (Exception exception) {
					// empty catch block
				}
			}
		}
		_titulo = titulo;
		_restriccionesA = restriccionesA;
		_restriccionesB = restriccionesB;
		efectuarRestriccionesA();
		efectuarRestriccionesB();
		refrescarSetClase();
		Objeto mascota = getObjPosicion(8);
		if (mascota != null) {
			_mascota = Mundo.getMascota(mascota.getID());
		}
	}

	public Personaje(int id, String nombre, int sexo, int clase, int color1, int color2, int color3, int nivel,
			int talla, int gfxid, Map<Integer, Integer> stats, Map<Integer, Objeto> objetos, int pdvPorc,
			byte mostarAlineacion, int montura, int nivelAlineacion, byte alineacion) {
		_ID = id;
		_nombre = nombre;
		_sexo = sexo;
		_clase = clase;
		_color1 = color1;
		_color2 = color2;
		_color3 = color3;
		_nivel = nivel;
		_nivelAlineacion = nivelAlineacion;
		_talla = talla;
		_gfxID = gfxid;
		_baseStats = new Stats(stats, true, this);
		_objetos.putAll(objetos);
		_PDVMAX = _encarnacion != null ? _encarnacion.getPDVMAX()
				: (nivel - 1) * 5 + (_nivel > 200 ? (_nivel - 200) * 5 : 0) + Constantes.getBasePDV(clase)
						+ getTotalStats().getEfecto(125);
		_PDV = _PDVMAX * pdvPorc / 100;
		_recuperarVida = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				regenerarPuntoAPunto();
			}
		});
		_exPdv = _PDV;
		_alineacion = alineacion;
		_mostrarAlas = _alineacion != 0 ? mostarAlineacion == 1 : false;
		if (montura != -1) {
			_montura = Mundo.getDragopavoPorID(montura);
		}
	}

	public static synchronized Personaje crearPersonaje(String nombre, int sexo, int clase, int color1, int color2,
			int color3, Cuenta cuenta) {
		String zaaps = "";
		if (LesGuardians.USAR_ZAAPS) {
			zaaps = "164,528,844,935,951,1158,1242,1841,2191,3022,3250,4263,4739,5295,6137,6855,6954,7411,8037,8088,8125,8163,8437,8785,9454,10297,10304,10317,10349,10643,11170,11210";
		}
		long kamas = 100000L;
		String objetos = "";
		short nivel = LesGuardians.CONFIG_INICIAR_NIVEL;
		if (cuenta.getPrimeraVez() != 0) {
			Objeto obj = Mundo.getObjModelo(1737).crearObjDesdeModelo(40, false);
			Objeto obj2 = Mundo.getObjModelo(580).crearObjDesdeModelo(30, false);
			Objeto obj3 = Mundo.getObjModelo(548).crearObjDesdeModelo(30, false);
			Mundo.addObjeto(obj, true);
			Mundo.addObjeto(obj2, true);
			Mundo.addObjeto(obj3, true);
			obj.setPosicion(35);
			obj2.setPosicion(36);
			obj3.setPosicion(37);
			Mundo.ObjetoSet objSet = Mundo.getObjetoSet(LesGuardians.INICIAR_SET_ID);
			if (objSet != null) {
				for (Objeto.ObjetoModelo OM : objSet.getObjetosModelos()) {
					Objeto x = OM.crearObjDesdeModelo(1, true);
					Mundo.addObjeto(x, true);
					if (objetos.length() > 0) {
						objetos = String.valueOf(objetos) + "|";
					}
					objetos = String.valueOf(objetos) + x.getID();
				}
			}
			if (objetos.length() > 0) {
				objetos = String.valueOf(objetos) + "|";
			}
			objetos = String.valueOf(objetos) + obj.getID() + "|" + obj2.getID() + "|" + obj3.getID() + "|";
			kamas += LesGuardians.CONFIG_INICIAR_KAMAS;
			cuenta.setPrimeraVez(0);
			SQLManager.ACTUALIZAR_PRIMERA_VEZ(cuenta);
		}
		Personaje nuevoPersonaje = new Personaje(Mundo.getSigIDPersonaje(), nombre, sexo, clase, color1, color2, color3,
				kamas, (nivel - 1) * 1, (nivel - 1) * 5, (short) 10000, nivel, Mundo.getExpMinPersonaje(nivel),
				(short) 100, Integer.parseInt(clase + "" + sexo), (byte) 0, cuenta.getID(),
				new TreeMap<Integer, Integer>(), 1, (byte) 0, "*#%!pi$:?", Constantes.getMapaInicio(clase), 314,
				objetos, 100, "", "7411,311", "", 0, -1, 0, 0, 0, zaaps, (byte) 0, 0, "", 0, 0, 0, 0, 0, 0, 0, 8192, 0,
				-1);
		nuevoPersonaje._hechizos = Constantes.getHechizosIniciales(clase);
		for (int a = 1; a <= nuevoPersonaje.getNivel(); ++a) {
			Constantes.subirNivelAprenderHechizos(nuevoPersonaje, a);
		}
		nuevoPersonaje._lugaresHechizos = Constantes.getLugaresHechizosIniciales(clase);
		if (!SQLManager.AGREGAR_PJ_EN_BD(nuevoPersonaje, objetos)) {
			return null;
		}
		Mundo.addPersonaje(nuevoPersonaje);
		nuevoPersonaje.setEncarnacion(null);
		nuevoPersonaje._cuenta = cuenta;
		return nuevoPersonaje;
	}

	public void Conectarse() {
		if (_cuenta.getEntradaPersonaje() == null) {
			System.out.println("O personagem " + _nombre + " teve a entrada NULL");
			return;
		}
		PrintWriter out = _cuenta.getEntradaPersonaje().getOut();
		_cuenta.setTempPerso(this);
		_enLinea = true;
		if (_esMercante == 1) {
			_mapa.removerMercante(_ID);
			_esMercante = 0;
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_mapa, _ID);
		}
		if (_montura != null) {
			SocketManager.ENVIAR_Re_DETALLES_MONTURA(this, "+", _montura);
		}
		SocketManager.ENVIAR_Rx_EXP_DONADA_MONTURA(this);
		SocketManager.ENVIAR_ASK_PERSONAJE_SELECCIONADO(out, this);
		for (int idSet = 1; idSet < Mundo.getNumeroObjetoSet(); ++idSet) {
			int num = getNroObjEquipadosDeSet(idSet);
			if (num == 0)
				continue;
			SocketManager.ENVIAR_OS_BONUS_SET(this, idSet, num);
		}
		if (_statsOficios.size() > 0) {
			ArrayList<Oficio.StatsOficio> listaStatOficios = new ArrayList<Oficio.StatsOficio>();
			listaStatOficios.addAll(_statsOficios.values());
			SocketManager.ENVIAR_JS_TRABAJO_POR_OFICIO(this, listaStatOficios);
			SocketManager.ENVIAR_JX_EXPERINENCIA_OFICIO(this, listaStatOficios);
			SocketManager.ENVIAR_JO_OFICIO_OPCIONES(this, listaStatOficios);
			Objeto obj = getObjPosicion(1);
			if (obj != null) {
				for (Oficio.StatsOficio statOficio : listaStatOficios) {
					Oficio oficio = statOficio.getOficio();
					if (!oficio.herramientaValida(obj.getModelo().getID()))
						continue;
					SocketManager.ENVIAR_OT_OBJETO_HERRAMIENTA(out, oficio.getID());
					String strOficioPub = Constantes.trabajosOficioTaller(oficio.getID());
					if (_mapa.esTaller() && _oficioPublico) {
						SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(out, "+", _ID, strOficioPub);
					}
					_stringOficiosPublicos = strOficioPub;
				}
			}
		}
		SocketManager.ENVIAR_ZS_ENVIAR_ALINEACION(out, _alineacion);
		SocketManager.ENVIAR_cC_ACTIVAR_CANALES(out,
				String.valueOf(_canales) + "^" + (_cuenta.getRango() > 1 ? "@\u00c2\u00a4" : ""));
		if (_miembroGremio != null) {
			SocketManager.ENVIAR_gS_STATS_GREMIO(this, _miembroGremio);
		}
		SocketManager.ENVIAR_SL_LISTA_HECHIZOS(this);
		SocketManager.ENVIAR_eL_LISTA_EMOTES(this, _emotes, "0");
		SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this);
		SocketManager.ENVIAR_FO_MOSTRAR_CONEXION_AMIGOS(out, _mostrarConeccionAmigos);
		_cuenta.mensajeAAmigos();
		SocketManager.ENVIAR_Im_INFORMACION(this, "189");
		if (!_cuenta.getUltimaConeccion().equals("") && !_cuenta.getUltimoIP().equals("")) {
			SocketManager.ENVIAR_Im_INFORMACION(this,
					"0152;" + _cuenta.getUltimaConeccion() + "~" + _cuenta.getUltimoIP());
		}
		SocketManager.ENVIAR_Im_INFORMACION(this, "0153;" + _cuenta.getActualIP());
		if (!_cuenta.getActualIP().isEmpty()) {
			_cuenta.setUltimoIP(_cuenta.getActualIP());
		}
		Date fechaActual = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
		String dia = dateFormat.format(fechaActual);
		dateFormat = new SimpleDateFormat("MM");
		String mes = dateFormat.format(fechaActual);
		dateFormat = new SimpleDateFormat("yyyy");
		String a\u00f1o = dateFormat.format(fechaActual);
		dateFormat = new SimpleDateFormat("HH");
		String hora = dateFormat.format(fechaActual);
		dateFormat = new SimpleDateFormat("mm");
		String min = dateFormat.format(fechaActual);
		_cuenta.setUltimaConeccion(String.valueOf(a\u00f1o) + "~" + mes + "~" + dia + "~" + hora + "~" + min);
		if (_miembroGremio != null) {
			_miembroGremio.setUltConeccion(String.valueOf(a\u00f1o) + "~" + mes + "~" + dia + "~" + hora + "~" + min);
		}
		SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this, LesGuardians.MSG_BEM_VINDO_1);
		SocketManager.ENVIAR_al_ESTADO_ZONA_ALINEACION(out);
		SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(out, Long.toString(getRestriccionesA(), 36));
		_recuperarVida.start();
		if (_pelea != null) {
			_reconectado = true;
			return;
		}
		SocketManager.ENVIAR_ILS_TIEMPO_REGENERAR_VIDA(this, 1000);
		ArrayList<Integer> array = new ArrayList<Integer>();
		array.addAll(_objetos.keySet());
		for (Integer idObj : array) {
			Mascota masc = null;
			masc = Mundo.getMascota(idObj);
			if (masc == null || masc.getPDV() < 1)
				continue;
			if (masc.esDevoraAlmas()) {
				SocketManager.ENVIAR_Im_INFORMACION(this, "025");
				continue;
			}
			if (masc.entreComidas() > 1440L) {
				if (masc.getDelgado()) {
					restarVidaMascota(masc);
				}
				masc.setCorpulencia(2);
				SocketManager.ENVIAR_Im_INFORMACION(this, "150");
				continue;
			}
			SocketManager.ENVIAR_Im_INFORMACION(this, "025");
		}
		mostrarRates();
		SQLManager.UPDATE_CUENTA_LOG_UNO(_cuenta.getID());
	}

	public void crearJuegoPJ() {
		if (_cuenta.getEntradaPersonaje() == null) {
			return;
		}
		PrintWriter out = _cuenta.getEntradaPersonaje().getOut();
		SocketManager.ENVIAR_GCK_CREAR_PANTALLA_PJ(out);
		SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		SocketManager.ENVIAR_GDM_CAMBIO_DE_MAPA(out, _mapa.getID(), _mapa.getFecha(), _mapa.getCodigo());
		if (_pelea != null) {
			return;
		}
		SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(out, _mapa);
		_mapa.addJugador(this);
	}

	public void regenerarPuntoAPunto() {
		if (_mapa == null || _pelea != null || _PDV == _PDVMAX) {
			return;
		}
		++_PDV;
	}

	public void setHechizos(Map<Integer, Hechizo.StatsHechizos> hechizos) {
		_hechizos.clear();
		_lugaresHechizos.clear();
		_hechizos = hechizos;
		_lugaresHechizos = Constantes.getLugaresHechizosIniciales(_clase);
	}

	public void cambiarSexo() {
		_sexo = _sexo == 1 ? 0 : 1;
	}

	public void setPtosHechizos(int puntos) {
		_puntosHechizo = puntos;
	}

	public boolean enLinea() {
		return _enLinea;
	}

	public void setGrupo(Grupo grupo) {
		_grupo = grupo;
	}

	public Grupo getGrupo() {
		return _grupo;
	}

	public boolean aprenderHechizo(int hechizoID, int nivel, boolean conectando, boolean enviar) {
		if (_encarnacion != null && !conectando) {
			return false;
		}
		Hechizo aprender = Mundo.getHechizo(hechizoID);
		if (aprender == null || aprender.getStatsPorNivel(nivel) == null) {
			System.out.println("[ERROR]Hechizo " + hechizoID + " nivel " + nivel + " no ubicado.");
			return false;
		}
		_hechizos.remove(hechizoID);
		_hechizos.put(hechizoID, aprender.getStatsPorNivel(nivel));
		if (enviar) {
			SocketManager.ENVIAR_SL_LISTA_HECHIZOS(this);
			SocketManager.ENVIAR_Im_INFORMACION(this, "03;" + hechizoID);
		}
		return true;
	}

	public String analizarHechizosABD() {
		String hechizos = "";
		if (_hechizos.isEmpty()) {
			return "";
		}
		for (int key : _hechizos.keySet()) {
			Hechizo.StatsHechizos SH = _hechizos.get(key);
			hechizos = String.valueOf(hechizos) + SH.getHechizoID() + ";" + SH.getNivel() + ";";
			hechizos = _lugaresHechizos.get(key) != null ? String.valueOf(hechizos) + _lugaresHechizos.get(key)
					: String.valueOf(hechizos) + "_";
			hechizos = String.valueOf(hechizos) + ",";
		}
		hechizos = hechizos.substring(0, hechizos.length() - 1);
		return hechizos;
	}

	private void analizarPosHechizos(String str) {
		String[] hechizos;
		String[] arrstring = hechizos = str.split(",");
		int n = hechizos.length;
		for (int i = 0; i < n; ++i) {
			String hechi = arrstring[i];
			try {
				int id = Integer.parseInt(hechi.split(";")[0]);
				int nivel = Integer.parseInt(hechi.split(";")[1]);
				char pos = hechi.split(";")[2].charAt(0);
				aprenderHechizo(id, nivel, true, false);
				_lugaresHechizos.put(id, Character.valueOf(pos));
				continue;
			} catch (NumberFormatException numberFormatException) {
				// empty catch block
			}
		}
	}

	public String getPtoSalvada() {
		return _puntoSalvado;
	}

	public void setOficioPublico(boolean publico) {
		_oficioPublico = publico;
	}

	public void setStrOficiosPublicos(String oficios) {
		_stringOficiosPublicos = oficios;
	}

	public boolean getOficioPublico() {
		return _oficioPublico;
	}

	public String getStringOficiosPublicos() {
		return _stringOficiosPublicos;
	}

	public int mapaClase() {
		int mapa = 8570;
		switch (_clase) {
		case 1: {
			mapa = 7398;
			break;
		}
		case 2: {
			mapa = 7545;
			break;
		}
		case 3: {
			mapa = 7442;
			break;
		}
		case 4: {
			mapa = 7392;
			break;
		}
		case 5: {
			mapa = 7332;
			break;
		}
		case 6: {
			mapa = 7446;
			break;
		}
		case 7: {
			mapa = 7361;
			break;
		}
		case 8: {
			mapa = 7427;
			break;
		}
		case 9: {
			mapa = 7378;
			break;
		}
		case 10: {
			mapa = 7395;
			break;
		}
		case 11: {
			mapa = 7336;
			break;
		}
		case 12: {
			mapa = 8035;
			break;
		}
		default: {
			mapa = 7411;
		}
		}
		return mapa;
	}

	public void setSalvarZaap(String savePos) {
		_puntoSalvado = savePos;
	}

	public int getIntercambiandoCon() {
		return _intercambioCon;
	}

	public void setIntercambiandoCon(int intercambiando) {
		_intercambioCon = intercambiando;
	}

	public int getConversandoCon() {
		return _conversandoCon;
	}

	public void setConversandoCon(int conversando) {
		_conversandoCon = conversando;
	}

	public long getKamas() {
		return _kamas;
	}

	public void setKamas(long l) {
		if (l < 0L) {
			l = 0L;
		}
		_kamas = l;
	}

	public void addKamas(long l) {
		_kamas += l;
		if (_kamas < 0L) {
			_kamas = 0L;
		}
	}

	public Cuenta getCuenta() {
		return _cuenta;
	}

	public int getPuntosHechizos() {
		return _puntosHechizo;
	}

	public Gremio getGremio() {
		if (_miembroGremio == null) {
			return null;
		}
		return _miembroGremio.getGremio();
	}

	public void setMiembroGremio(Gremio.MiembroGremio gremio) {
		_miembroGremio = gremio;
	}

	public boolean estaListo() {
		return _listo;
	}

	public void setListo(boolean listo) {
		_listo = listo;
	}

	public int getDueloID() {
		return _dueloID;
	}

	public Combate getPelea() {
		return _pelea;
	}

	public void setDueloID(int dueloID) {
		_dueloID = dueloID;
	}

	public int getEnergia() {
		return _energia;
	}

	public boolean mostrarConeccionAmigo() {
		return _mostrarConeccionAmigos;
	}

	public boolean mostrarAlas() {
		return _mostrarAlas;
	}

	public String getCanal() {
		return _canales;
	}

	public boolean esTumba() {
		return _esTumba;
	}

	public void setRestriccionesA(int restr) {
		_restriccionesA = restr;
		efectuarRestriccionesA();
		SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(_cuenta.getEntradaPersonaje().getOut(),
				Integer.toString(_restriccionesA, 36));
	}

	public void setRestriccionesB(int restr) {
		_restriccionesB = restr;
		efectuarRestriccionesB();
	}

	public void convertirTumba() {
		try {
			_gfxID = _clase * 10 + 3;
			_esFantasma = false;
			_esTumba = true;
			_esFantasma = false;
			_puedeAgredir = false;
			_puedeSerAgredido = false;
			_puedeSerDesafiado = false;
			_puedeHacerIntercambio = false;
			_puedeIntercambiar = false;
			_puedeHablarNPC = false;
			_puedeMercante = false;
			_puedeInteractuarRecaudador = false;
			_puedeInteractuarPrisma = false;
			_puedeUsarObjetos = false;
			_forzadoCaminar = true;
			_esLento = true;
			_ocupado = true;
			_puedeAtacarAMutante = false;
			_puedeDesafiar = false;
			_puedeSerAtacado = false;
			_puedeInteractuarObjetos = false;
			SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(_mapa, this);
			SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(_cuenta.getEntradaPersonaje().getOut(),
					Integer.toString(getRestriccionesA(), 36));
			SocketManager.ENVIAR_M1_MENSAJE_SERVER(this, "12", "", "");
		} catch (Exception exception) {
			// empty catch block
		}
	}

	public void agregarEnergia(int energia) {
		int exEnergia = _energia;
		_energia += energia;
		if (_energia > 10000) {
			_energia = 10000;
		}
		if (_esFantasma && exEnergia <= 0 && _energia > 0) {
			if (_encarnacion != null) {
				_gfxID = _encarnacion.getGfx();
			} else {
				deformar();
			}
			_energia = energia;
			_esTumba = false;
			_esFantasma = false;
			_puedeAgredir = true;
			_puedeSerAgredido = true;
			_puedeSerDesafiado = true;
			_puedeHacerIntercambio = true;
			_puedeIntercambiar = true;
			_puedeHablarNPC = true;
			_puedeMercante = true;
			_puedeInteractuarRecaudador = true;
			_puedeInteractuarPrisma = true;
			_puedeUsarObjetos = true;
			_esLento = false;
			_ocupado = false;
			_forzadoCaminar = false;
			_puedeAtacarAMutante = false;
			_puedeDesafiar = true;
			_puedeSerAtacado = true;
			_puedeInteractuarObjetos = true;
			SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
			SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(_mapa, this);
			SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(_cuenta.getEntradaPersonaje().getOut(),
					Long.toString(getRestriccionesA(), 36));
		}
	}

	public void restarEnergia(int energia) {
		if (LesGuardians._cerrando) {
			return;
		}
		_energia -= energia;
		if (_energia <= 0) {
			convertirTumba();
		} else if (_energia < 1500) {
			SocketManager.ENVIAR_M1_MENSAJE_SERVER(this, "11", String.valueOf(energia), "");
		}
	}

	public void volverseFantasma() {
		_gfxID = 8004;
		_esTumba = false;
		_esFantasma = true;
		_puedeAgredir = false;
		_puedeSerAgredido = false;
		_puedeSerDesafiado = false;
		_puedeHacerIntercambio = false;
		_puedeIntercambiar = false;
		_puedeHablarNPC = false;
		_puedeMercante = false;
		_puedeInteractuarRecaudador = false;
		_puedeInteractuarPrisma = false;
		_puedeUsarObjetos = false;
		_forzadoCaminar = true;
		_esLento = true;
		_ocupado = true;
		_puedeAtacarAMutante = false;
		_puedeDesafiar = false;
		_puedeSerAtacado = false;
		_puedeInteractuarObjetos = false;
		if (LesGuardians.MODO_HEROICO) {
			if (_grupo != null) {
				_grupo.dejarGrupo(this);
			}
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_mapa, _ID);
			_celda.removerPersonaje(_ID);
			resetVariables();
			SocketManager.ENVIAR_GO_GAME_OVER(this);
			Mundo.eliminarPj(this, false);
			SQLManager.SALVAR_PERSONAJE(this, false);
			return;
		}
		SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(_mapa, this);
		SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(_cuenta.getEntradaPersonaje().getOut(),
				Long.toString(getRestriccionesA(), 36));
		teleport((short) 1188, (short) 297);
		SocketManager.ENVIAR_M1_MENSAJE_SERVER(this, "15", "", "");
		SocketManager.ENVIAR_IH_COORDINAS_UBICACION(this,
				"12;12;270|-1;33;1399|10;19;268|5;-9;7796|2;-12;8534|-30;-54;4285|-26;35;4551|-23;38;12169|-11;-54;3360|-43;0;10430|-10;13;9227|-41;-17;9539|36;5;1118|24;-43;7910|27;-33;8054|-60;-3;10672|-58;18;10590|-14;31;5717|25;-4;844|");
	}

	public void revivir() {
		if (_encarnacion != null) {
			_gfxID = _encarnacion.getGfx();
		} else {
			deformar();
		}
		_energia = 1000;
		_esTumba = false;
		_esFantasma = false;
		_puedeAgredir = true;
		_puedeSerAgredido = true;
		_puedeSerDesafiado = true;
		_puedeHacerIntercambio = true;
		_puedeIntercambiar = true;
		_puedeHablarNPC = true;
		_puedeMercante = true;
		_puedeInteractuarRecaudador = true;
		_puedeInteractuarPrisma = true;
		_puedeUsarObjetos = true;
		_esLento = false;
		_ocupado = false;
		_forzadoCaminar = false;
		_puedeAtacarAMutante = false;
		_puedeDesafiar = true;
		_puedeSerAtacado = true;
		_puedeInteractuarObjetos = true;
		SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(_mapa, this);
		SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(_cuenta.getEntradaPersonaje().getOut(),
				Long.toString(getRestriccionesA(), 36));
		SocketManager.ENVIAR_IH_COORDINAS_UBICACION(this, "");
	}

	public int getNivel() {
		return _nivel;
	}

	public void setNivel(int nivel) {
		_nivel = nivel;
	}

	public long getExperiencia() {
		return _experiencia;
	}

	public Mapa.Celda getCelda() {
		return _celda;
	}

	public void setCelda(Mapa.Celda celda) {
		_celda.removerPersonaje(_ID);
		_celda = celda;
		celda.addPersonaje(this);
	}

	public int getTalla() {
		return _talla;
	}

	public void setTalla(int talla) {
		_talla = talla;
	}

	public void setPelea(Combate pelea) {
		_pelea = pelea;
		if (pelea == null) {
			return;
		}
		if (_montando && _montura != null) {
			_montura.energiaPerdida(20);
		}
		try {
			if (pelea.getTipoPelea() > 0) {
				for (int i = 20; i < 22; ++i) {
					Objeto obj = getObjPosicion(i);
					if (obj == null)
						continue;
					int idObj = obj.getID();
					String stats = obj.convertirStatsAString();
					String[] arg = stats.split(",");
					obj.clearTodo();
					String[] arrstring = arg;
					int n = arg.length;
					for (int j = 0; j < n; ++j) {
						String efec = arrstring[j];
						String[] val = efec.split("#");
						if (Integer.parseInt(val[0], 16) != 811)
							continue;
						int turnos = Integer.parseInt(val[3], 16);
						if (turnos == 0) {
							borrarObjetoSinEliminar(idObj);
							Mundo.eliminarObjeto(idObj);
							SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, idObj);
							if (i == 21) {
								_bendEfecto = 0;
								_bendHechizo = 0;
								_bendModif = 0;
							}
							Thread.sleep(200L);
							continue;
						}
						String antiguo = "32b#0#0#" + Integer.toString(turnos, 16);
						String nuevo = "32b#0#0#" + Integer.toString(turnos - 1, 16);
						stats = stats.replace(antiguo, nuevo);
						obj.convertirStringAStats(stats);
						SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(this, obj);
						Thread.sleep(200L);
					}
				}
			}
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	public int getGfxID() {
		return _gfxID;
	}

	public void setGfxID(int gfxid) {
		_gfxID = gfxid;
	}

	public void deformar() {
		_gfxID = _clase * 10 + _sexo;
	}

	public int getID() {
		return _ID;
	}

	public Mapa getMapa() {
		return _mapa;
	}

	public String getNombre() {
		return _nombre;
	}

	public boolean estaOcupado() {
		return _ocupado;
	}

	public void setOcupado(boolean ocupado) {
		_ocupado = ocupado;
	}

	public boolean estaSentado() {
		return _sentado;
	}

	public int getSexo() {
		return _sexo;
	}

	public int getClase(boolean original) {
		if (_encarnacion != null && !original) {
			return _encarnacion.getClase();
		}
		return _clase;
	}

	public void setClase(int clase) {
		_clase = clase;
	}

	public void setExperiencia(long exp) {
		_experiencia = exp;
	}

	public int getColor1() {
		return _color1;
	}

	public int getColor2() {
		return _color2;
	}

	public Stats getBaseStats() {
		return _baseStats;
	}

	public int getColor3() {
		return _color3;
	}

	public int getCapital() {
		return _capital;
	}

	public void resetearStats() {
		_baseStats.addStat(125, (short) (-_baseStats.getEfecto(125) + _scrollVitalidad));
		_baseStats.addStat(124, (short) (-_baseStats.getEfecto(124) + _scrollSabiduria));
		_baseStats.addStat(118, (short) (-_baseStats.getEfecto(118) + _scrollFuerza));
		_baseStats.addStat(123, (short) (-_baseStats.getEfecto(123) + _scrollSuerte));
		_baseStats.addStat(119, (short) (-_baseStats.getEfecto(119) + _scrollAgilidad));
		_baseStats.addStat(126, (short) (-_baseStats.getEfecto(126) + _scrollInteligencia));
	}

	public boolean tieneHechizoID(int hechizo) {
		if (_encarnacion != null) {
			return _encarnacion.tieneHechizoID(hechizo);
		}
		return _hechizos.get(hechizo) != null;
	}

	public boolean boostearHechizo(int hechizoID) {
		if (_encarnacion != null || _hechizos.get(hechizoID) == null) {
			return false;
		}
		int antNivel = _hechizos.get(hechizoID).getNivel();
		if (antNivel == 6) {
			return false;
		}
		if (_puntosHechizo >= antNivel
				&& Mundo.getHechizo(hechizoID).getStatsPorNivel(antNivel + 1).getReqNivel() <= _nivel) {
			if (aprenderHechizo(hechizoID, antNivel + 1, false, false)) {
				_puntosHechizo -= antNivel;
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean olvidarHechizo(int hechizoID) {
		if (_encarnacion != null || _hechizos.get(hechizoID) == null) {
			return false;
		}
		int antNivel = _hechizos.get(hechizoID).getNivel();
		if (antNivel <= 1) {
			return false;
		}
		if (aprenderHechizo(hechizoID, 1, false, false)) {
			_puntosHechizo += Fórmulas.costeHechizo(antNivel);
			return true;
		}
		return false;
	}

	public String stringListaHechizos() {
		if (_encarnacion != null) {
			return _encarnacion.stringListaHechizos();
		}
		StringBuffer str = new StringBuffer();
		for (Hechizo.StatsHechizos SH : _hechizos.values()) {
			if (_lugaresHechizos.get(SH.getHechizoID()) == null) {
				str.append(String.valueOf(SH.getHechizoID()) + "~" + SH.getNivel() + "~_;");
				continue;
			}
			str.append(String.valueOf(SH.getHechizoID()) + "~" + SH.getNivel() + "~"
					+ _lugaresHechizos.get(SH.getHechizoID()) + ";");
		}
		return str.toString();
	}

	public void setPosHechizo(int hechizo, char pos) {
		if (_encarnacion != null) {
			_encarnacion.setPosHechizo(hechizo, pos);
			return;
		}
		reemplazarHechizoEnPos(pos);
		_lugaresHechizos.remove(hechizo);
		_lugaresHechizos.put(hechizo, Character.valueOf(pos));
	}

	private void reemplazarHechizoEnPos(char pos) {
		for (int key : _hechizos.keySet()) {
			if (_lugaresHechizos.get(key) == null || !_lugaresHechizos.get(key).equals(Character.valueOf(pos)))
				continue;
			_lugaresHechizos.remove(key);
		}
	}

	public Hechizo.StatsHechizos getStatsHechizo(int hechizoID) {
		if (_encarnacion != null) {
			return _encarnacion.getStatsHechizo(hechizoID);
		}
		return _hechizos.get(hechizoID);
	}

	public String stringParaListaPJsServer() {
		StringBuffer str = new StringBuffer("|");
		str.append(String.valueOf(_ID) + ";");
		str.append(String.valueOf(_nombre) + ";");
		str.append(String.valueOf(_nivel) + ";");
		str.append(String.valueOf(_gfxID) + ";");
		str.append(String.valueOf(_color1 != -1 ? Integer.toHexString(_color1) : "-1") + ";");
		str.append(String.valueOf(_color2 != -1 ? Integer.toHexString(_color2) : "-1") + ";");
		str.append(String.valueOf(_color3 != -1 ? Integer.toHexString(_color3) : "-1") + ";");
		str.append(String.valueOf(getStringAccesorios()) + ";");
		str.append(String.valueOf(_esMercante) + ";");
		str.append(String.valueOf(LesGuardians.SERVER_ID) + ";");
		if (LesGuardians.MODO_HEROICO) {
			str.append(_esFantasma ? Integer.valueOf(1) : "0;");
		} else {
			str.append("0;");
		}
		str.append(";");
		str.append(LesGuardians.MAX_NIVEL);
		return str.toString();
	}

	public void reiniciarCero() {
		revivir();
		_nivel = LesGuardians.CONFIG_INICIAR_NIVEL;
		_encarnacion = null;
		_idEncarnacion = -1;
		_oficioPublico = false;
		_kamas = 0L;
		_puntosHechizo = _nivel - 1;
		_capital = (_nivel - 1) * 5;
		_alineacion = 0;
		_honor = 0;
		_deshonor = 0;
		_nivelAlineacion = 1;
		_energia = 10000;
		_experiencia = Mundo.getExpMinPersonaje(_nivel);
		_montura = null;
		_talla = 100;
		_gfxID = Integer.parseInt(String.valueOf(_clase) + _sexo);
		_xpDonadaMontura = 0;
		_mostrarConeccionAmigos = false;
		_mostrarAlas = false;
		_canales = "*#%!pi$:?";
		_mapa = Mundo.getMapa(Constantes.getMapaInicio(_clase));
		_celda = _mapa.getCelda((short) 340);
		_puntoSalvado = "7411,311";
		_cambiarNombre = false;
		_objetos.clear();
		_tienda.clear();
		_esMercante = 0;
		_PDV = _PDVMAX = (_nivel - 1) * 5 + (_nivel > 200 ? (_nivel - 200) * (_clase == 11 ? 2 : 1) * 5 : 0)
				+ Constantes.getBasePDV(_clase) + getTotalStats().getEfecto(125);
		_hechizos.clear();
		_hechizos = Constantes.getHechizosIniciales(_clase);
		for (int a = 1; a <= _nivel; ++a) {
			Constantes.subirNivelAprenderHechizos(this, a);
		}
		_lugaresHechizos = Constantes.getLugaresHechizosIniciales(_clase);
		_exPdv = _PDV;
		_statsOficios.clear();
		_titulo = 0;
		_restriccionesA = 8192;
		_restriccionesB = 0;
		efectuarRestriccionesA();
		efectuarRestriccionesB();
		_mascota = null;
		_baseStats._statsEfecto.clear();
		_baseStats = new Stats(_baseStats._statsEfecto, true, this);
		_scrollAgilidad = 0;
		_scrollFuerza = 0;
		_scrollInteligencia = 0;
		_scrollSabiduria = 0;
		_scrollSuerte = 0;
		_scrollVitalidad = 0;
	}

	public void mostrarAmigosEnLinea(boolean mostrar) {
		_mostrarConeccionAmigos = mostrar;
	}

	public static void setTitle(String title) {
		AnsiConsole.out.printf("%c]0;%s%c", Character.valueOf('\u001b'), title, Character.valueOf('\u0007'));
	}

	public void mostrarRates() {
		SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION(this, String.valueOf(LesGuardians.NOME_SERVIDOR)
				+ "\n<b>Divers\u00e3o garantida !</b> \n\n"
				+ "Servidor 24/7. Completo e com atualiza\u00e7\u00f5es peri\u00f3dicas para sua melhor jogabilidade. ");
	}

	public void RATES() {
		SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION(this,
				"<b>Evento Atual : " + LesGuardians.EVENTO + "\n<b>Rates do Servidor</b>" + "\nKAMAS : "
						+ LesGuardians.RATE_KAMAS + "   \nDROP : " + LesGuardians.RATE_DROP + "\nEXP : "
						+ LesGuardians.RATE_XP_PVM + "   \nPVP : " + LesGuardians.RATE_XP_PVP + "\nPROF : "
						+ LesGuardians.RATE_XP_PROF + "  \nForja : " + LesGuardians.RATE_PORC_FM + "</b>");
	}

	public void INFOS() {
		long enLinea = System.currentTimeMillis() - LesGuardians._servidorPersonaje.getTiempoInicio();
		int hora = (int) (enLinea / 3600000L);
		int minuto = (int) ((enLinea %= 3600000L) / 60000L);
		int segundo = (int) ((enLinea %= 60000L) / 1000L);
		SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION(this,
				String.valueOf(LesGuardians.NOME_SERVIDOR) + "\n" + "\nDivers\u00e3o garantida !\n"
						+ "\nJogadores Online : " + LesGuardians._servidorPersonaje.nroJugadoresLinea()
						+ "\nM\u00e1ximo de Conex\u00f5es : " + LesGuardians._servidorPersonaje.getRecordJugadores()
						+ "   </b>" + "\nTempo Online : " + hora + "H " + minuto + "M " + segundo + "s");
	}

	public String analizarFiguraDelPJ() {
		String packetOa = "Oa";
		packetOa = String.valueOf(packetOa) + _ID + "|";
		packetOa = String.valueOf(packetOa) + getStringAccesorios();
		return packetOa;
	}

	public String stringGMmercante() {
		StringBuffer str = new StringBuffer();
		str.append(String.valueOf(_celda.getID()) + ";");
		str.append("1;");
		str.append("0;");
		str.append(String.valueOf(_ID) + ";");
		str.append(String.valueOf(_nombre) + ";");
		str.append("-5");
		str.append(getTitulo() > 0 ? "," + getTitulo() + ";" : ";");
		str.append(String.valueOf(_gfxID) + "^" + _talla + ";");
		str.append(String.valueOf(_color1 == -1 ? "-1" : Integer.toHexString(_color1)) + ";");
		str.append(String.valueOf(_color2 == -1 ? "-1" : Integer.toHexString(_color2)) + ";");
		str.append(String.valueOf(_color3 == -1 ? "-1" : Integer.toHexString(_color3)) + ";");
		str.append(String.valueOf(getStringAccesorios()) + ";");
		if (_miembroGremio != null && _miembroGremio.getGremio().getPjMiembros().size() > 9) {
			str.append(String.valueOf(_miembroGremio.getGremio().getNombre()) + ";"
					+ _miembroGremio.getGremio().getEmblema() + ";");
		} else {
			str.append(";;");
		}
		str.append("0");
		return str.toString();
	}

	public String stringGM() {
		StringBuffer str = new StringBuffer();
		if (_pelea != null) {
			return "";
		}
		str.append(String.valueOf(_celda.getID()) + ";");
		str.append(String.valueOf(_orientacion) + ";");
		str.append("0;");
		str.append(String.valueOf(_ID) + ";");
		str.append(String.valueOf(_nombre) + ";");
		str.append(_clase);
		str.append(_titulo > 0 ? "," + _titulo + ";" : ";");
		str.append(String.valueOf(_gfxID) + "^" + _talla + ";");
		str.append(String.valueOf(_sexo) + ";");
		str.append(String.valueOf(_alineacion) + ",");
		str.append(String.valueOf(getNivelAlineacion()) + ",");
		str.append((_mostrarAlas ? Integer.valueOf(getNivelAlineacion()) : "0") + ",");
		str.append(String.valueOf(_nivel) + ",");
		str.append(String.valueOf(_deshonor > 0 ? 1 : 0) + ";");
		str.append(String.valueOf(_color1 == -1 ? "-1" : Integer.toHexString(_color1)) + ";");
		str.append(String.valueOf(_color2 == -1 ? "-1" : Integer.toHexString(_color2)) + ";");
		str.append(String.valueOf(_color3 == -1 ? "-1" : Integer.toHexString(_color3)) + ";");
		str.append(String.valueOf(getStringAccesorios()) + ";");
		if (LesGuardians.AURA_ATIVADA) {
			str.append(String.valueOf(_nivel > 99 ? 1 : (_nivel > 199 ? 2 : (tieneObjSetVampirico() ? 3 : 0))) + ";");
		} else {
			str.append("0;");
		}
		str.append(";");
		str.append(";");
		if (_miembroGremio != null && _miembroGremio.getGremio().getPjMiembros().size() > 9) {
			str.append(String.valueOf(_miembroGremio.getGremio().getNombre()) + ";"
					+ _miembroGremio.getGremio().getEmblema() + ";");
		} else {
			str.append(";;");
		}
		str.append(String.valueOf(Integer.toString(getRestriccionesB(), 36)) + ";");
		str.append(String.valueOf(
				_montando && _montura != null ? _montura.getStringColor(stringColorDue\u00f1oPavo()) : "") + ";");
		str.append(";");
		return str.toString();
	}

	public String getStringAccesorios() {
		StringBuffer str = new StringBuffer();
		str.append(String.valueOf(getModeloObjEnPos(1)) + ",");
		str.append(String.valueOf(getModeloObjEnPos(6)) + ",");
		str.append(String.valueOf(getModeloObjEnPos(7)) + ",");
		str.append(String.valueOf(getModeloObjEnPos(8)) + ",");
		str.append(getModeloObjEnPos(15));
		return str.toString();
	}

	public String stringStatsPacket() {
		Combate.Luchador luchador;
		Stats objEquipStats = getStatsObjEquipados();
		Stats totalStats = getTotalStats();
		Stats boostStats = getStatsBoost();
		Stats benMaldStats = getStatsBendMald();
		refrescarVida();
		StringBuffer str = new StringBuffer("As");
		str.append(String.valueOf(xpString(",")) + "|");
		str.append(String.valueOf(_kamas) + "|");
		if (_encarnacion != null) {
			str.append("0|0|");
		} else {
			str.append(String.valueOf(_capital) + "|" + _puntosHechizo + "|");
		}
		str.append(String.valueOf(_alineacion) + "~" + _alineacion + "," + _nivelAlineacion + "," + getNivelAlineacion()
				+ "," + _honor + "," + _deshonor + "," + (_mostrarAlas ? "1" : "0") + "|");
		int pdv = getPDV();
		int pdvMax = getPDVMAX();
		if (_pelea != null && (luchador = _pelea.getLuchadorPorPJ(this)) != null) {
			pdv = luchador.getPDVConBuff();
			pdvMax = luchador.getPDVMaxConBuff();
		}
		str.append(String.valueOf(pdv) + "," + pdvMax + "|");
		str.append(String.valueOf(_energia) + ",10000|");
		str.append(String.valueOf(getIniciativa()) + "|");
		str.append(String.valueOf(totalStats.getEfecto(176)) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(111)) + "," + objEquipStats.getEfecto(111) + ","
				+ benMaldStats.getEfecto(111) + "," + boostStats.getEfecto(111) + "," + totalStats.getEfecto(111)
				+ "|");
		str.append(String.valueOf(_baseStats.getEfecto(128)) + "," + objEquipStats.getEfecto(128) + ","
				+ benMaldStats.getEfecto(128) + "," + boostStats.getEfecto(128) + "," + totalStats.getEfecto(128)
				+ "|");
		str.append(String.valueOf(_baseStats.getEfecto(118)) + "," + objEquipStats.getEfecto(118) + ","
				+ benMaldStats.getEfecto(118) + "," + boostStats.getEfecto(118) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(125)) + "," + objEquipStats.getEfecto(125) + ","
				+ benMaldStats.getEfecto(125) + "," + boostStats.getEfecto(125) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(124)) + "," + objEquipStats.getEfecto(124) + ","
				+ benMaldStats.getEfecto(124) + "," + boostStats.getEfecto(124) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(123)) + "," + objEquipStats.getEfecto(123) + ","
				+ benMaldStats.getEfecto(123) + "," + boostStats.getEfecto(123) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(119)) + "," + objEquipStats.getEfecto(119) + ","
				+ benMaldStats.getEfecto(119) + "," + boostStats.getEfecto(119) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(126)) + "," + objEquipStats.getEfecto(126) + ","
				+ benMaldStats.getEfecto(126) + "," + boostStats.getEfecto(126) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(117)) + "," + objEquipStats.getEfecto(117) + ","
				+ benMaldStats.getEfecto(117) + "," + boostStats.getEfecto(117) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(182)) + "," + objEquipStats.getEfecto(182) + ","
				+ benMaldStats.getEfecto(182) + "," + boostStats.getEfecto(182) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(112)) + "," + objEquipStats.getEfecto(112) + ","
				+ benMaldStats.getEfecto(112) + "," + boostStats.getEfecto(112) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(142)) + "," + objEquipStats.getEfecto(142) + ","
				+ benMaldStats.getEfecto(142) + "," + boostStats.getEfecto(142) + "|");
		str.append("0,0,0,0|");
		str.append(String.valueOf(_baseStats.getEfecto(138)) + "," + objEquipStats.getEfecto(138) + ","
				+ benMaldStats.getEfecto(138) + "," + boostStats.getEfecto(138) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(178)) + "," + objEquipStats.getEfecto(178) + ","
				+ benMaldStats.getEfecto(178) + "," + boostStats.getEfecto(178) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(225)) + "," + objEquipStats.getEfecto(225) + ","
				+ benMaldStats.getEfecto(225) + "," + boostStats.getEfecto(225) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(226)) + "," + objEquipStats.getEfecto(226) + ","
				+ benMaldStats.getEfecto(226) + "," + boostStats.getEfecto(226) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(220)) + "," + objEquipStats.getEfecto(220) + ","
				+ benMaldStats.getEfecto(220) + "," + boostStats.getEfecto(220) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(115)) + "," + objEquipStats.getEfecto(115) + ","
				+ benMaldStats.getEfecto(115) + "," + boostStats.getEfecto(115) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(122)) + "," + objEquipStats.getEfecto(122) + ","
				+ benMaldStats.getEfecto(122) + "," + boostStats.getEfecto(122) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(160)) + "," + objEquipStats.getEfecto(160) + "," + 0 + ","
				+ benMaldStats.getEfecto(160) + "," + boostStats.getEfecto(160) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(161)) + "," + objEquipStats.getEfecto(161) + "," + 0 + ","
				+ benMaldStats.getEfecto(161) + "," + boostStats.getEfecto(161) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(241)) + "," + objEquipStats.getEfecto(241) + "," + 0 + ","
				+ benMaldStats.getEfecto(241) + "," + boostStats.getEfecto(241) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(214)) + "," + objEquipStats.getEfecto(214) + "," + 0 + ","
				+ benMaldStats.getEfecto(214) + "," + boostStats.getEfecto(214) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(264)) + "," + objEquipStats.getEfecto(264) + "," + 0 + ","
				+ benMaldStats.getEfecto(264) + "," + boostStats.getEfecto(264) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(254)) + "," + objEquipStats.getEfecto(254) + "," + 0 + ","
				+ benMaldStats.getEfecto(254) + "," + boostStats.getEfecto(254) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(242)) + "," + objEquipStats.getEfecto(242) + "," + 0 + ","
				+ benMaldStats.getEfecto(242) + "," + boostStats.getEfecto(242) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(210)) + "," + objEquipStats.getEfecto(210) + "," + 0 + ","
				+ benMaldStats.getEfecto(210) + "," + boostStats.getEfecto(210) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(260)) + "," + objEquipStats.getEfecto(260) + "," + 0 + ","
				+ benMaldStats.getEfecto(260) + "," + boostStats.getEfecto(260) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(250)) + "," + objEquipStats.getEfecto(250) + "," + 0 + ","
				+ benMaldStats.getEfecto(250) + "," + boostStats.getEfecto(250) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(243)) + "," + objEquipStats.getEfecto(243) + "," + 0 + ","
				+ benMaldStats.getEfecto(243) + "," + boostStats.getEfecto(243) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(211)) + "," + objEquipStats.getEfecto(211) + "," + 0 + ","
				+ benMaldStats.getEfecto(211) + "," + boostStats.getEfecto(211) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(261)) + "," + objEquipStats.getEfecto(261) + "," + 0 + ","
				+ benMaldStats.getEfecto(261) + "," + boostStats.getEfecto(261) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(251)) + "," + objEquipStats.getEfecto(251) + "," + 0 + ","
				+ benMaldStats.getEfecto(251) + "," + boostStats.getEfecto(251) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(244)) + "," + objEquipStats.getEfecto(244) + "," + 0 + ","
				+ benMaldStats.getEfecto(244) + "," + boostStats.getEfecto(244) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(212)) + "," + objEquipStats.getEfecto(212) + "," + 0 + ","
				+ benMaldStats.getEfecto(212) + "," + boostStats.getEfecto(212) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(262)) + "," + objEquipStats.getEfecto(262) + "," + 0 + ","
				+ benMaldStats.getEfecto(262) + "," + boostStats.getEfecto(262) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(252)) + "," + objEquipStats.getEfecto(252) + "," + 0 + ","
				+ benMaldStats.getEfecto(252) + "," + boostStats.getEfecto(252) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(240)) + "," + objEquipStats.getEfecto(240) + "," + 0 + ","
				+ benMaldStats.getEfecto(240) + "," + boostStats.getEfecto(240) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(213)) + "," + objEquipStats.getEfecto(213) + "," + 0 + ","
				+ benMaldStats.getEfecto(213) + "," + boostStats.getEfecto(213) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(263)) + "," + objEquipStats.getEfecto(263) + "," + 0 + ","
				+ benMaldStats.getEfecto(263) + "," + boostStats.getEfecto(263) + "|");
		str.append(String.valueOf(_baseStats.getEfecto(253)) + "," + objEquipStats.getEfecto(253) + "," + 0 + ","
				+ benMaldStats.getEfecto(253) + "," + boostStats.getEfecto(253) + "|");
		return str.toString();
	}

	public String xpString(String c) {
		return String.valueOf(_experiencia) + c + Mundo.getExpMinPersonaje(_nivel) + c
				+ Mundo.getExpMaxPersonaje(_nivel);
	}

	public int emoteActivado() {
		return _emoteActivado;
	}

	public void setEmoteActivado(int emoteActivado) {
		_emoteActivado = emoteActivado;
	}

	private Stats getStatsObjEquipados() {
		Stats stats = new Stats(false, null);
		ArrayList<Integer> listaSetsEquipados = new ArrayList<Integer>();
		Collection<Objeto> objetos = _objetos.values();
		for (Objeto objeto : objetos) {
			if (objeto.getPosicion() == -1 || (objeto.getPosicion() < 0 || objeto.getPosicion() > 15)
					&& (objeto.getPosicion() < 20 || objeto.getPosicion() > 27))
				continue;
			stats = Stats.acumularStats(stats, objeto.getStats());
			int setID = objeto.getModelo().getSetID();
			if (setID <= 0 || listaSetsEquipados.contains(setID))
				continue;
			listaSetsEquipados.add(setID);
			Mundo.ObjetoSet IS = Mundo.getObjetoSet(setID);
			if (IS == null)
				continue;
			stats = Stats.acumularStats(stats, IS.getBonusStatPorNroObj(getNroObjEquipadosDeSet(setID)));
		}
		if (_montando && _montura != null) {
			stats = Stats.acumularStats(stats, _montura.getStats());
		}
		return stats;
	}

	private Stats getStatsBoost() {
		Stats stats = new Stats(false, null);
		return stats;
	}

	private Stats getStatsBendMald() {
		Stats stats = new Stats(false, null);
		return stats;
	}

	public Stats getTotalStats() {
		Stats total = new Stats(false, null);
		total = Stats.acumularStats(total, _baseStats);
		total = Stats.acumularStats(total, getStatsObjEquipados());
		total = Stats.acumularStats(total, getStatsBendMald());
		return total;
	}

	public byte getOrientacion() {
		return _orientacion;
	}

	public void setOrientacion(byte orientacion) {
		_orientacion = orientacion;
	}

	public int getIniciativa() {
		Stats objEquipados = getTotalStats();
		int fact = 4;
		int pvmax = _PDVMAX - Constantes.getBasePDV(_clase);
		int pv = _PDV - Constantes.getBasePDV(_clase);
		if (_clase == 11) {
			fact = 8;
		}
		double coef = pvmax / fact;
		coef += (double) getStatsObjEquipados().getEfecto(174);
		coef += (double) objEquipados.getEfecto(119);
		coef += (double) objEquipados.getEfecto(123);
		coef += (double) objEquipados.getEfecto(126);
		coef += (double) objEquipados.getEfecto(118);
		int init = 1;
		if (pvmax != 0) {
			init = (int) (coef * (double) (pv / pvmax));
		}
		if (init < 0) {
			init = 0;
		}
		return init;
	}

	public int getPodUsados() {
		int pod = 0;
		for (Objeto objeto : _objetos.values()) {
			pod += objeto.getModelo().getPeso() * objeto.getCantidad();
		}
		return pod;
	}

	public int getMaxPod() {
		int pods = getTotalStats().getEfecto(158);
		pods += getTotalStats().getEfecto(118) * 5;
		for (Oficio.StatsOficio SO : _statsOficios.values()) {
			pods += SO.getNivel() * 5;
			if (SO.getNivel() != 100)
				continue;
			pods += 1000;
		}
		if (pods < 1000) {
			pods = 1000;
		}
		return pods;
	}

	public int getPDV() {
		return _PDV;
	}

	public void setPDV(int pdv) {
		if (pdv > getPDVMAX()) {
			pdv = getPDVMAX();
		}
		_PDV = pdv;
		actualizarInfoGrupo();
	}

	public int getPDVMAX() {
		if (_encarnacion != null) {
			return _encarnacion.getPDVMAX();
		}
		return _PDVMAX;
	}

	public void setPDVMAX(int pdvmax) {
		_PDVMAX = pdvmax;
		actualizarInfoGrupo();
	}

	public void actualizarInfoGrupo() {
		if (_grupo != null) {
			SocketManager.ENVIAR_PM_ACTUALIZAR_INFO_PJ_GRUPO(_grupo, this);
		}
	}

	public void setSentado(boolean sentado) {
		_sentado = sentado;
		int diferencia = _PDV - _exPdv;
		int tiempo = sentado ? 500 : 1000;
		_exPdv = _PDV;
		if (_enLinea) {
			SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(this, diferencia);
			SocketManager.ENVIAR_ILS_TIEMPO_REGENERAR_VIDA(this, tiempo);
		}
		_recuperarVida.setDelay(tiempo);
		if (!(_emoteActivado != 1 && _emoteActivado != 19 || sentado)) {
			_emoteActivado = 0;
		}
	}

	public void stopRecuperarVida() {
		_recuperarVida.stop();
	}

	public byte getAlineacion() {
		return _alineacion;
	}

	public int getPorcPDV() {
		int porcPDV = 100;
		porcPDV = 100 * _PDV / _PDVMAX;
		if (porcPDV > 100) {
			return 100;
		}
		return porcPDV;
	}

	public void emote(String str) {
		try {
			int id = Integer.parseInt(str);
			Mapa mapa = _mapa;
			if (_pelea == null) {
				SocketManager.ENVIAR_cS_EMOTICON_MAPA(mapa, _ID, id);
			} else {
				SocketManager.ENVIAR_cS_EMOTE_EN_PELEA(_pelea, 7, _ID, id);
			}
		} catch (NumberFormatException e) {
			return;
		}
	}

	public void retornoMapa() {
		_pelea = null;
		_ocupado = false;
		_listo = false;
		_dueloID = -1;
		_mapa.addJugador(this);
	}

	public void retornoMapaDesPeleaRecau() {
		_pelea = null;
		_ocupado = false;
		_listo = false;
		_dueloID = -1;
		try {
			teleport(_tempMapaDefPerco.getID(), _tempCeldaDefPerco.getID());
		} catch (NullPointerException E) {
			teleport(_mapa.getID(), _celda.getID());
		}
		_tempMapaDefPerco = null;
		_tempCeldaDefPerco = null;
	}

	public void retornoPtoSalvadaRecau() {
		_pelea = null;
		_ocupado = false;
		_listo = false;
		_dueloID = -1;
		if (_energia > 0) {
			String[] infos = _puntoSalvado.split(",");
			teleport(Short.parseShort(infos[0]), Short.parseShort(infos[1]));
		}
		_tempMapaDefPerco = null;
		_tempCeldaDefPerco = null;
		try {
			Thread.sleep(1000L);
		} catch (Exception exception) {
			// empty catch block
		}
		SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(this);
	}

	public void retornoPtoSalvada(boolean teleportar) {
		_pelea = null;
		_ocupado = false;
		_dueloID = -1;
		if (_energia > 0 && teleportar) {
			String[] infos = _puntoSalvado.split(",");
			teleport(Short.parseShort(infos[0]), Short.parseShort(infos[1]));
		}
		try {
			Thread.sleep(1000L);
		} catch (Exception exception) {
			// empty catch block
		}
		SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(this);
	}

	public void retornoPtoSalvadaPocima() {
		_pelea = null;
		_ocupado = false;
		_listo = false;
		_dueloID = -1;
		try {
			String[] infos = _puntoSalvado.split(",");
			teleport(Short.parseShort(infos[0]), Short.parseShort(infos[1]));
		} catch (Exception exception) {
			// empty catch block
		}
	}

	public void boostStat(int stat) {
		int valor = 0;
		switch (stat) {
		case 10: {
			valor = _baseStats.getEfecto(118);
			break;
		}
		case 13: {
			valor = _baseStats.getEfecto(123);
			break;
		}
		case 14: {
			valor = _baseStats.getEfecto(119);
			break;
		}
		case 15: {
			valor = _baseStats.getEfecto(126);
			break;
		}
		}
		int cantidad = Constantes.getRepartoPuntoSegunClase(_clase, stat, valor);
		if (cantidad <= _capital) {
			switch (stat) {
			case 11: {
				if (_clase != 11) {
					_baseStats.addStat(125, 1);
					break;
				}
				_baseStats.addStat(125, 2);
				break;
			}
			case 12: {
				_baseStats.addStat(124, 1);
				break;
			}
			case 10: {
				_baseStats.addStat(118, 1);
				break;
			}
			case 13: {
				_baseStats.addStat(123, 1);
				break;
			}
			case 14: {
				_baseStats.addStat(119, 1);
				break;
			}
			case 15: {
				_baseStats.addStat(126, 1);
				break;
			}
			default: {
				return;
			}
			}
			_capital -= cantidad;
			SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		}
	}

	public void boostStat2(int stat) {
		int valor = 0;
		int capital = _capital;
		int cantidad = 0;
		while (capital >= cantidad) {
			switch (stat) {
			case 10: {
				valor = _baseStats.getEfecto(118);
				break;
			}
			case 13: {
				valor = _baseStats.getEfecto(123);
				break;
			}
			case 14: {
				valor = _baseStats.getEfecto(119);
				break;
			}
			case 15: {
				valor = _baseStats.getEfecto(126);
				break;
			}
			}
			cantidad = Constantes.getRepartoPuntoSegunClase(_clase, stat, valor);
			if (cantidad > _capital)
				continue;
			switch (stat) {
			case 11: {
				if (_clase != 11) {
					_baseStats.addStat(125, 1);
					break;
				}
				_baseStats.addStat(125, 2);
				break;
			}
			case 12: {
				_baseStats.addStat(124, 1);
				break;
			}
			case 10: {
				_baseStats.addStat(118, 1);
				break;
			}
			case 13: {
				_baseStats.addStat(123, 1);
				break;
			}
			case 14: {
				_baseStats.addStat(119, 1);
				break;
			}
			case 15: {
				_baseStats.addStat(126, 1);
				break;
			}
			default: {
				return;
			}
			}
			capital -= cantidad;
		}
		_capital = capital;
		SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
	}

	public void boostStatFixedCount(int stat, int countVal) {
		if (LesGuardians.MOSTRAR_ENVIOS_STD) {
			System.out.println("Perso " + _nombre + ": tentative de boost stat " + stat + " " + countVal + " fois");
		}
		for (int i = 0; i < countVal; ++i) {
			int value = 0;
			switch (stat) {
			case 10: {
				value = _baseStats.getEfecto(118);
				break;
			}
			case 13: {
				value = _baseStats.getEfecto(123);
				break;
			}
			case 14: {
				value = _baseStats.getEfecto(119);
				break;
			}
			case 15: {
				value = _baseStats.getEfecto(126);
			}
			}
			int cout = Constantes.getRepartoPuntoSegunClase(_clase, stat, value);
			if (cout > _capital)
				continue;
			switch (stat) {
			case 11: {
				if (_clase != 11) {
					_baseStats.addStat(125, 1);
					break;
				}
				_baseStats.addStat(125, 2);
				break;
			}
			case 12: {
				_baseStats.addStat(124, 1);
				break;
			}
			case 10: {
				_baseStats.addStat(118, 1);
				break;
			}
			case 13: {
				_baseStats.addStat(123, 1);
				break;
			}
			case 14: {
				_baseStats.addStat(119, 1);
				break;
			}
			case 15: {
				_baseStats.addStat(126, 1);
				break;
			}
			default: {
				return;
			}
			}
			_capital -= cout;
		}
		SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		SQLManager.SALVAR_PERSONAJE(this, true);
	}

	public boolean estaMuteado() {
		return _cuenta.estaMuteado();
	}

	public void setMapa(Mapa mapa) {
		_mapa = mapa;
	}

	public String stringObjetosABD() {
		StringBuffer str = new StringBuffer();
		for (Map.Entry<Integer, Objeto> entry : _objetos.entrySet()) {
			Objeto obj = entry.getValue();
			str.append(String.valueOf(obj.getID()) + "|");
		}
		return str.toString();
	}

	public Objeto getObjSimilarInventario(Objeto objeto) {
		Objeto.ObjetoModelo objModelo = objeto.getModelo();
		if (objModelo.getTipo() == 85 || objModelo.getTipo() == 18
				|| LesGuardians.ARMAS_ENCARNACAO.contains(objModelo.getID())) {
			return null;
		}
		for (Map.Entry<Integer, Objeto> entry : _objetos.entrySet()) {
			Objeto obj = entry.getValue();
			if (obj.getPosicion() != -1 || objeto.getID() == obj.getID() || obj.getModelo().getID() != objModelo.getID()
					|| !obj.getStats().sonStatsIguales(objeto.getStats()))
				continue;
			return obj;
		}
		return null;
	}

	public boolean addObjetoSimilar(Objeto objeto, boolean tieneSimilar, int idAntigua) {
		Objeto.ObjetoModelo objModelo = objeto.getModelo();
		if (objModelo.getTipo() == 85 || objModelo.getTipo() == 18
				|| LesGuardians.ARMAS_ENCARNACAO.contains(objModelo.getID())) {
			return false;
		}
		if (tieneSimilar) {
			for (Map.Entry<Integer, Objeto> entry : _objetos.entrySet()) {
				Objeto obj = entry.getValue();
				if (obj.getPosicion() != -1 || obj.getID() == idAntigua || obj.getModelo().getID() != objModelo.getID()
						|| !obj.getStats().sonStatsIguales(objeto.getStats()))
					continue;
				obj.setCantidad(obj.getCantidad() + objeto.getCantidad());
				SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, obj);
				return true;
			}
		}
		return false;
	}

	public void addObjetoPut(Objeto objeto) {
		if (objeto == null) {
			return;
		}
		_objetos.put(objeto.getID(), objeto);
	}

	public Map<Integer, Objeto> getObjetos() {
		return _objetos;
	}

	public String stringPersonajeElegido() {
		StringBuffer str = new StringBuffer();
		Objeto objeto = getObjPosicion(21);
		if (getObjPosicion(21) != null) {
			String[] arg;
			String stats = objeto.convertirStatsAString();
			String[] arrstring = arg = stats.split(",");
			int n = arg.length;
			for (int i = 0; i < n; ++i) {
				String efec = arrstring[i];
				String[] val = efec.split("#");
				int efecto = Integer.parseInt(val[0], 16);
				if (efecto < 281 && efecto > 292)
					continue;
				_bendEfecto = efecto;
				_bendHechizo = Integer.parseInt(val[1], 16);
				_bendModif = Integer.parseInt(val[3], 16);
			}
		}
		for (Objeto obj : _objetos.values()) {
			str.append(obj.stringObjetoConGui\u00f1o());
		}
		return str.toString();
	}

	public String getObjetosBancoPorID(String splitter) {
		StringBuffer str = new StringBuffer();
		for (int entry : _cuenta.getObjetosBanco().keySet()) {
			if (str.length() != 0) {
				str.append(splitter);
			}
			str.append(entry);
		}
		return str.toString();
	}

	public String getObjetosPersonajePorID(String splitter) {
		StringBuffer str = new StringBuffer();
		for (int entry : _objetos.keySet()) {
			if (str.length() != 0) {
				str.append(splitter);
			}
			str.append(entry);
		}
		return str.toString();
	}

	public boolean tieneObjetoID(int id) {
		return _objetos.get(id) != null;
	}

	public void venderObjeto(int id, int cant) {
		if (cant <= 0) {
			return;
		}
		Objeto objeto = _objetos.get(id);
		Objeto.ObjetoModelo objModelo = objeto.getModelo();
		int precioUnitario = objModelo.getPrecio();
		int precioVIP = objModelo.getPrecioVIP();
		if (precioUnitario == 0 && precioVIP > 0) {
			int ptosAconseguir = cant * precioVIP;
			int misPuntos = SQLManager.getPuntosCuenta(_cuenta.getID());
			SQLManager.setPuntoCuenta(ptosAconseguir + misPuntos, _cuenta.getID());
			int nuevaCant = objeto.getCantidad() - cant;
			if (nuevaCant <= 0) {
				_objetos.remove(id);
				Mundo.eliminarObjeto(id);
				SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, id);
			} else {
				objeto.setCantidad(nuevaCant);
				SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objeto);
			}
		} else {
			if (objeto.getCantidad() < cant) {
				cant = objeto.getCantidad();
			}
			int precio = cant * (precioUnitario / 10);
			int nuevaCant = objeto.getCantidad() - cant;
			if (nuevaCant <= 0) {
				_objetos.remove(id);
				Mundo.eliminarObjeto(id);
				SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, id);
			} else {
				objeto.setCantidad(nuevaCant);
				SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objeto);
			}
			_kamas += (long) precio;
		}
		SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this);
		SocketManager.ENVIAR_ESK_VENDIDO(this);
	}

	public void borrarObjetoSinEliminar(int id) {
		_objetos.remove(id);
	}

	public void borrarObjetoEliminar(int idObjeto, int cantidad, boolean borrarMundoDofus) {
		Objeto obj = _objetos.get(idObjeto);
		if (cantidad > obj.getCantidad()) {
			cantidad = obj.getCantidad();
		}
		if (obj.getCantidad() >= cantidad) {
			int nuevaCant = obj.getCantidad() - cantidad;
			if (nuevaCant > 0) {
				obj.setCantidad(nuevaCant);
				if (_enLinea) {
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, obj);
				}
			} else {
				_objetos.remove(obj.getID());
				if (borrarMundoDofus) {
					Mundo.eliminarObjeto(obj.getID());
				}
				if (_enLinea) {
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, obj.getID());
				}
			}
		}
	}

	public Objeto getObjPosicion(int pos) {
		if (pos == -1) {
			return null;
		}
		for (Objeto objeto : _objetos.values()) {
			if (objeto.getPosicion() != pos)
				continue;
			return objeto;
		}
		return null;
	}

	public void refrescarVida() {
		double actPdvPorc = 100.0 * (double) _PDV / (double) _PDVMAX;
		_PDVMAX = _encarnacion != null ? _encarnacion.getPDVMAX()
				: (_nivel - 1) * 5 + (_nivel > 200 ? (_nivel - 200) * (_clase == 11 ? 2 : 1) * 5 : 0)
						+ Constantes.getBasePDV(_clase) + getTotalStats().getEfecto(125);
		_PDV = (int) Math.round((double) _PDVMAX * actPdvPorc / 100.0);
	}

	public void subirNivel(boolean addXp) {
		if (_nivel == LesGuardians.MAX_NIVEL || _encarnacion != null) {
			return;
		}
		++_nivel;
		_capital += 5;
		_PDVMAX += 5;
		if (_nivel > 200) {
			_PDVMAX += 5;
		}
		++_puntosHechizo;
		if (_nivel == LesGuardians.NIVEL_PA1) {
			_baseStats.addStat(111, LesGuardians.MAX_PA1);
		}
		Constantes.subirNivelAprenderHechizos(this, _nivel);
		if (addXp) {
			_experiencia = Mundo.getExpNivel((int) _nivel)._personaje;
		}
		_PDV = _PDVMAX;
	}

	public void addExp(long xp) {
		if (_encarnacion != null) {
			_encarnacion.addExp(xp, this);
			return;
		}
		_experiencia += xp;
		int exNivel = _nivel;
		while (_experiencia >= Mundo.getExpMaxPersonaje(_nivel) && _nivel < LesGuardians.MAX_NIVEL) {
			subirNivel(false);
		}
		if (_enLinea) {
			if (exNivel < _nivel) {
				SocketManager.ENVIAR_AN_MENSAJE_NUEVO_NIVEL(this, _nivel);
				try {
					if (getGremio() != null) {
						getMiembroGremio().setNivel(_nivel);
					}
					SocketManager.ENVIAR_SL_LISTA_HECHIZOS(this);
					actualizarInfoGrupo();
				} catch (NullPointerException nullPointerException) {
					// empty catch block
				}
			}
			SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		}
	}

	public void setIntercambio(Mundo.Intercambio inter) {
		_intercambio = inter;
	}

	public Mundo.Intercambio getIntercambio() {
		return _intercambio;
	}

	public void setTallerInvitado(Mundo.InvitarTaller inter) {
		_tallerInvitado = inter;
	}

	public Mundo.InvitarTaller getTallerInvitado() {
		return _tallerInvitado;
	}

	public int aprenderOficio(Oficio oficio) {
		for (Map.Entry<Integer, Oficio.StatsOficio> entry : _statsOficios.entrySet()) {
			if (entry.getValue().getOficio().getID() != oficio.getID())
				continue;
			return -1;
		}
		int cantOficios = _statsOficios.size();
		if (cantOficios == 6) {
			return -1;
		}
		int pos = -1;
		if (_statsOficios.get(0) == null) {
			pos = 0;
		} else if (_statsOficios.get(1) == null) {
			pos = 1;
		} else if (_statsOficios.get(2) == null) {
			pos = 2;
		} else if (_statsOficios.get(3) == null) {
			pos = 3;
		} else if (_statsOficios.get(4) == null) {
			pos = 4;
		} else if (_statsOficios.get(5) == null) {
			pos = 5;
		}
		if (pos == -1) {
			return -1;
		}
		Oficio.StatsOficio statOficio = new Oficio.StatsOficio(pos, oficio, 1, 0L);
		_statsOficios.put(pos, statOficio);
		if (_enLinea) {
			ArrayList<Oficio.StatsOficio> list = new ArrayList<Oficio.StatsOficio>();
			list.add(statOficio);
			SocketManager.ENVIAR_Im_INFORMACION(this, "02;" + oficio.getID());
			SocketManager.ENVIAR_JS_TRABAJO_POR_OFICIO(this, list);
			SocketManager.ENVIAR_JX_EXPERINENCIA_OFICIO(this, list);
			SocketManager.ENVIAR_JO_OFICIO_OPCIONES(this, list);
			Objeto obj = getObjPosicion(1);
			if (obj != null && oficio.herramientaValida(obj.getModelo().getID())) {
				PrintWriter out = _cuenta.getEntradaPersonaje().getOut();
				SocketManager.ENVIAR_OT_OBJETO_HERRAMIENTA(out, oficio.getID());
				String strOficioPub = Constantes.trabajosOficioTaller(oficio.getID());
				if (_mapa.esTaller() && _oficioPublico) {
					SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(out, "+", _ID, strOficioPub);
				}
				_stringOficiosPublicos = strOficioPub;
			}
		}
		return pos;
	}

	public void olvidarOficio(int pos) {
		_statsOficios.remove(pos);
	}

	public boolean tieneEquipado(int id) {
		for (Objeto objeto : _objetos.values()) {
			if (objeto.getModelo().getID() != id || objeto.getPosicion() == -1)
				continue;
			return true;
		}
		return false;
	}

	private boolean tieneObjSetVampirico() {
		for (Objeto objeto : _objetos.values()) {
			int idObjModelo;
			if (objeto.getPosicion() == -1 || (idObjModelo = objeto.getModelo().getID()) != 10054
					&& idObjModelo != 10055 && idObjModelo != 10056 && idObjModelo != 10058 && idObjModelo != 10061
					&& idObjModelo != 10102)
				continue;
			return true;
		}
		return false;
	}

	public void setInvitado(int invitando) {
		_invitando = invitando;
	}

	public int getInvitado() {
		return _invitando;
	}

	public String stringInfoGrupo() {
		StringBuffer str = new StringBuffer();
		str.append(String.valueOf(_ID) + ";");
		str.append(String.valueOf(_nombre) + ";");
		str.append(String.valueOf(_gfxID) + ";");
		str.append(String.valueOf(_color1) + ";");
		str.append(String.valueOf(_color2) + ";");
		str.append(String.valueOf(_color3) + ";");
		str.append(String.valueOf(getStringAccesorios()) + ";");
		str.append(String.valueOf(_PDV) + "," + _PDVMAX + ";");
		str.append(String.valueOf(_nivel) + ";");
		str.append(String.valueOf(getIniciativa()) + ";");
		str.append(String.valueOf(getTotalStats().getEfecto(176)) + ";");
		str.append("1");
		return str.toString();
	}

	public int getNroObjEquipadosDeSet(int setID) {
		int nro = 0;
		for (Objeto objeto : _objetos.values()) {
			if (objeto.getPosicion() == -1 || objeto.getModelo().getSetID() != setID)
				continue;
			++nro;
		}
		return nro;
	}

	public void iniciarAccionEnCelda(GameThread.AccionDeJuego GA) {
		short celdaID = -1;
		int accion = -1;
		try {
			celdaID = Short.parseShort(GA._args.split(";")[0]);
			accion = Integer.parseInt(GA._args.split(";")[1]);
		} catch (Exception exception) {
			// empty catch block
		}
		if (celdaID == -1 || accion == -1) {
			return;
		}
		if (!_mapa.getCelda(celdaID).puedeHacerAccion(accion, _pescarKuakua)) {
			return;
		}
		_mapa.getCelda(celdaID).iniciarAccion(this, GA);
	}

	public void finalizarAccionEnCelda(GameThread.AccionDeJuego AJ) {
		short celdaID = -1;
		try {
			celdaID = Short.parseShort(AJ._args.split(";")[0]);
		} catch (Exception exception) {
			// empty catch block
		}
		if (celdaID == -1 || AJ == null || _mapa.getCelda(celdaID) == null) {
			return;
		}
		_mapa.getCelda(celdaID).finalizarAccion(this, AJ);
	}

	public void teleport(short nuevoMapaID, short nuevaCeldaID) {
		Mapa nuevoMapa;
		if (_tutorial != null || _accionTrabajo != null) {
			return;
		}
		if (!_huir) {
			if (System.currentTimeMillis() - _tiempoAgre > 8000L) {
				_huir = true;
			} else {
				SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this,
						"Voc\u00ea n\u00e3o pode se teleportar, espere 10 segundos.");
				return;
			}
		}
		if ((nuevoMapa = Mundo.getMapa(nuevoMapaID)) == null || nuevoMapa.getCelda(nuevaCeldaID) == null) {
			return;
		}
		if (nuevoMapa.esTaller() && nuevoMapa.getPersos().size() > LesGuardians.LIMITE_ARTESAOS_OFIC) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this,
					"Voc\u00ea n\u00e3o pode entrar na loja, porque est\u00e1 cheia. Por Favor volte mais tarde ou tente outra loja.");
			nuevoMapaID = (short) 951;
			nuevaCeldaID = (short) 340;
			nuevoMapa = Mundo.getMapa(nuevoMapaID);
		}
		PrintWriter out = null;
		if (_cuenta.getEntradaPersonaje() != null) {
			out = _cuenta.getEntradaPersonaje().getOut();
		}
		if (out != null) {
			SocketManager.ENVIAR_GA2_CARGANDO_MAPA(out, _ID);
		}
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_mapa, _ID);
		_celda.removerPersonaje(_ID);
		_mapa = nuevoMapa;
		_celda = _mapa.getCelda(nuevaCeldaID);
		if (out != null) {
			SocketManager.ENVIAR_GDM_CAMBIO_DE_MAPA(out, nuevoMapaID, _mapa.getFecha(), _mapa.getCodigo());
		}
		_mapa.addJugador(this);
		if (!_seguidores.isEmpty()) {
			ArrayList<Personaje> seguidores = new ArrayList<Personaje>();
			try {
				seguidores.addAll(_seguidores.values());
			} catch (ConcurrentModificationException concurrentModificationException) {
				// empty catch block
			}
			for (Personaje seguido : seguidores) {
				if (seguido._enLinea) {
					SocketManager.ENVIAR_IC_PERSONAJE_BANDERA_COMPAS(seguido, this);
					continue;
				}
				_seguidores.remove(seguido.getID());
			}
		}
	}

	public boolean getDefendiendo() {
		return _defendiendo;
	}

	public void setDefendiendo(boolean def) {
		_defendiendo = def;
	}

	public boolean teleportSinTodos(short nuevoMapaID, short nuevaCeldaID) {
		Mapa nuevoMapa;
		PrintWriter out = null;
		_defendiendo = true;
		if (_cuenta.getEntradaPersonaje() != null) {
			out = _cuenta.getEntradaPersonaje().getOut();
		}
		if ((nuevoMapa = Mundo.getMapa(nuevoMapaID)) == null || nuevoMapa.getCelda(nuevaCeldaID) == null
				|| _celda == null) {
			return false;
		}
		if (out != null) {
			SocketManager.ENVIAR_GA2_CARGANDO_MAPA(out, _ID);
		}
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_mapa, _ID);
		_celda.removerPersonaje(_ID);
		_mapa = nuevoMapa;
		_celda = _mapa.getCelda(nuevaCeldaID);
		if (out != null) {
			SocketManager.ENVIAR_GDM_CAMBIO_DE_MAPA(out, nuevoMapaID, _mapa.getFecha(), _mapa.getCodigo());
		}
		return true;
	}

	public void setHuir(boolean huir) {
		_huir = huir;
	}

	public boolean getHuir() {
		return _huir;
	}

	public long getTiempoAgre() {
		return _tiempoAgre;
	}

	public void setTiempoAgre(long tiempo) {
		_tiempoAgre = tiempo;
	}

	public void setAgresion(boolean agre) {
		_agresion = agre;
	}

	public boolean getAgresion() {
		return _agresion;
	}

	public int getCostoAbrirBanco() {
		return _cuenta.getObjetosBanco().size();
	}

	public String getStringVar(String str) {
		if (str.equals("nombre")) {
			return _nombre;
		}
		if (str.equals("costoBanco")) {
			return String.valueOf(getCostoAbrirBanco());
		}
		return "";
	}

	public void setKamasBanco(long i) {
		_cuenta.setKamasBanco(i);
	}

	public long getKamasBanco() {
		return _cuenta.getKamasBanco();
	}

	public void setEnBanco(boolean b) {
		_estarBanco = b;
	}

	public boolean enBanco() {
		return _estarBanco;
	}

	public String stringBanco() {
		String packet = "";
		for (Map.Entry<Integer, Objeto> entry : _cuenta.getObjetosBanco().entrySet()) {
			packet = String.valueOf(packet) + "O" + entry.getValue().stringObjetoConGui\u00f1o();
		}
		if (getKamasBanco() != 0L) {
			packet = String.valueOf(packet) + "G" + getKamasBanco();
		}
		return packet;
	}

	public void addCapital(int pts) {
		_capital += pts;
	}

	public void setCapital(int capital) {
		_capital = capital;
	}

	public void addPuntosHechizos(int puntos) {
		_puntosHechizo += puntos;
	}

	public void addObjAlBanco(int id, int cant) {
		Objeto objAGuardar = Mundo.getObjeto(id);
		if (_objetos.get(id) == null || objAGuardar.getPosicion() != -1) {
			return;
		}
		Objeto objBanco = getSimilarObjetoBanco(objAGuardar);
		int nuevaCant = objAGuardar.getCantidad() - cant;
		if (objBanco == null) {
			if (nuevaCant <= 0) {
				borrarObjetoSinEliminar(objAGuardar.getID());
				_cuenta.getObjetosBanco().put(objAGuardar.getID(), objAGuardar);
				String str = "O+" + objAGuardar.getID() + "|" + objAGuardar.getCantidad() + "|"
						+ objAGuardar.getModelo().getID() + "|" + objAGuardar.convertirStatsAString();
				SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
				SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, id);
			} else {
				objAGuardar.setCantidad(nuevaCant);
				objBanco = Objeto.clonarObjeto(objAGuardar, cant);
				Mundo.addObjeto(objBanco, true);
				_cuenta.getObjetosBanco().put(objBanco.getID(), objBanco);
				String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID()
						+ "|" + objBanco.convertirStatsAString();
				SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
				SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objAGuardar);
			}
		} else if (nuevaCant <= 0) {
			borrarObjetoSinEliminar(objAGuardar.getID());
			objBanco.setCantidad(objBanco.getCantidad() + objAGuardar.getCantidad());
			String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID()
					+ "|" + objBanco.convertirStatsAString();
			Mundo.eliminarObjeto(objAGuardar.getID());
			SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
			SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, id);
		} else {
			objAGuardar.setCantidad(nuevaCant);
			objBanco.setCantidad(objBanco.getCantidad() + cant);
			String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID()
					+ "|" + objBanco.convertirStatsAString();
			SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
			SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objAGuardar);
		}
		SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this);
	}

	private Objeto getSimilarObjetoBanco(Objeto obj) {
		for (Objeto objeto : _cuenta.getObjetosBanco().values()) {
			Objeto.ObjetoModelo objetoMod = objeto.getModelo();
			if (objetoMod.getTipo() == 85 || objetoMod.getID() != obj.getModelo().getID()
					|| !objeto.getStats().sonStatsIguales(obj.getStats()))
				continue;
			return objeto;
		}
		return null;
	}

	public void removerDelBanco(int id, int cant) {
		Objeto objBanco = Mundo.getObjeto(id);
		if (_cuenta.getObjetosBanco().get(id) == null) {
			return;
		}
		Objeto objetoARecibir = getObjSimilarInventario(objBanco);
		int nuevaCant = objBanco.getCantidad() - cant;
		if (objetoARecibir == null) {
			if (nuevaCant <= 0) {
				_cuenta.getObjetosBanco().remove(id);
				_objetos.put(id, objBanco);
				SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, "O-" + id);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this, objBanco);
			} else {
				objetoARecibir = Objeto.clonarObjeto(objBanco, cant);
				objBanco.setCantidad(nuevaCant);
				Mundo.addObjeto(objetoARecibir, true);
				_objetos.put(objetoARecibir.getID(), objetoARecibir);
				String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID()
						+ "|" + objBanco.convertirStatsAString();
				SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this, objetoARecibir);
			}
		} else if (nuevaCant <= 0) {
			_cuenta.getObjetosBanco().remove(objBanco.getID());
			objetoARecibir.setCantidad(objetoARecibir.getCantidad() + objBanco.getCantidad());
			SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objetoARecibir);
			Mundo.eliminarObjeto(objBanco.getID());
			SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, "O-" + id);
		} else {
			objBanco.setCantidad(nuevaCant);
			objetoARecibir.setCantidad(objetoARecibir.getCantidad() + cant);
			SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objetoARecibir);
			String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID()
					+ "|" + objBanco.convertirStatsAString();
			SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
		}
		SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this);
	}

	public void abrirCercado() {
		if (getDeshonor() >= 5) {
			SocketManager.ENVIAR_Im_INFORMACION(this, "183");
			return;
		}
		_enCercado = _mapa.getCercado();
		_ocupado = true;
		String str = analizarListaDrago();
		SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(this, 16, str);
	}

	private String analizarListaDrago() { // FIXME revisar buen funcionamiento
		StringBuffer packet = new StringBuffer();
		boolean primero = false;
		if (_cuenta.getEstablo().size() > 0) {
			for (Dragopavo DP : _cuenta.getEstablo()) {
				if (primero) {
					packet.append(";");
				}
				packet.append(DP.detallesMontura());
				primero = true;
			}
		}
		packet.append("~");
		if (_enCercado.getListaCriando().size() > 0) {
			primero = false;
			Iterator<Integer> iterator = _enCercado.getListaCriando().iterator();
			while (iterator.hasNext()) {
				int pavo = (Integer) iterator.next();
				Dragopavo dragopavo = Mundo.getDragopavoPorID(pavo);
				if (dragopavo.getDue\u00f1o() == _ID) {
					if (primero) {
						packet.append(";");
					}
					packet.append(dragopavo.detallesMontura());
					primero = true;
					continue;
				}
				if (getMiembroGremio() == null || !getMiembroGremio().puede(Constantes.G_OTRASMONTURAS)
						|| _enCercado.getDue\u00f1o() == -1)
					continue;
				if (primero) {
					packet.append(";");
				}
				packet.append(dragopavo.detallesMontura());
				primero = true;
			}
		}
		return packet.toString();
	}

	public void salirDeCercado() {
		_enCercado = null;
	}

	public Mapa.Cercado getEnCercado() {
		return _enCercado;
	}

	public void fullPDV() {
		_PDV = _PDVMAX;
	}

	public void removerObjetoPorModYCant(int objModeloID, int cantidad) {
		ArrayList<Objeto> lista = new ArrayList<Objeto>();
		lista.addAll(_objetos.values());
		ArrayList<Objeto> listaObjBorrar = new ArrayList<Objeto>();
		int cantTemp = cantidad;
		for (Objeto obj : lista) {
			int nuevaCant;
			if (obj.getModelo().getID() != objModeloID)
				continue;
			if (obj.getCantidad() >= cantidad) {
				nuevaCant = obj.getCantidad() - cantidad;
				if (nuevaCant > 0) {
					obj.setCantidad(nuevaCant);
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, obj);
				} else {
					_objetos.remove(obj.getID());
					Mundo.eliminarObjeto(obj.getID());
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, obj.getID());
				}
				return;
			}
			if (obj.getCantidad() >= cantTemp) {
				nuevaCant = obj.getCantidad() - cantTemp;
				if (nuevaCant > 0) {
					obj.setCantidad(nuevaCant);
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, obj);
				} else {
					listaObjBorrar.add(obj);
				}
				for (Objeto objBorrar : listaObjBorrar) {
					_objetos.remove(objBorrar.getID());
					Mundo.eliminarObjeto(objBorrar.getID());
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, objBorrar.getID());
				}
				continue;
			}
			cantTemp -= obj.getCantidad();
			listaObjBorrar.add(obj);
		}
	}

	public ArrayList<Objeto> getObjetosEquipados() {
		ArrayList<Objeto> objetos = new ArrayList<Objeto>();
		for (Objeto objeto : _objetos.values()) {
			if (objeto.getPosicion() == -1 || objeto.getPosicion() > 15)
				continue;
			objetos.add(objeto);
		}
		return objetos;
	}

	public void eliminarObjetoPorModelo(int objModeloID) {
		ArrayList<Objeto> lista = new ArrayList<Objeto>();
		lista.addAll(_objetos.values());
		for (Objeto obj : lista) {
			if (obj.getModelo().getID() != objModeloID)
				continue;
			_objetos.remove(obj.getID());
			Mundo.eliminarObjeto(obj.getID());
			SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, obj.getID());
		}
	}

	public Map<Integer, Oficio.StatsOficio> getStatsOficios() {
		return _statsOficios;
	}

	public void iniciarAccionOficio(int idTrabajo, Mapa.ObjetoInteractivo objInterac, GameThread.AccionDeJuego GA,
			Mapa.Celda celda) {
		Oficio.StatsOficio SO = getOficioPorTrabajo(idTrabajo);
		if (SO == null) {
			return;
		}
		SO.iniciarTrabajo(idTrabajo, this, objInterac, GA, celda);
	}

	public void finalizarAccionOficio(int idTrabajo, GameThread.AccionDeJuego GA, Mapa.Celda celda) {
		Oficio.StatsOficio SO = getOficioPorTrabajo(idTrabajo);
		if (SO == null) {
			return;
		}
		SO.finalizarTrabajo(idTrabajo, this, GA, celda);
	}

	public String stringOficios() {
		StringBuffer str = new StringBuffer();
		for (Oficio.StatsOficio SO : _statsOficios.values()) {
			if (str.length() > 0) {
				str.append(";");
			}
			str.append(String.valueOf(SO.getOficio().getID()) + "," + SO.getXP());
		}
		return str.toString();
	}

	public int totalOficiosBasicos() {
		int i = 0;
		for (Oficio.StatsOficio SO : _statsOficios.values()) {
			int idOficio = SO.getOficio().getID();
			if (idOficio != 2 && idOficio != 11 && idOficio != 13 && idOficio != 14 && idOficio != 15 && idOficio != 16
					&& idOficio != 17 && idOficio != 18 && idOficio != 19 && idOficio != 20 && idOficio != 24
					&& idOficio != 25 && idOficio != 26 && idOficio != 27 && idOficio != 28 && idOficio != 31
					&& idOficio != 36 && idOficio != 41 && idOficio != 56 && idOficio != 58 && idOficio != 60
					&& idOficio != 65)
				continue;
			++i;
		}
		return i;
	}

	public int totalOficiosFM() {
		int i = 0;
		for (Oficio.StatsOficio SO : _statsOficios.values()) {
			int idOficio = SO.getOficio().getID();
			if (idOficio != 43 && idOficio != 44 && idOficio != 45 && idOficio != 46 && idOficio != 47 && idOficio != 48
					&& idOficio != 49 && idOficio != 50 && idOficio != 62 && idOficio != 63 && idOficio != 64)
				continue;
			++i;
		}
		return i;
	}

	public void setHaciendoTrabajo(Oficio.AccionTrabajo AT) {
		_accionTrabajo = AT;
	}

	public Oficio.AccionTrabajo getHaciendoTrabajo() {
		return _accionTrabajo;
	}

	public Oficio.StatsOficio getOficioPorTrabajo(int trabajoID) {
		for (Oficio.StatsOficio SO : _statsOficios.values()) {
			if (!SO.esValidoTrabajo(trabajoID))
				continue;
			return SO;
		}
		return null;
	}

	public String analizarListaAmigos(int id) {
		StringBuffer str = new StringBuffer(";");
		str.append("?;");
		str.append(String.valueOf(_nombre) + ";");
		if (_cuenta.esAmigo(id)) {
			str.append(String.valueOf(_nivel) + ";");
			str.append(String.valueOf(_alineacion) + ";");
		} else {
			str.append("?;");
			str.append("-1;");
		}
		str.append(String.valueOf(_clase) + ";");
		str.append(String.valueOf(_sexo) + ";");
		str.append(_gfxID);
		return str.toString();
	}

	public String analizarListaEnemigos(int id) {
		StringBuffer str = new StringBuffer(";");
		str.append("?;");
		str.append(String.valueOf(_nombre) + ";");
		if (_cuenta.esEnemigo(id)) {
			str.append(String.valueOf(_nivel) + ";");
			str.append(String.valueOf(_alineacion) + ";");
		} else {
			str.append("?;");
			str.append("-1;");
		}
		str.append(String.valueOf(_clase) + ";");
		str.append(String.valueOf(_sexo) + ";");
		str.append(_gfxID);
		return str.toString();
	}

	public Oficio.StatsOficio getOficioPorID(int oficio) {
		for (Oficio.StatsOficio SO : _statsOficios.values()) {
			if (SO.getOficio().getID() != oficio)
				continue;
			return SO;
		}
		return null;
	}

	public boolean estaMontando() {
		return _montando;
	}

	public void bajarMontura() {
		_montando = !_montando;
		SocketManager.ENVIAR_Rr_ESTADO_MONTADO(this, _montando ? "+" : "-");
	}

	public void subirBajarMontura() {
		if (_encarnacion != null) {
			SocketManager.ENVIAR_Im_INFORMACION(this, "134|44");
			return;
		}
		if (_montura.getEnergia() <= 0) {
			SocketManager.ENVIAR_Im_INFORMACION(this, "1113");
			return;
		}
		_montando = !_montando;
		Objeto mascota = getObjPosicion(8);
		if (_montando && mascota != null) {
			mascota.setPosicion(-1);
			_mascota = null;
			SocketManager.ENVIAR_OM_MOVER_OBJETO(this, mascota);
		}
		SocketManager.ENVIAR_Re_DETALLES_MONTURA(this, "+", _montura);
		if (_pelea == null) {
			SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(_mapa, this);
		} else if (_pelea.getEstado() == 2) {
			SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_PELEA(_pelea, _pelea.getLuchadorPorPJ(this));
		}
		SocketManager.ENVIAR_Rr_ESTADO_MONTADO(this, _montando ? "+" : "-");
		SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		_montura.energiaPerdida(15);
	}

	public int getXpDonadaMontura() {
		return _xpDonadaMontura;
	}

	public Dragopavo getMontura() {
		return _montura;
	}

	public void setMontura(Dragopavo DP) {
		_montura = DP;
	}

	public void setDonarXPMontura(int xp) {
		_xpDonadaMontura = xp;
	}

	public void setEnLinea(boolean linea) {
		_enLinea = linea;
	}

	public void resetVariables() {
		_intercambioCon = 0;
		_conversandoCon = 0;
		_ocupado = false;
		_emoteActivado = 0;
		_listo = false;
		_intercambio = null;
		_estarBanco = false;
		_invitando = -1;
		_sentado = false;
		_accionTrabajo = null;
		_enZaaping = false;
		_enCercado = null;
		_montando = false;
		_recaudando = false;
		_recaudandoRecaudadorID = 0;
		_esDoble = false;
		_olvidandoHechizo = false;
		_ausente = false;
		_invisible = false;
		_trueque = null;
		_cofre = null;
		_casa = null;
		_listaArtesanos = false;
		_cambiarNombre = false;
		_dragopaveando = false;
		_siguiendo = null;
		_tallerInvitado = null;
		_tutorial = null;
	}

	public void addCanal(String canal) {
		if (_canales.indexOf(canal) >= 0) {
			return;
		}
		_canales = String.valueOf(_canales) + canal;
		SocketManager.ENVIAR_cC_SUSCRIBIR_CANAL(this, '+', canal);
	}

	public void removerCanal(String canal) {
		_canales = _canales.replace(canal, "");
		SocketManager.ENVIAR_cC_SUSCRIBIR_CANAL(this, '-', canal);
	}

	public void modificarAlineamiento(byte a) {
		_honor = 0;
		_deshonor = 0;
		_alineacion = a;
		_nivelAlineacion = 1;
		_mostrarAlas = false;
		SocketManager.ENVIAR_ZC_ESPECIALIDAD_ALINEACION(this, a);
		SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
	}

	public void setDeshonor(int deshonor) {
		_deshonor = deshonor;
	}

	public int getDeshonor() {
		return _deshonor;
	}

	public boolean estaMostrandoAlas() {
		return _mostrarAlas;
	}

	public void setMostrarAlas(boolean mostrarAlas) {
		_mostrarAlas = mostrarAlas;
	}

	public int getHonor() {
		return _honor;
	}

	public int getNivelAlineacion() {
		if (_alineacion == 0) {
			return 1;
		}
		return _nivelAlineacion;
	}

	public void botonActDesacAlas(char c) {
		if (_alineacion == 0) {
			return;
		}
		int honorPerd = _honor / 20;
		switch (c) {
		case '*': {
			SocketManager.ENVIAR_GIP_ACT_DES_ALAS_PERDER_HONOR(this, honorPerd);
			return;
		}
		case '+': {
			_mostrarAlas = true;
			SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
			break;
		}
		case '-': {
			_mostrarAlas = false;
			_honor -= honorPerd;
			SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		}
		}
	}

	public void addHonor(int honor) {
		if (honor == 0) {
			return;
		}
		int nivelAntes = _nivelAlineacion;
		_honor += honor;
		if (_honor < 0) {
			_honor = 0;
		} else if (_honor >= 25000) {
			_nivelAlineacion = 10;
			_honor = 25000;
		}
		for (int n = 1; n <= 10; ++n) {
			if (_honor >= Mundo.getExpNivel((int) n)._pvp)
				continue;
			_nivelAlineacion = n - 1;
			break;
		}
		if (nivelAntes == _nivelAlineacion) {
			return;
		}
		if (nivelAntes < _nivelAlineacion) {
			SocketManager.ENVIAR_Im_INFORMACION(this, "082;" + _nivelAlineacion);
		} else if (nivelAntes > _nivelAlineacion) {
			SocketManager.ENVIAR_Im_INFORMACION(this, "083;" + _nivelAlineacion);
		}
	}

	public Gremio.MiembroGremio getMiembroGremio() {
		return _miembroGremio;
	}

	public int getCuentaID() {
		return _cuenta.getID();
	}

	public void setCuenta(Cuenta c) {
		_cuenta = c;
	}

	public String stringListaZaap() {
		StringBuffer str = new StringBuffer();
		str.append(_puntoSalvado.split(",")[0]);
		int subAreaID = _mapa.getSubArea().getArea().getSuperArea().getID();
		for (short i : _zaaps) {
			if (Mundo.getMapa(i) == null || Mundo.getMapa(i).getSubArea().getArea().getSuperArea().getID() != subAreaID)
				continue;
			int costo = Fórmulas.calcularCosteZaap(_mapa, Mundo.getMapa(i));
			if (i == _mapa.getID()) {
				costo = 0;
			}
			str.append("|" + i + ";" + costo);
		}
		return str.toString();
	}

	public String stringListaPrismas() {
		StringBuffer str = new StringBuffer(_mapa.getID());
		int subAreaID = _mapa.getSubArea().getArea().getSuperArea().getID();
		for (Prisma prisma : Mundo.todosPrismas()) {
			short mapaID;
			if (prisma.getAlineacion() != _alineacion || Mundo.getMapa(mapaID = prisma.getMapaID()) == null
					|| Mundo.getMapa(mapaID).getSubArea().getArea().getSuperArea().getID() != subAreaID)
				continue;
			if (prisma.getEstadoPelea() == 0 || prisma.getEstadoPelea() == -2) {
				str.append("|" + mapaID + ";*");
				continue;
			}
			int costo = Fórmulas.calcularCosteZaap(_mapa, Mundo.getMapa(mapaID));
			if (mapaID == _mapa.getID()) {
				costo = 0;
			}
			str.append("|" + mapaID + ";" + costo);
		}
		return str.toString();
	}

	public boolean tieneZaap(int mapID) {
		for (short i : _zaaps) {
			if (i != mapID)
				continue;
			return true;
		}
		return false;
	}

	public void abrirMenuZaap() {
		if (_pelea == null) {
			if (getDeshonor() >= 3) {
				SocketManager.ENVIAR_Im_INFORMACION(this, "183");
				return;
			}
			_enZaaping = true;
			if (!tieneZaap(_mapa.getID())) {
				_zaaps.add(_mapa.getID());
				SocketManager.ENVIAR_Im_INFORMACION(this, "024");
			}
			SocketManager.ENVIAR_WC_MENU_ZAAP(this);
		}
	}

	public void abrirMenuPrisma() {
		if (_pelea == null) {
			if (getDeshonor() >= 3) {
				SocketManager.ENVIAR_Im_INFORMACION(this, "183");
				return;
			}
			_enZaaping = true;
			SocketManager.ENVIAR_Wp_MENU_PRISMA(this);
		}
	}

	public void usarZaap(short mapaID) {
		if (!_enZaaping || _pelea != null || !tieneZaap(mapaID)) {
			return;
		}
		int costo = Fórmulas.calcularCosteZaap(_mapa, Mundo.getMapa(mapaID));
		if (_kamas < costo) {
			return;
		}
		int superAreaID = _mapa.getSubArea().getArea().getSuperArea().getID();
		short celdaID = Mundo.getCeldaZaapPorMapaID(mapaID);
		Mapa zaapMapa = Mundo.getMapa(mapaID);
		if (zaapMapa == null || zaapMapa.getCelda(celdaID) == null || !zaapMapa.getCelda(celdaID).esCaminable(true) || zaapMapa.getSubArea().getArea().getSuperArea().getID() != superAreaID) {
			SocketManager.ENVIAR_WUE_ZAPPI_ERROR(this);
			return;
		}
		if (_alineacion == 2 && mapaID == 4263 || _alineacion == 1 && mapaID == 5295) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this, "<b>Sistema</b>: Esse zaap \u00e9 do alinhamento inimigo.");
			SocketManager.ENVIAR_WUE_ZAPPI_ERROR(this);
			return;
		}
		_kamas -= (long) costo;
		teleport(mapaID, celdaID);
		SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		SocketManager.ENVIAR_WV_CERRAR_ZAAP(this);
		_enZaaping = false;
	}

	public String stringZaaps() {
		StringBuffer str = new StringBuffer();
		boolean primero = false;
		for (short i : _zaaps) {
			if (primero) {
				str.append(",");
			}
			primero = true;
			str.append(i);
		}
		return str.toString();
	}

	public void cerrarZaap() {
		if (!_enZaaping) {
			return;
		}
		_enZaaping = false;
		SocketManager.ENVIAR_WV_CERRAR_ZAAP(this);
	}

	public void cerrarPrisma() {
		if (!_enZaaping) {
			return;
		}
		_enZaaping = false;
		SocketManager.ENVIAR_Ww_CERRAR_PRISMA(this);
	}

	public void cerrarZaapi() {
		if (!_enZaaping) {
			return;
		}
		_enZaaping = false;
		SocketManager.ENVIAR_Wv_CERRAR_ZAPPI(this);
	}

	public void usarZaapi(String packet) {
		short mapaID = Short.parseShort(packet.substring(2));
		Mapa mapa = Mundo.getMapa(mapaID);
		short celdaId = 100;
		if (mapa != null) {
			for (Map.Entry<Short, Mapa.Celda> entry : mapa.getCeldas().entrySet()) {
				Mapa.ObjetoInteractivo obj = entry.getValue().getObjetoInterac();
				if (obj == null || obj.getID() != 7031 && obj.getID() != 7030)
					continue;
				celdaId = (short) (entry.getValue().getID() + 18);
			}
		}
		if (mapa.getSubArea().getArea().getID() == 7 || mapa.getSubArea().getArea().getID() == 11) {
			int precio = 20;
			if (_alineacion == 1 || _alineacion == 2) {
				precio = 10;
			}
			_kamas -= (long) precio;
			SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
			teleport(mapaID, celdaId);
			SocketManager.ENVIAR_Wv_CERRAR_ZAPPI(this);
		}
	}

	public void usarPrisma(String packet) {
		short celdaID = 340;
		short mapaID = 7411;
		try {
			mapaID = Short.parseShort(packet.substring(2));
		} catch (Exception exception) {
			// empty catch block
		}
		for (Prisma prisma : Mundo.todosPrismas()) {
			if (prisma.getMapaID() != mapaID)
				continue;
			celdaID = prisma.getCeldaID();
			mapaID = prisma.getMapaID();
			break;
		}
		int costo = Fórmulas.calcularCosteZaap(_mapa, Mundo.getMapa(mapaID));
		if (mapaID == _mapa.getID()) {
			costo = 0;
		}
		if (_kamas < (long) costo) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this,
					"<b>Sistema</b>: Voc\u00ea n\u00e3o tem as kamas necess\u00e1rias para essa a\u00e7\u00e3o.");
			return;
		}
		_kamas -= (long) costo;
		SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
		teleport(mapaID, celdaID);
		SocketManager.ENVIAR_Ww_CERRAR_PRISMA(this);
	}

	public boolean tieneObjModeloNoEquip(int id, int cantidad) {
		for (Objeto obj : _objetos.values()) {
			if (obj.getPosicion() != -1 && obj.getPosicion() < 35 || obj.getModelo().getID() != id
					|| obj.getCantidad() < cantidad)
				continue;
			return true;
		}
		return false;
	}

	public Objeto getObjModeloNoEquip(int id, int cantidad) {
		for (Objeto obj : _objetos.values()) {
			if (obj.getPosicion() != -1 && obj.getPosicion() < 35 || obj.getModelo().getID() != id
					|| obj.getCantidad() < cantidad)
				continue;
			return obj;
		}
		return null;
	}

	public void setZaaping(boolean zaaping) {
		_enZaaping = zaaping;
	}

	public void setOlvidandoHechizo(boolean olvidandoHechizo) {
		_olvidandoHechizo = olvidandoHechizo;
	}

	public boolean estaOlvidandoHechizo() {
		return _olvidandoHechizo;
	}

	public boolean estaDisponible(Personaje perso) {
		if (_ausente) {
			return false;
		}
		if (_invisible) {
			return _cuenta.esAmigo(perso.getCuenta().getID());
		}
		return true;
	}

	public void setSiguiendo(Personaje perso) {
		_siguiendo = perso;
	}

	public Personaje getSiguiendo() {
		return _siguiendo;
	}

	public boolean estaAusente() {
		return _ausente;
	}

	public void setEstaAusente(boolean ausente) {
		_ausente = ausente;
	}

	public boolean esInvisible() {
		return _invisible;
	}

	public boolean esFantasma() {
		return _esFantasma;
	}

	public void setEsInvisible(boolean invisible) {
		_invisible = invisible;
	}

	public boolean esDoble() {
		return _esDoble;
	}

	public void esDoble(boolean esDoble) {
		_esDoble = esDoble;
	}

	public boolean getRecaudando() {
		return _recaudando;
	}

	public void setRecaudando(boolean recaudando) {
		_recaudando = recaudando;
	}

	public void setDragopaveando(boolean recaudando) {
		_dragopaveando = recaudando;
	}

	public boolean getMochilaMontura() {
		return _dragopaveando;
	}

	public int getRecaudandoRecauID() {
		return _recaudandoRecaudadorID;
	}

	public void setRecaudandoRecaudadorID(int recaudadorID) {
		_recaudandoRecaudadorID = recaudadorID;
	}

	public void setTitulo(int titulo) {
		_titulo = titulo;
	}

	public int getTitulo() {
		return _titulo;
	}

	public boolean cambiarNombre() {
		return _cambiarNombre;
	}

	public void cambiarNombre(boolean cambiar) {
		_cambiarNombre = cambiar;
	}

	public void setNombre(String nombre) {
		_nombre = nombre;
		_cambiarNombre = false;
		SQLManager.ACTUALIZAR_NOMBRE(this);
		if (getMiembroGremio() != null) {
			SQLManager.REPLACE_MIEMBRO_GREMIO(getMiembroGremio());
		}
	}

	public static Personaje personajeClonado(Personaje perso, int id) {
		TreeMap<Integer, Integer> stats = new TreeMap<Integer, Integer>();
		Stats statsBase = perso.getBaseStats();
		stats.put(125, statsBase.getEfecto(125));
		stats.put(118, statsBase.getEfecto(118));
		stats.put(124, statsBase.getEfecto(124));
		stats.put(126, statsBase.getEfecto(126));
		stats.put(123, statsBase.getEfecto(123));
		stats.put(119, statsBase.getEfecto(119));
		stats.put(111, statsBase.getEfecto(111));
		stats.put(128, statsBase.getEfecto(128));
		stats.put(214, statsBase.getEfecto(214));
		stats.put(210, statsBase.getEfecto(210));
		stats.put(213, statsBase.getEfecto(213));
		stats.put(211, statsBase.getEfecto(211));
		stats.put(212, statsBase.getEfecto(212));
		stats.put(160, statsBase.getEfecto(160));
		stats.put(161, statsBase.getEfecto(161));
		byte mostrarAlas = 0;
		int nivelAlineacion = 0;
		if (perso._alineacion != 0 && perso._mostrarAlas) {
			mostrarAlas = 1;
			nivelAlineacion = perso.getNivelAlineacion();
		}
		int monturaID = -1;
		if (perso._montura != null) {
			monturaID = perso._montura.getID();
		}
		Personaje clon = new Personaje(id, perso._nombre, perso._sexo, perso._clase, perso._color1, perso._color2,
				perso._color3, perso._nivel, 100, perso._gfxID, stats, perso._objetos, 100, mostrarAlas, monturaID,
				nivelAlineacion, perso._alineacion);
		clon.esDoble(true);
		if (perso._montando) {
			clon._montando = true;
		}
		return clon;
	}

	public void verificaYCambiaObjPosicion() {
		boolean primerAmuleto = true;
		boolean primerAnillo1 = true;
		boolean primerAnillo2 = true;
		boolean primerArma = true;
		boolean primerBotas = true;
		boolean primerEscudo = true;
		boolean primerCapa = true;
		boolean primerCinturon = true;
		boolean primerSombrero = true;
		boolean primerDofus1 = true;
		boolean primerDofus2 = true;
		boolean primerDofus3 = true;
		boolean primerDofus4 = true;
		boolean primerDofus5 = true;
		boolean primerDofus6 = true;
		boolean primerMascota = true;
		for (Objeto obj : _objetos.values()) {
			if (obj.getPosicion() == -1)
				continue;
			if (obj.getPosicion() == 0) {
				if (primerAmuleto) {
					primerAmuleto = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 2) {
				if (primerAnillo1) {
					primerAnillo1 = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 4) {
				if (primerAnillo2) {
					primerAnillo2 = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 1) {
				if (primerArma) {
					primerArma = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 5) {
				if (primerBotas) {
					primerBotas = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 15) {
				if (primerEscudo) {
					primerEscudo = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 7) {
				if (primerCapa) {
					primerCapa = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 3) {
				if (primerCinturon) {
					primerCinturon = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 6) {
				if (primerSombrero) {
					primerSombrero = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 9) {
				if (primerDofus1) {
					primerDofus1 = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 10) {
				if (primerDofus2) {
					primerDofus2 = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 11) {
				if (primerDofus3) {
					primerDofus3 = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 12) {
				if (primerDofus4) {
					primerDofus4 = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 13) {
				if (primerDofus5) {
					primerDofus5 = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() == 14) {
				if (primerDofus6) {
					primerDofus6 = false;
					continue;
				}
				obj.setPosicion(-1);
				continue;
			}
			if (obj.getPosicion() != 8)
				continue;
			if (primerMascota) {
				primerMascota = false;
				continue;
			}
			obj.setPosicion(-1);
		}
	}

	public MisionPVP getMisionPVP() {
		return _misionPvp;
	}

	public void setMisionPVP(MisionPVP mision) {
		_misionPvp = mision;
	}

	public void esposoDe(Personaje esposo) {
		_esposo = esposo.getID();
	}

	public String getEsposoListaAmigos() {
		Personaje esposo = Mundo.getPersonaje(_esposo);
		StringBuffer str = new StringBuffer();
		if (esposo != null) {
			str.append(String.valueOf(esposo._nombre) + "|" + esposo._clase + esposo._sexo + "|" + esposo._color1 + "|"
					+ esposo._color2 + "|" + esposo._color3 + "|");
			if (!esposo._enLinea) {
				str.append("|");
			} else {
				str.append(String.valueOf(esposo.stringUbicEsposo()) + "|");
			}
		} else {
			str.append("|");
		}
		return str.toString();
	}

	public String stringUbicEsposo() {
		int p = 0;
		if (_pelea != null) {
			p = 1;
		}
		return String.valueOf(_mapa.getID()) + "|" + _nivel + "|" + p;
	}

	public void casarse(Personaje perso) {
		if (perso == null) {
			return;
		}
		int dist = (_mapa.getX() - perso._mapa.getX()) * (_mapa.getX() - perso._mapa.getX())
				+ (_mapa.getY() - perso._mapa.getY()) * (_mapa.getY() - perso._mapa.getY());
		if (dist > 100) {
			if (perso.getSexo() == 0) {
				SocketManager.ENVIAR_Im_INFORMACION(this, "178");
			} else {
				SocketManager.ENVIAR_Im_INFORMACION(this, "179");
			}
			return;
		}
		short celdaPosicion = Constantes.getCeldaIDCercanaNoUsada(perso);
		if (celdaPosicion == 0) {
			if (perso.getSexo() == 0) {
				SocketManager.ENVIAR_Im_INFORMACION(this, "141");
			} else {
				SocketManager.ENVIAR_Im_INFORMACION(this, "142");
			}
			return;
		}
		teleport(perso._mapa.getID(), celdaPosicion);
	}

	public void divorciar() {
		if (_enLinea) {
			SocketManager.ENVIAR_Im_INFORMACION(this, "047;" + Mundo.getPersonaje(_esposo).getNombre());
		}
		_esposo = 0;
	}

	public int getEsposo() {
		return _esposo;
	}

	public int setEsOK(int ok) {
		_esOK = ok;
		return _esOK;
	}

	public int getEsOK() {
		return _esOK;
	}

	public void cambiarOrientacion(byte orientacion) {
		if (_orientacion == 0 || _orientacion == 2 || _orientacion == 4 || _orientacion == 6) {
			setOrientacion(orientacion);
			SocketManager.ENVIAR_eD_CAMBIAR_ORIENTACION(_mapa, getID(), orientacion);
		}
	}

	public void setCofre(Cofre cofre) {
		_cofre = cofre;
	}

	public Cofre getCofre() {
		return _cofre;
	}

	public void setCasa(Casa casa) {
		_casa = casa;
	}

	public Casa getCasa() {
		return _casa;
	}

	public String stringColorDue\u00f1oPavo() {
		return String.valueOf(_color1 == -1 ? "" : Integer.toHexString(_color1)) + ","
				+ (_color2 == -1 ? "" : Integer.toHexString(_color2)) + ","
				+ (_color3 == -1 ? "" : Integer.toHexString(_color3));
	}

	public String getModeloObjEnPos(int posiciones) {
		if (posiciones == -1) {
			return null;
		}
		for (Map.Entry<Integer, Objeto> entry : _objetos.entrySet()) {
			Objeto obj = entry.getValue();
			if (obj.getPosicion() != posiciones)
				continue;
			if (obj.getObjeviID() != 0) {
				for (Objevivo objevi : Mundo.getTodosObjevivos()) {
					if (objevi.getID() != obj.getObjeviID())
						continue;
					String toReturn = String.valueOf(Integer.toHexString(objevi.getRealModeloDB())) + "~"
							+ objevi.getTipo() + "~" + objevi.getMascara();
					return toReturn;
				}
				continue;
			}
			return Integer.toHexString(obj.getModelo().getID());
		}
		return null;
	}

	public boolean objetoAInvetario(int id) {
		if (this == null || _intercambioCon != _ID) {
			return false;
		}
		Objeto objMovido = null;
		for (Objeto objeto : _tienda) {
			if (objeto.getID() != id)
				continue;
			objMovido = objeto;
			break;
		}
		if (objMovido == null) {
			return false;
		}
		_tienda.remove(objMovido);
		if (addObjetoSimilar(objMovido, true, -1)) {
			Mundo.eliminarObjeto(objMovido.getID());
		} else {
			addObjetoPut(objMovido);
			SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this, objMovido);
		}
		Mundo.borrarObjTienda(objMovido.getID());
		return true;
	}

	public int contarTienda() {
		int cantidad = _tienda.size();
		return cantidad;
	}

	public int maxTienda() {
		int cantidad = _nivel / 10;
		return cantidad;
	}

	public String getStringTienda() {
		StringBuffer str = new StringBuffer();
		for (Objeto objetos : _tienda) {
			str.append(String.valueOf(objetos.getID()) + "|");
		}
		return str.toString();
	}

	public void agregarObjTienda(Objeto objeto) {
		_tienda.add(objeto);
	}

	public void borrarObjTienda(Objeto objeto) {
		if (_tienda.contains(objeto)) {
			_tienda.remove(objeto);
			Mundo.borrarObjTienda(objeto.getID());
		}
	}

	public boolean quedaObjTienda() {
		return _tienda.size() != 0;
	}

	public String listaTienda() {
		String lista = "";
		boolean esPrimero = true;
		if (_tienda.isEmpty()) {
			return lista;
		}
		for (Objeto objeto : _tienda) {
			int idobjeto;
			Tienda tienda;
			if (!esPrimero) {
				lista = String.valueOf(lista) + "|";
			}
			if ((tienda = Mundo.getObjTienda(idobjeto = objeto.getID())) == null)
				continue;
			lista = String.valueOf(lista) + idobjeto + ";" + tienda.getCantidad() + ";" + objeto.getModelo().getID()
					+ ";" + objeto.convertirStatsAString() + ";" + tienda.getPrecio();
			esPrimero = false;
		}
		return lista;
	}

	public ArrayList<Objeto> getTienda() {
		return _tienda;
	}

	public void actualizarObjTienda(int objID, long precio) {
		Tienda tienda = Mundo.getObjTienda(objID);
		tienda.setPrecio(precio);
	}

	public long precioTotalTienda() {
		long precio = 0L;
		for (Objeto obj : _tienda) {
			Tienda tienda = Mundo.getObjTienda(obj.getID());
			if (tienda == null) {
				return 0L;
			}
			precio += tienda.getPrecio();
		}
		return precio;
	}

	public void refrescarSetClase() {
		for (int j = 2; j < 8; ++j) {
			String[] stats;
			if (getObjPosicion(j) == null)
				continue;
			Objeto obj = getObjPosicion(j);
			int template = obj.getModelo().getID();
			int set = obj.getModelo().getSetID();
			if ((set < 81 || set > 92) && (set < 201 || set > 212))
				continue;
			String[] arrstring = stats = obj.getModelo().getStringStatsObj().split(",");
			int n = stats.length;
			for (int i = 0; i < n; ++i) {
				String stat = arrstring[i];
				String[] val = stat.split("#");
				int efecto = Integer.parseInt(val[0], 16);
				int hechizo = Integer.parseInt(val[1], 16);
				int modif = Integer.parseInt(val[3], 16);
				String modificacion = String.valueOf(efecto) + ";" + hechizo + ";" + modif;
				SocketManager.ENVIAR_SB_HECHIZO_BOOST_SET_CLASE(this, modificacion);
				addHechizosSetClase(hechizo, efecto, modif);
			}
			if (_setClase.contains(template))
				continue;
			_setClase.add(template);
		}
	}

	public int getModifSetClase(int hechizo, int efecto) {
		int modif = 0;
		if (_bendHechizo == hechizo && _bendEfecto == efecto) {
			modif += _bendModif;
		}
		if (_hechizosSetClase.containsKey(hechizo)
				&& (Integer) _hechizosSetClase.get((Object) Integer.valueOf((int) hechizo))._primero == efecto) {
			return modif += ((Integer) _hechizosSetClase.get((Object) Integer.valueOf((int) hechizo))._segundo)
					.intValue();
		}
		return modif;
	}

	public String analizarPrismas() {
		String str = "";
		Prisma prisma = Mundo.getPrisma(_mapa.getSubArea().getPrismaID());
		str = prisma == null ? "-3"
				: (prisma.getEstadoPelea() == 0 ? "0;" + prisma.getTiempoTurno() + ";45000;7"
						: String.valueOf(prisma.getEstadoPelea()));
		return str;
	}

	public void setObjetoARomper(int objetoID) {
		_objetoIDRomper = objetoID;
	}

	public int getObjetoARomper() {
		return _objetoIDRomper;
	}

	public void setRompiendo(boolean romper) {
		_rompiendo = romper;
	}

	public boolean getRompiendo() {
		return _rompiendo;
	}

	public void restarVidaMascota(Mascota mascota) {
		Objeto masc = null;
		masc = mascota == null ? getObjPosicion(8) : Mundo.getObjeto(mascota.getID());
		if (masc != null) {
			int idMascota = masc.getID();
			if (mascota == null) {
				mascota = Mundo.getMascota(idMascota);
			}
			if (mascota == null) {
				return;
			}
			if (mascota.getPDV() > 1) {
				mascota.setPDV(mascota.getPDV() - 1);
				SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(this, masc);
			} else {
				mascota.setPDV(0);
				SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, idMascota);
				int nuevoModelo = Constantes.fantasmaMascota(masc.getIDModelo());
				if (nuevoModelo != 0) {
					masc.setPosicion(-1);
					masc.setIDModelo(nuevoModelo);
					SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this, masc);
					_mascota = null;
				} else {
					borrarObjetoSinEliminar(idMascota);
					Mundo.eliminarObjeto(idMascota);
					_mascota = null;
				}
				refrescarVida();
				SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
				SocketManager.ENVIAR_Im_INFORMACION(this, "154");
			}
		}
	}

	public void setAceptar(boolean aceptar) {
		_aceptaKoli = aceptar;
	}

	public boolean getAceptar() {
		return _aceptaKoli;
	}

	public long expKoli() {
		switch (_categoria) {
		case 1: {
			return (Mundo.getExpNivel((int) (_nivel + 1))._personaje - Mundo.getExpNivel((int) _nivel)._personaje) * 2L;
		}
		case 2: {
			return Mundo.getExpNivel((int) (_nivel + 1))._personaje - Mundo.getExpNivel((int) _nivel)._personaje;
		}
		case 3: {
			if (_nivel == 300) {
				return 0L;
			}
			return (Mundo.getExpNivel((int) (_nivel + 1))._personaje - Mundo.getExpNivel((int) _nivel)._personaje) / 6L;
		}
		}
		return 0L;
	}

	public long kamasKoli() {
		switch (_categoria) {
		case 1: {
			return 20000L;
		}
		case 2: {
			return 50000L;
		}
		case 3: {
			if (_nivel == 300) {
				return 250000L;
			}
			return 100000L;
		}
		}
		return 0L;
	}

	public int get_savestat() {
		return savestat;
	}

	public void set_savestat(int stat) {
		savestat = stat;
	}

	public static class Grupo {
		private CopyOnWriteArrayList<Personaje> _personajesGrupo = new CopyOnWriteArrayList<Personaje>();
		private Personaje _liderGrupo;

		public Grupo(Personaje p1, Personaje p2) {
			_liderGrupo = p1;
			_personajesGrupo.add(p1);
			_personajesGrupo.add(p2);
		}

		public boolean esLiderGrupo(int id) {
			return _liderGrupo.getID() == id;
		}

		public void addPerso(Personaje perso) {
			_personajesGrupo.add(perso);
		}

		public ArrayList<Integer> getIDsPersos() {
			ArrayList<Integer> lista = new ArrayList<Integer>();
			for (Personaje perso : _personajesGrupo) {
				lista.add(perso.getID());
			}
			return lista;
		}

		public int getNumeroPjs() {
			return _personajesGrupo.size();
		}

		public int getNivelGrupo() {
			int nivel = 0;
			for (Personaje p : _personajesGrupo) {
				nivel += p.getNivel();
			}
			return nivel;
		}

		public CopyOnWriteArrayList<Personaje> getPersos() {
			return _personajesGrupo;
		}

		public Personaje getLiderGrupo() {
			return _liderGrupo;
		}

		public void dejarGrupo(Personaje p) {
			if (!_personajesGrupo.contains(p)) {
				return;
			}
			p.setGrupo(null);
			_personajesGrupo.remove(p);
			if (_personajesGrupo.size() == 1) {
				_personajesGrupo.get(0).setGrupo(null);
				if (_personajesGrupo.get(0).getCuenta() == null
						|| _personajesGrupo.get(0).getCuenta().getEntradaPersonaje() == null) {
					return;
				}
				SocketManager.ENVIAR_PV_DEJAR_GRUPO(_personajesGrupo.get(0).getCuenta().getEntradaPersonaje().getOut(),
						"");
			} else {
				SocketManager.ENVIAR_PM_EXPULSAR_PJ_GRUPO(this, p.getID());
			}
		}
	}

	public static class GrupoKoliseo {
		private Personaje _koli1;
		private Personaje _koli2;
		private Personaje _koli3;
		private int _sumaNivel = 0;
		private int _categoria = 0;

		public GrupoKoliseo(Personaje koli1, Personaje koli2, Personaje koli3, int categoria) {
			_koli1 = koli1;
			_koli2 = koli2;
			_koli3 = koli3;
			_koli1._categoria = categoria;
			_koli2._categoria = categoria;
			_koli3._categoria = categoria;
			_sumaNivel = _koli1.getNivel() + _koli2.getNivel() + _koli3.getNivel();
			_categoria = categoria;
		}

		public int getSumaNiveles() {
			return _sumaNivel;
		}

		public ArrayList<Personaje> getParticipantes() {
			ArrayList<Personaje> grupo = new ArrayList<Personaje>();
			grupo.add(_koli1);
			grupo.add(_koli2);
			grupo.add(_koli3);
			return grupo;
		}

		public int getCategoria() {
			return _categoria;
		}
	}

	public static class MisionPVP {
		private long _tiempo;
		private Personaje _victimaPVP;
		private int _kamas;

		public MisionPVP(long tiempo, Personaje personaje, int kamas) {
			_tiempo = tiempo;
			_victimaPVP = personaje;
			_kamas = kamas;
		}

		public void setPjMision(Personaje personaje) {
			_victimaPVP = personaje;
		}

		public int getKamasRecompensa() {
			return _kamas;
		}

		public void setKamasRecompensa(int kamas) {
			_kamas = kamas;
		}

		public Personaje getPjMision() {
			return _victimaPVP;
		}

		public long getTiempoPVP() {
			return _tiempo;
		}

		public void setTiempoPVP(long tiempo) {
			_tiempo = tiempo;
		}
	}

	public static class Stats {
		public Map<Integer, Integer> _statsEfecto = new TreeMap<Integer, Integer>();

		public Stats(boolean addBases, Personaje perso) {
			_statsEfecto = new TreeMap<Integer, Integer>();
			if (!addBases) {
				return;
			}
			_statsEfecto.put(111, perso.getNivel() < LesGuardians.NIVEL_PA1 ? 6 : 6 + LesGuardians.MAX_PA1);
			_statsEfecto.put(128, 3);
			_statsEfecto.put(176, perso.getClase(false) == 3 ? 140 : 100);
			_statsEfecto.put(158, 1000);
			_statsEfecto.put(182, 1);
			_statsEfecto.put(174, 1);
		}

		public int getEffect(int statsAddFuerza) {
			return 0;
		}

		public Stats(Map<Integer, Integer> stats, boolean addBases, Personaje perso) {
			_statsEfecto = stats;
			if (!addBases) {
				return;
			}
			_statsEfecto.put(111, perso.getNivel() < LesGuardians.NIVEL_PA1 ? 6 : 6 + LesGuardians.MAX_PA1);
			_statsEfecto.put(128, 3);
			_statsEfecto.put(176, perso.getClase(false) == 3 ? 140 : 100);
			_statsEfecto.put(158, 1000);
			_statsEfecto.put(182, 1);
			_statsEfecto.put(174, 1);
		}

		public Stats(Map<Integer, Integer> stats) {
			_statsEfecto = stats;
		}

		public Stats() {
			_statsEfecto = new TreeMap<Integer, Integer>();
		}

		public int addStat(int stat, int valor) {
			if (_statsEfecto.get(stat) == null) {
				_statsEfecto.put(stat, valor);
			} else {
				int nuevoValor = _statsEfecto.get(stat) + valor;
				_statsEfecto.remove(stat);
				_statsEfecto.put(stat, nuevoValor);
			}
			return _statsEfecto.get(stat);
		}

		public int especificarStat(int stat, int valor) {
			if (_statsEfecto.get(stat) == null) {
				_statsEfecto.put(stat, valor);
			} else {
				_statsEfecto.remove(stat);
				_statsEfecto.put(stat, valor);
			}
			return _statsEfecto.get(stat);
		}

		public boolean sonStatsIguales(Stats otros) {
			for (Map.Entry<Integer, Integer> entry : _statsEfecto.entrySet()) {
				if (otros.getStatsComoMap().get(entry.getKey()) != null
						&& otros.getStatsComoMap().get(entry.getKey()) == entry.getValue())
					continue;
				return false;
			}
			for (Map.Entry<Integer, Integer> entry : otros.getStatsComoMap().entrySet()) {
				if (_statsEfecto.get(entry.getKey()) != null && _statsEfecto.get(entry.getKey()) == entry.getValue())
					continue;
				return false;
			}
			return true;
		}

		public int getEfecto(int id) {
			int val = _statsEfecto.get(id) == null ? 0 : _statsEfecto.get(id);
			switch (id) {
			case 160: {
				if (_statsEfecto.get(162) != null) {
					val -= getEfecto(162);
				}
				if (_statsEfecto.get(124) == null)
					break;
				val += getEfecto(124) / 4;
				break;
			}
			case 161: {
				if (_statsEfecto.get(163) != null) {
					val -= getEfecto(163);
				}
				if (_statsEfecto.get(124) == null)
					break;
				val += getEfecto(124) / 4;
				break;
			}
			case 174: {
				if (_statsEfecto.get(175) == null)
					break;
				val -= _statsEfecto.get(175).intValue();
				break;
			}
			case 119: {
				if (_statsEfecto.get(154) == null)
					break;
				val -= _statsEfecto.get(154).intValue();
				break;
			}
			case 118: {
				if (_statsEfecto.get(157) == null)
					break;
				val -= _statsEfecto.get(157).intValue();
				break;
			}
			case 123: {
				if (_statsEfecto.get(152) == null)
					break;
				val -= _statsEfecto.get(152).intValue();
				break;
			}
			case 126: {
				if (_statsEfecto.get(155) == null)
					break;
				val -= _statsEfecto.get(155).intValue();
				break;
			}
			case 111: {
				if (_statsEfecto.get(120) != null) {
					val += _statsEfecto.get(120).intValue();
				}
				if (_statsEfecto.get(101) != null) {
					val -= _statsEfecto.get(101).intValue();
				}
				if (_statsEfecto.get(168) == null)
					break;
				val -= _statsEfecto.get(168).intValue();
				break;
			}
			case 128: {
				if (_statsEfecto.get(78) != null) {
					val += _statsEfecto.get(78).intValue();
				}
				if (_statsEfecto.get(127) != null) {
					val -= _statsEfecto.get(127).intValue();
				}
				if (_statsEfecto.get(169) == null)
					break;
				val -= _statsEfecto.get(169).intValue();
				break;
			}
			case 117: {
				if (_statsEfecto.get(116) == null)
					break;
				val -= _statsEfecto.get(116).intValue();
				break;
			}
			case 125: {
				if (_statsEfecto.get(110) != null) {
					val += _statsEfecto.get(110).intValue();
				}
				if (_statsEfecto.get(153) == null)
					break;
				val -= _statsEfecto.get(153).intValue();
				break;
			}
			case 112: {
				if (_statsEfecto.get(145) == null)
					break;
				val -= _statsEfecto.get(145).intValue();
				break;
			}
			case 158: {
				if (_statsEfecto.get(159) == null)
					break;
				val -= _statsEfecto.get(159).intValue();
				break;
			}
			case 176: {
				if (_statsEfecto.get(177) != null) {
					val -= _statsEfecto.get(177).intValue();
				}
				if (_statsEfecto.get(123) == null)
					break;
				val += _statsEfecto.get(123) / 10;
				break;
			}
			case 242: {
				if (_statsEfecto.get(247) == null)
					break;
				val -= _statsEfecto.get(247).intValue();
				break;
			}
			case 243: {
				if (_statsEfecto.get(248) == null)
					break;
				val -= _statsEfecto.get(248).intValue();
				break;
			}
			case 244: {
				if (_statsEfecto.get(249) == null)
					break;
				val -= _statsEfecto.get(249).intValue();
				break;
			}
			case 240: {
				if (_statsEfecto.get(245) == null)
					break;
				val -= _statsEfecto.get(245).intValue();
				break;
			}
			case 241: {
				if (_statsEfecto.get(246) == null)
					break;
				val -= _statsEfecto.get(246).intValue();
				break;
			}
			case 210: {
				if (_statsEfecto.get(215) == null)
					break;
				val -= _statsEfecto.get(215).intValue();
				break;
			}
			case 211: {
				if (_statsEfecto.get(216) == null)
					break;
				val -= _statsEfecto.get(216).intValue();
				break;
			}
			case 212: {
				if (_statsEfecto.get(217) == null)
					break;
				val -= _statsEfecto.get(217).intValue();
				break;
			}
			case 213: {
				if (_statsEfecto.get(218) == null)
					break;
				val -= _statsEfecto.get(218).intValue();
				break;
			}
			case 214: {
				if (_statsEfecto.get(219) == null)
					break;
				val -= _statsEfecto.get(219).intValue();
				break;
			}
			case 165: {
				if (_statsEfecto.get(165) == null)
					break;
				val = _statsEfecto.get(165);
				break;
			}
			case 138: {
				if (_statsEfecto.get(186) == null)
					break;
				val -= _statsEfecto.get(186).intValue();
				break;
			}
			case 178: {
				if (_statsEfecto.get(179) == null)
					break;
				val -= _statsEfecto.get(179).intValue();
			}
			}
			return val;
		}

		public static Stats acumularStats(Stats s1, Stats s2) {
			TreeMap<Integer, Integer> stats = new TreeMap<Integer, Integer>();
			for (int a = 0; a <= 1500; ++a) {
				if (!(s1._statsEfecto.get(a) != null && s1._statsEfecto.get(a) != 0
						|| s2._statsEfecto.get(a) != null && s2._statsEfecto.get(a) != 0))
					continue;
				int suma = 0;
				if (s1._statsEfecto.get(a) != null) {
					suma += s1._statsEfecto.get(a).intValue();
				}
				if (s2._statsEfecto.get(a) != null) {
					suma += s2._statsEfecto.get(a).intValue();
				}
				stats.put(a, suma);
			}
			return new Stats(stats, false, null);
		}

		public Map<Integer, Integer> getStatsComoMap() {
			return _statsEfecto;
		}

		public String convertirStatsAString() {
			StringBuffer str = new StringBuffer();
			for (Map.Entry<Integer, Integer> entry : _statsEfecto.entrySet()) {
				if (str.length() > 0) {
					str.append(",");
				}
				str.append(String.valueOf(Integer.toHexString(entry.getKey())) + "#"
						+ Integer.toHexString(entry.getValue()) + "#0#0");
			}
			return str.toString();
		}
	}
}