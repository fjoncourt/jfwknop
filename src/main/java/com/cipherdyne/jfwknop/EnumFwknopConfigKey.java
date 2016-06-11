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
