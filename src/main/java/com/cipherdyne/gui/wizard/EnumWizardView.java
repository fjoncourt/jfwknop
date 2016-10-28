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

import com.cipherdyne.gui.wizard.views.AccessView;
import com.cipherdyne.gui.wizard.views.AesView;
import com.cipherdyne.gui.wizard.views.CryptoView;
import com.cipherdyne.gui.wizard.views.GpgHomeDirectoryView;
import com.cipherdyne.gui.wizard.views.GpgRecipientIdView;
import com.cipherdyne.gui.wizard.views.GpgSignerIdView;
import com.cipherdyne.gui.wizard.views.HmacView;
import com.cipherdyne.gui.wizard.views.IWizardView;
import com.cipherdyne.gui.wizard.views.RemoteHostView;

/**
 *
 * @author Franck Joncourt
 */
public enum EnumWizardView {
    SELECT_CRYPTO {
        @Override
        public IWizardView getView() {
            return new CryptoView();
        }
    },
    SETUP_AES {
        @Override
        public IWizardView getView() {
            return new AesView();
        }
    },
    SETUP_HMAC {
        @Override
        public IWizardView getView() {
            return new HmacView();
        }
    },
    SETUP_ACCESS {
        @Override
        public IWizardView getView() {
            return new AccessView();
        }
    },
    SETUP_GPG_HOME_DIRECTORY {
        @Override
        public IWizardView getView() {
            return new GpgHomeDirectoryView();
        }
    },
    SETUP_GPG_SIGNER_ID {
        @Override
        public IWizardView getView() {
            return new GpgSignerIdView();
        }
    },
    SETUP_GPG_RECIPIENT_ID {
        @Override
        public IWizardView getView() {
            return new GpgRecipientIdView();
        }
    },
    SETUP_REMOTE_HOST {
        @Override
        public IWizardView getView() {
            return new RemoteHostView();
        }
    };

    public abstract IWizardView getView();
}
