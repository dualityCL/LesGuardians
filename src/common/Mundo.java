package estaticos;

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
import variables.Animacion;
import variables.Cuenta;
import variables.Dragopavo;
import variables.MisionEtapaModelo;
import variables.MisionModelo;
import variables.casa.Casa;
import variables.casa.Cofre;
import variables.combate.Prisma;
import variables.combate.Reto;
import variables.gremio.Gremio;
import variables.gremio.Recaudador;
import variables.hechizo.Hechizo;
import variables.mapa.Mapa;
import variables.mapa.Cercado;
import variables.mapa.ObjInteracModelo;
import variables.mascota.Mascota;
import variables.mascota.MascotaModelo;
import variables.mercadillo.Mercadillo;
import variables.mercadillo.Mercadillo.ObjetoMercadillo;
import variables.MisionObjetivoModelo;
import variables.mob.MobModelo;
import variables.npc.NPCModelo;
import variables.npc.PreguntaNPC;
import variables.npc.RespuestaNPC;
import variables.objeto.Objeto;
import variables.objeto.ObjetoModelo;
import variables.objeto.Objevivo;
import variables.objeto.PiedraDeAlma;
import variables.oficio.Oficio;
import variables.oficio.AccionTrabajo;
import variables.oficio.StatsOficio;
import variables.personaje.Personaje;
import variables.personaje.Encarnación;
import variables.personaje.GrupoKoliseo;
import variables.personaje.Stats;
import variables.RankingPVP;
import variables.Tienda;
import variables.Tutorial;

public class Mundo {
	private static Map<Integer, Cuenta> Cuentas = new TreeMap<Integer, Cuenta>();
	private static Map<String, Integer> CuentasPorNombre = new TreeMap<String, Integer>();
	private static Map<Integer, Personaje> Personajes = new TreeMap<Integer, Personaje>();
	public static Map<Integer, Integer> DonesModelo = new TreeMap<Integer, Integer>();
	private static Map<Short, Mapa> Mapas = new TreeMap<Short, Mapa>();
	private static Map<Integer, Objeto> Objetos = new TreeMap<Integer, Objeto>();
	private static Map<Integer, ExpNivel> Experiencia = new TreeMap<Integer, ExpNivel>();
	private static Map<Integer, Hechizo> Hechizos = new TreeMap<Integer, Hechizo>();
	private static Map<Integer, ObjetoModelo> ObjModelos = new TreeMap<Integer, ObjetoModelo>();
	private static Map<Integer, Objevivo> Objevivos = new TreeMap<Integer, Objevivo>();
	private static Map<Integer, MobModelo> MobModelos = new TreeMap<Integer, MobModelo>();
	private static Map<Integer, NPCModelo> NPCModelos = new TreeMap<Integer, NPCModelo>();
	private static Map<Integer, PreguntaNPC> NPCPreguntas = new TreeMap<Integer, PreguntaNPC>();
	private static Map<Integer, RespuestaNPC> NPCRespuesta = new TreeMap<Integer, RespuestaNPC>();
	private static Map<Integer, ObjInteracModelo> ObjInteractivos = new TreeMap<Integer, ObjInteracModelo>();
	private static Map<Integer, Dragopavo> Dragopavos = new TreeMap<Integer, Dragopavo>();
	private static Map<Integer, SuperArea> SuperAreas = new TreeMap<Integer, SuperArea>();
	private static Map<Short, Area> Areas = new TreeMap<Short, Area>();
	private static Map<Integer, SubArea> SubAreas = new TreeMap<Integer, SubArea>();
	private static Map<Byte, Oficio> Oficios = new TreeMap<Byte, Oficio>();
	private static Map<Integer, ArrayList<Duo<Integer, Integer>>> Recetas = new TreeMap<Integer, ArrayList<Duo<Integer, Integer>>>();
	private static Map<Integer, ObjetoSet> ObjetoSets = new TreeMap<Integer, ObjetoSet>();
	private static Map<Integer, Gremio> Gremios = new TreeMap<Integer, Gremio>();
	private static Map<Integer, Casa> Casas = new TreeMap<Integer, Casa>();
	private static Map<Integer, Mercadillo> PuestosMercadillos = new TreeMap<Integer, Mercadillo>();
	private static Map<Integer, Map<Integer, ArrayList<ObjetoMercadillo>>> ObjMercadillos = new HashMap<Integer, Map<Integer, ArrayList<ObjetoMercadillo>>>();
	private static Map<Integer, Personaje> Esposos = new TreeMap<Integer, Personaje>();
	private static Map<Integer, Animacion> Animaciones = new TreeMap<Integer, Animacion>();
	private static Map<Integer, Reto> Retos = new TreeMap<Integer, Reto>();
	private static Map<Integer, Tienda> Tiendas = new TreeMap<Integer, Tienda>();
	private static Map<Integer, Mascota> Mascotas = new TreeMap<Integer, Mascota>();
	private static Map<Integer, MascotaModelo> MascotasModelos = new TreeMap<Integer, MascotaModelo>();
	private static Map<Short, Cercado> Cercados = new TreeMap<Short, Cercado>();
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
	public static Map<Integer, MisionObjetivoModelo> ObjetivosModelo = new HashMap<Integer, MisionObjetivoModelo>();
	public static Map<Integer, MisionEtapaModelo> Etapas = new HashMap<Integer, MisionEtapaModelo>();
	public static Map<Integer, MisionModelo> MisionesModelo = new HashMap<Integer, MisionModelo>();
	public static String liderRanking = "Ninguno";
	private static int sigIDLineaMerca;
	private static int sigIDObjeto = 0;
	private static int sigIDPersonaje = 0;
	private static int sigIDRecaudador = -100;
	private static int sigIDCofre = 0;
	private static byte _estado = 1;
	private static byte _gmAcceso = 0;
	public static Personaje _salvador = null;
	public static long _tiempoUltSalvada = 0;
	
	public static int getSigIDPersonaje() {
		return ++sigIDPersonaje;
	}
	
	public static int getSigIDCofre() {
		return ++sigIDCofre;
	}
	
	public static int getSigIDRecaudador() {
		sigIDRecaudador -= 3;
		return sigIDRecaudador;
	}
	
	public synchronized static void salvarServidor() {
		if (_estado != Constantes.SERVIDOR_ONLINE || Awaken._guardando) {
			System.out.println("Se esta intentando salvar el servidor, cuando este ya esta siendo salvado (MUNDO DOFUS)");
			return;
		}
		try {
			if ((System.currentTimeMillis() - _tiempoUltSalvada) < 30000) {
				System.out.println("Se esta intentando salvar el servidor, ni siquiera ha pasado 30 segundos de la ultima salvada (MUNDO DOFUS), tiempo diferencia " + (System.currentTimeMillis() - _tiempoUltSalvada) + " milisegundos");
				return;
			}
			System.out.println("Se invoco el metodo salvar Servidor (MUNDO DOFUS)");
			_estado = Constantes.SERVIDOR_GUARDANDO;
			Awaken._guardando = true;
			SQLManager.comenzarTransacciones();
			//GestorSQL.TIMER(false);
			//Thread.sleep(2000);
			Map<Integer, Cuenta> _cuentas = new TreeMap<Integer, Cuenta>();
			_cuentas.putAll(Cuentas);
			Map<Integer, Personaje> _personajes = new TreeMap<Integer, Personaje>();
			_personajes.putAll(Personajes);
			Map<Integer, Objevivo> _objevivos = new TreeMap<Integer, Objevivo>();
			_objevivos.putAll(Objevivos);
			Map<Integer, Encarnación> _encarnaciones = new TreeMap<Integer, Encarnación>();
			_encarnaciones.putAll(Encarnaciones);
			Map<Integer, Tienda> _tiendas = new TreeMap<Integer, Tienda>();
			_tiendas.putAll(Tiendas);
			Map<Integer, Prisma> _prismas = new TreeMap<Integer, Prisma>();
			_prismas.putAll(Prismas);
			Map<Integer, Mascota> _mascotas = new TreeMap<Integer, Mascota>();
			_mascotas.putAll(Mascotas);
			Map<Integer, Gremio> _gremios = new TreeMap<Integer, Gremio>();
			_gremios.putAll(Gremios);
			Map<Integer, Recaudador> _recaudadores = new TreeMap<Integer, Recaudador>();
			_recaudadores.putAll(Recaudadores);
			Map<Integer, Dragopavo> _dragopavos = new TreeMap<Integer, Dragopavo>();
			_dragopavos.putAll(Dragopavos);
			Map<Integer, RankingPVP> _ranking = new TreeMap<Integer, RankingPVP>();
			_ranking.putAll(RankingsPVP);
			System.out.println("Salvando las cuentas");
			for (Cuenta cuenta : _cuentas.values()) {
				if (cuenta.enLinea()) {
					Thread.sleep(10);
					SQLManager.SALVAR_CUENTA(cuenta);
				}
			}
			System.out.println("Salvando los personajes");
			for (Personaje perso : _personajes.values()) {
				if (perso.enLinea()) {
					Thread.sleep(10);
					SQLManager.SALVAR_PERSONAJE(perso, true);
				}
			}
			System.out.println("Salvando los objevivos");
			for (Objevivo objevivo : _objevivos.values()) {
				Thread.sleep(10);
				SQLManager.SALVAR_OBJEVIVO(objevivo);
			}
			System.out.println("Salvando los encarnaciones");
			for (Encarnación encarnacion : _encarnaciones.values()) {
				Thread.sleep(10);
				SQLManager.REPLACE_ENCARNACION(encarnacion);
			}
			System.out.println("Salvando los objetos mercantes");
			for (Tienda tienda : _tiendas.values()) {
				Thread.sleep(10);
				SQLManager.REPLACE_MERCANTE_OBJETOS(tienda);
			}
			System.out.println("Salvando las areas");
			for (Area area : Areas.values()) {
				SQLManager.ACTUALIZAR_AREA(area);
			}
			System.out.println("Salvando las subareas");
			for (SubArea subarea : SubAreas.values()) {
				SQLManager.ACTUALIZAR_SUBAREA(subarea);
			}
			System.out.println("Salvando los prismas");
			for (Prisma prisma : _prismas.values()) {
				Thread.sleep(10);
				if ((Mapas.get(prisma.getMapaID())).getSubArea().getPrismaID() != prisma.getID()) {
					SQLManager.DELETE_PRISMA(prisma.getID());
				} else {
					SQLManager.UPDATE_PRISMA(prisma);
				}
			}
			System.out.println("Salvando las mascotas");
			for (Mascota mascota : _mascotas.values()) {
				Thread.sleep(10);
				SQLManager.REPLACE_MASCOTA(mascota);
			}
			System.out.println("Salvando los gremios");
			for (Gremio gremio : _gremios.values()) {
				Thread.sleep(10);
				SQLManager.REPLACE_GREMIO(gremio);
			}
			System.out.println("Salvando los recaudadores");
			for (Recaudador recau : _recaudadores.values()) {
				if (recau.getEstadoPelea() > 0)
					continue;
				Thread.sleep(10);
				SQLManager.REPLACE_RECAUDADOR(recau);
			}
			System.out.println("Salvando los cercados");
			for (Cercado cercado : Cercados.values()) {
				Thread.sleep(10);
				SQLManager.UPDATE_CERCADO(cercado);
				SQLManager.UPDATE_CELDAS_OBJETO(cercado.getMapa().getID(), cercado.getStringCeldasObj());
			}
			System.out.println("Salvando las monturas");
			for (Dragopavo montura : _dragopavos.values()) {
				Thread.sleep(10);
				SQLManager.REPLACE_MONTURA(montura, true);
			}
			System.out.println("Salvando las casas");
			for (Casa casa : Casas.values()) {
				if (casa.getDueñoID() > 0) {
					Thread.sleep(10);
					SQLManager.UPDATE_CASA(casa);
				}
			}
			System.out.println("Salvando los rankings");
			for (RankingPVP rank : _ranking.values()) {
				Thread.sleep(10);
				SQLManager.REPLACE_RANKING_PVP(rank);
			}
			System.out.println("Salvando los cofres");
			for (Cofre cofre : Cofres.values()) {
				if (cofre.getDueñoID() > 0) {
					Thread.sleep(10);
					SQLManager.UPDATE_KAMAS_OBJETOS_COFRE(cofre);
				}
			}
			SQLManager.UPDATE_KAMAS_OBJETOS_COFRE(Cofre.getCofrePorUbicacion((short) 0, (short) 0));
			System.out.println("Salvando los puestos mercadillos");
			ArrayList<ObjetoMercadillo> listaObjMerca = new ArrayList<ObjetoMercadillo>();
			for (Mercadillo puesto : PuestosMercadillos.values()) {
				Thread.sleep(10);
				listaObjMerca.addAll(puesto.todoListaObjMercaDeUnPuesto());
			}
			SQLManager.REPLACE_OBJ_MERCADILLOS(listaObjMerca);
			_estado = Constantes.SERVIDOR_ONLINE;
			Awaken._guardando = false;
			System.out.println("Se salvó exitosamente el servidor 100%");
			_tiempoUltSalvada = System.currentTimeMillis();
			if (_salvador != null)
				SocketManager
				.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_salvador, "Se salvó exitosamente el servidor 100% (MUNDO DOFUS)");
			_salvador = null;
		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
			_estado = Constantes.SERVIDOR_ONLINE;
			Awaken._guardando = false;
			System.out.println("Ocurrio un error de concurrent");
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_salvador, "Ocurrio un error de concurrent (MUNDO DOFUS)");
			_salvador = null;
		} catch (Exception e) {
			e.printStackTrace();
			_estado = Constantes.SERVIDOR_ONLINE;
			Awaken._guardando = false;
			System.out.println("Error al salvar : " + e.getMessage());
			_salvador = null;
		} finally {
			SQLManager.comenzarTransacciones();
			// GestorSQL.TIMER(true);
			_estado = Constantes.SERVIDOR_ONLINE;
			Awaken._guardando = false;
		}
	}
	
	public static synchronized int getSigIDObjeto() {
		sigIDObjeto += 1;
		return sigIDObjeto;
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
		} else {
			System.out.println("No hay registros de prismas de conquista.");
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
		SQLManager.CARGAR_DONES_MODELO();
		System.out.println("Se han cargado " + DonesModelo.size() + " dones.");
		SQLManager.CARGAR_OBJETOS_SETS();
		System.out.println("Se han cargado " + ObjetoSets.size() + " sets.");
		SQLManager.CARGAR_MAPAS();
		System.out.println("Se han cargado " + Mapas.size() + " mapas.");
		numero = SQLManager.CARGAR_MAPAS_FIXEADOS();
		System.out.println("Se han cargado " + numero + " mapas fix.");
		numero = SQLManager.CARGAR_TRIGGERS();
		System.out.println("Se han cargado " + numero + " triggers.");
		numero = SQLManager.CARGAR_ACCIONES_FINALES_DE_COMBATE();
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
		SQLManager.CARGAR_OBJETIVOS();
		System.out.println("Se han cargado " + ObjetivosModelo.size() + " objetivos de misiones.");
		SQLManager.CARGAR_ETAPAS();
		System.out.println("Se han cargado " + Etapas.size() + " etapas de misiones.");
		SQLManager.CARGAR_MISIONES();
		System.out.println("Se han cargado " + MisionesModelo.size() + " misiones.");	
		System.out.println("=======>Datos Dinámicos<=======");
		SQLManager.UPDATE_TODAS_CUENTAS_CERO();
		SQLManager.SELECT_OBJEVIVOS();
		if (Objevivos.size() > 0) {
		System.out.println("Se han cargado " + Objevivos.size() + " objevivos de PJs.");
		} else {
			System.out.println("No hay registros de objevivos de PJs.");
		}
		SQLManager.CARGAR_ENCARNACIONES();
		if (Encarnaciones.size() > 0) {
		System.out.println("Se han cargado " + Encarnaciones.size() + " encarnaciones de PJs.");
		} else {
			System.out.println("No hay registros de encarnaciones de PJs.");
		}
		SQLManager.SELECT_OBJETOS_BDTANIA();
		System.out.println("Se han cargado " + Objetos.size() + " objetos de PJs.");
		SQLManager.CARGAR_MONTURAS();
		if (Dragopavos.size() > 0) {
		System.out.println("Se han cargado " + Dragopavos.size() + " dragopavos de PJs.");
		} else {
			System.out.println("No hay registros de dragopavos de PJs.");
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
		numero = SQLManager.SELECT_MERCANTES();
		if (numero > 0) {
		System.out.println("Se han cargado " + numero + " PJs mercantes.");
		} else {
			System.out.println("No hay registros de PJs en modo mercante.");
		}
		SQLManager.CARGAR_GREMIOS();
		if (Gremios.size() > 0) {
		System.out.println("Se han cargado " + Gremios.size() + " gremios.");
		} else {
			System.out.println("No hay registros de gremios.");
		}
		numero = SQLManager.CARGAR_MIEMBROS_GREMIO();
		if (numero > 0) {
		System.out.println("Se han cargado " + numero + " miembros de gremio.");
		} else {
			System.out.println("No hay registros de miembros de gremio.");
		}
		numero = SQLManager.CARGAR_CERCADOS();
		System.out.println("Se han cargado " + numero + " cercados.");
		numero = SQLManager.CARGAR_RECAUDADORES();
		if (numero > 0) {
		System.out.println("Se han cargado " + numero + " recaudadores.");
		} else {
			System.out.println("No hay registros de recaudadores.");		
		}
		numero = SQLManager.CARGAR_CASAS();
		System.out.println("Se han cargado " + numero + " casas.");
		SQLManager.CARGAR_COFRE();
		System.out.println("Se han cargado " + Cofres.size() + " cofres.");
		numero = SQLManager.SELECT_BANIP();
		if (numero > 0) {
		System.out.println("Se han cargado " + numero + " IPs baneadas.");
		} else {
			System.out.println("No hay registros de IPS baneadas.");
		}
		SQLManager.CARGAR_OBJETOS_MERCANTES();
		if (Tiendas.size() > 0) {
		System.out.println("Se han cargado " + Tiendas.size() + " objetos de PJs mercantes.");
		} else {
			System.out.println("No hay registros de objetos de PJs en modo mercante que esten en venta.");
		}
		SQLManager.CARGAR_MASCOTAS();
		if (Mascotas.size() > 0) {
		System.out.println("Se han cargado " + Mascotas.size() + " mascotas de PJs.");
		} else {
			System.out.println("No hay registros de mascotas pertenecientes a PJs.");
		}
		SQLManager.SELECT_PUESTOS_MERCADILLOS();
		System.out.println("Se han cargado " + PuestosMercadillos.size() + " puestos de mercadillo.");
		SQLManager.SELECT_OBJETOS_MERCADILLOS();
		if (ObjMercadillos.size() > 0) {
		System.out.println("Se han cargado " + ObjMercadillos.size() + " objetos de mercadillo en venta.");
		} else {
			System.out.println("No hay registros de objetos de mercadillo que se encuentren en venta.");
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
	
	public static void addRespuestaNPC(RespuestaNPC respuesta) {
		NPCRespuesta.put(respuesta.getID(), respuesta);
	}
	
	public static RespuestaNPC getRespuestaNPC(int id) {
		return NPCRespuesta.get(id);
	}
	
	public static void addExpLevel(int nivel, ExpNivel exp) {
		Experiencia.put(nivel, exp);
	}
	
	public static Cuenta getCuenta(int id) {
		return Cuentas.get(id);
	}
	
	public static void addPreguntaNPC(PreguntaNPC pregunta) {
		NPCPreguntas.put(pregunta.getID(), pregunta);
	}
	
	public static PreguntaNPC getPreguntaNPC(int id) {
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
		if (!Mapas.containsKey(mapa.getID()))
			Mapas.put(mapa.getID(), mapa);
	}
	
	public static Mapa mapaPorCoordXYContinente(int mapaX, int mapaY, int idContinente) {
		for (Mapa mapa : Mapas.values()) {
			if ((mapa.getX() == mapaX) && (mapa.getY() == mapaY)
			&& (mapa.getSubArea().getArea().getSuperArea().getID() == idContinente))
				return mapa;
		}
		return null;
	}
	
	public static String mapaPorCoordenadas(int mapaX, int mapaY) {
		String str = "";
		for (Mapa mapa : Mapas.values()) {
			if ((mapa.getX() == mapaX) && (mapa.getY() == mapaY))
				str += mapa.getID() + ", ";
		}
		return str;
	}
	
	public static void subirEstrellasMobs() {
		for (Mapa mapa : Mapas.values())
			mapa.subirEstrellasMobs();
	}
	
	public static void subirEstrellasMobs(int cant) {
		for (Mapa mapa : Mapas.values())
			mapa.subirEstrellasCantidad(cant);
	}
	
	public static Cuenta getCuentaPorNombre(String nombre) {
		return CuentasPorNombre.get(nombre.toLowerCase()) != null
		? Cuentas.get(CuentasPorNombre.get(nombre.toLowerCase()))
		: null;
	}
	
	public static Personaje getPersonaje(int id) {
		return Personajes.get(id);
	}
	
	public static synchronized void addCuenta(Cuenta cuenta) {
		Cuentas.put(cuenta.getID(), cuenta);
		CuentasPorNombre.put(cuenta.getNombre().toLowerCase(), cuenta.getID());
	}
	
	public static synchronized void addPersonaje(Personaje perso) {
		if (perso.getID() > sigIDPersonaje)
			sigIDPersonaje = perso.getID();
		Personajes.put(perso.getID(), perso);
	}
	
	public static int getCantidadPersonajes() {
		return Personajes.size();
	}
	
	public static Personaje getPjPorNombre(String nombre) {
		ArrayList<Personaje> Ps = new ArrayList<Personaje>();
		Ps.addAll(Personajes.values());
		for (Personaje perso : Ps)
			if (perso.getNombre().equalsIgnoreCase(nombre))
				return perso;
		return null;
	}
	
	public static Casa getCasaPorMapa(short idMapa) {
		for (Casa casa : Casas.values()) {
			if (casa.getMapasContenidos().contains(idMapa)) {
				return casa;
			}
		}
		return null;
	}
	
	public static synchronized void eliminarPj(Personaje perso, boolean totalmente) {
		perso.getObjetos().clear();
		desconectarPerso(perso);
		if (perso.esMercante()) {
			perso.getMapa().removerMercante(perso.getID());
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(perso.getMapa(), perso.getID());
		}
		for (Casa casa : Casas.values()) {
			if (casa.getDueñoID() == perso.getID()) {
				casa.resetear();
				SQLManager.UPDATE_CASA(casa);
			}
		}
		for (Cercado cercado : Cercados.values()) {
			if (cercado.getDueño() == perso.getID()) {
				String[] criando = cercado.getCriando().split(";");
				String x = "";
				for (String pavo : criando) {
					int id = Integer.parseInt(pavo);
					if (x.length() > 0)
						x += ",";
					x += id;
					Mundo.borrarDragopavo(id);
				}
				if (x.length() > 0)
					SQLManager.DELETE_DRAGOPAVO_LISTA(x);
				cercado.resetear();
				SQLManager.UPDATE_CERCADO(cercado);
			}
		}
		if (perso.getMiembroGremio() != null)
			SQLManager.DELETE_MIEMBRO_GREMIO(perso.getID());
		delRankingPVP(perso.getID());
		if (totalmente) {
			Personaje esposo = Mundo.getPersonaje(perso.getEsposo());
			if (esposo != null)
				esposo.divorciar();
			perso.divorciar();
			SQLManager.DELETE_PERSONAJE(perso);
			Personajes.remove(perso.getID());
		}
	}
	
	public static String getAlineacionTodasSubareas() {
		String str = "";
		boolean primero = false;
		for (SubArea subarea : SubAreas.values()) {
			if (!subarea.getConquistable())
				continue;
			if (primero)
				str += "|";
			str += subarea.getID() + ";" + subarea.getAlineacion();
			primero = true;
		}
		return str;
	}
	
	public static long getExpMinPersonaje(int nivel) {
		if (nivel > Awaken.MAX_NIVEL)
			nivel = Awaken.MAX_NIVEL;
		if (nivel < 1)
			nivel = 1;
		return (Experiencia.get(nivel))._personaje;
	}
	
	public static long getExpMaxPersonaje(int nivel) {
		if (nivel >= Awaken.MAX_NIVEL)
			nivel = Awaken.MAX_NIVEL - 1;
		if (nivel <= 1)
			nivel = 1;
		return (Experiencia.get(nivel + 1))._personaje;
	}
	
	public static long getExpMaxEncarnacion(int nivel) {
		if (nivel >= 50)
			nivel = 49;
		if (nivel <= 1)
			nivel = 1;
		return (Experiencia.get(nivel + 1))._encarnacion;
	}
	
	public static ExpNivel getExpNivel(int nivel) {
		return Experiencia.get(nivel);
	}
	
	public static ObjInteracModelo getObjInteractivoModelo(int id) {
		return ObjInteractivos.get(id);
	}
	
	public static void addObjInteractivo(ObjInteracModelo OIM) {
		ObjInteractivos.put(OIM.getID(), OIM);
	}
	
	public static Oficio getOficio(byte id) {
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
		if (listaIDRecetas == null)
			return -1;
		for (int id : listaIDRecetas) {
			ArrayList<Duo<Integer, Integer>> receta = Recetas.get(id);
			if (receta == null || receta.size() != ingredientes.size())
				continue;
			boolean ok = true;
			for (Duo<Integer, Integer> ing : receta) {
				if (ingredientes.get(ing._primero) == null) {
					ok = false;
					break;
				}
				int primera = ingredientes.get(ing._primero);
				int segunda = ing._segundo;
				if (primera != segunda) {
					ok = false;
					break;
				}
			}
			if (ok)
				return id;
		}
		return -1;
	}
	
	public static Cuenta getCuentaPorApodo(String apodo) {
		for (Cuenta C : Cuentas.values())
			if (C.getApodo().equals(apodo))
				return C;
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
	
	public synchronized static int getSigIDMontura() {
		int max = -101;
		for (int a : Dragopavos.keySet())
			if (a < max)
				max = a;
		return max - 3;
	}
	
	public synchronized static int getSigIDPrisma() {
		int max = -102;
		for (int a : Prismas.keySet())
			if (a < max)
				max = a;
		return max - 3;
	}
	
	public static synchronized int getSigIDObjevivo() {
		int max = 0;
		for (int a : Objevivos.keySet())
			if (a > max)
				max = a;
		return max + 1;
	}
	
	public synchronized static int getSigIdGremio() {
		if (Gremios.isEmpty())
			return 1;
		int n = 0;
		for (int x : Gremios.keySet())
			if (n < x)
				n = x;
		return n + 1;
	}
	
	public static synchronized int sigIDLineaMercadillo() {
		sigIDLineaMerca += 1;
		return sigIDLineaMerca;
	}
	
	public static synchronized void addGremio(Gremio gremio) {
		Gremios.put(gremio.getID(), gremio);
	}
	
	public static synchronized boolean nombreGremioUsado(String nombre) {
		for (Gremio gremio : Gremios.values())
			if (gremio.getNombre().equalsIgnoreCase(nombre))
				return true;
		return false;
	}
	
	public static synchronized boolean emblemaGremioUsado(String emblema) {
		for (Gremio gremio : Gremios.values()) {
			if (gremio.getEmblema().equals(emblema))
				return true;
		}
		return false;
	}
	
	public static Gremio getGremio(int i) {
		return Gremios.get(i);
	}
	
	public static long getXPMaxGremio(int nivel) {
		if (nivel >= 200)
			nivel = 199;
		if (nivel <= 1)
			nivel = 1;
		return (Experiencia.get(nivel + 1))._gremio;
	}
	
	public static short getCeldaZaapPorMapaID(short i) {
		for (Entry<Short, Short> zaap : Constantes.ZAAPS.entrySet()) {
			if (zaap.getKey() == i)
				return zaap.getValue();
		}
		return -1;
	}
	
	public static short getCeldaCercadoPorMapaID(short i) {
		Cercado cercado = getMapa(i).getCercado();
		if ((cercado != null) && (cercado.getCeldaID() > 0)) {
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
			if (c.getActualIP().compareTo(ip) == 0)
				return true;
		}
		return false;
	}
	
	public static int cuentasIP(String ip) {
		int veces = 0;
		for (Cuenta c : Cuentas.values()) {
			if (c.getActualIP().compareTo(ip) == 0)
				veces++;
		}
		return veces;
	}
	
	public static void desconectarPerso(Personaje perso) {
		perso.setEnLinea(false);
		if (perso.getCasa() == null)
			perso.stopRecuperarVida();
	}
	
	/*public static void a() {
		System.out.println("Recuperando el server");
		for (Cuenta cuenta : Cuentas.values()) {
			SQLManager.ELIMINAR_CUENTA(cuenta.getID());
		}
		for (Personaje perso : Personajes.values()) {
			SQLManager.DELETE_PERSONAJE(perso);
		}
		for (Prisma prisma : Prismas.values()) {
			SQLManager.DELETE_PRISMA(prisma.getID());
		}
		for (Mascota mascota : Mascotas.values()) {
			SQLManager.BORRAR_MASCOTA(mascota.getID());
		}
		for (Gremio gremio : Gremios.values()) {
			SQLManager.DELETE_GREMIO(gremio.getID());
		}
		for (Recaudador recau : Recaudadores.values())
			SQLManager.DELETE_RECAUDADOR(recau.getID());
	}*/
	
	public static void addHechizo(Hechizo hechizo) {
		Hechizos.put(hechizo.getID(), hechizo);
	}
	
	public static void addObjModelo(ObjetoModelo obj) {
		ObjModelos.put(obj.getID(), obj);
	}
	
	public static Hechizo getHechizo(int id) {
		return Hechizos.get(id);
	}
	
	public static ObjetoModelo getObjModelo(int id) {
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
			if (!perso.enLinea() || (perso.getCuenta().getEntradaPersonaje() == null))
				continue;
			enLinea.add(perso);
		}
		return enLinea;
	}
	
	public static synchronized Objeto objetoIniciarServer(int id, int modelo, int cant, int pos, String strStats,
	int idObvi) {
		ObjetoModelo objModelo = getObjModelo(modelo);
		if (objModelo == null) {
			System.out.println("La id del objeto bug " + id);
			SQLManager.DELETE_OBJETO(id);
			return null;
		}
		if (objModelo.getTipo() == 85)
			return new PiedraDeAlma(id, cant, modelo, pos, strStats);
		else
			return new Objeto(id, modelo, cant, pos, strStats, idObvi);
	}
	
	public static synchronized void addObjeto(Objeto obj, boolean salvarSQL) {
		if (obj == null)
			return;
		if (obj.getID() == 0)
			obj.setID(getSigIDObjeto());
		Objetos.put(obj.getID(), obj);
		if (salvarSQL && obj.getObjModelo().getTipo() == 113)
			SQLManager.SALVAR_OBJETO(obj);
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
		return (Dragopavo) Dragopavos.get(id);
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
	
	public synchronized static void addObjMercadillo(int cuentaID, int idPuestoMerca, ObjetoMercadillo objMercadillo) {
		if (ObjMercadillos.get(cuentaID) == null)
			ObjMercadillos.put(cuentaID, new HashMap<Integer, ArrayList<ObjetoMercadillo>>());
		if (ObjMercadillos.get(cuentaID).get(idPuestoMerca) == null)
			ObjMercadillos.get(cuentaID).put(idPuestoMerca, new ArrayList<ObjetoMercadillo>());
		ObjMercadillos.get(cuentaID).get(idPuestoMerca).add(objMercadillo);
	}
	
	public synchronized static void borrarObjMercadillo(int cuentaID, int idPuestoMerca, ObjetoMercadillo objMerca) {
		ObjMercadillos.get(cuentaID).get(idPuestoMerca).remove(objMerca);
	}
	
	public static int cantPuestosMercadillos() {
		return PuestosMercadillos.size();
	}
	
	public static int cantObjMercadillos() {
		int cantidad = 0;
		for (Map<Integer, ArrayList<ObjetoMercadillo>> tempCuenta : ObjMercadillos.values()) {
			for (ArrayList<ObjetoMercadillo> objMercadillo : tempCuenta.values()) {
				cantidad += objMercadillo.size();
			}
		}
		return cantidad;
	}
	
	public static synchronized void addPuestoMercadillo(Mercadillo mercadillo) {
		PuestosMercadillos.put(mercadillo.getIDMercadillo(), mercadillo);
	}
	
	public static Map<Integer, ArrayList<ObjetoMercadillo>> getMisObjetos(int cuentaID) {
		if (ObjMercadillos.get(cuentaID) == null)
			ObjMercadillos.put(cuentaID, new HashMap<Integer, ArrayList<ObjetoMercadillo>>());
		return ObjMercadillos.get(cuentaID);
	}
	
	public static Collection<ObjetoModelo> getObjModelos() {
		return ObjModelos.values();
	}
	
	public static Personaje getCasado(int id) {
		return Esposos.get(id);
	}
	
	public static synchronized void addEsposo(int sexo, Personaje perso) {
		Personaje esposo = Esposos.get(sexo);
		if (esposo != null) {
			Esposos.remove(sexo);
		}
		Esposos.put(sexo, perso);
	}
	
	public static synchronized void discursoSacerdote(Personaje perso, Mapa mapa, int idSacerdote) {
		Personaje esposo = Esposos.get(0);
		Personaje esposa = Esposos.get(1);
		if (esposo.getEsposo() != 0) {
			SocketManager.ENVIAR_cs_CHAT_MENSAJE_A_MAPA(mapa, esposo.getNombre() + " no acepta el matrimonio.",
			Awaken.COLOR_MENSAJE);
			return;
		}
		if (esposa.getEsposo() != 0) {
			SocketManager.ENVIAR_cs_CHAT_MENSAJE_A_MAPA(mapa, esposa.getNombre() + " no acepta el matrimonio.",
			Awaken.COLOR_MENSAJE);
			return;
		}
		SocketManager.ENVIAR_cMK_CHAT_MENSAJE_MAPA(perso.getMapa(), "", -1, "Sacerdote", perso.getNombre()
		+ " aceptas como esposo(a) a " + getCasado(perso.getSexo() == 1 ? 0 : 1).getNombre() + " ?");
		SocketManager.ENVIAR_ACCION_JUEGO_CASARSE(mapa, 617, esposo == perso ? esposo.getID() : esposa.getID(),
		esposo == perso ? esposa.getID() : esposo.getID(), idSacerdote);
	}
	
	public static void casando(Personaje hombre, Personaje mujer, boolean acepta) {
		if (acepta) {
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_MAPA(hombre.getMapa(), "", -1, "Sacedote", "Los declaro marido "
			+ hombre.getNombre() + " y mujer " + mujer.getNombre()
			+ " unidos por la línea del sagrado matrimonio, hasta que el otro o la otra los separe. xD");
			hombre.esposoDe(mujer);
			mujer.esposoDe(hombre);
		} else {
			SocketManager
			.ENVIAR_Im_INFORMACION_A_MAPA(hombre.getMapa(), "048;" + hombre.getNombre() + "~" + mujer.getNombre());
		}
		hombre.setAceptarCasarse(false);
		mujer.setAceptarCasarse(false);
		Esposos.clear();
	}
	//START QUEST SYSTEM
	public static void addMision(MisionModelo mision) {  
		MisionesModelo.put(mision.getID(), mision);
	}
	
	public static MisionModelo getMision(int id) {
		return MisionesModelo.get(id);
	}
	
	public static void addMisionObjetivoModelo(int id, byte tipo, String args) {
		ObjetivosModelo.put(id, new MisionObjetivoModelo(id, tipo, args));
	}
	
	public static MisionObjetivoModelo getMisionObjetivoModelo(int id) {
		return ObjetivosModelo.get(id);
	}
	
	public static void addEtapa(int id, String recompensas, String steps, String nombre) {
		Etapas.put(id, new MisionEtapaModelo(id, recompensas, steps, nombre));
	}
	
	public static MisionEtapaModelo getEtapa(int id) {
		return Etapas.get(id);
	}
	//END QUEST SYSTEM
	public static Collection<Objevivo> getTodosObjevivos() {
		return Objevivos.values();
	}
	
	public static Objevivo getObjevivos(int idObjevivo) {
		return Objevivos.get(idObjevivo);
	}
	
	public static synchronized void addObjevivo(Objevivo objevivo) {
		Objevivos.put(objevivo.getID(), objevivo);
	}
	
	public static Animacion getAnimacion(int animacionId) {
		return Animaciones.get(animacionId);
	}
	
	public static void addAnimation(Animacion animacion) {
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
		Tiendas.put(tiendas.getIDObjeto(), tiendas);
		if (salvarSQL)
			SQLManager.REPLACE_MERCANTE_OBJETOS(tiendas);
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
	
	public static void agregarMascotaModelo(int id, MascotaModelo mascota) {
		MascotasModelos.put(id, mascota);
	}
	
	public static MascotaModelo getMascotaModelo(int id) {
		return MascotasModelos.get(id);
	}
	
	public static Set<Integer> getIDTodasMascotas() {
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
	
	public synchronized static void addCercado(Cercado cercado) {
		Cercados.put(cercado.getMapa().getID(), cercado);
		cercado.startMoverDrago();
	}
	
	public static Cercado getCercadoPorMapa(short mapa) {
		return Cercados.get(mapa);
	}
	
	public static Collection<Cercado> todosCercados() {
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
		return Prismas.values();
	}
	
	public static void addCofre(Cofre cofre) {
		Cofres.put(cofre.getID(), cofre);
		if (sigIDCofre < cofre.getID())
			sigIDCofre = cofre.getID();
	}
	
	public static Cofre getCofre(int id) {
		return Cofres.get(id);
	}
	
	public static Map<Integer, Cofre> getCofres() {
		return Cofres;
	}
	
	public static void addRecaudador(Recaudador recauda) {
		if (recauda.getID() < sigIDRecaudador)
			sigIDRecaudador = recauda.getID();
		Recaudadores.put(recauda.getID(), recauda);
	}
	
	public static Recaudador getRecaudador(int id) {
		return Recaudadores.get(id);
	}
	
	public static void borrarRecaudador(int id) {
		Recaudadores.remove(id);
		SQLManager.DELETE_RECAUDADOR(id);
	}
	
	public static Collection<Recaudador> getTodosRecaudadores() {
		return Recaudadores.values();
	}
	
	public static Recaudador getRecauPorMapaID(short id) {
		for (Entry<Integer, Recaudador> recau : Recaudadores.entrySet()) {
			if (recau.getValue().getMapa().getID() == id) {
				return Recaudadores.get((recau.getValue().getID()));
			}
		}
		return null;
	}
	
	public static int cantRecauDelGremio(int gremiodID) {
		int i = 0;
		for (Entry<Integer, Recaudador> recau : Recaudadores.entrySet()) {
			if ((recau.getValue()).getGremioID() == gremiodID) {
				i++;
			}
		}
		return i;
	}
	
	public static void addDonModelo(int id, int stat) {
		DonesModelo.put(id, stat);
	}
	
	public static int getDonStat(int id) {
		return DonesModelo.get(id);
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
			} else {
				if ((rank.getVictorias() != vict) || (rank.getDerrotas() > derr))
					continue;
				vict = rank.getVictorias();
				id = rank.getNombre();
				derr = rank.getDerrotas();
			}
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
			} else {
				if ((rank.getVictorias() != vict) || (rank.getDerrotas() > derr))
					continue;
				vict = rank.getVictorias();
				id = rank.getID();
				derr = rank.getDerrotas();
			}
		}
		return id;
	}
	
	public static float getBalanceMundo(int alineacion) {
		int cant = 0;
		for (SubArea subarea : SubAreas.values()) {
			if (subarea.getAlineacion() == alineacion)
				cant++;
		}
		if (cant == 0)
			return 0.0F;
		return (float) Math.rint(10 * cant / 4 / 10);
	}
	
	public static float getBalanceArea(Area area, int alineacion) {
		int cant = 0;
		for (SubArea subarea : SubAreas.values()) {
			if ((subarea.getArea() == area) && (subarea.getAlineacion() == alineacion))
				cant++;
		}
		if (cant == 0)
			return 0.0F;
		return (float) Math.rint(1000 * cant / area.getSubAreas().size() / 10);
	}
	
	public static String prismasGeoposicion(int alineacion) {
		String str = "";
		boolean primero = false;
		int subareas = 0;
		for (SubArea subarea : SubAreas.values()) {
			if (!subarea.getConquistable())
				continue;
			if (primero)
				str += ";";
			str += subarea.getID() + "," + (subarea.getAlineacion() == 0 ? -1 : subarea.getAlineacion()) + ",0,";
			if (getPrisma(subarea.getPrismaID()) == null)
				str += "0,1";
			else
				str += (subarea.getPrismaID() == 0 ? 0 : getPrisma(subarea.getPrismaID()).getMapaID()) + ",1";
			primero = true;
			subareas++;
		}
		if (alineacion == 1)
			str += "|" + Area._bontas;
		else if (alineacion == 2)
			str += "|" + Area._brakmars;
		str += "|" + Areas.size() + "|";
		primero = false;
		for (Area area : Areas.values()) {
			if (area.getAlineacion() == 0)
				continue;
			if (primero)
				str += ";";
			str += area.getID() + "," + area.getAlineacion() + ",1," + (area.getPrismaID() == 0 ? 0 : 1);
			primero = true;
		}
		if (alineacion == 1)
			str = Area._bontas + "|" + subareas + "|" + (subareas - (SubArea._bontas + SubArea._brakmars)) + "|" + str;
		else if (alineacion == 2)
			str = Area._brakmars + "|" + subareas + "|" + (subareas - (SubArea._bontas + SubArea._brakmars)) + "|" + str;
		return str;
	}
	
	public static void addKoliseo1(Personaje perso) {
		if (Koliseo1.isEmpty())
			Koliseo1.add(perso);
		else
			for (Personaje persos : Koliseo1) {
				if ((persos == null) || (persos.getNivel() < perso.getNivel()))
					continue;
				int index = Koliseo1.indexOf(persos);
				Koliseo1.add(index - 1, perso);
			}
	}
	
	public static void addKoliseo2(Personaje perso) {
		if (Koliseo2.isEmpty())
			Koliseo2.add(perso);
		else
			for (Personaje persos : Koliseo2) {
				if ((persos == null) || (persos.getNivel() < perso.getNivel()))
					continue;
				int index = Koliseo2.indexOf(persos);
				Koliseo2.add(index - 1, perso);
			}
	}
	
	public static void addKoliseo3(Personaje perso) {
		if (Koliseo3.isEmpty())
			Koliseo3.add(perso);
		else
			for (Personaje persos : Koliseo3) {
				if ((persos == null) || (persos.getNivel() < perso.getNivel()))
					continue;
				int index = Koliseo3.indexOf(persos);
				Koliseo3.add(index - 1, perso);
			}
	}
	
	public static void crearGruposKoliseo1() {
		CopyOnWriteArrayList<Personaje> kolis1 = new CopyOnWriteArrayList<Personaje>();
		for (Personaje persos : Koliseo1) {
			if ((persos == null) || (!persos.enLinea()))
				continue;
			kolis1.add(persos);
		}
		if (kolis1.size() < 6)
			return;
		int size = kolis1.size();
		for (int i = 0; i < size; i += 3) {
			Personaje koli1 = null;
			Personaje koli2 = null;
			Personaje koli3 = null;
			Random rand = new Random();
			int random = rand.nextInt(kolis1.size() - 1);
			koli1 = kolis1.get(random);
			kolis1.remove(random);
			random = rand.nextInt(kolis1.size() - 1);
			koli2 = kolis1.get(random);
			kolis1.remove(random);
			random = rand.nextInt(kolis1.size() - 1);
			koli3 = kolis1.get(random);
			kolis1.remove(random);
			if ((koli1 != null) && (koli2 != null) && (koli3 != null)) {
				GrupoKoliseo grupo = new GrupoKoliseo(koli1, koli2, koli3, 1);
				GrupoKoliseo1.add(grupo);
			}
		}
	}
	
	public static void crearGruposKoliseo2() {
		CopyOnWriteArrayList<Personaje> kolis1 = new CopyOnWriteArrayList<Personaje>();
		for (Personaje persos : Koliseo2) {
			if ((persos == null) || (!persos.enLinea()))
				continue;
			kolis1.add(persos);
		}
		if (kolis1.size() < 6)
			return;
		int size = kolis1.size();
		for (int i = 0; i < size; i += 3) {
			Personaje koli1 = null;
			Personaje koli2 = null;
			Personaje koli3 = null;
			Random rand = new Random();
			int random = rand.nextInt(kolis1.size() - 1);
			koli1 = kolis1.get(random);
			kolis1.remove(random);
			random = rand.nextInt(kolis1.size() - 1);
			koli2 = kolis1.get(random);
			kolis1.remove(random);
			random = rand.nextInt(kolis1.size() - 1);
			koli3 = kolis1.get(random);
			kolis1.remove(random);
			if ((koli1 != null) && (koli2 != null) && (koli3 != null)) {
				GrupoKoliseo grupo = new GrupoKoliseo(koli1, koli2, koli3, 1);
				GrupoKoliseo2.add(grupo);
			}
		}
	}
	
	public static void crearGruposKoliseo3() {
		CopyOnWriteArrayList<Personaje> kolis1 = new CopyOnWriteArrayList<Personaje>();
		for (Personaje persos : Koliseo3) {
			if ((persos == null) || (!persos.enLinea()))
				continue;
			kolis1.add(persos);
		}
		if (kolis1.size() < 6)
			return;
		int size = kolis1.size();
		for (int i = 0; i < size; i += 3) {
			Personaje koli1 = null;
			Personaje koli2 = null;
			Personaje koli3 = null;
			Random rand = new Random();
			int random = rand.nextInt(kolis1.size() - 1);
			koli1 = kolis1.get(random);
			kolis1.remove(random);
			random = rand.nextInt(kolis1.size() - 1);
			koli2 = kolis1.get(random);
			kolis1.remove(random);
			random = rand.nextInt(kolis1.size() - 1);
			koli3 = kolis1.get(random);
			kolis1.remove(random);
			if ((koli1 != null) && (koli2 != null) && (koli3 != null)) {
				GrupoKoliseo grupo = new GrupoKoliseo(koli1, koli2, koli3, 1);
				GrupoKoliseo3.add(grupo);
			}
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
	
	public synchronized static Duo<Integer, Integer> getDuoPorIDObjeto(ArrayList<Duo<Integer, Integer>> objetos, int id) {
		for (Duo<Integer, Integer> duo : objetos) {
			if ((duo._primero) == id)
				return duo;
		}
		return null;
	}
	public static class Area {
		private short _id;
		private SuperArea _superArea;
		private String _nombre;
		private ArrayList<SubArea> _subAreas = new ArrayList<SubArea>();
		private int _alineacion;
		public static int _bontas = 0;
		public static int _brakmars = 0;
		private int _prisma = 0;
		
		public Area(short id, int superArea, String nombre, int alineacion, int prisma) {
			_id = id;
			_nombre = nombre;
			_superArea = Mundo.getSuperArea(superArea);
			if (_superArea == null) {
				_superArea = new SuperArea(superArea);
				addSuperArea(_superArea);
			}
			_alineacion = 0;
			_prisma = 0;
			if (getPrisma(prisma) != null) {
				_alineacion = alineacion;
				_prisma = prisma;
			}
			if (_alineacion == 1)
				_bontas += 1;
			else if (_alineacion == 2)
				_brakmars += 1;
		}
		
		public static int subareasBontas() {
			return _bontas;
		}
		
		public static int subareasBrakmars() {
			return _brakmars;
		}
		
		public int getAlineacion() {
			return _alineacion;
		}
		
		public int getPrismaID() {
			return _prisma;
		}
		
		public void setPrismaID(int prisma) {
			_prisma = prisma;
		}
		
		public void setAlineacion(int alineacion) {
			if ((_alineacion == 1) && (alineacion == -1))
				_bontas -= 1;
			else if ((_alineacion == 2) && (alineacion == -1))
				_brakmars -= 1;
			else if ((_alineacion == -1) && (alineacion == 1))
				_bontas += 1;
			else if ((_alineacion == -1) && (alineacion == 2))
				_brakmars += 1;
			_alineacion = alineacion;
		}
		
		public String getNombre() {
			return _nombre;
		}
		
		public short getID() {
			return _id;
		}
		
		public SuperArea getSuperArea() {
			return _superArea;
		}
		
		public void addSubArea(SubArea sa) {
			_subAreas.add(sa);
		}
		
		public ArrayList<SubArea> getSubAreas() {
			return _subAreas;
		}
		
		public ArrayList<Mapa> getMapas() {
			ArrayList<Mapa> mapas = new ArrayList<Mapa>();
			for (SubArea SA : _subAreas)
				mapas.addAll(SA.getMapas());
			return mapas;
		}
	}

	public static class Duo<L, R> {
		public L _primero;
		public R _segundo;
		
		public Duo(L s, R i) {
			_primero = s;
			_segundo = i;
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
			_personaje = perso;
			_oficio = oficio;
			_montura = montura;
			_pvp = pvp;
			_gremio = (_personaje * 20L);
			_encarnacion = encarnacion;
		}
	}
	public static class Intercambio {
		private Personaje _perso1;
		private Personaje _perso2;
		private PrintWriter _out1;
		private PrintWriter _out2;
		private long _kamas1 = 0L;
		private long _kamas2 = 0L;
		private ArrayList<Duo<Integer, Integer>> _objetos1 = new ArrayList<Duo<Integer, Integer>>();
		private ArrayList<Duo<Integer, Integer>> _objetos2 = new ArrayList<Duo<Integer, Integer>>();
		private boolean _ok1;
		private boolean _ok2;
		
		public Intercambio(Personaje p1, Personaje p2) {
			_perso1 = p1;
			_perso2 = p2;
			_out1 = _perso1.getCuenta().getEntradaPersonaje().getOut();
			_out2 = _perso2.getCuenta().getEntradaPersonaje().getOut();
		}
		
		public synchronized long getKamas(int id) {
			int i = 0;
			if (_perso1.getID() == id)
				i = 1;
			else if (_perso2.getID() == id)
				i = 2;
			if (i == 1)
				return _kamas1;
			if (i == 2)
				return _kamas2;
			return 0L;
		}
		
		public synchronized void botonOK(int id) {
			int i = 0;
			if (_perso1.getID() == id)
				i = 1;
			else if (_perso2.getID() == id)
				i = 2;
			if (i == 1) {
				_ok1 = (!_ok1);
				SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, id);
				SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, id);
			} else if (i == 2) {
				_ok2 = (!_ok2);
				SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, id);
				SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, id);
			} else {
				return;
			}
			if ((_ok1) && (_ok2))
				aplicar();
		}
		
		public synchronized void setKamas(int id, long k) {
			_ok1 = false;
			_ok2 = false;
			int i = 0;
			if (_perso1.getID() == id)
				i = 1;
			else if (_perso2.getID() == id)
				i = 2;
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, _perso1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, _perso1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, _perso2.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, _perso2.getID());
			if (i == 1) {
				_kamas1 = k;
				SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out1, 'G', "", k + "");
				SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out2, 'G', "", k + "");
			} else if (i == 2) {
				_kamas2 = k;
				SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out2, 'G', "", k + "");
				SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out1, 'G', "", k + "");
			}
		}
		
		public synchronized void cancel() {
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out1);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out2);
			_perso1.setIntercambiandoCon(0);
			_perso2.setIntercambiandoCon(0);
			_perso1.setIntercambio(null);
			_perso2.setIntercambio(null);
			_perso1.setOcupado(false);
			_perso2.setOcupado(false);
		}
		
		public synchronized void aplicar() {
			_perso1.addKamas(-_kamas1 + _kamas2);
			_perso2.addKamas(-_kamas2 + _kamas1);
			for (Duo<Integer, Integer> duo : _objetos1) {
				int idObjeto = (duo._primero);
				int cant = (duo._segundo);
				if (cant == 0)
					continue;
				Objeto obj = getObjeto(idObjeto);
				if (obj.getCantidad() - cant < 1) {
					_perso1.borrarObjetoSinEliminar(idObjeto);
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out1, idObjeto);
					if (_perso2.addObjetoSimilar(obj, true, -1)) {
						eliminarObjeto(idObjeto);
					} else {
						_perso2.addObjetoPut(obj);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out2, obj);
					}
				} else {
					obj.setCantidad(obj.getCantidad() - cant);
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso1, obj);
					Objeto nuevoObj = Objeto.clonarObjeto(obj, cant);
					if (!_perso2.addObjetoSimilar(nuevoObj, true, idObjeto)) {
						Mundo.addObjeto(nuevoObj, true);
						_perso2.addObjetoPut(nuevoObj);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out2, nuevoObj);
					}
				}
			}
			for (Duo<Integer, Integer> duo : _objetos2) {
				int idObjeto = (duo._primero);
				int cant = (duo._segundo);
				if (cant == 0)
					continue;
				Objeto obj = getObjeto(idObjeto);
				if (obj.getCantidad() - cant < 1) {
					_perso2.borrarObjetoSinEliminar(idObjeto);
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out2, idObjeto);
					if (_perso1.addObjetoSimilar(obj, true, -1)) {
						eliminarObjeto(idObjeto);
					} else {
						_perso1.addObjetoPut(obj);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out1, obj);
					}
				} else {
					obj.setCantidad(obj.getCantidad() - cant);
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso2, obj);
					Objeto nuevoObj = Objeto.clonarObjeto(obj, cant);
					if (!_perso1.addObjetoSimilar(nuevoObj, true, idObjeto)) {
						Mundo.addObjeto(nuevoObj, true);
						_perso1.addObjetoPut(nuevoObj);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out1, nuevoObj);
					}
				}
			}
			_perso1.setIntercambiandoCon(0);
			_perso2.setIntercambiandoCon(0);
			_perso1.setIntercambio(null);
			_perso2.setIntercambio(null);
			SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso1);
			SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso2);
			SocketManager.ENVIAR_EV_INTERCAMBIO_EFECTUADO(_out1, 'a');
			SocketManager.ENVIAR_EV_INTERCAMBIO_EFECTUADO(_out2, 'a');
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso1);
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso2);
			_perso1.setOcupado(false);
			_perso2.setOcupado(false);
			try {
				StringBuffer str = new StringBuffer();
				boolean primero = false;
				for (Duo<Integer, Integer> d : _objetos1) {
					if (primero)
						str.append(";");
					str.append(d._primero + "," + d._segundo);
					primero = true;
				}
				StringBuffer str2 = new StringBuffer();
				primero = false;
				for (Duo<Integer, Integer> d : _objetos2) {
					if (primero)
						str2.append(";");
					str2.append(d._primero + "," + d._segundo);
					primero = true;
				}
				SQLManager.INSERT_INTERCAMBIO(_perso1.getNombre() + " a " + _perso2.getNombre()
				+ " los siguientes objetos: perso1 " + str.toString() + "   perso2 " + str2.toString());
			} catch (Exception e) {}
		}
		
		synchronized public void addObjeto(Objeto obj, int cant, int idPerso) {
			_ok1 = false;
			_ok2 = false;
			int idObj = obj.getID();
			int i = 0;
			if (_perso1.getID() == idPerso)
				i = 1;
			else
				i = 2;
			if (cant == 1)
				cant = 1;
			String str = idObj + "|" + cant;
			String add = "|" + obj.getObjModelo().getID() + "|" + obj.convertirStatsAString();
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, _perso1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, _perso1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, _perso2.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, _perso2.getID());
			if (i == 1) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetos1, idObj);
				if (duo != null) {
					duo._segundo += cant;
					SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out1, 'O', "+", idObj + "|" + duo._segundo);
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out2, 'O', "+", idObj + "|" + duo._segundo + add);
					return;
				}
				SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out1, 'O', "+", str);
				SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out2, 'O', "+", str + add);
				_objetos1.add(new Duo<Integer, Integer>(idObj, cant));
			} else if (i == 2) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetos2, idObj);
				if (duo != null) {
					duo._segundo += cant;
					SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out2, 'O', "+", idObj + "|" + duo._segundo);
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out1, 'O', "+", idObj + "|" + duo._segundo + add);
					return;
				}
				SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out2, 'O', "+", str);
				SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out1, 'O', "+", str + add);
				_objetos2.add(new Duo<Integer, Integer>(idObj, cant));
			}
		}
		
		public synchronized void borrarObjeto(Objeto obj, int cant, int idPerso) {
			int i = 0;
			if (_perso1.getID() == idPerso)
				i = 1;
			else
				i = 2;
			_ok1 = false;
			_ok2 = false;
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, _perso1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, _perso1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, _perso2.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, _perso2.getID());
			int idObj = obj.getID();
			String add = "|" + obj.getObjModelo().getID() + "|" + obj.convertirStatsAString();
			if (i == 1) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetos1, idObj);
				if (duo != null) {
					int nuevaCantidad = (duo._segundo) - cant;
					if (nuevaCantidad < 1) {
						_objetos1.remove(duo);
						SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out1, 'O', "-", idObj + "");
						SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out2, 'O', "-", idObj + "");
					} else {
						duo._segundo = nuevaCantidad;
						SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out1, 'O', "+", idObj + "|" + nuevaCantidad);
						SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out2, 'O', "+", idObj + "|" + nuevaCantidad + add);
					}
				}
			} else if (i == 2) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetos2, idObj);
				if (duo != null) {
					int nuevaCantidad = (duo._segundo) - cant;
					if (nuevaCantidad < 1) {
						_objetos2.remove(duo);
						SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out1, 'O', "-", idObj + "");
						SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out2, 'O', "-", idObj + "");
					} else {
						duo._segundo = nuevaCantidad;
						SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out1, 'O', "+", idObj + "|" + nuevaCantidad + add);
						SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out2, 'O', "+", idObj + "|" + nuevaCantidad);
					}
				}
			}
		}
		
		public synchronized int getCantObjeto(int objetoID, int idPerso) {
			ArrayList<Duo<Integer, Integer>> objetos;
			if (_perso1.getID() == idPerso)
				objetos = _objetos1;
			else
				objetos = _objetos2;
			for (Duo<Integer, Integer> duo : objetos) {
				if ((duo._primero) == objetoID) {
					return (duo._segundo);
				}
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
		private ArrayList<Duo<Integer, Integer>> _objArtesano1 = new ArrayList<Duo<Integer, Integer>>();
		private ArrayList<Duo<Integer, Integer>> _objCliente2 = new ArrayList<Duo<Integer, Integer>>();
		private boolean _ok1;
		private boolean _ok2;
		private int _maxIngredientes;
		private ArrayList<Duo<Integer, Integer>> _objetosPago = new ArrayList<Duo<Integer, Integer>>();
		private ArrayList<Duo<Integer, Integer>> _objetosSiSeConsegui = new ArrayList<Duo<Integer, Integer>>();
		
		public InvitarTaller(Personaje p1, Personaje p2, int max) {
			_artesano1 = p1;
			_cliente2 = p2;
			_out1 = _artesano1.getCuenta().getEntradaPersonaje().getOut();
			_out2 = _cliente2.getCuenta().getEntradaPersonaje().getOut();
			_maxIngredientes = max;
		}
		
		public long getKamasSiSeConsigue() {
			return _kamasSiSeConsigue;
		}
		
		public long getKamasPaga() {
			return _kamasPago;
		}
		
		public synchronized void botonOK(int id) {
			int i = 0;
			if (_artesano1.getID() == id)
				i = 1;
			else if (_cliente2.getID() == id)
				i = 2;
			if (i == 1) {
				_ok1 = (!_ok1);
				SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, id);
				SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, id);
			} else if (i == 2) {
				_ok2 = (!_ok2);
				SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, id);
				SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, id);
			} else {
				return;
			}
			if ((_ok1) && (_ok2))
				aplicar();
		}
		
		public void setKamas(int id, long k, long kamasT) {
			_ok1 = false;
			_ok2 = false;
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, _cliente2.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, _cliente2.getID());
			if (id == 1) {
				long kamasTotal = _kamasSiSeConsigue + k;
				if (kamasTotal > kamasT) {
					k = kamasT - _kamasSiSeConsigue;
				}
				_kamasPago = k;
			} else {
				long kamasTotal = _kamasPago + k;
				if (kamasTotal > kamasT) {
					k = kamasT - _kamasPago;
				}
				_kamasSiSeConsigue = k;
			}
			SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out1, id, "G", "+", k + "");
			SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out2, id, "G", "+", k + "");
		}
		
		public synchronized void cancel() {
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out1);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out2);
			_artesano1.setIntercambiandoCon(0);
			_cliente2.setIntercambiandoCon(0);
			_artesano1.setTallerInvitado(null);
			_cliente2.setTallerInvitado(null);
			_artesano1.setOcupado(false);
			_cliente2.setOcupado(false);
		}
		
		public void aplicar() {
			AccionTrabajo trabajo = _artesano1.getHaciendoTrabajo();
			boolean resultado = trabajo.iniciarTrabajoPago(_artesano1, _cliente2, _objArtesano1, _objCliente2);
			StatsOficio oficio = _artesano1.getOficioPorTrabajo(trabajo.getIDTrabajo());
			if (oficio != null) {
				if (resultado) {
					_cliente2.setKamas(_cliente2.getKamas() - _kamasSiSeConsigue);
					_artesano1.setKamas(_artesano1.getKamas() + _kamasSiSeConsigue);
					for (Duo<Integer, Integer> duo : _objetosSiSeConsegui) {
						int idObjeto = (duo._primero);
						int cant = (duo._segundo);
						if (cant == 0)
							continue;
						Objeto obj = getObjeto(idObjeto);
						if (obj.getCantidad() - cant < 1) {
							_cliente2.borrarObjetoSinEliminar(idObjeto);
							SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out2, idObjeto);
							if (_artesano1.addObjetoSimilar(obj, true, -1)) {
								eliminarObjeto(idObjeto);
							} else {
								_artesano1.addObjetoPut(obj);
								SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out1, obj);
							}
						} else {
							obj.setCantidad(obj.getCantidad() - cant);
							SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_cliente2, obj);
							Objeto nuevoObj = Objeto.clonarObjeto(obj, cant);
							if (!_artesano1.addObjetoSimilar(nuevoObj, true, idObjeto)) {
								Mundo.addObjeto(nuevoObj, true);
								_artesano1.addObjetoPut(nuevoObj);
								SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out1, nuevoObj);
							}
						}
					}
				}
				if (!oficio.esGratisSiFalla() || resultado) {
					_cliente2.setKamas(_cliente2.getKamas() - _kamasPago);
					_artesano1.setKamas(_artesano1.getKamas() + _kamasPago);
					for (Duo<Integer, Integer> duo : _objetosPago) {
						int idObjeto = (duo._primero);
						int cant = (duo._segundo);
						if (cant == 0)
							continue;
						Objeto obj = getObjeto(idObjeto);
						if (obj.getCantidad() - cant < 1) {
							_cliente2.borrarObjetoSinEliminar(idObjeto);
							SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out2, idObjeto);
							if (_artesano1.addObjetoSimilar(obj, true, -1)) {
								eliminarObjeto(idObjeto);
							} else {
								_artesano1.addObjetoPut(obj);
								SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out1, obj);
							}
						} else {
							obj.setCantidad(obj.getCantidad() - cant);
							SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_cliente2, obj);
							Objeto nuevoObj = Objeto.clonarObjeto(obj, cant);
							if (!_artesano1.addObjetoSimilar(nuevoObj, true, idObjeto)) {
								Mundo.addObjeto(nuevoObj, true);
								_artesano1.addObjetoPut(nuevoObj);
								SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out1, nuevoObj);
							}
						}
					}
				}
			}
			_objetosSiSeConsegui.clear();
			_objetosPago.clear();
			_objArtesano1.clear();
			_objCliente2.clear();
			_kamasPago = 0L;
			_kamasSiSeConsigue = 0L;
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_artesano1);
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_cliente2);
			SocketManager.ENVIAR_Ec_INICIAR_RECETA(_artesano1, _artesano1.getForjaEcK());
			SocketManager.ENVIAR_Ec_INICIAR_RECETA(_cliente2, _cliente2.getForjaEcK());
		}
		
		synchronized public void addObjeto(Objeto obj, int cant, int idPerso) {
			if (cantObjetosActual() >= _maxIngredientes) {
				return;
			}
			_ok1 = false;
			_ok2 = false;
			int idObj = obj.getID();
			int i = 0;
			if (_artesano1.getID() == idPerso)
				i = 1;
			else
				i = 2;
			if (cant == 1)
				cant = 1;
			String str = idObj + "|" + cant;
			String add = "|" + obj.getObjModelo().getID() + "|" + obj.convertirStatsAString();
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, _cliente2.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, _cliente2.getID());
			if (i == 1) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objArtesano1, idObj);
				if (duo != null) {
					duo._segundo += cant;
					SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out1, 'O', "+", "" + idObj + "|" + duo._segundo);
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out2, 'O', "+", "" + idObj + "|" + duo._segundo + add);
					return;
				}
				SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out1, 'O', "+", str);
				SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out2, 'O', "+", str + add);
				_objArtesano1.add(new Duo<Integer, Integer>(idObj, cant));
			} else if (i == 2) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objCliente2, idObj);
				if (duo != null) {
					duo._segundo += cant;
					SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out2, 'O', "+", "" + idObj + "|" + duo._segundo);
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out1, 'O', "+", "" + idObj + "|" + duo._segundo + add);
					return;
				}
				SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out2, 'O', "+", str);
				SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out1, 'O', "+", str + add);
				_objCliente2.add(new Duo<Integer, Integer>(idObj, cant));
			}
		}
		
		public synchronized void borrarObjeto(Objeto obj, int cant, int idPerso) {
			int i = 0;
			if (_artesano1.getID() == idPerso)
				i = 1;
			else
				i = 2;
			_ok1 = false;
			_ok2 = false;
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, _cliente2.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, _cliente2.getID());
			int idObj = obj.getID();
			String add = "|" + obj.getObjModelo().getID() + "|" + obj.convertirStatsAString();
			if (i == 1) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objArtesano1, idObj);
				int nuevaCantidad = (duo._segundo) - cant;
				if (nuevaCantidad < 1) {
					_objArtesano1.remove(duo);
					SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out1, 'O', "-", idObj + "");
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out2, 'O', "-", idObj + "");
				} else {
					duo._segundo = nuevaCantidad;
					SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out1, 'O', "+", idObj + "|" + nuevaCantidad);
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out2, 'O', "+", idObj + "|" + nuevaCantidad + add);
				}
			} else if (i == 2) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objCliente2, idObj);
				int nuevaCantidad = (duo._segundo) - cant;
				if (nuevaCantidad < 1) {
					_objCliente2.remove(duo);
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out1, 'O', "-", idObj + "");
					SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out2, 'O', "-", idObj + "");
				} else {
					duo._segundo = nuevaCantidad;
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out1, 'O', "+", idObj + "|" + nuevaCantidad + add);
					SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out2, 'O', "+", idObj + "|" + nuevaCantidad);
				}
			}
		}
		
		synchronized public void addObjetoPaga(Objeto obj, int cant, int idPago) {
			if (cantObjetosActual() >= _maxIngredientes) {
				return;
			}
			_ok1 = false;
			_ok2 = false;
			int idObj = obj.getID();
			if (cant == 1)
				cant = 1;
			String str = idObj + "|" + cant;
			String add = "|" + obj.getObjModelo().getID() + "|" + obj.convertirStatsAString();
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, _cliente2.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, _cliente2.getID());
			if (idPago == 1) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetosPago, idObj);
				if (duo != null) {
					duo._segundo += cant;
					SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out1, idPago, "O", "+", idObj + "|" + duo._segundo + add);
					SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out2, idPago, "O", "+", idObj + "|" + duo._segundo);
					return;
				}
				SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out1, idPago, "O", "+", str + add);
				SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out2, idPago, "O", "+", str);
				_objetosPago.add(new Duo<Integer, Integer>(idObj, cant));
			} else {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetosSiSeConsegui, idObj);
				if (duo != null) {
					duo._segundo += cant;
					SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out1, idPago, "O", "+", idObj + "|" + duo._segundo + add);
					SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out2, idPago, "O", "+", idObj + "|" + duo._segundo);
					return;
				}
				SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out1, idPago, "O", "+", str + add);
				SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out2, idPago, "O", "+", str);
				_objetosSiSeConsegui.add(new Duo<Integer, Integer>(idObj, cant));
			}
		}
		
		public synchronized void borrarObjetoPaga(Objeto obj, int cant, int idPago) {
			int idObj = obj.getID();
			_ok1 = false;
			_ok2 = false;
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok1, _artesano1.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out1, _ok2, _cliente2.getID());
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out2, _ok2, _cliente2.getID());
			String add = "|" + obj.getObjModelo().getID() + "|" + obj.convertirStatsAString();
			if (idPago == 1) {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetosPago, idObj);
				if (duo == null)
					return;
				int nuevaCantidad = (duo._segundo) - cant;
				SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out1, idPago, "O", "-", idObj + "");
				SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out2, idPago, "O", "-", idObj + "");
				if (nuevaCantidad < 1) {
					_objetosPago.remove(duo);
				} else {
					duo._segundo = nuevaCantidad;
					SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out1, idPago, "O", "+", idObj + "|" + nuevaCantidad + add);
					SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out2, idPago, "O", "+", idObj + "|" + nuevaCantidad);
				}
			} else {
				Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetosSiSeConsegui, idObj);
				if (duo == null)
					return;
				int nuevaCantidad = (duo._segundo) - cant;
				SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out1, idPago, "O", "-", idObj + "");
				SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out2, idPago, "O", "-", idObj + "");
				if (nuevaCantidad < 1) {
					_objetosSiSeConsegui.remove(duo);
				} else {
					duo._segundo = nuevaCantidad;
					SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out1, idPago, "O", "+", idObj + "|" + nuevaCantidad + add);
					SocketManager.ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(_out2, idPago, "O", "+", idObj + "|" + nuevaCantidad);
				}
			}
		}
		
		public synchronized int getCantObjeto(int idObj, int idPerso) {
			ArrayList<Duo<Integer, Integer>> objetos;
			if (_artesano1.getID() == idPerso)
				objetos = _objArtesano1;
			else
				objetos = _objCliente2;
			for (Duo<Integer, Integer> duo : objetos) {
				if ((duo._primero) == idObj) {
					return (duo._segundo);
				}
			}
			return 0;
		}
		
		public synchronized int getCantObjetoPago(int idObj, int idPerso) {
			ArrayList<Duo<Integer, Integer>> objetos;
			if (idPerso == 1)
				objetos = _objetosPago;
			else
				objetos = _objetosSiSeConsegui;
			for (Duo<Integer, Integer> duo : objetos) {
				if ((duo._primero) == idObj) {
					return (duo._segundo);
				}
			}
			return 0;
		}
		
		public int cantObjetosActual() {
			int cant = _objArtesano1.size() + _objCliente2.size();
			return cant;
		}
	}
	public static class ObjetoSet {
		private int _id;
		private ArrayList<ObjetoModelo> _objetosModelos = new ArrayList<ObjetoModelo>();
		private ArrayList<Stats> _bonus = new ArrayList<Stats>();
		
		public ObjetoSet(int id, String items, String bonuses) {
			_id = id;
			for (String str : items.split(","))
				try {
					ObjetoModelo t = getObjModelo(Integer.parseInt(str.trim()));
					if (t == null)
						continue;
					_objetosModelos.add(t);
				} catch (Exception localException) {}
			_bonus.add(new Stats());
			for (String str : bonuses.split(";")) {
				Stats S = new Stats();
				for (String str2 : str.split(","))
					try {
						String[] infos = str2.split(":");
						int stat = Integer.parseInt(infos[0]);
						int value = Integer.parseInt(infos[1]);
						S.addStat(stat, value);
					} catch (Exception localException1) {}
				_bonus.add(S);
			}
		}
		
		public int getId() {
			return _id;
		}
		
		public Stats getBonusStatPorNroObj(int numero) {
			if (numero > _bonus.size())
				return new Stats();
			return _bonus.get(numero - 1);
		}
		
		public ArrayList<ObjetoModelo> getObjetosModelos() {
			return _objetosModelos;
		}
	}

	public static class SubArea {
		private int _id;
		private Area _area;
		private int _alineacion;
		private String _nombre;
		private ArrayList<Mapa> _mapas = new ArrayList<Mapa>();
		private boolean _conquistable;
		private int _prisma;
		public static int _bontas = 0;
		public static int _brakmars = 0;
		
		public SubArea(int id, short areaID, int alineacion, String nombre, int conquistable, int prisma) {
			_id = id;
			_nombre = nombre;
			_area = Mundo.getArea(areaID);
			_alineacion = 0;
			_conquistable = (conquistable == 0);
			_prisma = prisma;
			_prisma = 0;
			if (getPrisma(prisma) != null) {
				_alineacion = alineacion;
				_prisma = prisma;
			}
			if (_alineacion == 1)
				_bontas += 1;
			else if (_alineacion == 2)
				_brakmars += 1;
		}
		
		public String getNombre() {
			return _nombre;
		}
		
		public int getPrismaID() {
			return _prisma;
		}
		
		public void setPrismaID(int prisma) {
			_prisma = prisma;
		}
		
		public boolean getConquistable() {
			return _conquistable;
		}
		
		public int getID() {
			return _id;
		}
		
		public Area getArea() {
			return _area;
		}
		
		public int getAlineacion() {
			return _alineacion;
		}
		
		public void setAlineacion(int alineacion) {
			if ((_alineacion == 1) && (alineacion == -1))
				_bontas -= 1;
			else if ((_alineacion == 2) && (alineacion == -1))
				_brakmars -= 1;
			else if ((_alineacion == -1) && (alineacion == 1))
				_bontas += 1;
			else if ((_alineacion == -1) && (alineacion == 2))
				_brakmars += 1;
			_alineacion = alineacion;
		}
		
		public ArrayList<Mapa> getMapas() {
			return _mapas;
		}
		
		public void addMapa(Mapa mapa) {
			_mapas.add(mapa);
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
		private ArrayList<Area> _areas = new ArrayList<Area>();
		
		public SuperArea(int id) {
			_id = id;
		}
		
		public void addArea(Area area) {
			_areas.add(area);
		}
		
		public int getID() {
			return _id;
		}
	}
	public static class Trueque {
		private Personaje _perso;
		private PrintWriter _out;
		private ArrayList<Duo<Integer, Integer>> _objetos = new ArrayList<Duo<Integer, Integer>>();
		private boolean _ok;
		private String _objetoPedir = "";
		private String _objetoDar = "";
		private int _objetoConseguir = -1;
		private int _cantObjConseguir = -1;
		private boolean _resucitar = false;
		private int _idMascota = -1;
		
		public Trueque(Personaje perso, String Objetopedir, String Objetodar) {
			_perso = perso;
			_objetoPedir = Objetopedir;
			_objetoDar = Objetodar;
			if (_objetoDar.equalsIgnoreCase("resucitar"))
				_resucitar = true;
			_out = _perso.getCuenta().getEntradaPersonaje().getOut();
		}
		
		public synchronized void botonOK(int id) {
			int i = 0;
			if (_perso.getID() == id)
				i = 1;
			if (i == 1) {
				_ok = (!_ok);
				SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out, _ok, id);
			} else {
				return;
			}
			if (_ok)
				aplicar();
		}
		
		public synchronized void cancel() {
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			_perso.setTrueque(null);
		}
		
		public synchronized void aplicar() {
			for (Duo<Integer, Integer> duo : _objetos) {
				int idObj = (duo._primero);
				int cant = (duo._segundo);
				if (cant == 0)
					continue;
				if (!_perso.tieneObjetoID(idObj)) {
					cant = 0;
				} else {
					Objeto obj = getObjeto(idObj);
					int nuevaCant = obj.getCantidad() - cant;
					if (_resucitar) {
						if (nuevaCant < 1) {
							SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, idObj);
							if (obj.getObjModelo().getTipo() != 90) {
								_perso.borrarObjetoSinEliminar(idObj);
								eliminarObjeto(idObj);
							} else {
								_idMascota = idObj;
							}
						} else {
							obj.setCantidad(nuevaCant);
							SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, obj);
						}
					} else if (nuevaCant < 1) {
						_perso.borrarObjetoSinEliminar(idObj);
						SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, idObj);
						eliminarObjeto(idObj);
					} else {
						obj.setCantidad(nuevaCant);
						SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, obj);
					}
				}
			}
			if (_resucitar) {
				if (_idMascota != -1) {
					Objeto objMasc = getObjeto(_idMascota);
					objMasc.setCantidad(1);
					objMasc.setPosicion(-1);
					objMasc.setIDModelo(Constantes.resucitarMascota(objMasc.getObjModelo().getID()));
					SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, objMasc);
				}
			} else if ((_objetoConseguir != -1) && (_cantObjConseguir != -1)) {
				Objeto nuevoObjeto = getObjModelo(_objetoConseguir).crearObjDesdeModelo(_cantObjConseguir, false);
				if (!_perso.addObjetoSimilar(nuevoObjeto, true, -1)) {
					addObjeto(nuevoObjeto, true);
					_perso.addObjetoPut(nuevoObjeto);
					SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, nuevoObjeto);
				}
			}
			_perso.setTrueque(null);
			SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
			SocketManager.ENVIAR_EV_INTERCAMBIO_EFECTUADO(_out, 'a');
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
			_perso.setOcupado(false);
		}
		
		synchronized public void addObjetoTrueque(int idObjeto, int cantObj) {
			_ok = false;
			if (cantObj == 1)
				cantObj = 1;
			String str = idObjeto + "|" + cantObj;
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out, _ok, _perso.getID());
			Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetos, idObjeto);
			if (duo != null) {
				duo._segundo += cantObj;
				SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out, 'O', "+", idObjeto + "|" + duo._segundo);
				if (_resucitar) {
					if (_idMascota != -1)
						SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "-", "" + _idMascota);
				} else if (_objetoConseguir != -1)
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "-", "" + 1);
				_objetoConseguir = -1;
				_idMascota = -1;
				String[] pedir = _objetoPedir.split("\\|");
				int cantSolicitadas = 0;
				int j = 0;
				for (Duo<Integer, Integer> acouple : _objetos) {
					ObjetoModelo objModelo = getObjeto(acouple._primero).getObjModelo();
					int idModelo = objModelo.getID();
					if (_resucitar) {
						if (objModelo.getTipo() == 90) {
							_idMascota = acouple._primero;
						}
					}
					for (String apedir : pedir) {
						if (idModelo == Integer.parseInt(apedir.split(",")[0])) {
							int cantidades = (acouple._segundo / Integer.parseInt(apedir.split(",")[1]));
							if (cantidades < 1)
								continue;
							if (cantSolicitadas == 0 || cantidades < cantSolicitadas)
								cantSolicitadas = cantidades;
							j++;
							break;
						}
						continue;
					}
				}
				if (cantSolicitadas > 0 && (pedir.length == j)) {
					if (_resucitar) {
						if (_idMascota != -1) {
							Objeto mascota = getObjeto(_idMascota);
							SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "+", _idMascota + "|1|"
							+ Constantes.resucitarMascota(mascota.getObjModelo().getID()) + "|" + mascota.convertirStatsAString()
							+ ",320#0#0#1");
						}
					} else {
						int idObjModDar = Integer.parseInt(_objetoDar.split(",")[0]);
						int cant = Integer.parseInt(_objetoDar.split(",")[1]);
						int cantFinal = cant * cantSolicitadas;
						SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "+", 1 + "|" + cantFinal + "|" + idObjModDar + "|"
						+ getObjModelo(idObjModDar).getStringStatsObj());
						_objetoConseguir = idObjModDar;
						_cantObjConseguir = cantFinal;
					}
				}
				return;
			}
			SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out, 'O', "+", str);
			_objetos.add(new Duo<Integer, Integer>(idObjeto, cantObj));
			_objetoConseguir = -1;
			_idMascota = -1;
			String[] pedir = _objetoPedir.split("\\|");
			int cantSolicitadas = 0;
			int j = 0;
			for (Duo<Integer, Integer> acouple : _objetos) {
				ObjetoModelo objModelo = getObjeto(acouple._primero).getObjModelo();
				int idModelo = objModelo.getID();
				if (_resucitar) {
					if (objModelo.getTipo() == 90) {
						_idMascota = acouple._primero;
					}
				}
				for (String apedir : pedir) {
					if (idModelo == Integer.parseInt(apedir.split(",")[0])) {
						int cantidades = (acouple._segundo / Integer.parseInt(apedir.split(",")[1]));
						if (cantidades < 1)
							continue;
						if (cantSolicitadas == 0 || cantidades < cantSolicitadas)
							cantSolicitadas = cantidades;
						j++;
						break;
					}
					continue;
				}
			}
			if (cantSolicitadas > 0 && (pedir.length == j)) {
				if (_resucitar) {
					if (_idMascota != -1) {
						Objeto mascota = getObjeto(_idMascota);
						SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "+", _idMascota + "|1|"
						+ Constantes.resucitarMascota(mascota.getObjModelo().getID()) + "|" + mascota.convertirStatsAString()
						+ ",320#0#0#1");
					}
				} else {
					int idObjModDar = Integer.parseInt(_objetoDar.split(",")[0]);
					int cant = Integer.parseInt(_objetoDar.split(",")[1]);
					int cantFinal = cant * cantSolicitadas;
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "+", 1 + "|" + cantFinal + "|" + idObjModDar + "|"
					+ getObjModelo(idObjModDar).getStringStatsObj());
					_objetoConseguir = idObjModDar;
					_cantObjConseguir = cantFinal;
				}
			}
			return;
		}
		
		public synchronized void quitarObjeto(int idObjeto, int cantObjeto) {
			_ok = false;
			SocketManager.ENVIAR_EK_CHECK_OK_INTERCAMBIO(_out, _ok, _perso.getID());
			Duo<Integer, Integer> duo = Mundo.getDuoPorIDObjeto(_objetos, idObjeto);
			int nuevaCant = (duo._segundo) - cantObjeto;
			if (nuevaCant < 1) {
				_objetos.remove(duo);
				SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out, 'O', "-", idObjeto + "");
			} else {
				duo._segundo = nuevaCant;
				SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out, 'O', "+", idObjeto + "|" + nuevaCant);
			}
			if (_resucitar) {
				if (_idMascota != -1)
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "-", _idMascota + "");
			} else if (_objetoConseguir != -1)
				SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "-", "1");
			_objetoConseguir = -1;
			_idMascota = -1;
			String[] pedir = _objetoPedir.split("\\|");
			int cantSolicitadas = 0;
			int j = 0;
			for (Duo<Integer, Integer> acouple : _objetos) {
				ObjetoModelo objModelo = getObjeto(acouple._primero).getObjModelo();
				int idModelo = objModelo.getID();
				if ((_resucitar) && (objModelo.getTipo() == 90)) {
					_idMascota = (acouple._primero);
				}
				for (String apedir : pedir) {
					if (idModelo == Integer.parseInt(apedir.split(",")[0])) {
						int cantidades = (acouple._segundo) / Integer.parseInt(apedir.split(",")[1]);
						if (cantidades < 1)
							continue;
						if ((cantSolicitadas == 0) || (cantidades < cantSolicitadas))
							cantSolicitadas = cantidades;
						j++;
						break;
					}
				}
			}
			if ((cantSolicitadas > 0) && (pedir.length == j))
				if (_resucitar) {
					if (_idMascota != -1) {
						Objeto mascota = getObjeto(_idMascota);
						SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "+", _idMascota + "|1|"
						+ Constantes.resucitarMascota(mascota.getObjModelo().getID()) + "|" + mascota.convertirStatsAString()
						+ ",320#0#0#1");
					}
				} else {
					int idObjModDar = Integer.parseInt(_objetoDar.split(",")[0]);
					int cant = Integer.parseInt(_objetoDar.split(",")[1]);
					int cantFinal = cant * cantSolicitadas;
					SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, 'O', "+", "1|" + cantFinal + "|" + idObjModDar + "|"
					+ getObjModelo(idObjModDar).getStringStatsObj());
					_objetoConseguir = idObjModDar;
					_cantObjConseguir = cantFinal;
				}
		}
		
		public synchronized int getCantObj(int objetoID, int personajeId) {
			ArrayList<Duo<Integer, Integer>> objetos = null;
			if (_perso.getID() == personajeId)
				objetos = _objetos;
			for (Duo<Integer, Integer> duo : objetos) {
				if ((duo._primero) == objetoID) {
					return (duo._segundo);
				}
			}
			return 0;
		}
	}
}