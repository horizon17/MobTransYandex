package com.example.admin3.mobtransyandex;

import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Admin3 on 14.04.2017.
 */

class CustomViewBinder implements SimpleCursorAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) throws RuntimeException {

//        try{
//            int id = view.getId();
//            switch (id) {
//               case R.id.fav:
//                    Log.v("CustomViewBinder 1");
//                    //TextView idFav = (TextView) view.findViewById(R.id.fav);
//                    Log.v("CustomViewBinder 2");
//                    //final int fav = Integer.parseInt(idFav.getText().toString());
//                    final int fav = Character.getNumericValue(((String) data).charAt(0));
//                    Log.v("CustomViewBinder 3");
//                    ImageView imageView = (ImageView) view.findViewById(R.id.iconImage);
//                    Log.v("CustomViewBinder 4");
//                    if (fav == 1) {
//                        imageView.setImageResource(R.mipmap.fav1);
//                    } else {
//                        imageView.setImageResource(R.mipmap.fav0);
//                    }
//                    return true;
//                case R.id.iconImage:
//                    ImageView iconImage = (ImageView) view;
//                    //int noteType = cursor.getInte(columnIndex);
//                    Log.v("CustomViewBinder 5");
//                    iconImage.setImageResource(R.drawable.fav1);
//            }
//            return false;
//        } catch (RuntimeException RE){
//            Log.v("RuntimeException CustomViewBinder "+RE.toString());
            return false;
//        }
    }
}
