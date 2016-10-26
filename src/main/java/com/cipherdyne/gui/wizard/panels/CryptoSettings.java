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
package com.cipherdyne.gui.wizard.panels;

import com.cipherdyne.gui.components.IFwknopVariable;
import com.cipherdyne.gui.components.JFwknopTextField;
import com.cipherdyne.gui.wizard.EnumWizardButton;
import com.cipherdyne.gui.wizard.EnumWizardVariable;
import com.cipherdyne.utils.InternationalizationHelper;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 * Wizard view that displays basic Fwknop variables to set up in order to quickly run and SPA packet
 *
 * @author Franck Joncourt
 */
public class CryptoSettings extends DefaultPanel {

    private JEditorPane cryptoDescription;
    final private static String AES_ENCRYPTION = "AES";
    final private static String GNUPG_ENCRYPTION = "GnuPG";

    final private Map<EnumWizardVariable, IFwknopVariable> varMap;

    /**
     * Wizard view to selct AES or GPG encryption
     */
    public CryptoSettings(Map<EnumWizardVariable, IFwknopVariable> varMap, Map<EnumWizardButton, JButton> btnMap) {
        super();

        this.varMap = varMap;

        //Create the radio buttons and add them to a group to handle autoamtic selection/unselection
        JRadioButton aesButton = new JRadioButton(AES_ENCRYPTION);
        aesButton.setActionCommand(AES_ENCRYPTION);
        aesButton.setSelected(false);

        JRadioButton gpgButton = new JRadioButton(GNUPG_ENCRYPTION);
        gpgButton.setActionCommand(GNUPG_ENCRYPTION);
        gpgButton.setSelected(true);
        this.varMap.put(EnumWizardVariable.ENCRYPTION_MODE, new JFwknopTextField(GNUPG_ENCRYPTION));

        ButtonGroup group = new ButtonGroup();
        group.add(aesButton);
        group.add(gpgButton);

        // Create a transparent text area rather than a label since the description can be long and may need to be wrapped on several lines
        this.cryptoDescription = new JEditorPane();
        this.cryptoDescription.setEditable(false);
        this.cryptoDescription.setOpaque(false);
        this.cryptoDescription.setBackground(new Color(10, 10, 10, 0));
        this.cryptoDescription.setContentType("text/html");
        this.cryptoDescription.setText(InternationalizationHelper.getMessage("i18n.wizard.encyption.gnupg.description"));

        // Add object to the newly created panel
        this.add(new JLabel(InternationalizationHelper.getMessage("i18n.wizard.select.encryption.mode")), "growx");
        this.add(aesButton, "growx");
        this.add(gpgButton, "growx");
        this.add(this.cryptoDescription, "growx");

        // Set up action listeners to display encryption description when the user select the encryption mode to use
        aesButton.addActionListener((ActionEvent e) -> {
            this.cryptoDescription.setText(InternationalizationHelper.getMessage("i18n.wizard.encyption.aes.description"));
            this.varMap.get(EnumWizardVariable.ENCRYPTION_MODE).setText(AES_ENCRYPTION);

        });

        gpgButton.addActionListener((ActionEvent e) -> {
            this.cryptoDescription.setText(InternationalizationHelper.getMessage("i18n.wizard.encyption.gnupg.description"));
            this.varMap.get(EnumWizardVariable.ENCRYPTION_MODE).setText(GNUPG_ENCRYPTION);

        });

    }
}
