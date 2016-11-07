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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Class that helps to manage and create a fwknopd access file.
 */
public class AccessFile {

    static final Logger LOGGER = LogManager.getLogger(AccessFile.class.getName());

    // Default fwknopd GPG home directory - Generally it is /root/.gnupg
    static final String DEFAULT_FWKNOPD_GPG_HOMEDIR = "/root/.gnupg";

    private final String filename;

    /**
     * Access file constructor
     *
     * @param filename filepath of the access file
     */
    public AccessFile(String filename) {
        this.filename = filename;
    }

    /**
     * Generate an access file from a map of fwknop rc keys
     *
     * @param fwknopConfig map of fwknoprc keys to use to generate the access file
     */
    public void generate(Map<EnumFwknopRcKey, String> fwknopConfig) {
        final Path fileP = Paths.get(this.filename);
        final Charset charset = Charset.forName("utf-8");

        try (BufferedWriter writer = Files.newBufferedWriter(fileP, charset)) {

            // Configure "SOURCE" key first as this is requested by fwknopd server when parsing access file
            if (fwknopConfig.get(EnumFwknopRcKey.ALLOW_IP) != null) {
                writer.write(generateAccessLine(EnumFwknopRcKey.ALLOW_IP.getRemoteKey(), fwknopConfig.get(EnumFwknopRcKey.ALLOW_IP)));
                fwknopConfig.remove(EnumFwknopRcKey.ALLOW_IP);
            }

            // Configure other keys
            for (final Map.Entry<EnumFwknopRcKey, String> entry : fwknopConfig.entrySet()) {
                if (!entry.getValue().isEmpty() && (entry.getKey().getRemoteKey() != null)) {
                    EnumFwknopdRcKey fwknopdKey = entry.getKey().getRemoteKey();
                    writer.write(generateAccessLine(fwknopdKey, entry.getValue()));
                    if (fwknopdKey.equals(EnumFwknopdRcKey.GPG_DECRYPT_ID)) {
                        writer.write(generateAccessLine(EnumFwknopdRcKey.GPG_DECRYPT_PW, "__CHANGEME__"));
                        writer.write(generateAccessLine(EnumFwknopdRcKey.GPG_HOME_DIR, DEFAULT_FWKNOPD_GPG_HOMEDIR));
                    } else if (fwknopdKey.equals(EnumFwknopdRcKey.GPG_REMOTE_ID)) {
                        writer.write(generateAccessLine(EnumFwknopdRcKey.GPG_REQUIRE_SIG, "Y"));
                        writer.write(generateAccessLine(EnumFwknopdRcKey.GPG_IGNORE_SIG_VERIFY_ERROR, "N"));
                    }
                }
            }
        } catch (final IOException e) {
            LOGGER.error("Unable to create access file: " + this.filename, e);
        }
    }

    /**
     * Create a line for an access conf file.
     *
     * @param key Key to set
     * @param value Value to set for the key
     * @return an access file line as a string ready to be stored
     */
    private String generateAccessLine(EnumFwknopdRcKey key, String value) {
        String fwknopdValue;
        switch (key) {
            case GPG_REMOTE_ID:
                fwknopdValue = String.format("%-32s    %s\n", key.toString(), value.substring(value.length() - 8, value.length()));
                break;
            default:
                fwknopdValue = String.format("%-32s    %s\n", key.toString(), value);
        }
        return fwknopdValue;
    }
}
