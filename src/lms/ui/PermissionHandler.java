/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui;

import lms.members.Member;

/**
 * Created by kin0025 on 16/07/2016.
 */
public class PermissionHandler {
    public static boolean checkOperation(int requiredPermission, Member member){
        if(member.getPermissionLevel() >= requiredPermission){
            return true;
        }
        return false;
    }



}
