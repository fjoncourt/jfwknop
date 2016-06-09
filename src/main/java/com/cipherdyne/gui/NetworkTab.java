/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.gui;

import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.FwknopFactory;
import com.cipherdyne.jfwknop.IFwknopVariable;
import com.cipherdyne.utils.InternationalizationHelper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author franck
 */
public class NetworkTab extends JPanel {

    public NetworkTab(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {
        super(new MigLayout("fill", "", ""));
        initialize(varMap);
    }

    private void initialize(Map<EnumFwknopRcKey, IFwknopVariable> varMap) {
        List<EnumFwknopRcKey> list = Arrays.asList(
                EnumFwknopRcKey.NAT_ACCESS,
                EnumFwknopRcKey.NAT_LOCAL,
                EnumFwknopRcKey.NAT_PORT,
                EnumFwknopRcKey.NAT_RAND_PORT);

        this.add(FwknopFactory.createPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![200]", ""),
                InternationalizationHelper.getMessage("i18n.nat"), varMap, list), "growy, aligny top");
    }
}
