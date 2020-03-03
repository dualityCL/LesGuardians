package objects;

import common.Fórmulas;
import common.SocketManager;
import objects.Fight;

public class Desafios {
    private byte _id;
    private String _bonus;
    private int _bonusXP;
    private int _bonusPP;

    public Desafios(byte ID, String bonusreto) {
        this._id = ID;
        this._bonus = bonusreto;
        String[] bonus = this._bonus.split(";");
        this._bonusXP = Integer.parseInt(bonus[0]) + Integer.parseInt(bonus[1]);
        this._bonusPP = Integer.parseInt(bonus[2]) + Integer.parseInt(bonus[3]);
    }

    public String getBonus() {
        return this._bonus;
    }

    public int getId() {
        return this._id;
    }

    public String getDetalleReto(Fight pelea) {
        String datosReto = "";
        int idMob = 0;
        short celda = 0;
        if (this._id == 3 || this._id == 4 || this._id == 32) {
            int azar = Fórmulas.getRandomValor(0, pelea.luchadoresDeEquipo(2).size() - 1);
            Fight.Luchador mob = pelea.luchadoresDeEquipo(2).get(azar);
            idMob = mob.getID();
            celda = mob.getCeldaPelea().getID();
        }
        switch (this._id) {
            case 1: 
            case 2: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12: 
            case 14: 
            case 15: 
            case 16: 
            case 17: 
            case 18: 
            case 19: 
            case 20: 
            case 21: 
            case 22: 
            case 23: 
            case 24: 
            case 25: 
            case 28: 
            case 29: 
            case 30: 
            case 31: 
            case 33: 
            case 34: 
            case 35: 
            case 36: 
            case 37: 
            case 38: 
            case 39: 
            case 40: 
            case 41: 
            case 42: 
            case 43: 
            case 44: 
            case 45: 
            case 46: 
            case 47: 
            case 48: 
            case 49: 
            case 50: {
                datosReto = String.valueOf(this._id) + ";0;0;" + this._bonus + ";0";
                break;
            }
            case 3: 
            case 4: 
            case 32: {
                datosReto = String.valueOf(this._id) + ";1;" + idMob + ";" + this._bonus + ";0";
                pelea.setIDMobReto(idMob);
                SocketManager.ENVIAR_Gf_MOSTRAR_CASILLA_EN_PELEA(pelea, 5, idMob, celda);
            }
        }
        pelea.putStringReto(this._id, datosReto);
        return datosReto;
    }

    public int bonusXP() {
        return this._bonusXP;
    }

    public int bonusPP() {
        return this._bonusPP;
    }
}

