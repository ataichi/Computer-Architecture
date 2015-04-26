/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeObjects.JType;

import Caches.CachedTables;
import CodeObjects.Instruction;
import Functions.Usable;
import java.math.BigInteger;

/**
 *
 * @author joechua
 */
public class J extends Instruction {

    private Usable usable = new Usable();
    private String destLabel;

    public J(String addr, String destlabel) {
      
        this.destLabel = destlabel;
    }

    @Override
    public boolean haveDataHazard(int rd) {
        return false;
    }

   
    @Override
    public String ALU(CachedTables ct) {
        String result = "";
        String sIMM;
        sIMM = ct.getOtc().geOpcodeRow(this.getInsNumber()).getImm() + "00";
        result = sIMM.substring(2); //remove first two "00"
        BigInteger binaryOp2 = new BigInteger(result, 2);
        result = binaryOp2.toString(16);
        result = usable.hexToNbit(result, 16);
        return result;
    }

    @Override
    public int specialFunction(CachedTables ct) {
        //never mawawala sa list kasi nasa error label check from da start.
        String destination;
        destination = this.destLabel;
        for (int i = 0; i <= 2047; i++) {
            if (ct.getCtc().getCodeLine(i).getLabel().equals(destination)) {
                return i; //MATCHY MATCHY RETURN INDEX KUNG NASAN SI LABEL.
            }
        }
        return -1;
    }

    public String getDestLabel() {
        return destLabel;
    }

}
