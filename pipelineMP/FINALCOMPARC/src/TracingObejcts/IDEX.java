/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TracingObejcts;

import Caches.CachedTables;
import CodeObjects.Instruction;
import Functions.Usable;
import java.awt.Point;
import java.math.BigInteger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author collins chua
 */
public class IDEX {

    private String IR = "";
    private String A = "";
    private String B = "";
    private String IMM = "";
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
     * @return the A
     */
    public String getA() {
        return A;
    }

    /**
     * @param A the A to set
     */
    public void setA(String A) {
        this.A = A;
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
     * @return the IMM
     */
    public String getIMM() {
        return IMM;
    }

    /**
     * @param IMM the IMM to set
     */
    public void setIMM(String IMM) {
        this.IMM = IMM;
    }

    public void decode(Instruction ins, CachedTables ct) {
        this.ins=ins;
        System.out.println("testing [IDEX.decode]... ");
        
        //added this line to ensure that the data will always be fresh
        ct.getRtc().refreshRegisterCacheArray(); 
        
        this.IR = ct.getOtc().geOpcodeRow(ins.getInsNumber()).getOpcode();
        int a = Integer.parseInt(ct.getOtc().geOpcodeRow(ins.getInsNumber()).getA().toString(), 2);
        this.A = ct.getRtc().getRegisterRow(a).toString();
        System.out.println("testing [IDEX.decode] A: " + this.A);
        int b = Integer.parseInt(ct.getOtc().geOpcodeRow(ins.getInsNumber()).getB().toString(), 2);
        this.B = ct.getRtc().getRegisterRow(b).toString();
        this.IMM = ct.getOtc().geOpcodeRow(ins.getInsNumber()).getImm().toString();
        BigInteger binaryOp = new BigInteger(this.IMM, 2);
        this.IMM = binaryOp.toString(16);
        this.IMM = new Usable().hexToNbit(this.IMM, 16);
    }

    public void reDecode( CachedTables ct) {
        System.out.println("testing [IDEX.redecode]... ");
        
        //added this line to ensure that the data will always be fresh
        ct.getRtc().refreshRegisterCacheArray(); 
        
        this.IR = ct.getOtc().geOpcodeRow(ins.getInsNumber()).getOpcode();
        int a = Integer.parseInt(ct.getOtc().geOpcodeRow(ins.getInsNumber()).getA().toString(), 2);
        this.A = ct.getRtc().getRegisterRow(a).toString();
        System.out.println("testing [IDEX.redecode] A: " + this.A);
        int b = Integer.parseInt(ct.getOtc().geOpcodeRow(ins.getInsNumber()).getB().toString(), 2);
        this.B = ct.getRtc().getRegisterRow(b).toString();
        this.IMM = ct.getOtc().geOpcodeRow(ins.getInsNumber()).getImm().toString();
        BigInteger binaryOp = new BigInteger(this.IMM, 2);
        this.IMM = binaryOp.toString(16);
        this.IMM = new Usable().hexToNbit(this.IMM, 16);
    }
    
    public void drawToMap(DefaultTableModel pipelinemapmodel) {
        pipelinemapmodel.setValueAt("ID", this.position.y, this.position.x);
    }

    public void drawStall(DefaultTableModel pipelinemapmodel) {
        pipelinemapmodel.setValueAt("*", this.position.y, this.position.x);
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
