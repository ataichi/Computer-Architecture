package UI;

import splashdemo.MIPSSplash;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import javax.swing.*;
import static splashdemo.MIPSSplash.mySplash;

public class MyPanel extends JPanel {

    private String immORlabel;
    private long nREG1;
    private long nREG2;
    private long nREG3;
    
    private JComboBox loadstoreCBOX;
    private JLabel titleLABEL;
    private JComboBox instructiontypeCBOX;
    private JLabel typeLABEL;
    private JComboBox arithmeticCBOX;
    private JComboBox logicalCBOX;
    private JComboBox transferCBOX;
    private JComboBox setCBOX;
    private JComboBox shiftCBOX;
    private JList codeLIST;
    private JComboBox reglist1;
    private JComboBox reglist2;
    private JComboBox reglist3;
    private JLabel comma1;
    private JLabel comma2;
    private JLabel instructionType;
    private JLabel Opcode6;
    private JLabel Func6;
    private JLabel codeLABEL;
    private JTextField immTEXTFIELD;
    private JLabel open;
    private JLabel close;
    private JButton addBUTTON;

    public String convertToOpcode(){
    //MyPanel.y;
        return null;
    }
    
    public String toBinary(long decimalResult, int N) {
        int i;
        String binary = Long.toBinaryString(decimalResult); //gives us a binary string, but is missing leading zeros
        System.out.println("#testing2"+binary);
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
            throw new IllegalArgumentException("Hex String is not a N bit number!");
        }
    }

    private static String hexStringToNBitBinary(String hexString, int N) {
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

    public MyPanel() {
        //construct preComponents
        String[] loadstoreCBOXItems = {"LW", "LWU", "SW"};
        String[] instructiontypeCBOXItems = {"Load-Store Instruction", "Arithmetic Instruction", "Logical Instruction", "Shift Instruction", "Control and Transfer Instruction", "Set-on-condition Instruction"};
        String[] arithmeticCBOXItems = {"DADDIU", "DSUBU", "DDIV"};
        String[] logicalCBOXItems = {"AND", "ORI"};
        String[] transferCBOXItems = {"J", "BEQ"};
        String[] setCBOXItems = {"SLT"};
        String[] shiftCBOXItems = {"DSRLV"};
        String[] codeLISTItems = {"Line Code 1", "Line Code 2", "Line Code 3"};
        String[] reglist1Items = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31", "R32"};
        String[] reglist2Items = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31", "R32"};
        String[] reglist3Items = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31", "R32"};

        //construct components
        loadstoreCBOX = new JComboBox(loadstoreCBOXItems);
        titleLABEL = new JLabel("Pipeline Flush");
        instructiontypeCBOX = new JComboBox(instructiontypeCBOXItems);
        typeLABEL = new JLabel("Instruction Type");
        arithmeticCBOX = new JComboBox(arithmeticCBOXItems);
        logicalCBOX = new JComboBox(logicalCBOXItems);
        transferCBOX = new JComboBox(transferCBOXItems);
        setCBOX = new JComboBox(setCBOXItems);
        shiftCBOX = new JComboBox(shiftCBOXItems);
        codeLIST = new JList(codeLISTItems);
        reglist1 = new JComboBox(reglist1Items);
        reglist2 = new JComboBox(reglist2Items);
        reglist3 = new JComboBox(reglist3Items);
        comma1 = new JLabel(",");
        comma2 = new JLabel(",");
        instructionType =new JLabel("this is to store the instruction type such as 'ddiv'");
        Opcode6=new JLabel("for writing bianry stirng opcode");
        Func6=new JLabel("for func opcode");
        codeLABEL = new JLabel("CODE");
        immTEXTFIELD = new JTextField(5);
        open = new JLabel("(");
        close = new JLabel(")");
        addBUTTON = new JButton("ADD TO CODE");

        //adjust size and set layout
        setPreferredSize(new Dimension(667, 366));
        setLayout(null);

        //add components
        add(loadstoreCBOX);
        add(titleLABEL);
        add(instructiontypeCBOX);
        add(typeLABEL);
        add(arithmeticCBOX);
        add(logicalCBOX);
        add(transferCBOX);
        add(setCBOX);
        add(shiftCBOX);
        add(codeLIST);
        add(reglist1);
        add(reglist2);
        add(reglist3);
        add(comma1);
        add(comma2);
        add(codeLABEL);
        add(immTEXTFIELD);
        add(open);
        add(close);
        add(addBUTTON);

        //set component bounds (only needed by Absolute Positioning)
        loadstoreCBOX.setBounds(25, 70, 80, 25);
        titleLABEL.setBounds(10, 5, 85, 30);
        instructiontypeCBOX.setBounds(120, 35, 185, 25);
        typeLABEL.setBounds(25, 35, 100, 25);
        arithmeticCBOX.setBounds(25, 70, 80, 25);
        logicalCBOX.setBounds(25, 70, 80, 25);
        transferCBOX.setBounds(25, 70, 80, 25);
        setCBOX.setBounds(25, 70, 80, 25);
        shiftCBOX.setBounds(25, 70, 80, 25);
        codeLIST.setBounds(320, 35, 325, 310);
        reglist1.setBounds(115, 70, 50, 25);
        reglist2.setBounds(185, 70, 50, 25);
        reglist3.setBounds(250, 70, 50, 25);
        comma1.setBounds(175, 75, 15, 20);
        comma2.setBounds(240, 75, 15, 25);
        codeLABEL.setBounds(330, 10, 170, 25);
        immTEXTFIELD.setBounds(185, 70, 55, 25);
        open.setBounds(245, 70, 25, 25);
        close.setBounds(300, 70, 15, 25);
        addBUTTON.setBounds(25, 110, 280, 25);

        //HIDE FIRST UNTIL FIRST SELECT
        loadstoreCBOX.setVisible(false);
        arithmeticCBOX.setVisible(false);
        logicalCBOX.setVisible(false);
        transferCBOX.setVisible(false);
        setCBOX.setVisible(false);
        shiftCBOX.setVisible(false);
        reglist1.setVisible(false);
        reglist2.setVisible(false);
        reglist3.setVisible(false);
        comma1.setVisible(false);
        comma2.setVisible(false);
        immTEXTFIELD.setVisible(false);
        open.setVisible(false);
        close.setVisible(false);

        //After select. For displaying list of commands available per instruction type
        instructiontypeCBOX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object selected = instructiontypeCBOX.getSelectedItem();
                loadstoreCBOX.setVisible(false);
                arithmeticCBOX.setVisible(false);
                logicalCBOX.setVisible(false);
                transferCBOX.setVisible(false);
                setCBOX.setVisible(false);
                shiftCBOX.setVisible(false);
                reglist1.setVisible(false);
                reglist2.setVisible(false);
                reglist3.setVisible(false);
                comma1.setVisible(false);
                comma2.setVisible(false);
                immTEXTFIELD.setVisible(false);
                open.setVisible(false);
                close.setVisible(false);
                switch (selected.toString()) {
                    case "Load-Store Instruction":
                        loadstoreCBOX.setVisible(true);
                        reglist1.setVisible(true);
                        reglist3.setVisible(true);
                        comma1.setVisible(true);
                        immTEXTFIELD.setBounds(185, 70, 55, 25);
                        immTEXTFIELD.setVisible(true);
                        open.setVisible(true);
                        close.setVisible(true);
                        break;
                    case "Arithmetic Instruction":
                        arithmeticCBOX.setVisible(true);
                        break;
                    case "Logical Instruction":
                        logicalCBOX.setVisible(true);
                        break;
                    case "Shift Instruction":
                        shiftCBOX.setVisible(true);
                        reglist1.setVisible(true);
                        reglist2.setVisible(true);
                        reglist3.setVisible(true);
                        comma1.setVisible(true);
                        comma2.setVisible(true);
                        break;
                    case "Control and Transfer Instruction":
                        transferCBOX.setVisible(true);
                        break;
                    case "Set-on-condition Instruction":
                        setCBOX.setVisible(true);
                        reglist1.setVisible(true);
                        reglist2.setVisible(true);
                        reglist3.setVisible(true);
                        comma1.setVisible(true);
                        comma2.setVisible(true);
                        break;
                }
            }
        });

        //for display of DADDIU, DSUBU, DDIV
        arithmeticCBOX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object selected = arithmeticCBOX.getSelectedItem();
                loadstoreCBOX.setVisible(false);
                logicalCBOX.setVisible(false);
                transferCBOX.setVisible(false);
                setCBOX.setVisible(false);
                shiftCBOX.setVisible(false);
                reglist1.setVisible(false);
                reglist2.setVisible(false);
                reglist3.setVisible(false);
                comma1.setVisible(false);
                comma2.setVisible(false);
                immTEXTFIELD.setVisible(false);
                open.setVisible(false);
                close.setVisible(false);
                 instructionType.setText(selected.toString()); //for retrieval later, ex. daddiu
                switch (selected.toString()) {
                    case "DADDIU":
                        reglist1.setVisible(true);
                        comma1.setVisible(true);
                        reglist2.setVisible(true);
                        comma2.setVisible(true);
                        immTEXTFIELD.setBounds(250, 70, 55, 25);
                        immTEXTFIELD.setVisible(true);
                        Opcode6.setText(toBinary(25, 6));
                      
                        break;
                    case "DSUBU":
                        reglist1.setVisible(true);
                        comma1.setVisible(true);
                        reglist2.setVisible(true);
                        comma2.setVisible(true);
                        reglist3.setVisible(true);
                        Opcode6.setText(toBinary(0, 6));
                        Func6.setText(toBinary(47,6));
                        System.out.println("#testingopcode"+Opcode6.getText());
                        break;
                    case "DDIV":
                        reglist2.setVisible(true);
                        comma2.setVisible(true);
                        reglist3.setVisible(true);
                        Opcode6.setText(toBinary(0, 6));
                        Func6.setText(toBinary(30,6));
                        break;
                }
            }
        });

        //for display of AND and ORI
        logicalCBOX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object selected = logicalCBOX.getSelectedItem();
                loadstoreCBOX.setVisible(false);
                arithmeticCBOX.setVisible(false);
                transferCBOX.setVisible(false);
                setCBOX.setVisible(false);
                shiftCBOX.setVisible(false);
                reglist1.setVisible(false);
                reglist2.setVisible(false);
                reglist3.setVisible(false);
                comma1.setVisible(false);
                comma2.setVisible(false);
                immTEXTFIELD.setVisible(false);
                open.setVisible(false);
                close.setVisible(false);
                instructionType.setText(selected.toString());
                switch (selected.toString()) {
                    case "AND":
                        reglist1.setVisible(true);
                        comma1.setVisible(true);
                        reglist2.setVisible(true);
                        comma2.setVisible(true);
                        reglist3.setVisible(true);
                        Opcode6.setText(toBinary(0, 6));
                        Func6.setText(toBinary(36,6));
                        break;
                    case "ORI":
                        reglist1.setVisible(true);
                        comma1.setVisible(true);
                        reglist2.setVisible(true);
                        comma2.setVisible(true);
                        immTEXTFIELD.setBounds(250, 70, 55, 25);
                        immTEXTFIELD.setVisible(true);
                        Opcode6.setText(toBinary(13, 6));
                        break;
                }
            }
        });

        //for display of J and BEQ
        transferCBOX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object selected = transferCBOX.getSelectedItem();
                loadstoreCBOX.setVisible(false);
                arithmeticCBOX.setVisible(false);
                logicalCBOX.setVisible(false);
                setCBOX.setVisible(false);
                shiftCBOX.setVisible(false);
                reglist1.setVisible(false);
                reglist2.setVisible(false);
                reglist3.setVisible(false);
                comma1.setVisible(false);
                comma2.setVisible(false);
                immTEXTFIELD.setVisible(false);
                open.setVisible(false);
                close.setVisible(false);
                 instructionType.setText(selected.toString());
                switch (selected.toString()) {
                    case "J":
                        immTEXTFIELD.setBounds(115, 70, 55, 25);
                        immTEXTFIELD.setVisible(true);
                        Opcode6.setText(toBinary(2, 6));
                        break;
                    case "BEQ":
                        reglist1.setVisible(true);
                        comma1.setVisible(true);
                        reglist2.setVisible(true);
                        comma2.setVisible(true);
                        immTEXTFIELD.setBounds(250, 70, 55, 25);
                        immTEXTFIELD.setVisible(true);
                        Opcode6.setText(toBinary(4, 6));
                        break;
                }
            }
        });


        addBUTTON.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.out.println("from action"+ntesting);
                //System.out.println(instructionType.getText());
                System.out.println("hello  ");
                //convertToOpcode();
                String selected1 = reglist1.getSelectedItem().toString().substring(1);
                String selected2 = reglist2.getSelectedItem().toString().substring(1);
                String selected3 = reglist3.getSelectedItem().toString().substring(1);
                //<insert error checking here>
                immORlabel = immTEXTFIELD.getText();
                nREG1 = Integer.parseInt(selected1);
                nREG2 = Integer.parseInt(selected2);
                nREG3 = Integer.parseInt(selected3);
                //Long.toBinaryString(nREG1);
                //Long.toBinaryString(nREG2);
                String a=toBinary(nREG1, 5);
                String b=toBinary(nREG2, 5);
                String c=toBinary(nREG3, 5);
                String d=toBinary(0, 5);
                String immorlabeloroffset=hexStringToNBitBinary(immORlabel, 16);
                System.out.println("register 1:"+a);
                System.out.println("register 2:"+b);
                System.out.println(hexStringToNBitBinary(immORlabel, 16));
                //String x=a+b;
                //System.out.println("r1 with r2"+x);
                //BigInteger binaryOpcode = new BigInteger(x, 2);
                //String opcode =binaryOpcode.toString(16);
                //System.out.println("opcode in hex"+opcode);
                String x=new String();
                String op, rs, rt, rd, five, func, imm;
                //convert to hexopcode
                switch(instructionType.getText().toLowerCase()){
                    //case r type
                    case "dsrlv":
                    case "and":
                    case "slt":
                    case "dsubu":
                        op=Opcode6.getText();
                        rs=b;
                        rt=c;
                        rd=a;
                        five=d;
                        func=Func6.getText();
                        x=op+rs+rt+rd+five+func;
                        break;
                    case "ddiv":
                        op=Opcode6.getText();
                        rs=b;
                        rt=c;
                        rd=d;
                        five=d;
                        func=Func6.getText();
                        x=op+rs+rt+rd+five+func;
                        break;
                    
                    //i type
                    case "beq":
                    case "sw":
                        op=Opcode6.getText();
                        rs=b;
                        rt=c;
                        imm=immorlabeloroffset;
                        x=op+rs+rt+imm;
                        break;
                    case "ori":
                    case "daddiu":
                    case "lw":
                    case "lwu":
                        op=Opcode6.getText();
                        rs=b;
                        rt=a;
                        imm=immorlabeloroffset;
                        x=op+rs+rt+imm;
                        break;
                }
                BigInteger binaryOpcode = new BigInteger(x, 2);
                String opcode =binaryOpcode.toString(16);
                System.out.println("opcode in hex"+opcode);
                //System.out.println(toBinary(nREG1, 6)); //6 bit binary
                //Long.toHexString(toopcode);
                        
            }

            
        });

    }

    public static void main(String[] args) {
         MIPSSplash m = new MIPSSplash();
        MIPSSplash.splashInit();           // initialize splash overlay drawing parameters
        MIPSSplash.appInit();              // simulate what an application would do before starting
        if (MIPSSplash.mySplash != null) // check if we really had a spash screen
        {
            MIPSSplash.mySplash.close();   // we're done with it
        }
        // begin with the interactive portion of the program
        JFrame frame = new JFrame("Group 7");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MyPanel());
        frame.pack();
        frame.setVisible(true);
        int ntesting=5;
        
        
    }
}
