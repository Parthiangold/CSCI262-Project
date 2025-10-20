package org.example;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStore.SecretKeyEntry;
import java.security.SecureRandom;
import java.util.Scanner;

public class PasswordEncryptor {

    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH = 128;

    // Encrypts plaintext using the given key
    public static void encrypt(String plaintext, FileOutputStream outputStream, String name) throws Exception {
        // Generates new key used for encryption and decryption
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();

        // Master password is retrieved
        Scanner fr = new Scanner("src/main/resources/master.txt");
        char[] keyPW = fr.next().toCharArray();
        fr.close();

        // KeyStore is generated
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileOutputStream keyOut = new FileOutputStream("src/main/resources/key" + name);
        keyStore.load(null, keyPW);
        KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(keyPW);
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
        keyStore.setEntry("alias", secretKeyEntry, protection);

        // Secret key is stored in a file using the master password
        keyStore.store(keyOut, keyPW);
        keyOut.flush();
        keyOut.close();

        // Generates the GCM initialisation vector
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);

        // Initialises the AES cipher
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        // Writes the IV and ciphertext into the file
        CipherOutputStream cipherOut = new CipherOutputStream(outputStream, cipher);
        outputStream.write(iv);
        cipherOut.write(plaintext.getBytes());
        cipherOut.close();
    }

    // Decrypts file contents using the given key
    public static String decrypt(FileInputStream input, String hash, String name) throws Exception {
        // Master password is retrieved
        Scanner fr = new Scanner("src/main/resources/master.txt");
        char[] keyPW = fr.next().toCharArray();
        fr.close();

        // KeyStore is generated and loads the key file
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        File keyFile = new File("src/main/resources/key" + name);
        if(!keyFile.isFile()) {
            System.out.println("\n!! KEY FILE NOT FOUND. NO DATA WILL BE LOADED AS A RESULT !!");
            return null;
        }
        FileInputStream keyIn = new FileInputStream(keyFile);
        keyStore.load(keyIn, keyPW);

        // Secret key is them retrieved
        KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(keyPW); // Password for the key entry
        KeyStore.SecretKeyEntry skEntry = (SecretKeyEntry)keyStore.getEntry("alias", protection);
        SecretKey key = skEntry.getSecretKey();
        keyIn.close();

        // Reads the IV from the file
        byte[] fileIV = new byte[IV_SIZE];
        int check = input.read(fileIV);
        if (check < IV_SIZE) { return ""; }
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, fileIV);
        
        // Initialises the AES cipher
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        // Readers used to interpret the ciphertext
        CipherInputStream cipherIn = new CipherInputStream(input, cipher);
        InputStreamReader inputReader = new InputStreamReader(cipherIn);
        BufferedReader reader = new BufferedReader(inputReader);

        // Constructs the plaintext from reading the file from the buffer
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
