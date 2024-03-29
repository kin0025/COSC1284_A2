/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui;

import lms.Inventory;
import lms.ui.SearchInventory;
import lms.util.DateTime;
import lms.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by akinr on 11/04/2016 as part of s3603437_A2
 */
public class UI {
    public static final char[] MEMBER_TYPES = Utilities.MEMBER_TYPES;
    public static final char[] HOLDING_TYPES = Utilities.HOLDING_TYPES;
    public static final String[] CHOICE_OPTIONS = {"y (yes)", "n (no)", "e (exit)"};
    public static final String[] BORROW_OPTIONS = {"y (yes)", "c (change member)", "n (no)", "e (exit)"};
    public static final String[] MEMBER_OPTIONS = {"s (standard)", "p (premium)"};
    public static final String[] HOLDING_OPTIONS = {"b (book)", "v (video)"};
    public static int consoleWidth = 150;
    public static final File RUN_STATUS = new File("./running");
    public static final Scanner input = new Scanner(System.in);
    public static final Inventory inv = new Inventory();
    public static final SearchInventory searchInventory = inv.createSearch();
    //Functional Methods


    /**
     * Runs on creation of a UI object.
     * Prompts user for inventory size input.
     */
    public UI() {
        Shared.logoBoot();
    }
    
    
}
