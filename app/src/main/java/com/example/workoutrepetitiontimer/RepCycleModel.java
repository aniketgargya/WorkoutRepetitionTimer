package com.example.workoutrepetitiontimer;

import java.util.ArrayList;

public class RepCycleModel {

    private String mName;
    private String mList;
    private String mRepetitions;
    private String mId;

    public RepCycleModel(String name, String list, String repetitions, String id){
        mName = name;
        mList = list;
        mRepetitions = repetitions;
        mId = id;
    }

    public String getName(){
        return mName;
    }

    public String getList(){
        return mList;
    }

    public String getRepetitions(){
        return mRepetitions;
    }

    public String getId(){
        return mId;
    }


    public ArrayList<Integer> getRepCycleList(){

        ArrayList<Integer> repCycleList = new ArrayList<Integer>();

        String[] splitList = mList.split("-");

        for(int i = 0; i < splitList.length; i++){

            int repPart;

            try {

                repPart = Integer.parseInt(splitList[i]);
                repCycleList.add(repPart);

            } catch (NumberFormatException e){
                e.printStackTrace();
            }

        }

        return repCycleList;
    }

}
