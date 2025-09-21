/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libs.Minesweeper.Statistics;

import Libs.Minesweeper.*;
import Libs.Util.*;
import javax.swing.JLabel;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  
import java.util.Date;

/**
 *
 * @author psalm
 */

class Timer implements Runnable {
    int milliseconds = 0;
    MinesweeperRecorder record;
    JLabel timerLabel;
    
    public Timer(MinesweeperRecorder record, JLabel timerLabel) {
        this.record = record;
        this.timerLabel = timerLabel;
    }
    
    public int getTotalTime() {
        return this.milliseconds;
    }

    @Override
    public void run() {
        while (this.record.timerRunning) {
            this.milliseconds += 1;
            try {
                Thread.sleep(1);
            } catch(InterruptedException e) {}
            if (this.timerLabel != null) {
                this.timerLabel.setText(MillisecondFormatter.mmssms(this.milliseconds));
            }
        }
    }
}

public class MinesweeperRecorder {
    Timer timer;
    boolean timerRunning = false;
    Runnable timerCallback;
    MinesweeperGame minesweeperGame;
        
    public MinesweeperRecorder(MinesweeperGame minesweeperGame, JLabel timerlabel) {
        this.minesweeperGame = minesweeperGame;
        this.timer = new Timer(this, timerlabel);
    }
    
    public MinesweeperRecorder(MinesweeperGame minesweeperGame) {
        this.minesweeperGame = minesweeperGame;
        this.timer = new Timer(this, null);
    }
    
    public void startTimer() {
        this.timerRunning = true;
        Thread t = new Thread(this.timer);
        t.start();
    }
    
    public int stopTimer() {
        this.timerRunning = false;
        return this.timer.getTotalTime();
    }
    
    public boolean isTimerRunning() {
        return this.timerRunning;
    }
    
    public MinesweeperRecord getRecord(MinesweeperParameters minesweeperParameters, boolean won) {
        MinesweeperRecord record = new MinesweeperRecord(
                minesweeperParameters.getName(),
                LocalDateTime.now(),
                this.timer.getTotalTime(),
                minesweeperGame.getDimensions(),
                won
        );
        return record;
    }
}
