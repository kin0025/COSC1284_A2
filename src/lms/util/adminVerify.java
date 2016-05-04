/*
*@author - Alex Kinross-Smith
*/
package lms.util;

/**
 * Created by akinr on 28/04/2016.
 */
public class AdminVerify {
    private boolean isVerified;
    private String passwordHashes[] = new String[8];
    private String masterPassword = "Password1";

    public boolean authenticated(String passcode) {
        if (passcode.equals(masterPassword)) {
            return true;
        }
        return false;
    }
}
