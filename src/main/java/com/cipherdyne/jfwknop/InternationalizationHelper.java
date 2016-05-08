package com.cipherdyne.jfwknop;

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
