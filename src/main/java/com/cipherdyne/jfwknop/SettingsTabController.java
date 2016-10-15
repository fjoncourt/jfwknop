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

import com.cipherdyne.gui.MainWindowView;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

/**
 *
 * @author Franck Joncourt
 */
class SettingsTabController extends AbstractController {

    public SettingsTabController(MainWindowView parentView, MainWindowController parentController) {
        super(parentView, parentController);
    }

    @Override
    public void initialize() {

        // Add action listener to browse for another fwknop file path
        this.parentView.getBtnBrowseforFwknop().addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Browse");
            final int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                this.parentView.setFwknopFilePath(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Add action listener to enable/disable verbose mode
        this.parentView.getBtnFwknopVerbose().addActionListener((ActionEvent e) -> {
            this.parentView.getVarFwknopArgs().setVerbose(((JCheckBox) e.getSource()).isSelected());
        });

        // Add action listener to enable/disable test mode
        this.parentView.getBtnFwknopTest().addActionListener((ActionEvent e) -> {
            this.parentView.getVarFwknopArgs().setTest(((JCheckBox) e.getSource()).isSelected());
        });

        // Add action listener to save fwknop settings
        this.parentView.getBtnSaveFwknopSettings().addActionListener((ActionEvent e) -> {
            this.parentController.save();
            this.parentController.updateFwknopModel();
            this.parentController.getFwknopClientModel().save();
        });

        // Add action listener to save key settings
        this.parentView.getBtnSaveKeySettings().addActionListener((ActionEvent e) -> {
            this.updateKeyModel();
            this.parentController.getKeyModel().save();
        });
    }

    /**
     * Update the key model with the settings set by the user in the user interface
     */
    private void updateKeyModel() {
        this.parentController.getKeyModel().setContext(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH,
            this.parentView.getVarKeyRijndaelLength().getText());
        this.parentController.getKeyModel().setContext(EnumFwknopConfigKey.KEY_HMAC_LENGTH,
            this.parentView.getVarKeyHmacLength().getText());
        this.parentController.getKeyModel().setContext(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH,
            this.parentView.getVarBase64RijndaelBytes().getText());
        this.parentController.getKeyModel().setContext(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH,
            this.parentView.getVarBase64HmacBytes().getText());
    }
}
