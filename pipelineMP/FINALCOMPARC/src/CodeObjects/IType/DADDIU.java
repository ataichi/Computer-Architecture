/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeObjects.IType;

import Caches.CachedTables;
import java.math.BigInteger;
import Functions.Usable;

/**
 *
 * @author joechua
 */
public class DADDIU extends IType {

    private Usable usable = new Usable();

    public DADDIU(String addr, int rd, int rs, int rt, String immORoffset) {
        super(addr, rd, rs, rt, immORoffset);
    }

    @Override
    public boolean haveDataHazard(int rd) {
        if (this.getRs() == rd) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the rd
     */
    public int getRd() {
        return rd;
    }

    /**
     * @param rd the rd to set
     */
    public void setRd(int rd) {
        this.rd = rd;
    }

    @Override
    public String ALU(CachedTables ct) {
        BigInteger rs, ans, imm;

        String ansRT = null;
        String ansALU = null;
        String immTemp = null;

        immTemp = ct.getOtc().geOpcodeRow(this.insNumber).getImm();
        imm = new BigInteger(immTemp, 2);

//        System.out.println("rs" + ct.getRtc().getRegisterRow(0));
//        System.out.println("rt" + ct.getRtc().getRegisterRow(2));
//        System.out.println("rd" + ct.getRtc().getRegisterRow(3));
        rs = new BigInteger(ct.getRtc().getRegisterRow(this.getRs()), 16);

        ans = rs.add(imm);

        ansALU = ans.toString(16);

        if (ansALU.length() > 16) {
            ansALU = ansALU.substring(ansALU.length() - 16);
        } 
        else {
            ansALU = usable.hexToNbit(ansALU, 16);
        }

        return ansALU.toUpperCase();
    }

    @Override
    public int specialFunction(CachedTables ct) {
        ct.getRtc().saveRegisterValueToCache(this.getRd(), this.ALU(ct).toUpperCase());
        ct.getRtc().drawToRegisterTable();
        return -1;
    }

}
