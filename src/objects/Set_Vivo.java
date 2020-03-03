package objects;

public class Set_Vivo {
    private int _id;
    private int _comidaA\u00f1o;
    private int _comidaFecha;
    private int _comidaHora;
    private int _humor;
    private int _mascara;
    private int _tipo;
    private int _objAsociadoID;
    private long _exp;
    private int _interA\u00f1o;
    private int _interFecha;
    private int _interHora;
    private int _adqA\u00f1o;
    private int _adqFecha;
    private int _adqHora;
    private int _asociado;
    private int _realModeloDB;
    private int _idObjevivoOrig;
    private String _stats;

    public Set_Vivo(int ID, int comidaA\u00f1o, int comidaFecha, int comidaHora, int humor, int mascara, int tipo, int objAsociadoID, long XP, int interAno, int interFecha, int interHoras, int adqA\u00f1o, int adqFecha, int adqHora, int asociado, int realModeloDB, int objevivoOrig, String stats) {
        this._id = ID;
        this._comidaA\u00f1o = comidaA\u00f1o;
        this._comidaFecha = comidaFecha;
        this._comidaHora = comidaHora;
        this._humor = humor;
        this._mascara = mascara;
        this._tipo = tipo;
        this._objAsociadoID = objAsociadoID;
        this._exp = XP;
        this._interA\u00f1o = interAno;
        this._interFecha = interFecha;
        this._interHora = interHoras;
        this._adqA\u00f1o = adqA\u00f1o;
        this._adqFecha = adqFecha;
        this._adqHora = adqHora;
        this._asociado = asociado;
        this._realModeloDB = realModeloDB;
        this._idObjevivoOrig = objevivoOrig;
        this._stats = stats;
    }

    public int getID() {
        return this._id;
    }

    public int getComidaA\u00f1o() {
        return this._comidaA\u00f1o;
    }

    public void setComidaA\u00f1o(int a\u00f1o) {
        this._comidaA\u00f1o = a\u00f1o;
    }

    public int getComidaFecha() {
        return this._comidaFecha;
    }

    public void setComidaFecha(int fecha) {
        this._comidaFecha = fecha;
    }

    public int getComidaHora() {
        return this._comidaHora;
    }

    public void setComidaHora(int hora) {
        this._comidaHora = hora;
    }

    public int getHumor() {
        return this._humor;
    }

    public void setHumor(int humor) {
        this._humor = humor;
    }

    public int getMascara() {
        return this._mascara;
    }

    public void setMascara(int mascara) {
        this._mascara = mascara;
    }

    public int getTipo() {
        return this._tipo;
    }

    public void setTipo(int tipo) {
        this._tipo = tipo;
    }

    public int getObjetoAsociadoID() {
        return this._objAsociadoID;
    }

    public void setObjetoAsociadoID(int objetoID) {
        this._objAsociadoID = objetoID;
    }

    public long getExp() {
        return this._exp;
    }

    public void setExp(long exp) {
        this._exp = exp;
    }

    public int getInterA\u00f1o() {
        return this._interA\u00f1o;
    }

    public void setInterA\u00f1o(int a\u00f1o) {
        this._interA\u00f1o = a\u00f1o;
    }

    public int getInterFecha() {
        return this._interFecha;
    }

    public void setInterFecha(int fecha) {
        this._interFecha = fecha;
    }

    public int getInterHora() {
        return this._interHora;
    }

    public void setInterHora(int hora) {
        this._interHora = hora;
    }

    public int getAdqA\u00f1o() {
        return this._adqA\u00f1o;
    }

    public void setAdqA\u00f1o(int a\u00f1o) {
        this._adqA\u00f1o = a\u00f1o;
    }

    public int getAdqFecha() {
        return this._adqFecha;
    }

    public void setAdqFecha(int fecha) {
        this._adqFecha = fecha;
    }

    public int getAdqHora() {
        return this._adqHora;
    }

    public void setAdqHora(int hora) {
        this._adqHora = hora;
    }

    public int getAsociado() {
        return this._asociado;
    }

    public void setAsociado(int asociado) {
        this._asociado = asociado;
    }

    public int getRealModeloDB() {
        return this._realModeloDB;
    }

    public void setRealModeloDB(int modelo) {
        this._realModeloDB = modelo;
    }

    public int getIDObjevivoOrig() {
        return this._idObjevivoOrig;
    }

    public String getStat() {
        return this._stats;
    }

    public void setStat(String stat) {
        this._stats = stat;
    }

    public String convertirAString() {
        String str = "328#" + Integer.toHexString(this._comidaA\u00f1o) + "#" + Integer.toHexString(this._comidaFecha) + "#" + Integer.toHexString(this._comidaHora) + "," + "3cb#0#0#" + Integer.toBinaryString(this._humor) + "," + "3cc#0#0#" + Integer.toHexString(this._mascara) + "," + "3cd#0#0#" + Integer.toHexString(this._tipo) + "," + "3ca#0#0#" + Integer.toHexString(this._realModeloDB) + "," + "3ce#0#0#" + Long.toHexString(this._exp) + "," + "3d7#" + Integer.toHexString(this._interA\u00f1o) + "#" + Integer.toHexString(this._interFecha) + "#" + Integer.toHexString(this._interHora) + "," + "325#" + Integer.toHexString(this._adqA\u00f1o) + "#" + Integer.toHexString(this._adqFecha) + "#" + Integer.toHexString(this._adqHora);
        return str;
    }
}

