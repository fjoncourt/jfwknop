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
package com.cipherdyne.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
 * @author Franck Joncourt
 */
public class GpgUtils {

    static final Logger LOGGER = LogManager.getLogger(GpgUtils.class.getName());

    static private final String GPG1_PUBLIC_KEYRING = "pubring.gpg";
    static private final String GPG1_PRIVATE_KEYRING = "secring.gpg";

    static public void addPrivateKeyToKeyring(final String gpgHomeDirectory, final String keyringFile) throws FileNotFoundException, IOException, PGPException {
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
                LOGGER.error(e);
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
            LOGGER.error(ex);
        }
    }

    /**
     * Import a GPG v1 private key from an armored file to a GPG v2 keyring
     *
     * BouncyCastle does not support GPG V2 and is only able to create GPG v1 key. This methods
     * provides a workaround to import a GPG V1 key to an existing GPG V2 keyring
     *
     * @param gpgHomeDirectory GPG home directory
     * @param privateKeyFile path to the armored file where the private key is stored
     * @param passphrase Passphrase of the key to import
     */
    private static void addPrivateKeyToGpg2Keyring(final String gpgHomeDirectory, final String privateKeyFile, final String passphrase) {

        // Create the command as a string
        String gpgCmd = "/usr/bin/gpg --homedir " + gpgHomeDirectory + " --batch --passphrase " + passphrase + " -a --import " + privateKeyFile;

        // Build the process and run it
        ProcessBuilder pb = new ProcessBuilder(gpgCmd.split(" "));
        pb = pb.redirectErrorStream(true);
        try {
            Process p = pb.start();
        } catch (IOException ex) {
            LOGGER.error("Unable to import secret key to gnupg v2 keyring");
        }
    }

    public static void createKey(final String gpgHomeDirectory, final String userId, final String passphrase) {
        try {
            Security.addProvider(new BouncyCastleProvider());

            // Create the main key set
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(2048);
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

            addPublicKeyToKeyring(gpgHomeDirectory, "public.asc");
            addPrivateKeyToKeyring(gpgHomeDirectory, "private.asc");
            addPrivateKeyToGpg2Keyring(gpgHomeDirectory, "private.asc", passphrase);

        } catch (NoSuchAlgorithmException | IOException | PGPException | NoSuchProviderException ex) {
            LOGGER.error(ex);
        }
    }

    /**
     * Create an empty GPG V1 home directory with public and private empty keyrings
     *
     * @param homeDirectory Full path to the newly GNUPG home directory to create
     */
    public static void createGpg1HomeDirectory(final String homeDirectory) {
        List<String> files = new ArrayList<>(Arrays.asList(
            homeDirectory + System.getProperty("file.separator") + GPG1_PUBLIC_KEYRING,
            homeDirectory + System.getProperty("file.separator") + GPG1_PRIVATE_KEYRING));
        File directory = new File(homeDirectory);

        if (!directory.exists()) {
            if (directory.mkdirs()) {
                // Clear all permissions for all users
                directory.setReadable(false, false);
                directory.setWritable(false, false);
                directory.setExecutable(false, false);

                // Set owner permissions
                directory.setReadable(true, true);
                directory.setWritable(true, true);
                directory.setExecutable(true, true);
                try {

                    for (String keyringPath : files) {

                        Files.createFile(Paths.get(keyringPath));
                        File keyringFile = new File(keyringPath);

                        // Clear all permissions for all users
                        keyringFile.setReadable(false, false);
                        keyringFile.setWritable(false, false);
                        keyringFile.setExecutable(false, false);

                        // Set owner permissions
                        keyringFile.setReadable(true, true);
                        keyringFile.setWritable(true, true);
                    }
                } catch (IOException e) {
                    LOGGER.error("Unable to create JFwknop GPG home directory: ", e);
                }
            } else {
                LOGGER.error("Unable to create JFwknop GPG home directory: " + homeDirectory);
            }
        }
    }
}
