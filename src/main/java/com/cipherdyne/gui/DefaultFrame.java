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
package com.cipherdyne.gui;

import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.gui.components.IFwknopVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

/**
 *
 * @author Franck Joncourt
 */
class DefaultFrame extends JFrame {

    protected final Map<EnumFwknopRcKey, IFwknopVariable> varMap;
    protected final List<JMenuItem> varRecentRcFiles;
    protected final Map<EnumButton, JButton> btnMap;
    protected final Map<EnumMenuItem, JMenuItem> menuItemMap;

    public DefaultFrame(String title) {
        super(title);
        this.varMap = new HashMap<>();
        this.varRecentRcFiles = new ArrayList<>();
        this.btnMap = new HashMap<>();
        this.menuItemMap = new HashMap<>();
    }
}
