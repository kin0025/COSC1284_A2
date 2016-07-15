/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui.Operations;

import lms.util.Utilities;
import static lms.ui.UI.*;
import static lms.ui.Input.*;
import static lms.ui.Shared.*;

/**
 * Created by kin0025 on 15/07/2016.
 */
public class MemberOperations {
    /**
     * Edits the name of members.
     *
     * @param types    Used for valid types for input.
     * @param typeName Used in user facing prompts.
     */
    public static void editName(char[] types, String typeName) {
        //Get the ID
        String oldID = getExistingID(typeName, types);
        //If it was entered correctly keep going
        if (oldID != null) {
            //Get input after prompting
            System.out.println("Please enter replacement name");
            System.out.print(Utilities.INPUT_MESSAGE);
            String name = input.nextLine();

            //Validate it
            while (name.length() > 0) {
                System.out.println(Utilities.WARNING_MESSAGE + "Please enter at least one character:");
                System.out.print(Utilities.INPUT_MESSAGE);
                name = input.nextLine();
            }
            //Replace the name and inform the user
            boolean result = inv.replaceName(oldID, name);
            if (!result) {
                System.out.println(Utilities.ERROR_MESSAGE + "Change failed.");
            }
        } else System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect ID, nothing was changed.");
    }



    /**
     * Resets the credit of the ID input by the user. Is for a member.
     */
    public static void resetMemberCredit() {
        //Get the ID
        String ID = getExistingID("Member", MEMBER_TYPES);
        if (ID != null) {
//Reset the credit
            inv.resetMemberCredit(ID);
            System.out.println(Utilities.INFORMATION_MESSAGE + "Credit was reset");
        }
    }


}
