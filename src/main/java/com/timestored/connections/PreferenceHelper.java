package com.timestored.connections;

import com.timestored.StringUtils;
import com.timestored.misc.Base64;
import com.timestored.misc.Encryptor;

import java.io.IOException;


public class PreferenceHelper {
    private static final String ENCRYPTED_PREFIX = "XXX_I_AM_ENCRYPTED_XXX";
    private static final String ENCODED_PREFIX = "XXX_ENCODED_XXX";
    private static final String DES_KEY = "ov0EXlLxDUY=";

    public static String decode(String txt) throws IOException {
        if (txt.startsWith("XXX_I_AM_ENCRYPTED_XXX")) {
            String t = txt.substring("XXX_I_AM_ENCRYPTED_XXX".length());
            txt = Encryptor.decrypt(t, "ov0EXlLxDUY=");
        } else if (txt.startsWith("XXX_ENCODED_XXX")) {
            String t = txt.substring("XXX_ENCODED_XXX".length());
            byte[] b = Base64.decode(t);
            txt = StringUtils.decompress(b);
        }
        return txt;
    }

    public static String encode(String txt) {
        byte[] t = StringUtils.compress(txt);

        return "XXX_ENCODED_XXX" + Base64.encodeBytes(t);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\connections\PreferenceHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */