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
package com.cipherdyne.gui.wizard.views;

import com.cipherdyne.gui.components.IFwknopVariable;
import com.cipherdyne.gui.components.JFwknopTextField;
import com.cipherdyne.gui.wizard.EnumWizardButton;
import com.cipherdyne.gui.wizard.EnumWizardVariable;
import com.cipherdyne.gui.wizard.EnumWizardView;
import java.util.Map;
import javax.swing.JButton;

/**
 * Wizard view that displays basic Fwknop variables to set up in order to quickly run and SPA packet
 *
 * @author Franck Joncourt
 */
public class GpgSignerIdView extends AbstractView {

    @Override
    public void initialize(Map<EnumWizardVariable, IFwknopVariable> varMap, Map<EnumWizardButton, JButton> btnMap) {
        varMap.put(EnumWizardVariable.GPG_SIGNER_ID, new JFwknopTextField(""));
        varMap.put(EnumWizardVariable.GPG_SIGNER_PASSWORD, new JFwknopTextField(""));
        btnMap.put(EnumWizardButton.BROWSE_FOR_GPG_SIGNER_ID, new JButton(EnumWizardButton.BROWSE_FOR_GPG_SIGNER_ID.getDescription()));

        // Add object to the newly created panel
        this.add(Utils.createItem(varMap, EnumWizardVariable.GPG_SIGNER_ID), "growx");
        this.add(btnMap.get(EnumWizardButton.BROWSE_FOR_GPG_SIGNER_ID), "growx");
        this.add(Utils.createItem(varMap, EnumWizardVariable.GPG_SIGNER_PASSWORD), "growx");
    }

    @Override
    public EnumWizardView getNextPanel() {
        return EnumWizardView.SETUP_GPG_RECIPIENT_ID;
    }
}
