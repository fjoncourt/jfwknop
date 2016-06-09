/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui.components;

/**
 *
 * @author franck
 */
public class JFwknopArgs extends JFwknopTextField {
    
    private boolean verbosityEnabled;
    private boolean testEnabled;
    private String args;

    public JFwknopArgs(String val) {
        super(val);
        this.setEditable(false);
        this.args = val;
    }
    
    public void setVerbose(boolean enable) {
        this.verbosityEnabled = enable;
        buildArgs();
    }
    
    public void setTest(boolean enable) {
        this.testEnabled = enable;
        buildArgs();
    }
    
    public void setArgs(String args) {
        this.args = args;
        buildArgs();
    }
    
    private void buildArgs() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(this.args);
        if (this.verbosityEnabled) {
            sb.append(" --verbose");
        }        
        if (this.testEnabled) {
            sb.append(" --test");
        }      
        
        this.setText(sb.toString().trim());
    }
}
