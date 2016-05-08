package com.cipherdyne.jfwknop;

public enum EnumFwknopConfigKey {
    FWKNOP_FILEPATH("fwknop_filepath"), 
    FWKNOP_ARGS("fwknop_args"), 
    FWKNOP_EXTRA_ARGS("fwknop_extra_args"), 
    FWKNOP_VERBOSE("fwknop_verbose"), 
    
    LANGUAGE("language");

    private final String key;

    private EnumFwknopConfigKey(final String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
