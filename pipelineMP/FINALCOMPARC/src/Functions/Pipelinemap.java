/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Functions;

import Caches.CachedTables;
import CodeObjects.IType.IType;
import CodeObjects.IType.LW;
import CodeObjects.IType.LWU;
import CodeObjects.Instruction;
import CodeObjects.RType.RType;
import TracingObejcts.Cycle;
import TracingObejcts.EXMEM;
import TracingObejcts.IDEX;
import TracingObejcts.IFID;
import TracingObejcts.MEMWB;
import TracingObejcts.WB;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Evy
 */
public class Pipelinemap {

    /**
     * @param args the command line arguments
     */
    //private ArrayList<Instruction> instruction;
    private DefaultTableModel model;
    int isloadInstruction = 0; //load shoudl be checked twice
    int tempLoadRD = 0; //store load instruction rd for checking twice
    boolean isFirstInstruction = true; //first instruction don't need to be compared
    int tempInstructionNumber = -1; //when encounter branch store the pc, so that kknow where to go back
    int currentRownumber = 0;
    int beqDetectedRownumber = 0;
    int lastRd = 0;
    Point nextIF = new Point(1, 0);
    ArrayList<Cycle> cycles = new ArrayList();
    private CachedTables ct;
    private ArrayList<String> beqDestLabel;
    private int branchCountdown = -1;
    private int i; //this is to control the flow of the instructions
    private Instruction branchHolder;
    private ArrayList instList;
    private ArrayList<HazardTrigger> rdList;

    public Pipelinemap() {
        rdList = new ArrayList();
    }

    public Pipelinemap(DefaultTableModel dtm) {
        this.model = dtm;
        rdList = new ArrayList();
    }

    public int getCycleCount() {
        return this.cycles.size();
    }

    public void addPLMCol() {
//        System.out.println("testing colcount: "+(this.model.getColumnCount()+1));
        this.model.addColumn("" + this.model.getColumnCount() + "");
//        this.model.addColumn("col");

    }

    public Object getPLMValue(int row, int col) {
        //error chercing
        System.out.println("testing[getplmvalue] in getvalue:");
        return this.model.getValueAt(row, col);

        //return null;
    }

    public void setValue(String s, int row, int col) {
        this.model.setValueAt(s, row, col);
    }

    public void addPLMRow() {
        try {
            System.out.println("testing [addplmrow] is row " + this.nextIF.y + " col " + this.nextIF.x + " exists?");
            this.getPLMValue((this.nextIF.y), this.nextIF.x); //don't know error
        } catch (Exception e) {
            System.out.println("testing [addplmrow] need for more rows...");
            this.model.addRow(new Object[]{"a"});
        }

    }

    //instructions that is Not Branch 
//perform fetch decode execute
    public void processNBInstruction(Boolean stall, Instruction ins) {
        if (this.isFirstInstruction) {
            System.out.println("testing[processnbiinstruction] row:" + this.nextIF.y + " col" + this.nextIF.x);
//            System.out.println("testing[processnbiinstruction] nextif > " + this.getPLMValue(this.nextIF.y - 1, this.nextIF.x - 1).toString());
            //create five cycle for tracing
            for (int x = 0; x < 5; x++) {
                this.cycles.add(new Cycle());
            }
            System.out.println("testing[processnbiinstruction] add 1 row here");
            this.addPLMRow();
            traceNotbeqwithoutstall(ins);
        } else {
            System.out.println("testing[processnbiinstruction] not first time process");
            if (stall == true) {
                try {
                    this.getPLMValue((int) (this.nextIF.getX() + 8), this.nextIF.y); //don't know error
                } catch (Exception e) {
                    //lacking so add nine col
//                    System.out.println("testing[drawnbitomap] lacking col");
                    for (int x = 0; x < 4; x++) {
                        this.addPLMCol();
                        this.cycles.add(new Cycle());
                    }
                }
                System.out.println("testing[processnbiinstruction] add 1 row here");
                this.addPLMRow();
                traceNotbeqButstalled(ins);
            } else {
                try {
                    this.getPLMValue((int) (this.nextIF.getX() + 4), this.nextIF.y); //don't know error
                } catch (Exception f) {
                    //lacking so four nine col
//                for (int x = 0; x < 5; x++) {
//                    System.out.println("testing[drawnbitomap] lacking col for not stalled");
                    this.addPLMCol(); //add one because 4 already exist
//                }
                }
                System.out.println("testing[processnbiinstruction] add 1 row here");
                this.addPLMRow();
                this.cycles.add(new Cycle());
                traceNotbeqwithoutstall(ins);
            }
        }
        this.nextIF.x -= 4;
        this.nextIF.y++;
    }
    /* public void setInstructions(ArrayList<Instruction> i) {
     this.instruction = i; //or do a arraylist copy

     }*/

    //building pipeline map for single execution
    public void buildPipelineMapSingle(Instruction ins) {
        if (isFirstInstruction) {
            System.out.println("testing[buildpipelinemapsingle] first instruction");
            processNBInstruction(false, ins); // proceed to tracign and drawing of map
            isFirstInstruction = false;
            //checking if control hazard 
            if (ins.haveControlHazard()) {
                System.out.println("testing[buildpipelinemapsingle] control hazard detected...");
                //if beq or j ^ it will true
                this.branchCountdown = 3;
                this.branchHolder = ins;
                try {
                    //cast to beq
                } catch (Exception e) {
                    //cast to j
                }
            }
            pushRD(ins); //gets the last rd
        } else {
            //check for hazards
            System.out.println("testing[buildpipelinemapsingle] checking for hazard...");
            if (this.branchCountdown < 0) { // no determining of jump in progress
                System.out.println("testing[buildpipelinemapsingle] branchCountdown <0 no jump in progress...");
                if (this.checkDataHazard(ins)) { //changed here to accomodate the checkign olf laod instruction
                    System.out.println("testing[buildpipelinemapsingle] testing data hazard occured");
                    processNBInstruction(true, ins);
                    clearRD(); //disregard the checking of all above instructiom because if stalled the next instruction will never hit the mem
                } else {
                    System.out.println("testing[buildpipelinemapsingle] tesign hapy hapy");
                    processNBInstruction(false, ins);
                }
                //checking if control hazard 
                if (ins.haveControlHazard()) {
                    System.out.println("testing[buildpipelinemapsingle] control hazard detected...");
                    //if beq or j ^ it will true
                    this.branchCountdown = 3;
                    this.branchHolder = ins;
                    try {
                        //cast to beq
                        this.branchCountdown = 3;
                    } catch (Exception e) {
                        //cast to j
                    }
                }
                pushRD(ins); //gets the last rd
            } else if (this.branchCountdown > 0) {
                System.out.println("testing[buildpipelinemapsingle] jumpdetermining in progress...");
                determineAndFlushInstruction(ins);
                this.branchCountdown--;
            } else { //in wb already know where to jump na
                System.out.println("testing[buildpipelinemapsingle]  jumping...");
                this.branchCountdown = -1; //free the branch lock, no more countdown
                if (ins.getInsNumber() > this.branchHolder.specialFunction(ct)) { //destlabel is before current
                    this.nextIF.y = this.branchHolder.specialFunction(ct);
                    this.i = new Integer(this.nextIF.y);
                    ins = (Instruction) this.instList.get(i);
                } else {
                    int numSkippedInst = this.branchHolder.specialFunction(ct) - ins.getInsNumber();
                    for (int x = 0; x < numSkippedInst; x++) {
                        System.out.println("testing[testingbuildpipelinemapsingle] add 1 row here");
                        this.addPLMRow();
                        this.setValue(ct.getOtc().geOpcodeRow(ins.getInsNumber() + x).getInstruction(), this.nextIF.y, 0);
                        this.nextIF.y++;
                    }
                    this.i = this.branchHolder.specialFunction(ct);
                    ins = (Instruction) this.instList.get(i);
                    ins.setInsNumber(this.i);
                }
                //special case add cycle and col then the other one will be added noramlly
                for (int x = 0; x < 3; x++) {
                    this.addPLMCol();
                    this.cycles.add(new Cycle());
                }
                processNBInstruction(false, ins);
                if (ins.haveControlHazard()) {
                    System.out.println("testing[buildpipelinemapsingle] control hazard detected...");
                    //if beq or j ^ it will true
                    this.branchCountdown = 3;
                    this.branchHolder = ins;
                    try {
                        //cast to beq
                    } catch (Exception e) {
                        //cast to j
                    }
                }
                clearRD(); //disregard the checking of all above instruction
                pushRD(ins); //gets the last rd
            }
        }
    }

    public void buildPipelineMap(ArrayList instructionList, CachedTables ct, DefaultTableModel cycleModel, String hazardType) {
        this.ct = ct;
        this.instList = instructionList;
        switch (hazardType) {

            case "PipelineFlush":

                for (this.i = 0; this.i < instructionList.size(); this.i++) { //just move down till finish
                    System.out.println("testing[buildpipelinemapwhole] inst# " + this.i);
                    ((Instruction) instructionList.get(this.i)).setInsNumber(this.i);
                    buildPipelineMapSingle((Instruction) instructionList.get(this.i));
                }
                break;
            case "Pipeline2":
                for (this.i = 0; this.i < instructionList.size(); this.i++) { //just move down till finish
                    System.out.println("testing[buildpipelinemapwhole] inst# " + this.i);
                    ((Instruction) instructionList.get(this.i)).setInsNumber(this.i);
                    buildPipelineMapSinglePipeline2((Instruction) instructionList.get(this.i));
                }
                break;
        }
        //place here to retreive all cycles and print it to the tracing table
        System.out.println(
                "testing [buildpipelinemapwhole] " + this.cycles.get(0).getIfid().getIR());

        this.buildTracing(cycleModel); //takes the cycles and draws it
        //isloadInstruction=2
        //check if beq
        //set cond
        //perform beq

        //if loadinstruction!=0
        //compare with load
        //isloadinstruction--
    }

    public void reset() {
        isloadInstruction = 0; //load shoudl be checked twice
        tempLoadRD = 0; //store load instruction rd for checking twice
        isFirstInstruction = true; //first instruction don't need to be compared
        tempInstructionNumber = -1; //when encounter branch store the pc, so that kknow where to go back
        currentRownumber = 0;
        beqDetectedRownumber = 0;
    }

    private IFID fetch(Instruction ins, int cyclenum) {
        IFID ifid = new IFID();
//        ifid.setIR(ins.getOpcodeInHex());
//        ifid.setNPC(ins.getMemAddrInHex()); //temporary, should get from code segment
//        ifid.setPC(ins.getMemAddrInHex());
        System.out.println("testing[fetch0]: fetching... insNum: " + ins.getInsNumber());
        this.setValue(ct.getOtc().geOpcodeRow(ins.getInsNumber()).getInstruction(), this.nextIF.y, 0);
        System.out.println("testing[fetch0]: " + cyclenum + "cyclesize" + cycles.size());
        ifid.fetch(ins, ct);
        ifid.setPosition(new Point(this.nextIF.x, this.nextIF.y));
        this.nextIF.x++;
        ifid.drawToMap(this.model);
        System.out.println("testing[fetch1]: " + cyclenum + "cyclesize" + cycles.size());
        return ifid;
    }

    private IDEX decode(Instruction ins, int cyclenum) {
        IDEX idex = new IDEX();
        idex.decode(ins, ct);
        idex.setPosition(new Point(this.nextIF.x, this.nextIF.y));
        this.nextIF.x++;
        idex.drawToMap(this.model);
        return idex;
    }

    private EXMEM execute(Instruction ins, int cyclenum) {
        EXMEM exmem = new EXMEM();
        exmem.execute(ins, ct);
        exmem.setPosition(new Point(this.nextIF.x, this.nextIF.y));
        this.nextIF.x++;
        exmem.drawToMap(this.model);
        return exmem;
    }

    private MEMWB memory(Instruction ins, int cyclenum) {
        MEMWB memwb = new MEMWB();
        memwb.memory(ins, ct);
        memwb.setPosition(new Point(this.nextIF.x, this.nextIF.y));
        this.nextIF.x++;
        memwb.drawToMap(this.model);
        return memwb;
    }

    private WB write(Instruction ins, int cyclenum) {
        WB wb = new WB();
        wb.writeback(ins, ct);
        wb.setPosition(new Point(this.nextIF.x, this.nextIF.y));
        this.nextIF.x++;
        wb.drawToMap(this.model);
        return wb;
    }

    private void determineAndFlushInstruction(Instruction ins) {
        System.out.println("testing[determineAndFlushInstruction] add 1 row here");
        this.addPLMRow();
        int cyclenum = new Point(this.nextIF).x;
        cyclenum -= 1;
        System.out.println("testing[traceNotbeqwitouthstall] cyclesize" + cycles.size() + " cyclenum " + cyclenum);
        this.cycles.get(cyclenum).setIfid(fetch(ins, cyclenum++));
        System.out.println("testing[traceNotbeqwitouthstall] cyclenum.x " + cyclenum);
        this.nextIF.y++;
    }

    private void traceNotbeqwithoutstall(Instruction ins) {
        int cyclenum = new Point(this.nextIF).x;
        cyclenum -= 1;
        System.out.println("testing[traceNotbeqwitouthstall] cyclesize" + cycles.size() + " cyclenum " + cyclenum);
        this.cycles.get(cyclenum).setIfid(fetch(ins, cyclenum++));
        this.cycles.get(cyclenum).setIdex(decode(ins, cyclenum++));
        this.cycles.get(cyclenum).setExmem(execute(ins, cyclenum++));
        this.cycles.get(cyclenum).setMemwb(memory(ins, cyclenum++));
        System.out.println("testing[traceNotbeqwitouthstall] cyclenum.x " + cyclenum);
        this.cycles.get(cyclenum).setWb(write(ins, cyclenum));

    }

    private void traceNotbeqButstalled(Instruction ins) {
        int cyclenum = new Point(this.nextIF).x;
        //placing cyclenum.x++ in the parameter means passing cyclenum.x then when the function returns already,
//        then that is the time you increment

//        the print, test that if i alter the cyclenum.x it will not change the point.x
//        cyclenum.x++;
//        System.out.println("testing[processnbiinstruction] next if " + this.nextIF.x + " compared to cyclenum" + this.cyclenum.x); 
        cyclenum -= 1;
        System.out.println("testing[traceNotbeqbutstalled] cyclesize" + cycles.size() + " cyclenum " + cyclenum);
        this.cycles.get(cyclenum).setIfid(fetch(ins, cyclenum));
        for (int x = 0; x < 3; x++) {
            this.setValue("*", this.nextIF.y, this.nextIF.x);
            this.nextIF.x++;
        }
        cyclenum += 4;
        this.cycles.get(cyclenum).setIdex(decode(ins, cyclenum++));
        this.cycles.get(cyclenum).setExmem(execute(ins, cyclenum++));
        this.cycles.get(cyclenum).setMemwb(memory(ins, cyclenum++));
        System.out.println("testing[tracenotbeqbutstalled] cyclenum.x " + cyclenum);
        this.cycles.get(cyclenum).setWb(write(ins, cyclenum));

    }

    private void buildTracing(DefaultTableModel cycleModel) {
        int cycleSize;

        cycleSize = this.cycles.size();
        System.out.println(cycleSize);
        for (int i = 0; i < cycleSize; i++) {

            cycleModel.addColumn("Cycle " + (i + 1));

            try {
                cycleModel.setValueAt(this.cycles.get(i).getIfid().getIR(), 1, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getIfid().getNPC(), 2, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getIfid().getPC(), 3, i + 1);
            } catch (Exception e) {
                System.out.println("testing [buildTracing] no IF for cycle " + i + 1);
            }

            try {
                cycleModel.setValueAt(this.cycles.get(i).getIdex().getIR(), 5, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getIdex().getA(), 6, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getIdex().getB(), 7, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getIdex().getIMM(), 8, i + 1);
            } catch (Exception f) {
                System.out.println("testing [buildTracing] no ID for cycle " + i + 1);
            }

            try {
                cycleModel.setValueAt(this.cycles.get(i).getExmem().getIR(), 10, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getExmem().getALUOUTPUT(), 11, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getExmem().getB(), 12, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getExmem().getCond(), 13, i + 1);
            } catch (Exception g) {
                System.out.println("testing [buildTracing] no EX for cycle " + i + 1);
            }

            try {
                cycleModel.setValueAt(this.cycles.get(i).getMemwb().getIR(), 15, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getMemwb().getALUOUTPUT(), 16, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getMemwb().getLMD(), 17, i + 1);
                cycleModel.setValueAt(this.cycles.get(i).getMemwb().getMEM_ALUOUTPUT(), 18, i + 1);
            } catch (Exception h) {
                System.out.println("testing [buildTracing] no MEM for cycle " + i + 1);
            }

            try {
                cycleModel.setValueAt(this.cycles.get(i).getWb().getAffectedRegister(), 20, i + 1);
            } catch (Exception j) {
                System.out.println("testing [buildTracing] no WB for cycle " + i + 1);
            }
        }

    }

    public void clearRD() { //for beq flush sake because after jump sure there is no dependency
        for (int x = 0; x < this.rdList.size(); x++) {
            this.rdList.get(x).setValidity(0);
        }
    }

    public void pushRD(Instruction ins) {
        int tempRd = -1;
        try {
            tempRd = ((IType) ins).getRd();
        } catch (Exception e) {
            System.out.println("testing[buildpipelinemapsingle] oops.. not itype");
            try {
                tempRd = ((RType) ins).getRd();
            } catch (Exception f) {
                System.out.println("testing[buildpipelinemapsingle] jump or beq is detected");
            }
        }//note to not check for dependency in the succeeding
        if (tempRd == 0) {
            tempRd = -1;
        }

        //search for zero in the array
        try {
            traverse: //x<=rdList to trigger an exception if nothign is found.
            for (int x = 0; x <= this.rdList.size(); x++) {
                if (this.rdList.get(x).getValidity() == 0) {
                    //push to queue the rd of that instruction 
                    if (ins instanceof LW || ins instanceof LWU) {
                        this.rdList.get(x).setValidity(2);
                        this.rdList.get(x).setRd(tempRd);
                    } else {//for normal instrucion
                        this.rdList.get(x).setValidity(1);
                        this.rdList.get(x).setRd(tempRd);
                    }
                    break traverse;
                }
            }
//everything in the list are still utilized, create new one instead

        } catch (Exception e) {
            if (ins instanceof LW || ins instanceof LWU) {
                this.rdList.add(new HazardTrigger(2, tempRd));
            } else {//for normal instrucion
                this.rdList.add(new HazardTrigger(1, tempRd));
            }
        }

    }

    //building pipeline map for single execution
    public void buildPipelineMapSinglePipeline2(Instruction ins) {
        if (isFirstInstruction) {
            System.out.println("testing[buildpipelinemapsingle2] first instruction");
            processNBInstruction(false, ins); // proceed to tracign and drawing of map
            isFirstInstruction = false;
            //checking if control hazard 
            if (ins.haveControlHazard()) {
                System.out.println("testing[buildpipelinemapsingle2] control hazard detected...");
                //if beq or j ^ it will true
                this.branchCountdown = 0;
                this.branchHolder = ins;
                try {
                    //cast to beq
                } catch (Exception e) {
                    //cast to j
                }
            }
            pushRD(ins); //gets the last rd
        } else {
            //check for hazards
            System.out.println("testing[buildpipelinemapsingle2] checking for hazard...");
            if (this.branchCountdown < 0) { // no determining of jump in progress
                System.out.println("testing[buildpipelinemapsingle2] branchCountdown <0 no jump in progress...");
                if (this.checkDataHazard(ins)) { //changed here to accomodate the checkign olf laod instruction
                    System.out.println("testing[buildpipelinemapsingle2] testing data hazard occured");
                    processNBInstruction(true, ins);
                    clearRD(); //disregard the checking of all above instructiom because if stalled the next instruction will never hit the mem
                } else {
                    System.out.println("testing[buildpipelinemapsingle2] tesign hapy hapy");
                    processNBInstruction(false, ins);
                }
                //checking if control hazard 
                if (ins.haveControlHazard()) {
                    System.out.println("testing[buildpipelinemapsingle2] control hazard detected...");
                    //if beq or j ^ it will true
                    this.branchCountdown = 0;
                    this.branchHolder = ins; //takes note of the beq instance
                    try {
                        //cast to beq
                        this.branchCountdown = 0;
                    } catch (Exception e) {
                        //cast to j
                    }
                }
                pushRD(ins); //gets the last rd
            } //                else if (this.branchCountdown > 0) {
            //                System.out.println("testing[buildpipelinemapsingle2] jumpdetermining in progress...");
            //                determineAndFlushInstruction(ins);
            //                this.branchCountdown--;
            //            } 
            else { //in wb already know where to jump na
                System.out.println("testing[buildpipelinemapsingle2]  jumping... ");
                System.out.println("testing[buildpipelinemapsingle2]  specialfunc: " + this.branchHolder.specialFunction(ct) + "ins num+1 =" + (ins.getInsNumber() + 1));
                this.branchCountdown = -1; //free the branch lock, no more countdown
                if (ins.getInsNumber() == this.branchHolder.specialFunction(ct)) { //destlabel is before current
                    System.out.println("testing[testingbuildpipelinemapsingle2] not jumping");
                    int numSkippedInst = this.branchHolder.specialFunction(ct) - ins.getInsNumber();
                    for (int x = 0; x < numSkippedInst; x++) {
                        System.out.println("testing[testingbuildpipelinemapsingle2] add 1 row here");
                        this.addPLMRow();
                        this.setValue(ct.getOtc().geOpcodeRow(ins.getInsNumber() + x).getInstruction(), this.nextIF.y, 0);
                        this.nextIF.y++;
                    }
                    this.i = this.branchHolder.specialFunction(ct);
                    ins = (Instruction) this.instList.get(i);
                    ins.setInsNumber(this.i);

                    if (this.checkDataHazard(ins)) { //changed here to accomodate the checkign olf laod instruction
                        System.out.println("testing[buildpipelinemapsingle2] testing data hazard occured");
                        processNBInstruction(true, ins);
                        clearRD(); //disregard the checking of all above instructiom because if stalled the next instruction will never hit the mem
                    } else {
                        System.out.println("testing[buildpipelinemapsingle2] tesign hapy hapy");
                        processNBInstruction(false, ins);
                    }
                } else { //jump is below 
                    System.out.println("testing[testingbuildpipelinemapsingle2] jump taken");
                    determineAndFlushInstruction(ins);
                    int numSkippedInst = this.branchHolder.specialFunction(ct) - ins.getInsNumber();
                    for (int x = 0; x < numSkippedInst; x++) {
                        System.out.println("testing[testingbuildpipelinemapsingle2] add 1 row here");
                        this.addPLMRow();
                        this.setValue(ct.getOtc().geOpcodeRow(ins.getInsNumber() + x).getInstruction(), this.nextIF.y, 0);
                        this.nextIF.y++;
                    }
                    this.i = this.branchHolder.specialFunction(ct);
                    ins = (Instruction) this.instList.get(i);
                    ins.setInsNumber(this.i);
                    this.addPLMCol();
                    this.cycles.add(new Cycle());

                    processNBInstruction(false, ins);
                }
//                //special case add cycle and col then the other one will be added noramlly
//                for (int x = 0; x < 3; x++) {
//                    this.addPLMCol();
//                    this.cycles.add(new Cycle());
//                }

                if (ins.haveControlHazard()) {
                    System.out.println("testing[buildpipelinemapsingle2] control hazard detected...");
                    //if beq or j ^ it will true
                    this.branchCountdown = 0;
                    this.branchHolder = ins;
                    try {
                        //cast to beq
                    } catch (Exception e) {
                        //cast to j
                    }
                }
                pushRD(ins); //gets the last rd
            }
        }
    }

    public boolean checkDataHazard(Instruction ins) {
        System.out.println("testing [@checkingDataHazard] rdlist size " + this.rdList.size());
        for (int x = 0; x < this.rdList.size(); x++) {
            System.out.println("testing [@checkingDataHazard] checking x's validity:" + this.rdList.get(x).getValidity());
            if (this.rdList.get(x).getValidity() > 0) {
                int tempvalidity = this.rdList.get(x).getValidity();
                this.rdList.get(x).setValidity(tempvalidity - 1);
                int temprd = this.rdList.get(x).getRd();
                System.out.println("testing [@checkingDataHazard] deducted the validity:" + this.rdList.get(x).getValidity());
                System.out.println("testing [@checkingDataHazard] rd in the list:" + temprd);
                if (ins.haveDataHazard(temprd)) {
                    return true;
                }
            }

        }
        return false;
    }

    //clears the  table drawing
    public void clearTable(DefaultTableModel model) {
        for (int y = 0; y < model.getRowCount(); y++) {
            for (int x = 1; x < model.getColumnCount(); x++) {
                model.setValueAt("", y, x);
            }
        }
    }

//    //clears the pipelinemap table drawing
//    public void clearPipelineMapTable(DefaultTableModel pipelinemapModel){
//        for(int y=1; y<pipelinemapModel.getRowCount(); y++){
//            for(int x=1; x<pipelinemapModel.getColumnCount(); x++){
//                pipelinemapModel.setValueAt("", y, x);
//            }
//        }
//    }
//    //clears the tracing table drawing
//    public void clearPipelineTracingTable(DefaultTableModel pipelinemapModel){
//        for(int y=1; y<pipelinemapModel.getRowCount(); y++){
//            for(int x=1; x<pipelinemapModel.getColumnCount(); x++){
//                pipelinemapModel.setValueAt("", y, x);
//            }
//        }
//    }
    public void runCycle(int i, CachedTables ct, DefaultTableModel cycleModel) {
        try {
            this.cycles.get(i).getWb().reWriteback(ct);
            this.cycles.get(i).getWb().drawToMap(this.model);

            //draw the table of wb
            cycleModel.setValueAt(this.cycles.get(i).getWb().getAffectedRegister(), 20, i + 1);
        } catch (Exception e) {
            System.out.println("testing [runcycle] no writeback should exist at cycle " + (i + 1) + "!");
        }

        try {
            this.cycles.get(i).getMemwb().reMemory(ct);
            this.cycles.get(i).getMemwb().drawToMap(this.model);

            //drawing of tracing of mem
            cycleModel.setValueAt(this.cycles.get(i).getMemwb().getIR(), 15, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getMemwb().getALUOUTPUT(), 16, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getMemwb().getLMD(), 17, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getMemwb().getMEM_ALUOUTPUT(), 18, i + 1);
        } catch (Exception e) {
            System.out.println("testing [runcycle] no mem should exist at cycle " + (i + 1) + "!");
        }

        try {
            this.cycles.get(i).getExmem().reExecute(ct);
            this.cycles.get(i).getExmem().drawToMap(this.model);

            //drawing of the tracing of execute
            cycleModel.setValueAt(this.cycles.get(i).getExmem().getIR(), 10, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getExmem().getALUOUTPUT(), 11, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getExmem().getB(), 12, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getExmem().getCond(), 13, i + 1);

        } catch (Exception e) {
            System.out.println("testing [runcycle] no exec should exist at cycle " + (i + 1) + "!");
        }

        try {
            this.cycles.get(i).getIdex().reDecode(ct);
            this.cycles.get(i).getIdex().drawToMap(this.model);

            //drawing the tracing of decode
            cycleModel.setValueAt(this.cycles.get(i).getIdex().getIR(), 5, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getIdex().getA(), 6, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getIdex().getB(), 7, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getIdex().getIMM(), 8, i + 1);
        } catch (Exception e) {
            System.out.println("testing [runcycle] no decode should exist at cycle " + (i + 1) + "!");
        }

        try {
            this.cycles.get(i).getIfid().reFetch(ct);
            this.cycles.get(i).getIfid().drawToMap(this.model);

            //draw the tracing of fetch
            cycleModel.setValueAt(this.cycles.get(i).getIfid().getIR(), 1, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getIfid().getNPC(), 2, i + 1);
            cycleModel.setValueAt(this.cycles.get(i).getIfid().getPC(), 3, i + 1);
        } catch (Exception e) {
            System.out.println("testing [runcycle] no fetch should exist at cycle " + (i + 1) + "!");
        }
    }
}
