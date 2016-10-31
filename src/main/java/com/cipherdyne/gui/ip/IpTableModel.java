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
package com.cipherdyne.gui.ip;

import com.cipherdyne.utils.InternationalizationHelper;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Table model used to list IPV4 or IPV6 addresses of the host
 */
public class IpTableModel extends AbstractTableModel {

    // Network interface is column 0 of the table model
    static public final int NETWORK_IFACE_COL_ID = 0;

    // IP address is column 1 of the table model
    static public final int IP_ADDRESS_COL_ID = 1;

    // Name of the available columns in the table model
    final private String[] columnNames = {
        InternationalizationHelper.getMessage("i18n.network.interface.id"),
        InternationalizationHelper.getMessage("i18n.ip.address.id")};

    // List of IP along withe their network interface that are displayed in the jtable
    private List<SimpleIp> ipData;

    /**
     * Create the IP table model.
     */
    public IpTableModel() {
        super();
        populateIp();
    }

    /**
     * Iterate over the network interfaces and add all the IP addresses to the data model
     */
    private void populateIp() {
        this.ipData = new ArrayList<>();

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                // filters out inactive interfaces
                if (!iface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ipData.add(new SimpleIp(iface.getDisplayName(), addr.getHostAddress()));
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return this.ipData.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SimpleIp ip = ipData.get(rowIndex);
        String value;
        switch (columnIndex) {
            case NETWORK_IFACE_COL_ID:
                value = ip.getNetowrkInterface();
                break;
            case IP_ADDRESS_COL_ID:
                value = ip.getIpAddress();
                break;
            default:
                value = "unknown";
        }
        return value;
    }

    /**
     * Class that represents a simple IP object. It provides the network interface and the IP
     * addresses.
     */
    private class SimpleIp {

        // Key id as an hexadicmal string
        final private String networkInterface;
        // IP address associated to the network interface (IP4 or IPV6)
        final private String ipAddress;

        /**
         * Create a simple IP object
         *
         * @param networkInterface Name of the network interface to link the IP to
         * @param ipAddress IP address
         */
        public SimpleIp(String networkInterface, String ipAddress) {
            this.networkInterface = networkInterface;
            this.ipAddress = ipAddress;
        }

        // Return the name of the network interface (lo, wlo1, eth0...)
        private String getNetowrkInterface() {
            return this.networkInterface;
        }

        // Return the IP address (IPV4 or IPV6)
        private String getIpAddress() {
            return this.ipAddress;
        }
    }

}
