/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui;

import lms.SystemOperations;
import lms.holding.Holding;
import lms.members.Member;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by kin0025 on 14/07/2016.
 */
public class SearchInventory {
    private ArrayList<Member> members;
    private ArrayList<Holding> holdings;
    private Scanner input = new Scanner(System.in);


    public SearchInventory(ArrayList<Member> members, ArrayList<Holding> holdings) {
        this.members = members;
        this.holdings = holdings;
    }

    public SystemOperations searchMenu() {
        System.out.println("Pick what you want to search:");
        System.out.println(" 1. ID");
        System.out.println(" 2. Title");
        System.out.println(" 3. Name");
        System.out.println("Enter your choice:");
        int choice = Integer.parseInt(input.nextLine());
        switch (choice) {
            case 1:
                System.out.println("Enter your search term:");
                return searchID(input.nextLine());

            case 2:
                System.out.println("Enter your search term:");
                return searchTitle(input.nextLine());
            case 3:
                System.out.println("Enter your search term:");
                return searchName(input.nextLine());
            default:
                return null;
        }
    }

    private SystemOperations searchID(String searchString) {
        ArrayList<SystemOperations> searchedCandidates = new ArrayList<>();

        //Search all the holdings
        for (Holding holding : holdings) {

            //Get the string to compare
            String compare = holding.getID();

            //Check the string for something to compare to
            for (int i = 0; compare.length() <= i; i++) {

                //Check each spot
                if (compare.charAt(i) == searchString.charAt(0)) {

                    //Check if there is enough room to fit the search
                    if ((i + searchString.length()) <= compare.length()) {

                        String comparable = compare.substring(i, i + searchString.length());

                        if (comparable.equalsIgnoreCase(searchString)) {
                            searchedCandidates.add(holding);
                        }

                    }
                }
            }


        }
        //Search all the holdings
        for (Member member : members) {

            //Get the string to compare
            String compare = member.getID();

            //Check the string for something to compare to
            for (int i = 0; compare.length() <= i; i++) {

                //Check each spot
                if (compare.charAt(i) == searchString.charAt(0)) {

                    //Check if there is enough room to fit the search
                    if ((i + searchString.length()) <= compare.length()) {

                        String comparable = compare.substring(i, i + searchString.length());

                        if (comparable.equalsIgnoreCase(searchString)) {
                            searchedCandidates.add(member);
                        }

                    }
                }
            }


        }

        for (int i = 0; i < searchedCandidates.size(); i++) {
            SystemOperations operations = searchedCandidates.get(i);
            System.out.println(i + operations.lineSummary());
        }
        int choice;
        boolean canGo = false;
        do {
            choice = input.nextInt();
            if (choice < searchedCandidates.size()) {
                canGo = true;
            }
        } while (!canGo);

        return searchedCandidates.get(choice);


    }

    private SystemOperations searchTitle(String searchString) {
        ArrayList<SystemOperations> searchedCandidates = new ArrayList<>();

        //Search all the holdings
        for (Holding holding : holdings) {

            //Get the string to compare
            String compare = holding.getTitle();

            //Check the string for something to compare to
            for (int i = 0; compare.length() <= i; i++) {

                //Check each spot
                if (compare.charAt(i) == searchString.charAt(0)) {

                    //Check if there is enough room to fit the search
                    if ((i + searchString.length()) <= compare.length()) {

                        String comparable = compare.substring(i, i + searchString.length());

                        if (comparable.equalsIgnoreCase(searchString)) {
                            searchedCandidates.add(holding);
                        }

                    }
                }
            }


        }


        for (int i = 0; i < searchedCandidates.size(); i++) {
            SystemOperations operations = searchedCandidates.get(i);
            System.out.println(i + operations.lineSummary());
        }
        int choice;
        boolean canGo = false;
        do {
            choice = input.nextInt();
            if (choice < searchedCandidates.size()) {
                canGo = true;
            }
        } while (!canGo);

        return searchedCandidates.get(choice);


    }

    private SystemOperations searchName(String searchString) {
        ArrayList<SystemOperations> searchedCandidates = new ArrayList<>();


        //Search all the holdings
        for (Member member : members) {

            //Get the string to compare
            String compare = member.getFullName();

            //Check the string for something to compare to
            for (int i = 0; compare.length() <= i; i++) {

                //Check each spot
                if (compare.charAt(i) == searchString.charAt(0)) {

                    //Check if there is enough room to fit the search
                    if ((i + searchString.length()) <= compare.length()) {

                        String comparable = compare.substring(i, i + searchString.length());

                        if (comparable.equalsIgnoreCase(searchString)) {
                            searchedCandidates.add(member);
                        }

                    }
                }
            }


        }

        for (int i = 0; i < searchedCandidates.size(); i++) {
            SystemOperations operations = searchedCandidates.get(i);
            System.out.println(i + operations.lineSummary());
        }
        int choice;
        boolean canGo = false;
        do {
            choice = input.nextInt();
            if (choice < searchedCandidates.size()) {
                canGo = true;
            }
        } while (!canGo);

        return searchedCandidates.get(choice);


    }

}
