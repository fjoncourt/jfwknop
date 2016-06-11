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
package com.cipherdyne.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class InternationalizationHelper {

    static private ResourceBundle messages;
    private static final Logger logger = LogManager.getLogger(InternationalizationHelper.class.getName());
    private static InternationalizationHelper instance = null;

    /**
     * Hidden constructor
     *
     * @param language
     * @param country
     */
    private InternationalizationHelper(final String language, final String country) {

        messages = ResourceBundle.getBundle("messages", new Locale(language, country));
    }

    public static String getMessage(final String i18nKey) {

        String translation = i18nKey;

        if (instance == null) {
            instance = new InternationalizationHelper("en", "EN");
        }

        try {
            translation = messages.getString(i18nKey);
        } catch (final MissingResourceException e) {
            logger.warn("No translation found for key " + i18nKey);
        }

        return translation;
    }

    public static void configure(final String locale) {
        String[] localeArray = locale.split("_");
        instance = new InternationalizationHelper(localeArray[0], localeArray[1]);
    }
}
