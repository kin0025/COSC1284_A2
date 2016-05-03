/*
*@author - Alex Kinross-Smith
*/
package lms.members;

/**
 * Created by akinr on 11/04/2016.
 */
public class PremiumMember extends Member {

    public PremiumMember(String premimumMemberId, String premiumMemberName) {
setID(premimumMemberId);
        setName(premiumMemberName);
    }
    public boolean setID(String ID) {
        if (ID.charAt(0) == 'p' && ID.length() == 7) {
            super.setID(ID);
            return (true);
        } else {
            return (false);
        }

    }
}
