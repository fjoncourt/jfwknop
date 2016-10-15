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

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.utils.GpgUtils;
import com.cipherdyne.utils.InternationalizationHelper;
import com.cipherdyne.jfwknop.MainWindowController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.bouncycastle.openpgp.PGPException;

/**
 *
 */
public class GpgController {

    // View used to display all GPG user interface components
    final private GpgView view;

    // Parent window we come from
    final private MainWindowView parentWindow;

    // Fwknop key to update when the key is slected
    final EnumFwknopRcKey fwknopKey;

    // GPG gome directory where GPG keys are stored
    final private String gpgHomeDirectory;

    /**
     * Create a GPG controller that handled the GPG view
     *
     * @param frame parent window
     * @param fwknopKey GPG_SIGNER or GPG_RECIPIENT fwknop key to update when the key is selected
     * @param gpgHomeDirectory GPG home directory to use to display GPG settings
     */
    public GpgController(MainWindowView frame, EnumFwknopRcKey fwknopKey, String gpgHomeDirectory) {
        this.parentWindow = frame;
        this.gpgHomeDirectory = gpgHomeDirectory;
        this.fwknopKey = fwknopKey;

        // Build the GPG view according to the GPG home directory selected
        this.view = new GpgView(this.parentWindow, gpgHomeDirectory);

        // Configure button action listeners
        updateBtnBehaviour();

        // Display the view
        this.view.setVisible(true);
    }

    /**
     * Update the behaviour of all buttons displayed in the GPG view
     */
    private void updateBtnBehaviour() {
        this.view.getBtnSelect().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyId = GpgController.this.view.getSelectKeyId();
                GpgController.this.parentWindow.getVariables().get(GpgController.this.fwknopKey).setText(keyId);
                GpgController.this.view.dispose();
            }
        });
        this.view.getBtnExport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedKeyId = GpgController.this.view.getSelectKeyId();
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle(InternationalizationHelper.getMessage(InternationalizationHelper.getMessage("i18n.save.as")));
                fileChooser.setSelectedFile(new File(selectedKeyId + ".asc"));
                final int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        GpgUtils.exportKey(GpgController.this.gpgHomeDirectory, selectedKeyId, filename);
                        JOptionPane.showMessageDialog(GpgController.this.parentWindow,
                            InternationalizationHelper.getMessage("i18n.export.key.success") + ": " + selectedKeyId,
                            InternationalizationHelper.getMessage("i18n.information"),
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException | PGPException ex) {
                        JOptionPane.showMessageDialog(GpgController.this.parentWindow,
                            InternationalizationHelper.getMessage("i18n.unable.to.export.key") + ": " + selectedKeyId + "\n" + ex.getMessage(),
                            InternationalizationHelper.getMessage("i18n.gpg.error"),
                            JOptionPane.ERROR_MESSAGE);
                        Logger.getLogger(GpgController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });
        this.view.getBtnImport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle(InternationalizationHelper.getMessage("i18n.browse.for.gpg.key"));
                final int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        GpgUtils.addPublicKeyToKeyring(gpgHomeDirectory, fileChooser.getSelectedFile().getAbsolutePath());
                        ((GpgTableModel) (GpgController.this.view.getKeyTable().getModel())).reload();
                        JOptionPane.showMessageDialog(GpgController.this.parentWindow,
                            InternationalizationHelper.getMessage("i18n.import.key.success") + ": " + fileChooser.getSelectedFile().getAbsolutePath(),
                            InternationalizationHelper.getMessage("i18n.information"),
                            JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException | PGPException ex) {
                        JOptionPane.showMessageDialog(GpgController.this.parentWindow,
                            InternationalizationHelper.getMessage("i18n.unable.to.import.key") + ": " + fileChooser.getSelectedFile().getAbsolutePath() + "\n" + ex.getMessage(),
                            InternationalizationHelper.getMessage("i18n.gpg.error"),
                            JOptionPane.ERROR_MESSAGE);
                        java.util.logging.Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        this.view.getBtnRemove().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String selectedKeyId = GpgController.this.view.getSelectKeyId();
                    GpgUtils.removeKeyFromKeyring(gpgHomeDirectory, selectedKeyId);
                    ((GpgTableModel) (GpgController.this.view.getKeyTable().getModel())).reload();
                } catch (IOException | PGPException ex) {
                    Logger.getLogger(GpgController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        this.view.getBtnCreate().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GpgKeySettingsController keyController = new GpgKeySettingsController(GpgController.this.parentWindow, gpgHomeDirectory);
                    ((GpgTableModel) (GpgController.this.view.getKeyTable().getModel())).reload();
                } catch (IOException | PGPException ex) {
                    Logger.getLogger(GpgController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        this.view.getBtnCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GpgController.this.view.dispose();
            }
        });

    }
}
