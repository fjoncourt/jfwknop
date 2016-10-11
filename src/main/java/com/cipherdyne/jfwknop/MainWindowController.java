/* 
 * Copyright (C) 2016 Franck Joncourt <franck.joncourt@gmail.com>
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
import com.cipherdyne.utils.InternationalizationHelper;
import com.cipherdyne.gui.components.JFwknopComboBox;
import com.cipherdyne.model.RcFileModel;
import com.cipherdyne.model.FwknopClientModel;
import com.cipherdyne.gui.gpg.GpgController;
import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.gui.ip.IpController;
import com.cipherdyne.gui.ssh.SshController;
import com.cipherdyne.gui.wizard.WizardController;
import com.cipherdyne.model.KeyModel;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;

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

        populateMenuBar();
        populateBtn();

        this.view.display();

    }

    /**
     * @return the key model that contains key settings
     */
    public KeyModel getKeyModel() {
        return this.keyModel;
    }
    
    /**
     * Set up action listeners for buttons from the general tab
     */
    private void populateGeneralTabButton() {
        
        // Add action listener to browse for an IP in order to configure the ALLOW_IP field
        this.view.getButton(EnumButton.GENERAL_BROWSE_FOR_IP).addActionListener((ActionEvent e) -> {
            javax.swing.SwingUtilities.invokeLater(() -> new IpController(MainWindowController.this.view,
                EnumFwknopRcKey.ALLOW_IP));
        });
    }

    /**
     * Set up action listeners for buttons from the cipher tab
     */
    private void populateCipherTabButton() {
        
        // Add action listener to generate/remove rijndael key
        this.view.getButton(EnumButton.CIPHER_GENERATE_RIJNDAEL_KEY).addActionListener((ActionEvent e) -> {
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.KEY).setText(
                MainWindowController.this.keyModel.getRandomRijndaelKey());
        });

        this.view.getButton(EnumButton.CIPHER_REMOVE_RIJNDAEL_KEY).addActionListener((ActionEvent e) -> {
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.KEY).setDefaultValue();
        });

        // Add action listener to generate/remove rijndael base64 key
        this.view.getButton(EnumButton.CIPHER_GENERATE_BASE64_RIJNDAEL_KEY).addActionListener((ActionEvent e) -> {
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.KEY_BASE64).setText(
                MainWindowController.this.keyModel.getRandomBase64Rijndael());
        });

        this.view.getButton(EnumButton.CIPHER_REMOVE_BASE64_RIJNDAEL_KEY).addActionListener((ActionEvent e) -> {
            ((IFwknopVariable) MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.KEY_BASE64)).setDefaultValue();
        });

        // Add action listener to generate/remove rijndael key
        this.view.getButton(EnumButton.CIPHER_GENERATE_HMAC_KEY).addActionListener((ActionEvent e) -> {
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.HMAC_KEY).setText(
                MainWindowController.this.keyModel.getRandomHmacKey());
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("Y");
        });

        this.view.getButton(EnumButton.CIPHER_REMOVE_HMAC_KEY).addActionListener((ActionEvent e) -> {
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.HMAC_KEY).setDefaultValue();
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("N");
        });

        // Add action listener to generate/remove HMAC base64 key
        this.view.getButton(EnumButton.CIPHER_GENERATE_BASE64_HMAC_KEY).addActionListener((ActionEvent e) -> {
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.HMAC_KEY_BASE64).setText(
                MainWindowController.this.keyModel.getRandomBase64Hmac());
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("Y");
        });

        this.view.getButton(EnumButton.CIPHER_REMOVE_BASE64_HMAC_KEY).addActionListener((ActionEvent e) -> {
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.HMAC_KEY_BASE64).setDefaultValue();
            MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("N");
        });

        // Add action listener to select a GPG key and its home directory
        this.view.getButton(EnumButton.CIPHER_SELECT_RECIPIENT_GPG_ID).addActionListener((ActionEvent e) -> {
            javax.swing.SwingUtilities.invokeLater(() -> new GpgController(MainWindowController.this.view,
                EnumFwknopRcKey.GPG_RECIPIENT,
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.GPG_HOMEDIR).getText()));
        });

        this.view.getButton(EnumButton.CIPHER_SELECT_SIGNER_GPG_ID).addActionListener((ActionEvent e) -> {
            javax.swing.SwingUtilities.invokeLater(() -> new GpgController(MainWindowController.this.view,
                EnumFwknopRcKey.GPG_SIGNER,
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.GPG_HOMEDIR).getText()));
        });

        this.view.getButton(EnumButton.CIPHER_BROWSE_GPG_HOMEDIR).addActionListener((ActionEvent e) -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileHidingEnabled(false);
            fileChooser.setDialogTitle(InternationalizationHelper.getMessage("i18n.browse"));
            final int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.GPG_HOMEDIR).setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        this.view.getButton(EnumButton.CIPHER_GENERATE_BASE64_GPG).addActionListener((ActionEvent e) -> {
            IFwknopVariable gpgSigningPw = (IFwknopVariable) MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.GPG_SIGNING_PW);
            if (gpgSigningPw.isDefault()) {
                JOptionPane.showMessageDialog(MainWindowController.this.view,
                    InternationalizationHelper.getMessage("i18n.please.fill.in.passphrase"),
                    InternationalizationHelper.getMessage("i18n.error"),
                    JOptionPane.ERROR_MESSAGE);
            } else {
                IFwknopVariable gpgSigningPwBase64 = (IFwknopVariable) MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.GPG_SIGNING_PW_BASE64);
                gpgSigningPwBase64.setText(KeyModel.encodeToBase64(gpgSigningPw.getText().getBytes()));
            }
        });
    }

    /**
     * Set up action listeners for all buttons in the view
     */
    private void populateBtn() {

        // Add action listener to browse for another fwknop file path
        this.view.getBtnBrowseforFwknop().addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Browse");
            final int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                MainWindowController.this.view.setFwknopFilePath(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Add action listener to run fwknop binary
        this.view.getBtnExecute().addActionListener(e -> {
            long period = 0;
            boolean stopEnabled = false;

            // Try to save the current settings before executing the fwknop client
            if (save() == 0) {
                updateFwknopModel();

                if (MainWindowController.this.view.getPeriodicExecution().isSelected()) {
                    period = Long.parseLong(MainWindowController.this.view.getFwknopPeriod().getText());
                    stopEnabled = true;
                }

                MainWindowController.this.view.getBtnStop().setEnabled(stopEnabled);
                MainWindowController.this.fwknopClientModel.start(period);
            }
        });

        // Add action listener to clear the console
        this.view.getBtnClearConsole().addActionListener(e -> {
            MainWindowController.this.view.clearConsole();
        });

        // Add action listner to enable/disable verbose mode
        this.view.getBtnFwknopVerbose().addActionListener((ActionEvent e) -> {
            MainWindowController.this.view.getVarFwknopArgs().setVerbose(((JCheckBox) e.getSource()).isSelected());
        });

        // Add action listener to enable/disable test mode
        this.view.getBtnFwknopTest().addActionListener((ActionEvent e) -> {
            MainWindowController.this.view.getVarFwknopArgs().setTest(((JCheckBox) e.getSource()).isSelected());
        });

        // Add action listener to save fwknop settings
        this.view.getBtnSaveFwknopSettings().addActionListener((ActionEvent e) -> {
            save();
            updateFwknopModel();
            MainWindowController.this.fwknopClientModel.save();
        });

        populateGeneralTabButton();
        populateCipherTabButton();

        // Add action listener to save key settings
        this.view.getBtnSaveKeySettings().addActionListener((ActionEvent e) -> {
            updateKeyModel();
            MainWindowController.this.keyModel.save();
        });

        this.view.getCbConfigList().addActionListener((ActionEvent e) -> {
            String filename = ((JFwknopComboBox) e.getSource()).getText();
            if (filename != null) {
                MainWindowController.this.rcFileModel.loadRcFile(filename);
                updateNewRcFile(filename);
            }
        });

        this.view.getBtnStop().addActionListener((ActionEvent e) -> {
            MainWindowController.this.fwknopClientModel.stop();
            MainWindowController.this.view.getBtnStop().setEnabled(false);
        });
    }

    /**
     * Update the fwknop client model with the settings set by the user in the user interface
     */
    private void updateFwknopModel() {
        MainWindowController.this.fwknopClientModel.setFwknopConfig(EnumFwknopConfigKey.FWKNOP_FILEPATH,
            MainWindowController.this.view.getVarFwknopFilePath().getText());
        MainWindowController.this.fwknopClientModel.setFwknopConfig(EnumFwknopConfigKey.FWKNOP_ARGS,
            MainWindowController.this.view.getVarFwknopArgs().getText());
        MainWindowController.this.fwknopClientModel.setFwknopConfig(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS,
            MainWindowController.this.view.getVarFwknopExtraArgs().getText());
        MainWindowController.this.fwknopClientModel.setFwknopConfig(EnumFwknopConfigKey.FWKNOP_VERBOSE,
            MainWindowController.this.view.getBtnFwknopVerbose().isSelected() ? "1" : "0");
    }

    /**
     * Update the key model with the settings set by the user in the user interface
     */
    private void updateKeyModel() {
        MainWindowController.this.keyModel.setContext(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH,
            MainWindowController.this.view.getVarKeyRijndaelLength().getText());
        MainWindowController.this.keyModel.setContext(EnumFwknopConfigKey.KEY_HMAC_LENGTH,
            MainWindowController.this.view.getVarKeyHmacLength().getText());
        MainWindowController.this.keyModel.setContext(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH,
            MainWindowController.this.view.getVarBase64RijndaelBytes().getText());
        MainWindowController.this.keyModel.setContext(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH,
            MainWindowController.this.view.getVarBase64HmacBytes().getText());
    }

    private void populateMenuBar() {

        // Set up action listener when opening a new configuration file
        this.view.getOpenMenuItem().addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Browse");
            fileChooser.setFileHidingEnabled(false);
            final int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                final String filename = fileChooser.getSelectedFile().getAbsolutePath();
                this.rcFileModel.loadRcFile(filename);
                updateNewRcFile(filename);
                updateConfigurationList();
            }
        });

        // Set up action listener when quitting the application
        this.view.getExitMenuItem().addActionListener(e -> System.exit(0));

        // Set up action listener when saving a file
        this.view.getSaveMenuItem().addActionListener(e -> {
            if (MainWindowController.this.rcFileModel.exists()) {
                MainWindowController.this.rcFileModel.saveRcFile(convertViewToConfig(this.view.getVariables()));
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
    private int save() {
        int error = 0;
        if (MainWindowController.this.rcFileModel.exists()) {
            MainWindowController.this.rcFileModel.saveRcFile(convertViewToConfig(this.view.getVariables()));
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
            MainWindowController.this.rcFileModel.saveAsRcFile(convertViewToConfig(this.view.getVariables()),
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
                MainWindowController.this.rcFileModel.loadRcFile(e.getActionCommand());
                updateNewRcFile(e.getActionCommand());
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

    private void updateNewRcFile(String rcFilename) {
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
}
