package com.example.jenil.parsedemo;

/**
 * Created by adas on 5/10/2015.
 */
public class YourListObject {
    private String name;
    private int turn_number;

    public YourListObject(String _name, int _turn_number){
        name = _name;
        turn_number = _turn_number;
    }

    public String getName(){return name;}
    public String getTurnNumber(){return Integer.toString(turn_number);}
}
