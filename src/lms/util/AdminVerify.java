/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.util;

/**
 * Created by akinr on 28/04/2016 as part of s3603437_A2
 */
public class AdminVerify {
    private final String masterPassword = "Password1";

    public boolean authenticated(String passcode) {
        return passcode.equals(masterPassword);
    }
}
