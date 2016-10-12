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
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.MainWindowController;
import com.cipherdyne.model.RcFileModel;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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

        // Generate a random rijndael key
        this.view.getButton(EnumWizardButton.GENERATE_RIJNDAEL_KEY).addActionListener(e -> {
            this.view.varMap.get(EnumWizardVariable.KEY).setText(parentController.getKeyModel().getRandomRijndaelKey());
        });        
        
        // Generate a random HMAC key
        this.view.getButton(EnumWizardButton.GENERATE_HMAC_KEY).addActionListener(e -> {
            this.view.varMap.get(EnumWizardVariable.HMAC).setText(parentController.getKeyModel().getRandomHmacKey());
        });        
        
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

            // Handle default HMAC values
            String useHmac = "N";
            String hmacKey = this.view.getVariable(EnumWizardVariable.HMAC).getText();

            if (!StringUtils.EMPTY.equals(hmacKey)) {
                useHmac = "Y";
            }

            // Add new ones from user input
            context.put(EnumFwknopRcKey.KEY, this.view.getVariable(EnumWizardVariable.KEY).getText());            
            context.put(EnumFwknopRcKey.USE_HMAC, useHmac);
            context.put(EnumFwknopRcKey.HMAC_KEY, hmacKey);
            context.put(EnumFwknopRcKey.SPA_SERVER, this.view.getVariable(EnumWizardVariable.REMOTE_HOST).getText());
            context.put(EnumFwknopRcKey.ACCESS, this.view.getVariable(EnumWizardVariable.ACCESS).getText());
            context.put(EnumFwknopRcKey.ALLOW_IP, "resolve");

            // Update context and refresh all listeners
            rcFileModel.setContext(context);
            rcFileModel.updateListeners();

            // Close this window
            this.view.dispose();
        });
    }
}
