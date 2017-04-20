package com.example.admin3.mobtransyandex;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBUtil {

    public static List querySelectAll(DBHelper dbhelper,boolean fav_only){

//        final int fav1 = R.drawable.fav1;
//        final int fav0 = R.drawable.fav0;

        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String OrderBy = "id DESC";
        Cursor cursor = db.query("notebook", null, null, null, null, null, OrderBy);

        if (fav_only){
            String[] selectionArg = new String[] {"1"};
            cursor = db.query("notebook", null, " fav=?", selectionArg, null, null, OrderBy);
        }

        List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();

        if (cursor.moveToFirst()){
            do {
                Map<String, Object> datum = new HashMap<String, Object>(2);
                datum.put("id", cursor.getString(cursor.getColumnIndex("id")));
                datum.put("input", cursor.getString(cursor.getColumnIndex("input")));
                datum.put("output", cursor.getString(cursor.getColumnIndex("output")));
                datum.put("dirs", cursor.getString(cursor.getColumnIndex("dirs")));
                String fav = cursor.getString(cursor.getColumnIndex("fav"));
                datum.put("fav", fav);
                if (fav.equals("1")){
                    datum.put("cb",true);
                }else{
                    datum.put("cb",false);
                }
                // использование след кода приводило к: W/EGL_genymotion( 2508): eglSurfaceAttrib not implemented
                // а жаль, идея хорошая, пришлось использовать чек-боксы.  ( в rowlayout.xml был (ImageView) R.id.iconImage )
                /*if (fav.equals("1")){
                    datum.put("fav",fav1);
                }else{
                    datum.put("fav",fav0);
                }*/
                arrayList.add(datum);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public static void insertDataItem(DBHelper dbhelper, DataItem dataItem){
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("input",dataItem.getInput());
        values.put("output",dataItem.getOutput());
        values.put("dirs",dataItem.getDirs());
        values.put("fav",dataItem.getFav());

        dataItem.setID(db.insert("notebook",null,values));;
    }

    public static boolean changeFavorites(DBHelper dbhelper,DataItem dataItem) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        dataItem.setFav((dataItem.getFav()==1)? 0 : 1 );
        ContentValues values = new ContentValues();
        values.put("fav", dataItem.getFav());
        return db.update("notebook", values, "id=" + dataItem.getID(), null) > 0;
    }

    public static int getFavorites(DBHelper dbhelper,DataItem dataItem) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String whereClause = "id=?";
        String [] whereArgs = {Long.toString(dataItem.getID())};
        Cursor cursor = db.query("notebook",null,whereClause,whereArgs,null,null,null);
        String fav = "0";
        if (cursor.moveToFirst()){
            fav = cursor.getString(cursor.getColumnIndex("fav"));
        }
        cursor.close();
        dataItem.setFav(Integer.parseInt(fav));
        return dataItem.getFav();
    }

    public static DataItem getDataItem(DBHelper dbhelper,long id) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String whereClause = "id=?";
        String [] whereArgs = {Long.toString(id)};
        Cursor cursor = db.query("notebook",null,whereClause,whereArgs,null,null,null);
        DataItem dataItem=new DataItem();
        if (cursor.moveToFirst()){
            dataItem.setID(Long.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
            dataItem.setInput(cursor.getString(cursor.getColumnIndex("input")));
            dataItem.setOutput(cursor.getString(cursor.getColumnIndex("output")));
            dataItem.setDirs(cursor.getString(cursor.getColumnIndex("dirs")));
            dataItem.setFav(Integer.valueOf(cursor.getString(cursor.getColumnIndex("fav"))));
        }
        cursor.close();
        return dataItem;
    }

    public static void deleteValue(DBHelper dbhelper,String deleteString){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        db.delete("notebook","input = ?",new String[]{deleteString});
    }

    public static Boolean queryDataItemExist(DBHelper dbhelper, DataItem dataItem){
        if(dataItem.getInput() == null || dataItem.getInput().isEmpty()){
            return false;
        }
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String whereClause = "input=?";
        String [] whereArgs = {dataItem.getInput()};
        Cursor cursor = db.query("notebook",null,whereClause,whereArgs,null,null,null);
        int count=cursor.getCount();
        cursor.close();
        if (count==0){
            return false;
        }else{
            return true;
        }
    }

}
