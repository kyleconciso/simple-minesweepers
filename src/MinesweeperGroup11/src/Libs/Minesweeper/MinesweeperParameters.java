/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libs.Minesweeper;

import java.awt.Point;

/**
 *
 * @author psalm
 */
public class MinesweeperParameters {
    String name;
    Point dimensions;
    
    public MinesweeperParameters(String name, Point dimensions) {
        this.name = name;
        this.dimensions = dimensions;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Point getDimensions() {
        return this.dimensions;
    }
   
}
