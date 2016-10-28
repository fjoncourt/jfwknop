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

import com.cipherdyne.gui.wizard.EnumWizardView;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Franck Joncourt
 */
abstract class AbstractView extends JPanel implements IWizardView {

    private EnumWizardView previousPanel;

    public AbstractView() {
        this.setLayout(new MigLayout("aligny top, fillx, insets 0, gapy 5, flowy", "", ""));
    }

    @Override
    public void setPreviousPanel(EnumWizardView panel) {
        this.previousPanel = panel;
    }

    @Override
    public EnumWizardView getPreviousPanel() {
        return this.previousPanel;
    }
}
