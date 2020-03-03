package objects;

import common.Condiciones;
import common.SQLManager;
import common.Mundo;
import java.util.ArrayList;
import objects.Acción;
import objects.Gremio;
import objects.Objeto;
import objects.Personaje;

public class NPCModelo {
    private int _id;
    private String _nombre;
    private int _bonusValue;
    private int _gfxID;
    private int _escalaX;
    private int _escalaY;
    private int _sexo;
    private int _color1;
    private int _color2;
    private int _color3;
    private String _accesorios;
    private int _extraClip;
    private int _customArtWork;
    private int _preguntaID;
    private ArrayList<Objeto.ObjetoModelo> _objVender = new ArrayList();
    private long _kamas;
    private String _listaObjetos = "";

    public NPCModelo(int id, int bonusValue, int gfxid, int escalaX, int escalaY, int sexo, int color1, int color2, int color3, String accesorios, int clip, int artWork, int preguntaID, String objVender, String nombre, long kamas) {
        this._id = id;
        this._bonusValue = bonusValue;
        this._gfxID = gfxid;
        this._escalaX = escalaX;
        this._escalaY = escalaY;
        this._sexo = sexo;
        this._color1 = color1;
        this._color2 = color2;
        this._color3 = color3;
        this._accesorios = accesorios;
        this._extraClip = clip;
        this._customArtWork = artWork;
        this._preguntaID = preguntaID;
        this._nombre = nombre;
        this._kamas = kamas;
        if (objVender.equals("")) {
            return;
        }
        for (String obj : objVender.split("\\,")) {
            try {
                int idModelo = Integer.parseInt(obj);
                Objeto.ObjetoModelo objModelo = Mundo.getObjModelo(idModelo);
                if (objModelo == null) continue;
                this._objVender.add(objModelo);
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        this.actualizarObjetosAVender();
    }

    public int getID() {
        return this._id;
    }

    public long getKamas() {
        return this._kamas;
    }

    public void setKamas(long kamas) {
        this._kamas = kamas;
    }

    public String getNombre() {
        return this._nombre;
    }

    public int getBonusValor() {
        return this._bonusValue;
    }

    public int getGfxID() {
        return this._gfxID;
    }

    public int getTallaX() {
        return this._escalaX;
    }

    public int getTallaY() {
        return this._escalaY;
    }

    public int getSexo() {
        return this._sexo;
    }

    public int getColor1() {
        return this._color1;
    }

    public int getColor2() {
        return this._color2;
    }

    public int getColor3() {
        return this._color3;
    }

    public String getAccesorios() {
        return this._accesorios;
    }

    public int getExtraClip() {
        return this._extraClip;
    }

    public int getCustomArtWork() {
        return this._customArtWork;
    }

    public int getPreguntaID() {
        return this._preguntaID;
    }

    public void configurarNPC(int gfxid, int sexo, int color1, int color2, int color3, String accesorios) {
        this._gfxID = gfxid;
        this._sexo = sexo;
        this._color1 = color1;
        this._color2 = color2;
        this._color3 = color3;
        this._accesorios = accesorios;
        SQLManager.ACTUALIZAR_NPC_COLOR_SEXO(this);
    }

    public void actualizarObjetosAVender() {
        String objetos = "";
        if (this._objVender.isEmpty()) {
            this._listaObjetos = objetos;
        }
        for (Objeto.ObjetoModelo obj : this._objVender) {
            objetos = String.valueOf(objetos) + obj.stringDeStatsParaTienda() + "|";
        }
        this._listaObjetos = objetos;
    }

    public String actualizarStringBD() {
        String objetos = "";
        if (this._objVender.isEmpty()) {
            return objetos;
        }
        for (Objeto.ObjetoModelo obj : this._objVender) {
            objetos = String.valueOf(objetos) + obj.getID() + ",";
        }
        return objetos;
    }

    public String stringObjetosAVender() {
        return this._listaObjetos;
    }

    public boolean addObjetoAVender(Objeto.ObjetoModelo objModelo) {
        if (this._objVender.contains(objModelo)) {
            return false;
        }
        this._objVender.add(objModelo);
        this.actualizarObjetosAVender();
        SQLManager.ACTUALIZAR_NPC_VENTAS(this);
        return true;
    }

    public boolean borrarObjetoAVender(int idModelo) {
        ArrayList<Objeto.ObjetoModelo> nuevosObj = new ArrayList<Objeto.ObjetoModelo>();
        boolean remove = false;
        for (Objeto.ObjetoModelo OM : this._objVender) {
            if (OM.getID() == idModelo) {
                remove = true;
                continue;
            }
            nuevosObj.add(OM);
        }
        this._objVender = nuevosObj;
        this.actualizarObjetosAVender();
        SQLManager.ACTUALIZAR_NPC_VENTAS(this);
        return remove;
    }

    public void setPreguntaID(int pregunta) {
        this._preguntaID = pregunta;
    }

    public boolean tieneObjeto(int idModelo) {
        for (Objeto.ObjetoModelo OM : this._objVender) {
            if (OM.getID() != idModelo) continue;
            return true;
        }
        return false;
    }

    public static class NPC {
        private NPCModelo _modeloBD;
        private short _celdaID;
        private int _id;
        private byte _orientacion;
        private String _nombre;

        public NPC(NPCModelo npcModelo, int id, short celda, byte o, String nombre) {
            this._modeloBD = npcModelo;
            this._id = id;
            this._celdaID = celda;
            this._orientacion = o;
            this._nombre = nombre;
        }

        public NPCModelo getModeloBD() {
            return this._modeloBD;
        }

        public String getNombre() {
            return this._nombre;
        }

        public short getCeldaID() {
            return this._celdaID;
        }

        public int getID() {
            return this._id;
        }

        public byte getOrientacion() {
            return this._orientacion;
        }

        public String analizarGM() {
            String sock = "";
            sock = String.valueOf(sock) + "+";
            sock = String.valueOf(sock) + this._celdaID + ";";
            sock = String.valueOf(sock) + this._orientacion + ";";
            sock = String.valueOf(sock) + "0;";
            sock = String.valueOf(sock) + this._id + ";";
            sock = String.valueOf(sock) + this._modeloBD.getID() + ";";
            sock = String.valueOf(sock) + "-4;";
            String talla = "";
            talla = this._modeloBD.getTallaX() == this._modeloBD.getTallaY() ? "" + this._modeloBD.getTallaY() : String.valueOf(this._modeloBD.getTallaX()) + "x" + this._modeloBD.getTallaY();
            sock = String.valueOf(sock) + this._modeloBD.getGfxID() + "^" + talla + ";";
            sock = String.valueOf(sock) + this._modeloBD.getSexo() + ";";
            sock = String.valueOf(sock) + (this._modeloBD.getColor1() != -1 ? Integer.toHexString(this._modeloBD.getColor1()) : "-1") + ";";
            sock = String.valueOf(sock) + (this._modeloBD.getColor2() != -1 ? Integer.toHexString(this._modeloBD.getColor2()) : "-1") + ";";
            sock = String.valueOf(sock) + (this._modeloBD.getColor3() != -1 ? Integer.toHexString(this._modeloBD.getColor3()) : "-1") + ";";
            sock = String.valueOf(sock) + this._modeloBD.getAccesorios() + ";";
            sock = String.valueOf(sock) + (this._modeloBD.getExtraClip() != -1 ? Integer.valueOf(this._modeloBD.getExtraClip()) : "") + ";";
            sock = String.valueOf(sock) + this._modeloBD.getCustomArtWork();
            return sock;
        }

        public void setCeldaID(short id) {
            this._celdaID = id;
        }

        public void setOrientacion(byte o) {
            this._orientacion = o;
        }
    }

    public static class PreguntaNPC {
        private int _id;
        private String _respuestas;
        private String _args;
        private String _condicion;
        private int _falsaPregunta;

        public PreguntaNPC(int id, String respuestas, String args, String cond, int falsaPregunta) {
            this._id = id;
            this._respuestas = respuestas;
            this._args = args;
            this._condicion = cond;
            this._falsaPregunta = falsaPregunta;
        }

        public int getID() {
            return this._id;
        }

        public String stringArgParaDialogo(Personaje perso) {
            try {
                if (!Condiciones.validaCondiciones(null, perso, this._condicion)) {
                    return Mundo.getPreguntaNPC(this._falsaPregunta).stringArgParaDialogo(perso);
                }
                String str = String.valueOf(this._id);
                if (!this._args.equals("")) {
                    str = String.valueOf(str) + ";" + this.analizarArgumentos(this._args, perso);
                }
                if (!this._respuestas.isEmpty()) {
                    str = String.valueOf(str) + "|" + this._respuestas;
                }
                return str;
            }
            catch (Exception e) {
                System.out.println("hay un error en el NPC Pregunta " + this._id);
                return "";
            }
        }

        public String stringGremio(Personaje perso, Gremio gremio) {
            String str = String.valueOf(this._id);
            if (!this._args.equals("")) {
                str = String.valueOf(str) + ";" + this.analizarArgumentosGremio(this._args, gremio);
            }
            return str;
        }

        public String getRespuestas() {
            return this._respuestas;
        }

        private String analizarArgumentos(String args, Personaje perso) {
            String arg = args;
            arg = arg.replace("[nombre]", perso.getStringVar("nombre"));
            arg = arg.replace("[costoBanco]", perso.getStringVar("costoBanco"));
            arg = arg.replace("[lider]", Mundo.liderRanking);
            arg = arg.replace("[npcKamas]", String.valueOf(Mundo.getNPCModelo(408).getKamas()));
            return arg;
        }

        private String analizarArgumentosGremio(String args, Gremio gremio) {
            String arg = args;
            arg = arg.replace("[gremio]", gremio.getInfoGremio());
            return arg;
        }

        public void setRespuestas(String respuestas) {
            this._respuestas = respuestas;
        }
    }

    public static class RespuestaNPC {
        private int _id;
        private ArrayList<Acción> _acciones = new ArrayList();

        public RespuestaNPC(int id) {
            this._id = id;
        }

        public int getID() {
            return this._id;
        }

        public void addAccion(Acción accion) {
            ArrayList<Acción> c = new ArrayList<Acción>();
            c.addAll(this._acciones);
            for (Acción a : c) {
                if (a.getID() != accion.getID()) continue;
                this._acciones.remove(a);
            }
            this._acciones.add(accion);
        }

        public void aplicar(Personaje perso) {
            for (Acción accion : this._acciones) {
                accion.aplicar(perso, null, -1, (short)-1);
            }
        }

        public boolean esOtroDialogo() {
            for (Acción accion : this._acciones) {
                if (accion.getID() != 1) continue;
                return true;
            }
            return false;
        }
    }
}

