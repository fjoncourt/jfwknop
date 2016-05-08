/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.jfwknop;

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
 * @author franck
 */
public class JFwknopConfig {

    static final Logger logMgr = LogManager.getLogger(JFwknopConfig.class.getName());

    private static JFwknopConfig instance = null;

    private static final String CONFIG_PROPERTIES = "config.properties";
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
            this.configProperties.load(new FileInputStream(CONFIG_PROPERTIES));
        } catch (final IOException e) {
            logMgr.error("Unable to load configuration file (" + CONFIG_PROPERTIES + ") : " + e.getMessage());
        }

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
        this.configKeyMap.put(EnumFwknopConfigKey.LANGUAGE, this.configProperties.getProperty(EnumFwknopConfigKey.LANGUAGE.getKey()));
        this.configKeyMap.put(EnumFwknopConfigKey.FWKNOP_VERBOSE, this.configProperties.getProperty(EnumFwknopConfigKey.FWKNOP_VERBOSE.getKey()));
        this.configKeyMap.put(EnumFwknopConfigKey.FWKNOP_FILEPATH, this.configProperties.getProperty(EnumFwknopConfigKey.FWKNOP_FILEPATH.getKey()));
        this.configKeyMap.put(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS, this.configProperties.getProperty(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS.getKey()));
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
     * Add a file path to the recent file lists if it does not exists. If added,
     * the oldest file is removed from the list. The list is automatically
     * saved.
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

    /**
     * Save current configuration to the properties file
     */
    public void saveConfig() {

        // Update the recent file list in the properties file context
        for (int ix = 0; ix < recentFiles.size(); ix++) {
            this.configProperties.setProperty(CONFIG_RECENT_FILE_PREFIX + (ix + 1), recentFiles.get(ix));
        }

        // Update the fwknop client settings in the properties file context
        this.configProperties.setProperty(EnumFwknopConfigKey.LANGUAGE.getKey(), this.configKeyMap.get(EnumFwknopConfigKey.LANGUAGE));
        this.configProperties.setProperty(EnumFwknopConfigKey.FWKNOP_VERBOSE.getKey(), this.configKeyMap.get(EnumFwknopConfigKey.FWKNOP_VERBOSE));
        this.configProperties.setProperty(EnumFwknopConfigKey.FWKNOP_FILEPATH.getKey(), this.configKeyMap.get(EnumFwknopConfigKey.FWKNOP_FILEPATH));
        this.configProperties.setProperty(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS.getKey(), this.configKeyMap.get(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS));

        // Store settings
        try {
            this.configProperties.store(new FileOutputStream(CONFIG_PROPERTIES), null);
        } catch (IOException ex) {
            logMgr.error("Unable to save jfwknop configuration file (" + CONFIG_PROPERTIES + ") : " + ex.getMessage());
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
