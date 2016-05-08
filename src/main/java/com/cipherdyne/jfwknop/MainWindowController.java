package com.cipherdyne.jfwknop;

import com.cipherdyne.gui.MainWindowView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JCheckBox;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainWindowController {

    static final Logger logger = LogManager.getLogger(MainWindowController.class.getName());

    private final MainWindowView view;
    private final RcFileModel rcFileModel;
    private final FwknopClientModel fwknopClientModel;
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

        populateMenuBar();
        populateBtn();

        this.view.display();

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

        // Add action listener to execute fwknop binary
        this.view.getBtnExecute().addActionListener(e -> {
            updateFwknopModel();
            MainWindowController.this.fwknopClientModel.execute();
            //ExternalCommand ext = new ExternalCommand("/usr/bin/x-terminal-emulator -e \"notify-send -t 5000 test\"");
            //ExternalCommand ext = new ExternalCommand("/usr/bin/x-terminal-emulator_-x_'ssh franck@127.0.0.1'");
            //ExternalCommand ext = new ExternalCommand("ssh -T -t franck@127.0.0.1");
            //ExternalCommand ext = new ExternalCommand("/usr/bin/iceweasel");
            //ext.execute();
        });

        // Add action listener to clear the console
        this.view.getBtnClearConsole().addActionListener(e -> {
            MainWindowController.this.view.clearConsole();
        });

        // Add action listner to enable/disable verbose mode
        this.view.getBtnFwknopVerbose().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowController.this.view.getVarFwknopArgs().setVerbose(((JCheckBox) e.getSource()).isSelected());
            }
        });

        // Add action listener to enable/disable test mode
        this.view.getBtnFwknopTest().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowController.this.view.getVarFwknopArgs().setTest(((JCheckBox) e.getSource()).isSelected());
            }
        });

        // Add action listener to save fwknop settings
        this.view.getBtnSaveFwknopSettings().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFwknopModel();
                MainWindowController.this.fwknopClientModel.save();
            }
        });

        // Add action listener to generate/remove rijndael key
        this.view.getBtnGenerateRijndaelKey().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.KEY).setText(RandomStringUtils.randomAlphabetic(16));
            }
        });

        this.view.getBtnRemoveRijndaelKey().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.KEY).setDefaultValue();
            }
        });

        // Add action listener to generate/remove rijndael base64 key
        this.view.getBtnGenerateBase64Rijndael().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                byte[] byteArray = new byte[32];
                new Random().nextBytes(byteArray);
                IFwknopVariable keybase64 = (IFwknopVariable) MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.KEY_BASE64);
                keybase64.setText(computeBase64(byteArray));

            }
        });

        this.view.getBtnRemoveBase64Rijndael().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((IFwknopVariable) MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.KEY_BASE64)).setDefaultValue();
            }
        });
        
        // Add action listener to generate/remove rijndael key
        this.view.getBtnGenerateHmacKey().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.HMAC_KEY).setText(RandomStringUtils.randomAlphabetic(16));
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("Y");
            }
        });

        this.view.getBtnRemoveHmacKey().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.HMAC_KEY).setDefaultValue();
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("N");                
            }
        });
        
        // Add action listener to generate/remove HMAC base64 key
        this.view.getBtnGenerateBase64Hmac().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                byte[] byteArray = new byte[64];
                new Random().nextBytes(byteArray);
                IFwknopVariable keybase64 = (IFwknopVariable) MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.HMAC_KEY_BASE64);
                keybase64.setText(computeBase64(byteArray));
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("Y");
            }
        });

        this.view.getBtnRemoveBase64Hmac().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.HMAC_KEY_BASE64).setDefaultValue();
                MainWindowController.this.view.getVariables().get(EnumFwknopRcKey.USE_HMAC).setText("N");
            }
        });
    }

    /**
     * Update the fwknop client model with the settings set by the user in the
     * user interface
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

        populateRecentFiles();

        // Set up action listener to open a terminal
        this.view.getOpenTerminalMenuItem().addActionListener(e -> {
            ExternalCommand extCmd = new ExternalCommand("/usr/bin/x-terminal-emulator");
            extCmd.execute();
        });
    }

    /**
     * Open browser to allow user to select the filename to save the current
     * configuration to
     */
    private void saveAs() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as");
        final int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            final String filename = fileChooser.getSelectedFile().getAbsolutePath();
            MainWindowController.this.rcFileModel.saveAsRcFile(convertViewToConfig(this.view.getVariables()),
                    filename);
            updateNewRcFile(filename);
        }
    }

    /**
     * Populate recent file list from the file menu.
     *
     * This function adds action listener to each entry in the recent file list.
     * When selected, the file is loaded.
     */
    private void populateRecentFiles() {
        for (final JMenuItem miFilename : this.view.getVarRecentRcFiles()) {
            miFilename.addActionListener(e -> {
                MainWindowController.this.rcFileModel.loadRcFile(e.getActionCommand());
                updateNewRcFile(e.getActionCommand());
            });

        }
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

    private String computeBase64(byte[] key) {
        String base64Str = "failed";

        base64Str = Base64.getEncoder().encodeToString(key);
        //String decodedString = new String(Base64.getDecoder().decode(base64String), "utf-8");
        //System.out.println(unencodedString + " ==> " + base64String + " ==> " + decodedString);

        return base64Str;
    }
}
