/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TracingObejcts;

import Caches.CachedTables;
import CodeObjects.IType.LW;
import CodeObjects.IType.LWU;
import CodeObjects.IType.SW;
import CodeObjects.Instruction;
import Functions.Usable;
import java.awt.Point;
import java.math.BigInteger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author collins chua
 */
public class MEMWB {

    private String IR;
    private String ALUOUTPUT = "";
    private String LMD = "";
    private String MEM_ALUOUTPUT = "";
    private Point position;
    private Instruction ins;
    
    /**
     * @return the IR
     */
    public String getIR() {
        return IR;
    }

    /**
     * @param IR the IR to set
     */
    public void setIR(String IR) {
        this.IR = IR;
    }

    /**
     * @return the ALUOUTPUT
     */
    public String getALUOUTPUT() {
        return ALUOUTPUT;
    }

    /**
     * @param ALUOUTPUT the ALUOUTPUT to set
     */
    public void setALUOUTPUT(String ALUOUTPUT) {
        this.ALUOUTPUT = ALUOUTPUT;
    }

    /**
     * @return the LMD
     */
    public String getLMD() {
        return LMD;
    }

    /**
     * @param LMD the LMD to set
     */
    public void setLMD(String LMD) {
        this.LMD = LMD;
    }

    /**
     * @return the MEM_ALUOUTPUT
     */
    public String getMEM_ALUOUTPUT() {
        return MEM_ALUOUTPUT;
    }

    /**
     * @param MEM_ALUOUTPUT the MEM_ALUOUTPUT to set
     */
    public void setMEM_ALUOUTPUT(String MEM_ALUOUTPUT) {
        this.MEM_ALUOUTPUT = MEM_ALUOUTPUT;
    }

    public void memory(Instruction ins, CachedTables ct) {
        this.ins=ins;
        this.LMD = "N/A";
        this.ALUOUTPUT = ins.ALU(ct);
        this.IR = ct.getOtc().geOpcodeRow(ins.getInsNumber()).getOpcode();
        if (ins instanceof LW || ins instanceof LWU) {
            this.LMD = this.ALUOUTPUT;
            ins.specialFunction(ct);
        }
        if (ins instanceof SW) {
            int b = Integer.parseInt(ct.getOtc().geOpcodeRow(ins.getInsNumber()).getB().toString(), 2);
//                Long temp = Long.parseLong(ct.getRtc().getRegisterRow(b).toString(),16);
//                String sTemp = temp.toString();
//                BigInteger binaryOp = new BigInteger(sTemp, 2);
//                this.MEM_ALUOUTPUT= new Usable().hexToNbit(binaryOp.toString(), 16);
            this.MEM_ALUOUTPUT = ct.getRtc().getRegisterRow(b).toString();
        } else {
            this.MEM_ALUOUTPUT = "n/a";
        }
    }

    public void reMemory( CachedTables ct) {
        this.LMD = "N/A";
        this.ALUOUTPUT = ins.ALU(ct);
        this.IR = ct.getOtc().geOpcodeRow(ins.getInsNumber()).getOpcode();
        if (ins instanceof LW || ins instanceof LWU) {
            this.LMD = this.ALUOUTPUT;
            ins.specialFunction(ct);
        }
        if (ins instanceof SW) {
            int b = Integer.parseInt(ct.getOtc().geOpcodeRow(ins.getInsNumber()).getB().toString(), 2);
//                Long temp = Long.parseLong(ct.getRtc().getRegisterRow(b).toString(),16);
//                String sTemp = temp.toString();
//                BigInteger binaryOp = new BigInteger(sTemp, 2);
//                this.MEM_ALUOUTPUT= new Usable().hexToNbit(binaryOp.toString(), 16);
            this.MEM_ALUOUTPUT = ct.getRtc().getRegisterRow(b).toString();
        } else {
            this.MEM_ALUOUTPUT = "n/a";
        }
    }
    
    public void drawToMap(DefaultTableModel pipelinemapmodel) {
        pipelinemapmodel.setValueAt("MEM", this.getPosition().y, this.getPosition().x);
    }

    public void drawStall(DefaultTableModel pipelinemapmodel) {
        pipelinemapmodel.setValueAt("*", this.getPosition().y, this.getPosition().x);
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Point position) {
        this.position = position;
    }
}
