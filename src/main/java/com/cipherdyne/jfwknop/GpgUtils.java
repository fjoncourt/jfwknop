/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.jfwknop;

import com.cipherdyne.gui.gpg.GpgTableModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Security;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;

/**
 *
 * @author franck
 */
public class GpgUtils {

    static public void addKeyToKeyring(String gpgHomeDirectory, String keyringFile) throws FileNotFoundException, IOException, PGPException {
        FileInputStream in = new FileInputStream(gpgHomeDirectory + "/pubring.gpg");
        Security.addProvider(new BouncyCastleProvider());
        PGPPublicKeyRingCollection pubRings = new PGPPublicKeyRingCollection(in, new JcaKeyFingerprintCalculator());

        PGPPublicKeyRing pgpKeyring = new PGPPublicKeyRing(PGPUtil.getDecoderStream(new FileInputStream(keyringFile)), new JcaKeyFingerprintCalculator());
        pubRings = PGPPublicKeyRingCollection.addPublicKeyRing(pubRings, pgpKeyring);
        FileOutputStream out = new FileOutputStream(new File(gpgHomeDirectory + "/pubring.gpg"));
        pubRings.encode(out);
        out.close();
    }

    static public void exportKey(String gpgHomeDirectory, String keyId) throws FileNotFoundException, IOException, PGPException {
        FileInputStream in = new FileInputStream(gpgHomeDirectory + "/pubring.gpg");
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

            boolean firstKey = true;
            boolean firstId = true;
            while (it.hasNext()) {
                PGPPublicKey pgpKey = (PGPPublicKey) it.next();

                if (firstKey) {
                    String crtkeyid = Long.toHexString(pgpKey.getKeyID()).toUpperCase();
                    if (crtkeyid.equals(keyId)) {
                        ByteArrayOutputStream encOut = new ByteArrayOutputStream();
                        ArmoredOutputStream armorOut
                            = new ArmoredOutputStream(encOut);
                        armorOut.write(pgpKey.getEncoded());
                        armorOut.flush();
                        armorOut.close();
                        FileOutputStream out = new FileOutputStream(new File(keyId + ".asc"));
                        out.write(encOut.toByteArray());
                        out.flush();
                        out.close();
                        //System.out.println(new String(encOut.toByteArray()));
                    }
                    firstKey = false;
                }
            }
        }

        try {
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(GpgTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
