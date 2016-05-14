/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui.gpg;

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author franck
 */
public class GpgController {

    // View used to display all GPG user interface components
    private GpgView gpgView;

    // Parent window we come from
    private MainWindowView parentWindow;
    
    // Fwknop key to update when the key is slected
    EnumFwknopRcKey fwknopKey; 

    /**
     *
     * @param frame parent window
     * @param fwknopKey GPG_SIGNER or GPG_RECIPIENT fwknop key to update when the key is selected
     */
    public GpgController(MainWindowView frame, EnumFwknopRcKey fwknopKey, String gpgHomeDirectory) {
        this.parentWindow = frame;
        this.fwknopKey = fwknopKey;
        
        this.gpgView = new GpgView(this.parentWindow, gpgHomeDirectory);

        this.gpgView.getBtnSelect().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyId = GpgController.this.gpgView.getSelectKeyId();
                System.out.println(keyId);
                GpgController.this.parentWindow.getVariables().get(GpgController.this.fwknopKey).setText(keyId);
                GpgController.this.gpgView.dispose();
            }
        });
        this.gpgView.getBtnCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GpgController.this.gpgView.dispose();
            }
        });

        this.gpgView.setVisible(true);
    }
}
