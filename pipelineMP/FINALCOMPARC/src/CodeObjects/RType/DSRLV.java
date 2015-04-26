/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeObjects.RType;

import Caches.CachedTables;
import javax.swing.table.DefaultTableModel;
import Functions.Usable;
import java.math.BigInteger;

/**
 *
 * @author joechua
 */
public class DSRLV extends RType {

    private Usable usable = new Usable();

    public DSRLV(String addr, int rd, int rs, int rt) {
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
        String rd = "";
        String sRS, sRT;
        //Long rs, rt;
        int temp;
        //int rt;
        BigInteger rs, rt;

        rs = new BigInteger(ct.getRtc().getRegisterRow(this.getRs()), 16);
        rt = new BigInteger(ct.getRtc().getRegisterRow(this.getRt()), 16);

//        rs = Long.parseLong(ct.getRtc().getRegisterRow(this.getRs()), 16);
 //       rt = Long.parseLong(ct.getRtc().getRegisterRow(this.getRt()), 16);
        sRS = rs.toString(2);
        sRS = usable.binaryToNbit(sRS, 64);
        System.out.println("sRS: " + sRS);
        sRT = rt.toString(2);
        sRT = usable.binaryToNbit(sRT, 64);
        System.out.println("String length: " + sRT.length());

        sRT = sRT.substring(58);
        System.out.println("sRT substring: " + sRT);
        temp = Integer.parseInt(sRT, 2);
        System.out.println("sRT int: " + temp);
        rd = sRS.substring(0, sRS.length() - temp);
        System.out.println("rdB: " + rd);
       // rs = Long.parseLong(ct.getRtc().getRegisterRow(this.getRs()));
        // rt = Integer.parseInt(ct.getRtc().getRegisterRow(this.getRt()), 16);
//        sRS = usable.hexStringToNBitBinary(Long.toBinaryString(rs), 64);
//        sRT = usable.hexStringToNBitBinary(Long.toBinaryString(rt), 64);
//        System.out.println("sRS: " + sRS);
//        System.out.println("sRD: " + sRT);
//        rd = sRS.substring(0, sRS.length() - rt);
        BigInteger binaryOp = new BigInteger(rd, 2);
        rd = binaryOp.toString(16);
        rd = usable.hexToNbit(rd, 16);
        System.out.println("rdB: " + rd);
        return rd;
    }

    @Override
    public int specialFunction(CachedTables ct) {
        ct.getRtc().saveRegisterValueToCache(this.getRd(), this.ALU(ct).toUpperCase());
        ct.getRtc().drawToRegisterTable();
        return -1;
    }

}
