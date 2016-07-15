/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui;

import lms.holding.Holding;
import lms.ui.Operations.GenericOperations;
import lms.ui.Operations.HoldingOperations;
import lms.ui.Operations.InventoryOperations;
import lms.ui.Operations.MemberOperations;
import lms.util.AdminVerify;
import lms.util.Utilities;

import static lms.ui.UI.*;
import static lms.ui.Shared.*;
import static lms.ui.Input.*;
/**
 * Created by kin0025 on 16/07/2016.
 */
public class Admin {
    /**
     * Used to limit access to admin menu
     */
    public static void adminMenuVerify() {
        AdminVerify verify = new AdminVerify();
        newPage("Admin Verification");
        System.out.println("Please enter your admin password (Password1):");
        System.out.print(Utilities.INPUT_MESSAGE);
        //Check the password
        String password = input.nextLine();
        //Run the admin menu
        if (verify.authenticated(password)) {
            adminMenu();
        }
        //Wait 5 seconds so they can't spam passwords.
        else {
            try {
                System.out.println(Utilities.INFORMATION_MESSAGE + "Incorrect Password. Returning to main menu");
                System.out.print("In 5 seconds");
                for (int i = 5; i != 0; i--) {
                    Thread.sleep(1000);
                    System.out.print("\r");
                    System.out.print("In " + i + " seconds");


                }
            } catch (InterruptedException ignored) {
            }
        }

    }
    /**
     * Secondary menu for admin functions used to choose what admin methods to run.
     */
    public static void adminMenu() {
        while (true) {
            newPage("Admin");
//The menu for admin stuff
            System.out.println(" 1. Activate");
            System.out.println(" 2. Deactivate");
            System.out.println(" 3. Edit Holding Details");
            System.out.println(" 4. Reset Member Credit");
            System.out.println(" 5. Edit Member Details");
            System.out.println(" 6. Return holding ignoring fees");
            System.out.println(" 7. Undelete holdings");
            System.out.println(" 8. Back to Main Menu");
            printCharTimes('=', 50, true);
            String[] adminOptions = {"1", "2", "3", "4", "5", "6", "7", "8"};
            int options = Integer.parseInt(receiveStringInput("Enter an option:", adminOptions, false, 1)); //http://stackoverflow.com/questions/5585779/converting-string-to-int-in-java
            switch (options) {
                case 1:
                    GenericOperations.activate();
                    break;
                case 2:
                    GenericOperations.deactivate();
                    break;
                case 3:
                    Menu.editHoldingMenu();
                    break;
                case 4:
                    MemberOperations.resetMemberCredit();
                    break;
                case 5:
                    Menu.editMemberMenu();
                    break;
                case 6:
                    InventoryOperations.returnHoldingNoFee();
                    break;
                case 7:
                    InventoryOperations.undeleteHolding();
                    break;
                case 8:
                    return;
                default:
                    adminMenu();
            }
        }
    }


}
