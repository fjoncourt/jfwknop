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
import javax.swing.JCheckBox;

/**
 * JFwknop check box implementation
 *
 * @author Franck Joncourt <franck.joncourt@gmail.com>
 */
public class JFwknopCheckBox extends JCheckBox implements IFwknopVariable {

    // Set to true if default behaviour is enable
    private final boolean defaultEnabled;

    /**
     * JFwknopCheckBox constructor
     *
     * @param defaultEnabled Set to true if default behaviour is enable
     */
    public JFwknopCheckBox(boolean defaultEnabled) {
        super();
        this.defaultEnabled = defaultEnabled;
    }

    @Override
    public void setDefaultValue() {
        this.setEnabled(this.defaultEnabled);
    }

    @Override
    public boolean isDefault() {
        boolean isDefault;

        if (this.defaultEnabled ^ this.isEnabled()) {
            isDefault = false;
        } else {
            isDefault = true;
        }

        return isDefault;
    }
}
