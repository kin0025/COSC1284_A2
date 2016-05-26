/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms;/*
*@author - Alex Kinross-Smith
*/

import lms.exceptions.*;
import lms.holding.Book;
import lms.holding.Holding;
import lms.holding.Video;
import lms.members.Member;
import lms.members.PremiumMember;
import lms.members.StandardMember;
import lms.util.DateTime;
import lms.util.IDManager;
import lms.util.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class Inventory {
    //Start Screen
    private final Holding[] holdings;
    private final Holding[] deletedHoldings = new Holding[5];
    private final Member[] members;
    private int numberOfHoldings = 0;
    private int numberOfMembers = 0;
    private static final String fileExtension = Utilities.FILE_EXTENSION;

    /**
     * Creates holding and member arrays of size 15
     */
    public Inventory() {
        this.holdings = new Holding[15];
        this.members = new Member[15];
    }

    /**
     * Creates holding and member arrays of specified input size
     *
     * @param holdings Size of the holdings array
     * @param members  Size of the members array
     */
    public Inventory(int holdings, int members) {
        this.holdings = new Holding[holdings];
        this.members = new Member[members];
    }

    /**
     * Gets the number of holdings already in use.
     *
     * @return number of holdings currently in inventory.
     */
    int getNumberOfHoldings() {
        return (numberOfHoldings);
    }

    /**
     * Gets the number of members already in use.
     *
     * @return number of members currently in inventory.
     */
    int getNumberOfMembers() {
        return numberOfMembers;
    }

    /**
     * Checks an ID for validity, and if it has already been taken.
     *
     * @param ID       the numerical aspect of the ID been examined.
     * @param itemType the type of item been examined
     * @return Returns one of these strings: <code>Wrong Number of Digits,ID contains characters,Already taken, Valid</code>
     */
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
     * Finds the array index of the first null value
     *
     * @param type the type of array to be examined
     * @return returns an array index of the first null value
     */
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
     * Provides a string form printout of inventory values.
     *
     * @return Returns "Holdings:"number/max "Members:" Number/max
     */
    public String infoPrintout() {
        return ("Holdings:" + numberOfHoldings + "/" + holdings.length + " | " + "Members:" + numberOfMembers + "/" + members.length);
    }

    /**
     * Creates a 6 digit ID that is valid for use and is not already used for a holding of <code>itemType</code>
     *
     * @param itemType the type of item been examined
     * @return String form 6 digit ID.
     */
    public String generateValidID(char itemType) {
        String randomID;
        do {
            randomID = Utilities.randomID();
        } while (!checkID(randomID, itemType).equalsIgnoreCase("valid"));
        return randomID;
    }

    /**
     * Recalculates the statistics for number of items.
     */
    private void recalculateStatistics() {
        numberOfHoldings = 0;
        numberOfMembers = 0;
        for (Holding holding : holdings) {
            if (holding != null) {
                numberOfHoldings++;
            }
        }
        for (Member member : members) {
            if (member != null) {
                numberOfMembers++;
            }
        }
    }

    /**
     * Searches for the array index of the holding or member with the ID.
     *
     * @param ID Search for this ID in the arrays.
     * @return array index that has an item that has a matching ID.
     */
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
            System.out.println(Utilities.INFORMATION_MESSAGE + " Unable to find holding with that ID");
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
            System.out.println(Utilities.WARNING_MESSAGE + " Wrong ID format");
            return -1;
        }
        System.out.println(Utilities.INFORMATION_MESSAGE + " Unable to find member with that ID");
        return -1;
    }

    /* Methods that add items to inventory */

    /**
     * Creates a member based on provided information.
     *
     * @param ID
     * @param itemType
     * @param title
     * @param loanFee
     * @return
     */
    public boolean addHolding(String ID, char itemType, String title, int loanFee) throws IncorrectDetailsException {
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
            } else System.out.println(Utilities.WARNING_MESSAGE + " Invalid ID");
        } else
            System.out.println(Utilities.ERROR_MESSAGE + " All holding spots are taken. Please pay for a larger subscription to support more holdings, or remove exiting holdings");
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
            System.out.println(Utilities.WARNING_MESSAGE + " Invalid ID");
        }
        System.out.println(Utilities.ERROR_MESSAGE + " All member spots are taken. Please pay for a larger subscription to support more members, or remove exiting members");
        return false;
    }

    /* Remove */
    public boolean removeHolding(String ID) {
        return removeHolding(ID, false);
    }

    public boolean removeHolding(String ID, boolean force) {
        int holdingPos = searchArrays(ID);
        boolean proceed = true;
        int[] holdingBorrowed = new int[2];
        for (int i = 0; i < members.length; i++) {
            if (proceed && members[i] != null) {
                for (int j = 0; j < members[i].getCurrentHoldings().length; j++) {
                    Holding holding = members[i].getCurrentHoldings()[j];
                    if (holding != null) {
                        if (holding.getID().equals(ID)) {
                            proceed = false;
                            holdingBorrowed[0] = i;
                            holdingBorrowed[1] = j;
                        }
                    }
                }
            }
        }
        if (proceed) {
            if (holdingPos >= 0) {
                addDeleted(holdings[holdingPos]);
                holdings[holdingPos] = null;
                recalculateStatistics();
                System.out.println(Utilities.INFORMATION_MESSAGE + " Holding " + ID + " was removed. There are " + numberOfHoldings + " holdings still in system");
            } else {
                System.out.println(Utilities.WARNING_MESSAGE + " Nothing was removed");
                proceed = false;
            }
        } else if (force) {
            if (holdingPos >= 0) {
                addDeleted(holdings[holdingPos]);
                holdings[holdingPos] = null;
                members[holdingBorrowed[0]].getCurrentHoldings()[holdingBorrowed[1]] = null;
                recalculateStatistics();
                System.out.println(Utilities.INFORMATION_MESSAGE + " Holding " + ID + " was removed from member and inventory. There are " + numberOfHoldings + " holdings still in system");
                proceed = true;
            } else {
                System.out.println(Utilities.WARNING_MESSAGE + " Nothing was removed");
                proceed = false;
            }
        }
        return proceed;
    }

    private void addDeleted(Holding deleted) {
        if (deletedHoldings[deletedHoldings.length - 1] != null) {
            IDManager.removeIdentifier(deletedHoldings[deletedHoldings.length - 1].getUUID());
        }
        //Reference IntelliJ IDEA code analyse.
        System.arraycopy(deletedHoldings, 0, deletedHoldings, 1, deletedHoldings.length - 1); //So I had the code below, and my IDE was like "Performance Issues", so I performed its recommended fix.
        /*for (int i = deletedHoldings.length - 1; 0 < i; i--) {
            deletedHoldings[i] = deletedHoldings[i - 1];
        }*/
        deletedHoldings[0] = deleted;
    }

    public boolean undeleteHolding(int deletedIndex) {

        int index = firstNullArray('b');
        if (index < 0) {
            return false;
        }
        Holding deleted = deletedHoldings[deletedIndex];
        if (deleted == null) {
            return false;
        } else {
            holdings[index] = deleted;
            deletedHoldings[deletedIndex] = null;
            return true;
        }

    }

    public void printDeleted() {
        for (int i = 0; i < deletedHoldings.length; i++) {
            if (deletedHoldings[i] != null) {
                System.out.print(i + Utilities.ANSI_RED + ": " + Utilities.ANSI_RESET);
                System.out.println(deletedHoldings[i]);
            }
        }
    }

    public boolean removeMember(String ID) {
        int memberPos = searchArrays(ID);
        if (members[memberPos].numberOfBorrowedHoldings() == 0) {
            if (memberPos >= 0) {
                IDManager.removeIdentifier(members[memberPos].getUUID());
                members[memberPos] = null;
                recalculateStatistics();
            } else {
                System.out.println(Utilities.WARNING_MESSAGE + " Nothing was removed");
                return false;
            }
            System.out.println(Utilities.INFORMATION_MESSAGE + " Member " + ID + " was removed. There are " + numberOfMembers + " members still in system");
            return true;
        } else {
            System.out.println(Utilities.INFORMATION_MESSAGE + " Member still has borrowed holdings, please remove before attempting to remove member.");
            return false;
        }
    }

    public boolean borrowHolding(String holdingID, String memberID) throws InsufficientCreditException, OnLoanException, ItemInactiveException {
        int holdingPos = searchArrays(holdingID);
        int memberPos = searchArrays(memberID);
        if (holdingPos >= 0 && memberPos >= 0) {
            members[memberPos].borrowHolding(holdings[holdingPos]);
            return true;
        } else return false;
    }

    public boolean returnHoldingNoFee(String holdingID, String memberID) throws InsufficientCreditException, OnLoanException, ItemInactiveException, TimeTravelException {
        int holdingPos = searchArrays(holdingID);
        int memberPos = searchArrays(memberID);
        if (holdingPos >= 0 && memberPos >= 0) {
            members[memberPos].returnHoldingNoFee(holdings[holdingPos], holdings[holdingPos].getBorrowDate());
            return true;
        } else return false;

    }

    public boolean returnHolding(String holdingID, String memberID, DateTime returnDate) throws InsufficientCreditException, OnLoanException, ItemInactiveException, TimeTravelException {
        int holdingPos = searchArrays(holdingID);
        int memberPos = searchArrays(memberID);
        if (holdingPos >= 0 && memberPos >= 0) {
            members[memberPos].returnHolding(holdings[holdingPos], returnDate);
            return true;
        } else return false;

    }

    public void printAllHoldings(int pageWidth) {
        for (Holding holding : holdings) {
            if (holding != null) {
                holding.print();
                UI.printCharTimes('=', pageWidth, true);
            }
        }
    }

    public void printAllMembers(int pageWidth) {
        for (Member member : members) {
            if (member != null) {
                member.print();
                UI.printCharTimes('=', pageWidth, true);
            }
        }
    }

    public void printHolding(String ID) {
        if (ID.length() != 7) {
            System.out.println(Utilities.WARNING_MESSAGE + " Incorrect ID");
            return;
        }
        int holdingID = searchArrays(ID);
        if (holdingID < 0) {
            System.out.println(Utilities.WARNING_MESSAGE + " No Holding Found");
        } else holdings[holdingID].print();
    }

    public void printMember(String ID) {
        if (ID.length() != 7) {
            System.out.println(Utilities.WARNING_MESSAGE + " Incorrect ID");
            return;
        }
        int memberID = searchArrays(ID);
        if (memberID < 0) {
            System.out.println(Utilities.WARNING_MESSAGE + " No Member Found");
        } else members[memberID].print();
    }

    public String getMemberName(String ID) {
        boolean result;
        if (ID.charAt(0) == 's' || ID.charAt(0) == 'p') {
            result = idExists(ID);
        } else {
            System.out.println(Utilities.WARNING_MESSAGE + " ID not a member. Please try again");
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
            System.out.println(Utilities.WARNING_MESSAGE + " Incorrect ID");
            return false;
        }
        if (searchArrays(ID) < 0) {
            System.out.println(Utilities.WARNING_MESSAGE + " Not Found");
            return false;
        } else
            return true;
    }

    public void replaceID(String oldID, String newID) {
        char type = oldID.charAt(0);
        int arrayIndex = searchArrays(oldID);
        if (type == 'b' || type == 'v') {
            holdings[arrayIndex].setID(newID);
        } else if (type == 'p' || type == 's') {
            holdings[arrayIndex].setID(newID);
        }
        searchArrays(oldID);
    }

    public boolean replaceName(String ID, String name) {
        int arrayPos = searchArrays(ID);
        return members[arrayPos].setName(name);
    }

    public void replaceTitle(String ID, String title) {
        int arrayPos = searchArrays(ID);
        holdings[arrayPos].setTitle(title);
    }

    public boolean activate(String ID) throws IncorrectDetailsException {
        int arrayPos = searchArrays(ID);
        if (ID.charAt(0) == 'b' || ID.charAt(0) == 'v') {
            return holdings[arrayPos].activate();
        } else {
            return members[arrayPos].activate();
        }
    }

    public boolean deactivate(String ID) {
        int arrayPos = searchArrays(ID);
        if (ID.charAt(0) == 'b' || ID.charAt(0) == 'v') {
            return holdings[arrayPos].deactivate();
        } else {
            return members[arrayPos].deactivate();
        }
    }

    public void replaceLoan(String ID, int loanFee) throws IncorrectDetailsException {
        int arrayPos = searchArrays(ID);
        Video video = (Video) holdings[arrayPos];
        video.setLoanFee(loanFee);
    }

    public void resetMemberCredit(String ID) {
        int arrayPos = searchArrays(ID);
        members[arrayPos].resetCredit();
    }

    public int getMemberBalance(String ID) {
        int balance;

        balance = (int) members[searchArrays(ID)].getBalance();

        return balance;
    }

    /**
     * Saves the program state to holdings.txt, members.txt and state.txt
     *
     * @param folderName
     * @throws IOException
     */
    public void save(String folderName) throws IOException {
        File folder = new File("./" + folderName);
        folder.mkdirs();
        File holdingsFile = new File(folder.getAbsolutePath() + "\\" + "holdings" + fileExtension);

        File membersFile = new File(folder.getAbsolutePath() + "\\" + "members" + fileExtension);

        File stateFile = new File(folder.getAbsolutePath() + "\\" + "state" + fileExtension);

        System.out.println(folder.getAbsolutePath());

        FileWriter holdingsWriter = new FileWriter(holdingsFile.getAbsoluteFile());
        FileWriter membersWriter = new FileWriter(membersFile.getAbsoluteFile());
        FileWriter stateWriter = new FileWriter(stateFile.getAbsoluteFile());

        for (Holding holding : holdings) {
            if (holding != null) {
                holdingsWriter.append(holding.toFile()).append('\n');//Again IDE said performance issue without extra append, as I used to have a "+" there.
            }
        }
        for (Member member : members) {
            if (member != null) {
                membersWriter.append(member.toFile()).append("\n");
            }
        }
        stateWriter.append(outputState());
        stateWriter.flush();
        stateWriter.close();
        membersWriter.flush();
        membersWriter.close();
        holdingsWriter.flush();
        holdingsWriter.close();
    }

    private String loadLastHash(String folder) {
        File stateFile = new File("./" + "\\" + folder + "\\state" + fileExtension);
        Scanner state = null;
        try {
            state = new Scanner(stateFile);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        try {
            if (state != null && state.hasNextLine()) {
                return state.nextLine();
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public String outputState() {
        return Utilities.hashString(toStateString());
    }

    /**
     * Loads the holdings.txt, members.txt and state.txt files from the folder specified relative to the location.
     *
     * @param folderName
     * @throws IOException
     */
    public void load(String folderName) throws IOException {
        File folder = new File("./" + folderName);
        folder.mkdir();
        File holdingsFile = new File(folder.getAbsolutePath() + "\\" + "holdings" + fileExtension);

        File membersFile = new File(folder.getAbsolutePath() + "\\" + "members" + fileExtension);

        System.out.println(folder.getAbsolutePath());

        Scanner holdingsReader = new Scanner(holdingsFile.getAbsoluteFile());
        Scanner membersReader = new Scanner(membersFile.getAbsoluteFile());
//Load Holdings
        while (holdingsReader.hasNextLine()) {
            StringTokenizer holdingToken = new StringTokenizer(holdingsReader.nextLine(), ",");
            String identifier = holdingToken.nextToken();
            String title = holdingToken.nextToken();
            int loanFee = Integer.parseInt(holdingToken.nextToken());
            int maxLoanPeriod = Integer.parseInt(holdingToken.nextToken());

            //Load the borrow time.
            String borrowTime = holdingToken.nextToken();
            DateTime borrowDate = null;
            if (!borrowTime.equals("null")) {
                StringTokenizer dateSplit = new StringTokenizer(borrowTime, "-");
                int year = Integer.parseInt(dateSplit.nextToken());
                int month = Integer.parseInt(dateSplit.nextToken());
                int day = Integer.parseInt(dateSplit.nextToken());
                borrowDate = new DateTime(day, month, year);
            }


            boolean active = Boolean.parseBoolean(holdingToken.nextToken());
            String uniqueID = holdingToken.nextToken();

            //Add them to the array.
            if (!IDManager.isAlreadyTaken(uniqueID)) {
                if (identifier.charAt(0) == 'b') {
                    holdings[firstNullArray('b')] = new Book(identifier, title, loanFee, maxLoanPeriod, borrowDate, active, uniqueID);
                } else if (identifier.charAt(0) == 'v') {
                    holdings[firstNullArray('v')] = new Video(identifier, title, loanFee, maxLoanPeriod, borrowDate, active, uniqueID);
                }
            } else {
                System.out.println("Holding with the ID: " + identifier + " and the title: + " + title + "was not added due to a duplicate unique identifier.");
            }
        }
        //Load Members
        while (membersReader.hasNextLine()) {
            StringTokenizer membersToken = new StringTokenizer(membersReader.nextLine(), ",");
            String identifier = membersToken.nextToken();
            String name = membersToken.nextToken();
            int maxCredit = Integer.parseInt(membersToken.nextToken());
            double balance = Double.parseDouble(membersToken.nextToken());
            String holdingsString = membersToken.nextToken();
            ArrayList<Holding> borrowed = new ArrayList<>();
            StringTokenizer borrowedTokens = new StringTokenizer(holdingsString, ":");
            while (borrowedTokens.hasMoreTokens()) {
                String holdingUUID = borrowedTokens.nextToken();
                if (holdingUUID != null && !holdingUUID.equals("null")) {
                    borrowed.add(uuidToHolding(holdingUUID));
                }
            }

            boolean active = Boolean.parseBoolean(membersToken.nextToken());
            String uniqueID = membersToken.nextToken();
            if (!IDManager.isAlreadyTaken(uniqueID)) {
                if (identifier.charAt(0) == 's') {
                    members[firstNullArray('s')] = new StandardMember(identifier, name, maxCredit, balance, borrowed, active, uniqueID);
                } else if (identifier.charAt(0) == 'p') {
                    members[firstNullArray('p')] = new PremiumMember(identifier, name, maxCredit, balance, borrowed, active, uniqueID);
                }
            } else {
                System.out.println("Member with the ID: " + identifier + " and the name: + " + name + "was not added due to a duplicate UUID.");
            }
        }


        membersReader.close();
        holdingsReader.close();

        recalculateStatistics();

        try

        {
            if (loadLastHash(folderName).equals(outputState())) {
                System.out.println("Program state was preserved since last save.");//If loading a database into an already populated database please ignore.
                System.out.println("LAST HASH: " + loadLastHash(folderName));
                System.out.println("CURRENT HASH: " + outputState());
            } else {
                System.out.println(Utilities.WARNING_MESSAGE + "WARNING PROGRAM STATE CHANGED ACROSS BOOT! INFORMATION MAY NOT BE THE SAME AS BEFORE. ");
                System.out.println("LAST HASH: " + loadLastHash(folderName));
                System.out.println("CURRENT HASH: " + outputState());
            }
        } catch (
                NullPointerException e
                )

        {
            System.out.println("There was no state saved last time.");
        }

    }

    private Holding uuidToHolding(String uuid) {
        for (Holding h : holdings
                ) {
            if (h != null) {
                if (h.getUUID().equals(uuid)) {
                    return h;
                }
            }
        }
        return null;
    }

    public int getHoldingReturnTime(String ID) {
        int holdingPos = searchArrays(ID);
        if (holdingPos >= 0) {
            return holdings[holdingPos].getMaxLoanPeriod();
        } else return 0;
    }

    public int getHoldingLateFee(String ID,DateTime returnDay){
        int holdingPos = searchArrays(ID);
        if (holdingPos >= 0) {
            return holdings[holdingPos].calculateLateFee(returnDay);
        } else return 0;
    }

    public String toStateString() {
        String megaString = null;
        for (Holding h : holdings
                ) {
            if (h != null) {
                megaString += h.toFile();
            }
        }
        for (Member m : members
                ) {
            if (m != null) {
                megaString += m.toFile();
            }
        }
        megaString += IDManager.stateString();
        return megaString;
    }

}

