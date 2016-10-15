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

import com.cipherdyne.gui.IConsole;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * This class intends to provide an interface to run external commands periodically or only once.
 */
public class ExternalCommand implements Runnable {

    // Logger
    static final Logger LOGGER = LogManager.getLogger(ExternalCommand.class.getName());

    // Command line to execute - can be more than one arguments - space separated list
    private final String[] args;

    // Timeout before two executions - Set to 0 to run the only command once
    private final long period;

    // Set to false when a periodic command has to be stoppped
    private boolean isRunning;

    // IConsole interface used to log external command output
    private final IConsole console;

    /**
     * External command constructor that is executed only once
     *
     * @param args List of argument to use to build the process
     * @param console IConsole appender to log command output
     */
    public ExternalCommand(final String[] args, IConsole console) {
        this.args = args;
        this.period = 0;
        this.console = console;
        this.isRunning = false;
    }

    /**
     * External command constructor that runs a command periodically.
     *
     * @param args List of argument to use to build the process
     * @param period Period between two command executions
     * @param console IConsole appender to log command output
     */
    public ExternalCommand(final String[] args, long period, IConsole console) {
        this.args = args;
        this.period = period;
        this.console = console;
        this.isRunning = false;
    }

    /**
     * Append a message to the specified console to trace changes if configured
     *
     * @param msg Message to log to the console
     */
    private void appendToConsole(final String msg) {
        if (this.console != null) {
            this.console.appendToConsole(msg);
        }
    }

    @Override
    public void run() {

        this.isRunning = true;

        try {
            while (this.isRunning) {

                appendToConsole("[*] Executing : " + Arrays.toString(this.args));

                // Build the process and run it
                ProcessBuilder pb = new ProcessBuilder(args);
                pb = pb.redirectErrorStream(true);
                Process p = pb.start();
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

                // For each line, send the message to the console appender
                String line;
                while ((line = br.readLine()) != null) {
                    appendToConsole(line);
                }

                // Check if we run only once or if we wait a bit before running the command again
                // TODO: can be achieve with a timer
                if (period == 0) {
                    isRunning = false;
                } else {
                    Thread.sleep(this.period * 1000);
                }
            }

        } catch (IOException | InterruptedException e) {
            LOGGER.error("[*] Unable to execute : " + Arrays.toString(this.args), e);
            appendToConsole("[*] Unable to execute : " + Arrays.toString(this.args) + "\n" + e.getMessage());
        }
    }

    /**
     * Stop the curent command. This method has to be executed when the command is run periodically
     * to be able to stop the process
     */
    public void stop() {
        this.isRunning = false;
    }
}
