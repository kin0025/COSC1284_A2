/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.exceptions;

/**
 * Created by Emily on 23/05/2016 as part of s3603437_A2
 */
public class IncorrectDetailsException extends RuntimeException {
    public IncorrectDetailsException() {
    }

    public IncorrectDetailsException(String message) {
        super(message);
    }
}
