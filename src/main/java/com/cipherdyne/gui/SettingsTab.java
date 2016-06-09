/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui;

import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.IFwknopVariable;
import com.cipherdyne.utils.InternationalizationHelper;
import com.cipherdyne.gui.components.JFwknopArgs;
import com.cipherdyne.gui.components.JFwknopLabel;
import com.cipherdyne.gui.components.JFwknopTextField;
import java.awt.Font;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author franck
 */
public class SettingsTab extends JPanel {
    
    public JButton btnBrowseforFwknop;
    public JFwknopTextField varFwknopFilePath;
    public JFwknopArgs varFwknopArgs;
    public JFwknopTextField varFwknopExtraArgs;
    public JCheckBox btnFwknopVerbose;
    public JCheckBox btnFwknopTest;
    public JButton btnSaveFwknopSettings;
    
    public JFwknopTextField varRijndaelKeyLength;
    public JFwknopTextField varBase64RijndaelBytes;
    public JFwknopTextField varHmacKeyLength;
    public JFwknopTextField varBase64HmacBytes;
    public JButton btnSaveKeySettings;

    public SettingsTab(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {
        super(new MigLayout("fill, flowx", "", ""));

        JPanel fwknopPanel = initializeFwknopSettings(varMap);
        JPanel keyPanel = initializeKeySettings(varMap);

        this.add(fwknopPanel, "growy, aligny top");
        this.add(keyPanel, "growy, aligny top");
    }

    /**
     * Build a panel that stores fwknop client settings
     *
     * @param varMap Fwknop variable to use
     *
     * @return the fwknop panel
     */
    private JPanel initializeFwknopSettings(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {

        // Component initialization
        this.btnBrowseforFwknop = new JButton(InternationalizationHelper.getMessage("i18n.fwknop.browse"));
        this.btnFwknopVerbose = new JCheckBox();
        this.btnFwknopTest = new JCheckBox();
        this.btnSaveFwknopSettings = new JButton(InternationalizationHelper.getMessage("i18n.save.fwknop.settings"));

        // Create panel
        final JPanel fwknopPanel = new JPanel(new MigLayout("flowx, gapy 1!", "[120!]0![350!][]", ""));
        fwknopPanel.setBorder(new TitledBorder(null, InternationalizationHelper.getMessage("i18n.fwknop"), TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD, 10)));

        // Add components
        fwknopPanel.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.fwknop.binary")), "growx");
        fwknopPanel.add(this.varFwknopFilePath = new JFwknopTextField(StringUtils.EMPTY), "growx");
        fwknopPanel.add(this.btnBrowseforFwknop, "wrap");
        fwknopPanel.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.fwknop.args")), "growx");
        fwknopPanel.add(this.varFwknopArgs = new JFwknopArgs(StringUtils.EMPTY), "growx, wrap");
        fwknopPanel.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.fwknop.extraargs")), "growx");
        fwknopPanel.add(this.varFwknopExtraArgs = new JFwknopTextField(StringUtils.EMPTY), "growx, wrap");
        fwknopPanel.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.fwknop.verbose")), "growx");
        fwknopPanel.add(this.btnFwknopVerbose, "height 24, wrap");
        fwknopPanel.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.fwknop.test")), "growx");
        fwknopPanel.add(this.btnFwknopTest, "height 24, wrap");
        fwknopPanel.add(this.btnSaveFwknopSettings, "gaptop 5, span 3, growx");

        return fwknopPanel;
    }

    /**
     * Build a panel that stores key settings
     *
     * @param varMap Fwknop variable to use
     *
     * @return the key panel
     */
    private JPanel initializeKeySettings(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {

        // Component initialization
        this.btnSaveKeySettings = new JButton(InternationalizationHelper.getMessage("i18n.save.key.settings"));
        
        // Create panel
        final JPanel keyPanel = new JPanel(new MigLayout("flowx, gapy 1!, wrap 2", "[350!]0![120!]", ""));
        keyPanel.setBorder(new TitledBorder(null, InternationalizationHelper.getMessage("i18n.key"), TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD, 10)));

        // Add components
        keyPanel.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.rijndael.key.length")), "growx");
        keyPanel.add(this.varRijndaelKeyLength = new JFwknopTextField(StringUtils.EMPTY), "growx");        
        keyPanel.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.rijndael.base64.bytes.array.length")), "growx");
        keyPanel.add(this.varBase64RijndaelBytes = new JFwknopTextField(StringUtils.EMPTY), "growx");
        
        keyPanel.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.hmac.key.length")), "growx");
        keyPanel.add(this.varHmacKeyLength = new JFwknopTextField(StringUtils.EMPTY), "growx");  
        keyPanel.add(new JFwknopLabel(InternationalizationHelper.getMessage("i18n.hmac.base64.bytes.array.length")), "growx");
        keyPanel.add(this.varBase64HmacBytes = new JFwknopTextField(StringUtils.EMPTY), "growx");
        
        keyPanel.add(new JLabel(" "), "wrap");
        keyPanel.add(this.btnSaveKeySettings, "gaptop 5, span 2, growx");

        return keyPanel;
    }
}
