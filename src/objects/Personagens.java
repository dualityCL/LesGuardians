package objects;

import common.Constantes;
import common.Fórmulas;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.World;
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
import objects.Conta;
import objects.Dragossauros;
import objects.Fight;
import objects.Guild;
import objects.Incarnacao;
import objects.Maps;
import objects.Objeto;
import objects.Pets;
import objects.Prisma;
import objects.Profissao;
import objects.Set_Vivo;
import objects.Spell;
import objects.Stockage;
import objects.Tutorial;
import org.fusesource.jansi.AnsiConsole;

public class Personagens {
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
    private Conta _cuenta;
    private String _emotes = "7667711";
    private byte _alineacion = 0;
    private int _deshonor = 0;
    private int _honor = 0;
    private boolean _mostrarAlas = false;
    private int _nivelAlineacion = 0;
    private Guild.MiembroGremio _miembroGremio;
    private boolean _mostrarConeccionAmigos;
    private String _canales;
    Stats _baseStats;
    private Fight _pelea;
    private boolean _ocupado;
    private Maps _mapa;
    private Maps.Celda _celda;
    private int _PDV;
    private boolean _estarBanco;
    private int _PDVMAX;
    private boolean _sentado;
    private boolean _listo = false;
    private boolean _enLinea = false;
    private Grupo _grupo;
    private int _intercambioCon = 0;
    private World.Intercambio _intercambio;
    private World.InvitarTaller _tallerInvitado;
    private int _conversandoCon = 0;
    private int _invitando = 0;
    private int _dueloID = -1;
    private Map<Integer, Spell.StatsHechizos> _hechizos = new TreeMap<Integer, Spell.StatsHechizos>();
    private Map<Integer, Character> _lugaresHechizos = new TreeMap<Integer, Character>();
    private Map<Integer, Objeto> _objetos = new TreeMap<Integer, Objeto>();
    private ArrayList<Objeto> _tienda = new ArrayList();
    private Map<Integer, Profissao.StatsOficio> _statsOficios = new TreeMap<Integer, Profissao.StatsOficio>();
    private Timer _recuperarVida;
    private String _puntoSalvado;
    private int _exPdv;
    private Maps.Cercado _enCercado;
    private int _emoteActivado = 0;
    private Profissao.AccionTrabajo _accionTrabajo;
    private Dragossauros _montura;
    private int _xpDonadaMontura = 0;
    private boolean _montando = false;
    private boolean _enZaaping = false;
    private ArrayList<Short> _zaaps = new ArrayList();
    private boolean _ausente = false;
    private boolean _invisible = false;
    private Personagens _siguiendo = null;
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
    private World.Trueque _trueque = null;
    private ArrayList<Integer> _setClase = new ArrayList();
    private Map<Integer, World.Duo<Integer, Integer>> _hechizosSetClase = new TreeMap<Integer, World.Duo<Integer, Integer>>();
    private boolean _esFantasma = false;
    private Maps _tempMapaDefPerco = null;
    private Maps.Celda _tempCeldaDefPerco = null;
    private Map<Integer, Personagens> _seguidores = new TreeMap<Integer, Personagens>();
    private Cofre _cofre;
    private Casa _casa;
    private Pets _mascota;
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
    private Incarnacao _encarnacion;
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
        this._tutorial = tuto;
    }

    public Tutorial getTutorial() {
        return this._tutorial;
    }

    public void setReconectado(boolean recon) {
        this._reconectado = recon;
    }

    public boolean getReconectado() {
        Personagens.setTitle("Les Guardians, Conectados : " + LesGuardians._servidorPersonaje.nroJugadoresLinea());
        return this._reconectado;
    }

    public void setEncarnacion(Incarnacao encarnacion) {
        this._encarnacion = encarnacion;
        this._idEncarnacion = encarnacion != null ? encarnacion.getID() : -1;
    }

    public int getIDEncarnacion() {
        return this._idEncarnacion;
    }

    public Incarnacao getEncarnacion() {
        return this._encarnacion;
    }

    public void setPescarKuakua(boolean pescar) {
        this._pescarKuakua = pescar;
    }

    public boolean getPescarKuakua() {
        return this._pescarKuakua;
    }

    public void setUltimaMision(String nombre) {
        this._ultimaMisionPVP = nombre;
    }

    public String getUltimaMision() {
        return this._ultimaMisionPVP;
    }

    public void setForjaEcK(String forja) {
        this._forjaEcK = forja;
    }

    public String getForjaEcK() {
        return this._forjaEcK;
    }

    public void efectuarRestriccionesA() {
        this._puedeAgredir = (this._restriccionesA & 1) != 1;
        this._puedeDesafiar = (this._restriccionesA & 2) != 2;
        this._puedeIntercambiar = (this._restriccionesA & 4) != 4;
        this._puedeAtacarAMutante = (this._restriccionesA & 8) == 8;
        this._puedeChatATodos = (this._restriccionesA & 0x10) != 16;
        this._puedeMercante = (this._restriccionesA & 0x20) != 32;
        this._puedeUsarObjetos = (this._restriccionesA & 0x40) != 64;
        this._puedeInteractuarRecaudador = (this._restriccionesA & 0x80) != 128;
        this._puedeInteractuarObjetos = (this._restriccionesA & 0x100) != 256;
        this._puedeHablarNPC = (this._restriccionesA & 0x200) != 512;
        this._puedeAtacarMobsDungCuandoMutante = (this._restriccionesA & 0x1000) == 4096;
        this._puedeMoverTodasDirecciones = (this._restriccionesA & 0x2000) == 8192;
        this._puedeAtacarMobsCualquieraCuandoMutante = (this._restriccionesA & 0x4000) == 16384;
        this._puedeInteractuarPrisma = (this._restriccionesA & 0x8000) != 32768;
    }

    public String mostrarmeA() {
        this._puedeAgredir = (this._restriccionesA & 1) != 1;
        this._puedeDesafiar = (this._restriccionesA & 2) != 2;
        this._puedeIntercambiar = (this._restriccionesA & 4) != 4;
        this._puedeAtacarAMutante = (this._restriccionesA & 8) == 8;
        this._puedeChatATodos = (this._restriccionesA & 0x10) != 16;
        this._puedeMercante = (this._restriccionesA & 0x20) != 32;
        this._puedeUsarObjetos = (this._restriccionesA & 0x40) != 64;
        this._puedeInteractuarRecaudador = (this._restriccionesA & 0x80) != 128;
        this._puedeInteractuarObjetos = (this._restriccionesA & 0x100) != 256;
        this._puedeHablarNPC = (this._restriccionesA & 0x200) != 512;
        this._puedeAtacarMobsDungCuandoMutante = (this._restriccionesA & 0x1000) == 4096;
        this._puedeMoverTodasDirecciones = (this._restriccionesA & 0x2000) == 8192;
        this._puedeAtacarMobsCualquieraCuandoMutante = (this._restriccionesA & 0x4000) == 16384;
        this._puedeInteractuarPrisma = (this._restriccionesA & 0x8000) != 32768;
        String retorno = "RESTRICCIONES DE A ---------------------------" + this._nombre + "\n_puedeAgredir : " + this._puedeAgredir + "\n_puedeDesafiar : " + this._puedeDesafiar + "\n_puedeIntercambiar : " + this._puedeIntercambiar + "\n_puedeAtacarAMutante : " + this._puedeAtacarAMutante + "\n_puedeChatATodos : " + this._puedeChatATodos + "\n_puedeMercante : " + this._puedeMercante + "\n_puedeUsarObjetos : " + this._puedeUsarObjetos + "\n_puedeInteractuarRecaudador : " + this._puedeInteractuarRecaudador + "\n_puedeInteractuarObjetos : " + this._puedeInteractuarObjetos + "\n_puedeHablarNPC : " + this._puedeHablarNPC + "\n_puedeAtacarMobsDungCuandoMutante : " + this._puedeAtacarMobsDungCuandoMutante + "\n_puedeMoverTodasDirecciones : " + this._puedeMoverTodasDirecciones + "\n_puedeAtacarMobsCualquieraCuandoMutante : " + this._puedeAtacarMobsCualquieraCuandoMutante + "\n_puedeInteractuarPrisma : " + this._puedeInteractuarPrisma;
        return retorno;
    }

    public int getRestriccionesA() {
        int restr = 0;
        if (!this._puedeAgredir) {
            ++restr;
        }
        if (!this._puedeDesafiar) {
            restr += 2;
        }
        if (!this._puedeIntercambiar) {
            restr += 4;
        }
        if (this._puedeAtacarAMutante) {
            restr += 8;
        }
        if (!this._puedeChatATodos) {
            restr += 16;
        }
        if (!this._puedeMercante) {
            restr += 32;
        }
        if (!this._puedeUsarObjetos) {
            restr += 64;
        }
        if (!this._puedeInteractuarRecaudador) {
            restr += 128;
        }
        if (!this._puedeInteractuarObjetos) {
            restr += 256;
        }
        if (!this._puedeHablarNPC) {
            restr += 512;
        }
        if (this._puedeAtacarMobsDungCuandoMutante) {
            restr += 4096;
        }
        if (this._puedeMoverTodasDirecciones) {
            restr += 8192;
        }
        if (this._puedeAtacarMobsCualquieraCuandoMutante) {
            restr += 16384;
        }
        if (!this._puedeInteractuarPrisma) {
            restr += 32768;
        }
        this._restriccionesA = restr;
        return restr;
    }

    public void efectuarRestriccionesB() {
        this._puedeSerAgredido = (this._restriccionesB & 1) != 1;
        this._puedeSerDesafiado = (this._restriccionesB & 2) != 2;
        this._puedeHacerIntercambio = (this._restriccionesB & 4) != 4;
        this._puedeSerAtacado = (this._restriccionesB & 8) != 8;
        this._forzadoCaminar = (this._restriccionesB & 0x10) == 16;
        this._esLento = (this._restriccionesB & 0x20) == 32;
        this._puedeSwitchModoCriatura = (this._restriccionesB & 0x40) != 64;
        this._esTumba = (this._restriccionesB & 0x80) == 128;
    }

    public String mostrarmeB() {
        this._puedeSerAgredido = (this._restriccionesB & 1) != 1;
        this._puedeSerDesafiado = (this._restriccionesB & 2) != 2;
        this._puedeHacerIntercambio = (this._restriccionesB & 4) != 4;
        this._puedeSerAtacado = (this._restriccionesB & 8) != 8;
        this._forzadoCaminar = (this._restriccionesB & 0x10) == 16;
        this._esLento = (this._restriccionesB & 0x20) == 32;
        this._puedeSwitchModoCriatura = (this._restriccionesB & 0x40) != 64;
        this._esTumba = (this._restriccionesB & 0x80) == 128;
        String retorno = "RESTRICCIONES DE B ---------------------------" + this._nombre + "\n_puedeSerAgredido : " + this._puedeSerAgredido + "\n_puedeSerDesafiado : " + this._puedeSerDesafiado + "\n_puedeHacerIntercambio : " + this._puedeHacerIntercambio + "\n_puedeSerAtacado : " + this._puedeSerAtacado + "\n_forzadoCaminar : " + this._forzadoCaminar + "\n_esLento : " + this._esLento + "\n_puedeSwitchModoCriatura : " + this._puedeSwitchModoCriatura + "\n_esTumba : " + this._esTumba;
        return retorno;
    }

    public int getRestriccionesB() {
        int restr = 0;
        if (!this._puedeSerAgredido) {
            ++restr;
        }
        if (!this._puedeSerDesafiado) {
            restr += 2;
        }
        if (!this._puedeHacerIntercambio) {
            restr += 4;
        }
        if (!this._puedeSerAtacado) {
            restr += 8;
        }
        if (this._forzadoCaminar) {
            restr += 16;
        }
        if (this._esLento) {
            restr += 32;
        }
        if (!this._puedeSwitchModoCriatura) {
            restr += 64;
        }
        if (this._esTumba) {
            restr += 128;
        }
        this._restriccionesB = restr;
        return restr;
    }

    public Map<Integer, Personagens> getSeguidores() {
        return this._seguidores;
    }

    public void setMascota(Pets mascota) {
        this._mascota = mascota;
    }

    public Pets getMascota() {
        return this._mascota;
    }

    public void setGrupoKoliseo(GrupoKoliseo koli) {
        this._koliseo = koli;
    }

    public GrupoKoliseo getGrupoKoliseo() {
        return this._koliseo;
    }

    public void setEnKoliseo(boolean koliseo) {
        this._enKoliseo = koliseo;
    }

    public boolean getEnKoliseo() {
        return this._enKoliseo;
    }

    public void setCategoria(int categoria) {
        this._categoria = categoria;
    }

    public int getCategoria() {
        return this._categoria;
    }

    public Map<Integer, World.Duo<Integer, Integer>> getHechizosSetClase() {
        return this._hechizosSetClase;
    }

    public void delHechizosSetClase(int hechizo) {
        if (this._hechizosSetClase.containsKey(hechizo)) {
            this._hechizosSetClase.remove(hechizo);
        }
    }

    public void addHechizosSetClase(int hechizo, int efecto, int modificacion) {
        if (!this._hechizosSetClase.containsKey(hechizo)) {
            this._hechizosSetClase.put(hechizo, new World.Duo<Integer, Integer>(efecto, modificacion));
        }
    }

    public void setListaArtesanos(boolean viendo) {
        this._listaArtesanos = viendo;
    }

    public boolean getListaArtesanos() {
        return this._listaArtesanos;
    }

    public void addScrollFuerza(int scroll) {
        this._scrollFuerza += scroll;
    }

    public void addScrollAgilidad(int scroll) {
        this._scrollAgilidad += scroll;
    }

    public void addScrollSuerte(int scroll) {
        this._scrollSuerte += scroll;
    }

    public void addScrollVitalidad(int scroll) {
        this._scrollVitalidad += scroll;
    }

    public void addScrollSabiduria(int scroll) {
        this._scrollSabiduria += scroll;
    }

    public void addScrollInteligencia(int scroll) {
        this._scrollInteligencia += scroll;
    }

    public int getScrollFuerza() {
        return this._scrollFuerza;
    }

    public int getScrollAgilidad() {
        return this._scrollAgilidad;
    }

    public int getScrollSuerte() {
        return this._scrollSuerte;
    }

    public int getScrollVitalidad() {
        return this._scrollVitalidad;
    }

    public int getScrollSabiduria() {
        return this._scrollSabiduria;
    }

    public int getScrollInteligencia() {
        return this._scrollInteligencia;
    }

    public void setMapaDefPerco(Maps mapa) {
        this._tempMapaDefPerco = mapa;
    }

    public void setCeldaDefPerco(Maps.Celda celda) {
        this._tempCeldaDefPerco = celda;
    }

    public Maps.Celda getCeldaDefPerco() {
        return this._tempCeldaDefPerco;
    }

    public Maps getMapaDefPerco() {
        return this._tempMapaDefPerco;
    }

    public ArrayList<Integer> getSetClase() {
        return this._setClase;
    }

    public void setSetClase(ArrayList<Integer> SetClase) {
        this._setClase = SetClase;
    }

    public void agregarSetClase(int item) {
        if (!this._setClase.contains(item)) {
            this._setClase.add(item);
        }
    }

    public void borrarSetClase(int item) {
        if (this._setClase.contains(item)) {
            int index = this._setClase.indexOf(item);
            this._setClase.remove(index);
        }
    }

    public int getMercante() {
        return this._esMercante;
    }

    public void setMercante(int mercante) {
        this._esMercante = mercante;
    }

    public World.Trueque getTrueque() {
        return this._trueque;
    }

    public void setTrueque(World.Trueque trueque) {
        this._trueque = trueque;
    }

    public GameThread.AccionDeJuego getTaller() {
        return this._taller;
    }

    public void setTaller(GameThread.AccionDeJuego Taller) {
        this._taller = Taller;
    }

    public Personagens(int id, String nombre, int sexo, int clase, int color1, int color2, int color3, long kamas, int pts, int capital, int energia, int nivel, long exp, int talla, int gfxID, byte alineacion, int cuenta, Map<Integer, Integer> stats, int mostrarAmigos, byte mostarAlineacion, String canal, short mapa, int celda, String inventario, int pdvPorc, String hechizos, String ptoSalvada, String oficios, int xpMontura, int montura, int honor, int deshonor, int nivelAlineacion, String zaaps, byte titulo, int esposoId, String tienda, int mercante, int ScrollFuerza, int ScrollInteligencia, int ScrollAgilidad, int ScrollSuerte, int ScrollVitalidad, int ScrollSabiduria, int restriccionesA, int restriccionesB, int encarnacion) {
        String[] infos;
        Objeto obj;
        this._encarnacion = World.getEncarnacion(encarnacion);
        if (this._encarnacion != null) {
            this._idEncarnacion = encarnacion;
        }
        this._oficioPublico = false;
        this._scrollAgilidad = ScrollAgilidad;
        this._scrollFuerza = ScrollFuerza;
        this._scrollInteligencia = ScrollInteligencia;
        this._scrollSabiduria = ScrollSabiduria;
        this._scrollSuerte = ScrollSuerte;
        this._scrollVitalidad = ScrollVitalidad;
        this._ID = id;
        this._nombre = nombre;
        this._sexo = sexo;
        this._clase = clase;
        this._color1 = color1;
        this._color2 = color2;
        this._color3 = color3;
        this.savestat = 0;
        this._kamas = kamas;
        this._puntosHechizo = pts;
        this._capital = capital;
        this._alineacion = alineacion;
        this._honor = honor;
        this._deshonor = deshonor;
        this._nivelAlineacion = nivelAlineacion;
        this._energia = energia;
        this._nivel = nivel;
        this._experiencia = exp;
        if (montura != -1) {
            this._montura = World.getDragopavoPorID(montura);
        }
        this._talla = talla;
        this._gfxID = gfxID;
        this._xpDonadaMontura = xpMontura;
        this._baseStats = new Stats(stats, true, this);
        this._cuenta = World.getCuenta(cuenta);
        try {
            this._cuenta.addPerso(this);
        }
        catch (NullPointerException e) {
            System.out.println("O personagem " + nombre + " n\u00e3o pode ser adicionado \u00e0 conta.");
        }
        this._mostrarConeccionAmigos = mostrarAmigos == 1;
        this._esposo = esposoId;
        this._mostrarAlas = this._alineacion != 0 ? mostarAlineacion == 1 : false;
        this._canales = canal;
        this._mapa = World.getMapa(mapa);
        this._puntoSalvado = ptoSalvada;
        if (this._mapa == null) {
            this._mapa = World.getMapa((short)this.mapaClase());
            this._celda = this._mapa.getCelda((short)311);
        } else if (this._mapa != null) {
            this._celda = this._mapa.getCelda((short)celda);
            if (this._celda == null) {
                this._mapa = World.getMapa((short)this.mapaClase());
                this._celda = this._mapa.getCelda((short)311);
            }
        }
        this._cambiarNombre = false;
        for (String str : zaaps.split(",")) {
            try {
                this._zaaps.add(Short.parseShort(str));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (this._mapa == null || this._celda == null) {
            System.out.println("Mapa ou c\u00e9lula invalida no personagem: " + this._nombre + ".");
            try {
                Thread.sleep(10000L);
            }
            catch (InterruptedException str) {
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
            if (item.equals("") || (obj = World.getObjeto(idObj = Integer.parseInt((infos = item.split(":"))[0]))) == null) continue;
            this._objetos.put(obj.getID(), obj);
        }
        if (!tienda.equals("")) {
            if (tienda.charAt(tienda.length() - 1) == '|') {
                tienda = tienda.substring(0, tienda.length() - 1);
            }
            SQLManager.CARGAR_OBJETOS(tienda.replace("|", ","));
        }
        for (String item : tienda.split("\\|")) {
            int idObjeto;
            if (item.equals("") || (obj = World.getObjeto(idObjeto = Integer.parseInt((infos = item.split(":"))[0]))) == null) continue;
            this._tienda.add(obj);
        }
        this._esMercante = mercante;
        this._PDVMAX = this._encarnacion != null ? this._encarnacion.getPDVMAX() : (nivel - 1) * 5 + (this._nivel > 200 ? (this._nivel - 200) * (this._clase == 11 ? 2 : 1) * 5 : 0) + Constantes.getBasePDV(clase) + this.getTotalStats().getEfecto(125);
        this._PDV = pdvPorc > 100 ? this._PDVMAX * 100 / 100 : this._PDVMAX * pdvPorc / 100;
        this.analizarPosHechizos(hechizos);
        this._recuperarVida = new Timer(1000, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Personagens.this.regenerarPuntoAPunto();
            }
        });
        this._exPdv = this._PDV;
        if (!oficios.equals("")) {
            for (String aJobData : oficios.split(";")) {
                infos = aJobData.split(",");
                try {
                    int oficioID = Integer.parseInt(infos[0]);
                    long xp = Long.parseLong(infos[1]);
                    Profissao oficio = World.getOficio(oficioID);
                    int pos = this.aprenderOficio(oficio);
                    if (pos == -1) continue;
                    Profissao.StatsOficio statsOficio = this._statsOficios.get(pos);
                    statsOficio.addXP(this, xp);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        this._titulo = titulo;
        this._restriccionesA = restriccionesA;
        this._restriccionesB = restriccionesB;
        this.efectuarRestriccionesA();
        this.efectuarRestriccionesB();
        this.refrescarSetClase();
        Objeto mascota = this.getObjPosicion(8);
        if (mascota != null) {
            this._mascota = World.getMascota(mascota.getID());
        }
    }

    public Personagens(int id, String nombre, int sexo, int clase, int color1, int color2, int color3, int nivel, int talla, int gfxid, Map<Integer, Integer> stats, Map<Integer, Objeto> objetos, int pdvPorc, byte mostarAlineacion, int montura, int nivelAlineacion, byte alineacion) {
        this._ID = id;
        this._nombre = nombre;
        this._sexo = sexo;
        this._clase = clase;
        this._color1 = color1;
        this._color2 = color2;
        this._color3 = color3;
        this._nivel = nivel;
        this._nivelAlineacion = nivelAlineacion;
        this._talla = talla;
        this._gfxID = gfxid;
        this._baseStats = new Stats(stats, true, this);
        this._objetos.putAll(objetos);
        this._PDVMAX = this._encarnacion != null ? this._encarnacion.getPDVMAX() : (nivel - 1) * 5 + (this._nivel > 200 ? (this._nivel - 200) * 5 : 0) + Constantes.getBasePDV(clase) + this.getTotalStats().getEfecto(125);
        this._PDV = this._PDVMAX * pdvPorc / 100;
        this._recuperarVida = new Timer(1000, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Personagens.this.regenerarPuntoAPunto();
            }
        });
        this._exPdv = this._PDV;
        this._alineacion = alineacion;
        this._mostrarAlas = this._alineacion != 0 ? mostarAlineacion == 1 : false;
        if (montura != -1) {
            this._montura = World.getDragopavoPorID(montura);
        }
    }

    public static synchronized Personagens crearPersonaje(String nombre, int sexo, int clase, int color1, int color2, int color3, Conta cuenta) {
        String zaaps = "";
        if (LesGuardians.USAR_ZAAPS) {
            zaaps = "164,528,844,935,951,1158,1242,1841,2191,3022,3250,4263,4739,5295,6137,6855,6954,7411,8037,8088,8125,8163,8437,8785,9454,10297,10304,10317,10349,10643,11170,11210";
        }
        long kamas = 100000L;
        String objetos = "";
        short nivel = LesGuardians.CONFIG_INICIAR_NIVEL;
        if (cuenta.getPrimeraVez() != 0) {
            Objeto obj = World.getObjModelo(1737).crearObjDesdeModelo(40, false);
            Objeto obj2 = World.getObjModelo(580).crearObjDesdeModelo(30, false);
            Objeto obj3 = World.getObjModelo(548).crearObjDesdeModelo(30, false);
            World.addObjeto(obj, true);
            World.addObjeto(obj2, true);
            World.addObjeto(obj3, true);
            obj.setPosicion(35);
            obj2.setPosicion(36);
            obj3.setPosicion(37);
            World.ObjetoSet objSet = World.getObjetoSet(LesGuardians.INICIAR_SET_ID);
            if (objSet != null) {
                for (Objeto.ObjetoModelo OM : objSet.getObjetosModelos()) {
                    Objeto x = OM.crearObjDesdeModelo(1, true);
                    World.addObjeto(x, true);
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
        Personagens nuevoPersonaje = new Personagens(World.getSigIDPersonaje(), nombre, sexo, clase, color1, color2, color3, kamas, (nivel - 1) * 1, (nivel - 1) * 5, (short) 10000,  nivel, World.getExpMinPersonaje(nivel), (short) 100,  Integer.parseInt(clase + "" + sexo), (byte) 0, cuenta.getID(), new TreeMap<Integer, Integer>(), 1, (byte) 0, "*#%!pi$:?", Constantes.getMapaInicio(clase), 314, objetos, 100, "", "7411,311", "", 0, -1, 0, 0, 0, zaaps, (byte) 0, 0, "", 0, 0, 0, 0, 0, 0, 0, 8192, 0, -1);
        nuevoPersonaje._hechizos = Constantes.getHechizosIniciales(clase);
        for (int a = 1; a <= nuevoPersonaje.getNivel(); ++a) {
            Constantes.subirNivelAprenderHechizos(nuevoPersonaje, a);
        }
        nuevoPersonaje._lugaresHechizos = Constantes.getLugaresHechizosIniciales(clase);
        if (!SQLManager.AGREGAR_PJ_EN_BD(nuevoPersonaje, objetos)) {
            return null;
        }
        World.addPersonaje(nuevoPersonaje);
        nuevoPersonaje.setEncarnacion(null);
        nuevoPersonaje._cuenta = cuenta;
        return nuevoPersonaje;
    }

    public void Conectarse() {
        if (this._cuenta.getEntradaPersonaje() == null) {
            System.out.println("O personagem " + this._nombre + " teve a entrada NULL");
            return;
        }
        PrintWriter out = this._cuenta.getEntradaPersonaje().getOut();
        this._cuenta.setTempPerso(this);
        this._enLinea = true;
        if (this._esMercante == 1) {
            this._mapa.removerMercante(this._ID);
            this._esMercante = 0;
            SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(this._mapa, this._ID);
        }
        if (this._montura != null) {
            SocketManager.ENVIAR_Re_DETALLES_MONTURA(this, "+", this._montura);
        }
        SocketManager.ENVIAR_Rx_EXP_DONADA_MONTURA(this);
        SocketManager.ENVIAR_ASK_PERSONAJE_SELECCIONADO(out, this);
        for (int idSet = 1; idSet < World.getNumeroObjetoSet(); ++idSet) {
            int num = this.getNroObjEquipadosDeSet(idSet);
            if (num == 0) continue;
            SocketManager.ENVIAR_OS_BONUS_SET(this, idSet, num);
        }
        if (this._statsOficios.size() > 0) {
            ArrayList<Profissao.StatsOficio> listaStatOficios = new ArrayList<Profissao.StatsOficio>();
            listaStatOficios.addAll(this._statsOficios.values());
            SocketManager.ENVIAR_JS_TRABAJO_POR_OFICIO(this, listaStatOficios);
            SocketManager.ENVIAR_JX_EXPERINENCIA_OFICIO(this, listaStatOficios);
            SocketManager.ENVIAR_JO_OFICIO_OPCIONES(this, listaStatOficios);
            Objeto obj = this.getObjPosicion(1);
            if (obj != null) {
                for (Profissao.StatsOficio statOficio : listaStatOficios) {
                    Profissao oficio = statOficio.getOficio();
                    if (!oficio.herramientaValida(obj.getModelo().getID())) continue;
                    SocketManager.ENVIAR_OT_OBJETO_HERRAMIENTA(out, oficio.getID());
                    String strOficioPub = Constantes.trabajosOficioTaller(oficio.getID());
                    if (this._mapa.esTaller() && this._oficioPublico) {
                        SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(out, "+", this._ID, strOficioPub);
                    }
                    this._stringOficiosPublicos = strOficioPub;
                }
            }
        }
        SocketManager.ENVIAR_ZS_ENVIAR_ALINEACION(out, this._alineacion);
        SocketManager.ENVIAR_cC_ACTIVAR_CANALES(out, String.valueOf(this._canales) + "^" + (this._cuenta.getRango() > 1 ? "@\u00c2\u00a4" : ""));
        if (this._miembroGremio != null) {
            SocketManager.ENVIAR_gS_STATS_GREMIO(this, this._miembroGremio);
        }
        SocketManager.ENVIAR_SL_LISTA_HECHIZOS(this);
        SocketManager.ENVIAR_eL_LISTA_EMOTES(this, this._emotes, "0");
        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this);
        SocketManager.ENVIAR_FO_MOSTRAR_CONEXION_AMIGOS(out, this._mostrarConeccionAmigos);
        this._cuenta.mensajeAAmigos();
        SocketManager.ENVIAR_Im_INFORMACION(this, "189");
        if (!this._cuenta.getUltimaConeccion().equals("") && !this._cuenta.getUltimoIP().equals("")) {
            SocketManager.ENVIAR_Im_INFORMACION(this, "0152;" + this._cuenta.getUltimaConeccion() + "~" + this._cuenta.getUltimoIP());
        }
        SocketManager.ENVIAR_Im_INFORMACION(this, "0153;" + this._cuenta.getActualIP());
        if (!this._cuenta.getActualIP().isEmpty()) {
            this._cuenta.setUltimoIP(this._cuenta.getActualIP());
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
        this._cuenta.setUltimaConeccion(String.valueOf(a\u00f1o) + "~" + mes + "~" + dia + "~" + hora + "~" + min);
        if (this._miembroGremio != null) {
            this._miembroGremio.setUltConeccion(String.valueOf(a\u00f1o) + "~" + mes + "~" + dia + "~" + hora + "~" + min);
        }
        SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this, LesGuardians.MSG_BEM_VINDO_1);
        SocketManager.ENVIAR_al_ESTADO_ZONA_ALINEACION(out);
        SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(out, Long.toString(this.getRestriccionesA(), 36));
        this._recuperarVida.start();
        if (this._pelea != null) {
            this._reconectado = true;
            return;
        }
        SocketManager.ENVIAR_ILS_TIEMPO_REGENERAR_VIDA(this, 1000);
        ArrayList<Integer> array = new ArrayList<Integer>();
        array.addAll(this._objetos.keySet());
        for (Integer idObj : array) {
            Pets masc = null;
            masc = World.getMascota(idObj);
            if (masc == null || masc.getPDV() < 1) continue;
            if (masc.esDevoraAlmas()) {
                SocketManager.ENVIAR_Im_INFORMACION(this, "025");
                continue;
            }
            if (masc.entreComidas() > 1440L) {
                if (masc.getDelgado()) {
                    this.restarVidaMascota(masc);
                }
                masc.setCorpulencia(2);
                SocketManager.ENVIAR_Im_INFORMACION(this, "150");
                continue;
            }
            SocketManager.ENVIAR_Im_INFORMACION(this, "025");
        }
        this.mostrarRates();
        SQLManager.UPDATE_CUENTA_LOG_UNO(this._cuenta.getID());
    }

    public void crearJuegoPJ() {
        if (this._cuenta.getEntradaPersonaje() == null) {
            return;
        }
        PrintWriter out = this._cuenta.getEntradaPersonaje().getOut();
        SocketManager.ENVIAR_GCK_CREAR_PANTALLA_PJ(out);
        SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
        SocketManager.ENVIAR_GDM_CAMBIO_DE_MAPA(out, this._mapa.getID(), this._mapa.getFecha(), this._mapa.getCodigo());
        if (this._pelea != null) {
            return;
        }
        SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(out, this._mapa);
        this._mapa.addJugador(this);
    }

    public void regenerarPuntoAPunto() {
        if (this._mapa == null || this._pelea != null || this._PDV == this._PDVMAX) {
            return;
        }
        ++this._PDV;
    }

    public void setHechizos(Map<Integer, Spell.StatsHechizos> hechizos) {
        this._hechizos.clear();
        this._lugaresHechizos.clear();
        this._hechizos = hechizos;
        this._lugaresHechizos = Constantes.getLugaresHechizosIniciales(this._clase);
    }

    public void cambiarSexo() {
        this._sexo = this._sexo == 1 ? 0 : 1;
    }

    public void setPtosHechizos(int puntos) {
        this._puntosHechizo = puntos;
    }

    public boolean enLinea() {
        return this._enLinea;
    }

    public void setGrupo(Grupo grupo) {
        this._grupo = grupo;
    }

    public Grupo getGrupo() {
        return this._grupo;
    }

    public boolean aprenderHechizo(int hechizoID, int nivel, boolean conectando, boolean enviar) {
        if (this._encarnacion != null && !conectando) {
            return false;
        }
        Spell aprender = World.getHechizo(hechizoID);
        if (aprender == null || aprender.getStatsPorNivel(nivel) == null) {
            System.out.println("[ERROR]Hechizo " + hechizoID + " nivel " + nivel + " no ubicado.");
            return false;
        }
        this._hechizos.remove(hechizoID);
        this._hechizos.put(hechizoID, aprender.getStatsPorNivel(nivel));
        if (enviar) {
            SocketManager.ENVIAR_SL_LISTA_HECHIZOS(this);
            SocketManager.ENVIAR_Im_INFORMACION(this, "03;" + hechizoID);
        }
        return true;
    }

    public String analizarHechizosABD() {
        String hechizos = "";
        if (this._hechizos.isEmpty()) {
            return "";
        }
        for (int key : this._hechizos.keySet()) {
            Spell.StatsHechizos SH = this._hechizos.get(key);
            hechizos = String.valueOf(hechizos) + SH.getHechizoID() + ";" + SH.getNivel() + ";";
            hechizos = this._lugaresHechizos.get(key) != null ? String.valueOf(hechizos) + this._lugaresHechizos.get(key) : String.valueOf(hechizos) + "_";
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
                this.aprenderHechizo(id, nivel, true, false);
                this._lugaresHechizos.put(id, Character.valueOf(pos));
                continue;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
    }

    public String getPtoSalvada() {
        return this._puntoSalvado;
    }

    public void setOficioPublico(boolean publico) {
        this._oficioPublico = publico;
    }

    public void setStrOficiosPublicos(String oficios) {
        this._stringOficiosPublicos = oficios;
    }

    public boolean getOficioPublico() {
        return this._oficioPublico;
    }

    public String getStringOficiosPublicos() {
        return this._stringOficiosPublicos;
    }

    public int mapaClase() {
        int mapa = 8570;
        switch (this._clase) {
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
        this._puntoSalvado = savePos;
    }

    public int getIntercambiandoCon() {
        return this._intercambioCon;
    }

    public void setIntercambiandoCon(int intercambiando) {
        this._intercambioCon = intercambiando;
    }

    public int getConversandoCon() {
        return this._conversandoCon;
    }

    public void setConversandoCon(int conversando) {
        this._conversandoCon = conversando;
    }

    public long getKamas() {
        return this._kamas;
    }

    public void setKamas(long l) {
        if (l < 0L) {
            l = 0L;
        }
        this._kamas = l;
    }

    public void addKamas(long l) {
        this._kamas += l;
        if (this._kamas < 0L) {
            this._kamas = 0L;
        }
    }

    public Conta getCuenta() {
        return this._cuenta;
    }

    public int getPuntosHechizos() {
        return this._puntosHechizo;
    }

    public Guild getGremio() {
        if (this._miembroGremio == null) {
            return null;
        }
        return this._miembroGremio.getGremio();
    }

    public void setMiembroGremio(Guild.MiembroGremio gremio) {
        this._miembroGremio = gremio;
    }

    public boolean estaListo() {
        return this._listo;
    }

    public void setListo(boolean listo) {
        this._listo = listo;
    }

    public int getDueloID() {
        return this._dueloID;
    }

    public Fight getPelea() {
        return this._pelea;
    }

    public void setDueloID(int dueloID) {
        this._dueloID = dueloID;
    }

    public int getEnergia() {
        return this._energia;
    }

    public boolean mostrarConeccionAmigo() {
        return this._mostrarConeccionAmigos;
    }

    public boolean mostrarAlas() {
        return this._mostrarAlas;
    }

    public String getCanal() {
        return this._canales;
    }

    public boolean esTumba() {
        return this._esTumba;
    }

    public void setRestriccionesA(int restr) {
        this._restriccionesA = restr;
        this.efectuarRestriccionesA();
        SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(this._cuenta.getEntradaPersonaje().getOut(), Integer.toString(this._restriccionesA, 36));
    }

    public void setRestriccionesB(int restr) {
        this._restriccionesB = restr;
        this.efectuarRestriccionesB();
    }

    public void convertirTumba() {
        try {
            this._gfxID = this._clase * 10 + 3;
            this._esFantasma = false;
            this._esTumba = true;
            this._esFantasma = false;
            this._puedeAgredir = false;
            this._puedeSerAgredido = false;
            this._puedeSerDesafiado = false;
            this._puedeHacerIntercambio = false;
            this._puedeIntercambiar = false;
            this._puedeHablarNPC = false;
            this._puedeMercante = false;
            this._puedeInteractuarRecaudador = false;
            this._puedeInteractuarPrisma = false;
            this._puedeUsarObjetos = false;
            this._forzadoCaminar = true;
            this._esLento = true;
            this._ocupado = true;
            this._puedeAtacarAMutante = false;
            this._puedeDesafiar = false;
            this._puedeSerAtacado = false;
            this._puedeInteractuarObjetos = false;
            SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(this._mapa, this);
            SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(this._cuenta.getEntradaPersonaje().getOut(), Integer.toString(this.getRestriccionesA(), 36));
            SocketManager.ENVIAR_M1_MENSAJE_SERVER(this, "12", "", "");
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void agregarEnergia(int energia) {
        int exEnergia = this._energia;
        this._energia += energia;
        if (this._energia > 10000) {
            this._energia = 10000;
        }
        if (this._esFantasma && exEnergia <= 0 && this._energia > 0) {
            if (this._encarnacion != null) {
                this._gfxID = this._encarnacion.getGfx();
            } else {
                this.deformar();
            }
            this._energia = energia;
            this._esTumba = false;
            this._esFantasma = false;
            this._puedeAgredir = true;
            this._puedeSerAgredido = true;
            this._puedeSerDesafiado = true;
            this._puedeHacerIntercambio = true;
            this._puedeIntercambiar = true;
            this._puedeHablarNPC = true;
            this._puedeMercante = true;
            this._puedeInteractuarRecaudador = true;
            this._puedeInteractuarPrisma = true;
            this._puedeUsarObjetos = true;
            this._esLento = false;
            this._ocupado = false;
            this._forzadoCaminar = false;
            this._puedeAtacarAMutante = false;
            this._puedeDesafiar = true;
            this._puedeSerAtacado = true;
            this._puedeInteractuarObjetos = true;
            SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
            SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(this._mapa, this);
            SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(this._cuenta.getEntradaPersonaje().getOut(), Long.toString(this.getRestriccionesA(), 36));
        }
    }

    public void restarEnergia(int energia) {
        if (LesGuardians._cerrando) {
            return;
        }
        this._energia -= energia;
        if (this._energia <= 0) {
            this.convertirTumba();
        } else if (this._energia < 1500) {
            SocketManager.ENVIAR_M1_MENSAJE_SERVER(this, "11", String.valueOf(energia), "");
        }
    }

    public void volverseFantasma() {
        this._gfxID = 8004;
        this._esTumba = false;
        this._esFantasma = true;
        this._puedeAgredir = false;
        this._puedeSerAgredido = false;
        this._puedeSerDesafiado = false;
        this._puedeHacerIntercambio = false;
        this._puedeIntercambiar = false;
        this._puedeHablarNPC = false;
        this._puedeMercante = false;
        this._puedeInteractuarRecaudador = false;
        this._puedeInteractuarPrisma = false;
        this._puedeUsarObjetos = false;
        this._forzadoCaminar = true;
        this._esLento = true;
        this._ocupado = true;
        this._puedeAtacarAMutante = false;
        this._puedeDesafiar = false;
        this._puedeSerAtacado = false;
        this._puedeInteractuarObjetos = false;
        if (LesGuardians.MODO_HEROICO) {
            if (this._grupo != null) {
                this._grupo.dejarGrupo(this);
            }
            SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(this._mapa, this._ID);
            this._celda.removerPersonaje(this._ID);
            this.resetVariables();
            SocketManager.ENVIAR_GO_GAME_OVER(this);
            World.eliminarPj(this, false);
            SQLManager.SALVAR_PERSONAJE(this, false);
            return;
        }
        SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(this._mapa, this);
        SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(this._cuenta.getEntradaPersonaje().getOut(), Long.toString(this.getRestriccionesA(), 36));
        this.teleport((short)1188, (short)297);
        SocketManager.ENVIAR_M1_MENSAJE_SERVER(this, "15", "", "");
        SocketManager.ENVIAR_IH_COORDINAS_UBICACION(this, "12;12;270|-1;33;1399|10;19;268|5;-9;7796|2;-12;8534|-30;-54;4285|-26;35;4551|-23;38;12169|-11;-54;3360|-43;0;10430|-10;13;9227|-41;-17;9539|36;5;1118|24;-43;7910|27;-33;8054|-60;-3;10672|-58;18;10590|-14;31;5717|25;-4;844|");
    }

    public void revivir() {
        if (this._encarnacion != null) {
            this._gfxID = this._encarnacion.getGfx();
        } else {
            this.deformar();
        }
        this._energia = 1000;
        this._esTumba = false;
        this._esFantasma = false;
        this._puedeAgredir = true;
        this._puedeSerAgredido = true;
        this._puedeSerDesafiado = true;
        this._puedeHacerIntercambio = true;
        this._puedeIntercambiar = true;
        this._puedeHablarNPC = true;
        this._puedeMercante = true;
        this._puedeInteractuarRecaudador = true;
        this._puedeInteractuarPrisma = true;
        this._puedeUsarObjetos = true;
        this._esLento = false;
        this._ocupado = false;
        this._forzadoCaminar = false;
        this._puedeAtacarAMutante = false;
        this._puedeDesafiar = true;
        this._puedeSerAtacado = true;
        this._puedeInteractuarObjetos = true;
        SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
        SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(this._mapa, this);
        SocketManager.ENVIAR_AR_RESTRICCIONES_PERSONAJE(this._cuenta.getEntradaPersonaje().getOut(), Long.toString(this.getRestriccionesA(), 36));
        SocketManager.ENVIAR_IH_COORDINAS_UBICACION(this, "");
    }

    public int getNivel() {
        return this._nivel;
    }

    public void setNivel(int nivel) {
        this._nivel = nivel;
    }

    public long getExperiencia() {
        return this._experiencia;
    }

    public Maps.Celda getCelda() {
        return this._celda;
    }

    public void setCelda(Maps.Celda celda) {
        this._celda.removerPersonaje(this._ID);
        this._celda = celda;
        celda.addPersonaje(this);
    }

    public int getTalla() {
        return this._talla;
    }

    public void setTalla(int talla) {
        this._talla = talla;
    }

    public void setPelea(Fight pelea) {
        this._pelea = pelea;
        if (pelea == null) {
            return;
        }
        if (this._montando && this._montura != null) {
            this._montura.energiaPerdida(20);
        }
        try {
            if (pelea.getTipoPelea() > 0) {
                for (int i = 20; i < 22; ++i) {
                    Objeto obj = this.getObjPosicion(i);
                    if (obj == null) continue;
                    int idObj = obj.getID();
                    String stats = obj.convertirStatsAString();
                    String[] arg = stats.split(",");
                    obj.clearTodo();
                    String[] arrstring = arg;
                    int n = arg.length;
                    for (int j = 0; j < n; ++j) {
                        String efec = arrstring[j];
                        String[] val = efec.split("#");
                        if (Integer.parseInt(val[0], 16) != 811) continue;
                        int turnos = Integer.parseInt(val[3], 16);
                        if (turnos == 0) {
                            this.borrarObjetoSinEliminar(idObj);
                            World.eliminarObjeto(idObj);
                            SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, idObj);
                            if (i == 21) {
                                this._bendEfecto = 0;
                                this._bendHechizo = 0;
                                this._bendModif = 0;
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
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    public int getGfxID() {
        return this._gfxID;
    }

    public void setGfxID(int gfxid) {
        this._gfxID = gfxid;
    }

    public void deformar() {
        this._gfxID = this._clase * 10 + this._sexo;
    }

    public int getID() {
        return this._ID;
    }

    public Maps getMapa() {
        return this._mapa;
    }

    public String getNombre() {
        return this._nombre;
    }

    public boolean estaOcupado() {
        return this._ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this._ocupado = ocupado;
    }

    public boolean estaSentado() {
        return this._sentado;
    }

    public int getSexo() {
        return this._sexo;
    }

    public int getClase(boolean original) {
        if (this._encarnacion != null && !original) {
            return this._encarnacion.getClase();
        }
        return this._clase;
    }

    public void setClase(int clase) {
        this._clase = clase;
    }

    public void setExperiencia(long exp) {
        this._experiencia = exp;
    }

    public int getColor1() {
        return this._color1;
    }

    public int getColor2() {
        return this._color2;
    }

    public Stats getBaseStats() {
        return this._baseStats;
    }

    public int getColor3() {
        return this._color3;
    }

    public int getCapital() {
        return this._capital;
    }

    public void resetearStats() {
        this._baseStats.addStat(125, (short)(-this._baseStats.getEfecto(125) + this._scrollVitalidad));
        this._baseStats.addStat(124, (short)(-this._baseStats.getEfecto(124) + this._scrollSabiduria));
        this._baseStats.addStat(118, (short)(-this._baseStats.getEfecto(118) + this._scrollFuerza));
        this._baseStats.addStat(123, (short)(-this._baseStats.getEfecto(123) + this._scrollSuerte));
        this._baseStats.addStat(119, (short)(-this._baseStats.getEfecto(119) + this._scrollAgilidad));
        this._baseStats.addStat(126, (short)(-this._baseStats.getEfecto(126) + this._scrollInteligencia));
    }

    public boolean tieneHechizoID(int hechizo) {
        if (this._encarnacion != null) {
            return this._encarnacion.tieneHechizoID(hechizo);
        }
        return this._hechizos.get(hechizo) != null;
    }

    public boolean boostearHechizo(int hechizoID) {
        if (this._encarnacion != null || this._hechizos.get(hechizoID) == null) {
            return false;
        }
        int antNivel = this._hechizos.get(hechizoID).getNivel();
        if (antNivel == 6) {
            return false;
        }
        if (this._puntosHechizo >= antNivel && World.getHechizo(hechizoID).getStatsPorNivel(antNivel + 1).getReqNivel() <= this._nivel) {
            if (this.aprenderHechizo(hechizoID, antNivel + 1, false, false)) {
                this._puntosHechizo -= antNivel;
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean olvidarHechizo(int hechizoID) {
        if (this._encarnacion != null || this._hechizos.get(hechizoID) == null) {
            return false;
        }
        int antNivel = this._hechizos.get(hechizoID).getNivel();
        if (antNivel <= 1) {
            return false;
        }
        if (this.aprenderHechizo(hechizoID, 1, false, false)) {
            this._puntosHechizo += Fórmulas.costeHechizo(antNivel);
            return true;
        }
        return false;
    }

    public String stringListaHechizos() {
        if (this._encarnacion != null) {
            return this._encarnacion.stringListaHechizos();
        }
        StringBuffer str = new StringBuffer();
        for (Spell.StatsHechizos SH : this._hechizos.values()) {
            if (this._lugaresHechizos.get(SH.getHechizoID()) == null) {
                str.append(String.valueOf(SH.getHechizoID()) + "~" + SH.getNivel() + "~_;");
                continue;
            }
            str.append(String.valueOf(SH.getHechizoID()) + "~" + SH.getNivel() + "~" + this._lugaresHechizos.get(SH.getHechizoID()) + ";");
        }
        return str.toString();
    }

    public void setPosHechizo(int hechizo, char pos) {
        if (this._encarnacion != null) {
            this._encarnacion.setPosHechizo(hechizo, pos);
            return;
        }
        this.reemplazarHechizoEnPos(pos);
        this._lugaresHechizos.remove(hechizo);
        this._lugaresHechizos.put(hechizo, Character.valueOf(pos));
    }

    private void reemplazarHechizoEnPos(char pos) {
        for (int key : this._hechizos.keySet()) {
            if (this._lugaresHechizos.get(key) == null || !this._lugaresHechizos.get(key).equals(Character.valueOf(pos))) continue;
            this._lugaresHechizos.remove(key);
        }
    }

    public Spell.StatsHechizos getStatsHechizo(int hechizoID) {
        if (this._encarnacion != null) {
            return this._encarnacion.getStatsHechizo(hechizoID);
        }
        return this._hechizos.get(hechizoID);
    }

    public String stringParaListaPJsServer() {
        StringBuffer str = new StringBuffer("|");
        str.append(String.valueOf(this._ID) + ";");
        str.append(String.valueOf(this._nombre) + ";");
        str.append(String.valueOf(this._nivel) + ";");
        str.append(String.valueOf(this._gfxID) + ";");
        str.append(String.valueOf(this._color1 != -1 ? Integer.toHexString(this._color1) : "-1") + ";");
        str.append(String.valueOf(this._color2 != -1 ? Integer.toHexString(this._color2) : "-1") + ";");
        str.append(String.valueOf(this._color3 != -1 ? Integer.toHexString(this._color3) : "-1") + ";");
        str.append(String.valueOf(this.getStringAccesorios()) + ";");
        str.append(String.valueOf(this._esMercante) + ";");
        str.append(String.valueOf(LesGuardians.SERVER_ID) + ";");
        if (LesGuardians.MODO_HEROICO) {
            str.append(this._esFantasma ? Integer.valueOf(1) : "0;");
        } else {
            str.append("0;");
        }
        str.append(";");
        str.append(LesGuardians.MAX_NIVEL);
        return str.toString();
    }

    public void reiniciarCero() {
        this.revivir();
        this._nivel = LesGuardians.CONFIG_INICIAR_NIVEL;
        this._encarnacion = null;
        this._idEncarnacion = -1;
        this._oficioPublico = false;
        this._kamas = 0L;
        this._puntosHechizo = this._nivel - 1;
        this._capital = (this._nivel - 1) * 5;
        this._alineacion = 0;
        this._honor = 0;
        this._deshonor = 0;
        this._nivelAlineacion = 1;
        this._energia = 10000;
        this._experiencia = World.getExpMinPersonaje(this._nivel);
        this._montura = null;
        this._talla = 100;
        this._gfxID = Integer.parseInt(String.valueOf(this._clase) + this._sexo);
        this._xpDonadaMontura = 0;
        this._mostrarConeccionAmigos = false;
        this._mostrarAlas = false;
        this._canales = "*#%!pi$:?";
        this._mapa = World.getMapa(Constantes.getMapaInicio(this._clase));
        this._celda = this._mapa.getCelda((short)340);
        this._puntoSalvado = "7411,311";
        this._cambiarNombre = false;
        this._objetos.clear();
        this._tienda.clear();
        this._esMercante = 0;
        this._PDV = this._PDVMAX = (this._nivel - 1) * 5 + (this._nivel > 200 ? (this._nivel - 200) * (this._clase == 11 ? 2 : 1) * 5 : 0) + Constantes.getBasePDV(this._clase) + this.getTotalStats().getEfecto(125);
        this._hechizos.clear();
        this._hechizos = Constantes.getHechizosIniciales(this._clase);
        for (int a = 1; a <= this._nivel; ++a) {
            Constantes.subirNivelAprenderHechizos(this, a);
        }
        this._lugaresHechizos = Constantes.getLugaresHechizosIniciales(this._clase);
        this._exPdv = this._PDV;
        this._statsOficios.clear();
        this._titulo = 0;
        this._restriccionesA = 8192;
        this._restriccionesB = 0;
        this.efectuarRestriccionesA();
        this.efectuarRestriccionesB();
        this._mascota = null;
        this._baseStats._statsEfecto.clear();
        this._baseStats = new Stats(this._baseStats._statsEfecto, true, this);
        this._scrollAgilidad = 0;
        this._scrollFuerza = 0;
        this._scrollInteligencia = 0;
        this._scrollSabiduria = 0;
        this._scrollSuerte = 0;
        this._scrollVitalidad = 0;
    }

    public void mostrarAmigosEnLinea(boolean mostrar) {
        this._mostrarConeccionAmigos = mostrar;
    }

    public static void setTitle(String title) {
        AnsiConsole.out.printf("%c]0;%s%c", Character.valueOf('\u001b'), title, Character.valueOf('\u0007'));
    }

    public void mostrarRates() {
        SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION(this, String.valueOf(LesGuardians.NOME_SERVIDOR) + "\n<b>Divers\u00e3o garantida !</b> \n\n" + "Servidor 24/7. Completo e com atualiza\u00e7\u00f5es peri\u00f3dicas para sua melhor jogabilidade. ");
    }

    public void RATES() {
        SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION(this, "<b>Evento Atual : " + LesGuardians.EVENTO + "\n<b>Rates do Servidor</b>" + "\nKAMAS : " + LesGuardians.RATE_KAMAS + "   \nDROP : " + LesGuardians.RATE_DROP + "\nEXP : " + LesGuardians.RATE_XP_PVM + "   \nPVP : " + LesGuardians.RATE_XP_PVP + "\nPROF : " + LesGuardians.RATE_XP_PROF + "  \nForja : " + LesGuardians.RATE_PORC_FM + "</b>");
    }

    public void INFOS() {
        long enLinea = System.currentTimeMillis() - LesGuardians._servidorPersonaje.getTiempoInicio();
        int hora = (int)(enLinea / 3600000L);
        int minuto = (int)((enLinea %= 3600000L) / 60000L);
        int segundo = (int)((enLinea %= 60000L) / 1000L);
        SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION(this, String.valueOf(LesGuardians.NOME_SERVIDOR) + "\n" + "\nDivers\u00e3o garantida !\n" + "\nJogadores Online : " + LesGuardians._servidorPersonaje.nroJugadoresLinea() + "\nM\u00e1ximo de Conex\u00f5es : " + LesGuardians._servidorPersonaje.getRecordJugadores() + "   </b>" + "\nTempo Online : " + hora + "H " + minuto + "M " + segundo + "s");
    }

    public String analizarFiguraDelPJ() {
        String packetOa = "Oa";
        packetOa = String.valueOf(packetOa) + this._ID + "|";
        packetOa = String.valueOf(packetOa) + this.getStringAccesorios();
        return packetOa;
    }

    public String stringGMmercante() {
        StringBuffer str = new StringBuffer();
        str.append(String.valueOf(this._celda.getID()) + ";");
        str.append("1;");
        str.append("0;");
        str.append(String.valueOf(this._ID) + ";");
        str.append(String.valueOf(this._nombre) + ";");
        str.append("-5");
        str.append(this.getTitulo() > 0 ? "," + this.getTitulo() + ";" : ";");
        str.append(String.valueOf(this._gfxID) + "^" + this._talla + ";");
        str.append(String.valueOf(this._color1 == -1 ? "-1" : Integer.toHexString(this._color1)) + ";");
        str.append(String.valueOf(this._color2 == -1 ? "-1" : Integer.toHexString(this._color2)) + ";");
        str.append(String.valueOf(this._color3 == -1 ? "-1" : Integer.toHexString(this._color3)) + ";");
        str.append(String.valueOf(this.getStringAccesorios()) + ";");
        if (this._miembroGremio != null && this._miembroGremio.getGremio().getPjMiembros().size() > 9) {
            str.append(String.valueOf(this._miembroGremio.getGremio().getNombre()) + ";" + this._miembroGremio.getGremio().getEmblema() + ";");
        } else {
            str.append(";;");
        }
        str.append("0");
        return str.toString();
    }

    public String stringGM() {
        StringBuffer str = new StringBuffer();
        if (this._pelea != null) {
            return "";
        }
        str.append(String.valueOf(this._celda.getID()) + ";");
        str.append(String.valueOf(this._orientacion) + ";");
        str.append("0;");
        str.append(String.valueOf(this._ID) + ";");
        str.append(String.valueOf(this._nombre) + ";");
        str.append(this._clase);
        str.append(this._titulo > 0 ? "," + this._titulo + ";" : ";");
        str.append(String.valueOf(this._gfxID) + "^" + this._talla + ";");
        str.append(String.valueOf(this._sexo) + ";");
        str.append(String.valueOf(this._alineacion) + ",");
        str.append(String.valueOf(this.getNivelAlineacion()) + ",");
        str.append((this._mostrarAlas ? Integer.valueOf(this.getNivelAlineacion()) : "0") + ",");
        str.append(String.valueOf(this._nivel) + ",");
        str.append(String.valueOf(this._deshonor > 0 ? 1 : 0) + ";");
        str.append(String.valueOf(this._color1 == -1 ? "-1" : Integer.toHexString(this._color1)) + ";");
        str.append(String.valueOf(this._color2 == -1 ? "-1" : Integer.toHexString(this._color2)) + ";");
        str.append(String.valueOf(this._color3 == -1 ? "-1" : Integer.toHexString(this._color3)) + ";");
        str.append(String.valueOf(this.getStringAccesorios()) + ";");
        if (LesGuardians.AURA_ATIVADA) {
            str.append(String.valueOf(this._nivel > 99 ? 1 : (this._nivel > 199 ? 2 : (this.tieneObjSetVampirico() ? 3 : 0))) + ";");
        } else {
            str.append("0;");
        }
        str.append(";");
        str.append(";");
        if (this._miembroGremio != null && this._miembroGremio.getGremio().getPjMiembros().size() > 9) {
            str.append(String.valueOf(this._miembroGremio.getGremio().getNombre()) + ";" + this._miembroGremio.getGremio().getEmblema() + ";");
        } else {
            str.append(";;");
        }
        str.append(String.valueOf(Integer.toString(this.getRestriccionesB(), 36)) + ";");
        str.append(String.valueOf(this._montando && this._montura != null ? this._montura.getStringColor(this.stringColorDue\u00f1oPavo()) : "") + ";");
        str.append(";");
        return str.toString();
    }

    public String getStringAccesorios() {
        StringBuffer str = new StringBuffer();
        str.append(String.valueOf(this.getModeloObjEnPos(1)) + ",");
        str.append(String.valueOf(this.getModeloObjEnPos(6)) + ",");
        str.append(String.valueOf(this.getModeloObjEnPos(7)) + ",");
        str.append(String.valueOf(this.getModeloObjEnPos(8)) + ",");
        str.append(this.getModeloObjEnPos(15));
        return str.toString();
    }

    public String stringStatsPacket() {
        Fight.Luchador luchador;
        Stats objEquipStats = this.getStatsObjEquipados();
        Stats totalStats = this.getTotalStats();
        Stats boostStats = this.getStatsBoost();
        Stats benMaldStats = this.getStatsBendMald();
        this.refrescarVida();
        StringBuffer str = new StringBuffer("As");
        str.append(String.valueOf(this.xpString(",")) + "|");
        str.append(String.valueOf(this._kamas) + "|");
        if (this._encarnacion != null) {
            str.append("0|0|");
        } else {
            str.append(String.valueOf(this._capital) + "|" + this._puntosHechizo + "|");
        }
        str.append(String.valueOf(this._alineacion) + "~" + this._alineacion + "," + this._nivelAlineacion + "," + this.getNivelAlineacion() + "," + this._honor + "," + this._deshonor + "," + (this._mostrarAlas ? "1" : "0") + "|");
        int pdv = this.getPDV();
        int pdvMax = this.getPDVMAX();
        if (this._pelea != null && (luchador = this._pelea.getLuchadorPorPJ(this)) != null) {
            pdv = luchador.getPDVConBuff();
            pdvMax = luchador.getPDVMaxConBuff();
        }
        str.append(String.valueOf(pdv) + "," + pdvMax + "|");
        str.append(String.valueOf(this._energia) + ",10000|");
        str.append(String.valueOf(this.getIniciativa()) + "|");
        str.append(String.valueOf(totalStats.getEfecto(176)) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(111)) + "," + objEquipStats.getEfecto(111) + "," + benMaldStats.getEfecto(111) + "," + boostStats.getEfecto(111) + "," + totalStats.getEfecto(111) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(128)) + "," + objEquipStats.getEfecto(128) + "," + benMaldStats.getEfecto(128) + "," + boostStats.getEfecto(128) + "," + totalStats.getEfecto(128) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(118)) + "," + objEquipStats.getEfecto(118) + "," + benMaldStats.getEfecto(118) + "," + boostStats.getEfecto(118) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(125)) + "," + objEquipStats.getEfecto(125) + "," + benMaldStats.getEfecto(125) + "," + boostStats.getEfecto(125) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(124)) + "," + objEquipStats.getEfecto(124) + "," + benMaldStats.getEfecto(124) + "," + boostStats.getEfecto(124) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(123)) + "," + objEquipStats.getEfecto(123) + "," + benMaldStats.getEfecto(123) + "," + boostStats.getEfecto(123) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(119)) + "," + objEquipStats.getEfecto(119) + "," + benMaldStats.getEfecto(119) + "," + boostStats.getEfecto(119) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(126)) + "," + objEquipStats.getEfecto(126) + "," + benMaldStats.getEfecto(126) + "," + boostStats.getEfecto(126) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(117)) + "," + objEquipStats.getEfecto(117) + "," + benMaldStats.getEfecto(117) + "," + boostStats.getEfecto(117) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(182)) + "," + objEquipStats.getEfecto(182) + "," + benMaldStats.getEfecto(182) + "," + boostStats.getEfecto(182) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(112)) + "," + objEquipStats.getEfecto(112) + "," + benMaldStats.getEfecto(112) + "," + boostStats.getEfecto(112) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(142)) + "," + objEquipStats.getEfecto(142) + "," + benMaldStats.getEfecto(142) + "," + boostStats.getEfecto(142) + "|");
        str.append("0,0,0,0|");
        str.append(String.valueOf(this._baseStats.getEfecto(138)) + "," + objEquipStats.getEfecto(138) + "," + benMaldStats.getEfecto(138) + "," + boostStats.getEfecto(138) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(178)) + "," + objEquipStats.getEfecto(178) + "," + benMaldStats.getEfecto(178) + "," + boostStats.getEfecto(178) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(225)) + "," + objEquipStats.getEfecto(225) + "," + benMaldStats.getEfecto(225) + "," + boostStats.getEfecto(225) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(226)) + "," + objEquipStats.getEfecto(226) + "," + benMaldStats.getEfecto(226) + "," + boostStats.getEfecto(226) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(220)) + "," + objEquipStats.getEfecto(220) + "," + benMaldStats.getEfecto(220) + "," + boostStats.getEfecto(220) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(115)) + "," + objEquipStats.getEfecto(115) + "," + benMaldStats.getEfecto(115) + "," + boostStats.getEfecto(115) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(122)) + "," + objEquipStats.getEfecto(122) + "," + benMaldStats.getEfecto(122) + "," + boostStats.getEfecto(122) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(160)) + "," + objEquipStats.getEfecto(160) + "," + 0 + "," + benMaldStats.getEfecto(160) + "," + boostStats.getEfecto(160) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(161)) + "," + objEquipStats.getEfecto(161) + "," + 0 + "," + benMaldStats.getEfecto(161) + "," + boostStats.getEfecto(161) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(241)) + "," + objEquipStats.getEfecto(241) + "," + 0 + "," + benMaldStats.getEfecto(241) + "," + boostStats.getEfecto(241) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(214)) + "," + objEquipStats.getEfecto(214) + "," + 0 + "," + benMaldStats.getEfecto(214) + "," + boostStats.getEfecto(214) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(264)) + "," + objEquipStats.getEfecto(264) + "," + 0 + "," + benMaldStats.getEfecto(264) + "," + boostStats.getEfecto(264) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(254)) + "," + objEquipStats.getEfecto(254) + "," + 0 + "," + benMaldStats.getEfecto(254) + "," + boostStats.getEfecto(254) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(242)) + "," + objEquipStats.getEfecto(242) + "," + 0 + "," + benMaldStats.getEfecto(242) + "," + boostStats.getEfecto(242) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(210)) + "," + objEquipStats.getEfecto(210) + "," + 0 + "," + benMaldStats.getEfecto(210) + "," + boostStats.getEfecto(210) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(260)) + "," + objEquipStats.getEfecto(260) + "," + 0 + "," + benMaldStats.getEfecto(260) + "," + boostStats.getEfecto(260) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(250)) + "," + objEquipStats.getEfecto(250) + "," + 0 + "," + benMaldStats.getEfecto(250) + "," + boostStats.getEfecto(250) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(243)) + "," + objEquipStats.getEfecto(243) + "," + 0 + "," + benMaldStats.getEfecto(243) + "," + boostStats.getEfecto(243) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(211)) + "," + objEquipStats.getEfecto(211) + "," + 0 + "," + benMaldStats.getEfecto(211) + "," + boostStats.getEfecto(211) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(261)) + "," + objEquipStats.getEfecto(261) + "," + 0 + "," + benMaldStats.getEfecto(261) + "," + boostStats.getEfecto(261) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(251)) + "," + objEquipStats.getEfecto(251) + "," + 0 + "," + benMaldStats.getEfecto(251) + "," + boostStats.getEfecto(251) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(244)) + "," + objEquipStats.getEfecto(244) + "," + 0 + "," + benMaldStats.getEfecto(244) + "," + boostStats.getEfecto(244) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(212)) + "," + objEquipStats.getEfecto(212) + "," + 0 + "," + benMaldStats.getEfecto(212) + "," + boostStats.getEfecto(212) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(262)) + "," + objEquipStats.getEfecto(262) + "," + 0 + "," + benMaldStats.getEfecto(262) + "," + boostStats.getEfecto(262) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(252)) + "," + objEquipStats.getEfecto(252) + "," + 0 + "," + benMaldStats.getEfecto(252) + "," + boostStats.getEfecto(252) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(240)) + "," + objEquipStats.getEfecto(240) + "," + 0 + "," + benMaldStats.getEfecto(240) + "," + boostStats.getEfecto(240) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(213)) + "," + objEquipStats.getEfecto(213) + "," + 0 + "," + benMaldStats.getEfecto(213) + "," + boostStats.getEfecto(213) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(263)) + "," + objEquipStats.getEfecto(263) + "," + 0 + "," + benMaldStats.getEfecto(263) + "," + boostStats.getEfecto(263) + "|");
        str.append(String.valueOf(this._baseStats.getEfecto(253)) + "," + objEquipStats.getEfecto(253) + "," + 0 + "," + benMaldStats.getEfecto(253) + "," + boostStats.getEfecto(253) + "|");
        return str.toString();
    }

    public String xpString(String c) {
        return String.valueOf(this._experiencia) + c + World.getExpMinPersonaje(this._nivel) + c + World.getExpMaxPersonaje(this._nivel);
    }

    public int emoteActivado() {
        return this._emoteActivado;
    }

    public void setEmoteActivado(int emoteActivado) {
        this._emoteActivado = emoteActivado;
    }

    private Stats getStatsObjEquipados() {
        Stats stats = new Stats(false, null);
        ArrayList<Integer> listaSetsEquipados = new ArrayList<Integer>();
        Collection<Objeto> objetos = this._objetos.values();
        for (Objeto objeto : objetos) {
            if (objeto.getPosicion() == -1 || (objeto.getPosicion() < 0 || objeto.getPosicion() > 15) && (objeto.getPosicion() < 20 || objeto.getPosicion() > 27)) continue;
            stats = Stats.acumularStats(stats, objeto.getStats());
            int setID = objeto.getModelo().getSetID();
            if (setID <= 0 || listaSetsEquipados.contains(setID)) continue;
            listaSetsEquipados.add(setID);
            World.ObjetoSet IS = World.getObjetoSet(setID);
            if (IS == null) continue;
            stats = Stats.acumularStats(stats, IS.getBonusStatPorNroObj(this.getNroObjEquipadosDeSet(setID)));
        }
        if (this._montando && this._montura != null) {
            stats = Stats.acumularStats(stats, this._montura.getStats());
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
        total = Stats.acumularStats(total, this._baseStats);
        total = Stats.acumularStats(total, this.getStatsObjEquipados());
        total = Stats.acumularStats(total, this.getStatsBendMald());
        return total;
    }

    public byte getOrientacion() {
        return this._orientacion;
    }

    public void setOrientacion(byte orientacion) {
        this._orientacion = orientacion;
    }

    public int getIniciativa() {
        Stats objEquipados = this.getTotalStats();
        int fact = 4;
        int pvmax = this._PDVMAX - Constantes.getBasePDV(this._clase);
        int pv = this._PDV - Constantes.getBasePDV(this._clase);
        if (this._clase == 11) {
            fact = 8;
        }
        double coef = pvmax / fact;
        coef += (double)this.getStatsObjEquipados().getEfecto(174);
        coef += (double)objEquipados.getEfecto(119);
        coef += (double)objEquipados.getEfecto(123);
        coef += (double)objEquipados.getEfecto(126);
        coef += (double)objEquipados.getEfecto(118);
        int init = 1;
        if (pvmax != 0) {
            init = (int)(coef * (double)(pv / pvmax));
        }
        if (init < 0) {
            init = 0;
        }
        return init;
    }

    public int getPodUsados() {
        int pod = 0;
        for (Objeto objeto : this._objetos.values()) {
            pod += objeto.getModelo().getPeso() * objeto.getCantidad();
        }
        return pod;
    }

    public int getMaxPod() {
        int pods = this.getTotalStats().getEfecto(158);
        pods += this.getTotalStats().getEfecto(118) * 5;
        for (Profissao.StatsOficio SO : this._statsOficios.values()) {
            pods += SO.getNivel() * 5;
            if (SO.getNivel() != 100) continue;
            pods += 1000;
        }
        if (pods < 1000) {
            pods = 1000;
        }
        return pods;
    }

    public int getPDV() {
        return this._PDV;
    }

    public void setPDV(int pdv) {
        if (pdv > this.getPDVMAX()) {
            pdv = this.getPDVMAX();
        }
        this._PDV = pdv;
        this.actualizarInfoGrupo();
    }

    public int getPDVMAX() {
        if (this._encarnacion != null) {
            return this._encarnacion.getPDVMAX();
        }
        return this._PDVMAX;
    }

    public void setPDVMAX(int pdvmax) {
        this._PDVMAX = pdvmax;
        this.actualizarInfoGrupo();
    }

    public void actualizarInfoGrupo() {
        if (this._grupo != null) {
            SocketManager.ENVIAR_PM_ACTUALIZAR_INFO_PJ_GRUPO(this._grupo, this);
        }
    }

    public void setSentado(boolean sentado) {
        this._sentado = sentado;
        int diferencia = this._PDV - this._exPdv;
        int tiempo = sentado ? 500 : 1000;
        this._exPdv = this._PDV;
        if (this._enLinea) {
            SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(this, diferencia);
            SocketManager.ENVIAR_ILS_TIEMPO_REGENERAR_VIDA(this, tiempo);
        }
        this._recuperarVida.setDelay(tiempo);
        if (!(this._emoteActivado != 1 && this._emoteActivado != 19 || sentado)) {
            this._emoteActivado = 0;
        }
    }

    public void stopRecuperarVida() {
        this._recuperarVida.stop();
    }

    public byte getAlineacion() {
        return this._alineacion;
    }

    public int getPorcPDV() {
        int porcPDV = 100;
        porcPDV = 100 * this._PDV / this._PDVMAX;
        if (porcPDV > 100) {
            return 100;
        }
        return porcPDV;
    }

    public void emote(String str) {
        try {
            int id = Integer.parseInt(str);
            Maps mapa = this._mapa;
            if (this._pelea == null) {
                SocketManager.ENVIAR_cS_EMOTICON_MAPA(mapa, this._ID, id);
            } else {
                SocketManager.ENVIAR_cS_EMOTE_EN_PELEA(this._pelea, 7, this._ID, id);
            }
        }
        catch (NumberFormatException e) {
            return;
        }
    }

    public void retornoMapa() {
        this._pelea = null;
        this._ocupado = false;
        this._listo = false;
        this._dueloID = -1;
        this._mapa.addJugador(this);
    }

    public void retornoMapaDesPeleaRecau() {
        this._pelea = null;
        this._ocupado = false;
        this._listo = false;
        this._dueloID = -1;
        try {
            this.teleport(this._tempMapaDefPerco.getID(), this._tempCeldaDefPerco.getID());
        }
        catch (NullPointerException E) {
            this.teleport(this._mapa.getID(), this._celda.getID());
        }
        this._tempMapaDefPerco = null;
        this._tempCeldaDefPerco = null;
    }

    public void retornoPtoSalvadaRecau() {
        this._pelea = null;
        this._ocupado = false;
        this._listo = false;
        this._dueloID = -1;
        if (this._energia > 0) {
            String[] infos = this._puntoSalvado.split(",");
            this.teleport(Short.parseShort(infos[0]), Short.parseShort(infos[1]));
        }
        this._tempMapaDefPerco = null;
        this._tempCeldaDefPerco = null;
        try {
            Thread.sleep(1000L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(this);
    }

    public void retornoPtoSalvada(boolean teleportar) {
        this._pelea = null;
        this._ocupado = false;
        this._dueloID = -1;
        if (this._energia > 0 && teleportar) {
            String[] infos = this._puntoSalvado.split(",");
            this.teleport(Short.parseShort(infos[0]), Short.parseShort(infos[1]));
        }
        try {
            Thread.sleep(1000L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        SocketManager.ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(this);
    }

    public void retornoPtoSalvadaPocima() {
        this._pelea = null;
        this._ocupado = false;
        this._listo = false;
        this._dueloID = -1;
        try {
            String[] infos = this._puntoSalvado.split(",");
            this.teleport(Short.parseShort(infos[0]), Short.parseShort(infos[1]));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void boostStat(int stat) {
        int valor = 0;
        switch (stat) {
            case 10: {
                valor = this._baseStats.getEfecto(118);
                break;
            }
            case 13: {
                valor = this._baseStats.getEfecto(123);
                break;
            }
            case 14: {
                valor = this._baseStats.getEfecto(119);
                break;
            }
            case 15: {
                valor = this._baseStats.getEfecto(126);
                break;
            }
        }
        int cantidad = Constantes.getRepartoPuntoSegunClase(this._clase, stat, valor);
        if (cantidad <= this._capital) {
            switch (stat) {
                case 11: {
                    if (this._clase != 11) {
                        this._baseStats.addStat(125, 1);
                        break;
                    }
                    this._baseStats.addStat(125, 2);
                    break;
                }
                case 12: {
                    this._baseStats.addStat(124, 1);
                    break;
                }
                case 10: {
                    this._baseStats.addStat(118, 1);
                    break;
                }
                case 13: {
                    this._baseStats.addStat(123, 1);
                    break;
                }
                case 14: {
                    this._baseStats.addStat(119, 1);
                    break;
                }
                case 15: {
                    this._baseStats.addStat(126, 1);
                    break;
                }
                default: {
                    return;
                }
            }
            this._capital -= cantidad;
            SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
        }
    }

    public void boostStat2(int stat) {
        int valor = 0;
        int capital = this._capital;
        int cantidad = 0;
        while (capital >= cantidad) {
            switch (stat) {
                case 10: {
                    valor = this._baseStats.getEfecto(118);
                    break;
                }
                case 13: {
                    valor = this._baseStats.getEfecto(123);
                    break;
                }
                case 14: {
                    valor = this._baseStats.getEfecto(119);
                    break;
                }
                case 15: {
                    valor = this._baseStats.getEfecto(126);
                    break;
                }
            }
            cantidad = Constantes.getRepartoPuntoSegunClase(this._clase, stat, valor);
            if (cantidad > this._capital) continue;
            switch (stat) {
                case 11: {
                    if (this._clase != 11) {
                        this._baseStats.addStat(125, 1);
                        break;
                    }
                    this._baseStats.addStat(125, 2);
                    break;
                }
                case 12: {
                    this._baseStats.addStat(124, 1);
                    break;
                }
                case 10: {
                    this._baseStats.addStat(118, 1);
                    break;
                }
                case 13: {
                    this._baseStats.addStat(123, 1);
                    break;
                }
                case 14: {
                    this._baseStats.addStat(119, 1);
                    break;
                }
                case 15: {
                    this._baseStats.addStat(126, 1);
                    break;
                }
                default: {
                    return;
                }
            }
            capital -= cantidad;
        }
        this._capital = capital;
        SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
    }

    public void boostStatFixedCount(int stat, int countVal) {
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("Perso " + this._nombre + ": tentative de boost stat " + stat + " " + countVal + " fois");
        }
        for (int i = 0; i < countVal; ++i) {
            int value = 0;
            switch (stat) {
                case 10: {
                    value = this._baseStats.getEfecto(118);
                    break;
                }
                case 13: {
                    value = this._baseStats.getEfecto(123);
                    break;
                }
                case 14: {
                    value = this._baseStats.getEfecto(119);
                    break;
                }
                case 15: {
                    value = this._baseStats.getEfecto(126);
                }
            }
            int cout = Constantes.getRepartoPuntoSegunClase(this._clase, stat, value);
            if (cout > this._capital) continue;
            switch (stat) {
                case 11: {
                    if (this._clase != 11) {
                        this._baseStats.addStat(125, 1);
                        break;
                    }
                    this._baseStats.addStat(125, 2);
                    break;
                }
                case 12: {
                    this._baseStats.addStat(124, 1);
                    break;
                }
                case 10: {
                    this._baseStats.addStat(118, 1);
                    break;
                }
                case 13: {
                    this._baseStats.addStat(123, 1);
                    break;
                }
                case 14: {
                    this._baseStats.addStat(119, 1);
                    break;
                }
                case 15: {
                    this._baseStats.addStat(126, 1);
                    break;
                }
                default: {
                    return;
                }
            }
            this._capital -= cout;
        }
        SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
        SQLManager.SALVAR_PERSONAJE(this, true);
    }

    public boolean estaMuteado() {
        return this._cuenta.estaMuteado();
    }

    public void setMapa(Maps mapa) {
        this._mapa = mapa;
    }

    public String stringObjetosABD() {
        StringBuffer str = new StringBuffer();
        for (Map.Entry<Integer, Objeto> entry : this._objetos.entrySet()) {
            Objeto obj = entry.getValue();
            str.append(String.valueOf(obj.getID()) + "|");
        }
        return str.toString();
    }

    public Objeto getObjSimilarInventario(Objeto objeto) {
        Objeto.ObjetoModelo objModelo = objeto.getModelo();
        if (objModelo.getTipo() == 85 || objModelo.getTipo() == 18 || LesGuardians.ARMAS_ENCARNACAO.contains(objModelo.getID())) {
            return null;
        }
        for (Map.Entry<Integer, Objeto> entry : this._objetos.entrySet()) {
            Objeto obj = entry.getValue();
            if (obj.getPosicion() != -1 || objeto.getID() == obj.getID() || obj.getModelo().getID() != objModelo.getID() || !obj.getStats().sonStatsIguales(objeto.getStats())) continue;
            return obj;
        }
        return null;
    }

    public boolean addObjetoSimilar(Objeto objeto, boolean tieneSimilar, int idAntigua) {
        Objeto.ObjetoModelo objModelo = objeto.getModelo();
        if (objModelo.getTipo() == 85 || objModelo.getTipo() == 18 || LesGuardians.ARMAS_ENCARNACAO.contains(objModelo.getID())) {
            return false;
        }
        if (tieneSimilar) {
            for (Map.Entry<Integer, Objeto> entry : this._objetos.entrySet()) {
                Objeto obj = entry.getValue();
                if (obj.getPosicion() != -1 || obj.getID() == idAntigua || obj.getModelo().getID() != objModelo.getID() || !obj.getStats().sonStatsIguales(objeto.getStats())) continue;
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
        this._objetos.put(objeto.getID(), objeto);
    }

    public Map<Integer, Objeto> getObjetos() {
        return this._objetos;
    }

    public String stringPersonajeElegido() {
        StringBuffer str = new StringBuffer();
        Objeto objeto = this.getObjPosicion(21);
        if (this.getObjPosicion(21) != null) {
            String[] arg;
            String stats = objeto.convertirStatsAString();
            String[] arrstring = arg = stats.split(",");
            int n = arg.length;
            for (int i = 0; i < n; ++i) {
                String efec = arrstring[i];
                String[] val = efec.split("#");
                int efecto = Integer.parseInt(val[0], 16);
                if (efecto < 281 && efecto > 292) continue;
                this._bendEfecto = efecto;
                this._bendHechizo = Integer.parseInt(val[1], 16);
                this._bendModif = Integer.parseInt(val[3], 16);
            }
        }
        for (Objeto obj : this._objetos.values()) {
            str.append(obj.stringObjetoConGui\u00f1o());
        }
        return str.toString();
    }

    public String getObjetosBancoPorID(String splitter) {
        StringBuffer str = new StringBuffer();
        for (int entry : this._cuenta.getObjetosBanco().keySet()) {
            if (str.length() != 0) {
                str.append(splitter);
            }
            str.append(entry);
        }
        return str.toString();
    }

    public String getObjetosPersonajePorID(String splitter) {
        StringBuffer str = new StringBuffer();
        for (int entry : this._objetos.keySet()) {
            if (str.length() != 0) {
                str.append(splitter);
            }
            str.append(entry);
        }
        return str.toString();
    }

    public boolean tieneObjetoID(int id) {
        return this._objetos.get(id) != null;
    }

    public void venderObjeto(int id, int cant) {
        if (cant <= 0) {
            return;
        }
        Objeto objeto = this._objetos.get(id);
        Objeto.ObjetoModelo objModelo = objeto.getModelo();
        int precioUnitario = objModelo.getPrecio();
        int precioVIP = objModelo.getPrecioVIP();
        if (precioUnitario == 0 && precioVIP > 0) {
            int ptosAconseguir = cant * precioVIP;
            int misPuntos = SQLManager.getPuntosCuenta(this._cuenta.getID());
            SQLManager.setPuntoCuenta(ptosAconseguir + misPuntos, this._cuenta.getID());
            int nuevaCant = objeto.getCantidad() - cant;
            if (nuevaCant <= 0) {
                this._objetos.remove(id);
                World.eliminarObjeto(id);
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
                this._objetos.remove(id);
                World.eliminarObjeto(id);
                SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, id);
            } else {
                objeto.setCantidad(nuevaCant);
                SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objeto);
            }
            this._kamas += (long)precio;
        }
        SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this);
        SocketManager.ENVIAR_ESK_VENDIDO(this);
    }

    public void borrarObjetoSinEliminar(int id) {
        this._objetos.remove(id);
    }

    public void borrarObjetoEliminar(int idObjeto, int cantidad, boolean borrarMundoDofus) {
        Objeto obj = this._objetos.get(idObjeto);
        if (cantidad > obj.getCantidad()) {
            cantidad = obj.getCantidad();
        }
        if (obj.getCantidad() >= cantidad) {
            int nuevaCant = obj.getCantidad() - cantidad;
            if (nuevaCant > 0) {
                obj.setCantidad(nuevaCant);
                if (this._enLinea) {
                    SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, obj);
                }
            } else {
                this._objetos.remove(obj.getID());
                if (borrarMundoDofus) {
                    World.eliminarObjeto(obj.getID());
                }
                if (this._enLinea) {
                    SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, obj.getID());
                }
            }
        }
    }

    public Objeto getObjPosicion(int pos) {
        if (pos == -1) {
            return null;
        }
        for (Objeto objeto : this._objetos.values()) {
            if (objeto.getPosicion() != pos) continue;
            return objeto;
        }
        return null;
    }

    public void refrescarVida() {
        double actPdvPorc = 100.0 * (double)this._PDV / (double)this._PDVMAX;
        this._PDVMAX = this._encarnacion != null ? this._encarnacion.getPDVMAX() : (this._nivel - 1) * 5 + (this._nivel > 200 ? (this._nivel - 200) * (this._clase == 11 ? 2 : 1) * 5 : 0) + Constantes.getBasePDV(this._clase) + this.getTotalStats().getEfecto(125);
        this._PDV = (int)Math.round((double)this._PDVMAX * actPdvPorc / 100.0);
    }

    public void subirNivel(boolean addXp) {
        if (this._nivel == LesGuardians.MAX_NIVEL || this._encarnacion != null) {
            return;
        }
        ++this._nivel;
        this._capital += 5;
        this._PDVMAX += 5;
        if (this._nivel > 200) {
            this._PDVMAX += 5;
        }
        ++this._puntosHechizo;
        if (this._nivel == LesGuardians.NIVEL_PA1) {
            this._baseStats.addStat(111, LesGuardians.MAX_PA1);
        }
        Constantes.subirNivelAprenderHechizos(this, this._nivel);
        if (addXp) {
            this._experiencia = World.getExpNivel((int)this._nivel)._personaje;
        }
        this._PDV = this._PDVMAX;
    }

    public void addExp(long xp) {
        if (this._encarnacion != null) {
            this._encarnacion.addExp(xp, this);
            return;
        }
        this._experiencia += xp;
        int exNivel = this._nivel;
        while (this._experiencia >= World.getExpMaxPersonaje(this._nivel) && this._nivel < LesGuardians.MAX_NIVEL) {
            this.subirNivel(false);
        }
        if (this._enLinea) {
            if (exNivel < this._nivel) {
                SocketManager.ENVIAR_AN_MENSAJE_NUEVO_NIVEL(this, this._nivel);
                try {
                    if (this.getGremio() != null) {
                        this.getMiembroGremio().setNivel(this._nivel);
                    }
                    SocketManager.ENVIAR_SL_LISTA_HECHIZOS(this);
                    this.actualizarInfoGrupo();
                }
                catch (NullPointerException nullPointerException) {
                    // empty catch block
                }
            }
            SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
        }
    }

    public void setIntercambio(World.Intercambio inter) {
        this._intercambio = inter;
    }

    public World.Intercambio getIntercambio() {
        return this._intercambio;
    }

    public void setTallerInvitado(World.InvitarTaller inter) {
        this._tallerInvitado = inter;
    }

    public World.InvitarTaller getTallerInvitado() {
        return this._tallerInvitado;
    }

    public int aprenderOficio(Profissao oficio) {
        for (Map.Entry<Integer, Profissao.StatsOficio> entry : this._statsOficios.entrySet()) {
            if (entry.getValue().getOficio().getID() != oficio.getID()) continue;
            return -1;
        }
        int cantOficios = this._statsOficios.size();
        if (cantOficios == 6) {
            return -1;
        }
        int pos = -1;
        if (this._statsOficios.get(0) == null) {
            pos = 0;
        } else if (this._statsOficios.get(1) == null) {
            pos = 1;
        } else if (this._statsOficios.get(2) == null) {
            pos = 2;
        } else if (this._statsOficios.get(3) == null) {
            pos = 3;
        } else if (this._statsOficios.get(4) == null) {
            pos = 4;
        } else if (this._statsOficios.get(5) == null) {
            pos = 5;
        }
        if (pos == -1) {
            return -1;
        }
        Profissao.StatsOficio statOficio = new Profissao.StatsOficio(pos, oficio, 1, 0L);
        this._statsOficios.put(pos, statOficio);
        if (this._enLinea) {
            ArrayList<Profissao.StatsOficio> list = new ArrayList<Profissao.StatsOficio>();
            list.add(statOficio);
            SocketManager.ENVIAR_Im_INFORMACION(this, "02;" + oficio.getID());
            SocketManager.ENVIAR_JS_TRABAJO_POR_OFICIO(this, list);
            SocketManager.ENVIAR_JX_EXPERINENCIA_OFICIO(this, list);
            SocketManager.ENVIAR_JO_OFICIO_OPCIONES(this, list);
            Objeto obj = this.getObjPosicion(1);
            if (obj != null && oficio.herramientaValida(obj.getModelo().getID())) {
                PrintWriter out = this._cuenta.getEntradaPersonaje().getOut();
                SocketManager.ENVIAR_OT_OBJETO_HERRAMIENTA(out, oficio.getID());
                String strOficioPub = Constantes.trabajosOficioTaller(oficio.getID());
                if (this._mapa.esTaller() && this._oficioPublico) {
                    SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(out, "+", this._ID, strOficioPub);
                }
                this._stringOficiosPublicos = strOficioPub;
            }
        }
        return pos;
    }

    public void olvidarOficio(int pos) {
        this._statsOficios.remove(pos);
    }

    public boolean tieneEquipado(int id) {
        for (Objeto objeto : this._objetos.values()) {
            if (objeto.getModelo().getID() != id || objeto.getPosicion() == -1) continue;
            return true;
        }
        return false;
    }

    private boolean tieneObjSetVampirico() {
        for (Objeto objeto : this._objetos.values()) {
            int idObjModelo;
            if (objeto.getPosicion() == -1 || (idObjModelo = objeto.getModelo().getID()) != 10054 && idObjModelo != 10055 && idObjModelo != 10056 && idObjModelo != 10058 && idObjModelo != 10061 && idObjModelo != 10102) continue;
            return true;
        }
        return false;
    }

    public void setInvitado(int invitando) {
        this._invitando = invitando;
    }

    public int getInvitado() {
        return this._invitando;
    }

    public String stringInfoGrupo() {
        StringBuffer str = new StringBuffer();
        str.append(String.valueOf(this._ID) + ";");
        str.append(String.valueOf(this._nombre) + ";");
        str.append(String.valueOf(this._gfxID) + ";");
        str.append(String.valueOf(this._color1) + ";");
        str.append(String.valueOf(this._color2) + ";");
        str.append(String.valueOf(this._color3) + ";");
        str.append(String.valueOf(this.getStringAccesorios()) + ";");
        str.append(String.valueOf(this._PDV) + "," + this._PDVMAX + ";");
        str.append(String.valueOf(this._nivel) + ";");
        str.append(String.valueOf(this.getIniciativa()) + ";");
        str.append(String.valueOf(this.getTotalStats().getEfecto(176)) + ";");
        str.append("1");
        return str.toString();
    }

    public int getNroObjEquipadosDeSet(int setID) {
        int nro = 0;
        for (Objeto objeto : this._objetos.values()) {
            if (objeto.getPosicion() == -1 || objeto.getModelo().getSetID() != setID) continue;
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
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (celdaID == -1 || accion == -1) {
            return;
        }
        if (!this._mapa.getCelda(celdaID).puedeHacerAccion(accion, this._pescarKuakua)) {
            return;
        }
        this._mapa.getCelda(celdaID).iniciarAccion(this, GA);
    }

    public void finalizarAccionEnCelda(GameThread.AccionDeJuego AJ) {
        short celdaID = -1;
        try {
            celdaID = Short.parseShort(AJ._args.split(";")[0]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (celdaID == -1 || AJ == null || this._mapa.getCelda(celdaID) == null) {
            return;
        }
        this._mapa.getCelda(celdaID).finalizarAccion(this, AJ);
    }

    public void teleport(short nuevoMapaID, short nuevaCeldaID) {
        Maps nuevoMapa;
        if (this._tutorial != null || this._accionTrabajo != null) {
            return;
        }
        if (!this._huir) {
            if (System.currentTimeMillis() - this._tiempoAgre > 8000L) {
                this._huir = true;
            } else {
                SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this, "Voc\u00ea n\u00e3o pode se teleportar, espere 10 segundos.");
                return;
            }
        }
        if ((nuevoMapa = World.getMapa(nuevoMapaID)) == null || nuevoMapa.getCelda(nuevaCeldaID) == null) {
            return;
        }
        if (nuevoMapa.esTaller() && nuevoMapa.getPersos().size() > LesGuardians.LIMITE_ARTESAOS_OFIC) {
            SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this, "Voc\u00ea n\u00e3o pode entrar na loja, porque est\u00e1 cheia. Por Favor volte mais tarde ou tente outra loja.");
            nuevoMapaID = (short)951;
            nuevaCeldaID = (short)340;
            nuevoMapa = World.getMapa(nuevoMapaID);
        }
        PrintWriter out = null;
        if (this._cuenta.getEntradaPersonaje() != null) {
            out = this._cuenta.getEntradaPersonaje().getOut();
        }
        if (out != null) {
            SocketManager.ENVIAR_GA2_CARGANDO_MAPA(out, this._ID);
        }
        SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(this._mapa, this._ID);
        this._celda.removerPersonaje(this._ID);
        this._mapa = nuevoMapa;
        this._celda = this._mapa.getCelda(nuevaCeldaID);
        if (out != null) {
            SocketManager.ENVIAR_GDM_CAMBIO_DE_MAPA(out, nuevoMapaID, this._mapa.getFecha(), this._mapa.getCodigo());
        }
        this._mapa.addJugador(this);
        if (!this._seguidores.isEmpty()) {
            ArrayList<Personagens> seguidores = new ArrayList<Personagens>();
            try {
                seguidores.addAll(this._seguidores.values());
            }
            catch (ConcurrentModificationException concurrentModificationException) {
                // empty catch block
            }
            for (Personagens seguido : seguidores) {
                if (seguido._enLinea) {
                    SocketManager.ENVIAR_IC_PERSONAJE_BANDERA_COMPAS(seguido, this);
                    continue;
                }
                this._seguidores.remove(seguido.getID());
            }
        }
    }

    public boolean getDefendiendo() {
        return this._defendiendo;
    }

    public void setDefendiendo(boolean def) {
        this._defendiendo = def;
    }

    public boolean teleportSinTodos(short nuevoMapaID, short nuevaCeldaID) {
        Maps nuevoMapa;
        PrintWriter out = null;
        this._defendiendo = true;
        if (this._cuenta.getEntradaPersonaje() != null) {
            out = this._cuenta.getEntradaPersonaje().getOut();
        }
        if ((nuevoMapa = World.getMapa(nuevoMapaID)) == null || nuevoMapa.getCelda(nuevaCeldaID) == null || this._celda == null) {
            return false;
        }
        if (out != null) {
            SocketManager.ENVIAR_GA2_CARGANDO_MAPA(out, this._ID);
        }
        SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(this._mapa, this._ID);
        this._celda.removerPersonaje(this._ID);
        this._mapa = nuevoMapa;
        this._celda = this._mapa.getCelda(nuevaCeldaID);
        if (out != null) {
            SocketManager.ENVIAR_GDM_CAMBIO_DE_MAPA(out, nuevoMapaID, this._mapa.getFecha(), this._mapa.getCodigo());
        }
        return true;
    }

    public void setHuir(boolean huir) {
        this._huir = huir;
    }

    public boolean getHuir() {
        return this._huir;
    }

    public long getTiempoAgre() {
        return this._tiempoAgre;
    }

    public void setTiempoAgre(long tiempo) {
        this._tiempoAgre = tiempo;
    }

    public void setAgresion(boolean agre) {
        this._agresion = agre;
    }

    public boolean getAgresion() {
        return this._agresion;
    }

    public int getCostoAbrirBanco() {
        return this._cuenta.getObjetosBanco().size();
    }

    public String getStringVar(String str) {
        if (str.equals("nombre")) {
            return this._nombre;
        }
        if (str.equals("costoBanco")) {
            return String.valueOf(this.getCostoAbrirBanco());
        }
        return "";
    }

    public void setKamasBanco(long i) {
        this._cuenta.setKamasBanco(i);
    }

    public long getKamasBanco() {
        return this._cuenta.getKamasBanco();
    }

    public void setEnBanco(boolean b) {
        this._estarBanco = b;
    }

    public boolean enBanco() {
        return this._estarBanco;
    }

    public String stringBanco() {
        String packet = "";
        for (Map.Entry<Integer, Objeto> entry : this._cuenta.getObjetosBanco().entrySet()) {
            packet = String.valueOf(packet) + "O" + entry.getValue().stringObjetoConGui\u00f1o();
        }
        if (this.getKamasBanco() != 0L) {
            packet = String.valueOf(packet) + "G" + this.getKamasBanco();
        }
        return packet;
    }

    public void addCapital(int pts) {
        this._capital += pts;
    }

    public void setCapital(int capital) {
        this._capital = capital;
    }

    public void addPuntosHechizos(int puntos) {
        this._puntosHechizo += puntos;
    }

    public void addObjAlBanco(int id, int cant) {
        Objeto objAGuardar = World.getObjeto(id);
        if (this._objetos.get(id) == null || objAGuardar.getPosicion() != -1) {
            return;
        }
        Objeto objBanco = this.getSimilarObjetoBanco(objAGuardar);
        int nuevaCant = objAGuardar.getCantidad() - cant;
        if (objBanco == null) {
            if (nuevaCant <= 0) {
                this.borrarObjetoSinEliminar(objAGuardar.getID());
                this._cuenta.getObjetosBanco().put(objAGuardar.getID(), objAGuardar);
                String str = "O+" + objAGuardar.getID() + "|" + objAGuardar.getCantidad() + "|" + objAGuardar.getModelo().getID() + "|" + objAGuardar.convertirStatsAString();
                SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
                SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, id);
            } else {
                objAGuardar.setCantidad(nuevaCant);
                objBanco = Objeto.clonarObjeto(objAGuardar, cant);
                World.addObjeto(objBanco, true);
                this._cuenta.getObjetosBanco().put(objBanco.getID(), objBanco);
                String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID() + "|" + objBanco.convertirStatsAString();
                SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
                SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objAGuardar);
            }
        } else if (nuevaCant <= 0) {
            this.borrarObjetoSinEliminar(objAGuardar.getID());
            objBanco.setCantidad(objBanco.getCantidad() + objAGuardar.getCantidad());
            String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID() + "|" + objBanco.convertirStatsAString();
            World.eliminarObjeto(objAGuardar.getID());
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
            SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, id);
        } else {
            objAGuardar.setCantidad(nuevaCant);
            objBanco.setCantidad(objBanco.getCantidad() + cant);
            String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID() + "|" + objBanco.convertirStatsAString();
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objAGuardar);
        }
        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this);
    }

    private Objeto getSimilarObjetoBanco(Objeto obj) {
        for (Objeto objeto : this._cuenta.getObjetosBanco().values()) {
            Objeto.ObjetoModelo objetoMod = objeto.getModelo();
            if (objetoMod.getTipo() == 85 || objetoMod.getID() != obj.getModelo().getID() || !objeto.getStats().sonStatsIguales(obj.getStats())) continue;
            return objeto;
        }
        return null;
    }

    public void removerDelBanco(int id, int cant) {
        Objeto objBanco = World.getObjeto(id);
        if (this._cuenta.getObjetosBanco().get(id) == null) {
            return;
        }
        Objeto objetoARecibir = this.getObjSimilarInventario(objBanco);
        int nuevaCant = objBanco.getCantidad() - cant;
        if (objetoARecibir == null) {
            if (nuevaCant <= 0) {
                this._cuenta.getObjetosBanco().remove(id);
                this._objetos.put(id, objBanco);
                SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, "O-" + id);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this, objBanco);
            } else {
                objetoARecibir = Objeto.clonarObjeto(objBanco, cant);
                objBanco.setCantidad(nuevaCant);
                World.addObjeto(objetoARecibir, true);
                this._objetos.put(objetoARecibir.getID(), objetoARecibir);
                String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID() + "|" + objBanco.convertirStatsAString();
                SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this, objetoARecibir);
            }
        } else if (nuevaCant <= 0) {
            this._cuenta.getObjetosBanco().remove(objBanco.getID());
            objetoARecibir.setCantidad(objetoARecibir.getCantidad() + objBanco.getCantidad());
            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objetoARecibir);
            World.eliminarObjeto(objBanco.getID());
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, "O-" + id);
        } else {
            objBanco.setCantidad(nuevaCant);
            objetoARecibir.setCantidad(objetoARecibir.getCantidad() + cant);
            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, objetoARecibir);
            String str = "O+" + objBanco.getID() + "|" + objBanco.getCantidad() + "|" + objBanco.getModelo().getID() + "|" + objBanco.convertirStatsAString();
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(this, str);
        }
        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this);
    }

    public void abrirCercado() {
        if (this.getDeshonor() >= 5) {
            SocketManager.ENVIAR_Im_INFORMACION(this, "183");
            return;
        }
        this._enCercado = this._mapa.getCercado();
        this._ocupado = true;
        String str = this.analizarListaDrago();
        SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(this, 16, str);
    }

    private String analizarListaDrago() { //FIXME revisar buen funcionamiento
        StringBuffer packet = new StringBuffer();
        boolean primero = false;
        if (this._cuenta.getEstablo().size() > 0) {
            for (Dragossauros DP : this._cuenta.getEstablo()) {
                if (primero) {
                    packet.append(";");
                }
                packet.append(DP.detallesMontura());
                primero = true;
            }
        }
        packet.append("~");
        if (this._enCercado.getListaCriando().size() > 0) {
            primero = false;
            Iterator<Integer> iterator = this._enCercado.getListaCriando().iterator();
            while (iterator.hasNext()) {
                int pavo = (Integer)iterator.next();
                Dragossauros dragopavo = World.getDragopavoPorID(pavo);
                if (dragopavo.getDue\u00f1o() == this._ID) {
                    if (primero) {
                        packet.append(";");
                    }
                    packet.append(dragopavo.detallesMontura());
                    primero = true;
                    continue;
                }
                if (this.getMiembroGremio() == null || !this.getMiembroGremio().puede(Constantes.G_OTRASMONTURAS) || this._enCercado.getDue\u00f1o() == -1) continue;
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
        this._enCercado = null;
    }

    public Maps.Cercado getEnCercado() {
        return this._enCercado;
    }

    public void fullPDV() {
        this._PDV = this._PDVMAX;
    }

    public void removerObjetoPorModYCant(int objModeloID, int cantidad) {
        ArrayList<Objeto> lista = new ArrayList<Objeto>();
        lista.addAll(this._objetos.values());
        ArrayList<Objeto> listaObjBorrar = new ArrayList<Objeto>();
        int cantTemp = cantidad;
        for (Objeto obj : lista) {
            int nuevaCant;
            if (obj.getModelo().getID() != objModeloID) continue;
            if (obj.getCantidad() >= cantidad) {
                nuevaCant = obj.getCantidad() - cantidad;
                if (nuevaCant > 0) {
                    obj.setCantidad(nuevaCant);
                    SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this, obj);
                } else {
                    this._objetos.remove(obj.getID());
                    World.eliminarObjeto(obj.getID());
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
                    this._objetos.remove(objBorrar.getID());
                    World.eliminarObjeto(objBorrar.getID());
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
        for (Objeto objeto : this._objetos.values()) {
            if (objeto.getPosicion() == -1 || objeto.getPosicion() > 15) continue;
            objetos.add(objeto);
        }
        return objetos;
    }

    public void eliminarObjetoPorModelo(int objModeloID) {
        ArrayList<Objeto> lista = new ArrayList<Objeto>();
        lista.addAll(this._objetos.values());
        for (Objeto obj : lista) {
            if (obj.getModelo().getID() != objModeloID) continue;
            this._objetos.remove(obj.getID());
            World.eliminarObjeto(obj.getID());
            SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this, obj.getID());
        }
    }

    public Map<Integer, Profissao.StatsOficio> getStatsOficios() {
        return this._statsOficios;
    }

    public void iniciarAccionOficio(int idTrabajo, Maps.ObjetoInteractivo objInterac, GameThread.AccionDeJuego GA, Maps.Celda celda) {
        Profissao.StatsOficio SO = this.getOficioPorTrabajo(idTrabajo);
        if (SO == null) {
            return;
        }
        SO.iniciarTrabajo(idTrabajo, this, objInterac, GA, celda);
    }

    public void finalizarAccionOficio(int idTrabajo, GameThread.AccionDeJuego GA, Maps.Celda celda) {
        Profissao.StatsOficio SO = this.getOficioPorTrabajo(idTrabajo);
        if (SO == null) {
            return;
        }
        SO.finalizarTrabajo(idTrabajo, this, GA, celda);
    }

    public String stringOficios() {
        StringBuffer str = new StringBuffer();
        for (Profissao.StatsOficio SO : this._statsOficios.values()) {
            if (str.length() > 0) {
                str.append(";");
            }
            str.append(String.valueOf(SO.getOficio().getID()) + "," + SO.getXP());
        }
        return str.toString();
    }

    public int totalOficiosBasicos() {
        int i = 0;
        for (Profissao.StatsOficio SO : this._statsOficios.values()) {
            int idOficio = SO.getOficio().getID();
            if (idOficio != 2 && idOficio != 11 && idOficio != 13 && idOficio != 14 && idOficio != 15 && idOficio != 16 && idOficio != 17 && idOficio != 18 && idOficio != 19 && idOficio != 20 && idOficio != 24 && idOficio != 25 && idOficio != 26 && idOficio != 27 && idOficio != 28 && idOficio != 31 && idOficio != 36 && idOficio != 41 && idOficio != 56 && idOficio != 58 && idOficio != 60 && idOficio != 65) continue;
            ++i;
        }
        return i;
    }

    public int totalOficiosFM() {
        int i = 0;
        for (Profissao.StatsOficio SO : this._statsOficios.values()) {
            int idOficio = SO.getOficio().getID();
            if (idOficio != 43 && idOficio != 44 && idOficio != 45 && idOficio != 46 && idOficio != 47 && idOficio != 48 && idOficio != 49 && idOficio != 50 && idOficio != 62 && idOficio != 63 && idOficio != 64) continue;
            ++i;
        }
        return i;
    }

    public void setHaciendoTrabajo(Profissao.AccionTrabajo AT) {
        this._accionTrabajo = AT;
    }

    public Profissao.AccionTrabajo getHaciendoTrabajo() {
        return this._accionTrabajo;
    }

    public Profissao.StatsOficio getOficioPorTrabajo(int trabajoID) {
        for (Profissao.StatsOficio SO : this._statsOficios.values()) {
            if (!SO.esValidoTrabajo(trabajoID)) continue;
            return SO;
        }
        return null;
    }

    public String analizarListaAmigos(int id) {
        StringBuffer str = new StringBuffer(";");
        str.append("?;");
        str.append(String.valueOf(this._nombre) + ";");
        if (this._cuenta.esAmigo(id)) {
            str.append(String.valueOf(this._nivel) + ";");
            str.append(String.valueOf(this._alineacion) + ";");
        } else {
            str.append("?;");
            str.append("-1;");
        }
        str.append(String.valueOf(this._clase) + ";");
        str.append(String.valueOf(this._sexo) + ";");
        str.append(this._gfxID);
        return str.toString();
    }

    public String analizarListaEnemigos(int id) {
        StringBuffer str = new StringBuffer(";");
        str.append("?;");
        str.append(String.valueOf(this._nombre) + ";");
        if (this._cuenta.esEnemigo(id)) {
            str.append(String.valueOf(this._nivel) + ";");
            str.append(String.valueOf(this._alineacion) + ";");
        } else {
            str.append("?;");
            str.append("-1;");
        }
        str.append(String.valueOf(this._clase) + ";");
        str.append(String.valueOf(this._sexo) + ";");
        str.append(this._gfxID);
        return str.toString();
    }

    public Profissao.StatsOficio getOficioPorID(int oficio) {
        for (Profissao.StatsOficio SO : this._statsOficios.values()) {
            if (SO.getOficio().getID() != oficio) continue;
            return SO;
        }
        return null;
    }

    public boolean estaMontando() {
        return this._montando;
    }

    public void bajarMontura() {
        this._montando = !this._montando;
        SocketManager.ENVIAR_Rr_ESTADO_MONTADO(this, this._montando ? "+" : "-");
    }

    public void subirBajarMontura() {
        if (this._encarnacion != null) {
            SocketManager.ENVIAR_Im_INFORMACION(this, "134|44");
            return;
        }
        if (this._montura.getEnergia() <= 0) {
            SocketManager.ENVIAR_Im_INFORMACION(this, "1113");
            return;
        }
        this._montando = !this._montando;
        Objeto mascota = this.getObjPosicion(8);
        if (this._montando && mascota != null) {
            mascota.setPosicion(-1);
            this._mascota = null;
            SocketManager.ENVIAR_OM_MOVER_OBJETO(this, mascota);
        }
        SocketManager.ENVIAR_Re_DETALLES_MONTURA(this, "+", this._montura);
        if (this._pelea == null) {
            SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(this._mapa, this);
        } else if (this._pelea.getEstado() == 2) {
            SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_PELEA(this._pelea, this._pelea.getLuchadorPorPJ(this));
        }
        SocketManager.ENVIAR_Rr_ESTADO_MONTADO(this, this._montando ? "+" : "-");
        SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
        this._montura.energiaPerdida(15);
    }

    public int getXpDonadaMontura() {
        return this._xpDonadaMontura;
    }

    public Dragossauros getMontura() {
        return this._montura;
    }

    public void setMontura(Dragossauros DP) {
        this._montura = DP;
    }

    public void setDonarXPMontura(int xp) {
        this._xpDonadaMontura = xp;
    }

    public void setEnLinea(boolean linea) {
        this._enLinea = linea;
    }

    public void resetVariables() {
        this._intercambioCon = 0;
        this._conversandoCon = 0;
        this._ocupado = false;
        this._emoteActivado = 0;
        this._listo = false;
        this._intercambio = null;
        this._estarBanco = false;
        this._invitando = -1;
        this._sentado = false;
        this._accionTrabajo = null;
        this._enZaaping = false;
        this._enCercado = null;
        this._montando = false;
        this._recaudando = false;
        this._recaudandoRecaudadorID = 0;
        this._esDoble = false;
        this._olvidandoHechizo = false;
        this._ausente = false;
        this._invisible = false;
        this._trueque = null;
        this._cofre = null;
        this._casa = null;
        this._listaArtesanos = false;
        this._cambiarNombre = false;
        this._dragopaveando = false;
        this._siguiendo = null;
        this._tallerInvitado = null;
        this._tutorial = null;
    }

    public void addCanal(String canal) {
        if (this._canales.indexOf(canal) >= 0) {
            return;
        }
        this._canales = String.valueOf(this._canales) + canal;
        SocketManager.ENVIAR_cC_SUSCRIBIR_CANAL(this, '+', canal);
    }

    public void removerCanal(String canal) {
        this._canales = this._canales.replace(canal, "");
        SocketManager.ENVIAR_cC_SUSCRIBIR_CANAL(this, '-', canal);
    }

    public void modificarAlineamiento(byte a) {
        this._honor = 0;
        this._deshonor = 0;
        this._alineacion = a;
        this._nivelAlineacion = 1;
        this._mostrarAlas = false;
        SocketManager.ENVIAR_ZC_ESPECIALIDAD_ALINEACION(this, a);
        SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
    }

    public void setDeshonor(int deshonor) {
        this._deshonor = deshonor;
    }

    public int getDeshonor() {
        return this._deshonor;
    }

    public boolean estaMostrandoAlas() {
        return this._mostrarAlas;
    }

    public void setMostrarAlas(boolean mostrarAlas) {
        this._mostrarAlas = mostrarAlas;
    }

    public int getHonor() {
        return this._honor;
    }

    public int getNivelAlineacion() {
        if (this._alineacion == 0) {
            return 1;
        }
        return this._nivelAlineacion;
    }

    public void botonActDesacAlas(char c) {
        if (this._alineacion == 0) {
            return;
        }
        int honorPerd = this._honor / 20;
        switch (c) {
            case '*': {
                SocketManager.ENVIAR_GIP_ACT_DES_ALAS_PERDER_HONOR(this, honorPerd);
                return;
            }
            case '+': {
                this._mostrarAlas = true;
                SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
                break;
            }
            case '-': {
                this._mostrarAlas = false;
                this._honor -= honorPerd;
                SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
            }
        }
    }

    public void addHonor(int honor) {
        if (honor == 0) {
            return;
        }
        int nivelAntes = this._nivelAlineacion;
        this._honor += honor;
        if (this._honor < 0) {
            this._honor = 0;
        } else if (this._honor >= 25000) {
            this._nivelAlineacion = 10;
            this._honor = 25000;
        }
        for (int n = 1; n <= 10; ++n) {
            if (this._honor >= World.getExpNivel((int)n)._pvp) continue;
            this._nivelAlineacion = n - 1;
            break;
        }
        if (nivelAntes == this._nivelAlineacion) {
            return;
        }
        if (nivelAntes < this._nivelAlineacion) {
            SocketManager.ENVIAR_Im_INFORMACION(this, "082;" + this._nivelAlineacion);
        } else if (nivelAntes > this._nivelAlineacion) {
            SocketManager.ENVIAR_Im_INFORMACION(this, "083;" + this._nivelAlineacion);
        }
    }

    public Guild.MiembroGremio getMiembroGremio() {
        return this._miembroGremio;
    }

    public int getCuentaID() {
        return this._cuenta.getID();
    }

    public void setCuenta(Conta c) {
        this._cuenta = c;
    }

    public String stringListaZaap() {
        StringBuffer str = new StringBuffer();
        str.append(this._puntoSalvado.split(",")[0]);
        int subAreaID = this._mapa.getSubArea().getArea().getSuperArea().getID();
        for (short i : this._zaaps) {
            if (World.getMapa(i) == null || World.getMapa(i).getSubArea().getArea().getSuperArea().getID() != subAreaID) continue;
            int costo = Fórmulas.calcularCosteZaap(this._mapa, World.getMapa(i));
            if (i == this._mapa.getID()) {
                costo = 0;
            }
            str.append("|" + i + ";" + costo);
        }
        return str.toString();
    }

    public String stringListaPrismas() {
        StringBuffer str = new StringBuffer(this._mapa.getID());
        int subAreaID = this._mapa.getSubArea().getArea().getSuperArea().getID();
        for (Prisma prisma : World.todosPrismas()) {
            short mapaID;
            if (prisma.getAlineacion() != this._alineacion || World.getMapa(mapaID = prisma.getMapaID()) == null || World.getMapa(mapaID).getSubArea().getArea().getSuperArea().getID() != subAreaID) continue;
            if (prisma.getEstadoPelea() == 0 || prisma.getEstadoPelea() == -2) {
                str.append("|" + mapaID + ";*");
                continue;
            }
            int costo = Fórmulas.calcularCosteZaap(this._mapa, World.getMapa(mapaID));
            if (mapaID == this._mapa.getID()) {
                costo = 0;
            }
            str.append("|" + mapaID + ";" + costo);
        }
        return str.toString();
    }

    public boolean tieneZaap(int mapID) {
        for (short i : this._zaaps) {
            if (i != mapID) continue;
            return true;
        }
        return false;
    }

    public void abrirMenuZaap() {
        if (this._pelea == null) {
            if (this.getDeshonor() >= 3) {
                SocketManager.ENVIAR_Im_INFORMACION(this, "183");
                return;
            }
            this._enZaaping = true;
            if (!this.tieneZaap(this._mapa.getID())) {
                this._zaaps.add(this._mapa.getID());
                SocketManager.ENVIAR_Im_INFORMACION(this, "024");
            }
            SocketManager.ENVIAR_WC_MENU_ZAAP(this);
        }
    }

    public void abrirMenuPrisma() {
        if (this._pelea == null) {
            if (this.getDeshonor() >= 3) {
                SocketManager.ENVIAR_Im_INFORMACION(this, "183");
                return;
            }
            this._enZaaping = true;
            SocketManager.ENVIAR_Wp_MENU_PRISMA(this);
        }
    }

    public void usarZaap(short mapaID) {
        if (!this._enZaaping || this._pelea != null || !this.tieneZaap(mapaID)) {
            return;
        }
        int costo = Fórmulas.calcularCosteZaap(this._mapa, World.getMapa(mapaID));
        if (this._kamas < (long)costo) {
            return;
        }
        int superAreaID = this._mapa.getSubArea().getArea().getSuperArea().getID();
        short celdaID = World.getCeldaZaapPorMapaID(mapaID);
        Maps zaapMapa = World.getMapa(mapaID);
        if (zaapMapa == null) {
            System.out.println("El mapa " + mapaID + " no esta implantado, Zaap rechazada");
            SocketManager.ENVIAR_WUE_ZAPPI_ERROR(this);
            return;
        }
        if (zaapMapa.getCelda(celdaID) == null) {
            System.out.println("La celda asociada un zaap " + mapaID + " no esta implatado, Zaap rechazada");
            SocketManager.ENVIAR_WUE_ZAPPI_ERROR(this);
            return;
        }
        if (!zaapMapa.getCelda(celdaID).esCaminable(true)) {
            System.out.println("La celda asociada a un zaap " + mapaID + " no esta 'walkable', Zaap rechazada");
            SocketManager.ENVIAR_WUE_ZAPPI_ERROR(this);
            return;
        }
        if (zaapMapa.getSubArea().getArea().getSuperArea().getID() != superAreaID) {
            SocketManager.ENVIAR_WUE_ZAPPI_ERROR(this);
            return;
        }
        if (this._alineacion == 2 && mapaID == 4263 || this._alineacion == 1 && mapaID == 5295) {
            SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this, "<b>Sistema</b>: Esse zaap \u00e9 do alinhamento inimigo.");
            SocketManager.ENVIAR_WUE_ZAPPI_ERROR(this);
            return;
        }
        this._kamas -= (long)costo;
        this.teleport(mapaID, celdaID);
        SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
        SocketManager.ENVIAR_WV_CERRAR_ZAAP(this);
        this._enZaaping = false;
    }

    public String stringZaaps() {
        StringBuffer str = new StringBuffer();
        boolean primero = false;
        for (short i : this._zaaps) {
            if (primero) {
                str.append(",");
            }
            primero = true;
            str.append(i);
        }
        return str.toString();
    }

    public void cerrarZaap() {
        if (!this._enZaaping) {
            return;
        }
        this._enZaaping = false;
        SocketManager.ENVIAR_WV_CERRAR_ZAAP(this);
    }

    public void cerrarPrisma() {
        if (!this._enZaaping) {
            return;
        }
        this._enZaaping = false;
        SocketManager.ENVIAR_Ww_CERRAR_PRISMA(this);
    }

    public void cerrarZaapi() {
        if (!this._enZaaping) {
            return;
        }
        this._enZaaping = false;
        SocketManager.ENVIAR_Wv_CERRAR_ZAPPI(this);
    }

    public void usarZaapi(String packet) {
        short mapaID = Short.parseShort(packet.substring(2));
        Maps mapa = World.getMapa(mapaID);
        short celdaId = 100;
        if (mapa != null) {
            for (Map.Entry<Short, Maps.Celda> entry : mapa.getCeldas().entrySet()) {
                Maps.ObjetoInteractivo obj = entry.getValue().getObjetoInterac();
                if (obj == null || obj.getID() != 7031 && obj.getID() != 7030) continue;
                celdaId = (short)(entry.getValue().getID() + 18);
            }
        }
        if (mapa.getSubArea().getArea().getID() == 7 || mapa.getSubArea().getArea().getID() == 11) {
            int precio = 20;
            if (this._alineacion == 1 || this._alineacion == 2) {
                precio = 10;
            }
            this._kamas -= (long)precio;
            SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
            this.teleport(mapaID, celdaId);
            SocketManager.ENVIAR_Wv_CERRAR_ZAPPI(this);
        }
    }

    public void usarPrisma(String packet) {
        short celdaID = 340;
        short mapaID = 7411;
        try {
            mapaID = Short.parseShort(packet.substring(2));
        }
        catch (Exception exception) {
            // empty catch block
        }
        for (Prisma prisma : World.todosPrismas()) {
            if (prisma.getMapaID() != mapaID) continue;
            celdaID = prisma.getCeldaID();
            mapaID = prisma.getMapaID();
            break;
        }
        int costo = Fórmulas.calcularCosteZaap(this._mapa, World.getMapa(mapaID));
        if (mapaID == this._mapa.getID()) {
            costo = 0;
        }
        if (this._kamas < (long)costo) {
            SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this, "<b>Sistema</b>: Voc\u00ea n\u00e3o tem as kamas necess\u00e1rias para essa a\u00e7\u00e3o.");
            return;
        }
        this._kamas -= (long)costo;
        SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
        this.teleport(mapaID, celdaID);
        SocketManager.ENVIAR_Ww_CERRAR_PRISMA(this);
    }

    public boolean tieneObjModeloNoEquip(int id, int cantidad) {
        for (Objeto obj : this._objetos.values()) {
            if (obj.getPosicion() != -1 && obj.getPosicion() < 35 || obj.getModelo().getID() != id || obj.getCantidad() < cantidad) continue;
            return true;
        }
        return false;
    }

    public Objeto getObjModeloNoEquip(int id, int cantidad) {
        for (Objeto obj : this._objetos.values()) {
            if (obj.getPosicion() != -1 && obj.getPosicion() < 35 || obj.getModelo().getID() != id || obj.getCantidad() < cantidad) continue;
            return obj;
        }
        return null;
    }

    public void setZaaping(boolean zaaping) {
        this._enZaaping = zaaping;
    }

    public void setOlvidandoHechizo(boolean olvidandoHechizo) {
        this._olvidandoHechizo = olvidandoHechizo;
    }

    public boolean estaOlvidandoHechizo() {
        return this._olvidandoHechizo;
    }

    public boolean estaDisponible(Personagens perso) {
        if (this._ausente) {
            return false;
        }
        if (this._invisible) {
            return this._cuenta.esAmigo(perso.getCuenta().getID());
        }
        return true;
    }

    public void setSiguiendo(Personagens perso) {
        this._siguiendo = perso;
    }

    public Personagens getSiguiendo() {
        return this._siguiendo;
    }

    public boolean estaAusente() {
        return this._ausente;
    }

    public void setEstaAusente(boolean ausente) {
        this._ausente = ausente;
    }

    public boolean esInvisible() {
        return this._invisible;
    }

    public boolean esFantasma() {
        return this._esFantasma;
    }

    public void setEsInvisible(boolean invisible) {
        this._invisible = invisible;
    }

    public boolean esDoble() {
        return this._esDoble;
    }

    public void esDoble(boolean esDoble) {
        this._esDoble = esDoble;
    }

    public boolean getRecaudando() {
        return this._recaudando;
    }

    public void setRecaudando(boolean recaudando) {
        this._recaudando = recaudando;
    }

    public void setDragopaveando(boolean recaudando) {
        this._dragopaveando = recaudando;
    }

    public boolean getMochilaMontura() {
        return this._dragopaveando;
    }

    public int getRecaudandoRecauID() {
        return this._recaudandoRecaudadorID;
    }

    public void setRecaudandoRecaudadorID(int recaudadorID) {
        this._recaudandoRecaudadorID = recaudadorID;
    }

    public void setTitulo(int titulo) {
        this._titulo = titulo;
    }

    public int getTitulo() {
        return this._titulo;
    }

    public boolean cambiarNombre() {
        return this._cambiarNombre;
    }

    public void cambiarNombre(boolean cambiar) {
        this._cambiarNombre = cambiar;
    }

    public void setNombre(String nombre) {
        this._nombre = nombre;
        this._cambiarNombre = false;
        SQLManager.ACTUALIZAR_NOMBRE(this);
        if (this.getMiembroGremio() != null) {
            SQLManager.REPLACE_MIEMBRO_GREMIO(this.getMiembroGremio());
        }
    }

    public static Personagens personajeClonado(Personagens perso, int id) {
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
        Personagens clon = new Personagens(id, perso._nombre, perso._sexo, perso._clase, perso._color1, perso._color2, perso._color3, perso._nivel, 100, perso._gfxID, stats, perso._objetos, 100, mostrarAlas, monturaID, nivelAlineacion, perso._alineacion);
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
        for (Objeto obj : this._objetos.values()) {
            if (obj.getPosicion() == -1) continue;
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
            if (obj.getPosicion() != 8) continue;
            if (primerMascota) {
                primerMascota = false;
                continue;
            }
            obj.setPosicion(-1);
        }
    }

    public MisionPVP getMisionPVP() {
        return this._misionPvp;
    }

    public void setMisionPVP(MisionPVP mision) {
        this._misionPvp = mision;
    }

    public void esposoDe(Personagens esposo) {
        this._esposo = esposo.getID();
    }

    public String getEsposoListaAmigos() {
        Personagens esposo = World.getPersonaje(this._esposo);
        StringBuffer str = new StringBuffer();
        if (esposo != null) {
            str.append(String.valueOf(esposo._nombre) + "|" + esposo._clase + esposo._sexo + "|" + esposo._color1 + "|" + esposo._color2 + "|" + esposo._color3 + "|");
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
        if (this._pelea != null) {
            p = 1;
        }
        return String.valueOf(this._mapa.getID()) + "|" + this._nivel + "|" + p;
    }

    public void casarse(Personagens perso) {
        if (perso == null) {
            return;
        }
        int dist = (this._mapa.getX() - perso._mapa.getX()) * (this._mapa.getX() - perso._mapa.getX()) + (this._mapa.getY() - perso._mapa.getY()) * (this._mapa.getY() - perso._mapa.getY());
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
        this.teleport(perso._mapa.getID(), celdaPosicion);
    }

    public void divorciar() {
        if (this._enLinea) {
            SocketManager.ENVIAR_Im_INFORMACION(this, "047;" + World.getPersonaje(this._esposo).getNombre());
        }
        this._esposo = 0;
    }

    public int getEsposo() {
        return this._esposo;
    }

    public int setEsOK(int ok) {
        this._esOK = ok;
        return this._esOK;
    }

    public int getEsOK() {
        return this._esOK;
    }

    public void cambiarOrientacion(byte orientacion) {
        if (this._orientacion == 0 || this._orientacion == 2 || this._orientacion == 4 || this._orientacion == 6) {
            this.setOrientacion(orientacion);
            SocketManager.ENVIAR_eD_CAMBIAR_ORIENTACION(this._mapa, this.getID(), orientacion);
        }
    }

    public void setCofre(Cofre cofre) {
        this._cofre = cofre;
    }

    public Cofre getCofre() {
        return this._cofre;
    }

    public void setCasa(Casa casa) {
        this._casa = casa;
    }

    public Casa getCasa() {
        return this._casa;
    }

    public String stringColorDue\u00f1oPavo() {
        return String.valueOf(this._color1 == -1 ? "" : Integer.toHexString(this._color1)) + "," + (this._color2 == -1 ? "" : Integer.toHexString(this._color2)) + "," + (this._color3 == -1 ? "" : Integer.toHexString(this._color3));
    }

    public String getModeloObjEnPos(int posiciones) {
        if (posiciones == -1) {
            return null;
        }
        for (Map.Entry<Integer, Objeto> entry : this._objetos.entrySet()) {
            Objeto obj = entry.getValue();
            if (obj.getPosicion() != posiciones) continue;
            if (obj.getObjeviID() != 0) {
                for (Set_Vivo objevi : World.getTodosObjevivos()) {
                    if (objevi.getID() != obj.getObjeviID()) continue;
                    String toReturn = String.valueOf(Integer.toHexString(objevi.getRealModeloDB())) + "~" + objevi.getTipo() + "~" + objevi.getMascara();
                    return toReturn;
                }
                continue;
            }
            return Integer.toHexString(obj.getModelo().getID());
        }
        return null;
    }

    public boolean objetoAInvetario(int id) {
        if (this == null || this._intercambioCon != this._ID) {
            return false;
        }
        Objeto objMovido = null;
        for (Objeto objeto : this._tienda) {
            if (objeto.getID() != id) continue;
            objMovido = objeto;
            break;
        }
        if (objMovido == null) {
            return false;
        }
        this._tienda.remove(objMovido);
        if (this.addObjetoSimilar(objMovido, true, -1)) {
            World.eliminarObjeto(objMovido.getID());
        } else {
            this.addObjetoPut(objMovido);
            SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this, objMovido);
        }
        World.borrarObjTienda(objMovido.getID());
        return true;
    }

    public int contarTienda() {
        int cantidad = this._tienda.size();
        return cantidad;
    }

    public int maxTienda() {
        int cantidad = this._nivel / 10;
        return cantidad;
    }

    public String getStringTienda() {
        StringBuffer str = new StringBuffer();
        for (Objeto objetos : this._tienda) {
            str.append(String.valueOf(objetos.getID()) + "|");
        }
        return str.toString();
    }

    public void agregarObjTienda(Objeto objeto) {
        this._tienda.add(objeto);
    }

    public void borrarObjTienda(Objeto objeto) {
        if (this._tienda.contains(objeto)) {
            this._tienda.remove(objeto);
            World.borrarObjTienda(objeto.getID());
        }
    }

    public boolean quedaObjTienda() {
        return this._tienda.size() != 0;
    }

    public String listaTienda() {
        String lista = "";
        boolean esPrimero = true;
        if (this._tienda.isEmpty()) {
            return lista;
        }
        for (Objeto objeto : this._tienda) {
            int idobjeto;
            Stockage tienda;
            if (!esPrimero) {
                lista = String.valueOf(lista) + "|";
            }
            if ((tienda = World.getObjTienda(idobjeto = objeto.getID())) == null) continue;
            lista = String.valueOf(lista) + idobjeto + ";" + tienda.getCantidad() + ";" + objeto.getModelo().getID() + ";" + objeto.convertirStatsAString() + ";" + tienda.getPrecio();
            esPrimero = false;
        }
        return lista;
    }

    public ArrayList<Objeto> getTienda() {
        return this._tienda;
    }

    public void actualizarObjTienda(int objID, long precio) {
        Stockage tienda = World.getObjTienda(objID);
        tienda.setPrecio(precio);
    }

    public long precioTotalTienda() {
        long precio = 0L;
        for (Objeto obj : this._tienda) {
            Stockage tienda = World.getObjTienda(obj.getID());
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
            if (this.getObjPosicion(j) == null) continue;
            Objeto obj = this.getObjPosicion(j);
            int template = obj.getModelo().getID();
            int set = obj.getModelo().getSetID();
            if ((set < 81 || set > 92) && (set < 201 || set > 212)) continue;
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
                this.addHechizosSetClase(hechizo, efecto, modif);
            }
            if (this._setClase.contains(template)) continue;
            this._setClase.add(template);
        }
    }

    public int getModifSetClase(int hechizo, int efecto) {
        int modif = 0;
        if (this._bendHechizo == hechizo && this._bendEfecto == efecto) {
            modif += this._bendModif;
        }
        if (this._hechizosSetClase.containsKey(hechizo) && (Integer)this._hechizosSetClase.get((Object)Integer.valueOf((int)hechizo))._primero == efecto) {
            return modif += ((Integer)this._hechizosSetClase.get((Object)Integer.valueOf((int)hechizo))._segundo).intValue();
        }
        return modif;
    }

    public String analizarPrismas() {
        String str = "";
        Prisma prisma = World.getPrisma(this._mapa.getSubArea().getPrismaID());
        str = prisma == null ? "-3" : (prisma.getEstadoPelea() == 0 ? "0;" + prisma.getTiempoTurno() + ";45000;7" : String.valueOf(prisma.getEstadoPelea()));
        return str;
    }

    public void setObjetoARomper(int objetoID) {
        this._objetoIDRomper = objetoID;
    }

    public int getObjetoARomper() {
        return this._objetoIDRomper;
    }

    public void setRompiendo(boolean romper) {
        this._rompiendo = romper;
    }

    public boolean getRompiendo() {
        return this._rompiendo;
    }

    public void restarVidaMascota(Pets mascota) {
        Objeto masc = null;
        masc = mascota == null ? this.getObjPosicion(8) : World.getObjeto(mascota.getID());
        if (masc != null) {
            int idMascota = masc.getID();
            if (mascota == null) {
                mascota = World.getMascota(idMascota);
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
                    this._mascota = null;
                } else {
                    this.borrarObjetoSinEliminar(idMascota);
                    World.eliminarObjeto(idMascota);
                    this._mascota = null;
                }
                this.refrescarVida();
                SocketManager.ENVIAR_As_STATS_DEL_PJ(this);
                SocketManager.ENVIAR_Im_INFORMACION(this, "154");
            }
        }
    }

    public void setAceptar(boolean aceptar) {
        this._aceptaKoli = aceptar;
    }

    public boolean getAceptar() {
        return this._aceptaKoli;
    }

    public long expKoli() {
        switch (this._categoria) {
            case 1: {
                return (World.getExpNivel((int)(this._nivel + 1))._personaje - World.getExpNivel((int)this._nivel)._personaje) * 2L;
            }
            case 2: {
                return World.getExpNivel((int)(this._nivel + 1))._personaje - World.getExpNivel((int)this._nivel)._personaje;
            }
            case 3: {
                if (this._nivel == 300) {
                    return 0L;
                }
                return (World.getExpNivel((int)(this._nivel + 1))._personaje - World.getExpNivel((int)this._nivel)._personaje) / 6L;
            }
        }
        return 0L;
    }

    public long kamasKoli() {
        switch (this._categoria) {
            case 1: {
                return 20000L;
            }
            case 2: {
                return 50000L;
            }
            case 3: {
                if (this._nivel == 300) {
                    return 250000L;
                }
                return 100000L;
            }
        }
        return 0L;
    }

    public int get_savestat() {
        return this.savestat;
    }

    public void set_savestat(int stat) {
        this.savestat = stat;
    }

    public static class Grupo {
        private CopyOnWriteArrayList<Personagens> _personajesGrupo = new CopyOnWriteArrayList();
        private Personagens _liderGrupo;

        public Grupo(Personagens p1, Personagens p2) {
            this._liderGrupo = p1;
            this._personajesGrupo.add(p1);
            this._personajesGrupo.add(p2);
        }

        public boolean esLiderGrupo(int id) {
            return this._liderGrupo.getID() == id;
        }

        public void addPerso(Personagens perso) {
            this._personajesGrupo.add(perso);
        }

        public ArrayList<Integer> getIDsPersos() {
            ArrayList<Integer> lista = new ArrayList<Integer>();
            for (Personagens perso : this._personajesGrupo) {
                lista.add(perso.getID());
            }
            return lista;
        }

        public int getNumeroPjs() {
            return this._personajesGrupo.size();
        }

        public int getNivelGrupo() {
            int nivel = 0;
            for (Personagens p : this._personajesGrupo) {
                nivel += p.getNivel();
            }
            return nivel;
        }

        public CopyOnWriteArrayList<Personagens> getPersos() {
            return this._personajesGrupo;
        }

        public Personagens getLiderGrupo() {
            return this._liderGrupo;
        }

        public void dejarGrupo(Personagens p) {
            if (!this._personajesGrupo.contains(p)) {
                return;
            }
            p.setGrupo(null);
            this._personajesGrupo.remove(p);
            if (this._personajesGrupo.size() == 1) {
                this._personajesGrupo.get(0).setGrupo(null);
                if (this._personajesGrupo.get(0).getCuenta() == null || this._personajesGrupo.get(0).getCuenta().getEntradaPersonaje() == null) {
                    return;
                }
                SocketManager.ENVIAR_PV_DEJAR_GRUPO(this._personajesGrupo.get(0).getCuenta().getEntradaPersonaje().getOut(), "");
            } else {
                SocketManager.ENVIAR_PM_EXPULSAR_PJ_GRUPO(this, p.getID());
            }
        }
    }

    public static class GrupoKoliseo {
        private Personagens _koli1;
        private Personagens _koli2;
        private Personagens _koli3;
        private int _sumaNivel = 0;
        private int _categoria = 0;

        public GrupoKoliseo(Personagens koli1, Personagens koli2, Personagens koli3, int categoria) {
            this._koli1 = koli1;
            this._koli2 = koli2;
            this._koli3 = koli3;
            this._koli1._categoria = categoria;
            this._koli2._categoria = categoria;
            this._koli3._categoria = categoria;
            this._sumaNivel = this._koli1.getNivel() + this._koli2.getNivel() + this._koli3.getNivel();
            this._categoria = categoria;
        }

        public int getSumaNiveles() {
            return this._sumaNivel;
        }

        public ArrayList<Personagens> getParticipantes() {
            ArrayList<Personagens> grupo = new ArrayList<Personagens>();
            grupo.add(this._koli1);
            grupo.add(this._koli2);
            grupo.add(this._koli3);
            return grupo;
        }

        public int getCategoria() {
            return this._categoria;
        }
    }

    public static class MisionPVP {
        private long _tiempo;
        private Personagens _victimaPVP;
        private int _kamas;

        public MisionPVP(long tiempo, Personagens personaje, int kamas) {
            this._tiempo = tiempo;
            this._victimaPVP = personaje;
            this._kamas = kamas;
        }

        public void setPjMision(Personagens personaje) {
            this._victimaPVP = personaje;
        }

        public int getKamasRecompensa() {
            return this._kamas;
        }

        public void setKamasRecompensa(int kamas) {
            this._kamas = kamas;
        }

        public Personagens getPjMision() {
            return this._victimaPVP;
        }

        public long getTiempoPVP() {
            return this._tiempo;
        }

        public void setTiempoPVP(long tiempo) {
            this._tiempo = tiempo;
        }
    }

    public static class Stats {
        public Map<Integer, Integer> _statsEfecto = new TreeMap<Integer, Integer>();

        public Stats(boolean addBases, Personagens perso) {
            this._statsEfecto = new TreeMap<Integer, Integer>();
            if (!addBases) {
                return;
            }
            this._statsEfecto.put(111, perso.getNivel() < LesGuardians.NIVEL_PA1 ? 6 : 6 + LesGuardians.MAX_PA1);
            this._statsEfecto.put(128, 3);
            this._statsEfecto.put(176, perso.getClase(false) == 3 ? 140 : 100);
            this._statsEfecto.put(158, 1000);
            this._statsEfecto.put(182, 1);
            this._statsEfecto.put(174, 1);
        }

        public int getEffect(int statsAddFuerza) {
            return 0;
        }

        public Stats(Map<Integer, Integer> stats, boolean addBases, Personagens perso) {
            this._statsEfecto = stats;
            if (!addBases) {
                return;
            }
            this._statsEfecto.put(111, perso.getNivel() < LesGuardians.NIVEL_PA1 ? 6 : 6 + LesGuardians.MAX_PA1);
            this._statsEfecto.put(128, 3);
            this._statsEfecto.put(176, perso.getClase(false) == 3 ? 140 : 100);
            this._statsEfecto.put(158, 1000);
            this._statsEfecto.put(182, 1);
            this._statsEfecto.put(174, 1);
        }

        public Stats(Map<Integer, Integer> stats) {
            this._statsEfecto = stats;
        }

        public Stats() {
            this._statsEfecto = new TreeMap<Integer, Integer>();
        }

        public int addStat(int stat, int valor) {
            if (this._statsEfecto.get(stat) == null) {
                this._statsEfecto.put(stat, valor);
            } else {
                int nuevoValor = this._statsEfecto.get(stat) + valor;
                this._statsEfecto.remove(stat);
                this._statsEfecto.put(stat, nuevoValor);
            }
            return this._statsEfecto.get(stat);
        }

        public int especificarStat(int stat, int valor) {
            if (this._statsEfecto.get(stat) == null) {
                this._statsEfecto.put(stat, valor);
            } else {
                this._statsEfecto.remove(stat);
                this._statsEfecto.put(stat, valor);
            }
            return this._statsEfecto.get(stat);
        }

        public boolean sonStatsIguales(Stats otros) {
            for (Map.Entry<Integer, Integer> entry : this._statsEfecto.entrySet()) {
                if (otros.getStatsComoMap().get(entry.getKey()) != null && otros.getStatsComoMap().get(entry.getKey()) == entry.getValue()) continue;
                return false;
            }
            for (Map.Entry<Integer, Integer> entry : otros.getStatsComoMap().entrySet()) {
                if (this._statsEfecto.get(entry.getKey()) != null && this._statsEfecto.get(entry.getKey()) == entry.getValue()) continue;
                return false;
            }
            return true;
        }

        public int getEfecto(int id) {
            int val = this._statsEfecto.get(id) == null ? 0 : this._statsEfecto.get(id);
            switch (id) {
                case 160: {
                    if (this._statsEfecto.get(162) != null) {
                        val -= this.getEfecto(162);
                    }
                    if (this._statsEfecto.get(124) == null) break;
                    val += this.getEfecto(124) / 4;
                    break;
                }
                case 161: {
                    if (this._statsEfecto.get(163) != null) {
                        val -= this.getEfecto(163);
                    }
                    if (this._statsEfecto.get(124) == null) break;
                    val += this.getEfecto(124) / 4;
                    break;
                }
                case 174: {
                    if (this._statsEfecto.get(175) == null) break;
                    val -= this._statsEfecto.get(175).intValue();
                    break;
                }
                case 119: {
                    if (this._statsEfecto.get(154) == null) break;
                    val -= this._statsEfecto.get(154).intValue();
                    break;
                }
                case 118: {
                    if (this._statsEfecto.get(157) == null) break;
                    val -= this._statsEfecto.get(157).intValue();
                    break;
                }
                case 123: {
                    if (this._statsEfecto.get(152) == null) break;
                    val -= this._statsEfecto.get(152).intValue();
                    break;
                }
                case 126: {
                    if (this._statsEfecto.get(155) == null) break;
                    val -= this._statsEfecto.get(155).intValue();
                    break;
                }
                case 111: {
                    if (this._statsEfecto.get(120) != null) {
                        val += this._statsEfecto.get(120).intValue();
                    }
                    if (this._statsEfecto.get(101) != null) {
                        val -= this._statsEfecto.get(101).intValue();
                    }
                    if (this._statsEfecto.get(168) == null) break;
                    val -= this._statsEfecto.get(168).intValue();
                    break;
                }
                case 128: {
                    if (this._statsEfecto.get(78) != null) {
                        val += this._statsEfecto.get(78).intValue();
                    }
                    if (this._statsEfecto.get(127) != null) {
                        val -= this._statsEfecto.get(127).intValue();
                    }
                    if (this._statsEfecto.get(169) == null) break;
                    val -= this._statsEfecto.get(169).intValue();
                    break;
                }
                case 117: {
                    if (this._statsEfecto.get(116) == null) break;
                    val -= this._statsEfecto.get(116).intValue();
                    break;
                }
                case 125: {
                    if (this._statsEfecto.get(110) != null) {
                        val += this._statsEfecto.get(110).intValue();
                    }
                    if (this._statsEfecto.get(153) == null) break;
                    val -= this._statsEfecto.get(153).intValue();
                    break;
                }
                case 112: {
                    if (this._statsEfecto.get(145) == null) break;
                    val -= this._statsEfecto.get(145).intValue();
                    break;
                }
                case 158: {
                    if (this._statsEfecto.get(159) == null) break;
                    val -= this._statsEfecto.get(159).intValue();
                    break;
                }
                case 176: {
                    if (this._statsEfecto.get(177) != null) {
                        val -= this._statsEfecto.get(177).intValue();
                    }
                    if (this._statsEfecto.get(123) == null) break;
                    val += this._statsEfecto.get(123) / 10;
                    break;
                }
                case 242: {
                    if (this._statsEfecto.get(247) == null) break;
                    val -= this._statsEfecto.get(247).intValue();
                    break;
                }
                case 243: {
                    if (this._statsEfecto.get(248) == null) break;
                    val -= this._statsEfecto.get(248).intValue();
                    break;
                }
                case 244: {
                    if (this._statsEfecto.get(249) == null) break;
                    val -= this._statsEfecto.get(249).intValue();
                    break;
                }
                case 240: {
                    if (this._statsEfecto.get(245) == null) break;
                    val -= this._statsEfecto.get(245).intValue();
                    break;
                }
                case 241: {
                    if (this._statsEfecto.get(246) == null) break;
                    val -= this._statsEfecto.get(246).intValue();
                    break;
                }
                case 210: {
                    if (this._statsEfecto.get(215) == null) break;
                    val -= this._statsEfecto.get(215).intValue();
                    break;
                }
                case 211: {
                    if (this._statsEfecto.get(216) == null) break;
                    val -= this._statsEfecto.get(216).intValue();
                    break;
                }
                case 212: {
                    if (this._statsEfecto.get(217) == null) break;
                    val -= this._statsEfecto.get(217).intValue();
                    break;
                }
                case 213: {
                    if (this._statsEfecto.get(218) == null) break;
                    val -= this._statsEfecto.get(218).intValue();
                    break;
                }
                case 214: {
                    if (this._statsEfecto.get(219) == null) break;
                    val -= this._statsEfecto.get(219).intValue();
                    break;
                }
                case 165: {
                    if (this._statsEfecto.get(165) == null) break;
                    val = this._statsEfecto.get(165);
                    break;
                }
                case 138: {
                    if (this._statsEfecto.get(186) == null) break;
                    val -= this._statsEfecto.get(186).intValue();
                    break;
                }
                case 178: {
                    if (this._statsEfecto.get(179) == null) break;
                    val -= this._statsEfecto.get(179).intValue();
                }
            }
            return val;
        }

        public static Stats acumularStats(Stats s1, Stats s2) {
            TreeMap<Integer, Integer> stats = new TreeMap<Integer, Integer>();
            for (int a = 0; a <= 1500; ++a) {
                if (!(s1._statsEfecto.get(a) != null && s1._statsEfecto.get(a) != 0 || s2._statsEfecto.get(a) != null && s2._statsEfecto.get(a) != 0)) continue;
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
            return this._statsEfecto;
        }

        public String convertirStatsAString() {
            StringBuffer str = new StringBuffer();
            for (Map.Entry<Integer, Integer> entry : this._statsEfecto.entrySet()) {
                if (str.length() > 0) {
                    str.append(",");
                }
                str.append(String.valueOf(Integer.toHexString(entry.getKey())) + "#" + Integer.toHexString(entry.getValue()) + "#0#0");
            }
            return str.toString();
        }
    }
}

