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
import com.cipherdyne.jfwknop.IFwknopVariable;
import com.cipherdyne.utils.InternationalizationHelper;
import com.cipherdyne.gui.components.JFwknopComboBox;
import java.awt.Font;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author franck
 */
public class ConsolePanel extends JPanel {

    /* Console that displays the fwknop output */
    public JTextArea varConsole;
    
    /* Button to clear the console */
    public JButton btnClearConsole;
    
    /* Button to Execute the fwknop client with the curent selected configuration */
    public JButton btnExecute;
    
    /* Combo box used to select the configuration to apply to knock */
    public JFwknopComboBox cbConfigList;

    public ConsolePanel(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {
        super(new MigLayout("insets 0 10 10 10, aligny top, flowy, gap 0, fill", "[grow]", "[][]"));
        initialize(varMap);
    }

    private void initialize(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {

        JPanel btnPanel = new JPanel(new MigLayout("insets 0, gap 0, flowx","",""));
        ImageIcon clearImg = new ImageIcon(this.getClass().getResource("/clear16.png"));
        this.btnClearConsole = new JButton(clearImg);
        this.btnClearConsole.setToolTipText(InternationalizationHelper.getMessage("i18n.btn.clear"));
        btnPanel.add(btnClearConsole);
        ImageIcon executeImg = new ImageIcon(this.getClass().getResource("/execute16.png"));
        this.btnExecute = new JButton(executeImg);
        this.btnExecute.setToolTipText(InternationalizationHelper.getMessage("i18n.btn.execute"));
        btnPanel.add(btnExecute);        
        this.cbConfigList = new JFwknopComboBox(new String[]{"Default"});
        btnPanel.add(this.cbConfigList);
        
        this.add(btnPanel);
        
        // Set up console area
        this.varConsole = new JTextArea();
        this.varConsole.setRows(10);
        this.varConsole.setEditable(false);
        final Font font = this.varConsole.getFont();
        this.varConsole.setFont(new Font("Free Monospaced", font.getStyle(),
                font.getSize() - 1));
        this.add(createScrollablePanel(this.varConsole), "grow");
    }

    /**
     * Creates a scrollable panel around a component.
     *
     * @param component Text area to wrap inside the panel.
     * @return Created panel.
     */
    private JPanel createScrollablePanel(final JComponent component) {
        final JPanel scrollablePanel = new JPanel(new MigLayout(
                "fill, flowy, insets 0", "", ""));
        JScrollPane scrollPane = new JScrollPane(component,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(component);
        scrollablePanel.add(scrollPane, /* new JScrollPane(component), */
                "grow, gapright 0");
        return scrollablePanel;
    }
}
