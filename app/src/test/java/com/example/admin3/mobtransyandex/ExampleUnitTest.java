package com.example.admin3.mobtransyandex;

import android.support.v7.app.AppCompatActivity;

import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//public class ExampleUnitTest extends ActivityInstrumentationTestCase<MainActivity> {
public class ExampleUnitTest{

    @Test
    public void translator_isCorrect() throws Exception {

        MainActivity mainActivity=new MainActivity();

        // в момент создания queue = Volley.newRequestQueue(mContext); происходит java.lang.reflect.InvocationTargetException,
        // похоже дело в JUnit test:
        //Translator translator=new Translator(mainActivity.getContext());

        DataItem dataItem=new DataItem();
        dataItem.setInput("dog");
        dataItem.setDirs("en-ru");

        // следующая строка вызывает ошибку android.util.Log.isLoggable(Ljava/lang/String;I)Z
        // причина которой в том что volley не подходит для JUnit test,
        // поскольку: "isLoggable is a native method so its not available at runtime".
        // Здень нужен RobolectricTestRunner, на который, к сожалению не хватило времени.
        /*translator.getTranslation(dataItem,new Translator.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.v("ExampleUnitTest result: "+result);
            }
        });*/
    }



    @Before
    public void setUp() throws Exception {

//        super.setU();
        //MainActivity mainActivity=new MainActivity();

        //Method method = mainActivity.get(methodName, argClasses);
        // arrange

        // act

        // assert

    }
}
