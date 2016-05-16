/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui.gpg;

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.GpgUtils;
import com.cipherdyne.jfwknop.InternationalizationHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.bouncycastle.openpgp.PGPException;

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

    private String homedirectory;

    /**
     *
     * @param frame parent window
     * @param fwknopKey GPG_SIGNER or GPG_RECIPIENT fwknop key to update when the key is selected
     */
    public GpgController(MainWindowView frame, EnumFwknopRcKey fwknopKey, String gpgHomeDirectory) {
        this.parentWindow = frame;
        this.homedirectory = gpgHomeDirectory;
        this.fwknopKey = fwknopKey;

        this.gpgView = new GpgView(this.parentWindow, gpgHomeDirectory);

        this.gpgView.getBtnSelect().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyId = GpgController.this.gpgView.getSelectKeyId();
                GpgController.this.parentWindow.getVariables().get(GpgController.this.fwknopKey).setText(keyId);
                GpgController.this.gpgView.dispose();
            }
        });
        this.gpgView.getBtnExport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedKeyId = GpgController.this.gpgView.getSelectKeyId();
                try {
                    GpgUtils.exportKey(GpgController.this.homedirectory, selectedKeyId);
                    JOptionPane.showMessageDialog(frame,
                        InternationalizationHelper.getMessage("i18n.export.key.success") + ": " + selectedKeyId,
                        InternationalizationHelper.getMessage("i18n.information"),
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException | PGPException ex) {
                    JOptionPane.showMessageDialog(frame,
                        InternationalizationHelper.getMessage("i18n.unable.to.export.key") + ": " + selectedKeyId + "\n" + ex.getMessage(),
                        InternationalizationHelper.getMessage("i18n.gpg.error"),
                        JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(GpgController.class.getName()).log(Level.SEVERE, null, ex);
                }
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
