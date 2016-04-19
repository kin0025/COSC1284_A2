package lms.holding;/*
*@author - Alex Kinross-Smith
*/

/**
 * Created by akinr on 11/04/2016.
 */
public class Book extends Holding {
    private String holdingID;
    private String title;

    protected void Book() {
        super.setLoanCost(10);
        super.setLoanDailyCost(2);
    }

    public boolean setID(String itemID, char itemType) {
        if(itemType == 'b') {
            super.setID(itemType + "" + itemID);
            return (true);
        }else{
            return(false);
        }
    }

    public boolean setID(String ID) {
        if(ID.charAt(0) == 'b'){
            super.setID(ID);
            return(true);
        }else{
            return (false);
        }

    }
}
