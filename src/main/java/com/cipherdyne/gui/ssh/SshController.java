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

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.gui.ssh.SshView.EnumSshSettings;
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.utils.InternationalizationHelper;
import com.cipherdyne.utils.SshUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;

/**
 *
 * @author franck
 */
public class SshController {

    // View used to display Ssh settings
    final private SshView view;

    // Parent window we come from
    final private MainWindowView parentWindow;

    public SshController(MainWindowView frame) {
        this.parentWindow = frame;
        this.view = new SshView(this.parentWindow);

        // Initialize some parameters from the parent window
        if (!parentWindow.getVariables().get(EnumFwknopRcKey.SPA_SERVER).isDefault()) {
            this.view.getSettings().get(EnumSshSettings.REMOTEHOST).setText(
                parentWindow.getVariables().get(EnumFwknopRcKey.SPA_SERVER).getText());
        }

        // Set up action listeners
        populateBtn();

        this.view.setVisible(true);
    }

    /**
     * Set up action listenr for all available buttons from the SshView
     */
    private void populateBtn() {
        this.view.getBtnBrowse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle(InternationalizationHelper.getMessage("i18n.browse.file"));
                fileChooser.setFileHidingEnabled(false);
                final int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    final String filename = fileChooser.getSelectedFile().getAbsolutePath();
                    SshController.this.view.getSettings().get(EnumSshSettings.FILEPATH).setText(filename);
                }
            }
        });

        this.view.getBtnExport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SshUtils.scpFile(SshController.this.view.getSettings().get(EnumSshSettings.REMOTEHOST).getText(),
                    Integer.parseInt(SshController.this.view.getSettings().get(EnumSshSettings.REMOTEPORT).getText()),
                    SshController.this.view.getSettings().get(EnumSshSettings.USERNAME).getText(), 
                    SshController.this.view.getSettings().get(EnumSshSettings.PASSWORD).getText(), 
                    SshController.this.view.getSettings().get(EnumSshSettings.FILEPATH).getText());
            }
        });
    }
}
