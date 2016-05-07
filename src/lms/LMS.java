/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms;

/**
 * Created by akinr on 20/04/2016.
 */
public class LMS {
    public static void main(String[] args){
        GUI state = new GUI();
        state.logoBoot();
        while(true){
            state.mainMenu();
        }
    }
}
