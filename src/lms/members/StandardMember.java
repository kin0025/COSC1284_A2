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
import lms.util.Utilities;

import java.util.ArrayList;

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class StandardMember extends Member{

    /**
     * Instantiates a new Standard member.
     *
     * @param standardMemberId   the standard member id
     * @param standardMemberName the standard member name
     */
    public StandardMember(String standardMemberId, String standardMemberName){
        super(standardMemberId,standardMemberName,30);

    }

    /**
     * Instantiates a new Standard member with all properties supplied. No validation carried out.
     *
     * @param ID        the id
     * @param name      the name
     * @param maxCredit the max credit
     * @param balance   the balance
     * @param borrowed  the borrowed
     * @param active    the active
     * @param uniqueID  the unique id
     */
    public StandardMember(String ID, String name, int maxCredit, double balance, ArrayList<Holding> borrowed, boolean active, String uniqueID) {
        super(ID, name, maxCredit, balance, borrowed, active, uniqueID);
    }

    /**
     * Sets ID after validating that it was correct.
     * @param ID The ID to be set
     * @return Whether the ID was set and is valid.
     */
    public boolean setID(String ID) {
        if(Utilities.isIDValid('s',ID)){
            super.setID(ID);
            return(true);
        }else{
            return (false);
        }

    }

}

