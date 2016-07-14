/*
*@author - Alex Kinross-Smith
*/
package lms;

import lms.exceptions.IncorrectDetailsException;

/**
 * Created by akinr on 4/05/2016 as part of s3603437_A2
 */
public interface SystemOperations {
    boolean activate() throws IncorrectDetailsException;

    boolean deactivate();

    String getID();

    void setID(String newID);
}
