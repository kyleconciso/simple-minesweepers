/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Libs.Minesweeper;

import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author psalm
 */
public class MinesweeperGame {
    MinesweeperCell[][] grid;
    MinesweeperResult latestResult;
    Point dimensions;
    
    public MinesweeperGame(Point dimensions) {
        this.dimensions = dimensions;
        this.grid = new MinesweeperCell[dimensions.x][dimensions.y];
    }
    
    public void fill(Point startingCell) {        
        Random rand = new Random();
        double chancesOfMine = 0.1d+(((double)dimensions.x*(double)dimensions.y)/9000d); // 0.1+(x*y)/9000
        // Occupy grid
        for (int y=0; y<dimensions.y; y++) {
            for (int x=0; x<dimensions.x; x++) {
                boolean isMine = rand.nextInt(100) <= (100*chancesOfMine);
                this.grid[x][y] = new MinesweeperCell(
                        new Point(x,y),
                        startingCell.equals(new Point(x,y)) ? false : isMine
                );
            }
        }
        
        // Set mineCounts of all cells
        for (int y=0; y<dimensions.y; y++) {
            for (int x=0; x<dimensions.x; x++) {
                MinesweeperCell cell = this.getCell(new Point(x,y));
                cell.setMineCount(this.getNeighbouringMineCount(cell));
            }
        }
    }
    
    public MinesweeperResult turn(MinesweeperCell cell) {            
        cell.reveal();
       
        if (cell.isMine()) {
            return new MinesweeperResult(false, true, null);
        } else {
            ArrayList<MinesweeperCell> sweepedCells = new ArrayList();
            this.sweep(cell, sweepedCells);
            return new MinesweeperResult(this.hasWon(), false, sweepedCells);
        }
    }
    
    private void sweep(MinesweeperCell cell, ArrayList<MinesweeperCell> sweepedCells) {
        // Recursive flood fill algorithm
        cell.reveal();
        sweepedCells.add(cell);
  
        // Iterate neighbours, sweep if not a mine and not revealed yet
        ArrayList<MinesweeperCell> neighbours = this.getNeighbouringCells(cell);
        for (int i=0; i < neighbours.size(); i++) {
            MinesweeperCell neighbourCell = neighbours.get(i);
            if (!neighbourCell.isMine() &&
                    !neighbourCell.isRevealed) {
                // Continue if cell has mines
                if (cell.getMineCount()>0) {
                    continue;
                }
                this.sweep(neighbourCell, sweepedCells);
            }
        }
    }
    
    private ArrayList<MinesweeperCell> getNeighbouringCells(MinesweeperCell cell) {
        // Gets neighboring cells from 8 directions
        ArrayList<MinesweeperCell> neighbours = new ArrayList();
        
        Point xy = cell.getLocation();
        
        for (int x=-1; x<=1; x++) {
            for (int y=-1; y<=1; y++) {
                Point point = new Point(xy.x+x,xy.y+y);
                
                // Check if neighbour point is in bound and not the center
                if ((point.x < this.dimensions.x && point.x >= 0) &&
                        (point.y < this.dimensions.y && point.y >= 0) &&
                                !(point.equals(xy))) {
                    neighbours.add(this.getCell(point));
                }
            }
        }
        
        return neighbours;
    }
    
    private int getNeighbouringMineCount(MinesweeperCell cell){
        ArrayList<MinesweeperCell> neighbours = this.getNeighbouringCells(cell);
        int mineCount = 0;
        for (int i=0; i < neighbours.size(); i++) {
            MinesweeperCell neighbourCell = neighbours.get(i);
            if (neighbourCell.isMine()) {
                mineCount += 1;
            }
        }
        return mineCount;
    }
    
    private boolean hasWon() {
        boolean won = true;
        for (int x=0; x<this.dimensions.x; x++) {
            for (int y=0; y<this.dimensions.y; y++) {
                MinesweeperCell cell = this.getCell(new Point(x,y));
                if (!cell.isMine && !cell.isRevealed) {
                    won = false;
                    break;
                }
            }
        }
        return won;
    }
    
    public MinesweeperCell getCell(Point xy) {
        return grid[xy.x][xy.y];
    }
    
    public MinesweeperCell[][] getGrid() {
        return this.grid;
    }
    
    public Point getDimensions() {
        return this.dimensions;
    }
}
