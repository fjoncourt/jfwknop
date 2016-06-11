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
import com.cipherdyne.jfwknop.EnumFwknopRcKey;
import com.cipherdyne.jfwknop.RcFile;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RcFileModel {

    static final Logger logger = LogManager.getLogger(RcFileModel.class.getName());

    private Map<EnumFwknopRcKey, String> context;
    private final MainWindowView view;
    private RcFile rcFile;

    public RcFileModel(final MainWindowView view) {
        super();
        this.view = view;
    }

    private void updateListeners() {
        this.view.onRcFileChange(this.context);
    }

    public void loadRcFile(final String filePath) {
        this.rcFile = new RcFile(filePath);
        if (this.rcFile.parse()) {
            this.context = this.rcFile.getConfig();
            updateListeners();
        }
    }

    public void saveRcFile(final Map<EnumFwknopRcKey, String> context) {
        this.rcFile.setConfig(context);
        logger.info("Save config");
        this.rcFile.save();
    }

    public void saveAsRcFile(final Map<EnumFwknopRcKey, String> newContext, final String filename) {
        this.context = newContext;
        if (!this.exists()) {
            this.rcFile = new RcFile("fwknoprctmp");
        }
        this.rcFile.setConfig(this.context);
        logger.info("Save config as :" + filename);
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
}
