package org.example;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHasher {

    private static final Argon2 argon2 = Argon2Factory.create();

    public static String hash(String password) {
        return argon2.hash(4, 65536, 2, password); //This is the settings I made as the default. This can be modified further to meet requirements
    }

    public static boolean verify(String hash, String password) {
        return argon2.verify(hash, password);
    }
}
