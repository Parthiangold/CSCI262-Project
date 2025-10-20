package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PasswordStorage {
    private static ArrayList<PasswordEntry> entries = new ArrayList<PasswordEntry>();
    private static String storageFilePath = "src/main/resources/passwords.enc";    // TODO: MIGHT CHANGE THIS AT END

    // Reads saved passwords from encrypted file
    public static void loadPasswords(String hash) throws Exception {
        File in = new File(storageFilePath);
        if(!in.isFile()) { in.createNewFile(); return; }
        FileInputStream input = new FileInputStream(storageFilePath);
        String data = PasswordEncryptor.decrypt(input, hash, "Storage");
        entries = new Gson().fromJson(data, new TypeToken<ArrayList<PasswordEntry>>(){}.getType());
        if (entries == null) { entries = new ArrayList<PasswordEntry>(); }
        input.close();
    }

    // Writes loaded passwords to an encrypted file
    public static void savePasswords() throws Exception {
        // File contents are converted to JSON format
        FileOutputStream output = new FileOutputStream(storageFilePath); // TODO: CHANGE
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String data = gson.toJson(entries);

        // The files are then encrypted using AES-GCM
        PasswordEncryptor.encrypt(data, output, "Storage");
        output.flush();
        output.close();
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
        // Checks though each password entry by comparing the inputted site and username
        for (int i = 0; i < entries.size(); i++) {
            PasswordEntry e = entries.get(i);
            if (e.getSite().equals(site) && e.getUsername().equals(username)) {
                // If there is a match, then the program first removes the password from the storage list
                entries.remove(i);

                // Then the password used in the former entry is hashed and moved to the history list
                String oldPW = e.getPassword();
                PasswordHistory.addOldPassword(oldPW);
                System.out.println("\nPassword successfuly removed from storage.");
                break;
            }

            // If there's no matching entry in the entire list, an error message is displayed
            else if (i == entries.size() - 1) {
                System.out.println("\nThere is no password entry with the inputted site and username.");
            }
        }
    }
}
