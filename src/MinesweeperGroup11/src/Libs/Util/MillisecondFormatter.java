/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libs.Util;

/**
 *
 * @author psalm
 */

// A Util function for formatting Milliseconds into MM:SS.SS
public class MillisecondFormatter {
    static private String formatDigit(double n) {
        String s = Integer.toString((int)n);
        return n<10 ? "0" + s : s;
    }
    
    static public String mmssms(int milliseconds) {
        double s = ((double)milliseconds)/1000;
        double mm = Math.floor(s/60);
        double ss = Math.floor(s-(mm*60));
        double ms = Math.floor((s*100)-(ss*100)-(mm*6000));
        return String.format("%s:%s.%s", formatDigit(mm), formatDigit(ss), formatDigit(ms));
    }
}
