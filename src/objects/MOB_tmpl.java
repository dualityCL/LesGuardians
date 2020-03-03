package objects;

import common.Constantes;
import common.Fórmulas;
import common.LesGuardians;
import common.World;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import objects.Fight;
import objects.Maps;
import objects.Objeto;
import objects.Personagens;
import objects.Spell;

public class MOB_tmpl {
    private int _ID;
    private int _gfxID;
    private byte _alineacion;
    private String _colores;
    private byte _tipoIA = 0;
    private int _minKamas;
    private int _maxKamas;
    private Map<Integer, MobGrado> _grados = new TreeMap<Integer, MobGrado>();
    private ArrayList<World.Drop> _drops = new ArrayList();
    private boolean _esCapturable;
    private int _talla;
    private String _nombre;

    public MOB_tmpl(int id, String nombre, int gfx, byte alineacion, String colores, String grados, String hechizos, String stats, String pdvs, String puntos, String init, int mK, int MK, String xpstr, byte tipoIA, boolean capturable, int talla) {
        this._ID = id;
        this._gfxID = gfx;
        this._alineacion = alineacion;
        this._colores = colores;
        this._minKamas = mK;
        this._maxKamas = MK;
        this._tipoIA = tipoIA;
        this._esCapturable = capturable;
        this._talla = talla;
        this._nombre = nombre;
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
                this._grados.put(idGrado, new MobGrado(this, idGrado, nivel, PA, PM, resistencias, aStats, aHechizos, pdvMax, aInit, xp));
                ++idGrado;
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public int getID() {
        return this._ID;
    }

    public void addDrop(World.Drop drop) {
        this._drops.add(drop);
    }

    public int getTalla() {
        return this._talla;
    }

    public ArrayList<World.Drop> getDrops() {
        return this._drops;
    }

    public int getGfxID() {
        return this._gfxID;
    }

    public int getMinKamas() {
        return this._minKamas;
    }

    public int getMaxKamas() {
        return this._maxKamas;
    }

    public byte getAlineacion() {
        return this._alineacion;
    }

    public String getColores() {
        return this._colores;
    }

    public byte getTipoInteligencia() {
        return this._tipoIA;
    }

    public void setTipoInteligencia(byte IA2) {
        this._tipoIA = IA2;
    }

    public Map<Integer, MobGrado> getGrados() {
        return this._grados;
    }

    public MobGrado getGradoPorNivel(int nivel) {
        for (Map.Entry<Integer, MobGrado> grado : this._grados.entrySet()) {
            if (grado.getValue().getNivel() != nivel) continue;
            return grado.getValue();
        }
        return null;
    }

    public MobGrado getRandomGrado() {
        int randomgrade = (int)(Math.random() * 5.0 + 1.0);
        int graderandom = 1;
        for (Map.Entry<Integer, MobGrado> grade : this._grados.entrySet()) {
            if (graderandom == randomgrade) {
                return grade.getValue();
            }
            ++graderandom;
        }
        return null;
    }

    public boolean esCapturable() {
        return this._esCapturable;
    }

    public String getNombre() {
        return this._nombre;
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

        public GrupoMobs(int id, byte alineacion, ArrayList<MobGrado> posiblesMobs, Maps Mapa, short celda, int maxMobsPorGrupo) {
            this._id = id;
            this._alin = alineacion;
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
                if (this._almas.containsKey(idModeloMob = mob.getModelo().getID())) {
                    int valor = this._almas.get(idModeloMob);
                    this._almas.remove(idModeloMob);
                    this._almas.put(idModeloMob, valor + 1);
                } else {
                    this._almas.put(idModeloMob, 1);
                }
                this._mobs.put(idMob, mob);
                --idMob;
            }
            this._distanciaAgresion = Constantes.agresionPorNivel(maxNivel);
            if (this._alin != 0) {
                this._distanciaAgresion = 15;
            }
            short s = this._celdaID = celda == -1 ? Mapa.getRandomCeldaIDLibre() : celda;
            if (this._celdaID == 0) {
                return;
            }
            this._orientacion = (byte)(Fórmulas.getRandomValor(0, 3) * 2);
            this._esFixeado = false;
            this._estrellas = 0;
        }

        public GrupoMobs(int id, short celdaID, String strGrupoMob) {
            this._id = id;
            this._alin = 0;
            this._celdaID = celdaID;
            this._distanciaAgresion = Constantes.agresionPorNivel(0);
            this._esFixeado = true;
            this._strGrupoMob = strGrupoMob;
            int idMob = -1;
            for (String data : strGrupoMob.split(";")) {
                String[] infos = data.split(",");
                try {
                    int idMobModelo = Integer.parseInt(infos[0]);
                    int min = Integer.parseInt(infos[1]);
                    int max = Integer.parseInt(infos[2]);
                    MOB_tmpl m = World.getMobModelo(idMobModelo);
                    ArrayList<MobGrado> mgs = new ArrayList<MobGrado>();
                    for (MobGrado MG : m.getGrados().values()) {
                        if (MG._nivel < min || MG._nivel > max) continue;
                        mgs.add(MG);
                    }
                    if (mgs.isEmpty()) continue;
                    if (this._almas.containsKey(idMobModelo)) {
                        int valor = this._almas.get(idMobModelo);
                        this._almas.remove(idMobModelo);
                        this._almas.put(idMobModelo, valor + 1);
                    } else {
                        this._almas.put(idMobModelo, 1);
                    }
                    this._mobs.put(idMob, (MobGrado)mgs.get(Fórmulas.getRandomValor(0, mgs.size() - 1)));
                    --idMob;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            this._orientacion = (byte)(Fórmulas.getRandomValor(0, 3) * 2 + 1);
        }

        public int getID() {
            return this._id;
        }

        public String getStrGrupoMob() {
            return this._strGrupoMob;
        }

        public void setMuerto(boolean muerto) {
            this._muerto = muerto;
        }

        public boolean estaMuerto() {
            return this._muerto;
        }

        public short getEstrellas() {
            return this._estrellas;
        }

        public void setEstrellas(short estre) {
            this._estrellas = estre;
        }

        public void aumentarEstrellas() {
            if (this._estrellas == 0) {
                this._estrellas = 1;
                return;
            }
            this._estrellas = (short)(this._estrellas + 25);
            if (this._estrellas > 1025) {
                this._estrellas = 0;
            }
        }

        public void aumentarEstrellasCant(int cant) {
            this._estrellas = (short)(this._estrellas + cant);
            if (this._estrellas > 1025) {
                this._estrellas = 0;
            }
        }

        public short getCeldaID() {
            return this._celdaID;
        }

        public byte getOrientacion() {
            return this._orientacion;
        }

        public int getDistanciaAgresion() {
            return this._distanciaAgresion;
        }

        public boolean esPermanente() {
            return this._esFixeado;
        }

        public void setOrientacion(byte o) {
            this._orientacion = 0;
        }

        public void setCeldaID(short id) {
            this._celdaID = id;
        }

        public byte getAlineamiento() {
            return this._alin;
        }

        public MobGrado getMobGradoPorID(int id) {
            return this._mobs.get(id);
        }

        public int getCantMobs() {
            return this._mobs.size();
        }

        public String enviarGM() {
            String mobIDs = "";
            String mobGFX = "";
            String mobNiveles = "";
            String colores = "";
            boolean primero = true;
            if (this._mobs.isEmpty()) {
                return "";
            }
            for (Map.Entry<Integer, MobGrado> entry : this._mobs.entrySet()) {
                if (!primero) {
                    mobIDs = String.valueOf(mobIDs) + ",";
                    mobGFX = String.valueOf(mobGFX) + ",";
                    mobNiveles = String.valueOf(mobNiveles) + ",";
                }
                MOB_tmpl mob = entry.getValue().getModelo();
                mobIDs = String.valueOf(mobIDs) + mob.getID();
                mobGFX = String.valueOf(mobGFX) + mob.getGfxID() + "^100";
                mobNiveles = String.valueOf(mobNiveles) + entry.getValue().getNivel();
                colores = String.valueOf(colores) + mob.getColores() + ";0,0,0,0;";
                primero = false;
            }
            return "+" + this._celdaID + ";" + this._orientacion + ";" + this._estrellas + ";" + this._id + ";" + mobIDs + ";-3;" + mobGFX + ";" + mobNiveles + ";" + colores;
        }

        public Map<Integer, MobGrado> getMobs() {
            return this._mobs;
        }

        public Map<Integer, Integer> getAlmasMobs() {
            return this._almas;
        }

        public void setCondicion(String cond) {
            this._condicion = cond;
        }

        public String getCondicion() {
            return this._condicion;
        }

        public void setEsPermanente(boolean fix) {
            this._esFixeado = fix;
        }

        public void inicioTiempoCondicion() {
            this._tiempoCondicion = new Timer();
            this._tiempoCondicion.schedule(new TimerTask(){

                @Override
                public void run() {
                    GrupoMobs.this._condicion = "";
                }
            }, LesGuardians.TIEMPO_ARENA);
        }

        public void stopConditionTimer() {
            try {
                this._tiempoCondicion.cancel();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static class MobGrado {
        private MOB_tmpl _modelo;
        private int _grado;
        private short _nivel;
        private int _PDV;
        private int _idEnPelea;
        private int _PDVMAX;
        private int _iniciativa;
        private short _PA;
        private short _PM;
        private Maps.Celda _celdaPelea;
        private int _baseXp = 10;
        private Map<Integer, Integer> _stats = new TreeMap<Integer, Integer>();
        private Map<Integer, Spell.StatsHechizos> _hechizos = new TreeMap<Integer, Spell.StatsHechizos>();
        private ArrayList<Objeto> _objetosHeroico = new ArrayList();
        private long _kamasHeroico = 0L;

        public MobGrado(MOB_tmpl modelo, int grado, short nivel, short PA, short PM, String resistencia, String stats, String hechizos, int pdvMax, int iniciativa, int xp) {
            String[] spellsArray;
            this._modelo = modelo;
            this._grado = grado;
            this._nivel = nivel;
            this._PDV = this._PDVMAX = pdvMax;
            this._PA = PA;
            this._PM = PM;
            this._baseXp = xp;
            this._iniciativa = iniciativa;
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
            this._stats.clear();
            this._stats.put(118, fuerza);
            this._stats.put(124, sabiduria);
            this._stats.put(126, inteligencia);
            this._stats.put(123, suerte);
            this._stats.put(119, agilidad);
            this._stats.put(214, RNeutral);
            this._stats.put(213, RFuego);
            this._stats.put(211, RAgua);
            this._stats.put(212, RAire);
            this._stats.put(210, RTierra);
            this._stats.put(160, EPA);
            this._stats.put(161, EPM);
            this._stats.put(111, Integer.valueOf(PA));
            this._stats.put(128, Integer.valueOf(PM));
            if (this._modelo.getID() == 423) {
                this._stats.put(182, 4);
                this._stats.put(105, 100);
            } else {
                this._stats.put(182, 2);
            }
            this._hechizos.clear();
            String[] arrstring = spellsArray = hechizos.split(";");
            int n = spellsArray.length;
            for (int i = 0; i < n; ++i) {
                Spell.StatsHechizos hechizoStats;
                Spell hechizo;
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
                if (hechizoID == 0 || hechizoNivel == 0 || (hechizo = World.getHechizo(hechizoID)) == null || (hechizoStats = hechizo.getStatsPorNivel(hechizoNivel)) == null) continue;
                this._hechizos.put(hechizoID, hechizoStats);
            }
        }

        private MobGrado(MOB_tmpl modelo, int grado, short nivel, int pdv, int pdvmax, short PA, short PM, Map<Integer, Integer> stats, Map<Integer, Spell.StatsHechizos> hechizos, int iniciativa, int xp) {
            this._modelo = modelo;
            this._grado = grado;
            this._nivel = nivel;
            this._PDV = pdv;
            this._PDVMAX = pdvmax;
            this._PA = PA;
            this._PM = PM;
            this._iniciativa = iniciativa;
            this._stats = stats;
            this._hechizos = hechizos;
            this._idEnPelea = -1;
            this._baseXp = xp;
        }

        public long getBaseXp() {
            return this._baseXp;
        }

        public int getIniciativa() {
            return this._iniciativa;
        }

        public MobGrado copiarMob() {
            TreeMap<Integer, Integer> nuevoStats = new TreeMap<Integer, Integer>();
            nuevoStats.putAll(this._stats);
            return new MobGrado(this._modelo, this._grado, this._nivel, this._PDV, this._PDVMAX, this._PA, this._PM, nuevoStats, this._hechizos, this._iniciativa, this._baseXp);
        }

        public Personagens.Stats getStats() {
            return new Personagens.Stats(this._stats);
        }

        public int getNivel() {
            return this._nivel;
        }

        public void addKamasHeroico(long kamas) {
            if (kamas < 1L) {
                return;
            }
            this._kamasHeroico += kamas;
        }

        public long getKamasHeroico() {
            return this._kamasHeroico;
        }

        public void borrarKamasHeroico() {
            this._kamasHeroico = 0L;
        }

        public void addObjetoHeroico(Objeto obj) {
            this._objetosHeroico.add(obj);
        }

        public ArrayList<Objeto> getObjetoHeroico() {
            return this._objetosHeroico;
        }

        public void borrarObjetosHeroico() {
            this._objetosHeroico.clear();
        }

        public Maps.Celda getCeldaPelea() {
            return this._celdaPelea;
        }

        public void setCeldaPelea(Maps.Celda celda) {
            this._celdaPelea = celda;
        }

        public Map<Integer, Spell.StatsHechizos> getHechizos() {
            return this._hechizos;
        }

        public MOB_tmpl getModelo() {
            return this._modelo;
        }

        public int getPDV() {
            return this._PDV;
        }

        public void setPDV(int pdv) {
            this._PDV = pdv;
        }

        public int getPDVMAX() {
            return this._PDVMAX;
        }

        public int getGrado() {
            return this._grado;
        }

        public void setIdEnPelea(int i) {
            this._idEnPelea = i;
        }

        public int getIdEnPelea() {
            return this._idEnPelea;
        }

        public int getPA() {
            return this._PA;
        }

        public int getPM() {
            return this._PM;
        }

        public void modificarStatsPorInvocador(Fight.Luchador invocador) {
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
            Personagens.Stats stats = invocador.getTotalStatsSinBuff();
            this._PDV = (int)((float)this._PDVMAX * coefVita) + stats.getEfecto(125) / 40;
            if (this._PDV < 1) {
                this._PDV = 1;
            }
            this._PDVMAX = this._PDV;
            int fuerza = (int)((float)this._stats.get(118).intValue() + (float)stats.getEfecto(118) * coefStats);
            int inteligencia = (int)((float)this._stats.get(126).intValue() + (float)stats.getEfecto(126) * coefStats);
            int agilidad = (int)((float)this._stats.get(119).intValue() + (float)stats.getEfecto(119) * coefStats);
            int sabiduria = (int)((float)this._stats.get(124).intValue() + (float)stats.getEfecto(124) * coefStats);
            int suerte = (int)((float)this._stats.get(123).intValue() + (float)stats.getEfecto(123) * coefStats);
            this._stats.remove(118);
            this._stats.remove(126);
            this._stats.remove(119);
            this._stats.remove(124);
            this._stats.remove(123);
            this._stats.put(118, fuerza);
            this._stats.put(126, inteligencia);
            this._stats.put(119, agilidad);
            this._stats.put(124, sabiduria);
            this._stats.put(123, suerte);
        }
    }
}

