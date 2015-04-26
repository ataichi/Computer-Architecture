/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeObjects.RType;

import Caches.CachedTables;
import javax.swing.table.DefaultTableModel;
import Functions.Usable;
import UI.FRAME1;
import java.math.BigInteger;

/**
 *
 * @author joechua
 */
public class DDIV extends RType {

    private Usable usable = new Usable();

    public DDIV(String addr, int rd, int rs, int rt) {
        super(addr, rd, rs, rt);
    }

    @Override
    public boolean haveDataHazard(int rd) {
        if (this.getRs() == rd || this.getRt() == rd) {
            return true; //
        } else {
            return false;
        }
    }

    @Override
    public String ALU(CachedTables ct) {
        String rd = "0000000000000000", sLO, sHI;
        // Long rs, rt, lo, hi;
        //rs = Long.parseLong(ct.getRtc().getRegisterRow(this.getRs()));
        //rt = Long.parseLong(ct.getRtc().getRegisterRow(this.getRt()));
        BigInteger rs, rt, lo, hi;

        rs = new BigInteger(ct.getRtc().getRegisterRow(this.getRs()), 16);
        rt = new BigInteger(ct.getRtc().getRegisterRow(this.getRt()), 16);

        String temprs, temprt;
        temprs = usable.binaryToNbit(rs.toString(2), 64);
        temprt = usable.binaryToNbit(rt.toString(2), 64);
        if (rt.equals(BigInteger.ZERO)) {
            new FRAME1().ERRORDIVZERO();

        } else {
            lo = rs.divide(rt);
            hi = rs.mod(rt);
            System.out.println("lo = " + lo + " hi = " + hi);

            if ((temprs.charAt(0) == '1' && temprt.charAt(0) == '0') ||
                    (temprs.charAt(0) == '0' && temprt.charAt(0) == '1')) { // meaning negative over positive
                sLO = usable.binaryToNbitSigned(lo.toString(16), 16); //gawing hex si decimal
                sHI = usable.hexToNbit(hi.toString(16), 16);
            } else {
                sLO = usable.hexToNbit(lo.toString(16), 16); //gawing hex si decimal
                sHI = usable.hexToNbit(hi.toString(16), 16);
            }
            rd = sHI + sLO;
        }
        return rd;
    }

    @Override
    public int specialFunction(CachedTables ct) {
        //32 LO //33 HI
        ct.getRtc().saveRegisterValueToCache(32, this.ALU(ct).substring(16).toUpperCase());
        ct.getRtc().saveRegisterValueToCache(33, this.ALU(ct).substring(0, 16).toUpperCase());
        ct.getRtc().drawToRegisterTable();
        return -1;
    }

}
