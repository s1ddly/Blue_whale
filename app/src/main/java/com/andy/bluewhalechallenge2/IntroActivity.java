package com.andy.bluewhalechallenge2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by STAR on 5/11/2017.
 */
public class IntroActivity extends AppCompatActivity {

    Button m_btnPlay;
    ImageButton m_btnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        SQLiteDatabase db;
        Cursor c1;

        ////////////////////////////////////////////////////////////////////
        db=openOrCreateDatabase("timeDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS timemanage7(settime Integer,curtime Integer,curday Integer,curtask Integer,curpage Integer );");

        c1=db.rawQuery("SELECT * FROM timemanage7", null);
        if(c1.getCount()==0)
        {
            db.execSQL("INSERT INTO timemanage7 VALUES(86400,0,0,0,0);");

        }

        if(c1.getCount()!=0) {
            c1.moveToLast();
            Globals.task_number=c1.getInt(3);
            int gettask = c1.getInt(4);
            switch(gettask)
            {
                case 1:
                    Intent in = new Intent(getApplicationContext(), ResultActivity.class);
                    in.putExtra("State", "AgainState");
                    startActivity(in);
                    break;
                case 7:
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(IntroActivity.this, TextActivity.class));
                    break;
/*
                case 3:
                    startActivity(new Intent(MainActivity.this, TextActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(MainActivity.this, IntroActivity.class));
                    break;
*/
                default:
                    break;
            }


/*
            if (gettask>0)
            {
                startActivity(new Intent(IntroActivity.this, ResultActivity.class));
            }
            else
            {
                if (c1.getInt(1)<86400)  {startActivity(new Intent(IntroActivity.this, ResultActivity.class));}
            }
            */
        }

        ////////////////////////////////////////////





        //Create Admob Banner
        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        m_btnPlay = (Button)findViewById(R.id.m_btnPlayID);

        m_btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntroActivity.this, SelectActivity.class));
            }
        });

        m_btnInfo = (ImageButton)findViewById(R.id.m_btnInfoID);
        m_btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(IntroActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Blue Whale Challenge");
                dialog.setMessage("The rules are simple, there will be 50 tasks in total, you will be given a task every 24 hours after completing the previous task, to prove the task has been complete you are required to upload a picture or text which will then be verified for you to progress onto the next task." );
                dialog.setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                            }
                });
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }
}