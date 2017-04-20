package com.example.admin3.mobtransyandex;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;


public class LangsModel {

    private ArrayList<String> dirs;
    private HashMap langs;

    public HashMap getLangs() {
        return langs;
    }

    public void setLangs(HashMap langs) {
        this.langs = langs;
    }


    public ArrayList<String> getDirs() {
        return dirs;
    }

    public void setDirs(ArrayList<String> dirs) {
        this.dirs = dirs;
    }



}

