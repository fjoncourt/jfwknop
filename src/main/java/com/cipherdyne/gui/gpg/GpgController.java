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
import com.cipherdyne.jfwknop.MainWindowController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.bouncycastle.openpgp.PGPException;

/**
 *
 */
public class GpgController {

    // View used to display all GPG user interface components
    final private GpgView gpgView;

    // Parent window we come from
    final private MainWindowView parentWindow;

    // Fwknop key to update when the key is slected
    final EnumFwknopRcKey fwknopKey;

    // GPG gome directory where GPG keys are stored
    final private String gpgHomeDirectory;

    /**
     *
     * @param frame parent window
     * @param fwknopKey GPG_SIGNER or GPG_RECIPIENT fwknop key to update when the key is selected
     */
    public GpgController(MainWindowView frame, EnumFwknopRcKey fwknopKey, String gpgHomeDirectory) {
        this.parentWindow = frame;
        this.gpgHomeDirectory = gpgHomeDirectory;
        this.fwknopKey = fwknopKey;

        // Build the GPG view according to the GPG home directory selected
        this.gpgView = new GpgView(this.parentWindow, gpgHomeDirectory);

        // Configure button action listeners
        updateBtnBehaviour();

        // Display the view
        this.gpgView.setVisible(true);
    }

    /**
     * Update the behaviour of all buttons displayed in the GPG view
     */
    private void updateBtnBehaviour() {
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
                    GpgUtils.exportKey(GpgController.this.gpgHomeDirectory, selectedKeyId);
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
        });
        this.gpgView.getBtnImport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle(InternationalizationHelper.getMessage("i18n.browse.for.gpg.key"));
                final int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        GpgUtils.addKeyToKeyring(gpgHomeDirectory, fileChooser.getSelectedFile().getAbsolutePath());
                        ((GpgTableModel) (GpgController.this.gpgView.getKeyTable().getModel())).reload();
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
        this.gpgView.getBtnRemove().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String selectedKeyId = GpgController.this.gpgView.getSelectKeyId();
                    GpgUtils.removeKeyFromKeyring(gpgHomeDirectory, selectedKeyId);
                    ((GpgTableModel) (GpgController.this.gpgView.getKeyTable().getModel())).reload();
                } catch (IOException | PGPException ex) {
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
    }
}
