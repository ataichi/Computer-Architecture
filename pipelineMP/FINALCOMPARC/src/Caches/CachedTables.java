/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Caches;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author joechua
 */
public class CachedTables {

    private CodeTableCache ctc;
    private DataTableCache dtc;
    private OpcodeTableCache otc;
    private RegisterTableCache rtc;

    public CachedTables(DefaultTableModel codeTable,DefaultTableModel dataTable,DefaultTableModel opcodeTable,DefaultTableModel registerTable, int listSize) {
        ctc = new CodeTableCache(listSize, codeTable);
        dtc = new DataTableCache(dataTable);
        otc = new OpcodeTableCache(listSize, opcodeTable);
        rtc = new RegisterTableCache(registerTable);
        
    }

    /**
     * @return the ctc
     */
    public CodeTableCache getCtc() {
        return ctc;
    }

    /**
     * @param ctc the ctc to set
     */
    public void setCtc(CodeTableCache ctc) {
        this.ctc = ctc;
    }

    /**
     * @return the dtc
     */
    public DataTableCache getDtc() {
        return dtc;
    }

    /**
     * @param dtc the dtc to set
     */
    public void setDtc(DataTableCache dtc) {
        this.dtc = dtc;
    }

    /**
     * @return the otc
     */
    public OpcodeTableCache getOtc() {
        return otc;
    }

    /**
     * @param otc the otc to set
     */
    public void setOtc(OpcodeTableCache otc) {
        this.otc = otc;
    }

    /**
     * @return the rtc
     */
    public RegisterTableCache getRtc() {
        return rtc;
    }

    /**
     * @param rtc the rtc to set
     */
    public void setRtc(RegisterTableCache rtc) {
        this.rtc = rtc;
    }
    
    
    
}

