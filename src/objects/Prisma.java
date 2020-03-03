package objects;

import common.SocketManager;
import common.World;
import java.util.Map;
import java.util.TreeMap;
import objects.Fight;
import objects.Maps;
import objects.Personagens;
import objects.Spell;

public class Prisma {
    private int _id;
    private byte _alineacion;
    private byte _nivel;
    private short _mapaID;
    private short _celda;
    private byte _dir;
    private short _nombre;
    private short _gfx;
    private byte _estadoPelea;
    private short _peleaID;
    private int _tiempoTurno = 45000;
    private int _honor = 0;
    private short _area = (short)-1;
    private Fight _pelea;
    private Map<Integer, Integer> _stats = new TreeMap<Integer, Integer>();
    private Map<Integer, Spell.StatsHechizos> _hechizos = new TreeMap<Integer, Spell.StatsHechizos>();

    public Prisma(int id, byte alineacion, byte nivel, short mapa, short celda, int honor, short area) {
        this._id = id;
        this._alineacion = alineacion;
        this._nivel = nivel;
        this._mapaID = mapa;
        this._celda = celda;
        this._dir = 1;
        if (alineacion == 1) {
            this._nombre = (short)1111;
            this._gfx = (short)8101;
        } else {
            this._nombre = (short)1112;
            this._gfx = (short)8100;
        }
        this._estadoPelea = (byte)-1;
        this._peleaID = (short)-1;
        this._honor = honor;
        this._area = area;
        this._pelea = null;
    }

    public int getID() {
        return this._id;
    }

    public short getAreaConquistada() {
        return this._area;
    }

    public void setAreaConquistada(short area) {
        this._area = area;
    }

    public byte getAlineacion() {
        return this._alineacion;
    }

    public byte getNivel() {
        return this._nivel;
    }

    public short getMapaID() {
        return this._mapaID;
    }

    public short getCeldaID() {
        return this._celda;
    }

    public Personagens.Stats getStats() {
        return new Personagens.Stats(this._stats);
    }

    public Map<Integer, Spell.StatsHechizos> getHechizos() {
        return this._hechizos;
    }

    public void actualizarStats() {
        String[] arrayHechizos;
        int fuerza = 1000 + 500 * this._nivel;
        int inteligencia = 1000 + 500 * this._nivel;
        int agilidad = 1000 + 500 * this._nivel;
        int sabiduria = 1000 + 500 * this._nivel;
        int suerte = 1000 + 500 * this._nivel;
        int resistencia = 9 * this._nivel;
        this._stats.clear();
        this._stats.put(118, fuerza);
        this._stats.put(126, inteligencia);
        this._stats.put(119, agilidad);
        this._stats.put(124, sabiduria);
        this._stats.put(123, suerte);
        this._stats.put(214, resistencia);
        this._stats.put(213, resistencia);
        this._stats.put(211, resistencia);
        this._stats.put(212, resistencia);
        this._stats.put(210, resistencia);
        this._stats.put(160, resistencia);
        this._stats.put(161, resistencia);
        this._stats.put(111, 12);
        this._stats.put(128, 0);
        this._hechizos.clear();
        String hechizos = "56@6;24@6;157@6;63@6;8@6;81@6";
        String[] arrstring = arrayHechizos = hechizos.split(";");
        int n = arrayHechizos.length;
        for (int i = 0; i < n; ++i) {
            Spell.StatsHechizos hechizoStats;
            Spell hechizo;
            String str = arrstring[i];
            if (str.equals("")) continue;
            String[] hechizoInfo = str.split("@");
            int hechizoID = 0;
            int hechizoNivel = 0;
            try {
                hechizoID = Integer.parseInt(hechizoInfo[0]);
                hechizoNivel = Integer.parseInt(hechizoInfo[1]);
            }
            catch (Exception e) {
                continue;
            }
            if (hechizoID == 0 || hechizoNivel == 0 || (hechizo = World.getHechizo(hechizoID)) == null || (hechizoStats = hechizo.getStatsPorNivel(hechizoNivel)) == null) continue;
            this._hechizos.put(hechizoID, hechizoStats);
        }
    }

    public void setNivel(byte nivel) {
        this._nivel = nivel;
    }

    public byte getEstadoPelea() {
        return this._estadoPelea;
    }

    public void setEstadoPelea(byte pelea) {
        this._estadoPelea = pelea;
    }

    public short getPeleaID() {
        return this._peleaID;
    }

    public void setPeleaID(short pelea) {
        this._peleaID = pelea;
    }

    public void setPelea(Fight pelea) {
        this._pelea = pelea;
    }

    public Fight getPelea() {
        return this._pelea;
    }

    public void descontarTiempoTurno(int tiempo) {
        this._tiempoTurno -= tiempo;
    }

    public void setTiempoTurno(int tiempo) {
        this._tiempoTurno = tiempo;
    }

    public int getTiempoTurno() {
        return this._tiempoTurno;
    }

    public byte getX() {
        Maps mapa = World.getMapa(this._mapaID);
        return mapa.getX();
    }

    public byte getY() {
        Maps mapa = World.getMapa(this._mapaID);
        return mapa.getY();
    }

    public World.SubArea getSubArea() {
        Maps mapa = World.getMapa(this._mapaID);
        return mapa.getSubArea();
    }

    public World.Area getArea() {
        Maps mapa = World.getMapa(this._mapaID);
        return mapa.getSubArea().getArea();
    }

    public int getAlinSubArea() {
        Maps mapa = World.getMapa(this._mapaID);
        return mapa.getSubArea().getAlineacion();
    }

    public int getAlinArea() {
        Maps mapa = World.getMapa(this._mapaID);
        return mapa.getSubArea().getAlineacion();
    }

    public int getHonor() {
        return this._honor;
    }

    public void addHonor(int honor) {
        this._honor += honor;
        if (this._honor >= 25000) {
            this._nivel = (byte)10;
            this._honor = 25000;
        }
        for (int n = 1; n <= 10; ++n) {
            if (this._honor >= World.getExpNivel((int)n)._pvp) continue;
            this._nivel = (byte)(n - 1);
            break;
        }
    }

    public void setCelda(short celda) {
        this._celda = celda;
    }

    public String getGMPrisma() {
        if (this._estadoPelea != -1) {
            return "";
        }
        String str = "GM|+";
        str = String.valueOf(str) + this._celda + ";";
        str = String.valueOf(str) + this._dir + ";0;" + this._id + ";" + this._nombre + ";-10;" + this._gfx + "^100;" + this._nivel + ";" + this._nivel + ";" + this._alineacion;
        return str;
    }

    public static void analizarAtaque(Personagens perso) {
        for (Prisma prisma : World.todosPrismas()) {
            if (prisma._estadoPelea != 0 && prisma._estadoPelea != -2 || perso.getAlineacion() != prisma.getAlineacion()) continue;
            SocketManager.ENVIAR_Cp_INFO_ATACANTES_PRISMA(perso, Prisma.atacantesDePrisma(prisma._id, prisma._mapaID, prisma._peleaID));
        }
    }

    public static void analizarDefensa(Personagens perso) {
        for (Prisma prisma : World.todosPrismas()) {
            if (prisma._estadoPelea != 0 && prisma._estadoPelea != -2 || perso.getAlineacion() != prisma.getAlineacion()) continue;
            SocketManager.ENVIAR_CP_INFO_DEFENSORES_PRISMA(perso, Prisma.defensoresDePrisma(prisma._id, prisma._mapaID, prisma._peleaID));
        }
    }

    public static String atacantesDePrisma(int id, short mapaId, int peleaId) {
        String str = "+";
        str = String.valueOf(str) + Integer.toString(id, 36);
        for (Map.Entry<Short, Fight> pelea : World.getMapa(mapaId).getPeleas().entrySet()) {
            if (pelea.getValue().getID() != peleaId) continue;
            for (Fight.Luchador luchador : pelea.getValue().luchadoresDeEquipo(1)) {
                if (luchador.getPersonaje() == null) continue;
                str = String.valueOf(str) + "|";
                str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getID(), 36) + ";";
                str = String.valueOf(str) + luchador.getPersonaje().getNombre() + ";";
                str = String.valueOf(str) + luchador.getPersonaje().getNivel() + ";";
                str = String.valueOf(str) + "0;";
            }
        }
        return str;
    }

    public static String defensoresDePrisma(int id, short mapaId, int peleaId) {
        String str = "+";
        String stra = "";
        str = String.valueOf(str) + Integer.toString(id, 36);
        for (Map.Entry<Short, Fight> pelea : World.getMapa(mapaId).getPeleas().entrySet()) {
            if (pelea.getValue().getID() != peleaId) continue;
            for (Fight.Luchador luchador : pelea.getValue().luchadoresDeEquipo(2)) {
                if (luchador.getPersonaje() == null) continue;
                str = String.valueOf(str) + "|";
                str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getID(), 36) + ";";
                str = String.valueOf(str) + luchador.getPersonaje().getNombre() + ";";
                str = String.valueOf(str) + luchador.getPersonaje().getGfxID() + ";";
                str = String.valueOf(str) + luchador.getPersonaje().getNivel() + ";";
                str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getColor1(), 36) + ";";
                str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getColor2(), 36) + ";";
                str = String.valueOf(str) + Integer.toString(luchador.getPersonaje().getColor3(), 36) + ";";
                str = pelea.getValue().luchadoresDeEquipo(2).size() > 7 ? String.valueOf(str) + "1;" : String.valueOf(str) + "0;";
            }
            stra = str.substring(1);
            stra = "-" + stra;
            pelea.getValue().setListaDefensores(stra);
        }
        return str;
    }
}

