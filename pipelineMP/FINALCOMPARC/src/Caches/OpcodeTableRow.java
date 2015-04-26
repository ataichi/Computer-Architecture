/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caches;

import java.util.ArrayList;

/**
 *
 * @author joechua
 */
public class OpcodeTableRow {
     private String instruction;
    private String opcode;
    private String ir05;
    private String a;

    public OpcodeTableRow(String instruction, String opcode, String ir05, String a, String b, String imm) {
        this.instruction = instruction;
        this.opcode = opcode;
        this.ir05 = ir05;
        this.a = a;
        this.b = b;
        this.imm = imm;
    }
    private String b;
    private String imm;

    /**
     * @return the instruction
     */
    public String getInstruction() {
        return instruction;
    }

    /**
     * @param instruction the instruction to set
     */
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    /**
     * @return the opcode
     */
    public String getOpcode() {
        return opcode;
    }

    /**
     * @param opcode the opcode to set
     */
    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    /**
     * @return the ir05
     */
    public String getIr05() {
        return ir05;
    }

    /**
     * @param ir05 the ir05 to set
     */
    public void setIr05(String ir05) {
        this.ir05 = ir05;
    }

    /**
     * @return the a
     */
    public String getA() {
        return a;
    }

    /**
     * @param a the a to set
     */
    public void setA(String a) {
        this.a = a;
    }

    /**
     * @return the b
     */
    public String getB() {
        return b;
    }

    /**
     * @param b the b to set
     */
    public void setB(String b) {
        this.b = b;
    }

    /**
     * @return the imm
     */
    public String getImm() {
        return imm;
    }

    /**
     * @param imm the imm to set
     */
    public void setImm(String imm) {
        this.imm = imm;
    }
}
