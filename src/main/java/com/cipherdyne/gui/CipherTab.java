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

import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.FwknopFactory;
import com.cipherdyne.jfwknop.IFwknopVariable;
import com.cipherdyne.utils.InternationalizationHelper;
import java.util.Arrays;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * @author Franck Joncourt <franck.joncourt@gmail.com>
 */
public class CipherTab extends JPanel {

    public CipherTab(Map<EnumFwknopRcKey, IFwknopVariable> varMap, Map<EnumButton, JButton> btnMap) {
        super(new MigLayout("fill", "[left][center][right]", ""));
        createComponents(varMap, btnMap);
        initialize(varMap, btnMap);
    }

    private void createComponents(Map<EnumFwknopRcKey, IFwknopVariable> varMap, Map<EnumButton, JButton> btnMap) {
        ImageIcon plusImg = new ImageIcon(this.getClass().getResource("/plus16.png"));
        ImageIcon removeImg = new ImageIcon(this.getClass().getResource("/remove16.png"));
        ImageIcon encodeImg = new ImageIcon(this.getClass().getResource("/encode16.png"));
        ImageIcon browseImg = new ImageIcon(this.getClass().getResource("/browse16.png"));

        JButton generateRijndaelKey = new JButton(plusImg);
        generateRijndaelKey.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.key"));
        btnMap.put(EnumButton.CIPHER_GENERATE_RIJNDAEL_KEY, generateRijndaelKey);

        JButton removeRijndaelKey = new JButton(removeImg);
        removeRijndaelKey.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.key"));
        btnMap.put(EnumButton.CIPHER_REMOVE_RIJNDAEL_KEY, removeRijndaelKey);

        JButton generateBase64RijndaelKey = new JButton(plusImg);
        generateBase64RijndaelKey.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.base64.key"));
        btnMap.put(EnumButton.CIPHER_GENERATE_BASE64_RIJNDAEL_KEY, generateBase64RijndaelKey);

        JButton removeBase64RijndaelKey = new JButton(removeImg);
        removeBase64RijndaelKey.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.base64.key"));
        btnMap.put(EnumButton.CIPHER_REMOVE_BASE64_RIJNDAEL_KEY, removeBase64RijndaelKey);

        JButton btnGenerateBase64GpgPassphrase = new JButton(encodeImg);
        btnGenerateBase64GpgPassphrase.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.base64.passphrase"));
        btnMap.put(EnumButton.CIPHER_GENERATE_BASE64_GPG, btnGenerateBase64GpgPassphrase);

        JButton selectSignerGpgId = new JButton(browseImg);
        selectSignerGpgId.setToolTipText(InternationalizationHelper.getMessage("i18n.browse"));
        btnMap.put(EnumButton.CIPHER_SELECT_SIGNER_GPG_ID, selectSignerGpgId);

        JButton selectRecipientGpgId = new JButton(browseImg);
        selectRecipientGpgId.setToolTipText(InternationalizationHelper.getMessage("i18n.browse"));
        btnMap.put(EnumButton.CIPHER_SELECT_RECIPIENT_GPG_ID, selectRecipientGpgId);

        JButton browseforGpgHomedir = new JButton(browseImg);
        browseforGpgHomedir.setToolTipText(InternationalizationHelper.getMessage("i18n.browse"));
        btnMap.put(EnumButton.CIPHER_BROWSE_GPG_HOMEDIR, browseforGpgHomedir);

        JButton generateHmacKey = new JButton(plusImg);
        generateHmacKey.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.key"));
        btnMap.put(EnumButton.CIPHER_GENERATE_HMAC_KEY, generateHmacKey);

        JButton removeHmacKey = new JButton(removeImg);
        removeHmacKey.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.key"));
        btnMap.put(EnumButton.CIPHER_REMOVE_HMAC_KEY, removeHmacKey);

        JButton generateBase64HmacKey = new JButton(plusImg);
        generateBase64HmacKey.setToolTipText(InternationalizationHelper.getMessage("i18n.generate.base64.key"));
        btnMap.put(EnumButton.CIPHER_GENERATE_BASE64_HMAC_KEY, generateBase64HmacKey);

        JButton removeBase64HmacKey = new JButton(removeImg);
        removeBase64HmacKey.setToolTipText(InternationalizationHelper.getMessage("i18n.remove.base64.key"));
        btnMap.put(EnumButton.CIPHER_REMOVE_BASE64_HMAC_KEY, removeBase64HmacKey);
    }

    private void initialize(Map<EnumFwknopRcKey, IFwknopVariable> varMap, Map<EnumButton, JButton> btnMap) {

        /**
         * Rijndael panel
         */
        JPanel rijndaelPanel = (JPanel) FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[80]0![180]0![16]", ""),
            InternationalizationHelper.getMessage("i18n.rijndael"), varMap, Arrays.asList(
            EnumFwknopRcKey.KEY,
            EnumFwknopRcKey.KEY_BASE64));

        JPanel rijndaelActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        rijndaelActionPanel.add(btnMap.get(EnumButton.CIPHER_GENERATE_RIJNDAEL_KEY));
        rijndaelActionPanel.add(btnMap.get(EnumButton.CIPHER_REMOVE_RIJNDAEL_KEY));
        rijndaelPanel.add(rijndaelActionPanel, "cell 2 0");

        JPanel rijndaelBase64ActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        rijndaelBase64ActionPanel.add(btnMap.get(EnumButton.CIPHER_GENERATE_BASE64_RIJNDAEL_KEY));
        rijndaelBase64ActionPanel.add(btnMap.get(EnumButton.CIPHER_REMOVE_BASE64_RIJNDAEL_KEY));
        rijndaelPanel.add(rijndaelBase64ActionPanel, "cell 2 1, aligny top");

        this.add(rijndaelPanel, "growy, aligny top");

        /**
         * GPG panel
         */
        JPanel gpgPanel = (JPanel) FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[100]0![180]0![16]", ""),
            InternationalizationHelper.getMessage("i18n.gpg"), varMap, Arrays.asList(
            EnumFwknopRcKey.USE_GPG,
            EnumFwknopRcKey.USE_GPG_AGENT,
            EnumFwknopRcKey.GPG_SIGNING_PW,
            EnumFwknopRcKey.GPG_SIGNING_PW_BASE64,
            EnumFwknopRcKey.GPG_SIGNER,
            EnumFwknopRcKey.GPG_RECIPIENT,
            EnumFwknopRcKey.GPG_HOMEDIR));
        this.add(gpgPanel, "growy, aligny top");
        gpgPanel.add(btnMap.get(EnumButton.CIPHER_GENERATE_BASE64_GPG), "cell 2 3, top");
        gpgPanel.add(btnMap.get(EnumButton.CIPHER_SELECT_SIGNER_GPG_ID), "cell 2 4");
        gpgPanel.add(btnMap.get(EnumButton.CIPHER_SELECT_RECIPIENT_GPG_ID), "cell 2 5");
        gpgPanel.add(btnMap.get(EnumButton.CIPHER_BROWSE_GPG_HOMEDIR), "cell 2 6");

        /**
         * Hmac panel
         */
        JPanel hmacPanel = (JPanel) FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[85]0![180]0![16]", ""),
            InternationalizationHelper.getMessage("i18n.hmac"), varMap, Arrays.asList(
            EnumFwknopRcKey.USE_HMAC,
            EnumFwknopRcKey.HMAC_KEY,
            EnumFwknopRcKey.HMAC_KEY_BASE64,
            EnumFwknopRcKey.HMAC_DIGEST_TYPE));

        JPanel hmacActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        hmacActionPanel.add(btnMap.get(EnumButton.CIPHER_GENERATE_HMAC_KEY));
        hmacActionPanel.add(btnMap.get(EnumButton.CIPHER_REMOVE_HMAC_KEY));
        hmacPanel.add(hmacActionPanel, "cell 2 1");

        JPanel hmacBase64ActionPanel = new JPanel(new MigLayout("insets 0, flowx, gap 0", "", ""));
        hmacBase64ActionPanel.add(btnMap.get(EnumButton.CIPHER_GENERATE_BASE64_HMAC_KEY));
        hmacBase64ActionPanel.add(btnMap.get(EnumButton.CIPHER_REMOVE_BASE64_HMAC_KEY));
        hmacPanel.add(hmacBase64ActionPanel, "cell 2 2, aligny top");

        this.add(hmacPanel, "growy, aligny top");
    }
}
