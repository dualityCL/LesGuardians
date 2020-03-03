package objects;

import common.Fórmulas;
import common.Pathfinding;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Combate;
import objects.Mapa;
import objects.EfectoHechizo;

public class Hechizo {
    private String _nombre;
    private int _ID;
    private int _spriteID;
    private String _spriteInfos;
    private Map<Integer, StatsHechizos> _statsHechizos = new TreeMap<Integer, StatsHechizos>();
    private ArrayList<Integer> _afectadosEstandar = new ArrayList();

    public Hechizo(int aHechizoID, String aNombre, int aSpriteID, String aSpriteInfos, String afectados) {
        this._ID = aHechizoID;
        this._nombre = aNombre;
        this._spriteID = aSpriteID;
        this._spriteInfos = aSpriteInfos;
        String afectadosEstandar = afectados.split(":")[0];
        String afectadosPorGC = "";
        if (afectados.split(":").length > 1) {
            afectadosPorGC = afectados.split(":")[1];
        }
        for (String num : afectadosEstandar.split(";")) {
            try {
                this._afectadosEstandar.add(Integer.parseInt(num));
            }
            catch (Exception e) {
                this._afectadosEstandar.add(0);
            }
        }
        for (String num : afectadosPorGC.split(";")) {
            try {
                this._afectadosEstandar.add(Integer.parseInt(num));
            }
            catch (Exception e) {
                this._afectadosEstandar.add(0);
            }
        }
    }

    public ArrayList<Integer> getAfectadosEstandar() {
        return this._afectadosEstandar;
    }

    public void setAfectadosEstandar(String afectados) {
        this._afectadosEstandar.clear();
        String afectadosEstandar = afectados.split(":")[0];
        String afectadosPorGC = "";
        if (afectados.split(":").length > 1) {
            afectadosPorGC = afectados.split(":")[1];
        }
        for (String num : afectadosEstandar.split(";")) {
            try {
                this._afectadosEstandar.add(Integer.parseInt(num));
            }
            catch (Exception e) {
                this._afectadosEstandar.add(0);
            }
        }
        for (String num : afectadosPorGC.split(";")) {
            try {
                this._afectadosEstandar.add(Integer.parseInt(num));
            }
            catch (Exception e) {
                this._afectadosEstandar.add(0);
            }
        }
    }

    public int getSpriteID() {
        return this._spriteID;
    }

    public String getSpriteInfos() {
        return this._spriteInfos;
    }

    public void setSpriteInfos(String str) {
        this._spriteInfos = str;
    }

    public void setSpriteID(int id) {
        this._spriteID = id;
    }

    public String getNombre() {
        return this._nombre;
    }

    public int getID() {
        return this._ID;
    }

    public StatsHechizos getStatsPorNivel(int lvl) {
        return this._statsHechizos.get(lvl);
    }

    public void addStatsHechizos(Integer lvl, StatsHechizos stats) {
        if (this._statsHechizos.get(lvl) != null) {
            return;
        }
        this._statsHechizos.put(lvl, stats);
    }

    public static class StatsHechizos {
        private int _hechizoID;
        private int _nivel;
        private int _costePA;
        private int _minAlc;
        private int _maxAlc;
        private int _porcGC;
        private int _porcFC;
        private boolean _esLanzLinea;
        private boolean _tieneLineaVuelo;
        private boolean _esCeldaVacia;
        private boolean _esModifAlc;
        private int _maxLanzPorTurno;
        private int _maxLanzPorObjetivo;
        private int _sigLanzamiento;
        private int _reqLevel;
        private boolean _esFinTurnoSiFC;
        private ArrayList<EfectoHechizo> _efectos;
        private ArrayList<EfectoHechizo> _efectosGC;
        private String _afectados;
        private ArrayList<Integer> _estadosProhibidos = new ArrayList();
        private ArrayList<Integer> _estadosNecesarios = new ArrayList();
        private Hechizo _hechizo;
        private byte _tipoHechizo;

        public StatsHechizos(int hechizoID, int nivel, int costePA, int minAlc, int maxAlc, int porcGC, int porcFC, boolean esLanzLinea, boolean tieneLineaVuelo, boolean esCeldaVacia, boolean esModifAlc, int maxLanzPorTurno, int maxLanzPorObjetivo, int sigLanzamiento, int reqLevel, boolean esFinTurnoSiFC, String efectos, String efectosGC, String afectados, String estadosProhibidos, String estadosNecesarios, Hechizo hechizo, byte tipoHechizo) {
            String[] estadosN;
            String[] estados;
            this._hechizoID = hechizoID;
            this._nivel = nivel;
            this._costePA = costePA;
            this._minAlc = minAlc;
            this._maxAlc = maxAlc;
            this._porcGC = porcGC;
            this._porcFC = porcFC;
            this._esLanzLinea = esLanzLinea;
            this._tieneLineaVuelo = tieneLineaVuelo;
            this._esCeldaVacia = esCeldaVacia;
            this._esModifAlc = esModifAlc;
            this._maxLanzPorTurno = maxLanzPorTurno;
            this._maxLanzPorObjetivo = maxLanzPorObjetivo;
            this._sigLanzamiento = sigLanzamiento;
            this._reqLevel = reqLevel;
            this._esFinTurnoSiFC = esFinTurnoSiFC;
            this._efectos = this.analizarEfectos(efectos);
            this._efectosGC = this.analizarEfectos(efectosGC);
            this._afectados = afectados;
            String[] arrstring = estados = estadosProhibidos.split(";");
            int n = estados.length;
            for (int i = 0; i < n; ++i) {
                String esta = arrstring[i];
                esta = esta.trim();
                this._estadosProhibidos.add(Integer.parseInt(esta));
            }
            String[] arrstring2 = estadosN = estadosNecesarios.split(";");
            int n2 = estadosN.length;
            for (n = 0; n < n2; ++n) {
                String esta = arrstring2[n];
                esta = esta.trim();
                this._estadosNecesarios.add(Integer.parseInt(esta));
            }
            this._hechizo = hechizo;
            this._tipoHechizo = tipoHechizo;
        }

        private ArrayList<EfectoHechizo> analizarEfectos(String e) {
            String[] splt;
            ArrayList<EfectoHechizo> efectos = new ArrayList<EfectoHechizo>();
            String[] arrstring = splt = e.split("\\|");
            int n = splt.length;
            for (int i = 0; i < n; ++i) {
                String a = arrstring[i];
                try {
                    if (e.equals("-1")) continue;
                    int id = Integer.parseInt(a.split(";", 2)[0]);
                    String args = a.split(";", 2)[1];
                    efectos.add(new EfectoHechizo(id, args, this._hechizoID, this._nivel));
                    continue;
                }
                catch (Exception f) {
                    f.printStackTrace();
                    System.out.println(a);
                    System.exit(1);
                }
            }
            return efectos;
        }

        public int getHechizoID() {
            return this._hechizoID;
        }

        public Hechizo getHechizo() {
            return this._hechizo;
        }

        public int getSpriteID() {
            return this._hechizo.getSpriteID();
        }

        public String getSpriteInfos() {
            return this._hechizo.getSpriteInfos();
        }

        public int getNivel() {
            return this._nivel;
        }

        public ArrayList<Integer> getEstadosProhi() {
            return this._estadosProhibidos;
        }

        public ArrayList<Integer> getEstadosNeces() {
            return this._estadosNecesarios;
        }

        public int getCostePA() {
            return this._costePA;
        }

        public int getMinAlc() {
            return this._minAlc;
        }

        public int getMaxAlc() {
            return this._maxAlc;
        }

        public int getPorcGC() {
            return this._porcGC;
        }

        public int getPorcFC() {
            return this._porcFC;
        }

        public boolean esLanzLinea() {
            return this._esLanzLinea;
        }

        public boolean tieneLineaVuelo() {
            return this._tieneLineaVuelo;
        }

        public boolean esCeldaVacia() {
            return this._esCeldaVacia;
        }

        public boolean esModifAlc() {
            return this._esModifAlc;
        }

        public int getMaxLanzPorTurno() {
            return this._maxLanzPorTurno;
        }

        public int getMaxLanzPorJugador() {
            return this._maxLanzPorObjetivo;
        }

        public int getSigLanzamiento() {
            return this._sigLanzamiento;
        }

        public int getReqNivel() {
            return this._reqLevel;
        }

        public boolean esFinTurnoSiFC() {
            return this._esFinTurnoSiFC;
        }

        public ArrayList<EfectoHechizo> getEfectos() {
            return this._efectos;
        }

        public ArrayList<EfectoHechizo> getEfectosGC() {
            return this._efectosGC;
        }

        public String getAfectados() {
            return this._afectados;
        }

        public void aplicaTrampaAPelea(Combate pelea, Combate.Luchador lanzador, Mapa.Celda celda, ArrayList<Mapa.Celda> celdas, boolean esGC) {
            ArrayList<EfectoHechizo> efectosH = esGC ? this._efectosGC : this._efectos;
            int azar = Fórmulas.getRandomValor(0, 99);
            int suerte = 0;
            for (EfectoHechizo EH : efectosH) {
                if (EH.getSuerte() != 0 && EH.getSuerte() != 100) {
                    if (azar <= suerte || azar >= EH.getSuerte() + suerte) {
                        suerte += EH.getSuerte();
                        continue;
                    }
                    suerte += EH.getSuerte();
                }
                ArrayList<Combate.Luchador> ojetivos = EfectoHechizo.getAfectados(pelea, celdas, this._hechizoID);
                EH.aplicarHechizoAPelea(pelea, lanzador, celda, ojetivos, celdas);
            }
        }

        public void aplicaHechizoAPelea(Combate pelea, Combate.Luchador lanzador, Mapa.Celda celda, boolean esGC) {
            ArrayList<EfectoHechizo> efectosH = esGC ? this._efectosGC : this._efectos;
            int suerte = 0;
            int num = 0;
            int azar = Fórmulas.getRandomValor(0, 99);
            for (EfectoHechizo EH : efectosH) {
                Combate.Luchador invocador;
                if (pelea.getEstado() >= 4) {
                    return;
                }
                if (EH.getSuerte() != 0 && EH.getSuerte() != 100) {
                    if (azar <= suerte || azar > EH.getSuerte() + suerte) {
                        suerte += EH.getSuerte();
                        ++num;
                        continue;
                    }
                    suerte += EH.getSuerte();
                }
                int tipoAlcance = num * 2;
                if (esGC) {
                    tipoAlcance += this._efectos.size() * 2;
                }
                ArrayList<Mapa.Celda> celdas = Pathfinding.getCeldasAfectadasEnElArea(pelea.getMapaCopia(), celda.getID(), lanzador.getCeldaPelea().getID(), this._afectados, tipoAlcance, esGC);
                ArrayList<Mapa.Celda> celdasFinales = new ArrayList<Mapa.Celda>();
                int afectados = 0;
                if (this._hechizo != null && this._hechizo.getAfectadosEstandar().size() > num) {
                    afectados = this._hechizo.getAfectadosEstandar().get(num);
                }
                for (Mapa.Celda C : celdas) {
                    Combate.Luchador L;
                    if (C == null || (L = C.getPrimerLuchador()) == null || (afectados & 1) == 1 && L.getEquipoBin() == lanzador.getEquipoBin() || (afectados >> 1 & 1) == 1 && L.getID() == lanzador.getID() || (afectados >> 2 & 1) == 1 && L.getEquipoBin() != lanzador.getEquipoBin() || (afectados >> 3 & 1) == 1 && !L.esInvocacion() || (afectados >> 4 & 1) == 1 && L.esInvocacion() || (afectados >> 5 & 1) == 1 && L.getID() != lanzador.getID() || (afectados >> 7 & 1) == 1 && celda.getID() == L.getCeldaPelea().getID()) continue;
                    celdasFinales.add(C);
                }
                if ((afectados >> 5 & 1) == 1 && !celdasFinales.contains(lanzador.getCeldaPelea())) {
                    celdasFinales.add(lanzador.getCeldaPelea());
                }
                if ((afectados >> 6 & 1) == 1 && (invocador = lanzador.getInvocador()) != null && !celdasFinales.contains(invocador.getCeldaPelea())) {
                    celdasFinales.add(invocador.getCeldaPelea());
                }
                ArrayList<Combate.Luchador> objetivos = EfectoHechizo.getAfectados(pelea, celdasFinales, this._hechizoID);
                EH.aplicarHechizoAPelea(pelea, lanzador, celda, objetivos, celdasFinales);
                ++num;
            }
        }
    }
}

