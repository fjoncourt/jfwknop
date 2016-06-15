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
package com.cipherdyne.gui.ssh;

import com.cipherdyne.utils.InternationalizationHelper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

/**
 *
 */
public class SshFileTableModel extends AbstractTableModel {

    // filename is column 0 of the table model
    static public final int FILENAME_COL_ID = 0;

    // Exchange file status is column 1 of the table model
    static public final int EXCHANGE_FILE_STATUS_COL_ID = 1;

    // Name of the available columns in the table model
    final private String[] columnNames = {
        InternationalizationHelper.getMessage("i18n.filename"),
        InternationalizationHelper.getMessage("i18n.status")};

    // List of files that are displayed in the jtable
    private Set<FileExchange> fileExchangeData;

    /**
     * Create the SSH file table model.
     */
    public SshFileTableModel() {
        super();
        fileExchangeData = new HashSet<>();
        fileExchangeData.add(new FileExchange("test"));
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return this.fileExchangeData.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<FileExchange> list = new ArrayList<>();
        list.addAll(fileExchangeData);
        FileExchange file = list.get(rowIndex);
        String value;
        switch (columnIndex) {
            case FILENAME_COL_ID:
                value = file.getFilename();
                break;
            case EXCHANGE_FILE_STATUS_COL_ID:
                value = file.getStatus();
                break;
            default:
                value = "unknown";
        }
        return value;
    }

    /**
     * Add a file to the table model
     * 
     * @param filename  filename to add to the table
     */
    public void add(String filename) {
        fileExchangeData.add(new FileExchange(filename));
    }

    /**
     * Remove one file from the table model
     * 
     * @param filename filename to remove from the table
     */
    public void remove(String filename) {
        fileExchangeData.remove(new FileExchange(filename));
    }    
    
    /**
     * Reload the SshFile table model
     */
    public void reload() {
        this.fireTableDataChanged();
    }

    /**
     * Class that represents a file object. It provides the filename and its exchange status.
     */
    private class FileExchange {

        // Filename
        final private String filename;
        // Transfer status
        final private String status;

        /**
         * Create a simple FileExchange object
         *
         * @param keyId Id of the GPG key
         * @param userId key owner id
         */
        public FileExchange(String filename) {
            this.filename = filename;
            this.status = "Unknown";
        }

        // Return the filename
        private String getFilename() {
            return this.filename;
        }

        // Return the transfer status of this file
        private String getStatus() {
            return this.status;
        }

        @Override
        public boolean equals(Object other) {
            boolean result = false;
            if (((FileExchange) other).getFilename().equals(this.getFilename())) {
                result = true;
            }
            return result;
        }

        @Override
        public int hashCode() {
            return this.filename.hashCode();
        }
    }
}
