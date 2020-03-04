package objects;

import common.Constantes;
import common.CryptManager;
import common.Fórmulas;
import common.LesGuardians;
import common.Pathfinding;
import common.SQLManager;
import common.SocketManager;
import common.Mundo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.Timer;
import objects.Mapa;
import objects.Objeto;
import objects.Personaje;

public class Dragopavo {
    private int _id;
    private int _colorID;
    private int _sexo;
    private int _amor;
    private int _resistencia;
    private int _nivel;
    private long _experiencia;
    private String _nombre;
    private int _fatiga;
    private int _energia;
    private int _reprod;
    private int _madurez;
    private int _serenidad;
    private Personaje.Stats _stats = new Personaje.Stats();
    private String _ancestros = "?,?,?,?,?,?,?,?,?,?,?,?,?,?";
    private ArrayList<Objeto> _objetos = new ArrayList<Objeto>();
    private List<Integer> _capacidades = new ArrayList<Integer>(2);
    String _habilidad = ",";
    private short _celdaID;
    private int _due\u00f1o;
    private int _talla;
    private short _mapaID;
    private int _orientacion;
    private int _fecundadaHace;
    private int _pareja;
    private Timer _aumentarFecundo = new Timer(60000, new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            Dragopavo dragossauros = Dragopavo.this;
            dragossauros._fecundadaHace = dragossauros._fecundadaHace + 1;
            Dragopavo.this.startFecundo();
        }
    });
    private String _vip = "";

    public Dragopavo(int color, int due\u00f1o) {
        int sexo = Fórmulas.getRandomValor(0, 1);
        this._id = Mundo.getSigIDMontura();
        this._colorID = color;
        this._sexo = sexo;
        this._nivel = 100;
        this._experiencia = 0L;
        this._nombre = "Sans Nom";
        this._fatiga = 0;
        this._energia = this.getMaxEnergia();
        this._reprod = 0;
        this._madurez = this.getMaxMadurez();
        this._serenidad = 0;
        this._stats = this._colorID == 75 ? Constantes.getStatsMonturaVIP(this._vip, this._nivel) : Constantes.getStatsMontura(this._colorID, this._nivel);
        this._ancestros = "?,?,?,?,?,?,?,?,?,?,?,?,?,?";
        this._habilidad = "0";
        this._talla = 100;
        this._due\u00f1o = due\u00f1o;
        this._celdaID = (short)-1;
        this._mapaID = (short)-1;
        this._orientacion = 1;
        this._fecundadaHace = -1;
        this._pareja = -1;
        this._vip = "";
        if (this._fecundadaHace > 0) {
            this.startFecundo();
        }
        Mundo.addDragopavo(this);
    }

    public Dragopavo(int color, Dragopavo madre, Dragopavo padre) {
        int[] arrn = new int[4];
        arrn[2] = 1;
        int[] sexo = arrn;
        this._id = Mundo.getSigIDMontura();
        this._colorID = color;
        this._sexo = sexo[Fórmulas.getRandomValor(0, 3)];
        this._nivel = 200;
        this._experiencia = 0L;
        this._nombre = "(Sin Nombre)";
        this._fatiga = 0;
        this._energia = 0;
        this._reprod = 0;
        this._madurez = 0;
        this._serenidad = 0;
        this._stats = Constantes.getStatsMontura(this._colorID, this._nivel);
        String[] papa = padre.getAncestros().split(",");
        String[] mama = madre.getAncestros().split(",");
        String primerapapa = String.valueOf(papa[0]) + "," + papa[1];
        String primeramama = String.valueOf(mama[0]) + "," + mama[1];
        String segundapapa = String.valueOf(papa[2]) + "," + papa[3] + "," + papa[4] + "," + papa[5];
        String segundamama = String.valueOf(mama[2]) + "," + mama[3] + "," + mama[4] + "," + mama[5];
        this._ancestros = String.valueOf(padre.getColor()) + "," + madre.getColor() + "," + primerapapa + "," + primeramama + "," + segundapapa + "," + segundamama;
        int habilidad = Fórmulas.getRandomValor(1, 8);
        this._habilidad = String.valueOf(habilidad);
        this._celdaID = (short)-1;
        this._mapaID = (short)-1;
        this._due\u00f1o = madre.getDue\u00f1o();
        this._talla = 50;
        this._orientacion = 1;
        this._fecundadaHace = -1;
        this._pareja = -1;
        this._vip = "";
        if (this._fecundadaHace > 0) {
            this.startFecundo();
        }
        Mundo.addDragopavo(this);
    }

    public Dragopavo(int id, int color, int sexo, int amor, int resistencia, int nivel, long exp, String nombre, int fatiga, int energia, int reprod, int madurez, int serenidad, String objetos, String anc, String habilidad, int talla, short celda, short mapa, int due\u00f1o, int orientacion, int fecundable, int pareja, String vip) {
        this._id = id;
        this._colorID = color;
        this._sexo = sexo;
        this._amor = amor;
        this._resistencia = resistencia;
        this._nivel = nivel;
        this._experiencia = exp;
        this._nombre = nombre;
        this._fatiga = fatiga;
        this._energia = energia;
        this._reprod = reprod;
        this._madurez = madurez;
        this._serenidad = serenidad;
        this._ancestros = anc;
        this._vip = vip;
        this._stats = this._colorID == 75 ? Constantes.getStatsMonturaVIP(this._vip, this._nivel) : Constantes.getStatsMontura(this._colorID, this._nivel);
        this._habilidad = habilidad;
        this._talla = talla;
        this._celdaID = celda;
        this._mapaID = mapa;
        this._due\u00f1o = due\u00f1o;
        this._orientacion = orientacion;
        this._fecundadaHace = fecundable;
        this._pareja = pareja;
        if (this._fecundadaHace > 0) {
            this.startFecundo();
        }
        for (String s : habilidad.split(",", 2)) {
            if (s == null) continue;
            int a = Integer.parseInt(s);
            try {
                this._capacidades.add(a);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        for (String str : objetos.split(";")) {
            try {
                Objeto obj = Mundo.getObjeto(Integer.parseInt(str));
                if (obj == null) continue;
                this._objetos.add(obj);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public void startFecundo() {
        if (this._fecundadaHace < 1440 && this._fecundadaHace > 0) {
            this._aumentarFecundo.restart();
        } else {
            this._aumentarFecundo.stop();
        }
    }

    public int getID() {
        return this._id;
    }

    public int getColor() {
        return this._colorID;
    }

    public void setStatsVIP(String vip) {
        this._vip = vip;
        this._stats = Constantes.getStatsMonturaVIP(vip, this._nivel);
    }

    public String getVIP() {
        return this._vip;
    }

    public int getSexo() {
        return this._sexo;
    }

    public int getAmor() {
        return this._amor;
    }

    public String getAncestros() {
        return this._ancestros;
    }

    public int getResistencia() {
        return this._resistencia;
    }

    public int getPodsActuales() {
        int pods = 0;
        for (Objeto obj : this._objetos) {
            if (obj == null) continue;
            pods += obj.getModelo().getPeso() * obj.getCantidad();
        }
        return pods;
    }

    public String getListaObjDragopavo() {
        String objetos = "";
        for (Objeto obj : this._objetos) {
            objetos = String.valueOf(objetos) + "O" + obj.stringObjetoConGui\u00f1o();
        }
        return objetos;
    }

    public void addObjAMochila(int id, int cant, Personaje perso) {
        Objeto objetoAgregar = Mundo.getObjeto(id);
        if (objetoAgregar.getPosicion() != -1) {
            return;
        }
        Objeto objIgualEnMochila = this.getSimilarObjeto(objetoAgregar);
        int nuevaCant = objetoAgregar.getCantidad() - cant;
        if (objIgualEnMochila == null) {
            if (nuevaCant <= 0) {
                perso.borrarObjetoSinEliminar(objetoAgregar.getID());
                this._objetos.add(objetoAgregar);
                String str = "O+" + objetoAgregar.getID() + "|" + objetoAgregar.getCantidad() + "|" + objetoAgregar.getModelo().getID() + "|" + objetoAgregar.convertirStatsAString();
                SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
                SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(perso, id);
            } else {
                objetoAgregar.setCantidad(nuevaCant);
                objIgualEnMochila = Objeto.clonarObjeto(objetoAgregar, cant);
                Mundo.addObjeto(objIgualEnMochila, true);
                this._objetos.add(objIgualEnMochila);
                String str = "O+" + objIgualEnMochila.getID() + "|" + objIgualEnMochila.getCantidad() + "|" + objIgualEnMochila.getModelo().getID() + "|" + objIgualEnMochila.convertirStatsAString();
                SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
                SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, objetoAgregar);
            }
        } else if (nuevaCant <= 0) {
            perso.borrarObjetoSinEliminar(objetoAgregar.getID());
            objIgualEnMochila.setCantidad(objIgualEnMochila.getCantidad() + objetoAgregar.getCantidad());
            String str = "O+" + objIgualEnMochila.getID() + "|" + objIgualEnMochila.getCantidad() + "|" + objIgualEnMochila.getModelo().getID() + "|" + objIgualEnMochila.convertirStatsAString();
            Mundo.eliminarObjeto(objetoAgregar.getID());
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
            SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(perso, id);
        } else {
            objetoAgregar.setCantidad(nuevaCant);
            objIgualEnMochila.setCantidad(objIgualEnMochila.getCantidad() + cant);
            String str = "O+" + objIgualEnMochila.getID() + "|" + objIgualEnMochila.getCantidad() + "|" + objIgualEnMochila.getModelo().getID() + "|" + objIgualEnMochila.convertirStatsAString();
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, objetoAgregar);
        }
        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
        SocketManager.ENVIAR_Ew_PODS_MONTURA(perso, this.getPodsActuales());
        SQLManager.REPLACE_MONTURA(this, false);
    }

    private Objeto getSimilarObjeto(Objeto obj) {
        for (Objeto value : this._objetos) {
            Objeto.ObjetoModelo objetoMod = value.getModelo();
            if (objetoMod.getTipo() == 85 || objetoMod.getID() != obj.getModelo().getID() || !value.getStats().sonStatsIguales(obj.getStats())) continue;
            return value;
        }
        return null;
    }

    public void removerDeLaMochila(int id, int cant, Personaje perso) {
        Objeto objARetirar = Mundo.getObjeto(id);
        if (!this._objetos.contains(objARetirar)) {
            return;
        }
        Objeto objIgualInventario = perso.getObjSimilarInventario(objARetirar);
        int nuevaCant = objARetirar.getCantidad() - cant;
        if (objIgualInventario == null) {
            if (nuevaCant <= 0) {
                this._objetos.remove(objARetirar);
                if (perso.addObjetoSimilar(objARetirar, true, -1)) {
                    Mundo.eliminarObjeto(id);
                } else {
                    perso.addObjetoPut(objARetirar);
                    SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, objARetirar);
                }
                String str = "O-" + id;
                SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
            } else {
                objIgualInventario = Objeto.clonarObjeto(objARetirar, cant);
                Mundo.addObjeto(objIgualInventario, true);
                objARetirar.setCantidad(nuevaCant);
                perso.addObjetoPut(objIgualInventario);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, objIgualInventario);
                String str = "O+" + objARetirar.getID() + "|" + objARetirar.getCantidad() + "|" + objARetirar.getModelo().getID() + "|" + objARetirar.convertirStatsAString();
                SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
            }
        } else if (nuevaCant <= 0) {
            this._objetos.remove(objARetirar);
            objIgualInventario.setCantidad(objIgualInventario.getCantidad() + objARetirar.getCantidad());
            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, objIgualInventario);
            Mundo.eliminarObjeto(objARetirar.getID());
            String str = "O-" + id;
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
        } else {
            objARetirar.setCantidad(nuevaCant);
            objIgualInventario.setCantidad(objIgualInventario.getCantidad() + cant);
            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, objIgualInventario);
            String str = "O+" + objARetirar.getID() + "|" + objARetirar.getCantidad() + "|" + objARetirar.getModelo().getID() + "|" + objARetirar.convertirStatsAString();
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, str);
        }
        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
        SocketManager.ENVIAR_Ew_PODS_MONTURA(perso, this.getPodsActuales());
        SQLManager.REPLACE_MONTURA(this, false);
    }

    public void setDue\u00f1o(int due\u00f1o) {
        this._due\u00f1o = due\u00f1o;
    }

    public int getNivel() {
        return this._nivel;
    }

    public long getExp() {
        return this._experiencia;
    }

    public String getNombre() {
        return this._nombre;
    }

    public int getDue\u00f1o() {
        return this._due\u00f1o;
    }

    public boolean estaCriando() {
        return this._celdaID != -1;
    }

    public void actCapacidades() {
        this._capacidades.clear();
        for (String s : this._habilidad.split(",", 2)) {
            if (s == null) continue;
            int a = Integer.parseInt(s);
            try {
                this._capacidades.add(a);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public int getFecundadaHace() {
        if (this._reprod == -1 || this._reprod >= 20) {
            this._fecundadaHace = -1;
            return -1;
        }
        if (this._fecundadaHace >= 1) {
            return this._fecundadaHace;
        }
        this._fecundadaHace = -1;
        return this._fecundadaHace;
    }

    public void setHabilidad(String habilidad) {
        this._habilidad = habilidad;
        this.actCapacidades();
    }

    public int getFecunda() {
        if (this._reprod == -1 || this._fecundadaHace >= 1) {
            return 0;
        }
        if (this._amor >= 7500 && this._resistencia >= 7500 && this._nivel > 4) {
            return 10;
        }
        return 0;
    }

    public void setMapaCelda(short mapa, short celda) {
        this._mapaID = mapa;
        this._celdaID = celda;
    }

    public int getFatiga() {
        return this._fatiga;
    }

    public short getMapa() {
        return this._mapaID;
    }

    public int getCelda() {
        return this._celdaID;
    }

    public int getTalla() {
        return this._talla;
    }

    public int getEnergia() {
        return this._energia;
    }

    public int getReprod() {
        return this._reprod;
    }

    public int getMadurez() {
        return this._madurez;
    }

    public int getSerenidad() {
        return this._serenidad;
    }

    public Personaje.Stats getStats() {
        return this._stats;
    }

    public ArrayList<Objeto> getObjetos() {
        return this._objetos;
    }

    public List<Integer> getCapacidades() {
        return this._capacidades;
    }

    public String detallesMontura() {
        String str = String.valueOf(this._id) + ":";
        str = String.valueOf(str) + this._colorID + ":";
        str = String.valueOf(str) + this._ancestros + ":";
        str = String.valueOf(str) + ",," + this._habilidad + ":";
        str = String.valueOf(str) + this._nombre + ":";
        str = String.valueOf(str) + this._sexo + ":";
        str = String.valueOf(str) + this.parseXpString() + ":";
        str = String.valueOf(str) + this._nivel + ":";
        str = String.valueOf(str) + this.esMontable() + ":";
        str = String.valueOf(str) + this.getTotalPod() + ":";
        str = String.valueOf(str) + "0:";
        str = String.valueOf(str) + this._resistencia + ",10000:";
        str = String.valueOf(str) + this._madurez + "," + this.getMaxMadurez() + ":";
        str = String.valueOf(str) + this._energia + "," + this.getMaxEnergia() + ":";
        str = String.valueOf(str) + this._serenidad + ",-10000,10000:";
        str = String.valueOf(str) + this._amor + ",10000:";
        str = String.valueOf(str) + this.getFecundadaHace() + ":";
        str = String.valueOf(str) + this.getFecunda() + ":";
        str = String.valueOf(str) + this.convertirStringAStats() + ":";
        str = String.valueOf(str) + this._fatiga + ",240:";
        str = String.valueOf(str) + this._reprod + ",20:";
        return str;
    }

    public void castrarPavo() {
        this._reprod = -1;
    }

    private String convertirStringAStats() {
        String stats = "";
        for (Map.Entry<Integer, Integer> entry : this._stats.getStatsComoMap().entrySet()) {
            if (entry.getValue() <= 0) continue;
            if (stats.length() > 0) {
                stats = String.valueOf(stats) + ",";
            }
            stats = String.valueOf(stats) + Integer.toHexString(entry.getKey()) + "#" + Integer.toHexString(entry.getValue()) + "#0#0";
        }
        return stats;
    }

    private int getMaxEnergia() {
        return 10 * this._nivel + 150 * Constantes.getGeneracion(this._colorID);
    }

    private int getMaxMadurez() {
        return 1500 * Constantes.getGeneracion(this._colorID);
    }

    private int getTotalPod() {
        int habilidad = 0;
        if (this._capacidades.contains(2)) {
            habilidad = 20 * this._nivel;
        }
        return 10 * this._nivel + (100 * Constantes.getGeneracion(this._colorID) + habilidad);
    }

    private String parseXpString() {
        return String.valueOf(this._experiencia) + "," + Mundo.getExpNivel((int)this._nivel)._montura + "," + Mundo.getExpNivel((int)(this._nivel + 1))._montura;
    }

    public int esMontable() {
        if (this._energia < 10 || this._madurez < this.getMaxMadurez() || this._fatiga == 240) {
            return 0;
        }
        return 1;
    }

    public void aumFatiga() {
        this._fatiga += 2;
        if (this._capacidades.contains(1)) {
            --this._fatiga;
        }
        if (this._fatiga > 240) {
            this._fatiga = 240;
        }
    }

    public void aumResistencia() {
        this._resistencia = (int)((float)this._resistencia + 100.0f * LesGuardians.RATE_FILHOTE_MOUNT);
        if (this._capacidades.contains(5)) {
            this._resistencia = (int)((float)this._resistencia + 100.0f * LesGuardians.RATE_FILHOTE_MOUNT);
        }
        if (this._resistencia > 10000) {
            this._resistencia = 10000;
        }
    }

    public void setAmor(int amor) {
        this._amor = amor;
    }

    public void setResistencia(int resistencia) {
        this._resistencia = resistencia;
    }

    public void setMaxMadurez() {
        this._madurez = this.getMaxMadurez();
    }

    public void setMaxEnergia() {
        this._energia = this.getMaxEnergia();
    }

    public void aumMadurez() {
        int maxMadurez = this.getMaxMadurez();
        if (this._madurez < maxMadurez) {
            this._madurez = (int)((float)this._madurez + 100.0f * LesGuardians.RATE_FILHOTE_MOUNT);
            if (this._capacidades.contains(7)) {
                this._madurez = (int)((float)this._madurez + 100.0f * LesGuardians.RATE_FILHOTE_MOUNT);
            }
            if (this._talla < 100) {
                Mapa mapa = Mundo.getMapa(this._mapaID);
                if (maxMadurez / this._madurez <= 1) {
                    this._talla = 100;
                    SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapa, this._id);
                    SocketManager.ENVIAR_GM_DRAGOPAVO_A_MAPA(mapa, this);
                    return;
                }
                if (this._talla < 75 && maxMadurez / this._madurez == 2) {
                    this._talla = 75;
                    SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapa, this._id);
                    SocketManager.ENVIAR_GM_DRAGOPAVO_A_MAPA(mapa, this);
                    return;
                }
                if (this._talla < 50 && maxMadurez / this._madurez == 3) {
                    this._talla = 50;
                    SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapa, this._id);
                    SocketManager.ENVIAR_GM_DRAGOPAVO_A_MAPA(mapa, this);
                    return;
                }
            }
        }
        if (this._madurez > maxMadurez) {
            this._madurez = maxMadurez;
        }
    }

    public void aumAmor() {
        this._amor = (int)((float)this._amor + 100.0f * LesGuardians.RATE_FILHOTE_MOUNT);
        if (this._amor > 10000) {
            this._amor = 10000;
        }
    }

    public void aumSerenidad() {
        this._serenidad = (int)((float)this._serenidad + 100.0f * LesGuardians.RATE_FILHOTE_MOUNT);
        if (this._serenidad > 10000) {
            this._serenidad = 10000;
        }
    }

    public void resSerenidad() {
        this._serenidad = (int)((float)this._serenidad - 100.0f * LesGuardians.RATE_FILHOTE_MOUNT);
        if (this._serenidad < -10000) {
            this._serenidad = -10000;
        }
    }

    public void aumEnergia() {
        this._energia = (int)((float)this._energia + 10.0f * LesGuardians.RATE_FILHOTE_MOUNT);
        int maxEnergia = this.getMaxEnergia();
        if (this._energia > maxEnergia) {
            this._energia = maxEnergia;
        }
    }

    public void aumEnergia(int valor, int veces) {
        this._energia += valor * veces;
        int maxEnergia = this.getMaxEnergia();
        if (this._energia > maxEnergia) {
            this._energia = maxEnergia;
        }
    }

    public void resFatiga() {
        this._fatiga -= 20;
        if (this._fatiga < 0) {
            this._fatiga = 0;
        }
    }

    public void resAmor(int amor) {
        this._amor -= amor;
        if (this._amor < 0) {
            this._amor = 0;
        }
    }

    public void resResistencia(int resistencia) {
        this._resistencia -= resistencia;
        if (this._resistencia < 0) {
            this._resistencia = 0;
        }
    }

    public void aumReproduccion() {
        if (this._reprod == -1) {
            return;
        }
        ++this._reprod;
    }

    public String stringObjetosBD() {
        String str = "";
        for (Objeto obj : this._objetos) {
            str = String.valueOf(str) + (str.length() > 0 ? ";" : "") + obj.getID();
        }
        return str;
    }

    public void setNombre(String nombre) {
        this._nombre = nombre;
    }

    public void addXp(long aumentar) {
        if (this._capacidades.contains(4)) {
            aumentar *= 2L;
        }
        this._experiencia += aumentar;
        while (this._experiencia >= (long)Mundo.getExpNivel((int)(this._nivel + 1))._montura && this._nivel < 100) {
            this.subirNivel();
        }
    }

    public void subirNivel() {
        ++this._nivel;
        this._stats = this._colorID != 74 ? Constantes.getStatsMontura(this._colorID, this._nivel) : Constantes.getStatsMonturaVIP(this._vip, this._nivel);
    }

    public String getStringColor(String colorDue\u00f1oPavo) {
        String b = "";
        if (this._capacidades.contains(9)) {
            b = "," + colorDue\u00f1oPavo;
        }
        if (this._colorID == 75) {
            int colorRandom = Fórmulas.getRandomValor(1, 87);
            b = "," + Constantes.getStringColorDragopavo(colorRandom);
        }
        return String.valueOf(this._colorID) + b;
    }

    public String getHabilidad() {
        return this._habilidad;
    }

    public boolean addCapacidad(String capa) {
        int c = 0;
        for (String s : capa.split(",", 2)) {
            if (this._capacidades.size() >= 2) {
                return false;
            }
            try {
                c = Integer.parseInt(s);
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (c != 0) {
                this._capacidades.add(c);
            }
            this._habilidad = this._capacidades.size() == 1 ? this._capacidades.get(0) + "," : this._capacidades.get(0) + "," + this._capacidades.get(1);
        }
        return true;
    }

    public void energiaPerdida(int energia) {
        this._energia = energia;
    }

    public int getPareja() {
        return this._pareja;
    }

    public void setPareja(int pareja) {
        this._pareja = pareja;
    }

    public int getOrientacion() {
        return this._orientacion;
    }

    public void setFecundadaHace(int fecundable) {
        if (this._reprod == -1) {
            return;
        }
        this._fecundadaHace = fecundable;
    }

    public boolean esCastrado() {
        return this._reprod == -1;
    }

    public synchronized String getCriarMontura(Mapa.Cercado cercado) {
        String str = "GM|+";
        str = this._celdaID == -1 && this._mapaID == -1 ? String.valueOf(str) + cercado.getColocarCelda() + ";" : String.valueOf(str) + this._celdaID + ";";
        str = String.valueOf(str) + this._orientacion + ";0;" + this._id + ";" + this._nombre + ";-9;";
        str = this._colorID == 88 ? String.valueOf(str) + 7005 : String.valueOf(str) + 7002;
        str = String.valueOf(str) + "^" + this._talla + ";";
        str = Mundo.getPersonaje(this._due\u00f1o) == null ? String.valueOf(str) + "Sin Due\u00f1o" : String.valueOf(str) + Mundo.getPersonaje(this._due\u00f1o).getNombre();
        str = String.valueOf(str) + ";" + this._nivel + ";" + this._colorID;
        return str;
    }

    public synchronized void moverMontura(Personaje due\u00f1o, int casillas, boolean alejar) {
        int accion = 0;
        if (due\u00f1o == null) {
            return;
        }
        if (due\u00f1o.getCelda().getID() == this._celdaID) {
            return;
        }
        String path = "";
        Mapa mapa = due\u00f1o.getMapa();
        if (mapa.getCercado() == null) {
            return;
        }
        Mapa.Cercado cercado = mapa.getCercado();
        int azar = Fórmulas.getRandomValor(1, 10);
        char direccion = Pathfinding.getDirEntreDosCeldas(mapa, this._celdaID, due\u00f1o.getCelda().getID());
        if (alejar) {
            direccion = Pathfinding.getDireccionOpuesta(direccion);
        }
        short celda = this._celdaID;
        short celdaprueba = this._celdaID;
        for (int i = 0; i < casillas; ++i) {
            if (mapa.getCelda(celdaprueba = Pathfinding.getSigIDCeldaMismaDir(celdaprueba, direccion, this._mapaID)) == null) {
                return;
            }
            if (cercado.getCeldayObjeto().containsKey(celdaprueba)) {
                int item = cercado.getCeldayObjeto().get(celdaprueba);
                if (item == 7758) {
                    this.resSerenidad();
                } else if (item == 7781) {
                    if (this._serenidad < 0) {
                        this.aumResistencia();
                    }
                } else if (item == 7613) {
                    this.resFatiga();
                    this.aumEnergia();
                } else if (item == 7696) {
                    if (this._serenidad > 0) {
                        this.aumAmor();
                    }
                } else if (item == 7628) {
                    this.aumSerenidad();
                } else if (item == 7594 && this._serenidad <= 2000 && this._serenidad >= -2000) {
                    this.aumMadurez();
                }
                this.aumFatiga();
                break;
            }
            if (!mapa.getCelda(celdaprueba).esCaminable(false) || cercado.getPuerta() == celdaprueba || mapa.celdaSalienteLateral(celda, celdaprueba)) break;
            celda = celdaprueba;
            path = String.valueOf(path) + direccion + CryptManager.celdaIDACodigo(celda);
        }
        if (celda == this._celdaID) {
            this._orientacion = CryptManager.getNumeroPorValorHash(direccion);
            SocketManager.ENVIAR_eD_CAMBIAR_ORIENTACION(mapa, this._id, this._orientacion);
            SocketManager.ENVIAR_GDE_FRAME_OBJECT_EXTERNAL(mapa, String.valueOf(celdaprueba) + ";4");
            SocketManager.ENVIAR_eUK_EMOTE_MAPA(mapa, this._id, accion, "");
            return;
        }
        if (azar == 5) {
            accion = 8;
        }
        if (cercado.getListaCriando().size() > 1) {
            for (int pavo : cercado.getListaCriando()) {
                Dragopavo dragopavo = Mundo.getDragopavoPorID(pavo);
                if (dragopavo._sexo == this._sexo || dragopavo.getFecunda() == 0 || this.getFecunda() == 0 || dragopavo.getCelda() != celdaprueba || dragopavo._reprod >= 20 || this._reprod >= 20 || dragopavo.esCastrado() || this.esCastrado()) continue;
                int aparearce = Fórmulas.getRandomValor(2, 4);
                if (dragopavo._capacidades.contains(6) || this._capacidades.contains(6)) {
                    aparearce = 3;
                }
                if (aparearce != 3) continue;
                if (dragopavo._sexo == 1) {
                    dragopavo._fecundadaHace = 1;
                    dragopavo._aumentarFecundo.start();
                    dragopavo.setPareja(this._id);
                    this.resAmor(7500);
                    this.resResistencia(7500);
                } else if (this._sexo == 1) {
                    this._fecundadaHace = 1;
                    this._aumentarFecundo.start();
                    this._pareja = dragopavo.getID();
                    dragopavo.resAmor(7500);
                    dragopavo.resResistencia(7500);
                }
                accion = 4;
                break;
            }
        }
        SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(mapa, "0", 1, String.valueOf(this._id), "a" + CryptManager.celdaIDACodigo(this._celdaID) + path);
        this._celdaID = celda;
        this._orientacion = CryptManager.getNumeroPorValorHash(direccion);
        int ID = this._id;
        try {
            Thread.sleep(1250L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        SocketManager.ENVIAR_GDE_FRAME_OBJECT_EXTERNAL(mapa, String.valueOf(celdaprueba) + ";4");
        try {
            Thread.sleep(500L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (accion != 0) {
            SocketManager.ENVIAR_eUK_EMOTE_MAPA(mapa, ID, accion, "");
        }
    }

    public synchronized void moverMonturaAuto(char dir, int casillas, boolean alejar) {
        int accion = 0;
        String path = "";
        Mapa mapa = Mundo.getMapa(this._mapaID);
        if (mapa == null) {
            return;
        }
        if (mapa.getCercado() == null) {
            return;
        }
        Mapa.Cercado cercado = mapa.getCercado();
        char direccion = dir;
        int azar = Fórmulas.getRandomValor(1, 10);
        short celda = this._celdaID;
        short celdaprueba = this._celdaID;
        for (int i = 0; i < casillas; ++i) {
            if (mapa.getCelda(celdaprueba = Pathfinding.getSigIDCeldaMismaDir(celdaprueba, direccion, this._mapaID)) == null) {
                return;
            }
            if (cercado.getCeldayObjeto().containsKey(celdaprueba)) {
                int objeto = cercado.getCeldayObjeto().get(celdaprueba);
                if (objeto == 7758) {
                    this.resSerenidad();
                } else if (objeto == 7781) {
                    if (this._serenidad < 0) {
                        this.aumResistencia();
                    }
                } else if (objeto == 7613) {
                    this.resFatiga();
                    this.aumEnergia();
                } else if (objeto == 7696) {
                    if (this._serenidad > 0) {
                        this.aumAmor();
                    }
                } else if (objeto == 7628) {
                    this.aumSerenidad();
                } else if (objeto == 7594 && this._serenidad <= 2000 && this._serenidad >= -2000) {
                    this.aumMadurez();
                }
                this.aumFatiga();
                break;
            }
            if (!mapa.getCelda(celdaprueba).esCaminable(false) || cercado.getPuerta() == celdaprueba || mapa.celdaSalienteLateral(celda, celdaprueba)) break;
            celda = celdaprueba;
            path = String.valueOf(path) + direccion + CryptManager.celdaIDACodigo(celda);
        }
        if (celda == this._celdaID) {
            this._orientacion = CryptManager.getNumeroPorValorHash(direccion);
            SocketManager.ENVIAR_eD_CAMBIAR_ORIENTACION(mapa, this._id, this._orientacion);
            SocketManager.ENVIAR_GDE_FRAME_OBJECT_EXTERNAL(mapa, String.valueOf(celdaprueba) + ";4");
            SocketManager.ENVIAR_eUK_EMOTE_MAPA(mapa, this._id, accion, "");
            return;
        }
        if (azar == 5) {
            accion = 8;
        }
        if (cercado.getListaCriando().size() > 1) {
            for (Integer pavo : cercado.getListaCriando()) {
                Dragopavo dragopavo = Mundo.getDragopavoPorID(pavo);
                if (dragopavo._sexo == this._sexo || dragopavo.getFecunda() == 0 || this.getFecunda() == 0 || dragopavo.getCelda() != celdaprueba || dragopavo._reprod >= 20 || this._reprod >= 20 || dragopavo.esCastrado() || this.esCastrado()) continue;
                int aparearce = Fórmulas.getRandomValor(2, 4);
                if (dragopavo._capacidades.contains(6) || this._capacidades.contains(6)) {
                    aparearce = 3;
                }
                if (aparearce != 3) continue;
                if (dragopavo._sexo == 1) {
                    dragopavo._fecundadaHace = 1;
                    dragopavo._aumentarFecundo.start();
                    dragopavo.setPareja(this._id);
                    this.resAmor(7500);
                    this.resResistencia(7500);
                } else if (this._sexo == 1) {
                    this._fecundadaHace = 1;
                    this._aumentarFecundo.start();
                    this._pareja = dragopavo.getID();
                    dragopavo.resAmor(7500);
                    dragopavo.resResistencia(7500);
                }
                accion = 4;
                break;
            }
        }
        SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(mapa, "0", 1, String.valueOf(this._id), "a" + CryptManager.celdaIDACodigo(this._celdaID) + path);
        this._celdaID = celda;
        this._orientacion = CryptManager.getNumeroPorValorHash(direccion);
        int ID = this._id;
        try {
            Thread.sleep(1250L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        SocketManager.ENVIAR_GDE_FRAME_OBJECT_EXTERNAL(mapa, String.valueOf(celdaprueba) + ";4");
        try {
            Thread.sleep(500L);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (accion != 0) {
            SocketManager.ENVIAR_eUK_EMOTE_MAPA(mapa, ID, accion, "");
        }
    }

    public int minutosParir() {
        if (this._reprod == 0) {
            return LesGuardians.RATE_TEMPO_NASCER;
        }
        if (this._reprod < 5) {
            return 2 * LesGuardians.RATE_TEMPO_NASCER;
        }
        if (this._reprod < 11) {
            return 3 * LesGuardians.RATE_TEMPO_NASCER;
        }
        if (this._reprod <= 20) {
            return 4 * LesGuardians.RATE_TEMPO_NASCER;
        }
        return 1;
    }
}

