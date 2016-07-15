/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

/**
 * Created by akinr on 20/04/2016 as part of s3603437_A2
 */
@SuppressWarnings("WeakerAccess")
public class Utilities {

    public static final char[] MEMBER_TYPES = {'s', 'p'};
    public static final char[] HOLDING_TYPES = {'b', 'v'};
    public static final String FILE_EXTENSION = ".csv";
    //Comment the block below after enabling colours
    public static String ANSI_RESET = "";
    public static String ANSI_BLACK = "";
    public static String ANSI_RED = "";
    public static final String ERROR_MESSAGE = ANSI_RED + "Error:" + ANSI_RESET;
    public static String ANSI_GREEN = "";
    public static final String INPUT_MESSAGE = ANSI_GREEN + ":" + ANSI_RESET;
    public static String ANSI_YELLOW = "";
    public static final String WARNING_MESSAGE = ANSI_YELLOW + "WARNING:" + ANSI_RESET;
    public static String ANSI_BLUE = "";
    public static String ANSI_PURPLE = "";
    public static String ANSI_CYAN = "";
    public static final String INFORMATION_MESSAGE = ANSI_CYAN + "INFORMATION:" + ANSI_RESET;
    public static String ANSI_WHITE = "";
    public static boolean colours = false;

    /**
     * Generates a random 6 digit ID.
     *
     * @return the generated ID
     */
    public static String randomID() {
        int[] numbers = new int[6];
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            numbers[i] = random.nextInt(10);
        }
        return (numbers[0] + "" + numbers[1] + numbers[2] + numbers[3] + numbers[4] + numbers[5]);
    }

    /**
     * Checks if an ID meets the validity requirements-  the first character muse equal the expected type. It must be 7 long, and the last 6 digits must be numbers between 0 and 9.
     *
     * @param expectedType the expected type
     * @param ID           the id to be checked
     * @return true if ID is valid
     */
    public static boolean isIDValid(char expectedType, String ID) {
        boolean validID = true;
        int i = 1;
        while (ID.length() > i && validID) {
            if (!((int) ID.charAt(i) >= 48 && (int) ID.charAt(i) <= 58)) { //Ascii codes for numbers are between 48 and 58 (including 0). If each character is between these values, they are numbers.
                validID = false;
            }
            i++;
        }
        return ID.charAt(0) == expectedType && ID.length() == 7 && validID;

    }

    /**
     * Hashes a string passed to it with an MD5 algorithm.
     *
     * @param input the input string
     * @return the output MD5 hash
     */
    public static String hashString(String input) {
        String outputHash = null;
        //https://dzone.com/articles/get-md5-hash-few-lines-java
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            outputHash = new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return outputHash;
    }

    public static void toggleColours() {
        if (colours) {
            disableColours();
        } else {
            enableColours();
        }
    }

    public static void disableColours() {
        ANSI_RESET = "";
        ANSI_BLACK = "";
        ANSI_RED = "";
        ANSI_GREEN = "";
        ANSI_YELLOW = "";
        ANSI_BLUE = "";
        ANSI_PURPLE = "";
        ANSI_CYAN = "";
        ANSI_WHITE = "";
        colours = false;
        System.out.println("Colours disabled");

    }

    public static void enableColours() {

        //ANSI Codes from http://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
        //Un comment if the console you are using supports ANSI colours and stuff. Adds colours to the console output.

        ANSI_RESET = "\u001B[0m";
        ANSI_BLACK = "\u001B[30m";
        ANSI_RED = "\u001B[31m";
        ANSI_GREEN = "\u001B[32m";
        ANSI_YELLOW = "\u001B[33m";
        ANSI_BLUE = "\u001B[34m";
        ANSI_PURPLE = "\u001B[35m";
        ANSI_CYAN = "\u001B[36m";
        ANSI_WHITE = "\u001B[37m";
        colours = true;
        System.out.println("Colours enabled");
    }

    /**
     * Returns an array of Strings (for example {a,b,c} as a String in the format (a/b/c)
     *
     * @param array used as the input to formulate the final string.
     * @return A string in the format of (0/1/2/3/...) of the input array {0,1,2,3,...}
     **/
    public static String stringArrayToString(String[] array) {
        String result = Utilities.ANSI_YELLOW + "{" + Utilities.ANSI_RESET;
        //Add all the things together
        for (int i = 0; i < array.length; i++) {
            //If this isn't the last entry in the array, add a / on as well
            if (i < array.length - 1) {
                result += array[i] + Utilities.ANSI_YELLOW + "/" + Utilities.ANSI_RESET;
            } else { //If it is the last entry, add a closing bracket instead.
                result += array[i] + Utilities.ANSI_YELLOW + "}" + Utilities.ANSI_RESET;
            }
        }
        return (result);
    }
}
