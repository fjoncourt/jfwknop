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
package com.cipherdyne.jfwknop;

import com.cipherdyne.gui.EnumButton;
import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.gui.components.IFwknopVariable;
import com.cipherdyne.gui.gpg.GpgController;
import com.cipherdyne.model.KeyModel;
import com.cipherdyne.utils.InternationalizationHelper;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Franck Joncourt
 */
public class CipherTabController extends AbstractController {

    public CipherTabController(MainWindowView parentView, MainWindowController parentController) {
        super(parentView, parentController);
    }

    /**
     * Set up action listeners for buttons
     */
    @Override
    public void initialize() {

        // Add action listener to generate/remove rijndael key
        this.parentView.getButton(EnumButton.CIPHER_GENERATE_RIJNDAEL_KEY).addActionListener((ActionEvent e) -> {
            this.parentView.getVariables().get(EnumFwknopRcKey.KEY).setText(
                this.parentController.getKeyModel().getRandomRijndaelKey());
        });

        this.parentView.getButton(EnumButton.CIPHER_REMOVE_RIJNDAEL_KEY).addActionListener((ActionEvent e) -> {
            this.parentView.getVariables().get(EnumFwknopRcKey.KEY).setDefaultValue();
        });

        // Add action listener to generate/remove rijndael base64 key
        this.parentView.getButton(EnumButton.CIPHER_GENERATE_BASE64_RIJNDAEL_KEY).addActionListener((ActionEvent e) -> {
            this.parentView.getVariables().get(EnumFwknopRcKey.KEY_BASE64).setText(
                this.parentController.getKeyModel().getRandomBase64Rijndael());
        });

        this.parentView.getButton(EnumButton.CIPHER_REMOVE_BASE64_RIJNDAEL_KEY).addActionListener((ActionEvent e) -> {
            ((IFwknopVariable) this.parentView.getVariables().get(EnumFwknopRcKey.KEY_BASE64)).setDefaultValue();
        });

        // Add action listener to generate/remove rijndael key
        this.parentView.getButton(EnumButton.CIPHER_GENERATE_HMAC_KEY).addActionListener((ActionEvent e) -> {
            this.parentView.getVariables().get(EnumFwknopRcKey.HMAC_KEY).setText(
                this.parentController.getKeyModel().getRandomHmacKey());
            this.parentView.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("Y");
        });

        this.parentView.getButton(EnumButton.CIPHER_REMOVE_HMAC_KEY).addActionListener((ActionEvent e) -> {
            this.parentView.getVariables().get(EnumFwknopRcKey.HMAC_KEY).setDefaultValue();
            this.parentView.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("N");
        });

        // Add action listener to generate/remove HMAC base64 key
        this.parentView.getButton(EnumButton.CIPHER_GENERATE_BASE64_HMAC_KEY).addActionListener((ActionEvent e) -> {
            this.parentView.getVariables().get(EnumFwknopRcKey.HMAC_KEY_BASE64).setText(
                this.parentController.getKeyModel().getRandomBase64Hmac());
            this.parentView.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("Y");
        });

        this.parentView.getButton(EnumButton.CIPHER_REMOVE_BASE64_HMAC_KEY).addActionListener((ActionEvent e) -> {
            this.parentView.getVariables().get(EnumFwknopRcKey.HMAC_KEY_BASE64).setDefaultValue();
            this.parentView.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("N");
        });

        // Add action listener to select a GPG key and its home directory
        this.parentView.getButton(EnumButton.CIPHER_SELECT_RECIPIENT_GPG_ID).addActionListener((ActionEvent e) -> {
            javax.swing.SwingUtilities.invokeLater(() -> new GpgController(this.parentView,
                this.parentView.getVariables().get(EnumFwknopRcKey.GPG_RECIPIENT),
                this.parentView.getVariables().get(EnumFwknopRcKey.GPG_HOMEDIR).getText()));
        });

        this.parentView.getButton(EnumButton.CIPHER_SELECT_SIGNER_GPG_ID).addActionListener((ActionEvent e) -> {
            javax.swing.SwingUtilities.invokeLater(() -> new GpgController(this.parentView,
                this.parentView.getVariables().get(EnumFwknopRcKey.GPG_SIGNER),
                this.parentView.getVariables().get(EnumFwknopRcKey.GPG_HOMEDIR).getText()));
        });

        this.parentView.getButton(EnumButton.CIPHER_BROWSE_GPG_HOMEDIR).addActionListener((ActionEvent e) -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileHidingEnabled(false);
            fileChooser.setDialogTitle(InternationalizationHelper.getMessage("i18n.browse"));
            final int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                this.parentView.getVariables().get(EnumFwknopRcKey.GPG_HOMEDIR).setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        this.parentView.getButton(EnumButton.CIPHER_GENERATE_BASE64_GPG).addActionListener((ActionEvent e) -> {
            IFwknopVariable gpgSigningPw = (IFwknopVariable) this.parentView.getVariables().get(EnumFwknopRcKey.GPG_SIGNING_PW);
            if (gpgSigningPw.isDefault()) {
                JOptionPane.showMessageDialog(this.parentView,
                    InternationalizationHelper.getMessage("i18n.please.fill.in.passphrase"),
                    InternationalizationHelper.getMessage("i18n.error"),
                    JOptionPane.ERROR_MESSAGE);
            } else {
                IFwknopVariable gpgSigningPwBase64 = (IFwknopVariable) this.parentView.getVariables().get(EnumFwknopRcKey.GPG_SIGNING_PW_BASE64);
                gpgSigningPwBase64.setText(KeyModel.encodeToBase64(gpgSigningPw.getText().getBytes()));
            }
        });
    }
}
