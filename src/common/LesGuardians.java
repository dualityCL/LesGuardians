package common;

import common.Consola;
import common.ConsolaPersonalización;
import common.SQLManager;
import common.Mundo;
import game.GameServer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.ArrayList;
import org.fusesource.jansi.AnsiConsole;
import realm.RealmServer;

public class LesGuardians {
    public static boolean _estaIniciado = false;
    public static GameServer _servidorPersonaje;
    public static RealmServer _servidorGeneral;
    public static boolean _cerrando;
    public static boolean _corriendo;
    public static boolean _salvando;
    public static String IP_PC_SERVER;
    public static String BDD_HOST;
    public static String BDD_USER;
    public static String BDD_PASS;
    public static String BDD_STATIC;
    public static String BDD_OTHER;
    public static String NOME_SERVIDOR;
    public static String CRYPTER_IP;
    public static String EVENTO;
    public static short LIMITE_MAPAS;
    public static boolean MODO_HEROICO;
    public static String MAPAS_TEST;
    public static short MAPA_LAG;
    public static boolean EXPULSAR;
    public static long TEMPO_COMERCIO_RECRUTAMENTO;
    public static String MSG_BEM_VINDO_1;
    public static String MSG_BEM_VINDO_2;
    public static long TEMPO_VIP;
    public static String COR_MSG;
    public static boolean MOSTRAR_RECIBIDOS;
    public static boolean MOSTRAR_ENVIOS_SOS;
    public static boolean MOSTRAR_ENVIOS_STD;
    public static boolean CONFIG_POLICIA;
    public static int CONFIG_PRECO_SCROLLAR;
    public static short PRECO_RECURSO;
    public static ArrayList<Integer> TIPO_RECURSOS = new ArrayList<Integer>();
    public static ArrayList<Integer> OBJETOS_NAO_PERMITIDOS = new ArrayList<Integer>();;
    public static ArrayList<Integer> NAO_COMERCIALIZADOS_EN = new ArrayList<Integer>();;
    public static ArrayList<Integer> ALIMENTOS_MONTARIA = new ArrayList<Integer>();;
    public static ArrayList<Integer> ARMAS_ENCARNACAO = new ArrayList<Integer>();;
    public static ArrayList<String> PUBLICIDADE = new ArrayList<String>();;
    public static String ARMAS;
    public static int INICIAR_SET_ID;
    public static int PORTA_SERVIDOR;
    public static int PORTA_JOGO;
    public static short CONFIG_START_MAP;
    public static short CONFIG_MAP_SHOP;
    public static short CONFIG_CELL_SHOP;
    public static byte MAX_CHAR_POR_CONTA;
    public static byte MAX_MULTI_CONTAS;
    public static boolean PERMITIR_MULTI_CONTA;
    public static int TIEMPO_MOVERSE_PAVOS;
    public static byte MAX_NIVEL_PROF;
    public static int TIEMPO_ARENA;
    public static int CONFIG_PRECO_FORJAR;
    public static String CONFIG_MOTD_COLOR;
    public static int BD_COMENZAR_TRANSACCION;
    public static byte SERVER_ID;
    public static int TIEMPO_PELEA;
    public static boolean CONFIG_ATIVAR_STATS_2;
    public static int TEMPO_SALVAR;
    public static int RELOGAR_ESTRELAS_MOBS;
    public static short LIMITE_JOGADORES;
    public static short CONFIG_INICIAR_NIVEL;
    public static long CONFIG_INICIAR_KAMAS;
    public static short MAX_NIVEL;
    public static short NIVEL_PA1;
    public static short MAX_PA1;
    public static short NIVEL_PM1;
    public static short MAX_PM1;
    public static boolean USAR_ZAAPS;
    public static boolean PERMITIR_PVP;
    public static boolean USAR_MOBS;
    public static boolean USAR_IP_CRIPTO;
    public static boolean AURA_ATIVADA;
    public static byte PROBABILIDADE_OBJ_ESPECIAL;
    public static byte LIMITE_ARTESAOS_OFIC;
    public static byte CANT_DROP;
    public static byte CHAPAS_MISION;
    public static byte RATE_DROP;
    public static float RATE_XP_PVP;
    public static float RATE_XP_PVM;
    public static float RATE_KAMAS;
    public static float RATE_HONOR;
    public static float RATE_XP_PROF;
    public static float RATE_PORC_FM;
    public static float RATE_FILHOTE_MOUNT;
    public static byte RATE_TEMPO_ALIMENTACAO;
    public static byte RATE_TEMPO_NASCER;
    private static float DEFECTO_XP_PVM;
    private static float DEFECTO_XP_PVP;
    private static float DEFECTO_XP_OFICIO;
    private static float DEFECTO_XP_HONOR;
    private static float DEFECTO_PORC_FM;
    private static byte DEFECTO_DROP;
    private static float DEFECTO_KAMAS;
    private static float DEFECTO_CRIANZA_PAVOS;
    private static byte DEFECTO_TIEMPO_ALIMENTACION;
    private static byte DEFECTO_TIEMPO_PARIR;
    private static String DEFECTO_EVENTO;
    public static boolean CONTRER_DDOS;
	private static BufferedReader config;

    static {
        _cerrando = false;
        _corriendo = false;
        _salvando = false;
        IP_PC_SERVER = "localhost";
        BDD_HOST = "localhost";
        BDD_USER = "root";
        BDD_PASS = "";
        BDD_STATIC = "";
        BDD_OTHER = "";
        NOME_SERVIDOR = "";
        EVENTO = "";
        LIMITE_MAPAS = (short)7411;
        MODO_HEROICO = false;
        MAPAS_TEST = "";
        MAPA_LAG = (short)7411;
        EXPULSAR = false;
        TEMPO_COMERCIO_RECRUTAMENTO = 45000L;
        MSG_BEM_VINDO_1 = "";
        MSG_BEM_VINDO_2 = "";
        TEMPO_VIP = 31536000000L;
        COR_MSG = "";
        MOSTRAR_RECIBIDOS = false;
        MOSTRAR_ENVIOS_SOS = false;
        MOSTRAR_ENVIOS_STD = false;
        CONFIG_POLICIA = false;
        CONFIG_PRECO_SCROLLAR = 6000000;
        PRECO_RECURSO = (short)200;
        ARMAS = "9544,9545,9546,9547,9548,10125,10126,10127,10133";
        INICIAR_SET_ID = -1;
        PORTA_SERVIDOR = 444;
        PORTA_JOGO = 5555;
        CONFIG_START_MAP = (short)10114;
        CONFIG_MAP_SHOP = (short)10114;
        CONFIG_CELL_SHOP = (short)282;
        MAX_CHAR_POR_CONTA = (byte)5;
        MAX_MULTI_CONTAS = (byte)3;
        PERMITIR_MULTI_CONTA = true;
        TIEMPO_MOVERSE_PAVOS = 90000;
        MAX_NIVEL_PROF = (byte)100;
        TIEMPO_ARENA = 600000;
        CONFIG_PRECO_FORJAR = 500000;
        CONFIG_MOTD_COLOR = "";
        BD_COMENZAR_TRANSACCION = 60000;
        SERVER_ID = 1;
        TIEMPO_PELEA = 30000;
        CONFIG_ATIVAR_STATS_2 = false;
        TEMPO_SALVAR = 3600000;
        RELOGAR_ESTRELAS_MOBS = 900000;
        LIMITE_JOGADORES = (short)100;
        CONFIG_INICIAR_NIVEL = 1;
        CONFIG_INICIAR_KAMAS = 100000L;
        MAX_NIVEL = (short)200;
        NIVEL_PA1 = (short)100;
        MAX_PA1 = 1;
        NIVEL_PM1 = (short)200;
        MAX_PM1 = 1;
        USAR_ZAAPS = false;
        PERMITIR_PVP = false;
        USAR_MOBS = false;
        USAR_IP_CRIPTO = false;
        AURA_ATIVADA = false;
        PROBABILIDADE_OBJ_ESPECIAL = (byte)99;
        LIMITE_ARTESAOS_OFIC = (byte)5;
        CANT_DROP = (byte)100;
        CHAPAS_MISION = 1;
        RATE_DROP = 1;
        RATE_XP_PVP = 10.0f;
        RATE_XP_PVM = 1.0f;
        RATE_KAMAS = 1.0f;
        RATE_HONOR = 1.0f;
        RATE_XP_PROF = 1.0f;
        RATE_PORC_FM = 1.0f;
        RATE_FILHOTE_MOUNT = 1.0f;
        RATE_TEMPO_ALIMENTACAO = (byte)5;
        RATE_TEMPO_NASCER = (byte)10;
        CONTRER_DDOS = false;
    }

    public static void println(String message, int color) {
        AnsiConsole.out.println("\u001b[" + color + "m" + message + "\u001b[0m");
    }

    public static void print(String message, int color) {
        AnsiConsole.out.print("\u001b[" + color + "m" + message + "\u001b[0m");
    }

    public static void clearScreen() {
        AnsiConsole.out.print("\u001b[H\u001b[2J");
    }

    public static void setTitle(String title) {
        AnsiConsole.out.printf("%c]0;%s%c", Character.valueOf('\u001b'), title, Character.valueOf('\u0007'));
    }

    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(){

            @Override
            public void run() {
                LesGuardians.cerrarServer();
            }
        });
        Consola.clear();
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("--- LES Guardians Emu 2.7.5 --- ");
        System.out.println("--- Equipe de Desenvolvimento: ---");
        System.out.println("--- EduardoLBS ---");
        System.out.println("--- Samuka ---");
        System.out.println("--- Contato: eduardo.lbs@live.com ---");
        System.out.println("--- Contato: samuel@dpbrasil.net ---");
        System.out.println("--- Decompilado por RSPAWN ---");
        System.out.println("Carregando configuracao");
        LesGuardians.setTitle("Les Guardians, Carregando...");
        LesGuardians.cargarConfiguracion();
        _estaIniciado = true;
        System.out.print("Conexao com a DB :");
        if (!SQLManager.iniciarConexion()) {
            System.out.println("Conexao invalida");
            LesGuardians.cerrarServer();
            return;
        }
        System.out.println(" Conexao OK");
        System.out.println("Criacao do servidor.");
        Mundo.crearServer();
        _corriendo = true;
        LesGuardians.DofemuStarted();
    }

    public static void DofemuStarted() {
        Consola.clear();
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("--- LES Guardians Emu 2.7.5 --- ");
        System.out.println("--- Equipe de Desenvolvimento: ---");
        System.out.println("--- EduardoLBS ---");
        System.out.println("--- Samuka ---");
        System.out.println("--- Contato: eduardo.lbs@live.com ---");
        System.out.println("--- Contato: samuel@dpbrasil.net ---");
        System.out.println("--- Versoes do emulador [JAVA]: 1.0.0, 1,5.0, 2.0.0, 2.2.0, 2.3.0, 2.3.3, 2.3.4, 2.5.0 e 2.7.5");
        System.out.print("Carregando servidor...");
        for (int i = 0; i < 40; ++i) {
            System.out.print(".");
            try {
                Thread.sleep(100L);
                continue;
            }
            catch (InterruptedException interruptedException) {}
        }
        System.out.println(". Ok");
        String Ip = "";
        try {
            Ip = InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            try {
                Thread.sleep(10000L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            System.exit(1);
        }
        Ip = IP_PC_SERVER;
        _servidorGeneral = new RealmServer();
        _servidorPersonaje = new GameServer(Ip);
        _cerrando = false;
        LesGuardians.setTitle("Les Guardians, Conectados : " + _servidorPersonaje.nroJugadoresLinea());
        Consola.println("Les Guardians ON ! A espera de conexoes...", Consola.ConsoleColorEnum.GREEN);
        Consola.println("Use (HELP ou ? para lista de comandos).", Consola.ConsoleColorEnum.YELLOW);
        new ConsolaPersonalización();
        try {
            Thread.sleep(21600000L);
        }
        catch (InterruptedException interruptedException) {
            // empty catch block
        }
        System.exit(0);
    }

    public static void ReDofemuStarted() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("--- LES Guardians Emu 2.7.5 --- ");
        System.out.println("--- Equipe de Desenvolvimento: ---");
        System.out.println("--- EduardoLBS ---");
        System.out.println("--- Samuka ---");
        System.out.println("--- Contato: eduardo.lbs@live.com ---");
        System.out.println("--- Contato: samuel@dpbrasil.net ---");
        System.out.println("--- Versoes do emulador [JAVA]: 1.0.0, 1,5.0, 2.0.0, 2.2.0, 2.3.0, 2.3.3, 2.3.4, 2.5.0 e 2.7.5");
        System.out.print("Carregando servidor...");
        System.out.print("........................................");
        System.out.println(". Ok");
        Consola.println("Les Guardians ON ! A espera de conexoes...", Consola.ConsoleColorEnum.GREEN);
        Consola.println("Use (HELP ou ? para lista de comandos).", Consola.ConsoleColorEnum.YELLOW);
        new ConsolaPersonalización();
    }

    private static void cargarConfiguracion() {
        try {
            config = new BufferedReader(new FileReader("LesGuardians.txt"));
            String linea = "";
            while ((linea = config.readLine()) != null) {
                if (linea.split("=").length == 1) continue;
                String param = linea.split("=")[0].trim();
                String valor = linea.split("=")[1].trim();
                if (param.equalsIgnoreCase("ENVIAR_SOS")) {
                    if (!valor.equalsIgnoreCase("true")) continue;
                    MOSTRAR_ENVIOS_SOS = true;
                    continue;
                }
                if (param.equalsIgnoreCase("ENVIAR_STD")) {
                    if (!valor.equalsIgnoreCase("true")) continue;
                    MOSTRAR_ENVIOS_STD = true;
                    continue;
                }
                if (param.equalsIgnoreCase("INICIAR_KAMAS")) {
                    CONFIG_INICIAR_KAMAS = Long.parseLong(valor);
                    if (CONFIG_INICIAR_KAMAS < 0L) {
                        CONFIG_INICIAR_KAMAS = 0L;
                    }
                    if (CONFIG_INICIAR_KAMAS <= 100000000L) continue;
                    CONFIG_INICIAR_KAMAS = 100000000L;
                    continue;
                }
                if (param.equalsIgnoreCase("INICIAR_NIVEL")) {
                    CONFIG_INICIAR_NIVEL = Short.parseShort(valor);
                    if (CONFIG_INICIAR_NIVEL < 1) {
                        CONFIG_INICIAR_NIVEL = 1;
                    }
                    if (CONFIG_INICIAR_NIVEL <= 200) continue;
                    CONFIG_INICIAR_NIVEL = (short)200;
                    continue;
                }
                if (param.equalsIgnoreCase("TEMPO_SALVAR")) {
                    TEMPO_SALVAR = Integer.parseInt(valor) * 60 * 1000;
                    continue;
                }
                if (param.equalsIgnoreCase("TEMPO_PARA_TRANSACAO")) {
                    BD_COMENZAR_TRANSACCION = Integer.parseInt(valor) * 1000;
                    continue;
                }
                if (param.equalsIgnoreCase("KAMAS")) {
                    DEFECTO_KAMAS = RATE_KAMAS = Float.parseFloat(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("HONOR")) {
                    DEFECTO_XP_HONOR = RATE_HONOR = Float.parseFloat(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("XP_PROF")) {
                    DEFECTO_XP_OFICIO = RATE_XP_PROF = Float.parseFloat(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("ATIVAR_STATUS_2")) {
                    if (!valor.equalsIgnoreCase("true")) continue;
                    CONFIG_ATIVAR_STATS_2 = true;
                    continue;
                }
                if (param.equalsIgnoreCase("XP_PVM")) {
                    DEFECTO_XP_PVM = RATE_XP_PVM = Float.parseFloat(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("START_MAP")) {
                    CONFIG_START_MAP = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("MAP_SHOP")) {
                    CONFIG_MAP_SHOP = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("CELL_SHOP")) {
                    CONFIG_CELL_SHOP = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("XP_PVP")) {
                    DEFECTO_XP_PVP = RATE_XP_PVP = Float.parseFloat(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("DROP")) {
                    DEFECTO_DROP = RATE_DROP = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("PORC_FM")) {
                    DEFECTO_PORC_FM = RATE_PORC_FM = Float.parseFloat(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("FILHOTE_MOUNT")) {
                    DEFECTO_CRIANZA_PAVOS = RATE_FILHOTE_MOUNT = Float.parseFloat(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("ZAAP")) {
                    if (!valor.equalsIgnoreCase("true")) continue;
                    USAR_ZAAPS = true;
                    continue;
                }
                if (param.equalsIgnoreCase("ENCRIPTAR_IP")) {
                    if (!valor.equalsIgnoreCase("true")) continue;
                    USAR_IP_CRIPTO = true;
                    continue;
                }
                if (param.equalsIgnoreCase("MSG_BEM_VINDO")) {
                    MSG_BEM_VINDO_1 = linea.split("=", 2)[1];
                    continue;
                }
                if (param.equalsIgnoreCase("EVENTO")) {
                    DEFECTO_EVENTO = EVENTO = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("COR_MSG")) {
                    COR_MSG = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("GAME_PORT")) {
                    PORTA_JOGO = Integer.parseInt(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("REALM_PORT")) {
                    PORTA_SERVIDOR = Integer.parseInt(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("MAP_TEST")) {
                    MAPAS_TEST = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("IP_SERVER")) {
                    IP_PC_SERVER = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("BDD_HOST")) {
                    BDD_HOST = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("BDD_USER")) {
                    BDD_USER = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("BDD_PASS")) {
                    BDD_PASS = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("DB_LES")) {
                    BDD_STATIC = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("DB_GUARDIANS")) {
                    BDD_OTHER = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("NOME_SERVIDOR")) {
                    NOME_SERVIDOR = valor;
                    continue;
                }
                if (param.equalsIgnoreCase("TEMPO_COMERCIO_RECRUTAMENTO")) {
                    TEMPO_COMERCIO_RECRUTAMENTO = Integer.parseInt(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("ATIVAR_MOBS")) {
                    USAR_MOBS = valor.equalsIgnoreCase("true");
                    continue;
                }
                if (param.equalsIgnoreCase("MAX_PERSO_POR_CONTA")) {
                    MAX_CHAR_POR_CONTA = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("MAX_MULTI_CONTA_IP")) {
                    MAX_MULTI_CONTAS = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("AUTORIZAR_MULTI_CONTAS")) {
                    PERMITIR_MULTI_CONTA = valor.equalsIgnoreCase("true");
                    continue;
                }
                if (param.equalsIgnoreCase("LIMITE_JOGADORES")) {
                    LIMITE_JOGADORES = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("ARENA_TIMER")) {
                    TIEMPO_ARENA = Integer.parseInt(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("ATIVAR_AURA")) {
                    AURA_ATIVADA = valor.equalsIgnoreCase("true");
                    continue;
                }
                if (param.equalsIgnoreCase("ANTI_DDOS")) {
                    CONTRER_DDOS = valor.equalsIgnoreCase("true");
                    continue;
                }
                if (param.equalsIgnoreCase("AUTORIZAR_PVP")) {
                    PERMITIR_PVP = valor.equalsIgnoreCase("true");
                    continue;
                }
                if (param.equalsIgnoreCase("MAX_LEVEL")) {
                    MAX_NIVEL = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("LEVEL_PA1")) {
                    NIVEL_PA1 = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("PRECO_FORJAR")) {
                    CONFIG_PRECO_SCROLLAR = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("PA1")) {
                    MAX_PA1 = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("LEVEL_PM1")) {
                    NIVEL_PM1 = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("PM1")) {
                    MAX_PM1 = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("LIMITE_MAP")) {
                    LIMITE_MAPAS = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("PRECO_RECURSO")) {
                    PRECO_RECURSO = Short.parseShort(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("SERVER_ID")) {
                    SERVER_ID = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("SET_INICIAL_ID")) {
                    INICIAR_SET_ID = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("TEMPO_LUTA")) {
                    TIEMPO_PELEA = Integer.parseInt(valor) * 1000;
                    continue;
                }
                if (param.equalsIgnoreCase("CANT_DROP")) {
                    CANT_DROP = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("PROB_OBJ_ESPECIAL")) {
                    PROBABILIDADE_OBJ_ESPECIAL = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("PLAQUES_MISSION")) {
                    if (Integer.parseInt(valor) <= 0) continue;
                    CHAPAS_MISION = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("TEMPS_REPAS")) {
                    DEFECTO_TIEMPO_ALIMENTACION = RATE_TEMPO_ALIMENTACAO = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("TEMPO_REPRODUCAO_MOUNT")) {
                    DEFECTO_TIEMPO_PARIR = RATE_TEMPO_NASCER = Byte.parseByte(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("TEMPO_MOUNT_MOVER")) {
                    TIEMPO_MOVERSE_PAVOS = Integer.parseInt(valor);
                    continue;
                }
                if (param.equalsIgnoreCase("PUB_1")) {
                    PUBLICIDADE.add(linea.split("=", 2)[1]);
                    continue;
                }
                if (param.equalsIgnoreCase("PUB_2")) {
                    PUBLICIDADE.add(linea.split("=", 2)[1]);
                    continue;
                }
                if (param.equalsIgnoreCase("PUB_3")) {
                    PUBLICIDADE.add(linea.split("=", 2)[1]);
                    continue;
                }
                if (param.equalsIgnoreCase("PUB_4")) {
                    PUBLICIDADE.add(linea.split("=", 2)[1]);
                    continue;
                }
                if (param.equalsIgnoreCase("PUB_5")) {
                    PUBLICIDADE.add(linea.split("=", 2)[1]);
                    continue;
                }
                if (param.equalsIgnoreCase("RECEBER")) {
                    if (!valor.equalsIgnoreCase("true")) continue;
                    MOSTRAR_RECIBIDOS = true;
                    continue;
                }
                if (param.equalsIgnoreCase("MODO_HEROICO")) {
                    if (!valor.equalsIgnoreCase("true")) continue;
                    MODO_HEROICO = true;
                    continue;
                }
                if (param.equalsIgnoreCase("TIPO_RECURSOS")) {
                    for (String str : valor.split(",")) {
                        TIPO_RECURSOS.add(Integer.parseInt(str));
                    }
                    continue;
                }
                if (param.equalsIgnoreCase("OBJETOS_NAO_PERMITIDOS")) {
                    for (String str : valor.split(",")) {
                        OBJETOS_NAO_PERMITIDOS.add(Integer.parseInt(str));
                    }
                    continue;
                }
                if (param.equalsIgnoreCase("MAPAS_PARA_MERCADOR")) {
                    for (String str : valor.split(",")) {
                        NAO_COMERCIALIZADOS_EN.add(Integer.parseInt(str));
                    }
                    continue;
                }
                if (!param.equalsIgnoreCase("TIPO_ALIMENTO_MONTARIA")) continue;
                for (String str : valor.split(",")) {
                    ALIMENTOS_MONTARIA.add(Integer.parseInt(str));
                }
            }
            if (BDD_STATIC == null || BDD_OTHER == null || BDD_HOST == null || BDD_PASS == null || BDD_USER == null) {
                throw new Exception();
            }
            for (String str : ARMAS.split(",")) {
                ARMAS_ENCARNACAO.add(Integer.parseInt(str));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Config invisivel ou invalida.");
            System.out.println("Servidor OFF");
            System.exit(1);
        }
    }

    public static void cerrarServer() {
        System.out.println("Fechando servidor ...");
        if (_corriendo) {
            _corriendo = false;
            Mundo.salvarServidor();
            _servidorPersonaje.cerrarServidor();
            SQLManager.cerrarConexion();
        }
        System.out.println("Servidor fechado: OK");
        _corriendo = false;
    }

    public static void resetRates() {
        RATE_XP_PVM = DEFECTO_XP_PVM;
        RATE_XP_PVP = DEFECTO_XP_PVP;
        RATE_XP_PROF = DEFECTO_XP_OFICIO;
        RATE_PORC_FM = DEFECTO_PORC_FM;
        RATE_HONOR = DEFECTO_XP_HONOR;
        RATE_DROP = DEFECTO_DROP;
        RATE_KAMAS = DEFECTO_KAMAS;
        RATE_TEMPO_ALIMENTACAO = DEFECTO_TIEMPO_ALIMENTACION;
        RATE_FILHOTE_MOUNT = DEFECTO_CRIANZA_PAVOS;
        RATE_TEMPO_NASCER = DEFECTO_TIEMPO_PARIR;
        EVENTO = DEFECTO_EVENTO;
    }
}

