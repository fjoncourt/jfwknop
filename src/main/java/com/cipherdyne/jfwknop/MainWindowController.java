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
import com.cipherdyne.gui.ssh.SshController;
import com.cipherdyne.gui.wizard.WizardController;
import com.cipherdyne.model.FwknopClientModel;
import com.cipherdyne.model.KeyModel;
import com.cipherdyne.model.RcFileModel;
import com.cipherdyne.utils.InternationalizationHelper;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainWindowController {

    static final Logger LOGGER = LogManager.getLogger(MainWindowController.class.getName());

    private final MainWindowView view;
    private final RcFileModel rcFileModel;
    private final FwknopClientModel fwknopClientModel;
    private final KeyModel keyModel;
    private final JFwknopConfig jfwknopConfig;

    public MainWindowController() {

        // Read the application settings
        this.jfwknopConfig = JFwknopConfig.getInstance();

        // Initialize the internationalization helper
        InternationalizationHelper.configure(this.jfwknopConfig.getConfigKey().get(EnumFwknopConfigKey.LANGUAGE));

        // Build the view
        this.view = new MainWindowView("Default configuration");
        this.view.buildRecentFilesList(this.jfwknopConfig.getRecentFileList());
        this.rcFileModel = new RcFileModel(this.view);

        // Initialize the client model from the configuration
        this.fwknopClientModel = new FwknopClientModel(this.view);

        // Initialize the key model from the configuration
        this.keyModel = new KeyModel(this.view);

        // Set up sub controllers
        List<IController> controllerList = new ArrayList<>(Arrays.asList(
            new CipherTabController(this.view, this),
            new GeneralTabController(this.view, this),
            new SettingsTabController(this.view, this),
            new ConsoleController(this.view, this)));

        for (IController controller : controllerList) {
            controller.initialize();
        }

        // Setup action listeners
        populateMenuBar();

        this.view.display();

        loadDefaultFwknoprc();
    }

    /**
     * Load ~./fwknoprc file if only one user specifi stanza is available
     */
    private void loadDefaultFwknoprc() {
        try {
            String fwknoprc = System.getProperty("user.home") + System.getProperty("file.separator") + ".fwknoprc";

            // Initialize the rc file model and check for the number of stanza available before laoding it
            this.rcFileModel.setRcFilename(fwknoprc);
            List<String> stanzaList = this.rcFileModel.getStanzas();

            // If there is no stanza or only one stanza defined, we can load the rf file
            if (stanzaList.size() < 2) {
                this.rcFileModel.setRcFilename(fwknoprc);
                this.rcFileModel.load();
            } // If there are more than one stanza defined, we prompt the user to select the stanza to load
            else {
                String selectedStanza = (String) JOptionPane.showInputDialog(this.view,
                    InternationalizationHelper.getMessage("i18n.select.the.stanza.to.load"),
                    InternationalizationHelper.getMessage("i18n.warning"),
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    stanzaList.toArray(),
                    stanzaList.get(0));

                // If the user cancel, we abort the update by throwing an exception
                if (selectedStanza == null) {
                    throw new IOException();
                }

                this.rcFileModel.load(selectedStanza);
            }

            updateNewRcFile(fwknoprc);
            updateConfigurationList();
        } catch (IOException ex) {
            // Nothing to do - The file does not exist or no stanza has ben selected by the user
        }
    }

    /**
     * @return the key model that contains key settings
     */
    public KeyModel getKeyModel() {
        return this.keyModel;
    }

    /**
     * Update the fwknop client model with the settings set by the user in the user interface
     */
    public void updateFwknopModel() {
        this.fwknopClientModel.setFwknopConfig(EnumFwknopConfigKey.FWKNOP_FILEPATH,
            this.view.getVarFwknopFilePath().getText());
        this.fwknopClientModel.setFwknopConfig(EnumFwknopConfigKey.FWKNOP_ARGS,
            this.view.getVarFwknopArgs().getText());
        this.fwknopClientModel.setFwknopConfig(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS,
            this.view.getVarFwknopExtraArgs().getText());
        this.fwknopClientModel.setFwknopConfig(EnumFwknopConfigKey.FWKNOP_VERBOSE,
            this.view.getBtnFwknopVerbose().isSelected() ? "1" : "0");
    }

    private void populateMenuBar() {

        // Set up action listener when opening a new configuration file
        this.view.getOpenMenuItem().addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Browse");
            fileChooser.setFileHidingEnabled(false);
            final int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    this.rcFileModel.setRcFilename(filename);
                    this.rcFileModel.load();
                    updateNewRcFile(filename);
                    updateConfigurationList();
                } catch (IOException ex) {
                    this.LOGGER.error("Unable to load rc file : " + filename);
                }
            }
        });

        // Set up action listener when quitting the application
        this.view.getExitMenuItem().addActionListener(e -> System.exit(0));

        // Set up action listener when saving a file
        this.view.getSaveMenuItem().addActionListener(e -> {
            if (MainWindowController.this.rcFileModel.exists()) {
                MainWindowController.this.rcFileModel.save(convertViewToConfig(this.view.getVariables()));
            } else {
                saveAs();
            }
        });
        this.view.getSaveAsMenuItem().addActionListener(e -> {
            MainWindowController.this.saveAs();
        });

        // Set up action listener to open a terminal
        this.view.getOpenTerminalMenuItem().addActionListener(e -> {
            ExternalCommand command = new ExternalCommand("x-terminal-emulator".split(" "), null);
            Thread thread = new Thread(command);
            thread.start();
        });

        // Set up action listener to edit rc file with the default text editor
        this.view.getOpenRcFileMenuItem().addActionListener(e -> {
            String filename = MainWindowController.this.rcFileModel.getRcFilename();

            // Inform the user no rc file is currently loaded
            if (StringUtils.EMPTY.equals(filename)) {
                JOptionPane.showMessageDialog(MainWindowController.this.view,
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
        this.view.getExportFileMenuItem().addActionListener(e -> {
            new SshController(this.view);
        });

        // Set up action listener to generate access.conf file for fwknop server
        this.view.getGenerateAccessMenuItem().addActionListener((ActionEvent e) -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(InternationalizationHelper.getMessage(InternationalizationHelper.getMessage("i18n.save.as")));
            fileChooser.setSelectedFile(new File("access.conf"));
            final int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                AccessFile accessFile = new AccessFile(fileChooser.getSelectedFile().getAbsolutePath());
                accessFile.generate(convertViewToConfig(MainWindowController.this.view.getVariables()));
            }
        });

        this.view.getEasySetupMenuItem().addActionListener(e -> {
            new WizardController(this, this.view);
        });

        populateRecentFiles();

        // FIXME: Not very nice
        // Set up config list and select default config
        ArrayList<String> configs = new ArrayList<>();
        configs.add(InternationalizationHelper.getMessage("i18n.default"));
        configs.addAll(1, this.jfwknopConfig.getRecentFileList());
        this.view.setCbConfigList(configs.toArray(new String[0]));
    }

    /**
     * Save current fwknop configuration.
     *
     * If the fwknoprc file does not exist, the user is prompted to select where to save the
     * configuration
     *
     * @return 0 if successful, > 0 if an error occured
     */
    public int save() {
        int error = 0;
        if (MainWindowController.this.rcFileModel.exists()) {
            MainWindowController.this.rcFileModel.save(convertViewToConfig(this.view.getVariables()));
        } else {
            error = saveAs();
        }

        return error;
    }

    /**
     * Open browser to allow user to select the filename to save the current configuration to
     *
     * @return 0 if successful, > 0 if an error occured
     */
    private int saveAs() {
        int error = 0;
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(InternationalizationHelper.getMessage("i18n.save.as"));
        final int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            final String filename = fileChooser.getSelectedFile().getAbsolutePath();
            MainWindowController.this.rcFileModel.saveAs(convertViewToConfig(this.view.getVariables()),
                filename);
            updateNewRcFile(filename);
            updateConfigurationList();
        } else {
            error = 1;
        }

        return error;
    }

    /**
     * Populate recent file list from the file menu.
     *
     * This function adds action listener to each entry in the recent file list. When selected, the
     * file is loaded.
     */
    private void populateRecentFiles() {
        for (final JMenuItem miFilename : this.view.getVarRecentRcFiles()) {
            miFilename.addActionListener(e -> {
                try {
                    this.rcFileModel.setRcFilename(e.getActionCommand());
                    this.rcFileModel.load();
                    updateNewRcFile(e.getActionCommand());
                } catch (IOException ex) {
                    this.LOGGER.error("Unable to load rc file : " + e.getActionCommand());
                }
            });
        }
    }

    /**
     * Update the combo box list with the latest configuration files loaded by the user
     */
    private void updateConfigurationList() {
        String[] configs = new String[this.jfwknopConfig.getRecentFileList().size()];
        configs = this.jfwknopConfig.getRecentFileList().toArray(configs);
        this.view.setCbConfigList(configs);
    }

    public void updateNewRcFile(String rcFilename) {
        this.view.getVarFwknopArgs().setArgs("--rc-file " + rcFilename);
        this.view.setTitle(rcFilename);
        this.jfwknopConfig.addRecentFile(rcFilename);
        MainWindowController.this.view.buildRecentFilesList(this.jfwknopConfig.getRecentFileList());
        populateRecentFiles();
    }

    private Map<EnumFwknopRcKey, String> convertViewToConfig(final Map<EnumFwknopRcKey, IFwknopVariable> viewVariables) {
        final Map<EnumFwknopRcKey, String> updatedConfig = new HashMap<>();
        String value;
        for (final Entry<EnumFwknopRcKey, IFwknopVariable> entry : viewVariables.entrySet()) {
            value = ((IFwknopVariable) entry.getValue()).getText();
            if (isValidFwknopValue(value)) {
                updatedConfig.put(entry.getKey(), value);
            }
        }
        return updatedConfig;
    }

    private boolean isValidFwknopValue(final String value) {

        boolean valid = true;

        if (value == null) {
            valid = false;
        } else if (value.isEmpty()) {
            valid = false;
        } else {
            final Pattern pattern = Pattern.compile("^<.*>$");
            final Matcher matcher = pattern.matcher(value);

            if (matcher.find()) {
                valid = false;
            }
        }

        return valid;
    }

    public RcFileModel getRcFileModel() {
        return this.rcFileModel;
    }

    /**
     * @return the fwknop model
     */
    public FwknopClientModel getFwknopClientModel() {
        return this.fwknopClientModel;
    }
}
