/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.members;

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class PremiumMember extends Member {

    public PremiumMember(String premiumMemberId, String premiumMemberName) {
super(premiumMemberId,premiumMemberName,45);

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
