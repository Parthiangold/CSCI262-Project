package org.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PasswordHistory {
    private static ArrayList<String> previousPasswords = new ArrayList<String>();
    private static String historyFilePath = "src/main/resources/history.json";    // TODO: MIGHT CHANGE THIS AT END

    // Reads saved passwords from json file
    public static void loadHistory() throws IOException {
        FileReader reader = new FileReader(historyFilePath);
        previousPasswords = new Gson().fromJson(reader, new TypeToken<ArrayList<String>>(){}.getType());
        if (previousPasswords == null) { previousPasswords = new ArrayList<String>(); }
        reader.close();
    }

    // Writes loaded passwords to the json file
    public static void saveHistory() throws IOException {
        FileWriter output = new FileWriter(historyFilePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(previousPasswords, output);
        output.flush();
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
