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
        int options;
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
        printCharTimes('=', 50, true);
        System.out.println("Enter an option:");
        options = input.nextInt();
        switch (options) {
            case 1:
                addHolding();
                break;
            case 2:
                removeHolding();
                break;
            case 3:
                addMember();
                break;
            case 4:
                removeMember();
                break;
            case 5:
                borrowHolding();
                break;
            case 6:
                returnHolding();
                break;
            case 7:
                printAllHoldings();
                break;
            case 8:
                printAllMembers();
                break;
            case 9:
                printHolding();
                break;
            case 10:
                printMember();
                break;
            case 11:
                save();
                break;
            case 12:
                load();
                break;
            case 13:
                exit();
                mainMenu();
                break;
            case 14:
                adminMenu();
                break;
            default:
                mainMenu();
                break;
        }
    }

    public void adminMenuVerify(){
        newPage("Admin Verification");
        System.out.println("Please enter your admin password (VerySecure):");
        String password = input.nextLine();
        if(password.equals("Password1") || password.equals("VerySecure")){
            adminMenu();
        }else{
            System.out.println("Incorrect Password. Returning to main menu");
            //todo- 1 second delay.
            mainMenu();
        }

    }
    public void adminMenu(){
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
        switch(options) {
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
        for(int i=30; i != 0; i--){System.out.printf("\n");}
        printCharTimes('=', 100, true);

        String leftText = DateTime.getCurrentTime();
        String centreText = "Library Management System: " + title;
        String rightText = inv.infoPrintout();

        int left = leftText.length();
        int centre = centreText.length();
        int right = rightText.length();

        int leftSpacing = 50 - left - (int)centre/2;
        int rightSpacing = 49 - right - (int)centre/2;

        System.out.print(leftText);
        printCharTimes(' ', leftSpacing, false);
        System.out.print(centreText);
        printCharTimes(' ', rightSpacing, false);
        System.out.println(rightText);


        printCharTimes('=', 100, true);
        System.out.println();
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
        Scanner input = new Scanner(System.in);
        newPage("Add Holding");
        System.out.println("Enter type of holding: Video/Book (v/b/video/book)");
        char type = input.nextLine().toLowerCase().charAt(0);
        System.out.println("Enter the unique 6 digit ID:");
        String ID = input.nextLine();
        String IDResult = inv.checkID(ID,type);
        System.out.println("ID was " + IDResult);
        switch(IDResult.toLowerCase()){
            case "wrong number of digits":
            case "already taken":
                String newID = inv.generateValidID(type);
                System.out.println(newID + "is not taken. Do you want to set it as the id? (y/n)");
                char answer = input.nextLine().toLowerCase().charAt(0);
                if(answer == 'y'){
                    ID = newID;
                }else{
                    do {
                        System.out.println("Enter new ID");
                        ID = input.nextLine();
                        System.out.print(inv.checkID(ID,type));
                    } while(!inv.checkID(ID,type).equalsIgnoreCase("valid"));
                }
                break;
            case "invalid type":
                do {
                    System.out.println("Enter new type (b/v)");
                    type = input.nextLine().toLowerCase().charAt(0);
                    System.out.print(inv.checkID(ID,type));
                } while(!inv.checkID(ID,type).equalsIgnoreCase("valid"));

                break;
            case "valid":
                break;
            default:
                do {
                    System.out.println("Enter new ID");
                    ID = input.nextLine();
                    System.out.print(inv.checkID(ID,type));
                } while(!inv.checkID(ID,type).equalsIgnoreCase("valid"));
        }
        String typeName;
        if(type == 'b'){
            typeName = "Book";
        }else if(type == 'v'){
            typeName = "Video";
        }else {
            typeName = "Incorrect";
            }
        newPage("Add " + typeName);
        System.out.println("ID: " + type + ID);
        System.out.println("Enter the title of the " + typeName);
        String title = input.nextLine();
        int loanFee = 0;
        if(type == 'v') {
            System.out.println("Enter the Loan fee (4/6):");
            loanFee = input.nextInt();
            while(loanFee != 4 && loanFee != 6) {
                System.out.println("Incorrect value, please try again. (4/6)");
                loanFee = input.nextInt();
                input.nextLine();
            }
        }
        newPage("Add " + typeName + ": Confirmation");
System.out.println("ID:" + type + ID);
        System.out.println("Title: " + title);
        if(type == 'v') System.out.println("Loan Fee: " + loanFee);

        System.out.println("Check details. Are they correct? (y/n)");
        char answer = input.nextLine().toLowerCase().charAt(0);
        if(answer == 'y') {
            inv.addHolding(ID, type, title, loanFee);
            mainMenu();
        }else if(answer == 'n'){
            System.out.println("Creation canceled. Please start from beginning");
            mainMenu();
        }else{
            while(answer != 'n' && answer != 'y') {
                System.out.println("Incorrect value, please try again. (4/6)");
                answer = input.nextLine().toLowerCase().charAt(0);
            }
            if(answer == 'y') {
                inv.addHolding(ID, type, title, loanFee);
            }else {
                System.out.println("Creation canceled. Please start from beginning");
                mainMenu();
            }

        }
    }

    private void removeHolding() {
        newPage("Remove Holding");
        System.out.println("Enter Holding ID:");
        // TODO: 19/04/2016 Holding Logic Here
    }

    private void addMember() {
        newPage("Add Member");
        System.out.println("Enter Name:");
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
    private void  activateHolding(){

    }
    private void deactivateHolding(){

    }
    private void resetMemberCredit(){

    }
    private void temporaryRemoveHolding(){

    }
    private void returnHoldingNoFee(){

    }

}
