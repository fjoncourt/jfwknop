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

import com.cipherdyne.gui.components.JFwknopTextField;
import com.cipherdyne.utils.InternationalizationHelper;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * Wizard view that displays basic Fwknop variables to set up in order to quickly run and SPA packet
 *
 * @author Franck Joncourt <franck.joncourt@gmail.com>
 */
public class WizardView extends DefaultDialog<EnumWizardVariable, EnumWizardButton> {

    /**
     * Wizard view
     *
     * @param frame parent frame to inherit from
     */
    public WizardView(JFrame frame) {
        super(frame, InternationalizationHelper.getMessage("i18n.wizard.title"));

        // Create variable components and add them to the map
        this.varMap.put(EnumWizardVariable.KEY, new JFwknopTextField(""));
        this.varMap.put(EnumWizardVariable.HMAC, new JFwknopTextField(""));
        this.varMap.put(EnumWizardVariable.REMOTE_HOST, new JFwknopTextField(""));
        this.varMap.put(EnumWizardVariable.ACCESS, new JFwknopTextField("tcp/22"));

        // Create button components and add them to the map
        this.btnMap.put(EnumWizardButton.CANCEL, new JButton(InternationalizationHelper.getMessage("i18n.wizard.cancel")));
        this.btnMap.put(EnumWizardButton.CREATE, new JButton(InternationalizationHelper.getMessage("i18n.wizard.create")));

        // Create the wizard picture to be added on the view
        JPanel imagePanel = new JPanel();
        JLabel wizardLabel = new JLabel();
        wizardLabel.setIcon(new ImageIcon(this.getClass().getResource("/wizard256.png")));
        imagePanel.add(wizardLabel);

        // Add components to the panel
        this.setLayout(new MigLayout("fill, insets 10, flowx", "[256]0![500!]", "[500]"));

        JPanel mainPanel = new JPanel(new MigLayout("flowy", "", ""));
        mainPanel.add(createVarPanel(), "growx");
        mainPanel.add(createButtonPanel(), "growx");

        this.add(imagePanel);
        this.add(mainPanel);

        this.pack();
        
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
    }

    /**
     * Create an item for the wizard setup.
     *
     * An item is composed of a long description and a variable to be filled in. Both of them are
     * added to a single panel.
     *
     * @param longDescription description to display to the user to help him fill in the variable
     * value
     * @param var EnumVariable variable that contains the user value
     * @return the panel that contains the description and the variable
     */
    private JPanel createItem(String longDescription, EnumWizardVariable var) {
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 1, flowy, fillx", "", ""));

        // Create a transparent text area rather than a label since the description can long and may need to be wrapped on several lines
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.setBackground(new Color(10, 10, 10, 0));
        editorPane.setContentType("text/html");
        editorPane.setText(longDescription);

        // Add object to the newly created panel
        panel.add(editorPane, "growx");
        panel.add((Component) this.varMap.get(var), "growx");

        return panel;
    }

    /**
     * Create the panel to display the user settings (rijndael key, remote spa server and access
     * field)
     *
     * @return the panel
     */
    private JPanel createVarPanel() {
        JPanel varPanel = new JPanel(new MigLayout("insets 0, gapy 10, flowy", "", ""));

        varPanel.add(createItem(InternationalizationHelper.getMessage("i18n.wizard.key.description"), EnumWizardVariable.KEY), "growx");
        varPanel.add(createItem(InternationalizationHelper.getMessage("i18n.wizard.hmac.description"), EnumWizardVariable.HMAC), "growx");
        varPanel.add(createItem(InternationalizationHelper.getMessage("i18n.wizard.remotehost.description"), EnumWizardVariable.REMOTE_HOST), "growx");
        varPanel.add(createItem(InternationalizationHelper.getMessage("i18n.wizard.access.description"), EnumWizardVariable.ACCESS), "growx");

        return varPanel;
    }

    /**
     * Create a apanel that contains the create and cancel button
     *
     * @return the panel
     */
    private JPanel createButtonPanel() {
        JPanel btnPanel = new JPanel(new MigLayout("insets 0, fillx, flowx", "", ""));
        btnPanel.add(this.btnMap.get(EnumWizardButton.CANCEL), "align left");
        btnPanel.add(this.btnMap.get(EnumWizardButton.CREATE), "align right");
        return btnPanel;
    }
}
