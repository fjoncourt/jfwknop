/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.jfwknop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
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
}
