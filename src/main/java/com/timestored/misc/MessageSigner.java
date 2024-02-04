package com.timestored.misc;

import sun.security.provider.DSAPrivateKey;
import sun.security.provider.DSAPublicKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.*;

public class MessageSigner {
    private static final String ALGO = "DSA";
    private static final int KEYSIZE = 1024;

    public static String getMessage(String pubKey, String signedMessage) {
        try {
            byte[] sm = Base64.decode(signedMessage);
            ByteArrayInputStream is = new ByteArrayInputStream(sm);

            byte[] sl = new byte[4];
            is.read(sl);
            int signatureLength = fromByteArray(sl);

            if (signatureLength < sm.length) {
                byte[] signature = new byte[signatureLength];
                is.read(signature);

                int mLength = sm.length - 4 - signatureLength;
                if (mLength > 0) {
                    byte[] message = new byte[sm.length - 4 - signatureLength];
                    is.read(message);
                    if (verify(Base64.decode(pubKey), message, signature)) {
                        return new String(message);
                    }
                }
            }
        } catch (GeneralSecurityException | IllegalArgumentException | IOException e) {

        }

        return null;
    }

    private static byte[] toByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    private static int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | bytes[3] & 0xFF;
    }

    public static String createSignedMessage(String b64PrivateKey, String message) throws IOException, GeneralSecurityException {
        byte[] privKey = Base64.decode(b64PrivateKey);
        byte[] msg = message.getBytes();
        byte[] signature = signMessage(privKey, msg);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(toByteArray(signature.length));
        outputStream.write(signature);
        outputStream.write(msg);

        return Base64.encodeBytes(outputStream.toByteArray(), 2);
    }

    private static boolean verify(byte[] pubKey, byte[] message, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature verifyalg = Signature.getInstance("DSA");
        verifyalg.initVerify(new DSAPublicKey(pubKey));
        verifyalg.update(message);
        return verifyalg.verify(signature);
    }

    private static byte[] signMessage(byte[] privKey, byte[] message) throws GeneralSecurityException {
        Signature signalg = Signature.getInstance("DSA");
        signalg.initSign(new DSAPrivateKey(privKey));
        signalg.update(message);
        return signalg.sign();
    }

    public static String[] generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator pairgen = KeyPairGenerator.getInstance("DSA");
        SecureRandom random = new SecureRandom();
        pairgen.initialize(1024, random);
        KeyPair keyPair = pairgen.generateKeyPair();
        byte[] privKey = keyPair.getPrivate().getEncoded();
        byte[] pubKey = keyPair.getPublic().getEncoded();

        String pub = Base64.encodeBytes(pubKey);
        String priv = Base64.encodeBytes(privKey);
        System.out.println("public = " + pub);
        System.out.println("private = " + priv);

        return new String[]{pub, priv};
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\MessageSigner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */