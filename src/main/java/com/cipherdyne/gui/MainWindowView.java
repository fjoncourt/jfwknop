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
package com.cipherdyne.gui;

import com.cipherdyne.jfwknop.EnumFwknopConfigKey;
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.IFwknopVariable;
import com.cipherdyne.utils.InternationalizationHelper;
import com.cipherdyne.gui.components.JFwknopArgs;
import com.cipherdyne.gui.components.JFwknopCheckBox;
import com.cipherdyne.gui.components.JFwknopComboBox;
import com.cipherdyne.gui.components.JFwknopTextField;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

public class MainWindowView extends JFrame implements IConsole {

    private static final long serialVersionUID = 7003086413693874319L;
    private static final String JFWKNOP_TITLE = "JFwknop - ";

    private JMenuItem exitMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem openMenuItem;
    private JMenu recentsMenu;
    private JMenuItem openTerminalMenuItem;
    private JMenuItem openRcFileMenuItem;
    private JMenuItem exportFileMenuItem;
    private JMenuItem generateAccessMenuItem;
    private JMenuItem easySetupMenuItem;
    private JMenuItem aboutMenuItem;

    private final Map<EnumFwknopRcKey, IFwknopVariable> varMap;
    private final List<JMenuItem> varRecentRcFiles;

    private final SettingsTab settingsTab;
    private final CipherTab cipherTab;

    private final ConsolePanel consolePanel;

    public MainWindowView(final String title) {
        super(JFWKNOP_TITLE + title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("inset 0, gap 0, flowy", "[grow]", "[][fill]"));
        setIconImage(new ImageIcon(this.getClass().getResource("/cipherdyne.png")).getImage());

        varMap = new HashMap<>();
        varRecentRcFiles = new ArrayList<>();

        createMenuBar();

        final JTabbedPane mainPane = new JTabbedPane(JTabbedPane.TOP);
        this.add(mainPane, "growx, height 366!");

        final JTabbedPane rcConfigPane = new JTabbedPane(JTabbedPane.TOP);
        rcConfigPane.addTab("General", null, new GeneralTab(varMap), "General");
        rcConfigPane.addTab("Network", null, new NetworkTab(varMap), "Network");
        this.cipherTab = new CipherTab(varMap);
        rcConfigPane.addTab("Cipher", null, this.cipherTab, "Cipher");
        mainPane.addTab("Configuration", null, rcConfigPane, "Configuration");
        this.settingsTab = new SettingsTab(varMap);

        mainPane.addTab("Settings", null, this.settingsTab, "Settings");

        this.consolePanel = new ConsolePanel(varMap);
        this.add(this.consolePanel, "grow");

        pack();
    }

    /**
     * Build the recent files menu from the recent file list. For each entry an index provides an
     * easy way to know which file is the most recent (1). Also a shortcut (Ctrl + R) is available
     * to quickly load the most recent file used.
     *
     * @param recentFiles List of files recently used
     */
    public void buildRecentFilesList(final LinkedList<String> recentFiles) {
        int pos = 1;
        this.recentsMenu.removeAll();
        for (final String file : recentFiles) {
            final JMenuItem mi = new JMenuItem(pos + ": " + file);

            // Add a shortcut to easily reload the last file
            if (pos == 1) {
                mi.setAccelerator(KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            }

            mi.setActionCommand(file);
            this.recentsMenu.add(mi);
            this.varRecentRcFiles.add(mi);
            pos++;
        }
        repaint();
    }

    private void createMenuBar() {

        final JMenuBar menubar = new JMenuBar();

        menubar.add(createFileMenu());
        menubar.add(createToolMenu());
        menubar.add(createWizardMenu());
        menubar.add(createHelpMenu());

        setJMenuBar(menubar);
    }

    private JMenu createToolMenu() {
        final JMenu menu = new JMenu(InternationalizationHelper.getMessage("window.menu.tools"));

        this.openTerminalMenuItem = new JMenuItem(InternationalizationHelper.getMessage("window.menu.tools.openterminal"),
            new ImageIcon(this.getClass().getResource("/openterminal16.png")));
        menu.add(this.openTerminalMenuItem);

        this.openRcFileMenuItem = new JMenuItem(InternationalizationHelper.getMessage("window.menu.tools.openrcfile"),
            new ImageIcon(this.getClass().getResource("/edit16.png")));
        menu.add(this.openRcFileMenuItem);

        this.generateAccessMenuItem = new JMenuItem(InternationalizationHelper.getMessage("i18n.window.menu.tools.generate.access"));
        menu.add(this.generateAccessMenuItem);

        this.exportFileMenuItem = new JMenuItem(InternationalizationHelper.getMessage("window.menu.tools.exportfile"),
            new ImageIcon(this.getClass().getResource("/export16.png")));
        menu.add(this.exportFileMenuItem);

        return menu;
    }

    private JMenu createWizardMenu() {
        final JMenu menu = new JMenu(InternationalizationHelper.getMessage("i18n.window.menu.wizard"));
        menu.setMnemonic(KeyEvent.VK_W);

        this.easySetupMenuItem = new JMenuItem(InternationalizationHelper.getMessage("i18n.window.menu.wizard.easysetup"),
            new ImageIcon(this.getClass().getResource("/wizard16.png")));
        menu.add(this.easySetupMenuItem);

        return menu;
    }    
    
    private JMenu createHelpMenu() {
        final JMenu menu = new JMenu(InternationalizationHelper.getMessage("window.menu.help"));
        menu.setMnemonic(KeyEvent.VK_H);

        this.aboutMenuItem = new JMenuItem(InternationalizationHelper.getMessage("window.menu.help.about"));
        menu.add(this.aboutMenuItem);

        return menu;
    }

    private JMenu createFileMenu() {
        final JMenu menu = new JMenu(InternationalizationHelper.getMessage("window.menu.file"));
        menu.setMnemonic(KeyEvent.VK_F);

        this.openMenuItem = new JMenuItem(InternationalizationHelper.getMessage("window.menu.file.openfile"),
            new ImageIcon(this.getClass().getResource("/open16.png")));
        menu.add(this.openMenuItem);

        this.saveMenuItem = new JMenuItem(InternationalizationHelper.getMessage("window.menu.file.savefile"),
            new ImageIcon(this.getClass().getResource("/save16.png")));
        this.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        menu.add(this.saveMenuItem);

        this.saveAsMenuItem = new JMenuItem(InternationalizationHelper.getMessage("window.menu.file.saveasfile"),
            new ImageIcon(this.getClass().getResource("/saveas16.png")));
        menu.add(this.saveAsMenuItem);

        menu.addSeparator();
        this.recentsMenu = new JMenu("Recent files");
        menu.add(this.recentsMenu);

        menu.addSeparator();
        this.exitMenuItem = new JMenuItem(InternationalizationHelper.getMessage("window.menu.file.exitapplication"));
        this.exitMenuItem.setMnemonic(KeyEvent.VK_E);
        menu.add(this.exitMenuItem);

        return menu;
    }

    public void display() {
        setVisible(true);
    }

    public void onRcFileChange(final Map<EnumFwknopRcKey, String> rcFileContext) {

        this.varMap.entrySet().stream().map((entry) -> entry.getKey()).forEach((crtKey) -> {
            final IFwknopVariable component = this.varMap.get(crtKey);
            final String newVal = rcFileContext.get(crtKey);
            if (component != null) {
                if (newVal != null) {
                    component.setText(newVal);
                } else {
                    component.setDefaultValue();
                }
            }
        });

        repaint();
    }

    public void onFwknopConfigChange(final Map<EnumFwknopConfigKey, String> fwknopContext) {
        this.settingsTab.varFwknopFilePath.setText(fwknopContext.get(EnumFwknopConfigKey.FWKNOP_FILEPATH));
        this.settingsTab.varFwknopArgs.setText(fwknopContext.get(EnumFwknopConfigKey.FWKNOP_ARGS));
        this.settingsTab.varFwknopExtraArgs.setText(fwknopContext.get(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS));

        // If the verbosity is set in the fwknop context, then we select the verbose check box and ensure
        // the fwknop arguement are updated accordingly
        if ("1".equals(fwknopContext.get(EnumFwknopConfigKey.FWKNOP_VERBOSE))) {
            this.settingsTab.btnFwknopVerbose.setSelected(true);
            this.settingsTab.varFwknopArgs.setVerbose(true);
        }

        repaint();
    }

    /**
     * Refresh the view when the key model is updated
     *
     * @param keyContext key context to use to update the view
     */
    public void onKeyContextChange(Map<EnumFwknopConfigKey, String> keyContext) {
        this.settingsTab.varRijndaelKeyLength.setText(keyContext.get(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH));
        this.settingsTab.varHmacKeyLength.setText(keyContext.get(EnumFwknopConfigKey.KEY_HMAC_LENGTH));
        this.settingsTab.varBase64RijndaelBytes.setText(keyContext.get(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH));
        this.settingsTab.varBase64HmacBytes.setText(keyContext.get(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH));

        repaint();
    }

    public Map<EnumFwknopRcKey, IFwknopVariable> getVariables() {
        return this.varMap;
    }

    public void setFwknopFilePath(final String absolutePath) {
        this.settingsTab.varFwknopFilePath.setText(absolutePath);
        repaint();
    }

    public JButton getBtnExecute() {
        return this.consolePanel.btnExecute;
    }

    public JButton getBtnBrowseforFwknop() {
        return this.settingsTab.btnBrowseforFwknop;
    }

    public JFwknopTextField getVarFwknopFilePath() {
        return this.settingsTab.varFwknopFilePath;
    }

    public JFwknopArgs getVarFwknopArgs() {
        return this.settingsTab.varFwknopArgs;
    }

    public JFwknopTextField getVarFwknopExtraArgs() {
        return this.settingsTab.varFwknopExtraArgs;
    }

    public JMenuItem getOpenMenuItem() {
        return this.openMenuItem;
    }

    public JMenuItem getExitMenuItem() {
        return this.exitMenuItem;
    }

    public JMenuItem getSaveMenuItem() {
        return this.saveMenuItem;
    }

    public JMenuItem getSaveAsMenuItem() {
        return this.saveAsMenuItem;
    }

    public JMenuItem getEasySetupMenuItem() {
        return this.easySetupMenuItem;
    }
    
    public List<JMenuItem> getVarRecentRcFiles() {
        return this.varRecentRcFiles;
    }

    /**
     * @return the menu item used to open a terminal
     */
    public JMenuItem getOpenTerminalMenuItem() {
        return this.openTerminalMenuItem;
    }

    /**
     * @return the menu item used to open the current rc file
     */
    public JMenuItem getOpenRcFileMenuItem() {
        return this.openRcFileMenuItem;
    }

    /**
     * @return the menu item used to send file through sssh copy
     */
    public JMenuItem getExportFileMenuItem() {
        return this.exportFileMenuItem;
    }

    /**
     * @return the menu item used to generate access.conf file
     */
    public JMenuItem getGenerateAccessMenuItem() {
        return this.generateAccessMenuItem;
    }
    
    @Override
    public void appendToConsole(String msg) {
        SwingUtilities.invokeLater(() -> {
            MainWindowView.this.consolePanel.varConsole.append(msg + "\n");
        });
    }

    /**
     * Clear the console area
     */
    public void clearConsole() {
        this.consolePanel.varConsole.setText(null);
    }

    /**
     * @return the clear button console
     */
    public JButton getBtnClearConsole() {
        return this.consolePanel.btnClearConsole;
    }

    public JCheckBox getBtnFwknopVerbose() {
        return this.settingsTab.btnFwknopVerbose;
    }

    /**
     * @return the button used to save fwknop client settings
     */
    public JButton getBtnSaveFwknopSettings() {
        return this.settingsTab.btnSaveFwknopSettings;
    }

    /**
     * @return the button used to save key settings
     */
    public JButton getBtnSaveKeySettings() {
        return this.settingsTab.btnSaveKeySettings;
    }

    public JCheckBox getBtnFwknopTest() {
        return this.settingsTab.btnFwknopTest;
    }

    /**
     * @return the default rijndael key length
     */
    public JFwknopTextField getVarKeyRijndaelLength() {
        return this.settingsTab.varRijndaelKeyLength;
    }

    /**
     * @return the default hmac key length
     */
    public JFwknopTextField getVarKeyHmacLength() {
        return this.settingsTab.varHmacKeyLength;
    }

    /**
     * @return the base64 hmac byte array length
     */
    public JFwknopTextField getVarBase64HmacBytes() {
        return this.settingsTab.varBase64HmacBytes;
    }

    /**
     * @return the base64 rijndael byte array length
     */
    public JFwknopTextField getVarBase64RijndaelBytes() {
        return this.settingsTab.varBase64RijndaelBytes;
    }

    /**
     * @return the button used to remove Rijndael key
     */
    public JButton getBtnRemoveRijndaelKey() {
        return this.cipherTab.removeRijndaelKey;
    }

    /**
     * @return the button used to remove the base64 rijndael key
     */
    public JButton getBtnRemoveBase64Rijndael() {
        return this.cipherTab.removeBase64RijndaelKey;
    }

    /**
     * @return the button used to generate random Rijndael key
     */
    public JButton getBtnGenerateRijndaelKey() {
        return this.cipherTab.generateRijndaelKey;
    }

    /**
     * @return the button used to generate random base64 rijndael key
     */
    public JButton getBtnGenerateBase64Rijndael() {
        return this.cipherTab.generateBase64RijndaelKey;
    }

    /**
     * @return the button used to generate random HMAC key
     */
    public JButton getBtnGenerateHmacKey() {
        return this.cipherTab.generateHmacKey;
    }

    /**
     * @return the button used to remove HMAC key
     */
    public JButton getBtnRemoveHmacKey() {
        return this.cipherTab.removeHmacKey;
    }

    /**
     * @return the button used to remove HMAC base64 key
     */
    public JButton getBtnRemoveBase64Hmac() {
        return this.cipherTab.removeBase64HmacKey;
    }

    /**
     * @return the button used to generate radom base64 HMAC key
     */
    public JButton getBtnGenerateBase64Hmac() {
        return this.cipherTab.generateBase64HmacKey;
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(JFWKNOP_TITLE + title);
    }

    /**
     * @return the button used to select the recipient GPG id
     */
    public JButton getBtnRecipientGpgId() {
        return this.cipherTab.selectRecipientGpgId;
    }

    /**
     * @return the button used to select the signer GPG id
     */
    public JButton getBtnSignerGpgId() {
        return this.cipherTab.selectSignerGpgId;
    }

    /**
     * @return the button used to browse for the GPG home directory
     */
    public JButton getBtnGpgHomedir() {
        return this.cipherTab.browseforGpgHomedir;
    }

    /**
     * @return the button used to encode the GPG passphrase to base64
     */
    public JButton getBtnEncodeGpgPassphrase() {
        return this.cipherTab.btnGenerateBase64GpgPassphrase;
    }

    /**
     * Update the configuration list. Firstly the list is cleared and then all new items are added
     *
     * @param configs list of configuration to
     */
    public void setCbConfigList(String[] configs) {
        this.consolePanel.cbConfigList.removeAllItems();
        for (String config : configs) {
            this.consolePanel.cbConfigList.addItem(config);
        }
    }

    public JFwknopComboBox getCbConfigList() {
        return this.consolePanel.cbConfigList;
    }

    public JFwknopCheckBox getPeriodicExecution() {
        return this.consolePanel.periodicExecution;
    }

    public JButton getBtnStop() {
        return this.consolePanel.btnStop;
    }

    public JFwknopTextField getFwknopPeriod() {
        return this.consolePanel.varPeriod;
    }
}
