package objects;

import common.Constantes;
import common.Fórmulas;
import common.LesGuardians;
import common.SocketManager;
import common.Mundo;
import game.GameThread;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.Timer;
import objects.Mapa;
import objects.Objeto;
import objects.Personaje;
import objects.EfectoHechizo;

public class Oficio {
    private int _id;
    private ArrayList<Integer> _herramientas = new ArrayList<Integer>();
    private Map<Integer, ArrayList<Integer>> _recetas = new TreeMap<Integer, ArrayList<Integer>>();

    public Oficio(int id, String herramientas, String recetas) {
        this._id = id;
        if (!herramientas.equals("")) {
            for (String str : herramientas.split(",")) {
                try {
                    int herramienta = Integer.parseInt(str);
                    this._herramientas.add(herramienta);
                }
                catch (Exception herramienta) {}
            }
        }
        if (!recetas.equals("")) {
            for (String str : recetas.split("\\|")) {
                try {
                    int trabajoID = Integer.parseInt(str.split(";")[0]);
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for (String str2 : str.split(";")[1].split(",")) {
                        list.add(Integer.parseInt(str2));
                    }
                    this._recetas.put(trabajoID, list);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    public ArrayList<Integer> listaRecetaPorTrabajo(int trabajo) {
        return this._recetas.get(trabajo);
    }

    public boolean puedeReceta(int trabajo, int modelo) {
        if (this._recetas.get(trabajo) != null) {
            for (int a : this._recetas.get(trabajo)) {
                if (a != modelo) continue;
                return true;
            }
        }
        return false;
    }

    public int getID() {
        return this._id;
    }

    public boolean herramientaValida(int t) {
        for (int a : this._herramientas) {
            if (t != a) continue;
            return true;
        }
        return false;
    }

    public static byte tieneStatActualObjeto(Objeto obj, String stat) {
        if (!obj.convertirStatsAString().isEmpty()) {
            for (Map.Entry<Integer, Integer> entry : obj.getStats().getStatsComoMap().entrySet()) {
                String statObjActual = Integer.toHexString(entry.getKey());
                if (statObjActual.toLowerCase().compareTo(stat.toLowerCase()) > 0) {
                    if (statObjActual.toLowerCase().compareTo("98") == 0 && stat.toLowerCase().compareTo("7b") == 0) {
                        return 2;
                    }
                    if (statObjActual.toLowerCase().compareTo("9a") == 0 && stat.toLowerCase().compareTo("77") == 0) {
                        return 2;
                    }
                    if (statObjActual.toLowerCase().compareTo("9b") == 0 && stat.toLowerCase().compareTo("7e") == 0) {
                        return 2;
                    }
                    if (statObjActual.toLowerCase().compareTo("9d") == 0 && stat.toLowerCase().compareTo("76") == 0) {
                        return 2;
                    }
                    if (statObjActual.toLowerCase().compareTo("9c") == 0 && stat.toLowerCase().compareTo("7c") == 0) {
                        return 2;
                    }
                    if (statObjActual.toLowerCase().compareTo("99") == 0 && stat.toLowerCase().compareTo("7d") == 0) {
                        return 2;
                    }
                }
                if (statObjActual.toLowerCase().compareTo(stat.toLowerCase()) != 0) continue;
                return 1;
            }
        }
        return 0;
    }

    public static byte tieneStatBaseObjeto(Objeto obj, String stat) {
        String[] split;
        String[] arrstring = split = obj.getModelo().getStringStatsObj().split(",");
        int n = split.length;
        for (int i = 0; i < n; ++i) {
            String s = arrstring[i];
            String[] stats = s.split("#");
            if (stats[0].toLowerCase().compareTo(stat.toLowerCase()) > 0) {
                if (stats[0].toLowerCase().compareTo("98") == 0 && stat.toLowerCase().compareTo("7b") == 0) {
                    return 2;
                }
                if (stats[0].toLowerCase().compareTo("9a") == 0 && stat.toLowerCase().compareTo("77") == 0) {
                    return 2;
                }
                if (stats[0].toLowerCase().compareTo("9b") == 0 && stat.toLowerCase().compareTo("7e") == 0) {
                    return 2;
                }
                if (stats[0].toLowerCase().compareTo("9d") == 0 && stat.toLowerCase().compareTo("76") == 0) {
                    return 2;
                }
                if (stats[0].toLowerCase().compareTo("9c") == 0 && stat.toLowerCase().compareTo("7c") == 0) {
                    return 2;
                }
                if (stats[0].toLowerCase().compareTo("99") == 0 && stat.toLowerCase().compareTo("7d") == 0) {
                    return 2;
                }
            }
            if (stats[0].toLowerCase().compareTo(stat.toLowerCase()) != 0) continue;
            return 1;
        }
        return 0;
    }

    public static int pesoStatActual(Objeto obj, String statsModif) {
        for (Map.Entry<Integer, Integer> entry : obj.getStats().getStatsComoMap().entrySet()) {
            int statID = entry.getKey();
            if (Integer.toHexString(statID).toLowerCase().compareTo(statsModif.toLowerCase()) > 0 || Integer.toHexString(statID).toLowerCase().compareTo(statsModif.toLowerCase()) != 0) continue;
            int multi = 1;
            int coef = 1;
            byte tieneStatBase = Oficio.tieneStatBaseObjeto(obj, Integer.toHexString(statID));
            if (tieneStatBase == 2) {
                coef = 3;
            } else if (tieneStatBase == 0) {
                coef = 8;
            }
            if (statID == 125 || statID == 158 || statID == 174) {
                multi = 1;
            } else if (statID == 118 || statID == 126 || statID == 119 || statID == 123) {
                multi = 2;
            } else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
                multi = 3;
            } else if (statID == 124 || statID == 176) {
                multi = 5;
            } else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
                multi = 7;
            } else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
                multi = 8;
            } else if (statID == 225) {
                multi = 15;
            } else if (statID == 178 || statID == 112) {
                multi = 20;
            } else if (statID == 115 || statID == 182) {
                multi = 30;
            } else if (statID == 117) {
                multi = 50;
            } else if (statID == 128) {
                multi = 90;
            } else if (statID == 111) {
                multi = 100;
            }
            int peso = entry.getValue() * multi * coef;
            return peso;
        }
        return 0;
    }

    public static int pesoTotalActualObj(String statsModelo, Objeto obj) {
        String[] split;
        int peso = 0;
        int suma = 0;
        String[] arrstring = split = statsModelo.split(",");
        int n = split.length;
        for (int i = 0; i < n; ++i) {
            String s = arrstring[i];
            String[] stats = s.split("#");
            int statID = Integer.parseInt(stats[0], 16);
            boolean siguiente = false;
            int[] arrn = Constantes.ID_EFECTOS_ARMAS;
            int n2 = Constantes.ID_EFECTOS_ARMAS.length;
            for (int j = 0; j < n2; ++j) {
                int a = arrn[j];
                if (a != statID) continue;
                siguiente = true;
            }
            if (siguiente) continue;
            String jet = "";
            int cantidad = 1;
            try {
                jet = stats[4];
                cantidad = Fórmulas.getRandomValor(jet);
                try {
                    int min = Integer.parseInt(stats[1], 16);
                    int max = Integer.parseInt(stats[2], 16);
                    cantidad = min;
                    if (max != 0) {
                        cantidad = max;
                    }
                }
                catch (Exception e) {
                    cantidad = Fórmulas.getRandomValor(jet);
                }
            }
            catch (Exception e) {
                // empty catch block
            }
            int multi = 1;
            int coef = 1;
            byte tieneStatBase = Oficio.tieneStatBaseObjeto(obj, stats[0]);
            if (tieneStatBase == 2) {
                coef = 3;
            } else if (tieneStatBase == 0) {
                coef = 8;
            }
            if (statID == 125 || statID == 158 || statID == 174) {
                multi = 1;
            } else if (statID == 118 || statID == 126 || statID == 119 || statID == 123) {
                multi = 2;
            } else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
                multi = 3;
            } else if (statID == 124 || statID == 176) {
                multi = 5;
            } else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
                multi = 7;
            } else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
                multi = 8;
            } else if (statID == 225) {
                multi = 15;
            } else if (statID == 178 || statID == 112) {
                multi = 20;
            } else if (statID == 115 || statID == 182) {
                multi = 30;
            } else if (statID == 117) {
                multi = 50;
            } else if (statID == 128) {
                multi = 90;
            } else if (statID == 111) {
                multi = 100;
            }
            peso = cantidad * multi * coef;
            suma += peso;
        }
        return suma;
    }

    public static int pesoTotalBaseObj(int idObjModelo) {
        String[] split;
        int peso = 0;
        int suma = 0;
        String statsModelo = "";
        statsModelo = Mundo.getObjModelo(idObjModelo).getStringStatsObj();
        if (statsModelo == null || statsModelo.isEmpty()) {
            return 0;
        }
        String[] arrstring = split = statsModelo.split(",");
        int n = split.length;
        for (int i = 0; i < n; ++i) {
            String s = arrstring[i];
            String[] stats = s.split("#");
            int statID = Integer.parseInt(stats[0], 16);
            boolean sig = true;
            int[] arrn = Constantes.ID_EFECTOS_ARMAS;
            int n2 = Constantes.ID_EFECTOS_ARMAS.length;
            for (int j = 0; j < n2; ++j) {
                int a = arrn[j];
                if (a != statID) continue;
                sig = false;
            }
            if (!sig) continue;
            String jet = "";
            int valor = 1;
            try {
                jet = stats[4];
                valor = Fórmulas.getRandomValor(jet);
                try {
                    int min = Integer.parseInt(stats[1], 16);
                    int max = Integer.parseInt(stats[2], 16);
                    valor = min;
                    if (max != 0) {
                        valor = max;
                    }
                }
                catch (Exception e) {
                    valor = Fórmulas.getRandomValor(jet);
                }
            }
            catch (Exception e) {
                // empty catch block
            }
            int multi = 1;
            if (statID == 125 || statID == 158 || statID == 174) {
                multi = 1;
            } else if (statID == 118 || statID == 126 || statID == 119 || statID == 123) {
                multi = 2;
            } else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
                multi = 3;
            } else if (statID == 124 || statID == 176) {
                multi = 5;
            } else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
                multi = 7;
            } else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
                multi = 8;
            } else if (statID == 225) {
                multi = 15;
            } else if (statID == 178 || statID == 112) {
                multi = 20;
            } else if (statID == 115 || statID == 182) {
                multi = 30;
            } else if (statID == 117) {
                multi = 50;
            } else if (statID == 128) {
                multi = 90;
            } else if (statID == 111) {
                multi = 100;
            }
            peso = valor * multi;
            suma += peso;
        }
        return suma;
    }

    public static class AccionTrabajo {
        private int _idTrabajoMod;
        private int _casillasMin = 1;
        private int _casillasMax = 1;
        private boolean _esReceta;
        private int _suerte = 100;
        private int _tiempo = 0;
        private int _xpGanada = 0;
        private long _iniciarTiempo;
        private Map<Integer, Integer> _ingredientes = new TreeMap<Integer, Integer>();
        private Map<Integer, Integer> _ultimoTrabajo = new TreeMap<Integer, Integer>();
        private Timer _tiempoTrabajo;
        private Personaje _artesano;
        private Personaje _cliente;
        private int _veces = -1;
        public static float _tolerNormal = 1.3f;
        public static float _tolerVIP = 2.8f;
        private String _datos = "";
        private boolean _primero = false;
        private boolean _romperse = false;
        private boolean _interrumpir = false;

        public AccionTrabajo(int idTrabajo, int min, int max, boolean esReceta, int nSuerteTiempo, int xpGanada) {
            this._idTrabajoMod = idTrabajo;
            this._casillasMin = min;
            this._casillasMax = max;
            this._esReceta = esReceta;
            if (esReceta) {
                this._suerte = nSuerteTiempo;
            } else {
                this._tiempo = nSuerteTiempo;
            }
            this._xpGanada = xpGanada;
            this._tiempoTrabajo = new Timer(800, new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    AccionTrabajo.this.iniciarReceta();
                    AccionTrabajo.this._tiempoTrabajo.stop();
                }
            });
        }

        public void finalizarAccionTrabajo(Personaje perso, GameThread.AccionDeJuego AJ, Mapa.Celda celda) {
            Mapa.ObjetoInteractivo OI = celda.getObjetoInterac();
            if (this._esReceta) {
                SocketManager.ENVIAR_GDF_FORZADO_PERSONAJE(perso, celda.getID(), 1, 1);
            } else if (OI != null) {
                if (this._iniciarTiempo - System.currentTimeMillis() > 500L) {
                    return;
                }
                if (OI != null) {
                    OI.iniciarTiempoRefresco();
                    SocketManager.ENVIAR_GDF_ESTADO_OBJETO_INTERACTIVO(perso.getMapa(), celda);
                    OI.setEstado(4);
                }
                boolean especial = Fórmulas.getRandomValor(0, LesGuardians.PROBABILIDADE_OBJ_ESPECIAL) == 0;
                int cant = this._casillasMax > this._casillasMin ? Fórmulas.getRandomValor(this._casillasMin, this._casillasMax) : this._casillasMin;
                int idObjModelo = Constantes.getObjetoPorTrabajo(this._idTrabajoMod, especial);
                Objeto.ObjetoModelo OM = Mundo.getObjModelo(idObjModelo);
                if (OM != null) {
                    if (cant > 0) {
                        Objeto objeto = OM.crearObjDesdeModelo(cant, false);
                        if (!perso.addObjetoSimilar(objeto, true, -1)) {
                            Mundo.addObjeto(objeto, true);
                            perso.addObjetoPut(objeto);
                            SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, objeto);
                        }
                        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
                    }
                    SocketManager.ENVIAR_IQ_NUMERO_ARRIBA_PJ(perso, perso.getID(), cant);
                } else {
                    System.out.println("el idTrabajoMod " + this._idTrabajoMod + " no tiene objeto para recolectar");
                }
            }
        }

        public void iniciarAccionTrabajo(Personaje perso, Mapa.ObjetoInteractivo OI, GameThread.AccionDeJuego AJ, Mapa.Celda celda) {
            this._artesano = perso;
            if (this._esReceta) {
                perso.setOcupado(true);
                perso.setHaciendoTrabajo(this);
                SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(perso, 3, String.valueOf(this._casillasMax) + ";" + this._idTrabajoMod);
                SocketManager.ENVIAR_GDF_FORZADO_PERSONAJE(perso, celda.getID(), 2, 1);
            } else {
                OI.setInteractivo(false);
                OI.setEstado(2);
                SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(perso.getMapa(), "" + AJ._idUnica, 501, String.valueOf(perso.getID()), String.valueOf(celda.getID()) + "," + this._tiempo);
                SocketManager.ENVIAR_GDF_ESTADO_OBJETO_INTERACTIVO(perso.getMapa(), celda);
                this._iniciarTiempo = System.currentTimeMillis() + (long)this._tiempo;
            }
        }

        public int getIDTrabajo() {
            return this._idTrabajoMod;
        }

        public int getCasillasMin() {
            return this._casillasMin;
        }

        public int getCasillasMax() {
            return this._casillasMax;
        }

        public int getXpGanada() {
            return this._xpGanada;
        }

        public int getSuerte() {
            return this._suerte;
        }

        public int getTiempo() {
            return this._tiempo;
        }

        public boolean esReceta() {
            return this._esReceta;
        }

        public void modificarIngrediente(PrintWriter out, int id, int cant) {
            int c = this._ingredientes.get(id) == null ? 0 : this._ingredientes.get(id);
            this._ingredientes.remove(id);
            if ((c += cant) > 0) {
                this._ingredientes.put(id, c);
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(out, 'O', "+", String.valueOf(id) + "|" + c);
            } else {
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(out, 'O', "-", String.valueOf(id));
            }
        }

        public void iniciarReceta() {
            if (!this._esReceta) {
                return;
            }
            boolean firmado = false;
            if (this._idTrabajoMod == 1 || this._idTrabajoMod == 113 || this._idTrabajoMod == 115 || this._idTrabajoMod == 116 || this._idTrabajoMod == 117 || this._idTrabajoMod == 118 || this._idTrabajoMod == 119 || this._idTrabajoMod == 120 || this._idTrabajoMod >= 163 && this._idTrabajoMod <= 169) {
                this.recetaForjaMagia();
                return;
            }
            try {
                PrintWriter out = this._artesano.getCuenta().getEntradaPersonaje().getOut();
                TreeMap<Integer, Integer> ingredPorModelo = new TreeMap<Integer, Integer>();
                for (Map.Entry<Integer, Integer> ingrediente : this._ingredientes.entrySet()) {
                    int nuevaCant;
                    int idObjeto = ingrediente.getKey();
                    int cantObjeto = ingrediente.getValue();
                    if (!this._artesano.tieneObjetoID(idObjeto)) {
                        SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EI");
                        return;
                    }
                    Objeto obj = Mundo.getObjeto(idObjeto);
                    if (obj == null || obj.getCantidad() < cantObjeto) {
                        SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EI");
                        return;
                    }
                    if (this._primero) {
                        SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(out, 'O', "+", String.valueOf(idObjeto) + "|" + cantObjeto);
                    }
                    if ((nuevaCant = obj.getCantidad() - cantObjeto) == 0) {
                        this._artesano.borrarObjetoSinEliminar(idObjeto);
                        Mundo.eliminarObjeto(idObjeto);
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._artesano, idObjeto);
                    } else {
                        obj.setCantidad(nuevaCant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._artesano, obj);
                    }
                    ingredPorModelo.put(obj.getModelo().getID(), cantObjeto);
                }
                this._primero = true;
                if (ingredPorModelo.containsKey(7508)) {
                    firmado = true;
                }
                ingredPorModelo.remove(7508);
                SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._artesano);
                StatsOficio SO = this._artesano.getOficioPorTrabajo(this._idTrabajoMod);
                int idReceta = Mundo.getIDRecetaPorIngredientes(SO.getOficio().listaRecetaPorTrabajo(this._idTrabajoMod), ingredPorModelo);
                if (idReceta == -1 || !SO.getOficio().puedeReceta(this._idTrabajoMod, idReceta)) {
                    SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EI");
                    SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-");
                    this._ingredientes.clear();
                    return;
                }
                int suerte = Constantes.getSuertePorNroCasillaYNivel(SO.getNivel(), this._ingredientes.size());
                int jet = Fórmulas.getRandomValor(1, 100);
                boolean exito = (suerte >= jet);
                if (!exito) {
                    SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EF");
                    SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-" + idReceta);
                    SocketManager.ENVIAR_Im_INFORMACION(this._artesano, "0118");
                } else {
                    Objeto objCreado = Mundo.getObjModelo(idReceta).crearObjDesdeModelo(1, false);
                    if (firmado) {
                        objCreado.addTextoStat(988, this._artesano.getNombre());
                    }
                    Objeto igual = null;
                    igual = this._artesano.getObjSimilarInventario(objCreado);
                    if (igual == null) {
                        Mundo.addObjeto(objCreado, true);
                        this._artesano.addObjetoPut(objCreado);
                        SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._artesano, objCreado);
                    } else {
                        igual.setCantidad(igual.getCantidad() + 1);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._artesano, igual);
                        objCreado = igual;
                    }
                    SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._artesano);
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._artesano, 'O', "+", objCreado.stringObjetoConPalo(1));
                    SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "+" + idReceta);
                    SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "K;" + idReceta);
                }
                int xpGanada = (int)((float)Constantes.calculXpGanadaEnOficio(SO.getNivel(), this._ingredientes.size()) * LesGuardians.RATE_XP_PROF);
                SO.addXP(this._artesano, xpGanada);
                ArrayList<StatsOficio> statsOficios = new ArrayList<StatsOficio>();
                statsOficios.add(SO);
                SocketManager.ENVIAR_JX_EXPERINENCIA_OFICIO(this._artesano, statsOficios);
                this._ultimoTrabajo.clear();
                this._ultimoTrabajo.putAll(this._ingredientes);
                this._ingredientes.clear();
                try {
                    Thread.sleep(200L);
                }
                catch (Exception exception) {}
            }
            catch (Exception e) {
                return;
            }
        }

        private void recetaForjaMagia() {
            boolean firmado = false;
            Objeto objAMaguear = null;
            Objeto objRunaFirma = null;
            Objeto objModificador = null;
            int nivelRunaElemento = 0;
            int statAgre = -1;
            int nivelRunaStats = 0;
            int agregar = 0;
            int idABorrar = -1;
            int runa = 0;
            boolean vip = false;
            String statAMaguear = "-1";
            for (int idIngrediente : this._ingredientes.keySet()) {
                Objeto ing = Mundo.getObjeto(idIngrediente);
                if (ing == null || !this._artesano.tieneObjetoID(idIngrediente)) {
                    SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EI");
                    SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-");
                    this._ingredientes.clear();
                    return;
                }
                int idModelo = ing.getModelo().getID();
                switch (idModelo) {
                    case 1333: {
                        statAgre = 99;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1335: {
                        statAgre = 96;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1337: {
                        statAgre = 98;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1338: {
                        statAgre = 97;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1340: {
                        statAgre = 97;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1341: {
                        statAgre = 96;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1342: {
                        statAgre = 98;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1343: {
                        statAgre = 99;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1345: {
                        statAgre = 99;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1346: {
                        statAgre = 96;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1347: {
                        statAgre = 98;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1348: {
                        statAgre = 97;
                        nivelRunaElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1519: {
                        objModificador = ing;
                        statAMaguear = "76";
                        agregar = 1;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1521: {
                        objModificador = ing;
                        statAMaguear = "7c";
                        agregar = 1;
                        runa = 6;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1522: {
                        objModificador = ing;
                        statAMaguear = "7e";
                        agregar = 1;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1523: {
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 3;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1524: {
                        objModificador = ing;
                        statAMaguear = "77";
                        agregar = 1;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1525: {
                        objModificador = ing;
                        statAMaguear = "7b";
                        agregar = 1;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1545: {
                        objModificador = ing;
                        statAMaguear = "76";
                        agregar = 3;
                        runa = 3;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1546: {
                        objModificador = ing;
                        statAMaguear = "7c";
                        agregar = 3;
                        runa = 18;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1547: {
                        objModificador = ing;
                        statAMaguear = "7e";
                        agregar = 3;
                        runa = 3;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1548: {
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 10;
                        runa = 10;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1549: {
                        objModificador = ing;
                        statAMaguear = "77";
                        agregar = 3;
                        runa = 3;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1550: {
                        objModificador = ing;
                        statAMaguear = "7b";
                        agregar = 3;
                        runa = 10;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1551: {
                        objModificador = ing;
                        statAMaguear = "76";
                        agregar = 10;
                        runa = 10;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1552: {
                        objModificador = ing;
                        statAMaguear = "7c";
                        agregar = 10;
                        runa = 50;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1553: {
                        objModificador = ing;
                        statAMaguear = "7e";
                        agregar = 10;
                        runa = 10;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1554: {
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 30;
                        runa = 10;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1555: {
                        objModificador = ing;
                        statAMaguear = "77";
                        agregar = 10;
                        runa = 10;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1556: {
                        objModificador = ing;
                        statAMaguear = "7b";
                        agregar = 10;
                        runa = 10;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1557: {
                        objModificador = ing;
                        statAMaguear = "6f";
                        agregar = 1;
                        runa = 100;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1558: {
                        objModificador = ing;
                        statAMaguear = "80";
                        agregar = 1;
                        runa = 90;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7433: {
                        objModificador = ing;
                        statAMaguear = "73";
                        agregar = 1;
                        runa = 30;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7434: {
                        objModificador = ing;
                        statAMaguear = "b2";
                        agregar = 1;
                        runa = 20;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7435: {
                        objModificador = ing;
                        statAMaguear = "70";
                        agregar = 1;
                        runa = 20;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7436: {
                        objModificador = ing;
                        statAMaguear = "8a";
                        agregar = 1;
                        runa = 2;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7437: {
                        objModificador = ing;
                        statAMaguear = "dc";
                        agregar = 1;
                        runa = 2;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7438: {
                        objModificador = ing;
                        statAMaguear = "75";
                        agregar = 1;
                        runa = 50;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7442: {
                        objModificador = ing;
                        statAMaguear = "b6";
                        agregar = 1;
                        runa = 30;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7443: {
                        objModificador = ing;
                        statAMaguear = "9e";
                        agregar = 10;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7444: {
                        objModificador = ing;
                        statAMaguear = "9e";
                        agregar = 30;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7445: {
                        objModificador = ing;
                        statAMaguear = "9e";
                        agregar = 100;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7446: {
                        objModificador = ing;
                        statAMaguear = "e1";
                        agregar = 1;
                        runa = 15;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7447: {
                        objModificador = ing;
                        statAMaguear = "e2";
                        agregar = 1;
                        runa = 2;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7448: {
                        objModificador = ing;
                        statAMaguear = "ae";
                        agregar = 10;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7449: {
                        objModificador = ing;
                        statAMaguear = "ae";
                        agregar = 30;
                        runa = 3;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7450: {
                        objModificador = ing;
                        statAMaguear = "ae";
                        agregar = 100;
                        runa = 10;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7451: {
                        objModificador = ing;
                        statAMaguear = "b0";
                        agregar = 1;
                        runa = 5;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7452: {
                        objModificador = ing;
                        statAMaguear = "f3";
                        agregar = 1;
                        runa = 4;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7453: {
                        objModificador = ing;
                        statAMaguear = "f2";
                        agregar = 1;
                        runa = 4;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7454: {
                        objModificador = ing;
                        statAMaguear = "f1";
                        agregar = 1;
                        runa = 4;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7455: {
                        objModificador = ing;
                        statAMaguear = "f0";
                        agregar = 1;
                        runa = 4;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7456: {
                        objModificador = ing;
                        statAMaguear = "f4";
                        agregar = 1;
                        runa = 4;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7457: {
                        objModificador = ing;
                        statAMaguear = "d5";
                        agregar = 1;
                        runa = 5;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7458: {
                        objModificador = ing;
                        statAMaguear = "d4";
                        agregar = 1;
                        runa = 5;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7459: {
                        objModificador = ing;
                        statAMaguear = "d2";
                        agregar = 1;
                        runa = 5;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7460: {
                        objModificador = ing;
                        statAMaguear = "d6";
                        agregar = 1;
                        runa = 5;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7560: {
                        objModificador = ing;
                        statAMaguear = "d3";
                        agregar = 1;
                        runa = 5;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 8379: {
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 10;
                        runa = 10;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 10662: {
                        objModificador = ing;
                        statAMaguear = "b0";
                        agregar = 3;
                        runa = 15;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7508: {
                        firmado = true;
                        objRunaFirma = ing;
                        break;
                    }
                    case 11118: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "76";
                        agregar = 15;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11119: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "7c";
                        agregar = 15;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11120: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "7e";
                        agregar = 15;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11121: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 45;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11122: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "77";
                        agregar = 15;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11123: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "7b";
                        agregar = 15;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11124: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "b0";
                        agregar = 10;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11125: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "73";
                        agregar = 3;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11126: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "b2";
                        agregar = 5;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11127: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "70";
                        agregar = 5;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11128: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "8a";
                        agregar = 10;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11129: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "dc";
                        agregar = 5;
                        runa = 1;
                        nivelRunaStats = ing.getModelo().getNivel();
                        break;
                    }
                    default: {
                        int tipo = ing.getModelo().getTipo();
                        if (!(tipo >= 1 && tipo <= 11 || tipo >= 16 && tipo <= 22 || tipo == 81 || tipo == 102 || tipo == 114) && ing.getModelo().getCostePA() <= 0) continue;
                        objAMaguear = ing;
                        SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._artesano.getCuenta().getEntradaPersonaje().getOut(), 'O', "+", String.valueOf(objAMaguear.getID()) + "|" + 1);
                        idABorrar = idIngrediente;
                        Objeto nuevoObj = Objeto.clonarObjeto(objAMaguear, 1);
                        if (objAMaguear.getCantidad() > 1) {
                            int nuevaCant = objAMaguear.getCantidad() - 1;
                            objAMaguear.setCantidad(nuevaCant);
                            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._artesano, objAMaguear);
                            break;
                        }
                        Mundo.eliminarObjeto(idIngrediente);
                        this._artesano.borrarObjetoSinEliminar(idIngrediente);
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._artesano, idIngrediente);
                        objAMaguear = nuevoObj;
                    }
                }
            }
            StatsOficio oficio = this._artesano.getOficioPorTrabajo(this._idTrabajoMod);
            oficio.addXP(this._artesano, (int)((double)LesGuardians.RATE_XP_PROF + 0.9) * 10);
            if (oficio == null || objAMaguear == null || objModificador == null) {
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EI");
                SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-");
                this._ingredientes.clear();
                return;
            }
            if (idABorrar != -1) {
                this._ingredientes.remove(idABorrar);
            }
            Objeto.ObjetoModelo objModelo = objAMaguear.getModelo();
            int suerte = 0;
            int nivelOficio = oficio.getNivel();
            int objModeloID = objModelo.getID();
            String statStringObj = objAMaguear.convertirStatsAString();
            if (nivelRunaElemento > 0 && nivelRunaStats == 0) {
                suerte = Fórmulas.calculoPorcCambioElenemto(nivelOficio, objModelo.getNivel(), nivelRunaElemento);
                if (suerte > 100 - nivelOficio / 20) {
                    suerte = 100 - nivelOficio / 20;
                }
                if (suerte < nivelOficio / 20) {
                    suerte = nivelOficio / 20;
                }
            } else if (nivelRunaStats > 0 && nivelRunaElemento == 0) {
                int pesoTotalBase;
                int pesoTotalActual = 1;
                int pesoActualStat = 1;
                if (!statStringObj.isEmpty()) {
                    pesoTotalActual = Oficio.pesoTotalActualObj(statStringObj, objAMaguear);
                    pesoActualStat = Oficio.pesoStatActual(objAMaguear, statAMaguear);
                }
                if ((pesoTotalBase = Oficio.pesoTotalBaseObj(objModeloID)) < 0) {
                    pesoTotalBase = 0;
                }
                if (pesoActualStat < 0) {
                    pesoActualStat = 0;
                }
                if (pesoTotalActual < 0) {
                    pesoTotalActual = 0;
                }
                float coef = 1.0f;
                byte tieneStatBase = Oficio.tieneStatBaseObjeto(objAMaguear, statAMaguear);
                byte tieneStatActual = Oficio.tieneStatActualObjeto(objAMaguear, statAMaguear);
                if (tieneStatBase == 1 && tieneStatActual == 1 || tieneStatBase == 1 && tieneStatActual == 0) {
                    coef = 1.0f;
                } else if (tieneStatBase == 2 && tieneStatActual == 2) {
                    coef = 0.5f;
                } else if (tieneStatBase == 0 && tieneStatActual == 0 || tieneStatBase == 0 && tieneStatActual == 1) {
                    coef = 0.25f;
                }
                if (Objeto.getStatActual(objAMaguear, statAMaguear) >= Objeto.getStatBaseMax(objAMaguear.getModelo(), statAMaguear)) {
                    coef = 0.15f;
                }
                float tolerancia = vip ? _tolerVIP : _tolerNormal;
                int diferencia = (int)((float)pesoTotalBase * tolerancia) - pesoTotalActual;
                suerte = Fórmulas.suerteFM(pesoTotalBase, pesoTotalActual, pesoActualStat, runa, diferencia, coef);
                suerte += oficio.getAdicional();
                if (vip) {
                    suerte += 20;
                }
                if (suerte < 1) {
                    suerte = 1;
                } else if (suerte > 100) {
                    suerte = 100;
                }
            }
            int jet = Fórmulas.getRandomValor(1, 100);
            boolean exito = (suerte >= jet);
            if (!exito) {
                int romper = Fórmulas.getRandomValor(1, 100);
                if (objRunaFirma != null) {
                    int nuevaCant = objRunaFirma.getCantidad() - 1;
                    if (nuevaCant <= 0) {
                        this._artesano.borrarObjetoSinEliminar(objRunaFirma.getID());
                        Mundo.eliminarObjeto(objRunaFirma.getID());
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._artesano, objRunaFirma.getID());
                    } else {
                        objRunaFirma.setCantidad(nuevaCant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._artesano, objRunaFirma);
                    }
                }
                if (objModificador != null) {
                    int nuevaCant = objModificador.getCantidad() - 1;
                    if (nuevaCant <= 0) {
                        this._artesano.borrarObjetoSinEliminar(objModificador.getID());
                        Mundo.eliminarObjeto(objModificador.getID());
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._artesano, objModificador.getID());
                    } else {
                        objModificador.setCantidad(nuevaCant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._artesano, objModificador);
                    }
                }
                if (romper == 100) {
                    this._romperse = true;
                } else {
                    Mundo.addObjeto(objAMaguear, false);
                    this._artesano.addObjetoPut(objAMaguear);
                    if (!statStringObj.isEmpty()) {
                        String statsStr = objAMaguear.stringStatsFCForja(objAMaguear, runa);
                        objAMaguear.clearTodo();
                        objAMaguear.convertirStringAStats(statsStr);
                    }
                    SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._artesano, objAMaguear);
                    SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._artesano);
                    String datos = objAMaguear.stringObjetoConPalo(1);
                    if (this._veces != 0 || this._interrumpir) {
                        SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._artesano, 'O', "+", datos);
                    }
                    this._datos = datos;
                }
                SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-" + objModeloID);
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EF");
                SocketManager.ENVIAR_Im_INFORMACION(this._artesano, "0183");
                if (this._romperse) {
                    SocketManager.ENVIAR_Im_INFORMACION(this._artesano, "0CRAFT_LOOP_END_INVALID");
                }
            } else {
                int coef = 0;
                if (nivelRunaElemento == 1) {
                    coef = 50;
                } else if (nivelRunaElemento == 25) {
                    coef = 65;
                } else if (nivelRunaElemento == 50) {
                    coef = 85;
                }
                if (firmado) {
                    objAMaguear.addTextoStat(985, this._artesano.getNombre());
                }
                if (nivelRunaElemento > 0 && nivelRunaStats == 0) {
                    for (EfectoHechizo EH : objAMaguear.getEfectos()) {
                        if (EH.getEfectoID() != 100) continue;
                        String[] infos = EH.getArgs().split(";");
                        try {
                            int min = Integer.parseInt(infos[0], 16);
                            int max = Integer.parseInt(infos[1], 16);
                            int nuevoMin = min * coef / 100;
                            int nuevoMax = max * coef / 100;
                            if (nuevoMin == 0) {
                                nuevoMin = 1;
                            }
                            String nuevoRango = "1d" + (nuevoMax - nuevoMin + 1) + "+" + (nuevoMin - 1);
                            String nuevosArgs = String.valueOf(Integer.toHexString(nuevoMin)) + ";" + Integer.toHexString(nuevoMax) + ";-1;-1;0;" + nuevoRango;
                            EH.setArgs(nuevosArgs);
                            EH.setEfectoID(statAgre);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (nivelRunaStats > 0 && nivelRunaElemento == 0) {
                    boolean negativo = false;
                    byte actualStat = Oficio.tieneStatActualObjeto(objAMaguear, statAMaguear);
                    if (actualStat == 2) {
                        if (statAMaguear.compareTo("7b") == 0) {
                            statAMaguear = "98";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("77") == 0) {
                            statAMaguear = "9a";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("7e") == 0) {
                            statAMaguear = "9b";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("76") == 0) {
                            statAMaguear = "9d";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("7c") == 0) {
                            statAMaguear = "9c";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("7d") == 0) {
                            statAMaguear = "99";
                            negativo = true;
                        }
                    }
                    if (actualStat == 1 || actualStat == 2) {
                        String statsStr = objAMaguear.convertirStatsAStringFM(statAMaguear, objAMaguear, agregar, negativo);
                        objAMaguear.clearTodo();
                        objAMaguear.convertirStringAStats(statsStr);
                    } else if (statStringObj.isEmpty()) {
                        String statsStr = String.valueOf(statAMaguear) + "#" + Integer.toHexString(agregar) + "#0#0#0d0+" + agregar;
                        objAMaguear.clearTodo();
                        objAMaguear.convertirStringAStats(statsStr);
                    } else {
                        String statsStr = String.valueOf(objAMaguear.convertirStatsAStringFM(statAMaguear, objAMaguear, agregar, negativo)) + "," + statAMaguear + "#" + Integer.toHexString(agregar) + "#0#0#0d0+" + agregar;
                        objAMaguear.clearTodo();
                        objAMaguear.convertirStringAStats(statsStr);
                    }
                }
                if (objRunaFirma != null) {
                    int nuevaCant = objRunaFirma.getCantidad() - 1;
                    if (nuevaCant <= 0) {
                        this._artesano.borrarObjetoSinEliminar(objRunaFirma.getID());
                        Mundo.eliminarObjeto(objRunaFirma.getID());
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._artesano, objRunaFirma.getID());
                    } else {
                        objRunaFirma.setCantidad(nuevaCant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._artesano, objRunaFirma);
                    }
                }
                if (objModificador != null) {
                    int nuevaCant = objModificador.getCantidad() - 1;
                    if (nuevaCant <= 0) {
                        this._artesano.borrarObjetoSinEliminar(objModificador.getID());
                        Mundo.eliminarObjeto(objModificador.getID());
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._artesano, objModificador.getID());
                    } else {
                        objModificador.setCantidad(nuevaCant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._artesano, objModificador);
                    }
                }
                Mundo.addObjeto(objAMaguear, false);
                this._artesano.addObjetoPut(objAMaguear);
                SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._artesano);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._artesano, objAMaguear);
                String datos = objAMaguear.stringObjetoConPalo(1);
                if (this._veces != 0 || this._interrumpir) {
                    SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._artesano, 'O', "+", datos);
                }
                this._datos = datos;
                SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "+" + objModeloID);
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "K;" + objModeloID);
            }
            this._ultimoTrabajo.clear();
            this._ultimoTrabajo.putAll(this._ingredientes);
            this._ultimoTrabajo.put(objAMaguear.getID(), 1);
            this._ingredientes.clear();
            try {
                Thread.sleep(200L);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        public boolean iniciarTrabajoPago(Personaje artesano, Personaje cliente, ArrayList<Mundo.Duo<Integer, Integer>> objArtesano, ArrayList<Mundo.Duo<Integer, Integer>> objCliente) {
            if (!this._esReceta) {
                return false;
            }
            this._artesano = artesano;
            this._cliente = cliente;
            boolean firmado = false;
            for (Mundo.Duo<Integer, Integer> duo : objArtesano) {
                this._ingredientes.put((Integer)duo._primero, (Integer)duo._segundo);
            }
            for (Mundo.Duo<Integer, Integer> duo : objCliente) {
                this._ingredientes.put((Integer)duo._primero, (Integer)duo._segundo);
            }
            if (this._idTrabajoMod == 1 || this._idTrabajoMod == 113 || this._idTrabajoMod == 115 || this._idTrabajoMod == 116 || this._idTrabajoMod == 117 || this._idTrabajoMod == 118 || this._idTrabajoMod == 119 || this._idTrabajoMod == 120 || this._idTrabajoMod >= 163 && this._idTrabajoMod <= 169) {
                boolean resultado = this.trabajoPagoFM();
                return resultado;
            }
            TreeMap<Integer, Integer> ingredPorModelo = new TreeMap<Integer, Integer>();
            for (Map.Entry<Integer, Integer> ingrediente : this._ingredientes.entrySet()) {
                int idObjeto = ingrediente.getKey();
                int cantObjeto = ingrediente.getValue();
                Objeto obj = Mundo.getObjeto(idObjeto);
                if (obj == null || obj.getCantidad() < cantObjeto) {
                    SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EI");
                    SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._cliente, "EI");
                    return false;
                }
                int nuevaCant = obj.getCantidad() - cantObjeto;
                if (nuevaCant == 0) {
                    if (this._artesano.tieneObjetoID(idObjeto)) {
                        this._artesano.borrarObjetoSinEliminar(idObjeto);
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._artesano, idObjeto);
                    } else {
                        this._cliente.borrarObjetoSinEliminar(idObjeto);
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._cliente, idObjeto);
                    }
                    Mundo.eliminarObjeto(idObjeto);
                } else {
                    obj.setCantidad(nuevaCant);
                    if (this._artesano.tieneObjetoID(idObjeto)) {
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._artesano, obj);
                    } else {
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._cliente, obj);
                    }
                }
                int idModelo = obj.getIDModelo();
                if (ingredPorModelo.get(idModelo) == null) {
                    ingredPorModelo.put(idModelo, cantObjeto);
                    continue;
                }
                int nueva = (Integer)ingredPorModelo.get(idModelo) + cantObjeto;
                ingredPorModelo.remove(idModelo);
                ingredPorModelo.put(idModelo, nueva);
            }
            if (ingredPorModelo.containsKey(7508)) {
                firmado = true;
            }
            ingredPorModelo.remove(7508);
            SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._artesano);
            SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._cliente);
            StatsOficio SO = this._artesano.getOficioPorTrabajo(this._idTrabajoMod);
            int idReceta = Mundo.getIDRecetaPorIngredientes(SO.getOficio().listaRecetaPorTrabajo(this._idTrabajoMod), ingredPorModelo);
            if (idReceta == -1 || !SO.getOficio().puedeReceta(this._idTrabajoMod, idReceta)) {
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EI");
                SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-");
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._cliente, "EI");
                this._ingredientes.clear();
                return false;
            }
            int suerte = Constantes.getSuertePorNroCasillaYNivel(SO.getNivel(), _ingredientes.size());
            int jet = Fórmulas.getRandomValor(1, 100);
            boolean exito = (suerte >= jet);
            if (!exito) {
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EF");
                SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-" + idReceta);
                SocketManager.ENVIAR_Im_INFORMACION(this._artesano, "0118");
                SocketManager.ENVIAR_Im_INFORMACION(this._cliente, "0118");
            } else {
                Objeto nuevoObj = Mundo.getObjModelo(idReceta).crearObjDesdeModelo(1, false);
                if (firmado) {
                    nuevoObj.addTextoStat(988, this._artesano.getNombre());
                }
                Objeto igual = null;
                igual = this._cliente.getObjSimilarInventario(nuevoObj);
                if (igual == null) {
                    Mundo.addObjeto(nuevoObj, true);
                    this._cliente.addObjetoPut(nuevoObj);
                    SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._cliente, nuevoObj);
                } else {
                    igual.setCantidad(igual.getCantidad() + 1);
                    SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._cliente, igual);
                    nuevoObj = igual;
                }
                SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._cliente);
                String statsNuevoObj = nuevoObj.convertirStatsAString();
                String todaInfo = nuevoObj.stringObjetoConPalo(1);
                SocketManager.ENVIAR_ErK_RESULTADO_TRABAJO(this._artesano, "O", "+", todaInfo);
                SocketManager.ENVIAR_ErK_RESULTADO_TRABAJO(this._cliente, "O", "+", todaInfo);
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "K;" + idReceta + ";T" + this._cliente.getNombre() + ";" + statsNuevoObj);
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._cliente, "K;" + idReceta + ";B" + this._artesano.getNombre() + ";" + statsNuevoObj);
                SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "+" + idReceta);
            }
            int xpGanada = (int)((float)Constantes.calculXpGanadaEnOficio(SO.getNivel(), this._ingredientes.size()) * LesGuardians.RATE_XP_PROF);
            SO.addXP(this._artesano, xpGanada);
            ArrayList<StatsOficio> statsOficios = new ArrayList<StatsOficio>();
            statsOficios.add(SO);
            SocketManager.ENVIAR_JX_EXPERINENCIA_OFICIO(this._artesano, statsOficios);
            this._ingredientes.clear();
            return exito;
        }

        private boolean trabajoPagoFM() {
            boolean firmado = false;
            Objeto objAMaguear = null;
            Objeto objRunaFirma = null;
            Objeto objModificador = null;
            int esCambioElemento = 0;
            int statAgre = -1;
            int esCambiadoStats = 0;
            int agregar = 0;
            int idABorrar = -1;
            int runa = 0;
            boolean vip = false;
            String statAMaguear = "-1";
            block77: for (int idIngrediente : this._ingredientes.keySet()) {
                Objeto ing = Mundo.getObjeto(idIngrediente);
                if (ing == null) {
                    SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EI");
                    SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._cliente, "EI");
                    SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-");
                    this._ingredientes.clear();
                    return false;
                }
                int idModelo = ing.getModelo().getID();
                switch (idModelo) {
                    case 1333: {
                        statAgre = 99;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1335: {
                        statAgre = 96;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1337: {
                        statAgre = 98;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1338: {
                        statAgre = 97;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1340: {
                        statAgre = 97;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1341: {
                        statAgre = 96;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1342: {
                        statAgre = 98;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1343: {
                        statAgre = 99;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1345: {
                        statAgre = 99;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1346: {
                        statAgre = 96;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1347: {
                        statAgre = 98;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1348: {
                        statAgre = 97;
                        esCambioElemento = ing.getModelo().getNivel();
                        objModificador = ing;
                        break;
                    }
                    case 1519: {
                        objModificador = ing;
                        statAMaguear = "76";
                        agregar = 1;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1521: {
                        objModificador = ing;
                        statAMaguear = "7c";
                        agregar = 1;
                        runa = 6;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1522: {
                        objModificador = ing;
                        statAMaguear = "7e";
                        agregar = 1;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1523: {
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 3;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1524: {
                        objModificador = ing;
                        statAMaguear = "77";
                        agregar = 1;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1525: {
                        objModificador = ing;
                        statAMaguear = "7b";
                        agregar = 1;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1545: {
                        objModificador = ing;
                        statAMaguear = "76";
                        agregar = 3;
                        runa = 3;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1546: {
                        objModificador = ing;
                        statAMaguear = "7c";
                        agregar = 3;
                        runa = 18;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1547: {
                        objModificador = ing;
                        statAMaguear = "7e";
                        agregar = 3;
                        runa = 3;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1548: {
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 10;
                        runa = 10;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1549: {
                        objModificador = ing;
                        statAMaguear = "77";
                        agregar = 3;
                        runa = 3;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1550: {
                        objModificador = ing;
                        statAMaguear = "7b";
                        agregar = 3;
                        runa = 10;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1551: {
                        objModificador = ing;
                        statAMaguear = "76";
                        agregar = 10;
                        runa = 10;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1552: {
                        objModificador = ing;
                        statAMaguear = "7c";
                        agregar = 10;
                        runa = 50;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1553: {
                        objModificador = ing;
                        statAMaguear = "7e";
                        agregar = 10;
                        runa = 10;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1554: {
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 30;
                        runa = 10;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1555: {
                        objModificador = ing;
                        statAMaguear = "77";
                        agregar = 10;
                        runa = 10;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1556: {
                        objModificador = ing;
                        statAMaguear = "7b";
                        agregar = 10;
                        runa = 10;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1557: {
                        objModificador = ing;
                        statAMaguear = "6f";
                        agregar = 1;
                        runa = 100;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 1558: {
                        objModificador = ing;
                        statAMaguear = "80";
                        agregar = 1;
                        runa = 90;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7433: {
                        objModificador = ing;
                        statAMaguear = "73";
                        agregar = 1;
                        runa = 30;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7434: {
                        objModificador = ing;
                        statAMaguear = "b2";
                        agregar = 1;
                        runa = 20;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7435: {
                        objModificador = ing;
                        statAMaguear = "70";
                        agregar = 1;
                        runa = 20;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7436: {
                        objModificador = ing;
                        statAMaguear = "8a";
                        agregar = 1;
                        runa = 2;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7437: {
                        objModificador = ing;
                        statAMaguear = "dc";
                        agregar = 1;
                        runa = 2;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7438: {
                        objModificador = ing;
                        statAMaguear = "75";
                        agregar = 1;
                        runa = 50;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7442: {
                        objModificador = ing;
                        statAMaguear = "b6";
                        agregar = 1;
                        runa = 30;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7443: {
                        objModificador = ing;
                        statAMaguear = "9e";
                        agregar = 10;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7444: {
                        objModificador = ing;
                        statAMaguear = "9e";
                        agregar = 30;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7445: {
                        objModificador = ing;
                        statAMaguear = "9e";
                        agregar = 100;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7446: {
                        objModificador = ing;
                        statAMaguear = "e1";
                        agregar = 1;
                        runa = 15;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7447: {
                        objModificador = ing;
                        statAMaguear = "e2";
                        agregar = 1;
                        runa = 2;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7448: {
                        objModificador = ing;
                        statAMaguear = "ae";
                        agregar = 10;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7449: {
                        objModificador = ing;
                        statAMaguear = "ae";
                        agregar = 30;
                        runa = 3;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7450: {
                        objModificador = ing;
                        statAMaguear = "ae";
                        agregar = 100;
                        runa = 10;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7451: {
                        objModificador = ing;
                        statAMaguear = "b0";
                        agregar = 1;
                        runa = 5;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7452: {
                        objModificador = ing;
                        statAMaguear = "f3";
                        agregar = 1;
                        runa = 4;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7453: {
                        objModificador = ing;
                        statAMaguear = "f2";
                        agregar = 1;
                        runa = 4;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7454: {
                        objModificador = ing;
                        statAMaguear = "f1";
                        agregar = 1;
                        runa = 4;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7455: {
                        objModificador = ing;
                        statAMaguear = "f0";
                        agregar = 1;
                        runa = 4;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7456: {
                        objModificador = ing;
                        statAMaguear = "f4";
                        agregar = 1;
                        runa = 4;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7457: {
                        objModificador = ing;
                        statAMaguear = "d5";
                        agregar = 1;
                        runa = 5;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7458: {
                        objModificador = ing;
                        statAMaguear = "d4";
                        agregar = 1;
                        runa = 5;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7459: {
                        objModificador = ing;
                        statAMaguear = "d2";
                        agregar = 1;
                        runa = 5;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7460: {
                        objModificador = ing;
                        statAMaguear = "d6";
                        agregar = 1;
                        runa = 5;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7560: {
                        objModificador = ing;
                        statAMaguear = "d3";
                        agregar = 1;
                        runa = 5;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 8379: {
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 10;
                        runa = 10;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 10662: {
                        objModificador = ing;
                        statAMaguear = "b0";
                        agregar = 3;
                        runa = 15;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 7508: {
                        firmado = true;
                        objRunaFirma = ing;
                        break;
                    }
                    case 11118: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "76";
                        agregar = 15;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11119: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "7c";
                        agregar = 15;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11120: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "7e";
                        agregar = 15;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11121: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "7d";
                        agregar = 45;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11122: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "77";
                        agregar = 15;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11123: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "7b";
                        agregar = 15;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11124: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "b0";
                        agregar = 10;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11125: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "73";
                        agregar = 3;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11126: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "b2";
                        agregar = 5;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11127: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "70";
                        agregar = 5;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11128: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "8a";
                        agregar = 10;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    case 11129: {
                        vip = true;
                        objModificador = ing;
                        statAMaguear = "dc";
                        agregar = 5;
                        runa = 1;
                        esCambiadoStats = ing.getModelo().getNivel();
                        break;
                    }
                    default: {
                        int tipo = ing.getModelo().getTipo();
                        if (!(tipo >= 1 && tipo <= 11 || tipo >= 16 && tipo <= 22 || tipo == 81 || tipo == 102 || tipo == 114) && ing.getModelo().getCostePA() <= 0 || (objAMaguear = ing).getCantidad() <= 1) continue block77;
                        Personaje modificado = this._artesano.tieneObjetoID(idIngrediente) ? this._artesano : this._cliente;
                        int nuevaCant = objAMaguear.getCantidad() - 1;
                        Objeto nuevoObj = Objeto.clonarObjeto(objAMaguear, nuevaCant);
                        objAMaguear.setCantidad(1);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(modificado, objAMaguear);
                        Mundo.addObjeto(nuevoObj, true);
                        SocketManager.ENVIAR_OAKO_APARECER_OBJETO(modificado, nuevoObj);
                    }
                }
            }
            StatsOficio oficio = this._artesano.getOficioPorTrabajo(this._idTrabajoMod);
            oficio.addXP(this._artesano, (int)((double)LesGuardians.RATE_XP_PROF + 0.9) * 10);
            if (oficio == null || objAMaguear == null || objModificador == null) {
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._artesano, "EI");
                SocketManager.ENVIAR_Ec_INICIAR_RECETA(this._cliente, "EI");
                SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-");
                this._ingredientes.clear();
                return false;
            }
            if (idABorrar != -1) {
                this._ingredientes.remove(idABorrar);
            }
            Objeto.ObjetoModelo objModeloMaguear = objAMaguear.getModelo();
            int idObjMaguear = objAMaguear.getID();
            int suerte = 0;
            int nivelOficio = oficio.getNivel();
            int idObjModMaguear = objModeloMaguear.getID();
            String statStringObj = objAMaguear.convertirStatsAString();
            if (esCambioElemento > 0 && esCambiadoStats == 0) {
                suerte = Fórmulas.calculoPorcCambioElenemto(nivelOficio, objModeloMaguear.getNivel(), esCambioElemento);
                if (suerte > 100 - nivelOficio / 20) {
                    suerte = 100 - nivelOficio / 20;
                }
                if (suerte < nivelOficio / 20) {
                    suerte = nivelOficio / 20;
                }
            } else if (esCambiadoStats > 0 && esCambioElemento == 0) {
                int pesoTotalBase;
                int pesoTotalActual = 1;
                int pesoActualStat = 1;
                if (!statStringObj.isEmpty()) {
                    pesoTotalActual = Oficio.pesoTotalActualObj(statStringObj, objAMaguear);
                    pesoActualStat = Oficio.pesoStatActual(objAMaguear, statAMaguear);
                }
                if ((pesoTotalBase = Oficio.pesoTotalBaseObj(idObjModMaguear)) < 0) {
                    pesoTotalBase = 0;
                }
                if (pesoActualStat < 0) {
                    pesoActualStat = 0;
                }
                if (pesoTotalActual < 0) {
                    pesoTotalActual = 0;
                }
                float coef = 1.0f;
                byte tieneStatBase = Oficio.tieneStatBaseObjeto(objAMaguear, statAMaguear);
                byte tieneStatActual = Oficio.tieneStatActualObjeto(objAMaguear, statAMaguear);
                if (tieneStatBase == 1 && tieneStatActual == 1 || tieneStatBase == 1 && tieneStatActual == 0) {
                    coef = 1.0f;
                } else if (tieneStatBase == 2 && tieneStatActual == 2) {
                    coef = 0.5f;
                } else if (tieneStatBase == 0 && tieneStatActual == 0 || tieneStatBase == 0 && tieneStatActual == 1) {
                    coef = 0.25f;
                }
                if (Objeto.getStatActual(objAMaguear, statAMaguear) >= Objeto.getStatBaseMax(objModeloMaguear, statAMaguear)) {
                    coef = 0.25f;
                }
                float tolerancia = vip ? _tolerVIP : _tolerNormal;
                int diferencia = (int)((float)pesoTotalBase * tolerancia) - pesoTotalActual;
                suerte = Fórmulas.suerteFM(pesoTotalBase, pesoTotalActual, pesoActualStat, runa, diferencia, coef);
                suerte += oficio.getAdicional();
                if (vip) {
                    suerte += 20;
                }
                if (suerte < 1) {
                    suerte = 1;
                } else if (suerte > 100) {
                    suerte = 100;
                }
            }
            int jet = Fórmulas.getRandomValor(1, 100);
            boolean exito = (suerte >= jet);
            if (!exito) {
                if (objRunaFirma != null) {
                    Personaje modificado = this._artesano.tieneObjetoID(objRunaFirma.getID()) ? this._artesano : this._cliente;
                    int nuevaCant = objRunaFirma.getCantidad() - 1;
                    if (nuevaCant <= 0) {
                        modificado.borrarObjetoSinEliminar(objRunaFirma.getID());
                        Mundo.eliminarObjeto(objRunaFirma.getID());
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(modificado, objRunaFirma.getID());
                    } else {
                        objRunaFirma.setCantidad(nuevaCant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(this._artesano, objRunaFirma);
                    }
                }
                if (objModificador != null) {
                    Personaje modificado = this._artesano.tieneObjetoID(objModificador.getID()) ? this._artesano : this._cliente;
                    int nuevaCant = objModificador.getCantidad() - 1;
                    if (nuevaCant <= 0) {
                        modificado.borrarObjetoSinEliminar(objModificador.getID());
                        Mundo.eliminarObjeto(objModificador.getID());
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(modificado, objModificador.getID());
                    } else {
                        objModificador.setCantidad(nuevaCant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(modificado, objModificador);
                    }
                }
                int romper = Fórmulas.getRandomValor(1, 100);
                if (romper == 100) {
                  this._romperse = true;
                    Personaje modificado = this._artesano.tieneObjetoID(idObjMaguear) ? this._artesano : this._cliente;
                    Mundo.eliminarObjeto(idObjMaguear);
                    modificado.borrarObjetoSinEliminar(idObjMaguear);
                    SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(modificado, idObjMaguear);
                } else {
                    if (this._artesano.tieneObjetoID(idObjMaguear)) {
                        this._artesano.borrarObjetoSinEliminar(idObjMaguear);
                        this._cliente.addObjetoPut(objAMaguear);
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._artesano, idObjMaguear);
                        SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._cliente, objAMaguear);
                    }
                    if (!statStringObj.isEmpty()) {
                        String statsStr = objAMaguear.stringStatsFCForja(objAMaguear, runa);
                        objAMaguear.clearTodo();
                        objAMaguear.convertirStringAStats(statsStr);
                    }
                    SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._cliente);
                    SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(this._cliente, objAMaguear);
                    String todaInfo = objAMaguear.stringObjetoConPalo(1);
                    SocketManager.ENVIAR_ErK_RESULTADO_TRABAJO(this._artesano, "O", "+", todaInfo);
                    SocketManager.ENVIAR_ErK_RESULTADO_TRABAJO(this._cliente, "O", "+", todaInfo);
                }
                SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "-" + idObjModMaguear);
                SocketManager.ENVIAR_Im_INFORMACION(this._artesano, "0183");
                SocketManager.ENVIAR_Im_INFORMACION(this._cliente, "0183");
                if (this._romperse) {
                    SocketManager.ENVIAR_Im_INFORMACION(this._artesano, "0CRAFT_LOOP_END_INVALID");
                    SocketManager.ENVIAR_Im_INFORMACION(this._cliente, "0CRAFT_LOOP_END_INVALID");
                }
                this._artesano.setForjaEcK("EF");
                this._cliente.setForjaEcK("EF");
            } else {
                int coef = 0;
                if (esCambioElemento == 1) {
                    coef = 50;
                } else if (esCambioElemento == 25) {
                    coef = 65;
                } else if (esCambioElemento == 50) {
                    coef = 85;
                }
                if (firmado) {
                    objAMaguear.addTextoStat(985, this._artesano.getNombre());
                }
                if (esCambioElemento > 0 && esCambiadoStats == 0) {
                    for (EfectoHechizo EH : objAMaguear.getEfectos()) {
                        if (EH.getEfectoID() != 100) continue;
                        String[] infos = EH.getArgs().split(";");
                        try {
                            int min = Integer.parseInt(infos[0], 16);
                            int max = Integer.parseInt(infos[1], 16);
                            int nuevoMin = min * coef / 100;
                            int nuevoMax = max * coef / 100;
                            if (nuevoMin == 0) {
                                nuevoMin = 1;
                            }
                            String nuevoRango = "1d" + (nuevoMax - nuevoMin + 1) + "+" + (nuevoMin - 1);
                            String nuevosArgs = String.valueOf(Integer.toHexString(nuevoMin)) + ";" + Integer.toHexString(nuevoMax) + ";-1;-1;0;" + nuevoRango;
                            EH.setArgs(nuevosArgs);
                            EH.setEfectoID(statAgre);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (esCambiadoStats > 0 && esCambioElemento == 0) {
                    boolean negativo = false;
                    byte actualStat = Oficio.tieneStatActualObjeto(objAMaguear, statAMaguear);
                    if (actualStat == 2) {
                        if (statAMaguear.compareTo("7b") == 0) {
                            statAMaguear = "98";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("77") == 0) {
                            statAMaguear = "9a";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("7e") == 0) {
                            statAMaguear = "9b";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("76") == 0) {
                            statAMaguear = "9d";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("7c") == 0) {
                            statAMaguear = "9c";
                            negativo = true;
                        }
                        if (statAMaguear.compareTo("7d") == 0) {
                            statAMaguear = "99";
                            negativo = true;
                        }
                    }
                    if (actualStat == 1 || actualStat == 2) {
                        String statsStr = objAMaguear.convertirStatsAStringFM(statAMaguear, objAMaguear, agregar, negativo);
                        objAMaguear.clearTodo();
                        objAMaguear.convertirStringAStats(statsStr);
                    } else if (statStringObj.isEmpty()) {
                        String statsStr = String.valueOf(statAMaguear) + "#" + Integer.toHexString(agregar) + "#0#0#0d0+" + agregar;
                        objAMaguear.clearTodo();
                        objAMaguear.convertirStringAStats(statsStr);
                    } else {
                        String statsStr = String.valueOf(objAMaguear.convertirStatsAStringFM(statAMaguear, objAMaguear, agregar, negativo)) + "," + statAMaguear + "#" + Integer.toHexString(agregar) + "#0#0#0d0+" + agregar;
                        objAMaguear.clearTodo();
                        objAMaguear.convertirStringAStats(statsStr);
                    }
                }
                if (objRunaFirma != null) {
                    Personaje modificado = this._artesano.tieneObjetoID(objRunaFirma.getID()) ? this._artesano : this._cliente;
                    int nuevaCant = objRunaFirma.getCantidad() - 1;
                    if (nuevaCant <= 0) {
                        modificado.borrarObjetoSinEliminar(objRunaFirma.getID());
                        Mundo.eliminarObjeto(objRunaFirma.getID());
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(modificado, objRunaFirma.getID());
                    } else {
                        objRunaFirma.setCantidad(nuevaCant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(modificado, objRunaFirma);
                    }
                }
                if (objModificador != null) {
                    Personaje modificado = this._artesano.tieneObjetoID(objModificador.getID()) ? this._artesano : this._cliente;
                    int nuevaCant = objModificador.getCantidad() - 1;
                    if (nuevaCant <= 0) {
                        modificado.borrarObjetoSinEliminar(objModificador.getID());
                        Mundo.eliminarObjeto(objModificador.getID());
                        SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(modificado, objModificador.getID());
                    } else {
                        objModificador.setCantidad(nuevaCant);
                        SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(modificado, objModificador);
                    }
                }
                if (this._artesano.tieneObjetoID(idObjMaguear)) {
                    this._artesano.borrarObjetoSinEliminar(idObjMaguear);
                    this._cliente.addObjetoPut(objAMaguear);
                    SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(this._artesano, idObjMaguear);
                    SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._cliente, objAMaguear);
                }
                SocketManager.ENVIAR_Ow_PODS_DEL_PJ(this._cliente);
                SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(this._cliente, objAMaguear);
                String statsNuevoObj = objAMaguear.convertirStatsAString();
                String todaInfo = objAMaguear.stringObjetoConPalo(1);
                SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(this._artesano.getMapa(), this._artesano.getID(), "+" + idObjModMaguear);
                SocketManager.ENVIAR_ErK_RESULTADO_TRABAJO(this._artesano, "O", "+", todaInfo);
                SocketManager.ENVIAR_ErK_RESULTADO_TRABAJO(this._cliente, "O", "+", todaInfo);
                this._artesano.setForjaEcK("K;" + idObjModMaguear + ";T" + this._cliente.getNombre() + ";" + statsNuevoObj);
                this._cliente.setForjaEcK("K;" + idObjModMaguear + ";B" + this._artesano.getNombre() + ";" + statsNuevoObj);
            }
            this._ingredientes.clear();
            return exito;
        }

        public void resetReceta() {
            this._ingredientes.clear();
            this._ultimoTrabajo.clear();
        }

        public void unaMagueada() {
            this._primero = false;
            this._tiempoTrabajo.start();
            this.enviarEmK(this._datos);
        }

        public void variasMagueadas(int tiempo, Personaje perso) {
            this._tiempoTrabajo.stop();
            this._ultimoTrabajo.clear();
            this._ultimoTrabajo.putAll(this._ingredientes);
            for (int a = tiempo; a >= 0; --a) {
                this._ingredientes.clear();
                if (this._romperse || this._interrumpir) {
                    SocketManager.ENVIAR_Ea_TERMINO_RECETAS(perso, this._interrumpir ? "2" : "4");
                    return;
                }
                this._veces = a;
                SocketManager.ENVIAR_EA_TURNO_RECETA(perso, String.valueOf(a));
                this._ingredientes.putAll(this._ultimoTrabajo);
                this.iniciarReceta();
                try {
                    Thread.sleep(300L);
                    continue;
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }
            SocketManager.ENVIAR_Ea_TERMINO_RECETAS(perso, "1");
            this.enviarEmK(this._datos);
        }

        private void enviarEmK(String str) {
            if (!str.isEmpty()) {
                SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(this._artesano, 'O', "+", str);
            }
        }

        public void interrumpirMagueada() {
            this._interrumpir = true;
        }

        public void ponerIngredUltRecet() {
            if (this._artesano == null || this._ultimoTrabajo == null || !this._ingredientes.isEmpty()) {
                return;
            }
            this._ingredientes.clear();
            this._ingredientes.putAll(this._ultimoTrabajo);
            for (Map.Entry<Integer, Integer> e : this._ingredientes.entrySet()) {
                Objeto objeto = Mundo.getObjeto(e.getKey());
                if (objeto == null || objeto.getCantidad() < e.getValue()) continue;
                SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(this._artesano.getCuenta().getEntradaPersonaje().getOut(), 'O', "+", e.getKey() + "|" + e.getValue());
            }
        }
    }

    public static class StatsOficio {
        private int _posicion;
        private Oficio _oficio;
        private int _nivel;
        private long _exp;
        private ArrayList<AccionTrabajo> _trabajosPoderRealizar = new ArrayList<AccionTrabajo>();
        private boolean _esPagable;
        private boolean _gratisSiFalla;
        private boolean _noProporcRecurso;
        private int _slotsPublico;
        private int _adicional = 0;
        private AccionTrabajo _tempTrabajo;

        public StatsOficio(int posicion, Oficio oficio, int nivel, long xp) {
            this._posicion = posicion;
            this._oficio = oficio;
            this._nivel = nivel;
            this._exp = xp;
            this._trabajosPoderRealizar = Constantes.getTrabajosPorOficios(oficio.getID(), nivel);
            this._slotsPublico = Constantes.getIngMaxPorNivel(nivel);
        }

        public int getAdicional() {
            return this._adicional;
        }

        public int getNivel() {
            return this._nivel;
        }

        public boolean esPagable() {
            return this._esPagable;
        }

        public boolean esGratisSiFalla() {
            return this._gratisSiFalla;
        }

        public boolean noProveerRecuerso() {
            return this._noProporcRecurso;
        }

        public void setSlotsPublico(int slots) {
            this._slotsPublico = slots;
        }

        public int getSlotsPublico() {
            return this._slotsPublico;
        }

        public void subirNivel(Personaje perso) {
            ++this._nivel;
            this._trabajosPoderRealizar = Constantes.getTrabajosPorOficios(this._oficio.getID(), this._nivel);
            this._adicional = (int)Math.sqrt(this._nivel);
        }

        public String analizarTrabajolOficio() {
            String str = "|" + this._oficio.getID() + ";";
            boolean primero = true;
            for (AccionTrabajo AT : this._trabajosPoderRealizar) {
                if (!primero) {
                    str = String.valueOf(str) + ",";
                } else {
                    primero = false;
                }
                str = String.valueOf(str) + AT.getIDTrabajo() + "~" + AT.getCasillasMax() + "~";
                str = AT.esReceta() ? String.valueOf(str) + "0~0~" + AT.getSuerte() : String.valueOf(str) + AT.getCasillasMin() + "~0~" + AT.getTiempo();
            }
            return str;
        }

        public long getXP() {
            return this._exp;
        }

        public void iniciarTrabajo(int idTrabajo, Personaje perso, Mapa.ObjetoInteractivo OI, GameThread.AccionDeJuego AJ, Mapa.Celda celda) {
            for (AccionTrabajo AT : this._trabajosPoderRealizar) {
                if (AT.getIDTrabajo() != idTrabajo) continue;
                this._tempTrabajo = AT;
                AT.iniciarAccionTrabajo(perso, OI, AJ, celda);
                return;
            }
        }

        public void finalizarTrabajo(int idTrabajo, Personaje perso, GameThread.AccionDeJuego AJ, Mapa.Celda celda) {
            if (this._tempTrabajo == null) {
                return;
            }
            this._tempTrabajo.finalizarAccionTrabajo(perso, AJ, celda);
            this.addXP(perso, (int)((float)this._tempTrabajo.getXpGanada() * LesGuardians.RATE_XP_PROF));
        }

        public void addXP(Personaje perso, long xp) {
            if (this._nivel > LesGuardians.MAX_NIVEL_PROF - 1) {
                return;
            }
            int exNivel = this._nivel;
            this._exp += xp;
            while (this._exp >= (long)Mundo.getExpNivel((int)(this._nivel + 1))._oficio && this._nivel < LesGuardians.MAX_NIVEL_PROF) {
                this.subirNivel(perso);
            }
            ArrayList<StatsOficio> list = new ArrayList<StatsOficio>();
            list.add(this);
            if (perso.enLinea()) {
                if (this._nivel >= LesGuardians.MAX_NIVEL_PROF) {
                    perso.setTitulo(Constantes.getIdTituloOficio(this._oficio._id));
                    if (perso.getPelea() == null) {
                        SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(perso.getMapa(), perso);
                    }
                }
                if (this._nivel > exNivel) {
                    SocketManager.ENVIAR_JS_TRABAJO_POR_OFICIO(perso, list);
                    SocketManager.ENVIAR_JN_OFICIO_NIVEL(perso, this._oficio.getID(), this._nivel);
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                    SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
                    SocketManager.ENVIAR_JO_OFICIO_OPCIONES(perso, list);
                }
                SocketManager.ENVIAR_JX_EXPERINENCIA_OFICIO(perso, list);
            }
        }

        public String getXpString(String s) {
            String str = String.valueOf(Mundo.getExpNivel((int)this._nivel)._oficio) + s;
            str = String.valueOf(str) + this._exp + s;
            str = String.valueOf(str) + Mundo.getExpNivel((int)(this._nivel < LesGuardians.MAX_NIVEL_PROF ? this._nivel + 1 : this._nivel))._oficio;
            return str;
        }

        public Oficio getOficio() {
            return this._oficio;
        }

        public boolean esValidoTrabajo(int id) {
            for (AccionTrabajo AT : this._trabajosPoderRealizar) {
                if (AT.getIDTrabajo() != id) continue;
                return true;
            }
            return false;
        }

        public int getOpcionBin() {
            int nro = 0;
            nro += this._noProporcRecurso ? 4 : 0;
            nro += this._gratisSiFalla ? 2 : 0;
            return nro += this._esPagable ? 1 : 0;
        }

        public void setOpciones(int bin) {
            this._noProporcRecurso = (bin & 4) == 4;
            this._gratisSiFalla = (bin & 2) == 2;
            this._esPagable = (bin & 1) == 1;
        }

        public int getPosicion() {
            return this._posicion;
        }
    }
}

