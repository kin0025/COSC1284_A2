/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.members;

import lms.exceptions.IncorrectDetailsException;
import lms.exceptions.ItemInactiveException;
import lms.exceptions.OnLoanException;
import lms.exceptions.TimeTravelException;
import lms.holding.Holding;
import lms.util.DateTime;
import lms.util.Utilities;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class PremiumMember extends Member {

    /**
     * Instantiates a new Premium member.
     *
     * @param premiumMemberId   the premium member id
     * @param premiumMemberName the premium member name
     */
    public PremiumMember(String premiumMemberId, String premiumMemberName) {
        super(premiumMemberId, premiumMemberName, 45);

    }

    /**
     * Instantiates a new Premium member.
     *
     * @param ID        the id
     * @param name      the name
     * @param maxCredit the max credit
     * @param balance   the balance
     * @param borrowed  the borrowed
     * @param active    the active
     * @param uniqueID  the unique id
     */
    public PremiumMember(String ID, String name, int maxCredit, double balance, ArrayList<Holding> borrowed, boolean active, String uniqueID) {
        super(ID, name, maxCredit, balance, borrowed, active, uniqueID);
    }

    /**
     * Sets and ID after validation.
     * @param ID The ID to be set
     * @return Whether the ID was valid and set.
     */
    public boolean setID(String ID) {
        if (Utilities.isIDValid('p',ID)) {
            super.setID(ID);
            return (true);
        } else {
            return (false);
        }

    }

    /**Returns a holding. Validates the success of holding return, however does not require the user to have sufficient balance to repay late fees.
     * @param holding the holding to be returned.
     * @param returnDate the date the holding was returned.
     * @return Whether the holding was successfully returned.
     */
    @Override
    public boolean returnHolding(Holding holding, DateTime returnDate) throws TimeTravelException,OnLoanException,ItemInactiveException{
        //Check that the holding is actually borrowed by the user.
        if (borrowed.contains(holding)) {

            //Check that the holding can be returned.
            if (holding.returnHolding(returnDate)) {
                //Deduct the fee, remove the holding from the member.
                int lateFee = holding.calculateLateFee(returnDate);
                updateRemainingCredit(lateFee);
                borrowed.remove(holding);

                //Print some stuff to the console for the user to see.
                if (balance < 0) {
                    System.out.println(Utilities.INFORMATION_MESSAGE + " Balance is less than 0 dollars, you will be unable to borrow any books until it is reloaded.");
                }
                if (lateFee > 0) {
                    System.out.println(Utilities.INFORMATION_MESSAGE + " Holding was returned with late fee of:" + lateFee + ". Remaining balance is " + getBalance());
                } else {
                    System.out.println(Utilities.INFORMATION_MESSAGE + " Holding was returned with no late fee. Thank you for returning your books in a timely fashion. Your remaining balance is:" + lateFee);
                }
                return true;
            //If the holding cannot be returned return false. It will likely throw an exception anyway.
            } else {
                return false;
            }
            //Throw an exception if it was not.
        } else {
            throw new OnLoanException("User has not borrowed holding");
        }

    }


}
