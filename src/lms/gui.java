/*
*@author - Alex Kinross-Smith
*/
package lms;

import lms.util.DateTime;

import java.util.Scanner;

/**
 * Created by akinr on 11/04/2016.
 */
public class gui {
    private final String[] choiceOptions = {"y", "n","e","yes","no","exit"};
    private final String[] memberOptions = {"s", "p","standard","premium"};
    private final String[] holdingOptions = {"b", "v","book","video"};
    private final char[] memberTypes = {'s', 'p'};
    private final char[] holdingTypes = {'b', 'v'};
    private Scanner input = new Scanner(System.in);
    private Inventory inv = new Inventory();
    //Functional Methods

    /**
     * Prints the specified character "c" number of "times", then if new line == tru prints a new line at the end
     **/
    public static void printCharTimes(char c, int times, boolean newLine) {
        for (int i = 0; i < times; i++) {
            System.out.print(c);
        }
        if (newLine) {
            System.out.println();
        }
    }

    /**
     * Creates a new page with the title "title"
     **/
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
     * Returns an array of Strings (for example {a,b,c} as a String in the format (a/b/c)
     **/
    private String stringArrayToString(String[] array) {
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

    /**
     * Receives an input. Prints the flavourText, then requests input from the user. Will continue requesting input from the user until input matches an entry in the array options. if printOptionText is false will not show the user what options are avaliable. Final number is length of returned string
     **/
    private String receiveStringInput(String flavourText, String[] options, boolean printOptionText, int outputLength) {
        System.out.print(flavourText + " ");
        if (outputLength <= 0) {
            outputLength = 1;
        }
        if (printOptionText) {
            System.out.println(stringArrayToString(options));
        } else System.out.println();
        String inputString = input.nextLine().toLowerCase();
        while (inputString.length() == 0) {
            System.out.println("Answer needs to be entered");
            System.out.print(flavourText + " ");
            if (printOptionText) {
                System.out.println(stringArrayToString(options));
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

    /**
     * Receives an input. Prints the flavourText, then requests input from the user. Will continue requesting input from the user until input matches an entry in the array options or is empty. if it is empty returns the defaultAnswer. Final number is length of returned string
     **/
    private String receiveStringInput(String flavourText, String[] options, String defaultAnswer, int outputLength) {
        System.out.println(flavourText + " " + stringArrayToString(options) + "[" + defaultAnswer + "]");
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
            inputChar = receiveStringInput(flavourText, options, defaultAnswer, outputLength);//If they didn't get it right the first time, supply options.
        }
        return inputChar;
    }

    /**
     * Propts the user for an ID and returns it. typeID is a string that will be displayed when asking the user for and ID, and expectedTypes restricts input(i.e if someone enters s000001, but the user is searching for a holding it wont work.
     **/
    private String getExistingID(String typeID, char[] expectedTypes) {
        System.out.println("Enter " + typeID + " ID:");
        String ID = input.nextLine();
        boolean result = inv.idExists(ID);
        if (result) {
            for (int i = 0; i < expectedTypes.length; i++)
                if (ID.charAt(0) != expectedTypes[i]) {
                    result = false;
                }
        }
        char choice;
        //If the ID doesn't exist, prompt the user for a working ID
        if (!result) {
            //Ask them if they want to continue or return to the main menu
            choice = receiveStringInput("Incorrect " + typeID + " ID. Do you want to try again?", choiceOptions, "y", 1).charAt(0);
            //If they want to continue give them 4 tries so they don't get stuck here.
            if (choice == 'y') {
                System.out.println("You have 4 tries before returning to main menu");
                int tries = 0;
                //End the loop either upon entering a correct id, or using up all the tries
                while (!result && tries < 4) {
                    System.out.println((tries + 1) + ": Enter a valid " + typeID + " ID:");
                    ID = input.nextLine();
                    result = inv.idExists(ID);
                    if (result) {
                        for (int i = 0; i < expectedTypes.length; i++)
                            if (ID.charAt(0) != expectedTypes[i]) {
                                result = false;
                            }
                    }
                    tries++;
                }
                //If they time out because of tries, return them to main menu
                if (tries == 4) {
                    mainMenu();
                }
                //If they choose no earlier return them to main menu.
            } else {
                mainMenu();
            }
        }
        return (ID);
    }

    /**
     * Adds the default holdings
     **/
    public void addDefault() {
        String[] holdingTitle = {"Intro to Java", "Learning UML", "Design Patterns", "Advanced Java", "Java 1", "Java 2", "UML 1", "UML 2"};
        char[] holdingType = {'b', 'b', 'b', 'b', 'v', 'v', 'v', 'v'};
        String[] holdingID = {"000001", "000002", "000003", "000004", "000001", "000002", "000003", "000004"};
        int[] holdingFee = {0, 0, 0, 0, 4, 6, 6, 4};
        for (int i = 0; i < holdingID.length; i++) {
            inv.addHolding(holdingID[i], holdingType[i], holdingTitle[i], holdingFee[i]);
        }
        String[] memberID = {"000001", "000001", "000002", "000002"};
        String[] memberName = {"Joe Bloggs", "Fred Bloggs", "Jane Smith", "Fred Smith"};
        char[] memberType = {'s', 'p', 's', 'p'};
        for (int i = 0; i < memberID.length; i++) {
            inv.addMember(memberID[i], memberType[i], memberName[i]);
        }
        //inv.recalculateStatistics();
    }

    private String getValidID(String typeID, char type){

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
                    type = receiveStringInput("Invalid type, try again", holdingOptions, true, 1).charAt(0);
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
        return ID;
    }

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
        input.nextLine();
        String[] choiceOptions = {"default", "d", "saved", "s", "new", "n"};
        char choice = receiveStringInput("Do you want to load the default inventory, a saved inventory or start new?", choiceOptions, true, 1).charAt(0);
        if (choice == 'd') {
            addDefault();
        } else if (choice == 's') {
            load();
        }
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
                adminMenuVerify();
                break;
            default:
                break;
        }
    }

    private void adminMenuVerify() {
        newPage("Admin Verification");
        System.out.println("Please enter your admin password (VerySecure):");
        String password = input.nextLine();
        if (password.equals("Password1") || password.equals("VerySecure")) {
            adminMenu();
        } else {
            System.out.println("Incorrect Password. Returning to main menu");
            //todo- 1 second delay.
        }

    }

    private void adminMenu() {
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
                break;
            default:
                adminMenu();
        }
    }


    //Page Main Menu Methods
    private void addHolding() {
        //Using the the method to take input.

        //Creating the first page
        newPage("Add Holding");
        char type = receiveStringInput("Enter type of holding: Video/Book", holdingOptions, true, 1).charAt(0);
        String ID = getValidID("Holding",type);
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

    private void addMember() {
        //Setting options that will be used later on as choices.
        Scanner input = new Scanner(System.in);

        //Creating the first page
        newPage("Add Member");
        //Using the the method to take input.
        char type = receiveStringInput("Enter type of member: Standard/Premium", memberOptions, true, 1).charAt(0);
        String ID = getValidID("Holding",type);

        //The ID is done now.

        //Set a String representation of the type for use in prompts.
        String typeName;
        if (type == 's') {
            typeName = "Standard Member";
        } else if (type == 'v') {
            typeName = "Premium Member";
        } else {
            typeName = "Incorrect";
        }

        //Create a new page.
        newPage("Add " + typeName);
        //Tell the user what they just entered.
        System.out.println("ID: " + type + ID);
        //Now prompt them for the name of the holding
        System.out.println("Enter the name of the " + typeName);
        String name = input.nextLine();
        while (name.length() == 0) { //If the name was not entered, prompt the user until they get it right.
            System.out.println("Name not entered correctly, try again");
            name = input.nextLine();
        }

        //Title is now set.


        //We have all the info from the user.
        newPage("Add " + typeName + ": Confirmation");
        //Print all the info they have entered.
        System.out.println("ID:" + type + ID);
        System.out.println("Name: " + name);
        //Prompt the user for confirmation before creating the holding. Default to yes.
        char choice = receiveStringInput("Check Details. Are they correct?", choiceOptions, "y", 1).charAt(0);
        if (choice == 'y') {
            //If they say yes add the method, record its success.
            boolean added = inv.addMember(ID, type, name);
            //If it failed tell the user.
            if (!added) {
                System.out.println("Adding failed. Please try again.");
            } else System.out.println("Adding successful. Member created");
            //Done. Return to main menu. // TODO: 2/05/2016 Prompt the user to save database to file here.
            System.out.println("Press enter to return to main menu");
            input.nextLine();
            mainMenu();
        } else if (choice == 'n') {
            //If they choose not to continue, return them to to main menu.
            System.out.println("Creation canceled. No member was created. Please start from beginning");
            System.out.println("Press enter to return to main menu");
            input.nextLine();
            mainMenu();
        }
    } //Done

    private void removeHolding() {
        char choice;
        newPage("Remove Holding");
        if (inv.getNumberOfHoldings() == 0) {
            System.out.printf("There are no holdings to remove. \nPlease press enter to return to main menu");
            input.nextLine();
            mainMenu();
        }
        String ID = getExistingID("Holding", holdingTypes);
        boolean result;
        inv.printHolding(ID);
        //We now have a correct id. Ask the user if they want to delete the holding.
        choice = receiveStringInput("Do you want to remove this holding? Please confirm", choiceOptions, "n", 1).charAt(0);
        //If they do, remove the holding.
        if (choice == 'y') {
            result = inv.removeHolding(ID);
            //If successful, inform them it was, and return them to the menu. // TODO: 2/05/2016 Add save functionality.
            if (result) {
                System.out.println(ID + " deleted. Press enter to return to main menu.");
                input.nextLine();
            } else {
                //If the holding is somehow not deleted after all the checks, inform the user.
                System.out.println("Holding deletion failed. Please report to developer and try again. Press enter to return to main menu");
                input.nextLine();
            }
            //If they decide not to delete the holding, return them to the menu.
        } else {
            System.out.println("Holding deletion canceled. Press enter to return to main menu.");
            input.nextLine();
        }
        mainMenu();
    } //Done

    private void removeMember() {
        char choice;
        newPage("Remove Member");
        if (inv.getNumberOfMembers() == 0) {
            System.out.printf("There are no members to remove. \nPlease press enter to return to main menu");
            input.nextLine();
            mainMenu();
        }
        //Request member ID
        String ID = getExistingID("Member", memberTypes);
        boolean result;
        inv.printMember(ID);
        //We now have a correct id. Ask the user if they want to delete the member.
        choice = receiveStringInput("Do you want to remove this member? Please confirm", choiceOptions, "n", 1).charAt(0);
        //If they do, remove the member.
        if (choice == 'y') {
            result = inv.removeMember(ID);
            //If successful, inform them it was, and return them to the menu. // TODO: 2/05/2016 Add save functionality.
            if (result) {
                System.out.println(ID + " deleted. Press enter to return to main menu.");
                input.nextLine();
            } else {
                //If the member is somehow not deleted after all the checks, inform the user.
                System.out.println("Member deletion failed. Please report to developer and try again. Press enter to return to main menu");
                input.nextLine();
            }
            //If they decide not to delete the member, return them to the menu.
        } else {
            System.out.println("Member deletion canceled. Press enter to return to main menu.");
            input.nextLine();
        }
        mainMenu();
    } //Done

    private void borrowHolding() {
        newPage("Borrow");
        String memberID = getExistingID("Member", memberTypes);
        System.out.println(inv.getMemberName(memberID));
        char choice = receiveStringInput("Is this your name?", choiceOptions, "y", 1).charAt(0);
        if (choice == 'n') {
            System.out.println("Holding has not been borrowed. Press enter to return to menu.");
            input.nextLine();
            mainMenu();
        }
        String holdingID = getExistingID("Holding", holdingTypes);
        inv.printHolding(holdingID);
        choice = receiveStringInput("Do you want to borrow this holding?", choiceOptions, "y", 1).charAt(0);
        if (choice == 'y') {
            boolean result = inv.borrowHolding(holdingID, memberID);
            if (result) {
                System.out.println("Holding " + holdingID + " has been borrowed. Press enter to return to menu.");
                input.nextLine();
                mainMenu();
            } else {
                System.out.println("Holding was not been borrowed. Press enter to return to menu.");
                input.nextLine();
                mainMenu();
            }
        } else {
            System.out.println("Holding has not been borrowed. Press enter to return to menu.");
            input.nextLine();
            mainMenu();
        }

    }

    private void returnHolding() {
        newPage("Return");
        String memberID = getExistingID("Member", memberTypes);
        System.out.println(inv.getMemberName(memberID));
        char choice = receiveStringInput("Is this your name?", choiceOptions, "y", 1).charAt(0);
        if (choice == 'n') {
            System.out.println("No holding was returned. Press enter to return to menu.");
            input.nextLine();
            mainMenu();
        }
        String holdingID = getExistingID("Holding", holdingTypes);
        inv.printHolding(holdingID);
        choice = receiveStringInput("Do you want to return this holding?", choiceOptions, "y", 1).charAt(0);
        if (choice == 'y') {
            boolean result = inv.returnHolding(holdingID, memberID);
            if (result) {
                System.out.println("Holding " + holdingID + " has been returned. Press enter to return to menu.");
                input.nextLine();
                mainMenu();
            } else {
                System.out.println("Holding was not been returned. Press enter to return to menu.");
                input.nextLine();
                mainMenu();
            }
        } else {
            System.out.println("Holding has not been returned. Press enter to return to menu.");
            input.nextLine();
            mainMenu();
        }

    }

    private void printAllHoldings() {
        newPage("Holding Listing");
        inv.printAllHoldings();
    }

    private void printAllMembers() {
        newPage("Members Listing");
        inv.printAllMembers();
    }

    private void printHolding() {
        newPage("Holding");
        String ID = getExistingID("Holding", holdingTypes);
        newPage("Holding: " + ID);
        inv.printHolding(ID);
    }

    private void printMember() {
        newPage("Member");
        String ID = getExistingID("Member", memberTypes);
        newPage("Member: " + ID);
        inv.printHolding(ID);
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
        char choice = receiveStringInput("You you want to exit?", choiceOptions, "y", 1).charAt(0);
        if (choice == 'y') {
            System.exit(0);
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
