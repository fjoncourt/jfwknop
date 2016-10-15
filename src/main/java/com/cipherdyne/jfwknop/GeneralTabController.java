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
package com.cipherdyne.jfwknop;

import com.cipherdyne.gui.EnumButton;
import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.gui.ip.IpController;
import java.awt.event.ActionEvent;

/**
 *
 * @author Franck Joncourt
 */
public class GeneralTabController extends AbstractController {

    public GeneralTabController(MainWindowView parentView, MainWindowController parentController) {
        super(parentView, parentController);
    }

    /**
     * Set up action listeners for buttons
     */
    @Override
    public void initialize() {

        // Add action listener to browse for an IP in order to configure the ALLOW_IP field
        this.parentView.getButton(EnumButton.GENERAL_BROWSE_FOR_IP).addActionListener((ActionEvent e) -> {
            javax.swing.SwingUtilities.invokeLater(() -> new IpController(this.parentView,
                EnumFwknopRcKey.ALLOW_IP));
        });
    }
}
