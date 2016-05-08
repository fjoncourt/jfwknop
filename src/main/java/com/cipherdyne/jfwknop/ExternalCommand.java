/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
