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
import java.util.Iterator;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Franck Joncourt
 */
public class About extends JDialog {

    public About(JFrame parentFrame) {
        super(parentFrame, InternationalizationHelper.getMessage("i18n.about"), true);

        try {


            Properties props = new Properties();

            props.load(this.getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"));

            for (Iterator iterator = props.keySet().iterator(); iterator.hasNext();) {

	                    Object key = iterator.next();

	                    Object value = props.get(key);

	                    System.out.println(key+"="+value);

            }
            this.add(new JLabel("version 1.2.0-SNAPSHOT"));

        } catch (Throwable e) {

            e.printStackTrace();

        }


        this.pack();

        this.setLocationRelativeTo(parentFrame);
    }
}
