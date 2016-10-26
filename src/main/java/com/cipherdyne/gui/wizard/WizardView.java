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

import com.cipherdyne.gui.components.IFwknopVariable;
import com.cipherdyne.gui.wizard.panels.AccessSettings;
import com.cipherdyne.gui.wizard.panels.AesSettings;
import com.cipherdyne.gui.wizard.panels.CryptoSettings;
import com.cipherdyne.gui.wizard.panels.HmacSettings;
import com.cipherdyne.gui.wizard.panels.RemoteHostSettings;
import com.cipherdyne.utils.InternationalizationHelper;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Franck Joncourt
 */
class WizardView extends JDialog {

    private final Map<EnumWizardVariable, IFwknopVariable> varMap = new HashMap<>();
    private final Map<EnumWizardButton, JButton> btnMap = new HashMap<>();
    private Map<EnumWizardPanel, JPanel> panelMap = new HashMap<>();

    private JPanel mainPanel;
    private EnumWizardPanel currentPanel = EnumWizardPanel.SELECT_CRYPTO;

    public WizardView(JFrame frame) {
        super(frame, InternationalizationHelper.getMessage("i18n.wizard.title"), true);
        this.createViews();
        this.createButtons();
        this.create();
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
    }

    private void createViews() {
        this.panelMap.put(EnumWizardPanel.SELECT_CRYPTO, new CryptoSettings(varMap, btnMap));
        this.panelMap.put(EnumWizardPanel.SETUP_AES, new AesSettings(varMap, btnMap));
        this.panelMap.put(EnumWizardPanel.SETUP_HMAC, new HmacSettings(varMap, btnMap));
        this.panelMap.put(EnumWizardPanel.SETUP_ACCESS, new AccessSettings(varMap, btnMap));
        this.panelMap.put(EnumWizardPanel.SETUP_REMOTE_HOST, new RemoteHostSettings(varMap, btnMap));
    }

    private void createButtons() {
        this.btnMap.put(EnumWizardButton.CANCEL, new JButton(InternationalizationHelper.getMessage("i18n.wizard.cancel")));
        this.btnMap.put(EnumWizardButton.BACK, new JButton(InternationalizationHelper.getMessage("i18n.wizard.back")));
        this.btnMap.put(EnumWizardButton.NEXT, new JButton(InternationalizationHelper.getMessage("i18n.wizard.next")));
        this.btnMap.put(EnumWizardButton.FINISH, new JButton(InternationalizationHelper.getMessage("i18n.wizard.finish")));
    }

    private void create() {
        // Add components to the panel
        this.setLayout(new MigLayout("insets 5, flowx, wrap 2", "[256!]0![500!]", "[][]"));
        this.mainPanel = new JPanel(new MigLayout("fill", "", ""));

        updateMainPanel(currentPanel);

        this.add(createWizardPanel());
        this.add(this.mainPanel, "grow");
        this.add(createFooterPanel(), "growx, spanx 2");

        this.pack();
    }

    public Map<EnumWizardVariable, IFwknopVariable> getContext() {
        return this.varMap;
    }

    public IFwknopVariable getVariable(EnumWizardVariable varId) {
        return this.varMap.get(varId);
    }

    public JButton getButton(EnumWizardButton btnId) {
        return this.btnMap.get(btnId);
    }

    protected JPanel createWizardPanel() {
        JPanel imagePanel = new JPanel(new MigLayout("flowy, insets 0", "", ""));
        JLabel wizardPicture = new JLabel(new ImageIcon(this.getClass().getResource("/wizard256.png")));
        imagePanel.add(wizardPicture);

        return imagePanel;
    }

    /**
     * Create a apanel that contains the cancel and next buttons
     *
     * @return the panel
     */
    private JPanel createFooterPanel() {

        JPanel btnPanel = new JPanel(new MigLayout("insets 0, fillx, flowx", "", ""));
        btnPanel.add(this.btnMap.get(EnumWizardButton.CANCEL), "width 120, center");
        btnPanel.add(this.btnMap.get(EnumWizardButton.BACK), "width 120, center");
        btnPanel.add(this.btnMap.get(EnumWizardButton.NEXT), "width 120, center");
        btnPanel.add(this.btnMap.get(EnumWizardButton.FINISH), "width 120, center");

        return btnPanel;
    }

    private void updateMainPanel(EnumWizardPanel panel) {
        boolean backButtonEnabled = true;
        boolean nextButtonEnabled = true;
        boolean finishButtonEnabled = false;

        this.currentPanel = panel;

        this.mainPanel.removeAll();
        this.mainPanel.add(this.panelMap.get(this.currentPanel), "grow");
        this.mainPanel.repaint();

        if (this.currentPanel == EnumWizardPanel.SELECT_CRYPTO) {
            backButtonEnabled = false;
        } else if (this.currentPanel == EnumWizardPanel.SETUP_REMOTE_HOST) {
            nextButtonEnabled = false;
            finishButtonEnabled = true;
        }

        this.btnMap.get(EnumWizardButton.BACK).setEnabled(backButtonEnabled);
        this.btnMap.get(EnumWizardButton.NEXT).setEnabled(nextButtonEnabled);
        this.btnMap.get(EnumWizardButton.FINISH).setEnabled(finishButtonEnabled);
    }

    public void next() {
        EnumWizardPanel nextPanel;

        switch (this.currentPanel) {
            case SELECT_CRYPTO:
                nextPanel = EnumWizardPanel.SETUP_AES;
                break;
            case SETUP_AES:
                nextPanel = EnumWizardPanel.SETUP_HMAC;
                break;
            case SETUP_HMAC:
                nextPanel = EnumWizardPanel.SETUP_ACCESS;
                break;
            case SETUP_ACCESS:
                nextPanel = EnumWizardPanel.SETUP_REMOTE_HOST;
                break;
            default:
                nextPanel = this.currentPanel;
        }

        updateMainPanel(nextPanel);
    }

    public void back() {
        EnumWizardPanel previousPanel;

        switch (this.currentPanel) {
            case SETUP_AES:
                previousPanel = EnumWizardPanel.SELECT_CRYPTO;
                break;
            case SETUP_HMAC:
                previousPanel = EnumWizardPanel.SETUP_AES;
                break;
            case SETUP_ACCESS:
                previousPanel = EnumWizardPanel.SETUP_HMAC;
                break;
            case SETUP_REMOTE_HOST:
                previousPanel = EnumWizardPanel.SETUP_ACCESS;
                break;
            default:
                previousPanel = this.currentPanel;
        }

        updateMainPanel(previousPanel);
    }
}
