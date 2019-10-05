package com.example.workoutrepetitiontimer;

public class TimeUtils {

    public String secondsToDisplay(int seconds){

        String display = "";

        if(((seconds - (seconds % 60)) / 60) < 10){
            display = display + 0;
        }

        display = display + ((seconds - (seconds % 60)) / 60) + ":";
        if(String.valueOf(seconds % 60).length() != 2){
            display = display + 0;
        }

        display = display + (seconds % 60);

        return display;

    }

}
