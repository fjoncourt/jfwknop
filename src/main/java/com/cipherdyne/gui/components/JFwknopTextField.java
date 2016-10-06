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
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (((JFwknopTextField) e.getSource()).getText().equals(StringUtils.EMPTY)) {
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
