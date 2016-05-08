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

    public JButton generateRijndael;
    public JButton removeRijndael;
    public JButton generateBase64Rijndael;
    public JButton removeBase64Rijndael;
    public JButton generateHmac;
    public JButton removeHmac;
    public JButton generateBase64Hmac;
    public JButton removeBase64Hmac;    

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
        this.generateRijndael = new JButton(plusImg);
        this.generateRijndael.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.key"));
        rijndaelActionPanel.add(this.generateRijndael);
        this.removeRijndael = new JButton(removeImg);
        this.removeRijndael.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.key"));
        rijndaelActionPanel.add(this.removeRijndael);
        rijndaelPanel.add(rijndaelActionPanel, "cell 2 0");
        
        JPanel rijndaelBase64ActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        this.generateBase64Rijndael = new JButton(plusImg);
        this.generateBase64Rijndael.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.base64.key"));
        rijndaelBase64ActionPanel.add(this.generateBase64Rijndael);
        this.removeBase64Rijndael = new JButton(removeImg);
        this.removeBase64Rijndael.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.base64.key"));         
        rijndaelBase64ActionPanel.add(this.removeBase64Rijndael);
        rijndaelPanel.add(rijndaelBase64ActionPanel, "cell 2 1, aligny top");
        
        this.add(rijndaelPanel, "growy, aligny top");

        /**
         * GPG panel
         */
        this.add(FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![220]", ""),
                InternationalizationHelper.getMessage("i18n.gpg"), varMap, Arrays.asList(
                EnumFwknopRcKey.USE_GPG,
                EnumFwknopRcKey.USE_GPG_AGENT,
                EnumFwknopRcKey.GPG_SIGNING_PW,
                EnumFwknopRcKey.GPG_SIGNING_PW_BASE64,
                EnumFwknopRcKey.GPG_SIGNER,
                EnumFwknopRcKey.GPG_RECIPIENT,
                EnumFwknopRcKey.GPG_HOMEDIR)), "growy, aligny top");

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
        this.generateHmac = new JButton(plusImg);
        this.generateHmac.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.key"));
        hmacActionPanel.add(this.generateHmac);
        this.removeHmac = new JButton(removeImg);
        this.removeHmac.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.key"));
        hmacActionPanel.add(this.removeHmac);
        hmacPanel.add(hmacActionPanel, "cell 2 1");
        
        JPanel hmacBase64ActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        this.generateBase64Hmac = new JButton(plusImg);
        this.generateBase64Hmac.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.base64.key"));
        hmacBase64ActionPanel.add(this.generateBase64Hmac);
        this.removeBase64Hmac = new JButton(removeImg);
        this.removeBase64Hmac.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.base64.key"));         
        hmacBase64ActionPanel.add(this.removeBase64Hmac);
        hmacPanel.add(hmacBase64ActionPanel, "cell 2 2, aligny top");
        
        this.add(hmacPanel, "growy, aligny top");
    }
}
