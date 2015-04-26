/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Functions;

import java.awt.event.ItemEvent;
import java.math.BigDecimal;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author nievabri
 */
public class Usable {

    public String chopHexStringToBinary(String hex, int N1, int N2) {
        String result = "";
        hex = hexStringToNBitBinary(hex, 32);
        for (int i = N1; i <= N2; i++) {
            result = result + hex.charAt(i);
        }
        return result;
    }

    public class MyInputVerifier extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            //boolean match = text.matches("[a-zA-Z0-9]+");
            String text = ((JTextField) input).getText();
            try {
                BigDecimal value = new BigDecimal(text);
                return (value.scale() <= Math.abs(4));
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    public String hexToNbit(String hex, int N) {
        int i;
        int length = hex.length();
        if (length == N) {
            return hex;
        } else if (length < N) {
            int difference = N - length;
            char[] buffer = new char[N]; //allocate a new char buffer of the desired length 
            for (i = 0; i < difference; i++) {
                buffer[i] = '0'; //fill in the needed number of leading zeros
            }
            hex.getChars(0, length, buffer, difference); //copies the original binary string into the buffer after leading zeros
            return new String(buffer);
        }
        return hex;
    }

    
    public String binaryToNbitSigned(String hex, int N) {
        int i;
        int length = hex.length();
        if (length == N) {
            return hex;
        } else if (length < N) {
            int difference = N - length;
            char[] buffer = new char[N]; //allocate a new char buffer of the desired length 
            for (i = 0; i < difference; i++) {
                buffer[i] = 'F'; //fill in the needed number of leading zeros
            }
            hex.getChars(0, length, buffer, difference); //copies the original binary string into the buffer after leading zeros
            return new String(buffer);
        }
        return hex;
    }
    
    public String binaryToNbit(String binary, int N) {
        int i;
        int length = binary.length();
        if (length == N) {
            return binary;
        } else if (length < N) {
            int difference = N - length;
            char[] buffer = new char[N]; //allocate a new char buffer of the desired length 
            for (i = 0; i < difference; i++) {
                buffer[i] = '0'; //fill in the needed number of leading zeros
            }
            binary.getChars(0, length, buffer, difference); //copies the original binary string into the buffer after leading zeros
            return new String(buffer);
        }
        return binary;
    }
    
   public String toBinary(long decimalResult, int N) {
        int i;
        String binary = Long.toBinaryString(decimalResult); //gives us a binary string, but is missing leading zeros
        //System.out.println("#testing2" + binary);
        int length = binary.length();
        if (length == N) {
            return binary;
        } else if (length < N) {
            int difference = N - length;
            char[] buffer = new char[N]; //allocate a new char buffer of the desired length 
            for (i = 0; i < difference; i++) {
                buffer[i] = '0'; //fill in the needed number of leading zeros
            }
            binary.getChars(0, length, buffer, difference); //copies the original binary string into the buffer after leading zeros
            return new String(buffer);
        } else {
            throw new IllegalArgumentException("check if parameter is LONG data type!");
        }
    }
   
    public String hexStringToNBitBinary(String hexString, int N) {
        long decimalResult = 0;
        int length = hexString.length(); //store the length in memory
        int i;

        for (i = 0; i < length; i++) {
            //equivalent to multiplying the result by 16 and adding the value of the new digit, but uses bit operations for performance
            decimalResult = (decimalResult << 4) | Character.digit(hexString.charAt(i), 16);
        }
        String binary = Long.toBinaryString(decimalResult); //gives us a binary string, but is missing leading zeros
        length = binary.length();
        if (length == N) {
            return binary;
        } else if (length < N) {
            int difference = N - length;
            char[] buffer = new char[N]; //allocate a new char buffer of the desired length 
            for (i = 0; i < difference; i++) {
                buffer[i] = '0'; //fill in the needed number of leading zeros
            }
            binary.getChars(0, length, buffer, difference); //copies the original binary string into the buffer after leading zeros
            return new String(buffer);
        } else {
            throw new IllegalArgumentException("Hex String is not a N bit number!");
        }
    }

}
