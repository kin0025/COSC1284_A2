/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.util;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Alex on 18/05/2016 as part of s3603437_A2
 */
public class IDManager {
    private static final ArrayList<String> identifiers = new ArrayList<>();

    /**
     * Adds an identifier to the arraylist after checking if it is not already taken.
     *
     * @param identifier the identifier
     */
    public static void addIdentifier(String identifier) {
        if (!isAlreadyTaken(identifier)) {
            identifiers.add(identifier);
        }
    }

    /**
     * Returns a string of all unique IDs added together.
     *
     * @return All unique IDs added
     */
    public static String stateString() {

        String megaString;
        //works with java 1.7
        /*for (String a : identifiers
                ) {
            megaString += a;
        }*/
        //Requires Java 1.8
        megaString = String.join(":",identifiers);
        return megaString;
    }

    /**
     * Remove identifier from list.
     *
     * @param identifier the identifier to be removed
     * @return whether the identifier was removed.
     */
    public static boolean removeIdentifier(String identifier) {
        int index = identifier.indexOf(identifier);
        if (index >= 0) {
            identifiers.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Returns whether the ID is already in use.
     *
     * @param testID the test id
     * @return the boolean
     */
    public static boolean isAlreadyTaken(String testID) {
        return identifiers.indexOf(testID) >= 0;
    }


    /**
     * Generates UUID after checking it is unique.
     *
     * @return The generated UUID
     */
    public static String generateUUID() {
        String test;
        boolean result;
        do {
            test = String.valueOf(UUID.randomUUID());
            result = isAlreadyTaken(test);
        } while (result);
        identifiers.add(test);
        return test;
    }
}
