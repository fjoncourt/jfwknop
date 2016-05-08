/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.jfwknop;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextArea;

/**
 *
 * @author franck
 */
public class JFwknopTextArea extends JTextArea implements IFwknopVariable {

    private static final long serialVersionUID = 1L;
    private final String defaultVal;

    public JFwknopTextArea(final String val) {
        super(4, 30);
        this.setLineWrap (true);
        this.setWrapStyleWord(true);
        this.setText(val);
        this.defaultVal = val;
        setBackground(new Color(255, 255, 153));
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (((JFwknopTextArea) e.getSource()).getText().equals("")) {
                    setDefaultValue();
                }
            }
        });
    }

    @Override
    public void setDefaultValue() {
        this.setText(this.defaultVal);
    }
}
