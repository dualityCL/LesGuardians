package realm;

import common.SocketManager;
import objects.Conta;
import realm.RealmServer;

public class Pending {
    public static void EnEspera(Conta C) {
        if (C == null) {
            return;
        }
        if (C._posicion <= 1) {
            try {
                Thread.sleep(750L);
                if (C == null || C.getEntradaGeneral() == null || C.getEntradaGeneral()._out == null) {
                    return;
                }
                SocketManager.ENVIAR_Af_ABONADOS_POSCOLA(C.getEntradaGeneral()._out, 1, RealmServer._totalAbonodos, RealmServer._totalNoAbonados, "1", RealmServer._nroColaID);
                C._posicion = -1;
                --RealmServer._totalAbonodos;
            }
            catch (InterruptedException e) {
                SocketManager.ENVIAR_AlEc_MISMA_CUENTA_CONECTADA(C.getEntradaGeneral()._out);
                System.out.println("Error : " + e.getMessage());
            }
        } else {
            try {
                Thread.sleep(750 * C._posicion);
                if (C == null || C.getEntradaGeneral() == null || C.getEntradaGeneral()._out == null) {
                    return;
                }
                SocketManager.ENVIAR_Af_ABONADOS_POSCOLA(C.getEntradaGeneral()._out, 1, RealmServer._totalAbonodos, RealmServer._totalNoAbonados, "1", RealmServer._nroColaID);
                C._posicion = -1;
                --RealmServer._totalAbonodos;
            }
            catch (InterruptedException e) {
                SocketManager.ENVIAR_AlEc_MISMA_CUENTA_CONECTADA(C.getEntradaGeneral()._out);
                System.out.println("Error : " + e.getMessage());
            }
        }
    }
}

