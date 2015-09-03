/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    OraDB myjdb;
    OraLDAP myldap;
    javax.swing.table.DefaultTableModel maintblmodel;
    javax.swing.table.DefaultTableModel[] minortblmodel;
    boolean minortblrefreshed;
    Map<String, String> myldaplist;
    Map<String, String> myldapport;
    Map<String, String> myldapservice;

    /**
     * Creates new form MainForm
     */
    public MainForm() {

        this.minortblmodel = new javax.swing.table.DefaultTableModel[15];
        this.myjdb = new OraDB();
        this.myldap = new OraLDAP();
        this.myldaplist = new TreeMap<>();
        this.myldapport = new TreeMap<>();
        this.myldapservice = new TreeMap<>();

        initComponents();

        //Initialize LDAP
        if (this.myldap.initmyldap("ldaphost.example.org"/*host*/,"389"/*port*/) == 0) {

            this.myldaplist = myldap.gettnsrecs();
            this.myldapport = myldap.gettnsports();
            this.myldapservice = myldap.gettnsservices();
        }
        //
        if (!myldaplist.isEmpty()) {
            for(Map.Entry<String, String> k : this.myldaplist.entrySet()){
                jComboBox1.addItem(k.getKey());
            }                    
        }
        //
        jTextField1.setText(this.myjdb.dbhost);
        jTextField2.setText(this.myjdb.dbsrv);
        jTextField3.setText(this.myjdb.dbusername);
        jTextField4.setText(this.myjdb.dbpassword);
        jTextField5.setText(this.myjdb.dbport);
        //
        this.maintblmodel = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        this.minortblmodel[0] = (javax.swing.table.DefaultTableModel) jTable2.getModel();
        this.minortblmodel[1] = (javax.swing.table.DefaultTableModel) jTable3.getModel();
        this.minortblmodel[2] = (javax.swing.table.DefaultTableModel) jTable4.getModel();
        this.minortblmodel[3] = (javax.swing.table.DefaultTableModel) jTable5.getModel();
        this.minortblmodel[4] = (javax.swing.table.DefaultTableModel) jTable6.getModel();
        this.minortblmodel[5] = (javax.swing.table.DefaultTableModel) jTable7.getModel();
        this.minortblmodel[6] = (javax.swing.table.DefaultTableModel) jTable8.getModel();
        this.minortblmodel[7] = (javax.swing.table.DefaultTableModel) jTable9.getModel();
        this.minortblmodel[8] = (javax.swing.table.DefaultTableModel) jTable10.getModel();
        this.minortblmodel[9] = (javax.swing.table.DefaultTableModel) jTable11.getModel();
        this.minortblmodel[10] = (javax.swing.table.DefaultTableModel) jTable12.getModel();
        this.minortblmodel[11] = (javax.swing.table.DefaultTableModel) jTable13.getModel();
        this.minortblmodel[12] = (javax.swing.table.DefaultTableModel) jTable14.getModel();
        this.minortblmodel[13] = (javax.swing.table.DefaultTableModel) jTable15.getModel();
        this.minortblmodel[14] = (javax.swing.table.DefaultTableModel) jTable16.getModel();
        //
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                wrapfillminortbl();
                minortblrefreshed = true;
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
        jLabel1 = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ODBH");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("SID");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(jLabel1)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addGap(30, 30, 30)
                .addComponent(jSplitPane2))
        );

        jButton2.getAccessibleContext().setAccessibleName("jBRefresh");
        jButton2.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void wrapfillminortbl() {
        int selectedrownum = jTable1.getSelectedRow();
        if (selectedrownum >= 0) {
            int[] vals = {
                Integer.parseInt(jTable1.getModel().getValueAt(selectedrownum, 1).toString().trim()),
                Integer.parseInt(jTable1.getModel().getValueAt(selectedrownum, 2).toString().trim()),
                Integer.parseInt(jTable1.getModel().getValueAt(selectedrownum, 3).toString().trim())
            };
            fillminortbl(jTabbedPane1.getSelectedIndex(), vals);
        }
    }

    private void fillmaintbl() {
        int mytblrowscount;
        Object[][] result;
        mytblrowscount = maintblmodel.getRowCount();
        for (int i = mytblrowscount - 1; i >= 0; i--) {
            maintblmodel.removeRow(i);
        }
        result = myjdb.mainquery();
        for (Object[] result1 : result) {
            maintblmodel.addRow(result1);
        }
    }

    private void fillminortbl(int tabid, int[] inp) {
        int mytblrowscount;
        Object[][] result;

        switch (tabid) {
            case 15:
                jTextArea1.setText("");
                break;
            default:
                mytblrowscount = minortblmodel[tabid].getRowCount();
                for (int i = mytblrowscount - 1; i >= 0; i--) {
                    minortblmodel[tabid].removeRow(i);
                }
                break;
        }
        result = myjdb.minorquery(tabid, inp);
        if (result != null) {
            for (Object[] r : result) {
                switch (tabid) {
                    case 15://SQLMONITOR
                        jTextArea1.setText(r[0].toString());
                        break;
                    default:
                        minortblmodel[tabid].addRow(r);
                        break;
                }
            }
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (myjdb.initmyjdb(jTextField1.getText(), jTextField2.getText(), jTextField3.getText(), jTextField4.getText(), jTextField5.getText()) == 0) {
            fillmaintbl();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (minortblrefreshed) {
            minortblrefreshed = false;
        } else {
            wrapfillminortbl();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        int tabid = jTabbedPane1.getSelectedIndex();
        int selectedrownum = jTable1.getSelectedRow();
        fillmaintbl();
        if (selectedrownum >= 0) {
            int[] vals = {
                Integer.parseInt(jTable1.getModel().getValueAt(selectedrownum, 0).toString().trim()),
                Integer.parseInt(jTable1.getModel().getValueAt(selectedrownum, 1).toString().trim()),
                Integer.parseInt(jTable1.getModel().getValueAt(selectedrownum, 2).toString().trim())
            };
            fillminortbl(tabid, vals);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_formComponentShown

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        jTextField1.setText(this.myldaplist.get(jComboBox1.getSelectedItem().toString()));
        jTextField2.setText(this.myldapservice.get(jComboBox1.getSelectedItem().toString()));        
        jTextField5.setText(this.myldapport.get(jComboBox1.getSelectedItem().toString()));
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jScrollPane16ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane16ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane16ComponentShown

    private void jScrollPane15ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane15ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane15ComponentShown

    private void jScrollPane14ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane14ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane14ComponentShown

    private void jScrollPane13ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane13ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane13ComponentShown

    private void jScrollPane12ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane12ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane12ComponentShown

    private void jScrollPane11ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane11ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane11ComponentShown

    private void jScrollPane10ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane10ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane10ComponentShown

    private void jScrollPane9ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane9ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane9ComponentShown

    private void jScrollPane7ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane7ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane7ComponentShown

    private void jScrollPane6ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane6ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane6ComponentShown

    private void jScrollPane5ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane5ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane5ComponentShown

    private void jScrollPane4ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane4ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane4ComponentShown

    private void jScrollPane3ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane3ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane3ComponentShown

    private void jScrollPane2ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane2ComponentShown
        wrapfillminortbl();
    }//GEN-LAST:event_jScrollPane2ComponentShown

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        wrapfillminortbl();
    }//GEN-LAST:event_jTabbedPane1MouseClicked
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
    private javax.swing.JLabel jLabel1;
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
