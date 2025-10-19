package org.example;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PasswordStorage {
    private static ArrayList<PasswordEntry> entries = new ArrayList<PasswordEntry>();
    private static String storageFilePath = "src/main/resources/passwords.json";    // TODO: MIGHT CHANGE THIS AT END

    // Reads saved passwords from json file
    public static void loadPasswords() throws IOException {
        FileReader reader = new FileReader(storageFilePath);
        entries = new Gson().fromJson(reader, new TypeToken<ArrayList<PasswordEntry>>(){}.getType());
        if (entries == null) { entries = new ArrayList<PasswordEntry>(); }
        reader.close();
    }

    // Writes loaded passwords to the json file
    public static void savePasswords() throws IOException {
        FileWriter output = new FileWriter(storageFilePath);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(entries, output);
        output.flush();
    }

    // Adds password entry to list
    public static void addPassword(PasswordEntry entry) {
        entries.add(entry);
    }

    // Returns the list of passwords
    public static ArrayList<PasswordEntry> getPasswords() {
        return entries == null ? new ArrayList<PasswordEntry>() : entries;
    }

    // Removes a specified password from the list
    public static void removePassword(String site, String username) {

    }
}
