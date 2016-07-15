/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui.Operations;

import lms.exceptions.IncorrectDetailsException;
import lms.util.Utilities;
import static lms.ui.UI.*;
import static lms.ui.Input.*;
import static lms.ui.Shared.*;
import static lms.ui.Print.*;
/**
 * Created by kin0025 on 15/07/2016.
 */
public class HoldingOperations {
    /**
     * Edits the ID of either members or holdings.
     *
     * @param types    Used for valid types for input.
     * @param typeName Used in user facing prompts.
     */
    public static void editID(char[] types, String typeName) {
        //Get the ID
        String oldID = getExistingID(typeName, types);
        //If it was entered correctly keep going
        if (oldID != null) {
            String[] options = {oldID.substring(0, 1)};
            String[] idInfo = getValidID(typeName, options);
            if (idInfo == null) {
                return;
            }
            String newID = idInfo[0] + idInfo[1];
            //Replace the ID
            inv.replaceID(oldID, newID);
        } else System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect ID, nothing was changed.");

    }

    /**
     * Edits the title of holdings.
     *
     * @param types    Used for valid types for input.
     * @param typeName Used in user facing prompts.
     */
    public static void editTitle(char[] types, String typeName) {
        //Get the ID
        String oldID = getExistingID(typeName, types);
        //If it was entered correctly keep going
        if (oldID != null) {
            //Get input after prompting
            System.out.println("Please enter replacement title");
            System.out.print(Utilities.INPUT_MESSAGE);
            String title = input.nextLine();

            //Validate it
            while (title.length() == 0) {
                System.out.println(Utilities.WARNING_MESSAGE + "Please enter at least one character:");
                System.out.print(Utilities.INPUT_MESSAGE);
                title = input.nextLine();
            }
            //Replace the title
            inv.replaceTitle(oldID, title);
        } else System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect ID, nothing was changed.");

    }
    /**
     * Edits the loan cost of Videos.
     */
    public static void editLoanCost() {
        //We can only edit loan cost on Videos.
        char[] type = {'v'};
        String oldID = getExistingID("Video", type);

        //Validate ID
        if (oldID != null) {
            //Run input validation
            String[] choiceOptions = {"4", "6"};
            int newLoan = Integer.parseInt(receiveStringInput("Enter new Loan Fee:", choiceOptions, true, 1));
            try {
                //Replace the loan fee
                inv.replaceLoan(oldID, newLoan);
            } catch (IncorrectDetailsException e) {
                System.out.println(Utilities.ERROR_MESSAGE + " Adding failed due to " + e + " with error message: \n" + e.getMessage());
            }
        } else System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect ID, nothing was changed.");
    }

}
