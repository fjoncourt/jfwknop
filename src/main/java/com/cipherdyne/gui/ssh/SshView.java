/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui.ssh;

import com.cipherdyne.jfwknop.IFwknopVariable;
import com.cipherdyne.utils.InternationalizationHelper;
import com.cipherdyne.gui.components.JFwknopLabel;
import com.cipherdyne.gui.components.JFwknopTextField;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;

/**
 * Create a UI to display SSH settings to export a file to a remote server.
 *
 * @author franck
 */
public class SshView extends JDialog {

    // SSH settings map that contains username, password as IFwknopVariable
    private final Map<EnumSshSettings, IFwknopVariable> settingsMap = new HashMap<>();

    // Browse button to look for the file to send
    private final JButton btnBrowse;

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

        // Create components
        this.settingsMap.put(EnumSshSettings.USERNAME, new JFwknopTextField("<username>"));
        this.settingsMap.put(EnumSshSettings.PASSWORD, new JFwknopTextField("<password>"));
        this.settingsMap.put(EnumSshSettings.REMOTEHOST, new JFwknopTextField("<remote host>"));
        this.settingsMap.put(EnumSshSettings.REMOTEPORT, new JFwknopTextField("22"));
        this.settingsMap.put(EnumSshSettings.FILEPATH, new JFwknopTextField("<filepath>"));

        // Create buttons
        this.btnBrowse = new JButton(InternationalizationHelper.getMessage("i18n.browse.file"));
        this.btnExport = new JButton(InternationalizationHelper.getMessage("i18n.export.file"));

        // Add components to the panel
        this.setLayout(new MigLayout("inset 20, gap 0, flowx, wrap 2", "[200!][300!]", ""));
        this.add(this.btnBrowse, "growx, span 2");
        this.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.ssh.filepath")), "growx");
        this.add((Component) this.settingsMap.get(EnumSshSettings.FILEPATH), "growx, gapbottom 20");
        
        this.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.ssh.user")), "growx");
        this.add((Component) this.settingsMap.get(EnumSshSettings.USERNAME), "growx");
        this.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.ssh.password")), "growx");
        this.add((Component) this.settingsMap.get(EnumSshSettings.PASSWORD), "growx");
        this.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.ssh.remotehostname")), "growx");
        this.add((Component) this.settingsMap.get(EnumSshSettings.REMOTEHOST), "growx");
        this.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.ssh.remoteport")), "growx");
        this.add((Component) this.settingsMap.get(EnumSshSettings.REMOTEPORT), "growx");

        this.add(this.btnExport, "gaptop 20, growx, span 2");

        this.pack();
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
     * @return the browse button used to look for the local file to send to remote SSH server
     */
    public JButton getBtnBrowse() {
        return this.btnBrowse;
    }

    /**
     * Enum used to access all available variables in the view
     */
    public enum EnumSshSettings {
        USERNAME, PASSWORD, REMOTEHOST, REMOTEPORT, FILEPATH;
    }
}
