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
import com.cipherdyne.gui.components.JFwknopComboBox;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 *
 * @author Franck Joncourt
 */
public class ConsoleController extends AbstractController {

    public ConsoleController(MainWindowView parentView, MainWindowController parentController) {
        super(parentView, parentController);
    }

    /**
     * Set up action listners
     */
    @Override
    public void initialize() {
        // Add action listener to clear the console
        this.parentView.getBtnClearConsole().addActionListener(e -> {
            this.parentView.clearConsole();
        });

        // Add action listener to run fwknop binary
        this.parentView.getBtnExecute().addActionListener(e -> {
            long period = 0;
            boolean stopEnabled = false;

            // Try to save the current settings before executing the fwknop client
            if (this.parentController.save() == 0) {
                this.parentController.updateFwknopModel();

                if (this.parentView.getPeriodicExecution().isSelected()) {
                    period = Long.parseLong(this.parentView.getFwknopPeriod().getText());
                    stopEnabled = true;
                }

                this.parentView.getBtnStop().setEnabled(stopEnabled);
                this.parentController.getFwknopClientModel().start(period);
            }
        });

        this.parentView.getCbConfigList().addActionListener((ActionEvent e) -> {
            JFwknopComboBox cb = ((JFwknopComboBox) e.getSource());
            String filename = cb.getText();

            // Reload the rc file if this is not the default configuration and filename is valid
            if ((filename != null) && !cb.isDefault()) {
                try {
                    if (!this.parentController.getRcFileModel().getRcFilename().equals(filename)) {
                        this.parentController.getRcFileModel().setRcFilename(filename);
                        this.parentController.getRcFileModel().load();
                        this.parentController.updateNewRcFile(filename);
                    }
                } catch (IOException ex) {
                    MainWindowController.LOGGER.error("Unable to load rc file : " + filename);
                }
            }
        });

        this.parentView.getBtnStop().addActionListener((ActionEvent e) -> {
            this.parentController.getFwknopClientModel().stop();
            this.parentView.getBtnStop().setEnabled(false);
        });
    }
}
