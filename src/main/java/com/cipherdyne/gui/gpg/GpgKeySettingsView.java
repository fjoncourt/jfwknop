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
package com.cipherdyne.gui.gpg;

import com.cipherdyne.utils.InternationalizationHelper;
import com.cipherdyne.gui.components.JFwknopTextField;
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
        return this.passphrase;
    }    
    
    public JButton getBtnSubmit() {
        return this.btnSubmit;
    }        
}
