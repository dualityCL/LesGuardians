package common;

import common.CryptManager;
import common.Fórmulas;
import common.Pathfinding;
import common.World;
import game.GameThread;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import objects.Fight;
import objects.Fight.Luchador;
import objects.Maps;
import objects.Spell;
import objects.SpellEffect;


public class IA {

    public static class MotorIA
    implements Runnable {
        private Fight _pelea;
        private Fight.Luchador _atacante;
        private static boolean stop = false;
        private Thread _t;

        public MotorIA(Fight.Luchador atacante, Fight pelea) {
            this._atacante = atacante;
            this._pelea = pelea;
            this._t = new Thread(this);
            this._t.setDaemon(true);
            this._t.start();
        }

        @Override
        public void run() {
            stop = false;
            if (this._atacante.getMob() == null) {
                if (this._atacante.esDoble()) {
                    MotorIA.tipo_5(this._atacante, this._pelea);
                    try {
                        Thread.sleep(1500L);
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                    this._pelea.finTurno(this._atacante);
                } else if (this._atacante.esRecaudador()) {
                    MotorIA.tipo_Recaudador(this._atacante, this._pelea);
                    try {
                        Thread.sleep(1500L);
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                    this._pelea.finTurno(this._atacante);
                } else if (this._atacante.esPrisma()) {
                    MotorIA.tipo_Prisma(this._atacante, this._pelea);
                    try {
                        Thread.sleep(1500L);
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                    this._pelea.finTurno(this._atacante);
                } else {
                    try {
                        Thread.sleep(1500L);
                    }
                    catch (InterruptedException interruptedException) {
                        // empty catch block
                    }
                    this._pelea.finTurno(this._atacante);
                }
            } else if (this._atacante.getMob().getModelo() == null) {
                this._pelea.finTurno(this._atacante);
            } else {
                switch (this._atacante.getMob().getModelo().getTipoInteligencia()) {
                    case 0: {
                        MotorIA.tipo_0(this._atacante, this._pelea);
                        break;
                    }
                    case 1: {
                        MotorIA.tipo_1(this._atacante, this._pelea);
                        break;
                    }
                    case 2: {
                        MotorIA.tipo_2(this._atacante, this._pelea);
                        break;
                    }
                    case 3: {
                        MotorIA.tipo_3(this._atacante, this._pelea);
                        break;
                    }
                    case 4: {
                        MotorIA.tipo_4(this._atacante, this._pelea);
                        break;
                    }
                    case 5: {
                        MotorIA.tipo_5(this._atacante, this._pelea);
                        break;
                    }
                    case 6: {
                        MotorIA.tipo_6(this._atacante, this._pelea);
                        break;
                    }
                    case 7: {
                        MotorIA.tipo_7(this._atacante, this._pelea);
                        break;
                    }
                    case 8: {
                        MotorIA.tipo_8(this._atacante, this._pelea);
                        break;
                    }
                    case 9: {
                        MotorIA.tipo_9(this._atacante, this._pelea);
                        break;
                    }
                    case 10: {
                        MotorIA.tipo_10(this._atacante, this._pelea);
                        break;
                    }
                    case 11: {
                        MotorIA.tipo_11(this._atacante, this._pelea);
                        break;
                    }
                    case 12: {
                        MotorIA.tipo_12(this._atacante, this._pelea);
                        break;
                    }
                    case 13: {
                        MotorIA.tipo_13(this._atacante, this._pelea);
                        break;
                    }
                    case 14: {
                        MotorIA.tipo_14(this._atacante, this._pelea);
                        break;
                    }
                    case 15: {
                        MotorIA.tipo_15(this._atacante, this._pelea);
                        break;
                    }
                    case 16: {
                        MotorIA.tipo_16(this._atacante, this._pelea);
                    }
                }
                try {
                    Thread.sleep(1500L);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
                if (!this._atacante.estaMuerto()) {
                    this._pelea.finTurno(this._atacante);
                }
            }
        }

        private static void tipo_0(Fight.Luchador lanzador, Fight pelea) {
            stop = true;
        }

        private static void tipo_1(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                int ataque;
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                int porcPDV = lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff();
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                Fight.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (porcPDV > 15) {
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    }
                    if (!MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
                        if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador) || MotorIA.curaSiEsPosible(pelea, lanzador, true) || MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo)) continue;
                        enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                        if (enemigo == null) {
                            MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                            return;
                        }
                        if (MotorIA.acercarseA(pelea, lanzador, enemigo) || MotorIA.invocarSiEsPosible1(pelea, lanzador)) continue;
                        stop = true;
                        continue;
                    }
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    }
                    continue;
                }
                if (MotorIA.curaSiEsPosible(pelea, lanzador, true)) continue;
                ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                }
                if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador) || MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo) || MotorIA.invocarSiEsPosible1(pelea, lanzador)) continue;
                enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (MotorIA.acercarseA(pelea, lanzador, enemigo)) continue;
                stop = true;
            }
        }

        private static void tipo_2(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    return;
                }
                int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                }
                stop = true;
            }
        }

        private static void tipo_3(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                }
                enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (MotorIA.acercarseA(pelea, lanzador, enemigo)) continue;
                stop = true;
            }
        }

        private static void tipo_4(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                if (ataque == 0 && !stop) {
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                    }
                } else if (MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
                    ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                    if (ataque == 0 && !stop) {
                        while (ataque == 0 && !stop) {
                            if (ataque == 5) {
                                stop = true;
                            }
                            ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                        }
                    }
                } else {
                    enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                    if (enemigo == null) {
                        MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                        return;
                    }
                    if (MotorIA.acercarseA(pelea, lanzador, enemigo)) {
                        ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                        while (ataque == 0 && !stop) {
                            if (ataque == 5) {
                                stop = true;
                            }
                            ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                        }
                    }
                }
                MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                stop = true;
            }
        }

        private static void tipo_5(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (MotorIA.acercarseA(pelea, lanzador, enemigo)) continue;
                stop = true;
            }
        }

        private static void tipo_6(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
                if (MotorIA.acercarseA(pelea, lanzador, amigo)) continue;
                while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, amigo)) {
                }
                while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, lanzador)) {
                }
                stop = true;
            }
        }

        private static void tipo_7(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                }
                enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (MotorIA.acercarseA(pelea, lanzador, enemigo)) continue;
                stop = true;
            }
        }

        private static void tipo_8(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
                if (amigo == null) {
                    return;
                }
                if (MotorIA.acercarseA(pelea, lanzador, amigo)) continue;
                while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, amigo)) {
                }
                stop = true;
            }
        }

        private static void tipo_9(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, lanzador)) {
                }
                stop = true;
            }
        }

        private static void tipo_10(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                }
                while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, lanzador)) {
                }
                stop = true;
            }
        }

        private static void tipo_11(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                int ataque = MotorIA.atacaSiEsPosible3(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible3(pelea, lanzador);
                }
                while (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador)) {
                }
                enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (MotorIA.acercarseA(pelea, lanzador, enemigo)) continue;
                stop = true;
            }
        }

        private static void tipo_12(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                int ataque = 0;
                if (MotorIA.invocarSiEsPosible2(pelea, lanzador)) continue;
                if (!MotorIA.buffeaKralamar(pelea, lanzador, lanzador)) {
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    }
                    stop = true;
                    continue;
                }
                ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                }
                stop = true;
            }
        }

        private static void tipo_13(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                if (MotorIA.buffeaSiEsPosible2(pelea, lanzador, lanzador)) continue;
                int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
                }
                stop = true;
            }
        }

        private static void tipo_14(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                int ataque;
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                int porcPDV = lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff();
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                Fight.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (porcPDV > 15) {
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    }
                    if (!MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
                        if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador) || MotorIA.curaSiEsPosible(pelea, lanzador, false) || MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo)) continue;
                        enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                        if (enemigo == null) {
                            MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                            return;
                        }
                        if (MotorIA.acercarseA(pelea, lanzador, enemigo)) continue;
                        stop = true;
                        continue;
                    }
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    }
                    continue;
                }
                if (MotorIA.curaSiEsPosible(pelea, lanzador, true)) continue;
                ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                }
                if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador) || MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo)) continue;
                enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (MotorIA.acercarseA(pelea, lanzador, enemigo)) continue;
                stop = true;
            }
        }

        private static void tipo_15(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                int ataque;
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                int porcPDV = lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff();
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (porcPDV > 15) {
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    }
                    if (!MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
                        if (MotorIA.curaSiEsPosible(pelea, lanzador, false)) continue;
                        enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                        if (enemigo == null) {
                            MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                            return;
                        }
                        if (MotorIA.acercarseA(pelea, lanzador, enemigo) || MotorIA.invocarSiEsPosible1(pelea, lanzador)) continue;
                        stop = true;
                        continue;
                    }
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                    }
                    continue;
                }
                if (MotorIA.curaSiEsPosible(pelea, lanzador, true)) continue;
                ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                }
                if (MotorIA.invocarSiEsPosible1(pelea, lanzador)) continue;
                enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                if (MotorIA.acercarseA(pelea, lanzador, enemigo)) continue;
                stop = true;
            }
        }

        private static void tipo_16(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                Fight.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
                if (enemigo == null) {
                    MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                    return;
                }
                int ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                }
                if (!MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
                    if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador) || MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo)) continue;
                    enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                    if (enemigo == null) {
                        MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                        return;
                    }
                    if (MotorIA.acercarseA(pelea, lanzador, enemigo) || MotorIA.invocarSiEsPosible1(pelea, lanzador)) continue;
                    stop = true;
                    continue;
                }
                ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
                }
            }
        }

        private static void tipo_Prisma(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                int ataque;
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                Fight.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
                if (amigo != null) {
                    if (MotorIA.curaSiEsPosiblePrisma(pelea, lanzador, false) || MotorIA.buffeaSiEsPosiblePrisma(pelea, lanzador, amigo) || MotorIA.buffeaSiEsPosiblePrisma(pelea, lanzador, lanzador)) continue;
                    ataque = MotorIA.atacaSiEsPosiblePrisma(pelea, lanzador);
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosiblePrisma(pelea, lanzador);
                    }
                    stop = true;
                    continue;
                }
                ataque = MotorIA.atacaSiEsPosiblePrisma(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosiblePrisma(pelea, lanzador);
                }
                stop = true;
            }
        }

        private static void tipo_Recaudador(Fight.Luchador lanzador, Fight pelea) {
            int veces = 0;
            while (!stop && lanzador.puedeJugar()) {
                int ataque;
                if (++veces >= 8) {
                    stop = true;
                }
                if (veces > 15) {
                    return;
                }
                int porcPDV = lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff();
                Fight.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
                Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                if (porcPDV > 15) {
                    ataque = MotorIA.atacaSiEsPosibleRecau(pelea, lanzador);
                    while (ataque == 0 && !stop) {
                        if (ataque == 5) {
                            stop = true;
                        }
                        ataque = MotorIA.atacaSiEsPosibleRecau(pelea, lanzador);
                    }
                    if (MotorIA.curaSiEsPosibleRecau(pelea, lanzador, false) || MotorIA.buffeaSiEsPosibleRecau(pelea, lanzador, amigo)) continue;
                    enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
                    if (enemigo == null) {
                        MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
                        return;
                    }
                    if (MotorIA.acercarseA(pelea, lanzador, enemigo)) continue;
                    stop = true;
                    continue;
                }
                if (MotorIA.curaSiEsPosibleRecau(pelea, lanzador, true)) continue;
                ataque = MotorIA.atacaSiEsPosibleRecau(pelea, lanzador);
                while (ataque == 0 && !stop) {
                    if (ataque == 5) {
                        stop = true;
                    }
                    ataque = MotorIA.atacaSiEsPosibleRecau(pelea, lanzador);
                }
                if (MotorIA.mueveLoMasLejosPosible(pelea, lanzador)) continue;
                stop = true;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private static boolean mueveLoMasLejosPosible(Fight pelea, Fight.Luchador lanzador) {
            int[] movidas;
            if (lanzador.getTempPM(pelea) <= 0) {
                return false;
            }
            short celdaIDLanzador = lanzador.getCeldaPelea().getID();
            Maps mapa = pelea.getMapaCopia();
            short[] dist = new short[]{1000, 11000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
            short[] celda = new short[10];
            for (int i = 0; i < 10; ++i) {
                for (Fight.Luchador blanco : pelea.luchadoresDeEquipo(3)) {
                    short celdaEnemigo;
                    if (blanco.estaMuerto() || blanco == lanzador || blanco.getParamEquipoAliado() == lanzador.getParamEquipoAliado() || (celdaEnemigo = blanco.getCeldaPelea().getID()) == celda[0] || celdaEnemigo == celda[1] || celdaEnemigo == celda[2] || celdaEnemigo == celda[3] || celdaEnemigo == celda[4] || celdaEnemigo == celda[5] || celdaEnemigo == celda[6] || celdaEnemigo == celda[7] || celdaEnemigo == celda[8] || celdaEnemigo == celda[9]) continue;
                    short d = 0;
                    d = Pathfinding.distanciaEntreDosCeldas(mapa, celdaIDLanzador, celdaEnemigo);
                    if (d == 0) continue;
                    if (d < dist[i]) {
                        dist[i] = d;
                        celda[i] = celdaEnemigo;
                    }
                    if (dist[i] != 1000) continue;
                    dist[i] = 0;
                    celda[i] = celdaIDLanzador;
                }
            }
            if (dist[0] == 0) {
                return false;
            }
            int[] dist2 = new int[10];
            byte ancho = mapa.getAncho();
            int PM = lanzador.getTempPM(pelea);
            short celdaInicio = celdaIDLanzador;
            short celdaDestino = celdaIDLanzador;
            short ultCelda = mapa.ultimaCeldaID();
            Random rand = new Random();
            int valor = rand.nextInt(3);
            if (valor == 0) {
                int[] arrn = new int[4];
                arrn[1] = 1;
                arrn[2] = 2;
                arrn[3] = 3;
                movidas = arrn;
            } else if (valor == 1) {
                int[] arrn = new int[4];
                arrn[0] = 1;
                arrn[1] = 2;
                arrn[2] = 3;
                movidas = arrn;
            } else if (valor == 1) {
                int[] arrn = new int[4];
                arrn[0] = 2;
                arrn[1] = 3;
                arrn[3] = 1;
                movidas = arrn;
            } else {
                int[] arrn = new int[4];
                arrn[0] = 3;
                arrn[2] = 1;
                arrn[3] = 2;
                movidas = arrn;
            }
            for (int i = 0; i <= PM; ++i) {
                if (celdaDestino > 0) {
                    celdaInicio = celdaDestino;
                }
                short celdaTemporal = celdaInicio;
                int infl = 0;
                int inflF = 0;
                int[] arrn = movidas;
                int n = movidas.length;
                for (int j = 0; j < n; ++j) {
                    Integer x = arrn[j];
                    switch (x) {
                        case 0: {
                            celdaTemporal = (short)(celdaTemporal + ancho);
                            break;
                        }
                        case 1: {
                            celdaTemporal = (short)(celdaInicio + (ancho - 1));
                            break;
                        }
                        case 2: {
                            celdaTemporal = (short)(celdaInicio - ancho);
                            break;
                        }
                        case 3: {
                            celdaTemporal = (short)(celdaInicio - (ancho - 1));
                            break;
                        }
                    }
                    infl = 0;
                    for (int a = 0; a < 10 && dist[a] != 0; ++a) {
                        dist2[a] = Pathfinding.distanciaEntreDosCeldas(mapa, celdaTemporal, celda[a]);
                        if (dist2[a] <= dist[a]) continue;
                        ++infl;
                    }
                    if (infl <= inflF || celdaTemporal <= 0 || celdaTemporal >= ultCelda || mapa.celdaSalienteLateral(celdaDestino, celdaTemporal) || !mapa.getCelda(celdaTemporal).esCaminable(false)) continue;
                    inflF = infl;
                    celdaDestino = celdaTemporal;
                }
            }
            if (celdaDestino < 0) return false;
            if (celdaDestino > ultCelda) return false;
            if (celdaDestino == celdaIDLanzador) return false;
            if (!mapa.getCelda(celdaDestino).esCaminable(false)) {
                return false;
            }
            ArrayList<Maps.Celda> path = Pathfinding.pathMasCortoEntreDosCeldas(mapa, celdaIDLanzador, celdaDestino, 0);
            if (path == null) {
                return false;
            }
            ArrayList<Maps.Celda> finalPath = new ArrayList<Maps.Celda>();
            for (int a = 0; a < lanzador.getTempPM(pelea); ++a) {
                if (path.size() == a) break;
                finalPath.add(path.get(a));
            }
            String pathstr = "";
            try {
                short tempCeldaID = celdaIDLanzador;
                char tempDir = '\u0000';
                Iterator iterator = finalPath.iterator();
                do {
                    if (!iterator.hasNext()) {
                        if (tempCeldaID != celdaIDLanzador) {
                            pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
                        }
                        break;
                    }
                    Maps.Celda c = (Maps.Celda)iterator.next();
                    char d = Pathfinding.getDirEntreDosCeldas(tempCeldaID, c.getID(), mapa, true);
                    if (d == '\u0000') {
                        return false;
                    }
                    if (tempDir != d) {
                        if (finalPath.indexOf(c) != 0) {
                            pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
                        }
                        pathstr = String.valueOf(pathstr) + d;
                    }
                    tempCeldaID = c.getID();
                } while (true);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            GameThread.AccionDeJuego GA = new GameThread.AccionDeJuego(0, 1, "");
            GA._args = pathstr;
            return pelea.puedeMoverseLuchador(lanzador, GA);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private static boolean acercarseA(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            ArrayList<Maps.Celda> path;
            Maps mapa = pelea.getMapaCopia();
            if (lanzador.getTempPM(pelea) <= 0) {
                return false;
            }
            if (objetivo == null) {
                objetivo = MotorIA.enemigoMasCercano(pelea, lanzador);
            }
            if (objetivo == null) {
                return false;
            }
            short celdaID = -1;
            try {
                if (Pathfinding.esSiguienteA(lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID(), mapa)) {
                    return false;
                }
                celdaID = Pathfinding.getCeldaMasCercanaAlrededor(mapa, objetivo.getCeldaPelea().getID(), lanzador.getCeldaPelea().getID(), null);
            }
            catch (NullPointerException e) {
                return false;
            }
            if (celdaID == -1) {
                ArrayList<Fight.Luchador> enemigos = MotorIA.listaEnemigosMenosPDV(pelea, lanzador);
                for (Fight.Luchador enemigo : enemigos) {
                    short celdaID2 = Pathfinding.getCeldaMasCercanaAlrededor(mapa, enemigo.getCeldaPelea().getID(), lanzador.getCeldaPelea().getID(), null);
                    if (celdaID2 == -1) continue;
                    celdaID = celdaID2;
                    break;
                }
            }
            if ((path = Pathfinding.pathMasCortoEntreDosCeldas(mapa, lanzador.getCeldaPelea().getID(), celdaID, 0)) == null) return false;
            if (path.isEmpty()) {
                return false;
            }
            ArrayList<Maps.Celda> finalPath = new ArrayList<Maps.Celda>();
            for (int a = 0; a < lanzador.getTempPM(pelea); ++a) {
                if (path.size() == a) break;
                finalPath.add(path.get(a));
            }
            String pathstr = "";
            try {
                short tempCeldaID = lanzador.getCeldaPelea().getID();
                char tempDir = '\u0000';
                Iterator iterator = finalPath.iterator();
                do {
                    if (!iterator.hasNext()) {
                        if (tempCeldaID != lanzador.getCeldaPelea().getID()) {
                            pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
                        }
                        break;
                    }
                    Maps.Celda c = (Maps.Celda)iterator.next();
                    char d = Pathfinding.getDirEntreDosCeldas(tempCeldaID, c.getID(), mapa, true);
                    if (d == '\u0000') {
                        return false;
                    }
                    if (tempDir != d) {
                        if (finalPath.indexOf(c) != 0) {
                            pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
                        }
                        pathstr = String.valueOf(pathstr) + d;
                    }
                    tempCeldaID = c.getID();
                } while (true);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            GameThread.AccionDeJuego GA = new GameThread.AccionDeJuego(0, 1, "");
            GA._args = pathstr;
            return pelea.puedeMoverseLuchador(lanzador, GA);
        }

        private static boolean invocarSiEsPosible1(Fight pelea, Fight.Luchador invocador) {
            if (invocador.getNroInvocaciones() >= invocador.getTotalStatsConBuff().getEfecto(182)) {
                return false;
            }
            Fight.Luchador enemigoCercano = MotorIA.enemigoMasCercano(pelea, invocador);
            if (enemigoCercano == null) {
                return false;
            }
            short celdaMasCercana = Pathfinding.getCeldaMasCercanaAlrededor(pelea.getMapaCopia(), invocador.getCeldaPelea().getID(), enemigoCercano.getCeldaPelea().getID(), null);
            if (celdaMasCercana == -1) {
                return false;
            }
            Spell.StatsHechizos hechizo = MotorIA.hechizoInvocacion(pelea, invocador, celdaMasCercana);
            if (hechizo == null) {
                return false;
            }
            int invoc = pelea.intentarLanzarHechizo(invocador, hechizo, celdaMasCercana);
            return invoc == 0;
        }

        private static boolean invocarSiEsPosible2(Fight pelea, Fight.Luchador invocador) {
            if (invocador.getNroInvocaciones() >= invocador.getTotalStatsConBuff().getEfecto(182)) {
                return false;
            }
            Fight.Luchador enemigoCercano = MotorIA.enemigoMasCercano(pelea, invocador);
            if (enemigoCercano == null) {
                return false;
            }
            int invoc = MotorIA.hechizoInvocacion2(pelea, invocador, enemigoCercano);
            return invoc == 0;
        }

        private static Spell.StatsHechizos hechizoInvocacion(Fight pelea, Fight.Luchador invocador, short celdaCercana) {
            if (invocador.getMob() == null) {
                return null;
            }
            for (Map.Entry<Integer, Spell.StatsHechizos> SH : invocador.getMob().getHechizos().entrySet()) {
                if (!pelea.puedeLanzarHechizo(invocador, SH.getValue(), pelea.getMapaCopia().getCelda(celdaCercana), (short)-1)) continue;
                for (SpellEffect EH : SH.getValue().getEfectos()) {
                    if (EH.getEfectoID() != 181 && EH.getEfectoID() != 185) continue;
                    return SH.getValue();
                }
            }
            return null;
        }

        private static int hechizoInvocacion2(Fight pelea, Fight.Luchador invocador, Fight.Luchador enemigoCercano) {
            if (invocador.getMob() == null) {
                return 5;
            }
            ArrayList<Spell.StatsHechizos> hechizos = new ArrayList<Spell.StatsHechizos>();
            Spell.StatsHechizos SH = null;
            short celdaMasCercana = -1;
            try {
                for (Map.Entry<Integer, Spell.StatsHechizos> SS : invocador.getMob().getHechizos().entrySet()) {
                    Spell.StatsHechizos hechi = SS.getValue();
                    boolean paso = false;
                    for (SpellEffect EH : hechi.getEfectos()) {
                        if (paso || EH.getEfectoID() != 181 && EH.getEfectoID() != 185 || (celdaMasCercana = Pathfinding.getCeldaMasCercanaAlrededor2(pelea.getMapaCopia(), invocador.getCeldaPelea().getID(), enemigoCercano.getCeldaPelea().getID(), hechi.getMinAlc(), hechi.getMaxAlc())) == -1 || !pelea.puedeLanzarHechizo(invocador, hechi, pelea.getMapaCopia().getCelda(celdaMasCercana), (short)-1)) continue;
                        hechizos.add(hechi);
                        paso = true;
                    }
                }
            }
            catch (NullPointerException e) {
                return 5;
            }
            if (hechizos.size() <= 0) {
                return 5;
            }
            SH = hechizos.size() == 1 ? (Spell.StatsHechizos)hechizos.get(0) : (Spell.StatsHechizos)hechizos.get(Fórmulas.getRandomValor(0, hechizos.size() - 1));
            int invoca = pelea.intentarLanzarHechizo(invocador, SH, celdaMasCercana);
            return invoca;
        }

        private static boolean curaSiEsPosible(Fight pelea, Fight.Luchador lanzador, boolean autoCura) {
            if (autoCura && lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff() > 95) {
                return false;
            }
            Fight.Luchador objetivo = null;
            Spell.StatsHechizos SH = null;
            if (autoCura) {
                objetivo = lanzador;
                SH = MotorIA.mejorHechizoCuracion(pelea, lanzador, objetivo);
            } else {
                Fight.Luchador tempObjetivo = null;
                int porcPDVmin = 100;
                Spell.StatsHechizos tempSH = null;
                for (Fight.Luchador blanco : pelea.luchadoresDeEquipo(3)) {
                    if (blanco.estaMuerto() || blanco == lanzador || blanco.getParamEquipoAliado() != lanzador.getParamEquipoAliado()) continue;
                    int porcPDV = 0;
                    int PDVMAX = blanco.getPDVMaxConBuff();
                    porcPDV = PDVMAX == 0 ? 0 : blanco.getPDVConBuff() * 100 / PDVMAX;
                    if (porcPDV >= porcPDVmin || porcPDV >= 95) continue;
                    int infl = 0;
                    for (Map.Entry<Integer, Spell.StatsHechizos> sh : lanzador.getMob().getHechizos().entrySet()) {
                        int infCura = MotorIA.calculaInfluenciaCura(sh.getValue());
                        if (infl >= infCura || infCura == 0 || !pelea.puedeLanzarHechizo(lanzador, sh.getValue(), blanco.getCeldaPelea(), (short)-1)) continue;
                        infl = infCura;
                        tempSH = sh.getValue();
                    }
                    if (tempSH == SH || tempSH == null) continue;
                    tempObjetivo = blanco;
                    SH = tempSH;
                    porcPDVmin = porcPDV;
                }
                objetivo = tempObjetivo;
            }
            if (objetivo == null) {
                return false;
            }
            if (SH == null) {
                return false;
            }
            int cura = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
            return cura == 0;
        }

        private static boolean curaSiEsPosiblePrisma(Fight pelea, Fight.Luchador prisma, boolean autoCura) {
            if (autoCura && prisma.getPDVConBuff() * 100 / prisma.getPDVMaxConBuff() > 95) {
                return false;
            }
            Fight.Luchador objetivo = null;
            Spell hechizo = World.getHechizo(124);
            Spell.StatsHechizos SH = hechizo.getStatsPorNivel(6);
            if (autoCura) {
                objetivo = prisma;
            } else {
                Fight.Luchador curado = null;
                int porcPDVmin = 100;
                for (Fight.Luchador blanco : pelea.luchadoresDeEquipo(3)) {
                    int porcPDV;
                    if (blanco.estaMuerto() || blanco == prisma || blanco.getParamEquipoAliado() != prisma.getParamEquipoAliado() || (porcPDV = blanco.getPDVConBuff() * 100 / blanco.getPDVMaxConBuff()) >= porcPDVmin || porcPDV >= 95) continue;
                    curado = blanco;
                    porcPDVmin = porcPDV;
                }
                objetivo = curado;
            }
            if (objetivo == null) {
                return false;
            }
            if (SH == null) {
                return false;
            }
            int cura = pelea.intentarLanzarHechizo(prisma, SH, objetivo.getCeldaPelea().getID());
            return cura == 0;
        }

        private static boolean curaSiEsPosibleRecau(Fight pelea, Fight.Luchador recaudador, boolean autoCura) {
            if (autoCura && recaudador.getPDVConBuff() * 100 / recaudador.getPDVMaxConBuff() > 95) {
                return false;
            }
            Fight.Luchador objetivo = null;
            Spell.StatsHechizos SH = null;
            if (autoCura) {
                objetivo = recaudador;
                SH = MotorIA.mejorHechizoCuracionRecaudador(pelea, recaudador, objetivo);
            } else {
                Fight.Luchador tempObjetivo = null;
                int porcPDVmin = 100;
                Spell.StatsHechizos tempSH = null;
                if (pelea.luchadoresDeEquipo(recaudador.getParamEquipoAliado()).size() <= 1) {
                    return false;
                }
                for (Fight.Luchador blanco : pelea.luchadoresDeEquipo(3)) {
                    int porcPDV;
                    if (blanco.estaMuerto() || blanco == recaudador || blanco.getParamEquipoAliado() != recaudador.getParamEquipoAliado() || (porcPDV = blanco.getPDVConBuff() * 100 / blanco.getPDVMaxConBuff()) >= porcPDVmin || porcPDV >= 95) continue;
                    int infl = 0;
                    for (Map.Entry<Integer, Spell.StatsHechizos> sh : World.getGremio(recaudador.getRecau().getGremioID()).getHechizos().entrySet()) {
                        int infCura;
                        if (sh.getValue() == null || infl >= (infCura = MotorIA.calculaInfluenciaCura(sh.getValue())) || infCura == 0 || !pelea.puedeLanzarHechizo(recaudador, sh.getValue(), blanco.getCeldaPelea(), (short)-1)) continue;
                        infl = infCura;
                        tempSH = sh.getValue();
                    }
                    if (tempSH == SH || tempSH == null) continue;
                    tempObjetivo = blanco;
                    SH = tempSH;
                    porcPDVmin = porcPDV;
                }
                objetivo = tempObjetivo;
            }
            if (objetivo == null) {
                return false;
            }
            if (SH == null) {
                return false;
            }
            int cura = pelea.intentarLanzarHechizo(recaudador, SH, objetivo.getCeldaPelea().getID());
            return cura == 0;
        }

        private static boolean buffeaSiEsPosible1(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            Spell.StatsHechizos SH;
            block4: {
                if (objetivo == null) {
                    return false;
                }
                try {
                    SH = MotorIA.mejorBuff1(pelea, lanzador, objetivo);
                    if (SH != null) break block4;
                    return false;
                }
                catch (NullPointerException e) {
                    return false;
                }
            }
            int buff = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
            return buff == 0;
        }

        private static boolean buffeaSiEsPosible2(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            Spell.StatsHechizos SH;
            block4: {
                if (objetivo == null) {
                    return false;
                }
                try {
                    SH = MotorIA.mejorBuff2(pelea, lanzador, objetivo);
                    if (SH != null) break block4;
                    return false;
                }
                catch (NullPointerException e) {
                    return false;
                }
            }
            int buff = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
            return buff == 0;
        }

        private static boolean buffeaKralamar(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            if (objetivo == null) {
                return false;
            }
            Spell hechizo = World.getHechizo(1106);
            Spell.StatsHechizos SH = hechizo.getStatsPorNivel(1);
            if (SH == null) {
                return false;
            }
            int buff = 5;
            try {
                buff = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
            }
            catch (NullPointerException e) {
                return false;
            }
            return buff == 0;
        }

        private static boolean buffeaSiEsPosiblePrisma(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            if (objetivo == null) {
                return false;
            }
            Spell.StatsHechizos SH = MotorIA.mejorBuffPrisma(pelea, lanzador);
            if (SH == null) {
                return false;
            }
            int buff = 5;
            try {
                buff = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
            }
            catch (NullPointerException e) {
                return false;
            }
            return buff == 0;
        }

        private static boolean buffeaSiEsPosibleRecau(Fight pelea, Fight.Luchador recaudador, Fight.Luchador objetivo) {
            Spell.StatsHechizos SH;
            block4: {
                if (objetivo == null) {
                    return false;
                }
                try {
                    SH = MotorIA.mejorBuffRecaudador(pelea, recaudador, objetivo);
                    if (SH != null) break block4;
                    return false;
                }
                catch (NullPointerException e) {
                    return false;
                }
            }
            int buff = pelea.intentarLanzarHechizo(recaudador, SH, objetivo.getCeldaPelea().getID());
            return buff == 0;
        }

        private static Spell.StatsHechizos mejorBuffPrisma(Fight pelea, Fight.Luchador lanzador) {
            Spell hechizo = World.getHechizo(153);
            Spell.StatsHechizos hechizoStats = hechizo.getStatsPorNivel(6);
            return hechizoStats;
        }

        private static Spell.StatsHechizos mejorBuff1(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            int infl = 0;
            Spell.StatsHechizos sh = null;
            if (objetivo == null) {
                return null;
            }
            try {
                for (Map.Entry<Integer, Spell.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
                    int infDa\u00f1o = MotorIA.calculaInfluenciaDa\u00f1o(SH.getValue(), lanzador, objetivo);
                    if (infl >= infDa\u00f1o || infDa\u00f1o <= 0 || !pelea.puedeLanzarHechizo(lanzador, SH.getValue(), objetivo.getCeldaPelea(), (short)-1)) continue;
                    infl = infDa\u00f1o;
                    sh = SH.getValue();
                }
            }
            catch (NullPointerException e) {
                return null;
            }
            return sh;
        }

        private static Spell.StatsHechizos mejorBuff2(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            Spell.StatsHechizos sh;
            ArrayList<Spell.StatsHechizos> hechizos;
            block6: {
                hechizos = new ArrayList<Spell.StatsHechizos>();
                sh = null;
                if (objetivo == null) {
                    return null;
                }
                try {
                    Maps.Celda celdaObj = objetivo.getCeldaPelea();
                    for (Map.Entry<Integer, Spell.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
                        if (!pelea.puedeLanzarHechizo(lanzador, SH.getValue(), celdaObj, (short)-1)) continue;
                        hechizos.add(SH.getValue());
                    }
                    if (hechizos.size() > 0) break block6;
                    return null;
                }
                catch (NullPointerException e) {
                    return null;
                }
            }
            if (hechizos.size() == 1) {
                return (Spell.StatsHechizos)hechizos.get(0);
            }
            sh = (Spell.StatsHechizos)hechizos.get(Fórmulas.getRandomValor(0, hechizos.size() - 1));
            return sh;
        }

        private static Spell.StatsHechizos mejorBuffRecaudador(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            int infl = 0;
            Spell.StatsHechizos sh = null;
            if (objetivo == null) {
                return null;
            }
            try {
                for (Map.Entry<Integer, Spell.StatsHechizos> SH : World.getGremio(lanzador.getRecau().getGremioID()).getHechizos().entrySet()) {
                    int infDa\u00f1os;
                    if (SH.getValue() == null || infl >= (infDa\u00f1os = MotorIA.calculaInfluenciaDa\u00f1o(SH.getValue(), lanzador, objetivo)) || infDa\u00f1os <= 0 || !pelea.puedeLanzarHechizo(lanzador, SH.getValue(), objetivo.getCeldaPelea(), (short)-1)) continue;
                    infl = infDa\u00f1os;
                    sh = SH.getValue();
                }
            }
            catch (NullPointerException e) {
                return null;
            }
            return sh;
        }

        private static Spell.StatsHechizos mejorHechizoCuracion(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            int infl = 0;
            Spell.StatsHechizos sh = null;
            if (objetivo == null) {
                return null;
            }
            try {
                for (Map.Entry<Integer, Spell.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
                    int infCura = MotorIA.calculaInfluenciaCura(SH.getValue());
                    if (infl >= infCura || infCura == 0 || !pelea.puedeLanzarHechizo(lanzador, SH.getValue(), objetivo.getCeldaPelea(), (short)-1)) continue;
                    infl = infCura;
                    sh = SH.getValue();
                }
            }
            catch (NullPointerException e) {
                return null;
            }
            return sh;
        }

        private static Spell.StatsHechizos mejorHechizoCuracionRecaudador(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            int infl = 0;
            Spell.StatsHechizos sh = null;
            if (objetivo == null) {
                return null;
            }
            try {
                for (Map.Entry<Integer, Spell.StatsHechizos> SH : World.getGremio(lanzador.getRecau().getGremioID()).getHechizos().entrySet()) {
                    int infCura;
                    if (SH.getValue() == null || infl >= (infCura = MotorIA.calculaInfluenciaCura(SH.getValue())) || infCura == 0 || !pelea.puedeLanzarHechizo(lanzador, SH.getValue(), objetivo.getCeldaPelea(), (short)-1)) continue;
                    infl = infCura;
                    sh = SH.getValue();
                }
            }
            catch (NullPointerException e) {
                return null;
            }
            return sh;
        }

        private static Fight.Luchador amigoMasCercano(Fight pelea, Fight.Luchador lanzador) {
            short dist = 1000;
            Fight.Luchador tempObjetivo = null;
            for (Fight.Luchador objetivo : pelea.luchadoresDeEquipo(lanzador.getParamEquipoAliado())) {
                short d;
                if (objetivo.estaMuerto() || objetivo == lanzador || (d = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID())) >= dist) continue;
                dist = d;
                tempObjetivo = objetivo;
            }
            return tempObjetivo;
        }

        private static Fight.Luchador enemigoMasCercano(Fight pelea, Fight.Luchador lanzador) {
            short dist = 1000;
            Fight.Luchador tempObjetivo = null;
            for (Fight.Luchador objetivo : pelea.luchadoresDeEquipo(lanzador.getParamEquipoEnemigo())) {
                short d;
                if (objetivo.estaMuerto() || objetivo.esInvisible() || (d = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID())) >= dist) continue;
                dist = d;
                tempObjetivo = objetivo;
            }
            return tempObjetivo;
        }

        private static Fight.Luchador luchadorMasCercano(Fight pelea, Fight.Luchador lanzador) {
            short dist = 1000;
            Fight.Luchador tempObjetivo = null;
            for (Fight.Luchador objetivo : pelea.luchadoresDeEquipo(3)) {
                short d;
                if (objetivo.estaMuerto() || objetivo == lanzador || (d = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID())) >= dist) continue;
                dist = d;
                tempObjetivo = objetivo;
            }
            return tempObjetivo;
        }

        private static ArrayList<Fight.Luchador> listaTodoEnemigos(Fight pelea, Fight.Luchador lanzador) {
            ArrayList<Fight.Luchador> listaEnemigos = new ArrayList<Fight.Luchador>();
            ArrayList<Fight.Luchador> enemigosNoInvo = new ArrayList<Fight.Luchador>();
            ArrayList<Fight.Luchador> enemigosInvo = new ArrayList<Fight.Luchador>();
            for (Fight.Luchador objetivo : pelea.luchadoresDeEquipo(lanzador.getParamEquipoEnemigo())) {
                if (objetivo.estaMuerto() || objetivo.esInvisible()) continue;
                if (objetivo.esInvocacion()) {
                    enemigosInvo.add(objetivo);
                    continue;
                }
                enemigosNoInvo.add(objetivo);
            }
            Random rand = new Random();
            if (rand.nextBoolean()) {
                listaEnemigos.addAll(enemigosInvo);
                listaEnemigos.addAll(enemigosNoInvo);
            } else {
                listaEnemigos.addAll(enemigosNoInvo);
                listaEnemigos.addAll(enemigosInvo);
            }
            return listaEnemigos;
        }

        private static ArrayList<Luchador> listaEnemigosMenosPDV(Fight pelea, Luchador lanzador) {
			ArrayList<Luchador> listaEnemigos = new ArrayList<Luchador>();
			ArrayList<Luchador> enemigosNoInvo = new ArrayList<Luchador>();
			ArrayList<Luchador> enemigosInvo = new ArrayList<Luchador>();
			for (Luchador objetivo : pelea.luchadoresDeEquipo(lanzador.getParamEquipoEnemigo())) {
				if (objetivo.estaMuerto() || objetivo.esInvisible())
					continue;
				if (objetivo.esInvocacion())
					enemigosInvo.add(objetivo);
				else
					enemigosNoInvo.add(objetivo);
			}
			int i = 0;
			int tempPDV;
			Random rand = new Random();
			if (rand.nextBoolean()) {
				try {
					int i3 = enemigosNoInvo.size(), i2 = enemigosInvo.size();
					while (i < i2) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosInvo.remove(index);
						i++;
					}
					i = 0;
					while (i < i3) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosNoInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosNoInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosNoInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosNoInvo.remove(index);
						i++;
					}
				} catch (NullPointerException e) {
					return listaEnemigos;
				}
			} else
				try {
					int i2 = enemigosNoInvo.size(), i3 = enemigosInvo.size();
					while (i < i2) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosNoInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosNoInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosNoInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosNoInvo.remove(index);
						i++;
					}
					i = 0;
					while (i < i3) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosInvo.remove(index);
						i++;
					}
				} catch (NullPointerException e) {
					return listaEnemigos;
				}
			return listaEnemigos;
		}

        private static ArrayList<Luchador> listaTodoLuchadores(Fight pelea, Luchador lanzador) {
			Luchador enemigoMasCercano = luchadorMasCercano(pelea, lanzador);
			ArrayList<Luchador> listaEnemigos = new ArrayList<Luchador>();
			ArrayList<Luchador> enemigosNoInvo = new ArrayList<Luchador>();
			ArrayList<Luchador> enemigosInvo = new ArrayList<Luchador>();
			for (Luchador objetivo : pelea.luchadoresDeEquipo(3)) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.esInvocacion())
					enemigosInvo.add(objetivo);
				else
					enemigosNoInvo.add(objetivo);
			}
			if (enemigoMasCercano != null)
				listaEnemigos.add(enemigoMasCercano);
			int i = 0;
			int tempPDV;
			Random rand = new Random();
			if (rand.nextBoolean()) {
				try {
					int i3 = enemigosNoInvo.size(), i2 = enemigosInvo.size();
					while (i < i2) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosInvo.remove(index);
						i++;
					}
					i = 0;
					while (i < i3) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosNoInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosNoInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosNoInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosNoInvo.remove(index);
						i++;
					}
				} catch (NullPointerException e) {
					return listaEnemigos;
				}
			} else
				try {
					int i2 = enemigosNoInvo.size(), i3 = enemigosInvo.size();
					while (i < i2) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosNoInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosNoInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosNoInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosNoInvo.remove(index);
						i++;
					}
					i = 0;
					while (i < i3) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosInvo.remove(index);
						i++;
					}
				} catch (NullPointerException e) {}
			return listaEnemigos;
		}

        private static int atacaSiEsPosibleRecau(Fight pelea, Fight.Luchador recaudador) {
            ArrayList<Fight.Luchador> listaEnemigos = MotorIA.objetivosMasCercanos(pelea, recaudador);
            Spell.StatsHechizos SH = null;
            Fight.Luchador objetivo = null;
            for (Fight.Luchador blanco : listaEnemigos) {
                SH = MotorIA.mejorHechizoRecau(pelea, recaudador, blanco);
                if (SH == null) continue;
                objetivo = blanco;
                break;
            }
            if (objetivo == null || SH == null) {
                return 666;
            }
            int attack = pelea.intentarLanzarHechizo(recaudador, SH, objetivo.getCeldaPelea().getID());
            if (attack != 0) {
                return attack;
            }
            return 0;
        }

        private static int atacaSiEsPosiblePrisma(Fight pelea, Fight.Luchador lanzador) {
            ArrayList<Fight.Luchador> listaEnemigos = MotorIA.listaEnemigosMenosPDV(pelea, lanzador);
            Spell.StatsHechizos SH = null;
            Fight.Luchador objetivo = null;
            for (Fight.Luchador blanco : listaEnemigos) {
                SH = MotorIA.mejorHechizoPrisma(pelea, lanzador, blanco);
                if (SH == null) continue;
                objetivo = blanco;
                break;
            }
            if (objetivo == null || SH == null) {
                return 666;
            }
            int ataque = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
            if (ataque != 0) {
                return ataque;
            }
            return 0;
        }

        private static int atacaSiEsPosible1(Fight pelea, Fight.Luchador lanzador) {
            ArrayList<Fight.Luchador> listaEnemigos = MotorIA.objetivosMasCercanos(pelea, lanzador);
            Spell.StatsHechizos SH = null;
            Fight.Luchador objetivo = null;
            for (Fight.Luchador blanco : listaEnemigos) {
                SH = MotorIA.mejorHechizo1(pelea, lanzador, blanco);
                if (SH == null) continue;
                objetivo = blanco;
                break;
            }
            if (objetivo == null || SH == null) {
                return 666;
            }
            int ataque = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
            if (ataque != 0) {
                return ataque;
            }
            return 0;
        }

        private static int atacaSiEsPosible2(Fight pelea, Fight.Luchador lanzador) {
            ArrayList<Fight.Luchador> listaEnemigos = MotorIA.objetivosMasCercanos(pelea, lanzador);
            Spell.StatsHechizos SH = null;
            Fight.Luchador objetivo = null;
            for (Fight.Luchador blanco : listaEnemigos) {
                SH = MotorIA.mejorHechizo2(pelea, lanzador, blanco);
                if (SH == null) continue;
                objetivo = blanco;
                break;
            }
            if (objetivo == null || SH == null) {
                return 666;
            }
            int ataque = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
            if (ataque != 0) {
                return ataque;
            }
            return 0;
        }

        private static int atacaSiEsPosible3(Fight pelea, Fight.Luchador lanzador) {
            ArrayList<Fight.Luchador> listaEnemigos = MotorIA.listaTodoLuchadores(pelea, lanzador);
            Spell.StatsHechizos SH = null;
            Fight.Luchador objetivo = null;
            for (Fight.Luchador blanco : listaEnemigos) {
                SH = MotorIA.mejorHechizo2(pelea, lanzador, blanco);
                if (SH == null) continue;
                objetivo = blanco;
                break;
            }
            if (objetivo == null || SH == null) {
                return 666;
            }
            int ataque = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
            if (ataque != 0) {
                return ataque;
            }
            return 0;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private static boolean moverYAtacarSiEsPosible(Fight pelea, Fight.Luchador lanzador) {
            ArrayList<Short> celdas = Pathfinding.listaCeldasDesdeLuchador(pelea, lanzador);
            if (celdas == null) {
                return false;
            }
            Fight.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
            if (enemigo == null) {
                return false;
            }
            short distMin = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(), enemigo.getCeldaPelea().getID());
            ArrayList<Spell.StatsHechizos> hechizos = MotorIA.hechizosLanzables(lanzador, pelea, distMin);
            if (hechizos == null || hechizos.isEmpty()) {
                return false;
            }
            Spell.StatsHechizos hechizo = hechizos.size() == 1 ? hechizos.get(0) : hechizos.get(Fórmulas.getRandomValor(0, hechizos.size() - 1));
            ArrayList<Fight.Luchador> objetivos = MotorIA.objetivosMasCercanosAlHechizo(pelea, lanzador, hechizo);
            if (objetivos == null) {
                return false;
            }
            short celdaDestino = 0;
            Fight.Luchador objetivo = null;
            boolean encontrado = false;
            for (short celda : celdas) {
                for (Fight.Luchador O : objetivos) {
                    if (pelea.puedeLanzarHechizo(lanzador, hechizo, O.getCeldaPelea(), celda)) {
                        celdaDestino = celda;
                        objetivo = O;
                        encontrado = true;
                    }
                    if (encontrado) break;
                }
                if (encontrado) break;
            }
            if (celdaDestino == 0) {
                return false;
            }
            ArrayList<Maps.Celda> path = Pathfinding.pathMasCortoEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(), celdaDestino, 0);
            if (path == null) {
                return false;
            }
            String pathStr = "";
            try {
                short tempCeldaID = lanzador.getCeldaPelea().getID();
                char tempDir = '\u0000';
                Iterator<Maps.Celda> iterator = path.iterator();
                do {
                    if (!iterator.hasNext()) {
                        if (tempCeldaID != lanzador.getCeldaPelea().getID()) {
                            pathStr = String.valueOf(pathStr) + CryptManager.celdaIDACodigo(tempCeldaID);
                        }
                        break;
                    }
                    Maps.Celda c = iterator.next();
                    char dir = Pathfinding.getDirEntreDosCeldas(tempCeldaID, c.getID(), pelea.getMapaCopia(), true);
                    if (dir == '\u0000') {
                        return false;
                    }
                    if (tempDir != dir) {
                        if (path.indexOf(c) != 0) {
                            pathStr = String.valueOf(pathStr) + CryptManager.celdaIDACodigo(tempCeldaID);
                        }
                        pathStr = String.valueOf(pathStr) + dir;
                    }
                    tempCeldaID = c.getID();
                } while (true);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            GameThread.AccionDeJuego GA = new GameThread.AccionDeJuego(0, 1, "");
            GA._args = pathStr;
            boolean resultado = pelea.puedeMoverseLuchador(lanzador, GA);
            if (resultado && objetivo != null && hechizo != null) {
                pelea.intentarLanzarHechizo(lanzador, hechizo, objetivo.getCeldaPelea().getID());
            }
            return resultado;
        }

        private static ArrayList<Spell.StatsHechizos> hechizosLanzables(Fight.Luchador lanzador, Fight pelea, int distMin) {
            ArrayList<Spell.StatsHechizos> hechizos = new ArrayList<Spell.StatsHechizos>();
            if (lanzador.getMob() == null) {
                return null;
            }
            for (Map.Entry<Integer, Spell.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
                Spell.StatsHechizos hechizo = SH.getValue();
                if (hechizo.getCostePA() > lanzador.getTempPA(pelea) || !Fight.HechizoLanzado.poderSigLanzamiento(lanzador, hechizo.getHechizoID()) || hechizo.getMaxLanzPorTurno() - Fight.HechizoLanzado.getNroLanzamientos(lanzador, hechizo.getHechizoID()) <= 0 && hechizo.getMaxLanzPorTurno() > 0 || MotorIA.calculaInfluenciaDa\u00f1o(hechizo, lanzador, lanzador) >= 0) continue;
                hechizos.add(hechizo);
            }
            ArrayList<Spell.StatsHechizos> hechizosFinales = MotorIA.hechizosMasAMenosDa\u00f1os(lanzador, hechizos);
            return hechizosFinales;
        }

        private static ArrayList<Spell.StatsHechizos> hechizosMasAMenosDa\u00f1os(Fight.Luchador lanzador, ArrayList<Spell.StatsHechizos> hechizos) {
            if (hechizos == null) {
                return null;
            }
            ArrayList<Spell.StatsHechizos> hechizosFinales = new ArrayList<Spell.StatsHechizos>();
            TreeMap<Integer, Spell.StatsHechizos> copia = new TreeMap<Integer, Spell.StatsHechizos>();
            for (Spell.StatsHechizos SH : hechizos) {
                copia.put(SH.getHechizoID(), SH);
            }
            int tempInfluencia = 0;
            int tempID = 0;
            while (copia.size() > 0) {
                tempInfluencia = 0;
                tempID = 0;
                for (Entry SH : copia.entrySet()) {
                    int influencia = -MotorIA.calculaInfluenciaDa\u00f1o((Spell.StatsHechizos)SH.getValue(), lanzador, lanzador);
                    if (influencia <= tempInfluencia) continue;
                    tempID = ((Spell.StatsHechizos)SH.getValue()).getHechizoID();
                    tempInfluencia = influencia;
                }
                if (tempID == 0 || tempInfluencia == 0) break;
                hechizosFinales.add((Spell.StatsHechizos)copia.get(tempID));
                copia.remove(tempID);
            }
            return hechizosFinales;
        }

        private static ArrayList<Fight.Luchador> objetivosMasCercanosAlHechizo(Fight pelea, Fight.Luchador lanzador, Spell.StatsHechizos hechizo) {
            ArrayList<Fight.Luchador> objetivos = new ArrayList<Fight.Luchador>();
            ArrayList<Fight.Luchador> objetivos1 = new ArrayList<Fight.Luchador>();
            int distMax = hechizo.getMaxAlc();
            distMax += lanzador.getTempPM(pelea);
            ArrayList<Fight.Luchador> objetivosP = MotorIA.listaTodoEnemigos(pelea, lanzador);
            Iterator<Fight.Luchador> iterator = objetivosP.iterator();
            while (iterator.hasNext()) {
                Fight.Luchador entry;
                Fight.Luchador objetivo = entry = iterator.next();
                short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID());
                if (dist >= distMax) continue;
                objetivos.add(objetivo);
            }
            while (objetivos.size() > 0) {
                int index = 0;
                short dista = 1000;
                for (Fight.Luchador objetivo : objetivos) {
                    short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID());
                    if (dist >= dista) continue;
                    dista = dist;
                    index = objetivos.indexOf(objetivo);
                }
                objetivos1.add((Fight.Luchador)objetivos.get(index));
                objetivos.remove(index);
            }
            return objetivos1;
        }

        private static ArrayList<Luchador> objetivosMasCercanos(Fight pelea, Luchador lanzador) {
            ArrayList<Luchador> objetivos = new ArrayList<Luchador>();
            ArrayList<Luchador> objetivos1 = listaTodoEnemigos(pelea, lanzador);
            while (objetivos.size() > 0) {
              int index = 0;
              int dista = 1000;
              for (Luchador objetivo : objetivos) {
                int dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(), objetivo
                    .getCeldaPelea().getID());
                if (dist < dista) {
                  dista = dist;
                  index = objetivos.indexOf(objetivo);
                } 
              } 
              objetivos1.add(objetivos.get(index));
              objetivos.remove(index);
            } 
            return objetivos1;
          }

        private static Spell.StatsHechizos mejorHechizoRecau(Fight pelea, Fight.Luchador recaudador, Fight.Luchador objetivo) {
            int influenciaMax = 0;
            Spell.StatsHechizos sh = null;
            Map<Integer, Spell.StatsHechizos> hechiRecau = World.getGremio(recaudador.getRecau().getGremioID()).getHechizos();
            if (objetivo == null) {
                return null;
            }
            for (Map.Entry<Integer, Spell.StatsHechizos> SH1 : hechiRecau.entrySet()) {
                Spell.StatsHechizos hechizo1 = SH1.getValue();
                if (hechizo1 == null) continue;
                int tempInfluencia = 0;
                int influencia1 = 0;
                int influencia2 = 0;
                int PA = 6;
                int[] costePA = new int[2];
                if (!pelea.puedeLanzarHechizo(recaudador, hechizo1, objetivo.getCeldaPelea(), (short)-1) || (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo1, recaudador, objetivo)) == 0) continue;
                if (tempInfluencia > influenciaMax) {
                    sh = hechizo1;
                    costePA[0] = sh.getCostePA();
                    influenciaMax = influencia1 = tempInfluencia;
                }
                for (Map.Entry<Integer, Spell.StatsHechizos> SH2 : hechiRecau.entrySet()) {
                    Spell.StatsHechizos hechizo2 = SH2.getValue();
                    if (hechizo2 == null || PA - costePA[0] < hechizo2.getCostePA() || !pelea.puedeLanzarHechizo(recaudador, hechizo2, objetivo.getCeldaPelea(), (short)-1) || (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo2, recaudador, objetivo)) == 0) continue;
                    if (influencia1 + tempInfluencia > influenciaMax) {
                        sh = hechizo2;
                        costePA[1] = hechizo2.getCostePA();
                        influencia2 = tempInfluencia;
                        influenciaMax = influencia1 + influencia2;
                    }
                    for (Map.Entry<Integer, Spell.StatsHechizos> SH3 : hechiRecau.entrySet()) {
                        Spell.StatsHechizos hechizo3 = SH3.getValue();
                        if (hechizo3 == null || PA - costePA[0] - costePA[1] < hechizo3.getCostePA() || !pelea.puedeLanzarHechizo(recaudador, hechizo3, objetivo.getCeldaPelea(), (short)-1) || (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo3, recaudador, objetivo)) == 0 || tempInfluencia + influencia1 + influencia2 <= influenciaMax) continue;
                        sh = hechizo3;
                        influenciaMax = tempInfluencia + influencia1 + influencia2;
                    }
                }
            }
            return sh;
        }

        private static Spell.StatsHechizos mejorHechizoPrisma(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            Spell.StatsHechizos sh = null;
            ArrayList<Spell.StatsHechizos> posibles = new ArrayList<Spell.StatsHechizos>();
            if (objetivo == null) {
                return null;
            }
            try {
                for (Map.Entry<Integer, Spell.StatsHechizos> SH : lanzador.getPrisma().getHechizos().entrySet()) {
                    Spell.StatsHechizos statsH = SH.getValue();
                    if (!pelea.puedeLanzarHechizo(lanzador, statsH, objetivo.getCeldaPelea(), (short)-1)) continue;
                    posibles.add(statsH);
                }
            }
            catch (NullPointerException e) {
                return null;
            }
            if (posibles.isEmpty()) {
                return sh;
            }
            if (posibles.size() == 1) {
                return (Spell.StatsHechizos)posibles.get(0);
            }
            sh = (Spell.StatsHechizos)posibles.get(Fórmulas.getRandomValor(0, posibles.size() - 1));
            return sh;
        }

        private static Spell.StatsHechizos mejorHechizo1(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            int influenciaMax = 0;
            Spell.StatsHechizos sh = null;
            Map<Integer, Spell.StatsHechizos> hechiMob = lanzador.getMob().getHechizos();
            if (objetivo == null) {
                return null;
            }
            for (Map.Entry<Integer, Spell.StatsHechizos> SH : hechiMob.entrySet()) {
                int tempInfluencia = 0;
                int influencia1 = 0;
                int influencia2 = 0;
                int PA = lanzador.getTempPA(pelea);
                int[] costePA = new int[2];
                Spell.StatsHechizos hechizo1 = SH.getValue();
                if (!pelea.puedeLanzarHechizo(lanzador, hechizo1, objetivo.getCeldaPelea(), (short)-1) || (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo1, lanzador, objetivo)) == 0) continue;
                if (tempInfluencia > influenciaMax) {
                    sh = hechizo1;
                    costePA[0] = sh.getCostePA();
                    influenciaMax = influencia1 = tempInfluencia;
                }
                for (Map.Entry<Integer, Spell.StatsHechizos> SH2 : hechiMob.entrySet()) {
                    Spell.StatsHechizos hechizo2 = SH2.getValue();
                    if (PA - costePA[0] < hechizo2.getCostePA() || !pelea.puedeLanzarHechizo(lanzador, hechizo2, objetivo.getCeldaPelea(), (short)-1) || (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo2, lanzador, objetivo)) == 0) continue;
                    if (influencia1 + tempInfluencia > influenciaMax) {
                        sh = hechizo2;
                        costePA[1] = hechizo2.getCostePA();
                        influencia2 = tempInfluencia;
                        influenciaMax = influencia1 + influencia2;
                    }
                    for (Map.Entry<Integer, Spell.StatsHechizos> SH3 : hechiMob.entrySet()) {
                        Spell.StatsHechizos hechizo3 = SH3.getValue();
                        if (PA - costePA[0] - costePA[1] < hechizo3.getCostePA() || !pelea.puedeLanzarHechizo(lanzador, hechizo3, objetivo.getCeldaPelea(), (short)-1) || (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo3, lanzador, objetivo)) == 0 || tempInfluencia + influencia1 + influencia2 <= influenciaMax) continue;
                        sh = hechizo3;
                        influenciaMax = tempInfluencia + influencia1 + influencia2;
                    }
                }
            }
            return sh;
        }

        private static Spell.StatsHechizos mejorHechizo2(Fight pelea, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            Spell.StatsHechizos sh = null;
            ArrayList<Spell.StatsHechizos> posibles = new ArrayList<Spell.StatsHechizos>();
            if (objetivo == null) {
                return null;
            }
            try {
                for (Map.Entry<Integer, Spell.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
                    Spell.StatsHechizos hechizo = SH.getValue();
                    if (!pelea.puedeLanzarHechizo(lanzador, hechizo, objetivo.getCeldaPelea(), (short)-1)) continue;
                    posibles.add(hechizo);
                }
            }
            catch (NullPointerException e) {
                return null;
            }
            if (posibles.isEmpty()) {
                return sh;
            }
            if (posibles.size() == 1) {
                return (Spell.StatsHechizos)posibles.get(0);
            }
            sh = (Spell.StatsHechizos)posibles.get(Fórmulas.getRandomValor(0, posibles.size() - 1));
            return sh;
        }

        private static int calculaInfluenciaCura(Spell.StatsHechizos SH) {
            int inf = 0;
            for (SpellEffect SE : SH.getEfectos()) {
                int efectoID = SE.getEfectoID();
                if (efectoID != 108 && efectoID != 81) continue;
                inf += 100 * Fórmulas.getMaxValor(SE.getValores());
            }
            return inf;
        }

        private static int calculaInfluenciaDa\u00f1o(Spell.StatsHechizos SH, Fight.Luchador lanzador, Fight.Luchador objetivo) {
            int influenciaTotal = 0;
            for (SpellEffect SE : SH.getEfectos()) {
                int inf = 0;
                switch (SE.getEfectoID()) {
                    case 5: {
                        inf = 500 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 77: {
                        inf = 1500 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 84: {
                        inf = 1500 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 89: {
                        inf = 200 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 91: {
                        inf = 150 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 92: {
                        inf = 150 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 93: {
                        inf = 150 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 94: {
                        inf = 150 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 95: {
                        inf = 150 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 96: {
                        inf = 100 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 97: {
                        inf = 100 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 98: {
                        inf = 100 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 99: {
                        inf = 100 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 100: {
                        inf = 100 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 101: {
                        inf = 1000 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 111: {
                        inf = -1000 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 117: {
                        inf = -500 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 121: {
                        inf = -100 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 122: {
                        inf = 200 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 123: {
                        inf = -200 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 124: {
                        inf = -200 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 125: {
                        inf = -200 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 126: {
                        inf = -200 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 127: {
                        inf = 1000 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 128: {
                        inf = -1000 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 131: {
                        inf = 300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 132: {
                        inf = 2000;
                        break;
                    }
                    case 138: {
                        inf = -50 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 141: {
                        inf = 50000;
                        break;
                    }
                    case 150: {
                        inf = -2000;
                        break;
                    }
                    case 168: {
                        inf = 1000 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 169: {
                        inf = 1000 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 210: {
                        inf = -300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 211: {
                        inf = -300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 212: {
                        inf = -300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 213: {
                        inf = -300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 214: {
                        inf = -300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 215: {
                        inf = 300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 216: {
                        inf = 300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 217: {
                        inf = 300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 218: {
                        inf = 300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 219: {
                        inf = 300 * Fórmulas.getMaxValor(SE.getValores());
                        break;
                    }
                    case 265: {
                        inf = -250 * Fórmulas.getMaxValor(SE.getValores());
                    }
                }
                if (objetivo == null) continue;
                if (lanzador.getParamEquipoAliado() == objetivo.getParamEquipoAliado()) {
                    influenciaTotal -= inf;
                    continue;
                }
                influenciaTotal += inf;
            }
            return influenciaTotal;
        }
    }
}

