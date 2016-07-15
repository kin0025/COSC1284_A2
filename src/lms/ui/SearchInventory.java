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
 *
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
        ArrayList<SystemOperations> choices;
        switch (choice) {
            case 1:
                System.out.println("Enter your search term:");
                choices = searchID(input.nextLine());
                break;

            case 2:
                System.out.println("Enter your search term:");
                choices = searchTitle(input.nextLine());
                break;
            case 3:
                System.out.println("Enter your search term:");
                choices = searchName(input.nextLine());
                break;
            default:
                return null;
        }
        listSearched(choices);
        return chooseSearched(choices);
    }

    private ArrayList<SystemOperations> searchID(String searchString) {
        ArrayList<SystemOperations> searchedCandidates = new ArrayList<>();

        //Search all the holdings
        for (Holding holding : holdings) {

            //Get the string to compare
            String compare = holding.getID();

            if (searchString(compare, searchString)) {
                searchedCandidates.add(holding);

            }


        }
        //Search all the holdings
        for (Member member : members) {

            //Get the string to compare
            String compare = member.getID();

            //Check the string for something to compare to
            for (int i = 0; i < compare.length(); i++) {

                if (searchString(compare, searchString)) {
                    searchedCandidates.add(member);

                }


            }
        }

            return searchedCandidates;


        }

        private ArrayList<SystemOperations> searchTitle(String searchString){
            ArrayList<SystemOperations> searchedCandidates = new ArrayList<>();

            //Search all the holdings
            for (Holding holding : holdings) {

                //Get the string to compare
                String compare = holding.getTitle();

                if (searchString(compare, searchString)) {
                    searchedCandidates.add(holding);

                }


            }

            return searchedCandidates;


        }

        private ArrayList<SystemOperations> searchName (String searchString){
            ArrayList<SystemOperations> searchedCandidates = new ArrayList<>();


            //Search all the holdings
            for (Member member : members) {

                //Get the string to compare
                String compare = member.getFullName();

                if (searchString(compare, searchString)) {
                    searchedCandidates.add(member);

                }


            }
            return searchedCandidates;


        }

    private void listSearched(ArrayList<SystemOperations> searched) {
        for (int i = 0; i < searched.size(); i++) {
            SystemOperations operations = searched.get(i);
            System.out.println(i + ": " + operations.lineSummary());
        }

    }

    private SystemOperations chooseSearched(ArrayList<SystemOperations> searched) {
        int choice;
        boolean canGo = false;

        System.out.println("Please enter your choice");
        do {
            choice = Integer.parseInt(input.nextLine());
            if (choice < searched.size()) {
                canGo = true;
            } else {
                System.out.println("Choice not valid, try again lol");
            }
        } while (!canGo);

        return searched.get(choice);
    }

    private boolean searchString(String compare, String searchString) {
        //Check the string for something to compare to
        for (int i = 0; i < compare.length(); i++) {

            //Check each spot
            if (compare.charAt(i) == searchString.charAt(0)) {

                //Check if there is enough room to fit the search
                if ((i + searchString.length()) <= compare.length()) {

                    String comparable = compare.substring(i, i + searchString.length());

                    if (comparable.equalsIgnoreCase(searchString)) {
                        return true;
                    }

                }
            }
        }
        return false;
    }
}
