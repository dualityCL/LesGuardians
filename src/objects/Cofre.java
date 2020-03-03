package objects;

import common.Constantes;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.World;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Casa;
import objects.Conta;
import objects.Objeto;
import objects.Personagens;

public class Cofre {
    private int _id;
    private int _casaID;
    private short _mapaID;
    private int _celdaID;
    private Map<Integer, Objeto> _objetos = new TreeMap<Integer, Objeto>();
    private long _kamas;
    private String _clave;
    private int _due\u00f1oID;

    public Cofre(int id, int casaID, short mapaID, int celdaID, String objetos, long kamas, String clave, int due\u00f1oID) {
        this._id = id;
        this._casaID = casaID;
        this._mapaID = mapaID;
        this._celdaID = celdaID;
        for (String objeto : objetos.split("\\|")) {
            String[] infos;
            int idObjeto;
            Objeto obj;
            if (objeto.equals("") || (obj = World.getObjeto(idObjeto = Integer.parseInt((infos = objeto.split(":"))[0]))) == null) continue;
            this._objetos.put(obj.getID(), obj);
        }
        this._kamas = kamas;
        this._clave = clave;
        this._due\u00f1oID = due\u00f1oID;
    }

    public int getID() {
        return this._id;
    }

    public int getCasaPorID() {
        return this._casaID;
    }

    public int getMapaID() {
        return this._mapaID;
    }

    public int getCeldaID() {
        return this._celdaID;
    }

    public Map<Integer, Objeto> getObjetos() {
        return this._objetos;
    }

    public long getKamas() {
        return this._kamas;
    }

    public void setKamas(long kamas) {
        this._kamas = kamas;
    }

    public String getClave() {
        return this._clave;
    }

    public void setClave(String clave) {
        this._clave = clave;
    }

    public int getDue\u00f1oID() {
        return this._due\u00f1oID;
    }

    public void setDue\u00f1oID(int due\u00f1oID) {
        this._due\u00f1oID = due\u00f1oID;
    }

    public void bloquear(Personagens perso) {
        SocketManager.ENVIAR_K_CLAVE(perso, "CK1|8");
    }

    public static Cofre getCofrePorUbicacion(int mapaID, int celdaID) {
        for (Map.Entry<Integer, Cofre> cofres : World.getCofres().entrySet()) {
            Cofre cofre = cofres.getValue();
            if (cofre.getMapaID() != mapaID || cofre.getCeldaID() != celdaID) continue;
            return cofre;
        }
        return null;
    }

    public static void codificarCofre(Personagens perso, String packet) {
        Cofre cofre = perso.getCofre();
        if (cofre == null) {
            return;
        }
        if (cofre.esSuCofre(perso, cofre)) {
            SQLManager.CODIFICAR_COFRE(perso, cofre, packet);
            cofre.setClave(packet);
            Cofre.cerrarVentanaCofre(perso);
        } else {
            Cofre.cerrarVentanaCofre(perso);
        }
        perso.setCofre(null);
    }

    public void chekeadoPor(Personagens perso) {
        if (perso.getPelea() != null || perso.getConversandoCon() != 0 || perso.getIntercambiandoCon() != 0 || perso.getHaciendoTrabajo() != null || perso.getIntercambio() != null) {
            return;
        }
        Cofre cofre = perso.getCofre();
        Casa casa = World.getCasa(this._casaID);
        if (cofre == null) {
            return;
        }
        if (cofre.getDue\u00f1oID() == perso.getCuentaID() || perso.getGremio() != null && perso.getGremio().getID() == casa.getGremioID() && casa.tieneDerecho(Constantes.C_SINCODIGOGREMIO)) {
            Cofre.abrirCofre(perso, "-", true);
        } else {
            if (perso.getGremio() == null && casa.tieneDerecho(Constantes.C_ABRIRGREMIO)) {
                SocketManager.ENVIAR_cs_CHAT_MENSAJE(perso, "Este cofre esta abierto s\u00f3lo para los miembros del gremio.", LesGuardians.COR_MSG);
                return;
            }
            if (cofre.getDue\u00f1oID() > 0) {
                SocketManager.ENVIAR_K_CLAVE(perso, "CK0|8");
            } else {
                return;
            }
        }
    }

    public static void abrirCofre(Personagens perso, String packet, boolean esSuCofre) {
        Cofre cofre = perso.getCofre();
        if (cofre == null) {
            return;
        }
        if (packet.compareTo(cofre.getClave()) == 0 || esSuCofre) {
            SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(perso.getCuenta().getEntradaPersonaje().getOut(), 5, "");
            SocketManager.ENVIAR_EL_LISTA_OBJETOS_COFRE(perso, cofre);
            Cofre.cerrarVentanaCofre(perso);
        } else if (packet.compareTo(cofre.getClave()) != 0) {
            SocketManager.ENVIAR_K_CLAVE(perso, "KE");
            Cofre.cerrarVentanaCofre(perso);
            perso.setCofre(null);
        }
    }

    public static void cerrarVentanaCofre(Personagens perso) {
        SocketManager.ENVIAR_K_CLAVE(perso, "V");
    }

    public boolean esSuCofre(Personagens perso, Cofre cofre) {
        return cofre.getDue\u00f1oID() == perso.getCuentaID();
    }

    public static ArrayList<Cofre> getCofresPorCasa(Casa casa) {
        ArrayList<Cofre> cofres = new ArrayList<Cofre>();
        for (Map.Entry<Integer, Cofre> cofre : World.getCofres().entrySet()) {
            if (cofre.getValue().getCasaPorID() != casa.getID()) continue;
            cofres.add(cofre.getValue());
        }
        return cofres;
    }

    public String analizarCofre() {
        String packet = "";
        for (Objeto obj : this._objetos.values()) {
            packet = String.valueOf(packet) + "O" + obj.stringObjetoConGui\u00f1o() + ";";
        }
        if (this.getKamas() != 0L) {
            packet = String.valueOf(packet) + "G" + this.getKamas();
        }
        return packet;
    }

    public void agregarAlCofre(int idObj, int cantidad, Personagens perso) {
        if (this._objetos.size() >= 80) {
            SocketManager.ENVIAR_cs_CHAT_MENSAJE(perso, "Llegaste al m\u00e1ximo de objetos que puede soportar este cofre", LesGuardians.COR_MSG);
            return;
        }
        Objeto persoObj = World.getObjeto(idObj);
        if (persoObj == null) {
            return;
        }
        if (!perso.tieneObjetoID(idObj)) {
            return;
        }
        String str = "";
        if (persoObj.getPosicion() != -1) {
            return;
        }
        Objeto cofreObj = this.objetoSimilarEnElCofre(persoObj);
        int nuevaCant = persoObj.getCantidad() - cantidad;
        if (cofreObj == null) {
            if (nuevaCant <= 0) {
                perso.borrarObjetoSinEliminar(idObj);
                this._objetos.put(idObj, persoObj);
                str = "O+" + idObj + "|" + persoObj.getCantidad() + "|" + persoObj.getModelo().getID() + "|" + persoObj.convertirStatsAString();
                SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(perso, idObj);
            } else {
                persoObj.setCantidad(nuevaCant);
                cofreObj = Objeto.clonarObjeto(persoObj, cantidad);
                World.addObjeto(cofreObj, true);
                this._objetos.put(cofreObj.getID(), cofreObj);
                str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|" + cofreObj.convertirStatsAString();
                SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, persoObj);
            }
        } else if (nuevaCant <= 0) {
            perso.borrarObjetoSinEliminar(idObj);
            World.eliminarObjeto(idObj);
            cofreObj.setCantidad(cofreObj.getCantidad() + persoObj.getCantidad());
            str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|" + cofreObj.convertirStatsAString();
            SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(perso, idObj);
        } else {
            persoObj.setCantidad(nuevaCant);
            cofreObj.setCantidad(cofreObj.getCantidad() + cantidad);
            str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|" + cofreObj.convertirStatsAString();
            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, persoObj);
        }
        for (Personagens pj : perso.getMapa().getPersos()) {
            if (pj.getCofre() == null || this._id != pj.getCofre().getID()) continue;
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(pj, str);
        }
        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
    }

    public void retirarDelCofre(int idObj, int cant, Personagens perso) {
        if (perso.getCofre().getID() != this._id) {
            return;
        }
        Objeto cofreObj = World.getObjeto(idObj);
        if (cofreObj == null) {
            return;
        }
        if (this._objetos.get(idObj) == null) {
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(perso, "O-" + idObj);
            return;
        }
        Objeto persoObj = perso.getObjSimilarInventario(cofreObj);
        String str = "";
        int nuevaCant = cofreObj.getCantidad() - cant;
        if (persoObj == null) {
            if (nuevaCant <= 0) {
                this._objetos.remove(idObj);
                perso.getObjetos().put(idObj, cofreObj);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, cofreObj);
                str = "O-" + idObj;
            } else {
                persoObj = Objeto.clonarObjeto(cofreObj, cant);
                World.addObjeto(persoObj, true);
                cofreObj.setCantidad(nuevaCant);
                perso.getObjetos().put(persoObj.getID(), persoObj);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(perso, persoObj);
                str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|" + cofreObj.convertirStatsAString();
            }
        } else if (nuevaCant <= 0) {
            this._objetos.remove(cofreObj.getID());
            World.eliminarObjeto(cofreObj.getID());
            persoObj.setCantidad(persoObj.getCantidad() + cofreObj.getCantidad());
            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, persoObj);
            str = "O-" + idObj;
        } else {
            cofreObj.setCantidad(nuevaCant);
            persoObj.setCantidad(persoObj.getCantidad() + cant);
            SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(perso, persoObj);
            str = "O+" + cofreObj.getID() + "|" + cofreObj.getCantidad() + "|" + cofreObj.getModelo().getID() + "|" + cofreObj.convertirStatsAString();
        }
        for (Personagens pj : perso.getMapa().getPersos()) {
            if (pj.getCofre() == null || this._id != pj.getCofre().getID()) continue;
            SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(pj, str);
        }
        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
    }

    private Objeto objetoSimilarEnElCofre(Objeto obj) {
        for (Objeto objeto : this._objetos.values()) {
            if (objeto.getModelo().getTipo() == 85 || objeto.getModelo().getID() != obj.getModelo().getID() || !objeto.getStats().sonStatsIguales(obj.getStats())) continue;
            return objeto;
        }
        return null;
    }

    public String analizarObjetoCofreABD() {
        String str = "";
        for (Objeto objeto : this._objetos.values()) {
            str = String.valueOf(str) + objeto.getID() + "|";
        }
        return str;
    }

    public void limpiarCofre() {
        for (Map.Entry<Integer, Objeto> obj : this.getObjetos().entrySet()) {
            World.eliminarObjeto(obj.getKey());
        }
        this.getObjetos().clear();
    }

    public void moverCofreABanco(Conta cuenta) {
        for (Map.Entry<Integer, Objeto> obj : this.getObjetos().entrySet()) {
            cuenta.getObjetosBanco().put(obj.getKey(), obj.getValue());
        }
        this.getObjetos().clear();
    }
}

