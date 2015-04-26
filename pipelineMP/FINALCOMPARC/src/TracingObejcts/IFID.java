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
public class IFID {

    private String IR;
    private String NPC;
    private String PC;
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
     * @return the NPC
     */
    public String getNPC() {
        return NPC;
    }

    /**
     * @param NPC the NPC to set
     */
    public void setNPC(String NPC) {
        this.NPC = NPC;
    }

    /**
     * @return the PC
     */
    public String getPC() {
        return PC;
    }

    /**
     * @param PC the PC to set
     */
    public void setPC(String PC) {
        this.PC = PC;
    }

    public void fetch(Instruction ins, CachedTables ct) {
        this.ins=ins;
        System.out.println("testing [IFID.fetch]... ");
        this.IR = ct.getOtc().geOpcodeRow(ins.getInsNumber()).getOpcode();
        try {
            this.PC = new Usable().hexToNbit(ct.getCtc().getCodeLine(ins.getInsNumber() + 1).getAddress().toString(), 16);
            this.NPC = new Usable().hexToNbit(ct.getCtc().getCodeLine(ins.getInsNumber() + 1).getAddress().toString(), 16);
            System.out.println("testing [IFID.fetch] pc/npc: " + this.NPC);
        } catch (Exception e) {
            long templong = Long.parseLong(ct.getCtc().getCodeLine(ins.getInsNumber()).getAddress().toString(), 16) + 4;
            System.out.println("testing IFID.fetch ¬exception templong: " + templong);
            String tempaddress = Long.toBinaryString(templong);
            BigInteger binaryOpcode = new BigInteger(tempaddress, 2);
            String opcode = binaryOpcode.toString(16);
            String hex = new Usable().hexToNbit(opcode, 16);
            System.out.println("testing [IFID.fetch] " + hex);
            this.PC = hex;
            this.NPC = hex;
        }
    }

        public void reFetch(CachedTables ct) {
        System.out.println("testing [IFID.refetch]... ");
        this.IR = ct.getOtc().geOpcodeRow(this.ins.getInsNumber()).getOpcode();
        try {
            this.PC = new Usable().hexToNbit(ct.getCtc().getCodeLine(this.ins.getInsNumber() + 1).getAddress().toString(), 16);
            this.NPC = new Usable().hexToNbit(ct.getCtc().getCodeLine(this.ins.getInsNumber() + 1).getAddress().toString(), 16);
            System.out.println("testing [IFID.refetch] pc/npc: " + this.NPC);
        } catch (Exception e) {
            long templong = Long.parseLong(ct.getCtc().getCodeLine(this.ins.getInsNumber()).getAddress().toString(), 16) + 4;
            System.out.println("testing IFID.refetch ¬exception templong: " + templong);
            String tempaddress = Long.toBinaryString(templong);
            BigInteger binaryOpcode = new BigInteger(tempaddress, 2);
            String opcode = binaryOpcode.toString(16);
            String hex = new Usable().hexToNbit(opcode, 16);
            System.out.println("testing [IFID.refetch] " + hex);
            this.PC = hex;
            this.NPC = hex;
        }
    }
        
    public void drawToMap(DefaultTableModel pipelinemapmodel) {
        System.out.println("testing [drawtomap] " + pipelinemapmodel.getValueAt(0, 0));
        System.out.println("testing [drawtomap] row " + this.position.y + " col " + this.position.x);

        pipelinemapmodel.setValueAt("IF", this.position.y, this.position.x);
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
