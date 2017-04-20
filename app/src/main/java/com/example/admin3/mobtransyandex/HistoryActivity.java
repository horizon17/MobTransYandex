package com.example.admin3.mobtransyandex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView mlistView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) throws RuntimeException{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        try {

            mlistView = (ListView) findViewById(R.id.ListViewID);

            dbHelper = new DBHelper(this, "MyStore.db", null, Constants.DB_VERSION);

        } catch (RuntimeException RE) {
            Log.v("onCreate RuntimeException" + RE.toString());
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_history);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FillHistoryListView();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_history);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void FillHistoryListView() throws RuntimeException{

        try {

            List data = DBUtil.querySelectAll(dbHelper,false);

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.rowlayout,
                    new String[]{"id", "input", "output", "dirs", "cb"},
                    new int[]{R.id.id,
                            R.id.input,
                            R.id.output,
                            R.id.dirs,
                            //  R.id.iconImage - установка сюда R.drawable.fav1/R.drawable.fav0 приводило к: W/EGL_genymotion( 2508): eglSurfaceAttrib not implemented
                            R.id.checkBox}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView idView = (TextView) view.findViewById(R.id.id);
                    final long id = Long.valueOf(idView.getText().toString());
                    final DataItem dataItem = DBUtil.getDataItem(dbHelper,id);

                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                    checkBox.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            DBUtil.changeFavorites(dbHelper, dataItem);
                            FillHistoryListView();
                        }
                    });

                    return view;
                }
            };

            //adapter.setViewBinder(new CustomViewBinder());
            mlistView.setAdapter(adapter);
        } catch (RuntimeException RE) {
            Log.v("FillHistoryListView RuntimeException " + RE.toString());
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    return true;
                case R.id.navigation_history:
                    return true;
                case R.id.navigation_favorites:
                    Intent intent1 = new Intent(HistoryActivity.this, FavoritesActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                    return true;
            }
            return false;
        }

    };
}
