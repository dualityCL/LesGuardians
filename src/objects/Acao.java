package objects;

import common.Condiciones;
import common.Constantes;
import common.Fórmulas;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.World;
import java.io.PrintWriter;
import java.util.ArrayList;
import objects.Almas;
import objects.Animacao;
import objects.Casa;
import objects.Dragossauros;
import objects.MOB_tmpl;
import objects.Maps;
import objects.NPC_tmpl;
import objects.Objeto;
import objects.Personagens;
import objects.Prisma;
import objects.Profissao;
import objects.Tutorial;

public class Acao {
    private int _ID;
    private String _args;
    private String _cond;

    public Acao(int id, String args, String cond) {
        this._ID = id;
        this._args = args;
        this._cond = cond;
    }

    public String getCondiciones() {
        return this._cond;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void aplicar(Personagens perso, Personagens objetivo, int objUsadoID, short celda) {
        PrintWriter out;
        if (perso == null || perso.getCuenta().getEntradaPersonaje() == null) {
            return;
        }
        if (!(this._cond.equalsIgnoreCase("") || this._cond.equalsIgnoreCase("-1") || Condiciones.validaCondiciones(null, perso, this._cond))) {
            SocketManager.ENVIAR_Im_INFORMACION(perso, "119");
            return;
        }
        if (objetivo == null) {
            objetivo = perso;
        }
        try {
            out = objetivo.getCuenta().getEntradaPersonaje().getOut();
        }
        catch (Exception e) {
            objetivo = perso;
            out = perso.getCuenta().getEntradaPersonaje().getOut();
        }
        Objeto objUsar = World.getObjeto(objUsadoID);
        switch (this._ID) {
            case -2: {
                try {
                    if (perso.estaOcupado()) {
                        return;
                    }
                    if (perso.getGremio() != null || perso.getMiembroGremio() != null) {
                        SocketManager.ENVIAR_gC_CREAR_PANEL_GREMIO(perso, "Ea");
                        return;
                    }
                    SocketManager.ENVIAR_gn_CREAR_GREMIO(perso);
                    return;
                }
                catch (Exception exception) {
                    // empty catch block
                }
                return;
            }
            case -1: {
                try {
                    if (perso.getDeshonor() >= 1) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "183");
                        return;
                    }
                    int costo = perso.getCostoAbrirBanco();
                    if (costo > 0) {
                        long nKamas = perso.getKamas() - (long)costo;
                        if (nKamas < 0L) {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "1128;" + costo);
                            SocketManager.ENVIAR_M1_MENSAJE_SERVER(perso, "10", "", "");
                            return;
                        }
                        perso.setKamas(nKamas);
                        SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "020;" + costo);
                    }
                    SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(perso, 5, "");
                    SocketManager.ENVIAR_EL_LISTA_OBJETOS_BANCO(perso);
                    perso.setOcupado(true);
                    perso.setEnBanco(true);
                    return;
                }
                catch (Exception costo) {
                    // empty catch block
                }
                return;
            }
            case 0: {
                try {
                    short nuevoMapaID = Short.parseShort(this._args.split(",", 2)[0]);
                    short nuevaCeldaID = Short.parseShort(this._args.split(",", 2)[1]);
                    perso.teleport(nuevoMapaID, nuevaCeldaID);
                    return;
                }
                catch (Exception nuevoMapaID) {
                    // empty catch block
                }
                return;
            }
            case 1: {
                try {
                    if (this._args.equalsIgnoreCase("DV")) {
                        SocketManager.ENVIAR_DV_FINALIZAR_DIALOGO(out);
                        perso.setConversandoCon(0);
                        return;
                    }
                    int preguntaID = -1;
                    try {
                        preguntaID = Integer.parseInt(this._args);
                    }
                    catch (NumberFormatException nuevaCeldaID) {
                        // empty catch block
                    }
                    NPC_tmpl.PreguntaNPC pregunta = World.getPreguntaNPC(preguntaID);
                    if (pregunta == null) {
                        SocketManager.ENVIAR_DV_FINALIZAR_DIALOGO(out);
                        perso.setConversandoCon(0);
                        return;
                    }
                    SocketManager.ENVIAR_DQ_DIALOGO_PREGUNTA(out, pregunta.stringArgParaDialogo(perso));
                    return;
                }
                catch (Exception preguntaID) {
                    // empty catch block
                }
                return;
            }
            case 2: {
                try {
                    String quitar = this._args.split(";")[0];
                    String[] azar = this._args.split(";")[1].split("\\|");
                    int id = Integer.parseInt(quitar.split(",")[0]);
                    int cant = Integer.parseInt(quitar.split(",")[1]);
                    if (cant < 0) {
                        perso.removerObjetoPorModYCant(id, -cant);
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "022;" + -cant + "~" + id);
                    }
                    String objetoazar = azar[Fórmulas.getRandomValor(0, azar.length - 1)];
                    int ID = Integer.parseInt(objetoazar.split(",")[0]);
                    int cantidad = Integer.parseInt(objetoazar.split(",")[1]);
                    boolean enviar = true;
                    if (objetoazar.split(",").length > 2) {
                        enviar = objetoazar.split(",")[2].equals("1");
                    }
                    if (cantidad > 0) {
                        Objeto.ObjetoModelo OM = World.getObjModelo(ID);
                        if (OM == null) {
                            return;
                        }
                        Objeto obj = OM.crearObjDesdeModelo(cantidad, false);
                        if (!perso.addObjetoSimilar(obj, true, -1)) {
                            World.addObjeto(obj, true);
                            perso.addObjetoPut(obj);
                            SocketManager.ENVIAR_OAKO_APARECER_OBJETO(out, obj);
                        }
                    } else {
                        perso.removerObjetoPorModYCant(ID, -cantidad);
                    }
                    if (perso.enLinea() && enviar) {
                        if (cantidad >= 0) {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "021;" + cantidad + "~" + ID);
                        } else if (cantidad < 0) {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "022;" + -cantidad + "~" + ID);
                        }
                    }
                    SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
                    return;
                }
                catch (Exception quitar) {
                    // empty catch block
                }
                return;
            }
            case 4: {
                try {
                    long cant = Integer.parseInt(this._args);
                    long tempKamas = perso.getKamas();
                    long nuevasKamas = tempKamas + cant;
                    if (nuevasKamas < 0L) {
                        nuevasKamas = 0L;
                    }
                    perso.setKamas(nuevasKamas);
                    if (!perso.enLinea()) return;
                    if (cant > 0L) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "045;" + cant);
                    } else if (cant < 0L) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "046;" + -cant);
                    }
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                    return;
                }
                catch (Exception cant) {
                    // empty catch block
                }
                return;
            }
            case 5: {
                try {
                    int id = Integer.parseInt(this._args.split(",")[0]);
                    int cant = Integer.parseInt(this._args.split(",")[1]);
                    if (cant > 0) {
                        Objeto.ObjetoModelo OM = World.getObjModelo(id);
                        if (OM == null) {
                            return;
                        }
                        Objeto obj = OM.crearObjDesdeModelo(cant, false);
                        if (!perso.addObjetoSimilar(obj, true, -1)) {
                            World.addObjeto(obj, true);
                            perso.addObjetoPut(obj);
                            SocketManager.ENVIAR_OAKO_APARECER_OBJETO(out, obj);
                        }
                    } else if (cant < 0) {
                        perso.removerObjetoPorModYCant(id, -cant);
                    }
                    if (!perso.enLinea()) {
                        return;
                    }
                    SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
                    if (cant > 0) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "021;" + cant + "~" + id);
                        return;
                    } else {
                        if (cant >= 0) return;
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "022;" + -cant + "~" + id);
                    }
                    return;
                }
                catch (Exception e6) {
                    e6.printStackTrace();
                }
                return;
            }
            case 6: {
                try {
                    int idOficio = Integer.parseInt(this._args);
                    if (World.getOficio(idOficio) == null) {
                        return;
                    }
                    if (idOficio == 2 || idOficio == 11 || idOficio == 13 || idOficio == 14 || idOficio == 15 || idOficio == 16 || idOficio == 17 || idOficio == 18 || idOficio == 19 || idOficio == 20 || idOficio == 24 || idOficio == 25 || idOficio == 26 || idOficio == 27 || idOficio == 28 || idOficio == 31 || idOficio == 36 || idOficio == 41 || idOficio == 56 || idOficio == 58 || idOficio == 60 || idOficio == 65) {
                        if (perso.getOficioPorID(idOficio) != null) {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "111");
                            return;
                        }
                        if (perso.totalOficiosBasicos() > 2) {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "19");
                            return;
                        }
                        perso.aprenderOficio(World.getOficio(idOficio));
                        return;
                    }
                    if (idOficio != 43 && idOficio != 44 && idOficio != 45 && idOficio != 46 && idOficio != 47 && idOficio != 48 && idOficio != 49 && idOficio != 50 && idOficio != 62 && idOficio != 63 && idOficio != 64) return;
                    if (perso.getOficioPorID(17) != null && perso.getOficioPorID(17).getNivel() >= 65 && idOficio == 43 || perso.getOficioPorID(11) != null && perso.getOficioPorID(11).getNivel() >= 65 && idOficio == 44 || perso.getOficioPorID(14) != null && perso.getOficioPorID(14).getNivel() >= 65 && idOficio == 45 || perso.getOficioPorID(20) != null && perso.getOficioPorID(20).getNivel() >= 65 && idOficio == 46 || perso.getOficioPorID(31) != null && perso.getOficioPorID(31).getNivel() >= 65 && idOficio == 47 || perso.getOficioPorID(13) != null && perso.getOficioPorID(13).getNivel() >= 65 && idOficio == 48 || perso.getOficioPorID(19) != null && perso.getOficioPorID(19).getNivel() >= 65 && idOficio == 49 || perso.getOficioPorID(18) != null && perso.getOficioPorID(18).getNivel() >= 65 && idOficio == 50 || perso.getOficioPorID(15) != null && perso.getOficioPorID(15).getNivel() >= 65 && idOficio == 62 || perso.getOficioPorID(16) != null && perso.getOficioPorID(16).getNivel() >= 65 && idOficio == 63 || perso.getOficioPorID(27) != null && perso.getOficioPorID(27).getNivel() >= 65 && idOficio == 64) {
                        if (perso.getOficioPorID(idOficio) != null) {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "111");
                            return;
                        }
                        if (perso.totalOficiosFM() > 2) {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "19");
                            return;
                        }
                        perso.aprenderOficio(World.getOficio(idOficio));
                        return;
                    }
                    SocketManager.ENVIAR_Im_INFORMACION(perso, "12");
                    return;
                }
                catch (Exception idOficio) {
                    // empty catch block
                }
                return;
            }
            case 7: {
                if (perso.getPelea() != null) return;
                perso.retornoPtoSalvadaPocima();
                return;
            }
            case 8: {
                try {
                    int statID = Integer.parseInt(this._args.split(",", 2)[0]);
                    int cantidad = Integer.parseInt(this._args.split(",", 2)[1]);
                    int mensajeID = 0;
                    switch (statID) {
                        case 124: {
                            if (perso.getScrollSabiduria() >= 500) {
                                SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Esse \u00e9 o m\u00e1ximo que pode ser scrollado.");
                                return;
                            }
                            perso.addScrollSabiduria(cantidad);
                            mensajeID = 9;
                            break;
                        }
                        case 118: {
                            if (perso.getScrollFuerza() >= 500) {
                                SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Esse \u00e9 o m\u00e1ximo que pode ser scrollado.");
                                return;
                            }
                            perso.addScrollFuerza(cantidad);
                            mensajeID = 10;
                            break;
                        }
                        case 123: {
                            if (perso.getScrollSuerte() >= 500) {
                                SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Esse \u00e9 o m\u00e1ximo que pode ser scrollado.");
                                return;
                            }
                            perso.addScrollSuerte(cantidad);
                            mensajeID = 11;
                            break;
                        }
                        case 119: {
                            if (perso.getScrollAgilidad() >= 500) {
                                SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Esse \u00e9 o m\u00e1ximo que pode ser scrollado.");
                                return;
                            }
                            perso.addScrollAgilidad(cantidad);
                            mensajeID = 12;
                            break;
                        }
                        case 125: {
                            if (perso.getScrollVitalidad() >= 500) {
                                SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Esse \u00e9 o m\u00e1ximo que pode ser scrollado.");
                                return;
                            }
                            perso.addScrollVitalidad(cantidad);
                            mensajeID = 13;
                            break;
                        }
                        case 126: {
                            if (perso.getScrollInteligencia() >= 500) {
                                SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Esse \u00e9 o m\u00e1ximo que pode ser scrollado.");
                                return;
                            }
                            perso.addScrollInteligencia(cantidad);
                            mensajeID = 14;
                        }
                    }
                    perso.getBaseStats().addStat(statID, cantidad);
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                    if (mensajeID <= 0) return;
                    SocketManager.ENVIAR_Im_INFORMACION(perso, "0" + mensajeID + ";" + cantidad);
                    return;
                }
                catch (Exception statID) {
                    // empty catch block
                }
                return;
            }
            case 9: {
                try {
                    int sID = Integer.parseInt(this._args);
                    if (World.getHechizo(sID) == null) {
                        return;
                    }
                    perso.aprenderHechizo(sID, 1, false, true);
                    return;
                }
                catch (Exception sID) {
                    // empty catch block
                }
                return;
            }
            case 10: {
                try {
                    int min = Integer.parseInt(this._args.split(",", 2)[0]);
                    int max = Integer.parseInt(this._args.split(",", 2)[1]);
                    if (max == 0) {
                        max = min;
                    }
                    int val = Fórmulas.getRandomValor(min, max);
                    if (objetivo.getPDV() + val > objetivo.getPDVMAX()) {
                        val = objetivo.getPDVMAX() - objetivo.getPDV();
                    }
                    objetivo.setPDV(objetivo.getPDV() + val);
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(objetivo);
                    if (objetivo.getPelea() == null) return;
                    SocketManager.ENVIAR_GTM_INFO_STATS_TODO_LUCHADORES(objetivo.getPelea(), objetivo);
                    return;
                }
                catch (Exception min) {
                    // empty catch block
                }
                return;
            }
            case 11: {
                try {
                    boolean remplaza;
                    byte nuevaAlin = Byte.parseByte(this._args.split(",", 2)[0]);
                    boolean bl = remplaza = Integer.parseInt(this._args.split(",", 2)[1]) == 1;
                    if (perso.getAlineacion() != 0 && !remplaza) {
                        return;
                    }
                    perso.modificarAlineamiento(nuevaAlin);
                    return;
                }
                catch (Exception nuevaAlin) {
                    // empty catch block
                }
                return;
            }
            case 12: {
                try {
                    boolean delObj = this._args.split(",")[0].equals("true");
                    boolean enArena = this._args.split(",")[1].equals("true");
                    if (enArena && !perso.getMapa().esArena()) {
                        SocketManager.ENVIAR_Im_INFORMACION(out, "113");
                        return;
                    }
                    Almas piedraAlma = (Almas)objUsar;
                    String grupoMobs = piedraAlma.analizarGrupo();
                    String condicion = "MiS = " + perso.getID();
                    perso.getMapa().addGrupoMobTimer(true, perso.getCelda().getID(), grupoMobs, condicion);
                    if (!delObj) return;
                    perso.borrarObjetoEliminar(objUsadoID, 1, true);
                    return;
                }
                catch (Exception delObj) {
                    // empty catch block
                }
                return;
            }
            case 13: {
                try {
                    perso.resetearStats();
                    perso.addCapital((perso.getNivel() - 1) * 5 - perso.getCapital());
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                    return;
                }
                catch (Exception delObj) {
                    // empty catch block
                }
                return;
            }
            case 14: {
                try {
                    perso.setOlvidandoHechizo(true);
                    SocketManager.ENVIAR_SF_OLVIDAR_HECHIZO('+', perso);
                    return;
                }
                catch (Exception delObj) {
                    // empty catch block
                }
                return;
            }
            case 15: {
                try {
                    short nuevoMapaID = Short.parseShort(this._args.split(",")[0]);
                    short nuevaCeldaID = Short.parseShort(this._args.split(",")[1]);
                    int objNecesario = Integer.parseInt(this._args.split(",")[2]);
                    int mapaNecesario = Integer.parseInt(this._args.split(",")[3]);
                    if (objNecesario == 0) {
                        perso.teleport(nuevoMapaID, nuevaCeldaID);
                        return;
                    }
                    if (objNecesario <= 0) return;
                    if (perso.tieneObjModeloNoEquip(objNecesario, 1)) {
                        perso.removerObjetoPorModYCant(objNecesario, 1);
                        SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
                        if (mapaNecesario == 0) {
                            perso.teleport(nuevoMapaID, nuevaCeldaID);
                            return;
                        } else {
                            if (mapaNecesario <= 0) return;
                            if (perso.getMapa().getID() == mapaNecesario) {
                                perso.teleport(nuevoMapaID, nuevaCeldaID);
                                return;
                            } else {
                                if (perso.getMapa().getID() == mapaNecesario) return;
                                SocketManager.ENVIAR_Im_INFORMACION(perso, "113");
                            }
                        }
                        return;
                    }
                    SocketManager.ENVIAR_Im_INFORMACION(perso, "14|45");
                    return;
                }
                catch (Exception nuevoMapaID) {
                    // empty catch block
                }
                return;
            }
            case 16: {
                try {
                    if (perso.getAlineacion() == 0) return;
                    int addHonor = Integer.parseInt(this._args);
                    perso.addHonor(addHonor);
                    return;
                }
                catch (Exception addHonor) {
                    // empty catch block
                }
                return;
            }
            case 17: {
                try {
                    int oficioID = Integer.parseInt(this._args.split(",")[0]);
                    int exp = Integer.parseInt(this._args.split(",")[1]);
                    if (perso.getOficioPorID(oficioID) == null) {
                        return;
                    }
                    perso.getOficioPorID(oficioID).addXP(perso, exp);
                    return;
                }
                catch (Exception oficioID) {
                    // empty catch block
                }
                return;
            }
            case 18: {
                if (!Casa.tieneOtraCasa(perso) || !perso.tieneObjModeloNoEquip(objUsar.getModelo().getID(), 1)) {
                    return;
                }
                perso.removerObjetoPorModYCant(objUsar.getModelo().getID(), 1);
                Casa casa = Casa.getCasaDePj(perso);
                if (casa == null) {
                    return;
                }
                perso.teleport(casa.getMapaIDDentro(), casa.getCeldaIDDentro());
                return;
            }
            case 19: {
                SocketManager.GAME_SEND_GUILDHOUSE_PACKET(perso);
                return;
            }
            case 20: {
                int pts = Integer.parseInt(this._args);
                if (pts < 1) {
                    return;
                }
                perso.addPuntosHechizos(pts);
                SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                return;
            }
            case 21: {
                try {
                    int energia = Integer.parseInt(this._args);
                    if (energia < 1) {
                        return;
                    }
                    perso.agregarEnergia(energia);
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                    return;
                }
                catch (Exception energia) {
                    // empty catch block
                }
                return;
            }
            case 22: {
                long expAgregar = Integer.parseInt(this._args);
                if (expAgregar < 1L) {
                    return;
                }
                long totalXp = perso.getExperiencia() + expAgregar;
                perso.setExperiencia(totalXp);
                SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                return;
            }
            case 23: {
                int oficio = Integer.parseInt(this._args);
                if (oficio < 1) {
                    return;
                }
                Profissao.StatsOficio statsOficio = perso.getOficioPorID(oficio);
                if (statsOficio == null) {
                    return;
                }
                int pos = statsOficio.getPosicion();
                perso.olvidarOficio(pos);
                SocketManager.ENVIAR_JR_OLVIDAR_OFICIO(perso, oficio);
                SQLManager.SALVAR_PERSONAJE(perso, false);
                return;
            }
            case 24: {
                int gfxID = Integer.parseInt(this._args);
                if (gfxID < 0) {
                    return;
                }
                perso.setGfxID(gfxID);
                SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(perso.getMapa(), perso.getID());
                SocketManager.ENVIAR_GM_AGREGAR_PJ_A_TODOS(perso.getMapa(), perso);
                return;
            }
            case 25: {
                int gfxOriginal = perso.getClase(true) * 10 + perso.getSexo();
                perso.setGfxID(gfxOriginal);
                SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(perso.getMapa(), perso.getID());
                SocketManager.ENVIAR_GM_AGREGAR_PJ_A_TODOS(perso.getMapa(), perso);
                return;
            }
            case 26: {
                SocketManager.GAME_SEND_GUILDENCLO_PACKET(perso);
                return;
            }
            case 27: {
                try {
                    String mobGrupo = "";
                    for (String mobYNivel : this._args.split("\\|")) {
                        int mobID = -1;
                        int mobNivel = -1;
                        String[] mobONivel = mobYNivel.split(",");
                        mobID = Integer.parseInt(mobONivel[0]);
                        mobNivel = Integer.parseInt(mobONivel[1]);
                        if (World.getMobModelo(mobID) == null || World.getMobModelo(mobID).getGradoPorNivel(mobNivel) == null) {
                            System.out.println("MobGrupo invalido mobID:" + mobID + " mobNivel:" + mobNivel);
                            continue;
                        }
                        mobGrupo = String.valueOf(mobGrupo) + mobID + "," + mobNivel + "," + mobNivel + ";";
                    }
                    if (mobGrupo.isEmpty()) {
                        return;
                    }
                    MOB_tmpl.GrupoMobs grupo = new MOB_tmpl.GrupoMobs(perso.getMapa()._sigIDMapaInfo, perso.getCelda().getID(), mobGrupo);
                    perso.getMapa().iniciarPeleaVSMobs(perso, grupo);
                    return;
                }
                catch (Exception mobGrupo) {
                    // empty catch block
                }
                return;
            }
            case 28: {
                try {
                    if (perso.getMontura() != null) {
                        if (perso.getMontura().esMontable() == 1) {
                            perso.subirBajarMontura();
                            return;
                        } else {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "1176");
                        }
                        return;
                    } else {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "MOUNT_NO_EQUIP");
                    }
                    return;
                }
                catch (Exception mobGrupo) {
                    // empty catch block
                }
                return;
            }
            case 29: {
                try {
                    short mapa = perso.getMapa().getID();
                    switch (mapa) {
                        case 13105: {
                            if (perso.getBaseStats().getEfecto(118) >= 21) break;
                            perso.getBaseStats().addStat(118, 1);
                            break;
                        }
                        case 13125: {
                            if (perso.getBaseStats().getEfecto(126) >= 21) break;
                            perso.getBaseStats().addStat(126, 1);
                            break;
                        }
                        case 13145: {
                            if (perso.getBaseStats().getEfecto(119) >= 21) break;
                            perso.getBaseStats().addStat(119, 1);
                            break;
                        }
                        case 13165: {
                            if (perso.getBaseStats().getEfecto(123) >= 21) break;
                            perso.getBaseStats().addStat(123, 1);
                            break;
                        }
                        case 13110: {
                            if (perso.getBaseStats().getEfecto(118) >= 41) break;
                            perso.getBaseStats().addStat(118, 1);
                            break;
                        }
                        case 13130: {
                            if (perso.getBaseStats().getEfecto(126) >= 41) break;
                            perso.getBaseStats().addStat(126, 1);
                            break;
                        }
                        case 13150: {
                            if (perso.getBaseStats().getEfecto(119) >= 41) break;
                            perso.getBaseStats().addStat(119, 1);
                            break;
                        }
                        case 13170: {
                            if (perso.getBaseStats().getEfecto(123) >= 41) break;
                            perso.getBaseStats().addStat(123, 1);
                            break;
                        }
                        case 13115: {
                            if (perso.getBaseStats().getEfecto(118) >= 81) break;
                            perso.getBaseStats().addStat(118, 1);
                            break;
                        }
                        case 13135: {
                            if (perso.getBaseStats().getEfecto(126) >= 81) break;
                            perso.getBaseStats().addStat(126, 1);
                            break;
                        }
                        case 13155: {
                            if (perso.getBaseStats().getEfecto(119) >= 81) break;
                            perso.getBaseStats().addStat(119, 1);
                            break;
                        }
                        case 13175: {
                            if (perso.getBaseStats().getEfecto(123) >= 81) break;
                            perso.getBaseStats().addStat(123, 1);
                            break;
                        }
                        case 13120: {
                            if (perso.getBaseStats().getEfecto(118) >= 101) break;
                            perso.getBaseStats().addStat(118, 1);
                            break;
                        }
                        case 13140: {
                            if (perso.getBaseStats().getEfecto(126) >= 101) break;
                            perso.getBaseStats().addStat(126, 1);
                            break;
                        }
                        case 13160: {
                            if (perso.getBaseStats().getEfecto(119) >= 101) break;
                            perso.getBaseStats().addStat(119, 1);
                            break;
                        }
                        case 13180: {
                            if (perso.getBaseStats().getEfecto(123) >= 101) break;
                            perso.getBaseStats().addStat(123, 1);
                        }
                    }
                    perso.teleport((short)6954, (short)268);
                    return;
                }
                catch (Exception mapa) {
                    // empty catch block
                }
                return;
            }
            case 30: {
                perso.getMapa().refrescarGrupoMobs();
                return;
            }
            case 31: {
                try {
                    if (perso.getEncarnacion() != null) {
                        return;
                    }
                    int clase = Integer.parseInt(this._args);
                    if (clase == perso.getClase(true)) {
                        SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Voc\u00ea j\u00e1 pertence a essa classe.");
                        return;
                    }
                    int nivel = perso.getNivel();
                    perso.setClase(clase);
                    perso.resetearStats();
                    Thread.sleep(150L);
                    perso.setCapital(0);
                    perso.setPtosHechizos(0);
                    perso.setHechizos(Constantes.getHechizosIniciales(clase));
                    Thread.sleep(150L);
                    perso.setNivel(1);
                    while (perso.getNivel() < nivel) {
                        perso.subirNivel(false);
                    }
                    perso.deformar();
                    SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(perso.getMapa(), perso);
                    SocketManager.ENVIAR_ASK_PERSONAJE_SELECCIONADO(out, perso);
                    SocketManager.ENVIAR_SL_LISTA_HECHIZOS(perso);
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                    SQLManager.CAMBIAR_SEXO_CLASE(perso);
                    return;
                }
                catch (Exception clase) {
                    // empty catch block
                }
                return;
            }
            case 32: {
                try {
                    perso.cambiarSexo();
                    Thread.sleep(300L);
                    perso.deformar();
                    SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(perso.getMapa(), perso);
                    SQLManager.CAMBIAR_SEXO_CLASE(perso);
                    return;
                }
                catch (Exception clase) {
                    // empty catch block
                }
                return;
            }
            case 33: {
                Objeto nuevo;
                int objBoost = Integer.parseInt(this._args);
                Objeto obje = perso.getObjPosicion(20);
                if (obje != null) {
                    int idObj = obje.getID();
                    perso.borrarObjetoSinEliminar(idObj);
                    World.eliminarObjeto(idObj);
                    SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(out, idObj);
                }
                if (!perso.addObjetoSimilar(nuevo = World.getObjModelo(objBoost).crearObjPosDesdeModelo(1, 20, false), true, -1)) {
                    World.addObjeto(nuevo, true);
                    perso.addObjetoPut(nuevo);
                    SocketManager.ENVIAR_OAKO_APARECER_OBJETO(out, nuevo);
                }
                SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
                SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                return;
            }
            case 34: {
                int posAMover = Integer.parseInt(this._args);
                Objeto objetoPos = perso.getObjPosicion(posAMover);
                if (objetoPos != null) {
                    String maxStats = Objeto.ObjetoModelo.generarStatsModeloDB(objetoPos.getModelo().getStringStatsObj(), true);
                    objetoPos.clearTodo();
                    objetoPos.convertirStringAStats(maxStats);
                    perso.borrarObjetoEliminar(objUsadoID, 1, true);
                    SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(out, objetoPos);
                    SQLManager.SALVAR_OBJETO(objetoPos);
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                    return;
                } else {
                    SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "O objeto na\u00f5 foi encontrado.");
                }
                return;
            }
            case 35: {
                try {
                    int kamasApostar = Integer.parseInt(this._args);
                    long tempKamas = perso.getKamas();
                    if (tempKamas < (long)kamasApostar) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "182");
                        return;
                    }
                    perso.setKamas(tempKamas - (long)kamasApostar);
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                    perso.setPescarKuakua(true);
                    return;
                }
                catch (Exception kamasApostar) {
                    // empty catch block
                }
                return;
            }
            case 36: {
                try {
                    Tutorial tuto;
                    int aleatorio;
                    long precio = Integer.parseInt(this._args.split(";")[0]);
                    int tutorial = Integer.parseInt(this._args.split(";")[1]);
                    if (tutorial == 30 && (aleatorio = Fórmulas.getRandomValor(1, 200)) == 100) {
                        tutorial = 31;
                    }
                    if ((tuto = World.getTutorial(tutorial)) == null) {
                        return;
                    }
                    if (perso.getKamas() >= precio) {
                        if (precio != 0L) {
                            perso.setKamas(perso.getKamas() - precio);
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "046;" + precio);
                        }
                        try {
                            tuto.getInicio().aplicar(perso, null, -1, (short)-1);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                        Thread.sleep(1500L);
                        SocketManager.enviar(perso, "TC" + tutorial + "|7001010000");
                        perso.setTutorial(tuto);
                        perso.setOcupado(true);
                        return;
                    }
                    SocketManager.ENVIAR_Im_INFORMACION(perso, "182");
                    return;
                }
                catch (Exception precio) {
                    // empty catch block
                }
                return;
            }
            case 37: {
                try {
                    String[] strs = this._args.split("\\|");
                    String[] strs2 = strs[1].split(",");
                    int objNecesario = Integer.parseInt(strs[0]);
                    if (perso.tieneObjModeloNoEquip(objNecesario, 1)) {
                        perso.removerObjetoPorModYCant(objNecesario, 1);
                        int objNuevo = Integer.parseInt(strs2[Fórmulas.getRandomValor(0, strs2.length - 1)]);
                        Objeto nuevoObj = World.getObjModelo(objNuevo).crearObjDesdeModelo(1, false);
                        if (!perso.addObjetoSimilar(nuevoObj, true, -1)) {
                            World.addObjeto(nuevoObj, true);
                            perso.addObjetoPut(nuevoObj);
                            SocketManager.ENVIAR_OAKO_APARECER_OBJETO(out, nuevoObj);
                        }
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "022;1~" + objNecesario);
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "021;1~" + objNuevo);
                        return;
                    } else {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "134|43");
                    }
                    return;
                }
                catch (Exception strs) {
                    // empty catch block
                }
                return;
            }
            case 38: {
                try {
                    short nuevaCelda = Short.parseShort(this._args);
                    Maps mapa = perso.getMapa();
                    SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapa, perso.getID());
                    perso.setCelda(mapa.getCelda(nuevaCelda));
                    SocketManager.ENVIAR_GM_AGREGAR_PJ_A_TODOS(perso.getMapa(), perso);
                    return;
                }
                catch (Exception nuevaCelda) {
                    // empty catch block
                }
                return;
            }
            case 39: {
                try {
                    NPC_tmpl npcModelo = World.getNPCModelo(408);
                    perso.setKamas(perso.getKamas() + npcModelo.getKamas());
                    SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
                    npcModelo.setKamas(1000000L);
                    return;
                }
                catch (Exception npcModelo) {
                    // empty catch block
                }
                return;
            }
            case 40: {
                try {
                    NPC_tmpl npcModelo = World.getNPCModelo(408);
                    npcModelo.setKamas(npcModelo.getKamas() + 10000L);
                    return;
                }
                catch (Exception npcModelo) {
                    // empty catch block
                }
                return;
            }
            case 41: {
                try {
                    String mobGrupo = "";
                    int mobID = Integer.parseInt(this._args);
                    int mobNivel = ((perso.getNivel() - 1) / 20 + 1) * 20;
                    if (World.getMobModelo(mobID) == null || World.getMobModelo(mobID).getGradoPorNivel(mobNivel) == null) {
                        System.out.println("MobGrupo invalido mobID:" + mobID + " mobNivel:" + mobNivel);
                        return;
                    }
                    mobGrupo = String.valueOf(mobGrupo) + mobID + "," + mobNivel + "," + mobNivel + ";";
                    MOB_tmpl.GrupoMobs grupo = new MOB_tmpl.GrupoMobs(perso.getMapa()._sigIDMapaInfo, perso.getCelda().getID(), mobGrupo);
                    perso.getMapa().iniciarPeleaVSDopeul(perso, grupo);
                    return;
                }
                catch (Exception mobGrupo) {
                    // empty catch block
                }
                return;
            }
            case 42: {
                try {
                    int idSet = Integer.parseInt(this._args);
                    World.ObjetoSet OS = World.getObjetoSet(idSet);
                    if (OS == null) {
                        return;
                    }
                    for (Objeto.ObjetoModelo objMod : OS.getObjetosModelos()) {
                        Objeto obj = objMod.crearObjDesdeModelo(1, false);
                        if (objetivo.addObjetoSimilar(obj, true, -1)) continue;
                        World.addObjeto(obj, true);
                        objetivo.addObjetoPut(obj);
                        SocketManager.ENVIAR_OAKO_APARECER_OBJETO(objetivo, obj);
                    }
                    SocketManager.ENVIAR_Ow_PODS_DEL_PJ(objetivo);
                    return;
                }
                catch (Exception idSet) {
                    // empty catch block
                }
                return;
            }
            case 50: {
                if (perso.getAlineacion() == 0 || perso.getAlineacion() == 3) {
                    SocketManager.ENVIAR_Im_INFORMACION(perso, "134");
                    return;
                }
                if (perso.getMisionPVP() == null) {
                    Personagens.MisionPVP mision = new Personagens.MisionPVP(0L, null, 0);
                    perso.setMisionPVP(mision);
                }
                if (perso.getMisionPVP().getTiempoPVP() == 0L || System.currentTimeMillis() - perso.getMisionPVP().getTiempoPVP() > 600000L) {
                    Personagens tempP = null;
                    ArrayList<Personagens> victimas = new ArrayList<Personagens>();
                    for (Personagens victima : World.getPJsEnLinea()) {
                        if (victima == null || victima == perso || victima.getAlineacion() == perso.getAlineacion() || victima.getAlineacion() == 0 || victima.getAlineacion() == 3 || !victima.mostrarAlas() || victima.getCuenta().getActualIP().compareTo(perso.getCuenta().getActualIP()) == 0 || victima.getNombre().equalsIgnoreCase(perso.getUltimaMision()) || perso.getNivel() + 20 < victima.getNivel() || perso.getNivel() - 20 > victima.getNivel()) continue;
                        victimas.add(victima);
                    }
                    if (victimas.size() == 0) {
                        SocketManager.ENVIAR_cs_CHAT_MENSAJE(perso, "<b>Thomas Sacre</b> : Aucune cible pour l'instant , reviens plus tard.", "000000");
                        return;
                    }
                    tempP = (Personagens)victimas.get(Fórmulas.getRandomValor(0, victimas.size() - 1));
                    String nombreVict = tempP.getNombre();
                    SocketManager.ENVIAR_cs_CHAT_MENSAJE(perso, "<b>Thomas Sacre</b> : Seu alvo \u00e9 " + nombreVict + ".", "000000");
                    perso.setUltimaMision(nombreVict);
                    perso.getMisionPVP().setPjMision(tempP);
                    perso.getMisionPVP().setTiempoPVP(System.currentTimeMillis());
                    Objeto.ObjetoModelo objModelo = World.getObjModelo(10085);
                    Objeto nuevoObj = objModelo.crearObjDesdeModelo(20, false);
                    nuevoObj.addTextoStat(989, nombreVict);
                    nuevoObj.addTextoStat(961, Integer.toHexString(tempP.getNivelAlineacion()));
                    nuevoObj.addTextoStat(962, Integer.toHexString(tempP.getNivel()));
                    Objeto pergamino = perso.getObjModeloNoEquip(10085, 1);
                    if (pergamino != null) {
                        pergamino.setCantidad(20);
                        pergamino.convertirStringAStats(nuevoObj.convertirStatsAString());
                        SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(out, pergamino);
                        return;
                    } else {
                        World.addObjeto(nuevoObj, true);
                        perso.addObjetoPut(nuevoObj);
                        SocketManager.ENVIAR_OAKO_APARECER_OBJETO(out, nuevoObj);
                    }
                    return;
                } else {
                    SocketManager.ENVIAR_cs_CHAT_MENSAJE(perso, "<b>Thomas Sacre</b> : Voc\u00ea j\u00e1 est\u00e1 em busca de algu\u00e9m, volte ap\u00f3s matar seu alvo.", "000000");
                }
                return;
            }
            case 51: {
                try {
                    if (perso.getPelea() != null || perso.esFantasma()) {
                        return;
                    }
                    String nombreCazar = "";
                    nombreCazar = objUsar.getNombreMision();
                    if (nombreCazar == null) {
                        return;
                    }
                    Personagens victima = World.getPjPorNombre(nombreCazar);
                    if (victima == null || !victima.enLinea()) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "1211");
                        return;
                    }
                    if (victima.esFantasma()) {
                        SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Seu alvo est\u00e1 em <b>Modo Fantasma</b>.");
                        return;
                    }
                    int kamas = objUsar.getRecompensaKamas();
                    if (perso.getMisionPVP() == null) {
                        perso.setMisionPVP(new Personagens.MisionPVP(0L, victima, kamas));
                    } else {
                        Personagens.MisionPVP mision = perso.getMisionPVP();
                        mision.setPjMision(victima);
                        mision.setKamasRecompensa(kamas);
                    }
                    SocketManager.ENVIAR_IC_PERSONAJE_BANDERA_COMPAS(perso, victima);
                    return;
                }
                catch (NullPointerException nombreCazar) {
                    // empty catch block
                }
                return;
            }
            case 52: {
                if (perso.getPelea() != null || perso.esFantasma()) {
                    return;
                }
                String nombreCazar = "";
                nombreCazar = World.getObjeto(objUsadoID).getNombreMision();
                if (nombreCazar == null) {
                    return;
                }
                Personagens victima = World.getPjPorNombre(nombreCazar);
                try {
                    if (victima == null || !victima.enLinea()) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "1211");
                        return;
                    }
                    if (victima.esFantasma()) {
                        SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Seu alvo est\u00e1 em <b>Modo Fantasma</b>.");
                        return;
                    }
                    if (!victima.estaMostrandoAlas()) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "1195");
                        return;
                    }
                    if (victima.getPelea() != null) {
                        SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Seu alvo est\u00e1 em <b>luta</b>.");
                        return;
                    }
                    long tiempo = System.currentTimeMillis();
                    if (!victima.getHuir()) {
                        if (tiempo - victima.getTiempoAgre() > 10000L) {
                            victima.setHuir(true);
                        } else {
                            SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Seu alvo est\u00e1 em <b>PVP</b>.");
                            return;
                        }
                    }
                    Short[] mapas = new Short[]{(short)4422, (short)7810, (short)952, (short)1887, (short)833};
                    short mapa = mapas[Fórmulas.getRandomValor(0, 4)];
                    perso.teleport(mapa, (short)399);
                    victima.teleport(mapa, (short)194);
                    perso.setHuir(false);
                    victima.setHuir(false);
                    if (perso.getMisionPVP() == null) {
                        perso.setMisionPVP(new Personagens.MisionPVP(0L, victima, 0));
                    } else {
                        Personagens.MisionPVP mision = perso.getMisionPVP();
                        mision.setPjMision(victima);
                        mision.setKamasRecompensa(0);
                    }
                    perso.setTiempoAgre(tiempo);
                    victima.setTiempoAgre(tiempo);
                    return;
                }
                catch (NullPointerException tiempo) {
                    // empty catch block
                }
                return;
            }
            case 53: {
                try {
                    String objetodar = this._args.split(";")[0];
                    String objetopedir = this._args.split(";")[1];
                    World.Trueque trueque = new World.Trueque(perso, objetopedir, objetodar);
                    perso.setTrueque(trueque);
                    SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(out, 9, "");
                    return;
                }
                catch (Exception objetodar) {
                    // empty catch block
                }
                return;
            }
            case 54: {
                try {
                    if (this._args.isEmpty()) {
                        return;
                    }
                    int args = Integer.parseInt(this._args);
                    if (args != 10 && args != 11 && args != 12 && args != 13 && args != 14 && args != 15) return;
                    perso.boostStat2(args);
                    return;
                }
                catch (Exception args) {
                    // empty catch block
                }
                return;
            }
            case 55: {
                try {
                    if (this._args.isEmpty()) {
                        return;
                    }
                    long precio = Long.parseLong(this._args.split(";")[0]);
                    int accion = Integer.parseInt(this._args.split(";")[1]);
                    String args = "";
                    if (this._args.split(";").length > 2) {
                        args = this._args.split(";")[2];
                    }
                    if (perso.getKamas() >= precio) {
                        perso.setKamas(perso.getKamas() - precio);
                        new Acao(accion, args, "").aplicar(perso, null, -1, (short)-1);
                        return;
                    } else {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "182");
                    }
                    return;
                }
                catch (Exception precio) {
                    // empty catch block
                }
                return;
            }
            case 56: {
                try {
                    String quitar = this._args.split(";")[0];
                    String agregar = this._args.split(";")[1];
                    int objModelo = Integer.parseInt(quitar.split(",")[0]);
                    int cant = Integer.parseInt(quitar.split(",")[1]);
                    int objNuevo = Integer.parseInt(agregar.split(",")[0]);
                    int cantNuevo = Integer.parseInt(agregar.split(",")[1]);
                    if (perso.tieneObjModeloNoEquip(objModelo, cant)) {
                        perso.removerObjetoPorModYCant(objModelo, cant);
                        Objeto nuevoObj = World.getObjModelo(objNuevo).crearObjDesdeModelo(cantNuevo, false);
                        if (!perso.addObjetoSimilar(nuevoObj, true, -1)) {
                            World.addObjeto(nuevoObj, true);
                            perso.addObjetoPut(nuevoObj);
                            SocketManager.ENVIAR_OAKO_APARECER_OBJETO(out, nuevoObj);
                        }
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "022;" + cant + "~" + objModelo);
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "021;" + cantNuevo + "~" + objNuevo);
                        return;
                    } else {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "134|43");
                    }
                    return;
                }
                catch (Exception quitar) {
                    // empty catch block
                }
                return;
            }
            case 57: {
                try {
                    SocketManager.ENVIAR_AlE_CAMBIAR_NOMBRE(out, "r");
                    perso.cambiarNombre(true);
                    return;
                }
                catch (Exception quitar) {
                    // empty catch block
                }
                return;
            }
            case 58: {
                try {
                    String quitar = this._args.split(";")[0];
                    String[] azar = this._args.split(";")[1].split("\\|");
                    int id = Integer.parseInt(quitar.split(",")[0]);
                    int cant = Integer.parseInt(quitar.split(",")[1]);
                    if (cant < 0) {
                        perso.removerObjetoPorModYCant(id, -cant);
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "022;" + -cant + "~" + id);
                    }
                    String[] arrstring = azar;
                    int n = azar.length;
                    for (int i = 0; i < n; ++i) {
                        String objetoazar = arrstring[i];
                        int ID = Integer.parseInt(objetoazar.split(",")[0]);
                        int cantidad = Integer.parseInt(objetoazar.split(",")[1]);
                        boolean enviar = true;
                        if (objetoazar.split(",").length > 2) {
                            enviar = objetoazar.split(",")[2].equals("1");
                        }
                        if (cantidad > 0) {
                            Objeto.ObjetoModelo OM = World.getObjModelo(ID);
                            if (OM == null) {
                                return;
                            }
                            Objeto obj = OM.crearObjDesdeModelo(cantidad, false);
                            if (!perso.addObjetoSimilar(obj, true, -1)) {
                                World.addObjeto(obj, true);
                                perso.addObjetoPut(obj);
                                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(out, obj);
                            }
                        } else {
                            perso.removerObjetoPorModYCant(ID, -cantidad);
                        }
                        if (!perso.enLinea() || !enviar) continue;
                        if (cantidad >= 0) {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "021;" + cantidad + "~" + ID);
                            continue;
                        }
                        if (cantidad >= 0) continue;
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "022;" + -cantidad + "~" + ID);
                    }
                    SocketManager.ENVIAR_Ow_PODS_DEL_PJ(perso);
                    return;
                }
                catch (Exception quitar) {
                    // empty catch block
                }
                return;
            }
            case 59: {
                try {
                    int idSet = Integer.parseInt(this._args.split(";")[0]);
                    int fichas = Integer.parseInt(this._args.split(";")[1]);
                    World.ObjetoSet OS = World.getObjetoSet(idSet);
                    if (OS == null) {
                        return;
                    }
                    if (perso.tieneObjModeloNoEquip(1749, fichas)) {
                        perso.removerObjetoPorModYCant(1749, fichas);
                        for (Objeto.ObjetoModelo objM : OS.getObjetosModelos()) {
                            Objeto obj = objM.crearObjDesdeModelo(1, false);
                            if (!objetivo.addObjetoSimilar(obj, true, -1)) {
                                World.addObjeto(obj, true);
                                objetivo.addObjetoPut(obj);
                                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(objetivo, obj);
                            }
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "021;1~" + objM.getID());
                        }
                    } else {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "134|43");
                    }
                    SocketManager.ENVIAR_Ow_PODS_DEL_PJ(objetivo);
                    return;
                }
                catch (Exception idSet) {
                    // empty catch block
                }
                return;
            }
            case 60: {
                try {
                    if (!objetivo.esTumba()) return;
                    objetivo.revivir();
                    return;
                }
                catch (Exception idSet) {
                    // empty catch block
                }
                return;
            }
            case 61: {
                try {
                    if (!objetivo.esTumba()) return;
                    objetivo.volverseFantasma();
                    return;
                }
                catch (Exception idSet) {
                    // empty catch block
                }
                return;
            }
            case 100: {
                if (perso.getMontura() == null) {
                    return;
                }
                int habilidad = 0;
                habilidad = this._args.split(",").length == 2 ? Fórmulas.getRandomValor(Integer.parseInt(this._args.split(",")[0]), Integer.parseInt(this._args.split(",")[1])) : Integer.parseInt(this._args);
                Dragossauros montura = perso.getMontura();
                montura.setHabilidad(String.valueOf(habilidad));
                perso.setMontura(montura);
                SocketManager.ENVIAR_Re_DETALLES_MONTURA(perso, "+", World.getDragopavoPorID(montura.getID()));
                SQLManager.REPLACE_MONTURA(montura, false);
                return;
            }
            case 101: {
                if (perso.getSexo() == 0 && perso.getCelda().getID() == 282 || perso.getSexo() == 1 && perso.getCelda().getID() == 297) {
                    World.addEsposo(perso.getSexo(), perso);
                    return;
                } else {
                    SocketManager.ENVIAR_Im_INFORMACION(perso, "1102");
                }
                return;
            }
            case 102: {
                World.discursoSacerdote(perso, perso.getMapa(), perso.getConversandoCon());
                return;
            }
            case 103: {
                if (perso.getKamas() < 50000L) {
                    return;
                }
                perso.setKamas(perso.getKamas() - 50000L);
                Personagens esposo = World.getPersonaje(perso.getEsposo());
                esposo.divorciar();
                perso.divorciar();
                return;
            }
            case 200: {
                try {
                    Maps mapa = perso.getMapa();
                    int idModelo = objUsar.getModelo().getID();
                    if (mapa.getCercado() == null) {
                        return;
                    }
                    Maps.Cercado cercado = mapa.getCercado();
                    if (!perso.getNombre().equalsIgnoreCase("Elbusta")) {
                        if (perso.getGremio() == null) {
                            SocketManager.ENVIAR_BN_NADA(perso);
                            return;
                        }
                        if (!perso.getMiembroGremio().puede(8192)) {
                            SocketManager.ENVIAR_Im_INFORMACION(perso, "193");
                            return;
                        }
                        if (cercado.getCeldasObj().size() == 0 || !cercado.getCeldasObj().contains(celda)) {
                            SocketManager.ENVIAR_BN_NADA(perso);
                            return;
                        }
                    }
                    if (cercado.getCantObjColocados() < cercado.getCantObjMax()) {
                        cercado.addObjetoCria(celda, idModelo, perso.getID());
                        SocketManager.ENVIAR_GDO_PONER_OBJETO_CRIA(mapa, String.valueOf(celda) + ";" + idModelo + ";1;1000;1000");
                        perso.borrarObjetoEliminar(objUsadoID, 1, true);
                        return;
                    } else {
                        SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(perso, "Esse padoque j\u00e1 est\u00e1 no <b>limite</b> de itens de cria\u00e7\u00e3o.");
                    }
                    return;
                }
                catch (Exception mapa) {
                    // empty catch block
                }
                return;
            }
            case 201: {
                try {
                    short celdapj = perso.getCelda().getID();
                    Maps mapa = perso.getMapa();
                    World.SubArea subarea = mapa.getSubArea();
                    World.Area area = subarea.getArea();
                    byte alineacion = perso.getAlineacion();
                    if (celdapj <= 0) {
                        return;
                    }
                    if (perso.getDeshonor() >= 1) {
                        SocketManager.ENVIAR_Im_INFORMACION(out, "183");
                        return;
                    }
                    if (perso.getNivelAlineacion() < 3) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "155");
                        return;
                    }
                    if (alineacion == 0 || alineacion == 3) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "134|43");
                        return;
                    }
                    if (!perso.mostrarAlas()) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "1148");
                        return;
                    }
                    if (mapa.esArena() || mapa.esCasa() || mapa.esTaller() || mapa.getID() > 13000 || mapa.getAncho() != 15 && mapa.getAncho() != 19) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "1146");
                        return;
                    }
                    if (subarea.getAlineacion() != 0 || !subarea.getConquistable()) {
                        SocketManager.ENVIAR_Im_INFORMACION(perso, "1149");
                        return;
                    }
                    Prisma prisma = new Prisma(World.getSigIDPrisma(), (byte)alineacion, (byte)1, (short)mapa.getID(), (short)celdapj, 0, (short)-1);
                    subarea.setAlineacion(alineacion);
                    subarea.setPrismaID(prisma.getID());
                    for (Personagens z : World.getPJsEnLinea()) {
                        if (z.getAlineacion() == 0) {
                            SocketManager.ENVIAR_am_MENSAJE_ALINEACION_SUBAREA(z, String.valueOf(subarea.getID()) + "|" + alineacion + "|1");
                            if (area.getAlineacion() != 0) continue;
                            SocketManager.ENVIAR_aM_MENSAJE_ALINEACION_AREA(z, String.valueOf(area.getID()) + "|" + alineacion);
                            continue;
                        }
                        SocketManager.ENVIAR_am_MENSAJE_ALINEACION_SUBAREA(z, String.valueOf(subarea.getID()) + "|" + alineacion + "|0");
                        if (area.getAlineacion() != 0) continue;
                        SocketManager.ENVIAR_aM_MENSAJE_ALINEACION_AREA(z, String.valueOf(area.getID()) + "|" + alineacion);
                    }
                    if (area.getAlineacion() == 0) {
                        area.setPrismaID(prisma.getID());
                        area.setAlineacion(alineacion);
                        prisma.setAreaConquistada(area.getID());
                    }
                    World.addPrisma(prisma);
                    SQLManager.REPLACE_PRISMA(prisma);
                    SocketManager.ENVIAR_GM_PRISMA_A_MAPA(mapa, prisma);
                    return;
                }
                catch (Exception celdapj) {
                    // empty catch block
                }
                return;
            }
            case 228: {
                int animacionID = Integer.parseInt(this._args);
                Animacao animacion = World.getAnimacion(animacionID);
                if (perso.getPelea() != null) {
                    return;
                }
                if (perso.getMapa().getID() == LesGuardians.MAPA_LAG && LesGuardians.EXPULSAR) {
                    perso.getCuenta().getEntradaPersonaje().salir();
                    return;
                }
                perso.cambiarOrientacion((byte)1);
                SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(perso.getMapa(), "0", 228, String.valueOf(perso.getID()) + ";" + celda + "," + Animacao.preparaAGameAccion(animacion), "");
                return;
            }
            default: {
                System.out.println("Accion ID = " + this._ID + " no implantada");
            }
        }
    }

    public int getID() {
        return this._ID;
    }

    public String getArgs() {
        return this._args;
    }
}

