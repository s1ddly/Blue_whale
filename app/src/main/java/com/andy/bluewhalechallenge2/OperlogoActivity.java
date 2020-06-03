package com.andy.bluewhalechallenge2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class OperlogoActivity extends AppCompatActivity {
    Cursor c1;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operlogo);
        db=openOrCreateDatabase("timeDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS timemanage7(settime Integer,curtime Integer,curday Integer,curtask Integer,curpage Integer );");
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {

                if (Globals.photoAnswer[Globals.task_number])
                {

                    db.execSQL("UPDATE timemanage7 SET curpage=3");
                    startActivity(new Intent(OperlogoActivity.this, MainActivity.class));
                    finish();

                }else {
                    db.execSQL("UPDATE timemanage7 SET curpage=7");
                    startActivity(new Intent(OperlogoActivity.this, TextActivity.class));
                    finish();
                }
            }

        }, 1000);
    }
}
