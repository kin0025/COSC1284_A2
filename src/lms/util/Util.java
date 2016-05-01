/*
*@author - Alex Kinross-Smith
*/
package lms.util;

import java.util.Random;

/**
 * Created by akinr on 20/04/2016.
 */
public class Util {
    public static String randomID() {
        int[] numbers = new int[6];
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            numbers[i] = random.nextInt(10);
        }
        return(numbers[0] + "" + numbers[1] + numbers [2] + numbers [3] + numbers[4] + numbers [5]);
    }
}
