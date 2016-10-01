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
package com.cipherdyne.gui.wizard;

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.jfwknop.EnumFwknopConfigKey;
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.MainWindowController;
import com.cipherdyne.model.FwknopClientModel;
import com.cipherdyne.model.RcFileModel;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Controller used to manage variables from the wizard view
 */
public class WizardController {

    // Logger
    static final Logger LOGGER = LogManager.getLogger(WizardController.class.getName());

    // View used to display the wizard
    final private WizardView view;

    // Parent window we come from
    final private MainWindowView parentWindow;

    final private MainWindowController parentController;

    public WizardController(MainWindowController parentController, MainWindowView frame) {
        this.parentWindow = frame;
        this.parentController = parentController;
        this.view = new WizardView(this.parentWindow);

        populateBtn();

        this.view.setVisible(true);
    }

    private void populateBtn() {

        // When the user cancel the easy setup, we close the wizard window and go back to the main JFwknop view
        this.view.getButton(EnumWizardButton.CANCEL).addActionListener(e -> {
            this.view.dispose();
        });

        // When the user create the configuration, we fetch his settings and update the JFwknop model accordingly
        this.view.getButton(EnumWizardButton.CREATE).addActionListener(e -> {

            // Get rcFile model used by JFwknop
            RcFileModel rcFileModel = this.parentController.getRcFileModel();

            // Create an empty context
            Map<EnumFwknopRcKey, String> context = new HashMap<EnumFwknopRcKey, String>();

            // Add new ones from user input
            context.put(EnumFwknopRcKey.KEY, this.view.getVariable(EnumWizardVariable.KEY).getText());
            context.put(EnumFwknopRcKey.SPA_SERVER, this.view.getVariable(EnumWizardVariable.REMOTE_HOST).getText());
            context.put(EnumFwknopRcKey.ACCESS, this.view.getVariable(EnumWizardVariable.ACCESS).getText());

            // Updazte context and refresh all listeners
            rcFileModel.setContext(context);
            rcFileModel.updateListeners();

            // Close this window
            this.view.dispose();
        });
    }
}
