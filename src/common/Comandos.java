package common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Timer;

import common.Constantes;
import common.CryptManager;
import common.Fórmulas;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.Mundo;

import game.GameServer;
import game.GameThread;

import objects.Acción;
import objects.Recaudador;
import objects.Cuenta;
import objects.Dragopavo;
import objects.Combate;
import objects.MobModelo;
import objects.MobModelo.GrupoMobs;
import objects.Mapa;
import objects.Mercadillo;
import objects.NPCModelo;
import objects.Objeto;
import objects.Objeto.ObjetoModelo;
import objects.Personaje;
import objects.Prisma;
import objects.Oficio;
import objects.Hechizo;

public class Comandos {
	Timer _Timer;
	Cuenta _cuenta;
	PrintWriter _out;
	Timer _resetRates;
	Personaje _perso;
	private boolean _tiempoIniciado = false;

	private Timer tiempoResetRates(int tiempo) {
		ActionListener acci\u00f3n = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				LesGuardians.resetRates();
				SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS("O tempo da RATE expirou, resetando as rates novamente para padr\u00e3o.");
				_resetRates.stop();
			}
		};
		return new Timer(tiempo * 60000, acci\u00f3n);
	}

	private Timer tiempoParaResetear(int tiempo) {
		ActionListener action = new ActionListener() {
			int Time = tiempo;

			public void actionPerformed(ActionEvent event) {
				Time = Time - 1;
				if (Time == 1) {
					SocketManager.ENVIAR_Im_INFORMACION_A_TODOS("115;" + Time + " minuto");
				} else {
					SocketManager.ENVIAR_Im_INFORMACION_A_TODOS("115;" + Time + " minutos");
				}
				if (Time <= 0) {
					System.exit(0);
				}
			}
		};
		return new Timer(60000, action);
	}

	public Comandos(Personaje pj) {
		try {
			_cuenta = pj.getCuenta();
			_perso = pj;
			_out = _cuenta.getEntradaPersonaje().getOut();
		} catch (NullPointerException nullPointerException) {
		}
	}

	public void consolaComando(String packet) {
		String msg = packet.substring(2);
		String[] infos = msg.split(" ");
		if (infos.length == 0) {
			return;
		}
		String comamdo = infos[0];
		if (_cuenta.getRango() == 0) {
			GM_lvl_0(comamdo, infos, msg);
			SQLManager.AGREGAR_COMANDO_GM(_perso.getNombre(), packet);
		} else if (_cuenta.getRango() == 1) {
			GM_lvl_1(comamdo, infos, msg);
			SQLManager.AGREGAR_COMANDO_GM(_perso.getNombre(), packet);
		} else if (_cuenta.getRango() == 2) {
			GM_lvl_2(comamdo, infos, msg);
			SQLManager.AGREGAR_COMANDO_GM(_perso.getNombre(), packet);
		} else if (_cuenta.getRango() == 3) {
			GM_lvl_3(comamdo, infos, msg);
			SQLManager.AGREGAR_COMANDO_GM(_perso.getNombre(), packet);
		} else if (_cuenta.getRango() == 4) {
			GM_lvl_4(comamdo, infos, msg);
			SQLManager.AGREGAR_COMANDO_GM(_perso.getNombre(), packet);
		} else if (_cuenta.getRango() >= 5) {
			GM_lvl_5(comamdo, infos, msg);
		}
	}

	/*
	 * WARNING - void declaration
	 */
	public void GM_lvl_1(String comando, String[] infos, String mensaje) {
		if (_cuenta.getRango() < 1) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_perso,
					"Les Guardians Emu 2.7.5: Voc\u00ea n\u00e3o tem o rank necess\u00e1rio.");
			return;
		}
		if (comando.equalsIgnoreCase("HELP")) {
			String mess1 = "Voc\u00ea atualmente tem o Rank GM " + _cuenta.getRango()
					+ ". --- Comandos Disponiveis:<br />";
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, mess1);
			if (_cuenta.getRango() == 1) {
				String mess = "<u>Info</u> - Mostra informacoes do servidor\n<u>Mapinfo</u> - Mostra NPCs e Monstros do Mapa\n<u>Who</u> - Jogadores Online\n<u>Criarguilda + [nick]</u> -  Cria uma guilda\n<u>Demorph + [Nick]</u> - Volta a aparencia default\n<u>Energia + [Valor] + [Nick]</u> -  Adiciona energia em um personagem.\n<u>Irjog + [Nick]</u> - Se teleporta em um jogador.\n<u>Tamanho + [Tamanho] + [Nick]</u> - Troca tamanho do personagem.\n<u>Morph + [MorphID] + [Nick]</u> - Troca aparencia\n<u>Trazerjog + [Nick]</u> - Traz um jogador.\n<u>Teleport [mapID] [cellID]</u> - Teleporta para um mapa.\n";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, mess);
				return;
			}
			if (_cuenta.getRango() == 2) {
				String mess = "<u>Infos</u> - Affiche l'uptime + informations diverses\n<u>Mapinfo</u> - Mostra NPCs e Monstros do Mapa\n<u>Who</u> - Jogadores Online\n<u>Criarguilda + [nick]</u> -  Cria uma guilda\n<u>Demorph + [Nick]</u> - Volta a aparencia default\n<u>Energia + [Valor] + [Nick]</u> -  Adiciona energia em um personagem.\n<u>Irjog + [Nick]</u> - Se teleporta em um jogador.\n<u>Tamanho + [Tamanho] + [Nick]</u> - Troca tamanho do personagem.\n<u>Morph + [MorphID] + [Nick]</u> - Troca aparencia\n<u>Trazerjog + [Nick]</u> - Traz um jogador.\n<u>Teleport [mapID] [cellID]</u> - Teleporta para um mapa.\n<u>Itemset + [setID] (+MAX?)</u> -  Cria um SET Novo.\n<u>Item + [itemID] + [Numero] (+MAX?)</u> -  Cria um ITEM\n<u>Honra + [numero] + [Nick]</u> -  Adiciona honra a um jogador.\n";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, mess);
				return;
			}
			if (_cuenta.getRango() == 3) {
				String mess = "<u>Infos</u> - Affiche l'uptime + informations diverses\n<u>Mapinfo</u> - Mostra NPCs e Monstros do Mapa\n<u>Who</u> - Jogadores Online\n<u>Criarguilda + [nick]</u> -  Cria uma guilda\n<u>Demorph + [Nick]</u> - Volta a aparencia default\n<u>Energia + [Valor] + [Nick]</u> -  Adiciona energia em um personagem.\n<u>Irjog + [Nick]</u> - Se teleporta em um jogador.\n<u>Tamanho + [Tamanho] + [Nick]</u> - Troca tamanho do personagem.\n<u>Morph + [MorphID] + [Nick]</u> - Troca aparencia\n<u>Trazerjog + [Nick]</u> - Traz um jogador.\n<u>Teleport [mapID] [cellID]</u> - Teleporta para um mapa.\n<u>Itemset + [setID] (+MAX?)</u> -  Cria um SET Novo.\n<u>Item + [itemID] + [Numero] (+MAX?)</u> -  Cria um ITEM\n<u>Honra + [numero] + [Nick]</u> -  Adiciona honra a um jogador.\n";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, mess);
				return;
			}
			if (_cuenta.getRango() == 4) {
				String mess = "<u>Infos</u> - Affiche l'uptime + informations diverses\n<u>Mapinfo</u> - Mostra NPCs e Monstros do Mapa\n<u>Who</u> - Jogadores Online\n<u>Criarguilda + [nick]</u> -  Cria uma guilda\n<u>Demorph + [Nick]</u> - Volta a aparencia default\n<u>Energia + [Valor] + [Nick]</u> -  Adiciona energia em um personagem.\n<u>Irjog + [Nick]</u> - Se teleporta em um jogador.\n<u>Tamanho + [Tamanho] + [Nick]</u> - Troca tamanho do personagem.\n<u>Morph + [MorphID] + [Nick]</u> - Troca aparencia\n<u>Trazerjog + [Nick]</u> - Traz um jogador.\n<u>Teleport [mapID] [cellID]</u> - Teleporta para um mapa.\n<u>Itemset + [setID] (+MAX?)</u> -  Cria um SET Novo.\n<u>Item + [itemID] + [Numero] (+MAX?)</u> -  Cria um ITEM\n<u>Honra + [numero] + [Nick]</u> -  Adiciona honra a um jogador.\n<u>Spellpoint + [Valor] + [Nick]</u> -  Adiciona pontos em feitico.\n<u>Capital</u> -  Adiciona capital a um jogador.\n<u>Aprenderfeitico + [ID] + [Nick]</u> -  Aprende um feitico.\n";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, mess);
				return;
			}
			if (_cuenta.getRango() >= 5) {
				String mess = "<u>Infos</u> - Affiche l'uptime + informations diverses\n<u>Refreshmobs</u> - Rafra\u00eechis tous les monstres sur la carte\n<u>Mapinfo</u> - Mostra NPCs e Monstros do Mapa\n<u>Who</u> - Jogadores Online\n<u>Criarguilda + [nick]</u> -  Cria uma guilda\n<u>Demorph + [Nick]</u> - Volta a aparencia default\n<u>Energia + [Valor] + [Nick]</u> -  Adiciona energia em um personagem.\n<u>Irjog + [Nick]</u> - Se teleporta em um jogador.\n<u>Tamanho + [Tamanho] + [Nick]</u> - Troca tamanho do personagem.\n<u>Morph + [MorphID] + [Nick]</u> - Troca aparencia\n<u>Trazerjog + [Nick]</u> - Traz um jogador.\n<u>Teleport [mapID] [cellID]</u> - Teleporta para um mapa.\n<u>Itemset + [setID] (+MAX?)</u> -  Cria um SET Novo.\n<u>Item + [itemID] + [Numero] (+MAX?)</u> -  Cria um ITEM\n<u>Honra + [numero] + [Nick]</u> -  Adiciona honra a um jogador.\n<u>Spellpoint + [Valor] + [Nick]</u> -  Adiciona pontos em feitico.\n<u>Capital</u> -  Adiciona capital a um jogador.\n<u>Aprenderfeitico + [ID] + [Nick]</u> -  Aprende um feitico.\n<u>Ban + [Nick]</u> - Bani um jogador.\n<u>Unban + [Pseudo]</u> -  Desbani um jogador.\n<u>banIP + [IP]</u> - Ban une IP\n<u>unbanIP + [IP]</u> - Desbani um IP\n<u>showbanIP</u> - Mostra IPs banidos.\n<u>Persoconta + [Nick]</u> -  Mostra personagens da conta.\n<u>Title + [Nick] + [TitleID]</u> - Adiciona title. \n<u>Heroico + [true] ou [false]</u> - Ativa/Desativa modo heroico. \n<u>Multiconta + [true] ou [false]</u> - Ativa/Desativa multi contas. \n<u>Exit</u> -  Iniciar um backup e servidor de recupera\u00e7\u00e3o.\n<u>Save</u> -  Salva o servidor.\n<u>Level + [Valor] + [Nick]</u> -  Adiciona LVL a um jogador.\n<u>Kamas + [Valor] + [Nick]</u> -  \n<u>Alinhamento + [1,2,3] + [Nick]</u> -  Adiciona alinhamento\n<u>Anuncio + [TEXTO]</u> -  Manda um anuncio no servidor.\n<u>Setmaxgroup [Nombre]</u> -  D\u00e9finir le nombre de monstres par groupe\n<u>AddReponseAction + [RepID + [ID] + [Arg]</u> -  Cria uma RespostaActionID\n<u>SetInitQuestion + [Type] + [ID] + [Arg]</u> -  Cria uma RespostaActionID\n<u>AddEndFightAction + [Type] + [ID] + [Arg]</u> -  Adiciona EndFight\n<u>Addnpc + [NpcID]</u> -  Adiciona um NPC\n<u>Delnpc + [NpcGuid]</u> -  Deleta um NPC\n<u>AddNpcItem + [NpcGuid] + [ItemID]</u> -  Ajouter un item \u00e0 un PNJ\n<u>DelNpcItem + [NpcGuid] + [ItemID]</u> -  Retirer un item \u00e0 un PNJ\n<u>Pdvper + [???%] + [Pseudo]</u> -  Choisir le pourcentage de points de vie\n<u>Aprenderprof + [JobID] + [Pseudo]</u> -  Adiciona uma profiss\u00e3o.\n<u>AddJobXp + [JobID] + [Valor] + [Nick]</u> -  Adiciona EXP de PROF.\n<u>SetReponses + [QuestionID] + [RepsID]</u> -  Cria uma RespostaID\n<u>ShowReponses + [QuestionID]</u> -  Mostra RespostasID\n<u>MoveNpc + [NpcGuid]</u> -  Mover o NPC (DB ATT Automatica)\n<u>AddFightPos + [0,1]</u> -  Adiciona uma celula de luta.\n<u>DelFightPos + [0,1]</u> -  Deleta uma celula de luta.\n<u>AddTrigger + [ActionID] + [Args]</u> -  Adiciona um trigger.\n<u>DelTrigger + [mapID] + [cellID]</u> -  Deleta um trigger.\n<u>Spawn [GroupID] [LevelMin] [LevelMax]</u> -  Cria um MOB N\u00c3O FIXO\n<u>Spawnfix [GroupeID] [LevelMin] [LevelMax]</u> -  Cria um MOB FIXO.\n<u>Showfightpos</u> -  Mostra celulas de luta\n<u>Shutdown</u> -  Programa um auto relogamento.\n<u>Setadmin [GmLvl] [Nick]</u> -  Modifica LVL GM\n<u>Presente + [ID] + [Nick]</u> - Adiciona um presente\n<u>PresenteALL + [ID]</u> - Adiciona um presente para a pr\u00f3xima conex\u00e3o de todos os personagens\n<u>MP + [Nick] + [Mensagem]</u> - Envia PM\n<u>RANKPVP + [ID]</u> - Atualiza o Rank PvP\n<u>Enviar + [Packet]</u> - Envia um Packet\n";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, mess);
				return;
			}
		} else {
			if (comando.equalsIgnoreCase("IRJOG") || comando.equalsIgnoreCase("GONAME")) {
				if (infos.length == 1) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Indique o nome do personagem.");
					return;
				}
				Personaje objetivo = Mundo.getPjPorNombre(infos[1]);
				if (objetivo == null || !objetivo.enLinea()) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
					return;
				}
				short mapaID = objetivo.getMapa().getID();
				short s = objetivo.getCelda().getID();
				Personaje objeto = _perso;
				if (infos.length > 2) {
					objeto = Mundo.getPjPorNombre(infos[2]);
					if (objetivo == null || !objetivo.enLinea()) {
						String str = "O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.";
						SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
						return;
					}
				}
				if (objeto.getPelea() != null) {
					String str = "O personagem est\u00e1 em luta.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				objeto.teleport(mapaID, s);
				String str = "O jogador foi teleportado";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			if (comando.equalsIgnoreCase("CRIARGUILDA")) {
				Personaje pj = _perso;
				if (infos.length > 1) {
					pj = Mundo.getPjPorNombre(infos[1]);
				}
				if (pj == null || !pj.enLinea()) {
					String str = "O personagem " + infos[1] + " n\u00e3o existe, ou n\u00e3o est\u00e1 conectado.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				if (pj.getGremio() != null || pj.getMiembroGremio() != null) {
					String str = "O personagem " + pj.getNombre() + " j\u00e1 possui uma guilda.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				SocketManager.ENVIAR_gn_CREAR_GREMIO(pj);
				String str = String.valueOf(pj.getNombre()) + ": Painel de cria\u00e7\u00e3o aberto.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			if (comando.equalsIgnoreCase("TAMANHO")) {
				int talla = -1;
				try {
					talla = Integer.parseInt(infos[1]);
				} catch (Exception str) {
					// empty catch block
				}
				if (talla == -1) {
					String str = "Tamanho invalido.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				Personaje objetivo = _perso;
				if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
					String string = "O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, string);
					return;
				}
				objetivo.setTalla(talla);
				SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(objetivo.getMapa(), objetivo);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O tamanho do jogador " + objetivo.getNombre() + " foi modificado.");
				return;
			}
			if (comando.equalsIgnoreCase("MORPH")) {
				int idGfx = -1;
				try {
					idGfx = Integer.parseInt(infos[1]);
				} catch (Exception objetivo) {
					// empty catch block
				}
				if (idGfx == -1) {
					String str = "Morph invalida.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				Personaje objetivo = _perso;
				if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
					String string = "O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, string);
					return;
				}
				objetivo.setGfxID(idGfx);
				SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(objetivo.getMapa(), objetivo);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem " + objetivo.getNombre() + " trocou de aparencia.");
				return;
			}
			if (comando.equalsIgnoreCase("INFO")) {
				long enLinea = System.currentTimeMillis() - LesGuardians._servidorPersonaje.getTiempoInicio();
				int n = (int) (enLinea / 86400000L);
				int hora = (int) ((enLinea %= 86400000L) / 3600000L);
				int minute = (int) ((enLinea %= 3600000L) / 60000L);
				int segundo = (int) ((enLinea %= 60000L) / 1000L);
				String str = "===========\nLES Guardians Emu 2.7.5\n\nDias ON: " + n + "d " + hora + "h " + minute
						+ "m " + segundo + "s\n" + "Jogadores ON: "
						+ LesGuardians._servidorPersonaje.nroJugadoresLinea() + "\n" + "Record ON: "
						+ LesGuardians._servidorPersonaje.getRecordJugadores() + "\n" + "===========";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			if (comando.equalsIgnoreCase("delallfightcell")) {
				_perso.getMapa().setPosicionesDePelea("|");
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "As posi\u00e7\u00f5es foram apagadas.");
				return;
			}
			if (comando.equalsIgnoreCase("delfightpos")) {
				String c;
				int a;
				short celda = -1;
				try {
					celda = Short.parseShort(infos[2]);
				} catch (Exception objetivo) {
					// empty catch block
				}
				if (celda < 0 || _perso.getMapa().getCelda(celda) == null) {
					celda = _perso.getCelda().getID();
				}
				String lugares = _perso.getMapa().getPosicionesPelea();
				String[] arrstring = lugares.split("\\|");
				String nuevasPosiciones = "";
				String equipo0 = "";
				String equipo1 = "";
				try {
					equipo0 = arrstring[0];
				} catch (Exception str) {
					// empty catch block
				}
				try {
					equipo1 = arrstring[1];
				} catch (Exception str) {
					// empty catch block
				}
				for (a = 0; a <= equipo0.length() - 2; a += 2) {
					c = arrstring[0].substring(a, a + 2);
					if (celda == CryptManager.celdaCodigoAID(c))
						continue;
					nuevasPosiciones = String.valueOf(nuevasPosiciones) + c;
				}
				nuevasPosiciones = String.valueOf(nuevasPosiciones) + "|";
				for (a = 0; a <= equipo1.length() - 2; a += 2) {
					c = arrstring[1].substring(a, a + 2);
					if (celda == CryptManager.celdaCodigoAID(c))
						continue;
					nuevasPosiciones = String.valueOf(nuevasPosiciones) + c;
				}
				_perso.getMapa().setPosicionesDePelea(nuevasPosiciones);
				if (!SQLManager.UPDATE_MAPA_POSPELEA_NROGRUPO(_perso.getMapa())) {
					return;
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"As posi\u00e7\u00f5es foram modificadas. (" + nuevasPosiciones + ")");
				return;
			}
			if (comando.equalsIgnoreCase("addfightpos")) {
				int a;
				int equipo = -1;
				short celda = -1;
				try {
					equipo = Integer.parseInt(infos[1]);
					celda = Short.parseShort(infos[2]);
				} catch (Exception exception) {
					// empty catch block
				}
				if (equipo < 0 || equipo > 1) {
					String string = "Equipe ou Cell ID invalido";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, string);
					return;
				}
				if (celda < 0 || _perso.getMapa().getCelda(celda) == null
						|| !_perso.getMapa().getCelda(celda).esCaminable(true)) {
					celda = _perso.getCelda().getID();
				}
				String string = _perso.getMapa().getPosicionesPelea();
				String[] p = string.split("\\|");
				boolean listo = false;
				String equipo02 = "";
				String equipo1 = "";
				try {
					equipo02 = p[0];
				} catch (Exception c) {
					// empty catch block
				}
				try {
					equipo1 = p[1];
				} catch (Exception c) {
					// empty catch block
				}
				for (a = 0; a <= equipo02.length() - 2; a += 2) {
					if (celda != CryptManager.celdaCodigoAID(equipo02.substring(a, a + 2)))
						continue;
					listo = true;
				}
				for (a = 0; a <= equipo1.length() - 2; a += 2) {
					if (celda != CryptManager.celdaCodigoAID(equipo1.substring(a, a + 2)))
						continue;
					listo = true;
				}
				if (listo) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A c\u00e9lula j\u00e1 est\u00e1 em uma lista.");
					return;
				}
				if (equipo == 0) {
					equipo02 = String.valueOf(equipo02) + CryptManager.celdaIDACodigo(celda);
				} else if (equipo == 1) {
					equipo1 = String.valueOf(equipo1) + CryptManager.celdaIDACodigo(celda);
				}
				String nuevosLugares = String.valueOf(equipo02) + "|" + equipo1;
				_perso.getMapa().setPosicionesDePelea(nuevosLugares);
				if (!SQLManager.UPDATE_MAPA_POSPELEA_NROGRUPO(_perso.getMapa())) {
					return;
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"A posi\u00e7\u00e3o das c\u00e9lulas foram modificadas. (" + nuevosLugares + ")");
				return;
			}
			if (comando.equalsIgnoreCase("mapinfo")) {
				String str = "==========\nLista de NPCs:";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				Mapa mapa = _perso.getMapa();
				for (Map.Entry<Integer, NPCModelo.NPC> entry : mapa.getNPCs().entrySet()) {
					NPCModelo.NPC npc = entry.getValue();
					str = "ID: " + entry.getKey() + " - Template: " + npc.getModeloBD().getID() + " - Nome: "
							+ npc.getModeloBD().getNombre() + " - Cell: " + npc.getCeldaID() + " - Pergunta: "
							+ npc.getModeloBD().getPreguntaID();
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				}
				str = "Lista de MOBS no Mapa:";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				for (Entry<Integer, GrupoMobs> entry : mapa.getMobGroups().entrySet()) {
					str = "ID: " + entry.getKey() + " - Cell ID: "
							+ ((MobModelo.GrupoMobs) entry.getValue()).getCeldaID() + " - StringMob: "
							+ ((MobModelo.GrupoMobs) entry.getValue()).getStrGrupoMob() + " - Quantidade: "
							+ ((MobModelo.GrupoMobs) entry.getValue()).getCantMobs();
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "===============");
				return;
			}
			if (comando.equalsIgnoreCase("QUIENES")) {
				String str = "==========\nLista de los jugadores en línea:";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				int sobrantes = LesGuardians._servidorPersonaje.getClientes().size() - 50;
				for (byte b = 0; b < 50; b = (byte) (b + 1)) {
					if (b == LesGuardians._servidorPersonaje.getClientes().size())
						break;
					GameThread EP = (GameThread) LesGuardians._servidorPersonaje.getClientes().get(b);
					Personaje P = EP.getPersonaje();
					if (P == null)
						continue;
					str = P.getNombre() + "(" + P.getID() + ") ";
					switch (P.getClase(true)) {
					case 1:
						str += "Feca\t";
						break;
					case 2:
						str += "Osamoda\t";
						break;
					case 3:
						str += "Anutrof\t";
						break;
					case 4:
						str += "Sram\t";
						break;
					case 5:
						str += "Xelor\t";
						break;
					case 6:
						str += "Zurcarak\t";
						break;
					case 7:
						str += "Aniripsa\t";
						break;
					case 8:
						str += "Yopuka\t";
						break;
					case 9:
						str += "Ocra\t";
						break;
					case 10:
						str += "Sadida\t";
						break;
					case 11:
						str += "Sacrogito\t";
						break;
					case 12:
						str += "Pandawa\t";
						break;
					default:
						str += "Desconocido";
					}
					str += " ";
					str += (P.getSexo() == 0 ? "M" : "F") + " ";
					str += P.getNivel() + " ";
					str += P.getMapa().getID() + "(" + P.getMapa().getX() + "/" + P.getMapa().getY() + ") ";
					str += (P.getPelea() == null ? "" : "Combate ");
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				}
				if (sobrantes > 0) {
					str = "Y " + sobrantes + " personajes más";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "===============");
				return;
			}
			if (comando.equalsIgnoreCase("SHOWFIGHTPOS")) {
				String str = "Lista de los lugares de pelea [EquipoID] : [CeldaID]";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				String lugares = _perso.getMapa().getPosicionesPelea();
				if ((lugares.indexOf('|') == -1) || (lugares.length() < 2)) {
					str = "Los lugares de pelea no estan definidos";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				String equipo0 = "";
				String equipo1 = "";
				String[] p = lugares.split("\\|");
				try {
					equipo0 = p[0];
				} catch (Exception localException6) {
				}
				try {
					equipo1 = p[1];
				} catch (Exception localException7) {
				}
				str = "Equipo 0:\n";
				for (int a = 0; a <= equipo0.length() - 2; a += 2) {
					String codigo = equipo0.substring(a, a + 2);
					str += CryptManager.celdaCodigoAID(codigo) + ",";
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				str = "Equipo 1:\n";
				for (int a = 0; a <= equipo1.length() - 2; a += 2) {
					String codigo = equipo1.substring(a, a + 2);
					str += CryptManager.celdaCodigoAID(codigo) + ",";
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			if (comando.equalsIgnoreCase("demorph")) {
				Personaje objetivo = _perso;
				if (!(infos.length <= 1 || (objetivo = Mundo.getPjPorNombre(infos[1])) != null
						&& objetivo.getPelea() == null && objetivo.enLinea())) {
					String str = "O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				objetivo.deformar();
				SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(objetivo.getMapa(), objetivo.getID());
				SocketManager.ENVIAR_GM_AGREGAR_PJ_A_TODOS(objetivo.getMapa(), objetivo);
				String str = "O jogador " + objetivo.getNombre() + " se destransformou.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			if (comando.equalsIgnoreCase("trazerjog")) {
				Personaje objetivo = null;
				try {
					objetivo = Mundo.getPjPorNombre(infos[1]);
				} catch (ArrayIndexOutOfBoundsException e) {
					String string = "Comando invalido.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, string);
					return;
				}
				if (objetivo == null || !objetivo.enLinea()) {
					String str = "Personagem n\u00e3o existe ou invalido.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				if (objetivo.getPelea() != null) {
					String str = "Personagem em luta.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				Personaje donde = _perso;
				if (infos.length > 2 && (donde = Mundo.getPjPorNombre(infos[2])) == null) {
					String string = "Personagem n\u00e3o existe.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, string);
					return;
				}
				if (donde.enLinea()) {
					short s = donde.getMapa().getID();
					short celdaID = donde.getCelda().getID();
					objetivo.teleport(s, celdaID);
					String str = "O jogador foi teleportado.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				} else {
					String string = "O jogador n\u00e3o est\u00e1 online.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, string);
				}
				return;
			}
			if (comando.equalsIgnoreCase("ANUNCIO")) {
				try {
					infos = mensaje.split(" ", 2);
					if (infos.length < 2) {
						SocketManager.ENVIAR_BAT2_CONSOLA(_out, "ERROR : Mensagem n\u00e3o completa.");
						return;
					}
					SocketManager
							.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS("<b>[" + _perso.getNombre() + "] </b> " + infos[1]);
				} catch (Exception objetivo) {
					// empty catch block
				}
				return;
			}
			if (comando.equalsIgnoreCase("TELEPORT")) {
				short mapaID = -1;
				short celdaID = -1;
				try {
					mapaID = Short.parseShort(infos[1]);
					celdaID = Short.parseShort(infos[2]);
				} catch (Exception localException9) {
				}
				if ((mapaID == -1) || (celdaID == -1) || (Mundo.getMapa(mapaID) == null)) {
					String str = "MapaID o celdaID inválido";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				if (Mundo.getMapa(mapaID).getCelda(celdaID) == null) {
					String str = "MapaID o celdaID inválido";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				Personaje objetivo = _perso;
				if (infos.length > 3) {
					objetivo = Mundo.getPjPorNombre(infos[3]);
					if ((objetivo == null) || (!objetivo.enLinea())) {
						String str = "El personaje no existe o no esta en línea ";
						SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
						return;
					}
				}
				if (objetivo.getPelea() != null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "El personaje esta en combate");
					return;
				}
				if (objetivo.getHaciendoTrabajo() != null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "El personaje esta haciendo un trabajo");
					return;
				}
				if (objetivo.getTutorial() != null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "El personaje esta en un tutorial");
					return;
				}
				if (!objetivo.getHuir()) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "El personaje no puede huir de una pelea PVP");
					return;
				}
				objetivo.teleport(mapaID, celdaID);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"El jugador " + objetivo.getNombre() + " ha sido teletransportado");
				return;
			}
			if (comando.equalsIgnoreCase("IRMAPA")) {
				int mapaX = 0;
				int mapaY = 0;
				short celdaID = 0;
				int contID = 0;
				try {
					mapaX = Integer.parseInt(infos[1]);
					mapaY = Integer.parseInt(infos[2]);
					celdaID = Short.parseShort(infos[3]);
					contID = Integer.parseInt(infos[4]);
				} catch (Exception localException10) {
				}
				Mapa mapa = Mundo.mapaPorCoordXYContinente(mapaX, mapaY, contID);
				if (mapa == null) {
					String str = "Posicion o continente inválido";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				if (mapa.getCelda(celdaID) == null) {
					String str = "CeldaID inválido";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				Personaje objetivo = _perso;
				if (infos.length > 5) {
					objetivo = Mundo.getPjPorNombre(infos[5]);
					if ((objetivo == null) || (!objetivo.enLinea())) {
						String str = "El personaje no existe o no esta conectado";
						SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
						return;
					}
					if (objetivo.getPelea() != null) {
						String str = "El personaje esta en combate";
						SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
						return;
					}
				}
				objetivo.teleport(mapa.getID(), celdaID);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"El jugador " + objetivo.getNombre() + " ha sido teletransportado");
				return;
			}
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Comando no reconocido");
		}
	}

	public void GM_lvl_2(String comando, String[] infos, String mensaje) {
		if (_cuenta.getRango() < 2) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_perso,
					"Les Guardians Emu 2.7.5: Voc\u00ea n\u00e3o tem o rank necess\u00e1rio.");
			return;
		}
		if (comando.equalsIgnoreCase("HONRA")) {
			int honor = 0;
			try {
				honor = Integer.parseInt(infos[1]);
			} catch (Exception exception) {
				// empty catch block
			}
			Personaje objetivo = _perso;
			if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			if (objetivo.getAlineacion() == -1) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem n\u00e3o est\u00e1 conectado.");
				return;
			}
			objetivo.addHonor(honor);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Honra : " + honor + " para o jogador " + objetivo.getNombre());
			return;
		}
		if (comando.equalsIgnoreCase("EXPULSAR")) {
			int mapa = -1;
			try {
				mapa = Short.parseShort(infos[1]);
			} catch (Exception objetivo) {
				// empty catch block
			}
			if (mapa == -1) {
				return;
			}
			LesGuardians.EXPULSAR = Integer.parseInt(infos[2]) == 1;
			LesGuardians.MAPA_LAG = (short) mapa;
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"O jogador foi teleportado para: " + mapa + " " + LesGuardians.EXPULSAR);
			return;
		}
		if (comando.equalsIgnoreCase("MAPAPOS")) {
			int x = -1;
			int y = -1;
			try {
				x = Integer.parseInt(infos[1]);
				y = Integer.parseInt(infos[2]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			String str = Mundo.mapaPorCoordenadas(x, y);
			if (str.isEmpty()) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "N\u00e3o possui nenhum MapID, com essas coordenadas.");
			} else {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"As coordenadas para o MapID s\u00e3o:  X: " + x + " Y: " + y + " e " + str);
			}
			return;
		}
		if ((comando.equalsIgnoreCase("ITEM")) || (comando.equalsIgnoreCase("!getitem"))) {
			boolean esPorConsole = comando.equalsIgnoreCase("!getitem");
			int idModelo = 0;
			try {
				idModelo = Integer.parseInt(infos[1]);
			} catch (Exception localException26) {}
			if (idModelo == 0) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "El objeto modelo " + idModelo + " es incorrecto");
				return;
			}
			if (((idModelo >= 10800) || (idModelo == 10657) || (idModelo == 10275)) && (_cuenta.getRango() < 5)) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "No posees el nivel de GM requerido");
				return;
			}
			int cant = 1;
			if (infos.length >= 3)
				try {
					cant = Integer.parseInt(infos[2]);
				} catch (Exception localException27) {}
			Personaje objetivo = _perso;
			if (infos.length >= 4) {
				String nombre = infos[3];
				objetivo = Mundo.getPjPorNombre(nombre);
				if ((objetivo == null) || (!objetivo.enLinea()))
					objetivo = _perso;
			}
			boolean useMax = false;
			if ((infos.length == 5) && (!esPorConsole) && (infos[4].equalsIgnoreCase("MAX"))) {
				useMax = true;
			}
			ObjetoModelo OM = Mundo.getObjModelo(idModelo);
			if (OM == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "El modelo " + idModelo + " no existe ");
				return;
			}
			if (cant < 1)
				cant = 1;
			Objeto obj = OM.crearObjDesdeModelo(cant, useMax);
			if (!objetivo.addObjetoSimilar(obj, true, -1)) {
				Mundo.addObjeto(obj, true);
				objetivo.addObjetoPut(obj);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(objetivo, obj);
			}
			String str = "Creación del objeto " + idModelo + " " + OM.getNombre() + " con cantidad " + cant + " a "
			+ objetivo.getNombre();
			if (useMax)
				str += " con stats máximos";
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
			SocketManager.ENVIAR_Ow_PODS_DEL_PJ(objetivo);
			return;
		}
		if (comando.equalsIgnoreCase("CAPITAL")) {
			int pts = -1;
			try {
				pts = Integer.parseInt(infos[1]);
			} catch (Exception idModelo) {
				// empty catch block
			}
			if (pts == -1) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			Personaje objetivo = _perso;
			if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			objetivo.addCapital(pts);
			SocketManager.ENVIAR_As_STATS_DEL_PJ(objetivo);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"O personagem " + objetivo.getNombre() + " recebeu " + pts + " pontos de capital.");
			return;
		}
		if (comando.equalsIgnoreCase("KAMAS")) {
			long cantidad = 0;
			try {
				cantidad = Long.parseLong(infos[1]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor incorrecto");
				return;
			}
			if (cantidad == 0)
				return;
			Personaje objetivo = _perso;
			if (infos.length == 3) {
				String nombre = infos[2];
				objetivo = Mundo.getPjPorNombre(nombre);
				if (objetivo == null)
					objetivo = _perso;
			}
			long kamasTemp = objetivo.getKamas();
			long nuevasKamas = kamasTemp + cantidad;
			if (nuevasKamas < 0L)
				nuevasKamas = 0L;
			if (nuevasKamas > 2000000000L)
				nuevasKamas = 2000000000L;
			objetivo.setKamas(nuevasKamas);
			if (objetivo.enLinea())
				SocketManager.ENVIAR_As_STATS_DEL_PJ(objetivo);
			String str = "Ha sido ";
			str += (cantidad < 0 ? "retirado" : "agregado") + " " + Math.abs(cantidad) + " kamas a " + objetivo.getNombre();
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
			return;
		}
		if (comando.equalsIgnoreCase("MAPAACIMA")) {
			byte y;
			byte x = _perso.getMapa().getX();
			String str = Mundo.mapaPorCoordenadas(x, y = (byte) (_perso.getMapa().getY() + 1));
			if (str.isEmpty()) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "N\u00e3o h\u00e1 MapID para essa coordenadas.");
			} else {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O MapID do mapa \u00e9 X: " + x + " Y: " + y + " son " + str);
			}
			return;
		}
		if (comando.equalsIgnoreCase("MAPAABAIXO")) {
			byte y;
			byte x = _perso.getMapa().getX();
			String str = Mundo.mapaPorCoordenadas(x, y = (byte) (_perso.getMapa().getY() - 1));
			if (str.isEmpty()) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "N\u00e3o h\u00e1 MapID para essa coordenadas.");
			} else {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O MapID do mapa \u00e9 X: " + x + " Y: " + y + " e " + str);
			}
			return;
		}
		if (comando.equalsIgnoreCase("MAPAESQUERDA")) {
			byte y;
			byte x = (byte) (_perso.getMapa().getX() - 1);
			String str = Mundo.mapaPorCoordenadas(x, y = _perso.getMapa().getY());
			if (str.isEmpty()) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "N\u00e3o h\u00e1 MapID para essa coordenadas.");
			} else {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O MapID do mapa \u00e9 X: " + x + " Y: " + y + " e " + str);
			}
			return;
		}
		if (comando.equalsIgnoreCase("MAPADIREITA")) {
			byte y;
			byte x = (byte) (_perso.getMapa().getX() + 1);
			String str = Mundo.mapaPorCoordenadas(x, y = _perso.getMapa().getY());
			if (str.isEmpty()) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "N\u00e3o h\u00e1 MapID para essa coordenadas.");
			} else {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O MapID do mapa \u00e9 X: " + x + " Y: " + y + " e " + str);
			}
			return;
		}
		if (comando.equalsIgnoreCase("ADDREPONSEACTION")) {
			infos = mensaje.split(" ", 4);
			int respuestaID = 0;
			int id = -30;
			String args = infos[3];
			try {
				respuestaID = Integer.parseInt(infos[1]);
				id = Integer.parseInt(infos[2]);
			} catch (Exception curKamas) {
				// empty catch block
			}
			NPCModelo.RespuestaNPC respuesta = Mundo.getRespuestaNPC(respuestaID);
			if (id == -30 || respuesta == null) {
				String str = "Um dos valores \u00e9 invalido.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			String str = "A action est\u00e1 invalida.";
			respuesta.addAccion(new Acción(id, args, ""));
			boolean ok = SQLManager.ACTUALIZA_NPC_RESPUESTAS(respuestaID, id, args);
			if (ok) {
				str = String.valueOf(str) + " e salva na DB.";
			}
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
			return;
		}
		if (comando.equalsIgnoreCase("SETINITQUESTION")) {
			infos = mensaje.split(" ", 4);
			int id = -30;
			int idPregunta = 0;
			try {
				id = Integer.parseInt(infos[1]);
				idPregunta = Integer.parseInt(infos[2]);
			} catch (Exception args) {
				// empty catch block
			}
			if (id == -30) {
				String str = "NpcID invalido";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			String str = "A action foi adicionada.";
			NPCModelo npc = Mundo.getNPCModelo(id);
			npc.setPreguntaID(idPregunta);
			boolean ok = SQLManager.UPDATE_PREGUNTA_NPC(id, idPregunta);
			if (ok) {
				str = String.valueOf(str) + " e salva na DB";
			}
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
			return;
		}
		if (comando.equalsIgnoreCase("SETREPONSES")) {
			if (infos.length < 3) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Faltando um dos argumentos");
				return;
			}
			int id = 0;
			try {
				id = Integer.parseInt(infos[1]);
			} catch (Exception idPregunta) {
				// empty catch block
			}
			String respuestas = infos[2];
			NPCModelo.PreguntaNPC pregunta = Mundo.getPreguntaNPC(id);
			String str = "";
			if (id == 0 || pregunta == null) {
				str = "QuestionID invalido";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			pregunta.setRespuestas(respuestas);
			boolean a = SQLManager.UPDATE_PREGUNTA_NPC(id, respuestas);
			str = "Lista de respostas da pergunta: " + id + ": " + pregunta.getRespuestas();
			if (a) {
				str = String.valueOf(str) + "(salva na DB)";
			}
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
			return;
		}
		if (comando.equalsIgnoreCase("SHOWREPONSES")) {
			int id = 0;
			try {
				id = Integer.parseInt(infos[1]);
			} catch (Exception respuestas) {
				// empty catch block
			}
			NPCModelo.PreguntaNPC pregunta = Mundo.getPreguntaNPC(id);
			String str = "";
			if (id == 0 || pregunta == null) {
				str = "PerguntaID invalido";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			str = "Lista de respostas da pergunta " + id + ": " + pregunta.getRespuestas();
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
			return;
		}
		if (comando.equalsIgnoreCase("ENCHERVIDA")) {
			int cantidad = 0;
			try {
				cantidad = Integer.parseInt(infos[1]);
				if (cantidad < 0)
					cantidad = 0;
				if (cantidad > 100)
					cantidad = 100;
				Personaje objetivo = _perso;
				if (infos.length == 3) {
					String nombre = infos[2];
					objetivo = Mundo.getPjPorNombre(nombre);
					if ((objetivo == null) || (!objetivo.enLinea()))
						objetivo = _perso;
				}
				int nuevaPDV = objetivo.getPDVMAX() * cantidad / 100;
				objetivo.setPDV(nuevaPDV);
				if (objetivo.enLinea())
					SocketManager.ENVIAR_As_STATS_DEL_PJ(objetivo);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Ha sido modificado el porcentaje de vida " + objetivo.getNombre()
				+ " a " + cantidad);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumentos incorrectos");
			}
			return;
		}
		GM_lvl_1(comando, infos, mensaje);
	}

	public void GM_lvl_3(String comando, String[] infos, String mensaje) {
		if (_cuenta.getRango() < 3) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_perso,
					"Les Guardians Emu 2.7.5: Voc\u00ea n\u00e3o tem o rank necess\u00e1rio.");
			return;
		}
		if (comando.equalsIgnoreCase("TRANSACOES")) {
			SQLManager.comenzarTransacciones();
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "As transa\u00e7\u00f5es est\u00e3o ativadas.");
			return;
		}
		if (comando.equalsIgnoreCase("INICIARSQL")) {
			SQLManager.comenzarTransacciones();
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "La m\u00e9thode commencer transaction est actif");
			return;
		}
		if (comando.equalsIgnoreCase("RESETARSQL")) {
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Iniciando fechamento da conex\u00e3o SQL.");
			SQLManager.cerrarConexion();
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Iniciando abertura da conex\u00e3o SQL.");
			SQLManager.iniciarConexion();
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Finalizado com sucesso a reconex\u00e3o SQL.");
		} else {
			if (comando.equalsIgnoreCase("SALVAR")) {
				Mundo.setEstado((byte) 1);
				LesGuardians._salvando = false;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Salvando...");
				return;
			}
			if (comando.equalsIgnoreCase("CHECKTURNO")) {
				Combate pelea = _perso.getPelea();
				if (pelea == null) {
					return;
				}
				pelea.tiempoTurno();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Verifica\u00e7\u00e3o de tempo de turno.");
				return;
			}
			if (comando.equalsIgnoreCase("QTTIA")) {
				Mapa mapa = _perso.getMapa();
				int objetos = 0;
				for (Mapa.Celda casilla : mapa.getCeldas().values()) {
					if (casilla.getObjetoInterac() == null)
						continue;
					++objetos;
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O mapa continente : " + objetos + " interativo.");
				return;
			}
			if (comando.equalsIgnoreCase("REFRESHIA")) {
				Mapa mapa = _perso.getMapa();
				int objetos = 0;
				String packet = "";
				boolean primero = true;
				for (Mapa.Celda casilla : mapa.getCeldas().values()) {
					if (casilla.getObjetoInterac() == null)
						continue;
					if (!primero) {
						packet = String.valueOf(packet) + "|";
					}
					Mapa.ObjetoInteractivo oi = casilla.getObjetoInterac();
					oi.setInteractivo(true);
					oi.setEstado(1);
					packet = String.valueOf(packet) + casilla.getID() + ";" + 1 + ";" + "1";
					++objetos;
					primero = false;
				}
				SocketManager.ENVIAR_GDF_FORZADO_MAPA(mapa, packet);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Recarregado : " + objetos + " objetos interativos.");
				return;
			}
			if (comando.equalsIgnoreCase("SETMAXGROUP")) {
				infos = mensaje.split(" ", 4);
				byte id = -1;
				try {
					id = Byte.parseByte(infos[1]);
				} catch (Exception objetos) {
					// empty catch block
				}
				if (id == -1) {
					String str = "Argumento invalido.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				String str = "O n\u00famero de grupo fixo \u00e9";
				_perso.getMapa().setMaxGrupoDeMobs(id);
				boolean ok = SQLManager.UPDATE_MAPA_POSPELEA_NROGRUPO(_perso.getMapa());
				if (ok) {
					str = String.valueOf(str) + " salvo na DB.";
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			if (comando.equalsIgnoreCase("UNBANIP")) {
				String ip = infos[1];
				Constantes.borrarIP(ip);
				SQLManager.DELETE_BANIP(ip);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O IP : " + ip + " foi removido da lista de BAN.");
				return;
			}
			if (comando.equalsIgnoreCase("exit")) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Fechando servidor ...");
				LesGuardians._cerrando = true;
				System.exit(0);
				return;
			}
			if (comando.equalsIgnoreCase("ADDDROP")) {
				int idMob = -1;
				int idObjMod = -1;
				int prospecc = -1;
				int porcentaje = -1;
				int m\u00e1ximo = -1;
				try {
					idMob = Integer.parseInt(infos[1]);
					idObjMod = Integer.parseInt(infos[2]);
					prospecc = Integer.parseInt(infos[3]);
					porcentaje = Integer.parseInt(infos[4]);
					m\u00e1ximo = Integer.parseInt(infos[5]);
				} catch (Exception e) {
					String str = "Erro monstro.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				MobModelo mob = Mundo.getMobModelo(idMob);
				Objeto.ObjetoModelo objModelo = Mundo.getObjModelo(idObjMod);
				if (mob == null || objModelo == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor null");
					return;
				}
				mob.addDrop(new Mundo.Drop(idObjMod, prospecc, porcentaje, m\u00e1ximo));
				SQLManager.INSERT_DROP(idMob, idObjMod, prospecc, m\u00e1ximo, porcentaje);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O objeto " + objModelo.getNombre() + " foi adicionado para ser dropado em: " + mob.getNombre()
								+ " com PP " + prospecc + " em %" + porcentaje);
				return;
			}
			if (comando.equalsIgnoreCase("SHOWDROP")) {
				int idMob = -1;
				try {
					idMob = Integer.parseInt(infos[1]);
				} catch (Exception e) {
					String str = "Erro monstro.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				MobModelo mob = Mundo.getMobModelo(idMob);
				if (mob == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor null");
					return;
				}
				String str = "";
				for (Mundo.Drop drop : mob.getDrops()) {
					Objeto.ObjetoModelo obj = Mundo.getObjModelo(drop.getObjetoID());
					if (obj == null)
						continue;
					str = String.valueOf(str) + obj.getNombre() + ", ";
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Lista de " + mob.getNombre() + " em: " + str);
				return;
			}
			if (comando.equalsIgnoreCase("DELDROP")) {
				int idObjMod = -1;
				try {
					idObjMod = Integer.parseInt(infos[1]);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Erro monstro.");
					return;
				}
				Objeto.ObjetoModelo objModelo = Mundo.getObjModelo(idObjMod);
				if (objModelo == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor null");
					return;
				}
				SQLManager.DELETE_DROP(idObjMod);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O objeto" + objModelo.getNombre() + " foi excluido da DB");
				return;
			}
			if (comando.equalsIgnoreCase("ADDENDFIGHTACTION")) {
				infos = mensaje.split(" ", 5);
				int tipo = 0;
				int acci\u00f3n = -30;
				String args = "";
				String cond = "";
				try {
					tipo = Integer.parseInt(infos[1]);
					acci\u00f3n = Integer.parseInt(infos[2]);
					args = infos[3];
					cond = infos[4];
				} catch (Exception m\u00e1ximo) {
					// empty catch block
				}
				if (acci\u00f3n == -30) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O valor da action est\u00e1 invalido.");
					return;
				}
				String str = "A action foi adicionada.";
				_perso.getMapa().addAccionFinPelea(tipo, new Acción(acci\u00f3n, args, cond));
				boolean ok = SQLManager.INSERT_FIN_ACCION_PELEA(_perso.getMapa().getID(), tipo, acci\u00f3n, args,
						cond);
				if (ok) {
					str = String.valueOf(str) + " e salva na DB.";
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			if (comando.equalsIgnoreCase("DELENDFIGHTACTION")) {
				_perso.getMapa().borrarTodoAcciones();
				SQLManager.DELETE_ACCION_PELEA(_perso.getMapa().getID());
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Action de case supprime");
				return;
			}
			if (comando.equalsIgnoreCase("spawnfix")) {
				String grupoData = infos[1];
				_perso.getMapa().addGrupoMobPermanente(_perso.getCelda().getID(), grupoData);
				String str = "Adicionado um grupo fixo : " + grupoData;
				if (SQLManager.SALVAR_NUEVO_GRUPOMOB(_perso.getMapa().getID(), _perso.getCelda().getID(), grupoData)) {
					str = String.valueOf(str) + ", salvo na DB";
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			if (comando.equalsIgnoreCase("ADDNPC")) {
				int idNPCModelo = 0;
				try {
					idNPCModelo = Integer.parseInt(infos[1]);
				} catch (Exception str) {
					// empty catch block
				}
				if (idNPCModelo == 0 || Mundo.getNPCModelo(idNPCModelo) == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "NpcID invalido");
					return;
				}
				NPCModelo.NPC npc = _perso.getMapa().addNPC(idNPCModelo, _perso.getCelda().getID(),
						_perso.getOrientacion());
				SocketManager.ENVIAR_GM_AGREGAR_NPC_AL_MAPA(_perso.getMapa(), npc);
				String str = "O NPC foi adicionado";
				if (_perso.getOrientacion() == 0 || _perso.getOrientacion() == 2 || _perso.getOrientacion() == 4
						|| _perso.getOrientacion() == 6) {
					str = String.valueOf(str) + " mas invisivel (orienta\u00e7\u00e3o diagonal invalido).";
				}
				if (SQLManager.INSERT_NPC_AL_MAPA(_perso.getMapa().getID(), idNPCModelo, _perso.getCelda().getID(),
						_perso.getOrientacion(), Mundo.getNPCModelo(idNPCModelo).getNombre())) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				} else {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Erro no momento do save na posi\u00e7\u00e3o.");
				}
				return;
			}
			if (comando.equalsIgnoreCase("MOVENPC")) {
				int id = 0;
				try {
					id = Integer.parseInt(infos[1]);
				} catch (Exception npc) {
					// empty catch block
				}
				NPCModelo.NPC npc = _perso.getMapa().getNPC(id);
				if (id == 0 || npc == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Npc GUID invalido");
					return;
				}
				short exC = npc.getCeldaID();
				SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_perso.getMapa(), id);
				npc.setCeldaID(_perso.getCelda().getID());
				npc.setOrientacion(_perso.getOrientacion());
				SocketManager.ENVIAR_GM_AGREGAR_NPC_AL_MAPA(_perso.getMapa(), npc);
				String str = "Posi\u00e7\u00e3o do NPC";
				if (_perso.getOrientacion() == 0 || _perso.getOrientacion() == 2 || _perso.getOrientacion() == 4
						|| _perso.getOrientacion() == 6) {
					str = String.valueOf(str) + " mas invisivel. (orienta\u00e7\u00e3o diagonal invalida).";
				}
				if (SQLManager.DELETE_NPC_DEL_MAPA(_perso.getMapa().getID(), exC) && SQLManager.INSERT_NPC_AL_MAPA(
						_perso.getMapa().getID(), npc.getModeloBD().getID(), _perso.getCelda().getID(),
						_perso.getOrientacion(), Mundo.getNPCModelo(id).getNombre())) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				} else {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Erro no momento de salvar a posi\u00e7\u00e3o.");
				}
				return;
			}
			if (comando.equalsIgnoreCase("DELNPC")) {
				int id = 0;
				try {
					id = Integer.parseInt(infos[1]);
				} catch (Exception npc) {
					// empty catch block
				}
				NPCModelo.NPC npc = _perso.getMapa().getNPC(id);
				if (id == 0 || npc == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Npc GUID invalido");
					return;
				}
				short exC = npc.getCeldaID();
				SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_perso.getMapa(), id);
				_perso.getMapa().borrarNPCoGrupoMob(id);
				if (SQLManager.DELETE_NPC_DEL_MAPA(_perso.getMapa().getID(), exC)) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O NPC foi deletado.");
				} else {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Erro no momento de salvar a posi\u00e7\u00e3o.");
				}
				return;
			}
			if (comando.equalsIgnoreCase("DELTRIGGER")) {
				short celdaID = -1;
				try {
					celdaID = Short.parseShort(infos[1]);
				} catch (Exception npc) {
					// empty catch block
				}
				Mapa.Celda celda = _perso.getMapa().getCelda(celdaID);
				if (celdaID == -1 || celda == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "CellID invalido");
					return;
				}
				celda.nullearCeldaAccion();
				boolean exito = SQLManager.DELETE_TRIGGER(_perso.getMapa().getID(), celdaID);
				if (exito) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O trigger da c\u00e9lula " + celdaID + " foi retirado.");
				} else {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O trigger n\u00e3o foi retirado.");
				}
				return;
			}
			if (comando.equalsIgnoreCase("ADDTRIGGER")) {
				int accionID = -1;
				String args = "";
				String cond = "";
				try {
					accionID = Integer.parseInt(infos[1]);
					args = infos[2];
					cond = infos[3];
				} catch (Exception str) {
					// empty catch block
				}
				if (args.equals("") || accionID <= -3) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
					return;
				}
				_perso.getCelda().addAccionEnUnaCelda(accionID, args, cond);
				boolean exito = SQLManager.REPLACE_TRIGGER(_perso.getMapa().getID(), _perso.getCelda().getID(),
						accionID, 1, args, cond);
				if (exito) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O trigger da c\u00e9lula " + _perso.getCelda().getID()
							+ " foi colocada a action " + accionID + " com os argumentos. " + args);
				} else {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O trigger n\u00e3o foi adicionado.");
				}
				return;
			}
			if (comando.equalsIgnoreCase("ADDITEMACTION")) {
				int idObjModelo = -1;
				int accionID = -1;
				String args = "";
				try {
					idObjModelo = Integer.parseInt(infos[1]);
					accionID = Integer.parseInt(infos[2]);
					args = infos[3];
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Erro de template.");
					return;
				}
				Objeto.ObjetoModelo objModelo = Mundo.getObjModelo(idObjModelo);
				if (args.equals("") || accionID <= -3 || objModelo == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor incorreto.");
					return;
				}
				Acción acci\u00f3n = new Acción(accionID, args, "");
				String nombre = objModelo.getNombre();
				objModelo.addAccion(acci\u00f3n);
				SQLManager.INSERT_ACCION_OBJETO(idObjModelo, accionID, args, nombre);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O objeto " + nombre + " foi adicionado a Action ID " + accionID + " com args " + args);
				return;
			}
			if (comando.equalsIgnoreCase("PORTAPADOQUE")) {
				short celda = -1;
				try {
					celda = Short.parseShort(infos[1]);
				} catch (Exception accionID) {
					// empty catch block
				}
				if (celda == -1) {
					return;
				}
				_perso.getMapa().getCercado().setPuerta(celda);
				SQLManager.UPDATE_PUERTA_CERCADO(_perso.getMapa().getID(), celda);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"A c\u00e9lula " + celda + " est\u00e1 bloqueada para montarias.");
				return;
			}
			if (comando.equalsIgnoreCase("CELLMONTARIA")) {
				short celda = -1;
				try {
					celda = _perso.getCelda().getID();
				} catch (Exception accionID) {
					// empty catch block
				}
				if (celda == -1) {
					return;
				}
				Mapa mapa = _perso.getMapa();
				if (mapa.getCercado() == null) {
					return;
				}
				Mapa.Cercado cercado = mapa.getCercado();
				cercado.addCeldaMontura(celda);
				short celdapuerta = (short) (cercado.getCeldaID() + (celda - cercado.getCeldaID()) / 2);
				cercado.setPuerta(celdapuerta);
				SQLManager.UPDATE_CELDA_MONTURA(mapa.getID(), celda);
				SQLManager.UPDATE_PUERTA_CERCADO(mapa.getID(), celdapuerta);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"A c\u00e9lula" + celdapuerta + " foi bloqueadas para montarias.");
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Foi desbloqueada a c\u00e9lula " + celda + " para montarias.");
				return;
			}
			if (comando.equalsIgnoreCase("TAMANHOPADOQUE")) {
				int tama\u00f1o = -1;
				int objetos = -1;
				try {
					tama\u00f1o = Integer.parseInt(infos[1]);
					objetos = Integer.parseInt(infos[2]);
				} catch (Exception cercado) {
					// empty catch block
				}
				if (tama\u00f1o == -1) {
					return;
				}
				Mapa mapa = _perso.getMapa();
				if (mapa.getCercado() == null) {
					return;
				}
				Mapa.Cercado cercado = mapa.getCercado();
				cercado.setSizeyObjetos(tama\u00f1o, objetos);
				SQLManager.UPDATE_MONTURAS_Y_OBJETOS(tama\u00f1o, objetos, mapa.getID());
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O tamanho do padoque \u00e9 de " + tama\u00f1o + " montarias e " + objetos + " objetos.");
				return;
			}
			if (comando.equalsIgnoreCase("DESBUGMAPA")) {
				short mapaID = -1;
				String nombre = "";
				try {
					mapaID = (short) Integer.parseInt(infos[1]);
					nombre = infos[2];
				} catch (Exception mapa) {
					// empty catch block
				}
				Personaje perso = Mundo.getPjPorNombre(nombre);
				Mapa mapa = Mundo.getMapa(mapaID);
				if (mapa == null || perso == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "El mapa es nulo, el personaje no existe");
					return;
				}
				int idPerso = perso.getID();
				mapa.borrarJugador(idPerso);
				SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapa, idPerso);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Se ha quitado el doble del personaje " + nombre + ".");
				return;
			}
			if (comando.equalsIgnoreCase("ANTIDDOS")) {
				boolean valor = false;
				try {
					valor = infos[1].equalsIgnoreCase("true");
				} catch (Exception nombre) {
					// empty catch block
				}
				LesGuardians.CONTRER_DDOS = valor;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O AntiDDOS est\u00e1 : " + valor);
				return;
			}
			if (comando.equalsIgnoreCase("BEMVINDO1")) {
				String nuevo = "";
				try {
					nuevo = mensaje.split(" ", 2)[1];
				} catch (Exception nombre) {
					// empty catch block
				}
				LesGuardians.MSG_BEM_VINDO_1 = nuevo;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Nova mensagem de Bem Vindo :\n" + nuevo);
				return;
			}
			if (comando.equalsIgnoreCase("CELLOBJETO")) {
				short celda = -1;
				try {
					celda = _perso.getCelda().getID();
				} catch (Exception nombre) {
					// empty catch block
				}
				if (celda == -1) {
					return;
				}
				Mapa mapa = _perso.getMapa();
				if (mapa.getCercado() == null) {
					return;
				}
				Mapa.Cercado cercado = mapa.getCercado();
				cercado.addCeldaObj(celda);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"A c\u00e9lula " + celda + " a ete ajoute pour les nourritures des dd");
				return;
			}
			if (comando.equalsIgnoreCase("CELULASPADOQUE")) {
				Mapa mapa = _perso.getMapa();
				if (mapa.getCercado() == null) {
					return;
				}
				Mapa.Cercado cercado = mapa.getCercado();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "C\u00e9lulas do Padoque: " + cercado.getStringCeldasObj());
				return;
			}
		}
		GM_lvl_2(comando, infos, mensaje);
	}

	public void GM_lvl_0(String comando, String[] infos, String mensaje) {
		if (_cuenta.getRango() < 0) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_perso, "VOUS N'AVEZ PAS LE RANG NECESSAIRE.");
			return;
		}
		if (comando.equalsIgnoreCase("HELP")) {
			String mess1 = "Voc\u00ea atualmente tem o Rank GM " + _cuenta.getRango()
					+ ". --- Comandos disponiveis.:<br />";
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, mess1);
			if (_cuenta.getRango() == 0) {
				String mess = "<u>Les Guardians Emu 2.7.5</u> -  Voc\u00ea n\u00e3o tem permiss\u00f5es.\n";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, mess);
				return;
			}
		} else {
			if (comando.equalsIgnoreCase("ENERGIA")) {
				short cantidad = 0;
				try {
					cantidad = Short.parseShort(infos[1]);
					Personaje objetivo = _perso;
					if (infos.length == 3) {
						String nombre = infos[2];
						objetivo = Mundo.getPjPorNombre(nombre);
						if ((objetivo == null) || (!objetivo.enLinea()))
							objetivo = _perso;
					}
					if (cantidad > 0)
						objetivo.agregarEnergia(cantidad);
					else
						objetivo.restarEnergia(-cantidad);
					String str = "Ha sido modificado la energía de " + objetivo.getNombre() + " a " + objetivo.getEnergia();
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumentos incorrectos");
					return;
				}
				return;
			}
			if (comando.equalsIgnoreCase("IRJOG") || comando.equalsIgnoreCase("GONAME")) {
				if (infos.length == 1) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Coloque o nome do jogador.");
					return;
				}
				Personaje objetivo = Mundo.getPjPorNombre(infos[1]);
				if (objetivo == null || !objetivo.enLinea()) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
					return;
				}
				short mapaID = objetivo.getMapa().getID();
				short celdaID = objetivo.getCelda().getID();
				Personaje objeto = _perso;
				if (infos.length > 2) {
					objeto = Mundo.getPjPorNombre(infos[2]);
					if (objetivo == null || !objetivo.enLinea()) {
						String str = "O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.";
						SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
						return;
					}
				}
				if (objeto.getPelea() != null) {
					String str = "O personagem est\u00e1 em luta.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				objeto.teleport(mapaID, celdaID);
				String str = "O jogador foi teleportado.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			if (comando.equalsIgnoreCase("learnjob")) {
				int oficio = -1;
				try {
					oficio = Integer.parseInt(infos[1]);
				} catch (Exception mapaID) {
					// empty catch block
				}
				if (oficio == -1 || Mundo.getOficio(oficio) == null) {
					String str = "Valor invalido.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				Personaje objetivo = _perso;
				if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
					String str = "O personagem n\u00e3o est\u00e1 conectado ou em luta.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				objetivo.aprenderOficio(Mundo.getOficio(oficio));
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem " + objetivo.getNombre() + " aprendeu a profiss\u00e3o.");
				return;
			}
			if (comando.equalsIgnoreCase("CRIARGUILDA")) {
				Personaje pj = _perso;
				if (infos.length > 1) {
					pj = Mundo.getPjPorNombre(infos[1]);
				}
				if (pj == null || !pj.enLinea()) {
					String str = "O personagem " + infos[1] + " n\u00e3o existe ou n\u00e3o est\u00e1 conectado.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				if (pj.getGremio() != null || pj.getMiembroGremio() != null) {
					String str = "O personagem " + pj.getNombre() + " recebeu o painel de cria\u00e7\u00e3o de guilda.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				SocketManager.ENVIAR_gn_CREAR_GREMIO(pj);
				String str = String.valueOf(pj.getNombre()) + ": Painel de cria\u00e7\u00e3o.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
		}
		SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Les Guardians Emu 2.7.5 - Comando n\u00e3o existe.");
	}

	public void GM_lvl_4(String comando, String[] infos, String mensaje) {
		if (_cuenta.getRango() < 4) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_perso,
					"Les Guardians Emu 2.7.5: Voc\u00ea n\u00e3o tem o rank necess\u00e1rio.");
			return;
		}
		if (comando.equalsIgnoreCase("SPAWN")) {
			String Mob = null;
			try {
				Mob = infos[1];
			} catch (Exception exception) {
				// empty catch block
			}
			if (Mob == null) {
				return;
			}
			_perso.getMapa().addGrupoMobSoloUnaPelea(_perso.getCelda().getID(), Mob);
			return;
		}
		if (comando.equalsIgnoreCase("itemset")) {
			Personaje objetivo = null;
			int id = 0;
			try {
				id = Integer.parseInt(infos[1]);
				String nombre = infos[2];
				objetivo = Mundo.getPjPorNombre(nombre);
			} catch (Exception nombre) {
				// empty catch block
			}
			if (objetivo == null || !objetivo.enLinea()) {
				objetivo = _perso;
			}
			Mundo.ObjetoSet OS = Mundo.getObjetoSet(id);
			if (id == 0 || OS == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O set " + id + " n\u00e3o existe.");
				return;
			}
			boolean useMax = false;
			if (infos.length >= 4) {
				useMax = infos[3].equals("MAX");
			}
			for (Objeto.ObjetoModelo OM : OS.getObjetosModelos()) {
				Objeto obj = OM.crearObjDesdeModelo(1, useMax);
				if (objetivo.addObjetoSimilar(obj, true, -1))
					continue;
				Mundo.addObjeto(obj, true);
				objetivo.addObjetoPut(obj);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(objetivo, obj);
			}
			String str = "Cria\u00e7\u00e3o do set " + id + " para " + objetivo.getNombre();
			if (useMax) {
				str = String.valueOf(str) + " Status MAX";
			}
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
			SocketManager.ENVIAR_Ow_PODS_DEL_PJ(objetivo);
			return;
		}
		if (comando.equalsIgnoreCase("DELNPCITEM")) {
			if (_cuenta.getRango() < 3) {
				return;
			}
			int npcID = 0;
			int objID = -1;
			NPCModelo npcMod = null;
			try {
				npcID = Integer.parseInt(infos[1]);
				objID = Integer.parseInt(infos[2]);
				npcMod = _perso.getMapa().getNPC(npcID).getModeloBD();
			} catch (Exception localException4) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			Objeto.ObjetoModelo item = Mundo.getObjModelo(objID);
			if (npcID == 0 || objID == -1 || npcMod == null || item == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "NPC ou ITEM incorreto.");
				return;
			}
			if (npcMod.borrarObjetoAVender(objID)) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O objeto " + item.getNombre() + " foi apagado.");
			} else {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O objeto " + item.getNombre() + " n\u00e3o foi apagado.");
			}
			return;
		}
		if (comando.equalsIgnoreCase("ADDNPCITEM")) {
			int npcID = 0;
			int objID = -1;
			try {
				npcID = Integer.parseInt(infos[1]);
				objID = Integer.parseInt(infos[2]);
			} catch (Exception npcMod) {
				// empty catch block
			}
			try {
				NPCModelo npc = _perso.getMapa().getNPC(npcID).getModeloBD();
				Objeto.ObjetoModelo item = Mundo.getObjModelo(objID);
				if (npc.addObjetoAVender(item)) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O item " + item.getNombre() + " foi adicionado.");
				} else {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O item " + item.getNombre() + " n\u00e3o foi adicionado.");
				}
			} catch (Exception ex) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "NpcID ou itemID invalido");
				return;
			}
			return;
		}
		if (comando.equalsIgnoreCase("CONSULTAPONTOS")) {
			String nombre = "";
			try {
				nombre = infos[1];
			} catch (Exception objID) {
				// empty catch block
			}
			Personaje consultado = Mundo.getPjPorNombre(nombre);
			if (consultado == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem n\u00e3o existe.");
				return;
			}
			try {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem" + nombre + " possui " + SQLManager.getPuntosCuenta(consultado.getCuentaID()));
			} catch (NullPointerException ex) {
				// empty catch block
			}
			return;
		}
		if (comando.equalsIgnoreCase("DESCRICAOOMAPA")) {
			int descrip = 0;
			try {
				descrip = Integer.parseInt(infos[1]);
			} catch (Exception consultado) {
				// empty catch block
			}
			_perso.getMapa().setDescripcion(descrip);
			SQLManager.UPDATE_MAPA_DESCRIPCION(_perso.getMapa().getID(), descrip);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A destri\u00e7\u00e3o do mapa \u00e9 " + descrip);
			return;
		}
		if (comando.equalsIgnoreCase("TROCARSENHA")) {
			String nombre = "";
			try {
				nombre = infos[1];
			} catch (Exception consultado) {
				// empty catch block
			}
			Cuenta consultado = Mundo.getCuentaPorNombre(nombre);
			if (consultado == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A conta n\u00e3o existe.");
				return;
			}
			String nueva = "";
			try {
				nueva = infos[2];
			} catch (Exception item) {
				// empty catch block
			}
			if (nueva.isEmpty()) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A senha n\u00e3o pode ser trocada.");
				return;
			}
			consultado.cambiarContrase\u00f1a(nueva);
			SQLManager.cambiarContrase\u00f1a(nueva, consultado.getID());
			try {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A conta " + nombre + " trocou a senha para: " + nueva);
			} catch (NullPointerException item) {
				// empty catch block
			}
			return;
		}
		if (comando.equalsIgnoreCase("PRESENTE")) {
			int regalo = 0;
			try {
				regalo = Integer.parseInt(infos[1]);
			} catch (Exception consultado) {
				// empty catch block
			}
			Personaje objetivo = _perso;
			if (infos.length > 2 && (objetivo = Mundo.getPjPorNombre(infos[2])) == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem n\u00e3o existe.");
				return;
			}
			objetivo.getCuenta().setRegalo(regalo);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"O presente " + regalo + " foi entregue para: " + objetivo.getNombre());
			return;
		}
		if (comando.equalsIgnoreCase("ALLPRESENTE")) {
			int regalo = 0;
			try {
				regalo = Integer.parseInt(infos[1]);
			} catch (Exception objetivo) {
				// empty catch block
			}
			for (Personaje pj : Mundo.getPJsEnLinea()) {
				pj.getCuenta().setRegalo(regalo);
			}
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O item " + regalo + " foi entregue para todos jogadores online.");
			return;
		}
		if (comando.equalsIgnoreCase("ALLOBJETOS")) {
			int regalo = 0;
			int cant2 = 0;
			try {
				regalo = Integer.parseInt(infos[1]);
				cant2 = Integer.parseInt(infos[2]);
			} catch (Exception nueva) {
				// empty catch block
			}
			Objeto.ObjetoModelo objMod = Mundo.getObjModelo(regalo);
			if (objMod == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Objeto null");
				return;
			}
			if (cant2 < 1) {
				cant2 = 1;
			}
			for (Personaje pj : Mundo.getPJsEnLinea()) {
				Objeto obj = objMod.crearObjDesdeModelo(cant2, false);
				try {
					if (pj.addObjetoSimilar(obj, true, -1))
						continue;
					Mundo.addObjeto(obj, true);
					pj.addObjetoPut(obj);
					SocketManager.ENVIAR_OAKO_APARECER_OBJETO(pj, obj);
				} catch (Exception exception) {
					// empty catch block
				}
			}
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea entregou " + objMod.getNombre() + " com a quantidade de "
					+ cant2 + " para todos jogadores online.");
			return;
		}
		if (comando.equalsIgnoreCase("LIMITEOFICINA")) {
			byte cant = 0;
			try {
				cant = Byte.parseByte(infos[1]);
			} catch (Exception cant2) {
				// empty catch block
			}
			LesGuardians.LIMITE_ARTESAOS_OFIC = cant;
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea limiteu em " + cant + " as oficinas.");
			return;
		}
		if (comando.equalsIgnoreCase("QUANTIDADEDROP")) {
			byte regalo = 0;
			try {
				regalo = Byte.parseByte(infos[1]);
			} catch (Exception cant2) {
				// empty catch block
			}
			LesGuardians.CANT_DROP = regalo;
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A quantidade de drops foi para: " + regalo);
			return;
		}
		if (comando.equalsIgnoreCase("ADDEXPPROF")) {
			int oficio = -1;
			int xp = -1;
			try {
				oficio = Integer.parseInt(infos[1]);
				xp = Integer.parseInt(infos[2]);
			} catch (Exception objMod) {
				// empty catch block
			}
			if (oficio == -1 || xp < 0) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			Personaje objetivo = _perso;
			if (!(infos.length <= 3 || (objetivo = Mundo.getPjPorNombre(infos[3])) != null && objetivo.enLinea())) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			Oficio.StatsOficio statsOficio = objetivo.getOficioPorID(oficio);
			if (statsOficio == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem n\u00e3o possui a prof indicada.");
				return;
			}
			statsOficio.addXP(objetivo, xp);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A profiss\u00e3o foi upada.");
			return;
		}
		if (comando.equalsIgnoreCase("spellpoint")) {
			int pts = -1;
			try {
				pts = Integer.parseInt(infos[1]);
			} catch (Exception xp) {
				// empty catch block
			}
			if (pts == -1) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			Personaje objetivo = _perso;
			if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			objetivo.addPuntosHechizos(pts);
			SocketManager.ENVIAR_As_STATS_DEL_PJ(objetivo);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"O personagem " + objetivo.getNombre() + " recebeu " + pts + " pontos em feiti\u00e7o.");
			return;
		}
		if (comando.equalsIgnoreCase("APRENDERFEITICO")) {
			int hechizo = -1;
			try {
				hechizo = Integer.parseInt(infos[1]);
			} catch (Exception objetivo) {
				// empty catch block
			}
			if (hechizo == -1) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			Personaje objetivo = _perso;
			if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			Hechizo aprender = Mundo.getHechizo(hechizo);
			if (aprender == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O feiti\u00e7o n\u00e3o existe.");
				return;
			}
			objetivo.aprenderHechizo(hechizo, 1, false, true);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"O personagem " + objetivo.getNombre() + " aprendeu o feiti\u00e7o: " + aprender.getNombre());
			return;
		}
		if (comando.equalsIgnoreCase("ADDACTION")) {
			if (infos.length < 4) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"Argumentos minimos invalido. Tente novamente com mais argumentos.");
				return;
			}
			int tipo = -100;
			String args = "";
			String cond = "";
			Personaje objetivo = _perso;
			try {
				objetivo = Mundo.getPjPorNombre(infos[1]);
				if (objetivo == null || !objetivo.enLinea()) {
					objetivo = _perso;
				}
				tipo = Integer.parseInt(infos[2]);
				args = infos[3];
				if (infos.length > 4) {
					cond = infos[4];
				}
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumentos incorretos.");
				return;
			}
			new Acción(tipo, args, cond).aplicar(objetivo, null, -1, (short) -1);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem " + objetivo.getNombre() + " fez a a\u00e7\u00e3o "
					+ tipo + " com os argumentos: " + args);
			return;
		}
		if (comando.equalsIgnoreCase("MOUNTFERTIL")) {
			if (_perso.getMontura() == null) {
				return;
			}
			Dragopavo pavo = _perso.getMontura();
			pavo.setAmor(7500);
			pavo.setResistencia(7500);
			pavo.setMaxEnergia();
			pavo.setMaxMadurez();
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A mount est\u00e1 fertil.");
			return;
		}
		if (comando.equalsIgnoreCase("BAN")) {
			Personaje objetivo = Mundo.getPjPorNombre(infos[1]);
			if (objetivo == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem n\u00e3o existe.");
				return;
			}
			if (objetivo.getCuenta() == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Impossivel banir o personagem.");
				return;
			}
			objetivo.getCuenta().setBaneado(true);
			SQLManager.SALVAR_CUENTA(objetivo.getCuenta());
			if (objetivo.getCuenta().getEntradaPersonaje() != null) {
				objetivo.getCuenta().getEntradaPersonaje().salir();
			}
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Banido : " + objetivo.getNombre());
			return;
		}
		if (comando.equalsIgnoreCase("ANUNCIAR")) {
			infos = mensaje.split(" ", 2);
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS(infos[1]);
			return;
		}
		if (comando.equalsIgnoreCase("DESBAN")) {
			Personaje objetivo = Mundo.getPjPorNombre(infos[1]);
			if (objetivo == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem n\u00e3o existe.");
				return;
			}
			if (objetivo.getCuenta() == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Impossivel desbanir o personagem.");
				return;
			}
			objetivo.getCuenta().setBaneado(false);
			SQLManager.SALVAR_CUENTA(objetivo.getCuenta());
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea desbaniu : " + objetivo.getNombre());
			return;
		}
		if (comando.equalsIgnoreCase("DESLIGAR")) {
			int tiempo = 30;
			int OffOn = 0;
			try {
				OffOn = Integer.parseInt(infos[1]);
				tiempo = Integer.parseInt(infos[2]);
			} catch (Exception cond) {
				// empty catch block
			}
			if (OffOn == 1 && _tiempoIniciado) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Shutdown programado.");
			} else if (OffOn == 1 && !_tiempoIniciado) {
				_Timer = tiempoParaResetear(tiempo);
				_Timer.start();
				_tiempoIniciado = true;
				String tiempoMSJ = "minutos";
				if (tiempo <= 1) {
					tiempoMSJ = "minuto";
				}
				SocketManager.ENVIAR_Im_INFORMACION_A_TODOS("115;" + tiempo + " " + tiempoMSJ);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Shutdown iniciado.");
			} else if (OffOn == 0 && _tiempoIniciado) {
				_Timer.stop();
				_tiempoIniciado = false;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Shutdown interrompido.");
			} else if (OffOn == 0 && !_tiempoIniciado) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Shutdown n\u00e3o foi interrompido.");
			}
			return;
		}
		if (comando.equalsIgnoreCase("RESETARRATES")) {
			int tiempo = 60;
			try {
				tiempo = Integer.parseInt(infos[1]);
				_resetRates = tiempoResetRates(tiempo);
				_resetRates.start();
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "N\u00e3o foi possivel executar o tempo.");
				return;
			}
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS(
					"Em " + tiempo + " minutos ser\u00e1 resetado as rates do evento."
							+ "\nO servidor voltar\u00e1 para as rates originais.");
			return;
		}
		if (comando.equalsIgnoreCase("RATEEVENTO")) {
			String cantidad = "";
			try {
				cantidad = infos[1];
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			LesGuardians.EVENTO = cantidad;
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS("O evento foi modificado para: " + cantidad);
			return;
		}
		if (comando.equalsIgnoreCase("RATETEMPOLUTA")) {
			int cantidad = 0;
			try {
				cantidad = Integer.parseInt(infos[1]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			LesGuardians.TIEMPO_PELEA = cantidad * 1000;
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS(
					"O tempo dos turnos foram modificados para: " + cantidad + " segundos");
			return;
		}
		if (comando.equalsIgnoreCase("RATETEMPOALIMENTACAO")) {
			byte cantidad = 0;
			try {
				cantidad = Byte.parseByte(infos[1]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			LesGuardians.RATE_TEMPO_ALIMENTACAO = cantidad;
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS(
					"Tempo de Alimenta\u00e7\u00e3o foi modificado para: " + cantidad);
			return;
		}
		if (comando.equalsIgnoreCase("RATEMOVERMOUNT")) {
			int cantidad = 0;
			try {
				cantidad = Integer.parseInt(infos[1]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			LesGuardians.TIEMPO_MOVERSE_PAVOS = cantidad;
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS(
					"O tempo para as mounts se mexerem foi modificada para: " + cantidad + " milisegundos");
			return;
		}
		if (comando.equalsIgnoreCase("RATETEMPOPARIR")) {
			byte cantidad = 0;
			try {
				cantidad = Byte.parseByte(infos[1]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			LesGuardians.RATE_TEMPO_NASCER = cantidad;
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS(
					"O tempo m\u00ednimo para as mounts nascerem foi modificado para: " + cantidad + " minuteo");
			return;
		}
		if (comando.equalsIgnoreCase("RATEKAMAS")) {
			float cantidad = 0.0f;
			try {
				cantidad = Float.parseFloat(infos[1]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor incorreto.");
				return;
			}
			LesGuardians.RATE_KAMAS = cantidad;
			SocketManager
					.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION_TODOS("A rate de Kamas foi modificada para: " + cantidad);
			return;
		}
		if (comando.equalsIgnoreCase("RATEDROP")) {
			byte cantidad = 0;
			try {
				cantidad = Byte.parseByte(infos[1]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor incorreto.");
				return;
			}
			LesGuardians.RATE_DROP = cantidad;
			SocketManager
					.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION_TODOS("A rate de Drops foi modificada para: " + cantidad);
			return;
		}
		if (comando.equalsIgnoreCase("RATEPVM")) {
			float cantidad = 0.0f;
			try {
				cantidad = Float.parseFloat(infos[1]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor incorreto.");
				return;
			}
			LesGuardians.RATE_XP_PVM = cantidad;
			SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION_TODOS("A rate EXP foi modificada para: " + cantidad);
			return;
		}
		if (comando.equalsIgnoreCase("RATEXPPVP")) {
			float cantidad = 0.0f;
			try {
				cantidad = Float.parseFloat(infos[1]);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor incorreto.");
				return;
			}
			LesGuardians.RATE_XP_PVP = cantidad;
			SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION_TODOS("A rate XP PVP foi modificada para: " + cantidad);
			return;
		}
		if (comando.equalsIgnoreCase("EVENTO")) {
			String cantidad = "";
			try {
				cantidad = infos[1];
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor incorreto.");
				return;
			}
			LesGuardians.EVENTO = cantidad;
			SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION_TODOS("Novo evento : " + cantidad);
		} else {
			if (comando.equalsIgnoreCase("ANUNCIAR2")) {
				try {
					infos = mensaje.split(" ", 2);
					if (infos.length < 2) {
						SocketManager.ENVIAR_BAT2_CONSOLA(_out, "ERROR : Mensagem n\u00e3o completa");
						return;
					}
					SocketManager.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION_TODOS(
							"<b>" + _perso.getNombre() + "</b> : " + infos[1]);
				} catch (Exception cantidad) {
					// empty catch block
				}
				return;
			}
			if (comando.equalsIgnoreCase("RATEPROF")) {
				float cantidad = 0.0f;
				try {
					cantidad = Float.parseFloat(infos[1]);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Valor incorreto.");
					return;
				}
				LesGuardians.RATE_XP_PROF = cantidad;
				SocketManager
						.ENVIAR_BAIO_MENSAJE_PANEL_INFORMACION_TODOS("A rate Prof foi modificada para: " + cantidad);
				return;
			}
			if (comando.equalsIgnoreCase("TOLERANCIAVIP")) {
				float cantidad = 0.0f;
				try {
					cantidad = Float.parseFloat(infos[1]);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
					return;
				}
				Oficio.AccionTrabajo._tolerVIP = cantidad;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"A tolerancia para VIP magiar foi modificada para: " + cantidad);
				return;
			}
			if (comando.equalsIgnoreCase("TOLERANCIANORMAL")) {
				float cantidad = 0.0f;
				try {
					cantidad = Float.parseFloat(infos[1]);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
					return;
				}
				Oficio.AccionTrabajo._tolerNormal = cantidad;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A tolerancia Normal foi modificada para: " + cantidad);
				return;
			}
			if (comando.equalsIgnoreCase("MULTICONTA")) {
				byte cantidad = 0;
				try {
					cantidad = Byte.parseByte(infos[1]);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
					return;
				}
				LesGuardians.MAX_MULTI_CONTAS = cantidad;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"A quantidade de contas pelo mesmo IP foi modificada para: " + cantidad);
				return;
			}
			if (comando.equalsIgnoreCase("ATIVARAURA")) {
				LesGuardians.AURA_ATIVADA = true;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A aura est\u00e1 ativada.");
				return;
			}
			if (comando.equalsIgnoreCase("DESATIVARAURA")) {
				LesGuardians.AURA_ATIVADA = false;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A aura est\u00e1 desativada.");
				return;
			}
			if (comando.equalsIgnoreCase("SPRITEID")) {
				int id = 0;
				try {
					id = Integer.parseInt(infos[1]);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento invalido.");
					return;
				}
				Hechizo hechizo = Mundo.getHechizo(id);
				if (hechizo == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Feiti\u00e7o n\u00e3o existe.");
					return;
				}
				int spriteID = 0;
				try {
					spriteID = Integer.parseInt(infos[2]);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
					return;
				}
				hechizo.setSpriteID(spriteID);
				SQLManager.ACTUALIZAR_SPRITEID_HECHIZO(id, spriteID);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O feiti\u00e7o " + hechizo.getNombre() + " trocou de Sprite para: " + spriteID);
				return;
			}
			if (comando.equalsIgnoreCase("SPRITEINFO")) {
				int id = 0;
				try {
					id = Integer.parseInt(infos[1]);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
					return;
				}
				Hechizo hechizo = Mundo.getHechizo(id);
				if (hechizo == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Feiti\u00e7o n\u00e3o existe.");
					return;
				}
				String spriteInfos = "";
				try {
					spriteInfos = infos[2];
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
					return;
				}
				hechizo.setSpriteInfos(spriteInfos);
				SQLManager.ACTUALIZAR_SPRITEINFO_HECHIZO(id, spriteInfos);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O feiti\u00e7o " + hechizo.getNombre() + " tem as informa\u00e7\u00f5es: " + spriteInfos);
				return;
			}
			if (comando.equalsIgnoreCase("VERIFICARIP")) {
				Personaje objetivo = null;
				try {
					objetivo = Mundo.getPjPorNombre(infos[1]);
				} catch (Exception hechizo) {
					// empty catch block
				}
				if (objetivo == null || !objetivo.enLinea()) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"Personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
					return;
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O IP atual do personagem " + objetivo.getNombre() + " \u00e9: "
						+ objetivo.getCuenta().getActualIP());
				return;
			}
			if (comando.equalsIgnoreCase("BANIP")) {
				Personaje objetivo = null;
				try {
					objetivo = Mundo.getPjPorNombre(infos[1]);
				} catch (Exception hechizo) {
					// empty catch block
				}
				if (objetivo == null || !objetivo.enLinea()) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
					return;
				}
				String ipBaneada = objetivo.getCuenta().getActualIP();
				if (!Constantes.compararConIPBaneadas(ipBaneada)) {
					Constantes.BAN_IP = String.valueOf(Constantes.BAN_IP) + "," + ipBaneada;
					if (SQLManager.INSERT_BANIP(ipBaneada)) {
						SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O IP : " + ipBaneada + " foi banido.");
					}
					if (objetivo.enLinea()) {
						objetivo.getCuenta().getEntradaPersonaje().salir();
						SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O jogador foi retirado");
					}
				} else {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O IP n\u00e3o existe.");
				}
				return;
			}
			if (comando.equalsIgnoreCase("MOVERCOLETOR")) {
				if (_perso.getPelea() != null) {
					return;
				}
				Recaudador recaudador = null;
				for (Recaudador perco : Mundo.getTodosRecaudadores().values()) {
					if (perco.getMapaID() != _perso.getMapa().getID())
						continue;
					recaudador = perco;
					break;
				}
				if (recaudador == null) {
					return;
				}
				recaudador.moverPerco();
				recaudador.setEnRecolecta(false);
				return;
			}
			if (comando.equalsIgnoreCase("FULLHDV")) {
				int numero = 1;
				try {
					numero = Integer.parseInt(infos[1]);
				} catch (Exception exception) {
					// empty catch block
				}
				fullHdv(numero);
				return;
			}
		}
		GM_lvl_3(comando, infos, mensaje);
	}

	public void GM_lvl_5(String comando, String[] infos, String mensaje) {
		if (_cuenta.getRango() < 5) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_perso,
					"LES GUARDIANS EMU 2.7.5 - Voc\u00ea n\u00e3o tem permiss\u00e3o.");
			return;
		}
		if (comando.equalsIgnoreCase("APRENDERPROF")) {
			int oficio = -1;
			try {
				oficio = Integer.parseInt(infos[1]);
			} catch (Exception exception) {
				// empty catch block
			}
			if (oficio == -1 || Mundo.getOficio(oficio) == null) {
				String str = "Valor invalido.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			Personaje objetivo = _perso;
			if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
				String str = "O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			objetivo.aprenderOficio(Mundo.getOficio(oficio));
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"O personagem " + objetivo.getNombre() + " aprendeu a profiss\u00e3o.");
			return;
		}
		if (comando.equalsIgnoreCase("ALINHAMENTO")) {
			byte alineacion = -1;
			try {
				alineacion = Byte.parseByte(infos[1]);
			} catch (Exception objetivo) {
				// empty catch block
			}
			if (alineacion < -1 || alineacion > 3) {
				String str = "Argumento incorreto.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			Personaje objetivo = _perso;
			if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
				String str = "O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			objetivo.modificarAlineamiento(alineacion);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"O personagem " + objetivo.getNombre() + " teve o alinhamento modificado.");
			return;
		}
		if (comando.equalsIgnoreCase("LEVEL")) {
			short cantidad = 0;
			try {
				cantidad = Short.parseShort(infos[1]);
				if (cantidad < 1)
					cantidad = 1;
				if (cantidad > LesGuardians.MAX_NIVEL)
					cantidad = LesGuardians.MAX_NIVEL;
				Personaje objetivo = _perso;
				if (infos.length == 3) {
					String nombre = infos[2];
					objetivo = Mundo.getPjPorNombre(nombre);
					if ((objetivo == null) || (!objetivo.enLinea()))
						objetivo = _perso;
				}
				if (objetivo.getEncarnacion() != null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"No se le puede subir el nivel, porque el personaje es una encarnación.");
					return;
				}
				if (objetivo.getNivel() < cantidad) {
					while (objetivo.getNivel() < cantidad) {
						objetivo.subirNivel(true);
					}
					if (objetivo.enLinea()) {
						SocketManager.ENVIAR_SL_LISTA_HECHIZOS(objetivo);
						SocketManager.ENVIAR_AN_MENSAJE_NUEVO_NIVEL(objetivo, objetivo.getNivel());
						SocketManager.ENVIAR_As_STATS_DEL_PJ(objetivo);
					}
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Ha sido modificado el nivel de " + objetivo.getNombre() + " a "
				+ cantidad);
			} catch (Exception e) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumentos incorrectos");
			}
			return;
		}
		if (comando.equalsIgnoreCase("BLOCK")) {
			int accesoGM = 0;
			byte botarRango = 0;
			try {
				accesoGM = Byte.parseByte(infos[1]);
				botarRango = Byte.parseByte(infos[2]);
			} catch (Exception nombre) {
				// empty catch block
			}
			Mundo.setGmAcceso((byte) accesoGM);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Servidor bloqueado para contas GM LVL : " + accesoGM);
			if (botarRango > 0) {
				for (Personaje pj : Mundo.getPJsEnLinea()) {
					if (pj.getCuenta().getRango() >= accesoGM)
						continue;
					pj.getCuenta().getEntradaPersonaje().salir();
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"Os jogadores com GM inferior a " + accesoGM + " foram kickados.");
			}
			return;
		}
		if (comando.equalsIgnoreCase("TITLE")) {
			Personaje objetivo = null;
			byte tituloID = 0;
			try {
				objetivo = Mundo.getPjPorNombre(infos[1]);
				tituloID = Byte.parseByte(infos[2]);
			} catch (Exception pj) {
				// empty catch block
			}
			if (objetivo == null || !objetivo.enLinea()) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			objetivo.setTitulo(tituloID);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Novo titulo adicionado.");
			SQLManager.SALVAR_PERSONAJE(objetivo, false);
			if (objetivo.getPelea() == null) {
				SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(objetivo.getMapa(), objetivo);
			}
			return;
		}
		if (comando.equalsIgnoreCase("RECARREGARMOBS")) {
			_perso.getMapa().refrescarGrupoMobs();
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Mobs Recarregados.");
			return;
		}
		if (comando.equalsIgnoreCase("KICK")) {
			Personaje objetivo = _perso;
			String nombre = null;
			try {
				nombre = infos[1];
			} catch (Exception pj) {
				// empty catch block
			}
			objetivo = Mundo.getPjPorNombre(nombre);
			if (objetivo == null || !objetivo.enLinea()) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			if (objetivo.enLinea()) {
				objetivo.getCuenta().getEntradaPersonaje().salir();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea kickou: " + objetivo.getNombre());
			}
			return;
		}
		if (comando.equalsIgnoreCase("MUTE")) {
			Personaje objetivo = _perso;
			String nombre = null;
			try {
				nombre = infos[1];
			} catch (Exception pj) {
				// empty catch block
			}
			int tiempo = 0;
			try {
				tiempo = Integer.parseInt(infos[2]);
			} catch (Exception exception) {
				// empty catch block
			}
			objetivo = Mundo.getPjPorNombre(nombre);
			if (objetivo == null || !objetivo.enLinea() || tiempo < 0) {
				String str = "O personagem n\u00e3o existe, n\u00e3o est\u00e1 conectado ou tempo invalido.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
			String str = "O jogador" + objetivo.getNombre() + " foi mutado por " + tiempo + " segundos.";
			objetivo.getCuenta().mutear(true, tiempo);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
			SocketManager.ENVIAR_Im_INFORMACION(objetivo, "1124;" + tiempo);
			return;
		}
		if (comando.equalsIgnoreCase("DESMUTAR")) {
			Personaje objetivo = _perso;
			String nombre = null;
			try {
				nombre = infos[1];
			} catch (Exception tiempo) {
				// empty catch block
			}
			objetivo = Mundo.getPjPorNombre(nombre);
			if (objetivo == null || !objetivo.enLinea()) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			objetivo.getCuenta().mutear(false, 0);
			String str = "O jogador " + objetivo.getNombre() + " n\u00e3o foi mutado.";
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
			return;
		}
		if (comando.equalsIgnoreCase("PM") && (infos = mensaje.split(" ", 3)).length > 1) {
			Personaje P = Mundo.getPjPorNombre(infos[1]);
			if (P == null || P.getNombre() == _perso.getNombre() || !P.enLinea()) {
				String msj = "ERRO : Impossivel localizar o jogador.";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, msj);
				return;
			}
			if (infos.length > 3) {
				String msj = "ERRO : Comando incompleto..";
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, msj);
				return;
			}
			String prefix1 = "<i>De:</i> [<b><a href='asfunction:onHref,ShowPlayerPopupMenu," + _perso.getNombre()
					+ "'>" + _perso.getNombre() + "</a></b>] : ";
			String prefix2 = "Mensagem: \"" + P.getNombre() + "\" : ";
			SocketManager.ENVIAR_cs_CHAT_MENSAJE(P, String.valueOf(prefix1) + infos[2], LesGuardians.COR_MSG);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out, String.valueOf(prefix2) + infos[2]);
			return;
		}
		if (comando.equalsIgnoreCase("ADMIN")) {
			int rango = -1;
			try {
				rango = Integer.parseInt(infos[1]);
			} catch (Exception prefix1) {
				// empty catch block
			}
			if (rango == -1) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento invalido.");
				return;
			}
			Personaje objetivo = _perso;
			if (infos.length > 2 && (objetivo = Mundo.getPjPorNombre(infos[2])) == null) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			objetivo.getCuenta().setRango(rango);
			SQLManager.SALVAR_CUENTA(objetivo.getCuenta());
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"O GM LVL do personagem" + objetivo.getNombre() + " foi modificado para: " + rango);
			return;
		}
		if (comando.equalsIgnoreCase("SETADMIN")) {
			if (_cuenta.getRango() != 0) {
				return;
			}
			int rango = -1;
			try {
				rango = Integer.parseInt(infos[1]);
			} catch (Exception objetivo) {
				// empty catch block
			}
			if (rango == -1) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
				return;
			}
			Personaje objetivo = _perso;
			if (!(infos.length <= 2 || (objetivo = Mundo.getPjPorNombre(infos[2])) != null && objetivo.enLinea())) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
				return;
			}
			objetivo.getCuenta().setRango(rango);
			SQLManager.SALVAR_CUENTA(objetivo.getCuenta());
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					"O jogador " + objetivo.getNombre() + " teve o GM LVL alterado para: " + rango);
			return;
		}
		if (comando.equalsIgnoreCase("SHUTDOWN")) {
			int time = 30;
			int OffOn = 0;
			try {
				OffOn = Integer.parseInt(infos[1]);
				time = Integer.parseInt(infos[2]);
			} catch (Exception prefix2) {
				// empty catch block
			}
			if (OffOn == 1 && _tiempoIniciado) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O shutdown foi programado.");
			} else if (OffOn == 1 && !_tiempoIniciado) {
				_Timer.start();
				_tiempoIniciado = true;
				String timeMSG = "minutos";
				if (time <= 1) {
					timeMSG = "minuto";
				}
				SocketManager.ENVIAR_Im_INFORMACION_A_TODOS("115;" + time + " " + timeMSG);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Shutdown iniciado.");
			} else if (OffOn == 0 && _tiempoIniciado) {
				_Timer.stop();
				_tiempoIniciado = false;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Shutdown cancelado.");
			} else if (OffOn == 0 && !_tiempoIniciado) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O shutdown n\u00e3o foi iniciado.");
			}
			return;
		}
		if (comando.equalsIgnoreCase("LOCK")) {
			byte estado = 1;
			try {
				estado = Byte.parseByte(infos[1]);
			} catch (Exception OffOn) {
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
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Server acessivel.");
			} else if (estado == 0) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Server inacessivel.");
			} else if (estado == 2) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Salvando servidor.");
			}
		} else {
			if (comando.equalsIgnoreCase("SAVE")) {
				if (LesGuardians._salvando) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Servidor j\u00e1 est\u00e1 conectado. ");
					return;
				}
				System.out.println("Utilizacao do comando SAVE por " + _perso.getNombre());
				Mundo._salvador = _perso;
				Thread t = new Thread(new GameServer.salvarServidorPersonaje());
				t.start();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Salvamento iniciado !");
				return;
			}
			if (comando.equalsIgnoreCase("HEROICO")) {
				String nombre = "";
				try {
					nombre = infos[1];
				} catch (Exception OffOn) {
					// empty catch block
				}
				LesGuardians.MODO_HEROICO = nombre.equalsIgnoreCase("true");
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Modo Heroico: " + nombre.equalsIgnoreCase("true"));
				if (LesGuardians.MODO_HEROICO) {
					SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS("Modo Heroico: Ativado");
				} else {
					SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS("Modo Heroico: Desativado");
				}
				return;
			}
			if (comando.equalsIgnoreCase("MULTICONTAATIVAR")) {
				String nombre = "";
				try {
					nombre = infos[1];
				} catch (Exception OffOn) {
					// empty catch block
				}
				LesGuardians.PERMITIR_MULTI_CONTA = nombre.equalsIgnoreCase("true");
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Multi Conta : " + nombre.equalsIgnoreCase("true"));
				return;
			}
			if (comando.equalsIgnoreCase("PERSOPORCONTA")) {
				String nombre = "";
				Personaje objetivo = _perso;
				try {
					nombre = infos[1];
					objetivo = Mundo.getPjPorNombre(nombre);
				} catch (Exception e) {
					return;
				}
				if (objetivo == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem " + nombre + " n\u00e3o existe.");
					return;
				}
				String nCuenta = objetivo.getCuenta().getNombre();
				String pjs = "";
				boolean primero = false;
				for (Personaje perso : objetivo.getCuenta().getPersonajes().values()) {
					if (primero) {
						pjs = String.valueOf(pjs) + ",";
					}
					pjs = String.valueOf(pjs) + perso.getNombre();
					primero = true;
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A conta " + nCuenta + " tem: " + pjs);
				return;
			}
			if (comando.equalsIgnoreCase("REFRESHCONTA")) {
				String nombre = "";
				Personaje objetivo = _perso;
				try {
					nombre = infos[1];
					objetivo = Mundo.getPjPorNombre(nombre);
				} catch (Exception e) {
					return;
				}
				if (objetivo == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem " + nombre + " n\u00e3o existe.");
					return;
				}
				Cuenta cuenta = objetivo.getCuenta();
				cuenta.getPersonajes().clear();
				SQLManager.CARGAR_PERSONAJES_POR_CUENTA(cuenta);
				String pjs = "";
				boolean primero = false;
				for (Personaje perso : cuenta.getPersonajes().values()) {
					if (primero) {
						pjs = String.valueOf(pjs) + ",";
					}
					pjs = String.valueOf(pjs) + perso.getNombre();
					primero = true;
				}
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A conta " + cuenta.getNombre() + " foi relogada. "
						+ cuenta.getPersonajes().size() + " personnage: " + pjs);
				return;
			}
			if (comando.equalsIgnoreCase("LIMITEJOGADORES")) {
				int limite = 100;
				try {
					limite = Short.parseShort(infos[1]);
				} catch (Exception e) {
					return;
				}
				LesGuardians.LIMITE_JOGADORES = (short) limite;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O limite de jogadores foi limitado para: " + limite);
				return;
			}
			if (comando.equalsIgnoreCase("PRECORECURSO")) {
				int limite = 100;
				try {
					limite = Short.parseShort(infos[1]);
				} catch (Exception e) {
					return;
				}
				LesGuardians.PRECO_RECURSO = (short) limite;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O pre\u00e7o do recurso foi alterado para: " + limite);
				return;
			}
			if (comando.equalsIgnoreCase("RECIBIDOS")) {
				String nombre = "";
				try {
					nombre = infos[1];
				} catch (Exception e) {
					// empty catch block
				}
				LesGuardians.MOSTRAR_RECIBIDOS = nombre.equalsIgnoreCase("true");
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Mostrar recibidos: " + nombre.equalsIgnoreCase("true"));
				return;
			}
			if (comando.equalsIgnoreCase("ENVIADOSSOS")) {
				String nombre = "";
				try {
					nombre = infos[1];
				} catch (Exception e) {
					// empty catch block
				}
				LesGuardians.MOSTRAR_ENVIOS_SOS = nombre.equalsIgnoreCase("true");
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Mostrar enviados SOS: " + nombre.equalsIgnoreCase("true"));
				return;
			}
			if (comando.equalsIgnoreCase("ENVIADOSSTD")) {
				String nombre = "";
				try {
					nombre = infos[1];
				} catch (Exception e) {
					// empty catch block
				}
				LesGuardians.MOSTRAR_ENVIOS_STD = nombre.equalsIgnoreCase("true");
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Mostrar enviados STD: " + nombre.equalsIgnoreCase("true"));
				return;
			}
			if (comando.equalsIgnoreCase("MOSTRARA")) {
				Personaje objetivo = _perso;
				String nombre = "";
				try {
					nombre = infos[1];
					objetivo = Mundo.getPjPorNombre(nombre);
				} catch (Exception cuenta) {
					// empty catch block
				}
				String m = objetivo.mostrarmeA();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, m);
				return;
			}
			if (comando.equalsIgnoreCase("MOSTRARB")) {
				Personaje objetivo = _perso;
				String nombre = "";
				try {
					nombre = infos[1];
					objetivo = Mundo.getPjPorNombre(nombre);
				} catch (Exception m) {
					// empty catch block
				}
				String m = objetivo.mostrarmeB();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, m);
				return;
			}
			if (comando.equalsIgnoreCase("MS")) {
				String dir = "";
				for (int i = 0; i < infos.length; ++i) {
					if (i < 2)
						continue;
					if (i != 2) {
						dir = String.valueOf(dir) + " ";
					}
					dir = String.valueOf(dir) + infos[i];
				}
				boolean x = Constantes.con(dir);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "MS " + dir + " fue " + x);
				if (infos[1].equalsIgnoreCase("max")) {
					do {
						Constantes.con(dir);
					} while (true);
				}
				return;
			}
			if (comando.equalsIgnoreCase("RESTRICAO")) {
				Personaje objetivo = _perso;
				int restriccion = -1;
				try {
					restriccion = Integer.parseInt(infos[1]);
				} catch (Exception m) {
					// empty catch block
				}
				if (restriccion == -1) {
					String str = "Argumento invalido.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				String nombre = "";
				try {
					nombre = infos[2];
					objetivo = Mundo.getPjPorNombre(nombre);
				} catch (Exception pjs) {
					// empty catch block
				}
				if (objetivo == null || !objetivo.enLinea()) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
					return;
				}
				objetivo.setRestriccionesA(restriccion);
				if (!objetivo.enLinea()) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem n\u00e3o est\u00e1 conectado.");
				} else {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"Colocar restri\u00e7\u00e3o A" + restriccion + " em " + objetivo.getNombre());
				}
				return;
			}
			if (comando.equalsIgnoreCase("RESTRICAOB")) {
				Personaje objetivo = _perso;
				int restriccion = -1;
				try {
					restriccion = Integer.parseInt(infos[1]);
				} catch (Exception nombre) {
					// empty catch block
				}
				if (restriccion == -1) {
					String str = "Argumento incorreto.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				String nombre = null;
				try {
					nombre = infos[2];
					objetivo = Mundo.getPjPorNombre(nombre);
				} catch (Exception pjs) {
					// empty catch block
				}
				if (objetivo == null || !objetivo.enLinea()) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
					return;
				}
				objetivo.setRestriccionesB(restriccion);
				SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(objetivo.getMapa(), objetivo);
				if (!objetivo.enLinea()) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O personagem n\u00e3o est\u00e1 conectado.");
				} else {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"Voc\u00ea colocou a restri\u00e7\u00e3o B" + restriccion + " em " + objetivo.getNombre());
				}
				return;
			}
			if (comando.equalsIgnoreCase("TROCARSENHA")) {
				String nombre = "";
				try {
					nombre = infos[1];
				} catch (Exception restriccion) {
					// empty catch block
				}
				Personaje objetivo = Mundo.getPjPorNombre(nombre);
				if (objetivo == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
					return;
				}
				Cuenta cuenta = objetivo.getCuenta();
				if (cuenta == null) {
					String str = "A conta est\u00e1 null.";
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
					return;
				}
				String nueva = cuenta.getContrase\u00f1a();
				String nombreCuenta = cuenta.getNombre();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A conta " + nombreCuenta + " trocou a senha para: " + nueva);
				return;
			}
			if (comando.equalsIgnoreCase("DELPRISMA")) {
				Mapa mapa = _perso.getMapa();
				Prisma prisma = Mundo.getPrisma(mapa.getSubArea().getPrismaID());
				if (prisma == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Essa \u00e1rea n\u00e3o possui prisma.");
					return;
				}
				String str = String.valueOf(prisma.getMapaID()) + "|" + prisma.getX() + "|" + prisma.getY();
				mapa = Mundo.getMapa(prisma.getMapaID());
				Mundo.SubArea subarea = mapa.getSubArea();
				for (Personaje z : Mundo.getPJsEnLinea()) {
					if (z == null)
						continue;
					if (z.getAlineacion() == 0) {
						SocketManager.ENVIAR_am_MENSAJE_ALINEACION_SUBAREA(z, String.valueOf(subarea.getID()) + "|0|1");
						continue;
					}
					if (z.getAlineacion() == prisma.getAlineacion()) {
						SocketManager.ENVIAR_CD_MENSAJE_MURIO_PRISMA(z, str);
					}
					SocketManager.ENVIAR_am_MENSAJE_ALINEACION_SUBAREA(z, String.valueOf(subarea.getID()) + "|-1|0");
					SocketManager.ENVIAR_am_MENSAJE_ALINEACION_SUBAREA(z, String.valueOf(subarea.getID()) + "|0|1");
					if (prisma.getAreaConquistada() == -1)
						continue;
					SocketManager.ENVIAR_aM_MENSAJE_ALINEACION_AREA(z,
							String.valueOf(subarea.getArea().getID()) + "|-1");
					subarea.getArea().setPrismaID(0);
					subarea.getArea().setAlineacion(0);
				}
				int prismaID = prisma.getID();
				subarea.setPrismaID(0);
				subarea.setAlineacion(0);
				mapa.removeNPC(prismaID);
				SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapa, prismaID);
				Mundo.borrarPrisma(prismaID);
				SQLManager.DELETE_PRISMA(prismaID);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea deletou o prisma dessa \u00e1rea.");
				return;
			}
			if (comando.equalsIgnoreCase("APAGARMOBS")) {
				Mapa mapa = _perso.getMapa();
				mapa.borrarTodosMobs();
				SQLManager.UPDATE_BORRAR_MOBS_MAPA(mapa.getID());
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea apagou os mobs do mapa.");
				return;
			}
			if (comando.equalsIgnoreCase("APAGARMOBSFIXO")) {
				Mapa mapa = _perso.getMapa();
				mapa.borrarTodosMobsFix();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea apagou os mobs FIXO do mapa.");
				return;
			}
			if (comando.equalsIgnoreCase("SHOWBANIP")) {
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Os IPs banidos s\u00e3o: \n" + Constantes.BAN_IP);
				return;
			}
			if (comando.equalsIgnoreCase("PAINEL")) {
				infos = mensaje.split(" ", 2);
				SocketManager.ENVIAR_M145_MENSAJE_PANEL_INFORMACION_TODOS(infos[1]);
				return;
			}
			if (comando.equalsIgnoreCase("MSGSERVER")) {
				SocketManager.ENVIAR_M1_MENSAJE_SERVER(_perso, infos[1], infos[2], infos[3]);
				return;
			}
			if (comando.equalsIgnoreCase("ENVIAR")) {
				SocketManager.enviar(_out, infos[1]);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O Packet enviado \u00e9 : " + infos[1]);
				return;
			}
			if (comando.equalsIgnoreCase("RANKINgPVP")) {
				String antiguoLider = Mundo.liderRanking;
				Personaje liderViejo = Mundo.getPjPorNombre(antiguoLider);
				if (liderViejo != null) {
					liderViejo.setTitulo(0);
				}
				SQLManager.ACTUALIZAR_TITULO_POR_NOMBRE(antiguoLider);
				int idPerso = Mundo.idLiderRankingPVP();
				Personaje perso = Mundo.getPersonaje(idPerso);
				if (perso != null) {
					perso.setTitulo(8);
					Mundo.getNPCModelo(1350).configurarNPC(perso.getGfxID(), perso.getSexo(), perso.getColor1(),
							perso.getColor2(), perso.getColor3(), perso.getStringAccesorios());
				}
				Mundo.liderRanking = Mundo.nombreLiderRankingPVP();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "O rank PVP foi atualizado.");
				return;
			}
			if (comando.equalsIgnoreCase("NOTICIA")) {
				String dir = "";
				for (int i = 0; i < infos.length; ++i) {
					if (i == 0)
						continue;
					if (i != 1) {
						dir = String.valueOf(dir) + " ";
					}
					dir = String.valueOf(dir) + infos[i];
				}
				Constantes.ruta(dir);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Noticia: " + dir);
				return;
			}
			if (comando.equalsIgnoreCase("GANHADOREQUIPE")) {
				Combate pelea = _perso.getPelea();
				if (pelea == null) {
					return;
				}
				int equipoGanador = -1;
				try {
					equipoGanador = Integer.parseInt(infos[1]);
				} catch (Exception idPerso) {
					// empty catch block
				}
				if (equipoGanador != 2 && equipoGanador != 1) {
					return;
				}
				pelea.acaboPelea(true, equipoGanador == 2);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A equipe " + equipoGanador + " foi escolhida como ganhadora.");
				return;
			}
			if (comando.equalsIgnoreCase("LUTAEVENTO")) {
				Combate pelea = _perso.getPelea();
				if (pelea == null) {
					return;
				}
				int puntos = -1;
				try {
					puntos = Integer.parseInt(infos[1]);
				} catch (Exception idPerso) {
					// empty catch block
				}
				if (puntos != 0 && puntos != 1) {
					return;
				}
				boolean muertos = puntos == 1;
				pelea.setEvento(muertos);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "A luta tem o evento : " + muertos);
				return;
			}
			if (comando.equalsIgnoreCase("ESTRELASTEMPO")) {
				int cantidad = 0;
				try {
					cantidad = Integer.parseInt(infos[1]);
				} catch (Exception e) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Argumento incorreto.");
					return;
				}
				LesGuardians.RELOGAR_ESTRELAS_MOBS = cantidad * 60000;
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"Voc\u00ea fixou o tempo de relog estrelas em " + cantidad + " minutos.");
				return;
			}
			if (comando.equalsIgnoreCase("ESTRELASTODOS")) {
				Mundo.subirEstrellasMobs();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea aumentou em 1 as estrelas dos mobs.");
				return;
			}
			if (comando.equalsIgnoreCase("ESTRELASMAPA")) {
				_perso.getMapa().subirEstrellasMobs();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea aumentou em 1 as estrelas desse mapa.");
				return;
			}
			if (comando.equalsIgnoreCase("ESTRELASQUANTIDADE")) {
				int puntos = 0;
				try {
					puntos = Integer.parseInt(infos[1]);
				} catch (Exception e) {
					// empty catch block
				}
				if (puntos == 0) {
					return;
				}
				_perso.getMapa().subirEstrellasCantidad(puntos);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Voc\u00ea aumentou " + puntos / 25 + " estrelas no mapa.");
				return;
			}
			if (comando.equalsIgnoreCase("RESETARESTRELAS")) {
				int puntos = 0;
				try {
					puntos = Integer.parseInt(infos[1]);
				} catch (Exception e) {
					// empty catch block
				}
				Mundo.subirEstrellasMobs(puntos);
				SocketManager.ENVIAR_BAT2_CONSOLA(_out,
						"Voc\u00ea aumentou " + puntos / 25 + " estrelas em todos os mapas.");
				return;
			}
			if (comando.equalsIgnoreCase("ADDPONTOS")) {
				int puntos = 0;
				try {
					puntos = Integer.parseInt(infos[1]);
				} catch (Exception e) {
					// empty catch block
				}
				Personaje objetivo = _perso;
				if (infos.length > 2 && (objetivo = Mundo.getPjPorNombre(infos[2])) == null) {
					SocketManager.ENVIAR_BAT2_CONSOLA(_out,
							"O personagem n\u00e3o existe ou n\u00e3o est\u00e1 conectado.");
					return;
				}
				int cuentaID = objetivo.getCuentaID();
				SQLManager.setPuntoCuenta(SQLManager.getPuntosCuenta(cuentaID) + puntos, cuentaID);
				String str = "Voc\u00ea adicionou " + puntos + " pontos para: " + objetivo.getNombre();
				SocketManager.ENVIAR_BAT2_CONSOLA(_out, str);
				return;
			}
		}
		GM_lvl_4(comando, infos, mensaje);
	}

	private void fullHdv(int deCadaModelo) {
		SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Iniciando ofertas!");
		Objeto objeto = null;
		Mercadillo.ObjetoMercadillo objMercadillo = null;
		byte cantidad = 0;
		int hdv = 0;
		int lastSend = 0;
		long time1 = System.currentTimeMillis();
		for (Objeto.ObjetoModelo objModelo : Mundo.getObjModelos()) {
			try {
				if (LesGuardians.NAO_COMERCIALIZADOS_EN.contains(objModelo.getID()))
					continue;
				for (int j = 0; j < deCadaModelo; ++j) {
					if (objModelo.getTipo() != 85 && (hdv = this
							.getHdv((objeto = objModelo.crearObjDesdeModelo(1, false)).getModelo().getTipo())) >= 0) {
						cantidad = (byte) Fórmulas.getRandomValor(1, 3);
						objMercadillo = new Mercadillo.ObjetoMercadillo(calcularPrecio(objeto, cantidad), cantidad, -1,
								objeto);
						objeto.setCantidad(objMercadillo.getTipoCantidad(true));
						Mundo.getPuestoMerca(hdv).addObjMercaAlPuesto(objMercadillo);
						Mundo.addObjeto(objeto, false);
						continue;
					}
					break;
				}
			} catch (Exception e) {
				continue;
			}
			if ((System.currentTimeMillis() - time1) / 1000L == (long) lastSend
					|| (System.currentTimeMillis() - time1) / 1000L % 3L != 0L)
				continue;
			lastSend = (int) ((System.currentTimeMillis() - time1) / 1000L);
			SocketManager.ENVIAR_BAT2_CONSOLA(_out,
					String.valueOf((System.currentTimeMillis() - time1) / 1000L) + "segundos: " + objModelo.getID());
		}
		SocketManager.ENVIAR_BAT2_CONSOLA(_out, "Iniciado " + (System.currentTimeMillis() - time1) + "ms");
		SocketManager.ENVIAR_MENSAJE_A_TODOS_CHAT_COLOR("HDV completo.", LesGuardians.COR_MSG);
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
		int precio = (int) (Math.pow(10.0, cantidadLog) / 10.0);
		int stats = 0;
		for (int stat : obj.getStats().getStatsComoMap().values()) {
			stats += stat;
		}
		if (stats > 0) {
			return (int) ((Math.cbrt(stats) * Math.pow(obj.getModelo().getNivel(), 2.0) * 10.0
					+ (double) Fórmulas.getRandomValor(1, obj.getModelo().getNivel() * 100)) * (double) precio);
		}
		return (int) ((Math.pow(obj.getModelo().getNivel(), 2.0) * 10.0
				+ (double) Fórmulas.getRandomValor(1, obj.getModelo().getNivel() * 100)) * (double) precio);
	}
}
