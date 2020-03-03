package common;

import common.Constantes;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import objects.PiedraAlma;
import objects.Animación;
import objects.Casa;
import objects.Cofre;
import objects.Recaudador;
import objects.Cuenta;
import objects.Reto;
import objects.Dragopavo;
import objects.Gremio;
import objects.Encarnación;
import objects.MobModelo;
import objects.Mapa;
import objects.Mercadillo;
import objects.NPCModelo;
import objects.Objeto;
import objects.Personaje;
import objects.Personaje.GrupoKoliseo;
import objects.Mascota;
import objects.Prisma;
import objects.Oficio;
import objects.RankingPVP;
import objects.Objevivo;
import objects.Hechizo;
import objects.Tienda;
import objects.Tutorial;

public class Mundo {
    private static Map<Integer, Cuenta> Cuentas = new TreeMap<Integer, Cuenta>();
    private static Map<String, Integer> CuentasPorNombre = new TreeMap<String, Integer>();
    private static Map<Integer, Personaje> Personajes = new TreeMap<Integer, Personaje>();
    private static Map<Short, Mapa> Mapas = new TreeMap<Short, Mapa>();
    private static Map<Integer, Objeto> Objetos = new TreeMap<Integer, Objeto>();
    private static Map<Integer, ExpNivel> Experiencia = new TreeMap<Integer, ExpNivel>();
    private static Map<Integer, Hechizo> Hechizos = new TreeMap<Integer, Hechizo>();
    private static Map<Integer, Objeto.ObjetoModelo> ObjModelos = new TreeMap<Integer, Objeto.ObjetoModelo>();
    private static Map<Integer, Objevivo> Objevivos = new TreeMap<Integer, Objevivo>();
    private static Map<Integer, MobModelo> MobModelos = new TreeMap<Integer, MobModelo>();
    private static Map<Integer, NPCModelo> NPCModelos = new TreeMap<Integer, NPCModelo>();
    private static Map<Integer, NPCModelo.PreguntaNPC> NPCPreguntas = new TreeMap<Integer, NPCModelo.PreguntaNPC>();
    private static Map<Integer, NPCModelo.RespuestaNPC> NPCRespuesta = new TreeMap<Integer, NPCModelo.RespuestaNPC>();
    private static Map<Integer, ObjInteractivoModelo> ObjInteractivos = new TreeMap<Integer, ObjInteractivoModelo>();
    private static Map<Integer, Dragopavo> Dragopavos = new TreeMap<Integer, Dragopavo>();
    private static Map<Integer, SuperArea> SuperAreas = new TreeMap<Integer, SuperArea>();
    private static Map<Short, Area> Areas = new TreeMap<Short, Area>();
    private static Map<Integer, SubArea> SubAreas = new TreeMap<Integer, SubArea>();
    private static Map<Integer, Oficio> Oficios = new TreeMap<Integer, Oficio>();
    private static Map<Integer, ArrayList<Duo<Integer, Integer>>> Recetas = new TreeMap<Integer, ArrayList<Duo<Integer, Integer>>>();
    private static Map<Integer, ObjetoSet> ObjetoSets = new TreeMap<Integer, ObjetoSet>();
    private static Map<Integer, Gremio> Gremios = new TreeMap<Integer, Gremio>();
    private static Map<Integer, Casa> Casas = new TreeMap<Integer, Casa>();
    private static Map<Integer, Mercadillo> PuestosMercadillos = new TreeMap<Integer, Mercadillo>();
    private static Map<Integer, Map<Integer, ArrayList<Mercadillo.ObjetoMercadillo>>> ObjMercadillos = new HashMap<Integer, Map<Integer, ArrayList<Mercadillo.ObjetoMercadillo>>>();
    private static Map<Integer, Personaje> Esposos = new TreeMap<Integer, Personaje>();
    private static Map<Integer, Animación> Animaciones = new TreeMap<Integer, Animación>();
    private static Map<Integer, Reto> Retos = new TreeMap<Integer, Reto>();
    private static Map<Integer, Tienda> Tiendas = new TreeMap<Integer, Tienda>();
    private static Map<Integer, Mascota> Mascotas = new TreeMap<Integer, Mascota>();
    private static Map<Integer, Mascota.MascotaModelo> MascotasModelos = new TreeMap<Integer, Mascota.MascotaModelo>();
    private static Map<Short, Mapa.Cercado> Cercados = new TreeMap<Short, Mapa.Cercado>();
    private static Map<Integer, Prisma> Prismas = new TreeMap<Integer, Prisma>();
    private static Map<Integer, Cofre> Cofres = new TreeMap<Integer, Cofre>();
    private static Map<Integer, Recaudador> Recaudadores = new TreeMap<Integer, Recaudador>();
    private static Map<Integer, RankingPVP> RankingsPVP = new TreeMap<Integer, RankingPVP>();
    private static CopyOnWriteArrayList<Personaje> Koliseo1 = new CopyOnWriteArrayList<Personaje>();
    private static CopyOnWriteArrayList<Personaje> Koliseo2 = new CopyOnWriteArrayList<Personaje>();
    private static CopyOnWriteArrayList<Personaje> Koliseo3 = new CopyOnWriteArrayList<Personaje>();
    private static CopyOnWriteArrayList<GrupoKoliseo> GrupoKoliseo1 = new CopyOnWriteArrayList<GrupoKoliseo>();
    private static CopyOnWriteArrayList<GrupoKoliseo> GrupoKoliseo2 = new CopyOnWriteArrayList<GrupoKoliseo>();
    private static CopyOnWriteArrayList<GrupoKoliseo> GrupoKoliseo3 = new CopyOnWriteArrayList<GrupoKoliseo>();
    private static Map<Integer, Encarnación> Encarnaciones = new TreeMap<Integer, Encarnación>();
    private static Map<Integer, Tutorial> Tutoriales = new TreeMap<Integer, Tutorial>();
    public static String liderRanking = "Ninguem";
    private static int sigIDLineaMerca;
    private static int sigIDObjeto;
    private static int sigIDPersonaje;
    private static int sigIDRecaudador;
    private static byte _estado;
    private static byte _gmAcceso;
    public static Personaje _salvador;
    public static long _tiempoUltSalvada;

    static {
        sigIDObjeto = 0;
        sigIDPersonaje = 0;
        sigIDRecaudador = -100;
        _estado = 1;
        _gmAcceso = 0;
        _salvador = null;
        _tiempoUltSalvada = 0L;
    }

    public static int getSigIDPersonaje() {
        return ++sigIDPersonaje;
    }

    public static int getSigIDRecaudador() {
        return sigIDRecaudador -= 3;
    }

    public static synchronized void salvarServidor() {
        if (_estado != 1 || LesGuardians._salvando) {
            System.out.println("Salvando servidor...");
            return;
        }
        try {
            if (System.currentTimeMillis() - _tiempoUltSalvada < 30000L) {
                System.out.println("O servidor tentou salvar, mas e necessario esperar 30 segundos entre cada SAVE. (MUNDO DOFUS)" + (System.currentTimeMillis() - _tiempoUltSalvada) + " milisegundos");
                return;
            }
            try {
                System.out.println("Iniciando save do servidor. (World)");
                _estado = (byte)2;
                LesGuardians._salvando = true;
                SQLManager.comenzarTransacciones();
                SQLManager.TIMER(false);
                Thread.sleep(2000L);
                TreeMap<Integer, Cuenta> _cuentas = new TreeMap<Integer, Cuenta>();
                _cuentas.putAll(Cuentas);
                TreeMap<Integer, Personaje> _personajes = new TreeMap<Integer, Personaje>();
                _personajes.putAll(Personajes);
                TreeMap<Integer, Objevivo> _objevivos = new TreeMap<Integer, Objevivo>();
                _objevivos.putAll(Objevivos);
                TreeMap<Integer, Encarnación> _encarnaciones = new TreeMap<Integer, Encarnación>();
                _encarnaciones.putAll(Encarnaciones);
                TreeMap<Integer, Tienda> _tiendas = new TreeMap<Integer, Tienda>();
                _tiendas.putAll(Tiendas);
                TreeMap<Integer, Prisma> _prismas = new TreeMap<Integer, Prisma>();
                _prismas.putAll(Prismas);
                TreeMap<Integer, Mascota> _mascotas = new TreeMap<Integer, Mascota>();
                _mascotas.putAll(Mascotas);
                TreeMap<Integer, Gremio> _gremios = new TreeMap<Integer, Gremio>();
                _gremios.putAll(Gremios);
                TreeMap<Integer, Recaudador> _recaudadores = new TreeMap<Integer, Recaudador>();
                _recaudadores.putAll(Recaudadores);
                TreeMap<Integer, Dragopavo> _dragopavos = new TreeMap<Integer, Dragopavo>();
                _dragopavos.putAll(Dragopavos);
                TreeMap<Integer, RankingPVP> _ranking = new TreeMap<Integer, RankingPVP>();
                _ranking.putAll(RankingsPVP);
                System.out.println("Salvando contas");
                for (Cuenta cuenta : _cuentas.values()) {
                    if (!cuenta.enLinea()) continue;
                    Thread.sleep(1000L);
                    SQLManager.SALVAR_CUENTA(cuenta);
                }
                SQLManager.ACTUALIZAR_NPC_KAMAS(Mundo.getNPCModelo(408));
                System.out.println("Salvando personagens");
                for (Personaje perso : _personajes.values()) {
                    if (!perso.enLinea()) continue;
                    Thread.sleep(50L);
                    SQLManager.SALVAR_PERSONAJE(perso, true);
                }
                System.out.println("Salvando itens vivos.");
                for (Objevivo objevivo : _objevivos.values()) {
                    Thread.sleep(50L);
                    SQLManager.SALVAR_OBJEVIVO(objevivo);
                }
                System.out.println("Salvando incarnacoes.");
                for (Encarnación encarnacion : _encarnaciones.values()) {
                    Thread.sleep(50L);
                    SQLManager.REPLACE_ENCARNACION(encarnacion);
                }
                System.out.println("Salvando itens de mercador.");
                for (Tienda tienda : _tiendas.values()) {
                    Thread.sleep(100L);
                    SQLManager.REPLACE_MERCANTE_OBJETOS(tienda);
                }
                System.out.println("Salvando areas");
                for (Area area : Areas.values()) {
                    SQLManager.ACTUALIZAR_AREA(area);
                }
                System.out.println("Salvando sub-areas");
                for (SubArea subarea : SubAreas.values()) {
                    SQLManager.ACTUALIZAR_SUBAREA(subarea);
                }
                System.out.println("Salvando prismas");
                for (Prisma prisma : _prismas.values()) {
                    Thread.sleep(100L);
                    if (Mapas.get(prisma.getMapaID()).getSubArea().getPrismaID() != prisma.getID()) {
                        SQLManager.DELETE_PRISMA(prisma.getID());
                        continue;
                    }
                    SQLManager.UPDATE_PRISMA(prisma);
                }
                System.out.println("Salvando pets");
                for (Mascota mascota : _mascotas.values()) {
                    Thread.sleep(100L);
                    SQLManager.REPLACE_MASCOTA(mascota);
                }
                System.out.println("Salvando guildas");
                for (Gremio gremio : _gremios.values()) {
                    Thread.sleep(100L);
                    SQLManager.REPLACE_GREMIO(gremio);
                }
                System.out.println("Salvando coletores");
                for (Recaudador recau : _recaudadores.values()) {
                    if (recau.getEstadoPelea() > 0) continue;
                    Thread.sleep(100L);
                    SQLManager.REPLACE_RECAUDADOR(recau);
                }
                System.out.println("Salvando padoques");
                for (Mapa.Cercado cercado : Cercados.values()) {
                    Thread.sleep(25L);
                    SQLManager.UPDATE_CERCADO(cercado);
                    SQLManager.UPDATE_CELDAS_OBJETO(cercado.getMapa().getID(), cercado.getStringCeldasObj());
                }
                System.out.println("Salvando montarias");
                for (Dragopavo montura : _dragopavos.values()) {
                    Thread.sleep(100L);
                    SQLManager.REPLACE_MONTURA(montura, true);
                }
                System.out.println("Salvando casas");
                for (Casa casa : Casas.values()) {
                    if (casa.getDue\u00f1oID() <= 0) continue;
                    Thread.sleep(100L);
                    SQLManager.UPDATE_CASA(casa);
                }
                System.out.println("Salvando Rank PvP");
                for (RankingPVP rank : _ranking.values()) {
                    Thread.sleep(10L);
                    SQLManager.UPDATE_RANKINGPVP(rank);
                }
                System.out.println("Salvando cofres");
                for (Cofre cofre : Cofres.values()) {
                    if (cofre.getDue\u00f1oID() <= 0) continue;
                    Thread.sleep(100L);
                    SQLManager.ACTUALIZAR_COFRE(cofre);
                }
                SQLManager.ACTUALIZAR_COFRE(Cofre.getCofrePorUbicacion(0, 0));
                System.out.println("Salvando msgs dos cofres");
                ArrayList<Mercadillo.ObjetoMercadillo> listaObjMerca = new ArrayList<Mercadillo.ObjetoMercadillo>();
                for (Mercadillo puesto : PuestosMercadillos.values()) {
                    Thread.sleep(10L);
                    listaObjMerca.addAll(puesto.todoListaObjMercaDeUnPuesto());
                }
                SQLManager.VACIA_Y_ACTUALIZA_OBJ_MERCADILLOS(listaObjMerca);
                _estado = 1;
                LesGuardians._salvando = false;
                System.out.println("O save do servidor foi concluido.");
                _tiempoUltSalvada = System.currentTimeMillis();
            }
            catch (ConcurrentModificationException e) {
                e.printStackTrace();
                _estado = 1;
                LesGuardians._salvando = false;
                System.out.println("Ocorreu um erro.");
                SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_salvador, "Ocorreu um erro (MUNDO DOFUS)");
                _salvador = null;
            }
            catch (Exception e) {
                e.printStackTrace();
                _estado = 1;
                LesGuardians._salvando = false;
                System.out.println("Erro ao salvar : " + e.getMessage());
                _salvador = null;
            }
        }
        finally {
            SQLManager.comenzarTransacciones();
            SQLManager.TIMER(true);
            _estado = 1;
            LesGuardians._salvando = false;
        }
    }

    public static synchronized int getSigIDObjeto() {
        return ++sigIDObjeto;
    }

    public static void crearServer() {
        System.out.println("=======>Datos Estáticos<=======");
		SQLManager.CARGAR_EXPERIENCIA();
		System.out.println("Se han cargado " + Experiencia.size() + " niveles de EXP.");
		SQLManager.CARGAR_HECHIZOS();
		System.out.println("Se han cargado " + Hechizos.size() + " hechizos.");
		SQLManager.CARGAR_MODELOS_MOB();
		System.out.println("Se han cargado " + MobModelos.size() + " modelos de mobs.");
		SQLManager.CARGAR_MODELOS_OBJETOS();
		System.out.println("Se han cargado " + ObjModelos.size() + " modelos de objetos.");
		SQLManager.CARGAR_MODELOS_NPC();
		System.out.println("Se han cargado " + NPCModelos.size() + " modelos de NPC.");
		SQLManager.CARGAR_PREGUNTAS();
		System.out.println("Se han cargado " + NPCPreguntas.size() + " preguntas de NPCs.");
		SQLManager.CARGAR_RESPUESTAS();
		System.out.println("Se han cargado " + NPCRespuesta.size() + " respuestas a los NPCs.");
		int numero = SQLManager.CARGAR_PRISMAS();
		if (numero > 0) {
		System.out.println("Se han cargado " + numero + " prismas de conquista.");
		}
		SQLManager.CARGAR_AREA();
		System.out.println("Se han cargado " + Areas.size() + " áreas.");
		SQLManager.CARGAR_SUBAREA();
		System.out.println("Se han cargado " + SubAreas.size() + " sub-áreas.");
		SQLManager.CARGAR_INTERACTIVOS();
		System.out.println("Se han cargado " + ObjInteractivos.size() + " objetos interactivos.");
		SQLManager.CARGAR_RECETAS();
		System.out.println("Se han cargado " + Recetas.size() + " recetas.");
		SQLManager.CARGAR_OFICIOS();
		System.out.println("Se han cargado " + Oficios.size() + " oficios.");
		SQLManager.CARGAR_OBJETOS_SETS();
		System.out.println("Se han cargado " + ObjetoSets.size() + " sets.");
		SQLManager.CARGAR_MAPAS();
		System.out.println("Se han cargado " + Mapas.size() + " mapas.");
		numero = SQLManager.CARGAR_MAPAS_FIXEADOS();
		System.out.println("Se han cargado " + numero + " mapas fix.");
		numero = SQLManager.CARGAR_TRIGGERS();
		System.out.println("Se han cargado " + numero + " triggers.");
		numero = SQLManager.CARGAR_FINALES_DE_COMBATE();
		System.out.println("Se han cargado " + numero + " acciones finales de combate.");
		numero = SQLManager.CARGAR_NPCS();
		System.out.println("Se han cargado " + numero + " NPCs.");
		numero = SQLManager.CARGAR_ACCIONES_USO_OBJETOS();
		System.out.println("Se han cargado " + numero + " acciones de objetos.");
		SQLManager.CARGAR_DROPS();
		System.out.println("Se han cargado todos los drops.");
		SQLManager.SELECT_ANIMACIONES();
		System.out.println("Se han cargado " + Animaciones.size() + " animaciones.");
		SQLManager.CARGAR_RETOS();
		System.out.println("Se han cargado " + Retos.size() + " retos de combate.");
		numero = SQLManager.CARGAR_COMIDAS_MASCOTAS();
		System.out.println("Se han cargado " + numero + " comidas de mascotas.");
		SQLManager.CARGAR_TUTORIALES();
		System.out.println("Se han cargado " + Tutoriales.size() + " tutoriales.");     
		numero = SQLManager.SELECT_ZAAPS();
		System.out.println("Se han cargado " + numero + " zaaps.");     
        System.out.println("=======>Datos Dinámicos<=======");     
        SQLManager.UPDATE_TODAS_CUENTAS_CERO();
		SQLManager.SELECT_OBJEVIVOS();
		if (Objevivos.size() > 0) {
		System.out.println("Se han cargado " + Objevivos.size() + " objevivos de PJs.");
		}
		SQLManager.SELECT_ENCARNACIONES();
		if (Encarnaciones.size() > 0) {
		System.out.println("Se han cargado " + Encarnaciones.size() + " encarnaciones de PJs.");
		}
		SQLManager.SELECT_OBJETOS_BDD_OTHER();
		System.out.println("Se han cargado " + Objetos.size() + " objetos de PJs.");
		SQLManager.CARGAR_MONTURAS();
		if (Dragopavos.size() > 0) {
		System.out.println("Se han cargado " + Dragopavos.size() + " dragopavos de PJs.");
		}
		SQLManager.CARGAR_CUENTAS();
		System.out.println("Se han cargado " + Cuentas.size() + " cuentas.");
		SQLManager.CARGAR_PERSONAJES();
		System.out.println("Se han cargado " + Personajes.size() + " personajes.");
		SQLManager.SELECT_RANKINGPVP();
		System.out.println("Se han cargado " + RankingsPVP.size() + " puestos  de ranking PVP.");
		if (RankingsPVP.size() > 0) {
			liderRanking = nombreLiderRankingPVP();
		}
		numero = SQLManager.CARGAR_MERCANTES();
		if (numero > 0) {
		System.out.println("Se han cargado " + numero + " PJs mercantes.");
		}
		SQLManager.CARGAR_GREMIOS();
		if (Gremios.size() > 0) {
		System.out.println("Se han cargado " + Gremios.size() + " gremios.");
		}
		numero = SQLManager.CARGAR_MIEMBROS_GREMIO();
		if (numero > 0) {
		System.out.println("Se han cargado " + numero + " miembros de gremio.");
		}
		numero = SQLManager.CARGAR_CERCADOS();
		System.out.println("Se han cargado " + numero + " cercados.");
		numero = SQLManager.CARGAR_RECAUDADORES();
		if (numero > 0) {
		System.out.println("Se han cargado " + numero + " recaudadores.");
		}
		numero = SQLManager.CARGAR_CASAS();
		System.out.println("Se han cargado " + numero + " casas.");
		SQLManager.CARGAR_COFRE();
		System.out.println("Se han cargado " + Cofres.size() + " cofres.");
		numero = SQLManager.SELECT_BANIP();
		if (numero > 0) {
		System.out.println("Se han cargado " + numero + " IPs baneadas.");
		}
		SQLManager.CARGAR_OBJETOS_MERCANTES();
		if (Tiendas.size() > 0) {
		System.out.println("Se han cargado " + Tiendas.size() + " objetos de PJs mercantes.");
		}
		SQLManager.CARGAR_MASCOTAS();
		if (Mascotas.size() > 0) {
		System.out.println("Se han cargado " + Mascotas.size() + " mascotas de PJs.");
		}
		SQLManager.SELECT_PUESTOS_MERCADILLOS();
		System.out.println("Se han cargado " + PuestosMercadillos.size() + " puestos de mercadillo.");
		SQLManager.SELECT_OBJETOS_MERCADILLOS();
		if (ObjMercadillos.size() > 0) {
		System.out.println("Se han cargado " + ObjMercadillos.size() + " objetos de mercadillo en venta.");
		}
		sigIDObjeto = SQLManager.getSigIDObjeto();
	}

    public static Area getArea(short area) {
        return Areas.get(area);
    }

    public static SuperArea getSuperArea(int superArea) {
        return SuperAreas.get(superArea);
    }

    public static SubArea getSubArea(int subArea) {
        return SubAreas.get(subArea);
    }

    public static void addArea(Area area) {
        Areas.put(area.getID(), area);
    }

    public static void addSuperArea(SuperArea superArea) {
        SuperAreas.put(superArea.getID(), superArea);
    }

    public static void addSubArea(SubArea subArea) {
        SubAreas.put(subArea.getID(), subArea);
    }

    public static void addRespuestaNPC(NPCModelo.RespuestaNPC respuesta) {
        NPCRespuesta.put(respuesta.getID(), respuesta);
    }

    public static NPCModelo.RespuestaNPC getRespuestaNPC(int id) {
        return NPCRespuesta.get(id);
    }

    public static void addExpLevel(int nivel, ExpNivel exp) {
        Experiencia.put(nivel, exp);
    }

    public static Cuenta getCuenta(int id) {
        return Cuentas.get(id);
    }

    public static void addPreguntaNPC(NPCModelo.PreguntaNPC pregunta) {
        NPCPreguntas.put(pregunta.getID(), pregunta);
    }

    public static NPCModelo.PreguntaNPC getPreguntaNPC(int id) {
        return NPCPreguntas.get(id);
    }

    public static NPCModelo getNPCModelo(int id) {
        return NPCModelos.get(id);
    }

    public static void addNpcModelo(NPCModelo npcModelo) {
        NPCModelos.put(npcModelo.getID(), npcModelo);
    }

    public static Mapa getMapa(short id) {
        return Mapas.get(id);
    }

    public static void addMapa(Mapa mapa) {
        if (!Mapas.containsKey(mapa.getID())) {
            Mapas.put(mapa.getID(), mapa);
        }
    }

    public static Mapa mapaPorCoordXYContinente(int mapaX, int mapaY, int idContinente) {
        for (Mapa mapa : Mapas.values()) {
            if (mapa.getX() != mapaX || mapa.getY() != mapaY || mapa.getSubArea().getArea().getSuperArea().getID() != idContinente) continue;
            return mapa;
        }
        return null;
    }

    public static String mapaPorCoordenadas(int mapaX, int mapaY) {
        String str = "";
        for (Mapa mapa : Mapas.values()) {
            if (mapa.getX() != mapaX || mapa.getY() != mapaY) continue;
            str = String.valueOf(str) + mapa.getID() + ", ";
        }
        return str;
    }

    public static void subirEstrellasMobs() {
        for (Mapa mapa : Mapas.values()) {
            mapa.subirEstrellasMobs();
        }
    }

    public static void subirEstrellasMobs(int cant) {
        for (Mapa mapa : Mapas.values()) {
            mapa.subirEstrellasCantidad(cant);
        }
    }

    public static Cuenta getCuentaPorNombre(String nombre) {
        return CuentasPorNombre.get(nombre.toLowerCase()) != null ? Cuentas.get(CuentasPorNombre.get(nombre.toLowerCase())) : null;
    }

    public static Personaje getPersonaje(int id) {
        return Personajes.get(id);
    }

    public static synchronized void addCuenta(Cuenta cuenta) {
        Cuentas.put(cuenta.getID(), cuenta);
        CuentasPorNombre.put(cuenta.getNombre().toLowerCase(), cuenta.getID());
    }

    public static synchronized void addPersonaje(Personaje perso) {
        if (perso.getID() > sigIDPersonaje) {
            sigIDPersonaje = perso.getID();
        }
        Personajes.put(perso.getID(), perso);
    }

    public static int getCantidadPersonajes() {
        return Personajes.size();
    }

    public static Personaje getPjPorNombre(String nombre) {
        ArrayList<Personaje> Ps = new ArrayList<Personaje>();
        Ps.addAll(Personajes.values());
        for (Personaje perso : Ps) {
            if (!perso.getNombre().equalsIgnoreCase(nombre)) continue;
            return perso;
        }
        return null;
    }

    public static synchronized void eliminarPj(Personaje perso, boolean totalmente) {
        Mundo.desconectarPerso(perso);
        for (Casa casa : Casas.values()) {
            if (casa.getDue\u00f1oID() != perso.getID()) continue;
            casa = new Casa(casa.getID(), casa.getMapaIDFuera(), casa.getCeldaIDFuera(), 1000000, 0, 0, 0, "-", 0, casa.getMapaIDDentro(), casa.getCeldaIDDentro());
            SQLManager.UPDATE_CASA(casa);
        }
        for (Mapa.Cercado cercado : Cercados.values()) {
            if (cercado.getDue\u00f1o() != perso.getID()) continue;
            String[] criando = cercado.getCriando().split(";");
            String x = "";
            String[] arrstring = criando;
            int n = criando.length;
            for (int i = 0; i < n; ++i) {
                String pavo = arrstring[i];
                int id = Integer.parseInt(pavo);
                if (x.length() > 0) {
                    x = String.valueOf(x) + ",";
                }
                x = String.valueOf(x) + id;
                Mundo.borrarDragopavo(id);
            }
            if (x.length() > 0) {
                SQLManager.DELETE_DRAGOPAVO_LISTA(x);
            }
            cercado = new Mapa.Cercado(0, cercado.getMapa(), cercado.getCeldaID(), cercado.getTama\u00f1o(), -1, 3000000, cercado.getColocarCelda(), "", cercado.getPuerta(), "", cercado.getCantObjMax(), "");
            SQLManager.UPDATE_CERCADO(cercado);
        }
        if (perso.getMiembroGremio() != null) {
            SQLManager.DELETE_MIEMBRO_GREMIO(perso.getID());
        }
        Mundo.delRankingPVP(perso.getID());
        if (totalmente) {
            SQLManager.DELETE_PERSONAJE(perso);
            Personajes.remove(perso.getID());
        }
    }

    public static String getAlineacionTodasSubareas() {
        String str = "";
        boolean primero = false;
        for (SubArea subarea : SubAreas.values()) {
            if (!subarea.getConquistable()) continue;
            if (primero) {
                str = String.valueOf(str) + "|";
            }
            str = String.valueOf(str) + subarea.getID() + ";" + subarea.getAlineacion();
            primero = true;
        }
        return str;
    }

    public static long getExpMinPersonaje(int nivel) {
        if (nivel > LesGuardians.MAX_NIVEL) {
            nivel = LesGuardians.MAX_NIVEL;
        }
        if (nivel < 1) {
            nivel = 1;
        }
        return Mundo.Experiencia.get((Object)Integer.valueOf((int)nivel))._personaje;
    }

    public static long getExpMaxPersonaje(int nivel) {
        if (nivel >= LesGuardians.MAX_NIVEL) {
            nivel = LesGuardians.MAX_NIVEL - 1;
        }
        if (nivel <= 1) {
            nivel = 1;
        }
        return Mundo.Experiencia.get((Object)Integer.valueOf((int)(nivel + 1)))._personaje;
    }

    public static long getExpMaxEncarnacion(int nivel) {
        if (nivel >= 50) {
            nivel = 49;
        }
        if (nivel <= 1) {
            nivel = 1;
        }
        return Mundo.Experiencia.get((Object)Integer.valueOf((int)(nivel + 1)))._encarnacion;
    }

    public static ExpNivel getExpNivel(int nivel) {
        return Experiencia.get(nivel);
    }

    public static ObjInteractivoModelo getObjInteractivoModelo(int id) {
        return ObjInteractivos.get(id);
    }

    public static void addObjInteractivo(ObjInteractivoModelo OIM) {
        ObjInteractivos.put(OIM.getID(), OIM);
    }

    public static Oficio getOficio(int id) {
        return Oficios.get(id);
    }

    public static void addOficio(Oficio oficio) {
        Oficios.put(oficio.getID(), oficio);
    }

    public static void addReceta(int id, ArrayList<Duo<Integer, Integer>> arrayDuos) {
        Recetas.put(id, arrayDuos);
    }

    public static ArrayList<Duo<Integer, Integer>> getReceta(int id) {
        return Recetas.get(id);
    }

    public static int getIDRecetaPorIngredientes(ArrayList<Integer> listaIDRecetas, Map<Integer, Integer> ingredientes) {
        if (listaIDRecetas == null) {
            return -1;
        }
        for (int id : listaIDRecetas) {
            ArrayList<Duo<Integer, Integer>> receta = Recetas.get(id);
            if (receta == null || receta.size() != ingredientes.size()) continue;
            boolean ok = true;
            for (Duo<Integer, Integer> ing : receta) {
                int segunda;
                if (ingredientes.get(ing._primero) == null) {
                    ok = false;
                    break;
                }
                int primera = ingredientes.get(ing._primero);
                if (primera == (segunda = ((Integer)ing._segundo).intValue())) continue;
                ok = false;
                break;
            }
            if (!ok) continue;
            return id;
        }
        return -1;
    }

    public static Cuenta getCuentaPorApodo(String apodo) {
        for (Cuenta C : Cuentas.values()) {
            if (!C.getApodo().equals(apodo)) continue;
            return C;
        }
        return null;
    }

    public static void addObjetoSet(ObjetoSet objetoSet) {
        ObjetoSets.put(objetoSet.getId(), objetoSet);
    }

    public static ObjetoSet getObjetoSet(int id) {
        return ObjetoSets.get(id);
    }

    public static int getNumeroObjetoSet() {
        return ObjetoSets.size();
    }

    public static synchronized int getSigIDMontura() {
        int max = -101;
        for (int a : Dragopavos.keySet()) {
            if (a >= max) continue;
            max = a;
        }
        return max - 3;
    }

    public static synchronized int getSigIDPrisma() {
        int max = -102;
        for (int a : Prismas.keySet()) {
            if (a >= max) continue;
            max = a;
        }
        return max - 3;
    }

    public static synchronized int getSigIDObjevivo() {
        int max = 0;
        for (int a : Objevivos.keySet()) {
            if (a <= max) continue;
            max = a;
        }
        return max + 1;
    }

    public static synchronized int getSigIdGremio() {
        if (Gremios.isEmpty()) {
            return 1;
        }
        int n = 0;
        for (int x : Gremios.keySet()) {
            if (n >= x) continue;
            n = x;
        }
        return n + 1;
    }

    public static synchronized int sigIDLineaMercadillo() {
        return ++sigIDLineaMerca;
    }

    public static synchronized void addGremio(Gremio gremio) {
        Gremios.put(gremio.getID(), gremio);
    }

    public static synchronized boolean nombreGremioUsado(String nombre) {
        for (Gremio gremio : Gremios.values()) {
            if (!gremio.getNombre().equalsIgnoreCase(nombre)) continue;
            return true;
        }
        return false;
    }

    public static synchronized boolean emblemaGremioUsado(String emblema) {
        for (Gremio gremio : Gremios.values()) {
            if (!gremio.getEmblema().equals(emblema)) continue;
            return true;
        }
        return false;
    }

    public static Gremio getGremio(int i) {
        return Gremios.get(i);
    }

    public static long getXPMaxGremio(int nivel) {
        if (nivel >= 200) {
            nivel = 199;
        }
        if (nivel <= 1) {
            nivel = 1;
        }
        return Mundo.Experiencia.get((Object)Integer.valueOf((int)(nivel + 1)))._gremio;
    }

    public static short getCeldaZaapPorMapaID(short i) {
		for (Entry<Short, Short> zaap : Constantes.ZAAPS.entrySet()) {
			if (zaap.getKey() == i)
				return zaap.getValue();
		}
		return -1;
	}

    public static short getCeldaCercadoPorMapaID(short i) {
        Mapa.Cercado cercado = Mundo.getMapa(i).getCercado();
        if (cercado != null && cercado.getCeldaID() > 0) {
            return cercado.getCeldaID();
        }
        return -1;
    }

    public static void borrarDragopavo(int id) {
        Dragopavos.remove(id);
    }

    public static void eliminarGremio(int id) {
        SQLManager.UPDATE_CASA_GREMIO_A_CERO(id);
        Gremios.remove(id);
        SQLManager.DELETE_GREMIO(id);
    }

    public static boolean usandoIP(String ip) {
        for (Cuenta c : Cuentas.values()) {
            if (c.getActualIP().compareTo(ip) != 0) continue;
            return true;
        }
        return false;
    }

    public static int cuentasIP(String ip) {
        int veces = 0;
        for (Cuenta c : Cuentas.values()) {
            if (c.getActualIP().compareTo(ip) != 0) continue;
            ++veces;
        }
        return veces;
    }

    public static void desconectarPerso(Personaje perso) {
        perso.setEnLinea(false);
        if (perso.getCasa() == null) {
            perso.stopRecuperarVida();
        }
    }

    public static synchronized Objeto objetoIniciarServer(int id, int modelo, int cant, int pos, String strStats, int idObvi) {
        Objeto.ObjetoModelo objModelo = Mundo.getObjModelo(modelo);
        if (objModelo == null) {
            System.out.println("La id del objeto bug " + id);
            SQLManager.DELETE_OBJETO(id);
            return null;
        }
        if (objModelo.getTipo() == 85) {
            return new PiedraAlma(id, cant, modelo, pos, strStats);
        }
        return new Objeto(id, modelo, cant, pos, strStats, idObvi);
    }

    public static void addHechizo(Hechizo hechizo) {
        Hechizos.put(hechizo.getID(), hechizo);
    }

    public static void addObjModelo(Objeto.ObjetoModelo obj) {
        ObjModelos.put(obj.getID(), obj);
    }

    public static Hechizo getHechizo(int id) {
        return Hechizos.get(id);
    }

    public static Objeto.ObjetoModelo getObjModelo(int id) {
        return ObjModelos.get(id);
    }

    public static void addMobModelo(int id, MobModelo mob) {
        MobModelos.put(id, mob);
    }

    public static MobModelo getMobModelo(int id) {
        return MobModelos.get(id);
    }

    public static ArrayList<Personaje> getPJsEnLinea() {
        ArrayList<Personaje> enLinea = new ArrayList<Personaje>();
        Map<Integer, Personaje> personajesO = Personajes;
        for (Personaje perso : personajesO.values()) {
            if (!perso.enLinea() || perso.getCuenta().getEntradaPersonaje() == null) continue;
            enLinea.add(perso);
        }
        return enLinea;
    }

    public static synchronized void addObjeto(Objeto obj, boolean salvarSQL) {
        if (obj == null) {
            return;
        }
        if (obj.getID() == 0) {
            obj.setID(Mundo.getSigIDObjeto());
        }
        Objetos.put(obj.getID(), obj);
        if (salvarSQL && obj.getModelo().getTipo() == 113) {
            SQLManager.SALVAR_OBJETO(obj);
        }
    }

    public static Objeto getObjeto(int id) {
        return Objetos.get(id);
    }

    public static synchronized void eliminarObjeto(int id) {
        Objetos.remove(id);
        SQLManager.DELETE_OBJETO(id);
        if (Mascotas.containsKey(id)) {
            SQLManager.BORRAR_MASCOTA(id);
            Mascotas.remove(id);
        }
    }

    public static void eliminarMascota(int id) {
        SQLManager.BORRAR_MASCOTA(id);
        Mascotas.remove(id);
    }

    public static Dragopavo getDragopavoPorID(int id) {
        return Dragopavos.get(id);
    }

    public static synchronized void addDragopavo(Dragopavo DP) {
        Dragopavos.put(DP.getID(), DP);
    }

    public static byte getEstado() {
        return _estado;
    }

    public static void setEstado(byte estado) {
        _estado = estado;
    }

    public static byte getGmAcceso() {
        return _gmAcceso;
    }

    public static void setGmAcceso(byte gmAcceso) {
        _gmAcceso = gmAcceso;
    }

    public static Mercadillo getPuestoMerca(int mapaID) {
        return PuestosMercadillos.get(mapaID);
    }

    public static synchronized void addObjMercadillo(int cuentaID, int idPuestoMerca, Mercadillo.ObjetoMercadillo objMercadillo) {
        if (ObjMercadillos.get(cuentaID) == null) {
            ObjMercadillos.put(cuentaID, new HashMap());
        }
        if (ObjMercadillos.get(cuentaID).get(idPuestoMerca) == null) {
            ObjMercadillos.get(cuentaID).put(idPuestoMerca, new ArrayList());
        }
        ObjMercadillos.get(cuentaID).get(idPuestoMerca).add(objMercadillo);
    }

    public static synchronized void borrarObjMercadillo(int cuentaID, int idPuestoMerca, Mercadillo.ObjetoMercadillo objMerca) {
        ObjMercadillos.get(cuentaID).get(idPuestoMerca).remove(objMerca);
    }

    public static int cantPuestosMercadillos() {
        return PuestosMercadillos.size();
    }

    public static int cantObjMercadillos() {
        int cantidad = 0;
        for (Map<Integer, ArrayList<Mercadillo.ObjetoMercadillo>> tempCuenta : ObjMercadillos.values()) {
            for (ArrayList<Mercadillo.ObjetoMercadillo> objMercadillo : tempCuenta.values()) {
                cantidad += objMercadillo.size();
            }
        }
        return cantidad;
    }

    public static synchronized void addPuestoMercadillo(Mercadillo mercadillo) {
        PuestosMercadillos.put(mercadillo.getIDMercadillo(), mercadillo);
    }

    public static Map<Integer, ArrayList<Mercadillo.ObjetoMercadillo>> getMisObjetos(int cuentaID) {
        if (ObjMercadillos.get(cuentaID) == null) {
            ObjMercadillos.put(cuentaID, new HashMap());
        }
        return ObjMercadillos.get(cuentaID);
    }

    public static Collection<Objeto.ObjetoModelo> getObjModelos() {
        return ObjModelos.values();
    }

    public static Personaje getCasado(int id) {
        return Esposos.get(id);
    }

    public static synchronized void addEsposo(int id, Personaje perso) {
        Personaje esposo = Esposos.get(id);
        if (esposo != null) {
            if (perso.getID() == esposo.getID()) {
                return;
            }
            if (esposo.enLinea()) {
                Esposos.remove(id);
                Esposos.put(id, perso);
                return;
            }
            if (perso.getCelda() == esposo.getCelda()) {
                return;
            }
            return;
        }
        Esposos.put(id, perso);
    }

    public static void discursoSacerdote(Personaje perso, Mapa mapa, int idSacerdote) {
        Personaje esposo = Esposos.get(0);
        Personaje esposa = Esposos.get(1);
        if (esposo.getEsposo() != 0) {
            SocketManager.ENVIAR_cs_CHAT_MENSAJE_A_MAPA(mapa, String.valueOf(esposo.getNombre()) + " n\u00e3o aceitou o casamento.", LesGuardians.COR_MSG);
            return;
        }
        if (esposa.getEsposo() != 0) {
            SocketManager.ENVIAR_cs_CHAT_MENSAJE_A_MAPA(mapa, String.valueOf(esposa.getNombre()) + " n\u00e3o aceitou o casamento.", LesGuardians.COR_MSG);
            return;
        }
        SocketManager.ENVIAR_cMK_CHAT_MENSAJE_MAPA(perso.getMapa(), "", -1, "Padre", String.valueOf(perso.getNombre()) + " Voc\u00ea aceita " + Mundo.getCasado(perso.getSexo() == 1 ? 0 : 1).getNombre() + " em casamento ?");
        SocketManager.ENVIAR_ACCION_JUEGO_CASARSE(mapa, 617, esposo == perso ? esposo.getID() : esposa.getID(), esposo == perso ? esposa.getID() : esposo.getID(), idSacerdote);
    }

    public static void casando(Personaje hombre, Personaje mujer, int isOK) {
        if (isOK > 0) {
            SocketManager.ENVIAR_cMK_CHAT_MENSAJE_MAPA(hombre.getMapa(), "", -1, "Padre", "Eu declaro " + hombre.getNombre() + " et " + mujer.getNombre() + " unidos pelos la\u00e7os do matrimonio.");
            hombre.esposoDe(mujer);
            mujer.esposoDe(hombre);
        } else {
            SocketManager.ENVIAR_Im_INFORMACION_A_MAPA(hombre.getMapa(), "048;" + hombre.getNombre() + "~" + mujer.getNombre());
        }
        Esposos.get(0).setEsOK(0);
        Esposos.get(1).setEsOK(0);
        Esposos.clear();
    }

    public static Collection<Objevivo> getTodosObjevivos() {
        return Objevivos.values();
    }

    public static Objevivo getObjevivos(int idObjevivo) {
        return Objevivos.get(idObjevivo);
    }

    public static synchronized void addObjevivo(Objevivo objevivo) {
        Objevivos.put(objevivo.getID(), objevivo);
    }

    public static Animación getAnimacion(int animacionId) {
        return Animaciones.get(animacionId);
    }

    public static void addAnimation(Animación animacion) {
        Animaciones.put(animacion.getId(), animacion);
    }

    public static void addReto(Reto reto) {
        Retos.put(reto.getId(), reto);
    }

    public static Reto getReto(int id) {
        return Retos.get(id);
    }

    public static synchronized Tienda nuevaTienda(int objetoID, int precio, int cantidad) {
        return new Tienda(objetoID, precio, cantidad);
    }

    public static synchronized void agregarTienda(Tienda tiendas, boolean salvarSQL) {
        Tiendas.put(tiendas.getIdObjeto(), tiendas);
        if (salvarSQL) {
            SQLManager.REPLACE_MERCANTE_OBJETOS(tiendas);
        }
    }

    public static Tienda getObjTienda(int id) {
        return Tiendas.get(id);
    }

    public static synchronized void borrarObjTienda(int id) {
        Tiendas.remove(id);
        SQLManager.DELETE_MERCANTE_OBJETOS(id);
    }

    public static Mascota getMascota(int id) {
        return Mascotas.get(id);
    }

    public static synchronized void addMascota(Mascota mascota) {
        Mascotas.put(mascota.getID(), mascota);
    }

    public static Collection<Mascota> getTodasMascotas() {
        return Mascotas.values();
    }

    public static void agregarMascotaModelo(int id, Mascota.MascotaModelo mascota) {
        MascotasModelos.put(id, mascota);
    }

    public static Mascota.MascotaModelo getMascotaModelo(int id) {
        return MascotasModelos.get(id);
    }

    public static Set<Integer> getIdTodasMascotas() {
        return Mascotas.keySet();
    }

    public static void agregarCasa(Casa casa) {
        Casas.put(casa.getID(), casa);
    }

    public static Map<Integer, Casa> getCasas() {
        return Casas;
    }

    public static Casa getCasa(int id) {
        return Casas.get(id);
    }

    public static synchronized void addCercado(Mapa.Cercado cercado) {
        Cercados.put(cercado.getMapa().getID(), cercado);
    }

    public static Mapa.Cercado getCercadoPorMapa(short mapa) {
        return Cercados.get(mapa);
    }

    public static Collection<Mapa.Cercado> todosCercados() {
        return Cercados.values();
    }

    public static synchronized void addPrisma(Prisma prisma) {
        Prismas.put(prisma.getID(), prisma);
    }

    public static Prisma getPrisma(int id) {
        return Prismas.get(id);
    }

    public static void borrarPrisma(int id) {
        Prismas.remove(id);
    }

    public static Collection<Prisma> todosPrismas() {
        if (Prismas.size() > 0) {
            return Prismas.values();
        }
        return null;
    }

    public static void addCofre(Cofre cofre) {
        Cofres.put(cofre.getID(), cofre);
    }

    public static Cofre getCofre(int id) {
        return Cofres.get(id);
    }

    public static Map<Integer, Cofre> getCofres() {
        return Cofres;
    }

    public static void addRecaudador(Recaudador recauda) {
        if (recauda.getID() < sigIDRecaudador) {
            sigIDRecaudador = recauda.getID();
        }
        Recaudadores.put(recauda.getID(), recauda);
    }

    public static Recaudador getRecaudador(int id) {
        return Recaudadores.get(id);
    }

    public static void borrarRecaudador(int id) {
        Recaudadores.remove(id);
        SQLManager.DELETE_RECAUDADOR(id);
    }

    public static Map<Integer, Recaudador> getTodosRecaudadores() {
        return Recaudadores;
    }

    public static Recaudador getRecauPorMapaID(short id) {
        for (Map.Entry<Integer, Recaudador> recau : Recaudadores.entrySet()) {
            if (recau.getValue().getMapaID() != id) continue;
            return Recaudadores.get(recau.getValue().getID());
        }
        return null;
    }

    public static int cantRecauDelGremio(int gremiodID) {
        int i = 0;
        for (Map.Entry<Integer, Recaudador> recau : Recaudadores.entrySet()) {
            if (recau.getValue().getGremioID() != gremiodID) continue;
            ++i;
        }
        return i;
    }

    public static void addRankingPVP(RankingPVP rank) {
        RankingsPVP.put(rank.getID(), rank);
    }

    public static void delRankingPVP(int id) {
        SQLManager.DELETE_RANKINGPVP(id);
        RankingsPVP.remove(id);
    }

    public static boolean estaRankingPVP(int id) {
        return RankingsPVP.get(id) != null;
    }

    public static RankingPVP getRanking(int id) {
        return RankingsPVP.get(id);
    }

    public static String nombreLiderRankingPVP() {
        String id = "";
        if (RankingsPVP.size() <= 0) {
            return id;
        }
        int vict = 0;
        int derr = 0;
        for (RankingPVP rank : RankingsPVP.values()) {
            if (rank.getVictorias() > vict) {
                vict = rank.getVictorias();
                id = rank.getNombre();
                derr = rank.getDerrotas();
                continue;
            }
            if (rank.getVictorias() != vict || rank.getDerrotas() > derr) continue;
            vict = rank.getVictorias();
            id = rank.getNombre();
            derr = rank.getDerrotas();
        }
        return id;
    }

    public static int idLiderRankingPVP() {
        int id = 0;
        if (RankingsPVP.size() <= 0) {
            return id;
        }
        int vict = 0;
        int derr = 0;
        for (RankingPVP rank : RankingsPVP.values()) {
            if (rank.getVictorias() > vict) {
                vict = rank.getVictorias();
                id = rank.getID();
                derr = rank.getDerrotas();
                continue;
            }
            if (rank.getVictorias() != vict || rank.getDerrotas() > derr) continue;
            vict = rank.getVictorias();
            id = rank.getID();
            derr = rank.getDerrotas();
        }
        return id;
    }

    public static float getBalanceMundo(int alineacion) {
        int cant = 0;
        for (SubArea subarea : SubAreas.values()) {
            if (subarea.getAlineacion() != alineacion) continue;
            ++cant;
        }
        if (cant == 0) {
            return 0.0f;
        }
        return (float)Math.rint(10 * cant / 4 / 10);
    }

    public static float getBalanceArea(Area area, int alineacion) {
        int cant = 0;
        for (SubArea subarea : SubAreas.values()) {
            if (subarea.getArea() != area || subarea.getAlineacion() != alineacion) continue;
            ++cant;
        }
        if (cant == 0) {
            return 0.0f;
        }
        return (float)Math.rint(1000 * cant / area.getSubAreas().size() / 10);
    }

    public static String prismasGeoposicion(int alineacion) {
        String str = "";
        boolean primero = false;
        int subareas = 0;
        for (SubArea subarea : SubAreas.values()) {
            if (!subarea.getConquistable()) continue;
            if (primero) {
                str = String.valueOf(str) + ";";
            }
            str = String.valueOf(str) + subarea.getID() + "," + (subarea.getAlineacion() == 0 ? -1 : subarea.getAlineacion()) + ",0,";
            str = Mundo.getPrisma(subarea.getPrismaID()) == null ? String.valueOf(str) + "0,1" : String.valueOf(str) + (subarea.getPrismaID() == 0 ? 0 : (int)Mundo.getPrisma(subarea.getPrismaID()).getMapaID()) + ",1";
            primero = true;
            ++subareas;
        }
        if (alineacion == 1) {
            str = String.valueOf(str) + "|" + Area._bontas;
        } else if (alineacion == 2) {
            str = String.valueOf(str) + "|" + Area._brakmars;
        }
        str = String.valueOf(str) + "|" + Areas.size() + "|";
        primero = false;
        for (Area area : Areas.values()) {
            if (area.getAlineacion() == 0) continue;
            if (primero) {
                str = String.valueOf(str) + ";";
            }
            str = String.valueOf(str) + area.getID() + "," + area.getAlineacion() + ",1," + (area.getPrismaID() == 0 ? 0 : 1);
            primero = true;
        }
        if (alineacion == 1) {
            str = String.valueOf(Area._bontas) + "|" + subareas + "|" + (subareas - (SubArea._bontas + SubArea._brakmars)) + "|" + str;
        } else if (alineacion == 2) {
            str = String.valueOf(Area._brakmars) + "|" + subareas + "|" + (subareas - (SubArea._bontas + SubArea._brakmars)) + "|" + str;
        }
        return str;
    }

    public static void addKoliseo1(Personaje perso) {
        if (Koliseo1.isEmpty()) {
            Koliseo1.add(perso);
        } else {
            for (Personaje persos : Koliseo1) {
                if (persos == null || persos.getNivel() < perso.getNivel()) continue;
                int index = Koliseo1.indexOf(persos);
                Koliseo1.add(index - 1, perso);
            }
        }
    }

    public static void addKoliseo2(Personaje perso) {
        if (Koliseo2.isEmpty()) {
            Koliseo2.add(perso);
        } else {
            for (Personaje persos : Koliseo2) {
                if (persos == null || persos.getNivel() < perso.getNivel()) continue;
                int index = Koliseo2.indexOf(persos);
                Koliseo2.add(index - 1, perso);
            }
        }
    }

    public static void addKoliseo3(Personaje perso) {
        if (Koliseo3.isEmpty()) {
            Koliseo3.add(perso);
        } else {
            for (Personaje persos : Koliseo3) {
                if (persos == null || persos.getNivel() < perso.getNivel()) continue;
                int index = Koliseo3.indexOf(persos);
                Koliseo3.add(index - 1, perso);
            }
        }
    }

    public static void crearGruposKoliseo1() {
        CopyOnWriteArrayList<Personaje> kolis1 = new CopyOnWriteArrayList<Personaje>();
        for (Personaje persos : Koliseo1) {
            if (persos == null || !persos.enLinea()) continue;
            kolis1.add(persos);
        }
        if (kolis1.size() < 6) {
            return;
        }
        int size = kolis1.size();
        for (int i = 0; i < size; i += 3) {
            Personaje koli1 = null;
            Personaje koli2 = null;
            Personaje koli3 = null;
            Random rand = new Random();
            int random = rand.nextInt(kolis1.size() - 1);
            koli1 = (Personaje)kolis1.get(random);
            kolis1.remove(random);
            random = rand.nextInt(kolis1.size() - 1);
            koli2 = (Personaje)kolis1.get(random);
            kolis1.remove(random);
            random = rand.nextInt(kolis1.size() - 1);
            koli3 = (Personaje)kolis1.get(random);
            kolis1.remove(random);
            if (koli1 == null || koli2 == null || koli3 == null) continue;
            Personaje.GrupoKoliseo grupo = new Personaje.GrupoKoliseo(koli1, koli2, koli3, 1);
            GrupoKoliseo1.add(grupo);
        }
    }

    public static void crearGruposKoliseo2() {
        CopyOnWriteArrayList<Personaje> kolis1 = new CopyOnWriteArrayList<Personaje>();
        for (Personaje persos : Koliseo2) {
            if (persos == null || !persos.enLinea()) continue;
            kolis1.add(persos);
        }
        if (kolis1.size() < 6) {
            return;
        }
        int size = kolis1.size();
        for (int i = 0; i < size; i += 3) {
            Personaje koli1 = null;
            Personaje koli2 = null;
            Personaje koli3 = null;
            Random rand = new Random();
            int random = rand.nextInt(kolis1.size() - 1);
            koli1 = (Personaje)kolis1.get(random);
            kolis1.remove(random);
            random = rand.nextInt(kolis1.size() - 1);
            koli2 = (Personaje)kolis1.get(random);
            kolis1.remove(random);
            random = rand.nextInt(kolis1.size() - 1);
            koli3 = (Personaje)kolis1.get(random);
            kolis1.remove(random);
            if (koli1 == null || koli2 == null || koli3 == null) continue;
            Personaje.GrupoKoliseo grupo = new Personaje.GrupoKoliseo(koli1, koli2, koli3, 1);
            GrupoKoliseo2.add(grupo);
        }
    }

    public static void crearGruposKoliseo3() {
        CopyOnWriteArrayList<Personaje> kolis1 = new CopyOnWriteArrayList<Personaje>();
        for (Personaje persos : Koliseo3) {
            if (persos == null || !persos.enLinea()) continue;
            kolis1.add(persos);
        }
        if (kolis1.size() < 6) {
            return;
        }
        int size = kolis1.size();
        for (int i = 0; i < size; i += 3) {
            Personaje koli1 = null;
            Personaje koli2 = null;
            Personaje koli3 = null;
            Random rand = new Random();
            int random = rand.nextInt(kolis1.size() - 1);
            koli1 = (Personaje)kolis1.get(random);
            kolis1.remove(random);
            random = rand.nextInt(kolis1.size() - 1);
            koli2 = (Personaje)kolis1.get(random);
            kolis1.remove(random);
            random = rand.nextInt(kolis1.size() - 1);
            koli3 = (Personaje)kolis1.get(random);
            kolis1.remove(random);
            if (koli1 == null || koli2 == null || koli3 == null) continue;
            Personaje.GrupoKoliseo grupo = new Personaje.GrupoKoliseo(koli1, koli2, koli3, 1);
            GrupoKoliseo3.add(grupo);
        }
    }

    public static void addEncarnacion(Encarnación encarnacion) {
        Encarnaciones.put(encarnacion.getID(), encarnacion);
    }

    public static Encarnación getEncarnacion(int id) {
        return Encarnaciones.get(id);
    }

    public static void borrarEncarnacion(int id) {
        Encarnaciones.remove(id);
        SQLManager.DELETE_ENCARNACION(id);
    }

    public static void addTutorial(Tutorial tutorial) {
        Tutoriales.put(tutorial.getID(), tutorial);
    }

    public static Tutorial getTutorial(int id) {
        return Tutoriales.get(id);
    }

    public static class Area {
        private short _id;
        private SuperArea _superArea;
        private String _nombre;
        private ArrayList<SubArea> _subAreas = new ArrayList();
        private int _alineacion;
        public static int _bontas = 0;
        public static int _brakmars = 0;
        private int _prisma = 0;

        public Area(short id, int superArea, String nombre, int alineacion, int prisma) {
            this._id = id;
            this._nombre = nombre;
            this._superArea = Mundo.getSuperArea(superArea);
            if (this._superArea == null) {
                this._superArea = new SuperArea(superArea);
                Mundo.addSuperArea(this._superArea);
            }
            this._alineacion = 0;
            this._prisma = 0;
            if (Mundo.getPrisma(prisma) != null) {
                this._alineacion = alineacion;
                this._prisma = prisma;
            }
            if (this._alineacion == 1) {
                ++_bontas;
            } else if (this._alineacion == 2) {
                ++_brakmars;
            }
        }

        public static int subareasBontas() {
            return _bontas;
        }

        public static int subareasBrakmars() {
            return _brakmars;
        }

        public int getAlineacion() {
            return this._alineacion;
        }

        public int getPrismaID() {
            return this._prisma;
        }

        public void setPrismaID(int prisma) {
            this._prisma = prisma;
        }

        public void setAlineacion(int alineacion) {
            if (this._alineacion == 1 && alineacion == -1) {
                --_bontas;
            } else if (this._alineacion == 2 && alineacion == -1) {
                --_brakmars;
            } else if (this._alineacion == -1 && alineacion == 1) {
                ++_bontas;
            } else if (this._alineacion == -1 && alineacion == 2) {
                ++_brakmars;
            }
            this._alineacion = alineacion;
        }

        public String getNombre() {
            return this._nombre;
        }

        public short getID() {
            return this._id;
        }

        public SuperArea getSuperArea() {
            return this._superArea;
        }

        public void addSubArea(SubArea sa) {
            this._subAreas.add(sa);
        }

        public ArrayList<SubArea> getSubAreas() {
            return this._subAreas;
        }

        public ArrayList<Mapa> getMapas() {
            ArrayList<Mapa> mapas = new ArrayList<Mapa>();
            for (SubArea SA : this._subAreas) {
                mapas.addAll(SA.getMapas());
            }
            return mapas;
        }
    }

    public static class Drop {
        private int _objModeloID;
        private int _prospeccion;
        private int _probabilidad;
        private int _maximo;

        public Drop(int obj, int prosp, int probabilidad, int max) {
            this._objModeloID = obj;
            this._prospeccion = prosp;
            this._probabilidad = probabilidad;
            this._maximo = max;
        }

        public void setDropMax(int max) {
            this._maximo = max;
        }

        public int getObjetoID() {
            return this._objModeloID;
        }

        public int getProspReq() {
            return this._prospeccion;
        }

        public int getProbabilidad() {
            return this._probabilidad;
        }

        public int getDropMax() {
            return this._maximo;
        }
    }

    public static class Duo<L, R> {
        public L _primero;
        public R _segundo;

        public Duo(L s, R i) {
            this._primero = s;
            this._segundo = i;
        }
    }

    public static class ExpNivel {
        public long _personaje;
        public int _oficio;
        public int _montura;
        public int _pvp;
        public long _gremio;
        public long _encarnacion;

        public ExpNivel(long perso, int oficio, int montura, int pvp, int encarnacion) {
            this._personaje = perso;
            this._oficio = oficio;
            this._montura = montura;
            this._pvp = pvp;
            this._gremio = this._personaje * 20L;
            this._encarnacion = encarnacion;
        }
    }

    public static class Intercambio {
        private Personaje _perso1;
        private Personaje _perso2;
        private PrintWriter _out1;
        private PrintWriter _out2;
        private long _kamas1 = 0L;
        private long _kamas2 = 0L;
        private ArrayList<Duo<Integer, Integer>> _objetos1 = new ArrayList();
        private ArrayList<Duo<Integer, Integer>> _objetos2 = new ArrayList();
        private boolean _ok1;
        private boolean _ok2;

        public Intercambio(Personaje p1, Personaje p2) {
            this._perso1 = p1;
            this._perso2 = p2;
            this._out1 = this._perso1.getCuenta().getEntradaPersonaje().getOut();
            this._out2 = this._perso2.getCuenta().getEntradaPersonaje().getOut();
        }

        public synchronized long getKamas(int id) {
            int i = 0;
            if (this._perso1.getID() == id) {
                i = 1;
            } else if (this._perso2.getID() == id) {
                i = 2;
            }
            if (i == 1) {
                return this._kamas1;
            }
            if (i == 2) {
                return this._kamas2;
            }
            return 0L;
        }

        public synchronized void botonOK(int id) {
            int i = 0;
            if (this._perso1.getID() == id) {
                i = 1;
            } else if (this._perso2.getID() == id) {
                i = 2;
            }
            if (i == 1) {
                this._ok1 = !this._ok1;
                SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, id);
                SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, id);
            } else if (i == 2) {
                this._ok2 = !this._ok2;
                SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, id);
                SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, id);
            } else {
                return;
            }
            if (this._ok1 && this._ok2) {
                this.aplicar();
            }
        }

        public synchronized void setKamas(int id, long k) {
            this._ok1 = false;
            this._ok2 = false;
            int i = 0;
            if (this._perso1.getID() == id) {
                i = 1;
            } else if (this._perso2.getID() == id) {
                i = 2;
            }
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, this._perso1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, this._perso1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, this._perso2.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, this._perso2.getID());
            if (i == 1) {
                this._kamas1 = k;
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out1, 'G', "", String.valueOf(k));
                SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out2, 'G', "", String.valueOf(k));
            } else if (i == 2) {
                this._kamas2 = k;
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out2, 'G', "", String.valueOf(k));
                SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out1, 'G', "", String.valueOf(k));
            }
        }

        public synchronized void cancel() {
            SocketManager.ENVIAR_EV_CERRAR_VENTANAS(this._out1);
            SocketManager.ENVIAR_EV_CERRAR_VENTANAS(this._out2);
            this._perso1.setIntercambiandoCon(0);
            this._perso2.setIntercambiandoCon(0);
            this._perso1.setIntercambio(null);
            this._perso2.setIntercambio(null);
            this._perso1.setOcupado(false);
            this._perso2.setOcupado(false);
        }

        public synchronized void aplicar() {
            Objeto nuevoObj;
            Objeto obj;
            int cant;
            int idObjeto;
            this._perso1.addKamas(-this._kamas1 + this._kamas2);
            this._perso2.addKamas(-this._kamas2 + this._kamas1);
            for (Duo<Integer, Integer> duo : this._objetos1) {
                idObjeto = (Integer)duo._primero;
                cant = (Integer)duo._segundo;
                if (cant == 0) continue;
                obj = Mundo.getObjeto(idObjeto);
                if (obj.getCantidad() - cant < 1) {
                    this._perso1.borrarObjetoSinEliminar(idObjeto);
                    SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._out1, idObjeto);
                    if (this._perso2.addObjetoSimilar(obj, true, -1)) {
                        Mundo.eliminarObjeto(idObjeto);
                        continue;
                    }
                    this._perso2.addObjetoPut(obj);
                    SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out2, obj);
                    continue;
                }
                obj.setCantidad(obj.getCantidad() - cant);
                SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._perso1, obj);
                nuevoObj = Objeto.clonarObjeto(obj, cant);
                if (this._perso2.addObjetoSimilar(nuevoObj, true, idObjeto)) continue;
                Mundo.addObjeto(nuevoObj, true);
                this._perso2.addObjetoPut(nuevoObj);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out2, nuevoObj);
            }
            for (Duo<Integer, Integer> duo : this._objetos2) {
                idObjeto = (Integer)duo._primero;
                cant = (Integer)duo._segundo;
                if (cant == 0) continue;
                obj = Mundo.getObjeto(idObjeto);
                if (obj.getCantidad() - cant < 1) {
                    this._perso2.borrarObjetoSinEliminar(idObjeto);
                    SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._out2, idObjeto);
                    if (this._perso1.addObjetoSimilar(obj, true, -1)) {
                        Mundo.eliminarObjeto(idObjeto);
                        continue;
                    }
                    this._perso1.addObjetoPut(obj);
                    SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out1, obj);
                    continue;
                }
                obj.setCantidad(obj.getCantidad() - cant);
                SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._perso2, obj);
                nuevoObj = Objeto.clonarObjeto(obj, cant);
                if (this._perso1.addObjetoSimilar(nuevoObj, true, idObjeto)) continue;
                Mundo.addObjeto(nuevoObj, true);
                this._perso1.addObjetoPut(nuevoObj);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out1, nuevoObj);
            }
            this._perso1.setIntercambiandoCon(0);
            this._perso2.setIntercambiandoCon(0);
            this._perso1.setIntercambio(null);
            this._perso2.setIntercambio(null);
            SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._perso1);
            SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._perso2);
            SocketManager.ENVIAR_EV_INTERCAMBIO_EFECTUADO(this._out1, 'a');
            SocketManager.ENVIAR_EV_INTERCAMBIO_EFECTUADO(this._out2, 'a');
            SocketManager.ENVIAR_As_STATS_DEL_PJ(this._perso1);
            SocketManager.ENVIAR_As_STATS_DEL_PJ(this._perso2);
            this._perso1.setOcupado(false);
            this._perso2.setOcupado(false);
            try {
                StringBuffer str = new StringBuffer();
                boolean primero = false;
                for (Duo<Integer, Integer> d : this._objetos1) {
                    if (primero) {
                        str.append(";");
                    }
                    str.append(d._primero + "," + d._segundo);
                    primero = true;
                }
                StringBuffer str2 = new StringBuffer();
                primero = false;
                for (Duo<Integer, Integer> d : this._objetos2) {
                    if (primero) {
                        str2.append(";");
                    }
                    str2.append(d._primero + "," + d._segundo);
                    primero = true;
                }
                SQLManager.AGREGAR_INTERCAMBIO(String.valueOf(this._perso1.getNombre()) + " a " + this._perso2.getNombre() + " los siguientes objetos: perso1 " + str.toString() + "   perso2 " + str2.toString());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        public synchronized void addObjeto(Objeto obj, int cant, int idPerso) {
            this._ok1 = false;
            this._ok2 = false;
            int idObj = obj.getID();
            int i = 0;
            i = this._perso1.getID() == idPerso ? 1 : 2;
            if (cant == 1) {
                cant = 1;
            }
            String str = String.valueOf(idObj) + "|" + cant;
            String add = "|" + obj.getModelo().getID() + "|" + obj.convertirStatsAString();
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, this._perso1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, this._perso1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, this._perso2.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, this._perso2.getID());
            if (i == 1) {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objetos1, idObj);
                if (duo != null) {
                    duo._segundo = (Integer)duo._segundo + cant;
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out1, 'O', "+", String.valueOf(idObj) + "|" + duo._segundo);
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out2, 'O', "+", String.valueOf(idObj) + "|" + duo._segundo + add);
                    return;
                }
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out1, 'O', "+", str);
                SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out2, 'O', "+", String.valueOf(str) + add);
                this._objetos1.add(new Duo<Integer, Integer>(idObj, cant));
            } else if (i == 2) {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objetos2, idObj);
                if (duo != null) {
                    duo._segundo = (Integer)duo._segundo + cant;
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out2, 'O', "+", String.valueOf(idObj) + "|" + duo._segundo);
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out1, 'O', "+", String.valueOf(idObj) + "|" + duo._segundo + add);
                    return;
                }
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out2, 'O', "+", str);
                SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out1, 'O', "+", String.valueOf(str) + add);
                this._objetos2.add(new Duo<Integer, Integer>(idObj, cant));
            }
        }

        public synchronized void borrarObjeto(Objeto obj, int cant, int idPerso) {
            Duo<Integer, Integer> duo;
            int i = 0;
            i = this._perso1.getID() == idPerso ? 1 : 2;
            this._ok1 = false;
            this._ok2 = false;
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, this._perso1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, this._perso1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, this._perso2.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, this._perso2.getID());
            int idObj = obj.getID();
            String add = "|" + obj.getModelo().getID() + "|" + obj.convertirStatsAString();
            if (i == 1) {
                Duo<Integer, Integer> duo2 = this.getDuoPorIDObjeto(this._objetos1, idObj);
                if (duo2 != null) {
                    int nuevaCantidad = (Integer)duo2._segundo - cant;
                    if (nuevaCantidad < 1) {
                        this._objetos1.remove(duo2);
                        SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out1, 'O', "-", String.valueOf(idObj));
                        SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out2, 'O', "-", String.valueOf(idObj));
                    } else {
                        duo2._segundo = nuevaCantidad;
                        SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out1, 'O', "+", String.valueOf(idObj) + "|" + nuevaCantidad);
                        SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out2, 'O', "+", String.valueOf(idObj) + "|" + nuevaCantidad + add);
                    }
                }
            } else if (i == 2 && (duo = this.getDuoPorIDObjeto(this._objetos2, idObj)) != null) {
                int nuevaCantidad = (Integer)duo._segundo - cant;
                if (nuevaCantidad < 1) {
                    this._objetos2.remove(duo);
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out1, 'O', "-", String.valueOf(idObj));
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out2, 'O', "-", String.valueOf(idObj));
                } else {
                    duo._segundo = nuevaCantidad;
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out1, 'O', "+", String.valueOf(idObj) + "|" + nuevaCantidad + add);
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out2, 'O', "+", String.valueOf(idObj) + "|" + nuevaCantidad);
                }
            }
        }

        private synchronized Duo<Integer, Integer> getDuoPorIDObjeto(ArrayList<Duo<Integer, Integer>> objetos, int id) {
            for (Duo<Integer, Integer> duo : objetos) {
                if ((Integer)duo._primero != id) continue;
                return duo;
            }
            return null;
        }

        public synchronized int getCantObjeto(int objetoID, int idPerso) {
            ArrayList<Duo<Integer, Integer>> objetos = this._perso1.getID() == idPerso ? this._objetos1 : this._objetos2;
            for (Duo<Integer, Integer> duo : objetos) {
                if ((Integer)duo._primero != objetoID) continue;
                return (Integer)duo._segundo;
            }
            return 0;
        }
    }

    public static class InvitarTaller {
        private Personaje _artesano1;
        private Personaje _cliente2;
        private PrintWriter _out1;
        private PrintWriter _out2;
        private long _kamasPago = 0L;
        private long _kamasSiSeConsigue = 0L;
        private ArrayList<Duo<Integer, Integer>> _objArtesano1 = new ArrayList();
        private ArrayList<Duo<Integer, Integer>> _objCliente2 = new ArrayList();
        private boolean _ok1;
        private boolean _ok2;
        private int _maxIngredientes;
        private ArrayList<Duo<Integer, Integer>> _objetosPago = new ArrayList();
        private ArrayList<Duo<Integer, Integer>> _objetosSiSeConsegui = new ArrayList();

        public InvitarTaller(Personaje p1, Personaje p2, int max) {
            this._artesano1 = p1;
            this._cliente2 = p2;
            this._out1 = this._artesano1.getCuenta().getEntradaPersonaje().getOut();
            this._out2 = this._cliente2.getCuenta().getEntradaPersonaje().getOut();
            this._maxIngredientes = max;
        }

        public long getKamasSiSeConsigue() {
            return this._kamasSiSeConsigue;
        }

        public long getKamasPaga() {
            return this._kamasPago;
        }

        public synchronized void botonOK(int id) {
            int i = 0;
            if (this._artesano1.getID() == id) {
                i = 1;
            } else if (this._cliente2.getID() == id) {
                i = 2;
            }
            if (i == 1) {
                this._ok1 = !this._ok1;
                SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, id);
                SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, id);
            } else if (i == 2) {
                this._ok2 = !this._ok2;
                SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, id);
                SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, id);
            } else {
                return;
            }
            if (this._ok1 && this._ok2) {
                this.aplicar();
            }
        }

        public void setKamas(int id, long k, long kamasT) {
            this._ok1 = false;
            this._ok2 = false;
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, this._cliente2.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, this._cliente2.getID());
            if (id == 1) {
                long kamasTotal = this._kamasSiSeConsigue + k;
                if (kamasTotal > kamasT) {
                    k = kamasT - this._kamasSiSeConsigue;
                }
                this._kamasPago = k;
            } else {
                long kamasTotal = this._kamasPago + k;
                if (kamasTotal > kamasT) {
                    k = kamasT - this._kamasPago;
                }
                this._kamasSiSeConsigue = k;
            }
            SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out1, id, "G", "+", String.valueOf(k));
            SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out2, id, "G", "+", String.valueOf(k));
        }

        public synchronized void cancel() {
            SocketManager.ENVIAR_EV_CERRAR_VENTANAS(this._out1);
            SocketManager.ENVIAR_EV_CERRAR_VENTANAS(this._out2);
            this._artesano1.setIntercambiandoCon(0);
            this._cliente2.setIntercambiandoCon(0);
            this._artesano1.setTallerInvitado(null);
            this._cliente2.setTallerInvitado(null);
            this._artesano1.setOcupado(false);
            this._cliente2.setOcupado(false);
        }

        public void aplicar() {
            Oficio.AccionTrabajo trabajo = this._artesano1.getHaciendoTrabajo();
            boolean resultado = trabajo.iniciarTrabajoPago(this._artesano1, this._cliente2, this._objArtesano1, this._objCliente2);
            Oficio.StatsOficio oficio = this._artesano1.getOficioPorTrabajo(trabajo.getIDTrabajo());
            if (oficio != null) {
                Objeto nuevoObj;
                Objeto obj;
                int cant;
                int idObjeto;
                if (resultado) {
                    this._cliente2.setKamas(this._cliente2.getKamas() - this._kamasSiSeConsigue);
                    this._artesano1.setKamas(this._artesano1.getKamas() + this._kamasSiSeConsigue);
                    for (Duo<Integer, Integer> duo : this._objetosSiSeConsegui) {
                        idObjeto = (Integer)duo._primero;
                        cant = (Integer)duo._segundo;
                        if (cant == 0) continue;
                        obj = Mundo.getObjeto(idObjeto);
                        if (obj.getCantidad() - cant < 1) {
                            this._cliente2.borrarObjetoSinEliminar(idObjeto);
                            SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._out2, idObjeto);
                            if (this._artesano1.addObjetoSimilar(obj, true, -1)) {
                                Mundo.eliminarObjeto(idObjeto);
                                continue;
                            }
                            this._artesano1.addObjetoPut(obj);
                            SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out1, obj);
                            continue;
                        }
                        obj.setCantidad(obj.getCantidad() - cant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._cliente2, obj);
                        nuevoObj = Objeto.clonarObjeto(obj, cant);
                        if (this._artesano1.addObjetoSimilar(nuevoObj, true, idObjeto)) continue;
                        Mundo.addObjeto(nuevoObj, true);
                        this._artesano1.addObjetoPut(nuevoObj);
                        SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out1, nuevoObj);
                    }
                }
                if (!oficio.esGratisSiFalla() || resultado) {
                    this._cliente2.setKamas(this._cliente2.getKamas() - this._kamasPago);
                    this._artesano1.setKamas(this._artesano1.getKamas() + this._kamasPago);
                    for (Duo<Integer, Integer> duo : this._objetosPago) {
                        idObjeto = (Integer)duo._primero;
                        cant = (Integer)duo._segundo;
                        if (cant == 0) continue;
                        obj = Mundo.getObjeto(idObjeto);
                        if (obj.getCantidad() - cant < 1) {
                            this._cliente2.borrarObjetoSinEliminar(idObjeto);
                            SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._out2, idObjeto);
                            if (this._artesano1.addObjetoSimilar(obj, true, -1)) {
                                Mundo.eliminarObjeto(idObjeto);
                                continue;
                            }
                            this._artesano1.addObjetoPut(obj);
                            SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out1, obj);
                            continue;
                        }
                        obj.setCantidad(obj.getCantidad() - cant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._cliente2, obj);
                        nuevoObj = Objeto.clonarObjeto(obj, cant);
                        if (this._artesano1.addObjetoSimilar(nuevoObj, true, idObjeto)) continue;
                        Mundo.addObjeto(nuevoObj, true);
                        this._artesano1.addObjetoPut(nuevoObj);
                        SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out1, nuevoObj);
                    }
                }
            }
            this._objetosSiSeConsegui.clear();
            this._objetosPago.clear();
            this._objArtesano1.clear();
            this._objCliente2.clear();
            this._kamasPago = 0L;
            this._kamasSiSeConsigue = 0L;
            SocketManager.ENVIAR_As_STATS_DEL_PJ(this._artesano1);
            SocketManager.ENVIAR_As_STATS_DEL_PJ(this._cliente2);
            SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano1, this._artesano1.getForjaEcK());
            SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._cliente2, this._cliente2.getForjaEcK());
        }

        public synchronized void addObjeto(Objeto obj, int cant, int idPerso) {
            if (this.cantObjetosActual() >= this._maxIngredientes) {
                return;
            }
            this._ok1 = false;
            this._ok2 = false;
            int idObj = obj.getID();
            int i = 0;
            i = this._artesano1.getID() == idPerso ? 1 : 2;
            if (cant == 1) {
                cant = 1;
            }
            String str = String.valueOf(idObj) + "|" + cant;
            String add = "|" + obj.getModelo().getID() + "|" + obj.convertirStatsAString();
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, this._cliente2.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, this._cliente2.getID());
            if (i == 1) {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objArtesano1, idObj);
                if (duo != null) {
                    duo._segundo = (Integer)duo._segundo + cant;
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out1, 'O', "+", idObj + "|" + duo._segundo);
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out2, 'O', "+", idObj + "|" + duo._segundo + add);
                    return;
                }
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out1, 'O', "+", str);
                SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out2, 'O', "+", String.valueOf(str) + add);
                this._objArtesano1.add(new Duo<Integer, Integer>(idObj, cant));
            } else if (i == 2) {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objCliente2, idObj);
                if (duo != null) {
                    duo._segundo = (Integer)duo._segundo + cant;
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out2, 'O', "+", idObj + "|" + duo._segundo);
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out1, 'O', "+", idObj + "|" + duo._segundo + add);
                    return;
                }
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out2, 'O', "+", str);
                SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out1, 'O', "+", String.valueOf(str) + add);
                this._objCliente2.add(new Duo<Integer, Integer>(idObj, cant));
            }
        }

        public synchronized void borrarObjeto(Objeto obj, int cant, int idPerso) {
            int i = 0;
            i = this._artesano1.getID() == idPerso ? 1 : 2;
            this._ok1 = false;
            this._ok2 = false;
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, this._cliente2.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, this._cliente2.getID());
            int idObj = obj.getID();
            String add = "|" + obj.getModelo().getID() + "|" + obj.convertirStatsAString();
            if (i == 1) {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objArtesano1, idObj);
                int nuevaCantidad = (Integer)duo._segundo - cant;
                if (nuevaCantidad < 1) {
                    this._objArtesano1.remove(duo);
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out1, 'O', "-", String.valueOf(idObj));
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out2, 'O', "-", String.valueOf(idObj));
                } else {
                    duo._segundo = nuevaCantidad;
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out1, 'O', "+", String.valueOf(idObj) + "|" + nuevaCantidad);
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out2, 'O', "+", String.valueOf(idObj) + "|" + nuevaCantidad + add);
                }
            } else if (i == 2) {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objCliente2, idObj);
                int nuevaCantidad = (Integer)duo._segundo - cant;
                if (nuevaCantidad < 1) {
                    this._objCliente2.remove(duo);
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out1, 'O', "-", String.valueOf(idObj));
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out2, 'O', "-", String.valueOf(idObj));
                } else {
                    duo._segundo = nuevaCantidad;
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out1, 'O', "+", String.valueOf(idObj) + "|" + nuevaCantidad + add);
                    SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out2, 'O', "+", String.valueOf(idObj) + "|" + nuevaCantidad);
                }
            }
        }

        public synchronized void addObjetoPaga(Objeto obj, int cant, int idPago) {
            if (this.cantObjetosActual() >= this._maxIngredientes) {
                return;
            }
            this._ok1 = false;
            this._ok2 = false;
            int idObj = obj.getID();
            if (cant == 1) {
                cant = 1;
            }
            String str = String.valueOf(idObj) + "|" + cant;
            String add = "|" + obj.getModelo().getID() + "|" + obj.convertirStatsAString();
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, this._cliente2.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, this._cliente2.getID());
            if (idPago == 1) {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objetosPago, idObj);
                if (duo != null) {
                    duo._segundo = (Integer)duo._segundo + cant;
                    SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out1, idPago, "O", "+", String.valueOf(idObj) + "|" + duo._segundo + add);
                    SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out2, idPago, "O", "+", String.valueOf(idObj) + "|" + duo._segundo);
                    return;
                }
                SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out1, idPago, "O", "+", String.valueOf(str) + add);
                SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out2, idPago, "O", "+", str);
                this._objetosPago.add(new Duo<Integer, Integer>(idObj, cant));
            } else {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objetosSiSeConsegui, idObj);
                if (duo != null) {
                    duo._segundo = (Integer)duo._segundo + cant;
                    SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out1, idPago, "O", "+", String.valueOf(idObj) + "|" + duo._segundo + add);
                    SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out2, idPago, "O", "+", String.valueOf(idObj) + "|" + duo._segundo);
                    return;
                }
                SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out1, idPago, "O", "+", String.valueOf(str) + add);
                SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out2, idPago, "O", "+", str);
                this._objetosSiSeConsegui.add(new Duo<Integer, Integer>(idObj, cant));
            }
        }

        public synchronized void borrarObjetoPaga(Objeto obj, int cant, int idPago) {
            int idObj = obj.getID();
            this._ok1 = false;
            this._ok2 = false;
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok1, this._artesano1.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out1, this._ok2, this._cliente2.getID());
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out2, this._ok2, this._cliente2.getID());
            String add = "|" + obj.getModelo().getID() + "|" + obj.convertirStatsAString();
            if (idPago == 1) {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objetosPago, idObj);
                if (duo == null) {
                    return;
                }
                int nuevaCantidad = (Integer)duo._segundo - cant;
                SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out1, idPago, "O", "-", String.valueOf(idObj));
                SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out2, idPago, "O", "-", String.valueOf(idObj));
                if (nuevaCantidad < 1) {
                    this._objetosPago.remove(duo);
                } else {
                    duo._segundo = nuevaCantidad;
                    SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out1, idPago, "O", "+", String.valueOf(idObj) + "|" + nuevaCantidad + add);
                    SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out2, idPago, "O", "+", String.valueOf(idObj) + "|" + nuevaCantidad);
                }
            } else {
                Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objetosSiSeConsegui, idObj);
                if (duo == null) {
                    return;
                }
                int nuevaCantidad = (Integer)duo._segundo - cant;
                SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out1, idPago, "O", "-", String.valueOf(idObj));
                SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out2, idPago, "O", "-", String.valueOf(idObj));
                if (nuevaCantidad < 1) {
                    this._objetosSiSeConsegui.remove(duo);
                } else {
                    duo._segundo = nuevaCantidad;
                    SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out1, idPago, "O", "+", String.valueOf(idObj) + "|" + nuevaCantidad + add);
                    SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(this._out2, idPago, "O", "+", String.valueOf(idObj) + "|" + nuevaCantidad);
                }
            }
        }

        private synchronized Duo<Integer, Integer> getDuoPorIDObjeto(ArrayList<Duo<Integer, Integer>> objetos, int id) {
            for (Duo<Integer, Integer> duo : objetos) {
                if ((Integer)duo._primero != id) continue;
                return duo;
            }
            return null;
        }

        public synchronized int getCantObjeto(int idObj, int idPerso) {
            ArrayList<Duo<Integer, Integer>> objetos = this._artesano1.getID() == idPerso ? this._objArtesano1 : this._objCliente2;
            for (Duo<Integer, Integer> duo : objetos) {
                if ((Integer)duo._primero != idObj) continue;
                return (Integer)duo._segundo;
            }
            return 0;
        }

        public synchronized int getCantObjetoPago(int idObj, int idPerso) {
            ArrayList<Duo<Integer, Integer>> objetos = idPerso == 1 ? this._objetosPago : this._objetosSiSeConsegui;
            for (Duo<Integer, Integer> duo : objetos) {
                if ((Integer)duo._primero != idObj) continue;
                return (Integer)duo._segundo;
            }
            return 0;
        }

        public int cantObjetosActual() {
            int cant = this._objArtesano1.size() + this._objCliente2.size();
            return cant;
        }
    }

    public static class ObjInteractivoModelo {
        private int _id;
        private int _tiempoRespuesta;
        private int _duracion;
        private int _animacionnPJ;
        private boolean _caminable;

        public ObjInteractivoModelo(int id, int tiempoRespuesta, int duracion, int spritePJ, boolean caminable) {
            this._id = id;
            this._tiempoRespuesta = tiempoRespuesta;
            this._duracion = duracion;
            this._animacionnPJ = spritePJ;
            this._caminable = caminable;
        }

        public int getID() {
            return this._id;
        }

        public boolean esCaminable() {
            return this._caminable;
        }

        public int getTiempoRespuesta() {
            return this._tiempoRespuesta;
        }

        public int getDuracion() {
            return this._duracion;
        }

        public int getAnimacionPJ() {
            return this._animacionnPJ;
        }
    }

    public static class ObjetoSet {
        private int _id;
        private ArrayList<Objeto.ObjetoModelo> _objetosModelos = new ArrayList();
        private ArrayList<Personaje.Stats> _bonus = new ArrayList();

        public ObjetoSet(int id, String items, String bonuses) {
            this._id = id;
            for (String str : items.split(",")) {
                try {
                    Objeto.ObjetoModelo t = Mundo.getObjModelo(Integer.parseInt(str.trim()));
                    if (t == null) continue;
                    this._objetosModelos.add(t);
                }
                catch (Exception t) {
                    // empty catch block
                }
            }
            this._bonus.add(new Personaje.Stats());
            for (String str : bonuses.split(";")) {
                Personaje.Stats S = new Personaje.Stats();
                for (String str2 : str.split(",")) {
                    try {
                        String[] infos = str2.split(":");
                        int stat = Integer.parseInt(infos[0]);
                        int value = Integer.parseInt(infos[1]);
                        S.addStat(stat, value);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                this._bonus.add(S);
            }
        }

        public int getId() {
            return this._id;
        }

        public Personaje.Stats getBonusStatPorNroObj(int numero) {
            if (numero > this._bonus.size()) {
                return new Personaje.Stats();
            }
            return this._bonus.get(numero - 1);
        }

        public ArrayList<Objeto.ObjetoModelo> getObjetosModelos() {
            return this._objetosModelos;
        }
    }

    public static class SubArea {
        private int _id;
        private Area _area;
        private int _alineacion;
        private String _nombre;
        private ArrayList<Mapa> _mapas = new ArrayList();
        private boolean _conquistable;
        private int _prisma;
        public static int _bontas = 0;
        public static int _brakmars = 0;

        public SubArea(int id, short areaID, int alineacion, String nombre, int conquistable, int prisma) {
            this._id = id;
            this._nombre = nombre;
            this._area = Mundo.getArea(areaID);
            this._alineacion = 0;
            this._conquistable = conquistable == 0;
            this._prisma = prisma;
            this._prisma = 0;
            if (Mundo.getPrisma(prisma) != null) {
                this._alineacion = alineacion;
                this._prisma = prisma;
            }
            if (this._alineacion == 1) {
                ++_bontas;
            } else if (this._alineacion == 2) {
                ++_brakmars;
            }
        }

        public String getNombre() {
            return this._nombre;
        }

        public int getPrismaID() {
            return this._prisma;
        }

        public void setPrismaID(int prisma) {
            this._prisma = prisma;
        }

        public boolean getConquistable() {
            return this._conquistable;
        }

        public int getID() {
            return this._id;
        }

        public Area getArea() {
            return this._area;
        }

        public int getAlineacion() {
            return this._alineacion;
        }

        public void setAlineacion(int alineacion) {
            if (this._alineacion == 1 && alineacion == -1) {
                --_bontas;
            } else if (this._alineacion == 2 && alineacion == -1) {
                --_brakmars;
            } else if (this._alineacion == -1 && alineacion == 1) {
                ++_bontas;
            } else if (this._alineacion == -1 && alineacion == 2) {
                ++_brakmars;
            }
            this._alineacion = alineacion;
        }

        public ArrayList<Mapa> getMapas() {
            return this._mapas;
        }

        public void addMapa(Mapa mapa) {
            this._mapas.add(mapa);
        }

        public static int subareasBontas() {
            return _bontas;
        }

        public static int subareasBrakmars() {
            return _brakmars;
        }
    }

    public static class SuperArea {
        private int _id;
        private ArrayList<Area> _areas = new ArrayList();

        public SuperArea(int id) {
            this._id = id;
        }

        public void addArea(Area area) {
            this._areas.add(area);
        }

        public int getID() {
            return this._id;
        }
    }

    public static class Trueque {
        private Personaje _perso;
        private PrintWriter _out;
        private ArrayList<Duo<Integer, Integer>> _objetos = new ArrayList();
        private boolean _ok;
        private String _objetoPedir = "";
        private String _objetoDar = "";
        private int _objetoConseguir = -1;
        private int _cantObjConseguir = -1;
        private boolean _resucitar = false;
        private int _idMascota = -1;

        public Trueque(Personaje perso, String Objetopedir, String Objetodar) {
            this._perso = perso;
            this._objetoPedir = Objetopedir;
            this._objetoDar = Objetodar;
            if (this._objetoDar.equalsIgnoreCase("resucitar")) {
                this._resucitar = true;
            }
            this._out = this._perso.getCuenta().getEntradaPersonaje().getOut();
        }

        public synchronized void botonOK(int id) {
            boolean i = false;
            if (this._perso.getID() == id) {
                i = true;
            }
            if (!i) {
                return;
            }
            this._ok = !this._ok;
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out, this._ok, id);
            if (this._ok) {
                this.aplicar();
            }
        }

        public synchronized void cancel() {
            SocketManager.ENVIAR_EV_CERRAR_VENTANAS(this._out);
            this._perso.setTrueque(null);
        }

        public synchronized void aplicar() {
            Objeto nuevoObjeto;
            for (Duo<Integer, Integer> duo : this._objetos) {
                int idObj = (Integer)duo._primero;
                int cant = (Integer)duo._segundo;
                if (cant == 0) continue;
                if (!this._perso.tieneObjetoID(idObj)) {
                    cant = 0;
                    continue;
                }
                Objeto obj = Mundo.getObjeto(idObj);
                int nuevaCant = obj.getCantidad() - cant;
                if (this._resucitar) {
                    if (nuevaCant < 1) {
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._out, idObj);
                        if (obj.getModelo().getTipo() != 90) {
                            this._perso.borrarObjetoSinEliminar(idObj);
                            Mundo.eliminarObjeto(idObj);
                            continue;
                        }
                        this._idMascota = idObj;
                        continue;
                    }
                    obj.setCantidad(nuevaCant);
                    SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._perso, obj);
                    continue;
                }
                if (nuevaCant < 1) {
                    this._perso.borrarObjetoSinEliminar(idObj);
                    SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._out, idObj);
                    Mundo.eliminarObjeto(idObj);
                    continue;
                }
                obj.setCantidad(nuevaCant);
                SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._perso, obj);
            }
            if (this._resucitar) {
                if (this._idMascota != -1) {
                    Objeto objMasc = Mundo.getObjeto(this._idMascota);
                    objMasc.setCantidad(1);
                    objMasc.setPosicion(-1);
                    objMasc.setIDModelo(Constantes.resucitarMascota(objMasc.getModelo().getID()));
                    SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out, objMasc);
                }
            } else if (this._objetoConseguir != -1 && this._cantObjConseguir != -1 && !this._perso.addObjetoSimilar(nuevoObjeto = Mundo.getObjModelo(this._objetoConseguir).crearObjDesdeModelo(this._cantObjConseguir, false), true, -1)) {
                Mundo.addObjeto(nuevoObjeto, true);
                this._perso.addObjetoPut(nuevoObjeto);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._out, nuevoObjeto);
            }
            this._perso.setTrueque(null);
            SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._perso);
            SocketManager.ENVIAR_EV_INTERCAMBIO_EFECTUADO(this._out, 'a');
            SocketManager.ENVIAR_As_STATS_DEL_PJ(this._perso);
            this._perso.setOcupado(false);
        }

        public synchronized void addObjetoTrueque(int idObjeto, int cantObj) {
            this._ok = false;
            if (cantObj == 1) {
                cantObj = 1;
            }
            String str = String.valueOf(idObjeto) + "|" + cantObj;
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out, this._ok, this._perso.getID());
            Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objetos, idObjeto);
            if (duo != null) {
                duo._segundo = (Integer)duo._segundo + cantObj;
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out, 'O', "+", String.valueOf(idObjeto) + "|" + duo._segundo);
                if (this._resucitar) {
                    if (this._idMascota != -1) {
                        SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "-", "" + this._idMascota);
                    }
                } else if (this._objetoConseguir != -1) {
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "-", "1");
                }
                this._objetoConseguir = -1;
                this._idMascota = -1;
                String[] pedir = this._objetoPedir.split("\\|");
                int cantSolicitadas = 0;
                int j = 0;
                block0: for (Duo<Integer, Integer> acouple : this._objetos) {
                    Objeto.ObjetoModelo objModelo = Mundo.getObjeto((Integer)acouple._primero).getModelo();
                    int idModelo = objModelo.getID();
                    if (this._resucitar && objModelo.getTipo() == 90) {
                        this._idMascota = (Integer)acouple._primero;
                    }
                    String[] arrstring = pedir;
                    int n = pedir.length;
                    for (int i = 0; i < n; ++i) {
                        int cantidades;
                        String apedir = arrstring[i];
                        if (idModelo != Integer.parseInt(apedir.split(",")[0]) || (cantidades = (Integer)acouple._segundo / Integer.parseInt(apedir.split(",")[1])) < 1) continue;
                        if (cantSolicitadas == 0 || cantidades < cantSolicitadas) {
                            cantSolicitadas = cantidades;
                        }
                        ++j;
                        continue block0;
                    }
                }
                if (cantSolicitadas > 0 && pedir.length == j) {
                    if (this._resucitar) {
                        if (this._idMascota != -1) {
                            Objeto mascota = Mundo.getObjeto(this._idMascota);
                            SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "+", String.valueOf(this._idMascota) + "|1|" + Constantes.resucitarMascota(mascota.getModelo().getID()) + "|" + mascota.convertirStatsAString() + ",320#0#0#1");
                        }
                    } else {
                        int idObjModDar = Integer.parseInt(this._objetoDar.split(",")[0]);
                        int cant = Integer.parseInt(this._objetoDar.split(",")[1]);
                        int cantFinal = cant * cantSolicitadas;
                        SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "+", "1|" + cantFinal + "|" + idObjModDar + "|" + Mundo.getObjModelo(idObjModDar).getStringStatsObj());
                        this._objetoConseguir = idObjModDar;
                        this._cantObjConseguir = cantFinal;
                    }
                }
                return;
            }
            SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out, 'O', "+", str);
            this._objetos.add(new Duo<Integer, Integer>(idObjeto, cantObj));
            this._objetoConseguir = -1;
            this._idMascota = -1;
            String[] pedir = this._objetoPedir.split("\\|");
            int cantSolicitadas = 0;
            int j = 0;
            block2: for (Duo<Integer, Integer> acouple : this._objetos) {
                Objeto.ObjetoModelo objModelo = Mundo.getObjeto((Integer)acouple._primero).getModelo();
                int idModelo = objModelo.getID();
                if (this._resucitar && objModelo.getTipo() == 90) {
                    this._idMascota = (Integer)acouple._primero;
                }
                String[] arrstring = pedir;
                int n = pedir.length;
                for (int i = 0; i < n; ++i) {
                    int cantidades;
                    String apedir = arrstring[i];
                    if (idModelo != Integer.parseInt(apedir.split(",")[0]) || (cantidades = (Integer)acouple._segundo / Integer.parseInt(apedir.split(",")[1])) < 1) continue;
                    if (cantSolicitadas == 0 || cantidades < cantSolicitadas) {
                        cantSolicitadas = cantidades;
                    }
                    ++j;
                    continue block2;
                }
            }
            if (cantSolicitadas > 0 && pedir.length == j) {
                if (this._resucitar) {
                    if (this._idMascota != -1) {
                        Objeto mascota = Mundo.getObjeto(this._idMascota);
                        SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "+", String.valueOf(this._idMascota) + "|1|" + Constantes.resucitarMascota(mascota.getModelo().getID()) + "|" + mascota.convertirStatsAString() + ",320#0#0#1");
                    }
                } else {
                    int idObjModDar = Integer.parseInt(this._objetoDar.split(",")[0]);
                    int cant = Integer.parseInt(this._objetoDar.split(",")[1]);
                    int cantFinal = cant * cantSolicitadas;
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "+", "1|" + cantFinal + "|" + idObjModDar + "|" + Mundo.getObjModelo(idObjModDar).getStringStatsObj());
                    this._objetoConseguir = idObjModDar;
                    this._cantObjConseguir = cantFinal;
                }
            }
        }

        public synchronized void quitarObjeto(int idObjeto, int cantObjeto) {
            this._ok = false;
            SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(this._out, this._ok, this._perso.getID());
            Duo<Integer, Integer> duo = this.getDuoPorIDObjeto(this._objetos, idObjeto);
            int nuevaCant = (Integer)duo._segundo - cantObjeto;
            if (nuevaCant < 1) {
                this._objetos.remove(duo);
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out, 'O', "-", String.valueOf(idObjeto));
            } else {
                duo._segundo = nuevaCant;
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._out, 'O', "+", String.valueOf(idObjeto) + "|" + nuevaCant);
            }
            if (this._resucitar) {
                if (this._idMascota != -1) {
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "-", String.valueOf(this._idMascota));
                }
            } else if (this._objetoConseguir != -1) {
                SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "-", "1");
            }
            this._objetoConseguir = -1;
            this._idMascota = -1;
            String[] pedir = this._objetoPedir.split("\\|");
            int cantSolicitadas = 0;
            int j = 0;
            block0: for (Duo<Integer, Integer> acouple : this._objetos) {
                Objeto.ObjetoModelo objModelo = Mundo.getObjeto((Integer)acouple._primero).getModelo();
                int idModelo = objModelo.getID();
                if (this._resucitar && objModelo.getTipo() == 90) {
                    this._idMascota = (Integer)acouple._primero;
                }
                String[] arrstring = pedir;
                int n = pedir.length;
                for (int i = 0; i < n; ++i) {
                    int cantidades;
                    String apedir = arrstring[i];
                    if (idModelo != Integer.parseInt(apedir.split(",")[0]) || (cantidades = (Integer)acouple._segundo / Integer.parseInt(apedir.split(",")[1])) < 1) continue;
                    if (cantSolicitadas == 0 || cantidades < cantSolicitadas) {
                        cantSolicitadas = cantidades;
                    }
                    ++j;
                    continue block0;
                }
            }
            if (cantSolicitadas > 0 && pedir.length == j) {
                if (this._resucitar) {
                    if (this._idMascota != -1) {
                        Objeto mascota = Mundo.getObjeto(this._idMascota);
                        SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "+", String.valueOf(this._idMascota) + "|1|" + Constantes.resucitarMascota(mascota.getModelo().getID()) + "|" + mascota.convertirStatsAString() + ",320#0#0#1");
                    }
                } else {
                    int idObjModDar = Integer.parseInt(this._objetoDar.split(",")[0]);
                    int cant = Integer.parseInt(this._objetoDar.split(",")[1]);
                    int cantFinal = cant * cantSolicitadas;
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._out, 'O', "+", "1|" + cantFinal + "|" + idObjModDar + "|" + Mundo.getObjModelo(idObjModDar).getStringStatsObj());
                    this._objetoConseguir = idObjModDar;
                    this._cantObjConseguir = cantFinal;
                }
            }
        }

        private synchronized Duo<Integer, Integer> getDuoPorIDObjeto(ArrayList<Duo<Integer, Integer>> objetos, int id) {
            for (Duo<Integer, Integer> duo : objetos) {
                if ((Integer)duo._primero != id) continue;
                return duo;
            }
            return null;
        }

        public synchronized int getCantObj(int objetoID, int personajeId) {
            ArrayList<Duo<Integer, Integer>> objetos = null;
            if (this._perso.getID() == personajeId) {
                objetos = this._objetos;
            }
            for (Duo duo : objetos) {
                if ((Integer)duo._primero != objetoID) continue;
                return (Integer)duo._segundo;
            }
            return 0;
        }
    }
}

