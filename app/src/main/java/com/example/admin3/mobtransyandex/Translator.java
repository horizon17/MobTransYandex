package com.example.admin3.mobtransyandex;

import android.content.Context;
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

/**
 * Created by Admin3 on 26.04.2017.
 */

public class Translator{

    private RequestQueue queue;
    private TransModel model;

    Context mContext;

    Translator(Context context){
        mContext = context;
        queue = Volley.newRequestQueue(mContext);
    }

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    public void getTranslation(final DataItem dataItem, final VolleyCallback callback){

        if (dataItem.getInput()==null || dataItem.getInput().isEmpty()){
            return;
        }
        if (dataItem.getDirs()==null){
            return;
        }

        String query;
        try {
            query= URLEncoder.encode(dataItem.getInput(),"utf-8");
        } catch (UnsupportedEncodingException e){
            query="";
        }

        final String url = Constants.Yandex_URL + Constants.Yandex_api + Constants.Yandex_key + "&text=" +query+ "&lang="+dataItem.getDirs();

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
                                callback.onSuccess(dataItem.getOutput());

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

}
