/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/

import lms.UI;
import lms.util.Utilities;

/**
 * Created by akinr on 20/04/2016 as part of s3603437_A2
 */
public class LMS {
    public static void main(String[] args) {
        UI state = new UI();
        state.logoBoot();
       // try {
            while (true) {
                state.mainMenu();
       }
       /* } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }finally {
            System.out.println("Saving program state to backup directory due to thrown exception.");
            state.save("backup");
        }*/
    }
// TODO: 22/05/2016 Custom and Java exceptions behave as expected and are handled to prevent run time errors that cause the program to crash.
    // TODO: 22/05/2016 Your code commenting was generally appropriate and improved the readability of your code.
// TODO: 22/05/2016 Your gcode formatting was generally neat, consistent and followed an accepted coding style convention.
// TODO: 22/05/2016 Your code formatting is generally poor - indentation needs to be consistent, code should be spaced out into logical segments and expressions should also be spaced out to make your code easier to read.
// TODO: 23/05/2016 Reduce prompt frequency

}
