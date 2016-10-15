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
package com.cipherdyne.gui.gpg;

import com.cipherdyne.utils.GpgUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 * 
 */
public class GpgKeySettingsController {

    // GPG key settings view used by the controller
    private GpgKeySettingsView view;
    
    // GPG home directory to use to create the brand new GPG key
    private String gpgHomeDirectory;
    
    /**
     * Constructor
     * 
     * @param parentWindow parent frame
     * @param gpgHomeDirectory GPG home directory
     */
    public GpgKeySettingsController(JFrame parentWindow, String gpgHomeDirectory) {
        this.gpgHomeDirectory = gpgHomeDirectory;
        this.view = new GpgKeySettingsView(parentWindow);
        updateBtnBehaviour();
        this.view.setVisible(true);
    }

    /**
     * Update button action listeners
     */
    private void updateBtnBehaviour() {
        this.view.getBtnSubmit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = GpgKeySettingsController.this.view.getUserId().getText();
                String passphrase = GpgKeySettingsController.this.view.getPassphrase().getText();
                GpgUtils.createKey(gpgHomeDirectory, userId, passphrase);
                GpgKeySettingsController.this.view.dispose();
            }
        });
    }
}
