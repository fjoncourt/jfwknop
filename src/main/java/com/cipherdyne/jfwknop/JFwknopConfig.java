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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Franck Joncourt
 */
public class JFwknopConfig {

    static final Logger LOGGER = LogManager.getLogger(JFwknopConfig.class.getName());

    private static JFwknopConfig instance = null;

    private static final String JFWKNOP_DIRECTORY = ".jfwknop";
    private static final String JFWKNOP_CONFIG_PROPERTIES = ".jfwknoprc";
    private final SortedProperties configProperties;
    private static final String CONFIG_RECENT_FILE_PREFIX = "recentFile";

    private static final int MAX_RECENT_FILES = 5;
    private final RecentFiles recentFiles;

    private Map<EnumFwknopConfigKey, String> configKeyMap;

    /**
     * Create a new JFwknop confguration file instance
     */
    private JFwknopConfig() {
        this.configKeyMap = new HashMap<>();
        this.configProperties = new SortedProperties();
        this.recentFiles = new RecentFiles(MAX_RECENT_FILES);

        // Opening properties file
        try {
            this.configProperties.load(new FileInputStream(this.getJfwknoprcFilepath()));
        } catch (final IOException e) {
            LOGGER.error("Unable to load configuration file (" + this.getJfwknoprcFilepath() + ") : " + e.getMessage());
            LOGGER.error("A default configuration file is created");
            createDefaultDirectory();
            createDefaultConfig(this.getJfwknoprcFilepath());
        }

        readConfig();
    }

    /**
     * @return Jfwknop rc filepath according to the user home directory
     */
    static private String getJfwknoprcFilepath() {
        return getJfwknopWorkingDirectory() + JFWKNOP_CONFIG_PROPERTIES;
    }

    /**
     * @return JFwknop working directory
     */
    static public String getJfwknopWorkingDirectory() {
        return System.getProperty("user.home")
            + System.getProperty("file.separator")
            + JFWKNOP_DIRECTORY
            + System.getProperty("file.separator");
    }

    public static JFwknopConfig getInstance() {
        if (instance == null) {
            instance = new JFwknopConfig();
        }
        return instance;
    }

    /**
     * @return the list of fwknoprc file recently opened
     */
    public RecentFiles getRecentFileList() {
        return this.recentFiles;
    }

    /**
     * Add a file path to the recent file lists if it does not exists. If added, the oldest file is
     * removed from the list. The list is automatically saved.
     *
     * @param filepath File path to add to the list
     */
    public void addRecentFile(String filepath) {

        // Add the entry id if it does not exist.
        if (!this.recentFiles.contains(filepath)) {
            this.recentFiles.addFirst(filepath);
            if (this.recentFiles.size() > MAX_RECENT_FILES) {
                this.recentFiles.removeLast();
            }
        } else {
            this.recentFiles.remove(filepath);
            this.recentFiles.addFirst(filepath);
        }

        saveConfig();
    }

    public Map<EnumFwknopConfigKey, String> getConfigKey() {
        return this.configKeyMap;
    }

    private void createDefaultConfig(String filename) {
        // Set default fwknop client settings
        this.configKeyMap.put(EnumFwknopConfigKey.LANGUAGE, "en_EN");
        this.configKeyMap.put(EnumFwknopConfigKey.FWKNOP_VERBOSE, "1");
        this.configKeyMap.put(EnumFwknopConfigKey.FWKNOP_FILEPATH, "/usr/bin/fwknop");
        this.configKeyMap.put(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS, "");

        // Set default key settings
        this.configKeyMap.put(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH, "16");
        this.configKeyMap.put(EnumFwknopConfigKey.KEY_HMAC_LENGTH, "16");
        this.configKeyMap.put(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH, "32");
        this.configKeyMap.put(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH, "64");

        saveConfig();
    }

    /**
     * Save current configuration to the properties file
     */
    public void saveConfig() {

        // Update the recent file list in the properties file context
        for (int ix = 0; ix < recentFiles.size(); ix++) {
            this.configProperties.setProperty(CONFIG_RECENT_FILE_PREFIX + (ix + 1), recentFiles.get(ix));
        }

        // Update the fwknop client settings in the properties file context
        this.configProperties.setProperty(EnumFwknopConfigKey.LANGUAGE.getKey(),
            this.configKeyMap.get(EnumFwknopConfigKey.LANGUAGE));
        this.configProperties.setProperty(EnumFwknopConfigKey.FWKNOP_VERBOSE.getKey(),
            this.configKeyMap.get(EnumFwknopConfigKey.FWKNOP_VERBOSE));
        this.configProperties.setProperty(EnumFwknopConfigKey.FWKNOP_FILEPATH.getKey(),
            this.configKeyMap.get(EnumFwknopConfigKey.FWKNOP_FILEPATH));
        this.configProperties.setProperty(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS.getKey(),
            this.configKeyMap.get(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS));

        this.configProperties.setProperty(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH.getKey(),
            this.configKeyMap.get(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH));
        this.configProperties.setProperty(EnumFwknopConfigKey.KEY_HMAC_LENGTH.getKey(),
            this.configKeyMap.get(EnumFwknopConfigKey.KEY_HMAC_LENGTH));
        this.configProperties.setProperty(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH.getKey(),
            this.configKeyMap.get(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH));
        this.configProperties.setProperty(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH.getKey(),
            this.configKeyMap.get(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH));

        // Store settings
        try {
            this.configProperties.store(new FileOutputStream(this.getJfwknoprcFilepath()), null);
        } catch (IOException ex) {
            LOGGER.error("Unable to save jfwknop configuration file (" + this.getJfwknoprcFilepath() + ") : " + ex.getMessage());
        }
    }

    /**
     * Read config from the configuration file previously opened
     */
    private void readConfig() {
        // Build fwknoprc recent file list
        int ix = MAX_RECENT_FILES;
        do {
            final String filename = this.configProperties.getProperty(CONFIG_RECENT_FILE_PREFIX + ix);
            if (filename != null) {
                this.recentFiles.add(filename);
            }
            ix--;
        } while (ix != 0);

        // Read fwknop client settings
        this.configKeyMap.put(EnumFwknopConfigKey.LANGUAGE,
            this.configProperties.getProperty(EnumFwknopConfigKey.LANGUAGE.getKey()));
        this.configKeyMap.put(EnumFwknopConfigKey.FWKNOP_VERBOSE,
            this.configProperties.getProperty(EnumFwknopConfigKey.FWKNOP_VERBOSE.getKey()));
        this.configKeyMap.put(EnumFwknopConfigKey.FWKNOP_FILEPATH,
            this.configProperties.getProperty(EnumFwknopConfigKey.FWKNOP_FILEPATH.getKey()));
        this.configKeyMap.put(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS,
            this.configProperties.getProperty(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS.getKey()));

        // Read the key settings
        this.configKeyMap.put(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH,
            this.configProperties.getProperty(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH.getKey()));
        this.configKeyMap.put(EnumFwknopConfigKey.KEY_HMAC_LENGTH,
            this.configProperties.getProperty(EnumFwknopConfigKey.KEY_HMAC_LENGTH.getKey()));
        this.configKeyMap.put(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH,
            this.configProperties.getProperty(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH.getKey()));
        this.configKeyMap.put(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH,
            this.configProperties.getProperty(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH.getKey()));
    }

    /**
     * Create the default jfwknop directory where configuration files are stored
     */
    private void createDefaultDirectory() {
        File directory = new File(getJfwknopWorkingDirectory());
        if (!directory.exists()) {
            if (directory.mkdirs()) {
            } else {
                LOGGER.error("Unable to create JFwknop user directory: " + getJfwknopWorkingDirectory());
            }
        }
    }

    class SortedProperties extends Properties {

        public Enumeration keys() {
            Enumeration keysEnum = super.keys();
            Vector<String> keyList = new Vector<String>();
            while (keysEnum.hasMoreElements()) {
                keyList.add((String) keysEnum.nextElement());
            }
            Collections.sort(keyList);
            return keyList.elements();
        }
    }
}
