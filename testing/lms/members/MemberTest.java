/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

package lms.members;

import lms.exceptions.InsufficientCreditException;
import lms.exceptions.ItemInactiveException;
import lms.exceptions.OnLoanException;
import lms.exceptions.TimeTravelException;
import lms.holding.Holding;
import lms.util.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Emily on 25/05/2016 as part of s3603437_A2
 */
public class MemberTest {
    Member smember;
    Member pmember;

    @Before
    public void setUp() throws Exception {
        smember = new StandardMember("name","mae");
        pmember = new PremiumMember("Name","ID");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void setID() throws Exception {

    }

    @Test
    public void setCredit() throws Exception {

    }

    @Test
    public void setName() throws Exception {

    }

    @Test
    public void getBalance() throws Exception {

    }

    @Test
    public void isActive() throws Exception {

    }

    @Test
    public void getFullName() throws Exception {

    }

    @Test
    public void getStatus() throws Exception {

    }

    @Test
    public void getID() throws Exception {

    }

    @Test
    public void getMaxCredit() throws Exception {

    }

    @Test
    public void calculateRemainingCredit() throws Exception {

    }

    @Test
    public void resetCredit() throws Exception {

    }

    @Test
    public void activate() throws Exception {

    }

    @Test
    public void deactivate() throws Exception {

    }

    @Test
    public void getCurrentHoldings() throws Exception {

    }

    @Test
    public void updateRemainingCredit() throws Exception {

    }

    @Test
    public void checkAllowedCreditOverdraw() throws Exception {

    }

    @Test
    public void borrowHolding() throws Exception {

    }

    @Test
    public void returnHolding() throws Exception {

    }

    @Test
    public void returnHoldingNoFee() throws Exception {

    }

    @Test
    public void findHolding() throws Exception {

    }

    @Test
    public void print() throws Exception {

    }

    @Test
    public void numberOfBorrowedHoldings() throws Exception {

    }

    @Test
    public void toFile() throws Exception {

    }

    @Test
    public void getUniqueID() throws Exception {

    }

    @Test
    public void setUniqueID() throws Exception {

    }

    @Test
    public void setUniqueID1() throws Exception {

    }

}