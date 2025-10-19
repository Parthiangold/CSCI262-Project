package org.example;

public class PasswordEntry {
    private String site, username, password;

    public PasswordEntry(){} // Empty constructor used for file loading methods

    // Main constructor
    public PasswordEntry(String s, String u, String p) {
        this.site = s;      // Website of the entry
        this.username = u;  // Username of the entry
        this.password = p;  // Generated password of the entry
    }

    public String getSite() {
        return this.site;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String p) {
        this.password = p;
    }

    // Console display of password entry
    public String toString() {
        return String.format("| %s | %s | %s |", site, username, password);
    }
}
