/*
*@author - Alex Kinross-Smith
*/

import lms.holding.*;
import lms.util.DateTime;

import javax.swing.text.ViewFactory;

/**
 * Created by akinr on 26/04/2016 as part of s3603437_A2
 */
@SuppressWarnings("WeakerAccess")
public class HoldingTesting {
    public static void main(String[] args) {
        Book book;
        Video video;
        try {
            book = new Book("b123456", "Book Title");
            video = new Video("v123456", "Video", 6);
            testSetID(book,video);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println((2.0 + 1.0) / 10);
        //testBorrowReturn(book, video);
    }

    public static void testSetID(Book book, Video video) {
        if (!book.setID("b123456")) {
            System.out.println("Book correct Set ID Failed");
        }
        if (book.setID("v123456")) {
            System.out.println("Book wrong type Set ID Failed");
        }
        if (book.setID("b1234")) {
            System.out.println("Book wrong number Set ID Failed");
        }
        if (book.setID("b1234ea")) {
            System.out.println("Book letters in numbers Set ID Failed");
        }
        if (!video.setID("v123456")) {
            System.out.println("Video correct Set ID Failed");
        }
        if (video.setID("b123456")) {
            System.out.println("Video wrong type Set ID Failed");
        }
        if (video.setID("v1234")) {
            System.out.println("Video wrong number Set ID Failed");
        }
        if (video.setID("v1234ea")) {
            System.out.println("Video letters in numbers Set ID Failed");
        }
    }

    public static void testBorrowReturn(Book book, Video video) {
        DateTime currentDay = new DateTime();
        DateTime nextWeek = new DateTime(7);
        DateTime oldDate = new DateTime(1, 1, 1980);
        try {
            if (!book.borrowHolding()) {
                System.out.println("borrow failed");
            }
            if (!book.returnHolding(currentDay)) {
                System.out.println("return same day failed");
            }
            if (!book.borrowHolding()) {
                System.out.println("borrow failed");
            }
            if (!book.returnHolding(nextWeek)) {
                System.out.println("return next week failed");
            }
            if (!book.borrowHolding()) {
                System.out.println("borrow failed");
            }
            if (book.returnHolding(oldDate)) {
                System.out.println("return old date failed");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
