/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    private final String filename;
        
    public AccessFile(String filename) {
      this.filename = filename;
    }
    
    public void generate(Map<EnumFwknopRcKey, String> fwknopConfig) {
               final Path fileP = Paths.get(this.filename);
        final Charset charset = Charset.forName("utf-8");

        try (BufferedWriter writer = Files.newBufferedWriter(fileP, charset)) {
            for (final Map.Entry<EnumFwknopRcKey, String> entry : fwknopConfig.entrySet()) {
                if (!entry.getValue().isEmpty() && (entry.getKey().getRemoteKey() != null)) {
                    writer.write(entry.getKey().getRemoteKey().toString() + "\t\t" + entry.getValue() + "\n");
                }
            }
        } catch (final IOException e) {
            LOGGER.error("Unable to create access file: " + this.filename, e);
        }   
    }
}
