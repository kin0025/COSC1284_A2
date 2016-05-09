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

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class StandardMember extends Member{

    public StandardMember(String standardMemberId, String standardMemberName){
        super(standardMemberId,standardMemberName,30);

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

