package com.example.admin3.mobtransyandex;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class TransModel {

    private String lang;
    private ArrayList<String> text;
    private int code;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        Charset cset = Charset.forName("UTF-8");
        String FinString="";
        for (String next : text){
            FinString=next+" "+FinString;
        }
        ByteBuffer buf = cset.encode(FinString);  //text.get(0)
        byte[] b = buf.array();
        return new String(b);
    }

    public void setText(ArrayList<String> text) {
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
