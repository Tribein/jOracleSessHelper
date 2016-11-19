/* 
 * Copyright (c) 2016, lesha
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package odbh;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Comparator;

/**
 *
 * @author Tribein
 */
public class MainForm extends javax.swing.JFrame {

    OraDB oraDBInst;
    OraLDAP ldapInst;
    javax.swing.table.DefaultTableModel mainTableModel;
    javax.swing.table.DefaultTableModel[] minorTableModel;
    boolean minorTableRefreshed;
    Map<String, String> ldapList;
    Map<String, String> ldapPort;
    Map<String, String> ldapService;

    /**
     * Creates new form MainForm
     */
    public MainForm() {

        this.minorTableModel = new javax.swing.table.DefaultTableModel[15];
        this.oraDBInst = new OraDB();
        this.ldapInst = new OraLDAP();
        this.ldapList = new TreeMap<>();
        this.ldapPort = new TreeMap<>();
        this.ldapService = new TreeMap<>();

        initComponents();

        //Initialize LDAP
        if (this.ldapInst.initLDAP("ldap.example.org"/*host*/,"389"/*port*/) == 0) {

            this.ldapList = ldapInst.getTNSRecs();
            this.ldapPort = ldapInst.getTNSPorts();
            this.ldapService = ldapInst.getTNSServices();
        }
        //
        if (!ldapList.isEmpty()) {
            for(Map.Entry<String, String> k : this.ldapList.entrySet()){
                jComboBox1.addItem(k.getKey());
            }                    
        }
        //
        jTextField1.setText(this.oraDBInst.dbHostname);
        jTextField2.setText(this.oraDBInst.dbService);
        jTextField3.setText(this.oraDBInst.dbUsername);
        jTextField4.setText(this.oraDBInst.dbPassword);
        jTextField5.setText(this.oraDBInst.dbPort);
        //
        this.mainTableModel = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        this.minorTableModel[0] = (javax.swing.table.DefaultTableModel) jTable2.getModel();
        this.minorTableModel[1] = (javax.swing.table.DefaultTableModel) jTable3.getModel();
        this.minorTableModel[2] = (javax.swing.table.DefaultTableModel) jTable4.getModel();
        this.minorTableModel[3] = (javax.swing.table.DefaultTableModel) jTable5.getModel();
        this.minorTableModel[4] = (javax.swing.table.DefaultTableModel) jTable6.getModel();
        this.minorTableModel[5] = (javax.swing.table.DefaultTableModel) jTable7.getModel();
        this.minorTableModel[6] = (javax.swing.table.DefaultTableModel) jTable8.getModel();
        this.minorTableModel[7] = (javax.swing.table.DefaultTableModel) jTable9.getModel();
        this.minorTableModel[8] = (javax.swing.table.DefaultTableModel) jTable10.getModel();
        this.minorTableModel[9] = (javax.swing.table.DefaultTableModel) jTable11.getModel();
        this.minorTableModel[10] = (javax.swing.table.DefaultTableModel) jTable12.getModel();
        this.minorTableModel[11] = (javax.swing.table.DefaultTableModel) jTable13.getModel();
        this.minorTableModel[12] = (javax.swing.table.DefaultTableModel) jTable14.getModel();
        this.minorTableModel[13] = (javax.swing.table.DefaultTableModel) jTable15.getModel();
        this.minorTableModel[14] = (javax.swing.table.DefaultTableModel) jTable16.getModel();
        //
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                wrapFillMinorTable();
                minorTableRefreshed = true;
            }
        });
        //
        //ENABLE SORTING
        jTable1.setAutoCreateRowSorter(true);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable1.getModel());
        jTable1.setRowSorter(sorter);
        //LISTEN TO SORTING EVENTS
        sorter.addRowSorterListener(new RowSorterListener() {

            @Override
            public void sorterChanged(RowSorterEvent evt) {
            }
        });
        //SPECIFY COMPARATOR FOR SORTING A SPECIFIC COLUMN
        //ROWNUM
        sorter.setComparator(0, new Comparator<Integer>() {
            @Override
            public int compare(Integer val1, Integer val2) {
                return val1.compareTo(val2);
            }
        });
        //INST_ID
        sorter.setComparator(1, new Comparator<Integer>() {
            @Override
            public int compare(Integer val1, Integer val2) {
                return val1.compareTo(val2);
            }
        });
        //SID
        sorter.setComparator(2, new Comparator<String>() {
            @Override
            public int compare(String val1, String val2) {
                return ((Integer.parseInt(val1.trim()) < Integer.parseInt(val2.trim()))? -1 : 1);
            }
        }); 
        //SERIAL
        sorter.setComparator(3, new Comparator<Integer>() {
            @Override
            public int compare(Integer val1, Integer val2) {
                return val1.compareTo(val2);
            }
        });        
        //SECONDS
        sorter.setComparator(12, new Comparator<Float>() {
            @Override
            public int compare(Float val1, Float val2) {
                return val1.compareTo(val2);
            }
        });
        //PGA_USED_MEM
        sorter.setComparator(13, new Comparator<Float>() {
            @Override
            public int compare(Float val1, Float val2) {
                return val1.compareTo(val2);
            }
        });
        //PGA_ALLOC_MEM
        sorter.setComparator(14, new Comparator<Float>() {
            @Override
            public int compare(Float val1, Float val2) {
                return val1.compareTo(val2);
            }
        });
        //PGA_MAX_MEM
        sorter.setComparator(15, new Comparator<Float>() {
            @Override
            public int compare(Float val1, Float val2) {
                return val1.compareTo(val2);
            }
        });
        //
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jTextField5 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable10 = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable11 = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable12 = new javax.swing.JTable();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable14 = new javax.swing.JTable();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTable15 = new javax.swing.JTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTable16 = new javax.swing.JTable();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ODBH");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Хост");

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Логин");

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Пароль");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Scan");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox1.setMaximumRowCount(32);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTextField5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Порт");

        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jTable1.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "I", "SID", "SERIAL", "STATUS", "USERNAME", "PROCESS", "MODULE", "MACHINE", "LOGON TIME", "STATE", "EVENT", "SECONDS", "PGA_USED", "PGA_ALLOC", "PGA_MAX", "TYPE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1452, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1452, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE))
        );

        jSplitPane2.setLeftComponent(jPanel1);

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jScrollPane2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane2ComponentShown(evt);
            }
        });

        jTable2.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SEQ#", "EVENT", "P1", "P1 TEXT", "P2", "P2 TEXT", "P3", "P3 TEXT", "WAIT TIME", "TIME SINCE LAST WAIT μS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jTabbedPane1.addTab("WAITS", jScrollPane2);

        jScrollPane3.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane3ComponentShown(evt);
            }
        });

        jTable3.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TOTAL WAITS", "EVENT", "TOTAL WAIT mS", "AVG WAIT mS", "TIMEOUTS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setColumnSelectionAllowed(true);
        jScrollPane3.setViewportView(jTable3);
        jTable3.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        jTabbedPane1.addTab("EVENTS", jScrollPane3);

        jScrollPane4.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane4ComponentShown(evt);
            }
        });

        jTable4.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NAME", "VALUE", "STATCLASS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setColumnSelectionAllowed(true);
        jScrollPane4.setViewportView(jTable4);
        jTable4.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        jTabbedPane1.addTab("STATS", jScrollPane4);

        jScrollPane5.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane5ComponentShown(evt);
            }
        });

        jTable5.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "COMMAND", "TIME", "PROGRAM", "OSPID", "RMPID", "RESOURCE GROUP", "ACTION", "PLSQL ENTRY OBJECT", "PLSQL OBJECT", "ENTRY SUBPROGRAM", "SUBPROGRAM", "TRACE", "TRACE WAITS", "TRACE BINDS", "PARALLEL DDL", "PARALLEL DML", "PARALLEL QUERY", "TRACEFILE", "SERVICE_NAME", "SERVICE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.setColumnSelectionAllowed(true);
        jScrollPane5.setViewportView(jTable5);
        jTable5.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jTabbedPane1.addTab("APP", jScrollPane5);

        jScrollPane6.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane6ComponentShown(evt);
            }
        });

        jTable6.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TABLESPACE", "SEGMENT TYPE", "SIZE MB"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTable6);

        jTabbedPane1.addTab("TEMP", jScrollPane6);

        jScrollPane7.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane7ComponentShown(evt);
            }
        });

        jTable7.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "XIDUSN", "RECORDS", "MB"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable7);

        jTabbedPane1.addTab("UNDO", jScrollPane7);

        jTable8.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STATUS", "MESSAGE", "SQL ID", "PLAN LINE", "SQL OPERATION", "SO FAR", "TOTAL", "START TIME", "TIME REMAINING"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTable8);

        jTabbedPane1.addTab("LONGOPS", jScrollPane8);

        jScrollPane9.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane9ComponentShown(evt);
            }
        });

        jTable9.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STATE", "EXEC ID", "EXEC START", "SQL ID", "HASH VALUE", "TEXT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable9.setColumnSelectionAllowed(true);
        jScrollPane9.setViewportView(jTable9);
        jTable9.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable9.getColumnModel().getColumnCount() > 0) {
            jTable9.getColumnModel().getColumn(0).setResizable(false);
        }

        jTabbedPane1.addTab("SQL", jScrollPane9);

        jScrollPane10.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane10ComponentShown(evt);
            }
        });

        jTable10.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "INSTANCE", "SQL ID", "CHILD NUMBER", "HASH VALUE", "PLAN", "ROWS", "CPU COST", "I/O COST", "TIME", "TEMP", "MB"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable10.setColumnSelectionAllowed(true);
        jScrollPane10.setViewportView(jTable10);
        jTable10.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        jTabbedPane1.addTab("PLAN", jScrollPane10);

        jScrollPane11.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane11ComponentShown(evt);
            }
        });

        jTable11.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SQL ID", "POSITION", "DATATYPE", "VALUE", "TIME CAPTURED"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable11.setColumnSelectionAllowed(true);
        jScrollPane11.setViewportView(jTable11);
        jTable11.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        jTabbedPane1.addTab("BINDS", jScrollPane11);

        jScrollPane12.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane12ComponentShown(evt);
            }
        });

        jTable12.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable12.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "INSTANCE", "START TIME", "STATUS", "NAME", "SPACE", "RECURSIVE", "NOUNDO", "PARALLEL", "UNDO BLOCKS", "UNDO RECORDS", "LOGICAL I/O", "PHYSICAL I/O"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane12.setViewportView(jTable12);

        jTabbedPane1.addTab("TRN", jScrollPane12);

        jScrollPane13.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane13ComponentShown(evt);
            }
        });

        jTable13.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable13.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "OWNER / LOCK", "OBJECT / DESCRIPTION", "OBJECT TYPE / TIME", "FILE NAME / LOCKED MODE", "WAIT ROW / BLOCK MODE", "ROW ID / REQUEST MODE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable13.setColumnSelectionAllowed(true);
        jScrollPane13.setViewportView(jTable13);
        jTable13.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        jTabbedPane1.addTab("LOCKS", jScrollPane13);

        jScrollPane14.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane14ComponentShown(evt);
            }
        });

        jTable14.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable14.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "QC INSTANCE", "QC SID", "QC SERIAL", "SID", "P ID", "SERVER GROUP", "SERVER SET", "SERVER", "DEGREE", "REQUIRED DEGREE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable14.setColumnSelectionAllowed(true);
        jScrollPane14.setViewportView(jTable14);
        jTable14.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        jTabbedPane1.addTab("PQ", jScrollPane14);

        jScrollPane15.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane15ComponentShown(evt);
            }
        });

        jTable15.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable15.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CATEGORY", "ALLOCATED MB", "USED MB", "MAX ALLOCATED MB"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane15.setViewportView(jTable15);

        jTabbedPane1.addTab("PGA", jScrollPane15);

        jScrollPane16.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jScrollPane16ComponentShown(evt);
            }
        });

        jTable16.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTable16.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AUTH TYPE", "NET SRV BANNER", "CLIENT CHARSET", "CLIENT CONNECTION", "CLIENT OCI LIBRARY", "CLIENT VERSION", "CLIENT DRIVER", "CLIENT LOBATTR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane16.setViewportView(jTable16);

        jTabbedPane1.addTab("CONN", jScrollPane16);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane18.setViewportView(jTextArea1);

        jTabbedPane1.addTab("SQLMON", jScrollPane18);

        jScrollPane17.setViewportView(jTabbedPane1);
        jTabbedPane1.getAccessibleContext().setAccessibleName("WAITS");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1452, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 1452, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 466, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel3);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("®");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jComboBox2.setMaximumRowCount(2);
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SID", "SERVICE" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
            .addComponent(jSplitPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addGap(30, 30, 30)
                .addComponent(jSplitPane2))
        );

        jButton2.getAccessibleContext().setAccessibleName("jBRefresh");
        jButton2.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void wrapFillMinorTable() {
        int selectedrownum = jTable1.getSelectedRow();
        if (selectedrownum >= 0) {
            int[] vals = {
                Integer.parseInt(jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(selectedrownum), 1).toString().trim()),
                Integer.parseInt(jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(selectedrownum), 2).toString().trim()),
                Integer.parseInt(jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(selectedrownum), 3).toString().trim())
            };
            fillMinorTable(jTabbedPane1.getSelectedIndex(), vals);
        }
    }

    private void fillMainTable() {
        int mytblrowscount;
        Object[][] result;
        mytblrowscount = mainTableModel.getRowCount();
        for (int i = mytblrowscount - 1; i >= 0; i--) {
            mainTableModel.removeRow(i);
        }
        result = oraDBInst.mainQuery();
        for (Object[] result1 : result) {
            mainTableModel.addRow(result1);
        }
    }

    private void fillMinorTable(int tabID, int[] inp) {
        int tableRowsCounter;
        Object[][] result;

        switch (tabID) {
            case 15:
                jTextArea1.setText("");
                break;
            default:
                tableRowsCounter = minorTableModel[tabID].getRowCount();
                for (int i = tableRowsCounter - 1; i >= 0; i--) {
                    minorTableModel[tabID].removeRow(i);
                }
                break;
        }
        result = oraDBInst.minorQuery(tabID, inp);
        if (result != null) {
            for (Object[] r : result) {
                switch (tabID) {
                    case 15://SQLMONITOR
                        jTextArea1.setText(r[0].toString());
                        break;
                    default:
                        minorTableModel[tabID].addRow(r);
                        break;
                }
            }
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (oraDBInst.initmyjdb(jTextField1.getText(), ((jComboBox2.getSelectedIndex()==0)? ":" : "/" ) + jTextField2.getText(), jTextField3.getText(), jTextField4.getText(), jTextField5.getText()) == 0) {
            fillMainTable();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (minorTableRefreshed) {
            minorTableRefreshed = false;
        } else {
            wrapFillMinorTable();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        int tabid = jTabbedPane1.getSelectedIndex();
        int selectedRowNumber = jTable1.getSelectedRow();
        fillMainTable();
        if (selectedRowNumber >= 0) {
            int[] vals = {
                Integer.parseInt(jTable1.getModel().getValueAt(selectedRowNumber, 0).toString().trim()),
                Integer.parseInt(jTable1.getModel().getValueAt(selectedRowNumber, 1).toString().trim()),
                Integer.parseInt(jTable1.getModel().getValueAt(selectedRowNumber, 2).toString().trim())
            };
            fillMinorTable(tabid, vals);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_formComponentShown

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        jTextField1.setText(this.ldapList.get(jComboBox1.getSelectedItem().toString()));
        jTextField2.setText(this.ldapService.get(jComboBox1.getSelectedItem().toString()).substring(1));       
        jTextField5.setText(this.ldapPort.get(jComboBox1.getSelectedItem().toString()));
        jComboBox2.setSelectedIndex((this.ldapService.get(jComboBox1.getSelectedItem().toString()).substring(0,1).equals(":"))? 0:1);
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jScrollPane16ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane16ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane16ComponentShown

    private void jScrollPane15ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane15ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane15ComponentShown

    private void jScrollPane14ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane14ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane14ComponentShown

    private void jScrollPane13ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane13ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane13ComponentShown

    private void jScrollPane12ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane12ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane12ComponentShown

    private void jScrollPane11ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane11ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane11ComponentShown

    private void jScrollPane10ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane10ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane10ComponentShown

    private void jScrollPane9ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane9ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane9ComponentShown

    private void jScrollPane7ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane7ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane7ComponentShown

    private void jScrollPane6ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane6ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane6ComponentShown

    private void jScrollPane5ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane5ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane5ComponentShown

    private void jScrollPane4ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane4ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane4ComponentShown

    private void jScrollPane3ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane3ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane3ComponentShown

    private void jScrollPane2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane2ComponentShown
        wrapFillMinorTable();
    }//GEN-LAST:event_jScrollPane2ComponentShown

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        wrapFillMinorTable();
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed
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
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable10;
    private javax.swing.JTable jTable11;
    private javax.swing.JTable jTable12;
    private javax.swing.JTable jTable13;
    private javax.swing.JTable jTable14;
    private javax.swing.JTable jTable15;
    private javax.swing.JTable jTable16;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
