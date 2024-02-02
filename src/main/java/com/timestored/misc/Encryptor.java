package com.timestored.misc;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;


public class Encryptor {
    public static byte[] encrypt(byte[] utf8, String keyString) throws IOException {
        try {
            Cipher ecipher = Cipher.getInstance("DES");
            ecipher.init(1, getKey(keyString));
            byte[] enc = ecipher.doFinal(utf8);
            return enc;
        } catch (IllegalBlockSizeException e) {
            throw new IOException(e);
        } catch (GeneralSecurityException e) {
            throw new IOException(e);
        }
    }


    public static String encrypt(String str, String keyString) throws IOException {
        return Base64.encodeBytes(encrypt(str.getBytes(StandardCharsets.UTF_8), keyString));
    }

    private static Key getKey(String keyString) throws IOException {
        byte[] p = Base64.decode(keyString);
        Key key = new SecretKeySpec(p, 0, p.length, "DES");
        return key;
    }


    public static byte[] decrypt(byte[] dec, String keyString) throws IOException {
        try {
            Cipher dcipher = Cipher.getInstance("DES");
            dcipher.init(2, getKey(keyString));
            return dcipher.doFinal(dec);
        } catch (IllegalBlockSizeException e) {
            throw new IOException(e);
        } catch (GeneralSecurityException e) {
            throw new IOException(e);
        }
    }


    public static String decrypt(String str, String keyString) throws IOException {
        return new String(decrypt(Base64.decode(str.getBytes()), keyString), StandardCharsets.UTF_8);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\Encryptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */