package objects;

public class Animacao {
    private int _ID;
    private int _animacionID;
    private String _nombreAnimacion;
    private int _areaAnimacion;
    private int _accionAnimacion;
    private int _tama\u00f1oAnimacion;

    public Animacao(int Id, int animacionID, String nombre, int area, int accion, int tama\u00f1o) {
        this._ID = Id;
        this._animacionID = animacionID;
        this._nombreAnimacion = nombre;
        this._areaAnimacion = area;
        this._accionAnimacion = accion;
        this._tama\u00f1oAnimacion = tama\u00f1o;
    }

    public int getId() {
        return this._ID;
    }

    public String getNombre() {
        return this._nombreAnimacion;
    }

    public int getArea() {
        return this._areaAnimacion;
    }

    public int getAccion() {
        return this._accionAnimacion;
    }

    public int getTama\u00f1o() {
        return this._tama\u00f1oAnimacion;
    }

    public int getAnimacionId() {
        return this._animacionID;
    }

    public static String preparaAGameAccion(Animacao animacion) {
        String Packet = String.valueOf(animacion.getAnimacionId()) + "," + animacion.getArea() + "," + animacion.getAccion() + "," + animacion.getTama\u00f1o();
        return Packet;
    }
}

