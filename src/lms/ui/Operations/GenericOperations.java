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
import lms.ui.Input;
import lms.util.Utilities;
import static lms.ui.UI.*;
import static lms.ui.Input.*;
import static lms.ui.Shared.*;
import static lms.ui.Print.*;
/**
 * Created by kin0025 on 15/07/2016.
 */
public class GenericOperations {
    /**
     * Activates the ID input by the user. Can be a member or holding.
     */
    public static void activate() {
        //Get the ID
        String ID = Input.getExistingID("Item", new char[]{'b', 'v', 's', 'p'});
        //If it was entered correctly keep going
        if (ID != null) {
            try {
                //Run the activation and report result
                boolean result = inv.activate(ID);
                if (!result) {
                    System.out.println(Utilities.WARNING_MESSAGE + " Activation failed");
                } else {
                    System.out.println(Utilities.INFORMATION_MESSAGE + " Activation success");
                }
            } catch (IncorrectDetailsException e) {
                System.out.println(Utilities.ERROR_MESSAGE + " Adding failed due to " + e + " with error message: \n" + e.getMessage());
            }
        }
    }

    /**
     * Deactivates the ID input by the user. Can be a member or holding.
     */
    public static void deactivate() {
        //Get the ID
        String ID = getExistingID("Item", new char[]{'b', 'v', 's', 'p'});
        //If it was entered correctly keep going
        if (ID != null) {
            //Run the activation and report result
            boolean result = inv.deactivate(ID);
            if (!result) {
                System.out.println(Utilities.WARNING_MESSAGE + "Deactivation failed");
            } else {
                System.out.println(Utilities.INFORMATION_MESSAGE + "Deactivation success");
            }
        }
    }

}
