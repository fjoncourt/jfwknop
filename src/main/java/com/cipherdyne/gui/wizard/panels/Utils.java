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
package com.cipherdyne.gui.wizard.panels;

import com.cipherdyne.gui.components.IFwknopVariable;
import com.cipherdyne.gui.wizard.EnumWizardVariable;
import java.awt.Color;
import java.awt.Component;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Franck Joncourt
 */
public class Utils {

    /**
     * Create an item for the wizard setup.
     *
     * An item is composed of a long description and a variable to be filled in. Both of them are
     * added to a single panel.
     *
     * @param longDescription description to display to the user to help him fill in the variable
     * value
     * @param var EnumVariable variable that contains the user value
     * @return the panel that contains the description and the variable
     */
    static JPanel createItem(Map<EnumWizardVariable, IFwknopVariable> context, EnumWizardVariable var) {
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 1, flowy, fill", "", ""));

        // Create a transparent text area rather than a label since the description can long and may need to be wrapped on several lines
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        editorPane.setBackground(new Color(10, 10, 10, 0));
        editorPane.setContentType("text/html");
        editorPane.setText(var.getDescription());

        // Add object to the newly created panel
        panel.add(editorPane, "growx");
        panel.add((Component) context.get(var), "growx");

        return panel;
    }
}
