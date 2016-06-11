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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franck
 */
public class ExternalCommand {

    private final String commandLine;

    public ExternalCommand(String commandLine) {
        this.commandLine = commandLine;
    }

    public void executeRuntime() {
        Runtime rt = Runtime.getRuntime();
        try {
            Process proc = rt.exec(this.commandLine);
        } catch (IOException ex) {
            Logger.getLogger(ExternalCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void execute() {
        try {
            List<String> args = new ArrayList<String>();
            args.addAll(Arrays.asList(this.commandLine.split(" ")));

            ProcessBuilder pb = new ProcessBuilder(args);
            pb.inheritIO();
            Process p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
