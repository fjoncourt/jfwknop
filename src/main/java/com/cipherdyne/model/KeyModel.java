/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.model;

import com.cipherdyne.gui.MainWindowView;
import com.cipherdyne.jfwknop.EnumFwknopConfigKey;
import com.cipherdyne.jfwknop.JFwknopConfig;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class KeyModel {

    private final MainWindowView view;
    private final Map<EnumFwknopConfigKey, String> context = new HashMap<>();

    public KeyModel(MainWindowView view) {
        this.view = view;
        this.context.put(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH, JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.KEY_RIJNDAEL_LENGTH));
        this.context.put(EnumFwknopConfigKey.KEY_HMAC_LENGTH, JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.KEY_HMAC_LENGTH));
        this.context.put(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH, JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.KEY_BASE64_RIJNDAEL_LENGTH));
        this.context.put(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH, JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.KEY_BASE64_HMAC_LENGTH));
        this.context.put(EnumFwknopConfigKey.KEY_BASE64_GPG_LENGTH, JFwknopConfig.getInstance().getConfigKey().get(EnumFwknopConfigKey.KEY_BASE64_GPG_LENGTH));

        updateListeners();
    }

    private void updateListeners() {
        this.view.onKeyContextChange(this.context);
    }

    /**
     * Store the key in the context. The value is not saved.
     *
     * @param key Key to store
     * @param value New value od the key to store in the context
     */
    public void setContext(EnumFwknopConfigKey key, String value) {
        this.context.put(key, value);
    }

    /**
     * Save the key settings.
     *
     * The key settings are stored in the JFwknop configuration file
     */
    public void save() {

        // Get jfwknop configuration keys
        Map<EnumFwknopConfigKey, String> jfwknopConfig = JFwknopConfig.getInstance().getConfigKey();

        // If available store the keys
        for (final Map.Entry<EnumFwknopConfigKey, String> entry : this.context.entrySet()) {
            if (jfwknopConfig.containsKey(entry.getKey())) {
                jfwknopConfig.put(entry.getKey(), entry.getValue());
            }
        }

        // Save the Jwknop settings
        JFwknopConfig.getInstance().saveConfig();
    }
}
