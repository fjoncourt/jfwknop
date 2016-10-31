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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JLabel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Hyperlink class that displays an internet link and provide the ability to open a browser when
 * clicking on the link.
 *
 * @author Franck Joncourt
 */
public class Hyperlink extends JLabel {

    static final Logger LOGGER = LogManager.getLogger(Hyperlink.class.getName());

    /**
     * Hyperlink constructor
     *
     * @param label link to display
     */
    public Hyperlink(String label) {
        super(label);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setForeground(Color.BLUE);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 0) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            URI uri = new URI(label);
                            desktop.browse(uri);
                        } catch (IOException | URISyntaxException ex) {
                            LOGGER.error("Unable to open browser", ex);
                        }
                    }
                }
            }
        });
    }
}
