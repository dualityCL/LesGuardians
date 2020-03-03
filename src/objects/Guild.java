package objects;

import common.Constantes;
import common.SQLManager;
import common.World;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Casa;
import objects.Coletor;
import objects.Dragossauros;
import objects.Maps;
import objects.Personagens;
import objects.Spell;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class Guild {
    private int _id;
    private String _nombre = "";
    private String _emblema = "";
    private Map<Integer, MiembroGremio> _miembros = new TreeMap<Integer, MiembroGremio>();
    private int _nvl;
    private long _xp;
    private int _capital = 0;
    private int _nroRecaudadores = 0;
    private Map<Integer, Spell.StatsHechizos> _hechizos = new TreeMap<Integer, Spell.StatsHechizos>();
    private Map<Integer, Integer> _stats = new TreeMap<Integer, Integer>();
    private Map<Integer, Integer> _statsPelea = new TreeMap<Integer, Integer>();

    public Guild(Personagens due\u00f1o, String nombre, String emblema) {
        this._id = World.getSigIdGremio();
        this._nombre = nombre;
        this._emblema = emblema;
        this._nvl = 1;
        this._xp = 0L;
        this.decompilarHechizos("462;0|461;0|460;0|459;0|458;0|457;0|456;0|455;0|454;0|453;0|452;0|451;0|");
        this.decompilarStats("176;100|158;1000|124;100|");
    }

    public Guild(int id, String nombre, String emblema, int nivel, long xp, int capital, int nroMaxRecau, String hechizos, String stats) {
        this._id = id;
        this._nombre = nombre;
        this._emblema = emblema;
        this._xp = xp;
        this._nvl = nivel;
        this._capital = capital;
        this._nroRecaudadores = nroMaxRecau;
        this.decompilarHechizos(hechizos);
        this.decompilarStats(stats);
        this._statsPelea.clear();
        this._statsPelea.put(111, 7);
        this._statsPelea.put(128, 4);
        this._statsPelea.put(118, this._nvl * 9);
        this._statsPelea.put(124, this.getStats(124));
        this._statsPelea.put(126, this._nvl * 9);
        this._statsPelea.put(123, this._nvl * 9);
        this._statsPelea.put(119, this._nvl * 9);
        this._statsPelea.put(214, (int)Math.floor(this.getNivel() / 3));
        this._statsPelea.put(213, (int)Math.floor(this.getNivel() / 3));
        this._statsPelea.put(211, (int)Math.floor(this.getNivel() / 3));
        this._statsPelea.put(212, (int)Math.floor(this.getNivel() / 3));
        this._statsPelea.put(210, (int)Math.floor(this.getNivel() / 3));
        this._statsPelea.put(160, (int)Math.floor(this.getNivel() / 3));
        this._statsPelea.put(161, (int)Math.floor(this.getNivel() / 3));
    }

    public MiembroGremio addMiembro(int id, String nombre, int nivel, int gfx, int r, byte pXp, long x, int derechos, String ultConeccion) {
        MiembroGremio miembro = new MiembroGremio(id, this, nombre, nivel, gfx, r, x, pXp, derechos, ultConeccion);
        this._miembros.put(id, miembro);
        return miembro;
    }

    public MiembroGremio addNuevoMiembro(Personagens perso) {
        MiembroGremio miembro = new MiembroGremio(perso.getID(), this, perso.getNombre(), perso.getNivel(), perso.getGfxID(), 0, 0L, (byte) 0, 0, perso.getCuenta().getUltimaConeccion());
        this._miembros.put(perso.getID(), miembro);
        return miembro;
    }

    public int getID() {
        return this._id;
    }

    public int getNroRecau() {
        return this._nroRecaudadores;
    }

    public String getInfoGremio() {
        String str = String.valueOf(this._nombre) + "," + this.getStats(158) + "," + this.getStats(176) + "," + this.getStats(124) + "," + this.getRecauColocados();
        return str;
    }

    public String getInfo() {
        String str = this._nombre;
        return str;
    }

    public void setNroRecau(int nro) {
        this._nroRecaudadores = nro;
    }

    public int getRecauColocados() {
        int numero = 0;
        for (Coletor perco : World.getTodosRecaudadores().values()) {
            if (perco.getGremioID() != this._id) continue;
            ++numero;
        }
        return numero;
    }

    public int getCapital() {
        return this._capital;
    }

    public void setCapital(int nro) {
        this._capital = nro;
    }

    public Map<Integer, Spell.StatsHechizos> getHechizos() {
        return this._hechizos;
    }

    public Map<Integer, Integer> getStats() {
        return this._stats;
    }

    public void addStat(int stat, int cant) {
        int vieja = this._stats.get(stat);
        this._stats.put(stat, vieja + cant);
    }

    public void boostHechizo(int ID) {
        Spell.StatsHechizos SS = this._hechizos.get(ID);
        if (SS != null && SS.getNivel() == 5) {
            return;
        }
        this._hechizos.put(ID, SS == null ? World.getHechizo(ID).getStatsPorNivel(1) : World.getHechizo(ID).getStatsPorNivel(SS.getNivel() + 1));
    }

    public Personagens.Stats getStatsPelea() {
        return new Personagens.Stats(this._statsPelea);
    }

    public String getNombre() {
        return this._nombre;
    }

    public String getEmblema() {
        return this._emblema;
    }

    public long getXP() {
        return this._xp;
    }

    public int getNivel() {
        return this._nvl;
    }

    public int getSize() {
        return this._miembros.size();
    }

    public String analizarMiembrosGM() {
        String str = "";
        for (MiembroGremio GM : this._miembros.values()) {
            String enLinea = "0";
            if (GM.getPerso() == null) continue;
            if (GM.getPerso().enLinea()) {
                enLinea = "1";
            }
            if (str.length() != 0) {
                str = String.valueOf(str) + "|";
            }
            str = String.valueOf(str) + GM.getID() + ";";
            str = String.valueOf(str) + GM.getNombre() + ";";
            str = String.valueOf(str) + GM.getNivel() + ";";
            str = String.valueOf(str) + GM.getGfx() + ";";
            str = String.valueOf(str) + GM.getRango() + ";";
            str = String.valueOf(str) + GM.getXpDonada() + ";";
            str = String.valueOf(str) + GM.getPorcXpDonada() + ";";
            str = String.valueOf(str) + GM.getDerechos() + ";";
            str = String.valueOf(str) + enLinea + ";";
            str = String.valueOf(str) + GM.getPerso().getAlineacion() + ";";
            str = String.valueOf(str) + GM.getHorasDeUltimaConeccion();
        }
        return str;
    }

    public ArrayList<Personagens> getPjMiembros() {
        ArrayList<Personagens> a = new ArrayList<Personagens>();
        for (MiembroGremio MG : this._miembros.values()) {
            a.add(MG.getPerso());
        }
        return a;
    }

    public ArrayList<MiembroGremio> getTodosMiembros() {
        ArrayList<MiembroGremio> a = new ArrayList<MiembroGremio>();
        for (MiembroGremio MG : this._miembros.values()) {
            a.add(MG);
        }
        return a;
    }

    public MiembroGremio getMiembro(int idMiembro) {
        return this._miembros.get(idMiembro);
    }

    public void expulsarMiembro(Personagens perso) {
        Casa casa = Casa.getCasaDePj(perso);
        if (casa != null && Casa.casaGremio(this._id) > 0) {
            SQLManager.UPDATE_CASA_GREMIO(casa, 0, 0);
        }
        this._miembros.remove(perso.getID());
        SQLManager.DELETE_MIEMBRO_GREMIO(perso.getID());
    }

    public void addXp(long xp) {
        this._xp += xp;
        while (this._xp >= World.getXPMaxGremio(this._nvl) && this._nvl < 200) {
            this.subirNivel();
        }
    }

    public void subirNivel() {
        ++this._nvl;
        this._capital += 5;
    }

    public String analizarInfoCercados() {
        byte maxCercados = (byte)Math.floor(this._nvl / 10);
        String packet = String.valueOf(maxCercados);
        for (Maps.Cercado cercados : World.todosCercados()) {
            if (cercados.getGremio() != this) continue;
            packet = String.valueOf(packet) + "|" + cercados.getMapa().getID() + ";" + cercados.getTama\u00f1o() + ";" + cercados.getCantObjMax();
            if (cercados.getListaCriando().size() <= 0) continue;
            packet = String.valueOf(packet) + ";";
            boolean primero = false;
            for (Integer monturas : cercados.getListaCriando()) {
                Dragossauros montura = World.getDragopavoPorID(monturas);
                if (montura == null) continue;
                if (primero) {
                    packet = String.valueOf(packet) + ",";
                }
                primero = true;
                packet = World.getPersonaje(montura.getDue\u00f1o()) == null ? String.valueOf(packet) + montura.getColor() + "," + montura.getNombre() + ",SIN DUE\u00c3\u2018O" : String.valueOf(packet) + montura.getColor() + "," + montura.getNombre() + "," + World.getPersonaje(montura.getDue\u00f1o()).getNombre();
            }
        }
        return packet;
    }

    public void decompilarHechizos(String strHechizo) {
        for (String split : strHechizo.split("\\|")) {
            int id = Integer.parseInt(split.split(";")[0]);
            int nivel = Integer.parseInt(split.split(";")[1]);
            this._hechizos.put(id, World.getHechizo(id).getStatsPorNivel(nivel));
        }
    }

    public void decompilarStats(String statsStr) {
        for (String split : statsStr.split("\\|")) {
            int id = Integer.parseInt(split.split(";")[0]);
            int value = Integer.parseInt(split.split(";")[1]);
            this._stats.put(id, value);
        }
    }

    public String compilarHechizo() {
        String str = "";
        boolean primero = true;
        for (Map.Entry<Integer, Spell.StatsHechizos> statHechizo : this._hechizos.entrySet()) {
            if (!primero) {
                str = String.valueOf(str) + "|";
            }
            str = String.valueOf(str) + statHechizo.getKey() + ";" + (statHechizo.getValue() == null ? 0 : statHechizo.getValue().getNivel());
            primero = false;
        }
        return str;
    }

    public String compilarStats() {
        String str = "";
        boolean primero = true;
        for (Map.Entry<Integer, Integer> stats : this._stats.entrySet()) {
            if (!primero) {
                str = String.valueOf(str) + "|";
            }
            str = String.valueOf(str) + stats.getKey() + ";" + stats.getValue();
            primero = false;
        }
        return str;
    }

    public void actualizarStats(int statsID, int add) {
        int actual = this._stats.get(statsID);
        this._stats.put(statsID, actual + add);
    }

    public int getStats(int statsID) {
        int valor = 0;
        for (Map.Entry<Integer, Integer> tempStats : this._stats.entrySet()) {
            if (tempStats.getKey() != statsID) continue;
            valor = tempStats.getValue();
        }
        return valor;
    }

    public String analizarRecauAGrmio() {
        String packet = String.valueOf(this.getNroRecau()) + "|" + World.cantRecauDelGremio(this.getID()) + "|" + 100 * this.getNivel() + "|" + this.getNivel() + "|" + this.getStats(158) + "|" + this.getStats(176) + "|" + this.getStats(124) + "|" + this.getNroRecau() + "|" + this.getCapital() + "|" + (1000 + 10 * this.getNivel()) + "|" + this.compilarHechizo();
        return packet;
    }

    public static class MiembroGremio {
        private int _id;
        private Guild _gremio;
        private String _nombre;
        private int _nivel;
        private int _gfx;
        private int _rango = 0;
        private byte _porcXpDonada = 0;
        private long _xpDonada = 0L;
        private int _derechos = 0;
        private String _ultConeccion;
        private Map<Integer, Boolean> tieneDerecho = new TreeMap<Integer, Boolean>();

        public MiembroGremio(int id, Guild gremio, String nombre, int nivel, int gfx, int r, long x, byte pXp, int derechos, String ultConeccion) {
            this._id = id;
            this._gremio = gremio;
            this._nombre = nombre;
            this._nivel = nivel;
            this._gfx = gfx;
            this._rango = r;
            this._xpDonada = x;
            this._porcXpDonada = pXp;
            this._derechos = derechos;
            this._ultConeccion = ultConeccion;
            this.convertirDerechosAInt(this._derechos);
            Personagens perso = World.getPersonaje(id);
            if (perso != null) {
                perso.setMiembroGremio(this);
            }
        }

        public int getGfx() {
            return this._gfx;
        }

        public int getNivel() {
            return this._nivel;
        }

        public String getNombre() {
            return this._nombre;
        }

        public String getVerdaderoNombre() {
            Personagens perso = World.getPersonaje(this._id);
            if (perso == null) {
                return this._nombre;
            }
            this._nombre = perso.getNombre();
            return this._nombre;
        }

        public int getID() {
            return this._id;
        }

        public int getRango() {
            return this._rango;
        }

        public Guild getGremio() {
            return this._gremio;
        }

        public String analizarDerechos() {
            return Integer.toString(this._derechos, 36);
        }

        public int getDerechos() {
            return this._derechos;
        }

        public long getXpDonada() {
            return this._xpDonada;
        }

        public int getPorcXpDonada() {
            return this._porcXpDonada;
        }

        public String getUltimaConeccion() {
            return this._ultConeccion;
        }

        public int getHorasDeUltimaConeccion() {
            String[] strFecha = this._ultConeccion.toString().split("~");
            LocalDate ultConeccion = new LocalDate(Integer.parseInt(strFecha[0]), Integer.parseInt(strFecha[1]), Integer.parseInt(strFecha[2]));
            LocalDate ahora = new LocalDate();
            return Days.daysBetween(ultConeccion, ahora).getDays() * 24;
        }

        public Personagens getPerso() {
            return World.getPersonaje(this._id);
        }

        public boolean puede(int derecho) {
            if (this._derechos == 1) {
                return true;
            }
            return this.tieneDerecho.get(derecho);
        }

        public void setRango(int i) {
            this._rango = i;
        }

        public void setTodosDerechos(int rank, byte xp, int derechos) {
            if (rank == -1) {
                rank = this._rango;
            }
            if (xp < 0) {
                xp = this._porcXpDonada;
            }
            if (xp > 90) {
                xp = (byte)90;
            }
            if (derechos == -1) {
                derechos = this._derechos;
            }
            this._rango = rank;
            this._porcXpDonada = xp;
            if (derechos != this._derechos && derechos != 1) {
                this.convertirDerechosAInt(derechos);
            }
            this._derechos = derechos;
        }

        public void setNivel(int nivel) {
            this._nivel = nivel;
        }

        public void darXpAGremio(long xp) {
            this._xpDonada += xp;
            this._gremio.addXp(xp);
        }

        public void derechosIniciales() {
            this.tieneDerecho.put(Constantes.G_MODIFBOOST, false);
            this.tieneDerecho.put(Constantes.G_MODIFDERECHOS, false);
            this.tieneDerecho.put(Constantes.G_INVITAR, false);
            this.tieneDerecho.put(Constantes.G_BANEAR, false);
            this.tieneDerecho.put(Constantes.G_TODASXPDONADAS, false);
            this.tieneDerecho.put(Constantes.G_SUXPDONADA, false);
            this.tieneDerecho.put(Constantes.G_MODRANGOS, false);
            this.tieneDerecho.put(Constantes.G_PONERRECAUDADOR, false);
            this.tieneDerecho.put(Constantes.G_RECOLECTARRECAUDADOR, false);
            this.tieneDerecho.put(Constantes.G_USARCERCADOS, false);
            this.tieneDerecho.put(Constantes.G_MEJORARCERCADOS, false);
            this.tieneDerecho.put(Constantes.G_OTRASMONTURAS, false);
        }

        public void convertirDerechosAInt(int total) {
            if (this.tieneDerecho.isEmpty()) {
                this.derechosIniciales();
            }
            if (total == 1) {
                return;
            }
            if (this.tieneDerecho.size() > 0) {
                this.tieneDerecho.clear();
            }
            this.derechosIniciales();
            Integer[] mapKey = this.tieneDerecho.keySet().toArray(new Integer[this.tieneDerecho.size()]);
            block0 : while (total > 0) {
                for (int i = this.tieneDerecho.size() - 1; i < this.tieneDerecho.size(); --i) {
                    if (mapKey[i] > total) continue;
                    total ^= mapKey[i].intValue();
                    this.tieneDerecho.put(mapKey[i], true);
                    continue block0;
                }
            }
        }

        public void setUltConeccion(String ultConeccion) {
            this._ultConeccion = ultConeccion;
        }
    }
}

