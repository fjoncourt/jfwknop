/* 
 * Copyright (C) 2016 Franck Joncourt <franck.joncourt@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
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
