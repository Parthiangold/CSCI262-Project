package org.example;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncryptor {

    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH = 128;

    // Encrypts plaintext using the given key
    public static String encrypt(byte[] key, String plaintext) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), spec);

        byte[] cipherText = cipher.doFinal(plaintext.getBytes());
        byte[] combined = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    // Decrypts file contents using the given key
    public static String decrypt(byte[] key, String cipherTextBase64) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(cipherTextBase64);

        byte[] iv = new byte[IV_SIZE];
        byte[] cipherText = new byte[decoded.length - IV_SIZE];
        System.arraycopy(decoded, 0, iv, 0, IV_SIZE);
        System.arraycopy(decoded, IV_SIZE, cipherText, 0, cipherText.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), spec);

        return new String(cipher.doFinal(cipherText));
    }
}
