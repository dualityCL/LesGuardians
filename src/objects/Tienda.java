package objects;

public class Tienda {
    private int _idObjeto;
    private long _precio;
    private int _cantidad;

    public Tienda(int idObjeto, long precio, int cantidad) {
        this._idObjeto = idObjeto;
        this._precio = precio;
        this._cantidad = cantidad;
    }

    public long getPrecio() {
        return this._precio;
    }

    public void setPrecio(long precio) {
        this._precio = precio;
    }

    public int getCantidad() {
        return this._cantidad;
    }

    public void setCantidad(int cant) {
        this._cantidad = cant;
    }

    public int getIdObjeto() {
        return this._idObjeto;
    }
}

