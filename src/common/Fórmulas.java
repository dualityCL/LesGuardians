package common;

import common.LesGuardians;
import common.SocketManager;
import common.Mundo;
import java.util.ArrayList;
import java.util.Random;
import objects.Recaudador;
import objects.Dragopavo;
import objects.Combate;
import objects.Gremio;
import objects.Mapa;
import objects.Objeto;
import objects.Personaje;
import objects.EfectoHechizo;

public class Fórmulas {
    public static float ADIC_PJ = 0.95f;
    public static float ADIC_MOB = 1.0f;
    public static float ADIC_CAC = 0.9f;
    public static float PROSP_REQ = 1.0f;

    public static int getRandomValor(int i1, int i2) {
        Random rand = new Random();
        return rand.nextInt(i2 - i1 + 1) + i1;
    }

    public static int getRandomValor(String rango) {
        try {
            int num = 0;
            int veces = Integer.parseInt(rango.split("d")[0]);
            int margen = Integer.parseInt(rango.split("d")[1].split("\\+")[0]);
            int adicional = Integer.parseInt(rango.split("d")[1].split("\\+")[1]);
            for (int a = 0; a < veces; ++a) {
                num += Fórmulas.getRandomValor(1, margen);
            }
            return num += adicional;
        }
        catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    public static int getMaxValor(String rango) {
        try {
            int num = 0;
            int veces = Integer.parseInt(rango.split("d")[0]);
            int margen = Integer.parseInt(rango.split("d")[1].split("\\+")[0]);
            int adicional = Integer.parseInt(rango.split("d")[1].split("\\+")[1]);
            for (int a = 0; a < veces; ++a) {
                num += margen;
            }
            return num += adicional;
        }
        catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    public static int getMinValor(String rango) {
        try {
            int num = 0;
            int veces = Integer.parseInt(rango.split("d")[0]);
            int adicional = Integer.parseInt(rango.split("d")[1].split("\\+")[1]);
            for (int a = 0; a < veces; ++a) {
                ++num;
            }
            return num += adicional;
        }
        catch (NumberFormatException numberFormatException) {
            return -1;
        }
    }

    public static int getMedioValor(String rango) {
        try {
            int num = 0;
            int veces = Integer.parseInt(rango.split("d")[0]);
            int margen = Integer.parseInt(rango.split("d")[1].split("\\+")[0]);
            int adicional = Integer.parseInt(rango.split("d")[1].split("\\+")[1]);
            num += (1 + margen) / 2 * veces;
            return num += adicional;
        }
        catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    public static int getPorcTacleo(Combate.Luchador tacleador, Combate.Luchador tacleado) {
        int suerte;
        int agiTR = tacleador.getTotalStatsConBuff().getEfecto(119);
        int agiT = tacleado.getTotalStatsConBuff().getEfecto(119);
        int a = agiTR + 25;
        int b = agiTR + agiT + 50;
        if (b <= 0) {
            b = 1;
        }
        if ((suerte = (int)((long)(300 * a / b) - 100L)) < 10) {
            suerte = 10;
        }
        if (suerte > 90) {
            suerte = 90;
        }
        return suerte;
    }

    public static int calculFinalCura(Combate.Luchador curador, int rango, boolean esCaC) {
        int inteligencia = curador.getTotalStatsConBuff().getEfecto(126);
        int curas = curador.getTotalStatsConBuff().getEfecto(178);
        if (inteligencia < 0) {
            inteligencia = 0;
        }
        float adic = 100.0f;
        if (esCaC) {
            adic = 105.0f;
        }
        return (int)((double)rango * ((100.0 + (double)inteligencia) / (double)adic) + (double)(curas / 2));
    }

    public static int calculFinalDa\u00f1o(Combate pelea, Combate.Luchador lanzador, Combate.Luchador objetivo, int statID, int rango, boolean esCaC, int hechizoID, boolean veneno) {
        int armadura;
        Personaje perso;
        float adicPj = ADIC_PJ;
        float adicMob = ADIC_MOB;
        float a = 1.0f;
        float num = 0.0f;
        int statC = 0;
        float masDa\u00f1os = 0.0f;
        float porcDa\u00f1os = 0.0f;
        float resMasT = 0.0f;
        float resPorcT = 0.0f;
        int multiplicaDa\u00f1os = 0;
        Personaje.Stats totalLanzador = lanzador.getTotalStatsConBuff();
        Personaje.Stats totalObjetivo = objetivo.getTotalStatsConBuff();
        masDa\u00f1os = totalLanzador.getEfecto(112);
        porcDa\u00f1os = totalLanzador.getEfecto(138);
        multiplicaDa\u00f1os = totalLanzador.getEfecto(114);
        switch (statID) {
            case -1: {
                statC = 0;
                resMasT = 0.0f;
                resPorcT = 0.0f;
                break;
            }
            case 0: {
                statC = totalLanzador.getEfecto(118);
                resMasT = totalObjetivo.getEfecto(241);
                resPorcT = totalObjetivo.getEfecto(214);
                if (objetivo.getPersonaje() != null) {
                    resPorcT += (float)totalObjetivo.getEfecto(254);
                    resMasT += (float)totalObjetivo.getEfecto(264);
                }
                masDa\u00f1os += (float)totalLanzador.getEfecto(142);
                resMasT += (float)totalObjetivo.getEfecto(184);
                break;
            }
            case 1: {
                statC = totalLanzador.getEfecto(118);
                resMasT = totalObjetivo.getEfecto(242);
                resPorcT = totalObjetivo.getEfecto(210);
                if (objetivo.getPersonaje() != null) {
                    resPorcT += (float)totalObjetivo.getEfecto(250);
                    resMasT += (float)totalObjetivo.getEfecto(260);
                }
                resMasT += (float)totalObjetivo.getEfecto(183);
                break;
            }
            case 2: {
                statC = totalLanzador.getEfecto(123);
                resMasT = totalObjetivo.getEfecto(243);
                resPorcT = totalObjetivo.getEfecto(211);
                if (objetivo.getPersonaje() != null) {
                    resPorcT += (float)totalObjetivo.getEfecto(251);
                    resMasT += (float)totalObjetivo.getEfecto(261);
                }
                resMasT += (float)totalObjetivo.getEfecto(183);
                break;
            }
            case 3: {
                statC = totalLanzador.getEfecto(126);
                resMasT = totalObjetivo.getEfecto(240);
                resPorcT = totalObjetivo.getEfecto(213);
                if (objetivo.getPersonaje() != null) {
                    resPorcT += (float)totalObjetivo.getEfecto(253);
                    resMasT += (float)totalObjetivo.getEfecto(263);
                }
                resMasT += (float)totalObjetivo.getEfecto(183);
                break;
            }
            case 4: {
                statC = totalLanzador.getEfecto(119);
                resMasT = totalObjetivo.getEfecto(244);
                resPorcT = totalObjetivo.getEfecto(212);
                if (objetivo.getPersonaje() != null) {
                    resPorcT += (float)totalObjetivo.getEfecto(252);
                    resMasT += (float)totalObjetivo.getEfecto(262);
                }
                resMasT += (float)totalObjetivo.getEfecto(183);
            }
        }
        if (objetivo.getPersonaje() != null && resPorcT > 75.0f) {
            resPorcT = 75.0f;
        }
        if (statC < 0) {
            statC = 0;
        }
        if ((perso = lanzador.getPersonaje()) != null && esCaC) {
            adicPj = ADIC_CAC;
            int armaTipo = 0;
            try {
                armaTipo = perso.getObjPosicion(1).getModelo().getTipo();
            }
            catch (Exception exception) {
                // empty catch block
            }
            float i = 0.0f;
            int porc = 90;
            int clase = perso.getClase(true);
            switch (armaTipo) {
                case 2: {
                    if (lanzador.tieneBuffHechizoID(392)) {
                        i = lanzador.getDa\u00f1oDominio(392);
                    }
                    if (clase == 4) {
                        porc = 95;
                        break;
                    }
                    if (clase != 9) break;
                    porc = 100;
                    break;
                }
                case 3: {
                    if (lanzador.tieneBuffHechizoID(394)) {
                        i = lanzador.getDa\u00f1oDominio(394);
                    }
                    if (clase == 1 || clase == 5) {
                        porc = 95;
                        break;
                    }
                    if (clase != 7) break;
                    porc = 100;
                    break;
                }
                case 4: {
                    if (lanzador.tieneBuffHechizoID(390)) {
                        i = lanzador.getDa\u00f1oDominio(390);
                    }
                    if (clase == 7 || clase == 2 || clase == 12) {
                        porc = 95;
                        break;
                    }
                    if (clase != 1 && clase != 10) break;
                    porc = 100;
                    break;
                }
                case 5: {
                    if (lanzador.tieneBuffHechizoID(395)) {
                        i = lanzador.getDa\u00f1oDominio(395);
                    }
                    if (clase == 9 || clase == 6) {
                        porc = 95;
                        break;
                    }
                    if (clase != 4) break;
                    porc = 100;
                    break;
                }
                case 6: {
                    if (lanzador.tieneBuffHechizoID(391)) {
                        i = lanzador.getDa\u00f1oDominio(391);
                    }
                    if (clase != 8 && clase != 6) break;
                    porc = 100;
                    break;
                }
                case 7: {
                    if (lanzador.tieneBuffHechizoID(393)) {
                        i = lanzador.getDa\u00f1oDominio(393);
                    }
                    if (clase == 3 || clase == 8 || clase == 10) {
                        porc = 95;
                        break;
                    }
                    if (clase != 2 && clase != 5) break;
                    porc = 100;
                    break;
                }
                case 8: {
                    if (lanzador.tieneBuffHechizoID(396)) {
                        i = lanzador.getDa\u00f1oDominio(396);
                    }
                    if (clase != 3) break;
                    porc = 100;
                    break;
                }
                case 19: {
                    if (lanzador.tieneBuffHechizoID(397)) {
                        i = lanzador.getDa\u00f1oDominio(397);
                    }
                    if (clase != 12) break;
                    porc = 100;
                }
            }
            a = ((float)porc + i) / 100.0f;
        }
        num = a * ((float)rango * (((float)(100 + statC) + porcDa\u00f1os + (float)(multiplicaDa\u00f1os * 100)) / 100.0f)) + masDa\u00f1os;
        if (lanzador.getMob() != null && lanzador.getMob().getModelo().getID() == 116) {
            num = lanzador.getPDVConBuff();
        }
        int reduc = (int)((num -= resMasT) * resPorcT / 100.0f);
        num -= (float)reduc;
        if (!veneno && (armadura = Fórmulas.getResisArmadura(objetivo, statID)) > 0) {
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 105, String.valueOf(lanzador.getID()), String.valueOf(objetivo.getID()) + "," + armadura);
            num -= (float)armadura;
        }
        if (num < 1.0f) {
            num = 0.0f;
        }
        if (lanzador.getMob() != null) {
            if (lanzador.getMob().getModelo().getID() == 116) {
                return (int)num;
            }
            float calculCoef = 5.5f * (float)lanzador.getNivel() / (200.0f + (float)lanzador.getNivel());
            if (calculCoef < 1.0f) {
                calculCoef = 1.0f;
            }
            if (calculCoef > 5.0f) {
                calculCoef = 5.0f;
            }
            return (int)(num * calculCoef * adicMob);
        }
        if (num > 50.0f && (num = (float)((int)((double)num * ((double)adicPj - 0.01)))) > 100.0f && (num = (float)((int)((double)num * ((double)adicPj - 0.03)))) > 200.0f && (num = (float)((int)((double)num * ((double)adicPj - 0.06)))) > 400.0f) {
            num = (int)((double)num * ((double)adicPj - 0.1));
        }
        if (pelea.getTipoPelea() != 4) {
            int pdvMax = objetivo.getPDVMax();
            if ((pdvMax -= (int)(num * 0.1f)) < 0) {
                pdvMax = 0;
            }
            objetivo.setPDVMAX(pdvMax);
        }
        return (int)num;
    }

    public static int calcularCosteZaap(Mapa map1, Mapa map2) {
        return 10 * (Math.abs(map2.getX() - map1.getX()) + Math.abs(map2.getY() - map1.getY()) - 1);
    }

    private static int getResisArmadura(Combate.Luchador afectado, int statID) {
        int defensa = 0;
        int adic = 100;
        block18: for (EfectoHechizo EH : afectado.getBuffsPorEfectoID(265)) {
            Combate.Luchador lanzArmadura;
            float div = 2.0f;
            switch (EH.getHechizoID()) {
                case 1: {
                    if (statID != 3) continue block18;
                    div = 1.5f;
                    lanzArmadura = EH.getLanzador();
                    break;
                }
                case 6: {
                    if (statID != 1 && statID != 0) continue block18;
                    div = 1.5f;
                    lanzArmadura = EH.getLanzador();
                    break;
                }
                case 14: {
                    if (statID != 4) continue block18;
                    div = 1.5f;
                    lanzArmadura = EH.getLanzador();
                    break;
                }
                case 18: {
                    if (statID != 2) continue block18;
                    div = 1.5f;
                    lanzArmadura = EH.getLanzador();
                    break;
                }
                default: {
                    lanzArmadura = EH.getLanzador();
                }
            }
            Personaje.Stats statsLanzArmadura = lanzArmadura.getTotalStatsConBuff();
            int inteligencia = statsLanzArmadura.getEfecto(126);
            int carac = 0;
            switch (statID) {
                case 4: {
                    carac = statsLanzArmadura.getEfecto(119);
                    break;
                }
                case 3: {
                    carac = statsLanzArmadura.getEfecto(126);
                    break;
                }
                case 2: {
                    carac = statsLanzArmadura.getEfecto(123);
                    break;
                }
                case 0: 
                case 1: {
                    carac = statsLanzArmadura.getEfecto(118);
                }
            }
            int valor = EH.getValor();
            int a = valor * (100 + (int)((float)inteligencia / div) + carac / 2) / adic;
            defensa += a;
        }
        Personaje.Stats statsAfectado = afectado.getTotalStatsConBuff();
        for (EfectoHechizo SE : afectado.getBuffsPorEfectoID(105)) {
            int inteligencia = statsAfectado.getEfecto(126);
            int carac = 0;
            switch (statID) {
                case 4: {
                    carac = statsAfectado.getEfecto(119);
                    break;
                }
                case 3: {
                    carac = statsAfectado.getEfecto(126);
                    break;
                }
                case 2: {
                    carac = statsAfectado.getEfecto(123);
                    break;
                }
                case 0: 
                case 1: {
                    carac = statsAfectado.getEfecto(118);
                }
            }
            int valor = SE.getValor();
            int a = valor * (100 + inteligencia / 2 + carac / 2) / adic;
            defensa += a;
        }
        return defensa;
    }

    public static int getPuntosPerdidos(char z, int valor, Combate.Luchador lanzador, Combate.Luchador objetivo) {
        int esquivaLanzador = z == 'a' ? lanzador.getTotalStatsConBuff().getEfecto(160) : lanzador.getTotalStatsConBuff().getEfecto(161);
        int esquivaObjetivo = z == 'a' ? objetivo.getTotalStatsConBuff().getEfecto(160) : objetivo.getTotalStatsConBuff().getEfecto(161);
        int ptsMax = z == 'a' ? objetivo.getTotalStatsConBuff().getEfecto(111) : objetivo.getTotalStatsConBuff().getEfecto(128);
        int resta = 0;
        int acierto = esquivaLanzador - esquivaObjetivo;
        if ((acierto /= 10) > 15) {
            acierto = 15;
        }
        if (acierto < 1) {
            acierto = 1;
        }
        for (int i = 0; i < valor; ++i) {
            int azar = Fórmulas.getRandomValor(0, 16);
            if (azar > acierto) continue;
            ++resta;
        }
        if (resta > ptsMax) {
            resta = ptsMax;
        }
        return resta;
    }

    public static long getXpGanadaRecau(Recaudador recaudador, long totalXP) {
        Gremio G = Mundo.getGremio(recaudador.getGremioID());
        float sabi = G.getStats(124);
        float coef = (sabi + 100.0f) / 100.0f;
        long xpGanada = 0L;
        xpGanada = (int)(coef * (float)totalXP);
        return (long)((float)xpGanada * LesGuardians.RATE_XP_PVM);
    }

    public static long getXpGanadaPVM(Combate.Luchador perso, ArrayList<Combate.Luchador> ganadores, ArrayList<Combate.Luchador> perdedores, long grupoXP) {
        if (perso.getPersonaje() == null) {
            return 0L;
        }
        if (ganadores.contains(perso)) {
            float sabiduria = perso.getTotalStatsConBuff().getEfecto(124);
            float coef = (sabiduria + 100.0f) / 100.0f;
            long xpGanada = 0L;
            int nivelMax = 0;
            for (Combate.Luchador entry : ganadores) {
                if (entry.getNivel() <= nivelMax) continue;
                nivelMax = entry.getNivel();
            }
            int nro = 0;
            for (Combate.Luchador entry : ganadores) {
                if (entry.getNivel() <= nivelMax / 3) continue;
                ++nro;
            }
            float bonus = 1.0f;
            if (nro == 2) {
                bonus = 1.1f;
            }
            if (nro == 3) {
                bonus = 1.3f;
            }
            if (nro == 4) {
                bonus = 2.2f;
            }
            if (nro == 5) {
                bonus = 2.5f;
            }
            if (nro == 6) {
                bonus = 2.8f;
            }
            if (nro == 7) {
                bonus = 3.1f;
            }
            if (nro >= 8) {
                bonus = 3.5f;
            }
            int nivelPerdedores = 0;
            for (Combate.Luchador entry : perdedores) {
                nivelPerdedores += entry.getNivel();
            }
            int nivelGanadores = 0;
            for (Combate.Luchador entry : ganadores) {
                nivelGanadores += entry.getNivel();
            }
            float porcEntreGyP = 1.0f + (float)(nivelPerdedores / nivelGanadores);
            if ((double)porcEntreGyP <= 1.3) {
                porcEntreGyP = 1.3f;
            }
            int nivel = perso.getNivel();
            float porcEntrePjyG = 1.0f + (float)(nivel / nivelGanadores);
            xpGanada = (long)((float)grupoXP * porcEntreGyP * bonus * coef * porcEntrePjyG);
            return (long)((float)xpGanada * LesGuardians.RATE_XP_PVM);
        }
        return 0L;
    }

    public static long getXPDonadaGremio(Combate.Luchador perso, long xpGanada) {
        if (perso.getPersonaje() == null) {
            return 0L;
        }
        if (perso.getPersonaje().getMiembroGremio() == null) {
            return 0L;
        }
        Gremio.MiembroGremio gm = perso.getPersonaje().getMiembroGremio();
        float xp = xpGanada;
        float nivel = perso.getNivel();
        float nivelGremio = perso.getPersonaje().getGremio().getNivel();
        float porcXPDonada = (float)gm.getPorcXpDonada() / 100.0f;
        float maxP = xp * porcXPDonada * 0.1f;
        float diferencia = Math.abs(nivel - nivelGremio);
        float alGremio = diferencia >= 70.0f ? maxP * 0.1f : (diferencia >= 31.0f && diferencia <= 69.0f ? (float)((double)maxP - (double)(maxP * 0.1f) * Math.floor((diferencia + 30.0f) / 10.0f)) : (diferencia >= 10.0f && diferencia <= 30.0f ? (float)((double)maxP - (double)(maxP * 0.2f) * Math.floor(diferencia / 10.0f)) : maxP));
        return Math.round(alGremio);
    }

    public static long getXPDonadaDragopavo(Combate.Luchador luchador, long xpGanada) {
        Personaje perso = luchador.getPersonaje();
        if (perso == null) {
            return 0L;
        }
        Dragopavo pavo = perso.getMontura();
        if (pavo == null) {
            return 0L;
        }
        float xp = xpGanada;
        float coef = 1.0f;
        float porcMontura = (float)perso.getXpDonadaMontura() / 100.0f;
        if (pavo.getNivel() < 50) {
            coef = 1.0f;
        } else if (pavo.getNivel() < 100) {
            coef = 0.75f;
        } else if (pavo.getNivel() < 150) {
            coef = 0.5f;
        } else if (pavo.getNivel() <= 200) {
            coef = 0.25f;
        }
        long xpdonada = (long)(xp * porcMontura * coef / 100.0f);
        return xpdonada;
    }

    public static long getKamasGanadas(Combate.Luchador luchador, long maxkamas, long minkamas) {
        int prospeccion = luchador.getTotalStatsConBuff().getEfecto(176);
        float coef = ((float)prospeccion + 100.0f) / 100.0f;
        long kamas = (long)(Math.random() * (double)(++maxkamas - minkamas) + (double)minkamas);
        return (long)((float)kamas * coef * LesGuardians.RATE_KAMAS);
    }

    public static long getKamasGanadaRecau(long maxkamas, long minkamas) {
        long kamas = (long)(Math.random() * (double)(++maxkamas - minkamas)) + minkamas;
        return (long)((float)kamas * LesGuardians.RATE_KAMAS);
    }

    public static int calculoPorcCambioElenemto(int nivelOficio, int nivelObjeto, int nivelRunaElemento) {
        int K = 1;
        if (nivelRunaElemento == 1) {
            K = 100;
        } else if (nivelRunaElemento == 25) {
            K = 175;
        } else if (nivelRunaElemento == 50) {
            K = 350;
        }
        return nivelOficio * 100 / (K + nivelObjeto);
    }

    public static int calcularHonorGanado(ArrayList<Combate.Luchador> ganadores, ArrayList<Combate.Luchador> perdedores, Combate.Luchador luchador) {
        float totalNivAlineacionGanador = 0.0f;
        float totalNivelGanador = 0.0f;
        float totalNivAlineacionPerdedor = 0.0f;
        float totalNivelPerdedor = 0.0f;
        boolean prisma = false;
        int luchadores = 0;
        for (Combate.Luchador lucha : ganadores) {
            if (lucha.getPersonaje() == null && lucha.getPrisma() == null) continue;
            if (lucha.getPersonaje() != null) {
                totalNivelGanador += (float)lucha.getNivel();
                totalNivAlineacionGanador += (float)lucha.getPersonaje().getNivelAlineacion();
                continue;
            }
            prisma = true;
            totalNivelGanador += 200.0f;
            totalNivAlineacionGanador += (float)(lucha.getPrisma().getNivel() * 20 + 80);
        }
        for (Combate.Luchador lucha : perdedores) {
            if (lucha.getPersonaje() == null && lucha.getPrisma() == null) continue;
            if (lucha.getPersonaje() != null) {
                totalNivelPerdedor += (float)lucha.getNivel();
                totalNivAlineacionPerdedor += (float)lucha.getPersonaje().getNivelAlineacion();
                ++luchadores;
                continue;
            }
            prisma = true;
            totalNivelPerdedor += 200.0f;
            totalNivAlineacionPerdedor += (float)(lucha.getPrisma().getNivel() * 15 + 80);
        }
        if (!prisma && totalNivelGanador - totalNivelPerdedor > (float)(15 * luchadores)) {
            return 0;
        }
        int base = (int)(100.0f * (totalNivAlineacionPerdedor * totalNivelPerdedor / (totalNivAlineacionGanador * totalNivelGanador))) / ganadores.size();
        if (perdedores.contains(luchador)) {
            base = -base;
        }
        return (int)((float)base * LesGuardians.RATE_HONOR);
    }

    public static Mundo.Duo<Integer, Integer> decompilarPiedraAlma(Objeto objeto) {
        String[] stats = objeto.convertirStatsAString().split("#");
        int nivelMax = Integer.parseInt(stats[3], 16);
        int suerte = Integer.parseInt(stats[1], 16);
        return new Mundo.Duo<Integer, Integer>(suerte, nivelMax);
    }

    public static int totalPorcCaptura(int suertePiedra, Personaje perso) {
        int suerteHechizo = 0;
        switch (perso.getStatsHechizo(413).getNivel()) {
            case 1: {
                suerteHechizo = 1;
                break;
            }
            case 2: {
                suerteHechizo = 3;
                break;
            }
            case 3: {
                suerteHechizo = 6;
                break;
            }
            case 4: {
                suerteHechizo = 10;
                break;
            }
            case 5: {
                suerteHechizo = 15;
                break;
            }
            case 6: {
                suerteHechizo = 25;
            }
        }
        return suerteHechizo + suertePiedra;
    }

    public static int costeHechizo(int numero) {
        int total = 0;
        for (int i = 1; i < numero; ++i) {
            total += i;
        }
        return total;
    }

    public static int suerteFM(int pesoTotalBase, int pesoTotalActual, int pesoStatActual, int runa, int diferencia, float coef) {
        float porcentaje = 0.0f;
        float a = (float)(pesoTotalBase + diferencia) * coef * LesGuardians.RATE_PORC_FM;
        float b = (float)(Math.sqrt(pesoTotalActual + pesoStatActual) + (double)runa);
        if ((double)b < 1.0) {
            b = 1.0f;
        }
        porcentaje = a / b;
        return (int)porcentaje;
    }

    public static int getXPMision(int nivel) {
        if (nivel < 50) {
            return 10000;
        }
        if (nivel < 60) {
            return 65000;
        }
        if (nivel < 70) {
            return 90000;
        }
        if (nivel < 80) {
            return 120000;
        }
        if (nivel < 90) {
            return 160000;
        }
        if (nivel < 100) {
            return 210000;
        }
        if (nivel < 110) {
            return 270000;
        }
        if (nivel < 120) {
            return 350000;
        }
        if (nivel < 130) {
            return 440000;
        }
        if (nivel < 140) {
            return 540000;
        }
        if (nivel < 150) {
            return 650000;
        }
        if (nivel < 155) {
            return 760000;
        }
        if (nivel < 160) {
            return 880000;
        }
        if (nivel < 165) {
            return 1000000;
        }
        if (nivel < 170) {
            return 1130000;
        }
        if (nivel < 175) {
            return 1300000;
        }
        if (nivel < 180) {
            return 1500000;
        }
        if (nivel < 185) {
            return 1800000;
        }
        if (nivel < 190) {
            return 0x200B20;
        }
        if (nivel < 195) {
            return 2500000;
        }
        if (nivel < 200) {
            return 3200000;
        }
        if (nivel >= 200) {
            return 5000000;
        }
        return 0;
    }
}

