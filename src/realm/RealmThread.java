package realm;

import common.Constantes;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.World;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import objects.Conta;
import realm.Pending;
import realm.RealmServer;

public class RealmThread
implements Runnable {
    private BufferedReader _in;
    private Thread _thread;
    public PrintWriter _out;
    private Socket _socketCuenta;
    private String _codigoLlave;
    private int _packetNum = 0;
    private String _nombreCuenta;
    private String _claveEscrita;
    private Conta _cuenta;

    public RealmThread(Socket socket) {
        try {
            try {
                this._socketCuenta = socket;
                this._in = new BufferedReader(new InputStreamReader(this._socketCuenta.getInputStream()));
                this._out = new PrintWriter(this._socketCuenta.getOutputStream());
                this._thread = new Thread(this);
                this._thread.setDaemon(true);
                this._thread.start();
            }
            catch (IOException e) {
                try {
                    if (!this._socketCuenta.isClosed()) {
                        this._socketCuenta.close();
                    }
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                if (this._cuenta != null) {
                    this._cuenta.setEntradaGeneral(null);
                    this._cuenta.setEntradaPersonaje(null);
                    this._cuenta.setTempIP("");
                }
            }
        }
        finally {
            if (this._cuenta != null) {
                this._cuenta.setEntradaGeneral(null);
                this._cuenta.setEntradaPersonaje(null);
                this._cuenta.setTempIP("");
            }
        }
    }

    @Override
    public void run() {
        try {
            try {
                String packet = "";
                char[] charCur = new char[1];
                if (LesGuardians.CONFIG_POLICIA) {
                    SocketManager.ENVIAR_XML_POLICIA(this._out);
                }
                this._codigoLlave = SocketManager.ENVIAR_HC_CODIGO_LLAVE(this._out);
                do {
                    if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r') {
                        packet = String.valueOf(packet) + charCur[0];
                    } else if (!packet.isEmpty()) {
                        ++this._packetNum;
                        this.analizar_Packet_Real(packet);
                        packet = "";
                    }
                    if (this._in.read(charCur, 0, 1) != -1) continue;
                    break;
                } while (LesGuardians._corriendo);
            }
            catch (IOException e) {
                try {
                    this._in.close();
                    this._out.close();
                    if (this._cuenta != null) {
                        this._cuenta.setTempPerso(null);
                        this._cuenta.setEntradaPersonaje(null);
                        this._cuenta.setEntradaGeneral(null);
                        this._cuenta.setTempIP("");
                    }
                    if (!this._socketCuenta.isClosed()) {
                        this._socketCuenta.close();
                    }
                    this._thread.interrupt();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                try {
                    this._in.close();
                    this._out.close();
                    if (this._cuenta != null) {
                        this._cuenta.setTempPerso(null);
                        this._cuenta.setEntradaPersonaje(null);
                        this._cuenta.setEntradaGeneral(null);
                        this._cuenta.setTempIP("");
                    }
                    if (!this._socketCuenta.isClosed()) {
                        this._socketCuenta.close();
                    }
                    this._thread.interrupt();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                try {
                    this._in.close();
                    this._out.close();
                    if (this._cuenta != null) {
                        this._cuenta.setTempPerso(null);
                        this._cuenta.setEntradaPersonaje(null);
                        this._cuenta.setEntradaGeneral(null);
                        this._cuenta.setTempIP("");
                    }
                    if (!this._socketCuenta.isClosed()) {
                        this._socketCuenta.close();
                    }
                    this._thread.interrupt();
                }
                catch (IOException iOException) {}
            }
        }
        finally {
            try {
                this._in.close();
                this._out.close();
                if (this._cuenta != null) {
                    this._cuenta.setTempPerso(null);
                    this._cuenta.setEntradaPersonaje(null);
                    this._cuenta.setEntradaGeneral(null);
                    this._cuenta.setTempIP("");
                }
                if (!this._socketCuenta.isClosed()) {
                    this._socketCuenta.close();
                }
                this._thread.interrupt();
            }
            catch (IOException iOException) {}
        }
    }

    private void analizar_Packet_Real(String packet) {
        try {
            switch (this._packetNum) {
                case 1: {
                    if (packet.equalsIgnoreCase("1.29.1")) break;
                    SocketManager.ENVIAR_AlEv_VERSION_DEL_CLIENTE(this._out);
                    try {
                        this._socketCuenta.close();
                    }
                    catch (IOException iOException) {}
                    break;
                }
                case 2: {
                    this._nombreCuenta = packet.toLowerCase();
                    break;
                }
                case 3: {
                    if (!packet.substring(0, 2).equalsIgnoreCase("#1")) {
                        try {
                            this._socketCuenta.close();
                        }
                        catch (IOException iOException) {
                            // empty catch block
                        }
                    }
                    this._claveEscrita = packet;
                    if (Conta.cuentaLogin(this._nombreCuenta, this._claveEscrita, this._codigoLlave)) {
                        this._cuenta = World.getCuentaPorNombre(this._nombreCuenta);
                        if (this._cuenta.enLinea() && this._cuenta.getEntradaPersonaje() != null) {
                            this._cuenta.getEntradaPersonaje().salir();
                        } else if (this._cuenta.enLinea() && this._cuenta.getEntradaPersonaje() == null) {
                            SocketManager.ENVIAR_AlEc_MISMA_CUENTA_CONECTADA(this._cuenta.getEntradaGeneral()._out);
                            this._cuenta.desconexion();
                        }
                        if (this._cuenta.estaBaneado()) {
                            SocketManager.ENVIAR_AlEb_CUENTA_BANEADA(this._out);
                            try {
                                this._socketCuenta.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                            return;
                        }
                        if (LesGuardians.LIMITE_JOGADORES != -1 && LesGuardians.LIMITE_JOGADORES <= LesGuardians._servidorPersonaje.nroJugadoresLinea() && this._cuenta.getRango() == 0 && this._cuenta.getVIP() == 0) {
                            SocketManager.ENVIAR_AlEw_MUCHOS_JUG_ONLINE(this._out);
                            try {
                                this._socketCuenta.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                            return;
                        }
                        if (World.getGmAcceso() > this._cuenta.getRango()) {
                            SocketManager.ENVIAR_AlEw_MUCHOS_JUG_ONLINE(this._out);
                            return;
                        }
                        String ip = this._socketCuenta.getInetAddress().getHostAddress();
                        if (Constantes.compararConIPBaneadas(ip)) {
                            SocketManager.ENVIAR_AlEb_CUENTA_BANEADA(this._out);
                            return;
                        }
                        if (!LesGuardians.PERMITIR_MULTI_CONTA && World.usandoIP(ip)) {
                            SocketManager.ENVIAR_AlEw_MUCHOS_JUG_ONLINE(this._out);
                            try {
                                this._socketCuenta.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                            return;
                        }
                        if (World.cuentasIP(ip) >= LesGuardians.MAX_MULTI_CONTAS) {
                            SocketManager.ENVIAR_AlEw_MUCHOS_JUG_ONLINE(this._out);
                            try {
                                this._socketCuenta.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                            return;
                        }
                        this._cuenta.setEntradaGeneral(this);
                        this._cuenta.setTempIP(ip);
                        this._cuenta._posicion = ++RealmServer._totalAbonodos;
                        SocketManager.ENVIAR_Ad_Ac_AH_AlK_AQ_INFO_CUENTA_Y_SERVER(this._out, this._cuenta.getApodo(), this._cuenta.getRango() > 0 ? 1 : 0, this._cuenta.getPregunta());
                        break;
                    }
                    SQLManager.CARGAR_CUENTA_POR_NOMBRE(this._nombreCuenta);
                    if (Conta.cuentaLogin(this._nombreCuenta, this._claveEscrita, this._codigoLlave)) {
                        this._cuenta = World.getCuentaPorNombre(this._nombreCuenta);
                        if (this._cuenta.enLinea() && this._cuenta.getEntradaPersonaje() != null) {
                            this._cuenta.getEntradaPersonaje().salir();
                        } else if (this._cuenta.enLinea() && this._cuenta.getEntradaPersonaje() == null) {
                            SocketManager.ENVIAR_AlEc_MISMA_CUENTA_CONECTADA(this._out);
                            SocketManager.ENVIAR_AlEc_MISMA_CUENTA_CONECTADA(this._cuenta.getEntradaGeneral()._out);
                            return;
                        }
                        if (this._cuenta.estaBaneado()) {
                            SocketManager.ENVIAR_AlEb_CUENTA_BANEADA(this._out);
                            try {
                                this._socketCuenta.close();
                            }
                            catch (IOException ip) {
                                // empty catch block
                            }
                            return;
                        }
                        if (LesGuardians.LIMITE_JOGADORES != -1 && LesGuardians.LIMITE_JOGADORES <= LesGuardians._servidorPersonaje.nroJugadoresLinea() && this._cuenta.getRango() == 0 && this._cuenta.getVIP() == 0) {
                            SocketManager.ENVIAR_AlEw_MUCHOS_JUG_ONLINE(this._out);
                            try {
                                this._socketCuenta.close();
                            }
                            catch (IOException ip) {
                                // empty catch block
                            }
                            return;
                        }
                        if (World.getGmAcceso() > this._cuenta.getRango()) {
                            SocketManager.ENVIAR_AlEw_MUCHOS_JUG_ONLINE(this._out);
                            return;
                        }
                        String ip = this._socketCuenta.getInetAddress().getHostAddress();
                        if (Constantes.compararConIPBaneadas(ip)) {
                            SocketManager.ENVIAR_AlEb_CUENTA_BANEADA(this._out);
                            return;
                        }
                        if (!LesGuardians.PERMITIR_MULTI_CONTA && World.usandoIP(ip)) {
                            SocketManager.ENVIAR_AlEw_MUCHOS_JUG_ONLINE(this._out);
                            try {
                                this._socketCuenta.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                            return;
                        }
                        if (World.cuentasIP(ip) >= LesGuardians.MAX_MULTI_CONTAS) {
                            SocketManager.ENVIAR_AlEw_MUCHOS_JUG_ONLINE(this._out);
                            try {
                                this._socketCuenta.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                            return;
                        }
                        this._cuenta.setTempIP(ip);
                        this._cuenta.setEntradaGeneral(this);
                        this._cuenta._posicion = ++RealmServer._totalAbonodos;
                        SocketManager.ENVIAR_Ad_Ac_AH_AlK_AQ_INFO_CUENTA_Y_SERVER(this._out, this._cuenta.getApodo(), this._cuenta.getRango() > 0 ? 1 : 0, this._cuenta.getPregunta());
                        break;
                    }
                    SocketManager.ENVIAR_AlEf_LOGIN_ERROR(this._out);
                    try {
                        this._socketCuenta.close();
                    }
                    catch (IOException ip) {}
                    break;
                }
                default: {
                    if (packet.substring(0, 2).equals("Af")) {
                        --this._packetNum;
                        Pending.EnEspera(this._cuenta);
                        break;
                    }
                    if (packet.substring(0, 2).equals("Ax")) {
                        if (this._cuenta == null) {
                            return;
                        }
                        this._cuenta.getPersonajes().clear();
                        SQLManager.CARGAR_PERSONAJES_POR_CUENTA(this._cuenta);
                        SocketManager.ENVIAR_AxK_TIEMPO_ABONADO_NRO_PJS(this._out, this._cuenta.getPersonajes().size());
                        break;
                    }
                    if (!packet.equals("AX" + LesGuardians.SERVER_ID)) break;
                    LesGuardians._servidorPersonaje.addEsperandoCuenta(this._cuenta);
                    String ip = this._cuenta.getActualIP();
                    SocketManager.ENVIAR_AXK_O_AYK_IP_SERVER(this._out, this._cuenta.getID(), ip.equals("127.0.0.1"));
                    break;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

