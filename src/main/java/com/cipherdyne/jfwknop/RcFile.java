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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RcFile {

    // Pattern that matches a stanza
    final private static Pattern STANZA_PATTERN = Pattern.compile("^\\[(.*)\\]");

    // Pattern that matches a key/value line
    final private static Pattern KEY_PATTERN = Pattern.compile("^(?!#)(.*)\\s+(.*)");

    // Default stanza used by fwknop client in fwknoprc file to provide common settings for all stanzas
    final public static String DEFAULT_STANZA = "default";

    // Path to the rc file
    private String filepath;

    // Static logger for this class
    static final Logger LOGGER = LogManager.getLogger(RcFile.class.getName());

    // List of key found in the rc file once parsed
    private Map<EnumFwknopRcKey, String> config;

    /**
     * Constructor
     *
     * @param filepath rc file patch
     */
    public RcFile(final String filepath) {
        this.filepath = filepath;
    }

    /**
     * Parse rc file and initialize context with key/value found in default and selected stanza
     *
     * @param selectedStanza Stanza to load settings from along with the default stanza settings
     * @throws IOException
     */
    public void parse(String selectedStanza) throws IOException {

        if (selectedStanza == null) {
            selectedStanza = DEFAULT_STANZA;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(this.filepath))) {

            String line;
            EnumFwknopRcKey key;
            String currentStanza = StringUtils.EMPTY;
            this.config = new HashMap<>();

            // Iterate over all lines in the rc file
            while ((line = reader.readLine()) != null) {

                // Detect stanza and set it as current to avoid parsing unwanted stanza
                final Matcher stanzaMatcher = STANZA_PATTERN.matcher(line);
                if (stanzaMatcher.find()) {
                    currentStanza = stanzaMatcher.group(1).trim();
                    continue;
                }

                // Parse only line for default and selected stanza
                if (DEFAULT_STANZA.equals(currentStanza) || selectedStanza.equals(currentStanza)) {
                    final Matcher keyMatcher = KEY_PATTERN.matcher(line.trim());
                    if (keyMatcher.find()) {
                        try {
                            key = EnumFwknopRcKey.valueOf(keyMatcher.group(1).trim());
                        } catch (final java.lang.IllegalArgumentException e) {
                            LOGGER.warn("Unsupported variable: " + e.getMessage());
                            continue;
                        }
                        this.config.put(key, keyMatcher.group(2).trim());
                    }
                }
            }

            reader.close();

        } catch (final IOException e) {
            LOGGER.error("Unable to open rc file : " + e.getMessage());
            throw (e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (final Map.Entry<EnumFwknopRcKey, String> entry : this.config.entrySet()) {
            sb.append(" * " + entry.getKey().toString() + " = " + entry.getValue() + "\n");
        }

        return sb.toString();
    }

    public void save() {

        final Path fileP = Paths.get(this.filepath);
        final Charset charset = Charset.forName("utf-8");

        try (BufferedWriter writer = Files.newBufferedWriter(fileP, charset)) {
            writer.write("[" + DEFAULT_STANZA + "]\n");
            for (final Entry<EnumFwknopRcKey, String> entry : this.config.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    writer.write(generateFwknoprcLine(entry.getKey(), entry.getValue()));
                }
            }
        } catch (final IOException e) {
            LOGGER.error("Unable to save rc file " + this.filepath, e);
        }

        fixPermissions();

    }

    /**
     * Configure read/write persmissions for local user only.
     *
     * Rc file contains passwords used to authenticate on fwknop server. We ensure the file
     * permissions as set accordingly
     */
    private void fixPermissions() {
        File file = new File(this.filepath);

        // Clear all permissions for all users
        file.setReadable(false, false);
        file.setWritable(false, false);
        file.setExecutable(false, false);

        // Set owner permissions
        file.setReadable(true, true);
        file.setWritable(true, true);
    }

    public void saveAs(final String filename) {
        this.filepath = filename;
        save();
    }

    public void setConfig(final Map<EnumFwknopRcKey, String> context) {
        this.config = context;
    }

    public Map<EnumFwknopRcKey, String> getConfig() {
        return this.config;
    }

    /**
     * @return the rc filename
     */
    public String getRcFilename() {
        return this.filepath;
    }

    /**
     * Look up the lsit of stanzas available in the rc file
     *
     * @return the list of stanza available in the rc file except the default stanza
     * @throws IOException
     */
    public List<String> lookUpStanza() throws IOException {
        List<String> stanzaList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.filepath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                final Matcher matcher = STANZA_PATTERN.matcher(line);

                if (matcher.find()) {
                    String stanza = matcher.group(1).trim();
                    if (!DEFAULT_STANZA.equals(stanza)) {
                        stanzaList.add(stanza);
                    }
                }
            }

            reader.close();

        } catch (final IOException e) {
            LOGGER.error("Unable to open rc file *" + this.filepath + "* :" + e.getMessage());
            throw (e);
        }

        return stanzaList;
    }

    /**
     * Create a line for an fwknoprc conf file.
     *
     * @param key Key to set
     * @param value Value to set for the key
     * @return an access file line as a string ready to be stored
     */
    private String generateFwknoprcLine(EnumFwknopRcKey key, String value) {
        return String.format("%-32s    %s\n", key.toString(), value);
    }
}
