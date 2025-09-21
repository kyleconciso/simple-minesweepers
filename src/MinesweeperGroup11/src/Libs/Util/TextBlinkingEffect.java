/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libs.Util;

import java.awt.Color;
import javax.swing.*;


/**
 *
 * @author psalm
 */
public class TextBlinkingEffect {
    JLabel label;
    Thread thread;
    
    
    public TextBlinkingEffect(JLabel label) {
        this.label = label;
    }
    
    public void start() {
        
        this.thread = new Thread() {
            public void run() {
                while (!this.isInterrupted()) {
                    label.setForeground(Color.GRAY);
                    try { 
                          Thread.sleep(500);
                    } catch (InterruptedException e) {break;}
                    label.setForeground(Color.WHITE);
                    try { 
                          Thread.sleep(500);
                    } catch (InterruptedException e) {break;}
                }
            }
        };
        
        this.thread.start();
    }
    
    public void stop() {
        this.thread.interrupt();  
        this.label.setForeground(Color.WHITE);
    }
    
}
