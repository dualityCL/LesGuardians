package common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import common.CryptManager;
import common.Fórmulas;
import common.Pathfinding;
import common.Mundo;

import game.GameThread;

import objects.Combate;
import objects.Combate.Luchador;
import objects.Mapa;
import objects.Mapa.Celda;
import objects.Hechizo;
import objects.Hechizo.StatsHechizos;
import objects.EfectoHechizo;

public class IA {

	public static class MotorIA implements Runnable {
		private Combate _pelea;
		private Combate.Luchador _atacante;
		private static boolean stop = false;
		private Thread _t;

		public MotorIA(Combate.Luchador atacante, Combate pelea) {
			this._atacante = atacante;
			this._pelea = pelea;
			this._t = new Thread(this);
			this._t.setDaemon(true);
			this._t.start();
		}

		@Override
		public void run() {
			stop = false;
			if (this._atacante.getMob() == null) {
				if (this._atacante.esDoble()) {
					MotorIA.tipo_5(this._atacante, this._pelea);
					try {
						Thread.sleep(1500L);
					} catch (InterruptedException interruptedException) {
						// empty catch block
					}
					this._pelea.finTurno(this._atacante);
				} else if (this._atacante.esRecaudador()) {
					MotorIA.tipo_Recaudador(this._atacante, this._pelea);
					try {
						Thread.sleep(1500L);
					} catch (InterruptedException interruptedException) {
						// empty catch block
					}
					this._pelea.finTurno(this._atacante);
				} else if (this._atacante.esPrisma()) {
					MotorIA.tipo_Prisma(this._atacante, this._pelea);
					try {
						Thread.sleep(1500L);
					} catch (InterruptedException interruptedException) {
						// empty catch block
					}
					this._pelea.finTurno(this._atacante);
				} else {
					try {
						Thread.sleep(1500L);
					} catch (InterruptedException interruptedException) {
						// empty catch block
					}
					this._pelea.finTurno(this._atacante);
				}
			} else if (this._atacante.getMob().getModelo() == null) {
				this._pelea.finTurno(this._atacante);
			} else {
				switch (this._atacante.getMob().getModelo().getTipoInteligencia()) {
				case 0: {
					MotorIA.tipo_0(this._atacante, this._pelea);
					break;
				}
				case 1: {
					MotorIA.tipo_1(this._atacante, this._pelea);
					break;
				}
				case 2: {
					MotorIA.tipo_2(this._atacante, this._pelea);
					break;
				}
				case 3: {
					MotorIA.tipo_3(this._atacante, this._pelea);
					break;
				}
				case 4: {
					MotorIA.tipo_4(this._atacante, this._pelea);
					break;
				}
				case 5: {
					MotorIA.tipo_5(this._atacante, this._pelea);
					break;
				}
				case 6: {
					MotorIA.tipo_6(this._atacante, this._pelea);
					break;
				}
				case 7: {
					MotorIA.tipo_7(this._atacante, this._pelea);
					break;
				}
				case 8: {
					MotorIA.tipo_8(this._atacante, this._pelea);
					break;
				}
				case 9: {
					MotorIA.tipo_9(this._atacante, this._pelea);
					break;
				}
				case 10: {
					MotorIA.tipo_10(this._atacante, this._pelea);
					break;
				}
				case 11: {
					MotorIA.tipo_11(this._atacante, this._pelea);
					break;
				}
				case 12: {
					MotorIA.tipo_12(this._atacante, this._pelea);
					break;
				}
				case 13: {
					MotorIA.tipo_13(this._atacante, this._pelea);
					break;
				}
				case 14: {
					MotorIA.tipo_14(this._atacante, this._pelea);
					break;
				}
				case 15: {
					MotorIA.tipo_15(this._atacante, this._pelea);
					break;
				}
				case 16: {
					MotorIA.tipo_16(this._atacante, this._pelea);
				}
				}
				try {
					Thread.sleep(1500L);
				} catch (InterruptedException interruptedException) {
					// empty catch block
				}
				if (!this._atacante.estaMuerto()) {
					this._pelea.finTurno(this._atacante);
				}
			}
		}

		private static void tipo_0(Combate.Luchador lanzador, Combate pelea) {
			stop = true;
		}

		private static void tipo_1(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				int ataque;
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				int porcPDV = lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff();
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				Combate.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (porcPDV > 15) {
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					}
					if (!MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
						if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador)
								|| MotorIA.curaSiEsPosible(pelea, lanzador, true)
								|| MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo))
							continue;
						enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
						if (enemigo == null) {
							MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
							return;
						}
						if (MotorIA.acercarseA(pelea, lanzador, enemigo)
								|| MotorIA.invocarSiEsPosible1(pelea, lanzador))
							continue;
						stop = true;
						continue;
					}
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					}
					continue;
				}
				if (MotorIA.curaSiEsPosible(pelea, lanzador, true))
					continue;
				ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				}
				if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador)
						|| MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo)
						|| MotorIA.invocarSiEsPosible1(pelea, lanzador))
					continue;
				enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (MotorIA.acercarseA(pelea, lanzador, enemigo))
					continue;
				stop = true;
			}
		}

		private static void tipo_2(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					return;
				}
				int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				}
				stop = true;
			}
		}

		private static void tipo_3(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				}
				enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (MotorIA.acercarseA(pelea, lanzador, enemigo))
					continue;
				stop = true;
			}
		}

		private static void tipo_4(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				if (ataque == 0 && !stop) {
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
					}
				} else if (MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
					ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
					if (ataque == 0 && !stop) {
						while (ataque == 0 && !stop) {
							if (ataque == 5) {
								stop = true;
							}
							ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
						}
					}
				} else {
					enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
					if (enemigo == null) {
						MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
						return;
					}
					if (MotorIA.acercarseA(pelea, lanzador, enemigo)) {
						ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
						while (ataque == 0 && !stop) {
							if (ataque == 5) {
								stop = true;
							}
							ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
						}
					}
				}
				MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
				stop = true;
			}
		}

		private static void tipo_5(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (MotorIA.acercarseA(pelea, lanzador, enemigo))
					continue;
				stop = true;
			}
		}

		private static void tipo_6(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
				if (MotorIA.acercarseA(pelea, lanzador, amigo))
					continue;
				while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, amigo)) {
				}
				while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, lanzador)) {
				}
				stop = true;
			}
		}

		private static void tipo_7(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				}
				enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (MotorIA.acercarseA(pelea, lanzador, enemigo))
					continue;
				stop = true;
			}
		}

		private static void tipo_8(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
				if (amigo == null) {
					return;
				}
				if (MotorIA.acercarseA(pelea, lanzador, amigo))
					continue;
				while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, amigo)) {
				}
				stop = true;
			}
		}

		private static void tipo_9(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, lanzador)) {
				}
				stop = true;
			}
		}

		private static void tipo_10(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				}
				while (MotorIA.buffeaSiEsPosible2(pelea, lanzador, lanzador)) {
				}
				stop = true;
			}
		}

		private static void tipo_11(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				int ataque = MotorIA.atacaSiEsPosible3(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible3(pelea, lanzador);
				}
				while (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador)) {
				}
				enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (MotorIA.acercarseA(pelea, lanzador, enemigo))
					continue;
				stop = true;
			}
		}

		private static void tipo_12(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				int ataque = 0;
				if (MotorIA.invocarSiEsPosible2(pelea, lanzador))
					continue;
				if (!MotorIA.buffeaKralamar(pelea, lanzador, lanzador)) {
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					}
					stop = true;
					continue;
				}
				ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				}
				stop = true;
			}
		}

		private static void tipo_13(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				if (MotorIA.buffeaSiEsPosible2(pelea, lanzador, lanzador))
					continue;
				int ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible2(pelea, lanzador);
				}
				stop = true;
			}
		}

		private static void tipo_14(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				int ataque;
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				int porcPDV = lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff();
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				Combate.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (porcPDV > 15) {
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					}
					if (!MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
						if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador)
								|| MotorIA.curaSiEsPosible(pelea, lanzador, false)
								|| MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo))
							continue;
						enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
						if (enemigo == null) {
							MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
							return;
						}
						if (MotorIA.acercarseA(pelea, lanzador, enemigo))
							continue;
						stop = true;
						continue;
					}
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					}
					continue;
				}
				if (MotorIA.curaSiEsPosible(pelea, lanzador, true))
					continue;
				ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				}
				if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador)
						|| MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo))
					continue;
				enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (MotorIA.acercarseA(pelea, lanzador, enemigo))
					continue;
				stop = true;
			}
		}

		private static void tipo_15(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				int ataque;
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				int porcPDV = lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff();
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (porcPDV > 15) {
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					}
					if (!MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
						if (MotorIA.curaSiEsPosible(pelea, lanzador, false))
							continue;
						enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
						if (enemigo == null) {
							MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
							return;
						}
						if (MotorIA.acercarseA(pelea, lanzador, enemigo)
								|| MotorIA.invocarSiEsPosible1(pelea, lanzador))
							continue;
						stop = true;
						continue;
					}
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
					}
					continue;
				}
				if (MotorIA.curaSiEsPosible(pelea, lanzador, true))
					continue;
				ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				}
				if (MotorIA.invocarSiEsPosible1(pelea, lanzador))
					continue;
				enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				if (MotorIA.acercarseA(pelea, lanzador, enemigo))
					continue;
				stop = true;
			}
		}

		private static void tipo_16(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				Combate.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
				if (enemigo == null) {
					MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
					return;
				}
				int ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				}
				if (!MotorIA.moverYAtacarSiEsPosible(pelea, lanzador)) {
					if (MotorIA.buffeaSiEsPosible1(pelea, lanzador, lanzador)
							|| MotorIA.buffeaSiEsPosible1(pelea, lanzador, amigo))
						continue;
					enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
					if (enemigo == null) {
						MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
						return;
					}
					if (MotorIA.acercarseA(pelea, lanzador, enemigo) || MotorIA.invocarSiEsPosible1(pelea, lanzador))
						continue;
					stop = true;
					continue;
				}
				ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosible1(pelea, lanzador);
				}
			}
		}

		private static void tipo_Prisma(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				int ataque;
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				Combate.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
				if (amigo != null) {
					if (MotorIA.curaSiEsPosiblePrisma(pelea, lanzador, false)
							|| MotorIA.buffeaSiEsPosiblePrisma(pelea, lanzador, amigo)
							|| MotorIA.buffeaSiEsPosiblePrisma(pelea, lanzador, lanzador))
						continue;
					ataque = MotorIA.atacaSiEsPosiblePrisma(pelea, lanzador);
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosiblePrisma(pelea, lanzador);
					}
					stop = true;
					continue;
				}
				ataque = MotorIA.atacaSiEsPosiblePrisma(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosiblePrisma(pelea, lanzador);
				}
				stop = true;
			}
		}

		private static void tipo_Recaudador(Combate.Luchador lanzador, Combate pelea) {
			int veces = 0;
			while (!stop && lanzador.puedeJugar()) {
				int ataque;
				if (++veces >= 8) {
					stop = true;
				}
				if (veces > 15) {
					return;
				}
				int porcPDV = lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff();
				Combate.Luchador amigo = MotorIA.amigoMasCercano(pelea, lanzador);
				Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
				if (porcPDV > 15) {
					ataque = MotorIA.atacaSiEsPosibleRecau(pelea, lanzador);
					while (ataque == 0 && !stop) {
						if (ataque == 5) {
							stop = true;
						}
						ataque = MotorIA.atacaSiEsPosibleRecau(pelea, lanzador);
					}
					if (MotorIA.curaSiEsPosibleRecau(pelea, lanzador, false)
							|| MotorIA.buffeaSiEsPosibleRecau(pelea, lanzador, amigo))
						continue;
					enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
					if (enemigo == null) {
						MotorIA.mueveLoMasLejosPosible(pelea, lanzador);
						return;
					}
					if (MotorIA.acercarseA(pelea, lanzador, enemigo))
						continue;
					stop = true;
					continue;
				}
				if (MotorIA.curaSiEsPosibleRecau(pelea, lanzador, true))
					continue;
				ataque = MotorIA.atacaSiEsPosibleRecau(pelea, lanzador);
				while (ataque == 0 && !stop) {
					if (ataque == 5) {
						stop = true;
					}
					ataque = MotorIA.atacaSiEsPosibleRecau(pelea, lanzador);
				}
				if (MotorIA.mueveLoMasLejosPosible(pelea, lanzador))
					continue;
				stop = true;
			}
		}

		private static boolean mueveLoMasLejosPosible(Combate pelea, Luchador lanzador) {
			int[] movidas;
			if (lanzador.getTempPM(pelea) <= 0)
				return false;
			short celdaIDLanzador = lanzador.getCeldaPelea().getID();
			Mapa mapa = pelea.getMapaCopia();
			short[] dist = { 1000, 11000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000 }, celda = new short[10];
			for (int i = 0; i < 10; i++) {
				for (Luchador blanco : pelea.luchadoresDeEquipo(3)) {
					if (blanco.estaMuerto())
						continue;
					if (blanco == lanzador || blanco.getParamEquipoAliado() == lanzador.getParamEquipoAliado())
						continue;
					short celdaEnemigo = blanco.getCeldaPelea().getID();
					if (celdaEnemigo == celda[0] || celdaEnemigo == celda[1] || celdaEnemigo == celda[2]
							|| celdaEnemigo == celda[3] || celdaEnemigo == celda[4] || celdaEnemigo == celda[5]
							|| celdaEnemigo == celda[6] || celdaEnemigo == celda[7] || celdaEnemigo == celda[8]
							|| celdaEnemigo == celda[9])
						continue;
					short d = 0;
					d = Pathfinding.distanciaEntreDosCeldas(mapa, celdaIDLanzador, celdaEnemigo);
					if (d == 0)
						continue;
					if (d < dist[i]) {
						dist[i] = d;
						celda[i] = celdaEnemigo;
					}
					if (dist[i] == 1000) {
						dist[i] = 0;
						celda[i] = celdaIDLanzador;
					}
				}
			}
			if (dist[0] == 0)
				return false;
			int[] dist2 = new int[10];
			byte ancho = mapa.getAncho();
			int PM = lanzador.getTempPM(pelea);
			short celdaInicio = celdaIDLanzador;
			short celdaDestino = celdaIDLanzador;
			short ultCelda = mapa.ultimaCeldaID();
			Random rand = new Random();
			int valor = rand.nextInt(3);
			if (valor == 0) {
				movidas = new int[] { 0, 1, 2, 3 };
			} else if (valor == 1) {
				movidas = new int[] { 1, 2, 3 };
			} else if (valor == 1) {
				movidas = new int[] { 2, 3, 1 };
			} else {
				movidas = new int[] { 3, 1, 2 };
			}
			for (int j = 0; j <= PM; j++) {
				if (celdaDestino > 0)
					celdaInicio = celdaDestino;
				short celdaTemporal = celdaInicio;
				int infl = 0, inflF = 0;
				byte b;
				int k, arrayOfInt[];
				for (k = (arrayOfInt = movidas).length, b = 0; b < k;) {
					Integer x = Integer.valueOf(arrayOfInt[b]);
					switch (x.intValue()) {
					case 0:
						celdaTemporal = (short) (celdaTemporal + ancho);
						break;
					case 1:
						celdaTemporal = (short) (celdaInicio + ancho - 1);
						break;
					case 2:
						celdaTemporal = (short) (celdaInicio - ancho);
						break;
					case 3:
						celdaTemporal = (short) (celdaInicio - ancho - 1);
						break;
					}
					infl = 0;
					for (int m = 0; m < 10 && dist[m] != 0; m++) {
						dist2[m] = Pathfinding.distanciaEntreDosCeldas(mapa, celdaTemporal, celda[m]);
						if (dist2[m] > dist[m])
							infl++;
					}
					if (infl > inflF && celdaTemporal > 0 && celdaTemporal < ultCelda
							&& !mapa.celdaSalienteLateral(celdaDestino, celdaTemporal)
							&& mapa.getCelda(celdaTemporal).esCaminable(false)) {
						inflF = infl;
						celdaDestino = celdaTemporal;
					}
					b++;
				}
			}
			if (celdaDestino < 0 || celdaDestino > ultCelda || celdaDestino == celdaIDLanzador
					|| !mapa.getCelda(celdaDestino).esCaminable(false))
				return false;
			ArrayList<Celda> path = Pathfinding.pathMasCortoEntreDosCeldas(mapa, celdaIDLanzador, celdaDestino, 0);
			if (path == null)
				return false;
			ArrayList<Celda> finalPath = new ArrayList<Celda>();
			for (int a = 0; a < lanzador.getTempPM(pelea) && path.size() != a; a++)
				finalPath.add(path.get(a));
			String pathstr = "";
			try {
				short tempCeldaID = celdaIDLanzador;
				int tempDir = 0;
				for (Celda c : finalPath) {
					char d = Pathfinding.getDirEntreDosCeldas(tempCeldaID, c.getID(), mapa, true);
					if (d == '\000')
						return false;
					if (tempDir != d) {
						if (finalPath.indexOf(c) != 0)
							pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
						pathstr = String.valueOf(pathstr) + d;
					}
					tempCeldaID = c.getID();
				}
				if (tempCeldaID != celdaIDLanzador)
					pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			GameThread.AccionDeJuego GA = new GameThread.AccionDeJuego(0, 1, "");
			GA._args = pathstr;
			boolean resultado = pelea.puedeMoverseLuchador(lanzador, GA);
			return resultado;
		}

		private static boolean acercarseA(Combate pelea, Luchador lanzador, Luchador objetivo) {
			Mapa mapa = pelea.getMapaCopia();
			if (lanzador.getTempPM(pelea) <= 0)
				return false;
			if (objetivo == null)
				objetivo = enemigoMasCercano(pelea, lanzador);
			if (objetivo == null)
				return false;
			short celdaID = -1;
			try {
				if (Pathfinding.esSiguienteA(lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID(), mapa))
					return false;
				celdaID = Pathfinding.getCeldaMasCercanaAlrededor(mapa, objetivo.getCeldaPelea().getID(),
						lanzador.getCeldaPelea().getID(), null);
			} catch (NullPointerException e) {
				return false;
			}
			if (celdaID == -1) {
				ArrayList<Luchador> enemigos = listaEnemigosMenosPDV(pelea, lanzador);
				for (Luchador enemigo : enemigos) {
					short celdaID2 = Pathfinding.getCeldaMasCercanaAlrededor(mapa, enemigo.getCeldaPelea().getID(),
							lanzador.getCeldaPelea().getID(), null);
					if (celdaID2 != -1) {
						celdaID = celdaID2;
						break;
					}
				}
			}
			ArrayList<Celda> path = Pathfinding.pathMasCortoEntreDosCeldas(mapa, lanzador.getCeldaPelea().getID(),
					celdaID, 0);
			if (path == null || path.isEmpty())
				return false;
			ArrayList<Celda> finalPath = new ArrayList<Celda>();
			for (int a = 0; a < lanzador.getTempPM(pelea) && path.size() != a; a++)
				finalPath.add(path.get(a));
			String pathstr = "";
			try {
				short tempCeldaID = lanzador.getCeldaPelea().getID();
				int tempDir = 0;
				for (Celda c : finalPath) {
					char d = Pathfinding.getDirEntreDosCeldas(tempCeldaID, c.getID(), mapa, true);
					if (d == '\000')
						return false;
					if (tempDir != d) {
						if (finalPath.indexOf(c) != 0)
							pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
						pathstr = String.valueOf(pathstr) + d;
					}
					tempCeldaID = c.getID();
				}
				if (tempCeldaID != lanzador.getCeldaPelea().getID())
					pathstr = String.valueOf(pathstr) + CryptManager.celdaIDACodigo(tempCeldaID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			GameThread.AccionDeJuego GA = new GameThread.AccionDeJuego(0, 1, "");
			GA._args = pathstr;
			boolean resultado = pelea.puedeMoverseLuchador(lanzador, GA);
			return resultado;
		}

		private static boolean invocarSiEsPosible1(Combate pelea, Combate.Luchador invocador) {
			if (invocador.getNroInvocaciones() >= invocador.getTotalStatsConBuff().getEfecto(182)) {
				return false;
			}
			Combate.Luchador enemigoCercano = MotorIA.enemigoMasCercano(pelea, invocador);
			if (enemigoCercano == null) {
				return false;
			}
			short celdaMasCercana = Pathfinding.getCeldaMasCercanaAlrededor(pelea.getMapaCopia(),
					invocador.getCeldaPelea().getID(), enemigoCercano.getCeldaPelea().getID(), null);
			if (celdaMasCercana == -1) {
				return false;
			}
			Hechizo.StatsHechizos hechizo = MotorIA.hechizoInvocacion(pelea, invocador, celdaMasCercana);
			if (hechizo == null) {
				return false;
			}
			int invoc = pelea.intentarLanzarHechizo(invocador, hechizo, celdaMasCercana);
			return invoc == 0;
		}

		private static boolean invocarSiEsPosible2(Combate pelea, Combate.Luchador invocador) {
			if (invocador.getNroInvocaciones() >= invocador.getTotalStatsConBuff().getEfecto(182)) {
				return false;
			}
			Combate.Luchador enemigoCercano = MotorIA.enemigoMasCercano(pelea, invocador);
			if (enemigoCercano == null) {
				return false;
			}
			int invoc = MotorIA.hechizoInvocacion2(pelea, invocador, enemigoCercano);
			return invoc == 0;
		}

		private static Hechizo.StatsHechizos hechizoInvocacion(Combate pelea, Combate.Luchador invocador,
				short celdaCercana) {
			if (invocador.getMob() == null) {
				return null;
			}
			for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : invocador.getMob().getHechizos().entrySet()) {
				if (!pelea.puedeLanzarHechizo(invocador, SH.getValue(), pelea.getMapaCopia().getCelda(celdaCercana),
						(short) -1))
					continue;
				for (EfectoHechizo EH : SH.getValue().getEfectos()) {
					if (EH.getEfectoID() != 181 && EH.getEfectoID() != 185)
						continue;
					return SH.getValue();
				}
			}
			return null;
		}

		private static int hechizoInvocacion2(Combate pelea, Combate.Luchador invocador,
				Combate.Luchador enemigoCercano) {
			if (invocador.getMob() == null) {
				return 5;
			}
			ArrayList<Hechizo.StatsHechizos> hechizos = new ArrayList<Hechizo.StatsHechizos>();
			Hechizo.StatsHechizos SH = null;
			short celdaMasCercana = -1;
			try {
				for (Map.Entry<Integer, Hechizo.StatsHechizos> SS : invocador.getMob().getHechizos().entrySet()) {
					Hechizo.StatsHechizos hechi = SS.getValue();
					boolean paso = false;
					for (EfectoHechizo EH : hechi.getEfectos()) {
						if (paso || EH.getEfectoID() != 181 && EH.getEfectoID() != 185
								|| (celdaMasCercana = Pathfinding.getCeldaMasCercanaAlrededor2(pelea.getMapaCopia(),
										invocador.getCeldaPelea().getID(), enemigoCercano.getCeldaPelea().getID(),
										hechi.getMinAlc(), hechi.getMaxAlc())) == -1
								|| !pelea.puedeLanzarHechizo(invocador, hechi,
										pelea.getMapaCopia().getCelda(celdaMasCercana), (short) -1))
							continue;
						hechizos.add(hechi);
						paso = true;
					}
				}
			} catch (NullPointerException e) {
				return 5;
			}
			if (hechizos.size() <= 0) {
				return 5;
			}
			SH = hechizos.size() == 1 ? (Hechizo.StatsHechizos) hechizos.get(0)
					: (Hechizo.StatsHechizos) hechizos.get(Fórmulas.getRandomValor(0, hechizos.size() - 1));
			int invoca = pelea.intentarLanzarHechizo(invocador, SH, celdaMasCercana);
			return invoca;
		}

		private static boolean curaSiEsPosible(Combate pelea, Combate.Luchador lanzador, boolean autoCura) {
			if (autoCura && lanzador.getPDVConBuff() * 100 / lanzador.getPDVMaxConBuff() > 95) {
				return false;
			}
			Combate.Luchador objetivo = null;
			Hechizo.StatsHechizos SH = null;
			if (autoCura) {
				objetivo = lanzador;
				SH = MotorIA.mejorHechizoCuracion(pelea, lanzador, objetivo);
			} else {
				Combate.Luchador tempObjetivo = null;
				int porcPDVmin = 100;
				Hechizo.StatsHechizos tempSH = null;
				for (Combate.Luchador blanco : pelea.luchadoresDeEquipo(3)) {
					if (blanco.estaMuerto() || blanco == lanzador
							|| blanco.getParamEquipoAliado() != lanzador.getParamEquipoAliado())
						continue;
					int porcPDV = 0;
					int PDVMAX = blanco.getPDVMaxConBuff();
					porcPDV = PDVMAX == 0 ? 0 : blanco.getPDVConBuff() * 100 / PDVMAX;
					if (porcPDV >= porcPDVmin || porcPDV >= 95)
						continue;
					int infl = 0;
					for (Map.Entry<Integer, Hechizo.StatsHechizos> sh : lanzador.getMob().getHechizos().entrySet()) {
						int infCura = MotorIA.calculaInfluenciaCura(sh.getValue());
						if (infl >= infCura || infCura == 0 || !pelea.puedeLanzarHechizo(lanzador, sh.getValue(),
								blanco.getCeldaPelea(), (short) -1))
							continue;
						infl = infCura;
						tempSH = sh.getValue();
					}
					if (tempSH == SH || tempSH == null)
						continue;
					tempObjetivo = blanco;
					SH = tempSH;
					porcPDVmin = porcPDV;
				}
				objetivo = tempObjetivo;
			}
			if (objetivo == null) {
				return false;
			}
			if (SH == null) {
				return false;
			}
			int cura = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
			return cura == 0;
		}

		private static boolean curaSiEsPosiblePrisma(Combate pelea, Combate.Luchador prisma, boolean autoCura) {
			if (autoCura && prisma.getPDVConBuff() * 100 / prisma.getPDVMaxConBuff() > 95) {
				return false;
			}
			Combate.Luchador objetivo = null;
			Hechizo hechizo = Mundo.getHechizo(124);
			Hechizo.StatsHechizos SH = hechizo.getStatsPorNivel(6);
			if (autoCura) {
				objetivo = prisma;
			} else {
				Combate.Luchador curado = null;
				int porcPDVmin = 100;
				for (Combate.Luchador blanco : pelea.luchadoresDeEquipo(3)) {
					int porcPDV;
					if (blanco.estaMuerto() || blanco == prisma
							|| blanco.getParamEquipoAliado() != prisma.getParamEquipoAliado()
							|| (porcPDV = blanco.getPDVConBuff() * 100 / blanco.getPDVMaxConBuff()) >= porcPDVmin
							|| porcPDV >= 95)
						continue;
					curado = blanco;
					porcPDVmin = porcPDV;
				}
				objetivo = curado;
			}
			if (objetivo == null) {
				return false;
			}
			if (SH == null) {
				return false;
			}
			int cura = pelea.intentarLanzarHechizo(prisma, SH, objetivo.getCeldaPelea().getID());
			return cura == 0;
		}

		private static boolean curaSiEsPosibleRecau(Combate pelea, Combate.Luchador recaudador, boolean autoCura) {
			if (autoCura && recaudador.getPDVConBuff() * 100 / recaudador.getPDVMaxConBuff() > 95) {
				return false;
			}
			Combate.Luchador objetivo = null;
			Hechizo.StatsHechizos SH = null;
			if (autoCura) {
				objetivo = recaudador;
				SH = MotorIA.mejorHechizoCuracionRecaudador(pelea, recaudador, objetivo);
			} else {
				Combate.Luchador tempObjetivo = null;
				int porcPDVmin = 100;
				Hechizo.StatsHechizos tempSH = null;
				if (pelea.luchadoresDeEquipo(recaudador.getParamEquipoAliado()).size() <= 1) {
					return false;
				}
				for (Combate.Luchador blanco : pelea.luchadoresDeEquipo(3)) {
					int porcPDV;
					if (blanco.estaMuerto() || blanco == recaudador
							|| blanco.getParamEquipoAliado() != recaudador.getParamEquipoAliado()
							|| (porcPDV = blanco.getPDVConBuff() * 100 / blanco.getPDVMaxConBuff()) >= porcPDVmin
							|| porcPDV >= 95)
						continue;
					int infl = 0;
					for (Map.Entry<Integer, Hechizo.StatsHechizos> sh : Mundo
							.getGremio(recaudador.getRecau().getGremioID()).getHechizos().entrySet()) {
						int infCura;
						if (sh.getValue() == null || infl >= (infCura = MotorIA.calculaInfluenciaCura(sh.getValue()))
								|| infCura == 0 || !pelea.puedeLanzarHechizo(recaudador, sh.getValue(),
										blanco.getCeldaPelea(), (short) -1))
							continue;
						infl = infCura;
						tempSH = sh.getValue();
					}
					if (tempSH == SH || tempSH == null)
						continue;
					tempObjetivo = blanco;
					SH = tempSH;
					porcPDVmin = porcPDV;
				}
				objetivo = tempObjetivo;
			}
			if (objetivo == null) {
				return false;
			}
			if (SH == null) {
				return false;
			}
			int cura = pelea.intentarLanzarHechizo(recaudador, SH, objetivo.getCeldaPelea().getID());
			return cura == 0;
		}

		private static boolean buffeaSiEsPosible1(Combate pelea, Combate.Luchador lanzador, Combate.Luchador objetivo) {
			Hechizo.StatsHechizos SH;
			block4: {
				if (objetivo == null) {
					return false;
				}
				try {
					SH = MotorIA.mejorBuff1(pelea, lanzador, objetivo);
					if (SH != null)
						break block4;
					return false;
				} catch (NullPointerException e) {
					return false;
				}
			}
			int buff = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
			return buff == 0;
		}

		private static boolean buffeaSiEsPosible2(Combate pelea, Combate.Luchador lanzador, Combate.Luchador objetivo) {
			Hechizo.StatsHechizos SH;
			block4: {
				if (objetivo == null) {
					return false;
				}
				try {
					SH = MotorIA.mejorBuff2(pelea, lanzador, objetivo);
					if (SH != null)
						break block4;
					return false;
				} catch (NullPointerException e) {
					return false;
				}
			}
			int buff = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
			return buff == 0;
		}

		private static boolean buffeaKralamar(Combate pelea, Combate.Luchador lanzador, Combate.Luchador objetivo) {
			if (objetivo == null) {
				return false;
			}
			Hechizo hechizo = Mundo.getHechizo(1106);
			Hechizo.StatsHechizos SH = hechizo.getStatsPorNivel(1);
			if (SH == null) {
				return false;
			}
			int buff = 5;
			try {
				buff = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
			} catch (NullPointerException e) {
				return false;
			}
			return buff == 0;
		}

		private static boolean buffeaSiEsPosiblePrisma(Combate pelea, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			if (objetivo == null) {
				return false;
			}
			Hechizo.StatsHechizos SH = MotorIA.mejorBuffPrisma(pelea, lanzador);
			if (SH == null) {
				return false;
			}
			int buff = 5;
			try {
				buff = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
			} catch (NullPointerException e) {
				return false;
			}
			return buff == 0;
		}

		private static boolean buffeaSiEsPosibleRecau(Combate pelea, Combate.Luchador recaudador,
				Combate.Luchador objetivo) {
			Hechizo.StatsHechizos SH;
			block4: {
				if (objetivo == null) {
					return false;
				}
				try {
					SH = MotorIA.mejorBuffRecaudador(pelea, recaudador, objetivo);
					if (SH != null)
						break block4;
					return false;
				} catch (NullPointerException e) {
					return false;
				}
			}
			int buff = pelea.intentarLanzarHechizo(recaudador, SH, objetivo.getCeldaPelea().getID());
			return buff == 0;
		}

		private static Hechizo.StatsHechizos mejorBuffPrisma(Combate pelea, Combate.Luchador lanzador) {
			Hechizo hechizo = Mundo.getHechizo(153);
			Hechizo.StatsHechizos hechizoStats = hechizo.getStatsPorNivel(6);
			return hechizoStats;
		}

		private static Hechizo.StatsHechizos mejorBuff1(Combate pelea, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			int infl = 0;
			Hechizo.StatsHechizos sh = null;
			if (objetivo == null) {
				return null;
			}
			try {
				for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
					int infDa\u00f1o = MotorIA.calculaInfluenciaDa\u00f1o(SH.getValue(), lanzador, objetivo);
					if (infl >= infDa\u00f1o || infDa\u00f1o <= 0
							|| !pelea.puedeLanzarHechizo(lanzador, SH.getValue(), objetivo.getCeldaPelea(), (short) -1))
						continue;
					infl = infDa\u00f1o;
					sh = SH.getValue();
				}
			} catch (NullPointerException e) {
				return null;
			}
			return sh;
		}

		private static Hechizo.StatsHechizos mejorBuff2(Combate pelea, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			Hechizo.StatsHechizos sh;
			ArrayList<Hechizo.StatsHechizos> hechizos;
			block6: {
				hechizos = new ArrayList<Hechizo.StatsHechizos>();
				sh = null;
				if (objetivo == null) {
					return null;
				}
				try {
					Mapa.Celda celdaObj = objetivo.getCeldaPelea();
					for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
						if (!pelea.puedeLanzarHechizo(lanzador, SH.getValue(), celdaObj, (short) -1))
							continue;
						hechizos.add(SH.getValue());
					}
					if (hechizos.size() > 0)
						break block6;
					return null;
				} catch (NullPointerException e) {
					return null;
				}
			}
			if (hechizos.size() == 1) {
				return (Hechizo.StatsHechizos) hechizos.get(0);
			}
			sh = (Hechizo.StatsHechizos) hechizos.get(Fórmulas.getRandomValor(0, hechizos.size() - 1));
			return sh;
		}

		private static Hechizo.StatsHechizos mejorBuffRecaudador(Combate pelea, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			int infl = 0;
			Hechizo.StatsHechizos sh = null;
			if (objetivo == null) {
				return null;
			}
			try {
				for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : Mundo.getGremio(lanzador.getRecau().getGremioID())
						.getHechizos().entrySet()) {
					int infDa\u00f1os;
					if (SH.getValue() == null
							|| infl >= (infDa\u00f1os = MotorIA.calculaInfluenciaDa\u00f1o(SH.getValue(), lanzador,
									objetivo))
							|| infDa\u00f1os <= 0
							|| !pelea.puedeLanzarHechizo(lanzador, SH.getValue(), objetivo.getCeldaPelea(), (short) -1))
						continue;
					infl = infDa\u00f1os;
					sh = SH.getValue();
				}
			} catch (NullPointerException e) {
				return null;
			}
			return sh;
		}

		private static Hechizo.StatsHechizos mejorHechizoCuracion(Combate pelea, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			int infl = 0;
			Hechizo.StatsHechizos sh = null;
			if (objetivo == null) {
				return null;
			}
			try {
				for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
					int infCura = MotorIA.calculaInfluenciaCura(SH.getValue());
					if (infl >= infCura || infCura == 0
							|| !pelea.puedeLanzarHechizo(lanzador, SH.getValue(), objetivo.getCeldaPelea(), (short) -1))
						continue;
					infl = infCura;
					sh = SH.getValue();
				}
			} catch (NullPointerException e) {
				return null;
			}
			return sh;
		}

		private static Hechizo.StatsHechizos mejorHechizoCuracionRecaudador(Combate pelea, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			int infl = 0;
			Hechizo.StatsHechizos sh = null;
			if (objetivo == null) {
				return null;
			}
			try {
				for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : Mundo.getGremio(lanzador.getRecau().getGremioID())
						.getHechizos().entrySet()) {
					int infCura;
					if (SH.getValue() == null || infl >= (infCura = MotorIA.calculaInfluenciaCura(SH.getValue()))
							|| infCura == 0
							|| !pelea.puedeLanzarHechizo(lanzador, SH.getValue(), objetivo.getCeldaPelea(), (short) -1))
						continue;
					infl = infCura;
					sh = SH.getValue();
				}
			} catch (NullPointerException e) {
				return null;
			}
			return sh;
		}

		private static Combate.Luchador amigoMasCercano(Combate pelea, Combate.Luchador lanzador) {
			short dist = 1000;
			Combate.Luchador tempObjetivo = null;
			for (Combate.Luchador objetivo : pelea.luchadoresDeEquipo(lanzador.getParamEquipoAliado())) {
				short d;
				if (objetivo.estaMuerto() || objetivo == lanzador
						|| (d = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(),
								lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID())) >= dist)
					continue;
				dist = d;
				tempObjetivo = objetivo;
			}
			return tempObjetivo;
		}

		private static Combate.Luchador enemigoMasCercano(Combate pelea, Combate.Luchador lanzador) {
			short dist = 1000;
			Combate.Luchador tempObjetivo = null;
			for (Combate.Luchador objetivo : pelea.luchadoresDeEquipo(lanzador.getParamEquipoEnemigo())) {
				short d;
				if (objetivo.estaMuerto() || objetivo.esInvisible()
						|| (d = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(),
								lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID())) >= dist)
					continue;
				dist = d;
				tempObjetivo = objetivo;
			}
			return tempObjetivo;
		}

		private static Combate.Luchador luchadorMasCercano(Combate pelea, Combate.Luchador lanzador) {
			short dist = 1000;
			Combate.Luchador tempObjetivo = null;
			for (Combate.Luchador objetivo : pelea.luchadoresDeEquipo(3)) {
				short d;
				if (objetivo.estaMuerto() || objetivo == lanzador
						|| (d = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(),
								lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID())) >= dist)
					continue;
				dist = d;
				tempObjetivo = objetivo;
			}
			return tempObjetivo;
		}

		private static ArrayList<Combate.Luchador> listaTodoEnemigos(Combate pelea, Combate.Luchador lanzador) {
			ArrayList<Combate.Luchador> listaEnemigos = new ArrayList<Combate.Luchador>();
			ArrayList<Combate.Luchador> enemigosNoInvo = new ArrayList<Combate.Luchador>();
			ArrayList<Combate.Luchador> enemigosInvo = new ArrayList<Combate.Luchador>();
			for (Combate.Luchador objetivo : pelea.luchadoresDeEquipo(lanzador.getParamEquipoEnemigo())) {
				if (objetivo.estaMuerto() || objetivo.esInvisible())
					continue;
				if (objetivo.esInvocacion()) {
					enemigosInvo.add(objetivo);
					continue;
				}
				enemigosNoInvo.add(objetivo);
			}
			Random rand = new Random();
			if (rand.nextBoolean()) {
				listaEnemigos.addAll(enemigosInvo);
				listaEnemigos.addAll(enemigosNoInvo);
			} else {
				listaEnemigos.addAll(enemigosNoInvo);
				listaEnemigos.addAll(enemigosInvo);
			}
			return listaEnemigos;
		}

		private static ArrayList<Luchador> listaEnemigosMenosPDV(Combate pelea, Luchador lanzador) {
			ArrayList<Luchador> listaEnemigos = new ArrayList<Luchador>();
			ArrayList<Luchador> enemigosNoInvo = new ArrayList<Luchador>();
			ArrayList<Luchador> enemigosInvo = new ArrayList<Luchador>();
			for (Luchador objetivo : pelea.luchadoresDeEquipo(lanzador.getParamEquipoEnemigo())) {
				if (objetivo.estaMuerto() || objetivo.esInvisible())
					continue;
				if (objetivo.esInvocacion())
					enemigosInvo.add(objetivo);
				else
					enemigosNoInvo.add(objetivo);
			}
			int i = 0;
			int tempPDV;
			Random rand = new Random();
			if (rand.nextBoolean()) {
				try {
					int i3 = enemigosNoInvo.size(), i2 = enemigosInvo.size();
					while (i < i2) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosInvo.remove(index);
						i++;
					}
					i = 0;
					while (i < i3) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosNoInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosNoInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosNoInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosNoInvo.remove(index);
						i++;
					}
				} catch (NullPointerException e) {
					return listaEnemigos;
				}
			} else
				try {
					int i2 = enemigosNoInvo.size(), i3 = enemigosInvo.size();
					while (i < i2) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosNoInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosNoInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosNoInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosNoInvo.remove(index);
						i++;
					}
					i = 0;
					while (i < i3) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosInvo.remove(index);
						i++;
					}
				} catch (NullPointerException e) {
					return listaEnemigos;
				}
			return listaEnemigos;
		}

		private static ArrayList<Luchador> listaTodoLuchadores(Combate pelea, Luchador lanzador) {
			Luchador enemigoMasCercano = luchadorMasCercano(pelea, lanzador);
			ArrayList<Luchador> listaEnemigos = new ArrayList<Luchador>();
			ArrayList<Luchador> enemigosNoInvo = new ArrayList<Luchador>();
			ArrayList<Luchador> enemigosInvo = new ArrayList<Luchador>();
			for (Luchador objetivo : pelea.luchadoresDeEquipo(3)) {
				if (objetivo.estaMuerto())
					continue;
				if (objetivo.esInvocacion())
					enemigosInvo.add(objetivo);
				else
					enemigosNoInvo.add(objetivo);
			}
			if (enemigoMasCercano != null)
				listaEnemigos.add(enemigoMasCercano);
			int i = 0;
			int tempPDV;
			Random rand = new Random();
			if (rand.nextBoolean()) {
				try {
					int i3 = enemigosNoInvo.size(), i2 = enemigosInvo.size();
					while (i < i2) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosInvo.remove(index);
						i++;
					}
					i = 0;
					while (i < i3) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosNoInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosNoInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosNoInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosNoInvo.remove(index);
						i++;
					}
				} catch (NullPointerException e) {
					return listaEnemigos;
				}
			} else
				try {
					int i2 = enemigosNoInvo.size(), i3 = enemigosInvo.size();
					while (i < i2) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosNoInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosNoInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosNoInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosNoInvo.remove(index);
						i++;
					}
					i = 0;
					while (i < i3) {
						tempPDV = 200000;
						int index = 0;
						for (Luchador invo : enemigosInvo) {
							if (invo.getPDVConBuff() <= tempPDV) {
								tempPDV = invo.getPDVConBuff();
								index = enemigosInvo.indexOf(invo);
							}
						}
						Luchador test = enemigosInvo.get(index);
						if (test != null)
							listaEnemigos.add(test);
						enemigosInvo.remove(index);
						i++;
					}
				} catch (NullPointerException e) {
				}
			return listaEnemigos;
		}

		private static int atacaSiEsPosibleRecau(Combate pelea, Combate.Luchador recaudador) {
			ArrayList<Combate.Luchador> listaEnemigos = MotorIA.objetivosMasCercanos(pelea, recaudador);
			Hechizo.StatsHechizos SH = null;
			Combate.Luchador objetivo = null;
			for (Combate.Luchador blanco : listaEnemigos) {
				SH = MotorIA.mejorHechizoRecau(pelea, recaudador, blanco);
				if (SH == null)
					continue;
				objetivo = blanco;
				break;
			}
			if (objetivo == null || SH == null) {
				return 666;
			}
			int attack = pelea.intentarLanzarHechizo(recaudador, SH, objetivo.getCeldaPelea().getID());
			if (attack != 0) {
				return attack;
			}
			return 0;
		}

		private static int atacaSiEsPosiblePrisma(Combate pelea, Combate.Luchador lanzador) {
			ArrayList<Combate.Luchador> listaEnemigos = MotorIA.listaEnemigosMenosPDV(pelea, lanzador);
			Hechizo.StatsHechizos SH = null;
			Combate.Luchador objetivo = null;
			for (Combate.Luchador blanco : listaEnemigos) {
				SH = MotorIA.mejorHechizoPrisma(pelea, lanzador, blanco);
				if (SH == null)
					continue;
				objetivo = blanco;
				break;
			}
			if (objetivo == null || SH == null) {
				return 666;
			}
			int ataque = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
			if (ataque != 0) {
				return ataque;
			}
			return 0;
		}

		private static int atacaSiEsPosible1(Combate pelea, Combate.Luchador lanzador) {
			ArrayList<Combate.Luchador> listaEnemigos = MotorIA.objetivosMasCercanos(pelea, lanzador);
			Hechizo.StatsHechizos SH = null;
			Combate.Luchador objetivo = null;
			for (Combate.Luchador blanco : listaEnemigos) {
				SH = MotorIA.mejorHechizo1(pelea, lanzador, blanco);
				if (SH == null)
					continue;
				objetivo = blanco;
				break;
			}
			if (objetivo == null || SH == null) {
				return 666;
			}
			int ataque = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
			if (ataque != 0) {
				return ataque;
			}
			return 0;
		}

		private static int atacaSiEsPosible2(Combate pelea, Combate.Luchador lanzador) {
			ArrayList<Combate.Luchador> listaEnemigos = MotorIA.objetivosMasCercanos(pelea, lanzador);
			Hechizo.StatsHechizos SH = null;
			Combate.Luchador objetivo = null;
			for (Combate.Luchador blanco : listaEnemigos) {
				SH = MotorIA.mejorHechizo2(pelea, lanzador, blanco);
				if (SH == null)
					continue;
				objetivo = blanco;
				break;
			}
			if (objetivo == null || SH == null) {
				return 666;
			}
			int ataque = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
			if (ataque != 0) {
				return ataque;
			}
			return 0;
		}

		private static int atacaSiEsPosible3(Combate pelea, Combate.Luchador lanzador) {
			ArrayList<Combate.Luchador> listaEnemigos = MotorIA.listaTodoLuchadores(pelea, lanzador);
			Hechizo.StatsHechizos SH = null;
			Combate.Luchador objetivo = null;
			for (Combate.Luchador blanco : listaEnemigos) {
				SH = MotorIA.mejorHechizo2(pelea, lanzador, blanco);
				if (SH == null)
					continue;
				objetivo = blanco;
				break;
			}
			if (objetivo == null || SH == null) {
				return 666;
			}
			int ataque = pelea.intentarLanzarHechizo(lanzador, SH, objetivo.getCeldaPelea().getID());
			if (ataque != 0) {
				return ataque;
			}
			return 0;
		}

		/*
		 * Enabled aggressive block sorting Enabled unnecessary exception pruning
		 * Enabled aggressive exception aggregation
		 */
		private static boolean moverYAtacarSiEsPosible(Combate pelea, Combate.Luchador lanzador) {
			ArrayList<Short> celdas = Pathfinding.listaCeldasDesdeLuchador(pelea, lanzador);
			if (celdas == null) {
				return false;
			}
			Combate.Luchador enemigo = MotorIA.enemigoMasCercano(pelea, lanzador);
			if (enemigo == null) {
				return false;
			}
			short distMin = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(),
					enemigo.getCeldaPelea().getID());
			ArrayList<Hechizo.StatsHechizos> hechizos = MotorIA.hechizosLanzables(lanzador, pelea, distMin);
			if (hechizos == null || hechizos.isEmpty()) {
				return false;
			}
			Hechizo.StatsHechizos hechizo = hechizos.size() == 1 ? hechizos.get(0)
					: hechizos.get(Fórmulas.getRandomValor(0, hechizos.size() - 1));
			ArrayList<Combate.Luchador> objetivos = MotorIA.objetivosMasCercanosAlHechizo(pelea, lanzador, hechizo);
			if (objetivos == null) {
				return false;
			}
			short celdaDestino = 0;
			Combate.Luchador objetivo = null;
			boolean encontrado = false;
			for (short celda : celdas) {
				for (Combate.Luchador O : objetivos) {
					if (pelea.puedeLanzarHechizo(lanzador, hechizo, O.getCeldaPelea(), celda)) {
						celdaDestino = celda;
						objetivo = O;
						encontrado = true;
					}
					if (encontrado)
						break;
				}
				if (encontrado)
					break;
			}
			if (celdaDestino == 0) {
				return false;
			}
			ArrayList<Mapa.Celda> path = Pathfinding.pathMasCortoEntreDosCeldas(pelea.getMapaCopia(),
					lanzador.getCeldaPelea().getID(), celdaDestino, 0);
			if (path == null) {
				return false;
			}
			String pathStr = "";
			try {
				short tempCeldaID = lanzador.getCeldaPelea().getID();
				char tempDir = '\u0000';
				Iterator<Mapa.Celda> iterator = path.iterator();
				do {
					if (!iterator.hasNext()) {
						if (tempCeldaID != lanzador.getCeldaPelea().getID()) {
							pathStr = String.valueOf(pathStr) + CryptManager.celdaIDACodigo(tempCeldaID);
						}
						break;
					}
					Mapa.Celda c = iterator.next();
					char dir = Pathfinding.getDirEntreDosCeldas(tempCeldaID, c.getID(), pelea.getMapaCopia(), true);
					if (dir == '\u0000') {
						return false;
					}
					if (tempDir != dir) {
						if (path.indexOf(c) != 0) {
							pathStr = String.valueOf(pathStr) + CryptManager.celdaIDACodigo(tempCeldaID);
						}
						pathStr = String.valueOf(pathStr) + dir;
					}
					tempCeldaID = c.getID();
				} while (true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			GameThread.AccionDeJuego GA = new GameThread.AccionDeJuego(0, 1, "");
			GA._args = pathStr;
			boolean resultado = pelea.puedeMoverseLuchador(lanzador, GA);
			if (resultado && objetivo != null && hechizo != null) {
				pelea.intentarLanzarHechizo(lanzador, hechizo, objetivo.getCeldaPelea().getID());
			}
			return resultado;
		}

		private static ArrayList<Hechizo.StatsHechizos> hechizosLanzables(Combate.Luchador lanzador, Combate pelea,
				int distMin) {
			ArrayList<Hechizo.StatsHechizos> hechizos = new ArrayList<Hechizo.StatsHechizos>();
			if (lanzador.getMob() == null) {
				return null;
			}
			for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
				Hechizo.StatsHechizos hechizo = SH.getValue();
				if (hechizo.getCostePA() > lanzador.getTempPA(pelea)
						|| !Combate.HechizoLanzado.poderSigLanzamiento(lanzador, hechizo.getHechizoID())
						|| hechizo.getMaxLanzPorTurno()
								- Combate.HechizoLanzado.getNroLanzamientos(lanzador, hechizo.getHechizoID()) <= 0
								&& hechizo.getMaxLanzPorTurno() > 0
						|| MotorIA.calculaInfluenciaDa\u00f1o(hechizo, lanzador, lanzador) >= 0)
					continue;
				hechizos.add(hechizo);
			}
			ArrayList<Hechizo.StatsHechizos> hechizosFinales = MotorIA.hechizosMasAMenosDaño(lanzador, hechizos);
			return hechizosFinales;
		}

		private static ArrayList<StatsHechizos> hechizosMasAMenosDaño(Luchador lanzador,
				ArrayList<StatsHechizos> hechizos) {
			if (hechizos == null)
				return null;
			ArrayList<StatsHechizos> hechizosFinales = new ArrayList<StatsHechizos>();
			Map<Integer, StatsHechizos> copia = new TreeMap<Integer, StatsHechizos>();
			for (StatsHechizos SH : hechizos)
				copia.put(Integer.valueOf(SH.getHechizoID()), SH);
			int tempInfluencia = 0;
			int tempID = 0;
			while (copia.size() > 0) {
				tempInfluencia = 0;
				tempID = 0;
				for (Map.Entry<Integer, StatsHechizos> SH : copia.entrySet()) {
					int influencia = -calculaInfluenciaDaño((StatsHechizos) SH.getValue(), lanzador, lanzador);
					if (influencia > tempInfluencia) {
						tempID = ((StatsHechizos) SH.getValue()).getHechizoID();
						tempInfluencia = influencia;
					}
				}
				if (tempID == 0 || tempInfluencia == 0)
					break;
				hechizosFinales.add(copia.get(Integer.valueOf(tempID)));
				copia.remove(Integer.valueOf(tempID));
			}
			return hechizosFinales;
		}

		private static ArrayList<Luchador> objetivosMasCercanosAlHechizo(Combate pelea, Luchador lanzador,
				StatsHechizos hechizo) {
			ArrayList<Luchador> objetivos = new ArrayList<Luchador>();
			ArrayList<Luchador> objetivos1 = new ArrayList<Luchador>();
			int distMax = hechizo.getMaxAlc();
			distMax += lanzador.getTempPM(pelea);
			ArrayList<Luchador> objetivosP = listaTodoEnemigos(pelea, lanzador);
			for (Luchador entry : objetivosP) {
				Luchador objetivo = entry;
				int dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(), lanzador.getCeldaPelea().getID(),
						objetivo.getCeldaPelea().getID());
				if (dist < distMax)
					objetivos.add(objetivo);
			}
			while (objetivos.size() > 0) {
				int index = 0;
				int dista = 1000;
				for (Luchador objetivo : objetivos) {
					int dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(),
							lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID());
					if (dist < dista) {
						dista = dist;
						index = objetivos.indexOf(objetivo);
					}
				}
				objetivos1.add(objetivos.get(index));
				objetivos.remove(index);
			}
			return objetivos1;
		}

		private static ArrayList<Luchador> objetivosMasCercanos(Combate pelea, Luchador lanzador) {
			ArrayList<Luchador> objetivos = new ArrayList<Luchador>();
			ArrayList<Luchador> objetivos1 = listaTodoEnemigos(pelea, lanzador);
			while (objetivos.size() > 0) {
				int index = 0;
				int dista = 1000;
				for (Luchador objetivo : objetivos) {
					int dist = Pathfinding.distanciaEntreDosCeldas(pelea.getMapaCopia(),
							lanzador.getCeldaPelea().getID(), objetivo.getCeldaPelea().getID());
					if (dist < dista) {
						dista = dist;
						index = objetivos.indexOf(objetivo);
					}
				}
				objetivos1.add(objetivos.get(index));
				objetivos.remove(index);
			}
			return objetivos1;
		}

		private static Hechizo.StatsHechizos mejorHechizoRecau(Combate pelea, Combate.Luchador recaudador,
				Combate.Luchador objetivo) {
			int influenciaMax = 0;
			Hechizo.StatsHechizos sh = null;
			Map<Integer, Hechizo.StatsHechizos> hechiRecau = Mundo.getGremio(recaudador.getRecau().getGremioID())
					.getHechizos();
			if (objetivo == null) {
				return null;
			}
			for (Map.Entry<Integer, Hechizo.StatsHechizos> SH1 : hechiRecau.entrySet()) {
				Hechizo.StatsHechizos hechizo1 = SH1.getValue();
				if (hechizo1 == null)
					continue;
				int tempInfluencia = 0;
				int influencia1 = 0;
				int influencia2 = 0;
				int PA = 6;
				int[] costePA = new int[2];
				if (!pelea.puedeLanzarHechizo(recaudador, hechizo1, objetivo.getCeldaPelea(), (short) -1)
						|| (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo1, recaudador, objetivo)) == 0)
					continue;
				if (tempInfluencia > influenciaMax) {
					sh = hechizo1;
					costePA[0] = sh.getCostePA();
					influenciaMax = influencia1 = tempInfluencia;
				}
				for (Map.Entry<Integer, Hechizo.StatsHechizos> SH2 : hechiRecau.entrySet()) {
					Hechizo.StatsHechizos hechizo2 = SH2.getValue();
					if (hechizo2 == null || PA - costePA[0] < hechizo2.getCostePA()
							|| !pelea.puedeLanzarHechizo(recaudador, hechizo2, objetivo.getCeldaPelea(), (short) -1)
							|| (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo2, recaudador,
									objetivo)) == 0)
						continue;
					if (influencia1 + tempInfluencia > influenciaMax) {
						sh = hechizo2;
						costePA[1] = hechizo2.getCostePA();
						influencia2 = tempInfluencia;
						influenciaMax = influencia1 + influencia2;
					}
					for (Map.Entry<Integer, Hechizo.StatsHechizos> SH3 : hechiRecau.entrySet()) {
						Hechizo.StatsHechizos hechizo3 = SH3.getValue();
						if (hechizo3 == null || PA - costePA[0] - costePA[1] < hechizo3.getCostePA()
								|| !pelea.puedeLanzarHechizo(recaudador, hechizo3, objetivo.getCeldaPelea(), (short) -1)
								|| (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo3, recaudador,
										objetivo)) == 0
								|| tempInfluencia + influencia1 + influencia2 <= influenciaMax)
							continue;
						sh = hechizo3;
						influenciaMax = tempInfluencia + influencia1 + influencia2;
					}
				}
			}
			return sh;
		}

		private static Hechizo.StatsHechizos mejorHechizoPrisma(Combate pelea, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			Hechizo.StatsHechizos sh = null;
			ArrayList<Hechizo.StatsHechizos> posibles = new ArrayList<Hechizo.StatsHechizos>();
			if (objetivo == null) {
				return null;
			}
			try {
				for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : lanzador.getPrisma().getHechizos().entrySet()) {
					Hechizo.StatsHechizos statsH = SH.getValue();
					if (!pelea.puedeLanzarHechizo(lanzador, statsH, objetivo.getCeldaPelea(), (short) -1))
						continue;
					posibles.add(statsH);
				}
			} catch (NullPointerException e) {
				return null;
			}
			if (posibles.isEmpty()) {
				return sh;
			}
			if (posibles.size() == 1) {
				return (Hechizo.StatsHechizos) posibles.get(0);
			}
			sh = (Hechizo.StatsHechizos) posibles.get(Fórmulas.getRandomValor(0, posibles.size() - 1));
			return sh;
		}

		private static Hechizo.StatsHechizos mejorHechizo1(Combate pelea, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			int influenciaMax = 0;
			Hechizo.StatsHechizos sh = null;
			Map<Integer, Hechizo.StatsHechizos> hechiMob = lanzador.getMob().getHechizos();
			if (objetivo == null) {
				return null;
			}
			for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : hechiMob.entrySet()) {
				int tempInfluencia = 0;
				int influencia1 = 0;
				int influencia2 = 0;
				int PA = lanzador.getTempPA(pelea);
				int[] costePA = new int[2];
				Hechizo.StatsHechizos hechizo1 = SH.getValue();
				if (!pelea.puedeLanzarHechizo(lanzador, hechizo1, objetivo.getCeldaPelea(), (short) -1)
						|| (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo1, lanzador, objetivo)) == 0)
					continue;
				if (tempInfluencia > influenciaMax) {
					sh = hechizo1;
					costePA[0] = sh.getCostePA();
					influenciaMax = influencia1 = tempInfluencia;
				}
				for (Map.Entry<Integer, Hechizo.StatsHechizos> SH2 : hechiMob.entrySet()) {
					Hechizo.StatsHechizos hechizo2 = SH2.getValue();
					if (PA - costePA[0] < hechizo2.getCostePA()
							|| !pelea.puedeLanzarHechizo(lanzador, hechizo2, objetivo.getCeldaPelea(), (short) -1)
							|| (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo2, lanzador, objetivo)) == 0)
						continue;
					if (influencia1 + tempInfluencia > influenciaMax) {
						sh = hechizo2;
						costePA[1] = hechizo2.getCostePA();
						influencia2 = tempInfluencia;
						influenciaMax = influencia1 + influencia2;
					}
					for (Map.Entry<Integer, Hechizo.StatsHechizos> SH3 : hechiMob.entrySet()) {
						Hechizo.StatsHechizos hechizo3 = SH3.getValue();
						if (PA - costePA[0] - costePA[1] < hechizo3.getCostePA()
								|| !pelea.puedeLanzarHechizo(lanzador, hechizo3, objetivo.getCeldaPelea(), (short) -1)
								|| (tempInfluencia = MotorIA.calculaInfluenciaDa\u00f1o(hechizo3, lanzador,
										objetivo)) == 0
								|| tempInfluencia + influencia1 + influencia2 <= influenciaMax)
							continue;
						sh = hechizo3;
						influenciaMax = tempInfluencia + influencia1 + influencia2;
					}
				}
			}
			return sh;
		}

		private static Hechizo.StatsHechizos mejorHechizo2(Combate pelea, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			Hechizo.StatsHechizos sh = null;
			ArrayList<Hechizo.StatsHechizos> posibles = new ArrayList<Hechizo.StatsHechizos>();
			if (objetivo == null) {
				return null;
			}
			try {
				for (Map.Entry<Integer, Hechizo.StatsHechizos> SH : lanzador.getMob().getHechizos().entrySet()) {
					Hechizo.StatsHechizos hechizo = SH.getValue();
					if (!pelea.puedeLanzarHechizo(lanzador, hechizo, objetivo.getCeldaPelea(), (short) -1))
						continue;
					posibles.add(hechizo);
				}
			} catch (NullPointerException e) {
				return null;
			}
			if (posibles.isEmpty()) {
				return sh;
			}
			if (posibles.size() == 1) {
				return (Hechizo.StatsHechizos) posibles.get(0);
			}
			sh = (Hechizo.StatsHechizos) posibles.get(Fórmulas.getRandomValor(0, posibles.size() - 1));
			return sh;
		}

		private static int calculaInfluenciaCura(Hechizo.StatsHechizos SH) {
			int inf = 0;
			for (EfectoHechizo SE : SH.getEfectos()) {
				int efectoID = SE.getEfectoID();
				if (efectoID != 108 && efectoID != 81)
					continue;
				inf += 100 * Fórmulas.getMaxValor(SE.getValores());
			}
			return inf;
		}

		private static int calculaInfluenciaDa\u00f1o(Hechizo.StatsHechizos SH, Combate.Luchador lanzador,
				Combate.Luchador objetivo) {
			int influenciaTotal = 0;
			for (EfectoHechizo SE : SH.getEfectos()) {
				int inf = 0;
				switch (SE.getEfectoID()) {
				case 5: {
					inf = 500 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 77: {
					inf = 1500 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 84: {
					inf = 1500 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 89: {
					inf = 200 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 91: {
					inf = 150 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 92: {
					inf = 150 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 93: {
					inf = 150 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 94: {
					inf = 150 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 95: {
					inf = 150 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 96: {
					inf = 100 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 97: {
					inf = 100 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 98: {
					inf = 100 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 99: {
					inf = 100 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 100: {
					inf = 100 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 101: {
					inf = 1000 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 111: {
					inf = -1000 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 117: {
					inf = -500 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 121: {
					inf = -100 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 122: {
					inf = 200 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 123: {
					inf = -200 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 124: {
					inf = -200 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 125: {
					inf = -200 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 126: {
					inf = -200 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 127: {
					inf = 1000 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 128: {
					inf = -1000 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 131: {
					inf = 300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 132: {
					inf = 2000;
					break;
				}
				case 138: {
					inf = -50 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 141: {
					inf = 50000;
					break;
				}
				case 150: {
					inf = -2000;
					break;
				}
				case 168: {
					inf = 1000 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 169: {
					inf = 1000 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 210: {
					inf = -300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 211: {
					inf = -300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 212: {
					inf = -300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 213: {
					inf = -300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 214: {
					inf = -300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 215: {
					inf = 300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 216: {
					inf = 300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 217: {
					inf = 300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 218: {
					inf = 300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 219: {
					inf = 300 * Fórmulas.getMaxValor(SE.getValores());
					break;
				}
				case 265: {
					inf = -250 * Fórmulas.getMaxValor(SE.getValores());
				}
				}
				if (objetivo == null)
					continue;
				if (lanzador.getParamEquipoAliado() == objetivo.getParamEquipoAliado()) {
					influenciaTotal -= inf;
					continue;
				}
				influenciaTotal += inf;
			}
			return influenciaTotal;
		}
	}
}
