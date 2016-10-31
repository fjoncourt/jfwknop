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
package com.cipherdyne.gui.about;

import com.cipherdyne.utils.InternationalizationHelper;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * About window provides information about the application version, licence, copyright...
 *
 * @author Franck Joncourt
 */
public class About extends JDialog {

    /**
     * About dialog constructor
     *
     * @param parentFrame parent frame we inherit
     */
    public About(JFrame parentFrame) {
        super(parentFrame, InternationalizationHelper.getMessage("i18n.about"), true);
        this.setResizable(false);

        // Create title
        JLabel title = new JLabel("JFwknop 1.2.0");
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, title.getFont().getSize() + 4));

        // Create a panel that contains the JFwknop title along with the icon
        JPanel titlePanel = new JPanel(new MigLayout("insets 10, flowx", "", ""));
        titlePanel.add(new JLabel(new ImageIcon(this.getClass().getResource("/jfwknop.png"))));
        titlePanel.add(title);

        // Build the main panel and add licence, copyright holder, github reference
        JPanel pane = new JPanel(new MigLayout("insets 30, flowy, gapy 10", "[left]", ""));
        pane.add(titlePanel);
        pane.add(new JLabel(" A user interface for Fwknop client"));
        pane.add(new Hyperlink("https://github.com/fjoncourt/jfwknop"));
        pane.add(new JLabel("Copyright 2016 Franck Joncourt"));
        pane.add(new JLabel("Licences under GPLv2+"));
        this.add(pane);

        // Pack everything
        this.pack();

        // Center the dialog according the main frame position
        this.setLocationRelativeTo(parentFrame);
    }
}
