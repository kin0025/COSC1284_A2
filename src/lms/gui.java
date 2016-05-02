/*
*@author - Alex Kinross-Smith
*/
package lms;

import lms.util.DateTime;

import java.util.Scanner;

/**
 * Created by akinr on 11/04/2016.
 */
public class GUI {
    private Scanner input = new Scanner(System.in);
    private Inventory inv = new Inventory();

    //UI Methods
    public void logoBoot() {
        System.out.printf("                                                            \n" +
                "                                                            \n" +
                "                                                            \n" +
                "                                  +                         \n" +
                "                                ++++'                       \n" +
                "                               +++ +++                      \n" +
                "                             '++'   +++;                    \n" +
                "                            +++       +++                   \n" +
                "                           +++         +++                  \n" +
                "                         '++'           '++;                \n" +
                "                        +++               +++               \n" +
                "                       +++                 +++              \n" +
                "                     +++                     +++            \n" +
                "                    +++                       +++           \n" +
                "                  '+++                         +++          \n" +
                "                 +++                             +++        \n" +
                "                +++                               +++       \n" +
                "              +++                                  '++'     \n" +
                "             +++                                     +++    \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "                                                            \n" +
                "                                                            \n" +
                "              +++++++    ++++++++  ;++++++++    +++++++     \n" +
                "              +++++++   +++++++++++++++++++++  '+++++++     \n" +
                "              +;    +   +'       +++       ++  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +        ;+        '+  '+    '+     \n" +
                "              +;    +   +         +        '+  '+    '+     \n" +
                "              +;    +   +                  '+  '+    '+     \n" +
                "              +;    +   +                  '+  '+    '+     \n" +
                "              +;    +   +                  '+  '+    '+     \n" +
                "              +;    +   +                  '+  '+    '+     \n" +
                "              +;    +   ++++++++++ ++++++++++  '+    '+     \n" +
                "              +++++++   ++++';'+++++++';'++++  '+++++++     \n" +
                "              +++++++   +         +         +   +++++++     \n" +
                "                                                            \n" +
                "                                                            \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "            +                                          '+   \n" +
                "            +                                          '+   \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "            +++++++++++++++++++++++++++++++++++++++++++++   \n" +
                "                                                            \n" +
                "                       Press enter key to continue              \n" +
                "                                                            \n" +
                "                                                            \n");
        input.nextLine(); // TODO: 19/04/2016 On any kb input, not just enter.
    }

    public void mainMenu() {
        newPage("Home");
        System.out.println(" 1. Add Holding");
        System.out.println(" 2. Remove Holding");
        System.out.println(" 3. Add Member");
        System.out.println(" 4. Remove Member");
        System.out.println(" 5. Borrow Holding");
        System.out.println(" 6. Return Holding");
        System.out.println(" 7. Print all Holdings");
        System.out.println(" 8. Print all Members");
        System.out.println(" 9. Print specific Holding");
        System.out.println("10. Print specific Member");
        System.out.println("11. Save to file");
        System.out.println("12. Load from file");
        System.out.println("13. Exit");
        System.out.println("14. Admin");
        printCharTimes('=', 150, true);
        String[] options = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};
        String choice = receiveStringInput("Enter an option:", options, false, 2);
        switch (choice) {
            case "1":
                addHolding();
                break;
            case "2":
                removeHolding();
                break;
            case "3":
                addMember();
                break;
            case "4":
                removeMember();
                break;
            case "5":
                borrowHolding();
                break;
            case "6":
                returnHolding();
                break;
            case "7":
                printAllHoldings();
                break;
            case "8":
                printAllMembers();
                break;
            case "9":
                printHolding();
                break;
            case "10":
                printMember();
                break;
            case "11":
                save();
                break;
            case "12":
                load();
                break;
            case "13":
                exit();
                mainMenu();
                break;
            case "14":
                adminMenu();
                break;
            default:
                mainMenu();
                break;
        }
    }

    public void adminMenuVerify() {
        newPage("Admin Verification");
        System.out.println("Please enter your admin password (VerySecure):");
        String password = input.nextLine();
        if (password.equals("Password1") || password.equals("VerySecure")) {
            adminMenu();
        } else {
            System.out.println("Incorrect Password. Returning to main menu");
            //todo- 1 second delay.
            mainMenu();
        }

    }

    public void adminMenu() {
        newPage("Admin");
        System.out.println(" 1. Activate Holding");
        System.out.println(" 2. Deactivate Holding");
        System.out.println(" 3. Edit Holding");
        System.out.println(" 4. Reset Member Credit");
        System.out.println(" 5. Edit Member Details");
        System.out.println(" 6. Temporary Deactivate Holding");
        System.out.println(" 7. Return holding ignoring fees");
        System.out.println(" 8. Back to Main Menu");
        printCharTimes('=', 50, true);
        System.out.println("Enter an option:");
        int options = input.nextInt();
        switch (options) {
            case 1:
                activateHolding();
                break;
            case 2:
                deactivateHolding();
                break;
            case 3:
                editHolding();
                break;
            case 4:
                resetMemberCredit();
                break;
            case 5:
                editMember();
                break;
            case 6:
                temporaryRemoveHolding();
                break;
            case 7:
                returnHoldingNoFee();
                break;
            case 8:
                mainMenu();
                break;
            default:
                adminMenu();
        }
    }

    private void newPage(String title) {
        int pageWidth = 150;
        for (int i = 30; i != 0; i--) {
            System.out.printf("\n");
        }
        printCharTimes('=', pageWidth, true);

        String leftText = DateTime.getCurrentTime();
        String centreText = "Library Management System: " + title;
        String rightText = inv.infoPrintout();

        int left = leftText.length();
        int centre = centreText.length();
        int right = rightText.length();

        int leftSpacing = pageWidth / 2 - left - (int) centre / 2;
        int rightSpacing = pageWidth / 2 - 1 - right - (int) centre / 2;

        System.out.print(leftText);
        printCharTimes(' ', leftSpacing, false);
        System.out.print(centreText);
        printCharTimes(' ', rightSpacing, false);
        System.out.println(rightText);


        printCharTimes('=', pageWidth, true);
        System.out.println();
    }

    /**
     * Returns an array of chars (for example {a,b,c} as a String in the format (a/b/c)
     **/
    private String charArrayToString(String[] array) {
        String result = "(";
        for (int i = 0; i < array.length; i++) {
            result += array[i];
            if (i < array.length - 1) {
                result += "/";
            }
        }
        result += ")";
        return (result);
    }

    private String receiveStringInput(String flavourText, String[] options, boolean printOptionText, int outputLength) {
        System.out.print(flavourText + " ");
        if (outputLength <= 0) {
            outputLength = 1;
        }
        if (printOptionText) {
            System.out.println(charArrayToString(options));
        } else System.out.println();
        String inputString = input.nextLine().toLowerCase();
        while (inputString.length() == 0) {
            System.out.println("Answer needs to be entered");
            System.out.print(flavourText + " ");
            if (printOptionText) {
                System.out.println(charArrayToString(options));
            } else System.out.println();
            inputString = input.nextLine().toLowerCase();
        }
        String inputChar = inputString.toLowerCase();
        if (inputChar.length() >= outputLength) {
            inputChar = inputChar.substring(0, outputLength);
        }
        boolean isCorrect = false;
        int i = 0;
        while (i < options.length && !isCorrect) {
            if (options[i].equals(inputChar)) {
                isCorrect = true;
            }
            i++;
        }
        if (isCorrect) {
            return (inputChar);
        } else {
            System.out.println("Input was not an option. Please try again.");
            inputChar = receiveStringInput(flavourText, options, true, outputLength);//If they didn't get it right the first time, supply options.
        }
        return (inputChar);
    }


    private String receiveStringInput(String flavourText, String[] options, String defaultAnswer, int outputLength) {
        System.out.println(flavourText + " " + charArrayToString(options) + "[" + defaultAnswer + "]");
        if (outputLength <= 0) {
            outputLength = 1;
        }
        String inputString = input.nextLine().toLowerCase();
        if (inputString.length() == 0) {
            return ("" + defaultAnswer);
        }
        String inputChar = inputString.toLowerCase();
        if (inputChar.length() >= outputLength) {
            inputChar = inputChar.substring(0, outputLength);
        }
        boolean isCorrect = false;
        int i = 0;
        while (i < options.length && !isCorrect) {
            if (options[i].equals(inputChar)) {
                isCorrect = true;
            }
            i++;
        }
        if (isCorrect) {
            return (inputChar);
        } else {
            System.out.println("Input was not an option. Please try again.");
            inputChar = receiveStringInput(flavourText, options, true, outputLength);//If they didn't get it right the first time, supply options.
        }
        return inputChar;
    }

    private static void printCharTimes(char c, int times, boolean newLine) {
        for (int i = 0; i < times; i++) {
            System.out.print(c);
        }
        if (newLine) {
            System.out.println();
        }
    }

    //Page Main Menu Methods
    private void addHolding() {
        //Setting options that will be used later on as choices.
        String choiceOptions[] = {"y", "n"};
        String[] typeOptions = {"v", "b"};
        Scanner input = new Scanner(System.in);

        //Creating the first page
        newPage("Add Holding");
        //Using the the method to take input.
        char type = receiveStringInput("Enter type of holding: Video/Book", typeOptions, true, 1).charAt(0);

        //Not a choice, requesting input. If they don't enter anything it will be caught below, and one will be generated for them.
        System.out.println("Enter the unique 6 digit ID or press enter to have one generated:");
        String ID = input.nextLine();

        //Check validity of the entered information
        String IDResult = inv.checkID(ID, type);
        //Print the result
        System.out.println(IDResult);
        switch (IDResult.toLowerCase()) {
            case "wrong number of digits":
            case "already taken":
                //If the ID number was already taken or had the wrong number of digits request a new one or give the user an already generated one.
                String newID = inv.generateValidID(type);
                //Using the variant of the choice input that has a default option. Pressing enter will select that default option, rather than prompting for a non null result.
                char answer = receiveStringInput(newID + " is not taken. Do you want to set it as the id?", choiceOptions, "y", 1).charAt(0);
                //If the user has selected to use the generated ID, use that one.
                if (answer == 'y') {
                    ID = newID;
                } else { //If the user is an idiot and wants to try entering a 6 digit number that they somehow got wrong the first time again, give them the choice.
                    do {
                        //Ask for the new ID
                        System.out.println("Enter new ID");
                        //Receive the new ID as input
                        ID = input.nextLine();
                        //Print the result
                        System.out.println(inv.checkID(ID, type));
                    }
                    while (!inv.checkID(ID, type).equalsIgnoreCase("valid")); //If they somehow still fail to enter a 6 digit number that is different from the other 15 6 digit number, let them try again.
                }
                break;
            case "invalid type":
                //If they entered type is incorrect(somehow, as the input method won't let an incorrect one be entered), request a valid one.
                do {
                    type = receiveStringInput("Invalid type, try again", typeOptions, true, 1).charAt(0);
                    System.out.println(inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
                break;
            case "id contains characters":
                do {
                    System.out.println("Invalid ID. Can only contain numbers, try again");
                    ID = input.nextLine();
                    System.out.println(inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
                break;
            case "valid": //If it is valid, keep going.
                break;
            default: //Any further errors will be caused by the ID been incorrect, so prompt for it again.
                do {
                    System.out.println("Enter new ID");
                    ID = input.nextLine();
                    System.out.println(inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
        }
        //The ID is done now.

        //Set a String representation of the type for use in prompts.
        String typeName;
        if (type == 'b') {
            typeName = "Book";
        } else if (type == 'v') {
            typeName = "Video";
        } else {
            typeName = "Incorrect";
        }

        //Create a new page.
        newPage("Add " + typeName);
        //Tell the user what they just entered.
        System.out.println("ID: " + type + ID);
        //Now prompt them for the title of the holding
        System.out.println("Enter the title of the " + typeName);
        String title = input.nextLine();
        while (title.length() == 0) { //If the title was not entered, prompt the user until they get it right.
            //Holy shit if they can't even enter a title what are they going to do with a book? How will they know how to put the dvd in?
            System.out.println("Title not entered correctly, try again");
            title = input.nextLine();
        }

        //Title is now set.
        int loanFee = 10;
        //If the item is a video, the loan fee varies. Ask the user what it will be.
        if (type == 'v') {
            //Give the user the possible loan values
            String[] loanOptions = {"4", "6"};
            //Use the choice method to request the loan fee. Convert the string result to int using a built in method. We don't need to catch here, but shoudld. //// TODO: 2/05/2016 Add a catch for any possible errors.
            loanFee = Integer.parseInt(receiveStringInput("Enter the loan fee", loanOptions, true, 1)); //http://stackoverflow.com/questions/5585779/converting-string-to-int-in-java
        }

        //We have all the info from the user.
        newPage("Add " + typeName + ": Confirmation");
        //Print all the info they have entered.
        System.out.println("ID:" + type + ID);
        System.out.println("Title: " + title);
        if (type == 'v') System.out.println("Loan Fee: " + loanFee);
        //Prompt the user for confirmation before creating the holding. Default to yes.
        char choice = receiveStringInput("Check Details. Are they correct?", choiceOptions, "y", 1).charAt(0);
        if (choice == 'y') {
            //If they say yes add the method, record its success.
            boolean added = inv.addHolding(ID, type, title, loanFee);
            //If it failed tell the user.
            if (!added) {
                System.out.println("Adding failed. Please try again.");
            } else System.out.println("Adding successful. Holding created");
            //Done. Return to main menu. // TODO: 2/05/2016 Prompt the user to save database to file here.
            System.out.println("Press enter to return to main menu");
            input.nextLine();
            mainMenu();
        } else if (choice == 'n') {
            //If they choose not to continue, return them to to main menu.
            System.out.println("Creation canceled. No holding was created. Please start from beginning");
            System.out.println("Press enter to return to main menu");
            input.nextLine();
            mainMenu();
        }
    } //Done

    private void removeHolding() {
        newPage("Remove Holding");
        System.out.println("Enter Holding ID:");
        String ID = input.nextLine();
        boolean result = inv.removeHolding(ID);
        while (!result) {
            char choice;
            do {
                System.out.println("Incorrect holding ID. Nothing was removed. Do you want to try again? y/n");
                choice = input.nextLine().toLowerCase().charAt(0);
            } while (!(choice == 'y') && !(choice == 'n'));
            if (choice == 'y') {
                ID = input.nextLine();
                result = inv.removeHolding(ID);
            } else {
                mainMenu();
            }
        }
    }

    private void addMember() {
        newPage("Add Member");
        System.out.println("Enter Name:");
        String name = input.nextLine();
        // TODO: 19/04/2016 Holding Logic Here
    }

    private void removeMember() {
        newPage("Remove Member");
        System.out.println("Enter Member ID:");
        // TODO: 19/04/2016 Holding Logic Here
        System.out.println("Enter Surname");
        // TODO: 19/04/2016 Members can only be removed if all data is correct. If user enters su mode, it doesn't need to be.
    }

    private void borrowHolding() {
        newPage("Borrow");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
    }

    private void returnHolding() {
        newPage("Return");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
    }

    private void printAllHoldings() {
        newPage("Holding Listing");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
    }

    private void printAllMembers() {
        newPage("Members Listing");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
    }

    private void printHolding() {
        Scanner input = new Scanner(System.in);
        newPage("Holding");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
        String ID = input.nextLine();
        newPage("Holding: " + ID);
    }

    private void printMember() {
        Scanner input = new Scanner(System.in);
        newPage("Members Listing");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
        String ID = input.nextLine();
        newPage("Member" + ID);
    }

    private void save() {
        newPage("Save");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
    }

    private void load() {
        newPage("Load File");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
        // TODO: 19/04/2016 Add-  not replace
    }

    private void exit() {
        Scanner input = new Scanner(System.in);
        newPage("Exit");
        System.out.println("Do you want to exit? y/n");
        String exit = input.nextLine();
        if (exit.charAt(0) == 'y') {
            exit();
        }

    }

    //Page Admin Menu Pages
    private void edit() {
        //Establishing type
        newPage("Edit");
        String type;
        boolean result = false;
        System.out.println("Enter the type of item you want to edit. (Member/Holding");
        type = input.nextLine();
        while (!type.equalsIgnoreCase("member") || !type.equalsIgnoreCase("holding")) {
            System.out.println("Incorrect entry. Check spelling and try again. (Member/Holding)");
            type = input.nextLine();
        }
        String typeExact;
        System.out.print("Now enter the type of ");
        if (type.equalsIgnoreCase("member")) {
            System.out.print("Member (Standard/Premium)");
            typeExact = input.nextLine();
            while (!typeExact.equalsIgnoreCase("standard") || !typeExact.equalsIgnoreCase("premium")) {
                System.out.println("Incorrect entry. Check spelling and try again. (Standard/Premium)");
                typeExact = input.nextLine();
            }
        } else if (type.equalsIgnoreCase("holding")) {
            System.out.print("Holding (Book/Video)");
            typeExact = input.nextLine();
            while (!typeExact.equalsIgnoreCase("book") || !typeExact.equalsIgnoreCase("video")) {
                System.out.println("Incorrect entry. Check spelling and try again. (Book/Video)");
                typeExact = input.nextLine();
            }
        }
    }

    private void editHolding() {
        newPage("Edit Holding");
        System.out.println(" 1. ID");
        System.out.println(" 2. Title");
        System.out.println(" 3. Loan Cost");
        System.out.println(" 4. Daily Loan Amount");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Enter your selections separated by commas");
    }

    private void editMember() {
    }

    private void activateHolding() {

    }

    private void deactivateHolding() {

    }

    private void resetMemberCredit() {

    }

    private void temporaryRemoveHolding() {

    }

    private void returnHoldingNoFee() {

    }

}
