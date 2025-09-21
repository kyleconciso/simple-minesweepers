/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libs.Minesweeper.Statistics;

import java.util.*;
import java.io.*;

/**
 *
 * @author psalm
 */
class MinesweeperStatisticsData implements Serializable {
    ArrayList<MinesweeperRecord> records;
    
    public MinesweeperStatisticsData(ArrayList<MinesweeperRecord> records) {
        this.records = records;
    }
    
    public void addRecord(MinesweeperRecord record){
        this.records.add(record);
    }
    
    public ArrayList<MinesweeperRecord> getRecords() {
        return this.records;
    }
}


public class MinesweeperStatistics {
    
    private static MinesweeperStatistics singletonInstance;
    final String fileName = "src/Assets/Data/statistics.dat";
    private MinesweeperStatisticsData data;
            
    private MinesweeperStatistics() {
        // Attempt to load file
        try {   
            FileInputStream file = new FileInputStream(this.fileName);
            ObjectInputStream in = new ObjectInputStream(file);
            this.data = (MinesweeperStatisticsData)in.readObject();
            
            in.close();
            file.close();
            
        } catch(IOException | ClassNotFoundException ex) {
            System.out.println("");
            // Create minesweeper statistics with empty records
            this.data = new MinesweeperStatisticsData(new ArrayList<>());
        }
    }
    
    public static synchronized MinesweeperStatistics getInstance() {
        if (singletonInstance == null){
            singletonInstance = new MinesweeperStatistics();
        }
        
        return singletonInstance;
    }
    
    public synchronized ArrayList<MinesweeperRecord> getRecords() {
        return this.data.getRecords();
    }
    
    private synchronized void save() {
        // Attempt to save file
        try {   
            FileOutputStream file = new FileOutputStream(this.fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this.data);
            
            out.close();
            file.close();
            
        } catch(IOException e) {
            System.out.println("");
        }
    }
    
    public synchronized void addRecord(MinesweeperRecord record) {
        this.data.addRecord(record);
        this.save();
    }
    
}