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
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.RcFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RcFileModel {

    static final Logger LOGGER = LogManager.getLogger(RcFileModel.class.getName());

    private Map<EnumFwknopRcKey, String> context;
    private final MainWindowView view;
    private RcFile rcFile;

    public RcFileModel(final MainWindowView view) {
        super();
        this.view = view;
    }

    /**
     * Refresh all listeners
     */
    public void updateListeners() {
        this.view.onRcFileChange(this.context);
    }

    /**
     * Load a RC file.
     *
     * The RC file is parsed and if this one is valid, model listeners are updated with tits
     * configuration
     *
     * @throws IOException if the rc file does not exist
     */
    public void load() throws IOException {
        String selectedStanza = null;
        List<String> stanzaList = this.rcFile.lookUpStanza();
        if (stanzaList.size() == 1) {
            selectedStanza = stanzaList.get(0);
        }
        this.rcFile.parse(selectedStanza);
        this.context = this.rcFile.getConfig();
        updateListeners();
    }

    /**
     *
     * @param selectedStanza
     * @throws IOException
     */
    public void load(String selectedStanza) throws IOException {
        this.rcFile.parse(selectedStanza);
        this.context = this.rcFile.getConfig();
        updateListeners();
    }

    // FIXME: do not set context directly but prefer setContext method
    public void save(final Map<EnumFwknopRcKey, String> context) {
        this.rcFile.setConfig(context);
        LOGGER.info("Save config");
        this.rcFile.save();
    }

    // FIXME: do not set context directly but prefer setContext method
    @Deprecated
    public void saveAs(final Map<EnumFwknopRcKey, String> newContext, final String filename) {
        this.context = newContext;
        if (!this.exists()) {
            this.rcFile = new RcFile("fwknoprctmp");
        }
        this.rcFile.setConfig(this.context);
        LOGGER.info("Save config as :" + filename);
        this.rcFile.saveAs(filename);
    }

    /**
     * @return true if the configuration exists in a save file, false otherwise
     */
    public boolean exists() {
        return (this.rcFile != null);
    }

    /**
     * @return the rc filename used by the RcFileModel or an empty string if uninitialized
     */
    public String getRcFilename() {
        String filename = StringUtils.EMPTY;

        if (this.exists()) {
            filename = this.rcFile.getRcFilename();
        }

        return filename;
    }

    /**
     * Set rc filename
     *
     * @param filename
     */
    public void setRcFilename(String filename) {
        this.rcFile = new RcFile(filename);
    }

    public List<String> getStanzas() throws IOException {
        return this.rcFile.lookUpStanza();
    }

    public void setContext(Map<EnumFwknopRcKey, String> context) {
        this.context = context;
    }

}
