/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui;

import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.FwknopFactory;
import com.cipherdyne.jfwknop.IFwknopVariable;
import com.cipherdyne.jfwknop.InternationalizationHelper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author franck
 */
public class GeneralTab extends JPanel {

    public GeneralTab(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {
        super(new MigLayout("fill", "[left][center][right]", ""));
        initialize(varMap);
    }

    private void initialize(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {

        List<EnumFwknopRcKey> list = Arrays.asList(
                EnumFwknopRcKey.ALLOW_IP,
                EnumFwknopRcKey.RESOLVE_IP_HTTPS,
                EnumFwknopRcKey.RESOLVE_HTTP_ONLY,
                EnumFwknopRcKey.RESOLVE_URL,
                EnumFwknopRcKey.ACCESS,
                EnumFwknopRcKey.SPA_SOURCE_PORT,
                EnumFwknopRcKey.RAND_PORT,
                EnumFwknopRcKey.SPA_SERVER_PROTO,
                EnumFwknopRcKey.SPOOF_USER,
                EnumFwknopRcKey.SPOOF_SOURCE_IP);

        this.add(FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[150]0![200]", ""), 
                InternationalizationHelper.getMessage("i18n.spa.client"), varMap, list), "growy, aligny top");

        list = Arrays.asList(
                EnumFwknopRcKey.SPA_SERVER,
                EnumFwknopRcKey.SPA_SERVER_PORT,
                EnumFwknopRcKey.SERVER_RESOLVE_IPV4);

        this.add(FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![200]", ""), 
                InternationalizationHelper.getMessage("i18n.spa.server"), varMap, list), "growy, aligny top");

        list = Arrays.asList(
                EnumFwknopRcKey.FW_TIMEOUT,
                EnumFwknopRcKey.TIME_OFFSET);

        this.add(FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![200]", ""), 
                InternationalizationHelper.getMessage("i18n.misc"), varMap, list), "growy, aligny top");
    }
}
