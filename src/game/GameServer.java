package game;

import common.Constantes;
import common.CryptManager;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.Mundo;
import game.GameThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import objects.Cuenta;
import objects.Mapa;
import objects.Personaje;
import org.fusesource.jansi.AnsiConsole;

public class GameServer implements Runnable {
	private ServerSocket _serverSocket;
	private Thread _thread;
	private ArrayList<GameThread> _clientes = new ArrayList<GameThread>();
	private ArrayList<Cuenta> _esperando = new ArrayList<Cuenta>();
	private Timer _tiempoSalvada;
	private Timer _subirEstrellas;
	private Timer _rankingPVP;
	private Timer _moverPavos;
	private Timer _publicidad;
	private long _tiempoInicio;
	private int _recordJugadores = 0;
	private String _primeraIp = "";
	private String _segundaIp = "";
	private String _terceraIp = "";
	private int _alterna = 0;
	private long _tiempoBan1 = 0L;
	private long _tiempoBan2 = 0L;
	private boolean _ban = true;
	private int _i = 0;

	public GameServer(String Ip) {
		try {
			_tiempoSalvada = new Timer();
			_tiempoSalvada.schedule(new TimerTask() {

				@Override
				public void run() {
					if (!LesGuardians._salvando) {
						System.out.println("Servidor salvo em :  " + System.currentTimeMillis() + "millisegundos.");
						Thread t = new Thread(new salvarServidorPersonaje());
						t.start();
					} else {
						System.out.println(
								"O servidor est\u00e1 tentando salvar automaticamente, mas o servidor j\u00e1 est\u00e1 salvando (SERVIDOR PERSONAGEM)TIME: "
										+ System.currentTimeMillis());
					}
				}
			}, LesGuardians.TEMPO_SALVAR, (long) LesGuardians.TEMPO_SALVAR);
			_subirEstrellas = new Timer();
			_subirEstrellas.schedule(new TimerTask() {

				@Override
				public void run() {
					Mundo.subirEstrellasMobs();
				}
			}, LesGuardians.RELOGAR_ESTRELAS_MOBS, (long) LesGuardians.RELOGAR_ESTRELAS_MOBS);
			_moverPavos = new Timer();
			_moverPavos.schedule(new TimerTask() {

				@Override
				public void run() {
					for (Mapa.Cercado cercado : Mundo.todosCercados()) {
						cercado.startMoverDrago();
					}
				}
			}, LesGuardians.TIEMPO_MOVERSE_PAVOS, (long) LesGuardians.TIEMPO_MOVERSE_PAVOS);
			_publicidad = new Timer();
			_publicidad.schedule(new TimerTask() {

				@Override
				public void run() {
					SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS(LesGuardians.PUBLICIDADE.get(_i));
					GameServer gameServer = GameServer.this;
					gameServer._i = gameServer._i + 1;
					if (_i >= LesGuardians.PUBLICIDADE.size()) {
						_i = 0;
					}
				}
			}, 3600000L, 3600000L);
			_rankingPVP = new Timer();
			_rankingPVP.schedule(new TimerTask() {

				@Override
				public void run() {
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
				}
			}, 3600000L, 3600000L);
			_serverSocket = new ServerSocket(LesGuardians.PORTA_JOGO);
			if (LesGuardians.USAR_IP_CRIPTO) {
				LesGuardians.CRYPTER_IP = String.valueOf(CryptManager.encriptarIP(Ip))
						+ CryptManager.encriptarPuerto(LesGuardians.PORTA_JOGO);
			}
			_tiempoInicio = System.currentTimeMillis();
			_thread = new Thread(this);
			_thread.start();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
			LesGuardians.cerrarServer();
		}
	}

	public static void print(String message, int color) {
		AnsiConsole.out.print("\u001b[" + color + "m" + message + "\u001b[0m");
	}

	public static void clearScreen() {
		AnsiConsole.out.print("\u001b[H\u001b[2J");
	}

	/*public static void setTitle(String title) {
		AnsiConsole.out.printf("%c]0;%s%c", Character.valueOf('\u001b'), title, Character.valueOf('\u0007'));
	}*/

	public ArrayList<GameThread> getClientes() {
		return _clientes;
	}

	public long getTiempoInicio() {
		return _tiempoInicio;
	}

	public int getRecordJugadores() {
		return _recordJugadores;
	}

	public int nroJugadoresLinea() {
		return _clientes.size();
	}

	@Override
	public void run() {
		while (LesGuardians._corriendo) {
			try {
				GameThread gestor = new GameThread(_serverSocket.accept());
				if (LesGuardians.CONTRER_DDOS) {
					++_alterna;
					String ipTemporal = gestor.getSock().getInetAddress().getHostAddress();
					if (!gestor.poderEntrar()) {
						continue;
					}
					if (_alterna == 1) {
						_primeraIp = ipTemporal;
						if (_ban) {
							_tiempoBan1 = System.currentTimeMillis();
						} else {
							_tiempoBan2 = System.currentTimeMillis();
						}
						_ban = !_ban;
					} else if (_alterna == 2) {
						_segundaIp = ipTemporal;
						if (_ban) {
							_tiempoBan1 = System.currentTimeMillis();
						} else {
							_tiempoBan2 = System.currentTimeMillis();
						}
						_ban = !_ban;
					} else {
						_terceraIp = ipTemporal;
						_alterna = 0;
						if (_ban) {
							_tiempoBan1 = System.currentTimeMillis();
						} else {
							_tiempoBan2 = System.currentTimeMillis();
						}
						_ban = !_ban;
					}
					if (_primeraIp.compareTo(ipTemporal) == 0 && _segundaIp.compareTo(ipTemporal) == 0
							&& _terceraIp.compareTo(ipTemporal) == 0 && Math.abs(_tiempoBan1 - _tiempoBan2) < 500L) {
						Constantes.BAN_IP = String.valueOf(Constantes.BAN_IP) + "," + ipTemporal;
						SQLManager.INSERT_BANIP(ipTemporal);
						System.out.println("IP Banido : " + ipTemporal);
						SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE_TODOS(
								"O IP " + ipTemporal + " foi banido por atacar o servidor.");
						gestor.getSock().close();
						continue;
					}
				}
				ArrayList<GameThread> array = new ArrayList<GameThread>();
				array.addAll(_clientes);
				for (GameThread EP : array) {
					if (EP.getCuenta() == null || gestor.getCuenta() == null
							|| gestor.getCuenta().getID() != EP.getCuenta().getID())
						continue;
					EP.salir();
				}
				_clientes.add(gestor);
				if (_clientes.size() <= _recordJugadores) {
					continue;
				}
				_recordJugadores = _clientes.size();
			} catch (IOException e) {
				System.out.println("IOException: " + e.getMessage());
				try {
					if (!_serverSocket.isClosed()) {
						_serverSocket.close();
					}
					LesGuardians.cerrarServer();
				} catch (IOException iOException) {}
			}
		}
	}

	public void cerrarServidor() {
		try {
			_serverSocket.close();
		} catch (IOException iOException) {}
		ArrayList<GameThread> array = new ArrayList<GameThread>();
		array.addAll(_clientes);
		for (GameThread EP : array) {
			try {
				EP.cerrarSocket();
			} catch (Exception exception) {}
		}
	}

	public void delGestorCliente(GameThread entradaPersonaje) {
		_clientes.remove(entradaPersonaje);
	}

	public synchronized Cuenta getEsperandoCuenta(int id) {
		for (int i = 0; i < _esperando.size(); ++i) {
			if (_esperando.get(i).getID() != id)
				continue;
			return _esperando.get(i);
		}
		return null;
	}

	public synchronized void delEsperandoCuenta(Cuenta cuenta) {
		_esperando.remove(cuenta);
	}

	public synchronized void addEsperandoCuenta(Cuenta cuenta) {
		_esperando.add(cuenta);
	}

	public static String getTiempoServer() {
		Date actDate = new Date();
		return "BT" + (actDate.getTime() + 3600000L);
	}

	public static String getFechaServer() {
		Date actDate = new Date();
		SimpleDateFormat fecha = new SimpleDateFormat("dd");
		String dia = String.valueOf(Integer.parseInt(fecha.format(actDate)));
		while (dia.length() < 2) {
			dia = "0" + dia;
		}
		fecha = new SimpleDateFormat("MM");
		String mes = String.valueOf(Integer.parseInt(fecha.format(actDate)) - 1);
		while (mes.length() < 2) {
			mes = "0" + mes;
		}
		fecha = new SimpleDateFormat("yyyy");
		String a\u00f1o = String.valueOf(Integer.parseInt(fecha.format(actDate)) - 1370);
		return "BD" + a\u00f1o + "|" + mes + "|" + dia;
	}

	public Thread getThread() {
		return _thread;
	}

	public static class salvarServidorPersonaje implements Runnable {
		@Override
		public synchronized void run() {
			if (!LesGuardians._salvando) {
				SocketManager.ENVIAR_Im_INFORMACION_A_TODOS("1164");
				Mundo.salvarServidor();
				SocketManager.ENVIAR_Im_INFORMACION_A_TODOS("1165");
			}
		}
	}
}