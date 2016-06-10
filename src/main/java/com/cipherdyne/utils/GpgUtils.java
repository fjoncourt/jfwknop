/*
 * To change this license header, choose License Headers inPriv Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template inPriv the editor.
 */
package com.cipherdyne.utils;

import com.cipherdyne.gui.gpg.GpgTableModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

/**
 * Utility class that provides GPG helpers
 *
 * @author franck
 */
public class GpgUtils {

    static public void addPrivateKeyToKeyring(String gpgHomeDirectory, String keyringFile) throws FileNotFoundException, IOException, PGPException {
        Security.addProvider(new BouncyCastleProvider());

        // Read user secret keyring
        FileInputStream inPriv = new FileInputStream(gpgHomeDirectory + "/secring.gpg");
        PGPSecretKeyRingCollection privRings = new PGPSecretKeyRingCollection(inPriv, new JcaKeyFingerprintCalculator());

        // Read keys (public and private) from armored file
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
            PGPUtil.getDecoderStream(new FileInputStream(keyringFile)), new JcaKeyFingerprintCalculator());

        // Iterate over the keys
        Iterator keyRingIter = pgpSec.getKeyRings();
        while (keyRingIter.hasNext()) {
            PGPSecretKeyRing keyRing = (PGPSecretKeyRing) keyRingIter.next();
            privRings = PGPSecretKeyRingCollection.addSecretKeyRing(privRings, keyRing);

            Iterator keyIter = keyRing.getSecretKeys();
            while (keyIter.hasNext()) {
                PGPSecretKey key = (PGPSecretKey) keyIter.next();

                if (key.isSigningKey()) {
                    System.out.println("Private key imported " + Long.toHexString(key.getKeyID()).toUpperCase());
                }
            }
        }

        try (FileOutputStream out = new FileOutputStream(new File(gpgHomeDirectory + "/secring.gpg"))) {
            privRings.encode(out);
        }
    }

    static public void addPublicKeyToKeyring(String gpgHomeDirectory, String keyringFile) throws FileNotFoundException, IOException, PGPException {
        Security.addProvider(new BouncyCastleProvider());

        FileInputStream in = new FileInputStream(gpgHomeDirectory + "/pubring.gpg");
        PGPPublicKeyRingCollection pubRings = new PGPPublicKeyRingCollection(in, new JcaKeyFingerprintCalculator());

        PGPPublicKeyRing pgpKeyring = new PGPPublicKeyRing(PGPUtil.getDecoderStream(new FileInputStream(keyringFile)), new JcaKeyFingerprintCalculator());
        pubRings = PGPPublicKeyRingCollection.addPublicKeyRing(pubRings, pgpKeyring);

        try (FileOutputStream out = new FileOutputStream(new File(gpgHomeDirectory + "/pubring.gpg"))) {
            pubRings.encode(out);
        }
    }

    static public void removeKeyFromKeyring(String gpgHomeDirectory, String keyId) throws IOException, PGPException {
        PGPPublicKeyRingCollection pubRings;
        try (FileInputStream in = new FileInputStream(gpgHomeDirectory + "/pubring.gpg")) {
            Security.addProvider(new BouncyCastleProvider());
            pubRings = new PGPPublicKeyRingCollection(in, new JcaKeyFingerprintCalculator());
            Iterator it = pubRings.getKeyRings();
            while (it.hasNext()) {
                PGPPublicKeyRing pgpKeyring = (PGPPublicKeyRing) it.next();
                String crtId = Long.toHexString(pgpKeyring.getPublicKey().getKeyID()).toUpperCase();
                if (keyId.equals(crtId)) {
                    pubRings = PGPPublicKeyRingCollection.removePublicKeyRing(pubRings, pgpKeyring);
                }
            }
        }

        try (FileOutputStream out = new FileOutputStream(new File(gpgHomeDirectory + "/pubring.gpg"))) {
            pubRings.encode(out);
        }
    }

    /**
     * Export a GPG key (armored) from a GPG home directory to a filename
     * 
     * @param gpgHomeDirectory GPG home directory where to look up the GPG key
     * @param keyId GPG key to look up
     * @param filename Filename to save the GPG key to
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @throws PGPException 
     */
    static public void exportKey(String gpgHomeDirectory, String keyId, String filename) throws FileNotFoundException, IOException, PGPException {
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
            while (it.hasNext()) {
                PGPPublicKey pgpKey = (PGPPublicKey) it.next();

                if (firstKey) {
                    String crtkeyid = Long.toHexString(pgpKey.getKeyID()).toUpperCase();
                    if (crtkeyid.equals(keyId)) {
                        ByteArrayOutputStream encOut = new ByteArrayOutputStream();
                        try (ArmoredOutputStream armorOut = new ArmoredOutputStream(encOut)) {
                            armorOut.write(pgpKey.getEncoded());
                            armorOut.flush();
                        }
                        try (FileOutputStream out = new FileOutputStream(new File(filename))) {
                            out.write(encOut.toByteArray());
                            out.flush();
                        }
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

    public static void createKey(String gpgHomeDirectory, String userId, String passphrase) {
        try {
            Security.addProvider(new BouncyCastleProvider());

            // Create the main key set
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(1024);
            KeyPair pair = keyGen.generateKeyPair();

            // Create output stream
            // FIXME : should not be files
            OutputStream pubOut = new FileOutputStream(new File("public.asc"));
            OutputStream privOut = new FileOutputStream(new File("private.asc"));

            // Create the private key with the provided settings
            privOut = new ArmoredOutputStream(privOut);
            PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1);
            PGPKeyPair keyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, pair, new Date());
            PGPSecretKey secretKey = new PGPSecretKey(PGPSignature.DEFAULT_CERTIFICATION, keyPair,
                userId, sha1Calc, null, null,
                new JcaPGPContentSignerBuilder(keyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1),
                new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.CAST5, sha1Calc).setProvider("BC").build(passphrase.toCharArray()));
            secretKey.encode(privOut);
            privOut.close();

            // Create Public key
            pubOut = new ArmoredOutputStream(pubOut);
            PGPPublicKey key = secretKey.getPublicKey();
            key.encode(pubOut);
            pubOut.close();

            GpgUtils.addPublicKeyToKeyring(gpgHomeDirectory, "public.asc");
            GpgUtils.addPrivateKeyToKeyring(gpgHomeDirectory, "private.asc");
            
        } catch (NoSuchAlgorithmException | IOException | PGPException | NoSuchProviderException ex) {
            Logger.getLogger(GpgUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
