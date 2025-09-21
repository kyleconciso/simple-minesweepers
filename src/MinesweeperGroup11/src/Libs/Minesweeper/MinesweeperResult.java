/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Libs.Minesweeper;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author psalm
 */
public class MinesweeperResult {
    boolean hasWon;
    boolean hasLost;
    ArrayList<MinesweeperCell> sweepedCells;
    
    public MinesweeperResult(boolean hasWon, boolean hasLost, ArrayList<MinesweeperCell> sweepedCells) {
        this.hasWon = hasWon;
        this.hasLost = hasLost;
        this.sweepedCells = sweepedCells;
    }
    
    public boolean hasWon() {
        return this.hasWon;
    }
    
    public boolean hasLost() {
        return this.hasLost;
    }
    
    public ArrayList<MinesweeperCell> getSweepedCells() {
        return this.sweepedCells;
    }
}
