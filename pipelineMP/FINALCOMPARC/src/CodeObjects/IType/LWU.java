/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeObjects.IType;

import Caches.CachedTables;
import java.math.BigInteger;
import Functions.Usable;
import UI.FRAME1;
import javax.swing.JOptionPane;

/**
 *
 * @author joechua
 */
public class LWU extends IType {

    private Usable usable = new Usable();

    public LWU(String addr, int rd, int rs, int rt, String immORoffset) {
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
        String lALU = null;
        String offsetTemp = null;
        long rs, rt, offset, ans;
        int value;
       // int address;

        offsetTemp = ct.getOtc().geOpcodeRow(this.insNumber).getImm();
        offset = Long.parseLong(offsetTemp, 2);
        rs = Long.parseLong(ct.getRtc().getRegisterRow(this.rs), 16);
        offset = offset + rs;
        System.out.println("ans: " + offset);

        offsetTemp = Long.toBinaryString(offset);
        BigInteger binaryOp = new BigInteger(offsetTemp, 2);
        offsetTemp = binaryOp.toString(16).toUpperCase();

        System.out.println("hex: " + offsetTemp);

        if (new FRAME1().isINVALIDinLW(offsetTemp)) {
            JOptionPane.showMessageDialog(new FRAME1(), "LOADING ADDRESS IS NOT AVAILABLE!", "Error", JOptionPane.ERROR_MESSAGE);

        } else {

            value = ct.getDtc().findAddrLocation(offsetTemp);
            lALU = ct.getDtc().getMemoryCacheContents(value);
            System.out.println("valueh: " + lALU);

            lALU = lALU.toUpperCase();
            lALU = usable.hexToNbit(lALU, 16);

        }
        System.out.println("value: " + lALU);

        return lALU;
    }

    @Override
    public int specialFunction(CachedTables ct) {
        ct.getRtc().saveRegisterValueToCache(this.getRd(), this.ALU(ct).toUpperCase());
        ct.getRtc().drawToRegisterTable();
        return -1;
    }

}
