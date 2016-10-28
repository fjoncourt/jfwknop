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
public enum EnumWizardButton {
    CANCEL("i18n.wizard.cancel"),
    BACK("i18n.wizard.back"),
    NEXT("i18n.wizard.next"),
    FINISH("i18n.wizard.finish"),
    GENERATE_AES_KEY("i18n.wizard.generate.aes.key"),
    GENERATE_HMAC_KEY("i18n.wizard.generate.hmac.key"),
    BROWSE_FOR_GPG_HOMEDIR("i18n.wizard.browse.for.gpg.homedir"),
    BROWSE_FOR_GPG_SIGNER_ID("i18n.wizard.browse.for.gpg.signerid"),
    BROWSE_FOR_GPG_RECIPIENT_ID("i18n.wizard.browse.for.gpg.recipientid");

    final private String description;

    private EnumWizardButton(String description) {
        this.description = description;
    }

    public String getDescription() {
        return InternationalizationHelper.getMessage(this.description);
    }
}
