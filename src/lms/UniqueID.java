/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms;

/**
 * Created by akinr on 19/05/2016 as part of s3603437_A2
 */
public interface UniqueID {
    String getUUID();

    void setUUID(String uniqueID);

    boolean setUUID();
}
