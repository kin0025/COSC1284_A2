/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/

import lms.ui.UI;

/**
 * Created by akinr on 20/04/2016 as part of s3603437_A2
 */
class LMS {


    public static void main(String[] args) {
        UI state = new UI();

        try {
            while (true) {
                lms.ui.Menu.mainMenu();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Saving program state to backup directory due to thrown exception.");
            lms.ui.Menu.save("backup");
        }
    }

}
