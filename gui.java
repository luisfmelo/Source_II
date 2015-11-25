/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;

import java.util.Calendar;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luís Melo <luismelo7@gmail.com>
 */
public class gui extends javax.swing.JFrame {

    /**
     * Creates new form gui
     */
    public gui() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        TransformTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        AssembleTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        UnloadTable = new javax.swing.JTable();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MES");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTabbedPane1.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N

        TransformTable.setAutoCreateRowSorter(true);
        TransformTable.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        TransformTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Produced Packages", "On Going Packages", "Pending Packages", "Initial Package", "Final Package", "Check In Time", "Start Time", "Finish Time", "State"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TransformTable);
        TransformTable.getAccessibleContext().setAccessibleName("TransformTable");
        TransformTable.getAccessibleContext().setAccessibleParent(null);

        jTabbedPane1.addTab("Transformation List", jScrollPane1);

        AssembleTable.setAutoCreateRowSorter(true);
        AssembleTable.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        AssembleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Produced Packages", "On Going Packages", "Pending Packages", "Bottom Package", "Top Package", "Check In Time", "Start Time", "Finish Time", "State"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(AssembleTable);

        jTabbedPane1.addTab("Assembling List", jScrollPane2);

        UnloadTable.setAutoCreateRowSorter(true);
        UnloadTable.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        UnloadTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Produced Packages", "On Going Packages", "Pending Packages", "Package", "Pusher Number", "Check In Time", "Start Time", "Finish Time", "State"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(UnloadTable);

        jTabbedPane1.addTab("Unload List", jScrollPane3);
        jTabbedPane1.addTab("Statistics", jTabbedPane2);

        jLabel2.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("MES");

        jLabel1.setText("® David Sousa & Luís Melo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(416, 338));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AssembleTable;
    private javax.swing.JTable TransformTable;
    private javax.swing.JTable UnloadTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    // End of variables declaration//GEN-END:variables
    
    
/**
 *  My Methods
 *  @author Luís Melo <luismelo7@gmail.com>
     * @param id
     * @param qt
     * @param initPkg
     * @param finalPkg
     * @param checkIn
     * @param StartTime
     * @param state
     * @param FinishTime
 */
    public void addNewTransformation(int id, int qt_produ, int qt_ongoing, int qt_pending, int initPkg, int finalPkg, Calendar checkIn, Calendar StartTime, Calendar FinishTime, int state) 
    {
        DefaultTableModel model = (DefaultTableModel) TransformTable.getModel();
        model.addRow(new Object[]{id, qt_produ, qt_ongoing, qt_pending, "P" + initPkg, "P" + finalPkg, checkIn, StartTime, FinishTime, state});
    }
    
    public void addNewAssemble(int id, int qt_produ, int qt_ongoing, int qt_pending, int bottomPkg, int topPkg, Calendar checkIn, Calendar StartTime, Calendar FinishTime, int state)
    {
        DefaultTableModel model = (DefaultTableModel) AssembleTable.getModel();
        model.addRow(new Object[]{id, qt_produ, qt_ongoing, qt_pending, "P" + bottomPkg, "P" + topPkg, checkIn, StartTime, FinishTime, state});
    }
    
    public void addNewUnload(int id, int qt_produ, int qt_ongoing, int qt_pending, int Pkg, int numPusher, Calendar checkIn, Calendar StartTime, Calendar FinishTime, int state)
    {
        DefaultTableModel model = (DefaultTableModel) UnloadTable.getModel();
        model.addRow(new Object[]{id, qt_produ, qt_ongoing, qt_pending, "P" + Pkg, numPusher, checkIn, StartTime, FinishTime, state});
    }
    
    public void oneTransformationGoing(int id) 
    {
        
        DefaultTableModel model = (DefaultTableModel) TransformTable.getModel();
        //int row = model.getValueAt(id, id);
        //row = TransformTable.
        //model.setValueAt("Moo", row, 1); 
    }
    /*
    Minha estrategia (para cada tipo):
    -> oneTransformationGoing:
        se state = pending.... passar para: ongoing
        pending packages: -1
        on going packages: +1
    -> oneTransformationArrived:
        se state = ongoing && ongoing packages = 1.... passar para: finished
        ongoing packages: -1
        produced packages: +1
    */
    
}
