/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.util;

import java.util.Random;

/**
 * Created by akinr on 20/04/2016 as part of s3603437_A2
 */
public class Utilities {
    //ANSI Codes from http://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ERROR_MESSAGE = ANSI_RED + "Error:" +ANSI_RESET;
    public static final String INFORMATION_MESSAGE = ANSI_CYAN + "INFORMATION:" + ANSI_RESET;
    public static final String WARNING_MESSAGE = ANSI_YELLOW + "WARNING:" + ANSI_RESET;
    public static String randomID() {
        int[] numbers = new int[6];
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            numbers[i] = random.nextInt(10);
        }
        return (numbers[0] + "" + numbers[1] + numbers[2] + numbers[3] + numbers[4] + numbers[5]);
    }
}
