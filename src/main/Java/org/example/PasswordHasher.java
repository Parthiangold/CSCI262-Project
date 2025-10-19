package org.example;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHasher {

    private static final Argon2 argon2 = Argon2Factory.create();

    // Hashes the given password with Argon2i
    public static String hash(String password) {
        char[] pw = password.toCharArray();
        return argon2.hash(4, 65536, 2, pw); //This is the settings I made as the default. This can be modified further to meet requirements
    }

    // Checks if a given password that is then converted into a hash outputs the same hash as the given hash
    public static boolean verify(String hash, String password) {
        char[] pw = password.toCharArray();
        return argon2.verify(hash, pw);
    }
}
