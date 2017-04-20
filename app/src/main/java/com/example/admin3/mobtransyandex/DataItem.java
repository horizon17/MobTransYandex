package com.example.admin3.mobtransyandex;

/**
 * Created by Admin3 on 19.04.2017.
 */

public class DataItem {

    private String input = null;
    private String output = null;
    private String dirs = null;
    private long ID = 0;
    private int fav = 0;

    public DataItem(String input, String dirs) {
        this.input = input;
        this.dirs = dirs;
    }

    public DataItem() {

    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getDirs() {
        return dirs;
    }

    public void setDirs(String dirs) {
        this.dirs = dirs;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }


}
