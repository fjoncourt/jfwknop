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
