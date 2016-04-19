package lms.holding;/*
*@author - Alex Kinross-Smith
*/

/**
 * Created by akinr on 11/04/2016.
 */
public class Video extends Holding {
    public Video(String holdingId, String title, int loanFee) {

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

    public boolean setID(String ID) {
        if(ID.charAt(0) == 'v' && ID.length() == 7){
            super.setID(ID);
            return(true);
        }else{
            return (false);
        }

    }
}

