package com.timestored.misc;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Base64 {
    public static final int NO_OPTIONS = 0;
    public static final int ENCODE = 1;
    public static final int DECODE = 0;
    public static final int GZIP = 2;
    public static final int DONT_GUNZIP = 4;
    public static final int DO_BREAK_LINES = 8;
    public static final int URL_SAFE = 16;
    public static final int ORDERED = 32;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte EQUALS_SIGN = 61;
    private static final byte NEW_LINE = 10;
    private static final String PREFERRED_ENCODING = "US-ASCII";
    private static final byte WHITE_SPACE_ENC = -5;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte[] _STANDARD_ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};

    private static final byte[] _STANDARD_DECODABET = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};

    private static final byte[] _URL_SAFE_ALPHABET = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};

    private static final byte[] _URL_SAFE_DECODABET = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};

    private static final byte[] _ORDERED_ALPHABET = {45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};

    private static final byte[] _ORDERED_DECODABET = {-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9};

    private static byte[] getAlphabet(int options) {
        if ((options & 0x10) == 16)
            return _URL_SAFE_ALPHABET;
        if ((options & 0x20) == 32) {
            return _ORDERED_ALPHABET;
        }
        return _STANDARD_ALPHABET;
    }

    private static byte[] getDecodabet(int options) {
        if ((options & 0x10) == 16)
            return _URL_SAFE_DECODABET;
        if ((options & 0x20) == 32) {
            return _ORDERED_DECODABET;
        }
        return _STANDARD_DECODABET;
    }

    private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {
        encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
        return b4;
    }

    private static void encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
        byte[] ALPHABET = getAlphabet(options);

        int inBuff = ((numSigBytes > 0) ? (source[srcOffset] << 24 >>> 8) : 0) | ((numSigBytes > 1) ? (source[srcOffset + 1] << 24 >>> 16) : 0) | ((numSigBytes > 2) ? (source[srcOffset + 2] << 24 >>> 24) : 0);

        switch (numSigBytes) {

            case 3:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
                destination[destOffset + 3] = ALPHABET[inBuff & 0x3F];
                return;

            case 2:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
                destination[destOffset + 3] = 61;
                return;

            case 1:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                destination[destOffset + 2] = 61;
                destination[destOffset + 3] = 61;
        }
    }

    public static void encode(ByteBuffer raw, ByteBuffer encoded) {
        byte[] raw3 = new byte[3];
        byte[] enc4 = new byte[4];

        while (raw.hasRemaining()) {
            int rem = Math.min(3, raw.remaining());
            raw.get(raw3, 0, rem);
            encode3to4(enc4, raw3, rem, 0);
            encoded.put(enc4);
        }
    }

    public static void encode(ByteBuffer raw, CharBuffer encoded) {
        byte[] raw3 = new byte[3];
        byte[] enc4 = new byte[4];

        while (raw.hasRemaining()) {
            int rem = Math.min(3, raw.remaining());
            raw.get(raw3, 0, rem);
            encode3to4(enc4, raw3, rem, 0);
            for (int i = 0; i < 4; i++) {
                encoded.put((char) (enc4[i] & 0xFF));
            }
        }
    }

    public static String encodeObject(Serializable serializableObject) throws IOException {
        return encodeObject(serializableObject, 0);
    }

    public static String encodeObject(Serializable serializableObject, int options) throws IOException {
        if (serializableObject == null) {
            throw new NullPointerException("Cannot serialize a null object.");
        }

        ByteArrayOutputStream baos = null;
        java.io.OutputStream b64os = null;
        GZIPOutputStream gzos = null;
        ObjectOutputStream oos = null;

        try {
            baos = new ByteArrayOutputStream();
            b64os = new OutputStream(baos, 0x1 | options);
            if ((options & 0x2) != 0) {

                gzos = new GZIPOutputStream(b64os);
                oos = new ObjectOutputStream(gzos);
            } else {

                oos = new ObjectOutputStream(b64os);
            }
            oos.writeObject(serializableObject);
        } finally {

            try {
                if (oos != null) oos.close();
            } catch (IOException e) {
            }
            try {
                if (gzos != null) gzos.close();
            } catch (IOException e) {
            }
            try {
                if (b64os != null) b64os.close();
            } catch (IOException e) {
            }
            try {
                if (baos != null) baos.close();
            } catch (IOException e) {
            }
        }

        return baos.toString(StandardCharsets.US_ASCII);
    }

    public static String encodeBytes(byte[] source) {
        String encoded = null;
        try {
            encoded = encodeBytes(source, 0, source.length, 0);
        } catch (IOException ex) {
            assert false : ex.getMessage();
        }
        assert encoded != null;
        return encoded;
    }

    public static String encodeBytes(byte[] source, int options) throws IOException {
        return encodeBytes(source, 0, source.length, options);
    }

    public static String encodeBytes(byte[] source, int off, int len) {
        String encoded = null;
        try {
            encoded = encodeBytes(source, off, len, 0);
        } catch (IOException ex) {
            assert false : ex.getMessage();
        }
        assert encoded != null;
        return encoded;
    }

    public static String encodeBytes(byte[] source, int off, int len, int options) throws IOException {
        byte[] encoded = encodeBytesToBytes(source, off, len, options);

        return new String(encoded, StandardCharsets.US_ASCII);
    }

    public static byte[] encodeBytesToBytes(byte[] source) {
        byte[] encoded = null;
        try {
            encoded = encodeBytesToBytes(source, 0, source.length, 0);
        } catch (IOException ex) {
            assert false : "IOExceptions only come from GZipping, which is turned off: " + ex.getMessage();
        }
        return encoded;
    }

    public static byte[] encodeBytesToBytes(byte[] source, int off, int len, int options) throws IOException {
        if (source == null) {
            throw new NullPointerException("Cannot serialize a null array.");
        }

        if (off < 0) {
            throw new IllegalArgumentException("Cannot have negative offset: " + off);
        }

        if (len < 0) {
            throw new IllegalArgumentException("Cannot have length offset: " + len);
        }

        if (off + len > source.length) {
            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", off, len, source.length));
        }

        if ((options & 0x2) != 0) {
            ByteArrayOutputStream baos = null;
            GZIPOutputStream gzos = null;
            OutputStream b64os = null;

            try {
                baos = new ByteArrayOutputStream();
                b64os = new OutputStream(baos, 0x1 | options);
                gzos = new GZIPOutputStream(b64os);

                gzos.write(source, off, len);
                gzos.close();
            } finally {

                try {
                    if (gzos != null) gzos.close();
                } catch (IOException | NullPointerException iOException) {
                }
                try {
                    if (b64os != null) b64os.close();
                } catch (IOException | NullPointerException iOException) {
                }
                try {
                    if (baos != null) baos.close();
                } catch (IOException | NullPointerException iOException) {
                }
            }

            return baos.toByteArray();
        }

        boolean breakLines = ((options & 0x8) != 0);

        int encLen = len / 3 * 4 + ((len % 3 > 0) ? 4 : 0);
        if (breakLines) {
            encLen += encLen / 76;
        }
        byte[] outBuff = new byte[encLen];

        int d = 0;
        int e = 0;
        int len2 = len - 2;
        int lineLength = 0;
        for (; d < len2; d += 3, e += 4) {
            encode3to4(source, d + off, 3, outBuff, e, options);

            lineLength += 4;
            if (breakLines && lineLength >= 76) {

                outBuff[e + 4] = 10;
                e++;
                lineLength = 0;
            }
        }

        if (d < len) {
            encode3to4(source, d + off, len - d, outBuff, e, options);
            e += 4;
        }

        if (e <= outBuff.length - 1) {

            byte[] finalOut = new byte[e];
            System.arraycopy(outBuff, 0, finalOut, 0, e);

            return finalOut;
        }

        return outBuff;
    }

    private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options) {
        if (source == null) {
            throw new NullPointerException("Source array was null.");
        }
        if (destination == null) {
            throw new NullPointerException("Destination array was null.");
        }
        if (srcOffset < 0 || srcOffset + 3 >= source.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and still process four bytes.", source.length, srcOffset));
        }

        if (destOffset < 0 || destOffset + 2 >= destination.length) {
            throw new IllegalArgumentException(String.format("Destination array with length %d cannot have offset of %d and still store three bytes.", destination.length, destOffset));
        }

        byte[] DECODABET = getDecodabet(options);

        if (source[srcOffset + 2] == 61) {

            int i = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12;

            destination[destOffset] = (byte) (i >>> 16);
            return 1;
        }

        if (source[srcOffset + 3] == 61) {

            int i = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6;

            destination[destOffset] = (byte) (i >>> 16);
            destination[destOffset + 1] = (byte) (i >>> 8);
            return 2;
        }

        int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | (DECODABET[source[srcOffset + 1]] & 0xFF) << 12 | (DECODABET[source[srcOffset + 2]] & 0xFF) << 6 | DECODABET[source[srcOffset + 3]] & 0xFF;

        destination[destOffset] = (byte) (outBuff >> 16);
        destination[destOffset + 1] = (byte) (outBuff >> 8);
        destination[destOffset + 2] = (byte) outBuff;

        return 3;
    }

    public static byte[] decode(byte[] source) throws IOException {
        byte[] decoded;

        decoded = decode(source, 0, source.length, 0);

        return decoded;
    }

    public static byte[] decode(byte[] source, int off, int len, int options) throws IOException {
        if (source == null) {
            throw new NullPointerException("Cannot decode null source array.");
        }
        if (off < 0 || off + len > source.length) {
            throw new IllegalArgumentException(String.format("Source array with length %d cannot have offset of %d and process %d bytes.", source.length, off, len));
        }

        if (len == 0)
            return new byte[0];
        if (len < 4) {
            throw new IllegalArgumentException("Base64-encoded string must have at least four characters, but length specified was " + len);
        }

        byte[] DECODABET = getDecodabet(options);

        int len34 = len * 3 / 4;
        byte[] outBuff = new byte[len34];
        int outBuffPosn = 0;

        byte[] b4 = new byte[4];
        int b4Posn = 0;
        int i;
        byte sbiDecode;

        for (i = off; i < off + len; i++) {

            sbiDecode = DECODABET[source[i] & 0xFF];

            if (sbiDecode >= -5) {
                if (sbiDecode >= -1) {
                    b4[b4Posn++] = source[i];
                    if (b4Posn > 3) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
                        b4Posn = 0;

                        if (source[i] == 61) {
                            break;
                        }
                    }
                }
            } else {

                throw new IOException(String.format("Bad Base64 input character decimal %d in array position %d", source[i] & 0xFF, i));
            }
        }

        byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }

    public static byte[] decode(String s) throws IOException {
        return decode(s, 0);
    }

    public static byte[] decode(String s, int options) throws IOException {
        if (s == null) {
            throw new NullPointerException("Input string was null.");
        }

        byte[] arr = s.getBytes(StandardCharsets.US_ASCII);

        byte[] arrayOfByte = decode(arr, 0, arr.length, options);

        boolean dontGunzip = ((options & 0x4) != 0);
        if (arrayOfByte.length >= 4 && !dontGunzip) {

            int head = arrayOfByte[0] & 0xFF | arrayOfByte[1] << 8 & 0xFF00;
            if (35615 == head) {
                byte[] buffer = new byte[2048];
                int length;

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ByteArrayInputStream bais = new ByteArrayInputStream(arrayOfByte); GZIPInputStream gzis = new GZIPInputStream(bais)) {

                    while ((length = gzis.read(buffer)) >= 0) {
                        baos.write(buffer, 0, length);
                    }

                    arrayOfByte = baos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return arrayOfByte;
    }

    public static Object decodeToObject(String encodedObject) throws IOException, ClassNotFoundException {
        return decodeToObject(encodedObject, 0, null);
    }

    public static Object decodeToObject(String encodedObject, int options, ClassLoader loader) throws IOException, ClassNotFoundException {
        byte[] objBytes = decode(encodedObject, options);

        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        Object obj;

        try {
            bais = new ByteArrayInputStream(objBytes);

            if (loader == null) {
                ois = new ObjectInputStream(bais);
            } else {

                ois = new ObjectInputStream(bais) {
                    public Class<?> resolveClass(ObjectStreamClass streamClass) throws IOException, ClassNotFoundException {
                        Class<?> c = Class.forName(streamClass.getName(), false, loader);
                        if (c == null) {
                            return super.resolveClass(streamClass);
                        }
                        return c;
                    }
                };
            }

            obj = ois.readObject();
        } finally {

            try {
                if (bais != null) bais.close();
            } catch (IOException e) {
            }
            try {
                if (ois != null) ois.close();
            } catch (IOException e) {
            }
        }

        return obj;
    }

    public static void encodeToFile(byte[] dataToEncode, String filename) throws IOException {
        if (dataToEncode == null) {
            throw new NullPointerException("Data to encode was null.");
        }

        try (OutputStream bos = new OutputStream(new FileOutputStream(filename), 1)) {

            bos.write(dataToEncode);
        }
    }

    public static void decodeToFile(String dataToDecode, String filename) throws IOException {
        try (OutputStream bos = new OutputStream(new FileOutputStream(filename), 0)) {
            bos.write(dataToDecode.getBytes(StandardCharsets.US_ASCII));
        }
    }

    public static byte[] decodeFromFile(String filename) throws IOException {
        byte[] decodedData;
        InputStream bis = null;

        try {
            File file = new File(filename);
            byte[] buffer;
            int length = 0;
            int numBytes;

            if (file.length() > 2147483647L) {
                throw new IOException("File is too big for this convenience method (" + file.length() + " bytes).");
            }
            buffer = new byte[(int) file.length()];

            bis = new InputStream(new BufferedInputStream(new FileInputStream(file)), 0);

            while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
                length += numBytes;
            }

            decodedData = new byte[length];
            System.arraycopy(buffer, 0, decodedData, 0, length);
        } finally {

            try {
                bis.close();
            } catch (Exception e) {
            }
        }

        return decodedData;
    }

    public static String encodeFromFile(String filename) throws IOException {
        String encodedData;
        InputStream bis = null;

        try {
            File file = new File(filename);
            byte[] buffer = new byte[Math.max((int) (file.length() * 1.4D + 1.0D), 40)];
            int length = 0;
            int numBytes;

            bis = new InputStream(new BufferedInputStream(new FileInputStream(file)), 1);

            while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
                length += numBytes;
            }

            encodedData = new String(buffer, 0, length, StandardCharsets.US_ASCII);
        } finally {

            try {
                bis.close();
            } catch (Exception e) {
            }
        }

        return encodedData;
    }

    public static void encodeFileToFile(String infile, String outfile) throws IOException {
        String encoded = encodeFromFile(infile);
        try (java.io.OutputStream out = new BufferedOutputStream(new FileOutputStream(outfile))) {

            out.write(encoded.getBytes(StandardCharsets.US_ASCII));
        }
    }

    public static void decodeFileToFile(String infile, String outfile) throws IOException {
        byte[] decoded = decodeFromFile(infile);
        try (java.io.OutputStream out = new BufferedOutputStream(new FileOutputStream(outfile))) {

            out.write(decoded);
        }
    }

    public static class InputStream
            extends FilterInputStream {
        private final boolean encode;
        private final byte[] buffer;
        private final int bufferLength;
        private final boolean breakLines;
        private final int options;
        private final byte[] decodabet;
        private int position;
        private int numSigBytes;
        private int lineLength;

        public InputStream(java.io.InputStream in) {
            this(in, 0);
        }

        public InputStream(java.io.InputStream in, int options) {
            super(in);
            this.options = options;
            this.breakLines = ((options & 0x8) > 0);
            this.encode = ((options & 0x1) > 0);
            this.bufferLength = this.encode ? 4 : 3;
            this.buffer = new byte[this.bufferLength];
            this.position = -1;
            this.lineLength = 0;
            this.decodabet = getDecodabet(options);
        }

        public int read() throws IOException {
            if (this.position < 0) {
                if (this.encode) {
                    byte[] b3 = new byte[3];
                    int numBinaryBytes = 0;
                    for (int i = 0; i < 3; ) {
                        int b = this.in.read();

                        if (b >= 0) {
                            b3[i] = (byte) b;
                            numBinaryBytes++;

                            i++;
                        }
                    }

                    if (numBinaryBytes > 0) {
                        encode3to4(b3, 0, numBinaryBytes, this.buffer, 0, this.options);
                        this.position = 0;
                        this.numSigBytes = 4;
                    } else {

                        return -1;
                    }
                } else {

                    byte[] b4 = new byte[4];
                    int i;
                    for (i = 0; i < 4; i++) {

                        int b;
                        do {
                            b = this.in.read();
                        } while (b >= 0 && this.decodabet[b & 0x7F] <= -5);

                        if (b < 0) {
                            break;
                        }

                        b4[i] = (byte) b;
                    }

                    if (i == 4) {
                        this.numSigBytes = decode4to3(b4, 0, this.buffer, 0, this.options);
                        this.position = 0;
                    } else {
                        if (i == 0) {
                            return -1;
                        }

                        throw new IOException("Improperly padded Base64 input.");
                    }
                }
            }

            if (this.position >= 0) {

                if (this.position >= this.numSigBytes) {
                    return -1;
                }

                if (this.encode && this.breakLines && this.lineLength >= 76) {
                    this.lineLength = 0;
                    return 10;
                }

                this.lineLength++;

                int b = this.buffer[this.position++];

                if (this.position >= this.bufferLength) {
                    this.position = -1;
                }

                return b & 0xFF;
            }

            throw new IOException("Error in Base64 code reading stream.");
        }

        public int read(byte[] dest, int off, int len) throws IOException {
            int i;
            for (i = 0; i < len; i++) {
                int b = this.read();

                if (b >= 0) {
                    dest[off + i] = (byte) b;
                } else {
                    if (i == 0) {
                        return -1;
                    }

                    break;
                }
            }
            return i;
        }
    }

    public static class OutputStream
            extends FilterOutputStream {
        private final boolean encode;
        private final int bufferLength;
        private final boolean breakLines;
        private final byte[] b4;
        private final int options;
        private final byte[] decodabet;
        private int position;
        private byte[] buffer;
        private int lineLength;
        private boolean suspendEncoding;

        public OutputStream(java.io.OutputStream out) {
            this(out, 1);
        }

        public OutputStream(java.io.OutputStream out, int options) {
            super(out);
            this.breakLines = ((options & 0x8) != 0);
            this.encode = ((options & 0x1) != 0);
            this.bufferLength = this.encode ? 3 : 4;
            this.buffer = new byte[this.bufferLength];
            this.position = 0;
            this.lineLength = 0;
            this.suspendEncoding = false;
            this.b4 = new byte[4];
            this.options = options;
            this.decodabet = getDecodabet(options);
        }

        public void write(int theByte) throws IOException {
            if (this.suspendEncoding) {
                this.out.write(theByte);

                return;
            }

            if (this.encode) {
                this.buffer[this.position++] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    this.out.write(encode3to4(this.b4, this.buffer, this.bufferLength, this.options));

                    this.lineLength += 4;
                    if (this.breakLines && this.lineLength >= 76) {
                        this.out.write(10);
                        this.lineLength = 0;
                    }

                    this.position = 0;
                }
            } else if (this.decodabet[theByte & 0x7F] > -5) {
                this.buffer[this.position++] = (byte) theByte;
                if (this.position >= this.bufferLength) {
                    int len = decode4to3(this.buffer, 0, this.b4, 0, this.options);
                    this.out.write(this.b4, 0, len);
                    this.position = 0;
                }
            } else if (this.decodabet[theByte & 0x7F] != -5) {
                throw new IOException("Invalid character in Base64 data.");
            }
        }

        public void write(byte[] theBytes, int off, int len) throws IOException {
            if (this.suspendEncoding) {
                this.out.write(theBytes, off, len);

                return;
            }
            for (int i = 0; i < len; i++) {
                this.write(theBytes[off + i]);
            }
        }

        public void flushBase64() throws IOException {
            if (this.position > 0) {
                if (this.encode) {
                    this.out.write(encode3to4(this.b4, this.buffer, this.position, this.options));
                    this.position = 0;
                } else {

                    throw new IOException("Base64 input not properly padded.");
                }
            }
        }

        public void close() throws IOException {
            this.flushBase64();

            super.close();

            this.buffer = null;
            this.out = null;
        }

        public void suspendEncoding() throws IOException {
            this.flushBase64();
            this.suspendEncoding = true;
        }

        public void resumeEncoding() {
            this.suspendEncoding = false;
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\misc\Base64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */