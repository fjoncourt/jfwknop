/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui;

import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.FwknopFactory;
import com.cipherdyne.jfwknop.IFwknopVariable;
import com.cipherdyne.jfwknop.InternationalizationHelper;
import com.cipherdyne.jfwknop.JFwknopArgs;
import com.cipherdyne.jfwknop.JFwknopTextField;
import java.awt.Font;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author franck
 */
public class SettingsTab extends JPanel {

    static private final int DEFAULT_RIJNDAEL_KEY_LENGTH = 128;
    static private final int DEFAULT_RIJNDAEL_BASE64_BYTES = 32;
    static private final int DEFAULT_HMAC_KEY_LENGTH = 128;
    static private final int DEFAULT_HMAC_BASE64_BYTES = 64;
    static private final int DEFAULT_GPG_BASE64_BYTES = 32;
    
    public JButton btnBrowseforFwknop;
    public JFwknopTextField varFwknopFilePath;
    public JFwknopArgs varFwknopArgs;
    public JFwknopTextField varFwknopExtraArgs;
    public JCheckBox btnFwknopVerbose;
    public JCheckBox btnFwknopTest;
    public JButton btnFwknopSaveConfig;
    
    public JFwknopTextField varRijndaelKeyLength;
    public JFwknopTextField varRijndaelBase64Bytes;
    public JFwknopTextField varHmacKeyLength;
    public JFwknopTextField varHmacBase64Bytes;
    public JFwknopTextField varGpgBase64Bytes;
    public JButton btnBase64SaveConfig;

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
        this.btnFwknopSaveConfig = new JButton(InternationalizationHelper.getMessage("i18n.save.fwknop.settings"));

        // Create panel
        final JPanel fwknopPanel = new JPanel(new MigLayout("flowx, gapy 1!", "[120!]0![400!][]", ""));
        fwknopPanel.setBorder(new TitledBorder(null, InternationalizationHelper.getMessage("i18n.fwknop"), TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD, 10)));

        // Add components
        fwknopPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.fwknop.binary")), "growx");
        fwknopPanel.add(this.varFwknopFilePath = new JFwknopTextField(""), "growx");
        fwknopPanel.add(this.btnBrowseforFwknop, "wrap");
        fwknopPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.fwknop.args")), "growx");
        fwknopPanel.add(this.varFwknopArgs = new JFwknopArgs(""), "growx, wrap");
        fwknopPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.fwknop.extraargs")), "growx");
        fwknopPanel.add(this.varFwknopExtraArgs = new JFwknopTextField(""), "growx, wrap");
        fwknopPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.fwknop.verbose")), "growx");
        fwknopPanel.add(this.btnFwknopVerbose, "height 24, wrap");
        fwknopPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.fwknop.test")), "growx");
        fwknopPanel.add(this.btnFwknopTest, "height 24, wrap");
        fwknopPanel.add(this.btnFwknopSaveConfig, "gaptop 5, span 3, growx");

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
        this.btnBase64SaveConfig = new JButton(InternationalizationHelper.getMessage("i18n.save.key.settings"));
        
        // Create panel
        final JPanel keyPanel = new JPanel(new MigLayout("flowx, gapy 1!, wrap 2", "[400!]0![120!]", ""));
        keyPanel.setBorder(new TitledBorder(null, InternationalizationHelper.getMessage("i18n.key"), TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD, 10)));

        // Add components
        keyPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.default.rijndael.key.length")), "growx");
        keyPanel.add(this.varRijndaelKeyLength = new JFwknopTextField(Integer.toString(DEFAULT_RIJNDAEL_KEY_LENGTH)), "growx");        
        keyPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.default.rijndael.base64.bytes.array.length")), "growx");
        keyPanel.add(this.varRijndaelBase64Bytes = new JFwknopTextField(Integer.toString(DEFAULT_RIJNDAEL_BASE64_BYTES)), "growx");
        
        keyPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.default.hmac.key.length")), "growx");
        keyPanel.add(this.varHmacKeyLength = new JFwknopTextField(Integer.toString(DEFAULT_HMAC_KEY_LENGTH)), "growx");  
        keyPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.default.hmac.base64.bytes.array.length")), "growx");
        keyPanel.add(this.varHmacBase64Bytes = new JFwknopTextField(Integer.toString(DEFAULT_HMAC_BASE64_BYTES)), "growx");
        
        keyPanel.add(FwknopFactory.createLabel(InternationalizationHelper.getMessage("i18n.default.gpg.base64.bytes.array.length")), "growx");
        keyPanel.add(this.varGpgBase64Bytes = new JFwknopTextField(Integer.toString(DEFAULT_GPG_BASE64_BYTES)), "growx");
        keyPanel.add(this.btnBase64SaveConfig, "gaptop 5, span 2, growx");

        return keyPanel;
    }
}
