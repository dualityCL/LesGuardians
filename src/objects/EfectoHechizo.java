package objects;

import common.Constantes;
import common.CryptManager;
import common.Fórmulas;
import common.Pathfinding;
import common.SocketManager;
import common.Mundo;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Combate;
import objects.MobModelo;
import objects.Mapa;
import objects.Personaje;
import objects.Hechizo;

public class EfectoHechizo {
    private int _efectoID;
    private int _turnos = 0;
    private String _valores = "0d0+0";
    private byte _suerte = (byte)100;
    private String _args;
    private int _valor = 0;
    private Combate.Luchador _lanzador = null;
    private int _hechizoID = 0;
    private int _nivelHechizo = 1;
    private boolean _desbuffeable = true;
    private int _duracion = 0;
    private int _turnosOriginales = 0;
    private Mapa.Celda _celdaObj = null;
    private boolean _veneno = false;
    private static int[] efectosReto = new int[]{77, 169, 84, 168, 108, 116, 320, 81, 82, 85, 86, 87, 88, 89, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101};

    public EfectoHechizo(int aID, String aArgs, int aHechizo, int aNivelHechizo) {
        this._efectoID = aID;
        this._args = aArgs;
        this._hechizoID = aHechizo;
        this._nivelHechizo = aNivelHechizo;
        try {
            String[] args = this._args.split(";");
            this._valor = Integer.parseInt(args[0]);
            this._turnos = Integer.parseInt(args[3]);
            this._turnosOriginales = Integer.parseInt(args[3]);
            this._suerte = Byte.parseByte(args[4]);
            this._valores = args[5];
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public EfectoHechizo(int id, int aValor, int aDuracion, int aTurnos, boolean debuffeable, Combate.Luchador aLanzador, String args2, int aHechizoID, boolean veneno) {
        this._efectoID = id;
        this._valor = aValor;
        this._turnos = aTurnos;
        this._desbuffeable = debuffeable;
        this._lanzador = aLanzador;
        this._duracion = aDuracion;
        this._args = args2;
        this._hechizoID = aHechizoID;
        this._veneno = veneno;
        try {
            String[] args = this._args.split(";");
            this._turnosOriginales = Integer.parseInt(args[3]);
            this._valores = args[5];
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static boolean esEfectoReto(int efecto) {
        int[] arrn = efectosReto;
        int n = efectosReto.length;
        for (int i = 0; i < n; ++i) {
            Integer e = arrn[i];
            if (e != efecto) continue;
            return true;
        }
        return false;
    }

    public boolean esMismoHechizo(int id) {
        return this._hechizoID == id;
    }

    public int getDuracion() {
        return this._duracion;
    }

    public int getTurnos() {
        return this._turnos;
    }

    public boolean esDesbufeable() {
        return this._desbuffeable;
    }

    public void setTurnos(int aturnos) {
        this._turnos = aturnos;
    }

    public int getEfectoID() {
        return this._efectoID;
    }

    public int getValor() {
        return this._valor;
    }

    public String getValores() {
        return this._valores;
    }

    public boolean getVenenoso() {
        return this._veneno;
    }

    public int getSuerte() {
        return this._suerte;
    }

    public String getArgs() {
        return this._args;
    }

    public void setArgs(String nuevasArgs) {
        this._args = nuevasArgs;
        try {
            String[] args = this._args.split(";");
            this._valor = Integer.parseInt(args[0]);
            this._turnos = Integer.parseInt(args[3]);
            this._suerte = Byte.parseByte(args[4]);
            this._valores = args[5];
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void setEfectoID(int id) {
        this._efectoID = id;
    }

    public void setValor(short v) {
        this._valor = v;
    }

    public int disminuirDuracion() {
        --this._duracion;
        return this._duracion;
    }

    public void aplicarBuffDeInicioTurno(Combate pelea, Combate.Luchador afectado) {
        ArrayList<Combate.Luchador> objetivos = new ArrayList<Combate.Luchador>();
        objetivos.add(afectado);
        this._turnos = -1;
        this.aplicarAPelea(pelea, this._lanzador, objetivos, false, null);
    }

    public void aplicarHechizoAPelea(Combate pelea, Combate.Luchador lanzador, Mapa.Celda casilla, ArrayList<Combate.Luchador> objetivos, ArrayList<Mapa.Celda> celdas) {
        this._celdaObj = casilla;
        this.aplicarAPelea(pelea, lanzador, objetivos, false, celdas);
    }

    public Combate.Luchador getLanzador() {
        return this._lanzador;
    }

    public int getHechizoID() {
        return this._hechizoID;
    }

    public int getMaxMinHechizo(Combate.Luchador objetivo, int valor) {
        int val = valor;
        if (objetivo.tieneBuff(782)) {
            int max = Integer.parseInt(this._args.split(";")[1]);
            if (max == -1) {
                max = Integer.parseInt(this._args.split(";")[0]);
            }
            valor = max;
        }
        if (objetivo.tieneBuff(781)) {
            valor = Integer.parseInt(this._args.split(";")[0]);
        }
        return val;
    }

    public static ArrayList<Combate.Luchador> getAfectados(Combate pelea, ArrayList<Mapa.Celda> celdas, int hechizo) {
        ArrayList<Combate.Luchador> objetivos = new ArrayList<Combate.Luchador>();
        if (hechizo == 418) {
            int i = 4;
            ArrayList<Mapa.Celda> celdas1 = new ArrayList<Mapa.Celda>();
            ArrayList<Mapa.Celda> celdas2 = new ArrayList<Mapa.Celda>();
            ArrayList<Mapa.Celda> celdas3 = new ArrayList<Mapa.Celda>();
            ArrayList<Mapa.Celda> celdas4 = new ArrayList<Mapa.Celda>();
            for (Mapa.Celda celda : celdas) {
                if (i % 4 == 0) {
                    celdas1.add(celda);
                } else if (i % 4 == 1) {
                    celdas2.add(celda);
                } else if (i % 4 == 2) {
                    celdas3.add(celda);
                } else {
                    celdas4.add(celda);
                }
                ++i;
            }
            celdas.clear();
            celdas.addAll(celdas4);
            celdas.addAll(celdas3);
            celdas.addAll(celdas2);
            celdas.addAll(celdas1);
        } else if (hechizo == 165) {
            ArrayList<Mapa.Celda> celdas1 = new ArrayList<Mapa.Celda>();
            for (int j = celdas.size() - 1; j >= 0; --j) {
                celdas1.add(celdas.get(j));
            }
            celdas.clear();
            celdas.addAll(celdas1);
        }
        for (Mapa.Celda celda : celdas) {
            Combate.Luchador luch;
            if (celda == null || (luch = celda.getPrimerLuchador()) == null) continue;
            objetivos.add(luch);
        }
        return objetivos;
    }

    public static int aplicarBuffContraGolpe(int da\u00f1oFinal, Combate.Luchador objetivo, Combate.Luchador lanzador, Combate pelea, int hechizoID, boolean esVeneno) {
        int[] arrn = Constantes.BUFF_ACCION_RESPUESTA;
        int n = Constantes.BUFF_ACCION_RESPUESTA.length;
        for (int i = 0; i < n; ++i) {
            int id = arrn[i];
            block23: for (EfectoHechizo buff : objetivo.getBuffsPorEfectoID(id)) {
                if (objetivo.estaMuerto()) {
                    return 0;
                }
                block6 : switch (id) {
                    case 9: {
                        Mapa.Celda nueva;
                        short d;
                        if (esVeneno || (d = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), objetivo.getCeldaPelea().getID(), lanzador.getCeldaPelea().getID())) > 1) continue block23;
                        int elusion = buff.getValor();
                        int azar = Fórmulas.getRandomValor(1, 100);
                        if (azar > elusion) continue block23;
                        int nroCasillas = 0;
                        try {
                            nroCasillas = Integer.parseInt(buff.getArgs().split(";")[1]);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                        if (nroCasillas == 0 || objetivo.tieneEstado(6)) continue block23;
                        Mapa.Celda aCelda = lanzador.getCeldaPelea();
                        Combate.Luchador afectado = null;
                        Mapa mapaCopia = pelea.getMapaCopia();
                        int nuevaCeldaID = Pathfinding.getCeldaDespEmpujon(mapaCopia, aCelda, objetivo.getCeldaPelea(), nroCasillas, pelea, objetivo);
                        if (nuevaCeldaID == 0) continue block23;
                        da\u00f1oFinal = 0;
                        if (nuevaCeldaID < 0) {
                            float c;
                            int a = -nuevaCeldaID;
                            int coef = Fórmulas.getRandomValor("1d5+8");
                            float b = (float)lanzador.getNivel() / 100.0f;
                            if ((double)b < 0.1) {
                                b = 0.1f;
                            }
                            if ((da\u00f1oFinal = (int)((float)coef * (c = b * (float)a))) < 1) {
                                da\u00f1oFinal = 1;
                            }
                            if (da\u00f1oFinal > objetivo.getPDVConBuff()) {
                                da\u00f1oFinal = objetivo.getPDVConBuff();
                            }
                            if (da\u00f1oFinal > 0) {
                                objetivo.restarPDV(da\u00f1oFinal);
                                if (objetivo.getPDVConBuff() <= 0) {
                                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(lanzador.getID()), String.valueOf(objetivo.getID()) + ",-" + da\u00f1oFinal);
                                    pelea.agregarAMuertos(objetivo);
                                    break;
                                }
                            }
                            a = nroCasillas - a;
                            nuevaCeldaID = Pathfinding.getCeldaDespEmpujon(mapaCopia, aCelda, objetivo.getCeldaPelea(), a, pelea, objetivo);
                            char dir = Pathfinding.getDirEntreDosCeldas(aCelda.getID(), objetivo.getCeldaPelea().getID(), mapaCopia, true);
                            short celdaSigID = 0;
                            celdaSigID = nuevaCeldaID == 0 ? Pathfinding.getSigIDCeldaMismaDir(objetivo.getCeldaPelea().getID(), dir, mapaCopia, true) : Pathfinding.getSigIDCeldaMismaDir((short)nuevaCeldaID, dir, mapaCopia, true);
                            Mapa.Celda celdaSig = mapaCopia.getCelda(celdaSigID);
                            if (celdaSig != null) {
                                afectado = celdaSig.getPrimerLuchador();
                            }
                        }
                        if (nuevaCeldaID != 0 && (nueva = mapaCopia.getCelda((short)nuevaCeldaID)) != null) {
                            objetivo.getCeldaPelea().getLuchadores().clear();
                            objetivo.setCeldaPelea(nueva);
                            nueva.addLuchador(objetivo);
                            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 5, String.valueOf(lanzador.getID()), String.valueOf(objetivo.getID()) + "," + nuevaCeldaID);
                            try {
                                Thread.sleep(500L);
                            }
                            catch (Exception coef) {
                                // empty catch block
                            }
                        }
                        if (da\u00f1oFinal > 0) {
                            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(lanzador.getID()), String.valueOf(objetivo.getID()) + ",-" + da\u00f1oFinal);
                        }
                        if (afectado != null) {
                            int da\u00f1oFinal2 = da\u00f1oFinal / 2;
                            if (da\u00f1oFinal2 < 1) {
                                da\u00f1oFinal2 = 1;
                            }
                            if (da\u00f1oFinal2 > afectado.getPDVConBuff()) {
                                da\u00f1oFinal2 = afectado.getPDVConBuff();
                            }
                            if (da\u00f1oFinal2 > 0) {
                                afectado.restarPDV(da\u00f1oFinal2);
                                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(lanzador.getID()), String.valueOf(afectado.getID()) + ",-" + da\u00f1oFinal2);
                                if (afectado.getPDVConBuff() <= 0) {
                                    pelea.agregarAMuertos(afectado);
                                }
                            }
                        }
                        try {
                            Thread.sleep(300L);
                        }
                        catch (Exception da\u00f1oFinal2) {
                            // empty catch block
                        }
                        for (Combate.Trampa trampa : pelea.getTrampas()) {
                            short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), (short)nuevaCeldaID);
                            if (dist > trampa.getTama\u00f1o()) continue;
                            trampa.activaTrampa(objetivo);
                            break;
                        }
                        return 0;
                    }
                    case 79: {
                        if (esVeneno) continue block23;
                        try {
                            String[] infos = buff.getArgs().split(";");
                            int coefDa\u00f1o = Integer.parseInt(infos[0]);
                            int coefCura = Integer.parseInt(infos[1]);
                            int suerte = Integer.parseInt(infos[2]);
                            int jet = Fórmulas.getRandomValor(0, 99);
                            if (jet < suerte) {
                                if (-(da\u00f1oFinal = -(da\u00f1oFinal * coefCura)) <= objetivo.getPDVMaxConBuff() - objetivo.getPDVConBuff()) continue block23;
                                da\u00f1oFinal = -(objetivo.getPDVMaxConBuff() - objetivo.getPDVConBuff());
                                break;
                            }
                            da\u00f1oFinal *= coefDa\u00f1o;
                        }
                        catch (Exception infos) {}
                        continue block23;
                    }
                    case 107: 
                    case 220: {
                        switch (hechizoID) {
                            case 66: 
                            case 71: 
                            case 164: 
                            case 181: 
                            case 196: 
                            case 200: 
                            case 219: {
                                break block6;
                            }
                        }
                        if (esVeneno) continue block23;
                        String[] args = buff.getArgs().split(";");
                        float coef = 1 + objetivo.getTotalStatsConBuff().getEfecto(124) / 100;
                        int reenvio = 0;
                        try {
                            reenvio = Integer.parseInt(args[1]) != -1 ? (int)(coef * (float)Fórmulas.getRandomValor(Integer.parseInt(args[0]), Integer.parseInt(args[1]))) : (int)(coef * (float)Integer.parseInt(args[0]));
                        }
                        catch (Exception e) {
                            break;
                        }
                        if (reenvio > da\u00f1oFinal) {
                            reenvio = da\u00f1oFinal;
                        }
                        da\u00f1oFinal -= reenvio;
                        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 107, "-1", String.valueOf(objetivo.getID()) + "," + reenvio);
                        if (reenvio > lanzador.getPDVConBuff()) {
                            reenvio = lanzador.getPDVConBuff();
                        }
                        if (da\u00f1oFinal < 0) {
                            da\u00f1oFinal = 0;
                        }
                        lanzador.restarPDV(reenvio);
                        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(lanzador.getID()), String.valueOf(lanzador.getID()) + ",-" + reenvio);
                        break;
                    }
                    case 776: {
                        if (esVeneno || !objetivo.tieneBuff(776)) continue block23;
                        int pdvMax = objetivo.getPDVMax();
                        float pda\u00f1o = (float)objetivo.getValorBuffPelea(776) / 100.0f;
                        if ((pdvMax -= (int)((float)da\u00f1oFinal * pda\u00f1o)) < 0) {
                            pdvMax = 0;
                        }
                        objetivo.setPDVMAX(pdvMax);
                        break;
                    }
                    case 788: {
                        if (esVeneno) continue block23;
                        int porc = lanzador.getPersonaje() == null ? 1 : 2;
                        int gana = da\u00f1oFinal / porc;
                        int stat = buff.getValor();
                        int max = 0;
                        try {
                            max = Integer.parseInt(buff.getArgs().split(";")[1]);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                        if ((max -= objetivo.getBonusCastigo(stat)) <= 0) continue block23;
                        if (gana > max) {
                            gana = max;
                        }
                        objetivo.setBonusCastigo(objetivo.getBonusCastigo(stat) + gana, stat);
                        objetivo.addBuff(stat, gana, 5, 1, false, buff.getHechizoID(), buff.getArgs(), lanzador, buff.getVenenoso());
                        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, stat, String.valueOf(lanzador.getID()), String.valueOf(objetivo.getID()) + "," + gana + "," + 5);
                        break;
                    }
                    default: {
                        System.out.println("Efecto id " + id + " no definido como EFECTO DE CONTRAGOLPE.");
                    }
                }
            }
        }
        return da\u00f1oFinal;
    }

    public void aplicarAPelea(Combate pelea, Combate.Luchador aLanzador, ArrayList<Combate.Luchador> objetivos, boolean esCaC, ArrayList<Mapa.Celda> celdas) {
        Personaje perso;
        try {
            if (this._turnos != -1) {
                this._turnos = Integer.parseInt(this._args.split(";")[3]);
            }
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        this._lanzador = aLanzador;
        try {
            this._valores = this._args.split(";")[5];
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (this._lanzador.getPersonaje() != null && (perso = this._lanzador.getPersonaje()).getHechizosSetClase().containsKey(this._hechizoID)) {
            int modi = 0;
            if (this._efectoID == 108) {
                modi = perso.getModifSetClase(this._hechizoID, 284);
            } else if (this._efectoID >= 91 && this._efectoID <= 100) {
                modi = perso.getModifSetClase(this._hechizoID, 283);
            }
            String jeta = this._valores.split("\\+")[0];
            int bonus = Integer.parseInt(this._valores.split("\\+")[1]) + modi;
            this._valores = String.valueOf(jeta) + "+" + bonus;
        }
        pelea.setUltAfec((byte)objetivos.size());
        if (pelea.getTipoPelea() == 4 && this._lanzador.getPersonaje() != null && EfectoHechizo.esEfectoReto(this._efectoID)) {
            TreeMap<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
            copiaRetos.putAll(pelea.getRetos());
            for (Map.Entry entry : copiaRetos.entrySet()) {
                int reto = (Integer)entry.getKey();
                int exitoReto = (Integer)entry.getValue();
                if (exitoReto != 0) continue;
                block2 : switch (reto) {
                    case 21: {
                        if (this._efectoID != 77 && this._efectoID != 169) break;
                        for (Combate.Luchador luch : pelea._inicioLucEquipo2) {
                            if (!objetivos.contains(luch)) continue;
                            SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
                            exitoReto = 2;
                            break block2;
                        }
                        break;
                    }
                    case 22: {
                        if (this._efectoID != 84 && this._efectoID != 101 && this._efectoID != 168) break;
                        for (Combate.Luchador luch : pelea._inicioLucEquipo2) {
                            if (!objetivos.contains(luch)) continue;
                            SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
                            exitoReto = 2;
                            break block2;
                        }
                        break;
                    }
                    case 23: {
                        if (this._efectoID != 116 && this._efectoID != 320) break;
                        for (Combate.Luchador luch : pelea._inicioLucEquipo2) {
                            if (!objetivos.contains(luch)) continue;
                            SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
                            exitoReto = 2;
                            break block2;
                        }
                        break;
                    }
                    case 31: {
                        if (pelea.getIDMobReto() == 0) break;
                        for (Combate.Luchador luch : objetivos) {
                            if (!pelea._inicioLucEquipo2.contains(luch) || luch.getID() == pelea.getIDMobReto()) continue;
                            SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
                            exitoReto = 2;
                            break block2;
                        }
                        break;
                    }
                    case 32: {
                        if (pelea.getIDMobReto() == 0) break;
                        for (Combate.Luchador luch : objetivos) {
                            if (!pelea._inicioLucEquipo2.contains(luch) || luch.getID() == pelea.getIDMobReto()) continue;
                            SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
                            exitoReto = 2;
                            break block2;
                        }
                        break;
                    }
                    case 34: {
                        if ((this._efectoID < 91 || this._efectoID > 100) && (this._efectoID < 85 || this._efectoID > 89) || pelea.getIDMobReto() == 0) break;
                        for (Combate.Luchador luch : objetivos) {
                            if (!pelea._inicioLucEquipo2.contains(luch) || luch.getID() == pelea.getIDMobReto()) continue;
                            SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
                            exitoReto = 2;
                            break block2;
                        }
                        break;
                    }
                    case 43: {
                        if (this._efectoID != 108) break;
                        for (Combate.Luchador luch : objetivos) {
                            if (luch.getID() != this._lanzador.getID()) continue;
                            SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
                            exitoReto = 2;
                            break block2;
                        }
                        break;
                    }
                    case 45: 
                    case 46: {
                        if ((this._efectoID < 91 || this._efectoID > 100) && (this._efectoID < 85 || this._efectoID > 89)) break;
                        for (Combate.Luchador luch : objetivos) {
                            if (!pelea._inicioLucEquipo2.contains(luch)) continue;
                            if (luch.getPjAtacante() == 0) {
                                luch.setPjAtacante(this._lanzador.getID());
                                continue;
                            }
                            if (luch.getPjAtacante() == this._lanzador.getID()) continue;
                            SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
                            exitoReto = 2;
                            break block2;
                        }
                        break;
                    }
                }
                if (exitoReto == 0) continue;
                pelea.getRetos().remove(reto);
                pelea.getRetos().put(reto, exitoReto);
            }
        }
        switch (this._efectoID) {
            case 4: {
                this.aplicarEfecto_4(pelea);
                break;
            }
            case 5: {
                this.aplicarEfecto_5(objetivos, pelea);
                break;
            }
            case 6: {
                this.aplicarEfecto_6(objetivos, pelea);
                break;
            }
            case 8: {
                this.aplicarEfecto_8(objetivos, pelea);
                break;
            }
            case 9: {
                this.aplicarEfecto_9(objetivos, pelea);
                break;
            }
            case 50: {
                this.aplicarEfecto_50(pelea);
                break;
            }
            case 51: {
                this.aplicarEfecto_51(pelea);
                break;
            }
            case 77: {
                this.aplicarEfecto_77(objetivos, pelea);
                break;
            }
            case 78: {
                this.aplicarEfecto_78(objetivos, pelea);
                break;
            }
            case 79: {
                this.aplicarEfecto_79(objetivos, pelea);
                break;
            }
            case 81: {
                this.aplicarEfecto_81(objetivos, pelea);
                break;
            }
            case 82: {
                this.aplicarEfecto_82(objetivos, pelea);
                break;
            }
            case 84: {
                this.aplicarEfecto_84(objetivos, pelea);
                break;
            }
            case 85: {
                this.aplicarEfecto_85(objetivos, pelea);
                break;
            }
            case 86: {
                this.aplicarEfecto_86(objetivos, pelea);
                break;
            }
            case 87: {
                this.aplicarEfecto_87(objetivos, pelea);
                break;
            }
            case 88: {
                this.aplicarEfecto_88(objetivos, pelea);
                break;
            }
            case 89: {
                this.aplicarEfecto_89(objetivos, pelea);
                break;
            }
            case 90: {
                this.aplicarEfecto_90(objetivos, pelea);
                break;
            }
            case 91: {
                this.aplicarEfecto_91(objetivos, pelea, esCaC);
                break;
            }
            case 92: {
                this.aplicarEfecto_92(objetivos, pelea, esCaC);
                break;
            }
            case 93: {
                this.aplicarEfecto_93(objetivos, pelea, esCaC);
                break;
            }
            case 94: {
                this.aplicarEfecto_94(objetivos, pelea, esCaC);
                break;
            }
            case 95: {
                this.aplicarEfecto_95(objetivos, pelea, esCaC);
                break;
            }
            case 96: {
                this.aplicarEfecto_96(objetivos, pelea, esCaC);
                break;
            }
            case 97: {
                this.aplicarEfecto_97(objetivos, pelea, esCaC);
                break;
            }
            case 98: {
                this.aplicarEfecto_98(objetivos, pelea, esCaC);
                break;
            }
            case 99: {
                this.aplicarEfecto_99(objetivos, pelea, esCaC);
                break;
            }
            case 100: {
                this.aplicarEfecto_100(objetivos, pelea, esCaC);
                break;
            }
            case 101: {
                this.aplicarEfecto_101(objetivos, pelea);
                break;
            }
            case 105: {
                this.aplicarEfecto_105(objetivos, pelea);
                break;
            }
            case 106: {
                this.aplicarEfecto_106(objetivos, pelea);
                break;
            }
            case 107: {
                this.aplicarEfecto_107(objetivos, pelea);
                break;
            }
            case 108: {
                this.aplicarEfecto_108(objetivos, pelea, esCaC);
                break;
            }
            case 109: {
                this.aplicarEfecto_109(pelea);
                break;
            }
            case 110: {
                this.aplicarEfecto_110(objetivos, pelea);
                break;
            }
            case 111: {
                this.aplicarEfecto_111(objetivos, pelea);
                break;
            }
            case 112: {
                this.aplicarEfecto_112(objetivos, pelea);
                break;
            }
            case 114: {
                this.aplicarEfecto_114(objetivos, pelea);
                break;
            }
            case 115: {
                this.aplicarEfecto_115(objetivos, pelea);
                break;
            }
            case 116: {
                this.aplicarEfecto_116(objetivos, pelea);
                break;
            }
            case 117: {
                this.aplicarEfecto_117(objetivos, pelea);
                break;
            }
            case 118: {
                this.aplicarEfecto_118(objetivos, pelea);
                break;
            }
            case 119: {
                this.aplicarEfecto_119(objetivos, pelea);
                break;
            }
            case 120: {
                this.aplicarEfecto_120(pelea);
                break;
            }
            case 121: {
                this.aplicarEfecto_121(objetivos, pelea);
                break;
            }
            case 122: {
                this.aplicarEfecto_122(objetivos, pelea);
                break;
            }
            case 123: {
                this.aplicarEfecto_123(objetivos, pelea);
                break;
            }
            case 124: {
                this.aplicarEfecto_124(objetivos, pelea);
                break;
            }
            case 125: {
                this.aplicarEfecto_125(objetivos, pelea);
                break;
            }
            case 126: {
                this.aplicarEfecto_126(objetivos, pelea);
                break;
            }
            case 127: {
                this.aplicarEfecto_127(objetivos, pelea);
                break;
            }
            case 128: {
                this.aplicarEfecto_128(objetivos, pelea);
                break;
            }
            case 130: {
                this.aplicarEfecto_130(objetivos, pelea);
                break;
            }
            case 131: {
                this.aplicarEfecto_131(objetivos, pelea);
                break;
            }
            case 132: {
                this.aplicarEfecto_132(objetivos, pelea);
                break;
            }
            case 138: {
                this.aplicarEfecto_138(objetivos, pelea);
                break;
            }
            case 140: {
                this.aplicarEfecto_140(objetivos, pelea);
                break;
            }
            case 141: {
                this.aplicarEfecto_141(objetivos, pelea);
                break;
            }
            case 142: {
                this.aplicarEfecto_142(objetivos, pelea);
                break;
            }
            case 143: {
                this.aplicarEfecto_143(objetivos, pelea);
                break;
            }
            case 144: {
                this.aplicarEfecto_144(objetivos, pelea);
            }
            case 145: {
                this.aplicarEfecto_145(objetivos, pelea);
                break;
            }
            case 149: {
                this.aplicarEfecto_149(objetivos, pelea);
                break;
            }
            case 150: {
                this.aplicarEfecto_150(objetivos, pelea);
                break;
            }
            case 152: {
                this.aplicarEfecto_152(objetivos, pelea);
                break;
            }
            case 153: {
                this.aplicarEfecto_153(objetivos, pelea);
                break;
            }
            case 154: {
                this.aplicarEfecto_154(objetivos, pelea);
                break;
            }
            case 155: {
                this.aplicarEfecto_155(objetivos, pelea);
                break;
            }
            case 156: {
                this.aplicarEfecto_156(objetivos, pelea);
                break;
            }
            case 157: {
                this.aplicarEfecto_157(objetivos, pelea);
                break;
            }
            case 160: {
                this.aplicarEfecto_160(objetivos, pelea);
                break;
            }
            case 161: {
                this.aplicarEfecto_161(objetivos, pelea);
                break;
            }
            case 162: {
                this.aplicarEfecto_162(objetivos, pelea);
                break;
            }
            case 163: {
                this.aplicarEfecto_163(objetivos, pelea);
                break;
            }
            case 164: {
                this.aplicarEfecto_164(objetivos, pelea);
                break;
            }
            case 165: {
                this.aplicarEfecto_165(objetivos, pelea);
                break;
            }
            case 168: {
                this.aplicarEfecto_168(objetivos, pelea);
                break;
            }
            case 169: {
                this.aplicarEfecto_169(objetivos, pelea);
                break;
            }
            case 171: {
                this.aplicarEfecto_171(objetivos, pelea);
                break;
            }
            case 176: {
                this.aplicarEfecto_176(objetivos, pelea);
                break;
            }
            case 177: {
                this.aplicarEfecto_177(objetivos, pelea);
                break;
            }
            case 178: {
                this.aplicarEfecto_178(objetivos, pelea);
                break;
            }
            case 179: {
                this.aplicarEfecto_179(objetivos, pelea);
                break;
            }
            case 180: {
                this.aplicarEfecto_180(pelea);
                break;
            }
            case 181: {
                this.aplicarEfecto_181(pelea);
                break;
            }
            case 182: {
                this.aplicarEfecto_182(objetivos, pelea);
                break;
            }
            case 183: {
                this.aplicarEfecto_183(objetivos, pelea);
                break;
            }
            case 184: {
                this.aplicarEfecto_184(objetivos, pelea);
                break;
            }
            case 185: {
                this.aplicarEfecto_185(pelea);
                break;
            }
            case 186: {
                this.aplicarEfecto_186(objetivos, pelea);
                break;
            }
            case 202: {
                this.aplicarEfecto_202(objetivos, pelea, celdas);
                break;
            }
            case 210: {
                this.aplicarEfecto_210(objetivos, pelea);
                break;
            }
            case 211: {
                this.aplicarEfecto_211(objetivos, pelea);
                break;
            }
            case 212: {
                this.aplicarEfecto_212(objetivos, pelea);
                break;
            }
            case 213: {
                this.aplicarEfecto_213(objetivos, pelea);
                break;
            }
            case 214: {
                this.aplicarEfecto_214(objetivos, pelea);
                break;
            }
            case 215: {
                this.aplicarEfecto_215(objetivos, pelea);
                break;
            }
            case 216: {
                this.aplicarEfecto_216(objetivos, pelea);
                break;
            }
            case 217: {
                this.aplicarEfecto_217(objetivos, pelea);
                break;
            }
            case 218: {
                this.aplicarEfecto_218(objetivos, pelea);
                break;
            }
            case 219: {
                this.aplicarEfecto_219(objetivos, pelea);
                break;
            }
            case 220: {
                this.aplicarEfecto_220(objetivos, pelea);
                break;
            }
            case 265: {
                this.aplicarEfecto_265(objetivos, pelea);
                break;
            }
            case 266: {
                this.aplicarEfecto_266(objetivos, pelea);
                break;
            }
            case 267: {
                this.aplicarEfecto_267(objetivos, pelea);
                break;
            }
            case 268: {
                this.aplicarEfecto_268(objetivos, pelea);
                break;
            }
            case 269: {
                this.aplicarEfecto_269(objetivos, pelea);
                break;
            }
            case 270: {
                this.aplicarEfecto_270(objetivos, pelea);
                break;
            }
            case 271: {
                this.aplicarEfecto_271(objetivos, pelea);
                break;
            }
            case 275: {
                this.aplicarEfecto_275(objetivos, pelea);
                break;
            }
            case 276: {
                this.aplicarEfecto_276(objetivos, pelea);
                break;
            }
            case 277: {
                this.aplicarEfecto_277(objetivos, pelea);
                break;
            }
            case 278: {
                this.aplicarEfecto_278(objetivos, pelea);
                break;
            }
            case 279: {
                this.aplicarEfecto_279(objetivos, pelea);
                break;
            }
            case 293: {
                this.aplicarEfecto_293(pelea);
                break;
            }
            case 320: {
                this.aplicarEfecto_320(objetivos, pelea);
                break;
            }
            case 400: {
                this.aplicarEfecto_400(pelea);
                break;
            }
            case 401: {
                this.aplicarEfecto_401(pelea);
                break;
            }
            case 402: {
                this.aplicarEfecto_402(pelea);
                break;
            }
            case 606: {
                this.aplicarEfecto_606(objetivos, pelea);
                break;
            }
            case 607: {
                this.aplicarEfecto_607(objetivos, pelea);
                break;
            }
            case 608: {
                this.aplicarEfecto_608(objetivos, pelea);
                break;
            }
            case 609: {
                this.aplicarEfecto_609(objetivos, pelea);
                break;
            }
            case 610: {
                this.aplicarEfecto_610(objetivos, pelea);
                break;
            }
            case 611: {
                this.aplicarEfecto_611(objetivos, pelea);
                break;
            }
            case 666: {
                break;
            }
            case 671: {
                this.aplicarEfecto_671(objetivos, pelea);
                break;
            }
            case 672: {
                this.aplicarEfecto_672(objetivos, pelea);
                break;
            }
            case 750: {
                this.aplicarEfecto_750(objetivos, pelea);
                break;
            }
            case 765: {
                this.aplicarEfecto_765(objetivos, pelea);
                break;
            }
            case 776: {
                this.aplicarEfecto_776(objetivos, pelea);
                break;
            }
            case 780: {
                this.aplicarEfecto_780(pelea);
                break;
            }
            case 782: {
                this.aplicarEfecto_782(objetivos, pelea);
                break;
            }
            case 781: {
                this.aplicarEfecto_781(objetivos, pelea);
                break;
            }
            case 783: {
                this.aplicarEfecto_783(pelea);
                break;
            }
            case 784: {
                this.aplicarEfecto_784(objetivos, pelea);
                break;
            }
            case 786: {
                this.aplicarEfecto_786(objetivos, pelea);
                break;
            }
            case 787: {
                this.aplicarEfecto_787(objetivos, pelea);
                break;
            }
            case 788: {
                this.aplicarEfecto_788(objetivos, pelea);
                break;
            }
            case 950: {
                this.aplicarEfecto_950(objetivos, pelea);
                break;
            }
            case 951: {
                this.aplicarEfecto_951(objetivos, pelea);
                break;
            }
            default: {
                System.out.println("efecto no implantado : " + this._efectoID + " formula: " + this._args);
            }
        }
    }

    private void aplicarEfecto_4(Combate pelea) {
        if (this._turnos > 1) {
            return;
        }
        if (this._celdaObj.esCaminable(true) && !pelea.celdaOcupada(this._celdaObj.getID())) {
            this._lanzador.getCeldaPelea().removerLuchador(this._lanzador);
            this._lanzador.setCeldaPelea(this._celdaObj);
            this._lanzador.getCeldaPelea().addLuchador(this._lanzador);
            ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
            trampas.addAll(pelea.getTrampas());
            for (Combate.Trampa trampa : trampas) {
                short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), this._lanzador.getCeldaPelea().getID());
                if (dist > trampa.getTama\u00f1o()) continue;
                trampa.activaTrampa(this._lanzador);
            }
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + this._celdaObj.getID());
        } else if (this._lanzador.getPersonaje() != null) {
            SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(this._lanzador.getPersonaje(), "La celda a donde se quiere transportar esta ocupada.");
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_5(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (objetivos.size() == 1 && this._hechizoID == 120) {
            if (objetivos.get(0).tieneEstado(6)) {
                return;
            }
            if (!objetivos.get(0).estaMuerto()) {
                this._lanzador.setObjetivoDestZurca(objetivos.get(0));
            }
        }
        Mapa mapaCopia = pelea.getMapaCopia();
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                Mapa.Celda nueva;
                short nuevaCeldaID;
                if (objetivo.estaMuerto() || objetivo.tieneEstado(6)) continue;
                Mapa.Celda celdaLanz = this._celdaObj;
                int da\u00f1oFinal = 0;
                Combate.Luchador afectado = null;
                if (objetivo.getCeldaPelea().getID() == this._celdaObj.getID()) {
                    celdaLanz = this._lanzador.getCeldaPelea();
                }
                if ((nuevaCeldaID = Pathfinding.getCeldaDespEmpujon(mapaCopia, celdaLanz, objetivo.getCeldaPelea(), this._valor, pelea, objetivo)) == 0) continue;
                if (nuevaCeldaID < 0) {
                    float c;
                    int a = -nuevaCeldaID;
                    int coef = Fórmulas.getRandomValor("1d5+8");
                    float b = (float)this._lanzador.getNivel() / 100.0f;
                    if ((double)b < 0.1) {
                        b = 0.1f;
                    }
                    if ((da\u00f1oFinal = (int)((float)coef * (c = b * (float)a))) < 1) {
                        da\u00f1oFinal = 1;
                    }
                    if (da\u00f1oFinal > objetivo.getPDVConBuff()) {
                        da\u00f1oFinal = objetivo.getPDVConBuff();
                    }
                    if (da\u00f1oFinal > 0) {
                        objetivo.restarPDV(da\u00f1oFinal);
                        if (objetivo.getPDVConBuff() <= 0) {
                            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + ",-" + da\u00f1oFinal);
                            pelea.agregarAMuertos(objetivo);
                            continue;
                        }
                    }
                    a = this._valor - a;
                    nuevaCeldaID = Pathfinding.getCeldaDespEmpujon(mapaCopia, celdaLanz, objetivo.getCeldaPelea(), a, pelea, objetivo);
                    char dir = Pathfinding.getDirEntreDosCeldas(celdaLanz.getID(), objetivo.getCeldaPelea().getID(), mapaCopia, true);
                    short celdaSigID = 0;
                    celdaSigID = nuevaCeldaID == 0 ? Pathfinding.getSigIDCeldaMismaDir(objetivo.getCeldaPelea().getID(), dir, mapaCopia, true) : Pathfinding.getSigIDCeldaMismaDir(nuevaCeldaID, dir, mapaCopia, true);
                    Mapa.Celda celdaSig = mapaCopia.getCelda(celdaSigID);
                    if (celdaSig != null) {
                        afectado = celdaSig.getPrimerLuchador();
                    }
                }
                if (nuevaCeldaID != 0 && (nueva = mapaCopia.getCelda(nuevaCeldaID)) != null) {
                    objetivo.getCeldaPelea().getLuchadores().clear();
                    objetivo.setCeldaPelea(nueva);
                    nueva.addLuchador(objetivo);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 5, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + nuevaCeldaID);
                    try {
                        Thread.sleep(500L);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
                if (da\u00f1oFinal > 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + ",-" + da\u00f1oFinal);
                }
                if (afectado != null) {
                    int da\u00f1oFinal2 = da\u00f1oFinal / 2;
                    if (da\u00f1oFinal2 < 1) {
                        da\u00f1oFinal2 = 1;
                    }
                    if (da\u00f1oFinal2 > afectado.getPDVConBuff()) {
                        da\u00f1oFinal2 = afectado.getPDVConBuff();
                    }
                    if (da\u00f1oFinal2 > 0) {
                        afectado.restarPDV(da\u00f1oFinal2);
                        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(afectado.getID()) + ",-" + da\u00f1oFinal2);
                        if (afectado.getPDVConBuff() <= 0) {
                            pelea.agregarAMuertos(afectado);
                        }
                    }
                }
                try {
                    Thread.sleep(300L);
                }
                catch (Exception da\u00f1oFinal2) {
                    // empty catch block
                }
                for (Combate.Trampa trampa : pelea.getTrampas()) {
                    short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), nuevaCeldaID);
                    if (dist > trampa.getTama\u00f1o()) continue;
                    trampa.activaTrampa(objetivo);
                    break;
                }
                try {
                    Thread.sleep(300L);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_6(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            block4: for (Combate.Luchador objetivo : objetivos) {
                short nuevaCeldaID;
                if (objetivo.estaMuerto() || objetivo.tieneEstado(6)) continue;
                Mapa.Celda eCelda = this._celdaObj;
                if (objetivo.getCeldaPelea().getID() == this._celdaObj.getID()) {
                    eCelda = this._lanzador.getCeldaPelea();
                }
                if ((nuevaCeldaID = Pathfinding.getNuevaCeldaDespuesGolpe(pelea.getMapaCopia(), eCelda, objetivo.getCeldaPelea(), -this._valor, pelea, objetivo)) == 0) continue;
                if (nuevaCeldaID < 0) {
                    int a = -(this._valor + nuevaCeldaID);
                    nuevaCeldaID = Pathfinding.getNuevaCeldaDespuesGolpe(pelea.getMapaCopia(), this._lanzador.getCeldaPelea(), objetivo.getCeldaPelea(), a, pelea, objetivo);
                    if (nuevaCeldaID == 0 || pelea.getMapaCopia().getCelda(nuevaCeldaID) == null) continue;
                }
                objetivo.getCeldaPelea().getLuchadores().clear();
                objetivo.setCeldaPelea(pelea.getMapaCopia().getCelda(nuevaCeldaID));
                objetivo.getCeldaPelea().addLuchador(objetivo);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 5, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + nuevaCeldaID);
                try {
                    Thread.sleep(300L);
                }
                catch (Exception a) {
                    // empty catch block
                }
                for (Combate.Trampa trampa : pelea.getTrampas()) {
                    short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), nuevaCeldaID);
                    if (dist > trampa.getTama\u00f1o()) continue;
                    trampa.activaTrampa(objetivo);
                    continue block4;
                }
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_8(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (objetivos.isEmpty()) {
            return;
        }
        Combate.Luchador objetivo = objetivos.get(0);
        if (objetivo == null || objetivo.estaMuerto() || objetivo.tieneEstado(6)) {
            return;
        }
        switch (this._hechizoID) {
            case 438: {
                if (objetivo.getEquipoBin() == this._lanzador.getEquipoBin()) break;
                return;
            }
            case 445: {
                if (objetivo.getEquipoBin() != this._lanzador.getEquipoBin()) break;
                return;
            }
        }
        Mapa.Celda exCeldaObjetivo = objetivo.getCeldaPelea();
        Mapa.Celda exCeldaLanzador = this._lanzador.getCeldaPelea();
        exCeldaObjetivo.getLuchadores().clear();
        exCeldaLanzador.getLuchadores().clear();
        objetivo.setCeldaPelea(exCeldaLanzador);
        this._lanzador.setCeldaPelea(exCeldaObjetivo);
        exCeldaLanzador.addLuchador(objetivo);
        exCeldaObjetivo.addLuchador(this._lanzador);
        ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
        trampas.addAll(pelea.getTrampas());
        for (Combate.Trampa trampa : trampas) {
            short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), objetivo.getCeldaPelea().getID());
            short dist2 = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), this._lanzador.getCeldaPelea().getID());
            if (dist <= trampa.getTama\u00f1o()) {
                trampa.activaTrampa(objetivo);
                continue;
            }
            if (dist2 > trampa.getTama\u00f1o()) continue;
            trampa.activaTrampa(this._lanzador);
        }
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + exCeldaLanzador.getID());
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + exCeldaObjetivo.getID());
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_9(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, this._valor, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_50(Combate pelea) {
        Combate.Luchador objetivo = this._celdaObj.getPrimerLuchador();
        if (objetivo == null) {
            return;
        }
        if (objetivo.estaMuerto()) {
            return;
        }
        objetivo.getCeldaPelea().getLuchadores().clear();
        objetivo.setCeldaPelea(this._lanzador.getCeldaPelea());
        objetivo.setEstado(8, -1);
        this._lanzador.setEstado(3, -1);
        objetivo.setTransportadoPor(this._lanzador);
        this._lanzador.setTransportando(objetivo);
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + "," + 8 + ",1");
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + 3 + ",1");
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 50, String.valueOf(this._lanzador.getID()), "" + objetivo.getID());
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_51(Combate pelea) {
        if (!this._celdaObj.esCaminable(true) || this._celdaObj.getLuchadores().size() > 0) {
            return;
        }
        Combate.Luchador objetivo = this._lanzador.getTransportando();
        if (objetivo == null || objetivo.estaMuerto()) {
            return;
        }
        Mapa.Celda celdaLanz = this._lanzador.getCeldaPelea();
        celdaLanz.removerLuchador(objetivo);
        objetivo.setCeldaPelea(this._celdaObj);
        objetivo.getCeldaPelea().addLuchador(objetivo);
        objetivo.setEstado(8, 0);
        this._lanzador.setEstado(3, 0);
        objetivo.setTransportadoPor(null);
        this._lanzador.setTransportando(null);
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 51, String.valueOf(this._lanzador.getID()), String.valueOf(this._celdaObj.getID()));
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + "," + 8 + ",0");
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + 3 + ",0");
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_77(ArrayList<Combate.Luchador> afectados, Combate pelea) {
        int valor = -1;
        try {
            valor = Integer.parseInt(this._args.split(";")[0]);
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        if (valor == -1) {
            return;
        }
        int num = 0;
        for (Combate.Luchador objetivo : afectados) {
            if (objetivo.estaMuerto()) continue;
            int perdidos = Fórmulas.getPuntosPerdidos('m', valor, this._lanzador, objetivo);
            if (perdidos < valor) {
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 309, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + (valor - perdidos));
            }
            if (perdidos < 1) continue;
            objetivo.addBuff(127, perdidos, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 127, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + ",-" + perdidos + "," + this._turnos);
            num += perdidos;
        }
        if (num != 0) {
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 128, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + num + "," + this._turnos);
            this._lanzador.addBuff(128, num, 1, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            if (this._lanzador.puedeJugar()) {
                this._lanzador.addTempPM(pelea, num);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_78(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            if (objetivo.puedeJugar()) {
                objetivo.addTempPM(pelea, val);
            }
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_79(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos < 1) {
            return;
        }
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, -1, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_82(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                int da\u00f1oFinal = this._valor;
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_84(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int valor = -1;
        try {
            valor = Integer.parseInt(this._args.split(";")[0]);
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        if (valor == -1) {
            return;
        }
        int num = 0;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            int perdidos = Fórmulas.getPuntosPerdidos('m', valor, this._lanzador, objetivo);
            if (perdidos < valor) {
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 308, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + (valor - perdidos));
            }
            if (perdidos < 1) continue;
            if (this._hechizoID == 95 || this._hechizoID == 2079) {
                objetivo.addBuff(101, perdidos, 1, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            } else {
                objetivo.addBuff(101, perdidos, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 101, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + ",-" + perdidos + "," + this._turnos);
            num += perdidos;
        }
        if (num != 0) {
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 111, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + num + "," + this._turnos);
            this._lanzador.addBuff(111, num, 0, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            if (this._lanzador.puedeJugar()) {
                this._lanzador.addTempPA(pelea, num);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_85(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 2, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_86(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 1, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_87(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 4, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_88(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 3, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_89(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._hechizoID == 1679) {
            char[] dir = new char[]{'b', 'd', 'f', 'h'};
            Combate.Luchador victima = objetivos.get(0);
            objetivos.clear();
            for (int i = 0; i < 4; ++i) {
                Combate.Luchador objetivo;
                short idSigCelda = Pathfinding.getSigIDCeldaMismaDir(victima.getCeldaPelea().getID(), dir[i], pelea.getMapaCopia(), true);
                Mapa.Celda sigCelda = pelea.getMapaCopia().getCelda(idSigCelda);
                if (sigCelda == null || (objetivo = sigCelda.getPrimerLuchador()) == null) continue;
                objetivos.add(objetivo);
            }
            try {
                Thread.sleep(500L);
            }
            catch (Exception i) {
                // empty catch block
            }
        }
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 0, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_81(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            String[] jet = this._args.split(";");
            int cura = 0;
            cura = jet.length < 6 ? 1 : Fórmulas.getRandomValor(jet[5]);
            int cura2 = cura;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                cura = this.getMaxMinHechizo(objetivo, cura);
                int pdvMax = objetivo.getPDVMaxConBuff();
                int curaFinal = Fórmulas.calculFinalCura(this._lanzador, cura, false);
                if (curaFinal + objetivo.getPDVConBuff() > pdvMax) {
                    curaFinal = pdvMax - objetivo.getPDVConBuff();
                }
                if (curaFinal < 1) {
                    curaFinal = 0;
                }
                objetivo.restarPDV(-curaFinal);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 108, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + curaFinal);
                cura = cura2;
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_90(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            int porc = Fórmulas.getRandomValor(this._valores);
            int da\u00f1oFinal = (int)((double)porc / 100.0 * (double)this._lanzador.getPDVConBuff());
            if (da\u00f1oFinal > this._lanzador.getPDVConBuff()) {
                da\u00f1oFinal = this._lanzador.getPDVConBuff();
            }
            this._lanzador.restarPDV(da\u00f1oFinal);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + -da\u00f1oFinal);
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (da\u00f1oFinal + objetivo.getPDVConBuff() > objetivo.getPDVMaxConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVMaxConBuff() - objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(-da\u00f1oFinal);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + ",+" + da\u00f1oFinal);
            }
            if (this._lanzador.getPDVConBuff() <= 0) {
                pelea.agregarAMuertos(this._lanzador);
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_91(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 2, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 2, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_92(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 1, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 1, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_93(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 4, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 4, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_94(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 3, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 3, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_95(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 0, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 0, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                int cura = -da\u00f1oFinal / 2;
                if (this._lanzador.getPDVConBuff() + cura > this._lanzador.getPDVMaxConBuff()) {
                    cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                }
                this._lanzador.restarPDV(-cura);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + "," + cura);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_96(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 2, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 2, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_97(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 1, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                int da\u00f1oFinal;
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                if (this._suerte > 0 && this._hechizoID == 108) {
                    da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, this._lanzador, 1, da\u00f1o, false, this._hechizoID, this._veneno);
                    if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, this._lanzador, this._lanzador, pelea, this._hechizoID, this._veneno)) > this._lanzador.getPDVConBuff()) {
                        da\u00f1oFinal = this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(da\u00f1oFinal);
                    da\u00f1oFinal = -da\u00f1oFinal;
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + da\u00f1oFinal);
                    if (this._lanzador.getPDVConBuff() > 0) continue;
                    pelea.agregarAMuertos(this._lanzador);
                    continue;
                }
                da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 1, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_98(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 4, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 4, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
            if (this._hechizoID == 233 || this._hechizoID == 2006) {
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException objetivo) {}
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_99(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 3, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 3, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_100(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 0, da\u00f1o, true, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else if (this._turnos <= 0) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(this._turnosOriginales);
            }
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                for (EfectoHechizo EH : this._lanzador.getBuffsPorEfectoID(293)) {
                    if (EH.getValor() != this._hechizoID) continue;
                    int add = -1;
                    try {
                        add = Integer.parseInt(EH.getArgs().split(";")[2]);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    if (add <= 0) continue;
                    da\u00f1o += add;
                }
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 0, da\u00f1o, false, this._hechizoID, this._veneno);
                if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                    da\u00f1oFinal = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1oFinal);
                int cura = da\u00f1oFinal;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1oFinal = -da\u00f1oFinal;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_101(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                int perdidos = Fórmulas.getPuntosPerdidos('a', this._valor, this._lanzador, objetivo);
                if (this._valor - perdidos > 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 308, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + (this._valor - perdidos));
                }
                if (perdidos <= 0) continue;
                objetivo.addBuff(101, perdidos, 1, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                if (this._turnos > 1 && this._duracion > 1) continue;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 101, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",-" + perdidos);
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                int perdidos = Fórmulas.getPuntosPerdidos('a', this._valor, this._lanzador, objetivo);
                if (this._valor - perdidos > 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 308, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + (this._valor - perdidos));
                }
                if (perdidos <= 0) continue;
                if (this._hechizoID == 89) {
                    objetivo.addBuff(this._efectoID, perdidos, 0, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                } else {
                    objetivo.addBuff(this._efectoID, perdidos, 1, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                }
                if (this._turnos > 1 && this._duracion > 1) continue;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 101, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",-" + perdidos);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_105(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, false, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_106(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = -1;
        try {
            val = Integer.parseInt(this._args.split(";")[1]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (val == -1) {
            return;
        }
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, false, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_107(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos < 1) {
            return;
        }
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_108(ArrayList<Combate.Luchador> objetivos, Combate pelea, boolean esCaC) {
        if (esCaC) {
            if (this._lanzador.esInvisible()) {
                this._lanzador.hacerseVisible(-1);
            }
            String[] jet = this._args.split(";");
            int cura = 0;
            cura = jet.length < 6 ? 1 : Fórmulas.getRandomValor(jet[5]);
            int cura2 = cura;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                cura = this.getMaxMinHechizo(objetivo, cura);
                int pdvMax = objetivo.getPDVMaxConBuff();
                int curaFinal = Fórmulas.calculFinalCura(this._lanzador, cura, esCaC);
                if (curaFinal + objetivo.getPDVConBuff() > pdvMax) {
                    curaFinal = pdvMax - objetivo.getPDVConBuff();
                }
                if (curaFinal < 1) {
                    curaFinal = 0;
                }
                objetivo.restarPDV(-curaFinal);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 108, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + curaFinal);
                cura = cura2;
            }
        } else if (this._turnos <= 0) {
            String[] jet = this._args.split(";");
            int cura = 0;
            cura = jet.length < 6 ? 1 : Fórmulas.getRandomValor(jet[5]);
            int cura2 = cura;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                cura = this.getMaxMinHechizo(objetivo, cura);
                int pdvMax = objetivo.getPDVMaxConBuff();
                int curaFinal = Fórmulas.calculFinalCura(this._lanzador, cura, esCaC);
                if (curaFinal + objetivo.getPDVConBuff() > pdvMax) {
                    curaFinal = pdvMax - objetivo.getPDVConBuff();
                }
                if (curaFinal < 1) {
                    curaFinal = 0;
                }
                objetivo.restarPDV(-curaFinal);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 108, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + curaFinal);
                cura = cura2;
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_109(Combate pelea) {
        if (this._turnos <= 0) {
            int da\u00f1o = Fórmulas.getRandomValor(this._valores);
            int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, this._lanzador, -1, da\u00f1o, false, this._hechizoID, this._veneno);
            if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, this._lanzador, this._lanzador, pelea, this._hechizoID, this._veneno)) > this._lanzador.getPDVConBuff()) {
                da\u00f1oFinal = this._lanzador.getPDVConBuff();
            }
            this._lanzador.restarPDV(da\u00f1oFinal);
            da\u00f1oFinal = -da\u00f1oFinal;
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + da\u00f1oFinal);
            if (this._lanzador.getPDVConBuff() <= 0) {
                pelea.agregarAMuertos(this._lanzador);
            }
        } else {
            this._lanzador.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_110(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_111(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            if (objetivo.puedeJugar()) {
                objetivo.addTempPA(pelea, val);
            }
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_112(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_114(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_115(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_116(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            if (objetivo.puedeJugar() && objetivo == this._lanzador) {
                objetivo.getTotalStatsConBuff().addStat(116, (short)val);
            }
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_117(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            if (objetivo.puedeJugar() && objetivo == this._lanzador) {
                objetivo.getTotalStatsConBuff().addStat(117, (short)val);
            }
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_118(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_119(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_120(Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        this._lanzador.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        this._lanzador.addTempPA(pelea, val);
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + val + "," + this._turnos);
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_121(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_122(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_123(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_124(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_125(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        if (this._hechizoID == 441) {
            return;
        }
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_126(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_127(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                int perdidos = Fórmulas.getPuntosPerdidos('m', this._valor, this._lanzador, objetivo);
                if (this._valor - perdidos > 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 309, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + (this._valor - perdidos));
                }
                if (perdidos <= 0) continue;
                objetivo.addBuff(127, perdidos, 1, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                if (this._turnos > 1 && this._duracion > 1) continue;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 127, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",-" + perdidos);
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                int perdidos = Fórmulas.getPuntosPerdidos('m', this._valor, this._lanzador, objetivo);
                if (this._valor - perdidos > 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 309, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + (this._valor - perdidos));
                }
                if (perdidos <= 0) continue;
                if (this._hechizoID == 136) {
                    objetivo.addBuff(this._efectoID, perdidos, this._turnos, this._turnos, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                } else {
                    objetivo.addBuff(this._efectoID, perdidos, 1, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                }
                if (this._turnos > 1 && this._duracion > 1) continue;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 127, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",-" + perdidos);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_128(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            if (objetivo.puedeJugar()) {
                objetivo.addTempPM(pelea, val);
            }
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_130(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (pelea.getTipoPelea() == 0) {
            return;
        }
        int val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            Personaje perso = objetivo.getPersonaje();
            if (objetivo.estaMuerto() || perso == null) continue;
            if ((long)val > perso.getKamas()) {
                val = (int)perso.getKamas();
            }
            if (val == 0) continue;
            perso.setKamas(perso.getKamas() - (long)val);
            Personaje perso2 = this._lanzador.getPersonaje();
            if (perso2 != null) {
                perso2.setKamas(perso2.getKamas() + (long)val);
            }
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 130, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_131(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, this._valor, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_132(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.desbuffear();
            if (objetivo.puedeJugar() && objetivo == this._lanzador) {
                Personaje.Stats s1 = objetivo.getTotalStatsConBuff();
                Personaje.Stats s2 = objetivo.getTotalStatsSinBuff();
                for (int a = 0; a < 1000; ++a) {
                    if (s1._statsEfecto.get(a) == null || s2._statsEfecto.get(a) == null) continue;
                    int nuevo = s2._statsEfecto.get(a);
                    s1.especificarStat(a, nuevo);
                }
            }
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 132, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()));
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_138(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_140(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(300L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_141(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                this.aplicarEfecto_765B(pelea, objetivo);
                objetivo = objetivo.getBuff(765).getLanzador();
            }
            pelea.agregarAMuertos(objetivo);
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    private void aplicarEfecto_143(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            String[] jet = this._args.split(";");
            int cura = 0;
            cura = jet.length < 6 ? 1 : Fórmulas.getRandomValor(jet[5]);
            int dmg2 = cura;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                int curaFinal = Fórmulas.calculFinalCura(this._lanzador, cura = this.getMaxMinHechizo(objetivo, cura), false);
                if (curaFinal + objetivo.getPDVConBuff() > objetivo.getPDVMaxConBuff()) {
                    curaFinal = objetivo.getPDVMaxConBuff() - objetivo.getPDVConBuff();
                }
                if (curaFinal < 1) {
                    curaFinal = 0;
                }
                objetivo.restarPDV(-curaFinal);
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 108, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + curaFinal);
                cura = dmg2;
            }
        } else {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_142(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_144(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(145, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 145, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_145(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_149(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int id = -1;
        try {
            id = Integer.parseInt(this._args.split(";")[2]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            if (id == -1) {
                id = objetivo.getGfxDefecto();
            }
            objetivo.addBuff(this._efectoID, id, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            int defecto = objetivo.getGfxDefecto();
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + defecto + "," + id + "," + (objetivo.puedeJugar() ? this._turnos + 1 : this._turnos));
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_150(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos == 0) {
            return;
        }
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 150, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + ",4");
            objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_152(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_153(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_154(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_155(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_156(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_157(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_160(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_161(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_162(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_163(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_164(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = this._valor;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, false, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_165(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int valor = -1;
        try {
            valor = Integer.parseInt(this._args.split(";")[1]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (valor == -1) {
            return;
        }
        int val2 = valor;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            valor = this.getMaxMinHechizo(objetivo, valor);
            objetivo.addBuff(this._efectoID, valor, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            valor = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_168(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, this._valor, 1, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                if (this._turnos > 1 && this._duracion > 1) continue;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 168, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",-" + this._valor);
            }
        } else {
            boolean repetibles = false;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (this._hechizoID == 197 || this._hechizoID == 112) {
                    objetivo.addBuff(this._efectoID, this._valor, this._turnos, this._turnos, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                } else if (this._hechizoID == 115) {
                    if (!repetibles) {
                        short perdidosPA = (short)Fórmulas.getRandomValor(this._valores);
                        if (perdidosPA == -1) continue;
                        this._valor = perdidosPA;
                    }
                    objetivo.addBuff(this._efectoID, this._valor, this._turnos, this._turnos, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                    repetibles = true;
                } else {
                    objetivo.addBuff(this._efectoID, this._valor, 1, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                }
                if (this._turnos > 1 && this._duracion > 1) continue;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 168, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",-" + this._valor);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_169(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, this._valor, 1, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                if (this._turnos > 1 && this._duracion > 1) continue;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 169, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",-" + this._valor);
            }
        } else {
            if (!objetivos.isEmpty() && this._hechizoID == 120 && this._lanzador.getObjetivoDestZurca() != null) {
                this._lanzador.getObjetivoDestZurca().addBuff(this._efectoID, this._valor, this._turnos, this._turnos, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                if (this._turnos <= 1 || this._duracion <= 1) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 169, String.valueOf(this._lanzador.getObjetivoDestZurca().getID()), String.valueOf(this._lanzador.getObjetivoDestZurca().getID()) + ",-" + this._valor);
                }
            }
            for (Combate.Luchador objetivo : objetivos) {
                boolean repetibles = false;
                if (objetivo.estaMuerto()) continue;
                if (this._hechizoID == 192) {
                    objetivo.addBuff(this._efectoID, this._valor, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                } else if (this._hechizoID == 115) {
                    if (!repetibles) {
                        short perdidosPM = (short)Fórmulas.getRandomValor(this._valores);
                        if (perdidosPM == -1) continue;
                        this._valor = perdidosPM;
                    }
                    objetivo.addBuff(this._efectoID, this._valor, this._turnos, this._turnos, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                    repetibles = true;
                } else if (this._hechizoID == 197) {
                    objetivo.addBuff(this._efectoID, this._valor, this._turnos, this._turnos, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                } else {
                    objetivo.addBuff(this._efectoID, this._valor, 1, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
                }
                if (this._turnos > 1 && this._duracion > 1) continue;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 169, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",-" + this._valor);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_171(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_176(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 176, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_177(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 177, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_178(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_179(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_180(Combate pelea) {
        if (this._celdaObj.getPrimerLuchador() != null) {
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 151, String.valueOf(this._lanzador.getID()), String.valueOf(this._hechizoID));
            return;
        }
        int idInvocacion = pelea.getSigIDLuchador();
        Personaje clon = Personaje.personajeClonado(this._lanzador.getPersonaje(), idInvocacion);
        Combate.Luchador doble = new Combate.Luchador(pelea, clon);
        doble.setEquipoBin(this._lanzador.getEquipoBin());
        doble.setInvocador(this._lanzador);
        this._celdaObj.addLuchador(doble);
        doble.setCeldaPelea(this._celdaObj);
        pelea.getOrdenJug().add(pelea.getOrdenJug().indexOf(this._lanzador) + 1, doble);
        pelea.addLuchadorEnEquipo(doble, this._lanzador.getEquipoBin());
        String gm = "+" + doble.stringGM();
        String gtl = pelea.stringOrdenJugadores();
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 180, String.valueOf(this._lanzador.getID()), gm);
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(this._lanzador.getID()), gtl);
        ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
        trampas.addAll(pelea.getTrampas());
        for (Combate.Trampa trampa : trampas) {
            short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), doble.getCeldaPelea().getID());
            if (dist > trampa.getTama\u00f1o()) continue;
            trampa.activaTrampa(doble);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_181(Combate pelea) {
        if (this._celdaObj.getPrimerLuchador() != null) {
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 151, String.valueOf(this._lanzador.getID()), String.valueOf(this._hechizoID));
            return;
        }
        int mobID = -1;
        int mobNivel = -1;
        try {
            mobID = Integer.parseInt(this._args.split(";")[0]);
            mobNivel = Integer.parseInt(this._args.split(";")[1]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        MobModelo.MobGrado mob = null;
        try {
            mob = Mundo.getMobModelo(mobID).getGradoPorNivel(mobNivel).copiarMob();
        }
        catch (Exception e1) {
            System.out.println("El Mob ID esta mal configurado: " + mobID);
            return;
        }
        if (mobID == -1 || mobNivel == -1 || mob == null) {
            return;
        }
        int idInvocacion = pelea.getSigIDLuchador() - pelea.getNumeroInvos();
        mob.setIdEnPelea(idInvocacion);
        mob.modificarStatsPorInvocador(this._lanzador);
        Combate.Luchador invocacion = new Combate.Luchador(pelea, mob);
        invocacion.setEquipoBin(this._lanzador.getEquipoBin());
        invocacion.setInvocador(this._lanzador);
        this._celdaObj.addLuchador(invocacion);
        invocacion.setCeldaPelea(this._celdaObj);
        pelea.getOrdenJug().add(pelea.getOrdenJug().indexOf(this._lanzador) + 1, invocacion);
        pelea.addLuchadorEnEquipo(invocacion, this._lanzador.getEquipoBin());
        String gm = "+" + invocacion.stringGM();
        String gtl = pelea.stringOrdenJugadores();
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 181, String.valueOf(this._lanzador.getID()), gm);
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(this._lanzador.getID()), gtl);
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        this._lanzador.aumentarInvocaciones();
        ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
        trampas.addAll(pelea.getTrampas());
        for (Combate.Trampa trampa : trampas) {
            short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), invocacion.getCeldaPelea().getID());
            if (dist > trampa.getTama\u00f1o()) continue;
            trampa.activaTrampa(invocacion);
        }
        try {
            Thread.sleep(300L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_182(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_183(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, false, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_184(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, false, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_185(Combate pelea) {
        short celdaID = this._celdaObj.getID();
        int mobID = -1;
        int nivel = -1;
        try {
            mobID = Integer.parseInt(this._args.split(";")[0]);
            nivel = Integer.parseInt(this._args.split(";")[1]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        MobModelo.MobGrado MG = null;
        try {
            MG = Mundo.getMobModelo(mobID).getGradoPorNivel(nivel).copiarMob();
        }
        catch (Exception e1) {
            System.out.println("El Mob ID esta mal configurado: " + mobID);
            return;
        }
        if (mobID == -1 || nivel == -1 || MG == null) {
            return;
        }
        int idInvocacion = pelea.getSigIDLuchador() - pelea.getNumeroInvos();
        MG.setIdEnPelea(idInvocacion);
        MG.modificarStatsPorInvocador(this._lanzador);
        Combate.Luchador invocacion = new Combate.Luchador(pelea, MG);
        int equipoLanz = this._lanzador.getEquipoBin();
        invocacion.setEquipoBin(equipoLanz);
        invocacion.setInvocador(this._lanzador);
        Mapa.Celda nuevaCelda = pelea.getMapaCopia().getCelda(celdaID);
        nuevaCelda.addLuchador(invocacion);
        invocacion.setCeldaPelea(nuevaCelda);
        pelea.addLuchadorEnEquipo(invocacion, equipoLanz);
        String gm = "+" + invocacion.stringGM();
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 185, String.valueOf(this._lanzador.getID()), gm);
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_186(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_202(ArrayList<Combate.Luchador> objetivos, Combate pelea, ArrayList<Mapa.Celda> celdas) {
        int equipo = this._lanzador.getParamEquipoAliado();
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto() || !objetivo.esInvisible()) continue;
            objetivo.aparecer(this._lanzador);
        }
        for (Combate.Trampa trampa : pelea.getTrampas()) {
            if (trampa.getParamEquipoDue\u00f1o() == equipo) continue;
            for (Mapa.Celda celda : celdas) {
                if (celda.getID() != trampa.getCelda().getID()) continue;
                trampa.esVisibleParaEnemigo();
                trampa.aparecer(equipo);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_210(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_211(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_212(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_213(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_214(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_215(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_216(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_217(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_218(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_219(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_220(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos < 1) {
            return;
        }
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_265(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_266(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        int vol = 0;
        int val2 = val;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(152, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 152, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            vol += val;
            val = val2;
        }
        if (vol != 0) {
            this._lanzador.addBuff(123, vol, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 123, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + vol + "," + this._turnos);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_267(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        int vol = 0;
        int val2 = val;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(153, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 153, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            vol += val;
            val = val2;
        }
        if (vol == 0) {
            return;
        }
        this._lanzador.addBuff(125, vol, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 125, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + vol + "," + this._turnos);
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_268(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        int vol = 0;
        int val2 = val;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(154, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 154, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            vol += val;
            val = val2;
        }
        if (vol != 0) {
            this._lanzador.addBuff(119, vol, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 119, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + vol + "," + this._turnos);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_269(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        int vol = 0;
        int val2 = val;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(155, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 155, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            vol += val;
            val = val2;
        }
        if (vol == 0) {
            this._lanzador.addBuff(126, vol, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 126, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + vol + "," + this._turnos);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_270(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        int vol = 0;
        int val2 = val;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(156, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 156, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            vol += val;
            val = val2;
        }
        if (vol == 0) {
            this._lanzador.addBuff(124, vol, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 124, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + vol + "," + this._turnos);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_271(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        int vol = 0;
        int val2 = val;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(157, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 157, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            vol += val;
            val = val2;
        }
        if (vol == 0) {
            this._lanzador.addBuff(118, vol, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 118, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + vol + "," + this._turnos);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_275(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 2, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_276(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 1, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_277(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 4, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_278(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 3, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_279(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos <= 0) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo && this._hechizoID != 0) {
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                    objetivo = this._lanzador;
                }
                if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                    this.aplicarEfecto_765B(pelea, objetivo);
                    objetivo = objetivo.getBuff(765).getLanzador();
                }
                int da\u00f1o = Fórmulas.getRandomValor(this._valores);
                da\u00f1o = this.getMaxMinHechizo(objetivo, da\u00f1o);
                da\u00f1o = (int)((double)da\u00f1o / 100.0 * (double)this._lanzador.getPDVConBuff());
                int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, this._lanzador, objetivo, 0, da\u00f1o, false, this._hechizoID, this._veneno);
                if (da\u00f1o < 0) {
                    da\u00f1o = 0;
                }
                da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno);
                if (da\u00f1o > objetivo.getPDVConBuff()) {
                    da\u00f1o = objetivo.getPDVConBuff();
                }
                objetivo.restarPDV(da\u00f1o);
                int cura = da\u00f1o;
                if (objetivo.tieneBuff(786)) {
                    if (cura + this._lanzador.getPDVConBuff() > this._lanzador.getPDVMaxConBuff()) {
                        cura = this._lanzador.getPDVMaxConBuff() - this._lanzador.getPDVConBuff();
                    }
                    this._lanzador.restarPDV(-cura);
                    SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()), String.valueOf(this._lanzador.getID()) + ",+" + cura);
                }
                da\u00f1o = -da\u00f1o;
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1o);
                if (objetivo.getPDVConBuff() > 0) continue;
                pelea.agregarAMuertos(objetivo);
            }
        } else {
            this._veneno = true;
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(this._efectoID, 0, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_293(Combate pelea) {
        this._lanzador.addBuff(this._efectoID, this._valor, this._turnos, 1, false, this._hechizoID, this._args, this._lanzador, this._veneno);
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_320(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int valor = 1;
        try {
            valor = Integer.parseInt(this._args.split(";")[0]);
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        int num = 0;
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(116, valor, this._turnos, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 116, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + valor + "," + this._turnos);
            num = (short)(num + valor);
        }
        if (num != 0) {
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 117, String.valueOf(this._lanzador.getID()), String.valueOf(this._lanzador.getID()) + "," + num + "," + this._turnos);
            this._lanzador.addBuff(117, num, 1, 0, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            if (this._lanzador.puedeJugar()) {
                this._lanzador.getTotalStatsConBuff().addStat(117, num);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_400(Combate pelea) {
        if (!this._celdaObj.esCaminable(true)) {
            return;
        }
        if (this._celdaObj.getPrimerLuchador() != null) {
            return;
        }
        for (Combate.Trampa trampa : pelea.getTrampas()) {
            if (trampa.getCelda().getID() != this._celdaObj.getID()) continue;
            return;
        }
        String[] infos = this._args.split(";");
        short hechizoTrampaID = Short.parseShort(infos[0]);
        byte nivel = Byte.parseByte(infos[1]);
        String po = Mundo.getHechizo(this._hechizoID).getStatsPorNivel(this._nivelHechizo).getAfectados();
        byte tama\u00f1o = (byte)CryptManager.getNumeroPorValorHash(po.charAt(1));
        Hechizo.StatsHechizos ST = Mundo.getHechizo(hechizoTrampaID).getStatsPorNivel(nivel);
        Combate.Trampa trampa = new Combate.Trampa(pelea, this._lanzador, this._celdaObj, tama\u00f1o, ST, this._hechizoID);
        pelea.getTrampas().add(trampa);
        int color = trampa.getColor();
        int equipo = this._lanzador.getEquipoBin() + 1;
        String str = "GDZ+" + this._celdaObj.getID() + ";" + tama\u00f1o + ";" + color;
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, equipo, 999, String.valueOf(this._lanzador.getID()), str);
        str = "GDC" + this._celdaObj.getID() + ";Haaaaaaaaz3005;";
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, equipo, 999, String.valueOf(this._lanzador.getID()), str);
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_401(Combate pelea) {
        if (!this._celdaObj.esCaminable(true)) {
            return;
        }
        if (this._celdaObj.getPrimerLuchador() != null) {
            return;
        }
        String[] infos = this._args.split(";");
        short hechizoGlifoID = Short.parseShort(infos[0]);
        byte nivel = Byte.parseByte(infos[1]);
        byte duracion = Byte.parseByte(infos[3]);
        String po = Mundo.getHechizo(this._hechizoID).getStatsPorNivel(this._nivelHechizo).getAfectados();
        byte tama\u00f1o = (byte)CryptManager.getNumeroPorValorHash(po.charAt(1));
        Hechizo.StatsHechizos ST = Mundo.getHechizo(hechizoGlifoID).getStatsPorNivel(nivel);
        Combate.Glifo glifo = new Combate.Glifo(pelea, this._lanzador, this._celdaObj, tama\u00f1o, ST, duracion, this._hechizoID);
        pelea.getGlifos().add(glifo);
        int color = glifo.getColor();
        String str = "GDZ+" + this._celdaObj.getID() + ";" + tama\u00f1o + ";" + color;
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(this._lanzador.getID()), str);
        str = "GDC" + this._celdaObj.getID() + ";Haaaaaaaaa3005;";
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(this._lanzador.getID()), str);
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_402(Combate pelea) {
        if (!this._celdaObj.esCaminable(true)) {
            return;
        }
        String[] infos = this._args.split(";");
        short hechizoGlifoID = Short.parseShort(infos[0]);
        byte nivel = Byte.parseByte(infos[1]);
        byte duracion = Byte.parseByte(infos[3]);
        String po = Mundo.getHechizo(this._hechizoID).getStatsPorNivel(this._nivelHechizo).getAfectados();
        byte tama\u00f1o = (byte)CryptManager.getNumeroPorValorHash(po.charAt(1));
        Hechizo.StatsHechizos ST = Mundo.getHechizo(hechizoGlifoID).getStatsPorNivel(nivel);
        Combate.Glifo glifo = new Combate.Glifo(pelea, this._lanzador, this._celdaObj, tama\u00f1o, ST, duracion, this._hechizoID);
        pelea.getGlifos().add(glifo);
        int color = glifo.getColor();
        String str = "GDZ+" + this._celdaObj.getID() + ";" + tama\u00f1o + ";" + color;
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(this._lanzador.getID()), str);
        str = "GDC" + this._celdaObj.getID() + ";Haaaaaaaaa3005;";
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(this._lanzador.getID()), str);
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_606(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(124, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 124, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_607(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(118, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 118, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_608(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(123, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 123, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_609(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(119, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 119, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_610(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(125, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 125, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_611(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val;
        int val2 = val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            val = this.getMaxMinHechizo(objetivo, val);
            objetivo.addBuff(126, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 126, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
            val = val2;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_671(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        float val = (float)Fórmulas.getRandomValor(this._valores) / 100.0f;
        int pdvMax = this._lanzador.getPDVMaxConBuff();
        int pdvMedio = pdvMax / 2;
        float porc = 1.0f - (float)Math.abs(this._lanzador.getPDVConBuff() - pdvMedio) / (float)pdvMedio;
        int da\u00f1o = (int)(porc * val * (float)pdvMax);
        for (Combate.Luchador objetivo : objetivos) {
            int da\u00f1oFinal;
            int reduc;
            if (objetivo.estaMuerto()) continue;
            if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo) {
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                objetivo = this._lanzador;
            }
            if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                this.aplicarEfecto_765B(pelea, objetivo);
                objetivo = objetivo.getBuff(765).getLanzador();
            }
            if (objetivo.tieneBuff(105)) {
                da\u00f1o -= objetivo.getBuff(105).getValor();
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 105, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + objetivo.getBuff(105).getValor());
            }
            Personaje.Stats totalObjetivo = objetivo.getTotalStatsConBuff();
            int resMasT = totalObjetivo.getEfecto(241);
            int resPorcT = totalObjetivo.getEfecto(214);
            da\u00f1o -= resMasT;
            if ((da\u00f1o -= (reduc = (int)((float)da\u00f1o * (float)resPorcT / 100.0f))) < 1) {
                da\u00f1o = 1;
            }
            if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1o, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                da\u00f1oFinal = objetivo.getPDVConBuff();
            }
            objetivo.restarPDV(da\u00f1oFinal);
            da\u00f1oFinal = -da\u00f1oFinal;
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
            if (objetivo.getPDVConBuff() > 0) continue;
            pelea.agregarAMuertos(objetivo);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_672(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        float val = (float)Fórmulas.getRandomValor(this._valores) / 100.0f;
        int pdvMax = this._lanzador.getPDVMaxConBuff();
        int pdvMedio = pdvMax / 2;
        float porc = 1.0f - (float)Math.abs(this._lanzador.getPDVConBuff() - pdvMedio) / (float)pdvMedio;
        int da\u00f1o = (int)((double)(porc * val * (float)pdvMax) * 0.95);
        for (Combate.Luchador objetivo : objetivos) {
            int da\u00f1oFinal;
            int reduc;
            if (objetivo.estaMuerto()) continue;
            if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo) {
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + ",1");
                objetivo = this._lanzador;
            }
            if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null && !objetivo.getBuff(765).getLanzador().estaMuerto()) {
                this.aplicarEfecto_765B(pelea, objetivo);
                objetivo = objetivo.getBuff(765).getLanzador();
            }
            if (objetivo.tieneBuff(105)) {
                da\u00f1o -= objetivo.getBuff(105).getValor();
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 105, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + objetivo.getBuff(105).getValor());
            }
            Personaje.Stats totalObjetivo = objetivo.getTotalStatsConBuff();
            int resMasT = totalObjetivo.getEfecto(241);
            int resPorcT = totalObjetivo.getEfecto(214);
            da\u00f1o -= resMasT;
            if ((da\u00f1o -= (reduc = (int)((float)da\u00f1o * (float)resPorcT / 100.0f))) < 1) {
                da\u00f1o = 1;
            }
            if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1o, objetivo, this._lanzador, pelea, this._hechizoID, this._veneno)) > objetivo.getPDVConBuff()) {
                da\u00f1oFinal = objetivo.getPDVConBuff();
            }
            objetivo.restarPDV(da\u00f1oFinal);
            da\u00f1oFinal = -da\u00f1oFinal;
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
            if (objetivo.getPDVConBuff() > 0) continue;
            pelea.agregarAMuertos(objetivo);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_750(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
    }

    private void aplicarEfecto_765(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, 0, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_765B(Combate pelea, Combate.Luchador objetivo) {
        Combate.Luchador sacrificado = objetivo.getBuff(765).getLanzador();
        Mapa.Celda cSacrificado = sacrificado.getCeldaPelea();
        Mapa.Celda cObjetivo = objetivo.getCeldaPelea();
        cSacrificado.getLuchadores().clear();
        cObjetivo.getLuchadores().clear();
        sacrificado.setCeldaPelea(cObjetivo);
        cObjetivo.addLuchador(sacrificado);
        objetivo.setCeldaPelea(cSacrificado);
        cSacrificado.addLuchador(objetivo);
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(objetivo.getID()), String.valueOf(objetivo.getID()) + "," + cSacrificado.getID());
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(sacrificado.getID()), String.valueOf(sacrificado.getID()) + "," + cObjetivo.getID());
        try {
            Thread.sleep(300L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_776(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int val = Fórmulas.getRandomValor(this._valores);
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, val, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, this._efectoID, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + val + "," + this._turnos);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_780(Combate pelea) {
        Map<Integer, Combate.Luchador> muertos = pelea.getListaMuertos();
        Combate.Luchador objetivo = null;
        for (Map.Entry<Integer, Combate.Luchador> entry : muertos.entrySet()) {
            Combate.Luchador muerto = entry.getValue();
            if (muerto.estaRetirado() || muerto.getEquipoBin() != this._lanzador.getEquipoBin() || muerto.esInvocacion() && muerto.getInvocador().estaMuerto()) continue;
            objetivo = muerto;
        }
        if (objetivo == null) {
            return;
        }
        objetivo.setEstaMuerto(false);
        objetivo.setCeldaPelea(this._celdaObj);
        objetivo.getCeldaPelea().addLuchador(objetivo);
        objetivo.getBuffPelea().clear();
        int vida = (100 - this._valor) * objetivo.getPDVMaxConBuff() / 100;
        if (!objetivo.esInvocacion()) {
            SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(objetivo.getPersonaje(), vida);
        } else {
            pelea.getOrdenJug().add(pelea.getOrdenJug().indexOf(this._lanzador) + 1, objetivo);
        }
        objetivo.restarPDV(-vida);
        pelea.addLuchadorEnEquipo(objetivo, this._lanzador.getEquipoBin());
        String gm = "+" + objetivo.stringGM();
        String gtl = pelea.stringOrdenJugadores();
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 780, String.valueOf(objetivo.getID()), gm);
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(objetivo.getID()), gtl);
        if (!objetivo.esInvocacion()) {
            SocketManager.ENVIAR_As_STATS_DEL_PJ(objetivo.getPersonaje());
        }
        objetivo.setInvocador(this._lanzador);
        pelea.borrarUnMuerto(objetivo);
        this._lanzador.aumentarInvocaciones();
        ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
        trampas.addAll(pelea.getTrampas());
        for (Combate.Trampa trampa : trampas) {
            short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), objetivo.getCeldaPelea().getID());
            if (dist > trampa.getTama\u00f1o()) continue;
            trampa.activaTrampa(objetivo);
        }
        try {
            Thread.sleep(500L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_781(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, this._valor, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_782(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, this._valor, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_783(Combate pelea) {
        Mapa.Celda c1celda;
        Combate.Luchador objetivo;
        block13: {
            Mapa.Celda celdaLanzador = this._lanzador.getCeldaPelea();
            char d = Pathfinding.getDirEntreDosCeldas(celdaLanzador.getID(), this._celdaObj.getID(), pelea.getMapaCopia(), true);
            short idSigCelda = Pathfinding.getSigIDCeldaMismaDir(celdaLanzador.getID(), d, pelea.getMapaCopia(), true);
            Mapa.Celda sigCelda = pelea.getMapaCopia().getCelda(idSigCelda);
            if (sigCelda == null) {
                return;
            }
            if (sigCelda.getLuchadores().isEmpty()) {
                return;
            }
            objetivo = sigCelda.getPrimerLuchador();
            if (objetivo.tieneEstado(6)) {
                return;
            }
            short c1 = idSigCelda;
            short c2 = 0;
            int limite = 0;
            c1celda = pelea.getMapaCopia().getCelda(c1);
            Mapa.Celda case2 = null;
            ArrayList<Mapa.Celda> trampas = new ArrayList<Mapa.Celda>();
            for (Combate.Trampa trampa : pelea.getTrampas()) {
                trampas.add(trampa.getCelda());
            }
            do {
                if (Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true) == -1) {
                    return;
                }
                c2 = Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true);
                case2 = pelea.getMapaCopia().getCelda(c2);
                if (!case2.esCaminable(true) || pelea.celdaOcupada(c2)) break block13;
                if (Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true) == this._celdaObj.getID()) {
                    c1 = Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true);
                    c1celda = pelea.getMapaCopia().getCelda(c1);
                    break block13;
                }
                c1 = Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true);
                c1celda = pelea.getMapaCopia().getCelda(c1);
                if (c1celda == null) {
                    return;
                }
                if (trampas.contains(c1celda)) break block13;
            } while (++limite <= 50);
            return;
        }
        objetivo.getCeldaPelea().getLuchadores().clear();
        objetivo.setCeldaPelea(c1celda);
        objetivo.getCeldaPelea().addLuchador(objetivo);
        SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 5, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + c1celda.getID());
        try {
            Thread.sleep(300L);
        }
        catch (Exception trampa) {
            // empty catch block
        }
        for (Combate.Trampa trampa : pelea.getTrampas()) {
            short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), c1celda.getID());
            if (dist > trampa.getTama\u00f1o()) continue;
            trampa.activaTrampa(objetivo);
            break;
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_784(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        if (this._turnos > 1) {
            return;
        }
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto() || objetivo.esInvocacion()) continue;
            Mapa.Celda celda1 = null;
            for (Map.Entry<Integer, Mapa.Celda> entry : pelea.getPosInicial().entrySet()) {
                if (entry.getKey().intValue() != objetivo.getID()) continue;
                celda1 = entry.getValue();
                break;
            }
            if (celda1.esCaminable(true) && !pelea.celdaOcupada(celda1.getID())) {
                objetivo.getCeldaPelea().getLuchadores().clear();
                objetivo.setCeldaPelea(celda1);
                objetivo.getCeldaPelea().addLuchador(objetivo);
                ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
                trampas.addAll(pelea.getTrampas());
                for (Combate.Trampa trampa : trampas) {
                    short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), objetivo.getCeldaPelea().getID());
                    if (dist > trampa.getTama\u00f1o()) continue;
                    trampa.activaTrampa(objetivo);
                }
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + celda1.getID());
            }
            try {
                Thread.sleep(200L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    private void aplicarEfecto_786(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, this._valor, this._turnos, 1, true, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_787(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int hechizoID = -1;
        int hechizoNivel = -1;
        try {
            hechizoID = Integer.parseInt(this._args.split(";")[0]);
            hechizoNivel = Integer.parseInt(this._args.split(";")[1]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        Hechizo hechizo = Mundo.getHechizo(hechizoID);
        ArrayList<EfectoHechizo> EH = hechizo.getStatsPorNivel(hechizoNivel).getEfectos();
        for (EfectoHechizo eh : EH) {
            for (Combate.Luchador objetivo : objetivos) {
                if (objetivo.estaMuerto()) continue;
                objetivo.addBuff(eh._efectoID, eh._valor, 1, 1, true, eh._hechizoID, eh._args, this._lanzador, this._veneno);
            }
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_788(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            objetivo.addBuff(this._efectoID, this._valor, this._turnos, 1, true, this._hechizoID, this._args, objetivo, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_950(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int idEstado = -1;
        try {
            idEstado = Integer.parseInt(this._args.split(";")[2]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (idEstado == -1) {
            return;
        }
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto()) continue;
            if (this._turnos <= 0) {
                if (objetivo.puedeJugar()) {
                    objetivo.setEstado(idEstado, this._turnos + 1);
                } else {
                    objetivo.setEstado(idEstado, this._turnos);
                }
                SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + idEstado + ",1");
                continue;
            }
            if (objetivo.puedeJugar()) {
                objetivo.setEstado(idEstado, this._turnos + 1);
            } else {
                objetivo.setEstado(idEstado, this._turnos);
            }
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + idEstado + ",1");
            objetivo.addBuff(this._efectoID, idEstado, this._turnos, 1, false, this._hechizoID, this._args, this._lanzador, this._veneno);
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }

    private void aplicarEfecto_951(ArrayList<Combate.Luchador> objetivos, Combate pelea) {
        int id = -1;
        try {
            id = Integer.parseInt(this._args.split(";")[2]);
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (id == -1) {
            return;
        }
        for (Combate.Luchador objetivo : objetivos) {
            if (objetivo.estaMuerto() || !objetivo.tieneEstado(id)) continue;
            objetivo.setEstado(id, 0);
            SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(this._lanzador.getID()), String.valueOf(objetivo.getID()) + "," + id + ",0");
        }
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
    }
}

