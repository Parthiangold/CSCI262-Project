package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // Loads the json password storage and history files
        PasswordStorage.loadPasswords();
        PasswordHistory.loadHistory();

        // Retrieve user input
        Scanner input = new Scanner(System.in);

        while (true) {
            // Main menu message
            System.out.println("\nWelcome to the Password Manager System.\n");
            System.out.println("Menu Options:\n");
            System.out.println("G - Generate a new password");
            System.out.println("E - Edit a saved password");
            System.out.println("R - Remove a password");
            System.out.println("V - View passwords");
            System.out.println("Q - Quit program");
            System.out.print("\nInput an option: ");

            // User inputs menu options
            String option = input.next();
            System.out.println();

            switch(option) {
                // Option G - Generate a new password
                case "G":
                    // Retrieves user input for site, username, password length and number of uppercase, lowercase, digits and symbols
                    System.out.print("Enter the site name: ");
                    String site = input.next();

                    System.out.print("Enter the username: ");
                    String username = input.next();

                    // Checks in the list if there is a record that has a matching site and username
                    boolean exists = false;
                    for (int i = 0; i < PasswordStorage.getPasswords().size(); i++) {
                        PasswordEntry e = PasswordStorage.getPasswords().get(i);
                        if (e.getSite().equals(site) && e.getUsername().equals(username)) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        System.out.println("\nThere is already an existing password for this site and username.");
                        System.out.println("If you want to regenerate a new password, go to 'E' on the menu.");
                        break;
                    }

                    int length = 12;
                    while (true) {
                        System.out.print("Enter the desired number of characters (MUST BE >= 12): ");
                        length = Integer.parseInt(input.next());
                        if (length >= 12) { break; }
                        System.out.println("ERROR: CHARACTER NUMBER IS BELOW 12. TRY AGAIN. ");
                    }
                    System.out.print("Enter the minimum desired number of uppercase letters: ");
                    int upper = Integer.parseInt(input.next());
                    
                    System.out.print("Enter the minimum desired number of lowercase letters: ");
                    int lower = Integer.parseInt(input.next());
                    
                    System.out.print("Enter the minimum desired number of digits: ");
                    int digits = Integer.parseInt(input.next());
                    
                    System.out.print("Enter the minimum desired number of symbols: ");
                    int symbols = Integer.parseInt(input.next());
                    

                    // Password is generated, displayed to the user, and added to the password list
                    String pw = PasswordGenerator.generate(length, upper, lower, digits, symbols);
                    System.out.println("\nYour generated password is: " + pw);
                    PasswordEntry entry = new PasswordEntry(site, username, pw);
                    PasswordStorage.addPassword(entry);
                    break;

                
                // Option E - Edit a saved password
                case "E":
                    // User inputs the site name and username to retrieve the specific entry record first
                    System.out.print("Enter the site name: ");
                    site = input.next();
                    System.out.print("Enter the username: ");
                    username = input.next();

                    // Checks in the list if there is a record that has a matching site and username
                    for (int i = 0; i < PasswordStorage.getPasswords().size(); i++) {
                        PasswordEntry e = PasswordStorage.getPasswords().get(i);
                        if (e.getSite().equals(site) && e.getUsername().equals(username)) {
                            System.out.println("\nBeginning password regeneration...\n");
                            
                            // If there is a match, then we perform the password generation process through prompts
                            length = 12;
                            while (true) {
                                System.out.print("Enter the desired number of characters (MUST BE >= 12): ");
                                length = Integer.parseInt(input.next());
                                if (length >= 12) { break; }
                                System.out.println("ERROR: CHARACTER NUMBER IS BELOW 12. TRY AGAIN. ");
                            }
                            System.out.print("Enter the minimum desired number of uppercase letters: ");
                            upper = Integer.parseInt(input.next());
                            
                            System.out.print("Enter the minimum desired number of lowercase letters: ");
                            lower = Integer.parseInt(input.next());
                            
                            System.out.print("Enter the minimum desired number of digits: ");
                            digits = Integer.parseInt(input.next());
                            
                            System.out.print("Enter the minimum desired number of symbols: ");
                            symbols = Integer.parseInt(input.next());
                            

                            // Password is generated, displayed to the user, and the password entry is updated
                            pw = PasswordGenerator.generate(length, upper, lower, digits, symbols);
                            System.out.println("\nYour generated password is: " + pw);
                            e.setPassword(pw);
                            break;
                        }

                        // If there's no matching entry in the entire list, an error message is displayed
                        else if (i == PasswordStorage.getPasswords().size() - 1) {
                            System.out.println("\nThere is no password entry with the inputted site and username.");
                        }
                    }
                    break;


                // Option R - Remove a password
                case "R":
                    // User inputs the site name and username to retrieve the specific entry record first
                    System.out.print("Enter the site name: ");
                    site = input.next();
                    System.out.print("Enter the username: ");
                    username = input.next();

                    // Checks in the list if there is a record that has a matching site and username
                    for (int i = 0; i < PasswordStorage.getPasswords().size(); i++) {
                        PasswordEntry e = PasswordStorage.getPasswords().get(i);
                        if (e.getSite().equals(site) && e.getUsername().equals(username)) {
                            // If there is a match, then the program first removes the password from the storage list
                            PasswordStorage.getPasswords().remove(i);

                            // Then the password used in the former entry is hashed and moved to the history list
                            String oldPW = e.getPassword();
                            PasswordHistory.addOldPassword(oldPW);
                            System.out.println("\nPassword successfuly removed from storage.");
                            break;
                        }

                        // If there's no matching entry in the entire list, an error message is displayed
                        else if (i == PasswordStorage.getPasswords().size() - 1) {
                            System.out.println("\nThere is no password entry with the inputted site and username.");
                        }
                    }
                    break;


                // Option V - View passwords
                case "V":
                    // Prints out the password entry on a line for each entry
                    System.out.println("| - SITE NAME - | - USERNAME - | - PASSWORD - |");
                    for (PasswordEntry e : PasswordStorage.getPasswords()) {
                        System.out.println(e.toString());
                    }
                    break;
                

                // Option Q - Quit program
                case "Q":
                    // Displays confirmation message, double checking with the user if they wish to quit
                    System.out.println("Are you sure? Y/N");
                    
                    while(true) {
                        // Input from user is prompted
                        String shutDown = input.next();
                        
                        // If "N", the program returns to the menu
                        if (shutDown.equals("N")) {
                            break;
                        }
                        // If "Y", the passwords are written to the files and the program terminates
                        else if (shutDown.equals("Y")) {
                            // Saves passwords
                            PasswordStorage.savePasswords();
                            PasswordHistory.saveHistory();

                            // Program terminates
                            System.out.println("Terminating program.");
                            input.close();
                            System.exit(0);
                        }
                        // Any other input causes the program to reprompt the user to input either "Y" or "N" 
                        else {
                            System.out.println("Invalid input. Are you sure? Y/N");
                        }
                    }
                    break;
                

                // Default output if anything invalid is inputted
                default:
                    System.out.println("\'" + option + "\' is not a valid input. Try again.");
                    break;
            }
        }
    }
}
