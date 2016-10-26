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
package com.cipherdyne.jfwknop;

import com.cipherdyne.gui.components.IFwknopVariable;
import com.cipherdyne.gui.components.JFwknopComboBox;
import com.cipherdyne.gui.components.JFwknopLabel;
import com.cipherdyne.gui.components.JFwknopTextArea;
import com.cipherdyne.gui.components.JFwknopTextField;
import static com.cipherdyne.jfwknop.EnumFwknopRcType.*;
import java.awt.Component;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Franck Joncourt
 */
public class FwknopFactory {

    private static final String[] YES_NO_LABELS = new String[]{"<Default>", "Y", "N"};
    private static final String[] DIGEST_ALGO_LABELS = new String[]{"SHA256", "MD5", "SHA1", "SHA384", "SHA512", "SHA3_256", "SHA3_512"};
    private static final String[] PROTOCOL_LABELS = new String[]{"udp", "tcp", "http", "udpraw", "tcpraw", "icmp"};

    static IFwknopVariable createComponent(final EnumFwknopRcKey key) {
        EnumFwknopRcType type = key.getType();
        String i18Label = key.getLabel();
        IFwknopVariable component;
        switch (type) {
            case PORT_LIST:
                component = new JFwknopTextField("<port1, port2 ...>");
                break;
            case SINGLE_PORT:
                component = new JFwknopTextField("<port>");
                break;
            case PROTOCOL_PLUS_PORT:
                component = new JFwknopTextField("<proto/port>");
                break;
            case IP_PLUS_PORT:
                component = new JFwknopTextField("<ip/port>");
                break;
            case PROTOCOL:
                component = new JFwknopComboBox(PROTOCOL_LABELS);
                break;
            case PASSPHRASE:
                component = new JFwknopTextField("<passphrase>");
                break;
            case BASE64_PASSPHRASE:
                component = new JFwknopTextArea("<base64 encoded passphrase>");
                break;
            case URL:
                component = new JFwknopTextField("<url>");
                break;
            case TIME:
                component = new JFwknopTextField("<time>");
                break;
            case SECONDS:
                component = new JFwknopTextField("<seconds>");
                break;
            case Y_N:
                component = new JFwknopComboBox(YES_NO_LABELS);
                break;
            case DIGEST_ALGORITHM:
                component = new JFwknopComboBox(DIGEST_ALGO_LABELS);
                break;
            case IP_ADDRESS:
                component = new JFwknopTextField("<Ip address>");
                break;
            case LOCAL_IP_ADDRESS:
                component = new JFwknopTextField("source");
                break;
            case GPG_KEY_ID:
                component = new JFwknopTextField("<Key id>");
                break;
            case DIRECTORY_PATH:
                component = new JFwknopTextField("<directory path>");
                break;
            case STRING:
                component = new JFwknopTextField("<string>");
                break;
            default:
                component = new JFwknopTextField("Undefined");
                break;
        }

        return component;
    }

    static public Component createPanel(Map<EnumFwknopRcKey, IFwknopVariable> varMap, List<EnumFwknopRcKey> keyList) {
        final JPanel pane = new JPanel(new MigLayout("insets 1, wrap 2, gapy 1!", "[120]0![200]", ""));
        keyList.forEach((item) -> {
            addVarToPanel(varMap, pane, item);
        });
        return pane;
    }

    static public Component createPanel(MigLayout layout, String title, Map<EnumFwknopRcKey, IFwknopVariable> varMap, List<EnumFwknopRcKey> keyList) {
        JPanel pane = (new JPanel(layout));
        pane.setBorder(new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD, 10)));
        keyList.forEach((item) -> {
            addVarToPanel(varMap, pane, item);
        });
        return pane;
    }

    static private void addVarToPanel(Map<EnumFwknopRcKey, IFwknopVariable> varMap, final JPanel panel, final EnumFwknopRcKey rcKey) {
        varMap.put(rcKey, FwknopFactory.createComponent(rcKey));
        JFwknopLabel label = new JFwknopLabel(rcKey.getLabel());
        if (rcKey.getTooltip() != null) {
            label.setToolTipText(rcKey.getTooltip());
        }
        panel.add(label, "growx");
        panel.add((JComponent) (varMap.get(rcKey)), "growx");
    }
}
