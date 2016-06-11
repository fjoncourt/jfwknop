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
package com.cipherdyne.gui.gpg;

import com.cipherdyne.utils.InternationalizationHelper;
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
    
    // Button used to create a new key (public and private)
    final private JButton btnCreate;

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
        
        btnExport = new JButton(InternationalizationHelper.getMessage("i18n.key.export"));
        btnImport = new JButton(InternationalizationHelper.getMessage("i18n.key.import"));
        btnRemove = new JButton(InternationalizationHelper.getMessage("i18n.key.remove"));
        btnCreate = new JButton(InternationalizationHelper.getMessage("i18n.key.create"));
        btnSelect = new JButton(InternationalizationHelper.getMessage("i18n.key.select"));
        btnCancel = new JButton(InternationalizationHelper.getMessage("i18n.key.cancel"));

        // Add components to the ui
        JScrollPane scrollPane = new JScrollPane(keyTable);
        this.getContentPane().add(scrollPane, "span 2, growx, wrap");
        
        JPanel operationPanel = new JPanel(new MigLayout("fill, flowx, gap 0, insets 1", "[][][]", ""));
        operationPanel.setBorder(new TitledBorder(null, "Operations", TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD, 10)));
        operationPanel.add(btnExport, "growx");
        operationPanel.add(btnImport, "growx");
        operationPanel.add(btnRemove, "growx");
        operationPanel.add(btnCreate, "growx");
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
     * @return the create button
     */
    public JButton getBtnCreate() {
        return this.btnCreate;
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
