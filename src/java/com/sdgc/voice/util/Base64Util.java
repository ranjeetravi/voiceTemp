/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdgc.voice.util;

/**
 *
 * @author ranjeetr
 */
import org.apache.commons.codec.binary.Base64;

public class Base64Util {

    public byte[] decode(byte[] s) {
        return Base64.decodeBase64(s);
    }

    public String encode(byte[] s) {
        return Base64.encodeBase64String(s);
    }
}
