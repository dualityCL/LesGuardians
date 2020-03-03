package objects;

import common.SocketManager;
import common.Mundo;
import java.util.Map;
import java.util.TreeMap;
import objects.Personaje;
import objects.Hechizo;

public class Encarnación {
    private int _id;
    private int _clase;
    private long _experiencia;
    private int _nivel;
    private int _segundos;
    private int _gfx;
    private Map<Integer, Hechizo.StatsHechizos> _hechizos = new TreeMap<Integer, Hechizo.StatsHechizos>();
    private Map<Integer, Character> _lugaresHechizos = new TreeMap<Integer, Character>();

    public Encarnación(int id, int clase, int nivel, long experiencia, int segundos, String hechizos) {
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

    public Map<Integer, Hechizo.StatsHechizos> getMapHechizos() {
        return this._hechizos;
    }

    public void subirNivel(Personaje perso) {
        if (this._nivel == 50) {
            return;
        }
        ++this._nivel;
        if (this._nivel % 10 == 0) {
            this.hechizosClase(this._clase, this._nivel / 10 + 1);
            SocketManager.enviar(perso, this.actualizarNivelHechizos(this._nivel / 10 + 1, true));
        }
        SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(perso, Mundo.getObjeto(this._id));
        perso.setPDVMAX(this.getPDVMAX());
        perso.fullPDV();
        perso.actualizarInfoGrupo();
        SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
    }

    public String actualizarNivelHechizos(int nivel, boolean encarnacion) {
        boolean primero = true;
        String packet = "";
        for (Hechizo.StatsHechizos SH : this.getMapHechizos().values()) {
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

    public void addExp(long xp, Personaje perso) {
        this._experiencia += xp;
        while (this._experiencia >= Mundo.getExpMaxPersonaje(this._nivel) && this._nivel < 50) {
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
            Hechizo.StatsHechizos SH = this._hechizos.get(key);
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
        this._hechizos = new TreeMap<Integer, Hechizo.StatsHechizos>();
        switch (clase) {
            case 14: {
                this._hechizos.put(1291, Mundo.getHechizo(1291).getStatsPorNivel(nivel));
                this._hechizos.put(1296, Mundo.getHechizo(1296).getStatsPorNivel(nivel));
                this._hechizos.put(1289, Mundo.getHechizo(1289).getStatsPorNivel(nivel));
                this._hechizos.put(1285, Mundo.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1290, Mundo.getHechizo(1290).getStatsPorNivel(nivel));
                break;
            }
            case 13: {
                this._hechizos.put(1299, Mundo.getHechizo(1299).getStatsPorNivel(nivel));
                this._hechizos.put(1288, Mundo.getHechizo(1288).getStatsPorNivel(nivel));
                this._hechizos.put(1297, Mundo.getHechizo(1297).getStatsPorNivel(nivel));
                this._hechizos.put(1285, Mundo.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1298, Mundo.getHechizo(1298).getStatsPorNivel(nivel));
                break;
            }
            case 17: {
                this._hechizos.put(1300, Mundo.getHechizo(1300).getStatsPorNivel(nivel));
                this._hechizos.put(1301, Mundo.getHechizo(1301).getStatsPorNivel(nivel));
                this._hechizos.put(1303, Mundo.getHechizo(1303).getStatsPorNivel(nivel));
                this._hechizos.put(1285, Mundo.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1302, Mundo.getHechizo(1302).getStatsPorNivel(nivel));
                break;
            }
            case 16: {
                this._hechizos.put(1292, Mundo.getHechizo(1292).getStatsPorNivel(nivel));
                this._hechizos.put(1293, Mundo.getHechizo(1293).getStatsPorNivel(nivel));
                this._hechizos.put(1294, Mundo.getHechizo(1294).getStatsPorNivel(nivel));
                this._hechizos.put(1285, Mundo.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1295, Mundo.getHechizo(1295).getStatsPorNivel(nivel));
                break;
            }
            case 15: {
                this._hechizos.put(1283, Mundo.getHechizo(1283).getStatsPorNivel(nivel));
                this._hechizos.put(1284, Mundo.getHechizo(1284).getStatsPorNivel(nivel));
                this._hechizos.put(1286, Mundo.getHechizo(1286).getStatsPorNivel(nivel));
                this._hechizos.put(1285, Mundo.getHechizo(1285).getStatsPorNivel(nivel));
                this._hechizos.put(1287, Mundo.getHechizo(1287).getStatsPorNivel(nivel));
                break;
            }
            case 18: {
                this._hechizos.put(1601, Mundo.getHechizo(1601).getStatsPorNivel(nivel));
                this._hechizos.put(1602, Mundo.getHechizo(1602).getStatsPorNivel(nivel));
                this._hechizos.put(1603, Mundo.getHechizo(1603).getStatsPorNivel(nivel));
                this._hechizos.put(1604, Mundo.getHechizo(1604).getStatsPorNivel(nivel));
                this._hechizos.put(1605, Mundo.getHechizo(1605).getStatsPorNivel(nivel));
                this._hechizos.put(1606, Mundo.getHechizo(1606).getStatsPorNivel(nivel));
                this._hechizos.put(1607, Mundo.getHechizo(1607).getStatsPorNivel(nivel));
                this._hechizos.put(1608, Mundo.getHechizo(1608).getStatsPorNivel(nivel));
                this._hechizos.put(1609, Mundo.getHechizo(1609).getStatsPorNivel(nivel));
                this._hechizos.put(1610, Mundo.getHechizo(1610).getStatsPorNivel(nivel));
                this._hechizos.put(1611, Mundo.getHechizo(1611).getStatsPorNivel(nivel));
                this._hechizos.put(1612, Mundo.getHechizo(1612).getStatsPorNivel(nivel));
                this._hechizos.put(1613, Mundo.getHechizo(1613).getStatsPorNivel(nivel));
                this._hechizos.put(1614, Mundo.getHechizo(1614).getStatsPorNivel(nivel));
                this._hechizos.put(1615, Mundo.getHechizo(1615).getStatsPorNivel(nivel));
                this._hechizos.put(1616, Mundo.getHechizo(1616).getStatsPorNivel(nivel));
                this._hechizos.put(1617, Mundo.getHechizo(1617).getStatsPorNivel(nivel));
                this._hechizos.put(1618, Mundo.getHechizo(1618).getStatsPorNivel(nivel));
                this._hechizos.put(1619, Mundo.getHechizo(1619).getStatsPorNivel(nivel));
                this._hechizos.put(1620, Mundo.getHechizo(1620).getStatsPorNivel(nivel));
                break;
            }
            case 19: {
                this._hechizos.put(1561, Mundo.getHechizo(1561).getStatsPorNivel(nivel));
                this._hechizos.put(1562, Mundo.getHechizo(1562).getStatsPorNivel(nivel));
                this._hechizos.put(1563, Mundo.getHechizo(1563).getStatsPorNivel(nivel));
                this._hechizos.put(1564, Mundo.getHechizo(1564).getStatsPorNivel(nivel));
                this._hechizos.put(1565, Mundo.getHechizo(1565).getStatsPorNivel(nivel));
                this._hechizos.put(1566, Mundo.getHechizo(1566).getStatsPorNivel(nivel));
                this._hechizos.put(1567, Mundo.getHechizo(1567).getStatsPorNivel(nivel));
                this._hechizos.put(1568, Mundo.getHechizo(1568).getStatsPorNivel(nivel));
                this._hechizos.put(1569, Mundo.getHechizo(1569).getStatsPorNivel(nivel));
                this._hechizos.put(1570, Mundo.getHechizo(1570).getStatsPorNivel(nivel));
                this._hechizos.put(1571, Mundo.getHechizo(1571).getStatsPorNivel(nivel));
                this._hechizos.put(1572, Mundo.getHechizo(1572).getStatsPorNivel(nivel));
                this._hechizos.put(1573, Mundo.getHechizo(1573).getStatsPorNivel(nivel));
                this._hechizos.put(1574, Mundo.getHechizo(1574).getStatsPorNivel(nivel));
                this._hechizos.put(1575, Mundo.getHechizo(1575).getStatsPorNivel(nivel));
                this._hechizos.put(1576, Mundo.getHechizo(1576).getStatsPorNivel(nivel));
                this._hechizos.put(1577, Mundo.getHechizo(1577).getStatsPorNivel(nivel));
                this._hechizos.put(1578, Mundo.getHechizo(1578).getStatsPorNivel(nivel));
                this._hechizos.put(1579, Mundo.getHechizo(1579).getStatsPorNivel(nivel));
                this._hechizos.put(1580, Mundo.getHechizo(1580).getStatsPorNivel(nivel));
                break;
            }
            case 20: {
                this._hechizos.put(1581, Mundo.getHechizo(1581).getStatsPorNivel(nivel));
                this._hechizos.put(1582, Mundo.getHechizo(1582).getStatsPorNivel(nivel));
                this._hechizos.put(1583, Mundo.getHechizo(1583).getStatsPorNivel(nivel));
                this._hechizos.put(1584, Mundo.getHechizo(1584).getStatsPorNivel(nivel));
                this._hechizos.put(1585, Mundo.getHechizo(1585).getStatsPorNivel(nivel));
                this._hechizos.put(1586, Mundo.getHechizo(1586).getStatsPorNivel(nivel));
                this._hechizos.put(1587, Mundo.getHechizo(1587).getStatsPorNivel(nivel));
                this._hechizos.put(1588, Mundo.getHechizo(1588).getStatsPorNivel(nivel));
                this._hechizos.put(1589, Mundo.getHechizo(1589).getStatsPorNivel(nivel));
                this._hechizos.put(1590, Mundo.getHechizo(1590).getStatsPorNivel(nivel));
                this._hechizos.put(1591, Mundo.getHechizo(1591).getStatsPorNivel(nivel));
                this._hechizos.put(1592, Mundo.getHechizo(1592).getStatsPorNivel(nivel));
                this._hechizos.put(1593, Mundo.getHechizo(1593).getStatsPorNivel(nivel));
                this._hechizos.put(1594, Mundo.getHechizo(1594).getStatsPorNivel(nivel));
                this._hechizos.put(1595, Mundo.getHechizo(1595).getStatsPorNivel(nivel));
                this._hechizos.put(1596, Mundo.getHechizo(1596).getStatsPorNivel(nivel));
                this._hechizos.put(1597, Mundo.getHechizo(1597).getStatsPorNivel(nivel));
                this._hechizos.put(1598, Mundo.getHechizo(1598).getStatsPorNivel(nivel));
                this._hechizos.put(1599, Mundo.getHechizo(1599).getStatsPorNivel(nivel));
                this._hechizos.put(1600, Mundo.getHechizo(1600).getStatsPorNivel(nivel));
                break;
            }
            case 21: {
                this._hechizos.put(1541, Mundo.getHechizo(1541).getStatsPorNivel(nivel));
                this._hechizos.put(1542, Mundo.getHechizo(1542).getStatsPorNivel(nivel));
                this._hechizos.put(1543, Mundo.getHechizo(1543).getStatsPorNivel(nivel));
                this._hechizos.put(1544, Mundo.getHechizo(1544).getStatsPorNivel(nivel));
                this._hechizos.put(1545, Mundo.getHechizo(1545).getStatsPorNivel(nivel));
                this._hechizos.put(1546, Mundo.getHechizo(1546).getStatsPorNivel(nivel));
                this._hechizos.put(1547, Mundo.getHechizo(1547).getStatsPorNivel(nivel));
                this._hechizos.put(1548, Mundo.getHechizo(1548).getStatsPorNivel(nivel));
                this._hechizos.put(1549, Mundo.getHechizo(1549).getStatsPorNivel(nivel));
                this._hechizos.put(1550, Mundo.getHechizo(1550).getStatsPorNivel(nivel));
                this._hechizos.put(1551, Mundo.getHechizo(1551).getStatsPorNivel(nivel));
                this._hechizos.put(1552, Mundo.getHechizo(1552).getStatsPorNivel(nivel));
                this._hechizos.put(1553, Mundo.getHechizo(1553).getStatsPorNivel(nivel));
                this._hechizos.put(1554, Mundo.getHechizo(1554).getStatsPorNivel(nivel));
                this._hechizos.put(1555, Mundo.getHechizo(1555).getStatsPorNivel(nivel));
                this._hechizos.put(1556, Mundo.getHechizo(1556).getStatsPorNivel(nivel));
                this._hechizos.put(1557, Mundo.getHechizo(1557).getStatsPorNivel(nivel));
                this._hechizos.put(1558, Mundo.getHechizo(1558).getStatsPorNivel(nivel));
                this._hechizos.put(1559, Mundo.getHechizo(1559).getStatsPorNivel(nivel));
                this._hechizos.put(1560, Mundo.getHechizo(1560).getStatsPorNivel(nivel));
            }
        }
    }

    public boolean tieneHechizoID(int hechizo) {
        return this._hechizos.get(hechizo) != null;
    }

    public String stringListaHechizos() {
        String str = "";
        for (Hechizo.StatsHechizos SH : this._hechizos.values()) {
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

    public Hechizo.StatsHechizos getStatsHechizo(int hechizoID) {
        return this._hechizos.get(hechizoID);
    }
}

