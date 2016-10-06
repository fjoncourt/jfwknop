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
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author franck
 */
public class GeneralTab extends JPanel {

    public GeneralTab(Map<EnumFwknopRcKey, IFwknopVariable> varMap, Map<EnumButton, JButton> btnMap) {
        super(new MigLayout("fill", "[left][center][right]", ""));
        
        // Create a button to browse for IP
        ImageIcon browseImg = new ImageIcon(this.getClass().getResource("/browse16.png"));
        JButton browseForIp = new JButton(browseImg);
        browseForIp.setToolTipText(InternationalizationHelper.getMessage("i18n.browse"));
        btnMap.put(EnumButton.GENERAL_BROWSE_FOR_IP, browseForIp);
        
        // Build UI
        initializeVariables(varMap, btnMap);
    }

    private void initializeVariables(Map<EnumFwknopRcKey, IFwknopVariable> varMap, Map<EnumButton, JButton> btnMap) {

        List<EnumFwknopRcKey> list;

        /**
         * Client panel
         */
        JPanel clientPanel = (JPanel) FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[150]0![200]0![16]", ""),
            InternationalizationHelper.getMessage("i18n.spa.client"), varMap, Arrays.asList(
            EnumFwknopRcKey.ALLOW_IP,
            EnumFwknopRcKey.RESOLVE_IP_HTTPS,
            EnumFwknopRcKey.RESOLVE_HTTP_ONLY,
            EnumFwknopRcKey.RESOLVE_URL,
            EnumFwknopRcKey.ACCESS,
            EnumFwknopRcKey.SPA_SOURCE_PORT,
            EnumFwknopRcKey.RAND_PORT,
            EnumFwknopRcKey.SPA_SERVER_PROTO,
            EnumFwknopRcKey.SPOOF_USER,
            EnumFwknopRcKey.SPOOF_SOURCE_IP));
        clientPanel.add(btnMap.get(EnumButton.GENERAL_BROWSE_FOR_IP), "cell 2 0, top");
        this.add(clientPanel, "growy, aligny top");

        /**
         * Server panel
         */
        list = Arrays.asList(
            EnumFwknopRcKey.SPA_SERVER,
            EnumFwknopRcKey.SPA_SERVER_PORT,
            EnumFwknopRcKey.SERVER_RESOLVE_IPV4);

        this.add(FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![200]", ""),
            InternationalizationHelper.getMessage("i18n.spa.server"), varMap, list), "growy, aligny top");

        /**
         * Misc panel
         */
        list = Arrays.asList(
            EnumFwknopRcKey.FW_TIMEOUT,
            EnumFwknopRcKey.TIME_OFFSET);

        this.add(FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![200]", ""),
            InternationalizationHelper.getMessage("i18n.misc"), varMap, list), "growy, aligny top");
    }
}
