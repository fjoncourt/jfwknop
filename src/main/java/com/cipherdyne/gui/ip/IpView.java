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
package com.cipherdyne.gui.ip;

import com.cipherdyne.utils.InternationalizationHelper;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;

/**
 * Create a UI to display network interfaces in a modal window.
 */
public class IpView extends JDialog {

    // Cancel button to close the window without selected any IP addresses
    private final JButton btnCancel;
    
    // Select button to update the main window with the IP addresses currently highlighted
    private final JButton btnSelect;
    
    // Table that displays all network interfaces
    private JTable intfTable = null;

    public IpView(JFrame frame) {
        super(frame, InternationalizationHelper.getMessage("i18n.ip.management"), true);

        this.setLayout(new MigLayout("inset 5, gap 0, flowx", "[225!][225!]", ""));

        // Build the IP table
        intfTable = new JTable(new IpTableModel());
        intfTable.setFillsViewportHeight(true);
        intfTable.setAutoCreateRowSorter(true);
        intfTable.getColumnModel().getColumn(IpTableModel.NETWORK_IFACE_COL_ID).setMaxWidth(80);
        intfTable.getColumnModel().getColumn(IpTableModel.IP_ADDRESS_COL_ID).setMinWidth(140);

        // Build the action buttons
        btnCancel = new JButton(InternationalizationHelper.getMessage("i18n.key.cancel"));
        btnSelect = new JButton(InternationalizationHelper.getMessage("i18n.key.select"));

        // Add components to the ui
        JScrollPane scrollPane = new JScrollPane(intfTable);
        this.getContentPane().add(scrollPane, "span 2, growx, wrap");

        this.add(btnCancel, "growx");
        this.add(btnSelect, "growx");

        this.pack();

        this.setLocationRelativeTo(frame);
    }
    
    /**
     * @return the cancel button
     */
    public JButton getBtnCancel() {
        return this.btnCancel;
    }

    /**
     * @return the select button
     */
    public JButton getBtnSelect() {
        return this.btnSelect;
    }    
    
    /**
     * @return the IP address currently selected
     */
    public String getSelectIp() {
        String selectedIpAddress = StringUtils.EMPTY;

        // Ensure at least one row is selected
        if (intfTable.getSelectedRow() != -1) {
            selectedIpAddress = (String) (intfTable.getValueAt(intfTable.getSelectedRow(), IpTableModel.IP_ADDRESS_COL_ID));
        }
        return selectedIpAddress;
    }
}
