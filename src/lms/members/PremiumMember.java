/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.members;

import lms.holding.Holding;
import lms.util.DateTime;
import lms.util.Utilities;

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class PremiumMember extends Member {

    public PremiumMember(String premiumMemberId, String premiumMemberName) {
        super(premiumMemberId, premiumMemberName, 45);

    }

    public boolean setID(String ID) {
        if (ID.charAt(0) == 'p' && ID.length() == 7) {
            super.setID(ID);
            return (true);
        } else {
            return (false);
        }

    }

    /**
     * @param holding
     * @param returnDate
     * @return
     */
    @Override
    public boolean returnHolding(Holding holding, DateTime returnDate) {
        int searchedPos = findHolding(holding);
        if (searchedPos <= 0) {
            DateTime currentDate = new DateTime();
            int lateFee = borrowed[searchedPos].calculateLateFee(currentDate);
            balance -= lateFee;
            if (borrowed[searchedPos].returnHolding(currentDate)) {
                borrowed[searchedPos] = null;
                if (balance < 0) {
                    System.out.println(Utilities.INFORMATION_MESSAGE + " Balance is less than 0 dollars, you will be unable to borrow any books until it is reloaded.");
                } else if (lateFee > 0) {
                    System.out.println(Utilities.INFORMATION_MESSAGE + " Holding was returned with late fee of:" + lateFee + ". Remaining balance is " + getBalance());
                } else {
                    System.out.println(Utilities.INFORMATION_MESSAGE + " Holding was returned with no late fee. Thank you for returning your books in a timely fashion. Your remaining balance is:" + lateFee);
                }
                return true;
            } else {
                System.out.println(Utilities.ERROR_MESSAGE + " Holding could not be returned");
                return false;
            }

        } else {
            System.out.println(Utilities.INFORMATION_MESSAGE + " User has not borrowed holding");
            return false;
        }

    }


}
