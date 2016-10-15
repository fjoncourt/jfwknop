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
package com.cipherdyne.model;

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.jfwknop.EnumFwknopConfigKey;
import com.cipherdyne.jfwknop.ExternalCommand;
import com.cipherdyne.jfwknop.JFwknopConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FwknopClientModel {

    static final Logger LOGGER = LogManager.getLogger(FwknopClientModel.class.getName());

    private final MainWindowView view;
    private final Map<EnumFwknopConfigKey, String> fwknopConfig = new HashMap<>();
    private ExternalCommand command;

    public FwknopClientModel(final MainWindowView view) {
        this.view = view;
        this.fwknopConfig.put(EnumFwknopConfigKey.FWKNOP_FILEPATH,
            JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.FWKNOP_FILEPATH));
        this.fwknopConfig.put(EnumFwknopConfigKey.FWKNOP_ARGS, "");
        this.fwknopConfig.put(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS,
            JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS));
        this.fwknopConfig.put(EnumFwknopConfigKey.FWKNOP_VERBOSE,
            JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.FWKNOP_VERBOSE));
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

    /**
     * Start a fwknop command
     *
     * @param period period between to knock. Set to 0 to knowk only once
     */
    public void start(final long period) {
        command = new ExternalCommand(buildArgs(), period, this.view);
        Thread thread = new Thread(command);
        thread.start();
    }

    /**
     * Stop the current knock
     */
    public void stop() {
        if (this.command != null) {
            this.command.stop();
            this.command = null;
        }
    }

    public void refresh() {
        updateListeners();
    }

    /**
     * Gather fwknop file path, fwknop arguments and extra arguments and build the fwknop command
     * line as a String array
     *
     * @return the fwknop command line as a String array
     */
    private String[] buildArgs() {

        List<String> args = new ArrayList<>();
        args.add(this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_FILEPATH));
        args.addAll(Arrays.asList(this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_ARGS).split(" ")));
        args.addAll(Arrays.asList(this.fwknopConfig.get(EnumFwknopConfigKey.FWKNOP_EXTRA_ARGS).split(" ")));

        String[] argsArray = new String[args.size()];

        return args.toArray(argsArray);
    }
}
