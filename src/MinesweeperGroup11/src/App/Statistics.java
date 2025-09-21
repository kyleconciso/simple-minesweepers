/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import Libs.Minesweeper.*;
import Libs.Minesweeper.Statistics.*;
import Libs.Util.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.time.format.*;


/**
 *
 * @author psalm
 */
public class Statistics extends javax.swing.JFrame {
    
    // Get minesweeperStatistics
    MinesweeperStatistics minesweeperStatistics = MinesweeperStatistics.getInstance();
    
    // Store return game parameters
    MinesweeperParameters minesweeperParameters;
    
    // Flag to avoid triggering event when populating combo box
    boolean busyPopulating = false;
    
    public Statistics(MinesweeperParameters minesweeperParameters) {
        initComponents();
        
        // Store parameters
        this.minesweeperParameters = minesweeperParameters;
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        recordsTable.setDefaultRenderer(String.class, centerRenderer);

        // Loop through leaderboard
        ArrayList<MinesweeperRecord> records = minesweeperStatistics.getRecords();
        DefaultTableModel table = (DefaultTableModel) this.recordsTable.getModel();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
        for (MinesweeperRecord record : records) {
            table.addRow(new Object[]{
                record.getDate().format(formatter),
                record.getName(),
                record.getGrid(),
                record.getWon() ? "Won" : "Lost",
                MillisecondFormatter.mmssms(record.getMilliseconds())
            });
        }
        
        // Action listener for cells
        this.recordsTable.addMouseListener(
            new MouseAdapter() {
                Statistics statistics;
                private MouseAdapter init(Statistics statistics) {
                    this.statistics = statistics;
                    return this;
                }
                @Override
                public void mouseClicked(MouseEvent e){
                    if ((e.getButton() != 1) || (gridComboBox.getSelectedItem()==null)) {return;}
                    String name = (String) recordsTable.getValueAt(recordsTable.getSelectedRow(), 1);
                    this.statistics.updatePlayerPerformance(name);
                }
            }.init(this)
        );
        
        // Initialize grid combo box
        this.gridComboBox.setVisible(false);
        this.gridComboBox.removeAllItems();
        this.gridComboBox.addItem("All");
    }
    
    
    private void updatePlayerPerformance(String name) {
        
        boolean changed = !this.playerValueLabel.getText().equals(name);
        
        HashMap<String, Boolean> playerGrids = new HashMap<>();
        
        // Update gridComboBox
        if (changed) {
            busyPopulating = true;
                
            for (MinesweeperRecord record : this.minesweeperStatistics.getRecords()) {
                if (record.getName().equals(name)) {
                    String grid = record.getGrid();
                    if (playerGrids.get(grid)==null){
                        playerGrids.put(grid, true);
                    }
                }
            }

            this.gridComboBox.setVisible(true);
            this.gridComboBox.removeAllItems();
            this.gridComboBox.addItem("All");
            
            for (HashMap.Entry v : playerGrids.entrySet()) {
                String grid = (String) v.getKey();
                this.gridComboBox.addItem(grid);
            }
            
            busyPopulating = false;
        }
        
        // Iterate stats
        String chosenGrid = (String) this.gridComboBox.getSelectedItem();
        System.out.println(chosenGrid);
        int wins = 0;
        int loses = 0;
        int longestDuration = 0;
        int shortestDuration = Integer.MAX_VALUE;
        ArrayList<Integer> durations = new ArrayList<>();
        
        if (chosenGrid==null) {return;}
        
        for (MinesweeperRecord record : this.minesweeperStatistics.getRecords()) {
            if ((record.getName().equals(name))&&(chosenGrid.equals("All") || record.getGrid().equals(chosenGrid))) {
                if (record.getWon()) {
                    int duration = record.getMilliseconds();
                    durations.add(duration);
                    longestDuration = duration > longestDuration ? duration : longestDuration;
                    shortestDuration = duration < shortestDuration ? duration : shortestDuration;
                }
                if (record.getWon()==true)
                    wins +=1;
                else
                    loses +=1;
            }
        }
        
        // Compute average duration
        int averageSum = 0;
        for (int v : durations)
            averageSum += v;
        
        int averageDuration = averageSum/ (!durations.isEmpty() ? durations.size() : 1);
        
        // Compute wr
        int total = wins+loses;
        double wr = Math.round(((double)wins/((double)total)*10000))/100;
       
        this.playerValueLabel.setText(name);
        this.totalGamesValueLabel.setText(Integer.toString(total));
        this.wrValueLabel.setText(Double.toString(wr)+"%");
        this.winsValueLabel.setText(Integer.toString(wins));
        this.losesValueLabel.setText(Integer.toString(loses));
        
        if (!durations.isEmpty()) {
            this.averageValueLabel.setText(MillisecondFormatter.mmssms(averageDuration));
            this.shortestValueLabel.setText(MillisecondFormatter.mmssms(shortestDuration));
            this.longestValueLabel.setText(MillisecondFormatter.mmssms(longestDuration));
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        recordsTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        playerLabel = new javax.swing.JLabel();
        wrLabel = new javax.swing.JLabel();
        totalGamesLabel = new javax.swing.JLabel();
        winsLabel = new javax.swing.JLabel();
        losesLabel = new javax.swing.JLabel();
        playerValueLabel = new javax.swing.JLabel();
        totalGamesValueLabel = new javax.swing.JLabel();
        wrValueLabel = new javax.swing.JLabel();
        winsValueLabel = new javax.swing.JLabel();
        losesValueLabel = new javax.swing.JLabel();
        averageLabel = new javax.swing.JLabel();
        averageValueLabel = new javax.swing.JLabel();
        shortestLabel = new javax.swing.JLabel();
        shortestValueLabel = new javax.swing.JLabel();
        longestValueLabel = new javax.swing.JLabel();
        longestLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        gridComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        recordsTable.setAutoCreateRowSorter(true);
        recordsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Name", "Grid", "Results", "Duration"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
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
        jScrollPane1.setViewportView(recordsTable);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Records");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Click on a record to access their performance.");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Statistics");

        playerLabel.setForeground(new java.awt.Color(153, 153, 153));
        playerLabel.setText("Player:");

        wrLabel.setForeground(new java.awt.Color(153, 153, 153));
        wrLabel.setText("W/R (Win Rate):");

        totalGamesLabel.setForeground(new java.awt.Color(153, 153, 153));
        totalGamesLabel.setText("Total Games:");

        winsLabel.setForeground(new java.awt.Color(153, 153, 153));
        winsLabel.setText("Wins:");

        losesLabel.setForeground(new java.awt.Color(153, 153, 153));
        losesLabel.setText("Loses:");

        playerValueLabel.setText("N/A");

        totalGamesValueLabel.setText("N/A");

        wrValueLabel.setText("N/A");

        winsValueLabel.setText("N/A");

        losesValueLabel.setText("N/A");

        averageLabel.setForeground(new java.awt.Color(153, 153, 153));
        averageLabel.setText("Average Win Duration:");

        averageValueLabel.setText("N/A");

        shortestLabel.setForeground(new java.awt.Color(153, 153, 153));
        shortestLabel.setText("Shortest Win:");

        shortestValueLabel.setText("N/A");

        longestValueLabel.setText("N/A");

        longestLabel.setForeground(new java.awt.Color(153, 153, 153));
        longestLabel.setText("Longest Win:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(playerLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(playerValueLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(averageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(averageValueLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(totalGamesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalGamesValueLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(shortestLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(shortestValueLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(winsLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(winsValueLabel))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(losesLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(losesValueLabel)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(wrLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(wrValueLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(longestLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(longestValueLabel)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(averageLabel)
                        .addComponent(averageValueLabel))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(playerLabel)
                        .addComponent(playerValueLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(shortestLabel)
                        .addComponent(shortestValueLabel))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totalGamesLabel)
                        .addComponent(totalGamesValueLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(longestLabel)
                        .addComponent(longestValueLabel))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(wrLabel)
                        .addComponent(wrValueLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(winsLabel)
                    .addComponent(winsValueLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(losesLabel)
                    .addComponent(losesValueLabel))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jButton1.setText("<< Return to Game");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        gridComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gridComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gridComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        Gameplay.main(minesweeperParameters);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void gridComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridComboBoxActionPerformed
        if (this.busyPopulating) {return;}
        this.updatePlayerPerformance(this.playerValueLabel.getText());
    }//GEN-LAST:event_gridComboBoxActionPerformed

    /**
     * @param minesweeperParameters
     */
    public static void main(MinesweeperParameters minesweeperParameters) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        FlatMacDarkLaf.setup();
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Statistics app = new Statistics(minesweeperParameters);
                app.setVisible(true);
                app.setTitle("Simple Minesweepers");
                app.setDefaultCloseOperation(EXIT_ON_CLOSE);
                app.setLocationRelativeTo(null);            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel averageLabel;
    private javax.swing.JLabel averageValueLabel;
    private javax.swing.JComboBox<String> gridComboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel longestLabel;
    private javax.swing.JLabel longestValueLabel;
    private javax.swing.JLabel losesLabel;
    private javax.swing.JLabel losesValueLabel;
    private javax.swing.JLabel playerLabel;
    private javax.swing.JLabel playerValueLabel;
    private javax.swing.JTable recordsTable;
    private javax.swing.JLabel shortestLabel;
    private javax.swing.JLabel shortestValueLabel;
    private javax.swing.JLabel totalGamesLabel;
    private javax.swing.JLabel totalGamesValueLabel;
    private javax.swing.JLabel winsLabel;
    private javax.swing.JLabel winsValueLabel;
    private javax.swing.JLabel wrLabel;
    private javax.swing.JLabel wrValueLabel;
    // End of variables declaration//GEN-END:variables
}
