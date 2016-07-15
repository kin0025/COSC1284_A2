/*
 * Copyright Alex Kinross-Smith (c) 2016.
 * s3603437@student.rmit.edu.au
 * alex@akinrosssmith.id.au
 */

/*
*@author - Alex Kinross-Smith
*/
package lms.ui;

import lms.members.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kin0025 on 16/07/2016.
 */
public class MenuGenerator {

    public MenuGenerator(){
        String[] options = {};
        int[] values = {};
    }


    public List<ArrayList> generateMenu(Member member,String[] menuOptions,int[] menuValues) {
        int numberChoices = 0;
        ArrayList<String> choices = new ArrayList<>();

        int memberPermissions = member.getPermissionLevel();
        for(int i=0;i<menuOptions.length;i++){
            if(menuValues[i] <= memberPermissions){
                String prefix;
                if(numberChoices < 10){
                    prefix = " " + numberChoices;
                }else{
                    prefix = numberChoices + "";
                }

                choices.add(prefix + menuOptions[i]);
                numberChoices++;
            }
        }



        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < numberChoices;i++){
            options.add(i + "");
        }
    List<ArrayList> finalList = new ArrayList<ArrayList>();

        finalList.add(choices);
        finalList.add(options);
        return finalList;
    }
}
