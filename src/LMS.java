/*
*@author - Alex Kinross-Smith
*/

import lms.GUI;
import lms.holding.*;
import lms.members.PremiumMember;
import lms.members.StandardMember;

/**
 * Created by akinr on 11/04/2016.
 */
public class LMS {
    //Start Screen
    private Book[] books = new Book[128];
    private Video[] videos = new Video[128];
    private StandardMember[] standardMembers = new StandardMember[128];
    private PremiumMember[] premiumMembers = new PremiumMember[128];
    private int currentBookPos = 0;
    private int currentVideoPos = 0;
    private int currentSMemberPos = 0;
    private int currentPMemberPos = 0;

    public static void main(String[] args) {
        GUI ui = new GUI();


        ui.logoBoot();
        ui.mainMenu();
    }

    public void setID(String ID, char itemType) {
        //Set the book id
        if (itemType == 'v' || itemType == 'b' || itemType == 's' || itemType == 'p') {
            if (itemType == 'b' && books[currentBookPos].setID(ID, itemType)) {
                System.out.println("ID Set successfully");
            } else if (itemType == 'v' && videos[currentVideoPos].setID(ID, itemType)) {
                System.out.println("ID Set successfully");
            } else if (itemType == 's' && standardMembers[currentSMemberPos].setID(ID, itemType)) {
                System.out.println("ID Set successfully");
            } else if (itemType == 'p' && premiumMembers[currentPMemberPos].

                    setID(ID, itemType)

                    )

            {
                System.out.println("ID Set successfully");
            } else

            {
                System.out.println("The number you specified was either already taken or not 6 characters long"); //// TODO: 20/04/2016 Add checking for already taken ids.
            }


            else System.out.println("Invalid item type entered");


        }
    }

    public void addMember(String ID, char itemType) {
        if (itemType == 'v' || itemType == 'b') {
            setID(ID, itemType);
        }
    }

    // TODO: 19/04/2016 All ui info and user input through up class. Processing (Search, formatting and other validation) is in the LMS Class methods
}
