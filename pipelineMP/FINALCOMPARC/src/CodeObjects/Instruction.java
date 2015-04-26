/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeObjects;

import Caches.CachedTables;
import CodeObjects.IType.BEQ;
import CodeObjects.JType.J;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Evy
 */
public abstract class Instruction {

    protected int insNumber;
    protected String memAddrInHex;
    protected String aluoutput;


    public Instruction() {
        this.memAddrInHex = "";
        this.insNumber = 0;//changed here
        this.aluoutput="";
    }

    public abstract String ALU(CachedTables ct);

    public abstract int specialFunction(CachedTables ct);

    public boolean haveControlHazard() {
        if (this instanceof BEQ || this instanceof J) {
            return true;
        }
        return false;
    }

    public abstract boolean haveDataHazard(int rd);
//    public abstract int getRd();

    public String getMemAddrInHex() {
        return memAddrInHex;
    }

    public void setMemAddrInHex(String memAddrInHex) {
        this.memAddrInHex = memAddrInHex;
    }

    public int getInsNumber() {
        return insNumber;
    }

    public void setInsNumber(int insNumber) {
        this.insNumber = insNumber;
    }
         

    public String getAluoutput() {
        return aluoutput;
    }

    /**
     * @param aluoutput the aluoutput to set
     */
    public void setAluoutput(String aluoutput) {
        this.aluoutput = aluoutput;
    }
}
