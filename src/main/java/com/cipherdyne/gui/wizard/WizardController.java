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

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.gui.gpg.GpgController;
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.MainWindowController;
import com.cipherdyne.model.RcFileModel;
import com.cipherdyne.utils.InternationalizationHelper;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import org.apache.commons.lang3.StringUtils;

/**
 * Controller used to manage variables from the wizard view
 */
public class WizardController {

    // View used to display the wizard
    final private WizardMainView view;

    // Parent window we come from
    final private MainWindowView parentWindow;

    // Parent controller we come from
    final private MainWindowController parentController;

    /**
     * WizardController constructor
     *
     * @param parentController parent controller where can get KeyModel, RcFileModel ...
     * @param frame parent frame we come from
     */
    public WizardController(MainWindowController parentController, MainWindowView frame) {
        this.parentWindow = frame;
        this.parentController = parentController;
        this.view = new WizardMainView(this.parentWindow);

        populateFooterBtn();
        populateViewsBtn();
        this.view.setVisible(true);
    }

    /**
     * Set up action listener for buttons in the footer wizard view
     */
    private void populateFooterBtn() {

        // When the user cancel the easy setup, we close the wizard window and go back to the main JFwknop view
        this.view.getButton(EnumWizardButton.CANCEL).addActionListener(e -> {
            this.view.dispose();
        });

        // When the user create the configuration, we fetch his settings and update the JFwknop model accordingly
        this.view.getButton(EnumWizardButton.FINISH).addActionListener(e -> {

            // Get rcFile model used by JFwknop, update it according to the context and refresh all listeners
            RcFileModel rcFileModel = this.parentController.getRcFileModel();
            rcFileModel.setContext(createContext());
            rcFileModel.updateListeners();

            // Close this window
            this.view.dispose();
        });

        // Move to the next wizard screen
        this.view.getButton(EnumWizardButton.NEXT).addActionListener(e -> {
            this.view.next();
        });

        // Move to the previous wizard screen
        this.view.getButton(EnumWizardButton.BACK).addActionListener(e -> {
            this.view.back();
        });
    }

    /**
     * @return a context that contains JFwknop settings according to the user inputs in the wizard
     */
    private Map<EnumFwknopRcKey, String> createContext() {

        // Create an empty context
        Map<EnumFwknopRcKey, String> context = new HashMap<>();

        // Configure default HMAC values
        String useHmac = "N";
        String hmacKey = this.view.getVariable(EnumWizardVariable.HMAC_KEY).getText();
        if (!StringUtils.EMPTY.equals(hmacKey)) {
            useHmac = "Y";
        }
        context.put(EnumFwknopRcKey.USE_HMAC, useHmac);
        context.put(EnumFwknopRcKey.HMAC_KEY, hmacKey);

        // Configure GPG encryption settings
        if (this.view.isGpgSelected()) {
            context.put(EnumFwknopRcKey.USE_GPG, "Y");
            context.put(EnumFwknopRcKey.GPG_HOMEDIR, this.view.getVariable(EnumWizardVariable.GPG_HOME_DIRECTORY).getText());
            context.put(EnumFwknopRcKey.GPG_RECIPIENT, this.view.getVariable(EnumWizardVariable.GPG_RECIPIENT_ID).getText());
            context.put(EnumFwknopRcKey.GPG_SIGNER, this.view.getVariable(EnumWizardVariable.GPG_SIGNER_ID).getText());
            context.put(EnumFwknopRcKey.GPG_SIGNING_PW, this.view.getVariable(EnumWizardVariable.GPG_SIGNER_PASSWORD).getText());

        } // Configure AES encryption settings
        else {
            context.put(EnumFwknopRcKey.USE_GPG, "N");
            context.put(EnumFwknopRcKey.KEY, this.view.getVariable(EnumWizardVariable.AES_KEY).getText());
        }

        // Configure other settings
        context.put(EnumFwknopRcKey.SPA_SERVER, this.view.getVariable(EnumWizardVariable.REMOTE_HOST).getText());
        context.put(EnumFwknopRcKey.ACCESS, this.view.getVariable(EnumWizardVariable.ACCESS).getText());
        context.put(EnumFwknopRcKey.ALLOW_IP, "resolve");

        return context;
    }

    /**
     * Set up action listeners for buttons in the main panel of the wizard view
     */
    private void populateViewsBtn() {
        // Generate a random rijndael key
        this.view.getButton(EnumWizardButton.GENERATE_AES_KEY).addActionListener(e -> {
            this.view.getVariable(EnumWizardVariable.AES_KEY).setText(parentController.getKeyModel().getRandomRijndaelKey());
        });

        // Generate a random HMAC key
        this.view.getButton(EnumWizardButton.GENERATE_HMAC_KEY).addActionListener(e -> {
            this.view.getVariable(EnumWizardVariable.HMAC_KEY).setText(parentController.getKeyModel().getRandomHmacKey());
        });

        // Browse for a GPG home directory
        this.view.getButton(EnumWizardButton.BROWSE_FOR_GPG_HOMEDIR).addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileHidingEnabled(false);
            fileChooser.setDialogTitle(InternationalizationHelper.getMessage("i18n.browse"));
            final int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                this.view.getVariable(EnumWizardVariable.GPG_HOME_DIRECTORY).setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Browse for a GPG signer ID
        this.view.getButton(EnumWizardButton.BROWSE_FOR_GPG_SIGNER_ID).addActionListener(e -> {
            javax.swing.SwingUtilities.invokeLater(() -> new GpgController(this.view,
                this.view.getVariable(EnumWizardVariable.GPG_SIGNER_ID),
                this.view.getVariable(EnumWizardVariable.GPG_HOME_DIRECTORY).getText()));
        });

        // Browse for a GPG recipient ID
        this.view.getButton(EnumWizardButton.BROWSE_FOR_GPG_RECIPIENT_ID).addActionListener(e -> {
            javax.swing.SwingUtilities.invokeLater(() -> new GpgController(this.view,
                this.view.getVariable(EnumWizardVariable.GPG_RECIPIENT_ID),
                this.view.getVariable(EnumWizardVariable.GPG_HOME_DIRECTORY).getText()));
        });
    }
}
