/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TracingObejcts;

import Caches.CachedTables;
import CodeObjects.IType.BEQ;
import CodeObjects.IType.IType;
import CodeObjects.IType.SW;
import CodeObjects.Instruction;
import CodeObjects.JType.J;
import CodeObjects.RType.DDIV;
import CodeObjects.RType.RType;
import java.awt.Point;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author collins chua
 */
public class WB {
    private String affectedRegister="n/a";
    private Point position;
    private Instruction ins;
    /**
     * @return the affectedRegister
     */
    public String getAffectedRegister() {
        return affectedRegister;
    }

    /**
     * @param affectedRegister the affectedRegister to set
     */
    public void setAffectedRegister(String affectedRegister) {
        this.affectedRegister = affectedRegister;
    }
    public void writeback(Instruction ins, CachedTables ct){
        this.ins=ins;
        if(ins instanceof BEQ || ins instanceof SW || ins instanceof J){
            if(ins instanceof SW){
                ins.specialFunction(ct);
            }
            this.affectedRegister="n/a";  
        }
        else if(ins instanceof DDIV){
            this.affectedRegister="hi and lo"+"= "+ins.ALU(ct);
            ins.specialFunction(ct);
        }
        else{
            try{
                this.affectedRegister="R"+((RType)ins).getRd()+"= "+ins.ALU(ct);
            }catch(Exception e){
                this.affectedRegister="R"+((IType)ins).getRd()+"= "+ins.ALU(ct);
            }
            ins.specialFunction(ct);
        }
    }
    
        public void reWriteback( CachedTables ct){
        if(ins instanceof BEQ || ins instanceof SW || ins instanceof J){
            if(ins instanceof SW){
                ins.specialFunction(ct);
            }
            this.affectedRegister="n/a";  
        }
        else if(ins instanceof DDIV){
            this.affectedRegister="hi and lo"+"= "+ins.ALU(ct);
            ins.specialFunction(ct);
        }
        else{
            try{
                this.affectedRegister="R"+((RType)ins).getRd()+"= "+ins.ALU(ct);
            }catch(Exception e){
                this.affectedRegister="R"+((IType)ins).getRd()+"= "+ins.ALU(ct);
            }
            ins.specialFunction(ct);
        }
    }
    
 public void drawToMap(DefaultTableModel pipelinemapmodel){
        pipelinemapmodel.setValueAt("WB", this.position.y, this.position.x);
    }
    public void drawStall(DefaultTableModel pipelinemapmodel){
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
