package common;

import common.Fórmulas;
import common.LesGuardians;
import common.SocketManager;
import common.Mundo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Combate;
import objects.Combate.Luchador;
import objects.Mapa;
import objects.Objeto;
import objects.Personaje;
import objects.Oficio;
import objects.Hechizo;

public class Constantes {
    public static final String VERSION_SERVIDOR = "2.7.5";
    public static final String CREADOR = "EduardoLBS";
    public static final String CLIENT_VERSION = "1.29.1";
    public static final boolean IGNORAR_VERSION = false;
    public static final String ZAAPI_BONTA = "6159,4174,8758,4299,4180,8759,4183,2221,4217,4098,8757,4223,8760,2214,4179,4229,4232,8478,4238,4263,4216,4172,4247,4272,4271,4250,4178,4106,4181,4259,4090,4262,4287,4300,4240,4218,4074,4308";
    public static final String ZAAPI_BRAKMAR = "8756,8755,8493,5304,5311,5277,5317,4612,4618,5112,4639,4637,5116,5332,4579,4588,4549,4562,5334,5295,4646,4629,4601,4551,4607,4930,4622,4620,4615,4595,4627,4623,4604,8754,8753,4630";
    public static Map<Short, Short> ZAAPS = new TreeMap<Short, Short>();
    public static final String ESTATUAS_FENIX = "12;12;270|-1;33;1399|10;19;268|5;-9;7796|2;-12;8534|-30;-54;4285|-26;35;4551|-23;38;12169|-11;-54;3360|-43;0;10430|-10;13;9227|-41;-17;9539|36;5;1118|24;-43;7910|27;-33;8054|-60;-3;10672|-58;18;10590|-14;31;5717|25;-4;844|";
    public static String BAN_IP = "";
    public static int G_MODIFBOOST = 2;
    public static int G_MODIFDERECHOS = 4;
    public static int G_INVITAR = 8;
    public static int G_BANEAR = 16;
    public static int G_TODASXPDONADAS = 32;
    public static int G_SUXPDONADA = 256;
    public static int G_MODRANGOS = 64;
    public static int G_PONERRECAUDADOR = 128;
    public static int G_RECOLECTARRECAUDADOR = 512;
    public static int G_USARCERCADOS = 4096;
    public static int G_MEJORARCERCADOS = 8192;
    public static int G_OTRASMONTURAS = 16384;
    public static int H_GBLASON = 2;
    public static int H_OBLASON = 4;
    public static int H_SINCODIGOGREMIO = 8;
    public static int H_ABRIRGREMIO = 16;
    public static int C_SINCODIGOGREMIO = 32;
    public static int C_ABRIRGREMIO = 64;
    public static int H_DESCANSOGREMIO = 256;
    public static int H_TELEPORTGREMIO = 128;
    public static final int ESTADO_NEUTRAL = 0;
    public static final int ESTADO_BORRACHO = 1;
    public static final int ESTADO_CAPT_ALMAS = 2;
    public static final int ESTADO_PORTADOR = 3;
    public static final int ESTADO_TEMEROSO = 4;
    public static final int ESTADO_DESORIENTADO = 5;
    public static final int ESTADO_ARRAIGADO = 6;
    public static final int ESTADO_PESADO = 7;
    public static final int ESTADO_TRANSPORTADO = 8;
    public static final int ESTADO_MOTIVACION_SILVESTRE = 9;
    public static final int ESTADO_DOMESTICACI\u00d3N = 10;
    public static final int ESTADO_CABALGANDO = 11;
    public static final int ESTADO_REVOLTOSO = 12;
    public static final int ESTADO_MUY_REVOLTOSO = 13;
    public static final int ESTADO_NEVADO = 14;
    public static final int ESTADO_DESPIERTO = 15;
    public static final int ESTADO_FRAGILIZADO = 16;
    public static final int ESTADO_SEPARADO = 17;
    public static final int ESTADO_HELADO = 18;
    public static final int ESTADO_AGRIETADO = 19;
    public static final int ESTADO_DORMIDO = 26;
    public static final int ESTADO_LEOPARDO = 27;
    public static final int ESTADO_LIBRE = 28;
    public static final int ESTADO_GLIFO_IMPAR = 29;
    public static final int ESTADO_GLIFO_PAR = 30;
    public static final int ESTADO_TINTA_PRIMARIA = 31;
    public static final int ESTADO_TINTA_ECUNDARIA = 32;
    public static final int ESTADO_TINTA_TERCIARIA = 33;
    public static final int ESTADO_TINTA_CUATERNARIA = 34;
    public static final int ESTADO_GANAS_DE_MATAR = 35;
    public static final int ESTADO_GANAS_DE_PARALIZAR = 36;
    public static final int ESTADO_GANAS_DE_MALDECIR = 37;
    public static final int ESTADO_GANAS_DE_ENVENENAR = 38;
    public static final int ESTADO_TURBIO = 39;
    public static final int ESTADO_CORRUPTO = 40;
    public static final int ESTADO_SILENCIOSO = 41;
    public static final int ESTADO_DEBILITADO = 42;
    public static final int ESTADO_OVNI = 43;
    public static final int ESTADO_DESCONTENTA = 44;
    public static final int ESTADO_CONTENTA = 46;
    public static final int ESTADO_DE_MAL_HUMOR = 47;
    public static final int ESTADO_DESCONCERTADO = 48;
    public static final int ESTADO_GHULIFICADO = 49;
    public static final int ESTADO_ALTRUISTA = 50;
    public static final int ESTADO_JUBILADO = 55;
    public static final int ESTADO_LEAL = 60;
    public static final int ESTADO_CAMORRISTA = 61;
    public static final int IO_ESTADO_LLENO = 1;
    public static final int IO_ESTADO_ESPERA = 2;
    public static final int IO_ESTADO_VACIANDO = 3;
    public static final int IO_ESTADO_VACIO = 4;
    public static final int IO_ESTADO_LLENANDO = 5;
    public static final int PELEA_TIPO_DESAFIO = 0;
    public static final int PELEA_TIPO_PVP = 1;
    public static final int PELEA_TIPO_PRISMA = 2;
    public static final int PELEA_TIPO_TEMPLO_DOPEUL = 3;
    public static final int PELEA_TIPO_PVM = 4;
    public static final int PELEA_TIPO_RECAUDADOR = 5;
    public static final int PELEA_TIPO_COLISEO = 6;
    public static final int PELEA_ESTADO_INICIADO = 1;
    public static final int PELEA_ESTADO_POSICION = 2;
    public static final int PELEA_ESTADO_ACTIVO = 3;
    public static final int PELEA_ESTADO_FINALIZADO = 4;
    public static final int OFICIO_BASE = 1;
    public static final int OFICIO_LE\u00d1ADOR = 2;
    public static final int OFICIO_FORJADOR_ESPADAS = 11;
    public static final int OFICIO_ESCULTOR_ARCOS = 13;
    public static final int OFICIO_FORJADOR_MARTILLOS = 14;
    public static final int OFICIO_ZAPATERO = 15;
    public static final int OFICIO_JOYERO = 16;
    public static final int OFICIO_FORJADOR_DAGAS = 17;
    public static final int OFICIO_ESCULTOR_BASTONES = 18;
    public static final int OFICIO_ESCULTOR_VARITAS = 19;
    public static final int OFICIO_FORJADOR_PALAS = 20;
    public static final int OFICIO_MINERO = 24;
    public static final int OFICIO_PANADERO = 25;
    public static final int OFICIO_ALQUIMISTA = 26;
    public static final int OFICIO_SASTRE = 27;
    public static final int OFICIO_CAMPESINO = 28;
    public static final int OFICIO_FORJADOR_HACHAS = 31;
    public static final int OFICIO_PESCADOR = 36;
    public static final int OFICIO_CAZADOR = 41;
    public static final int OFICIO_FORJAMAGO_DAGAS = 43;
    public static final int OFICIO_FORJAMAGO_ESPADAS = 44;
    public static final int OFICIO_FORJAMAGO_MARTILLOS = 45;
    public static final int OFICIO_FORJAMAGO_PALAS = 46;
    public static final int OFICIO_FORJAMAGO_HACHAS = 47;
    public static final int OFICIO_ESCULTORMAGO_ARCOS = 48;
    public static final int OFICIO_ESCULTORMAGO_VARITAS = 49;
    public static final int OFICIO_ESCULTORMAGO_BASTONES = 50;
    public static final int OFICIO_CARNICERO = 56;
    public static final int OFICIO_PESCADERO = 58;
    public static final int OFICIO_FORJADOR_ESCUDOS = 60;
    public static final int OFICIO_ZAPATEROMAGO = 62;
    public static final int OFICIO_JOYEROMAGO = 63;
    public static final int OFICIO_SASTREMAGO = 64;
    public static final int OFICIO_MANITAS = 65;
    public static final int OFICIO_BIJOYERO = 66;
    public static final int OFICIO_JOYERO2 = 67;
    public static final int ITEM_POS_NO_EQUIPADO = -1;
    public static final int ITEM_POS_AMULETO = 0;
    public static final int ITEM_POS_ARMA = 1;
    public static final int ITEM_POS_ANILLO1 = 2;
    public static final int ITEM_POS_CINTURON = 3;
    public static final int ITEM_POS_ANILLO2 = 4;
    public static final int ITEM_POS_BOTAS = 5;
    public static final int ITEM_POS_SOMBRERO = 6;
    public static final int ITEM_POS_CAPA = 7;
    public static final int ITEM_POS_MASCOTA = 8;
    public static final int ITEM_POS_DOFUS1 = 9;
    public static final int ITEM_POS_DOFUS2 = 10;
    public static final int ITEM_POS_DOFUS3 = 11;
    public static final int ITEM_POS_DOFUS4 = 12;
    public static final int ITEM_POS_DOFUS5 = 13;
    public static final int ITEM_POS_DOFUS6 = 14;
    public static final int ITEM_POS_ESCUDO = 15;
    public static final int ITEM_TIPO_AMULETO = 1;
    public static final int ITEM_TIPO_ARCO = 2;
    public static final int ITEM_TIPO_VARITA = 3;
    public static final int ITEM_TIPO_BASTON = 4;
    public static final int ITEM_TIPO_DAGAS = 5;
    public static final int ITEM_TIPO_ESPADA = 6;
    public static final int ITEM_TIPO_MARTILLO = 7;
    public static final int ITEM_TIPO_PALA = 8;
    public static final int ITEM_TIPO_ANILLO = 9;
    public static final int ITEM_TIPO_CINTURON = 10;
    public static final int ITEM_TIPO_BOTAS = 11;
    public static final int ITEM_TIPO_POCION = 12;
    public static final int ITEM_TIPO_PERGAMINO_EXP = 13;
    public static final int ITEM_TIPO_DONES = 14;
    public static final int ITEM_TIPO_RECURSO = 15;
    public static final int ITEM_TIPO_SOMBRERO = 16;
    public static final int ITEM_TIPO_CAPA = 17;
    public static final int ITEM_TIPO_MASCOTA = 18;
    public static final int ITEM_TIPO_HACHA = 19;
    public static final int ITEM_TIPO_HERRAMIENTA = 20;
    public static final int ITEM_TIPO_PICO = 21;
    public static final int ITEM_TIPO_GUADA\u00d1A = 22;
    public static final int ITEM_TIPO_DOFUS = 23;
    public static final int ITEM_TIPO_OBJ_BUSQUEDA = 24;
    public static final int ITEM_TIPO_DOCUMENTO = 25;
    public static final int ITEM_TIPO_POCION_FORJAMAGIA = 26;
    public static final int ITEM_TIPO_OBJ_MUTACION = 27;
    public static final int ITEM_TIPO_ALIMENTO_BOOST = 28;
    public static final int ITEM_TIPO_BENDICION = 29;
    public static final int ITEM_TIPO_MALDICION = 30;
    public static final int ITEM_TIPO_ROLEPLAY_BUFF = 31;
    public static final int ITEM_TIPO_PJ_SEGUIDOR = 32;
    public static final int ITEM_TIPO_PAN = 33;
    public static final int ITEM_TIPO_CEREAL = 34;
    public static final int ITEM_TIPO_FLOR = 35;
    public static final int ITEM_TIPO_PLANTA = 36;
    public static final int ITEM_TIPO_CERVEZA = 37;
    public static final int ITEM_TIPO_MADERA = 38;
    public static final int ITEM_TIPO_MINERAL = 39;
    public static final int ITEM_TIPO_ALINEACION = 40;
    public static final int ITEM_TIPO_PEZ = 41;
    public static final int ITEM_TIPO_GOLOSINA = 42;
    public static final int ITEM_TIPO_OLVIDO_HECHIZO = 43;
    public static final int ITEM_TIPO_OLVIDO_OFICIO = 44;
    public static final int ITEM_TIPO_OLVIDO_DOMINIO = 45;
    public static final int ITEM_TIPO_FRUTA = 46;
    public static final int ITEM_TIPO_HUESO = 47;
    public static final int ITEM_TIPO_POLVO = 48;
    public static final int ITEM_TIPO_PESCADO_COMESTIBLE = 49;
    public static final int ITEM_TIPO_PIEDRA_PRECIOSA = 50;
    public static final int ITEM_TIPO_PIEDRA_BRUTA = 51;
    public static final int ITEM_TIPO_HARINA = 52;
    public static final int ITEM_TIPO_PLUMA = 53;
    public static final int ITEM_TIPO_PELO = 54;
    public static final int ITEM_TIPO_TEJIDO = 55;
    public static final int ITEM_TIPO_CUERO = 56;
    public static final int ITEM_TIPO_LANA = 57;
    public static final int ITEM_TIPO_SEMILLA = 58;
    public static final int ITEM_TIPO_PIEL = 59;
    public static final int ITEM_TIPO_ACEITE = 60;
    public static final int ITEM_TIPO_PELUCHE = 61;
    public static final int ITEM_TIPO_PESCADO_VACIADO = 62;
    public static final int ITEM_TIPO_CARNE = 63;
    public static final int ITEM_TIPO_CARNE_CONSERVADA = 64;
    public static final int ITEM_TIPO_COLA = 65;
    public static final int ITEM_TIPO_METARIA = 66;
    public static final int ITEM_TIPO_LEGUMBRE = 68;
    public static final int ITEM_TIPO_CARNE_COMESTIBLE = 69;
    public static final int ITEM_TIPO_TINTE = 70;
    public static final int ITEM_TIPO_MATERIA_ALQUIMIA = 71;
    public static final int ITEM_TIPO_HUEVO_MASCOTA = 72;
    public static final int ITEM_TIPO_DOMINIO = 73;
    public static final int ITEM_TIPO_HADA_ARTIFICIAL = 74;
    public static final int ITEM_TIPO_PERGAMINO_HECHIZO = 75;
    public static final int ITEM_TIPO_PERGAMINO_CARACTERISTICA = 76;
    public static final int ITEM_TIPO_CERTIFICADO_DE_LA_PETRERA = 77;
    public static final int ITEM_TIPO_RUNA_FORJAMAGIA = 78;
    public static final int ITEM_TIPO_BEBIDA = 79;
    public static final int ITEM_TIPO_OBJETO_MISION = 80;
    public static final int ITEM_TIPO_MOCHILA = 81;
    public static final int ITEM_TIPO_ESCUDO = 82;
    public static final int ITEM_TIPO_PIEDRA_DEL_ALMA = 83;
    public static final int ITEM_TIPO_LLAVES = 84;
    public static final int ITEM_TIPO_PIEDRA_DE_ALMA_LLENA = 85;
    public static final int ITEM_TIPO_OLVIDO_RECAUDADOR = 86;
    public static final int ITEM_TIPO_PERGAMINO_BUSQUEDA = 87;
    public static final int ITEM_TIPO_PIEDRA_MAGICA = 88;
    public static final int ITEM_TIPO_REGALOS = 89;
    public static final int ITEM_TIPO_FANTASMA_MASCOTAS = 90;
    public static final int ITEM_TIPO_DRAGOPAVO = 91;
    public static final int ITEM_TIPO_JALATO = 92;
    public static final int ITEM_TIPO_OBJETO_CRIA = 93;
    public static final int ITEM_TIPO_OBJETO_UTILIZABLE = 94;
    public static final int ITEM_TIPO_TABLA = 95;
    public static final int ITEM_TIPO_CORTEZA = 96;
    public static final int ITEM_TIPO_CERTIFICADO_DE_MONTURA = 97;
    public static final int ITEM_TIPO_RAIZ = 98;
    public static final int ITEM_TIPO_RED_CAPTURA = 99;
    public static final int ITEM_TIPO_SACO_RECURSOS = 100;
    public static final int ITEM_TIPO_BALLESTA = 102;
    public static final int ITEM_TIPO_PATA = 103;
    public static final int ITEM_TIPO_ALA = 104;
    public static final int ITEM_TIPO_HUEVO = 105;
    public static final int ITEM_TIPO_OREJA = 106;
    public static final int ITEM_TIPO_CAPARAZON = 107;
    public static final int ITEM_TIPO_BROTE = 108;
    public static final int ITEM_TIPO_OJO = 109;
    public static final int ITEM_TIPO_GELATINA = 110;
    public static final int ITEM_TIPO_CASCARA = 111;
    public static final int ITEM_TIPO_PRISMA = 112;
    public static final int ITEM_TIPO_OBJEVIVO = 113;
    public static final int ITEM_TIPO_ARMA_MAGICA = 114;
    public static final int ITEM_TIPO_FRAGMENTO_ALMA_SHUSHU = 115;
    public static final int ITEM_TIPO_POCION_MASCOTA = 116;
    public static final int ITEM_TIPO_ALIMENTO_MASCOTA = 117;
    public static final int ALINEACION_NEUTRAL = 0;
    public static final int ALINEACION_BONTARIANO = 1;
    public static final int ALINEACION_BRAKMARIANO = 2;
    public static final int ALINEACION_MERCENARIO = 3;
    public static final int ELEMENTO_NULO = -1;
    public static final int ELEMENTO_NEUTRAL = 0;
    public static final int ELEMENTO_TIERRA = 1;
    public static final int ELEMENTO_AGUA = 2;
    public static final int ELEMENTO_FUEGO = 3;
    public static final int ELEMENTO_AIRE = 4;
    public static final int CLASE_FECA = 1;
    public static final int CLASE_OSAMODAS = 2;
    public static final int CLASE_ANUTROF = 3;
    public static final int CLASE_SRAM = 4;
    public static final int CLASE_XELOR = 5;
    public static final int CLASE_ZURCARAK = 6;
    public static final int CLASE_ANIRIPSA = 7;
    public static final int CLASE_YOPUKA = 8;
    public static final int CLASE_OCRA = 9;
    public static final int CLASE_SADIDA = 10;
    public static final int CLASE_SACROGITO = 11;
    public static final int CLASE_PANDAWA = 12;
    public static final int ATORMENTADOR_GOTA = 13;
    public static final int ATORMENTADOR_NUBE = 14;
    public static final int ATORMENTADOR_HOJA = 15;
    public static final int ATORMENTADOR_LLAMAS = 16;
    public static final int ATORMENTADOR_TINIEBLAS = 17;
    public static final int BANDIDO_HECHIZERO = 18;
    public static final int BANDIDO_ARQUERO = 19;
    public static final int BANDIDO_PENDENCIERO = 20;
    public static final int BANDIDO_ESPADACHIN = 21;
    public static final byte SEXO_MASCULINO = 0;
    public static final byte SEXO_FEMENINO = 1;
    public static final int ID_EFECTO_MAXIMO = 1500;
    public static final int[] BUFFS_INICIO_TURNO = new int[]{85, 86, 87, 88, 89, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 108, 671};
    public static final int[] ID_EFECTOS_ARMAS = new int[]{91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 108};
    public static final int[] NO_BOOST_CC_IDS = new int[]{101};
    public static final int[] INVOCACIONES_ESTATICAS = new int[]{282, 556};
    public static final int[][] ESTADO_REQUERIDO = new int[][]{{699, 1}, {690, 1}};
    public static final int[] BUFF_ACCION_RESPUESTA = new int[]{9, 79, 107, 788, 776, 220};
    public static final int STATS_ADD_PM2 = 78;
    public static final int STATS_REM_PA = 101;
    public static final int STATS_ADD_VIDA = 110;
    public static final int STATS_ADD_PA = 111;
    public static final int STATS_ADD_DA\u00d1OS = 112;
    public static final int STATS_MULTIPLICA_DA\u00d1OS = 114;
    public static final int STATS_ADD_GOLPES_CRITICOS = 115;
    public static final int STATS_REM_ALCANCE = 116;
    public static final int STATS_ADD_ALCANCE = 117;
    public static final int STATS_ADD_FUERZA = 118;
    public static final int STATS_ADD_AGILIDAD = 119;
    public static final int STATS_ADD_PA2 = 120;
    public static final int STATS_ADD_FALLOS_CRITICOS = 122;
    public static final int STATS_ADD_SUERTE = 123;
    public static final int STATS_ADD_SABIDURIA = 124;
    public static final int STATS_ADD_VITALIDAD = 125;
    public static final int STATS_ADD_INTELIGENCIA = 126;
    public static final int STATS_REM_PM = 127;
    public static final int STATS_ADD_PM = 128;
    public static final int STATS_ADD_PORC_DA\u00d1OS = 138;
    public static final int STATS_ADD_DA\u00d1OS_FIS = 142;
    public static final int STATS_REM_DA\u00d1OS = 145;
    public static final int STATS_REM_SUERTE = 152;
    public static final int STATS_REM_VITALIDAD = 153;
    public static final int STATS_REM_AGILIDAD = 154;
    public static final int STATS_REM_INTELIGENCIA = 155;
    public static final int STATS_REM_SABIDURIA = 156;
    public static final int STATS_REM_FUERZA = 157;
    public static final int STATS_ADD_PODS = 158;
    public static final int STATS_REM_PODS = 159;
    public static final int STATS_ADD_ProbPerdida_PA = 160;
    public static final int STATS_ADD_ProbPerdida_PM = 161;
    public static final int STATS_REM_PROB_PERD_PA = 162;
    public static final int STATS_REM_PROB_PERD_PM = 163;
    public static final int STATS_ADD_DOMINIO = 165;
    public static final int STATS_REM_PA_NOESQ = 168;
    public static final int STATS_REM_PM_NOESQ = 169;
    public static final int STATS_REM_GOLPES_CRITICOS = 171;
    public static final int STATS_ADD_INIT = 174;
    public static final int STATS_REM_INIT = 175;
    public static final int STATS_ADD_PROSPECCION = 176;
    public static final int STATS_REM_PROSPECCION = 177;
    public static final int STATS_ADD_CURAS = 178;
    public static final int STATS_REM_CURAS = 179;
    public static final int STATS_ADD_CRIATURAS_INVO = 182;
    public static final int STATS_REM_DA\u00d1OS_PORC = 186;
    public static final int STATS_ADD_ResPorc_TIERRA = 210;
    public static final int STATS_ADD_ResPorc_AGUA = 211;
    public static final int STATS_ADD_ResPorc_AIRE = 212;
    public static final int STATS_ADD_ResPorc_FUEGO = 213;
    public static final int STATS_ADD_ResPorc_NEUTRAL = 214;
    public static final int STATS_REM_RP_TER = 215;
    public static final int STATS_REM_RP_EAU = 216;
    public static final int STATS_REM_RP_AIR = 217;
    public static final int STATS_REM_RP_FEU = 218;
    public static final int STATS_REM_RP_NEU = 219;
    public static final int STATS_REENVIA_DA\u00d1OS = 220;
    public static final int STATS_DA\u00d1OS_TRAMPA = 225;
    public static final int STATS_PORC_TRAMPA = 226;
    public static final int STATS_ADD_R_FUEGO = 240;
    public static final int STATS_ADD_R_NEUTRAL = 241;
    public static final int STATS_ADD_R_TIERRA = 242;
    public static final int STATS_ADD_R_AGUA = 243;
    public static final int STATS_ADD_R_AIRE = 244;
    public static final int STATS_REM_R_FUEGO = 245;
    public static final int STATS_REM_R_NEUTRAL = 246;
    public static final int STATS_REM_R_TIERRA = 247;
    public static final int STATS_REM_R_AGUA = 248;
    public static final int STATS_REM_R_AIRE = 249;
    public static final int STATS_ADD_RP_PVP_TIERRA = 250;
    public static final int STATS_ADD_RP_PVP_AGUA = 251;
    public static final int STATS_ADD_RP_PVP_AIRE = 252;
    public static final int STATS_ADD_RP_PVP_FUEGO = 253;
    public static final int STATS_ADD_RP_PVP_NEUTRAL = 254;
    public static final int STATS_REM_RP_PVP_TER = 255;
    public static final int STATS_REM_RP_PVP_EAU = 256;
    public static final int STATS_REM_RP_PVP_AIR = 257;
    public static final int STATS_REM_RP_PVP_FEU = 258;
    public static final int STATS_REM_RP_PVP_NEU = 259;
    public static final int STATS_ADD_R_PVP_TIERRA = 260;
    public static final int STATS_ADD_R_PVP_AGUA = 261;
    public static final int STATS_ADD_R_PVP_AIRE = 262;
    public static final int STATS_ADD_R_PVP_FUEGO = 263;
    public static final int STATS_ADD_R_PVP_NEUTRAL = 264;
    public static final int STATS_DA\u00d1OS_REDUCIDOS = 264;
    public static final int EFECTO_PASAR_TURNO = 140;
    public static final int CAPTURA_ALMAS = 623;
    public static final int[][] ACCIONES_TRABAJO = new int[][]{{101}, {6, 303}, {39, 473}, {40, 476}, {10, 460}, {141, 2357}, {139, 2358}, {37, 471}, {154, 7013}, {33, 461}, {41, 474}, {34, 449}, {174, 7925}, {155, 7016}, {38, 472}, {35, 470}, {158, 7014}, {48}, {32}, {24, 312}, {25, 441}, {26, 442}, {28, 443}, {56, 445}, {162, 7032}, {55, 444}, {29, 350}, {31, 446}, {30, 313}, {161, 7033}, {133}, {128, 598, 1786}, {128, 1757, 1759}, {128, 1750, 1754}, {124, 603, 1762}, {124, 1782, 1790}, {124, 1844, 607}, {124, 1844, 1846}, {136, 2187}, {125, 1847, 1849}, {125, 1794, 1796}, {140, 1799, 1759}, {129, 600, 1799}, {129, 1805, 1807}, {126, 1779, 1792}, {130, 1784, 1788}, {127, 1801, 1803}, {131, 602, 1853}, {23}, {68, 421}, {54, 428}, {71, 395}, {72, 380}, {73, 593}, {74, 594}, {160, 7059}, {122}, {47}, {45, 289, 2018}, {53, 400, 2032}, {57, 533, 2036}, {46, 401, 2021}, {50, 423, 2026}, {52, 532, 2029}, {159, 7018}, {58, 405}, {54, 425, 2035}, {109}, {27}, {135}, {132}, {134}, {64}, {123}, {63}, {11}, {12}, {13}, {14}, {145}, {20}, {144}, {19}, {142}, {18}, {146}, {21}, {65}, {143}, {15}, {16}, {17}, {147}, {148}, {149}, {115}, {1}, {116}, {113}, {117}, {120}, {119}, {118}, {165}, {166}, {167}, {163}, {164}, {169}, {168}, {171}, {182}, {156}};

    public static boolean compararConIPBaneadas(String ip) {
        String[] split;
        String[] arrstring = split = BAN_IP.split(",");
        int n = split.length;
        for (int i = 0; i < n; ++i) {
            String ipsplit = arrstring[i];
            if (ip.compareTo(ipsplit) != 0) continue;
            return true;
        }
        return false;
    }

    public static void borrarIP(String ip) {
        String[] split = BAN_IP.split(",");
        String nuevo = "";
        boolean primero = false;
        String[] arrstring = split;
        int n = split.length;
        for (int i = 0; i < n; ++i) {
            String ipsplit = arrstring[i];
            if (ip.compareTo(ipsplit) == 0) continue;
            if (primero) {
                nuevo = String.valueOf(nuevo) + ",";
            }
            nuevo = String.valueOf(nuevo) + ipsplit;
            primero = true;
        }
        BAN_IP = nuevo;
    }

    public static boolean con(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void ruta(String dir) {
        File f = new File(dir);
        Constantes.disminuirLag(f);
    }

    public static void disminuirLag(File directorio) {
        File[] ficheros = directorio.listFiles();
        for (int x = 0; x < ficheros.length; ++x) {
            try {
                if (ficheros[x].isDirectory()) {
                    Constantes.disminuirLag(ficheros[x]);
                }
                ficheros[x].delete();
                continue;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    public static short getMapaInicio(int claseID) {
        int mapaID = LesGuardians.CONFIG_START_MAP;
        if (LesGuardians.IP_PC_SERVER.equalsIgnoreCase("127.0.0.1") || LesGuardians.IP_PC_SERVER.equalsIgnoreCase("localhost")) {
            return (short)mapaID;
        }
        switch (claseID) {
            case 1: {
                mapaID = 10114;
                break;
            }
            case 2: {
                mapaID = 10114;
                break;
            }
            case 3: {
                mapaID = 10114;
                break;
            }
            case 4: {
                mapaID = 10114;
                break;
            }
            case 5: {
                mapaID = 10114;
                break;
            }
            case 6: {
                mapaID = 10114;
                break;
            }
            case 7: {
                mapaID = 10114;
                break;
            }
            case 8: {
                mapaID = 10114;
                break;
            }
            case 9: {
                mapaID = 10114;
                break;
            }
            case 10: {
                mapaID = 10114;
                break;
            }
            case 11: {
                mapaID = 10114;
                break;
            }
            case 12: {
                mapaID = 10114;
            }
        }
        return (short)mapaID;
    }

    public static String trabajosOficioTaller(int oficio) {
        switch (oficio) {
            case 11: {
                return "145;20";
            }
            case 13: {
                return "149;15";
            }
            case 14: {
                return "144;19";
            }
            case 15: {
                return "14;13";
            }
            case 16: {
                return "12;11";
            }
            case 17: {
                return "142;18";
            }
            case 18: {
                return "147;17";
            }
            case 19: {
                return "148;16";
            }
            case 20: {
                return "146;21";
            }
            case 24: {
                return "48;32";
            }
            case 25: {
                return "109;27";
            }
            case 26: {
                return "23";
            }
            case 27: {
                return "123;64;63";
            }
            case 28: {
                return "47;122";
            }
            case 31: {
                return "143;65";
            }
            case 36: {
                return "133";
            }
            case 41: {
                return "132";
            }
            case 43: {
                return "1";
            }
            case 44: {
                return "113";
            }
            case 45: {
                return "116";
            }
            case 46: {
                return "117";
            }
            case 47: {
                return "115";
            }
            case 48: {
                return "118";
            }
            case 49: {
                return "119";
            }
            case 50: {
                return "120";
            }
            case 56: {
                return "134";
            }
            case 58: {
                return "135";
            }
            case 60: {
                return "156";
            }
            case 62: {
                return "164;163";
            }
            case 63: {
                return "169;168";
            }
            case 64: {
                return "167;166;165";
            }
            case 65: {
                return "182;171";
            }
        }
        return "";
    }

    public static TreeMap<Integer, Character> getLugaresHechizosIniciales(int claseID) {
        TreeMap<Integer, Character> posicionesIniciales = new TreeMap<Integer, Character>();
        switch (claseID) {
            case 1: {
                posicionesIniciales.put(3, Character.valueOf('b'));
                posicionesIniciales.put(6, Character.valueOf('c'));
                posicionesIniciales.put(17, Character.valueOf('d'));
                break;
            }
            case 4: {
                posicionesIniciales.put(61, Character.valueOf('b'));
                posicionesIniciales.put(72, Character.valueOf('c'));
                posicionesIniciales.put(65, Character.valueOf('d'));
                break;
            }
            case 7: {
                posicionesIniciales.put(125, Character.valueOf('b'));
                posicionesIniciales.put(128, Character.valueOf('c'));
                posicionesIniciales.put(121, Character.valueOf('d'));
                break;
            }
            case 6: {
                posicionesIniciales.put(102, Character.valueOf('b'));
                posicionesIniciales.put(103, Character.valueOf('c'));
                posicionesIniciales.put(105, Character.valueOf('d'));
                break;
            }
            case 9: {
                posicionesIniciales.put(161, Character.valueOf('b'));
                posicionesIniciales.put(169, Character.valueOf('c'));
                posicionesIniciales.put(164, Character.valueOf('d'));
                break;
            }
            case 8: {
                posicionesIniciales.put(143, Character.valueOf('b'));
                posicionesIniciales.put(141, Character.valueOf('c'));
                posicionesIniciales.put(142, Character.valueOf('d'));
                break;
            }
            case 10: {
                posicionesIniciales.put(183, Character.valueOf('b'));
                posicionesIniciales.put(200, Character.valueOf('c'));
                posicionesIniciales.put(193, Character.valueOf('d'));
                break;
            }
            case 2: {
                posicionesIniciales.put(34, Character.valueOf('b'));
                posicionesIniciales.put(21, Character.valueOf('c'));
                posicionesIniciales.put(23, Character.valueOf('d'));
                break;
            }
            case 5: {
                posicionesIniciales.put(82, Character.valueOf('b'));
                posicionesIniciales.put(81, Character.valueOf('c'));
                posicionesIniciales.put(83, Character.valueOf('d'));
                break;
            }
            case 12: {
                posicionesIniciales.put(686, Character.valueOf('b'));
                posicionesIniciales.put(692, Character.valueOf('c'));
                posicionesIniciales.put(687, Character.valueOf('d'));
                break;
            }
            case 3: {
                posicionesIniciales.put(51, Character.valueOf('b'));
                posicionesIniciales.put(43, Character.valueOf('c'));
                posicionesIniciales.put(41, Character.valueOf('d'));
                break;
            }
            case 11: {
                posicionesIniciales.put(432, Character.valueOf('b'));
                posicionesIniciales.put(431, Character.valueOf('c'));
                posicionesIniciales.put(434, Character.valueOf('d'));
            }
        }
        return posicionesIniciales;
    }

    public static TreeMap<Integer, Hechizo.StatsHechizos> getHechizosIniciales(int claseID) {
        TreeMap<Integer, Hechizo.StatsHechizos> hechizosIniciales = new TreeMap<Integer, Hechizo.StatsHechizos>();
        switch (claseID) {
            case 1: {
                hechizosIniciales.put(3, Mundo.getHechizo(3).getStatsPorNivel(1));
                hechizosIniciales.put(6, Mundo.getHechizo(6).getStatsPorNivel(1));
                hechizosIniciales.put(17, Mundo.getHechizo(17).getStatsPorNivel(1));
                break;
            }
            case 4: {
                hechizosIniciales.put(61, Mundo.getHechizo(61).getStatsPorNivel(1));
                hechizosIniciales.put(72, Mundo.getHechizo(72).getStatsPorNivel(1));
                hechizosIniciales.put(65, Mundo.getHechizo(65).getStatsPorNivel(1));
                break;
            }
            case 7: {
                hechizosIniciales.put(125, Mundo.getHechizo(125).getStatsPorNivel(1));
                hechizosIniciales.put(128, Mundo.getHechizo(128).getStatsPorNivel(1));
                hechizosIniciales.put(121, Mundo.getHechizo(121).getStatsPorNivel(1));
                break;
            }
            case 6: {
                hechizosIniciales.put(102, Mundo.getHechizo(102).getStatsPorNivel(1));
                hechizosIniciales.put(103, Mundo.getHechizo(103).getStatsPorNivel(1));
                hechizosIniciales.put(105, Mundo.getHechizo(105).getStatsPorNivel(1));
                break;
            }
            case 9: {
                hechizosIniciales.put(161, Mundo.getHechizo(161).getStatsPorNivel(1));
                hechizosIniciales.put(169, Mundo.getHechizo(169).getStatsPorNivel(1));
                hechizosIniciales.put(164, Mundo.getHechizo(164).getStatsPorNivel(1));
                break;
            }
            case 8: {
                hechizosIniciales.put(143, Mundo.getHechizo(143).getStatsPorNivel(1));
                hechizosIniciales.put(141, Mundo.getHechizo(141).getStatsPorNivel(1));
                hechizosIniciales.put(142, Mundo.getHechizo(142).getStatsPorNivel(1));
                break;
            }
            case 10: {
                hechizosIniciales.put(183, Mundo.getHechizo(183).getStatsPorNivel(1));
                hechizosIniciales.put(200, Mundo.getHechizo(200).getStatsPorNivel(1));
                hechizosIniciales.put(193, Mundo.getHechizo(193).getStatsPorNivel(1));
                break;
            }
            case 2: {
                hechizosIniciales.put(34, Mundo.getHechizo(34).getStatsPorNivel(1));
                hechizosIniciales.put(21, Mundo.getHechizo(21).getStatsPorNivel(1));
                hechizosIniciales.put(23, Mundo.getHechizo(23).getStatsPorNivel(1));
                break;
            }
            case 5: {
                hechizosIniciales.put(82, Mundo.getHechizo(82).getStatsPorNivel(1));
                hechizosIniciales.put(81, Mundo.getHechizo(81).getStatsPorNivel(1));
                hechizosIniciales.put(83, Mundo.getHechizo(83).getStatsPorNivel(1));
                break;
            }
            case 12: {
                hechizosIniciales.put(686, Mundo.getHechizo(686).getStatsPorNivel(1));
                hechizosIniciales.put(692, Mundo.getHechizo(692).getStatsPorNivel(1));
                hechizosIniciales.put(687, Mundo.getHechizo(687).getStatsPorNivel(1));
                break;
            }
            case 3: {
                hechizosIniciales.put(51, Mundo.getHechizo(51).getStatsPorNivel(1));
                hechizosIniciales.put(43, Mundo.getHechizo(43).getStatsPorNivel(1));
                hechizosIniciales.put(41, Mundo.getHechizo(41).getStatsPorNivel(1));
                break;
            }
            case 11: {
                hechizosIniciales.put(432, Mundo.getHechizo(432).getStatsPorNivel(1));
                hechizosIniciales.put(431, Mundo.getHechizo(431).getStatsPorNivel(1));
                hechizosIniciales.put(434, Mundo.getHechizo(434).getStatsPorNivel(1));
            }
        }
        return hechizosIniciales;
    }

    public static int getBasePDV(int claseID) {
        switch (claseID) {
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 12: {
                return 50;
            }
            case 11: {
                return 100;
            }
        }
        return 50;
    }

    public static int getRepartoPuntoSegunClase(int claseID, int statID, int val) {
        switch (statID) {
            case 11: {
                return 1;
            }
            case 12: {
                return 3;
            }
            case 10: {
                switch (claseID) {
                    case 11: {
                        return 3;
                    }
                    case 1: {
                        if (val < 50) {
                            return 2;
                        }
                        if (val < 150) {
                            return 3;
                        }
                        if (val < 250) {
                            return 4;
                        }
                        return 5;
                    }
                    case 5: {
                        if (val < 50) {
                            return 2;
                        }
                        if (val < 150) {
                            return 3;
                        }
                        if (val < 250) {
                            return 4;
                        }
                        return 5;
                    }
                    case 4: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 2: {
                        if (val < 50) {
                            return 2;
                        }
                        if (val < 150) {
                            return 3;
                        }
                        if (val < 250) {
                            return 4;
                        }
                        return 5;
                    }
                    case 7: {
                        if (val < 50) {
                            return 2;
                        }
                        if (val < 150) {
                            return 3;
                        }
                        if (val < 250) {
                            return 4;
                        }
                        return 5;
                    }
                    case 12: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        return 3;
                    }
                    case 10: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 250) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 9: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 150) {
                            return 2;
                        }
                        if (val < 250) {
                            return 3;
                        }
                        if (val < 350) {
                            return 4;
                        }
                        return 5;
                    }
                    case 3: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 150) {
                            return 2;
                        }
                        if (val < 250) {
                            return 3;
                        }
                        if (val < 350) {
                            return 4;
                        }
                        return 5;
                    }
                    case 6: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 8: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                }
                break;
            }
            case 13: {
                switch (claseID) {
                    case 1: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 5: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 11: {
                        return 3;
                    }
                    case 4: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 10: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 12: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        return 3;
                    }
                    case 8: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 3: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 150) {
                            return 2;
                        }
                        if (val < 230) {
                            return 3;
                        }
                        if (val < 330) {
                            return 4;
                        }
                        return 5;
                    }
                    case 2: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 6: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 7: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 9: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                }
                break;
            }
            case 14: {
                switch (claseID) {
                    case 1: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 5: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 11: {
                        return 3;
                    }
                    case 4: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 10: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 12: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        return 3;
                    }
                    case 7: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 8: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 3: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 6: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 100) {
                            return 2;
                        }
                        if (val < 150) {
                            return 3;
                        }
                        if (val < 200) {
                            return 4;
                        }
                        return 5;
                    }
                    case 9: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 100) {
                            return 2;
                        }
                        if (val < 150) {
                            return 3;
                        }
                        if (val < 200) {
                            return 4;
                        }
                        return 5;
                    }
                    case 2: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                }
                break;
            }
            case 15: {
                switch (claseID) {
                    case 5: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 1: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 11: {
                        return 3;
                    }
                    case 4: {
                        if (val < 50) {
                            return 2;
                        }
                        if (val < 150) {
                            return 3;
                        }
                        if (val < 250) {
                            return 4;
                        }
                        return 5;
                    }
                    case 10: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 3: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 60) {
                            return 2;
                        }
                        if (val < 100) {
                            return 3;
                        }
                        if (val < 140) {
                            return 4;
                        }
                        return 5;
                    }
                    case 12: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        return 3;
                    }
                    case 8: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                    case 7: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 9: {
                        if (val < 50) {
                            return 1;
                        }
                        if (val < 150) {
                            return 2;
                        }
                        if (val < 250) {
                            return 3;
                        }
                        if (val < 350) {
                            return 4;
                        }
                        return 5;
                    }
                    case 2: {
                        if (val < 100) {
                            return 1;
                        }
                        if (val < 200) {
                            return 2;
                        }
                        if (val < 300) {
                            return 3;
                        }
                        if (val < 400) {
                            return 4;
                        }
                        return 5;
                    }
                    case 6: {
                        if (val < 20) {
                            return 1;
                        }
                        if (val < 40) {
                            return 2;
                        }
                        if (val < 60) {
                            return 3;
                        }
                        if (val < 80) {
                            return 4;
                        }
                        return 5;
                    }
                }
            }
        }
        return 5;
    }

    public static int agresionPorNivel(int nivel) {
        int distancia = 0;
        distancia = nivel / 600;
        if (nivel > 600) {
            distancia = 10;
        }
        return distancia;
    }

    public static boolean esUbicacionValidaObjeto(Objeto.ObjetoModelo objetoMod, int pos) {
        switch (objetoMod.getTipo()) {
            case 1: {
                if (pos != 0) break;
                return true;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 19: 
            case 20: 
            case 21: 
            case 22: 
            case 83: 
            case 114: {
                if (pos != 1) break;
                return true;
            }
            case 9: {
                if (pos != 2 && pos != 4) break;
                return true;
            }
            case 10: {
                if (pos != 3) break;
                return true;
            }
            case 11: {
                if (pos != 5) break;
                return true;
            }
            case 16: {
                if (pos != 6) break;
                return true;
            }
            case 17: {
                if (pos != 7) break;
                return true;
            }
            case 81: {
                if (pos != 7) break;
                return true;
            }
            case 18: {
                if (pos != 8) break;
                return true;
            }
            case 23: {
                if (pos != 9 && pos != 10 && pos != 11 && pos != 12 && pos != 13 && pos != 14) break;
                return true;
            }
            case 82: {
                if (pos != 15) break;
                return true;
            }
            case 12: 
            case 13: 
            case 14: 
            case 28: 
            case 33: 
            case 37: 
            case 41: 
            case 42: 
            case 49: 
            case 63: 
            case 64: 
            case 69: 
            case 70: 
            case 73: 
            case 74: 
            case 75: 
            case 79: 
            case 85: 
            case 87: 
            case 89: 
            case 93: 
            case 94: 
            case 112: 
            case 117: {
                if (pos < 35 || pos > 48) break;
                return true;
            }
        }
        return false;
    }

    public static int getClasePorObjMod(int objModelo) {
        switch (objModelo) {
            case 9544: {
                return 17;
            }
            case 9545: {
                return 16;
            }
            case 9546: {
                return 15;
            }
            case 9547: {
                return 13;
            }
            case 9548: {
                return 14;
            }
            case 10125: {
                return 19;
            }
            case 10126: {
                return 21;
            }
            case 10127: {
                return 20;
            }
            case 10133: {
                return 18;
            }
        }
        return -1;
    }

    public static void subirNivelAprenderHechizos(Personaje perso, int nivel) {
        switch (perso.getClase(true)) {
            case 1: {
                if (nivel == 3) {
                    perso.aprenderHechizo(4, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(2, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(1, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(9, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(18, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(20, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(14, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(19, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(5, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(16, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(8, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(12, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(11, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(10, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(7, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(15, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(13, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1901, 1, false, false);
                break;
            }
            case 2: {
                if (nivel == 3) {
                    perso.aprenderHechizo(26, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(22, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(35, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(28, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(37, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(30, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(27, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(24, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(33, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(25, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(38, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(36, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(32, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(29, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(39, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(40, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(31, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1902, 1, false, false);
                break;
            }
            case 3: {
                if (nivel == 3) {
                    perso.aprenderHechizo(49, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(42, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(47, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(48, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(45, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(53, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(46, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(52, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(44, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(50, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(54, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(55, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(56, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(58, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(59, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(57, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(60, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1903, 1, false, false);
                break;
            }
            case 4: {
                if (nivel == 3) {
                    perso.aprenderHechizo(66, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(68, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(63, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(74, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(64, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(79, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(78, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(71, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(62, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(69, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(77, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(73, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(67, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(70, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(75, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(76, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(80, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1904, 1, false, false);
                break;
            }
            case 5: {
                if (nivel == 3) {
                    perso.aprenderHechizo(84, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(100, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(92, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(88, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(93, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(85, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(96, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(98, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(86, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(89, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(90, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(87, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(94, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(99, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(95, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(91, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(97, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1905, 1, false, false);
                break;
            }
            case 6: {
                if (nivel == 3) {
                    perso.aprenderHechizo(109, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(113, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(111, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(104, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(119, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(101, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(107, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(116, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(106, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(117, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(108, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(115, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(118, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(110, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(112, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(114, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(120, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1906, 1, false, false);
                break;
            }
            case 7: {
                if (nivel == 3) {
                    perso.aprenderHechizo(124, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(122, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(126, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(127, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(123, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(130, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(131, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(132, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(133, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(134, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(135, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(129, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(136, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(137, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(138, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(139, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(140, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1907, 1, false, false);
                break;
            }
            case 8: {
                if (nivel == 3) {
                    perso.aprenderHechizo(144, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(145, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(146, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(147, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(148, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(154, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(150, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(151, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(155, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(152, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(153, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(149, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(156, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(157, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(158, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(160, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(159, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1908, 1, false, false);
                break;
            }
            case 9: {
                if (nivel == 3) {
                    perso.aprenderHechizo(163, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(165, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(172, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(167, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(168, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(162, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(170, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(171, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(166, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(173, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(174, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(176, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(175, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(178, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(177, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(179, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(180, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1909, 1, false, false);
                break;
            }
            case 10: {
                if (nivel == 3) {
                    perso.aprenderHechizo(198, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(195, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(182, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(192, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(197, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(189, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(181, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(199, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(191, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(186, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(196, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(190, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(194, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(185, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(184, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(188, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(187, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1910, 1, false, false);
                break;
            }
            case 11: {
                if (nivel == 3) {
                    perso.aprenderHechizo(444, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(449, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(436, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(437, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(439, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(433, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(443, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(440, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(442, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(441, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(445, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(438, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(446, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(447, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(448, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(435, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(450, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1911, 1, false, false);
                break;
            }
            case 12: {
                if (nivel == 3) {
                    perso.aprenderHechizo(689, 1, false, false);
                }
                if (nivel == 6) {
                    perso.aprenderHechizo(690, 1, false, false);
                }
                if (nivel == 9) {
                    perso.aprenderHechizo(691, 1, false, false);
                }
                if (nivel == 13) {
                    perso.aprenderHechizo(688, 1, false, false);
                }
                if (nivel == 17) {
                    perso.aprenderHechizo(693, 1, false, false);
                }
                if (nivel == 21) {
                    perso.aprenderHechizo(694, 1, false, false);
                }
                if (nivel == 26) {
                    perso.aprenderHechizo(695, 1, false, false);
                }
                if (nivel == 31) {
                    perso.aprenderHechizo(696, 1, false, false);
                }
                if (nivel == 36) {
                    perso.aprenderHechizo(697, 1, false, false);
                }
                if (nivel == 42) {
                    perso.aprenderHechizo(698, 1, false, false);
                }
                if (nivel == 48) {
                    perso.aprenderHechizo(699, 1, false, false);
                }
                if (nivel == 54) {
                    perso.aprenderHechizo(700, 1, false, false);
                }
                if (nivel == 60) {
                    perso.aprenderHechizo(701, 1, false, false);
                }
                if (nivel == 70) {
                    perso.aprenderHechizo(702, 1, false, false);
                }
                if (nivel == 80) {
                    perso.aprenderHechizo(703, 1, false, false);
                }
                if (nivel == 90) {
                    perso.aprenderHechizo(704, 1, false, false);
                }
                if (nivel == 100) {
                    perso.aprenderHechizo(705, 1, false, false);
                }
                if (nivel != 200) break;
                perso.aprenderHechizo(1912, 1, false, false);
            }
        }
    }

    public static int getColorGlifo(int hechizo) {
        switch (hechizo) {
            case 10: 
            case 2033: {
                return 4;
            }
            case 12: 
            case 2034: {
                return 3;
            }
            case 13: 
            case 2035: {
                return 6;
            }
            case 15: 
            case 2036: {
                return 5;
            }
            case 17: 
            case 2037: {
                return 2;
            }
        }
        return 4;
    }

    public static int getColorTrampa(int hechizo) {
        switch (hechizo) {
            case 65: {
                return 7;
            }
            case 69: {
                return 10;
            }
            case 71: 
            case 2068: {
                return 9;
            }
            case 73: {
                return 12;
            }
            case 77: 
            case 2071: {
                return 11;
            }
            case 79: 
            case 2072: {
                return 8;
            }
            case 80: {
                return 13;
            }
        }
        return 7;
    }

    public static ArrayList<Oficio.AccionTrabajo> getTrabajosPorOficios(int idOficio, int nivel) {
        ArrayList<Oficio.AccionTrabajo> listaTrabajos = new ArrayList<Oficio.AccionTrabajo>();
        int tiempoGanado = nivel * 100;
        int dropGanado = nivel / 5;
        switch (idOficio) {
            case 16: {
                listaTrabajos.add(new Oficio.AccionTrabajo(11, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(12, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 27: {
                listaTrabajos.add(new Oficio.AccionTrabajo(64, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(123, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(63, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 15: {
                listaTrabajos.add(new Oficio.AccionTrabajo(13, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(14, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 65: {
                listaTrabajos.add(new Oficio.AccionTrabajo(171, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(182, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 13: {
                listaTrabajos.add(new Oficio.AccionTrabajo(15, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(149, 3, 3, true, nivel, 0));
                break;
            }
            case 19: {
                listaTrabajos.add(new Oficio.AccionTrabajo(16, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(148, 3, 3, true, nivel, 0));
                break;
            }
            case 18: {
                listaTrabajos.add(new Oficio.AccionTrabajo(17, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(147, 3, 3, true, nivel, 0));
                break;
            }
            case 17: {
                listaTrabajos.add(new Oficio.AccionTrabajo(18, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(142, 3, 3, true, nivel, 0));
                break;
            }
            case 14: {
                listaTrabajos.add(new Oficio.AccionTrabajo(19, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(144, 3, 3, true, nivel, 0));
                break;
            }
            case 11: {
                listaTrabajos.add(new Oficio.AccionTrabajo(20, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(145, 3, 3, true, nivel, 0));
                break;
            }
            case 20: {
                listaTrabajos.add(new Oficio.AccionTrabajo(21, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(146, 3, 3, true, nivel, 0));
                break;
            }
            case 31: {
                listaTrabajos.add(new Oficio.AccionTrabajo(65, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(143, 3, 3, true, nivel, 0));
                break;
            }
            case 60: {
                listaTrabajos.add(new Oficio.AccionTrabajo(156, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 62: {
                listaTrabajos.add(new Oficio.AccionTrabajo(163, 3, 3, true, nivel, 0));
                listaTrabajos.add(new Oficio.AccionTrabajo(164, 3, 3, true, nivel, 0));
                break;
            }
            case 63: {
                listaTrabajos.add(new Oficio.AccionTrabajo(169, 3, 3, true, nivel, 0));
                listaTrabajos.add(new Oficio.AccionTrabajo(168, 3, 3, true, nivel, 0));
                break;
            }
            case 64: {
                listaTrabajos.add(new Oficio.AccionTrabajo(165, 3, 3, true, nivel, 0));
                listaTrabajos.add(new Oficio.AccionTrabajo(167, 3, 3, true, nivel, 0));
                listaTrabajos.add(new Oficio.AccionTrabajo(166, 3, 3, true, nivel, 0));
                break;
            }
            case 50: {
                listaTrabajos.add(new Oficio.AccionTrabajo(120, 3, 3, true, nivel, 0));
                break;
            }
            case 49: {
                listaTrabajos.add(new Oficio.AccionTrabajo(119, 3, 3, true, nivel, 0));
                break;
            }
            case 48: {
                listaTrabajos.add(new Oficio.AccionTrabajo(118, 3, 3, true, nivel, 0));
                break;
            }
            case 47: {
                listaTrabajos.add(new Oficio.AccionTrabajo(115, 3, 3, true, nivel, 0));
                break;
            }
            case 43: {
                listaTrabajos.add(new Oficio.AccionTrabajo(1, 3, 3, true, nivel, 0));
                break;
            }
            case 44: {
                listaTrabajos.add(new Oficio.AccionTrabajo(113, 3, 3, true, nivel, 0));
                break;
            }
            case 45: {
                listaTrabajos.add(new Oficio.AccionTrabajo(116, 3, 3, true, nivel, 0));
                break;
            }
            case 46: {
                listaTrabajos.add(new Oficio.AccionTrabajo(117, 3, 3, true, nivel, 0));
                break;
            }
            case 41: {
                listaTrabajos.add(new Oficio.AccionTrabajo(134, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 56: {
                listaTrabajos.add(new Oficio.AccionTrabajo(132, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 58: {
                listaTrabajos.add(new Oficio.AccionTrabajo(135, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 25: {
                listaTrabajos.add(new Oficio.AccionTrabajo(27, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(109, 3, 3, true, 100, -1));
                break;
            }
            case 24: {
                if (nivel > 99) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(161, -19 + dropGanado, -18 + dropGanado, false, 12000 - tiempoGanado, 60));
                }
                if (nivel > 79) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(30, -15 + dropGanado, -14 + dropGanado, false, 12000 - tiempoGanado, 55));
                }
                if (nivel > 69) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(31, -13 + dropGanado, -12 + dropGanado, false, 12000 - tiempoGanado, 50));
                }
                if (nivel > 59) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(29, -11 + dropGanado, -10 + dropGanado, false, 12000 - tiempoGanado, 40));
                }
                if (nivel > 49) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(55, -9 + dropGanado, -8 + dropGanado, false, 12000 - tiempoGanado, 35));
                    listaTrabajos.add(new Oficio.AccionTrabajo(162, -9 + dropGanado, -8 + dropGanado, false, 12000 - tiempoGanado, 35));
                }
                if (nivel > 39) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(56, -7 + dropGanado, -6 + dropGanado, false, 12000 - tiempoGanado, 30));
                }
                if (nivel > 29) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(28, -5 + dropGanado, -4 + dropGanado, false, 12000 - tiempoGanado, 25));
                }
                if (nivel > 19) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(26, -3 + dropGanado, -2 + dropGanado, false, 12000 - tiempoGanado, 20));
                }
                if (nivel > 9) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(25, -1 + dropGanado, 0 + dropGanado, false, 12000 - tiempoGanado, 15));
                }
                listaTrabajos.add(new Oficio.AccionTrabajo(24, 1 + dropGanado, 2 + dropGanado, false, 12000 - tiempoGanado, 10));
                listaTrabajos.add(new Oficio.AccionTrabajo(32, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(48, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 36: {
                if (nivel > 74) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(131, 0, 1, false, 12000 - tiempoGanado, 35));
                }
                if (nivel > 69) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(127, 0, 1, false, 12000 - tiempoGanado, 35));
                }
                if (nivel > 49) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(130, 0, 1, false, 12000 - tiempoGanado, 30));
                }
                if (nivel > 39) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(126, 0, 1, false, 12000 - tiempoGanado, 25));
                }
                if (nivel > 19) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(129, 0, 1, false, 12000 - tiempoGanado, 20));
                }
                if (nivel > 9) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(125, 0, 1, false, 12000 - tiempoGanado, 15));
                }
                listaTrabajos.add(new Oficio.AccionTrabajo(140, 0, 1, false, 12000 - tiempoGanado, 50));
                listaTrabajos.add(new Oficio.AccionTrabajo(136, 1, 1, false, 12000 - tiempoGanado, 5));
                listaTrabajos.add(new Oficio.AccionTrabajo(124, 0, 1, false, 12000 - tiempoGanado, 10));
                listaTrabajos.add(new Oficio.AccionTrabajo(128, 0, 1, false, 12000 - tiempoGanado, 10));
                listaTrabajos.add(new Oficio.AccionTrabajo(133, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 26: {
                if (nivel > 49) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(160, -9 + dropGanado, -8 + dropGanado, false, 12000 - tiempoGanado, 35));
                    listaTrabajos.add(new Oficio.AccionTrabajo(74, -9 + dropGanado, -8 + dropGanado, false, 12000 - tiempoGanado, 35));
                }
                if (nivel > 39) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(73, -7 + dropGanado, -6 + dropGanado, false, 12000 - tiempoGanado, 30));
                }
                if (nivel > 29) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(72, -5 + dropGanado, -4 + dropGanado, false, 12000 - tiempoGanado, 25));
                }
                if (nivel > 19) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(71, -3 + dropGanado, -2 + dropGanado, false, 12000 - tiempoGanado, 20));
                }
                if (nivel > 9) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(54, -1 + dropGanado, 0 + dropGanado, false, 12000 - tiempoGanado, 15));
                }
                listaTrabajos.add(new Oficio.AccionTrabajo(68, 1 + dropGanado, 2 + dropGanado, false, 12000 - tiempoGanado, 10));
                listaTrabajos.add(new Oficio.AccionTrabajo(23, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 2: {
                if (nivel > 99) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(158, -19 + dropGanado, -18 + dropGanado, false, 12000 - tiempoGanado, 75));
                }
                if (nivel > 89) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(35, -17 + dropGanado, -16 + dropGanado, false, 12000 - tiempoGanado, 70));
                }
                if (nivel > 79) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(38, -15 + dropGanado, -14 + dropGanado, false, 12000 - tiempoGanado, 65));
                    listaTrabajos.add(new Oficio.AccionTrabajo(155, -15 + dropGanado, -14 + dropGanado, false, 12000 - tiempoGanado, 65));
                }
                if (nivel > 74) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(174, -14 + dropGanado, -13 + dropGanado, false, 12000 - tiempoGanado, 55));
                }
                if (nivel > 69) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(34, -13 + dropGanado, -12 + dropGanado, false, 12000 - tiempoGanado, 50));
                }
                if (nivel > 59) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(41, -11 + dropGanado, -10 + dropGanado, false, 12000 - tiempoGanado, 45));
                }
                if (nivel > 49) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(33, -9 + dropGanado, -8 + dropGanado, false, 12000 - tiempoGanado, 40));
                    listaTrabajos.add(new Oficio.AccionTrabajo(154, -9 + dropGanado, -8 + dropGanado, false, 12000 - tiempoGanado, 40));
                }
                if (nivel > 39) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(37, -7 + dropGanado, -6 + dropGanado, false, 12000 - tiempoGanado, 35));
                }
                if (nivel > 34) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(139, -6 + dropGanado, -5 + dropGanado, false, 12000 - tiempoGanado, 30));
                    listaTrabajos.add(new Oficio.AccionTrabajo(141, -6 + dropGanado, -5 + dropGanado, false, 12000 - tiempoGanado, 30));
                }
                if (nivel > 29) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(10, -5 + dropGanado, -4 + dropGanado, false, 12000 - tiempoGanado, 25));
                }
                if (nivel > 19) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(40, -3 + dropGanado, -2 + dropGanado, false, 12000 - tiempoGanado, 20));
                }
                if (nivel > 9) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(39, -1 + dropGanado, 0 + dropGanado, false, 12000 - tiempoGanado, 15));
                }
                listaTrabajos.add(new Oficio.AccionTrabajo(6, 1 + dropGanado, 2 + dropGanado, false, 12000 - tiempoGanado, 10));
                listaTrabajos.add(new Oficio.AccionTrabajo(101, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                break;
            }
            case 28: {
                if (nivel > 69) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(54, -13 + dropGanado, -12 + dropGanado, false, 12000 - tiempoGanado, 45));
                }
                if (nivel > 59) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(58, -11 + dropGanado, -10 + dropGanado, false, 12000 - tiempoGanado, 40));
                }
                if (nivel > 49) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(159, -9 + dropGanado, -8 + dropGanado, false, 12000 - tiempoGanado, 35));
                    listaTrabajos.add(new Oficio.AccionTrabajo(52, -9 + dropGanado, -8 + dropGanado, false, 12000 - tiempoGanado, 35));
                }
                if (nivel > 39) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(50, -7 + dropGanado, -6 + dropGanado, false, 12000 - tiempoGanado, 30));
                }
                if (nivel > 29) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(46, -5 + dropGanado, -4 + dropGanado, false, 12000 - tiempoGanado, 25));
                }
                if (nivel > 19) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(57, -3 + dropGanado, -2 + dropGanado, false, 12000 - tiempoGanado, 20));
                }
                if (nivel > 9) {
                    listaTrabajos.add(new Oficio.AccionTrabajo(53, -1 + dropGanado, 0 + dropGanado, false, 12000 - tiempoGanado, 15));
                }
                listaTrabajos.add(new Oficio.AccionTrabajo(45, 1 + dropGanado, 2 + dropGanado, false, 12000 - tiempoGanado, 10));
                listaTrabajos.add(new Oficio.AccionTrabajo(47, 2, Constantes.getIngMaxPorNivel(nivel), true, Constantes.getSuerteMaxPorNivel(nivel), -1));
                listaTrabajos.add(new Oficio.AccionTrabajo(122, 1, 1, true, 100, -1));
            }
        }
        return listaTrabajos;
    }

    public static int getIngMaxPorNivel(int nivel) {
        if (nivel < 10) {
            return 2;
        }
        if (nivel >= 100) {
            return 9;
        }
        return nivel / 20 + 3;
    }

    public static int getSuerteMaxPorNivel(int nivel) {
        if (nivel < 10) {
            return 50;
        }
        return 54 + (int)((float)nivel / 10.0f - 1.0f) * 5;
    }

    public static int getSuerteNivelYSlots(int nivel, int slots) {
        if (nivel < 10) {
            return 50;
        }
        return (int)((54.0f + ((float)nivel / 10.0f - 1.0f) * 5.0f) * ((float)Constantes.getIngMaxPorNivel(nivel) / (float)slots));
    }

    public static int calculXpGanadaEnOficio(int nivel, int nroCasillas) {
        if (nivel == 100) {
            return 0;
        }
        switch (nroCasillas) {
            case 1: {
                return 3;
            }
            case 2: {
                return 10;
            }
            case 3: {
                if (nivel > 9) {
                    return 25;
                }
                return 0;
            }
            case 4: {
                if (nivel > 19) {
                    return 50;
                }
                return 0;
            }
            case 5: {
                if (nivel > 39) {
                    return 100;
                }
                return 0;
            }
            case 6: {
                if (nivel > 59) {
                    return 250;
                }
                return 0;
            }
            case 7: {
                if (nivel > 79) {
                    return 500;
                }
                return 0;
            }
            case 8: {
                if (nivel > 99) {
                    return 1000;
                }
                return 0;
            }
        }
        return 0;
    }

    public static boolean esTrabajo(int trabajoID) {
        for (int v = 0; v < ACCIONES_TRABAJO.length; ++v) {
            if (ACCIONES_TRABAJO[v][0] != trabajoID) continue;
            return true;
        }
        return false;
    }

    public static int getObjetoPorTrabajo(int trabajoID, boolean especial) {
		try {
			ArrayList<ArrayList<Integer>> obj = new ArrayList<ArrayList<Integer>>();
			for (int v = 0; v < ACCIONES_TRABAJO.length; v++) {
				if (ACCIONES_TRABAJO[v][0] == trabajoID) {
					ArrayList<Integer> x = new ArrayList<Integer>();
					x.add(ACCIONES_TRABAJO[v][1]);
					if (ACCIONES_TRABAJO[v].length > 2) {
						x.add(ACCIONES_TRABAJO[v][2]);
					}
					obj.add(x);
				}
			}
			if (obj.size() == 0) {
				return -1;
			} else if (obj.size() == 1) {
				return obj.get(0).size() > 1 && especial ? obj.get(0).get(1) : obj.get(0).get(0);
			} else if (obj.size() >= 2) {
				ArrayList<Integer> z = new ArrayList<Integer>();
				z = obj.get(Fórmulas.getRandomValor(0, obj.size() - 1));
				return z.size() > 1 && especial ? z.get(1) : z.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

    public static int getSuertePorNroCasillaYNivel(int nivel, int nroCasilla) {
        if (nroCasilla <= Constantes.getIngMaxPorNivel(nivel) - 2) {
            return 100;
        }
        return Constantes.getSuerteNivelYSlots(nivel, nroCasilla);
    }

    public static boolean esOficioMago(int id) {
        return id >= 43 && id <= 50 || id >= 62 && id <= 64;
    }

    public static String getStringColorDragopavo(int color) {
        switch (color) {
            case 1: {
                return "16772045,-1,16772045";
            }
            case 3: {
                return "1245184,393216,1245184";
            }
            case 6: {
                return "16747520,-1,16747520";
            }
            case 9: {
                return "1182992,16777200,16777200";
            }
            case 10: {
                return "16747520,-1,16747520";
            }
            case 11: {
                return "16747520,16777200,16777200";
            }
            case 12: {
                return "16747520,1703936,1774084";
            }
            case 15: {
                return "4251856,-1,4251856";
            }
            case 16: {
                return "16777200,16777200,16777200";
            }
            case 17: {
                return "4915330,-1,4915330";
            }
            case 18: {
                return "16766720,16766720,16766720";
            }
            case 19: {
                return "14423100,-1,14423100";
            }
            case 20: {
                return "16772045,-1,16772045";
            }
            case 21: {
                return "3329330,-1,3329330";
            }
            case 22: {
                return "15859954,16777200,15859954";
            }
            case 23: {
                return "14524637,-1,14524637";
            }
            case 33: {
                return "16772045,16766720,16766720";
            }
            case 34: {
                return "16772045,1245184,1245184";
            }
            case 35: {
                return "16772045,3329330,3329330";
            }
            case 36: {
                return "16772045,4915330,4915330";
            }
            case 37: {
                return "16772045,16777200,16777200";
            }
            case 38: {
                return "16772045,16747520,16747520";
            }
            case 39: {
                return "16772045,4251856,4251856";
            }
            case 40: {
                return "16772045,15859954,15859954";
            }
            case 41: {
                return "16772045,14423100,14423100";
            }
            case 42: {
                return "1245184,16766720,16766720";
            }
            case 43: {
                return "16766720,3329330,3329330";
            }
            case 44: {
                return "16766720,4915330,4915330";
            }
            case 45: {
                return "16766720,16777200,16777200";
            }
            case 46: {
                return "16766720,16747520,16747520";
            }
            case 47: {
                return "16766720,4251856,4251856";
            }
            case 48: {
                return "16766720,15859954,15859954";
            }
            case 49: {
                return "16766720,14423100,14423100";
            }
            case 50: {
                return "1245184,3329330,3329330";
            }
            case 51: {
                return "4915330,4915330,1245184";
            }
            case 52: {
                return "1245184,4251856,4251856";
            }
            case 53: {
                return "15859954,0,0";
            }
            case 54: {
                return "14423100,14423100,1245184";
            }
            case 55: {
                return "3329330,4915330,4915330";
            }
            case 56: {
                return "3329330,16777200,16777200";
            }
            case 57: {
                return "3329330,16747520,16747520";
            }
            case 58: {
                return "3329330,4251856,4251856";
            }
            case 59: {
                return "3329330,15859954,15859954";
            }
            case 60: {
                return "3329330,14423100,14423100";
            }
            case 61: {
                return "4915330,16777200,16777200";
            }
            case 62: {
                return "4915330,16747520,16747520";
            }
            case 63: {
                return "4915330,4251856,4251856";
            }
            case 64: {
                return "4915330,15859954,15859954";
            }
            case 65: {
                return "14423100,4915330,4915330";
            }
            case 66: {
                return "16777200,4251856,4251856";
            }
            case 67: {
                return "16777200,16731355,16711910";
            }
            case 68: {
                return "14423100,16777200,16777200";
            }
            case 69: {
                return "4251856,16747520,16747520";
            }
            case 70: {
                return "14315734,16747520,16747520";
            }
            case 71: {
                return "14423100,16747520,16747520";
            }
            case 72: {
                return "15859954,4251856,4251856";
            }
            case 73: {
                return "14423100,4251856,4251856";
            }
            case 74: {
                return "16766720,16766720,16766720";
            }
            case 76: {
                return "14315734,14423100,14423100";
            }
            case 77: {
                return "14524637,16772045,16772045";
            }
            case 78: {
                return "14524637,16766720,16766720";
            }
            case 79: {
                return "14524637,1245184,1245184";
            }
            case 80: {
                return "14524637,3329330,3329330";
            }
            case 82: {
                return "14524637,4915330,4915330";
            }
            case 83: {
                return "14524637,16777200,16777200";
            }
            case 84: {
                return "14524637,16747520,16747520";
            }
            case 85: {
                return "14524637,4251856,4251856";
            }
            case 86: {
                return "14524637,15859954,15859954";
            }
            case 87: {
                return "14524637,14423100,14423100";
            }
        }
        return "-1,-1,-1";
    }

    public static int getGeneracion(int color) {
        switch (color) {
            case 10: 
            case 18: 
            case 20: {
                return 1;
            }
            case 33: 
            case 38: 
            case 46: {
                return 2;
            }
            case 3: 
            case 17: {
                return 3;
            }
            case 12: 
            case 34: 
            case 36: 
            case 42: 
            case 44: 
            case 51: 
            case 62: {
                return 4;
            }
            case 19: 
            case 22: {
                return 5;
            }
            case 40: 
            case 41: 
            case 48: 
            case 49: 
            case 53: 
            case 54: 
            case 64: 
            case 65: 
            case 70: 
            case 71: 
            case 76: {
                return 6;
            }
            case 15: 
            case 16: {
                return 7;
            }
            case 9: 
            case 11: 
            case 37: 
            case 39: 
            case 45: 
            case 47: 
            case 52: 
            case 61: 
            case 63: 
            case 66: 
            case 67: 
            case 68: 
            case 69: 
            case 72: 
            case 73: {
                return 8;
            }
            case 21: 
            case 23: {
                return 9;
            }
            case 35: 
            case 43: 
            case 50: 
            case 55: 
            case 56: 
            case 57: 
            case 58: 
            case 59: 
            case 60: 
            case 77: 
            case 78: 
            case 79: 
            case 80: 
            case 82: 
            case 83: 
            case 84: 
            case 85: 
            case 86: {
                return 10;
            }
        }
        return 1;
    }

    public static int colorCria(int color1, int color2) {
        int colorcria = 1;
        int A = 0;
        int B = 0;
        int C = 0;
        if (color1 == 75) {
            color1 = 10;
        }
        if (color2 == 75) {
            color2 = 10;
        }
        if (color1 > color2) {
            A = color2;
            B = color1;
        } else if (color1 <= color2) {
            A = color1;
            B = color2;
        }
        if (A == 10 && B == 18) {
            C = 46;
        } else if (A == 10 && B == 20) {
            C = 38;
        } else if (A == 18 && B == 20) {
            C = 33;
        } else if (A == 33 && B == 38) {
            C = 17;
        } else if (A == 33 && B == 46) {
            C = 3;
        } else if (A == 10 && B == 17) {
            C = 62;
        } else if (A == 10 && B == 3) {
            C = 12;
        } else if (A == 17 && B == 20) {
            C = 36;
        } else if (A == 3 && B == 20) {
            C = 34;
        } else if (A == 17 && B == 18) {
            C = 44;
        } else if (A == 3 && B == 18) {
            C = 42;
        } else if (A == 3 && B == 17) {
            C = 51;
        } else if (A == 38 && B == 51) {
            C = 19;
        } else if (A == 46 && B == 51) {
            C = 22;
        } else if (A == 10 && B == 19) {
            C = 71;
        } else if (A == 10 && B == 22) {
            C = 70;
        } else if (A == 19 && B == 20) {
            C = 41;
        } else if (A == 20 && B == 22) {
            C = 40;
        } else if (A == 18 && B == 19) {
            C = 49;
        } else if (A == 18 && B == 22) {
            C = 48;
        } else if (A == 17 && B == 19) {
            C = 65;
        } else if (A == 17 && B == 22) {
            C = 64;
        } else if (A == 3 && B == 19) {
            C = 54;
        } else if (A == 3 && B == 22) {
            C = 53;
        } else if (A == 19 && B == 22) {
            C = 76;
        } else if (A == 53 && B == 76) {
            C = 15;
        } else if (A == 65 && B == 76) {
            C = 16;
        } else if (A == 10 && B == 16) {
            C = 11;
        } else if (A == 10 && B == 15) {
            C = 69;
        } else if (A == 16 && B == 20) {
            C = 37;
        } else if (A == 15 && B == 20) {
            C = 39;
        } else if (A == 16 && B == 18) {
            C = 45;
        } else if (A == 15 && B == 18) {
            C = 47;
        } else if (A == 16 && B == 17) {
            C = 61;
        } else if (A == 15 && B == 17) {
            C = 63;
        } else if (A == 3 && B == 16) {
            C = 9;
        } else if (A == 3 && B == 15) {
            C = 52;
        } else if (A == 16 && B == 19) {
            C = 68;
        } else if (A == 15 && B == 19) {
            C = 73;
        } else if (A == 16 && B == 22) {
            C = 67;
        } else if (A == 15 && B == 22) {
            C = 72;
        } else if (A == 15 && B == 16) {
            C = 66;
        } else if (A == 66 && B == 68) {
            C = 21;
        } else if (A == 66 && B == 72) {
            C = 23;
        } else if (A == 10 && B == 21) {
            C = 57;
        } else if (A == 20 && B == 21) {
            C = 35;
        } else if (A == 18 && B == 21) {
            C = 43;
        } else if (A == 3 && B == 21) {
            C = 50;
        } else if (A == 17 && B == 21) {
            C = 55;
        } else if (A == 16 && B == 21) {
            C = 56;
        } else if (A == 15 && B == 21) {
            C = 58;
        } else if (A == 21 && B == 22) {
            C = 59;
        } else if (A == 19 && B == 21) {
            C = 60;
        } else if (A == 20 && B == 23) {
            C = 77;
        } else if (A == 18 && B == 23) {
            C = 78;
        } else if (A == 3 && B == 23) {
            C = 79;
        } else if (A == 21 && B == 23) {
            C = 80;
        } else if (A == 17 && B == 23) {
            C = 82;
        } else if (A == 16 && B == 23) {
            C = 83;
        } else if (A == 10 && B == 23) {
            C = 84;
        } else if (A == 15 && B == 23) {
            C = 85;
        } else if (A == 22 && B == 23) {
            C = 86;
        } else if (A == 19 && B == 23) {
            C = 87;
        }
        ArrayList<Integer> posibles = new ArrayList<Integer>();
        posibles.add(18);
        posibles.add(20);
        posibles.add(A);
        posibles.add(B);
        posibles.add(10);
        posibles.add(18);
        if (C != 0) {
            for (int j = 11; j > Constantes.getGeneracion(C); --j) {
                posibles.add(C);
            }
        }
        colorcria = (Integer)posibles.get(Fórmulas.getRandomValor(0, posibles.size() - 1));
        return colorcria;
    }

    public static Personaje.Stats getStatsMonturaVIP(String definido, int nivel) {
        Personaje.Stats stats = new Personaje.Stats();
        if (definido.isEmpty() || definido == "") {
            return stats;
        }
        String[] statsD = definido.split(",");
        int a = statsD.length;
        ArrayList<String> s = new ArrayList<String>();
        double coef = (double)(a * a) / (double)(2 * a - 1);
        String[] arrstring = statsD;
        int n = statsD.length;
        for (int i = 0; i < n; ++i) {
            String todos = arrstring[i];
            if (s.contains(todos)) continue;
            if (todos.equalsIgnoreCase("sabiduria")) {
                stats.addStat(124, (int)((double)nivel / coef * 0.8));
            } else if (todos.equalsIgnoreCase("vitalidad")) {
                stats.addStat(125, (int)((double)nivel / coef * 4.0));
            } else if (todos.equalsIgnoreCase("inteligencia")) {
                stats.addStat(126, (int)((double)nivel / coef * 2.0));
            } else if (todos.equalsIgnoreCase("fuerza")) {
                stats.addStat(118, (int)((double)nivel / coef * 2.0));
            } else if (todos.equalsIgnoreCase("agilidad")) {
                stats.addStat(119, (int)((double)nivel / coef * 2.0));
            } else if (todos.equalsIgnoreCase("suerte")) {
                stats.addStat(123, (int)((double)nivel / coef * 2.0));
            } else if (todos.equalsIgnoreCase("alcance")) {
                stats.addStat(117, (int)((double)nivel / coef / 60.0));
            } else if (todos.equalsIgnoreCase("prospeccion")) {
                stats.addStat(176, (int)((double)nivel / coef * 0.6));
            } else if (todos.equalsIgnoreCase("raire")) {
                stats.addStat(212, (int)((double)nivel / coef / 5.0));
            } else if (todos.equalsIgnoreCase("ragua")) {
                stats.addStat(211, (int)((double)nivel / coef / 5.0));
            } else if (todos.equalsIgnoreCase("rtierra")) {
                stats.addStat(210, (int)((double)nivel / coef / 5.0));
            } else if (todos.equalsIgnoreCase("rfuego")) {
                stats.addStat(213, (int)((double)nivel / coef / 5.0));
            } else if (todos.equalsIgnoreCase("rneutral")) {
                stats.addStat(214, (int)((double)nivel / coef / 5.0));
            } else if (todos.equalsIgnoreCase("da\u00f1os")) {
                stats.addStat(138, (int)((double)nivel / coef * 0.7));
            } else if (todos.equalsIgnoreCase("iniciativa")) {
                stats.addStat(174, (int)((double)nivel / coef * 16.0));
            }
            s.add(todos);
        }
        return stats;
    }

    public static Personaje.Stats getStatsMontura(int color, int nivel) {
        Personaje.Stats stats = new Personaje.Stats();
        switch (color) {
            case 1: {
                break;
            }
            case 3: {
                stats.addStat(125, nivel / 2);
                stats.addStat(119, (int)((double)nivel / 1.25));
                break;
            }
            case 10: {
                stats.addStat(125, nivel);
                break;
            }
            case 20: {
                stats.addStat(174, nivel * 10);
                break;
            }
            case 18: {
                stats.addStat(125, nivel / 2);
                stats.addStat(124, (int)((double)nivel / 2.5));
                break;
            }
            case 38: {
                stats.addStat(174, nivel * 5);
                stats.addStat(125, nivel);
                stats.addStat(182, nivel / 50);
                break;
            }
            case 46: {
                stats.addStat(125, nivel);
                stats.addStat(124, nivel / 4);
                break;
            }
            case 33: {
                stats.addStat(174, nivel * 5);
                stats.addStat(124, nivel / 4);
                stats.addStat(125, nivel / 2);
                stats.addStat(182, nivel / 100);
                break;
            }
            case 17: {
                stats.addStat(123, (int)((double)nivel / 1.25));
                stats.addStat(125, nivel / 2);
                break;
            }
            case 62: {
                stats.addStat(125, (int)((double)nivel * 1.5));
                stats.addStat(123, (int)((double)nivel / 1.65));
                break;
            }
            case 12: {
                stats.addStat(125, (int)((double)nivel * 1.5));
                stats.addStat(119, (int)((double)nivel / 1.65));
                break;
            }
            case 36: {
                stats.addStat(174, nivel * 5);
                stats.addStat(125, nivel / 2);
                stats.addStat(123, (int)((double)nivel / 1.65));
                stats.addStat(182, nivel / 100);
                break;
            }
            case 19: {
                stats.addStat(118, (int)((double)nivel / 1.25));
                stats.addStat(125, nivel / 2);
                break;
            }
            case 22: {
                stats.addStat(126, (int)((double)nivel / 1.25));
                stats.addStat(125, nivel / 2);
                break;
            }
            case 48: {
                stats.addStat(125, nivel);
                stats.addStat(124, nivel / 4);
                stats.addStat(126, (int)((double)nivel / 1.65));
                break;
            }
            case 65: {
                stats.addStat(125, nivel);
                stats.addStat(123, nivel / 2);
                stats.addStat(118, nivel / 2);
                break;
            }
            case 64: {
                stats.addStat(125, nivel);
                stats.addStat(123, nivel / 2);
                stats.addStat(126, nivel / 2);
                break;
            }
            case 54: {
                stats.addStat(125, nivel);
                stats.addStat(118, nivel / 2);
                stats.addStat(119, nivel / 2);
                break;
            }
            case 53: {
                stats.addStat(125, nivel);
                stats.addStat(119, nivel / 2);
                stats.addStat(126, nivel / 2);
                break;
            }
            case 76: {
                stats.addStat(125, nivel);
                stats.addStat(126, nivel / 2);
                stats.addStat(118, nivel / 2);
                break;
            }
            case 34: {
                stats.addStat(174, nivel * 5);
                stats.addStat(125, nivel / 2);
                stats.addStat(119, (int)((double)nivel / 1.65));
                stats.addStat(182, nivel / 100);
                break;
            }
            case 44: {
                stats.addStat(125, nivel);
                stats.addStat(124, nivel / 4);
                stats.addStat(123, (int)((double)nivel / 1.65));
                break;
            }
            case 42: {
                stats.addStat(125, nivel);
                stats.addStat(124, nivel / 4);
                stats.addStat(119, (int)((double)nivel / 1.65));
                break;
            }
            case 51: {
                stats.addStat(125, nivel);
                stats.addStat(123, nivel / 2);
                stats.addStat(119, nivel / 2);
                break;
            }
            case 71: {
                stats.addStat(125, (int)((double)nivel * 1.5));
                stats.addStat(118, (int)((double)nivel / 1.65));
                break;
            }
            case 70: {
                stats.addStat(125, (int)((double)nivel * 1.5));
                stats.addStat(126, (int)((double)nivel / 1.65));
                break;
            }
            case 41: {
                stats.addStat(174, nivel * 5);
                stats.addStat(125, nivel / 2);
                stats.addStat(118, (int)((double)nivel / 1.65));
                stats.addStat(182, nivel / 100);
                break;
            }
            case 40: {
                stats.addStat(174, nivel * 5);
                stats.addStat(125, nivel / 2);
                stats.addStat(126, (int)((double)nivel / 1.65));
                stats.addStat(182, nivel / 100);
                break;
            }
            case 49: {
                stats.addStat(125, nivel);
                stats.addStat(124, nivel / 4);
                stats.addStat(118, (int)((double)nivel / 1.65));
                break;
            }
            case 16: {
                stats.addStat(125, nivel / 2);
                stats.addStat(138, nivel / 2);
                break;
            }
            case 15: {
                stats.addStat(125, nivel / 2);
                stats.addStat(176, (int)((double)nivel / 1.25));
                break;
            }
            case 11: {
                stats.addStat(125, nivel * 2);
                stats.addStat(138, (int)((double)nivel / 2.5));
                break;
            }
            case 69: {
                stats.addStat(125, nivel * 2);
                stats.addStat(176, (int)((double)nivel / 2.5));
                break;
            }
            case 39: {
                stats.addStat(174, nivel * 5);
                stats.addStat(125, nivel / 2);
                stats.addStat(176, (int)((double)nivel / 2.5));
                stats.addStat(182, nivel / 100);
                break;
            }
            case 37: {
                stats.addStat(174, nivel * 5);
                stats.addStat(125, nivel / 2);
                stats.addStat(138, (int)((double)nivel / 2.5));
                stats.addStat(182, nivel / 100);
                break;
            }
            case 45: {
                stats.addStat(125, nivel);
                stats.addStat(138, (int)((double)nivel / 2.5));
                stats.addStat(124, nivel / 4);
                break;
            }
            case 47: {
                stats.addStat(125, nivel);
                stats.addStat(176, (int)((double)nivel / 2.5));
                stats.addStat(124, nivel / 4);
                break;
            }
            case 61: {
                stats.addStat(125, nivel);
                stats.addStat(123, (int)((double)nivel / 2.5));
                stats.addStat(138, (int)((double)nivel / 2.5));
                break;
            }
            case 63: {
                stats.addStat(125, nivel);
                stats.addStat(123, (int)((double)nivel / 1.65));
                stats.addStat(176, (int)((double)nivel / 2.5));
                break;
            }
            case 9: {
                stats.addStat(125, nivel);
                stats.addStat(119, (int)((double)nivel / 2.5));
                stats.addStat(138, (int)((double)nivel / 2.5));
                break;
            }
            case 52: {
                stats.addStat(125, nivel);
                stats.addStat(119, (int)((double)nivel / 1.65));
                stats.addStat(176, (int)((double)nivel / 2.5));
                break;
            }
            case 67: {
                stats.addStat(125, nivel);
                stats.addStat(126, (int)((double)nivel / 1.65));
                stats.addStat(138, (int)((double)nivel / 2.5));
                break;
            }
            case 68: {
                stats.addStat(125, nivel);
                stats.addStat(118, (int)((double)nivel / 1.65));
                stats.addStat(138, (int)((double)nivel / 2.5));
                break;
            }
            case 73: {
                stats.addStat(125, nivel);
                stats.addStat(118, (int)((double)nivel / 1.65));
                stats.addStat(176, (int)((double)nivel / 2.5));
                break;
            }
            case 72: {
                stats.addStat(125, nivel);
                stats.addStat(126, (int)((double)nivel / 1.65));
                stats.addStat(176, (int)((double)nivel / 2.5));
                break;
            }
            case 66: {
                stats.addStat(125, nivel);
                stats.addStat(138, (int)((double)nivel / 2.5));
                stats.addStat(176, (int)((double)nivel / 2.5));
                break;
            }
            case 21: {
                stats.addStat(125, nivel * 2);
                stats.addStat(128, nivel / 100);
                break;
            }
            case 23: {
                stats.addStat(125, nivel * 2);
                stats.addStat(117, nivel / 50);
                break;
            }
            case 57: {
                stats.addStat(125, nivel * 3);
                stats.addStat(128, nivel / 100);
                break;
            }
            case 84: {
                stats.addStat(125, nivel * 3);
                stats.addStat(117, nivel / 100);
                break;
            }
            case 35: {
                stats.addStat(125, nivel);
                stats.addStat(128, nivel / 100);
                stats.addStat(182, nivel / 100);
                stats.addStat(174, nivel * 5);
                break;
            }
            case 77: {
                stats.addStat(125, nivel * 2);
                stats.addStat(174, nivel * 5);
                stats.addStat(117, nivel / 100);
                stats.addStat(182, nivel / 100);
                break;
            }
            case 43: {
                stats.addStat(125, nivel);
                stats.addStat(124, nivel / 4);
                stats.addStat(128, nivel / 100);
                break;
            }
            case 78: {
                stats.addStat(125, nivel * 2);
                stats.addStat(124, nivel / 4);
                stats.addStat(117, nivel / 100);
                break;
            }
            case 55: {
                stats.addStat(125, nivel);
                stats.addStat(123, (int)((double)nivel / 3.33));
                stats.addStat(128, nivel / 100);
                break;
            }
            case 82: {
                stats.addStat(125, nivel * 2);
                stats.addStat(123, (int)((double)nivel / 1.65));
                stats.addStat(117, nivel / 100);
                break;
            }
            case 50: {
                stats.addStat(125, nivel);
                stats.addStat(119, (int)((double)nivel / 3.33));
                stats.addStat(128, nivel / 100);
                break;
            }
            case 79: {
                stats.addStat(125, nivel * 2);
                stats.addStat(119, (int)((double)nivel / 1.65));
                stats.addStat(117, nivel / 100);
                break;
            }
            case 60: {
                stats.addStat(125, nivel);
                stats.addStat(118, (int)((double)nivel / 3.33));
                stats.addStat(128, nivel / 100);
                break;
            }
            case 87: {
                stats.addStat(125, nivel * 2);
                stats.addStat(118, (int)((double)nivel / 1.65));
                stats.addStat(117, nivel / 100);
                break;
            }
            case 59: {
                stats.addStat(125, nivel);
                stats.addStat(126, (int)((double)nivel / 3.33));
                stats.addStat(128, nivel / 100);
                break;
            }
            case 86: {
                stats.addStat(125, nivel * 2);
                stats.addStat(126, (int)((double)nivel / 1.65));
                stats.addStat(117, nivel / 100);
                break;
            }
            case 56: {
                stats.addStat(125, nivel);
                stats.addStat(138, (int)((double)nivel / 3.33));
                stats.addStat(128, nivel / 100);
                break;
            }
            case 83: {
                stats.addStat(125, nivel * 2);
                stats.addStat(138, (int)((double)nivel / 1.65));
                stats.addStat(117, nivel / 100);
                break;
            }
            case 58: {
                stats.addStat(125, nivel);
                stats.addStat(176, (int)((double)nivel / 3.33));
                stats.addStat(128, nivel / 100);
                break;
            }
            case 85: {
                stats.addStat(125, nivel * 2);
                stats.addStat(176, (int)((double)nivel / 1.65));
                stats.addStat(117, nivel / 100);
                break;
            }
            case 80: {
                stats.addStat(125, nivel * 2);
                stats.addStat(128, nivel / 100);
                stats.addStat(117, nivel / 100);
                break;
            }
            case 88: {
                stats.addStat(138, nivel / 2);
                stats.addStat(212, nivel / 20);
                stats.addStat(211, nivel / 20);
                stats.addStat(210, nivel / 20);
                stats.addStat(213, nivel / 20);
                stats.addStat(214, nivel / 20);
                break;
            }
            case 89: {
                stats.addStat(176, nivel * 4 / 5);
                break;
            }
            case 90: {
                stats.addStat(138, nivel / 2);
            }
        }
        return stats;
    }

    public static int getScrollporMontura(int color) {
        switch (color) {
            case 33: {
                return 802;
            }
            case 38: {
                return 806;
            }
            case 46: {
                return 802;
            }
            case 3: {
                return 798;
            }
            case 17: {
                return 809;
            }
            case 12: 
            case 62: {
                return 806;
            }
            case 36: {
                return 809;
            }
            case 34: {
                return 798;
            }
            case 44: {
                return 802;
            }
            case 42: {
                return 802;
            }
            case 51: {
                return 809;
            }
            case 19: {
                return 683;
            }
            case 22: {
                return 686;
            }
            case 71: {
                return 683;
            }
            case 70: {
                return 815;
            }
            case 41: {
                return 795;
            }
            case 40: {
                return 807;
            }
            case 48: 
            case 49: {
                return 803;
            }
            case 64: 
            case 65: {
                return 811;
            }
            case 53: 
            case 54: {
                return 799;
            }
            case 76: {
                return 815;
            }
            case 15: {
                return 800;
            }
            case 16: {
                return 812;
            }
            case 11: 
            case 69: {
                return 808;
            }
            case 37: {
                return 812;
            }
            case 39: {
                return 808;
            }
            case 45: 
            case 47: {
                return 804;
            }
            case 61: 
            case 63: {
                return 812;
            }
            case 9: 
            case 52: {
                return 800;
            }
            case 68: 
            case 73: {
                return 796;
            }
            case 67: 
            case 72: {
                return 816;
            }
            case 66: {
                return 800;
            }
            case 21: {
                return 797;
            }
            case 23: {
                return 817;
            }
            case 35: 
            case 57: {
                return 810;
            }
            case 43: {
                return 805;
            }
            case 50: {
                return 797;
            }
            case 55: {
                return 814;
            }
            case 56: {
                return 814;
            }
            case 58: {
                return 801;
            }
            case 59: {
                return 817;
            }
            case 60: {
                return 797;
            }
            case 77: {
                return 810;
            }
            case 78: {
                return 805;
            }
            case 79: {
                return 801;
            }
            case 80: {
                return 817;
            }
            case 82: {
                return 814;
            }
            case 83: {
                return 814;
            }
            case 84: {
                return 810;
            }
            case 85: {
                return 801;
            }
            case 86: {
                return 817;
            }
        }
        return -1;
    }

    public static Objeto.ObjetoModelo getPergaPorColorDragopavo(int color) {
        switch (color) {
            case 2: {
                return Mundo.getObjModelo(7807);
            }
            case 3: {
                return Mundo.getObjModelo(7808);
            }
            case 4: {
                return Mundo.getObjModelo(7809);
            }
            case 9: {
                return Mundo.getObjModelo(7810);
            }
            case 10: {
                return Mundo.getObjModelo(7811);
            }
            case 11: {
                return Mundo.getObjModelo(7812);
            }
            case 12: {
                return Mundo.getObjModelo(7813);
            }
            case 15: {
                return Mundo.getObjModelo(7814);
            }
            case 16: {
                return Mundo.getObjModelo(7815);
            }
            case 17: {
                return Mundo.getObjModelo(7816);
            }
            case 18: {
                return Mundo.getObjModelo(7817);
            }
            case 19: {
                return Mundo.getObjModelo(7818);
            }
            case 20: {
                return Mundo.getObjModelo(7819);
            }
            case 21: {
                return Mundo.getObjModelo(7820);
            }
            case 22: {
                return Mundo.getObjModelo(7821);
            }
            case 23: {
                return Mundo.getObjModelo(7822);
            }
            case 33: {
                return Mundo.getObjModelo(7823);
            }
            case 34: {
                return Mundo.getObjModelo(7824);
            }
            case 35: {
                return Mundo.getObjModelo(7825);
            }
            case 36: {
                return Mundo.getObjModelo(7826);
            }
            case 37: {
                return Mundo.getObjModelo(7827);
            }
            case 38: {
                return Mundo.getObjModelo(7828);
            }
            case 39: {
                return Mundo.getObjModelo(7829);
            }
            case 40: {
                return Mundo.getObjModelo(7830);
            }
            case 41: {
                return Mundo.getObjModelo(7831);
            }
            case 42: {
                return Mundo.getObjModelo(7832);
            }
            case 43: {
                return Mundo.getObjModelo(7833);
            }
            case 44: {
                return Mundo.getObjModelo(7834);
            }
            case 45: {
                return Mundo.getObjModelo(7835);
            }
            case 46: {
                return Mundo.getObjModelo(7836);
            }
            case 47: {
                return Mundo.getObjModelo(7837);
            }
            case 48: {
                return Mundo.getObjModelo(7838);
            }
            case 49: {
                return Mundo.getObjModelo(7839);
            }
            case 50: {
                return Mundo.getObjModelo(7840);
            }
            case 51: {
                return Mundo.getObjModelo(7841);
            }
            case 52: {
                return Mundo.getObjModelo(7842);
            }
            case 53: {
                return Mundo.getObjModelo(7843);
            }
            case 54: {
                return Mundo.getObjModelo(7844);
            }
            case 55: {
                return Mundo.getObjModelo(7845);
            }
            case 56: {
                return Mundo.getObjModelo(7846);
            }
            case 57: {
                return Mundo.getObjModelo(7847);
            }
            case 58: {
                return Mundo.getObjModelo(7848);
            }
            case 59: {
                return Mundo.getObjModelo(7849);
            }
            case 60: {
                return Mundo.getObjModelo(7850);
            }
            case 61: {
                return Mundo.getObjModelo(7851);
            }
            case 62: {
                return Mundo.getObjModelo(7852);
            }
            case 63: {
                return Mundo.getObjModelo(7853);
            }
            case 64: {
                return Mundo.getObjModelo(7854);
            }
            case 65: {
                return Mundo.getObjModelo(7855);
            }
            case 66: {
                return Mundo.getObjModelo(7856);
            }
            case 67: {
                return Mundo.getObjModelo(7857);
            }
            case 68: {
                return Mundo.getObjModelo(7858);
            }
            case 69: {
                return Mundo.getObjModelo(7859);
            }
            case 70: {
                return Mundo.getObjModelo(7860);
            }
            case 71: {
                return Mundo.getObjModelo(7861);
            }
            case 72: {
                return Mundo.getObjModelo(7862);
            }
            case 73: {
                return Mundo.getObjModelo(7863);
            }
            case 74: {
                return Mundo.getObjModelo(7864);
            }
            case 75: {
                return Mundo.getObjModelo(11143);
            }
            case 76: {
                return Mundo.getObjModelo(7866);
            }
            case 77: {
                return Mundo.getObjModelo(7867);
            }
            case 78: {
                return Mundo.getObjModelo(7868);
            }
            case 79: {
                return Mundo.getObjModelo(7869);
            }
            case 80: {
                return Mundo.getObjModelo(7870);
            }
            case 82: {
                return Mundo.getObjModelo(7871);
            }
            case 83: {
                return Mundo.getObjModelo(7872);
            }
            case 84: {
                return Mundo.getObjModelo(7873);
            }
            case 85: {
                return Mundo.getObjModelo(7874);
            }
            case 86: {
                return Mundo.getObjModelo(7875);
            }
            case 87: {
                return Mundo.getObjModelo(7876);
            }
            case 88: {
                return Mundo.getObjModelo(9582);
            }
            case 89: {
                return Mundo.getObjModelo(81000);
            }
            case 90: {
                return Mundo.getObjModelo(80000);
            }
        }
        return null;
    }

    public static int getColorDragoPavoPorPerga(int tID) {
        for (int a = 1; a < 100; ++a) {
            if (Constantes.getPergaPorColorDragopavo(a) == null || Constantes.getPergaPorColorDragopavo(a).getID() != tID) continue;
            return a;
        }
        return -1;
    }

    public static void aplicarAccionOI(Personaje perso, int mapaID, int celdaID) {
        switch (mapaID) {
            case 2196: {
                if (perso.estaOcupado()) {
                    return;
                }
                if (perso.getGremio() != null || perso.getMiembroGremio() != null) {
                    SocketManager.ENVIAR_gC_CREAR_PANEL_GREMIO(perso, "Ea");
                    return;
                }
                if (!perso.tieneObjModeloNoEquip(1575, 1)) {
                    SocketManager.ENVIAR_Im_INFORMACION(perso, "14");
                    return;
                }
                SocketManager.ENVIAR_gn_CREAR_GREMIO(perso);
                break;
            }
            default: {
                System.out.println("Accion de OI no generado por el mapa: " + mapaID + " celda: " + celdaID);
            }
        }
    }

    public static short getCeldaIDCercanaNoUsada(Personaje perso) {
        Mapa mapa = perso.getMapa();
        short celda = perso.getCelda().getID();
        byte ancho = perso.getMapa().getAncho();
        short celdaFrente = (short)(celda + ancho);
        short celdaAtras = (short)(celda - ancho);
        short celdaDerecha = (short)(celda + (ancho - 1));
        short celdaIzquierda = (short)(celda - (ancho - 1));
        if (mapa.getCelda(celdaFrente).getObjetoTirado() == null && mapa.getCeldas().get(celdaFrente).getPersos().isEmpty() && mapa.getCeldas().get(celdaFrente).esCaminable(false)) {
            return celdaFrente;
        }
        if (mapa.getCelda(celdaAtras).getObjetoTirado() == null && mapa.getCeldas().get(celdaAtras).getPersos().isEmpty() && mapa.getCeldas().get(celdaAtras).esCaminable(false)) {
            return celdaAtras;
        }
        if (mapa.getCelda(celdaDerecha).getObjetoTirado() == null && mapa.getCeldas().get(celdaDerecha).getPersos().isEmpty() && mapa.getCeldas().get(celdaDerecha).esCaminable(false)) {
            return celdaDerecha;
        }
        if (mapa.getCelda(celdaIzquierda).getObjetoTirado() == null && mapa.getCeldas().get(celdaIzquierda).getPersos().isEmpty() && mapa.getCeldas().get(celdaIzquierda).esCaminable(false)) {
            return celdaIzquierda;
        }
        return 0;
    }

    public static int getIdTituloOficio(int oficio) {
        switch (oficio) {
            case 43: {
                return 40;
            }
            case 44: {
                return 41;
            }
            case 45: {
                return 42;
            }
            case 46: {
                return 43;
            }
            case 47: {
                return 44;
            }
            case 48: {
                return 45;
            }
            case 49: {
                return 46;
            }
            case 50: {
                return 47;
            }
            case 62: {
                return 51;
            }
            case 63: {
                return 52;
            }
            case 64: {
                return 53;
            }
        }
        return 0;
    }

    public static int getValorStatRuna(int statID) {
        int multi = 1;
        if (statID == 118 || statID == 126 || statID == 125 || statID == 119 || statID == 123 || statID == 158 || statID == 174) {
            multi = 1;
        } else if (statID == 138 || statID == 666 || statID == 226 || statID == 220) {
            multi = 2;
        } else if (statID == 124 || statID == 176) {
            multi = 3;
        } else if (statID == 240 || statID == 241 || statID == 242 || statID == 243 || statID == 244) {
            multi = 4;
        } else if (statID == 210 || statID == 211 || statID == 212 || statID == 213 || statID == 214) {
            multi = 5;
        } else if (statID == 225) {
            multi = 6;
        } else if (statID == 178 || statID == 112) {
            multi = 7;
        } else if (statID == 115 || statID == 182) {
            multi = 8;
        } else if (statID == 117) {
            multi = 9;
        } else if (statID == 128) {
            multi = 10;
        } else if (statID == 111) {
            multi = 10;
        }
        return multi;
    }

    public static int statDeRunas(int stat, int cant) {
        switch (stat) {
            case 111: {
                return 1557;
            }
            case 112: {
                return 7435;
            }
            case 115: {
                return 7433;
            }
            case 117: {
                return 7438;
            }
            case 118: {
                if (cant > 70) {
                    return 1551;
                }
                if (cant <= 70 && cant > 20) {
                    return 1545;
                }
                return 1519;
            }
            case 119: {
                if (cant > 70) {
                    return 1555;
                }
                if (cant <= 70 && cant > 20) {
                    return 1549;
                }
                return 1524;
            }
            case 123: {
                if (cant > 70) {
                    return 1556;
                }
                if (cant <= 70 && cant > 20) {
                    return 1550;
                }
                return 1525;
            }
            case 124: {
                if (cant > 30) {
                    return 1552;
                }
                if (cant <= 30 && cant > 10) {
                    return 1546;
                }
                return 1521;
            }
            case 125: {
                if (cant > 230) {
                    return 1554;
                }
                if (cant <= 230 && cant > 60) {
                    return 1548;
                }
                return 1523;
            }
            case 126: {
                if (cant > 70) {
                    return 1553;
                }
                if (cant <= 70 && cant > 20) {
                    return 1547;
                }
                return 1522;
            }
            case 128: {
                return 1558;
            }
            case 138: {
                return 7436;
            }
            case 158: {
                if (cant > 300) {
                    return 7445;
                }
                if (cant <= 300 && cant > 100) {
                    return 7444;
                }
                return 7443;
            }
            case 174: {
                if (cant > 300) {
                    return 7450;
                }
                if (cant <= 300 && cant > 100) {
                    return 7449;
                }
                return 7448;
            }
            case 176: {
                if (cant > 5) {
                    return 10662;
                }
                return 7451;
            }
            case 178: {
                return 7434;
            }
            case 182: {
                return 7442;
            }
            case 220: {
                return 7437;
            }
            case 225: {
                return 7446;
            }
            case 226: {
                return 7447;
            }
            case 240: {
                return 7452;
            }
            case 241: {
                return 7453;
            }
            case 242: {
                return 7454;
            }
            case 243: {
                return 7455;
            }
            case 244: {
                return 7456;
            }
            case 210: {
                return 7457;
            }
            case 211: {
                return 7458;
            }
            case 212: {
                return 7560;
            }
            case 213: {
                return 7459;
            }
            case 214: {
                return 7460;
            }
        }
        return 0;
    }

    public static int efectoElemento(int efectoID) {
        switch (efectoID) {
            case 85: 
            case 91: 
            case 96: {
                return 1;
            }
            case 86: 
            case 92: 
            case 97: {
                return 2;
            }
            case 87: 
            case 93: 
            case 98: {
                return 3;
            }
            case 88: 
            case 94: 
            case 99: {
                return 4;
            }
            case 89: 
            case 95: 
            case 100: {
                return 5;
            }
        }
        return -1;
    }

    public static boolean alimentoMontura(int tipo) {
        for (Integer t : LesGuardians.ALIMENTOS_MONTARIA) {
            if (tipo != t) continue;
            return true;
        }
        return false;
    }

    public static int fantasmaMascota(int mascota) {
        switch (mascota) {
            case 8153: {
                return 8171;
            }
            case 7704: {
                return 7722;
            }
            case 8561: {
                return 8565;
            }
            case 7706: {
                return 7724;
            }
            case 8151: {
                return 8172;
            }
            case 9594: {
                return 9595;
            }
            case 10657: {
                return 10658;
            }
            case 2075: {
                return 7540;
            }
            case 2076: {
                return 7541;
            }
            case 2074: {
                return 7539;
            }
            case 2077: {
                return 7542;
            }
            case 8000: {
                return 8017;
            }
            case 7707: {
                return 7725;
            }
            case 9623: {
                return 9671;
            }
            case 7520: {
                return 7550;
            }
            case 7703: {
                return 7721;
            }
            case 9624: {
                return 9666;
            }
            case 7519: {
                return 7549;
            }
            case 8154: {
                return 8173;
            }
            case 9785: {
                return 9786;
            }
            case 8693: {
                return 8706;
            }
            case 7518: {
                return 7548;
            }
            case 10544: {
                return 10597;
            }
            case 7911: {
                return 8016;
            }
            case 6604: {
                return 7543;
            }
            case 7705: {
                return 7723;
            }
            case 1728: {
                return 7537;
            }
            case 9617: {
                return 9661;
            }
            case 7524: {
                return 8885;
            }
            case 7891: {
                return 7893;
            }
            case 7522: {
                return 7551;
            }
            case 9620: {
                return 9665;
            }
            case 10107: {
                return 10109;
            }
            case 6716: {
                return 7544;
            }
            case 8155: {
                return 8174;
            }
            case 9619: {
                return 9664;
            }
            case 6978: {
                return 7545;
            }
            case 7414: {
                return 7546;
            }
            case 7415: {
                return 7547;
            }
            case 7709: {
                return 7727;
            }
            case 7708: {
                return 7726;
            }
            case 7711: {
                return 7729;
            }
            case 7710: {
                return 7728;
            }
            case 7712: {
                return 7730;
            }
            case 7713: {
                return 7731;
            }
            case 8677: {
                return 8679;
            }
            case 7714: {
                return 8020;
            }
            case 10106: {
                return 10108;
            }
            case 1748: {
                return 7538;
            }
            case 8211: {
                return 8524;
            }
            case 1711: {
                return 7536;
            }
            case 7892: {
                return 7894;
            }
        }
        return 0;
    }

    public static int resucitarMascota(int fantasma) {
        switch (fantasma) {
            case 8171: {
                return 8153;
            }
            case 7722: {
                return 7704;
            }
            case 8565: {
                return 8561;
            }
            case 7724: {
                return 7706;
            }
            case 8172: {
                return 8151;
            }
            case 9595: {
                return 9594;
            }
            case 10658: {
                return 10657;
            }
            case 7540: {
                return 2075;
            }
            case 7541: {
                return 2076;
            }
            case 7539: {
                return 2074;
            }
            case 7542: {
                return 2077;
            }
            case 8017: {
                return 8000;
            }
            case 7725: {
                return 7707;
            }
            case 9671: {
                return 9623;
            }
            case 7550: {
                return 7520;
            }
            case 7721: {
                return 7703;
            }
            case 9666: {
                return 9624;
            }
            case 7549: {
                return 7519;
            }
            case 8173: {
                return 8154;
            }
            case 9786: {
                return 9785;
            }
            case 8706: {
                return 8693;
            }
            case 7548: {
                return 7518;
            }
            case 10597: {
                return 10544;
            }
            case 8016: {
                return 7911;
            }
            case 7543: {
                return 6604;
            }
            case 7723: {
                return 7705;
            }
            case 7537: {
                return 1728;
            }
            case 9661: {
                return 9617;
            }
            case 8885: {
                return 7524;
            }
            case 7893: {
                return 7891;
            }
            case 7551: {
                return 7522;
            }
            case 9665: {
                return 9620;
            }
            case 10109: {
                return 10107;
            }
            case 7544: {
                return 6716;
            }
            case 8174: {
                return 8155;
            }
            case 9664: {
                return 9619;
            }
            case 7545: {
                return 6978;
            }
            case 7546: {
                return 7414;
            }
            case 7547: {
                return 7415;
            }
            case 7727: {
                return 7709;
            }
            case 7726: {
                return 7708;
            }
            case 7729: {
                return 7711;
            }
            case 7728: {
                return 7710;
            }
            case 7730: {
                return 7712;
            }
            case 7731: {
                return 7713;
            }
            case 8679: {
                return 8677;
            }
            case 8020: {
                return 7714;
            }
            case 10108: {
                return 10106;
            }
            case 7538: {
                return 1748;
            }
            case 8524: {
                return 8211;
            }
            case 7536: {
                return 1711;
            }
            case 7894: {
                return 7892;
            }
        }
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static boolean esRetoPosible2(int nro, int nuevo) {
        try {
            if (nro == nuevo) {
                return false;
            }
            switch (nro) {
                case 1: 
                case 2: 
                case 8: {
                    if (nuevo != 1 && nuevo != 2 && nuevo != 8) break;
                    return false;
                }
                case 5: {
                    if (nuevo != 5 && nuevo != 6 && nuevo != 9 && nuevo != 24) break;
                    return false;
                }
                case 9: {
                    if (nuevo != 5 && nuevo != 9 && nuevo != 11 && nuevo != 19 && nuevo != 24) break;
                    return false;
                }
                case 6: {
                    if (nuevo != 5 && nuevo != 6) break;
                    return false;
                }
                case 11: {
                    if (nuevo != 9 && nuevo != 11) break;
                    return false;
                }
                case 19: {
                    if (nuevo != 9 && nuevo != 19) break;
                    return false;
                }
                case 24: {
                    if (nuevo != 5 && nuevo != 9 && nuevo != 24) break;
                    return false;
                }
                case 28: {
                    if (nuevo != 29) break;
                    return false;
                }
                case 29: {
                    if (nuevo != 28) break;
                    return false;
                }
                case 36: 
                case 40: {
                    if (nuevo != 36 && nuevo != 40) break;
                    return false;
                }
                case 37: 
                case 39: {
                    if (nuevo != 37 && nuevo != 39) break;
                    return false;
                }
                case 3: 
                case 4: 
                case 10: 
                case 25: 
                case 31: 
                case 32: 
                case 34: 
                case 35: 
                case 38: 
                case 45: {
                    if (nuevo != 3 && nuevo != 4 && nuevo != 10 && nuevo != 25 && nuevo != 31 && nuevo != 32 && nuevo != 34 && nuevo != 35 && nuevo != 38 && nuevo != 45) break;
                    return false;
                }
            }
            return true;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean esRetoPosible1(int nuevo, Combate pelea) {
		try {
			switch (nuevo) {
				case 7 :
					for (Luchador luchador : pelea.luchadoresDeEquipo(1)) {
						if (luchador.getPersonaje().tieneHechizoID(367)) {
							return true;
						}
					}
					return false;
				case 12 :
					for (Luchador luchador : pelea.luchadoresDeEquipo(1)) {
						if (luchador.getPersonaje().tieneHechizoID(373)) {
							return true;
						}
					}
					return false;
				case 14 :// casino real, lanzar el hechizo ruleta cada vez q se pueda
					for (Luchador luchador : pelea.luchadoresDeEquipo(1)) {
						if (luchador.getPersonaje().tieneHechizoID(101)) {
							return true;
						}
					}
					return false;
				case 15 :// aracnofilo, invocar una araña cada vez q se pueda
					for (Luchador luchador : pelea.luchadoresDeEquipo(1)) {
						if (luchador.getPersonaje().tieneHechizoID(370)) {
							return true;
						}
					}
					return false;
				case 29 :
				case 28 :
					int masc = 0;
					int fem = 0;
					for (Luchador luchador : pelea.luchadoresDeEquipo(1)) {
						if (luchador.getPersonaje().getSexo() == 1) {
							fem++;
						} else {
							masc++;
						}
					}
					if (fem > 0 && masc > 0) {
						return true;
					}
					return false;
				case 10 :
				case 25 :
				case 42 :
					if (pelea.luchadoresDeEquipo(2).size() >= 2) {
						return true;
					}
					return false;
				case 44 :
				case 46 :
					if (pelea.luchadoresDeEquipo(2).size() >= pelea.luchadoresDeEquipo(1).size() && pelea.luchadoresDeEquipo(1).size() > 1) {
						return true;
					}
					return false;
				case 37 :
				case 30 :
				case 33 :
				case 47 :
					if (pelea.luchadoresDeEquipo(1).size() >= 2) {
						return true;
					}
					return false;
			}
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}

    public static int getDoplonDopeul(int idMob) {
        switch (idMob) {
            case 168: {
                return 10302;
            }
            case 165: {
                return 10303;
            }
            case 166: {
                return 10304;
            }
            case 162: {
                return 10305;
            }
            case 160: {
                return 10306;
            }
            case 167: {
                return 10307;
            }
            case 161: {
                return 10308;
            }
            case 2691: {
                return 10309;
            }
            case 455: {
                return 10310;
            }
            case 169: {
                return 10311;
            }
            case 163: {
                return 10312;
            }
            case 164: {
                return 10313;
            }
        }
        return -1;
    }

    public static int getCertificadoDopeul(int idMob) {
        switch (idMob) {
            case 168: {
                return 10289;
            }
            case 165: {
                return 10290;
            }
            case 166: {
                return 10291;
            }
            case 162: {
                return 10292;
            }
            case 160: {
                return 10293;
            }
            case 167: {
                return 10294;
            }
            case 161: {
                return 10295;
            }
            case 2691: {
                return 10296;
            }
            case 455: {
                return 10297;
            }
            case 169: {
                return 10298;
            }
            case 163: {
                return 10299;
            }
            case 164: {
                return 10300;
            }
        }
        return -1;
    }
}

