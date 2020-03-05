package objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.TreeMap;

import common.LesGuardians;
import common.Mundo;
import common.Mundo.Duo;

import objects.Objeto;

public class Mascota {
    private int _objID;
    private int _nroComidas;
    private int _pdv;
    private String _stringStats;
    private String _fechaUltComida;
    private int _a\u00f1o;
    private int _mes;
    private int _dia;
    private int _hora;
    private int _minuto;
    private MascotaModelo _mascModelo;
    private int _idModelo;
    private boolean _esDevoraAlmas;
    private int _ultimaComida;
    private String _almasDevoradas;
    private int _obeso;
    private int _delgado;

    public Mascota(int objetoID, int PDV, String stats, int nrComidas, int a\u00f1o, int mes, int dia, int hora, int minuto, int ultComida, String almasDevoradas, int obeso, int delgado, int idModelo) {
        this._idModelo = idModelo;
        this._objID = objetoID;
        this._nroComidas = nrComidas;
        this._pdv = PDV;
        this._stringStats = stats;
        this._a\u00f1o = a\u00f1o;
        this._mes = mes;
        this._dia = dia;
        this._hora = hora;
        this._minuto = minuto;
        this._almasDevoradas = almasDevoradas;
        this._ultimaComida = ultComida;
        this._obeso = obeso;
        this._delgado = delgado;
        this._fechaUltComida = "328#" + Integer.toHexString(this._a\u00f1o) + "#" + Integer.toHexString((this._mes - 1) * 100 + this._dia) + "#" + Integer.toHexString(this._hora * 100 + this._minuto);
        this._mascModelo = Mundo.getMascotaModelo(this._idModelo);
        this._esDevoraAlmas = this._mascModelo._esDevorador;
    }

    public int getUltimaComida() {
        return this._ultimaComida;
    }

    public int getIDModelo() {
        return this._idModelo;
    }

    public int getID() {
        return this._objID;
    }

    public int getA\u00f1o() {
        return this._a\u00f1o;
    }

    public int getMes() {
        return this._mes;
    }

    public int getDia() {
        return this._dia;
    }

    public int getHora() {
        return this._hora;
    }

    public int getMinuto() {
        return this._minuto;
    }

    public int getNroComidas() {
        return this._nroComidas;
    }

    public int getPDV() {
        return this._pdv;
    }

    public void setPDV(int pdv) {
        this._pdv = pdv;
    }

    public int getMinutosDia() {
        int total = 0;
        total += (this._mes - 1) * 43200;
        total += (this._dia - 1) * 1440;
        total += this._hora * 60;
        return total += this._minuto;
    }

    public void setFecha(int A\u00f1o, int Mes, int Dia, int Hora, int Minuto) {
        this._a\u00f1o = A\u00f1o;
        this._mes = Mes;
        this._dia = Dia;
        this._hora = Hora;
        this._minuto = Minuto;
    }

    public boolean esDevoraAlmas() {
        return this._esDevoraAlmas;
    }

    public String getStringStats() {
        return this._stringStats;
    }

    public String getAlmasDevoradas() {
        return this._almasDevoradas;
    }

    public boolean esComestible(int idModComida) {
        if (idModComida == 11045)
          return true; 
        ArrayList<Comida> comidas = _mascModelo._comidas;
        for (Comida comi : comidas) {
          if (comi.getIDComida() == idModComida)
            return true; 
        } 
        return false;
      }

    public boolean getDelgado() {
        return this._delgado == 7;
    }

    public boolean getObeso() {
        return this._obeso == 7;
    }

    public void setCorpulencia(int numero) {
        if (numero == 0) {
            this._obeso = 0;
            this._delgado = 0;
        } else if (numero == 1) {
            this._obeso = 7;
            this._delgado = 0;
        } else if (numero == 2) {
            this._obeso = 0;
            this._delgado = 7;
        }
    }

    public void comerAlma(int idModeloMob, int cantAlmasDevor) {
        boolean puede;
        Comida comida = null;
        for (Comida comi : this._mascModelo._comidas) {
            if (comi.getIDComida() != idModeloMob) continue;
            comida = comi;
            break;
        }
        if (comida == null) {
            return;
        }
        int cantNecesaria = comida.getCantidad();
        int valorTemp = 0;
        TreeMap<Integer, Integer> almas = new TreeMap<Integer, Integer>();
        if (!this._almasDevoradas.isEmpty()) {
            for (String stati : this._almasDevoradas.split(";")) {
                int statID = Integer.parseInt(stati.split(",")[0]);
                int valor = Integer.parseInt(stati.split(",")[1]);
                almas.put(statID, valor);
            }
        }
        if (almas.containsKey(idModeloMob)) {
            valorTemp = (Integer)almas.get(idModeloMob);
            int nuevo = valorTemp + cantAlmasDevor;
            almas.remove(idModeloMob);
            almas.put(idModeloMob, nuevo);
        } else {
            almas.put(idModeloMob, cantAlmasDevor);
        }
        int valorNuevo = valorTemp + cantAlmasDevor;
        String almasDevoradas = "";
        boolean primero = true;
        if (almas.size() != 0) {
        	for (Entry<Integer, Integer> entry : almas.entrySet()) {
                if (!primero) {
                    almasDevoradas = String.valueOf(almasDevoradas) + ";";
                }
                almasDevoradas = String.valueOf(almasDevoradas) + entry.getKey() + "," + entry.getValue();
                primero = false;
            }
            this._almasDevoradas = almasDevoradas;
        }
        int efecto = comida.getIDStat();
        int maximo = 0;
        int maxPorStat = 0;
        if (!this._stringStats.isEmpty()) {
            String[] stat;
            String[] arrstring = stat = this._stringStats.split(",");
            int n = stat.length;
            for (int i = 0; i < n; ++i) {
                String Stat = arrstring[i];
                String[] a = Stat.split("#");
                int statId = Integer.parseInt(a[0], 16);
                if (statId == efecto) {
                    maxPorStat = Integer.parseInt(a[4].split("\\+")[1]);
                    maximo += maxPorStat;
                    continue;
                }
                if (statId >= 210 && statId <= 214) {
                    maximo += Integer.parseInt(a[4].split("\\+")[1]) * 6;
                    continue;
                }
                maximo += Integer.parseInt(a[4].split("\\+")[1]);
            }
        }
        boolean puedeAumentar = maxPorStat < this._mascModelo.getStatsPorEfecto(efecto);
        puede = maximo < this._mascModelo._maxStats;
        if (!puedeAumentar || !puede) {
            return;
        }
        if (valorNuevo / cantNecesaria > valorTemp / cantNecesaria) {
            TreeMap<Integer, Integer> stasitos = new TreeMap<Integer, Integer>();
            if (!this._stringStats.isEmpty()) {
                for (String stati : this._stringStats.split(",")) {
                    int statID = Integer.parseInt(stati.split("#")[0], 16);
                    int valor = Integer.parseInt(stati.split("#")[4].split("\\+")[1]);
                    stasitos.put(statID, valor);
                }
            }
            if (stasitos.containsKey(efecto)) {
                int nuevo = (Integer)stasitos.get(efecto);
                nuevo = efecto == 158 ? (nuevo += 10) : ++nuevo;
                stasitos.remove(efecto);
                stasitos.put(efecto, nuevo);
            } else if (efecto == 158) {
                stasitos.put(efecto, 10);
            } else {
                stasitos.put(efecto, 1);
            }
            String statsfinal = "";
            boolean esPrimero = true;
            if (stasitos.size() != 0) {
                for (Entry<Integer, Integer> entry : stasitos.entrySet()) {
                    if (!esPrimero) {
                        statsfinal = String.valueOf(statsfinal) + ",";
                    }
                    statsfinal = String.valueOf(statsfinal) + Integer.toHexString((Integer)entry.getKey()) + "#" + Integer.toHexString((Integer)entry.getValue()) + "#0#0#0d0+" + entry.getValue();
                    esPrimero = false;
                }
                this._stringStats = statsfinal;
            }
        }
    }

    public void comerComida(int idModComida) {
        ++this._nroComidas;
        this._ultimaComida = idModComida;
        if (this._nroComidas == 3) {
            boolean puede;
            this._nroComidas = 0;
            Comida comida = null;
            for (Comida comi : this._mascModelo._comidas) {
                if (comi.getIDComida() < 0) {
                    Objeto.ObjetoModelo objMod = Mundo.getObjModelo(idModComida);
                    if (Math.abs(comi.getIDComida()) != objMod.getTipo()) continue;
                    comida = comi;
                    break;
                }
                if (comi.getIDComida() != idModComida) continue;
                comida = comi;
                break;
            }
            if (comida == null) {
                return;
            }
            int efecto = comida.getIDStat();
            int maximo = 0;
            int maxPorStat = 0;
            if (!this._stringStats.isEmpty()) {
                String[] stat;
                String[] arrstring = stat = this._stringStats.split(",");
                int n = stat.length;
                for (int i = 0; i < n; ++i) {
                    String Stat = arrstring[i];
                    String[] a = Stat.split("#");
                    int statId = Integer.parseInt(a[0], 16);
                    if (statId == efecto) {
                        maxPorStat = Integer.parseInt(a[4].split("\\+")[1]);
                        maximo += maxPorStat;
                        continue;
                    }
                    if (statId >= 210 && statId <= 214) {
                        maximo += Integer.parseInt(a[4].split("\\+")[1]) * 6;
                        continue;
                    }
                    maximo += Integer.parseInt(a[4].split("\\+")[1]);
                }
            }
            boolean puedeAumentar = maxPorStat < this._mascModelo.getStatsPorEfecto(efecto);
            puede = maximo < this._mascModelo._maxStats;
            if (!puedeAumentar || !puede) {
                return;
            }
            TreeMap<Integer, Integer> stasitos = new TreeMap<Integer, Integer>();
            if (!this._stringStats.isEmpty()) {
                for (String stati : this._stringStats.split(",")) {
                    int statID = Integer.parseInt(stati.split("#")[0], 16);
                    int valor = Integer.parseInt(stati.split("#")[4].split("\\+")[1]);
                    stasitos.put(statID, valor);
                }
            }
            if (stasitos.containsKey(efecto)) {
                int nuevo = (Integer)stasitos.get(efecto);
                nuevo = efecto == 158 ? (nuevo += 10) : ++nuevo;
                stasitos.remove(efecto);
                stasitos.put(efecto, nuevo);
            } else if (efecto == 158) {
                stasitos.put(efecto, 10);
            } else {
                stasitos.put(efecto, 1);
            }
            String statsfinal = "";
            boolean esPrimero = true;
            if (stasitos.size() != 0) {
                for (Entry<Integer, Integer> entry : stasitos.entrySet()) {
                    if (!esPrimero) {
                        statsfinal = String.valueOf(statsfinal) + ",";
                    }
                    statsfinal = String.valueOf(statsfinal) + Integer.toHexString((Integer)entry.getKey()) + "#" + Integer.toHexString((Integer)entry.getValue()) + "#0#0#0d0+" + entry.getValue();
                    esPrimero = false;
                }
                this._stringStats = statsfinal;
            }
        }
    }

    public boolean horaComer() {
        Calendar actual = Calendar.getInstance();
        int bmes = actual.get(2) + 1;
        int bdia = actual.get(5);
        int bhora = actual.get(11);
        int bminuto = actual.get(12);
        long total = 0L;
        total += (long)((bmes - 1) * 43200);
        total += (long)((bdia - 1) * 1440);
        total += (long)(bhora * 60);
        long resta = (total += (long)bminuto) - (long)this.getMinutosDia();
        if (resta >= (long)LesGuardians.RATE_TEMPO_ALIMENTACAO) {
            this.setFecha(2012, bmes, bdia, bhora, bminuto);
            this._fechaUltComida = "328#" + Integer.toHexString(this._a\u00f1o) + "#" + Integer.toHexString((this._mes - 1) * 100 + this._dia) + "#" + Integer.toHexString(this._hora * 100 + this._minuto);
            this.setCorpulencia(0);
            return true;
        }
        return false;
    }

    public long entreComidas() {
        Calendar actual = Calendar.getInstance();
        int bmes = actual.get(2) + 1;
        int bdia = actual.get(5);
        int bhora = actual.get(11);
        int bminuto = actual.get(12);
        int total = 0;
        total += (bmes - 1) * 43200;
        total += (bdia - 1) * 1440;
        total += bhora * 60;
        return (total += bminuto) - this.getMinutosDia();
    }

    public String analizarStatsMascota() {
        String stats = "320#0#0#" + Integer.toHexString(this.getPDV()) + "," + this._fechaUltComida + ",326#0#" + this._obeso + "#" + this._delgado;
        if (this._ultimaComida != -1) {
            stats = String.valueOf(stats) + ",327#0#0#" + Integer.toHexString(this._ultimaComida);
        }
        if (!this._almasDevoradas.isEmpty()) {
            String str = "";
            boolean primero = false;
            for (String almas : this._almasDevoradas.split(";")) {
                String[] detalle = almas.split(",");
                if (primero) {
                    str = String.valueOf(str) + ",";
                }
                int idReal = Integer.parseInt(detalle[0]);
                String nombreMob = Mundo.getMobModelo(idReal).getNombre();
                str = String.valueOf(str) + "2cd#" + Integer.toHexString(idReal) + "#0#" + Integer.toHexString(Integer.parseInt(detalle[1])) + "#" + nombreMob;
                primero = true;
            }
            stats = String.valueOf(stats) + "," + str;
        }
        if (!this._stringStats.isEmpty()) {
            stats = String.valueOf(stats) + "," + this._stringStats;
        }
        return stats;
    }

    public String analizarStatsFantasma() {
        String stats = String.valueOf(this._fechaUltComida) + ",326#0#" + this._obeso + "#" + this._delgado;
        if (this._ultimaComida != -1) {
            stats = String.valueOf(stats) + ",327#0#0#" + Integer.toHexString(this._ultimaComida);
        }
        if (!this._almasDevoradas.isEmpty()) {
            String str = "";
            boolean primero = false;
            for (String almas : this._almasDevoradas.split(";")) {
                String[] detalle = almas.split(",");
                if (primero) {
                    str = String.valueOf(str) + ",";
                }
                int idReal = Integer.parseInt(detalle[0]);
                String nombreMob = Mundo.getMobModelo(idReal).getNombre();
                str = String.valueOf(str) + "2cd#" + Integer.toHexString(idReal) + "#0#" + Integer.toHexString(Integer.parseInt(detalle[1])) + "#" + nombreMob;
                primero = true;
            }
            stats = String.valueOf(stats) + "," + str;
        }
        if (!this._stringStats.isEmpty()) {
            stats = String.valueOf(stats) + "," + this._stringStats;
        }
        return stats;
    }

    public static class Comida {
        private int _idComida;
        private int _cant;
        private int _idStat;

        public Comida(int idModelo, int cant, int idStat) {
            this._idComida = idModelo;
            this._cant = cant;
            this._idStat = idStat;
        }

        public int getIDComida() {
            return this._idComida;
        }

        public int getCantidad() {
            return this._cant;
        }

        public int getIDStat() {
            return this._idStat;
        }
    }

    public static class MascotaModelo {
        private int _maxStats;
        private ArrayList<Duo<Integer, Integer>> _statsPorEfecto = new ArrayList<Duo<Integer, Integer>>();
        private ArrayList<Comida> _comidas = new ArrayList<Comida>();
        private boolean _esDevorador;

        public MascotaModelo(int maxStas, String statsPorEfecto, String comidas, int devorador) {
            String[] stats;
            int n;
            this._maxStats = maxStas;
            if (!comidas.isEmpty()) {
                String[] arrstring = comidas.split("\\|");
                n = arrstring.length;
                for (int i = 0; i < n; ++i) {
                    String comida = arrstring[i];
                    if (comida.isEmpty()) continue;
                    String[] str = comida.split(";");
                    try {
                        Comida comi = new Comida(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]));
                        this._comidas.add(comi);
                        continue;
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
            String[] arrstring = stats = statsPorEfecto.split("\\|");
            int n2 = stats.length;
            for (n = 0; n < n2; ++n) {
                String s = arrstring[n];
                try {
                    this._statsPorEfecto.add(new Mundo.Duo<Integer, Integer>(Integer.parseInt(s.split(";")[0]), Integer.parseInt(s.split(";")[1])));
                    continue;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            this._esDevorador = devorador == 1;
        }

        public int getMaxStats() {
            return this._maxStats;
        }

        public int getStatsPorEfecto(int stat) {
            for (Mundo.Duo<Integer, Integer> duo : this._statsPorEfecto) {
                if ((Integer)duo._primero != stat) continue;
                return (Integer)duo._segundo;
            }
            return 0;
        }
    }
}

