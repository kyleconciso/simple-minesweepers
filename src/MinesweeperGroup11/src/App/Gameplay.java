/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import Libs.Minesweeper.Statistics.MinesweeperRecorder;
import Libs.Util.*;
import Libs.Minesweeper.*;
import Libs.Minesweeper.Statistics.MinesweeperStatistics;
import java.awt.Point;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Icon;
import java.util.HashMap;
import java.io.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.util.ArrayList;
import java.util.Map.Entry;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
/**
 *
 * @author psalm
 */

public class Gameplay extends javax.swing.JFrame {
    
    MinesweeperGame minesweeper;
    MinesweeperParameters minesweeperParameters;
    MinesweeperRecorder minesweeperRecorder;
    float minesweeperCellSize;    
    Point minesweeperDimensions;
    boolean started = false;
    boolean finished = false;
    
    // Get minesweeper statistics
    MinesweeperStatistics minesweeperStatistics = MinesweeperStatistics.getInstance();
    
    // A map of JButtons to MinesweeperCells, and vice versa
    HashMap<JButton, MinesweeperCell> buttonCells = new HashMap<>();  
    HashMap<MinesweeperCell, JButton> cellButtons = new HashMap<>();  

    // A map of JButtons to boolean (used for tracking flags)
    HashMap<JButton, Boolean> buttonFlags = new HashMap<>();
    
    // A map of X,Y point to JButtons, and vice versa
    HashMap<Point, JButton> xyButtons = new HashMap<>();
    HashMap<JButton, Point> buttonsXy = new HashMap<>();

    // An array of colors (used for mineCount colors)
    Color[] mineCountColors = {
        new Color(255, 255, 255), new Color(135, 177, 255), new Color(60, 222, 60),
        new Color(247, 161, 146), new Color(77, 49, 214), new Color(184, 15, 37),
        new Color(7, 139, 168), new Color(15, 15, 15), new Color(148, 146, 146)
    };
    Color unrevealedColor = new Color(200, 200, 200);
    Color revealedColor = new Color(50, 50, 50);

    // For image assets
    Icon bombIcon;
    Icon boomIcon;
    Icon flagIcon;
    
    // Sound players
    SoundPlayer boomSoundPlayer = new SoundPlayer("src/Assets/Sounds/boom.wav");
    SoundPlayer tickSoundPlayer = new SoundPlayer("src/Assets/Sounds/tick.wav");
    SoundPlayer winSoundPlayer = new SoundPlayer("src/Assets/Sounds/win.wav");
    SoundPlayer restartSoundPlayer = new SoundPlayer("src/Assets/Sounds/restart.wav");
    SoundPlayer suspenseSoundPlayer = new SoundPlayer("src/Assets/Sounds/suspense.wav");
    SoundPlayer flagSoundPlayer = new SoundPlayer("src/Assets/Sounds/flag.wav");
    SoundPlayer timerSoundPlayer = new SoundPlayer("src/Assets/Sounds/timer.wav");


    public Gameplay(MinesweeperParameters params) {
        initComponents();
        
        // Get params
        this.minesweeperParameters = params;
        this.minesweeperDimensions = params.getDimensions();
        
        // Create new Minesweeper game
        minesweeper = new MinesweeperGame(
                minesweeperDimensions
        );
        
        // Create minesweeperRecorder
        minesweeperRecorder = new MinesweeperRecorder(minesweeper, this.timerLabel);
        
        // Calculate optimal size for cell components
        minesweeperCellSize = ((float) this.getSize().width) / ((float) minesweeperDimensions.x) * 0.5f;                
        
        // <editor-fold defaultstate="collapsed" desc="Get and resize image assets">   
        bombIcon = new ImageIcon (
                new ImageIcon("src/Assets/Images/bomb.png").getImage()
                        .getScaledInstance(
                                (int) minesweeperCellSize, 
                                (int) minesweeperCellSize,
                                Image.SCALE_DEFAULT)
        );

        boomIcon = new ImageIcon (
        new ImageIcon("src/Assets/Images/boom.png").getImage()
                .getScaledInstance(
                        (int) minesweeperCellSize, 
                        (int) minesweeperCellSize, 
                        Image.SCALE_DEFAULT)
        );

        flagIcon = new ImageIcon (
        new ImageIcon("src/Assets/Images/flag.png").getImage()
                .getScaledInstance(
                        (int) minesweeperCellSize, 
                        (int) minesweeperCellSize, 
                        Image.SCALE_DEFAULT)
        );//</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Listeners for JButtons (cells)">    
        ActionListener cellClicked = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (finished) {return;}
                
                // Started callback
                if (!started) {
                    started = true;
                    startedCallback(buttonsXy.get((JButton)e.getSource()));
                }
                
                // Get button and cell
                JButton button = (JButton) e.getSource();
                MinesweeperCell cell = buttonCells.get(button);
                                
                // Get minesweeper results
                MinesweeperResult result = minesweeper.turn(cell);
                ArrayList<MinesweeperCell> sweepedCells = result.getSweepedCells();
                
                if (result.hasLost() && !result.hasWon()) {
                    lostCallback(button);
                } else if (!result.hasLost() && result.hasWon()) {
                    wonCallback();
                } else if (sweepedCells != null) {
                    sweepCallback(sweepedCells);
                }
            }
        };
        
        MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                if (finished) {return;}
                if (e.getButton()==3 && !buttonCells.get(button).isRevealed()) {
                    flagSoundPlayer.play();
                    boolean isFlag = buttonFlags.get(button);
                    buttonFlags.put(button, !isFlag);
                    if (!isFlag) {
                        button.setIcon(flagIcon);
                    } else {
                        button.setIcon(null);
                    }
                }
            }
        };//</editor-fold>  

        // <editor-fold defaultstate="collapsed" desc="Create JButtons (cells) in grid layout panel">  
        // Setup grid panel
        GridLayout layout = new GridLayout(minesweeperDimensions.x, minesweeperDimensions.y);
        gridPanel.setLayout(layout);
        gameplayPanel.setLayer(gridPanel, 1);
        
        for (int y=0; y<minesweeperDimensions.y; y++) {
            for (int x=0; x<minesweeperDimensions.x; x++) {
                // Create button and add to panel
                JButton button = new JButton();
                button.setFont(button.getFont().deriveFont(minesweeperCellSize));
                button.setForeground(Color.WHITE);
                button.addActionListener(cellClicked);
                button.addMouseListener(mouseListener);
                gridPanel.add(button);
                // Add to xyButtons and buttonsXy
                xyButtons.put(new Point(x,y), button);
                buttonsXy.put(button, new Point(x,y));
            }
        }// </editor-fold> 
    }
    
    // Map grid buttons to minesweeper cells
    private void mapGridPanel() {
        for (int y=0; y<minesweeperDimensions.y; y++) {
            for (int x=0; x<minesweeperDimensions.x; x++) {
                // Get button and cell
                JButton button = xyButtons.get(new Point(x,y));
                MinesweeperCell cell = minesweeper.getCell(new Point(x, y));
                // Map
                this.buttonCells.put(button, cell);
                this.cellButtons.put(cell, button);
                this.buttonFlags.put(button, false);
            }
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Minesweeper Callbacks">  
    
    // Started callback
    private void startedCallback(Point startingXy) {
        this.minesweeper.fill(startingXy);
        this.mapGridPanel();
        this.restartGameMenuItem.setEnabled(true);
        this.statusLabel.setText("Time elapsed");
        this.minesweeperRecorder.startTimer();
        this.timerSoundPlayer.play();
    }
    
    // Sweep callback
    private void sweepCallback(ArrayList<MinesweeperCell> sweepedCells) {
        boolean suspenseSoundPlayed = false;
        // Play sound
        this.tickSoundPlayer.play();
        // Sweeped cells
        for (int i = 0; i < sweepedCells.size(); i++) {
            // Find button
            MinesweeperCell sweepedCell = sweepedCells.get(i);
            int mineCount = sweepedCell.getMineCount();
            JButton sweepedButton = cellButtons.get(sweepedCell);
            sweepedButton.setIcon(null);
            sweepedButton.setBackground(revealedColor);
            sweepedButton.setForeground(mineCountColors[mineCount]);
            sweepedButton.setText(mineCount > 0 ? Integer.toString(mineCount):"");
            if (mineCount > 2 && !suspenseSoundPlayed) {
                suspenseSoundPlayed = true;
                suspenseSoundPlayer.play();
            }
        }
    }

    // Won callback
    private void wonCallback() {
        finished = true;
        // Sound
        winSoundPlayer.play();
        // Effects
        ConfettiEffect ce = new ConfettiEffect("src/assets/Images/confetti.gif", this.gameplayPanel);
        ce.play();
        // Reveal grid
        reveal();
        // Stop timer
        this.minesweeperRecorder.stopTimer();
        // Message
        TextBlinkingEffect tbe = new TextBlinkingEffect(this.timerLabel);
        tbe.start();
        JOptionPane.showMessageDialog(null, "Congratulations! You won!", "Simple Minesweepers", JOptionPane.INFORMATION_MESSAGE);
        tbe.stop();
        minesweeperStatistics.addRecord(minesweeperRecorder.getRecord(this.minesweeperParameters, true));
        this.statusLabel.setText("Press Ctrl + R to restart the Game, Ctrl + N to start a new game.");
    }
    
    // Lost callback
    private void lostCallback(JButton boomButton) {
        finished = true;
        // Play sound
        boomSoundPlayer.play();
        // Reveal grid
        reveal();
        // Update ui
        boomButton.setIcon(boomIcon);
        boomButton.setBackground(new Color(156, 56, 39));
        // Stop timer
        this.minesweeperRecorder.stopTimer();
        // Message
        TextBlinkingEffect tbe = new TextBlinkingEffect(this.timerLabel);
        tbe.start();
        JOptionPane.showMessageDialog(null, "You lost!", "Simple Minesweepers", JOptionPane.INFORMATION_MESSAGE);
        tbe.stop();
        minesweeperStatistics.addRecord(minesweeperRecorder.getRecord(this.minesweeperParameters, false));
        this.statusLabel.setText("Press Ctrl + R to restart the Game, Ctrl + N to start a new game.");
    }
    
    private void reveal() {
        // Update grid panel
        buttonCells.forEach((key, value) -> {
            String mineCount = Integer.toString(value.getMineCount());
            key.setBackground(revealedColor);
            key.setForeground(mineCountColors[value.getMineCount()]);
            key.setText(!value.isMine() ? (value.getMineCount() > 0 ? mineCount : "") : "");
            key.setIcon(null);
            if (value.isMine()) {
                key.setIcon(bombIcon);
            }
        });
    } //</editor-fold>
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        titlePanel = new javax.swing.JPanel();
        timerLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        gameplayPanel = new javax.swing.JLayeredPane();
        gridPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        gameMenu = new javax.swing.JMenu();
        newGameMenuItem = new javax.swing.JMenuItem();
        restartGameMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitGame = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        developersOfTheGame = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        timerLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText("Ready");

        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        statusLabel.setForeground(new java.awt.Color(102, 102, 102));
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLabel.setText("Click on the tiles to start the timer");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(timerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addComponent(timerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusLabel))
        );

        gameplayPanel.setMaximumSize(new java.awt.Dimension(500, 500));
        gameplayPanel.setMinimumSize(new java.awt.Dimension(500, 500));

        gridPanel.setMaximumSize(new java.awt.Dimension(500, 500));
        gridPanel.setMinimumSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout gridPanelLayout = new javax.swing.GroupLayout(gridPanel);
        gridPanel.setLayout(gridPanelLayout);
        gridPanelLayout.setHorizontalGroup(
            gridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        gridPanelLayout.setVerticalGroup(
            gridPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        gameplayPanel.setLayer(gridPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout gameplayPanelLayout = new javax.swing.GroupLayout(gameplayPanel);
        gameplayPanel.setLayout(gameplayPanelLayout);
        gameplayPanelLayout.setHorizontalGroup(
            gameplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gridPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        gameplayPanelLayout.setVerticalGroup(
            gameplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gridPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        gameMenu.setText("Game");

        newGameMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newGameMenuItem.setText("New Game");
        newGameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameMenuItemActionPerformed(evt);
            }
        });
        gameMenu.add(newGameMenuItem);

        restartGameMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        restartGameMenuItem.setText("Restart Game");
        restartGameMenuItem.setEnabled(false);
        restartGameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartGameMenuItemActionEvent(evt);
            }
        });
        gameMenu.add(restartGameMenuItem);
        gameMenu.add(jSeparator1);

        exitGame.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        exitGame.setText("Exit");
        exitGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitGame(evt);
            }
        });
        gameMenu.add(exitGame);

        jMenuBar1.add(gameMenu);

        jMenu1.setText("Statistics");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Go To Statistics");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        helpMenu.setText("Help");
        helpMenu.setMaximumSize(new java.awt.Dimension(39, 32767));
        helpMenu.setPreferredSize(new java.awt.Dimension(39, 19));

        developersOfTheGame.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        developersOfTheGame.setText("Developers");
        developersOfTheGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devsInfoActionEvent(evt);
            }
        });
        helpMenu.add(developersOfTheGame);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titlePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(gameplayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(gameplayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Action listener for the Menu"> 
    private void restartGameMenuItemActionEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartGameMenuItemActionEvent
        Gameplay app = new Gameplay(this.minesweeperParameters);
        app.setVisible(true);
        app.setTitle("Simple Minesweepers");
        app.setDefaultCloseOperation(EXIT_ON_CLOSE);
        app.setLocationRelativeTo(null);
        this.dispose();
        restartSoundPlayer.play();
    }//GEN-LAST:event_restartGameMenuItemActionEvent

    private void exitGame(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitGame
        System.exit(0);
    }//GEN-LAST:event_exitGame

    private void devsInfoActionEvent(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devsInfoActionEvent
        String devs = "Welcome to Simple Minesweepers! \n\n"
                + "Made with ❤ by Group 11\n"
                + "Conciso, Psalmwel Kyle \n"
                + "Manikan, Isaiah Gabriel\n"
                + "Medina, Art Gabriel\n";
        JOptionPane.showMessageDialog(null, devs, "Developers", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_devsInfoActionEvent

    private void newGameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameMenuItemActionPerformed
        this.dispose();
        Start.main(new String[0]);
    }//GEN-LAST:event_newGameMenuItemActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (this.started && !this.finished) {
            int result = JOptionPane.showConfirmDialog(this,"Game progress will not be saved. Continue?", "Simple Minesweepers",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.NO_OPTION){
                return;
            }
        }
        this.dispose();
        Statistics.main(this.minesweeperParameters);
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    //</editor-fold>  
    
    public static void main(MinesweeperParameters params) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        FlatMacDarkLaf.setup();
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Gameplay app = new Gameplay(params);
                app.setVisible(true);
                app.setTitle("Simple Minesweepers");
                app.setDefaultCloseOperation(EXIT_ON_CLOSE);
                app.setLocationRelativeTo(null);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem developersOfTheGame;
    private javax.swing.JMenuItem exitGame;
    private javax.swing.JMenu gameMenu;
    private javax.swing.JLayeredPane gameplayPanel;
    private javax.swing.JPanel gridPanel;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenuItem newGameMenuItem;
    private javax.swing.JMenuItem restartGameMenuItem;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel timerLabel;
    private javax.swing.JPanel titlePanel;
    // End of variables declaration//GEN-END:variables
}
