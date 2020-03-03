package realm;

import common.LesGuardians;
import java.io.IOException;
import java.net.ServerSocket;
import realm.RealmThread;

public class RealmServer
implements Runnable {
    private ServerSocket _serverSocket;
    private Thread _thread;
    public static int _totalNoAbonados = 0;
    public static int _totalAbonodos = 0;
    public static int _nroColaID = -1;
    public static int _subscribe = 1;

    public RealmServer() {
        try {
            this._serverSocket = new ServerSocket(LesGuardians.PORTA_SERVIDOR);
            this._thread = new Thread(this);
            this._thread.setDaemon(true);
            this._thread.start();
        }
        catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            LesGuardians.cerrarServer();
        }
    }

    @Override
    public void run() {
        while (LesGuardians._corriendo) {
            try {
                new RealmThread(this._serverSocket.accept());
            }
            catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
                try {
                    System.out.println("Cierre del server de conexion");
                    if (this._serverSocket.isClosed()) continue;
                    this._serverSocket.close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }
    }

    public void cerrarServidorGeneral() {
        try {
            this._serverSocket.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public Thread getThreadDeServGeneral() {
        return this._thread;
    }
}

