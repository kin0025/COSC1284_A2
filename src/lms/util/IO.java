/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.util;

import lms.Inventory;
import lms.holding.Holding;
import lms.members.Member;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Alex on 27/05/2016 as part of s3603437_A2
 */
public class IO {
    private static final String fileExtension = Utilities.FILE_EXTENSION;
    private final Inventory inv;

    public IO(Inventory inventory) {
        inv = inventory;
    }

    /**
     * Loads the holdings.txt, members.txt and state.txt files from the folder specified relative to the location.
     *
     * @param folderName the name of the folder to be loaded from
     * @throws IOException
     */
    public void load(String folderName) throws IOException {
        File folder = new File("./" + folderName);
        //Check our folder structure is present
        folder.mkdir();

        //Create the file objects
        File holdingsFile = new File(folder.getAbsolutePath() + "\\" + "holdings" + fileExtension);
        File membersFile = new File(folder.getAbsolutePath() + "\\" + "members" + fileExtension);
        //Print this so we know where we are saving.
        System.out.println(folder.getAbsolutePath());

        //Create the reader objects
        Scanner holdingsReader = new Scanner(holdingsFile.getAbsoluteFile());
        Scanner membersReader = new Scanner(membersFile.getAbsoluteFile());

        //Load Holdings
        while (holdingsReader.hasNextLine()) {
            //Tokenize each line
            StringTokenizer holdingToken = new StringTokenizer(holdingsReader.nextLine(), ",");
            //Load each property
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
                inv.addHolding(identifier, title, loanFee, maxLoanPeriod, borrowDate, active, uniqueID);
            } else {
                System.out.println("Holding with the ID: " + identifier + " and the title: + " + title + "was not added due to a duplicate unique identifier.");
            }
        }
        //Load Members
        while (membersReader.hasNextLine()) {
            //Tokenize each line
            StringTokenizer membersToken = new StringTokenizer(membersReader.nextLine(), ",");
            //Load each property
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
                    borrowed.add(inv.uuidToHolding(holdingUUID));
                }
            }

            boolean active = Boolean.parseBoolean(membersToken.nextToken());
            String uniqueID = membersToken.nextToken();
            //Add them to the array.
            if (!IDManager.isAlreadyTaken(uniqueID)) {
                inv.addMember(identifier, name, maxCredit, balance, borrowed, active, uniqueID);
            } else {
                System.out.println("Member with the ID: " + identifier + " and the name: + " + name + "was not added due to a duplicate UUID.");
            }
        }

//Close all the things
        membersReader.close();
        holdingsReader.close();
//Recheck the arrays.
        inv.recalculateStatistics();

        //Run state checking
        try {
            //Print stuff after comparing state
            if (inv.loadLastHash(folderName).equals(inv.outputState())) {
                System.out.println("Program state was preserved since last save.");//If loading a database into an already populated database please ignore.
                System.out.println("LAST HASH: " + inv.loadLastHash(folderName));
                System.out.println("CURRENT HASH: " + inv.outputState());
            } else {
                System.out.println(Utilities.WARNING_MESSAGE + "WARNING PROGRAM STATE CHANGED ACROSS BOOT! INFORMATION MAY NOT BE THE SAME AS BEFORE. ");
                System.out.println("LAST HASH: " + inv.loadLastHash(folderName));
                System.out.println("CURRENT HASH: " + inv.outputState());
            }
        } catch (NullPointerException e) {
            System.out.println("There was no state saved last time.");
        }

    }

    /**
     * Saves the program state to holdings.txt, members.txt and state.txt
     *
     * @param folderName the name of the folder to save to relative to project root
     * @throws IOException
     */
    public void save(String folderName) throws IOException {
        File folder = new File("./" + folderName);
        //Check out folder structure is in place
        folder.mkdirs();

        //Initialise the files.
        File holdingsFile = new File(folder.getAbsolutePath() + "\\" + "holdings" + fileExtension);
        File membersFile = new File(folder.getAbsolutePath() + "\\" + "members" + fileExtension);
        File stateFile = new File(folder.getAbsolutePath() + "\\" + "state" + fileExtension);

        //Print this so we know where we are saving.
        System.out.println(folder.getAbsolutePath());

        //Create the writer objects
        FileWriter holdingsWriter = new FileWriter(holdingsFile.getAbsoluteFile());
        FileWriter membersWriter = new FileWriter(membersFile.getAbsoluteFile());
        FileWriter stateWriter = new FileWriter(stateFile.getAbsoluteFile());

        //Output each holding to file
        for (Holding holding : inv.getHoldings()) {
            if (holding != null) {
                holdingsWriter.append(holding.toFile()).append('\n');//Again IDE said performance issue without extra append, as I used to have a "+" there.
            }
        }

        //Output each member to file
        for (Member member : inv.getMembers()) {
            if (member != null) {
                membersWriter.append(member.toFile()).append("\n");
            }
        }
        //Write the output state to file
        stateWriter.append(inv.outputState());

        //Save them all
        stateWriter.flush();
        stateWriter.close();
        membersWriter.flush();
        membersWriter.close();
        holdingsWriter.flush();
        holdingsWriter.close();
    }

}
