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
public class RegisterTableRow {
     private String RegisterNumber;
    private String RegisterValue;

    public RegisterTableRow(String RegisterNumber, String RegisterValue) {
        this.RegisterNumber = RegisterNumber;
        this.RegisterValue = RegisterValue;
    }

    /**
     * @return the RegisterNumber
     */
    public String getRegisterNumber() {
        return RegisterNumber;
    }

    /**
     * @param RegisterNumber the RegisterNumber to set
     */
    public void setRegisterNumber(String RegisterNumber) {
        this.RegisterNumber = RegisterNumber;
    }

    /**
     * @return the RegisterValue
     */
    public String getRegisterValue() {
        return RegisterValue;
    }

    /**
     * @param RegisterValue the RegisterValue to set
     */
    public void setRegisterValue(String RegisterValue) {
        this.RegisterValue = RegisterValue;
    }
}
