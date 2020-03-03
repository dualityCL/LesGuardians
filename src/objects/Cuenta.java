package objects;

import common.CryptManager;
import common.LesGuardians;
import common.SQLManager;
import common.SocketManager;
import common.Mundo;
import game.GameThread;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import objects.Dragopavo;
import objects.Mercadillo;
import objects.Objeto;
import objects.Personaje;
import realm.RealmThread;

public class Cuenta {
    private int _guid;
    private String _account;
    private String _pass;
    private String _apodo;
    private String _key;
    private String _ultimoIP = "";
    private String _question;
    private String _reponse;
    private boolean _baneado = false;
    private int _rango = 0;
    private int _vip = 0;
    private String _tempIP = "";
    private String _ultimaFechaConeccion = "";
    private GameThread _entradaPersonaje;
    private RealmThread _entradaGeneral;
    private Personaje _tempPerso;
    private long _kamasBanco = 0L;
    private Map<Integer, Objeto> _objetosEnBanco = new TreeMap<Integer, Objeto>();
    private ArrayList<Integer> _idsAmigos = new ArrayList();
    private ArrayList<Integer> _idsEnemigos = new ArrayList();
    private ArrayList<Dragopavo> _establo = new ArrayList();
    private boolean _muteado = false;
    public long _tiempoMuteado;
    public int _posicion = -1;
    private int _primeraVez;
    private Map<Integer, ArrayList<Mercadillo.ObjetoMercadillo>> _objMercadillos;
    private Map<Integer, Personaje> _personajes = new TreeMap<Integer, Personaje>();
    private int _regalo;
    public long _horaMuteada = 0L;

    public Cuenta(int ID, String nombre, String password, String apodo, String pregunta, String respuesta, int nivelGM, int vip, boolean baneado, String ultimaIP, String ultimaConeccion, String banco, long kamasBanco, String amigos, String enemigos, String establo, int primeravez, int regalo) {
        this._guid = ID;
        this._account = nombre;
        this._pass = password;
        this._apodo = apodo;
        this._question = pregunta;
        this._reponse = respuesta;
        this._rango = nivelGM;
        this._vip = vip;
        this._baneado = baneado;
        this._ultimoIP = ultimaIP;
        this._ultimaFechaConeccion = ultimaConeccion;
        this._kamasBanco = kamasBanco;
        this._objMercadillos = Mundo.getMisObjetos(this._guid);
        for (String item : banco.split("\\|")) {
            String[] infos;
            int id;
            Objeto obj;
            if (item.equals("") || (obj = Mundo.getObjeto(id = Integer.parseInt((infos = item.split(":"))[0]))) == null) continue;
            this._objetosEnBanco.put(obj.getID(), obj);
        }
        for (String f : amigos.split(";")) {
            try {
                this._idsAmigos.add(Integer.parseInt(f));
            }
            catch (Exception infos) {
                // empty catch block
            }
        }
        for (String f : enemigos.split(";")) {
            try {
                this._idsEnemigos.add(Integer.parseInt(f));
            }
            catch (Exception infos) {
                // empty catch block
            }
        }
        for (String d : establo.split(";")) {
            try {
                Dragopavo DP = Mundo.getDragopavoPorID(Integer.parseInt(d));
                if (DP == null) continue;
                this._establo.add(DP);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        this._primeraVez = primeravez;
        this._regalo = regalo;
    }

    public ArrayList<Dragopavo> getEstablo() {
        return this._establo;
    }

    public int getPrimeraVez() {
        return this._primeraVez;
    }

    public void setKamasBanco(long i) {
        this._kamasBanco = i;
    }

    public boolean estaMuteado() {
        return this._muteado;
    }

    public int getRegalo() {
        return this._regalo;
    }

    public void setRegalo() {
        this._regalo = 0;
    }

    public void setRegalo(int regalo) {
        this._regalo = regalo;
    }

    public void mutear(boolean b, int tiempo) {
        this._muteado = b;
        String msg = "";
        msg = this._muteado ? "Ha sido muteado" : "Ha sido desmuteado";
        SocketManager.ENVIAR_cs_CHAT_MENSAJE(this._tempPerso, msg, LesGuardians.COR_MSG);
        if (tiempo == 0) {
            return;
        }
        this._tiempoMuteado = tiempo * 1000;
        this._horaMuteada = System.currentTimeMillis();
    }

    public String stringBancoObjetosBD() {
        String str = "";
        for (Map.Entry<Integer, Objeto> entry : this._objetosEnBanco.entrySet()) {
            Objeto obj = entry.getValue();
            str = String.valueOf(str) + obj.getID() + "|";
        }
        return str;
    }

    public Map<Integer, Objeto> getObjetosBanco() {
        return this._objetosEnBanco;
    }

    public long getKamasBanco() {
        return this._kamasBanco;
    }

    public void setEntradaPersonaje(GameThread t) {
        this._entradaPersonaje = t;
    }

    public void setTempIP(String ip) {
        this._tempIP = ip;
    }

    public String getUltimaConeccion() {
        return this._ultimaFechaConeccion;
    }

    public void setUltimoIP(String ultimoIP) {
        this._ultimoIP = ultimoIP;
    }

    public void setUltimaConeccion(String ultimaConeccion) {
        this._ultimaFechaConeccion = ultimaConeccion;
    }

    public GameThread getEntradaPersonaje() {
        return this._entradaPersonaje;
    }

    public RealmThread getEntradaGeneral() {
        return this._entradaGeneral;
    }

    public int getID() {
        return this._guid;
    }

    public String getNombre() {
        return this._account;
    }

    public String getContrase\u00f1a() {
        return this._pass;
    }

    public String getApodo() {
        if (this._apodo.isEmpty() || this._apodo == "") {
            this._apodo = this._account;
        }
        return this._apodo;
    }

    public String getClaveCliente() {
        return this._key;
    }

    public void setClaveCliente(String aKey) {
        this._key = aKey;
    }

    public String getUltimoIP() {
        return this._ultimoIP;
    }

    public String getPregunta() {
        return this._question;
    }

    public Personaje getTempPersonaje() {
        return this._tempPerso;
    }

    public String getRespuesta() {
        return this._reponse;
    }

    public boolean estaBaneado() {
        return this._baneado;
    }

    public void setBaneado(boolean baneado) {
        this._baneado = baneado;
    }

    public boolean enLinea() {
        return this._entradaGeneral != null || this._entradaPersonaje != null;
    }

    public int getRango() {
        return this._rango;
    }

    public String getActualIP() {
        return this._tempIP;
    }

    public void cambiarContrase\u00f1a(String nueva) {
        this._pass = nueva;
    }

    public static boolean cuentaLogin(String nombre, String contrase\u00f1a, String codigoLlave) {
        Cuenta cuenta = Mundo.getCuentaPorNombre(nombre);
        return cuenta != null && cuenta.esContrase\u00f1aValida(contrase\u00f1a, codigoLlave);
    }

    public boolean esContrase\u00f1aValida(String contrase\u00f1a, String codigoLlave) {
        return contrase\u00f1a.equals(CryptManager.encriptarPassword(codigoLlave, this._pass));
    }

    public Map<Integer, Personaje> getPersonajes() {
        return this._personajes;
    }

    public void addPerso(Personaje perso) {
        if (this._personajes.get(perso.getID()) != null) {
            System.out.println("Se esta intentado volver agregar a la cuenta, al personaje " + perso.getNombre());
            return;
        }
        this._personajes.put(perso.getID(), perso);
    }

    public boolean crearPj(String nombre, int sexo, int clase, int color1, int color2, int color3) {
        Personaje perso = Personaje.crearPersonaje(nombre, sexo, clase, color1, color2, color3, this);
        if (perso == null) {
            return false;
        }
        this._personajes.put(perso.getID(), perso);
        SocketManager.ENVIAR_TB_CINEMA_INICIO_JUEGO(perso);
        return true;
    }

    public void borrarPerso(int id) {
        if (!this._personajes.containsKey(id)) {
            return;
        }
        Mundo.eliminarPj(this._personajes.get(id), true);
        this._personajes.remove(id);
    }

    public void setEntradaGeneral(RealmThread thread) {
        this._entradaGeneral = thread;
    }

    public void setTempPerso(Personaje perso) {
        this._tempPerso = perso;
    }

    public void actualizarInformacion(int id, String nombre, String contrase\u00f1a, String apodo, String pregunta, String respuesta, int rango, boolean baneado) {
        this._guid = id;
        this._account = nombre;
        this._pass = contrase\u00f1a;
        this._apodo = apodo;
        this._question = pregunta;
        this._reponse = respuesta;
        this._rango = rango;
        this._baneado = baneado;
    }

    public synchronized void desconexion() {
        this._tempPerso = null;
        this._entradaPersonaje = null;
        this._entradaGeneral = null;
        this._tempIP = "";
        SQLManager.SALVAR_CUENTA(this);
        this.resetTodosPjs();
        SQLManager.UPDATE_CUENTA_LOG_CERO(this.getID());
    }

    public synchronized void resetTodosPjs() {
        try {
          for (Personaje perso : this._personajes.values()) {
            if (!perso.enLinea())
              continue; 
            if (perso.getIntercambio() != null)
              perso.getIntercambio().cancel(); 
            Combate pelea = perso.getPelea();
            perso.setEnLinea(false);
            if (perso.getHaciendoTrabajo() != null)
              perso.getHaciendoTrabajo().interrumpirMagueada(); 
            if (pelea != null) {
              if (pelea.getEstado() != 3 || pelea.getEspectadores().containsKey(Integer.valueOf(perso.getID())) || pelea.getTipoPelea() == 0) {
                pelea.retirarsePelea(perso, null);
              } else {
                perso.getCelda().removerPersonaje(perso.getID());
                pelea.desconectarLuchador(perso);
                continue;
              } 
            } else if (perso.getMapa() != null) {
              SocketManager.ENVIAR_GM_BORRAR_PJ_A_TODOS(perso.getMapa(), perso.getID());
            } 
            if (perso.getGrupo() != null)
              perso.getGrupo().dejarGrupo(perso); 
            perso.getCelda().removerPersonaje(perso.getID());
            perso.resetVariables();
            SQLManager.SALVAR_PERSONAJE(perso, true);
            Mundo.desconectarPerso(perso);
          } 
        } catch (Exception e) {
          resetTodosPjs();
        } 
      }

    public void mensajeAAmigos() {
        for (int i : this._idsAmigos) {
            Personaje perso = Mundo.getPersonaje(i);
            if (perso == null || !perso.mostrarConeccionAmigo() || !perso.enLinea()) continue;
            SocketManager.ENVIAR_Im0143_AMIGO_CONECTADO(this._tempPerso, perso);
        }
    }

    public String analizarListaAmigosABD() {
        StringBuffer str = new StringBuffer();
        for (int i : this._idsAmigos) {
            if (!str.toString().isEmpty()) {
                str = str.append(";");
            }
            str = str.append(i);
        }
        return str.toString();
    }

    public String stringListaEnemigosABD() {
        StringBuffer str = new StringBuffer();
        for (int i : this._idsEnemigos) {
            if (!str.toString().isEmpty()) {
                str = str.append(";");
            }
            str = str.append(i);
        }
        return str.toString();
    }

    public String stringListaAmigos() {
        StringBuffer str = new StringBuffer();
        for (int i : this._idsAmigos) {
            Personaje perso;
            Cuenta cuenta = Mundo.getCuenta(i);
            if (cuenta == null) continue;
            str = str.append("|" + cuenta.getApodo());
            if (!cuenta.enLinea() || (perso = cuenta.getTempPersonaje()) == null) continue;
            str = str.append(perso.analizarListaAmigos(this._guid));
        }
        return str.toString();
    }

    public String stringListaEnemigos() {
        StringBuffer str = new StringBuffer();
        for (int i : this._idsEnemigos) {
            Personaje perso;
            Cuenta cuenta = Mundo.getCuenta(i);
            if (cuenta == null) continue;
            str = str.append("|" + cuenta.getApodo());
            if (!cuenta.enLinea() || (perso = cuenta.getTempPersonaje()) == null) continue;
            str = str.append(perso.analizarListaAmigos(this._guid));
        }
        return str.toString();
    }

    public void addAmigo(int id) {
        if (this._guid == id) {
            SocketManager.ENVIAR_FA_AGREGAR_AMIGO(this._tempPerso, "Ey");
            return;
        }
        if (this._idsEnemigos.contains(id)) {
            SocketManager.ENVIAR_iA_AGREGAR_ENEMIGO(this._tempPerso, "Ea");
            return;
        }
        if (!this._idsAmigos.contains(id)) {
            this._idsAmigos.add(id);
            Cuenta amigo = Mundo.getCuenta(id);
            SocketManager.ENVIAR_FA_AGREGAR_AMIGO(this._tempPerso, "K" + amigo.getApodo() + amigo.getTempPersonaje().analizarListaAmigos(this._guid));
        } else {
            SocketManager.ENVIAR_FA_AGREGAR_AMIGO(this._tempPerso, "Ea");
        }
    }

    public void addEnemigo(String packet, int id) {
        if (this._guid == id) {
            SocketManager.ENVIAR_iA_AGREGAR_ENEMIGO(this._tempPerso, "Ey");
            return;
        }
        if (this._idsAmigos.contains(id)) {
            SocketManager.ENVIAR_FA_AGREGAR_AMIGO(this._tempPerso, "Ea");
            return;
        }
        if (!this._idsEnemigos.contains(id)) {
            this._idsEnemigos.add(id);
            Cuenta amigo = Mundo.getCuenta(id);
            SocketManager.ENVIAR_iA_AGREGAR_ENEMIGO(this._tempPerso, "K" + amigo.getApodo() + amigo.getTempPersonaje().analizarListaEnemigos(this._guid));
        } else {
            SocketManager.ENVIAR_iA_AGREGAR_ENEMIGO(this._tempPerso, "Ea");
        }
    }

    public void borrarAmigo(int id) {
        this._idsAmigos.remove(this._idsAmigos.indexOf(id));
        SocketManager.ENVIAR_FD_BORRAR_AMIGO(this._tempPerso, "K");
    }

    public void borrarEnemigo(int id) {
        this._idsEnemigos.remove(this._idsEnemigos.indexOf(id));
        SocketManager.ENVIAR_iD_BORRAR_ENEMIGO(this._tempPerso, "K");
    }

    public boolean esAmigo(int id) {
        return this._idsAmigos.contains(id);
    }

    public boolean esEnemigo(int id) {
        return this._idsEnemigos.contains(id);
    }

    public void setPrimeraVez(int valor) {
        this._primeraVez = valor;
    }

    public synchronized String stringIDsEstablo() {
        StringBuffer str = new StringBuffer();
        boolean primero = false;
        for (Dragopavo DP : this._establo) {
            if (primero) {
                str = str.append(";");
            }
            str = str.append(DP.getID());
            primero = true;
        }
        return str.toString();
    }

    public void setRango(int rango) {
        this._rango = rango;
    }

    public int getVIP() {
        return this._vip;
    }

    public boolean recuperarObjeto(int lineaID, int cantidad) {
        if (this._tempPerso == null) {
            return false;
        }
        if (this._tempPerso.getIntercambiandoCon() >= 0) {
            return false;
        }
        int idPuestoMerca = Math.abs(this._tempPerso.getIntercambiandoCon());
        Mercadillo.ObjetoMercadillo objMerca = null;
        try {
            for (Mercadillo.ObjetoMercadillo tempEntry : this._objMercadillos.get(idPuestoMerca)) {
                if (tempEntry.getLineaID() != lineaID) continue;
                objMerca = tempEntry;
                break;
            }
        }
        catch (NullPointerException e) {
            return false;
        }
        if (objMerca == null) {
            return false;
        }
        this._objMercadillos.get(idPuestoMerca).remove(objMerca);
        Objeto obj = objMerca.getObjeto();
        if (this._tempPerso.addObjetoSimilar(obj, true, -1)) {
            Mundo.eliminarObjeto(obj.getID());
        } else {
            this._tempPerso.addObjetoPut(obj);
            SocketManager.ENVIAR_OAKO_APARECER_OBJETO(this._entradaPersonaje.getOut(), obj);
        }
        Mundo.getPuestoMerca(idPuestoMerca).borrarObjMercaDelPuesto(objMerca);
        return true;
    }

    public Mercadillo.ObjetoMercadillo[] getObjMercaDePuesto(int idPuestoMerca) {
        if (this._objMercadillos.get(idPuestoMerca) == null) {
            return new Mercadillo.ObjetoMercadillo[1];
        }
        Mercadillo.ObjetoMercadillo[] listaObjMercadillos = new Mercadillo.ObjetoMercadillo[20];
        for (int i = 0; i < this._objMercadillos.get(idPuestoMerca).size(); ++i) {
            listaObjMercadillos[i] = this._objMercadillos.get(idPuestoMerca).get(i);
        }
        return listaObjMercadillos;
    }

    public int cantidadObjMercadillo(int idPuestoMerca) {
        if (this._objMercadillos.get(idPuestoMerca) == null) {
            return 0;
        }
        return this._objMercadillos.get(idPuestoMerca).size();
    }
}

