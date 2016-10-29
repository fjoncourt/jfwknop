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
import com.cipherdyne.gui.wizard.views.CryptoView;
import com.cipherdyne.gui.wizard.views.IWizardView;
import com.cipherdyne.utils.InternationalizationHelper;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Franck Joncourt
 */
class WizardMainView extends JFrame {

    // List of IFwknopVariable that are available in all views
    private final Map<EnumWizardVariable, IFwknopVariable> varMap = new HashMap<>();

    // List of JBUutton that are available in all views
    private final Map<EnumWizardButton, JButton> btnMap = new HashMap<>();

    // List of wizard views available. They are dislayed one at a time
    final private Map<EnumWizardView, IWizardView> viewMap = new HashMap<>();

    // Main panel which is refreshed according to the wizard step (CRYPTO, HMAC ...)
    private JPanel mainView;

    // This list contains all the view that are displayed to the user according to his choices.
    // It is used to provide a way to navigate to previous views through the footer buttons
    private final LinkedList<EnumWizardView> viewHistory = new LinkedList<>();

    /**
     * WizardMainView constructor
     *
     * @param frame parent frame
     */
    public WizardMainView(JFrame frame) {
        super(InternationalizationHelper.getMessage("i18n.wizard.title"));
        this.createViews();
        this.createButtons();
        this.createMainView();
        this.setLocationRelativeTo(frame);
        this.setResizable(false);
    }

    /**
     * Create the list of views that will be displayed in the main view of the wizard. They should
     * be built at WizardMainView initialization
     */
    private void createViews() {
        for (EnumWizardView v : EnumWizardView.values()) {
            this.viewMap.put(v, v.getView());
            this.viewMap.get(v).initialize(varMap, btnMap);
        }
    }

    /**
     * Create buttons displayed in the footer of the wizard view (back, finish, next ...)
     */
    private void createButtons() {
        this.btnMap.put(EnumWizardButton.CANCEL, new JButton(EnumWizardButton.CANCEL.getDescription()));
        this.btnMap.put(EnumWizardButton.BACK, new JButton(EnumWizardButton.BACK.getDescription()));
        this.btnMap.put(EnumWizardButton.NEXT, new JButton(EnumWizardButton.NEXT.getDescription()));
        this.btnMap.put(EnumWizardButton.FINISH, new JButton(EnumWizardButton.FINISH.getDescription()));
    }

    private void createMainView() {

        // Set layout
        this.setLayout(new MigLayout("insets 5, flowx, wrap 2", "[256!]0![500!]", "[][]"));
        this.mainView = new JPanel(new MigLayout("fill", "", ""));

        // Create the main panel
        this.viewHistory.addLast(EnumWizardView.SELECT_CRYPTO);
        updateMainView(EnumWizardView.SELECT_CRYPTO);

        // Add components to the panel
        this.add(createWizardPanel());
        this.add(this.mainView, "grow");
        this.add(createFooterPanel(), "growx, spanx 2");

        // Pack everyting just in case
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

    private void updateMainView(EnumWizardView nextPanel) {
        boolean backButtonEnabled = true;
        boolean nextButtonEnabled = true;
        boolean finishButtonEnabled = false;

        this.mainView.removeAll();
        this.mainView.add((JPanel) this.viewMap.get(nextPanel), "grow");
        this.mainView.repaint();

        if (nextPanel == EnumWizardView.SELECT_CRYPTO) {
            backButtonEnabled = false;
        } else if (nextPanel == EnumWizardView.SETUP_REMOTE_HOST) {
            nextButtonEnabled = false;
            finishButtonEnabled = true;
        }

        this.btnMap.get(EnumWizardButton.BACK).setEnabled(backButtonEnabled);
        this.btnMap.get(EnumWizardButton.NEXT).setEnabled(nextButtonEnabled);
        this.btnMap.get(EnumWizardButton.FINISH).setEnabled(finishButtonEnabled);
    }

    public void next() {
        // Add the current view to history
        EnumWizardView nextPanel = this.viewMap.get(this.viewHistory.getLast()).getNextPanel();
        this.viewHistory.addLast(nextPanel);

        // Update view with the lastest one
        updateMainView(this.viewHistory.getLast());
    }

    public void back() {
        // Remove the current view from history
        this.viewHistory.removeLast();

        // Update view with the lastest one
        updateMainView(this.viewHistory.getLast());
    }

    /**
     * @return true if the GPG encryption mod is selected, false if AES encryption mode is selected
     */
    public boolean isGpgSelected() {
        return ((CryptoView) (this.viewMap.get(EnumWizardView.SELECT_CRYPTO))).isGpgSelected();
    }
}
