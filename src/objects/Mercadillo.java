package objects;

import common.SQLManager;
import common.SocketManager;
import common.Mundo;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import objects.Cuenta;
import objects.Objeto;
import objects.Personaje;

public class Mercadillo {
    private int _idPuestoMerca;
    private float _porcMercadillo;
    private short _tiempoVenta;
    private short _maxObjCuenta;
    private String _tipoObjPermitidos;
    private short _nivelMax;
    private Map<Integer, TipoObjetos> _listaTipoObj = new HashMap<Integer, TipoObjetos>();
    private Map<Integer, Mundo.Duo<Integer, Integer>> _path = new HashMap<Integer, Mundo.Duo<Integer, Integer>>();
    private DecimalFormat _porcentaje = new DecimalFormat("0.0");

    public Mercadillo(int mercadilloID, float tasa, short tiempoVenta, short maxObjCuenta, short nivelMax, String tipoObj) {
        this._idPuestoMerca = mercadilloID;
        this._porcMercadillo = tasa;
        this._maxObjCuenta = maxObjCuenta;
        this._tipoObjPermitidos = tipoObj;
        this._nivelMax = nivelMax;
        for (String tipo : tipoObj.split(",")) {
            int tipoID = Integer.parseInt(tipo);
            this._listaTipoObj.put(tipoID, new TipoObjetos(tipoID));
        }
    }

    public int getIDMercadillo() {
        return this._idPuestoMerca;
    }

    public float getPorcentaje() {
        return this._porcMercadillo;
    }

    public short getTiempoVenta() {
        return this._tiempoVenta;
    }

    public short getMaxObjCuenta() {
        return this._maxObjCuenta;
    }

    public String getTipoObjPermitidos() {
        return this._tipoObjPermitidos;
    }

    public short getNivelMax() {
        return this._nivelMax;
    }

    public String analizarParaEHl(int modeloID) {
        int tipo = Mundo.getObjModelo(modeloID).getTipo();
        return this._listaTipoObj.get(tipo).getModelo(modeloID).analizarParaEHl();
    }

    public String stringModelo(int tipoObj) {
        return this._listaTipoObj.get(tipoObj).stringModelo();
    }

    public String porcentajeImpuesto() {
        return this._porcentaje.format(this._porcMercadillo).replace(",", ".");
    }

    public LineaMercadillo getLinea(int lineaID) {
        try {
            int tipoObj = (Integer)this._path.get((Object)Integer.valueOf((int)lineaID))._primero;
            int idModelo = (Integer)this._path.get((Object)Integer.valueOf((int)lineaID))._segundo;
            return this._listaTipoObj.get(tipoObj).getModelo(idModelo).getLinea(lineaID);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public ArrayList<ObjetoMercadillo> todoListaObjMercaDeUnPuesto() {
        ArrayList<ObjetoMercadillo> listaObjMerca = new ArrayList<ObjetoMercadillo>();
        for (TipoObjetos tipo : this._listaTipoObj.values()) {
            listaObjMerca.addAll(tipo.todoListaObjMercaDeUnTipo());
        }
        return listaObjMerca;
    }

    public void addObjMercaAlPuesto(ObjetoMercadillo objMerca) {
        if (objMerca.getObjeto() == null) {
            return;
        }
        objMerca.setIDPuesto(this._idPuestoMerca);
        Objeto.ObjetoModelo objModelo = objMerca.getObjeto().getModelo();
        int tipoObj = objModelo.getTipo();
        int idModelo = objModelo.getID();
        this._listaTipoObj.get(tipoObj).addModeloVerificacion(objMerca);
        this._path.put(objMerca.getLineaID(), new Mundo.Duo<Integer, Integer>(tipoObj, idModelo));
        Mundo.addObjMercadillo(objMerca.getDue\u00f1o(), this._idPuestoMerca, objMerca);
    }

    public boolean borrarObjMercaDelPuesto(ObjetoMercadillo objMerca) {
        Objeto objeto = objMerca.getObjeto();
        if (objeto == null) {
            return false;
        }
        boolean borrable = this._listaTipoObj.get(objeto.getModelo().getTipo()).borrarObjMercaDeModelo(objMerca);
        if (borrable) {
            this._path.remove(objMerca.getLineaID());
            Mundo.borrarObjMercadillo(objMerca.getDue\u00f1o(), objMerca.getIDDelPuesto(), objMerca);
        }
        return borrable;
    }

    public synchronized boolean comprarObjeto(int lineaID, int cant, int precio, Personaje nuevoDue\u00f1o) {
        boolean posible;
        block8: {
            posible = true;
            if (nuevoDue\u00f1o.getKamas() >= (long)precio) break block8;
            return false;
        }
        try {
            Cuenta cuenta;
            LineaMercadillo linea = this.getLinea(lineaID);
            ObjetoMercadillo objAComprar = linea.tuTienes(cant, precio);
            nuevoDue\u00f1o.addKamas(precio * -1);
            if (objAComprar.getDue\u00f1o() != -1 && (cuenta = Mundo.getCuenta(objAComprar.getDue\u00f1o())) != null) {
                cuenta.setKamasBanco(cuenta.getKamasBanco() + (long)objAComprar.getPrecio());
            }
            SocketManager.ENVIAR_As_STATS_DEL_PJ(nuevoDue\u00f1o);
            Objeto objeto = objAComprar.getObjeto();
            if (nuevoDue\u00f1o.addObjetoSimilar(objeto, true, -1)) {
                Mundo.eliminarObjeto(objeto.getID());
            } else {
                nuevoDue\u00f1o.addObjetoPut(objeto);
                SocketManager.ENVIAR_OAKO_APARECER_OBJETO(nuevoDue\u00f1o, objeto);
            }
            objeto.getModelo().nuevoPrecio(objAComprar.getTipoCantidad(true), precio);
            this.borrarObjMercaDelPuesto(objAComprar);
            if (Mundo.getCuenta(objAComprar.getDue\u00f1o()) != null && Mundo.getCuenta(objAComprar.getDue\u00f1o()).getTempPersonaje() != null) {
                SocketManager.ENVIAR_Im_INFORMACION(Mundo.getCuenta(objAComprar.getDue\u00f1o()).getTempPersonaje(), "065;" + precio + "~" + objeto.getModelo().getID() + "~" + objeto.getModelo().getID() + "~1");
            }
            if (objAComprar.getDue\u00f1o() == -1) {
                SQLManager.SALVAR_OBJETO(objeto);
            }
            objAComprar = null;
        }
        catch (NullPointerException e) {
            posible = false;
        }
        return posible;
    }

    public class LineaMercadillo {
        private int _lineaID;
        private ArrayList<ArrayList<ObjetoMercadillo>> _categorias = new ArrayList(3);
        private String _strStats;
        private int _modeloID;

        public LineaMercadillo(int lineaID, ObjetoMercadillo objMercadillo) {
            this._lineaID = lineaID;
            Objeto objeto = objMercadillo.getObjeto();
            this._strStats = objeto.convertirStatsAString();
            this._modeloID = objeto.getModelo().getID();
            for (int i = 0; i < 3; ++i) {
                this._categorias.add(new ArrayList());
            }
            this.addObjMercaALinea(objMercadillo);
        }

        public String getStringStats() {
            return this._strStats;
        }

        public boolean addObjMercaALinea(ObjetoMercadillo objMerca) {
            if (!this.categoriaVacia() && !this.tieneMismoStats(objMerca)) {
                return false;
            }
            objMerca.setLineaID(this._lineaID);
            byte index = (byte)(objMerca.getTipoCantidad(false) - 1);
            this._categorias.get(index).add(objMerca);
            this.ordenar(index);
            return true;
        }

        public boolean tieneMismoStats(ObjetoMercadillo objMerca) {
            Objeto objeto = objMerca.getObjeto();
            return this._strStats.equalsIgnoreCase(objeto.convertirStatsAString()) && objeto.getModelo().getTipo() != 85;
        }

        public ObjetoMercadillo tuTienes(int categoria, int precio) {
            int index = categoria - 1;
            for (int i = 0; i < this._categorias.get(index).size(); ++i) {
                if (this._categorias.get(index).get(i).getPrecio() != precio) continue;
                return this._categorias.get(index).get(i);
            }
            return null;
        }

        public int[] getPrimeras() {
            int[] aRetornar = new int[3];
            for (int i = 0; i < this._categorias.size(); ++i) {
                try {
                    aRetornar[i] = this._categorias.get(i).get(0).getPrecio();
                    continue;
                }
                catch (IndexOutOfBoundsException e) {
                    aRetornar[i] = 0;
                }
            }
            return aRetornar;
        }

        public ArrayList<ObjetoMercadillo> todosObjMercaDeUnaLinea() {
            int totalEntradas = this._categorias.get(0).size() + this._categorias.get(1).size() + this._categorias.get(2).size();
            ArrayList<ObjetoMercadillo> todosObjMerca = new ArrayList<ObjetoMercadillo>(totalEntradas);
            for (int cat = 0; cat < this._categorias.size(); ++cat) {
                todosObjMerca.addAll((Collection<ObjetoMercadillo>)this._categorias.get(cat));
            }
            return todosObjMerca;
        }

        public boolean borrarObjMercaDeLinea(ObjetoMercadillo objMercadillo) {
            byte categoria = (byte)(objMercadillo.getTipoCantidad(false) - 1);
            boolean borrable = this._categorias.get(categoria).remove(objMercadillo);
            this.ordenar(categoria);
            return borrable;
        }

        public String analizarParaEHl() {
            int[] precio;
            String aRetornar = "";
            aRetornar = String.valueOf(aRetornar) + this._lineaID + ";" + this._strStats + ";" + ((precio = this.getPrimeras())[0] == 0 ? "" : Integer.valueOf(precio[0])) + ";" + (precio[1] == 0 ? "" : Integer.valueOf(precio[1])) + ";" + (precio[2] == 0 ? "" : Integer.valueOf(precio[2]));
            return aRetornar;
        }

        public String analizarParaEHm() {
            int[] precio;
            String aRetornar = String.valueOf(this._lineaID) + "|" + this._modeloID + "|" + this._strStats + "|" + ((precio = this.getPrimeras())[0] == 0 ? "" : Integer.valueOf(precio[0])) + "|" + (precio[1] == 0 ? "" : Integer.valueOf(precio[1])) + "|" + (precio[2] == 0 ? "" : Integer.valueOf(precio[2]));
            return aRetornar;
        }

        public void ordenar(byte categoria) {
            Collections.sort((List)this._categorias.get(categoria));
        }

        public boolean categoriaVacia() {
            int i = 0;
            while (i < this._categorias.size()) {
                try {
                    if (this._categorias.get(i).get(0) == null) continue;
                    return false;
                }
                catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
                    ++i;
                }
            }
            return true;
        }
    }

    private class Modelo {
        int _modeloID;
        Map<Integer, LineaMercadillo> _lineas = new HashMap<Integer, LineaMercadillo>();

        public Modelo(int modeloID, ObjetoMercadillo objMercadillo) {
            this._modeloID = modeloID;
            this.addObjMercaConLinea(objMercadillo);
        }

        public void addObjMercaConLinea(ObjetoMercadillo objMerca) {
            for (LineaMercadillo linea : this._lineas.values()) {
                if (!linea.addObjMercaALinea(objMerca)) continue;
                return;
            }
            int lineaID = Mundo.sigIDLineaMercadillo();
            this._lineas.put(lineaID, new LineaMercadillo(lineaID, objMerca));
        }

        public LineaMercadillo getLinea(int lineaID) {
            return this._lineas.get(lineaID);
        }

        public boolean borrarObjMercaDeUnaLinea(ObjetoMercadillo objMerca) {
            boolean borrable = this._lineas.get(objMerca.getLineaID()).borrarObjMercaDeLinea(objMerca);
            if (this._lineas.get(objMerca.getLineaID()).categoriaVacia()) {
                this._lineas.remove(objMerca.getLineaID());
            }
            return borrable;
        }

        public ArrayList<ObjetoMercadillo> todosObjMercaDeUnModelo() {
            ArrayList<ObjetoMercadillo> listaObj = new ArrayList<ObjetoMercadillo>();
            for (LineaMercadillo linea : this._lineas.values()) {
                listaObj.addAll(linea.todosObjMercaDeUnaLinea());
            }
            return listaObj;
        }

        public String analizarParaEHl() {
            String string = String.valueOf(this._modeloID) + "|";
            boolean primero = true;
            for (LineaMercadillo linea : this._lineas.values()) {
                if (!primero) {
                    string = String.valueOf(string) + "|";
                }
                string = String.valueOf(string) + linea.analizarParaEHl();
                primero = false;
            }
            return string;
        }

        public boolean estaVacio() {
            return this._lineas.size() == 0;
        }
    }

    public static class ObjetoMercadillo
    implements Comparable<ObjetoMercadillo> {
        private int _idDelPuesto;
        private int _precio;
        private int _tipoCantidad;
        private Objeto _objeto;
        private int _lineaID;
        private int _due\u00f1o;

        public ObjetoMercadillo(int precio, int cant, int due\u00f1o, Objeto obj) {
            this._precio = precio;
            this._tipoCantidad = cant;
            this._objeto = obj;
            this._due\u00f1o = due\u00f1o;
        }

        public void setIDPuesto(int id) {
            this._idDelPuesto = id;
        }

        public int getIDDelPuesto() {
            return this._idDelPuesto;
        }

        public int getPrecio() {
            return this._precio;
        }

        public int getTipoCantidad(boolean cantidadReal) {
            if (cantidadReal) {
                return (int)(Math.pow(10.0, this._tipoCantidad) / 10.0);
            }
            return this._tipoCantidad;
        }

        public Objeto getObjeto() {
            return this._objeto;
        }

        public int getLineaID() {
            return this._lineaID;
        }

        public void setLineaID(int ID) {
            this._lineaID = ID;
        }

        public int getDue\u00f1o() {
            return this._due\u00f1o;
        }

        public String analizarParaEL() {
            int cantidad = this.getTipoCantidad(true);
            return String.valueOf(this._lineaID) + ";" + cantidad + ";" + this._objeto.getModelo().getID() + ";" + this._objeto.convertirStatsAString() + ";" + this._precio + ";350";
        }

        public String analizarParaEmK() {
            int cantidad = this.getTipoCantidad(true);
            return String.valueOf(this._objeto.getID()) + "|" + cantidad + "|" + this._objeto.getModelo().getID() + "|" + this._objeto.convertirStatsAString() + "|" + this._precio + "|350";
        }

        public String analizarObjeto(char separador) {
            int cantidad = this.getTipoCantidad(true);
            return String.valueOf(this._lineaID + separador + cantidad + separador + this._objeto.getModelo().getID() + separador) + this._objeto.convertirStatsAString() + separador + this._precio + separador + "350";
        }

        @Override
        public int compareTo(ObjetoMercadillo objMercadillo) {
            int otroPrecio = objMercadillo.getPrecio();
            if (otroPrecio > this._precio) {
                return -1;
            }
            if (otroPrecio == this._precio) {
                return 0;
            }
            if (otroPrecio < this._precio) {
                return 1;
            }
            return 0;
        }
    }

    private class TipoObjetos {
        Map<Integer, Modelo> _modelos = new HashMap<Integer, Modelo>();
        int _tipoObjID;

        public TipoObjetos(int categoriaID) {
            this._tipoObjID = categoriaID;
        }

        public void addModeloVerificacion(ObjetoMercadillo objMerca) {
            int modeloID = objMerca.getObjeto().getModelo().getID();
            Modelo modelo = this._modelos.get(modeloID);
            if (modelo == null) {
                this._modelos.put(modeloID, new Modelo(modeloID, objMerca));
            } else {
                modelo.addObjMercaConLinea(objMerca);
            }
        }

        public boolean borrarObjMercaDeModelo(ObjetoMercadillo objMerca) {
            boolean borrable = false;
            int idModelo = objMerca.getObjeto().getModelo().getID();
            this._modelos.get(idModelo).borrarObjMercaDeUnaLinea(objMerca);
            borrable = this._modelos.get(idModelo).estaVacio();
            if (borrable) {
                this.borrarModelo(idModelo);
            }
            return borrable;
        }

        public Modelo getModelo(int modeloID) {
            return this._modelos.get(modeloID);
        }

        public ArrayList<ObjetoMercadillo> todoListaObjMercaDeUnTipo() {
            ArrayList<ObjetoMercadillo> listaObjMerca = new ArrayList<ObjetoMercadillo>();
            for (Modelo modelo : this._modelos.values()) {
                listaObjMerca.addAll(modelo.todosObjMercaDeUnModelo());
            }
            return listaObjMerca;
        }

        public String stringModelo() {
            boolean primero = true;
            String string = "";
            for (int curTemp : this._modelos.keySet()) {
                if (!primero) {
                    string = String.valueOf(string) + ";";
                }
                string = String.valueOf(string) + curTemp;
                primero = false;
            }
            return string;
        }

        public void borrarModelo(int modeloID) {
            this._modelos.remove(modeloID);
        }
    }
}

