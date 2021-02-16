package com.example.testgc2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


/*
 this program is made by 박정조,조성재
 this project provide restaurent search operation.
 the development date : from 2017-11-27 to 2017-12-06

 */

public class MainActivity extends AppCompatActivity {

    Adapter adapter2;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button SearchButton = (Button)findViewById(R.id.Searchbutton);
        final LinearLayout slide = (LinearLayout) findViewById(R.id.slide);


        viewPager = (ViewPager)findViewById(R.id.view);
        adapter2 = new Adapter(this);
        viewPager.setAdapter(adapter2);


    }

    public void StartClick(View v){
        Intent intent = new Intent(MainActivity.this , MapsActivity.class);
        startActivity(intent);

    }


    }

