/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.exceptions;

/**
 * Created by Emily on 12/05/2016 as part of s3603437_A2
 */
public class OnLoanException extends RuntimeException {
    public OnLoanException() {
    }

    public OnLoanException(String message) {
        super(message);
    }
}
