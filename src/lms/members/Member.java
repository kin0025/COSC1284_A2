
/*
*@author - Alex Kinross-Smith
*/
package lms.members;
import lms.holding.Holding;
import lms.util.DateTime;
/**
 * Created by akinr on 11/04/2016.
 */
public class Member {
    public Member(String memberID, String fullName, int credit) {

    }
protected Member(){}
    public String getID() {
    return(null);
    }

    public String getFullName() {

        return(null);}

    public boolean getStatus() {
        return(false);}

    public int getMaxCredit() {
        return(1);}

    public int calculateRemainingCredit() {
        return(1); }

    public void resetCredit() {
    }

    public boolean activate() {
        return(false);
    }

    public boolean deactivate() {
    return(false);
    }

    public Holding[] getCurrentHoldings() {
        return(null);
    }

    //    A member must maintain a collection of holdings they currently have on loan. You may implement this collection using an array or one of the Java Collection Framework (JCF) data structures, but regardless of the data structure this data should be returned to the client in the format of an array.
    public boolean updateRemainingCredit(int loanFee) {
        return(false);
    }

    public boolean checkAllowedCreditOverdraw(int loanFee) {
        return(false);}

    public boolean borrowHolding(Holding holding) {
        return(false);}/*
    A member can only be borrow a holding if:
             They are currently active in the system
     They have enough credit to pay the initial loan fee*/

    public boolean returnHolding(Holding holding, DateTime returnDate) {
        return(false);}

    //    The conditions for returning a holding are different depending on the type of member, please see the functional requirements section above.
    public void print() {
    }

    public String toString() {
        return(null);}
  /*  The member class and its sub-classes should override the toString() method to provide a pre-determined string representation of the member. The format for the member representation separates each attribute via the use of a colon ‘:’
    member_id:full_name:remaining_credit
    e.g. p00001:Joe Bloggs:25
*/
}