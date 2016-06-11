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
