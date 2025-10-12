package org.example;

import java.security.SecureRandom;
import java.util.*;

public class PasswordGenerator {
    //Initialise fields
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+?/<>,.";
    private static final SecureRandom RVG = new SecureRandom(); //RVG stands for Random Value Generator

    public static String generate(int length, int minUpper, int minLower, int minDigits, int minSymbols){
        if (length < (minUpper + minLower + minDigits + minSymbols)) {
            throw new IllegalArgumentException("Length does not meet requirements");
        }
        List<Character> chars = new ArrayList<>(length);
        for (int i = 0; i < minUpper; i++) chars.add(randomChar(UPPER));
        for (int i = 0; i < minLower; i++) chars.add(randomChar(LOWER));
        for (int i = 0; i < minDigits; i++) chars.add(randomChar(DIGITS));
        for (int i = 0; i < minSymbols; i++) chars.add(randomChar(SYMBOLS));

        String all = UPPER + LOWER +DIGITS + SYMBOLS;
        while (chars.size() < length) chars.add(randomChar(all));

        Collections.shuffle(chars, RVG);
        StringBuilder sb = new StringBuilder();
        for (char c : chars) sb.append(c);
        return sb.toString();
    }

    private static char randomChar(String pool){
        return pool.charAt(RVG.nextInt(pool.length()));
    }
}
