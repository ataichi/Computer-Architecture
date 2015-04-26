/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TracingObejcts;

/**
 *
 * @author collins chua
 */
public class Cycle {
    private IFID ifid;
    private IDEX idex;
    private EXMEM exmem;
    private MEMWB memwb;
    private WB wb;
    public Cycle(){
        
    }

    /**
     * @return the ifid
     */
    public IFID getIfid() {
        return ifid;
    }

    /**
     * @param ifid the ifid to set
     */
    public void setIfid(IFID ifid) {
        this.ifid = ifid;
    }

    /**
     * @return the idex
     */
    public IDEX getIdex() {
        return idex;
    }

    /**
     * @param idex the idex to set
     */
    public void setIdex(IDEX idex) {
        this.idex = idex;
    }

    /**
     * @return the exmem
     */
    public EXMEM getExmem() {
        return exmem;
    }

    /**
     * @param exmem the exmem to set
     */
    public void setExmem(EXMEM exmem) {
        this.exmem = exmem;
    }

    /**
     * @return the memwb
     */
    public MEMWB getMemwb() {
        return memwb;
    }

    /**
     * @param memwb the memwb to set
     */
    public void setMemwb(MEMWB memwb) {
        this.memwb = memwb;
    }

    /**
     * @return the wb
     */
    public WB getWb() {
        return wb;
    }

    /**
     * @param wb the wb to set
     */
    public void setWb(WB wb) {
        this.wb = wb;
    }
    
}
