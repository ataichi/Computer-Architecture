/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Caches.CachedTables;
import Caches.DataTableCache;
import Caches.OpcodeTableCache;
import Caches.OpcodeTableRow;
import Caches.RegisterTableCache;
import CodeObjects.IType.BEQ;
import CodeObjects.IType.DADDIU;
import CodeObjects.IType.LW;
import CodeObjects.IType.LWU;
import CodeObjects.IType.ORI;
import CodeObjects.IType.SW;
import CodeObjects.Instruction;
import CodeObjects.JType.J;
import CodeObjects.RType.AND;
import CodeObjects.RType.DDIV;
import CodeObjects.RType.DSRLV;
import CodeObjects.RType.DSUBU;
import CodeObjects.RType.SLT;
import Functions.Pipelinemap;
import javax.swing.table.DefaultTableModel;
import Functions.Usable;
import java.awt.Color;
import java.awt.Rectangle;
import java.math.BigInteger;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import splashdemo.MIPSSplash;

/**
 *
 * @author nievabri
 */
public class FRAME1 extends javax.swing.JFrame {

    private DefaultTableModel regModel;
    private DefaultTableModel pipelinemodel;
    private DefaultTableModel opcodemodel;
    private DefaultTableModel codemodel;
    private DefaultTableModel datamodel;
    private DefaultTableModel pipelinemapmodel;
    private OpcodeTableRow opcodetablerow;
    private OpcodeTableCache opcodetablecache;
    private RegisterTableCache registertablecache;
    private DataTableCache datatablecache;
    private CachedTables cachedtables;

    Usable usable = new Usable();
    private String hex;
    private int nIndex = -1;
    private ArrayList<String> labelSA = new ArrayList<String>();
    private ArrayList<String> sAddress = new ArrayList<String>();
    private ArrayList<String> OPCODE8 = new ArrayList<String>();
    private ArrayList<String> sInstruction = new ArrayList<String>();
    private ArrayList<String> JIndexArray = new ArrayList<String>();
    private ArrayList<String> BEQXArray = new ArrayList<String>();
    private Object selected;
    private String d;
    private String x;
    private String temp;
    private String tempInst;
    private String a;
    private String b;
    private String c;
    private String Jnull = "";
    private int index = 0;
    private Pipelinemap plm;
    private int flag = 0;
    private ArrayList<Instruction> iList = new ArrayList();
    private int nIndex2 = 0;

    private boolean firstClickSS = true; //first click of single step
    private Pipelinemap pm;
    private CachedTables initialTableState;
    private int singleExecIndex = 0;
    private String hazardType;

    public void ERRORLDexceed() {
        JOptionPane.showMessageDialog(this, "Exceeded maximum address! Please RESET now", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isINVALIDinLW(String lALU) {

        if ((!lALU.matches("[a-fA-F0-9]*")) || !(lALU.substring(0, 1).matches("[2-3]*"))
                || lALU.length() != 4) {
            return true;
        }
        return false;

    }

    public void ERRORDIVZERO() {
        JOptionPane.showMessageDialog(this, "Division by zero error! Please RESET now", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void checkInput(int row, int column) {
        regModel = (DefaultTableModel) jTable3.getModel();
        int r = row;
        int c = column;
        Object val = regModel.getValueAt(r, c);
        String changedValue = JOptionPane.showInputDialog("Enter value for " + regModel.getValueAt(r, c - 1));
        String extendValue;

        if (changedValue.matches("[a-fA-F0-9]*") && changedValue.length() > 0 && changedValue.length() <= 16) {
            extendValue = usable.hexToNbit(changedValue, 16);
            regModel.setValueAt(extendValue.toUpperCase(), r, c);
        } else {
            JOptionPane.showMessageDialog(this, "value is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        registertablecache = new RegisterTableCache(regModel);
//        cachedtables = new CachedTables(codemodel, datamodel, opcodemodel, regModel, opcodemodel.getRowCount());
    }

    public void checkInputData(int row, int column) {
        DefaultTableModel dataModel = (DefaultTableModel) jTable4.getModel();
        int r = row;
        int c = column;
        Object val = dataModel.getValueAt(r, c);
        String changedValue = JOptionPane.showInputDialog("Enter value for " + dataModel.getValueAt(r, c - 1));
        String extendValue;

        if (changedValue.matches("[a-fA-F0-9]*") && changedValue.length() > 0 && changedValue.length() <= 2) {
            extendValue = usable.hexToNbit(changedValue, 2);
            dataModel.setValueAt(extendValue.toUpperCase(), r, c);
            dataModel.setValueAt(extendValue.toUpperCase(), r, c + 1);
        } else {
            JOptionPane.showMessageDialog(this, "value is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        datatablecache = new DataTableCache(dataModel);
    }

    public boolean isLabelInvalid() {
        if (!jTextField1.getText().equals("")) {
            String sTemp = jTextField1.getText().toUpperCase();
            if (sTemp.equals("DSUBU") || sTemp.equals("DDIV")
                    || sTemp.equals("AND") || sTemp.equals("BEQ")
                    || sTemp.equals("DSRLV") || sTemp.equals("SLT")
                    || sTemp.equals("LW") || sTemp.equals("LWU")
                    || sTemp.equals("SW") || sTemp.equals("DADDIU")
                    || sTemp.equals("ORI") || sTemp.equals("J")
                    || sTemp.substring(0, 1).matches("[0-9]")
                    || !sTemp.matches("[a-zA-Z0-9_]*")) {
                return true;
            }
            for (int i = 0; i < labelSA.size(); i++) {
                if (sTemp.equals(labelSA.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isJBEQLabelInvalid(String label) {
        if (label.equals("")) {
            return true;
        } else {
            String sTemp = label.toUpperCase();
            if (sTemp.equals("DSUBU") || sTemp.equals("DDIV")
                    || sTemp.equals("AND") || sTemp.equals("BEQ")
                    || sTemp.equals("DSRLV") || sTemp.equals("SLT")
                    || sTemp.equals("LW") || sTemp.equals("LWU")
                    || sTemp.equals("SW") || sTemp.equals("DADDIU")
                    || sTemp.equals("ORI") || sTemp.equals("J")
                    || sTemp.substring(0, 1).matches("[0-9]")
                    || !sTemp.matches("[a-zA-Z0-9_]*")) {
                return true;
            }
        }
        return false;
    }

    public boolean isInvalidImmediateLoadStore() {
        String sTemp = jTextField2.getText().toString();
        if ((sTemp.equals("")) || (!sTemp.matches("[a-fA-F0-9]*")) || !(sTemp.substring(0, 1).matches("[2-3]*"))
                || sTemp.length() != 4) {
            return true;
        }
        return false;
    }

    public boolean isInvalidImmediateOther() {
        String sTemp = jTextField5.getText().toString();
        if ((sTemp.equals("")) || (!sTemp.matches("[a-fA-F0-9]*"))
                || sTemp.length() != 4) {
            return true;
        }
        return false;
    }

    public void resizeTableColumnWidth() {
        /* OPCODE TABLE */
        jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_OFF);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(120);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(50);
        jTable1.setAutoResizeMode(jTable1.AUTO_RESIZE_LAST_COLUMN);
        /* REGISTERS TABLE */
        jTable3.setAutoResizeMode(jTable3.AUTO_RESIZE_OFF);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(4);
        jTable3.getColumnModel().getColumn(1).setPreferredWidth(90);
        jTable3.setAutoResizeMode(jTable3.AUTO_RESIZE_LAST_COLUMN);
        /* CODE SEGMENT TABLE */
        jTable5.setAutoResizeMode(jTable5.AUTO_RESIZE_OFF);
        jTable5.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTable5.getColumnModel().getColumn(1).setPreferredWidth(55);
        jTable5.getColumnModel().getColumn(2).setPreferredWidth(15);
        jTable5.getColumnModel().getColumn(3).setPreferredWidth(100);
        jTable5.setAutoResizeMode(jTable5.AUTO_RESIZE_LAST_COLUMN);
        /* DATA SEGMENT TABLE */
        jTable4.setAutoResizeMode(jTable4.AUTO_RESIZE_OFF);
        jTable4.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTable4.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTable4.getColumnModel().getColumn(2).setPreferredWidth(80);
        jTable4.setAutoResizeMode(jTable4.AUTO_RESIZE_LAST_COLUMN);
        /* PIPELINE MAP TABLE */
        jTable2.setAutoResizeMode(jTable2.AUTO_RESIZE_OFF);
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(100);
        for (int i = 1; i < jTable2.getColumnCount(); i++) {
            jTable2.getColumnModel().getColumn(i).setPreferredWidth(10);
        }
        //   jTable2.setAutoResizeMode(jTable2.AUTO_RESIZE_LAST_COLUMN);

    }

    public void dynamicGeneratedValuesOfAddresses() {
        codemodel = (DefaultTableModel) jTable5.getModel();
        for (int i = 0; i <= 8191; i++) {
            int compare = i % 4;
            if (compare == 0) {
                hex = Integer.toHexString(i).toUpperCase();
                hex = usable.hexToNbit(hex, 4);
                codemodel.addRow(new Object[]{hex});
            }
        }
    }

    public void dataSegmentAddresses() {
        datamodel = (DefaultTableModel) jTable4.getModel();
        for (int i = 8192; i <= 16383; i++) {
            hex = Integer.toHexString(i).toUpperCase();
            hex = usable.hexToNbit(hex, 4);
            datamodel.addRow(new Object[]{hex, "00", "00"});
        }
    }

    public void uponStartHide() {
        jButton7.setEnabled(false); //pipeline2
        jButton6.setEnabled(false); //full exec button
        jButton5.setEnabled(false); //single exec button
        ERRORlabel.setVisible(false);
        ERRORImmLS.setVisible(false);
        ERRORnosuchlabel.setVisible(false);
        jButton2.setEnabled(false); //done button
        jButton4.setEnabled(false); //reset button
    }

    public FRAME1() {
        initComponents();
        resizeTableColumnWidth();
        dynamicGeneratedValuesOfAddresses();
        dataSegmentAddresses();
        uponStartHide();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel32 = new javax.swing.JLabel();
        panel1 = new java.awt.Panel();
        jButton2 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboBox4 = new javax.swing.JComboBox();
        lblGroup7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panel2 = new java.awt.Panel();
        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jComboBox7 = new javax.swing.JComboBox();
        jComboBox8 = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jComboBox9 = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jComboBox11 = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        panel3 = new java.awt.Panel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        ERRORlabel = new javax.swing.JLabel();
        ERRORImmLS = new javax.swing.JLabel();
        ERRORnosuchlabel = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();

        jLabel32.setText("jLabel32");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel1.setBackground(new java.awt.Color(51, 51, 51));

        jButton2.setFont(new java.awt.Font("Castellar", 0, 14)); // NOI18N
        jButton2.setText("DONE");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Castellar", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 204, 204));
        jLabel18.setText("Step 2 - OPCODE TABLE");

        jButton1.setFont(new java.awt.Font("Castellar", 0, 14)); // NOI18N
        jButton1.setText("ADD TO CODE");
        jButton1.setFocusTraversalPolicyProvider(true);
        jButton1.setHideActionText(true);
        jButton1.setInheritsPopupMenu(true);
        jButton1.setSelected(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DSUBU", "DDIV", "AND", "DSRLV", "SLT", "BEQ", "LW", "LWU", "SW", "DADDIU", "ORI", "J" }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        lblGroup7.setFont(new java.awt.Font("Mongolian Baiti", 1, 30)); // NOI18N
        lblGroup7.setForeground(new java.awt.Color(0, 204, 204));
        lblGroup7.setText("GROUP 7 - PIPELINE FLUSH");
        lblGroup7.setPreferredSize(new java.awt.Dimension(100, 100));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                labelKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 204));
        jLabel2.setText("(OPTIONAL) LABEL");

        jLabel3.setFont(new java.awt.Font("Castellar", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 204, 204));
        jLabel3.setText("Step 1 - INPUT CODE");

        panel2.setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jLabel4.setText(",");

        jLabel5.setText(",");

        jLabel7.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 204, 204));
        jLabel7.setText("RD");

        jLabel8.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 204, 204));
        jLabel8.setText("RS");

        jLabel9.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 204, 204));
        jLabel9.setText("RT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel9))
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addContainerGap())
        );

        panel2.add(jPanel1, "card5");

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel12.setText(",");

        jLabel13.setText("(");

        jLabel14.setText(")");

        jLabel15.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 204, 204));
        jLabel15.setText("RD");

        jLabel16.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 204, 204));
        jLabel16.setText("RS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel15)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel16))
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel2.add(jPanel3, "card10");

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jLabel6.setText(",");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 204, 204));
        jLabel10.setText("RS");

        jLabel11.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 204, 204));
        jLabel11.setText("RT");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel11))
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(95, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        panel2.add(jPanel2, "card9");

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );

        panel2.add(jPanel5, "card10");

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox9ActionPerformed(evt);
            }
        });

        jLabel22.setText(",");

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox10ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 204, 204));
        jLabel23.setText("RS");

        jLabel24.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 204, 204));
        jLabel24.setText("RT");

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 204, 204));
        jLabel25.setText("LABEL");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel23)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel24)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel25)
                        .addGap(0, 29, Short.MAX_VALUE))
                    .addComponent(jTextField4))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        panel2.add(jPanel6, "card9");

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox11ActionPerformed(evt);
            }
        });

        jLabel26.setText(",");

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R29", "R30", "R31" }));
        jComboBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox12ActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 204, 204));
        jLabel27.setText("RD");

        jLabel28.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 204, 204));
        jLabel28.setText("RS");

        jLabel29.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 204, 204));
        jLabel29.setText("IMM");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel27)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel28)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel29)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        panel2.add(jPanel7, "card9");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jScrollPane3.setAutoscrolls(true);

        jTable2.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Instruction", "1", "2", "3", "4", "5"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane3.setViewportView(jTable2);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Instruction", "OPCODE", "IR(0..5)", "IR(6..10)", "IR(11..15)", "IR(16..31)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setFillsViewportHeight(true);
        jTable1.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(3).setResizable(false);
        }

        jScrollPane4.setViewportView(jScrollPane1);

        jLabel20.setFont(new java.awt.Font("Castellar", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 204, 204));
        jLabel20.setText("Step 3 - PIPELINE MAP");

        panel3.setBackground(new java.awt.Color(51, 51, 51));

        jScrollPane5.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"R0", "0000000000000000"},
                {"R1", "0000000000000000"},
                {"R2", "0000000000000000"},
                {"R3", "0000000000000000"},
                {"R4", "0000000000000000"},
                {"R5", "0000000000000000"},
                {"R6", "0000000000000000"},
                {"R7", "0000000000000000"},
                {"R8", "0000000000000000"},
                {"R9", "0000000000000000"},
                {"R10", "0000000000000000"},
                {"R11", "0000000000000000"},
                {"R12", "0000000000000000"},
                {"R13", "0000000000000000"},
                {"R14", "0000000000000000"},
                {"R15", "0000000000000000"},
                {"R16", "0000000000000000"},
                {"R17", "0000000000000000"},
                {"R18", "0000000000000000"},
                {"R19", "0000000000000000"},
                {"R20", "0000000000000000"},
                {"R21", "0000000000000000"},
                {"R22", "0000000000000000"},
                {"R23", "0000000000000000"},
                {"R24", "0000000000000000"},
                {"R25", "0000000000000000"},
                {"R26", "0000000000000000"},
                {"R27", "0000000000000000"},
                {"R28", "0000000000000000"},
                {"R29", "0000000000000000"},
                {"R30", "0000000000000000"},
                {"R31", "0000000000000000"},
                {"LO", "0000000000000000"},
                {"HI", "0000000000000000"}
            },
            new String [] {
                "Register", "Register Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setCellSelectionEnabled(true);
        jTable3.setFillsViewportHeight(true);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTable3);

        jLabel1.setFont(new java.awt.Font("Castellar", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 204));
        jLabel1.setText("Registers");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Address", "Before Execution", "After Execution"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable4.setFillsViewportHeight(true);
        jTable4.setUpdateSelectionOnSort(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable4);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Address", "Representation", "Label", "Instruction"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jScrollPane7.setViewportView(jTable5);

        jLabel17.setFont(new java.awt.Font("Castellar", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 204, 204));
        jLabel17.setText("Code Segment");

        jLabel19.setFont(new java.awt.Font("Castellar", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 204, 204));
        jLabel19.setText("Data");

        jLabel30.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 204, 204));
        jLabel30.setText("GO TO");

        jTextField6.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jButton3.setText("GO");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel3Layout = new javax.swing.GroupLayout(panel3);
        panel3.setLayout(panel3Layout);
        panel3Layout.setHorizontalGroup(
            panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel3Layout.createSequentialGroup()
                .addGroup(panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addContainerGap())
                    .addGroup(panel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(panel3Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(89, 89, 89)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
        );
        panel3Layout.setVerticalGroup(
            panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel19)
                    .addComponent(jLabel30)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        jScrollPane8.setAutoscrolls(true);

        jTable6.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"IF Cycle"},
                {"IF/ID.IR"},
                {"IF/ID.NPC"},
                {"PC"},
                {"ID Cycle"},
                {"ID/EX.IR"},
                {"ID/EX.A"},
                {"ID/EX.B"},
                {"ID/EX. IMM"},
                {"EX Cycle"},
                {"EX/MEM.IR"},
                {"EX/MEM.ALUOUTPUT"},
                {"EX/MEM.B"},
                {"EX/MEM.cond"},
                {"MEM Cycle"},
                {"MEM/WB.IR"},
                {"MEM/WB.ALUOUTPUT"},
                {"MEM/WB.LMD"},
                {"MEM[ALUOUTPUT]"},
                {"WB Cycle"},
                {"R ? = "}
            },
            new String [] {
                "IF | ID | EX | MEM | WB"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane8.setViewportView(jTable6);

        jLabel21.setFont(new java.awt.Font("Castellar", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 204, 204));
        jLabel21.setText("CLOCK CYCLE VALUES / TRACING");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane8)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton4.setBackground(new java.awt.Color(204, 0, 0));
        jButton4.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jButton4.setLabel("RESET");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 204, 204));
        jLabel31.setText("CODE ADDED");

        jButton5.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jButton5.setText("Single-step");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Castellar", 0, 11)); // NOI18N
        jButton6.setText("Full Execution");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        ERRORlabel.setForeground(java.awt.Color.red);
        ERRORlabel.setText("*ERROR: Label is INVALID");

        ERRORImmLS.setForeground(java.awt.Color.red);
        ERRORImmLS.setText("*ERROR: Invalid Immediate");

        ERRORnosuchlabel.setForeground(java.awt.Color.red);
        ERRORnosuchlabel.setText("RESET now. *ERROR: no such labels exist!");

        jButton7.setText("Pipeline 2");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(ERRORnosuchlabel))
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel2)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(14, 14, 14)
                                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(ERRORlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jButton2)))
                                        .addGap(10, 10, 10)
                                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(ERRORImmLS))))
                                    .addComponent(lblGroup7, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                        .addComponent(jLabel31)
                                        .addGap(63, 63, 63)
                                        .addComponent(jButton4))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton6))
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(lblGroup7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton4)
                                            .addComponent(jLabel31))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(ERRORnosuchlabel))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panel1Layout.createSequentialGroup()
                                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jButton1)
                                                    .addComponent(jButton2))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(ERRORImmLS)
                                                    .addComponent(ERRORlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(11, 11, 11)
                                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(panel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5)
                            .addComponent(jButton6)
                            .addComponent(jButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        ERRORImmLS.getAccessibleContext().setAccessibleName("ERRORImmLS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        flag = 0;
        ERRORlabel.setVisible(false);
        ERRORImmLS.setVisible(false);
        ERRORnosuchlabel.setVisible(false);
        jButton2.setEnabled(true); //done button
        jButton4.setEnabled(true);
        jButton4.setBackground(Color.red);

        if (!isLabelInvalid()) {
            nIndex++;
            temp = usable.toBinary((nIndex * 4), 28);
            temp = temp.substring(0, temp.length() - 2);
            sAddress.add(temp);
            labelSA.add(jTextField1.getText());

            selected = jComboBox4.getSelectedItem();
            d = usable.toBinary(0, 5);
            x = new String();

            if (selected.toString().equals("DSUBU") || selected.toString().equals("DSRLV") || selected.toString().equals("SLT") || selected.toString().equals("AND")) {
                flag = 1;
                JIndexArray.add(Jnull);
                BEQXArray.add("");
                tempInst = jComboBox4.getSelectedItem().toString() + " "
                        + jComboBox1.getSelectedItem().toString() + " , "
                        + jComboBox2.getSelectedItem().toString() + " , "
                        + jComboBox3.getSelectedItem().toString();
                /* Binary of registers selected in jPanel1*/
                a = usable.toBinary(Integer.parseInt(jComboBox1.getSelectedItem().toString().substring(1)), 5);
                b = usable.toBinary(Integer.parseInt(jComboBox2.getSelectedItem().toString().substring(1)), 5);
                c = usable.toBinary(Integer.parseInt(jComboBox3.getSelectedItem().toString().substring(1)), 5);
                switch (selected.toString()) {
                    case "DSUBU":
                        x = (usable.toBinary(0, 6) + b + c + a + d + usable.toBinary(47, 6));
                        iList.add(new DSUBU(sAddress.get(nIndex2),
                                Integer.parseInt(jComboBox1.getSelectedItem().toString().substring(1)),
                                Integer.parseInt(jComboBox2.getSelectedItem().toString().substring(1)),
                                Integer.parseInt(jComboBox3.getSelectedItem().toString().substring(1))));
                        break;
                    case "DSRLV":
                        x = (usable.toBinary(0, 6) + b + c + a + d + usable.toBinary(22, 6));
                        iList.add(new DSRLV(sAddress.get(nIndex2),
                                Integer.parseInt(jComboBox1.getSelectedItem().toString().substring(1)),
                                Integer.parseInt(jComboBox2.getSelectedItem().toString().substring(1)),
                                Integer.parseInt(jComboBox3.getSelectedItem().toString().substring(1))));
                        break;
                    case "SLT":
                        x = (usable.toBinary(0, 6) + b + c + a + d + usable.toBinary(42, 6));
                        iList.add(new SLT(sAddress.get(nIndex2),
                                Integer.parseInt(jComboBox1.getSelectedItem().toString().substring(1)),
                                Integer.parseInt(jComboBox2.getSelectedItem().toString().substring(1)),
                                Integer.parseInt(jComboBox3.getSelectedItem().toString().substring(1))));
                        break;
                    case "AND":
                        x = (usable.toBinary(0, 6) + b + c + a + d + usable.toBinary(36, 6));
                        iList.add(new AND(sAddress.get(nIndex2),
                                Integer.parseInt(jComboBox1.getSelectedItem().toString().substring(1)),
                                Integer.parseInt(jComboBox2.getSelectedItem().toString().substring(1)),
                                Integer.parseInt(jComboBox3.getSelectedItem().toString().substring(1))));
                        break;
                }
            } else if (selected.toString() == "LWU" || selected.toString() == "LW" || selected.toString() == "SW") {
                if (isInvalidImmediateLoadStore()) {
                    flag = 0;
                    ERRORImmLS.setVisible(true);
                } else {
                    flag = 1;
                    ERRORImmLS.setVisible(false);
                    BEQXArray.add("");
                    JIndexArray.add(Jnull);
                    tempInst = jComboBox4.getSelectedItem().toString() + " "
                            + jComboBox7.getSelectedItem().toString() + ", " + jTextField2.getText() + "("
                            + jComboBox8.getSelectedItem().toString() + ")";
                    String a3 = usable.toBinary(Integer.parseInt(jComboBox7.getSelectedItem().toString().substring(1)), 5);
                    String b3 = usable.toBinary(Integer.parseInt(jComboBox8.getSelectedItem().toString().substring(1)), 5);
                    String jTF2 = usable.hexStringToNBitBinary(jTextField2.getText(), 16);
                    switch (selected.toString()) {
                        case "LW":
                            x = usable.toBinary(35, 6) + b3 + a3 + jTF2;
                            iList.add(new LW(sAddress.get(nIndex2),
                                    Integer.parseInt(jComboBox7.getSelectedItem().toString().substring(1)),
                                    Integer.parseInt(jComboBox8.getSelectedItem().toString().substring(1)),
                                    -1, jTF2));
                            break;
                        case "LWU":
                            x = usable.toBinary(39, 6) + b3 + a3 + jTF2;
                            iList.add(new LWU(sAddress.get(nIndex2),
                                    Integer.parseInt(jComboBox7.getSelectedItem().toString().substring(1)),
                                    Integer.parseInt(jComboBox8.getSelectedItem().toString().substring(1)),
                                    -1, jTF2));
                            break;
                        case "SW":
                            x = usable.toBinary(43, 6) + b3 + a3 + jTF2;
                            iList.add(new SW(sAddress.get(nIndex2),
                                    -1, Integer.parseInt(jComboBox8.getSelectedItem().toString().substring(1)),
                                    Integer.parseInt(jComboBox7.getSelectedItem().toString().substring(1)), jTF2));
                            break;
                    }
                }
            } else if (selected.toString().equals("DADDIU") || selected.toString().equals("ORI")) {
                if (isInvalidImmediateOther()) {
                    flag = 0;
                    ERRORImmLS.setVisible(true);
                } else {
                    flag = 1;
                    ERRORImmLS.setVisible(false);
                    BEQXArray.add("");
                    JIndexArray.add(Jnull);
                    String a7 = usable.toBinary(Integer.parseInt(jComboBox11.getSelectedItem().toString().substring(1)), 5);
                    String b7 = usable.toBinary(Integer.parseInt(jComboBox12.getSelectedItem().toString().substring(1)), 5);
                    String jTF5 = usable.hexStringToNBitBinary(jTextField5.getText(), 16);
                    tempInst = jComboBox4.getSelectedItem().toString() + " " + jComboBox11.getSelectedItem().toString()
                            + " , " + jComboBox12.getSelectedItem().toString() + " , "
                            + jTextField5.getText();
                    switch (selected.toString()) {
                        case "DADDIU":
                            x = usable.toBinary(25, 6) + b7 + a7 + jTF5;
                            iList.add(new DADDIU(sAddress.get(nIndex2),
                                    Integer.parseInt(jComboBox11.getSelectedItem().toString().substring(1)),
                                    Integer.parseInt(jComboBox12.getSelectedItem().toString().substring(1)), -1, jTF5));
//                          iList.add(new )
                            break;
                        case "ORI":
                            x = (usable.toBinary(13, 6) + b7 + a7 + jTF5);
                            iList.add(new ORI(sAddress.get(nIndex2),
                                    Integer.parseInt(jComboBox11.getSelectedItem().toString().substring(1)),
                                    Integer.parseInt(jComboBox12.getSelectedItem().toString().substring(1)), -1, jTF5));
                            break;
                    }
                }
            } else if (selected.toString().equals("J") || selected.toString().equals("BEQ")) {
                String toPass = "";
                if (selected.toString().equals("J")) {
                    toPass = jTextField3.getText();
                } else {
                    toPass = jTextField4.getText();
                }
                if (isJBEQLabelInvalid(toPass)) {
                    flag = 0;
                    ERRORlabel.setVisible(true);
                } else {
                    flag = 1;
                    ERRORlabel.setVisible(false);
                    switch (selected.toString()) {
                        case "J": //na kay DONE BUTTON yung logic niya hehez.
                            JIndexArray.add(jTextField3.getText());
                            BEQXArray.add("");
                            tempInst = jComboBox4.getSelectedItem().toString() + " " + jTextField3.getText();
                            x = usable.toBinary(2, 6); //temp j opcode value to replace in done button
                            iList.add(new J(sAddress.get(nIndex2), toPass));
                            break;
                        case "BEQ":
                            JIndexArray.add(Jnull);
                            tempInst = jComboBox4.getSelectedItem().toString() + " " + jComboBox9.getSelectedItem().toString()
                                    + " , " + jComboBox10.getSelectedItem().toString() + " , " + jTextField4.getText();
                            //jCB9, jCB10, jTF4
                            String a6 = usable.toBinary(Integer.parseInt(jComboBox9.getSelectedItem().toString().substring(1)), 5);
                            String b7 = usable.toBinary(Integer.parseInt(jComboBox10.getSelectedItem().toString().substring(1)), 5);
                            String jTF4 = jTextField4.getText();
                            String BEQopcodeTEMP;
                            x = a6; //temp opcode
                            BEQopcodeTEMP = usable.toBinary(4, 6) + a6 + b7 + jTF4;
                            BEQXArray.add(BEQopcodeTEMP);
                            iList.add(new BEQ(sAddress.get(nIndex2),
                                    -1, Integer.parseInt(jComboBox9.getSelectedItem().toString().substring(1)),
                                    Integer.parseInt(jComboBox10.getSelectedItem().toString().substring(1)), jTF4));
                            break;
                    }
                }
            } else {
                flag = 1;
                switch (selected.toString()) {
                    case "DDIV":
                        /* Binary of registers selected in jPanel2*/
                        JIndexArray.add(Jnull);
                        BEQXArray.add("");
                        tempInst = jComboBox4.getSelectedItem().toString() + " " + jComboBox5.getSelectedItem().toString()
                                + " , " + jComboBox6.getSelectedItem().toString();
                        String a2 = usable.toBinary(Integer.parseInt(jComboBox5.getSelectedItem().toString().substring(1)), 5);
                        String b2 = usable.toBinary(Integer.parseInt(jComboBox6.getSelectedItem().toString().substring(1)), 5);
                        x = (usable.toBinary(0, 6) + a2 + b2 + d + d + usable.toBinary(30, 6));
                        iList.add(new DDIV(sAddress.get(nIndex2),
                                -1, Integer.parseInt(jComboBox5.getSelectedItem().toString().substring(1)),
                                Integer.parseInt(jComboBox6.getSelectedItem().toString().substring(1))));
                        break;
                }
            }
            /* PRINTING IN CODE TEXT AREA WITH LABEL OR WITHOUT*/
            if (flag == 1) {

                if (!jTextField1.getText().equals("")) {
                    jTextArea1.setText(jTextArea1.getText() + jTextField1.getText() + ": " + tempInst + "\n");
                } else {
                    jTextArea1.setText(jTextArea1.getText() + tempInst + "\n");
                }
                BigInteger binaryOpcode = new BigInteger(x, 2);
                String opcode = binaryOpcode.toString(16);
                System.out.println("\nopcode in hex: " + usable.hexToNbit(opcode, 8));

                OPCODE8.add(usable.hexToNbit(opcode, 8));
                sInstruction.add(tempInst);
                jTextField1.setText("");
                nIndex2++;
            }
        } else {
            ERRORlabel.setVisible(true);
        }
        flag = 0;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        Object selected = jComboBox4.getSelectedItem();
        jPanel1.setVisible(false);
        jPanel2.setVisible(false);
        jPanel3.setVisible(false);
        jPanel5.setVisible(false);
        jPanel6.setVisible(false);
        jPanel7.setVisible(false);
        switch (selected.toString()) {
            case "DSUBU":
            case "DSRLV":
            case "SLT":
            case "AND":
                jPanel1.setVisible(true);
                break;
            case "LW":
            case "LWU":
            case "SW":
                jPanel3.setVisible(true);
                break;
            case "J":
                jPanel5.setVisible(true);
                break;
            case "BEQ":
                jPanel6.setVisible(true);
                break;
            case "DDIV":
                jPanel2.setVisible(true);
                break;
            case "DADDIU":
            case "ORI":
                jPanel7.setVisible(true);
                break;
        }
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox8ActionPerformed

    public boolean labelexistJ() {
        int count = 0;
        for (int i = 0; i < JIndexArray.size(); i++) {
            if (JIndexArray.get(i).equals("")) {
                count++;
            }
        }
        if (count == BEQXArray.size()) {
            return true;
        }
        for (int i = 0; i < JIndexArray.size(); i++) {
            if (!JIndexArray.get(i).equals("")) {
                for (int j = 0; j < labelSA.size(); j++) {
                    if (labelSA.get(j).equals(JIndexArray.get(i))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean labelexistBEQ() {
        int count = 0;
        for (int i = 0; i < BEQXArray.size(); i++) {
            if (BEQXArray.get(i).equals("")) {
                count++;
            }
        }
        if (count == BEQXArray.size()) {
            return true;
        }
        for (int i = 0; i < BEQXArray.size(); i++) {
            if (!BEQXArray.get(i).equals("")) {
                for (int j = 0; j < labelSA.size(); j++) {
                    if (labelSA.get(j).equals(BEQXArray.get(i).substring(16))) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        /* DONE BUTTON */
        jButton7.setEnabled(true); //pipeline2
        jTable3.setEnabled(false); //register table
        jButton6.setEnabled(true); //full exec button
        jButton5.setEnabled(true); //single exec button
        jButton2.setEnabled(false);
        jButton1.setEnabled(false);
        nIndex++;
        temp = usable.toBinary((nIndex * 4), 28);
        temp = temp.substring(0, temp.length() - 2);
        sAddress.add(temp);

        if (labelexistJ() && labelexistBEQ()) {

            for (int i = 0; i < JIndexArray.size(); i++) {
                if (!JIndexArray.get(i).equals("")) {

                    x = usable.toBinary(2, 6) + sAddress.get((labelSA.indexOf(JIndexArray.get(i))));
                    BigInteger binaryOpcode2 = new BigInteger(x, 2);
                    String newJOpcode = "";
                    newJOpcode = binaryOpcode2.toString(16);
                    System.out.println("\nJopcode in hex: " + usable.hexToNbit(newJOpcode, 8));
                    OPCODE8.set(i, usable.hexToNbit(newJOpcode, 8));
                }
            }
            long target = 0L;
            long PC = 0L;
            String BEQoffset = "";
            long temp1 = 0L;

            for (int i = 0; i < BEQXArray.size(); i++) {
                if (!BEQXArray.get(i).equals("")) {
                    target = Long.parseLong((sAddress.get(labelSA.indexOf(BEQXArray.get(i).substring(16))) + "00"), 2);
                    PC = (i + 1) * 4;
                    System.out.println(target + "-" + PC);
                    temp1 = target - PC;
                    if (temp1 < 0) {
                        BEQoffset = Long.toBinaryString(target - PC); //32 bit binary                
                        System.out.println(BEQoffset + " - A");
                        BEQoffset = BEQoffset.substring(BEQoffset.length() - 18, BEQoffset.length() - 2); //correct.
                        System.out.println(target + "-" + PC + " - A");
                    } else {
                        BEQoffset = usable.toBinary(temp1, 18);
                        BEQoffset = BEQoffset.substring(0, BEQoffset.length() - 2);
                        System.out.println(BEQoffset + " - B");

                    }
                    x = BEQXArray.get(i).substring(0, 16) + BEQoffset;
                    System.out.println(x);
                    System.out.println("\n" + BEQoffset + "\n" + x);
                    BigInteger binaryOpcode2 = new BigInteger(x, 2);
                    String newBEQOpcode = new String();
                    newBEQOpcode = binaryOpcode2.toString(16);
                    System.out.println("\nBEQopcode in hex: " + usable.hexToNbit(newBEQOpcode, 8));
                    OPCODE8.set(i, usable.hexToNbit(newBEQOpcode, 8));
                }
            }

            opcodemodel = (DefaultTableModel) jTable1.getModel(); //model = opcode table
            for (int i = 0; i < OPCODE8.size(); i++) {
                opcodemodel.addRow(new Object[]{sInstruction.get(i), OPCODE8.get(i).toUpperCase(), usable.chopHexStringToBinary(OPCODE8.get(i), 0, 5),
                    usable.chopHexStringToBinary(OPCODE8.get(i), 6, 10), usable.chopHexStringToBinary(OPCODE8.get(i), 11, 15),
                    usable.chopHexStringToBinary(OPCODE8.get(i), 16, 31)});
                /* update only 2nd column onwards */
                codemodel.setValueAt(OPCODE8.get(i).toUpperCase(), i, 1);
                codemodel.setValueAt(labelSA.get(i), i, 2);
                codemodel.setValueAt(sInstruction.get(i), i, 3);
            }
            /* opcodemodel to pass to opcodetablecache */
            opcodetablecache = new OpcodeTableCache(opcodemodel.getRowCount(), opcodemodel);

            jButton2.setEnabled(false); //done button
            jButton1.setEnabled(false); //add to code button
            jButton4.setBackground(Color.red); /* reset button */

            // new Pipelinemap(this).processNBInstruction();
//  new Pipelinemap(model).addPLMCol();
//  new Pipelinemap(model).processNBInstruction();

            /* set of Instruction variables */
            int tempInd = 0;
            String hexaddr = "";
            cachedtables = new CachedTables(codemodel, datamodel, opcodemodel, (DefaultTableModel) jTable3.getModel(), opcodemodel.getRowCount());
            /*
             for (int i = 0; i < iList.size(); i++, tempInd++) {
             iList.get(i).setInsNumber(i);
             hexaddr = Integer.toHexString(tempInd * 4);
             hexaddr = usable.hexStringToNBitBinary(hexaddr, 4);
             // iList.get(i).setMemAddrInHex(hexaddr);
             System.out.println("testing [done] ins instance: " + iList.get(i));
             System.out.println(iList.get(i).ALU(cachedtables));
             }
             */
//            System.out.println("testing [done] cachedtables: " + cachedtables);

        } else {
            ERRORnosuchlabel.setVisible(true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox9ActionPerformed

    private void jComboBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox10ActionPerformed

    private void jComboBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11ActionPerformed

    private void jComboBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox12ActionPerformed

    public void registerRESET() {
        for (int i = 0; i < jTable5.getRowCount(); i++) {
            jTable5.setValueAt("0000000000000000", i, 1);
        }
        regModel = (DefaultTableModel) jTable5.getModel();
    }

    private void resetAll() {
        registerRESET();
        iList.clear();
        nIndex2 = 0;
        nIndex = -1;
        ERRORImmLS.setVisible(false);
        ERRORlabel.setVisible(false);
        ERRORnosuchlabel.setVisible(false);
        jButton1.setEnabled(true); //add to code button
        jTextArea1.setText("");
        datamodel.setNumRows(0);
        codemodel.setNumRows(0);
        opcodemodel.setNumRows(0);
        pipelinemodel.setNumRows(0);
        /* Clock Cycle Values  */
        for (int i = 0; i < jTable6.getModel().getRowCount(); i++) {
            for (int j = 1; j < jTable6.getModel().getColumnCount(); j++) {
                jTable6.getModel().setValueAt("", i, j);
            }
        }

        registerRESET();
        jButton2.setEnabled(false); /* DONE button*/

        jButton4.setBackground(Color.white); /* RESET button */

        jButton4.setEnabled(false);
        for (int i = 0; i < OPCODE8.size(); i++) {
            OPCODE8.remove(i);
        }
        for (int i = 0; i < sAddress.size(); i++) {
            sAddress.remove(i);
        }
        for (int i = 0; i < labelSA.size(); i++) {
            labelSA.remove(i);
        }
        for (int i = 0; i < sInstruction.size(); i++) {
            sInstruction.remove(i);
        }
        for (int i = 0; i < JIndexArray.size(); i++) {
            JIndexArray.remove(i);
        }
        for (int i = 0; i < BEQXArray.size(); i++) {
            BEQXArray.remove(i);
        }
        dynamicGeneratedValuesOfAddresses();
        dataSegmentAddresses();
        uponStartHide();
        firstClickSS = true; //first click of single step

        singleExecIndex = 0;

        OPCODE8.clear();
        sAddress.clear();
        labelSA.clear();
        sInstruction.clear();
        JIndexArray.clear();
        BEQXArray.clear();
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        /* RESET BUTTON */
        jButton7.setEnabled(false); //pipeline2

        jTable3.setEnabled(true); //register table
        resetAll();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
    }//GEN-LAST:event_jTextField1KeyPressed

    private void labelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labelKeyTyped

    }//GEN-LAST:event_labelKeyTyped

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed


    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        for (int i = 0; i < datamodel.getRowCount(); i++) {
            if (datamodel.getValueAt(i, 0).toString().equals(jTextField6.getText().toUpperCase())) {
                jTable4.scrollRectToVisible(new Rectangle(jTable4.getCellRect(i, 0, true)));
            }
        }
        for (int i = 0; i < codemodel.getRowCount(); i++) {
            if (codemodel.getValueAt(i, 0).toString().equals(jTextField6.getText().toUpperCase())) {
                jTable5.scrollRectToVisible(new Rectangle(jTable5.getCellRect(i, 0, true)));
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jButton6.setEnabled(false); //full exec button
        jButton5.setEnabled(true); //single exec button
        jButton7.setEnabled(false); //pipeline2 button

        CachedTables ct = new CachedTables(((DefaultTableModel) jTable5.getModel()), ((DefaultTableModel) jTable4.getModel()), ((DefaultTableModel) jTable1.getModel()), ((DefaultTableModel) jTable3.getModel()), opcodemodel.getRowCount());
        pipelinemodel = (DefaultTableModel) jTable2.getModel();
        this.hazardType = new String("PipelineFlush");
        new Pipelinemap(pipelinemodel).buildPipelineMap(iList, ct, (DefaultTableModel) jTable6.getModel(), this.hazardType);

    }//GEN-LAST:event_jButton6ActionPerformed


    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        /* SINGLE EXECUTION */
        jButton6.setEnabled(false);
        if (this.firstClickSS) {
            initPipelineMap();
            //execute first
            this.pm.runCycle(this.singleExecIndex++, this.initialTableState, (DefaultTableModel) jTable6.getModel());
            this.firstClickSS = false;
        } else {
            //execute till the end
            if (this.singleExecIndex < this.pm.getCycleCount()) {
                this.pm.runCycle(this.singleExecIndex++, this.initialTableState, (DefaultTableModel) jTable6.getModel());
            } else {
                System.out.println("program already done!");
                jButton1.setEnabled(false);
                jButton2.setEnabled(false);
                jButton3.setEnabled(false);
                jButton5.setEnabled(false);
                jButton7.setEnabled(false);
            }
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        int r = jTable3.getSelectedRow();
        int c = jTable3.getSelectedColumn();
        System.out.println(r);
        System.out.println(c);
        if (r == 0 && c == 1) {
            JOptionPane.showMessageDialog(this, "value of R0 cannot be edited!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            checkInput(r, c);
        }
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        int r = jTable4.getSelectedRow();
        int c = jTable4.getSelectedColumn();
        System.out.println(r);
        System.out.println(c);
        if (c == 1) {
            checkInputData(r, c);
        } else {
            JOptionPane.showMessageDialog(this, "value cannot be edited!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jTable4MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked

    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jButton6.setEnabled(false); //full exec button
        jButton5.setEnabled(true); //single exec button
        jButton7.setEnabled(false);
        CachedTables ct = new CachedTables(((DefaultTableModel) jTable5.getModel()), ((DefaultTableModel) jTable4.getModel()), ((DefaultTableModel) jTable1.getModel()), ((DefaultTableModel) jTable3.getModel()), opcodemodel.getRowCount());
        pipelinemodel = (DefaultTableModel) jTable2.getModel();
        this.hazardType = new String("Pipeline2");
        new Pipelinemap(pipelinemodel).buildPipelineMap(iList, ct, (DefaultTableModel) jTable6.getModel(), this.hazardType);

    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FRAME1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FRAME1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FRAME1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FRAME1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        MIPSSplash m = new MIPSSplash();
        m.splashInit();           // initialize splash overlay drawing parameters
        m.appInit();              // simulate what an application would do before starting
        if (m.mySplash != null) // check if we really had a spash screen
        {
            m.mySplash.close();   // we're done with it
        }
        // begin with the interactive portion of the program
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FRAME1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ERRORImmLS;
    private javax.swing.JLabel ERRORlabel;
    private javax.swing.JLabel ERRORnosuchlabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox12;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel lblGroup7;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    private java.awt.Panel panel3;
    // End of variables declaration//GEN-END:variables

    private void initPipelineMap() {
        this.hazardType = JOptionPane.showInputDialog("Enter control hazard type: ");
        CachedTables ct = new CachedTables(((DefaultTableModel) jTable5.getModel()), ((DefaultTableModel) jTable4.getModel()), ((DefaultTableModel) jTable1.getModel()), ((DefaultTableModel) jTable3.getModel()), opcodemodel.getRowCount());
        this.initialTableState = new CachedTables(((DefaultTableModel) jTable5.getModel()), ((DefaultTableModel) jTable4.getModel()), ((DefaultTableModel) jTable1.getModel()), ((DefaultTableModel) jTable3.getModel()), opcodemodel.getRowCount());
        pipelinemodel = (DefaultTableModel) jTable2.getModel();
        this.pm = new Pipelinemap(pipelinemodel);
        this.pm.buildPipelineMap(iList, ct, (DefaultTableModel) jTable6.getModel(), hazardType);

        //reset all tables in visuals
        this.initialTableState.getRtc().drawToRegisterTable();
        this.initialTableState.getDtc().drawToTable();
        this.pm.clearTable(pipelinemodel);
        this.pm.clearTable((DefaultTableModel) jTable6.getModel());
    }
}
