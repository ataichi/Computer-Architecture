/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TracingObejcts;

import Caches.CachedTables;
import CodeObjects.IType.BEQ;
import CodeObjects.Instruction;
import CodeObjects.JType.J;
import java.awt.Point;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author collins chua
 */
public class EXMEM {

    private String IR;
    private String ALUOUTPUT = "";
    private String B = "";
    private int cond;
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
     * @return the B
     */
    public String getB() {
        return B;
    }

    /**
     * @param B the B to set
     */
    public void setB(String B) {
        this.B = B;
    }

    /**
     * @return the cond
     */
    public int getCond() {
        return cond;
    }

    /**
     * @param cond the cond to set
     */
    public void setCond(int cond) {
        this.cond = cond;
    }

    public void execute(Instruction ins, CachedTables ct) {
        this.ins=ins;
        this.IR = ct.getOtc().geOpcodeRow(ins.getInsNumber()).getOpcode();
        int b = Integer.parseInt(ct.getOtc().geOpcodeRow(ins.getInsNumber()).getB().toString(), 2);
        this.B = ct.getRtc().getRegisterRow(b).toString();
        this.ALUOUTPUT = ins.ALU(ct);
//        this.ALUOUTPUT=ins.getAluoutput();
        try {
            this.cond = ((BEQ) ins).isCond();
        } catch (Exception e) {
            //baka jump
            try {
                ((J) ins).getInsNumber();
                this.cond = 1;
            } catch (Exception f) {
                //not beq
                this.cond = 0;
            }
        }

    }

    public void reExecute(CachedTables ct) {
        this.IR = ct.getOtc().geOpcodeRow(ins.getInsNumber()).getOpcode();
        int b = Integer.parseInt(ct.getOtc().geOpcodeRow(ins.getInsNumber()).getB().toString(), 2);
        this.B = ct.getRtc().getRegisterRow(b).toString();
        this.ALUOUTPUT = ins.ALU(ct);
//        this.ALUOUTPUT=ins.getAluoutput();
        try {
            this.cond = ((BEQ) ins).isCond();
        } catch (Exception e) {
            //baka jump
            try {
                ((J) ins).getInsNumber();
                this.cond = 1;
            } catch (Exception f) {
                //not beq
                this.cond = 0;
            }
        }

    }
    
    public void drawToMap(DefaultTableModel pipelinemapmodel) {
        pipelinemapmodel.setValueAt("EX", this.getPosition().y, this.getPosition().x);
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
