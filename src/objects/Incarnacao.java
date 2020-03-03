package objects;

import common.SocketManager;
import common.World;
import java.util.Map;
import java.util.TreeMap;
import objects.Personagens;
import objects.Spell;

public class Incarnacao {
    private int _id;
    private int _clase;
    private long _experiencia;
    private int _nivel;
    private int _segundos;
    private int _gfx;
    private Map<Integer, Spell.StatsHechizos> _hechizos = new TreeMap<Integer, Spell.StatsHechizos>();
    private Map<Integer, Character> _lugaresHechizos = new TreeMap<Integer, Character>();

    public Incarnacao(int id, int clase, int nivel, long experiencia, int segundos, String hechizos) {
        this._id = id;
        this._clase = clase;
        this._nivel = nivel;
        this._experiencia = experiencia;
        this._gfx = this.getGFXPorClase();
        this._segundos = segundos;
        this.hechizosClase(this._clase, this._nivel / 10 + 1);
        if (hechizos.isEmpty()) {
            hechizos = this.stringHechizosABD();
        }
        this.analizarPosHechizos(hechizos);
    }

    public int getGFXPorClase() {
        switch (this._clase) {
            case 14: {
                return 1701;
            }
            case 13: {
                return 1702;
            }
            case 17: {
                return 1700;
            }
            case 16: {
                return 1704;
            }
            case 15: {
                return 1703;
            }
            case 18: {
                return 8034;
            }
            case 19: {
                return 8032;
            }
            case 20: {
                return 8033;
            }
            case 21: {
                return 8035;
            }
        }
        return 9999;
    }

    public boolean sePuedePoner(int segundos) {
        int resta = Math.abs(this._segundos - segundos);
        return resta > 60;
    }

    public Map<Integer, Spell.StatsHechizos> getMapHechizos() {
        return this._hechizos;
    }

    public void subirNivel(Personagens perso) {
        if (this._nivel == 50) {
            return;
        }
        ++this._nivel;
        if (this._nivel % 10 == 0) {
            this.hechizosClase(this._clase, this._nivel / 10 + 1);
            SocketManager.enviar(perso, this.actualizarNivelHechizos(this._nivel / 10 + 1, true));
        }
        SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(perso, World.getObjeto(this._id));
        perso.setPDVMAX(this.getPDVMAX());
        perso.fullPDV();
        perso.actualizarInfoGrupo();
        SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
    }

    public String actualizarNivelHechizos(int nivel, boolean encarnacion) {
        boolean primero = true;
        String packet = "";
        for (Spell.StatsHechizos SH : this.getMapHechizos().values()) {
            if (!primero) {
                packet = String.valueOf(packet) + '\u0000';
            }
            packet = encarnacion ? String.valueOf(packet) + "SUK" + SH.getHechizoID() + "~" + nivel : String.valueOf(packet) + "SUK" + SH.getHechizoID() + "~" + SH.getNivel();
            primero = false;
        }
        return packet;
    }

    public int getPDVMAX() {
        return 50 + this._nivel * 20;
    }

    public void setSegundos(int segundos) {
        this._segundos = segundos;
    }

    public void addExp(long xp, Personagens perso) {
        this._experiencia += xp;
        while (this._experiencia >= World.getExpMaxPersonaje(this._nivel) && this._nivel < 50) {
            this.subirNivel(perso);
        }
        SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
    }

    public String stringHechizosABD() {
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
            String e = arrstring[i];
            try {
                int id = Integer.parseInt(e.split(";")[0]);
                char pos = e.split(";")[2].charAt(0);
                this.hechizosClase(this._clase, this._nivel / 10 + 1);
                this._lugaresHechizos.put(id, Character.valueOf(pos));
                continue;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
    }

    public void hechizosClase(int clase, int nivel) {
        this._hechizos = new TreeMap<Integer, Spell.StatsHechizos>();
        switch (clase) {
            case 14: {
                this._hechizos.put(1291, World.getHechizo(1291).getStatsPorNivel(nivel));
                this._hechizos.put(1296, World.getHechizo(1296).getStatsPorNivel(nivel));
                this._hechizos.put(1289, World.getHechizo(1289).getStatsPorNivel(nivel));
                this._hechizos.put(1285, World.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1290, World.getHechizo(1290).getStatsPorNivel(nivel));
                break;
            }
            case 13: {
                this._hechizos.put(1299, World.getHechizo(1299).getStatsPorNivel(nivel));
                this._hechizos.put(1288, World.getHechizo(1288).getStatsPorNivel(nivel));
                this._hechizos.put(1297, World.getHechizo(1297).getStatsPorNivel(nivel));
                this._hechizos.put(1285, World.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1298, World.getHechizo(1298).getStatsPorNivel(nivel));
                break;
            }
            case 17: {
                this._hechizos.put(1300, World.getHechizo(1300).getStatsPorNivel(nivel));
                this._hechizos.put(1301, World.getHechizo(1301).getStatsPorNivel(nivel));
                this._hechizos.put(1303, World.getHechizo(1303).getStatsPorNivel(nivel));
                this._hechizos.put(1285, World.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1302, World.getHechizo(1302).getStatsPorNivel(nivel));
                break;
            }
            case 16: {
                this._hechizos.put(1292, World.getHechizo(1292).getStatsPorNivel(nivel));
                this._hechizos.put(1293, World.getHechizo(1293).getStatsPorNivel(nivel));
                this._hechizos.put(1294, World.getHechizo(1294).getStatsPorNivel(nivel));
                this._hechizos.put(1285, World.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1295, World.getHechizo(1295).getStatsPorNivel(nivel));
                break;
            }
            case 15: {
                this._hechizos.put(1283, World.getHechizo(1283).getStatsPorNivel(nivel));
                this._hechizos.put(1284, World.getHechizo(1284).getStatsPorNivel(nivel));
                this._hechizos.put(1286, World.getHechizo(1286).getStatsPorNivel(nivel));
                this._hechizos.put(1285, World.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1287, World.getHechizo(1287).getStatsPorNivel(nivel));
                break;
            }
            case 18: {
                this._hechizos.put(1601, World.getHechizo(1601).getStatsPorNivel(nivel));
                this._hechizos.put(1602, World.getHechizo(1602).getStatsPorNivel(nivel));
                this._hechizos.put(1603, World.getHechizo(1603).getStatsPorNivel(nivel));
                this._hechizos.put(1604, World.getHechizo(1604).getStatsPorNivel(nivel));
                this._hechizos.put(1605, World.getHechizo(1605).getStatsPorNivel(nivel));
                this._hechizos.put(1606, World.getHechizo(1606).getStatsPorNivel(nivel));
                this._hechizos.put(1607, World.getHechizo(1607).getStatsPorNivel(nivel));
                this._hechizos.put(1608, World.getHechizo(1608).getStatsPorNivel(nivel));
                this._hechizos.put(1609, World.getHechizo(1609).getStatsPorNivel(nivel));
                this._hechizos.put(1610, World.getHechizo(1610).getStatsPorNivel(nivel));
                this._hechizos.put(1611, World.getHechizo(1611).getStatsPorNivel(nivel));
                this._hechizos.put(1612, World.getHechizo(1612).getStatsPorNivel(nivel));
                this._hechizos.put(1613, World.getHechizo(1613).getStatsPorNivel(nivel));
                this._hechizos.put(1614, World.getHechizo(1614).getStatsPorNivel(nivel));
                this._hechizos.put(1615, World.getHechizo(1615).getStatsPorNivel(nivel));
                this._hechizos.put(1616, World.getHechizo(1616).getStatsPorNivel(nivel));
                this._hechizos.put(1617, World.getHechizo(1617).getStatsPorNivel(nivel));
                this._hechizos.put(1618, World.getHechizo(1618).getStatsPorNivel(nivel));
                this._hechizos.put(1619, World.getHechizo(1619).getStatsPorNivel(nivel));
                this._hechizos.put(1620, World.getHechizo(1620).getStatsPorNivel(nivel));
                break;
            }
            case 19: {
                this._hechizos.put(1561, World.getHechizo(1561).getStatsPorNivel(nivel));
                this._hechizos.put(1562, World.getHechizo(1562).getStatsPorNivel(nivel));
                this._hechizos.put(1563, World.getHechizo(1563).getStatsPorNivel(nivel));
                this._hechizos.put(1564, World.getHechizo(1564).getStatsPorNivel(nivel));
                this._hechizos.put(1565, World.getHechizo(1565).getStatsPorNivel(nivel));
                this._hechizos.put(1566, World.getHechizo(1566).getStatsPorNivel(nivel));
                this._hechizos.put(1567, World.getHechizo(1567).getStatsPorNivel(nivel));
                this._hechizos.put(1568, World.getHechizo(1568).getStatsPorNivel(nivel));
                this._hechizos.put(1569, World.getHechizo(1569).getStatsPorNivel(nivel));
                this._hechizos.put(1570, World.getHechizo(1570).getStatsPorNivel(nivel));
                this._hechizos.put(1571, World.getHechizo(1571).getStatsPorNivel(nivel));
                this._hechizos.put(1572, World.getHechizo(1572).getStatsPorNivel(nivel));
                this._hechizos.put(1573, World.getHechizo(1573).getStatsPorNivel(nivel));
                this._hechizos.put(1574, World.getHechizo(1574).getStatsPorNivel(nivel));
                this._hechizos.put(1575, World.getHechizo(1575).getStatsPorNivel(nivel));
                this._hechizos.put(1576, World.getHechizo(1576).getStatsPorNivel(nivel));
                this._hechizos.put(1577, World.getHechizo(1577).getStatsPorNivel(nivel));
                this._hechizos.put(1578, World.getHechizo(1578).getStatsPorNivel(nivel));
                this._hechizos.put(1579, World.getHechizo(1579).getStatsPorNivel(nivel));
                this._hechizos.put(1580, World.getHechizo(1580).getStatsPorNivel(nivel));
                break;
            }
            case 20: {
                this._hechizos.put(1581, World.getHechizo(1581).getStatsPorNivel(nivel));
                this._hechizos.put(1582, World.getHechizo(1582).getStatsPorNivel(nivel));
                this._hechizos.put(1583, World.getHechizo(1583).getStatsPorNivel(nivel));
                this._hechizos.put(1584, World.getHechizo(1584).getStatsPorNivel(nivel));
                this._hechizos.put(1585, World.getHechizo(1585).getStatsPorNivel(nivel));
                this._hechizos.put(1586, World.getHechizo(1586).getStatsPorNivel(nivel));
                this._hechizos.put(1587, World.getHechizo(1587).getStatsPorNivel(nivel));
                this._hechizos.put(1588, World.getHechizo(1588).getStatsPorNivel(nivel));
                this._hechizos.put(1589, World.getHechizo(1589).getStatsPorNivel(nivel));
                this._hechizos.put(1590, World.getHechizo(1590).getStatsPorNivel(nivel));
                this._hechizos.put(1591, World.getHechizo(1591).getStatsPorNivel(nivel));
                this._hechizos.put(1592, World.getHechizo(1592).getStatsPorNivel(nivel));
                this._hechizos.put(1593, World.getHechizo(1593).getStatsPorNivel(nivel));
                this._hechizos.put(1594, World.getHechizo(1594).getStatsPorNivel(nivel));
                this._hechizos.put(1595, World.getHechizo(1595).getStatsPorNivel(nivel));
                this._hechizos.put(1596, World.getHechizo(1596).getStatsPorNivel(nivel));
                this._hechizos.put(1597, World.getHechizo(1597).getStatsPorNivel(nivel));
                this._hechizos.put(1598, World.getHechizo(1598).getStatsPorNivel(nivel));
                this._hechizos.put(1599, World.getHechizo(1599).getStatsPorNivel(nivel));
                this._hechizos.put(1600, World.getHechizo(1600).getStatsPorNivel(nivel));
                break;
            }
            case 21: {
                this._hechizos.put(1541, World.getHechizo(1541).getStatsPorNivel(nivel));
                this._hechizos.put(1542, World.getHechizo(1542).getStatsPorNivel(nivel));
                this._hechizos.put(1543, World.getHechizo(1543).getStatsPorNivel(nivel));
                this._hechizos.put(1544, World.getHechizo(1544).getStatsPorNivel(nivel));
                this._hechizos.put(1545, World.getHechizo(1545).getStatsPorNivel(nivel));
                this._hechizos.put(1546, World.getHechizo(1546).getStatsPorNivel(nivel));
                this._hechizos.put(1547, World.getHechizo(1547).getStatsPorNivel(nivel));
                this._hechizos.put(1548, World.getHechizo(1548).getStatsPorNivel(nivel));
                this._hechizos.put(1549, World.getHechizo(1549).getStatsPorNivel(nivel));
                this._hechizos.put(1550, World.getHechizo(1550).getStatsPorNivel(nivel));
                this._hechizos.put(1551, World.getHechizo(1551).getStatsPorNivel(nivel));
                this._hechizos.put(1552, World.getHechizo(1552).getStatsPorNivel(nivel));
                this._hechizos.put(1553, World.getHechizo(1553).getStatsPorNivel(nivel));
                this._hechizos.put(1554, World.getHechizo(1554).getStatsPorNivel(nivel));
                this._hechizos.put(1555, World.getHechizo(1555).getStatsPorNivel(nivel));
                this._hechizos.put(1556, World.getHechizo(1556).getStatsPorNivel(nivel));
                this._hechizos.put(1557, World.getHechizo(1557).getStatsPorNivel(nivel));
                this._hechizos.put(1558, World.getHechizo(1558).getStatsPorNivel(nivel));
                this._hechizos.put(1559, World.getHechizo(1559).getStatsPorNivel(nivel));
                this._hechizos.put(1560, World.getHechizo(1560).getStatsPorNivel(nivel));
            }
        }
    }

    public boolean tieneHechizoID(int hechizo) {
        return this._hechizos.get(hechizo) != null;
    }

    public String stringListaHechizos() {
        String str = "";
        for (Spell.StatsHechizos SH : this._hechizos.values()) {
            str = this._lugaresHechizos.get(SH.getHechizoID()) == null ? String.valueOf(str) + SH.getHechizoID() + "~" + SH.getNivel() + "~_;" : String.valueOf(str) + SH.getHechizoID() + "~" + SH.getNivel() + "~" + this._lugaresHechizos.get(SH.getHechizoID()) + ";";
        }
        return str;
    }

    public void setPosHechizo(int hechizo, char pos) {
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

    public int getClase() {
        return this._clase;
    }

    public int getID() {
        return this._id;
    }

    public int getGfx() {
        return this._gfx;
    }

    public long getExperiencia() {
        return this._experiencia;
    }

    public int getNivel() {
        return this._nivel;
    }

    public int getSegundos() {
        return this._segundos;
    }

    public Spell.StatsHechizos getStatsHechizo(int hechizoID) {
        return this._hechizos.get(hechizoID);
    }
}

