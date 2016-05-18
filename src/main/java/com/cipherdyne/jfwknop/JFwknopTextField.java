package com.cipherdyne.jfwknop;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

public class JFwknopTextField extends JTextField implements IFwknopVariable {

    private static final long serialVersionUID = 1L;
    private final String defaultVal;

    public JFwknopTextField(final String val) {
        super(val);
        this.defaultVal = val;
        setBackground(new Color(255, 255, 153));
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (((JFwknopTextField)e.getSource()).getText().equals(StringUtils.EMPTY)) {
                    setDefaultValue();
                }
            }
        });
    }
    
    @Override
    public void setDefaultValue() {
        this.setText(this.defaultVal);
    }
    
    @Override
    public boolean isDefault() {
        boolean def = false;
        if (this.defaultVal.equals(this.getText())) {
            def = true;
        }
        
        return def;
    }    
}
