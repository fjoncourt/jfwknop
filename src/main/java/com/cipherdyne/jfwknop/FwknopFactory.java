/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.jfwknop;

import static com.cipherdyne.jfwknop.EnumFwknopRcType.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author franck
 */
public class FwknopFactory {

    private static final String[] YES_NO_LABELS = new String[]{"<Default>", "Y", "N"};
    private static final String[] DIGEST_ALGO_LABELS = new String[]{"SHA256", "MD5", "SHA1", "SHA384", "SHA512"};
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

    static public JLabel createLabel(String label) {
        JLabel labelComponent = new JLabel(label);
        Border paddingBorder = BorderFactory.createEmptyBorder(0, 2, 0, 2);
        MatteBorder border = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray);
        labelComponent.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
        return labelComponent;
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
        panel.add(FwknopFactory.createLabel(rcKey.getLabel()), "growx");
        panel.add((JComponent)(varMap.get(rcKey)), "growx");
    }
}
