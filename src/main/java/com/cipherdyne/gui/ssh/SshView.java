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
package com.cipherdyne.gui.ssh;

import com.cipherdyne.jfwknop.IFwknopVariable;
import com.cipherdyne.utils.InternationalizationHelper;
import com.cipherdyne.gui.components.JFwknopLabel;
import com.cipherdyne.gui.components.JFwknopTextField;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;

/**
 * Create a UI to display SSH settings to export a file to a remote server.
 *
 * @author franck
 */
public class SshView extends JDialog {

    private JTable sshFileTable = null;

    // SSH settings map that contains username, password as IFwknopVariable
    private final Map<EnumSshSettings, IFwknopVariable> settingsMap = new HashMap<>();

    // Browse button to look for the file to send
    private final JButton btnAddFile;

    // Browse button to look for the file to send
    private final JButton btnRemoveFile;

    // Button used to perform the export action
    private final JButton btnExport;

    /**
     * Create a SSH view that provide SSH settings used to connect to a remote SSH server and copy
     * local file to it
     *
     * @param frame parent frame
     */
    public SshView(JFrame frame) {
        super(frame, InternationalizationHelper.getMessage("i18n.ssh.export.title"), true);

        // Create file table
        sshFileTable = new JTable(new SshFileTableModel());
        sshFileTable.setFillsViewportHeight(true);
        sshFileTable.setAutoCreateRowSorter(true);
        sshFileTable.getColumnModel().getColumn(SshFileTableModel.FILENAME_COL_ID).setMinWidth(300);

        // Create components
        this.settingsMap.put(EnumSshSettings.USERNAME, new JFwknopTextField("<username>"));
        this.settingsMap.put(EnumSshSettings.PASSWORD, new JFwknopTextField("<password>"));
        this.settingsMap.put(EnumSshSettings.REMOTEHOST, new JFwknopTextField("<remote host>"));
        this.settingsMap.put(EnumSshSettings.REMOTEPORT, new JFwknopTextField("22"));

        // Create buttons
        this.btnAddFile = new JButton(new ImageIcon(this.getClass().getResource("/plus16.png")));
        this.btnRemoveFile = new JButton(new ImageIcon(this.getClass().getResource("/remove16.png")));
        this.btnExport = new JButton(InternationalizationHelper.getMessage("i18n.export.file"));

        // Add components to the panel
        this.setLayout(new MigLayout("inset 20, gap 0, flowx", "[200!]0![300!]0![]", ""));

        JPanel btnPanel = new JPanel(new MigLayout("inset 0, gap 0, flowx", "", ""));
        btnPanel.add(btnAddFile);
        btnPanel.add(btnRemoveFile);
        this.add(btnPanel, "wrap");
        
        this.add(new JScrollPane(sshFileTable), "growx, span 3, wrap");

        this.add(new JLabel(" "), "growx, span 3, wrap");

        this.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.ssh.user")), "growx");
        this.add((Component) this.settingsMap.get(EnumSshSettings.USERNAME), "growx, wrap");
        this.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.ssh.password")), "growx");
        this.add((Component) this.settingsMap.get(EnumSshSettings.PASSWORD), "growx, wrap");
        this.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.ssh.remotehostname")), "growx");
        this.add((Component) this.settingsMap.get(EnumSshSettings.REMOTEHOST), "growx, wrap");
        this.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.ssh.remoteport")), "growx");
        this.add((Component) this.settingsMap.get(EnumSshSettings.REMOTEPORT), "growx, wrap");

        this.add(this.btnExport, "gaptop 20, growx, span 3");

        this.pack();
        
        this.setLocationRelativeTo(frame);        
    }

    /**
     * @return the SSH settings
     */
    public Map<EnumSshSettings, IFwknopVariable> getSettings() {
        return this.settingsMap;
    }

    /**
     * @return the export button used to execute the SSH command to export the local file
     */
    public JButton getBtnExport() {
        return this.btnExport;
    }

    /**
     * @return the button that adds a file to the file table
     */
    public JButton getBtnAddFile() {
        return this.btnAddFile;
    }

    /**
     * @return the button that remove a file from the file table
     */
    public JButton getBtnRemoveFile() {
        return this.btnRemoveFile;
    }

    /**
     * @return the tazble that contains file to export to remote host
     */
    public JTable getFileTable() {
        return this.sshFileTable;
    }
    /**
     * Enum used to access all available variables in the view
     */
    public enum EnumSshSettings {
        USERNAME, PASSWORD, REMOTEHOST, REMOTEPORT, FILEPATH1, FILEPATH2;
    }
}
