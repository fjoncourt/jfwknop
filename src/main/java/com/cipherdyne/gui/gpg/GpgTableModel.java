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
package com.cipherdyne.gui.gpg;

import com.cipherdyne.utils.InternationalizationHelper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;

/**
 *
 */
public class GpgTableModel extends AbstractTableModel {

    // KEY ID is column 0 of the table model
    static public final int KEY_ID_COL = 0;
    
    // USER ID is column 1 of the table model
    static public final int USER_ID_COL = 1;

    // Name of the available columns in the table model
    final private String[] columnNames = {
        InternationalizationHelper.getMessage("i18n.key.id"), 
        InternationalizationHelper.getMessage("i18n.user.id")};

    // List of GPG key that are displayed in the jtable
    private List<SimpleGpgKey> gpgKeyData;
    
    // GPG home directory where to find GPG keys
    final private String gpgHomeDirectory;

    /**
     * Create the GPG table model. The keyring is parsed to provide data to fill the table model
     *
     * @param gpgHomeDirectory GPG home directory where pubring.gpg file can be find
     *
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws org.bouncycastle.openpgp.PGPException
     */
    public GpgTableModel(String gpgHomeDirectory) throws IOException, FileNotFoundException, PGPException {
        super();
        this.gpgHomeDirectory = gpgHomeDirectory;
        parseKeyring();
    }

    /**
     * Read keyring and fetch keys and user ids
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws PGPException
     */
    private void parseKeyring() throws FileNotFoundException, IOException, PGPException {

        // Reset the key table
        this.gpgKeyData = new ArrayList<>();
        
        // Read the keyring
        FileInputStream in = new FileInputStream(this.gpgHomeDirectory + "/pubring.gpg");
        Security.addProvider(new BouncyCastleProvider());
        PGPPublicKeyRingCollection pubRings = new PGPPublicKeyRingCollection(in, new JcaKeyFingerprintCalculator());

        Iterator rIt = pubRings.getKeyRings();
        PGPPublicKey pubKey = null;
        while (rIt.hasNext()) {
            PGPPublicKeyRing pgpPub = (PGPPublicKeyRing) rIt.next();

            try {
                pubKey = pgpPub.getPublicKey();
            } catch (Exception e) {
                Logger.getLogger(GpgTableModel.class.getName()).log(Level.SEVERE, null, e);
                continue;
            }

            Iterator it = pgpPub.getPublicKeys();

            String userId = "unknown";
            String keyId = "unkwown";
            boolean firstKey = true;
            boolean firstId = true;
            while (it.hasNext()) {
                PGPPublicKey pgpKey = (PGPPublicKey) it.next();

                if (firstKey) {
                    keyId = Long.toHexString(pgpKey.getKeyID());
                    firstKey = false;
                }

                Iterator iid = pgpKey.getUserIDs();
                while (iid.hasNext()) {
                    if (firstId) {
                        firstId = false;
                        userId = (String) iid.next();
                    } else {
                        iid.next();
                    }
                }
            }

            this.gpgKeyData.add(new GpgTableModel.SimpleGpgKey(keyId, userId));
        }

        try {
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(GpgTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return this.gpgKeyData.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SimpleGpgKey gpgKey = gpgKeyData.get(rowIndex);
        String value;
        switch (columnIndex) {
            case KEY_ID_COL:
                value = gpgKey.getKeyId();
                break;
            case USER_ID_COL:
                value = gpgKey.getUserId();
                break;
            default:
                value = "unknown";
        }
        return value;
    }

    /**
     * Reload the GPG table model
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * @throws PGPException 
     */
    public void reload() throws IOException, FileNotFoundException, PGPException {
        this.gpgKeyData = new ArrayList<>();
        parseKeyring();
        this.fireTableDataChanged();
    }
    
    /**
     * Class that represents a simple GPG object. It provides the key and its user id.
     */
    private class SimpleGpgKey {

        // Key id as an hexadicmal string
        final private String keyId;
        // Name of the user associated to the key
        final private String userId;

        /**
         * Create a simple GPG object
         *
         * @param keyId Id of the GPG key
         * @param userId key owner id
         */
        public SimpleGpgKey(String keyId, String userId) {
            this.keyId = keyId.toUpperCase();
            this.userId = userId;
        }

        // Return the key id
        private String getKeyId() {
            return this.keyId;
        }

        // Return the user id
        private String getUserId() {
            return this.userId;
        }
    }

}
