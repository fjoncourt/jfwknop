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

public enum EnumFwknopdRcKey {
    
    SOURCE,
    
    GPG_REMOTE_ID,
    GPG_DECRYPT_ID,
    GPG_DECRYPT_PW,
    GPG_HOME_DIR,
    GPG_REQUIRE_SIG,
    GPG_IGNORE_SIG_VERIFY_ERROR,
   
    KEY,
    KEY_BASE64,
    
    USE_HMAC,
    HMAC_KEY,
    HMAC_KEY_BASE64,
    HMAC_DIGEST_TYPE;
}
