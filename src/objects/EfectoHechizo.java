package objects;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import common.Constantes;
import common.CryptManager;
import common.Fórmulas;
import common.Pathfinding;
import common.SocketManager;
import common.Mundo;

import objects.Combate;
import objects.Combate.Luchador;
import objects.Combate.Trampa;
import objects.MobModelo;
import objects.Mapa;
import objects.Mapa.Celda;
import objects.Personaje;
import objects.Personaje.Stats;
import objects.Hechizo;

public class EfectoHechizo {
	private int _efectoID;
	private int _turnos = 0;
	private String _valores = "0d0+0";
	private byte _suerte = 100;
	private String _args;
	private int _valor = 0;
	private Luchador _lanzador = null;
	private int _hechizoID = 0;
	private int _nivelHechizo = 1;
	private boolean _desbuffeable = true;
	private int _duracion = 0;
	private int _turnosOriginales = 0;
	private Celda _celdaObj = null;
	private boolean _veneno = false;
	private static int[] efectosReto = new int[] { 77, 169, 84, 168, 108, 116, 320, 81, 82, 85, 86, 87, 88, 89, 91, 92,
			93, 94, 95, 96, 97, 98, 99, 100, 101 };

	public EfectoHechizo(int aID, String aArgs, int aHechizo, int aNivelHechizo) {
		_efectoID = aID;
		_args = aArgs;
		_hechizoID = aHechizo;
		_nivelHechizo = aNivelHechizo;
		try {
			String[] args = _args.split(";");
			_valor = Integer.parseInt(args[0]);
			_turnos = Integer.parseInt(args[3]);
			_turnosOriginales = Integer.parseInt(args[3]);
			_suerte = Byte.parseByte(args[4]);
			_valores = args[5];
		} catch (Exception exception) {
		}
	}

	public EfectoHechizo(int id, int aValor, int aDuracion, int aTurnos, boolean debuffeable, Luchador aLanzador,
			String args2, int aHechizoID, boolean veneno) {
		_efectoID = id;
		_valor = aValor;
		_turnos = aTurnos;
		_desbuffeable = debuffeable;
		_lanzador = aLanzador;
		_duracion = aDuracion;
		_args = args2;
		_hechizoID = aHechizoID;
		_veneno = veneno;
		try {
			String[] args = _args.split(";");
			_turnosOriginales = Integer.parseInt(args[3]);
			_valores = args[5];
		} catch (Exception exception) {
		}
	}

	private static boolean esEfectoReto(int efecto) {
		int[] arrn = efectosReto;
		int n = efectosReto.length;
		for (int i = 0; i < n; ++i) {
			Integer e = arrn[i];
			if (e != efecto)
				continue;
			return true;
		}
		return false;
	}

	public boolean esMismoHechizo(int id) {
		return _hechizoID == id;
	}

	public int getDuracion() {
		return _duracion;
	}

	public int getTurnos() {
		return _turnos;
	}

	public boolean esDesbufeable() {
		return _desbuffeable;
	}

	public void setTurnos(int aturnos) {
		_turnos = aturnos;
	}

	public int getEfectoID() {
		return _efectoID;
	}

	public int getValor() {
		return _valor;
	}

	public String getValores() {
		return _valores;
	}

	public boolean getVenenoso() {
		return _veneno;
	}

	public int getSuerte() {
		return _suerte;
	}

	public String getArgs() {
		return _args;
	}

	public void setArgs(String nuevasArgs) {
		_args = nuevasArgs;
		try {
			String[] args = _args.split(";");
			_valor = Integer.parseInt(args[0]);
			_turnos = Integer.parseInt(args[3]);
			_suerte = Byte.parseByte(args[4]);
			_valores = args[5];
		} catch (Exception exception) {
			// empty catch block
		}
	}

	public void setEfectoID(int id) {
		_efectoID = id;
	}

	public void setValor(short v) {
		_valor = v;
	}

	public int disminuirDuracion() {
		--_duracion;
		return _duracion;
	}

	public void aplicarBuffDeInicioTurno(Combate pelea, Luchador afectado) {
		ArrayList<Luchador> objetivos = new ArrayList<Luchador>();
		objetivos.add(afectado);
		_turnos = -1;
		aplicarAPelea(pelea, _lanzador, objetivos, false, null);
	}

	public void aplicarHechizoAPelea(Combate pelea, Luchador lanzador, Celda casilla, ArrayList<Luchador> objetivos,
			ArrayList<Celda> celdas) {
		_celdaObj = casilla;
		aplicarAPelea(pelea, lanzador, objetivos, false, celdas);
	}

	public Luchador getLanzador() {
		return _lanzador;
	}

	public int getHechizoID() {
		return _hechizoID;
	}

	public int getMaxMinHechizo(Luchador objetivo, int valor) {
		int val = valor;
		if (objetivo.tieneBuff(782)) {
			int max = Integer.parseInt(_args.split(";")[1]);
			if (max == -1) {
				max = Integer.parseInt(_args.split(";")[0]);
			}
			valor = max;
		}
		if (objetivo.tieneBuff(781)) {
			valor = Integer.parseInt(_args.split(";")[0]);
		}
		return val;
	}

	public static ArrayList<Luchador> getAfectados(Combate pelea, ArrayList<Celda> celdas, int hechizo) {
		ArrayList<Luchador> objetivos = new ArrayList<Luchador>();
		if (hechizo == 418) {
			int i = 4;
			ArrayList<Celda> celdas1 = new ArrayList<Celda>();
			ArrayList<Celda> celdas2 = new ArrayList<Celda>();
			ArrayList<Celda> celdas3 = new ArrayList<Celda>();
			ArrayList<Celda> celdas4 = new ArrayList<Celda>();
			for (Celda celda : celdas) {
				if (i % 4 == 0) {
					celdas1.add(celda);
				} else if (i % 4 == 1) {
					celdas2.add(celda);
				} else if (i % 4 == 2) {
					celdas3.add(celda);
				} else {
					celdas4.add(celda);
				}
				++i;
			}
			celdas.clear();
			celdas.addAll(celdas4);
			celdas.addAll(celdas3);
			celdas.addAll(celdas2);
			celdas.addAll(celdas1);
		} else if (hechizo == 165) {
			ArrayList<Celda> celdas1 = new ArrayList<Celda>();
			for (int j = celdas.size() - 1; j >= 0; --j) {
				celdas1.add(celdas.get(j));
			}
			celdas.clear();
			celdas.addAll(celdas1);
		}
		for (Celda celda : celdas) {
			Luchador luch;
			if (celda == null || (luch = celda.getPrimerLuchador()) == null)
				continue;
			objetivos.add(luch);
		}
		return objetivos;
	}

	public static int aplicarBuffContraGolpe(int dañoFinal, Luchador objetivo, Luchador lanzador, Combate pelea,
			int hechizoID, boolean esVeneno) {
		for (int id : Constantes.BUFF_ACCION_RESPUESTA) {
			for (EfectoHechizo buff : objetivo.getBuffsPorEfectoID(id)) {
				if (objetivo.estaMuerto()) {
					return 0;
				}
				switch (id) {
				case 9: {
					Celda nueva;
					if (esVeneno) {
						continue;
					}
					int d = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), objetivo.getCeldaPelea().getID(),
							lanzador.getCeldaPelea().getID());
					if (d > 1) {
						continue;
					}
					int elusion = buff.getValor();
					int azar = Fórmulas.getRandomValor(1, 100);
					if (azar > elusion) {
						continue;
					}
					int nroCasillas = 0;
					try {
						nroCasillas = Integer.parseInt(buff.getArgs().split(";")[1]);
					} catch (Exception exception) {
					}
					if (nroCasillas == 0 || objetivo.tieneEstado(6))
						continue;
					Celda aCelda = lanzador.getCeldaPelea();
					Luchador afectado = null;
					Mapa mapaCopia = pelea.getMapaCopia();
					int nuevaCeldaID = Pathfinding.getCeldaDespEmpujon(mapaCopia, aCelda, objetivo.getCeldaPelea(),
							nroCasillas, pelea, objetivo);
					if (nuevaCeldaID == 0) {
						continue;
					}
					dañoFinal = 0;
					if (nuevaCeldaID < 0) {
						int a = -nuevaCeldaID;
						int coef = Fórmulas.getRandomValor("1d5+8");
						float b = lanzador.getNivel() / 100.0f;
						if (b < 0.1) {
							b = 0.1f;
						}
						float c = b * a;
						dañoFinal = (int) (coef * c);
						if (dañoFinal < 1) {
							dañoFinal = 1;
						}
						if (dañoFinal > objetivo.getPDVConBuff()) {
							dañoFinal = objetivo.getPDVConBuff();
						}
						if (dañoFinal > 0) {
							objetivo.restarPDV(dañoFinal);
							if (objetivo.getPDVConBuff() <= 0) {
								SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(lanzador.getID()),
										(objetivo.getID() + ",-" + da\u00f1oFinal));
								pelea.agregarAMuertos(objetivo);
								break;
							}
						}
						a = nroCasillas - a;
						nuevaCeldaID = Pathfinding.getCeldaDespEmpujon(mapaCopia, aCelda, objetivo.getCeldaPelea(), a,
								pelea, objetivo);
						char dir = Pathfinding.getDirEntreDosCeldas(aCelda.getID(), objetivo.getCeldaPelea().getID(),
								mapaCopia, true);
						short celdaSigID = 0;
						celdaSigID = nuevaCeldaID == 0
								? Pathfinding.getSigIDCeldaMismaDir(objetivo.getCeldaPelea().getID(), dir, mapaCopia,
										true)
								: Pathfinding.getSigIDCeldaMismaDir((short) nuevaCeldaID, dir, mapaCopia, true);
						Celda celdaSig = mapaCopia.getCelda(celdaSigID);
						if (celdaSig != null) {
							afectado = celdaSig.getPrimerLuchador();
						}
					}
					if (nuevaCeldaID != 0 && (nueva = mapaCopia.getCelda((short) nuevaCeldaID)) != null) {
						objetivo.getCeldaPelea().getLuchadores().clear();
						objetivo.setCeldaPelea(nueva);
						nueva.addLuchador(objetivo);
						SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 5, String.valueOf(lanzador.getID()),
								String.valueOf(objetivo.getID()) + "," + nuevaCeldaID);
						try {
							Thread.sleep(500L);
						} catch (Exception coef) {
						}
					}
					if (dañoFinal > 0) {
						SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(lanzador.getID()),
								String.valueOf(objetivo.getID()) + ",-" + dañoFinal);
					}
					if (afectado != null) {
						int dañoFinal2 = dañoFinal / 2;
						if (dañoFinal2 < 1) {
							dañoFinal2 = 1;
						}
						if (dañoFinal2 > afectado.getPDVConBuff()) {
							dañoFinal2 = afectado.getPDVConBuff();
						}
						if (dañoFinal2 > 0) {
							afectado.restarPDV(dañoFinal2);
							SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(lanzador.getID()),
									String.valueOf(afectado.getID()) + ",-" + dañoFinal2);
							if (afectado.getPDVConBuff() <= 0) {
								pelea.agregarAMuertos(afectado);
							}
						}
					}
					try {
						Thread.sleep(300L);
					} catch (Exception da\u00f1oFinal2) {
						// empty catch block
					}
					for (Combate.Trampa trampa : pelea.getTrampas()) {
						short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(),
								trampa.getCelda().getID(), (short) nuevaCeldaID);
						if (dist > trampa.getTama\u00f1o())
							continue;
						trampa.activaTrampa(objetivo);
						break;
					}
					return 0;
				}
				case 79: {
					if (esVeneno)
						continue;
					try {
						String[] infos = buff.getArgs().split(";");
						int coefDa\u00f1o = Integer.parseInt(infos[0]);
						int coefCura = Integer.parseInt(infos[1]);
						int suerte = Integer.parseInt(infos[2]);
						int jet = Fórmulas.getRandomValor(0, 99);
						if (jet < suerte) {
							if (-(da\u00f1oFinal = -(da\u00f1oFinal * coefCura)) <= objetivo.getPDVMaxConBuff()
									- objetivo.getPDVConBuff())
								continue;
							da\u00f1oFinal = -(objetivo.getPDVMaxConBuff() - objetivo.getPDVConBuff());
							break;
						}
						da\u00f1oFinal *= coefDa\u00f1o;
					} catch (Exception infos) {
					}
					continue;
				}
				case 107:
				case 220: {
					switch (hechizoID) {
					case 66:
					case 71:
					case 164:
					case 181:
					case 196:
					case 200:
					case 219: {
						break;
					}
					}
					if (esVeneno)
						continue;
					String[] args = buff.getArgs().split(";");
					float coef = 1 + objetivo.getTotalStatsConBuff().getEfecto(124) / 100;
					int reenvio = 0;
					try {
						reenvio = Integer.parseInt(args[1]) != -1 ? (int) (coef
								* (float) Fórmulas.getRandomValor(Integer.parseInt(args[0]), Integer.parseInt(args[1])))
								: (int) (coef * (float) Integer.parseInt(args[0]));
					} catch (Exception e) {
						break;
					}
					if (reenvio > da\u00f1oFinal) {
						reenvio = da\u00f1oFinal;
					}
					da\u00f1oFinal -= reenvio;
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 107, "-1",
							String.valueOf(objetivo.getID()) + "," + reenvio);
					if (reenvio > lanzador.getPDVConBuff()) {
						reenvio = lanzador.getPDVConBuff();
					}
					if (da\u00f1oFinal < 0) {
						da\u00f1oFinal = 0;
					}
					lanzador.restarPDV(reenvio);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(lanzador.getID()),
							String.valueOf(lanzador.getID()) + ",-" + reenvio);
					break;
				}
				case 776: {
					if (esVeneno || !objetivo.tieneBuff(776))
						continue;
					int pdvMax = objetivo.getPDVMax();
					float pda\u00f1o = (float) objetivo.getValorBuffPelea(776) / 100.0f;
					if ((pdvMax -= (int) ((float) da\u00f1oFinal * pda\u00f1o)) < 0) {
						pdvMax = 0;
					}
					objetivo.setPDVMAX(pdvMax);
					break;
				}
				case 788: {
					if (esVeneno)
						continue;
					int porc = lanzador.getPersonaje() == null ? 1 : 2;
					int gana = da\u00f1oFinal / porc;
					int stat = buff.getValor();
					int max = 0;
					try {
						max = Integer.parseInt(buff.getArgs().split(";")[1]);
					} catch (Exception exception) {
						// empty catch block
					}
					if ((max -= objetivo.getBonusCastigo(stat)) <= 0)
						continue;
					if (gana > max) {
						gana = max;
					}
					objetivo.setBonusCastigo(objetivo.getBonusCastigo(stat) + gana, stat);
					objetivo.addBuff(stat, gana, 5, 1, false, buff.getHechizoID(), buff.getArgs(), lanzador,
							buff.getVenenoso());
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, stat, String.valueOf(lanzador.getID()),
							String.valueOf(objetivo.getID()) + "," + gana + "," + 5);
					break;
				}
				default: {
					System.out.println("Efecto id " + id + " no definido como EFECTO DE CONTRAGOLPE.");
				}
				}
			}
		}
		return da\u00f1oFinal;
	}

	public void aplicarAPelea(Combate pelea, Luchador aLanzador, ArrayList<Luchador> objetivos, boolean esCaC,
			ArrayList<Celda> celdas) {
		Personaje perso;
		try {
			if (_turnos != -1) {
				_turnos = Integer.parseInt(_args.split(";")[3]);
			}
		} catch (NumberFormatException numberFormatException) {
			// empty catch block
		}
		_lanzador = aLanzador;
		try {
			_valores = _args.split(";")[5];
		} catch (Exception exception) {
			// empty catch block
		}
		if (_lanzador.getPersonaje() != null
				&& (perso = _lanzador.getPersonaje()).getHechizosSetClase().containsKey(_hechizoID)) {
			int modi = 0;
			if (_efectoID == 108) {
				modi = perso.getModifSetClase(_hechizoID, 284);
			} else if (_efectoID >= 91 && _efectoID <= 100) {
				modi = perso.getModifSetClase(_hechizoID, 283);
			}
			String jeta = _valores.split("\\+")[0];
			int bonus = Integer.parseInt(_valores.split("\\+")[1]) + modi;
			_valores = String.valueOf(jeta) + "+" + bonus;
		}
		pelea.setUltAfec((byte) objetivos.size());
		if (pelea.getTipoPelea() == 4 && _lanzador.getPersonaje() != null && EfectoHechizo.esEfectoReto(_efectoID)) {
			TreeMap<Integer, Integer> copiaRetos = new TreeMap<Integer, Integer>();
			copiaRetos.putAll(pelea.getRetos());
			for (Entry<Integer, Integer> entry : copiaRetos.entrySet()) {
				int reto = (Integer) entry.getKey();
				int exitoReto = (Integer) entry.getValue();
				if (exitoReto != 0)
					continue;
				block2: switch (reto) {
				case 21: {
					if (_efectoID != 77 && _efectoID != 169)
						break;
					for (Luchador luch : pelea._inicioLucEquipo2) {
						if (!objetivos.contains(luch))
							continue;
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
						exitoReto = 2;
						break block2;
					}
					break;
				}
				case 22: {
					if (_efectoID != 84 && _efectoID != 101 && _efectoID != 168)
						break;
					for (Luchador luch : pelea._inicioLucEquipo2) {
						if (!objetivos.contains(luch))
							continue;
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
						exitoReto = 2;
						break block2;
					}
					break;
				}
				case 23: {
					if (_efectoID != 116 && _efectoID != 320)
						break;
					for (Luchador luch : pelea._inicioLucEquipo2) {
						if (!objetivos.contains(luch))
							continue;
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
						exitoReto = 2;
						break block2;
					}
					break;
				}
				case 31: {
					if (pelea.getIDMobReto() == 0)
						break;
					for (Luchador luch : objetivos) {
						if (!pelea._inicioLucEquipo2.contains(luch) || luch.getID() == pelea.getIDMobReto())
							continue;
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
						exitoReto = 2;
						break block2;
					}
					break;
				}
				case 32: {
					if (pelea.getIDMobReto() == 0)
						break;
					for (Luchador luch : objetivos) {
						if (!pelea._inicioLucEquipo2.contains(luch) || luch.getID() == pelea.getIDMobReto())
							continue;
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
						exitoReto = 2;
						break block2;
					}
					break;
				}
				case 34: {
					if ((_efectoID < 91 || _efectoID > 100) && (_efectoID < 85 || _efectoID > 89)
							|| pelea.getIDMobReto() == 0)
						break;
					for (Luchador luch : objetivos) {
						if (!pelea._inicioLucEquipo2.contains(luch) || luch.getID() == pelea.getIDMobReto())
							continue;
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
						exitoReto = 2;
						break block2;
					}
					break;
				}
				case 43: {
					if (_efectoID != 108)
						break;
					for (Luchador luch : objetivos) {
						if (luch.getID() != _lanzador.getID())
							continue;
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
						exitoReto = 2;
						break block2;
					}
					break;
				}
				case 45:
				case 46: {
					if ((_efectoID < 91 || _efectoID > 100) && (_efectoID < 85 || _efectoID > 89))
						break;
					for (Luchador luch : objetivos) {
						if (!pelea._inicioLucEquipo2.contains(luch))
							continue;
						if (luch.getPjAtacante() == 0) {
							luch.setPjAtacante(_lanzador.getID());
							continue;
						}
						if (luch.getPjAtacante() == _lanzador.getID())
							continue;
						SocketManager.ENVIAR_GdaO_RETO_PERDIDO(pelea, reto);
						exitoReto = 2;
						break block2;
					}
					break;
				}
				}
				if (exitoReto == 0)
					continue;
				pelea.getRetos().remove(reto);
				pelea.getRetos().put(reto, exitoReto);
			}
		}
		switch (_efectoID) {
		case 4: {
			aplicarEfecto_4(pelea);
			break;
		}
		case 5: {
			aplicarEfecto_5(objetivos, pelea);
			break;
		}
		case 6: {
			aplicarEfecto_6(objetivos, pelea);
			break;
		}
		case 8: {
			aplicarEfecto_8(objetivos, pelea);
			break;
		}
		case 9: {
			aplicarEfecto_9(objetivos, pelea);
			break;
		}
		case 50: {
			aplicarEfecto_50(pelea);
			break;
		}
		case 51: {
			aplicarEfecto_51(pelea);
			break;
		}
		case 77: {
			aplicarEfecto_77(objetivos, pelea);
			break;
		}
		case 78: {
			aplicarEfecto_78(objetivos, pelea);
			break;
		}
		case 79: {
			aplicarEfecto_79(objetivos, pelea);
			break;
		}
		case 81: {
			aplicarEfecto_81(objetivos, pelea);
			break;
		}
		case 82: {
			aplicarEfecto_82(objetivos, pelea);
			break;
		}
		case 84: {
			aplicarEfecto_84(objetivos, pelea);
			break;
		}
		case 85: {
			aplicarEfecto_85(objetivos, pelea);
			break;
		}
		case 86: {
			aplicarEfecto_86(objetivos, pelea);
			break;
		}
		case 87: {
			aplicarEfecto_87(objetivos, pelea);
			break;
		}
		case 88: {
			aplicarEfecto_88(objetivos, pelea);
			break;
		}
		case 89: {
			aplicarEfecto_89(objetivos, pelea);
			break;
		}
		case 90: {
			aplicarEfecto_90(objetivos, pelea);
			break;
		}
		case 91: {
			aplicarEfecto_91(objetivos, pelea, esCaC);
			break;
		}
		case 92: {
			aplicarEfecto_92(objetivos, pelea, esCaC);
			break;
		}
		case 93: {
			aplicarEfecto_93(objetivos, pelea, esCaC);
			break;
		}
		case 94: {
			aplicarEfecto_94(objetivos, pelea, esCaC);
			break;
		}
		case 95: {
			aplicarEfecto_95(objetivos, pelea, esCaC);
			break;
		}
		case 96: {
			aplicarEfecto_96(objetivos, pelea, esCaC);
			break;
		}
		case 97: {
			aplicarEfecto_97(objetivos, pelea, esCaC);
			break;
		}
		case 98: {
			aplicarEfecto_98(objetivos, pelea, esCaC);
			break;
		}
		case 99: {
			aplicarEfecto_99(objetivos, pelea, esCaC);
			break;
		}
		case 100: {
			aplicarEfecto_100(objetivos, pelea, esCaC);
			break;
		}
		case 101: {
			aplicarEfecto_101(objetivos, pelea);
			break;
		}
		case 105: {
			aplicarEfecto_105(objetivos, pelea);
			break;
		}
		case 106: {
			aplicarEfecto_106(objetivos, pelea);
			break;
		}
		case 107: {
			aplicarEfecto_107(objetivos, pelea);
			break;
		}
		case 108: {
			aplicarEfecto_108(objetivos, pelea, esCaC);
			break;
		}
		case 109: {
			aplicarEfecto_109(pelea);
			break;
		}
		case 110: {
			aplicarEfecto_110(objetivos, pelea);
			break;
		}
		case 111: {
			aplicarEfecto_111(objetivos, pelea);
			break;
		}
		case 112: {
			aplicarEfecto_112(objetivos, pelea);
			break;
		}
		case 114: {
			aplicarEfecto_114(objetivos, pelea);
			break;
		}
		case 115: {
			aplicarEfecto_115(objetivos, pelea);
			break;
		}
		case 116: {
			aplicarEfecto_116(objetivos, pelea);
			break;
		}
		case 117: {
			aplicarEfecto_117(objetivos, pelea);
			break;
		}
		case 118: {
			aplicarEfecto_118(objetivos, pelea);
			break;
		}
		case 119: {
			aplicarEfecto_119(objetivos, pelea);
			break;
		}
		case 120: {
			aplicarEfecto_120(pelea);
			break;
		}
		case 121: {
			aplicarEfecto_121(objetivos, pelea);
			break;
		}
		case 122: {
			aplicarEfecto_122(objetivos, pelea);
			break;
		}
		case 123: {
			aplicarEfecto_123(objetivos, pelea);
			break;
		}
		case 124: {
			aplicarEfecto_124(objetivos, pelea);
			break;
		}
		case 125: {
			aplicarEfecto_125(objetivos, pelea);
			break;
		}
		case 126: {
			aplicarEfecto_126(objetivos, pelea);
			break;
		}
		case 127: {
			aplicarEfecto_127(objetivos, pelea);
			break;
		}
		case 128: {
			aplicarEfecto_128(objetivos, pelea);
			break;
		}
		case 130: {
			aplicarEfecto_130(objetivos, pelea);
			break;
		}
		case 131: {
			aplicarEfecto_131(objetivos, pelea);
			break;
		}
		case 132: {
			aplicarEfecto_132(objetivos, pelea);
			break;
		}
		case 138: {
			aplicarEfecto_138(objetivos, pelea);
			break;
		}
		case 140: {
			aplicarEfecto_140(objetivos, pelea);
			break;
		}
		case 141: {
			aplicarEfecto_141(objetivos, pelea);
			break;
		}
		case 142: {
			aplicarEfecto_142(objetivos, pelea);
			break;
		}
		case 143: {
			aplicarEfecto_143(objetivos, pelea);
			break;
		}
		case 144: {
			aplicarEfecto_144(objetivos, pelea);
		}
		case 145: {
			aplicarEfecto_145(objetivos, pelea);
			break;
		}
		case 149: {
			aplicarEfecto_149(objetivos, pelea);
			break;
		}
		case 150: {
			aplicarEfecto_150(objetivos, pelea);
			break;
		}
		case 152: {
			aplicarEfecto_152(objetivos, pelea);
			break;
		}
		case 153: {
			aplicarEfecto_153(objetivos, pelea);
			break;
		}
		case 154: {
			aplicarEfecto_154(objetivos, pelea);
			break;
		}
		case 155: {
			aplicarEfecto_155(objetivos, pelea);
			break;
		}
		case 156: {
			aplicarEfecto_156(objetivos, pelea);
			break;
		}
		case 157: {
			aplicarEfecto_157(objetivos, pelea);
			break;
		}
		case 160: {
			aplicarEfecto_160(objetivos, pelea);
			break;
		}
		case 161: {
			aplicarEfecto_161(objetivos, pelea);
			break;
		}
		case 162: {
			aplicarEfecto_162(objetivos, pelea);
			break;
		}
		case 163: {
			aplicarEfecto_163(objetivos, pelea);
			break;
		}
		case 164: {
			aplicarEfecto_164(objetivos, pelea);
			break;
		}
		case 165: {
			aplicarEfecto_165(objetivos, pelea);
			break;
		}
		case 168: {
			aplicarEfecto_168(objetivos, pelea);
			break;
		}
		case 169: {
			aplicarEfecto_169(objetivos, pelea);
			break;
		}
		case 171: {
			aplicarEfecto_171(objetivos, pelea);
			break;
		}
		case 176: {
			aplicarEfecto_176(objetivos, pelea);
			break;
		}
		case 177: {
			aplicarEfecto_177(objetivos, pelea);
			break;
		}
		case 178: {
			aplicarEfecto_178(objetivos, pelea);
			break;
		}
		case 179: {
			aplicarEfecto_179(objetivos, pelea);
			break;
		}
		case 180: {
			aplicarEfecto_180(pelea);
			break;
		}
		case 181: {
			aplicarEfecto_181(pelea);
			break;
		}
		case 182: {
			aplicarEfecto_182(objetivos, pelea);
			break;
		}
		case 183: {
			aplicarEfecto_183(objetivos, pelea);
			break;
		}
		case 184: {
			aplicarEfecto_184(objetivos, pelea);
			break;
		}
		case 185: {
			aplicarEfecto_185(pelea);
			break;
		}
		case 186: {
			aplicarEfecto_186(objetivos, pelea);
			break;
		}
		case 202: {
			aplicarEfecto_202(objetivos, pelea, celdas);
			break;
		}
		case 210: {
			aplicarEfecto_210(objetivos, pelea);
			break;
		}
		case 211: {
			aplicarEfecto_211(objetivos, pelea);
			break;
		}
		case 212: {
			aplicarEfecto_212(objetivos, pelea);
			break;
		}
		case 213: {
			aplicarEfecto_213(objetivos, pelea);
			break;
		}
		case 214: {
			aplicarEfecto_214(objetivos, pelea);
			break;
		}
		case 215: {
			aplicarEfecto_215(objetivos, pelea);
			break;
		}
		case 216: {
			aplicarEfecto_216(objetivos, pelea);
			break;
		}
		case 217: {
			aplicarEfecto_217(objetivos, pelea);
			break;
		}
		case 218: {
			aplicarEfecto_218(objetivos, pelea);
			break;
		}
		case 219: {
			aplicarEfecto_219(objetivos, pelea);
			break;
		}
		case 220: {
			aplicarEfecto_220(objetivos, pelea);
			break;
		}
		case 265: {
			aplicarEfecto_265(objetivos, pelea);
			break;
		}
		case 266: {
			aplicarEfecto_266(objetivos, pelea);
			break;
		}
		case 267: {
			aplicarEfecto_267(objetivos, pelea);
			break;
		}
		case 268: {
			aplicarEfecto_268(objetivos, pelea);
			break;
		}
		case 269: {
			aplicarEfecto_269(objetivos, pelea);
			break;
		}
		case 270: {
			aplicarEfecto_270(objetivos, pelea);
			break;
		}
		case 271: {
			aplicarEfecto_271(objetivos, pelea);
			break;
		}
		case 275: {
			aplicarEfecto_275(objetivos, pelea);
			break;
		}
		case 276: {
			aplicarEfecto_276(objetivos, pelea);
			break;
		}
		case 277: {
			aplicarEfecto_277(objetivos, pelea);
			break;
		}
		case 278: {
			aplicarEfecto_278(objetivos, pelea);
			break;
		}
		case 279: {
			aplicarEfecto_279(objetivos, pelea);
			break;
		}
		case 293: {
			aplicarEfecto_293(pelea);
			break;
		}
		case 320: {
			aplicarEfecto_320(objetivos, pelea);
			break;
		}
		case 400: {
			aplicarEfecto_400(pelea);
			break;
		}
		case 401: {
			aplicarEfecto_401(pelea);
			break;
		}
		case 402: {
			aplicarEfecto_402(pelea);
			break;
		}
		case 606: {
			aplicarEfecto_606(objetivos, pelea);
			break;
		}
		case 607: {
			aplicarEfecto_607(objetivos, pelea);
			break;
		}
		case 608: {
			aplicarEfecto_608(objetivos, pelea);
			break;
		}
		case 609: {
			aplicarEfecto_609(objetivos, pelea);
			break;
		}
		case 610: {
			aplicarEfecto_610(objetivos, pelea);
			break;
		}
		case 611: {
			aplicarEfecto_611(objetivos, pelea);
			break;
		}
		case 666: {
			break;
		}
		case 671: {
			aplicarEfecto_671(objetivos, pelea);
			break;
		}
		case 672: {
			aplicarEfecto_672(objetivos, pelea);
			break;
		}
		case 750: {
			aplicarEfecto_750(objetivos, pelea);
			break;
		}
		case 765: {
			aplicarEfecto_765(objetivos, pelea);
			break;
		}
		case 776: {
			aplicarEfecto_776(objetivos, pelea);
			break;
		}
		case 780: {
			aplicarEfecto_780(pelea);
			break;
		}
		case 782: {
			aplicarEfecto_782(objetivos, pelea);
			break;
		}
		case 781: {
			aplicarEfecto_781(objetivos, pelea);
			break;
		}
		case 783: {
			aplicarEfecto_783(pelea);
			break;
		}
		case 784: {
			aplicarEfecto_784(objetivos, pelea);
			break;
		}
		case 786: {
			aplicarEfecto_786(objetivos, pelea);
			break;
		}
		case 787: {
			aplicarEfecto_787(objetivos, pelea);
			break;
		}
		case 788: {
			aplicarEfecto_788(objetivos, pelea);
			break;
		}
		case 950: {
			aplicarEfecto_950(objetivos, pelea);
			break;
		}
		case 951: {
			aplicarEfecto_951(objetivos, pelea);
			break;
		}
		default: {
			System.out.println("efecto no implantado : " + _efectoID + " formula: " + _args);
		}
		}
	}

	private void aplicarEfecto_4(Combate pelea) {
		if (_turnos > 1) {
			return;
		}
		if (_celdaObj.esCaminable(true) && !pelea.celdaOcupada(_celdaObj.getID())) {
			_lanzador.getCeldaPelea().removerLuchador(_lanzador);
			_lanzador.setCeldaPelea(_celdaObj);
			_lanzador.getCeldaPelea().addLuchador(_lanzador);
			ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
			trampas.addAll(pelea.getTrampas());
			for (Combate.Trampa trampa : trampas) {
				short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
						_lanzador.getCeldaPelea().getID());
				if (dist > trampa.getTama\u00f1o())
					continue;
				trampa.activaTrampa(_lanzador);
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + _celdaObj.getID());
		} else if (_lanzador.getPersonaje() != null) {
			SocketManager.ENVIAR_Im1223_MENSAJE_IMBORRABLE(_lanzador.getPersonaje(),
					"La celda a donde se quiere transportar esta ocupada.");
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_5(ArrayList<Luchador> objetivos, Combate pelea) {
		if (objetivos.size() == 1 && this._hechizoID == 120) {
			if (((Luchador) objetivos.get(0)).tieneEstado(6))
				return;
			if (!((Luchador) objetivos.get(0)).estaMuerto())
				this._lanzador.setObjetivoDestZurca(objetivos.get(0));
		}
		Mapa mapaCopia = pelea.getMapaCopia();
		if (this._turnos <= 0)
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto() || objetivo.tieneEstado(6))
					continue;
				Celda celdaLanz = this._celdaObj;
				int dañoFinal = 0;
				Luchador afectado = null;
				if (objetivo.getCeldaPelea().getID() == this._celdaObj.getID())
					celdaLanz = this._lanzador.getCeldaPelea();
				short nuevaCeldaID = Pathfinding.getCeldaDespEmpujon(mapaCopia, celdaLanz, objetivo.getCeldaPelea(),
						this._valor, pelea, objetivo);
				if (nuevaCeldaID == 0)
					continue;
				if (nuevaCeldaID < 0) {
					int a = -nuevaCeldaID;
					int coef = Fórmulas.getRandomValor("1d5+8");
					float b = this._lanzador.getNivel() / 100.0F;
					if (b < 0.1D)
						b = 0.1F;
					float c = b * a;
					dañoFinal = (int) (coef * c);
					if (dañoFinal < 1)
						dañoFinal = 1;
					if (dañoFinal > objetivo.getPDVConBuff())
						dañoFinal = objetivo.getPDVConBuff();
					if (dañoFinal > 0) {
						objetivo.restarPDV(dañoFinal);
						if (objetivo.getPDVConBuff() <= 0) {
							SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100,
									(new StringBuilder(String.valueOf(this._lanzador.getID()))).toString(),
									String.valueOf(objetivo.getID()) + ",-" + dañoFinal);
							pelea.agregarAMuertos(objetivo);
							continue;
						}
					}
					a = this._valor - a;
					nuevaCeldaID = Pathfinding.getCeldaDespEmpujon(mapaCopia, celdaLanz, objetivo.getCeldaPelea(), a,
							pelea, objetivo);
					char dir = Pathfinding.getDirEntreDosCeldas(celdaLanz.getID(), objetivo.getCeldaPelea().getID(),
							mapaCopia, true);
					short celdaSigID = 0;
					if (nuevaCeldaID == 0) {
						celdaSigID = Pathfinding.getSigIDCeldaMismaDir(objetivo.getCeldaPelea().getID(), dir, mapaCopia,
								true);
					} else {
						celdaSigID = Pathfinding.getSigIDCeldaMismaDir(nuevaCeldaID, dir, mapaCopia, true);
					}
					Celda celdaSig = mapaCopia.getCelda(celdaSigID);
					if (celdaSig != null)
						afectado = celdaSig.getPrimerLuchador();
				}
				if (nuevaCeldaID != 0) {
					Celda nueva = mapaCopia.getCelda(nuevaCeldaID);
					if (nueva != null) {
						objetivo.getCeldaPelea().getLuchadores().clear();
						objetivo.setCeldaPelea(nueva);
						nueva.addLuchador(objetivo);
						SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 5,
								(new StringBuilder(String.valueOf(this._lanzador.getID()))).toString(),
								String.valueOf(objetivo.getID()) + "," + nuevaCeldaID);
						try {
							Thread.sleep(500L);
						} catch (Exception exception) {
						}
					}
				}
				if (dañoFinal > 0)
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100,
							(new StringBuilder(String.valueOf(this._lanzador.getID()))).toString(),
							String.valueOf(objetivo.getID()) + ",-" + dañoFinal);
				if (afectado != null) {
					int dañoFinal2 = dañoFinal / 2;
					if (dañoFinal2 < 1)
						dañoFinal2 = 1;
					if (dañoFinal2 > afectado.getPDVConBuff())
						dañoFinal2 = afectado.getPDVConBuff();
					if (dañoFinal2 > 0) {
						afectado.restarPDV(dañoFinal2);
						SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100,
								(new StringBuilder(String.valueOf(this._lanzador.getID()))).toString(),
								String.valueOf(afectado.getID()) + ",-" + dañoFinal2);
						if (afectado.getPDVConBuff() <= 0)
							pelea.agregarAMuertos(afectado);
					}
				}
				try {
					Thread.sleep(300L);
				} catch (Exception exception) {
				}
				for (Trampa trampa : pelea.getTrampas()) {
					int dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
							nuevaCeldaID);
					if (dist <= trampa.getTamaño()) {
						trampa.activaTrampa(objetivo);
						break;
					}
				}
				try {
					Thread.sleep(300L);
				} catch (Exception exception) {
				}
			}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
		}
	}

	private void aplicarEfecto_6(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			block4: for (Luchador objetivo : objetivos) {
				short nuevaCeldaID;
				if (objetivo.estaMuerto() || objetivo.tieneEstado(6))
					continue;
				Celda eCelda = _celdaObj;
				if (objetivo.getCeldaPelea().getID() == _celdaObj.getID()) {
					eCelda = _lanzador.getCeldaPelea();
				}
				if ((nuevaCeldaID = Pathfinding.getNuevaCeldaDespuesGolpe(pelea.getMapaCopia(), eCelda,
						objetivo.getCeldaPelea(), -_valor, pelea, objetivo)) == 0)
					continue;
				if (nuevaCeldaID < 0) {
					int a = -(_valor + nuevaCeldaID);
					nuevaCeldaID = Pathfinding.getNuevaCeldaDespuesGolpe(pelea.getMapaCopia(),
							_lanzador.getCeldaPelea(), objetivo.getCeldaPelea(), a, pelea, objetivo);
					if (nuevaCeldaID == 0 || pelea.getMapaCopia().getCelda(nuevaCeldaID) == null)
						continue;
				}
				objetivo.getCeldaPelea().getLuchadores().clear();
				objetivo.setCeldaPelea(pelea.getMapaCopia().getCelda(nuevaCeldaID));
				objetivo.getCeldaPelea().addLuchador(objetivo);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 5, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + nuevaCeldaID);
				try {
					Thread.sleep(300L);
				} catch (Exception a) {
					// empty catch block
				}
				for (Combate.Trampa trampa : pelea.getTrampas()) {
					short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
							nuevaCeldaID);
					if (dist > trampa.getTama\u00f1o())
						continue;
					trampa.activaTrampa(objetivo);
					continue block4;
				}
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_8(ArrayList<Luchador> objetivos, Combate pelea) {
		if (objetivos.isEmpty()) {
			return;
		}
		Luchador objetivo = objetivos.get(0);
		if (objetivo == null || objetivo.estaMuerto() || objetivo.tieneEstado(6)) {
			return;
		}
		switch (_hechizoID) {
		case 438: {
			if (objetivo.getEquipoBin() == _lanzador.getEquipoBin())
				break;
			return;
		}
		case 445: {
			if (objetivo.getEquipoBin() != _lanzador.getEquipoBin())
				break;
			return;
		}
		}
		Celda exCeldaObjetivo = objetivo.getCeldaPelea();
		Celda exCeldaLanzador = _lanzador.getCeldaPelea();
		exCeldaObjetivo.getLuchadores().clear();
		exCeldaLanzador.getLuchadores().clear();
		objetivo.setCeldaPelea(exCeldaLanzador);
		_lanzador.setCeldaPelea(exCeldaObjetivo);
		exCeldaLanzador.addLuchador(objetivo);
		exCeldaObjetivo.addLuchador(_lanzador);
		ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
		trampas.addAll(pelea.getTrampas());
		for (Combate.Trampa trampa : trampas) {
			short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
					objetivo.getCeldaPelea().getID());
			short dist2 = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
					_lanzador.getCeldaPelea().getID());
			if (dist <= trampa.getTama\u00f1o()) {
				trampa.activaTrampa(objetivo);
				continue;
			}
			if (dist2 > trampa.getTama\u00f1o())
				continue;
			trampa.activaTrampa(_lanzador);
		}
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(_lanzador.getID()),
				String.valueOf(objetivo.getID()) + "," + exCeldaLanzador.getID());
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(_lanzador.getID()),
				String.valueOf(_lanzador.getID()) + "," + exCeldaObjetivo.getID());
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_9(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, _valor, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_50(Combate pelea) {
		Luchador objetivo = _celdaObj.getPrimerLuchador();
		if (objetivo == null) {
			return;
		}
		if (objetivo.estaMuerto()) {
			return;
		}
		objetivo.getCeldaPelea().getLuchadores().clear();
		objetivo.setCeldaPelea(_lanzador.getCeldaPelea());
		objetivo.setEstado(8, -1);
		_lanzador.setEstado(3, -1);
		objetivo.setTransportadoPor(_lanzador);
		_lanzador.setTransportando(objetivo);
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(objetivo.getID()),
				String.valueOf(objetivo.getID()) + "," + 8 + ",1");
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(_lanzador.getID()),
				String.valueOf(_lanzador.getID()) + "," + 3 + ",1");
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 50, String.valueOf(_lanzador.getID()), "" + objetivo.getID());
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_51(Combate pelea) {
		if (!_celdaObj.esCaminable(true) || _celdaObj.getLuchadores().size() > 0) {
			return;
		}
		Luchador objetivo = _lanzador.getTransportando();
		if (objetivo == null || objetivo.estaMuerto()) {
			return;
		}
		Celda celdaLanz = _lanzador.getCeldaPelea();
		celdaLanz.removerLuchador(objetivo);
		objetivo.setCeldaPelea(_celdaObj);
		objetivo.getCeldaPelea().addLuchador(objetivo);
		objetivo.setEstado(8, 0);
		_lanzador.setEstado(3, 0);
		objetivo.setTransportadoPor(null);
		_lanzador.setTransportando(null);
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 51, String.valueOf(_lanzador.getID()),
				String.valueOf(_celdaObj.getID()));
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(objetivo.getID()),
				String.valueOf(objetivo.getID()) + "," + 8 + ",0");
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(_lanzador.getID()),
				String.valueOf(_lanzador.getID()) + "," + 3 + ",0");
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_77(ArrayList<Luchador> afectados, Combate pelea) {
		int valor = -1;
		try {
			valor = Integer.parseInt(_args.split(";")[0]);
		} catch (NumberFormatException numberFormatException) {
			// empty catch block
		}
		if (valor == -1) {
			return;
		}
		int num = 0;
		for (Luchador objetivo : afectados) {
			if (objetivo.estaMuerto())
				continue;
			int perdidos = Fórmulas.getPuntosPerdidos('m', valor, _lanzador, objetivo);
			if (perdidos < valor) {
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 309, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + (valor - perdidos));
			}
			if (perdidos < 1)
				continue;
			objetivo.addBuff(127, perdidos, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 127, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + ",-" + perdidos + "," + _turnos);
			num += perdidos;
		}
		if (num != 0) {
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 128, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + num + "," + _turnos);
			_lanzador.addBuff(128, num, 1, 0, true, _hechizoID, _args, _lanzador, _veneno);
			if (_lanzador.puedeJugar()) {
				_lanzador.addTempPM(pelea, num);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_78(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			if (objetivo.puedeJugar()) {
				objetivo.addTempPM(pelea, val);
			}
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_79(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos < 1) {
			return;
		}
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, -1, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_82(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				int da\u00f1oFinal = _valor;
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_84(ArrayList<Luchador> objetivos, Combate pelea) {
		int valor = -1;
		try {
			valor = Integer.parseInt(_args.split(";")[0]);
		} catch (NumberFormatException numberFormatException) {
			// empty catch block
		}
		if (valor == -1) {
			return;
		}
		int num = 0;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			int perdidos = Fórmulas.getPuntosPerdidos('m', valor, _lanzador, objetivo);
			if (perdidos < valor) {
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 308, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + (valor - perdidos));
			}
			if (perdidos < 1)
				continue;
			if (_hechizoID == 95 || _hechizoID == 2079) {
				objetivo.addBuff(101, perdidos, 1, 1, true, _hechizoID, _args, _lanzador, _veneno);
			} else {
				objetivo.addBuff(101, perdidos, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 101, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + ",-" + perdidos + "," + _turnos);
			num += perdidos;
		}
		if (num != 0) {
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 111, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + num + "," + _turnos);
			_lanzador.addBuff(111, num, 0, 0, true, _hechizoID, _args, _lanzador, _veneno);
			if (_lanzador.puedeJugar()) {
				_lanzador.addTempPA(pelea, num);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_85(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 2, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_86(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 1, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_87(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 4, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_88(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 3, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_89(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_hechizoID == 1679) {
			char[] dir = new char[] { 'b', 'd', 'f', 'h' };
			Luchador victima = objetivos.get(0);
			objetivos.clear();
			for (int i = 0; i < 4; ++i) {
				Luchador objetivo;
				short idSigCelda = Pathfinding.getSigIDCeldaMismaDir(victima.getCeldaPelea().getID(), dir[i],
						pelea.getMapaCopia(), true);
				Celda sigCelda = pelea.getMapaCopia().getCelda(idSigCelda);
				if (sigCelda == null || (objetivo = sigCelda.getPrimerLuchador()) == null)
					continue;
				objetivos.add(objetivo);
			}
			try {
				Thread.sleep(500L);
			} catch (Exception i) {
				// empty catch block
			}
		}
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 0, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_81(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			String[] jet = _args.split(";");
			int cura = 0;
			cura = jet.length < 6 ? 1 : Fórmulas.getRandomValor(jet[5]);
			int cura2 = cura;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				cura = getMaxMinHechizo(objetivo, cura);
				int pdvMax = objetivo.getPDVMaxConBuff();
				int curaFinal = Fórmulas.calculFinalCura(_lanzador, cura, false);
				if (curaFinal + objetivo.getPDVConBuff() > pdvMax) {
					curaFinal = pdvMax - objetivo.getPDVConBuff();
				}
				if (curaFinal < 1) {
					curaFinal = 0;
				}
				objetivo.restarPDV(-curaFinal);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 108, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + curaFinal);
				cura = cura2;
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_90(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			int porc = Fórmulas.getRandomValor(_valores);
			int da\u00f1oFinal = (int) ((double) porc / 100.0 * (double) _lanzador.getPDVConBuff());
			if (da\u00f1oFinal > _lanzador.getPDVConBuff()) {
				da\u00f1oFinal = _lanzador.getPDVConBuff();
			}
			_lanzador.restarPDV(da\u00f1oFinal);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + -da\u00f1oFinal);
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (da\u00f1oFinal + objetivo.getPDVConBuff() > objetivo.getPDVMaxConBuff()) {
					da\u00f1oFinal = objetivo.getPDVMaxConBuff() - objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(-da\u00f1oFinal);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + ",+" + da\u00f1oFinal);
			}
			if (_lanzador.getPDVConBuff() <= 0) {
				pelea.agregarAMuertos(_lanzador);
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_91(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 2, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 2, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_92(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 1, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 1, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_93(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 4, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 4, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_94(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 3, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 3, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_95(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 0, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 0, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				int cura = -da\u00f1oFinal / 2;
				if (_lanzador.getPDVConBuff() + cura > _lanzador.getPDVMaxConBuff()) {
					cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
				}
				_lanzador.restarPDV(-cura);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
						String.valueOf(_lanzador.getID()) + "," + cura);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_96(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 2, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 2, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_97(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 1, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				int da\u00f1oFinal;
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				if (_suerte > 0 && _hechizoID == 108) {
					da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, _lanzador, 1, da\u00f1o, false,
							_hechizoID, _veneno);
					if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, _lanzador, _lanzador,
							pelea, _hechizoID, _veneno)) > _lanzador.getPDVConBuff()) {
						da\u00f1oFinal = _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(da\u00f1oFinal);
					da\u00f1oFinal = -da\u00f1oFinal;
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
							String.valueOf(_lanzador.getID()) + "," + da\u00f1oFinal);
					if (_lanzador.getPDVConBuff() > 0)
						continue;
					pelea.agregarAMuertos(_lanzador);
					continue;
				}
				da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 1, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_98(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 4, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 4, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
			if (_hechizoID == 233 || _hechizoID == 2006) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException objetivo) {
				}
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_99(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 3, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 3, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_100(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 0, da\u00f1o, true,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else if (_turnos <= 0) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(_turnosOriginales);
			}
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				for (EfectoHechizo EH : _lanzador.getBuffsPorEfectoID(293)) {
					if (EH.getValor() != _hechizoID)
						continue;
					int add = -1;
					try {
						add = Integer.parseInt(EH.getArgs().split(";")[2]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (add <= 0)
						continue;
					da\u00f1o += add;
				}
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 0, da\u00f1o, false,
						_hechizoID, _veneno);
				if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno)) > objetivo.getPDVConBuff()) {
					da\u00f1oFinal = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1oFinal);
				int cura = da\u00f1oFinal;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1oFinal = -da\u00f1oFinal;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1oFinal);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_101(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				int perdidos = Fórmulas.getPuntosPerdidos('a', _valor, _lanzador, objetivo);
				if (_valor - perdidos > 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 308, String.valueOf(_lanzador.getID()),
							String.valueOf(objetivo.getID()) + "," + (_valor - perdidos));
				}
				if (perdidos <= 0)
					continue;
				objetivo.addBuff(101, perdidos, 1, 1, true, _hechizoID, _args, _lanzador, _veneno);
				if (_turnos > 1 && _duracion > 1)
					continue;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 101, String.valueOf(objetivo.getID()),
						String.valueOf(objetivo.getID()) + ",-" + perdidos);
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				int perdidos = Fórmulas.getPuntosPerdidos('a', _valor, _lanzador, objetivo);
				if (_valor - perdidos > 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 308, String.valueOf(_lanzador.getID()),
							String.valueOf(objetivo.getID()) + "," + (_valor - perdidos));
				}
				if (perdidos <= 0)
					continue;
				if (_hechizoID == 89) {
					objetivo.addBuff(_efectoID, perdidos, 0, 1, true, _hechizoID, _args, _lanzador, _veneno);
				} else {
					objetivo.addBuff(_efectoID, perdidos, 1, 1, true, _hechizoID, _args, _lanzador, _veneno);
				}
				if (_turnos > 1 && _duracion > 1)
					continue;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 101, String.valueOf(objetivo.getID()),
						String.valueOf(objetivo.getID()) + ",-" + perdidos);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_105(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, false, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_106(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = -1;
		try {
			val = Integer.parseInt(_args.split(";")[1]);
		} catch (Exception exception) {
			// empty catch block
		}
		if (val == -1) {
			return;
		}
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, val, _turnos, 1, false, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_107(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos < 1) {
			return;
		}
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_108(ArrayList<Luchador> objetivos, Combate pelea, boolean esCaC) {
		if (esCaC) {
			if (_lanzador.esInvisible()) {
				_lanzador.hacerseVisible(-1);
			}
			String[] jet = _args.split(";");
			int cura = 0;
			cura = jet.length < 6 ? 1 : Fórmulas.getRandomValor(jet[5]);
			int cura2 = cura;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				cura = getMaxMinHechizo(objetivo, cura);
				int pdvMax = objetivo.getPDVMaxConBuff();
				int curaFinal = Fórmulas.calculFinalCura(_lanzador, cura, esCaC);
				if (curaFinal + objetivo.getPDVConBuff() > pdvMax) {
					curaFinal = pdvMax - objetivo.getPDVConBuff();
				}
				if (curaFinal < 1) {
					curaFinal = 0;
				}
				objetivo.restarPDV(-curaFinal);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 108, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + curaFinal);
				cura = cura2;
			}
		} else if (_turnos <= 0) {
			String[] jet = _args.split(";");
			int cura = 0;
			cura = jet.length < 6 ? 1 : Fórmulas.getRandomValor(jet[5]);
			int cura2 = cura;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				cura = getMaxMinHechizo(objetivo, cura);
				int pdvMax = objetivo.getPDVMaxConBuff();
				int curaFinal = Fórmulas.calculFinalCura(_lanzador, cura, esCaC);
				if (curaFinal + objetivo.getPDVConBuff() > pdvMax) {
					curaFinal = pdvMax - objetivo.getPDVConBuff();
				}
				if (curaFinal < 1) {
					curaFinal = 0;
				}
				objetivo.restarPDV(-curaFinal);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 108, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + curaFinal);
				cura = cura2;
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_109(Combate pelea) {
		if (_turnos <= 0) {
			int da\u00f1o = Fórmulas.getRandomValor(_valores);
			int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, _lanzador, -1, da\u00f1o, false,
					_hechizoID, _veneno);
			if ((da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, _lanzador, _lanzador, pelea,
					_hechizoID, _veneno)) > _lanzador.getPDVConBuff()) {
				da\u00f1oFinal = _lanzador.getPDVConBuff();
			}
			_lanzador.restarPDV(da\u00f1oFinal);
			da\u00f1oFinal = -da\u00f1oFinal;
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + da\u00f1oFinal);
			if (_lanzador.getPDVConBuff() <= 0) {
				pelea.agregarAMuertos(_lanzador);
			}
		} else {
			_lanzador.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_110(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_111(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			if (objetivo.puedeJugar()) {
				objetivo.addTempPA(pelea, val);
			}
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_112(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_114(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_115(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_116(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			if (objetivo.puedeJugar() && objetivo == _lanzador) {
				objetivo.getTotalStatsConBuff().addStat(116, (short) val);
			}
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_117(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			if (objetivo.puedeJugar() && objetivo == _lanzador) {
				objetivo.getTotalStatsConBuff().addStat(117, (short) val);
			}
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_118(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_119(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_120(Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		_lanzador.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
		_lanzador.addTempPA(pelea, val);
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
				String.valueOf(_lanzador.getID()) + "," + val + "," + _turnos);
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_121(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_122(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_123(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_124(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_125(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		if (_hechizoID == 441) {
			return;
		}
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_126(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_127(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				int perdidos = Fórmulas.getPuntosPerdidos('m', _valor, _lanzador, objetivo);
				if (_valor - perdidos > 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 309, String.valueOf(_lanzador.getID()),
							String.valueOf(objetivo.getID()) + "," + (_valor - perdidos));
				}
				if (perdidos <= 0)
					continue;
				objetivo.addBuff(127, perdidos, 1, 1, true, _hechizoID, _args, _lanzador, _veneno);
				if (_turnos > 1 && _duracion > 1)
					continue;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 127, String.valueOf(objetivo.getID()),
						String.valueOf(objetivo.getID()) + ",-" + perdidos);
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				int perdidos = Fórmulas.getPuntosPerdidos('m', _valor, _lanzador, objetivo);
				if (_valor - perdidos > 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 309, String.valueOf(_lanzador.getID()),
							String.valueOf(objetivo.getID()) + "," + (_valor - perdidos));
				}
				if (perdidos <= 0)
					continue;
				if (_hechizoID == 136) {
					objetivo.addBuff(_efectoID, perdidos, _turnos, _turnos, true, _hechizoID, _args, _lanzador,
							_veneno);
				} else {
					objetivo.addBuff(_efectoID, perdidos, 1, 1, true, _hechizoID, _args, _lanzador, _veneno);
				}
				if (_turnos > 1 && _duracion > 1)
					continue;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 127, String.valueOf(objetivo.getID()),
						String.valueOf(objetivo.getID()) + ",-" + perdidos);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_128(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			if (objetivo.puedeJugar()) {
				objetivo.addTempPM(pelea, val);
			}
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_130(ArrayList<Luchador> objetivos, Combate pelea) {
		if (pelea.getTipoPelea() == 0) {
			return;
		}
		int val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			Personaje perso = objetivo.getPersonaje();
			if (objetivo.estaMuerto() || perso == null)
				continue;
			if ((long) val > perso.getKamas()) {
				val = (int) perso.getKamas();
			}
			if (val == 0)
				continue;
			perso.setKamas(perso.getKamas() - (long) val);
			Personaje perso2 = _lanzador.getPersonaje();
			if (perso2 != null) {
				perso2.setKamas(perso2.getKamas() + (long) val);
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 130, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_131(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, _valor, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_132(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.desbuffear();
			if (objetivo.puedeJugar() && objetivo == _lanzador) {
				Personaje.Stats s1 = objetivo.getTotalStatsConBuff();
				Personaje.Stats s2 = objetivo.getTotalStatsSinBuff();
				for (int a = 0; a < 1000; ++a) {
					if (s1._statsEfecto.get(a) == null || s2._statsEfecto.get(a) == null)
						continue;
					int nuevo = s2._statsEfecto.get(a);
					s1.especificarStat(a, nuevo);
				}
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 132, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()));
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_138(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_140(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(300L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_141(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
					&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
				aplicarEfecto_765B(pelea, objetivo);
				objetivo = objetivo.getBuff(765).getLanzador();
			}
			pelea.agregarAMuertos(objetivo);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException interruptedException) {
				// empty catch block
			}
		}
	}

	private void aplicarEfecto_143(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			String[] jet = _args.split(";");
			int cura = 0;
			cura = jet.length < 6 ? 1 : Fórmulas.getRandomValor(jet[5]);
			int dmg2 = cura;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				int curaFinal = Fórmulas.calculFinalCura(_lanzador, cura = getMaxMinHechizo(objetivo, cura), false);
				if (curaFinal + objetivo.getPDVConBuff() > objetivo.getPDVMaxConBuff()) {
					curaFinal = objetivo.getPDVMaxConBuff() - objetivo.getPDVConBuff();
				}
				if (curaFinal < 1) {
					curaFinal = 0;
				}
				objetivo.restarPDV(-curaFinal);
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 108, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + curaFinal);
				cura = dmg2;
			}
		} else {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_142(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_144(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(145, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 145, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_145(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_149(ArrayList<Luchador> objetivos, Combate pelea) {
		int id = -1;
		try {
			id = Integer.parseInt(_args.split(";")[2]);
		} catch (Exception exception) {
			// empty catch block
		}
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			if (id == -1) {
				id = objetivo.getGfxDefecto();
			}
			objetivo.addBuff(_efectoID, id, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			int defecto = objetivo.getGfxDefecto();
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + defecto + "," + id + ","
							+ (objetivo.puedeJugar() ? _turnos + 1 : _turnos));
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_150(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos == 0) {
			return;
		}
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 150, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + ",4");
			objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_152(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_153(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_154(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_155(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_156(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_157(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_160(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_161(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_162(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_163(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_164(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = _valor;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, val, _turnos, 1, false, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_165(ArrayList<Luchador> objetivos, Combate pelea) {
		int valor = -1;
		try {
			valor = Integer.parseInt(_args.split(";")[1]);
		} catch (Exception exception) {
			// empty catch block
		}
		if (valor == -1) {
			return;
		}
		int val2 = valor;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			valor = getMaxMinHechizo(objetivo, valor);
			objetivo.addBuff(_efectoID, valor, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			valor = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_168(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, _valor, 1, 1, true, _hechizoID, _args, _lanzador, _veneno);
				if (_turnos > 1 && _duracion > 1)
					continue;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 168, String.valueOf(objetivo.getID()),
						String.valueOf(objetivo.getID()) + ",-" + _valor);
			}
		} else {
			boolean repetibles = false;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (_hechizoID == 197 || _hechizoID == 112) {
					objetivo.addBuff(_efectoID, _valor, _turnos, _turnos, true, _hechizoID, _args, _lanzador, _veneno);
				} else if (_hechizoID == 115) {
					if (!repetibles) {
						short perdidosPA = (short) Fórmulas.getRandomValor(_valores);
						if (perdidosPA == -1)
							continue;
						_valor = perdidosPA;
					}
					objetivo.addBuff(_efectoID, _valor, _turnos, _turnos, true, _hechizoID, _args, _lanzador, _veneno);
					repetibles = true;
				} else {
					objetivo.addBuff(_efectoID, _valor, 1, 1, true, _hechizoID, _args, _lanzador, _veneno);
				}
				if (_turnos > 1 && _duracion > 1)
					continue;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 168, String.valueOf(objetivo.getID()),
						String.valueOf(objetivo.getID()) + ",-" + _valor);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_169(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, _valor, 1, 1, true, _hechizoID, _args, _lanzador, _veneno);
				if (_turnos > 1 && _duracion > 1)
					continue;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 169, String.valueOf(objetivo.getID()),
						String.valueOf(objetivo.getID()) + ",-" + _valor);
			}
		} else {
			if (!objetivos.isEmpty() && _hechizoID == 120 && _lanzador.getObjetivoDestZurca() != null) {
				_lanzador.getObjetivoDestZurca().addBuff(_efectoID, _valor, _turnos, _turnos, true, _hechizoID, _args,
						_lanzador, _veneno);
				if (_turnos <= 1 || _duracion <= 1) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 169,
							String.valueOf(_lanzador.getObjetivoDestZurca().getID()),
							String.valueOf(_lanzador.getObjetivoDestZurca().getID()) + ",-" + _valor);
				}
			}
			for (Luchador objetivo : objetivos) {
				boolean repetibles = false;
				if (objetivo.estaMuerto())
					continue;
				if (_hechizoID == 192) {
					objetivo.addBuff(_efectoID, _valor, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
				} else if (_hechizoID == 115) {
					if (!repetibles) {
						short perdidosPM = (short) Fórmulas.getRandomValor(_valores);
						if (perdidosPM == -1)
							continue;
						_valor = perdidosPM;
					}
					objetivo.addBuff(_efectoID, _valor, _turnos, _turnos, true, _hechizoID, _args, _lanzador, _veneno);
					repetibles = true;
				} else if (_hechizoID == 197) {
					objetivo.addBuff(_efectoID, _valor, _turnos, _turnos, true, _hechizoID, _args, _lanzador, _veneno);
				} else {
					objetivo.addBuff(_efectoID, _valor, 1, 1, true, _hechizoID, _args, _lanzador, _veneno);
				}
				if (_turnos > 1 && _duracion > 1)
					continue;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 169, String.valueOf(objetivo.getID()),
						String.valueOf(objetivo.getID()) + ",-" + _valor);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_171(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_176(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 176, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_177(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 177, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_178(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_179(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_180(Combate pelea) {
		if (_celdaObj.getPrimerLuchador() != null) {
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 151, String.valueOf(_lanzador.getID()),
					String.valueOf(_hechizoID));
			return;
		}
		int idInvocacion = pelea.getSigIDLuchador();
		Personaje clon = Personaje.personajeClonado(_lanzador.getPersonaje(), idInvocacion);
		Luchador doble = new Luchador(pelea, clon);
		doble.setEquipoBin(_lanzador.getEquipoBin());
		doble.setInvocador(_lanzador);
		_celdaObj.addLuchador(doble);
		doble.setCeldaPelea(_celdaObj);
		pelea.getOrdenJug().add(pelea.getOrdenJug().indexOf(_lanzador) + 1, doble);
		pelea.addLuchadorEnEquipo(doble, _lanzador.getEquipoBin());
		String gm = "+" + doble.stringGM();
		String gtl = pelea.stringOrdenJugadores();
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 180, String.valueOf(_lanzador.getID()), gm);
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(_lanzador.getID()), gtl);
		ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
		trampas.addAll(pelea.getTrampas());
		for (Combate.Trampa trampa : trampas) {
			short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
					doble.getCeldaPelea().getID());
			if (dist > trampa.getTama\u00f1o())
				continue;
			trampa.activaTrampa(doble);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_181(Combate pelea) {
		if (_celdaObj.getPrimerLuchador() != null) {
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 151, String.valueOf(_lanzador.getID()),
					String.valueOf(_hechizoID));
			return;
		}
		int mobID = -1;
		int mobNivel = -1;
		try {
			mobID = Integer.parseInt(_args.split(";")[0]);
			mobNivel = Integer.parseInt(_args.split(";")[1]);
		} catch (Exception exception) {
			// empty catch block
		}
		MobModelo.MobGrado mob = null;
		try {
			mob = Mundo.getMobModelo(mobID).getGradoPorNivel(mobNivel).copiarMob();
		} catch (Exception e1) {
			System.out.println("El Mob ID esta mal configurado: " + mobID);
			return;
		}
		if (mobID == -1 || mobNivel == -1 || mob == null) {
			return;
		}
		int idInvocacion = pelea.getSigIDLuchador() - pelea.getNumeroInvos();
		mob.setIdEnPelea(idInvocacion);
		mob.modificarStatsPorInvocador(_lanzador);
		Luchador invocacion = new Luchador(pelea, mob);
		invocacion.setEquipoBin(_lanzador.getEquipoBin());
		invocacion.setInvocador(_lanzador);
		_celdaObj.addLuchador(invocacion);
		invocacion.setCeldaPelea(_celdaObj);
		pelea.getOrdenJug().add(pelea.getOrdenJug().indexOf(_lanzador) + 1, invocacion);
		pelea.addLuchadorEnEquipo(invocacion, _lanzador.getEquipoBin());
		String gm = "+" + invocacion.stringGM();
		String gtl = pelea.stringOrdenJugadores();
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 181, String.valueOf(_lanzador.getID()), gm);
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(_lanzador.getID()), gtl);
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
		_lanzador.aumentarInvocaciones();
		ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
		trampas.addAll(pelea.getTrampas());
		for (Combate.Trampa trampa : trampas) {
			short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
					invocacion.getCeldaPelea().getID());
			if (dist > trampa.getTama\u00f1o())
				continue;
			trampa.activaTrampa(invocacion);
		}
		try {
			Thread.sleep(300L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_182(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_183(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, false, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_184(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, false, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_185(Combate pelea) {
		short celdaID = _celdaObj.getID();
		int mobID = -1;
		int nivel = -1;
		try {
			mobID = Integer.parseInt(_args.split(";")[0]);
			nivel = Integer.parseInt(_args.split(";")[1]);
		} catch (Exception exception) {
			// empty catch block
		}
		MobModelo.MobGrado MG = null;
		try {
			MG = Mundo.getMobModelo(mobID).getGradoPorNivel(nivel).copiarMob();
		} catch (Exception e1) {
			System.out.println("El Mob ID esta mal configurado: " + mobID);
			return;
		}
		if (mobID == -1 || nivel == -1 || MG == null) {
			return;
		}
		int idInvocacion = pelea.getSigIDLuchador() - pelea.getNumeroInvos();
		MG.setIdEnPelea(idInvocacion);
		MG.modificarStatsPorInvocador(_lanzador);
		Luchador invocacion = new Luchador(pelea, MG);
		int equipoLanz = _lanzador.getEquipoBin();
		invocacion.setEquipoBin(equipoLanz);
		invocacion.setInvocador(_lanzador);
		Celda nuevaCelda = pelea.getMapaCopia().getCelda(celdaID);
		nuevaCelda.addLuchador(invocacion);
		invocacion.setCeldaPelea(nuevaCelda);
		pelea.addLuchadorEnEquipo(invocacion, equipoLanz);
		String gm = "+" + invocacion.stringGM();
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 185, String.valueOf(_lanzador.getID()), gm);
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_186(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_202(ArrayList<Luchador> objetivos, Combate pelea, ArrayList<Celda> celdas) {
		int equipo = _lanzador.getParamEquipoAliado();
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto() || !objetivo.esInvisible())
				continue;
			objetivo.aparecer(_lanzador);
		}
		for (Combate.Trampa trampa : pelea.getTrampas()) {
			if (trampa.getParamEquipoDue\u00f1o() == equipo)
				continue;
			for (Celda celda : celdas) {
				if (celda.getID() != trampa.getCelda().getID())
					continue;
				trampa.esVisibleParaEnemigo();
				trampa.aparecer(equipo);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_210(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_211(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_212(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_213(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_214(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_215(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_216(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_217(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_218(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_219(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_220(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos < 1) {
			return;
		}
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_265(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_266(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		int vol = 0;
		int val2 = val;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(152, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 152, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			vol += val;
			val = val2;
		}
		if (vol != 0) {
			_lanzador.addBuff(123, vol, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 123, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + vol + "," + _turnos);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_267(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		int vol = 0;
		int val2 = val;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(153, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 153, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			vol += val;
			val = val2;
		}
		if (vol == 0) {
			return;
		}
		_lanzador.addBuff(125, vol, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 125, String.valueOf(_lanzador.getID()),
				String.valueOf(_lanzador.getID()) + "," + vol + "," + _turnos);
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_268(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		int vol = 0;
		int val2 = val;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(154, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 154, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			vol += val;
			val = val2;
		}
		if (vol != 0) {
			_lanzador.addBuff(119, vol, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 119, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + vol + "," + _turnos);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_269(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		int vol = 0;
		int val2 = val;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(155, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 155, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			vol += val;
			val = val2;
		}
		if (vol == 0) {
			_lanzador.addBuff(126, vol, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 126, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + vol + "," + _turnos);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_270(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		int vol = 0;
		int val2 = val;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(156, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 156, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			vol += val;
			val = val2;
		}
		if (vol == 0) {
			_lanzador.addBuff(124, vol, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 124, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + vol + "," + _turnos);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_271(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		int vol = 0;
		int val2 = val;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(157, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 157, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			vol += val;
			val = val2;
		}
		if (vol == 0) {
			_lanzador.addBuff(118, vol, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 118, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + vol + "," + _turnos);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_275(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 2, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_276(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 1, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_277(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 4, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_278(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 3, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_279(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos <= 0) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= _nivelHechizo && _hechizoID != 0) {
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106, String.valueOf(objetivo.getID()),
							String.valueOf(objetivo.getID()) + ",1");
					objetivo = _lanzador;
				}
				if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
						&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
					aplicarEfecto_765B(pelea, objetivo);
					objetivo = objetivo.getBuff(765).getLanzador();
				}
				int da\u00f1o = Fórmulas.getRandomValor(_valores);
				da\u00f1o = getMaxMinHechizo(objetivo, da\u00f1o);
				da\u00f1o = (int) ((double) da\u00f1o / 100.0 * (double) _lanzador.getPDVConBuff());
				int da\u00f1oFinal = Fórmulas.calculFinalDa\u00f1o(pelea, _lanzador, objetivo, 0, da\u00f1o, false,
						_hechizoID, _veneno);
				if (da\u00f1o < 0) {
					da\u00f1o = 0;
				}
				da\u00f1oFinal = EfectoHechizo.aplicarBuffContraGolpe(da\u00f1oFinal, objetivo, _lanzador, pelea,
						_hechizoID, _veneno);
				if (da\u00f1o > objetivo.getPDVConBuff()) {
					da\u00f1o = objetivo.getPDVConBuff();
				}
				objetivo.restarPDV(da\u00f1o);
				int cura = da\u00f1o;
				if (objetivo.tieneBuff(786)) {
					if (cura + _lanzador.getPDVConBuff() > _lanzador.getPDVMaxConBuff()) {
						cura = _lanzador.getPDVMaxConBuff() - _lanzador.getPDVConBuff();
					}
					_lanzador.restarPDV(-cura);
					SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(objetivo.getID()),
							String.valueOf(_lanzador.getID()) + ",+" + cura);
				}
				da\u00f1o = -da\u00f1o;
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + da\u00f1o);
				if (objetivo.getPDVConBuff() > 0)
					continue;
				pelea.agregarAMuertos(objetivo);
			}
		} else {
			_veneno = true;
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(_efectoID, 0, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_293(Combate pelea) {
		_lanzador.addBuff(_efectoID, _valor, _turnos, 1, false, _hechizoID, _args, _lanzador, _veneno);
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_320(ArrayList<Luchador> objetivos, Combate pelea) {
		int valor = 1;
		try {
			valor = Integer.parseInt(_args.split(";")[0]);
		} catch (NumberFormatException numberFormatException) {
			// empty catch block
		}
		int num = 0;
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(116, valor, _turnos, 0, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 116, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + valor + "," + _turnos);
			num = (short) (num + valor);
		}
		if (num != 0) {
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 117, String.valueOf(_lanzador.getID()),
					String.valueOf(_lanzador.getID()) + "," + num + "," + _turnos);
			_lanzador.addBuff(117, num, 1, 0, true, _hechizoID, _args, _lanzador, _veneno);
			if (_lanzador.puedeJugar()) {
				_lanzador.getTotalStatsConBuff().addStat(117, num);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_400(Combate pelea) {
		if (!_celdaObj.esCaminable(true)) {
			return;
		}
		if (_celdaObj.getPrimerLuchador() != null) {
			return;
		}
		for (Combate.Trampa trampa : pelea.getTrampas()) {
			if (trampa.getCelda().getID() != _celdaObj.getID())
				continue;
			return;
		}
		String[] infos = _args.split(";");
		short hechizoTrampaID = Short.parseShort(infos[0]);
		byte nivel = Byte.parseByte(infos[1]);
		String po = Mundo.getHechizo(_hechizoID).getStatsPorNivel(_nivelHechizo).getAfectados();
		byte tama\u00f1o = (byte) CryptManager.getNumeroPorValorHash(po.charAt(1));
		Hechizo.StatsHechizos ST = Mundo.getHechizo(hechizoTrampaID).getStatsPorNivel(nivel);
		Combate.Trampa trampa = new Combate.Trampa(pelea, _lanzador, _celdaObj, tama\u00f1o, ST, _hechizoID);
		pelea.getTrampas().add(trampa);
		int color = trampa.getColor();
		int equipo = _lanzador.getEquipoBin() + 1;
		String str = "GDZ+" + _celdaObj.getID() + ";" + tama\u00f1o + ";" + color;
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, equipo, 999, String.valueOf(_lanzador.getID()), str);
		str = "GDC" + _celdaObj.getID() + ";Haaaaaaaaz3005;";
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, equipo, 999, String.valueOf(_lanzador.getID()), str);
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_401(Combate pelea) {
		if (!_celdaObj.esCaminable(true)) {
			return;
		}
		if (_celdaObj.getPrimerLuchador() != null) {
			return;
		}
		String[] infos = _args.split(";");
		short hechizoGlifoID = Short.parseShort(infos[0]);
		byte nivel = Byte.parseByte(infos[1]);
		byte duracion = Byte.parseByte(infos[3]);
		String po = Mundo.getHechizo(_hechizoID).getStatsPorNivel(_nivelHechizo).getAfectados();
		byte tama\u00f1o = (byte) CryptManager.getNumeroPorValorHash(po.charAt(1));
		Hechizo.StatsHechizos ST = Mundo.getHechizo(hechizoGlifoID).getStatsPorNivel(nivel);
		Combate.Glifo glifo = new Combate.Glifo(pelea, _lanzador, _celdaObj, tama\u00f1o, ST, duracion, _hechizoID);
		pelea.getGlifos().add(glifo);
		int color = glifo.getColor();
		String str = "GDZ+" + _celdaObj.getID() + ";" + tama\u00f1o + ";" + color;
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(_lanzador.getID()), str);
		str = "GDC" + _celdaObj.getID() + ";Haaaaaaaaa3005;";
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(_lanzador.getID()), str);
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_402(Combate pelea) {
		if (!_celdaObj.esCaminable(true)) {
			return;
		}
		String[] infos = _args.split(";");
		short hechizoGlifoID = Short.parseShort(infos[0]);
		byte nivel = Byte.parseByte(infos[1]);
		byte duracion = Byte.parseByte(infos[3]);
		String po = Mundo.getHechizo(_hechizoID).getStatsPorNivel(_nivelHechizo).getAfectados();
		byte tama\u00f1o = (byte) CryptManager.getNumeroPorValorHash(po.charAt(1));
		Hechizo.StatsHechizos ST = Mundo.getHechizo(hechizoGlifoID).getStatsPorNivel(nivel);
		Combate.Glifo glifo = new Combate.Glifo(pelea, _lanzador, _celdaObj, tama\u00f1o, ST, duracion, _hechizoID);
		pelea.getGlifos().add(glifo);
		int color = glifo.getColor();
		String str = "GDZ+" + _celdaObj.getID() + ";" + tama\u00f1o + ";" + color;
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(_lanzador.getID()), str);
		str = "GDC" + _celdaObj.getID() + ";Haaaaaaaaa3005;";
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(_lanzador.getID()), str);
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_606(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(124, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 124, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_607(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(118, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 118, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_608(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(123, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 123, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_609(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(119, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 119, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_610(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(125, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 125, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_611(ArrayList<Luchador> objetivos, Combate pelea) {
		int val;
		int val2 = val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			val = getMaxMinHechizo(objetivo, val);
			objetivo.addBuff(126, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 126, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
			val = val2;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_671(ArrayList<Luchador> objetivos, Combate pelea) {
		float val = Fórmulas.getRandomValor(this._valores) / 100.0F;
		int pdvMax = this._lanzador.getPDVMaxConBuff();
		int pdvMedio = pdvMax / 2;
		float porc = 1.0F - Math.abs(this._lanzador.getPDVConBuff() - pdvMedio) / pdvMedio;
		int daño = (int) (porc * val * pdvMax);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo) {
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106,
						(new StringBuilder(String.valueOf(objetivo.getID()))).toString(),
						String.valueOf(objetivo.getID()) + ",1");
				objetivo = this._lanzador;
			}
			if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
					&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
				aplicarEfecto_765B(pelea, objetivo);
				objetivo = objetivo.getBuff(765).getLanzador();
			}
			if (objetivo.tieneBuff(105)) {
				daño -= objetivo.getBuff(105).getValor();
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 105,
						(new StringBuilder(String.valueOf(this._lanzador.getID()))).toString(),
						String.valueOf(objetivo.getID()) + "," + objetivo.getBuff(105).getValor());
			}
			Stats totalObjetivo = objetivo.getTotalStatsConBuff();
			int resMasT = totalObjetivo.getEfecto(241);
			int resPorcT = totalObjetivo.getEfecto(214);
			daño -= resMasT;
			int reduc = (int) (daño * resPorcT / 100.0F);
			daño -= reduc;
			if (daño < 1)
				daño = 1;
			int dañoFinal = aplicarBuffContraGolpe(daño, objetivo, this._lanzador, pelea, this._hechizoID,
					this._veneno);
			if (dañoFinal > objetivo.getPDVConBuff())
				dañoFinal = objetivo.getPDVConBuff();
			objetivo.restarPDV(dañoFinal);
			dañoFinal = -dañoFinal;
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100,
					(new StringBuilder(String.valueOf(this._lanzador.getID()))).toString(),
					String.valueOf(objetivo.getID()) + "," + dañoFinal);
			if (objetivo.getPDVConBuff() <= 0)
				pelea.agregarAMuertos(objetivo);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
		}
	}

	private void aplicarEfecto_672(ArrayList<Luchador> objetivos, Combate pelea) {
		float val = Fórmulas.getRandomValor(this._valores) / 100.0F;
		int pdvMax = this._lanzador.getPDVMaxConBuff();
		int pdvMedio = pdvMax / 2;
		float porc = 1.0F - Math.abs(this._lanzador.getPDVConBuff() - pdvMedio) / pdvMedio;
		int daño = (int) ((porc * val * pdvMax) * 0.95D);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			if (objetivo.tieneBuff(106) && objetivo.getValorBuffPelea(106) >= this._nivelHechizo) {
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 106,
						(new StringBuilder(String.valueOf(objetivo.getID()))).toString(),
						String.valueOf(objetivo.getID()) + ",1");
				objetivo = this._lanzador;
			}
			if (objetivo.tieneBuff(765) && objetivo.getBuff(765) != null
					&& !objetivo.getBuff(765).getLanzador().estaMuerto()) {
				aplicarEfecto_765B(pelea, objetivo);
				objetivo = objetivo.getBuff(765).getLanzador();
			}
			if (objetivo.tieneBuff(105)) {
				daño -= objetivo.getBuff(105).getValor();
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 105,
						(new StringBuilder(String.valueOf(this._lanzador.getID()))).toString(),
						String.valueOf(objetivo.getID()) + "," + objetivo.getBuff(105).getValor());
			}
			Stats totalObjetivo = objetivo.getTotalStatsConBuff();
			int resMasT = totalObjetivo.getEfecto(241);
			int resPorcT = totalObjetivo.getEfecto(214);
			daño -= resMasT;
			int reduc = (int) (daño * resPorcT / 100.0F);
			daño -= reduc;
			if (daño < 1)
				daño = 1;
			int dañoFinal = aplicarBuffContraGolpe(daño, objetivo, this._lanzador, pelea, this._hechizoID,
					this._veneno);
			if (dañoFinal > objetivo.getPDVConBuff())
				dañoFinal = objetivo.getPDVConBuff();
			objetivo.restarPDV(dañoFinal);
			dañoFinal = -dañoFinal;
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 100,
					(new StringBuilder(String.valueOf(this._lanzador.getID()))).toString(),
					String.valueOf(objetivo.getID()) + "," + dañoFinal);
			if (objetivo.getPDVConBuff() <= 0)
				pelea.agregarAMuertos(objetivo);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
		}
	}

	private void aplicarEfecto_750(ArrayList<Luchador> objetivos, Combate pelea) {
	}

	private void aplicarEfecto_765(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, 0, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_765B(Combate pelea, Luchador objetivo) {
		Luchador sacrificado = objetivo.getBuff(765).getLanzador();
		Celda cSacrificado = sacrificado.getCeldaPelea();
		Celda cObjetivo = objetivo.getCeldaPelea();
		cSacrificado.getLuchadores().clear();
		cObjetivo.getLuchadores().clear();
		sacrificado.setCeldaPelea(cObjetivo);
		cObjetivo.addLuchador(sacrificado);
		objetivo.setCeldaPelea(cSacrificado);
		cSacrificado.addLuchador(objetivo);
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(objetivo.getID()),
				String.valueOf(objetivo.getID()) + "," + cSacrificado.getID());
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(sacrificado.getID()),
				String.valueOf(sacrificado.getID()) + "," + cObjetivo.getID());
		try {
			Thread.sleep(300L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_776(ArrayList<Luchador> objetivos, Combate pelea) {
		int val = Fórmulas.getRandomValor(_valores);
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, val, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, _efectoID, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + val + "," + _turnos);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_780(Combate pelea) {
		Map<Integer, Luchador> muertos = pelea.getListaMuertos();
		Luchador objetivo = null;
		for (Map.Entry<Integer, Luchador> entry : muertos.entrySet()) {
			Luchador muerto = entry.getValue();
			if (muerto.estaRetirado() || muerto.getEquipoBin() != _lanzador.getEquipoBin()
					|| muerto.esInvocacion() && muerto.getInvocador().estaMuerto())
				continue;
			objetivo = muerto;
		}
		if (objetivo == null) {
			return;
		}
		objetivo.setEstaMuerto(false);
		objetivo.setCeldaPelea(_celdaObj);
		objetivo.getCeldaPelea().addLuchador(objetivo);
		objetivo.getBuffPelea().clear();
		int vida = (100 - _valor) * objetivo.getPDVMaxConBuff() / 100;
		if (!objetivo.esInvocacion()) {
			SocketManager.ENVIAR_ILF_CANTIDAD_DE_VIDA(objetivo.getPersonaje(), vida);
		} else {
			pelea.getOrdenJug().add(pelea.getOrdenJug().indexOf(_lanzador) + 1, objetivo);
		}
		objetivo.restarPDV(-vida);
		pelea.addLuchadorEnEquipo(objetivo, _lanzador.getEquipoBin());
		String gm = "+" + objetivo.stringGM();
		String gtl = pelea.stringOrdenJugadores();
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 780, String.valueOf(objetivo.getID()), gm);
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 999, String.valueOf(objetivo.getID()), gtl);
		if (!objetivo.esInvocacion()) {
			SocketManager.ENVIAR_As_STATS_DEL_PJ(objetivo.getPersonaje());
		}
		objetivo.setInvocador(_lanzador);
		pelea.borrarUnMuerto(objetivo);
		_lanzador.aumentarInvocaciones();
		ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
		trampas.addAll(pelea.getTrampas());
		for (Combate.Trampa trampa : trampas) {
			short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
					objetivo.getCeldaPelea().getID());
			if (dist > trampa.getTama\u00f1o())
				continue;
			trampa.activaTrampa(objetivo);
		}
		try {
			Thread.sleep(500L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_781(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, _valor, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_782(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, _valor, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_783(Combate pelea) {
		Celda c1celda;
		Luchador objetivo;
		block13: {
			Celda celdaLanzador = _lanzador.getCeldaPelea();
			char d = Pathfinding.getDirEntreDosCeldas(celdaLanzador.getID(), _celdaObj.getID(), pelea.getMapaCopia(),
					true);
			short idSigCelda = Pathfinding.getSigIDCeldaMismaDir(celdaLanzador.getID(), d, pelea.getMapaCopia(), true);
			Celda sigCelda = pelea.getMapaCopia().getCelda(idSigCelda);
			if (sigCelda == null) {
				return;
			}
			if (sigCelda.getLuchadores().isEmpty()) {
				return;
			}
			objetivo = sigCelda.getPrimerLuchador();
			if (objetivo.tieneEstado(6)) {
				return;
			}
			short c1 = idSigCelda;
			short c2 = 0;
			int limite = 0;
			c1celda = pelea.getMapaCopia().getCelda(c1);
			Celda case2 = null;
			ArrayList<Celda> trampas = new ArrayList<Celda>();
			for (Combate.Trampa trampa : pelea.getTrampas()) {
				trampas.add(trampa.getCelda());
			}
			do {
				if (Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true) == -1) {
					return;
				}
				c2 = Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true);
				case2 = pelea.getMapaCopia().getCelda(c2);
				if (!case2.esCaminable(true) || pelea.celdaOcupada(c2))
					break block13;
				if (Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true) == _celdaObj.getID()) {
					c1 = Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true);
					c1celda = pelea.getMapaCopia().getCelda(c1);
					break block13;
				}
				c1 = Pathfinding.getSigIDCeldaMismaDir(c1, d, pelea.getMapaCopia(), true);
				c1celda = pelea.getMapaCopia().getCelda(c1);
				if (c1celda == null) {
					return;
				}
				if (trampas.contains(c1celda))
					break block13;
			} while (++limite <= 50);
			return;
		}
		objetivo.getCeldaPelea().getLuchadores().clear();
		objetivo.setCeldaPelea(c1celda);
		objetivo.getCeldaPelea().addLuchador(objetivo);
		SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 5, String.valueOf(_lanzador.getID()),
				String.valueOf(objetivo.getID()) + "," + c1celda.getID());
		try {
			Thread.sleep(300L);
		} catch (Exception trampa) {
			// empty catch block
		}
		for (Combate.Trampa trampa : pelea.getTrampas()) {
			short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
					c1celda.getID());
			if (dist > trampa.getTama\u00f1o())
				continue;
			trampa.activaTrampa(objetivo);
			break;
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_784(ArrayList<Luchador> objetivos, Combate pelea) {
		if (_turnos > 1) {
			return;
		}
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto() || objetivo.esInvocacion())
				continue;
			Celda celda1 = null;
			for (Map.Entry<Integer, Celda> entry : pelea.getPosInicial().entrySet()) {
				if (entry.getKey().intValue() != objetivo.getID())
					continue;
				celda1 = entry.getValue();
				break;
			}
			if (celda1.esCaminable(true) && !pelea.celdaOcupada(celda1.getID())) {
				objetivo.getCeldaPelea().getLuchadores().clear();
				objetivo.setCeldaPelea(celda1);
				objetivo.getCeldaPelea().addLuchador(objetivo);
				ArrayList<Combate.Trampa> trampas = new ArrayList<Combate.Trampa>();
				trampas.addAll(pelea.getTrampas());
				for (Combate.Trampa trampa : trampas) {
					short dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), trampa.getCelda().getID(),
							objetivo.getCeldaPelea().getID());
					if (dist > trampa.getTama\u00f1o())
						continue;
					trampa.activaTrampa(objetivo);
				}
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 4, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + celda1.getID());
			}
			try {
				Thread.sleep(200L);
			} catch (InterruptedException interruptedException) {
				// empty catch block
			}
		}
	}

	private void aplicarEfecto_786(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, _valor, _turnos, 1, true, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_787(ArrayList<Luchador> objetivos, Combate pelea) {
		int hechizoID = -1;
		int hechizoNivel = -1;
		try {
			hechizoID = Integer.parseInt(_args.split(";")[0]);
			hechizoNivel = Integer.parseInt(_args.split(";")[1]);
		} catch (Exception exception) {
			// empty catch block
		}
		Hechizo hechizo = Mundo.getHechizo(hechizoID);
		ArrayList<EfectoHechizo> EH = hechizo.getStatsPorNivel(hechizoNivel).getEfectos();
		for (EfectoHechizo eh : EH) {
			for (Luchador objetivo : objetivos) {
				if (objetivo.estaMuerto())
					continue;
				objetivo.addBuff(eh._efectoID, eh._valor, 1, 1, true, eh._hechizoID, eh._args, _lanzador, _veneno);
			}
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_788(ArrayList<Luchador> objetivos, Combate pelea) {
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			objetivo.addBuff(_efectoID, _valor, _turnos, 1, true, _hechizoID, _args, objetivo, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
			// empty catch block
		}
	}

	private void aplicarEfecto_950(ArrayList<Luchador> objetivos, Combate pelea) {
		int idEstado = -1;
		try {
			idEstado = Integer.parseInt(_args.split(";")[2]);
		} catch (Exception exception) {
		}
		if (idEstado == -1) {
			return;
		}
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto())
				continue;
			if (_turnos <= 0) {
				if (objetivo.puedeJugar()) {
					objetivo.setEstado(idEstado, _turnos + 1);
				} else {
					objetivo.setEstado(idEstado, _turnos);
				}
				SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(_lanzador.getID()),
						String.valueOf(objetivo.getID()) + "," + idEstado + ",1");
				continue;
			}
			if (objetivo.puedeJugar()) {
				objetivo.setEstado(idEstado, _turnos + 1);
			} else {
				objetivo.setEstado(idEstado, _turnos);
			}
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(_lanzador.getID()),
					String.valueOf(objetivo.getID()) + "," + idEstado + ",1");
			objetivo.addBuff(_efectoID, idEstado, _turnos, 1, false, _hechizoID, _args, _lanzador, _veneno);
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {
		}
	}

	private void aplicarEfecto_951(ArrayList<Luchador> objetivos, Combate pelea) {
		int id = -1;
		try {
			id = Integer.parseInt(_args.split(";")[2]);
		} catch (Exception exception) {}
		if (id == -1) {
			return;
		}
		for (Luchador objetivo : objetivos) {
			if (objetivo.estaMuerto() || !objetivo.tieneEstado(id))
				continue;
			objetivo.setEstado(id, 0);
			SocketManager.ENVIAR_GA_ACCION_PELEA(pelea, 7, 950, String.valueOf(_lanzador.getID()), String.valueOf(objetivo.getID()) + "," + id + ",0");
		}
		try {
			Thread.sleep(200L);
		} catch (InterruptedException interruptedException) {}
	}
}