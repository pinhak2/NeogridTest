package com.neogridTest;

public class Instruction implements Comparable<Instruction> {

    Integer minutes;
    String action;

    public Instruction(int minutes, String action){
        this.minutes = minutes;
        this.action = action;
    }

    public Integer getMinutes(){
        return  minutes;
    }
    public String getAction(){
        return  action;
    }

    @Override
    public String toString()
    {
        return  this.action + " " + this.minutes + "min";
    }
    @Override
    public int compareTo(Instruction instruction) {
        return this.getMinutes().compareTo(instruction.getMinutes());
    }
}
