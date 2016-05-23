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
@SuppressWarnings("FieldCanBeLocal")
public class AdminVerify {
    //Wow! Such magic
    private final String masterPassword = "d41d8cd98f00b204e9800998ecf8427e";

    public boolean authenticated(String passcode) {
        String hash = Utilities.hashString(passcode);
        System.out.println(hash);
        System.out.println(masterPassword);
        return masterPassword.equals(hash);
    }
}
