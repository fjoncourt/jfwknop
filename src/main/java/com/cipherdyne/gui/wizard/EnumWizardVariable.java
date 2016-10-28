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
package com.cipherdyne.gui.wizard;

import com.cipherdyne.utils.InternationalizationHelper;

/**
 *
 * @author Franck Joncourt
 */
public enum EnumWizardVariable {
    ENCRYPTION_MODE("i18n.wizard.encryptionmode.description"),
    AES_KEY("i18n.wizard.key.description"),
    HMAC_KEY("i18n.wizard.hmac.description"),
    REMOTE_HOST("i18n.wizard.remotehost.description"),
    ACCESS("i18n.wizard.access.description"),
    GPG_HOME_DIRECTORY("i18n.wizard.gnupg.homedirectory.description"),
    GPG_SIGNER_ID("i18n.wizard.gnupg.signerid.description"),
    GPG_SIGNER_PASSWORD("i18n.wizard.gnupg.signerid.password"),
    GPG_RECIPIENT_ID("i18n.wizard.gnupg.recipientid.description");

    private final String description;

    private EnumWizardVariable(String description) {
        this.description = description;
    }

    public String getDescription() {
        return InternationalizationHelper.getMessage(this.description);
    }
}
