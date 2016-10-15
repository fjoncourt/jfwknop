/* 
 * JFwknop is developed primarily by the people listed in the file 'AUTHORS'.
 * Copyright (C) 2016 JFwknop developers and contributors.
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
package com.cipherdyne.gui.wizard;

import com.cipherdyne.jfwknop.IFwknopVariable;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Franck Joncourt
 */
class DefaultDialog<A,B> extends JDialog {

    protected final Map<A, IFwknopVariable> varMap = new HashMap<>();
    protected final Map<B, JButton> btnMap = new HashMap<>();

    public DefaultDialog(JFrame frame, String title) {
        super(frame, title, true);
    }

    public IFwknopVariable getVariable(A varId) {
        return this.varMap.get(varId);
    }

    public JButton getButton(B btnId) {
        return this.btnMap.get(btnId);
    }
}
