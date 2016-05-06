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
