/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.util;

import java.util.ArrayList;

/**
 * Created by Emily on 18/05/2016 as part of s3603437_A2
 */
public class IDManager{
    private static ArrayList<String> identifiers = new ArrayList<>();
public static boolean addIdentifier(String identifier){
    if(!isAlreadyTaken(identifier)){
        identifiers.add(identifier);
        return true;
    }
    return false;
}
    public static String stateString(){

        String megaString = null;
        for (String a:identifiers
             ) {
            megaString += a;
        }
        return megaString;
    }
       public static boolean removeIdentifier(String identifier) {
        int index = findIdentifier(identifier);
        if (index >= 0) {
            identifiers.remove(index);
            return true;
        }
        return false;
    }

    public static boolean isAlreadyTaken(String testID) {
        return findIdentifier(testID) >= 0;
    }

    private static int findIdentifier(String testID) {
        for (int i = 0; i < identifiers.size(); i++) {
            String actualID = identifiers.get(i);
            if (actualID != null && testID != null) {
                if(actualID.equals(testID)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static String generateUniqueID() {
        String test;
        boolean result;
        do {
            test = Utilities.randomID();
            result = isAlreadyTaken(test);
        } while (result);
   identifiers.add(test);
        return test;
    }
}
