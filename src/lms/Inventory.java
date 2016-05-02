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

    //Check if an ID is correct - both type and ID. Return the result in string form (Valid, Already Taken,Invalid Type)
    public String checkID(String ID, char itemType) {
        if (ID.length() != 6) {
            return ("Wrong Number of Digits");
        }
        //Checking for duplicate ID
        for (int i = 0; i < holdings.length; i++) {
            switch (itemType) {
                case 'b':
                case 'v':
                    if (holdings[i] != null) {
                        if (holdings[i].getID().substring(1, 6).equals(ID)) {
                            return ("Already Taken");
                        }
                    }
                    break;
                case 's':

                case 'p':
                    if (members[i] != null) {
                        if (members[i].getID().substring(1, 6).equals(ID)) {
                            return ("Already Taken");
                        }
                    }
                    break;
            }

        }
        return ("Valid");

    }

    public String infoPrintout() {
        return ("Holdings:" + numberOfHoldings + "/" + holdings.length + " " + "Members:" + numberOfMembers + "/" + members.length);
    }

    //Create an ID that isn't already taken for an item type
    public String generateValidID(char itemType) {
        String randomID;
        do {
            randomID = Util.randomID();
        } while (!checkID(randomID, itemType).equalsIgnoreCase("valid"));
        return randomID;
    }

    /* Add Methods */
    public boolean addHolding(String ID, char itemType, String title, int loanFee) {
        if (numberOfHoldings < holdings.length) {
            checkID(ID, itemType);
            String holdingID = itemType + ID;
            int itemNumber = firstNullArray(itemType);
            if (itemType == 'b') {
                holdings[itemNumber] = new Book(holdingID, title);
                numberOfHoldings++;
                return true;
            } else if (itemType == 'v') {
                holdings[itemNumber] = new Video(holdingID, title, loanFee);
                numberOfHoldings++;
                return true;
            }
        }
        System.out.println("All holding spots are taken. Please pay for a larger subscription to support more holdings, or remove exiting holdings");
        return false;

    }

    public boolean addMember(String ID, char itemType, String name) {
        if (numberOfMembers < members.length) {
            checkID(ID, itemType);
            String memberID = itemType + ID;
            int itemNumber = firstNullArray(itemType);
            if (itemType == 's') {
                members[itemNumber] = new StandardMember(memberID, name);
                numberOfMembers++;
                return true;
            } else if (itemType == 'p') {
                members[itemNumber] = new PremiumMember(memberID, name);
                numberOfMembers++;
                return true;
            }
        }
        System.out.println("All member spots are taken. Please pay for a larger subscription to support more members, or remove exiting members");
        return false;
    }

    /* Remove */
    public boolean removeHolding(String ID) {
        int holdingPos = searchArrays(ID);
        if (holdingPos >= 0) {
            holdings[holdingPos] = null;
            numberOfHoldings--;
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
            numberOfMembers--;
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
        if (holdingPos >= 0) {
            holdings[holdingPos].borrowHolding();
            members[memberPos].borrowHolding(holdings[holdingPos]);
            return true;
        } else return false;
    }

    public boolean returnHolding(String holdingID, String memberID) {
        int holdingPos = searchArrays(holdingID);
        int memberPos = searchArrays(memberID);
        DateTime current = new DateTime();
        if (holdingPos >= 0) {
            holdings[holdingPos].returnHolding(current);
            members[memberPos].returnHolding(holdings[holdingPos], current);
            return true;
        } else return false;

    }

    public void printAllHoldings() {
        for (int i = 0; i < holdings.length; i++) {
            if (holdings[i] != null) {
                holdings[i].printfResult();
            }
        }
    }

    public void printAllMembers() {
        for (int i = 0; i < members.length; i++) {
            if (members[i] != null) {
                members[i].print();
            }
        }
    }

    public void printHolding(String ID) {
        int holdingID = searchArrays(ID);
        holdings[holdingID].print();
    }

    public void printMember(String ID) {
        int memberID = searchArrays(ID);
        members[memberID].print();
    }

    public void save() {

    }

    private int searchArrays(String ID) {
        char itemType = ID.toLowerCase().charAt(0);
        if (itemType == 'v' || itemType == 'b') {
            for (int i = 0; holdings.length < i; i++) {
                if (holdings[i].getID().equals(ID)) {
                    return (i);
                }
            }
            System.out.println("Unable to find holding with that ID");
            return (-1);
        } else if (itemType == 'p' || itemType == 's') {
            for (int i = 0; members.length < i; i++) {
                if (members[i].getID().equals(ID)) {
                    return (i);
                }
            }
            System.out.println("Unable to find member with that ID");
            return -1;
        } else {
            System.out.println("Wrong ID format");
            return -1;
        }
    }

    private int firstNullArray(char type) {
        if (type == 'v' || type == 'b') {
            for (int i = 0; i < holdings.length; i++) {
                if (holdings[i] == null) {
                    return i;
                }
            }
        } else if (type == 'p' || type == 's') {
            for (int i = 0; i < members.length; i++) {
                if (holdings[i] == null) {
                    return i;
                }
            }
        }
        return -1;
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

