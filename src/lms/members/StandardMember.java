/*
*@author - Alex Kinross-Smith
*/
package lms.members;

/**
 * Created by akinr on 11/04/2016.
 */
public class StandardMember extends Member{

    public StandardMember(String standardMemberId, String standardMemberName){
        setID(standardMemberId);
        setName(standardMemberName);
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
