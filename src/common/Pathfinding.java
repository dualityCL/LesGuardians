package common;

import common.CryptManager;
import common.Fórmulas;
import common.Mundo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import objects.Combate;
import objects.Mapa;

public class Pathfinding {
    private static Integer _nroMovimientos = new Integer(0);

    public static short numeroMovimientos(Mapa mapa, short celdaID, AtomicReference<String> pathRef, Combate pelea) {
        synchronized (_nroMovimientos) {
          _nroMovimientos = Integer.valueOf(0);
          short nuevaCelda = celdaID;
          short movimientos = 0;
          String path = pathRef.get();
          String nuevoPath = "";
          for (int i = 0; i < path.length(); i += 3) {
            if (path.length() < i + 3)
              return movimientos; 
            String miniPath = path.substring(i, i + 3);
            char dir = miniPath.charAt(0);
            short celdaIDDeco = CryptManager.celdaCodigoAID(miniPath.substring(1));
            _nroMovimientos = Integer.valueOf(0);
            if (pelea != null && i != 0 && getEnemigoAlrededor(nuevaCelda, mapa, pelea) != null) {
              pathRef.set(nuevoPath);
              return movimientos;
            } 
            if (pelea != null && i != 0) {
              for (Combate.Trampa p : pelea.getTrampas()) {
                int dist = distanciaEntreDosCeldas(mapa, p.getCelda().getID(), nuevaCelda);
                if (dist <= p.getTamaño()) {
                  pathRef.set(nuevoPath);
                  return movimientos;
                } 
              } 
              if (pelea.getMapaCopia().getCelda(nuevaCelda).getPrimerLuchador() != null) {
                pathRef.set(nuevoPath);
                return (short)(movimientos + 10000);
              } 
            } 
            String[] aPathInfos = ValidSinglePath(nuevaCelda, miniPath, mapa, pelea).split(":");
            if (aPathInfos[0].equalsIgnoreCase("stop")) {
              nuevaCelda = Short.parseShort(aPathInfos[1]);
              movimientos = (short)(movimientos + _nroMovimientos.intValue());
              nuevoPath = String.valueOf(nuevoPath) + dir + CryptManager.celdaIDACodigo(nuevaCelda);
              pathRef.set(nuevoPath);
              return (short)-movimientos;
            } 
            if (aPathInfos[0].equalsIgnoreCase("ok")) {
              nuevaCelda = celdaIDDeco;
              movimientos = (short)(movimientos + _nroMovimientos.intValue());
            } else {
              pathRef.set(nuevoPath);
              return -1000;
            } 
            nuevoPath = String.valueOf(nuevoPath) + dir + CryptManager.celdaIDACodigo(nuevaCelda);
          } 
          pathRef.set(nuevoPath);
          return movimientos;
        } 
      }

    public static Combate.Luchador getEnemigoAlrededor(short celdaID, Mapa mapa, Combate pelea) {
        char[] dirs;
        char[] arrc = dirs = new char[]{'b', 'd', 'f', 'h'};
        int n = dirs.length;
        for (int i = 0; i < n; ++i) {
            Combate.Luchador luchador;
            char dir = arrc[i];
            Mapa.Celda sigCelda = mapa.getCelda(Pathfinding.getSigIDCeldaMismaDir(celdaID, dir, mapa, false));
            if (sigCelda == null || (luchador = sigCelda.getPrimerLuchador()) == null || luchador.getEquipoBin() == pelea.getLuchadorTurno().getEquipoBin()) continue;
            return luchador;
        }
        return null;
    }

    public static boolean hayAlrededor(Mapa mapa, Combate pelea, Combate.Luchador l, boolean amigo) {
        char[] dirs;
        char[] arrc = dirs = new char[]{'b', 'd', 'f', 'h'};
        int n = dirs.length;
        for (int i = 0; i < n; ++i) {
            Combate.Luchador luchador;
            char dir = arrc[i];
            Mapa.Celda sigCelda = mapa.getCelda(Pathfinding.getSigIDCeldaMismaDir(l.getCeldaPelea().getID(), dir, mapa, false));
            if (sigCelda == null || (luchador = sigCelda.getPrimerLuchador()) == null || !(amigo ? luchador.getEquipoBin() == l.getEquipoBin() : luchador.getEquipoBin() != l.getEquipoBin())) continue;
            return true;
        }
        return false;
    }

    public static boolean esSiguienteA(int celda1, int celda2, Mapa mapa) {
        byte ancho = mapa.getAncho();
        return celda1 + (ancho - 1) == celda2 || celda1 + ancho == celda2 || celda1 - (ancho - 1) == celda2 || celda1 - ancho == celda2;
    }

    public static String ValidSinglePath(short celdaID, String Path2, Mapa mapa, Combate pelea) {
        _nroMovimientos = 0;
        char dir = Path2.charAt(0);
        short celdaIDDeco = CryptManager.celdaCodigoAID(Path2.substring(1));
        if (pelea != null && pelea.celdaOcupada(celdaIDDeco)) {
            return "no:";
        }
        short ultimaCelda = celdaID;
        _nroMovimientos = 1;
        while (_nroMovimientos <= 64) {
            if (Pathfinding.getSigIDCeldaMismaDir(ultimaCelda, dir, mapa, pelea != null) == celdaIDDeco) {
                if (pelea != null && pelea.celdaOcupada(celdaIDDeco)) {
                    return "stop:" + ultimaCelda;
                }
                if (mapa.getCelda(celdaIDDeco).esCaminable(true)) {
                    return "ok:";
                }
                _nroMovimientos = _nroMovimientos - 1;
                return "stop:" + ultimaCelda;
            }
            ultimaCelda = Pathfinding.getSigIDCeldaMismaDir(ultimaCelda, dir, mapa, pelea != null);
            if (pelea != null && pelea.celdaOcupada(ultimaCelda)) {
                return "no:";
            }
            if (pelea != null) {
                if (Pathfinding.getEnemigoAlrededor(ultimaCelda, mapa, pelea) != null) {
                    return "stop:" + ultimaCelda;
                }
                for (Combate.Trampa p : pelea.getTrampas()) {
                    short dist = Pathfinding.distanciaEntreDosCeldas(mapa, p.getCelda().getID(), ultimaCelda);
                    if (dist > p.getTama\u00f1o()) continue;
                    return "stop:" + ultimaCelda;
                }
            }
            _nroMovimientos = _nroMovimientos + 1;
        }
        return "no:";
    }

    public static short getSigIDCeldaMismaDir(short celdaID, char direccion, Mapa mapa, boolean combate) {
        switch (direccion) {
            case 'a': {
                return (short)(combate ? -1 : celdaID + 1);
            }
            case 'b': {
                return (short)(celdaID + mapa.getAncho());
            }
            case 'c': {
                return (short)(combate ? -1 : celdaID + (mapa.getAncho() * 2 - 1));
            }
            case 'd': {
                return (short)(celdaID + (mapa.getAncho() - 1));
            }
            case 'e': {
                return (short)(combate ? -1 : celdaID - 1);
            }
            case 'f': {
                return (short)(celdaID - mapa.getAncho());
            }
            case 'g': {
                return (short)(combate ? -1 : celdaID - (mapa.getAncho() * 2 - 1));
            }
            case 'h': {
                return (short)(celdaID - mapa.getAncho() + 1);
            }
        }
        return -1;
    }

    public static short getSigIDCeldaMismaDir(short celdaID, char direccion, short mapaID) {
        Mapa mapa = Mundo.getMapa(mapaID);
        if (mapa == null) {
            return -1;
        }
        switch (direccion) {
            case 'b': {
                return (short)(celdaID + mapa.getAncho());
            }
            case 'd': {
                return (short)(celdaID + (mapa.getAncho() - 1));
            }
            case 'f': {
                return (short)(celdaID - mapa.getAncho());
            }
            case 'h': {
                return (short)(celdaID - mapa.getAncho() + 1);
            }
        }
        return -1;
    }

    public static short distanciaEntreDosCeldas(Mapa mapa, short id1, short id2) {
        if (id1 == id2) {
            return 0;
        }
        if (mapa == null) {
            return 0;
        }
        int diffX = Math.abs(Pathfinding.getCeldaCoordenadaX(mapa, id1) - Pathfinding.getCeldaCoordenadaX(mapa, id2));
        int diffY = Math.abs(Pathfinding.getCeldaCoordenadaY(mapa, id1) - Pathfinding.getCeldaCoordenadaY(mapa, id2));
        return (short)(diffX + diffY);
    }

    public static char getDirAleatorio() {
        char[] direcciones = new char[]{'b', 'd', 'f', 'h'};
        int aleatorio = Fórmulas.getRandomValor(0, 3);
        return direcciones[aleatorio];
    }

    public static short getNuevaCeldaDespuesGolpe(Mapa mapa, Mapa.Celda celdaInicio, Mapa.Celda celdaObjetivo, int valor, Combate pelea, Combate.Luchador objetivo) {
        if (celdaInicio.getID() == celdaObjetivo.getID()) {
            return 0;
        }
        char c = Pathfinding.getDirEntreDosCeldas(celdaInicio.getID(), celdaObjetivo.getID(), mapa, true);
        short idCelda = celdaObjetivo.getID();
        if (valor < 0) {
            c = Pathfinding.getDireccionOpuesta(c);
            valor = -valor;
        }
        for (int a = 0; a < valor; ++a) {
            short sigCelda = Pathfinding.getSigIDCeldaMismaDir(idCelda, c, mapa, true);
            if (mapa.getCelda(sigCelda) != null && mapa.getCelda(sigCelda).esCaminable(true) && mapa.getCelda(sigCelda).getLuchadores().isEmpty()) {
                idCelda = sigCelda;
                for (Combate.Trampa trampa : pelea.getTrampas()) {
                    short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), idCelda);
                    if (dist > trampa.getTama\u00f1o()) continue;
                    return idCelda;
                }
                continue;
            }
            return (short)(-(valor - a));
        }
        if (idCelda == celdaObjetivo.getID()) {
            idCelda = 0;
        }
        return idCelda;
    }

    public static short getCeldaDespEmpujon(Mapa mapa, Mapa.Celda celdaLanz, Mapa.Celda celdaObje, int valor, Combate pelea, Combate.Luchador objetivo) {
        if (celdaLanz.getID() == celdaObje.getID()) {
            return 0;
        }
        char c = Pathfinding.getDirEntreDosCeldas(celdaLanz.getID(), celdaObje.getID(), mapa, true);
        short idCelda = celdaObje.getID();
        if (valor < 0) {
            c = Pathfinding.getDireccionOpuesta(c);
            valor = -valor;
        }
        for (int a = 0; a < valor; ++a) {
            short sigCelda = Pathfinding.getSigIDCeldaMismaDir(idCelda, c, mapa, true);
            if (mapa.getCelda(sigCelda) != null && mapa.getCelda(sigCelda).esCaminable(true) && mapa.getCelda(sigCelda).getLuchadores().isEmpty()) {
                idCelda = sigCelda;
                for (Combate.Trampa trampa : pelea.getTrampas()) {
                    short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(), idCelda);
                    if (dist > trampa.getTama\u00f1o()) continue;
                    return idCelda;
                }
                continue;
            }
            return (short)(-(valor - a));
        }
        if (idCelda == celdaObje.getID()) {
            idCelda = 0;
        }
        return idCelda;
    }

    public static char getDireccionOpuesta(char c) {
        switch (c) {
            case 'a': {
                return 'e';
            }
            case 'b': {
                return 'f';
            }
            case 'c': {
                return 'g';
            }
            case 'd': {
                return 'h';
            }
            case 'e': {
                return 'a';
            }
            case 'f': {
                return 'b';
            }
            case 'g': {
                return 'c';
            }
            case 'h': {
                return 'd';
            }
        }
        return '\u0000';
    }

    public static boolean siCeldasEstanEnMismaLinea(Mapa map, short c1, short c2, char dir) {
        if (c1 == c2) {
            return true;
        }
        if (dir != 'z') {
            for (int a = 0; a < 70; ++a) {
                if (Pathfinding.getSigIDCeldaMismaDir(c1, dir, map, true) == c2) {
                    return true;
                }
                if (Pathfinding.getSigIDCeldaMismaDir(c1, dir, map, true) != -1) {
                    c1 = Pathfinding.getSigIDCeldaMismaDir(c1, dir, map, true);
                    continue;
                }
                break;
            }
        } else {
            char[] dirs;
            char[] arrc = dirs = new char[]{'b', 'd', 'f', 'h'};
            int n = dirs.length;
            for (int i = 0; i < n; ++i) {
                char d = arrc[i];
                short c = c1;
                for (int a = 0; a < 70; ++a) {
                    if (Pathfinding.getSigIDCeldaMismaDir(c, d, map, true) == c2) {
                        return true;
                    }
                    c = Pathfinding.getSigIDCeldaMismaDir(c, d, map, true);
                }
            }
        }
        return false;
    }

    public static ArrayList<Combate.Luchador> getObjetivosZonaArma(Combate pelea, int tipo, Mapa.Celda celda, short celdaIDLanzador) {
        ArrayList<Combate.Luchador> objetivos = new ArrayList<Combate.Luchador>();
        char c = Pathfinding.getDirEntreDosCeldas(celdaIDLanzador, celda.getID(), pelea.getMapaCopia(), true);
        if (c == '\u0000') {
            if (celda.getPrimerLuchador() != null) {
                objetivos.add(celda.getPrimerLuchador());
            }
            return objetivos;
        }
        switch (tipo) {
            case 7: {
                Combate.Luchador i;
                Combate.Luchador h;
                Combate.Luchador g;
                Combate.Luchador f = Pathfinding.getLuchadorAntesCelda(celdaIDLanzador, c, pelea.getMapaCopia());
                if (f != null) {
                    objetivos.add(f);
                }
                if ((g = Pathfinding.getPrimerLuchadorMismaDireccion(pelea.getMapaCopia(), celdaIDLanzador, (char)(c - '\u0001'))) != null) {
                    objetivos.add(g);
                }
                if ((h = Pathfinding.getPrimerLuchadorMismaDireccion(pelea.getMapaCopia(), celdaIDLanzador, (char)(c + '\u0001'))) != null) {
                    objetivos.add(h);
                }
                if ((i = celda.getPrimerLuchador()) == null) break;
                objetivos.add(i);
                break;
            }
            case 4: {
                Combate.Luchador l;
                Combate.Luchador k;
                Combate.Luchador j = Pathfinding.getPrimerLuchadorMismaDireccion(pelea.getMapaCopia(), celdaIDLanzador, (char)(c - '\u0001'));
                if (j != null) {
                    objetivos.add(j);
                }
                if ((k = Pathfinding.getPrimerLuchadorMismaDireccion(pelea.getMapaCopia(), celdaIDLanzador, (char)(c + '\u0001'))) != null) {
                    objetivos.add(k);
                }
                if ((l = celda.getPrimerLuchador()) == null) break;
                objetivos.add(l);
                break;
            }
            case 2: 
            case 3: 
            case 5: 
            case 6: 
            case 8: 
            case 19: 
            case 21: 
            case 22: {
                Combate.Luchador m = celda.getPrimerLuchador();
                if (m == null) break;
                objetivos.add(m);
            }
        }
        return objetivos;
    }

    private static Combate.Luchador getPrimerLuchadorMismaDireccion(Mapa mapa, short idCelda, char dir) {
        if (dir == '`') {
            dir = (char)104;
        }
        if (dir == 'i') {
            dir = (char)97;
        }
        return mapa.getCelda(Pathfinding.getSigIDCeldaMismaDir(idCelda, dir, mapa, false)).getPrimerLuchador();
    }

    private static Combate.Luchador getLuchadorAntesCelda(short celdaID, char dir, Mapa mapa) {
        short nueva2CeldaID = Pathfinding.getSigIDCeldaMismaDir(Pathfinding.getSigIDCeldaMismaDir(celdaID, dir, mapa, false), dir, mapa, false);
        return mapa.getCelda(nueva2CeldaID).getPrimerLuchador();
    }

    public static char getCharDir(int direccion) {
        switch (direccion) {
            case 0: {
                return 'a';
            }
            case 1: {
                return 'b';
            }
            case 2: {
                return 'c';
            }
            case 3: {
                return 'd';
            }
            case 4: {
                return 'e';
            }
            case 5: {
                return 'f';
            }
            case 6: {
                return 'g';
            }
            case 7: {
                return 'h';
            }
        }
        return 'b';
    }
    
    public static char getDirEntreDosCeldas(Mapa mapa, short id1, short id2) {
        if (id1 == id2)
          return Character.MIN_VALUE; 
        if (mapa == null)
          return Character.MIN_VALUE; 
        short difX = (short)(getCeldaCoordenadaX(mapa, id1) - getCeldaCoordenadaX(mapa, id2));
        short difY = (short)(getCeldaCoordenadaY(mapa, id1) - getCeldaCoordenadaY(mapa, id2));
        int difXabs = Math.abs(difX);
        int difYabs = Math.abs(difY);
        if (difXabs > difYabs) {
          if (difX > 0)
            return 'f'; 
          return 'b';
        } 
        if (difY > 0)
          return 'h'; 
        return 'd';
      }

    public static char getDirEntreDosCeldas(short celdaID1, short celdaID2, Mapa mapa, boolean combate) { //FIXME método con ligeros errores
        ArrayList<Character> direcciones = new ArrayList<Character>();
        direcciones.add(Character.valueOf('b'));
        direcciones.add(Character.valueOf('d'));
        direcciones.add(Character.valueOf('f'));
        direcciones.add(Character.valueOf('h'));
        if (!combate) {
            direcciones.add(Character.valueOf('a'));
            direcciones.add(Character.valueOf('b'));
            direcciones.add(Character.valueOf('c'));
            direcciones.add(Character.valueOf('d'));
        }
        Iterator iterator = direcciones.iterator();
        while (iterator.hasNext()) {
            char c = ((Character)iterator.next()).charValue();
            short celda = celdaID1;
            for (int i = 0; i <= 64; ++i) {
                if (Pathfinding.getSigIDCeldaMismaDir(celda, c, mapa, combate) == celdaID2) {
                    return c;
                }
                celda = Pathfinding.getSigIDCeldaMismaDir(celda, c, mapa, combate);
            }
        }
        return '\u0000';
    }

    public static ArrayList<Mapa.Celda> getCeldasAfectadasEnElArea(Mapa mapa, short celdaID, short celdaIDLanzador, String afectados, int posTipoAlcance, boolean esGC) {
        ArrayList<Mapa.Celda> cases = new ArrayList<Mapa.Celda>();
        if (mapa == null) {
            return cases;
        }
        if (mapa.getCelda(celdaID) == null) {
            return cases;
        }
        cases.add(mapa.getCelda(celdaID));
        int tama\u00f1o = CryptManager.getNumeroPorValorHash(afectados.charAt(posTipoAlcance + 1));
        switch (afectados.charAt(posTipoAlcance)) {
            case 'C': {
                for (int a = 0; a < tama\u00f1o; ++a) {
                    char[] dirs = new char[]{'b', 'd', 'f', 'h'};
                    ArrayList<Mapa.Celda> cases2 = new ArrayList<Mapa.Celda>();
                    cases2.addAll(cases);
                    for (Mapa.Celda aCell : cases2) {
                        char[] arrc = dirs;
                        int n = dirs.length;
                        for (int i = 0; i < n; ++i) {
                            char d = arrc[i];
                            Mapa.Celda cell = mapa.getCelda(Pathfinding.getSigIDCeldaMismaDir(aCell.getID(), d, mapa, true));
                            if (cell == null || cases.contains(cell)) continue;
                            cases.add(cell);
                        }
                    }
                }
                break;
            }
            case 'X': {
                char[] dirs;
                char[] arrc = dirs = new char[]{'b', 'd', 'f', 'h'};
                int n = dirs.length;
                for (int cases2 = 0; cases2 < n; ++cases2) {
                    char d = arrc[cases2];
                    short cID = celdaID;
                    for (int a = 0; a < tama\u00f1o; ++a) {
                        cases.add(mapa.getCelda(Pathfinding.getSigIDCeldaMismaDir(cID, d, mapa, true)));
                        cID = Pathfinding.getSigIDCeldaMismaDir(cID, d, mapa, true);
                    }
                }
                break;
            }
            case 'L': {
                char dir = Pathfinding.getDirEntreDosCeldas(celdaIDLanzador, celdaID, mapa, true);
                for (int a = 0; a < tama\u00f1o; ++a) {
                    cases.add(mapa.getCelda(Pathfinding.getSigIDCeldaMismaDir(celdaID, dir, mapa, true)));
                    celdaID = Pathfinding.getSigIDCeldaMismaDir(celdaID, dir, mapa, true);
                }
                break;
            }
            case 'P': 
            case 'T': {
                break;
            }
            default: {
                System.out.println("[FIXME]Tipo de alcance no reconocido: " + afectados.charAt(0));
            }
        }
        return cases;
    }

    public static short getCeldaCoordenadaX(Mapa mapa, short celdaID) {
        if (mapa == null) {
            return 0;
        }
        byte ancho = mapa.getAncho();
        return (short)((celdaID - (ancho - 1) * Pathfinding.getCeldaCoordenadaY(mapa, celdaID)) / ancho);
    }

    public static short getCeldaCoordenadaY(Mapa mapa, short celdaID) {
        byte ancho = mapa.getAncho();
        short loc5 = (short)(celdaID / (ancho * 2 - 1));
        short loc6 = (short)(celdaID - loc5 * (ancho * 2 - 1));
        short loc7 = (short)(loc6 % ancho);
        return (short)(loc5 - loc7);
    }

    public static boolean checkearLineaDeVista(Mapa mapa, short celda1, short celda2, Combate.Luchador luchador) {
        if (luchador.getPersonaje() != null)
          return true; 
        int dist = distanciaEntreDosCeldas(mapa, celda1, celda2);
        ArrayList<Short> los = new ArrayList<Short>();
        if (dist > 2)
          los = getLineaDeVista(celda1, celda2, mapa); 
        if (los != null && dist > 2)
          for (Iterator<Short> iterator = los.iterator(); iterator.hasNext(); ) {
            short i = ((Short)iterator.next()).shortValue();
            if (i != celda1 && i != celda2 && !mapa.getCelda(i).lineaDeVistaBloqueada())
              return false; 
          }  
        if (dist > 2) {
          short celda = getCeldaMasCercanaAlrededor(mapa, celda2, celda1, null);
          if (celda != -1 && !mapa.getCelda(celda).lineaDeVistaBloqueada())
            return false; 
        } 
        return true;
      }

    public static short getCeldaMasCercanaAlrededor(Mapa mapa, short celdaInicio, short celdaFinal, ArrayList<Mapa.Celda> celdasProhibidas) { //FIXME método con ligeros errores
        char[] dirs;
        short dist = 1000;
        short celdaID = celdaInicio;
        if (celdasProhibidas == null) {
            celdasProhibidas = new ArrayList();
        }
        char[] arrc = dirs = new char[]{'b', 'd', 'f', 'h'};
        int n = dirs.length;
        for (int i = 0; i < n; ++i) {
            char d = arrc[i];
            short sigCelda = Pathfinding.getSigIDCeldaMismaDir(celdaInicio, d, mapa, true);
            Mapa.Celda C = mapa.getCelda(sigCelda);
            if (C == null) break;
            short dis = Pathfinding.distanciaEntreDosCeldas(mapa, celdaFinal, sigCelda);
            if (dis >= dist || !C.esCaminable(true) || C.getPrimerLuchador() != null || celdasProhibidas.contains(C)) continue;
            dist = dis;
            celdaID = sigCelda;
        }
        return celdaID == celdaInicio ? (short)-1 : (short)celdaID;
    }

    public static short getCeldaMasCercanaAlrededor2(Mapa mapa, short celdaInicio, short celdaFinal, int alcanceMin, int alcanceMax) {
        Mapa.Celda C;
        short dist = 1000;
        short celdaID = celdaInicio;
        char d = Pathfinding.getDirEntreDosCeldas(mapa, celdaInicio, celdaFinal);
        short celdaInicio2 = celdaInicio;
        short sigCelda = 0;
        int i = 0;
        while (i < alcanceMax) {
            celdaInicio2 = sigCelda = Pathfinding.getSigIDCeldaMismaDir(celdaInicio2, d, mapa, true);
            if (++i > alcanceMin && (C = mapa.getCelda(sigCelda)) != null && C.esCaminable(true) && C.getPrimerLuchador() == null) break;
        }
        if ((C = mapa.getCelda(sigCelda)) == null) {
            return -1;
        }
        short dis = Pathfinding.distanciaEntreDosCeldas(mapa, celdaFinal, sigCelda);
        if (dis < dist && C.esCaminable(true) && C.getPrimerLuchador() == null) {
            dist = dis;
            celdaID = sigCelda;
        }
        return celdaID == celdaInicio ? (short)-1 : (short)celdaID;
    }

    public static ArrayList<Mapa.Celda> pathMasCortoEntreDosCeldas(Mapa mapa, short inicio, short destino, int distMax) {
        short celdaMasCercana;
        ArrayList<Mapa.Celda> tempPath = new ArrayList<Mapa.Celda>();
        ArrayList<Mapa.Celda> tempPath2 = new ArrayList<Mapa.Celda>();
        ArrayList<Mapa.Celda> celdasCerradas = new ArrayList<Mapa.Celda>();
        int limite = 1000;
        Mapa.Celda tempCelda = mapa.getCelda(inicio);
        int stepNum = 0;
        boolean stop = false;
        while (!stop && stepNum++ <= limite) {
            celdaMasCercana = Pathfinding.getCeldaMasCercanaAlrededor(mapa, tempCelda.getID(), destino, celdasCerradas);
            if (celdaMasCercana == -1) {
                celdasCerradas.add(tempCelda);
                if (tempPath.size() > 0) {
                    tempPath.remove(tempPath.size() - 1);
                    if (tempPath.size() > 0) {
                        tempCelda = tempPath.get(tempPath.size() - 1);
                        continue;
                    }
                    tempCelda = mapa.getCelda(inicio);
                    continue;
                }
                tempCelda = mapa.getCelda(inicio);
                continue;
            }
            if (distMax == 0 && celdaMasCercana == destino) {
                tempPath.add(mapa.getCelda(destino));
                break;
            }
            if (distMax > Pathfinding.distanciaEntreDosCeldas(mapa, celdaMasCercana, destino)) {
                tempPath.add(mapa.getCelda(destino));
                break;
            }
            tempCelda = mapa.getCelda(celdaMasCercana);
            celdasCerradas.add(tempCelda);
            tempPath.add(tempCelda);
        }
        tempCelda = mapa.getCelda(inicio);
        celdasCerradas.clear();
        if (!tempPath.isEmpty()) {
            celdasCerradas.add((Mapa.Celda)tempPath.get(0));
        }
        while (!stop && stepNum++ <= limite) {
            celdaMasCercana = Pathfinding.getCeldaMasCercanaAlrededor(mapa, tempCelda.getID(), destino, celdasCerradas);
            if (celdaMasCercana == -1) {
                celdasCerradas.add(tempCelda);
                if (tempPath2.size() > 0) {
                    tempPath2.remove(tempPath2.size() - 1);
                    if (tempPath2.size() > 0) {
                        tempCelda = (Mapa.Celda)tempPath2.get(tempPath2.size() - 1);
                        continue;
                    }
                    tempCelda = mapa.getCelda(inicio);
                    continue;
                }
                tempCelda = mapa.getCelda(inicio);
                continue;
            }
            if (distMax == 0 && celdaMasCercana == destino) {
                tempPath2.add(mapa.getCelda(destino));
                break;
            }
            if (distMax > Pathfinding.distanciaEntreDosCeldas(mapa, celdaMasCercana, destino)) {
                tempPath2.add(mapa.getCelda(destino));
                break;
            }
            tempCelda = mapa.getCelda(celdaMasCercana);
            celdasCerradas.add(tempCelda);
            tempPath2.add(tempCelda);
        }
        if (tempPath2.size() < tempPath.size() && tempPath2.size() > 0 || tempPath.isEmpty()) {
            tempPath = tempPath2;
        }
        return tempPath;
    }

    public static ArrayList<Short> listaCeldasDesdeLuchador(Combate pelea, Combate.Luchador luchador) {
        int[] tempPath;
        ArrayList<Short> celdas = new ArrayList<Short>();
        short celdaInicio = luchador.getCeldaPelea().getID();
        int i = 0;
        if (luchador.getTempPM(pelea) > 0) {
          tempPath = new int[luchador.getTempPM(pelea)];
        } else {
          return null;
        } 
        if (tempPath.length == 0)
          return null; 
        while (tempPath[0] != 5) {
          tempPath[i] = tempPath[i] + 1;
          if (tempPath[i] == 5 && i != 0) {
            tempPath[i] = 0;
            i--;
            continue;
          } 
          short tempCeldaID = getCeldaDesdePath(celdaInicio, tempPath, pelea.getMapaCopia());
          Mapa.Celda celdaTemp = pelea.getMapaCopia().getCelda(tempCeldaID);
          if (celdaTemp == null)
            continue; 
          if (celdaTemp.esCaminable(true) && celdaTemp.getPrimerLuchador() == null && 
            !celdas.contains(Short.valueOf(tempCeldaID))) {
            celdas.add(Short.valueOf(tempCeldaID));
            if (i < tempPath.length - 1)
              i++; 
          } 
        } 
        return listaCeldasPorDistancia(pelea, luchador, celdas);
      }

    public static short getCeldaDesdePath(short inicio, int[] path, Mapa mapa) {
        short celda = inicio;
        byte ancho = mapa.getAncho();
        for (int i = 0; i < path.length; i = (int)((short)(i + 1))) {
            if (path[i] == 1) {
                celda = (short)(celda - ancho);
            }
            if (path[i] == 2) {
                celda = (short)(celda - (ancho - 1));
            }
            if (path[i] == 3) {
                celda = (short)(celda + ancho);
            }
            if (path[i] != 4) continue;
            celda = (short)(celda + (ancho - 1));
        }
        return celda;
    }

    public static ArrayList<Short> listaCeldasPorDistancia(Combate pelea, Combate.Luchador luchador, ArrayList<Short> celdas) { //FIXME método con ligeros errores
        ArrayList<Short> celdasPelea = new ArrayList<Short>();
        ArrayList<Short> copiaCeldas = new ArrayList<Short>();
        copiaCeldas.addAll(celdasPelea);
        short dist = 100;
        short tempCelda = 0;
        int tempIndex = 0;
        while (copiaCeldas.size() > 0) {
            dist = 200;
            Iterator iterator = copiaCeldas.iterator();
            while (iterator.hasNext()) {
                short celda = (Short)iterator.next();
                short d = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), luchador.getCeldaPelea().getID(), celda);
                if (dist <= d) continue;
                dist = d;
                tempCelda = celda;
                tempIndex = copiaCeldas.indexOf(celda);
            }
            celdasPelea.add(tempCelda);
            copiaCeldas.remove(tempIndex);
        }
        return celdasPelea;
    }

    public static boolean esBorde1(int id) {
        int[] bords = new int[]{1, 30, 59, 88, 117, 146, 175, 204, 233, 262, 291, 320, 349, 378, 407, 436, 465, 15, 44, 73, 102, 131, 160, 189, 218, 247, 276, 305, 334, 363, 392, 421, 450, 479};
        ArrayList<Integer> test = new ArrayList<Integer>();
        int[] arrn = bords;
        int n = bords.length;
        for (int i = 0; i < n; ++i) {
            int i2 = arrn[i];
            test.add(i2);
        }
        return test.contains(id);
    }

    public static boolean esBorde2(int id) {
        int[] bords = new int[]{16, 45, 74, 103, 132, 161, 190, 219, 248, 277, 306, 335, 364, 393, 422, 451, 29, 58, 87, 116, 145, 174, 203, 232, 261, 290, 319, 348, 377, 406, 435, 464};
        ArrayList<Integer> test = new ArrayList<Integer>();
        int[] arrn = bords;
        int n = bords.length;
        for (int i = 0; i < n; ++i) {
            int i2 = arrn[i];
            test.add(i2);
        }
        return test.contains(id);
    }

    public static ArrayList<Short> getLineaDeVista(short celda1, short celda2, Mapa mapa) {
        int[] dir1;
        ArrayList<Short> lineasDeVista = new ArrayList<Short>();
        short celda = celda1;
        boolean siguiente = false;
        byte ancho = mapa.getAncho();
        byte alto = mapa.getAlto();
        short ultCelda = mapa.ultimaCeldaID();
        int[] arrn = dir1 = new int[]{1, -1, ancho + alto, -(ancho + alto), ancho, ancho - 1, -ancho, -(ancho - 1)};
        int n = dir1.length;
        for (int i = 0; i < n; ++i) {
            int i2 = arrn[i];
            lineasDeVista.clear();
            celda = celda1;
            lineasDeVista.add(celda);
            siguiente = false;
            while (!siguiente) {
                celda = (short)(celda + i2);
                lineasDeVista.add(celda);
                if (Pathfinding.esBorde2(celda) || Pathfinding.esBorde1(celda) || celda <= 0 || celda >= ultCelda) {
                    siguiente = true;
                }
                if (celda != celda2) continue;
                return lineasDeVista;
            }
        }
        return null;
    }

    public static short celdaMovPerco(Mapa mapa, short celda) {
        ArrayList<Short> celdasPosibles = new ArrayList<Short>();
        byte ancho = mapa.getAncho();
        short[] dir = new short[]{(short) -ancho, (short)(-(ancho - 1)), (short)(ancho - 1), ancho};
        for (int i = 0; i < dir.length; ++i) {
            if (celda + dir[i] <= 14 && celda + dir[i] >= 464 || !mapa.getCelda((short)(celda + dir[i])).esCaminable(false)) continue;
            celdasPosibles.add((short)(celda + dir[i]));
        }
        if (celdasPosibles.size() <= 0) {
            return -1;
        }
        short celda_mov = (Short)celdasPosibles.get(Fórmulas.getRandomValor(0, celdasPosibles.size() - 1));
        return celda_mov;
    }
}

