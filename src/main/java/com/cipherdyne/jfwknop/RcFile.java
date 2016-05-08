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
    static final Logger logger = LogManager.getLogger(MainWindowController.class.getName());

    // @Getter
    // @Setter
    private Map<EnumFwknopRcKey, String> config = new HashMap<EnumFwknopRcKey, String>();

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
                        logger.warn("Unsupported variable: " + e.getMessage());
                        continue;
                    }
                    this.config.put(key, matcher.group(2).trim());
                }
            }
            reader.close();

        } catch (final IOException e) {
            logger.error("Unable to open rc file : " + e.getMessage());
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
            e.printStackTrace();
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

}
