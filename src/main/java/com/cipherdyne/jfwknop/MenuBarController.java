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

import com.cipherdyne.gui.EnumMenuItem;
import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.gui.about.About;
import com.cipherdyne.gui.ssh.SshController;
import com.cipherdyne.gui.wizard.WizardController;
import com.cipherdyne.utils.InternationalizationHelper;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;

/**
 * Controller used to handle menu behavior
 *
 * @author Franck Joncourt
 */
class MenuBarController extends AbstractController {

    public MenuBarController(MainWindowView parentView, MainWindowController parentController) {
        super(parentView, parentController);
    }

    @Override
    public void initialize() {
        // Set up action listener when creating an empty configuration
        this.parentView.getMenuItem(EnumMenuItem.FILE_NEW).addActionListener(e -> {
            this.parentController.getRcFileModel().reset();
        });

        // Set up action listener when opening a new configuration file
        this.parentView.getMenuItem(EnumMenuItem.FILE_OPEN).addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Browse");
            fileChooser.setFileHidingEnabled(false);
            final int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                this.parentController.loadRcFile(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Set up action listener when quitting the application
        this.parentView.getMenuItem(EnumMenuItem.FILE_EXIT).addActionListener(e -> System.exit(0));

        // Set up action listener when saving a file
        this.parentView.getMenuItem(EnumMenuItem.FILE_SAVE).addActionListener(e -> {
            if (this.parentController.getRcFileModel().exists()) {
                this.parentController.getRcFileModel().save(this.parentController.convertViewToConfig(this.parentView.getVariables()));
            } else {
                this.parentController.saveAs();
            }
        });
        this.parentView.getMenuItem(EnumMenuItem.FILE_SAVEAS).addActionListener(e -> {
            this.parentController.saveAs();
        });

        // Set up action listener to open a terminal
        this.parentView.getMenuItem(EnumMenuItem.TOOLS_OPENTERMINAL).addActionListener(e -> {
            ExternalCommand command = new ExternalCommand("x-terminal-emulator".split(" "), null);
            Thread thread = new Thread(command);
            thread.start();
        });

        // Set up action listener to edit rc file with the default text editor
        this.parentView.getMenuItem(EnumMenuItem.TOOLS_OPENRCFILE).addActionListener(e -> {
            String filename = this.parentController.getRcFileModel().getRcFilename();

            // Inform the user no rc file is currently loaded
            if (StringUtils.EMPTY.equals(filename)) {
                JOptionPane.showMessageDialog(this.parentView,
                    InternationalizationHelper.getMessage("i18n.no.rcfile.loaded"),
                    InternationalizationHelper.getMessage("i18n.error"),
                    JOptionPane.ERROR_MESSAGE);
            } // Otherwise launch the editor
            else {
                try {
                    Desktop.getDesktop().open(new File(filename));
                    //ExternalCommand extCmd = new ExternalCommand("xdg-open " + filename);
                    //extCmd.run();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Set up action listener to scp files to remote server
        this.parentView.getMenuItem(EnumMenuItem.TOOLS_EXPORT_FILE).addActionListener(e -> {
            new SshController(this.parentView);
        });

        // Set up action listener to generate access.conf file for fwknop server
        this.parentView.getMenuItem(EnumMenuItem.TOOLS_GENERATE_ACCESS).addActionListener((ActionEvent e) -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(InternationalizationHelper.getMessage(InternationalizationHelper.getMessage("i18n.save.as")));
            fileChooser.setSelectedFile(new File("access.conf"));
            final int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                AccessFile accessFile = new AccessFile(fileChooser.getSelectedFile().getAbsolutePath());
                accessFile.generate(this.parentController.convertViewToConfig(this.parentView.getVariables()));
            }
        });

        // Run wizard
        this.parentView.getMenuItem(EnumMenuItem.WIZARD_EASYSETUP).addActionListener(e -> {
            new WizardController(this.parentController, this.parentView);
        });

        this.parentView.getMenuItem(EnumMenuItem.HELP_ABOUT).addActionListener(e -> {
            About aboutView = new About(this.parentView);
            aboutView.setVisible(true);
        });

    }
}
