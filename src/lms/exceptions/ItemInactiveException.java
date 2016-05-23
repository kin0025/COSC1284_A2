/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.exceptions;

import lms.util.Utilities;

/**
 * Created by Emily on 12/05/2016 as part of s3603437_A2
 */
public class ItemInactiveException extends Exception{
    public ItemInactiveException() {
    }

    public ItemInactiveException(String message) {
        super(Utilities.ERROR_MESSAGE + message);
    }
}
