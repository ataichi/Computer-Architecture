/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeObjects.IType;

import Caches.CachedTables;
import CodeObjects.Instruction;
import java.math.BigInteger;
import Functions.Usable;

/**
 *
 * @author joechua
 */
public class ORI extends IType {

    private Usable usable = new Usable();

    public ORI(String addr, int rd, int rs, int rt, String immORoffset) {
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

    @Override
    public String ALU(CachedTables ct) {
        String rd = "";
        String sRS, sIMM;
        Long rs;

        sRS = ct.getRtc().getRegisterRow(this.getRs());
        sRS = usable.hexStringToNBitBinary(sRS, 64);
        sIMM = ct.getOtc().geOpcodeRow(this.insNumber).getImm();
        sIMM = usable.hexToNbit(sIMM, 64);

        for (int i = sRS.length() - 1; i >= 0; i--) {
            if (sRS.charAt(i) == '0' && sIMM.charAt(i) == '0') {
                rd = "0" + rd;
            } else {
                rd = "1" + rd;
            }
        }
        BigInteger binaryOp2 = new BigInteger(rd, 2);
        rd = binaryOp2.toString(16);
        rd = usable.hexToNbit(rd, 16);
        System.out.println(rd);
        return rd;
    }

    @Override
    public int specialFunction(CachedTables ct) {
        ct.getRtc().saveRegisterValueToCache(this.getRd(), this.ALU(ct).toUpperCase());
        ct.getRtc().drawToRegisterTable();
        return -1;
    }

}
