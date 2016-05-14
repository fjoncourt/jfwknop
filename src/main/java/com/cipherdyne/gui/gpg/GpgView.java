/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui.gpg;

import com.cipherdyne.jfwknop.InternationalizationHelper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.openpgp.PGPException;

/**
 * Create a UI to display GPG keys along with their user ids in a modal window.
 */
public class GpgView extends JDialog {

    // Button used to select a key
    final private JButton btnSelect;

    // Button used to cancel the action and go back to the main window
    final private JButton btnCancel;

    // Table that displays all keys from the keyring
    private JTable keyTable = null;

    public GpgView(JFrame frame, String gpgHomeDirectory) {
        super(frame, InternationalizationHelper.getMessage("i18n.key.management"), true);

        this.setLayout(new MigLayout("inset 5, gap 0, flowx", "[240!][240!]", ""));

        // Build the key table and ensure the GPG home directory is parsable
        try {

            keyTable = new JTable(new GpgTableModel(gpgHomeDirectory));
        } catch (IOException | PGPException ex) {
            JOptionPane.showMessageDialog(frame,
                InternationalizationHelper.getMessage("i18n.bad.gpg.homedir") + ":\n" + gpgHomeDirectory,
                InternationalizationHelper.getMessage("i18n.gpg.error"),
                JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(GpgView.class.getName()).log(Level.SEVERE, null, ex);
        }
        keyTable.setFillsViewportHeight(true);
        keyTable.setAutoCreateRowSorter(true);
        keyTable.getColumnModel().getColumn(GpgTableModel.KEY_ID_COL).setMaxWidth(140);
        keyTable.getColumnModel().getColumn(GpgTableModel.KEY_ID_COL).setMinWidth(140);

        // Build the action buttons
        btnSelect = new JButton(InternationalizationHelper.getMessage("i18n.key.select"));
        btnCancel = new JButton(InternationalizationHelper.getMessage("i18n.key.cancel"));

        // Add components to the ui
        JScrollPane scrollPane = new JScrollPane(keyTable);
        this.getContentPane().add(scrollPane, "span 2, growx, wrap");
        this.add(btnSelect, "growx");
        this.add(btnCancel, "growx");

        this.pack();
    }

    /**
     * @return the select button
     */
    public JButton getBtnSelect() {
        return this.btnSelect;
    }

    /**
     * @return the cacncel button
     */
    public JButton getBtnCancel() {
        return this.btnCancel;
    }

    /**
     * @return the key id currently selected
     */
    public String getSelectKeyId() {
        String selectedKeyId = StringUtils.EMPTY;
        
        // Ensure at least one row is selected
        if (keyTable.getSelectedRow() != -1) {
            selectedKeyId = (String) (keyTable.getValueAt(keyTable.getSelectedRow(), GpgTableModel.KEY_ID_COL));
        }
        return selectedKeyId;
    }

}
