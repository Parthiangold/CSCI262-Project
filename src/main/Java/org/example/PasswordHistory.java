package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PasswordHistory {
    private static ArrayList<String> previousPasswords = new ArrayList<String>();
    private static String historyFilePath = "src/main/resources/history.enc";    // TODO: MIGHT CHANGE THIS AT END

    // Reads saved passwords from encrypted file
    public static void loadHistory(String hash) throws Exception {
        File in = new File(historyFilePath);
        if(!in.isFile()) { in.createNewFile(); return; }
        FileInputStream input = new FileInputStream(historyFilePath);
        String data = PasswordEncryptor.decrypt(input, hash, "History");
        previousPasswords = new Gson().fromJson(data, new TypeToken<ArrayList<String>>(){}.getType());
        if (previousPasswords == null) { previousPasswords = new ArrayList<String>(); }
        input.close();
    }

    // Writes loaded passwords to an encrypted file
    public static void saveHistory() throws Exception {
        // File contents are converted to JSON format
        FileOutputStream output = new FileOutputStream(historyFilePath); // TODO: CHANGE
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String data = gson.toJson(previousPasswords);

        // The files are then encrypted using AES-GCM
        PasswordEncryptor.encrypt(data, output, "History");
        output.flush();
        output.close();
    }

    // Adds a previous password to the list
    public static void addOldPassword(String pw) {
        String hash = PasswordHasher.hash(pw);
        previousPasswords.add(hash);
    }

    // Returns the list of previous passwords
    public static ArrayList<String> getHistory() {
        return previousPasswords == null ? new ArrayList<String>() : previousPasswords;
    }
}
