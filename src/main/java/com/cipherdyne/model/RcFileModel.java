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

/**
 * Model used to store fwknoprc settings that are displayed in JFwknop UI
 *
 * @author Franck Joncourt
 */
public class RcFileModel {

    // Default fwknop rc filename
    static final public String FWKNOPRC = ".fwknoprc";

    // Static logger for this class
    static final Logger LOGGER = LogManager.getLogger(RcFileModel.class.getName());

    // Context that contains the current fwknoprc settings
    private Map<EnumFwknopRcKey, String> context;

    // Rc file instance
    private RcFile rcFile;

    // The registered listener that is notified/updated when the model changes
    private final MainWindowView listener;

    /**
     * Rc file model constructor
     *
     * @param view listener to update when the model changes
     */
    public RcFileModel(final MainWindowView view) {
        super();
        this.listener = view;
        this.reset();
    }

    /**
     * Refresh all listeners (registered view)
     */
    public void updateListeners() {
        this.listener.onRcFileChange(this.context);
    }

    /**
     * Convert a multi stanza rc file to several single stanza rc file
     *
     * @param rcFilename initial rc file that contains more than one stanza
     * @throws IOException
     */
    public void convertToSingleStanzaFile(String rcFilename) throws IOException {

        RcFile legacyRcFile = new RcFile(rcFilename);
        List<String> stanzaList = legacyRcFile.lookUpStanza();
        for (String stanza : stanzaList) {
            RcFile singleStanzaRcFile = new RcFile(rcFilename);
            singleStanzaRcFile.parse(stanza);
            singleStanzaRcFile.saveAs(rcFilename + "." + stanza);
        }
    }

    /**
     * Load a RC file.
     *
     * The RC file is parsed and if this one is valid, model listeners are updated with tits
     * configuration
     *
     * @throws IOException if the rc file does not exist
     */
    //FIXME description
    public void load() throws IOException {
        String selectedStanza = null;
        List<String> stanzaList = this.rcFile.lookUpStanza();
        if (stanzaList.size() == 1) {
            selectedStanza = stanzaList.get(0);
        }
        load(selectedStanza);
    }

    /**
     *
     * @param selectedStanza
     * @throws IOException
     */
    // FIXME description
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

    /**
     * Reset the rc file model.
     *
     * This method clears the context/settings and updates listeners
     */
    public void reset() {
        this.rcFile = null;
        this.context = null;
        this.updateListeners();
    }

}
