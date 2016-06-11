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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RcFile {

    private String filepath;
    static final Logger LOGGER = LogManager.getLogger(RcFile.class.getName());

    private Map<EnumFwknopRcKey, String> config = new HashMap<>();

    public RcFile(final String filepath) {
        this.filepath = filepath;
    }

    public boolean parse() {

        try (BufferedReader reader = new BufferedReader(new FileReader(this.filepath))) {

            final Pattern pattern = Pattern.compile("^(?!#)(.*)\\s(.*)");
            String line;
            EnumFwknopRcKey key;

            while ((line = reader.readLine()) != null) {
                final Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    try {
                        key = EnumFwknopRcKey.valueOf(matcher.group(1).trim());
                    } catch (final java.lang.IllegalArgumentException e) {
                        LOGGER.warn("Unsupported variable: " + e.getMessage());
                        continue;
                    }
                    this.config.put(key, matcher.group(2).trim());
                }
            }
            reader.close();

        } catch (final IOException e) {
            LOGGER.error("Unable to open rc file : " + e.getMessage());
        }

        return true;
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
            writer.write("[default]\n");
            for (final Entry<EnumFwknopRcKey, String> entry : this.config.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    writer.write(entry.getKey().toString() + "\t\t" + entry.getValue() + "\n");
                }
            }
        } catch (final IOException e) {
            LOGGER.error("Unable to save rc file " + this.filepath, e);
        }

        fixPermissions();

    }

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
}
