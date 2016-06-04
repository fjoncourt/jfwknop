/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui.gpg;

import com.cipherdyne.jfwknop.InternationalizationHelper;
import com.cipherdyne.jfwknop.JFwknopTextField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author franck
 */
public class GpgKeySettingsView extends JDialog {
    
    private final JFwknopTextField userId;
    private final JFwknopTextField passphrase;
    private final JButton btnSubmit;
    
    public GpgKeySettingsView(JFrame parentWindow) {
        super(parentWindow, InternationalizationHelper.getMessage("i18n.key.creation"), true);

        this.userId = new JFwknopTextField("<user_id>");
        this.passphrase = new JFwknopTextField("<passphrase>");
        this.btnSubmit = new JButton(InternationalizationHelper.getMessage("i18n.key.create"));
        
        this.setLayout(new MigLayout("inset 20, gap 0, flowx, wrap 2", "[120!][200!]", ""));
        this.add(new JLabel(InternationalizationHelper.getMessage("i18n.user.id")), "growx");
        this.add(this.userId, "growx");
        this.add(new JLabel(InternationalizationHelper.getMessage("i18n.key.passphrase")), "growx");
        this.add(this.passphrase, "growx");
        this.add(this.btnSubmit, "growx, span 2");
        
        this.pack();
    }
    
    public JFwknopTextField getUserId() {
        return this.userId;
    }
    
    public JFwknopTextField getPassphrase() {
        return this.userId;
    }    
    
    public JButton getBtnSubmit() {
        return this.btnSubmit;
    }        
}
