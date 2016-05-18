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
import java.util.Arrays;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * @author franck
 */
public class CipherTab extends JPanel {

    public JButton generateRijndaelKey;
    public JButton removeRijndaelKey;
    public JButton generateBase64RijndaelKey;
    public JButton removeBase64RijndaelKey;
    public JButton generateHmacKey;
    public JButton removeHmacKey;
    public JButton generateBase64HmacKey;
    public JButton removeBase64HmacKey; 

    public JButton btnGenerateBase64GpgPassphrase;
    
    public JButton selectSignerGpgId;
    public JButton selectRecipientGpgId;
    public JButton browseforGpgHomedir;

    public CipherTab(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {
        super(new MigLayout("fill", "[left][center][right]", ""));
        initialize(varMap);
    }

    private void initialize(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {

        ImageIcon plusImg = new ImageIcon(this.getClass().getResource("/plus16.png"));
        ImageIcon removeImg = new ImageIcon(this.getClass().getResource("/remove16.png"));

        /**
         * Rijndael panel
         */
        JPanel rijndaelPanel = (JPanel) FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![220]0![16]", ""),
                InternationalizationHelper.getMessage("i18n.rijndael"), varMap, Arrays.asList(
                EnumFwknopRcKey.KEY,
                EnumFwknopRcKey.KEY_BASE64));

        JPanel rijndaelActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        this.generateRijndaelKey = new JButton(plusImg);
        this.generateRijndaelKey.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.key"));
        rijndaelActionPanel.add(this.generateRijndaelKey);
        this.removeRijndaelKey = new JButton(removeImg);
        this.removeRijndaelKey.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.key"));
        rijndaelActionPanel.add(this.removeRijndaelKey);
        rijndaelPanel.add(rijndaelActionPanel, "cell 2 0");
        
        JPanel rijndaelBase64ActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        this.generateBase64RijndaelKey = new JButton(plusImg);
        this.generateBase64RijndaelKey.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.base64.key"));
        rijndaelBase64ActionPanel.add(this.generateBase64RijndaelKey);
        this.removeBase64RijndaelKey = new JButton(removeImg);
        this.removeBase64RijndaelKey.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.base64.key"));         
        rijndaelBase64ActionPanel.add(this.removeBase64RijndaelKey);
        rijndaelPanel.add(rijndaelBase64ActionPanel, "cell 2 1, aligny top");
        
        this.add(rijndaelPanel, "growy, aligny top");

        /**
         * GPG panel
         */
        ImageIcon encodeImg = new ImageIcon(this.getClass().getResource("/encode16.png"));
        
        JPanel gpgPanel = (JPanel)FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![220]0![16]", ""),
                InternationalizationHelper.getMessage("i18n.gpg"), varMap, Arrays.asList(
                EnumFwknopRcKey.USE_GPG,
                EnumFwknopRcKey.USE_GPG_AGENT,
                EnumFwknopRcKey.GPG_SIGNING_PW,
                EnumFwknopRcKey.GPG_SIGNING_PW_BASE64,
                EnumFwknopRcKey.GPG_SIGNER,
                EnumFwknopRcKey.GPG_RECIPIENT,
                EnumFwknopRcKey.GPG_HOMEDIR));
        this.add(gpgPanel, "growy, aligny top");
        this.btnGenerateBase64GpgPassphrase = new JButton(encodeImg);
        this.btnGenerateBase64GpgPassphrase.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.base64.passphrase"));
        gpgPanel.add(this.btnGenerateBase64GpgPassphrase, "cell 2 3, center, growx, top");
        this.selectSignerGpgId = new JButton(InternationalizationHelper.getMessage("i18n.browse"));
        gpgPanel.add(this.selectSignerGpgId, "cell 2 4");
        this.selectRecipientGpgId = new JButton(InternationalizationHelper.getMessage("i18n.browse"));
        gpgPanel.add(this.selectRecipientGpgId, "cell 2 5");
        this.browseforGpgHomedir = new JButton(InternationalizationHelper.getMessage("i18n.browse"));
        gpgPanel.add(this.browseforGpgHomedir, "cell 2 6");
        
        /**
         * Hmac panel
         */
        JPanel hmacPanel = (JPanel) FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![220]0![16]", ""),
                InternationalizationHelper.getMessage("i18n.hmac"), varMap, Arrays.asList(
                EnumFwknopRcKey.USE_HMAC,
                EnumFwknopRcKey.HMAC_KEY,
                EnumFwknopRcKey.HMAC_KEY_BASE64,
                EnumFwknopRcKey.HMAC_DIGEST_TYPE));
        
        JPanel hmacActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        this.generateHmacKey = new JButton(plusImg);
        this.generateHmacKey.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.key"));
        hmacActionPanel.add(this.generateHmacKey);
        this.removeHmacKey = new JButton(removeImg);
        this.removeHmacKey.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.key"));
        hmacActionPanel.add(this.removeHmacKey);
        hmacPanel.add(hmacActionPanel, "cell 2 1");
        
        JPanel hmacBase64ActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        this.generateBase64HmacKey = new JButton(plusImg);
        this.generateBase64HmacKey.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.base64.key"));
        hmacBase64ActionPanel.add(this.generateBase64HmacKey);
        this.removeBase64HmacKey = new JButton(removeImg);
        this.removeBase64HmacKey.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.base64.key"));         
        hmacBase64ActionPanel.add(this.removeBase64HmacKey);
        hmacPanel.add(hmacBase64ActionPanel, "cell 2 2, aligny top");
        
        this.add(hmacPanel, "growy, aligny top");
    }
}
