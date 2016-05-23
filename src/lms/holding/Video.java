/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.holding;/*
*@author - Alex Kinross-Smith
*/

import lms.exceptions.IncorrectDetailsException;
import lms.util.DateTime;
import lms.util.Utilities;

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class Video extends Holding {
    private static final double LATE_FEE_MULTIPLIER = 0.5;
    private static final int LOAN_PERIOD = 7;
    private static final int[] LOAN_FEES = {4, 6};

    public Video(String holdingId, String title, int loanFee) {
        super(holdingId, title);
        setLoanFee(loanFee);
        setMaxLoanPeriod(LOAN_PERIOD);

        //Check that all the attributes are correct, deactivate if incorrect and activate if correct.
        activate();
    }

    /**
     * Used to reconstruct all instance variables for loading.
     *
     * @param ID            The ID of the item
     * @param title         The title of the item
     * @param loanFee       the loan fee of the item
     * @param maxLoanPeriod the loan period of the item
     * @param borrowDate    the date that the item was borrowed. If it is null the holding is not borrowed.
     * @param active        the active status of the item
     * @param uniqueID      the unique identifier of the item. As ID can be changed, this is used to identify the holding when saving.
     */
    public Video(String ID, String title, int loanFee, int maxLoanPeriod, DateTime borrowDate, boolean active, String uniqueID) {
        super(ID, title, loanFee, maxLoanPeriod, borrowDate, active, uniqueID);
    }

    /**
     * Sets the ID of the holding after validating it.
     *
     * @param ID The string form ID to be set for the holding.
     * @return returns the success state of setting the ID. If false, ID was not set.
     */
    @Override
    public boolean setID(String ID) {
        //Check if the ID was valid
        if (Utilities.isIDValid('v', ID)) {
            super.setID(ID);
            return true;
        }
        //If it isn't return false.
        else {
            return false;
        }
    }

    /**
     * Calculates the late fee of the holding and returns it.
     *
     * @param dateReturned The date the holding was returned.
     * @return The late fee in dollars. Will always be a whole number.
     */
    @Override
    public int calculateLateFee(DateTime dateReturned) {

        //Calculate how long it has been since the holding was borrowed.
        int daysOut = DateTime.diffDays(dateReturned, getBorrowDate());

        //Calculate how far beyond the max loan date the holding has been out.
        int daysDiff = daysOut - getMaxLoanPeriod();

        if (daysDiff <= 0) {
            //If they haven't had the holding for longer than max loan period, there is no late fee.
            return 0;
        }
        //Return the late fee.
        return ((int) (daysDiff * getDefaultLoanFee() * LATE_FEE_MULTIPLIER));
    }

    /**
     * Sets the loanFee. If it fails to validate it will deactivate the holding.
     *
     * @param loanFee The loan fee to be set.
     */
    @Override
    public void setLoanFee(int loanFee) throws IncorrectDetailsException{
        //Validates the loan fee.
        if (isValidLoanFee(loanFee)) {
            super.setLoanFee(loanFee);
        }
        //If invalid disable the item and print it to the user. Still set the fee.
        else {
            super.setLoanFee(loanFee);
            deactivate();
            throw new IncorrectDetailsException("Invalid loan cost. Holding has been deactivated. ID:" + getID());
        }
    }

    /**
     * Checks that the loan fee is in the LOAN_FEES array.
     *
     * @param loanFee the loan fee to be compared to values in the array.
     * @return Returns true if loan fee is valid, false if not.
     */
    private boolean isValidLoanFee(int loanFee) {
        //Run a foreach to validate the fee.
        for (int i : LOAN_FEES) {
            if (loanFee == i) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the validity of title, ID and loan fee.
     *
     * @return A string representation of the result.
     */
    @Override
    public String checkValidity() {
        //Check the ID and title using past logic.
        String result = super.checkValidity('v');

        //Validate the loan fee. If it is incorrect return invalid fee, otherwise use the result from the super check.
        if (isValidLoanFee(getDefaultLoanFee())) {
            return (result);
        } else return ("Invalid Fee");
    }
}

