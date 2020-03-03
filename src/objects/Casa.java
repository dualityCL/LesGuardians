package objects;

import common.Constantes;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.World;
import java.util.Map;
import java.util.TreeMap;
import objects.Cofre;
import objects.Conta;
import objects.Guild;
import objects.Personagens;

public class Casa {
    private int _id;
    private short _mapaIDFuera;
    private short _celdaIDFuera;
    private int _due\u00f1oID;
    private int _precioVenta;
    private int _gremioID;
    private int _derechosGremio;
    private int _acceso;
    private String _clave;
    private short _mapaIDDentro;
    private short _celdaIDDentro;
    private Map<Integer, Boolean> _tieneDerecho = new TreeMap<Integer, Boolean>();

    public Casa(int id, short mapaIDFuera, short celdaIDFuera, int due\u00f1o, int precio, int gremioID, int acceso, String key, int derechosGremio, short mapaIDDentro, short celdaIDDentro) {
        this._id = id;
        this._mapaIDFuera = mapaIDFuera;
        this._celdaIDFuera = celdaIDFuera;
        this._due\u00f1oID = due\u00f1o;
        this._precioVenta = precio;
        this._gremioID = gremioID;
        this._acceso = acceso;
        this._clave = key;
        this._derechosGremio = derechosGremio;
        this.analizarDerechos(derechosGremio);
        this._mapaIDDentro = mapaIDDentro;
        this._celdaIDDentro = celdaIDDentro;
    }

    public int getID() {
        return this._id;
    }

    public short getMapaIDFuera() {
        return this._mapaIDFuera;
    }

    public short getCeldaIDFuera() {
        return this._celdaIDFuera;
    }

    public int getDue\u00f1oID() {
        return this._due\u00f1oID;
    }

    public void setDue\u00f1oID(int id) {
        this._due\u00f1oID = id;
    }

    public int getPrecioVenta() {
        return this._precioVenta;
    }

    public void setPrecio(int precio) {
        this._precioVenta = precio;
    }

    public int getGremioID() {
        return this._gremioID;
    }

    public void setGremioID(int gremioID) {
        this._gremioID = gremioID;
    }

    public int getDerechosGremio() {
        return this._derechosGremio;
    }

    public void setDerechosGremio(int derechosGremio) {
        this._derechosGremio = derechosGremio;
    }

    public int getAcceso() {
        return this._acceso;
    }

    public void setAcceso(int accesso) {
        this._acceso = accesso;
    }

    public String getClave() {
        return this._clave;
    }

    public void setClave(String clave) {
        this._clave = clave;
    }

    public short getMapaIDDentro() {
        return this._mapaIDDentro;
    }

    public short getCeldaIDDentro() {
        return this._celdaIDDentro;
    }

    public static Casa getCasaPorUbicacion(int mapaID, int celdaID) {
        for (Map.Entry<Integer, Casa> casa : World.getCasas().entrySet()) {
            if (casa.getValue().getMapaIDFuera() != mapaID || casa.getValue().getCeldaIDFuera() != celdaID) continue;
            return casa.getValue();
        }
        return null;
    }

    public static void cargarCasa(Personagens perso, int nuevoMapaID) {
        for (Map.Entry<Integer, Casa> casa : World.getCasas().entrySet()) {
            Conta cuenta;
            if (casa.getValue().getMapaIDFuera() != nuevoMapaID) continue;
            String packet = "P" + casa.getValue().getID() + "|";
            packet = casa.getValue().getDue\u00f1oID() > 0 ? ((cuenta = World.getCuenta(casa.getValue().getDue\u00f1oID())) == null ? String.valueOf(packet) + "undefined;" : String.valueOf(packet) + World.getCuenta(casa.getValue().getDue\u00f1oID()).getApodo() + ";") : String.valueOf(packet) + ";";
            packet = casa.getValue().getPrecioVenta() > 0 ? String.valueOf(packet) + "1" : String.valueOf(packet) + "0";
            if (casa.getValue().getGremioID() > 0) {
                Guild gremio = World.getGremio(casa.getValue().getGremioID());
                String nombreGremio = gremio.getNombre();
                String emblemaGremio = gremio.getEmblema();
                if (gremio.getPjMiembros().size() < 10) {
                    SQLManager.UPDATE_CASA_GREMIO(casa.getValue(), 0, 0);
                }
                if (perso.getGremio() != null && perso.getGremio().getID() == casa.getValue().getGremioID() && casa.getValue().tieneDerecho(Constantes.H_GBLASON) && gremio.getPjMiembros().size() > 9) {
                    packet = String.valueOf(packet) + ";" + nombreGremio + ";" + emblemaGremio;
                } else if (casa.getValue().tieneDerecho(Constantes.H_OBLASON) && gremio.getPjMiembros().size() > 9) {
                    packet = String.valueOf(packet) + ";" + nombreGremio + ";" + emblemaGremio;
                }
            }
            SocketManager.ENVIAR_h_CASA(perso, packet);
            if (casa.getValue().getDue\u00f1oID() != perso.getCuentaID()) continue;
            String packet1 = "L+|" + casa.getValue().getID() + ";" + casa.getValue().getAcceso() + ";";
            if (casa.getValue().getPrecioVenta() <= 0) {
                packet1 = String.valueOf(packet1) + "0;" + casa.getValue().getPrecioVenta();
            } else if (casa.getValue().getPrecioVenta() > 0) {
                packet1 = String.valueOf(packet1) + "1;" + casa.getValue().getPrecioVenta();
            }
            SocketManager.ENVIAR_h_CASA(perso, packet1);
        }
    }

    public void respondeA(Personagens perso) {
        if (perso.getPelea() != null || perso.getConversandoCon() != 0 || perso.getIntercambiandoCon() != 0 || perso.getHaciendoTrabajo() != null || perso.getIntercambio() != null) {
            return;
        }
        Casa casa = perso.getCasa();
        if (casa == null) {
            return;
        }
        if (casa.getDue\u00f1oID() == perso.getCuentaID() || perso.getGremio() != null && perso.getGremio().getID() == casa.getGremioID() && this.tieneDerecho(Constantes.H_SINCODIGOGREMIO)) {
            Casa.abrirCasa(perso, "-", true);
        } else if (casa.getDue\u00f1oID() > 0) {
            SocketManager.ENVIAR_K_CLAVE(perso, "CK0|8");
        } else if (casa.getDue\u00f1oID() == 0) {
            Casa.abrirCasa(perso, "-", false);
        } else {
            return;
        }
    }

    public static void abrirCasa(Personagens perso, String packet, boolean esHogar) {
        Casa casa = perso.getCasa();
        if (!casa.tieneDerecho(Constantes.H_ABRIRGREMIO) && packet.compareTo(casa.getClave()) == 0 || esHogar) {
            perso.teleport(casa.getMapaIDDentro(), casa.getCeldaIDDentro());
            Casa.cerrarVentana(perso);
        } else if (packet.compareTo(casa.getClave()) != 0 || casa.tieneDerecho(Constantes.H_ABRIRGREMIO)) {
            SocketManager.ENVIAR_K_CLAVE(perso, "KE");
            SocketManager.ENVIAR_K_CLAVE(perso, "V");
        }
    }

    public void comprarEstaCasa(Personagens perso) {
        Casa casa = perso.getCasa();
        if (casa == null) {
            return;
        }
        String str = "CK" + casa.getID() + "|" + casa.getPrecioVenta();
        SocketManager.ENVIAR_h_CASA(perso, str);
    }

    public static void comprarCasa(Personagens perso) {
        Casa casa = perso.getCasa();
        if (Casa.tieneOtraCasa(perso)) {
            SocketManager.ENVIAR_Im_INFORMACION(perso, "132;1");
            return;
        }
        if (perso.getKamas() < (long)casa.getPrecioVenta()) {
            return;
        }
        long nuevasKamas = perso.getKamas() - (long)casa.getPrecioVenta();
        perso.setKamas(nuevasKamas);
        int kamasCofre = 0;
        for (Cofre cofre : Cofre.getCofresPorCasa(casa)) {
            if (casa.getDue\u00f1oID() > 0) {
                cofre.moverCofreABanco(World.getCuenta(casa.getDue\u00f1oID()));
            }
            kamasCofre = (int)((long)kamasCofre + cofre.getKamas());
            cofre.setKamas(0L);
            cofre.setClave("-");
            cofre.setDue\u00f1oID(0);
            SQLManager.ACTUALIZAR_COFRE(cofre);
        }
        if (casa.getDue\u00f1oID() > 0) {
            Conta cuentaVendedor = World.getCuenta(casa.getDue\u00f1oID());
            long bancoKamas = cuentaVendedor.getKamasBanco() + (long)casa.getPrecioVenta() + (long)kamasCofre;
            cuentaVendedor.setKamasBanco(bancoKamas);
            Personagens vendedor = cuentaVendedor.getTempPersonaje();
            if (vendedor != null) {
                SocketManager.ENVIAR_cs_CHAT_MENSAJE(vendedor, "Una casa ha sido vendida a " + casa.getPrecioVenta() + " kamas.", LesGuardians.COR_MSG);
                SQLManager.SALVAR_PERSONAJE(vendedor, true);
            }
            SQLManager.SALVAR_CUENTA(cuentaVendedor);
        }
        casa._due\u00f1oID = perso.getID();
        if (perso.getGremio() != null) {
            casa._gremioID = perso.getGremio().getID();
        }
        SQLManager.SALVAR_PERSONAJE(perso, true);
        SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
        SQLManager.COMPRAR_CASA(perso, casa);
        Casa.cerrarVentanaCompra(perso);
        for (Personagens z : perso.getMapa().getPersos()) {
            Casa.cargarCasa(z, z.getMapa().getID());
        }
    }

    public void venderla(Personagens perso) {
        Casa casa = perso.getCasa();
        if (this.esSuCasa(perso, casa)) {
            String str = "CK" + casa.getID() + "|" + casa.getPrecioVenta();
            SocketManager.ENVIAR_h_CASA(perso, str);
            return;
        }
    }

    public static void precioVenta(Personagens perso, String packet) {
        Casa casa = perso.getCasa();
        int precio = Integer.parseInt(packet);
        if (casa.esSuCasa(perso, casa)) {
            SocketManager.ENVIAR_h_CASA(perso, "V");
            SocketManager.ENVIAR_h_CASA(perso, "SK" + casa.getID() + "|" + precio);
            SQLManager.VENDER_CASA(casa, precio);
            for (Personagens z : perso.getMapa().getPersos()) {
                Casa.cargarCasa(z, z.getMapa().getID());
            }
            return;
        }
    }

    public boolean esSuCasa(Personagens perso, Casa casa) {
        return casa.getDue\u00f1oID() == perso.getCuentaID();
    }

    public static void cerrarVentana(Personagens perso) {
        SocketManager.ENVIAR_K_CLAVE(perso, "V");
    }

    public static void cerrarVentanaCompra(Personagens perso) {
        SocketManager.ENVIAR_h_CASA(perso, "V");
    }

    public void bloquear(Personagens perso) {
        SocketManager.ENVIAR_K_CLAVE(perso, "CK1|8");
    }

    public static void codificarCasa(Personagens perso, String packet) {
        Casa casa = perso.getCasa();
        if (casa.esSuCasa(perso, casa)) {
            SQLManager.CODIGO_CASA(perso, casa, packet);
            Casa.cerrarVentana(perso);
            return;
        }
        Casa.cerrarVentana(perso);
    }

    public static String analizarCasaGremio(Personagens perso) {
        boolean primero = true;
        String packet = "+";
        for (Map.Entry<Integer, Casa> entry : World.getCasas().entrySet()) {
            Casa casa = entry.getValue();
            if (casa.getGremioID() != perso.getGremio().getID() || casa.getDerechosGremio() <= 0) continue;
            if (primero) {
                packet = String.valueOf(packet) + entry.getKey() + ";";
                packet = World.getPersonaje(casa.getDue\u00f1oID()) == null ? String.valueOf(packet) + "DUE\u00d1O BUGEADO;" : String.valueOf(packet) + World.getPersonaje(casa.getDue\u00f1oID()).getCuenta().getApodo() + ";";
                packet = String.valueOf(packet) + World.getMapa(casa.getMapaIDDentro()).getX() + "," + World.getMapa(casa.getMapaIDDentro()).getY() + ";";
                packet = String.valueOf(packet) + "0;";
                packet = String.valueOf(packet) + casa.getDerechosGremio();
                primero = false;
                continue;
            }
            packet = String.valueOf(packet) + "|";
            packet = String.valueOf(packet) + entry.getKey() + ";";
            packet = World.getPersonaje(casa.getDue\u00f1oID()) == null ? String.valueOf(packet) + "DUE\u00d1O BUGEADO;" : String.valueOf(packet) + World.getPersonaje(casa.getDue\u00f1oID()).getCuenta().getApodo() + ";";
            packet = String.valueOf(packet) + World.getMapa(casa.getMapaIDDentro()).getX() + "," + World.getMapa(casa.getMapaIDDentro()).getY() + ";";
            packet = String.valueOf(packet) + "0;";
            packet = String.valueOf(packet) + casa.getDerechosGremio();
        }
        return packet;
    }

    public static boolean tieneOtraCasa(Personagens perso) {
        for (Map.Entry<Integer, Casa> casa : World.getCasas().entrySet()) {
            if (casa.getValue().getDue\u00f1oID() != perso.getCuentaID()) continue;
            return true;
        }
        return false;
    }

    public static void analizarCasaGremio(Personagens perso, String packet) {
        Casa casa = perso.getCasa();
        if (perso.getGremio() == null) {
            return;
        }
        if (packet != null) {
            if (packet.charAt(0) == '+') {
                byte maxCasasPorGremio = (byte)Math.floor(perso.getGremio().getNivel() / 10);
                if (Casa.casaGremio(perso.getGremio().getID()) >= maxCasasPorGremio) {
                    return;
                }
                if (perso.getGremio().getPjMiembros().size() < 10) {
                    return;
                }
                SQLManager.UPDATE_CASA_GREMIO(casa, perso.getGremio().getID(), 0);
                Casa.analizarCasaGremio(perso, null);
            } else if (packet.charAt(0) == '-') {
                SQLManager.UPDATE_CASA_GREMIO(casa, 0, 0);
                Casa.analizarCasaGremio(perso, null);
            } else {
                SQLManager.UPDATE_CASA_GREMIO(casa, casa.getGremioID(), Integer.parseInt(packet));
                casa.analizarDerechos(Integer.parseInt(packet));
            }
        } else if (packet == null) {
            if (casa.getGremioID() <= 0) {
                SocketManager.ENVIAR_h_CASA(perso, "G" + casa.getID());
            } else if (casa.getGremioID() > 0) {
                SocketManager.ENVIAR_h_CASA(perso, "G" + casa.getID() + ";" + perso.getGremio().getNombre() + ";" + perso.getGremio().getEmblema() + ";" + casa.getDerechosGremio());
            }
        }
    }

    public static byte casaGremio(int gremioID) {
        byte i = 0;
        for (Map.Entry<Integer, Casa> casa : World.getCasas().entrySet()) {
            if (casa.getValue().getGremioID() != gremioID) continue;
            i = (byte)(i + 1);
        }
        return i;
    }

    public boolean tieneDerecho(int derecho) {
        return this._tieneDerecho.get(derecho);
    }

    public void iniciarDerechos() {
        this._tieneDerecho.put(Constantes.H_GBLASON, false);
        this._tieneDerecho.put(Constantes.H_OBLASON, false);
        this._tieneDerecho.put(Constantes.H_SINCODIGOGREMIO, false);
        this._tieneDerecho.put(Constantes.H_ABRIRGREMIO, false);
        this._tieneDerecho.put(Constantes.C_SINCODIGOGREMIO, false);
        this._tieneDerecho.put(Constantes.C_ABRIRGREMIO, false);
        this._tieneDerecho.put(Constantes.H_DESCANSOGREMIO, false);
        this._tieneDerecho.put(Constantes.H_TELEPORTGREMIO, false);
    }

    public void analizarDerechos(int total) {
        if (this._tieneDerecho.isEmpty()) {
            this.iniciarDerechos();
        }
        if (total == 1) {
            return;
        }
        if (this._tieneDerecho.size() > 0) {
            this._tieneDerecho.clear();
        }
        this.iniciarDerechos();
        Integer[] mapKey = this._tieneDerecho.keySet().toArray(new Integer[this._tieneDerecho.size()]);
        block0 : while (total > 0) {
            for (int i = this._tieneDerecho.size() - 1; i < this._tieneDerecho.size(); --i) {
                if (mapKey[i] > total) continue;
                total ^= mapKey[i].intValue();
                this._tieneDerecho.put(mapKey[i], true);
                continue block0;
            }
        }
    }

    public static void salir(Personagens perso, String packet) {
        Casa casa = perso.getCasa();
        if (!casa.esSuCasa(perso, casa)) {
            return;
        }
        int Pid = Integer.parseInt(packet);
        Personagens objetivo = World.getPersonaje(Pid);
        if (objetivo == null || !objetivo.enLinea() || objetivo.getPelea() != null || objetivo.getMapa().getID() != perso.getMapa().getID()) {
            return;
        }
        objetivo.teleport(casa.getMapaIDFuera(), casa.getCeldaIDFuera());
        SocketManager.ENVIAR_Im_INFORMACION(objetivo, "018;" + perso.getNombre());
    }

    public static Casa getCasaDePj(Personagens perso) {
        try {
            for (Map.Entry<Integer, Casa> entry : World.getCasas().entrySet()) {
                Casa casa = entry.getValue();
                if (casa.getDue\u00f1oID() != perso.getCuentaID()) continue;
                return casa;
            }
        }
        catch (NullPointerException e) {
            return null;
        }
        return null;
    }

    public static void borrarCasaGremio(int gremioID) {
        for (Map.Entry<Integer, Casa> entry : World.getCasas().entrySet()) {
            Casa casa = entry.getValue();
            if (casa.getGremioID() != gremioID) continue;
            casa.setDerechosGremio(0);
            casa.setGremioID(0);
        }
        SQLManager.UPDATE_CASA_GREMIO_A_CERO(gremioID);
    }
}

