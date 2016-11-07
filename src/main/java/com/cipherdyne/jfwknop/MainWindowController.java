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
import com.cipherdyne.gui.components.IFwknopVariable;
import static com.cipherdyne.jfwknop.JFwknopConfig.getJfwknopWorkingDirectory;
import com.cipherdyne.model.FwknopClientModel;
import com.cipherdyne.model.KeyModel;
import com.cipherdyne.model.RcFileModel;
import com.cipherdyne.utils.InternationalizationHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
        this.view = new MainWindowView();
        this.view.buildRecentFilesList(this.jfwknopConfig.getRecentFileList());
        this.rcFileModel = new RcFileModel(this.view);

        // Initialize the client model from the configuration
        this.fwknopClientModel = new FwknopClientModel(this.view);

        // Initialize the key model from the configuration
        this.keyModel = new KeyModel(this.view);

        // Set up sub controllers
        List<IController> controllerList = new ArrayList<>(Arrays.asList(
            new MenuBarController(this.view, this),
            new CipherTabController(this.view, this),
            new GeneralTabController(this.view, this),
            new SettingsTabController(this.view, this),
            new ConsoleController(this.view, this)));

        for (IController controller : controllerList) {
            controller.initialize();
        }

        populateRecentFiles();

        // FIXME: Not very nice
        // Set up config list and select default config
        ArrayList<String> configs = new ArrayList<>();
        configs.add(InternationalizationHelper.getMessage("i18n.default"));
        configs.addAll(1, this.jfwknopConfig.getRecentFileList());
        this.view.setCbConfigList(configs.toArray(new String[0]));

        this.view.display();

        // Load initial rc file at startup
        loadRcFile();
    }

    /**
     * Load ~/.fwknoprc file or the latest configuration previously open if it exists
     */
    private void loadRcFile() {

        String rcFilename = System.getProperty("user.home") + System.getProperty("file.separator") + RcFileModel.FWKNOPRC;
        if (this.jfwknopConfig.getRecentFileList().size() > 0) {
            rcFilename = this.jfwknopConfig.getRecentFileList().get(0);
        }

        // Load rc file
        loadRcFile(rcFilename);
    }

    /**
     * Load a rc file and check whether this is a multi stanza file or a single stanza file.
     *
     * If this is a multi stanza file, then the application prompts the user to select the stanza to
     * load and split the file in several single stanza file.
     *
     * @param rcFilename rc file to load
     */
    public void loadRcFile(String rcFilename) {
        try {

            // Initialize the rc file model and check for the number of stanza available before laoding it
            this.rcFileModel.setRcFilename(rcFilename);
            List<String> stanzaList = this.rcFileModel.getStanzas();

            // If there are more than one stanza defined, we prompt the user to select the stanza to load
            if (stanzaList.size() >= 2) {
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

                // Convert the multiple stanza file to several single stanza file
                this.rcFileModel.convertToSingleStanzaFile(rcFilename);

                // Select the new single stanza file as rc file
                this.rcFileModel.setRcFilename(rcFilename + "." + selectedStanza);
            }

            this.rcFileModel.load();
            updateNewRcFile(this.rcFileModel.getRcFilename());
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
    public int saveAs() {
        int error = 0;
        final JFileChooser fileChooser = new JFileChooser(getJfwknopWorkingDirectory());
        fileChooser.setDialogTitle(InternationalizationHelper.getMessage("i18n.save.as"));
        final int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            final String filename = fileChooser.getSelectedFile().getAbsolutePath();
            this.rcFileModel.saveAs(convertViewToConfig(this.view.getVariables()),
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
                    LOGGER.error("Unable to load rc file : " + e.getActionCommand());
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

    public Map<EnumFwknopRcKey, String> convertViewToConfig(final Map<EnumFwknopRcKey, IFwknopVariable> viewVariables) {
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
