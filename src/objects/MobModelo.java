package objects;

import common.Constantes;
import common.Fórmulas;
import common.LesGuardians;
import common.Mundo;
import common.Mundo.Drop;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import objects.Combate;
import objects.Mapa;
import objects.Objeto;
import objects.Personaje;
import objects.Hechizo;

public class MobModelo {
    private int _ID;
    private int _gfxID;
    private byte _alineacion;
    private String _colores;
    private byte _tipoIA = 0;
    private int _minKamas;
    private int _maxKamas;
    private Map<Integer, MobGrado> _grados = new TreeMap<Integer, MobGrado>();
    private ArrayList<Drop> _drops = new ArrayList<Drop>();
    private boolean _esCapturable;
    private int _talla;
    private String _nombre;

    public MobModelo(int id, String nombre, int gfx, byte alineacion, String colores, String grados, String hechizos, String stats, String pdvs, String puntos, String init, int mK, int MK, String xpstr, byte tipoIA, boolean capturable, int talla) {
        _ID = id;
        _gfxID = gfx;
        _alineacion = alineacion;
        _colores = colores;
        _minKamas = mK;
        _maxKamas = MK;
        _tipoIA = tipoIA;
        _esCapturable = capturable;
        _talla = talla;
        _nombre = nombre;
        int idGrado = 1;
        for (int n = 0; n < 11; ++n) {
            try {
                String grado = grados.split("\\|")[n];
                String[] infos = grado.split("@");
                short nivel = Short.parseShort(infos[0]);
                String resistencias = infos[1];
                String aStats = stats.split("\\|")[n];
                String aHechizos = hechizos.split("\\|")[n];
                if (aHechizos.equals("-1")) {
                    aHechizos = "";
                }
                int pdvMax = 1;
                int aInit = 1;
                try {
                    pdvMax = Integer.parseInt(pdvs.split("\\|")[n]);
                    aInit = Integer.parseInt(init.split("\\|")[n]);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                short PA = 3;
                short PM = 3;
                int xp = 10;
                try {
                    String[] pts = puntos.split("\\|")[n].split(";");
                    try {
                        PA = Short.parseShort(pts[0]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    try {
                        PM = Short.parseShort(pts[1]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    try {
                        xp = Integer.parseInt(xpstr.split("\\|")[n]);
                    }
                    catch (Exception exception) {}
                }
                catch (Exception exception) {
                    // empty catch block
                }
                _grados.put(idGrado, new MobGrado(this, idGrado, nivel, PA, PM, resistencias, aStats, aHechizos, pdvMax, aInit, xp));
                ++idGrado;
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public int getID() {
        return _ID;
    }

    public void addDrop(Mundo.Drop drop) {
        _drops.add(drop);
    }

    public int getTalla() {
        return _talla;
    }

    public ArrayList<Mundo.Drop> getDrops() {
        return _drops;
    }

    public int getGfxID() {
        return _gfxID;
    }

    public int getMinKamas() {
        return _minKamas;
    }

    public int getMaxKamas() {
        return _maxKamas;
    }

    public byte getAlineacion() {
        return _alineacion;
    }

    public String getColores() {
        return _colores;
    }

    public byte getTipoInteligencia() {
        return _tipoIA;
    }

    public void setTipoInteligencia(byte IA2) {
        _tipoIA = IA2;
    }

    public Map<Integer, MobGrado> getGrados() {
        return _grados;
    }

    public MobGrado getGradoPorNivel(int nivel) {
        for (Map.Entry<Integer, MobGrado> grado : _grados.entrySet()) {
            if (grado.getValue().getNivel() != nivel) continue;
            return grado.getValue();
        }
        return null;
    }

    public MobGrado getRandomGrado() {
        int randomgrade = (int)(Math.random() * 5.0 + 1.0);
        int graderandom = 1;
        for (Map.Entry<Integer, MobGrado> grade : _grados.entrySet()) {
            if (graderandom == randomgrade) {
                return grade.getValue();
            }
            ++graderandom;
        }
        return null;
    }

    public boolean esCapturable() {
        return _esCapturable;
    }

    public String getNombre() {
        return _nombre;
    }

    public static class GrupoMobs {
        private int _id;
        private short _celdaID;
        private byte _orientacion = (byte)3;
        private byte _alin = 0;
        private int _distanciaAgresion = 0;
        private short _estrellas = 0;
        private boolean _esFixeado = false;
        private Map<Integer, MobGrado> _mobs = new TreeMap<Integer, MobGrado>();
        private Map<Integer, Integer> _almas = new TreeMap<Integer, Integer>();
        private String _condicion = "";
        private Timer _tiempoCondicion;
        private boolean _muerto = false;
        private String _strGrupoMob = "";

        public GrupoMobs(int id, byte alineacion, ArrayList<MobGrado> posiblesMobs, Mapa Mapa, short celda, int maxMobsPorGrupo) {
            _id = id;
            _alin = alineacion;
            int rand = 0;
            int nroMobs = 0;
            switch (maxMobsPorGrupo) {
                case 0: {
                    return;
                }
                case 1: {
                    nroMobs = 1;
                    break;
                }
                case 2: {
                    nroMobs = Fórmulas.getRandomValor(1, 2);
                    break;
                }
                case 3: {
                    nroMobs = Fórmulas.getRandomValor(1, 3);
                    break;
                }
                case 4: {
                    rand = Fórmulas.getRandomValor(0, 99);
                    if (rand < 22) {
                        nroMobs = 1;
                        break;
                    }
                    if (rand < 48) {
                        nroMobs = 2;
                        break;
                    }
                    if (rand < 74) {
                        nroMobs = 3;
                        break;
                    }
                    nroMobs = 4;
                    break;
                }
                case 5: {
                    rand = Fórmulas.getRandomValor(0, 99);
                    if (rand < 15) {
                        nroMobs = 1;
                        break;
                    }
                    if (rand < 35) {
                        nroMobs = 2;
                        break;
                    }
                    if (rand < 60) {
                        nroMobs = 3;
                        break;
                    }
                    if (rand < 85) {
                        nroMobs = 4;
                        break;
                    }
                    nroMobs = 5;
                    break;
                }
                case 6: {
                    rand = Fórmulas.getRandomValor(0, 99);
                    if (rand < 10) {
                        nroMobs = 1;
                        break;
                    }
                    if (rand < 25) {
                        nroMobs = 2;
                        break;
                    }
                    if (rand < 45) {
                        nroMobs = 3;
                        break;
                    }
                    if (rand < 65) {
                        nroMobs = 4;
                        break;
                    }
                    if (rand < 85) {
                        nroMobs = 5;
                        break;
                    }
                    nroMobs = 6;
                    break;
                }
                case 7: {
                    rand = Fórmulas.getRandomValor(0, 99);
                    if (rand < 9) {
                        nroMobs = 1;
                        break;
                    }
                    if (rand < 20) {
                        nroMobs = 2;
                        break;
                    }
                    if (rand < 35) {
                        nroMobs = 3;
                        break;
                    }
                    if (rand < 55) {
                        nroMobs = 4;
                        break;
                    }
                    if (rand < 75) {
                        nroMobs = 5;
                        break;
                    }
                    if (rand < 91) {
                        nroMobs = 6;
                        break;
                    }
                    nroMobs = 7;
                    break;
                }
                default: {
                    rand = Fórmulas.getRandomValor(0, 99);
                    nroMobs = rand < 9 ? 1 : (rand < 20 ? 2 : (rand < 33 ? 3 : (rand < 50 ? 4 : (rand < 67 ? 5 : (rand < 80 ? 6 : (rand < 91 ? 7 : 8))))));
                }
            }
            int idMob = -1;
            int maxNivel = 0;
            for (int a = 0; a < nroMobs; ++a) {
                int idModeloMob;
                int random = Fórmulas.getRandomValor(0, posiblesMobs.size() - 1);
                MobGrado mob = posiblesMobs.get(random).copiarMob();
                if (mob.getNivel() > maxNivel) {
                    maxNivel = mob.getNivel();
                }
                if (_almas.containsKey(idModeloMob = mob.getModelo().getID())) {
                    int valor = _almas.get(idModeloMob);
                    _almas.remove(idModeloMob);
                    _almas.put(idModeloMob, valor + 1);
                } else {
                    _almas.put(idModeloMob, 1);
                }
                _mobs.put(idMob, mob);
                --idMob;
            }
            _distanciaAgresion = Constantes.agresionPorNivel(maxNivel);
            if (_alin != 0) {
                _distanciaAgresion = 15;
            }
            _celdaID = celda == -1 ? Mapa.getRandomCeldaIDLibre() : celda;
            if (_celdaID == 0) {
                return;
            }
            _orientacion = (byte)(Fórmulas.getRandomValor(0, 3) * 2);
            _esFixeado = false;
            _estrellas = 0;
        }

        public GrupoMobs(int id, short celdaID, String strGrupoMob) {
            _id = id;
            _alin = 0;
            _celdaID = celdaID;
            _distanciaAgresion = Constantes.agresionPorNivel(0);
            _esFixeado = true;
            _strGrupoMob = strGrupoMob;
            int idMob = -1;
            for (String data : strGrupoMob.split(";")) {
                String[] infos = data.split(",");
                try {
                    int idMobModelo = Integer.parseInt(infos[0]);
                    int min = Integer.parseInt(infos[1]);
                    int max = Integer.parseInt(infos[2]);
                    MobModelo m = Mundo.getMobModelo(idMobModelo);
                    ArrayList<MobGrado> mgs = new ArrayList<MobGrado>();
                    for (MobGrado MG : m.getGrados().values()) {
                        if (MG._nivel < min || MG._nivel > max) continue;
                        mgs.add(MG);
                    }
                    if (mgs.isEmpty()) continue;
                    if (_almas.containsKey(idMobModelo)) {
                        int valor = _almas.get(idMobModelo);
                        _almas.remove(idMobModelo);
                        _almas.put(idMobModelo, valor + 1);
                    } else {
                        _almas.put(idMobModelo, 1);
                    }
                    _mobs.put(idMob, (MobGrado)mgs.get(Fórmulas.getRandomValor(0, mgs.size() - 1)));
                    --idMob;
                }
                catch (Exception exception) {}
            }
            _orientacion = (byte)(Fórmulas.getRandomValor(0, 3) * 2 + 1);
        }

        public int getID() {
            return _id;
        }

        public String getStrGrupoMob() {
            return _strGrupoMob;
        }

        public void setMuerto(boolean muerto) {
            _muerto = muerto;
        }

        public boolean estaMuerto() {
            return _muerto;
        }

        public short getEstrellas() {
            return _estrellas;
        }

        public void setEstrellas(short estre) {
            _estrellas = estre;
        }

        public void aumentarEstrellas() {
            if (_estrellas == 0) {
                _estrellas = 1;
                return;
            }
            _estrellas = (short)(_estrellas + 25);
            if (_estrellas > 1025) {
                _estrellas = 0;
            }
        }

        public void aumentarEstrellasCant(int cant) {
            _estrellas = (short)(_estrellas + cant);
            if (_estrellas > 1025) {
                _estrellas = 0;
            }
        }

        public short getCeldaID() {
            return _celdaID;
        }

        public byte getOrientacion() {
            return _orientacion;
        }

        public int getDistanciaAgresion() {
            return _distanciaAgresion;
        }

        public boolean esPermanente() {
            return _esFixeado;
        }

        public void setOrientacion(byte o) {
            _orientacion = 0;
        }

        public void setCeldaID(short id) {
            _celdaID = id;
        }

        public byte getAlineamiento() {
            return _alin;
        }

        public MobGrado getMobGradoPorID(int id) {
            return _mobs.get(id);
        }

        public int getCantMobs() {
            return _mobs.size();
        }

        public String enviarGM() {
            String mobIDs = "";
            String mobGFX = "";
            String mobNiveles = "";
            String colores = "";
            boolean primero = true;
            if (_mobs.isEmpty()) {
                return "";
            }
            for (Map.Entry<Integer, MobGrado> entry : _mobs.entrySet()) {
                if (!primero) {
                    mobIDs = String.valueOf(mobIDs) + ",";
                    mobGFX = String.valueOf(mobGFX) + ",";
                    mobNiveles = String.valueOf(mobNiveles) + ",";
                }
                MobModelo mob = entry.getValue().getModelo();
                mobIDs = String.valueOf(mobIDs) + mob.getID();
                mobGFX = String.valueOf(mobGFX) + mob.getGfxID() + "^100";
                mobNiveles = String.valueOf(mobNiveles) + entry.getValue().getNivel();
                colores = String.valueOf(colores) + mob.getColores() + ";0,0,0,0;";
                primero = false;
            }
            return "+" + _celdaID + ";" + _orientacion + ";" + _estrellas + ";" + _id + ";" + mobIDs + ";-3;" + mobGFX + ";" + mobNiveles + ";" + colores;
        }

        public Map<Integer, MobGrado> getMobs() {
            return _mobs;
        }

        public Map<Integer, Integer> getAlmasMobs() {
            return _almas;
        }

        public void setCondicion(String cond) {
            _condicion = cond;
        }

        public String getCondicion() {
            return _condicion;
        }

        public void setEsPermanente(boolean fix) {
            _esFixeado = fix;
        }

        public void inicioTiempoCondicion() {
            _tiempoCondicion = new Timer();
            _tiempoCondicion.schedule(new TimerTask(){

                @Override
                public void run() {
                    _condicion = "";
                }
            }, LesGuardians.TIEMPO_ARENA);
        }

        public void stopConditionTimer() {
            try {
                _tiempoCondicion.cancel();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static class MobGrado {
        private MobModelo _modelo;
        private int _grado;
        private short _nivel;
        private int _PDV;
        private int _idEnPelea;
        private int _PDVMAX;
        private int _iniciativa;
        private short _PA;
        private short _PM;
        private Mapa.Celda _celdaPelea;
        private int _baseXp = 10;
        private Map<Integer, Integer> _stats = new TreeMap<Integer, Integer>();
        private Map<Integer, Hechizo.StatsHechizos> _hechizos = new TreeMap<Integer, Hechizo.StatsHechizos>();
        private ArrayList<Objeto> _objetosHeroico = new ArrayList<Objeto>();
        private long _kamasHeroico = 0L;

        public MobGrado(MobModelo modelo, int grado, short nivel, short PA, short PM, String resistencia, String stats, String hechizos, int pdvMax, int iniciativa, int xp) {
            String[] spellsArray;
            _modelo = modelo;
            _grado = grado;
            _nivel = nivel;
            _PDV = _PDVMAX = pdvMax;
            _PA = PA;
            _PM = PM;
            _baseXp = xp;
            _iniciativa = iniciativa;
            String[] resistencias = resistencia.split(";");
            String[] statsArray = stats.split(",");
            int RNeutral = 0;
            int RFuego = 0;
            int RAgua = 0;
            int RAire = 0;
            int RTierra = 0;
            int EPA = 0;
            int EPM = 0;
            int fuerza = 0;
            int inteligencia = 0;
            int sabiduria = 0;
            int suerte = 0;
            int agilidad = 0;
            try {
                RNeutral = Integer.parseInt(resistencias[0]);
                RTierra = Integer.parseInt(resistencias[1]);
                RFuego = Integer.parseInt(resistencias[2]);
                RAgua = Integer.parseInt(resistencias[3]);
                RAire = Integer.parseInt(resistencias[4]);
                EPA = Integer.parseInt(resistencias[5]);
                EPM = Integer.parseInt(resistencias[6]);
                fuerza = Integer.parseInt(statsArray[0]);
                sabiduria = Integer.parseInt(statsArray[1]);
                inteligencia = Integer.parseInt(statsArray[2]);
                suerte = Integer.parseInt(statsArray[3]);
                agilidad = Integer.parseInt(statsArray[4]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            _stats.clear();
            _stats.put(118, fuerza);
            _stats.put(124, sabiduria);
            _stats.put(126, inteligencia);
            _stats.put(123, suerte);
            _stats.put(119, agilidad);
            _stats.put(214, RNeutral);
            _stats.put(213, RFuego);
            _stats.put(211, RAgua);
            _stats.put(212, RAire);
            _stats.put(210, RTierra);
            _stats.put(160, EPA);
            _stats.put(161, EPM);
            _stats.put(111, Integer.valueOf(PA));
            _stats.put(128, Integer.valueOf(PM));
            if (_modelo.getID() == 423) {
                _stats.put(182, 4);
                _stats.put(105, 100);
            } else {
                _stats.put(182, 2);
            }
            _hechizos.clear();
            String[] arrstring = spellsArray = hechizos.split(";");
            int n = spellsArray.length;
            for (int i = 0; i < n; ++i) {
                Hechizo.StatsHechizos hechizoStats;
                Hechizo hechizo;
                String str = arrstring[i];
                if (str.equals("")) continue;
                String[] spellInfo = str.split("@");
                int hechizoID = 0;
                int hechizoNivel = 0;
                try {
                    hechizoID = Integer.parseInt(spellInfo[0]);
                    hechizoNivel = Integer.parseInt(spellInfo[1]);
                }
                catch (Exception e) {
                    continue;
                }
                if (hechizoID == 0 || hechizoNivel == 0 || (hechizo = Mundo.getHechizo(hechizoID)) == null || (hechizoStats = hechizo.getStatsPorNivel(hechizoNivel)) == null) continue;
                _hechizos.put(hechizoID, hechizoStats);
            }
        }

        private MobGrado(MobModelo modelo, int grado, short nivel, int pdv, int pdvmax, short PA, short PM, Map<Integer, Integer> stats, Map<Integer, Hechizo.StatsHechizos> hechizos, int iniciativa, int xp) {
            _modelo = modelo;
            _grado = grado;
            _nivel = nivel;
            _PDV = pdv;
            _PDVMAX = pdvmax;
            _PA = PA;
            _PM = PM;
            _iniciativa = iniciativa;
            _stats = stats;
            _hechizos = hechizos;
            _idEnPelea = -1;
            _baseXp = xp;
        }

        public long getBaseXp() {
            return _baseXp;
        }

        public int getIniciativa() {
            return _iniciativa;
        }

        public MobGrado copiarMob() {
            TreeMap<Integer, Integer> nuevoStats = new TreeMap<Integer, Integer>();
            nuevoStats.putAll(_stats);
            return new MobGrado(_modelo, _grado, _nivel, _PDV, _PDVMAX, _PA, _PM, nuevoStats, _hechizos, _iniciativa, _baseXp);
        }

        public Personaje.Stats getStats() {
            return new Personaje.Stats(_stats);
        }

        public int getNivel() {
            return _nivel;
        }

        public void addKamasHeroico(long kamas) {
            if (kamas < 1L) {
                return;
            }
            _kamasHeroico += kamas;
        }

        public long getKamasHeroico() {
            return _kamasHeroico;
        }

        public void borrarKamasHeroico() {
            _kamasHeroico = 0L;
        }

        public void addObjetoHeroico(Objeto obj) {
            _objetosHeroico.add(obj);
        }

        public ArrayList<Objeto> getObjetoHeroico() {
            return _objetosHeroico;
        }

        public void borrarObjetosHeroico() {
            _objetosHeroico.clear();
        }

        public Mapa.Celda getCeldaPelea() {
            return _celdaPelea;
        }

        public void setCeldaPelea(Mapa.Celda celda) {
            _celdaPelea = celda;
        }

        public Map<Integer, Hechizo.StatsHechizos> getHechizos() {
            return _hechizos;
        }

        public MobModelo getModelo() {
            return _modelo;
        }

        public int getPDV() {
            return _PDV;
        }

        public void setPDV(int pdv) {
            _PDV = pdv;
        }

        public int getPDVMAX() {
            return _PDVMAX;
        }

        public int getGrado() {
            return _grado;
        }

        public void setIdEnPelea(int i) {
            _idEnPelea = i;
        }

        public int getIdEnPelea() {
            return _idEnPelea;
        }

        public int getPA() {
            return _PA;
        }

        public int getPM() {
            return _PM;
        }

        public void modificarStatsPorInvocador(Combate.Luchador invocador) {
            float coefStats = 1.0f;
            float coefVita = 1.0f;
            if (invocador.esInvocacion()) {
                invocador = invocador.getInvocador();
            }
            if (invocador.getPersonaje() != null) {
                coefStats += (float)invocador.getNivel() / 600.0f;
                coefVita += (float)invocador.getNivel() / 100.0f;
                if (invocador.getPersonaje().getClase(true) == 2) {
                    coefStats += 0.2f;
                    coefVita += 0.2f;
                }
            } else {
                coefStats = (float)invocador.getNivel() / 200.0f;
                coefVita = (float)invocador.getNivel() / 100.0f;
            }
            Personaje.Stats stats = invocador.getTotalStatsSinBuff();
            _PDV = (int)((float)_PDVMAX * coefVita) + stats.getEfecto(125) / 40;
            if (_PDV < 1) {
                _PDV = 1;
            }
            _PDVMAX = _PDV;
            int fuerza = (int)((float)_stats.get(118).intValue() + (float)stats.getEfecto(118) * coefStats);
            int inteligencia = (int)((float)_stats.get(126).intValue() + (float)stats.getEfecto(126) * coefStats);
            int agilidad = (int)((float)_stats.get(119).intValue() + (float)stats.getEfecto(119) * coefStats);
            int sabiduria = (int)((float)_stats.get(124).intValue() + (float)stats.getEfecto(124) * coefStats);
            int suerte = (int)((float)_stats.get(123).intValue() + (float)stats.getEfecto(123) * coefStats);
            _stats.remove(118);
            _stats.remove(126);
            _stats.remove(119);
            _stats.remove(124);
            _stats.remove(123);
            _stats.put(118, fuerza);
            _stats.put(126, inteligencia);
            _stats.put(119, agilidad);
            _stats.put(124, sabiduria);
            _stats.put(123, suerte);
        }
    }
}