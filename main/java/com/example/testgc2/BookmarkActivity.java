package com.example.testgc2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/*
 this program is made by 박정조,조성재
 this project provide restaurent search operation.
 the development date : from 2017-11-27 to 2017-12-06

 */
public class BookmarkActivity extends Activity {

    DatabaseHelper myDB;
    String name ;
    ArrayAdapter listAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        final ListView listView = (ListView) findViewById(R.id.listView1);
        myDB = new DatabaseHelper(this);

        Button button = (Button) findViewById(R.id.button3);

        final ArrayList<String> theList = new ArrayList<>();
        final Cursor data = myDB.getListContents2();
        if(data.getCount() == 0 )
        {
            Toast.makeText(this,"즐겨찾기가 비어있습니다.",Toast.LENGTH_LONG).show();
        }
        else
        {
            while(data.moveToNext())
            {
                theList.add(data.getString(1));
                listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BookmarkActivity.this , SelectActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){


            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

               AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
                final int j = i;
                builder.setTitle("즐겨찾기 제거")
                        .setMessage("즐겨찾기에서 제거하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("예",new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int whichButton){
                        name = listView.getItemAtPosition(j).toString();
                        Toast.makeText(BookmarkActivity.this,"즐겨찾기에서 제거되었습니다.",Toast.LENGTH_SHORT).show();
                        myDB.deletedata(name);
                        theList.remove(j);
                        listAdapter.notifyDataSetChanged();
                    }

                })
                        .setNegativeButton("아니오",new DialogInterface.OnClickListener(){

                            public void onClick(DialogInterface dialog, int whichButton){
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                Log.d("db","long");
                return false;
            }
        });








    }

}
