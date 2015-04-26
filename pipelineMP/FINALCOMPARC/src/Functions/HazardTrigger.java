/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Functions;

/**
 *
 * @author kimberly
 */
public class HazardTrigger {
    private int validity=0;
    private int rd=0;

    /**
     * @return the validity
     */
    public HazardTrigger(int validity, int rd){
        this.validity=validity;
        this.rd=rd;
    }
    
    public int getValidity() {
        return validity;
    }

    /**
     * @param validity the validity to set
     */
    public void setValidity(int validity) {
        this.validity = validity;
    }

    /**
     * @return the rd
     */
    public int getRd() {
        return rd;
    }

    /**
     * @param rd the rd to set
     */
    public void setRd(int rd) {
        this.rd = rd;
    }
}
