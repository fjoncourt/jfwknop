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
package com.cipherdyne.model;

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.jfwknop.EnumFwknopConfigKey;
import com.cipherdyne.jfwknop.JFwknopConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FwknopClientModel {

    static final Logger logger = LogManager.getLogger(FwknopClientModel.class.getName());

    private final MainWindowView view;
    private final Map<EnumFwknopConfigKey, String> fwknopConfig = new HashMap<>();

    public FwknopClientModel(final MainWindowView view) {
        this.view = view;
        this.fwknopConfig.put(EnumFwknopConfigKey.FWKNOP_FILEPATH, JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.FWKNOP_FILEPATH));
        this.fwknopConfig.put(EnumFwknopConfigKey.FWKNOP_ARGS, "");
        this.fwknopConfig.put(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS, JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS));
        this.fwknopConfig.put(EnumFwknopConfigKey.FWKNOP_VERBOSE, JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.FWKNOP_VERBOSE));
        updateListeners();
    }

    private void updateListeners() {
        this.view.onFwknopConfigChange(this.fwknopConfig);
    }

    /**
     * Store the key in the fwknop client model context. The value is not saved.
     *
     * @param key Key to store
     * @param value New value od the key to store in the context
     */
    public void setFwknopConfig(final EnumFwknopConfigKey key, final String value) {
        this.fwknopConfig.put(key, value);
    }

    /**
     * Save the fwknop client settings.
     *
     * The fwknop client settings are stored in the JFwknop configuration file
     */
    public void save() {
        
        // Get jfwknop configuration keys
        Map<EnumFwknopConfigKey, String> jfwknopConfig = JFwknopConfig.getInstance().getConfigKey();

        // If available store the keys
        for (final Map.Entry<EnumFwknopConfigKey, String> entry : this.fwknopConfig.entrySet()) {
            if (jfwknopConfig.containsKey(entry.getKey())) {
                jfwknopConfig.put(entry.getKey(), entry.getValue());
            }
        }

        // Save the Jwknop settings
        JFwknopConfig.getInstance().saveConfig();
    }

    public void execute() {
        System.out.println("Binary: " + this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_FILEPATH));
        System.out.println("Args: " + this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_ARGS));
        System.out.println("Extra args: " + this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS));

        this.view.appendToConsole("[*] Executing : " + this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_FILEPATH)
                + " " + this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_ARGS)
                + " " + this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS));
        runExternalCommand(buildArgs());
    }

    public void refresh() {
        updateListeners();
    }

    private void runExternalCommand(final String[] args) {

        try {
            ProcessBuilder pb = new ProcessBuilder(args);
            pb = pb.redirectErrorStream(true);
            Process p = pb.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = br.readLine()) != null) {
                this.view.appendToConsole(line);
            }
        } catch (IOException e) {
            this.view.appendToConsole("Unable to execute command: " + e.getMessage());
        }
    }

    /**
     * Gather fwknop file path, fwknop arguments and extra arguments and build
     * the fwknop command line as a String array
     *
     * @return the fwknop command line as a String array
     */
    private String[] buildArgs() {

        List<String> args = new ArrayList<String>();
        args.add(this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_FILEPATH));
        args.addAll(Arrays.asList(this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_ARGS).split(" ")));
        args.addAll(Arrays.asList(this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS).split(" ")));

        String[] argsArray = new String[args.size()];

        return args.toArray(argsArray);
    }
}
