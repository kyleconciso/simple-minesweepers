/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libs.Util;

import java.awt.*;
import javax.swing.*;


/**
 *
 * @author psalm
 */
public class ConfettiEffect {
    String filePath;
    JLabel label;
    JLayeredPane pane;
    
    public ConfettiEffect(String filePath, JLayeredPane pane) {
        this.filePath = filePath;
        this.pane = pane;
    }
    
    public void play() {
        Icon gif = new ImageIcon(new ImageIcon(this.filePath).getImage());
        this.label = new JLabel();
        label.setLayout(null);
        label.setIcon(gif);
        label.setBounds(this.pane.getBounds());
        this.pane.add(label);
        this.pane.setLayer(label, 2);
    }  
    
    public void stop() {
        this.pane.remove(this.label);
    }
}
