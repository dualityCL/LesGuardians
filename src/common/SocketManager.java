package common;

import common.CryptManager;
import common.LesGuardians;
import common.Mundo;
import game.GameServer;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Random;
import objects.Cofre;
import objects.Recaudador;
import objects.Cuenta;
import objects.Dragopavo;
import objects.Combate;
import objects.Gremio;
import objects.MobModelo;
import objects.Mapa;
import objects.Mapa.Celda;
import objects.Mapa.ObjetoInteractivo;
import objects.Mercadillo;
import objects.NPCModelo;
import objects.Objeto;
import objects.Personaje;
import objects.Prisma;
import objects.Oficio;

public class SocketManager {
    public static void enviar(Personaje perso, String packet) {
        if (perso == null || !perso.enLinea() || perso.getCuenta() == null || perso.getCuenta().getEntradaPersonaje() == null) {
            return;
        }
        PrintWriter out = perso.getCuenta().getEntradaPersonaje().getOut();
        if (out != null && !packet.equals("") && !packet.equals("\u0000")) {
            packet = CryptManager.aUTF(packet);
            out.print(String.valueOf(packet) + '\u0000');
            out.flush();
        }
    }

    public static void enviar(PrintWriter out, String packet) {
        if (out != null && !packet.equals("") && !packet.equals("\u0000")) {
            packet = CryptManager.aUTF(packet);
            out.print(String.valueOf(packet) + '\u0000');
            out.flush();
        }
    }

    public static void ENVIAR_HG_SALUDO_JUEGO_GENERAL(PrintWriter out) {
        String packet = "HG";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("SALUTATION JOUEUR: OUT>>  " + packet);
        }
    }

    public static void REALM_SEND_REQUIRED_APK(PrintWriter out) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String pass = "";
        for (int x = 0; x < 5; ++x) {
            int i = (int)Math.floor(Math.random() * 26.0);
            pass = String.valueOf(pass) + chars.charAt(i);
        }
        String packet = "APK" + pass;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_MESSAGE_ANNOUNCE(String msg, int Rank1, String Name2) {
        String Rank = "";
        if (Rank1 == 1) {
            Rank = "[VIP]  ";
        }
        if (Rank1 == 2) {
            Rank = "[Moderador] ";
        }
        if (Rank1 == 3) {
            Rank = "[Game Master]";
        }
        if (Rank1 == 4) {
            Rank = "[Programador]";
        }
        if (Rank1 == 5) {
            Rank = "[Administrador]";
        }
        if (Rank1 == 6) {
            Rank = " [Fundador] ";
        }
        String packet = "Im116;<b><font color='#000'>[Aviso]</font></b><b><font color='#000'>" + Rank + "</font></b>" + "<b><font color='#CC0000'><a href='asfunction:onHref,ShowPlayerPopupMenu," + Name2 + "'>[" + Name2 + "]</a></font></b>" + "<font color='#000'>~" + msg + "</font>";
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(perso, packet);
        }
    }

    public static void GAME_SEND_MESSAGE_VIP(String msg, int Rank1, String Name2) {
        String Rank = "";
        if (Rank1 == 1) {
            Rank = "[VIP]  ";
        }
        if (Rank1 == 2) {
            Rank = "[Moderador] ";
        }
        if (Rank1 == 3) {
            Rank = "[Game Master]";
        }
        if (Rank1 == 4) {
            Rank = "[Programador]";
        }
        if (Rank1 == 5) {
            Rank = "[Administrador]";
        }
        if (Rank1 == 6) {
            Rank = " [Fundador] ";
        }
        String packet = "Im116;<b><font color='#0000'>[Canal Vip]</font></b><b><font color='#000066'>" + Rank + "</font></b>" + "<b><font color='#1E90FF'><a href='asfunction:onHref,ShowPlayerPopupMenu," + Name2 + "'>[" + Name2 + "]</a></font></b>" + "<font color='#000'>~" + msg + "</font>";
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(perso, packet);
        }
    }

    public static void GAME_SEND_MESSAGE_GLOBAL(String msg, int Rank1, String Name2) {
        String Rank = "";
        if (Rank1 == 1) {
            Rank = "[VIP]  ";
        }
        if (Rank1 == 2) {
            Rank = "[Moderador] ";
        }
        if (Rank1 == 3) {
            Rank = "[Game Master]";
        }
        if (Rank1 == 4) {
            Rank = "[Programador]";
        }
        if (Rank1 == 5) {
            Rank = "[Administrador]";
        }
        if (Rank1 == 6) {
            Rank = " [Fundador] ";
        }
        String packet = "Im116;<b><font color='#000'>[Global]</font></b><b><font color='#000'>" + Rank + "</font></b>" + "<b><font color='#fff'><a href='asfunction:onHref,ShowPlayerPopupMenu," + Name2 + "'>[" + Name2 + "]</a></font></b>" + "<font color='#000000'>~" + msg + "</font>";
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(perso, packet);
        }
    }

    public static void GAME_SEND_MESSAGE_EVENTOS(String msg, int Rank1, String Name2) {
        String Rank = "";
        if (Rank1 == 1) {
            Rank = "[VIP]  ";
        }
        if (Rank1 == 2) {
            Rank = "[GM] ";
        }
        if (Rank1 == 3) {
            Rank = "[Programador]";
        }
        if (Rank1 == 4) {
            Rank = " [Administrador] ";
        }
        if (Rank1 == 5) {
            Rank = " [Fundador] ";
        }
        String packet = "Im116;<b><font color='#0000FF'>[Evento]</font></b><b><font color='#0000'>" + Rank + "</font></b>" + "<b><font color='#228B22'><a href='asfunction:onHref,ShowPlayerPopupMenu," + Name2 + "'>[" + Name2 + "]</a></font></b>" + "<font color='#000'>~" + msg + "</font>";
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(perso, packet);
        }
    }

    public static String ENVIAR_HC_CODIGO_LLAVE(PrintWriter out) {
        String alfabeto = "abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random();
        String codigoLlave = "";
        for (int i = 0; i < 32; ++i) {
            codigoLlave = String.valueOf(codigoLlave) + alfabeto.charAt(rand.nextInt(alfabeto.length()));
        }
        String packet = "HC" + codigoLlave;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CODE ENTRE: OUT>>" + packet);
        }
        return codigoLlave;
    }

    public static void ENVIAR_AlEv_VERSION_DEL_CLIENTE(PrintWriter out) {
        String packet = "AlEv1.29.1";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("VERSION CLIENT: CONNEXION>>" + packet);
        }
    }

    public static void ENVIAR_AlEf_LOGIN_ERROR(PrintWriter out) {
        String packet = "AlEf";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LOGIN ERROR: CONNEXION>>" + packet);
        }
    }

    public static void ENVIAR_Af_ABONADOS_POSCOLA(PrintWriter out, int posicion, int totalAbo, int totalNonAbo, String subscribe, int colaID) {
        String packet = "Af" + posicion + "|" + totalAbo + "|" + totalNonAbo + "|" + subscribe + "|" + colaID;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MULTIPAQUET: CONNEXION>>" + packet);
        }
    }

    public static void ENVIAR_Ad_Ac_AH_AlK_AQ_INFO_CUENTA_Y_SERVER(PrintWriter out, String apodo, int nivel, String pregunta) {
        String packet = "Ad" + apodo + '\u0000';
        packet = String.valueOf(packet) + "Ac0\u0000";
        packet = String.valueOf(packet) + "AH" + LesGuardians.SERVER_ID + ";" + Mundo.getEstado() + ";110;1" + '\u0000';
        packet = String.valueOf(packet) + "AlK" + nivel + '\u0000';
        packet = String.valueOf(packet) + "AQ" + pregunta.replace(" ", "+");
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CONNEXION: OUT>>" + packet);
        }
    }

    public static void ENVIAR_AlEb_CUENTA_BANEADA(PrintWriter out) {
        String packet = "AlEb";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("COMPTE BAN: CONEXION>>" + packet);
        }
    }

    public static void ENVIAR_AlEc_MISMA_CUENTA_CONECTADA(PrintWriter out) {
        String packet = "AlEc";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MEME COMPTE CONNECTE: CONEXION>>" + packet);
        }
    }

    public static void ENVIAR_XML_POLICIA(PrintWriter out) {
        String packet = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" secure=\"false\" /><site-control permitted-cross-domain-policies=\"master-only\" /></cross-domain-policy>";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("XML POLICE: OUT>>  " + packet);
        }
    }

    public static void ENVIAR_AxK_TIEMPO_ABONADO_NRO_PJS(PrintWriter out, int nroPersonajes) {
        String packet = "AxK" + LesGuardians.TEMPO_VIP;
        if (nroPersonajes > 0) {
            packet = String.valueOf(packet) + "|" + LesGuardians.SERVER_ID + "," + nroPersonajes;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("TEMPS D'ABONNEMENT: OUT>>" + packet);
        }
    }

    public static void ENVIAR_AXK_O_AYK_IP_SERVER(PrintWriter out, int cuentaID, boolean esHost) {
        String packet = "A";
        if (LesGuardians.USAR_IP_CRIPTO) {
            String ip = esHost ? String.valueOf(CryptManager.encriptarIP("127.0.0.1")) + CryptManager.encriptarPuerto(LesGuardians.PORTA_JOGO) : LesGuardians.CRYPTER_IP;
            packet = String.valueOf(packet) + "XK" + ip + cuentaID;
        } else {
            String ip = esHost ? "127.0.0.1" : LesGuardians.IP_PC_SERVER;
            packet = String.valueOf(packet) + "YK" + ip + ":" + LesGuardians.PORTA_JOGO + ";" + cuentaID;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CONENXION IP SERVER: OUT>>" + packet);
        }
    }

    public static void ENVIAR_AT_TICKET_FALLIDA(PrintWriter out) {
        String packet = "ATE";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR TICKET: OUT>>  " + packet);
        }
    }

    public static void ENVIAR_AT_TICKET_A_CUENTA(PrintWriter out) {
        String packet = "ATK0";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("TICKET SUR COMPTE: OUT>>  " + packet);
        }
    }

    public static void ENVIAR_AV_VERSION_REGIONAL(PrintWriter out) {
        String packet = "AV0";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("VERSION DE REGION: OUT>>  " + packet);
        }
    }

    public static void ENVIAR_APE2_GENERAR_NOMBRE_RANDOM(PrintWriter out) {
        String packet = "APE2";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GENERER UN NOM: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ALK_LISTA_DE_PERSONAJES(PrintWriter out, Cuenta cuenta) {
        String packet = "ALK" + LesGuardians.TEMPO_VIP + "|" + cuenta.getPersonajes().size();
        for (Personaje perso : cuenta.getPersonajes().values()) {
            packet = String.valueOf(packet) + perso.stringParaListaPJsServer();
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTE DE PERSONNAGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_AAEa_NOMBRE_YA_EXISTENTE(PrintWriter out) {
        String packet = "AAEa";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR NOM EXISTANT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_AAEf_MAXIMO_PJS_CREADOS(PrintWriter out) {
        String packet = "AAEf";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR MAX PERSO CREER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_AAK_CREACION_PJ(PrintWriter out) {
        String packet = "AAK";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CREATION PERSONNAGE OK: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ADE_ERROR_BORRAR_PJ(PrintWriter out) {
        String packet = "ADE";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR SUPPRESSION DU PERSONNAGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_AAEF_ERROR_CREAR_PJ(PrintWriter out) {
        String packet = "AAEF";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR CREATION PERSONNAGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ASE_SELECCION_PERSONAJE_FALLIDA(PrintWriter out) {
        String packet = "ASE";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR CHOIX PERSONNAGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ASK_PERSONAJE_SELECCIONADO(PrintWriter out, Personaje perso) {
        String packet = "ASK|" + perso.getID() + "|" + perso.getNombre() + "|" + perso.getNivel() + "|" + perso.getClase(false) + "|" + perso.getSexo() + "|" + perso.getGfxID() + "|" + (perso.getColor1() == -1 ? "-1" : Integer.toHexString(perso.getColor1())) + "|" + (perso.getColor2() == -1 ? "-1" : Integer.toHexString(perso.getColor2())) + "|" + (perso.getColor3() == -1 ? "-1" : Integer.toHexString(perso.getColor3())) + "|" + perso.stringPersonajeElegido();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("PERSONNAGE CHOISI: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ZS_ENVIAR_ALINEACION(PrintWriter out, int alineacionID) {
        String packet = "ZS" + alineacionID;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ENVOYER ALIGNEMENT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_cC_ACTIVAR_CANALES(PrintWriter out, String canal) {
        String packet = "cC+" + canal;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACTIVER CANAL: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_al_ESTADO_ZONA_ALINEACION(PrintWriter out) {
        String packet = "al|" + Mundo.getAlineacionTodasSubareas();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ALIGNEMENT SOUS-ZONES: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_SLo_MOSTRAR_TODO_HECHIZOS(PrintWriter out, boolean mostrar) {
        String packet = "SLo" + (mostrar ? "+" : "-");
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MONTRER TOUS LES SORTS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_AR_RESTRICCIONES_PERSONAJE(PrintWriter out, String str) {
        String packet = "AR" + str;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("RESTRICTION: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Ow_PODS_DEL_PJ(Personaje perso) {
        String packet = "Ow" + perso.getPodUsados() + "|" + perso.getMaxPod();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PODS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_FO_MOSTRAR_CONEXION_AMIGOS(PrintWriter out, boolean mostrar) {
        String packet = "FO" + (mostrar ? "+" : "-");
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MONTRER LA CONNEXION DES AMIS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GCK_CREAR_PANTALLA_PJ(PrintWriter out) {
        String packet = "GCK|1|";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CREER ECRAN: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_BT_TIEMPO_SERVER(PrintWriter out) {
        String packet = GameServer.getTiempoServer();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("TEMPS SERVEUR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_BD_FECHA_SERVER(PrintWriter out) {
        String packet = GameServer.getFechaServer();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DATE SERVEUR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GDM_CAMBIO_DE_MAPA(PrintWriter out, int id, String fecha, String key) {
        String packet = "GDM|" + id + "|" + fecha + "|" + key;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHANGEMENT DE MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GDE_FRAME_OBJECT_EXTERNAL(Personaje perso, String str) {
        String packet = "GDE|" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CADRE OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GDE_FRAME_OBJECT_EXTERNAL(Mapa mapa, String str) {
        String packet = "GDE|" + str;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CADRE OBJET: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GDK_CARGAR_MAPA(PrintWriter out) {
        String packet = "GDK";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHARGEMENT DE LA MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_GRUPOMOBS(PrintWriter out, Mapa mapa) {
        String packet = mapa.getGMsGrupoMobs();
        if (packet.isEmpty()) {
            return;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GROUPE DE MONSTRE SUR MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GDO_OBJETOS_CRIAS_EN_MAPA(PrintWriter out, Mapa mapa) {
        String packet = mapa.getObjetosCria();
        if (packet.isEmpty()) {
            return;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_NPCS(PrintWriter out, Mapa mapa) {
        String packet = mapa.getGMsNPCs();
        if (packet.isEmpty()) {
            return;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MONTRER NPC MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_RECAUDADORES(PrintWriter out, Mapa mapa) {
        String packet = Recaudador.enviarGMDeRecaudador(mapa);
        if (packet.isEmpty()) {
            return;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MONTRER PERCEPTEUR MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_PERSONAJES(PrintWriter out, Mapa mapa) {
        String packet = mapa.getGMsPackets();
        if (packet.isEmpty()) {
            return;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PERSONNAGE SUR LA MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_MERCANTES(PrintWriter out, Mapa mapa) {
        String packet = mapa.getGMsMercantes();
        if (packet.isEmpty()) {
            return;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MARCHANT SUR LA MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_PRISMAS(PrintWriter out, Mapa mapa) {
        String packet = mapa.getGMsPrismas();
        if (packet.isEmpty()) {
            return;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PRISME SUR LA MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_MONTURAS(PrintWriter out, Mapa mapa) {
        String packet = mapa.getGMsMonturas();
        if (packet.isEmpty()) {
            return;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DRAGODINDE SUR LA MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_BORRAR_PJ_A_TODOS(Mapa mapa, int id) {
        String packet = "GM|-" + id;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("SUPPRESSION NPC: MAP ID " + mapa.getID() + ": MAP>>" + packet);
        }
    }

    public static void ENVIAR_GM_BORRAR_LUCHADOR(Combate pelea, int id, int equipos) {
        String packet = "GM|-" + id;
        for (Combate.Luchador luchador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso = luchador.getPersonaje();
            if (perso == null || perso.getID() == id) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MONTRER: CELL ID " + pelea.getID() + ": FIGHT>>" + packet);
        }
    }

    public static void ENVIAR_GM_AGREGAR_PJ_A_TODOS(Mapa mapa, Personaje perso) {
        String packet = "GM|+" + perso.stringGM();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AGRESSION PERSONNAGE: MAP ID " + mapa.getID() + ": MAP>>" + packet);
        }
    }

    public static void ENVIAR_GM_GRUPMOBS(Mapa mapa) {
        String packet = mapa.getGMsGrupoMobs();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GROUPE DE MONSTRE: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GM_GRUPOMOB_A_MAPA(Mapa mapa, MobModelo.GrupoMobs grupoMobs) {
        String packet = "GM|";
        packet = String.valueOf(packet) + grupoMobs.enviarGM();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("ENVOYER GROUPE DE MONSTRE: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GM_PERSONAJE_A_MAPA(Mapa mapa, Personaje perso) {
        String packet = mapa.getGMsPackets();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PERSONNAGE: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GM_DRAGOPAVO_A_MAPA(Mapa mapa, Dragopavo dragopavo) {
        String packet = dragopavo.getCriarMontura(mapa.getCercado());
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DRAGODINDE: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GM_MERCANTE_A_MAPA(Mapa mapa, String packet) {
        if (packet.isEmpty()) {
            return;
        }
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MARCHANT: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GM_PRISMA_A_MAPA(Mapa mapa, Prisma prisma) {
        String packet = prisma.getGMPrisma();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PRISME: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GA903_UNIRSE_PELEA_Y_ESTAR_OCUPADO(PrintWriter out, int id) {
        String packet = "GA;903;" + id + ";o";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("UNIR PELEA OCUPADO: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GA903_UNIRSE_PELEA_Y_OPONENTE_OCUPADO(PrintWriter out, int id) {
        String packet = "GA;903;" + id + ";z";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("UNIR PELEA OPON OCUPADO: ENVIAR>>  " + packet);
        }
    }

    public static void ENVIAR_GA900_DESAFIAR(Mapa mapa, int id, int id2) {
        String packet = "GA;900;" + id + ";" + id2;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEFIER: MAP ID " + mapa.getID() + ": MAP>>" + packet);
        }
    }

    public static void ENVIAR_GA902_RECHAZAR_DESAFIO(Mapa mapa, int id, int id2) {
        String packet = "GA;902;" + id + ";" + id2;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("REFUSER DEFI: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GA901_ACEPTAR_DESAFIO(Mapa mapa, int id, int id2) {
        String packet = "GA;901;" + id + ";" + id2;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACCEPTER DEFI: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_fC_CANTIDAD_DE_PELEAS(PrintWriter out, Mapa mapa) {
        String packet = "fC" + mapa.getNumeroPeleas();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("QUANTITE COMBAT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_fC_CANTIDAD_DE_PELEAS(Mapa mapa) {
        String packet = "fC" + mapa.getNumeroPeleas();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("QUANTITE COMBAT: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GJK_UNIRSE_PELEA(Personaje perso, int estado, boolean botonCancelar, boolean mostrarBotones, boolean espectador, int tiempo, int tipoPelea) {
        String packet = "GJK" + estado + "|" + (botonCancelar ? 1 : 0) + "|" + (mostrarBotones ? 1 : 0) + "|" + (espectador ? 1 : 0) + "|" + tiempo + "|" + tipoPelea;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("UNIRSE PELEA: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GJK_UNIRSE_PELEA(Combate pelea, int equipos, int estado, boolean botonCancelar, boolean mostrarBotones, boolean espectador, int tiempo, int tipoPelea) {
        String packet = "GJK" + estado + "|" + (botonCancelar ? 1 : 0) + "|" + (mostrarBotones ? 1 : 0) + "|" + (espectador ? 1 : 0) + "|" + tiempo + "|" + tipoPelea;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("UNIRSE PELEA: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GP_POSICIONES_PELEA(PrintWriter out, String posiciones, int equipo) {
        String packet = "GP" + posiciones + "|" + equipo;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("POISTIONS CASES DE COMBAT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GP_POSICIONES_PELEA(Combate pelea, int equipos, String posiciones, int equipo) {
        String packet = "GP" + posiciones + "|" + equipo;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("POSITIONS CASES: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_Gc_MOSTRAR_ESPADA_EN_MAPA(Mapa mapa, int arg1, int id1, int id2, int cell1, String str1, int cell2, String str2) {
        String packet = "Gc+" + id1 + ";" + arg1 + "|" + id1 + ";" + cell1 + ";" + str1 + "|" + id2 + ";" + cell2 + ";" + str2;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MOSTRAR ESPADA: MAPA>>  " + packet);
        }
    }

    public static void ENVIAR_Gc_MOSTRAR_ESPADA_A_JUGADOR(Personaje perso, int arg1, int id1, int id2, int celda1, String str1, int celda2, String str2) {
        String packet = "Gc+" + id1 + ";" + arg1 + "|" + id1 + ";" + celda1 + ";" + str1 + "|" + id2 + ";" + celda2 + ";" + str2;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MOSTRAR ESPADA: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Gc_BORRAR_ESPADA_EN_MAPA(Mapa mapa, int id) {
        String packet = "Gc-" + id;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("BORRAR ESPADA: MAPA>>  " + packet);
        }
    }

    public static void ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(Mapa mapa, int idInit1, Combate.Luchador luchador) {
        String packet = "Gt" + idInit1 + "|+" + luchador.getID() + ";" + luchador.getNombreLuchador() + ";" + luchador.getNivel();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AGREGAR NOMBRE ESPADA: MAPA>>  " + packet);
        }
    }

    public static void ENVIAR_Gt_AGREGAR_NOMBRE_ESPADA(Personaje perso, int idInit1, String str) {
        String packet = "Gt" + idInit1 + "|+" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AGREGAR NOMBRE ESPADA: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Gt_BORRAR_NOMBRE_ESPADA(Mapa mapa, int idInit1, Combate.Luchador luchador) {
        String packet = "Gt" + idInit1 + "|-" + luchador.getID();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("BORRAR NOMBRE ESPADA: MAPA>>  " + packet);
        }
    }

    public static void ENVIAR_GDO_PONER_OBJETO_CRIA(Mapa mapa, String str) {
        String packet = "GDO+" + str;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PONER OBJ CRIA: MAPA>>  " + packet);
        }
    }

    public static void ENVIAR_Oa_CAMBIAR_ROPA(Mapa mapa, Personaje perso) {
        String packet = perso.analizarFiguraDelPJ();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CAMBIAR ROPA: MAPA>>  " + packet);
        }
    }

    public static void ENVIAR_Oa_CAMBIAR_ROPA_PELEA(Personaje perso, Combate pelea) {
        String packet = perso.analizarFiguraDelPJ();
        for (Combate.Luchador luchador : pelea.luchadoresDeEquipo(3)) {
            Personaje perso1;
            if (luchador.estaRetirado() || (perso1 = luchador.getPersonaje()) == null || !perso1.enLinea()) continue;
            SocketManager.enviar(perso1, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CAMBIAR ROPA: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GIC_CAMBIAR_POS_PELEA(Combate pelea, int equipos, Mapa mapa, int id, int celda) {
        String packet = "GIC|" + id + ";" + celda;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CAMBIAR POS PELEA: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_Go_BOTON_ESPEC_AYUDA(Mapa mapa, char s, char opcion, int id) {
        String packet = "Go" + s + opcion + id;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("BOT. ESPEC. AYUDA: MAPA>>  " + packet);
        }
    }

    public static void ENVIAR_GR_TODOS_LUCHADORES_LISTOS(Combate pelea, int equipos, int id, boolean b) {
        String packet = "GR" + (b ? "1" : "0") + id;
        if (pelea.getEstado() != 2) {
            return;
        }
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LUCHADORES LISTO: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_Im_INFORMACION_A_TODOS(String str) {
        String packet = "Im" + str;
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFORMACION: ALL>>  " + packet);
        }
    }

    public static void ENVIAR_Im_INFORMACION(PrintWriter out, String str) {
        String packet = "Im" + str;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFORMACION: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Im_INFORMACION(Personaje perso, String str) {
        String packet = "Im" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFORMACION: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ILS_TIEMPO_REGENERAR_VIDA(Personaje perso, int tiempoRegen) {
        String packet = "ILS" + tiempoRegen;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("TEMPS REGENERATION VITA: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ILF_CANTIDAD_DE_VIDA(Personaje perso, int cantidad) {
        String packet = "ILF" + cantidad;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("IMPOSSIBLE DE REGENERER LA VIE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ILS_TIEMPO_REGENERAR_VIDA(PrintWriter out, int tiempoRegen) {
        String packet = "ILS" + tiempoRegen;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("TEMPS REGENERATION VITALITE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ILF_CANTIDAD_DE_VIDA(PrintWriter out, int cantidad) {
        String packet = "ILF" + cantidad;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("IMPOSSIBLE DE REGENERER LA VIE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Im_INFORMACION_A_MAPA(Mapa mapa, String id) {
        String packet = "Im" + id;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFORMACION: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_eUK_EMOTE_MAPA(Mapa mapa, int id, int emote, String tiempo) {
        String packet = "eUK" + id + "|" + emote + "|" + tiempo;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("EMOTE: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_Im_INFORMACION_A_PELEA(Combate pelea, int equipos, String msj) {
        String packet = "Im" + msj;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFORMACION: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_cs_CHAT_MENSAJE(Personaje perso, String msj, String color) {
        String packet = "cs<font color='#" + color + "'>" + msj + "</font>";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_cs_CHAT_MENSAJE_A_MAPA(Mapa mapa, String msj, String color) {
        String packet = "cs<font color='#" + color + "'>" + msj + "</font>";
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GA903_ERROR_PELEA(PrintWriter out, char c) {
        String packet = "GA;903;;" + c;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR JEU: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GIC_UBICACION_LUCHADORES_INICIAR(Combate pelea, int equipos) {
        String packet = "GIC|";
        for (Combate.Luchador p : pelea.luchadoresDeEquipo(3)) {
            if (p.getCeldaPelea() == null) continue;
            packet = String.valueOf(packet) + p.getID() + ";" + p.getCeldaPelea().getID() + "|";
        }
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("UBIC LUCH INICIAR: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GIC_APARECER_LUCHADORES_INVISIBLES(Combate pelea, int equipos, Combate.Luchador luchador) {
        String packet = "GIC|" + luchador.getID() + ";" + luchador.getCeldaPelea().getID() + "|";
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("APARECER LUCH INVI: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GIC_APARECER_LUCHADORES_INVISIBLES(Combate pelea, Combate.Luchador luchador, Personaje perso) {
        String packet = "GIC|" + luchador.getID() + ";" + luchador.getCeldaPelea().getID() + "|";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("APARECER LUCH INVI: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GS_EMPEZAR_COMBATE_EQUIPOS(Combate pelea, int equipos) {
        String packet = "GS";
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INICIAR PELEA: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GS_EMPEZAR_COMBATE(Personaje perso) {
        String packet = "GS";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INICIO PELEA: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GTL_ORDEN_JUGADORES(Combate pelea, int equipos) {
        String packet = pelea.stringOrdenJugadores();
        for (Combate.Luchador l : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (l.estaRetirado() || (perso = l.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ORDEN LUCH: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GTL_ORDEN_JUGADORES(Personaje perso, Combate pelea) {
        String packet = pelea.stringOrdenJugadores();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ORDEN LUCH: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GTM_INFO_STATS_TODO_LUCHADORES(Combate pelea, int equipos) {
        String packet = "GTM";
        for (Combate.Luchador luchador : pelea.luchadoresDeEquipo(3)) {
            packet = String.valueOf(packet) + "|" + luchador.getID() + ";";
            if (luchador.estaMuerto()) {
                packet = String.valueOf(packet) + "1";
                continue;
            }
            packet = String.valueOf(packet) + "0;" + luchador.getPDVConBuff() + ";";
            int PA = luchador.getPAConBuff();
            packet = PA < 0 ? String.valueOf(packet) + "0;" + luchador.getPMConBuff() + ";" : String.valueOf(packet) + PA + ";" + luchador.getPMConBuff() + ";";
            packet = luchador.getCeldaPelea() == null ? String.valueOf(packet) + "-1" : String.valueOf(packet) + (luchador.esInvisible() ? "-1" : Integer.valueOf(luchador.getCeldaPelea().getID()));
            packet = String.valueOf(packet) + ";;" + luchador.getPDVMaxConBuff();
        }
        for (Combate.Luchador luchador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (luchador.estaRetirado() || (perso = luchador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFO STATS LUCH: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GTM_INFO_STATS_TODO_LUCHADORES(Combate pelea, Personaje perso) {
        String packet = "GTM";
        for (Combate.Luchador luchador : pelea.luchadoresDeEquipo(3)) {
            packet = String.valueOf(packet) + "|" + luchador.getID() + ";";
            if (luchador.estaMuerto()) {
                packet = String.valueOf(packet) + "1";
                continue;
            }
            packet = String.valueOf(packet) + "0;" + luchador.getPDVConBuff() + ";";
            int PA = luchador.getPAConBuff();
            packet = PA < 0 ? String.valueOf(packet) + "0;" + luchador.getPMConBuff() + ";" : String.valueOf(packet) + PA + ";" + luchador.getPMConBuff() + ";";
            packet = luchador.getCeldaPelea() == null ? String.valueOf(packet) + "-1" : String.valueOf(packet) + (luchador.esInvisible() ? "-1" : Integer.valueOf(luchador.getCeldaPelea().getID()));
            packet = String.valueOf(packet) + ";;" + luchador.getPDVMaxConBuff();
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFO STATS LUCH: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GTS_INICIO_TURNO_PELEA(Combate pelea, int equipos, int id, int tiempo) {
        String packet = "GTS" + id + "|" + tiempo;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEBUT TOUR: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_GTS_INICIO_TURNO_PELEA(Personaje perso, int id, int tiempo) {
        String packet = "GTS" + id + "|" + tiempo;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEBUT TOUR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(Personaje perso) {
        String packet = "GV";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("RESETEAR PANTALLA JUEGO: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_pong(PrintWriter out) {
        String packet = "pong";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DOFUS PONG: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_qpong(PrintWriter out) {
        String packet = "qpong";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DOFUS QPONG: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GAS_INICIO_DE_ACCION(Combate pelea, int equipos, int id) {
        String packet = "GAS" + id;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEBUT ACTION: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_GA_ACCION_PELEA(Combate pelea, int equipos, int accionID, String s1, String s2) {
        String packet = "GA;" + accionID + ";" + s1;
        if (!s2.equals("")) {
            packet = String.valueOf(packet) + ";" + s2;
        }
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CELL ACTION: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GA_ACCION_DE_JUEGO(PrintWriter out, String respuestaID, String s0, String s1, String s2) {
        String packet = "GA" + respuestaID + ";" + s0;
        if (!s1.equals("")) {
            packet = String.valueOf(packet) + ";" + s1;
        }
        if (!s2.equals("")) {
            packet = String.valueOf(packet) + ";" + s2;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME ACTION: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GA_ACCION_PELEA_CON_RESPUESTA(Combate pelea, int equipos, int respuestaID, String s1, String s2, String s3) {
        String packet = "GA" + respuestaID + ";" + s1 + ";" + s2 + ";" + s3;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACCION PELEA CON RESP.: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GAMEACTION_A_PELEA(Combate pelea, int equipos, String packet) {
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAMEACTION: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GAF_FINALIZAR_ACCION(Combate pelea, int equipos, int i1, int id) {
        String packet = "GAF" + i1 + "|" + id;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("TERMINER ACTION: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_BN_NADA(PrintWriter out) {
        String packet = "BN";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("NADA: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_BN_NADA(Personaje perso) {
        String packet = "BN";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("NADA: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GTF_FIN_DE_TURNO(Combate pelea, int equipos, int id) {
        String packet = "GTF" + id;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("FIN TOUR: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_GTR_TURNO_LISTO(Combate pelea, int equipos, int id) {
        String packet = "GTR" + id;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTE DES TOURS: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_cS_EMOTICON_MAPA(Mapa mapa, int id, int pid) {
        String packet = "cS" + id + "|" + pid;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("EMOTE: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_SUE_SUBIR_NIVEL_HECHIZO_ERROR(PrintWriter out) {
        String packet = "SUE";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("IMPOSSIBLE DE SUBIR SORT: OUT>>  " + packet);
        }
    }

    public static void ENVIAR_SUK_SUBIR_NIVEL_HECHIZO(PrintWriter out, int hechizoID, int nivel) {
        String packet = "SUK" + hechizoID + "~" + nivel;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("SUBIR NIVEL HECHIZOS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_SL_LISTA_HECHIZOS(Personaje perso) {
        String packet = "SL" + perso.stringListaHechizos();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTE SORT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GA103_JUGADOR_MUERTO(Combate pelea, int equipos, int id) {
        String packet = "GA;103;" + id + ";" + id;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MORT: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_GE_PANEL_RESULTADOS_PELEA(Combate pelea, int equipos, String packet) {
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PANEL RESULTAT COMBAT: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_GIE_EFECTO_HECHIZO(Combate pelea, int equipos, int tipo, int objetivo, int mParam1, String mParam2, String mParam3, String mParam4, int turnos, int hechizoID) {
        String packet = "GIE" + tipo + ";" + objetivo + ";" + mParam1 + ";" + mParam2 + ";" + mParam3 + ";" + mParam4 + ";" + turnos + ";" + hechizoID;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("EFFET DE SORT: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GM_LUCHADOR_A_TODA_PELEA(Combate pelea, int equipos, Mapa mapa) {
        String packet = mapa.getGMsLuchadores();
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GM LUCHADOR ENTRA PELEA: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GM_LUCHADORES(Combate pelea, Mapa mapa, Personaje perso) {
        String packet = mapa.getGMsLuchadores();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GM LUCHADORES: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_JUGADO_UNIRSE_PELEA(Combate pelea, int equipos, Combate.Luchador luchador) {
        String packet = "GM|+" + luchador.stringGM();
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador == luchador || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LUCH UNIR PELEA: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_fL_LISTA_PELEAS(PrintWriter out, Mapa mapa) {
        String packet = "fL";
        boolean primero = true;
        try {
            for (Map.Entry<Short, Combate> entry : mapa.getPeleas().entrySet()) {
                String info;
                if (!primero) {
                    packet = String.valueOf(packet) + "|";
                }
                if ((info = entry.getValue().infoPeleasEnMapa()).isEmpty()) continue;
                packet = String.valueOf(packet) + info;
                primero = false;
            }
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTAGE SORT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_cMK_CHAT_MENSAJE_PERSONAJE(Personaje perso, String sufijo, int id, String nombre, String msj) {
        String packet = "cMK" + sufijo + "|" + id + "|" + nombre + "|" + msj;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_cMK_CHAT_MENSAJE_MAPA(Mapa mapa, String sufijo, int id, String nombre, String msj) {
        String packet = "cMK" + sufijo + "|" + id + "|" + nombre + "|" + msj;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_cMK_CHAT_MENSAJE_GREMIO(Gremio gremio, String sufijo, int id, String nombre, String msj) {
        String packet = "cMK" + sufijo + "|" + id + "|" + nombre + "|" + msj;
        for (Personaje perso : gremio.getPjMiembros()) {
            if (perso == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT: GUILD>" + packet);
        }
    }

    public static void ENVIAR_cMK_CHAT_MENSAJE_TODOS(String sufijo, int id, String nombre, String msj) {
        String packet = "cMK" + sufijo + "|" + id + "|" + nombre + "|" + msj;
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT: ALL>>" + packet);
        }
    }

    public static void ENVIAR_cMK_CHAT_MENSAJE_ALINEACION(String sufijo, int id, String nombre, String msj, Personaje perso) {
        String packet = "cMK" + sufijo + "|" + id + "|" + nombre + "|" + msj;
        for (Personaje z : Mundo.getPJsEnLinea()) {
            if (z.getAlineacion() != perso.getAlineacion()) continue;
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT: ALIGNEMENT>>" + packet);
        }
    }

    public static void ENVIAR_cMK_CHAT_MENSAJE_ADMINS(String sufijo, int id, String nombre, String msj) {
        String packet = "cMK" + sufijo + "|" + id + "|" + nombre + "|" + msj;
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            if (!perso.enLinea() || perso.getCuenta() == null || perso.getCuenta().getRango() <= 0) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT: ADMINS>>" + packet);
        }
    }

    public static void ENVIAR_cMK_CHAT_MENSAJE_PELEA(Combate pelea, int equipos, String sufijo, int id, String nombre, String msj) {
        String packet = "cMK" + sufijo + "|" + id + "|" + nombre + "|" + msj;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_GDZ_ACTUALIZA_ZONA_EN_PELEA(Combate pelea, int equipos, String sufijo, int celda, int tama\u00f1o, int color) {
        String packet = "GDZ" + sufijo + celda + ";" + tama\u00f1o + ";" + color;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACTUALISER ZONE: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_GDC_ACTUALIZAR_CELDA_EN_PELEA(Combate pelea, int equipos, int celda) {
        String packet = "GDC" + celda;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACTUALISER CELL FIGHT: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_GDC_AUTORIZAR(Mapa mapa, int celda) {
        String packet = "GDC" + celda + ";aaWaaaaaaa800;1";
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACTUALISER CELL: CELL>>  " + packet);
        }
    }

    public static void ENVIAR_GA2_CARGANDO_MAPA(PrintWriter out, int id) {
        String packet = "GA;2;" + id + ";";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHARGEMENT MAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_cMEf_CHAT_ERROR(PrintWriter out, String nombre) {
        String packet = "cMEf" + nombre;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHAT ERROR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_eD_CAMBIAR_ORIENTACION(Mapa mapa, int id, int dir) {
        String packet = "eD" + id + "|" + dir;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHANGER ORIENTATION: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_TB_CINEMA_INICIO_JUEGO(Personaje perso) {
        String packet = "TB";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CINEMATIQUE DE DEPART: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_TC_CARGAR_TUTORIAL(PrintWriter out, int tutorial) {
        String packet = "TC" + tutorial + "|7001010000";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHARGEMENT DU TUTORIEL: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_TT_MOSTRAR_TIP(PrintWriter out, int tutorial) {
        String packet = "TT" + tutorial;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AIDE MONSTRE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(Personaje perso, int tipo, String str) {
        String packet = "ECK" + tipo;
        if (!str.equals("")) {
            packet = String.valueOf(packet) + "|" + str;
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PANEL ECHANGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(PrintWriter out, int tipo, String str) {
        String packet = "ECK" + tipo;
        if (!str.equals("")) {
            packet = String.valueOf(packet) + "|" + str;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PANEL ECHANGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EL_LISTA_OBJETOS_NPC(PrintWriter out, NPCModelo.NPC npc) {
        String packet = "EL" + npc.getModeloBD().stringObjetosAVender();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("OBJET NPC: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EL_LISTA_OBJETOS_RECAUDADOR(PrintWriter out, Recaudador recau) {
        String packet = "EL" + recau.getListaObjRecaudador();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET PERCEPTEUR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EL_LISTA_OBJETOS_DRAGOPAVO(PrintWriter out, Dragopavo drago) {
        String packet = "EL" + drago.getListaObjDragopavo();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET DRAGODINDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EL_LISTA_TIENDA_PERSONAJE(PrintWriter out, Personaje perso) {
        if (perso == null) {
            return;
        }
        String packet = "EL" + perso.listaTienda();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTER ACHAT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EV_CERRAR_VENTANAS(PrintWriter out) {
        String packet = "EV";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CREATION FENETRE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_DCK_CREAR_DIALOGO(PrintWriter out, int id) {
        String packet = "DCK" + id;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CREATION DIALOGUE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_DQ_DIALOGO_PREGUNTA(PrintWriter out, String str) {
        String packet = "DQ" + str;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DIALOGUE QUESTION: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_DV_FINALIZAR_DIALOGO(PrintWriter out) {
        String packet = "DV";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("TERMINER DIALOGUE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_BAT2_CONSOLA(PrintWriter out, String str) {
        String packet = "BAT2" + str;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CONSOLE COMMANDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EBE_ERROR_DE_COMPRA(PrintWriter out) {
        String packet = "EBE";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR ACHAT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ESE_ERROR_VENTA(PrintWriter out) {
        String packet = "ESE";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR VENTE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EBK_COMPRADO(PrintWriter out) {
        String packet = "EBK";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACHAT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ESK_VENDIDO(Personaje perso) {
        String packet = "ESK";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("VENTE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(Personaje perso, Objeto obj) {
        String packet = "OQ" + obj.getID() + "|" + obj.getCantidad();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET NE PEUT PAS CHANGER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OAKO_APARECER_OBJETO(Personaje perso, Objeto objeto) {
        String packet = "OAKO" + objeto.stringObjetoConGui\u00f1o();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("FIGURER OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OAKO_APARECER_OBJETO(PrintWriter out, Objeto objeto) {
        String packet = "OAKO" + objeto.stringObjetoConGui\u00f1o();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("FIGURER OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OR_ELIMINAR_OBJETO(Personaje perso, int id) {
        String packet = "OR" + id;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("SUPPRESSION OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OR_ELIMINAR_OBJETO(PrintWriter out, int id) {
        String packet = "OR" + id;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("SUPPRESSION OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OdE_ERROR_ELIMINAR_OBJETO(PrintWriter out) {
        String packet = "OdE";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR DE SUPPRESSION OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OM_MOVER_OBJETO(Personaje perso, Objeto obj) {
        String packet = "OM" + obj.getID() + "|";
        if (obj.getPosicion() != -1) {
            packet = String.valueOf(packet) + obj.getPosicion();
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEPLACER OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_cS_EMOTE_EN_PELEA(Combate pelea, int equipos, int id, int id2) {
        String packet = "cS" + id + "|" + id2;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("EMOTE CASE: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_OAEL_ERROR_AGREGAR_OBJETO(PrintWriter out) {
        String packet = "OAEL";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR AJOUT DE L'OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_AN_MENSAJE_NUEVO_NIVEL(Personaje perso, int nivel) {
        String packet = "AN" + nivel;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LEVEL UP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_MENSAJE_A_TODOS_CHAT_COLOR(String msj, String color) {
        String packet = "cs<font color='#" + color + "'>" + msj + "</font>";
        for (Personaje P : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(P, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MSG CHAT COLOR: ALL>>" + packet);
        }
    }

    public static void ENVIAR_OCK_ACTUALIZA_OBJETO(PrintWriter out, Objeto objeto) {
        String packet = "OCK" + objeto.stringObjetoConGui\u00f1o();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACTUALISER OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OCK_ACTUALIZA_OBJETO(Personaje perso, Objeto objeto) {
        String packet = "OCK" + objeto.stringObjetoConGui\u00f1o();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACTUALISER OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ERK_CONSULTA_INTERCAMBIO(PrintWriter out, int id, int idT, int tipo) {
        String packet = "ERK" + id + "|" + idT + "|" + tipo;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("VOIR ECHANGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ERE_ERROR_CONSULTA(PrintWriter out, char c) {
        String packet = "ERE" + c;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("VOIR ERREUR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EMK_MOVER_OBJETO_LOCAL(PrintWriter out, char tipoOG, String signo, String s1) {
        String packet = "EMK" + tipoOG + signo;
        if (!s1.equals("")) {
            packet = String.valueOf(packet) + s1;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET EN MOUVEMENT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EmK_MOVER_OBJETO_DISTANTE(PrintWriter out, char tipoOG, String signo, String s1) {
        String packet = "EmK" + tipoOG + signo;
        if (!s1.equals("")) {
            packet = String.valueOf(packet) + s1;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET DISTANT EN MOUVEMENT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EmK_MOVER_OBJETO_DISTANTE(Personaje perso, char tipoOG, String signo, String s1) {
        String packet = "EmK" + tipoOG + signo;
        if (!s1.equals("")) {
            packet = String.valueOf(packet) + s1;
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET DISTANT EN MOUVEMENT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EmE_ERROR_MOVER_OBJETO_DISTANTE(PrintWriter out, char tipoOG, String signo, String s1) {
        String packet = "EmE" + tipoOG + signo;
        if (!s1.equals("")) {
            packet = String.valueOf(packet) + s1;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR OBJET EN MOUVEMENT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EiK_MOVER_OBJETO_TIENDA(PrintWriter out, char tipo, String signo, String s1) {
        String packet = "EiK" + tipo + signo;
        if (!s1.equals("")) {
            packet = String.valueOf(packet) + s1;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("IMPOSSIBLE DE BOUGER OBJET SHOP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Ep_PAGO_TRABAJO_KAMAS_OBJETOS(PrintWriter out, int tipo, String objKama, String signo, String s1) {
        String packet = "Ep" + tipo + "K" + objKama + signo + s1;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PAIEMENT METIER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ErK_RESULTADO_TRABAJO(Personaje perso, String objKama, String signo, String s1) {
        String packet = "ErK" + objKama + signo + s1;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("RESULTAT METIER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EK_CHECK_OK_INTERCAMBIO(PrintWriter out, boolean ok, int id) {
        String packet = "EK" + (ok ? "1" : "0") + id;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACCEPTER ECHANGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EV_INTERCAMBIO_EFECTUADO(PrintWriter out, char c) {
        String packet = "EV" + c;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ECHANGE EFFECTUE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_PIE_ERROR_INVITACION_GRUPO(PrintWriter out, String s) {
        String packet = "PIE" + s;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR INVITATION GROUPE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_PIK_INVITAR_GRUPO(PrintWriter out, String n1, String n2) {
        String packet = "PIK" + n1 + "|" + n2;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INVITATION GROUPE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_PCK_CREAR_GRUPO(PrintWriter out, Personaje.Grupo grupo) {
        String packet = "PCK" + grupo.getLiderGrupo().getNombre();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CREER GROUPE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_PL_LIDER_GRUPO(PrintWriter out, Personaje.Grupo grupo) {
        String packet = "PL" + grupo.getLiderGrupo().getID();
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LEADER GROUPE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_PR_RECHAZAR_INVITACION_GRUPO(Personaje perso) {
        String packet = "PR";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("REFUSER INVITATION GROUPE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_PV_DEJAR_GRUPO(PrintWriter out, String s) {
        String packet = "PV" + s;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEJA DANS UN GROUPE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_PM_TODOS_MIEMBROS_GRUPO(PrintWriter out, Personaje.Grupo grupo) {
        String packet = "PM+";
        boolean primero = true;
        for (Personaje p : grupo.getPersos()) {
            if (!primero) {
                packet = String.valueOf(packet) + "|";
            }
            packet = String.valueOf(packet) + p.stringInfoGrupo();
            primero = false;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MEMBRE GROUPE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_PM_AGREGAR_PJ_GRUPO(Personaje.Grupo grupo, Personaje perso) {
        String packet = "PM+" + perso.stringInfoGrupo();
        for (Personaje P : grupo.getPersos()) {
            SocketManager.enviar(P, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AJOUTER PERSONNAGE: GROUPE>>  " + packet);
        }
    }

    public static void ENVIAR_PM_ACTUALIZAR_INFO_PJ_GRUPO(Personaje.Grupo grupo, Personaje perso) {
        String packet = "PM~" + perso.stringInfoGrupo();
        for (Personaje P : grupo.getPersos()) {
            SocketManager.enviar(P, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACTUALSER: GROUPE>>  " + packet);
        }
    }

    public static void ENVIAR_PM_EXPULSAR_PJ_GRUPO(Personaje.Grupo grupo, int id) {
        String packet = "PM-" + id;
        for (Personaje P : grupo.getPersos()) {
            SocketManager.enviar(P, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("EXPULSION PERSONNAGE: GROUPE>>  " + packet);
        }
    }

    public static void ENVIAR_cMK_MENSAJE_CHAT_GRUPO(Personaje.Grupo grupo, String str, int id, String nombre, String msj) {
        String packet = "cMK" + str + "|" + id + "|" + nombre + "|" + msj + "|";
        for (Personaje P : grupo.getPersos()) {
            SocketManager.enviar(P, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MESSAGE CHAT: GROUPE>>  " + packet);
        }
    }

    public static void ENVIAR_fD_DETALLES_PELEA(PrintWriter out, Combate pelea) {
        if (pelea == null) {
            return;
        }
        String packet = "fD" + pelea.getID() + "|";
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(1)) {
            if (peleador.esInvocacion()) continue;
            packet = String.valueOf(packet) + peleador.getNombreLuchador() + "~" + peleador.getNivel() + ";";
        }
        packet = String.valueOf(packet) + "|";
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(2)) {
            if (peleador.esInvocacion()) continue;
            packet = String.valueOf(packet) + peleador.getNombreLuchador() + "~" + peleador.getNivel() + ";";
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DETAILLE COMBAT,CELL: OUT>>  " + packet);
        }
    }

    public static void ENVIAR_IQ_NUMERO_ARRIBA_PJ(Personaje perso, int idPerso, int numero) {
        String packet = "IQ" + idPerso + "|" + numero;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("NUMERO MEILLEUR PERSONNAGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_JN_OFICIO_NIVEL(Personaje perso, int oficioID, int nivel) {
        String packet = "JN" + oficioID + "|" + nivel;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("NIVEAU METIER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GDF_OBJETOS_INTERACTIVOS(PrintWriter out, Mapa mapa) {
        String packet = mapa.getObjectosGDF();
        if (packet.isEmpty()) {
            return;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET INTERACTIF: PERSO>>  " + packet);
        }
    }
    
    public static void ENVIAR_GDF_ESTADO_OBJETO_INTERACTIVO(Mapa mapa, Celda celda) {
		ObjetoInteractivo objInteract = celda.getObjetoInterac();
		String packet = "GDF|" + celda.getID() + ";" + objInteract.getEstado() + ";" + (objInteract.esInteractivo() ? "1" : "0");
		for (Personaje p : mapa.getPersos())
			enviar(p, packet);
		if (LesGuardians.MOSTRAR_ENVIOS_SOS)
			System.out.println("ESTADO OBJ INTERACTIVO: MAPA>>  " + packet);
	}

    public static void ENVIAR_GDF_FORZADO_MAPA(Mapa mapa, String str) {
        String packet = "GDF|" + str;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET INTERACTIF FORCE: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GDF_FORZADO_PERSONAJE(Personaje perso, int celda, int frame, int esInteractivo) {
        String packet = "GDF|" + celda + ";" + frame + ";" + esInteractivo;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET INTERACTIF FORCE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GA_ACCION_JUEGO_AL_MAPA(Mapa mapa, String idUnica, int idAccionModelo, String s1, String s2) {
        String packet = "GA" + idUnica + ";" + idAccionModelo + ";" + s1;
        if (!s2.equals("")) {
            packet = String.valueOf(packet) + ";" + s2;
        }
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME ACTION : MAP>>  " + packet);
        }
    }

    public static void ENVIAR_EL_LISTA_OBJETOS_BANCO(Personaje perso) {
        String packet = "EL" + perso.stringBanco();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET BANQUE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_JX_EXPERINENCIA_OFICIO(Personaje perso, ArrayList<Oficio.StatsOficio> statsOficios) {
        String packet = "JX";
        for (Oficio.StatsOficio statOficio : statsOficios) {
            packet = String.valueOf(packet) + "|" + statOficio.getOficio().getID() + ";" + statOficio.getNivel() + ";" + statOficio.getXpString(";") + ";";
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("EXPERIENCE METIER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_JO_OFICIO_OPCIONES(Personaje perso, ArrayList<Oficio.StatsOficio> statsOficios) {
        for (Oficio.StatsOficio statOficio : statsOficios) {
            String packet = "JO" + statOficio.getPosicion() + "|" + statOficio.getOpcionBin() + "|" + statOficio.getSlotsPublico();
            SocketManager.enviar(perso, packet);
            if (!LesGuardians.MOSTRAR_ENVIOS_SOS) continue;
            System.out.println("OPTION METIER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_JO_OFICIO_OPCIONES(Personaje perso, Oficio.StatsOficio statOficio) {
        String packet = "JO" + statOficio.getPosicion() + "|" + statOficio.getOpcionBin() + "|" + statOficio.getSlotsPublico();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OPTION METIER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EJ_DESCRIPCION_LIBRO_ARTESANO(Personaje perso, String str) {
        String packet = "EJ" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DECRIPTION ARTISAN LIBRE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Ej_AGREGAR_LIBRO_ARTESANO(Personaje perso, String str) {
        String packet = "Ej" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AJOUTER ARTISAN LIBRE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_JS_TRABAJO_POR_OFICIO(Personaje perso, ArrayList<Oficio.StatsOficio> statsOficios) {
        String packet = "JS";
        for (Oficio.StatsOficio statOficio : statsOficios) {
            packet = String.valueOf(packet) + statOficio.analizarTrabajolOficio();
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("TRAVAIL POUR METIER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_JR_OLVIDAR_OFICIO(Personaje perso, int id) {
        String packet = "JR" + id;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("METIER OUBLIER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(Personaje perso, String str) {
        String packet = "EsK" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEPLACER OBJET: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Gf_MOSTRAR_CASILLA_EN_PELEA(Combate pelea, int equipos, int id, int celdaID) {
        String packet = "Gf" + id + "|" + celdaID;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(equipos)) {
            Personaje perso;
            if (peleador.estaRetirado() || (perso = peleador.getPersonaje()) == null || !perso.enLinea()) continue;
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MONTRER CASE: PELEA>>  " + packet);
        }
    }

    public static void ENVIAR_Ea_TERMINO_RECETAS(Personaje perso, String str) {
        String packet = "Ea" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CONDITION RECETTE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EA_TURNO_RECETA(Personaje perso, String str) {
        String packet = "EA" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("TEMPS DE RECETTE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Ec_INICIAR_RECETA(Personaje perso, String str) {
        String packet = "Ec" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEBUT RECETTE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_IO_ICONO_OBJ_INTERACTIVO(Mapa mapa, int id, String str) {
        String packet = "IO" + id + "|" + str;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ICONE OBJET INTERACTIF: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_FL_LISTA_DE_AMIGOS(Personaje perso) {
        String packet = "FL" + perso.getCuenta().stringListaAmigos();
        SocketManager.enviar(perso, packet);
        if (perso.getEsposo() != 0) {
            String packet2 = "FS" + perso.getEsposoListaAmigos();
            SocketManager.enviar(perso, packet2);
            if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
                System.out.println("MARI: PERSO>>  " + packet2);
            }
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AMIS EN LIGNE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Im0143_AMIGO_CONECTADO(Personaje amigo, Personaje perso) {
        String packet = "Im0143;" + amigo.getCuenta().getApodo() + " (<b><a href='asfunction:onHref,ShowPlayerPopupMenu," + amigo.getNombre() + "'>" + amigo.getNombre() + "</a></b>)";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MESSAGE AMI CONNECTER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_FA_AGREGAR_AMIGO(Personaje perso, String str) {
        String packet = "FA" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AJOUTER AMI: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_FD_BORRAR_AMIGO(Personaje perso, String str) {
        String packet = "FD" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("SUPPRIMER AMI: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_iA_AGREGAR_ENEMIGO(Personaje perso, String str) {
        String packet = "iA" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AJOUTER ENNEMI: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_iD_BORRAR_ENEMIGO(Personaje perso, String str) {
        String packet = "iD" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("SUPPRIMER ENNEMI: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_iL_LISTA_ENEMIGOS(Personaje perso) {
        String packet = "iL" + perso.getCuenta().stringListaEnemigos();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTE ENNEMIS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Rp_INFORMACION_CERCADO(Personaje perso, Mapa.Cercado cercado) {
        String packet = "";
        if (cercado == null) {
            return;
        }
        packet = "Rp" + cercado.getDue\u00f1o() + ";" + cercado.getPrecio() + ";" + cercado.getTama\u00f1o() + ";" + cercado.getCantObjMax() + ";";
        Gremio gremio = cercado.getGremio();
        packet = gremio != null ? String.valueOf(packet) + gremio.getNombre() + ";" + gremio.getEmblema() : String.valueOf(packet) + ";";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFO ENCLO: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OS_BONUS_SET(Personaje perso, int setID, int numero) {
        String packet = "OS";
        int num = 0;
        num = numero != -1 ? numero : perso.getNroObjEquipadosDeSet(setID);
        if (num == 0) {
            packet = String.valueOf(packet) + "-" + setID;
        } else {
            packet = String.valueOf(packet) + "+" + setID + "|";
            Mundo.ObjetoSet IS = Mundo.getObjetoSet(setID);
            if (IS != null) {
                String objetos = "";
                for (Objeto.ObjetoModelo OM : IS.getObjetosModelos()) {
                    if (!perso.tieneEquipado(OM.getID())) continue;
                    if (objetos.length() > 0) {
                        objetos = String.valueOf(objetos) + ";";
                    }
                    objetos = String.valueOf(objetos) + OM.getID();
                }
                packet = String.valueOf(packet) + objetos + "|" + IS.getBonusStatPorNroObj(num).convertirStatsAString();
            }
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("BONUS PANOPLIE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Rd_DESCRIPCION_MONTURA(Personaje perso, Dragopavo dragopavo) {
        String packet = "Rd" + dragopavo.detallesMontura();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DECRIPTION MONTURE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Rr_ESTADO_MONTADO(Personaje perso, String montado) {
        String packet = "Rr" + montado;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ETAT MONTURE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(Mapa mapa, Personaje perso) {
        String packet = "GM|~" + perso.stringGM();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("REFRESH PERSO: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GM_REFRESCAR_PJ_EN_PELEA(Combate pelea, Combate.Luchador luch) {
        String packet = "GM|~" + luch.stringGM();
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(3)) {
            if (peleador.getPersonaje() == null) continue;
            SocketManager.enviar(peleador.getPersonaje(), packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("REFRESH PERSO: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_As_STATS_DEL_PJ(Personaje perso) {
        String packet = perso.stringStatsPacket();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("STATS PERSONNAGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Rx_EXP_DONADA_MONTURA(Personaje perso) {
        String packet = "Rx" + perso.getXpDonadaMontura();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("XP DONNER,MONTER MONTURE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Rn_CAMBIO_NOMBRE_MONTURA(Personaje perso, String nombre) {
        String packet = "Rn" + nombre;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHANGEMENT NOM MONTURE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Re_DETALLES_MONTURA(Personaje perso, String simbolo, Dragopavo dragopavo) {
        String packet = "Re" + simbolo;
        if (simbolo.equals("+")) {
            packet = String.valueOf(packet) + dragopavo.detallesMontura();
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DETAILLE MONTURE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Ee_MONTURA_A_ESTABLO(Personaje perso, char c, String s) {
        String packet = "Ee" + c + s;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PANEL ETABLE MONTURE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Ef_MONTURA_A_CRIAR(Personaje perso, char c, String s) {
        String packet = "Ef" + c + s;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PANEL SENSIBILISATION MONTURE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_cC_SUSCRIBIR_CANAL(Personaje perso, char c, String s) {
        String packet = "cC" + c + s;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CANAL, ABONNEZ-VOUS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GM_AGREGAR_NPC_AL_MAPA(Mapa mapa, NPCModelo.NPC npc) {
        String packet = "GM|" + npc.analizarGM();
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AJOUTER NPC: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GM_AGREGAR_RECAUDADOR_AL_MAPA(Mapa mapa) {
        String str = Recaudador.enviarGMDeRecaudador(mapa);
        if (str.length() < 4) {
            return;
        }
        String packet = "GM|" + str;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("AJOUTER PERCEPTEUR: MAP>>  " + packet);
        }
    }

    public static void ENVIAR_GDO_OBJETO_TIRADO_AL_SUELO(Mapa mapa, char agre_borr, int celda, int idObjetoMod, int i) {
        String packet = "GDO" + agre_borr + celda + ";" + idObjetoMod + ";" + i;
        for (Personaje z : mapa.getPersos()) {
            SocketManager.enviar(z, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJ TIRADO SUELO: MAPA>>  " + packet);
        }
    }

    public static void ENVIAR_GDO_OBJETO_TIRADO_AL_SUELO(Personaje perso, char agre_borr, int celda, int idObjetoMod, int i) {
        String packet = "GDO" + agre_borr + celda + ";" + idObjetoMod + ";" + i;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJ TIRADO SUELO: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_ZC_ESPECIALIDAD_ALINEACION(Personaje perso, int a) {
        String packet = "ZC" + a;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHANGEMENT ALIGNEMENT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GIP_ACT_DES_ALAS_PERDER_HONOR(Personaje perso, int a) {
        String packet = "GIP" + a;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("REFRESH HONOR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gn_CREAR_GREMIO(Personaje perso) {
        String packet = "gn";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CREER GUILDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gC_CREAR_PANEL_GREMIO(Personaje perso, String s) {
        String packet = "gC" + s;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OUVRIR PANEL GUILDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gV_CERRAR_PANEL_GREMIO(Personaje perso) {
        String packet = "gV";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("FERMER PANEL GUILDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gIM_INFO_MIEMBROS_GREMIO(Personaje perso, Gremio g, char c) {
        String packet = "gIM" + c;
        switch (c) {
            case '+': {
                try {
                    packet = String.valueOf(packet) + g.analizarMiembrosGM();
                }
                catch (NullPointerException nullPointerException) {}
                break;
            }
            case '-': {
                try {
                    packet = String.valueOf(packet) + g.analizarMiembrosGM();
                }
                catch (NullPointerException nullPointerException) {}
                break;
            }
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFO MEMBRE GUILDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gIB_GREMIO_INFO_BOOST(Personaje perso, String infos) {
        String packet = "gIB" + infos;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFO BOOST GUILD: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gIH_GREMIO_INFO_CASAS(Personaje perso, String infos) {
        String packet = "gIH" + infos;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFO GUILDE MAISON: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gS_STATS_GREMIO(Personaje perso, Gremio.MiembroGremio miembro) {
        Gremio gremio = miembro.getGremio();
        String packet = "gS" + gremio.getNombre() + "|" + gremio.getEmblema().replace(',', '|') + "|" + miembro.analizarDerechos();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("STATS GUILDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gJ_GREMIO_UNIR(Personaje perso, String str) {
        String packet = "gJ" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("REJOINDRE GUILDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gK_GREMIO_BAN(Personaje perso, String str) {
        String packet = "gK" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("BAN GUILDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gIG_GREMIO_INFO_GENERAL(Personaje perso, Gremio gremio) {
        if (gremio == null) {
            return;
        }
        long xpMin = Mundo.getExpNivel((int)gremio.getNivel())._gremio;
        long xpMax = Mundo.getExpNivel(gremio.getNivel() + 1) == null ? -1L : Mundo.getExpNivel((int)(gremio.getNivel() + 1))._gremio;
        String packet = "gIG" + (gremio.getSize() > 9 ? 1 : 0) + "|" + gremio.getNivel() + "|" + xpMin + "|" + gremio.getXP() + "|" + xpMax;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFO GENERAL GUILDE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_WC_MENU_ZAAP(Personaje perso) {
        String packet = "WC" + perso.stringListaZaap();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MENU ZAAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Wp_MENU_PRISMA(Personaje perso) {
        String packet = "Wp" + perso.stringListaPrismas();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MENU PRISME: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_WV_CERRAR_ZAAP(Personaje perso) {
        String packet = "WV";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("FERMER ZAAP: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Ww_CERRAR_PRISMA(Personaje perso) {
        String packet = "Ww";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("FERMER PRISME: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Wc_LISTA_ZAPPIS(Personaje perso, String lista) {
        String packet = "Wc" + perso.getMapa().getID() + "|" + lista;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MENU ZAAPIS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Wv_CERRAR_ZAPPI(Personaje perso) {
        String packet = "Wv";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("FERMER ZAAPIS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_WUE_ZAPPI_ERROR(Personaje perso) {
        String packet = "WUE";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR ZAPPI: ENVOIE>>  " + packet);
        }
    }

    public static void ENVIAR_eL_LISTA_EMOTES(Personaje perso, String s, String s1) {
        String packet = "eL" + s + "|" + s1;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTE EMOTES: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_eUE_EMOTE_ERROR(Personaje perso) {
        String packet = "eUE";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ERREUR EMOTE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_AlEw_MUCHOS_JUG_ONLINE(PrintWriter out) {
        String packet = "AlEw";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MAX JOUEUR EN LIGNE: OUT>>  " + packet);
        }
    }

    public static void ENVIAR_BWK_QUIEN_ES(Personaje perso, String str) {
        String packet = "BWK" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("QUIEN ES: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_K_CLAVE(Personaje perso, String str) {
        String packet = "K" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("KEY: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_h_CASA(Personaje perso, String str) {
        String packet = "h" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MAISON: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_SF_OLVIDAR_HECHIZO(char signo, Personaje perso) {
        String packet = "SF" + signo;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OUBLIER SORT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Rv_MONTURA_CERRAR(PrintWriter out) {
        String packet = "Rv";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("FERMER MONTURE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_RD_COMPRAR_CERCADO(Personaje perso, String str) {
        String packet = "RD" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACHETER ENCLOS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gIF_GREMIO_INFO_CERCADOS(Personaje perso, String str) {
        String packet = "gIF" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("INFO ENCLOS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gITM_INFO_RECAUDADOR(Personaje perso, String str) {
        String packet = "gITM" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("INFO PERCEPTEUR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gITp_INFO_ATACANTES_RECAUDADOR(Personaje perso, String str) {
        String packet = "gITp" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("INFO ATTAQUANT DU PERCEPTEUR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_gITP_INFO_DEFENSORES_RECAUDADOR(Personaje perso, String str) {
        String packet = "gITP" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("INFO DEFENSEUR DU PERCEPTEUR: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_CP_INFO_DEFENSORES_PRISMA(Personaje perso, String str) {
        String packet = "CP" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFO DEFENSEUR PRISME: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Cp_INFO_ATACANTES_PRISMA(Personaje perso, String str) {
        String packet = "Cp" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INFO ATTAQUANT PRISME: PERSO>>  " + packet);
        }
    }

    public static void GAME_SEND_gT_PACKET(Personaje perso, String str) {
        String packet = "gT" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_GUILDHOUSE_PACKET(Personaje perso) {
        String packet = "gUT";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_GUILDENCLO_PACKET(Personaje perso) {
        String packet = "gUF";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_EHm_PACKET(Personaje perso, String sign, String str) {
        String packet = "EHm" + sign + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_EHM_PACKET(Personaje perso, String sign, String str) {
        String packet = "EHM" + sign + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_EHP_PACKET(Personaje perso, int modeloID) {
        String packet = "EHP" + modeloID + "|" + Mundo.getObjModelo(modeloID).getPrecioPromedio();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_EHl(Personaje perso, Mercadillo mercadillo, int modeloID) {
        String packet = "EHl" + mercadillo.analizarParaEHl(modeloID);
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_EHL_PACKET(Personaje perso, int categ, String modelos) {
        String packet = "EHL" + categ + "|" + modelos;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_EHL_PACKET(Personaje perso, String objetos) {
        String packet = "EHL" + objetos;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void GAME_SEND_HDVITEM_SELLING(Personaje perso) {
        String packet = "EL";
        Mercadillo.ObjetoMercadillo[] entries = perso.getCuenta().getObjMercaDePuesto(Math.abs(perso.getIntercambiandoCon()));
        boolean isFirst = true;
        Mercadillo.ObjetoMercadillo[] arrobjetoMercadillo = entries;
        int n = entries.length;
        for (int i = 0; i < n; ++i) {
            Mercadillo.ObjetoMercadillo curEntry = arrobjetoMercadillo[i];
            if (curEntry == null) break;
            if (!isFirst) {
                packet = String.valueOf(packet) + "|";
            }
            packet = String.valueOf(packet) + curEntry.analizarParaEL();
            isFirst = false;
        }
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void ENVIAR_ACCION_JUEGO_CASARSE(Mapa mapa, int accion, int hombre, int mujer, int sacerdote) {
        String packet = "GA;" + accion + ";" + hombre + ";" + hombre + "," + mujer + "," + sacerdote;
        Personaje perso = Mundo.getPersonaje(hombre);
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MARIAGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Oa_ACTUALIZAR_FIGURA_PJ(Personaje perso) {
        String packet = "Oa" + perso.getID() + "|" + perso.getStringAccesorios();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("ACTUALISER STATUT PERSONNAGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Gd_RETO_A_LOS_LUCHADORES(Combate pelea, String reto) {
        String packet = "Gd" + reto;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(1)) {
            if (peleador.estaRetirado()) continue;
            SocketManager.enviar(peleador.getPersonaje(), packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHALLENGE: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_Gd_RETO_A_PERSONAJE(Personaje perso, String reto) {
        String packet = "Gd" + reto;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHALLENGE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GdaK_RETO_REALIZADO(Personaje perso, int reto) {
        String packet = "GdaK" + reto;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEFI GAGNE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GdaO_RETO_PERDIDO(Personaje perso, int reto) {
        String packet = "GdaO" + reto;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEFI RATE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GdaK_RETO_REALIZADO(Combate pelea, int reto) {
        String packet = "GdaK" + reto;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(5)) {
            if (peleador.estaRetirado() || peleador.esInvocacion()) continue;
            SocketManager.enviar(peleador.getPersonaje(), packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEFI REALSE: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_GdaO_RETO_PERDIDO(Combate pelea, int reto) {
        String packet = "GdaO" + reto;
        for (Combate.Luchador peleador : pelea.luchadoresDeEquipo(5)) {
            if (peleador.estaRetirado() || peleador.esInvocacion()) continue;
            SocketManager.enviar(peleador.getPersonaje(), packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("DEFI PERDU: FIGHT>>  " + packet);
        }
    }

    public static void ENVIAR_Eq_PREGUNTAR_MERCANTE(Personaje perso, int todoItems, int tasa, long precioPagar) {
        String packet = "Eq" + todoItems + "|" + tasa + "|" + precioPagar;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("QUESTION MARCHANT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_SB_HECHIZO_BOOST_SET_CLASE(Personaje perso, String modificacion) {
        String packet = "SB" + modificacion;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("BOOST SORT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_M1_MENSAJE_SERVER(Personaje perso, String id, String msj, String nombre) {
        String packet = "M1" + id + "|" + msj + "|" + nombre;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MSG SERVER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_IH_COORDINAS_UBICACION(Personaje perso, String str) {
        String packet = "IH" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LIEU COORDONNE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_IC_PERSONAJE_BANDERA_COMPAS(Personaje perso, Personaje objetivo) {
        String packet = "IC" + objetivo.getMapa().getX() + "|" + objetivo.getMapa().getY();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PJ BAND COMPAS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_IC_BORRAR_BANDERA_COMPAS(Personaje perso) {
        String packet = "IC|";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("BORRAR BAND COMPAS: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Im1223_MENSAJE_IMBORRABLE(Personaje perso, String str) {
        String packet = "Im1223;" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("Im1223: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Im1223_MENSAJE_IMBORRABLE(PrintWriter out, String str) {
        String packet = "Im1223;" + str;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("Im1223: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS(String str) {
        String packet = "Im1223;" + str;
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("Im1223: ALL>>  " + packet);
        }
    }

    public static void ENVIAR_gA_MENSAJE_SOBRE_RECAUDADOR(Personaje perso, String str) {
        String packet = "gA" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MSJ SOBRE RECAU: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_am_MENSAJE_ALINEACION_SUBAREA(Personaje perso, String str) {
        String packet = "am" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("MSG ALIGNEMENT SOUS-ZONE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_aM_MENSAJE_ALINEACION_AREA(Personaje perso, String str) {
        String packet = "aM" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_STD) {
            System.out.println("MSG ZONE ALIGNEMENT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_CA_MENSAJE_ATAQUE_PRISMA(Personaje perso, String str) {
        String packet = "CA" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MSG ATTAQUE PRISME: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_CS_MENSAJE_SOBREVIVIO_PRISMA(Personaje perso, String str) {
        String packet = "CS" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MSJ SOBREVIVIO PRISMA: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_CD_MENSAJE_MURIO_PRISMA(Personaje perso, String str) {
        String packet = "CD" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MSJ MURIO PRISMA: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_PF_SEGUIR_PERSONAJE(Personaje perso, String str) {
        String packet = "PF" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("SEGUIR PERSO: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EL_LISTA_OBJETOS_COFRE(Personaje perso, Cofre cofre) {
        String packet = "EL" + cofre.analizarCofre();
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTE OBJET COFFRE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_OT_OBJETO_HERRAMIENTA(PrintWriter out, int id) {
        String packet = "OT";
        if (id > 0) {
            packet = String.valueOf(packet) + id;
        }
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("OBJET TOOL: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EW_OFICIO_MODO_PUBLICO(PrintWriter out, String signo) {
        String packet = "EW" + signo;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MODE PUBLIQUE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_EW_OFICIO_MODO_INVITACION(PrintWriter out, String signo, int idPerso, String idOficios) {
        String packet = "EW" + signo + idPerso + "|" + idOficios;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("INVITER ATTELIER: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Cb_BALANCE_CONQUISTA(Personaje perso, String str) {
        String packet = "Cb" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("BALANCE CONQUETE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_CB_BONUS_CONQUISTA(Personaje perso, String str) {
        String packet = "CB" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("BONUS CONQUETE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_CW_INFO_MUNDO_CONQUISTA(Personaje perso, String str) {
        String packet = "CW" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("MONDE CONQUIT: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_CIJ_INFO_UNIRSE_PRISMA(Personaje perso, String str) {
        String packet = "CIJ" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("REJOINDRE PRISME: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_CIV_CERRAR_INFO_CONQUISTA(Personaje perso) {
        String packet = "CIV";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("FERMER INFO CONQUETE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_Ag_LISTA_REGALOS(PrintWriter out, int idObjeto, String codObjeto) {
        String packet = "Ag1|" + idObjeto + "|Cadeau Dofus Ethernet|vous venez de re\u00e7evoir un cadeau de la part du Staff, " + "faites en bon usage" + "Bon jeu sur le Serveur Ethernet !" + codObjeto;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTE CADEAU: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_AG_SIGUIENTE_REGALO(PrintWriter out) {
        String packet = "AGK";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PROCHAIN CADEAU: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_AlE_CAMBIAR_NOMBRE(PrintWriter out, String letra) {
        String packet = "AlE" + letra;
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("CHANGEMENT NOM: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_M145_MENSAJE_PANEL_INFORMACION(Personaje perso, String str) {
        String packet = "M145|" + str;
        SocketManager.enviar(perso, packet);
    }

    public static void ENVIAR_M145_MENSAJE_PANEL_INFORMACION(PrintWriter out, String str) {
        String packet = "M145|" + str;
        SocketManager.enviar(out, packet);
    }

    public static void ENVIAR_M145_MENSAJE_PANEL_INFORMACION_TODOS(String str) {
        String packet = "M145|" + str;
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PANEL INFORMATION: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_BAIO_MENSAJE_PANEL(Personaje perso, String str) {
        String packet = "BAIO" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PANEL INFORMATION: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION(Personaje perso, String str) {
        String packet = "BAIO" + str;
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION_TODOS(String str) {
        String packet = "BAIO" + str;
        for (Personaje perso : Mundo.getPJsEnLinea()) {
            SocketManager.enviar(perso, packet);
        }
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("GAME: ENVOIE>>  " + packet);
        }
    }

    public static void ENVIAR_Ew_PODS_MONTURA(Personaje perso, int pods) {
        String packet = "Ew" + pods + ";1000";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("PODS MONTURE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_QL_LISTA_MISIONES(Personaje perso, String str) {
        String packet = "QL|442;1;5|220;0;1|185;0;3";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("LISTE QUETES: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_QS_PASOS_RECOMPENSA_MISION(Personaje perso, String str) {
        String packet = "QS220|253|2857,0;2858,0|||";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("RECOMPENSE QUETE: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GV_RESETEAR_PANTALLA_JUEGO(PrintWriter out) {
        String packet = "GV";
        SocketManager.enviar(out, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("RESET ECRAN JEU: PERSO>>  " + packet);
        }
    }

    public static void ENVIAR_GO_GAME_OVER(Personaje perso) {
        String packet = "GO";
        SocketManager.enviar(perso, packet);
        if (LesGuardians.MOSTRAR_ENVIOS_SOS) {
            System.out.println("RESET ECRAN JEU: PERSO>>  " + packet);
        }
    }
}

