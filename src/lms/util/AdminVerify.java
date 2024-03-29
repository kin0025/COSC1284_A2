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
    //An MD5 hash of the password.
    private final String masterPassword = "d41d8cd98f00b204e9800998ecf8427e";

    /**
     * Check that the password hashes to the same value as the master password and return the success of the operation.
     *
     * @param passcode The passcode to be examined.
     * @return Whether the password matched.
     */
    public boolean authenticated(String passcode) {
        //Hash the input passcode
        String hash = Utilities.hashString(passcode);

        return masterPassword.equals(hash);
    }
}
