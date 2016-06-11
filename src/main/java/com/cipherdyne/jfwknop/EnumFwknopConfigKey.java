package com.cipherdyne.jfwknop;

public enum EnumFwknopConfigKey {
    FWKNOP_FILEPATH("fwknop_filepath"), 
    FWKNOP_ARGS("fwknop_args"), 
    FWKNOP_EXTRA_ARGS("fwknop_extra_args"), 
    FWKNOP_VERBOSE("fwknop_verbose"), 
    
    KEY_RIJNDAEL_LENGTH("key_rijndael_length"),
    KEY_HMAC_LENGTH("key_hmac_length"),
    KEY_BASE64_RIJNDAEL_LENGTH("key_base64_rijndael_length"),
    KEY_BASE64_HMAC_LENGTH("key_base64_hmac_length"),
    
    LANGUAGE("language");

    private final String key;

    private EnumFwknopConfigKey(final String key) {
        this.key = key;
    }
        
    public String getKey() {
        return this.key;
    }
}
