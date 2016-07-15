/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui;

import lms.util.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import static lms.ui.UI.*;

/**
 * Created by kin0025 on 15/07/2016.
 */
public  class Input {

    
    
    /**
     * Receives an input. Prints the flavourText, then requests input from the user. Will continue requesting input from the user until input matches an entry in the array options. if printOptionText is false will not show the user what options are available.
     * Final number is length of returned string and length of input that will be compared to the strings.
     *
     * @param flavourText     Printed before every request for input.
     * @param options         The array of <code>Strings</code> that is used for validation of input.
     * @param printOptionText If true, display the potential inputs to the user. If false, don't display it.
     * @param outputLength    The length of input that will be validated and output.
     * @return The final validated chosen user input of the first <code>int outputLength</code> positions.
     **/
    public String receiveStringInput(String flavourText, String[] options, boolean printOptionText, int outputLength) {
        //Print the flavour text.
        System.out.print(flavourText + " ");
        //Make sure that we don't npe anywhere
        if (outputLength <= 0) {
            outputLength = 1;
        }
        //If we have enabled option printing, print the array.
        if (printOptionText) {
            System.out.println(Utilities.stringArrayToString(options));
        } else System.out.println(); //Otherwise end the line.
        //Receive input
        System.out.print(Utilities.INPUT_MESSAGE);
        String inputString = UI.input.nextLine().toLowerCase();
        //If it is too short, prompt for input again.
        while (inputString.length() == 0) {
            //Print the stuff again.
            System.out.println(Utilities.INFORMATION_MESSAGE + "Answer needs to be entered");
            System.out.print(flavourText + " ");
            if (printOptionText) {
                System.out.println(Utilities.stringArrayToString(options));
            } else System.out.println();
            //Request input again.
            System.out.print(Utilities.INPUT_MESSAGE);
            inputString = UI.input.nextLine().toLowerCase();
        }
        //Ensure input is the same.
        inputString = inputString.toLowerCase();
        //If the input we are given is longer than the specified maximum output, make it shorter.
        if (inputString.length() >= outputLength) {
            inputString = inputString.substring(0, outputLength);
        }
        boolean isCorrect = false;
        int i = 0;
        //Check it matches input parameters.
        while (i < options.length && !isCorrect) {
            String currentExamined;
            if (options[i].length() >= outputLength) {
                currentExamined = options[i].substring(0, outputLength);
            } else currentExamined = options[i];
            if (currentExamined.equals(inputString)) {
                isCorrect = true;
            }
            i++;
        }
        if (isCorrect) {
            return (inputString);
        } else {
            System.out.println(Utilities.ERROR_MESSAGE + "Input was not an option. Please try again.");
            inputString = receiveStringInput(flavourText, options, true, outputLength);//If they didn't get it right the first time, supply options.
        }
        return (inputString);
    }

    /**
     * Receives an input. Prints the flavourText, then requests input from the user. Will continue requesting input from the user until input matches an entry in the array options or is empty. if it is empty returns the defaultAnswer. Final number is length of returned string
     *
     * @param flavourText   Printed before every request for input.
     * @param options       The array of <code>Strings</code> that is used for validation of input.
     * @param defaultAnswer The default solutions. If no input is received this is returned.
     * @param outputLength  The length of input that will be validated and output.
     * @return The final validated chosen user input of the first <code>int outputLength</code> positions.
     **/
    public static String receiveStringInput(String flavourText, String[] options, String defaultAnswer, int outputLength, int waitTime) {
        //Print a prompt for the user to enter input.
        System.out.println(flavourText + " " + Utilities.stringArrayToString(options) + Utilities.ANSI_RED + "[" + defaultAnswer + "]" + Utilities.ANSI_RESET);
        //Ensure we don't try to access a negative array index.
        if (outputLength <= 0) {
            outputLength = 1;
        }
        //Actually get input.
        String inputString;
        if (waitTime == 0) {
            System.out.print(Utilities.INPUT_MESSAGE);
            inputString = UI.input.nextLine().toLowerCase();
        } else {
            inputString = returnWaitInput(waitTime, defaultAnswer).toLowerCase();

        }//If the user entered nothing, return the default input.
        if (inputString.length() == 0) {
            System.out.println(defaultAnswer);
            return ("" + defaultAnswer);
        }
        inputString = inputString.toLowerCase();
        if (inputString.length() >= outputLength) {
            inputString = inputString.substring(0, outputLength);
        }
        boolean isCorrect = false;
        int i = 0;
        //Check it matches a valid input
        while (i < options.length && !isCorrect) {
            String currentExamined;
            if (options[i].length() >= outputLength) {
                currentExamined = options[i].substring(0, outputLength);
            } else currentExamined = options[i];
            if (currentExamined.equals(inputString)) {
                isCorrect = true;
            }
            i++;
        }
        if (isCorrect) {
            return (inputString);
        } else {
            System.out.println(Utilities.ERROR_MESSAGE + "Input was not an option. Please try again.");
            inputString = receiveStringInput(flavourText, options, defaultAnswer, outputLength);//If they didn't get it right the first time, supply options.
        }
        return inputString;
    }

    /**
     * Wrapper for default string input with no auto input.
     *
     * @param flavourText   Printed before every request for input.
     * @param options       The array of <code>Strings</code> that is used for validation of input.
     * @param defaultAnswer The default solutions. If no input is received this is returned.
     * @param outputLength  The length of input that will be validated and output.
     * @return The final validated chosen user input of the first <code>int outputLength</code> positions.
     **/
    public static String receiveStringInput(String flavourText, String[] options, String defaultAnswer, int outputLength) {
        return receiveStringInput(flavourText, options, defaultAnswer, outputLength, 0);
    }

    /**
     * Prompts the user for an ID that already exists.
     * TypeID is a string that will be displayed when asking the user for and ID, and expectedTypes restricts input(i.e if someone enters s000001, but the user is searching for a holding it wont work.
     * If user doesn't want to enter input returns null.
     *
     * @param typeID        Used strictly for printing to the user. Displayed in user facing prompts.
     * @param expectedTypes The input ID must start with one of these expected types.
     * @return The ID chosen by the user.
     **/
    public static String getExistingID(String typeID, char[] expectedTypes) {
        System.out.println("Enter " + typeID + " ID:");
        System.out.print(Utilities.INPUT_MESSAGE);
        String ID = UI.input.nextLine();
        boolean result = inv.idExists(ID);
        if (result) {
            result = false;
            for (char expectedType : expectedTypes)
                if (ID.charAt(0) == expectedType) {
                    result = true;
                }
        }
        char choice;
        //If the ID doesn't exist, prompt the user for a working ID
        if (!result) {
            //Ask them if they want to continue or return to the main menu
            choice = receiveStringInput("Incorrect " + typeID + " ID. Do you want to try again?", CHOICE_OPTIONS, "y", 1).charAt(0);
            if (choice == 'e') return null;
            //If they want to continue give them 4 tries so they don't get stuck here.
            if (choice == 'y') {
                System.out.println(Utilities.INFORMATION_MESSAGE + "You have 4 tries before returning to main menu");
                int tries = 0;
                //End the loop either upon entering a correct id, or using up all the tries
                while (!result && tries < 4) {
                    System.out.println((tries + 1) + ": Enter a valid " + typeID + " ID:");
                    System.out.print(Utilities.INPUT_MESSAGE);
                    ID = UI.input.nextLine();
                    result = inv.idExists(ID);
                    if (result) {
                        result = false;
                        for (char expectedType : expectedTypes)
                            if (ID.charAt(0) == expectedType) {
                                result = true;
                            }
                    }
                    tries++;
                }
                //If they time out because of tries, return them to main menu
                if (tries == 4) {
                    return null;
                }
                //If they choose no earlier return them to main menu.
            } else {
                return null;
            }
        }
        return (ID);
    }

    /**
     * Prompts the user for a formatting valid ID, conforming to the types possible.
     *
     * @param typeID Used for user input in user facing prompts i.e Enter type of <code>typeID</code> :
     * @param types  Used by <code>{@link #receiveStringInput(String, String[], boolean, int)}</code> to get the type of ID to be created.
     * @return 2 long array of Strings. First position is the type, Second position is the 6 digit ID.
     */
    public String[] getValidID(String typeID, String[] types) {
        char type;
        if (types.length > 1) {
            type = receiveStringInput("Enter type of " + typeID + ":", types, true, 1).charAt(0);
        } else {
            type = types[0].charAt(0);
        }
        //Not a choice, requesting input. If they don't enter anything it will be caught below, and one will be generated for them.
        System.out.println("Enter the unique 6 digit ID or press enter to have one generated:");
        System.out.print(Utilities.INPUT_MESSAGE);
        String ID = UI.input.nextLine();
        if (ID != null && ID.toLowerCase().equals("e")) {
            return null;
        }
        //Check validity of the entered information
        String IDResult = inv.checkID(ID, type);
        //Print the result
        switch (IDResult.toLowerCase()) {
            case "wrong number of digits":
            case "already taken":
                //If the ID number was already taken or had the wrong number of digits request a new one or give the user an already generated one.
                String newID = inv.generateValidID(type);
                //Using the variant of the choice input that has a default option. Pressing enter will select that default option, rather than prompting for a non null result.
                char choice = receiveStringInput(newID + " is not taken. Do you want to set it as the id?", CHOICE_OPTIONS, "y", 1).charAt(0);
                //If the user has selected to use the generated ID, use that one.
                if (choice == 'y') {
                    ID = newID;
                } else { //If the user is an idiot and wants to try entering a 6 digit number that they somehow got wrong the first time again, give them the choice.
                    do {
                        //Ask for the new ID
                        System.out.println(Utilities.INFORMATION_MESSAGE + "Enter new 6 digit ID number");
                        //Receive the new ID as input
                        System.out.print(Utilities.INPUT_MESSAGE);
                        ID = UI.input.nextLine();
                        //Print the result
                        System.out.println(Utilities.ERROR_MESSAGE + inv.checkID(ID, type));
                    }
                    while (!inv.checkID(ID, type).equalsIgnoreCase("valid")); //If they somehow still fail to enter a 6 digit number that is different from the other 15 6 digit number, let them try again.
                }
                break;
            case "invalid type":
                //If they entered type is incorrect(somehow, as the input method won't let an incorrect one be entered), request a valid one.
                do {
                    type = receiveStringInput("Invalid type, try again", HOLDING_OPTIONS, true, 1).charAt(0);
                    System.out.println(Utilities.ERROR_MESSAGE + inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
                break;
            case "id contains characters":
                do {
                    System.out.println(Utilities.WARNING_MESSAGE + "Invalid ID. Can only contain numbers, try again");
                    System.out.print(Utilities.INPUT_MESSAGE);
                    ID = UI.input.nextLine();
                    System.out.println(Utilities.ERROR_MESSAGE + inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
                break;
            case "valid": //If it is valid, keep going.
                break;
            default: //Any further errors will be caused by the ID been incorrect, so prompt for it again.
                do {
                    System.out.println(Utilities.INFORMATION_MESSAGE + "Enter new 6 digit ID number");
                    System.out.print(Utilities.INPUT_MESSAGE);
                    ID = UI.input.nextLine();
                    System.out.println(Utilities.ERROR_MESSAGE + inv.checkID(ID, type));
                } while (!inv.checkID(ID, type).equalsIgnoreCase("valid"));
        }
        return new String[]{"" + type, ID};
    }

    public static String returnWaitInput(int waitTime, String defaultResult) { //http://stackoverflow.com/questions/10059068/set-timeout-for-users-input (Jeffrey's answer)
        //Start an input
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        //Get the start time
        long startTime = System.currentTimeMillis();
        String result;

        //Tell them how long they have
        System.out.println(Utilities.INFORMATION_MESSAGE + "You have " + waitTime + " seconds until an answer is chosen for you. You can press enter to choose the default." + Utilities.ANSI_GREEN + "Press enter once you have entered your input" + Utilities.ANSI_RESET);
        System.out.print(Utilities.INPUT_MESSAGE);
        try {
            //noinspection StatementWithEmptyBody
            //Wait for 1000 * wait time milliseconds
            //noinspection StatementWithEmptyBody
            while ((System.currentTimeMillis() - startTime) < waitTime * 1000
                    && !in.ready()) {
            }
            //Get the input
            if (in.ready()) {
                result = in.readLine();
            }
            //If there was no input default will be selected.
            else {
                System.out.println(Utilities.INFORMATION_MESSAGE + "No input was detected and a default answer has been selected");
                result = defaultResult;
            }
            if (result == null) {
                result = defaultResult;
            }
            return result;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            result = defaultResult;

        }
        return result;
    }

}
