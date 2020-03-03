package game;

import common.Comandos;
import common.Condiciones;
import common.Constantes;
import common.CryptManager;
import common.Fórmulas;
import common.LesGuardians;
import common.Pathfinding;
import common.SQLManager;
import common.SocketManager;
import common.World;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import objects.Casa;
import objects.Cofre;
import objects.Coletor;
import objects.Conta;
import objects.Dragossauros;
import objects.Fight;
import objects.Guild;
import objects.Incarnacao;
import objects.Maps;
import objects.Mercador;
import objects.NPC_tmpl;
import objects.Objeto;
import objects.Personagens;
import objects.Pets;
import objects.Prisma;
import objects.Profissao;
import objects.Set_Vivo;
import objects.Spell;
import objects.Stockage;
import objects.Tutorial;

public class GameThread implements Runnable {
	private BufferedReader _in;
	private Thread _thread;
	private PrintWriter _out;
	private Socket _socket;
	private Conta _cuenta;
	private Personagens _perso;
	private Map<Integer, AccionDeJuego> _acciones = new TreeMap<Integer, AccionDeJuego>();
	private long _tiempoUltComercio = 0L;
	private long _tiempoUltReclutamiento = 0L;
	private long _tiempoUltSalvada = 0L;
	private long _tiempoUltAlineacion = 0L;
	private Comandos _comando;
	private boolean _entrar = true;

	public GameThread(Socket socket) {
		try {
			_socket = socket;
			_entrar = true;
			if (Constantes.compararConIPBaneadas(_socket.getInetAddress().getHostAddress())) {
				_entrar = false;
				_socket.close();
				return;
			}
			_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			_out = new PrintWriter(_socket.getOutputStream());
			_thread = new Thread(this);
			_thread.setDaemon(true);
			_thread.start();
		} catch (IOException e) {
			try {
				System.out.println(e.getMessage());
				if (!_socket.isClosed()) {
					_socket.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean poderEntrar() {
		return _entrar;
	}

	public Socket getSock() {
		return _socket;
	}

	public void run() {
	    try {
	      String packet = "";
	      char[] charCur = new char[1];
	      SocketManager.ENVIAR_HG_SALUDO_JUEGO_GENERAL(_out);
	      while (_in.read(charCur, 0, 1) != -1 && LesGuardians._corriendo) {
	        if (charCur[0] != '\000' && charCur[0] != '\n' && charCur[0] != '\r') {
	          packet = String.valueOf(packet) + charCur[0];
	          continue;
	        } 
	        if (!packet.isEmpty()) {
	          packet = CryptManager.aUnicode(packet);
	          if (LesGuardians.MOSTRAR_RECIBIDOS)
	            System.out.println("<<RECU:  " + packet); 
	          analizar_Packets(packet);
	          packet = "";
	        } 
	      } 
	    } catch (IOException e) {
	      try {
	        _in.close();
	        _out.close();
	        if (_cuenta != null) {
	          _cuenta.setTempPerso(null);
	          _cuenta.setEntradaPersonaje(null);
	          _cuenta.setEntradaGeneral(null);
	        } 
	        if (!_socket.isClosed())
	          _socket.close(); 
	      } catch (IOException e1) {
	        e1.printStackTrace();
	      } 
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      salir();
	    } 
	  }

	private void analizar_Packets(String packet) {
		if (packet.length() > 3 && packet.substring(0, 4).equalsIgnoreCase("ping")) {
			SocketManager.ENVIAR_pong(_out);
			return;
		}
		if (packet.length() > 4 && packet.substring(0, 5).equalsIgnoreCase("qping")) {
			Fight.Luchador luchador;
			Fight pelea;
			if (_perso != null && (pelea = _perso.getPelea()) != null
					&& (luchador = pelea.getLuchadorPorPJ(_perso)) != null && luchador.puedeJugar()) {
				pelea.tiempoTurno();
			}
			SocketManager.ENVIAR_qpong(_out);
			return;
		}
		if (_perso != null && _perso.cambiarNombre()) {
			if (World.getPjPorNombre(packet) != null) {
				SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out, "Nick n\u00e3o disponivel.");
				SocketManager.ENVIAR_AlE_CAMBIAR_NOMBRE(_out, "r");
				SocketManager.ENVIAR_AlE_CAMBIAR_NOMBRE(_out, "r");
				return;
			}
			boolean esValido = true;
			String nombre = packet.toLowerCase();
			if (nombre.length() > 20) {
				esValido = false;
			}
			if (esValido) {
				int cantidadLetras = 0;
				char exLetraA = ' ';
				char exLetraB = ' ';
				for (char letra : nombre.toCharArray()) {
					if ((letra < 'a' || letra > 'z') && letra != '-') {
						esValido = false;
						break;
					}
					if (letra == exLetraA && letra == exLetraB) {
						esValido = false;
						break;
					}
					if (letra >= 'a' && letra <= 'z') {
						exLetraA = exLetraB;
						exLetraB = letra;
					}
					if (letra != '-')
						continue;
					if (cantidadLetras >= 1) {
						esValido = false;
						break;
					}
					++cantidadLetras;
				}
			}
			if (!esValido) {
				SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out, "Nick n\u00e3o disponivel.");
				SocketManager.ENVIAR_AlE_CAMBIAR_NOMBRE(_out, "r");
				SocketManager.ENVIAR_AlE_CAMBIAR_NOMBRE(_out, "r");
				return;
			}
			_perso.setNombre(packet);
			SocketManager.ENVIAR_AlE_CAMBIAR_NOMBRE(_out, "r");
			SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(_perso.getMapa(), _perso);
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out, "Seu novo nome \u00e9 <b>" + packet + "</b> .");
			return;
		}
		switch (packet.charAt(0)) {
		case 'A': {
			analizar_Cuenta(packet);
			break;
		}
		case 'B': {
			analizar_Basicos(packet);
			break;
		}
		case 'C': {
			analizar_Conquista(packet);
			break;
		}
		case 'c': {
			analizar_Canal(packet);
			break;
		}
		case 'D': {
			analizar_Dialogos(packet);
			break;
		}
		case 'E': {
			analizar_Intercambios(packet);
			break;
		}
		case 'e': {
			analizar_Ambiente(packet);
			break;
		}
		case 'F': {
			analizar_Amigos(packet);
			break;
		}
		case 'f': {
			analizar_Peleas(packet);
			break;
		}
		case 'G': {
			analizar_Juego(packet);
			break;
		}
		case 'g': {
			analizar_Gremio(packet);
			break;
		}
		case 'h': {
			analizar_Casas(packet);
			break;
		}
		case 'i': {
			analizar_Enemigos(packet);
			break;
		}
		case 'J': {
			analizar_Oficios(packet);
			break;
		}
		case 'K': {
			analizar_Claves(packet);
			break;
		}
		case 'O': {
			analizar_Objetos(packet);
			break;
		}
		case 'P': {
			analizar_Grupo(packet);
			break;
		}
		case 'Q': {
			analizar_Misiones(packet);
			break;
		}
		case 'R': {
			analizar_Montura(packet);
			break;
		}
		case 'S': {
			analizar_Hechizos(packet);
			break;
		}
		case 'T': {
			analizar_Tutoriales(packet);
			break;
		}
		case 'W': {
			analizar_Areas(packet);
		}
		}
	}

	private void analizar_Tutoriales(String packet) {
		String[] param = packet.split("\\|");
		Tutorial tuto = _perso.getTutorial();
		_perso.setTutorial(null);
		switch (packet.charAt(1)) {
		case 'V': {
			if (packet.charAt(2) != '0' && packet.charAt(2) != '4') {
				try {
					int index = Integer.parseInt(String.valueOf(packet.charAt(2))) - 1;
					tuto.getRecompensa().get(index).aplicar(_perso, null, -1, (short) -1);
				} catch (Exception e) {
					System.out.println("Parar jogar: " + tuto.getID() + " Home: " + param[0]);
				}
			}
			try {
				tuto.getFin().aplicar(_perso, null, -1, (short) -1);
			} catch (Exception exception) {
				// empty catch block
			}
			_perso.setOcupado(false);
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
			try {
				_perso.setOrientacion(Byte.parseByte(param[2]));
				_perso.setCelda(_perso.getMapa().getCelda(Short.parseShort(param[1])));
				break;
			} catch (Exception exception) {
				// empty catch block
			}
		}
		}
	}

	private void analizar_Conquista(String packet) {
		switch (packet.charAt(1)) {
		case 'b': {
			SocketManager.ENVIAR_Cb_BALANCE_CONQUISTA(_perso,
					String.valueOf(World.getBalanceMundo(_perso.getAlineacion())) + ";"
							+ World.getBalanceArea(_perso.getMapa().getSubArea().getArea(), _perso.getAlineacion()));
			break;
		}
		case 'B': {
			float porc = World.getBalanceMundo(_perso.getAlineacion());
			float porcN = (float) Math.rint((double) ((float) _perso.getNivelAlineacion() / 2.5f) + 1.0);
			SocketManager.ENVIAR_CB_BONUS_CONQUISTA(_perso, String.valueOf(porc) + "," + porc + "," + porc + ";" + porcN
					+ "," + porcN + "," + porcN + ";" + porc + "," + porc + "," + porc);
			break;
		}
		case 'W': {
			conquista_Geoposicion(packet);
			break;
		}
		case 'I': {
			conquista_Defensa(packet);
			break;
		}
		case 'F': {
			conquista_Unirse_Defensa_Prisma(packet);
		}
		}
	}

	private void analizar_Misiones(String packet) {
		switch (packet.charAt(1)) {
		case 'L': {
			SocketManager.ENVIAR_QL_LISTA_MISIONES(_perso, "");
			break;
		}
		case 'S': {
			SocketManager.ENVIAR_QS_PASOS_RECOMPENSA_MISION(_perso, "");
		}
		}
	}

	private void conquista_Defensa(String packet) {
		switch (packet.charAt(2)) {
		case 'J': {
			String str = _perso.analizarPrismas();
			Prisma prisma = World.getPrisma(_perso.getMapa().getSubArea().getPrismaID());
			if (prisma != null) {
				Prisma.analizarAtaque(_perso);
				Prisma.analizarDefensa(_perso);
			}
			SocketManager.ENVIAR_CIJ_INFO_UNIRSE_PRISMA(_perso, str);
			break;
		}
		case 'V': {
			SocketManager.ENVIAR_CIV_CERRAR_INFO_CONQUISTA(_perso);
		}
		}
	}

	private void conquista_Geoposicion(String packet) {
		switch (packet.charAt(2)) {
		case 'J': {
			SocketManager.ENVIAR_CW_INFO_MUNDO_CONQUISTA(_perso, World.prismasGeoposicion(_perso.getAlineacion()));
			break;
		}
		case 'V': {
			SocketManager.ENVIAR_CIV_CERRAR_INFO_CONQUISTA(_perso);
		}
		}
	}

	private void conquista_Unirse_Defensa_Prisma(String packet) {
		switch (packet.charAt(2)) {
		case 'J': {
			int prismaID = _perso.getMapa().getSubArea().getPrismaID();
			Prisma prisma = World.getPrisma(prismaID);
			if (prisma == null) {
				return;
			}
			short mapaID = prisma.getMapaID();
			short celdaID = prisma.getCeldaID();
			if (prisma.getAlineacion() != _perso.getAlineacion()) {
				return;
			}
			if (_perso.getPelea() != null) {
				return;
			}
			if (!prisma.getPelea().unirsePeleaPrisma(_perso, prismaID, mapaID, celdaID))
				break;
			for (Personagens z : World.getPJsEnLinea()) {
				if (z == null || z.getAlineacion() != _perso.getAlineacion())
					continue;
				Prisma.analizarDefensa(z);
			}
			break;
		}
		}
	}

	private void analizar_Casas(String packet) {
		switch (packet.charAt(1)) {
		case 'B': {
			packet = packet.substring(2);
			Casa.comprarCasa(_perso);
			break;
		}
		case 'G': {
			packet = packet.substring(2);
			if (packet.isEmpty()) {
				packet = null;
			}
			Casa.analizarCasaGremio(_perso, packet);
			break;
		}
		case 'Q': {
			packet = packet.substring(2);
			Casa.salir(_perso, packet);
			break;
		}
		case 'S': {
			packet = packet.substring(2);
			Casa.precioVenta(_perso, packet);
			break;
		}
		case 'V': {
			Casa.cerrarVentanaCompra(_perso);
		}
		}
	}

	private void analizar_Claves(String packet) {
		switch (packet.charAt(1)) {
		case 'V': {
			Casa.cerrarVentana(_perso);
			break;
		}
		case 'K': {
			House_code(packet);
		}
		}
	}

	private void House_code(String packet) {
	    switch (packet.charAt(2)) {
	      case '0':
	        packet = packet.substring(4);
	        if (this._perso.get_savestat() > 0) {
	          try {
	            int code = 0;
	            code = Integer.parseInt(packet);
	            if (code < 0)
	              return; 
	            if (this._perso.getCapital() < code)
	              code = this._perso.getCapital(); 
	            this._perso.boostStatFixedCount(this._perso.get_savestat(), code);
	          } catch (Exception exception) {
	            break;
	          } finally {
	            this._perso.set_savestat(0);
	            SocketManager.ENVIAR_K_CLAVE(this._perso, "V");
	          } 
	          break;
	        } 
	        if (this._perso.getCofre() != null) {
	          Cofre.abrirCofre(this._perso, packet, false);
	          break;
	        } 
	        Casa.abrirCasa(this._perso, packet, false);
	        break;
	    } 
	  }

	private void analizar_Enemigos(String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			enemigo_Agregar(packet);
			break;
		}
		case 'D': {
			enemigo_Borrar(packet);
			break;
		}
		case 'L': {
			SocketManager.ENVIAR_iL_LISTA_ENEMIGOS(_perso);
		}
		}
	}

	private void enemigo_Agregar(String packet) {
		if (_perso == null) {
			return;
		}
		int id = -1;
		switch (packet.charAt(2)) {
		case '%': {
			packet = packet.substring(3);
			Personagens perso = World.getPjPorNombre(packet);
			if (perso == null) {
				SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
				return;
			}
			id = perso.getCuentaID();
			break;
		}
		case '*': {
			packet = packet.substring(3);
			Conta cuenta = World.getCuentaPorApodo(packet);
			if (cuenta == null) {
				SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
				return;
			}
			id = cuenta.getID();
			break;
		}
		default: {
			packet = packet.substring(2);
			Personagens perso2 = World.getPjPorNombre(packet);
			if (perso2 == null || !perso2.enLinea()) {
				SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
				return;
			}
			id = perso2.getCuenta().getID();
		}
		}
		_cuenta.addEnemigo(packet, id);
	}

	private void enemigo_Borrar(String packet) {
		int id = -1;
		switch (packet.charAt(2)) {
		case '%': {
			packet = packet.substring(3);
			Personagens pj = World.getPjPorNombre(packet);
			if (pj == null) {
				SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
				return;
			}
			id = pj.getCuentaID();
			break;
		}
		case '*': {
			packet = packet.substring(3);
			Conta cuenta = World.getCuentaPorApodo(packet);
			if (cuenta == null) {
				SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
				return;
			}
			id = cuenta.getID();
			break;
		}
		default: {
			packet = packet.substring(2);
			Personagens perso = World.getPjPorNombre(packet);
			if (perso == null || !perso.enLinea()) {
				SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
				return;
			}
			id = perso.getCuenta().getID();
		}
		}
		_cuenta.borrarEnemigo(id);
	}

	private void analizar_Oficios(String packet) {
		switch (packet.charAt(1)) {
		case 'O': {
			String[] infos = packet.substring(2).split("\\|");
			int posOficio = Integer.parseInt(infos[0]);
			int opciones = Integer.parseInt(infos[1]);
			int slots = Integer.parseInt(infos[2]);
			Profissao.StatsOficio statOficio = _perso.getStatsOficios().get(posOficio);
			if (statOficio == null) {
				return;
			}
			statOficio.setOpciones(opciones);
			statOficio.setSlotsPublico(slots);
			SocketManager.ENVIAR_JO_OFICIO_OPCIONES(_perso, statOficio);
		}
		}
	}

	private void analizar_Areas(String packet) {
		switch (packet.charAt(1)) {
		case 'U': {
			zaap_Usar(packet);
			break;
		}
		case 'u': {
			zaapi_Usar(packet);
			break;
		}
		case 'v': {
			zaapi_Cerrar();
			break;
		}
		case 'V': {
			zaap_Cerrar();
			break;
		}
		case 'w': {
			prisma_Cerrar();
			break;
		}
		case 'p': {
			prisma_Usar(packet);
		}
		}
	}

	private void zaapi_Cerrar() {
		_perso.cerrarZaapi();
	}

	private void prisma_Cerrar() {
		_perso.cerrarPrisma();
	}

	private void zaapi_Usar(String packet) {
		if (_perso.getDeshonor() >= 2) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "183");
			return;
		}
		_perso.usarZaapi(packet);
	}

	private void prisma_Usar(String packet) {
		if (_perso.getDeshonor() >= 1) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "183");
			return;
		}
		_perso.usarPrisma(packet);
	}

	private void zaap_Cerrar() {
		_perso.cerrarZaap();
	}

	private void zaap_Usar(String packet) {
		if (_perso.getDeshonor() >= 3) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "183");
			return;
		}
		short id = -1;
		try {
			id = Short.parseShort(packet.substring(2));
		} catch (Exception exception) {
			// empty catch block
		}
		if (id == -1) {
			return;
		}
		_perso.usarZaap(id);
	}

	private void analizar_Gremio(String packet) {
		switch (packet.charAt(1)) {
		case 'B': {
			gremio_Stats(packet);
			break;
		}
		case 'b': {
			gremio_Hechizos(packet);
			break;
		}
		case 'C': {
			gremio_Crear(packet);
			break;
		}
		case 'f': {
			gremio_Cercado(packet.substring(2));
			break;
		}
		case 'F': {
			gremio_Borrar_Recaudador(packet.substring(2));
			break;
		}
		case 'h': {
			gremio_Casa(packet.substring(2));
			break;
		}
		case 'H': {
			gremio_Poner_Recaudador();
			break;
		}
		case 'I': {
			gremio_Informacion(packet.charAt(2));
			break;
		}
		case 'J': {
			gremio_Unirse(packet.substring(2));
			break;
		}
		case 'K': {
			gremio_Expulsar(packet.substring(2));
			break;
		}
		case 'P': {
			gremio_Promover_Rango(packet.substring(2));
			break;
		}
		case 'T': {
			gremio_Unirse_Pelea_Recaudador(packet.substring(2));
			break;
		}
		case 'V': {
			gremio_CancelarCreacion();
		}
		}
	}

	private void gremio_Stats(String packet) {
		if (_perso.getGremio() == null) {
			return;
		}
		Guild gremio = _perso.getGremio();
		if (!_perso.getMiembroGremio().puede(Constantes.G_MODIFBOOST)) {
			return;
		}
		switch (packet.charAt(2)) {
		case 'p': {
			if (gremio.getCapital() < 1) {
				return;
			}
			if (gremio.getStats(176) >= 500) {
				return;
			}
			gremio.setCapital(gremio.getCapital() - 1);
			gremio.actualizarStats(176, 1);
			break;
		}
		case 'x': {
			if (gremio.getCapital() < 1) {
				return;
			}
			if (gremio.getStats(124) >= 400) {
				return;
			}
			gremio.setCapital(gremio.getCapital() - 1);
			gremio.actualizarStats(124, 1);
			break;
		}
		case 'o': {
			if (gremio.getCapital() < 1) {
				return;
			}
			if (gremio.getStats(158) >= 5000) {
				return;
			}
			gremio.setCapital(gremio.getCapital() - 1);
			gremio.actualizarStats(158, 20);
			break;
		}
		case 'k': {
			if (gremio.getCapital() < 10) {
				return;
			}
			if (gremio.getNroRecau() >= 50) {
				return;
			}
			gremio.setCapital(gremio.getCapital() - 10);
			gremio.setNroRecau(gremio.getNroRecau() + 1);
		}
		}
		SocketManager.ENVIAR_gIB_GREMIO_INFO_BOOST(_perso, _perso.getGremio().analizarRecauAGrmio());
	}

	private void gremio_Hechizos(String packet) {
		if (_perso.getGremio() == null) {
			return;
		}
		Guild gremio = _perso.getGremio();
		if (!_perso.getMiembroGremio().puede(Constantes.G_MODIFBOOST)) {
			return;
		}
		int hechizoID = Integer.parseInt(packet.substring(2));
		if (gremio.getHechizos().containsKey(hechizoID)) {
			if (gremio.getCapital() < 5) {
				return;
			}
			gremio.setCapital(gremio.getCapital() - 5);
			gremio.boostHechizo(hechizoID);
			SocketManager.ENVIAR_gIB_GREMIO_INFO_BOOST(_perso, _perso.getGremio().analizarRecauAGrmio());
		}
	}

	private void gremio_Unirse_Pelea_Recaudador(String packet) {
		switch (packet.charAt(0)) {
		case 'J': {
			int recauID = Integer.parseInt(packet.substring(1));
			Coletor recau = World.getRecaudador(recauID);
			if (recau == null) {
				return;
			}
			short mapaID = recau.getMapaID();
			short celdaID = recau.getCeldalID();
			if (_perso.getPelea() != null) {
				return;
			}
			if (!recau.getPelea().unirsePeleaRecaudador(_perso, recauID, mapaID, celdaID))
				break;
			for (Personagens miembros : _perso.getGremio().getPjMiembros()) {
				if (miembros == null || !miembros.enLinea())
					continue;
				Coletor.analizarDefensa(miembros, _perso.getGremio().getID());
			}
			break;
		}
		}
	}

	private void gremio_Borrar_Recaudador(String packet) {
		if (_perso.getGremio() == null) {
			return;
		}
		if (!_perso.getMiembroGremio().puede(Constantes.G_RECOLECTARRECAUDADOR)) {
			return;
		}
		int recauID = Integer.parseInt(packet);
		Coletor recau = World.getRecaudador(recauID);
		if (recau == null || recau.getEstadoPelea() > 0) {
			return;
		}
		SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_perso.getMapa(), recauID);
		recau.borrarRecauPorRecolecta(recau.getID(), _perso);
		for (Personagens z : _perso.getGremio().getPjMiembros()) {
			if (z == null || !z.enLinea())
				continue;
			SocketManager.ENVIAR_gITM_INFO_RECAUDADOR(z, Coletor.analizarRecaudadores(z.getGremio().getID()));
			String str = "";
			str = String.valueOf(str) + "R" + recau.getN1() + "," + recau.getN2() + "|";
			str = String.valueOf(str) + recau.getMapaID() + "|";
			str = String.valueOf(str) + World.getMapa(recau.getMapaID()).getX() + "|"
					+ World.getMapa(recau.getMapaID()).getY() + "|" + _perso.getNombre();
			SocketManager.GAME_SEND_gT_PACKET(z, str);
		}
	}

	private void gremio_Poner_Recaudador() {
		Guild gremio = _perso.getGremio();
		if (gremio == null || !_perso.getMiembroGremio().puede(Constantes.G_PONERRECAUDADOR)
				|| gremio.getPjMiembros().size() < 10) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
		short precio = (short) (1000 + 10 * gremio.getNivel());
		if (_perso.getKamas() < (long) precio) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "182");
			return;
		}
		Maps mapa = _perso.getMapa();
		if (Coletor.getIDGremioPorMapaID(mapa.getID()) > 0) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1168;1");
			return;
		}
		if (mapa.getPosicionesPelea().length() < 5 || mapa.getID() == 11095) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "113");
			return;
		}
		if (World.cantRecauDelGremio(gremio.getID()) >= gremio.getNroRecau()) {
			return;
		}
		String random1 = Integer.toString(Fórmulas.getRandomValor(1, 129), 36);
		String random2 = Integer.toString(Fórmulas.getRandomValor(1, 227), 36);
		int id = World.getSigIDRecaudador();
		Coletor recaudador = new Coletor(id, mapa.getID(), _perso.getCelda().getID(), (byte) 3, gremio.getID(), random1,
				random2, "", 0L, 0L);
		World.addRecaudador(recaudador);
		SocketManager.ENVIAR_GM_AGREGAR_RECAUDADOR_AL_MAPA(mapa);
		for (Personagens z : gremio.getPjMiembros()) {
			if (z == null || !z.enLinea())
				continue;
			SocketManager.ENVIAR_gITM_INFO_RECAUDADOR(z, Coletor.analizarRecaudadores(gremio.getID()));
			String str = "";
			str = String.valueOf(str) + "S" + recaudador.getN1() + "," + recaudador.getN2() + "|";
			str = String.valueOf(str) + recaudador.getMapaID() + "|";
			str = String.valueOf(str) + World.getMapa(recaudador.getMapaID()).getX() + "|"
					+ World.getMapa(recaudador.getMapaID()).getY() + "|" + _perso.getNombre();
			SocketManager.GAME_SEND_gT_PACKET(z, str);
		}
	}

	private void gremio_Cercado(String packet) {
		if (_perso == null) {
			return;
		}
		if (_perso.getGremio() == null) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1135");
			return;
		}
		short mapaID = Short.parseShort(packet);
		Maps.Cercado cercado = World.getMapa(mapaID).getCercado();
		if (cercado.getGremio().getID() != _perso.getGremio().getID()) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1135");
			return;
		}
		short celdaID = World.getCeldaCercadoPorMapaID(mapaID);
		if (!_perso.tieneObjModeloNoEquip(9035, 1)) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1159");
			return;
		}
		_perso.removerObjetoPorModYCant(9035, 1);
		_perso.teleport(mapaID, celdaID);
	}

	private void gremio_Casa(String packet) {
		if (_perso.getGremio() == null) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1135");
			return;
		}
		if (_perso.getPelea() != null || _perso.estaOcupado()) {
			return;
		}
		int idCasa = Integer.parseInt(packet);
		Casa casa = World.getCasas().get(idCasa);
		if (casa == null) {
			return;
		}
		if (_perso.getGremio().getID() != casa.getGremioID()) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1135");
			return;
		}
		if (!casa.tieneDerecho(Constantes.H_TELEPORTGREMIO)) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1136");
			return;
		}
		if (!_perso.tieneObjModeloNoEquip(8883, 1)) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1137");
			return;
		}
		_perso.removerObjetoPorModYCant(8883, 1);
		_perso.teleport(casa.getMapaIDDentro(), casa.getCeldaIDDentro());
	}

	private void gremio_Promover_Rango(String packet) {
		if (_perso.getGremio() == null) {
			return;
		}
		String[] infos = packet.split("\\|");
		int id = Integer.parseInt(infos[0]);
		int rango = Integer.parseInt(infos[1]);
		int xpDonada = Byte.parseByte(infos[2]);
		int derecho = Integer.parseInt(infos[3]);
		Personagens perso = World.getPersonaje(id);
		Guild.MiembroGremio cambiador = _perso.getMiembroGremio();
		if (perso == null || perso.getGremio() == null) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
		if (_perso.getGremio().getID() != perso.getGremio().getID()) {
			SocketManager.ENVIAR_gK_GREMIO_BAN(_perso, "Ea");
			return;
		}
		Guild.MiembroGremio aCambiar = perso.getMiembroGremio();
		if (cambiador.getRango() == 1) {
			if (cambiador.getID() == aCambiar.getID()) {
				rango = -1;
				derecho = -1;
			} else if (rango == 1) {
				cambiador.setTodosDerechos(2, (byte) -1, 29694);
				rango = 1;
				xpDonada = -1;
				derecho = 1;
			}
		} else {
			if (aCambiar.getRango() == 1) {
				rango = -1;
				derecho = -1;
			} else {
				if (!cambiador.puede(Constantes.G_MODRANGOS) || rango == 1) {
					rango = -1;
				}
				if (!cambiador.puede(Constantes.G_MODIFDERECHOS) || derecho == 1) {
					derecho = -1;
				}
				if (!cambiador.puede(Constantes.G_SUXPDONADA) && !cambiador.puede(Constantes.G_TODASXPDONADAS)
						&& cambiador.getID() == aCambiar.getID()) {
					xpDonada = -1;
				}
			}
			if (!cambiador.puede(Constantes.G_TODASXPDONADAS) && !cambiador.equals(aCambiar)) {
				xpDonada = -1;
			}
		}
		aCambiar.setTodosDerechos(rango, (byte) xpDonada, derecho);
		SocketManager.ENVIAR_gS_STATS_GREMIO(_perso, _perso.getMiembroGremio());
		if (perso != null && perso.getID() != _perso.getID()) {
			SocketManager.ENVIAR_gS_STATS_GREMIO(perso, perso.getMiembroGremio());
		}
	}

	private void gremio_CancelarCreacion() {
		SocketManager.ENVIAR_gV_CERRAR_PANEL_GREMIO(_perso);
	}

	private void gremio_Expulsar(String nombre) {
		Guild.MiembroGremio aExpulsar;
		if (_perso.getGremio() == null) {
			return;
		}
		Personagens perso = World.getPjPorNombre(nombre);
		if (perso == null) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
		Guild gremio = perso.getGremio();
		if (gremio == null) {
			gremio = World.getGremio(_perso.getGremio().getID());
		}
		if ((aExpulsar = gremio.getMiembro(perso.getID())) == null
				|| aExpulsar.getGremio().getID() != _perso.getGremio().getID()) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
		if (gremio.getID() != _perso.getGremio().getID()) {
			SocketManager.ENVIAR_gK_GREMIO_BAN(_perso, "Ea");
			return;
		}
		Guild.MiembroGremio expulsador = _perso.getMiembroGremio();
		if (!expulsador.puede(Constantes.G_BANEAR) && expulsador.getID() != aExpulsar.getID()) {
			SocketManager.ENVIAR_gK_GREMIO_BAN(_perso, "Ed");
			return;
		}
		if (expulsador.getID() != aExpulsar.getID()) {
			if (aExpulsar.getRango() == 1) {
				return;
			}
			gremio.expulsarMiembro(aExpulsar.getPerso());
			if (perso != null) {
				perso.setMiembroGremio(null);
			}
			SocketManager.ENVIAR_gK_GREMIO_BAN(_perso, "K" + _perso.getNombre() + "|" + nombre);
			if (perso != null) {
				SocketManager.ENVIAR_gK_GREMIO_BAN(perso, "K" + _perso.getNombre());
			}
		} else {
			if (expulsador.getRango() == 1 && gremio.getPjMiembros().size() > 1) {
				for (Personagens pj : gremio.getPjMiembros()) {
					gremio.expulsarMiembro(pj);
					pj.setMiembroGremio(null);
				}
			} else {
				gremio.expulsarMiembro(_perso);
				_perso.setMiembroGremio(null);
			}
			if (gremio.getPjMiembros().isEmpty()) {
				World.eliminarGremio(gremio.getID());
			}
			SocketManager.ENVIAR_gK_GREMIO_BAN(_perso, "K" + nombre + "|" + nombre);
		}
	}

	private void gremio_Unirse(String packet) {
		switch (packet.charAt(0)) {
		case 'R': {
			Personagens perso = World.getPjPorNombre(packet.substring(1));
			if (perso == null || _perso.getGremio() == null) {
				SocketManager.ENVIAR_gJ_GREMIO_UNIR(_perso, "Eu");
				return;
			}
			if (!perso.enLinea()) {
				SocketManager.ENVIAR_gJ_GREMIO_UNIR(_perso, "Eu");
				return;
			}
			if (perso.estaOcupado()) {
				SocketManager.ENVIAR_gJ_GREMIO_UNIR(_perso, "Eo");
				return;
			}
			if (perso.getGremio() != null) {
				SocketManager.ENVIAR_gJ_GREMIO_UNIR(_perso, "Ea");
				return;
			}
			if (!_perso.getMiembroGremio().puede(Constantes.G_INVITAR)) {
				SocketManager.ENVIAR_gJ_GREMIO_UNIR(_perso, "Ed");
				return;
			}
			if (_perso.getGremio().getPjMiembros().size() >= 40 + _perso.getGremio().getNivel()) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "155;" + (40 + _perso.getGremio().getNivel()));
				return;
			}
			_perso.setInvitado(perso.getID());
			perso.setInvitado(_perso.getID());
			SocketManager.ENVIAR_gJ_GREMIO_UNIR(_perso, "R" + packet.substring(1));
			SocketManager.ENVIAR_gJ_GREMIO_UNIR(perso,
					"r" + _perso.getID() + "|" + _perso.getNombre() + "|" + _perso.getGremio().getNombre());
			break;
		}
		case 'E': {
			if (_perso == null) {
				return;
			}
			if (_perso.getInvitado() == 0) {
				return;
			}
			SocketManager.ENVIAR_BN_NADA(_out);
			Personagens invitado = World.getPersonaje(_perso.getInvitado());
			if (invitado == null) {
				return;
			}
			SocketManager.ENVIAR_gJ_GREMIO_UNIR(_perso, "Ec");
			SocketManager.ENVIAR_gJ_GREMIO_UNIR(invitado, "Ec");
			invitado.setInvitado(0);
			_perso.setInvitado(0);
			break;
		}
		case 'K': {
			if (!packet.substring(1).equalsIgnoreCase(String.valueOf(_perso.getInvitado())))
				break;
			Personagens invitado2 = World.getPersonaje(_perso.getInvitado());
			if (invitado2 == null) {
				return;
			}
			Guild gremio = invitado2.getGremio();
			if (gremio == null) {
				return;
			}
			Guild.MiembroGremio miembro = gremio.addNuevoMiembro(_perso);
			_perso.setMiembroGremio(miembro);
			_perso.setInvitado(-1);
			invitado2.setInvitado(-1);
			SocketManager.ENVIAR_gJ_GREMIO_UNIR(invitado2, "Ka" + _perso.getNombre());
			SocketManager.ENVIAR_gS_STATS_GREMIO(_perso, miembro);
			SocketManager.ENVIAR_gJ_GREMIO_UNIR(_perso, "Kj");
		}
		}
	}

	private void gremio_Informacion(char c) {
		Guild gremio = _perso.getGremio();
		switch (c) {
		case 'B': {
			SocketManager.ENVIAR_gIB_GREMIO_INFO_BOOST(_perso, gremio.analizarRecauAGrmio());
			break;
		}
		case 'F': {
			SocketManager.ENVIAR_gIF_GREMIO_INFO_CERCADOS(_perso, gremio.analizarInfoCercados());
			break;
		}
		case 'G': {
			SocketManager.ENVIAR_gIG_GREMIO_INFO_GENERAL(_perso, gremio);
			break;
		}
		case 'H': {
			SocketManager.ENVIAR_gIH_GREMIO_INFO_CASAS(_perso, Casa.analizarCasaGremio(_perso));
			break;
		}
		case 'M': {
			SocketManager.ENVIAR_gIM_INFO_MIEMBROS_GREMIO(_perso, gremio, '+');
			break;
		}
		case 'T': {
			SocketManager.ENVIAR_gITM_INFO_RECAUDADOR(_perso, Coletor.analizarRecaudadores(gremio.getID()));
			Coletor.analizarAtaque(_perso, gremio.getID());
			Coletor.analizarDefensa(_perso, gremio.getID());
		}
		}
	}

	private void gremio_Crear(String packet) {
		if (_perso == null || _perso.getPelea() != null) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
		if (_perso.getGremio() != null || _perso.getMiembroGremio() != null) {
			SocketManager.ENVIAR_gC_CREAR_PANEL_GREMIO(_perso, "Ea");
			return;
		}
		try {
			String[] infos = packet.substring(2).split("\\|");
			String escudoId = Integer.toString(Integer.parseInt(infos[0]), 36);
			String colorEscudo = Integer.toString(Integer.parseInt(infos[1]), 36);
			String emblemaId = Integer.toString(Integer.parseInt(infos[2]), 36);
			String colorEmblema = Integer.toString(Integer.parseInt(infos[3]), 36);
			String nombre = infos[4];
			if (World.nombreGremioUsado(nombre)) {
				SocketManager.ENVIAR_gC_CREAR_PANEL_GREMIO(_perso, "Ean");
				return;
			}
			String nombreTemp = nombre.toLowerCase();
			boolean esValido = true;
			if (nombreTemp.length() > 20) {
				esValido = false;
			}
			if (esValido) {
				int cantidadLetras = 0;
				for (char letra : nombreTemp.toCharArray()) {
					if ((letra < 'a' || letra > 'z') && letra != '-') {
						esValido = false;
						break;
					}
					if (letra != '-')
						continue;
					if (cantidadLetras >= 2) {
						esValido = false;
						break;
					}
					++cantidadLetras;
				}
			}
			if (!esValido) {
				SocketManager.ENVIAR_gC_CREAR_PANEL_GREMIO(_perso, "Ean");
				return;
			}
			String emblema = String.valueOf(escudoId) + "," + colorEscudo + "," + emblemaId + "," + colorEmblema;
			if (World.emblemaGremioUsado(emblema)) {
				SocketManager.ENVIAR_gC_CREAR_PANEL_GREMIO(_perso, "Eae");
				return;
			}
			if (_perso.getMapa().getID() == 2196) {
				if (!_perso.tieneObjModeloNoEquip(1575, 1)) {
					SocketManager.ENVIAR_Im_INFORMACION(_out, "14");
					return;
				}
				_perso.removerObjetoPorModYCant(1575, 1);
			}
			Guild gremio = new Guild(_perso, nombre, emblema);
			World.addGremio(gremio);
			SQLManager.INSERT_GREMIO(gremio);
			Guild.MiembroGremio miembro = gremio.addNuevoMiembro(_perso);
			miembro.setTodosDerechos(1, (byte) 0, 1);
			_perso.setMiembroGremio(miembro);
			SocketManager.ENVIAR_gS_STATS_GREMIO(_perso, miembro);
			SocketManager.ENVIAR_gC_CREAR_PANEL_GREMIO(_perso, "K");
			SocketManager.ENVIAR_gV_CERRAR_PANEL_GREMIO(_perso);
		} catch (Exception e) {
			return;
		}
	}

	private void analizar_Canal(String packet) {
		switch (packet.charAt(1)) {
		case 'C': {
			canal_Cambiar(packet);
		}
		}
	}

	private void canal_Cambiar(String packet) {
		String canal = String.valueOf(packet.charAt(3));
		switch (packet.charAt(2)) {
		case '+': {
			_perso.addCanal(canal);
			break;
		}
		case '-': {
			_perso.removerCanal(canal);
		}
		}
	}

	private void analizar_Montura(String packet) {
		switch (packet.charAt(1)) {
		case 'b': {
			montura_Comprar_Cercado(packet);
			break;
		}
		case 'd': {
			montura_Descripcion(packet);
			break;
		}
		case 'c': {
			montura_Castrar();
			break;
		}
		case 'p': {
			montura_Descripcion(packet);
			break;
		}
		case 'f': {
			montura_Liberar();
			break;
		}
		case 'n': {
			montura_Nombre(packet.substring(2));
			break;
		}
		case 'o': {
			montura_Borrar_Objeto_Crianza(packet);
			break;
		}
		case 'r': {
			montura_Montar();
			break;
		}
		case 's': {
			montura_Vender_Cercado(packet);
			break;
		}
		case 'v': {
			SocketManager.ENVIAR_Rv_MONTURA_CERRAR(_out);
			break;
		}
		case 'x': {
			montura_CambiarXP_Donada(packet);
		}
		}
	}

	private void montura_Vender_Cercado(String packet) {
		SocketManager.ENVIAR_Rv_MONTURA_CERRAR(_out);
		int precio = Integer.parseInt(packet.substring(2));
		Maps.Cercado cercado = _perso.getMapa().getCercado();
		if (cercado.getDue\u00f1o() == -1) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "194");
			return;
		}
		if (cercado.getDue\u00f1o() != _perso.getID()) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "195");
			return;
		}
		cercado.setPrecio(precio);
		SQLManager.UPDATE_CERCADO(cercado);
		for (Personagens z : _perso.getMapa().getPersos()) {
			SocketManager.ENVIAR_Rp_INFORMACION_CERCADO(z, cercado);
		}
	}

	private void montura_Comprar_Cercado(String packet) {
		SocketManager.ENVIAR_Rv_MONTURA_CERRAR(_out);
		Maps.Cercado cercado = _perso.getMapa().getCercado();
		Personagens vendedor = World.getPersonaje(cercado.getDue\u00f1o());
		if (cercado.getDue\u00f1o() == -1) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "196");
			return;
		}
		if (cercado.getPrecio() == 0) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "197");
			return;
		}
		if (_perso.getGremio() == null) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1135");
			return;
		}
		if (_perso.getMiembroGremio().getRango() != 1) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "198");
			return;
		}
		byte cercadosMax = (byte) Math.floor(_perso.getGremio().getNivel() / 10);
		byte cercadosTotalGremio = SQLManager.TOTAL_CERCADOS_GREMIO(_perso.getGremio().getID());
		if (cercadosTotalGremio >= cercadosMax) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1103");
			return;
		}
		if (_perso.getKamas() < (long) cercado.getPrecio()) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "182");
			return;
		}
		long nuevasKamas = _perso.getKamas() - (long) cercado.getPrecio();
		_perso.setKamas(nuevasKamas);
		if (vendedor != null) {
			long nuevasKamasBanco = vendedor.getKamasBanco() + (long) cercado.getPrecio();
			vendedor.setKamasBanco(nuevasKamasBanco);
			if (vendedor.enLinea()) {
				SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
						"Est\u00e1 casa foi vendida por " + cercado.getPrecio() + ".", LesGuardians.COR_MSG);
			}
		}
		cercado.setPrecio(0);
		cercado.setPropietario(_perso.getID());
		cercado.setGremio(_perso.getGremio());
		SQLManager.UPDATE_CERCADO(cercado);
		for (Personagens pj : _perso.getMapa().getPersos()) {
			SocketManager.ENVIAR_Rp_INFORMACION_CERCADO(pj, cercado);
		}
	}

	private void montura_CambiarXP_Donada(String packet) {
		int xp = Integer.parseInt(packet.substring(2));
		if (xp < 0) {
			xp = 0;
		}
		if (xp > 90) {
			xp = 90;
		}
		_perso.setDonarXPMontura(xp);
		SocketManager.ENVIAR_Rx_EXP_DONADA_MONTURA(_perso);
	}

	private void montura_Borrar_Objeto_Crianza(String packet) {
		short celda = Short.parseShort(packet.substring(2));
		Maps mapa = _perso.getMapa();
		if (mapa.getCercado() == null) {
			return;
		}
		Maps.Cercado cercado = mapa.getCercado();
		if (_perso.getNombre() != "Les Guardians") {
			if (_perso.getGremio() == null) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			if (!_perso.getMiembroGremio().puede(8192)) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "193");
				return;
			}
		}
		if (cercado.delObjetoCria(celda)) {
			SocketManager.ENVIAR_GDO_PONER_OBJETO_CRIA(mapa, String.valueOf(celda) + ";0;0");
			return;
		}
	}

	private void montura_Nombre(String nombre) {
		if (_perso == null) {
			return;
		}
		if (_perso.getMontura() == null) {
			return;
		}
		_perso.getMontura().setNombre(nombre);
		SocketManager.ENVIAR_Rn_CAMBIO_NOMBRE_MONTURA(_perso, nombre);
	}

	private void montura_Montar() {
		if (_perso.getNivel() < 60 || _perso.getMontura() == null || _perso.getMontura().esMontable() == 0
				|| _perso.esFantasma()) {
			SocketManager.ENVIAR_Re_DETALLES_MONTURA(_perso, "Er", null);
			return;
		}
		_perso.subirBajarMontura();
	}

	private void montura_Castrar() {
		if (_perso.getMontura() == null) {
			SocketManager.ENVIAR_Re_DETALLES_MONTURA(_perso, "Er", null);
			return;
		}
		_perso.getMontura().castrarPavo();
		SocketManager.ENVIAR_Re_DETALLES_MONTURA(_perso, "+", _perso.getMontura());
	}

	private void montura_Liberar() {
		if (_perso.getMontura() == null) {
			SocketManager.ENVIAR_Re_DETALLES_MONTURA(_perso, "Er", null);
		} else {
			Dragossauros drago = _perso.getMontura();
			_perso.setMontura(null);
			SQLManager.DELETE_DRAGOPAVO(drago);
			World.borrarDragopavo(drago.getID());
		}
	}

	private void montura_Descripcion(String packet) {
		Dragossauros DD;
		int DPid = -1;
		try {
			DPid = Integer.parseInt(packet.substring(2).split("\\|")[0]);
		} catch (Exception exception) {
			// empty catch block
		}
		if (DPid == -1) {
			return;
		}
		if (DPid > 0) {
			DPid = -DPid;
		}
		if ((DD = World.getDragopavoPorID(DPid)) == null) {
			return;
		}
		SocketManager.ENVIAR_Rd_DESCRIPCION_MONTURA(_perso, DD);
	}

	private void analizar_Amigos(String packet) {
		block0: switch (packet.charAt(1)) {
		case 'A': {
			amigo_Agregar(packet);
			break;
		}
		case 'D': {
			amigo_Borrar(packet);
			break;
		}
		case 'L': {
			SocketManager.ENVIAR_FL_LISTA_DE_AMIGOS(_perso);
			break;
		}
		case 'O': {
			switch (packet.charAt(2)) {
			case '-': {
				_perso.mostrarAmigosEnLinea(false);
				SocketManager.ENVIAR_BN_NADA(_out);
				break block0;
			}
			case '+': {
				_perso.mostrarAmigosEnLinea(true);
				SocketManager.ENVIAR_BN_NADA(_out);
			}
			}
			break;
		}
		case 'J': {
			amigo_Esposo(packet);
		}
		}
	}

	private void amigo_Esposo(String packet) {
		Personagens esposo = World.getPersonaje(_perso.getEsposo());
		if (esposo == null) {
			return;
		}
		if (!esposo.enLinea()) {
			SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso, "Seu amor n\u00e3o est\u00e1 online.", LesGuardians.COR_MSG);
			SocketManager.ENVIAR_FL_LISTA_DE_AMIGOS(_perso);
			return;
		}
		switch (packet.charAt(2)) {
		case 'S': {
			if (_perso.getPelea() != null) {
				return;
			}
			_perso.casarse(esposo);
			break;
		}
		case 'C': {
			if (packet.charAt(3) == '+') {
				if (_perso.getSiguiendo() != null) {
					_perso.getSiguiendo().getSeguidores().remove(_perso.getID());
				}
				SocketManager.ENVIAR_IC_PERSONAJE_BANDERA_COMPAS(_perso, esposo);
				_perso.setSiguiendo(esposo);
				esposo.getSeguidores().put(_perso.getID(), _perso);
				break;
			}
			SocketManager.ENVIAR_IC_BORRAR_BANDERA_COMPAS(_perso);
			_perso.setSiguiendo(null);
			esposo.getSeguidores().remove(_perso.getID());
		}
		}
	}

	private void amigo_Borrar(String packet) {
		if (_perso == null) {
			return;
		}
		int id = -1;
		switch (packet.charAt(2)) {
		case '%': {
			packet = packet.substring(3);
			Personagens P = World.getPjPorNombre(packet);
			if (P == null) {
				SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
				return;
			}
			id = P.getCuentaID();
			break;
		}
		case '*': {
			packet = packet.substring(3);
			Conta C = World.getCuentaPorApodo(packet);
			if (C == null) {
				SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
				return;
			}
			id = C.getID();
			break;
		}
		default: {
			packet = packet.substring(2);
			Personagens Pj = World.getPjPorNombre(packet);
			if (Pj == null || !Pj.enLinea()) {
				SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
				return;
			}
			id = Pj.getCuenta().getID();
		}
		}
		if (id == -1 || !_cuenta.esAmigo(id)) {
			SocketManager.ENVIAR_FD_BORRAR_AMIGO(_perso, "Ef");
			return;
		}
		_cuenta.borrarAmigo(id);
	}

	private void amigo_Agregar(String packet) {
		if (_perso == null) {
			return;
		}
		int id = -1;
		switch (packet.charAt(2)) {
		case '%': {
			packet = packet.substring(3);
			Personagens perso = World.getPjPorNombre(packet);
			if (perso == null || !perso.enLinea()) {
				SocketManager.ENVIAR_FA_AGREGAR_AMIGO(_perso, "Ef");
				return;
			}
			id = perso.getCuentaID();
			break;
		}
		case '*': {
			packet = packet.substring(3);
			Conta cuenta = World.getCuentaPorApodo(packet);
			if (cuenta == null || !cuenta.enLinea()) {
				SocketManager.ENVIAR_FA_AGREGAR_AMIGO(_perso, "Ef");
				return;
			}
			id = cuenta.getID();
			break;
		}
		default: {
			packet = packet.substring(2);
			Personagens Pj = World.getPjPorNombre(packet);
			if (Pj == null || !Pj.enLinea()) {
				SocketManager.ENVIAR_FA_AGREGAR_AMIGO(_perso, "Ef");
				return;
			}
			id = Pj.getCuenta().getID();
		}
		}
		if (id == -1) {
			SocketManager.ENVIAR_FA_AGREGAR_AMIGO(_perso, "Ef");
			return;
		}
		_cuenta.addAmigo(id);
	}

	private void analizar_Grupo(String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			grupo_Aceptar(packet);
			break;
		}
		case 'F': {
			Personagens.Grupo g = _perso.getGrupo();
			if (g == null) {
				return;
			}
			int pId = -1;
			try {
				pId = Integer.parseInt(packet.substring(3));
			} catch (NumberFormatException e) {
				return;
			}
			if (pId == -1) {
				return;
			}
			Personagens perso = World.getPersonaje(pId);
			if (perso == null || !perso.enLinea()) {
				return;
			}
			if (packet.charAt(2) == '+') {
				if (_perso.getSiguiendo() != null) {
					_perso.getSiguiendo().getSeguidores().remove(_perso.getID());
				}
				SocketManager.ENVIAR_IC_PERSONAJE_BANDERA_COMPAS(_perso, perso);
				SocketManager.ENVIAR_PF_SEGUIR_PERSONAJE(_perso, "+" + perso.getID());
				_perso.setSiguiendo(perso);
				perso.getSeguidores().put(_perso.getID(), _perso);
				break;
			}
			if (packet.charAt(2) != '-')
				break;
			SocketManager.ENVIAR_IC_BORRAR_BANDERA_COMPAS(_perso);
			SocketManager.ENVIAR_PF_SEGUIR_PERSONAJE(_perso, "-");
			_perso.setSiguiendo(null);
			perso.getSeguidores().remove(_perso.getID());
			break;
		}
		case 'G': {
			Personagens.Grupo g2 = _perso.getGrupo();
			if (g2 == null) {
				return;
			}
			int pId2 = -1;
			try {
				pId2 = Integer.parseInt(packet.substring(3));
			} catch (NumberFormatException e) {
				return;
			}
			if (pId2 == -1) {
				return;
			}
			Personagens P2 = World.getPersonaje(pId2);
			if (P2 == null || !P2.enLinea()) {
				return;
			}
			if (packet.charAt(2) == '+') {
				for (Personagens integrante : g2.getPersos()) {
					if (integrante.getID() == P2.getID())
						continue;
					if (integrante.getSiguiendo() != null) {
						integrante.getSiguiendo().getSeguidores().remove(_perso.getID());
					}
					SocketManager.ENVIAR_IC_PERSONAJE_BANDERA_COMPAS(integrante, P2);
					SocketManager.ENVIAR_PF_SEGUIR_PERSONAJE(integrante, "+" + P2.getID());
					integrante.setSiguiendo(P2);
					P2.getSeguidores().put(integrante.getID(), integrante);
				}
			} else {
				if (packet.charAt(2) != '-')
					break;
				for (Personagens integrante : g2.getPersos()) {
					if (integrante.getID() == P2.getID())
						continue;
					SocketManager.ENVIAR_IC_BORRAR_BANDERA_COMPAS(integrante);
					SocketManager.ENVIAR_PF_SEGUIR_PERSONAJE(integrante, "-");
					integrante.setSiguiendo(null);
					P2.getSeguidores().remove(integrante.getID());
				}
			}
			break;
		}
		case 'I': {
			grupo_Invitar(packet);
			break;
		}
		case 'R': {
			grupo_Rechazar();
			break;
		}
		case 'V': {
			grupo_Expulsar(packet);
			break;
		}
		case 'W': {
			grupo_Localizar();
		}
		}
	}

	private void grupo_Localizar() {
		if (_perso == null) {
			return;
		}
		Personagens.Grupo grupo = _perso.getGrupo();
		if (grupo == null) {
			return;
		}
		String str = "";
		boolean primero = false;
		for (Personagens pj : grupo.getPersos()) {
			if (primero) {
				str = String.valueOf(str) + "|";
			}
			Maps mapa = pj.getMapa();
			str = String.valueOf(str) + mapa.getX() + ";" + mapa.getY() + ";" + mapa.getID() + ";2;" + pj.getID() + ";"
					+ pj.getNombre();
			primero = true;
		}
		SocketManager.ENVIAR_IH_COORDINAS_UBICACION(_perso, str);
	}

	private void grupo_Expulsar(String packet) {
		if (_perso == null) {
			return;
		}
		Personagens.Grupo grupo = _perso.getGrupo();
		if (grupo == null) {
			return;
		}
		if (packet.length() == 2) {
			grupo.dejarGrupo(_perso);
			SocketManager.ENVIAR_PV_DEJAR_GRUPO(_out, "");
			SocketManager.ENVIAR_IH_COORDINAS_UBICACION(_perso, "");
		} else if (grupo.esLiderGrupo(_perso.getID())) {
			int id = -1;
			try {
				id = Integer.parseInt(packet.substring(2));
			} catch (NumberFormatException e) {
				return;
			}
			if (id == -1) {
				return;
			}
			Personagens expulsado = World.getPersonaje(id);
			if (expulsado == null) {
				return;
			}
			grupo.dejarGrupo(expulsado);
			if (expulsado.enLinea()) {
				SocketManager.ENVIAR_PV_DEJAR_GRUPO(expulsado.getCuenta().getEntradaPersonaje().getOut(),
						String.valueOf(_perso.getID()));
				SocketManager.ENVIAR_IH_COORDINAS_UBICACION(expulsado, "");
			}
		}
	}

	private void grupo_Invitar(String packet) {
		if (_perso == null) {
			return;
		}
		String nombre = packet.substring(2);
		Personagens invitado = World.getPjPorNombre(nombre);
		if (invitado == null) {
			return;
		}
		if (!invitado.enLinea()) {
			SocketManager.ENVIAR_PIE_ERROR_INVITACION_GRUPO(_out, "n" + nombre);
			return;
		}
		if (invitado.getGrupo() != null) {
			SocketManager.ENVIAR_PIE_ERROR_INVITACION_GRUPO(_out, "a" + nombre);
			return;
		}
		if (_perso.getGrupo() != null && _perso.getGrupo().getNumeroPjs() == 8) {
			SocketManager.ENVIAR_PIE_ERROR_INVITACION_GRUPO(_out, "f");
			return;
		}
		invitado.setInvitado(_perso.getID());
		_perso.setInvitado(invitado.getID());
		SocketManager.ENVIAR_PIK_INVITAR_GRUPO(_out, _perso.getNombre(), nombre);
		SocketManager.ENVIAR_PIK_INVITAR_GRUPO(invitado.getCuenta().getEntradaPersonaje().getOut(), _perso.getNombre(),
				nombre);
	}

	private void grupo_Rechazar() {
		if (_perso == null) {
			return;
		}
		if (_perso.getInvitado() == 0) {
			return;
		}
		SocketManager.ENVIAR_BN_NADA(_out);
		Personagens t = World.getPersonaje(_perso.getInvitado());
		if (t == null) {
			return;
		}
		SocketManager.ENVIAR_PR_RECHAZAR_INVITACION_GRUPO(t);
		t.setInvitado(0);
		_perso.setInvitado(0);
	}

	private void grupo_Aceptar(String packet) {
		if (_perso == null) {
			return;
		}
		if (_perso.getInvitado() == 0) {
			return;
		}
		Personagens invitado = World.getPersonaje(_perso.getInvitado());
		if (invitado == null) {
			return;
		}
		Personagens.Grupo grupo = invitado.getGrupo();
		try {
			if (grupo == null) {
				PrintWriter out = invitado.getCuenta().getEntradaPersonaje().getOut();
				grupo = new Personagens.Grupo(invitado, _perso);
				SocketManager.ENVIAR_PCK_CREAR_GRUPO(_out, grupo);
				SocketManager.ENVIAR_PL_LIDER_GRUPO(_out, grupo);
				SocketManager.ENVIAR_PCK_CREAR_GRUPO(out, grupo);
				SocketManager.ENVIAR_PL_LIDER_GRUPO(out, grupo);
				invitado.setGrupo(grupo);
				SocketManager.ENVIAR_PM_TODOS_MIEMBROS_GRUPO(out, grupo);
			} else {
				SocketManager.ENVIAR_PCK_CREAR_GRUPO(_out, grupo);
				SocketManager.ENVIAR_PL_LIDER_GRUPO(_out, grupo);
				SocketManager.ENVIAR_PM_AGREGAR_PJ_GRUPO(grupo, _perso);
				grupo.addPerso(_perso);
			}
			_perso.setGrupo(grupo);
			SocketManager.ENVIAR_PM_TODOS_MIEMBROS_GRUPO(_out, grupo);
			SocketManager.ENVIAR_PR_RECHAZAR_INVITACION_GRUPO(invitado);
		} catch (NullPointerException e) {
			SocketManager.ENVIAR_BN_NADA(_out);
		}
	}

	private void analizar_Objetos(String packet) {
		switch (packet.charAt(1)) {
		case 'd': {
			objeto_Eliminar(packet);
			break;
		}
		case 'D': {
			objeto_Tirar(packet);
			break;
		}
		case 'M': {
			objeto_Mover(packet);
			break;
		}
		case 'U': {
			objeto_Usar(packet);
			break;
		}
		case 's': {
			aparienciaObjevivo(packet);
			break;
		}
		case 'f': {
			alimentarObjevivo(packet);
			break;
		}
		case 'x': {
			desequiparObjevivo(packet);
		}
		}
	}

	private synchronized void objeto_Tirar(String packet) {
		int id = -1;
		int cant = -1;
		try {
			id = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			cant = Integer.parseInt(packet.split("\\|")[1]);
		} catch (Exception exception) {
			// empty catch block
		}
		if (id == -1 || cant <= 0 || !_perso.tieneObjetoID(id)) {
			return;
		}
		Objeto obj = World.getObjeto(id);
		if (obj == null) {
			return;
		}
		int idObjModelo = obj.getModelo().getID();
		if (idObjModelo == 10085) {
			_perso.borrarObjetoSinEliminar(id);
			SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, id);
			World.eliminarObjeto(id);
			return;
		}
		short celdaDrop = Constantes.getCeldaIDCercanaNoUsada(_perso);
		if (celdaDrop == 0) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "1145");
			return;
		}
		Maps.Celda celdaTirar = _perso.getMapa().getCelda(celdaDrop);
		if (cant >= obj.getCantidad()) {
			_perso.borrarObjetoSinEliminar(id);
			celdaTirar.addObjetoTirado(obj, _perso);
			obj.setPosicion(-1);
			SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, id);
		} else {
			obj.setCantidad(obj.getCantidad() - cant);
			Objeto obj2 = Objeto.clonarObjeto(obj, cant);
			obj2.setPosicion(-1);
			World.addObjeto(obj2, false);
			celdaTirar.addObjetoTirado(obj2, _perso);
			SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, obj);
		}
		SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
		SocketManager.ENVIAR_GDO_OBJETO_TIRADO_AL_SUELO(_perso.getMapa(), '+', (int) celdaTirar.getID(), idObjModelo,
				0);
		SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
	}

	private synchronized void objeto_Usar(String packet) {
		int id = -1;
		int idPjObjetivo = -1;
		short celdaId = -1;
		Personagens pjObjetivo = null;
		try {
			String[] infos = packet.substring(2).split("\\|");
			id = Integer.parseInt(infos[0]);
			try {
				idPjObjetivo = Integer.parseInt(infos[1]);
			} catch (Exception e) {
				idPjObjetivo = -1;
			}
			try {
				celdaId = Short.parseShort(infos[2]);
			} catch (Exception e) {
				celdaId = -1;
			}
		} catch (Exception e) {
			return;
		}
		if (World.getPersonaje(idPjObjetivo) != null) {
			pjObjetivo = World.getPersonaje(idPjObjetivo);
		}
		if (!_perso.tieneObjetoID(id)) {
			return;
		}
		Objeto obj = World.getObjeto(id);
		if (obj == null) {
			return;
		}
		Objeto.ObjetoModelo objModeloBD = obj.getModelo();
		if (_perso.getPelea() != null && (_perso.getPelea().getEstado() > 2
				|| objModeloBD.getTipo() != 33 && objModeloBD.getTipo() != 69 && objModeloBD.getTipo() != 49)) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "191");
			return;
		}
		if (!objModeloBD.getCondiciones().equalsIgnoreCase("")
				&& !Condiciones.validaCondiciones(_cuenta, _perso, objModeloBD.getCondiciones())) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "119|43");
			return;
		}
		if ((objModeloBD.getTipo() == 33 || objModeloBD.getTipo() == 69 || objModeloBD.getTipo() == 49)
				&& _perso.getPelea() == null) {
			SocketManager.ENVIAR_eUK_EMOTE_MAPA(_perso.getMapa(), _perso.getID(), 17, "");
		} else if ((objModeloBD.getTipo() == 12 || objModeloBD.getTipo() == 43 || objModeloBD.getTipo() == 44
				|| objModeloBD.getTipo() == 45 || objModeloBD.getTipo() == 79) && _perso.getPelea() == null) {
			SocketManager.ENVIAR_eUK_EMOTE_MAPA(_perso.getMapa(), _perso.getID(), 18, "");
		}
		objModeloBD.aplicarAccion(_perso, pjObjetivo, id, celdaId);
	}

	private void objeto_Eliminar(String packet) {
		String[] infos = packet.substring(2).split("\\|");
		try {
			int id = Integer.parseInt(infos[0]);
			int cant = 1;
			try {
				cant = Integer.parseInt(infos[1]);
			} catch (Exception exception) {
				// empty catch block
			}
			Objeto obj = World.getObjeto(id);
			if (obj == null || !_perso.tieneObjetoID(id) || cant <= 0) {
				SocketManager.ENVIAR_OdE_ERROR_ELIMINAR_OBJETO(_out);
				return;
			}
			int nuevaCant = obj.getCantidad() - cant;
			if (nuevaCant <= 0) {
				_perso.borrarObjetoSinEliminar(id);
				World.eliminarObjeto(id);
				SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, id);
			} else {
				obj.setCantidad(nuevaCant);
				SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, obj);
			}
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
			SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
		} catch (Exception e) {
			SocketManager.ENVIAR_OdE_ERROR_ELIMINAR_OBJETO(_out);
		}
	}

	private synchronized void objeto_Mover(String packet) {
		String[] infos = packet.substring(2).split("\n")[0].split("\\|");
		try {
			Objeto exObj;
			String modificacion;
			String[] val;
			int n;
			int nuevaCant;
			int cantObjMover;
			int idObjMover = Integer.parseInt(infos[0]);
			int posAMover = Integer.parseInt(infos[1]);
			try {
				cantObjMover = Integer.parseInt(infos[2]);
			} catch (Exception e) {
				cantObjMover = 1;
			}
			Objeto objMover = World.getObjeto(idObjMover);
			if (!_perso.tieneObjetoID(idObjMover) || objMover == null) {
				return;
			}
			Fight pelea = _perso.getPelea();
			if (pelea != null && pelea.getEstado() > 2) {
				return;
			}
			Objeto.ObjetoModelo objetoMod = objMover.getModelo();
			if (objetoMod.getNivel() > _perso.getNivel()) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "13");
				return;
			}
			if (LesGuardians.ARMAS_ENCARNACAO.contains(objetoMod.getID())) {
				int segundos = Calendar.getInstance().get(12) * 60 + Calendar.getInstance().get(13);
				if (_perso.getEncarnacion() == null && posAMover == 1) {
					Incarnacao encarnacion = World.getEncarnacion(idObjMover);
					if (encarnacion == null) {
						encarnacion = new Incarnacao(idObjMover, Constantes.getClasePorObjMod(objetoMod.getID()), 1, 0L,
								segundos, "");
						World.addEncarnacion(encarnacion);
					} else if (!encarnacion.sePuedePoner(segundos)) {
						SocketManager.ENVIAR_Im_INFORMACION(_out, "1166");
						return;
					}
					if (_perso.estaMontando()) {
						_perso.bajarMontura();
					}
					_perso.setEncarnacion(encarnacion);
					_perso.setGfxID(encarnacion.getGfx());
					SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(_perso.getMapa(), _perso);
					SocketManager.ENVIAR_ASK_PERSONAJE_SELECCIONADO(_out, _perso);
					SocketManager.ENVIAR_SL_LISTA_HECHIZOS(_perso);
				} else if (_perso.getEncarnacion() != null && posAMover == -1) {
					_perso.getEncarnacion().setSegundos(segundos);
					_perso.deformar();
					_perso.setEncarnacion(null);
					SocketManager.ENVIAR_GM_REFRESCAR_PJ_EN_MAPA(_perso.getMapa(), _perso);
					SocketManager.ENVIAR_ASK_PERSONAJE_SELECCIONADO(_out, _perso);
					SocketManager.ENVIAR_SL_LISTA_HECHIZOS(_perso);
				}
			}
			if (objetoMod.getTipo() == 18) {
				if (_perso.estaMontando()) {
					_perso.subirBajarMontura();
				}
				if (posAMover == 8 && _perso.getObjPosicion(8) == null) {
					if (objMover.getCantidad() > 1) {
						if (cantObjMover > objMover.getCantidad()) {
							cantObjMover = objMover.getCantidad();
						}
						if (objMover.getCantidad() - cantObjMover > 0) {
							nuevaCant = objMover.getCantidad() - cantObjMover;
							Objeto nuevoObj = Objeto.clonarObjeto(objMover, nuevaCant);
							if (!_perso.addObjetoSimilar(nuevoObj, true, idObjMover)) {
								World.addObjeto(nuevoObj, true);
								_perso.addObjetoPut(nuevoObj);
								SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, nuevoObj);
							}
							objMover.setCantidad(cantObjMover);
							SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, objMover);
						}
					}
					objMover.setPosicion(8);
					SocketManager.ENVIAR_OM_MOVER_OBJETO(_perso, objMover);
					equiparMascota(objMover);
					SocketManager.ENVIAR_Oa_CAMBIAR_ROPA(_perso.getMapa(), _perso);
					if (pelea != null) {
						SocketManager.ENVIAR_Oa_CAMBIAR_ROPA_PELEA(_perso, pelea);
					}
					return;
				}
			}
			if (posAMover == 8 && _perso.getObjPosicion(8) != null) {
				alimentarMascota(objMover, _perso.getObjPosicion(8), cantObjMover);
				return;
			}
			if (posAMover == 16 && _perso.getMontura() != null) {
				if (Constantes.alimentoMontura(objetoMod.getTipo())) {
					if (objMover.getCantidad() > 0) {
						if (cantObjMover > objMover.getCantidad()) {
							cantObjMover = objMover.getCantidad();
						}
						if (objMover.getCantidad() - cantObjMover > 0) {
							nuevaCant = objMover.getCantidad() - cantObjMover;
							objMover.setCantidad(nuevaCant);
							SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, objMover);
						} else {
							_perso.borrarObjetoSinEliminar(idObjMover);
							World.eliminarObjeto(idObjMover);
							SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, idObjMover);
						}
					}
					_perso.getMontura().aumEnergia(objMover.getModelo().getNivel(), cantObjMover);
					SocketManager.ENVIAR_Re_DETALLES_MONTURA(_perso, "+", _perso.getMontura());
					return;
				}
				SocketManager.ENVIAR_Im_INFORMACION(_perso, "190");
				return;
			}
			int idSetObjeto = objetoMod.getSetID();
			if (idSetObjeto >= 81 && idSetObjeto <= 92
					|| idSetObjeto >= 201 && idSetObjeto <= 212 && (posAMover == 2 || posAMover == 3 || posAMover == 4
							|| posAMover == 5 || posAMover == 6 || posAMover == 7 || posAMover == 0)) {
				String[] stats;
				String[] arrstring = stats = objetoMod.getStringStatsObj().split(",");
				n = stats.length;
				for (int i = 0; i < n; ++i) {
					String stat = arrstring[i];
					val = stat.split("#");
					int efecto = Integer.parseInt(val[0], 16);
					int hechizo = Integer.parseInt(val[1], 16);
					int modif = Integer.parseInt(val[3], 16);
					String modificacion2 = String.valueOf(efecto) + ";" + hechizo + ";" + modif;
					SocketManager.ENVIAR_SB_HECHIZO_BOOST_SET_CLASE(_perso, modificacion2);
					_perso.addHechizosSetClase(hechizo, efecto, modif);
				}
				_perso.agregarSetClase(objetoMod.getID());
			}
			if (idSetObjeto >= 81 && idSetObjeto <= 92 || idSetObjeto >= 201 && idSetObjeto <= 212 && posAMover == -1) {
				String[] stats;
				val = stats = objetoMod.getStringStatsObj().split(",");
				int n2 = stats.length;
				for (n = 0; n < n2; ++n) {
					String stat = val[n];
					String[] val2 = stat.split("#");
					modificacion = String.valueOf(Integer.parseInt(val2[0], 16)) + ";" + Integer.parseInt(val2[1], 16)
							+ ";0";
					SocketManager.ENVIAR_SB_HECHIZO_BOOST_SET_CLASE(_perso, modificacion);
					_perso.delHechizosSetClase(Integer.parseInt(val2[1], 16));
				}
				_perso.borrarSetClase(objetoMod.getID());
			}
			if (objetoMod.getTipo() == 113) {
				if (_perso.getObjPosicion(posAMover) == null) {
					SocketManager.ENVIAR_Im_INFORMACION(_out, "1161");
					return;
				}
				if (objMover.getCantidad() > 1) {
					int nuevaCant2;
					if (cantObjMover > objMover.getCantidad()) {
						cantObjMover = objMover.getCantidad();
					}
					if ((nuevaCant2 = objMover.getCantidad() - cantObjMover) > 0) {
						Objeto nuevoObj = Objeto.clonarObjeto(objMover, nuevaCant2);
						World.addObjeto(nuevoObj, true);
						_perso.addObjetoPut(nuevoObj);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, nuevoObj);
						objMover.setCantidad(cantObjMover);
						SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, objMover);
					}
				}
				Objeto objet = _perso.getObjPosicion(posAMover);
				equiparObjevivo(objMover, objet);
				return;
			}
			if (!Constantes.esUbicacionValidaObjeto(objetoMod, posAMover) && posAMover != -1) {
				return;
			}
			if (!objetoMod.getCondiciones().isEmpty() && posAMover != -1
					&& !Condiciones.validaCondiciones(_cuenta, _perso, objetoMod.getCondiciones())) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "119|43");
				return;
			}
			if (posAMover != -1 && objetoMod.getNivel() > _perso.getNivel()) {
				SocketManager.ENVIAR_OAEL_ERROR_AGREGAR_OBJETO(_out);
				return;
			}
			if (posAMover != -1 && (idSetObjeto != -1 || objetoMod.getTipo() == 23)
					&& _perso.tieneEquipado(objetoMod.getID())) {
				return;
			}
			if (posAMover != -1 && objetoMod.getTipo() == 23) {
				switch (objetoMod.getID()) {
				case 694: {
					if (!_perso.tieneEquipado(11012))
						break;
					return;
				}
				case 737: {
					if (!_perso.tieneEquipado(11007))
						break;
					return;
				}
				case 739: {
					if (!_perso.tieneEquipado(11013))
						break;
					return;
				}
				case 972: {
					if (!_perso.tieneEquipado(11008))
						break;
					return;
				}
				case 6980: {
					if (!_perso.tieneEquipado(11009))
						break;
					return;
				}
				case 7754: {
					if (!_perso.tieneEquipado(11011))
						break;
					return;
				}
				case 8072: {
					if (!_perso.tieneEquipado(11010))
						break;
					return;
				}
				case 11012: {
					if (!_perso.tieneEquipado(694))
						break;
					return;
				}
				case 11007: {
					if (!_perso.tieneEquipado(737))
						break;
					return;
				}
				case 11013: {
					if (!_perso.tieneEquipado(739))
						break;
					return;
				}
				case 11008: {
					if (!_perso.tieneEquipado(972))
						break;
					return;
				}
				case 11009: {
					if (!_perso.tieneEquipado(6980))
						break;
					return;
				}
				case 11011: {
					if (!_perso.tieneEquipado(7754))
						break;
					return;
				}
				case 11010: {
					if (!_perso.tieneEquipado(8072))
						break;
					return;
				}
				}
			}
			if ((exObj = _perso.getObjPosicion(posAMover)) != null) {
				Objeto.ObjetoModelo exObjModelo = exObj.getModelo();
				int idSetExObj = exObj.getModelo().getSetID();
				Objeto obj2 = _perso.getObjSimilarInventario(exObj);
				if (obj2 != null) {
					obj2.setCantidad(obj2.getCantidad() + exObj.getCantidad());
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, obj2);
					World.eliminarObjeto(exObj.getID());
					_perso.borrarObjetoSinEliminar(exObj.getID());
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, exObj.getID());
				} else {
					exObj.setPosicion(-1);
					if (idSetExObj >= 81 && idSetExObj <= 92 || idSetExObj >= 201 && idSetExObj <= 212) {
						String[] stats;
						String[] arrstring = stats = exObjModelo.getStringStatsObj().split(",");
						int n3 = stats.length;
						for (int modificacion2 = 0; modificacion2 < n3; ++modificacion2) {
							String stat = arrstring[modificacion2];
							String[] val3 = stat.split("#");
							modificacion = String.valueOf(Integer.parseInt(val3[0], 16)) + ";"
									+ Integer.parseInt(val3[1], 16) + ";0";
							SocketManager.ENVIAR_SB_HECHIZO_BOOST_SET_CLASE(_perso, modificacion);
							_perso.delHechizosSetClase(Integer.parseInt(val3[1], 16));
						}
						_perso.borrarSetClase(exObjModelo.getID());
					}
					SocketManager.ENVIAR_OM_MOVER_OBJETO(_perso, exObj);
				}
				if (_perso.getObjPosicion(1) == null) {
					SocketManager.ENVIAR_OT_OBJETO_HERRAMIENTA(_out, -1);
					if (_perso.getMapa().esTaller() && _perso.getOficioPublico()) {
						SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(_out, "-", _perso.getID(), "");
					}
					_perso.setStrOficiosPublicos("");
				}
				if (idSetExObj > 0) {
					SocketManager.ENVIAR_OS_BONUS_SET(_perso, idSetExObj, -1);
				}
			} else {
				Objeto obj2 = _perso.getObjSimilarInventario(objMover);
				if (obj2 != null) {
					if (cantObjMover > objMover.getCantidad()) {
						cantObjMover = objMover.getCantidad();
					}
					obj2.setCantidad(obj2.getCantidad() + cantObjMover);
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, obj2);
					if (objMover.getCantidad() - cantObjMover > 0) {
						objMover.setCantidad(objMover.getCantidad() - cantObjMover);
						SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, objMover);
					} else {
						World.eliminarObjeto(objMover.getID());
						_perso.borrarObjetoSinEliminar(objMover.getID());
						SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, objMover.getID());
					}
				} else {
					objMover.setPosicion(posAMover);
					if (objetoMod.getTipo() == 18 && posAMover == -1) {
						_perso.setMascota(null);
					}
					SocketManager.ENVIAR_OM_MOVER_OBJETO(_perso, objMover);
					if (objMover.getCantidad() > 1) {
						if (cantObjMover > objMover.getCantidad()) {
							cantObjMover = objMover.getCantidad();
						}
						if (objMover.getCantidad() - cantObjMover > 0) {
							int nuevaCant3 = objMover.getCantidad() - cantObjMover;
							Objeto nuevoObj = Objeto.clonarObjeto(objMover, nuevaCant3);
							if (!_perso.addObjetoSimilar(nuevoObj, true, idObjMover)) {
								World.addObjeto(nuevoObj, true);
								_perso.addObjetoPut(nuevoObj);
								SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, nuevoObj);
							}
							objMover.setCantidad(cantObjMover);
							SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, objMover);
						}
					}
				}
			}
			SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
			_perso.refrescarVida();
			if (_perso.getGrupo() != null) {
				SocketManager.ENVIAR_PM_ACTUALIZAR_INFO_PJ_GRUPO(_perso.getGrupo(), _perso);
			}
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
			if (posAMover == 1 || posAMover == 6 || posAMover == 7 || posAMover == 15 || posAMover == -1) {
				SocketManager.ENVIAR_Oa_CAMBIAR_ROPA(_perso.getMapa(), _perso);
			}
			Objeto arma = null;
			if (posAMover == -1 && _perso.getObjPosicion(1) == null) {
				SocketManager.ENVIAR_OT_OBJETO_HERRAMIENTA(_out, -1);
				if (_perso.getMapa().esTaller() && _perso.getOficioPublico()) {
					SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(_out, "-", _perso.getID(), "");
				}
				_perso.setStrOficiosPublicos("");
			} else if (posAMover == 1 && (arma = _perso.getObjPosicion(1)) != null) {
				int idModArma = arma.getModelo().getID();
				for (Map.Entry<Integer, Profissao.StatsOficio> statOficio : _perso.getStatsOficios().entrySet()) {
					Profissao oficio = statOficio.getValue().getOficio();
					if (!oficio.herramientaValida(idModArma))
						continue;
					SocketManager.ENVIAR_OT_OBJETO_HERRAMIENTA(_out, oficio.getID());
					String strOficioPub = Constantes.trabajosOficioTaller(oficio.getID());
					if (_perso.getMapa().esTaller() && _perso.getOficioPublico()) {
						SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(_out, "+", _perso.getID(), strOficioPub);
					}
					_perso.setStrOficiosPublicos(strOficioPub);
					break;
				}
			}
			if (idSetObjeto > 0) {
				SocketManager.ENVIAR_OS_BONUS_SET(_perso, idSetObjeto, -1);
			}
			if (pelea != null) {
				SocketManager.ENVIAR_Oa_CAMBIAR_ROPA_PELEA(_perso, pelea);
			}
		} catch (Exception e) {
			SocketManager.ENVIAR_OdE_ERROR_ELIMINAR_OBJETO(_out);
		}
	}

	private synchronized void aparienciaObjevivo(String packet) {
		try {
			int idObjeto = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			Objeto objeto = World.getObjeto(idObjeto);
			if (objeto == null) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			int objeviId = objeto.getObjeviID();
			Set_Vivo objevi = World.getObjevivos(objeviId);
			if (objevi == null) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			int aparienciaId = Integer.parseInt(packet.split("\\|")[2]);
			objevi.setMascara(aparienciaId);
			SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(_out, objeto);
			SocketManager.ENVIAR_Oa_ACTUALIZAR_FIGURA_PJ(_perso);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Aparencia Item Vivo: " + e.getMessage());
		}
	}

	private synchronized void alimentarObjevivo(String packet) {
		try {
			int idObjeto = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			Objeto objeto = World.getObjeto(idObjeto);
			int idObjAlimento = Integer.parseInt(packet.split("\\|")[2]);
			Objeto objetoAlimento = World.getObjeto(idObjAlimento);
			if (objetoAlimento == null || objeto == null) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			Set_Vivo objevi = World.getObjevivos(objeto.getObjeviID());
			if (objevi == null) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			if (objetoAlimento.getCantidad() > 1 && objetoAlimento.getCantidad() - 1 > 0) {
				int nuevaCant = objetoAlimento.getCantidad() - 1;
				Objeto nuevoObj = Objeto.clonarObjeto(objetoAlimento, nuevaCant);
				if (!_perso.addObjetoSimilar(nuevoObj, true, idObjAlimento)) {
					World.addObjeto(nuevoObj, true);
					_perso.addObjetoPut(nuevoObj);
					SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, nuevoObj);
				}
				objetoAlimento.setCantidad(1);
				SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, objetoAlimento);
			}
			long xp = Long.parseLong(Integer.toString(objetoAlimento.getModelo().getNivel()));
			objevi.setExp(objevi.getExp() + xp);
			World.eliminarObjeto(idObjAlimento);
			_perso.borrarObjetoSinEliminar(idObjAlimento);
			SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, idObjAlimento);
			SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(_out, objeto);
			SocketManager.ENVIAR_Oa_ACTUALIZAR_FIGURA_PJ(_perso);
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Alimentar Item Vivo: " + e.getMessage());
		}
	}

	private synchronized void equiparObjevivo(Objeto objevivo, Objeto objeto) {
		try {
			for (Set_Vivo objevi : World.getTodosObjevivos()) {
				if (objevi.getObjetoAsociadoID() != objevivo.getID() || objevi.getAsociado() != 0)
					continue;
				objeto.setObjeviID(objevi.getID());
				objevi.setStat(objeto.convertirStatsAString());
				objevi.setObjetoAsociadoID(objeto.getID());
				objevi.setAsociado(1);
				_perso.borrarObjetoSinEliminar(objevivo.getID());
				SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, objevivo.getID());
				SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(_out, objeto);
				SocketManager.ENVIAR_Oa_ACTUALIZAR_FIGURA_PJ(_perso);
				return;
			}
			int sigID = World.getSigIDObjevivo();
			String fecha = String.valueOf(Calendar.getInstance().get(2)) + Calendar.getInstance().get(5);
			String fecharInter = String.valueOf(Calendar.getInstance().get(5)) + Calendar.getInstance().get(5);
			String tiempo = String.valueOf(Calendar.getInstance().get(11)) + Calendar.getInstance().get(12);
			Set_Vivo nuevoObjevivo = new Set_Vivo(sigID, 2012, Integer.parseInt(fecha), Integer.parseInt(tiempo), 1, 1,
					objeto.getModelo().getTipo(), objeto.getID(), 0L, 2012, Integer.parseInt(fecharInter),
					Integer.parseInt(tiempo), 2012, Integer.parseInt(fecha), Integer.parseInt(tiempo), 1,
					objevivo.getModelo().getID(), objevivo.getID(), objeto.convertirStatsAString());
			World.addObjevivo(nuevoObjevivo);
			SQLManager.INSERT_OBJEVIVOS(nuevoObjevivo);
			objeto.setObjeviID(sigID);
			_perso.borrarObjetoSinEliminar(objevivo.getID());
			SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, objevivo.getID());
			SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(_out, objeto);
			SocketManager.ENVIAR_Oa_ACTUALIZAR_FIGURA_PJ(_perso);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Equipe Item Vivo " + e.getMessage());
		}
	}

	private synchronized void desequiparObjevivo(String packet) {
		try {
			int id = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			Objeto objeto = World.getObjeto(id);
			int idObjevivo = objeto.getObjeviID();
			Set_Vivo objevivo = World.getObjevivos(idObjevivo);
			if (objevivo == null || objeto == null) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			if (objevivo.getAsociado() == 1) {
				int idObjObjevivo = objevivo.getIDObjevivoOrig();
				Objeto objObjevivo = World.getObjeto(idObjObjevivo);
				if (objObjevivo == null) {
					SocketManager.ENVIAR_BN_NADA(_out);
					return;
				}
				objeto.convertirStringAStats(objevivo.getStat());
				objeto.setObjeviID(0);
				objObjevivo.setObjeviID(idObjevivo);
				objevivo.setObjetoAsociadoID(idObjObjevivo);
				objevivo.setAsociado(0);
				_perso.addObjetoPut(objObjevivo);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, objObjevivo);
				SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(_out, objeto);
				SocketManager.ENVIAR_Oa_ACTUALIZAR_FIGURA_PJ(_perso);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Desequipar Item Vivo " + e.getMessage());
		}
	}

	private synchronized void equiparMascota(Objeto objeto) {
		for (Pets mascota : World.getTodasMascotas()) {
			if (objeto.getID() != mascota.getID())
				continue;
			_perso.setMascota(mascota);
			SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
			return;
		}
		Calendar calendar = Calendar.getInstance();
		int mes = calendar.get(2) + 1;
		int dia = calendar.get(5);
		int hora = calendar.get(11);
		int minuto = calendar.get(12);
		Pets mascota = new Pets(objeto.getID(), 10, "", 0, 2012, mes, dia, hora, minuto, -1, "", 0, 0,
				objeto.getModelo().getID());
		World.addMascota(mascota);
		SocketManager.ENVIAR_Oa_ACTUALIZAR_FIGURA_PJ(_perso);
		_perso.setMascota(mascota);
	}

	private synchronized void alimentarMascota(Objeto comida, Objeto masc, int cantidad) {
		try {
			Pets mascota = World.getMascota(masc.getID());
			if (mascota == null) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			int idModComida = comida.getModelo().getID();
			if (idModComida == 11045) {
				if (mascota.getPDV() < 10) {
					mascota.setPDV(mascota.getPDV() + 1);
				}
				if (comida.getCantidad() > 0) {
					if (cantidad > comida.getCantidad()) {
						cantidad = comida.getCantidad();
					}
					if (comida.getCantidad() - cantidad > 0) {
						int nuevaCant = comida.getCantidad() - cantidad;
						comida.setCantidad(nuevaCant);
						SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, comida);
					} else {
						SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, comida.getID());
						_perso.borrarObjetoSinEliminar(comida.getID());
						World.eliminarObjeto(comida.getID());
					}
				}
				masc.clearTodo();
				masc.convertirStringAStats(mascota.getStringStats());
				SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(_out, masc);
				SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
			} else if (!mascota.esDevoraAlmas() && idModComida >= 11170 && idModComida <= 11184
					|| mascota.esComestible(idModComida)) {
				if (comida.getCantidad() - cantidad > 0) {
					int nuevaCant = comida.getCantidad() - cantidad;
					comida.setCantidad(nuevaCant);
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, comida);
				} else {
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, comida.getID());
					_perso.borrarObjetoSinEliminar(comida.getID());
					World.eliminarObjeto(comida.getID());
				}
				if (idModComida >= 11170 && idModComida <= 11184 || mascota.horaComer()) {
					mascota.comerComida(idModComida);
					masc.clearTodo();
					masc.convertirStringAStats(mascota.getStringStats());
					SocketManager.ENVIAR_OCK_ACTUALIZA_OBJETO(_out, masc);
					SocketManager.ENVIAR_Im_INFORMACION(_out, "032");
					SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
					SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
				} else {
					if (mascota.getObeso()) {
						_perso.restarVidaMascota(mascota);
					}
					mascota.setCorpulencia(1);
					SocketManager.ENVIAR_Im_INFORMACION(_out, "026");
				}
			} else {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "153");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("Alimentar PET " + e.getMessage());
		}
	}

	private void analizar_Dialogos(String packet) {
		switch (packet.charAt(1)) {
		case 'C': {
			dialogo_Iniciar(packet);
			break;
		}
		case 'R': {
			dialogo_Respuesta(packet);
			break;
		}
		case 'V': {
			dialogo_Fin();
		}
		}
	}

	private void dialogo_Respuesta(String packet) {
		String[] infos = packet.substring(2).split("\\|");
		try {
			int preguntaID = Integer.parseInt(infos[0]);
			int respuestaID = Integer.parseInt(infos[1]);
			NPC_tmpl.PreguntaNPC pregunta = World.getPreguntaNPC(preguntaID);
			NPC_tmpl.RespuestaNPC respuesta = World.getRespuestaNPC(respuestaID);
			if (pregunta == null || respuesta == null || !respuesta.esOtroDialogo()) {
				SocketManager.ENVIAR_DV_FINALIZAR_DIALOGO(_out);
				_perso.setConversandoCon(0);
			}
			respuesta.aplicar(_perso);
		} catch (Exception e) {
			SocketManager.ENVIAR_DV_FINALIZAR_DIALOGO(_out);
		}
	}

	private void dialogo_Fin() {
		SocketManager.ENVIAR_DV_FINALIZAR_DIALOGO(_out);
		if (_perso.getConversandoCon() != 0) {
			_perso.setConversandoCon(0);
		}
	}

	private void dialogo_Iniciar(String packet) {
		try {
			int ID = Integer.parseInt(packet.substring(2).split("\n")[0]);
			if (ID > -50) {
				int npcID = ID;
				NPC_tmpl.NPC npc = _perso.getMapa().getNPC(npcID);
				if (npc == null) {
					return;
				}
				SocketManager.ENVIAR_DCK_CREAR_DIALOGO(_out, npcID);
				int pID = npc.getModeloBD().getPreguntaID();
				NPC_tmpl.PreguntaNPC pregunta = World.getPreguntaNPC(pID);
				if (pregunta == null) {
					SocketManager.ENVIAR_DV_FINALIZAR_DIALOGO(_out);
					return;
				}
				SocketManager.ENVIAR_DQ_DIALOGO_PREGUNTA(_out, pregunta.stringArgParaDialogo(_perso));
				_perso.setConversandoCon(npcID);
			} else {
				Coletor recauda = World.getRecaudador(ID);
				if (recauda == null) {
					return;
				}
				SocketManager.ENVIAR_DCK_CREAR_DIALOGO(_out, ID);
				NPC_tmpl.PreguntaNPC pregunta = World.getPreguntaNPC(1);
				if (pregunta == null) {
					SocketManager.ENVIAR_DV_FINALIZAR_DIALOGO(_out);
					return;
				}
				Guild gremio = World.getGremio(recauda.getGremioID());
				SocketManager.ENVIAR_DQ_DIALOGO_PREGUNTA(_out, pregunta.stringGremio(_perso, gremio));
				_perso.setConversandoCon(ID);
			}
		} catch (NumberFormatException numberFormatException) {
			// empty catch block
		}
	}

	private void analizar_Intercambios(String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			intercambio_Aceptar();
			break;
		}
		case 'B': {
			intercambio_Comprar(packet);
			break;
		}
		case 'f': {
			intercambio_Cercado(packet);
			break;
		}
		case 'H': {
			intercambio_Mercadillo(packet);
			break;
		}
		case 'J': {
			intercambio_Oficios(packet);
			break;
		}
		case 'K': {
			intercambio_Ok();
			break;
		}
		case 'L': {
			intercambio_Repetir();
			break;
		}
		case 'M': {
			intercambio_Mover_Objeto(packet);
			break;
		}
		case 'q': {
			intercambio_Preg_Mercante();
			break;
		}
		case 'P': {
			intercambio_Pago_Por_Trabajo(packet);
			break;
		}
		case 'Q': {
			intercambio_Ok_Mercante();
			break;
		}
		case 'r': {
			intercambio_Establo(packet);
			break;
		}
		case 'R': {
			intercambio_Iniciar(packet);
			break;
		}
		case 'S': {
			intercambio_Vender(packet);
			break;
		}
		case 'V': {
			intercambio_Cerrar();
			break;
		}
		case 'W': {
			intercambio_Oficio_Publico(packet);
		}
		}
	}

	private void intercambio_Iniciar(String packet) {
		if (_perso.esFantasma()) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
		if (packet.substring(2, 4).equals("11")) {
			if (_perso.getIntercambiandoCon() < 0) {
				SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			}
			if (_perso.getDeshonor() >= 5) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "183");
				SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
				return;
			}
			Mercador mercadillo = World.getPuestoMerca(_perso.getMapa().getID());
			if (mercadillo == null) {
				return;
			}
			String info = "1,10,100;" + mercadillo.getTipoObjPermitidos() + ";" + mercadillo.porcentajeImpuesto() + ";"
					+ mercadillo.getNivelMax() + ";" + mercadillo.getMaxObjCuenta() + ";-1;"
					+ mercadillo.getTiempoVenta();
			SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_perso, 11, info);
			_perso.setIntercambiandoCon(0 - _perso.getMapa().getID());
			return;
		}
		if (packet.substring(2, 4).equals("10")) {
			if (_perso.getIntercambiandoCon() < 0) {
				SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			}
			if (_perso.getDeshonor() >= 5) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "183");
				SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
				return;
			}
			Mercador mercadillo = World.getPuestoMerca(_perso.getMapa().getID());
			if (mercadillo == null) {
				return;
			}
			String info = "1,10,100;" + mercadillo.getTipoObjPermitidos() + ";" + mercadillo.porcentajeImpuesto() + ";"
					+ mercadillo.getNivelMax() + ";" + mercadillo.getMaxObjCuenta() + ";-1;"
					+ mercadillo.getTiempoVenta();
			SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_perso, 10, info);
			_perso.setIntercambiandoCon(0 - _perso.getMapa().getID());
			SocketManager.GAME_SEND_HDVITEM_SELLING(_perso);
			return;
		}
		if (packet.substring(2, 4).equals("12")) {
			try {
				String[] nuevo = packet.substring(5).split("\\|");
				int idInvitado = Integer.parseInt(nuevo[0]);
				int idTrabajo = Integer.parseInt(nuevo[1]);
				Profissao.AccionTrabajo accionT = null;
				boolean paso = false;
				for (Profissao.StatsOficio statOficio : _perso.getStatsOficios().values()) {
					Profissao oficio = statOficio.getOficio();
					for (Profissao.AccionTrabajo trabajo : Constantes.getTrabajosPorOficios(oficio.getID(),
							statOficio.getNivel())) {
						if (trabajo.getIDTrabajo() != idTrabajo)
							continue;
						accionT = trabajo;
						paso = true;
						break;
					}
					if (paso)
						break;
				}
				if (accionT == null) {
					SocketManager.ENVIAR_BN_NADA(_out);
					return;
				}
				Personagens invitado = World.getPersonaje(idInvitado);
				_perso.setHaciendoTrabajo(accionT);
				invitado.setHaciendoTrabajo(accionT);
				_perso.setIntercambiandoCon(idInvitado);
				invitado.setIntercambiandoCon(_perso.getID());
				SocketManager.ENVIAR_ERK_CONSULTA_INTERCAMBIO(_out, _perso.getID(), idInvitado, 12);
				SocketManager.ENVIAR_ERK_CONSULTA_INTERCAMBIO(invitado.getCuenta().getEntradaPersonaje().getOut(),
						_perso.getID(), idInvitado, 13);
			} catch (Exception nuevo) {
				// empty catch block
			}
			return;
		}
		if (packet.substring(2, 4).equals("15")) {
			try {
				Dragossauros montura = _perso.getMontura();
				int idMontura = montura.getID();
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_out, 15, String.valueOf(_perso.getMontura().getID()));
				SocketManager.ENVIAR_EL_LISTA_OBJETOS_DRAGOPAVO(_out, montura);
				SocketManager.ENVIAR_Ew_PODS_MONTURA(_perso, montura.getPodsActuales());
				_perso.setIntercambiandoCon(idMontura);
				_perso.setDragopaveando(true);
				_perso.setOcupado(true);
			} catch (Exception montura) {
				// empty catch block
			}
			return;
		}
		if (packet.substring(2, 4).equals("17")) {
			try {
				int npcID = Integer.parseInt(packet.substring(5));
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_out, 17, String.valueOf(npcID));
				World.Trueque trueque = new World.Trueque(_perso, "", "resucitar");
				_perso.setTrueque(trueque);
				_perso.setOcupado(true);
			} catch (Exception npcID) {
				// empty catch block
			}
			return;
		}
		switch (packet.charAt(2)) {
		case '0': {
			try {
				int npcID = Integer.parseInt(packet.substring(4));
				NPC_tmpl.NPC npc = _perso.getMapa().getNPC(npcID);
				if (npc == null) {
					return;
				}
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_out, 0, String.valueOf(npcID));
				SocketManager.ENVIAR_EL_LISTA_OBJETOS_NPC(_out, npc);
				_perso.setIntercambiandoCon(npcID);
				_perso.setOcupado(true);
			} catch (NumberFormatException npcID) {
			}
			break;
		}
		case '1': {
			try {
				int idObjetivo = Integer.parseInt(packet.substring(4));
				Personagens objetivo = World.getPersonaje(idObjetivo);
				if (objetivo == null || objetivo.getMapa() != _perso.getMapa() || !objetivo.enLinea()) {
					SocketManager.ENVIAR_ERE_ERROR_CONSULTA(_out, 'E');
					return;
				}
				if (objetivo.estaOcupado() || _perso.estaOcupado() || objetivo.getIntercambiandoCon() != 0) {
					SocketManager.ENVIAR_ERE_ERROR_CONSULTA(_out, 'O');
					return;
				}
				SocketManager.ENVIAR_ERK_CONSULTA_INTERCAMBIO(_out, _perso.getID(), idObjetivo, 1);
				SocketManager.ENVIAR_ERK_CONSULTA_INTERCAMBIO(objetivo.getCuenta().getEntradaPersonaje().getOut(),
						_perso.getID(), idObjetivo, 1);
				_perso.setIntercambiandoCon(idObjetivo);
				objetivo.setIntercambiandoCon(_perso.getID());
				_perso.setOcupado(true);
				objetivo.setOcupado(true);
			} catch (NumberFormatException idObjetivo) {
			}
			break;
		}
		case '4': {
			try {
				int idMercante = Integer.parseInt(packet.split("\\|")[1]);
				Personagens mercante = World.getPersonaje(idMercante);
				if (mercante == null) {
					return;
				}
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_out, 4, String.valueOf(idMercante));
				SocketManager.ENVIAR_EL_LISTA_TIENDA_PERSONAJE(_out, mercante);
				_perso.setIntercambiandoCon(idMercante);
				_perso.setOcupado(true);
			} catch (NumberFormatException idMercante) {
			}
			break;
		}
		case '6': {
			try {
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_out, 6, String.valueOf(_perso.getID()));
				SocketManager.ENVIAR_EL_LISTA_TIENDA_PERSONAJE(_out, _perso);
				_perso.setIntercambiandoCon(_perso.getID());
				_perso.setOcupado(true);
			} catch (Exception idMercante) {
			}
			break;
		}
		case '8': {
			try {
				int recaudaID = Integer.parseInt(packet.substring(4));
				Coletor recau = World.getRecaudador(recaudaID);
				if (recau == null || recau.getEstadoPelea() > 0 || recau.getEnRecolecta() || _perso.getGremio() == null
						|| !_perso.getMiembroGremio().puede(Constantes.G_RECOLECTARRECAUDADOR)) {
					return;
				}
				recau.setEnRecolecta(true);
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_out, 8, String.valueOf(recau.getID()));
				SocketManager.ENVIAR_EL_LISTA_OBJETOS_RECAUDADOR(_out, recau);
				_perso.setIntercambiandoCon(recau.getID());
				_perso.setRecaudando(true);
				_perso.setRecaudandoRecaudadorID(recau.getID());
				_perso.setOcupado(true);
				break;
			} catch (NumberFormatException numberFormatException) {
				// empty catch block
			}
		}
		}
	}

	private void intercambio_Ok() {
		if (_perso.getTallerInvitado() != null) {
			_perso.getTallerInvitado().botonOK(_perso.getID());
		} else if (_perso.getHaciendoTrabajo() != null) {
			if (!_perso.getHaciendoTrabajo().esReceta()) {
				return;
			}
			_perso.getHaciendoTrabajo().unaMagueada();
		} else if (_perso.getRompiendo()) {
			Objeto Obj2;
			if (_perso.getObjetoARomper() == 0) {
				return;
			}
			int id = _perso.getObjetoARomper();
			Objeto Obj = World.getObjeto(id);
			if (Obj == null) {
				return;
			}
			String runaID = Objeto.getRunas(Obj);
			String[] objLista = runaID.split(";");
			boolean creado = false;
			if (Fórmulas.getRandomValor(0, 1) != 1 && objLista.length > 0) {
				try {
					creado = true;
					String runa = objLista[Fórmulas.getRandomValor(0, objLista.length - 1)];
					int objModeloID = Integer.parseInt(runa.split(",")[0]);
					int cantidad = Integer.parseInt(runa.split(",")[1]);
					Objeto.ObjetoModelo ObjTemp = World.getObjModelo(objModeloID);
					Objeto nuevoObj = ObjTemp.crearObjDesdeModelo(cantidad, true);
					if (!_perso.addObjetoSimilar(nuevoObj, true, -1)) {
						World.addObjeto(nuevoObj, true);
						_perso.addObjetoPut(nuevoObj);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, nuevoObj);
					}
					SocketManager.ENVIAR_Ec_INICIAR_RECETA(_perso, "K;" + objModeloID);
				} catch (Exception runa) {
					// empty catch block
				}
			}
			if ((Obj2 = World.getObjeto(id)).getCantidad() == 1) {
				_perso.borrarObjetoSinEliminar(id);
				World.eliminarObjeto(id);
				SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, id);
			} else {
				Obj2.setCantidad(Obj2.getCantidad() - 1);
				SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, Obj2);
			}
			if (!creado) {
				SocketManager.ENVIAR_Ec_INICIAR_RECETA(_perso, "EF");
				SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(_perso.getMapa(), _perso.getID(), "-");
			} else {
				SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
				SocketManager.ENVIAR_IO_ICONO_OBJ_INTERACTIVO(_perso.getMapa(), _perso.getID(), "+8378");
			}
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			_perso.iniciarAccionEnCelda(_perso.getTaller());
		} else if (_perso.getTrueque() != null) {
			_perso.getTrueque().botonOK(_perso.getID());
		} else if (_perso.getIntercambio() != null) {
			_perso.getIntercambio().botonOK(_perso.getID());
		}
	}

	private void intercambio_Aceptar() {
		if (_perso.getIntercambiandoCon() != 0) {
			if (_perso.getHaciendoTrabajo() != null) {
				Profissao.AccionTrabajo trabajo = _perso.getHaciendoTrabajo();
				Personagens artesano = World.getPersonaje(_perso.getIntercambiandoCon());
				try {
					SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(artesano.getCuenta().getEntradaPersonaje().getOut(),
							12, String.valueOf(trabajo.getCasillasMax()) + ";" + trabajo.getIDTrabajo());
					SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_out, 13,
							String.valueOf(trabajo.getCasillasMax()) + ";" + trabajo.getIDTrabajo());
				} catch (NullPointerException e) {
					SocketManager.ENVIAR_BN_NADA(_out);
					_perso.setIntercambiandoCon(0);
					artesano.setIntercambiandoCon(0);
					return;
				}
				World.InvitarTaller taller = new World.InvitarTaller(artesano, _perso, trabajo.getCasillasMax());
				try {
					artesano.setTallerInvitado(taller);
					_perso.setTallerInvitado(taller);
				} catch (NullPointerException e) {
					SocketManager.ENVIAR_BN_NADA(_out);
					return;
				}
			}
			Personagens pjInter = World.getPersonaje(_perso.getIntercambiandoCon());
			try {
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(pjInter.getCuenta().getEntradaPersonaje().getOut(), 1,
						"");
				SocketManager.ENVIAR_ECK_PANEL_DE_INTERCAMBIOS(_out, 1, "");
			} catch (NullPointerException e) {
				SocketManager.ENVIAR_BN_NADA(_out);
				_perso.setIntercambiandoCon(0);
				pjInter.setIntercambiandoCon(0);
				return;
			}
			World.Intercambio intercambio = new World.Intercambio(pjInter, _perso);
			try {
				pjInter.setIntercambio(intercambio);
				_perso.setIntercambio(intercambio);
			} catch (NullPointerException e) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
		}
	}

	public void intercambio_Cerrar() {
		if (!(_perso.getIntercambio() != null || _perso.enBanco() || _perso.getHaciendoTrabajo() != null
				|| _perso.getIntercambiandoCon() != 0 || _perso.getEnCercado() != null || _perso.getTrueque() != null
				|| _perso.getListaArtesanos() || _perso.getCofre() != null || _perso.getMochilaMontura()
				|| _perso.getRompiendo() || _perso.getTallerInvitado() != null)) {
			return;
		}
		if (_perso.getCofre() != null) {
			_perso.setCofre(null);
			_perso.setOcupado(false);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.enBanco()) {
			_perso.setEnBanco(false);
			_perso.setOcupado(false);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getListaArtesanos()) {
			_perso.setListaArtesanos(false);
			_perso.setIntercambiandoCon(0);
			_perso.setOcupado(false);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getRompiendo()) {
			_perso.setOcupado(false);
			_perso.setObjetoARomper(0);
			_perso.setRompiendo(false);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getTrueque() != null) {
			_perso.setIntercambiandoCon(0);
			_perso.setOcupado(false);
			_perso.setTrueque(null);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getIntercambio() != null) {
			_perso.setOcupado(false);
			_perso.getIntercambio().cancel();
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getMochilaMontura()) {
			_perso.setOcupado(false);
			_perso.setDragopaveando(false);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getTallerInvitado() != null) {
			Personagens perso = World.getPersonaje(_perso.getIntercambiandoCon());
			if (perso != null && perso.enLinea()) {
				PrintWriter out = perso.getCuenta().getEntradaPersonaje().getOut();
				perso.setIntercambiandoCon(0);
				perso.setOcupado(false);
				perso.setTallerInvitado(null);
				perso.setHaciendoTrabajo(null);
				SocketManager.ENVIAR_EV_CERRAR_VENTANAS(out);
			}
			_perso.setIntercambiandoCon(0);
			_perso.setOcupado(false);
			_perso.setTallerInvitado(null);
			_perso.setHaciendoTrabajo(null);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getHaciendoTrabajo() != null) {
			Personagens perso;
			_perso.getHaciendoTrabajo().resetReceta();
			_perso.setOcupado(false);
			_perso.setHaciendoTrabajo(null);
			_perso.setIntercambiandoCon(0);
			if (_perso.getIntercambiandoCon() > 0 && (perso = World.getPersonaje(_perso.getIntercambiandoCon())) != null
					&& perso.enLinea()) {
				PrintWriter out = perso.getCuenta().getEntradaPersonaje().getOut();
				perso.setHaciendoTrabajo(null);
				perso.setOcupado(false);
				perso.setIntercambiandoCon(0);
				SocketManager.ENVIAR_EV_CERRAR_VENTANAS(out);
			}
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getEnCercado() != null) {
			_perso.salirDeCercado();
			_perso.setOcupado(false);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getIntercambiandoCon() < 0) {
			_perso.setIntercambiandoCon(0);
			_perso.setOcupado(false);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getIntercambiandoCon() > 0) {
			Personagens perso = World.getPersonaje(_perso.getIntercambiandoCon());
			if (perso != null && perso.enLinea()) {
				PrintWriter out = perso.getCuenta().getEntradaPersonaje().getOut();
				perso.setIntercambiandoCon(0);
				perso.setIntercambio(null);
				perso.setOcupado(false);
				SocketManager.ENVIAR_EV_CERRAR_VENTANAS(out);
			}
			_perso.setIntercambiandoCon(0);
			_perso.setOcupado(false);
			_perso.setIntercambio(null);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
		if (_perso.getRecaudando()) {
			Coletor recau = World.getRecaudador(_perso.getRecaudandoRecauID());
			for (Personagens z : World.getGremio(recau.getGremioID()).getPjMiembros()) {
				if (z == null || !z.enLinea())
					continue;
				SocketManager.ENVIAR_gITM_INFO_RECAUDADOR(z, Coletor.analizarRecaudadores(z.getGremio().getID()));
				String str = "";
				str = String.valueOf(str) + "G" + recau.getN1() + "," + recau.getN2();
				str = String.valueOf(str) + "|.|" + World.getMapa(recau.getMapaID()).getX() + "|"
						+ World.getMapa(recau.getMapaID()).getY() + "|";
				str = String.valueOf(str) + _perso.getNombre() + "|";
				str = String.valueOf(str) + recau.getXp();
				if (!recau.stringObjetos().isEmpty()) {
					str = String.valueOf(str) + ";" + recau.stringObjetos();
				}
				SocketManager.GAME_SEND_gT_PACKET(z, str);
			}
			recau.setEnRecolecta(false);
			_perso.getMapa().removeNPC(recau.getID());
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_perso.getMapa(), recau.getID());
			recau.borrarRecauPorRecolecta(recau.getID(), _perso);
			_perso.setRecaudando(false);
			_perso.setRecaudandoRecaudadorID(0);
			_perso.setIntercambiandoCon(0);
			_perso.setOcupado(false);
			SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
			return;
		}
	}

	private void intercambio_Oficios(String packet) {
		switch (packet.charAt(2)) {
		case 'F': {
			int idOficio = Integer.parseInt(packet.substring(3));
			for (Personagens artesano : World.getPJsEnLinea()) {
				if (artesano.getStatsOficios().isEmpty())
					continue;
				String enviar = "";
				int id = artesano.getID();
				String nombre = artesano.getNombre();
				String colores = String.valueOf(artesano.getColor1()) + "," + artesano.getColor2() + ","
						+ artesano.getColor3();
				String accesorios = artesano.getStringAccesorios();
				int sexo = artesano.getSexo();
				short mapa = artesano.getMapa().getID();
				int entaller = mapa == 8731 || mapa == 8732 ? 1 : 0;
				int clase = artesano.getClase(true);
				for (Profissao.StatsOficio oficio : artesano.getStatsOficios().values()) {
					if (oficio.getOficio().getID() != idOficio)
						continue;
					enviar = "+" + oficio.getOficio().getID() + ";" + id + ";" + nombre + ";" + oficio.getNivel() + ";"
							+ mapa + ";" + entaller + ";" + clase + ";" + sexo + ";" + colores + ";" + accesorios + ";"
							+ oficio.getOpcionBin() + "," + oficio.getSlotsPublico();
					SocketManager.ENVIAR_EJ_DESCRIPCION_LIBRO_ARTESANO(_perso, enviar);
				}
			}
			break;
		}
		}
	}

	private void intercambio_Oficio_Publico(String packet) {
		switch (packet.charAt(2)) {
		case '+': {
			_perso.setOficioPublico(true);
			for (Profissao.StatsOficio oficio : _perso.getStatsOficios().values()) {
				int idModOficio = oficio.getOficio().getID();
				SocketManager.ENVIAR_Ej_AGREGAR_LIBRO_ARTESANO(_perso, "+" + idModOficio);
			}
			SocketManager.ENVIAR_EW_OFICIO_MODO_PUBLICO(_out, "+");
			if (!_perso.getMapa().esTaller())
				break;
			SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(_out, "+", _perso.getID(),
					_perso.getStringOficiosPublicos());
			break;
		}
		case '-': {
			_perso.setOficioPublico(false);
			for (Profissao.StatsOficio oficio : _perso.getStatsOficios().values()) {
				SocketManager.ENVIAR_Ej_AGREGAR_LIBRO_ARTESANO(_perso, "-" + oficio.getOficio().getID());
			}
			SocketManager.ENVIAR_EW_OFICIO_MODO_PUBLICO(_out, "-");
			SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(_out, "-", _perso.getID(), "");
		}
		}
	}

	private synchronized void intercambio_Mover_Objeto(String packet) {
		if (_perso.getTallerInvitado() != null) {
			World.InvitarTaller taller = _perso.getTallerInvitado();
			switch (packet.charAt(2)) {
			case 'O': {
				if (packet.charAt(3) == '+') {
					String[] infos = packet.substring(4).split("\\|");
					int id = -1;
					int cant = -1;
					try {
						id = Integer.parseInt(infos[0]);
						cant = Integer.parseInt(infos[1]);
					} catch (NumberFormatException numberFormatException) {
						// empty catch block
					}
					if (id == -1 || cant == -1) {
						return;
					}
					try {
						int cantInter = taller.getCantObjeto(id, _perso.getID());
						if (!_perso.tieneObjetoID(id)) {
							return;
						}
						Objeto obj = World.getObjeto(id);
						if (obj == null) {
							return;
						}
						int nuevaCant = obj.getCantidad() - cantInter;
						if (cant > nuevaCant) {
							cant = nuevaCant;
						}
						taller.addObjeto(obj, cant, _perso.getID());
					} catch (NullPointerException cantInter) {
					}
					break;
				}
				String[] infos = packet.substring(4).split("\\|");
				try {
					int id = Integer.parseInt(infos[0]);
					int cant = Integer.parseInt(infos[1]);
					if (cant <= 0) {
						return;
					}
					if (!_perso.tieneObjetoID(id)) {
						return;
					}
					Objeto obj = World.getObjeto(id);
					if (obj == null) {
						return;
					}
					int cantInter = taller.getCantObjeto(id, _perso.getID());
					if (cant > cantInter) {
						cant = cantInter;
					}
					taller.borrarObjeto(obj, cant, _perso.getID());
					break;
				} catch (NumberFormatException id) {
					// empty catch block
				}
			}
			}
			return;
		}
		if (_perso.getRecaudando()) {
			Coletor recaudador = World.getRecaudador(_perso.getRecaudandoRecauID());
			if (recaudador == null || recaudador.getEstadoPelea() > 0) {
				return;
			}
			switch (packet.charAt(2)) {
			case 'G': {
				if (packet.charAt(3) != '-')
					break;
				long kamas = Integer.parseInt(packet.substring(4));
				long kamasRetiradas = recaudador.getKamas() - kamas;
				if (kamasRetiradas < 0L) {
					kamasRetiradas = 0L;
					kamas = recaudador.getKamas();
				}
				recaudador.setKamas(kamasRetiradas);
				_perso.addKamas(kamas);
				SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
				SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(_perso, "G" + recaudador.getKamas());
				break;
			}
			case 'O': {
				if (packet.charAt(3) != '-')
					break;
				String[] infos = packet.substring(4).split("\\|");
				int id = 0;
				int cant = 0;
				try {
					id = Integer.parseInt(infos[0]);
					cant = Integer.parseInt(infos[1]);
				} catch (NumberFormatException numberFormatException) {
					// empty catch block
				}
				if (id <= 0 || cant <= 0) {
					return;
				}
				Objeto obj = World.getObjeto(id);
				if (obj == null) {
					return;
				}
				if (!recaudador.tieneObjeto(id))
					break;
				recaudador.borrarDesdeRecaudador(_perso, id, cant);
			}
			}
			_perso.getGremio().addXp(recaudador.getXp());
			recaudador.setXp(0L);
			return;
		}
		if (_perso.getRompiendo()) {
			if (packet.charAt(2) == 'O') {
				if (packet.charAt(3) == '+') {
					if (_perso.getObjetoARomper() == 0) {
						String[] Infos = packet.substring(4).split("\\|");
						try {
							int id = Integer.parseInt(Infos[0]);
							int cantidad = 1;
							if (!_perso.tieneObjetoID(id)) {
								return;
							}
							Objeto Obj = World.getObjeto(id);
							if (Obj == null || Obj.getModelo().getTipo() == 18) {
								return;
							}
							_perso.setObjetoARomper(id);
							SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out, 'O', "+",
									String.valueOf(id) + "|" + cantidad);
						} catch (NumberFormatException id) {
						}
					} else {
						String[] Infos = packet.substring(4).split("\\|");
						try {
							int ultimo = _perso.getObjetoARomper();
							SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out, 'O', "-", String.valueOf(ultimo) + "|1");
							int id = Integer.parseInt(Infos[0]);
							int cantidad = 1;
							if (!_perso.tieneObjetoID(id)) {
								return;
							}
							Objeto Obj = World.getObjeto(id);
							if (Obj == null) {
								return;
							}
							_perso.setObjetoARomper(id);
							SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out, 'O', "+",
									String.valueOf(id) + "|" + cantidad);
						} catch (NumberFormatException ultimo) {
						}
					}
				} else if (packet.charAt(3) == '-') {
					String[] Infos = packet.substring(4).split("\\|");
					try {
						int id = Integer.parseInt(Infos[0]);
						int cantidad = Integer.parseInt(Infos[1]);
						Objeto Obj = World.getObjeto(id);
						if (Obj == null) {
							return;
						}
						SocketManager.ENVIAR_EMK_MOVER_OBJETO_LOCAL(_out, 'O', "-",
								String.valueOf(id) + "|" + cantidad);
						_perso.setObjetoARomper(0);
					} catch (NumberFormatException id) {
						// empty catch block
					}
				}
			}
			return;
		}
		if (_perso.getIntercambiandoCon() < 0 && !_perso.getMochilaMontura() && !_perso.getRompiendo()) {
			switch (packet.charAt(3)) {
			case '-': {
				int idObjeto = Integer.parseInt(packet.substring(4).split("\\|")[0]);
				int cant = Integer.parseInt(packet.substring(4).split("\\|")[1]);
				if (cant <= 0) {
					return;
				}
				_perso.getCuenta().recuperarObjeto(idObjeto, cant);
				SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, '-', "", String.valueOf(idObjeto));
				break;
			}
			case '+': {
				int objetoID = Integer.parseInt(packet.substring(4).split("\\|")[0]);
				int cantidad = Integer.parseInt(packet.substring(4).split("\\|")[1]);
				int precio = 0;
				try {
					precio = Integer.parseInt(packet.substring(4).split("\\|")[2]);
				} catch (ArrayIndexOutOfBoundsException e) {
					precio = 0;
				}
				if (cantidad <= 0 || precio <= 0) {
					return;
				}
				Mercador puesto = World.getPuestoMerca(Math.abs(_perso.getIntercambiandoCon()));
				int porcentaje = (int) ((float) precio * (puesto.getPorcentaje() / 100.0f));
				if (!_perso.tieneObjetoID(objetoID)) {
					return;
				}
				if (_perso.getCuenta().cantidadObjMercadillo(puesto.getIDMercadillo()) >= puesto.getMaxObjCuenta()) {
					SocketManager.ENVIAR_Im_INFORMACION(_out, "058");
					return;
				}
				if (_perso.getKamas() < (long) porcentaje) {
					SocketManager.ENVIAR_Im_INFORMACION(_out, "176");
					return;
				}
				_perso.addKamas(porcentaje * -1);
				SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
				Objeto obj = World.getObjeto(objetoID);
				if (cantidad > obj.getCantidad()) {
					return;
				}
				int cantReal = (int) (Math.pow(10.0, cantidad) / 10.0);
				int nuevaCant = obj.getCantidad() - cantReal;
				if (nuevaCant <= 0) {
					_perso.borrarObjetoSinEliminar(objetoID);
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, objetoID);
				} else {
					obj.setCantidad(obj.getCantidad() - cantReal);
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, obj);
					Objeto nuevoObj = Objeto.clonarObjeto(obj, cantReal);
					World.addObjeto(nuevoObj, true);
					obj = nuevoObj;
				}
				Mercador.ObjetoMercadillo objMerca = new Mercador.ObjetoMercadillo(precio, cantidad,
						_perso.getCuenta().getID(), obj);
				puesto.addObjMercaAlPuesto(objMerca);
				SocketManager.ENVIAR_EmK_MOVER_OBJETO_DISTANTE(_out, '+', "", objMerca.analizarParaEmK());
			}
			}
			return;
		}
		if (_perso.getHaciendoTrabajo() != null) {
			if (!_perso.getHaciendoTrabajo().esReceta()) {
				return;
			}
			if (packet.charAt(2) == 'O') {
				if (packet.charAt(3) == '+') {
					String[] infos = packet.substring(4).split("\\|");
					try {
						int id = Integer.parseInt(infos[0]);
						int cantidad = Integer.parseInt(infos[1]);
						if (cantidad <= 0) {
							return;
						}
						if (!_perso.tieneObjetoID(id)) {
							return;
						}
						Objeto obj = World.getObjeto(id);
						if (obj == null) {
							return;
						}
						if (obj.getCantidad() < cantidad) {
							cantidad = obj.getCantidad();
						}
						_perso.getHaciendoTrabajo().modificarIngrediente(_out, id, cantidad);
					} catch (NumberFormatException id) {
					}
				} else {
					String[] infos = packet.substring(4).split("\\|");
					try {
						int id = Integer.parseInt(infos[0]);
						int cantidad = Integer.parseInt(infos[1]);
						if (cantidad <= 0) {
							return;
						}
						Objeto obj = World.getObjeto(id);
						if (obj == null) {
							return;
						}
						_perso.getHaciendoTrabajo().modificarIngrediente(_out, id, -cantidad);
					} catch (NumberFormatException id) {
					}
				}
			} else if (packet.charAt(2) == 'R') {
				int c = 0;
				try {
					c = Integer.parseInt(packet.substring(3));
				} catch (Exception id) {
					// empty catch block
				}
				_perso.getHaciendoTrabajo().variasMagueadas(c, _perso);
			}
			return;
		}
		if (_perso.enBanco()) {
			if (_perso.getIntercambio() != null) {
				return;
			}
			_perso.setOcupado(true);
			block33: switch (packet.charAt(2)) {
			case 'G': {
				long kamas = 0L;
				try {
					kamas = Integer.parseInt(packet.substring(3));
				} catch (Exception obj) {
					// empty catch block
				}
				if (kamas == 0L) {
					return;
				}
				if (kamas > 0L) {
					if (_perso.getKamas() < kamas) {
						kamas = _perso.getKamas();
					}
					_perso.setKamasBanco(_perso.getKamasBanco() + kamas);
					_perso.setKamas(_perso.getKamas() - kamas);
					SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
					SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(_perso, "G" + _perso.getKamasBanco());
					break;
				}
				kamas = -kamas;
				if (_perso.getKamasBanco() < kamas) {
					kamas = _perso.getKamasBanco();
				}
				_perso.setKamasBanco(_perso.getKamasBanco() - kamas);
				_perso.setKamas(_perso.getKamas() + kamas);
				SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
				SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(_perso, "G" + _perso.getKamasBanco());
				break;
			}
			case 'O': {
				int id = 0;
				int cant2 = 0;
				try {
					id = Integer.parseInt(packet.substring(4).split("\\|")[0]);
					cant2 = Integer.parseInt(packet.substring(4).split("\\|")[1]);
				} catch (Exception puesto) {
					// empty catch block
				}
				if (id == 0 || cant2 <= 0) {
					return;
				}
				if (World.getObjeto(id) == null) {
					SocketManager.ENVIAR_BN_NADA(_out);
					return;
				}
				int idModObj = World.getObjeto(id).getModelo().getID();
				if (idModObj >= 7808 && idModObj <= 7876 && idModObj != 7864 && idModObj != 7865 && idModObj != 7819
						&& idModObj != 7811 && idModObj != 7817) {
					int color = Constantes.getColorDragoPavoPorPerga(idModObj);
					int idScroll = Constantes.getScrollporMontura(color);
					if (idScroll == -1) {
						return;
					}
					_perso.borrarObjetoSinEliminar(id);
					World.eliminarObjeto(id);
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, id);
					Objeto scroll = World.getObjModelo(idScroll).crearObjDesdeModelo(2, false);
					if (!_perso.addObjetoSimilar(scroll, true, -1)) {
						World.addObjeto(scroll, true);
						_perso.addObjetoPut(scroll);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, scroll);
					}
					SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
					return;
				}
				switch (packet.charAt(3)) {
				case '+': {
					_perso.addObjAlBanco(id, cant2);
					break block33;
				}
				case '-': {
					_perso.removerDelBanco(id, cant2);
					break block33;
				}
				}
			}
			}
			return;
		}
		if (_perso.getMochilaMontura()) {
			Dragossauros drago = _perso.getMontura();
			if (drago == null) {
				return;
			}
			block41: switch (packet.charAt(2)) {
			case 'O': {
				int id = 0;
				int cant = 0;
				try {
					id = Integer.parseInt(packet.substring(4).split("\\|")[0]);
					cant = Integer.parseInt(packet.substring(4).split("\\|")[1]);
				} catch (Exception cant2) {
					// empty catch block
				}
				if (id == 0 || cant <= 0) {
					return;
				}
				if (World.getObjeto(id) == null) {
					SocketManager.ENVIAR_BN_NADA(_out);
					return;
				}
				switch (packet.charAt(3)) {
				case '+': {
					drago.addObjAMochila(id, cant, _perso);
					break block41;
				}
				case '-': {
					drago.removerDeLaMochila(id, cant, _perso);
					break block41;
				}
				}
			}
			}
			return;
		}
		if (_perso.getCofre() != null) {
			if (_perso.getIntercambio() != null) {
				return;
			}
			Cofre cofre = _perso.getCofre();
			block48: switch (packet.charAt(2)) {
			case 'G': {
				long kamas = 0L;
				try {
					kamas = Integer.parseInt(packet.substring(3));
				} catch (Exception cant2) {
					// empty catch block
				}
				if (kamas == 0L) {
					return;
				}
				if (kamas > 0L) {
					if (_perso.getKamas() < kamas) {
						kamas = _perso.getKamas();
					}
					cofre.setKamas(cofre.getKamas() + kamas);
					_perso.setKamas(_perso.getKamas() - kamas);
					SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
				} else {
					kamas = -kamas;
					if (cofre.getKamas() < kamas) {
						kamas = cofre.getKamas();
					}
					cofre.setKamas(cofre.getKamas() - kamas);
					_perso.setKamas(_perso.getKamas() + kamas);
					SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
				}
				for (Personagens P : World.getPJsEnLinea()) {
					if (P.getCofre() == null || _perso.getCofre().getID() != P.getCofre().getID())
						continue;
					SocketManager.ENVIAR_EsK_MOVER_A_TIENDA_COFRE_BANCO(P, "G" + cofre.getKamas());
				}
				break;
			}
			case 'O': {
				int id = 0;
				int cant = 0;
				try {
					id = Integer.parseInt(packet.substring(4).split("\\|")[0]);
					cant = Integer.parseInt(packet.substring(4).split("\\|")[1]);
				} catch (Exception idScroll) {
					// empty catch block
				}
				if (id == 0 || cant <= 0) {
					return;
				}
				switch (packet.charAt(3)) {
				case '+': {
					cofre.agregarAlCofre(id, cant, _perso);
					break block48;
				}
				case '-': {
					cofre.retirarDelCofre(id, cant, _perso);
					break block48;
				}
				}
			}
			}
			return;
		}
		if (_perso.getTrueque() != null) {
			switch (packet.charAt(2)) {
			case 'O': {
				if (packet.charAt(3) == '+') {
					String[] infos = packet.substring(4).split("\\|");
					try {
						int id = Integer.parseInt(infos[0]);
						int cant = Integer.parseInt(infos[1]);
						int cantInter = _perso.getTrueque().getCantObj(id, _perso.getID());
						if (!_perso.tieneObjetoID(id)) {
							return;
						}
						Objeto obj = World.getObjeto(id);
						if (obj == null) {
							return;
						}
						int nuevaCant = obj.getCantidad() - cantInter;
						if (cant > nuevaCant) {
							cant = nuevaCant;
						}
						_perso.getTrueque().addObjetoTrueque(id, cant);
					} catch (NumberFormatException id) {
					}
					break;
				}
				String[] infos = packet.substring(4).split("\\|");
				try {
					int id = Integer.parseInt(infos[0]);
					int cant = Integer.parseInt(infos[1]);
					if (cant <= 0) {
						return;
					}
					if (!_perso.tieneObjetoID(id)) {
						return;
					}
					Objeto obj = World.getObjeto(id);
					if (obj == null) {
						return;
					}
					int cantInter = _perso.getTrueque().getCantObj(id, _perso.getID());
					if (cant > cantInter) {
						cant = cantInter;
					}
					_perso.getTrueque().quitarObjeto(id, cant);
					break;
				} catch (NumberFormatException id) {
					// empty catch block
				}
			}
			}
			return;
		}
		if (_perso.getIntercambiandoCon() == _perso.getID()) {
			switch (packet.charAt(3)) {
			case '-': {
				int idObj = Integer.parseInt(packet.substring(4).split("\\|")[0]);
				int cant = Integer.parseInt(packet.substring(4).split("\\|")[1]);
				if (cant <= 0) {
					return;
				}
				_perso.objetoAInvetario(idObj);
				SocketManager.ENVIAR_EiK_MOVER_OBJETO_TIENDA(_out, '-', "", String.valueOf(idObj));
				break;
			}
			case '+': {
				int idObjeto = Integer.parseInt(packet.substring(4).split("\\|")[0]);
				int cantidad = Integer.parseInt(packet.substring(4).split("\\|")[1]);
				long precio = Long.parseLong(packet.substring(4).split("\\|")[2]);
				if (!_perso.getTienda().contains(World.getObjeto(idObjeto))) {
					if (cantidad <= 0 || precio <= 0L || !_perso.tieneObjetoID(idObjeto)) {
						return;
					}
					if (_perso.contarTienda() >= _perso.maxTienda()) {
						SocketManager.ENVIAR_Im_INFORMACION(_out, "176");
						return;
					}
					Objeto obj = World.getObjeto(idObjeto);
					if (cantidad > obj.getCantidad()) {
						return;
					}
					int sobrante = obj.getCantidad() - cantidad;
					if (sobrante <= 0) {
						_perso.borrarObjetoSinEliminar(idObjeto);
						SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, idObjeto);
					} else {
						obj.setCantidad(sobrante);
						SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, obj);
						Objeto nuevoObj = Objeto.clonarObjeto(obj, cantidad);
						World.addObjeto(nuevoObj, true);
						obj = nuevoObj;
					}
					Stockage nuevoObjeto = new Stockage(idObjeto, precio, cantidad);
					World.agregarTienda(nuevoObjeto, true);
					String venta = String.valueOf(obj.getID()) + "|" + cantidad + "|" + obj.getModelo().getID() + "|"
							+ obj.convertirStatsAString() + "|" + precio;
					SocketManager.ENVIAR_EiK_MOVER_OBJETO_TIENDA(_out, '+', "", venta);
					_perso.agregarObjTienda(obj);
					break;
				}
				if (precio <= 0L) {
					return;
				}
				Objeto obj = World.getObjeto(idObjeto);
				String venta = String.valueOf(idObjeto) + "|" + cantidad + "|" + obj.getModelo().getID() + "|"
						+ obj.convertirStatsAString() + "|" + precio;
				SocketManager.ENVIAR_EiK_MOVER_OBJETO_TIENDA(_out, '+', "", venta);
				_perso.actualizarObjTienda(idObjeto, precio);
			}
			}
			return;
		}
		if (_perso.getIntercambio() == null) {
			return;
		}
		World.Intercambio inter = _perso.getIntercambio();
		switch (packet.charAt(2)) {
		case 'O': {
			if (packet.charAt(3) == '+') {
				String[] infos = packet.substring(4).split("\\|");
				int id = -1;
				int cant = -1;
				try {
					id = Integer.parseInt(infos[0]);
					cant = Integer.parseInt(infos[1]);
				} catch (NumberFormatException precio) {
					// empty catch block
				}
				if (id == -1 || cant == -1) {
					return;
				}
				try {
					int cantInter = inter.getCantObjeto(id, _perso.getID());
					if (!_perso.tieneObjetoID(id)) {
						return;
					}
					Objeto obj = World.getObjeto(id);
					if (obj == null) {
						return;
					}
					int nuevaCant = obj.getCantidad() - cantInter;
					if (cant > nuevaCant) {
						cant = nuevaCant;
					}
					inter.addObjeto(obj, cant, _perso.getID());
				} catch (NullPointerException cantInter) {
				}
				break;
			}
			String[] infos = packet.substring(4).split("\\|");
			try {
				int id = Integer.parseInt(infos[0]);
				int cant = Integer.parseInt(infos[1]);
				if (cant <= 0) {
					return;
				}
				if (!_perso.tieneObjetoID(id)) {
					return;
				}
				Objeto obj = World.getObjeto(id);
				if (obj == null) {
					return;
				}
				int cantInter = inter.getCantObjeto(id, _perso.getID());
				if (cant > cantInter) {
					cant = cantInter;
				}
				inter.borrarObjeto(obj, cant, _perso.getID());
			} catch (NumberFormatException numberFormatException) {
			}
			break;
		}
		case 'G': {
			try {
				long numero = Integer.parseInt(packet.substring(3));
				if (_perso.getKamas() < numero) {
					numero = _perso.getKamas();
				}
				inter.setKamas(_perso.getID(), numero);
				break;
			} catch (NumberFormatException numberFormatException) {
				// empty catch block
			}
		}
		}
	}

	private void intercambio_Pago_Por_Trabajo(String packet) {
		if (_perso.getIntercambiandoCon() == 0) {
			SocketManager.ENVIAR_BN_NADA(_perso);
			return;
		}
		int tipoPago = Integer.parseInt(packet.substring(2, 3));
		char caracter = packet.charAt(3);
		char signo = packet.charAt(4);
		World.InvitarTaller taller = _perso.getTallerInvitado();
		if (caracter == 'G') {
			long kamas = Long.parseLong(packet.substring(4));
			_perso.getTallerInvitado().setKamas(tipoPago, kamas, _perso.getKamas());
		} else if (signo == '+') {
			String[] infos = packet.substring(5).split("\\|");
			int id = -1;
			int cant = -1;
			try {
				id = Integer.parseInt(infos[0]);
				cant = Integer.parseInt(infos[1]);
			} catch (NumberFormatException numberFormatException) {
				// empty catch block
			}
			if (id == -1 || cant == -1) {
				return;
			}
			try {
				int cantInter = taller.getCantObjetoPago(id, tipoPago);
				if (!_perso.tieneObjetoID(id)) {
					return;
				}
				Objeto obj = World.getObjeto(id);
				if (obj == null) {
					return;
				}
				int nuevaCant = obj.getCantidad() - cantInter;
				if (cant > nuevaCant) {
					cant = nuevaCant;
				}
				taller.addObjetoPaga(obj, cant, tipoPago);
			} catch (NullPointerException cantInter) {
			}
		} else {
			String[] infos = packet.substring(5).split("\\|");
			try {
				int id = Integer.parseInt(infos[0]);
				int cant = Integer.parseInt(infos[1]);
				if (cant <= 0) {
					return;
				}
				if (!_perso.tieneObjetoID(id)) {
					return;
				}
				Objeto obj = World.getObjeto(id);
				if (obj == null) {
					return;
				}
				int cantInter = taller.getCantObjetoPago(id, tipoPago);
				if (cant > cantInter) {
					cant = cantInter;
				}
				taller.borrarObjetoPaga(obj, cant, tipoPago);
			} catch (NumberFormatException numberFormatException) {
				// empty catch block
			}
		}
	}

	private void intercambio_Preg_Mercante() {
		if (_perso.getDeshonor() >= 4) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "183");
			return;
		}
		int objTienda = _perso.contarTienda();
		int tasa = _perso.getNivel() / 2;
		long impuesto = _perso.precioTotalTienda() * (long) tasa / 1000L;
		SocketManager.ENVIAR_Eq_PREGUNTAR_MERCANTE(_perso, objTienda, tasa, impuesto);
	}

	private synchronized void intercambio_Ok_Mercante() {
		Maps mapa = _perso.getMapa();
		int tasa = _perso.getNivel() / 2;
		long pagar = _perso.precioTotalTienda() * (long) tasa / 1000L;
		long kamas = _perso.getKamas();
		if (pagar == 0L) {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "123");
		} else if (kamas >= pagar) {
			if (mapa.cantMercantes() < mapa.getCapacidad() - 1) {
				_perso.setKamas(kamas - pagar);
				_perso.setMercante(1);
				mapa.agregarMercante(_perso.getID());
				String mercante = _perso.stringGMmercante();
				salir();
				SocketManager.ENVIAR_GM_MERCANTE_A_MAPA(mapa, mercante);
				return;
			}
			SocketManager.ENVIAR_Im_INFORMACION(_out, "125;" + mapa.getCapacidad());
		} else {
			SocketManager.ENVIAR_Im_INFORMACION(_out, "176");
		}
	}

	private synchronized void intercambio_Mercadillo(String packet) {
		if (_perso.getIntercambiandoCon() > 0) {
			return;
		}
		switch (packet.charAt(2)) {
		case 'B': {
			String[] info = packet.substring(3).split("\\|");
			Mercador puesto = World.getPuestoMerca(Math.abs(_perso.getIntercambiandoCon()));
			int lineaID = Integer.parseInt(info[0]);
			int cantidad = Integer.parseInt(info[1]);
			if (puesto.comprarObjeto(lineaID, cantidad, Integer.parseInt(info[2]), _perso)) {
				SocketManager.GAME_SEND_EHm_PACKET(_perso, "-", String.valueOf(lineaID));
				if (puesto.getLinea(lineaID) != null && !puesto.getLinea(lineaID).categoriaVacia()) {
					SocketManager.GAME_SEND_EHm_PACKET(_perso, "+", puesto.getLinea(lineaID).analizarParaEHm());
				}
				_perso.refrescarVida();
				SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
				SocketManager.ENVIAR_Im_INFORMACION(_out, "068");
				break;
			}
			SocketManager.ENVIAR_Im_INFORMACION(_out, "172");
			break;
		}
		case 'l': {
			int modeloId = Integer.parseInt(packet.substring(3));
			try {
				SocketManager.GAME_SEND_EHl(_perso, World.getPuestoMerca(Math.abs(_perso.getIntercambiandoCon())),
						modeloId);
			} catch (NullPointerException e) {
				SocketManager.GAME_SEND_EHM_PACKET(_perso, "-", String.valueOf(modeloId));
			}
			break;
		}
		case 'P': {
			int modeloID = Integer.parseInt(packet.substring(3));
			SocketManager.GAME_SEND_EHP_PACKET(_perso, modeloID);
			break;
		}
		case 'T': {
			int categoria = Integer.parseInt(packet.substring(3));
			String todosModelos = World.getPuestoMerca(Math.abs(_perso.getIntercambiandoCon())).stringModelo(categoria);
			SocketManager.GAME_SEND_EHL_PACKET(_perso, categoria, todosModelos);
		}
		}
	}

	private synchronized void intercambio_Cercado(String packet) {
		if (_perso.getEnCercado() != null) {
			if (_perso.getDeshonor() >= 5) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "183");
				SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
				return;
			}
			char c = packet.charAt(2);
			packet = packet.substring(3);
			int id = -1;
			try {
				id = Integer.parseInt(packet);
			} catch (Exception exception) {
				// empty catch block
			}
			switch (c) {
			case 'g': {
				Dragossauros DP3 = World.getDragopavoPorID(id);
				if (!_cuenta.getEstablo().contains(DP3)) {
					_cuenta.getEstablo().add(DP3);
				}
				_perso.getMapa().getCercado().delCriando(id);
				SocketManager.ENVIAR_Ef_MONTURA_A_CRIAR(_perso, '-', String.valueOf(DP3.getID()));
				if (DP3.getFecundadaHace() >= DP3.minutosParir() && DP3.getFecundadaHace() <= 1440) {
					int crias = Fórmulas.getRandomValor(1, 2);
					if (DP3.getCapacidades().contains(3)) {
						crias *= 2;
					}
					if (DP3.getReprod() + crias > 20) {
						crias = 20 - DP3.getReprod();
					}
					SocketManager.ENVIAR_Im_INFORMACION(_out, "1111;" + crias);
					Dragossauros DragoPadre = World.getDragopavoPorID(DP3.getPareja());
					for (int i = 0; i < crias; ++i) {
						int color = DragoPadre != null ? Constantes.colorCria(DP3.getColor(), DragoPadre.getColor())
								: Constantes.colorCria(DP3.getColor(), DP3.getColor());
						Dragossauros drago = new Dragossauros(color, DP3, DragoPadre);
						SocketManager.ENVIAR_Ee_MONTURA_A_ESTABLO(_perso, '~', drago.detallesMontura());
						DP3.aumReproduccion();
						_cuenta.getEstablo().add(drago);
					}
					DP3.resAmor(7500);
					DP3.resResistencia(7500);
					DP3.setFecundadaHace(-1);
				} else if (DP3.getFecundadaHace() > 1440) {
					SocketManager.ENVIAR_Im_INFORMACION(_out, "1112");
					DP3.aumReproduccion();
					DP3.resAmor(7500);
					DP3.resResistencia(7500);
					DP3.setFecundadaHace(-1);
				}
				SocketManager.ENVIAR_Ee_MONTURA_A_ESTABLO(_perso, '+', DP3.detallesMontura());
				SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_perso.getMapa(), id);
				DP3.setMapaCelda((short) -1, (short) -1);
				break;
			}
			case 'p': {
				Maps mapa = _perso.getMapa();
				if (mapa.getCercado().getListaCriando().size() >= mapa.getCercado().getTama\u00f1o()) {
					SocketManager.ENVIAR_Im_INFORMACION(_out, "1107");
					return;
				}
				if (_perso.getMontura() != null && _perso.getMontura().getID() == id) {
					if (_perso.estaMontando()) {
						_perso.subirBajarMontura();
					}
					if (_perso.estaMontando()) {
						return;
					}
					_perso.setMontura(null);
				}
				Dragossauros DP2 = World.getDragopavoPorID(id);
				DP2.setDue\u00f1o(_perso.getID());
				_cuenta.getEstablo().remove(DP2);
				mapa.getCercado().addCriando(id);
				SocketManager.ENVIAR_Ef_MONTURA_A_CRIAR(_perso, '+', DP2.detallesMontura());
				SocketManager.ENVIAR_Ee_MONTURA_A_ESTABLO(_perso, '-', String.valueOf(DP2.getID()));
				SocketManager.ENVIAR_GM_DRAGOPAVO_A_MAPA(mapa, DP2);
				DP2.setMapaCelda(mapa.getID(), mapa.getCercado().getColocarCelda());
			}
			}
		}
	}

	private synchronized void intercambio_Establo(String packet) {
		if (_perso.getEnCercado() != null) {
			if (_perso.getDeshonor() >= 5) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "183");
				SocketManager.ENVIAR_EV_CERRAR_VENTANAS(_out);
				return;
			}
			char c = packet.charAt(2);
			packet = packet.substring(3);
			int id = -1;
			try {
				id = Integer.parseInt(packet);
			} catch (Exception exception) {
				// empty catch block
			}
			switch (c) {
			case 'C': {
				if (id == -1 || !_perso.tieneObjetoID(id)) {
					return;
				}
				Objeto obj = World.getObjeto(id);
				int DPid = obj.getStats().getEfecto(995);
				Dragossauros DP = World.getDragopavoPorID(-DPid);
				if (DP == null) {
					int color = Constantes.getColorDragoPavoPorPerga(obj.getModelo().getID());
					if (color < 1) {
						return;
					}
					DP = new Dragossauros(color, _perso.getID());
				}
				if (obj.getCantidad() == 1) {
					_perso.borrarObjetoSinEliminar(id);
					World.eliminarObjeto(id);
					SocketManager.ENVIAR_OR_ELIMINAR_OBJETO(_out, id);
				} else {
					obj.setCantidad(obj.getCantidad() - 1);
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, obj);
				}
				if (!_cuenta.getEstablo().contains(DP)) {
					_cuenta.getEstablo().add(DP);
				}
				if (DP.getFecundadaHace() >= DP.minutosParir() && DP.getFecundadaHace() <= 1440) {
					int crias = Fórmulas.getRandomValor(1, 2);
					if (DP.getCapacidades().contains(3)) {
						crias *= 2;
					}
					if (DP.getReprod() + crias > 20) {
						crias = 20 - DP.getReprod();
					}
					SocketManager.ENVIAR_Im_INFORMACION(_out, "1111;" + crias);
					Dragossauros DragoPadre = World.getDragopavoPorID(DP.getPareja());
					for (int i = 0; i < crias; ++i) {
						int color = DragoPadre != null ? Constantes.colorCria(DP.getColor(), DragoPadre.getColor())
								: Constantes.colorCria(DP.getColor(), DP.getColor());
						Dragossauros Drago = new Dragossauros(color, DP, DragoPadre);
						SocketManager.ENVIAR_Ee_MONTURA_A_ESTABLO(_perso, '~', Drago.detallesMontura());
						DP.aumReproduccion();
						_cuenta.getEstablo().add(Drago);
					}
					DP.resAmor(7500);
					DP.resResistencia(7500);
					DP.setFecundadaHace(-1);
				} else if (DP.getFecundadaHace() > 1440) {
					SocketManager.ENVIAR_Im_INFORMACION(_out, "1112");
					DP.aumReproduccion();
					DP.resAmor(7500);
					DP.resResistencia(7500);
					DP.setFecundadaHace(-1);
				}
				SocketManager.ENVIAR_Ee_MONTURA_A_ESTABLO(_perso, '+', DP.detallesMontura());
				break;
			}
			case 'c': {
				Dragossauros DP1 = World.getDragopavoPorID(id);
				if (!_cuenta.getEstablo().contains(DP1) || DP1 == null) {
					return;
				}
				_cuenta.getEstablo().remove(DP1);
				Objeto.ObjetoModelo OM = Constantes.getPergaPorColorDragopavo(DP1.getColor());
				Objeto obj1 = OM.crearObjDesdeModelo(1, false);
				World.addObjeto(obj1, true);
				obj1.clearTodo();
				obj1.getStats().addStat(995, -DP1.getID());
				obj1.addTextoStat(996, _perso.getNombre());
				obj1.addTextoStat(997, DP1.getNombre());
				_perso.addObjetoPut(obj1);
				SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, obj1);
				SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
				SocketManager.ENVIAR_Ee_MONTURA_A_ESTABLO(_perso, '-', String.valueOf(DP1.getID()));
				break;
			}
			case 'g': {
				Dragossauros DP3 = World.getDragopavoPorID(id);
				if (!_cuenta.getEstablo().contains(DP3) || DP3 == null) {
					SocketManager.ENVIAR_Im_INFORMACION(_out, "1104");
					return;
				}
				if (_perso.getMontura() != null) {
					SocketManager.ENVIAR_BN_NADA(_out);
					return;
				}
				_cuenta.getEstablo().remove(DP3);
				_perso.setMontura(DP3);
				SocketManager.ENVIAR_Re_DETALLES_MONTURA(_perso, "+", DP3);
				SocketManager.ENVIAR_Ee_MONTURA_A_ESTABLO(_perso, '-', String.valueOf(DP3.getID()));
				SocketManager.ENVIAR_Rx_EXP_DONADA_MONTURA(_perso);
				break;
			}
			case 'p': {
				if (_perso.getMontura() == null || _perso.getMontura().getID() != id)
					break;
				Dragossauros DP2 = _perso.getMontura();
				if (DP2.getObjetos().size() == 0) {
					if (_perso.estaMontando()) {
						_perso.subirBajarMontura();
					}
					if (!_cuenta.getEstablo().contains(DP2)) {
						_cuenta.getEstablo().add(DP2);
					}
					_perso.setMontura(null);
					if (DP2.getFecundadaHace() >= DP2.minutosParir() && DP2.getFecundadaHace() <= 1440) {
						int crias = Fórmulas.getRandomValor(1, 2);
						if (DP2.getCapacidades().contains(3)) {
							crias *= 2;
						}
						if (DP2.getReprod() + crias > 20) {
							crias = 20 - DP2.getReprod();
						}
						SocketManager.ENVIAR_Im_INFORMACION(_out, "1111;" + crias);
						Dragossauros DragoPadre = World.getDragopavoPorID(DP2.getPareja());
						for (int i = 0; i < crias; ++i) {
							int color = DragoPadre != null ? Constantes.colorCria(DP2.getColor(), DragoPadre.getColor())
									: Constantes.colorCria(DP2.getColor(), DP2.getColor());
							Dragossauros Drago = new Dragossauros(color, DP2, DragoPadre);
							SocketManager.ENVIAR_Ee_MONTURA_A_ESTABLO(_perso, '~', Drago.detallesMontura());
							DP2.aumReproduccion();
							_cuenta.getEstablo().add(Drago);
						}
						DP2.resAmor(7500);
						DP2.resResistencia(7500);
						DP2.setFecundadaHace(-1);
					} else if (DP2.getFecundadaHace() > 1440) {
						SocketManager.ENVIAR_Im_INFORMACION(_out, "1112");
						DP2.aumReproduccion();
						DP2.resAmor(7500);
						DP2.resResistencia(7500);
						DP2.setFecundadaHace(-1);
					}
					SocketManager.ENVIAR_Ee_MONTURA_A_ESTABLO(_perso, '+', DP2.detallesMontura());
					SocketManager.ENVIAR_Re_DETALLES_MONTURA(_perso, "-", null);
					SocketManager.ENVIAR_Rx_EXP_DONADA_MONTURA(_perso);
					break;
				}
				SocketManager.ENVIAR_Im_INFORMACION(_out, "106");
			}
			}
		}
	}

	private void intercambio_Repetir() {
		if (_perso.getHaciendoTrabajo() != null) {
			_perso.getHaciendoTrabajo().ponerIngredUltRecet();
		}
	}

	private void intercambio_Vender(String packet) {
		try {
			String[] infos = packet.substring(2).split("\\|");
			int id = Integer.parseInt(infos[0]);
			int cant = Integer.parseInt(infos[1]);
			if (!_perso.tieneObjetoID(id)) {
				SocketManager.ENVIAR_ESE_ERROR_VENTA(_out);
				return;
			}
			_perso.venderObjeto(id, cant);
		} catch (Exception e) {
			SocketManager.ENVIAR_ESE_ERROR_VENTA(_out);
		}
	}

	private void intercambio_Comprar(String packet) {
		block27: {
			String[] infos = packet.substring(2).split("\\|");
			if (_perso.getIntercambiandoCon() < 0) {
				try {
					int idObjModelo = 0;
					int cantidad = 0;
					try {
						idObjModelo = Integer.parseInt(infos[0]);
						cantidad = Integer.parseInt(infos[1]);
					} catch (NumberFormatException numberFormatException) {
						// empty catch block
					}
					if (cantidad <= 0 || idObjModelo <= 0) {
						return;
					}
					Objeto.ObjetoModelo objModelo = World.getObjModelo(idObjModelo);
					if (objModelo == null) {
						SocketManager.ENVIAR_EBE_ERROR_DE_COMPRA(_out);
						return;
					}
					NPC_tmpl.NPC npc = _perso.getMapa().getNPC(_perso.getIntercambiandoCon());
					if (npc == null) {
						SocketManager.ENVIAR_EBE_ERROR_DE_COMPRA(_out);
						return;
					}
					NPC_tmpl npcMod = npc.getModeloBD();
					if (npcMod == null || !npcMod.tieneObjeto(idObjModelo)) {
						SocketManager.ENVIAR_EBE_ERROR_DE_COMPRA(_out);
						return;
					}
					int precioUnitario = objModelo.getPrecio();
					int precioVIP = objModelo.getPrecioVIP();
					int precio = precioUnitario * cantidad;
					if (precioUnitario == 0 && precioVIP > 0) {
						precio = precioVIP * cantidad;
						int mispuntos = SQLManager.getPuntosCuenta(_perso.getCuentaID());
						if (mispuntos < precio) {
							SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out,
									"<b>A\u00e7\u00e3o impossivel</b>: Falta " + (precio - mispuntos) + " pontos.");
							return;
						}
						int puntosnuevos = mispuntos - precio;
						SQLManager.setPuntoCuenta(puntosnuevos, _perso.getCuentaID());
						SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out,
								"<b>Les Guardians</b>: Obrigado pela sua compra, voc\u00ea tem<b> " + puntosnuevos
										+ " </b>pontos.");
					} else {
						if (npcMod.getID() == 809) {
							_perso.setKamas(0L);
							SocketManager.ENVIAR_EBK_COMPRADO(_out);
							SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
							return;
						}
						if (_perso.getKamas() < (long) precio) {
							SocketManager.ENVIAR_EBE_ERROR_DE_COMPRA(_out);
							return;
						}
						long nuevasKamas = _perso.getKamas() - (long) precio;
						_perso.setKamas(nuevasKamas);
					}
					Objeto nuevoObj = null;
					nuevoObj = objModelo.crearObjDesdeModelo(cantidad, false);
					if (!_perso.addObjetoSimilar(nuevoObj, true, -1)) {
						World.addObjeto(nuevoObj, true);
						_perso.addObjetoPut(nuevoObj);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, nuevoObj);
					}
					SocketManager.ENVIAR_EBK_COMPRADO(_out);
					SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
					SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
				} catch (Exception e) {
					e.printStackTrace();
					SocketManager.ENVIAR_EBE_ERROR_DE_COMPRA(_out);
					return;
				}
			}
			Personagens mercante = World.getPersonaje(_perso.getIntercambiandoCon());
			try {
				int id = Integer.parseInt(infos[0]);
				int cant = Integer.parseInt(infos[1]);
				if (cant <= 0) {
					return;
				}
				Objeto objeto = World.getObjeto(id);
				Stockage tienda = World.getObjTienda(id);
				if (objeto == null || tienda == null) {
					SocketManager.ENVIAR_EBE_ERROR_DE_COMPRA(_out);
					return;
				}
				long precio = tienda.getPrecio() * (long) cant;
				if (_perso.getKamas() < precio) {
					SocketManager.ENVIAR_EBE_ERROR_DE_COMPRA(_out);
					return;
				}
				int cantObjeto = objeto.getCantidad();
				if (cant == cantObjeto) {
					long nuevasKamas = _perso.getKamas() - precio;
					_perso.setKamas(nuevasKamas);
					mercante.setKamas(mercante.getKamas() + precio);
					mercante.borrarObjTienda(objeto);
					if (_perso.addObjetoSimilar(objeto, true, -1)) {
						World.eliminarObjeto(id);
					} else {
						_perso.addObjetoPut(objeto);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, objeto);
					}
					SocketManager.ENVIAR_EBK_COMPRADO(_out);
					SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
					SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
					if (!mercante.quedaObjTienda()) {
						_perso.getMapa().removerMercante(mercante.getID());
						mercante.setMercante(0);
						SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(_perso.getMapa(), mercante.getID());
						intercambio_Cerrar();
					} else {
						SocketManager.ENVIAR_EL_LISTA_TIENDA_PERSONAJE(_out, mercante);
					}
					break block27;
				}
				if (cant < cantObjeto) {
					Objeto nuevoObj = Objeto.clonarObjeto(objeto, cant);
					int nuevaCant = cantObjeto - cant;
					objeto.setCantidad(nuevaCant);
					tienda.setCantidad(nuevaCant);
					SocketManager.ENVIAR_OQ_CAMBIA_CANTIDAD_DEL_OBJETO(_perso, objeto);
					long nuevasKamas = _perso.getKamas() - precio;
					_perso.setKamas(nuevasKamas);
					mercante.setKamas(mercante.getKamas() + precio);
					if (!_perso.addObjetoSimilar(nuevoObj, true, id)) {
						World.addObjeto(nuevoObj, true);
						_perso.addObjetoPut(nuevoObj);
						SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, nuevoObj);
					}
					SocketManager.ENVIAR_EBK_COMPRADO(_out);
					SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
					SocketManager.ENVIAR_Ow_PODS_DEL_PJ(_perso);
					SocketManager.ENVIAR_EL_LISTA_TIENDA_PERSONAJE(_out, mercante);
					break block27;
				}
				SocketManager.ENVIAR_EBE_ERROR_DE_COMPRA(_out);
				return;
			} catch (Exception e) {
				SocketManager.ENVIAR_EBE_ERROR_DE_COMPRA(_out);
				SocketManager.ENVIAR_EL_LISTA_TIENDA_PERSONAJE(_out, mercante);
				return;
			}
		}
	}

	private void analizar_Ambiente(String packet) {
		switch (packet.charAt(1)) {
		case 'D': {
			ambiente_Cambio_Direccion(packet);
			break;
		}
		case 'U': {
			ambiente_Emote(packet);
		}
		}
	}

	private void ambiente_Emote(String packet) {
		int emote = -1;
		try {
			emote = Integer.parseInt(packet.substring(2));
		} catch (Exception exception) {
			// empty catch block
		}
		if (emote == -1 || _perso.getPelea() != null) {
			return;
		}
		switch (emote) {
		case 1:
		case 19: {
			if (_perso.estaSentado()) {
				emote = 0;
			}
			_perso.setSentado(!_perso.estaSentado());
		}
		}
		_perso.setEmoteActivado(emote);
		String tiempo = "";
		if (emote == 7) {
			tiempo = "9000";
		} else if (emote == 21) {
			tiempo = "5000";
		}
		Maps.Cercado cercado = _perso.getMapa().getCercado();
		SocketManager.ENVIAR_eUK_EMOTE_MAPA(_perso.getMapa(), _perso.getID(), emote, tiempo);
		if ((emote == 2 || emote == 4 || emote == 3 || emote == 6 || emote == 8 || emote == 10) && cercado != null) {
			ArrayList<Dragossauros> pavos = new ArrayList<Dragossauros>();
			for (int pavo : cercado.getListaCriando()) {
				if (World.getDragopavoPorID(pavo).getDue\u00f1o() != _perso.getID())
					continue;
				pavos.add(World.getDragopavoPorID(pavo));
			}
			if (pavos.size() > 0) {
				int casillas = 0;
				switch (emote) {
				case 2:
				case 4: {
					casillas = 1;
					break;
				}
				case 3:
				case 8: {
					casillas = Fórmulas.getRandomValor(2, 3);
					break;
				}
				case 6:
				case 10: {
					casillas = Fórmulas.getRandomValor(4, 7);
				}
				}
				boolean alejar = emote != 2 && emote != 3 && emote != 10;
				Dragossauros dragopavo = (Dragossauros) pavos.get(Fórmulas.getRandomValor(0, pavos.size() - 1));
				dragopavo.moverMontura(_perso, casillas, alejar);
			}
		}
	}

	private void ambiente_Cambio_Direccion(String packet) {
		try {
			if (_perso.getPelea() != null) {
				return;
			}
			byte dir = Byte.parseByte(packet.substring(2));
			_perso.setOrientacion(dir);
			SocketManager.ENVIAR_eD_CAMBIAR_ORIENTACION(_perso.getMapa(), _perso.getID(), dir);
		} catch (NumberFormatException e) {
			return;
		}
	}

	private void analizar_Hechizos(String packet) {
		switch (packet.charAt(1)) {
		case 'B': {
			hechizos_Boost(packet);
			break;
		}
		case 'F': {
			hechizos_Olvidar(packet);
			break;
		}
		case 'M': {
			hechizos_Acceso_Rapido(packet);
		}
		}
	}

	private void hechizos_Acceso_Rapido(String packet) {
		try {
			int hechizoID = Integer.parseInt(packet.substring(2).split("\\|")[0]);
			int posicion = Integer.parseInt(packet.substring(2).split("\\|")[1]);
			Spell.StatsHechizos hechizo = _perso.getStatsHechizo(hechizoID);
			if (hechizo != null) {
				_perso.setPosHechizo(hechizoID, CryptManager.getValorHashPorNumero(posicion));
				if (hechizoID == 183 && posicion == 12) {
					SocketManager.ENVIAR_BAIO_MENSAJE_PANEL(_perso, "");
				}
			}
			SocketManager.ENVIAR_BN_NADA(_out);
		} catch (Exception exception) {
			// empty catch block
		}
	}

	private void hechizos_Boost(String packet) {
		try {
			int id = Integer.parseInt(packet.substring(2));
			if (!_perso.boostearHechizo(id)) {
				SocketManager.ENVIAR_SUE_SUBIR_NIVEL_HECHIZO_ERROR(_out);
				return;
			}
			SocketManager.ENVIAR_SUK_SUBIR_NIVEL_HECHIZO(_out, id, _perso.getStatsHechizo(id).getNivel());
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
		} catch (Exception e) {
			SocketManager.ENVIAR_SUE_SUBIR_NIVEL_HECHIZO_ERROR(_out);
			return;
		}
	}

	private void hechizos_Olvidar(String packet) {
		if (!_perso.estaOlvidandoHechizo()) {
			return;
		}
		int id = Integer.parseInt(packet.substring(2));
		if (_perso.olvidarHechizo(id)) {
			SocketManager.ENVIAR_SUK_SUBIR_NIVEL_HECHIZO(_out, id, _perso.getStatsHechizo(id).getNivel());
			SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
			_perso.setOlvidandoHechizo(false);
		}
	}

	private void analizar_Peleas(String packet) {
		Fight pelea = _perso.getPelea();
		switch (packet.charAt(1)) {
		case 'D': {
			short key = -1;
			try {
				key = Short.parseShort(packet.substring(2).replace("0", ""));
			} catch (Exception exception) {
				// empty catch block
			}
			if (key == -1) {
				return;
			}
			SocketManager.ENVIAR_fD_DETALLES_PELEA(_out, _perso.getMapa().getPeleas().get(key));
			break;
		}
		case 'H': {
			if (pelea == null) {
				return;
			}
			pelea.botonAyuda(_perso.getID());
			SocketManager.ENVIAR_BN_NADA(_out);
			break;
		}
		case 'L': {
			SocketManager.ENVIAR_fL_LISTA_PELEAS(_out, _perso.getMapa());
			break;
		}
		case 'N': {
			if (pelea == null) {
				return;
			}
			pelea.botonBloquearMasJug(_perso.getID());
			SocketManager.ENVIAR_BN_NADA(_out);
			break;
		}
		case 'P': {
			if (pelea == null || _perso.getGrupo() == null) {
				return;
			}
			pelea.botonSoloGrupo(_perso.getID());
			SocketManager.ENVIAR_BN_NADA(_out);
			break;
		}
		case 'S': {
			if (pelea == null) {
				return;
			}
			pelea.botonBloquearEspect(_perso.getID());
			SocketManager.ENVIAR_BN_NADA(_out);
		}
		}
	}

	private void analizar_Basicos(String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			basicos_Consola(packet);
			break;
		}
		case 'D': {
			basicos_Enviar_Fecha();
			break;
		}
		case 'M': {
			basicos_Chat(packet);
			break;
		}
		case 'W': {
			basicos_Mensaje_Informacion(packet);
			break;
		}
		case 'S': {
			_perso.emote(packet.substring(2));
			break;
		}
		case 'Y': {
			basicos_Estado(packet);
		}
		}
	}

	public void basicos_Estado(String packet) {
		switch (packet.charAt(2)) {
		case 'A': {
			if (_perso.estaAusente()) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "038");
				_perso.setEstaAusente(false);
				break;
			}
			SocketManager.ENVIAR_Im_INFORMACION(_out, "037");
			_perso.setEstaAusente(true);
			break;
		}
		case 'I': {
			if (_perso.esInvisible()) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "051");
				_perso.setEsInvisible(false);
				break;
			}
			SocketManager.ENVIAR_Im_INFORMACION(_out, "050");
			_perso.setEsInvisible(true);
		}
		}
	}

	private void basicos_Consola(String packet) {
		if (_comando == null) {
			_comando = new Comandos(_perso);
		}
		_comando.consolaComando(packet);
	}

	private void basicos_Chat(String packet) {
		String mensajeC = "";
		if (_perso.estaMuteado()) {
			Conta cuenta = _perso.getCuenta();
			long tiempoTrans = System.currentTimeMillis() - cuenta._horaMuteada;
			if (tiempoTrans > cuenta._tiempoMuteado) {
				cuenta.mutear(false, 0);
			} else {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "1124;" + (cuenta._tiempoMuteado - tiempoTrans) / 1000L);
				return;
			}
		}
		packet = packet.replace("<", "");
		if ((packet = packet.replace(">", "")).length() == 3) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
		switch (packet.charAt(2)) {
		case '*': {
			if (!_perso.getCanal().contains(String.valueOf(packet.charAt(2)))) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			mensajeC = packet.split("\\|", 2)[1];
			if (mensajeC.length() <= 0) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			if (mensajeC.charAt(0) == '.') {
				try {
					Fight pelea;
					String[] infos;
					String mensaje;
					if (mensajeC.length() > 4 && mensajeC.substring(1, 5).equalsIgnoreCase("shop")) {
						if (_perso.getKamas() < 5000L) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									"Voce n\u00e3o tem kamas suficientes! <b>5.000k</b>", LesGuardians.COR_MSG);
							return;
						}
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport(LesGuardians.CONFIG_MAP_SHOP, LesGuardians.CONFIG_CELL_SHOP);
						SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso, "Voc\u00ea foi teleportado para <b>Mapa Shop.</b>",
								LesGuardians.COR_MSG);
						return;
					}
					if (mensajeC.length() > 8 && mensajeC.substring(1, 9).equalsIgnoreCase("comandos")) {
						SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
								"<b>Les Guardians Emu 2.7.5 - Comandos</b> :\n\n \n.<b>info</b> - Mostra informa\u00e7\u00f5es do servidor.\n.<b>salvar</b> - Salva seu personagem.\n.<b>shop</b> - Teleporta para o Mapa Shop.\n.<b>neutro</b> - Muda seu alinhamento para neutro.\n.<b>vida</b> - Enche sua vida.\n\n<b>===== Locais de UP =====</b>\n.<b>uplow</b> - Teleporta para <b>Mapa LvL Baixo</b>.\n.<b>upmedium</b> - Teleporta para <b>Mapa LvL M\u00e9dio</b>.\n.<b>uphigh</b> - Teleporta para <b>Mapa LvL Alto</b>.\n\n===== <b>Teleports</b> =====\n.<b>padoque</b> - Teleporta para o <b>Padoque P\u00fablico</b>.\n.<b>prof</b> - Teleporta para a <b>Oficina</b>.\n.<b>astrub</b> - Teleporta para <b>Astrub</b>.\n\n<b>===== Desbugs =====</b>\n.<b>passarturno</b> - Passa o turno do jogador bugado.\n.<b>iniciarluta</b> - Inicia uma luta bugada.\n.<b>atacar</b> - Faz o monstro bugado atacar.",
								LesGuardians.COR_MSG);
						return;
					}
					if (mensajeC.length() > 3 && mensajeC.substring(1, 4).equalsIgnoreCase("pvp")) {
						if (_perso.getKamas() < 5000L) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									"Voce n\u00e3o tem kamas suficientes! <b>5.000K</b>", LesGuardians.COR_MSG);
							return;
						}
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 952, (short) 77);
						SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
								"Voc\u00ea foi teleportado para o <b>Mapa PVP.</b>", LesGuardians.COR_MSG);
						return;
					}
					if (mensajeC.length() > 4 && mensajeC.substring(1, 5).equalsIgnoreCase("prof")) {
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 8731, (short) 337);
						SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso, "Voc\u00ea foi teleportado para a <b>Oficina.</b>",
								LesGuardians.COR_MSG);
						return;
					}
					if (mensajeC.length() > 14 && mensajeC.substring(1, 15).equalsIgnoreCase("atualizarconta")) {
						_cuenta.getPersonajes().clear();
						SQLManager.CARGAR_PERSONAJES_POR_CUENTA(_cuenta);
						String pjs = "";
						boolean primero = false;
						for (Personagens perso : _cuenta.getPersonajes().values()) {
							if (primero) {
								pjs = String.valueOf(pjs) + ",";
							}
							pjs = String.valueOf(pjs) + perso.getNombre();
							primero = true;
						}
						SocketManager.ENVIAR_M145_MENSAJE_PANEL_INFORMACION(_out, "A conta " + _cuenta.getNombre()
								+ " atualizou " + _cuenta.getPersonajes().size() + " personagens: " + pjs);
						return;
					}
					if (mensajeC.length() > 4 && mensajeC.substring(1, 5).equalsIgnoreCase("alvo")) {
						try {
							mensajeC = mensajeC.substring(0, mensajeC.length() - 1);
							String[] r = mensajeC.split(" ", 3);
							String nombreVictima = r[1];
							Personagens victima = World.getPjPorNombre(nombreVictima);
							if (victima == null || !victima.enLinea()) {
								SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out, "O personagem n\u00e3o existe.");
								return;
							}
							int kamas = 0;
							try {
								kamas = Integer.parseInt(r[2]);
							} catch (Exception e) {
								SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out, "Coloque um valor correto.");
								return;
							}
							if (kamas < 1000) {
								SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out,
										"A recompensa precisa ser maior que: <b>1000 kamas</b>");
								return;
							}
							int impuesto = kamas / 100;
							if (_perso.getKamas() < (long) (kamas + impuesto)) {
								SocketManager.ENVIAR_Im_INFORMACION(_out, "182");
								return;
							}
							_perso.setKamas(_perso.getKamas() - (long) (kamas + impuesto));
							Objeto.ObjetoModelo objModelo = World.getObjModelo(9995);
							Objeto nuevoObj = objModelo.crearObjDesdeModelo(10, false);
							nuevoObj.addTextoStat(989, victima.getNombre());
							nuevoObj.addTextoStat(962, Integer.toHexString(victima.getNivel()));
							nuevoObj.addStat(194, kamas);
							World.addObjeto(nuevoObj, true);
							_perso.addObjetoPut(nuevoObj);
							SocketManager.ENVIAR_OAKO_APARECER_OBJETO(_out, nuevoObj);
							SocketManager.ENVIAR_Im_INFORMACION(_perso, "021;10~9995");
						} catch (Exception e) {
							SocketManager.ENVIAR_BN_NADA(_out);
						}
						return;
					}
					if (mensajeC.length() > 8 && mensajeC.substring(1, 9).equalsIgnoreCase("desbugar")) {
						if (_perso.getPelea() != null) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						if (mensajeC.split(" ").length > 2) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						mensaje = mensajeC.split(" ", 2)[1];
						String mensajet = mensaje.substring(0, mensaje.length() - 1);
						short mapaID = (short) Integer.parseInt(mensajet);
						Maps mapa = World.getMapa(mapaID);
						if (mapa == null) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						mapa.borrarJugador(_perso.getID());
						SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapa, _perso.getID());
						_perso.teleport(_perso.getMapa().getID(), _perso.getCelda().getID());
						return;
					}
					if (mensajeC.length() > 4 && mensajeC.substring(1, 5).equalsIgnoreCase("info")) {
						_perso.INFOS();
						return;
					}
					if (mensajeC.length() > 2 && mensajeC.substring(1, 3).equalsIgnoreCase("ok")) {
						if (_perso.getEnKoliseo()) {
							_perso.setAceptar(true);
						}
						return;
					}
					if (mensajeC.length() > 12 && mensajeC.substring(1, 13).equalsIgnoreCase("desbugarmapa")) {
						if (_perso.getPelea() != null) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						if (mensajeC.split(" ").length > 2) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						mensaje = mensajeC.split(" ", 2)[1];
						String mensajet = mensaje.substring(0, mensaje.length() - 1);
						short mapaID = (short) Integer.parseInt(mensajet);
						Maps mapa = World.getMapa(mapaID);
						if (mapa == null) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						mapa.borrarJugador(_perso.getID());
						SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapa, _perso.getID());
						_perso.teleport(_perso.getMapa().getID(), _perso.getCelda().getID());
						return;
					}
					if (mensajeC.length() > 6 && mensajeC.substring(1, 7).equalsIgnoreCase("salvar")
							|| mensajeC.length() > 4 && mensajeC.substring(1, 5).equalsIgnoreCase("save")) {
						if (System.currentTimeMillis() - _tiempoUltSalvada < 360000L || _perso.getPelea() != null) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						_tiempoUltSalvada = System.currentTimeMillis();
						SQLManager.SALVAR_PERSONAJE(_perso, true);
						if (mensajeC.substring(1, 5).equalsIgnoreCase("save")) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									String.valueOf(_perso.getNombre()) + " foi <b>salvo</b> com sucesso.",
									LesGuardians.COR_MSG);
						} else {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									String.valueOf(_perso.getNombre()) + " foi <b>salvo</b> corretamente.",
									LesGuardians.COR_MSG);
						}
						return;
					}
					if (mensajeC.substring(1, 7).equalsIgnoreCase("pontos")) {
						SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso, "Voc\u00ea possui "
								+ SQLManager.getPuntosCuenta(_perso.getCuenta().getID()) + " pontos na loja.",
								LesGuardians.COR_MSG);
						return;
					}
					if (mensajeC.length() > 5 && mensajeC.substring(1, 6).equalsIgnoreCase("feira")) {
						if (_perso.getKamas() < 5000L) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									"Voce n\u00e3o tem kamas suficientes! <b>5.000k</b>", LesGuardians.COR_MSG);
							return;
						}
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 6863, (short) 324);
						return;
					}
					if (mensajeC.length() > 5 && mensajeC.substring(1, 6).equalsIgnoreCase("fenix")) {
						if (_perso.getKamas() < 5000L) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									"Voce n\u00e3o tem kamas suficientes! <b>5.000k</b>", LesGuardians.COR_MSG);
							return;
						}
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 8534, (short) 267);
						return;
					}
					if (mensajeC.length() > 4 && mensajeC.substring(1, 5).equalsIgnoreCase("prof")) {
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 8731, (short) 337);
						return;
					}
					if (mensajeC.length() > 6 && mensajeC.substring(1, 7).equalsIgnoreCase("astrub")) {
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 7411, (short) 311);
						return;
					}
					if (mensajeC.length() > 5 && mensajeC.substring(1, 6).equalsIgnoreCase("uplow")) {
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 10340, (short) 222);
						return;
					}
					if (mensajeC.length() > 8 && mensajeC.substring(1, 9).equalsIgnoreCase("upmedium")) {
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 11210, (short) 168);
						return;
					}
					if (mensajeC.length() > 6 && mensajeC.substring(1, 7).equalsIgnoreCase("uphigh")) {
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 11893, (short) 209);
						return;
					}
					if (mensajeC.length() > 1 && mensajeC.substring(1, 2).equalsIgnoreCase("v")
							&& _cuenta.getVIP() >= 1) {
						infos = mensajeC.split(" ", 2);
						SocketManager.GAME_SEND_MESSAGE_VIP(infos[1], _cuenta.getRango(), _perso.getNombre());
						return;
					}
					if (mensajeC.length() > 1 && mensajeC.substring(1, 2).equalsIgnoreCase("a")
							&& _cuenta.getRango() >= 5) {
						infos = mensajeC.split(" ", 2);
						SocketManager.GAME_SEND_MESSAGE_ANNOUNCE(infos[1], _cuenta.getRango(), _perso.getNombre());
						return;
					}
					if (mensajeC.length() > 1 && mensajeC.substring(1, 2).equalsIgnoreCase("e")
							&& _cuenta.getRango() >= 3) {
						infos = mensajeC.split(" ", 2);
						SocketManager.GAME_SEND_MESSAGE_EVENTOS(infos[1], _cuenta.getRango(), _perso.getNombre());
						return;
					}
					if (mensajeC.length() > 8 && mensajeC.substring(1, 9).equalsIgnoreCase("scrollar")) {
						if (_perso.getPelea() != null) {
							return;
						}
						String element = "";
						int nbreElement = 0;
						if (_perso.getBaseStats().getEfecto(125) < 101) {
							_perso.getBaseStats().addStat(125, 101 - _perso.getBaseStats().getEfecto(125));
							element = String.valueOf(element) + "Vitalidade";
							++nbreElement;
						}
						if (_perso.getBaseStats().getEfecto(124) < 101) {
							_perso.getBaseStats().addStat(124, 101 - _perso.getBaseStats().getEfecto(124));
							element = nbreElement == 0 ? String.valueOf(element) + "sagesse"
									: String.valueOf(element) + ", sabedoria";
							++nbreElement;
						}
						if (_perso.getBaseStats().getEfecto(118) < 101) {
							_perso.getBaseStats().addStat(118, 101 - _perso.getBaseStats().getEfecto(118));
							element = nbreElement == 0 ? String.valueOf(element) + "force"
									: String.valueOf(element) + ", for\u00e7a";
							++nbreElement;
						}
						if (_perso.getBaseStats().getEfecto(126) < 101) {
							_perso.getBaseStats().addStat(126, 101 - _perso.getBaseStats().getEfecto(126));
							element = nbreElement == 0 ? String.valueOf(element) + "intelligence"
									: String.valueOf(element) + ", intelig\u00eancia";
							++nbreElement;
						}
						if (_perso.getBaseStats().getEfecto(119) < 101) {
							_perso.getBaseStats().addStat(119, 101 - _perso.getBaseStats().getEfecto(119));
							element = nbreElement == 0 ? String.valueOf(element) + "agilit\u00e9"
									: String.valueOf(element) + ", agilidade";
							++nbreElement;
						}
						if (_perso.getBaseStats().getEfecto(123) < 101) {
							_perso.getBaseStats().addStat(123, 101 - _perso.getBaseStats().getEfecto(123));
							element = nbreElement == 0 ? String.valueOf(element) + "chance"
									: String.valueOf(element) + " e chance.";
							++nbreElement;
						}
						if (nbreElement == 0) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									"<i><b>Les Guardians Emu</b></i> : A\u00e7\u00e3o <b>impossivel.</b> .",
									LesGuardians.COR_MSG);
						} else {
							SocketManager.ENVIAR_As_STATS_DEL_PJ(_perso);
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									"<i><b>Les Guardians Emu</b></i> : Voc\u00ea scrollou <b>101</b> em tudo .",
									LesGuardians.COR_MSG);
						}
						return;
					}
					if (mensajeC.length() > 4 && mensajeC.substring(1, 5).equalsIgnoreCase("vida")
							&& _cuenta.getVIP() >= 1) {
						int count = 100;
						Personagens perso = _perso;
						int newPDV = perso.getPDVMAX() * count / 100;
						perso.setPDV(newPDV);
						if (perso.enLinea()) {
							SocketManager.ENVIAR_As_STATS_DEL_PJ(perso);
						}
						SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso, "Voc\u00ea encheu sua <b>vida</b>.",
								LesGuardians.COR_MSG);
						return;
					}
					if (mensajeC.length() > 3 && mensajeC.substring(1, 4).equalsIgnoreCase("vip")
							&& _cuenta.getVIP() >= 1) {
						SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
								"Obrigado por ser membro <b>VIP</b>! Comandos:\n.<b>v</b> - Fala no canal exclusivo <b>VIP</b>!\n.<b>vida</b> - Enche a sua vida em 100%\n.<b>pontos</b> - Mostra os pontos.\n.<b>scrollar</b> - Scrolla seus status em 101. <b>Gratis!</b>\nDigite .<b>comandos</b> para ver todos os comandos disponiveis!\nEm breve mais comandos VIP. Acesse a loja e compre itens exclusivos!",
								LesGuardians.COR_MSG);
						return;
					}
					if (mensajeC.length() > 6 && mensajeC.substring(1, 7).equalsIgnoreCase("guilda")) {
						if (_perso.getKamas() < 10000L) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									"Voce n\u00e3o tem kamas suficientes! <b>10.000k</b>", LesGuardians.COR_MSG);
							return;
						}
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 2056, (short) 369);
						return;
					}
					if (mensajeC.length() > 6 && mensajeC.substring(1, 7).equalsIgnoreCase("igreja")) {
						if (_perso.getKamas() < 5000L) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									"Voce n\u00e3o tem kamas suficientes! <b>5.000k</b>", LesGuardians.COR_MSG);
							return;
						}
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 2019, (short) 366);
						return;
					}
					if (mensajeC.length() > 4 && mensajeC.substring(1, 5).equalsIgnoreCase("info")) {
						_perso.INFOS();
						return;
					}
					if (mensajeC.length() > 5 && mensajeC.substring(1, 6).equalsIgnoreCase("rates")) {
						_perso.RATES();
						return;
					}
					if (mensajeC.length() > 7 && mensajeC.substring(1, 8).equalsIgnoreCase("padoque")) {
						if (_perso.getKamas() < 5000L) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso,
									"Voce n\u00e3o tem kamas suficientes! <b>5.000k</b>", LesGuardians.COR_MSG);
							return;
						}
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.teleport((short) 8747, (short) 709);
						SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso, "Voc\u00ea foi teleportado para o <b>Padoque.</b>",
								LesGuardians.COR_MSG);
						return;
					}
					if (mensajeC.length() > 5 && mensajeC.substring(1, 6).equalsIgnoreCase("staff")) {
						String staff = "<b>Membros da staff ON:</b>\n";
						boolean allOffline = true;
						for (int i = 0; i < World.getPJsEnLinea().size(); ++i) {
							if (World.getPJsEnLinea().get(i).getCuenta().getRango() <= 0)
								continue;
							staff = String.valueOf(staff) + "<b>@</b> " + World.getPJsEnLinea().get(i).getNombre()
									+ " (";
							staff = World.getPJsEnLinea().get(i).getCuenta().getRango() == 2
									? String.valueOf(staff) + "<b>Moderador</b>)"
									: (World.getPJsEnLinea().get(i).getCuenta().getRango() == 3
											? String.valueOf(staff) + "<b>Game Master</b>)"
											: (World.getPJsEnLinea().get(i).getCuenta().getRango() == 4
													? String.valueOf(staff) + "<b>Designer</b>)"
													: (World.getPJsEnLinea().get(i).getCuenta().getRango() == 5
															? String.valueOf(staff) + "<b>Administrador</b>)"
															: String.valueOf(staff) + "Undefined.")));
							staff = String.valueOf(staff) + "\n";
							allOffline = false;
						}
						if (!staff.isEmpty() && !allOffline) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso, staff, LesGuardians.COR_MSG);
						} else if (allOffline) {
							SocketManager.ENVIAR_cs_CHAT_MENSAJE(_perso, "Ninguem da Staff est\u00e1 ON.",
									LesGuardians.COR_MSG);
						}
						return;
					}
					if (mensajeC.length() > 6 && mensajeC.substring(1, 7).equalsIgnoreCase("neutro")) {
						if (_perso.getPelea() != null) {
							return;
						}
						_perso.modificarAlineamiento((byte) 0);
						return;
					}
					if (mensajeC.length() > 6 && mensajeC.substring(1, 7).equalsIgnoreCase("atacar")) {
						pelea = _perso.getPelea();
						if (pelea == null) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						Fight.Luchador luchador = pelea.getLuchadorPorPJ(_perso);
						if (!luchador.puedeJugar()) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						SocketManager.ENVIAR_GAF_FINALIZAR_ACCION(pelea, 7, 2, luchador.getID());
						if (_perso.getClase(true) == 12 && !luchador.tieneEstado(3)) {
							SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(luchador.getID()),
									String.valueOf(luchador.getID()) + "," + 3 + ",0");
						}
						return;
					}
					if (mensajeC.length() > 11 && mensajeC.substring(1, 12).equalsIgnoreCase("iniciarluta")) {
						pelea = _perso.getPelea();
						if (pelea == null) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						if (pelea.getEstado() < 3) {
							pelea.tiempoTurno();
						}
						return;
					}
					if (mensajeC.length() > 11 && mensajeC.substring(1, 12).equalsIgnoreCase("passarturno")) {
						pelea = _perso.getPelea();
						if (pelea == null) {
							SocketManager.ENVIAR_BN_NADA(_out);
							return;
						}
						if (pelea.getEstado() == 3) {
							pelea.tiempoTurno();
						}
						return;
					}
				} catch (Exception pelea) {
					// empty catch block
				}
			}
			if (_perso.getPelea() == null) {
				SocketManager.ENVIAR_cMK_CHAT_MENSAJE_MAPA(_perso.getMapa(), "", _perso.getID(), _perso.getNombre(),
						mensajeC);
				break;
			}
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_PELEA(_perso.getPelea(), 7, "", _perso.getID(), _perso.getNombre(),
					mensajeC);
			break;
		}
		case '#': {
			if (!_perso.getCanal().contains(String.valueOf(packet.charAt(2)))) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			if (_perso.getPelea() == null)
				break;
			mensajeC = packet.split("\\|", 2)[1];
			int equipo = _perso.getPelea().getParamEquipo(_perso.getID());
			if (equipo == -1) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_PELEA(_perso.getPelea(), equipo, "#", _perso.getID(),
					_perso.getNombre(), mensajeC);
			break;
		}
		case '$': {
			if (!_perso.getCanal().contains(String.valueOf(packet.charAt(2))) || _perso.getGrupo() == null) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			mensajeC = packet.split("\\|", 2)[1];
			SocketManager.ENVIAR_cMK_MENSAJE_CHAT_GRUPO(_perso.getGrupo(), "$", _perso.getID(), _perso.getNombre(),
					mensajeC);
			break;
		}
		case ':': {
			if (!_perso.getCanal().contains(String.valueOf(packet.charAt(2)))) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			long l = System.currentTimeMillis() - _tiempoUltComercio;
			if (l < 20000L) {
				l = (20000L - l) / 1000L;
				SocketManager.ENVIAR_Im_INFORMACION(_out, "0115;" + ((int) Math.ceil(l) + 1));
				return;
			}
			_tiempoUltComercio = System.currentTimeMillis();
			mensajeC = packet.split("\\|", 2)[1];
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_TODOS(":", _perso.getID(), _perso.getNombre(), mensajeC);
			break;
		}
		case '@': {
			if (_perso.getCuenta().getRango() < 1) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			mensajeC = packet.split("\\|", 2)[1];
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_ADMINS("@", _perso.getID(), _perso.getNombre(), mensajeC);
			break;
		}
		case '?': {
			if (!_perso.getCanal().contains(String.valueOf(packet.charAt(2)))) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			long j = System.currentTimeMillis() - _tiempoUltReclutamiento;
			if (j < 20000L) {
				j = (20000L - j) / 1000L;
				SocketManager.ENVIAR_Im_INFORMACION(_out, "0115;" + ((int) Math.ceil(j) + 1));
				return;
			}
			_tiempoUltReclutamiento = System.currentTimeMillis();
			mensajeC = packet.split("\\|", 2)[1];
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_TODOS("?", _perso.getID(), _perso.getNombre(), mensajeC);
			break;
		}
		case '%': {
			if (!_perso.getCanal().contains(String.valueOf(packet.charAt(2))) || _perso.getGremio() == null) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			mensajeC = packet.split("\\|", 2)[1];
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_GREMIO(_perso.getGremio(), "%", _perso.getID(), _perso.getNombre(),
					mensajeC);
			break;
		}
		case '\u00c2': {
			SocketManager.ENVIAR_BN_NADA(_out);
			break;
		}
		case '!': {
			if (!_perso.getCanal().contains(String.valueOf(packet.charAt(2))) || _perso.getAlineacion() == 0) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			if (_perso.getDeshonor() >= 1) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "183");
				return;
			}
			long k = System.currentTimeMillis() - _tiempoUltAlineacion;
			if (k < LesGuardians.TEMPO_COMERCIO_RECRUTAMENTO) {
				k = (LesGuardians.TEMPO_COMERCIO_RECRUTAMENTO - k) / 1000L;
				SocketManager.ENVIAR_Im_INFORMACION(_out, "0115;" + ((int) Math.ceil(k) + 1));
				return;
			}
			_tiempoUltAlineacion = System.currentTimeMillis();
			mensajeC = packet.split("\\|", 2)[1];
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_ALINEACION("!", _perso.getID(), _perso.getNombre(), mensajeC, _perso);
			break;
		}
		default: {
			String nombre = packet.substring(2).split("\\|")[0];
			mensajeC = packet.split("\\|", 2)[1];
			if (nombre.length() <= 1)
				break;
			Personagens perso = World.getPjPorNombre(nombre);
			if (perso == null || !perso.enLinea()) {
				SocketManager.ENVIAR_cMEf_CHAT_ERROR(_out, nombre);
				return;
			}
			Conta cuenta = perso.getCuenta();
			if (cuenta == null) {
				SocketManager.ENVIAR_cMEf_CHAT_ERROR(_out, nombre);
				return;
			}
			GameThread gestor = cuenta.getEntradaPersonaje();
			if (gestor == null) {
				SocketManager.ENVIAR_cMEf_CHAT_ERROR(_out, nombre);
				return;
			}
			if (cuenta.esEnemigo(_perso.getCuenta().getID()) || !perso.estaDisponible(_perso)) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "114;" + perso.getNombre());
				return;
			}
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_PERSONAJE(perso, "F", _perso.getID(), _perso.getNombre(), mensajeC);
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_PERSONAJE(_perso, "T", perso.getID(), perso.getNombre(), mensajeC);
		}
		}
	}

	private void basicos_Enviar_Fecha() {
		SocketManager.ENVIAR_BD_FECHA_SERVER(_out);
		SocketManager.ENVIAR_BT_TIEMPO_SERVER(_out);
	}

	private void basicos_Mensaje_Informacion(String packet) {
		Personagens perso = World.getPjPorNombre(packet = packet.substring(2));
		if (perso == null || !perso.enLinea()) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
		SocketManager.ENVIAR_BWK_QUIEN_ES(_perso,
				String.valueOf(perso.getCuenta().getApodo()) + "|1|" + perso.getNombre() + "|-1");
	}

	private void analizar_Juego(String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			if (_perso == null) {
				return;
			}
			juego_Acciones(packet);
			break;
		}
		case 'C': {
			if (_perso == null) {
				return;
			}
			_perso.crearJuegoPJ();
			break;
		}
		case 'f': {
			juego_Mostrar_Celda(packet);
			break;
		}
		case 'F': {
			_perso.volverseFantasma();
			break;
		}
		case 'I': {
			if (_perso == null) {
				return;
			}
			juego_Extra_Informacion();
			break;
		}
		case 'K': {
			juego_Finalizar_Accion(packet);
			break;
		}
		case 'P': {
			_perso.botonActDesacAlas(packet.charAt(2));
			break;
		}
		case 'p': {
			juego_Cambio_Posicion(packet);
			break;
		}
		case 'Q': {
			juego_Retirar_Pelea(packet);
			break;
		}
		case 'R': {
			juego_Listo(packet);
			break;
		}
		case 't': {
			Fight pelea = _perso.getPelea();
			if (pelea == null) {
				return;
			}
			pelea.pasarTurno(_perso);
		}
		}
	}

	private void casa_Accion(String packet) {
		int accionID = Integer.parseInt(packet.substring(5));
		Casa casa = _perso.getCasa();
		if (casa == null) {
			return;
		}
		switch (accionID) {
		case 81: {
			casa.bloquear(_perso);
			break;
		}
		case 97: {
			casa.comprarEstaCasa(_perso);
			break;
		}
		case 98:
		case 108: {
			casa.venderla(_perso);
		}
		}
	}

	private void juego_Retirar_Pelea(String packet) {
		Fight pelea;
		int objetivoID = -1;
		if (!packet.substring(2).isEmpty()) {
			try {
				objetivoID = Integer.parseInt(packet.substring(2));
			} catch (Exception exception) {
				// empty catch block
			}
		}
		if ((pelea = _perso.getPelea()) == null) {
			return;
		}
		if (objetivoID > 0) {
			Personagens expulsado = World.getPersonaje(objetivoID);
			if (expulsado == null || expulsado.getPelea() == null
					|| expulsado.getPelea().getParamEquipo(expulsado.getID()) != pelea.getParamEquipo(_perso.getID())) {
				return;
			}
			pelea.retirarsePelea(_perso, expulsado);
		} else {
			pelea.retirarsePelea(_perso, null);
		}
	}

	private void juego_Mostrar_Celda(String packet) {
		if (_perso == null || _perso.getPelea() == null || _perso.getPelea().getEstado() != 3) {
			return;
		}
		int celdaID = -1;
		try {
			celdaID = Integer.parseInt(packet.substring(2));
		} catch (Exception exception) {
			// empty catch block
		}
		if (celdaID == -1) {
			return;
		}
		SocketManager.ENVIAR_Gf_MOSTRAR_CASILLA_EN_PELEA(_perso.getPelea(), 7, _perso.getID(), celdaID);
	}

	private void juego_Listo(String packet) {
		Fight pelea = _perso.getPelea();
		if (pelea == null || pelea.getEstado() != 2) {
			return;
		}
		_perso.setListo(packet.substring(2).equalsIgnoreCase("1"));
		pelea.verificaTodosListos();
		SocketManager.ENVIAR_GR_TODOS_LUCHADORES_LISTOS(pelea, 3, _perso.getID(),
				packet.substring(2).equalsIgnoreCase("1"));
	}

	private void juego_Cambio_Posicion(String packet) {
		if (_perso.getPelea() == null) {
			return;
		}
		try {
			short celda = Short.parseShort(packet.substring(2));
			_perso.getPelea().cambiarLugar(_perso, celda);
		} catch (NumberFormatException e) {
			return;
		}
	}

	private void juego_Finalizar_Accion(String packet) {
		int idUnica = -1;
		String[] infos = packet.substring(3).split("\\|");
		try {
			idUnica = Integer.parseInt(infos[0]);
		} catch (Exception e) {
			return;
		}
		if (idUnica == -1) {
			return;
		}
		AccionDeJuego AJ = _acciones.get(idUnica);
		if (AJ == null) {
			return;
		}
		boolean esOk = packet.charAt(2) == 'K';
		switch (AJ._accionID) {
		case 1: {
			if (esOk) {
				if (_perso.getPelea() == null) {
					String path = AJ._args;
					Maps mapa = _perso.getMapa();
					Maps.Celda sigCelda = mapa.getCelda(CryptManager.celdaCodigoAID(path.substring(path.length() - 2)));
					if (sigCelda == null) {
						SocketManager.ENVIAR_BN_NADA(_out);
						return;
					}
					Maps.Celda celdaObjetivo = mapa
							.getCelda(CryptManager.celdaCodigoAID(AJ._packet.substring(AJ._packet.length() - 2)));
					_perso.setCelda(sigCelda);
					_perso.setOrientacion((byte) CryptManager.getNumeroPorValorHash(path.charAt(path.length() - 3)));
					Maps.ObjetoInteractivo objeto = null;
					if (celdaObjetivo != null) {
						objeto = celdaObjetivo.getObjetoInterac();
					}
					if (_perso.estaOcupado()) {
						_perso.setOcupado(false);
					}
					if (objeto != null) {
						if (objeto.getID() == 1324) {
							Constantes.aplicarAccionOI(_perso, mapa.getID(), celdaObjetivo.getID());
						} else if (objeto.getID() == 542 && (_perso.esFantasma() || _perso.getGfxID() == 8004)) {
							_perso.revivir();
						}
					}
					mapa.jugadorLLegaACelda(_perso, _perso.getCelda().getID());
					break;
				}
				_perso.getPelea().finalizarMovimiento(_perso);
				return;
			}
			short nuevaCeldaID = -1;
			try {
				nuevaCeldaID = Short.parseShort(infos[1]);
			} catch (Exception e) {
				return;
			}
			if (nuevaCeldaID == -1) {
				return;
			}
			Maps.Celda celda = _perso.getMapa().getCelda(nuevaCeldaID);
			String path = AJ._args;
			_perso.setCelda(celda);
			_perso.setOrientacion((byte) CryptManager.getNumeroPorValorHash(path.charAt(path.length() - 3)));
			SocketManager.ENVIAR_BN_NADA(_out);
			break;
		}
		case 500: {
			_perso.finalizarAccionEnCelda(AJ);
			break;
		}
		default: {
			System.out.println("No se ha establecido el  de la accion ID: " + AJ._accionID);
		}
		}
		borrarGA(AJ);
	}

	private void juego_Extra_Informacion() {
		if (_perso.getPelea() != null) {
			if (_perso.getPelea().getEstado() < 4) {
				SocketManager.ENVIAR_GDK_CARGAR_MAPA(_out);
				try {
					Thread.sleep(500L);
				} catch (Exception exception) {
					// empty catch block
				}
				if (_perso.getReconectado()) {
					_perso.getPelea().reconectarLuchador(_perso);
					_perso.setReconectado(false);
				}
				return;
			}
			if (_perso.getReconectado()) {
				_perso.setReconectado(false);
			}
		}
		if (_perso.getDefendiendo()) {
			_perso.setDefendiendo(false);
			return;
		}
		Maps mapa = _perso.getMapa();
		if (_perso.getMapaDefPerco() != null) {
			_perso.setMapa(_perso.getMapaDefPerco());
			SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(mapa, _perso.getID());
			_perso.setCelda(_perso.getCeldaDefPerco());
			_perso.setMapaDefPerco(null);
			_perso.setCeldaDefPerco(null);
		}
		SocketManager.ENVIAR_Rp_INFORMACION_CERCADO(_perso, mapa.getCercado());
		SocketManager.ENVIAR_GM_PERSONAJE_A_MAPA(mapa, _perso);
		SocketManager.ENVIAR_GM_GRUPOMOBS(_out, mapa);
		SocketManager.ENVIAR_GM_NPCS(_out, mapa);
		SocketManager.ENVIAR_GM_RECAUDADORES(_out, mapa);
		SocketManager.ENVIAR_GDF_OBJETOS_INTERACTIVOS(_out, mapa);
		SocketManager.ENVIAR_GDK_CARGAR_MAPA(_out);
		SocketManager.ENVIAR_fC_CANTIDAD_DE_PELEAS(_out, mapa);
		SocketManager.ENVIAR_GM_MERCANTES(_out, mapa);
		SocketManager.ENVIAR_GM_PRISMAS(_out, mapa);
		SocketManager.ENVIAR_GM_MONTURAS(_out, mapa);
		SocketManager.ENVIAR_GDO_OBJETOS_CRIAS_EN_MAPA(_out, mapa);
		SocketManager.ENVIAR_ILS_TIEMPO_REGENERAR_VIDA(_out, 1000);
		Casa.cargarCasa(_perso, mapa.getID());
		Fight.agregarEspadaDePelea(mapa, _perso);
		mapa.objetosTirados(_perso);
		if (mapa.esTaller() && _perso.getOficioPublico()) {
			SocketManager.ENVIAR_EW_OFICIO_MODO_INVITACION(_out, "+", _perso.getID(),
					_perso.getStringOficiosPublicos());
		}
	}

	private void juego_Acciones(String packet) {
		int accionID;
		try {
			accionID = Integer.parseInt(packet.substring(2, 5));
		} catch (NumberFormatException e) {
			return;
		}
		int sigAccionJuegoID = 0;
		if (_acciones.size() > 0) {
			sigAccionJuegoID = (Integer) _acciones.keySet().toArray()[_acciones.size() - 1] + 1;
		}
		AccionDeJuego AJ = new AccionDeJuego(sigAccionJuegoID, accionID, packet);
		switch (accionID) {
		case 1: {
			juego_Desplazamiento(AJ);
			break;
		}
		case 300: {
			juego_Lanzar_Hechizo(packet);
			break;
		}
		case 303: {
			juego_Ataque_CAC(packet);
			break;
		}
		case 500: {
			juego_Accion(AJ);
			_perso.setTaller(AJ);
			break;
		}
		case 512: {
			_perso.abrirMenuPrisma();
			break;
		}
		case 507: {
			casa_Accion(packet);
			break;
		}
		case 618: {
			_perso.setEsOK(Integer.parseInt(packet.substring(5, 6)));
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_MAPA(_perso.getMapa(), "", _perso.getID(), _perso.getNombre(), "Oui");
			if (World.getCasado(0).getEsOK() <= 0 || World.getCasado(1).getEsOK() <= 0)
				break;
			World.casando(World.getCasado(0), World.getCasado(1), 1);
			break;
		}
		case 619: {
			_perso.setEsOK(0);
			SocketManager.ENVIAR_cMK_CHAT_MENSAJE_MAPA(_perso.getMapa(), "", _perso.getID(), _perso.getNombre(),
					"Non, d\u00e9sol\u00e9");
			World.casando(World.getCasado(0), World.getCasado(1), 0);
			break;
		}
		case 900: {
			if (_perso.esFantasma()) {
				return;
			}
			juego_Desafiar(packet);
			break;
		}
		case 901: {
			juego_Aceptar_Desafio(packet);
			break;
		}
		case 902: {
			juego_Cancelar_Desafio(packet);
			break;
		}
		case 903: {
			juego_Unirse_Pelea(packet);
			break;
		}
		case 906: {
			juego_Agresion(packet);
			break;
		}
		case 909: {
			juego_Ataque_Recaudador(packet);
			break;
		}
		case 919: {
			break;
		}
		case 912: {
			juego_Ataque_Prisma(packet);
		}
		}
	}

	private void juego_Ataque_Recaudador(String packet) {
		try {
			if (_perso.esFantasma() || _perso.getPelea() != null || _perso.estaOcupado()
					|| !_perso.getMapa().aptoParaPelea()) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			int id = Integer.parseInt(packet.substring(5));
			Coletor recaudador = World.getRecaudador(id);
			if (recaudador == null || recaudador.getEstadoPelea() > 0) {
				return;
			}
			if (recaudador.getEnRecolecta()) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "1180");
				return;
			}
			SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(_perso.getMapa(), "", 909, String.valueOf(_perso.getID()),
					String.valueOf(id));
			_perso.getMapa().iniciarPeleaVSRecaudador(_perso, recaudador);
		} catch (Exception exception) {
			// empty catch block
		}
	}

	private void juego_Ataque_Prisma(String packet) {
		try {
			if (_perso.esFantasma() || _perso.getPelea() != null || _perso.estaOcupado() || _perso.getAlineacion() == 0
					|| !_perso.getMapa().aptoParaPelea()) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			int id = Integer.parseInt(packet.substring(5));
			Prisma prisma = World.getPrisma(id);
			if (prisma.getEstadoPelea() == 0 || prisma.getEstadoPelea() == -2) {
				return;
			}
			SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(_perso.getMapa(), "", 909, String.valueOf(_perso.getID()),
					String.valueOf(id));
			_perso.getMapa().iniciarPeleaVSPrisma(_perso, prisma);
		} catch (Exception exception) {
			// empty catch block
		}
	}

	private void juego_Agresion(String packet) {
		try {
			if (_perso.esFantasma() || _perso.getPelea() != null || _perso.estaOcupado()
					|| !_perso.getMapa().aptoParaPelea()) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			int id = Integer.parseInt(packet.substring(5));
			Personagens agredido = World.getPersonaje(id);
			Maps mapa = _perso.getMapa();
			if (agredido == null || !agredido.enLinea() || agredido.esFantasma() || agredido.getPelea() != null
					|| agredido.estaOcupado() || agredido.getMapa().getID() != mapa.getID()
					|| agredido.getAlineacion() == _perso.getAlineacion()) {
				SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out,
						"No se le puede agredir al objetivo, por diferentes causas.");
				return;
			}
			if (agredido.getAlineacion() == 0) {
				_perso.setDeshonor(_perso.getDeshonor() + 1);
				SocketManager.ENVIAR_Im_INFORMACION(_out, "084;1");
			}
			_perso.botonActDesacAlas('+');
			if (agredido.getAgresion() || _perso.getAgresion()) {
				SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_out, "El objetivo se encuentra en una agresi\u00f3n.");
				return;
			}
			agredido.setAgresion(true);
			_perso.setAgresion(true);
			SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(mapa, "", 906, String.valueOf(_perso.getID()),
					String.valueOf(id));
			mapa.iniciarPeleaPjVSPj(_perso, agredido, (byte) 1);
			agredido.setAgresion(false);
			_perso.setAgresion(false);
		} catch (Exception exception) {
			// empty catch block
		}
	}

	public void juego_Accion(AccionDeJuego AJ) {
		String packet = AJ._packet.substring(5);
		short celdaID = -1;
		int accionID = -1;
		try {
			celdaID = Short.parseShort(packet.split(";")[0]);
			accionID = Integer.parseInt(packet.split(";")[1]);
		} catch (Exception exception) {
			// empty catch block
		}
		if (celdaID == -1 || accionID == -1 || _perso == null || _perso.getMapa() == null
				|| _perso.getMapa().getCelda(celdaID) == null) {
			return;
		}
		AJ._args = String.valueOf(celdaID) + ";" + accionID;
		addGA(AJ);
		_perso.iniciarAccionEnCelda(AJ);
	}

	private void juego_Ataque_CAC(String packet) {
		try {
			if (_perso.getPelea() == null) {
				return;
			}
			short celdaID = -1;
			try {
				celdaID = Short.parseShort(packet.substring(5));
			} catch (Exception e) {
				return;
			}
			_perso.getPelea().intentarCaC(_perso, celdaID);
		} catch (Exception exception) {
			// empty catch block
		}
	}

	private void juego_Lanzar_Hechizo(String packet) {
		try {
			String[] splt = packet.split(";");
			int hechizoID = Integer.parseInt(splt[0].substring(5));
			short celdaID = Short.parseShort(splt[1]);
			Fight pelea = _perso.getPelea();
			if (pelea != null) {
				Spell.StatsHechizos SH = _perso.getStatsHechizo(hechizoID);
				if (SH == null) {
					return;
				}
				pelea.intentarLanzarHechizo(pelea.getLuchadorPorPJ(_perso), SH, celdaID);
			}
		} catch (NumberFormatException e) {
			return;
		}
	}

	private void juego_Unirse_Pelea(String packet) {
		String[] infos = packet.substring(5).split(";");
		if (infos.length == 1) {
			try {
				if (_perso.getPelea() != null) {
					SocketManager.ENVIAR_GA903_ERROR_PELEA(_out, 'o');
					return;
				}
				Fight pelea = _perso.getMapa().getPelea(Short.parseShort(infos[0]));
				pelea.unirseEspectador(_perso);
			} catch (Exception e) {
				return;
			}
		}
		try {
			if (_perso.getPelea() != null) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(_out, 'o');
				return;
			}
			int id = Integer.parseInt(infos[1]);
			if (_perso.estaOcupado()) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(_out, 'o');
				return;
			}
			if (_perso.esFantasma()) {
				SocketManager.ENVIAR_GA903_ERROR_PELEA(_out, 'd');
				return;
			}
			if (id < -100) {
				int resta = (id + 100) % 3;
				if (resta == -2) {
					Prisma prisma = World.getPrisma(id);
					if (prisma == null) {
						SocketManager.ENVIAR_BN_NADA(_out);
						return;
					}
					short mapaID = prisma.getMapaID();
					short celdaID = prisma.getCeldaID();
					if (prisma.getAlineacion() != _perso.getAlineacion()) {
						return;
					}
					if (prisma.getPelea().unirsePeleaPrisma(_perso, id, mapaID, celdaID)) {
						for (Personagens z : World.getPJsEnLinea()) {
							if (z == null || z.getAlineacion() != _perso.getAlineacion())
								continue;
							Prisma.analizarDefensa(z);
						}
					}
				} else if (resta == 0) {
					Coletor recau = World.getRecaudador(id);
					if (recau == null) {
						SocketManager.ENVIAR_BN_NADA(_out);
						return;
					}
					short mapaID = recau.getMapaID();
					short celdaID = recau.getCeldalID();
					if (_perso.getPelea() != null) {
						return;
					}
					if (recau.getPelea().unirsePeleaRecaudador(_perso, id, mapaID, celdaID)) {
						for (Personagens miembros : _perso.getGremio().getPjMiembros()) {
							if (miembros == null || !miembros.enLinea())
								continue;
							Coletor.analizarDefensa(miembros, _perso.getGremio().getID());
						}
					}
				}
			} else {
				World.getPersonaje(id).getPelea().unirsePelea(_perso, id);
			}
		} catch (Exception e) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
	}

	private void juego_Aceptar_Desafio(String packet) {
		int id = -1;
		try {
			id = Integer.parseInt(packet.substring(5));
		} catch (NumberFormatException e) {
			return;
		}
		int idDuelo = _perso.getDueloID();
		if (idDuelo != id || idDuelo == -1) {
			return;
		}
		Maps mapa = _perso.getMapa();
		if (!mapa.aptoParaPelea()) {
			SocketManager.ENVIAR_BN_NADA(_out);
			return;
		}
		SocketManager.ENVIAR_GA901_ACEPTAR_DESAFIO(mapa, idDuelo, _perso.getID());
		Fight pelea = mapa.iniciarPeleaPjVSPj(World.getPersonaje(idDuelo), _perso, (byte) 0);
		_perso.setPelea(pelea);
		World.getPersonaje(idDuelo).setPelea(pelea);
	}

	private void juego_Cancelar_Desafio(String packet) {
		if (_perso.getDueloID() == -1) {
			return;
		}
		SocketManager.ENVIAR_GA902_RECHAZAR_DESAFIO(_perso.getMapa(), _perso.getDueloID(), _perso.getID());
		try {
			Personagens desafiador = World.getPersonaje(_perso.getDueloID());
			desafiador.setOcupado(false);
			desafiador.setDueloID(-1);
		} catch (NullPointerException nullPointerException) {
			// empty catch block
		}
		_perso.setOcupado(false);
		_perso.setDueloID(-1);
	}

	private void juego_Desafiar(String packet) {
		Maps mapa = _perso.getMapa();
		int idPerso = _perso.getID();
		if (!mapa.aptoParaPelea()) {
			SocketManager.ENVIAR_GA903_ERROR_PELEA(_out, 'p');
			return;
		}
		try {
			int id = Integer.parseInt(packet.substring(5));
			if (_perso.estaOcupado() || _perso.getPelea() != null) {
				SocketManager.ENVIAR_GA903_UNIRSE_PELEA_Y_ESTAR_OCUPADO(_out, idPerso);
				return;
			}
			Personagens desafiado = World.getPersonaje(id);
			if (desafiado == null) {
				return;
			}
			if (desafiado.estaOcupado() || desafiado.getPelea() != null
					|| desafiado.getMapa().getID() != mapa.getID()) {
				SocketManager.ENVIAR_GA903_UNIRSE_PELEA_Y_OPONENTE_OCUPADO(_out, idPerso);
				return;
			}
			_perso.setDueloID(id);
			_perso.setOcupado(true);
			desafiado.setDueloID(_perso.getID());
			desafiado.setOcupado(true);
			SocketManager.ENVIAR_GA900_DESAFIAR(mapa, idPerso, id);
		} catch (NumberFormatException e) {
			return;
		}
	}

	private void juego_Desplazamiento(AccionDeJuego AJ) {
		String path = AJ._packet.substring(5);
		Fight pelea = _perso.getPelea();
		if (pelea == null) {
			if (_perso.esTumba()) {
				SocketManager.ENVIAR_BN_NADA(_out);
				return;
			}
			if (_perso.getPodUsados() > _perso.getMaxPod()) {
				SocketManager.ENVIAR_Im_INFORMACION(_out, "112");
				_perso.setOcupado(false);
				SocketManager.ENVIAR_GA_ACCION_DE_JUEGO(_out, "", "0", "", "");
				borrarGA(AJ);
				return;
			}
			AtomicReference<String> pathRef = new AtomicReference<String>(path);
			short celdaID = _perso.getCelda().getID();
			Maps mapa = _perso.getMapa();
			short resultado = Pathfinding.numeroMovimientos(mapa, celdaID, pathRef, null);
			if (resultado == 0) {
				SocketManager.ENVIAR_GA_ACCION_DE_JUEGO(_out, "", "0", "", "");
				borrarGA(AJ);
				return;
			}
			if (resultado != -1000 && resultado < 0) {
				resultado = (short) -resultado;
			}
			path = pathRef.get();
			if (resultado == -1000) {
				path = String.valueOf(CryptManager.getValorHashPorNumero(_perso.getOrientacion()))
						+ CryptManager.celdaIDACodigo(celdaID);
			}
			AJ._args = path;
			SocketManager.ENVIAR_GA_ACCION_JUEGO_AL_MAPA(mapa, String.valueOf(AJ._idUnica), 1,
					String.valueOf(_perso.getID()), "a" + CryptManager.celdaIDACodigo(celdaID) + path);
			addGA(AJ);
			if (_perso.estaSentado()) {
				_perso.setSentado(false);
			}
			_perso.setOcupado(true);
		} else {
			Fight.Luchador luchador = pelea.getLuchadorPorPJ(_perso);
			if (luchador == null) {
				return;
			}
			AJ._args = path;
			pelea.puedeMoverseLuchador(luchador, AJ);
		}
	}

	private void analizar_Cuenta(String packet) {
		switch (packet.charAt(1)) {
		case 'A': {
			cuenta_Crear_Personaje(packet);
			break;
		}
		case 'B': {
			if (LesGuardians.CONFIG_ATIVAR_STATS_2) {
				int stat = -1;
				try {
					stat = Integer.parseInt(packet.substring(2).split("/u000A")[0]);
					_perso.set_savestat(stat);
					SocketManager.ENVIAR_K_CLAVE(_perso, "CK0|5");
					break;
				} catch (Exception e) {
					return;
				}
			}
			int stat = -1;
			try {
				stat = Integer.parseInt(packet.substring(2).split("/u000A")[0]);
				_perso.boostStat(stat);
				break;
			} catch (NumberFormatException e) {
				return;
			}
		}
		case 'D': {
			cuenta_Eliminar_Personaje(packet);
			break;
		}
		case 'f': {
			int colaID = 1;
			int posicion = 1;
			SocketManager.ENVIAR_Af_ABONADOS_POSCOLA(_out, posicion, 1, 1, "1", colaID);
			break;
		}
		case 'g': {
			int regalo = _cuenta.getRegalo();
			if (regalo == 0)
				break;
			String idModObjeto = Integer.toString(regalo, 16);
			String efectos = World.getObjModelo(regalo).getStringStatsObj();
			SocketManager.ENVIAR_Ag_LISTA_REGALOS(_out, regalo, "1~" + idModObjeto + "~1~~" + efectos);
			break;
		}
		case 'G': {
			cuenta_Entregar_Regalo(packet.substring(2));
		}
		case 'i': {
			_cuenta.setClaveCliente(packet.substring(2));
			break;
		}
		case 'L': {
			SocketManager.ENVIAR_ALK_LISTA_DE_PERSONAJES(_out, _cuenta);
			break;
		}
		case 'R': {
			try {
				_cuenta.getPersonajes().get(Integer.parseInt(packet.substring(2))).reiniciarCero();
			} catch (Exception exception) {
				// empty catch block
			}
			SocketManager.ENVIAR_ALK_LISTA_DE_PERSONAJES(_out, _cuenta);
			break;
		}
		case 'S': {
			int idPerso = Integer.parseInt(packet.substring(2));
			if (_cuenta.getPersonajes().get(idPerso) != null) {
				_cuenta.setEntradaPersonaje(this);
				_perso = _cuenta.getPersonajes().get(idPerso);
				if (_perso != null) {
					_perso.setCuenta(_cuenta);
					_perso.Conectarse();
					break;
				}
				System.out.println("El personaje de ID " + idPerso + " es nulo");
				break;
			}
			SocketManager.ENVIAR_ASE_SELECCION_PERSONAJE_FALLIDA(_out);
			break;
		}
		case 'T': {
			try {
				int id = Integer.parseInt(packet.substring(2));
				_cuenta = LesGuardians._servidorPersonaje.getEsperandoCuenta(id);
				if (_cuenta != null) {
					String ip = _socket.getInetAddress().getHostAddress();
					_cuenta.setEntradaPersonaje(this);
					_cuenta.setTempIP(ip);
					LesGuardians._servidorPersonaje.delEsperandoCuenta(_cuenta);
					SocketManager.ENVIAR_AT_TICKET_A_CUENTA(_out);
					break;
				}
				SocketManager.ENVIAR_AT_TICKET_FALLIDA(_out);
			} catch (Exception exception) {
			}
			break;
		}
		case 'V': {
			SocketManager.ENVIAR_AV_VERSION_REGIONAL(_out);
			break;
		}
		case 'P': {
			SocketManager.REALM_SEND_REQUIRED_APK(_out);
		}
		}
	}

	private void cuenta_Entregar_Regalo(String packet) {
		String[] info = packet.split("\\|");
		int idObjeto = Integer.parseInt(info[0]);
		int idPj = Integer.parseInt(info[1]);
		Personagens pj = null;
		Objeto objeto = null;
		try {
			pj = World.getPersonaje(idPj);
			objeto = World.getObjModelo(idObjeto).crearObjDesdeModelo(1, true);
		} catch (Exception exception) {
			// empty catch block
		}
		if (pj == null || objeto == null) {
			return;
		}
		if (!pj.addObjetoSimilar(objeto, true, -1)) {
			World.addObjeto(objeto, true);
			pj.addObjetoPut(objeto);
			SocketManager.ENVIAR_OAKO_APARECER_OBJETO(pj, objeto);
		}
		_cuenta.setRegalo();
		SocketManager.ENVIAR_AG_SIGUIENTE_REGALO(_out);
		SQLManager.ACTUALIZAR_REGALO(_cuenta);
	}

	private void cuenta_Eliminar_Personaje(String packet) {
	    String[] split = packet.substring(2).split("\\|");
	    int id = Integer.parseInt(split[0]);
	    String respuesta = (split.length > 1) ? split[1] : "";
	    if (this._cuenta.getPersonajes().containsKey(Integer.valueOf(id))) {
	      if (((Personagens)this._cuenta.getPersonajes().get(Integer.valueOf(id))).getNivel() < 25 || ((
	        (Personagens)this._cuenta.getPersonajes().get(Integer.valueOf(id))).getNivel() >= 25 && respuesta.equals(this._cuenta.getRespuesta()))) {
	        this._cuenta.borrarPerso(id);
	        SocketManager.ENVIAR_ALK_LISTA_DE_PERSONAJES(this._out, this._cuenta);
	      } else {
	        SocketManager.ENVIAR_ADE_ERROR_BORRAR_PJ(this._out);
	      } 
	    } else {
	      SocketManager.ENVIAR_ADE_ERROR_BORRAR_PJ(this._out);
	    } 
	  }

	private void cuenta_Crear_Personaje(String packet) {
		String[] infos = packet.substring(2).split("\\|");
		if (World.getPjPorNombre(infos[0]) != null) {
			SocketManager.ENVIAR_AAEa_NOMBRE_YA_EXISTENTE(_out);
			return;
		}
		boolean esValido = true;
		String nombre = infos[0].toLowerCase();
		if (nombre.length() > 20) {
			esValido = false;
		}
		if (esValido) {
			int cantSimbol = 0;
			char letra_A = ' ';
			char letra_B = ' ';
			for (char letra : nombre.toCharArray()) {
				if ((letra < 'a' || letra > 'z') && letra != '-') {
					esValido = false;
					break;
				}
				if (letra == letra_A && letra == letra_B) {
					esValido = false;
					break;
				}
				if (letra >= 'a' && letra <= 'z') {
					letra_A = letra_B;
					letra_B = letra;
				}
				if (letra != '-')
					continue;
				if (cantSimbol >= 1) {
					esValido = false;
					break;
				}
				++cantSimbol;
			}
		}
		if (!esValido) {
			SocketManager.ENVIAR_AAEa_NOMBRE_YA_EXISTENTE(_out);
			return;
		}
		if (_cuenta.getPersonajes().size() >= LesGuardians.MAX_CHAR_POR_CONTA) {
			SocketManager.ENVIAR_AAEf_MAXIMO_PJS_CREADOS(_out);
			return;
		}
		if (_cuenta.crearPj(infos[0], Integer.parseInt(infos[2]), Integer.parseInt(infos[1]),
				Integer.parseInt(infos[3]), Integer.parseInt(infos[4]), Integer.parseInt(infos[5]))) {
			SocketManager.ENVIAR_AAK_CREACION_PJ(_out);
			SocketManager.ENVIAR_ALK_LISTA_DE_PERSONAJES(_out, _cuenta);
		} else {
			SocketManager.ENVIAR_AAEF_ERROR_CREAR_PJ(_out);
		}
	}

	public PrintWriter getOut() {
		return _out;
	}

	public void salir() {
		try {
			LesGuardians._servidorPersonaje.delGestorCliente(this);
			if (_cuenta != null) {
				_cuenta.desconexion();
			}
			if (!_socket.isClosed()) {
				_socket.close();
			}
			_in.close();
			_out.close();
			_thread.interrupt();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void cerrarSocket() {
		try {
			_socket.close();
		} catch (IOException iOException) {
			// empty catch block
		}
	}

	public Thread getThread() {
		return _thread;
	}

	public Personagens getPersonaje() {
		return _perso;
	}

	public Conta getCuenta() {
		return _cuenta;
	}

	public void borrarGA(AccionDeJuego AJ) {
		_acciones.remove(AJ._idUnica);
	}

	public void addGA(AccionDeJuego AJ) {
		_acciones.put(AJ._idUnica, AJ);
	}

	public static class AccionDeJuego {
		public int _idUnica;
		public int _accionID;
		public String _packet;
		public String _args;

		public AccionDeJuego(int id, int accionId, String packet) {
			_idUnica = id;
			_accionID = accionId;
			_packet = packet;
		}
	}
}