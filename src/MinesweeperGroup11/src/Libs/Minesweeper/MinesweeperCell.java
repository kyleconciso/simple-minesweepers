/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Libs.Minesweeper;

import java.awt.Point;

/**
 *
 * @author psalm
 */
public class MinesweeperCell {
    Point location; 
    boolean isMine;
    boolean isRevealed;
    int mineCount;
    

    
    
    public MinesweeperCell(Point location, boolean isMine) {
        this.location = location;
        this.isMine = isMine;
    }
    
    public boolean isMine() {
        return this.isMine;
    }
    
    public void reveal() {
        this.isRevealed = true;
    }
    
    public boolean isRevealed() {
        return this.isRevealed;
    }
    
    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }
    
    public int getMineCount() {
        return this.mineCount;
    }
    
    public Point getLocation() {
        return this.location;
    }
    
    
    
}
