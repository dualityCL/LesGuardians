package common;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Mapa;

public class CryptManager {
    public static String encriptarPassword(String codigoLlave, String Password) {
        char[] HASH = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        String encriptado = "#1";
        for (int i = 0; i < Password.length(); ++i) {
            char PPass = Password.charAt(i);
            char PKey = codigoLlave.charAt(i);
            int APass = PPass / 16;
            int AKey = PPass % 16;
            int ANB = (APass + PKey) % HASH.length;
            int ANB2 = (AKey + PKey) % HASH.length;
            encriptado = String.valueOf(encriptado) + HASH[ANB];
            encriptado = String.valueOf(encriptado) + HASH[ANB2];
        }
        return encriptado;
    }

    public static String desencriptarPassword(String pass, String key) {
        String l7 = "";
        String Chaine = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
        for (int l1 = 0; l1 <= pass.length() - 1; l1 += 2) {
            char l3 = key.charAt(l1 / 2);
            int l2 = Chaine.indexOf(pass.charAt(l1));
            int l4 = 64 + l2 - l3;
            int l11 = l1 + 1;
            l2 = Chaine.indexOf(pass.charAt(l11));
            int l5 = 64 + l2 - l3;
            if (l5 < 0) {
                l5 += 64;
            }
            l7 = String.valueOf(l7) + (char)(16 * l4 + l5);
        }
        return l7;
    }

    public static String encriptarIP(String IP) {
        String[] Splitted = IP.split("\\.");
        String Encrypted = "";
        int Count = 0;
        for (int i = 0; i < 50; ++i) {
            for (int o = 0; o < 50; ++o) {
                if (((i & 0xF) << 4 | o & 0xF) != Integer.parseInt(Splitted[Count])) continue;
                Character A = Character.valueOf((char)(i + 48));
                Character B = Character.valueOf((char)(o + 48));
                Encrypted = String.valueOf(Encrypted) + A.toString() + B.toString();
                i = 0;
                o = 0;
                if (++Count != 4) continue;
                return Encrypted;
            }
        }
        return "DD";
    }

    public static String encriptarPuerto(int configGamePort) {
        char[] HASH = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        int P = configGamePort;
        String nbr64 = "";
        for (int a = 2; a >= 0; --a) {
            nbr64 = String.valueOf(nbr64) + HASH[(int)((double)P / Math.pow(64.0, a))];
            P %= (int)Math.pow(64.0, a);
        }
        return nbr64;
    }

    public static String celdaIDACodigo(int celdaID) {
        char[] HASH = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        int char1 = celdaID / 64;
        int char2 = celdaID % 64;
        return String.valueOf(HASH[char1]) + HASH[char2];
    }

    public static short celdaCodigoAID(String celdaCodigo) {
        char[] HASH = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        char char1 = celdaCodigo.charAt(0);
        char char2 = celdaCodigo.charAt(1);
        int code1 = 0;
        int code2 = 0;
        for (int a = 0; a < HASH.length; a = (int)((short)(a + 1))) {
            if (HASH[a] == char1) {
                code1 = (short)(a * 64);
            }
            if (HASH[a] != char2) continue;
            code2 = a;
        }
        return (short)(code1 + code2);
    }

    public static short getNumeroPorValorHash(char c) {
        char[] HASH = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        for (short a = 0; a < HASH.length; a = (short)(a + 1)) {
            if (HASH[a] != c) continue;
            return a;
        }
        return -1;
    }

    public static char getValorHashPorNumero(int c) {
        char[] hash = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        return hash[c];
    }

    public static ArrayList<Mapa.Celda> analizarInicioCelda(Mapa mapa, int num) {
        ArrayList<Mapa.Celda> listaCeldas = null;
        String infos = null;
        if (!mapa.getPosicionesPelea().equalsIgnoreCase("-1")) {
            infos = mapa.getPosicionesPelea().split("\\|")[num];
            listaCeldas = new ArrayList<Mapa.Celda>();
            for (int a = 0; a < infos.length(); a += 2) {
                listaCeldas.add(mapa.getCelda((short)((CryptManager.getNumeroPorValorHash(infos.charAt(a)) << 6) + CryptManager.getNumeroPorValorHash(infos.charAt(a + 1)))));
            }
        }
        return listaCeldas;
    }

    public static Map<Short, Mapa.Celda> decompilarMapaData(Mapa mapa, String dData) {
        TreeMap<Short, Mapa.Celda> celdas = new TreeMap<Short, Mapa.Celda>();
        for (int f = 0; f < dData.length(); f = (int)((short)(f + 10))) {
            String celdaData = dData.substring(f, f + 10);
            ArrayList<Byte> celdaInfo = new ArrayList<Byte>();
            for (int i = 0; i < celdaData.length(); ++i) {
                celdaInfo.add((byte)CryptManager.getNumeroPorValorHash(celdaData.charAt(i)));
            }
            int caminable = ((Byte)celdaInfo.get(2) & 0x38) >> 3;
            boolean lineaDeVista = ((Byte)celdaInfo.get(0) & 1) != 0;
            int layerObject2 = (((Byte)celdaInfo.get(0) & 2) << 12) + (((Byte)celdaInfo.get(7) & 1) << 12) + ((Byte)celdaInfo.get(8) << 6) + (Byte)celdaInfo.get(9);
            boolean layerObjeto2Interac = ((Byte)celdaInfo.get(7) & 2) >> 1 != 0;
            int objeto = layerObjeto2Interac ? layerObject2 : -1;
            celdas.put((short)(f / 10), new Mapa.Celda(mapa, (short)(f / 10), caminable != 0, lineaDeVista, objeto));
        }
        return celdas;
    }

    public static String aUTF(String entrada) {
        String _out = "";
        try {
            _out = new String(entrada.getBytes("UTF8"));
        }
        catch (Exception e) {
            System.out.println("Conversion en UTF-8 fallida! : " + e.getMessage());
        }
        return _out;
    }

    public static String aUnicode(String entrada) {
        String _out = "";
        try {
            _out = new String(entrada.getBytes(), "UTF8");
        }
        catch (Exception e) {
            System.out.println("Conversion en UNICODE fallida! : " + e.getMessage());
        }
        return _out;
    }
}

