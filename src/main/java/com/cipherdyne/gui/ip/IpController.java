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
package com.cipherdyne.gui.ip;

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import java.awt.event.ActionEvent;

/**
 * Controller use to handle behaviour of the IP view
 */
public class IpController {

    // View used to display network interfaces
    final private IpView view;

    // Parent window we come from
    final private MainWindowView parentWindow;

    // Fwknop key to update when the interface is selected
    final EnumFwknopRcKey fwknopKey;

    /**
     * Create an IP controller that handled the IP view
     *
     * @param frame parent window
     * @param fwknopKey SPA client IP address
     */
    public IpController(MainWindowView frame, EnumFwknopRcKey fwknopKey) {
        this.parentWindow = frame;
        this.fwknopKey = fwknopKey;

        // Build the IP view
        this.view = new IpView(this.parentWindow);

        // Configure button action listeners
        updateBtnBehaviour();

        // Display the view
        this.view.setVisible(true);
    }

    /**
     * Update the behaviour of all buttons displayed in the IP view
     */
    private void updateBtnBehaviour() {

        //  Select button. Update the ALLOW_IP field in the mainwondow with the currently selected IP address and close the window
        this.view.getBtnSelect().addActionListener((ActionEvent e) -> {
            String selectedIp = this.view.getSelectIp();
            this.parentWindow.getVariables().get(this.fwknopKey).setText(selectedIp);
            this.view.dispose();
        });

        // Cancel button - Close the window
        this.view.getBtnCancel().addActionListener((ActionEvent e) -> {
            this.view.dispose();
        });
    }
}
