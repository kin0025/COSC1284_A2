package lms.holding;/*
*@author - Alex Kinross-Smith
*/

import lms.util.DateTime;

/**
 * Created by akinr on 11/04/2016.
 */
public class Video extends Holding {
    public Video(String holdingId, String title, int loanFee) {
        setTitle(title);
        setID(holdingId);
        setLoanCost(loanFee);
        setMaxLoanPeriod(7);
        if(!checkValidity().equalsIgnoreCase("valid")){
            deactivate();
            System.out.println(checkValidity());
            System.out.println("Item has been deactivated due to invalid details.");
        }
        setUnavailable(false);
        activate();
    }
    public Video(String ID, String title, int loanCost, int maxLoanPeriod, DateTime borrowDate, boolean active, boolean unavailable){
        super(ID, title,loanCost, maxLoanPeriod,borrowDate, active, unavailable);
    }
    public boolean setID(String itemID, char itemType) {
        if(itemType == 'v' && itemID.length() == 6){
            super.setID(itemType + "" + itemID);
            return (true);
        }else{
            if(itemID.length() == 6){}
            return(false);
        }
    }
@Override
    public boolean setID(String ID) {
        if(ID.charAt(0) == 'v' && ID.length() == 7){
            super.setID(ID);
            return(true);
        }else{
            return (false);
        }

    }
    @Override
    public int calculateLateFee(DateTime dateReturned) {
        int daysOut = DateTime.diffDays(dateReturned,getBorrowDate());
        int daysDiff = daysOut - getMaxLoanPeriod();
        if(daysDiff < 0){
            daysDiff = 0;
        }
        return ((int)(daysDiff * getDefaultLoanFee() * 0.5));
    }
    @Override
    public void setLoanCost(int loanCost) {
        if(loanCost == 4 || loanCost == 6) {
            super.setLoanCost(loanCost);
        }
        else {
            System.out.println("Invalid loan cost. Holding has been deactivated. ID:" + getID());
            deactivate();
        }
    }
    @Override
    public String checkValidity(){
        String result = super.checkValidity();
        if (getDefaultLoanFee() == 6 || getDefaultLoanFee() ==4) {
            return (result);
        }else return ("Invalid Fee");
    }
}

