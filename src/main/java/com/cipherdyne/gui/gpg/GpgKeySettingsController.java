/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui.gpg;

import com.cipherdyne.jfwknop.GpgUtils;
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
