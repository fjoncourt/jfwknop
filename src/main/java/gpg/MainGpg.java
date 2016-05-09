/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gpg;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Security;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import static org.bouncycastle.openpgp.examples.PubringDump.getAlgorithm;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.util.encoders.Hex;

/**
 *
 * @author franck
 */
public class MainGpg {

    public static void main(final String[] args) {
        FileInputStream in = null;
        try {
            in = new FileInputStream("/home/franck/.gnupg/pubring.gpg");
            Security.addProvider(new BouncyCastleProvider());
            PGPPublicKeyRingCollection pubRings = new PGPPublicKeyRingCollection(in, new JcaKeyFingerprintCalculator());
            
            
            Iterator rIt = pubRings.getKeyRings();
            PGPPublicKey pubKey = null;
            while (rIt.hasNext()) {
                PGPPublicKeyRing pgpPub = (PGPPublicKeyRing) rIt.next();

                try {
                    pubKey = pgpPub.getPublicKey();
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                Iterator it = pgpPub.getPublicKeys();
                boolean first = true;
                while (it.hasNext()) {
                    PGPPublicKey pgpKey = (PGPPublicKey) it.next();

                    if (first) {
                        System.out.println("Key ID: " + Long.toHexString(pgpKey.getKeyID()));
                        first = false;
                    } else {
                        System.out.println("Key ID: " + Long.toHexString(pgpKey.getKeyID()) + " (subkey)");
                    }
                    
                    
                    Iterator iid = pgpKey.getUserIDs();
                    while (iid.hasNext()) {
                        System.out.println("            Id: " + (String) iid.next());
                    }

                    System.out.println("            Algorithm:   " + getAlgorithm(pgpKey.getAlgorithm()));
                    System.out.println("            Fingerprint: " + new String(Hex.encode(pgpKey.getFingerprint())));
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(MainGpg.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(MainGpg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
