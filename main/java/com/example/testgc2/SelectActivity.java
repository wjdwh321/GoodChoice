package com.example.testgc2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/*
 this program is made by 박정조,조성재
 this project provide restaurent search operation.
 the development date : from 2017-11-27 to 2017-12-06

 */
public class SelectActivity extends Activity {

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select);


        TextView textView1 = (TextView) findViewById(R.id.textView5);
        TextView textView = (TextView) findViewById(R.id.textView3);
        myDB = new DatabaseHelper(this);
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListSelect();

            if(data.getCount() == 0 )
            {
                Toast.makeText(this,"뽑을게 없습니다.",Toast.LENGTH_LONG).show();
            }
            else {
                while (data.moveToNext()) {

                    textView1.setText(data.getString(1));
                    textView.setText("\n\n주소: " + data.getString(2) + "\n\n평점: " + data.getString(5));

                }
            }
    }

}
