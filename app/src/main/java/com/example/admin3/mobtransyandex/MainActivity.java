package com.example.admin3.mobtransyandex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView mTextMessage;
    private EditText mEditText;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TransModel model;
    private LangsModel langsModel;
    private DBHelper dbHelper;
    private HashMap<String,String> LangMap;                 // "ru","Русский"
    private HashMap<String,String> LangMapInvert;           // "Русский","ru"
    private List<Map.Entry<String,String>> LangListSort;
    private ArrayList arrayData;
    private String dirs = null;

    private DataItem dataItem;

    private RequestQueue queue;

    private ImageView imageView_fav;

    @Override
    public boolean onTouch(View v, MotionEvent event) {   // пользователь скорее всего закончил вводить слово - сохраним в Истории.
        if(dataItem.getInput() != null && !dataItem.getInput().isEmpty() || dataItem.getOutput() != null && !dataItem.getOutput().isEmpty()){
            SaveHistory(dataItem);
        }
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_history:
                    SaveHistory(dataItem); // 200% что запишем )
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                    return true;
                case R.id.navigation_favorites:
                    SaveHistory(dataItem);
                    Intent intent1 = new Intent(MainActivity.this, FavoritesActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dataItem.getID()!=0) {
            outState.putLong("ID", dataItem.getID());
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long ID = savedInstanceState.getLong("ID");
        if (ID!=0) {
            dataItem = DBUtil.getDataItem(dbHelper, ID);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("main onCreate");
        setContentView(R.layout.activity_main);

        dataItem = new DataItem();
        LangMap         =new HashMap<>();
        LangMapInvert   =new HashMap<>();
        arrayData       =new ArrayList();
        LangListSort    =new ArrayList<>();

        View v = findViewById(R.id.container);
        v.setOnTouchListener(this);

        mTextMessage = (TextView) findViewById(R.id.message);
        mEditText = (EditText) findViewById(R.id.editText);

        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) findViewById(R.id.spinnerTo);

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                define_dirs();
                getTranslation(mEditText.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                define_dirs();
                getTranslation(mEditText.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView imageView_del = (ImageView) findViewById(R.id.imageViewDel);
        imageView_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataItem=new DataItem();
                set_mEditText("");
                set_mTextMessage("");
                setImageView_fav();
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.imgButtonRok);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinnerToLast=spinnerTo.getSelectedItem().toString();
                spinnerTo.setSelection(arrayData.indexOf(spinnerFrom.getSelectedItem().toString()));
                spinnerFrom.setSelection(arrayData.indexOf(spinnerToLast));
            }
        });

        imageView_fav = (ImageView) findViewById(R.id.imageView_fav);

        imageView_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveHistory(dataItem);

                DBUtil.changeFavorites(dbHelper,dataItem);
                setImageView_fav();

            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getTranslation(s.toString());

            }
        });

        // Нажатие кнопки Готово однозначно указывает на необходимость сохранить перевод
        mEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    SaveHistory(dataItem);
                }
                return false;
            }
        });

        queue = Volley.newRequestQueue(this);
        dbHelper = new DBHelper(this,"MyStore.db",null,Constants.DB_VERSION);

        getTransDirect();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);

    }

    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);

        if (dataItem.getID()!=0){
                DBUtil.getFavorites(dbHelper,dataItem);
                setImageView_fav();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private boolean define_dirs(){

        if (LangMapInvert.isEmpty() || spinnerFrom.getCount()==0 || spinnerTo.getCount()==0){
            return false;
        }

        dirs=LangMapInvert.get(spinnerFrom.getSelectedItem().toString())+"-"+LangMapInvert.get(spinnerTo.getSelectedItem().toString());

        return true;
    }

    private void setImageView_fav(){
        if (dataItem.getFav()==1){
            imageView_fav.setImageResource(R.drawable.fav1);
        } else {
            imageView_fav.setImageResource(R.drawable.fav0);
        }
    }

    private void  SpinnerSet(Spinner spinner, Boolean from){

        if (!LangListSort.isEmpty()) {

            if (arrayData.isEmpty()) {
                for (Map.Entry<String, String> entry : LangListSort) {
                    arrayData.add(entry.getValue());
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayData);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);
            if (from){
                spinner.setSelection(adapter.getPosition("Английский"));
            }else {
                spinner.setSelection(adapter.getPosition("Русский"));
            }
        }
    }

    private void getTranslation(final String in){

        if (in==null || in.isEmpty()){
            return;
        }
        if (dirs==null){
            if (!define_dirs()){
                return;
            }
        }

        if (!in.equals(dataItem.getInput())) {
            dataItem = new DataItem(in, dirs);
        }

        String query;
        try {
            query=URLEncoder.encode(in,"utf-8");
        } catch (UnsupportedEncodingException e){
            query="";
        }

        final String url = Constants.Yandex_URL + Constants.Yandex_api + Constants.Yandex_key + "&text=" +query+ "&lang="+dirs;

        StringRequest request = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {

                            Gson gson = new Gson();
                            model = gson.fromJson(s, TransModel.class);

                            if (model != null) {

                                dataItem.setOutput(model.getText());
                                set_mTextMessage(dataItem.getOutput());

                            }
                        } catch (JsonSyntaxException ex) {
                            Log.v("getMessage "+ex.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.v("onErrorResponse url "+url+" "+volleyError.getMessage());
            }
        });

        queue.add(request); // добавляем запрос на перевод в очередь на асинхронное выполнение

    }

    private void getTransDirect(){

        final String url = Constants.Yandex_URL + Constants.Yandex_getLangs + Constants.Yandex_key +"&ui=ru"; // +in

        StringRequest request = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {

                            Gson gson = new Gson();
                            langsModel = gson.fromJson(s, LangsModel.class);

                            if (langsModel != null) {

                                LangMap = langsModel.getLangs();

                                LangListSort = new ArrayList(LangMap.entrySet());
                                Collections.sort(LangListSort, new Comparator<Map.Entry<String, String>>() {
                                    @Override
                                    public int compare(Map.Entry<String, String> a, Map.Entry<String, String> b) {
                                        return (a.getValue()).compareTo(b.getValue());
                                    }
                                });

                                for (Map.Entry<String, String> entry : LangMap.entrySet()) {
                                    LangMapInvert.put(entry.getValue(), entry.getKey());
                                }

                                SpinnerSet(spinnerFrom,true);
                                SpinnerSet(spinnerTo,false);
                            }
                        } catch (JsonSyntaxException ex) {
                            Log.v("error getTransDirect fromJson " + ex.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.v("error getTransDirect onErrorResponse "+volleyError.getMessage());
            }
        });

        queue.add(request);

    }

    private void SaveHistory(DataItem dataItem){
        if(dataItem.getInput() == null || dataItem.getInput().isEmpty() || dataItem.getOutput() == null || dataItem.getOutput().isEmpty()){
            return;
        }

        if(dataItem.getID()!=0){
            return;
        }
        DBUtil.insertDataItem(dbHelper,dataItem);
    }

    private void set_mEditText(String text){
        mEditText.setText(text);
    }

    private void set_mTextMessage(String text){
        mTextMessage.setText(text);
    }


}
