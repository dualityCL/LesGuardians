package common;

import java.io.Console;

import common.Fórmulas;
import common.LesGuardians;
import common.SocketManager;
import common.Mundo;

import game.GameServer;
import game.GameThread;

import objects.Mercadillo;
import objects.Objeto;
import objects.Personaje;

public class ConsolaPersonalización implements Runnable {
    private Thread _thread;
    private Personaje _perso;

    public ConsolaPersonalización() {
    	_thread = new Thread(this);
    	_thread.setDaemon(true);
    	_thread.start();
    }

    public void run() {
        while (LesGuardians._estaIniciado) {
          Console console = System.console();
          String command = console.readLine();
          try {
            Comandos(command);
          } catch (Exception exception) {
            continue;
          } finally {
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException interruptedException) {}
          } 
        } 
      }

    public void Comandos(String command) {
        String[] args = command.split(" ");
        String fct = args[0].toUpperCase();
        if (fct.equals("SALVAR")) {
            Thread t = new Thread(new GameServer.salvarServidorPersonaje());
            t.start();
        } else if (fct.equals("EXIT")) {
            System.exit(0);
        } else if (fct.equalsIgnoreCase("FULLHDV")) {
            int numero = 1;
            try {
                numero = Integer.parseInt(args[1]);
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.fullHdv(numero);
            return;
        }
        if (fct.equals("LIMPAR")) {
            LesGuardians.ReDofemuStarted();
        } else if (fct.equalsIgnoreCase("ANUNCIAR")) {
            String announce = command.substring(9);
            String PrefixConsole = "<b>Servidor</b> : ";
            SocketManager.ENVIAR_MENSAJE_A_TODOS_CHAT_COLOR(String.valueOf(PrefixConsole) + announce, LesGuardians.COR_MSG);
            ConsolaPersonalización.sendEcho("<Msg ALL:> " + announce);
        } else if (fct.equalsIgnoreCase("QUEMON")) {
            String str = "==========\nLista de Jogadores ON:";
            ConsolaPersonalización.sendInfo(str);
            int sobrantes = LesGuardians._servidorPersonaje.getClientes().size() - 50;
            for (int b = 0; b < 50; b = (int)((byte)(b + 1))) {
                if (b == LesGuardians._servidorPersonaje.getClientes().size()) break;
                GameThread EP = LesGuardians._servidorPersonaje.getClientes().get(b);
                Personaje P = EP.getPersonaje();
                if (P == null) continue;
                str = String.valueOf(P.getNombre()) + "(" + P.getID() + ") ";
                switch (P.getClase(true)) {
                    case 1: {
                        str = String.valueOf(str) + "Feca\t";
                        break;
                    }
                    case 2: {
                        str = String.valueOf(str) + "Osamodas\t";
                        break;
                    }
                    case 3: {
                        str = String.valueOf(str) + "Enutrof\t";
                        break;
                    }
                    case 4: {
                        str = String.valueOf(str) + "Sram\t";
                        break;
                    }
                    case 5: {
                        str = String.valueOf(str) + "Xelor\t";
                        break;
                    }
                    case 6: {
                        str = String.valueOf(str) + "Ecaflip\t";
                        break;
                    }
                    case 7: {
                        str = String.valueOf(str) + "Eniripsa\t";
                        break;
                    }
                    case 8: {
                        str = String.valueOf(str) + "Iop\t";
                        break;
                    }
                    case 9: {
                        str = String.valueOf(str) + "Cra\t";
                        break;
                    }
                    case 10: {
                        str = String.valueOf(str) + "Sadida\t";
                        break;
                    }
                    case 11: {
                        str = String.valueOf(str) + "Sacrier\t";
                        break;
                    }
                    case 12: {
                        str = String.valueOf(str) + "Pandawa\t";
                        break;
                    }
                    default: {
                        str = String.valueOf(str) + "Desconectado.";
                    }
                }
                str = String.valueOf(str) + " ";
                str = String.valueOf(str) + (P.getSexo() == 0 ? "M" : "F") + " ";
                str = String.valueOf(str) + P.getNivel() + " ";
                str = String.valueOf(str) + P.getMapa().getID() + "(" + P.getMapa().getX() + "/" + P.getMapa().getY() + ") ";
                str = String.valueOf(str) + (P.getPelea() == null ? "" : "Em luta. ");
                ConsolaPersonalización.sendInfo(str);
            }
            if (sobrantes > 0) {
                str = "E " + sobrantes + " outros jogadores.";
                ConsolaPersonalización.sendInfo(str);
            }
            ConsolaPersonalización.sendInfo("===============");
        } else if (fct.equalsIgnoreCase("TELEPORT")) {
            short mapaID = -1;
            short celdaID = -1;
            try {
                mapaID = Short.parseShort(args[1]);
                celdaID = Short.parseShort(args[2]);
            }
            catch (Exception b) {
                // empty catch block
            }
            if (mapaID == -1 || celdaID == -1 || Mundo.getMapa(mapaID) == null) {
                String str = "MapID ou CellID invalida";
                ConsolaPersonalización.sendEcho(str);
                return;
            }
            if (Mundo.getMapa(mapaID).getCelda(celdaID) == null) {
                String str = "MapID ou CellID invalida";
                ConsolaPersonalización.sendEcho(str);
                return;
            }
            Personaje objetivo2 = this._perso;
            if (!(args.length <= 3 || (objetivo2 = Mundo.getPjPorNombre(args[3])) != null && objetivo2.getPelea() == null && objetivo2.enLinea())) {
                String str = "O personagem n\u00e3o existe ou est\u00e1 em combate.";
                ConsolaPersonalización.sendEcho(str);
                return;
            }
            objetivo2.teleport(mapaID, celdaID);
            ConsolaPersonalización.sendEcho("O jogador " + objetivo2.getNombre() + " foi teleportado.");
        } else if (fct.equalsIgnoreCase("KICK")) {
            Personaje objetivo = this._perso;
            String nombre2 = null;
            try {
                nombre2 = args[1];
            }
            catch (Exception objetivo2) {
                // empty catch block
            }
            objetivo = Mundo.getPjPorNombre(nombre2);
            if (objetivo == null || !objetivo.enLinea()) {
                ConsolaPersonalización.sendEcho("O personagem n\u00e3o existe ou est\u00e1 em combate.");
                return;
            }
            if (objetivo.enLinea()) {
                objetivo.getCuenta().getEntradaPersonaje().salir();
                ConsolaPersonalización.sendEcho("Kick: " + objetivo.getNombre());
            }
        } else {
            if (fct.equalsIgnoreCase("HEROICO")) {
                String nombre = "";
                try {
                    nombre = args[1];
                }
                catch (Exception nombre2) {
                    // empty catch block
                }
                LesGuardians.MODO_HEROICO = nombre.equalsIgnoreCase("true");
                ConsolaPersonalización.sendEcho("Modo heroico: " + nombre.equalsIgnoreCase("true"));
                if (LesGuardians.MODO_HEROICO) {
                    SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS("Modo heroico foi ATIVADO.");
                } else {
                    SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS("Modo heroico foi DESATIVADO.");
                }
                return;
            }
            if (fct.equals("?") || command.equals("HELP")) {
                ConsolaPersonalización.sendInfo("------------Commandos:------------");
                ConsolaPersonalización.sendInfo("- EXIT = Reloga servidor.");
                ConsolaPersonalización.sendInfo("- SALVAR = Salva o servidor.");
                ConsolaPersonalización.sendInfo("- INFO = Mostra informacoes do servidor.");
                ConsolaPersonalización.sendInfo("- QUEMON = Mostra jogadores on.");
                ConsolaPersonalización.sendInfo("- HEROIC = Ativa/desativa modo heroico.");
                ConsolaPersonalización.sendInfo("- ANTIDDOS = Ativa/desativa anti ddos");
                ConsolaPersonalización.sendInfo("- FULLHDV = Ativa HDV.");
                ConsolaPersonalización.sendInfo("- LOCK = Abre o servidor.");
                ConsolaPersonalización.sendInfo("- BLOCK = Tranca o servidor...");
                ConsolaPersonalización.sendInfo("- KICK = Kicka um jogador.");
                ConsolaPersonalización.sendInfo("- LIMPAR = Limpa o console.");
                ConsolaPersonalización.sendInfo("- PRESENTEALL = Da presente para todos do servidor online.");
                ConsolaPersonalización.sendInfo("- TELEPORT = Teleporta um jogador.");
                ConsolaPersonalización.sendInfo("- ANUNCIAR = Mensagem para o servidor.");
                ConsolaPersonalización.sendInfo("----------------------------------");
            } else if (fct.equalsIgnoreCase("BLOCK")) {
                int accesoGM = 0;
                byte botarRango = 0;
                try {
                    accesoGM = Byte.parseByte(args[1]);
                    botarRango = Byte.parseByte(args[2]);
                }
                catch (Exception objetivo2) {
                    // empty catch block
                }
                Mundo.setGmAcceso((byte)accesoGM);
                ConsolaPersonalización.sendEcho("Server Bloqueado para GM : " + accesoGM);
                if (botarRango > 0) {
                    for (Personaje pj : Mundo.getPJsEnLinea()) {
                        if (pj.getCuenta().getRango() >= accesoGM) continue;
                        pj.getCuenta().getEntradaPersonaje().salir();
                    }
                    ConsolaPersonalización.sendEcho("Jogadores com GM LVL inferior a " + accesoGM + " foram expulsos !");
                }
            } else if (fct.equalsIgnoreCase("PRESENTEALL")) {
                int regalo = 0;
                try {
                    regalo = Integer.parseInt(args[1]);
                }
                catch (Exception botarRango) {
                    // empty catch block
                }
                for (Personaje pj : Mundo.getPJsEnLinea()) {
                    pj.getCuenta().setRegalo(regalo);
                }
                ConsolaPersonalización.sendEcho("O item " + regalo + " foi entregue a todos os jogadores online.");
            } else if (fct.equalsIgnoreCase("LOCK")) {
                byte estado = 1;
                try {
                    estado = Byte.parseByte(args[1]);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (estado > 2) {
                    estado = 2;
                }
                if (estado < 0) {
                    estado = 0;
                }
                Mundo.setEstado(estado);
                if (estado == 1) {
                    ConsolaPersonalización.sendEcho("Server Aberto.");
                } else if (estado == 0) {
                    ConsolaPersonalización.sendEcho("Server Fechado.");
                } else if (estado == 2) {
                    ConsolaPersonalización.sendEcho("Server Salvando.");
                }
            } else if (fct.equalsIgnoreCase("ANTIDDOS")) {
                boolean valor = false;
                try {
                    valor = args[1].equalsIgnoreCase("true");
                }
                catch (Exception exception) {
                    // empty catch block
                }
                LesGuardians.CONTRER_DDOS = valor;
                ConsolaPersonalización.sendEcho("Le contre ddos est : " + valor);
            } else if (fct.equals("INFO")) {
                long enLinea = System.currentTimeMillis() - LesGuardians._servidorPersonaje.getTiempoInicio();
                int dia = (int)(enLinea / 86400000L);
                int hora = (int)((enLinea %= 86400000L) / 3600000L);
                int minute = (int)((enLinea %= 3600000L) / 60000L);
                int segundo = (int)((enLinea %= 60000L) / 1000L);
                String str = "===========\nLES Guardians Emu 2.7.5\n\nTempo ON: " + dia + "D " + hora + "h " + minute + "m " + segundo + "s\n" + "Jogadores ON: " + LesGuardians._servidorPersonaje.nroJugadoresLinea() + "\n" + "Record ON: " + LesGuardians._servidorPersonaje.getRecordJugadores() + "\n" + "===========";
                ConsolaPersonalización.sendInfo(str);
            } else if (fct.equals("ECHO")) {
                try {
                    String message = command.substring(5);
                    ConsolaPersonalización.sendEcho(message);
                }
                catch (Exception exception) {}
            } else {
                ConsolaPersonalización.sendError("Comando nao reconhecido ou incompleto.");
            }
        }
    }

    public static void sendInfo(String msg) {
        Consola.println(msg, Consola.ConsoleColorEnum.GREEN);
    }

    public static void sendError(String msg) {
    	Consola.println(msg, Consola.ConsoleColorEnum.RED);
    }

    public static void send(String msg) {
    	Consola.println(msg);
    }

    public static void sendEcho(String msg) {
    	Consola.println(msg, Consola.ConsoleColorEnum.BLUE);
    }

    private void fullHdv(int deCadaModelo) {
        ConsolaPersonalización.sendEcho("Arranque de ofertas !");
        Objeto objeto = null;
        Mercadillo.ObjetoMercadillo objMercadillo = null;
        byte cantidad = 0;
        int hdv = 0;
        int lastSend = 0;
        long time1 = System.currentTimeMillis();
        for (Objeto.ObjetoModelo objModelo : Mundo.getObjModelos()) {
            try {
                if (LesGuardians.NAO_COMERCIALIZADOS_EN.contains(objModelo.getID())) continue;
                for (int j = 0; j < deCadaModelo; ++j) {
                    if (objModelo.getTipo() != 85 && (hdv = this.getHdv((objeto = objModelo.crearObjDesdeModelo(1, false)).getModelo().getTipo())) >= 0) {
                        cantidad = (byte)Fórmulas.getRandomValor(1, 3);
                        objMercadillo = new Mercadillo.ObjetoMercadillo(this.calcularPrecio(objeto, cantidad), cantidad, -1, objeto);
                        objeto.setCantidad(objMercadillo.getTipoCantidad(true));
                        Mundo.getPuestoMerca(hdv).addObjMercaAlPuesto(objMercadillo);
                        Mundo.addObjeto(objeto, false);
                        continue;
                    }
                    break;
                }
            }
            catch (Exception e) {
                continue;
            }
            if ((System.currentTimeMillis() - time1) / 1000L == (long)lastSend || (System.currentTimeMillis() - time1) / 1000L % 3L != 0L) continue;
            lastSend = (int)((System.currentTimeMillis() - time1) / 1000L);
            ConsolaPersonalización.sendEcho(String.valueOf((System.currentTimeMillis() - time1) / 1000L) + "sec modelo: " + objModelo.getID());
        }
        ConsolaPersonalización.sendEcho("Carregado em " + (System.currentTimeMillis() - time1) + "ms");
        SocketManager.ENVIAR_MENSAJE_A_TODOS_CHAT_COLOR("O HDV foi completado..", LesGuardians.COR_MSG);
    }

    private int getHdv(int tipo) {
        int rand = Fórmulas.getRandomValor(1, 4);
        int mapa = -1;
        switch (tipo) {
            case 12: 
            case 14: 
            case 26: 
            case 43: 
            case 44: 
            case 45: 
            case 66: 
            case 70: 
            case 71: 
            case 86: {
                mapa = rand == 1 ? 4271 : (rand == 2 ? 4607 : 7516);
                return mapa;
            }
            case 1: 
            case 9: {
                mapa = rand == 1 ? 4216 : (rand == 2 ? 4622 : 7514);
                return mapa;
            }
            case 18: 
            case 72: 
            case 77: 
            case 90: 
            case 97: 
            case 113: 
            case 116: {
                mapa = rand == 1 ? 8759 : 8753;
                return mapa;
            }
            case 63: 
            case 64: 
            case 69: {
                mapa = rand == 1 ? 4287 : (rand == 2 ? 4595 : (rand == 3 ? 7515 : 7350));
                return mapa;
            }
            case 33: 
            case 42: {
                mapa = rand == 1 ? 2221 : (rand == 2 ? 4630 : 7510);
                return mapa;
            }
            case 84: 
            case 93: 
            case 112: 
            case 114: {
                mapa = rand == 1 ? 4232 : (rand == 2 ? 4627 : 12262);
                return mapa;
            }
            case 38: 
            case 95: 
            case 96: 
            case 98: 
            case 108: {
                mapa = rand == 1 ? 4178 : (rand == 2 ? 5112 : 7289);
                return mapa;
            }
            case 10: 
            case 11: {
                mapa = rand == 1 ? 4183 : (rand == 2 ? 4562 : 7602);
                return mapa;
            }
            case 13: 
            case 25: 
            case 73: 
            case 75: 
            case 76: {
                mapa = rand == 1 ? 8760 : 8754;
                return mapa;
            }
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 19: 
            case 20: 
            case 21: 
            case 22: {
                mapa = rand == 1 ? 4098 : (rand == 2 ? 5317 : 7511);
                return mapa;
            }
            case 39: 
            case 40: 
            case 50: 
            case 51: 
            case 88: {
                mapa = rand == 1 ? 4179 : (rand == 2 ? 5311 : 7443);
                return mapa;
            }
            case 87: {
                mapa = rand == 1 ? 6159 : 6167;
                return mapa;
            }
            case 34: 
            case 52: 
            case 60: {
                mapa = rand == 1 ? 4299 : (rand == 2 ? 4629 : 7397);
                return mapa;
            }
            case 41: 
            case 49: 
            case 62: {
                mapa = rand == 1 ? 4247 : (rand == 2 ? 4615 : (rand == 3 ? 7501 : 7348));
                return mapa;
            }
            case 15: 
            case 35: 
            case 36: 
            case 46: 
            case 47: 
            case 48: 
            case 53: 
            case 54: 
            case 55: 
            case 56: 
            case 57: 
            case 58: 
            case 59: 
            case 65: 
            case 68: 
            case 103: 
            case 104: 
            case 105: 
            case 106: 
            case 107: 
            case 109: 
            case 110: 
            case 111: {
                mapa = rand == 1 ? 4262 : (rand == 2 ? 4646 : 7413);
                return mapa;
            }
            case 78: {
                mapa = rand == 1 ? 8757 : 8756;
                return mapa;
            }
            case 2: 
            case 3: 
            case 4: {
                mapa = rand == 1 ? 4174 : (rand == 2 ? 4618 : 7512);
                return mapa;
            }
            case 16: 
            case 17: 
            case 81: {
                mapa = rand == 1 ? 4172 : (rand == 2 ? 4588 : 7513);
                return mapa;
            }
            case 83: {
                mapa = rand == 1 ? 10129 : 8482;
                return mapa;
            }
            case 82: {
                return 8039;
            }
        }
        return -1;
    }

    private int calcularPrecio(Objeto obj, int cantidadLog) {
        int precio = (int)(Math.pow(10.0, cantidadLog) / 10.0);
        int stats = 0;
        for (int stat : obj.getStats().getStatsComoMap().values()) {
            stats += stat;
        }
        if (stats > 0) {
            return (int)((Math.cbrt(stats) * Math.pow(obj.getModelo().getNivel(), 2.0) * 10.0 + (double)Fórmulas.getRandomValor(1, obj.getModelo().getNivel() * 100)) * (double)precio);
        }
        return (int)((Math.pow(obj.getModelo().getNivel(), 2.0) * 10.0 + (double)Fórmulas.getRandomValor(1, obj.getModelo().getNivel() * 100)) * (double)precio);
    }
}

