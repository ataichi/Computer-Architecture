/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodeObjects.IType;

import CodeObjects.Instruction;
import CodeObjects.RType.RType;

/**
 *
 * @author joechua
 */
public abstract class IType extends RType{
    private String immORoffset;

    public IType(String addr, int rd, int rs, int rt, String immORoffset) {
        super(addr, rd, rs, rt);
        this.immORoffset=immORoffset;
    }
    
    /**
     * @return the immORoffset
     */
    public String getImmORoffset() {
        return immORoffset;
    }

    /**
     * @param immORoffset the immORoffset to set
     */
    public void setImmORoffset(String immORoffset) {
        this.immORoffset = immORoffset;
    }

    
}
