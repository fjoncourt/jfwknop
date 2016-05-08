/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cipherdyne.jfwknop;

/**
 *
 * @author franck
 */
public enum EnumFwknopRcType {
    
    PORT_LIST,
    IP_ADDRESS,
    Y_N,
    DIGEST_ALGORITHM,
    ENCRYPT_MODE,
    PASSPHRASE,
    BASE64_PASSPHRASE,
    GPG_KEY_ID,
    DIRECTORY_PATH,
    FILE_PATH,
    IP_PLUS_PORT,
    SINGLE_PORT,
    PROTOCOL,
    PROTOCOL_PLUS_PORT,
    SECONDS,
    URL,
    STRING,
    TIME;
    
    private EnumFwknopRcType() {
    }    
}
