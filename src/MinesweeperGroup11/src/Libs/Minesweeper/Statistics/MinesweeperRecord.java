/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libs.Minesweeper.Statistics;

import java.io.*;
import java.util.*;
import java.awt.Point;
import java.time.LocalDateTime;  

/**
 *
 * @author psalm
 */
public class MinesweeperRecord implements Serializable {
    String name;
    LocalDateTime date;
    int milliseconds;
    Point dimensions;
    boolean won;
    
    public MinesweeperRecord(String name, LocalDateTime date, int milliseconds, Point dimensions, boolean won) {
        this.name = name;
        this.date = date;
        this.milliseconds = milliseconds;
        this.dimensions = dimensions;
        this.won = won;
    }
    
    public String getName() {
        return this.name;
    }
    
    public LocalDateTime getDate() {
        return this.date;
    }
    
    public int getMilliseconds() {
        return this.milliseconds;
    }
    
    public String getGrid() {
        return String.format("%dx%d", dimensions.x, dimensions.y);
    }
    
    public boolean getWon() {
        return this.won;
    }
}

