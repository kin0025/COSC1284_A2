package lms;/*
*@author - Alex Kinross-Smith
*/

import lms.holding.*;
import lms.members.*;
import lms.util.*;

import java.util.Objects;


/**
 * Created by akinr on 11/04/2016.
 */
public class Inventory {
    //Start Screen
    private Holding[] holdings = new Holding[15];
    private Member[] members = new Member[15];
    private int numberOfHoldings = 0;
    private int numberOfMembers = 0;


    /**
     * Check if the details needed for an id are correct. Returns the result in string form (Valid, Already Taken,Invalid Type, ID contains Characters)
     **/
    int getNumberOfHoldings() {
        return (numberOfHoldings);
    }

    int getNumberOfMembers() {
        return numberOfMembers;
    }

    String checkID(String ID, char itemType) {
        if (ID.length() != 6) {
            return ("Wrong Number of Digits");
        }
        boolean validID = true;
        int i = 0;
        while (ID.length() > i && validID) {
            //So we need to cast the id to a char to an int. This returns an ascii value, as I found out with the loanFee.
            if (!((int) ID.charAt(i) >= 48 && (int) ID.charAt(i) <= 58)) { //Ascii codes for numbers are between 48 and 58 (including 0). If each character is between these values, they are numbers.
                validID = false;
            }
            i++;
        }
        if (!validID) return ("ID contains characters");
        //Checking for duplicate ID
        switch (itemType) {
            case 'b':
            case 'v':
                for (i = 0; i < holdings.length; i++) {
                    if (holdings[i] != null) {
                        if (holdings[i].getID().equals(itemType + ID)) {
                            return ("Already Taken");
                        }
                    }
                }
                break;
            case 's':

            case 'p':
                for (i = 0; i < members.length; i++) {
                    if (members[i] != null) {
                        if (members[i].getID().equals(itemType + ID)) {
                            return ("Already Taken");
                        }
                    }
                }
                break;
        }


        return ("Valid");

    }

    /**
     * Finds the array index of the firsy null value
     **/
    private int firstNullArray(char type) {
        if (type == 'v' || type == 'b') {
            for (int i = 0; i < holdings.length; i++) {
                if (holdings[i] == null) {
                    return i;
                }
            }
        } else if (type == 'p' || type == 's') {
            for (int i = 0; i < members.length; i++) {
                if (members[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Provides a string form printout of inventory values in the form Holdings:number/max Members: Number/max
     **/
    public String infoPrintout() {
        return ("Holdings:" + numberOfHoldings + "/" + holdings.length + " | " + "Members:" + numberOfMembers + "/" + members.length);
    }

    /**
     * Creates an ID number that is not already taken for a specific item type - holding or ID
     **/
    public String generateValidID(char itemType) {
        String randomID;
        do {
            randomID = Util.randomID();
        } while (!checkID(randomID, itemType).equalsIgnoreCase("valid"));
        return randomID;
    }

    public void recalculateStatistics() {
        numberOfHoldings = 0;
        numberOfMembers = 0;
        for (int i = 0; i < holdings.length; i++) {
            if (holdings[i] != null) {
                numberOfHoldings++;
            }
        }
        for (int i = 0; i < members.length; i++) {
            if (members[i] != null) {
                numberOfMembers++;
            }
        }
    }

    private int searchArrays(String ID) {
        char itemType = ID.toLowerCase().charAt(0);
        if (itemType == 'v' || itemType == 'b') {
            for (int i = 0; i < holdings.length; i++) {
                if (holdings[i] != null) {
                    if (holdings[i].getID().equals(ID)) {
                        return (i);
                    }
                }
            }
            System.out.println("Unable to find holding with that ID");
            return (-1);
        } else if (itemType == 'p' || itemType == 's') {
            for (int i = 0; i < members.length; i++) {
                if (members[i] != null) {
                    if (members[i].getID().equals(ID)) {
                        return (i);
                    }
                }
            }
        } else {
            System.out.println("Wrong ID format");
            return -1;
        }
        System.out.println("Unable to find member with that ID");
        return -1;
    }



    /* Methods that add items to inventory */

    /**
     * Adds a holding based upon provided information. Returns a booleans value of success
     **/
    public boolean addHolding(String ID, char itemType, String title, int loanFee) {
        if (numberOfHoldings < holdings.length) {
            if (checkID(ID, itemType).equals("Valid")) {
                String holdingID = itemType + ID;
                int itemNumber = firstNullArray(itemType);
                if (itemType == 'b') {
                    holdings[itemNumber] = new Book(holdingID, title);
                    recalculateStatistics();
                    return true;
                } else if (itemType == 'v') {
                    holdings[itemNumber] = new Video(holdingID, title, loanFee);
                    numberOfHoldings++;
                    return true;
                }
            } else System.out.println("Invalid ID");
        } else
            System.out.println("All holding spots are taken. Please pay for a larger subscription to support more holdings, or remove exiting holdings");
        return false;
    }

    public boolean addMember(String ID, char itemType, String name) {
        if (numberOfMembers < members.length) {
            if (checkID(ID, itemType).equals("Valid")) {
                String memberID = itemType + ID;
                int itemNumber = firstNullArray(itemType);
                if (itemType == 's') {
                    members[itemNumber] = new StandardMember(memberID, name);
                    recalculateStatistics();
                    return true;
                } else if (itemType == 'p') {
                    members[itemNumber] = new PremiumMember(memberID, name);
                    numberOfMembers++;
                    return true;
                }
            }
            System.out.println("Invalid ID");
        }
        System.out.println("All member spots are taken. Please pay for a larger subscription to support more members, or remove exiting members");
        return false;
    }

    /* Remove */
    public boolean removeHolding(String ID) {
        int holdingPos = searchArrays(ID);
        if (holdingPos >= 0) {
            holdings[holdingPos] = null;
            recalculateStatistics();
        } else {
            System.out.println("Nothing was removed");
            return false;
        }
        System.out.println("Holding " + ID + " was removed. There are " + numberOfHoldings + " holdings still in system");
        return true;
    }

    public boolean removeMember(String ID) {
        int memberPos = searchArrays(ID);
        if (memberPos >= 0) {
            members[memberPos] = null;
            recalculateStatistics();
        } else {
            System.out.println("Nothing was removed");
            return false;
        }
        System.out.println("Member " + ID + " was removed. There are " + numberOfMembers + " members still in system");
        return true;
    }

    public boolean borrowHolding(String holdingID, String memberID) {
        int holdingPos = searchArrays(holdingID);
        int memberPos = searchArrays(memberID);
        if (holdingPos >= 0 && memberPos >= 0) {
            holdings[holdingPos].borrowHolding();
            members[memberPos].borrowHolding(holdings[holdingPos]);
            return true;
        } else return false;
    }

    public boolean returnHolding(String holdingID, String memberID) {
        int holdingPos = searchArrays(holdingID);
        int memberPos = searchArrays(memberID);
        DateTime current = new DateTime();
        if (holdingPos >= 0 && memberPos >= 0) {
            holdings[holdingPos].returnHolding(current);
            members[memberPos].returnHolding(holdings[holdingPos], current);
            return true;
        } else return false;

    }

    public void printAllHoldings() {
        for (int i = 0; i < holdings.length; i++) {
            if (holdings[i] != null) {
                holdings[i].print();
                gui.printCharTimes('=', 150, true);
            }
        }
    }

    public void printAllMembers() {
        for (int i = 0; i < members.length; i++) {
            if (members[i] != null) {
                members[i].print();
                gui.printCharTimes('=', 150, true);
            }
        }
    }

    public boolean printHolding(String ID) {
        if (ID.length() != 7) {
            System.out.println("Incorrect ID");
            return false;
        }
        int holdingID = searchArrays(ID);
        if (holdingID < 0) {
            System.out.println("No Holding Found");
            return false;
        } else holdings[holdingID].print();
        return true;
    }

    public boolean printMember(String ID) {
        if (ID.length() != 7) {
            System.out.println("Incorrect ID");
            return false;
        }
        int memberID = searchArrays(ID);
        if (memberID < 0) {
            System.out.println("No Member Found");
            return false;
        } else members[memberID].print();
        return true;
    }

    public String getMemberName(String ID) {
        boolean result;
        if (ID.charAt(0) == 's' || ID.charAt(0) == 'p') {
            result = idExists(ID);
        } else {
            System.out.println("ID not a member. Please try again");
            return null;
        }
        if (!result) {
            return null;
        } else {
            return (members[searchArrays(ID)].getFullName());
        }
    }

    public boolean idExists(String ID) {
        if (ID.length() != 7) {
            System.out.println("Incorrect ID");
            return false;
        }
        if (searchArrays(ID) < 0) {
            System.out.println("Not Found");
            return false;
        } else
            return true;
    }

    public void save() {

    }


/*

    public void importFile() {
        */
/* Get first char of holding ID *//*

        String ID;
        String title;
        String author;
        int loanCost;
        int maxLoanPeriod;
        DateTime borrowDate;
        boolean active;
        boolean unavailable;
        if (ID.toLowerCase().charAt(0) == 'b') {
            holdings[currentHoldingPos] = new Book(ID,title,author,loanCost,maxLoanPeriod,borrowDate,active,unavailable);
        } else if (ID.toLowerCase().charAt(0) == 'v') {
            holdings[currentHoldingPos] = new Video(ID, title,loanCost, maxLoanPeriod,borrowDate, active, unavailable);
        } else System.out.println("IMPORT FAILED ON ID DUE TO INCORRECT ID TYPE:" + ID);
    }

*/
}

