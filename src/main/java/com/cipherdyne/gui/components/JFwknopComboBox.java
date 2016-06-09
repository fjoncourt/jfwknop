package com.cipherdyne.gui.components;

import com.cipherdyne.jfwknop.IFwknopVariable;
import java.awt.Color;

import javax.swing.JComboBox;

public class JFwknopComboBox extends JComboBox<String>implements IFwknopVariable {
    static final long        serialVersionUID   = 1L;
    private static final int DEFAULT_ITEM_INDEX = 0;

    public JFwknopComboBox(final String[] labelArray) {
        super(labelArray);
        setBackground(new Color(255, 255, 153));
        setEditable(false);
        setDefaultValue();
    }

    @Override
    public String getText() {
        return (String) getSelectedItem();
    }

    @Override
    public void setText(final String val) {
        setSelectedItem(val);
    }

    @Override
    public void setDefaultValue() {
        setSelectedIndex(DEFAULT_ITEM_INDEX);
    }
    
    @Override
    public boolean isDefault() {
        boolean def = false;
        if (this.getSelectedIndex() == DEFAULT_ITEM_INDEX) {
            def = true;
        }
        
        return def;
    }        
}
