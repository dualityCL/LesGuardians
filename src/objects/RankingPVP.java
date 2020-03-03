package objects;

public class RankingPVP {
    private int _id;
    private String _nombre;
    private int _victorias = 0;
    private int _derrotas = 0;
    private int _nivelAlineacion = 1;

    public RankingPVP(int id, String nombre, int victorias, int derrotas, int nivelAlineacion) {
        this._id = id;
        this._nombre = nombre;
        this._victorias = victorias;
        this._derrotas = derrotas;
        this._nivelAlineacion = nivelAlineacion;
    }

    public int getVictorias() {
        return this._victorias;
    }

    public int getDerrotas() {
        return this._derrotas;
    }

    public int getID() {
        return this._id;
    }

    public void aumentarVictoria() {
        ++this._victorias;
    }

    public void aumentarDerrota() {
        ++this._derrotas;
    }

    public void setNivelAlin(int nivel) {
        this._nivelAlineacion = nivel;
    }

    public String getNombre() {
        return this._nombre;
    }

    public int getNivelAlin() {
        return this._nivelAlineacion;
    }
}

