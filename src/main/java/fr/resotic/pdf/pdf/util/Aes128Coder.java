package fr.resotic.pdf.pdf.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static javax.crypto.Cipher.ENCRYPT_MODE;

public class Aes128Coder {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final String AES = "AES";
    private static final String SHA_1 = "SHA-1";
    private static final int ARRAY_LENGTH = 16; // AES needs the first 128 bits

    private Aes128Coder() {
        // Utility classes should not have a public or default constructor.
    }

    public static String encrypt(String key, String message) {
        try {
            byte[] formattedKey = Arrays.copyOf(formatKey(key), ARRAY_LENGTH);

            Cipher c = Cipher.getInstance(AES);
            c.init(ENCRYPT_MODE, new SecretKeySpec(formattedKey, AES));
            byte[] encValue = c.doFinal(message.getBytes());

            return Base64.encodeBase64URLSafeString(encValue);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String decrypt(String key, String encryptedMessage) {
        try {
            byte[] formattedKey = Arrays.copyOf(formatKey(key), ARRAY_LENGTH);

            Cipher c = Cipher.getInstance(AES);
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(formattedKey, AES));
            byte[] decodedValue = Base64.decodeBase64(encryptedMessage);
            return new String(c.doFinal(decodedValue));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String encryptToHex(String key, String message) throws Exception {
        if (key.length() < 16) {
            throw new Exception("Key length must be at least 16");
        }
        try {
            Key aesKey = new SecretKeySpec(key.substring(0, 16).getBytes(), AES);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encryptedValue = cipher.doFinal(message.getBytes());

            return Hex.encodeHexString(encryptedValue);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String decryptFromHex(String key, String encryptedMessage) throws Exception {
        if (key.length() < 16) {
            throw new Exception("Key length must be at least 16");
        }
        try {
            Key aesKey = new SecretKeySpec(key.substring(0, 16).getBytes(), AES);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] decryptedValue = cipher.doFinal(Hex.decodeHex(encryptedMessage.toLowerCase().toCharArray()));

            return new String(decryptedValue);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static byte[] formatKey(String id) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance(SHA_1);
        return sha.digest(id.getBytes(UTF_8));
    }
}
