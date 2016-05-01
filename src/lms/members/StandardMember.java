/*
*@author - Alex Kinross-Smith
*/
package lms.members;

/**
 * Created by akinr on 11/04/2016.
 */
public class StandardMember extends Member{

    public StandardMember(String standardMemberId, String standardMemberName){}
    public boolean setID(String itemID, char itemType) {
        if(itemType == 's' && itemID.length() == 6){
            super.setID(itemType + "" + itemID);
            return (true);
        }else{
            if(itemID.length() == 6){}
            return(false);
        }
    }

    public boolean setID(String ID) {
        if(ID.charAt(0) == 's' && ID.length() == 7){
            super.setID(ID);
            return(true);
        }else{
            return (false);
        }

    }
}
