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
package com.cipherdyne.gui;

import com.cipherdyne.gui.components.JFwknopArgs;
import com.cipherdyne.gui.components.JFwknopCheckBox;
import com.cipherdyne.gui.components.JFwknopComboBox;
import com.cipherdyne.gui.components.JFwknopTextField;
import com.cipherdyne.jfwknop.EnumFwknopConfigKey;
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.gui.components.IFwknopVariable;
import com.cipherdyne.utils.InternationalizationHelper;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.util.Date;
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

public class MainWindowView extends DefaultFrame implements IConsole {

    private static final long serialVersionUID = 7003086413693874319L;
    private static final String JFWKNOP_TITLE = "JFwknop - ";
    private static final String DEFAULT_CONFIGURATION = "Default configuration";

    private JMenu recentsMenu;

    private final SettingsTab settingsTab;

    private final ConsolePanel consolePanel;

    public MainWindowView() {
        super(JFWKNOP_TITLE + DEFAULT_CONFIGURATION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new MigLayout("inset 0, gap 0, flowy", "[grow]", "[][fill]"));
        setIconImage(new ImageIcon(this.getClass().getResource("/cipherdyne.png")).getImage());

        createMenuBar();

        final JTabbedPane mainPane = new JTabbedPane(JTabbedPane.TOP);
        this.add(mainPane, "growx, height 366!");

        final JTabbedPane rcConfigPane = new JTabbedPane(JTabbedPane.TOP);
        rcConfigPane.addTab("General", null, new GeneralTab(varMap, btnMap), "General");
        rcConfigPane.addTab("Network", null, new NetworkTab(varMap, btnMap), "Network");
        rcConfigPane.addTab("Cipher", null, new CipherTab(varMap, btnMap), "Cipher");
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

    /**
     * Build the tools menu
     *
     * @return the menu
     */
    private JMenu createToolMenu() {

        // Create menu
        final JMenu menu = new JMenu(InternationalizationHelper.getMessage("window.menu.tools"));

        // Create all items
        this.menuItemMap.put(EnumMenuItem.TOOLS_OPENTERMINAL, new JMenuItem(InternationalizationHelper.getMessage("window.menu.tools.openterminal"),
            new ImageIcon(this.getClass().getResource("/openterminal16.png"))));

        this.menuItemMap.put(EnumMenuItem.TOOLS_OPENRCFILE, new JMenuItem(InternationalizationHelper.getMessage("window.menu.tools.openrcfile"),
            new ImageIcon(this.getClass().getResource("/edit16.png"))));

        this.menuItemMap.put(EnumMenuItem.TOOLS_GENERATE_ACCESS, new JMenuItem(InternationalizationHelper.getMessage("i18n.window.menu.tools.generate.access")));

        this.menuItemMap.put(EnumMenuItem.TOOLS_EXPORT_FILE, new JMenuItem(InternationalizationHelper.getMessage("window.menu.tools.exportfile"),
            new ImageIcon(this.getClass().getResource("/export16.png"))));

        // Add items to menu
        menu.add(this.menuItemMap.get(EnumMenuItem.TOOLS_OPENTERMINAL));
        menu.add(this.menuItemMap.get(EnumMenuItem.TOOLS_OPENRCFILE));
        menu.add(this.menuItemMap.get(EnumMenuItem.TOOLS_GENERATE_ACCESS));
        menu.add(this.menuItemMap.get(EnumMenuItem.TOOLS_EXPORT_FILE));

        return menu;
    }

    private JMenu createWizardMenu() {

        // Create menu
        final JMenu menu = new JMenu(InternationalizationHelper.getMessage("i18n.window.menu.wizard"));
        menu.setMnemonic(KeyEvent.VK_W);

        // Create all items
        this.menuItemMap.put(EnumMenuItem.WIZARD_EASYSETUP, new JMenuItem(InternationalizationHelper.getMessage("i18n.window.menu.wizard.easysetup"),
            new ImageIcon(this.getClass().getResource("/wizard16.png"))));

        // Add items to menu
        menu.add(this.menuItemMap.get(EnumMenuItem.WIZARD_EASYSETUP));

        return menu;
    }

    private JMenu createHelpMenu() {

        // Create menu
        final JMenu menu = new JMenu(InternationalizationHelper.getMessage("window.menu.help"));
        menu.setMnemonic(KeyEvent.VK_H);

        // Create all items
        this.menuItemMap.put(EnumMenuItem.HELP_ABOUT, new JMenuItem(InternationalizationHelper.getMessage("window.menu.help.about")));

        // Add items to menu
        menu.add(this.menuItemMap.get(EnumMenuItem.HELP_ABOUT));

        return menu;
    }

    /**
     * Build the file menu
     *
     * @return the menu
     */
    private JMenu createFileMenu() {

        // Create the menu
        final JMenu menu = new JMenu(InternationalizationHelper.getMessage("window.menu.file"));
        menu.setMnemonic(KeyEvent.VK_F);

        // Create all items
        this.menuItemMap.put(EnumMenuItem.FILE_NEW, new JMenuItem(InternationalizationHelper.getMessage("window.menu.file.new"),
            new ImageIcon(this.getClass().getResource("/new16.png"))));

        this.menuItemMap.put(EnumMenuItem.FILE_OPEN, new JMenuItem(InternationalizationHelper.getMessage("window.menu.file.openfile"),
            new ImageIcon(this.getClass().getResource("/open16.png"))));

        this.menuItemMap.put(EnumMenuItem.FILE_SAVE, new JMenuItem(InternationalizationHelper.getMessage("window.menu.file.savefile"),
            new ImageIcon(this.getClass().getResource("/save16.png"))));

        this.menuItemMap.put(EnumMenuItem.FILE_SAVEAS, new JMenuItem(InternationalizationHelper.getMessage("window.menu.file.saveasfile"),
            new ImageIcon(this.getClass().getResource("/saveas16.png"))));

        this.menuItemMap.put(EnumMenuItem.FILE_EXIT, new JMenuItem(InternationalizationHelper.getMessage("window.menu.file.exitapplication")));

        // Set mnemonics and shortcuts
        this.menuItemMap.get(EnumMenuItem.FILE_NEW).setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.menuItemMap.get(EnumMenuItem.FILE_SAVE).setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        this.menuItemMap.get(EnumMenuItem.FILE_EXIT).setMnemonic(KeyEvent.VK_E);

        // Add items to menu
        menu.add(this.menuItemMap.get(EnumMenuItem.FILE_NEW));
        menu.add(this.menuItemMap.get(EnumMenuItem.FILE_OPEN));
        menu.add(this.menuItemMap.get(EnumMenuItem.FILE_SAVE));
        menu.add(this.menuItemMap.get(EnumMenuItem.FILE_SAVEAS));
        menu.addSeparator();
        this.recentsMenu = new JMenu("Recent files");
        menu.add(this.recentsMenu);
        menu.addSeparator();
        menu.add(this.menuItemMap.get(EnumMenuItem.FILE_EXIT));

        return menu;
    }

    public void display() {
        setVisible(true);
    }

    public void onRcFileChange(final Map<EnumFwknopRcKey, String> rcFileContext) {

        if (rcFileContext == null) {
            this.setTitle(DEFAULT_CONFIGURATION);
        }

        this.varMap.entrySet().stream().map((entry) -> entry.getKey()).forEach((crtKey) -> {
            final IFwknopVariable component = this.varMap.get(crtKey);
            if (component != null) {
                // Context is not empty - set value
                if (rcFileContext != null) {
                    final String newVal = rcFileContext.get(crtKey);
                    if (newVal != null) {
                        component.setText(newVal);
                    } else {
                        component.setDefaultValue();
                    }
                } // Context is empty - set default value for everything
                else {
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

    public JMenuItem getMenuItem(EnumMenuItem item) {
        return this.menuItemMap.get(item);
    }

    public List<JMenuItem> getVarRecentRcFiles() {
        return this.varRecentRcFiles;
    }

    @Override
    public void appendToConsole(String msg) {
        SwingUtilities.invokeLater(() -> {
            String now = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date());
            MainWindowView.this.consolePanel.varConsole.append("[" + now + "] " + msg + "\n");
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

    @Override
    public void setTitle(String title) {
        super.setTitle(JFWKNOP_TITLE + title);
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

    public JButton getButton(EnumButton buttonId) {
        return this.btnMap.get(buttonId);
    }
}
