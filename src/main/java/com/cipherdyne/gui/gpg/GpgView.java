/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui.gpg;

import com.cipherdyne.jfwknop.FwknopFactory;
import com.cipherdyne.jfwknop.InternationalizationHelper;
import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
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

    // Button used to export the selected key
    final private JButton btnExport;

    // Button used to import a key
    final private JButton btnImport;

    // Button used to remove a key
    final private JButton btnRemove;

    // Table that displays all keys from the keyring
    private JTable keyTable = null;

    public GpgView(JFrame frame, String gpgHomeDirectory) {
        super(frame, InternationalizationHelper.getMessage("i18n.key.management"), true);

        this.setLayout(new MigLayout("inset 5, gap 0, flowx", "[225!][225!]", ""));

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
        btnExport = new JButton(InternationalizationHelper.getMessage("i18n.key.export"));
        btnImport = new JButton(InternationalizationHelper.getMessage("i18n.key.import"));
        btnRemove = new JButton(InternationalizationHelper.getMessage("i18n.key.remove"));
        btnCancel = new JButton(InternationalizationHelper.getMessage("i18n.key.cancel"));

        // Add components to the ui
        JScrollPane scrollPane = new JScrollPane(keyTable);
        this.getContentPane().add(scrollPane, "span 2, growx, wrap");
        
        JPanel operationPanel = new JPanel(new MigLayout("fill, flowx, gap 0, insets 1", "[130!][130!][130!]", ""));
        operationPanel.setBorder(new TitledBorder(null, "Operations", TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD, 10)));
        operationPanel.add(btnExport, "growx");
        operationPanel.add(btnImport, "growx");
        operationPanel.add(btnRemove, "growx");
        this.add(operationPanel, "span 2, growx, wrap");
        
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
     * @return the cancel button
     */
    public JButton getBtnCancel() {
        return this.btnCancel;
    }

    /**
     * @return the export button
     */
    public JButton getBtnExport() {
        return this.btnExport;
    }

    /**
     * @return the import button
     */
    public JButton getBtnImport() {
        return this.btnImport;
    }

    /**
     * @return the remove button
     */
    public JButton getBtnRemove() {
        return this.btnRemove;
    }

    /**
     * @return the GPG key table
     */
    public JTable getKeyTable() {
        return this.keyTable;
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
