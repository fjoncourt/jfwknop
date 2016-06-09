/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui.components;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 * Create a JFwknop label.
 * 
 * @author franck
 */
public class JFwknopLabel extends JLabel {
    
    /**
     * JFwknopLabel constructor
     * 
     * @param label label to display
     */
    public JFwknopLabel(String label) {
        super(label);
        Border paddingBorder = BorderFactory.createEmptyBorder(0, 2, 0, 2);
        MatteBorder border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray);
        this.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
    }
}
