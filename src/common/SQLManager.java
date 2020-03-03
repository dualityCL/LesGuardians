package common;

import com.mysql.jdbc.PreparedStatement;
import common.Constantes;
import common.LesGuardians;
import common.World;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import objects.Acao;
import objects.Animacao;
import objects.Casa;
import objects.Cofre;
import objects.Coletor;
import objects.Conta;
import objects.Desafios;
import objects.Dragossauros;
import objects.Guild;
import objects.Incarnacao;
import objects.MOB_tmpl;
import objects.Maps;
import objects.Mercador;
import objects.NPC_tmpl;
import objects.Objeto;
import objects.Personagens;
import objects.Pets;
import objects.Prisma;
import objects.Profissao;
import objects.RankPVP;
import objects.Set_Vivo;
import objects.Spell;
import objects.Spell.StatsHechizos;
import objects.Stockage;
import objects.Tutorial;

import org.fusesource.jansi.AnsiConsole;

public class SQLManager {
    private static Connection BDD_OTHER;
    private static Connection BDD_STATIC;
    private static Timer timerComienzo;
    private static boolean necesitaHacerTransaccion;

    private static void cerrarResultado(ResultSet resultado) {
        try {
            resultado.getStatement().close();
            resultado.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cerrarDeclaracion(PreparedStatement declaracion) {
        try {
            declaracion.clearParameters();
            declaracion.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ResultSet ejecutarConsultaSQL(String consultaSQL, String bdNombre) throws SQLException {
        if (!LesGuardians._estaIniciado) {
            return null;
        }
        Connection conexión;
        if (bdNombre.equals(LesGuardians.BDD_OTHER)) {   
        	conexión = BDD_OTHER;       
        } else {
        	conexión = BDD_STATIC;
        }
        PreparedStatement declaracion = (PreparedStatement)conexión.prepareStatement(consultaSQL);
        ResultSet resultado = declaracion.executeQuery();
        declaracion.setQueryTimeout(300);
        return resultado;
    }

    public static synchronized PreparedStatement nuevaTransaccion(String consultaSQL, Connection coneccionSQL) throws SQLException {
        PreparedStatement declaracion = (PreparedStatement)coneccionSQL.prepareStatement(consultaSQL);
        necesitaHacerTransaccion = true;
        return declaracion;
    }

    public static void lineaSQL(String sql) {
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(sql, BDD_OTHER);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + sql);
            e.printStackTrace();
        }
    }

    public static int getSigIDObjeto() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT MAX(guid) AS max FROM items;", LesGuardians.BDD_OTHER);
            int id = 1;
            boolean encontrado = resultado.first();
            if (encontrado) {
                id = resultado.getInt("max");
            }
            SQLManager.cerrarResultado(resultado);
            return id;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    public static void TIMER(boolean iniciar) {
        if (iniciar) {
            timerComienzo = new Timer();
            timerComienzo.schedule(new TimerTask(){

                @Override
                public void run() {
                    if (!necesitaHacerTransaccion) {
                        return;
                    }
                    SQLManager.comenzarTransacciones();
                    necesitaHacerTransaccion = false;
                }
            }, LesGuardians.BD_COMENZAR_TRANSACCION, (long)LesGuardians.BD_COMENZAR_TRANSACCION);
        } else {
            timerComienzo.cancel();
        }
    }

    public static synchronized void comenzarTransacciones() {
        try {
            if (BDD_OTHER.isClosed() || BDD_STATIC.isClosed()) {
                SQLManager.cerrarConexion();
                SQLManager.iniciarConexion();
            }
            BDD_STATIC.commit();
            BDD_OTHER.commit();
        }
        catch (SQLException e) {
            System.out.println("SQL ERROR:" + e.getMessage());
            e.printStackTrace();
            SQLManager.comenzarTransacciones();
        }
    }

    public static synchronized void cerrarConexion() {
        try {
            SQLManager.comenzarTransacciones();
            BDD_OTHER.close();
            BDD_STATIC.close();
        }
        catch (Exception e) {
            System.out.println("Erreur a la fermeture des connexions SQL:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static final boolean iniciarConexion() {
        block3: {
            try {
                BDD_OTHER = DriverManager.getConnection("jdbc:mysql://" + LesGuardians.BDD_HOST + "/" + LesGuardians.BDD_OTHER, LesGuardians.BDD_USER, LesGuardians.BDD_PASS);
                BDD_OTHER.setAutoCommit(false);
                BDD_STATIC = DriverManager.getConnection("jdbc:mysql://" + LesGuardians.BDD_HOST + "/" + LesGuardians.BDD_STATIC, LesGuardians.BDD_USER, LesGuardians.BDD_PASS);
                BDD_STATIC.setAutoCommit(false);
                if (BDD_STATIC.isValid(1000) && BDD_OTHER.isValid(1000)) break block3;
                System.out.println("SQLError : Conexao com a DB invalida.");
                return false;
            }
            catch (SQLException e) {
                System.out.println("ERREUR SQL: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        necesitaHacerTransaccion = false;
        SQLManager.TIMER(true);
        return true;
    }

    public static int getPuntosCuenta(int cuentaID) {
        int puntos = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT `points` FROM `accounts` WHERE `guid` = " + cuentaID + ";", LesGuardians.BDD_OTHER);
            boolean encontrado = resultado.first();
            if (encontrado) {
                puntos = resultado.getInt("points");
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return puntos;
    }

    public static void setPuntoCuenta(int puntos, int cuentaID) {
        String consultaSQL = "UPDATE `accounts` SET `points`=? WHERE `guid`= ?";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setLong(1, puntos);
            declaracion.setInt(2, cuentaID);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void cambiarContrase\u00f1a(String password, int cuentaID) {
        String consultaSQL = "UPDATE `accounts` SET `pass`=? WHERE `guid`= ?";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, password);
            declaracion.setInt(2, cuentaID);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void SALVAR_CUENTA(Conta cuenta) {
        try {
            String consultaSQL = "UPDATE accounts SET `kamas` = ?,`objets` = ?,`level` = ?,`etable` = ?,`banned` = ?,`friends` = ?,`enemy` = ?,`lastIP` = ?,`lastConnectionDate` = ? WHERE `guid` = ?;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setLong(1, cuenta.getKamasBanco());
            declaracion.setString(2, cuenta.stringBancoObjetosBD());
            declaracion.setInt(3, cuenta.getRango());
            declaracion.setString(4, cuenta.stringIDsEstablo());
            declaracion.setInt(5, cuenta.estaBaneado() ? 1 : 0);
            declaracion.setString(6, cuenta.analizarListaAmigosABD());
            declaracion.setString(7, cuenta.stringListaEnemigosABD());
            declaracion.setString(8, cuenta.getUltimoIP());
            declaracion.setString(9, cuenta.getUltimaConeccion());
            declaracion.setInt(10, cuenta.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void ELIMINAR_CUENTA(int guid) {
        String consultaSQL = "DELETE FROM accounts WHERE guid = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, guid);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void ACTUALIZAR_PRIMERA_VEZ(Conta cuenta) {
        try {
            String consultaSQL = "UPDATE accounts SET primeravez = 0 WHERE `guid` = ?;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, cuenta.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CARGAR_RECETAS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from crafts;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                ArrayList<World.Duo<Integer, Integer>> arrayDuos = new ArrayList<World.Duo<Integer, Integer>>();
                boolean continua = false;
                for (String str : resultado.getString("craft").split(";")) {
                    try {
                        String[] s = str.split("\\*");
                        int idModeloObj = Integer.parseInt(s[0]);
                        int cantidad = Integer.parseInt(s[1]);
                        arrayDuos.add(new World.Duo<Integer, Integer>(idModeloObj, cantidad));
                        continua = true;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        continua = false;
                    }
                }
                if (!continua) continue;
                World.addReceta(resultado.getInt("id"), arrayDuos);
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CARGAR_GREMIOS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from guilds;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.addGremio(new Guild(resultado.getInt("id"), resultado.getString("name"), resultado.getString("emblem"), resultado.getInt("lvl"), resultado.getLong("xp"), resultado.getInt("capital"), resultado.getInt("nbrmax"), resultado.getString("sorts"), resultado.getString("stats")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int CARGAR_MIEMBROS_GREMIO() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from guild_members;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                Guild gremio = World.getGremio(resultado.getInt("guild"));
                if (gremio == null) continue;
                gremio.addMiembro(resultado.getInt("guid"), resultado.getString("name"), resultado.getInt("level"), resultado.getInt("gfxid"), resultado.getInt("rank"), resultado.getByte("pxp"), resultado.getLong("xpdone"), resultado.getInt("rights"), resultado.getDate("lastConnection").toString().replaceAll("-", "~"));
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
            numero = 0;
        }
        return numero;
    }

    public static void CARGAR_MONTURAS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from mounts_data;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.addDragopavo(new Dragossauros(resultado.getInt("id"), resultado.getInt("color"), resultado.getInt("sexe"), resultado.getInt("amour"), resultado.getInt("endurance"), resultado.getInt("level"), resultado.getLong("xp"), resultado.getString("name"), resultado.getInt("fatigue"), resultado.getInt("energie"), resultado.getInt("reproductions"), resultado.getInt("maturite"), resultado.getInt("serenite"), resultado.getString("items"), resultado.getString("ancetres"), resultado.getString("ability"), resultado.getInt("size"), resultado.getShort("cell"), resultado.getShort("map"), resultado.getInt("owner"), resultado.getInt("orientation"), resultado.getInt("reproductions"), resultado.getInt("couple"), resultado.getString("vip")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CARGAR_DROPS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from drops;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                MOB_tmpl MM = World.getMobModelo(resultado.getInt("mob"));
                MM.addDrop(new World.Drop(resultado.getInt("item"), resultado.getInt("seuil"), resultado.getInt("taux"), resultado.getInt("max")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CARGAR_OBJETOS_SETS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from itemsets;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.addObjetoSet(new World.ObjetoSet(resultado.getInt("ID"), resultado.getString("items"), resultado.getString("bonus")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CARGAR_INTERACTIVOS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from interactive_objects_data;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.addObjInteractivo(new World.ObjInteractivoModelo(resultado.getInt("id"), resultado.getInt("respawn"), resultado.getInt("duration"), resultado.getInt("actionPersonnage"), resultado.getInt("walkable") == 1));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int CARGAR_CERCADOS() {
        int num = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from mountpark;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                Maps mapa = World.getMapa(resultado.getShort("mapid"));
                if (mapa == null) continue;
                Maps.Cercado cercado = new Maps.Cercado(resultado.getInt("owner"), mapa, resultado.getShort("cellid"), resultado.getInt("taille"), resultado.getInt("guild"), resultado.getInt("price"), resultado.getShort("cellmonture"), resultado.getString("sensibilisation"), resultado.getShort("cellporte"), resultado.getString("cellsitem"), resultado.getInt("objets"), resultado.getString("objetoscolocados"));
                World.addCercado(cercado);
                ++num;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
            num = 0;
        }
        return num;
    }

    public static void CARGAR_OFICIOS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from jobs_data;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.addOficio(new Profissao(resultado.getInt("id"), resultado.getString("tools"), resultado.getString("crafts")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CARGAR_AREA() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from area_data;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.Area A = new World.Area(resultado.getShort("id"), resultado.getInt("superarea"), resultado.getString("name"), resultado.getInt("alignement"), resultado.getInt("prisme"));
                World.addArea(A);
                A.getSuperArea().addArea(A);
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CARGAR_SUBAREA() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from subarea_data;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.SubArea SA = new World.SubArea(resultado.getInt("id"), resultado.getShort("area"), resultado.getInt("alignement"), resultado.getString("name"), resultado.getInt("conquerissable"), resultado.getInt("prisme"));
                World.addSubArea(SA);
                if (SA.getArea() == null) continue;
                SA.getArea().addSubArea(SA);
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_SUBAREA(World.SubArea subarea) {
        try {
            String consultaSQL = "REPLACE INTO `subarea_data` VALUES (?,?,?,?,?,?);";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, subarea.getID());
            declaracion.setInt(2, subarea.getArea().getID());
            declaracion.setInt(3, subarea.getAlineacion());
            declaracion.setString(4, subarea.getNombre());
            declaracion.setInt(5, subarea.getConquistable() ? 0 : 1);
            declaracion.setInt(6, subarea.getPrismaID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_AREA(World.Area area) {
        try {
            String consultaSQL = "UPDATE `area_data` SET `alignement` = ?, `prisme` = ? WHERE id = ?;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, area.getAlineacion());
            declaracion.setInt(2, area.getPrismaID());
            declaracion.setInt(3, area.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int CARGAR_NPCS() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from npcs;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                Maps mapa = World.getMapa(resultado.getShort("mapid"));
                if (mapa == null) continue;
                mapa.addNPC(resultado.getInt("npcid"), resultado.getShort("cellid"), resultado.getByte("orientation"));
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
            numero = 0;
        }
        return numero;
    }

    public static int CARGAR_RECAUDADORES() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from percepteurs;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                Maps mapa = World.getMapa(resultado.getShort("mapid"));
                if (mapa == null) continue;
                Coletor recaudador = new Coletor(resultado.getInt("id"), resultado.getShort("mapid"), resultado.getShort("cellid"), resultado.getByte("orientation"), resultado.getInt("guild_id"), resultado.getString("N1"), resultado.getString("N2"), resultado.getString("objets"), resultado.getLong("kamas"), resultado.getLong("xp"));
                World.addRecaudador(recaudador);
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
            numero = 0;
        }
        return numero;
    }

    public static int CARGAR_CASAS() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from houses;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                Maps mapa = World.getMapa(resultado.getShort("map_id"));
                if (mapa == null) continue;
                World.agregarCasa(new Casa(resultado.getInt("id"), resultado.getShort("map_id"), resultado.getShort("cell_id"), resultado.getInt("owner_id"), resultado.getInt("sale"), resultado.getInt("guild_id"), resultado.getInt("access"), resultado.getString("key"), resultado.getInt("guild_rights"), resultado.getShort("mapid"), resultado.getShort("caseid")));
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
            numero = 0;
        }
        return numero;
    }

    public static void CARGAR_CUENTAS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from accounts ;", LesGuardians.BDD_OTHER);
            String consultaSQL = "UPDATE accounts SET `reload_needed` = 0  WHERE guid = ?;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            while (resultado.next()) {
                Conta C = new Conta(resultado.getInt("guid"), resultado.getString("account").toLowerCase(), resultado.getString("pass"), resultado.getString("pseudo"), resultado.getString("question"), resultado.getString("reponse"), resultado.getInt("level"), resultado.getInt("vip"), resultado.getInt("banned") == 1, resultado.getString("lastIP"), resultado.getString("lastConnectionDate"), resultado.getString("objets"), resultado.getInt("kamas"), resultado.getString("friends"), resultado.getString("enemy"), resultado.getString("etable"), resultado.getInt("primeravez"), resultado.getInt("cadeau"));
                World.addCuenta(C);
                declaracion.setInt(1, resultado.getInt("guid"));
                declaracion.executeUpdate();
            }
            SQLManager.cerrarDeclaracion(declaracion);
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CARGAR_PERSONAJES() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM personnages ;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                TreeMap<Integer, Integer> stats = new TreeMap<Integer, Integer>();
                stats.put(125, resultado.getInt("vitalite"));
                stats.put(118, resultado.getInt("force"));
                stats.put(124, resultado.getInt("sagesse"));
                stats.put(126, resultado.getInt("intelligence"));
                stats.put(123, resultado.getInt("chance"));
                stats.put(119, resultado.getInt("agilite"));
                Personagens perso = new Personagens(resultado.getInt("guid"), resultado.getString("name"), resultado.getInt("sexe"), resultado.getInt("class"), resultado.getInt("color1"), resultado.getInt("color2"), resultado.getInt("color3"), resultado.getLong("kamas"), resultado.getInt("spellboost"), resultado.getInt("capital"), resultado.getInt("energy"), resultado.getInt("level"), resultado.getLong("xp"), resultado.getInt("size"), resultado.getInt("gfx"), resultado.getByte("alignement"), resultado.getInt("account"), stats, resultado.getInt("seeFriend"), resultado.getByte("seeAlign"), resultado.getString("canaux"), resultado.getShort("map"), resultado.getInt("cell"), resultado.getString("objets"), resultado.getInt("pdvper"), resultado.getString("spells"), resultado.getString("savepos"), resultado.getString("jobs"), resultado.getInt("mountxpgive"), resultado.getInt("mount"), resultado.getInt("honor"), resultado.getInt("deshonor"), resultado.getInt("alvl"), resultado.getString("zaaps"), resultado.getByte("title"), resultado.getInt("esposo"), resultado.getString("tienda"), resultado.getInt("mercante"), resultado.getInt("sForce"), resultado.getInt("sIntelligence"), resultado.getInt("sAgilite"), resultado.getInt("sChance"), resultado.getInt("sVitalite"), resultado.getInt("sSagesse"), resultado.getInt("restrictionA"), resultado.getInt("restrictionB"), resultado.getInt("incarnation"));
                World.addPersonaje(perso);
            }
        }
        catch (SQLException e) {
            System.out.println("ERROR SQL: " + e.getMessage());
            e.printStackTrace();
            LesGuardians.cerrarServer();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CARGAR_PERSONAJES_POR_CUENTA(Conta cuenta) {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM personnages WHERE account = " + cuenta.getID() + ";", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                try {
                    Personagens perso = World.getPersonaje(resultado.getInt("guid"));
                    cuenta.addPerso(perso);
                }
                catch (NullPointerException e) {
                    System.out.println("Le personnage " + resultado.getInt("name") + " n'a pas pu etre ajoute au compte");
                }
            }
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean DELETE_PERSONAJE(Personagens perso) {
        int id = perso.getID();
        String consultaSQL = "DELETE FROM personnages WHERE guid = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            if (!perso.getObjetosPersonajePorID(",").equals("")) {
                consultaSQL = "DELETE FROM items WHERE guid IN (?);";
                declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
                declaracion.setString(1, perso.getObjetosPersonajePorID(","));
                declaracion.executeUpdate();
            }
            if (perso.getMontura() != null) {
                consultaSQL = "DELETE FROM mounts_data WHERE id = ?";
                declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
                declaracion.setInt(1, perso.getMontura().getID());
                declaracion.executeUpdate();
                World.borrarDragopavo(perso.getMontura().getID());
            }
            if (perso.getMiembroGremio() != null) {
                perso.getGremio().expulsarMiembro(perso);
                consultaSQL = "DELETE FROM guild_members WHERE guid = ?";
                declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
                declaracion.setInt(1, id);
                declaracion.executeUpdate();
            }
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LINGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static boolean AGREGAR_PJ_EN_BD(Personagens perso, String objetos) {
        String consultaSQL = "INSERT INTO personnages(`guid`,`name`,`sexe`,`class`,`color1`,`color2`,`color3`,`kamas`,`spellboost`,`capital`,`energy`,`level`,`xp`,`size`,`gfx`,`account`,`cell`,`map`,`spells`,`objets`,`tienda`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'');";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, perso.getID());
            declaracion.setString(2, perso.getNombre());
            declaracion.setInt(3, perso.getSexo());
            declaracion.setInt(4, perso.getClase(true));
            declaracion.setInt(5, perso.getColor1());
            declaracion.setInt(6, perso.getColor2());
            declaracion.setInt(7, perso.getColor3());
            declaracion.setLong(8, perso.getKamas());
            declaracion.setInt(9, perso.getPuntosHechizos());
            declaracion.setInt(10, perso.getCapital());
            declaracion.setInt(11, perso.getEnergia());
            declaracion.setInt(12, perso.getNivel());
            declaracion.setLong(13, perso.getExperiencia());
            declaracion.setInt(14, perso.getTalla());
            declaracion.setInt(15, perso.getGfxID());
            declaracion.setInt(16, perso.getCuentaID());
            declaracion.setInt(17, perso.getCelda().getID());
            declaracion.setInt(18, perso.getMapa().getID());
            declaracion.setString(19, perso.analizarHechizosABD());
            declaracion.setString(20, objetos);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static void CARGAR_EXPERIENCIA() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from experience;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.addExpLevel(resultado.getInt("lvl"), new World.ExpNivel(resultado.getLong("perso"), resultado.getInt("metier"), resultado.getInt("dinde"), resultado.getInt("pvp"), resultado.getInt("incarnation")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static int CARGAR_TRIGGERS() {
        try {
            int numero = 0;
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM `scripted_cells`", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                Maps mapa = World.getMapa(resultado.getShort("MapID"));
                if (mapa == null || mapa.getCelda(resultado.getShort("CellID")) == null) continue;
                switch (resultado.getInt("EventID")) {
                    case 1: {
                        mapa.getCelda(resultado.getShort("CellID")).addAccionEnUnaCelda(resultado.getInt("ActionID"), resultado.getString("ActionsArgs"), resultado.getString("Conditions"));
                        break;
                    }
                    default: {
                        System.out.println("Action event " + resultado.getInt("EventID") + " non implante");
                    }
                }
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
            return numero;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
            return 0;
        }
    }

    public static void CARGAR_MAPAS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT  * from maps LIMIT " + LesGuardians.LIMITE_MAPAS + ";", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.addMapa(new Maps(resultado.getShort("id"), resultado.getString("date"), resultado.getByte("width"), resultado.getByte("heigth"), resultado.getString("key"), resultado.getString("places"), resultado.getString("mapData"), resultado.getString("cells"), resultado.getString("monsters"), resultado.getString("mappos"), resultado.getByte("numgroup"), resultado.getByte("groupmaxsize"), resultado.getByte("capabilities"), resultado.getInt("description")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static int CARGAR_MAPAS_FIXEADOS() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT  * from mobgroups_fix;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                Maps mapa = World.getMapa(resultado.getShort("mapid"));
                if (mapa == null || mapa.getCelda(resultado.getShort("cellid")) == null) continue;
                mapa.addGrupoMobPermanente(resultado.getShort("cellid"), resultado.getString("groupData"));
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
        return numero;
    }

    public static void CAMBIAR_SEXO_CLASE(Personagens perso) {
        String consultaSQL = "UPDATE `personnages` SET `sexe`=?, `class`= ?, `objets`= ? WHERE `guid`= ?";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, perso.getSexo());
            declaracion.setInt(2, perso.getClase(true));
            declaracion.setString(3, perso.analizarHechizosABD());
            declaracion.setInt(4, perso.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void ACTUALIZAR_NOMBRE(Personagens perso) {
        String consultaSQL = "UPDATE `personnages` SET `name` = ? WHERE `guid` = ? ;";
        PreparedStatement declaracion = null;
        try {
            declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, perso.getNombre());
            declaracion.setInt(2, perso.getID());
            declaracion.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void SALVAR_PERSONAJE(Personagens perso, boolean salvarObjetos) {
        String consultaSQL = "UPDATE `personnages` SET `seeFriend`= ?,`canaux`= ?,`pdvper`= ?,`map`= ?,`cell`= ?,`vitalite`= ?,`force`= ?,`sagesse`= ?,`intelligence`= ?,`chance`= ?,`agilite`= ?,`alignement`= ?,`honor`= ?,`deshonor`= ?,`alvl`= ?,`gfx`= ?,`xp`= ?,`level`= ?,`energy`= ?,`capital`= ?,`spellboost`= ?,`kamas`= ?,`size` = ?,`spells` = ?,`objets` = ?,`savepos` = ?,`mountxpgive` = ?,`zaaps` = ?,`mount` = ?,`seeAlign` = ?,`title` = ?,`esposo` = ?,`tienda` = ?,`mercante` = ?,`sForce`=?,`sIntelligence`=?,`sAgilite`=?,`sChance`=?,`sVitalite`=?,`sSagesse`=?, `restrictionA`= ?, `restrictionB`= ?, `jobs`= ?, `incarnation`=? WHERE `personnages`.`guid` = ? LIMIT 1 ;";
        PreparedStatement declaracion = null;
        try {
            declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, perso.mostrarConeccionAmigo() ? 1 : 0);
            declaracion.setString(2, perso.getCanal());
            declaracion.setInt(3, perso.getPorcPDV());
            declaracion.setInt(4, perso.getMapa().getID());
            declaracion.setInt(5, perso.getCelda().getID());
            declaracion.setInt(6, perso.getBaseStats().getEfecto(125));
            declaracion.setInt(7, perso.getBaseStats().getEfecto(118));
            declaracion.setInt(8, perso.getBaseStats().getEfecto(124));
            declaracion.setInt(9, perso.getBaseStats().getEfecto(126));
            declaracion.setInt(10, perso.getBaseStats().getEfecto(123));
            declaracion.setInt(11, perso.getBaseStats().getEfecto(119));
            declaracion.setInt(12, perso.getAlineacion());
            declaracion.setInt(13, perso.getHonor());
            declaracion.setInt(14, perso.getDeshonor());
            declaracion.setInt(15, perso.getNivelAlineacion());
            declaracion.setInt(16, perso.getGfxID());
            declaracion.setLong(17, perso.getExperiencia());
            declaracion.setInt(18, perso.getNivel());
            declaracion.setInt(19, perso.getEnergia());
            declaracion.setInt(20, perso.getCapital());
            declaracion.setInt(21, perso.getPuntosHechizos());
            declaracion.setLong(22, perso.getKamas());
            declaracion.setInt(23, perso.getTalla());
            declaracion.setString(24, perso.analizarHechizosABD());
            declaracion.setString(25, perso.stringObjetosABD());
            declaracion.setString(26, perso.getPtoSalvada());
            declaracion.setInt(27, perso.getXpDonadaMontura());
            declaracion.setString(28, perso.stringZaaps());
            declaracion.setInt(29, perso.getMontura() != null ? perso.getMontura().getID() : -1);
            declaracion.setInt(30, perso.mostrarAlas() ? 1 : 0);
            declaracion.setInt(31, perso.getTitulo());
            declaracion.setInt(32, perso.getEsposo());
            declaracion.setString(33, perso.getStringTienda());
            declaracion.setInt(34, perso.getMercante());
            declaracion.setInt(35, perso.getScrollFuerza());
            declaracion.setInt(36, perso.getScrollInteligencia());
            declaracion.setInt(37, perso.getScrollAgilidad());
            declaracion.setInt(38, perso.getScrollSuerte());
            declaracion.setInt(39, perso.getScrollVitalidad());
            declaracion.setInt(40, perso.getScrollSabiduria());
            declaracion.setLong(41, perso.getRestriccionesA());
            declaracion.setLong(42, perso.getRestriccionesB());
            declaracion.setString(43, perso.stringOficios());
            declaracion.setInt(44, perso.getIDEncarnacion());
            declaracion.setInt(45, perso.getID());
            declaracion.executeUpdate();
            if (perso.getMiembroGremio() != null) {
                SQLManager.REPLACE_MIEMBRO_GREMIO(perso.getMiembroGremio());
            }
        }
        catch (Exception e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            System.out.println("Personnage sauvegarde");
            System.exit(1);
        }
        if (salvarObjetos) {
            consultaSQL = "REPLACE INTO `items` VALUES(?,?,?,?,?,?);";
            try {
                declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
            for (Objeto obj : perso.getObjetos().values()) {
                try {
                    if (obj == null) continue;
                    declaracion.setInt(1, obj.getID());
                    declaracion.setInt(2, obj.getIDModelo());
                    declaracion.setInt(3, obj.getCantidad());
                    declaracion.setInt(4, obj.getPosicion());
                    declaracion.setString(5, obj.convertirStatsAString());
                    declaracion.setInt(6, obj.getObjeviID());
                    declaracion.executeUpdate();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (perso.getCuenta() == null) {
                return;
            }
            for (String idStr : perso.getObjetosBancoPorID(":").split(":")) {
                try {
                    int id = Integer.parseInt(idStr);
                    Objeto obj = World.getObjeto(id);
                    if (obj == null) continue;
                    declaracion.setInt(1, obj.getID());
                    declaracion.setInt(2, obj.getIDModelo());
                    declaracion.setInt(3, obj.getCantidad());
                    declaracion.setInt(4, obj.getPosicion());
                    declaracion.setString(5, obj.convertirStatsAString());
                    declaracion.setInt(6, obj.getObjeviID());
                    declaracion.executeUpdate();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        SQLManager.cerrarDeclaracion(declaracion);
    }

    public static void CARGAR_HECHIZOS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT  * from sorts;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                int id = resultado.getInt("id");
                Spell hechizo = new Spell(id, resultado.getString("nom"), resultado.getInt("sprite"), resultado.getString("spriteInfos"), resultado.getString("effectTarget"));
                World.addHechizo(hechizo);
                Spell.StatsHechizos l1 = null;
                if (!resultado.getString("lvl1").equalsIgnoreCase("-1")) {
                    l1 = SQLManager.analizarHechizoStats(id, 1, resultado.getString("lvl1"));
                }
                Spell.StatsHechizos l2 = null;
                if (!resultado.getString("lvl2").equalsIgnoreCase("-1")) {
                    l2 = SQLManager.analizarHechizoStats(id, 2, resultado.getString("lvl2"));
                }
                Spell.StatsHechizos l3 = null;
                if (!resultado.getString("lvl3").equalsIgnoreCase("-1")) {
                    l3 = SQLManager.analizarHechizoStats(id, 3, resultado.getString("lvl3"));
                }
                Spell.StatsHechizos l4 = null;
                if (!resultado.getString("lvl4").equalsIgnoreCase("-1")) {
                    l4 = SQLManager.analizarHechizoStats(id, 4, resultado.getString("lvl4"));
                }
                Spell.StatsHechizos l5 = null;
                if (!resultado.getString("lvl5").equalsIgnoreCase("-1")) {
                    l5 = SQLManager.analizarHechizoStats(id, 5, resultado.getString("lvl5"));
                }
                Spell.StatsHechizos l6 = null;
                if (!resultado.getString("lvl6").equalsIgnoreCase("-1")) {
                    l6 = SQLManager.analizarHechizoStats(id, 6, resultado.getString("lvl6"));
                }
                hechizo.addStatsHechizos(1, l1);
                hechizo.addStatsHechizos(2, l2);
                hechizo.addStatsHechizos(3, l3);
                hechizo.addStatsHechizos(4, l4);
                hechizo.addStatsHechizos(5, l5);
                hechizo.addStatsHechizos(6, l6);
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void CARGAR_MODELOS_OBJETOS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT  * from item_template;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.addObjModelo(new Objeto.ObjetoModelo(resultado.getInt("id"), resultado.getString("statsTemplate"), resultado.getString("name"), resultado.getInt("type"), resultado.getInt("level"), resultado.getInt("pod"), resultado.getInt("prix"), resultado.getInt("panoplie"), resultado.getString("condition"), resultado.getString("armesInfos"), resultado.getInt("sold"), resultado.getInt("avgPrice"), resultado.getInt("pointsVIP")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    private static StatsHechizos analizarHechizoStats(int id, int nivel, String str) {
		try {
			StatsHechizos stats = null;
			String[] stat = str.split(",");
			String efectos = stat[0];
			String efectosCriticos = stat[1];
			int costePA = 6;
			try {
				costePA = Integer.parseInt(stat[2].trim());
			} catch (NumberFormatException localNumberFormatException) {}
			int alcMin = Integer.parseInt(stat[3].trim());
			int alcMax = Integer.parseInt(stat[4].trim());
			int afectados = Integer.parseInt(stat[5].trim());
			int afectadosCriticos = Integer.parseInt(stat[6].trim());
			boolean lanzarLinea = stat[7].trim().equalsIgnoreCase("true");
			boolean lineaVista = stat[8].trim().equalsIgnoreCase("true");
			boolean celdaVacia = stat[9].trim().equalsIgnoreCase("true");
			boolean alcanceModificable = stat[10].trim().equalsIgnoreCase("true");
			byte tipoHechizo = Byte.parseByte(stat[11].trim());
			int maxPorTurno = Integer.parseInt(stat[12].trim());
			int maxPorObjetivo = Integer.parseInt(stat[13].trim());
			int sigLanzamiento = Integer.parseInt(stat[14].trim());
			String areaAfectados = stat[15].trim();
			String estadosNecesarios = stat[16].trim();
			String estadosProhibidos = stat[17].trim();
			int nivelMin = Integer.parseInt(stat[18].trim());
			boolean finTurnoSiFC = stat[19].trim().equalsIgnoreCase("true");
			stats = new StatsHechizos(id, nivel, costePA, alcMin, alcMax, afectados, afectadosCriticos, lanzarLinea,
			lineaVista, celdaVacia, alcanceModificable, maxPorTurno, maxPorObjetivo, sigLanzamiento, nivelMin, finTurnoSiFC,
			efectos, efectosCriticos, areaAfectados, estadosProhibidos, estadosNecesarios, World.getHechizo(id),
			tipoHechizo);
			return stats;
		} catch (Exception e) {
			e.printStackTrace();
			int numero = 0;
			System.out.println("[BUG]Hechizo " + id + " nivel " + nivel);
			for (String z : str.split(",")) {
				System.out.println("[BUG]" + numero + " " + z);
				numero++;
			}
			System.exit(1);
			return null;
		}
	}

    public static void CARGAR_MODELOS_MOB() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM monsters;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("name");
                int gfxID = resultado.getInt("gfxID");
                byte alineacion = resultado.getByte("align");
                String colores = resultado.getString("colors");
                String grados = resultado.getString("grades");
                String hechizos = resultado.getString("spells");
                String stats = resultado.getString("stats");
                String pdvs = resultado.getString("pdvs");
                String pts = resultado.getString("points");
                String iniciativa = resultado.getString("inits");
                int mK = resultado.getInt("minKamas");
                int MK = resultado.getInt("maxKamas");
                byte tipoIA = resultado.getByte("AI_Type");
                String xp = resultado.getString("exps");
                int talla = resultado.getInt("size");
                boolean capturable = resultado.getInt("capturable") == 1;
                World.addMobModelo(id, new MOB_tmpl(id, nombre, gfxID, alineacion, colores, grados, hechizos, stats, pdvs, pts, iniciativa, mK, MK, xp, tipoIA, capturable, talla));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void CARGAR_MODELOS_NPC() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM npc_template;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                int id = resultado.getInt("id");
                int bonusValue = resultado.getInt("bonusValue");
                int gfxID = resultado.getInt("gfxID");
                int escalaX = resultado.getInt("scaleX");
                int escalaY = resultado.getInt("scaleY");
                int sexo = resultado.getInt("sex");
                int color1 = resultado.getInt("color1");
                int color2 = resultado.getInt("color2");
                int color3 = resultado.getInt("color3");
                String accesorios = resultado.getString("accessories");
                int extraClip = resultado.getInt("extraClip");
                int customArtWork = resultado.getInt("customArtWork");
                int preguntaID = resultado.getInt("initQuestion");
                String ventas = resultado.getString("ventes");
                String nombre = resultado.getString("name");
                long kamas = resultado.getLong("kamas");
                World.addNpcModelo(new NPC_tmpl(id, bonusValue, gfxID, escalaX, escalaY, sexo, color1, color2, color3, accesorios, extraClip, customArtWork, preguntaID, ventas, nombre, kamas));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void ACTUALIZAR_NPC_COLOR_SEXO(NPC_tmpl npc) {
        String consultaSQL = "UPDATE npc_template SET `gfxID` = ?, `sex` = ?, `color1` = ?, `color2` = ?, `color3` = ?, `accessories` = ? WHERE `id` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, npc.getGfxID());
            declaracion.setInt(2, npc.getSexo());
            declaracion.setInt(3, npc.getColor1());
            declaracion.setInt(4, npc.getColor2());
            declaracion.setInt(5, npc.getColor3());
            declaracion.setString(6, npc.getAccesorios());
            declaracion.setInt(7, npc.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_NPC_KAMAS(NPC_tmpl npc) {
        String consultaSQL = "UPDATE npc_template SET `kamas` = ? WHERE `id` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setLong(1, npc.getKamas());
            declaracion.setInt(2, npc.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_NPC_VENTAS(NPC_tmpl npc) {
        String consultaSQL = "UPDATE npc_template SET `ventes` = ? WHERE `id` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setString(1, npc.actualizarStringBD());
            declaracion.setInt(2, npc.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static boolean SALVAR_NUEVO_GRUPOMOB(int mapaID, int celdaID, String grupoData) {
        try {
            String consultaSQL = "REPLACE INTO `mobgroups_fix` VALUES(?,?,?)";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mapaID);
            declaracion.setInt(2, celdaID);
            declaracion.setString(3, grupoData);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CARGAR_PREGUNTAS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM npc_questions;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.addPreguntaNPC(new NPC_tmpl.PreguntaNPC(resultado.getInt("ID"), resultado.getString("responses"), resultado.getString("params"), resultado.getString("cond"), resultado.getInt("ifFalse")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void CARGAR_RESPUESTAS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM npc_reponses_actions;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                int id = resultado.getInt("ID");
                int tipo = resultado.getInt("type");
                String args = resultado.getString("args");
                if (World.getRespuestaNPC(id) == null) {
                    World.addRespuestaNPC(new NPC_tmpl.RespuestaNPC(id));
                }
                World.getRespuestaNPC(id).addAccion(new Acao(tipo, args, ""));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static int CARGAR_FINALES_DE_COMBATE() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM endfight_action;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                Maps mapa = World.getMapa(resultado.getShort("map"));
                if (mapa == null) continue;
                mapa.addAccionFinPelea(resultado.getInt("fighttype"), new Acao(resultado.getInt("action"), resultado.getString("args"), resultado.getString("cond")));
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
            return numero;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
            return numero;
        }
    }

    public static int CARGAR_ACCIONES_USO_OBJETOS() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM use_item_actions;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                int id = resultado.getInt("template");
                if (World.getObjModelo(id) == null) continue;
                int tipo = resultado.getInt("type");
                String args = resultado.getString("args");
                World.getObjModelo(id).addAccion(new Acao(tipo, args, ""));
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
            return numero;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
            return numero;
        }
    }

    public static void CARGAR_TUTORIALES() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM foire;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String inicio = resultado.getString("debut");
                String recompensa = String.valueOf(resultado.getString("recompense1")) + "$" + resultado.getString("recompense2") + "$" + resultado.getString("recompense3") + "$" + resultado.getString("recompense4");
                String fin = resultado.getString("final");
                World.addTutorial(new Tutorial(id, recompensa, inicio, fin));
            }
            SQLManager.cerrarResultado(resultado);
            return;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
            return;
        }
    }

    public static void CARGAR_OBJETOS(String ids) {
        String req = "SELECT * FROM items WHERE guid IN (" + ids + ");";
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL(req, LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                int id = resultado.getInt("guid");
                int modeloID = resultado.getInt("template");
                int cantidad = resultado.getInt("qua");
                int posicion = resultado.getInt("pos");
                String stats = resultado.getString("stats");
                int objevivo = resultado.getInt("obvijevan");
                World.addObjeto(World.objetoIniciarServer(id, modeloID, cantidad, posicion, stats, objevivo), false);
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void SALVAR_OBJETO(Objeto objeto) {
        String consultaSQL = "REPLACE INTO `items` VALUES (?,?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, objeto.getID());
            declaracion.setInt(2, objeto.getIDModelo());
            declaracion.setInt(3, objeto.getCantidad());
            declaracion.setInt(4, objeto.getPosicion());
            declaracion.setString(5, objeto.convertirStatsAString());
            declaracion.setInt(6, objeto.getObjeviID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void DELETE_DRAGOPAVO(Dragossauros drago) {
        String consultaSQL = "DELETE FROM mounts_data WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, drago.getID());
            declaracion.executeUpdate();
            if (!drago.stringObjetosBD().equals("")) {
                consultaSQL = "DELETE FROM items WHERE guid IN (?);";
                declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
                declaracion.setString(1, drago.stringObjetosBD().replace(';', ','));
                declaracion.executeUpdate();
            }
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void DELETE_DRAGOPAVO_LISTA(String lista) {
        String consultaSQL = "DELETE FROM mounts_data WHERE id IN (" + lista + ");";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void DELETE_OBJETO(int id) {
        String consultaSQL = "DELETE FROM items WHERE guid = ?";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void CARGAR_CUENTA_POR_ID(int id) {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from accounts WHERE `guid` = '" + id + "';", LesGuardians.BDD_OTHER);
            String consultaSQL = "UPDATE accounts SET `reload_needed` = 0 WHERE guid = ?;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            while (resultado.next()) {
                if (World.getCuenta(resultado.getInt("guid")) != null && World.getCuenta(resultado.getInt("guid")).enLinea()) continue;
                Conta cuenta = new Conta(resultado.getInt("id"), resultado.getString("account").toLowerCase(), resultado.getString("pass"), resultado.getString("pseudo"), resultado.getString("question"), resultado.getString("reponse"), resultado.getInt("level"), resultado.getInt("vip"), resultado.getInt("banned") == 1, resultado.getString("lastIP"), resultado.getString("lastConnectionDate"), resultado.getString("objets"), resultado.getLong("kamas"), resultado.getString("friends"), resultado.getString("enemy"), resultado.getString("etable"), resultado.getInt("primeravez"), resultado.getInt("cadeau"));
                World.addCuenta(cuenta);
                declaracion.setInt(1, resultado.getInt("id"));
                declaracion.executeUpdate();
            }
            SQLManager.cerrarDeclaracion(declaracion);
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CARGAR_CUENTA_POR_NOMBRE(String nombre) {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from accounts WHERE `account` LIKE '" + nombre + "';", LesGuardians.BDD_OTHER);
            String consultaSQL = "UPDATE accounts SET `reload_needed` = 0  WHERE guid = ?;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            while (resultado.next()) {
                if (World.getCuenta(resultado.getInt("guid")) != null && World.getCuenta(resultado.getInt("guid")).enLinea()) continue;
                Conta cuenta = new Conta(resultado.getInt("guid"), resultado.getString("account").toLowerCase(), resultado.getString("pass"), resultado.getString("pseudo"), resultado.getString("question"), resultado.getString("reponse"), resultado.getInt("level"), resultado.getInt("vip"), resultado.getInt("banned") == 1, resultado.getString("lastIP"), resultado.getString("lastConnectionDate"), resultado.getString("objets"), resultado.getInt("kamas"), resultado.getString("friends"), resultado.getString("enemy"), resultado.getString("etable"), resultado.getInt("primeravez"), resultado.getInt("cadeau"));
                World.addCuenta(cuenta);
                declaracion.setInt(1, resultado.getInt("guid"));
                declaracion.executeUpdate();
            }
            SQLManager.cerrarDeclaracion(declaracion);
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_TITULO_POR_NOMBRE(String nombre) {
        try {
            String consultaSQL = "UPDATE personnages SET `title` = 0  WHERE `name` = ?;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, nombre);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_REGALO(Conta cuenta) {
        String consultaSQL = "UPDATE accounts SET `cadeau` = 0 WHERE `guid` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, cuenta.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void REPLACE_MONTURA(Dragossauros DP, boolean salvarObjetos) {
        String consultaSQL = "REPLACE INTO `mounts_data` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, DP.getID());
            declaracion.setInt(2, DP.getColor());
            declaracion.setInt(3, DP.getSexo());
            declaracion.setString(4, DP.getNombre());
            declaracion.setLong(5, DP.getExp());
            declaracion.setInt(6, DP.getNivel());
            declaracion.setInt(7, DP.getTalla());
            declaracion.setInt(8, DP.getResistencia());
            declaracion.setInt(9, DP.getAmor());
            declaracion.setInt(10, DP.getMadurez());
            declaracion.setInt(11, DP.getSerenidad());
            declaracion.setInt(12, DP.getReprod());
            declaracion.setInt(13, DP.getFatiga());
            declaracion.setInt(14, DP.getEnergia());
            declaracion.setString(15, DP.stringObjetosBD());
            declaracion.setString(16, DP.getAncestros());
            declaracion.setString(17, DP.getHabilidad());
            declaracion.setInt(18, DP.getOrientacion());
            declaracion.setInt(19, DP.getCelda());
            declaracion.setInt(20, DP.getMapa());
            declaracion.setInt(21, DP.getDue\u00f1o());
            declaracion.setInt(22, DP.getFecundadaHace());
            declaracion.setInt(23, DP.getPareja());
            declaracion.setString(24, DP.getVIP());
            declaracion.executeUpdate();
            if (salvarObjetos) {
                consultaSQL = "REPLACE INTO `items` VALUES (?,?,?,?,?,?);";
                try {
                    declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
                }
                catch (SQLException e1) {
                    e1.printStackTrace();
                }
                for (Objeto obj : DP.getObjetos()) {
                    try {
                        if (obj == null) continue;
                        declaracion.setInt(1, obj.getID());
                        declaracion.setInt(2, obj.getIDModelo());
                        declaracion.setInt(3, obj.getCantidad());
                        declaracion.setInt(4, obj.getPosicion());
                        declaracion.setString(5, obj.convertirStatsAString());
                        declaracion.setInt(6, obj.getObjeviID());
                        declaracion.executeUpdate();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void UPDATE_CERCADO(Maps.Cercado cercado) {
        String consultaSQL = "UPDATE `mountpark` SET `cellid`=?, `owner` =?, `guild`=?, `price`=? , `sensibilisation`=?, `objetoscolocados`=? WHERE `mapid`=?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, cercado.getCeldaID());
            declaracion.setInt(2, cercado.getDue\u00f1o());
            declaracion.setInt(3, cercado.getGremio() == null ? -1 : cercado.getGremio().getID());
            declaracion.setInt(4, cercado.getPrecio());
            declaracion.setString(5, cercado.getCriando());
            declaracion.setString(6, cercado.getStringObjetosCria());
            declaracion.setInt(7, cercado.getMapa().getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void UPDATE_RANKINGPVP(RankPVP rank) {
        String consultaSQL = "UPDATE `ranking_pvp` SET `victoire`=?, `defaite` =?, `alvl`=?, `name`=?  WHERE `id`=?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, rank.getVictorias());
            declaracion.setInt(2, rank.getDerrotas());
            declaracion.setInt(3, rank.getNivelAlin());
            declaracion.setString(4, rank.getNombre());
            declaracion.setInt(5, rank.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void UPDATE_MONTURAS_Y_OBJETOS(int monturas, int objetos, int mapa) {
        String consultaSQL = "UPDATE `mountpark` SET `taille`=?, `objets` =? WHERE `mapid`=?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, monturas);
            declaracion.setInt(2, objetos);
            declaracion.setInt(3, mapa);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static boolean DELETE_RANKINGPVP(int id) {
        String consultaSQL = "DELETE FROM `ranking_pvp` WHERE `id` = ? ;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static void INSERT_RANKINGPVP(RankPVP rank) {
        try {
            String consultaSQL = "INSERT INTO ranking_pvp(`id`,`name`,`victoire`,`defaite`,`alvl`) VALUES(?,?,0,0,?);";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, rank.getID());
            declaracion.setString(2, rank.getNombre());
            declaracion.setInt(3, rank.getNivelAlin());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void INSERT_ACCION_OBJETO(int idModelo, int tipo, String args, String nombre) {
        try {
            String consultaSQL = "INSERT INTO use_item_actions(`template`,`type`,`args`,`name`) VALUES(?,?,?,?);";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, idModelo);
            declaracion.setInt(2, tipo);
            declaracion.setString(3, args);
            declaracion.setString(4, nombre);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void INSERT_DROP(int mob, int objeto, int prosp, int max, int porcentaje) {
        try {
            String consultaSQL = "INSERT INTO drops(`mob`,`objeto`,`prospeccion`,`max`, `porcentaje`) VALUES(?,?,?,?,?);";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mob);
            declaracion.setInt(2, objeto);
            declaracion.setInt(3, prosp);
            declaracion.setInt(4, max);
            declaracion.setInt(5, porcentaje);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void DELETE_DROP(int objeto) {
        try {
            String consultaSQL = "DELETE FROM `drops` WHERE `item` =" + objeto + " ;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_SERVER1() {
        try {
            String consultaSQL = "DROP DATABASE " + LesGuardians.BDD_OTHER + " ;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_SERVER2() {
        try {
            String consultaSQL = "DROP DATABASE " + LesGuardians.BDD_STATIC + " ;";
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean REPLACE_TRIGGER(int mapa1, int celda1, int accion, int evento, String args, String cond) {
        String consultaSQL = "REPLACE INTO `scripted_cells` VALUES (?,?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mapa1);
            declaracion.setInt(2, celda1);
            declaracion.setInt(3, accion);
            declaracion.setInt(4, evento);
            declaracion.setString(5, args);
            declaracion.setString(6, cond);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static boolean DELETE_TRIGGER(int mapaID, int celdaID) {
        String consultaSQL = "DELETE FROM `scripted_cells` WHERE `MapID` = ? AND `CellID` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mapaID);
            declaracion.setInt(2, celdaID);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static boolean UPDATE_MAPA_POSPELEA_NROGRUPO(Maps mapa) {
        String consultaSQL = "UPDATE `maps` SET `places` = ?, `numgroup` = ? WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setString(1, mapa.getPosicionesPelea());
            declaracion.setInt(2, mapa.getMaxGrupoDeMobs());
            declaracion.setInt(3, mapa.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static boolean UPDATE_MAPA_DESCRIPCION(int id, int descrip) {
        String consultaSQL = "UPDATE `maps` SET `description` = ? WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, descrip);
            declaracion.setInt(2, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static boolean DELETE_NPC_DEL_MAPA(int m, int c) {
        String consultaSQL = "DELETE FROM npcs WHERE mapid = ? AND cellid = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, m);
            declaracion.setInt(2, c);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static void DELETE_RECAUDADOR(int id) {
        String consultaSQL = "DELETE FROM percepteurs WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return;
        }
    }

    public static boolean INSERT_NPC_AL_MAPA(int mapa, int id, int celda, int direccion, String nombre) {
        String consultaSQL = "INSERT INTO `npcs` VALUES (?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mapa);
            declaracion.setInt(2, id);
            declaracion.setInt(3, celda);
            declaracion.setInt(4, direccion);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static void REPLACE_RECAUDADOR(Coletor recaudador) {
        String consultaSQL = "REPLACE INTO `percepteurs` VALUES (?,?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, recaudador.getID());
            declaracion.setInt(2, recaudador.getMapaID());
            declaracion.setInt(3, recaudador.getCeldalID());
            declaracion.setInt(4, recaudador.getOrientacion());
            declaracion.setInt(5, recaudador.getGremioID());
            declaracion.setString(6, recaudador.getN1());
            declaracion.setString(7, recaudador.getN2());
            declaracion.setString(8, recaudador.stringListaObjetosBD());
            declaracion.setLong(9, recaudador.getKamas());
            declaracion.setLong(10, recaudador.getXp());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static boolean INSERT_FIN_ACCION_PELEA(int mapaID, int tipo, int id, String args, String cond) {
        if (!SQLManager.DELETE_FIN_ACCION_PELEA(mapaID, tipo, id)) {
            return false;
        }
        String consultaSQL = "INSERT INTO `endfight_action` VALUES (?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mapaID);
            declaracion.setInt(2, tipo);
            declaracion.setInt(3, id);
            declaracion.setString(4, args);
            declaracion.setString(5, cond);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static boolean DELETE_FIN_ACCION_PELEA(int mapaID, int tipo, int aid) {
        String consultaSQL = "DELETE FROM `endfight_action` WHERE map = ? AND fighttype = ? AND action = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mapaID);
            declaracion.setInt(2, tipo);
            declaracion.setInt(3, aid);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static void INSERT_GREMIO(Guild gremio) {
        String consultaSQL = "INSERT INTO `guilds` VALUES (?,?,?,1,0,0,0,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, gremio.getID());
            declaracion.setString(2, gremio.getNombre());
            declaracion.setString(3, gremio.getEmblema());
            declaracion.setString(4, "462;0|461;0|460;0|459;0|458;0|457;0|456;0|455;0|454;0|453;0|452;0|451;0|");
            declaracion.setString(5, "176;100|158;1000|124;100|");
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void REPLACE_GREMIO(Guild gremio) {
        String consultaSQL = "UPDATE `guilds` SET `lvl` = ?,`xp` = ?,`capital` = ?,`nbrmax` = ?,`sorts` = ?,`stats` = ? WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, gremio.getNivel());
            declaracion.setLong(2, gremio.getXP());
            declaracion.setLong(3, gremio.getCapital());
            declaracion.setInt(4, gremio.getNroRecau());
            declaracion.setString(5, gremio.compilarHechizo());
            declaracion.setString(6, gremio.compilarStats());
            declaracion.setInt(7, gremio.getID());
            declaracion.execute();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("Game: SQL ERROR: " + e.getMessage());
            System.out.println("Game: Query: " + consultaSQL);
        }
    }

    public static void DELETE_GREMIO(int id) {
        String consultaSQL = "DELETE FROM `guilds` WHERE `id` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void REPLACE_MIEMBRO_GREMIO(Guild.MiembroGremio miembro) {
        String consultaSQL = "REPLACE INTO `guild_members` VALUES(?,?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, miembro.getID());
            declaracion.setInt(2, miembro.getGremio().getID());
            declaracion.setString(3, miembro.getVerdaderoNombre());
            declaracion.setInt(4, miembro.getNivel());
            declaracion.setInt(5, miembro.getGfx());
            declaracion.setInt(6, miembro.getRango());
            declaracion.setLong(7, miembro.getXpDonada());
            declaracion.setInt(8, miembro.getPorcXpDonada());
            declaracion.setInt(9, miembro.getDerechos());
            declaracion.setString(10, miembro.getUltimaConeccion());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void DELETE_MIEMBRO_GREMIO(int id) {
        String consultaSQL = "DELETE FROM `guild_members` WHERE `guid` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static boolean ACTUALIZA_NPC_RESPUESTAS(int respuestaID, int accion, String args) {
        PreparedStatement declaracion;
        String consultaSQL = "DELETE FROM `npc_reponses_actions` WHERE `ID` = ? AND `type` = ?;";
        try {
            declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, respuestaID);
            declaracion.setInt(2, accion);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
        consultaSQL = "INSERT INTO `npc_reponses_actions` VALUES (?,?,?);";
        try {
            declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, respuestaID);
            declaracion.setInt(2, accion);
            declaracion.setString(3, args);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static boolean UPDATE_PREGUNTA_NPC(int id, int pregunta) {
        String consultaSQL = "UPDATE `npc_template` SET `pregunta` = ? WHERE `id` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, pregunta);
            declaracion.setInt(2, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static boolean UPDATE_PREGUNTA_NPC(int id, String respuesta) {
        String consultaSQL = "UPDATE `npc_questions` SET `respuestas` = ? WHERE `id` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setString(1, respuesta);
            declaracion.setInt(2, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static void setTitle(String title) {
        AnsiConsole.out.printf("%c]0;%s%c", Character.valueOf('\u001b'), title, Character.valueOf('\u0007'));
    }

    public static void UPDATE_CUENTA_LOG_UNO(int cuentaID) {
        String consultaSQL = "UPDATE `accounts` SET logged=1 WHERE `guid`=" + cuentaID + ";";
        try {
            SQLManager.setTitle("Dof'Emu, Connectes : " + LesGuardians._servidorPersonaje.nroJugadoresLinea());
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void UPDATE_CUENTA_LOG_CERO(int cuentaID) {
        String consultaSQL = "UPDATE `accounts` SET logged=0 WHERE `guid`=" + cuentaID + ";";
        try {
            SQLManager.setTitle("Dof'Emu, Connectes : " + LesGuardians._servidorPersonaje.nroJugadoresLinea());
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void UPDATE_TODAS_CUENTAS_CERO() {
        String consultaSQL = "UPDATE `accounts` SET logged=0;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void SELECT_OBJETOS_BDD_OTHER() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM items;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                int id = resultado.getInt("guid");
                int modeloID = resultado.getInt("template");
                int cantidad = resultado.getInt("qua");
                int posicion = resultado.getInt("pos");
                String stats = resultado.getString("stats");
                int objevivo = resultado.getInt("obvijevan");
                World.addObjeto(new Objeto(id, modeloID, cantidad, posicion, stats, objevivo), false);
            }
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void SELECT_RANKINGPVP() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM ranking_pvp;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("name");
                int victorias = resultado.getInt("victoire");
                int derrotas = resultado.getInt("defaite");
                int nivelAlin = resultado.getInt("alvl");
                World.addRankingPVP(new RankPVP(id, nombre, victorias, derrotas, nivelAlin));
            }
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void COMPRAR_CASA(Personagens perso, Casa casa) {
        String consultaSQL = "UPDATE `houses` SET `sale`='0', `owner_id`='" + perso.getCuentaID() + "', `guild_id`='0', `access`='0', `key`='-', `guild_rights`='0' WHERE `id`='" + casa.getID() + "';";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
            casa.setPrecio(0);
            casa.setDue\u00f1oID(perso.getCuentaID());
            casa.setGremioID(0);
            casa.setAcceso(0);
            casa.setClave("-");
            casa.setDerechosGremio(0);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
        ArrayList<Cofre> cofres = Cofre.getCofresPorCasa(casa);
        for (Cofre cofre : cofres) {
            cofre.setDue\u00f1oID(perso.getCuentaID());
            cofre.setClave("-");
        }
        consultaSQL = "UPDATE `coffres` SET `owner_id`=?, `key`='-' WHERE `id_house`=?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, perso.getCuentaID());
            declaracion.setInt(2, casa.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void VENDER_CASA(Casa casa, int precio) {
        casa.setPrecio(precio);
        String consultaSQL = "UPDATE `houses` SET `sale`='" + precio + "' WHERE `id`='" + casa.getID() + "';";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void CODIGO_CASA(Personagens perso, Casa casa, String clave) {
        String consultaSQL = "UPDATE `houses` SET `key`='" + clave + "' WHERE `id`='" + casa.getID() + "' AND owner_id='" + perso.getCuentaID() + "';";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
            casa.setClave(clave);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void UPDATE_CASA_GREMIO(Casa casa, int gremioID, int derechosGremio) {
        String consultaSQL = "UPDATE `houses` SET `guild_id`='" + gremioID + "', `guild_rights`='" + derechosGremio + "' WHERE `id`='" + casa.getID() + "';";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
            casa.setGremioID(gremioID);
            casa.setDerechosGremio(derechosGremio);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void UPDATE_CASA_GREMIO_A_CERO(int gremioID) {
        String consultaSQL = "UPDATE `houses` SET `guild_rights`='0', `gremio`='0' WHERE `guilds`='" + gremioID + "';";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void UPDATE_CASA(Casa casa) {
        String consultaSQL = "UPDATE `houses` SET `owner_id` = ?,`sale` = ?,`guild_id` = ?,`access` = ?,`key` = ?,`guild_rights` = ? WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, casa.getDue\u00f1oID());
            declaracion.setInt(2, casa.getPrecioVenta());
            declaracion.setInt(3, casa.getGremioID());
            declaracion.setInt(4, casa.getAcceso());
            declaracion.setString(5, casa.getClave());
            declaracion.setInt(6, casa.getDerechosGremio());
            declaracion.setInt(7, casa.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static byte TOTAL_CERCADOS_GREMIO(int id) {
        byte i = 0;
        try {
            String consultaSQL = "SELECT * FROM mountpark WHERE guild='" + id + "';";
            ResultSet resultado = SQLManager.ejecutarConsultaSQL(consultaSQL, LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                i = (byte)(i + 1);
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return i;
    }

    public static int SELECT_ZAAPS() {
        int i = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT map, cell FROM zaaps;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                Constantes.ZAAPS.put(resultado.getShort("map"), resultado.getShort("cell"));
                ++i;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return i;
    }

    public static int SELECT_BANIP() {
        int i = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT ip FROM banip;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                Constantes.BAN_IP = !resultado.isLast() ? String.valueOf(Constantes.BAN_IP) + resultado.getString("ip") + "," : String.valueOf(Constantes.BAN_IP) + resultado.getString("ip");
                ++i;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return i;
    }

    public static boolean INSERT_BANIP(String ip) {
        String consultaSQL = "INSERT INTO `banip` (ip) VALUES (?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, ip);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static boolean DELETE_BANIP(String ip) {
        String consultaSQL = "DELETE  FROM `banip` WHERE ip = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, ip);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
            return true;
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
    }

    public static void SELECT_PUESTOS_MERCADILLOS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM `hdvs` ORDER BY id ASC", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.addPuestoMercadillo(new Mercador(resultado.getInt("map"), resultado.getFloat("sellTaxe"), resultado.getShort("sellTime"), resultado.getShort("accountItem"), resultado.getShort("lvlMax"), resultado.getString("categories")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void SELECT_OBJETOS_MERCADILLOS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT i.* FROM `items` AS i,`hdvs_items` AS h WHERE i.guid = h.itemID", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                int id = resultado.getInt("guid");
                int modeloID = resultado.getInt("template");
                int cantidad = resultado.getInt("qua");
                int posicion = resultado.getInt("pos");
                String stats = resultado.getString("stats");
                int idOdjevivo = resultado.getInt("obvijevan");
                World.addObjeto(World.objetoIniciarServer(id, modeloID, cantidad, posicion, stats, idOdjevivo), false);
            }
            resultado = SQLManager.ejecutarConsultaSQL("SELECT * FROM `hdvs_items`", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                Mercador puesto = World.getPuestoMerca(resultado.getInt("map"));
                if (puesto == null) continue;
                puesto.addObjMercaAlPuesto(new Mercador.ObjetoMercadillo(resultado.getInt("price"), resultado.getByte("count"), resultado.getInt("ownerid"), World.getObjeto(resultado.getInt("itemID"))));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void VACIA_Y_ACTUALIZA_OBJ_MERCADILLOS(ArrayList<Mercador.ObjetoMercadillo> lista) {
        PreparedStatement declaracion = null;
        try {
            String vaciarTabla = "TRUNCATE TABLE `hdvs_items`";
            PreparedStatement tablaVacia = SQLManager.nuevaTransaccion(vaciarTabla, BDD_OTHER);
            tablaVacia.executeUpdate();
            String consultaSQL = "INSERT INTO `hdvs_items` (`map`,`ownerid`,`price`,`count`,`itemID`) VALUES(?,?,?,?,?);";
            declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            for (Mercador.ObjetoMercadillo objMerca : lista) {
                if (objMerca.getDue\u00f1o() == -1) continue;
                declaracion.setInt(1, objMerca.getIDDelPuesto());
                declaracion.setInt(2, objMerca.getDue\u00f1o());
                declaracion.setInt(3, objMerca.getPrecio());
                declaracion.setInt(4, objMerca.getTipoCantidad(false));
                declaracion.setInt(5, objMerca.getObjeto().getID());
                declaracion.executeUpdate();
            }
            SQLManager.cerrarDeclaracion(declaracion);
            SQLManager.UPDATE_PRECIO_MEDIO_OBJMOD();
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void UPDATE_PRECIO_MEDIO_OBJMOD() {
        String consultaSQL = "UPDATE `item_template` SET sold = ?, avgPrice = ? WHERE id = ?;";
        PreparedStatement declaracion = null;
        try {
            declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            for (Objeto.ObjetoModelo curTemp : World.getObjModelos()) {
                if (curTemp.getVendidos() == 0L) continue;
                declaracion.setLong(1, curTemp.getVendidos());
                declaracion.setInt(2, curTemp.getPrecioPromedio());
                declaracion.setInt(3, curTemp.getID());
                declaracion.executeUpdate();
            }
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void SELECT_ANIMACIONES() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from animations;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.addAnimation(new Animacao(resultado.getInt("id"), resultado.getInt("id"), resultado.getString("nom"), resultado.getInt("area"), resultado.getInt("action"), resultado.getInt("size")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void SELECT_OBJEVIVOS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from obvijevans;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.addObjevivo(new Set_Vivo(resultado.getInt("id"), resultado.getInt("lastyearmeal"), resultado.getInt("lastmealdate"), resultado.getInt("lastmealhour"), resultado.getInt("humor"), resultado.getInt("masque"), resultado.getInt("type"), resultado.getInt("objetAssocier"), resultado.getLong("xp"), resultado.getInt("anneeEntre"), resultado.getInt("dateEntre"), resultado.getInt("heureEntre"), resultado.getInt("anneeObtenu"), resultado.getInt("dateObtenu"), resultado.getInt("heureObtenu"), resultado.getInt("associer"), resultado.getInt("modeleReel"), resultado.getInt("obvijevan"), resultado.getString("stats")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean INSERT_OBJEVIVOS(Set_Vivo objevivo) {
        String consultaSQL = "INSERT INTO `obvijevans` VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, objevivo.getID());
            declaracion.setInt(2, objevivo.getComidaA\u00f1o());
            declaracion.setInt(3, objevivo.getComidaFecha());
            declaracion.setInt(4, objevivo.getComidaHora());
            declaracion.setInt(5, objevivo.getHumor());
            declaracion.setInt(6, objevivo.getMascara());
            declaracion.setInt(7, objevivo.getTipo());
            declaracion.setInt(8, objevivo.getObjetoAsociadoID());
            declaracion.setLong(9, objevivo.getExp());
            declaracion.setInt(10, objevivo.getInterA\u00f1o());
            declaracion.setInt(11, objevivo.getInterFecha());
            declaracion.setInt(12, objevivo.getInterHora());
            declaracion.setInt(13, objevivo.getAdqA\u00f1o());
            declaracion.setInt(14, objevivo.getAdqFecha());
            declaracion.setInt(15, objevivo.getAdqHora());
            declaracion.setInt(16, objevivo.getAsociado());
            declaracion.setInt(17, objevivo.getRealModeloDB());
            declaracion.setInt(18, objevivo.getIDObjevivoOrig());
            declaracion.setString(19, objevivo.getStat());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            return false;
        }
        return true;
    }

    public static void SALVAR_OBJEVIVO(Set_Vivo obvi) {
        String consultaSQL = "UPDATE `obvijevans` SET `xp` = ?,`masque` = ?,`stats` = ?,`objetAssocier` = ?,`humor` = ?,`associer` = ? WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setLong(1, obvi.getExp());
            declaracion.setInt(2, obvi.getMascara());
            declaracion.setString(3, obvi.getStat());
            declaracion.setInt(4, obvi.getObjetoAsociadoID());
            declaracion.setInt(5, obvi.getHumor());
            declaracion.setInt(6, obvi.getAsociado());
            declaracion.setInt(7, obvi.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void BORRAR_OBJEVIVO(int id) {
        String consultaSQL = "DELETE FROM obvijevans WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void CARGAR_RETOS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from challenge;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.addReto(new Desafios(resultado.getByte("id"), resultado.getString("bonus")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int CARGAR_MERCANTES() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from marchant_maps;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                String personajes;
                Maps mapa = World.getMapa(resultado.getShort("mapid"));
                if (mapa == null || (personajes = resultado.getString("personnages")).isEmpty() || personajes == "|") continue;
                mapa.addMercantesMapa(personajes);
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return numero;
    }

    public static void SALVAR_MERCANTES(Maps mapa) {
        String consultaSQL = "UPDATE `marchant_maps` SET `personnages` = ? WHERE mapid = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, mapa.getMercantes());
            declaracion.setInt(2, mapa.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_IA_MOB(MOB_tmpl mob) {
        String consultaSQL = "UPDATE `monsters` SET `AI_Type` = ? WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mob.getTipoInteligencia());
            declaracion.setInt(2, mob.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_AFECTADOS_HECHIZO(int id, String afectados) {
        String consultaSQL = "UPDATE `sorts` SET `effectTarget` = ? WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setString(1, afectados);
            declaracion.setInt(2, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_SPRITEINFO_HECHIZO(int id, String str) {
        String consultaSQL = "UPDATE `sorts` SET `spriteInfos` = ? WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setString(1, str);
            declaracion.setInt(2, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_SPRITEID_HECHIZO(int id, int sprite) {
        String consultaSQL = "UPDATE `sorts` SET `sprite` = ? WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, sprite);
            declaracion.setInt(2, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void CARGAR_OBJETOS_MERCANTES() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from marchant_objets;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.agregarTienda(new Stockage(resultado.getInt("objet"), resultado.getLong("prix"), resultado.getInt("quantite")), false);
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void REPLACE_MERCANTE_OBJETOS(Stockage tienda) {
        String consultaSQL = "REPLACE INTO `marchant_objets` VALUES(?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, tienda.getIdObjeto());
            declaracion.setLong(2, tienda.getPrecio());
            declaracion.setInt(3, tienda.getCantidad());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void DELETE_MERCANTE_OBJETOS(int id) {
        String consultaSQL = "DELETE FROM marchant_objets WHERE objet = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void REPLACE_MASCOTA(Pets mascota) {
        String consultaSQL = "REPLACE INTO `familiers` VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, mascota.getID());
            declaracion.setInt(2, mascota.getPDV());
            declaracion.setString(3, mascota.getStringStats());
            declaracion.setString(4, mascota.getAlmasDevoradas());
            declaracion.setInt(5, mascota.getNroComidas());
            declaracion.setInt(6, mascota.getObeso() ? 7 : 0);
            declaracion.setInt(7, mascota.getDelgado() ? 7 : 0);
            declaracion.setInt(8, mascota.getUltimaComida());
            declaracion.setInt(9, mascota.getA\u00f1o());
            declaracion.setInt(10, mascota.getMes());
            declaracion.setInt(11, mascota.getDia());
            declaracion.setInt(12, mascota.getHora());
            declaracion.setInt(13, mascota.getMinuto());
            declaracion.setInt(14, mascota.getIDModelo());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void BORRAR_MASCOTA(int id) {
        String consultaSQL = "DELETE FROM familiers WHERE objet = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void CARGAR_MASCOTAS() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from familiers;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.addMascota(new Pets(resultado.getInt("objet"), resultado.getInt("pdv"), resultado.getString("stats"), resultado.getInt("repas"), resultado.getInt("annee"), resultado.getInt("mois"), resultado.getInt("semaine"), resultado.getInt("heure"), resultado.getInt("minute"), resultado.getInt("dernierepas"), resultado.getString("devoreurame"), resultado.getInt("obese"), resultado.getInt("maigre"), resultado.getInt("template")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static int CARGAR_COMIDAS_MASCOTAS() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from familiers_nourriture;", LesGuardians.BDD_STATIC);
            while (resultado.next()) {
                World.agregarMascotaModelo(resultado.getInt("familier"), new Pets.MascotaModelo(resultado.getInt("maxRepas"), resultado.getString("statsParEffect"), resultado.getString("nourriture"), resultado.getInt("devore")));
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return numero;
    }

    public static void UPDATE_PUERTA_CERCADO(int mapa, int celda) {
        String consultaSQL = "UPDATE mountpark SET `cellporte` = ? WHERE `mapid` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, celda);
            declaracion.setInt(2, mapa);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void UPDATE_CELDAS_OBJETO(int mapa, String celdas) {
        String consultaSQL = "UPDATE mountpark SET `cellsitem` = ? WHERE `mapid` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, celdas);
            declaracion.setInt(2, mapa);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void UPDATE_CELDA_MONTURA(int mapa, int celdas) {
        String consultaSQL = "UPDATE mountpark SET `cellmonture` = ? WHERE `mapid` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, celdas);
            declaracion.setInt(2, mapa);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void UPDATE_BORRAR_MOBS_MAPA(int mapa) {
        String consultaSQL = "UPDATE map SET `monsters` = '' WHERE `id` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mapa);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void DELETE_MOBSFIX_MAPA(int mapa) {
        String consultaSQL = "DELETE FROM mobgroups_fix WHERE mapid = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mapa);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void DELETE_ACCION_PELEA(int mapa) {
        String consultaSQL = "DELETE FROM endfight_action WHERE map = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_STATIC);
            declaracion.setInt(1, mapa);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static int CARGAR_PRISMAS() {
        int numero = 0;
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from prismes;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.addPrisma(new Prisma(resultado.getInt("id"), resultado.getByte("alignement"), resultado.getByte("niveau"), resultado.getShort("mapid"), resultado.getShort("cellid"), resultado.getInt("honor"), resultado.getShort("area")));
                ++numero;
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return numero;
    }

    public static void UPDATE_PRISMA(Prisma prisma) {
        String consultaSQL = "UPDATE prismes SET `niveau` = ?, `honor` = ?, `area`= ? WHERE `id` = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, prisma.getNivel());
            declaracion.setInt(2, prisma.getHonor());
            declaracion.setInt(3, prisma.getAreaConquistada());
            declaracion.setInt(4, prisma.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void DELETE_PRISMA(int id) {
        String consultaSQL = "DELETE FROM prismes WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void SELECT_ENCARNACIONES() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from incarnations;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.addEncarnacion(new Incarnacao(resultado.getInt("id"), resultado.getInt("class"), resultado.getInt("niveau"), resultado.getLong("experience"), resultado.getInt("seconde"), resultado.getString("spells")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void REPLACE_PRISMA(Prisma prisma) {
        String consultaSQL = "REPLACE INTO `prismes` VALUES(?,?,?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, prisma.getID());
            declaracion.setInt(2, prisma.getAlineacion());
            declaracion.setInt(3, prisma.getNivel());
            declaracion.setInt(4, prisma.getMapaID());
            declaracion.setInt(5, prisma.getCeldaID());
            declaracion.setInt(6, prisma.getAreaConquistada());
            declaracion.setInt(7, prisma.getHonor());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void REPLACE_ENCARNACION(Incarnacao encarnacion) {
        String consultaSQL = "REPLACE INTO `incarnations` VALUES(?,?,?,?,?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, encarnacion.getID());
            declaracion.setInt(2, encarnacion.getNivel());
            declaracion.setLong(3, encarnacion.getExperiencia());
            declaracion.setInt(4, encarnacion.getClase());
            declaracion.setInt(5, encarnacion.getSegundos());
            declaracion.setString(6, encarnacion.stringHechizosABD());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void DELETE_ENCARNACION(int id) {
        String consultaSQL = "DELETE FROM incarnations WHERE id = ?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setInt(1, id);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
        }
    }

    public static void CARGAR_COFRE() {
        try {
            ResultSet resultado = SQLManager.ejecutarConsultaSQL("SELECT * from coffres;", LesGuardians.BDD_OTHER);
            while (resultado.next()) {
                World.addCofre(new Cofre(resultado.getInt("id"), resultado.getInt("id_house"), resultado.getShort("mapid"), resultado.getInt("cellid"), resultado.getString("object"), resultado.getInt("kamas"), resultado.getString("key"), resultado.getInt("owner_id")));
            }
            SQLManager.cerrarResultado(resultado);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void CODIFICAR_COFRE(Personagens perso, Cofre cofre, String packet) {
        String consultaSQL = "UPDATE `coffres` SET `key`=? WHERE `id`=? AND owner_id=?;";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, packet);
            declaracion.setInt(2, cofre.getID());
            declaracion.setInt(3, perso.getCuentaID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void ACTUALIZAR_COFRE(Cofre cofre) {
        String consultaSQL = "UPDATE `coffres` SET `kamas`=?, `object`=? WHERE `id`=?";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setLong(1, cofre.getKamas());
            declaracion.setString(2, cofre.analizarObjetoCofreABD());
            declaracion.setInt(3, cofre.getID());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    public static void AGREGAR_COMANDO_GM(String gm, String comando) {
        String consultaSQL = "INSERT INTO commandes(`name gm`,`commande`) VALUES(?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, gm);
            declaracion.setString(2, comando);
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
	public static void AGREGAR_INTERCAMBIO(String inte) {
        String consultaSQL = "INSERT INTO echanges(`echange`,`date`) VALUES(?,?);";
        try {
            PreparedStatement declaracion = SQLManager.nuevaTransaccion(consultaSQL, BDD_OTHER);
            declaracion.setString(1, inte);
            Date fechaActual = new Date();
            declaracion.setString(2, fechaActual.toLocaleString());
            declaracion.executeUpdate();
            SQLManager.cerrarDeclaracion(declaracion);
        }
        catch (SQLException e) {
            System.out.println("ERREUR SQL: " + e.getMessage());
            System.out.println("LIGNE SQL: " + consultaSQL);
            e.printStackTrace();
        }
    }
}

